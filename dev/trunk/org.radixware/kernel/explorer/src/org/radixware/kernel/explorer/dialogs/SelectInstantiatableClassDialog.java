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

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QCheckBox;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QKeySequence;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLineEdit;
import java.util.List;

import com.trolltech.qt.gui.QTreeWidget;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QTreeWidgetItem;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.Stack;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.dialogs.ISelectInstantiatableClassDialog;
import org.radixware.kernel.common.client.enums.EFontStyle;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.types.InstantiatableClass;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.text.ExplorerFont;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;
import org.radixware.kernel.explorer.utils.WidgetUtils;

public final class SelectInstantiatableClassDialog extends ExplorerDialog implements ISelectInstantiatableClassDialog {
    
    private final static String DLG_SETTINGS_GROUP_NAME=SettingNames.SYSTEM+"/SEL_INST_CLASS_DLG";
    private final static String EXPANDED_ITEMS_GROUP_NAME="expandedItems";
    private final static Qt.ItemFlags FLAGS_FOR_SELECTABLE_INSTANTIATABLE_CLASS = 
        new Qt.ItemFlags(Qt.ItemFlag.ItemIsEnabled, Qt.ItemFlag.ItemIsSelectable, Qt.ItemFlag.ItemIsUserCheckable);
    private final static Qt.ItemFlags FLAGS_FOR_SELECTABLE_ABSTRACT_CLASS = 
        new Qt.ItemFlags(Qt.ItemFlag.ItemIsEnabled, Qt.ItemFlag.ItemIsSelectable, Qt.ItemFlag.ItemIsUserCheckable, Qt.ItemFlag.ItemIsTristate);
    private final static Qt.ItemFlags FLAGS_FOR_SELECTED_INSTANTIATABLE_CLASS = 
        new Qt.ItemFlags(Qt.ItemFlag.ItemIsEnabled, Qt.ItemFlag.ItemIsUserCheckable);  
    private final static Qt.ItemFlags FLAGS_FOR_UNSELECTABLE_CLASS = 
            new Qt.ItemFlags(Qt.ItemFlag.ItemIsEnabled, Qt.ItemFlag.ItemIsSelectable);
    
    private static interface ITreeVisitor{
        void visit(QTreeWidgetItem item, boolean isAbstractClass);
    }    
        
    private final ExplorerFont abstractClassFont;
    private final QTreeWidget tree;
    private final QLabel lbSelectedItems;
    private final String settingsGroupName;
    private final List<InstantiatableClass> expandedClasses = new ArrayList<>();
    private final List<InstantiatableClass> selectedClasses = new LinkedList<>();
    private final List<QTreeWidgetItem> autoExpandedItems = new LinkedList<>();    
    private final boolean sortByTitles;
    private final String selectedItemsCountTextTemplate;
    private final String selectedItemTextTemplate;
    private final String noSelectionText;
    private final Set<InstantiatableClass> currentSelection = new HashSet<>();
    private final List<QTreeWidgetItem> itemsByOrder = new ArrayList<>();
    private final QCheckBox cbMultiSelectionEnabled;
    private boolean applyingFilter;
    private InstantiatableClass currentClass;   
    
    @SuppressWarnings("LeakingThisInConstructor")
    public SelectInstantiatableClassDialog(final IClientEnvironment environment, 
                                           final QWidget parent, 
                                           final List<org.radixware.kernel.common.client.types.InstantiatableClass> classes,
                                           final String configGroup,
                                           final boolean sortByTitles) {
        this (environment,parent,classes,configGroup,sortByTitles,false);
    }    

    @SuppressWarnings("LeakingThisInConstructor")
    public SelectInstantiatableClassDialog(final IClientEnvironment environment, 
                                           final QWidget parent, 
                                           final List<org.radixware.kernel.common.client.types.InstantiatableClass> classes,
                                           final String configGroup,
                                           final boolean sortByTitles,
                                           final boolean multiSelectEnabled) {
        super(environment, parent, null);
        setAttribute(Qt.WidgetAttribute.WA_DeleteOnClose, true);
        abstractClassFont = ExplorerFont.Factory.getDefault().changeStyle(EFontStyle.ITALIC);
        settingsGroupName = DLG_SETTINGS_GROUP_NAME+"/"+configGroup;
        this.sortByTitles = sortByTitles;
        final MessageProvider mp = environment.getMessageProvider();
        setWindowTitle(mp.translate("SelectInstantiatableClass", "Select Class of New Object"));
                
        final QHBoxLayout ltFilter = new QHBoxLayout();
        final QLabel lbFilter = new QLabel(mp.translate("SelectInstantiatableClass", "&Filter:"), this);
        final QLineEdit leFilter = new QLineEdit(this);
        lbFilter.setBuddy(leFilter);
        ltFilter.addWidget(lbFilter);
        ltFilter.addWidget(leFilter);
        leFilter.textEdited.connect(this, "applyFilter(String)");
        dialogLayout().addLayout(ltFilter);

        tree = new QTreeWidget(this){
            @Override
            protected void keyPressEvent(final QKeyEvent event) {
                if (isMultiSelectAllowed() && event.matches(QKeySequence.StandardKey.SelectAll)){
                    selectAllItems();
                }else{
                    super.keyPressEvent(event);
                }
            }            
        };        
        
        layout().addWidget(tree);
        tree.setColumnCount(1);
        tree.header().hide();
        tree.currentItemChanged.connect(this, "onCurrentItemChanged(QTreeWidgetItem, QTreeWidgetItem)");        
        final List<InstantiatableClass> compactedClasses;
        if (sortByTitles){
            compactedClasses = InstantiatableClass.sortByTitles(InstantiatableClass.compact(classes));
        }else{
            compactedClasses = InstantiatableClass.compact(classes);
        }
        final List<InstantiatableClass> expandedItems = readExpandedItems();
        for (InstantiatableClass item : compactedClasses) {
            fillTree(item, null, expandedItems);
        }
        if (multiSelectEnabled){
            cbMultiSelectionEnabled = new QCheckBox(mp.translate("SelectInstantiatableClass", "Create several objects of different classes"), this);
            cbMultiSelectionEnabled.toggled.connect(this,"setMultiSelectionEnabled(Boolean)");
            layout().addWidget(cbMultiSelectionEnabled);
        }else{
            cbMultiSelectionEnabled = null;
        }
        lbSelectedItems = new QLabel(this);
        lbSelectedItems.setVisible(false);        
        layout().addWidget(lbSelectedItems);
        addButtons(EnumSet.of(EDialogButtonType.OK,EDialogButtonType.CANCEL),true);        
        tree.itemDoubleClicked.connect(this, "onDoubleClick(QTreeWidgetItem)");
        tree.itemClicked.connect(this, "onClick(QTreeWidgetItem)");
        tree.itemExpanded.connect(this,"onExpandItem(QTreeWidgetItem)");
        tree.itemCollapsed.connect(this,"onCollapseItem(QTreeWidgetItem)");
        if (multiSelectEnabled){
            tree.itemChanged.connect(this,"onChangeItem(QTreeWidgetItem)");
        }        
        selectedItemsCountTextTemplate = mp.translate("SelectInstantiatableClass", "Number of selected items: %1$s");            
        selectedItemTextTemplate = mp.translate("SelectInstantiatableClass", "Selected item: %1$s");
        noSelectionText = mp.translate("SelectInstantiatableClass", "No items selected");
        getButton(EDialogButtonType.OK).setEnabled(false);
    }
    
    private QTreeWidgetItem fillTree(final InstantiatableClass currentClass, 
                                     final QTreeWidgetItem parentItem,
                                     final List<InstantiatableClass> expandedItems){
        final List<InstantiatableClass> derivedClasses;
        if (sortByTitles){
            derivedClasses = InstantiatableClass.sortByTitles(currentClass.getDerivedClasses());
        }else{
            derivedClasses = currentClass.getDerivedClasses();
        }        
        
        final QTreeWidgetItem item = createTreeWidgetItemForClass(currentClass, parentItem, !derivedClasses.isEmpty());
        itemsByOrder.add(item);
        for (InstantiatableClass cl: derivedClasses){
            fillTree(cl, item, expandedItems);
        }
        if (derivedClasses.size()>0 && currentClass.containsIn(expandedItems)){
            tree.expandItem(item);
            expandedClasses.add(currentClass);
        }        
        return item;
    }        
    
    private QTreeWidgetItem createTreeWidgetItemForClass(final InstantiatableClass instantiatableClass, 
                                                                                           final QTreeWidgetItem parentItem,
                                                                                           final boolean hasDerivedClass){
        final QTreeWidgetItem treeItem;
        if (parentItem==null){
            treeItem = new QTreeWidgetItem(tree);
        }else{
            treeItem = new QTreeWidgetItem(parentItem);
        }
        treeItem.setText(0, instantiatableClass.getTitle());
        if (instantiatableClass.isAbstractClass()){
            treeItem.setFont(0, abstractClassFont.getQFont());
        }
        treeItem.setData(0, Qt.ItemDataRole.UserRole, instantiatableClass);        
        final Id itemId;
        if (instantiatableClass.getItemId()!=null){
            itemId = instantiatableClass.getItemId();
        }else if (instantiatableClass.getId()!=null){
            itemId = instantiatableClass.getId();
        }else{
            itemId = null;
        }
        if (itemId!=null){
            final String itemName = "rx_entity_class_node_"+itemId.toString();
            treeItem.setData(0, WidgetUtils.MODEL_ITEM_ROW_NAME_DATA_ROLE, itemName);
            treeItem.setData(0, WidgetUtils.MODEL_ITEM_CELL_NAME_DATA_ROLE, itemName);
        }
        return treeItem;
    }
    
    private static InstantiatableClass getClass(final QTreeWidgetItem treeItem){
        return treeItem==null ? null : (InstantiatableClass)treeItem.data(0, Qt.ItemDataRole.UserRole);
    }
    
    private static boolean isAbstractClass(final QTreeWidgetItem treeItem){
        return getClass(treeItem).isAbstractClass();
    }   

    @SuppressWarnings("unused")
    private void onCurrentItemChanged(final QTreeWidgetItem current, final QTreeWidgetItem previous) {
        if (current!=null && !applyingFilter && previous!=null && current!=previous  && isMultiSelectAllowed()){
            if (isShiftKeyPressed()){
                enableMultiSelect();
                setRangeCheckState(current, previous, true, Qt.CheckState.Checked);
            }
            if (isCtrlKeyPressed() && isMultiSelectEnabled()){
                setRangeCheckState(current, previous, true, Qt.CheckState.Unchecked);
            }
        }
        currentClass = getClass(current);
        updateOKButton();
        updateLabelText();
    }
    
    @SuppressWarnings("unused")
    private void onClick(final QTreeWidgetItem item) {       
        final QTreeWidgetItem current = tree.currentItem();
        if (current!=null && item!=current && isMultiSelectAllowed()){
            if (isShiftKeyPressed()){
                enableMultiSelect();
                setRangeCheckState(current, item, true, Qt.CheckState.Checked);
            }
            if (isCtrlKeyPressed() && isMultiSelectEnabled()){
                setRangeCheckState(current, item, true, Qt.CheckState.Unchecked);
            }
        }
        
    }
    
    @SuppressWarnings("unused")
    private void onDoubleClick(final QTreeWidgetItem item) {
        if (item!=null && !isAbstractClass(item)){
            if (currentSelection.isEmpty() || !isMultiSelectEnabled()){
                accept();
           }else {
                invertCheckState(item);
           }
        }
    }
    
    private static void invertCheckState(final QTreeWidgetItem item){
        if (item.checkState(0)==Qt.CheckState.Checked){
            item.setCheckState(0, Qt.CheckState.Unchecked);
        }else{
            item.setCheckState(0, Qt.CheckState.Checked);
        }        
    }
    
    private void updateOKButton(){
        final boolean isOkEnabled = (currentClass != null && !currentClass.isAbstractClass()) || !currentSelection.isEmpty();
        getButton(EDialogButtonType.OK).setEnabled(isOkEnabled);        
    }
    
    private void updateLabelText(){
        if (lbSelectedItems.isVisible()){
            if (currentSelection.isEmpty()){
                if (currentClass != null && !currentClass.isAbstractClass()){
                    lbSelectedItems.setText(String.format(selectedItemTextTemplate, currentClass.getTitle()));
                }else{
                    lbSelectedItems.setText(noSelectionText);
                }
            }else{
                lbSelectedItems.setText(String.format(selectedItemsCountTextTemplate, currentSelection.size()));        
            }
        }
    }

    @Override
    public void done(final int result) {
        if (result!=0){
            collectSelectedClasses();
            if (selectedClasses.isEmpty() && currentClass!=null){
                selectedClasses.add(currentClass);
            }
        }
        itemsByOrder.clear();
        writeExpandedItems();
        super.done(result);
    }        

    @Override
    public InstantiatableClass getCurrentClass() {
        return currentClass;
    }        

    @Override
    public List<InstantiatableClass> getSelectedClasses() {
        return selectedClasses;
    }        
    
    @SuppressWarnings("unused")
    private void onExpandItem(final QTreeWidgetItem item){
        expandedClasses.add(getClass(item));
        autoExpandedItems.remove(item);
    }
    
    private void onCollapseItem(final QTreeWidgetItem item){
        expandedClasses.remove(getClass(item));
        autoExpandedItems.remove(item);
    }
    
    @SuppressWarnings("unused")
    private void onChangeItem(final QTreeWidgetItem item){
        if (item.isHidden()){
            invertCheckState(item);
        }else  if (!isAbstractClass(item)){
            updateInstantiatableClassSelection(item);
        }
    }
    
    private void updateInstantiatableClassSelection(final QTreeWidgetItem item){
        if (item.checkState(0)==Qt.CheckState.Checked){
            final ExplorerTextOptions options =
                ExplorerTextOptions.Factory.getOptions(ETextOptionsMarker.SELECTOR_ROW,ETextOptionsMarker.CHOOSEN_OBJECT);
            item.setBackground(0, options.getBackgroundBrush());
            currentSelection.add(getClass(item));
        }else{
            item.setBackground(0, null);
            currentSelection.remove(getClass(item));
        }
        if (!currentSelection.isEmpty()){
            lbSelectedItems.setVisible(true);            
        }        
        updateOKButton();
        updateLabelText();
    }        
    
    private void setRangeCheckState(final QTreeWidgetItem item1, final QTreeWidgetItem item2, final boolean inclusiveItem2, final Qt.CheckState checkState){
        tree.blockSignals(true);
        try{
            visitRange(item1, item2, inclusiveItem2, new ITreeVisitor() {
                @Override
                public void visit(final QTreeWidgetItem item, final boolean isAbstractClass) {                    
                    if (!isAbstractClass){
                        item.setCheckState(0, checkState);
                        updateInstantiatableClassSelection(item);
                    }
                }
            });
        }finally{            
            tree.blockSignals(false);
        }        
    }
    
    private void enableMultiSelect(){
        if (isMultiSelectAllowed() && !isMultiSelectEnabled()){
            cbMultiSelectionEnabled.setChecked(true);
        }
    }
    
    @SuppressWarnings("unused")
    private void setMultiSelectionEnabled(final Boolean isEnabled){
        tree.blockSignals(true);
        try{
            boolean isAbstractClass;
            for (QTreeWidgetItem item: itemsByOrder){
                isAbstractClass = isAbstractClass(item);
                if (!isEnabled){
                    item.setCheckState(0, Qt.CheckState.Unchecked);
                    updateInstantiatableClassSelection(item);
                }
                if (isEnabled){
                    item.setFlags( isAbstractClass ? FLAGS_FOR_SELECTABLE_ABSTRACT_CLASS : FLAGS_FOR_SELECTABLE_INSTANTIATABLE_CLASS  );
                    item.setCheckState(0, Qt.CheckState.Unchecked);
                }else{
                    item.setFlags(FLAGS_FOR_UNSELECTABLE_CLASS);
                    item.setData(0, Qt.ItemDataRole.CheckStateRole, null);                    
                }
            }
            
            if (isEnabled){
                tree.itemChanged.connect(this,"onChangeItem(QTreeWidgetItem)");
            }else{
                tree.itemChanged.disconnect(this);
            }
            
        }finally{
            tree.blockSignals(false);
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
            if (!currentItem.isHidden()){
                visitor.visit(currentItem, isAbstractClass(currentItem));
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
    
    @SuppressWarnings("unused")
    private void applyFilter(final String filter){
        setUpdatesEnabled(false);
        applyingFilter = true;
        try{
            tree.itemExpanded.disconnect(this);
            tree.itemCollapsed.disconnect(this);
            try{
                for (QTreeWidgetItem item: autoExpandedItems){
                    tree.collapseItem(item);
                }
                autoExpandedItems.clear();
                for (int i=0, count=tree.topLevelItemCount(); i<count; i++){
                    applyFilter(filter, tree.topLevelItem(i));
                }
                updateCurrentItem();
            }finally{
                tree.itemExpanded.connect(this,"onExpandItem(QTreeWidgetItem)");
                tree.itemCollapsed.connect(this,"onCollapseItem(QTreeWidgetItem)");            
            }
        }finally{
            setUpdatesEnabled(true);
            applyingFilter = false;
        }
    }
    
    private boolean applyFilter(final String filter, final QTreeWidgetItem treeItem){
        final int childCount = treeItem.childCount();
        if (childCount>0){
            boolean isHidden = true;
            for (int i=0; i<childCount; i++){
                if (applyFilter(filter, treeItem.child(i))){
                    isHidden = false;
                }
            }
            if (isHidden){
                final InstantiatableClass cl = getClass(treeItem);
                if (!cl.isAbstractClass()){
                    isHidden = isFiltered(filter, treeItem);
                }
            }else if (filter!=null && !filter.isEmpty() && !treeItem.isExpanded()){
                treeItem.setExpanded(true);
                autoExpandedItems.add(treeItem);
            }
            treeItem.setHidden(isHidden);
            return !isHidden;            
        }else{
            final boolean isHidden = isFiltered(filter, treeItem);
            treeItem.setHidden(isHidden);
            return !isHidden;
        }
    }
    
    private void updateCurrentItem(){
        final Stack<QTreeWidgetItem> stack = new Stack<>();
        for (int i=tree.topLevelItemCount()-1; i>=0; i--){
            stack.push(tree.topLevelItem(i));
        }
        QTreeWidgetItem currentItem;
        while (!stack.isEmpty()){
            currentItem = stack.pop();
            if (!currentItem.isHidden() && !getClass(currentItem).isAbstractClass()){
                tree.setCurrentItem(currentItem);
                return;
            }
            if (currentItem.isExpanded()){
                for (int i=currentItem.childCount()-1; i>=0; i--){
                    stack.push(currentItem.child(i));
                }
            }
        }
        tree.setCurrentItem(null);
    }
    
    final static boolean isFiltered(final String filter, final QTreeWidgetItem treeItem){
        if (filter==null || filter.isEmpty()){
            return false;
        }else{
            final InstantiatableClass cl = getClass(treeItem);
            final String filterLowerCase = filter.toLowerCase();
            return !cl.getTitle().toLowerCase().contains(filterLowerCase) 
                   && (cl.getId()==null || !filterLowerCase.equals(cl.getId().toString()))
                   && (cl.getItemId()==null || !filterLowerCase.equals(cl.getItemId().toString()));
        }        
    }
    
    private void writeExpandedItems(){
        final ClientSettings settings = getEnvironment().getConfigStore();
        settings.remove(settingsGroupName);
        settings.beginGroup(settingsGroupName);
        try{            
            InstantiatableClass.writeToSettings(settings, EXPANDED_ITEMS_GROUP_NAME, expandedClasses);
        }finally{
            settings.endGroup();
        }
    }
    
    private List<InstantiatableClass> readExpandedItems(){
        final ClientSettings settings = getEnvironment().getConfigStore();
        settings.beginGroup(settingsGroupName);
        try{            
            return InstantiatableClass.readFromSettings(settings, EXPANDED_ITEMS_GROUP_NAME, null);
        }finally{
            settings.endGroup();
        }        
    }
    
    private void collectSelectedClasses(){
        for (QTreeWidgetItem item: itemsByOrder){
            if (!isAbstractClass(item) && item.checkState(0)==Qt.CheckState.Checked){
                selectedClasses.add(getClass(item));
            }
        }
    }
    
    private boolean isMultiSelectAllowed(){
        return cbMultiSelectionEnabled!=null;
    }
    
    private boolean isMultiSelectEnabled(){
        return isMultiSelectAllowed() && cbMultiSelectionEnabled.isChecked();
    }
    
    private void selectAllItems(){
        enableMultiSelect();
        tree.blockSignals(true);
        try{
            for (QTreeWidgetItem item: itemsByOrder){
                if (!item.isHidden() && !isAbstractClass(item)){
                    item.setCheckState(0, Qt.CheckState.Checked);
                    updateInstantiatableClassSelection(item);
                }
            }
        }finally{
            tree.blockSignals(false);
        }        
    }    
    
}