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
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLineEdit;
import java.util.List;

import com.trolltech.qt.gui.QTreeWidget;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QTreeWidgetItem;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.Stack;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.dialogs.ISelectInstantiatableClassDialog;
import org.radixware.kernel.common.client.enums.EFontStyle;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.types.InstantiatableClass;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.text.ExplorerFont;

public final class SelectInstantiatableClassDialog extends ExplorerDialog implements ISelectInstantiatableClassDialog {
    
    private final static String DLG_SETTINGS_GROUP_NAME=SettingNames.SYSTEM+"/SEL_INST_CLASS_DLG";
    private final static String EXPANDED_ITEMS_GROUP_NAME="expandedItems";
    
    private final ExplorerFont abstractClassFont;
    private final QTreeWidget tree;
    private final String settingsGroupName;
    private final List<InstantiatableClass> expandedClasses = new ArrayList<>();
    private final List<QTreeWidgetItem> autoExpandedItems = new LinkedList<>();
    private final boolean sortByTitles;
    private InstantiatableClass currentClass;

    @SuppressWarnings("LeakingThisInConstructor")
    public SelectInstantiatableClassDialog(final IClientEnvironment environment, 
                                           final QWidget parent, 
                                           final List<org.radixware.kernel.common.client.types.InstantiatableClass> classes,
                                           final String configGroup,
                                           final boolean sortByTitles) {
        super(environment, parent, null);
        setAttribute(Qt.WidgetAttribute.WA_DeleteOnClose, true);
        abstractClassFont = ExplorerFont.Factory.getDefault().changeStyle(EFontStyle.ITALIC);
        settingsGroupName = DLG_SETTINGS_GROUP_NAME+"/"+configGroup;
        this.sortByTitles = sortByTitles;
        final MessageProvider mp = environment.getMessageProvider();
        setWindowTitle(mp.translate("SelectInstantiatableClass", "Select Class of New Object"));
                
        final QHBoxLayout ltFilter = new QHBoxLayout();
        final QLabel lbFilter = new QLabel(mp.translate("SelectInstantiatableClass", "Filter:"), this);
        final QLineEdit leFilter = new QLineEdit(this);
        ltFilter.addWidget(lbFilter);
        ltFilter.addWidget(leFilter);
        leFilter.textEdited.connect(this, "applyFilter(String)");
        dialogLayout().addLayout(ltFilter);

        tree = new QTreeWidget(this);
        layout().addWidget(tree);
        tree.setColumnCount(1);
        tree.header().hide();
        tree.currentItemChanged.connect(this, "onCurrentItemChanged()");        
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
        addButtons(EnumSet.of(EDialogButtonType.OK,EDialogButtonType.CANCEL),true);        
        tree.itemDoubleClicked.connect(this, "onDoubleClick(QTreeWidgetItem)");
        tree.itemExpanded.connect(this,"onExpandItem(QTreeWidgetItem)");
        tree.itemCollapsed.connect(this,"onCollapseItem(QTreeWidgetItem)");
        getButton(EDialogButtonType.OK).setEnabled(false);
    }
    
    private QTreeWidgetItem fillTree(final InstantiatableClass currentClass, 
                                     final QTreeWidgetItem parentItem,
                                     final List<InstantiatableClass> expandedItems){
        final QTreeWidgetItem item = createTreeWidgetItemForClass(currentClass, parentItem);
        final List<InstantiatableClass> derivedClasses;
        if (sortByTitles){
            derivedClasses = InstantiatableClass.sortByTitles(currentClass.getDerivedClasses());
        }else{
            derivedClasses = currentClass.getDerivedClasses();
        }        
        for (InstantiatableClass cl: derivedClasses){
            fillTree(cl, item, expandedItems);
        }
        if (derivedClasses.size()>0 && currentClass.containsIn(expandedItems)){
            tree.expandItem(item);
            expandedClasses.add(currentClass);
        }
        return item;
    }        
    
    private QTreeWidgetItem createTreeWidgetItemForClass(final InstantiatableClass instantiatableClass, final QTreeWidgetItem parentItem){
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
        return treeItem;
    }
    
    private static InstantiatableClass getClass(final QTreeWidgetItem treeItem){
        return treeItem==null ? null : (InstantiatableClass)treeItem.data(0, Qt.ItemDataRole.UserRole);
    }
    
    private static boolean isAbstractClass(final QTreeWidgetItem treeItem){
        return getClass(treeItem).isAbstractClass();
    }

    @SuppressWarnings("unused")
    private void onCurrentItemChanged() {
        currentClass = getClass(tree.currentItem());
        getButton(EDialogButtonType.OK).setEnabled(currentClass != null && !currentClass.isAbstractClass());
    }

    @SuppressWarnings("unused")
    private void onDoubleClick(final QTreeWidgetItem item) {
        if (item != null && !isAbstractClass(item)) {
            accept();
        }
    }

    @Override
    public void done(final int result) {
        writeExpandedItems();
        super.done(result);
    }        

    @Override
    public InstantiatableClass getCurrentClass() {
        return currentClass;
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
    private void applyFilter(final String filter){
        setUpdatesEnabled(false);
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
}