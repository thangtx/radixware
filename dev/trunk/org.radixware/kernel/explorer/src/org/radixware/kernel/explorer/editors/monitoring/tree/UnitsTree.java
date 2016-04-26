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
package org.radixware.kernel.explorer.editors.monitoring.tree;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QTreeWidget;
import com.trolltech.qt.gui.QTreeWidgetItem;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.eas.CommandRequestHandle;
import org.radixware.kernel.common.client.eas.IResponseListener;
import org.radixware.kernel.common.client.eas.RequestHandle;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.explorer.editors.monitoring.UnitsWidget;
import org.radixware.kernel.explorer.editors.monitoring.UnitsWidget.IdsGetter;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.schemas.monitoringcommand.SysMonitoringRq;
import org.radixware.schemas.monitoringcommand.SysMonitoringRq.ExpandedInstances.Instance;
import org.radixware.schemas.monitoringcommand.SysMonitoringRqDocument;
import org.radixware.schemas.monitoringcommand.SysMonitoringRs;
import org.radixware.schemas.monitoringcommand.SysMonitoringRs.Unit;
import org.radixware.schemas.monitoringcommand.SysMonitoringRsDocument;

public final class UnitsTree {

    private final UnitsWidget parent;
    private final QTreeWidget tree;
    private final Model sourceGroup;
    private final IdsGetter idsGetter;
    private final List<MonitoringTreeItemDecorator> decorators = new ArrayList<>();
    private final List<QTreeWidgetItem> decorateItems = new ArrayList<>();
    private final Set<QTreeWidgetItem> scheduledForDecoration = new HashSet<>();
    private final Set<QTreeWidgetItem> decoratedInThisIteration = new HashSet<>();
    private boolean decorationIsInProgress = false;
    private final GroupModel instanceModel;
    private final GroupModel unitsModel;
    private final GroupModel channelsModel;
    private final MetricInfoGetter metricInfo;
    private final Map<Long, ExpandedTreeItem> expandedItemIds;
    private SysMonitoringRq rqItem;
    private SysMonitoringRs prevRs;

    public Map<Long, ExpandedTreeItem> getExpandedItemIds() {
        refreshExpandedItems();
        return expandedItemIds;
    }

    public void setExpandedItemIds(Map<Long, ExpandedTreeItem> expandedItems) {
        this.expandedItemIds.clear();
        this.expandedItemIds.putAll(expandedItems);
    }

    private static List<String> getColumnName() {
        List<String> list = new ArrayList<>();
        list.add(Application.translate("SystemMonitoring", "Object"));
        list.add(Application.translate("SystemMonitoring", "State"));
        list.add(Application.translate("SystemMonitoring", "Other Information"));
        return list;
    }

    /*public static class RereadEvent extends QEvent {

     public RereadEvent() {
     super(QEvent.Type.User);
     }        
     }
    
     public static class UpdateCommandBarEvent extends QEvent {
     private final QTreeWidgetItem item;
     public UpdateCommandBarEvent(final QTreeWidgetItem item) {
     super(QEvent.Type.User);
     this.item=item;
     } 
        
     public QTreeWidgetItem getSelectedItem(){
     return item;
     }
     }*/
    public UnitsTree(final UnitsWidget parent, final Model sourceGroup, GroupModel instanceModel, final GroupModel unitsModel, final GroupModel channelsModel) {
        this.parent = parent;
        this.instanceModel = instanceModel;
        this.unitsModel = unitsModel;
        this.channelsModel = channelsModel;
        this.expandedItemIds = new HashMap<>();

        this.metricInfo = new MetricInfoGetter(parent.getEnvironment());
        this.idsGetter = parent.getIdsGetter();
        this.sourceGroup = sourceGroup;
        tree = new ScrolledTreeWidget(parent);
        tree.setObjectName("UnitsTree");
        tree.itemExpanded.connect(this, "onExpandTreeItem(QTreeWidgetItem)");
        tree.currentItemChanged.connect(this, "curItemChanged(QTreeWidgetItem,QTreeWidgetItem)");
        tree.setAlternatingRowColors(true);
        tree.setSortingEnabled(true);
        tree.setItemDelegate(new ItemDelegate());

        List<String> columnNames = getColumnName();
        tree.setHeaderLabels(columnNames);
        for (int i = 0; i < columnNames.size(); i++) {
            tree.headerItem().setTextAlignment(i, Qt.AlignmentFlag.AlignCenter.value());
        }
        decorators.addAll(loadDecorators());
        //refreshDecorators();
        updateByCommand();
    }

    private void refreshDecorators(SysMonitoringRq rq, SysMonitoringRs rs) {
        for (MonitoringTreeItemDecorator decorator : decorators) {
            if (decorator instanceof MonitoringTreeItemDecoratorV2) {
                ((MonitoringTreeItemDecoratorV2) decorator).prepare(parent, rq, rs, prevRs);
            } else {
                decorator.prepare(rq, rs);
            }
        }
    }

    @SuppressWarnings("unused")
    private void curItemChanged(QTreeWidgetItem item, QTreeWidgetItem prevItem) {
        //QApplication.postEvent(tree, new UpdateContextEventPanelEvent(item)); 
        parent.curItemChanged(item);
    }

    private List<MonitoringTreeItemDecorator> loadDecorators() {
        final List<MonitoringTreeItemDecorator> loadedDecorators = new ArrayList<>();
        try {
            final ServiceLoader<IDecoratorFactory> loader = ServiceLoader.load(IDecoratorFactory.class, sourceGroup.getClass().getClassLoader());
            final Iterator<IDecoratorFactory> iterator = loader.iterator();
            while (iterator.hasNext()) {
                loadedDecorators.addAll(iterator.next().createDecorators(parent.getEnvironment()));
            }
        } catch (Exception ex) {
            parent.getEnvironment().messageException("Error", "Error while loading extensions for 'System Units' view", ex);
        }
        return loadedDecorators;
    }

    private void decorateReadyItems() {
        decorateItems(decorateItems);
    }

    private void decorateTree() {
        setDecorateItems(getVisibleItems(tree.invisibleRootItem()));
        decorateReadyItems();
    }

    private void decorateItems(final List<QTreeWidgetItem> treeItems) {
        if (treeItems == null || treeItems.isEmpty()) {
            return;
        }

        scheduledForDecoration.addAll(treeItems);

        if (decorationIsInProgress) {
            return;
        }

        decorationIsInProgress = true;

        try {
            while (!scheduledForDecoration.isEmpty()) {
                scheduledForDecoration.removeAll(decoratedInThisIteration);
                final List<QTreeWidgetItem> currentlyDecoratedItems = new ArrayList<>(scheduledForDecoration);
                for (QTreeWidgetItem item : currentlyDecoratedItems) {
                    if (item instanceof TreeItem) {
                        final TreeItem treeItem = (TreeItem) item;
                        final List<MonitoringTreeItemDecoration> allDecorations = new ArrayList<>();
                        for (MonitoringTreeItemDecorator decorator : decorators) {
                            try {
                                final List<MonitoringTreeItemDecoration> decorations = decorator.decorate(treeItem);
                                if (decorations != null) {
                                    boolean hideItem = decorations == MonitoringTreeItemDecorator.HIDE_ITEM_DECORATIONS;
                                    treeItem.setHidden(hideItem);
                                    if (hideItem) {
                                        break;
                                    }
                                    allDecorations.addAll(decorations);
                                }
                            } catch (Exception ex) {
                                Logger.getLogger(UnitsTree.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        if (!treeItem.isHidden() && !allDecorations.isEmpty()) {
                            Collections.sort(allDecorations, new Comparator<MonitoringTreeItemDecoration>() {
                                @Override
                                public int compare(MonitoringTreeItemDecoration o1, MonitoringTreeItemDecoration o2) {
                                    return o1.getIndex().compareTo(o2.getIndex());
                                }
                            });
                            final MetricData data = new MetricData(MetricData.Type.DECORATIONS);
                            data.setDecorations(allDecorations);
                            item.setData(2, Qt.ItemDataRole.UserRole, data);
                            treeItem.updateText(2);
                        }
                    }
                    decoratedInThisIteration.addAll(currentlyDecoratedItems);
                }
                /*for (MonitoringTreeItemDecorator decorator : decorators) {
                 for (QTreeWidgetItem item : currentlyDecoratedItems) {
                 if (item instanceof TreeItem) {
                 final TreeItem treeItem = (TreeItem) item;
                 boolean hideItem = false;
                 final List<MonitoringTreeItemDecoration> decorations = decorator.decorate(treeItem);
                 if (decorations == MonitoringTreeItemDecorator.HIDE_ITEM_DECORATIONS) {
                 hideItem = true;
                 } else if (decorations != null && !decorations.isEmpty()) {
                 final MetricData data = new MetricData(MetricData.Type.DECORATIONS);
                 data.setDecorations(decorations);
                 item.setData(2, Qt.ItemDataRole.UserRole, data);
                 treeItem.updateText(2);
                 }
                 treeItem.setHidden(hideItem);
                 }
                 }
                 decoratedInThisIteration.addAll(currentlyDecoratedItems);
                 }*/
            }
        } finally {
            scheduledForDecoration.clear();
            decoratedInThisIteration.clear();
            decorationIsInProgress = false;
        }
    }

    public void setDecorateItems(List<QTreeWidgetItem> items) {
        decorateItems.clear();
        decorateItems.addAll(items);
    }

    public List<QTreeWidgetItem> getVisibleItems(final QTreeWidgetItem root) {
        List<QTreeWidgetItem> list = new ArrayList<>();
        for (int i = 0; i < root.childCount(); i++) {
            list.add(root.child(i));
            if (root.child(i).isExpanded()) {
                list.addAll(getVisibleItems(root.child(i)));
            }
        }
        return list;
    }

    private void fillTree(List<TreeItem> treeItems) {
        for (int i = 0; i < treeItems.size(); i++) {
            TreeItem item = treeItems.get(i);
            tree.addTopLevelItem(item);
            if (expandedItemIds != null && !expandedItemIds.isEmpty()) {
                boolean isExpanded = expandedItemIds.containsKey(item.getId());
                item.setExpanded(isExpanded);
            }
        }
        if (!treeItems.isEmpty()) {
            tree.setCurrentItem(tree.topLevelItem(0));
        }
    }

    private void checkExpandedChildItems(TreeItem item, ExpandedTreeItem expandedTreeItem) {
        for (int i = 0; i < item.childCount(); i++) {
            TreeItem childItem = (TreeItem) item.child(i);
            if (expandedTreeItem.getExpandedChildItems().containsKey(childItem.getId())) {
                childItem.setExpanded(true);
            }
        }
    }

    private List<TreeItem> createItems(MetricInfoGetter metricInfo) {
        List<TreeItem> items = new ArrayList<>();
        for (Long instId : metricInfo.getInstanceInfoMap().keySet()) {
            InstanceTreeItem topItem = new InstanceTreeItem(idsGetter, metricInfo.getInstanceInfoMap().get(instId), instanceModel);
            topItem.addChild(new InstanceTreeItem(idsGetter, null, null));
            topItem.updateMetricDate(metricInfo);
            items.add(topItem);
        }
        return items;
    }

    @SuppressWarnings("unused")
    void onExpandTreeItem(QTreeWidgetItem index) {
        QApplication.postEvent(tree, new ExpandEvent(index));
        /*if(index instanceof InstanceTreeItem && !((InstanceTreeItem)index).isCalculated()){
         InstanceTreeItem instanceTreeItem=(InstanceTreeItem)index;
         try {
         //if (!parent.getEnvironment().getEasSession().isBusy()){
         expandTreeItemCommand(instanceTreeItem);
         //}
         } catch (ServiceClientException | InterruptedException ex) {
         Logger.getLogger(UnitsTree.class.getName()).log(Level.SEVERE, null, ex);
         }
         }*/
    }

    private void expandTreeItem(QTreeWidgetItem index) {
        if (index instanceof InstanceTreeItem && !((InstanceTreeItem) index).isCalculated()) {
            InstanceTreeItem instanceTreeItem = (InstanceTreeItem) index;
            try {
                expandTreeItemCommand(instanceTreeItem);
            } catch (ServiceClientException | InterruptedException ex) {
                Logger.getLogger(UnitsTree.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void expandTreeItemCommand(InstanceTreeItem treeItem) throws ServiceClientException, InterruptedException {
        if ((treeItem != null) && (treeItem.parent() == null)) {
            treeItem.takeChildren();

            SysMonitoringRqDocument rqDoc = SysMonitoringRqDocument.Factory.newInstance();
            SysMonitoringRq rq = rqDoc.addNewSysMonitoringRq();
            SysMonitoringRq.ExpandedInstances xInst = rq.addNewExpandedInstances();
            Instance inst = Instance.Factory.newInstance();
            inst.setId(treeItem.getId());
            xInst.getInstanceList().add(inst);

            if (parent.getEnvironment().getEasSession().isBusy()) {
                parent.getEnvironment().getEasSession().breakRequest();
            }
            Application.processEventWhenEasSessionReady(tree, new ExpandCommandEvent(treeItem, rq));
        }
    }

    private void expandTreeItem(SysMonitoringRs res, InstanceTreeItem index) {
        try {
            tree.blockSignals(true);
            tree.setUpdatesEnabled(false);
            calcChildItems(res, index);
            for (int i = 0; i < index.childCount(); i++) {
                if (index.child(i) instanceof UnitNetPortHandlerTreeItem) {
                    UnitNetPortHandlerTreeItem netPortHandlerItem = (UnitNetPortHandlerTreeItem) index.child(i);
                    netPortHandlerItem.addNetChannels(res, metricInfo, channelsModel, decorators);
                }
            }
            index.setIsCalculated(true);
            decorateItems(getVisibleItems(index));
            tree.resizeColumnToContents(0);
        } catch (InterruptedException ex) {
        } finally {
            tree.blockSignals(false);
            tree.setUpdatesEnabled(true);
        }
    }

    private void calcChildItems(SysMonitoringRs res, InstanceTreeItem instanceItem) throws InterruptedException {
        boolean isInstanceStarted = instanceItem.isStarted();
        for (Unit unit : res.getUnitList()) {
            UnitTreeItem item = createUnitItem(unit, isInstanceStarted);
            item.updateMetricDate(metricInfo);
            instanceItem.addChild(item);
        }
    }

    private UnitTreeItem createUnitItem(Unit xUnit, final boolean isInstanceStarted) {
        MetricInfoGetter.UnitInfo unit = metricInfo.createUnitInfo(xUnit);
        return UnitTreeItem.Factory.newInstance(idsGetter, unit, isInstanceStarted, unitsModel);
    }

    public QTreeWidget getTree() {
        return tree;
    }

    /* public void setEnableTableUpdate(boolean isEnable) {
     tree.verticalScrollBar().setEnabled(isEnable);
     for (int i = 0; i < tree.topLevelItemCount(); i++) {
     tree.topLevelItem(i).setDisabled(!isEnable);
     }
     }*/
    public void reread() throws InterruptedException {
        metricInfo.clear();
        updateByCommand();
    }

    public final void updateByCommand() {
        SysMonitoringRqDocument rqDoc = createRequestDoc();
        CommandRequestHandle handle = RequestHandle.Factory.createForSendContextlessCommand(parent.getEnvironment(), idsGetter.getCommandId(), rqDoc, SysMonitoringRs.class);
        handle.addListener(new MonitoringResponseListener(rqDoc.getSysMonitoringRq()));
        parent.getEnvironment().getEasSession().sendAsync(handle);
    }

    public SysMonitoringRqDocument createRequestDoc() {
        SysMonitoringRqDocument rq = SysMonitoringRqDocument.Factory.newInstance();
        SysMonitoringRq metricRq = rq.addNewSysMonitoringRq();
        SysMonitoringRq.ExpandedInstances xInsts = metricRq.addNewExpandedInstances();

        for (int i = 0; i < getTree().topLevelItemCount(); i++) {
            QTreeWidgetItem item = getTree().topLevelItem(i);
            if (item instanceof InstanceTreeItem && item.isExpanded()) {
                Instance xInst = xInsts.addNewInstance();
                InstanceTreeItem instanceItem = (InstanceTreeItem) item;
                Long id = instanceItem.getId();
                xInst.setId(id);
                /*for(MonitoringTreeItemDecorator decorator:instanceItem.getDecorators()){
                 List<Id> propIds=decorator.getPropIds(instanceItem);
                 if(propIds!=null)   
                 xInst.getPropIdList().addAll(propIds);
                 if(decorator.getMetricKinds()!=null)
                 xInst.getMetricKindList().addAll(decorator.getMetricKinds());
                 } 
                 for(int j=0;j<item.childCount();j++){
                 QTreeWidgetItem unitItem=item.child(j);
                 if(unitItem instanceof UnitTreeItem){
                 MetricReq.Units.Unit xUnit=xUnits.addNewUnit();
                 Long unitId=((UnitTreeItem)unitItem).getId();            
                 xUnit.setId(unitId);
                 for(MonitoringTreeItemDecorator decorator: ((UnitTreeItem)unitItem).getDecorators()){
                 List<Id> propIds=decorator.getPropIds((UnitTreeItem)unitItem);
                 if(propIds!=null)   
                 xUnit.getPropIdList().addAll(propIds);
                 if(decorator.getMetricKinds()!=null)
                 xUnit.getMetricKindList().addAll(decorator.getMetricKinds());
                 }
                 if(unitItem instanceof UnitNetPortHandlerTreeItem){
                 for(int k=0;k<unitItem.childCount();k++){
                 QTreeWidgetItem channelItem=unitItem.child(k);
                 if(channelItem instanceof NetChanelTreeItem){
                 MetricReq.Channels.Channel xChannel=xChannels.addNewChannel();
                 Long channelId=((NetChanelTreeItem)channelItem).getId();            
                 xChannel.setId(channelId);
                 for(MonitoringTreeItemDecorator decorator : ((NetChanelTreeItem)channelItem).getDecorators()){
                 List<Id> propIds=decorator.getPropIds((NetChanelTreeItem)channelItem);
                 if(propIds!=null)  
                 xChannel.getPropIdList().addAll(propIds);
                 if(decorator.getMetricKinds()!=null)
                 xChannel.getMetricKindList().addAll(decorator.getMetricKinds());
                 }
                 }
                 }
                 }
                 }
                 }*/
            }
        }
        rqItem = rq.getSysMonitoringRq();
        return rq;
    }

    public void onResponseRecived(SysMonitoringRs rsItem) {
        try {
            metricInfo.update(rsItem);
            refreshDecorators(rqItem, rsItem);
            updateMetric(metricInfo);
//            parent.updateTimeDisplayerBar();
        } finally {
            prevRs = rsItem;
            if (parent.isUpdateEnabled()) {
                parent.getMetricTimer().start();
            }
        }
    }

    public class MonitoringResponseListener implements IResponseListener {

        private final SysMonitoringRq rq;

        MonitoringResponseListener(final SysMonitoringRq rq) {
            this.rq = rq;
        }

        @Override
        public void registerRequestHandle(RequestHandle handle) {
        }

        @Override
        public void unregisterRequestHandle(RequestHandle handle) {
        }

        @Override
        public void onResponseReceived(XmlObject response, RequestHandle handle) {
            try {
                SysMonitoringRs doc = (SysMonitoringRs) ((CommandRequestHandle) handle).getOutput();
                metricInfo.update(doc);
                refreshDecorators(rq, doc);
                updateMetric(metricInfo);
//                parent.updateTimeDisplayerBar();
            } catch (ServiceClientException ex) {
                final String messageError = ClientException.getExceptionReason(parent.getEnvironment().getMessageProvider(), ex) + "\n" + ClientException.exceptionStackToString(ex);
                parent.getEnvironment().getTracer().error("Unexpected exception" + ":\n" + messageError);
            } catch (InterruptedException ex) {
            } finally {
                if (parent.isUpdateEnabled()) {
                    parent.getMetricTimer().start();
                }
            }
        }

        @Override
        public void onServiceClientException(ServiceClientException exception, RequestHandle handle) {
            parent.getMetricTimer().start();
        }

        @Override
        public void onRequestCancelled(XmlObject request, RequestHandle handler) {
            parent.getMetricTimer().start();
        }
    }

    /* public void refreshDecoratorsAndDecorate() {
     refreshDecorators();
     decorateTree();
     }*/
    public void updateMetric(MetricInfoGetter metricInfo) {
        if (tree.topLevelItemCount() == 0) {
            List<TreeItem> items = createItems(metricInfo);
            if (items != null) {
                fillTree(items);
                if (getTree().topLevelItemCount() > 0) {
                    TreeItem item = (TreeItem) getTree().topLevelItem(0);
                    getTree().setCurrentItem(item);
                }
            }
            //}else if(expandedItem!=null){
            //     updateChildItemMetric(expandedItem,metricInfo);
        } else {
            for (int i = 0; i < tree.topLevelItemCount(); i++) {
                TreeItem item = (TreeItem) tree.topLevelItem(i);
                item.updateMetricDate(metricInfo);
                updateChildItemMetric(item, metricInfo);
            }
        }
        decorateTree();
        tree.resizeColumnToContents(0);
    }

    private void updateChildItemMetric(TreeItem item, MetricInfoGetter metricInfo) {
        for (int i = 0; i < item.childCount(); i++) {
            TreeItem child_item = (TreeItem) item.child(i);
            child_item.updateMetricDate(metricInfo);
            updateChildItemMetric(child_item, metricInfo);
        }
    }

    public void clear() {
        refreshExpandedItems();
        tree.clear();
    }

    public void refreshExpandedItems() {
        expandedItemIds.clear();
        for (int i = 0; i < tree.topLevelItemCount(); i++) {
            TreeItem item = (TreeItem) tree.topLevelItem(i);
            if (item.isExpanded()) {
                ExpandedTreeItem expandedItem = new ExpandedTreeItem();
                expandedItem.collectExpandedItems(item);
                expandedItemIds.put(item.getId(), expandedItem);
            }
        }
    }

    public static class ExpandedTreeItem {
        //private final Long id;

        private final Map<Long, ExpandedTreeItem> expandedChildItems;

        public ExpandedTreeItem() {
            //this.id=id;
            expandedChildItems = new HashMap<>();
        }

        public void addExpandedChildId(Long id, ExpandedTreeItem expandedChildItem) {
            expandedChildItems.put(id, expandedChildItem);
        }

        public Map<Long, ExpandedTreeItem> getExpandedChildItems() {
            return expandedChildItems;
        }

        public ExpandedTreeItem getById(final Long id) {
            if (expandedChildItems == null || expandedChildItems.isEmpty()) {
                return null;
            }
            if (expandedChildItems.containsKey(id)) {
                return expandedChildItems.get(id);
            } else {
                for (ExpandedTreeItem item : expandedChildItems.values()) {
                    ExpandedTreeItem target = item.getById(id);
                    if (target != null) {
                        return target;
                    }
                }
            }
            return null;
        }

        public void collectExpandedItems(TreeItem item) {
            for (int i = 0; i < item.childCount(); i++) {
                TreeItem childItem = (TreeItem) item.child(i);
                if (childItem.isExpanded()) {
                    ExpandedTreeItem expandedChildItem = new ExpandedTreeItem();
                    collectExpandedItems(childItem);
                    addExpandedChildId(childItem.getId(), expandedChildItem);
                }
            }
        }
    }

    class ScrolledTreeWidget extends QTreeWidget {

        ScrolledTreeWidget(final UnitsWidget parent) {
            super(parent);
        }

        @Override
        protected void customEvent(QEvent qevent) {
            /*if(qevent instanceof ScrollUnitTreeEvent){ 
             qevent.accept();
             if (parent.getEnvironment().getEasSession().isBusy()){
             QApplication.postEvent(this, new ScrollUnitTreeEvent());
             }else{
             loadMoreRows();
             }               
             }if(qevent instanceof ScrollEventLogEvent){   
             ScrollEventLogEvent scrollEvent=(ScrollEventLogEvent)qevent;
             qevent.accept();
             if (parent.getEnvironment().getEasSession().isBusy()){
             QApplication.postEvent(this, new ScrollEventLogEvent(scrollEvent.getTable()));
             }else{
             scrollEvent.getTable().scroll();
             }               
             }
             else */ if (qevent instanceof ExpandEvent) {
                ExpandEvent expandEvent = (ExpandEvent) qevent;
                expandEvent.accept();
                if (parent.getEnvironment().getEasSession().isBusy()) {
                    QApplication.postEvent(this, new ExpandEvent(expandEvent.getExpandedItem()));
                } else {
                    expandTreeItem(expandEvent.getExpandedItem());
                }
            } else if (qevent instanceof UpdateContextEventPanelEvent) {
                UpdateContextEventPanelEvent curItemChangedEvent = (UpdateContextEventPanelEvent) qevent;
                curItemChangedEvent.accept();
                if (parent.getEnvironment().getEasSession().isBusy()) {
                    QApplication.postEvent(this, new UpdateContextEventPanelEvent(curItemChangedEvent.getExpandedItem()));
                } else if (curItemChangedEvent.getExpandedItem() instanceof TreeItem) {
                    final TreeItem item = (TreeItem) curItemChangedEvent.getExpandedItem();
                    UnitsTree.this.parent.getEventLogPanel().updateContextTab(item);
                }
            } else if (qevent instanceof ExpandCommandEvent) {
                ExpandCommandEvent commandEvent = (ExpandCommandEvent) qevent;

                try {
                    SysMonitoringRsDocument rs = (SysMonitoringRsDocument) parent.getEnvironment().getEasSession().executeContextlessCommand(idsGetter.getCommandId(), commandEvent.getRqItem(), SysMonitoringRsDocument.class);
                    refreshDecorators(commandEvent.getRqItem(), rs.getSysMonitoringRs());
                    expandTreeItem(rs.getSysMonitoringRs(), commandEvent.getTreeItem());

                    ExpandedTreeItem rootExpandedItem = new ExpandedTreeItem();
                    rootExpandedItem.getExpandedChildItems().putAll(expandedItemIds);
                    ExpandedTreeItem infoAboutExpandedChilds = rootExpandedItem.getById(commandEvent.getTreeItem().getId());
                    if (infoAboutExpandedChilds != null) {
                        checkExpandedChildItems(commandEvent.getTreeItem(), infoAboutExpandedChilds);
                    }
                } catch (ServiceClientException ex) {
                    Logger.getLogger(UnitsTree.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    //ignore
                }

            } /*else if(qevent instanceof UpdateMetricsEvent){
             UpdateMetricsEvent updateMetricsEvent=(UpdateMetricsEvent)qevent;
             updateMetricsEvent.accept();
             if (!parent.getEnvironment().getEasSession().isBusy()){
             refreshDecoratorsAndDecorate();
             }
             }else if(qevent instanceof RereadEvent){
             qevent.accept();
             if (parent.getEnvironment().getEasSession().isBusy()){
             QApplication.postEvent(this, new RereadEvent());
             }else{
             parent.reread();
             }
             } else if(qevent instanceof UpdateCommandBarEvent){
             UpdateCommandBarEvent updateCommandBarEvent=(UpdateCommandBarEvent)qevent;
             qevent.accept();
             if (parent.getEnvironment().getEasSession().isBusy()){                   
             QApplication.postEvent(this, new UpdateCommandBarEvent(updateCommandBarEvent.getSelectedItem()));
             }else{
             parent.curItemChanged(updateCommandBarEvent.getSelectedItem());
             }
             }*/ else {
                super.customEvent(qevent);
            }
        }
    }

    public static class ExpandCommandEvent extends QEvent {

        private final SysMonitoringRq rqItem;
        private final InstanceTreeItem treeItem;

        public ExpandCommandEvent(InstanceTreeItem treeItem, SysMonitoringRq rqItem) {
            super(QEvent.Type.User);
            this.rqItem = rqItem;
            this.treeItem = treeItem;
        }

        public SysMonitoringRq getRqItem() {
            return rqItem;
        }

        public InstanceTreeItem getTreeItem() {
            return treeItem;
        }
    }

    public static class ExpandEvent extends QEvent {

        private final QTreeWidgetItem expandedItem;

        public ExpandEvent(QTreeWidgetItem expandedItem) {
            super(QEvent.Type.User);
            this.expandedItem = expandedItem;
        }

        QTreeWidgetItem getExpandedItem() {
            return expandedItem;
        }
    }

    public static class UpdateContextEventPanelEvent extends QEvent {

        private final QTreeWidgetItem expandedItem;

        public UpdateContextEventPanelEvent(QTreeWidgetItem expandedItem) {
            super(QEvent.Type.User);
            this.expandedItem = expandedItem;
        }

        QTreeWidgetItem getExpandedItem() {
            return expandedItem;
        }
    }
}
/*public static class ExpandEvent extends QEvent {
 private QTreeWidgetItem expandedItem;
        
 public ExpandEvent(QTreeWidgetItem expandedItem) {
 super(QEvent.Type.User);
 this.expandedItem=expandedItem;
 }
        
 QTreeWidgetItem  getExpandedItem(){
 return expandedItem;
 }            
 }
    
 public static class ScrollEventLogEvent extends QEvent {
 private ScrolledTableWidget table;

 public ScrollEventLogEvent(ScrolledTableWidget table) {
 super(QEvent.Type.User);
 this.table=table;
 }
        
 public ScrolledTableWidget getTable(){
 return table;
 }
 }
    
 public static class ScrollUnitTreeEvent extends QEvent {

 public ScrollUnitTreeEvent() {
 super(QEvent.Type.User);
 }
 }
    
 public static class UpdateMetricsEvent extends QEvent {

 public UpdateMetricsEvent() {
 super(QEvent.Type.User);
 }        
 }*/
/*public class InstancesInfo{
 private Long id;
 private boolean isExpanded=false;
    
 public InstancesInfo(Long id, boolean isExpanded){
 this.id=id;
 this.isExpanded=isExpanded;
 }
        
 public Long getId(){
 return id;
 }
 public boolean isExpanded(){
 return isExpanded;
 }
 }*/
