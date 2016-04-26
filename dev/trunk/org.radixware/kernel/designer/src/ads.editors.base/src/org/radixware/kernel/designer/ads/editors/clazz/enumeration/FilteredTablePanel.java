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

package org.radixware.kernel.designer.ads.editors.clazz.enumeration;

import java.awt.LayoutManager;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import org.radixware.kernel.designer.common.dialogs.components.AdvanceTable;
import org.radixware.kernel.designer.common.dialogs.components.AdvanceTableModel;


abstract class FilteredTablePanel extends javax.swing.JPanel {

    public FilteredTablePanel() {
        initComponents();

        getSearchField().addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    getSearchField().setText("");
                }
            }
        });

        getSearchField().getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                if (getValuesTable().getRowSorter() != null) {
                    getValuesTable().getRowSorter().sort();
                }
            }
        });

    }

    @Override
    public void setLayout(LayoutManager mgr) {
        if (mgr != null) {
            super.setLayout(mgr);
        }
    }

    public final JTextField getSearchField() {
        return txtSearch;
    }

    public final AdvanceTable<?> getValuesTable() {
        return (AdvanceTable<?>) valuesTable;
    }

    public TableRowSorter<AdvanceTableModel> setSorter() {
        final AdvanceTableModel model = getValuesTable().getModel();
        if (model != null) {
            final TableRowSorter<AdvanceTableModel> sorter = AdvanceTable.SorterFactory.createInstance(model, getSearchField());

            getValuesTable().setRowSorter(sorter);
            return sorter;
        }
        return null;
    }

    /**
     * Factory method for creating a table component.
     *
     * @return instance of JTable.
     */
    protected abstract AdvanceTable<?> createTable();

    private JTable createTableInternal() {
        final JTable table = createTable();
        if (table == null) {
            return new JTable();
        }
        return table;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblSearch = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        valuesScrollPane = new javax.swing.JScrollPane();
        valuesTable = createTableInternal();

        lblSearch.setText(org.openide.util.NbBundle.getMessage(FilteredTablePanel.class, "FilteredTablePanel.lblSearch.text")); // NOI18N

        txtSearch.setText(org.openide.util.NbBundle.getMessage(FilteredTablePanel.class, "FilteredTablePanel.txtSearch.text")); // NOI18N

        valuesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        valuesScrollPane.setViewportView(valuesTable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(lblSearch)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSearch, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE))
            .addComponent(valuesScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSearch)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(valuesScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblSearch;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JScrollPane valuesScrollPane;
    private javax.swing.JTable valuesTable;
    // End of variables declaration//GEN-END:variables
}
