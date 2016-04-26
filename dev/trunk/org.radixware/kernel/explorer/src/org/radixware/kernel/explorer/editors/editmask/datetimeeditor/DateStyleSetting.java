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

package org.radixware.kernel.explorer.editors.editmask.datetimeeditor;

import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EEditMaskOption;
import org.radixware.kernel.common.enums.EDateTimeStyle;
import org.radixware.schemas.editmask.EditMask;


final class DateStyleSetting extends AbstractDateTimeStyleSetting {
        
    public DateStyleSetting(final IClientEnvironment env, final QWidget parent) {
        super(env, parent);
        label.setText(env.getMessageProvider().translate("EditMask", "Date style:"));
    }
    
    @Override
    public void addToXml(EditMask editMask) {
        final org.radixware.schemas.editmask.EditMaskDateTime emdt = editMask.getDateTime();
        emdt.setDateStyle((EDateTimeStyle)getValue());
    }

    @Override
    public void loadFromXml(EditMask editMask) {
        final org.radixware.schemas.editmask.EditMaskDateTime emdt = editMask.getDateTime();
        final EDateTimeStyle style = emdt.getDateStyle();
        setValue(style);
    }

    @Override
    public EEditMaskOption getOption() {
        return EEditMaskOption.DATETIME_DATESTYLE;
    }
}
