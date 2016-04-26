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
package org.radixware.kernel.common.client.meta.explorerItems;

import java.util.*;
import org.radixware.kernel.common.client.meta.IExplorerItemsHolder;
import org.radixware.kernel.common.client.meta.IModelDefinition;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.types.Id;

/**
 * Class-container of {@link RadExplorerItemDef explorer items} definitions.
 */
public final class RadExplorerItems implements Iterable<RadExplorerItemDef> {

    /**
     * Container which has no explorer items.
     */
    public final static RadExplorerItems EMPTY = new RadExplorerItems(new RadExplorerItemDef[0], null);

    private final List<RadExplorerItemDef> items = new ArrayList<>();
    private final Map<Id, RadExplorerItemDef> itemsById;
    private final List<Id> orderedIds;

    /**
     * Constructs a container with specified
     * {@link RadExplorerItemDef explorer items}.
     *
     * @param explorerItems items in this container.
     * @param explorerItemsSettings defines an order of items and their
     * visibility.
     */
    public RadExplorerItems(final RadExplorerItemDef[] explorerItems, final Id[] orderedIds) {
        this(explorerItems, null, orderedIds, null);
    }

    public RadExplorerItems(final RadExplorerItemDef[] explorerItems, final RadExplorerItems mergeWith, final Id[] orderedIds) {
        this(explorerItems, mergeWith, orderedIds, null);
    }

    /**
     * Constructs a container by merging specified
     * {@link RadExplorerItemDef explorer items} and
     * {@link RadExplorerItemDef explorer items} in another container.
     *
     * @param explorerItems items in this container. Cannot be
     * <code>null</code>.
     * @param mergeWith explorer items container to merge with. May be
     * <code>null</code>.
     * @param orderedIds array of explorer items identifiers that defines items
     * order. May be <code>null</code>.
     * @param visibleItems collection of visible explorer items. All explorer
     * items are visible if this parameter is <code>null</code> and all items
     * are hidden when collection is empty.
     */
    public RadExplorerItems(final RadExplorerItemDef[] explorerItems, final RadExplorerItems mergeWith, final Id[] orderedIds, final Id[] filter) {
        if (explorerItems != null) {
            Collections.addAll(items, explorerItems);
        }
        final Set<Id> test = filter == null ? null : new HashSet<Id>();
        if (filter != null) {
            for (int i = 0; i < filter.length; i++) {
                test.add(filter[i]);
            }
        }

        if (mergeWith != null) {

            for (RadExplorerItemDef item : mergeWith.items) {
                if (!existsById(item.getId()) && (test == null || test.contains(item.getId()))) {
                    items.add(item);
                }
            }
        }
        itemsById = new HashMap<>(items.size() * 2);
        if (orderedIds == null) {
            this.orderedIds = Collections.emptyList();
        } else {
            this.orderedIds = new ArrayList<>(orderedIds.length);
            Collections.addAll(this.orderedIds, orderedIds);
        }
        reorderItems(this.orderedIds);
    }

    private boolean existsById(final Id id) {
        for (RadExplorerItemDef item : items) {
            if (item.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    

  
    /**
     * Returns the explorer item with specified identifier. The search is
     * performed recursively by child explorer items requested from
     * {@link IModelDefinition#getChildrenExplorerItems()} method.
     * <p>
     * If no explorer item found method returns null.
     *
     * @param id identifier of explorer item search for
     * @return explorer item with specified identifier.
     */
    public RadExplorerItemDef findExplorerItem(final Id id) {
        if (itemsById.containsKey(id)) {
            return itemsById.get(id);
        }

        final Stack<RadExplorerItemDef> items_to_scan = new Stack<>();
        final ArrayList<Id> already_scaned = new ArrayList<>();

        for (RadExplorerItemDef ei : items) {
            items_to_scan.push(ei);
        }

        while (!items_to_scan.empty()) {
            RadExplorerItemDef item = items_to_scan.pop();

            if (item.getId().equals(id)) {
                itemsById.put(id, item);
                return item;
            }

            if (item.getModelDefinitionId() != null) {
                final IModelDefinition modelDefinition;
                try {
                    modelDefinition = item.getModelDefinition();
                } catch (DefinitionError err) {
                    continue;//cant get child explorer item - continue searching;
                }
                if (modelDefinition instanceof IExplorerItemsHolder) {
                    final RadExplorerItems child_items;
                    try {
                        child_items = ((IExplorerItemsHolder) modelDefinition).getChildrenExplorerItems();
                    } catch (DefinitionError err) {
                        continue;//cant get child explorer item - continue searching;
                    }
                    for (RadExplorerItemDef ei : child_items) {
                        if (!already_scaned.contains(ei.getId())) {
                            already_scaned.add(ei.getId());
                            items_to_scan.add(ei);
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Returns iterator by {@link RadExplorerItemDef explorer items} in this
     * container.
     *
     * @return iterator by explorer item.
     */
    @Override
    public Iterator<RadExplorerItemDef> iterator() {
        return items.iterator();
    }

    /**
     * Returns <code>true</code> if this container contains no explorer items.
     *
     * @return <code>true</code> if this container contains no explorer items
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }

    /**
     * Reorders explorer items according to the order of items' id's. In case
     * when an item doesn't have corresponding Id in the list, it's pushed to
     * the end of the resulting list.
     *
     * @param orderedIds list with properly ordered Id's
     */
    private void reorderItems(final List<Id> orderedIds) {
        if (orderedIds == null || orderedIds.isEmpty()) {
            return;
        } else {
            Collections.sort(items, new Comparator<RadExplorerItemDef>() {

                private int index1 = -1;
                private int index2 = -1;

                @Override
                public int compare(RadExplorerItemDef item1, RadExplorerItemDef item2) {
                    findIndicies(item1, item2);

                    try {
                        if (index1 >= 0 && index2 >= 0) {
                            return Integer.compare(index1, index2);
                        } else if (index1 >= 0) {
                            return -1;               // ----
                        } else if (index2 >= 0) {    // это сравнение обеспечивает перемещение неуказанных элементов в конец списка
                            return 1;                // ----
                        } else {
                            return 0;
                        }
                    } finally {
                        index1 = -1;
                        index2 = -1;
                    }
                }

                private void findIndicies(RadExplorerItemDef item1, RadExplorerItemDef item2) {
                    for (int i = 0; i < orderedIds.size(); i++) {
                        final Id id = orderedIds.get(i);
                        if (item1.getId().equals(id)) {
                            index1 = i;
                        }
                        if (item2.getId().equals(id)) {
                            index2 = i;
                        }
                        if (index1 >= 0 && index2 >= 0) {
                            break;
                        }
                    }
                }

            });
        }
    }
}
