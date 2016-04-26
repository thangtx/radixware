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

/*
 * NewTitleItemsPanel.java
 *
 * Created on Mar 18, 2009, 4:38:22 PM
 */

package org.radixware.kernel.designer.ads.editors.clazz.simple;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;



public class NewTitleItemsPanel extends javax.swing.JPanel {

    private IAdsPropertiesListProvider propertiesListProvider;
    /** Creates new form NewTitleItemsPanel */
    public NewTitleItemsPanel(IAdsPropertiesListProvider propertiesListProvider) {
        super();
        this.propertiesListProvider = propertiesListProvider;
        initComponents();
       
        update();

        //set position
        final Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((dimension.width - getWidth()) / 2, (dimension.height - getHeight()) / 2);
    }

    private void update(){
        setupTable();
    }

    private void setupTable(){

        javax.swing.ToolTipManager.sharedInstance().unregisterComponent(titleItemsTable);
        titleItemsTable.setModel(new TitleItemsTableModel(propertiesListProvider));

        final TableColumnModel columnModel = titleItemsTable.getColumnModel();
        final TableColumn checkboxesColumn = columnModel.getColumn(TitleItemsTableModel.COLUMN_CHECK_INDEX);

        checkboxesColumn.setMaxWidth(20);
        checkboxesColumn.setPreferredWidth(20);

        titleItemsTable.setRowHeight(24);

        titleItemsTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        titleItemsTable.setAutoCreateColumnsFromModel(false);
        titleItemsTable.setColumnSelectionAllowed(false);
        titleItemsTable.setRowSelectionAllowed(true);

        final int rowsCount = titleItemsTable.getRowCount();
        if (rowsCount > 0) {
            final int newRowPosition = rowsCount - 1;
            titleItemsTable.setRowSelectionInterval(newRowPosition, newRowPosition);
        }

        titleItemsTable.addMouseListener(new MouseAdapter() {

            private static final int DOUBLE_CLICK = 2;

            @Override
            public void mouseClicked(MouseEvent e) {

                if (e.getClickCount() == DOUBLE_CLICK && titleItemsTable.getSelectedColumn() == TitleItemsTableModel.COLUMN_PROPERTY_TITLE_INDEX){
                    final int currentRow = titleItemsTable.getSelectedRow();
                    titleItemsTable.setValueAt( ! ((Boolean) titleItemsTable.getValueAt(currentRow, TitleItemsTableModel.COLUMN_CHECK_INDEX)), currentRow, TitleItemsTableModel.COLUMN_CHECK_INDEX);
                }
            }
        });
    }

    public List<AdsPropertyDef> getSelectedProperties(){
        return ((TitleItemsTableModel)titleItemsTable.getModel()).getSelectedProperties();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        titleItemsTable = new javax.swing.JTable();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        jScrollPane1.setAutoscrolls(true);

        titleItemsTable.setModel(new TitleItemsTableModel(propertiesListProvider));
        titleItemsTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
        titleItemsTable.setMaximumSize(null);
        titleItemsTable.setMinimumSize(null);
        titleItemsTable.setPreferredSize(null);
        jScrollPane1.setViewportView(titleItemsTable);

        add(jScrollPane1);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable titleItemsTable;
    // End of variables declaration//GEN-END:variables

}
