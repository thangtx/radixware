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
 * DefaultAndBaseSortingsPanel.java
 *
 * Created on 03.11.2009, 11:22:02
 */
package org.radixware.kernel.designer.ads.editors.presentations;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.openide.util.ChangeSupport;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsFilterDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSortingDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.SelectorAddons;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ConfigureDefinitionListCfg;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ConfigureDefinitionTable;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ConfigureDefinitionTable.DefBooleanValueChangeEvent;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ConfigureDefinitionTable.IdBooleanValueChangeEvent;
import org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditRenderer;


public class DefaultAndBaseSortingsPanel extends javax.swing.JPanel {

    private final ConfigureDefinitionTable.DefBooleanValueChangeListener enabledSortingsListener = new ConfigureDefinitionTable.DefBooleanValueChangeListener() {

        @Override
        public void onEvent(DefBooleanValueChangeEvent e) {
            DefaultAndBaseSortingsPanel.this.onEnabledSortingsChange(e);
        }
    };
    private final ConfigureDefinitionTable.IdBooleanValueChangeListener idSortingsListener = new ConfigureDefinitionTable.IdBooleanValueChangeListener() {

        @Override
        public void onEvent(IdBooleanValueChangeEvent e) {
            DefaultAndBaseSortingsPanel.this.onEnabledIdChange(e);
        }
    };
    private AdsDefinition definition;
    private boolean readonly = false;

    /** Creates new form DefaultAndBaseSortingsPanel */
    public DefaultAndBaseSortingsPanel() {
        initComponents();
        ChangeListener defaultSortingListener = new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                DefaultAndBaseSortingsPanel.this.onDefaultSortingChange();
            }
        };
        defaultSortingEditor.addChangeListener(defaultSortingListener);
        defaultSortingEditor.setComboMode();
        ActionListener anySortingListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultAndBaseSortingsPanel.this.onAnySortingEnabledChange();
            }
        };
        anySortingButton.addActionListener(anySortingListener);
        onlySpecSortingsButton.addActionListener(anySortingListener);
        baseSortingsTable.addConfigurableDefinitionIsCheckedListener(enabledSortingsListener);
        baseSortingsTable.addConfigurableIdIsCheckedListener(idSortingsListener);

        ActionListener customSortingsListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultAndBaseSortingsPanel.this.onCustomSortingsUsageChange();
            }
        };
        customSortingsCheck.addActionListener(customSortingsListener);

        baseSortingsTable.addSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                DefaultAndBaseSortingsPanel.this.selectionChangeSupport.fireChange();
            }
        });
    }
    private ChangeSupport selectionChangeSupport = new ChangeSupport(this);

    public void addChangeListener(ChangeListener changeListener) {
        selectionChangeSupport.addChangeListener(changeListener);
    }

    public void removeChangeListener(ChangeListener changeListener) {
        selectionChangeSupport.removeChangeListener(changeListener);
    }

    public AdsFilterDef.EnabledSorting getSelectedEnabledSorting() {
        Definition selected = baseSortingsTable.getSelectedDefinition();
        if (selected != null) {
            boolean stop = false;
            int i = 0;
            AdsFilterDef.EnabledSortings enabledSortings = ((AdsFilterDef) definition).getEnabledSortings();
            while (!stop && i < enabledSortings.size()) {
                Id es = enabledSortings.get(i).getSortingId();
                if (es.equals(selected.getId())) {
                    stop = true;
                    return enabledSortings.get(i);
                } else {
                    i++;
                }
            }
        }
        return null;
    }

    public void open(final AdsDefinition definition) {
        this.definition = definition;
        update();
    }
    private boolean isUpdate = false;

    public void update() {
        isUpdate = true;
        this.readonly = definition.isReadOnly();
        if (definition instanceof AdsFilterDef) {
            updateForFilter();
        } else if (definition instanceof AdsSelectorPresentationDef) {
            updateForPresentation();
        }
        isUpdate = false;
    }

    private void updateForFilter() {
        final AdsFilterDef filter = (AdsFilterDef) definition;
        setEnabled(!readonly);

        defaultSortingEditor.setComboBoxValues(filter.getOwnerClass().getPresentations().getSortings().get(EScope.ALL), true);
        defaultSortingEditor.open(filter.findDefaultSorting(), filter.getDefaultSortingId());
        boolean anySorting = filter.isAnyBaseSortingEnabled();
        anySortingButton.setSelected(anySorting);
        onlySpecSortingsButton.setSelected(!anySorting);

        List<Id> sortingsIds = new ArrayList<Id>();
        List<AdsSortingDef> sortingsList = filter.getOwnerClass().getPresentations().getSortings().get(EScope.ALL);
        for (AdsSortingDef s : sortingsList) {
            sortingsIds.add(s.getId());
        }
        ConfigureDefinitionListCfg sortingsCfg = ConfigureDefinitionListCfg.Factory.newInstanceWithEnabledIds(sortingsList, sortingsIds);
        baseSortingsTable.open(filter.getEnabledSortings().getEnabledSortingIds(), sortingsCfg);
        customSortingsCheck.setSelected(filter.isCustomSortingEnabled());
    }

    private void updateForPresentation() {
        final AdsSelectorPresentationDef presentation = (AdsSelectorPresentationDef) definition;

        SelectorAddons addons = presentation.getAddons();
        setEnabled(!addons.isFilterObligatory() && !presentation.isAddonsInherited());
        defaultSortingEditor.setComboBoxValues(presentation.getOwnerClass().getPresentations().getSortings().get(EScope.ALL), true);
        defaultSortingEditor.open(addons.findDefaultSorting(), addons.getDefaultSortingId());
        boolean anySortings = addons.isAnyBaseSortingEnabled();
        anySortingButton.setSelected(anySortings);
        onlySpecSortingsButton.setSelected(!anySortings);
        List<Id> sortingsIds = new ArrayList<Id>();
        List<AdsSortingDef> sortingsList = presentation.getOwnerClass().getPresentations().getSortings().get(EScope.ALL);
        for (AdsSortingDef s : sortingsList) {
            sortingsIds.add(s.getId());
        }
        ConfigureDefinitionListCfg sortingsCfg = ConfigureDefinitionListCfg.Factory.newInstanceWithEnabledIds(sortingsList, sortingsIds);
        baseSortingsTable.open(addons.getEnabledSortingIds(), sortingsCfg);
        customSortingsCheck.setSelected(addons.isCustomSortingEnabled());
    }

    @Override
    public void setEnabled(boolean enabled) {
        defaultSortingLabel.setEnabled(enabled && !readonly);
        defaultSortingEditor.setEnabled(enabled && !readonly);
        customSortingsCheck.setEnabled(enabled && !readonly);
        anySortingButton.setEnabled(enabled && !readonly);
        onlySpecSortingsButton.setEnabled(enabled && !readonly);
        boolean enableTable = false;
        if (definition != null) {
            if (definition instanceof AdsSelectorPresentationDef) {
                enableTable = !((AdsSelectorPresentationDef) definition).getAddons().isAnyBaseSortingEnabled();
            } else if (definition instanceof AdsFilterDef) {
                enableTable = !((AdsFilterDef) definition).isAnyBaseSortingEnabled();
            }
        }
        baseSortingsTable.setEnabled(enabled && !readonly && enableTable);
        super.setEnabled(enabled);
    }

    private void onDefaultSortingChange() {
        if (!isUpdate && definition != null) {
            if (definition instanceof AdsFilterDef) {
                AdsFilterDef filter = (AdsFilterDef) definition;
                Id newId = defaultSortingEditor.getDefinitionId();
                filter.setDefaultSortingId(newId);
                defaultSortingEditor.setComboBoxValues(filter.getOwnerClass().getPresentations().getSortings().get(EScope.ALL), true);
                defaultSortingEditor.open(filter.findDefaultSorting(), filter.getDefaultSortingId());
            } else if (definition instanceof AdsSelectorPresentationDef) {
                AdsSelectorPresentationDef presentation = (AdsSelectorPresentationDef) definition;
                presentation.getAddons().setDefaultSortingId(defaultSortingEditor.getDefinitionId());
                defaultSortingEditor.setComboBoxValues(presentation.getOwnerClass().getPresentations().getSortings().get(EScope.ALL), true);
                defaultSortingEditor.open(presentation.getAddons().findDefaultSorting(), presentation.getAddons().getDefaultSortingId());
            }
            defaultSortingEditor.setComboBoxRenderer(new DefinitionLinkEditRenderer());
        }
    }

    private void onEnabledIdChange(IdBooleanValueChangeEvent e) {
        if (!isUpdate) {
            Id selected = e.getId();

            if (definition instanceof AdsSelectorPresentationDef) {
                AdsSelectorPresentationDef selector = (AdsSelectorPresentationDef) definition;
                AdsSortingDef sorting = selector.getOwnerClass().getPresentations().getSortings().findById(selected, EScope.ALL).get();
                if (sorting == null && !e.getNewValue()) {
                    selector.getAddons().removeEnabledSortingId(selected);
                    updateForPresentation();
                }
            } else if (definition instanceof AdsFilterDef) {
                AdsFilterDef filter = (AdsFilterDef) definition;
                AdsSortingDef sorting = filter.getOwnerClass().getPresentations().getSortings().findById(selected, EScope.ALL).get();
                if (sorting == null && !e.getNewValue()) {
                    boolean stop = false;
                    int i = 0;
                    AdsFilterDef.EnabledSortings enabledSortings = filter.getEnabledSortings();

                    while (!stop && i < enabledSortings.size()) {
                        Id es = enabledSortings.get(i).getSortingId();
                        if (es.equals(selected)) {
                            stop = true;
                            ((AdsFilterDef) definition).getEnabledSortings().remove(i);
                        } else {
                            i++;
                        }
                    }
                    updateForFilter();
                }
            }
        }
    }

    private void onEnabledSortingsChange(DefBooleanValueChangeEvent e) {
        if (!isUpdate) {
            boolean isSelected = e.getNewValue();
            final AdsSortingDef modifiedSorting = (AdsSortingDef) e.getRadixObject();

            if (definition instanceof AdsSelectorPresentationDef) {
                SelectorAddons addons = ((AdsSelectorPresentationDef) definition).getAddons();
                if (isSelected) {
                    addons.addEnabledSortingId(modifiedSorting.getId());
                } else {
                    addons.removeEnabledSortingId(modifiedSorting.getId());
                }
            } else if (definition instanceof AdsFilterDef) {
                if (isSelected) {
                    ((AdsFilterDef) definition).enableSorting(modifiedSorting);
                } else {
                    boolean stop = false;
                    int i = 0;
                    AdsFilterDef.EnabledSortings enabledSortings = ((AdsFilterDef) definition).getEnabledSortings();

                    while (!stop && i < enabledSortings.size()) {
                        Id es = enabledSortings.get(i).getSortingId();
                        if (es.equals(modifiedSorting.getId())) {
                            stop = true;
                            ((AdsFilterDef) definition).getEnabledSortings().remove(i);
                        } else {
                            i++;
                        }
                    }
                }
                selectionChangeSupport.fireChange();
            }
        }
    }

    private void onAnySortingEnabledChange() {
        if (!isUpdate) {
            boolean anySelected = anySortingButton.isSelected();
            if (definition instanceof AdsFilterDef) {
                ((AdsFilterDef) definition).setAnyBaseSortingEnabled(anySelected);
            } else if (definition instanceof AdsSelectorPresentationDef) {
                ((AdsSelectorPresentationDef) definition).getAddons().setAnyBaseSortingEnabled(anySelected);
            }
            baseSortingsTable.setEnabled(!anySelected && !readonly);
            selectionChangeSupport.fireChange();
        }
    }

    private void onCustomSortingsUsageChange() {
        if (!isUpdate) {
            boolean isSelected = customSortingsCheck.isSelected();
            if (definition instanceof AdsSelectorPresentationDef) {
                ((AdsSelectorPresentationDef) definition).getAddons().setCustomSortingEnabled(isSelected);
            } else if (definition instanceof AdsFilterDef) {
                ((AdsFilterDef) definition).setCustomSortingEnabled(isSelected);
            }
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

        baseSortingsGroup = new javax.swing.ButtonGroup();
        defaultSortingLabel = new javax.swing.JLabel();
        defaultSortingEditor = new org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel();
        customSortingsCheck = new javax.swing.JCheckBox();
        baseSortingsPanel = new javax.swing.JPanel();
        anySortingButton = new javax.swing.JRadioButton();
        onlySpecSortingsButton = new javax.swing.JRadioButton();
        baseSortingsTable = new org.radixware.kernel.designer.common.dialogs.chooseobject.ConfigureDefinitionListPanel();

        defaultSortingLabel.setText(org.openide.util.NbBundle.getMessage(DefaultAndBaseSortingsPanel.class, "Addons-DefaultSortingTip")); // NOI18N

        customSortingsCheck.setText(org.openide.util.NbBundle.getMessage(DefaultAndBaseSortingsPanel.class, "Addons-CustomSortingsTip")); // NOI18N

        baseSortingsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(DefaultAndBaseSortingsPanel.class, "Addons-BaseSortings"))); // NOI18N

        baseSortingsGroup.add(anySortingButton);
        anySortingButton.setText(org.openide.util.NbBundle.getMessage(DefaultAndBaseSortingsPanel.class, "Addons-AnySortingTip")); // NOI18N

        baseSortingsGroup.add(onlySpecSortingsButton);
        onlySpecSortingsButton.setText(org.openide.util.NbBundle.getMessage(DefaultAndBaseSortingsPanel.class, "Addons-OnlySpecSortingsTip")); // NOI18N

        javax.swing.GroupLayout baseSortingsPanelLayout = new javax.swing.GroupLayout(baseSortingsPanel);
        baseSortingsPanel.setLayout(baseSortingsPanelLayout);
        baseSortingsPanelLayout.setHorizontalGroup(
            baseSortingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, baseSortingsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(baseSortingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(baseSortingsTable, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE)
                    .addComponent(anySortingButton, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(onlySpecSortingsButton, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );
        baseSortingsPanelLayout.setVerticalGroup(
            baseSortingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(baseSortingsPanelLayout.createSequentialGroup()
                .addComponent(anySortingButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(onlySpecSortingsButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(baseSortingsTable, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(baseSortingsPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(defaultSortingLabel)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(customSortingsCheck)
                            .addComponent(defaultSortingEditor, javax.swing.GroupLayout.DEFAULT_SIZE, 366, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(defaultSortingLabel)
                    .addComponent(defaultSortingEditor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(customSortingsCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(baseSortingsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton anySortingButton;
    private javax.swing.ButtonGroup baseSortingsGroup;
    private javax.swing.JPanel baseSortingsPanel;
    private org.radixware.kernel.designer.common.dialogs.chooseobject.ConfigureDefinitionListPanel baseSortingsTable;
    private javax.swing.JCheckBox customSortingsCheck;
    private org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel defaultSortingEditor;
    private javax.swing.JLabel defaultSortingLabel;
    private javax.swing.JRadioButton onlySpecSortingsButton;
    // End of variables declaration//GEN-END:variables
}
