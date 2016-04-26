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

package org.radixware.kernel.explorer.widgets.selector;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QCompleter;
import com.trolltech.qt.gui.QFocusEvent;
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QStackedWidget;
import com.trolltech.qt.gui.QWidget;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.dialogs.IFindAndReplaceDialog;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;
import org.radixware.kernel.common.client.meta.mask.EditMaskNone;
import org.radixware.kernel.common.client.models.items.SelectorColumnModelItem;
import org.radixware.kernel.common.client.widgets.IPeriodicalTask;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.client.widgets.TimerEventHandler;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.dialogs.FindAndReplaceDialog;
import org.radixware.kernel.explorer.editors.valeditors.ValBoolEditor;
import org.radixware.kernel.explorer.env.ExplorerSettings;



public final class FindInSelectorDialog extends FindAndReplaceDialog {
    
    private static class SearchWidget extends QStackedWidget implements IFindAndReplaceDialog.ISearchWidget {

        final public Signal0 onSearchStringChanged = new Signal0();
        private final QLineEdit leEditor = new QLineEdit();
        private final Map<Id, QCompleter> completers = new HashMap<>(8);
        private final ValBoolEditor vbEditor;

        public SearchWidget(IClientEnvironment environment) {
            vbEditor = new ValBoolEditor(environment, null, new EditMaskNone(), false, false);
            setObjectName("searchWidget");
            vbEditor.setObjectName("vbditorInSearchWidget");
            leEditor.setObjectName("leEditorInSearchWidget");
            vbEditor.valueChanged.connect(onSearchStringChanged);
            leEditor.textChanged.connect(onSearchStringChanged);
            currentChanged.connect(onSearchStringChanged);
            addWidget(leEditor);
            addWidget(vbEditor);
        }

        @Override
        public String getSearchString() {
            return currentIndex() == 0 ? leEditor.text() : vbEditor.getEditMask().toStr(vbEditor.getEnvironment(), vbEditor.getValue());
        }

        @Override
        public void setSearchString(String currentSearchString) {
            leEditor.setText(currentSearchString);
        }

        @Override
        protected void focusInEvent(QFocusEvent event) {
            super.focusInEvent(event);
            focusNextChild();
        }

        private QCompleter getCompleterForEnum(final RadEnumPresentationDef enumDef) {
            QCompleter completer = completers.get(enumDef.getId());
            if (completer == null) {
                RadEnumPresentationDef.Items items = enumDef.getItems();
                final List<String> completitions = new ArrayList<String>(items.size());
                for (RadEnumPresentationDef.Item item : items) {
                    completitions.add(item.getTitle());
                }
                completer = new QCompleter(completitions, this);
                completer.setCompletionMode(QCompleter.CompletionMode.UnfilteredPopupCompletion);
                completer.setObjectName("completer for " + enumDef.getId().toString());
                completers.put(enumDef.getId(), completer);
            }
            return completer;
        }

        public void updateForSelelectorColumn(final SelectorColumnModelItem selectorColumn) {
            if (selectorColumn.getPropertyDef().getType() == EValType.BOOL) {
                setCurrentIndex(1);
            } else {
                setCurrentIndex(0);
                if (selectorColumn.getPropertyDef().getConstSet() != null) {
                    leEditor.setCompleter(getCompleterForEnum(selectorColumn.getPropertyDef().getConstSet()));
                } else {
                    leEditor.setCompleter(null);
                }
            }
        }
        
        @Override
        public boolean isDisposed() {
            return nativeId()==0;
        }

        @Override
        public String getObjectName() {
            return objectName();
        }

        @Override
        public IPeriodicalTask startTimer(TimerEventHandler handler) {
            throw new UnsupportedOperationException("startTimer is not supported here.");
        }

        @Override
        public void killTimer(IPeriodicalTask task) {
            throw new UnsupportedOperationException("killTimer is not supported here.");
        }                
    }
    
    private static class ComboBoxInternal extends QComboBox implements IWidget{
        
        public ComboBoxInternal(final QWidget parent){
            super(parent);
        }

        @Override
        public boolean isDisposed() {
            return nativeId()==0;
        }

        @Override
        public String getObjectName() {
            return objectName();
        }

        @Override
        public IPeriodicalTask startTimer(TimerEventHandler handler) {
            throw new UnsupportedOperationException("startTimer is not supported here.");
        }

        @Override
        public void killTimer(IPeriodicalTask task) {
            throw new UnsupportedOperationException("killTimer is not supported here.");
        }        
    }
            
    private final SearchWidget searchWidget;
    private final ComboBoxInternal cbColumn = new ComboBoxInternal(this);
    
    public FindInSelectorDialog(IClientEnvironment environment, final QWidget parent, final String configPrefix) {
        super(parent, (ExplorerSettings)environment.getConfigStore(), configPrefix, false);
        searchWidget = new SearchWidget(environment);
        setSearchWidget(searchWidget);
        searchWidget.onSearchStringChanged.connect(this, "refresh()");
        addSearchParameter(environment.getMessageProvider().translate("Selector", "In &Column:"), cbColumn);
        cbColumn.currentIndexChanged.connect(this, "onColumnSelected(int)");
    }
    private List<SelectorColumnModelItem> columns;

    public void setSelectorColumns(final List<SelectorColumnModelItem> columns, final int currentIdx) {
        this.columns = columns;
        cbColumn.clear();
        int absoluteIndex = 0;
        int visibleIndex = currentIdx;
        for (SelectorColumnModelItem column : columns) {
            if (column.isVisible()) {
                cbColumn.addItem(column.getTitle(), Integer.valueOf(absoluteIndex));
            } else if (absoluteIndex < currentIdx) {
                visibleIndex--;
            }
            absoluteIndex++;
        }
        if (visibleIndex > -1 && visibleIndex < cbColumn.count()) {
            cbColumn.setCurrentIndex(visibleIndex);
        }
    }

    @SuppressWarnings("unused")
    private void onColumnSelected(final int column) {
        if (column >= 0 && column < cbColumn.count()) {
            final Integer absoluteIndex = (Integer) cbColumn.itemData(column, Qt.ItemDataRole.UserRole);
            searchWidget.updateForSelelectorColumn(columns.get(absoluteIndex.intValue()));
        }
        searchWidget.setFocus();
    }

    public int getColumnIdx() {
        final int idx = cbColumn.currentIndex();
        if (idx > -1 && (cbColumn.itemData(idx) instanceof Integer)) {
            return ((Integer) cbColumn.itemData(idx)).intValue();
        } else {
            return -1;
        }
    }

    @Override
    public void done(int result) {
        columns = null;
        super.done(result);
    }
}
