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

import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.MouseButton;
import com.trolltech.qt.core.Qt.Orientation;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QHeaderView;
import com.trolltech.qt.gui.QMenu;
import com.trolltech.qt.gui.QMouseEvent;
import com.trolltech.qt.gui.QTreeWidget;
import com.trolltech.qt.gui.QTreeWidgetItem;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.GroupModelReader;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.profiling.ProfilerWidget.EPropertyName;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.progress.TaskWaiter;


public class AbstractProfileTree {
    protected final ItemTextCalculator itemCalc;
    protected QTreeWidget tree;
    protected ProfilerWidget editor;
    protected List<TreeItem> fullPeriodList = new ArrayList<>();
    protected List<Long> selectedInstances = new ArrayList<>();
    private Double percentFilter = 0.0;
    protected boolean isPercentMode;
    protected static final QColor hardLoadedColor = QColor.blue;
    protected static final QColor middleLoadedColor = QColor.darkCyan;
    //protected static List<String> labels;
    protected static int COLUMN_COUNT;
    public static final String STR_TREE_NODE = "Classification Tree";
    public static final String STR_CONTEXT_TREE_NODE = "Context Tree";
    public static final String STR_FLAT_LIST_NODE = "Flat List";
    public static Id domainCpuId;
    public static Id domainDbId;
    public static Id domainExtId;
    protected Id propNameId;
    protected Id propPeriodId;
    protected Id propContextId;
    protected Id propDurationId;
    protected Id propMinDurationId;
    protected Id propMaxDurationId;
    protected Id propCountId;
    protected Id propInstanceId;
    protected String MICROSECONDS_SYMBOL = Application.translate("ProfilerDialogAbbrev", " (<micro>)");
    
    final static String strTab = "&nbsp;&nbsp;";
    
    public void setPropIdsFrom(AbstractProfileTree tree) {
        propNameId = tree.propNameId;
        propPeriodId = tree.propPeriodId;
        propContextId = tree.propContextId;
        propDurationId = tree.propDurationId;
        propMinDurationId = tree.propMinDurationId;
        propMaxDurationId = tree.propMaxDurationId;
        propCountId = tree.propCountId;
        propInstanceId = tree.propInstanceId;
    }

    public void setPropIds() {
        propNameId = editor.getIdsGetter().getPropertyIdByName(EPropertyName.NAME);
        propPeriodId = editor.getIdsGetter().getPropertyIdByName(EPropertyName.PERIOD);
        propContextId = editor.getIdsGetter().getPropertyIdByName(EPropertyName.CONTEXT);
        propDurationId = editor.getIdsGetter().getPropertyIdByName(EPropertyName.DURATION);
        propMinDurationId = editor.getIdsGetter().getPropertyIdByName(EPropertyName.MIN_DURATION);
        propMaxDurationId = editor.getIdsGetter().getPropertyIdByName(EPropertyName.MAX_DURATION);
        propCountId = editor.getIdsGetter().getPropertyIdByName(EPropertyName.COUNT);
        propInstanceId = editor.getIdsGetter().getPropertyIdByName(EPropertyName.INSTANCE);
    }

    public void setDomainIds() {
        domainCpuId = editor.getIdsGetter().getDomainCpuId();
        domainDbId = editor.getIdsGetter().getDomainDbId();
        domainExtId = editor.getIdsGetter().getDomainExtId();
    }

    protected final List<String> getColumnsName() {
        List<String> labels = new LinkedList<>();
        labels.add(Application.translate("ProfilerDialog", "Name"));
        labels.add(Application.translate("ProfilerDialogAbbrev", "Duration") + strPercentOrSecond());
        labels.add(Application.translate("ProfilerDialogAbbrev", "Min Duration") + MICROSECONDS_SYMBOL);
        labels.add(Application.translate("ProfilerDialogAbbrev", "Max Duration") + MICROSECONDS_SYMBOL);
        labels.add(Application.translate("ProfilerDialogAbbrev", "Average Duration") + strPercentOrSecond());
        labels.add(Application.translate("ProfilerDialogAbbrev", "Pure Duration") + strPercentOrSecond());
        labels.add(Application.translate("ProfilerDialogAbbrev", "Pure Average Duration") + strPercentOrSecond());
        labels.add(Application.translate("ProfilerDialogAbbrev", "Count"));
        labels.add(Application.translate("ProfilerDialog", "CPU") + strPercentOrSecond());        
        labels.add(Application.translate("ProfilerDialog", "DB") + strPercentOrSecond());        
        labels.add(Application.translate("ProfilerDialogAbbrev", "Ext") + strPercentOrSecond());
        labels.add(Application.translate("ProfilerDialogAbbrev", "Pure CPU") + strPercentOrSecond());
        labels.add(Application.translate("ProfilerDialogAbbrev", "Pure DB") + strPercentOrSecond());
        labels.add(Application.translate("ProfilerDialogAbbrev", "Pure Ext") + strPercentOrSecond());       
        return labels;
    }
    
    protected final List<String> getColumnsToolTip() {
        List<String> labels = new LinkedList<>();
        labels.add(Application.translate("ProfilerDialog", "Name"));
        labels.add(Application.translate("ProfilerDialog", "Duration"));
        labels.add(Application.translate("ProfilerDialog", "Minimum Duration"));
        labels.add(Application.translate("ProfilerDialog", "Maximum Duration"));
        labels.add(Application.translate("ProfilerDialog", "Average Duration") );
        labels.add(Application.translate("ProfilerDialog", "Pure Duration") );
        labels.add(Application.translate("ProfilerDialog", "Pure Average Duration"));
        labels.add(Application.translate("ProfilerDialog", "Count"));
        labels.add(Application.translate("ProfilerDialog", "CPU Waiting"));        
        labels.add(Application.translate("ProfilerDialog", "DB Waiting") );        
        labels.add(Application.translate("ProfilerDialog", "External System Waiting"));
        labels.add(Application.translate("ProfilerDialog", "Pure CPU Waiting"));
        labels.add(Application.translate("ProfilerDialog", "Pure DB Waiting") );
        labels.add(Application.translate("ProfilerDialog", "Pure External System Waiting"));       
        return labels;
    }

    private String strPercentOrSecond() {
        return isPercentMode ? " (%)" : MICROSECONDS_SYMBOL;
    }

    protected AbstractProfileTree() {
        itemCalc=new ItemTextCalculator();
        COLUMN_COUNT = 14;
    }

    protected AbstractProfileTree(final ProfilerWidget parent,boolean isPercentMode) {
        this();
        MICROSECONDS_SYMBOL = MICROSECONDS_SYMBOL.replace("<micro>", "\u00B5s"); //Dirty walkaround Qt Translater Bug with UTF-8 characters.
        this.isPercentMode=isPercentMode;
        tree = new QTreeWidget(parent);
        tree.setFont(new QFont("Monospace"));//Monospace,DejaVu Sans Mono,Liberation Mono
        editor = parent;
        tree.setColumnCount(COLUMN_COUNT);
        tree.setHeader(new QHeaderView(Orientation.Horizontal, tree) {

            @Override
            protected void mouseReleaseEvent(QMouseEvent event) {
                if ((event.button() == MouseButton.RightButton)) {
                    onHeaderMouseClick(event.globalPos());
                }
                super.mouseReleaseEvent(event);
            }
        });
        tree.setObjectName("ProfileTree");
        tree.setAlternatingRowColors(true);
        tree.setHeaderLabels(getColumnsName());
        
        List<String> columnToolTip=getColumnsToolTip();
        for (int i = 0; i < COLUMN_COUNT; i++) {
            tree.headerItem().setTextAlignment(i, Qt.AlignmentFlag.AlignCenter.value());
            tree.headerItem().setToolTip(i, columnToolTip.get(i));
        }
        tree.setSortingEnabled(true);
        tree.setItemDelegate(new ItemDelegate());
    }

    private void onHeaderMouseClick(QPoint pos) {
        QMenu menu = new QMenu();
        List<String> labels = getColumnsName();
        for (int i = 1, size = labels.size(); i < size; i++) {
            QAction act = new QAction(labels.get(i), tree);
            act.setCheckable(true);
            boolean isChacked = !tree.isColumnHidden(i);
            act.setChecked(isChacked);
            act.triggered.connect(this, "setColumnHidden" + i + "()");
            menu.addAction(act);
        }
        menu.exec(pos);
    }

    @SuppressWarnings("unused")
    private void setColumnHidden1() {
        setColumnVisibility(1);
    }

    @SuppressWarnings("unused")
    private void setColumnHidden2() {
        setColumnVisibility(2);
    }

    @SuppressWarnings("unused")
    private void setColumnHidden3() {
        setColumnVisibility(3);
    }

    @SuppressWarnings("unused")
    private void setColumnHidden4() {
        setColumnVisibility(4);
    }

    @SuppressWarnings("unused")
    private void setColumnHidden5() {
        setColumnVisibility(5);
    }

    @SuppressWarnings("unused")
    private void setColumnHidden6() {
        setColumnVisibility(6);
    }

    @SuppressWarnings("unused")
    private void setColumnHidden7() {
        setColumnVisibility(7);
    }

    @SuppressWarnings("unused")
    private void setColumnHidden8() {
        setColumnVisibility(8);
    }

    @SuppressWarnings("unused")
    private void setColumnHidden9() {
        setColumnVisibility(9);
    }

    @SuppressWarnings("unused")
    private void setColumnHidden10() {
        setColumnVisibility(10);
    }
    
    @SuppressWarnings("unused")
    private void setColumnHidden11() {
        setColumnVisibility(11);
    }
    
    @SuppressWarnings("unused")
    private void setColumnHidden12() {
        setColumnVisibility(11);
    }
    
    @SuppressWarnings("unused")
    private void setColumnHidden13() {
        setColumnVisibility(11);
    }
    

    private void setColumnVisibility(int index) {
        if (tree.isColumnHidden(index)) {
            tree.showColumn(index);
        } else {
            tree.hideColumn(index);
        }
    }

    public List<TreeItem> getPeriodList() {
        return fullPeriodList;
    }

    public void filterByPersent(Double filterPercent, boolean isSummaryMode) {
        this.percentFilter = filterPercent;
        for (int i = 0; i < tree.topLevelItemCount(); i++) {
            TreeItem item = (TreeItem) tree.topLevelItem(i);
            if (item.isCalculated() || isSummaryMode) {
                filterByPercent(item);
            }
        }
    }

    protected boolean isInSelectedInstance(Long inst) {
        if (selectedInstances == null) {
            return true;
        }
        return selectedInstances.contains(inst);
    }

    protected void filterByPercent(TreeItem parent, Double filterPercent) {
        this.percentFilter = filterPercent;
        filterByPercent(parent);
    }

    protected void filterByPercent(TreeItem parent) {
        for (int i = 0; i < parent.childCount(); i++) {
            TreeItem item = (TreeItem) parent.child(i);
            if (item.isMeaningful()) {
                boolean hide = item.getLoad() < percentFilter;
                item.setHidden(hide);
            }
            filterByPercent(item);
            if (!item.isMeaningful()) {
                boolean hide = getVisibleChildCount(item) == 0;
                item.setHidden(hide);
            }
        }
    }

    private int getVisibleChildCount(TreeItem item) {
        int res = 0;
        for (int i = 0; i < item.childCount(); i++) {
            TreeItem child_item = (TreeItem) item.child(i);
            if (!child_item.isHidden()) {
                res++;
            }
        }
        return res;
    }

    protected void highlightItems(TreeItem flatList, TreeItem contextList, TreeItem treeList) {
        if (flatList == null) {
            return;
        }
        Map<QTreeWidgetItem, QColor> highlightItems = getHighlightItems(flatList);

        TreeItem contextlessFlatListItem = (TreeItem) flatList.clone();
        flatList.addChild(contextlessFlatListItem);
        for (int i = 0; i < contextlessFlatListItem.childCount(); i++) {
            TreeItem item = (TreeItem) contextlessFlatListItem.child(i);
            if (summItemsInsidePeriod(contextlessFlatListItem, item, false)) {
                contextlessFlatListItem.removeChild(item);
                i--;
            }
        }
        Map<QTreeWidgetItem, QColor> contextlesshighlightItems = getHighlightItems(contextlessFlatListItem);
        flatList.removeChild(contextlessFlatListItem);

        highlightItems(flatList, highlightItems);
        if (contextList != null) {
            highlightItems(contextList, highlightItems);
        }
        if (treeList != null) {
            highlightItems(treeList, contextlesshighlightItems);
        }
    }

    private void highlightItems(QTreeWidgetItem treeItem, Map<QTreeWidgetItem, QColor> highlightItems) {
        for (int i = 0; i < treeItem.childCount(); i++) {
            QTreeWidgetItem item = treeItem.child(i);
            if (highlightItems.containsKey(item)) {
                highlightItem(item, highlightItems.get(item));
            } else {
                highlightItem(item, QColor.black);
            }
            highlightItems(item, highlightItems);
        }
    }

    private Map<QTreeWidgetItem, QColor> getHighlightItems(TreeItem flatListItem) {
        Map<QTreeWidgetItem, QColor> result = new HashMap<>();
        BigDecimal bigDecimal = new BigDecimal(flatListItem.childCount() / 10);
        bigDecimal = bigDecimal.setScale(0, BigDecimal.ROUND_HALF_EVEN);
        int contextItemCount = bigDecimal.intValue() <= 1 ? 1 : bigDecimal.intValue();

        flatListItem.sortChildren(TreeItem.PURE_LOAD_COL_INDEX, Qt.SortOrder.DescendingOrder);
        Map<QTreeWidgetItem, QColor> hardLoadedMap = getHighlightItems(flatListItem, 0, contextItemCount, hardLoadedColor);
        Map<QTreeWidgetItem, QColor> middleLoadedMap = getHighlightItems(flatListItem, hardLoadedMap.size(), contextItemCount, middleLoadedColor);
        result.putAll(hardLoadedMap);
        result.putAll(middleLoadedMap);
        return result;
    }

    private Map<QTreeWidgetItem, QColor> getHighlightItems(TreeItem flatListItem, int start, int end, QColor color) {
        Map<QTreeWidgetItem, QColor> result = new HashMap<>();
        end = start + end;
        for (int i = start; i < end; i++) {
            result.put(flatListItem.child(i), color);
            if ((i == end - 1) && (i + 1 < flatListItem.childCount())) {
                int j = i + 1;
                TreeItem item = (TreeItem) flatListItem.child(i);
                TreeItem next_item = (TreeItem) flatListItem.child(j);
                while (item.getPureLoad().doubleValue() == next_item.getPureLoad().doubleValue()) {
                    result.put(next_item, color);
                    j++;
                    next_item = (TreeItem) flatListItem.child(j);
                }
            }
        }
        return result;
    }

    private void highlightItem(QTreeWidgetItem item, QColor color) {
        QBrush b = new QBrush();
        b.setColor(color);
        for (int i = 0; i < COLUMN_COUNT; i++) {
            item.setForeground(i, b);
        }
    }

    protected void resizeColumns() {
        for (int i = 0; i < COLUMN_COUNT; i++) {
            tree.resizeColumnToContents(i);
        }
    }


    public QTreeWidget getTree() {
        return tree;
    }

    protected void buildTree(List<TreeItem> items) {
        TaskWaiter waiter = new TaskWaiter(editor.getEnvironment(),tree);
        waiter.setMessage(Application.translate("ProfilerDialog", "Build Tree..."));
        try {
            for (TreeItem item : items) {
                if (!item.isCalculated()) {
                    item.takeChildren();
                    item.addChildren(getChildItems(item));
                }
            }
            final Runnable task = new TreeModeBuilder(items, editor.getTimeSectionEnum(), itemCalc);
            waiter.runAndWait(task);
        } catch (InterruptedException ex) {
        } finally {
            waiter.close();
        }
    }

    private List<QTreeWidgetItem> getChildItems(TreeItem parent) throws InterruptedException {
        List<QTreeWidgetItem> childItems = new ArrayList<>();
        try {
            EntityModel entity = parent.getEntity();
            Model childModel = entity.getChildModel(editor.getIdsGetter().getProfilerLogChildId());
            //Iterator iter= entity.getEditorPresentationDef().getChildrenExplorerItems().iterator();
            //if(iter.hasNext()){
            //Id childId = entity.getEditorPresentationDef().getChildrenExplorerItems().iterator().next().getId();
            //Model childModel = entity.getChildModel(editor.getIdsGetter().getProfilerLogChildId());
            if (childModel instanceof GroupModel) {
                final GroupModelReader childrenReader = 
                    new GroupModelReader((GroupModel)childModel, EnumSet.of(GroupModelReader.EReadingFlags.TRACE_SERVICE_CLIENT_EXCEPTION));
                for (EntityModel childEntity: childrenReader) {
                    String name = (String) getPropValue(childEntity, propNameId);
                    name = name == null ? "" : name;
                    Timestamp timePeriod = parent.getPeriod();
                    String context = (String) getPropValue(childEntity, propContextId);
                    context = context == null ? "" : context;
                    Long duration = (Long) getPropValue(childEntity, propDurationId);
                    Long minDuration = (Long) getPropValue(childEntity, propMinDurationId);
                    Long maxDuration = (Long) getPropValue(childEntity, propMaxDurationId);
                    Long count = (Long) getPropValue(childEntity, propCountId);
                    Long instanceId = parent.getInstanceId();
                    String title = itemCalc.getItemTitle(name, editor.getTimeSectionEnum());
                    TreeItem item = new TreeItem(name, timePeriod, context, duration, minDuration, maxDuration, count, instanceId, 0l, null, null, null, null, childEntity.getPid(), title, isPercentMode);
                    childItems.add(item);
                }
                if (childrenReader.wasInterrupted()){
                    throw new InterruptedException();
                }
            }
            //}
        } catch (ServiceClientException ex) {
            Logger.getLogger(AbstractProfileTree.class.getName()).log(Level.SEVERE, null, ex);
        }
        return childItems;
    }

    protected Object getPropValue(EntityModel entity, Id propId) {
        return entity.getProperty(propId).getValueObject();
    }

    protected boolean summItemsInsidePeriod(TreeItem period, TreeItem item, boolean isContext) {
        for (int j = 0; j < period.childCount(); j++) {
            TreeItem it = (TreeItem) period.child(j);
            if ((!it.equals(item)) && (it.isCanSumWith(item, isContext))) {
                it.sumItemTextWith(item, false, isContext);
                return true;
            }
        }
        return false;
    }

    public QTreeWidgetItem getItemByPid(Pid pid) {
        for (int i = 0; i < tree.topLevelItemCount(); i++) {
            TreeItem item = (TreeItem) tree.topLevelItem(i);
            TreeItem findItem = findItem(item, Collections.singleton(pid));
            if (findItem != null) {
                return findItem;
            }
        }
        return null;
    }
    
    public QTreeWidgetItem getItemByPid(Collection<Pid> pids) {
        for (int i = 0; i < tree.topLevelItemCount(); i++) {
            TreeItem item = (TreeItem) tree.topLevelItem(i);
            TreeItem findItem = findItem(item, pids);
            if (findItem != null) {
                return findItem;
            }
        }
        return null;
    }
    

    private TreeItem findItem(TreeItem parent, Collection<Pid> pids) {
        for (int i = 0; i < parent.childCount(); i++) {
            TreeItem item = (TreeItem) parent.child(i);
            if (item.getPid() != null && pids.contains(item.getPid())) {
                return item;
            }
            findItem(item, pids);
        }
        return null;
    }        

    public void clear() {
        tree.clear();
        //filteredPeriodList.clear();
    }

    public QTreeWidgetItem getCurrentItem() {
        return tree.currentItem();
    }

    public void setCurrentItem(QTreeWidgetItem item) {
        tree.expandItem(item);
        tree.setCurrentItem(item);
    }

    public List<Long> getSelectedInstanceIds() {
        return selectedInstances;
    }

    /*public List<String> getPeriodNames(){
    List<String> periodNames=new ArrayList<String>();
    for(int i=0;i<filteredPeriodList.size();i++){
    String name=filteredPeriodList.get(i).getName();
    periodNames.add(name);
    }
    return periodNames;
    }*/
    public Timestamp getCurrentPeriod() {
        TreeItem curItem = (TreeItem) tree.currentItem();
        Timestamp period = null;
        if (curItem != null) {
            period = curItem.getPeriod();
            while (period == null) {
                TreeItem parentItem = (TreeItem) curItem.parent();
                period = parentItem.getPeriod();
                curItem = parentItem;
            }

        }
        return period;
    }
    
    public TreeItem getPeriodItem(TreeItem curItem) {
        //TreeItem curItem = (TreeItem) tree.currentItem();
        //if (curItem != null) {
            while (curItem.parent() != null) {
                TreeItem parentItem = (TreeItem) curItem.parent();
                //period = parentItem.getPeriod();
                curItem = parentItem;
            }
        //}
        return curItem;
    }

    public boolean isTreeEmpty() {
        return tree.topLevelItemCount() == 0;
    }

    public Double getPercentFilter() {
        return percentFilter;
    }

    public void setClassificationMode(boolean isPercentMode) {
        this.isPercentMode = isPercentMode;
        tree.setHeaderLabels(getColumnsName());
        List<String> columnHeaderToolTip=getColumnsToolTip();
        for(int i=0;i<columnHeaderToolTip.size();i++)
            tree.headerItem().setToolTip(i, columnHeaderToolTip.get(i));
        for (int i = 0; i < tree.topLevelItemCount(); i++) {
            TreeItem item = (TreeItem) tree.topLevelItem(i);
            item.changePercentMode(isPercentMode);
            changeClassificationMode(item, isPercentMode);
        }
        tree.header().reset();
        //resizeColumns();
    }

    private void changeClassificationMode(TreeItem item, boolean isPercentMode) {
        for (int i = 0; i < item.childCount(); i++) {
            TreeItem childItem = (TreeItem) item.child(i);
            childItem.changePercentMode(isPercentMode);
            changeClassificationMode(childItem, isPercentMode);
        }
    }

    public boolean isPercentMode() {
        return isPercentMode;
    }
}
