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

package org.radixware.wps.views.selector;

import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMaskList;
import org.radixware.kernel.common.client.meta.mask.EditMaskList.Item;
import org.radixware.kernel.common.client.models.items.SelectorColumnModelItem;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.wps.dialogs.FindAndReplaceDialog;
import org.radixware.wps.rwt.HorizontalBoxContainer;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.rwt.ValueEditor;
import org.radixware.wps.views.editors.valeditors.ValBoolEditorController;
import org.radixware.wps.views.editors.valeditors.ValListEditorController;
import org.radixware.wps.views.editors.valeditors.ValStrEditorController;


public final class FindInSelectorDialog extends FindAndReplaceDialog {
    
    private static class SearchWidget extends HorizontalBoxContainer implements ISearchWidget {
        
        private final ValStrEditorController vsEditor;
        private final ValBoolEditorController vbEditor;
        private boolean isBool = false;
        private final IClientEnvironment env;
        public SearchWidget(IClientEnvironment env) {
            this.env = env;
            vsEditor = new ValStrEditorController(env);
            vsEditor.setMandatory(true);
            vbEditor = new ValBoolEditorController(env);
            ((UIObject)vbEditor.getValEditor()).getHtml().setCss("display", "none");
            this.add((UIObject)vsEditor.getValEditor());
            this.setAutoSizeChildren(true);
            this.add((UIObject)vbEditor.getValEditor());
        }

        @Override
        public String getSearchString() {
            getEnvironment();
            return isBool ? vbEditor.getEditMask().toStr(env, vbEditor.getValue()) : vsEditor.getValue();
        }

        @Override
        public void setSearchString(String currentSearchString) {
            vsEditor.setValue(currentSearchString);
        }
        
        public void updateForSelelectorColumn(final SelectorColumnModelItem selectorColumn) {
            if (selectorColumn.getPropertyDef().getType() == EValType.BOOL && !isBool) {
                ((UIObject)vbEditor.getValEditor()).getHtml().setCss("display", "block");
                ((UIObject)vsEditor.getValEditor()).getHtml().setCss("display", "none");
                isBool = true;
            } else if (selectorColumn.getPropertyDef().getType() != EValType.BOOL && isBool) {
                ((UIObject)vbEditor.getValEditor()).getHtml().setCss("display", "none");
                ((UIObject)vsEditor.getValEditor()).getHtml().setCss("display", "block");
                isBool = false;
            }
        }
    }
    
    private final SearchWidget searchWidget;
    private final ValListEditorController<Long> cbColumn;
            
    public FindInSelectorDialog(IClientEnvironment environment, String configPrefix, boolean canReplace) {
        super(environment, configPrefix, canReplace);
        searchWidget = new SearchWidget(environment);
        setSearchWidget(searchWidget);
        cbColumn = new ValListEditorController<>(super.getEnvironment());
        cbColumn.setMandatory(true);
        cbColumn.addValueChangeListener(new ValueEditor.ValueChangeListener<Long>() {
            @Override
            public void onValueChanged(Long oldValue, Long newValue) {
                onColumnSelected(newValue);
            }
        });
        addSearchParameter(environment.getMessageProvider().translate("RwtSelector", "In Column:"), (IWidget) cbColumn.getValEditor());
    }
 
    
    private List<SelectorColumnModelItem> visibleColumns;
    
    public void setSelectorColumns(final List<SelectorColumnModelItem> visibleColumns, final int currentIdx) {
        this.visibleColumns = visibleColumns;
        cbColumn.getEditMask().clearItems();
        EditMaskList editMaskList = new EditMaskList();
        int visibleIndex = 0;
        for (SelectorColumnModelItem column : visibleColumns) {
            editMaskList.addItem(column.getTitle(), Long.valueOf(visibleIndex));
            visibleIndex++;
        }
        cbColumn.setEditMask(editMaskList);
        if (currentIdx > -1 && currentIdx < cbColumn.getEditMask().getItems().size()) {
            cbColumn.setValue((Long)editMaskList.getItems().get(currentIdx).getValue());
        }
    }
    
    private void onColumnSelected(final Long column) {
        if (column >= 0 && column < cbColumn.getEditMask().getItems().size()) {
            final Long indx = (Long) cbColumn.getEditMask().getItems().get(column.intValue()).getValue();
            searchWidget.updateForSelelectorColumn(visibleColumns.get(indx.intValue()));
        }
        searchWidget.setFocused(true);
    }
    
    public int getColumnIdx() {
        List<Item> itemList = cbColumn.getEditMask().getItems();
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).getValue().equals(cbColumn.getValue())) {
                return i;
            }
        }
        return -1;
    }  
    
}
