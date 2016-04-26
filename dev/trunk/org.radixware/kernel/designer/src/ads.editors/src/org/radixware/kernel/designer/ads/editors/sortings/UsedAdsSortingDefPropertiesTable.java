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

package org.radixware.kernel.designer.ads.editors.sortings;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import org.openide.util.ChangeSupport;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSortingDef;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.chooseobject.AbstractItemCellRenderer;
import org.radixware.kernel.designer.common.dialogs.components.IRadixObjectChooserLeftComponent;
import org.radixware.kernel.designer.common.dialogs.components.RadixObjectChooserPanel;
import org.radixware.kernel.designer.common.general.displaying.HtmlNameSupport;
import org.radixware.kernel.designer.common.general.displaying.HtmlNameSupportsManager;


class UsedAdsSortingDefPropertiesTable implements IRadixObjectChooserLeftComponent {

    private JTable table;
    private JScrollPane component;
    private AdsSortingDef sorting;

    public UsedAdsSortingDefPropertiesTable(AdsSortingDef sorting) {
        this.sorting = sorting;
        table = new JTable();

        updateContent();

        final TableColumn checkboxesColumn = table.getColumnModel().getColumn(PropertiesTableModel.SECOND_COLUMN_INDEX);
        checkboxesColumn.setMaxWidth(45);
        checkboxesColumn.setPreferredWidth(45);

        table.setRowHeight(24);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateColumnsFromModel(false);
        table.setRowSelectionAllowed(true);

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                changeSupport.fireChange();
            }
        });
        table.setDefaultRenderer(table.getColumnClass(PropertiesTableModel.FIRST_COLUMN_INDEX), new AbstractItemCellRenderer(table) {

            @Override
            public String getObjectName(Object object, int row, int column) {
                RadixObject rdxObject = getRadixObjectByRow(row);
                if (rdxObject != null) {
                    final HtmlNameSupport htmlNameSupport = HtmlNameSupportsManager.newInstance(rdxObject);
                    final String name = "<html>" + htmlNameSupport.getHtmlName();
                    return name;
                }
                return String.valueOf(object);
            }

            @Override
            public RadixIcon getObjectIcon(Object object, int row, int column) {
                RadixObject rdxObject = getRadixObjectByRow(row);
                if (rdxObject != null) {
                    return rdxObject.getIcon();
                }
                return RadixObjectIcon.UNKNOWN;
            }

            @Override
            public String getObjectLocation(Object object, int row, int column) {
                return null;
            }

            @Override
            public RadixIcon getObjectLocationIcon(Object object, int row, int column) {
                return null;
            }

            private RadixObject getRadixObjectByRow(int row) {
                return ((PropertiesTableModel) table.getModel()).getOrderByRow(row).findProperty();
            }
        });

        MouseAdapter mouseAdapter = new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (e.getClickCount() == 2) {
                    final Point origin = e.getPoint();
                    int row = table.rowAtPoint(origin);
                    int column = table.columnAtPoint(origin);

                    if (row != -1 && column != -1) {
                        getDoubleClickSupport().fireEvent(new RadixObjectChooserPanel.DoubleClickChooserEvent(row));
                    }
                }
            }
        };
        table.addMouseListener(mouseAdapter);


        component = new JScrollPane(table);
    }

    @Override
    public boolean isOrderDependant() {
        return true;
    }

    @Override
    public void moveDown() {
        final int currentSelectedRowIndex = table.getSelectedRow();
        final PropertiesTableModel tableModel = (PropertiesTableModel) table.getModel();
        tableModel.moveDownItem(currentSelectedRowIndex);
        table.setRowSelectionInterval(currentSelectedRowIndex + 1, currentSelectedRowIndex + 1);
    }

    @Override
    public void moveUp() {
        final int currentSelectedRowIndex = table.getSelectedRow();
        ((PropertiesTableModel) table.getModel()).moveUpItem(currentSelectedRowIndex);
        table.setRowSelectionInterval(currentSelectedRowIndex - 1, currentSelectedRowIndex - 1);
    }
    private ChangeSupport changeSupport = new ChangeSupport(this);

    @Override
    public void removeSelectionEventListener(ChangeListener listener) {
        changeSupport.removeChangeListener(listener);
    }

    @Override
    public void addSelectionEventListener(ChangeListener listener) {
        changeSupport.addChangeListener(listener);
    }

    @Override
    public Integer getSelectedIndex() {
        return table.getSelectedRow();
    }

    @Override
    public void setSelectedItem(Integer index) {
        if (index > -1 && index < table.getRowCount()) {
            table.setRowSelectionInterval(index, index);
        }
    }

    @Override
    public void setSelectedItems(Object[] items) {
        final PropertiesTableModel tableModel = (PropertiesTableModel) table.getModel();
        assert (items.length == 1);
        final int modelRowIndex = tableModel.getRowIndexByProperty(items[0]);
        assert (modelRowIndex != -1);
        final int tableRowIndex = table.convertRowIndexToView(modelRowIndex);
        table.setRowSelectionInterval(tableRowIndex, tableRowIndex);
    }

    @Override
    public Object[] getSelectedItems() {
        int row = getSelectedIndex();
        Id prop = ((PropertiesTableModel) table.getModel()).getOrderByRow(row).getPropertyId();
        if (prop != null) {
            return new Object[]{prop};
        }
        return new Object[]{};
    }

    @Override
    public boolean hasSelection() {
        return table.getSelectedRow() != -1;
    }

    @Override
    public void removeAll(Object[] objects) {
        PropertiesTableModel tableModel = (PropertiesTableModel) table.getModel();
        for (Object xRadixObject : objects) {
            tableModel.removeItem(xRadixObject);
        }
    }

    @Override
    public void addAllItems(Object[] objects) {
        PropertiesTableModel tableModel = (PropertiesTableModel) table.getModel();
        int row = getSelectedIndex();
        if (row > -1 && row < table.getModel().getRowCount()) {
            for (Object xRadixObject : objects) {
                row++;
                tableModel.addItem(row, xRadixObject);
            }
        } else {
            for (Object xRadixObject : objects) {
                tableModel.addItem(xRadixObject);
            }
        }
    }

    @Override
    public Integer getItemCount() {
        return table.getRowCount();
    }

    @Override
    public JComponent getLabelComponent() {
        return new javax.swing.JLabel("Used Properties");
    }

    @Override
    public JComponent getVisualComponent() {
        return component;
    }

    @Override
    public boolean isReadonly() {
        return !table.isEnabled();
    }

    @Override
    public void setReadonly(boolean readonly) {
        table.setEnabled(!readonly);
    }

    @Override
    public final void updateContent() {
        table.setModel(new PropertiesTableModel(sorting));
    }

    @Override
    public RadixObjectChooserPanel.DoublieClickChooserSupport getDoubleClickSupport() {
        if (doubleClickSupport == null) {
            doubleClickSupport = new RadixObjectChooserPanel.DoublieClickChooserSupport();
        }
        return doubleClickSupport;
    }
    private RadixObjectChooserPanel.DoublieClickChooserSupport doubleClickSupport;
}
