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

package org.radixware.kernel.explorer.env.progress;

import org.radixware.kernel.common.client.views.IProgressHandle;


public class DummyProgressHandle implements IProgressHandle{
    
    private String text, title;
    private int maxValue, value;
    private boolean started, cancelled, cancellable;

    @Override
    public void startProgress(String text, boolean canCancel) {
        startProgress(text, canCancel, false);
    }
    
    @Override
    public void startProgress(String text, boolean canCancel, boolean forcedShow) {
        this.text = text;
        started = true;
        cancellable = canCancel;
    }

    @Override
    public boolean isActive() {
        return started;
    }

    @Override
    public void cancel() {
        cancelled = true;
        started = false;
    }

    @Override
    public boolean wasCanceled() {
        return cancelled;
    }

    @Override
    public void finishProgress() {
        started = false;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setMaximumValue(int value) {
        maxValue = value;
    }

    @Override
    public int getMaximumValue() {
        return maxValue;
    }

    @Override
    public void setValue(int value) {
        this.value = value;
    }
    
    @Override
    public void incValue() {
        value++;
    }
    

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public boolean canCancel() {
        return cancellable;
    }
    
}
