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
 * AdsCommandHandlerPropertiesPanel.java
 *
 * Created on Jan 26, 2009, 11:35:39 AM
 */

package org.radixware.kernel.designer.ads.method;

import org.radixware.kernel.common.defs.ads.clazz.members.AdsCommandHandlerMethodDef;
import org.radixware.kernel.common.defs.ads.command.AdsCommandDef;


public class AdsCommandHandlerPropertiesPanel extends javax.swing.JPanel {

    private AdsCommandHandlerMethodDef method;
    /** Creates new form AdsCommandHandlerPropertiesPanel */
    public AdsCommandHandlerPropertiesPanel() {
        initComponents();
    }

    public void open(AdsCommandHandlerMethodDef method){
        this.method = method;
        AdsCommandDef command = method.findCommand();
        commandField.open(command,command!=null?command.getId():null);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        commandNameLabel = new javax.swing.JLabel();
        commandField = new org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel();

        commandNameLabel.setText(org.openide.util.NbBundle.getMessage(AdsCommandHandlerPropertiesPanel.class, "CommandHandler-Label")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(commandNameLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(commandField, javax.swing.GroupLayout.PREFERRED_SIZE, 94, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(commandField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(commandNameLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {commandField, commandNameLabel});

    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel commandField;
    private javax.swing.JLabel commandNameLabel;
    // End of variables declaration//GEN-END:variables

}
