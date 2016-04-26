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

package org.radixware.kernel.common.userreport.common;

import org.radixware.kernel.common.defs.ads.build.NullFlowLogger;


public class BuildUserReportEnvironment implements org.radixware.kernel.common.builder.api.IBuildEnvironment  {

    @Override
    public void prepare() {
    }

    @Override
    public java.util.logging.Logger getLogger() {
        return java.util.logging.Logger.getAnonymousLogger();
    }


    @Override
    public org.radixware.kernel.common.defs.ads.build.IFlowLogger getFlowLogger() {
        //return FlowLoggerFactory.newBuildLogger(actionType);
        return new NullFlowLogger();
    }

    @Override
    public org.radixware.kernel.common.builder.check.common.CheckOptions getCheckOptions() {
        return new org.radixware.kernel.common.builder.check.common.CheckOptions();
    }

    @Override
    public void targetsDetermined(java.util.Set<org.radixware.kernel.common.defs.Definition> sucseedCheckDefinititons, java.util.List<org.radixware.kernel.common.defs.Definition> failedCheckDefinitions) {
        //do nothing
    }

   private final org.radixware.kernel.common.builder.api.IBuildProblemHandler problemHandler=new org.radixware.kernel.common.builder.api.IBuildProblemHandler() {
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
            public void accept(final org.radixware.kernel.common.check.RadixProblem problem) {
                //System.out.println(problem.getMessage());
                if (problem.getSeverity() == org.radixware.kernel.common.check.RadixProblem.ESeverity.ERROR) {
                    wasErrors = true;
                }
            }
        };

    @Override
    public org.radixware.kernel.common.builder.api.IBuildProblemHandler getBuildProblemHandler() {
        return problemHandler;
    }

    @Override
    public org.radixware.kernel.common.defs.ads.build.BuildOptions getBuildOptions() {
        final org.radixware.kernel.common.defs.ads.build.BuildOptions opts = org.radixware.kernel.common.defs.ads.build.BuildOptions.Factory.newInstance();
        opts.setEnvironment(java.util.EnumSet.allOf(org.radixware.kernel.common.enums.ERuntimeEnvironmentType.class));
        return opts;
    }

    @Override
    public org.radixware.kernel.common.builder.api.IBuildDisplayer getBuildDisplayer() {
        return new org.radixware.kernel.common.builder.api.IBuildDisplayer() {
            @Override
            public org.radixware.kernel.common.builder.api.IProgressHandleFactory getProgressHandleFactory() {
                return new org.radixware.kernel.common.builder.api.IProgressHandleFactory() {
                    @Override
                    public org.radixware.kernel.common.builder.api.IProgressHandle createHandle(String displayName) {
                        return new org.radixware.kernel.common.builder.api.IProgressHandle() {
                            @Override
                            public void start() {
                            }

                            @Override
                            public void start(int count) {
                            }

                            @Override
                            public void switchToDeterminate(int count) {
                            }

                            @Override
                            public void switchToIndeterminate() {
                            }

                            @Override
                            public void progress(String message, int count) {
                            }

                            @Override
                            public void progress(int count) {
                            }

                            @Override
                            public void setDisplayName(String name) {
                            }

                            @Override
                            public void finish() {
                            }
                        };
                    }

                    @Override
                    public org.radixware.kernel.common.builder.api.IProgressHandle createHandle(String displayName, org.radixware.kernel.common.defs.ads.build.Cancellable allowToCancel) {
                        return new org.radixware.kernel.common.builder.api.IProgressHandle() {
                            @Override
                            public void start() {
                            }

                            @Override
                            public void start(int count) {
                            }

                            @Override
                            public void switchToDeterminate(int count) {
                            }

                            @Override
                            public void switchToIndeterminate() {
                            }

                            @Override
                            public void progress(String message, int count) {
                            }

                            @Override
                            public void progress(int count) {
                            }

                            @Override
                            public void setDisplayName(String name) {
                            }

                            @Override
                            public void finish() {
                            }
                        };
                    }
                };
            }

            @Override
            public org.radixware.kernel.common.builder.api.IStatusDisplayer getStatusDisplayer() {
                return new org.radixware.kernel.common.builder.api.IStatusDisplayer() {
                    @Override
                    public void setStatusText(String text) {
                    }
                };
            }

            @Override
            public org.radixware.kernel.common.builder.api.IDialogUtils getDialogUtils() {
                return new org.radixware.kernel.common.builder.api.IDialogUtils() {
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
    private  final java.util.concurrent.locks.Lock lock = new java.util.concurrent.locks.ReentrantLock();

    @Override
    public org.radixware.kernel.common.builder.api.IMutex getMutex() {
        return new org.radixware.kernel.common.builder.api.IMutex() {
            @Override
            public void readAccess(Runnable r) {
                r.run();
            }

            @Override
            public java.util.concurrent.locks.Lock getLongProcessLock() {
                return lock;
            }
        };
    }

    @Override
    public org.radixware.kernel.common.builder.api.ILifecycleManager getLifecycleManager() {
        return new org.radixware.kernel.common.builder.api.ILifecycleManager() {
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
    public org.radixware.kernel.common.builder.BuildActionExecutor.EBuildActionType getActionType() {
        return org.radixware.kernel.common.builder.BuildActionExecutor.EBuildActionType.COMPILE_SINGLE;
    }
}
