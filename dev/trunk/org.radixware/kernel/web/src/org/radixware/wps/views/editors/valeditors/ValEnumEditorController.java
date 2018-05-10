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

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.dialogs.IListDialog;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;
import org.radixware.kernel.common.client.meta.mask.EditMaskConstSet;
import org.radixware.kernel.common.client.widgets.IListWidget;
import org.radixware.kernel.common.enums.EEditMaskEnumOrder;
import org.radixware.kernel.common.types.IKernelEnum;
import org.radixware.kernel.common.types.Id;
import org.radixware.wps.rwt.InputBox;


public class ValEnumEditorController<T extends IKernelEnum>  extends AbstractListEditorController<T,EditMaskConstSet> {
    
    private EditMaskConstSet.DisplayMode displayMode = EditMaskConstSet.DisplayMode.SHOW_TITLE;

    public ValEnumEditorController(final IClientEnvironment env) {
        this(env, null, null);
    }

    public ValEnumEditorController(final IClientEnvironment env, final Id enumerationId) {
        this(env, enumerationId, null);
    }
    
    public ValEnumEditorController(final IClientEnvironment env, final Id enumerationId, final LabelFactory factory) {
        super(env, enumerationId==null ? null : new EditMaskConstSet(enumerationId), factory);
    }

    @Override    
    protected void applyEditMask(final InputBox box) {
        updateListItems();
        super.applyEditMask(box);
    } 

    @SuppressWarnings("unchecked")
    private void updateListItems(){
        final EditMaskConstSet editMaskConstSet = getEditMask();
        if (editMaskConstSet!=null){
            final RadEnumPresentationDef.Items items = editMaskConstSet.getItems(getEnvironment().getApplication());            
            final List<TypifiedListWidgetItem<T>> listItems = new LinkedList<>();       
            final InputBox.DisplayController<T> displayController = getInputBox().getDisplayController();
            for (int i = 0, count = items.size(); i < count; ++i) {
                final T itemValue = (T)items.getItem(i).getConstant();
                final String title = displayController.getDisplayValue(itemValue, false, false);
                listItems.add(new TypifiedListWidgetItem<>(title, itemValue, items.getItem(i).getIcon()));
            }
            setItems(listItems);
        }
    }
    
    private RadEnumPresentationDef getEnumDef(){
        final EditMaskConstSet editMaskConstSet = getEditMask();
        if (editMaskConstSet==null){
            return null;
        }else{
            return editMaskConstSet.getRadEnumPresentationDef(getEnvironment().getApplication());
        }
    }    
    
    @Override
    protected String getSelectValueDialogConfigPrefix() {
        final RadEnumPresentationDef enumDef = getEnumDef();
        if (enumDef==null){
            return super.getSelectValueDialogConfigPrefix();
        }else{
            return enumDef.getId().toString();
        }
    }

    @Override
    protected int getMaxItemsInPopup() {
        final EditMaskConstSet mask = getEditMask();
        if (mask==null){
            return super.getMaxItemsInPopup();
        }else{
            final int itemsLimit = mask.getMaxIntemsNumberInDropDownList();
            return itemsLimit>=0 ? itemsLimit : super.getMaxItemsInPopup();
        }
    }

    @Override
    protected void beforeShowSelectValueDialog(final IListDialog dialog) {
        final EditMaskConstSet mask = getEditMask();
        if (mask!=null && mask.getOrder()==EEditMaskEnumOrder.BY_TITLE){
            dialog.setFeatures(EnumSet.of(IListWidget.EFeatures.FILTERING, IListWidget.EFeatures.AUTO_SORTING));
        }else{
            dialog.setFeatures(EnumSet.of(IListWidget.EFeatures.FILTERING, IListWidget.EFeatures.MANUAL_SORTING));
        }
    }        
    
    public EditMaskConstSet.DisplayMode getDisplayMode() {
        return displayMode;
    }

    public void setDisplayMode(final EditMaskConstSet.DisplayMode mode) {
        if (displayMode != mode) {
            displayMode = mode;
            if (editMask!=null){//getEditMask() returns copy
                editMask.setDisplayMode(displayMode);
            }
            updateListItems();
            refresh();
        }
    }   
}
