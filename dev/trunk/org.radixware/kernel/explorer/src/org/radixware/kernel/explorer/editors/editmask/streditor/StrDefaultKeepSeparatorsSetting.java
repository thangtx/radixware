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
import org.radixware.kernel.explorer.editors.editmask.controls.AbstractCheckableSetting;
import org.radixware.schemas.editmask.EditMask;
import org.radixware.schemas.editmask.EditMaskStr;


final class StrDefaultKeepSeparatorsSetting extends AbstractCheckableSetting {
    
    public StrDefaultKeepSeparatorsSetting(final IClientEnvironment environment, final QWidget parent) {
        super(parent);
        final String title = environment.getMessageProvider().translate("EditMask", "Keep separators");
        setText(title);
        setDefaultValue();
    }

    @Override
    public void addToXml(final EditMask editMask) {
        editMask.getStr().setKeepSeparators(this.isChecked());
    }

    @Override
    public void loadFromXml(final EditMask editMask) {
        final EditMaskStr editMaskStr = editMask.getStr();
        if(editMaskStr.isSetKeepSeparators()) {
            this.setChecked(editMaskStr.getKeepSeparators());
        } else {
            setDefaultValue();
        }
    }

    @Override
    public void setDefaultValue() {
        this.setChecked(false);
    }

    @Override
    public EEditMaskOption getOption() {
        return EEditMaskOption.STR_VC_DEFAULT_KEEPSEPARATORS;
    }

}
