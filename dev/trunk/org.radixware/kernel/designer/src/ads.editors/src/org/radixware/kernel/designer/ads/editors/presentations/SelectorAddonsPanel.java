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
 * SelectorAddonsPanel.java
 *
 * Created on 26.06.2009, 14:19:43
 */
package org.radixware.kernel.designer.ads.editors.presentations;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsFilterDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EntityObjectPresentations;
import org.radixware.kernel.common.defs.ads.clazz.presentation.SelectorAddons;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ConfigureDefinitionListCfg;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ConfigureDefinitionTable;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ConfigureDefinitionTable.DefBooleanValueChangeEvent;

public class SelectorAddonsPanel extends javax.swing.JPanel {

    private final ConfigureDefinitionTable.DefBooleanValueChangeListener enabledFiltersListener = new ConfigureDefinitionTable.DefBooleanValueChangeListener() {

        @Override
        public void onEvent(DefBooleanValueChangeEvent e) {
            SelectorAddonsPanel.this.onEnabledFiltersChange(e);
        }

    };
    private final ConfigureDefinitionTable.IdBooleanValueChangeListener enabledFilterIdsListener = new ConfigureDefinitionTable.IdBooleanValueChangeListener() {

        @Override
        public void onEvent(ConfigureDefinitionTable.IdBooleanValueChangeEvent e) {
            SelectorAddonsPanel.this.onEnabledFiltersChange(e);
        }

    };

    /**
     * Creates new form SelectorAddonsPanel
     */
    public SelectorAddonsPanel() {
        initComponents();

        ActionListener obligatoryListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                SelectorAddonsPanel.this.onFilterIsObligatoryChange();
            }

        };
        filterObligatoryCheck.addActionListener(obligatoryListener);

        ActionListener anyFilterListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                SelectorAddonsPanel.this.onAnyFilterEnabledChange();
            }

        };
        anyFilterButton.addActionListener(anyFilterListener);
        specFilterButton.addActionListener(anyFilterListener);
        baseFiltersTable.addConfigurableDefinitionIsCheckedListener(enabledFiltersListener);
        baseFiltersTable.addConfigurableIdIsCheckedListener(enabledFilterIdsListener);
        

        ActionListener customFiltersListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                SelectorAddonsPanel.this.onCustomFiltersUsageChange();
            }

        };
        customFiltersCheck.addActionListener(customFiltersListener);

        ChangeListener defaultFilterListener = new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                SelectorAddonsPanel.this.onDefaultFilterChange();
            }

        };
        defaultFilterEditor.addChangeListener(defaultFilterListener);
        defaultFilterEditor.setComboMode();

        ActionListener inheritanceListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!SelectorAddonsPanel.this.isUpdate) {
                    boolean isSelected = SelectorAddonsPanel.this.inheritCheck.isSelected();
                    SelectorAddonsPanel.this.presentation.setAddonsInherited(isSelected);
                    SelectorAddonsPanel.this.update();
                }
            }

        };
        inheritCheck.addActionListener(inheritanceListener);
        defaultForeground = inheritCheck.getForeground();

    }

    @Override
    public String getName() {
        return NbBundle.getMessage(SelectorAddonsPanel.class, "SelectorTabs-Addons");
    }

    private Color defaultForeground;
    private boolean isUpdate = false;
    private boolean readonly = false;
    private AdsSelectorPresentationDef presentation;

    public void open(final AdsSelectorPresentationDef presentation) {
        this.presentation = presentation;
        filterAbsencePanel.open(presentation);
        update();
    }

    public void update() {
        isUpdate = true;
        SelectorAddons addons = presentation.getAddons();
        boolean inherited = presentation.isAddonsInherited();
        this.readonly = presentation.isReadOnly();
        inheritCheck.setSelected(presentation.isAddonsInherited());
        boolean erronious = inherited
                && (presentation.getBasePresentationId() == null
                && presentation.getHierarchy().findOverwritten() == null);
        if (erronious) {
            inheritCheck.setForeground(Color.RED);
        } else {
            inheritCheck.setForeground(defaultForeground);
        }
        if (inherited) {
            inheritCheck.setEnabled(!readonly);
        } else {
            inheritCheck.setEnabled(!readonly && !erronious);
        }
        //inheritCheck.setEnabled(!readonly && presentation.getBasePresentationId() != null);
        boolean commonEnable = !inherited && !readonly;
        //update values for editors
        EntityObjectPresentations presentations = presentation.getOwnerClass().getPresentations();
        defaultFilterEditor.setComboBoxValues(presentations.getFilters().get(EScope.ALL), true);

        filterObligatoryCheck.setSelected(addons.isFilterObligatory());
        filterObligatoryCheck.setEnabled(commonEnable);

        filterAbsencePanel.update();
        filterAbsencePanel.setEnabled(!addons.isFilterObligatory() && !presentation.isAddonsInherited());

        customFiltersCheck.setSelected(addons.isCustomFilterEnabled());
        customFiltersCheck.setEnabled(commonEnable);

        defaultFilterEditor.open(addons.findDefaultFilter(), addons.getDefaultFilterId());
        defaultFilterEditor.setEnabled(commonEnable);
        defaultFilterLabel.setEnabled(commonEnable);
        boolean anyFilter = addons.isAnyBaseFilterEnabled();
        anyFilterButton.setSelected(anyFilter);
        anyFilterButton.setEnabled(commonEnable);
        specFilterButton.setSelected(!anyFilter);
        specFilterButton.setEnabled(commonEnable);
        List<Id> filtersIds = new ArrayList<Id>();
        List<AdsFilterDef> filtersList = presentations.getFilters().get(EScope.ALL);
        for (AdsFilterDef f : filtersList) {
            filtersIds.add(f.getId());
        }
        ConfigureDefinitionListCfg filtersCfg = ConfigureDefinitionListCfg.Factory.newInstanceWithEnabledIds(filtersList, filtersIds);
        baseFiltersTable.open(addons.getEnabledFilterIds(), filtersCfg);
        baseFiltersTable.setEnabled(commonEnable && !anyFilter);

        hintEditor.open(addons.getDefaultHint());
        hintEditor.setEnabled(commonEnable);
        isUpdate = false;
    }

    private void onDefaultColoreSchemeChange() {
//        if (!isUpdate && presentation != null){
//            presentation.getAddons().setDefaultColorSchemeId(defaultColorSchemeEditor.getDefinitionId());
//        }
    }

    private void onDefaultFilterChange() {
        if (!isUpdate && presentation != null) {
            presentation.getAddons().setDefaultFilterId(defaultFilterEditor.getDefinitionId());
        }
    }

    private void onFilterIsObligatoryChange() {
        if (!isUpdate) {
            boolean isSelected = filterObligatoryCheck.isSelected();
            presentation.getAddons().setFilterIsObligatory(isSelected);
            filterAbsencePanel.setEnabled(!isSelected && !readonly);
        }
    }

    private void onAnyFilterEnabledChange() {
        if (!isUpdate) {
            boolean anySelected = anyFilterButton.isSelected();
            presentation.getAddons().setAnyBaseFilterEnabled(anySelected);
            baseFiltersTable.setEnabled(!anySelected && !readonly);
        }
    }

    private void onEnabledFiltersChange(DefBooleanValueChangeEvent e) {
        if (!isUpdate) {
            SelectorAddons addons = presentation.getAddons();
            boolean isSelected = e.getNewValue();
            AdsFilterDef modifiedFilter = (AdsFilterDef) e.getRadixObject();
            if (isSelected) {
                addons.addEnabledFilterId(modifiedFilter.getId());
            } else {
                addons.removeEnabledFilterId(modifiedFilter.getId());
            }
        }
    }

    private void onEnabledFiltersChange(ConfigureDefinitionTable.IdBooleanValueChangeEvent e) {
        if (!isUpdate) {
            SelectorAddons addons = presentation.getAddons();
            boolean isSelected = e.getNewValue();
            Id modifiedFilterId = e.getId();
            if (isSelected) {
                addons.addEnabledFilterId(modifiedFilterId);
            } else {
                addons.removeEnabledFilterId(modifiedFilterId);
            }
        }
    }

    private void onCustomFiltersUsageChange() {
        if (!isUpdate) {
            boolean isSelected = customFiltersCheck.isSelected();
            presentation.getAddons().setCustomFilterEnabled(isSelected);
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

        baseFiltersGroup = new javax.swing.ButtonGroup();
        defaultFilterLabel = new javax.swing.JLabel();
        defaultFilterEditor = new org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel();
        baseFiltersPanel = new javax.swing.JPanel();
        anyFilterButton = new javax.swing.JRadioButton();
        specFilterButton = new javax.swing.JRadioButton();
        baseFiltersTable = new org.radixware.kernel.designer.common.dialogs.chooseobject.ConfigureDefinitionListPanel();
        filterObligatoryCheck = new javax.swing.JCheckBox();
        customFiltersCheck = new javax.swing.JCheckBox();
        inheritCheck = new javax.swing.JCheckBox();
        hintEditor = new org.radixware.kernel.designer.common.editors.sqml.SqmlEditorPanel();
        filterAbsencePanel = new org.radixware.kernel.designer.ads.editors.presentations.DefaultAndBaseSortingsPanel();

        defaultFilterLabel.setText(org.openide.util.NbBundle.getMessage(SelectorAddonsPanel.class, "Addons-DefaultFilter")); // NOI18N

        baseFiltersPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(SelectorAddonsPanel.class, "Addons-BaseFilters"))); // NOI18N

        baseFiltersGroup.add(anyFilterButton);
        anyFilterButton.setText(org.openide.util.NbBundle.getMessage(SelectorAddonsPanel.class, "Addons-AnyFilterEnabledTip")); // NOI18N

        baseFiltersGroup.add(specFilterButton);
        specFilterButton.setText(org.openide.util.NbBundle.getMessage(SelectorAddonsPanel.class, "Addons-OnlySpecFilterTip")); // NOI18N

        javax.swing.GroupLayout baseFiltersPanelLayout = new javax.swing.GroupLayout(baseFiltersPanel);
        baseFiltersPanel.setLayout(baseFiltersPanelLayout);
        baseFiltersPanelLayout.setHorizontalGroup(
            baseFiltersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(baseFiltersPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(baseFiltersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(baseFiltersTable, javax.swing.GroupLayout.DEFAULT_SIZE, 544, Short.MAX_VALUE)
                    .addComponent(anyFilterButton)
                    .addComponent(specFilterButton))
                .addContainerGap())
        );
        baseFiltersPanelLayout.setVerticalGroup(
            baseFiltersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(baseFiltersPanelLayout.createSequentialGroup()
                .addComponent(anyFilterButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(specFilterButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(baseFiltersTable, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                .addContainerGap())
        );

        filterObligatoryCheck.setText(org.openide.util.NbBundle.getMessage(SelectorAddonsPanel.class, "Addons-FilterIsObligatoryTip")); // NOI18N

        customFiltersCheck.setText(org.openide.util.NbBundle.getMessage(SelectorAddonsPanel.class, "Addons-CustomFiltersTip")); // NOI18N

        inheritCheck.setText(org.openide.util.NbBundle.getMessage(SelectorAddonsPanel.class, "InheritSelector")); // NOI18N

        hintEditor.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(SelectorAddonsPanel.class, "Addons-Hint"))); // NOI18N

        filterAbsencePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(SelectorAddonsPanel.class, "Addons-FilterAbsenceHeader"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(baseFiltersPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(filterAbsencePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 578, Short.MAX_VALUE)
                    .addComponent(hintEditor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 578, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(defaultFilterLabel)
                        .addGap(64, 64, 64)
                        .addComponent(defaultFilterEditor, javax.swing.GroupLayout.DEFAULT_SIZE, 442, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(inheritCheck)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(filterObligatoryCheck)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(customFiltersCheck)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inheritCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(defaultFilterEditor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(defaultFilterLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(baseFiltersPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(filterObligatoryCheck)
                    .addComponent(customFiltersCheck))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(filterAbsencePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(hintEditor, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton anyFilterButton;
    private javax.swing.ButtonGroup baseFiltersGroup;
    private javax.swing.JPanel baseFiltersPanel;
    private org.radixware.kernel.designer.common.dialogs.chooseobject.ConfigureDefinitionListPanel baseFiltersTable;
    private javax.swing.JCheckBox customFiltersCheck;
    private org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel defaultFilterEditor;
    private javax.swing.JLabel defaultFilterLabel;
    private org.radixware.kernel.designer.ads.editors.presentations.DefaultAndBaseSortingsPanel filterAbsencePanel;
    private javax.swing.JCheckBox filterObligatoryCheck;
    private org.radixware.kernel.designer.common.editors.sqml.SqmlEditorPanel hintEditor;
    private javax.swing.JCheckBox inheritCheck;
    private javax.swing.JRadioButton specFilterButton;
    // End of variables declaration//GEN-END:variables

}
