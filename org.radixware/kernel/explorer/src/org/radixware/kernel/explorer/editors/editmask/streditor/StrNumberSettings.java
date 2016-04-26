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
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.math.BigDecimal;
import java.util.Collection;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EEditMaskOption;
import org.radixware.kernel.explorer.editors.editmask.IEditMaskEditorSetting;
import org.radixware.schemas.editmask.EditMask;


final class StrNumberSettings extends AbstractOptionsContainer {
    private final QVBoxLayout layout = new QVBoxLayout();
        
    public StrNumberSettings(final IClientEnvironment environment, final QWidget parent) {
        super(parent);
        layout.setAlignment(new Alignment(AlignmentFlag.AlignTop, AlignmentFlag.AlignLeft));
        
        final StrNumberMinimumValueSetting minValue = new StrNumberMinimumValueSetting(environment, this);
        registerEditorSetting(minValue, minValue.getOption());
        final StrNumberMaximumValueSetting maxValue = new StrNumberMaximumValueSetting(environment, this);
        registerEditorSetting(maxValue, maxValue.getOption());
        final StrNumberPrecisionSetting precision = new StrNumberPrecisionSetting(environment, this);
        registerEditorSetting(precision, precision.getOption());
        
        minValue.setLabelWidth(maxValue.getLabelWidth());
        precision.setLabelWidth(maxValue.getLabelWidth());
        layout.addWidget(minValue);
        layout.addWidget(maxValue);
        layout.addWidget(precision);
        
        
        this.setLayout(layout);
        this.setFrameShape(Shape.StyledPanel);
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
        editMaskStr.setValidatorType(StrValueControlWidget.NUM);
    }

    @Override
    public void loadFromXml(final EditMask editMask) {
        final org.radixware.schemas.editmask.EditMaskStr editMaskStr = 
              editMask.getStr();
        
        if(editMaskStr.isSetValidatorType() &&
                editMaskStr.getValidatorType() == StrValueControlWidget.NUM) {
            final Collection<IEditMaskEditorSetting> values = settings();
            for(IEditMaskEditorSetting setting : values) {
                setting.loadFromXml(editMask);
            }
        }
    }
    
    @Override
    public boolean checkOptions() {
        final BigDecimal max = (BigDecimal) getSetting(EEditMaskOption.STR_VC_NUMBER_MAXIMUM).getValue();
        final BigDecimal min = (BigDecimal) getSetting(EEditMaskOption.STR_VC_NUMBER_MINIMUM).getValue();
        return max.compareTo(min) >= 0;
    }
}
