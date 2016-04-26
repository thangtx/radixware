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

import com.trolltech.qt.gui.QTreeWidgetItem;
import java.util.List;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;

public class TreeModeBuilder implements Runnable {

    private final List<TreeItem> items;
    private final RadEnumPresentationDef timeSectionEnum;
    boolean isPercentMode;
    private final ItemTextCalculator itemCalc;

    TreeModeBuilder(final List<TreeItem> items, final RadEnumPresentationDef timeSectionEnum, final ItemTextCalculator itemCalc) {
        this.items = items;
        this.itemCalc = itemCalc;
        this.timeSectionEnum = timeSectionEnum;
    }

    @Override
    public void run() {
        for (int i = 0; i < items.size(); i++) {
            final TreeItem item = items.get(i);
            if (!item.isCalculated()) {
                final TreeItem contextItem = (TreeItem) item.clone();
                final List<QTreeWidgetItem> flatList = item.takeChildren();
                buildContextTree(contextItem, "");
                contextItem.calculatePureDuration();
                itemCalc.calcChildItemsText(contextItem, contextItem.getDuration(), false, timeSectionEnum);

                final TreeItem flatitem = new TreeItem(AbstractProfileTree.STR_FLAT_LIST_NODE, item.getInstanceId(), item.isPercentMode());
                flatitem.setItemText();
                copyItemText(contextItem, flatList);
                flatitem.addChildren(flatList);
                item.addChild(flatitem);

                final TreeItem treeItem = new TreeItem(AbstractProfileTree.STR_TREE_NODE, item.getInstanceId(), item.isPercentMode());
                for (int j = 0; j < flatList.size(); j++) {
                    final TreeItem flatItem = (TreeItem) flatList.get(j);
                    final TreeItem copyItem = (TreeItem) flatItem.clone();
                    treeItem.addChild(copyItem);
                }
                for (int j = 0; j < treeItem.childCount(); j++) {
                    final TreeItem childitem = (TreeItem) treeItem.child(j);
                    for (int k = j + 1; k < treeItem.childCount(); k++) {
                        final TreeItem child_it = (TreeItem) treeItem.child(k);
                        if (childitem.isCanSumWith(child_it, false)) {
                            childitem.sumItemTextWith(child_it, false, false);
                            treeItem.removeChild(child_it);
                            k--;
                        }
                    }
                }

                buildTree(treeItem);
                buildFullTree(treeItem, null);//tree with meaningless items
                treeItem.calculateDuration();
                itemCalc.calcChildItemsText(treeItem, treeItem.getDuration(), false/*,null*/, timeSectionEnum);

                final TreeItem treeitem = new TreeItem(AbstractProfileTree.STR_TREE_NODE, item.getInstanceId(), item.isPercentMode());
                treeitem.setItemText();
                treeitem.addChildren(treeItem.takeChildren());

                final TreeItem contextitem = new TreeItem(AbstractProfileTree.STR_CONTEXT_TREE_NODE, item.getInstanceId(), item.isPercentMode());
                contextitem.addChildren(contextItem.takeChildren());
                contextitem.calcDomains(timeSectionEnum);
                contextitem.setItemText();
                item.addChild(contextitem);
                item.addChild(treeitem);
                item.setIsCalced(true);
            }
        }
    }

    private void copyItemText(final TreeItem item, final List<QTreeWidgetItem> flatList) {
        for (int i = 0; i < item.childCount(); i++) {
            final TreeItem child = (TreeItem) item.child(i);
            final TreeItem flatItem = itemCalc.searchInFlatList(child, flatList);
            if (flatItem != null) {
                flatItem.copyItemTextFrom(child, timeSectionEnum, itemCalc);
            }
            copyItemText(child, flatList);
        }
    }

    private void buildTree(final TreeItem parent) {
        for (int i = 0; i < parent.childCount(); i++) {
            final TreeItem item = (TreeItem) parent.child(i);
            final String s = calcSpreadName(item.getName());
            for (int k = 0; k < parent.childCount(); k++) {
                final TreeItem it = (TreeItem) parent.child(k);
                if (!it.equals(item)) {
                    String sessionId = calcSpreadName(it.getName());
                    if (sessionId.indexOf(":") != -1) {
                        sessionId = sessionId.substring(0, sessionId.indexOf(":"));
                    }
                    if ((sessionId.indexOf(s + ".") == 0) && (!sessionId.equals(s))) {
                        parent.removeChild(it);
                        item.addChild(it);
                        if (i >= k) {
                            i--;
                        }
                        k--;
                    }
                }
            }
            buildTree(item);
        }
    }

    private String calcSpreadName(String name) {
        if (name.contains(":")) {
            return name.substring(0, name.indexOf(":"));
        }
        return name;
    }

    private void buildContextTree(final TreeItem parent, final String parentContext) {
        for (int i = 0; i < parent.childCount(); i++) {
            final TreeItem item = (TreeItem) parent.child(i);
            String s = item.getContext();
            if (s != null && (!s.equals(""))) {
                if ((parentContext != null) && (!parentContext.equals(""))) {
                    s = calcContext(item, parentContext);
                }
                final String[] prefixes = s.split(";");
                if ((!s.equals("")) && (prefixes.length > 0)) {
                    final String prefix = prefixes[0];
                    final TreeItem newParentItem = getParentContextItem(parent, prefix, parentContext);
                    if (newParentItem != null) {
                        parent.removeChild(item);
                        newParentItem.addChild(item);

                        for (int k = i; k < parent.childCount(); k++) {
                            final TreeItem it = (TreeItem) parent.child(k);
                            String it_context = it.getContext();
                            if ((parentContext != null) && (!parentContext.equals(""))) {
                                it_context = calcContext(it, parentContext);
                            }
                            final String[] itContextParts = it_context.split(";");
                            if (itContextParts.length > 0 && itContextParts[0].equals(prefix)) {
                                parent.removeChild(it);
                                newParentItem.addChild(it);
                                k--;
                            }
                        }
                        i--;
                        final String patentContext = ((newParentItem.getContext() != null) && (!"".equals(newParentItem.getContext())))
                                ? newParentItem.getContext() + ";" + newParentItem.getName() : newParentItem.getName();
                        buildContextTree(newParentItem, patentContext);
                    }
                } else {
                    buildContextTree(item, item.getContext());
                }
            }
        }
    }

    private String calcContext(final TreeItem item, final String parentContext) {
        if (item.getContext().equals(parentContext)) {
            return "";
        } else {
            return item.getContext().substring(parentContext.length() + 1, item.getContext().length());
        }
    }

    private void buildFullTree(final TreeItem parent, final String parentName) {
        for (int i = 0; i < parent.childCount(); i++) {
            final TreeItem item = (TreeItem) parent.child(i);
            if ((parentName == null) || (item.getName().indexOf(parentName) == 0 && item.getName().length() > parentName.length())) {
                String s = item.getName();
                if (parentName != null) {
                    s = item.getName().substring(parentName.length() + 1, item.getName().length());
                }
                s = calcSpreadName(s);
                final String[] prefixes = s.split("\\.");
                if (prefixes.length > 1) {
                    final String prefix = parentName == null ? prefixes[0] : parentName + "." + prefixes[0];
                    final TreeItem newParentItem = getParentItem(parent, prefix, item.getContext());
                    parent.removeChild(item);
                    newParentItem.addChild(item);
                    parent.addChild(newParentItem);

                    for (int k = 0; k < parent.childCount(); k++) {
                        final TreeItem it = (TreeItem) parent.child(k);
                        if (it.getName().indexOf(prefix) == 0 && it.getName().length() > prefix.length() /*&& (it.getContext().equals(newParentItem.getContext()))*/) {
                            parent.removeChild(it);
                            newParentItem.addChild(it);
                            if (i >= k) {
                                i--;
                            }
                            k--;
                        }
                    }
                    buildFullTree(newParentItem, newParentItem.getName());
                } else {
                    buildFullTree(item, item.getName());
                }
            }
        }

        for (int i = 0; i < parent.childCount(); i++) {//убираем лишние префиксы
            final TreeItem item = (TreeItem) parent.child(i);
            item.calcTitleWithoutPrefixes();
        }
    }

    private TreeItem getParentContextItem(final TreeItem parent, final String prefix, final String parentContext) {
        TreeItem parentItem = null;
        for (int k = 0; k < parent.childCount(); k++) {
            final TreeItem it = (TreeItem) parent.child(k);
            if ((it.getName().equals(prefix)) && ((parentContext == null && it.getContext() == null) || (it.getContext().equals(parentContext)))) {
                parentItem = it;
                break;
            }
        }
        return parentItem;
    }

    private TreeItem getParentItem(final TreeItem parent, final String prefix, final String context) {
        TreeItem parentItem = null;
        for (int k = 0; k < parent.childCount(); k++) {
            final TreeItem it = (TreeItem) parent.child(k);
            if (it.getName().equals(prefix)) {
                parentItem = it;
            }
        }
        if (parentItem == null) {
            final String title = itemCalc.getItemTitle(prefix, timeSectionEnum);
            parentItem = new TreeItem(prefix, null, context, 0l, 0l, 0l, 0l, parent.getInstanceId(), 0l, null, null, null, null, null, title, parent.isPercentMode());
        }
        return parentItem;
    }
}
