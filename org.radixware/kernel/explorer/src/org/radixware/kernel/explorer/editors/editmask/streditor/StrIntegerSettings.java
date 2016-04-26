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

package org.radixware.kernel.explorer.editors.editmask.streditor;

import com.trolltech.qt.core.Qt.Alignment;
import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.Collection;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EEditMaskOption;
import org.radixware.kernel.explorer.editors.editmask.IEditMaskEditorSetting;
import org.radixware.schemas.editmask.EditMask;


final class StrIntegerSettings extends AbstractOptionsContainer {
       
    public StrIntegerSettings(final IClientEnvironment environment, final QWidget parent) {
        super(parent);
                
        final QVBoxLayout layout = new QVBoxLayout();
        layout.setAlignment(new Alignment(AlignmentFlag.AlignTop));
        
        final StrIntegerMinimumValueSetting minValue = 
                new StrIntegerMinimumValueSetting(environment, this);
        registerEditorSetting(minValue, minValue.getOption());
        final StrIntegerMaximumValueSetting maxValue = 
                new StrIntegerMaximumValueSetting(environment, this);
        registerEditorSetting(maxValue, maxValue.getOption());
        
        minValue.setLabelWidth(maxValue.getLabelWidth());
        layout.addWidget(minValue);
        layout.addWidget(maxValue);
        
        this.setFrameShape(Shape.StyledPanel);
        this.setLayout(layout);
        this.adjustSize();
    }
    
    @Override
    public void addToXml(final EditMask editMask) {
        final org.radixware.schemas.editmask.EditMaskStr editMaskStr = 
                editMask.getStr();
        final Collection<IEditMaskEditorSetting> values = settings();
        for(IEditMaskEditorSetting setting : values) {
            setting.addToXml(editMask);
        }
        editMaskStr.setValidatorType(StrValueControlWidget.INT);
    }

    @Override
    public void loadFromXml(final EditMask editMask) {
        final org.radixware.schemas.editmask.EditMaskStr editMaskStr = 
                editMask.getStr();
        if(editMaskStr.isSetValidatorType() && 
                editMaskStr.getValidatorType() == StrValueControlWidget.INT) {
            final Collection<IEditMaskEditorSetting> values = settings();
            for(IEditMaskEditorSetting setting : values) {
                setting.loadFromXml(editMask);
            }
        }
    }
    
    @Override
    public boolean checkOptions() {
        final Long min = (Long) getSetting(EEditMaskOption.STR_VC_INTEGER_MINIMUM).getValue();
        final Long max = (Long)getSetting(EEditMaskOption.STR_VC_INTEGER_MAXIMUM).getValue();
        return max.compareTo(min) >= 0;
    }
    
    public void setIsChar(final boolean isChar) {
        ((StrIntegerMinimumValueSetting)getSetting(EEditMaskOption.STR_VC_INTEGER_MINIMUM)).setIsChar(isChar);
        ((StrIntegerMaximumValueSetting)getSetting(EEditMaskOption.STR_VC_INTEGER_MAXIMUM)).setIsChar(isChar);
    }
}
