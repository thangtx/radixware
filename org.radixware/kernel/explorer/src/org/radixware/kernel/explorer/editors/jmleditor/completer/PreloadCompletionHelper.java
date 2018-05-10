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
package org.radixware.kernel.explorer.editors.jmleditor.completer;

import com.trolltech.qt.core.QTimer;
import java.util.concurrent.TimeUnit;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.EnvironmentVariables;
import org.radixware.kernel.common.utils.SystemPropUtils;


class PreloadCompletionHelper {

    private final CompleterProcessor parent;
    private final IClientEnvironment env;
    private final QTimer preloadStateChecker;
    private final PreloadPhase[] phases;
    private boolean isPreloadCompletionFinished = false;
    private boolean isPreloadCompletionStarted = false;
    
    private static final int CHECK_PREALOAD_FINISHED_INTERVAL_MS = 200;
    private static final int MIN_MEMORY_FOR_BRANCH_PRELOAD_MB = 1800; //Xmx~2Gb
    static final String JML_EDITOR_PRELOAD_COMPLETION_RESULT = "JML_EDITOR_PRELOAD_COMPLETION_RESULT";
    private final boolean usePreloadCompletion;
    
    public PreloadCompletionHelper(CompleterProcessor parent, IClientEnvironment env) {
        this.parent = parent;
        this.env = env;
        
        final Boolean usePreload = SystemPropUtils.getBooleanSystemProp("rdx.userfunc.use.preload.completion");
        if (usePreload != null) {
            usePreloadCompletion = usePreload;
        } else {
            long maxMemoryForApp;
            try {
                maxMemoryForApp = java.lang.management.ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getMax();
            } catch (Throwable e) {
                maxMemoryForApp = Runtime.getRuntime().maxMemory();
            }
            final long totalMemoryMByte = maxMemoryForApp / 1024 / 1024;
            final boolean enoughMemory = totalMemoryMByte >= MIN_MEMORY_FOR_BRANCH_PRELOAD_MB;
            usePreloadCompletion = enoughMemory;
        }
        
        preloadStateChecker = new QTimer(parent);
        preloadStateChecker.setSingleShot(true);
        preloadStateChecker.timeout.connect(this, "checkPreloadCompletionFinished()");
        phases = new PreloadPhase[] {new PreloadBrunchPhase(env), new PreloadCompleterPhase(env, parent)};
    }

    public boolean isCompletionReady() {
        return !isPreloadCompletionStarted || isPreloadCompletionFinished;
    }

    public void startPreloadCompletion() {
        if (usePreloadCompletion && !isPreloadCompletionStarted) {
            isPreloadCompletionStarted = true;
            checkPreloadCompletionFinished();
        }
    }
        
    private boolean checkPreloadCompletionFinished() {
        for (PreloadPhase p : phases) {
            if (!p.isFinished()) {
                if (p.isTimeout()) {
                    break;
                }
                if (!p.isStarted()) {
                    p.exec();
                }
                preloadStateChecker.start(CHECK_PREALOAD_FINISHED_INTERVAL_MS);
                return false;
            }
        }
        afterPreloadCompletion();
        return true;
    }

    private void afterPreloadCompletion() {
        isPreloadCompletionFinished = true;
        if (parent != null) {
            parent.afterPreloadCompletion();
        }
    }

    static class PreloadCompletionInfo {

        private volatile boolean preloadFinished; //set from waiter thread
        
        public boolean isPreloadFinished() {
            return preloadFinished;
        }

        public void setPreloadFinished(boolean preloadFinished) {
            this.preloadFinished = preloadFinished;
        }
    }

    public void cleanup() {
        preloadStateChecker.timeout.disconnect();
    }
    
    private static abstract class PreloadPhase {
        
        private static final long MAX_PRELOAD_WAIT_TIME_MS = TimeUnit.MINUTES.toMillis(2);
        
        private boolean isStarted = false;
        private final long startTimeMillis;

        public PreloadPhase() {
            startTimeMillis = System.currentTimeMillis();
        }
        
        long getStartTimeMs() {
            return startTimeMillis;
        }

        boolean isStarted() {
            return isStarted;
        }
        
        void exec() {
            isStarted = true;
            doExec();
        }
        
        boolean isTimeout() {
            return System.currentTimeMillis() - startTimeMillis >= MAX_PRELOAD_WAIT_TIME_MS;
        }
        
        abstract boolean isFinished();
        
        abstract void doExec();
    }
    
    private static final class PreloadBrunchPhase extends PreloadPhase {

        private final IClientEnvironment env;

        public PreloadBrunchPhase(IClientEnvironment env) {
            this.env = env;
        }

        @Override
        boolean isFinished() {
            if (!isStarted()) {
                return false;
            }
            return !env.getDefManager().getRepository().isPreloadBranchRunning();
        }

        @Override
        void doExec() {
            env.getDefManager().getRepository().preloadBranch();
        }

    }


    private static final class PreloadCompleterPhase extends PreloadPhase {
        
        private final IClientEnvironment env;
        private final CompleterProcessor parent;

        public PreloadCompleterPhase(IClientEnvironment env, CompleterProcessor parent) {
            this.env = env;
            this.parent = parent;
        }

        @Override
        boolean isFinished() {
            final PreloadCompletionInfo info = EnvironmentVariables.get(JML_EDITOR_PRELOAD_COMPLETION_RESULT, env);
            if (info != null) {
                return info.isPreloadFinished();
            }
            return false;
        }

        @Override
        void doExec() {
            EnvironmentVariables.put(JML_EDITOR_PRELOAD_COMPLETION_RESULT, env, new PreloadCompletionInfo());
            parent.beginPreloadCompletion();
        }

    }
}
