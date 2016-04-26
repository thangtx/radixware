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

package org.radixware.kernel.explorer.editors.form;

import com.trolltech.qt.core.Qt.CheckState;
import com.trolltech.qt.gui.*;
import java.util.EnumSet;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.env.Application;


public class CfgUpdatePeriodDialog extends ExplorerDialog {
    
    private final QSpinBox sbFormsPeriod;
    private final QCheckBox cbIsUpdate, cbIsSound;
    private final QLabel lbPeriod, lbSound;
    
    public CfgUpdatePeriodDialog(final IClientEnvironment environment, final QWidget parent, final boolean isUpdate, final int period, final boolean isSound) {
        super(environment, parent, "SelectDeletingMode");
        
        sbFormsPeriod = createSpinBox();
        lbPeriod = new QLabel(this);
        lbSound = new QLabel(this);
        cbIsUpdate = new QCheckBox(this);
        cbIsSound = new QCheckBox(this);
        
        setWindowTitle(Application.translate("UpdateCfg", "Settings"));
        createUi(isUpdate, period, isSound);
    }

    private QSpinBox createSpinBox(){        
        QSpinBox spinBox = new QSpinBox(this);
        spinBox.setMinimum(1);
        spinBox.setMaximum(86400); //24 часа
        spinBox.setSuffix(" c");
        return spinBox;
    }

    private void createUi(boolean isUpdate, int period, boolean isSound) {    
        QHBoxLayout periodLayout = createPeriodLayout(Application.translate("UpdateCfg", "Refresh period"), period, sbFormsPeriod);
        QHBoxLayout soundLayout = createSoundLayout(Application.translate("UpdateCfg", "Sound signal"), isSound);
        
        cbIsUpdate.setText(Application.translate("UpdateCfg", "Set refresh period"));
        cbIsUpdate.stateChanged.connect(this, "autoRangeChange(Integer)");
        cbIsUpdate.setCheckState(isUpdate ? CheckState.Checked : CheckState.Unchecked);
        autoRangeChange(0);
        
        dialogLayout().addWidget(cbIsUpdate);
        dialogLayout().addLayout(periodLayout);
        dialogLayout().addLayout(soundLayout);

        addButtons(EnumSet.of(EDialogButtonType.OK, EDialogButtonType.CANCEL), true);
    }

    private QHBoxLayout createPeriodLayout(String label, int val, QSpinBox spin) {
        QHBoxLayout layout = new QHBoxLayout();
        lbPeriod.setText(label + ":");
        spin.setValue(val);
        layout.addWidget(lbPeriod);
        layout.addWidget(spin);
        return layout;
    }
    
    private QHBoxLayout createSoundLayout(String label, boolean isSound) {
        QHBoxLayout layout = new QHBoxLayout();
        lbSound.setText(label);
        cbIsSound.setChecked(isSound);
        layout.addWidget(lbSound);
        layout.addWidget(cbIsSound);
        return layout;
    }

    private void autoRangeChange(Integer n) {
        boolean isEnable = isUpdate();
        sbFormsPeriod.setEnabled(isEnable);
        lbPeriod.setEnabled(isEnable);
        lbSound.setEnabled(isEnable);
        cbIsSound.setEnabled(isEnable);
    }
     
    @Override
    public void accept() {
        saveGeometryToConfig();
        super.accept();
    }
    
    public int getPeriod(){
        return sbFormsPeriod.value();
    }
    
    public boolean isSound() {
        return cbIsSound.isChecked();
    }
    
    public boolean isUpdate() {
        return cbIsUpdate.isChecked();
    }
}