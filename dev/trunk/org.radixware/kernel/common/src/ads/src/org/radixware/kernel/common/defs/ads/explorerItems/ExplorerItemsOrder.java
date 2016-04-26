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
package org.radixware.kernel.common.defs.ads.explorerItems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.EntireChangesSupport;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.HierarchyIterator;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.ChildExplorerItems;
import org.radixware.schemas.adsdef.ExplorerItemExternalCondition;

public class ExplorerItemsOrder extends RadixObject {

    public static class OrderedItem extends RadixObject {

        private Id explorerItemId;
        private boolean isVisible;

        public OrderedItem(AdsExplorerItemDef ei) {
            this.explorerItemId = ei.getId();
        }

        public OrderedItem(Id explorerItemId) {
            this.explorerItemId = explorerItemId;
        }

        public OrderedItem(Id explorerItemId, boolean isVisible) {
            this.explorerItemId = explorerItemId;
            this.isVisible = isVisible;
        }

        public boolean isVisible() {
            return isVisible;
        }

        public void setVisible(boolean isVisible) {
            this.isVisible = isVisible;
            setEditState(EEditState.MODIFIED);
        }

        public Id getExplorerItemId() {
            return explorerItemId;
        }
    }

    public List<Id> getOrderRoots() {
        return new ArrayList<>(orderMap.keySet());
    }

    public List<Id> getVisibilityRoots() {
        return new ArrayList<>(hideMap.keySet());
    }

    public List<OrderedItem> listOrderItems(Id rootId) {
        ItemList list = orderMap.get(rootId);
        if (list != null) {
            return list.list();
        } else {
            return Collections.emptyList();
        }
    }

    public List<OrderedItem> listVisibilityItems(Id rootId) {
        ItemList list = hideMap.get(rootId);
        if (list != null) {
            return list.list();
        } else {
            return Collections.emptyList();
        }
    }

    public static class ItemList extends RadixObjects<OrderedItem> {

        public ItemList(ExplorerItemsOrder container) {
            setContainer(container);
        }

        private boolean containsId(Id id) {
            for (OrderedItem item : this) {
                if (item.explorerItemId == id) {
                    return true;
                }
            }
            return false;
        }

        private void removeId(Id id) {
            OrderedItem item = findById(id);
            if (item != null) {
                remove(item);
                EntireChangesSupport.getInstance(AdsExplorerItemDef.class).fireChange(getOwnerExplorerItems());
            }
        }

        private OrderedItem findById(Id id) {
            for (OrderedItem item : this) {
                if (item.explorerItemId == id) {
                    return item;
                }
            }
            return null;
        }

        private ExplorerItems getOwnerExplorerItems() {
            return ((ExplorerItemsOrder) getContainer()).getOwnerExplorerItems();
        }
    }
    private final Map<Id, ItemList> orderMap = new HashMap<>();
    private final Map<Id, ItemList> hideMap = new HashMap<>();
    private final RadixObjects.ContainerChangesListener containerListener;

    public ExplorerItemsOrder(final ExplorerItems items, ChildExplorerItems xDef) {
        setContainer(items);
        if (xDef != null) {
            if (xDef.getOrder() != null) {
                loadExternalCondition(xDef.getOrder(), orderMap);
            }
            if (xDef.getVisibility() != null) {
                loadExternalCondition(xDef.getVisibility(), hideMap);
            }
        }
        items.getChildren().getLocal().getContainerChangesSupport().addEventListener(containerListener = new RadixObjects.ContainerChangesListener() {
            @Override
            public void onEvent(RadixObjects.ContainerChangedEvent e) {
                if (e.changeType == RadixObjects.EChangeType.SHRINK && e.object instanceof AdsExplorerItemDef && !e.object.isInBranch()) {
                    final AdsExplorerItemDef explorerItem = (AdsExplorerItemDef) e.object;
                    for (ItemList list : items.getItemsOrder().orderMap.values()) {
                        list.removeId(explorerItem.getId());
                    }
                    for (ItemList list : items.getItemsOrder().hideMap.values()) {
                        list.removeId(explorerItem.getId());
                    }
                }
            }
        });
    }

    private void loadExternalCondition(ExplorerItemExternalCondition condition, Map<Id, ItemList> map) {
        List<ExplorerItemExternalCondition.Item> orderItems = condition.getItemList();
        if (orderItems != null && !orderItems.isEmpty()) {

            for (ExplorerItemExternalCondition.Item item : orderItems) {
                if (item.getTargetId() != null) {
                    List<ExplorerItemExternalCondition.Item.Element> elements = item.getElementList();
                    if (!elements.isEmpty()) {
                        ItemList orderList = new ItemList(this);
                        for (ExplorerItemExternalCondition.Item.Element el : elements) {
                            orderList.add(new OrderedItem(el.getId(), el.getVisible()));
                        }
                        map.put(item.getTargetId(), orderList);
                    }
                }
            }
        }
    }

    private void storeExternalCondition(Map<Id, ItemList> map, ExplorerItemExternalCondition condition) {
        List<Id> ids = new ArrayList<>(map.keySet());
        Collections.sort(ids);

        for (Id id : ids) {
            ItemList itemList = map.get(id);
            if (!itemList.isEmpty()) {
                ExplorerItemExternalCondition.Item xItem = condition.addNewItem();
                xItem.setTargetId(id);
                for (OrderedItem item : itemList) {
                    ExplorerItemExternalCondition.Item.Element xElement = xItem.addNewElement();
                    xElement.setId(item.explorerItemId);
                    if (item.isVisible) {
                        xElement.setVisible(true);
                    }
                }
            }
        }
    }

    public ExplorerItems getOwnerExplorerItems() {
        return (ExplorerItems) getContainer();
    }

    public static class ExplorerItemRef {

        private final Id id;
        private final AdsExplorerItemDef explorerItem;

        public ExplorerItemRef(Id id, AdsExplorerItemDef explorerItem) {
            this.id = id;
            this.explorerItem = explorerItem;
        }

        public Id getId() {
            return id;
        }

        public AdsExplorerItemDef getExplorerItem() {
            return explorerItem;
        }
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider); //To change body of generated methods, choose Tools | Templates.
        for (ItemList list : orderMap.values()) {
            list.visit(visitor, provider);
        }
        for (ItemList list : hideMap.values()) {
            list.visit(visitor, provider);
        }
    }

    public ItemList getHidingRules(ExplorerItems rootContext, boolean createIfNotExists) {
        if (rootContext == null) {
            return null;
        }
        Definition owner = this.getOwnerDefinition();
        if (owner != null) {
            ItemList list = rootContext.getItemsOrder().hideMap.get(owner.getId());
            if (list == null && createIfNotExists) {
                list = new ItemList(rootContext.getItemsOrder());
                rootContext.getItemsOrder().hideMap.put(owner.getId(), list);
                rootContext.getItemsOrder().setEditState(EEditState.MODIFIED);
            }
            return list;
        } else {
            return null;
        }
    }

    public class OrderAndVisibilityRules {

        public final Id[] order;
        public final Map<Id, Boolean> visible;
        public final Id rootId;

        public OrderAndVisibilityRules(Id rootId, Id[] order, Map<Id, Boolean> visible) {
            this.order = order;
            this.visible = visible;
            this.rootId = rootId;
        }
    }

    public List<OrderAndVisibilityRules> getOrderAndVisibilityRules() {
        final List<OrderAndVisibilityRules> result = new LinkedList<>();
        final List<Id> allIds = new LinkedList<>();
        for (Id id : orderMap.keySet()) {
            allIds.add(id);
        }
        for (Id id : hideMap.keySet()) {
            if (!allIds.contains(id)) {
                allIds.add(id);
            }
        }

        for (Id id : allIds) {
            List<Id> order = null;
            ItemList list = orderMap.get(id);
            if (list != null && !list.isEmpty()) {
                order = new ArrayList<>(list.size());
                for (OrderedItem item : list) {
                    order.add(item.explorerItemId);
                }
            }
            Map<Id, Boolean> visible = null;;
            list = hideMap.get(id);
            if (list != null && !list.isEmpty()) {
                visible = new HashMap<>(list.size());
                for (OrderedItem item : list) {
                    visible.put(item.explorerItemId, item.isVisible);
                }
            }
            if ((order != null && !order.isEmpty()) || (visible != null && !visible.isEmpty())) {
                result.add(new OrderAndVisibilityRules(id, order == null ? null : order.toArray(new Id[order.size()]), visible));
            }
        }
        return result;
    }

    public ItemList getOrderRules(ExplorerItems rootContext, boolean createIfNotExists) {
        if (rootContext == null) {
            return null;
        }
        Definition owner = this.getOwnerDefinition();
        if (owner != null) {
            ItemList list = rootContext.getItemsOrder().orderMap.get(owner.getId());
            if (list == null && createIfNotExists) {
                list = new ItemList(rootContext.getItemsOrder());
                rootContext.getItemsOrder().orderMap.put(owner.getId(), list);
                rootContext.getItemsOrder().setEditState(EEditState.MODIFIED);
            }
            return list;
        } else {
            return null;
        }
    }

    public void removeOrderRules(ExplorerItems rootContext) {
        Definition owner = this.getOwnerDefinition();
        if (owner != null) {
            rootContext.getItemsOrder().orderMap.remove(owner.getId());
            rootContext.getItemsOrder().setEditState(EEditState.MODIFIED);
        }
    }

    /**
     * resulting list may contains nulls
     */
    public List<ExplorerItemRef> listChildren(final ExplorerItems rootContext) {
        final Map<Id, AdsExplorerItemDef> id2child = new HashMap<>();
        List<Id> currentOrder = this.listChildrenInCurrentOrder(rootContext, id2child);

        List<ExplorerItemRef> result = new LinkedList<>();
        for (Id id : currentOrder) {
            result.add(new ExplorerItemRef(id, id2child.get(id)));

        }
        return result;
    }

    public List<AdsExplorerItemDef> listHiddenChildren(final ExplorerItems rootContext) {
        List<AdsExplorerItemDef> result = new LinkedList<>();
        final ExplorerItems ownerExplorerItems = getOwnerExplorerItems();

        Definition ownerDef = getOwnerExplorerItems().getOwnerDefinition();
        boolean isTopItem = ownerExplorerItems.findOwnerEditorPresentation() == ownerDef || ownerExplorerItems.findOwnerTopLevelParagraph() == ownerDef;

        for (AdsExplorerItemDef def : ownerExplorerItems.getChildren().get(ExtendableDefinitions.EScope.ALL, new IFilter<AdsExplorerItemDef>() {

            @Override
            public boolean isTarget(AdsExplorerItemDef radixObject) {
                return prefilter(ownerExplorerItems, radixObject);
            }
        })) {
            ExplorerItems matchItems = isTopItem ? ownerExplorerItems : def.getOwnerExplorerItems();
            if (!matchItems.getItemsOrder().isItemVisible(rootContext, def)) {
                result.add(def);
            }
        }
        return result;
    }

    public boolean hasHiddenChildren(final ExplorerItems rootContext) {
        final ExplorerItems ownerExplorerItems = getOwnerExplorerItems();
        Definition ownerDef = getOwnerExplorerItems().getOwnerDefinition();
        boolean isTopItem = ownerExplorerItems.findOwnerEditorPresentation() == ownerDef || ownerExplorerItems.findOwnerTopLevelParagraph() == ownerDef;

        for (AdsExplorerItemDef def : ownerExplorerItems.getChildren().get(ExtendableDefinitions.EScope.ALL, new IFilter<AdsExplorerItemDef>() {

            @Override
            public boolean isTarget(AdsExplorerItemDef radixObject) {
                return prefilter(ownerExplorerItems, radixObject);
            }
        })) {
            ExplorerItems matchItems = isTopItem ? ownerExplorerItems : def.getOwnerExplorerItems();
            if (!matchItems.getItemsOrder().isItemVisible(rootContext, def)) {
                return true;
            }
        }
        return false;
    }

    private List<Id> listChildrenInNaturalOrder(final ExplorerItems rootContext, final Map<Id, AdsExplorerItemDef> map) {
        List<Id> result = new LinkedList<>();
        final ExplorerItems ownerExplorerItems = getOwnerExplorerItems();
        for (AdsExplorerItemDef def : ownerExplorerItems.getChildren().get(ExtendableDefinitions.EScope.ALL, new IFilter<AdsExplorerItemDef>() {

            @Override
            public boolean isTarget(AdsExplorerItemDef radixObject) {
                return prefilter(ownerExplorerItems, radixObject);
            }
        })) {
            if (isItemVisible(rootContext, def)) {
                if (map != null) {
                    map.put(def.getId(), def);
                }
                result.add(def.getId());
            }
        }
        return result;
    }

    //checks if given explorer items can attached to given explorer item set as inherited or as own by 
    private boolean prefilter(ExplorerItems ownerExplorerItems, AdsExplorerItemDef def) {

        if (def.getOwnerExplorerItems() == ownerExplorerItems) {
            return true;
        }

        AdsDefinition ownerDef = ownerExplorerItems.getOwnerDef();

        if (ownerDef instanceof AdsEditorPresentationDef) {
            AdsEditorPresentationDef epr = (AdsEditorPresentationDef) ownerDef;
            if (epr.isExplorerItemsInherited() || ownerExplorerItems.isChildInherited(def.getId())) {
                return true;
            } else {
                ExplorerItems container = def.getOwnerExplorerItems();
                if (container == null) {
                    return false;
                }
                AdsDefinition containerOwner = container.getOwnerDef();
                if (containerOwner instanceof AdsEditorPresentationDef && containerOwner.getId() == ownerDef.getId()) {
                    AdsClassDef thisOwnerClass = epr.getOwnerClass();
                    AdsClassDef thatOwnerClass = ((AdsEditorPresentationDef) containerOwner).getOwnerClass();
                    if (thisOwnerClass != null && thatOwnerClass != null && thisOwnerClass.getId() == thatOwnerClass.getId()) {
                        return true;
                    }
                }
                return false;
            }
        } else if (ownerDef instanceof AdsParagraphExplorerItemDef) {
            if (((AdsParagraphExplorerItemDef) ownerDef).isExplorerItemsInherited() || ownerExplorerItems.isChildInherited(def.getId())) {
                return true;
            }

        }

        return false;
    }

    private List<Id> listChildrenInCurrentOrder(ExplorerItems rootContext, Map<Id, AdsExplorerItemDef> map) {
        ExplorerItemsOrder lookuper = this;
        final ExplorerItemsOrder rootlookuper = this;
        ExplorerItems items = lookuper.getOwnerExplorerItems();
        AdsDefinition ownerDef = items.getOwnerDef();
        boolean isHierarchicalLookuper = ownerDef != null && (ownerDef.isTopLevelDefinition() || ownerDef == this.getOwnerExplorerItems().findOwnerEditorPresentation());

        while (true) {
            ItemList list = lookuper.getOrderRules(rootContext, false);
            if (list == null) {
                ExplorerItems next = findNextExplorerItems(rootContext);
                while (next != null) {
                    list = lookuper.getOrderRules(next, false);
                    if (list != null) {
                        break;
                    }
                    next = findNextExplorerItems(next);
                }
            }
            if (list == null) {
                if (isHierarchicalLookuper) {
                    items = findNextExplorerItems(items);
                    if (items == null) {
                        return listChildrenInNaturalOrder(rootContext, map);
                    }
                    lookuper = items.getItemsOrder();
                    if (lookuper == null) {
                        return listChildrenInNaturalOrder(rootContext, map);
                    }
                } else {
                    return listChildrenInNaturalOrder(rootContext, map);
                }

            } else {
                List<Id> result = new LinkedList<>();
                Set<Id> addedIds = new HashSet<>();
                final ExplorerItems eis = rootlookuper.getOwnerExplorerItems();
                for (OrderedItem item : list) {
                    final Id id = item.explorerItemId;
                    if (!eis.getChildren().findById(id, ExtendableDefinitions.EScope.ALL).isEmpty() && rootlookuper.isItemVisible(rootContext, id)) {
                        result.add(id);
                        addedIds.add(id);
                    }
                }

                final ExplorerItems ownerExplorerItems = rootlookuper.getOwnerExplorerItems();
                for (AdsExplorerItemDef def : ownerExplorerItems.getChildren().get(ExtendableDefinitions.EScope.ALL, new IFilter<AdsExplorerItemDef>() {

                    @Override
                    public boolean isTarget(AdsExplorerItemDef radixObject) {
                        return prefilter(ownerExplorerItems, radixObject);
                    }
                })) {
                    final Id id = def.getId();
                    if (map != null) {
                        map.put(id, def);
                    }
                    if (addedIds.contains(id)) {
                        continue;
                    }
                    if (rootlookuper.isItemVisible(rootContext, id)) {
                        result.add(id);
                        addedIds.add(id);
                    }
                }
                return result;
            }
        }
    }

    public void reorder(ExplorerItems rootContext, int[] perm) {
        //first build current item list
        List<Id> naturalOrder = listChildrenInNaturalOrder(rootContext, null);
        List<Id> currentOrder = listChildrenInCurrentOrder(rootContext, null);

        if (naturalOrder.size() != currentOrder.size()) {
            return;
        }

        if (perm.length != currentOrder.size()) {
            return;
        }
        List<Id> resultOrder = new ArrayList<>(perm.length);

        for (int i = 0; i < perm.length; i++) {
            resultOrder.add(null);
        }
        for (int i = 0; i < perm.length; i++) {
            resultOrder.set(perm[i], currentOrder.get(i));
        }

        //optimize order list length
        int size = resultOrder.size();

        for (int i = size - 1; i >= 0; i--) {
            if (resultOrder.get(i) == naturalOrder.get(i)) {
                size--;
            } else {
                break;
            }
        }

        ItemList list = this.getOrderRules(rootContext, true);
        list.clear();
        for (Id id : resultOrder) {
            list.add(new OrderedItem(id));
        }

        setEditState(EEditState.MODIFIED);
        EntireChangesSupport
                .getInstance(AdsExplorerItemDef.class
                ).fireChange(getOwnerExplorerItems());
    }

    public boolean isItemVisible(ExplorerItems rootContext, AdsExplorerItemDef explorerItem) {
        return isItemVisible(rootContext, explorerItem.getId());
    }

    public boolean isItemVisible(ExplorerItems rootContext, Id explorerItemId) {
        if (rootContext == null) {
            return false;
        }

        ExplorerItemsOrder lookuper = this;

        ExplorerItems items = lookuper.getOwnerExplorerItems();
        AdsDefinition ownerDef = items.getOwnerDef();
        boolean isHierarchicalLookuper = ownerDef != null && (ownerDef.isTopLevelDefinition() || ownerDef == this.getOwnerExplorerItems().findOwnerEditorPresentation());

        while (true) {
            ItemList hideList = lookuper.getHidingRules(rootContext, false);

            if (hideList != null) {
                OrderedItem item = hideList.findById(explorerItemId);
                if (item != null) {
                    return item.isVisible();
                } else {
                    ExplorerItems nextRoot = findNextExplorerItems(rootContext);
                    if (nextRoot != null) {
                        return lookuper.isItemVisible(nextRoot, explorerItemId);
                    } else {
                        return true;
                    }
                }
            } else {
                if (isHierarchicalLookuper) {
                    items = findNextExplorerItems(items);
                    if (items == null) {
                        break;
                    }
                    lookuper = items.getItemsOrder();
                    if (lookuper == null) {
                        break;
                    }
                } else {
                    break;
                }
            }
        }
        ExplorerItems nextRoot = findNextExplorerItems(rootContext);
        if (nextRoot != null) {
            return isItemVisible(nextRoot, explorerItemId);
        } else {
            return true;
        }
    }

    private static ExplorerItems findNextExplorerItems(ExplorerItems root) {
        if (root == null) {
            return null;
        }
        AdsEditorPresentationDef epr = root.findOwnerEditorPresentation();
        if (epr != null) {
            HierarchyIterator<AdsEditorPresentationDef> iter = epr.getHierarchyIterator(ExtendableDefinitions.EScope.ALL, HierarchyIterator.Mode.FIND_FIRST);
            AdsEditorPresentationDef next = iter.next().first();
            assert next == epr;
            next = iter.next().first();
            if (next != null) {
                return next.getExplorerItems();
            }
        } else {
            AdsParagraphExplorerItemDef p = root.findOwnerTopLevelParagraph();
            if (p != null) {
                p = (AdsParagraphExplorerItemDef) p.getHierarchy().findOverwritten().get();
                if (p != null) {
                    return p.getExplorerItems();
                }
            }
        }
        return null;
    }

    public void removeBrokenReference(ExplorerItems rootContext, Id id) {
        ItemList list = getOrderRules(rootContext, false);
        if (list != null) {
            list.removeId(id);
        }

    }

    public void setItemVisible(ExplorerItems rootContext, AdsExplorerItemDef explorerItem, boolean visible) {
        if (rootContext == null) {
            return;
        }
        if (isItemVisible(rootContext, explorerItem) == visible) {
            return;
        }
        ItemList hideList = this.getHidingRules(rootContext, true);

        OrderedItem item = hideList.findById(explorerItem.getId());

        if (item == null) {
            item = new OrderedItem(explorerItem);
            hideList.add(item);
        } else {
            hideList.removeId(explorerItem.getId());
            if (isItemVisible(rootContext, explorerItem) == visible) {
                return;
            } else {
                item = new OrderedItem(explorerItem);
                hideList.add(item);
            }
        }
        item.setVisible(visible);
        EntireChangesSupport
                .getInstance(AdsExplorerItemDef.class
                ).fireChange(getOwnerExplorerItems());
    }

    public void appendTo(ChildExplorerItems xDef) {
        if (!orderMap.isEmpty()) {
            storeExternalCondition(orderMap, xDef.addNewOrder());
        }
        if (!hideMap.isEmpty()) {
            storeExternalCondition(hideMap, xDef.addNewVisibility());
        }

    }
}
