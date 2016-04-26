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

import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.Alignment;
import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.gui.QAbstractItemView;
import com.trolltech.qt.gui.QDragEnterEvent;
import com.trolltech.qt.gui.QDropEvent;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLayout;
import com.trolltech.qt.gui.QListWidget;
import com.trolltech.qt.gui.QListWidgetItem;
import com.trolltech.qt.gui.QStackedWidget;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef.Items;
import org.radixware.kernel.common.client.views.ArrayEditorEventListener;
import org.radixware.kernel.common.enums.EEditMaskEnumOrder;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;


public final class EnumItemsEditor extends QWidget {
    
    public static final class NoValueLabel extends QLabel{
        
        public final Signal0 dropItems = new Signal0();
        
        public NoValueLabel(final String label){
            super(label);
            setAcceptDrops(true);
        }

        @Override
        protected void dragEnterEvent(final QDragEnterEvent event) {
            if (event.source() instanceof EnumItemsListWidget){
                event.acceptProposedAction();
            }else{
                super.dragEnterEvent(event);
            }
        }                

        @Override
        protected void dropEvent(final QDropEvent event) {
            if (event.source() instanceof EnumItemsListWidget){
                event.acceptProposedAction();
                dropItems.emit();
            }else{
                super.dropEvent(event);
            }
            
        }
    }

    public final Signal1<Boolean> valueStateChanged = new Signal1<>();
            
    private final EnumItemsListWidget sourceList;
    private final EnumItemsListWidget destList = new EnumItemsListWidget(this, null);
    private final QLabel lbAvailableItems = new QLabel(this);
    private final QLabel lbSelectedItems = new QLabel(this);
    private final Items items;
    private QToolButton btnAdd;
    private QToolButton btnMoveBack;
    private QToolButton btnAddAll;
    private QToolButton btnMoveBackAll;
    private QToolButton btnMoveUp;
    private QToolButton btnMoveDown;
    private QToolButton btnDefine;
    private QToolButton btnDeleteAll;
    private final QStackedWidget labelWidget = new QStackedWidget(this);
    private final QLabel errorMessage;
    private final NoValueLabel noValueLabel;
    private int minArrayItemsCount = -1;
    private int maxArrayItemsCount = -1;
    private boolean isNullValue = true;
    private boolean isMoveOperationsEnabled = true;
    private boolean isMandatory = false;
    private boolean isReadonly = false;
    private boolean isOperationsVisible = true;
    private String noValueStr = null;
    private boolean isCurrentValueValid = true;
    private final String minMess, maxMess;
    
    private List<ArrayEditorEventListener> listeners;

    @SuppressWarnings("LeakingThisInConstructor")
    public EnumItemsEditor(final IClientEnvironment environment, final QWidget parent, final Items items) {
        super(parent);
        this.items = items;
        final QHBoxLayout mainLayout = new QHBoxLayout();
        final QLayout moversLayout = buildMoveButtons(environment.getMessageProvider());
        final QLayout sortersLayout = buildSortButtons(environment.getMessageProvider());
        sourceList = new EnumItemsListWidget(this, items);
        sourceList.setSortingOrder(EEditMaskEnumOrder.BY_VALUE);
        sourceList.setMoveOperationEnabled(false);
        sourceList.ctrlRightPressed.connect(this, "onAdd()");        
        sourceList.doubleClicked.connect(this, "onAdd()", Qt.ConnectionType.QueuedConnection);
        sourceList.itemSelectionChanged.connect(this,"updateButtons()");
        sourceList.afterDrop.connect(this,"afterDropInSource(java.util.List, org.radixware.kernel.explorer.editors.EnumItemsListWidget$SelectionPart)");
        destList.deletePressed.connect(this, "onRemove()");
        destList.ctrlLeftPressed.connect(this, "onRemove()");
        destList.doubleClicked.connect(this, "onDestListDoubleClicked()");
        destList.itemSelectionChanged.connect(this,"updateButtons()");
        destList.afterDrop.connect(this,"afterDropInDest(java.util.List, org.radixware.kernel.explorer.editors.EnumItemsListWidget$SelectionPart)");

        final Alignment topLeft = new Alignment(AlignmentFlag.AlignLeft, AlignmentFlag.AlignTop);
        final QVBoxLayout sourceListWithLabel = new QVBoxLayout();
        sourceListWithLabel.setMargin(0);
        sourceListWithLabel.setAlignment(topLeft);
        String listLabel = environment.getMessageProvider().translate("ArrayEditor", "&Available Items:");
        lbAvailableItems.setText(listLabel);        
        lbAvailableItems.setBuddy(sourceList);
        sourceListWithLabel.addWidget(lbAvailableItems);
        sourceListWithLabel.addWidget(sourceList);

        final QGridLayout destListWithLabel = new QGridLayout();
        destListWithLabel.setMargin(0);
        destListWithLabel.setAlignment(topLeft);
        listLabel = environment.getMessageProvider().translate("ArrayEditor", "&Selected Items:");
        lbSelectedItems.setText(listLabel);
        lbSelectedItems.setBuddy(destList);
        destListWithLabel.addWidget(lbSelectedItems, 0, 0);
        destListWithLabel.addWidget(labelWidget, 1, 0);
        labelWidget.addWidget(destList);
        //destListWithLabel.addWidget(destList, 1, 0);
        destListWithLabel.addLayout(sortersLayout, 1, 1);
        sortersLayout.setAlignment(topLeft);
        destList.currentRowChanged.connect(this, "updateButtons()");
        maxMess = environment.getMessageProvider().translate("ArrayEditor", "Maximum items count is %d");
        minMess = environment.getMessageProvider().translate("ArrayEditor", "Minimum items count is %d");
        mainLayout.addLayout(sourceListWithLabel);
        mainLayout.addLayout(moversLayout);
        mainLayout.addLayout(destListWithLabel);

        final QGridLayout errorArea = new QGridLayout();
        errorArea.setObjectName("errorArea");
        destListWithLabel.addLayout(errorArea, 2, 0);
        errorMessage = new QLabel();
        errorMessage.setObjectName("errorMessage");
        errorArea.addWidget(errorMessage);
        errorMessage.setVisible(false);
        this.setLayout(mainLayout);

        noValueStr = environment.getMessageProvider().translate("Value", "<not defined>");
        noValueLabel = new NoValueLabel(noValueStr);
        noValueLabel.dropItems.connect(this,"onDropItems()");
        labelWidget.addWidget(noValueLabel);
        final QFont font = new QFont();
        font.setPointSize(12);
        font.setBold(true);
        font.setWeight(75);
        noValueLabel.setFont(font);
        noValueLabel.setFrameShape(com.trolltech.qt.gui.QFrame.Shape.StyledPanel);
        noValueLabel.setFrameShadow(com.trolltech.qt.gui.QFrame.Shadow.Sunken);
        noValueLabel.setAlignment(com.trolltech.qt.core.Qt.AlignmentFlag.createQFlags(com.trolltech.qt.core.Qt.AlignmentFlag.AlignCenter));
        noValueLabel.setVisible(false);
        labelWidget.setCurrentWidget(destList);
        updateUi(false);
    }

    private QLayout buildMoveButtons(final MessageProvider msgProvider) {
        final QVBoxLayout layout = new QVBoxLayout();
        layout.setAlignment(new Alignment(AlignmentFlag.AlignVCenter));

        btnAdd = new QToolButton(this);
        btnAdd.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.RIGHT));
        btnAdd.clicked.connect(this, "onAdd()");
        String toolTip = msgProvider.translate("ArrayEditor", "Select Item(s)");
        btnAdd.setToolTip(toolTip);

        btnMoveBack = new QToolButton(this);
        btnMoveBack.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.LEFT));
        btnMoveBack.clicked.connect(this, "onRemove()");
        toolTip = msgProvider.translate("ArrayEditor", "Unselect Item(s)");
        btnMoveBack.setToolTip(toolTip);

        btnAddAll = new QToolButton(this);
        btnAddAll.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.FORCE_RIGHT));
        btnAddAll.clicked.connect(this, "onAddAll()");
        toolTip = msgProvider.translate("ArrayEditor", "Select All Items");
        btnAddAll.setToolTip(toolTip);

        btnMoveBackAll = new QToolButton(this);
        btnMoveBackAll.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.FORCE_LEFT));
        btnMoveBackAll.clicked.connect(this, "onRemoveAll()");
        toolTip = msgProvider.translate("ArrayEditor", "Unselect All Items");
        btnMoveBackAll.setToolTip(toolTip);

        layout.addWidget(btnAdd);
        layout.addWidget(btnMoveBack);
        layout.addWidget(btnAddAll);
        layout.addWidget(btnMoveBackAll);

        return layout;
    }

    private QLayout buildSortButtons(final MessageProvider msgProvider) {
        final QVBoxLayout layout = new QVBoxLayout();
        layout.setAlignment(new Alignment(AlignmentFlag.AlignTop));

        btnMoveUp = new QToolButton(this);
        btnMoveUp.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.UP));
        btnMoveUp.clicked.connect(this, "onMoveUp()");
        String toolTip = msgProvider.translate("ArrayEditor", "Move Item Up");
        btnMoveUp.setToolTip(toolTip);

        btnMoveDown = new QToolButton(this);
        btnMoveDown.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.DOWN));
        btnMoveDown.clicked.connect(this, "onMoveDown()");
        toolTip = msgProvider.translate("ArrayEditor", "Move Item Down");
        btnMoveDown.setToolTip(toolTip);

        layout.addWidget(btnMoveUp);
        layout.addWidget(btnMoveDown);

        layout.addSpacing(20);

        btnDeleteAll = new QToolButton(this);
        btnDeleteAll.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.DELETE_ALL));
        btnDeleteAll.clicked.connect(this, "clearValuesTable()");
        toolTip = msgProvider.translate("ArrayEditor", "Delete All Items");
        btnDeleteAll.setToolTip(toolTip);
        layout.addWidget(btnDeleteAll);

        btnDefine = new QToolButton(this);
        btnDefine.clicked.connect(this, "onDefine()");
        layout.addWidget(btnDefine);

        return layout;
    }

    private void onAdd() {
        if (!isReadonly && isOperationsVisible && (maxArrayItemsCount<0 || destList.count() < maxArrayItemsCount)){
            final int rowsBefore = destList.count();
            final int count = moveSelectedItems(sourceList, destList);
            if (count>0){
                notifyRowsInserted(rowsBefore, count);
                updateButtons();
                isNullValue = false;
                updateUi(true);
            }
        }
    }

    @SuppressWarnings("unused")
    private void onAddAll() {
        if (!isReadonly && isOperationsVisible){
            sourceList.selectAll();
            final int rowsBefore = destList.count();
            final int count = moveSelectedItems(sourceList, destList);
            if (count>0){
                notifyRowsInserted(rowsBefore, count);
                updateButtons();
                isNullValue = false;
                updateUi(true);                
            }
        }
    }
    
    @SuppressWarnings("unused")
    private void onRemove() {
        if (!isReadonly && isOperationsVisible){
            final int startRow;
            final List<EnumItemsListWidget.SelectionPart> parts = destList.getSelectionParts();
            final int count = moveSelectedItems(destList, sourceList);
            if (count>0){
                notifyRowsRemoved(parts);
                sourceList.sort();
                updateButtons();
                updateUi(!currentValueIsNull());                
            }            
        }
    }

    @SuppressWarnings("unused")
    private void onRemoveAll() {
        if (!isReadonly && isOperationsVisible){
            destList.selectAll();
            final int count = moveSelectedItems(destList, sourceList);
            if (count>0){
                notifyRowsRemoved(0, count);
                sourceList.sort();
                updateButtons();
                updateUi(!currentValueIsNull());                
            }
        }
    }
    
    private void onDestListDoubleClicked(){
        if (isReadonly || !isOperationsVisible){
            notifyCellDoubleClick(destList.currentRow());
        }else{
            onRemove();
        }
    }
    
    private void onDefine() {
        isNullValue = !isNullValue;
        updateUi(!currentValueIsNull());
        notifyValueDefined(!isNullValue);
    }

    public boolean currentValueIsNull() {
        return isNullValue;
    }

    private void updateDefinedButtonState(final boolean defined) {
        if (defined) {
            btnDefine.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.CLEAR));
            btnDefine.setToolTip(Application.translate("Value", "Clear Value"));
        } else {
            btnDefine.setIcon(ExplorerIcon.getQIcon(ClientIcon.ValueModification.DEFINE));
            btnDefine.setToolTip(Application.translate("ArrayEditor", "Define Value"));
        }
        btnDefine.setEnabled(!isReadonly);
        btnDefine.setVisible(isOperationsVisible);
    }
    
    private void updateItemLists(final boolean isDefined){
        sourceList.setDragEnabled(!isReadonly && (maxArrayItemsCount<0 || destList.count() < maxArrayItemsCount));
        sourceList.setVisible(isOperationsVisible);
        lbAvailableItems.setVisible(isOperationsVisible);
        lbSelectedItems.setVisible(isOperationsVisible);
        final QAbstractItemView.SelectionMode selectionMode =
            !isReadonly && isOperationsVisible ? QAbstractItemView.SelectionMode.ExtendedSelection : QAbstractItemView.SelectionMode.SingleSelection;
        sourceList.setSelectionMode(selectionMode);
        destList.setEnabled(isDefined);
        if (isDefined){            
            destList.setMoveOperationEnabled(!isReadonly && isMoveOperationsEnabled && isOperationsVisible);
            destList.setSelectionMode(selectionMode);
        }
    }

    private void updateUi(final boolean defined) {
        if (defined) {            
            labelWidget.setCurrentWidget(destList);
        } else {
            if (!clearValuesTable()) {
                return;
            }
            labelWidget.setCurrentWidget(noValueLabel);
        }
        updateItemLists(defined);        

        noValueLabel.setText(noValueStr);
        noValueLabel.setVisible(currentValueIsNull());
        
        updateDefinedButtonState(!currentValueIsNull());
        updateButtons();
        revalidate();
    }

    private void revalidate() {
        final boolean prevCurrentValueValid = isCurrentValueValid;
        errorMessage.clear();
        if (isReadonly || !isOperationsVisible){
            isCurrentValueValid = true;
        }else if (isNullValue){
            isCurrentValueValid = !isMandatory;
        } else{
            final StringBuilder sb = new StringBuilder();
            if (maxArrayItemsCount >= 0 && destList.count() > maxArrayItemsCount) {
                sb.append("<font color='red'>");
                sb.append(String.format(maxMess, getMaxArrayItemsCount()));
                sb.append("</font>");
                isCurrentValueValid = false;
            }else if (minArrayItemsCount >= 0 && destList.count() < minArrayItemsCount) {
                sb.append(System.getProperty("line.separator"));
                sb.append("<font color='red'>");
                sb.append(String.format(minMess, getMinArrayItemsCount()));
                sb.append("</font>");
                isCurrentValueValid = false;
            }else{
                isCurrentValueValid = true;
            }

            if (!isCurrentValueValid) {
                errorMessage.setText(sb.toString().trim());
            }
        }
        errorMessage.setVisible(!isCurrentValueValid && !errorMessage.text().isEmpty());
        if (prevCurrentValueValid!=isCurrentValueValid){
            valueStateChanged.emit(Boolean.valueOf(isCurrentValueValid));
        }
    }

    @SuppressWarnings("unused")
    private void onMoveUp() {
        destList.moveUp();
    }

    @SuppressWarnings("unused")
    private void onMoveDown() {
        destList.moveDown();
    }
    
    public RadEnumPresentationDef.Item getCurrentItem(){
        final List<QListWidgetItem> selectedItems = destList.selectedItems();
        if (selectedItems.isEmpty()){
            return null;
        }else{
            final String idAsStr =  (String) selectedItems.get(0).data(Qt.ItemDataRole.UserRole);
            return items.findItemById(Id.Factory.loadFrom(idAsStr));
        }
    }

    public RadEnumPresentationDef.Items getSelectedItems() {
        if (currentValueIsNull()){
            return null;
        }else{
            final Items selectedItems = items.copy();
            selectedItems.clear();
            final int count = destList.count();
            String idAsStr;
            for (int i = 0; i < count; i++) {
                idAsStr = (String) destList.item(i).data(Qt.ItemDataRole.UserRole);
                selectedItems.addItemWithId(Id.Factory.loadFrom(idAsStr));
            }

            return selectedItems;
        }
    }

    public void setSelectedItems(final RadEnumPresentationDef.Items itemsToSet) {
        for (int row=destList.count()-1; row>=0; row--){
            moveItem(destList, sourceList, destList.item(row));
        }
        if (itemsToSet != null && !itemsToSet.isEmpty()) {
            for (RadEnumPresentationDef.Item i : itemsToSet) {
                final QListWidgetItem itemByData = sourceList.getItemByUserData(i.getId().toString());
                if (items.contains(i) && itemByData != null) {
                    moveItem(sourceList,destList,itemByData);
                }
            }
        }
        isNullValue = itemsToSet==null;
        sourceList.sort();
        updateUi(!isNullValue);
    }
    
    public String getNoValueStr() {
        return noValueStr;
    }    

    public void setNoValueStr(final String str) {
        this.noValueStr = str;
        noValueLabel.setText(str);
    }
    
    public EEditMaskEnumOrder getSortingOrder(){
        return sourceList.getSortingOrder();
    }

    public void setSortingOrder(final EEditMaskEnumOrder order) {
        sourceList.setSortingOrder(order);
    }    

    private void updateButtons() {
        final boolean sourceIsEmpty = (sourceList.count() == 0);
        final boolean destIsEmpty = (destList.count() == 0);
        final boolean canAdd = !sourceIsEmpty && (maxArrayItemsCount >=0 ? destList.count() < maxArrayItemsCount : true) && !isReadonly;

        btnAdd.setEnabled(canAdd && sourceList.selectedItems().size()>0);
        btnAdd.setVisible(isOperationsVisible);
        btnAddAll.setEnabled(canAdd && (maxArrayItemsCount >=0 ? sourceList.count() <= maxArrayItemsCount : true));
        btnAddAll.setVisible(isOperationsVisible);
        btnMoveBack.setDisabled(isReadonly || destIsEmpty || destList.selectedItems().isEmpty());
        btnMoveBack.setVisible(isOperationsVisible);
        btnMoveBackAll.setDisabled(isReadonly || destIsEmpty);
        btnMoveBackAll.setVisible(isOperationsVisible);
        
        btnDeleteAll.setDisabled(isReadonly || destIsEmpty);
        btnDeleteAll.setVisible(isOperationsVisible);

        final int currRow = destList.currentRow();
        btnMoveDown.setEnabled(!isReadonly && isMoveOperationsEnabled && !destIsEmpty && currRow > -1 && currRow < destList.count() - 1);
        btnMoveDown.setVisible(isOperationsVisible);
        btnMoveUp.setEnabled(!isReadonly && isMoveOperationsEnabled && !destIsEmpty && currRow >0);
        btnMoveUp.setVisible(isOperationsVisible);
    }

    private int moveSelectedItems(final QListWidget source, final QListWidget dest) {
        int indexToRestore = 0;
        final List<QListWidgetItem> selectedItems = source.selectedItems();
        if (selectedItems.isEmpty()) {
            if (isReadonly() || source.count() <= 0) {
                return 0;
            } else {
                selectedItems.add(source.item(0));
            }
        }
        int counter = 0;
        for (QListWidgetItem item : selectedItems) {
            indexToRestore = source.row(item);
            moveItem(source,dest,item);
            counter++;
        }

        final int lastRowIndex = source.count() - 1;
        source.setCurrentRow(Math.min(lastRowIndex, indexToRestore));
        return counter;
    }
    
    private void moveItem(final QListWidget source, final QListWidget dest, final QListWidgetItem item){
        final QListWidgetItem itemToMove = source.takeItem(source.row(item));
        dest.addItem(itemToMove);
    }

    public boolean isEmpty() {
        return destList.count() == 0;
    }
    
    @SuppressWarnings("unused")
    private void onDropItems(){
        if (!sourceList.selectedItems().isEmpty() && currentValueIsNull()){
            onDefine();
            final int count = moveSelectedItems(sourceList, destList);
            if (count>0){
                notifyRowsInserted(0, count);
            }
            updateItemLists(true);
            revalidate();
        }
    }
    
    @SuppressWarnings("unused")
    private void afterDropInSource(java.util.List<EnumItemsListWidget.SelectionPart> removed, EnumItemsListWidget.SelectionPart inserted){
        if (QObject.signalSender()!=destList){
            notifyRowsRemoved(removed);
            updateItemLists(true);
        }
        revalidate();
    }
    
    private void afterDropInDest(java.util.List<EnumItemsListWidget.SelectionPart> removed, EnumItemsListWidget.SelectionPart inserted){
        if (QObject.signalSender()!=sourceList){
            notifyRowsInserted(inserted.getStartRow(),inserted.getCount());
            updateItemLists(true);
        }
        revalidate();
    }

    /**
     * @return the minArrayItemsCount
     */
    public int getMinArrayItemsCount() {
        return minArrayItemsCount;
    }

    /**
     * Sets minimal number of array's items.
     *
     * @param minArrayItemsCount minimal number of array's items. If <code>minArrayItemsCount &lt;= 0</code>,
     * the number is unlimited.
     */
    public void setMinArrayItemsCount(final int minArrayItemsCount) {
        this.minArrayItemsCount = minArrayItemsCount;
        updateItemLists(currentValueIsNull());
        updateButtons();        
        revalidate();
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
        updateItemLists(currentValueIsNull());
        updateButtons();
        revalidate();
    }

    public boolean isReadonly() {
        return isReadonly;
    }

    public void setReadonly(final boolean readonly) {
        if (readonly != isReadonly){
            isReadonly = readonly;
            updateUi(!currentValueIsNull());
        }
    }
    
    public boolean isMandatory() {
        return isMandatory;
    }    

    public void setMandatory(final boolean notNull) {
        if (isMandatory != notNull){
            isMandatory = notNull;
            updateUi(!currentValueIsNull());
        }
    }
    
    public boolean isItemsMovable(){
        return isMoveOperationsEnabled;
    }    
    
    public void setItemsMovable(final boolean isMovable) {
        if (isMovable!=isMoveOperationsEnabled){
            isMoveOperationsEnabled = isMovable;
            updateUi(!currentValueIsNull());
        }
    }    
    
    public boolean isOperationsVisible(){
        return isOperationsVisible;
    }
    
    public void setOperationsVisible(final boolean isVisible){
        if (isVisible!=isOperationsVisible){
            isOperationsVisible = isVisible;
            updateUi(!currentValueIsNull());
        }
    }
    
    public boolean isCurrentValueValid(){
        return isCurrentValueValid;
    }
        
    public void addEventListener(final ArrayEditorEventListener listener) {
        if (listener!=null && (listeners==null || !listeners.contains(listener))){
            if (listeners==null){
                listeners = new LinkedList<>();
            }
            listeners.add(listener);
        }
    }
    
    public void removeEventListener(final ArrayEditorEventListener listener) {
        if (listeners!=null){
            listeners.remove(listener);
            if (listeners.isEmpty()){
                listeners = null;
            }
        }
    }
    
    private boolean clearValuesTable() {
        if (destList.count()>0){
            final String msg = Application.translate("ArrayEditor", "Do you really want to delete all items?");
            if (!Application.messageConfirmation(Application.translate("ArrayEditor", "Confirm To Clear Items"), msg)) {
                return false;
            }
            destList.selectAll();
            final int count = moveSelectedItems(destList, sourceList);
            if (count>0){
                notifyRowsRemoved(0, count);
            }
            sourceList.sort();
            updateItemLists(true);
            updateButtons();
            revalidate();
        }
        return true;
    }
    
    private Collection<ArrayEditorEventListener> copyListeners(){
        if (listeners==null || listeners.isEmpty()){
            return Collections.emptyList();
        }else{
            return new LinkedList<>(listeners);
        }
    }
    
    private void notifyCellDoubleClick(final int row){
        for (ArrayEditorEventListener listener: copyListeners()){
            listener.onCellDoubleClick(row);
        }
    }
    
    private void notifyRowsRemoved(final int startRow, final int count){
        for (ArrayEditorEventListener listener: copyListeners()){
            listener.onRowsRemoved(startRow, count);
        }        
    }
    
    private void notifyRowsRemoved(final List<EnumItemsListWidget.SelectionPart> parts){
        for (EnumItemsListWidget.SelectionPart part: parts){
            notifyRowsRemoved(part.getStartRow(), part.getCount());
        }        
    }
    
    private void notifyRowsInserted(final int startRow, final int count){
        for (ArrayEditorEventListener listener: copyListeners()){
            listener.onRowsInserted(startRow, count);
        }
    }
    
    private void notifyValueDefined(final boolean isDefined){
        for (ArrayEditorEventListener listener: copyListeners()){
            if (isDefined){
                listener.onDefineValue();
            }else{
                listener.onUndefineValue();
            }
        }        
    }
}
