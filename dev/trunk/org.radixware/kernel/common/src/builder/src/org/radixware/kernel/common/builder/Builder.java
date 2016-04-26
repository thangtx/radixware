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

package org.radixware.kernel.common.builder;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.*;
import org.radixware.kernel.common.builder.api.IBuildEnvironment;
import org.radixware.kernel.common.builder.api.IProgressHandle;
import org.radixware.kernel.common.builder.api.IProgressHandleFactory;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.components.ICancellable;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.build.BuildOptions;
import org.radixware.kernel.common.defs.ads.build.Cancellable;
import org.radixware.kernel.common.defs.ads.build.IFlowLogger;
import org.radixware.kernel.common.defs.ads.build.Make;
import org.radixware.kernel.common.defs.ads.build.Make.Distribution;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.src.JavaFileSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.CharOperations;
import org.radixware.kernel.common.utils.JarFiles;


class Builder extends AbstractAdsBuilder {

    private final BuildOptions.UserModeHandler userModeHandler;
    private final Map<Id, Id> idReplaceMap;

    Builder(Branch branch, EnumSet<ERuntimeEnvironmentType> envs, IBuildEnvironment buildEnv, BuildOptions options, final Map<Id, Id> idReplaceMap) {
        super(branch, envs, buildEnv, options);
        this.idReplaceMap = idReplaceMap;
        userModeHandler = options.getUserModeHandler();
        if (userModeHandler != null) {
            collectedResult = new ArrayList<>();
        }
    }

    @Override
    protected String getProcessName(ERuntimeEnvironmentType env) {
        return "Build (" + env.getName() + ")...";
    }

    public void accept(RadixProblem problem) {
        buildEnv.getBuildProblemHandler().accept(problem);
    }

    @Override
    public String getDisplayName() {
        return "Build";
    }

    private static class BuildFlow implements ProcessFlow {

        private final Make make;
        private final ERuntimeEnvironmentType currentEnvironment;
        private final Distributor distributor;
        private boolean imagesPacked = false;
        private final Builder builder;

        public BuildFlow(Builder builder, IBuildEnvironment buildEnv, ERuntimeEnvironmentType currentEnvironment, final Map<Id, Id> idReplaceMap, final List<List<Distribution>> collectedResult, boolean userMode) {
            this.builder = builder;
            this.make = new Make(buildEnv.getFlowLogger(), userMode);
            this.distributor = new Distributor(buildEnv, buildEnv.getBuildProblemHandler(), collectedResult, idReplaceMap);
            this.currentEnvironment = currentEnvironment;
        }

        @Override
        public ERuntimeEnvironmentType getEnvironment() {
            return currentEnvironment;
        }
    }
    private List<List<Distribution>> collectedResult;

    @Override
    protected void build(RadixObject radixObject, ProcessFlow flow) {
        if (radixObject instanceof Definition && flow instanceof BuildFlow) {
            BuildFlow f = (BuildFlow) flow;
            f.make.compile((Definition) radixObject, f.currentEnvironment, buildEnv.getBuildProblemHandler(), idReplaceMap, buildEnv.getActionType() == BuildActionExecutor.EBuildActionType.COMPILE_SINGLE, userModeHandler != null);
        }
    }

    @Override
    protected void build(Collection<RadixObject> radixObjects, ProcessFlow flow, IProgressHandle progressHandle, ICancellable cancellable) {
        if (flow instanceof BuildFlow) {
            final BuildFlow f = (BuildFlow) flow;
            final Map<Layer, List<Definition>> layer2DefList = new HashMap<>();

            for (RadixObject radixObject : radixObjects) {
                if (radixObject instanceof Definition) {
                    final Layer layer = radixObject.getLayer();
                    List<Definition> list = layer2DefList.get(layer);
                    if (list == null) {
                        list = new ArrayList<>();
                        layer2DefList.put(layer, list);
                    }
                    list.add((Definition) radixObject);
                }
            }
            final List<Branch> branches = new ArrayList<>(3);
            if (!layer2DefList.isEmpty()) {
                for (Layer l : layer2DefList.keySet()) {
                    final Branch branch = l.getBranch();
                    if (!branches.contains(branch)) {
                        branches.add(branch);
                    }
                }
            }
            for (final Branch branch : branches) {
                List<Layer> layers = branch.getLayers().getInOrder();
                for (final Layer l : layers) {
                    final List<Definition> definitions = layer2DefList.get(l);
                    if (definitions != null && !definitions.isEmpty()) {
                        f.make.compile(l,
                                definitions,
                                f.currentEnvironment,
                                buildEnv.getBuildProblemHandler(),
                                idReplaceMap,
                                buildEnv.getActionType() == BuildActionExecutor.EBuildActionType.COMPILE_SINGLE,
                                userModeHandler != null,
                                progressHandle,
                                cancellable);
                    }
                }
            }
        }
    }

    @Override
    protected ProcessFlow beforeLookup(ERuntimeEnvironmentType e) {
        return new BuildFlow(this, buildEnv, e, idReplaceMap, collectedResult, userModeHandler != null);
    }

    @Override
    protected void afterLookup(ProcessFlow flow, List<Module> lookupModules) {

        assert flow instanceof BuildFlow;
        BuildFlow f = (BuildFlow) flow;
        f.distributor.process(f.currentEnvironment, f);
        f.make.reset();
        if (f.distributor.wasCancelled()) {
            cancel();
            return;
        }
        if (f.currentEnvironment == ERuntimeEnvironmentType.COMMON && !f.imagesPacked && lookupModules != null) {
            f.imagesPacked = true;
            final IProgressHandle progressHandle = buildEnv.getBuildDisplayer().getProgressHandleFactory().createHandle("Packing Images...", f.distributor);
            try {
                progressHandle.start(lookupModules.size());
                int counter = 0;
                for (Module m : lookupModules) {
                    if (f.distributor.wasCancelled()) {
                        cancel();
                        return;
                    }
                    if (m instanceof AdsModule) {
                        f.distributor.processImages((AdsModule) m, f);
                        f.distributor.processLocales((AdsModule) m, f);
                    }
                    progressHandle.progress(counter++);
                }
            } finally {
                progressHandle.finish();
            }
        }
    }

    @Override
    protected void prepare() {
    }

    @Override
    protected boolean isBulkProcessor() {
        return true;
    }

    @Override
    protected void complete() {
        if (userModeHandler != null) {
            final Cancellable cancel = new Cancellable() {
                volatile boolean cancelled = false;

                @Override
                public boolean cancel() {
                    return cancelled = true;
                }

                @Override
                public boolean wasCancelled() {
                    return cancelled;
                }
            };
            final IProgressHandle progressHandle = buildEnv.getBuildDisplayer().getProgressHandleFactory().createHandle("Packing Definitions...", cancel);
            List<Make.CompoundDistribution> results = Distribution.merge(collectedResult);
            progressHandle.switchToDeterminate(results.size());

            for (int i = 0, len = results.size(); i < len; i++) {
                if (cancel.wasCancelled()) {
                    break;
                }

                final Make.CompoundDistribution dist = results.get(i);

                if (dist.getModule().isReadOnly()) {
                    continue;
                }
                final Id id = dist.getDefinition().getId();
                buildEnv.getMutex().readAccess(new Runnable() {
                    @Override
                    public void run() {
                        //if (!dist.getModule().isReadOnly()) {
                        File file = dist.mkJar(buildEnv.getBuildProblemHandler());
                        if (file != null) {
                            userModeHandler.acceptUserDefinitionRuntime(id, file);
                        }
                        //}
                    }
                });


                progressHandle.progress(i + 1);
            }
        }


        buildEnv.displayResults();
    }

    private static class DefaultDistributionProcessor implements Make.DistributionProcessor {

        private final IProgressHandle progressHandle;
        private final IBuildEnvironment buildEnv;
        private final Cancellable cancel;
        private final BuildFlow flow;

        public DefaultDistributionProcessor(IProgressHandle progressHandle, IBuildEnvironment buildEnv, Cancellable cancel, BuildFlow flow) {
            this.progressHandle = progressHandle;
            this.buildEnv = buildEnv;
            this.cancel = cancel;
            this.flow = flow;
        }

        @Override
        public void processSingleEnvironment(ERuntimeEnvironmentType env, List<Distribution> infos) {
            progressHandle.switchToDeterminate(infos.size());
            for (int i = 0, len = infos.size(); i < len; i++) {
                if (cancel.wasCancelled()) {
                    break;
                }

                final Make.Distribution dist = infos.get(i);

                if (dist.getModule().isReadOnly()) {
                    continue;
                }
                if (dist instanceof Make.JarDistribution) {
                    buildEnv.getMutex().readAccess(new Runnable() {
                        @Override
                        public void run() {
                            //if (!dist.getModule().isReadOnly()) {
                            ((Make.JarDistribution) dist).mkJar(buildEnv.getBuildProblemHandler());
                            //}
                        }
                    });
                }
                flow.builder.moduleProcessed(dist.getModule());
                progressHandle.progress(i + 1);
            }
        }

        @Override
        public void summary() {
            //ignore
        }
    }

    private static class AccumulatorDistributionProcesor implements Make.DistributionProcessor {

        final List<List<Distribution>> collectedResult;
        private final IProgressHandle progressHandle;
        private final Cancellable cancel;
        private final BuildFlow flow;

        public AccumulatorDistributionProcesor(IProgressHandle progressHandle, Cancellable cancel, BuildFlow flow, final List<List<Distribution>> collectedResult) {
            this.progressHandle = progressHandle;
            this.collectedResult = collectedResult;
            this.cancel = cancel;
            this.flow = flow;
        }

        @Override
        public void processSingleEnvironment(ERuntimeEnvironmentType env, List<Distribution> infos) {
            if (infos != null) {
                collectedResult.add(new ArrayList<>(infos));
                for (int i = 0, len = infos.size(); i < len; i++) {
                    if (cancel.wasCancelled()) {
                        break;
                    }
                    final Make.Distribution dist = infos.get(i);
                    flow.builder.moduleProcessed(dist.getModule());
                    progressHandle.progress(i + 1);
                }
            }
        }

        @Override
        public void summary() {
        }
    }

    private static class Distributor implements Cancellable {

        private final IProblemHandler ph;
        private final IBuildEnvironment buildEnv;
        private volatile boolean isCanceled = false;
        private final List<List<Distribution>> collectedResult;
        private FileFilter imgFileFilter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return !(pathname.isDirectory() || pathname.getName().endsWith(".xml"));
            }
        };
        private FileFilter xmlFileFilter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return !pathname.isDirectory() && pathname.getName().endsWith(".xml");
            }
        };
        private volatile Make.DistributionProcessor distribuitionProcessor;
        private final Map<Id, Id> idReplaceMap;

        Distributor(IBuildEnvironment buildEnv, IProblemHandler ph, List<List<Distribution>> collectedResult, final Map<Id, Id> idReplaceMap) {
            this.ph = ph;
            this.buildEnv = buildEnv;
            this.collectedResult = collectedResult;
            this.idReplaceMap = idReplaceMap;
        }

        @Override
        public boolean cancel() {
            isCanceled = true;

            final IFlowLogger flowLogger = buildEnv.getFlowLogger();
            if (flowLogger instanceof Cancellable) {
                ((Cancellable) flowLogger).cancel();
            }
            return true;
        }

        @Override
        public boolean wasCancelled() {
            return isCanceled;
        }

        void process(ERuntimeEnvironmentType sc, BuildFlow flow) {
            IProgressHandleFactory phf = buildEnv.getBuildDisplayer().getProgressHandleFactory();
            final IProgressHandle progressHandle = phf.createHandle("Packing Definitions...", this);

            try {
                progressHandle.start();
                List<Make.Distribution> infos = flow.make.computeDistribution(sc, ph, idReplaceMap);
                if (!infos.isEmpty()) {
                    if (distribuitionProcessor == null) {
                        if (collectedResult != null) {
                            distribuitionProcessor = new AccumulatorDistributionProcesor(progressHandle, this, flow, collectedResult);
                        } else {
                            distribuitionProcessor = new DefaultDistributionProcessor(progressHandle, buildEnv, this, flow);
                        }
                    }
                    distribuitionProcessor.processSingleEnvironment(sc, infos);
                }
            } finally {
                progressHandle.finish();
            }
        }

        public void processImages(AdsModule module, BuildFlow flow) {
            if (module.isReadOnly()) {
                return;
            }
            //File branchDir = module.getSegment().getLayer().getBranch().getDirectory();

            File imgDir = module.getImages().getDirectory();

            String pathPrefix = new String(CharOperations.merge(JavaSourceSupport.getPackageNameComponents(module, JavaSourceSupport.UsagePurpose.COMMON_EXECUTABLE), '/'));

            if (imgDir != null && imgDir.exists() && imgDir.isDirectory()) {
                File binDir = JavaFileSupport.getBinDir(module);
                try {
                    File imgJar = new File(binDir, "img.jar");
                    boolean mkJar = false;
                    if (!imgJar.exists()) {
                        mkJar = true;
                    } else {
                        Collection<File> files = JarFiles.listFiles(imgDir, imgFileFilter, pathPrefix);
                        long jarTime = imgJar.lastModified();
                        for (File file : files) {
                            if (file.lastModified() > jarTime) {
                                mkJar = true;
                                break;
                            }
                        }
                    }
                    if (mkJar) {
                        if (JarFiles.mkJar(imgDir, new File(binDir, "img.jar"), imgFileFilter, pathPrefix, false)) {
                            flow.builder.moduleProcessed(module);
                        }
                    }
                } catch (IOException ex) {
                    buildEnv.getBuildProblemHandler().accept(RadixProblem.Factory.newError(module, "Unable to pack module images: " + ex.getMessage()));
                }
            }
        }

        public void processLocales(AdsModule module, BuildFlow flow) {
            if (module.isReadOnly()) {
                return;
            }
            //File branchDir = module.getSegment().getLayer().getBranch().getDirectory();

            File localesDir = new File(module.getSrcDirContainer(), "locale");
            if (!localesDir.exists()) {
                return;
            }
            File[] localeLangDirs = localesDir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    try {
                        return pathname.isDirectory() && EIsoLanguage.getForValue(pathname.getName()) != null;
                    } catch (NoConstItemWithSuchValueError e) {
                        return false;
                    }
                }
            });

            String pathPrefix = new String(CharOperations.merge(JavaSourceSupport.getPackageNameComponents(module, JavaSourceSupport.UsagePurpose.COMMON_EXECUTABLE), '/'));
            final File binDir = JavaFileSupport.getBinDir(module);
            for (File localeLangDir : localeLangDirs) {
                if (localeLangDir != null && localeLangDir.exists() && localeLangDir.isDirectory()) {

                    try {
                        File localeJar = new File(binDir, "locale-" + localeLangDir.getName() + ".jar");
                        boolean mkJar = false;
                        if (!localeJar.exists()) {
                            mkJar = true;
                        } else {
                            Collection<File> files = JarFiles.listFiles(localeLangDir, xmlFileFilter, pathPrefix);
                            long jarTime = localeJar.lastModified();
                            for (File file : files) {
                                if (file.lastModified() > jarTime) {
                                    mkJar = true;
                                    break;
                                }
                            }
                        }
                        if (mkJar) {
                            if (JarFiles.mkJar(localeLangDir, localeJar, xmlFileFilter, pathPrefix, false)) {
                                flow.builder.moduleProcessed(module);
                            }
                        }
                    } catch (IOException ex) {
                        buildEnv.getBuildProblemHandler().accept(RadixProblem.Factory.newError(module, "Unable to pack module localization data for language" + localeLangDir.getName() + ": " + ex.getMessage()));
                    }
                }
            }
        }
    }
}
