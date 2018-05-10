/*
* Copyright (coffee) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.explorer.editors.editmask.booleditor;

import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EEditMaskOption;
import org.radixware.kernel.common.client.meta.mask.EditMaskBool;
import org.radixware.kernel.explorer.editors.editmask.IEditMaskEditorSetting;
import org.radixware.kernel.explorer.editors.editmask.controls.AbstractCheckableSetting;
import org.radixware.schemas.editmask.EditMask;


final class BooleanTitleValuesVisibleSetting extends AbstractCheckableSetting implements IEditMaskEditorSetting{

    public BooleanTitleValuesVisibleSetting(final IClientEnvironment environment, final QWidget parent) {
        super(parent);
        final String title = environment.getMessageProvider().translate("EditMask", "Value titles visible");
        this.setText(title);
        this.setChecked(false);
    }
    
    @Override
    public EEditMaskOption getOption() {
        return EEditMaskOption.BOOLEAN_TITLE_VALUES_VISIBLE;
    }

    @Override
    public void addToXml(EditMask editMask) {
        editMask.getBoolean().setValueTitleVisible(this.isChecked());
    }

    @Override
    public void loadFromXml(EditMask editMask) {
        final org.radixware.schemas.editmask.EditMaskBool ems = editMask.getBoolean();
        if (ems.isSetValueTitleVisible()) {
            this.setChecked(ems.getValueTitleVisible());
        } else {
            setDefaultValue();
        }
    }

    @Override
    public void setDefaultValue() {
        final EditMaskBool editMask = new EditMaskBool();
        final boolean value = editMask.getValueTitleVisible();
        this.setChecked(value);
    }

}