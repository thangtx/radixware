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

import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EEditMaskOption;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskTimeInterval.Scale;
import org.radixware.kernel.explorer.editors.editmask.IEditMaskEditorSetting;
import org.radixware.kernel.explorer.editors.editmask.controls.AbstractComplexCheckableSetting;
import org.radixware.kernel.explorer.editors.valeditors.ValIntEditor;


final class TimeIntervalMaximumValueSetting extends AbstractComplexCheckableSetting implements IEditMaskEditorSetting {
    private final ValIntEditor valueEditor;
    private final IClientEnvironment environment;
    
    TimeIntervalMaximumValueSetting(final IClientEnvironment environment, final QWidget parent) {
        super(parent);
        this.environment = environment;
        
        final String text = environment.getMessageProvider().translate("EditMask", "Maximum value:");
        setText(text);
        valueEditor = new ValIntEditor(environment, this);
        addWidget(valueEditor, getOption());
        setDefaultValue();
        setFocusProxy(valueEditor);
        enable(false);
    }

    @Override
    protected void onCheckBoxStateChanged() {
        final boolean checked = isChecked();
        enable(checked);
        if(!checked) {
            setDefaultValue();
        }
    }

    @Override
    public void addToXml(final org.radixware.schemas.editmask.EditMask editMask) {
        if(isChecked()) {
            editMask.getTimeInterval().setMaxValue(valueEditor.getValue());
        }
    }

    @Override
    public void loadFromXml(final org.radixware.schemas.editmask.EditMask editMask) {
        final org.radixware.schemas.editmask.EditMaskTimeInterval emti = editMask.getTimeInterval();
        if(emti.isSetMaxValue()) {
            valueEditor.setValue(emti.getMaxValue());
            enable(true);
        } else {
            setDefaultValue();
            enable(false);
        }
    }

    @Override
    public EEditMaskOption getOption() {
        return EEditMaskOption.TIMEINTERVAL_MAXIMUM;
    }

    @Override
    public void setDefaultValue() {
        valueEditor.setValue(Long.MAX_VALUE);
    }

    @Override
    public Long getValue() {
        return valueEditor.getValue();
    }
    
    public void updateLabelText(final Scale scale) {
        final MessageProvider msgProvider = environment.getMessageProvider();
        String labelText = "";
        
        switch(scale) {
            case HOUR: labelText = msgProvider.translate("EditMask", "Maximum value in hours:");
                break;
            case MILLIS: labelText = msgProvider.translate("EditMask", "Maximum value in milliseconds:");
                break;
            case MINUTE: labelText = msgProvider.translate("EditMask", "Maximum value in minutes:");
                break;
            case NONE: labelText = msgProvider.translate("EditMask", "Maximum value:");
                break;
            case SECOND: labelText = msgProvider.translate("EditMask", "Maximum value in seconds:");
                break;
        }
        setText(labelText);
    }
    
}
