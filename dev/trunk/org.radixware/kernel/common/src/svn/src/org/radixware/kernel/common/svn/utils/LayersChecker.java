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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.api.Usages2APIComparator;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.SVNRepositoryAdapter;
import org.radixware.kernel.common.svn.client.SvnEntry;
import org.radixware.kernel.common.svn.client.SvnPath;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.APIDocument;
import org.radixware.schemas.adsdef.UsagesDocument;
import org.radixware.schemas.product.Layer;
import org.radixware.schemas.product.LayerDocument;
import org.radixware.schemas.product.Module;
import org.radixware.schemas.product.ModuleDocument;

public abstract class LayersChecker implements NoizyVerifier {

    public abstract SVNRepositoryAdapter getRepository();

    public abstract long getRevision();

    //for example "distributives/696-1.2.15.12.26/release/org.radixware"
    public abstract List<String> getLayersSvnPathList();

    //for example "org.radixware"
    public abstract String[] getTopLayersURI();

    public abstract boolean wasCanceled();
    private boolean wasErrors = false;
    private long revision;
    private volatile boolean isInProgress = false;

    public LayersChecker() {
    }

    private void reportError(Exception e) {
        wasErrors = true;
        error(e);
    }

    private void reportError(String message) {
        wasErrors = true;
        error(message);
    }

    @Override
    public boolean verify() {
        if (isInProgress) {
            return false;
        }
        try {
            isInProgress = true;

            wasErrors = false;
            revision = getRevision();
            String[] topLayerUris = getTopLayersURI();

            Usages2APIComparator comparator = new Usages2APIComparator(new Usages2APIComparator.APILookup() {
                @Override
                public APIDocument lookup(String layerUri, Id moduleId) throws IOException {
                    LayerInfo layerInfo = readLayerData(layerUri);
                    if (layerInfo == null) {
                        reportError("Layer not found: " + layerUri);
                        throw new IOException("Unable to read layer information for layer " + layerUri);
                    } else {
                        return layerInfo.getAPIDocument(moduleId);
                    }
                }

                @Override
                public boolean isExpired(String layerUri, String expirationRelease) {
                    return false;
                }
                
            }, new Usages2APIComparator.UsagesLookup() {
                @Override
                public UsagesDocument.Usages lookup(String layerUri, Id moduleId) throws IOException {
                    LayerInfo layerInfo = readLayerData(layerUri);
                    if (layerInfo == null) {
                        reportError("Layer not found: " + layerUri);
                        throw new IOException("Unable to read layer information for layer " + layerUri);
                    } else {
                        return layerInfo.getUsages(moduleId);
                    }
                }
            }, new Usages2APIComparator.Reporter() {
                @Override
                public void message(EEventSeverity severity, String message) {
                    LayersChecker.this.error(message);
                }
            });
            //1. check usages2api compatibility
            for (String topLayerUri : topLayerUris) {
                checkLayer(topLayerUri, comparator);
            }

            return !wasErrors;
        } finally {
            isInProgress = false;
        }
    }

    private boolean checkLayer(String layerURI, Usages2APIComparator comparator) {
        if (wasCanceled()) {
            return false;
        }
        LayerInfo layer = readLayerData(layerURI);
        if (layer == null) {
            reportError("Layer not found: " + layerURI);
            return false;
        }
        if (wasCanceled()) {
            return false;
        }
        layer.uploadModules();
        for (ModuleInfo info : layer.modules.values()) {
            if (wasCanceled()) {
                return false;
            }
            if (!comparator.isModuleCompatible(layerURI, Id.Factory.loadFrom(info.id))) {
                return false;
            }
        }

        List<String> baseLayers = new LinkedList<>();
        if (layer.xLayer.isSetBaseLayerURIs()) {
            baseLayers.addAll(layer.xLayer.getBaseLayerURIs());
        } else if (layer.xLayer.isSetPrevLayerUri()) {
            baseLayers.add(layer.xLayer.getPrevLayerUri());
        }
        if (baseLayers.isEmpty()) {
            return true;
        } else {
            for (String bl : baseLayers) {
                if (wasCanceled()) {
                    return false;
                }
                if (!checkLayer(bl, comparator)) {
                    return false;
                }
            }
        }
        return true;
    }

    private class ModuleInfo {

        private Module xModule;
        private String path;
        private String id;
        private APIDocument xApi;
        private UsagesDocument.Usages xUsages;

        public ModuleInfo(Module xModule, String path) {
            this.xModule = xModule;
            this.path = path;
            this.id = xModule.getId();
        }

        public APIDocument getAPIDocument() throws IOException {
            if (xApi == null) {
                InputStream in = readModuleFile("api.xml");
                if (in != null) {
                    try {
                        xApi = APIDocument.Factory.parse(in);
                    } catch (XmlException | IOException ex) {
                        reportError(ex);
                        throw new IOException("Unable to load api declaration of module " + xModule.getName() + "(" + xModule.getId() + ")");
                    } finally {
                        try {
                            in.close();
                        } catch (IOException ex) {
                        }
                    }
                }
            }
            return xApi;
        }

        public UsagesDocument.Usages getUsages() throws IOException {
            if (xUsages == null) {
                InputStream in = readModuleFile("usages.xml");
                if (in != null) {
                    try {
                        xUsages = UsagesDocument.Factory.parse(in).getUsages();
                    } catch (XmlException | IOException ex) {
                        reportError(ex);
                        throw new IOException("Unable to load usages list of module " + xModule.getName() + "(" + xModule.getId() + ")");
                    } finally {
                        try {
                            in.close();
                        } catch (IOException ex) {
                        }
                    }
                } else {
                    throw new IOException("Unable to load usages list of module " + xModule.getName() + "(" + xModule.getId() + "): file not found");
                }
            }
            return xUsages;
        }

        private InputStream readModuleFile(String fileName) {
            try {

                String moduelXmlPath = SvnPath.append(path, fileName);
                if (getRepository().isFile(moduelXmlPath, revision)) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    try {
                        getRepository().getFile(moduelXmlPath, revision, null, out);
                        return new ByteArrayInputStream(out.toByteArray());
                    } catch (RadixSvnException e) {
                        reportError(e);
                        return null;
                    } finally {
                        try {
                            out.close();
                        } catch (IOException ex) {
                        }
                    }
                } else {
                    return null;
                }

            } catch (RadixSvnException e) {
                reportError(e);
                return null;
            }
        }
    }

    private class LayerInfo {

        private Layer xLayer;
        private String layerSvnPath;
        private String uri;
        private Map<String, ModuleInfo> modules;

        public LayerInfo(Layer xLayer, String layerSvnPath) {
            this.xLayer = xLayer;
            this.layerSvnPath = layerSvnPath;
            this.uri = xLayer.getUri();
        }

        public APIDocument getAPIDocument(Id moduleId) throws IOException {
            uploadModules();
            ModuleInfo info = modules.get(moduleId.toString());
            if (info != null) {
                return info.getAPIDocument();
            }
            return null;
        }

        public UsagesDocument.Usages getUsages(Id moduleId) throws IOException {
            uploadModules();
            ModuleInfo info = modules.get(moduleId.toString());
            if (info != null) {
                return info.getUsages();
            }
            throw new IOException("Unable to find module information for module " + moduleId.toString());
        }

        private void uploadModules() {
            if (modules == null) {
                modules = new HashMap<>();
                String adsPath = SvnPath.append(layerSvnPath, "ads");
                
                boolean findAds; //empty layer - RADIXMANAGER-392
                try {
                    findAds = getRepository().isDir(adsPath, revision);
                }
                catch (RadixSvnException ex) {
                    reportError(ex);
                    findAds = false;
                }
                    
                    
                if (findAds) {
                    final List<String> candidates = new LinkedList<>();
                    try {
                        getRepository().getDir(adsPath, revision,
                                new SVNRepositoryAdapter.EntryHandler() {

                                    @Override
                                    public void accept(SvnEntry entry) throws RadixSvnException {
                                        candidates.add(entry.getName());
                                    }
                                }
                        );

                        for (String candidate : candidates) {
                            String path = SvnPath.append(adsPath, candidate);
                            String moduelXmlPath = SvnPath.append(path, "module.xml");
                            if (getRepository().isFile(moduelXmlPath, revision)) {
                                ByteArrayOutputStream out = new ByteArrayOutputStream();
                                try {
                                    getRepository().getFile(moduelXmlPath, revision, null, out);
                                    ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
                                    try {
                                        Module xModule = ModuleDocument.Factory.parse(in).getModule();
                                        if (xModule != null) {
                                            modules.put(xModule.getId(), new ModuleInfo(xModule, path));
                                        }
                                    } finally {
                                        try {
                                            in.close();
                                        } catch (IOException e) {
                                        }
                                    }
                                } catch (RadixSvnException | XmlException | IOException e) {
                                    reportError(e);
                                    continue;
                                } finally {
                                    try {
                                        out.close();
                                    } catch (IOException ex) {
                                    }
                                }
                            }
                        }
                    } catch (RadixSvnException ex) {
                        reportError(ex);
                    }
                }
            }
        }
    }
    private Map<String, LayerInfo> loadedLayers;

    private LayerInfo readLayerData(String layerUri) {
        if (loadedLayers == null) {
            loadedLayers = new HashMap<>();
            for (String layerPath : getLayersSvnPathList()) {
                String layerXmlFilePath = SvnPath.append(layerPath, "layer.xml");
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                try {
                    getRepository().getFile(layerXmlFilePath, revision, null, out);
                    ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
                    try {
                        Layer xLayer = LayerDocument.Factory.parse(in).getLayer();
                        if (xLayer != null) {
                            loadedLayers.put(xLayer.getUri(), new LayerInfo(xLayer, layerPath));
                        }
                    } finally {
                        try {
                            in.close();
                        } catch (IOException e) {
                        }
                    }
                } catch (RadixSvnException | XmlException | IOException e) {
                    reportError(e);
                    continue;
                } finally {
                    try {
                        out.close();
                    } catch (IOException ex) {
                    }
                }
            }
        }
        return loadedLayers.get(layerUri);
    }
}
