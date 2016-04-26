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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangedEvent;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangesListener;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.DdsTriggerDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.kernel.common.utils.Utils;


public class TriggersList extends JList {

    public interface EditingTriggerChangeListener {

        public void editingTriggerChanged(DdsTriggerDef editingTrigger, boolean inherited);
    }

    private final class TriggerModel {

        DdsTriggerDef trigger;
        boolean inherited;

        TriggerModel(DdsTriggerDef trigger, boolean inherited) {
            this.trigger = trigger;
            this.inherited = inherited;
        }
    }

    private class TriggersListModel extends AbstractListModel implements ContainerChangesListener {

        private final DdsTableDef table;
        private final ArrayList<TriggerModel> triggers = new ArrayList<TriggerModel>();

        public TriggersListModel(DdsTableDef table) {
            this.table = table;
            table.getTriggers().getLocal().getContainerChangesSupport().addEventListener(this);
            update();
        }

        public void removeListeners() {
            table.getTriggers().getLocal().getContainerChangesSupport().removeEventListener(this);
        }

        private void update() {
            triggers.clear();
            ArrayList<DdsTriggerDef> arr = new ArrayList<DdsTriggerDef>();
            for (DdsTableDef curTable = table.findOverwritten(); curTable != null; curTable = curTable.findOverwritten()) {
                for (int i = 0; i < curTable.getTriggers().getLocal().size(); ++i) // base triggers must me displayed firstly.
                {
                    arr.add(curTable.getTriggers().getLocal().get(i));
                }
            }
            RadixObjectsUtils.sortByName(arr);
            for (DdsTriggerDef trigger : arr) {
                triggers.add(new TriggerModel(trigger, true));
            }
            arr.clear();
            for (DdsTriggerDef trg : table.getTriggers().getLocal()) {
                arr.add(trg);
            }
            RadixObjectsUtils.sortByName(arr);
            for (DdsTriggerDef trigger : arr) {
                triggers.add(new TriggerModel(trigger, false));
            }
        }

        @Override
        public int getSize() {
            return triggers.size();
        }

        @Override
        public Object getElementAt(int index) {
            if (index >= getSize() || index < 0) {
                return null;
            }
            return triggers.get(index);
        }

        @Override
        public void onEvent(ContainerChangedEvent e) {
            update();
            fireContentsChanged(this, 0, getSize() - 1);
        }

        public void updateRow(int row) {
            fireContentsChanged(this, row, row);
        }

        public int getIndexOfTrigger(DdsTriggerDef trigger) {
            for (int i = 0; i < triggers.size(); ++i) {
                if (Utils.equals(triggers.get(i).trigger.getId(), trigger.getId())) {
                    return i;
                }
            }
            return -1;
        }
    }

    private class TriggersListCellRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JComponent pattern = (JComponent) super.getListCellRendererComponent(list, "NULL", index, isSelected, cellHasFocus);
            if (value == null) {
                return pattern;
            }
            TriggerModel triggerModel = (TriggerModel) value;

            JLabel label = new JLabel(triggerModel.trigger.getPresentableName());
            label.setOpaque(true);

            if (triggerModel.trigger.isDeprecated()) {
                Font font = label.getFont();
                Map attributes = font.getAttributes();
                attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
                label.setFont(new Font(attributes));
            }

            label.setBackground(pattern.getBackground());
            if (triggerModel.inherited && !isSelected) {
                label.setForeground(Color.GRAY);
            } else {
                label.setForeground(pattern.getForeground());
            }
            if (cellHasFocus) {
                label.setBorder(pattern.getBorder());
            }
            return label;
        }
    }
    private final TriggersListModel model;
    private final ArrayList<EditingTriggerChangeListener> listeners = new ArrayList<EditingTriggerChangeListener>();

    public TriggersList(DdsTableDef table) {
        super();
        this.setModel(model = new TriggersListModel(table));
        this.setCellRenderer(new TriggersListCellRenderer());
        this.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        this.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                fireEditingTriggerChanged();
            }
        });
    }

    public void removeListeners() {
        model.removeListeners();
    }

    public void setSelectedTrigger(final DdsTriggerDef trigger) {
//        SwingUtilities.invokeLater(new Runnable() {
//
//            @Override
//            public void run() {
        int idx = model.getIndexOfTrigger(trigger);
        if (idx != -1) {
            TriggersList.this.setSelectedIndex(idx);
        }
//            }
//        });
    }

    public void updateRow(int row) {
        model.updateRow(row);
    }

    public int getCurrentTriggerIdx() {
        int ret = this.getSelectionModel().getAnchorSelectionIndex();
        if (ret >= model.getSize()) {
            ret = -1;
        }
//        if (!this.isSelectedIndex(ret))
//            ret = this.getSelectedIndex();
        return ret;
    }

    public void fireEditingTriggerChanged() {
        DdsTriggerDef trigger;
        boolean inherited;
        int row = getCurrentTriggerIdx();
        if (row != -1) {
            trigger = ((TriggerModel) model.getElementAt(row)).trigger;
            inherited = ((TriggerModel) model.getElementAt(row)).inherited;
        } else {
            trigger = null;
            inherited = false;
        }
        for (EditingTriggerChangeListener listener : listeners) {
            listener.editingTriggerChanged(trigger, inherited);
        }
    }

    public List<DdsTriggerDef> getSelectedTriggers() {
        int[] idxs = this.getSelectedIndices();
        ArrayList<DdsTriggerDef> ret = new ArrayList<DdsTriggerDef>(idxs.length);
        for (int idx : idxs) {
            ret.add(((TriggerModel) model.getElementAt(idx)).trigger);
        }
        return ret;
    }

    public void selectTriggers(List<DdsTriggerDef> triggers) {
        HashSet<Id> triggersIds = new HashSet<Id>(triggers.size());
        for (DdsTriggerDef trigger : triggers) {
            triggersIds.add(trigger.getId());
        }
        int[] idxs = new int[triggers.size()];
        int p = 0;
        for (int i = 0; i < model.getSize(); ++i) {
            if (triggersIds.contains(((TriggerModel) model.getElementAt(i)).trigger.getId())) {
                idxs[p++] = i;
            }
        }
        while (p < triggers.size()) {
            idxs[p++] = -1;
        }
        this.setSelectedIndices(idxs);
    }

    public boolean canRemoveSelectedTriggers() {
        int[] idxs = this.getSelectedIndices();
        for (int idx : idxs) {
            if (((TriggerModel) model.getElementAt(idx)).inherited) {
                return false;
            }
        }
        return idxs.length > 0;
    }

    public void addEditingIndexChangeListener(EditingTriggerChangeListener listener) {
        listeners.add(listener);
    }

    public void removeEditingIndexChangeListener(EditingTriggerChangeListener listener) {
        listeners.remove(listener);
    }
}
