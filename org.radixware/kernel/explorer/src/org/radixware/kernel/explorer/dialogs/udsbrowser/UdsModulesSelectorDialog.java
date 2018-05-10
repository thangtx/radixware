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

package org.radixware.kernel.explorer.dialogs.udsbrowser;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QAbstractItemView;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QKeySequence;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QTreeWidget;
import com.trolltech.qt.gui.QTreeWidgetItem;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.ESelectorRowStyle;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;


final class UdsModulesSelectorDialog extends ExplorerDialog{
    
    private final static ClientIcon UDS_MODULE_CLIENT_ICON = new ClientIcon("classpath:images/uds_module.svg"){
    };   
    
    private final static Qt.ItemFlags UDS_NODE_FLAGS = 
        new Qt.ItemFlags(Qt.ItemFlag.ItemIsEnabled, Qt.ItemFlag.ItemIsSelectable, Qt.ItemFlag.ItemIsUserCheckable);
    private final static Qt.ItemFlags LAYER_NODE_FLAGS = 
        new Qt.ItemFlags(Qt.ItemFlag.ItemIsEnabled, Qt.ItemFlag.ItemIsSelectable, Qt.ItemFlag.ItemIsTristate, Qt.ItemFlag.ItemIsUserCheckable);
    private final static EnumSet<ETextOptionsMarker> UNSELECTED_TREE_ITEM_TEXT_MARKERS = 
        EnumSet.of(ETextOptionsMarker.SELECTOR_ROW, ETextOptionsMarker.EDITABLE_VALUE, ETextOptionsMarker.REGULAR_VALUE);
    private final static EnumSet<ETextOptionsMarker> SELECTED_TREE_ITEM_TEXT_MARKERS = 
        EnumSet.of(ETextOptionsMarker.SELECTOR_ROW, ETextOptionsMarker.EDITABLE_VALUE, ETextOptionsMarker.CHOOSEN_OBJECT, ETextOptionsMarker.REGULAR_VALUE);
    
    private static interface ITreeVisitor{
        void visit(QTreeWidgetItem item, boolean isUdsModule);
    }    
        
    private final List<LayerNode> layerNodes = new LinkedList<>();
    private final QTreeWidget udsTree;
    private final QLabel lbSelection = new QLabel(this);
    private final ExplorerTextOptions itemTextOptions, selectedItemTextOptions;
    private final List<QTreeWidgetItem> itemsByOrder = new LinkedList<>();
    private final List<UdsModuleNode> selectedModules = new LinkedList<>();

    public UdsModulesSelectorDialog(final List<LayerNode> layerNodes, final IClientEnvironment environment, final QWidget parentWidget){
        super(environment, parentWidget);
        udsTree = new QTreeWidget(this){
            @Override
            protected void keyPressEvent(final QKeyEvent event) {
                if (event.matches(QKeySequence.StandardKey.SelectAll)){
                    selectAllItems();
                }else{
                    super.keyPressEvent(event);
                }
            }
        };
        this.layerNodes.addAll(layerNodes);
        itemTextOptions = 
            (ExplorerTextOptions)environment.getTextOptionsProvider().getOptions(UNSELECTED_TREE_ITEM_TEXT_MARKERS, ESelectorRowStyle.NORMAL);
        selectedItemTextOptions = 
            (ExplorerTextOptions)environment.getTextOptionsProvider().getOptions(SELECTED_TREE_ITEM_TEXT_MARKERS, ESelectorRowStyle.NORMAL);    
        setupUi();
    }
    
    private void setupUi(){
        setWindowTitle(getEnvironment().getMessageProvider().translate("UdsModulesSelectorDialog", "Select UDS Modules"));
        setWindowIcon(ExplorerIcon.getQIcon(UDS_MODULE_CLIENT_ICON));
        
        dialogLayout().addWidget(udsTree);
        fillUdsTree();
        udsTree.header().setVisible(false);
        udsTree.setSelectionBehavior(QAbstractItemView.SelectionBehavior.SelectRows);
        udsTree.setSelectionMode(QAbstractItemView.SelectionMode.SingleSelection);
        udsTree.itemChanged.connect(this,"onItemChanged(QTreeWidgetItem)");
        udsTree.itemDoubleClicked.connect(this,"onItemDoubleClicked(QTreeWidgetItem)");
        udsTree.itemClicked.connect(this,"onItemClicked(QTreeWidgetItem)");
        udsTree.currentItemChanged.connect(this,"currentItemChanged(QTreeWidgetItem, QTreeWidgetItem)");
        
        dialogLayout().addWidget(lbSelection);
        ExplorerTextOptions.getDefault().applyTo(lbSelection);
        
        addButton(EDialogButtonType.OK).addClickHandler(new IButton.ClickHandler() {
            @Override
            public void onClick(IButton source) {
                onOkClick();
            }            
        });
        addButton(EDialogButtonType.CANCEL).addClickHandler(new IButton.ClickHandler() {
            @Override
            public void onClick(IButton source) {
                reject();
            }
        });        
        updateAfterChangeSelection();
        udsTree.setFocus();
        setAttribute(Qt.WidgetAttribute.WA_DeleteOnClose, true);
    }  
            
    private void fillUdsTree(){        
        if (layerNodes.size()>1){
            QTreeWidgetItem layerTreeItem;
            for (LayerNode layerNode: layerNodes){                
                layerTreeItem = addTreeNode(layerNode, null);
                layerTreeItem.setFlags(LAYER_NODE_FLAGS);
                addUdsModuleNodes(layerNode, layerTreeItem);
            }
        }else if (layerNodes.size()==1){
            addUdsModuleNodes(layerNodes.get(0), null);
        }
        for (QTreeWidgetItem treeItem: itemsByOrder){
            if (isUdsModule(treeItem)){
                udsTree.setCurrentItem(treeItem);
                break;
            }
        }
    }
    
    private void addUdsModuleNodes(final LayerNode layerNode, final QTreeWidgetItem parentItem){
        final List<UdsModuleNode> udsModules = layerNode.getUdsModules();
        QTreeWidgetItem treeItem;
        for (UdsModuleNode moduleNode: udsModules){
            treeItem = addTreeNode(moduleNode, parentItem);
            treeItem.setFlags(UDS_NODE_FLAGS);
            treeItem.setData(0, Qt.ItemDataRole.UserRole, moduleNode);            
        }
        if (parentItem!=null){
            udsTree.expandItem(parentItem);
        }
    }        
    
    private QTreeWidgetItem addTreeNode(final AbstractBranchNode node, final QTreeWidgetItem parentItem){
        final QTreeWidgetItem treeItem;
        if (parentItem==null){
            treeItem = new QTreeWidgetItem(udsTree);
        }else{
            treeItem = new QTreeWidgetItem(parentItem);
        }
        treeItem.setText(0, node.getTitle());
        treeItem.setIcon(0, node.getIcon());
        if (node.getDescription()!=null && !node.getDescription().isEmpty()){
            treeItem.setToolTip(0, node.getDescription());
        }
        treeItem.setCheckState(0, Qt.CheckState.Unchecked);
        itemTextOptions.applyTo(treeItem, 0);        
        if (parentItem==null){
            udsTree.addTopLevelItem(treeItem);
        }
        itemsByOrder.add(treeItem);
        return treeItem;
    }
    
    private List<UdsModuleNode> collectSelectedModules(final boolean updateTextOptions){
        final List<UdsModuleNode> selection = new LinkedList<>();
        for (QTreeWidgetItem treeItem: itemsByOrder){
            if (isUdsModule(treeItem)){
                if (treeItem.checkState(0)==Qt.CheckState.Checked){
                    selection.add((UdsModuleNode)treeItem.data(0, Qt.ItemDataRole.UserRole));
                    selectedItemTextOptions.applyTo(treeItem,0);
                }else if (updateTextOptions){
                    itemTextOptions.applyTo(treeItem,0);
                }
            }
        }        
        if (selection.isEmpty() && udsTree.currentItem()!=null){
            final Object currentItemData = udsTree.currentItem().data(0, Qt.ItemDataRole.UserRole);
            if (currentItemData instanceof UdsModuleNode){
                selection.add((UdsModuleNode)currentItemData);
            }
        }
        return selection;
    }
    
    private boolean isMultiSelectionMode(){
        for (QTreeWidgetItem treeItem: itemsByOrder){
            if (isUdsModule(treeItem) && treeItem.checkState(0)==Qt.CheckState.Checked){
                return true;
            }
        }
        return false;
    }
    
    private void updateAfterChangeSelection(){
        final List<UdsModuleNode> selection = collectSelectedModules(true);        
        final MessageProvider messageProvider = getEnvironment().getMessageProvider();
        if (selection.isEmpty()){
            lbSelection.setText(messageProvider.translate("UdsModulesSelectorDialog", "No modules selected"));
        }else if (selection.size()==1){
            final String messageTemplate = 
                messageProvider.translate("UdsModulesSelectorDialog", "Selected module: %1$s");
            lbSelection.setText(String.format(messageTemplate, selection.get(0).getTitle()));
        }else{
            final String messageTemplate = 
                messageProvider.translate("UdsModulesSelectorDialog", "Number of selected modules: %1$s");
            lbSelection.setText(String.format( messageTemplate, String.valueOf(selection.size()) ));
        }
        getButton(EDialogButtonType.OK).setEnabled(!selection.isEmpty());
    }
        
    @SuppressWarnings("unused")
    private void onItemChanged(final QTreeWidgetItem item){
        updateAfterChangeSelection();
    }
    
    @SuppressWarnings("unused")
    private void currentItemChanged(final QTreeWidgetItem current, final QTreeWidgetItem previous){
        if (previous!=null && current!=previous){
            if (isShiftKeyPressed()){
                setRangeCheckState(current, previous, true, Qt.CheckState.Checked);
            }
            if (isCtrlKeyPressed()){
                setRangeCheckState(current, previous, true, Qt.CheckState.Unchecked);
            }
        }        
        updateAfterChangeSelection();
    }
    
    @SuppressWarnings("unused")
    private void onItemClicked(final QTreeWidgetItem item){
        final QTreeWidgetItem current = udsTree.currentItem();
        if (current!=null && item!=current){
            if (isShiftKeyPressed()){
                setRangeCheckState(current, item, true, Qt.CheckState.Checked);
            }
            if (isCtrlKeyPressed()){
                setRangeCheckState(current, item, true, Qt.CheckState.Unchecked);
            }
        }
    }
    
    @SuppressWarnings("unused")
    private void onItemDoubleClicked(final QTreeWidgetItem item){
        if (item!=null && isUdsModule(item)){
            if (isMultiSelectionMode()){
                if (item.checkState(0)==Qt.CheckState.Checked){
                    item.setCheckState(0, Qt.CheckState.Unchecked);
                }else{
                    item.setCheckState(0, Qt.CheckState.Checked);
                }       
                updateAfterChangeSelection();
            }else{
                selectedModules.clear();
                selectedModules.add((UdsModuleNode)item.data(0, Qt.ItemDataRole.UserRole));
                accept();
            }
        }
    }
    
    private static boolean isShiftKeyPressed(){
        final Qt.KeyboardModifiers modifiers = QApplication.keyboardModifiers();
        return modifiers.value()==Qt.KeyboardModifier.ShiftModifier.value();
    }
    
    private static boolean isCtrlKeyPressed(){
        final Qt.KeyboardModifiers modifiers = QApplication.keyboardModifiers();
        return modifiers.value()==Qt.KeyboardModifier.ControlModifier.value();
    }            
    
    private void setRangeCheckState(final QTreeWidgetItem item1, final QTreeWidgetItem item2, final boolean inclusiveItem2, final Qt.CheckState checkState){
        udsTree.blockSignals(true);
        try{
            visitRange(item1, item2, inclusiveItem2, new ITreeVisitor() {
                @Override
                public void visit(final QTreeWidgetItem item, final boolean isUdsModule) {
                    if (isUdsModule){
                        item.setCheckState(0, checkState);
                    }
                }
            });
        }finally{            
            udsTree.blockSignals(false);
        }        
    }    
    
    private void visitRange(final QTreeWidgetItem item1, final QTreeWidgetItem item2, final boolean inclusiveItem2, final ITreeVisitor visitor){
        final int firstItemIndex = itemsByOrder.indexOf(item1);
        int secondItemIndex = itemsByOrder.indexOf(item2);
        if (!inclusiveItem2){
            if (firstItemIndex>secondItemIndex){
                secondItemIndex++;
            }else{
                secondItemIndex--;
            }
        }
        for (int i=Math.min(firstItemIndex, secondItemIndex),to=Math.max(firstItemIndex, secondItemIndex); i<=to; i++){
            final QTreeWidgetItem currentItem = itemsByOrder.get(i);
            visitor.visit(currentItem, isUdsModule(currentItem));
        }
    }    
    
    private static UdsModuleNode getUdsModuleNode(final QTreeWidgetItem item){
        return (UdsModuleNode) item.data(0, Qt.ItemDataRole.UserRole);
    }
    
    private static boolean isUdsModule(final QTreeWidgetItem item){
        return getUdsModuleNode(item)!=null;
    }
    
    private void onOkClick(){
        selectedModules.clear();
        selectedModules.addAll(collectSelectedModules(false));
        if (!selectedModules.isEmpty()){
            accept();
        }
    }
    
    private void selectAllItems(){
        udsTree.blockSignals(true);
        try{
            for (QTreeWidgetItem item: itemsByOrder){
                if (isUdsModule(item)){
                    item.setCheckState(0, Qt.CheckState.Checked);                    
                }
            }
            updateAfterChangeSelection();
        }finally{
            udsTree.blockSignals(false);
        }
    }    
    
    public List<UdsModuleNode> getSelectedModules(){
        return selectedModules;
    }
}
