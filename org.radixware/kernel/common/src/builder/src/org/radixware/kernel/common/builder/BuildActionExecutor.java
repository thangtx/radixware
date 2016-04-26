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

import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;
import org.radixware.kernel.common.builder.api.IBuildEnvironment;
import org.radixware.kernel.common.builder.check.java.LinkageAnalyzer;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.build.BuildOptions;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.repository.fs.JarFileDataProvider;
import org.radixware.kernel.common.types.Id;

public class BuildActionExecutor {

    private IBuildEnvironment buildEnv;

    public BuildActionExecutor(IBuildEnvironment buildEnv) {
        this.buildEnv = buildEnv;
    }  

    public enum EBuildActionType {

        CLEAN("Clean"),
        BUILD("Build"),
        COMPILE_SINGLE("Compile"),
        RELEASE("Make Release"),
        API_COMPARE("API Compare"),
        API_COMPATIBILTY_CHECK("Upgrade Compatibility Check"),
        CLEAN_AND_BUILD("Clean and Build"),
        CHECK_DIST("Check Distribuition"),
        CHECK_API_DOC("Check API Documentation"),
        UPDATE_DIST("Update Distribuition");
        private String name;

        private EBuildActionType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
    private boolean wasErrors = false;

    private boolean isCancelled() {
        return buildEnv.getFlowLogger().getCancellable() != null && buildEnv.getFlowLogger().getCancellable().wasCancelled();
    }

    public void cancelled() {
        buildEnv.getFlowLogger().error("Cancelled by user");
        buildEnv.getFlowLogger().failure();
    }

    public void execute(final RadixObject[] context) {
        execute(context, buildEnv.getActionType());
    }

    public void execute(final RadixObject[] context, BuildOptions.UserModeHandler handler) {
        execute(context, buildEnv.getActionType(), handler);
    }

    public void execute(final RadixObject[] context, final EBuildActionType actionType) {
        execute(context, actionType, null);
    }

    public void execute(final RadixObject[] context, final EBuildActionType actionType, BuildOptions.UserModeHandler handler) {
        execute(context, actionType, null, true, handler);
    }

    public void execute(final RadixObject context) {
        if (context == null) {
            return;
        }
        execute(context, (BuildOptions.UserModeHandler) null);
    }

    public void execute(final RadixObject context, BuildOptions.UserModeHandler handler) {
        if (context == null) {
            return;
        }
        execute(new RadixObject[]{context}, handler);
    }

    public void execute(final RadixObject context, final EBuildActionType actionType) {
        if (context == null) {
            return;
        }
        execute(new RadixObject[]{context}, actionType);
    }

    public BuildOptions execute(final RadixObject context, final EBuildActionType actionType, final BuildOptions extra, final boolean reportOverallStatus) {
        if (context == null) {
            return null;
        }
        return execute(new RadixObject[]{context}, actionType, extra, reportOverallStatus);

    }

    public BuildOptions execute(final RadixObject[] context, final EBuildActionType actionType, final BuildOptions extra, final boolean reportOverallStatus) {
        return execute(context, actionType, extra, reportOverallStatus, null);
    }

    public BuildOptions execute(final RadixObject[] context, final EBuildActionType actionType, final BuildOptions extra, final boolean reportOverallStatus, final BuildOptions.UserModeHandler userModeHandler) {
        if (context == null || context.length <= 0 || context[0] == null) {
            return null;
        }

        buildEnv.prepare();
        buildEnv.getBuildProblemHandler().clear();
        if (buildEnv.getMutex().getLongProcessLock().tryLock()) {

            try {
                if (actionType == EBuildActionType.BUILD || actionType == EBuildActionType.RELEASE || actionType == EBuildActionType.CLEAN_AND_BUILD || actionType == EBuildActionType.COMPILE_SINGLE) {
                    buildEnv.getLifecycleManager().saveAll();
                }
                final BuildOptions[] options = new BuildOptions[1];

                if (extra != null) {
                    options[0] = extra;
                } else {
                    if (actionType != EBuildActionType.RELEASE) {
                        options[0] = buildEnv.getBuildOptions();
                    } else {
                        options[0] = BuildOptions.Factory.newInstance();
                        options[0].setEnvironment(EnumSet.allOf(ERuntimeEnvironmentType.class));
                        options[0].setVerifyClassLinkage(true);
                        options[0].setMultythread(false);
                    }
                }

                if (options[0] == null) {
                    return null;
                }

                options[0].setUserMode(userModeHandler);

                final Branch branch = context[0].getBranch();

                // Distillator.DistillationInfo info = new Distillator(actionType, phWrapper).distillate(contexts);
                buildEnv.getMutex().readAccess(new Runnable() {
                    @Override
                    public void run() {
                        long time = System.currentTimeMillis();
                        try {
                            RadixObject.disableChangeTracking();
                            try {
                                if (branch == null) {
                                    buildEnv.getBuildDisplayer().getDialogUtils().messageError("Parent branch not found");
                                    return;
                                }
                                if (!branch.getRepository().lock(1000)) {
                                    buildEnv.getBuildDisplayer().getDialogUtils().messageError("Unable to lock branch. Try again later");
                                    wasErrors = true;
                                    return;
                                }
                            } catch (IOException ex) {
                                buildEnv.getBuildDisplayer().getDialogUtils().messageError(new RadixError("Unable to lock branch", ex).getMessage());
                                wasErrors = true;
                                return;
                            }
                            try {
                                if (actionType == EBuildActionType.CHECK_DIST) {
                                    for (RadixObject ro : context) {
                                        DirectoryFileChecker.Factory.newInstance(ro, buildEnv).check();
                                    }
                                } else if (actionType == EBuildActionType.UPDATE_DIST) {
                                    for (RadixObject ro : context) {
                                        DirectoryFileChecker.Factory.newInstance(ro, buildEnv).update();
                                    }

                                } else {
                                    if (isCancelled()) {
                                        cancelled();
                                        return;
                                    }
                                    // PlatformLibs.getJreLib().getNameEnvironment().cleanup();
//                                    for (Layer l : branch.getLayers()) {
//                                        ((AdsSegment) l.getAds()).getBuildPath().cleanup();
//                                    }
                                    JarFileDataProvider.reset();
                                    final ContextChecker checker = new ContextChecker(options[0].isSkipCheck());
                                    final ContextChecker.ContextInfo checkedContexts = checker.determineTargets(actionType, context, buildEnv);

                                    if (actionType == EBuildActionType.CHECK_API_DOC) {
                                        return;
                                    }

                                    final Map<Id, Id> idReplaceMap = new HashMap<>(3);
                                    if (checkedContexts == null) {
                                        wasErrors = true;
                                        buildEnv.getBuildDisplayer().getDialogUtils().messageError("No suitable targets for build found");
                                    } else {
                                        if (isCancelled()) {
                                            cancelled();
                                            return;
                                        }
                                        buildEnv.targetsDetermined(checkedContexts.targets, checkedContexts.checkFailedDefinitions);
                                        if (userModeHandler != null) {
                                            for (Definition def : checkedContexts.targets) {
                                                userModeHandler.acceptBuildTarget(def, idReplaceMap);
                                            }
                                        } else {

                                        }
                                        final List<? extends AbstractAdsBuilder> builders = AbstractAdsBuilder.Factory.newInstance(branch, actionType, options[0].getEnvironment(), buildEnv, options[0], idReplaceMap);

                                        for (Layer l : branch.getLayers()) {
                                            ((AdsSegment) l.getAds()).getBuildPath().cleanup();
                                        }
                                        JarFileDataProvider.reset();
                                        //    System.gc();
                                        final HashSet<Module> processedModules = new HashSet<>(checkedContexts.processedModules.size());
                                        for (AbstractAdsBuilder b : builders) {
                                            b.execute(checkedContexts.targets, checkedContexts.processedModules, processedModules);
                                            if (b.wasCancelled() || isCancelled()) {
                                                cancelled();
                                                wasErrors = true;
                                                return;
                                            }
                                            if (buildEnv.getBuildProblemHandler().wasErrors()) {
                                                wasErrors = true;
                                            }
                                        }
                                        for (Layer l : branch.getLayers()) {
                                            ((AdsSegment) l.getAds()).getBuildPath().cleanup();
                                        }
                                        JarFileDataProvider.reset();
                                        //      System.gc();
                                        List<AdsModule> adsModules = new LinkedList<>();
                                        for (Module m : processedModules) {
                                            if (m instanceof AdsModule) {
                                                adsModules.add((AdsModule) m);
                                            }
                                        }

                                        if (!isCancelled()) {
                                            if (userModeHandler == null) {
                                                new DefinitionsDistributor(buildEnv, actionType).execute(adsModules);
                                            }
                                            JarFileDataProvider.reset();
                                        } else {
                                            cancelled();
                                            wasErrors = true;
                                            return;
                                        }
                                        if (!isCancelled()) {
                                            if (userModeHandler == null) {
                                                new DirectoryFileBuilder(buildEnv, actionType).execute(adsModules);
                                                if (checker.processedLayers != null) {
                                                    new DirectoryFileBuilder(buildEnv, actionType).updateLayers(checker.processedLayers);
                                                }
                                            }
                                        } else {
                                            cancelled();
                                            wasErrors = true;
                                            return;
                                        }
                                        if (!wasErrors && options[0].isVerifyClassLinkage() && actionType != EBuildActionType.CLEAN && userModeHandler == null) {

                                            LinkageAnalyzer analyzer = new LinkageAnalyzer(buildEnv);
                                            try {
                                                analyzer.process(new ArrayList<>(adsModules));
                                            } finally {
                                                analyzer.close();
                                            }
                                        }

                                    }
                                }
                            } finally {
                                try {
                                    branch.getRepository().unlock();
                                } catch (IOException ex) {
                                    buildEnv.getBuildDisplayer().getDialogUtils().messageError(ex);
                                }
                            }
                        } finally {
                            RadixObject.enableChangeTracking();
                            if (reportOverallStatus) {
                                time = System.currentTimeMillis() - time;
                                buildEnv.getBuildDisplayer().getStatusDisplayer().setStatusText(MessageFormat.format("Completed {0,time,mm:ss}", time));
                                buildEnv.getFlowLogger().finished("", time, !wasErrors);
                            }
                        }
                    }
                });

                return options[0];
            } finally {

                buildEnv.getMutex().getLongProcessLock().unlock();
            }
        } else {
            buildEnv.getBuildDisplayer().getDialogUtils().messageInformation("Build or check action is already running");
            return null;
        }
    }

    public boolean wasErrors() {
        return wasErrors || buildEnv.getBuildProblemHandler().wasErrors();
    }
}
