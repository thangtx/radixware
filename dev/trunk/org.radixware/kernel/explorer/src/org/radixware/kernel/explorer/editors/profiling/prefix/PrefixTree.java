/*
 * Copyright (c) 2008-2018, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */

package org.radixware.kernel.explorer.editors.profiling.prefix;

import com.trolltech.qt.core.Qt.CheckState;
import com.trolltech.qt.gui.QTreeWidget;
import com.trolltech.qt.gui.QTreeWidgetItem;
import com.trolltech.qt.gui.QWidget;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.radixware.kernel.common.client.env.ReleaseRepository;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.explorer.env.Application;


public class PrefixTree extends QTreeWidget {
    // private RadEnumPresentationDef eventSourceDef;

    private final static int checkedColum = 0;
    private final static String ARTE_MODULE_NAME = "Profiler";
    private final static String EVENT_SOURCE_ENUM_NAME = "TimingSection";

    public PrefixTree(QWidget parent, String value) {
        super(parent);
        this.header().setVisible(false);
        this.itemChanged.connect(this, "onChangedTreeItem(QTreeWidgetItem,Integer)");
        List<String> vals = getVals(value);
        List<QTreeWidgetItem> treeItems = buildTree(vals);
        initItemsState(treeItems, vals);
        this.addTopLevelItems(treeItems);
    }

    private void initItemsState(List<QTreeWidgetItem> treeItems, List<String> vals) {
        for (QTreeWidgetItem item : treeItems) {
            PrefixTreeItem prefixItem = (PrefixTreeItem) item;
            setItemState(prefixItem, vals);
        }
    }

    private void setItemState(PrefixTreeItem treeItem, List<String> vals) {
        if (vals.contains(treeItem.getValue())) {
            treeItem.changeState(true);
        } else {
            setChildItemsState(treeItem, vals);
        }
    }

    private void setChildItemsState(PrefixTreeItem treeItems, List<String> vals) {
        for (int i = 0; i < treeItems.childCount(); i++) {
            PrefixTreeItem childItem = (PrefixTreeItem) treeItems.child(i);
            setItemState(childItem, vals);
        }
    }

    private List<String> getVals(String value) {
        List<String> res = new ArrayList<>();
        if ((value != null) && (!value.isEmpty())) {
            String[] vals = value.split(";");
            res = Arrays.asList(vals);
        }
        return res;
    }

    @SuppressWarnings("unused")
    private void onChangedTreeItem(QTreeWidgetItem item, Integer column) {
        if (checkedColum == column) {
            boolean isChecked = item.checkState(checkedColum) == CheckState.Checked;
            PrefixTreeItem prefixItem = (PrefixTreeItem) item;
            prefixItem.changeState(isChecked);
        }
    }

    private List<QTreeWidgetItem> buildTree(List<String> vals) {        
        final ReleaseRepository.DefinitionInfo eventSourceDefinition =
                Application.getInstance().getDefManager().getRepository().findDefinitionByName(ARTE_MODULE_NAME, EDefType.ENUMERATION, EVENT_SOURCE_ENUM_NAME);
        if (eventSourceDefinition == null) {
            throw new RadixError("Can`t find  event source enumeration");
        }
        List<QTreeWidgetItem> treeItems = new ArrayList<>();
        RadEnumPresentationDef eventSourceDef = Application.getInstance().getDefManager().getEnumPresentationDef(eventSourceDefinition.id);
        final RadEnumPresentationDef.Items eventSourceItems = eventSourceDef.getItems();
        eventSourceItems.sort(RadEnumPresentationDef.ItemsOrder.BY_ORDER);
        for (RadEnumPresentationDef.Item item : eventSourceItems) {
            if (!item.isDeprecated()) {
                PrefixTreeItem treeItem = new PrefixTreeItem(item);
                treeItems.add(treeItem);
            }
        }
        buildTree(treeItems, vals);
        return treeItems;
    }

    private void buildTree(List<QTreeWidgetItem> treeItems, List<String> vals) {
        for (int i = 0; i < treeItems.size(); i++) {
            PrefixTreeItem item = (PrefixTreeItem) treeItems.get(i);
            String s = item.getValue();
            for (int k = 0; k < treeItems.size(); k++) {
                PrefixTreeItem it = (PrefixTreeItem) treeItems.get(k);
                if (!it.equals(item)) {
                    String name = it.getValue();
                    if ((name.indexOf(s) == 0) && (!name.equals(s))) {
                        treeItems.remove(it);
                        item.addChild(it);
                        if (i >= k) {
                            i--;
                        }
                        k--;
                    }
                }
            }
            List<QTreeWidgetItem> childItems = item.takeChildren();
            buildTree(childItems, vals);
            item.addChildren(childItems);
        }
    }

    public List<PrefixTreeItem> getSelectedItems() {
        List<PrefixTreeItem> selectedItems = new ArrayList<>();
        for (int i = 0; i < this.topLevelItemCount(); i++) {
            PrefixTreeItem item = (PrefixTreeItem) this.topLevelItem(i);
            if (item.checkState(checkedColum) == CheckState.Checked) {
                selectedItems.add(item);
            } else {
                getSelectedItems(item, selectedItems);
            }
        }
        return selectedItems;
    }

    public List<PrefixTreeItem> getSelectedItems(PrefixTreeItem parent, List<PrefixTreeItem> selectedItems) {
        for (int i = 0; i < parent.childCount(); i++) {
            PrefixTreeItem item = (PrefixTreeItem) parent.child(i);
            if (item.checkState(checkedColum) == CheckState.Checked) {
                selectedItems.add(item);
            } else {
                getSelectedItems(item, selectedItems);
            }
        }
        return selectedItems;
    }

    public String getSelectedPrefixes() {
        //String res = "";
        StringBuilder sb = new StringBuilder();
        List<PrefixTreeItem> selectedItems = getSelectedItems();
        for (int i = 0, size = selectedItems.size(); i < size; i++) {
            PrefixTreeItem item = selectedItems.get(i);
            sb.append(item.getValue());
            //res += item.getValue();
            if (i < size - 1) {
                //res += ";";
                sb.append(";");
            }
        }
        return sb.toString();
    }
}
