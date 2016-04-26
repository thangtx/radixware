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

package org.radixware.kernel.explorer.dialogs;

import com.trolltech.qt.core.QAbstractItemModel;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.WidgetAttribute;
import com.trolltech.qt.gui.QAbstractButton;
import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QCursor;
import com.trolltech.qt.gui.QFocusEvent;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QHeaderView.ResizeMode;
import com.trolltech.qt.gui.QItemDelegate;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLayout;
import com.trolltech.qt.gui.QListWidget;
import com.trolltech.qt.gui.QListWidgetItem;
import com.trolltech.qt.gui.QSizePolicy.Policy;
import com.trolltech.qt.gui.QStyleOptionViewItem;
import com.trolltech.qt.gui.QTableWidget;
import com.trolltech.qt.gui.QTableWidgetItem;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.meta.RadClassPresentationDef;
import org.radixware.kernel.common.client.meta.RadPropertyDef;
import org.radixware.kernel.common.client.meta.RadSortingDef;
import org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem;
import org.radixware.kernel.common.client.meta.RadUserSorting;
import org.radixware.kernel.common.client.models.groupsettings.IGroupSetting;
import org.radixware.kernel.common.client.utils.ClientValueFormatter;
import org.radixware.kernel.common.client.views.ISortingEditorDialog;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.valeditors.ValStrEditor;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.widgets.CustomSplitter;


public final class SortingEditorDialog extends ExplorerDialog implements ISortingEditorDialog{
    
    private class ComboBoxDelegate extends QItemDelegate {
                
        @Override
        public QWidget createEditor(final QWidget parent, final QStyleOptionViewItem option, final QModelIndex index) {
            final QComboBox comboBox = new QComboBox(parent);
            comboBox.addItem(ascSign);
            comboBox.addItem(descSign);
            return comboBox;
        }

        @Override
        public void setEditorData(final QWidget editor, final QModelIndex index) {
            final Object data = index.data(Qt.ItemDataRole.UserRole);
                        
            if(editor instanceof QComboBox && data != null) {
                final RadSortingDef.SortingItem.SortOrder sortingItem = (RadSortingDef.SortingItem.SortOrder) data;
                ((QComboBox)editor).setCurrentIndex(sortingItem == SortingItem.SortOrder.DESC ? Table.DESC_INDEX : Table.ASC_INDEX);
            } else {
                super.setEditorData(editor, index);
            }
        }

        @Override
        public void setModelData(final QWidget editor, final QAbstractItemModel model, final QModelIndex index) {
            if(editor instanceof QComboBox) {
                final int currentIndex = ((QComboBox)editor).currentIndex();
            
                final RadSortingDef.SortingItem.SortOrder userData = 
                        currentIndex == 0
                        ? RadSortingDef.SortingItem.SortOrder.ASC
                        : RadSortingDef.SortingItem.SortOrder.DESC;
                final String displayData = (currentIndex == 0)
                        ? SortingEditorDialog.this.ascSign
                        : SortingEditorDialog.this.descSign;

                model.setData(index, userData, Qt.ItemDataRole.UserRole);
                model.setData(index, displayData, Qt.ItemDataRole.DisplayRole);
            } else {
                super.setModelData(editor, model, index);
            }
        }

        @Override
        public QSize sizeHint(final QStyleOptionViewItem option, final QModelIndex index) {
            return super.sizeHint(option, index);
        }
    }
    
    private class Table extends QTableWidget {
        public static final int COL_TITLE = 0;
        public static final int COL_ORDER = 1;
        @SuppressWarnings("unused")
        private static final int ASC_INDEX = 0;
        @SuppressWarnings("unused")
        private static final int DESC_INDEX = 1;
        private final boolean readOnly;
        public final Signal0 focusChanged = new Signal0();
                
        public Table(final IClientEnvironment environment, final QWidget parent, final boolean readOnly) {
            super(parent);
            setColumnCount(2);
            setFocusPolicy(Qt.FocusPolicy.ClickFocus);
            this.readOnly = readOnly;
            if(!this.readOnly) {
                final ComboBoxDelegate delegate = new ComboBoxDelegate();
                setItemDelegateForColumn(COL_ORDER, delegate);
            }
                        
            setHorizontalHeaderItem(COL_TITLE, new QTableWidgetItem(
                    environment.getMessageProvider().translate("SelectorAddons", "Column")
            ));
            setHorizontalHeaderItem(COL_ORDER, new QTableWidgetItem(
                    environment.getMessageProvider().translate("SelectorAddons", "Sorting Order")
            ));
            horizontalHeader().resizeSections(ResizeMode.ResizeToContents);
            horizontalHeader().setResizeMode(COL_TITLE, ResizeMode.Stretch);
            horizontalHeader().setResizeMode(COL_ORDER, ResizeMode.ResizeToContents);
            //setSelectionMode(SelectionMode.ExtendedSelection);
            setSelectionBehavior(SelectionBehavior.SelectRows);
        }

        @Override
        protected void focusOutEvent(final QFocusEvent event) {
            focusChanged.emit();
            super.focusOutEvent(event);
        }

        @Override
        protected void focusInEvent(final QFocusEvent event) {
            focusChanged.emit();
            super.focusInEvent(event);
        }
        
        /**
         * Adds a sorting item to dialog
         * @param sortingItem an item to add
         */       
        public void add(final RadSortingDef.SortingItem sortingItem) {
            final int row = rowCount();
            final RadClassPresentationDef classPresentation =
                    environment.getDefManager().getClassPresentationDef(SortingEditorDialog.this.sorting.getOwnerClassId());
            if(classPresentation.isPropertyDefExistsById(sortingItem.propId)) {
                setRowCount(row + 1);
            
                //column 0
                QTableWidgetItem item = new QTableWidgetItem();
                item.setFlags(Qt.ItemFlag.ItemIsSelectable, Qt.ItemFlag.ItemIsEnabled);
                final String capitalizedColTitle = ClientValueFormatter.capitalizeIfNecessary(environment, classPresentation.getPropertyDefById(sortingItem.propId).getTitle());
                item.setText(capitalizedColTitle);
                item.setData(Qt.ItemDataRole.UserRole, sortingItem.propId);
                setItem(row, COL_TITLE, item);
                //column 1
                final boolean isDesc = sortingItem.sortDesc;
                final String text = isDesc ? SortingEditorDialog.this.descSign : SortingEditorDialog.this.ascSign;
                item = new QTableWidgetItem();
                if(readOnly) { 
                    item.setFlags(Qt.ItemFlag.ItemIsSelectable, Qt.ItemFlag.ItemIsEnabled);
                }
                item.setText(text);
                item.setData(Qt.ItemDataRole.UserRole,
                        isDesc ? SortingItem.SortOrder.DESC : SortingItem.SortOrder.ASC);
                setItem(row, COL_ORDER, item);
            }
        }
        
        public RadSortingDef.SortingItem remove(final int index) {
            RadSortingDef.SortingItem result = null;
            if(index >= 0 && index < rowCount()) {
                final Id colId = (Id) item(index, COL_TITLE).data(Qt.ItemDataRole.UserRole);
                final boolean isDesc = item(index, COL_ORDER).text().equals(SortingEditorDialog.this.descSign);
                removeRow(index);
                result = new RadSortingDef.SortingItem(colId, isDesc);
            }
            return result;
        }
        
        /**
         * Moves current table row up
         */
        public void moveUp() {
            move(true);
        }
        
        /**
         * Moves current table row down
         */
        public void moveDown() {
            move(false);
        }
        
        /**
         * Common method to move table rows
         * @param moveUp TRUE to move up, FALSE to move down
         */
        private void move(final boolean moveUp) {
            final int currentRow = currentRow();
            final int destRow = moveUp ? currentRow - 1 : currentRow + 1;
            
            if(destRow >= 0 && destRow < rowCount()) {
                final List<QTableWidgetItem> src = takeRow(currentRow);
                final List<QTableWidgetItem> dst = takeRow(destRow);
                setRow(destRow, src);
                setRow(currentRow, dst);
                setCurrentCell(destRow, COL_TITLE);
            }
           
        }
                
        private List<QTableWidgetItem> takeRow(final int row) {
            final List<QTableWidgetItem> takenRow = new ArrayList<>();
            final int columnCount = columnCount();
            for(int i = 0; i < columnCount; i++) {
                takenRow.add(takeItem(row, i));
            }
            return takenRow;
        }
        
        private void setRow(final int row, final List<QTableWidgetItem> items) {
            final int columnCount = columnCount();
            for(int i = 0; i < columnCount; i++) {
                setItem(row, i, items.get(i));
            }
        }
        
        public boolean contains(final Id id) {
            boolean result = false;
            final int rowCount = rowCount();
            for(int i = 0; i < rowCount; i++) {
                final Id itemId = (Id) item(i, COL_TITLE).data(Qt.ItemDataRole.UserRole);
                if(id.equals(itemId)) {
                    result = true;
                    break;
                }
            }
            return result;
        }
        
        public List<SortingItem> getSortingColumns() {
            final List<SortingItem> columns = new LinkedList<>();
            final int rowCount = rowCount();
            for(int i = 0; i < rowCount; i++) {
                final Id id = (Id) item(i, COL_TITLE).data(Qt.ItemDataRole.UserRole);
                final SortingItem.SortOrder sortOrder = (SortingItem.SortOrder) item(i, COL_ORDER).data(Qt.ItemDataRole.UserRole);
                columns.add( new SortingItem(id, sortOrder) );
            }
            
            return columns;
        }
    }
    
    private class PropertyList extends QListWidget {
        public final Signal0 focusChanged = new Signal0();
        
        public PropertyList(final QWidget parent) {
            super(parent);
            setFocusPolicy(Qt.FocusPolicy.ClickFocus);
        }

        @Override
        protected void focusOutEvent(final QFocusEvent event) {
            focusChanged.emit();
            super.focusOutEvent(event);
        }

        @Override
        protected void focusInEvent(final QFocusEvent event) {
            focusChanged.emit();
            super.focusInEvent(event);
        }
        
        public void add(final RadPropertyDef prop) {
            if(prop != null) {
                final QListWidgetItem item = new QListWidgetItem(this);
                item.setText(prop.getTitle());
                item.setData(Qt.ItemDataRole.UserRole, prop);
                addItem(item);
            }
        }
        
        public RadPropertyDef remove(final int row) {
            final QListWidgetItem item = takeItem(row);
            if(item == null) {
                return null;
            } else {
                return (RadPropertyDef) item.data(Qt.ItemDataRole.UserRole);
            }
        }
        
    }
   
    private final boolean viewOnly;
    private final Collection<String> restrictedNames;
    private final IClientEnvironment environment;
    private ValStrEditor sortTitle;
    private final RadSortingDef sorting;
    private final String ascSign, descSign;
    private Table tableSelectedItems = null;         // selected items
    private PropertyList listAvailableItems = null;    // available items list
    
    //buttons
    private QToolButton listToTableBtn = null;
    private QToolButton tableToListBtn = null;
    private QToolButton tableMoveUp = null;
    private QToolButton tableMoveDown = null;
            
    public SortingEditorDialog(final IClientEnvironment environment, final RadSortingDef sorting, final Collection<String> restrictedNames, final boolean showApplyButton, final QWidget parent) {
        super(environment, parent, null);
        this.restrictedNames = restrictedNames;
        this.environment = environment;
        this.sorting = sorting;
        ascSign = environment.getMessageProvider().translate("SelectorAddons", "Ascending");
        descSign = environment.getMessageProvider().translate("SelectorAddons", "Descending");
        setAttribute(WidgetAttribute.WA_DeleteOnClose, true);
        setWindowIcon(ExplorerIcon.getQIcon(ClientIcon.Selector.FILTER_AND_SORTING));
        viewOnly = !(sorting instanceof RadUserSorting);
        setUpUi(showApplyButton);
        
        final String editorTitle = environment.getMessageProvider().translate("SelectorAddons", "Sorting");
        final String viewerTitle = environment.getMessageProvider().translate("SelectorAddons", "Sorting");
        setWindowTitle(viewOnly ? viewerTitle : editorTitle);
    }
    
    private void setUpUi(final boolean showApplyButton) {
        final QVBoxLayout mainLayout = dialogLayout();
        final QLabel titleLabel = new QLabel(
                environment.getMessageProvider().translate("SelectorAddons", "Sorting By:"), this);
        mainLayout.addWidget(titleLabel);
        sortTitle = new ValStrEditor(environment, this);
        sortTitle.setValue(sorting.getName());
        sortTitle.setReadOnly(viewOnly);
        mainLayout.addWidget(sortTitle);
        
        final CustomSplitter splitter = new CustomSplitter(this);
        splitter.setSizePolicy(Policy.Preferred, Policy.Expanding);
        final QVBoxLayout leftLayout = new QVBoxLayout();
        leftLayout.setMargin(0);
        leftLayout.addWidget(
                new QLabel(
                    environment.getMessageProvider().translate("SelectorAddons", "Available Columns:"),
                    this
                ));
        leftLayout.addLayout(buildLeftPanel());
        final QWidget leftPanel = new QWidget();
        leftPanel.setLayout(leftLayout);
        leftPanel.setVisible(!viewOnly);
        
        final QVBoxLayout rightLayout = new  QVBoxLayout();
        rightLayout.setMargin(0);
        rightLayout.addWidget(new QLabel(
                environment.getMessageProvider().translate("SelectorAddons", "Selected Columns:"),
                this));
        rightLayout.addLayout(buildRightPanel());
        final QWidget rightPanel = new QWidget();
        rightPanel.setLayout(rightLayout);
        
        splitter.addWidget(leftPanel);
        splitter.addWidget(rightPanel);
        mainLayout.addWidget(splitter);
        
        final CustomSplitter.CustomSplitterHandle handle = (CustomSplitter.CustomSplitterHandle) splitter.handle(1);
        buildSwapButtons(handle);
        ((QVBoxLayout)handle.layout()).addStretch();
        mainLayout.addLayout(buildButtons(showApplyButton));
        watchButtons();
    }
    
    private QLayout buildLeftPanel() {
        final QHBoxLayout layout = new QHBoxLayout();
        
        listAvailableItems = new PropertyList(this);
        listAvailableItems.setSortingEnabled(true);
        layout.addWidget(listAvailableItems);
        listAvailableItems.focusChanged.connect(this, "watchButtons()");
        listAvailableItems.doubleClicked.connect(this, "fromListToTable()");
        
        final RadClassPresentationDef classPresentation = environment.getDefManager().getClassPresentationDef(sorting.getOwnerClassId());
        final List<RadPropertyDef> properties = classPresentation.getProperties();
        for(RadPropertyDef i : properties) {
            if(i.canBeUsedInSorting() && i.hasTitle() && !isPropertyUsed(i)) {
                listAvailableItems.add(i);
            }
        }
        return layout;
    }
    
    private QLayout buildRightPanel() {
        final QHBoxLayout layout = new QHBoxLayout();
                
        tableSelectedItems = new Table(environment, this, viewOnly);
        tableSelectedItems.currentItemChanged.connect(this, "watchButtons()");
        tableSelectedItems.focusChanged.connect(this, "watchButtons()");
        
        final List<RadSortingDef.SortingItem> sortingColumns = sorting.getSortingColumns();
        for(SortingItem i : sortingColumns ) {
            if(i.propId == null) continue;
            tableSelectedItems.add(i);
        }
        
        layout.addWidget(tableSelectedItems);
        if(!viewOnly) {
            layout.addLayout(buildMoveButtons());
        }
        return layout;
    }
    
    private void buildSwapButtons(final CustomSplitter.CustomSplitterHandle handle) {
        final QCursor cursor = new QCursor(Qt.CursorShape.ArrowCursor);
        
        listToTableBtn = new QToolButton(this);
        listToTableBtn.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.RIGHT));
        listToTableBtn.clicked.connect(this, "fromListToTable()");
        listToTableBtn.setCursor(cursor);
        
        handle.addWidget(listToTableBtn);
        
        tableToListBtn = new QToolButton(this);
        tableToListBtn.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.LEFT));
        tableToListBtn.clicked.connect(this, "fromTableToList()");
        tableToListBtn.setCursor(cursor);
        handle.addWidget(tableToListBtn);
        
        handle.alignWigetsInside();
    }
    
    private QLayout buildMoveButtons() {
        final QVBoxLayout layout = new QVBoxLayout();
        layout.setMargin(0);
        layout.setAlignment(new Qt.Alignment(Qt.AlignmentFlag.AlignTop));
        
        tableMoveUp = new QToolButton(this);
        tableMoveUp.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.UP));
        tableMoveUp.clicked.connect(tableSelectedItems, "moveUp()");
        layout.addWidget(tableMoveUp);
        
        tableMoveDown = new QToolButton(this);
        tableMoveDown.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.DOWN));
        tableMoveDown.clicked.connect(tableSelectedItems, "moveDown()");
        layout.addWidget(tableMoveDown);
        return layout;
    }
    
    private QLayout buildButtons(final boolean showApplyButton) {
        final QHBoxLayout layout = new QHBoxLayout();
        layout.setAlignment(new Qt.Alignment(Qt.AlignmentFlag.AlignRight));
                
        if(viewOnly) {
            addButtons(EnumSet.of(EDialogButtonType.CLOSE),true);
        } else {
            final EnumSet<EDialogButtonType> buttons = EnumSet.of(EDialogButtonType.OK,EDialogButtonType.CANCEL);                
            ((QAbstractButton)addButtons(buttons, false).get(EDialogButtonType.OK)).clicked.connect(this,"accept()");            
            if (showApplyButton){
                final QAbstractButton btnApply = (QAbstractButton)addButton(EDialogButtonType.APPLY);                    
                btnApply.clicked.connect(this,"onApply()");               
                btnApply.setIcon(ExplorerIcon.getQIcon(ClientIcon.Definitions.SORTING));
            }
            rejectButtonClick.connect(this,"reject()");
        }
        return layout;
    }
    
    @SuppressWarnings("unused")
    private void onApply(){
        done(IGroupSetting.APPLY_SETTING_RESULT);
    }
    
    @SuppressWarnings("unused")
    private void fromListToTable() {
        final int currentRow = listAvailableItems.currentRow();
        final RadPropertyDef data = listAvailableItems.remove(currentRow);
        if(data != null && data.canBeUsedInSorting() && !tableSelectedItems.contains(data.getId())) {
            final RadSortingDef.SortingItem sortingItem = new SortingItem(data.getId(), SortingItem.SortOrder.ASC);
            tableSelectedItems.add(sortingItem);
        } else {
            listAvailableItems.add(data);
        }
        watchButtons();
    }
    
    @SuppressWarnings("unused")
    private void fromTableToList() {
        final int currentRow = tableSelectedItems.currentRow();
        if(currentRow >= 0) {
            //column 0
            final int rowCount = tableSelectedItems.rowCount();
            final SortingItem sortingItem = tableSelectedItems.remove(currentRow);
            if(sortingItem != null) {
                final Id id = sortingItem.propId;
                final RadPropertyDef property = environment
                        .getDefManager()
                        .getClassPresentationDef(sorting.getOwnerClassId())
                        .getPropertyDefById(id);
                listAvailableItems.add(property);
            }
            tableSelectedItems.selectRow(currentRow == rowCount - 1 ? currentRow - 1 : currentRow);
        }
        watchButtons();
    }
    
    private void watchButtons() {
        listToTableBtn.setDisabled(viewOnly || listAvailableItems.count() == 0 || !listAvailableItems.hasFocus());

        final int tableRowCount = tableSelectedItems.rowCount();
        final int tableCurrentRow = tableSelectedItems.currentRow();
        tableToListBtn.setDisabled(viewOnly || tableRowCount == 0 || !tableSelectedItems.hasFocus());
        if(!viewOnly) {
            tableMoveUp.setDisabled(tableRowCount <= 1
                    || tableCurrentRow <= 0
                    || !tableSelectedItems.hasFocus());
            tableMoveDown.setDisabled(tableRowCount <= 1
                    || tableCurrentRow < 0
                    || tableCurrentRow >= tableRowCount - 1
                    || !tableSelectedItems.hasFocus());
        }
    }
    
    private boolean isPropertyUsed(final RadPropertyDef prop) {
        boolean result = false;
        final List<SortingItem> sortColumns = sorting.getSortingColumns();
        for(SortingItem i : sortColumns) {
            if(i.propId.equals(prop.getId())) {
                result = true;
                break;
            }
        }
        return result;
    }
    
    private boolean isNameRestricted(final String name) {
        return restrictedNames.contains(name);
    }

    @Override
    public void done(final int result) {
        if (result!=QtDialog.DialogCode.Rejected.value() && !viewOnly){
            if (!writeChanges()){
                return;
            }
        }
        super.done(result);
    }
    
    @Override
    public DialogResult execDialog() {
        int result = exec();
        if (result == DialogCode.Accepted.value()){
            return DialogResult.ACCEPTED;
        }
        else if (result == IGroupSetting.APPLY_SETTING_RESULT) {
            return DialogResult.APPLY;
        }else{
            return DialogResult.REJECTED;
        }
    }     
    
    private boolean writeChanges(){
        final List<SortingItem> columns = tableSelectedItems.getSortingColumns();
        final String name = sortTitle.getValue();
        if(columns.isEmpty()) {
            final String errorMessage = environment.getMessageProvider().translate("SelectorAddons", "You must select at least one column to sort by.");
            environment.messageInformation(environment.getMessageProvider().translate("ExplorerDialog", "Information"), errorMessage);
            return false;
        } else if(isNameRestricted(name)) {
            final String errorMessage = environment.getMessageProvider().translate("SelectorAddons", "A sort named [%s] already exists.");
            environment.messageInformation(environment.getMessageProvider().translate("ExplorerDialog", "Information"),
                    String.format(errorMessage, name));
            return false;
        } else {
            sorting.setName(name);
            ((RadUserSorting)sorting).setSortingColumns(environment,columns);
            return true;
        }                    
    }        
}
