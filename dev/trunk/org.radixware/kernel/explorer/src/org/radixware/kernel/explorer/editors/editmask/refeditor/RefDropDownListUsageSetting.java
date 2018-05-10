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

package org.radixware.kernel.explorer.editors.editmask.refeditor;

import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.enums.EEditMaskOption;
import org.radixware.kernel.explorer.editors.editmask.controls.AbstractCheckableSetting;
import org.radixware.schemas.editmask.EditMask;

public class RefDropDownListUsageSetting extends AbstractCheckableSetting{

    public RefDropDownListUsageSetting(QWidget parent) {
        super(parent);
    }

    @Override
    public EEditMaskOption getOption() {
        return EEditMaskOption.REF_DROP_DOWN;
    }

    @Override
    public void addToXml(EditMask editMask) {
        editMask.getReference().setUseDropDownList(isChecked());
    }

    @Override
    public void loadFromXml(EditMask editMask) {
        setChecked(editMask.getReference().isSetUseDropDownList());
    }

    @Override
    public void setDefaultValue() {
    }
    
}
