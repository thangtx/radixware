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
 * EmbeddedSourceEditorCodePanel.java
 *
 * Created on Nov 21, 2011, 3:14:25 PM
 */
package org.radixware.kernel.designer.ads.editors.clazz.simple;

import javax.swing.SwingUtilities;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.openide.util.ChangeSupport;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef.SourcePart;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;


public class EmbeddedSourceEditorCodePanel extends javax.swing.JPanel {

    private SourcePart part;
    private boolean isEditable;

    /** Creates new form EmbeddedSourceEditorCodePanel */
    public EmbeddedSourceEditorCodePanel() {
        initComponents();
    }

    public void open(SourcePart part, OpenInfo openInfo) {
        this.part = part;
        update(openInfo);
    }
    private final DocumentListener descriptionListener = new DocumentListener() {

        @Override
        public void insertUpdate(DocumentEvent e) {
            descriptionChange();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            descriptionChange();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            descriptionChange();
        }
    };

    private void descriptionChange() {
        if (part != null) {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    part.setDescription(edDescription.getText());
                    changeSupport.fireChange();
                }
            });


        }
    }

    private void update(OpenInfo openInfo) {
        if (part != null) {
            editor.open(part, openInfo);
            edDescription.getDocument().removeDocumentListener(descriptionListener);
            edDescription.setText(part.getDescription());
            edDescription.getDocument().addDocumentListener(descriptionListener);
            edDescription.setEditable(isEditable && !part.isReadOnly());
            editor.setEditable(isEditable && !part.isReadOnly());
            envSelectorPanel.open(part);
        }
    }
    private final ChangeSupport changeSupport = new ChangeSupport(this);

    public void removeChangeListener(ChangeListener listener) {
        changeSupport.removeChangeListener(listener);
    }

    public void addChangeListener(ChangeListener listener) {
        changeSupport.addChangeListener(listener);
    }

    public void setEditable(boolean isEditable) {
        this.isEditable = isEditable;
        update((OpenInfo) null);
    }

    public boolean requestFocusInWindow() {
        if (editor != null) {
            return editor.getPane().requestFocusInWindow();
        } else {
            return super.requestFocusInWindow();
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

        editor = new org.radixware.kernel.designer.common.editors.jml.JmlEditor();
        jLabel1 = new javax.swing.JLabel();
        edDescription = new javax.swing.JTextField();
        envSelectorPanel = new org.radixware.kernel.designer.ads.editors.base.EnvSelectorPanel();

        jLabel1.setText(org.openide.util.NbBundle.getMessage(EmbeddedSourceEditorCodePanel.class, "EmbeddedSourceEditorCodePanel.jLabel1.text")); // NOI18N

        edDescription.setText(org.openide.util.NbBundle.getMessage(EmbeddedSourceEditorCodePanel.class, "EmbeddedSourceEditorCodePanel.edDescription.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edDescription, javax.swing.GroupLayout.DEFAULT_SIZE, 522, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(envSelectorPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 562, Short.MAX_VALUE)
                            .addComponent(editor, javax.swing.GroupLayout.DEFAULT_SIZE, 562, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(edDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(envSelectorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(editor, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField edDescription;
    private org.radixware.kernel.designer.common.editors.jml.JmlEditor editor;
    private org.radixware.kernel.designer.ads.editors.base.EnvSelectorPanel envSelectorPanel;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}