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

package org.radixware.kernel.common.client.widgets.selector;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.RadSortingDef;
import org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem;
import org.radixware.kernel.common.client.meta.RadUserSorting;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.items.SelectorColumnModelItem;
import org.radixware.kernel.common.client.models.items.SelectorColumns;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;


public final class SelectorSortUtils {

    public final static String ASC_ARROW = "\u2191";
    public final static String DESC_ARROW = "\u2193";

    private SelectorSortUtils() {
    }

    public static RadSortingDef.SortingItem getSortItemById(final List<SortingItem> sortingItems, final Id id) {
        for (RadSortingDef.SortingItem i : sortingItems) {
            if (i.propId.equals(id)) {
                return i;
            }
        }
        return null;
    }

    /**
     * Builds a name for a sort by its settings set
     *
     * @param sortingItems The list of sort settings for each column to sort by
     * @param groupModel group model that contains corresponding column items
     * @return
     */
    public static String getSortName(final List<SortingItem> sortingItems,
            final GroupModel groupModel) {
        final StringBuilder sb = new StringBuilder();
        final IClientEnvironment environment = groupModel.getEnvironment();
        final String bySign = environment.getMessageProvider().translate("Selector", "By");
        final String descSign = environment.getMessageProvider().translate("SelectorAddons", "desc.");
        final SelectorColumns selectorColumns = groupModel.getSelectorColumns();
        sb.append(bySign);
        sb.append(" ");
        for (SortingItem selected : sortingItems) {
            final SelectorColumnModelItem item = selectorColumns.getColumnByPropertyId(selected.propId);
            if (selected.propId.equals(item.getId())) {
                sb.append("\"");
                sb.append(item.getTitle());
                sb.append("\"");
                if (selected.sortDesc) {
                    sb.append("(");
                    sb.append(descSign);
                    sb.append(")");
                }
                sb.append(", ");
            }
        }
        sb.delete(sb.length() - 2, sb.length() - 1);
        return sb.toString();
    }

    /**
     * Applies a sort by specified column to a group model
     *
     * @param groupModel the group model to apply the sort to
     * @param columnId column identifier
     * @param appendToCurrent If this parameter is <code>true</code> then column
     * will be appended to current sorting. If this parameter      * is <code>false</code> then single column sorting will be applied
     */
    public static void applySort(final GroupModel groupModel, final Id columnId, final boolean appendToCurrent) {
        final List<SortingItem> sortingItems = prepareColumns(groupModel, columnId, appendToCurrent);
        RadSortingDef sorting =
                groupModel.getSortings().findSortingByColumns(sortingItems, groupModel.getCurrentFilter());
        if (sorting == null) {
            if (groupModel.getSortings().canCreateNew(groupModel.getCurrentFilter())) {
                final String sortingName = getSortName(sortingItems, groupModel);
                sorting = RadUserSorting.Factory.newInstance(
                        groupModel.getEnvironment(),
                        groupModel.getSelectorPresentationDef().getOwnerClassId(),
                        sortingName);
                ((RadUserSorting) sorting).setSortingColumns(groupModel.getEnvironment(), sortingItems);
                groupModel.getSortings().add(sorting, null, groupModel.getSortings().getSettingsCount());
            } else {
                return;
            }
        }

        try {
            groupModel.setSorting(sorting);
        } catch (ServiceClientException ex) {
            groupModel.showException(groupModel.getEnvironment().getMessageProvider().translate("Selector", "Can't Apply Sorting"), ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(SelectorSortUtils.class.getName()).log(Level.FINE, ex.getMessage(), ex);
        }
    }

    private static List<SortingItem> prepareColumns(final GroupModel groupModel, final Id colId, final boolean appendToCurrentSorting) {
        final List<SortingItem> sortColumns = new ArrayList<>();
        final RadSortingDef currentSorting = groupModel.getCurrentSorting();
        List<SortingItem> currentSortColumns = new ArrayList<>();
        if (currentSorting != null) {
            currentSortColumns = currentSorting.getSortingColumns();
        }
        if (appendToCurrentSorting) {
            if (currentSorting != null) {
                sortColumns.addAll(currentSortColumns);
            }

            SortingItem sortingItem = SelectorSortUtils.getSortItemById(sortColumns, colId);
            if (sortingItem == null) {
                sortingItem = new SortingItem(colId, SortingItem.SortOrder.ASC);
                sortColumns.add(sortingItem);
            } else {
                //Запоминаем индекс существующей колонки, чтобы сохранить порядок сортировки
                final int indexOfColumn = sortColumns.indexOf(sortingItem);
                sortColumns.remove(indexOfColumn);
                sortingItem = sortingItem.getInvertedSorting();
                sortColumns.add(indexOfColumn, sortingItem);
            }

        } else {
            //try to find selected column in current sort. if NOT NULL then create a new sort with inverted order of selected column
            final SortingItem tryFind = SelectorSortUtils.getSortItemById(currentSortColumns, colId);
            final SortingItem sortItem = (tryFind == null)
                    ? new SortingItem(colId, SortingItem.SortOrder.ASC)
                    : tryFind.getInvertedSorting();
            sortColumns.add(sortItem);
        }

        return sortColumns;
    }

    public static String getSortIndicator(final int number, final SortingItem.SortOrder order) {
        final String text = String.valueOf(number);
        final StringBuilder result = new StringBuilder();
        if (order == SortingItem.SortOrder.ASC) {
            result.append(ASC_ARROW);
        } else {
            result.append(DESC_ARROW);
        }
        result.append(text);
        return result.toString();
    }
}
