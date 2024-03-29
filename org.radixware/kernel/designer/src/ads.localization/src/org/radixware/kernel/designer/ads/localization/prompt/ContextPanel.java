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
 * ContextPanel.java
 *
 * Created on Sep 10, 2009, 4:22:36 PM
 */

package org.radixware.kernel.designer.ads.localization.prompt;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import org.openide.util.lookup.Lookups;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.designer.ads.localization.RowString;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;


public class ContextPanel extends javax.swing.JPanel {

    private RowString rowString;
    /** Creates new form ContextPanel */
    public ContextPanel() {
        initComponents();
        contextArea.addHyperlinkListener(new HyperlinkListener(){

            @Override
            public void hyperlinkUpdate(final HyperlinkEvent event) {
                if ( event.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)){
                    goToContextDef();
                }
            }

        });
        contextArea.setBorder(null);
        jScrollPane1.setBorder(null);
        contextArea.setEditable(false);
    }

    public void setContext(final RowString rowString) {
        this.rowString=rowString;
        if(rowString==null){
            clearPanel();
            return;
        }
        final String sContext=rowString.getStrContext();
        contextArea.setText(sContext);
    }

    public void clearPanel() {
        contextArea.setText("");
    }

    private void goToContextDef() {
         final RadixObject definition=rowString.getContextDef();
        if (definition != null) {
            final OpenInfo inf=new OpenInfo((RadixObject)rowString.getMlStrings().get(0),Lookups.fixed(rowString.getMlStrings().get(0)));
            DialogUtils.goToObject(definition,inf);
        }
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
        contextArea = new javax.swing.JEditorPane();

        setBackground(java.awt.Color.white);

        jScrollPane1.setBackground(java.awt.Color.white);
        jScrollPane1.setBorder(null);
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        contextArea.setBorder(null);
        contextArea.setContentType("text/html"); // NOI18N
        jScrollPane1.setViewportView(contextArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JEditorPane contextArea;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

}
