package org.radixware.kernel.designer.ads.editors.documentation;

import java.awt.BorderLayout;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.doc.AdsDocTopicDef;
import org.radixware.kernel.common.defs.ads.doc.DocTopicBody;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.mml.Mml;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.components.localizing.HandleInfo;
import org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingEditorPanel;
import org.radixware.kernel.designer.common.general.utils.SwingUtils;

public final class AdsDocTopicEditor extends AdsDocEditor<AdsDocTopicDef> {

    private DocTopicBody body;
    private EIsoLanguage language;
    private LocalizingEditorPanel titleEditor = new LocalizingEditorPanel();

    public AdsDocTopicEditor(AdsDocTopicDef topic) {

        super(topic);
        initComponents();
        editorName.setText(getRadixObject().getName());

        // title
        titleEditor.open(new HandleInfo() {

            @Override
            public Id getTitleId() {
                return AdsDocTopicEditor.this.getRadixObject().getTitleId();
            }

            @Override
            public AdsDefinition getAdsDefinition() {
                return AdsDocTopicEditor.this.getRadixObject();
            }

            @Override
            protected void onAdsMultilingualStringDefChange(IMultilingualStringDef multilingualStringDef) {
                if (multilingualStringDef != null) {
                    AdsDocTopicEditor.this.getRadixObject().setTitleId(multilingualStringDef.getId());
                } else {
                    AdsDocTopicEditor.this.getRadixObject().setTitleId(null);
                }
            }

            @Override
            protected void onLanguagesPatternChange(EIsoLanguage language, String newStringValue) {
                if (getAdsMultilingualStringDef() != null) {
                    getAdsMultilingualStringDef().setValue(language, newStringValue);
                }
            }
        }
        );

        panelTitle.setLayout(new BorderLayout());
        panelTitle.add(titleEditor, BorderLayout.CENTER);

        // lang
        final List<EIsoLanguage> languages = getRadixObject().getLayer().getLanguages();
        DefaultComboBoxModel languageModel = new DefaultComboBoxModel(languages.toArray());
        comboBoxLanguage.setModel(languageModel);

        language = getRadixObject().getLayer().getMainDocLanguage();
        comboBoxLanguage.setSelectedItem(language);

        // readOnly
        Boolean enable = !getRadixObject().isReadOnly();
        editorName.setEditable(enable);
        titleEditor.setEnabled(enable);

        // update
        update();
    }

    @Override
    public void update() {

        // open mml
        body = getRadixObject().getBody(language);
        Mml mml = body.getContent();
        mmlEditor.open(mml, getRadixObject(), null);

        // mainBody
        DocTopicBody mainBody = getRadixObject().getMainDocLanguageBody();
        if (mainBody == body) {
            checkBoxAgreed.setEnabled(false);
            checkBoxCompleted.setEnabled(false);

            checkBoxAgreed.setSelected(false);
            checkBoxCompleted.setSelected(false);
        } else {
            checkBoxAgreed.setEnabled(!getRadixObject().isReadOnly());
            checkBoxCompleted.setEnabled(!getRadixObject().isReadOnly());

            checkBoxAgreed.setSelected(body.getIsAgreed());
            checkBoxCompleted.setSelected(body.getIsCompleted());
        }

        SwingUtils.checkValidName(editorName, getRadixObject());
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        editorName = new javax.swing.JTextField();
        comboBoxLanguage = new javax.swing.JComboBox();
        checkBoxAgreed = new javax.swing.JCheckBox();
        panelTitle = new javax.swing.JPanel();
        jLabelName = new javax.swing.JLabel();
        jLabelLanguage = new javax.swing.JLabel();
        checkBoxCompleted = new javax.swing.JCheckBox();
        jPanelEditor = new javax.swing.JPanel();
        mmlEditor = new org.radixware.kernel.designer.common.editors.mml.MmlEditor();

        setPreferredSize(new java.awt.Dimension(600, 600));

        editorName.setText(org.openide.util.NbBundle.getMessage(AdsDocTopicEditor.class, "AdsDocTopicEditor.editorName.text")); // NOI18N
        editorName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                editorNameKeyReleased(evt);
            }
        });

        comboBoxLanguage.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboBoxLanguage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxLanguageActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(checkBoxAgreed, org.openide.util.NbBundle.getMessage(AdsDocTopicEditor.class, "AdsDocTopicEditor.checkBoxAgreed.text")); // NOI18N
        checkBoxAgreed.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                checkBoxAgreedItemStateChanged(evt);
            }
        });

        panelTitle.setBackground(new java.awt.Color(102, 102, 102));

        javax.swing.GroupLayout panelTitleLayout = new javax.swing.GroupLayout(panelTitle);
        panelTitle.setLayout(panelTitleLayout);
        panelTitleLayout.setHorizontalGroup(
            panelTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 580, Short.MAX_VALUE)
        );
        panelTitleLayout.setVerticalGroup(
            panelTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 56, Short.MAX_VALUE)
        );

        org.openide.awt.Mnemonics.setLocalizedText(jLabelName, org.openide.util.NbBundle.getMessage(AdsDocTopicEditor.class, "AdsDocTopicEditor.jLabelName.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabelLanguage, org.openide.util.NbBundle.getMessage(AdsDocTopicEditor.class, "AdsDocTopicEditor.jLabelLanguage.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(checkBoxCompleted, org.openide.util.NbBundle.getMessage(AdsDocTopicEditor.class, "AdsDocTopicEditor.checkBoxCompleted.text")); // NOI18N
        checkBoxCompleted.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                checkBoxCompletedItemStateChanged(evt);
            }
        });

        jPanelEditor.setLayout(new java.awt.BorderLayout());
        jPanelEditor.add(mmlEditor, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelEditor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelTitle, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelLanguage)
                            .addComponent(jLabelName))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(editorName)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(comboBoxLanguage, 0, 387, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(checkBoxAgreed)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(checkBoxCompleted)
                                .addGap(2, 2, 2)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(editorName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(checkBoxAgreed)
                        .addComponent(checkBoxCompleted))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(comboBoxLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabelLanguage)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelEditor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void editorNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_editorNameKeyReleased
        changeName(editorName, editorName.getText());
    }//GEN-LAST:event_editorNameKeyReleased

    private void comboBoxLanguageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxLanguageActionPerformed
        language = (EIsoLanguage) comboBoxLanguage.getSelectedItem();
        update();
    }//GEN-LAST:event_comboBoxLanguageActionPerformed

    private void checkBoxAgreedItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_checkBoxAgreedItemStateChanged
        body.setIsAgreed(checkBoxAgreed.isSelected());
    }//GEN-LAST:event_checkBoxAgreedItemStateChanged

    private void checkBoxCompletedItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_checkBoxCompletedItemStateChanged
        body.setIsCompleted(checkBoxCompleted.isSelected());
    }//GEN-LAST:event_checkBoxCompletedItemStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox checkBoxAgreed;
    private javax.swing.JCheckBox checkBoxCompleted;
    private javax.swing.JComboBox comboBoxLanguage;
    private javax.swing.JTextField editorName;
    private javax.swing.JLabel jLabelLanguage;
    private javax.swing.JLabel jLabelName;
    private javax.swing.JPanel jPanelEditor;
    private org.radixware.kernel.designer.common.editors.mml.MmlEditor mmlEditor;
    private javax.swing.JPanel panelTitle;
    // End of variables declaration//GEN-END:variables

}
