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
package org.radixware.kernel.designer.ads.editors.clazz.forms.dialog;

import java.awt.Component;
import java.awt.Rectangle;
import java.util.ArrayList;
import javax.swing.AbstractListModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.ui.AdsItemWidgetDef.Items;
import org.radixware.kernel.common.defs.ads.ui.AdsItemWidgetDef.WidgetItem;
import org.radixware.kernel.common.defs.ads.ui.*;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Item;

public class ListPanel extends SetPanel<WidgetItem, AdsItemWidgetDef> {

    public ListPanel(AdsAbstractUIDef uiDef, AdsItemWidgetDef widget) {
        super(uiDef, widget);
    }

    @Override
    public void init() {
        super.init();

        getItemList().setCellRenderer(new ListRenderer());
    }

//    public void cursorChanged(int idx) {
//        update();
//    }
    @Override
    public String getTitle() {
        return NbBundle.getMessage(getClass(), "CTL_ListPanel");
    }

    @Override
    protected IListModel<WidgetItem> createListModel() {
        return new ListModel();
    }

    @Override
    protected ITableModel<WidgetItem> createTableModel() {
        return new TableModel(uiDef);
    }

    @Override
    protected WidgetItem createItem() {
        return new WidgetItem();
    }

    public class ListRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) {
            WidgetItem item = (WidgetItem) value;

            AdsUIProperty.LocalizedStringRefProperty text = (AdsUIProperty.LocalizedStringRefProperty) AdsUIUtil.getUiProperty(item, "text");
            AdsUIProperty.ImageProperty icon = (AdsUIProperty.ImageProperty) AdsUIUtil.getUiProperty(item, "icon");
            AdsUIProperty.BrushProperty background = (AdsUIProperty.BrushProperty) AdsUIUtil.getUiProperty(item, "background");
            AdsUIProperty.BrushProperty foreground = (AdsUIProperty.BrushProperty) AdsUIUtil.getUiProperty(item, "foreground");

            String s = Item.getTextById(item, text.getStringId());
            JLabel label = (JLabel) super.getListCellRendererComponent(list, s.isEmpty() ? " " : s, index, isSelected, hasFocus);

            Rectangle r = label.getBounds();
            r.height = 200;
            label.setBounds(r);

            RadixIcon radixIcon = Item.getIconById(item, icon.getImageId());
            if (radixIcon != null) {
                label.setIcon(radixIcon.getIcon());
            }

            if (!isSelected) {
                label.setBackground(background.color);
            }
            label.setForeground(foreground.color);

            return label;
        }
    }

    private class ListModel extends AbstractListModel<Object> implements IListModel<WidgetItem> {

        private final Items items = widget.getItems();

        ListModel() {
        }

        @Override
        public Object getElementAt(int i) {
            return items.get(i);
        }

        @Override
        public int getSize() {
            return items.size();
        }

        public Items getItems() {
            return items;
        }

        @Override
        public void fireContentChanged(WidgetItem item) {
            super.fireContentsChanged(this, items.indexOf(item), items.indexOf(item));
        }

        public void fireContentChanged(int idx) {
            super.fireContentsChanged(this, idx, idx);
        }

        void edited(WidgetItem item) {
            int idx = items.indexOf(item);
            if (idx >= 0 && idx < getSize()) {
                fireContentsChanged(this, idx, idx);
            }
        }

        @Override
        public void add(int idx, WidgetItem item) {
            if (idx < 0 || idx > getSize()) {
                idx = 0;
            }

            AdsUIProperty.LocalizedStringRefProperty text = new AdsUIProperty.LocalizedStringRefProperty("text");
            item.getProperties().add(text);

            items.add(idx, item);
            fireIntervalAdded(this, idx, idx);
        }

        @Override
        public void del(int idx) {
            if (idx >= 0 && idx < getSize() /*&& getSize() > 2*/) {
                items.remove(idx);
                fireIntervalRemoved(this, idx, idx);
            }
        }

        @Override
        public void up(int idx) {
            if (idx > 0 && idx < getSize()) {
                items.moveUp(idx);
                fireContentsChanged(this, idx - 1, idx);
            }
        }

        @Override
        public void down(int idx) {
            if (idx >= 0 && idx < getSize() - 1) {
                items.moveDown(idx);
                fireContentsChanged(this, idx, idx + 1);
            }
        }

        @Override
        public void fireChanged(int idx) {
        }

        @Override
        public WidgetItem getItemAt(int i) {
            return items.get(i);
        }
    }

    private class TableModel extends DefaultTableModel<WidgetItem> {

        public TableModel(AdsAbstractUIDef uiDef) {
            super(uiDef);
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting()) {
                return;
            }
            int idx = getItemList().getSelectedIndex();

            item = idx >= 0 ? (WidgetItem) getListModel().getElementAt(idx) : null;
            if (item != null) {
                rows = new ArrayList<>(AdsMetaInfo.getProps(AdsMetaInfo.WIDGET_ITEM_CLASS, widget));
                UiProperties props = AdsUIUtil.getUiProperties(item);
                for (int i = 0; i < rows.size(); i++) {
                    AdsUIProperty p = props.getByName(rows.get(i).getName());
                    if (p != null) {
                        rows.set(i, p);
                    } else {
                        rows.set(i, rows.get(i).duplicate());
                    }
                }
            }
            fireTableStructureChanged();
        }
    }
}
