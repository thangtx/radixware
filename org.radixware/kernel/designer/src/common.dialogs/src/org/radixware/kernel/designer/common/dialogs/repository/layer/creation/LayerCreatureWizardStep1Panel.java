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
 * LayerSetupStep1Visual.java
 *
 * Created on Nov 26, 2008, 7:16:31 AM
 */
package org.radixware.kernel.designer.common.dialogs.repository.layer.creation;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.*;
import javax.swing.ComboBoxModel;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListDataListener;
import javax.swing.text.Document;
import org.openide.util.NbBundle;


import org.radixware.kernel.designer.common.dialogs.LanguagesSelector;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.kernel.designer.common.dialogs.components.state.StateManager;


public class LayerCreatureWizardStep1Panel extends javax.swing.JPanel {

    private class LayerComboBoxModel implements ComboBoxModel {

        private class Item {

            private final Layer layer;

            public Item(Layer layer) {
                this.layer = layer;
            }

            @Override
            public String toString() {
                if (layer == null) {
                    return "<Not Defined>";
                }
                return layer.getName() + " (" + layer.getURI() + ")";
            }
        }
        private final List<Item> items;
        private Item selectedItem;

        public LayerComboBoxModel(Branch branch, Layer base) {
            this.items = new LinkedList<Item>();
            selectedItem = new Item(null);
            items.add(selectedItem);
            for (Layer l : branch.getLayers().getInOrder()) {
                Item item = new Item(l);
                items.add(item);
                if (l == base) {
                    selectedItem = item;
                }
            }
        }

        @Override
        public void setSelectedItem(Object anItem) {
            selectedItem = (Item) anItem;
        }

        @Override
        public Object getSelectedItem() {
            return selectedItem;
        }

        @Override
        public int getSize() {
            return items.size();
        }

        @Override
        public Object getElementAt(int index) {
            return items.get(index);
        }

        @Override
        public void addListDataListener(ListDataListener l) {
        }

        @Override
        public void removeListDataListener(ListDataListener l) {
        }
    }
    LayerCreatureWizardStep1 step;
    private List<EIsoLanguage> languages;
    private StateManager state;

    /**
     * Creates new form LayerSetupStep1Visual
     */
    public LayerCreatureWizardStep1Panel() {
        this.languages = Collections.emptyList();

        initComponents();
        this.cbPrevLayer.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (step != null) {
                    Layer prevLayer=((LayerComboBoxModel.Item) e.getItem()).layer;
                    step.setPrevLayer(prevLayer);
                    generateLocalizingURI();
                    isComplete();
                }
            }
        });

        this.state = new StateManager(this);

        DocumentListener docListener = new DocumentListener() {

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
                if (step != null) {
                    Document doc = e.getDocument();
                    if (doc.equals(titleValue.getDocument())) {
                        step.setTitle(titleValue.getText());
                    } else if (doc.equals(uriValue.getDocument())) {
                        step.setURI(uriValue.getText());                        
                    } else if (doc.equals(copyrightValue.getDocument())) {
                        step.setCopyright(copyrightValue.getText());
                    } else if (doc.equals(nameField.getDocument())) {
                        step.setName(nameField.getText());
                    }
                }
            }
        };
        titleValue.getDocument().addDocumentListener(docListener);
        uriValue.getDocument().addDocumentListener(docListener);
        copyrightValue.getDocument().addDocumentListener(docListener);
        nameField.getDocument().addDocumentListener(docListener);

    }

    void open(final LayerCreatureWizardStep1 step) {
        this.step = null;
        this.titleValue.setText(step.getTitle());
        this.nameField.setText(step.getName());
        this.uriValue.setText(step.getURI());
        this.copyrightValue.setText(step.getCopyright());
        this.languages = step.getLanguages();
        cbPrevLayer.setModel(new LayerComboBoxModel(step.getBranch(), step.getPrevLayer()));
        refreshLanguagesField();
        this.step = step;

        isComplete();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        titleValue = new javax.swing.JTextField();
        titleLabel = new javax.swing.JLabel();
        copyrightLabel = new javax.swing.JLabel();
        copyrightValue = new javax.swing.JTextField();
        languagesLabel = new javax.swing.JLabel();
        languagesValue = new javax.swing.JTextField();
        selectLanguagesBtn = new javax.swing.JButton();
        uriLabel = new javax.swing.JLabel();
        uriValue = new javax.swing.JTextField();
        baseLayerLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        cbPrevLayer = new javax.swing.JComboBox();
        chbIsLocalizing = new javax.swing.JCheckBox();

        titleLabel.setText(org.openide.util.NbBundle.getMessage(LayerCreatureWizardStep1Panel.class, "LayerCreature-Title")); // NOI18N

        copyrightLabel.setText(org.openide.util.NbBundle.getMessage(LayerCreatureWizardStep1Panel.class, "LayerCreature-Copyright")); // NOI18N

        languagesLabel.setText(org.openide.util.NbBundle.getMessage(LayerCreatureWizardStep1Panel.class, "LayerCreature-Languages")); // NOI18N

        languagesValue.setEditable(false);

        selectLanguagesBtn.setText(org.openide.util.NbBundle.getMessage(LayerCreatureWizardStep1Panel.class, "LayerCreature-ConfButton")); // NOI18N
        selectLanguagesBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectLanguagesBtnActionPerformed(evt);
            }
        });

        uriLabel.setText(org.openide.util.NbBundle.getMessage(LayerCreatureWizardStep1Panel.class, "LayerCreature-URI")); // NOI18N

        baseLayerLabel.setText(org.openide.util.NbBundle.getMessage(LayerCreatureWizardStep1Panel.class, "LayerCreature-BaseLayer")); // NOI18N

        jLabel1.setText(org.openide.util.NbBundle.getMessage(LayerCreatureWizardStep1Panel.class, "LayerCreature-Name")); // NOI18N

        cbPrevLayer.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        chbIsLocalizing.setText(org.openide.util.NbBundle.getMessage(LayerCreatureWizardStep1Panel.class, "LayerCreatureWizardStep1Panel.chbIsLocalizing.text")); // NOI18N
        chbIsLocalizing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chbIsLocalizingActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(languagesLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(copyrightLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(uriLabel))
                    .addComponent(titleLabel)
                    .addComponent(baseLayerLabel)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(chbIsLocalizing)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(nameField, javax.swing.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
                    .addComponent(titleValue, javax.swing.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
                    .addComponent(copyrightValue, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
                    .addComponent(uriValue, javax.swing.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(languagesValue, javax.swing.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(selectLanguagesBtn))
                    .addComponent(cbPrevLayer, 0, 324, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(titleValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(titleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(copyrightValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(copyrightLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(languagesValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(selectLanguagesBtn)
                    .addComponent(languagesLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(uriValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(uriLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(baseLayerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbPrevLayer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chbIsLocalizing)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void selectLanguagesBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectLanguagesBtnActionPerformed
        LanguagesSelector.configureLanguages(languages);
        refreshLanguagesField();
        if (this.step != null) {
            this.step.setLanguages(languages);
        }
        generateLocalizingURI();
}//GEN-LAST:event_selectLanguagesBtnActionPerformed

    private void chbIsLocalizingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chbIsLocalizingActionPerformed
        // TODO add your handling code here:
       boolean isLocalizing=chbIsLocalizing.isSelected();
       this.step.setIsLocalizing(isLocalizing);
       if(isLocalizing){
           uriValue.setEditable(false);
           generateLocalizingURI();          
       }else{
           uriValue.setEditable(true);
       }
       //isComplete();
    }//GEN-LAST:event_chbIsLocalizingActionPerformed

    private void generateLocalizingURI(){
        if (chbIsLocalizing.isSelected() 
                && cbPrevLayer.getSelectedItem() != null 
                && ((LayerComboBoxModel.Item)  cbPrevLayer.getSelectedItem()).layer != null
                && !languages.isEmpty()) {
            uriValue.setText(((LayerComboBoxModel.Item) cbPrevLayer.getSelectedItem()).layer.getURI() + Layer.LOCALE_LAYER_SUFFIX + languages.get(0).getValue());
        }
    }
    private void refreshLanguagesField() {
        //update listing of languages
        final int len = this.languages.size();
        if (len > 0) {

            final java.lang.StringBuilder stringBuilder = new java.lang.StringBuilder();

            final EIsoLanguage[] langs = new EIsoLanguage[len];
            this.languages.toArray(langs);
            int i = 0;
            for (; i < len - 1; ++i) {
                stringBuilder.append(langs[i].getName() + ", ");
            }
            stringBuilder.append(langs[i].getName());

            languagesValue.setText(stringBuilder.toString());
        } else {
            languagesValue.setText("");
        }
    }

    private static boolean URINameIsValid(final String str) {
        return RadixObjectsUtils.isCorrectName(str);
    }

    boolean isComplete() {
        boolean result = true;
        if (!URINameIsValid(nameField.getText())) {
            state.error(NbBundle.getMessage(LayerCreatureWizardStep1Panel.class, "Error-Invalid-Layer-Name"));
            result = false;
        } else if (titleValue.getText().isEmpty()) {
            state.error(NbBundle.getMessage(LayerCreatureWizardStep1Panel.class, "Error-Invalid-Layer-Title"));
            result = false;
        } else if (!RadixObjectsUtils.isCorrectURL(uriValue.getText())) {
            state.error(NbBundle.getMessage(LayerCreatureWizardStep1Panel.class, "Error-Invalid-Layer-URI"));
            result = false;
        }  else if (chbIsLocalizing.isSelected()){
            Layer baseLayer=((LayerComboBoxModel.Item)  cbPrevLayer.getSelectedItem()).layer;
            if(baseLayer==null) {
                state.error(NbBundle.getMessage(LayerCreatureWizardStep1Panel.class, "Error-Invalid-Layer-Base"));
                result = false; 
            }else{
                if(languages.size()==1){
                    result = checkLangForLayer( baseLayer, languages.get(0));
                }else{
                    state.error(NbBundle.getMessage(LayerCreatureWizardStep1Panel.class, "Error-Invalid-Localizing-Layer-Lang-Number"));
                    result = false; 
                }
            }
            if(result) {
                state.ok(); 
            }         
        }else {
            result = true;
            state.ok();
        }
        return result;
    }
    
    private boolean checkLangForLayer(Layer baseLayer,EIsoLanguage lang){
        if(baseLayer.getLanguages().contains(lang)){
            state.error(NbBundle.getMessage(LayerCreatureWizardStep1Panel.class, "Error-Invalid-Localizing-Layer-Lang"));
            return false;
        }
        for(Layer layer:baseLayer.getBranch().getLayers()){
            if(layer.isLocalizing() && layer.getBaseLayerURIs().contains(baseLayer.getURI())&&
                 layer.getLanguages().contains(lang)){
                state.error(NbBundle.getMessage(LayerCreatureWizardStep1Panel.class, "Error-Invalid-Localizing-Layer-Lang"));
                return false;
            }
        }
        return true;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel baseLayerLabel;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cbPrevLayer;
    private javax.swing.JCheckBox chbIsLocalizing;
    private javax.swing.JLabel copyrightLabel;
    private javax.swing.JTextField copyrightValue;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel languagesLabel;
    private javax.swing.JTextField languagesValue;
    private javax.swing.JTextField nameField;
    private javax.swing.JButton selectLanguagesBtn;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JTextField titleValue;
    private javax.swing.JLabel uriLabel;
    private javax.swing.JTextField uriValue;
    // End of variables declaration//GEN-END:variables
}
