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

package org.radixware.wps.views.editors.valeditors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.dialogs.IListDialog;
import org.radixware.kernel.common.client.meta.mask.EditMaskList;
import org.radixware.kernel.common.client.widgets.IListWidget;
import org.radixware.wps.rwt.InputBox;


public class ValListEditorController<T>  extends AbstractListEditorController<T,EditMaskList> {

    
    public ValListEditorController(final IClientEnvironment env, final EditMaskList editMask, final LabelFactory factory) {
        super(env,editMask, factory);
    }

    public ValListEditorController(final IClientEnvironment env, final EditMaskList editMask) {
        super(env, editMask);
    }

    public ValListEditorController(final IClientEnvironment env) {
        super(env, new EditMaskList());
    }
    
    private List<EditMaskList.Item> getMaskItems(){
        final EditMaskList maskList = getEditMask();
        final List<EditMaskList.Item> maskItems;
        if (maskList==null){
            maskItems = Collections.emptyList();
        }else{
            maskItems = new ArrayList<>(maskList.getItems());
        }
        if (maskList!=null && maskList.isAutoSortByTitles()){
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
    
    @SuppressWarnings("unchecked")
    private void updateListItems(){
        final List<EditMaskList.Item> maskItems = getMaskItems();
        final List<TypifiedListWidgetItem<T>> items = new LinkedList<>();
        final IClientEnvironment env = getEnvironment();
        final InputBox.DisplayController<T> displayController = getInputBox().getDisplayController();        
        for (EditMaskList.Item maskItem: maskItems) {
            final T itemValue = (T)maskItem.getValue();
            final String title = displayController.getDisplayValue(itemValue, false, false);
            final TypifiedListWidgetItem<T> item = 
                    new TypifiedListWidgetItem<>(title, itemValue, maskItem.getIcon(env));
            item.setExtendedTitle(maskItem.getExtendedTitle(env));
            item.setToolTip(maskItem.getToolTip(env));
            items.add(item);
        }
        setItems(items);
    }

    @Override
    protected void applyEditMask(final InputBox box) {
        updateListItems();
        super.applyEditMask(box);
    }
    
    @Override
    protected int getMaxItemsInPopup() {
        final EditMaskList maskList = getEditMask();
        if (maskList==null){
            return super.getMaxItemsInPopup();
        }else{
            final int rowsLimit = maskList.getMaxIntemsNumberInDropDownList();
            return rowsLimit>=0 ? rowsLimit : super.getMaxItemsInPopup();
        }
    }
    
    @Override
    protected void beforeShowSelectValueDialog(final IListDialog dialog) {
        final EditMaskList maskList = getEditMask();
        if (maskList!=null && maskList.isAutoSortByTitles()){
            dialog.setFeatures(EnumSet.of(IListWidget.EFeatures.FILTERING, IListWidget.EFeatures.AUTO_SORTING));
        }else{
            dialog.setFeatures(EnumSet.of(IListWidget.EFeatures.FILTERING, IListWidget.EFeatures.MANUAL_SORTING));
        }
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