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

package org.radixware.kernel.explorer.editors.editmask.timeintervaleditor;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QCheckBox;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EEditMaskOption;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskTimeInterval.Scale;
import org.radixware.kernel.explorer.editors.editmask.IEditMaskEditorSetting;
import org.radixware.kernel.explorer.utils.html.HtmlTableBuilder;
import org.radixware.kernel.explorer.editors.valeditors.ValStrEditor;


final class TimeIntervalQtMaskSetting extends QWidget implements IEditMaskEditorSetting {
    
    private final ValStrEditor maskInput;
    private final QCheckBox autoCheckBox;
    private Scale scale = Scale.values()[0];
    private boolean isChecked = false;
        
    TimeIntervalQtMaskSetting(final IClientEnvironment environment, final QWidget parent) {
        super(parent);
        
        final QVBoxLayout outterLayout = new QVBoxLayout();
        outterLayout.setMargin(0);
        outterLayout.setAlignment(new Qt.Alignment(Qt.AlignmentFlag.AlignTop));
        this.setLayout(outterLayout);
        
        final String labelText = environment.getMessageProvider().translate("EditMask", "Qt mask:");
        final QLabel label = new QLabel(labelText, this); 
        outterLayout.addWidget(label);
        
        final QHBoxLayout innerLayout = new QHBoxLayout();
        final String checkBoxLabelText = environment.getMessageProvider().translate("EditMask", "Auto mask");
        autoCheckBox = new QCheckBox(checkBoxLabelText, this);
        innerLayout.setContentsMargins(new QCheckBox().sizeHint().width(), 0, 0, 0);
        autoCheckBox.stateChanged.connect((QWidget)this, "onCheckBoxStateChanged(Integer)");
        innerLayout.addWidget(autoCheckBox);
        maskInput = new ValStrEditor(environment, this);
        innerLayout.addWidget(maskInput);
        outterLayout.addLayout(innerLayout);
        
        setMaskTooltip(environment);
        
        setFocusProxy(autoCheckBox);
        setDefaultValue();
    }

    @Override
    public void addToXml(final org.radixware.schemas.editmask.EditMask editMask) {
        final String value = maskInput.getValue();
        if(!value.isEmpty()) {
            editMask.getTimeInterval().setMask(value);
        }
    }

    @Override
    public void loadFromXml(final org.radixware.schemas.editmask.EditMask editMask) {
        final org.radixware.schemas.editmask.EditMaskTimeInterval emti = editMask.getTimeInterval();
        if(emti.isSetMask()) {
            final String value = emti.getMask();
            maskInput.setValue(value);
            autoCheckBox.setChecked(value.equals(scale.defaultMask()));
        } else {
            setDefaultValue();
        }
    }

    @Override
    public EEditMaskOption getOption() {
        return EEditMaskOption.TIMEINTERVAL_QTMASK;
    }

    @Override
    public void setDefaultValue() {
        maskInput.setValue("");
    }

    @Override
    public String getValue() {
        return maskInput.getValue();
    }
    
    @SuppressWarnings("unused")
    private void onCheckBoxStateChanged(final Integer state) {
        final Qt.CheckState checkState = Qt.CheckState.resolve(state);
        switch(checkState) {
            case Checked:
                maskInput.setDisabled(true);
                maskInput.setValue(scale.defaultMask());
                isChecked = true;
                break;
                
            case Unchecked:
                maskInput.setEnabled(true);
                setDefaultValue();
                isChecked = false;
                break;
                
            default:
                throw new IllegalArgumentException("Unknown checkbox state");
                
        }
    }
    
        
    public void updateScale(final Scale scale) {
        this.scale = scale;
        if(isChecked) {
            maskInput.setValue(scale.defaultMask());
        }
    }

    private void setMaskTooltip(final IClientEnvironment env) {
        final HtmlTableBuilder tooltip = new HtmlTableBuilder();
        final MessageProvider mp = env.getMessageProvider();
        
        tooltip.putTwoCellHeader( mp.translate("EditMask", "Expression"), mp.translate("EditMask", "Output") );
        tooltip.putTwoCellRow("d", mp.translate("EditMask", "Days"));
        tooltip.putTwoCellRow("h", mp.translate("EditMask", "Hours") );
        tooltip.putTwoCellRow("m", mp.translate("EditMask", "Minutes") );
        tooltip.putTwoCellRow("s", mp.translate("EditMask", "Seconds") );
        tooltip.putTwoCellRow("z", mp.translate("EditMask", "Milliseconds") );
        
        maskInput.setToolTip(tooltip.toString());
    }
}