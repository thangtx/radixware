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
import com.trolltech.qt.gui.QDoubleSpinBox;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumSet;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.env.Application;


public class CfgValueRangeDialog extends ExplorerDialog{
    private QDoubleSpinBox spinBoxMin;
    private QDoubleSpinBox spinBoxMax;
    private QLabel lbMin;
    private QLabel lbMax;
    private QCheckBox cbIsAutoRange;
    
    public CfgValueRangeDialog(final IClientEnvironment environment, QWidget parent, double minVal, double maxVal, boolean isAutoRange){
         super(environment,parent, "CfgDiagramDialog");
         this.setWindowTitle(Application.translate("SystemMonitoring", "Set Scale for Value"));
         createUi( minVal, maxVal,isAutoRange);
    }
    
    private void createUi(double minVal,double maxVal,boolean isAutoRange){
        this.setMinimumSize(100, 130);                  

        QGridLayout layout=new QGridLayout();
        lbMin = new QLabel(Application.translate("SystemMonitoring" ,"Minimum value")+":", this);
        lbMax = new QLabel(Application.translate("SystemMonitoring" ,"Maximum value")+":", this);
        spinBoxMin=createSpinBox(minVal);
        spinBoxMin.valueChanged.connect(this, "check()");
        spinBoxMin.setMaximum(Double.MAX_VALUE);
        spinBoxMin.setDecimals(2);
        spinBoxMax=createSpinBox(maxVal);
        spinBoxMax.valueChanged.connect(this, "check()");
        spinBoxMax.setMaximum(Double.MAX_VALUE);
        spinBoxMax.setDecimals(2);
        layout.addWidget(lbMin,0,0);
        layout.addWidget(spinBoxMin,0,1);
        layout.addWidget(lbMax,1,0);
        layout.addWidget(spinBoxMax,1,1);        

        cbIsAutoRange=new QCheckBox(this);
        cbIsAutoRange.setText(Application.translate("SystemMonitoring" ,"Auto Range"));
        cbIsAutoRange.stateChanged.connect(this, "autoRangeChange(Integer)");
        cbIsAutoRange.setCheckState(isAutoRange ? CheckState.Checked : CheckState.Unchecked);
        dialogLayout().addWidget(cbIsAutoRange);
        dialogLayout().setContentsMargins(10, 10, 10, 10);
        dialogLayout().addLayout(layout);
        addButtons(EnumSet.of(EDialogButtonType.OK, EDialogButtonType.CANCEL), true);
        //this.setWindowModality(WindowModality.WindowModal);
        this.setVisible(true);
    }
    
    private void check(){
        if(getButton(EDialogButtonType.OK)!=null)
            getButton(EDialogButtonType.OK).setEnabled(spinBoxMin.value()<=spinBoxMax.value());
    }
    
    private void autoRangeChange(Integer n){
        boolean isEnable=!cbIsAutoRange.isChecked();
        spinBoxMin.setEnabled(isEnable);
        spinBoxMax.setEnabled(isEnable);
        lbMin.setEnabled(isEnable);
        lbMax.setEnabled(isEnable); 
        if(isEnable){
            getButton(EDialogButtonType.OK).setEnabled(true);
        }else{
            check();
        }
    }
    
    public boolean isAutoRange(){
        return cbIsAutoRange.isChecked();
    }
    
    public double getMinVal(){
        return spinBoxMin.value();
    }
    
    public double getMaxVal(){
        return spinBoxMax.value();
    }
    
     @Override
    public void accept() {
        saveGeometryToConfig();
        super.accept();
    }
    
    private QDoubleSpinBox createSpinBox(double val){
        QDoubleSpinBox spinBox=new QDoubleSpinBox(this);
        spinBox.setMinimum(0);
        spinBox.setDecimals(3);
        spinBox.setValue(val);
        return spinBox;
    }    
}
