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

package org.radixware.kernel.explorer.trace;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.QTimer;
import com.trolltech.qt.core.Qt.FocusPolicy;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.RunParams;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchNameError;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;


public class TraceTrayItem extends QPushButton {

    public static class ChangeSeverityEvent extends QEvent{
        public final EEventSeverity eventSeverity;
        public ChangeSeverityEvent(final EEventSeverity eventSeverity){
            super(QEvent.Type.User);
            this.eventSeverity = eventSeverity;
        }
    }
    
    private final IClientEnvironment environment;
    private final QTimer timer = new QTimer(this);
    private boolean isIconVisible;
    private EEventSeverity severity;

    public TraceTrayItem(final QWidget parent, final IClientEnvironment environment) {
        super(parent);
        this.environment = environment;
        clicked.connect(this, "onClick()");
        setIconSize(new QSize(16, 16));
        setFocusPolicy(FocusPolicy.NoFocus);
        final int maxSize = Application.getMainWindow().statusBar().sizeHint().height() - 2;
        setMaximumHeight(Math.max(maxSize, 20));
        setMaximumWidth(30);
        setVisible(false);
    }

    public void maxSeverityChanged(EEventSeverity severity) {
        this.severity = severity;
        if (severity == EEventSeverity.NONE) {
            setVisible(false);
        } else {
            setVisible(true);
            setIcon(ExplorerIcon.getQIcon(ClientIcon.TraceLevel.findEventSeverityIcon(severity, environment)));
            String traceMinSeverity = RunParams.getTraceMinSeverity();
            if (traceMinSeverity != null) {
                try {
                    if (severity.getValue() < EEventSeverity.getForName(traceMinSeverity).getValue()) {
                        timer.disconnect();
                        isIconVisible = true;
                    } else {
                        blinkOnSeverityEvent(severity);
                    }
                } catch (NoConstItemWithSuchValueError ex) {
                }
            }
        }
    }

    public void blinkOnSeverityEvent(final EEventSeverity severity) {
        timer.disconnect();
        timer.timeout.connect(TraceTrayItem.this, "onTimer()");
        timer.setInterval(500);
        timer.start();
    }

    @SuppressWarnings("unused")
    private void onTimer() {
        if (isIconVisible) {
            setIcon(null);
            isIconVisible = false;
        } else {
            setIcon(ExplorerIcon.getQIcon(ClientIcon.TraceLevel.findEventSeverityIcon(TraceTrayItem.this.severity, environment)));
            isIconVisible = true;
        }
    }

    @SuppressWarnings("unused")
    private void onClick() {
        Application.getInstance().getActions().showTrace.trigger();
    }

    @Override
    protected void customEvent(final QEvent event) {
        if (event instanceof ChangeSeverityEvent){
            maxSeverityChanged(((ChangeSeverityEvent)event).eventSeverity);
        }
        super.customEvent(event);
    }


}
