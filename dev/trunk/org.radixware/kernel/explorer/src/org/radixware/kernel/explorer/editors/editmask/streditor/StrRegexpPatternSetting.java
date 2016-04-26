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

import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QLayout;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EEditMaskOption;
import org.radixware.kernel.explorer.editors.editmask.IEditMaskEditorSetting;
import org.radixware.kernel.explorer.editors.valeditors.ValStrEditor;


final class StrRegexpPatternSetting extends QWidget implements IEditMaskEditorSetting {
    private final ValStrEditor valueEditor;
        
    public StrRegexpPatternSetting(final IClientEnvironment environment, final QWidget parent) {
        super(parent);
        valueEditor = new ValStrEditor(environment, parent);
        final QLayout layout = new QHBoxLayout();
        layout.setMargin(0);
        layout.addWidget(valueEditor);
        setFocusProxy(valueEditor);
        this.setLayout(layout);
        setDefaultValue();
    } 
    @Override
    public void addToXml(final org.radixware.schemas.editmask.EditMask editMask) {
        if(!this.getValue().isEmpty()) {
            final org.radixware.schemas.editmask.EditMaskStr editMaskStr = 
                editMask.getStr();
            editMaskStr.setMask(this.getValue());
        }
    }

    @Override
    public void loadFromXml(final org.radixware.schemas.editmask.EditMask editMask) {
        final org.radixware.schemas.editmask.EditMaskStr editMaskStr = 
                editMask.getStr();
        
        if(editMaskStr.isSetMask()) {
            this.setValue(editMaskStr.getMask());
        } else {
            setDefaultValue();
        }
        
    }

    @Override
    public EEditMaskOption getOption() {
        return EEditMaskOption.STR_VC_REGEXP_PATTERN;
    }

    @Override
    public void setDefaultValue() {
        this.setValue("*");
    }

    public void setValue(final String string) {
        valueEditor.setValue(string);
    }

    @Override
    public String getValue() {
        return valueEditor.getValue();
    }

}
