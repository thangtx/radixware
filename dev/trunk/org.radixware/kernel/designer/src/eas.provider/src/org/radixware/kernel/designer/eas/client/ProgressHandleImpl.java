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

package org.radixware.kernel.designer.eas.client;

import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.radixware.kernel.common.client.views.IProgressHandle;


class ProgressHandleImpl implements IProgressHandle {

    private ProgressHandle impl;
    private boolean wasCancelled = false;
    private String dn;
    private String text;
    private boolean canCancel;
    private int max;
    private int val;
    private Cancellable cancellable;

    public ProgressHandleImpl() {
        this(null);
    }

    public ProgressHandleImpl(Cancellable cancellable) {
        this.cancellable = cancellable;
    }
    
    @Override
    public void startProgress(String string, boolean canCancel) {
        startProgress(string, canCancel, false);
    }

    @Override
    public void startProgress(String string, boolean canCancel, boolean forceShow) {
        dn = string;
        if (canCancel) {
            impl = ProgressHandleFactory.createHandle(string, new org.openide.util.Cancellable() {

                @Override
                public boolean cancel() {
                    ProgressHandleImpl.this.cancel();
                    return true;
                }
            });
        } else {
            impl = ProgressHandleFactory.createHandle(string);
        }
        this.canCancel = canCancel;
    }

    @Override
    public boolean isActive() {
        return impl != null;
    }

    @Override
    public void cancel() {
        if (cancellable != null) {
            cancellable.onCancel();
        }
        wasCancelled = true;
    }

    @Override
    public boolean wasCanceled() {
        return wasCancelled;
    }

    @Override
    public void finishProgress() {
        if (impl != null) {
            impl.finish();
        }
    }

    @Override
    public void setTitle(String string) {
        if (impl != null) {
            impl.setDisplayName(string);
        }
        dn = string;
    }

    @Override
    public String getTitle() {
        return dn;
    }

    @Override
    public void setText(String string) {
        text = string;
        if (impl != null) {
            impl.progress(string);
        }
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setMaximumValue(int i) {
        impl.switchToDeterminate(i);
        max = i;
    }

    @Override
    public int getMaximumValue() {
        return max;
    }

    @Override
    public void setValue(int i) {
        impl.progress(i);
        val = i;
    }

    @Override
    public int getValue() {
        return val;
    }

    @Override
    public void incValue() {
        setValue(val + 1);
    }

    @Override
    public boolean canCancel() {
        return canCancel;
    }
}
