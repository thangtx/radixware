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

import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QWidget;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.dialogs.IListDialog;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;
import org.radixware.kernel.common.client.meta.mask.EditMaskConstSet;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.types.IEditingHistory;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.utils.ValueConverter;
import org.radixware.kernel.common.client.widgets.IListWidget;
import org.radixware.kernel.common.client.widgets.ListWidgetItem;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EEditMaskEnumOrder;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.IKernelEnum;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.explorer.types.RdxIcon;
import org.radixware.kernel.explorer.widgets.propeditors.IDisplayStringProvider;

/**
 * ValConstSetEditor - редактор для значений типа Char, Long, BigDecimal, String.
 * 
 */
public class ValConstSetEditor extends AbstractListEditor<Object> {

    private EditMaskConstSet.DisplayMode displayMode = EditMaskConstSet.DisplayMode.SHOW_TITLE;
    private RadEnumPresentationDef.Items currentItems;
    private ValAsStr currentValAsStr;
    private IDisplayStringProvider currentDisplayStringProvider;
    private boolean isMandatory;
    private boolean isReadOnly;
    private int currentItemIndex;

    public ValConstSetEditor(final IClientEnvironment environment, final QWidget parent,
            final EditMaskConstSet editMaskConstSet,
            final boolean mandatory,
            final boolean readOnly) {
        super(environment, parent, editMaskConstSet, mandatory, readOnly);
        displayMode = editMaskConstSet.getDisplayMode();
        updateComboItems(false);
    }

    public ValConstSetEditor(final IClientEnvironment environment, final QWidget parent, final Id enumId){
        this (environment, parent, new EditMaskConstSet(enumId), true, false);
    }

    @Override
    protected void updateComboItems(final boolean updateStyleSheet) {
        final EditMaskConstSet editMaskConstSet = (EditMaskConstSet) getEditMask();
        final ValAsStr valAsStr = ValAsStr.Factory.newInstance(getValue(), editMaskConstSet.getRadEnumPresentationDef(getEnvironment().getApplication()).getItemType());
        final RadEnumPresentationDef.Items items = editMaskConstSet.getItems(getEnvironment().getApplication());        
        boolean nullItemIsPresent = items.size()<getItemsCount();
        if (currentItems==null 
            || !Objects.equals(currentValAsStr, valAsStr) 
            || !currentItems.equals(items)
            || currentDisplayStringProvider!=getDisplayStringProvider()
            || isMandatory()!=isMandatory
            || isReadOnly()!=isReadOnly){
            final List<ListWidgetItem> listItems = new LinkedList<>();
            //final List<String> titles = new ArrayList<>();
            //final List<QIcon> icons = new ArrayList<>();
            isMandatory = isMandatory();
            isReadOnly = isReadOnly();
            currentDisplayStringProvider = getDisplayStringProvider();
            currentValAsStr = valAsStr;
            currentItems = items.copy();
            final RadEnumPresentationDef.Item currentItem = editMaskConstSet.getRadEnumPresentationDef(getEnvironment().getApplication()).getItems().findItemByValue(valAsStr);
            RadEnumPresentationDef.Item enumItem;
            nullItemIsPresent = false;
            currentItemIndex = -1;            
            for (int i = 0; i < currentItems.size(); ++i) {
                enumItem = currentItems.getItem(i);
                final Comparable itemValue = enumItem.getValue();
                if (itemValue == null) {
                    nullItemIsPresent = true;
                }

                if (!isReadOnly) {
                    if (currentItem != null && Utils.equals(itemValue, currentItem.getValue())) {
                        currentItemIndex = i;
                    }
                    listItems.add(createListWidgetItem(enumItem));
                } else if (currentItem != null && Utils.equals(itemValue, currentItem.getValue())) {
                    listItems.add(createListWidgetItem(enumItem));
                    currentItemIndex = 0;
                    break;
                }
            }            
            setListWidgetItems(listItems);
        }
        updateComboBoxLook(currentItemIndex, nullItemIsPresent, updateStyleSheet);
    }
    
    private ListWidgetItem createListWidgetItem(RadEnumPresentationDef.Item enumItem){
        final String title = getStringToShow(enumItem.getConstant());//it can be optimized        
        final ValAsStr valAsStr = enumItem.getValAsStr();        
        final ListWidgetItem listItem = 
            new ListWidgetItem(title, valAsStr==null ? null : valAsStr.toString(), enumItem.getIcon());
        listItem.setName("rx_enum_item_"+enumItem.getId().toString());
        return listItem;
    }

    @Override
    protected void onActivatedIndex(final int index) {
        if (index < 0) {
            return;
        }
        final EditMaskConstSet mask = (EditMaskConstSet) getEditMask();
        if (index == mask.getItems(getEnvironment().getApplication()).size()) {
            setValue(null);
        } else {
            final RadEnumPresentationDef.Item item = mask.getItems(getEnvironment().getApplication()).getItem(index);
            if (getEditMask().validate(getEnvironment(),item.getValue())==ValidationResult.ACCEPTABLE) {
                setValue(item.getValue());
            } else {
                updateComboItems(true);
            }
        }
    }
    
    @Override
    protected boolean setOnlyValue(final Object value) {
        if (eq(this.value, value)) {
            return false;
        }
        // check: value == null && mandatory
        this.value = value;
        valueChanged.emit(value);
        editingFinished.emit(value);
        setFocus();
        return true;
    }
    
    @Override
    public void setValue(final Object value) {
        final Object rawValue = value instanceof IKernelEnum ? ((IKernelEnum) value).getValue() : value;
        if (setOnlyValue(rawValue)){
            updateComboItems(false);//optimization - update style sheet once in refreshColorSettings() call
            doValidation();
            clearBtn.setVisible(!isMandatory() && !isReadOnly() && getValue() != null); 
            updateValueMarkers(true);
        }
    }
    
    @Override
    protected String getStringToShow(final Object value){
        if (value instanceof IKernelEnum==false && value instanceof Comparable){
            final RadEnumPresentationDef.Item itemDef = getEnumDef().findItemByValue((Comparable) value );
            return super.getStringToShow(itemDef==null ? value : itemDef.getConstant());
        }else{
            return super.getStringToShow(value);
        }
    }
    
    private RadEnumPresentationDef getEnumDef(){
        return ((EditMaskConstSet)getEditMask()).getRadEnumPresentationDef(getEnvironment().getApplication());
    }
    
    public IKernelEnum getEnumItem() {
        final RadEnumPresentationDef.Item itemDef = getEnumDef().findItemByValue((Comparable) getValue());
        return itemDef != null ? itemDef.getConstant() : null;
    }
 
    public EditMaskConstSet.DisplayMode getDisplayMode() {
        return displayMode;
    }

    public void setDisplayMode(final EditMaskConstSet.DisplayMode mode) {
        if (displayMode != mode) {
            displayMode = mode;
            ((EditMaskConstSet) getEditMask()).setDisplayMode(displayMode);
            updateComboItems(false);
            refresh();
        }
    }

    @Override
    protected String getSelectValueDialogConfigPrefix() {
        return getEnumDef().getId().toString();
    }

    @Override
    protected int getMaxItemsInPopup() {
        final EditMaskConstSet mask = (EditMaskConstSet) getEditMask();
        final int itemsLimit = mask.getMaxIntemsNumberInDropDownList();
        return itemsLimit>=0 ? itemsLimit : super.getMaxItemsInPopup();
    }

    @Override
    protected void beforeShowSelectValueDialog(final IListDialog dialog) {
        final EditMaskConstSet mask = (EditMaskConstSet) getEditMask();
        if (mask.getOrder()==EEditMaskEnumOrder.BY_TITLE){
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
}
