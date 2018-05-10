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

package org.radixware.kernel.explorer.editors;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.KeyboardModifier;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QEventFilter;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QHeaderView.ResizeMode;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QAbstractItemView;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QItemDelegate;
import com.trolltech.qt.gui.QShowEvent;
import com.trolltech.qt.gui.QTableWidget;
import com.trolltech.qt.gui.QTableWidgetItem;
import com.trolltech.qt.gui.QToolButton;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.types.QtUserData;
import org.radixware.kernel.explorer.utils.WidgetUtils;
import org.radixware.kernel.explorer.widgets.ExplorerWidget;


public abstract class AbstractArrayEditor extends ExplorerWidget {
    
    private final static class RemoveCurrentRowEvent extends QEvent{
        
        private final int rowIndex;
        
        public RemoveCurrentRowEvent(final int row){
            super(QEvent.Type.User);
            rowIndex = row;
        }
        
        public int getRowIndex(){
            return rowIndex;
        }
    }

    public static enum EItemMoveMode {

        NOT_MOVABLE, UP_DOWN, DRAG_DROP
    }
    private final static Qt.ItemFlags ITEM_FLAGS =
            new Qt.ItemFlags(Qt.ItemFlag.ItemIsEnabled, Qt.ItemFlag.ItemIsSelectable);
    private final Ui_ArrayEditor ui_creator = new Ui_ArrayEditor();
    protected final QAction addAction = new QAction(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.CREATE), Application.translate("ArrayEditor", "Insert New Item"), this);
    protected final QAction delAction = new QAction(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.DELETE), Application.translate("ArrayEditor", "Delete Current Item"), this);
    protected final QAction clearAction = new QAction(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.DELETE_ALL), Application.translate("ArrayEditor", "Delete All Items"), this);
    protected final QAction upAction = new QAction(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.UP), Application.translate("ArrayEditor", "Move Item Up"), this);
    protected final QAction downAction = new QAction(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.DOWN), Application.translate("ArrayEditor", "Move Item Down"), this);
    public transient final Signal2<Integer, Integer> cellDoubleClicked = new Signal2<>();    
    public final Signal2<Integer, Integer> rowsRemoved = new Signal2<>();
    public final Signal2<Integer,Integer> rowsInserted = new Signal2<>();
    public final Signal0 valueUndefined = new Signal0();
    public final Signal0 valueDefined = new Signal0();
    private final QEventFilter eventListener = new QEventFilter(this) {

        @Override
        public boolean eventFilter(final QObject source, final QEvent event) {
            if (event instanceof QKeyEvent 
                && event.type() == QEvent.Type.KeyPress 
                && source instanceof QTableWidget
                && !inEditState()) {
                final QTableWidget table = (QTableWidget) source;
                final QKeyEvent keyEvent = (QKeyEvent) event;                
                if (keyEvent.key() == Qt.Key.Key_Return.value() || keyEvent.key() == Qt.Key.Key_Enter.value()) {
                    if (keyEvent.modifiers().isSet(KeyboardModifier.ControlModifier)
                            && table.currentItem().flags().isSet(Qt.ItemFlag.ItemIsEditable) && table.rowCount() > 0) {
                        table.editItem(table.currentItem());
                        keyEvent.ignore();
                        return true;
                    }
                } else if (keyEvent.key() == Qt.Key.Key_Home.value()
                        && keyEvent.modifiers().value() == KeyboardModifier.NoModifier.value()
                        && table.rowCount() > 0) {
                    table.setCurrentCell(0, 0);
                } else if (keyEvent.key() == Qt.Key.Key_End.value()
                        && keyEvent.modifiers().value() == KeyboardModifier.NoModifier.value()
                        && table.rowCount() > 0) {
                    table.setCurrentCell(table.rowCount() - 1, 0);
                } else {
                    table.blockSignals(true);
                    try {
                        return onKeyEvent(keyEvent);
                    } finally {
                        table.blockSignals(false);
                    }
                }
            }
            return false;
        }
    };
    private boolean isMandatory;
    private Boolean isItemMandatory;
    private boolean isReadonly;
    private boolean createNewRows;
    private boolean isDuplicatesEnabled = true;
    private boolean isNullValue;
    private boolean itemPrototypeUpdated;
    private String noValueStr = null;
    private EItemMoveMode moveMode = EItemMoveMode.UP_DOWN;
    private int firstArrayItemIndex = 1;
    private int minArrayItemsCount = -1;
    private int maxArrayItemsCount = -1;
    private Map<QAction,QToolButton> customButtons;

    public AbstractArrayEditor(final IClientEnvironment environment, final QWidget parent) {
        super(environment, parent);
        setupUi();
        fillValuesTable(null);
        eventListener.setProcessableEventTypes(EnumSet.of(QEvent.Type.KeyPress));
        ui_creator.valuesTable.installEventFilter(eventListener);
        
    }

    public AbstractArrayEditor(final IClientEnvironment environment, 
                                            final QWidget parent, 
                                            final boolean readonly, 
                                            final boolean mandatory, 
                                            final boolean duplicates) {
        super(environment, parent);
        isMandatory = mandatory;
        isReadonly = readonly;
        isDuplicatesEnabled = duplicates;
        setupUi();
        fillValuesTable(null);
        eventListener.setProcessableEventTypes(EnumSet.of(QEvent.Type.KeyPress));
        ui_creator.valuesTable.installEventFilter(eventListener);
        
    }

    protected final void setDelegate(final QItemDelegate delegate) {
        ui_creator.valuesTable.setItemDelegate(delegate);
    }

    public final void setDuplicatesEnabled(final boolean flag) {
        isDuplicatesEnabled = flag;
    }

    public final boolean isDuplicatesEnabled() {
        return isDuplicatesEnabled;
    }

    public final void setReadonly(final boolean readonly) {
        finishEdit();
        isReadonly = readonly;
        updateItemPrototype();
        updateDefinedButtonState(!currentValueIsNull());
        if (!currentValueIsNull()){
            updateUi(true);
        }
    }

    public final boolean isReadonly() {
        return isReadonly;
    }

    public final void setMandatory(final boolean mandatory) {
        finishEdit();
        isMandatory = mandatory;
        updateDefinedButtonState(!currentValueIsNull());
        if (!currentValueIsNull()){
            updateUi(true);
        }
        updateItemPrototype();
    }

    public final boolean isMandatory() {
        return isMandatory;
    }
    
    public final boolean isItemMandatory(){
        return isItemMandatory == null ? isMandatory() : isItemMandatory.booleanValue();
    }

    protected final boolean isNullItemInadmissible() {
        final boolean predefinedMandatory = isItemMandatory == null ? isMandatory() : isItemMandatory.booleanValue();
        final boolean hasNullValue = getValues(-1).contains(null);
        return predefinedMandatory || (!isDuplicatesEnabled() && hasNullValue);
    }
   
    public void setItemMandatory(final boolean isMandatory) {
        isItemMandatory = Boolean.valueOf(isMandatory);
    }

    public final String getNoValueStr() {
        if (noValueStr != null) {
            return noValueStr;
        } else {
            return Application.translate("Value", "<not defined>");
        }
    }

    public final void setNoValueStr(final String newString) {
        noValueStr = newString;
        ui_creator.notDefinedLabel.setText(getNoValueStr());
        if (!currentValueIsNull()) {
            ui_creator.defineBtn.setToolTip(getNoValueStr());
        }
    }

    public final void setItemMoveMode(final EItemMoveMode moveMode) {
        ui_creator.upBtn.setVisible(moveMode != EItemMoveMode.NOT_MOVABLE);
        ui_creator.downBtn.setVisible(moveMode != EItemMoveMode.NOT_MOVABLE);
        this.moveMode = moveMode;
        updateItemPrototype();
        updateUi(!currentValueIsNull());
    }

    public final EItemMoveMode getItemMoveMode() {
        return moveMode;
    }

    public final boolean currentValueIsNull() {
        return isNullValue;
    }

    public final void setValue(final ArrayList value) {        
        finishEdit();
        if (!itemPrototypeUpdated){
            updateItemPrototype();
        }
        fillValuesTable(value);
    }

    public final ArrayList getValue() {
        if (currentValueIsNull()) {
            return null;
        }

        final ArrayList<Object> value = new ArrayList<>();
        finishEdit();
        for (int row = 0; row < ui_creator.valuesTable.rowCount(); ++row) {
            if (ui_creator.valuesTable.item(row, 0) != null) {
                value.add(getValueForItem(ui_creator.valuesTable.item(row, 0)));
            }
        }
        return value;
    }

    protected final List<Object> getValues(final int exceptItem) {
        final List<Object> values = new ArrayList<>();
        for (int row = 0; row < ui_creator.valuesTable.rowCount(); ++row) {
            if (row != exceptItem && ui_creator.valuesTable.item(row, 0) != null) {
                values.add(getValueForItem(ui_creator.valuesTable.item(row, 0)));
            }
        }
        return values;
    }

    private void setupUi() {
        ui_creator.setupUi(this);


        setLayout(WidgetUtils.createVBoxLayout(this));
        layout().addWidget(ui_creator.contentWidget);

        ui_creator.valuesTable.setColumnCount(1);
        ui_creator.valuesTable.horizontalHeader().setStretchLastSection(true);
        ui_creator.valuesTable.horizontalHeader().setVisible(false);
        ui_creator.valuesTable.setSelectionMode(QAbstractItemView.SelectionMode.SingleSelection);

        ui_creator.valuesTable.verticalHeader().setResizeMode(ResizeMode.Fixed);

        ui_creator.notDefinedLabel.setText(getNoValueStr());
        ui_creator.errorAreaLayout.setAlignment(new Qt.Alignment(Qt.AlignmentFlag.AlignLeft));
        ui_creator.errorMessage.setTextFormat(Qt.TextFormat.RichText);
        ui_creator.errorIcon.setPixmap(ExplorerIcon.getQIcon(ClientIcon.Message.WARNING).pixmap(new QSize(16, 16)));
        
        ui_creator.addBtn.setDefaultAction(addAction);
        ui_creator.delBtn.setDefaultAction(delAction);
        ui_creator.clearBtn.setDefaultAction(clearAction);
        ui_creator.upBtn.setDefaultAction(upAction);
        ui_creator.downBtn.setDefaultAction(downAction);

        ui_creator.defineBtn.clicked.connect(this, "onDefinedClick()");

        clearAction.triggered.connect(this, "clearValuesTable()");
        clearAction.setShortcut("Ctrl+Delete");

        delAction.triggered.connect(this, "deleteItem()");
        delAction.setShortcut("Delete");

        addAction.triggered.connect(this, "onCreate()");
        addAction.setShortcut("Insert");

        upAction.triggered.connect(this, "moveUp()");
        upAction.setShortcut("Ctrl+Up");

        downAction.triggered.connect(this, "moveDown()");
        downAction.setShortcut("Ctrl+Down");

        ui_creator.valuesTable.setTabKeyNavigation(false);        
        ui_creator.valuesTable.itemChanged.connect(this, "onDataChanged(QTableWidgetItem)");
        ui_creator.valuesTable.currentItemChanged.connect(this, "onCurrentItemChanged(QTableWidgetItem,QTableWidgetItem)");
        ui_creator.valuesTable.cellDoubleClicked.connect(cellDoubleClicked);
        ui_creator.valuesTable.model().rowsInserted.connect(this, "rowsInserted(QModelIndex,int,int)");
        ui_creator.valuesTable.setSelectionMode(QAbstractItemView.SelectionMode.SingleSelection);
        ui_creator.valuesTable.setSelectionBehavior(QAbstractItemView.SelectionBehavior.SelectItems);

        ui_creator.valuesTable.setDragEnabled(true);
        ui_creator.valuesTable.setDropIndicatorShown(true);
        ui_creator.valuesTable.viewport().setAcceptDrops(true);
        ui_creator.valuesTable.setAcceptDrops(true);
        ui_creator.valuesTable.setDragDropMode(QAbstractItemView.DragDropMode.DragDrop);        
        setReadonly(false);
    }

    private void fillValuesTable(final ArrayList array) {
        ui_creator.valuesTable.setRowCount(0);
        if (array != null) {
            QTableWidgetItem item;
            for (int i = 0; i < array.size(); i++) {
                item = createItemForValue(array.get(i));
                if (item != null) {
                    addItem(item);
                }
            }
        }
        updateDefinedButtonState(array != null);
        updateUi(array != null);
        if (ui_creator.valuesTable.rowCount() > 0) {
            ui_creator.valuesTable.setCurrentCell(0, 0);
        }
    }

    private void updateUi(boolean defined) {
        
        if (defined) {
            ui_creator.stackedWidget.setCurrentIndex(0);
            final QTableWidgetItem currentItem = ui_creator.valuesTable.currentItem();
            final int lastRow = ui_creator.valuesTable.rowCount() - 1;
            
            upAction.setDisabled(moveMode == EItemMoveMode.NOT_MOVABLE || isReadonly || currentItem == null || currentItem.row() == 0);
            downAction.setDisabled(moveMode == EItemMoveMode.NOT_MOVABLE || isReadonly || currentItem == null || currentItem.row() == lastRow);
            setDragDropEnabled(moveMode == EItemMoveMode.DRAG_DROP && !isReadonly);
        } else {
            final String msg = Application.translate("ArrayEditor", "Do you really want to delete array?");
            if (ui_creator.valuesTable.rowCount() > 0 
                && !Application.messageConfirmation(Application.translate("ArrayEditor", "Confirm To Delete Array"), msg)) {
                updateDefinedButtonState(true);
                return;
            }
            clearValuesTableImpl();
            ui_creator.stackedWidget.setCurrentIndex(1);
            upAction.setDisabled(true);
            downAction.setDisabled(true);
            setDragDropEnabled(false);
        }
        clearAction.setDisabled(!defined || ui_creator.valuesTable.rowCount() == 0 || isReadonly);
        final boolean moreOrEqThanMax = maxArrayItemsCount >= 0 && length() >= maxArrayItemsCount;
        final boolean lessOrEqThanMin = minArrayItemsCount > 0 && length() <= minArrayItemsCount;
        delAction.setDisabled(!defined || ui_creator.valuesTable.rowCount() == 0 || isReadonly || lessOrEqThanMin);
        addAction.setDisabled(isReadonly || moreOrEqThanMax);
        
        updateErrorMessage();
        
        ui_creator.valuesTable.setVerticalHeaderLabels(createTableNumeration());
        afterRefreshUi();
    }

    @Override
    protected void showEvent(final QShowEvent event) {
        super.showEvent(event);
        if (!itemPrototypeUpdated){
            updateItemPrototype();
        }
    }    
    
    private void updateErrorMessage() {
        boolean lengthIsInvalid = false;
        ui_creator.errorMessage.clear();
        final String max = getEnvironment().getMessageProvider().translate("ArrayEditor", "Maximum items count is %d");
        final String min = getEnvironment().getMessageProvider().translate("ArrayEditor", "Minimum items count is %d");
        StringBuilder sb = new StringBuilder();
        if(maxArrayItemsCount >= 0 && length() > maxArrayItemsCount) {
            sb.append("<font color='red'>");
            sb.append(String.format(max, getMaxArrayItemsCount()));
            sb.append("</font>");
            lengthIsInvalid = true;
        }
        if(minArrayItemsCount >= 0 && length() < minArrayItemsCount) {
            sb.append(System.getProperty("line.separator"));
            sb.append("<font color='red'>");
            sb.append(String.format(min, getMinArrayItemsCount()));
            sb.append("</font>");
            lengthIsInvalid = true;
        }
        
        if(lengthIsInvalid) {
            ui_creator.errorMessage.setText(sb.toString().trim());
        }
        ui_creator.errorArea.setVisible(lengthIsInvalid);
    }
    
    private List<String> createTableNumeration() {
        final int last = ui_creator.valuesTable.rowCount();
        final List<String> labels = new LinkedList<>();
        final int baseIndex = getFirstArrayItemIndex();
        for(int i = 0; i < last; i++) {
            labels.add(String.valueOf(i + baseIndex));
        }
        
        return labels;
    }

    private void updateDefinedButtonState(final boolean defined) {
        if (defined) {
            ui_creator.defineBtn.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.CLEAR));
            ui_creator.defineBtn.setToolTip(Application.translate("Value", "Clear Value"));
            ui_creator.defineBtn.setEnabled(!isMandatory() && !isReadonly);
        } else {
            ui_creator.defineBtn.setIcon(ExplorerIcon.getQIcon(ClientIcon.ValueModification.DEFINE));
            ui_creator.defineBtn.setToolTip(Application.translate("ArrayEditor", "Define Value"));
            ui_creator.defineBtn.setEnabled(!isReadonly);
        }
        isNullValue = !defined;
    }

    protected void afterRefreshUi() {
    }

    public final void setOperationsVisible(final boolean isVisible) {
        ui_creator.buttonsWidged.setVisible(isVisible);
    }

    private void setDragDropEnabled(final boolean isEnabled) {
        ui_creator.valuesTable.setDragEnabled(isEnabled);
        ui_creator.valuesTable.setDragDropMode(isEnabled ? QAbstractItemView.DragDropMode.DragDrop : QAbstractItemView.DragDropMode.NoDragDrop);
        ui_creator.valuesTable.setAcceptDrops(isEnabled);
    }

    private boolean clearValuesTable() {
        finishEdit();
        final String msg = Application.translate("ArrayEditor", "Do you really want to delete all array items?");
        if (ui_creator.valuesTable.rowCount() > 0 
            && !Application.messageConfirmation(Application.translate("ArrayEditor", "Confirm To Clear Array"), msg)) {
            return false;
        }
        clearValuesTableImpl();
        return true;
    }
    
    private void clearValuesTableImpl(){
        final int count = ui_creator.valuesTable.rowCount();
        ui_creator.valuesTable.setRowCount(0);
        clearAction.setDisabled(true);
        delAction.setDisabled(true);        
        rowsRemoved.emit(0, count);
        updateErrorMessage();        
    }

    @SuppressWarnings("unused")
    private void onCreate() {
        final List<QTableWidgetItem> newItems = createNewItems();
        insertNewItems(newItems);        
    }

    @SuppressWarnings("unused")
    private void deleteItem() {
        finishEdit();
        final int row = ui_creator.valuesTable.currentRow();
        ui_creator.valuesTable.removeRow(row);
        updateUi(true);
        if (ui_creator.valuesTable.rowCount() > row) {
            ui_creator.valuesTable.setCurrentCell(row, 0);
        } else if (ui_creator.valuesTable.rowCount() > 0) {
            ui_creator.valuesTable.setCurrentCell(ui_creator.valuesTable.rowCount() - 1, 0);
        }
        rowsRemoved.emit(row,1);
        updateErrorMessage();
    }

    @SuppressWarnings("unused")
    private void moveUp() {
        if (upAction.isEnabled() && ui_creator.valuesTable.currentItem() != null) {
            finishEdit();
            final int currentRow = ui_creator.valuesTable.currentItem().row();
            swapItems(currentRow, currentRow - 1);
            ui_creator.valuesTable.setCurrentIndex(ui_creator.valuesTable.model().index(currentRow - 1, 0));
        }
    }

    @SuppressWarnings("unused")
    private void moveDown() {
        if (downAction.isEnabled() && ui_creator.valuesTable.currentItem() != null) {
            finishEdit();
            final int currentRow = ui_creator.valuesTable.currentItem().row();
            swapItems(currentRow, currentRow + 1);
            ui_creator.valuesTable.setCurrentIndex(ui_creator.valuesTable.model().index(currentRow + 1, 0));
        }
    }

    @SuppressWarnings("unused")
    private void onDefinedClick() {
        isNullValue = !isNullValue;        
        updateUi(!currentValueIsNull());
        updateDefinedButtonState(!currentValueIsNull());
        if (isNullValue){
            valueUndefined.emit();
        }else{
            valueDefined.emit();
        }
    }

    protected final QTableWidgetItem getItem(final int row) {
        return ui_creator.valuesTable.item(row, 0);
    }

    protected final QTableWidgetItem getCurrentItem() {
        return ui_creator.valuesTable.currentItem();
    }

    protected QTableWidgetItem createItemForValue(final Object value) {
        final QTableWidgetItem item = new QTableWidgetItem();
        item.setText(String.valueOf(value));
        item.setData(Qt.ItemDataRole.UserRole, value);
        item.setFlags(getItemFlags());
        return item;
    }
    
    protected Qt.ItemFlags getItemFlags() {
        final Qt.ItemFlags flags = new Qt.ItemFlags();
        flags.set(ITEM_FLAGS);
        if (!isReadonly() && getItemMoveMode() == EItemMoveMode.DRAG_DROP) {
            flags.set(Qt.ItemFlag.ItemIsDragEnabled);
        }
        return flags;
    }

    protected final void updateItemPrototype() {
        final QTableWidgetItem itemPrototype = new QTableWidgetItem();
        itemPrototype.setFlags(getItemFlags());
        ui_creator.valuesTable.setItemPrototype(itemPrototype);
        itemPrototypeUpdated = true;
    }

    protected Object getValueForItem(QTableWidgetItem item) {
        final Object value = item.data(Qt.ItemDataRole.UserRole);
        return (value instanceof QtUserData) ? ((QtUserData) value).get() : value;
    }

    public final Object getSelectedValue() {
        final QTableWidgetItem item = getCurrentItem();
        return item != null ? getValueForItem(item) : null;
    }

    public final boolean isEmpty() {
        return ui_creator.valuesTable.rowCount() == 0;
    }

    protected boolean updateValueForItem(final QTableWidgetItem item, final Object newValue) {
        if (newValue != null && !isDuplicatesEnabled()) {
            final Object actualValue = newValue instanceof QtUserData ? ((QtUserData) newValue).get() : newValue;
            if (getValues(item.row()).contains(actualValue)) {
                Application.beep();
                return false;
            }
        }
        item.setData(Qt.ItemDataRole.UserRole, newValue);
        return true;
    }

    private void addItem(final QTableWidgetItem item) {
        final int row = ui_creator.valuesTable.rowCount();
        ui_creator.valuesTable.insertRow(row);
        ui_creator.valuesTable.blockSignals(true);
        try {
            ui_creator.valuesTable.setItem(row, 0, item);
        } finally {
            ui_creator.valuesTable.blockSignals(false);
        }
    }

    protected final void swapItems(final int i, final int j) {
        final int rowCount = ui_creator.valuesTable.rowCount();
        if (i < 0 || i >= rowCount || j < 0 || j >= rowCount) {
            throw new IndexOutOfBoundsException();
        }
        if (i == j) {
            return;
        }
        final QTableWidgetItem itemI = ui_creator.valuesTable.takeItem(i, 0),
                itemJ = ui_creator.valuesTable.takeItem(j, 0);
        ui_creator.valuesTable.setItem(i, 0, itemJ);
        ui_creator.valuesTable.setItem(j, 0, itemI);
    }

    protected abstract List<QTableWidgetItem> createNewItems();

    protected final void insertNewItems(final List<QTableWidgetItem> items) {
        if (items != null && !items.isEmpty()) {
            final int startRow = ui_creator.valuesTable.rowCount(); 
            createNewRows = true;
            try {
                for (QTableWidgetItem item : items) {
                    addItem(item);
                }
            } finally {
                createNewRows = false;
            }
            final int lastRow = ui_creator.valuesTable.rowCount() - 1;
            final QTableWidgetItem lastItem = items.get(items.size() - 1);
            updateDefinedButtonState(true);
            updateUi(true);            
            ui_creator.valuesTable.setCurrentCell(lastRow, 0);
            updateErrorMessage();
            rowsInserted.emit(startRow,items.size());
            if (lastItem.flags().isSet(Qt.ItemFlag.ItemIsEditable)) {
                ui_creator.valuesTable.edit(ui_creator.valuesTable.model().index(lastRow, 0));
            }
        }
    }

    protected void onCurrentItemChanged(final QTableWidgetItem item1, final QTableWidgetItem item2) {
        updateUi(true);
    }

    protected void onDataChanged(QTableWidgetItem item) {
    }

    @SuppressWarnings("unused")
    private void rowsInserted(final QModelIndex index, int first, int count) {
        if (!createNewRows && getCurrentItem() != null) {
            final int current = getCurrentItem().row();
            ui_creator.valuesTable.setCurrentIndex(ui_creator.valuesTable.model().index(first, 0));
            QApplication.postEvent(this, new RemoveCurrentRowEvent(current));
        }
    }

    public boolean checkForDuplicates() {
        if (!isDuplicatesEnabled()) {
            final ArrayList currentValue = getValue();
            if (currentValue != null) {
                final int size = currentValue.size();
                for (int i = 0; i < size; i++) {
                    for (int j = i + 1; j < size; j++) {
                        if (Utils.equals(currentValue.get(i), currentValue.get(j))) {
                            final MessageProvider messageProvider = getEnvironment().getMessageProvider();
                            final String title = messageProvider.translate("ExplorerMessage", "Duplicate Values Are Not Allowed");
                            final String message = messageProvider.translate("ExplorerMessage", "Duplicate values detected for %s and %s items");
                            final int offset = getFirstArrayItemIndex();
                            getEnvironment().messageError(title, String.format(message, i + offset, j + offset));
                            ui_creator.valuesTable.setCurrentCell(i, 0);
                            ui_creator.valuesTable.editItem(ui_creator.valuesTable.currentItem());
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    @SuppressWarnings("PMD.CompareObjectsWithEquals")
    protected QToolButton getToolButtonForAction(final QAction action) {
        if (action == addAction) {
            return ui_creator.addBtn;
        } else if (action == delAction) {
            return ui_creator.delBtn;
        } else if (action == clearAction) {
            return ui_creator.clearBtn;
        } else if (action == upAction) {
            return ui_creator.upBtn;
        } else if (action == downAction) {
            return ui_creator.downBtn;
        } else {
            return customButtons==null ? null : customButtons.get(action);
        }
    }
    
    protected void insertCustomAction(final QAction action){
        if (customButtons==null || !customButtons.containsKey(action)){
            final QToolButton toolButton = new QToolButton(ui_creator.buttonsWidged);
            final String actionName = action.objectName();
            if (actionName!=null && !actionName.isEmpty()){
                toolButton.setObjectName("custom_btn_for "+actionName);
            }
            toolButton.setFocusPolicy(Qt.FocusPolicy.TabFocus);        
            final int index = ui_creator.vboxLayout3.count() - 4;//devider, upButton, downButton, verticalSpacer
            ui_creator.vboxLayout3.insertWidget(index, toolButton);
            action.setParent(this);
            toolButton.setDefaultAction(action);
            if (customButtons==null){
                customButtons = new HashMap<>();
            }
            customButtons.put(action, toolButton);
        }
    }
    
    protected void removeCustomAction(final QAction action){
        final QToolButton button = getToolButtonForAction(action);
        if (button!=null){
            ui_creator.vboxLayout3.removeWidget(button);
            customButtons.remove(action);
            if (customButtons.isEmpty()){
                customButtons = null;
            }
        }        
    }
    
    protected int getCurrentRow(){
        return ui_creator.valuesTable.currentRow();
    }
    
    protected void setCurrentRow(final int row){
        if (row>-1 && row<ui_creator.valuesTable.rowCount()){
            ui_creator.valuesTable.setCurrentCell(row, 0);
        }
    }
    
    
    protected final void focusTable(){
        ui_creator.valuesTable.setFocus();
    }

    protected void insertToolButton(final QToolButton button, final int index) {
        ui_creator.vboxLayout3.insertWidget(index, button);
    }    

    protected boolean onKeyEvent(final QKeyEvent keyEvent) {
        return false;
    }

    protected void finishEdit() {
    }

    protected final void closeEditor() {
        final int currrentRow = ui_creator.valuesTable.currentRow();                
        ui_creator.valuesTable.setCurrentItem(null);        
        ui_creator.valuesTable.setCurrentCell(currrentRow, 0);        
    }
    
    protected final void openEditor(final int row){
        final QTableWidgetItem item = ui_creator.valuesTable.item(row, 0);
        if (item!=null){
            ui_creator.valuesTable.setCurrentItem(item);
            ui_creator.valuesTable.editItem(item);
        }
    }    

    protected boolean inEditState() {
        return false;
    }
    
    /**
     * Returns a number which the table's row numeration starts with
     * @return 
     */
    public int getFirstArrayItemIndex() {
        return firstArrayItemIndex;
    }
    
    /**
     * Changes the values table's row numeration starting from <code>index</code>
     * @param index The new starting index of table's rows numeration.
     */
    public void setFirstArrayItemIndex(final int index) {
        this.firstArrayItemIndex = index;
        updateUi(true);
    }

    /**
     * @return the minArrayItemsCount
     */
    public int getMinArrayItemsCount() {
        return minArrayItemsCount;
    }

    /**
     * Sets minimal number of array's items.
     * @param minArrayItemsCount minimal number of array's items. If <code>minArrayItemsCount &lt;= 0</code>, the number is unlimited.
     */
    public void setMinArrayItemsCount(int minArrayItemsCount) {
        this.minArrayItemsCount = minArrayItemsCount;
    }

    /**
     * @return the maxArrayItemsCount
     */
    public int getMaxArrayItemsCount() {
        return maxArrayItemsCount;
    }
     
    /**
     * 
     * @param maxArrayItemsCount 
     */
    public void setMaxArrayItemsCount(final int maxArrayItemsCount) {
        this.maxArrayItemsCount = maxArrayItemsCount;
    }
    
    public int length() {
        return ui_creator.valuesTable.rowCount();
    }

    @Override
    protected void closeEvent(final QCloseEvent event) {
        if (customButtons!=null){
            customButtons.clear();
        }
        super.closeEvent(event);
    }
    
    

    @Override
    protected void customEvent(final QEvent qevent) {
        if (qevent instanceof RemoveCurrentRowEvent){
            qevent.accept();
            ui_creator.valuesTable.removeRow(((RemoveCurrentRowEvent)qevent).getRowIndex());
            updateUi(true);
            return;
        }
        super.customEvent(qevent);
    }      
}
