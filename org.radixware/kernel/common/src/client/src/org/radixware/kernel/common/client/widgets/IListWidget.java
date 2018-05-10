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

package org.radixware.kernel.common.client.widgets;

import java.util.Collection;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import org.radixware.kernel.common.client.text.TextOptions;


public interface IListWidget extends IWidget{
    
    public static enum EFeatures{ FILTERING, MANUAL_SORTING, AUTO_SORTING, MULTI_SELECT, SELECTION_LABEL, SELECTION_NOT_EMPTY};
    
    public interface ICurrentItemListener{
        void currentItemChanged(ListWidgetItem item);
    }        
    
    public interface IDoubleClickListener{
        void itemDoubleClick(ListWidgetItem item);
    }
    
    public interface IFilterListener{
        void afterApplyFilter(String filter);
    }
    
    public interface ISelectionListener{
        void selectionChanged(List<ListWidgetItem> selectedItems);
    }
    
    void add(ListWidgetItem item);
    void insert(ListWidgetItem item, int row);
    void clear();
    void remove(ListWidgetItem item);
    void removeRow(int row);
    
    void swapItems(ListWidgetItem item1, ListWidgetItem item2);
    void swapRows(int row1, int row2);
    
    void setCurrent(ListWidgetItem item);
    ListWidgetItem getCurrent();
    void setCurrentRow(int row); 
    int getCurrentRow();
    
    void setSelectedItems(Collection<ListWidgetItem> selectedItems);
    List<ListWidgetItem> getSelectedItems();
    List<Integer> getSelectedRows();
    
    boolean isItemSelected(final ListWidgetItem item);
    void setItemSelected(final ListWidgetItem item, final boolean isSelected);
    
    void setItems(List<ListWidgetItem> items);
    List<ListWidgetItem> getItems();
    
    ListWidgetItem getItem(int row);    
    int count();
    
    void setItemsComparator(Comparator<ListWidgetItem> comparator);
    Comparator<ListWidgetItem> getItemsComparator();
    
    void setFeatures(EnumSet<EFeatures> features);
    EnumSet<EFeatures> getFeatures();
    
    void setTextOptions(TextOptions textOptions);
    TextOptions getTextOptions();
    
    void setSelectionEnabled(final boolean isEnabled);
    boolean isSelectionEnabled();
    
    void setFocusToFilterString();
    
    void addCurrentItemListener(ICurrentItemListener listener);
    void removeCurrentItemListener(ICurrentItemListener listener);
    
    void addDoubleClickListener(IDoubleClickListener listener);
    void removeDoubleClickListener(IDoubleClickListener listener);
    
    void addFilterListener(IFilterListener listener);
    void removeFilterListener(IFilterListener listener);
    
    void addSelectionListener(ISelectionListener listener);
    void removeSelectionListener(ISelectionListener listener);    
}

