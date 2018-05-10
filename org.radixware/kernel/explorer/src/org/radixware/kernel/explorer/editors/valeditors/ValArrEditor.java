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

package org.radixware.kernel.explorer.editors.valeditors;

import com.trolltech.qt.gui.QAbstractButton;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QWidget;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskNone;
import org.radixware.kernel.common.client.widgets.arreditor.AbstractArrayEditorDelegate;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Arr;
import org.radixware.kernel.explorer.dialogs.ArrayEditorDialog;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;

public class ValArrEditor extends ValEditor<Arr> {

    private final EValType arrType;
    private final Class<?> valClass;
    private final QToolButton editBtn;   
    private AbstractArrayEditorDelegate<ValEditor, QWidget> delegate;
    private Boolean isArrayItemMandatory;
    private boolean isDuplicatesEnabled = true;
    private boolean isItemsMovable = true;    
    private int firstArrayItemIndex = 1;
    private int minArrayItemsCount = -1;
    private int maxArrayItemsCount = -1;

    private List<Object> predefItemValues = null;
    
    @SuppressWarnings("LeakingThisInConstructor")
    public ValArrEditor(final IClientEnvironment environment, final EValType valType, final Class<?> valClass,
            final QWidget parent, final boolean mandatory, final boolean readonly) {
        super(environment, parent, new EditMaskNone(), mandatory, readonly);
        this.arrType = valType.isArrayType() ? valType : valType.getArrayType();
        this.valClass = valClass;
        final QAction action = new QAction(this);
        action.triggered.connect(this, "edit()");
        action.setIcon(readonly
                ? ExplorerIcon.getQIcon(ClientIcon.CommonOperations.VIEW)
                : ExplorerIcon.getQIcon(ClientIcon.CommonOperations.EDIT));
        action.setObjectName("edit");
        editBtn = addButton(null, action);
        getLineEdit().setReadOnly(true);
    }

    public ValArrEditor(final IClientEnvironment environment, final EValType valType, final Class<?> valClass,
            final QWidget parent){
        this(environment,valType,valClass,parent,true,false);
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
    
    public int getMinArrayItemsCount(){
        return minArrayItemsCount;
    }
    
    public final void setMaxArrayItemsCount(final int count){
        maxArrayItemsCount = count;
    }
    
    public int getMaxArrayItemsCount(){
        return maxArrayItemsCount;
    }    
    
    public final void setArrayItemsMovable(final boolean isMovable){
        isItemsMovable = isMovable;
    }
    
    public boolean isArrayItemsMovable(){
        return isItemsMovable;
    }    
    
    public AbstractArrayEditorDelegate<ValEditor, QWidget> getArrayEditorDelegate(){
        return delegate;
    }
    
    public void setArrayEditorDelegate(final AbstractArrayEditorDelegate<ValEditor, QWidget> delegate){
        this.delegate = delegate;
    }        

    @Override
    public void setEditMask(final EditMask editMask) {        
        super.setEditMask(editMask==null ? new EditMaskNone() : editMask);
        refresh();
    }
        
    public void edit() {
        QWidget parentWidget = (QWidget) parent();
        if (parentWidget == null) {
            parentWidget = Application.getMainWindow();
        }
        
        final ArrayEditorDialog dialog = new ArrayEditorDialog(getEnvironment(), arrType, valClass, isReadOnly(), parentWidget);
        final String dlgTitle = getDialogTitle();
        if (dlgTitle!=null && !dlgTitle.isEmpty()){
            dialog.setWindowTitle(dlgTitle);
        }
        dialog.setPredefinedValues(getPredefinedItemValues());
        final EditMask mask = getEditMask();
        if (mask.getSupportedValueTypes().contains(arrType)) {
            dialog.setEditMask(mask);
        }
        if (delegate!=null){
            dialog.setEditorDelegate(delegate);
        }
        dialog.setDuplicatesEnabled(isDuplicatesEnabled());
        dialog.setMandatory(isMandatory());
        dialog.setItemMandatory(isArrayItemMandatory());
        dialog.setFirstArrayItemIndex(getFirstArrayItemIndex());
        dialog.setMinArrayItemsCount(getMinArrayItemsCount());
        dialog.setMaxArrayItemsCount(getMaxArrayItemsCount());
        dialog.setItemsMovable(isArrayItemsMovable());
        
        dialog.setCurrentValue(getValue());

        if (QDialog.DialogCode.resolve(dialog.exec()) == QDialog.DialogCode.Accepted) {
            setValue(dialog.getCurrentValue());
            editingFinished.emit(getValue());
        }
    }

    @Override
    protected boolean isButtonCanChangeValue(final QAbstractButton button) {
        if (button==editBtn){//NOPMD
            return !isReadOnly();
        }else{
            return super.isButtonCanChangeValue(button);
        }
    }
    
    
    /**
     * Sets up a list of predefined values for an array items
     * @param values - list of values to set
     */
    public void setPredefinedItemValues(final List<Object> values) {
        predefItemValues = values;
    }
    
    /**
     * Retrieves a list of array items' predefined values
     * @return - list of predefined values
     */
    public List<Object> getPredefinedItemValues() {
        return predefItemValues;
    }
    
    public EValType getArrayType(){
        return arrType;
    }    
}
