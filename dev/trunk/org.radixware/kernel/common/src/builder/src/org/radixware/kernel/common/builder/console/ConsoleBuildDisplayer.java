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

package org.radixware.kernel.common.builder.console;

import org.radixware.kernel.common.defs.ads.build.Cancellable;
import org.radixware.kernel.common.builder.api.IBuildDisplayer;
import org.radixware.kernel.common.builder.api.IDialogUtils;
import org.radixware.kernel.common.builder.api.IProgressHandle;
import org.radixware.kernel.common.builder.api.IProgressHandleFactory;
import org.radixware.kernel.common.builder.api.IStatusDisplayer;


public class ConsoleBuildDisplayer implements IBuildDisplayer {

    private IStatusDisplayer statusDisplayer = new ConsoleStatusDisplayer();
    private IProgressHandleFactory phFactory = new ConsoleProgressHandleFactory();
    private ConsoleDialogUtils dlgUtils = new ConsoleDialogUtils();

    @Override
    public IStatusDisplayer getStatusDisplayer() {
        return statusDisplayer;
    }

    @Override
    public IProgressHandleFactory getProgressHandleFactory() {
        return phFactory;
    }

    @Override
    public IDialogUtils getDialogUtils() {
        return dlgUtils;
    }

    private class ConsoleStatusDisplayer implements IStatusDisplayer {

        @Override
        public void setStatusText(String text) {
            //console has no status bar
        }
    }

    private class ConsoleProgressHandleFactory implements IProgressHandleFactory {

        @Override
        public IProgressHandle createHandle(String displayName) {
            return new ConsoleProgressHandle();
        }

        @Override
        public IProgressHandle createHandle(String displayName, Cancellable allowToCancel) {
            return new ConsoleProgressHandle();
        }
    }

    private class ConsoleProgressHandle implements IProgressHandle {

        //Console has no ProgressBar
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
    }

    private class ConsoleDialogUtils implements IDialogUtils {

        @Override
        public void messageError(String error) {
            System.err.println(error);
        }

        @Override
        public void messageInformation(String information) {
            System.out.println(information);
        }

        @Override
        public void messageError(Exception ex) {
            System.err.println(ex);
        }
    }
}
