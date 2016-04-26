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

package org.radixware.kernel.explorer.dialogs;

import com.trolltech.qt.core.QTimerEvent;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.views.IDialog;
import org.radixware.kernel.common.client.widgets.IPeriodicalTask;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.client.widgets.TimerEventHandler;
import org.radixware.kernel.explorer.types.RdxIcon;
import org.radixware.kernel.explorer.widgets.TimersController;


public abstract class QtDialog extends QDialog implements IDialog{

    public QtDialog(QWidget parent){
        super(parent);
    }

    public QtDialog(){
        super();
    }


//----------------------------------------------------------------------------
//    client dialog implementation
//----------------------------------------------------------------------------

    @Override
    public DialogResult execDialog() {
        int result = exec();
        if (result == DialogCode.Rejected.value()) {
            return DialogResult.REJECTED;
        }
        return DialogResult.ACCEPTED;
    }

    @Override
    public DialogResult execDialog(IWidget parent) {
        if (parent instanceof QWidget) {
            this.setParent((QWidget) parent,windowFlags());
        }
        int result = exec();
        if (result == DialogCode.Rejected.value()) {
            return DialogResult.REJECTED;
        }
        return DialogResult.ACCEPTED;
    }

    @Override
    public void acceptDialog() {
        accept();
    }

    @Override
    public void rejectDialog() {
        reject();
    }

    @Override
    public DialogResult getDialogResult() {
        int result = result();
        if (result == DialogCode.Rejected.value()) {
            return DialogResult.REJECTED;
        }
        return DialogResult.ACCEPTED;
    }


    @Override
    public String getWidowTitle() {
        return windowTitle();
    }

    @Override
    public Icon getWindowIcon() {
        QIcon icon = super.windowIcon();
        if (icon instanceof Icon) {
            return (Icon) icon;
        } else {
            return new RdxIcon(icon);
        }
    }

    @Override
    public void setWindowIcon(Icon icon) {
        if (icon instanceof QIcon) {
            setWindowIcon((QIcon) icon);
        }
    }

    private final EventSupport eventSupport = new EventSupport(this);    
    
    @Override
    public EventSupport getEventSupport() {
        return eventSupport;
    }

    @Override
    public String getObjectName() {
        return objectName();
    }

    @Override
    public boolean isDisposed() {
        return nativeId()==0;
    }
    
    private final TimersController timers = new TimersController();
    
    public IPeriodicalTask startTimer(final TimerEventHandler handler, final int interval) {
        return timers.startTimer(handler, interval, this);
    }

    @Override
    public IPeriodicalTask startTimer(final TimerEventHandler handler) {
        return timers.startTimer(handler, this);
    }

    @Override
    public void killTimer(final IPeriodicalTask task) {
        timers.killTimer(task, this);
    }

    @Override
    protected void timerEvent(final QTimerEvent event) {
        if (!timers.processTimerEvent(event)){
            super.timerEvent(event);
        }
    }

    @Override
    protected void closeEvent(QCloseEvent event) {
        timers.clearTimers();
        super.closeEvent(event);
    }
}
