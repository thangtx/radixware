package org.radixware.kernel.designer.ads.editors.documentation;

import org.radixware.kernel.designer.common.editors.documentation.DocResourcesEditor;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;
import org.radixware.kernel.common.defs.ads.doc.DocResource;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;

public class DocumentationResourcePanel extends JPanel {

    private AdsModule module;
    private DocResourcesEditor editor;

    public DocumentationResourcePanel(AdsModule module) {

        this.module = module;
        initComponents();

        // lang
        List<EIsoLanguage> languages = module.getLayer().getLanguages();
        DefaultComboBoxModel languageModel = new DefaultComboBoxModel(languages.toArray());
        comboBoxLanguage.setModel(languageModel);
        
        EIsoLanguage mainDocLanguage = module.getLayer().getMainDocLanguage();
        comboBoxLanguage.setSelectedItem(mainDocLanguage);
        changeLanguage(mainDocLanguage);
    }

    public void open(OpenInfo openInfo) {
        DocResource res = (DocResource) openInfo.getTarget();
        editor.setSelectFile(res.getFile().getName());
    }

    private void changeLanguage(EIsoLanguage lang) {
        editorPanel.removeAll();
        editor = new DocResourcesEditor(module, lang);
        editorPanel.add(editor);
        update();
    }

    public void update() {
        //editor.update();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        editorPanel = new javax.swing.JPanel();
        LangPanel = new javax.swing.JPanel();
        jLabelLanguage = new javax.swing.JLabel();
        comboBoxLanguage = new javax.swing.JComboBox();

        editorPanel.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(jLabelLanguage, org.openide.util.NbBundle.getMessage(DocumentationResourcePanel.class, "DocumentationResourcePanel.jLabelLanguage.text")); // NOI18N

        comboBoxLanguage.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboBoxLanguage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxLanguageActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout LangPanelLayout = new javax.swing.GroupLayout(LangPanel);
        LangPanel.setLayout(LangPanelLayout);
        LangPanelLayout.setHorizontalGroup(
            LangPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LangPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelLanguage)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(comboBoxLanguage, 0, 372, Short.MAX_VALUE)
                .addContainerGap())
        );
        LangPanelLayout.setVerticalGroup(
            LangPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LangPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(LangPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelLanguage)
                    .addComponent(comboBoxLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(editorPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE)
                    .addComponent(LangPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(LangPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(editorPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void comboBoxLanguageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxLanguageActionPerformed
        EIsoLanguage lang = (EIsoLanguage) comboBoxLanguage.getSelectedItem();
        changeLanguage(lang);
    }//GEN-LAST:event_comboBoxLanguageActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel LangPanel;
    private javax.swing.JComboBox comboBoxLanguage;
    private javax.swing.JPanel editorPanel;
    private javax.swing.JLabel jLabelLanguage;
    // End of variables declaration//GEN-END:variables
}
