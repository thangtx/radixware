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

import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.event.MouseWheelListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import javax.swing.JScrollPane;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.designer.ads.localization.MultilingualEditor;
import org.radixware.kernel.designer.ads.localization.MultilingualEditorUtils;
import org.radixware.kernel.designer.ads.localization.MultilingualEditorUtils.SelectionInfo;
import org.radixware.kernel.designer.ads.localization.RowString;


public class MainTranslationPanel extends javax.swing.JPanel implements ITranslationPanel {

    private final TranslateArea translationArea;
    private final TranslateArea translationAreaSource;
    private List<EIsoLanguage> sourceLanguages;
    private List<EIsoLanguage> translatelLanguages;
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
        parent.addChangeListener(listener);
    }
    
    public void open(){
        this.sourceLanguages = parent.getSourceLags();
        this.translatelLanguages = parent.getTranslatedLags();
        translationArea.open(translatelLanguages,sourceLanguages);
        translationAreaSource.open(sourceLanguages,sourceLanguages);
    }
    
    public void update() {
        translationArea.update(null);
        translationAreaSource.update(null);
    }

    @Override
    public void scroll(final Rectangle rect) {
        parent.scroll(rect);
    }

    public void setMlString(final RowString rowString) {
        if (rowString == null) {
            setReadOnly(true);
        } else {
            setReadOnly(false);
            translationArea.setRowString(rowString);
            translationAreaSource.setRowString(rowString);
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
        parent.fireChange(MultilingualEditorUtils.GO_TO_NEXT_ROW);
    }

    @Override
    public void setPrevRowSting() {
        parent.fireChange(MultilingualEditorUtils.GO_TO_PREVIOUS_ROW);
    }

    @Override
    public void setNextUncheckedRowSting() {
        parent.fireChange(MultilingualEditorUtils.GO_TO_NEXT_UNCHECKED_ROW);
    }

    @Override
    public void setPrevUncheckedRowSting() {
        parent.fireChange(MultilingualEditorUtils.GO_TO_PREVIOUS_UNCHECKED_ROW);
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

    @Override
    public void fireChange(String key) {
        parent.fireChange(key);
    }

    private PropertyChangeListener listener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String propertyName = evt.getPropertyName();
            switch (propertyName) {
                case MultilingualEditorUtils.GO_TO_PREV:
                    if (translationArea.getCurrentLangPanel().isFocusOwner()) {
                        if (!translationArea.goToPreviousTranslation()) {
                            translationAreaSource.selectLastPanel();
                        }
                    } else if (translationAreaSource.getCurrentLangPanel().isFocusOwner()) {
                        if (!translationAreaSource.goToPreviousTranslation()) {
                            setPrevRowSting();
                        }
                    } else {
                        translationArea.selectLastPanel();
                    }
                    break;
                case MultilingualEditorUtils.GO_TO_NEXT:
                    if (translationAreaSource.getCurrentLangPanel().isFocusOwner()) {
                        if (!translationAreaSource.goToNextTranslation()) {
                            translationArea.selectFirstPanel();
                        }
                    } else if (translationArea.getCurrentLangPanel().isFocusOwner()) {
                        if (!translationArea.goToNextTranslation()) {
                            setNextRowSting();
                        }
                    } else {
                        translationAreaSource.selectFirstPanel();
                    }
                    break;
                case MultilingualEditorUtils.GO_TO_NEXT_UNCHECKED:
                    if (translationAreaSource.getCurrentLangPanel().isFocusOwner()) {
                        if (!translationAreaSource.goToNextUncheckedTranslation()) {
                            if (!translationArea.selectFirstUncheckedPanel()){
                                setNextUncheckedRowSting();
                            }
                        }
                    } else if (translationArea.getCurrentLangPanel().isFocusOwner()) {
                        if (!translationArea.goToNextUncheckedTranslation()) {
                            setNextUncheckedRowSting();
                        }
                    } else {
                        translationAreaSource.selectFirstUncheckedPanel();
                    }
                    break;
                case MultilingualEditorUtils.GO_TO_PREV_UNCHECKED:
                    if (translationArea.getCurrentLangPanel().isFocusOwner()) {
                        if (!translationArea.goToPreviousUncheckedTranslation()) {
                            if (!translationAreaSource.selectLastUncheckedPanel()){
                                setPrevUncheckedRowSting();
                            }
                        }
                    } else if (translationAreaSource.getCurrentLangPanel().isFocusOwner()) {
                        if (!translationAreaSource.goToPreviousUncheckedTranslation()) {
                            setPrevUncheckedRowSting();
                        }
                    } else {
                        translationArea.selectLastUncheckedPanel();
                    }
                    break;
                case MultilingualEditorUtils.GO_TO_PREV_EDITABLE:
                    if (translationArea.getCurrentLangPanel().isFocusOwner()) {
                        if (!translationArea.goToPreviousEditableTranslation()) {
                            if (!translationAreaSource.selectLastEditablePanel()){
                                parent.fireChange(MultilingualEditorUtils.GO_TO_PREV_EDITABLE_ROW);
                            }
                        }
                    } else if (translationAreaSource.getCurrentLangPanel().isFocusOwner()) {
                        if (!translationAreaSource.goToPreviousEditableTranslation()) {
                            parent.fireChange(MultilingualEditorUtils.GO_TO_PREV_EDITABLE_ROW);
                        }
                    } else {
                        translationArea.selectLastEditablePanel();
                    }
                    break;
                case MultilingualEditorUtils.GO_TO_NEXT_EDITABLE:        
                     if (translationAreaSource.getCurrentLangPanel().isFocusOwner()) {
                        if (!translationAreaSource.goToNextEditableTranslation()) {
                            if (!translationArea.selectFirstEditablePanel()){
                                parent.fireChange(MultilingualEditorUtils.GO_TO_NEXT_EDITABLE_ROW);
                            }
                        }
                    } else if (translationArea.getCurrentLangPanel().isFocusOwner()) {
                        if (!translationArea.goToNextEditableTranslation()) {
                            parent.fireChange(MultilingualEditorUtils.GO_TO_NEXT_EDITABLE_ROW);
                        }
                    } else {
                        translationAreaSource.selectFirstEditablePanel();
                    }
                    break;   
                    
                case MultilingualEditorUtils.CHECK_ALL_AND_GO:
                    if (translationAreaSource.checkAll()){
                        if (translationArea.checkAll()){
                            setNextRowSting();
                        } else {
                            translationArea.selectFirstUncheckedPanel();
                        }
                    } else {
                        translationAreaSource.selectFirstUncheckedPanel();
                    }
                    break;
                    
                    
                //FOCUS    
                case MultilingualEditorUtils.FOCUS_TEXT:
                    translationAreaSource.setFocus();
                    break;    
                case MultilingualEditorUtils.SELECT_LAST_TEXT:
                    translationArea.selectLastPanel();
                    break;
                case MultilingualEditorUtils.SELECT_LAST_UNCHECKED_TEXT:
                    if (!translationArea.selectLastUncheckedPanel()){
                        if(!translationAreaSource.selectLastUncheckedPanel()){
                            translationAreaSource.setFocus();
                        }
                    }
                    break;
                case MultilingualEditorUtils.SELECT_FIRST_UNCHECKED_TEXT:
                    if (!translationAreaSource.selectFirstUncheckedPanel()){
                        if(!translationArea.selectFirstUncheckedPanel()){
                            translationArea.setFocus();
                        }
                    }
                    break;
                case MultilingualEditorUtils.SELECT_LAST_EDITABLE_TEXT:
                    if (!translationArea.selectLastEditablePanel()){
                        if(!translationAreaSource.selectLastEditablePanel()){
                            translationAreaSource.setFocus();
                        }
                    }
                    break;
                case MultilingualEditorUtils.SELECT_FIRST_EDITABLE_TEXT:
                    if (!translationAreaSource.selectLastEditablePanel()){
                        if(!translationArea.selectLastEditablePanel()){
                            translationArea.setFocus();
                        }
                    }
                    break;    
            }
        }
    };
    
}
