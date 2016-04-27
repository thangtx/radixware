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
 * TableButtons.java
 *
 * Created on Nov 17, 2009, 2:44:35 PM
 */

package org.radixware.kernel.designer.ads.localization.phrase_book;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.resources.RadixWareIcons;


public class TableButtons extends javax.swing.JPanel {
    private final MlstringPanel tablePanel;

    /** Creates new form TableButtons */
    public TableButtons(final MlstringPanel tablePanel) {
        this.tablePanel=tablePanel;
        initComponents();
        btnAdd.setIcon(RadixWareIcons.CREATE.ADD.getIcon());
        btnRemove.setIcon(RadixWareIcons.DELETE.DELETE.getIcon());
    }

    public void setRemoveBtnEnable(final boolean enable) {
        btnRemove.setEnabled(enable);
    }

    private JMenuItem createMenuItem(final String name,final boolean isEventCode) {
        final JMenuItem item=new JMenuItem();
        item.setName(name);
        item.setText(name);
        item.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(final ActionEvent e) {
                  tablePanel.btnAddClicked(isEventCode);
             }
        });
        return item;
    }

    public void setReadOnly(final boolean readonly) {
        btnRemove.setEnabled(!readonly);
        btnAdd.setEnabled(!readonly);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnAdd = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();

        setMaximumSize(new java.awt.Dimension(32, 32767));

        btnAdd.setPreferredSize(new java.awt.Dimension(32, 32));
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnRemove.setPreferredSize(new java.awt.Dimension(32, 32));
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(141, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        final JPopupMenu menu=new JPopupMenu();
        JMenuItem item=createMenuItem(NbBundle.getMessage(TableButtons.class, "SIMPLE"),false);
        menu.add(item);
        item=createMenuItem(NbBundle.getMessage(TableButtons.class, "EVENT_CODE"),true);
        menu.add(item);
        menu.show(btnAdd, 0, btnAdd.getHeight());
}//GEN-LAST:event_btnAddActionPerformed

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed
        tablePanel.btnRemoveClicked();
}//GEN-LAST:event_btnRemoveActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnRemove;
    // End of variables declaration//GEN-END:variables

}