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
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QWidget;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EEditMaskOption;
import org.radixware.kernel.explorer.editors.editmask.IEditMaskEditorSetting;
import org.radixware.schemas.editmask.EditMask;


final class StrRegexpSettings extends AbstractOptionsContainer {
    
    private final QGridLayout layout = new QGridLayout();
    private final QLabel label = new QLabel(this);
    
    public StrRegexpSettings(final IClientEnvironment environment, final QWidget parent) {
        super(parent);
        layout.setAlignment(new Alignment(AlignmentFlag.AlignTop));
        final String labelText = environment.getMessageProvider().translate("EditMask", "Pattern:");
        label.setText(labelText);
        
        final StrRegexpPatternSetting regexpPattern = new StrRegexpPatternSetting(environment, this);
        final StrRegexpMatchCaseSetting matchCase = new StrRegexpMatchCaseSetting(environment, this);
        registerEditorSetting(regexpPattern, regexpPattern.getOption());
        registerEditorSetting(matchCase, matchCase.getOption());
        
        layout.addWidget(label, 0, 0);
        layout.addWidget(regexpPattern, 0, 1);
        layout.addWidget(matchCase, 1, 1);
                
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
        
        editMaskStr.setValidatorType(StrValueControlWidget.REGEXP);
    }

    @Override
    public void loadFromXml(final EditMask editMask) {
        final org.radixware.schemas.editmask.EditMaskStr editMaskStr = 
                editMask.getStr();
        if(editMaskStr.isSetValidatorType() &&
            editMaskStr.getValidatorType() == StrValueControlWidget.REGEXP) { 
                final Collection<IEditMaskEditorSetting> values = settings();
                for(IEditMaskEditorSetting setting : values) {
                    setting.loadFromXml(editMask);
                }
        }
    }

    @Override
    public void setHiddenOptions(final Set<EEditMaskOption> options) {
        if(options.contains(EEditMaskOption.STR_VC_REGEXP_PATTERN)) {
            super.setHiddenOptions(EnumSet.of(EEditMaskOption.STR_VC_REGEXP_PATTERN,
                    EEditMaskOption.STR_VC_REGEXP_MATCHCASE));
        } else {
            super.setHiddenOptions(options);
        }
    }

    @Override
    public void setVisibleOptions(final Set<EEditMaskOption> options) {
        if(options.contains(EEditMaskOption.STR_VC_REGEXP_MATCHCASE)) {
            super.setVisibleOptions(EnumSet.of(EEditMaskOption.STR_VC_REGEXP_PATTERN,
                    EEditMaskOption.STR_VC_REGEXP_MATCHCASE));
        } else {
            super.setVisibleOptions(options);
        }
    }
    
    @Override
    public boolean checkOptions() {
        final String regExpValue = (String) getSetting(EEditMaskOption.STR_VC_REGEXP_PATTERN).getValue();
        if(regExpValue == null || regExpValue.isEmpty()) {
            return false;
        }
        return true;
    }
}
