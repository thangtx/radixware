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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipFile;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.api.Usages2APIComparator;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.lang.ClassLinkageAnalyzer;
import org.radixware.kernel.common.svn.SVNRepositoryAdapter;
import org.radixware.kernel.common.svn.utils.Utils.LayerInfo;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.APIDocument;
import org.radixware.schemas.adsdef.AdsUserFuncDefinitionDocument;
import org.radixware.schemas.adsdef.ClassDefinition;
import org.radixware.schemas.adsdef.UsagesDocument;
import org.radixware.schemas.adsdef.UsagesDocument.Usages;


public abstract class Usages2APIVerifier extends Usages2APIComparator implements NoizyVerifier {

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
    }

    private static class SVNApiLookup extends SVNLookup implements Usages2APIComparator.APILookup {

        private final Map<Utils.ModuleInfo, APIDocument> loadedAPIs = new HashMap<Utils.ModuleInfo, APIDocument>();

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

        private NoizyVerifier verifier;

        public VerifierReporter() {
        }

        @Override
        public void message(String message) {
            verifier.error(message);
        }
    }
    private final SVNRepositoryAdapter repository;
    private final String branchPath;
    private final ZipFile zipFile;
    private final String zipFilePath;
    private final boolean skipDevelopmentLayers;
    private final String predefinedBaseDevLayerURI;
    private boolean verbose = false;
    private BranchHolder holder;

    public Usages2APIVerifier(SVNRepositoryAdapter repository, String branchPath, ZipFile zipFile, boolean skipDevelopmentLayers, String predefinedBaseDevLayerURI) {
        this(repository, branchPath, new SVNApiLookup(), new SVNUsagesLookup(), new VerifierReporter(), skipDevelopmentLayers, predefinedBaseDevLayerURI, zipFile, null);
    }

    public Usages2APIVerifier(SVNRepositoryAdapter repository, String branchPath, String zipFilePath, boolean skipDevelopmentLayers, String predefinedBaseDevLayerURI) {
        this(repository, branchPath, new SVNApiLookup(), new SVNUsagesLookup(), new VerifierReporter(), skipDevelopmentLayers, predefinedBaseDevLayerURI, null, zipFilePath);
    }

    Usages2APIVerifier(SVNRepositoryAdapter repository, String branchPath, ZipFile zipFile, String zipFilePath, boolean skipDevelopmentLayers, String predefinedBaseDevLayerURI) {
        this(repository, branchPath, new SVNApiLookup(), new SVNUsagesLookup(), new VerifierReporter(), skipDevelopmentLayers, predefinedBaseDevLayerURI, zipFile, zipFilePath);
    }

    private Usages2APIVerifier(SVNRepositoryAdapter repository, String branchPath, SVNApiLookup apiLookup, SVNUsagesLookup usagesLookup, VerifierReporter reporter, boolean skipDevelopmentLayers, String predefinedBaseDevLayerURI, ZipFile zipFile, String zipFilePath) {
        super(apiLookup, usagesLookup, reporter);
        reporter.verifier = this;
        apiLookup.verifier = this;
        usagesLookup.verifier = this;
        this.repository = repository;
        this.branchPath = branchPath;
        this.skipDevelopmentLayers = skipDevelopmentLayers;
        this.zipFile = zipFile;
        this.zipFilePath = zipFilePath;
        this.predefinedBaseDevLayerURI = predefinedBaseDevLayerURI;
    }

    @Override
    public boolean verify() {
        BranchHolder h = new BranchHolder(this, verbose);
        try {
            h.initialize(repository, branchPath, zipFile, zipFilePath, predefinedBaseDevLayerURI, skipDevelopmentLayers);
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
            h.initialize(repository, branchPath, zipFile, zipFilePath, predefinedBaseDevLayerURI, skipDevelopmentLayers);
            return verifyUserFunctions(c, h);
        } finally {
            h.finish();
        }
    }

    class UserFuncInfo {
    }

    private LayerInfo processLayerForUfVerify(LayerInfo topLayer, LayerInfo layer, Set<LayerInfo> processedLayers, Id classId) {
        if (!processedLayers.contains(layer)) {
            for (Utils.ModuleInfo module : layer.modules.values()) {
                try {
                    APIDocument xApiDoc = apiLookup.lookup(layer.getURI(), module.getId());
                    ClassDefinition xClassDef = findUserFuncClass(xApiDoc.getAPI(), classId);
                    if (xClassDef != null) {
                        return topLayer;
                    }
                } catch (IOException ex) {
                }
            }
            processedLayers.add(layer);
        }
        return null;
    }

    private LayerInfo processLayerForUfVerify(LayerInfo topLayer, Set<LayerInfo> processedLayers, Id classId, boolean rootMode) {
        if (rootMode) {
            LayerInfo top = processLayerForUfVerify(topLayer, topLayer, processedLayers, classId);
            if (top != null) {
                return top;
            }
        }
        List<ClassLinkageAnalyzer.LayerInfo> prevs = topLayer.findPrevLayer();
        for (ClassLinkageAnalyzer.LayerInfo info : prevs) {
            LayerInfo top = processLayerForUfVerify(topLayer, (LayerInfo) info, processedLayers, classId);
            if (top != null) {
                return top;
            }
        }
        for (ClassLinkageAnalyzer.LayerInfo info : prevs) {
            LayerInfo top = processLayerForUfVerify((LayerInfo) info, processedLayers, classId, false);
            if (top != null) {
                return top;
            }
        }
        return null;

    }

    boolean verifyUserFunctions(final Connection c, BranchHolder holder) {
        this.holder = holder;

        try {
            PreparedStatement stmt = c.prepareStatement("select javasrc,classguid,updefid,upownerentityid,upownerpid from rdx_userfunc where javaruntime is not null");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                if (wasCancelled())
                    return false;
                String javaSrc = rs.getString(1);
                Id classId = Id.Factory.loadFrom(rs.getString(2));
                String updefid = rs.getString(3);
                String upownerid = rs.getString(4);
                String upownerpid = rs.getString(5);
                String userFuncName = upownerid + ":" + upownerpid + ":" + updefid;
                if (javaSrc != null) {
                    try {
                        if (wasCancelled())
                            return false;
                        AdsUserFuncDefinitionDocument xDoc = AdsUserFuncDefinitionDocument.Factory.parse(javaSrc);
                        List<LayerInfo> topLayerInfos = Utils.topLayers(holder.layers.values());
                        Set<LayerInfo> processedLayers = new HashSet<>();
                        LayerInfo userFuncTopLayer = null;
                        boolean userFuncFound = false;
                        for (LayerInfo info : topLayerInfos) {
                            if (wasCancelled())
                                return false;
                            userFuncTopLayer = processLayerForUfVerify(info, processedLayers, classId, true);
                            if (userFuncTopLayer != null) {
                                userFuncFound = true;
                            }
                            if (userFuncFound) {
                                break;
                            }
                        }
                        if (userFuncFound) {
                            Usages xUsages = xDoc.getAdsUserFuncDefinition().getUsages();
                            if (xUsages != null) {
                                if (!isUsagesCompatible(xUsages, userFuncTopLayer.getURI(), Id.Factory.newInstance(EDefinitionIdPrefix.MODULE))) {
                                    return false;
                                }
                            } else {
                                error("Warning: no usages information found for user function " + userFuncName);
                            }
                        } else {
                            if (holder.developmentLayers.isEmpty()) {
                                error("Base class not found for user function: " + userFuncName);
                                return false;
                            } else {
                                error("Warning: base class not found for user function: " + userFuncName + ". Base class in development layer?");
                            }

                        }
                    } catch (XmlException ex) {
                        error(new RadixError("Invalid format of user function " + userFuncName, ex));
                        return false;
                    }
                }
            }
            return true;
        } catch (SQLException e) {
            error(new RadixError("Database error", e));
            return false;
        }
    }
}
