/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.common.builder.api;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;
import org.radixware.kernel.common.builder.BuildActionExecutor;
import org.radixware.kernel.common.builder.check.common.CheckOptions;
import org.radixware.kernel.common.builder.console.ConsoleFlowLogger;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.build.BuildOptions;
import org.radixware.kernel.common.defs.ads.build.Cancellable;
import org.radixware.kernel.common.defs.ads.build.IFlowLogger;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;

/**
 *
 * @author akrylov
 */
public class DefaultBuildEnvironment implements IBuildEnvironment {

    private static class DummyProgressHandle implements IProgressHandle {

        @Override
        public void switchToIndeterminate() {

        }

        @Override
        public void switchToDeterminate(int size) {

        }

        @Override
        public void start() {

        }

        @Override
        public void progress(String title, int progress) {

        }

        @Override
        public void finish() {

        }

        @Override
        public void setDisplayName(String name) {

        }

        @Override
        public void progress(int count) {

        }

        @Override
        public void start(int count) {

        }

    }

    private final BuildActionExecutor.EBuildActionType actionType;
    private final IBuildProblemHandler buildProblemHandler;
    private final BuildOptions buildOptions;
    private final CheckOptions checkOptions;
    private final IMutex MUTEX = new IMutex() {
        final Lock lock = new ReentrantLock();

        @Override
        public void readAccess(Runnable r) {
            r.run();
        }

        @Override
        public Lock getLongProcessLock() {
            return lock;
        }

    };

    public DefaultBuildEnvironment(BuildActionExecutor.EBuildActionType actionType, IBuildProblemHandler buildProblemHandler, BuildOptions buildOptions, CheckOptions checkOptions) {
        this.actionType = actionType;
        this.buildProblemHandler = buildProblemHandler;
        this.buildOptions = buildOptions;
        this.checkOptions = checkOptions;
    }

    private static BuildOptions createBuildOptions() {
        BuildOptions opts = BuildOptions.Factory.newInstance();
        opts.setEnvironment(EnumSet.allOf(ERuntimeEnvironmentType.class));
        return opts;
    }

    private static CheckOptions createCheckOptions() {
        CheckOptions opts = new CheckOptions(false, false, null);
        return opts;
    }

    public DefaultBuildEnvironment(BuildActionExecutor.EBuildActionType actionType, final IProblemHandler problemHandlerDelegate) {
        this(actionType, problemHandlerDelegate, createBuildOptions(), createCheckOptions());
    }

    public DefaultBuildEnvironment(BuildActionExecutor.EBuildActionType actionType, final IBuildProblemHandler problemHandlerDelegate) {
        this(actionType, problemHandlerDelegate, createBuildOptions(), createCheckOptions());
    }

    public DefaultBuildEnvironment(BuildActionExecutor.EBuildActionType actionType, final IProblemHandler problemHandlerDelegate, BuildOptions buildOptions, CheckOptions checkOptions) {
        this(actionType, new IBuildProblemHandler() {
            boolean wasErrors = false;

            @Override
            public void clear() {
                wasErrors = false;
            }

            @Override
            public boolean wasErrors() {
                return wasErrors;
            }

            @Override
            public void accept(RadixProblem problem) {
                problemHandlerDelegate.accept(problem);
            }
        }, buildOptions, checkOptions);
    }

    @Override
    public void prepare() {

    }

    @Override
    public Logger getLogger() {
        return Logger.getLogger(DefaultBuildEnvironment.class.getName());
    }

    @Override
    public BuildActionExecutor.EBuildActionType getActionType() {
        return actionType;
    }

    @Override
    public IFlowLogger getFlowLogger() {
        return new ConsoleFlowLogger();
    }

    @Override
    public IBuildProblemHandler getBuildProblemHandler() {
        return buildProblemHandler;
    }

    @Override
    public BuildOptions getBuildOptions() {
        return buildOptions;
    }

    @Override
    public CheckOptions getCheckOptions() {
        return checkOptions;
    }

    @Override
    public IBuildDisplayer getBuildDisplayer() {
        return new IBuildDisplayer() {

            @Override
            public IProgressHandleFactory getProgressHandleFactory() {
                return new IProgressHandleFactory() {

                    @Override
                    public IProgressHandle createHandle(String displayName) {
                        return new DummyProgressHandle();
                    }

                    @Override
                    public IProgressHandle createHandle(String displayName, Cancellable allowToCancel) {
                        return new DummyProgressHandle();
                    }
                };
            }

            @Override
            public IStatusDisplayer getStatusDisplayer() {
                return new IStatusDisplayer() {

                    @Override
                    public void setStatusText(String text) {

                    }
                };
            }

            @Override
            public IDialogUtils getDialogUtils() {
                return new IDialogUtils() {

                    @Override
                    public void messageError(String error) {

                    }

                    @Override
                    public void messageError(Exception ex) {

                    }

                    @Override
                    public void messageInformation(String information) {

                    }
                };
            }
        };
    }

    @Override
    public IMutex getMutex() {
        return MUTEX;
    }

    @Override
    public ILifecycleManager getLifecycleManager() {
        return new ILifecycleManager() {

            @Override
            public void saveAll() {

            }

            @Override
            public void exit() {

            }
        };
    }

    @Override
    public void displayResults() {
    }

    @Override
    public void targetsDetermined(Set<Definition> sucseedCheckDefinititons, List<Definition> failedCheckDefinitions) {
    }

    @Override
    public void complete() {
    }

}
