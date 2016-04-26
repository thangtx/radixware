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

package org.radixware.kernel.designer.common.dialogs.build;

import java.lang.reflect.InvocationTargetException;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import org.openide.LifecycleManager;
import org.openide.awt.StatusDisplayer;
import org.radixware.kernel.common.builder.BuildActionExecutor.EBuildActionType;
import org.radixware.kernel.common.builder.api.IBuildDisplayer;
import org.radixware.kernel.common.builder.api.IBuildEnvironment;
import org.radixware.kernel.common.builder.api.IBuildProblemHandler;
import org.radixware.kernel.common.builder.api.IDialogUtils;
import org.radixware.kernel.common.defs.ads.build.IFlowLogger;
import org.radixware.kernel.common.builder.api.ILifecycleManager;
import org.radixware.kernel.common.builder.api.IMutex;
import org.radixware.kernel.common.builder.api.IProgressHandleFactory;
import org.radixware.kernel.common.builder.api.IStatusDisplayer;
import org.radixware.kernel.common.builder.check.common.CheckOptions;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.check.RadixProblemRegistry;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.build.BuildOptions;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;

import org.radixware.kernel.designer.common.dialogs.check.CheckResultsTopComponent;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.utils.RadixMutex;


public class DesignerBuildEnvironment implements IBuildEnvironment {

    private final IBuildDisplayer buildDisplayer = new DesigerBuildDisplayer();
    private final IMutex mutex = new MutexDelegate();
    private final ILifecycleManager lfcmDelegate = new LifecycleManagerDelegate();
    private final IBuildProblemHandler buildProblemHandler = new DesignerBuildProblemHandler();
    private IFlowLogger logger = null;

    @Override
    public void prepare() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                CheckResultsTopComponent.findInstance();
            }
        });
    }
    private EBuildActionType actionType;
    private boolean suicideMode;

    public DesignerBuildEnvironment(EBuildActionType actionType) {
        this(false, actionType);
    }

    public DesignerBuildEnvironment(boolean suicideMode, EBuildActionType actionType) {
        this.actionType = actionType;
        this.suicideMode = suicideMode && actionType != EBuildActionType.RELEASE;
        if (actionType != EBuildActionType.RELEASE) {
            this.logger = FlowLoggerFactory.newBuildLogger(actionType);
        }
    }

    @Override
    public Logger getLogger() {
        return Logger.getLogger(DesignerBuildEnvironment.class.getName());
    }

    @Override
    public CheckOptions getCheckOptions() {
        Object opts = getBuildOptions().getProperty("CheckOptions");
        if (opts instanceof CheckOptions) {
            return (CheckOptions) opts;
        }
        return new CheckOptions(false, true, null);
    }
    private BuildOptions configuredOptions = null;

    @Override
    public BuildOptions getBuildOptions() {
        synchronized (this) {
            if (configuredOptions == null) {
                final BuildOptions[] options = new BuildOptions[1];
                if (actionType != EBuildActionType.RELEASE && actionType != EBuildActionType.UPDATE_DIST && actionType != EBuildActionType.CHECK_DIST && BuildOptions.UserModeHandlerLookup.getUserModeHandler() == null) {
                    if (SwingUtilities.isEventDispatchThread()) {
                        options[0] = ConfigureBuild.configureBuild(suicideMode, actionType);
                    } else {
                        try {
                            SwingUtilities.invokeAndWait(new Runnable() {
                                @Override
                                public void run() {
                                    options[0] = ConfigureBuild.configureBuild(suicideMode, actionType);
                                }
                            });
                        } catch (InterruptedException ex) {
                            return null;
                        } catch (InvocationTargetException ex) {
                            return null;
                        }
                    }
                } else {
                    options[0] = BuildOptions.Factory.newInstance();
                    options[0].setEnvironment(EnumSet.allOf(ERuntimeEnvironmentType.class));
                }
                configuredOptions = options[0];
            }
            return configuredOptions;
        }

    }

    @Override
    public IFlowLogger getFlowLogger() {
        synchronized (this) {
            if (logger == null) {
                logger = FlowLoggerFactory.newBuildLogger(actionType);
            }
            return logger;
        }
    }

    @Override
    public void displayResults() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                CheckResultsTopComponent checkResults = CheckResultsTopComponent.findInstance();
                if (!checkResults.isEmpty()) {
                    checkResults.open();
                    checkResults.requestVisible();
                    checkResults.requestActive();
                } else {
                    StatusDisplayer.getDefault().setStatusText("No errors found during build");
                }
            }
        });
    }

    @Override
    public IBuildDisplayer getBuildDisplayer() {
        return buildDisplayer;
    }

    @Override
    public IMutex getMutex() {
        return mutex;
    }

    @Override
    public ILifecycleManager getLifecycleManager() {
        return lfcmDelegate;
    }

    @Override
    public void complete() {
        return;
    }

    @Override
    public IBuildProblemHandler getBuildProblemHandler() {
        return buildProblemHandler;
    }

    @Override
    public void targetsDetermined(Set<Definition> sucseedCheckDefinititons, List<Definition> failedCheckDefinitions) {
        //do nothing
    }

    private class MutexDelegate implements IMutex {

        @Override
        public void readAccess(Runnable r) {
            RadixMutex.readAccess(r);
        }

        @Override
        public Lock getLongProcessLock() {
            return RadixMutex.getLongProcessLock();
        }
    }

    private class LifecycleManagerDelegate implements ILifecycleManager {

        @Override
        public void saveAll() {
            LifecycleManager.getDefault().saveAll();
        }

        @Override
        public void exit() {
            LifecycleManager.getDefault().exit();
        }
    }

    private class DesigerBuildDisplayer implements IBuildDisplayer {

        private final IProgressHandleFactory phfDelegate = new ProgressHandleFactoryDelegate();
        private final IStatusDisplayer sdDelegate = new StatusDisplayerDelegate();
        private final IDialogUtils duDelegate = new DialogUtilsDelegate();

        @Override
        public IProgressHandleFactory getProgressHandleFactory() {
            return phfDelegate;
        }

        @Override
        public IStatusDisplayer getStatusDisplayer() {
            return sdDelegate;
        }

        @Override
        public IDialogUtils getDialogUtils() {
            return duDelegate;
        }

        private class StatusDisplayerDelegate implements IStatusDisplayer {

            @Override
            public void setStatusText(String arg0) {
                StatusDisplayer.getDefault().setStatusText(arg0);
            }
        }

        private class DialogUtilsDelegate implements IDialogUtils {

            @Override
            public void messageError(String arg0) {
                DialogUtils.messageError(arg0);
            }

            @Override
            public void messageError(Exception arg0) {
                DialogUtils.messageError(arg0);
            }

            @Override
            public void messageInformation(String arg0) {
                DialogUtils.messageInformation(arg0);
            }
        }
    }

    private class DesignerBuildProblemHandler implements IBuildProblemHandler {

        private int errorCount = 0;

        @Override
        public void clear() {
            errorCount = 0;
        }

        @Override
        public boolean wasErrors() {
            return errorCount > 0;
        }

        @Override
        public void accept(RadixProblem problem) {
            if (problem.getSeverity().equals(RadixProblem.ESeverity.ERROR)) {
                errorCount++;
            }
            RadixProblemRegistry.getDefault().accept(problem);
            logger.problem(problem);
        }
    }

    @Override
    public EBuildActionType getActionType() {
        return actionType;
    }
}
