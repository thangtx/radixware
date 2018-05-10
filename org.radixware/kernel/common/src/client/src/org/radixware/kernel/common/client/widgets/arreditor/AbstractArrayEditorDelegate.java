/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.common.client.widgets.arreditor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import org.radixware.kernel.common.client.IClientApplication;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.dialogs.IListDialog;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;
import org.radixware.kernel.common.client.meta.mask.*;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.views.IDialog;
import org.radixware.kernel.common.client.widgets.IListWidget;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.client.widgets.ListWidgetItem;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EEditMaskEnumOrder;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.types.Bin;


public abstract class AbstractArrayEditorDelegate<T,W> {

    public abstract T createEditor(final W parent,
                                                 final IClientEnvironment environment,
                                                 final ArrayItemEditingOptions options,
                                                 final int index,
                                                 final List<Object> currentValues);
    
    public abstract void setValueToEditor(T editor, Object value);
    
    public abstract Object getValueFromEditor(T editor);
    
    public String getDisplayTextForValue(final IClientEnvironment environment,
                                                            final ArrayItemEditingOptions options,
                                                            final Object value){
        if (value==null){
            return options.getEditMask().getArrayItemNoValueStr(environment.getMessageProvider());
        }else{                        
            final boolean isAdvancedValBoolEditor = options.getEditMask() instanceof EditMaskBool                 
                && EnumSet.of(EValType.BOOL, EValType.INT, EValType.NUM, EValType.STR).contains(options.getValType());
            if (isAdvancedValBoolEditor){
                final EditMaskBool editMask = (EditMaskBool)options.getEditMask();
                final String valueTitle;
                if (value.equals(editMask.getTrueValue())){
                    valueTitle = editMask.getTrueTitle(environment.getDefManager());
                }else if (value.equals(editMask.getFalseValue())){
                    valueTitle = editMask.getFalseTitle(environment.getDefManager());
                }else{
                    valueTitle = null;
                }
                if (valueTitle!=null && !valueTitle.isEmpty()){
                    return valueTitle;
                }
            }
            return options.getEditMask().toStr(environment, value);            
        }
    }
    
    public List<Object> createNewValues(final IWidget arrayEditor,
                                                             final IClientEnvironment environment,
                                                             final ArrayItemEditingOptions options,
                                                             final List<Object> currentValues) {
        final List<Object> newValues = new ArrayList<>(1);
        final EditMask itemEditMask = options.getEditMask();
        if (itemEditMask instanceof EditMaskList
                     || itemEditMask instanceof EditMaskConstSet){
            final List<ListWidgetItem> listItems;
            boolean canSort;
            if (itemEditMask instanceof EditMaskList){
                listItems = ListWidgetItem.createForEditMaskList(environment, (EditMaskList)itemEditMask);
                if (!options.isDuplicatesEnabled() && currentValues!=null && !currentValues.isEmpty()){
                    for (int i=listItems.size()-1; i>=0; i--){
                        if (currentValues.contains(listItems.get(i).getValue())){
                            listItems.remove(i);
                        }
                    }
               }
               canSort = true;
            }else{
                final EditMaskConstSet mask = (EditMaskConstSet)itemEditMask;
                if (options.isDuplicatesEnabled()){
                    listItems = ListWidgetItem.createForEditMaskConstSet(environment, mask); 
                }else{
                    final EditMaskConstSet fixedMask = 
                        excludeExistingItems(environment.getApplication(), mask, currentValues);
                    listItems = ListWidgetItem.createForEditMaskConstSet(environment, fixedMask); 
                }
                canSort = mask.getOrder()!=EEditMaskEnumOrder.BY_TITLE;
            }
            if (listItems.isEmpty()){
                newValues.add(getInitialItemValue(environment, options, currentValues));
            }else if (listItems.size()==1){
                newValues.add(listItems.get(0).getValue());
            }else{                
                if (!options.isMandatory()){
                    boolean hasNullItem = false;
                    for (ListWidgetItem item: listItems){
                        if (item.getValue()==null){
                            hasNullItem = true;
                            break;
                        }
                    }
                    if (!hasNullItem){
                        final Icon nullIcon = environment.getApplication().getImageManager().getIcon(ClientIcon.ValueModification.CLEAR);
                        final String noValueStr = options.getEditMask().getNoValueStr(environment.getMessageProvider());
                        final ListWidgetItem nullItem = new ListWidgetItem(noValueStr, null, nullIcon);                            
                        listItems.add(nullItem);
                    }
                }
                final IListDialog dialog = 
                    environment.getApplication().getDialogFactory().newListDialog(environment, arrayEditor, null);
                dialog.setItems(listItems);
                dialog.setWindowTitle(environment.getMessageProvider().translate("Value", "Select Values"));
                final EnumSet<IListWidget.EFeatures> features = 
                    EnumSet.of(IListWidget.EFeatures.FILTERING, IListWidget.EFeatures.MULTI_SELECT, IListWidget.EFeatures.SELECTION_LABEL, IListWidget.EFeatures.SELECTION_NOT_EMPTY);
                if (canSort){
                    features.add(IListWidget.EFeatures.MANUAL_SORTING);
                }
                dialog.setFeatures(features);
                dialog.setAcceptOnItemDoubleClick(true);
                dialog.setItemsComparator(new Comparator<ListWidgetItem>(){
                    @Override
                    public int compare(final ListWidgetItem item1, final ListWidgetItem item2) {
                            if (item1.getValue()==null){
                                return item2.getValue()==null ?  0 : 1;
                            }
                            if (item2.getValue()==null){
                                return item1.getValue()==null ? 0 : -1;
                            }
                            final String text1 = item1.getText()==null ? "" : item1.getText();
                            final String text2 = item2.getText()==null ? "" : item2.getText();
                            return text1.compareTo(text2);
                    }                    
                });
                if (dialog.execDialog()==IDialog.DialogResult.ACCEPTED){
                    final List<ListWidgetItem> selectedItems = dialog.getSelectedItems();
                    for (ListWidgetItem selectedItem: selectedItems){
                        newValues.add(selectedItem.getValue());
                    }
                }
            }
        }else {
            newValues.add(getInitialItemValue(environment, options, currentValues));
        }
        return newValues;
    }
    
    protected Object getInitialItemValue(final IClientEnvironment environment,
                                                             final ArrayItemEditingOptions options,
                                                             final List<Object> currentValues) {
        if (!options.isMandatory()){
            return null;
        }
        final EValType type = options.getValType();
        final EditMask editMask = options.getEditMask();
        switch (type) {
            case STR:
            case CHAR:
                if (editMask instanceof EditMaskStr) {
                    return type == EValType.STR ? "" : Character.valueOf(' ');
                } else if (editMask instanceof EditMaskList) {
                    final EditMaskList mask = (EditMaskList) editMask;
                    if (mask.getItems().size() > 0) {
                        return mask.getItems().get(0).getValue();
                    } else {
                        return null;
                    }
                } else if (editMask instanceof EditMaskConstSet) {
                    EditMaskConstSet mask = (EditMaskConstSet) editMask;
                    if (!options.isDuplicatesEnabled()) {
                        mask = excludeExistingItems(environment.getApplication(), mask, currentValues);
                    }
                    if (mask.getItems(environment.getApplication()).size() > 0) {
                        return mask.getItems(environment.getApplication()).getItem(0).getValue();
                    } else {
                        return null;
                    }
                } else if (editMask instanceof EditMaskBool) {
                    return null;
                } else if (editMask instanceof EditMaskFilePath) {
                    return null;
                } else {
                    throw new IllegalUsageError("Edit mask \'" + editMask.getClass().getName() + "\' is not applicable for \'" + type.toString() + "\' type");
                }
            case CLOB:
                if (editMask instanceof EditMaskStr) {
                    return "";
                } else {
                    throw new IllegalUsageError("Edit mask \'" + editMask.getClass().getName() + "\' is not applicable for \'" + type.toString() + "\' type");
                }
            case BIN:
            case BLOB:
                return new Bin(new byte[]{});
            case INT:
                if (editMask instanceof EditMaskInt) {
                    final Long zero = Long.valueOf(0);
                    if (editMask.validate(environment, zero) == ValidationResult.ACCEPTABLE) {
                        return zero;
                    } else {
                        final EditMaskInt editMaskInt = (EditMaskInt) editMask;
                        return Long.valueOf(Math.min(Math.abs(editMaskInt.getMinValue()), Math.abs(editMaskInt.getMaxValue())));
                    }
                } else if (editMask instanceof EditMaskList) {
                    final EditMaskList mask = (EditMaskList) editMask;
                    if (mask.getItems().size() > 0) {
                        return mask.getItems().get(0).getValue();
                    } else {
                        return null;
                    }
                } else if (editMask instanceof EditMaskConstSet) {
                    EditMaskConstSet mask = (EditMaskConstSet) editMask;
                    if (!options.isDuplicatesEnabled()) {
                        mask = excludeExistingItems(environment.getApplication(), mask, currentValues);
                    }
                    if (mask.getItems(environment.getApplication()).size() > 0) {
                        return mask.getItems(environment.getApplication()).getItem(0).getValue();
                    } else {
                        return null;
                    }
                } else if (editMask instanceof EditMaskBool) {
                    return null;
                } else {
                    throw new IllegalUsageError("Edit mask \'" + editMask.getClass().getName() + "\' is not applicable for \'" + type.toString() + "\' type");
                }
            case DATE_TIME:
                if (editMask instanceof EditMaskDateTime) {
                    final Timestamp serverTime = environment.getCurrentServerTime();
                    if (editMask.validate(environment, serverTime) == ValidationResult.ACCEPTABLE) {
                        return serverTime;
                    }
                    final Timestamp zero = new Timestamp(0);
                    if (editMask.validate(environment, zero) == ValidationResult.ACCEPTABLE) {
                        return zero;
                    }
                    return ((EditMaskDateTime) editMask).getMaximumTime();
                } else {
                    throw new IllegalUsageError("Edit mask \'" + editMask.getClass().getName() + "\' is not applicable for \'" + type.toString() + "\' type");
                }
            case NUM:
                if (editMask instanceof EditMaskNum) {
                    if (editMask.validate(environment, BigDecimal.ZERO) == ValidationResult.ACCEPTABLE) {
                        return BigDecimal.ZERO;
                    } else {
                        final EditMaskNum editMaskNum = (EditMaskNum) editMask;
                        if (editMaskNum.getMinValue().abs().compareTo(editMaskNum.getMaxValue().abs()) < 0) {
                            return editMaskNum.getMinValue();
                        } else {
                            return editMaskNum.getMaxValue();
                        }
                    }
                } else if (editMask instanceof EditMaskBool) {
                    return null;
                } else {
                    throw new IllegalUsageError("Edit mask \'" + editMask.getClass().getName() + "\' is not applicable for \'" + type.toString() + "\' type");
                }
            case BOOL:
                if (editMask instanceof EditMaskBool) {
                    return null;
                } else if (editMask instanceof EditMaskNone) {
                    return null;
                } else {
                    throw new IllegalUsageError("Edit mask \'" + editMask.getClass().getName() + "\' is not applicable for \'" + type.toString() + "\' type");
                }
            case PARENT_REF:
                return null;
            default:
                throw new IllegalUsageError("Edit mask \'" + editMask.getClass().getName() + "\' is not applicable for \'" + type.toString() + "\' type");
        }
    }
    
    protected static EditMaskConstSet excludeExistingItems(final IClientApplication application, 
                                                                                       final EditMaskConstSet sourceMask, 
                                                                                       final List<Object> itemValues) {
        if (itemValues!=null && !itemValues.isEmpty()){
            final EditMaskConstSet resultMask = (EditMaskConstSet) EditMask.newCopy(sourceMask);
            final EValType enumType = resultMask.getRadEnumPresentationDef(application).getItemType();
            final RadEnumPresentationDef.Items excludedItems = resultMask.getExcludedItems(application);
            ValAsStr valAsStr;
            RadEnumPresentationDef.Item item;        
            for (Object itemValue : itemValues) {
                valAsStr = ValAsStr.Factory.newInstance(itemValue, enumType);
                item = resultMask.getRadEnumPresentationDef(application).getItems().findItemByValue(valAsStr);
                if (item != null) {
                    excludedItems.addItem(item);
                }
            }
            resultMask.setItems(sourceMask.getItems(application));
            resultMask.setExcludedItems(excludedItems);
            return resultMask;
        }else{
            return sourceMask;
        }
    }
}
