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

package org.radixware.kernel.explorer.dialogs;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QWidget;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.dialogs.IListDialog;
import org.radixware.kernel.common.client.meta.mask.validators.EValidatorState;
import org.radixware.kernel.common.client.meta.mask.validators.InvalidValueReason;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.text.TextOptions;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.IListWidget;
import org.radixware.kernel.common.client.widgets.IPushButton;
import org.radixware.kernel.common.client.widgets.ListWidgetItem;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.widgets.ExplorerListWidget;


public class ExplorerListDialog extends ExplorerDialog implements IListDialog{
    
    private final ExplorerListWidget list;
    private boolean acceptOnDoubleClick = true;
    
    private final IListWidget.IDoubleClickListener doubleClickListener = new IListWidget.IDoubleClickListener() {
        @Override
        public void itemDoubleClick(final ListWidgetItem item) {
            if (list.getSelectedItems().size()==1 && list.getSelectedItems().get(0).equals(item)){
                ExplorerListDialog.this.acceptDialogSlot();
            }
        }
    };
    
    private final IListWidget.ISelectionListener selectionChanged = new IListWidget.ISelectionListener() {

        @Override
        public void selectionChanged(final List<ListWidgetItem> selectedItems) {
            ExplorerListDialog.this.onSelectionChanged(selectedItems);
        }
        
    };
    
    private final IListWidget.ICurrentItemListener currentItemListener = new IListWidget.ICurrentItemListener(){
        @Override
        public void currentItemChanged(final ListWidgetItem item) {
            ExplorerListDialog.this.onCurrentItemChanged(item);
        }    
    };
       
    public ExplorerListDialog(final IClientEnvironment environment, final QWidget parentWidget, final String dlgName){
        super(environment, parentWidget, dlgName);
        list = new ExplorerListWidget(environment, this);
        dialogLayout().addWidget(list);
        list.addDoubleClickListener(doubleClickListener);
        list.addCurrentItemListener(currentItemListener);
        list.addSelectionListener(selectionChanged);
        addButton(EDialogButtonType.OK).addClickHandler(new IButton.ClickHandler() {
            @Override
            public void onClick(final IButton source) {
                onOkButtonClick();
            }
        });
        getButton(EDialogButtonType.OK).setEnabled(false);
        addButtons(EnumSet.of(EDialogButtonType.CANCEL), false);
        getButton(EDialogButtonType.CANCEL).addClickHandler(new IButton.ClickHandler() {

            @Override
            public void onClick(IButton source) {
                ExplorerListDialog.this.done(-1);
            }
        });
        setWindowTitle(environment.getMessageProvider().translate("Value", "Select Value"));
        setAttribute(Qt.WidgetAttribute.WA_DeleteOnClose, true);
    }
    
    public ExplorerListDialog(final IClientEnvironment environment, final QWidget parentWidget){
        this(environment, parentWidget, null);
    }
    
    private void onCurrentItemChanged(final ListWidgetItem item){
        final IPushButton button = getButton(EDialogButtonType.OK);
        if (button!=null){
            if (list.getFeatures().contains(EFeatures.MULTI_SELECT) &&
                !list.getFeatures().contains(EFeatures.SELECTION_NOT_EMPTY)) {
                button.setEnabled(true);
            } else {
                button.setEnabled(item!=null);
            }
        }        
    }
    
    private void onSelectionChanged(final List<ListWidgetItem> selection){
        if (list.getFeatures().contains(IListWidget.EFeatures.MULTI_SELECT)){
            final IPushButton button = getButton(EDialogButtonType.OK);
            if (button!=null && list.getFeatures().contains(EFeatures.SELECTION_NOT_EMPTY)){
                button.setEnabled(!selection.isEmpty());
            } else {
                button.setEnabled(true);
            }
        }
    }
    
    private void onOkButtonClick(){
        acceptDialogSlot();
    }

    @Override
    public int exec() {
        if (list.getFeatures().contains(IListWidget.EFeatures.FILTERING)){
            setFocusToFilterString();
        }
        return super.exec();
    }                

    @Override
    public void setAcceptOnItemDoubleClick(final boolean accept) {        
        if (acceptOnDoubleClick!=accept){
            if (accept){
                addDoubleClickListener(doubleClickListener);
            }else{
                removeDoubleClickListener(doubleClickListener);
            }
            acceptOnDoubleClick = accept;
        }
    }

    @Override
    public boolean isAcceptOnItemDouableClick() {
        return acceptOnDoubleClick;
    }

    @Override
    public void add(final ListWidgetItem item) {
        list.add(item);
    }

    @Override
    public void insert(final ListWidgetItem item, final int row) {
        list.insert(item, row);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public void remove(final ListWidgetItem item) {
        list.remove(item);
    }

    @Override
    public void removeRow(final int row) {
        list.removeRow(row);
    }

    @Override
    public void swapItems(final ListWidgetItem item1, final ListWidgetItem item2) {
        list.swapItems(item1, item2);
    }

    @Override
    public void swapRows(final int row1, final int row2) {
        list.swapRows(row1, row2);
    }

    @Override
    public void setCurrent(final ListWidgetItem item) {
        list.setCurrent(item);
    }

    @Override
    public ListWidgetItem getCurrent() {
        return list.getCurrent();
    }

    @Override
    public void setCurrentRow(final int row) {
        list.setCurrentRow(row);
    }

    @Override
    public int getCurrentRow() {
        return list.getCurrentRow();
    }

    @Override
    public void setSelectedItems(final Collection<ListWidgetItem> selectedItems) {
        list.setSelectedItems(selectedItems);
    }

    @Override
    public List<ListWidgetItem> getSelectedItems() {
        return list.getSelectedItems();
    }

    @Override
    public List<Integer> getSelectedRows() {
        return list.getSelectedRows();
    }

    @Override
    public boolean isItemSelected(final ListWidgetItem item) {
        return list.isItemSelected(item);
    }        
    
    @Override
    public void setItemSelected(final ListWidgetItem item, final boolean isSelected) {
        list.setItemSelected(item, isSelected);
    }    

    @Override
    public void setItems(final List<ListWidgetItem> items) {
        list.setItems(items);
    }        

    @Override
    public List<ListWidgetItem> getItems() {
        return list.getItems();
    }

    @Override
    public ListWidgetItem getItem(final int row) {
        return list.getItem(row);
    }

    @Override
    public int count() {
        return list.count();
    }

    @Override
    public void setItemsComparator(final Comparator<ListWidgetItem> comparator) {
        list.setItemsComparator(comparator);
    }

    @Override
    public Comparator<ListWidgetItem> getItemsComparator() {
        return list.getItemsComparator();
    }

    @Override
    public void setFeatures(final EnumSet<EFeatures> features) {
        list.setFeatures(features);
        if (list.getFeatures().contains(IListWidget.EFeatures.MULTI_SELECT) &&
                !this.list.getFeatures().contains(IListWidget.EFeatures.SELECTION_NOT_EMPTY)) {
            IPushButton okButton = getButton(EDialogButtonType.OK);
            if (okButton != null) {
                okButton.setEnabled(true);
            }
        }
    }

    @Override
    public EnumSet<EFeatures> getFeatures() {
        return list.getFeatures();
    }

    @Override
    public void setTextOptions(final TextOptions textOptions) {
        list.setTextOptions(textOptions);
    }

    @Override
    public TextOptions getTextOptions() {
        return list.getTextOptions();
    }

    @Override
    public void setSelectionEnabled(final boolean isEnabled) {
        if (isEnabled!=list.isSelectionEnabled()){
            list.setSelectionEnabled(isEnabled);
            clearButtons();
            if (isEnabled){
                addCurrentItemListener(currentItemListener);                 
                addButtons(EnumSet.of(EDialogButtonType.OK, EDialogButtonType.CANCEL), true);
                getButton(EDialogButtonType.OK).setEnabled(getCurrent()!=null);
            }else{
                removeCurrentItemListener(currentItemListener);
                addButtons(EnumSet.of(EDialogButtonType.CLOSE), true);
            }
        }
    }

    @Override
    public boolean isSelectionEnabled() {
        return list.isSelectionEnabled();
    }

    @Override
    public void setFocusToFilterString() {
        list.setFocusToFilterString();
    }
        
    @Override
    public void addCurrentItemListener(final ICurrentItemListener listener) {
        list.addCurrentItemListener(listener);
    }

    @Override
    public void removeCurrentItemListener(final ICurrentItemListener listener) {
        list.removeCurrentItemListener(listener);
    }

    @Override
    public void addDoubleClickListener(final IDoubleClickListener listener) {
        list.addDoubleClickListener(listener);
    }

    @Override
    public void removeDoubleClickListener(final IDoubleClickListener listener) {
        list.removeDoubleClickListener(listener);
    }

    @Override
    public void addFilterListener(final IFilterListener listener) {
        list.addFilterListener(listener);
    }

    @Override
    public void removeFilterListener(final IFilterListener listener) {
        list.removeFilterListener(listener);
    }

    @Override
    public void addSelectionListener(final ISelectionListener listener) {
        list.addSelectionListener(listener);
    }

    @Override
    public void removeSelectionListener(final ISelectionListener listener) {
        list.removeSelectionListener(listener);
    }

    @Override
    public void accept() {
        acceptDialogSlot();
    }
    
    private void acceptDialogSlot() {
        List<ListWidgetItem> itemsList = list.getSelectedItems();
        if (itemsList != null && !itemsList.isEmpty()) {
            List<ValidationResult> validationList = new LinkedList<>();
            for (ListWidgetItem item : itemsList) {
                if (item.getValidationResult().getState() != EValidatorState.ACCEPTABLE) {
                    if (validationList.isEmpty()) {
                        setCurrent(item);
                    }
                    validationList.add(item.getValidationResult());
                }
            }
            if (!validationList.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (ValidationResult result : validationList) {
                    sb.append(result.getInvalidValueReason().getMessage(getEnvironment().getMessageProvider(), InvalidValueReason.EMessageType.Value));
                    sb.append("\n");
                }
                getEnvironment().messageError("Validation Error", sb.toString());
            } else {
                done(1);
            }
        } else {
            done(1);
        }
    }
    
}
