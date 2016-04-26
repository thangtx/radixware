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

package org.radixware.kernel.explorer.env.session.resources;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QProgressBar;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.eas.resources.IProgressMonitor;


class ProgressWidget extends QWidget implements IProgressMonitor {

    private static class ProgressBar extends QProgressBar {

        public ProgressBar(final QWidget parent) {
            super(parent);
            this.setTextVisible(false); //RADIX-2209: For valid width in windows os when text is empty .
        }
    }
    private final QVBoxLayout layout = new QVBoxLayout(this);
    private final QLabel label = new QLabel(this);
    private final QProgressBar progressBar = new ProgressBar(this);
    private final boolean showTrace, showProgressBar;
    private final String windowTitle;
    private boolean canCancel, wasCancelled;

    public ProgressWidget(ProgressDialogResource resource, final boolean showProgressBar, final boolean showTrace, final String title) {
        super(resource);
        this.showTrace = showTrace;
        this.showProgressBar = showProgressBar;
        windowTitle = title;
        progressBar.setMaximum(0);
        progressBar.setValue(-1);        
        setupUi();
    }

    private void setupUi() {
        label.setAlignment(Qt.AlignmentFlag.AlignHCenter);
        label.setObjectName("lbText");
        layout.addWidget(label);
        progressBar.setObjectName("progressBar");
        layout.addWidget(progressBar);
        setLayout(layout);
        setSizePolicy(QSizePolicy.Policy.Minimum, QSizePolicy.Policy.Fixed);
    }

    public long getProgressId() {
        return this.nativeId();
    }

    public void setText(final String text) {
        label.setText(text);
    }

    public void setValue(final int value) {
        if (showProgressBar) {
            if (progressBar.maximum()==0){
                progressBar.setMaximum(100);
            }
            progressBar.setValue(value);
        }
    }

    public void setCanCancel(final boolean canCancel) {
        this.canCancel = canCancel;
    }

    public boolean canCancel() {
        return canCancel;
    }

    public void cancel() {
        wasCancelled = true;
    }

    public boolean wasCanceled() {
        return wasCancelled;
    }

    public boolean showTrace() {
        return showTrace;
    }

    public String getText() {
        return label.text();
    }

    public String getWindowTitle() {
        return windowTitle;
    }
}
