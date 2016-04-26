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

package org.radixware.kernel.explorer.editors.profiling;

import com.trolltech.qt.core.Qt.CheckState;
import com.trolltech.qt.core.Qt.SortOrder;
import com.trolltech.qt.gui.QTreeWidgetItem;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.client.meta.RadSortingDef;
import org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.GroupModelReader;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.exceptions.ServiceClientException;


public class ProfileTree extends AbstractProfileTree {
    private GroupModel sourceGroup;
    private GroupModel sumSourceGroup;
    private final static int rowCount = 50;
    private final static int checkedColum = 0;
    private int entityIndex = 0;
    private int maxStrPeriodLength = 0;

    @SuppressWarnings("LeakingThisInConstructor")
    ProfileTree(final ProfilerWidget parent,final boolean isPercentMode) {
        super(parent,isPercentMode);
        sumSourceGroup = parent.getSummarySourceGroup();
        tree.itemExpanded.connect(this, "onExpandTreeItem(QTreeWidgetItem)");
        tree.verticalScrollBar().valueChanged.connect(this, "treeScrolled(int)");
        tree.itemChanged.connect(this, "onChangedTreeItem(QTreeWidgetItem,Integer)");
        tree.header().sortIndicatorChanged.connect(this,"onSortTreeItem(Integer,com.trolltech.qt.core.Qt.SortOrder)");
    }
    
    @SuppressWarnings("unused")
    private void onSortTreeItem(final Integer column,final com.trolltech.qt.core.Qt.SortOrder sortOrder){
        if(column==0 && propPeriodId!=null){
            //loadMoreSections(null, true);
            try{
                RadSortingDef.SortingItem.SortOrder order = sortOrder==SortOrder.AscendingOrder?
                                                            RadSortingDef.SortingItem.SortOrder.ASC:
                                                            RadSortingDef.SortingItem.SortOrder.DESC;
                RadSortingDef.SortingItem sortingItem=new RadSortingDef.SortingItem(propPeriodId,order);
                List<RadSortingDef.SortingItem> sortItems=new ArrayList<>();
                sortItems.add(sortingItem);                    
                RadSortingDef sorting=sourceGroup.getSortings().findSortingByColumns(sortItems, null);

                sourceGroup.setSorting(sorting);
                editor.reread();
            }catch (Exception ex) {
                sourceGroup.showException(editor.getEnvironment().getMessageProvider().translate("Selector", "Can't Apply Sorting"), ex);
            }
        }
    }

    public void update(final GroupModel sourceGroup,final List<Long> selectedInstances) {
        entityIndex = 0;
        fullPeriodList.clear();
        this.sourceGroup = sourceGroup;
        this.selectedInstances = selectedInstances;
        List<TreeItem> items = createPeriodList(null,false);
        if (items != null) {
            fillTree(items);
        }
    }

    public void filterByInstances(final List<Long> selectedInstances) {
        this.selectedInstances = selectedInstances;
        final List<TreeItem> checkedItems = getSelectedItems();
        final List<TreeItem> filteredPeriodList = new ArrayList<>();        
       
        clear();
        for (int i = 0; i < fullPeriodList.size(); i++) {
            TreeItem item = (TreeItem) fullPeriodList.get(i).clone();
            if (isInSelectedInstance(item.getInstanceId())) {
                filteredPeriodList.add(item);
            }
        }
        fillTree(checkedItems, filteredPeriodList);
        if (filteredPeriodList.size() <= rowCount) {            
            loadMoreSections(null,false);            
               
        }        
        if (!hasCheckedItems()) {
            editor.canRemoveItems(false);
        }
        
        if (tree.topLevelItemCount()>0){
            tree.setCurrentItem(tree.topLevelItem(0));
        }
    }

    public List<TreeItem> getItemsForSummaryMode() {
        final List<TreeItem> items = new ArrayList<>();
        if (sumSourceGroup!=null) {
            final GroupModelReader sumSourceGroupReader = new GroupModelReader(sumSourceGroup);
            for (EntityModel entity: sumSourceGroupReader){                        
                Long instanceId = (Long) getPropValue(entity, propInstanceId);
                Timestamp timePeriod = (Timestamp) getPropValue(entity, propPeriodId);

                TreeItem topItem = new TreeItem(timePeriod.toString(), timePeriod, instanceId, entity, isPercentMode);
                //String strPeriod=topItem.text(0);
                //if((!strPeriod.isEmpty())&&(maxStrPeriodLength<strPeriod.length()))
                //    maxStrPeriodLength=strPeriod.length();
                topItem.addChild(new TreeItem("", instanceId, isPercentMode));
                //fullPeriodList.add((TreeItem)topItem.clone());
                if (isInSelectedInstance(instanceId)) {
                    items.add(topItem);
                }
            }
            if (sumSourceGroupReader.wasException()){
                sourceGroup.showException(sumSourceGroupReader.getServiceClientException());
            }
        }        
        return items;
    }

    private List<TreeItem> createPeriodList(final Timestamp to,final boolean loadALL) {
        List<TreeItem> items = new ArrayList<>();
        if ((sourceGroup != null) && (entityIndex >= 0)) {
            while (((items.size() <= rowCount) && (to == null)) || (to != null) || loadALL) {
                try {
                    final EntityModel entity;
                    try{
                        entity = sourceGroup.getEntity(entityIndex);
                    }catch(BrokenEntityObjectException exception){
                        entityIndex++;//ignoring broken entity
                        continue;
                    }
                    if (entity != null) {
                        Long instanceId = (Long) getPropValue(entity, propInstanceId);
                        Timestamp timePeriod = (Timestamp) getPropValue(entity, propPeriodId);

                        TreeItem topItem = new TreeItem(timePeriod.toString(), timePeriod, instanceId, entity, isPercentMode);
                        String strPeriod = topItem.text(0);
                        if ((!strPeriod.isEmpty()) && (maxStrPeriodLength < strPeriod.length())) {
                            maxStrPeriodLength = strPeriod.length();
                        }
                        topItem.addChild(new TreeItem("", instanceId, isPercentMode));
                        fullPeriodList.add((TreeItem) topItem.clone());
                        if (isInSelectedInstance(instanceId)) {
                            items.add(topItem);
                        }
                        entityIndex++;
                        if ((to != null) && (timePeriod.after(to))) {
                            break;
                        }
                    } else {
                        entityIndex = -1;
                        break;
                    }
                } catch (ServiceClientException ex) {
                    sourceGroup.showException(ex);
                } catch (InterruptedException ex) {
                    return null;
                }
            }
        }
        return items;
    }

    @SuppressWarnings("unused")
    private void onChangedTreeItem(final QTreeWidgetItem item, final Integer column) {
        if (column == checkedColum) {
            if ((item.checkState(column) == CheckState.Checked)) {
                editor.canRemoveItems(true);
            } else if (!hasCheckedItems()) {
                editor.canRemoveItems(false);
            }
        }
    }

    @SuppressWarnings("unused")
    private void onExpandTreeItem(final QTreeWidgetItem index) {
        if ((index != null) && (index.parent() == null) && (!((TreeItem) index).isCalculated())) {
            tree.setUpdatesEnabled(false);
            index.takeChildren();
            TreeItem item = (TreeItem) index.clone();
            expandItem(item);
            index.addChildren(item.takeChildren());
            ((TreeItem) index).setIsCalced(true);
            hidhlidhtChildItems((TreeItem) index);
            filterByPercent((TreeItem) index);
            tree.setUpdatesEnabled(true);
        }
    }

    private void expandItem(final TreeItem item) {
        List<TreeItem> items = new ArrayList<>();
        items.add(item);
        buildTree(items);
    }

    private void hidhlidhtChildItems(final TreeItem parent) {
        TreeItem flatListItem = null;
        TreeItem contextList = null;
        TreeItem treeList = null;
        for (int i = 0; i < parent.childCount(); i++) {
            TreeItem childItem = (TreeItem) parent.child(i);
            if (childItem.getName().equals(STR_FLAT_LIST_NODE)) {
                flatListItem = childItem;
            } else if (childItem.getName().equals(STR_CONTEXT_TREE_NODE)) {
                contextList = childItem;
            } else if (childItem.getName().equals(STR_TREE_NODE)) {
                treeList = childItem;
            }
        }
        highlightItems(flatListItem, contextList, treeList);
    }

    @SuppressWarnings("unused")
    private void treeScrolled(final int min) {
        if ((tree.verticalScrollBar().maximum() > 0) && (min >= tree.verticalScrollBar().maximum()) && (entityIndex >= 0)) {
            loadMoreSections(null,false);  
            tree.scrollToItem(tree.topLevelItem(tree.topLevelItemCount() - 2)); 
        }
    }

    public void loadMoreSections(Timestamp to,boolean loadAll) {
        List<TreeItem> items = createPeriodList(to,loadAll);
        if (items != null) {
            fillTree(null, items);
        }
    }

    private void fillTree(List<TreeItem> itemList) {
        tree.clear();
        fillTree(null, itemList);
    }

    private void fillTree(List<TreeItem> checkedItems, List<TreeItem> itemList) {
        for (int i = 0; i < itemList.size(); i++) {
            TreeItem item = itemList.get(i);
            item.setCheckState(checkedColum, getItemCheckState(checkedItems, item));
            tree.addTopLevelItem(item);
            //if(curItem!=null && item.equals(curItem))
            //    tree.setCurrentItem(item);
        }
        resizeColumns();
    }

    private CheckState getItemCheckState(List<TreeItem> checkedItems, TreeItem item) {
        return (checkedItems != null && checkedItems.contains(item)) ? CheckState.Checked : CheckState.Unchecked;
    }

    public void removeSelectedItems(boolean isDeleteFromDb) {
        for (int i = 0; i < tree.topLevelItemCount(); i++) {
            TreeItem topItem = (TreeItem) tree.topLevelItem(i);
            if (topItem.checkState(checkedColum) == CheckState.Checked) {
                if (!topItem.isCalculated()) {
                    expandItem(topItem);
                }
                try {
                    EntityModel entity = topItem.getEntity();
                    //Id childId = entity.getEditorPresentationDef().getChildrenExplorerItems().iterator().next().getId();
                    Model childModel = entity.getChildModel(editor.getIdsGetter().getProfilerLogChildId());
                    if (childModel instanceof GroupModel) {
                        ((GroupModel) childModel).deleteAll(true);
                    }
                    entity.delete(true);
                    removeItemFromFlatList(topItem);
                    tree.takeTopLevelItem(i);
                    i--;
                } catch (ServiceClientException ex) {
                    sourceGroup.showException(ex);
                } catch (InterruptedException ex) {
                    return;
                }
            }
        }
    }

    private boolean removeItemFromFlatList(TreeItem removingItem) {
        return fullPeriodList.remove(removingItem);
    }

    private boolean hasCheckedItems() {
        for (int i = 0; i < tree.topLevelItemCount(); i++) {
            QTreeWidgetItem topItem = tree.topLevelItem(i);
            if (topItem.checkState(checkedColum) == CheckState.Checked) {
                return true;
            }
        }
        return false;
    }

    public List<TreeItem> getSelectedItems() {
        List<TreeItem> res = new ArrayList<>();
        for (int i = 0; i < tree.topLevelItemCount(); i++) {
            TreeItem topItem = (TreeItem) tree.topLevelItem(i);
            if (topItem.checkState(checkedColum) == CheckState.Checked) {
                res.add((TreeItem) topItem.clone());
            }
        }
        return res;
    }
    
    public String toHtml(boolean isTreeMode, boolean isFlatListMode, boolean isContextTreeMode, List<Integer> columns) {
        final StringBuilder htmlBuilder = new StringBuilder(64);
        QTreeWidgetItem curItem=getCurrentItem();
        if(curItem!=null && curItem instanceof TreeItem){
                TreeItem item=(TreeItem)curItem;
                TreeItem periodItem=getPeriodItem(item); 
                if(periodItem!=null){                    
                   /* String header = "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />";
                    header+="<style>\ntable {\nwidth: 100%;\nborder-spacing: 0;\n" +
                            "   }\n   tr:nth-child(6n+1) {\n" +
                            "    background: #F2F2F2;\n" +
                            "   }\n" +
                            "   tr:nth-child(6n+2) {\n" +
                            "    background: #E8E8E8;\n" +
                            "   }\n" +
                            "   tr:nth-child(6n+3) {\n" +
                            "    background: #DEDEDE;\n" +
                            "   }\n" +
                            "   tr:nth-child(6n+4) {\n" +
                            "    background: #D4D4D4;\n" +
                            "   }\n" +
                            "   tr:nth-child(6n+5) {\n" +
                            "    background: #CACACA;\n" +
                            "   }\n" +
                            "   tr:nth-child(6n+6) {\n" +
                            "    background: #C0C0C0;\n" +
                            "   } \n" +
                            "  </style>";
                    header += Application.translate("ProfilerDialog", "Profiling Journal for ");
                    header += (periodItem.equals(item))? periodItem.text(TreeItem.TITLE_COL_INDEX) : periodItem.text(TreeItem.TITLE_COL_INDEX)+" -> "+item.text(TreeItem.TITLE_COL_INDEX);
                    
                    res += "<html><head>" + header + "</head><body>";*/
                    //res += "<table><thead><tr>";       
        
                    //int visibleColumnCount = 0;
                    htmlBuilder.append("<table cellspacing=\"0\"><thead><tr>");
                    for (int i = 0; i <columns.size(); i++) {
                        int columnIndex=columns.get(i);
                        htmlBuilder.append("<th>");
                        htmlBuilder.append(tree.headerItem().text(columnIndex));
                        htmlBuilder.append("</th>");                        
                        //visibleColumnCount++;
                    }
                    htmlBuilder.append("</tr></thead><tr>");
                    if(!periodItem.isCalculated())
                         onExpandTreeItem(periodItem);
                    
                    htmlBuilder.append(item.itemToHtml( strTab, columns));
                    htmlBuilder.append("<tr></table>");
                }
        }
        return htmlBuilder.toString();
    }
}
