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
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EEditMaskOption;
import org.radixware.kernel.explorer.editors.editmask.IEditMaskEditorSetting;
import org.radixware.schemas.editmask.EditMask;


final class StrDefaultSettings extends AbstractOptionsContainer {
    private final QVBoxLayout layout = new QVBoxLayout();
        
    public StrDefaultSettings(final IClientEnvironment environment, final QWidget parent) {
        super(parent);
        
        final String title = environment.getMessageProvider().translate("EditMask", "Qt mask:");
        final StrDefaultQtMaskSetting qtMask = new StrDefaultQtMaskSetting(environment, this);
        qtMask.setLabelText(title);
        final StrDefaultBlankSeparatorsSetting blankChar = new StrDefaultBlankSeparatorsSetting(environment, this);
        final StrDefaultKeepSeparatorsSetting keepSeparators = new StrDefaultKeepSeparatorsSetting(environment, this);
        registerEditorSetting(qtMask, qtMask.getOption());
        registerEditorSetting(blankChar, blankChar.getOption());
        registerEditorSetting(keepSeparators, keepSeparators.getOption());
        
        layout.setAlignment(new Alignment(AlignmentFlag.AlignTop, AlignmentFlag.AlignLeft));
        layout.addWidget(qtMask);
        layout.addWidget(blankChar);
        layout.addWidget(keepSeparators);
                  
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
        editMaskStr.setValidatorType(StrValueControlWidget.DEFAULT);
    }

    @Override
    public void loadFromXml(final EditMask editMask) {
        final org.radixware.schemas.editmask.EditMaskStr editMaskStr = 
                editMask.getStr();
        if(editMaskStr.isSetValidatorType() &&
                editMaskStr.getValidatorType() == StrValueControlWidget.DEFAULT) {
            final Collection<IEditMaskEditorSetting> values = settings();
            for(IEditMaskEditorSetting setting : values) {
                setting.loadFromXml(editMask);
            }
        }
    }

    @Override
    public void setHiddenOptions(final Set<EEditMaskOption> options) {
        if(options.contains(EEditMaskOption.STR_VC_DEFAULT_QTMASK)) {
            super.setHiddenOptions(EnumSet.of(EEditMaskOption.STR_VC_DEFAULT_QTMASK, 
                                                EEditMaskOption.STR_VC_DEFAULT_DONOTUSEBLANKCHAR,
                                                EEditMaskOption.STR_VC_DEFAULT_KEEPSEPARATORS));
        } else {
            super.setHiddenOptions(options);
        }
    }

    @Override
    public void setVisibleOptions(final Set<EEditMaskOption> options) {
        if(options.contains(EEditMaskOption.STR_VC_DEFAULT_DONOTUSEBLANKCHAR)) {
            super.setVisibleOptions(EnumSet.of(EEditMaskOption.STR_VC_DEFAULT_QTMASK, 
                    EEditMaskOption.STR_VC_DEFAULT_DONOTUSEBLANKCHAR));
        } else if(options.contains(EEditMaskOption.STR_VC_DEFAULT_KEEPSEPARATORS)) {
            super.setVisibleOptions(EnumSet.of(EEditMaskOption.STR_VC_DEFAULT_QTMASK, 
                    EEditMaskOption.STR_VC_DEFAULT_KEEPSEPARATORS));
        } else { 
            super.setVisibleOptions(options);
        }
    }
    
    @Override
    public boolean checkOptions() {
        return true;
    }
}
