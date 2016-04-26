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
 * AddWordPanel.java
 *
 * Created on Dec 19, 2011, 1:53:28 PM
 */
package org.radixware.kernel.designer.common.editors.spellcheck;

import org.radixware.kernel.designer.common.dialogs.components.WordEditorPanel;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import org.radixware.kernel.common.check.spelling.Word;
import org.radixware.kernel.designer.common.dialogs.components.state.StateManager;
import org.radixware.kernel.designer.common.dialogs.components.values.ValueChangeEvent;
import org.radixware.kernel.designer.common.dialogs.components.values.ValueChangeListener;


final class AddWordPanel extends javax.swing.JPanel {

    public static final String PROP_NAME_WORD_VALIDITY = "dictionaryWordValidity";
    private StateManager manager;

    public AddWordPanel() {
        this(null, "");
    }
    private boolean oldValue;

    public AddWordPanel(Word word, String description) {
        initComponents();

        setMinimumSize(new Dimension(200, 100));
        setPreferredSize(new Dimension(340, 230));
        setMaximumSize(new Dimension(1000, 800));

        setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        manager = new StateManager(this);
        getWordEditorPanel().setValue(word);
        txtDescription.setText(description);

        oldValue = word != null;

        getWordEditorPanel().addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChanged(ValueChangeEvent e) {
                boolean valid = getWordEditorPanel().isSetValue();
                if (valid != oldValue) {
                    firePropertyChange(PROP_NAME_WORD_VALIDITY, oldValue, valid);
                    oldValue = valid;
                }
            }
        });
    }

    public Word getWord() {
        return getWordEditorPanel().getValue();
    }

    public String getDescription() {
        return txtDescription.getText();
    }

    public boolean isValidWord() {
        return manager.isErrorneous();
    }

    @Override
    public void requestFocus() {
        getWordEditorPanel().requestFocus();
    }

    private WordEditorPanel getWordEditorPanel() {
        return (WordEditorPanel) wordEditor;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jScrollPane1 = new javax.swing.JScrollPane();
        txtDescription = new javax.swing.JTextArea();
        lblWord = new javax.swing.JLabel();
        lblDescription = new javax.swing.JLabel();
        wordEditor = new WordEditorPanel();

        setLayout(new java.awt.GridBagLayout());

        txtDescription.setColumns(20);
        txtDescription.setRows(5);
        jScrollPane1.setViewportView(txtDescription);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 652;
        gridBagConstraints.ipady = 370;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jScrollPane1, gridBagConstraints);

        lblWord.setText(org.openide.util.NbBundle.getMessage(AddWordPanel.class, "AddWordPanel.lblWord.text")); // NOI18N
        lblWord.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 4, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        add(lblWord, gridBagConstraints);

        lblDescription.setText(org.openide.util.NbBundle.getMessage(AddWordPanel.class, "AddWordPanel.lblDescription.text")); // NOI18N
        lblDescription.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 4, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 12;
        gridBagConstraints.ipady = 6;
        add(lblDescription, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(wordEditor, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblDescription;
    private javax.swing.JLabel lblWord;
    private javax.swing.JTextArea txtDescription;
    private javax.swing.JPanel wordEditor;
    // End of variables declaration//GEN-END:variables
}
