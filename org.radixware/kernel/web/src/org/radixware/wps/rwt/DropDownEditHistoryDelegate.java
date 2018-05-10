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


package org.radixware.wps.rwt;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import org.radixware.kernel.common.client.types.IEditingHistory;
import org.radixware.kernel.common.client.types.RefEditingHistory;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.utils.Utils;

public class DropDownEditHistoryDelegate<T> extends DropDownListDelegate<T> {

    private List<DropDownListItem<T>> listBoxItems;

    public DropDownEditHistoryDelegate(InputBox box, IEditingHistory history, EValType type) {
        updateItems(box, history, type);
        setDisplayCurrentItemInDropDownList(true);
    }
    
    public boolean containsValue(final T value){
        final List<DropDownListItem<T>> items = getItems();
        for (DropDownListItem<T> item: items){
            if (Objects.equals(value, item.getValue())){
                return true;
            }
        }
        return false;
    }

    @Override
    public List<DropDownListItem<T>> getItems() {
        if (listBoxItems != null && !listBoxItems.isEmpty() && listBoxItems.size() >= 10) {
            listBoxItems.remove(listBoxItems.size() - 1);
        }
        return listBoxItems;
    }

    @SuppressWarnings("unchecked")
    public final void updateItems(InputBox box, IEditingHistory history, EValType type) {
        listBoxItems = new LinkedList<>();
        if (history == null || box == null) {
            return;
        }        
        final InputBox.DisplayController displayController = box.getDisplayController();
        if (type == EValType.PARENT_REF) {
            RefEditingHistory refHistory = (RefEditingHistory) history;
            for (Reference rr : refHistory.getReferences()){
                final String title = displayController.getDisplayValue(rr, false, box.isReadOnly());
                listBoxItems.add(new DropDownListItem(title, rr));
            }
        }else{
            final List<String> items = history.getEntries();
            T value;
            for (String item : items) {
                value = (T) ValAsStr.fromStr(item, type);
                final String title = displayController.getDisplayValue(value, false, box.isReadOnly());
                listBoxItems.add(new DropDownListItem<>(title, value));
            }
        }
    }

    @Override
    protected void updateButton(ToolButton button, InputBox inputBox) {
        final List<DropDownListItem<T>> items = getItems();
        final int itemsCount = items == null ? 0 : items.size();
        final boolean isValid = inputBox.getValidationMessage() == null || inputBox.getValidationMessage().isEmpty();
        final boolean isPopupEnabled = !inputBox.isReadOnly()
                && itemsCount > 0
                && (itemsCount > 1 || inputBox.getValue() == null || !isValid || inputBox.isClearable());
        button.setVisible(isPopupEnabled);
        if (items != null && inputBox.getValue() != null) {
            for (DropDownListItem<T> item : items) {
                if (Utils.equals(item.getValue(), inputBox.getValue())) {
                    inputBox.setValueIcon(item.getIcon());
                    break;
                }
            }
        }
    }
}
