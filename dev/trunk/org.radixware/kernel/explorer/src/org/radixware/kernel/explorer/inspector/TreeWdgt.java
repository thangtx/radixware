/*
 * Copyright (coffee) 2008-2016, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.explorer.inspector;

import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QAbstractItemView;
import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QHeaderView;
import com.trolltech.qt.gui.QTreeWidget;
import com.trolltech.qt.gui.QTreeWidgetItem;
import com.trolltech.qt.gui.QWidget;
import java.util.Stack;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.explorer.env.ImageManager;

class TreeWdgt<T> extends QTreeWidget {

    final static QBrush GRAY_BRUSH = new QBrush(QColor.gray);
    final static QBrush BLACK_BRUSH = new QBrush(QColor.black);
    IClientEnvironment environment;

    TreeWdgt(QWidget parent, IClientEnvironment environment) {
        super(parent);
        this.environment = environment;
        this.itemExpanded.connect(this, "treeItemExpandedSlot(QTreeWidgetItem)");
        this.itemCollapsed.connect(this, "treeItemCollapsedSlot(QTreeWidgetItem)");
        this.setAutoScroll(false);
        this.itemSelectionChanged.connect(this, "itemSelectionChangedSlot()");
    }

    public WidgetInfo<T> build(final WidgetInfo<T> wdgtInfo) {
        WidgetInfo<T> tempWidgetInfo;
        WidgetInfo<T> selectedWidgetInfo = null;
        Stack<WidgetInfo<T>> treeStack = new Stack<>();
        QTreeWidgetItem childTreeWidgetItem;
        this.setColumnCount(1);
        this.setHeaderHidden(true);
        this.header().setHorizontalScrollMode(QAbstractItemView.ScrollMode.ScrollPerPixel);
        this.header().setResizeMode(0, QHeaderView.ResizeMode.ResizeToContents);
        this.header().setStretchLastSection(false);

        for (WidgetInfo<T> topWdgtInfo = wdgtInfo; topWdgtInfo != null; topWdgtInfo = topWdgtInfo.getParentWidgetInfo()) {
            treeStack.push(topWdgtInfo);
        }

        tempWidgetInfo = treeStack.pop();
        for (WidgetInfo<T> topLvlWdgtInfo : wdgtInfo.getWidgetInspector().getTopLevelWidgets()) {
            if (!topLvlWdgtInfo.equals(tempWidgetInfo)) {
                QTreeWidgetItem treeWidgetItem = createQTreeWidgetItem(topLvlWdgtInfo, null);
                this.addTopLevelItem(treeWidgetItem);
            }
        }

        QTreeWidgetItem topTreeWdgtItem = createQTreeWidgetItem(tempWidgetInfo, null);
        this.addTopLevelItem(topTreeWdgtItem);
        if (tempWidgetInfo.equals(wdgtInfo)) {
            this.setCurrentItem(topTreeWdgtItem);
            selectedWidgetInfo = tempWidgetInfo;
        }

        while (!treeStack.isEmpty()) {
            for (WidgetInfo widgetInfo : tempWidgetInfo.getChildrenInfo()) {
                if (!(widgetInfo.equals(treeStack.peek()))) {
                    childTreeWidgetItem = createQTreeWidgetItem(widgetInfo, topTreeWdgtItem);
                    this.setCurrentItem(childTreeWidgetItem.parent());
                    this.expandItem(childTreeWidgetItem.parent());
                    if (tempWidgetInfo.equals(wdgtInfo)) {
                        this.setCurrentItem(childTreeWidgetItem);
                        selectedWidgetInfo = tempWidgetInfo;
                    }
                }
            }

            tempWidgetInfo = treeStack.pop();
            childTreeWidgetItem = createQTreeWidgetItem(tempWidgetInfo, topTreeWdgtItem);
            topTreeWdgtItem = childTreeWidgetItem;
            if (tempWidgetInfo.equals(wdgtInfo)) {
                this.setCurrentItem(topTreeWdgtItem);
                selectedWidgetInfo = tempWidgetInfo;
            }
        }
        this.setItemsExpandable(true);
        return selectedWidgetInfo;
    }

    private QTreeWidgetItem createQTreeWidgetItem(final WidgetInfo wdgtInfo, QTreeWidgetItem parent) {
        QTreeWidgetItem qTreeWdgtItem = (parent == null) ? new QTreeWidgetItem() : new QTreeWidgetItem(parent);
        qTreeWdgtItem.setText(0, wdgtInfo.getDescription());
        qTreeWdgtItem.setData(0, Qt.ItemDataRole.UserRole, wdgtInfo);
        qTreeWdgtItem.setData(0, Qt.ItemDataRole.DecorationRole, ImageManager.getQIcon(wdgtInfo.getIcon(wdgtInfo.getWidget().getClass(), environment)));
        if (!wdgtInfo.getChildrenInfo().isEmpty()) {
            qTreeWdgtItem.setChildIndicatorPolicy(QTreeWidgetItem.ChildIndicatorPolicy.ShowIndicator);
        }
        if (isDeleted(qTreeWdgtItem)) {
            QFont qFont = qTreeWdgtItem.font(0);
            qFont.setStrikeOut(true);
            qTreeWdgtItem.setFont(0, qFont);
        } else if (isVisible(qTreeWdgtItem)) {
            qTreeWdgtItem.setForeground(0, new QBrush(QColor.gray));
        }
        return qTreeWdgtItem;
    }

    @SuppressWarnings({"unused", "unchecked"})
    private void treeItemExpandedSlot(QTreeWidgetItem wdgtItem) {
        if (!isDeleted(wdgtItem)) {
            WidgetInfo<T> wdgetInfo = (WidgetInfo<T>) wdgtItem.data(0, Qt.ItemDataRole.UserRole);
            if (wdgtItem.childCount() == 0) {
                for (WidgetInfo wdgtInfoItem : wdgetInfo.getChildrenInfo()) {
                    QTreeWidgetItem childTreeWidgetItem = createQTreeWidgetItem(wdgtInfoItem, null);
                    wdgtItem.addChild(childTreeWidgetItem);
                    if (isDeleted(wdgtItem)) {
                        childTreeWidgetItem.setForeground(0, new QBrush(QColor.gray));
                    }
                }
            }
            resizeColumnToContents(0);
        }
    }

    @SuppressWarnings("unused")
    private void treeItemCollapsedSlot(QTreeWidgetItem wdgtItem) {
        resizeColumnToContents(0);
    }

    public boolean isDeleted(QTreeWidgetItem qTreeWidgetItem) {
        WidgetInfo<T> wdgtInfo = getWdgetInfo(qTreeWidgetItem);
        return !wdgtInfo.getWidgetInspector().isExists(wdgtInfo.getWidget());
    }

    public boolean isVisible(QTreeWidgetItem qTreeWidgetItem) {
        WidgetInfo<T> wdgtInfo = getWdgetInfo(qTreeWidgetItem);
        return !wdgtInfo.getWidgetInspector().isVisible(wdgtInfo.getWidget());
    }

    public WidgetInfo<T> getCurrentWidgetInfo() {
        return getWdgetInfo(this.currentItem());
    }

    private void refreshTree(QTreeWidgetItem qTreeWidgetItem) {
        if (isDeleted(qTreeWidgetItem)) {
            QFont font = qTreeWidgetItem.font(0);
            font.setStrikeOut(true);
            qTreeWidgetItem.setFont(0, font);
        } else if (isVisible(qTreeWidgetItem)) {
            qTreeWidgetItem.setForeground(0, GRAY_BRUSH);
        } else {
            qTreeWidgetItem.setForeground(0, BLACK_BRUSH);
        }
        if (qTreeWidgetItem.childCount() != 0) {
            for (int i = 0; i < qTreeWidgetItem.childCount(); i++) {
                refreshTree(qTreeWidgetItem.child(i));
            }
        }
    }

    public void colorTree() {
        for (int i = 0; this.topLevelItem(i) != null; i++) {
            if (this.topLevelItem(i).childCount() != 0) {
                refreshTree(this.topLevelItem(i));
            } else if (isDeleted(this.topLevelItem(i))) {
                QFont font = this.topLevelItem(i).font(0);
                font.setStrikeOut(true);
                this.topLevelItem(i).setFont(0, font);
            } else if (isVisible(this.topLevelItem(i))) {
                this.topLevelItem(i).setForeground(0, new QBrush(QColor.gray));
            } else {
                this.topLevelItem(i).setForeground(0, new QBrush(QColor.black));
            }
        }
    }

    @SuppressWarnings("unused")
    private void itemSelectionChangedSlot() {
        scrollToItem(currentItem());
        QRect r = visualItemRect(currentItem());
        if (horizontalScrollBar().isEnabled()) {
            horizontalScrollBar().setValue(r.x());
        }
    }

    @SuppressWarnings("unchecked")
    private WidgetInfo<T> getWdgetInfo(QTreeWidgetItem qTreeWidgetItem) {
        return (WidgetInfo<T>) qTreeWidgetItem.data(0, Qt.ItemDataRole.UserRole);
    }
}
