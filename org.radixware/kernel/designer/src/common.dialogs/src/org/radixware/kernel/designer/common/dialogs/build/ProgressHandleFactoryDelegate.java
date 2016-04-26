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

import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;

import org.radixware.kernel.common.builder.api.IProgressHandle;
import org.radixware.kernel.common.builder.api.IProgressHandleFactory;
import org.radixware.kernel.common.defs.ads.build.Cancellable;


public class ProgressHandleFactoryDelegate implements IProgressHandleFactory {

    @Override
    public IProgressHandle createHandle(String arg0) {
        ProgressHandle delegate = ProgressHandleFactory.createHandle(arg0);
        return new ProgressHandleDelegate(delegate);
    }

    @Override
    public IProgressHandle createHandle(final String arg0, final Cancellable allowToCancel) {
        ProgressHandle delegate = ProgressHandleFactory.createHandle(arg0, new org.openide.util.Cancellable() {

            @Override
            public boolean cancel() {
                return allowToCancel.cancel();
            }
        });
        return new ProgressHandleDelegate(delegate);
    }

    private class ProgressHandleDelegate implements IProgressHandle {

        private final ProgressHandle delegate;

        public ProgressHandleDelegate(ProgressHandle delegate) {
            this.delegate = delegate;
        }

        @Override
        public void start() {
            delegate.start();
        }

        @Override
        public void start(int arg0) {
            delegate.start(arg0);
        }

        @Override
        public void switchToDeterminate(int arg0) {
            if (arg0 <= 0) {
                delegate.switchToDeterminate(1);
            } else {
                delegate.switchToDeterminate(arg0);
            }
        }

        @Override
        public void switchToIndeterminate() {
            delegate.switchToIndeterminate();
        }

        @Override
        public void progress(String arg0, int arg1) {
            delegate.progress(arg0, arg1);
        }

        @Override
        public void progress(int arg0) {
            delegate.progress(arg0);
        }

        @Override
        public void setDisplayName(String arg0) {
            delegate.setDisplayName(arg0);
        }

        @Override
        public void finish() {
            delegate.finish();
        }
    }
}
