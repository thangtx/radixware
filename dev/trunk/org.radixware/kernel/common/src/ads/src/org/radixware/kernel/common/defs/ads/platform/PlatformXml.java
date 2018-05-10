/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */
package org.radixware.kernel.common.defs.ads.platform;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.ResourceLoader;
import org.apache.xmlbeans.SchemaTypeSystem;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.schema.SchemaTypeLoaderImpl;
import org.apache.xmlbeans.impl.schema.SchemaTypeSystemImpl;
import org.radixware.kernel.common.compiler.core.lookup.AdsNameEnvironment;
import org.radixware.kernel.common.defs.ads.xml.AbstractXmlDefinition;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.repository.fs.IJarDataProvider;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.utils.XmlUtils;
import org.xmlsoap.schemas.wsdl.DefinitionsDocument;

public class PlatformXml {

    private final BuildPath buildPath;
    private static final SchemaTypeSystem[] NO_STS = new SchemaTypeSystem[0];

    private class XbeansTsCache {

        private final Map<String, SchemaTypeSystem[]> uri2Sts = new HashMap<>();
        private final Map<String, SchemaTypeSystem> pool = new HashMap<>();

        public SchemaTypeSystem[] get(AbstractXmlDefinition def) {
            synchronized (uri2Sts) {
                final String namespace = def.getTargetNamespace();
                SchemaTypeSystem[] sts = uri2Sts.get(namespace);
                if (sts == null) {
                    sts = buildMapForRequest(namespace);
                }
                if (sts == null) {
                    buildSts(PlatformXml.this.buildPath.getLayer(), def.getUsageEnvironment());
                    sts = buildMapForRequest(namespace);
                }
                return sts;
            }
        }

        private SchemaTypeSystem[] buildMapForRequest(String namespace) {
            Map<String, List<SchemaTypeSystem>> map = new HashMap<>();
            for (String path : pool.keySet()) {
                SchemaTypeSystem ts = pool.get(path);
                if (ts.isNamespaceDefined(namespace)) {
                    List<SchemaTypeSystem> list = map.get(namespace);
                    if (list == null) {
                        list = new LinkedList<>();
                        map.put(namespace, list);
                    }
                    list.add(ts);
                }
            }
            for (Map.Entry<String, List<SchemaTypeSystem>> e : map.entrySet()) {
                uri2Sts.put(e.getKey(), e.getValue() == null ? NO_STS : e.getValue().toArray(NO_STS));
            }
            return uri2Sts.get(namespace);
        }

        private void buildSts(Layer layer, ERuntimeEnvironmentType env) {
            final List<AdsNameEnvironment.XBeansDataProvider> providers = new LinkedList<>();

            AdsNameEnvironment nameEnv = ((AdsSegment) layer.getAds()).getBuildPath().getPlatformLibs().getKernelLib(env).getAdsNameEnvironment();
            nameEnv.invokeRequest(new AdsNameEnvironment.XmlNameRequest() {
                @Override
                public boolean accept(AdsNameEnvironment.XBeansDataProvider provider) {
                    providers.add(provider);
                    return false;
                }
            });
            for (final AdsNameEnvironment.XBeansDataProvider provider : providers) {
                String stsName = provider.getTypeSystemName();
                IJarDataProvider dataProvider = provider.getDataSource();
                StringBuilder sb = new StringBuilder();
                if (dataProvider != null) {
                    sb.append(dataProvider.getName());
                    sb.append("#");
                }
                sb.append(stsName);
                String path = sb.toString();
                if(pool.containsKey(path)) {
                    continue;
                }
                
                final SchemaTypeSystemImpl sts = new SchemaTypeSystemImpl(new ResourceLoader() {
                    @Override
                    public InputStream getResourceAsStream(String string) {
                        String fullPath = "schemaorg_apache_xmlbeans/system/" + string;
                        return provider.getElementDataStream(fullPath);
                    }

                    @Override
                    public void close() {
                    }
                }, stsName, SchemaTypeLoaderImpl.build(null, new ResourceLoader() {
                    @Override
                    public InputStream getResourceAsStream(String string) {
                        for (AdsNameEnvironment.XBeansDataProvider provider : providers) {
                            InputStream in = provider.getElementDataStream(string);
                            if (in != null) {
                                return in;
                            }
                        }
                        return null;
                    }

                    @Override
                    public void close() {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
                }, null));
                sts.resolve();
                pool.put(path, sts);
            }
        }
    }
    private final XbeansTsCache xbeansTsCache = new XbeansTsCache();

    PlatformXml(BuildPath bp) {
        this.buildPath = bp;
    }

    public SchemaTypeSystem[] getSts(AbstractXmlDefinition definition) {
        return xbeansTsCache.get(definition);
    }

    /**
     * Tries to find xml schema file by given namespace
     */
    public InputStream findXmlSchemaDataByFileName(final String schemaFileName, final ERuntimeEnvironmentType env) {
        final Layer root = buildPath.getSegment().getLayer();
        return Layer.HierarchyWalker.walk(root, new Layer.HierarchyWalker.Acceptor<InputStream>() {
            @Override
            public void accept(Layer.HierarchyWalker.Controller<InputStream> controller, Layer layer) {
                InputStream result = findXmlSchemaDataByFileName(layer, schemaFileName, env);
                controller.setResultAndStop(result);

            }
        });
    }

    public static InputStream findXmlSchemaDataByFileName(Layer layer, final String schemaFileName, ERuntimeEnvironmentType env) {
        AdsNameEnvironment nameEnv = ((AdsSegment) layer.getAds()).getBuildPath().getPlatformLibs().getKernelLib(env).getAdsNameEnvironment();
        final InputStream[] result = new InputStream[1];
        nameEnv.invokeRequest(new AdsNameEnvironment.XmlNameRequest() {
            @Override
            public boolean accept(AdsNameEnvironment.XBeansDataProvider provider) {
                String[] sourceNames = provider.getSourceNames();
                for (String name : sourceNames) {
                    if (Utils.equals(schemaFileName, name)) {
                        result[0] = provider.getXmlSourceStream(name);
                        return true;
                    }
                }
                return false;
            }
        });
        if (result[0] != null) {
            return result[0];
        }

        return null;
    }

    public void cleanup() {
    }

    private XmlObject findXmlObjects(final String namespace, final String locationHint, final ERuntimeEnvironmentType env, final Collection<XmlObject> results) {
        final Layer root = buildPath.getSegment().getLayer();
        final char[] hint = locationHint == null ? null : locationHint.toCharArray();
        final Map<String, XmlObject> map = (results == null ? null : new HashMap<String, XmlObject>());
        XmlObject result = Layer.HierarchyWalker.walk(root, new Layer.HierarchyWalker.Acceptor<XmlObject>() {
            @Override
            public void accept(Layer.HierarchyWalker.Controller<XmlObject> controller, Layer layer) {
                XmlObject result = findXmlObjects(layer, namespace, hint, env, map);
                if (result != null && results == null) {
                    controller.setResultAndStop(result);
                }
            }
        });
        if (results != null && map != null) {
            results.addAll(map.values());
        }
        return result;

    }

    private static XmlObject findXmlObjects(final Layer layer, final String namespace, final char[] locationHint, ERuntimeEnvironmentType env, final Map<String, XmlObject> results) {
        final XmlObject[] schemaObj = new XmlObject[1];
        List<ERuntimeEnvironmentType> envs = new ArrayList<>();

        AdsNameEnvironment nameEnv = ((AdsSegment) layer.getAds()).getBuildPath().getPlatformLibs().getKernelLib(env).getAdsNameEnvironment();
        nameEnv.invokeRequest(new AdsNameEnvironment.XmlNameRequest() {
            @Override
            public boolean accept(final AdsNameEnvironment.XBeansDataProvider provider) {
                String[] sourceNames = provider.getSourceNames();
                String locationAsStr = locationHint == null ? null : String.valueOf(locationHint);
                for (final String name : sourceNames) {
                    if (locationAsStr != null && !locationAsStr.equals(name)) {
                        continue;
                    }
                    try {
                        InputStream stream = provider.getXmlSourceStream(name);
                        if (stream == null) {
                            return true;
                        }
                        try {
                            if (name.endsWith(".wsdl")) {
                                DefinitionsDocument wsdlDoc = DefinitionsDocument.Factory.parse(stream);
                                if (wsdlDoc.getDefinitions() != null && wsdlDoc.getDefinitions().getTargetNamespace() != null) {
                                    if (schemaObj != null && Utils.equals(namespace, wsdlDoc.getDefinitions().getTargetNamespace())) {
                                        schemaObj[0] = wsdlDoc;
                                        if (results != null) {
                                            String path = provider.getTypeSystemName() + "-" + name;
                                            if (!results.containsKey(path)) {
                                                results.put(path, wsdlDoc);
                                            }
                                        } else {
                                            return true;
                                        }
                                    }
                                }
                            } else {
                                org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument schema = org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Factory.parse(stream);
                                if (schema.getSchema() != null && schema.getSchema().getTargetNamespace() != null) {
                                    if (schemaObj != null && Utils.equals(namespace, schema.getSchema().getTargetNamespace())) {
                                        schemaObj[0] = schema;
                                        if (results != null) {
                                            String path = provider.getTypeSystemName() + "-" + name;
                                            if (!results.containsKey(path)) {
                                                results.put(path, schema);
                                            }
                                        } else {
                                            return true;
                                        }
                                    }
                                }
                            }
                        } finally {
                            if (stream != null) {
                                stream.close();
                            }
                        }
                    } catch (XmlException | IOException ex) {
                        Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                    }
                }
                return false;
            }
        });
        return schemaObj[0];
    }

//    public IJarDataProvider findXmlDataSource(final String namespace, final ERuntimeEnvironmentType env) {
//        AdsNameEnvironment.XmlObjectDataProvider rs = findXmlDataProvider(namespace, env);
//        return rs == null ? null : rs.getDataSource();
//    }
//
//    public AdsNameEnvironment.XmlObjectDataProvider findXmlDataProvider(final String namespace, final ERuntimeEnvironmentType env) {
//        final Layer root = buildPath.getSegment().getLayer();
//        return Layer.HierarchyWalker.walk(root, new Layer.HierarchyWalker.Acceptor<AdsNameEnvironment.XmlObjectDataProvider>() {
//            @Override
//            public void accept(Layer.HierarchyWalker.Controller<AdsNameEnvironment.XmlObjectDataProvider> controller, Layer layer) {
//                AdsNameEnvironment.XmlObjectDataProvider result = findDataSource(layer, namespace, env);
//                controller.setResultAndStop(result);
//
//            }
//        });
//    }
//
//    private static AdsNameEnvironment.XmlObjectDataProvider findDataSource(final Layer layer, final String namespace, ERuntimeEnvironmentType env) {
//        final AdsNameEnvironment.XmlObjectDataProvider[] schemaObj = new AdsNameEnvironment.XmlObjectDataProvider[1];
//        AdsNameEnvironment nameEnv = ((AdsSegment) layer.getAds()).getBuildPath().getPlatformLibs().getKernelLib(env).getAdsNameEnvironment();
//        nameEnv.invokeRequest(new AdsNameEnvironment.XmlNameRequest() {
//            @Override
//            public boolean accept(char[][] packageName, char[] className, AdsNameEnvironment.XmlObjectDataProvider provider) {
//                try {
//                    InputStream stream = provider.getSourceInputStream();
//                    try {
//                        if (CharOperation.endsWith(className, ".wsdl".toCharArray())) {
//                            DefinitionsDocument wsdlDoc = DefinitionsDocument.Factory.parse(stream);
//                            if (wsdlDoc.getDefinitions() != null && wsdlDoc.getDefinitions().getTargetNamespace() != null) {
//                                if (schemaObj != null && Utils.equals(namespace, wsdlDoc.getDefinitions().getTargetNamespace())) {
//                                    schemaObj[0] = provider;
//                                    return true;
//                                }
//                            }
//                        } else {
//                            org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument schema = org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Factory.parse(stream);
//                            if (schema.getSchema() != null && schema.getSchema().getTargetNamespace() != null) {
//                                if (schemaObj != null && Utils.equals(namespace, schema.getSchema().getTargetNamespace())) {
//                                    schemaObj[0] = provider;
//                                    return true;
//                                }
//                            }
//                        }
//                    } finally {
//                        if (stream != null) {
//                            stream.close();
//                        }
//                    }
//                } catch (XmlException | IOException ex) {
//                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
//                }
//                return false;
//            }
//        });
//        return schemaObj[0];
//    }
    public XmlObject findXmlSchema(String namespace, ERuntimeEnvironmentType env) {
        return findXmlSchema(namespace, null, env);
    }

    /**
     * Tries to find xml object containing schema for given namespace
     */
    public XmlObject findXmlSchema(String namespace, String locationHint, ERuntimeEnvironmentType env) {
        XmlObject result = findXmlObjects(namespace, locationHint, env, null);
        if (result != null && namespace.equals(XmlUtils.getTargetNamespace(result))) {
            return result;
        } else {
            return null;
        }
    }

    public void lookupXmlSchemas(String namespace, ERuntimeEnvironmentType env, Collection<XmlObject> results) {
        findXmlObjects(namespace, null, env, results);
    }
}
