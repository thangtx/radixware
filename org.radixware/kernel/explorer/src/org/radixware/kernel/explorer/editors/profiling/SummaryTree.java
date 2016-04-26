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
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.progress.TaskWaiter;


public class SummaryTree extends AbstractProfileTree {

    private Timestamp from = null;
    private Timestamp to = null;
    

    public SummaryTree(final List<TreeItem> items, final ProfilerWidget parent,final boolean isPercentMode) {
        super(parent,isPercentMode);
        this.isPercentMode = isPercentMode;
        //tree.expanded.connect(this, "resizeColumns()");
        fullPeriodList = items;
    }

    public SummaryTree(final List<TreeItem> items, final ProfilerWidget parent, final Timestamp from, final Timestamp to, final boolean isPercentMode) {
        super(parent,isPercentMode);
        this.isPercentMode = isPercentMode;
        //tree.expanded.connect(this, "resizeColumns()");
        this.from = from;
        this.to = to;
        fullPeriodList = items;
    }

    public final void update(final List<TreeItem> filteredItems, final List<Long> selectedInstances) {
        this.selectedInstances = selectedInstances;
        fillTreeInSummaryMode(filteredItems);
        highlightItems();
        filterByPersent(editor.getPercentFilter(), true);
    }

    private void highlightItems() {
        TreeItem flatListItem = null;
        TreeItem contextList = null;
        TreeItem treeList = null;
        for (int i = 0; i < tree.topLevelItemCount(); i++) {
            final TreeItem item = (TreeItem) tree.topLevelItem(i);
            if (item.getName().equals(STR_FLAT_LIST_NODE)) {
                flatListItem = item;
            } else if (item.getName().equals(STR_CONTEXT_TREE_NODE)) {
                contextList = item;
            } else if (item.getName().equals(STR_TREE_NODE)) {
                treeList = item;
            }
        }
        highlightItems(flatListItem, contextList, treeList);
    }

    private void fillTreeInSummaryMode(final List<TreeItem> items) {
        buildTree(items);
        final List<TreeItem> res = buildSummaryTree(items);
        tree.clear();
        if (res != null) {
            for (int i = 0; i < res.size(); i++) {
                final TreeItem item = res.get(i);
                tree.addTopLevelItem(item);
            }
            //tree.sortByColumn(0, SortOrder.AscendingOrder);
            if (tree.topLevelItemCount() > 0) {
                tree.setCurrentItem(tree.topLevelItem(0));
            }
            resizeColumns();
        }
    }

    private List<TreeItem> buildSummaryTree(final List<TreeItem> items) {
        List<TreeItem> res = null;
        final TaskWaiter waiter = new TaskWaiter(editor.getEnvironment(),this.getTree());
        waiter.setMessage(Application.translate("ProfilerDialog", "Build Summary Tree..."));
        final Callable<List<TreeItem>> task = new SummaryModeBuilder(items, editor.getTimeSectionEnum(), itemCalc);
        try {
            res = waiter.runAndWait(task);
        } catch (InterruptedException ex) {
        } catch (ExecutionException ex) {
            Logger.getLogger(SummaryTree.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            waiter.close();
        }
        return res;
    }

    public Timestamp getFrom() {
        return from;
    }

    public Timestamp getTo() {
        return to;
    }

    public String toHtml(final boolean isTreeMode, final boolean isFlatListMode, final boolean isContextTreeMode, final List<Integer> columns) {
        //int visibleColumnCount = 0;
        String res = "<table cellspacing=\"0\"><thead><tr>";
        
        for (int i = 0; i <columns.size(); i++) {
            final int columnIndex=columns.get(i);
            res += "<th>" + tree.headerItem().text(columnIndex) + "</th>";
            //visibleColumnCount++;
        }
        res += "</tr></thead><tr>";
        for (int i = 0; i < tree.topLevelItemCount(); i++) {
            final TreeItem item = (TreeItem) tree.topLevelItem(i);
            if (item.getName().equals(STR_TREE_NODE) && isTreeMode
                    || item.getName().equals(STR_FLAT_LIST_NODE) && isFlatListMode
                    || item.getName().equals(STR_CONTEXT_TREE_NODE) && isContextTreeMode) {
                //res += "<td>";// colspan=\"" + visibleColumnCount + "\"
                //res += item.text(0);
                //res += "</td>";
                res += item.itemToHtml( strTab, columns);
            }
        }
        res += "<tr></table>";
        return res;
    }
}
