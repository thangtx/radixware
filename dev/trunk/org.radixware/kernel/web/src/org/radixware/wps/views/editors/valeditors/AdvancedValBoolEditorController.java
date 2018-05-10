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
import org.radixware.kernel.common.client.meta.mask.EditMaskBool;
import org.radixware.wps.rwt.AdvancedTristateCheckBox;
import org.radixware.wps.rwt.InputBox;

import org.radixware.wps.rwt.InputBox.ValueController;


public class AdvancedValBoolEditorController<T> extends InputBoxController<T, EditMaskBool> {

    private AdvancedTristateCheckBox<T> tristateCheckBox;
    private String trueTitle;
    private String falseTitle;
    private boolean titleVisible;

    public AdvancedValBoolEditorController(final IClientEnvironment env) {
        this(env, new EditMaskBool(), null);
    }

    public AdvancedValBoolEditorController(final IClientEnvironment env, final EditMaskBool editMask) {
        this(env, editMask, null);
    }
    
    public AdvancedValBoolEditorController(final IClientEnvironment env, final EditMaskBool editMask, final LabelFactory factory) {
        super(env, factory);
        setEditMask(editMask==null ? new EditMaskBool() : editMask);
    }    

    @Override
    protected IValEditor<T, EditMaskBool> createValEditor() {
        tristateCheckBox = new AdvancedTristateCheckBox<>(getEnvironment(), this);
        tristateCheckBox.addValueChangeListener(new InputBox.ValueChangeListener<T>(){
            @Override
            public void onValueChanged(T oldValue, T newValue) {
                AdvancedValBoolEditorController.this.valueEdited(newValue);
            }
        });
        return tristateCheckBox;
    }

    @Override
    public void addValueChangeListener(ValueChangeListener<T> listener) {
        tristateCheckBox.addValueChangeListener(listener); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeValueChangeListener(ValueChangeListener<T> listener) {
        tristateCheckBox.removeValueChangeListener(listener); //To change body of generated methods, choose Tools | Templates.
    }
    
    

    @Override
    public final void setEditMask(final EditMaskBool editMask) {
        this.editMask = editMask;
        if (tristateCheckBox != null) {
            tristateCheckBox.setNullValueString(editMask.getNoValueStr(getEnvironment().getMessageProvider()));
        }
        setValueTitleVisible(editMask.getValueTitleVisible());
        final String truetitle = editMask.getTrueTitle(getEnvironment().getDefManager());
        setTrueTitle(truetitle);
        final String falsetitle = editMask.getFalseTitle(getEnvironment().getDefManager());
        setFalseTitle(falsetitle);
    }

    @Override
    public void setMandatory(final boolean mandatory) {
        tristateCheckBox.setCanBeNull(!mandatory);
    }       

    @Override
    protected ValueController<T> createValueController() {
        return null;
    }

    @Override
    public String getLabel() {
        return tristateCheckBox.getLabel();
    }

    @Override
    public boolean getLabelVisible() {
        return isLabelVisible();
    }

    @Override
    public boolean isLabelVisible() {
        return tristateCheckBox.isLabelVisible();
    }

    @Override
    public void setLabel(String label) {
        tristateCheckBox.setLabel(label);
    }

    @Override
    public void setLabelVisible(boolean visible) {
        tristateCheckBox.setLabelVisible(visible);
    }

    public void setTrueTitle(String title) {
        this.trueTitle = title;
        tristateCheckBox.setTrueTitle(title);
    }

    public void setFalseTitle(String title) {
        this.falseTitle = title;
        tristateCheckBox.setFalseTitle(title);
    }

    public String getFalseTitle() {
        return falseTitle;
    }

    public String getTrueTitle() {
        return trueTitle;
    }

    public void setValueTitleVisible(final boolean visible) {
        this.titleVisible = visible;
        tristateCheckBox.setValueTitleVisible(visible);
    }

    public boolean getValueTitleVisible() {
        return this.titleVisible;
    }  
}
