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

package org.radixware.utils.tsspellcheck;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QProgressBar;
import com.trolltech.qt.gui.QProgressDialog;
import com.trolltech.qt.gui.QWidget;


final class SpellcheckProgressDialog extends QProgressDialog{

    private final QProgressBar progressBar = new QProgressBar(this);

    public SpellcheckProgressDialog(final QWidget parent){
        super(parent);
        final Qt.WindowFlags windowFlags = new Qt.WindowFlags(Qt.WindowType.Dialog,Qt.WindowType.WindowTitleHint);        
        setWindowFlags(windowFlags);
        setLabelText(QApplication.translate("Spellchecker","Please Wait..."));
        setWindowTitle(QApplication.translate("Spellchecker", "Spellchecking"));
        setMinimumWidth(300);
        setMinimumDuration(0);
        progressBar.setMaximum(0);
        progressBar.setValue(-1);
        progressBar.setTextVisible(false);
        setBar(progressBar);
    }

    public static class SetMaximumValueEvent extends QEvent{
        public final int maximumValue;

        public SetMaximumValueEvent(final int value){
            super(QEvent.Type.User);
            maximumValue = value;
        }
    }

    public static class NextStepEvent extends QEvent{

        public NextStepEvent(){
            super(QEvent.Type.User);
        }
    }

    @Override
    protected void customEvent(final QEvent event) {
        if (event instanceof SetMaximumValueEvent){
            progressBar.setMaximum(((SetMaximumValueEvent)event).maximumValue);
            progressBar.setValue(0);
            progressBar.setTextVisible(true);
            event.setAccepted(true);
        }
        else if  (event instanceof NextStepEvent){
            progressBar.setValue(progressBar.value()+1);
            event.setAccepted(true);
        }
        super.customEvent(event);
    }



}
