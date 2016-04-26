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

import com.trolltech.qt.core.Qt.CheckState;
import com.trolltech.qt.gui.QCheckBox;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QSpinBox;
import java.util.EnumSet;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.editors.monitoring.diagram.MetricHistWidget;
import org.radixware.kernel.explorer.env.Application;


public class CfgUpdatePeriodDialog extends ExplorerDialog {
    
    private final QSpinBox sbMetricsPeriod;
    private final MetricHistWidget parent;
    private final QCheckBox cbIsUpdate;
    private final QLabel lbPeriod;
    
    public CfgUpdatePeriodDialog(final IClientEnvironment environment,final MetricHistWidget parent) {
        super(environment,parent, "SelectDeletingMode");
        this.parent=parent;     
        
        sbMetricsPeriod=createSpinBox();
        lbPeriod = new QLabel( this);
        cbIsUpdate=new QCheckBox(this);
        
        this.setWindowTitle(Application.translate("SystemMonitoring", "Update Period Editor"));
        createUi(parent.isUpdate());
    }
    
    private QSpinBox createSpinBox(){        
        QSpinBox spinBox=new QSpinBox(this);
        spinBox.setMinimum(1);
        spinBox.setMaximum(86400);//24 часа
        spinBox.setSuffix(" c");
        return spinBox;
    }

    private void createUi(boolean isUpdate) {
        
        int metricUpdPeriod =parent.getMetricTimer().interval();        
        QHBoxLayout metricsLayout = createCfgLayout(Application.translate("SystemMonitoring", "Metrics update period"), metricUpdPeriod, sbMetricsPeriod);
        
        cbIsUpdate.setText(Application.translate("SystemMonitoring" ,"Set update period"));
        cbIsUpdate.stateChanged.connect(this, "autoRangeChange(Integer)");
        cbIsUpdate.setCheckState(isUpdate ? CheckState.Checked : CheckState.Unchecked);
        autoRangeChange(0);
        
        dialogLayout().addWidget(cbIsUpdate);
        dialogLayout().addLayout(metricsLayout);        
        addButtons(EnumSet.of(EDialogButtonType.OK,EDialogButtonType.CANCEL),true);

        //this.setWindowModality(WindowModality.WindowModal);
    }

    private QHBoxLayout createCfgLayout(String label, int val,QSpinBox spin) {
        QHBoxLayout layout = new QHBoxLayout();
        lbPeriod.setText(label + ":");
        spin.setValue(val/1000);
        layout.addWidget(lbPeriod);
        layout.addWidget(spin,1);
        return layout;
    }

    public int getMetricsPeriod(){
        return sbMetricsPeriod.value()*1000;
    }

    @Override
    public void accept() {
        saveGeometryToConfig();
        super.accept();
    }
    
    private void autoRangeChange(Integer n){
        boolean isEnable=cbIsUpdate.isChecked();
        sbMetricsPeriod.setEnabled(isEnable);
        lbPeriod.setEnabled(isEnable);
        //if(isEnable){
        //    buttonBox.button(StandardButton.Ok).setEnabled(true);
        //}
    }
    
    public boolean isUpdate(){
        return cbIsUpdate.isChecked();
    }
}
