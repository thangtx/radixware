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

package org.radixware.kernel.explorer.editors.eventlog;

import com.trolltech.qt.core.Qt.WindowModality;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QSpinBox;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumSet;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.env.Application;


public class CfgUpdatePeriodDialog extends ExplorerDialog {
    
    private final QSpinBox sbPeriod;
    private final QLabel lbPeriod;

    private final QSpinBox sbEvent;
    private final QLabel lbEvent;
    
    private final QSpinBox sbWarning;
    private final QLabel lbWarning;
    
    private final QSpinBox sbError;
    private final QLabel lbError;

    private final QSpinBox sbAlarm;
    private final QLabel lbAlarm;
    
    public CfgUpdatePeriodDialog(final IClientEnvironment environment, final QWidget parent, final int period, final int eventDuration, final int warningDuration, final int errorDuration, final int alarmDuration) {
        super(environment, parent, "SelectDeletingMode");
        
        sbPeriod = createSpinBox(1, 86400);
        lbPeriod = new QLabel(this);

        sbEvent = createSpinBox(0, 60);
        lbEvent = new QLabel(this);
        
        sbWarning = createSpinBox(0, 60);
        lbWarning = new QLabel(this);

        sbError = createSpinBox(0, 60);
        lbError = new QLabel(this);

        sbAlarm = createSpinBox(0, 60);
        lbAlarm = new QLabel(this);
        
        setWindowTitle(Application.translate("UpdateCfg", "Settings"));
        createUi(period, eventDuration, warningDuration, errorDuration, alarmDuration);
    }

    private QSpinBox createSpinBox(final int min, final int max) {
        final QSpinBox spinBox = new QSpinBox(this);
        spinBox.setMinimum(min);
        spinBox.setMaximum(max);
        spinBox.setSuffix(" " + Application.translate("UpdateCfg", "s"));
        return spinBox;
    }

    private void createUi(final int period, final int eventDuration, final int warningDuration, final int errorDuration, final int alarmDuration) {
        
        final QHBoxLayout periodLayout = createCfgLayout(lbPeriod, Application.translate("UpdateCfg", "Refresh period"), sbPeriod, period);
        final QHBoxLayout eventLayout = createCfgLayout(lbEvent, Application.translate("UpdateCfg", "Event sound duration"), sbEvent, eventDuration);
        final QHBoxLayout warningLayout = createCfgLayout(lbWarning, Application.translate("UpdateCfg", "Warning sound duration"), sbWarning, warningDuration);
        final QHBoxLayout errorLayout = createCfgLayout(lbError, Application.translate("UpdateCfg", "Error sound duration"), sbError, errorDuration);
        final QHBoxLayout alarmLayout = createCfgLayout(lbAlarm, Application.translate("UpdateCfg", "Alarm sound duration"), sbAlarm, alarmDuration);        
        
        dialogLayout().addLayout(periodLayout);
        dialogLayout().addLayout(eventLayout);
        dialogLayout().addLayout(warningLayout);
        dialogLayout().addLayout(errorLayout);
        dialogLayout().addLayout(alarmLayout);
        addButtons(EnumSet.of(EDialogButtonType.OK, EDialogButtonType.CANCEL), true);
    }

    private QHBoxLayout createCfgLayout(QLabel lb, String label, QSpinBox spin, int val) {
        QHBoxLayout layout = new QHBoxLayout();
        lb.setText(label + ":");
        spin.setValue(val);
        layout.addWidget(lb);
        layout.addWidget(spin);
        return layout;
    }

    public int getPeriod(){
        return sbPeriod.value();
    }

    public int getEventDuration() {
        return sbEvent.value();
    }

    public int getWarningDuration() {
        return sbWarning.value();
    }

    public int getErrorDuration() {
        return sbError.value();
    }

    public int getAlarmDuration() {
        return sbAlarm.value();
    }
    
    @Override
    public void accept() {
        saveGeometryToConfig();
        super.accept();
    }    
}