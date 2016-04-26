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

package org.radixware.kernel.designer.dds.editors.table.widgets;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangedEvent;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangesListener;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsExtendableDefinitions;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EOrder;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;


public class TableColumnsList extends JList {

    public interface ChooseColumnActionListener {

        public void chooseColumnActionPerformed();
    }

    private class TableColumnsListModel extends AbstractListModel implements ContainerChangesListener {

        private final DdsExtendableDefinitions<DdsColumnDef> columns;
        private final ArrayList<DdsColumnDef> availableColumns = new ArrayList<DdsColumnDef>();
        private DdsIndexDef index = null;

        public TableColumnsListModel(DdsExtendableDefinitions<DdsColumnDef> columns) {
            this.columns = columns;
            columns.getLocal().getContainerChangesSupport().addEventListener(this);
        }

        private void update() {
            availableColumns.clear();
            HashSet<Id> usedColsIds = new HashSet<Id>(index != null ? index.getColumnsInfo().size() : 0);
            if (index != null) {
                for (DdsIndexDef.ColumnInfo col : index.getColumnsInfo()) {
                    usedColsIds.add(col.getColumnId());
                }
            }
            for (DdsColumnDef col : columns.get(EScope.ALL)) {
                if (!usedColsIds.contains(col.getId())) {
                    availableColumns.add(col);
                }
            }
            fireContentsChanged(this, 0, getSize() - 1);
        }

        public void setIndex(DdsIndexDef index) {
            assert index != null;
            if (this.index != null) {
                this.index.getColumnsInfo().getContainerChangesSupport().removeEventListener(this);
            }
            this.index = index;
            index.getColumnsInfo().getContainerChangesSupport().addEventListener(this);
            update();
        }

        public void removeColumns(List<DdsColumnDef> cols) {
            if (Utils.equals(index.getOwnerTable().getPrimaryKey(), index)) {
                for (DdsColumnDef col : cols) {
                    col.setNotNull(true);
                }
            }
            for (DdsColumnDef col : cols) {
                index.getColumnsInfo().add(col, EOrder.ASC);
            }
        }

        @Override
        public int getSize() {
            return availableColumns.size();
        }

        @Override
        public Object getElementAt(int index) {
            return availableColumns.get(index);
        }

        @Override
        public void onEvent(ContainerChangedEvent e) {
            update();
        }
    }

    private class TableColumnsListCellRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JComponent pattern = (JComponent) super.getListCellRendererComponent(list, "NULL", index, isSelected, cellHasFocus);
            if (value == null) {
                return pattern;
            }
            DdsColumnDef column = (DdsColumnDef) value;
            JLabel label = new JLabel(column.getName());
            label.setOpaque(true);
            label.setIcon(RadixObjectIcon.getForValType(column.getValType()).getIcon());
            label.setBackground(pattern.getBackground());
            label.setForeground(pattern.getForeground());
            if (cellHasFocus) {
                label.setBorder(pattern.getBorder());
            }
            return label;
        }
    }
    private final TableColumnsListModel model;
    private final ArrayList<ChooseColumnActionListener> listeners = new ArrayList<ChooseColumnActionListener>();

    public TableColumnsList(DdsTableDef table) {
        super();
        this.setModel(model = new TableColumnsListModel(table.getColumns()));
        this.setCellRenderer(new TableColumnsListCellRenderer());
        this.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
                    for (ChooseColumnActionListener listener : listeners) {
                        listener.chooseColumnActionPerformed();
                    }
                }
            }
        });
    }

    public void addChooseColumnActionListener(ChooseColumnActionListener listener) {
        listeners.add(listener);
    }

    public void removeChooseColumnActionListener(ChooseColumnActionListener listener) {
        listeners.remove(listener);
    }

    public void setIndex(DdsIndexDef index) {
        model.setIndex(index);
        this.clearSelection();
    }

    public void takeSelectedColumns() {
        final int[] idxs = this.getSelectedIndices();
        if (idxs.length == 0) {
            return;
        }
        ArrayList<DdsColumnDef> cols = new ArrayList<DdsColumnDef>(idxs.length);
        for (int idx : idxs) {
            cols.add((DdsColumnDef) model.getElementAt(idx));
        }
        model.removeColumns(cols);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (idxs.length == 1) {
                    if (idxs[0] < model.getSize()) {
                        TableColumnsList.this.setSelectedIndex(idxs[0]);
                    } else if (idxs[0] > 0) {
                        TableColumnsList.this.setSelectedIndex(idxs[0] - 1);
                    } else {
                        TableColumnsList.this.clearSelection();
                    }
                } else {
                    if (model.getSize() > 0) {
                        TableColumnsList.this.setSelectedIndex(0);
                    } else {
                        TableColumnsList.this.clearSelection();
                    }
                }
            }
        });
    }
}
