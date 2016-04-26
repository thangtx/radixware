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
package org.radixware.kernel.common.defs.ads.enumeration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.RadixObject.EEditState;
import org.radixware.schemas.adsdef.EnumDefinition;

public class ViewOrder {

    private final AdsEnumDef owner;
    private final List<Id> order;

    ViewOrder(AdsEnumDef owner) {
        this.order = new ArrayList<Id>();
        this.owner = owner;
    }

    ViewOrder(AdsEnumDef owner, List xDef) {
        this(owner);
        if (xDef != null) {
            for (Object obj : xDef) {
                Id id = Id.Factory.loadFrom((String) obj);
                if (owner.getItems().findById(id, EScope.ALL).get() != null) {
                    order.add(id);
                }
            }
        }
    }

    /**
     * Moves owner item on one position to the start of order list Returns true
     * if any changes in order list were occured
     */
    public boolean moveUp(AdsEnumItemDef item) {
        synchronized (order) {
            checkOrder();
            int index = order.indexOf(item.getId());
            if (index > 0) {
                Id id = order.get(index - 1);
                order.set(index - 1, item.getId());
                order.set(index, id);
                owner.setEditState(EEditState.MODIFIED);
                return true;
            }
            return false;
        }
    }

    public boolean setOrdered(AdsEnumItemDef item, boolean ordered) {
        synchronized (order) {
            if (ordered) {
                int index = order.indexOf(item.getId());
                if (index >= 0) {
                    return false;
                } else {
                    order.add(item.getId());
                    owner.setEditState(EEditState.MODIFIED);
                    return true;
                }
            } else {
                int index = order.indexOf(item.getId());
                if (index >= 0) {
                    order.remove(index);
                    owner.setEditState(EEditState.MODIFIED);
                    return true;
                } else {
                    return false;
                }
            }
        }
    }

    /**
     * Moves owner item on one position to the start of order list Returns true
     * if any changes in order list were occured
     */
    public boolean moveDn(AdsEnumItemDef item) {
        synchronized (order) {
            checkOrder();
            int index = order.indexOf(item.getId());
            if (index + 1 < order.size()) {
                Id id = order.get(index + 1);
                order.set(index + 1, item.getId());
                order.set(index, id);
                owner.setEditState(EEditState.MODIFIED);
                return true;
            }
            return false;
        }
    }

    void appendTo(EnumDefinition xDef) {
        synchronized (order) {
            List<String> viewOrderAsStr = new ArrayList<>(order.size());
            for (Id id : order) {
                viewOrderAsStr.add(id.toString());
            }
            xDef.setViewOrder(viewOrderAsStr);
        }
    }

    public List<AdsEnumItemDef> getOrder() {
        synchronized (order) {
            ArrayList<AdsEnumItemDef> itemOrder = new ArrayList<>(order.size());
            for (Id id : order) {
                AdsEnumItemDef item = owner.getItems().findById(id, EScope.ALL).get();
                if (item != null) {
                    itemOrder.add(item);
                }
            }
            for (AdsEnumItemDef other : owner.getItems().get(EScope.ALL)) {
                if (!order.contains(other.getId())) {
                    itemOrder.add(other);
                }
            }
            return itemOrder;
        }
    }

    public List<Id> getOrderedItemIds() {
        synchronized (order) {
            final List<Id> result = new ArrayList<>(order);
            for (AdsEnumItemDef other : owner.getItems().get(EScope.ALL)) {
                if (!result.contains(other.getId())) {
                    result.add(other.getId());
                }
            }
            return result;
        }
    }

    private void checkOrder() {
        synchronized (owner) {
            synchronized (order) {
                for (AdsEnumItemDef item : owner.getItems().get(EScope.ALL)) {
                    if (!order.contains(item.getId())) {
                        order.add(item.getId());
                    }
                }
            }
        }
    }
    private static final Comparator<AdsEnumItemDef> nameComparator = new Comparator<AdsEnumItemDef>() {

        @Override
        public int compare(AdsEnumItemDef o1, AdsEnumItemDef o2) {
            return o1.getName().compareTo(o2.getName());
        }
    };

    int getItemOrder(AdsEnumItemDef item) {
        List<Id> orderedList = getOrderedItemIds();
        int index = orderedList.indexOf(item.getId());
        if (index < 0) {//not ordered
            index = order.size();
            List<AdsEnumItemDef> items = new LinkedList<AdsEnumItemDef>();
            for (AdsEnumItemDef ei : owner.getItems().get(EScope.ALL)) {
                if (!order.contains(ei.getId())) {
                    items.add(ei);
                }
            }

            Collections.sort(items, nameComparator);
            int offset = items.indexOf(item);
            if (offset >= 0) {
                return index + offset;
            } else {
                return index;
            }
        } else {
            return index;
        }
    }

    public boolean isCorrect() {
        for (Id id : order) {
            if (owner.getItems().findById(id, EScope.ALL).get() == null) {
                return false;
            }
        }
        return true;
    }

    public void fixup() {
        for (int i = 0; i < order.size();) {
            Id id = order.get(i);
            if (owner.getItems().findById(id, EScope.ALL).get() == null) {
                order.remove(i);
            } else {
                i++;
            }
        }
    }
}
