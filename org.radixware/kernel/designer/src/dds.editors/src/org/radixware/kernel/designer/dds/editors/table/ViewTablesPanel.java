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

package org.radixware.kernel.designer.dds.editors.table;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.DdsViewDef;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinition;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ItemNameRenderer;


public class ViewTablesPanel extends javax.swing.JPanel {

    private class ListModelItem {

        public DdsViewDef.UsedTableRef ref;

        public ListModelItem(DdsViewDef.UsedTableRef ref) {
            this.ref = ref;
        }

        public Id getId() {
            return ref.getTableId();
        }

        public String toString() {
            return "#" + getId().toString();
        }
    }

    private class ListModel implements javax.swing.ListModel<ListModelItem> {

        private List<ListModelItem> items = new ArrayList<>();
        private final List<ListDataListener> listeners = new LinkedList<>();

        public ListModel() {
        }

        public void refresh() {
            items.clear();
            if (view != null) {
                for (DdsViewDef.UsedTableRef ref : view.getUsedTables().list()) {
                    items.add(new ListModelItem(ref));
                }
            }
            fireChange();
        }

        @Override
        public int getSize() {
            return items.size();
        }

        @Override
        public ListModelItem getElementAt(int index) {
            return items.get(index);
        }

        @Override
        public void addListDataListener(ListDataListener l) {
            synchronized (listeners) {
                if (!listeners.contains(l)) {
                    listeners.add(l);
                }
            }
        }

        @Override
        public void removeListDataListener(ListDataListener l) {
            synchronized (listeners) {
                listeners.remove(l);
            }
        }

        private void fireChange() {
            final List<ListDataListener> tmp;
            synchronized (listeners) {
                tmp = new ArrayList<>(listeners);
            }
            final ListDataEvent event = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, items.size() - 1);
            for (ListDataListener l : tmp) {
                l.contentsChanged(event);
            }
        }
    }
    /**
     * Creates new form ViewTablesPanel
     */
    private DdsViewDef view;
    private final ListModel listModel;
    private Id selectedId = null;

    public ViewTablesPanel() {
        initComponents();
        listModel = new ListModel();
        btAddTable.setIcon(RadixWareIcons.CREATE.ADD.getIcon());
        btRemoveTable.setIcon(RadixWareIcons.DELETE.DELETE.getIcon());
        this.lstTables.setModel(listModel);
        this.lstTables.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int index = e.getFirstIndex();
                if (index >= 0 && index < listModel.getSize()) {
                    selectedId = listModel.getElementAt(index).getId();
                } else {
                    selectedId = null;
                }
                updateEnabledState();
            }
        });
        btAddTable.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTable();
            }
        });
        btRemoveTable.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeSelectedTable();
            }
        });
        this.lstTables.setCellRenderer(new ItemNameRenderer(lstTables) {
            @Override
            protected RadixObject getRadixObject(Object object) {
                if (object instanceof ListModelItem) {
                    DdsTableDef table = ((ListModelItem) object).ref.findTable();
                    if (table != null) {
                        return table;
                    }
                }
                return null;
            }
        });
    }

    public void open(DdsViewDef view) {
        this.view = view;
        update();
    }

    private void updateEnabledState() {
        btAddTable.setEnabled(view != null && !view.isReadOnly());
        btRemoveTable.setEnabled(view != null && !view.isReadOnly() && selectedId != null);
    }

    public void update() {
        int index = lstTables.getSelectedIndex();
        Id id = null;
        if (index >= 0 && index < listModel.getSize()) {
            ListModelItem item = listModel.getElementAt(index);
            id = item.getId();
        }
        listModel.refresh();

        if (id != null) {
            for (int i = 0; i < listModel.getSize(); i++) {
                if (listModel.getElementAt(i).getId() == id) {
                    index = i;
                    break;
                }
            }
        }
        if (index >= 0 && index < listModel.getSize()) {
            lstTables.setSelectedIndex(index);
        }
        updateEnabledState();
    }

    private void addTable() {
        if (view == null || view.isReadOnly()) {
            return;
        }
        ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(view, new VisitorProvider() {
            @Override
            public boolean isTarget(RadixObject radixObject) {
                if (radixObject instanceof DdsTableDef) {
                    DdsTableDef table = (DdsTableDef) radixObject;
                    if (view.getUsedTables().getUsedTableIds().contains(table.getId())) {
                        return false;
                    } else {
                        return true;
                    }
                }
                return false;
            }
        });
        Definition def = ChooseDefinition.chooseDefinition(cfg);
        if (def instanceof DdsTableDef) {
            //view.getUsedTables().addTableId(def.getId());
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    update();
                }
            });
        }
    }

    private void removeSelectedTable() {
        if (view == null || view.isReadOnly()) {
            return;
        }
        if (selectedId != null) {
            //view.getUsedTables().removeTableId(selectedId);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    update();
                }
            });
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstTables = new javax.swing.JList();
        jPanel1 = new javax.swing.JPanel();
        btAddTable = new javax.swing.JButton();
        btRemoveTable = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        jFormattedTextField1.setText(org.openide.util.NbBundle.getMessage(ViewTablesPanel.class, "ViewTablesPanel.jFormattedTextField1.text")); // NOI18N

        jScrollPane1.setViewportView(lstTables);

        org.openide.awt.Mnemonics.setLocalizedText(btAddTable, org.openide.util.NbBundle.getMessage(ViewTablesPanel.class, "ViewTablesPanel.btAddTable.text")); // NOI18N
        btAddTable.setToolTipText(org.openide.util.NbBundle.getMessage(ViewTablesPanel.class, "ViewTablesPanel.btAddTable.toolTipText")); // NOI18N
        btAddTable.setFocusable(false);
        btAddTable.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btAddTable.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btAddTable.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel1.add(btAddTable);

        org.openide.awt.Mnemonics.setLocalizedText(btRemoveTable, org.openide.util.NbBundle.getMessage(ViewTablesPanel.class, "ViewTablesPanel.btRemoveTable.text")); // NOI18N
        btRemoveTable.setToolTipText(org.openide.util.NbBundle.getMessage(ViewTablesPanel.class, "ViewTablesPanel.btRemoveTable.toolTipText")); // NOI18N
        btRemoveTable.setFocusable(false);
        btRemoveTable.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btRemoveTable.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btRemoveTable.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel1.add(btRemoveTable);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(ViewTablesPanel.class, "ViewTablesPanel.jLabel1.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addContainerGap())
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 452, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAddTable;
    private javax.swing.JButton btRemoveTable;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList lstTables;
    // End of variables declaration//GEN-END:variables
}
