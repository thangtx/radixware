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
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskNone;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.views.IArrayEditorDialog;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.IButton.ClickHandler;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Arr;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.dialogs.ArrayEditorDialog;
import org.radixware.wps.rwt.Dialog;
import org.radixware.wps.rwt.InputBox.ValueController;
import org.radixware.wps.rwt.ToolButton;


public class ValArrEditorController<T extends Arr> extends InputBoxController<T,EditMaskNone>{
    
    private final EValType arrType;
    private final Class<?> valClass;
    private final ToolButton editButton;
    private String dialogTitle;
    private EditMask mask;
    
    public ValArrEditorController(final IClientEnvironment env, final EValType valType, final Class<?> valClass){
        super(env);
        arrType = valType.isArrayType() ? valType : valType.getArrayType();
        this.valClass = valClass;
        setEditMask(new EditMaskNone());
        editButton = new ToolButton();
        editButton.setIcon(env.getApplication().getImageManager().getIcon(ClientIcon.CommonOperations.EDIT));    
        editButton.setToolTip(getEnvironment().getMessageProvider().translate("PropArrEditor", "Edit Array"));
        editButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(IButton source) {
                edit();
            }
        });
        addButton(editButton);
    }
    
    protected IArrayEditorDialog createEditorDialog(){
        final WpsEnvironment wpsEnv = (WpsEnvironment) getEnvironment();
        final ArrayEditorDialog result = 
            new ArrayEditorDialog(wpsEnv, arrType, valClass, isReadOnly(), wpsEnv.getDialogDisplayer());
        if (mask != null) {
            result.setEditMask(mask);
        }
        final String dlgTitle = getDialogTitle();
        if (dlgTitle!=null && !dlgTitle.isEmpty()){
            result.setWindowTitle(dlgTitle);
        }
        return result;
    }
    
    @SuppressWarnings("unchecked")
    public void edit() {        
        final IArrayEditorDialog dialog = createEditorDialog();        
        dialog.setCurrentValue(getValue());        
        if (dialog.execDialog() == Dialog.DialogResult.ACCEPTED) {
            setValue((T)dialog.getCurrentValue());
        }
    }
    
    
    
    public void setItemEditMask(final EditMask mask){
        this.mask = mask;
    }

    @Override
    protected void afterChangeReadOnly() {
        super.afterChangeReadOnly();
        final Icon icon;
        final String toolTip;
        if (isReadOnly()){
            icon = getEnvironment().getApplication().getImageManager().getIcon(ClientIcon.CommonOperations.VIEW);
            toolTip = getEnvironment().getMessageProvider().translate("PropArrEditor", "View Array");
        }
        else{
            icon = getEnvironment().getApplication().getImageManager().getIcon(ClientIcon.CommonOperations.EDIT);
            toolTip = getEnvironment().getMessageProvider().translate("PropArrEditor", "Edit Array");
        }
        editButton.setIcon(icon);
        editButton.setToolTip(toolTip);        
    }        
    
    @Override
    protected ValueController<T> createValueController() {
        return null;//no keyboard input
    }
    
    public final void setEditButtonVisible(final boolean isVisible) {
        editButton.setVisible(isVisible);
    }    
    
    
    public final boolean isEditButtonVisible(){
        return editButton.isVisible();
    }

    public String getDialogTitle() {
        return dialogTitle;
    }

    public void setDialogTitle(final String dialogTitle) {
        this.dialogTitle = dialogTitle;
    }        
}
