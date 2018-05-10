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

package org.radixware.wps.views.editors.valeditors;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.types.IEditingHistory;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.wps.rwt.InputBox;
import org.radixware.wps.rwt.ToolButton;
import org.radixware.wps.rwt.DropDownListDelegate;


public abstract class AbstractListEditorController<T, V extends EditMask> extends InputBoxController<T, V> {    

    private final static class DropDownDelegate<T> extends DropDownListDelegate<T>{
                
        private final List<DropDownListItem<T>> listBoxItems = new LinkedList<>();
    
        public DropDownDelegate() {
        }
        
        public void setItems(final List<TypifiedListWidgetItem<T>> items){
            listBoxItems.clear();
            if (items!=null){
                for (TypifiedListWidgetItem<T> item: items){
                    listBoxItems.add(createDropDownListItem(item));
                }
            }
        }
        
        private DropDownListItem<T> createDropDownListItem(final TypifiedListWidgetItem<T> listItem){
            final DropDownListItem<T> item = 
                new DropDownListItem<>(listItem.getText(), listItem.getValue(), listItem.getIcon());
            final String itemName = listItem.getName();
            if (itemName!=null && !itemName.isEmpty()){
                item.setObjectName(itemName);
            }
            return item;
        }

        @Override
        protected List<DropDownListItem<T>> getItems() {
           return listBoxItems;
        }        
    }    
        
    private final DropDownDelegate<T> dropDownDelegate = new DropDownDelegate<>();
    private final ToolButton editBtn = new ToolButton();
    private final List<TypifiedListWidgetItem<T>> items = new LinkedList<>();    
    private int nullItemIndex = -1;
    
    public AbstractListEditorController(final IClientEnvironment environment, final V editMask){
        this(environment, editMask, null);
    }

    public AbstractListEditorController(final IClientEnvironment environment, final V editMask, final LabelFactory factory){
        super(environment,factory);
        editBtn.setToolTip(getEnvironment().getMessageProvider().translate("Value", "Edit Value"));
        editBtn.setText("...");
        editBtn.addClickHandler(new IButton.ClickHandler() {
            @Override
            public void onClick(final IButton source) {
                showDialogList();
            }
        });
        editBtn.setObjectName("tbSelectValue");
        editBtn.setVisible(false);
        addButton(editBtn);
        getInputBox().addDropDownDelegate(dropDownDelegate);
        if (editMask!=null){
            setEditMask(editMask);
        }
        subscribeToChangeSettingsEvent();
    }
    
    protected final void setItems(final List<TypifiedListWidgetItem<T>> listItems){
        items.clear();
        if (listItems!=null){
            nullItemIndex = -1;
            for (int i=0,count=listItems.size(); i<count; i++){
                items.add(listItems.get(i));
                if (items.get(i).getValue()==null){
                    nullItemIndex = i;
                }
            }
        }
        updateButtonsState();
    }    

    @Override
    protected void afterChangeReadOnly() {
        updateButtonsState();
        super.afterChangeReadOnly();
    }        
    
    private void updateButtonsState(){
        final boolean canChangeValue = canChangeValue();
        final boolean isShowDialogListBtnVisible = isShowListDialogButtonVisible();
        dropDownDelegate.setItems(!canChangeValue || isShowDialogListBtnVisible ? null : items);     
        editBtn.setVisible(isShowDialogListBtnVisible);
        getInputBox().updataButtonsState();
    }
    
    private boolean canChangeValue() {
        final int itemsCount = getItemsCount();
        return !isReadOnly()
                && itemsCount > 0
                && (itemsCount > 1 || getValue() == null || getEditMask().validate(getEnvironment(),getValue())!=ValidationResult.ACCEPTABLE || !isMandatory());
    }
    
    private boolean isShowListDialogButtonVisible(){
        final int limit = getMaxItemsInPopup();
        if (limit>=0){
            int itemsCount = getItemsCount();
            if (nullItemIndex>=0){
                itemsCount--;
            }
            return !isReadOnly()
                && itemsCount > 0
                && (itemsCount > 1 || getValue() == null || getEditMask().validate(getEnvironment(),getValue())!=ValidationResult.ACCEPTABLE || !isMandatory())
                && itemsCount>limit;            
        }else{
            return false;
        }
    }

    protected final int getItemsCount(){
        return items.size();
    }    
    
    protected int getMaxItemsInPopup(){
        return getEnvironment().getConfigStore().readInteger(MAX_ITEMS_IN_DD_LIST_SETTING_KEY);
    }    
    
    private void showDialogList(){
        final List<TypifiedListWidgetItem<T>> listItems = new LinkedList<>();
        int currentItemIndex = -1;
        final T currentValue = getValue();
        for (int i=0,count=items.size(); i<count; i++){
            if (i!=nullItemIndex){
                listItems.add(items.get(i));
                if (Objects.equals(items.get(i).getValue(), currentValue)){
                    currentItemIndex = -1;
                }
            }
        }
        final int selectedItemIndex = selectItem(listItems, currentItemIndex);
        if (selectedItemIndex>=0){
            onSelectListWidgetItem(listItems.get(selectedItemIndex));
        }
    }    

    @Override
    protected void onSettingsChanged() {
        updateButtonsState();
        super.onSettingsChanged();
    }
    
    @Override
    protected final InputBox.ValueController<T> createValueController() {
        return null;//keyboard input is not allowed
    }    
    
    @Override
    public final void setPredefinedValues(final List<T> predefValues) {
        if (predefValues!=null && !predefValues.isEmpty()){
            throw new UnsupportedOperationException("Unsupported operation.");
        }
    }

    @Override
    public final List<T> getPredefinedValues () {
        return Collections.<T>emptyList();
    }

    @Override
    public final void setEditHistory(final IEditingHistory history, EValType type) {
    }    
    
}
