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

package org.radixware.kernel.designer.ads.localization.dialog;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.ads.AdsValAsStr;
import org.radixware.kernel.common.defs.localization.ILocalizingBundleDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.EMultilingualStringKind;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.designer.ads.localization.source.AuthorFilter;
import org.radixware.kernel.designer.ads.localization.source.FilterSettings;
import org.radixware.kernel.designer.ads.localization.source.FilterUtils;
import org.radixware.kernel.designer.ads.localization.source.TimeFilter;
import org.radixware.kernel.designer.common.dialogs.components.state.StateAbstractDialog;
import org.radixware.kernel.designer.common.dialogs.components.values.ValueChangeEvent;
import org.radixware.kernel.designer.common.dialogs.components.values.ValueChangeListener;


public class FiltersDialog extends StateAbstractDialog.StateAbstractPanel {

    private FilterSettings filterSettings;
    private final ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                check();
            }
    };
    private final ValueChangeListener<AdsValAsStr> timeValListener = new ValueChangeListener<AdsValAsStr>() {
        @Override
        public void valueChanged(final ValueChangeEvent event) {
            check();
        }
    }; 

    /**
     * Creates new form TimeFilterDialog
     */
    public FiltersDialog() {
        initComponents();
    }
    
    public void open(FilterSettings timeFilter, final List<EIsoLanguage> srcLangs, final List<EIsoLanguage> transLangs, final Map<Layer, List<Module>> selectedLayers){
        this.filterSettings = timeFilter;
        createUi(srcLangs, transLangs);
        stringTypePanel.setMinimumSize(showModePanel.getMinimumSize());
        layerPanel.open(selectedLayers);
    }
    
    private void initAutorCombobox(JComboBox comboBox, AuthorFilter author){
        if (comboBox == null || author == null) return;
        
        comboBox.addItem(FilterUtils.NOT_DEFINED);
        for (String name : ILocalizingBundleDef.authors.getAuthors()){
            comboBox.addItem(name);
        }
        
        if (author.isEmpty()){
            comboBox.setSelectedIndex(0);
        } else {
            comboBox.setSelectedItem(author.getName()); 
        }
        comboBox.addActionListener(actionListener);
    }
    
    private void initTimePanel(TimeFilterPanel timeFilterPanel, TimeFilter timeFilter){
        if (timeFilterPanel == null || timeFilter == null) return;
        
        timeFilterPanel.open(timeFilter.getTimeFrom(), timeFilter.getTimeTo());
        timeFilterPanel.addTimeFromValueChangeListener(timeValListener);
        timeFilterPanel.addTimeToValueChangeListener(timeValListener);
    }

    private void createUi(final List<EIsoLanguage> srcLangs,final List<EIsoLanguage> transLangs) {
        initAutorCombobox(createdByComboBox,filterSettings.getCreateBy());
        initAutorCombobox(modifiedAuthorComboBox,filterSettings.getModifiedBy());
        
        initTimePanel(creationTimePanel, filterSettings.getCreationTimeFilter());
        initTimePanel(modifiedTimePanel, filterSettings.getModifiedTimeFilter());
        modifiedLanguagePanel.open(filterSettings.getModifiedTimeFilter(), srcLangs, transLangs);
        
        AuthorFilter authorFilter = filterSettings.getModifiedBy();
        modifiedAuthorLanguagePanel.open(authorFilter, srcLangs, transLangs);

        initTimePanel(changeStatusTimePanel, filterSettings.getStatusChangeTimeFilter());
        changeStatusPanelLanguagePanel.open(filterSettings.getStatusChangeTimeFilter(), srcLangs, transLangs);
        
        showPublished.setSelected(filterSettings.isShowOnlyPulished());
        showEmpty.setSelected(filterSettings.isShowEmpty());
        showNotEmpty.setSelected(filterSettings.isShowNotEmpty());
        showComment.setSelected(filterSettings.isShowCommentedSrc());
        showOnlyDifferentVersions.setSelected(filterSettings.isShowOnlyDifferentVersions());
        
        stringTypePanel.open(filterSettings.getStringTypes());
        notCheckedSrc.setSelected(filterSettings.isShowNotChecked());
        
        EIsoLanguage language = filterSettings.getNotTranslatedOn();
        ShowModeComboboxItem selectedItem = null;
        cbTranslilter.addItem("All multilanguage strings");
        if (transLangs != null){
            for (EIsoLanguage lang : transLangs) {
                ShowModeComboboxItem currentItem = new ShowModeComboboxItem(lang);
                cbTranslilter.addItem(currentItem);
                if (lang == language){
                    selectedItem = currentItem;
                }
            }
            
            if (selectedItem != null){
                cbTranslilter.setSelectedItem(selectedItem);
            }
            
            if (transLangs.size() > 1) {
                cbTranslilter.addItem("Not checked in all languages");
                if (filterSettings.isNotTranslatedOnAll()){
                   cbTranslilter.setSelectedIndex(cbTranslilter.getItemCount() - 1);
                }
            }
        }
        
        
    }

    public FilterSettings getNewFilterSettings() {
        if (filterSettings == null) {
            return null;
        }
        
        changeTimeFilter(filterSettings.getCreationTimeFilter(), creationTimePanel, null);
        changeAuthorFilter(filterSettings.getCreateBy(), createdByComboBox.getSelectedItem(), null);

        changeTimeFilter(filterSettings.getModifiedTimeFilter(), modifiedTimePanel, modifiedLanguagePanel);
        changeAuthorFilter(filterSettings.getModifiedBy(), modifiedAuthorComboBox.getSelectedItem(), modifiedAuthorLanguagePanel);

        changeTimeFilter(filterSettings.getStatusChangeTimeFilter(), changeStatusTimePanel, changeStatusPanelLanguagePanel);
        
        filterSettings.setShowOnlyPulished(showPublished.isSelected());
        filterSettings.setShowEmpty(showEmpty.isSelected());
        filterSettings.setShowNotEmpty(showNotEmpty.isSelected());
        filterSettings.setShowCommentedSrc(showComment.isSelected());
        
        filterSettings.setStringTypes(stringTypePanel.getTypes());
        
        filterSettings.setShowNotChecked(notCheckedSrc.isSelected());
        Object selectedItem = cbTranslilter.getSelectedItem();
        if (selectedItem instanceof ShowModeComboboxItem){
            filterSettings.setNotTranslatedOnAll(false);
            filterSettings.setNotTranslatedOn(((ShowModeComboboxItem)selectedItem).getLanguage());
        } else {
            filterSettings.setNotTranslatedOnAll(cbTranslilter.getSelectedIndex()!=0);
            filterSettings.setNotTranslatedOn(null);
        }
        
        filterSettings.setShowOnlyUnversion(showOnlyDifferentVersions.isSelected());
        
        return filterSettings;
    }
    
    public Map<Layer, List<Module>> getSelectedLayers() {
        return layerPanel.getSelectedLayers();
    }

    private void changeTimeFilter(TimeFilter filter, TimeFilterPanel timeFilterPanel, LanguagePanel languagePanel) {
        if (timeFilterPanel != null) {
            filter.setTimeFrom(timeFilterPanel.getTimeFrom());
            filter.setTimeTo(timeFilterPanel.getTimeTo());
        }

        if (languagePanel != null) {
            List<EIsoLanguage> langs = languagePanel.getLangs();
            boolean langSelected = languagePanel.isLanguageSelected();
            filter.setLanguageSelected(langSelected);
            if (!langSelected) {
                filter.setGrupLangs(languagePanel.getGroupLangs());
            }
            if (!langs.isEmpty()) {
                filter.setLanguage(langs);
            }
        }
    }
    
    private void changeAuthorFilter(AuthorFilter authorFilter, Object selectedName, LanguagePanel languagePanel) {
        if (selectedName == null || selectedName.equals(FilterUtils.NOT_DEFINED)) {
            authorFilter.setName(null);
        } else {
            authorFilter.setName(selectedName.toString());
        }
        if (languagePanel != null) {
            List<EIsoLanguage> langs = languagePanel.getLangs();
            boolean langSelected = languagePanel.isLanguageSelected();
            authorFilter.setLanguageSelected(langSelected);
            if (!langSelected) {
                authorFilter.setGrupLangs(languagePanel.getGroupLangs());
            }
            if (!langs.isEmpty()) {
                authorFilter.setLanguage(langs);
            }
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        creationPanel = new javax.swing.JPanel();
        creationTimePanel = new org.radixware.kernel.designer.ads.localization.dialog.TimeFilterPanel();
        createdByComboBox = new javax.swing.JComboBox();
        CreatedByLabel = new javax.swing.JLabel();
        updatedPanel = new javax.swing.JPanel();
        modifiedTimePanel = new org.radixware.kernel.designer.ads.localization.dialog.TimeFilterPanel();
        modifiedLanguagePanel = new org.radixware.kernel.designer.ads.localization.dialog.LanguagePanel();
        lastUpdatedAuthorPanel = new javax.swing.JPanel();
        modifiedAuthorComboBox = new javax.swing.JComboBox();
        modifiedAuthorLanguagePanel = new org.radixware.kernel.designer.ads.localization.dialog.LanguagePanel();
        additionalFiltresPanel = new javax.swing.JPanel();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 5), new java.awt.Dimension(0, 5), new java.awt.Dimension(32767, 5));
        jPanel1 = new javax.swing.JPanel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 32767));
        layerPanel = new org.radixware.kernel.designer.ads.localization.dialog.LayerPanel();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 32767));
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 10), new java.awt.Dimension(0, 10), new java.awt.Dimension(32767, 10));
        chekboxPanel = new javax.swing.JPanel();
        showPublished = new javax.swing.JCheckBox();
        showEmpty = new javax.swing.JCheckBox();
        showComment = new javax.swing.JCheckBox();
        showOnlyDifferentVersions = new javax.swing.JCheckBox();
        showNotEmpty = new javax.swing.JCheckBox();
        changeStatusPanel = new javax.swing.JPanel();
        changeStatusTimePanel = new org.radixware.kernel.designer.ads.localization.dialog.TimeFilterPanel();
        changeStatusPanelLanguagePanel = new org.radixware.kernel.designer.ads.localization.dialog.LanguagePanel();
        stringTypePanel = new org.radixware.kernel.designer.ads.localization.dialog.StringTypePanel();
        showModePanel = new javax.swing.JPanel();
        notCheckedSrc = new javax.swing.JCheckBox();
        cbTranslilter = new javax.swing.JComboBox();

        setLayout(new java.awt.GridBagLayout());

        creationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(FiltersDialog.class, "FiltersDialog.creationPanel.border.title"))); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(CreatedByLabel, org.openide.util.NbBundle.getMessage(FiltersDialog.class, "FiltersDialog.CreatedByLabel.text")); // NOI18N

        javax.swing.GroupLayout creationPanelLayout = new javax.swing.GroupLayout(creationPanel);
        creationPanel.setLayout(creationPanelLayout);
        creationPanelLayout.setHorizontalGroup(
            creationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(creationPanelLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(creationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(creationTimePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
                    .addGroup(creationPanelLayout.createSequentialGroup()
                        .addComponent(CreatedByLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(createdByComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(5, 5, 5))))
        );
        creationPanelLayout.setVerticalGroup(
            creationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(creationPanelLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(creationTimePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addGroup(creationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(createdByComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CreatedByLabel))
                .addGap(5, 5, 5))
        );

        CreatedByLabel.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(FiltersDialog.class, "FiltersDialog.CreatedByLabel.AccessibleContext.accessibleName")); // NOI18N

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        add(creationPanel, gridBagConstraints);

        updatedPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(FiltersDialog.class, "FiltersDialog.updatedPanel.border.title"))); // NOI18N

        javax.swing.GroupLayout updatedPanelLayout = new javax.swing.GroupLayout(updatedPanel);
        updatedPanel.setLayout(updatedPanelLayout);
        updatedPanelLayout.setHorizontalGroup(
            updatedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(modifiedLanguagePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 518, Short.MAX_VALUE)
            .addComponent(modifiedTimePanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        updatedPanelLayout.setVerticalGroup(
            updatedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(updatedPanelLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(modifiedTimePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(modifiedLanguagePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        add(updatedPanel, gridBagConstraints);

        lastUpdatedAuthorPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(FiltersDialog.class, "FiltersDialog.lastUpdatedAuthorPanel.border.title"))); // NOI18N

        javax.swing.GroupLayout lastUpdatedAuthorPanelLayout = new javax.swing.GroupLayout(lastUpdatedAuthorPanel);
        lastUpdatedAuthorPanel.setLayout(lastUpdatedAuthorPanelLayout);
        lastUpdatedAuthorPanelLayout.setHorizontalGroup(
            lastUpdatedAuthorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lastUpdatedAuthorPanelLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(modifiedAuthorComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(5, 5, 5))
            .addComponent(modifiedAuthorLanguagePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 518, Short.MAX_VALUE)
        );
        lastUpdatedAuthorPanelLayout.setVerticalGroup(
            lastUpdatedAuthorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lastUpdatedAuthorPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(modifiedAuthorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                .addComponent(modifiedAuthorLanguagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        add(lastUpdatedAuthorPanel, gridBagConstraints);

        additionalFiltresPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(FiltersDialog.class, "FiltersDialog.additionalFiltresPanel.border.title"))); // NOI18N
        additionalFiltresPanel.setLayout(new javax.swing.BoxLayout(additionalFiltresPanel, javax.swing.BoxLayout.PAGE_AXIS));
        additionalFiltresPanel.add(filler3);

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));
        jPanel1.add(filler1);
        jPanel1.add(layerPanel);
        jPanel1.add(filler2);

        additionalFiltresPanel.add(jPanel1);
        additionalFiltresPanel.add(filler4);

        chekboxPanel.setLayout(new java.awt.GridLayout(2, 3));

        org.openide.awt.Mnemonics.setLocalizedText(showPublished, org.openide.util.NbBundle.getMessage(FiltersDialog.class, "FiltersDialog.showPublished.text")); // NOI18N
        showPublished.setActionCommand(org.openide.util.NbBundle.getMessage(FiltersDialog.class, "FiltersDialog.showPublished.actionCommand")); // NOI18N
        showPublished.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chekboxPanel.add(showPublished);

        org.openide.awt.Mnemonics.setLocalizedText(showEmpty, org.openide.util.NbBundle.getMessage(FiltersDialog.class, "FiltersDialog.showEmpty.text")); // NOI18N
        chekboxPanel.add(showEmpty);

        org.openide.awt.Mnemonics.setLocalizedText(showComment, org.openide.util.NbBundle.getMessage(FiltersDialog.class, "FiltersDialog.showComment.text")); // NOI18N
        chekboxPanel.add(showComment);

        org.openide.awt.Mnemonics.setLocalizedText(showOnlyDifferentVersions, org.openide.util.NbBundle.getMessage(FiltersDialog.class, "FiltersDialog.showOnlyDifferentVersions.text_1")); // NOI18N
        chekboxPanel.add(showOnlyDifferentVersions);

        org.openide.awt.Mnemonics.setLocalizedText(showNotEmpty, org.openide.util.NbBundle.getMessage(FiltersDialog.class, "FiltersDialog.showNotEmpty.text")); // NOI18N
        chekboxPanel.add(showNotEmpty);

        additionalFiltresPanel.add(chekboxPanel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        add(additionalFiltresPanel, gridBagConstraints);

        changeStatusPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(FiltersDialog.class, "FiltersDialog.changeStatusPanel.border.title"))); // NOI18N

        javax.swing.GroupLayout changeStatusPanelLayout = new javax.swing.GroupLayout(changeStatusPanel);
        changeStatusPanel.setLayout(changeStatusPanelLayout);
        changeStatusPanelLayout.setHorizontalGroup(
            changeStatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(changeStatusPanelLanguagePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE)
            .addComponent(changeStatusTimePanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        changeStatusPanelLayout.setVerticalGroup(
            changeStatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(changeStatusPanelLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(changeStatusTimePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(changeStatusPanelLanguagePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        add(changeStatusPanel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(stringTypePanel, gridBagConstraints);

        showModePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(FiltersDialog.class, "FiltersDialog.showModePanel.border.title"))); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(notCheckedSrc, org.openide.util.NbBundle.getMessage(FiltersDialog.class, "FiltersDialog.notCheckedSrc.text")); // NOI18N
        notCheckedSrc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                notCheckedSrcActionPerformed(evt);
            }
        });

        cbTranslilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbTranslilterActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout showModePanelLayout = new javax.swing.GroupLayout(showModePanel);
        showModePanel.setLayout(showModePanelLayout);
        showModePanelLayout.setHorizontalGroup(
            showModePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(showModePanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(showModePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(showModePanelLayout.createSequentialGroup()
                        .addComponent(notCheckedSrc)
                        .addGap(0, 299, Short.MAX_VALUE))
                    .addComponent(cbTranslilter, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        showModePanelLayout.setVerticalGroup(
            showModePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(showModePanelLayout.createSequentialGroup()
                .addComponent(notCheckedSrc)
                .addGap(10, 10, 10)
                .addComponent(cbTranslilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(69, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        add(showModePanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void notCheckedSrcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_notCheckedSrcActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_notCheckedSrcActionPerformed

    private void cbTranslilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbTranslilterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbTranslilterActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel CreatedByLabel;
    private javax.swing.JPanel additionalFiltresPanel;
    private javax.swing.JComboBox cbTranslilter;
    private javax.swing.JPanel changeStatusPanel;
    private org.radixware.kernel.designer.ads.localization.dialog.LanguagePanel changeStatusPanelLanguagePanel;
    private org.radixware.kernel.designer.ads.localization.dialog.TimeFilterPanel changeStatusTimePanel;
    private javax.swing.JPanel chekboxPanel;
    private javax.swing.JComboBox createdByComboBox;
    private javax.swing.JPanel creationPanel;
    private org.radixware.kernel.designer.ads.localization.dialog.TimeFilterPanel creationTimePanel;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel lastUpdatedAuthorPanel;
    private org.radixware.kernel.designer.ads.localization.dialog.LayerPanel layerPanel;
    private javax.swing.JComboBox modifiedAuthorComboBox;
    private org.radixware.kernel.designer.ads.localization.dialog.LanguagePanel modifiedAuthorLanguagePanel;
    private org.radixware.kernel.designer.ads.localization.dialog.LanguagePanel modifiedLanguagePanel;
    private org.radixware.kernel.designer.ads.localization.dialog.TimeFilterPanel modifiedTimePanel;
    private javax.swing.JCheckBox notCheckedSrc;
    private javax.swing.JCheckBox showComment;
    private javax.swing.JCheckBox showEmpty;
    private javax.swing.JPanel showModePanel;
    private javax.swing.JCheckBox showNotEmpty;
    private javax.swing.JCheckBox showOnlyDifferentVersions;
    private javax.swing.JCheckBox showPublished;
    private org.radixware.kernel.designer.ads.localization.dialog.StringTypePanel stringTypePanel;
    private javax.swing.JPanel updatedPanel;
    // End of variables declaration//GEN-END:variables

    @Override
    public void check() {
        modifiedLanguagePanel.setEnabledLanguagePanel(!modifiedTimePanel.isEmpty());
        modifiedAuthorLanguagePanel.setEnabledLanguagePanel(!modifiedAuthorComboBox.getSelectedItem().equals(FilterUtils.NOT_DEFINED));
        changeStatusPanelLanguagePanel.setEnabledLanguagePanel(!changeStatusTimePanel.isEmpty());
        
        if (!modifiedTimePanel.isFilerValid()) {
            stateManager.error(NbBundle.getMessage(FiltersDialog.class, "WRONG_MODIFIED_TIME_FILTER"));
        } else if (!creationTimePanel.isFilerValid()) {
            stateManager.error(NbBundle.getMessage(FiltersDialog.class, "WRONG_CREATION_TIME_FILTER"));
        } if (!changeStatusTimePanel.isFilerValid()){
            stateManager.error(NbBundle.getMessage(FiltersDialog.class, "WRONG_STATUS_TIME_FILTER"));
        }else {
            stateManager.ok();
        }
        changeSupport.fireChange();
    }
    
    private class ShowModeComboboxItem{
        private final EIsoLanguage language;

        public ShowModeComboboxItem(EIsoLanguage language) {
            this.language = language;
        }

        @Override
        public String toString() {
            return "Not checked in " + (language != null ? language.getName() : "");
        }

        public EIsoLanguage getLanguage() {
            return language;
        }
    }
    

}
