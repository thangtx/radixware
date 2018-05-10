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

package org.radixware.kernel.common.builder.utils;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;
import org.radixware.kernel.common.builder.BuildActionExecutor.EBuildActionType;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.build.Cancellable;
import org.radixware.kernel.common.builder.api.IBuildDisplayer;
import org.radixware.kernel.common.builder.api.IBuildEnvironment;
import org.radixware.kernel.common.builder.api.IBuildProblemHandler;
import org.radixware.kernel.common.builder.api.IDialogUtils;
import org.radixware.kernel.common.defs.ads.build.IFlowLogger;
import org.radixware.kernel.common.builder.api.ILifecycleManager;
import org.radixware.kernel.common.builder.api.IMutex;
import org.radixware.kernel.common.builder.api.IProgressHandle;
import org.radixware.kernel.common.builder.api.IProgressHandleFactory;
import org.radixware.kernel.common.builder.api.IStatusDisplayer;
import org.radixware.kernel.common.builder.check.common.CheckOptions;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.build.BuildOptions;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.types.Id;


public class DefaultBuildEnvironment implements IBuildEnvironment {

    @Override
    public void prepare() {
    }

    @Override
    public Logger getLogger() {
        return Logger.getAnonymousLogger();
    }

    @Override
    public IFlowLogger getFlowLogger() {
        return new TestFlowLogger();
    }

    @Override
    public void targetsDetermined(Set<Definition> sucseedCheckDefinititons, List<Definition> failedCheckDefinitions) {
    }

    @Override
    public CheckOptions getCheckOptions() {
        return new CheckOptions();
    }

//    @Override
//    public void targetsDetermined(Set<AdsDefinition> sucseedCheckDefinititons, List<AdsDefinition> failedCheckDefinitions) {
//        //
//    }
    private static class PH implements IBuildProblemHandler {

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
            System.err.println(problem.getMessage());
            if (problem.getSeverity() == RadixProblem.ESeverity.ERROR) {
                wasErrors = true;
            }
        }
    }
    private final PH ph = new PH();

    @Override
    public IBuildProblemHandler getBuildProblemHandler() {
        return ph;
    }

    @Override
    public BuildOptions getBuildOptions() {
        BuildOptions opts = BuildOptions.Factory.newInstance();
        opts.setEnvironment(EnumSet.allOf(ERuntimeEnvironmentType.class));
        return opts;
    }

    @Override
    public IBuildDisplayer getBuildDisplayer() {
        return new IBuildDisplayer() {
            @Override
            public IProgressHandleFactory getProgressHandleFactory() {
                return new IProgressHandleFactory() {
                    @Override
                    public IProgressHandle createHandle(String displayName) {
                        return new IProgressHandle() {
                            private String handle = Id.Factory.newInstance(EDefinitionIdPrefix.USER_FUNC_CLASS).toString().substring(10, 20);

                            @Override
                            public void start() {
                                System.out.println("HANDLE" + handle + ":  Progress started");
                            }

                            @Override
                            public void start(int count) {
                                System.out.println("HANDLE" + handle + ":  Progress started (" + count + ")");
                            }

                            @Override
                            public void switchToDeterminate(int count) {
                                System.out.println("HANDLE" + handle + ":  Switch to determinate mode (" + count + ")");
                            }

                            @Override
                            public void switchToIndeterminate() {
                                System.out.println("HANDLE" + handle + ":  Switch to indeterminate mode");
                            }

                            @Override
                            public void progress(String message, int count) {
                                System.out.println("HANDLE" + handle + ":  " + message + " (" + count + ")");
                            }

                            @Override
                            public void progress(int count) {
                                System.out.println("HANDLE" + handle + ":  (" + count + ")");
                            }

                            @Override
                            public void setDisplayName(String name) {
                            }

                            @Override
                            public void finish() {
                                System.out.println("HANDLE" + ":  finished");
                            }
                        };
                    }

                    @Override
                    public IProgressHandle createHandle(String displayName, Cancellable allowToCancel) {
                        return new IProgressHandle() {
                            private String handle = Id.Factory.newInstance(EDefinitionIdPrefix.USER_FUNC_CLASS).toString().substring(10, 20);

                            @Override
                            public void start() {
                                System.out.println("HANDLE" + handle + ":  Progress started");
                            }

                            @Override
                            public void start(int count) {
                                System.out.println("HANDLE" + handle + ":  Progress started (" + count + ")");
                            }

                            @Override
                            public void switchToDeterminate(int count) {
                                System.out.println("HANDLE" + handle + ":  Switch to determinate mode (" + count + ")");
                            }

                            @Override
                            public void switchToIndeterminate() {
                                System.out.println("HANDLE" + handle + ":  Switch to indeterminate mode");
                            }

                            @Override
                            public void progress(String message, int count) {
                                System.out.println("HANDLE" + handle + ":  " + message + " (" + count + ")");
                            }

                            @Override
                            public void progress(int count) {
                                System.out.println("HANDLE" + handle + ":  (" + count + ")");
                            }

                            @Override
                            public void setDisplayName(String name) {
                            }

                            @Override
                            public void finish() {
                                System.out.println("HANDLE" + ":  finished");
                            }
                        };
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
    private static final Lock lock = new ReentrantLock();

    @Override
    public IMutex getMutex() {
        return new IMutex() {
            @Override
            public void readAccess(Runnable r) {
                r.run();
            }

            @Override
            public Lock getLongProcessLock() {
                return lock;
            }
        };
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
    public void complete() {
    }

    @Override
    public EBuildActionType getActionType() {
        return EBuildActionType.CLEAN_AND_BUILD;
    }
}
