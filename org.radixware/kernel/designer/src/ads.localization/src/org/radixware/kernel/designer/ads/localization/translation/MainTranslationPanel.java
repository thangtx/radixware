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
 * TranslationPanel.java
 *
 * Created on Aug 12, 2009, 11:35:33 AM
 */
package org.radixware.kernel.designer.ads.localization.translation;

import java.awt.Rectangle;
import java.awt.event.MouseWheelListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import javax.swing.JScrollPane;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.designer.ads.localization.MultilingualEditor;
import org.radixware.kernel.designer.ads.localization.RowString;


public class MainTranslationPanel extends javax.swing.JPanel implements ITranslationPanel {

    private final TranslateArea translationArea;
    private final TranslateArea translationAreaSource;
    private List<EIsoLanguage> sourceLangs;
    private List<EIsoLanguage> translLangs;
    private final MultilingualEditor parent;

    /**
     * Creates new form TranslationPanel
     */
    public MainTranslationPanel(final MultilingualEditor parent) {
        this.parent = parent;
        initComponents();
        //mainSourceTextPanel.setBorder(new EmptyBorder(0, 20, 0, 0));
        //createSourcetextPanelUi();
        translationArea = new TranslateArea(this, translationPanel, true);
        translationAreaSource = new TranslateArea(this, mainSourceTextPanel, false);
        parent.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                switch (propertyName) {
                    case MultilingualEditor.GO_TO_PREV:
                        translationArea.goToPreviousTranslation(translationArea.getCurrentLangPanel());
                        break;
                    case MultilingualEditor.GO_TO_NEXT:
                        translationArea.goToNextTranslation(translationArea.getCurrentLangPanel());
                        break;
                    case MultilingualEditor.GO_TO_NEXT_UNCHECKED:
                        translationArea.goToNextUncheckedTranslation(translationArea.getCurrentLangPanel());
                        break;
                    case MultilingualEditor.GO_TO_PREV_UNCHECKED:
                        translationArea.goToPreviousUncheckedTranslation(translationArea.getCurrentLangPanel());
                        break;
                }
            }
        });
    }
    
    public void open(){
        this.sourceLangs = parent.getSourceLags();
        this.translLangs = parent.getTranslatedLags();
        translationArea.open(translLangs,sourceLangs);
        translationAreaSource.open(sourceLangs,sourceLangs);
    }
    
    public void update() {
        translationArea.update(null);
        translationAreaSource.update(null);
    }

    @Override
    public void scroll(final Rectangle rect) {
        parent.scroll(rect);
    }

    public void setMlString(final RowString rowString, final boolean setFocusOnTranslation, final int selectUncheckedTranslation) {
        if (rowString == null) {
            setReadOnly(true);
        } else {
            setReadOnly(false);
            translationArea.setRowString(rowString, setFocusOnTranslation, selectUncheckedTranslation);
            translationAreaSource.setRowString(rowString, false, selectUncheckedTranslation);
        }      
    }
    
    public void setReadOnly(final boolean readOnly) {
        translationArea.setReadOnly(readOnly);
        translationAreaSource.setReadOnly(readOnly);
    }

    @Override
    public void updateScrollPanel() {
    }

    public EIsoLanguage getCurTranslateLanguage() {
        return translationArea.getCurrentTranslateLang();
    }

    @Override
    public void setNextRowSting() {
        parent.setNextString();
    }

    @Override
    public void setPrevRowSting() {
        parent.setPrevString();
    }

    @Override
    public void setNextUncheckedRowSting() {
        parent.setNextUncheckedString();
    }

    @Override
    public void setPrevUncheckedRowSting() {
        parent.setPrevUncheckedString();
    }

    public void setTranslationFromPraseList(final String translation) {
        translationArea.setTranslationFromPraseList(translation);
    }

    @Override
    public void updatePhrasesPanel() {
        parent.updatePhrasesPanel();
    }

    @Override
    public void translationWasEdited(final EIsoLanguage lang) {
        parent.translationWasEdited();
    }

    @Override
    public void updateTargetLangsStatus(final RowString rowString) {
        translationArea.updateStatus(rowString);
    }

    @Override
    public void addPhraseToPrompt(final RowString rowString) {
        parent.createPrompt(rowString);
    }

    @Override
    public void removePhraseFromPrompt(final RowString rowString) {
        parent.removePrompt(rowString);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        translationPanel = new javax.swing.JPanel();
        mainSourceTextPanel = new javax.swing.JPanel();

        setBorder(null);
        setPreferredSize(new java.awt.Dimension(200, 230));

        jLabel3.setText(org.openide.util.NbBundle.getMessage(MainTranslationPanel.class, "MainTranslationPanel.jLabel3.text")); // NOI18N

        jLabel4.setText(org.openide.util.NbBundle.getMessage(MainTranslationPanel.class, "MainTranslationPanel.jLabel4.text")); // NOI18N

        translationPanel.setBorder(null);

        javax.swing.GroupLayout translationPanelLayout = new javax.swing.GroupLayout(translationPanel);
        translationPanel.setLayout(translationPanelLayout);
        translationPanelLayout.setHorizontalGroup(
            translationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 176, Short.MAX_VALUE)
        );
        translationPanelLayout.setVerticalGroup(
            translationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        mainSourceTextPanel.setBorder(null);
        mainSourceTextPanel.setPreferredSize(new java.awt.Dimension(162, 0));

        javax.swing.GroupLayout mainSourceTextPanelLayout = new javax.swing.GroupLayout(mainSourceTextPanel);
        mainSourceTextPanel.setLayout(mainSourceTextPanelLayout);
        mainSourceTextPanelLayout.setHorizontalGroup(
            mainSourceTextPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 176, Short.MAX_VALUE)
        );
        mainSourceTextPanelLayout.setVerticalGroup(
            mainSourceTextPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(mainSourceTextPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
                    .addComponent(translationPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mainSourceTextPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(translationPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(170, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel mainSourceTextPanel;
    private javax.swing.JPanel translationPanel;
    // End of variables declaration//GEN-END:variables

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public void checkWarnings(final RowString rowString) {
        if (rowString != null) {
            parent.checkWarnings(rowString);
        }
    }

    @Override
    public void save() {
        parent.save();
    }

    /*public List<EIsoLanguage> getSourceLands() {
     return sourceLangs;
     }

     public List<EIsoLanguage> getTranslationLangs() {
     return translLangs;
     }*/

    @Override
    public JScrollPane getTranslationPanelScrollPane() {
        return parent.getTranslationPanelScrollPane();
    }
    
}
