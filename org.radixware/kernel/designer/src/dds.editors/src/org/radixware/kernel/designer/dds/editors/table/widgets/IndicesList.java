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

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractListModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangedEvent;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangesListener;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.kernel.common.utils.Utils;


public class IndicesList extends JList {

    public interface EditingIndexChangeListener {

        public void editingIndexChanged(DdsIndexDef editingIndex, boolean inherited);
    }

    private class IndexModel {

        DdsIndexDef index;
        boolean inherited;

        IndexModel(DdsIndexDef index, boolean inherited) {
            this.index = index;
            this.inherited = inherited;
        }
    }

    private class IndicesListModel extends AbstractListModel implements ContainerChangesListener {

        private final DdsTableDef table;
        private final ArrayList<IndexModel> indices = new ArrayList<IndexModel>();

        public IndicesListModel(DdsTableDef table) {
            this.table = table;
            table.getIndices().getLocal().getContainerChangesSupport().addEventListener(this);
            table.getPrimaryKey().getColumnsInfo().getContainerChangesSupport().addEventListener(this);
            update();
        }

        public void removeListeners() {
            table.getIndices().getLocal().getContainerChangesSupport().removeEventListener(this);
            table.getPrimaryKey().getColumnsInfo().getContainerChangesSupport().removeEventListener(this);
        }

        private void update() {
            indices.clear();
            //RADIX-8021
            if (table.getPrimaryKey().isValidIndex()) {
                indices.add(new IndexModel(table.getPrimaryKey(), inherited));
            }
            ArrayList<DdsIndexDef> arr = new ArrayList<DdsIndexDef>();
            for (DdsTableDef curTable = table.findOverwritten(); curTable != null; curTable = curTable.findOverwritten()) {
                for (int i = 0; i < curTable.getIndices().getLocal().size(); ++i) // base indices must me displayed firstly.
                {
                    arr.add(curTable.getIndices().getLocal().get(i));
                }
            }
            RadixObjectsUtils.sortByName(arr);
            for (DdsIndexDef index : arr) {
                indices.add(new IndexModel(index, true));
            }
            arr.clear();
            for (DdsIndexDef idx : table.getIndices().getLocal()) {
                arr.add(idx);
            }
            RadixObjectsUtils.sortByName(arr);
            for (DdsIndexDef index : arr) {
                indices.add(new IndexModel(index, false));
            }
        }

        @Override
        public int getSize() {
            return indices.size();
        }

        @Override
        public Object getElementAt(int index) {
            if (index >= getSize() || index < 0) {
                return null;
            }
            return indices.get(index);
        }

        @Override
        public void onEvent(ContainerChangedEvent e) {
            update();
            fireContentsChanged(this, 0, getSize() - 1);
        }

        public void updateRow(int row) {
            fireContentsChanged(this, row, row);
        }

        public int getIndexOfIndex(DdsIndexDef index) {
            for (int i = 0; i < indices.size(); ++i) {
                if (Utils.equals(indices.get(i).index.getId(), index.getId())) {
                    return i;
                }
            }
            return -1;
        }
    }

    private class IndicesListCellRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JComponent pattern = (JComponent) super.getListCellRendererComponent(list, "NULL", index, isSelected, cellHasFocus);
            if (value == null) {
                return pattern;
            }
            IndexModel indexModel = (IndexModel) value;
            JLabel label = new JLabel(indexModel.index.getName());
            label.setOpaque(true);
            if (indexModel.index.isDeprecated()) {
                Font font = label.getFont();
                Map attributes = font.getAttributes();
                attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
                label.setFont(new Font(attributes));
            }
            label.setBackground(pattern.getBackground());
            if (indexModel.inherited && !isSelected) {
                label.setForeground(Color.GRAY);
            } else {
                label.setForeground(pattern.getForeground());
            }
            if (cellHasFocus) {
                label.setBorder(pattern.getBorder());
            }
            if (index == 0) {
                label.setFont(label.getFont().deriveFont(Font.BOLD));
            }
            return label;
        }
    }
    private final boolean inherited;
    private final IndicesListModel model;
    private final ArrayList<EditingIndexChangeListener> listeners = new ArrayList<EditingIndexChangeListener>();

    public IndicesList(DdsTableDef table) {
        super();
        inherited = table.findOverwritten() != null;
        this.setModel(model = new IndicesListModel(table));
        this.setCellRenderer(new IndicesListCellRenderer());
        this.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        this.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                fireEditingIndexChanged();
            }
        });
        model.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
                updateListSelectionState();

            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                updateListSelectionState();
            }

            @Override
            public void contentsChanged(ListDataEvent e) {
                updateListSelectionState();
            }
        });
    }

    private void updateListSelectionState() {
        int index = getSelectedIndex();
        if (index < 0 || index >= model.getSize()) {
            index = model.getSize() - 1;
        }
        if (index >= 0) {
            setSelectedIndex(index);
        }
        fireEditingIndexChanged();

    }

    public void removeListeners() {
        model.removeListeners();
    }

    public void setSelectedIndex(final DdsIndexDef index) {
//        SwingUtilities.invokeLater(new Runnable() {
//
//            @Override
//            public void run() {
        int idx = model.getIndexOfIndex(index);
        if (idx != -1) {
            IndicesList.this.setSelectedIndex(idx);
        }
//            }
//        });
    }

    public void updateRow(int row) {
        model.updateRow(row);
    }

    public int getCurrentIndexIdx() {
        int ret = this.getSelectionModel().getAnchorSelectionIndex();
        if (ret >= model.getSize()) {
            ret = -1;
        }
//        if (!this.isSelectedIndex(ret))
//            ret = this.getSelectedIndex();
        return ret;
    }

    public void fireEditingIndexChanged() {
        DdsIndexDef index;
        boolean inherited;
        int row = getCurrentIndexIdx();
        if (row != -1) {
            index = ((IndexModel) model.getElementAt(row)).index;
            inherited = ((IndexModel) model.getElementAt(row)).inherited;
        } else {
            index = null;
            inherited = false;
        }
        for (EditingIndexChangeListener listener : listeners) {
            listener.editingIndexChanged(index, inherited);
        }
    }

    public void selectIndices(List<DdsIndexDef> indices) {
        HashSet<Id> indicesIds = new HashSet<Id>(indices.size());
        for (DdsIndexDef index : indices) {
            indicesIds.add(index.getId());
        }
        int[] idxs = new int[indices.size()];
        int p = 0;
        for (int i = 0; i < model.getSize(); ++i) {
            if (indicesIds.contains(((IndexModel) model.getElementAt(i)).index.getId())) {
                idxs[p++] = i;
            }
        }
        while (p < indices.size()) {
            idxs[p++] = -1;
        }
        this.setSelectedIndices(idxs);
    }

    public List<DdsIndexDef> getSelectedDdsIndices() {
        int[] idxs = this.getSelectedIndices();
        ArrayList<DdsIndexDef> ret = new ArrayList<DdsIndexDef>(idxs.length);
        for (int idx : idxs) {
            ret.add(((IndexModel) model.getElementAt(idx)).index);
        }
        return ret;
    }

    public boolean canRemoveSelectedIndices() {
        int[] idxs = this.getSelectedIndices();
        for (int idx : idxs) {
            if (idx == 0 || ((IndexModel) model.getElementAt(idx)).inherited) {
                return false;
            }
        }
        return idxs.length > 0;
    }

    public void addEditingIndexChangeListener(EditingIndexChangeListener listener) {
        listeners.add(listener);
    }

    public void removeEditingIndexChangeListener(EditingIndexChangeListener listener) {
        listeners.remove(listener);
    }
}
