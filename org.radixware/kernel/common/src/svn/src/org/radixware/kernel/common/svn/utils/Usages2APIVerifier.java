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

package org.radixware.kernel.common.svn.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipFile;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.api.Usages2APIComparator;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.lang.ClassLinkageAnalyzer;
import org.radixware.kernel.common.svn.SVNRepositoryAdapter;
import org.radixware.kernel.common.svn.utils.Utils.LayerInfo;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Pair;
import org.radixware.kernel.common.utils.ReleaseUtilsCommon;
import org.radixware.schemas.adsdef.APIDocument;
import org.radixware.schemas.adsdef.AdsUserFuncDefinitionDocument;
import org.radixware.schemas.adsdef.ClassDefinition;
import org.radixware.schemas.adsdef.UsagesDocument;
import org.radixware.schemas.adsdef.UsagesDocument.Usages;


public abstract class Usages2APIVerifier extends Usages2APIComparator implements NoizyVerifier, IContextVerifier, IDefinitionNameResolver {

    private final BranchHolderParams branchParams;
    private IVerifyContext context;
    private boolean verbose = false;
    private BranchHolder holder;
    private boolean stopOnFirstError = true;
    private final Set<Utils.ModuleInfo> modulesWithoutAPI = new HashSet<>();
    private final Map<Id, CacheDefInfo> defId2InfoCache = new HashMap<>();
    
    private static class SVNLookup {

        Usages2APIVerifier verifier;

        protected Utils.ModuleInfo findModule(String layerUri, Id moduleId) {
            LayerInfo layer = verifier.holder.layers.get(layerUri);
            if (layer == null) {
                return null;
            } else {
                return (Utils.ModuleInfo) layer.findModule(moduleId);
            }
        }
        
        public boolean isExpired(String layerUri, String expirationRelease) throws IOException {
            final LayerInfo layer = verifier.holder.layers.get(layerUri);
            if (layer == null) {
                throw new IOException("Layer info for uri: " + layerUri);
            } else {
                return ReleaseUtilsCommon.isExpired(layer.getReleaseNumber(), expirationRelease);
            }
        }
        
    }

    private static class SVNApiLookup extends SVNLookup implements Usages2APIComparator.APILookup {

        private final Map<Utils.ModuleInfo, APIDocument> loadedAPIs = new HashMap<>();

        @Override
        public APIDocument lookup(String layerUri, Id moduleId) throws IOException {

            Utils.ModuleInfo module = findModule(layerUri, moduleId);
            if (module == null) {
                return null;
            } else {
                APIDocument doc = loadedAPIs.get(module);
                if (doc == null) {
                    byte[] bytes = module.getAPIXmlData();
                    if (bytes == null) {
                        return null;
                    } else {
                        InputStream stream = new ByteArrayInputStream(bytes);
                        try {
                            try {
                                doc = APIDocument.Factory.parse(stream);
                                loadedAPIs.put(module, doc);
                            } catch (XmlException ex) {
                                throw new IOException("Invalid api.xml file format in module " + module.getDisplayName());
                            }
                        } finally {
                            try {
                                stream.close();
                            } catch (IOException e) {
                            }
                        }
                    }
                }
                return doc;
            }

        }
    }

    private static class SVNUsagesLookup extends SVNLookup implements Usages2APIComparator.UsagesLookup {

        @Override
        public Usages lookup(String layerUri, Id moduleId) throws IOException {
            Utils.ModuleInfo module = findModule(layerUri, moduleId);
            if (module == null) {
                return null;
            } else {
                byte[] bytes = module.getUsagesXmlData();
                if (bytes == null) {
                    return null;
                } else {
                    InputStream stream = new ByteArrayInputStream(bytes);
                    try {
                        try {
                            return UsagesDocument.Factory.parse(stream).getUsages();
                        } catch (XmlException ex) {
                            throw new IOException("Invalid usages.xml file format in module " + module.getDisplayName());
                        }
                    } finally {
                        try {
                            stream.close();
                        } catch (IOException e) {
                        }
                    }
                }
            }

        }
    }

    private static class VerifierReporter implements Usages2APIComparator.Reporter {

        private Usages2APIVerifier verifier;

        public VerifierReporter() {
        }

        @Override
        public void message(EEventSeverity severity, String message) {
            verifier.processMessage(severity, message);
        }
    }
    
    public Usages2APIVerifier(SVNRepositoryAdapter curRepository, String curBranchPath, ZipFile upgradeFile, boolean skipDevelopmentLayers, String predefinedBaseDevLayerURI) {
        this(new BranchHolderParams(curRepository, curBranchPath, null, upgradeFile, null, skipDevelopmentLayers, predefinedBaseDevLayerURI));
    }
    
    public Usages2APIVerifier(BranchHolderParams branchParams) {
        this(branchParams, new SVNApiLookup(), new SVNUsagesLookup(), new VerifierReporter());
    }
        
    private Usages2APIVerifier(BranchHolderParams branchParams, SVNApiLookup apiLookup, SVNUsagesLookup usagesLookup, VerifierReporter reporter) {
        super(apiLookup, usagesLookup, reporter);
        reporter.verifier = this;
        apiLookup.verifier = this;
        usagesLookup.verifier = this;
        this.branchParams = branchParams;
    }
    
    
    @Override
    public String resolveName(Id id) {
        if (!defId2InfoCache.containsKey(id)) {
            final Set<LayerInfo> processedLayers = new HashSet<>();
            CacheDefInfo def = null;
            for (LayerInfo topLayer : holder.getTopLayers()) {
                def = processLayerForUfVerify(topLayer, processedLayers, id, true);
            }
            if (def == null) {
                def = new CacheDefInfo(id.toString(), null);
            }
            defId2InfoCache.put(id, def);
        }
        return defId2InfoCache.get(id).name;
    }
    
    protected void processMessage(EEventSeverity severity, String message) {
        error(message);
    }
    
    @Override
    public boolean verify() {
        BranchHolder h = new BranchHolder(this, verbose);
        try {
            h.initialize(branchParams);
            return verifyExternalHolder(h);
        } finally {
            h.finish();
        }
    }

    public  boolean verifyExternalHolder(BranchHolder holder) {
        this.holder = holder;

        for (LayerInfo layer : holder.layers.values()) {
            for (Utils.ModuleInfo module : layer.modules.values()) {
                if (wasCancelled())
                    return false;
                if (!isModuleCompatible(layer.getURI(), module.getId())) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean verifyUserFunctions(final Connection c) {
        BranchHolder h = new BranchHolder(this, verbose);
        try {
            h.initialize(branchParams);
            return verifyUserFunctions(c, h);
        } finally {
            h.finish();
        }
    }
    
    private CacheDefInfo processLayerForUfVerify(LayerInfo topLayer, LayerInfo layer, Set<LayerInfo> processedLayers, Id classId) {
        if (!processedLayers.contains(layer)) {
            for (Utils.ModuleInfo module : layer.modules.values()) {
                if (modulesWithoutAPI.contains(module)) {
                    continue;
                }
                try {
                    APIDocument xApiDoc = apiLookup.lookup(layer.getURI(), module.getId());
                    if (xApiDoc != null) {
                        ClassDefinition xClassDef = findUserFuncClass(xApiDoc.getAPI(), classId);
                        if (xClassDef != null) {
                            final CacheDefInfo defInfo = new CacheDefInfo(xClassDef.getName(), new Pair<>(topLayer, module));
                            defId2InfoCache.put(classId, defInfo);
                            return defInfo;
                        }
                    } else {
                        error("Not found api.xml for module: " + module.getDisplayName());
                        modulesWithoutAPI.add(module);
                    }
                } catch (IOException ex) {
                }
            }
            processedLayers.add(layer);
        }
        return null;
    }

    private CacheDefInfo processLayerForUfVerify(LayerInfo topLayer, Set<LayerInfo> processedLayers, Id classId, boolean rootMode) {
        if (rootMode) {
            if (defId2InfoCache.containsKey(classId)) {
                return defId2InfoCache.get(classId);
            }
            
            CacheDefInfo top = processLayerForUfVerify(topLayer, topLayer, processedLayers, classId);
            if (top != null) {
                return top;
            }
        }
        List<ClassLinkageAnalyzer.LayerInfo> prevs = topLayer.findPrevLayer();
        for (ClassLinkageAnalyzer.LayerInfo info : prevs) {
            CacheDefInfo top = processLayerForUfVerify(topLayer, (LayerInfo) info, processedLayers, classId);
            if (top != null) {
                return top;
            }
        }
        for (ClassLinkageAnalyzer.LayerInfo info : prevs) {
            CacheDefInfo top = processLayerForUfVerify((LayerInfo) info, processedLayers, classId, false);
            if (top != null) {
                return top;
            }
        }
        return null;

    }

    boolean verifyUserFunctions(final Connection c, BranchHolder holder) {
        this.holder = holder;
        try (PreparedStatement stmt = c.prepareStatement("select javasrc,classguid,updefid,upownerentityid,upownerpid, id from rdx_userfunc where javaruntime is not null order by id desc")) {
            try (ResultSet rs = stmt.executeQuery()) {
                List<String> ufWithoutUsages = new LinkedList<>();
                while (rs.next()) {
                    if (wasCancelled()) {
                        return false;
                    }
                    String javaSrc = rs.getString(1);
                    Id classId = Id.Factory.loadFrom(rs.getString(2));
                    String updefid = rs.getString(3);
                    String upownerid = rs.getString(4);
                    String upownerpid = rs.getString(5);
                    final int ufId = rs.getInt(6);
                    String userFuncName = upownerid + ":" + upownerpid + ":" + updefid;
                    if (javaSrc != null) {
                        try {
                            setContext(new UserFuncContext(ufId, upownerid, upownerpid, updefid, classId));
                            if (wasCancelled()) {
                                return false;
                            }
                            AdsUserFuncDefinitionDocument xDoc = AdsUserFuncDefinitionDocument.Factory.parse(javaSrc);
                            List<LayerInfo> topLayerInfos = Utils.topLayers(holder.layers.values());
                            Set<LayerInfo> processedLayers = new HashSet<>();
                            CacheDefInfo userFuncLayerAndModule = null;
                            boolean userFuncFound = false;
                            for (LayerInfo info : topLayerInfos) {
                                if (wasCancelled()) {
                                    return false;
                                }
                                userFuncLayerAndModule = processLayerForUfVerify(info, processedLayers, classId, true);
                                userFuncFound = userFuncLayerAndModule != null && userFuncLayerAndModule.path != null;
                                if (userFuncLayerAndModule != null) {
                                    break;
                                }
                            }
                            if (userFuncFound) {
                                Usages xUsages = xDoc.getAdsUserFuncDefinition().getUsages();
                                if (xUsages != null) {
                                    if (!isUsagesCompatible(xUsages, userFuncLayerAndModule.path.getFirst().getURI(), userFuncLayerAndModule.path.getSecond().id)) {
                                        if (stopOnFirstError) {
                                            return false;
                                        }
                                    }
                                } else {
                                    ufWithoutUsages.add(userFuncName);
                                }
                            } else {
                                final String errMsg = String.format("Base class %s not found for user function %s", classId.toString(), userFuncName);
                                if (holder.developmentLayers.isEmpty()) {
                                    error(errMsg);
                                    if (stopOnFirstError) {
                                        return false;
                                    }
                                } else {
                                    error(errMsg + ". Base class in development layer?");
                                }

                            }
                        } catch (XmlException ex) {
                            error(new RadixError("Invalid format of user function " + userFuncName, ex));
                            return false;
                        } finally {
                            leaveContext();
                        }
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            error(new RadixError("Database error", e));
            return false;
        }
    }

    public void setStopOnFirstError(boolean stopOnFirstError) {
        this.stopOnFirstError = stopOnFirstError;
    }

    @Override
    public void setContext(IVerifyContext ctx) {
        this.context = ctx;
    }

    @Override
    public void leaveContext() {
        this.context = null;
    }
    
    @Override
    public IVerifyContext getContext() {
        return context;
    }
    
    private static class CacheDefInfo {
        
        final String name;
        final Pair<LayerInfo, Utils.ModuleInfo> path;

        public CacheDefInfo(String name, Pair<LayerInfo, Utils.ModuleInfo> pair) {
            this.name = name;
            this.path = pair;
        }
        
    }
}
