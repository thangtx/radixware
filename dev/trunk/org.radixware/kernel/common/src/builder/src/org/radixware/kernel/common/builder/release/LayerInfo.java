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
package org.radixware.kernel.common.builder.release;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.builder.BuildActionExecutor;
import org.radixware.kernel.common.builder.DirectoryFileChecker;
import org.radixware.kernel.common.builder.api.IProgressHandle;
import org.radixware.kernel.common.builder.check.common.CheckOptions;
import org.radixware.kernel.common.builder.check.common.Checker;
import org.radixware.kernel.common.builder.radixdoc.RadixdocOptions;
import org.radixware.kernel.common.builder.radixdoc.RadixdocSpecificator;
import org.radixware.kernel.common.builder.release.ReleaseUtils.ReleaseInfo;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.build.IFlowLogger;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.dds.DdsModule;
import org.radixware.kernel.common.defs.uds.module.UdsModule;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.radixdoc.DictionaryFactories;
import org.radixware.kernel.common.radixdoc.IRadixdocDictionary;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.repository.dds.DdsScripts;
import org.radixware.kernel.common.repository.dds.DdsSegment;
import org.radixware.kernel.common.repository.dds.DdsUpdateInfo;
import org.radixware.kernel.common.repository.uds.UdsSegment;
import org.radixware.kernel.common.svn.client.SvnPath;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.XmlUtils;
import org.radixware.schemas.product.LayerDocument;

class LayerInfo {

    SvnFileList fileList;
    private final IFlowLogger logger;
    final Layer layer;
    final String layerSrcUrl;
    byte[] layerXmlContent;
    byte[] layerXmlContentForSrc;
    byte[] changesXmlContent;
    private final ReleaseFlow flow;
    private final ReleaseSettings settings;
    private String newAPIVersion;
    private List<Id> patchModuleIds = null;
    private String branchPath;
    private final ReleaseInfo releaseInfo;

    public LayerInfo(Layer l, String branchUrl, ReleaseFlow flow, ReleaseInfo releaseInfo) {
        layer = l;
        this.flow = flow;
        this.settings = flow.getSettings();
        this.logger = this.settings.getLogger();
        layerSrcUrl = SvnPath.append(branchUrl, l.getDirectory().getName());
        this.releaseInfo = releaseInfo;
        this.branchPath = branchUrl;
    }

    boolean prepare(final long revision, final String newAPIVersion) {
        String message = "Preparing distribuition of layer " + layer.getURI() + "...";
        IProgressHandle ph = settings.getBuildEnvironment().getBuildDisplayer().getProgressHandleFactory().createHandle(message);
        logger.stateMessage(message);
        try {
            ph.start();
            if (!layer.isReadOnly()) {
                if (flow.isPatchRelease()) {
                    patchModuleIds = settings.getPatchModules(layer.getURI());
                    if (patchModuleIds == null) {
                        patchModuleIds = Collections.emptyList();
                    }
                } else {
                    if (newAPIVersion != null) {
                        this.newAPIVersion = newAPIVersion;
                    }
                    final byte[] oldLayerXmlFileContent = flow.getRepository().getFileBytes(SvnPath.append(layerSrcUrl, "layer.xml"), revision);
                    if (oldLayerXmlFileContent == null) {
                        return logger.fatal("Unable to read description file of layer " + layer.getURI());
                    }
                    this.layerXmlContent = updateLayerXmlContent(oldLayerXmlFileContent, settings.getNumber(), false);
                    this.layerXmlContentForSrc = updateLayerXmlContent(oldLayerXmlFileContent, settings.getNumber(), true);
                }

                if (!checkMetaState(flow, ph)) {
                    return false;
                }

                if (settings.isGenerateRadixdoc()) {
                    final List<Module> allModules = new LinkedList<>();
                    final IFilter<AdsModule> adsFilter = new IFilter<AdsModule>() {
                        @Override
                        public boolean isTarget(AdsModule radixObject) {
                            allModules.add(radixObject);
                            return false;
                        }
                    };
                    final IFilter<DdsModule> ddsFilter = new IFilter<DdsModule>() {
                        @Override
                        public boolean isTarget(DdsModule radixObject) {
                            allModules.add(radixObject);
                            return false;
                        }
                    };
                    ((DdsSegment) layer.getDds()).getModules().list(ddsFilter);
                    ((AdsSegment) layer.getAds()).getModules().list(adsFilter);

                    if (!generateRadixdoc(allModules, ph)) {
                        logger.error("Generate documentation for layer " + layer.getURI() + " failed");
                        return false;
                    }
                }

            } else {
                if (flow.isPatchRelease()) {
                    return true;
                }
            }
            fileList = new SvnFileList(flow,
                    layerSrcUrl,
                    layer,
                    revision,
                    releaseInfo.releaseDir.path,
                    releaseInfo.releaseSrcDir == null ? null : releaseInfo.releaseSrcDir.path,
                    releaseInfo.scriptsDir == null ? null : releaseInfo.scriptsDir.path,
                    releaseInfo, patchModuleIds);
        } finally {
            ph.finish();
        }

        return fileList.prepare();
    }

    private boolean generateRadixdoc(final List<Module> modules, final IProgressHandle ph) {
        if (modules.isEmpty()) {
            return true;
        }
        logger.stateMessage("Generate documentation for layer " + layer.getURI() + "...");
        final IRadixdocDictionary enDictionary = DictionaryFactories.createEnglish();
        final String branchLocatiopn = modules.get(0).getBranch().getDirectory().getPath();

        final RadixdocSpecificator specificator = RadixdocSpecificator.Factory.newInstance(
                RadixdocOptions.Factory.newInstance(modules, modules.get(0).getLayer().getLanguages(),
                        branchLocatiopn, false), enDictionary, ph, null);

        return specificator.generate();
    }

    private boolean checkObjects(Collection<? extends RadixObject> objects, IProgressHandle ph) {
        final Checker checker = new Checker(new CheckOptions(false, true, null));
        final Collection<? extends RadixObject> forCheck;
        final String message;
        if (objects == null) {
            message = "Checking layer " + layer.getURI() + "...";
            forCheck = Collections.singleton(layer);
        } else {
            message = "Checking selected modules of layer " + layer.getURI() + "...";
            forCheck = objects;
        }
        logger.stateMessage(message);
        ph.setDisplayName(message);
        if (!checker.check(forCheck)) {
            final String message2 = "Errors found during check of " + layer.getURI();
            if (!flow.getSettings().TEST_MODE && !logger.recoverableError(message2)) {
                return false;
            }
            if (flow.getSettings().TEST_MODE) {
                logger.error(message2);
            }
        }
        return true;
    }

    private boolean compileCompilableAndUpdateDistrubuition(List<AdsModule> explicitModules, IProgressHandle ph) {
        final Segment[] segments = new Segment[]{layer.getAds(), layer.getUds()};
        if (!settings.TEST_MODE && flow.getSettings().performCleanAndBuild()) {

            for (ERuntimeEnvironmentType sc : ERuntimeEnvironmentType.values()) {
                ((AdsSegment) layer.getAds()).getBuildPath().getPlatformLibs().getKernelLib(sc).cleanup();
            }
            final BuildActionExecutor exec = new BuildActionExecutor(flow.getSettings().getBuildEnvironment());
            if (explicitModules == null) {
                final String message = "Compiling ADS of layer " + layer.getURI() + "...";
                logger.stateMessage(message);
                ph.setDisplayName(message);
                exec.execute(layer);
            } else {
                final String message = "Compiling selected ADS modules of layer " + layer.getURI() + "...";
                logger.stateMessage(message);
                ph.setDisplayName(message);
                for (AdsModule module : explicitModules) {
                    exec.execute(module);
                }
            }
            if (exec.wasErrors()) {
                final String message2 = "ADS of layer " + layer.getURI() + " compiled with errors";
                if (!flow.getSettings().TEST_MODE && !logger.recoverableError(message2)) {
                    return false;
                }
                if (flow.getSettings().TEST_MODE) {
                    logger.error(message2);
                }
            }
            logger.setActive();
        } else {
            logger.message("Compiling of ADS of layer " + layer.getURI() + " is turned off by user");
        }
        final List<? extends RadixObject> objectsToUpdateDist;
        if (explicitModules == null) {
            objectsToUpdateDist = Arrays.asList(segments);
        } else {
            objectsToUpdateDist = explicitModules;
        }
        final boolean isPatch = settings.isPatchRelease();
        final String message = "Preparing module descriptions (API,usages,directory indices etc...)";
        ph.setDisplayName(message);
        logger.stateMessage(message);
        for (final RadixObject obj : objectsToUpdateDist) {
            if (obj instanceof UdsSegment || obj instanceof UdsModule) {
                continue;
            }
            boolean withSources = true;
            if (isPatch && obj instanceof AdsModule) {
                withSources = flow.getComponentManager().sourcesRequired(layer.getURI(), ((AdsModule) obj).getId());
            }
            if (!updateAPIAndUsages(obj, ph)) {
                return false;
            }
            if (!DirectoryFileChecker.Factory.newInstance(obj, flow.getSettings().getBuildEnvironment()).update(withSources) && !flow.getSettings().TEST_MODE) {
                return logger.fatal("Distribution state is inconsistent and can not be updated");

            }
        }
        if (explicitModules == null) {
            DirectoryFileChecker.LayerChecker checker = new DirectoryFileChecker.LayerChecker(layer, this.flow.getSettings().getBuildEnvironment());
            checker.updateDirectoryLayer();
        }

        return true;
    }

    private boolean updateAPIAndUsages(RadixObject obj, IProgressHandle ph) {
        if (obj instanceof AdsModule) {
            final AdsModule module = (AdsModule) obj;
            try {
                module.generateAPI(true);
                module.generateUsages(true);
                return true;
            } catch (IOException ex) {
                return logger.fatal(new RadixError("Unable to update declarations of module " + module.getQualifiedName(), ex));
            }
        } else if (obj instanceof AdsSegment) {
            final List<AdsModule> modules = ((AdsSegment) obj).getModules().list();
            ph.setDisplayName("Updating API and usages lists...");
            try {
                ph.switchToDeterminate(modules.size());
                logger.message("Updating API and usages lists of " + modules.size() + " modules of layer " + layer.getURI() + "...");
                int counter = 0;
                for (AdsModule module : modules) {
                    if (!updateAPIAndUsages(module, ph)) {
                        return false;
                    }
                    counter++;
                    ph.progress(counter);
                }
            } finally {
                ph.switchToIndeterminate();
            }
            return true;
        } else {
            return true;
        }
    }

    private boolean checkMetaState(final ReleaseFlow flow, IProgressHandle ph) {
        if (!layer.isReadOnly()) {
            List<AdsModule> patchModules = null;
            if (patchModuleIds != null) {
                patchModules = new LinkedList<AdsModule>();
                for (final Id id : patchModuleIds) {
                    final AdsModule module = (AdsModule) layer.getAds().getModules().findById(id);
                    if (module == null) {
                        final String message = "Errors found during check of " + layer.getURI() + ": module #" + id + " can not be found";
                        return logger.fatal(message);
                    } else {
                        patchModules.add(module);
                    }
                }
            }
            if (!settings.TEST_MODE) {

                if (patchModules != null) {
                    if (!checkObjects(patchModules, ph)) {
                        return false;
                    }
                    return compileCompilableAndUpdateDistrubuition(patchModules, ph);
                } else {
                    if (!checkObjects(null, ph)) {
                        return false;
                    }
                    logger.message("Checking DDS state of layer " + layer.getURI() + "...");
                    final DdsSegment dds = (DdsSegment) layer.getDds();
                    for (final DdsModule module : dds.getModules()) {
                        try {
                            if (module.getModelManager().getModifiedModel() != null) {
                                final String message = "DDS module " + module.getQualifiedName() + " is not fixed";
                                if (!flow.getSettings().TEST_MODE && !logger.recoverableError(message)) {
                                    return false;
                                }
                                if (flow.getSettings().TEST_MODE) {
                                    logger.error(message);
                                }
                            }
                        } catch (IOException ex) {
                            final String message = "Unable to determine DDS status: " + ex.getMessage();
                            if (flow.getSettings().TEST_MODE && !logger.recoverableError(message)) {
                                return false;
                            }
                            if (flow.getSettings().TEST_MODE) {
                                logger.error(message);
                            }
                        }
                    }
                }
            }
            return compileCompilableAndUpdateDistrubuition(null, ph);
        } else {
            return true;
        }
    }

    byte[] updateLayerXmlContent(byte[] oldLayerXmlBytes, String releaseNumber, boolean forSrc) {

        final ByteArrayInputStream inputStream = new ByteArrayInputStream(oldLayerXmlBytes);
        try {
            try {
                final LayerDocument xDoc = LayerDocument.Factory.parse(inputStream);
                if (xDoc.getLayer() != null) {
                    xDoc.getLayer().setReleaseNumber(releaseNumber);
                    if (!forSrc) {
                        final List<Layer> baseLayers = layer.listBaseLayers();
                        if (!baseLayers.isEmpty()) {
                            final org.radixware.schemas.product.Layer.Compatibility xCmp = xDoc.getLayer().addNewCompatibility();
                            for (final Layer l : baseLayers) {
                                final org.radixware.schemas.product.Layer.Compatibility.Layer2 xLayer = xCmp.addNewLayer();
                                xLayer.setURI(l.getURI());
                                if (l.isReadOnly()) {
                                    xLayer.setVersion(l.getReleaseNumber());
                                } else {
                                    xLayer.setVersion(releaseNumber);
                                }
                            }
                        }
                        if (newAPIVersion != null) {
                            xDoc.getLayer().setAPIVersion(newAPIVersion);
                        }
                    }
                    return XmlUtils.saveXmlPretty(xDoc);
                } else {
                    return null;
                }
            } catch (Exception ex) {
                return null;
            }
        } finally {
            try {
                inputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
        }
    }
}
