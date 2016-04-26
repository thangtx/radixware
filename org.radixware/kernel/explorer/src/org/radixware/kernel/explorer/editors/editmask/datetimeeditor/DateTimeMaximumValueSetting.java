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

import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QLayout;
import com.trolltech.qt.gui.QWidget;
import java.sql.Timestamp;
import java.util.Calendar;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EEditMaskOption;
import org.radixware.kernel.explorer.editors.editmask.IEditMaskEditorSetting;
import org.radixware.kernel.explorer.editors.editmask.controls.AbstractCheckableEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValDateTimeEditor;
import org.radixware.schemas.editmask.EditMask;


final class DateTimeMaximumValueSetting extends AbstractCheckableEditor<Timestamp> implements IEditMaskEditorSetting  {
   
    public DateTimeMaximumValueSetting(final IClientEnvironment environment, final QWidget parent) {
        super(environment, parent);
        setUpUi(environment);
    }
    
    private void setUpUi(final IClientEnvironment environment) {
        final String title = environment.getMessageProvider().translate("EditMask", "Maximum value:");
        setText(title);
        stateChanged.connect(this, "onStateChanged()");
        setDefaultValue();
        enable(false);
    }
    @Override
    public void addToXml(final EditMask editMask) {
        if(isChecked()) {
            final Calendar value = Calendar.getInstance();
            value.setTimeInMillis(getValue().getTime());
            editMask.getDateTime().setMaxValue(value);
        }
    }

    @Override
    public void loadFromXml(final EditMask editMask) {
        final org.radixware.schemas.editmask.EditMaskDateTime editMaskDateTime = editMask.getDateTime();
        if(editMaskDateTime.isSetMaxValue()) {
            final Calendar value = editMaskDateTime.getMaxValue();
            setValue(new Timestamp(value.getTime().getTime()));
            enable(true);
        } else {
            setDefaultValue();
            enable(false);
        }
    }

    @Override
    public EEditMaskOption getOption() {
        return EEditMaskOption.DATETIME_MAX;
    }

    @Override
    public void setDefaultValue() {
        setValue(new Timestamp(
                Calendar
                .getInstance()
                .getTime()
                .getTime())
                );
    }

    public void onStateChanged() {
        enable(isChecked());
        if(!isChecked()) {
            setDefaultValue();
        }
    }

    @Override
    protected ValDateTimeEditor initValueEditor(final IClientEnvironment environment) {
        return new ValDateTimeEditor(environment, this);
    }
}
