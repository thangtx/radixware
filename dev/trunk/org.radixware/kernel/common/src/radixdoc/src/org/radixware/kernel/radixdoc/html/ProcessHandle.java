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

package org.radixware.kernel.radixdoc.html;

import org.radixware.kernel.common.components.IProgressHandle;
import org.radixware.kernel.common.components.ICancellable;


final class ProcessHandle implements IProgressHandle, ICancellable {

    private final IProgressHandle progressHandle;
    private final ICancellable cancellable;
    private int progress = 0;

    public ProcessHandle(IProgressHandle progressHandle, ICancellable cancellable) {
        this.progressHandle = progressHandle;
        this.cancellable = cancellable;
    }

    @Override
    public void switchToIndeterminate() {
        if (progressHandle != null) {
            progressHandle.switchToIndeterminate();
        }
    }

    @Override
    public void switchToDeterminate(int size) {
        if (progressHandle != null) {
            progressHandle.switchToDeterminate(size);
        }
    }

    @Override
    public void start() {
        if (progressHandle != null) {
            progressHandle.start();
        }
    }

    @Override
    public void progress(String title, int progress) {
        if (progressHandle != null) {
            progressHandle.progress(title, progress);
            this.progress = progress;
        }
    }

    @Override
    public void finish() {
        if (progressHandle != null) {
            progressHandle.finish();
        }
    }

    @Override
    public void setDisplayName(String name) {
        if (progressHandle != null) {
            progressHandle.setDisplayName(name);
        }
    }

    @Override
    public void progress(int count) {
        if (progressHandle != null) {
            progressHandle.progress(count);
            this.progress = count;
        }
    }

    @Override
    public void start(int count) {
        if (progressHandle != null) {
            progressHandle.start(count);
        }
    }

    @Override
    public boolean cancel() {
        return cancellable != null && cancellable.cancel();
    }

    @Override
    public boolean wasCancelled() {
        return cancellable != null && cancellable.wasCancelled();
    }

    void incProgress() {
        progress(progress + 1);
    }

    void incProgress(String name) {
        progress(name, progress + 1);
    }
}
