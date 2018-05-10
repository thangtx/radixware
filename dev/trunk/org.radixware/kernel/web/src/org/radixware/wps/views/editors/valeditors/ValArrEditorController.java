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
import org.radixware.kernel.common.client.meta.mask.EditMaskFilePath;
import org.radixware.kernel.common.client.meta.mask.EditMaskNone;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.views.IArrayEditorDialog;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.IButton.ClickHandler;
import org.radixware.kernel.common.client.widgets.arreditor.AbstractArrayEditorDelegate;
import org.radixware.kernel.common.enums.EFileSelectionMode;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Arr;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.dialogs.ArrayEditorDialog;
import org.radixware.wps.rwt.Dialog;
import org.radixware.wps.rwt.InputBox.ValueController;
import org.radixware.wps.rwt.ToolButton;
import org.radixware.wps.rwt.UIObject;


public class ValArrEditorController<T extends Arr> extends InputBoxController<T,EditMaskNone>{
    
    private final EValType arrType;
    private final Class<?> valClass;
    private final ToolButton editButton;
    private AbstractArrayEditorDelegate<ValEditorController,UIObject> delegate;
    private String dialogTitle;
    private EditMask mask;
    private Boolean isArrayItemMandatory;
    private boolean isDuplicatesEnabled = true;
    private boolean isItemsMovable = true;    
    private int firstArrayItemIndex = 1;
    private int minArrayItemsCount = -1;
    private int maxArrayItemsCount = -1;
    
    public ValArrEditorController(final IClientEnvironment env, final EValType valType, final Class<?> valClass, final LabelFactory factory){
        super(env,factory);
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
        editButton.setObjectName("tbEdit");
        addButton(editButton);        
    }
    
    public ValArrEditorController(final IClientEnvironment env, final EValType valType, final Class<?> valClass){
        this(env, valType, valClass, null);
    }
    
    public final void setDuplicatesEnabled(final boolean isEnabled){
        isDuplicatesEnabled = isEnabled;
    }
    
    public boolean isDuplicatesEnabled(){
        return isDuplicatesEnabled;
    }
    
    public final void setArrayItemMandatory(final boolean isMandatory){
        isArrayItemMandatory = isMandatory;
    }
    
    public boolean isArrayItemMandatory(){
        return isArrayItemMandatory==null ? isMandatory() : isArrayItemMandatory;
    }
    
    public final void setFirstArrayItemInex(final int index){
        firstArrayItemIndex = index;
    }
    
    public int getFirstArrayItemIndex(){
        return firstArrayItemIndex;
    }
    
    public final void setMinArrayItemsCount(final int count){
        minArrayItemsCount = count;
    }
    
    public final int getMinArrayItemsCount(){
        return minArrayItemsCount;
    }
    
    public final void setMaxArrayItemsCount(final int count){
        maxArrayItemsCount = count;
    }
    
    public final int getMaxArrayItemsCount(){
        return maxArrayItemsCount;
    }
    
    public final void setArrayItemsMovable(final boolean isMovable){
        isItemsMovable = isMovable;
    }
    
    public boolean isArrayItemsMovable(){
        return isItemsMovable;
    }    
    
    public AbstractArrayEditorDelegate<ValEditorController,UIObject> getArrayEditorDelegate(){
        return delegate;
    }
    
    public void setEditorDelegate(final AbstractArrayEditorDelegate<ValEditorController,UIObject> delegate){
        this.delegate = delegate;
    }    
        
    protected IArrayEditorDialog createEditorDialog(){
        final WpsEnvironment wpsEnv = (WpsEnvironment) getEnvironment();
        final ArrayEditorDialog result = 
            new ArrayEditorDialog(wpsEnv, arrType, valClass, isReadOnly(), wpsEnv.getDialogDisplayer());
        if (mask != null) {
            result.setEditMask(mask);
        }
        if (delegate!=null){
            result.setEditorDelegate(delegate);
        }
        final String dlgTitle = getDialogTitle();
        if (dlgTitle!=null && !dlgTitle.isEmpty()){
            result.setWindowTitle(dlgTitle);
        }
        result.setDuplicatesEnabled(isDuplicatesEnabled());
        result.setMandatory(isMandatory());
        result.setItemMandatory(isArrayItemMandatory());
        result.setFirstArrayItemIndex(getFirstArrayItemIndex());
        result.setMinArrayItemsCount(minArrayItemsCount);
        result.setMaxArrayItemsCount(maxArrayItemsCount);        
        result.setItemsMovable(isArrayItemsMovable());        
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
        this.mask = mask==null ? null : EditMask.newCopy(mask);
    }
    
    public EditMask getItemEditMask(){
        return mask==null ? null : EditMask.newCopy(mask);
    }

    @Override
    protected void afterChangeReadOnly() {
        super.afterChangeReadOnly();
        updateEditButton();
    }        
    
    private void updateEditButton(){
        final boolean canEditArrayItem;
        if (isReadOnly()){
            canEditArrayItem = false;
        }else if (mask instanceof EditMaskFilePath){
            final EditMaskFilePath editMaskFilePath = (EditMaskFilePath)mask;
            canEditArrayItem = editMaskFilePath.getSelectionMode()!=EFileSelectionMode.SELECT_DIRECTORY 
                                           || editMaskFilePath.getHandleInputAvailable();            
        }else{
            canEditArrayItem = true;
        }
        final Icon icon;
        final String toolTip;
        if (canEditArrayItem){
            icon = getEnvironment().getApplication().getImageManager().getIcon(ClientIcon.CommonOperations.EDIT);
            toolTip = getEnvironment().getMessageProvider().translate("PropArrEditor", "Edit Array");            
        }
        else{
            icon = getEnvironment().getApplication().getImageManager().getIcon(ClientIcon.CommonOperations.VIEW);
            toolTip = getEnvironment().getMessageProvider().translate("PropArrEditor", "View Array");            
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
}
