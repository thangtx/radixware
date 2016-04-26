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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;


public class SummaryModeBuilder implements Callable<List<TreeItem>> {

    private final List<TreeItem> items;
    private final RadEnumPresentationDef timeSectionEnum;
    private final ItemTextCalculator itemCalc;

    SummaryModeBuilder(final List<TreeItem> items, final RadEnumPresentationDef timeSectionEnum, final ItemTextCalculator itemCalc) {
        this.items = items;
        this.itemCalc = itemCalc;
        this.timeSectionEnum = timeSectionEnum;
    }

    @Override
    public List<TreeItem> call() {
        final List<TreeItem> res = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {//цикл по периодам
            final TreeItem item = items.get(i);
            for (int k = 0; k < item.childCount(); k++) {
                TreeItem searchItem = (TreeItem) item.child(k);
                final boolean isContext = !searchItem.getName().equals(AbstractProfileTree.STR_TREE_NODE);
                for (int j = 0; j < items.size(); j++) {
                    final TreeItem it = items.get(j);
                    if ((!item.equals(it))) {
                        searchItem = findSumItem(it, searchItem, false, isContext);
                    }
                }
                res.add((TreeItem) searchItem.clone());
            }
        }
        final double sumDuration = getSummaryDuration(res);
        for (int i = 0; i < res.size(); i++) {
            final TreeItem item = res.get(i);
            item.setItemText();
            final boolean isFlatList = item.getName().equals(AbstractProfileTree.STR_FLAT_LIST_NODE);
            final boolean isContextTree = item.getName().equals(AbstractProfileTree.STR_CONTEXT_TREE_NODE);
            final boolean isTree = item.getName().equals(AbstractProfileTree.STR_TREE_NODE);
            if (isFlatList || isContextTree) {
                for (int k = 0; k < item.childCount(); k++) {
                    final TreeItem it = (TreeItem) item.child(k);
                    itemCalc.calcItemText(it, sumDuration, true, isFlatList ? null : timeSectionEnum);
                    if (isFlatList) {
                        it.setTitleWithContext(timeSectionEnum,itemCalc);
                    }
                    item.addChild(it);
                }
            } else if (isTree) {
                for (int k = 0; k < item.childCount(); k++) {
                    final TreeItem it = (TreeItem) item.child(k);
                    itemCalc.calcItemText(it, sumDuration/*it.getDuration()*/,true,timeSectionEnum);
                    item.addChild(it);
                }
            }

        }
        return res;
    }

    private double getSummaryDuration(final List<TreeItem> res) {
        double sumDuration = 0;
        for (int i = 0; i < res.size(); i++) {
            final TreeItem item = res.get(i);
            if (item.getName().equals(AbstractProfileTree.STR_FLAT_LIST_NODE)) {
                for (int k = 0; k < item.childCount(); k++) {
                    final TreeItem it = (TreeItem) item.child(k);
                    sumDuration += it.getPureDuration();
                }
            }
        }
        return sumDuration;
    }

    private TreeItem findSumItem(final TreeItem parent, final TreeItem searchItem, final boolean isflat, final boolean isContext) {
        for (int i = 0; i < parent.childCount(); i++) {
            final TreeItem item = (TreeItem) parent.child(i);
            if ((searchItem.isCanSumWith(item, isContext))) {
                searchItem.sumItemTextWith(item, !isflat, isContext);
                parent.removeChild(item);
                i--;
            } else if (isflat && searchItem.getName().startsWith(item.getName())) {
                findSumItem(item, searchItem, isflat, isContext);
            }
        }
        return searchItem;
    }
}
