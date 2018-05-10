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

package org.radixware.wps.rwt;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.ETextAlignment;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.wps.text.WpsTextOptions;


public abstract class LazyDropDownListDelegate<T> extends DropDownListDelegate<T> {
    
    private final class InternalListBox extends ListBox{
        
        public InternalListBox(){
            super();
            setBackground(Color.WHITE);
            setMinimumHeight(40);
            getHtml().setAttr("onfocus","$RWT.listBox.syncScroll");
        }

        @Override
        protected List<ListBoxItem> loadMore() {
            final List<DropDownListItem<T>> newItems = LazyDropDownListDelegate.this.loadMore();
            final List<ListBoxItem> items = new LinkedList<>();
            if (newItems!=null){
                if (pleaseWaitItem!=null){
                    remove(pleaseWaitItem);
                }
                items.addAll(newItems);
            }
            return items;
        }
                
        @Override
        protected boolean hasMoreData() {
            return LazyDropDownListDelegate.this.hasMoreItems();
        }        
    }
        
    private List<DropDownListItem<T>> items;
    private DropDownListItem<T> pleaseWaitItem;
    
    @Override
    protected List<String> getInputHandlers(final InputBox box) {
        if (box.isEnabled() && !box.isReadOnly()) {
            return Arrays.asList("alt+down");
        } else {
            return Collections.emptyList();
        }
    }
    
    @Override
    protected void handleHotKey(final String hotKey, final InputBox box) {
        if ("alt+down".equals(hotKey)) {
            expose(box);
        }
    }

    @Override
    protected void updateButton(final ToolButton button, final InputBox inputBox) {
        button.setVisible(!inputBox.isReadOnly());
        if (inputBox.getValue() != null) {
            for (DropDownListItem<T> item : getItems()) {
                if (Utils.equals(item.getValue(), inputBox.getValue())) {
                    inputBox.setValueIcon(item.getIcon());
                    break;
                }
            }
        }
    }
    
    @Override
    protected List<DropDownListItem<T>> getItems(){
        if (items==null){
            loadMore();
        }
        return items;
    }
    
    private List<DropDownListItem<T>> loadMore(){
        final int itemsCount;
        if (items==null){
            itemsCount = 0;
        }else{
            itemsCount = pleaseWaitItem!=null && hasMoreItems() ? items.size() - 1 : items.size();
        }
        List<DropDownListItem<T>> newItems = loadMoreItems(itemsCount);
        if (pleaseWaitItem!=null && hasMoreItems()){
            if (newItems==null){
                newItems = new LinkedList<>();                
            }
            newItems.add(pleaseWaitItem);
        }        
        if (items==null){
            items = new LinkedList<>();
        }
        if (newItems!=null){
            items.addAll(newItems);
        }
        return newItems;
    }
    
    protected abstract List<DropDownListItem<T>> loadMoreItems(int itemsCount);
    
    protected abstract boolean hasMoreItems();

    @Override
    protected ListBox createListBox(final IClientEnvironment environment) {
        pleaseWaitItem = createPleaseWaitItem(environment);
        return new InternalListBox();
    }        
    
    public void reset(){        
        items = null;
    }
    
    protected DropDownListItem<T> createPleaseWaitItem(final IClientEnvironment environment){        
        final DropDownListItem<T> item = 
            new DropDownListItem<>(environment.getMessageProvider().translate("Wait Dialog", "Please Wait..."), null);
        item.setEnabled(false);
        item.setForeground(Color.gray);
        item.setTextOptions(WpsTextOptions.Factory.getOptions(null, ETextAlignment.CENTER, Color.gray, null));
        return item;
    }    
    
}
