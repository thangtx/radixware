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

package org.radixware.kernel.explorer.editors.monitoring.diagram.dialogs;

import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QDoubleSpinBox;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QLabel;
import java.util.Calendar;
import java.util.EnumSet;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.editors.monitoring.diagram.MetricHistWidget;
import org.radixware.kernel.explorer.env.Application;


public class CfgTimeRangeDialog extends ExplorerDialog{
    
    private QDoubleSpinBox sbPeriod;
    private QComboBox cbTime;
    
    public CfgTimeRangeDialog(final IClientEnvironment environment, MetricHistWidget parent){
         super(environment,parent, "CfgDiagramDialog");
         this.setWindowTitle(Application.translate("SystemMonitoring", "Choose Time Axis Range"));
         
         String settingsKey = SettingNames.SYSTEM + "/" + "system_monitor_productivity_statictic_period";
         double res= getEnvironment().getConfigStore().readDouble(settingsKey, 1);
        
         settingsKey = SettingNames.SYSTEM + "/" + "system_monitor_productivity_statictic_period_unit";
         int periodUnit = getEnvironment().getConfigStore().readInteger(settingsKey, Calendar.HOUR);        
         
         createUi(res,periodUnit);
    }
    
    private void createUi(final double period,final int periodUnit){
        this.setMinimumSize(100, 100);

        QLabel lbSince = new QLabel(Application.translate("SystemMonitoring", "Show statistics for last") + ":", this);
        
        sbPeriod=new QDoubleSpinBox(this);
        sbPeriod.setMinimum(0.1);
        sbPeriod.setValue(period);
        sbPeriod.setDecimals(1);
        sbPeriod.setMaximum(720);//month
        sbPeriod.valueChanged.connect(this, "onFromValueChanged(Object)");
        
        
        cbTime=new QComboBox(this);      
        cbTime.addItem(Application.translate("SystemMonitoring","Second"),Calendar.SECOND);
        cbTime.addItem(Application.translate("SystemMonitoring","Minute"),Calendar.MINUTE);
        cbTime.addItem(Application.translate("SystemMonitoring","Hour"),Calendar.HOUR);
        int curIndex=cbTime.findData(periodUnit);
        cbTime.setCurrentIndex(curIndex);
        
        QGridLayout layout=new QGridLayout();
        layout.addWidget(lbSince,0,0);
        layout.addWidget(sbPeriod,0,1);
        layout.addWidget(cbTime,0,2);


        dialogLayout().setContentsMargins(10, 10, 10, 10);
        dialogLayout().addLayout(layout);
        addButtons(EnumSet.of(EDialogButtonType.OK, EDialogButtonType.CANCEL), true);
        //this.setWindowModality(WindowModality.WindowModal);
        this.setVisible(true);
    }
    
    @SuppressWarnings("unused")
    private void onFromValueChanged(Object obj) {
        getButton(EDialogButtonType.OK).setEnabled(obj!=null);
    }
    
    public double getPeriod() {
        return sbPeriod.value();
    }
    
    public int getPeriodUnit() {
        return ((Integer)cbTime.itemData(cbTime.currentIndex())).intValue();
    }
    
    @Override
    public void accept() {
        saveGeometryToConfig();
        
        super.accept();
    }

    @Override
    public void reject() {
        super.reject();
    }

}
