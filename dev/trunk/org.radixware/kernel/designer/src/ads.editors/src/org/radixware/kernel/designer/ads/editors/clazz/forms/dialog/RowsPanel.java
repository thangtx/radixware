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
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.swing.AbstractListModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableCellEditor;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangedEvent;
import org.radixware.kernel.common.defs.RadixObjects.EChangeType;
import org.radixware.kernel.common.defs.ads.ui.*;
import org.radixware.kernel.common.defs.ads.ui.AdsItemWidgetDef.Row;
import org.radixware.kernel.common.defs.ads.ui.AdsItemWidgetDef.Rows;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Item;
import org.radixware.kernel.designer.ads.editors.clazz.forms.props.UIPropertySupport;

public class RowsPanel extends SetPanel<Row, AdsItemWidgetDef> {

    public RowsPanel(AdsAbstractUIDef uiDef, AdsItemWidgetDef widget) {
        super(uiDef, widget);

        init();
    }

    @Override
    protected Row createItem() {
        return new Row();
    }

//    public void cursorChanged(int idx) {
//        update();
//    }
    public Rows getRows() {
        return ((ListModel) getListModel()).getRows();
    }

    @Override
    protected IListModel<Row> createListModel() {
        return new ListModel();
    }

    @Override
    protected ITableModel<Row> createTableModel() {
        return new TableModel(uiDef);
    }

    @Override
    public String getTitle() {
        return NbBundle.getMessage(getClass(), "CTL_RowsPanel");
    }

    @Override
    protected TableCellEditor getCellEditor() {
        return new org.radixware.kernel.designer.ads.editors.clazz.forms.dialog.PropertyCellEditor() {

            @Override
            protected UIPropertySupport getPropertySupport(AdsUIProperty prop) {
                final Row row = getTableModel().getItem();
                return new UIPropertySupport(prop, uiDef, row) {

                    @Override
                    public void setValue(Object val) {
                        try {
                            super.setValue(val);
                            getListModel().fireContentChanged(row);
                            ((ListModel) getListModel()).getRows().getContainerChangesSupport().fireEvent(new ContainerChangedEvent(row, EChangeType.MODIFY));
                        } catch (Throwable ex) {
                            java.util.logging.Logger.getLogger(UIPropertySupport.class.getName()).log(Level.WARNING, "Can not change property", ex);
                        }
                    }
                };
            }
        };
    }

    public class ListRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) {
            Row row = (Row) value;

            AdsUIProperty.LocalizedStringRefProperty text = (AdsUIProperty.LocalizedStringRefProperty) AdsUIUtil.getUiProperty(row, "text");
            AdsUIProperty.ImageProperty icon = (AdsUIProperty.ImageProperty) AdsUIUtil.getUiProperty(row, "icon");
            AdsUIProperty.BrushProperty background = (AdsUIProperty.BrushProperty) AdsUIUtil.getUiProperty(row, "background");
            AdsUIProperty.BrushProperty foreground = (AdsUIProperty.BrushProperty) AdsUIUtil.getUiProperty(row, "foreground");

            String s = Item.getTextById(row, text.getStringId());
            JLabel label = (JLabel) super.getListCellRendererComponent(list, s.isEmpty() ? " " : s, index, isSelected, hasFocus);
            RadixIcon radixIcon = Item.getIconById(row, icon.getImageId());
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

    private class ListModel extends AbstractListModel<Object> implements IListModel<Row> {

        private final Rows rows = widget.getRows();

        ListModel() {
        }

        @Override
        public Object getElementAt(int i) {
            return rows.get(i);
        }

        @Override
        public int getSize() {
            return rows.size();
        }

        public Rows getRows() {
            return rows;
        }

        @Override
        public void fireContentChanged(Row row) {
            super.fireContentsChanged(this, rows.indexOf(row), rows.indexOf(row));
        }

        public void fireContentChanged(int idx) {
            super.fireContentsChanged(this, idx, idx);
        }

        void edited(Row row) {
            int idx = rows.indexOf(row);
            if (idx >= 0 && idx < getSize()) {
                fireContentsChanged(this, idx, idx);
            }
        }

        @Override
        public void add(int idx, Row row) {
            if (idx < 0 || idx > getSize()) {
                idx = 0;
            }

            AdsUIProperty.LocalizedStringRefProperty text = new AdsUIProperty.LocalizedStringRefProperty("text");
            row.getProperties().add(text);

            rows.add(idx, row);

            syncPropRowCount();

            fireIntervalAdded(this, idx, idx);
        }

        @Override
        public void del(int idx) {
            if (idx >= 0 && idx < getSize()) {
                rows.remove(idx);

                syncPropRowCount();

                fireIntervalRemoved(this, idx, idx);
            }
        }

        @Override
        public void up(int idx) {
            if (idx > 0 && idx < getSize()) {
                rows.moveUp(idx);
                fireContentsChanged(this, idx - 1, idx);
            }
        }

        @Override
        public void down(int idx) {
            if (idx >= 0 && idx < getSize() - 1) {
                rows.moveDown(idx);
                fireContentsChanged(this, idx, idx + 1);
            }
        }

        void syncPropRowCount() {
            AdsUIProperty.IntProperty rowCount = (AdsUIProperty.IntProperty) widget.getProperties().getByName("rowCount");
            if (rowCount != null) {
                rowCount.value = rows.size();
                AdsUIUtil.fire(widget, rowCount, RowsPanel.this);
            }
        }

        @Override
        public void fireChanged(int idx) {
        }

        @Override
        public Row getItemAt(int i) {
            return rows.get(i);
        }
    }

    private class TableModel extends DefaultTableModel<Row> {

        public TableModel(AdsAbstractUIDef uiDef) {
            super(uiDef);
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting()) {
                return;
            }
            int idx = getItemList().getSelectedIndex();

            item = idx >= 0 ? (Row) getListModel().getElementAt(idx) : null;
            if (item != null) {
                rows = new ArrayList<>(AdsMetaInfo.getProps(AdsMetaInfo.WIDGET_ITEM_CLASS, widget));
                UiProperties props = AdsUIUtil.getUiProperties(item);

                for (int i = 0; i < rows.size(); i++) {
                    if (rows.get(i).getName().equals("resizeMode")) {
                        rows.remove(i);
                        break;
                    }
                }

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
