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
 * WarningPanel.java
 *
 * Created on Nov 19, 2009, 5:35:13 PM
 */
package org.radixware.kernel.designer.ads.localization.prompt;

import java.awt.Component;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import org.radixware.kernel.common.resources.RadixWareIcons;


public class WarningPanel extends javax.swing.JPanel {

    private final DefaultListModel<String> listModel;

    /**
     * Creates new form WarningPanel
     */
    public WarningPanel() {
        initComponents();
        listModel = new DefaultListModel<>();
        warList.setModel(listModel);
        warList.setCellRenderer(new StatusCellRenderer());
    }

    public void setWarningList(final List<String> warningList) {
        listModel.clear();
        if (warningList == null) {
            return;
        }
        for (int i = 0; i < warningList.size(); i++) {
            listModel.addElement(warningList.get(i));//.add(i, warningList.get(i));
        }
    }

    public boolean hasItems() {
        return listModel.getSize() > 0;
    }

    private class StatusCellRenderer extends DefaultListCellRenderer {

        @Override
        @SuppressWarnings("rawtypes")
        public Component getListCellRendererComponent(final javax.swing.JList list, final Object value, final int row, final boolean isSelected, final boolean hasFocus) {
            super.getListCellRendererComponent(list, value, row, isSelected, hasFocus);
            setText(listModel.getElementAt(row));
            final Icon icon = RadixWareIcons.CHECK.ERRORS.getIcon(16, 16);
            setIcon(icon);
            // this.setHorizontalAlignment(SwingConstants.CENTER);

            //RowString rowString=(RowString) ((TableModel)table.getModel()).getRow(row);
            //String toolTip=rowString.getToolTip(sourceLangs, translLangs);
            //this.setToolTipText(toolTip);
            return this;
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

        jScrollPane1 = new javax.swing.JScrollPane();
        warList = new javax.swing.JList<String>();

        setPreferredSize(new java.awt.Dimension(269, 50));

        jScrollPane1.setBackground(java.awt.Color.white);
        jScrollPane1.setBorder(null);
        jScrollPane1.setViewportBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 0, 0));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(258, 50));

        warList.setBorder(null);
        jScrollPane1.setViewportView(warList);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList<String> warList;
    // End of variables declaration//GEN-END:variables
}