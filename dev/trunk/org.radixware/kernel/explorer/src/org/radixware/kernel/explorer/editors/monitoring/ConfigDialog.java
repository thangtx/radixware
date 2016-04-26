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

package org.radixware.kernel.explorer.editors.monitoring;

import com.trolltech.qt.core.Qt.CheckState;
import com.trolltech.qt.gui.QCheckBox;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QSpinBox;
import java.util.EnumSet;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.env.Application;


public class ConfigDialog extends ExplorerDialog {

    private final QSpinBox sbMetricsPeriod;
    //private final QSpinBox sbEventPeriod;
    private final  QLabel lbMetricsPeriod;
    //private final  QLabel lbEventPeriod;
    private final QCheckBox cbIsUpdate;
    private final  UnitsWidget parent;

    public ConfigDialog(final IClientEnvironment environment, final UnitsWidget parent) {
        super(environment,parent, "SelectDeletingMode");
        this.parent=parent;
        
        lbMetricsPeriod=new QLabel( this);
        //lbEventPeriod=new QLabel( this); 
        sbMetricsPeriod=createSpinBox();        
        //sbEventPeriod=createSpinBox();
        cbIsUpdate=new QCheckBox(this);
        
        this.setWindowTitle(Application.translate("SystemMonitoring", "Choose Update Period"));
        createUi();
    }
    
    private QSpinBox createSpinBox(){        
        final QSpinBox spinBox=new QSpinBox(this);
        spinBox.setMinimum(1);
        spinBox.setMaximum(86400);//24 часа
        spinBox.setSuffix(" c");
        return spinBox;
    }

    private void createUi() {

        final int metricUpdPeriod =parent.getMetricTimer().interval(); 
        lbMetricsPeriod.setText(Application.translate("SystemMonitoring", "Information update period"));
        final QHBoxLayout metricsLayout = createCfgLayout(lbMetricsPeriod, metricUpdPeriod, sbMetricsPeriod);
        
        //lbEventPeriod.setText(Application.translate("SystemMonitoring", "Event update period"));
        //int eventUpdPeriod = parent.getEventTimer().interval();
        //QHBoxLayout eventLayout = createCfgLayout(lbEventPeriod, eventUpdPeriod, sbEventPeriod);
        
        cbIsUpdate.setText(Application.translate("SystemMonitoring" ,"Set update period"));
        cbIsUpdate.stateChanged.connect(this, "autoRangeChange(Integer)");
        cbIsUpdate.setCheckState(parent.isUpdateEnabled() ? CheckState.Checked : CheckState.Unchecked);
        autoRangeChange(0);
        
        dialogLayout().addWidget(cbIsUpdate);
        dialogLayout().addLayout(metricsLayout);
        //dialogLayout().addLayout(eventLayout);
        addButtons(EnumSet.of(EDialogButtonType.OK, EDialogButtonType.CANCEL), true);

        //this.setWindowModality(WindowModality.WindowModal);
    }
    
     private void autoRangeChange(final Integer n){
        final boolean isEnable=cbIsUpdate.isChecked();
        sbMetricsPeriod.setEnabled(isEnable);
        //sbEventPeriod.setEnabled(isEnable);
        lbMetricsPeriod.setEnabled(isEnable);
        //lbEventPeriod.setEnabled(isEnable);
        //if(isEnable){
        //    buttonBox.button(StandardButton.Ok).setEnabled(true);
        //}
    }

    private QHBoxLayout createCfgLayout( final QLabel lbPeriod, final int val,final QSpinBox spin) {
        final QHBoxLayout layout = new QHBoxLayout();
        spin.setValue(val/1000);
        layout.addWidget(lbPeriod);
        layout.addWidget(spin);
        return layout;
    }

    public int getMetricsPeriod(){
        return sbMetricsPeriod.value()*1000;
    }

    /*public int getEventPeriod(){
        return sbEventPeriod.value()*1000;
    }*/

    @Override
    public void accept() {
        saveGeometryToConfig();
        super.accept();
    }
    
    public boolean isUpdate(){
        return cbIsUpdate.isChecked();
    }
}
