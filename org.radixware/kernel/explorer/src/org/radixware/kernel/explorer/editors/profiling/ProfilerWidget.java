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


import com.trolltech.qt.core.Qt.SortOrder;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.explorer.editors.profiling.dialogs.ChoosePeriod;
import com.trolltech.qt.gui.QTreeWidget;
import com.trolltech.qt.gui.QTreeWidgetItem;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.GroupModelReader;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.items.ModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.widgets.actions.IMenu;
import org.radixware.kernel.common.client.widgets.actions.IToolBar;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.widgets.ExplorerWidget;
import org.radixware.kernel.explorer.widgets.selector.IExplorerSelectorWidget;


public class ProfilerWidget extends ExplorerWidget implements IExplorerSelectorWidget {

    private ProfileTree tree;
    private ToolBar toolBar;
    private final GroupModel sourceGroup;
    private final GroupModel sumSourceGroup;
    private TabWidget tabWidget;
    private List<SummaryTree> summaryTrees = new ArrayList<>();
    private Map<Long, String> instances = new HashMap<>();
    private RadEnumPresentationDef timeSectionEnum = null;
    private final ProfilerIdsGetter idsGetter;

    public static enum EPropertyName {
        NAME, PERIOD, CONTEXT, DURATION, MIN_DURATION, MAX_DURATION, COUNT, INSTANCE, FROM, TO
    }

    public interface ProfilerIdsGetter {

        Id getTimeSectionEnumId();

        Id getPeriodsViewClassId();

        Id getProfilerLogClassId();

        Id getProfilerLogAllChildId();

        Id getProfilerLogChildId();

        Id getSummaryModeSelectorPresentId();

        Id getPropertyIdByName(EPropertyName propName);

        Id getDomainCpuId();

        Id getDomainDbId();

        Id getDomainExtId();
    }

    public static final class ProfilEditorIcons extends ExplorerIcon.CommonOperations {

        private ProfilEditorIcons(final String fileName) {
            super(fileName, true);
        }
        public static final ProfilEditorIcons PERSENT_MODE = new ProfilEditorIcons("classpath:images/percent.svg");
        public static final ProfilEditorIcons SUM_MODE = new ProfilEditorIcons("classpath:images/summary_tree.svg");
        public static final ProfilEditorIcons DEL_ITEMS = new ProfilEditorIcons("classpath:images/del_elements.svg");
        public static final ProfilEditorIcons CHECK_ALL = new ProfilEditorIcons("classpath:images/checkbox_checked.svg");
        public static final ProfilEditorIcons UNCHECK_ALL = new ProfilEditorIcons("classpath:images/checkbox_unchecked.svg");
    }

    public GroupModel getSummarySourceGroup() {
        return sumSourceGroup;
    }

    public ProfilerWidget(final QWidget parent, final GroupModel sourceGroup, final ProfilerIdsGetter idsGetter) {
        super(sourceGroup.getEnvironment(), parent);
        this.idsGetter = idsGetter;
        this.sourceGroup = sourceGroup;
        Id periodsViewClassId = idsGetter.getPeriodsViewClassId();
        Id summaryModeSelectorPresentId = idsGetter.getSummaryModeSelectorPresentId();
        if (periodsViewClassId != null && summaryModeSelectorPresentId != null) {
            this.sumSourceGroup = GroupModel.openTableContextlessSelectorModel(getEnvironment(), periodsViewClassId, summaryModeSelectorPresentId);
        } else {
            this.sumSourceGroup = null;
        }
        QVBoxLayout mainlayout = new QVBoxLayout();
        mainlayout.setMargin(0);

        if (idsGetter.getTimeSectionEnumId() != null) {
            timeSectionEnum = getEnvironment().getApplication().getDefManager().getEnumPresentationDef(idsGetter.getTimeSectionEnumId());
        }
        // if (timeSectionEnum==null)
        //     throw new AppError("Can`t find  time section enumeration");
        tabWidget = new TabWidget(this,sourceGroup.getEnvironment());
        tabWidget.onCloseTab.connect(this, "removeSummaryTree(Integer)");
        tabWidget.onLastTabRemain.connect(this, "hideTabs()");
        tabWidget.currentChanged.connect(this, "currentTabChanged()");

        tree = new ProfileTree(this,true);
        tree.getTree().currentItemChanged.connect(this, "currentTabChanged()");
        toolBar = new ToolBar(this);

        mainlayout.addWidget(toolBar.getToolBar());
        mainlayout.addWidget(tree.getTree());
        this.setLayout(mainlayout);
    }

    public ProfilerIdsGetter getIdsGetter() {
        return idsGetter;
    }

    public void open() {
        tree.setPropIds();
        tree.setDomainIds();
        tree.update(sourceGroup, null);        
        tree.getTree().sortByColumn(0, SortOrder.DescendingOrder);
        if (tree.getTree().topLevelItemCount() > 0) {
            tree.getTree().setCurrentItem(tree.getTree().topLevelItem(0));
        }
        toolBar.setToolBarEnable(!tree.isTreeEmpty());
    }

    public void setInstanceList(Id classId, Id presentId, Id propId) {
        GroupModel group = GroupModel.openTableContextlessSelectorModel(getEnvironment(), classId, presentId);
        final GroupModelReader instancesReader = 
            new GroupModelReader(group,EnumSet.of(GroupModelReader.EReadingFlags.SHOW_SERVICE_CLIENT_EXCEPTION));
        for (EntityModel instanceModel: instancesReader) {
            Long instanceId = (Long) instanceModel.getProperty(propId).getValueObject();
            String instanceTitle = instanceModel.getTitle();
            instances.put(instanceId, instanceTitle);
        }
    }

    public RadEnumPresentationDef getTimeSectionEnum() {
        return timeSectionEnum;
    }

    private void showTabs() {
        this.layout().removeWidget(tree.getTree());
        tabWidget.addTab(tree.getTree(), Application.translate("ProfilerDialog", "All sections"));
        this.layout().addWidget(tabWidget);
        tabWidget.setVisible(true);
    }

    public void hideTabs() {
        for (int i = tabWidget.count() - 1; i >= 0; i--) {
            tabWidget.removeTab(i);
        }
        this.layout().removeWidget(tabWidget);
        tabWidget.setParent(null);
        this.layout().addWidget(tree.getTree());
        tree.getTree().setVisible(true);
    }
     
    public void updateSummaryModeTabs(List<Long> instances) {
        for (int i = 0; i < summaryTrees.size(); i++) {
            SummaryTree sumModeTree = summaryTrees.get(i);
            List<TreeItem> items = findItems(sumModeTree.getFrom(), sumModeTree.getTo());
            if (items == null) {
                items = findItems(sumModeTree.getPeriodList());
            }
            sumModeTree.update(items, instances);
        }
    }

    public void removeSummaryTree(Integer index) {
        summaryTrees.remove(index.intValue());
    }

    public void chageMode() {
        List<TreeItem> items = tree.getSelectedItems();
        if ((items == null) || (items.isEmpty())) {
            ChoosePeriod choosePeriod = new ChoosePeriod(this, tree.getCurrentPeriod());
            if (choosePeriod.exec() == 1) {
                Timestamp from = choosePeriod.getPeriodFrom();
                Timestamp to = choosePeriod.getPeriodTo();
                items = findItems(from, to);
                String title = "";
                if (from != null && to != null) {
                    if (from.equals(to)) {
                        title = from.toString();
                    } else {
                        title = "From " + from.toString() + " to " + to.toString();
                    }
                } else if (from != null) {
                    title = from.toString();
                } else if (to != null) {
                    title = to.toString();
                }
                SummaryTree sumModeTree = new SummaryTree(items, this, from, to, tree.isPercentMode());
                sumModeTree.setPropIdsFrom(tree);
                sumModeTree.update(items, null);
                sumModeTree.getTree().sortByColumn(0, SortOrder.DescendingOrder);
                addTab(sumModeTree, title);
            }
        } else {
            String title = getPeriodsName(items);
            SummaryTree sumModeTree = new SummaryTree(items, this, tree.isPercentMode());
            sumModeTree.setPropIdsFrom(tree);
            sumModeTree.update(items, null);
            sumModeTree.getTree().sortByColumn(0, SortOrder.DescendingOrder);
            addTab(sumModeTree, title);
        }
    }

    private void addTab(SummaryTree sumModeTree, String title) {
        if (tabWidget.count() == 0) {
            showTabs();
        }
        String tabName = Application.translate("ProfilerDialog", "Summary")+": " + title;
        tabWidget.addTab(sumModeTree.getTree(), getCutText(tabName));
        tabWidget.setTabToolTip(tabWidget.count() - 1, title);
        tabWidget.setCurrentIndex(tabWidget.count() - 1);
        summaryTrees.add(sumModeTree);
    }

    @SuppressWarnings("unused")
    private void currentTabChanged() {
        boolean isEnableSaveToFile=(tabWidget.currentIndex() > 0) ||((tabWidget.currentIndex() <= 0) &&(tree.getCurrentItem()!=null));
        toolBar.setSaveToFileEnabled(isEnableSaveToFile);
    }

    protected void setPropValue(EntityModel entity, Id propId, Object val) {
        entity.getProperty(propId).setValueObject(val);
    }

    private List<TreeItem> findItems(Timestamp from, Timestamp to) {
        List<TreeItem> sumItems = null;
        if ((sumSourceGroup != null) && (from != null || to != null)) {
            //sumItems = new ArrayList<>();
            if (from == null) {
                from = to;
            }
            if (to == null) {
                to = from;
            }
            sumSourceGroup.getProperty(idsGetter.getPropertyIdByName(EPropertyName.FROM)).setValueObject(from);
            sumSourceGroup.getProperty(idsGetter.getPropertyIdByName(EPropertyName.TO)).setValueObject(to);
            try {
                sumSourceGroup.reread();
            } catch (ServiceClientException ex) {
                sumSourceGroup.showException(ex);
            }

            /*TaskWaiter waiter = new TaskWaiter();
            waiter.setMessage(Environment.translate("ProfilerDialog","Build Summary Tree..."));
            final Callable<List<TreeItem>>  task =  new SummaryItemsGeter(propFromId,propToId,tree.getSelectedInstanceIds(), sumSourceGroup);
            try {
            sumItems=waiter.runAndWait(task);
            } catch (InterruptedException ex) {
            Logger.getLogger(SummaryTree.class.getName()).log(Level.SEVERE, null, ex);
            }catch (ExecutionException ex) {
            Logger.getLogger(SummaryTree.class.getName()).log(Level.SEVERE, null, ex);
            }finally{
            waiter.close();
            }*/
            sumItems = tree.getItemsForSummaryMode();
        }
        return sumItems;
    }

    /* private List<TreeItem> findItems(Timestamp from,Timestamp to){
    List<TreeItem> res=null;
    int treeItemCount=tree.getTree().topLevelItemCount();
    if((from!=null || to!=null) && (treeItemCount>0)){
    res=new ArrayList<TreeItem>();
    if(from==null) from=to;
    if(to==null) to=from;
    TreeItem lastItem=(TreeItem)tree.getTree().topLevelItem(treeItemCount-1).clone();
    if(lastItem.getPeriod().before(to)){
    tree.loadMoreSections(to);
    treeItemCount=tree.getTree().topLevelItemCount();
    }
    for(int i=0;i<treeItemCount;i++){
    TreeItem topItem=(TreeItem)tree.getTree().topLevelItem(i).clone();
    if((topItem.getPeriod().after(from)||(topItem.getPeriod().equals(from))) &&
    (topItem.getPeriod().before(to)||(topItem.getPeriod().equals(to)))){
    res.add((TreeItem)topItem.clone());
    }
    }
    }
    return res;
    }*/
    private List<TreeItem> findItems(List<TreeItem> items) {
        List<TreeItem> res = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            for (int j = 0; j < tree.getTree().topLevelItemCount(); j++) {
                TreeItem item = (TreeItem) tree.getTree().topLevelItem(j).clone();
                if (items.get(i).equals(item)) {
                    res.add(item);
                    break;
                }
            }
        }
        return res;
    }

    private String getPeriodsName(List<TreeItem> items) {
        String res = "";
        for (int i = 0; i < (items.size() - 1); i++) {
            res += items.get(i).getName() + "; ";
        }
        res += items.get(items.size() - 1).getName();
        return res;
    }

    private String getCutText(String title) {
        if (title.length() > 40) {
            title = title.substring(0, 40) + "...";
        }
        return title;
    }

    public void canRemoveItems(boolean hasCheckedItems) {
        toolBar.setRemoveSelectionEnable(hasCheckedItems);
    }

    public void clearTree() {
        try {
            if (tree.getTree().topLevelItemCount() > 0) {
                TreeItem item = (TreeItem) tree.getTree().topLevelItem(0);
                EntityModel entity = item.getEntity();
                if (entity != null) {
                    Model childModel = entity.getChildModel(idsGetter.getProfilerLogAllChildId());
                    if (childModel instanceof GroupModel) {
                        ((GroupModel) childModel).reread();
                        ((GroupModel) childModel).deleteAll(true);
                    }
                }
            }

            /* sourceGroup.reread();
            EntityModel childEntity =  sourceGroup.getEntity(i);
            while(childEntity!=null || sourceGroup.hasMoreRows()){
            if(childEntity!=null){
            Iterator iter = childEntity.getEditorPresentationDef().getChildrenExplorerItems().iterator();
            if ( iter.hasNext()) {
            Id childId = childEntity.getEditorPresentationDef().getChildrenExplorerItems().iterator().next().getId();
            Model childModel = childEntity.getChildModel(childId);
            if (childModel instanceof GroupModel) {
            System.out.println(((GroupModel) childModel).deleteAll(true) + " = "+ i);
            }
            }
            }
            i++;
            childEntity = sourceGroup.getEntity(i);
            }*/

            tree.clear();
            tree.getPeriodList().clear();
            if (tabWidget.count() >= 1) {
                hideTabs();
            }
            toolBar.setToolBarEnable(false);
        } catch (ServiceClientException ex) {
            sourceGroup.showException(ex);
        } catch (InterruptedException ex) {
        }
    }

    public void removeItems(boolean isDeleteFromDb) {
        tree.removeSelectedItems(isDeleteFromDb);
        toolBar.setToolBarEnable(!tree.isTreeEmpty());
        updateSummaryModeTabs(getSelectedInstanceIds());
    }

    public Map<Long, String> getInstances() {
        return instances;
    }

    public Double getPercentFilter() {
        return tree.getPercentFilter();
    }

    public List<Long> getSelectedInstanceIds() {
        return tree.getSelectedInstanceIds();
    }

    public void setSelectedInstanceIds(List<Long> instances) {
        if (!isEqualsSelectedInstanceIdsLists(getSelectedInstanceIds(), instances)) {
            tree.filterByInstances(instances);
            updateSummaryModeTabs(instances);
            toolBar.setToolBarEnable(!tree.isTreeEmpty());
        }
    }

    public void setPercentFilter(Double persentFilter) {
        tree.filterByPersent(persentFilter, false);
        for (int i = 0; i < summaryTrees.size(); i++) {
            summaryTrees.get(i).filterByPersent(persentFilter, true);
        }
    }

    private boolean isEqualsSelectedInstanceIdsLists(List<Long> oldInstances, List<Long> newInstances) {
        if ((oldInstances == null) && (newInstances == null)) {
            return true;
        } else if (((oldInstances == null) && (newInstances != null)) || ((oldInstances != null) && (newInstances == null))) {
            return false;
        } else if ((oldInstances.size() == newInstances.size()) && (oldInstances.containsAll(newInstances))) {
            return true;
        }
        return false;
    }

    public void saveToFile(String filename, boolean isTreeMode, boolean isFlatListMode, boolean isContextTreeMode, List<Integer> columns) {
        if (filename == null) {
            return;
        }
        try {
            String string = createHtmlDoc(isTreeMode, isFlatListMode, isContextTreeMode, columns);
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "UTF-8"));
            out.write(string);
            out.close();
        } catch (FileNotFoundException e) {
            String title = Application.translate("ExplorerDialog", "Error!");
            String message = Application.translate("ExplorerDialog", "File \'%s\' not found");
            Application.messageException(title, String.format(message, filename), e);
            Logger.getLogger(ProfilerWidget.class.getName()).log(Level.SEVERE, null, e);
        } catch (IOException ex) {
            String title = Application.translate("ExplorerDialog", "Error!");
            String message = Application.translate("ExplorerDialog", "IOException");
            Application.messageException(title, String.format(message, filename), ex);
            Logger.getLogger(ProfilerWidget.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

     private String createHtmlDoc(boolean isTreeMode, boolean isFlatListMode, boolean isContextTreeMode, List<Integer> columns) {
        String res = "";
        int curSummaryTreeIndex = tabWidget.currentIndex() - 1;
        String header = "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />";
        header+="<style>\ntable {\nwidth: 100%;\nborder-spacing: 0;border-collapse: collapse;\n" +
                        //"   frame: below; rule: \n" +
                        "   }\n   " +
                        "   td {\n" +
                        "    white-space: nowrap;\n" +
                        "   }\n " +
                        "   tr:nth-child(6n+2) {\n" +
                        "    background: #F2F2F2;\n" +
                        "   }\n" +
                        "   tr:nth-child(6n+3) {\n" +
                        "    background: #E8E8E8;\n" +
                        "   }\n" +
                        "   tr:nth-child(6n+4) {\n" +
                        "    background: #DEDEDE;\n" +
                        "   }\n" +
                        "   tr:nth-child(6n+5) {\n" +
                        "    background: #D4D4D4;\n" +
                        "   }\n" +
                        "   tr:nth-child(6n+6) {\n" +
                        "    background: #CACACA;\n" +
                        "   }\n" +
                        "   tr:nth-child(6n+7) {\n" +
                        "   background: #C0C0C0;\n" +
                        "   } \n" +
                        "   tr:nth-child(6n+7) td{\n" +
                        "   border-bottom: 1px solid black;"+
                        "   } \n" +
                        "  </style>";
        if (curSummaryTreeIndex >= 0) {
            SummaryTree curSummaryTree = summaryTrees.get(curSummaryTreeIndex);           
            header += Application.translate("ProfilerDialog", "Profiling Journal. Summary Mode")+" (" + tabWidget.tabToolTip(tabWidget.currentIndex()) + ")\n";
            res += "<html><head>" + header + "</head><body>";
            res += curSummaryTree.toHtml(isTreeMode, isFlatListMode, isContextTreeMode, columns);
            res += "</body></html>";
        }else { 
            QTreeWidgetItem curItem=tree.getCurrentItem();
            if(curItem!=null && curItem instanceof TreeItem){
                TreeItem item=(TreeItem)curItem;
                TreeItem periodItem=tree.getPeriodItem(item); 
                if(periodItem!=null){   
                    header += Application.translate("ProfilerDialog", "Profiling Journal for ");
                    header += (periodItem.equals(item))? periodItem.text(TreeItem.TITLE_COL_INDEX) : periodItem.text(TreeItem.TITLE_COL_INDEX)+" -> "+item.text(TreeItem.TITLE_COL_INDEX);
                    res += "<html><head>" + header + "</head><body>";
                    res+=tree.toHtml(isTreeMode, isFlatListMode, isContextTreeMode, columns);
                    res += "</body></html>";
                }
            }
        }
        return res;
    }

    @Override
    public void lockInput() {
    }

    @Override
    public void unlockInput() {
    }

    @Override
    public void finishEdit() {
    }

    @Override
    public void rereadAndSetCurrent(Pid pid) {
        QTreeWidgetItem curItem = tree.getCurrentItem();
        if (pid == null) {
            curItem = tree.getItemByPid(pid);
        }
        reread();
        tree.setCurrentItem(curItem);
    }

    @Override
    public void reread() {
        tree.clear();
        sourceGroup.reset();
        tree.update(sourceGroup, tree.getSelectedInstanceIds());
        boolean isShowSummaryEnable = tree.getTree().topLevelItemCount() > 0;
        toolBar.setToolBarEnable(isShowSummaryEnable);
        if (isShowSummaryEnable) {
            updateSummaryModeTabs(tree.getSelectedInstanceIds());
        } else {
            hideTabs();
        }
    }

    @Override
    public void clear() {
        clearTree();
    }

    @Override
    public void entityRemoved(Pid pid) {
        //QTreeWidgetItem item=tree.getItemByPid(pid);
        //tree.removeItem(item);
    }

    @Override
    public void setupSelectorMenu(IMenu menu) {
    }

    @Override
    public void setupSelectorToolBar(IToolBar toolBar) {
    }

    @Override
    public void afterPrepareCreate(EntityModel childEntity) {
    }

    @Override
    public void bind() {
    }

    @Override
    public void refresh(ModelItem changedItem) {
    }

    @Override
    public boolean setFocus(Property property) {
        return false;
    }

    @Override
    public QWidget asQWidget() {
        return this;
    }

    public Map<String, Boolean> getColumnsForCurTree() {
        Map<String, Boolean> columns = new LinkedHashMap<>();
        QTreeWidget currentTree = getCurTree().getTree();
        QTreeWidgetItem headerItem = currentTree.headerItem();
        for (int i = 1; i < headerItem.columnCount(); i++) {
            columns.put(headerItem.text(i), !currentTree.isColumnHidden(i));
        }
        return columns;
    }
    
    public List<ColumnHeaderInfo> getHeaderItem() {
        List<ColumnHeaderInfo> columnsInfo = new LinkedList<>();
        QTreeWidget currentTree = getCurTree().getTree();
        QTreeWidgetItem headerItem = currentTree.headerItem();
        for (int i = 1; i < headerItem.columnCount(); i++) {
            String text=headerItem.text(i);
            String toolTip=headerItem.toolTip(i);
            boolean isColumnHidden=currentTree.isColumnHidden(i);
            ColumnHeaderInfo columnHeaderInfo=new ColumnHeaderInfo(text, toolTip, isColumnHidden);
            columnsInfo.add(columnHeaderInfo);
        }
        return columnsInfo;
    }

    public AbstractProfileTree getCurTree() {
        int tabIndex = tabWidget.currentIndex();
        if (tabIndex <= 0) {
            return tree;
        }
        return summaryTrees.get(tabIndex - 1);
    }

    public void setClassificationMode(boolean isPercentMode) {
        tree.setClassificationMode(isPercentMode);
        for (int i = 0; i < summaryTrees.size(); i++) {
            summaryTrees.get(i).setClassificationMode(isPercentMode);
        }
    }
    
    public class ColumnHeaderInfo{
        private String text;
        private String toolTip;
        private boolean isHidden;
        
        ColumnHeaderInfo(String text,String toolTip,boolean isHidden){
            this.text=text;
            this.toolTip=toolTip;
            this.isHidden=isHidden;
        }
        
        public String getText(){
            return text;
        }
        
        public String getToolTip(){
            return toolTip;
        }
        
        public boolean getIsHidden(){
            return isHidden;
        }
    }
}