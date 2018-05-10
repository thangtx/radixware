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

import com.trolltech.qt.gui.QWidget;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.dialogs.IListDialog;
import org.radixware.kernel.common.client.meta.mask.EditMaskList;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.types.IEditingHistory;
import org.radixware.kernel.common.client.widgets.IListWidget;
import org.radixware.kernel.common.client.widgets.ListWidgetItem;



public class ValListEditor extends AbstractListEditor<Object> {

    public ValListEditor(final IClientEnvironment environment, final QWidget parent, final EditMaskList editMask, final boolean mandatory, final boolean readOnly) {
        super(environment, parent, editMask, mandatory, readOnly);
        updateComboItems(false);
    }

    public ValListEditor(final IClientEnvironment environment, final QWidget parent, final List<EditMaskList.Item> items){
        this (environment,parent, new EditMaskList(items),true,false);
    }
    
    @Override
    protected void updateComboItems(final boolean updateStylesheet) {        
        final List<EditMaskList.Item> maskItems = getMaskItems();
        final List<ListWidgetItem> items = new LinkedList<>();
        final Object currentValue = getValue();
        final IClientEnvironment env = getEnvironment();
        final boolean isReadOnly = isReadOnly();
        int newIndex = -1;
        boolean nullItemIsPresent = false;
        for (int i = 0; i < maskItems.size(); ++i) {
            final EditMaskList.Item maskItem = maskItems.get(i);
            final Object newValue = maskItem.getValue();
            final boolean isCurrentValue = eq(newValue, currentValue);
            if (isReadOnly && !isCurrentValue){
                continue;
            }
            if (newValue == null) {
                nullItemIsPresent = true;
            }
            final String title = getStringToShow(newValue);
            final ListWidgetItem item = new ListWidgetItem(env,maskItem);
            item.setText(title);
            item.setValue(maskItem.getValAsStr());
            items.add(item);
            if (isCurrentValue){
                if (isReadOnly){
                    newIndex = 0;
                    break;
                }else{
                    newIndex = i;
                }
            }
        }
        clearItems();
        addListWidgetItems(items);
        updateComboBoxLook(newIndex, nullItemIsPresent, updateStylesheet);        
    }    
    
    private List<EditMaskList.Item> getMaskItems(){
        final EditMaskList maskList = (EditMaskList) getEditMask();
        final List<EditMaskList.Item> maskItems = new ArrayList<>(maskList.getItems());        
        if (maskList.isAutoSortByTitles()){
            final IClientEnvironment environment = getEnvironment();
            Collections.sort(maskItems, new Comparator<EditMaskList.Item>(){

                @Override
                public int compare(final EditMaskList.Item item1, final EditMaskList.Item item2) {
                    return item1.getTitle(environment).compareTo(item2.getTitle(environment));
                }
                
            });
        }
        return maskItems;
    }

    @Override
    protected void onActivatedIndex(final int index) {
        if (index < 0) {
            return;
        }
        //final EditMaskList mask = (EditMaskList) getEditMask();
        final List<EditMaskList.Item> maskItems = getMaskItems();
        if (index == maskItems.size()) {
            setValue(null);
        } else {
            final Object newValue = maskItems.get(index).getValue();
            if (getEditMask().validate(getEnvironment(), newValue)==ValidationResult.ACCEPTABLE) {
                setValue(newValue);
            } else {
                updateComboItems(true);
            } 
        }
    }
    
    @Override
    public void setValue(final Object value) {
        if (setOnlyValue(value)){
            updateComboItems(false);//optimization - update style sheet only once in refreshColorSettings() call
            clearBtn.setVisible(!isMandatory() && !isReadOnly() && getValue() != null);
            updateValueMarkers(true);
        }
    }

    @Override
    protected int getMaxItemsInPopup() {
        final EditMaskList maskList = (EditMaskList) getEditMask();
        final int rowsLimit = maskList.getMaxIntemsNumberInDropDownList();
        return rowsLimit>=0 ? rowsLimit : super.getMaxItemsInPopup();
    }
    
    @Override
    protected void beforeShowSelectValueDialog(final IListDialog dialog) {
        final EditMaskList maskList = (EditMaskList) getEditMask();
        if (maskList.isAutoSortByTitles()){
            dialog.setFeatures(EnumSet.of(IListWidget.EFeatures.FILTERING, IListWidget.EFeatures.AUTO_SORTING));
        }else{
            dialog.setFeatures(EnumSet.of(IListWidget.EFeatures.FILTERING, IListWidget.EFeatures.MANUAL_SORTING));
        }
    }    
    
    @Override
    public void setPredefinedValues(final List<Object> predefValues) {
        throw new UnsupportedOperationException("Unsupported operation.");
    }

    @Override
    public List<Object> getPredefinedValues() {
        throw new UnsupportedOperationException("Unsupported operation.");
    }

    @Override
    public void setEditingHistory(final IEditingHistory history) {
    }
    
    public EditMaskList.Item getSelectedItem() {
        Object currentValue = getValue();
        for (EditMaskList.Item item : getMaskItems()) {
            Object editMaskItemValue = item.getValue();
            if (editMaskItemValue == null && currentValue == null) {
                return item;
            } else if (editMaskItemValue != null && currentValue != null) {
                if (editMaskItemValue.equals(currentValue)) {
                    return item;
                }
            }
        }
        return null;
    }
}