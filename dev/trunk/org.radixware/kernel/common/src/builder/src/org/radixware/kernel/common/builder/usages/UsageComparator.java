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
package org.radixware.kernel.common.builder.usages;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.builder.api.IBuildEnvironment;
import org.radixware.kernel.common.builder.api.IProgressHandle;
import org.radixware.kernel.common.builder.check.java.LinkageAnalyzer;
import org.radixware.kernel.common.builder.compare.DefinitionComparator;
import org.radixware.kernel.common.builder.compare.DefinitionComparator.IReporter;

import org.radixware.kernel.common.repository.fs.RepositoryInjection;

import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.build.IFlowLogger;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.exceptions.RadixPrivateException;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.ads.fs.InjectionFactory;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.UsageDescription;
import org.radixware.schemas.adsdef.UsagesDocument;
import org.radixware.schemas.adsdef.UsagesDocument.Usages;

public class UsageComparator {

    private class TmpReporter implements DefinitionComparator.IReporter {

        private final String usageContext;
        private final IReporter reporter;

        public TmpReporter(String usageContext, IReporter reporter) {
            this.usageContext = usageContext;
            this.reporter = reporter;
        }

        @Override
        public void incompatibleChange(String message) {
            reporter.incompatibleChange("Incompatible change at " + usageContext + ": " + message);
        }

        @Override
        public void compatibleChange(String message) {
            reporter.incompatibleChange("Compatible change at " + usageContext + ": " + message);
        }

        @Override
        public void removeChange(RadixObject removedObject) {
            reporter.removeChange(removedObject);
        }

        @Override
        public void addChange(RadixObject removedObject) {
            reporter.removeChange(removedObject);
        }
    }

    private class FlowLoggerWrapper implements IReporter {

        private final IFlowLogger logger;

        public FlowLoggerWrapper(IFlowLogger logger) {
            this.logger = logger;
        }

        @Override
        public void incompatibleChange(String message) {
            this.logger.error(message);
        }

        @Override
        public void compatibleChange(String message) {
            this.logger.stateMessage(message);
        }

        @Override
        public void removeChange(RadixObject removedObject) {
            this.logger.error("Definition removed: " + removedObject.getQualifiedName());
        }

        @Override
        public void addChange(RadixObject removedObject) {
            this.logger.stateMessage("Definition added: " + removedObject.getQualifiedName());
        }
    }
    private final Branch branch;
    private final IReporter reporter;
    private final RepositoryInjection injection;
    private final IBuildEnvironment buildEnv;

    public UsageComparator(Branch branch, IBuildEnvironment buildEnv) {
        this.reporter = new FlowLoggerWrapper(buildEnv.getFlowLogger());
        this.branch = branch;
        this.injection = null;
        this.buildEnv = buildEnv;
    }

    public UsageComparator(Branch branch, List<File> upgradeFiles, IBuildEnvironment buildEnv) throws IOException, RadixPrivateException {
        this.reporter = new FlowLoggerWrapper(buildEnv.getFlowLogger());
        this.branch = branch;
        if (upgradeFiles != null && !upgradeFiles.isEmpty()) {
            this.injection = InjectionFactory.newInstance(upgradeFiles);
        } else {
            this.injection = null;
        }
        this.buildEnv = buildEnv;
    }

    private void collectModules(List<AdsModule> modules) {
        for (Layer layer : branch.getLayers()) {
            for (Module module : layer.getAds().getModules()) {
                modules.add((AdsModule) module);
            }
        }
    }

    public boolean process() {
        IProgressHandle ph = buildEnv.getBuildDisplayer().getProgressHandleFactory().createHandle("Checking compatibility...");
        try {
            ph.start();
            if (injection != null) {
                try {
                    String message = "Installing injections...";
                    ph.setDisplayName(message);
                    buildEnv.getFlowLogger().stateMessage(message);
                    injection.install(branch);
                } catch (IOException ex) {
                    return false;
                }
            }
            List<AdsModule> modules = new LinkedList<AdsModule>();

            String message = "Collecting modules...";
            ph.setDisplayName(message);
            buildEnv.getFlowLogger().stateMessage(message);
            collectModules(modules);
            boolean result = true;

            message = "Check usages...";
            ph.setDisplayName(message);
            buildEnv.getFlowLogger().stateMessage(message);
            for (AdsModule module : modules) {
                InputStream in = null;
                try {
                    in = module.getRepository().getUsagesXmlInputStream();
                    UsagesDocument xDoc = UsagesDocument.Factory.parse(in);
                    Usages xDef = xDoc.getUsages();
                    for (Usages.Layer xLayer : xDef.getLayerList()) {
                        String usedLayerUri = xLayer.getURI();
                        for (Usages.Layer.Module xModule : xLayer.getModuleList()) {
                            Id usedModuleId = xModule.getId();
                            if (!compare(usedLayerUri, usedModuleId, xModule)) {
                                result = false;
                            }
                        }
                    }
                } catch (XmlException ex) {
                    buildEnv.getFlowLogger().error("Invalid usages file format at " + module.getQualifiedName());
                    result = false;
                } catch (IOException ex) {
                    buildEnv.getFlowLogger().error("Unable to read usages file from " + module.getQualifiedName());
                    result = false;
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException ex) {
                            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                        }
                    }
                }
            }
            if (result) {
                message = "Check binary compatiblity...";
                ph.setDisplayName(message);
                buildEnv.getFlowLogger().stateMessage(message);
                final boolean[] classCheckSuccessfull = new boolean[]{true};
                LinkageAnalyzer analyzer = new LinkageAnalyzer(buildEnv, false) {
                    @Override
                    protected void acceptProblem(ModuleInfo module, String message) {
                        buildEnv.getFlowLogger().error(message);
                        classCheckSuccessfull[0] = false;
                    }
                };
                analyzer.process(modules);
                if (!classCheckSuccessfull[0]) {
                    result = false;
                }
            }
            return result;
        } finally {
            if (injection != null) {
                String message = "Uninstalling injections...";
                ph.setDisplayName(message);
                buildEnv.getFlowLogger().stateMessage(message);
                injection.uninstall();
            }
            ph.finish();
        }
    }

    private AdsDefinition findUsed(String layerURI, Id moduleId, Id[] path) {
        Layer layer = branch.getLayers().findByURI(layerURI);
        if (layer == null) {
            return null;
        } else {
            AdsModule module = (AdsModule) layer.getAds().getModules().findById(moduleId);
            if (module == null) {
                return null;
            }
            if (path[0].getPrefix() == EDefinitionIdPrefix.IMAGE) {
                return module.getImages().findById(path[0]);
            }
            AdsDefinition root = module.getTopContainer().findById(path[0]);
            if (root == null) {
                return null;
            }
            for (int i = 1; i < path.length; i++) {
                root = root.findComponentDefinition(path[i]).get();
                if (root == null) {
                    return null;
                }
            }
            return root;
        }
    }

    private boolean compare(String usedLayerURI, Id usedModuleId, Usages.Layer.Module xModule) {
        for (UsageDescription xUsage : xModule.getUsageList()) {
            Id[] path = xUsage.getPath().toArray(new Id[xUsage.getPath().size()]);
            AdsDefinition usedDef = findUsed(usedLayerURI, usedModuleId, path);
            if (usedDef == null) {
                reporter.incompatibleChange("Used definition " + xUsage.getQName() + " from " + usedLayerURI + "::" + xModule.getName() + " not found");
                return false;
            } else {
                TmpReporter tmpReporter = new TmpReporter(xUsage.getQName(), reporter);
                boolean isExtension = xUsage.getIsExtension();
                switch (xUsage.getDefinitionType()) {
                    case CLASS_METHOD:
                        if (usedDef instanceof AdsMethodDef) {
                            AdsMethodDef usageMethod = AdsMethodDef.Factory.loadFrom(xUsage.getMethod());
                            if (!DefinitionComparator.checkMethodCompatibility(usageMethod, (AdsMethodDef) usedDef, tmpReporter)) {
                                return false;
                            }
                        } else {
                            reporter.incompatibleChange(xUsage.getQName() + " is expected to be method definition");
                            return false;
                        }
                        break;
                    case CLASS_PROPERTY:
                        if (usedDef instanceof AdsPropertyDef) {
                            AdsPropertyDef usageProperty = AdsPropertyDef.Factory.loadFrom(xUsage.getProperty());
                            if (!DefinitionComparator.checkPropertyCompatibility(usageProperty, (AdsPropertyDef) usedDef, tmpReporter)) {
                                return false;
                            }
                        } else {
                            reporter.incompatibleChange(xUsage.getQName() + " is expected to be property definition");
                            return false;
                        }
                        break;
                    case ENUM_ITEM:
                        if (usedDef instanceof AdsEnumItemDef) {
                            AdsEnumItemDef usageItem = AdsEnumItemDef.Factory.loadFrom(xUsage.getEnumItem());
                            if (!DefinitionComparator.checkEnumerationItemCompatibility(usageItem, (AdsEnumItemDef) usedDef, tmpReporter)) {
                                return false;
                            }
                        } else {
                            reporter.incompatibleChange(xUsage.getQName() + " is expected to be enumeration item definition");
                            return false;
                        }
                        break;
                    default:
                        if (usedDef.getDefinitionType() != xUsage.getDefinitionType()) {
                            tmpReporter.incompatibleChange("definition type mismatch: expected " + xUsage.getDefinitionType().getName() + " but was " + usedDef.getDefinitionType().getName());
                        }
                        break;
                }
            }
        }
        return true;
    }
}
