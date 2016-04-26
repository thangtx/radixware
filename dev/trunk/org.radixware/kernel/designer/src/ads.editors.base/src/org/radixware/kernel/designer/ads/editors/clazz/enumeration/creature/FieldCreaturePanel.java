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

package org.radixware.kernel.designer.ads.editors.clazz.enumeration.creature;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.designer.common.dialogs.components.values.NameEditorComponent;


final class FieldCreaturePanel extends javax.swing.JPanel {
    final NameEditorComponent editorComponent;

    public FieldCreaturePanel() {
        editorComponent = new NameEditorComponent();
        initComponents();
    }

    public void opne() {
        editorComponent.open(new NameEditorComponent.NamedContext() {

            @Override
            public String getName() {
                return "field";
            }

            @Override
            public void setName(String name) {
            }

            @Override
            public boolean isValidName(String name) {
                return !Utils.emptyOrNull(name);
            }
        }, "field");
    }

    public String getFieldName() {
        return editorComponent.getValue();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = editorComponent.getEditorComponent();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        jLabel1.setText(org.openide.util.NbBundle.getMessage(FieldCreaturePanel.class, "FieldCreaturePanel.jLabel1.text")); // NOI18N
        jLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 8));
        jPanel1.add(jLabel1);

        jTextField1.setText(org.openide.util.NbBundle.getMessage(FieldCreaturePanel.class, "FieldCreaturePanel.jTextField1.text")); // NOI18N
        jPanel1.add(jTextField1);

        add(jPanel1, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
