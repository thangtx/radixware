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

import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EEditMaskOption;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.explorer.editors.editmask.IEditMaskEditorSetting;
import org.radixware.kernel.explorer.editors.editmask.controls.AbstractCheckableSetting;



final class StrAllowEmptyStringSetting extends AbstractCheckableSetting implements IEditMaskEditorSetting {
       
    public StrAllowEmptyStringSetting(final IClientEnvironment environment, final QWidget parent) {
        super(parent);
        final String title = environment.getMessageProvider().translate("EditMask", "Allow empty string");
        this.setText(title);
        setDefaultValue();
    }
    
    @Override
    public void addToXml(final org.radixware.schemas.editmask.EditMask editMask) {
        editMask.getStr().setAllowEmptyString(this.isChecked());
    }

    @Override
    public void loadFromXml(final org.radixware.schemas.editmask.EditMask editMask) {
        final org.radixware.schemas.editmask.EditMaskStr ems = editMask.getStr();
        if(ems.isSetAllowEmptyString()) {
            this.setChecked(ems.getAllowEmptyString());
        } else {
            setDefaultValue();
        }
    }

    @Override
    public void setDefaultValue() {
        final EditMaskStr em = new EditMaskStr();
        final boolean value = em.isEmptyStringAllowed();
        this.setChecked(value);
    }

    @Override
    public EEditMaskOption getOption() {
        return EEditMaskOption.STR_ALLOW_EMPTY;
    }

}
    
