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

package org.radixware.wps.views.editors.valeditors;

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMaskNone;
import org.radixware.wps.rwt.InputBox.ValueController;
import org.radixware.wps.rwt.TristateCheckBox;


public class ValBoolEditorController extends InputBoxController<Boolean, EditMaskNone> {

    private TristateCheckBox checkBox;

    public ValBoolEditorController(final IClientEnvironment env) {
        super(env);
        setEditMask(new EditMaskNone());
    }

    @Override
    public void addValueChangeListener(ValueChangeListener<Boolean> listener) {
        checkBox.addValueChangeListener(listener);
    }

    @Override
    public void removeValueChangeListener(ValueChangeListener<Boolean> listener) {
        checkBox.removeValueChangeListener(listener);
    }

    @Override
    protected IValEditor<Boolean, EditMaskNone> createValEditor() {
        checkBox = new TristateCheckBox(getEnvironment(),this);
        return checkBox;
    }

    @Override
    public final void setEditMask(EditMaskNone editMask) {
        this.editMask = editMask;
        if (checkBox != null) {
            checkBox.setNullValueString(editMask.getNoValueStr(getEnvironment().getMessageProvider()));
        }
    }

    @Override
    public void setMandatory(final boolean mandatory) {
        checkBox.setCanBeNull(!mandatory);
    }

    @Override
    public Boolean getValue() {
        return checkBox.getValue();
    }

    @Override
    protected ValueController<Boolean> createValueController() {
        return null;
    }

    @Override
    public String getLabel() {
        return checkBox.getLabel();
    }

    @Override
    public boolean getLabelVisible() {
        return isLabelVisible();
    }

    @Override
    public boolean isLabelVisible() {
        return checkBox.isLabelVisible();
    }

    @Override
    public void setLabel(String label) {
        checkBox.setLabel(label);
    }

    @Override
    public void setLabelVisible(boolean visible) {
        checkBox.setLabelVisible(visible);
    }
}
