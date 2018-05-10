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

package org.radixware.kernel.explorer.dialogs;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumSet;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;
import org.radixware.kernel.common.client.meta.mask.EditMaskConstSet;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.models.items.properties.PropertyArr;
import org.radixware.kernel.common.client.views.IArrayEditorDialog;
import org.radixware.kernel.common.client.views.ArrayEditorEventListener;
import org.radixware.kernel.common.client.widgets.IPushButton;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EEditMaskEnumOrder;
import org.radixware.kernel.common.types.Arr;
import org.radixware.kernel.common.types.IKernelEnum;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.ArrayEditor;
import org.radixware.kernel.explorer.editors.EnumItemsEditor;


public final class EnumItemsEditorDialog extends ExplorerDialog implements IArrayEditorDialog {

    private final EnumItemsEditor editor;
    private final RadEnumPresentationDef enumDef;
    private final Class<?> valClass;
    private final EEditMaskEnumOrder order;
    private String noValueStr = null;    
    private Arr currentValue;
    private Object selectedValue;
    private boolean wasClosed;
    private boolean isReadonly;
        

    public EnumItemsEditorDialog(final IClientEnvironment environment, final QWidget parent,
            final Class<?> valClass, final RadEnumPresentationDef.Items enumItems,
            final EEditMaskEnumOrder enumOrder) {
        super(environment, parent, null);
        final RadEnumPresentationDef.Items availableItems = enumItems.copy();
        enumDef = availableItems.getEnumPresentationDef();        
        order = enumOrder;
        this.valClass = valClass;
        editor = new EnumItemsEditor(environment, this, availableItems);
        setupUi();
    }

    public EnumItemsEditorDialog(final IClientEnvironment environment, final QWidget parent,
            final Class<?> valClass, final EditMaskConstSet editMask) {
        this(environment, parent, valClass,
                editMask.getItems(environment.getApplication()),
                editMask.getOrder());        
    }

    public EnumItemsEditorDialog(final Property property, final QWidget parent) {
        this(property.getEnvironment(), parent, property.getValClass(), (EditMaskConstSet) property.getEditMask());
        this.noValueStr = ((EditMaskConstSet) property.getEditMask()).getNoValueStr(property.getEnvironment().getMessageProvider());
        if (property instanceof PropertyArr) {
            final PropertyArr propertyArr = (PropertyArr) property;
            editor.setNoValueStr(noValueStr);
            editor.setMaxArrayItemsCount(propertyArr.getMaxArrayItemsCount());
            editor.setMinArrayItemsCount(propertyArr.getMinArrayItemsCount());
            editor.setReadonly(propertyArr.isReadonly());
            editor.setMandatory(propertyArr.isMandatory());
        }
    }

    private void setupUi() {
        setAttribute(Qt.WidgetAttribute.WA_DeleteOnClose, true);
        editor.valueStateChanged.connect(this, "updateOkButton(Boolean)");
        if (order != null) {
            editor.setSortingOrder(order);
        }
        layout().addWidget(editor);
        final EnumSet<EDialogButtonType> buttons;
        if (isReadonly()) {
            buttons = EnumSet.of(EDialogButtonType.CLOSE);
        } else {
            buttons = EnumSet.of(EDialogButtonType.OK, EDialogButtonType.CANCEL);
        }
        addButtons(buttons, true);
    }

    public void setNoValueStr(final String noValueStr) {
        if (editor.getNoValueStr() == null ? noValueStr != null : !editor.getNoValueStr().equals(noValueStr)) {
            editor.setNoValueStr(noValueStr);
            this.noValueStr = noValueStr;
        }
    }

    public String getNoValueStr() {
        return noValueStr;
    }

    @Override
    public void setCurrentValue(final Arr arr) {
        final RadEnumPresentationDef.Items selectedItems;
        if (arr == null) {
            selectedItems = null;
        }else{
            selectedItems = enumDef.getEmptyItems();
            for (Object arrItem : arr) {
                if (arrItem instanceof IKernelEnum) {
                    selectedItems.addItem((IKernelEnum) arrItem);
                } else if (arrItem instanceof Id) {
                    selectedItems.addItemWithId((Id) arrItem);
                } else if (arrItem instanceof Comparable) {
                    selectedItems.addItemWithValue((Comparable) arrItem);
                }
            }
        }
        editor.setSelectedItems(selectedItems);
        updateOkButton(editor.isCurrentValueValid());
    }
    
    @SuppressWarnings("unused")
    private void updateOkButton(final Boolean isValid){
        final IPushButton pbOk = getButton(EDialogButtonType.OK);
        if (pbOk!=null){
            pbOk.setEnabled(isValid && (!isMandatory() || getCurrentValue()!=null));
        }
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public Arr getCurrentValue() {
        if (wasClosed){
            return currentValue;
        }else{
            if (editor==null || editor.currentValueIsNull()) {
                return null;
            }
            Arr value = null;
            if (valClass != null) {//enums            
                try {
                    value = (Arr) valClass.newInstance();
                } catch (Exception exception) {
                    getEnvironment().processException(exception);
                    return null;
                }

                for (RadEnumPresentationDef.Item item : editor.getSelectedItems()) {
                    value.add(item.getConstant());
                }
            } else {
                value = ArrayEditor.createEmptyArr(enumDef.getItemType());
                for (RadEnumPresentationDef.Item item : editor.getSelectedItems()) {
                    value.add(item.getValue());
                }
            }
            return value;
        }
    }
    
    public boolean isCurrentValueValid(){
        return editor.isCurrentValueValid();
    }

    @Override
    public boolean isReadonly() {
        return isReadonly;
    }

    @Override
    public void setReadonly(final boolean readOnly) {
        if (isReadonly!=readOnly){
            isReadonly = readOnly;
            editor.setReadonly(readOnly);
            clearButtons();
            if (isReadonly) {
                addButton(EDialogButtonType.CLOSE).setDefault(true);
            } else {
                addButton(EDialogButtonType.OK).setDefault(true);
                addButton(EDialogButtonType.CANCEL);
                updateOkButton(editor.isCurrentValueValid());
            }
        }
    }

    @Override
    public void done(final int result) {
        currentValue = getCurrentValue();
        selectedValue = getSelectedValue();
        super.done(result);                
        wasClosed = true;        
    }    

    @Override
    public Object getSelectedValue() {
        if (wasClosed){
            return selectedValue;
        }else{
            final RadEnumPresentationDef.Item item = editor.getCurrentItem();
            return item==null ? null : item.getValue();
        }
    }

    @Override
    public boolean isEmptyArray() {
        if (wasClosed){
            return currentValue==null || currentValue.isEmpty();
        }else{
            return editor.isEmpty();
        }
    }
    
    @Override
    public boolean isItemsMovable() {
        return editor.isItemsMovable();
    }    
    
    @Override
    public void setItemsMovable(final boolean isMovable) {
        editor.setItemsMovable(isMovable);
    }        

    @Override
    public boolean isMandatory() {
        return editor.isMandatory();
    }
    
    @Override
    public void setMandatory(final boolean mandatory) {
        editor.setMandatory(mandatory);
        updateOkButton(editor.isCurrentValueValid());
    }
    
    @Override
    public boolean isDuplicatesEnabled() {
        return false;
    }    

    @Override
    public void setDuplicatesEnabled(final boolean duplicates) {
        throw new UnsupportedOperationException("Not supported in EnumItemsEditorDialog");
    }
    
    public boolean isEditorReadonly(){
        return editor.isReadonly();
    }

    @Override
    public void setEditorReadonly(final boolean readonly) {
        editor.setReadonly(readonly);
    }
    
    @Override
    public boolean isItemMandatory() {
        return false;
    }    

    @Override
    public void setItemMandatory(final boolean mandatory) {
        throw new UnsupportedOperationException("Not supported in EnumItemsEditorDialog");
    }
    
    public boolean isOperationsVisible(){
        return editor.isOperationsVisible();
    }

    @Override
    public void setOperationsVisible(final boolean isVisible) {
       editor.setOperationsVisible(isVisible);
    }

    @Override
    public void setPredefinedValues(final List<Object> values) {
        throw new UnsupportedOperationException("Not supported in EnumItemsEditorDialog");
    }

    @Override
    public void addEventListener(final ArrayEditorEventListener listener) {
        editor.addEventListener(listener);
    }

    @Override
    public void removeEventListener(final ArrayEditorEventListener listener) {
        editor.removeEventListener(listener);
    }
    
    @Override
    public int getMinArrayItemsCount(){
        return editor.getMinArrayItemsCount();
    }
    
    @Override
    public void setMinArrayItemsCount(final int count){
        editor.setMinArrayItemsCount(count);
    }
    
    @Override
    public int getMaxArrayItemsCount(){
        return editor.getMaxArrayItemsCount();
    }
    
    @Override
    public void setMaxArrayItemsCount(final int count){
        editor.setMaxArrayItemsCount(count);
    }

    @Override
    public void addCustomAction(final Action action) {
        throw new UnsupportedOperationException("Not supported in EnumItemsEditorDialog");
    }

    @Override
    public void removeCustomAction(final Action action) {
        throw new UnsupportedOperationException("Not supported in EnumItemsEditorDialog");
    }        

    @Override
    public int getCurrentItemIndex() {
        throw new UnsupportedOperationException("Not supported in EnumItemsEditorDialog");
    }

    @Override
    public void setCurrentItemIndex(final int index) {
        throw new UnsupportedOperationException("Not supported in EnumItemsEditorDialog");
    }        
    
    public EEditMaskEnumOrder getSortingOrder(){
        return editor.getSortingOrder();
    }

    public void setSortingOrder(final EEditMaskEnumOrder order) {
        editor.setSortingOrder(order);
    }    

    @Override
    public void setFirstArrayItemIndex(int index) {   
        //empty implementation
    }

    @Override
    public int getFirstArrayItemIndex() {
        return -1;
    }        
    
}
