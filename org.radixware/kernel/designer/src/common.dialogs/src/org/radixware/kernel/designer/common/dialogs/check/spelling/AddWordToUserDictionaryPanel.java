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
 * AddWordToUserDictionaryPanel.java
 *
 * Created on Feb 7, 2012, 7:47:37 AM
 */
package org.radixware.kernel.designer.common.dialogs.check.spelling;

import java.util.LinkedList;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import org.radixware.kernel.common.check.spelling.Word;
import org.radixware.kernel.common.defs.HierarchyWalker;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.designer.common.dialogs.components.DictionaryLanguageListModel;
import org.radixware.kernel.designer.common.dialogs.components.WordEditorPanel;
import org.radixware.kernel.designer.common.dialogs.components.state.StateDisplayer;
import org.radixware.kernel.designer.common.dialogs.components.state.StateManager;
import org.radixware.kernel.designer.common.dialogs.components.values.ValueChangeEvent;
import org.radixware.kernel.designer.common.dialogs.components.values.ValueChangeListener;


class AddWordToUserDictionaryPanel extends javax.swing.JPanel {

    public static final String CHANGE_STATE_PROP_NAME = "AddWordToUserDictionary.Changed";
    private final StateManager stateManager = new StateManager(this);

    public AddWordToUserDictionaryPanel(String strWord, String description, Layer layer, EIsoLanguage language) {
        initComponents();

        final WordEditorPanel wordEditor = getWordEditor();

        wordEditor.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChanged(ValueChangeEvent e) {
                boolean setValue = wordEditor.isSetValue();
                if (setValue == stateManager.isErrorneous()) {
                    if (setValue) {
                        stateManager.ok();
                    } else {
                        stateManager.error("Empty word");
                    }
                    firePropertyChange(CHANGE_STATE_PROP_NAME, !setValue, setValue);
                }
            }
        });

        getWordEditor().setValue(Word.Factory.newInstance(strWord));

        DictionaryLanguageListModel dictionaryLanguageListModel = new DictionaryLanguageListModel(layer.getLanguages(), language);
        cmbLanguage.setModel(dictionaryLanguageListModel);

        LayerListModel layerListModel = new LayerListModel(layer);
        cmbLayer.setModel(layerListModel);

        stateManager.ok();

        wordEditor.requestFocus();
    }

    private WordEditorPanel getWordEditor() {
        return (WordEditorPanel) wordNamePanel;
    }

    public EIsoLanguage getLanguage() {
        Object lang = cmbLanguage.getSelectedItem();
        return lang instanceof EIsoLanguage ? (EIsoLanguage) lang : null;
    }

    public Layer getLayer() {
        LayerListItem layerItem = (LayerListItem) cmbLayer.getSelectedItem();
        return layerItem.layer;
    }

    public Word getWord() {
        return getWordEditor().getValue();
    }

    public String getWordDescription() {
        return txtDescription.getText();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel1 = new javax.swing.JLabel();
        cmbLayer = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        cmbLanguage = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDescription = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        statePanel = new StateDisplayer();
        wordNamePanel = new WordEditorPanel();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 4, 4, 4));
        setLayout(new java.awt.GridBagLayout());

        jLabel1.setText(org.openide.util.NbBundle.getMessage(AddWordToUserDictionaryPanel.class, "AddWordToUserDictionaryPanel.jLabel1.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        add(jLabel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        add(cmbLayer, gridBagConstraints);

        jLabel2.setText(org.openide.util.NbBundle.getMessage(AddWordToUserDictionaryPanel.class, "AddWordToUserDictionaryPanel.jLabel2.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        add(jLabel2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        add(cmbLanguage, gridBagConstraints);

        txtDescription.setColumns(20);
        txtDescription.setRows(5);
        jScrollPane1.setViewportView(txtDescription);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        add(jScrollPane1, gridBagConstraints);

        jLabel3.setText(org.openide.util.NbBundle.getMessage(AddWordToUserDictionaryPanel.class, "AddWordToUserDictionaryPanel.jLabel3.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        add(jLabel3, gridBagConstraints);

        jLabel4.setText(org.openide.util.NbBundle.getMessage(AddWordToUserDictionaryPanel.class, "AddWordToUserDictionaryPanel.jLabel4.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        add(jLabel4, gridBagConstraints);

        statePanel.setMinimumSize(new java.awt.Dimension(10, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(statePanel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        add(wordNamePanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbLanguage;
    private javax.swing.JComboBox cmbLayer;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel statePanel;
    private javax.swing.JTextArea txtDescription;
    private javax.swing.JPanel wordNamePanel;
    // End of variables declaration//GEN-END:variables

    private static final class LayerListModel extends AbstractListModel implements ComboBoxModel {

        private final List<LayerListItem> layers = new LinkedList<>();
        private LayerListItem selected;

        public LayerListModel(RadixObject context) {
            if (context != null) {
                Layer layer = context.getLayer();
                if (layer != null) {
                    collectLayers(layer);
                    setSelectedItem(findItemBy(layer));
                }
            }
        }

        private void collectLayers(Layer layer) {
            Layer.HierarchyWalker.walk(layer, new Layer.HierarchyWalker.Acceptor<Object>() {

                @Override
                public void accept(HierarchyWalker.Controller<Object> controller, Layer layer) {
                    layers.add(0, new LayerListItem(layer));
                }
            });
        }

        @Override
        public int getSize() {
            return layers.size();
        }

        @Override
        public Object getElementAt(int index) {
            return layers.get(index);
        }

        @Override
        public void setSelectedItem(Object layer) {
            selected = (LayerListItem) layer;
        }

        @Override
        public Object getSelectedItem() {
            return selected;
        }

        private LayerListItem findItemBy(Layer layer) {
            for (LayerListItem item : layers) {
                if (item.layer == layer) {
                    return item;
                }
            }
            return null;
        }
    }

    private static final class LayerListItem {

        private final Layer layer;

        public LayerListItem(Layer layer) {
            this.layer = layer;
        }

        @Override
        public String toString() {
            if (layer != null) {
                return layer.getName();
            }
            return "<null>";
        }
    }
}
