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

package org.radixware.kernel.designer.ads.editors.clazz.simple;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinition.AccessChangedEvent;
import org.radixware.kernel.common.defs.ads.AdsTitledDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsDynamicClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsInterfaceClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsApplicationClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef.DetailReferenceInfo;
import org.radixware.kernel.common.defs.ads.clazz.form.AdsFormHandlerClassDef;
import org.radixware.kernel.common.defs.ads.command.AdsCommandDef;
import org.radixware.kernel.common.defs.ads.command.AdsCommandModelClassDef;
import org.radixware.kernel.common.defs.ads.type.AdsAccessFlags;
import org.radixware.kernel.common.defs.ads.type.AdsTransparence;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.ads.common.dialogs.AccessPanel;
import org.radixware.kernel.designer.ads.common.dialogs.EnvironmentComboBoxModel;
import org.radixware.kernel.designer.ads.editors.base.EnvSelectorPanel;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ConfigureDefinitionListCfg;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ConfigureDefinitionListPanel;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ConfigureDefinitionTable.IdBooleanValueChangeEvent;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ConfigureDefinitionTable.IdBooleanValueChangeListener;
import org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel;
import org.radixware.kernel.designer.common.dialogs.components.DescriptionPanel;
import org.radixware.kernel.designer.common.dialogs.components.localizing.HandleInfo;
import org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingEditorPanel;


class AdsClassEditorGeneralPanel extends javax.swing.JPanel {

    private AdsClassDef sourceClass;
    private boolean isUpdate = false;
    private DescriptionPanel descriptionEditor = new DescriptionPanel();
    private JLabel accessLabel = new JLabel(NbBundle.getMessage(AdsClassEditorGeneralPanel.class, "GeneralPanel-AccessLabel"));
    private AccessPanel accessEditor = new AccessPanel();
    private JCheckBox abstractBox;
    private JCheckBox deprecatedBox;
    private JCheckBox staticBox;
    private DefinitionLinkEditPanel overwrittenEditor;
    private EnvSelectorPanel envSelector;
    private JTextField publishedClassViewer;
    private DefinitionLinkEditPanel linkedTablePanel;
    private ConfigureDefinitionListPanel allowedDetailsEditor;
    private JComboBox environmentEditor;
    private JCheckBox dualEditor;
    private TypeArgumentsOwnerPropertiesPanel typeArgumentsPanel;
    private LocalizingEditorPanel titleEditor;
    private RestrictionsPanel restrictionsPanel;
    private SuperClassHierarchyPanel superClassHierarchyPanel;
    private UploadModelPanel uploadModelPanel;
    private final JCheckBox chbOverwrite = new JCheckBox(NbBundle.getMessage(AdsClassEditorGeneralPanel.class, "AdsClassEditorGeneralPanel-chbOverwrite.Text"));
    private IdBooleanValueChangeListener allowedDetailListener = null;
    
    private final HandleInfo handleInfo = new HandleInfo() {
        @Override
        public AdsDefinition getAdsDefinition() {
            return sourceClass;
        }

        @Override
        public Id getTitleId() {
            return sourceClass != null ? ((AdsTitledDefinition) sourceClass).getTitleId() : null;
        }

        @Override
        public void onAdsMultilingualStringDefChange(IMultilingualStringDef multilingualStringDef) {
            if (multilingualStringDef != null) {
                ((AdsTitledDefinition) getAdsDefinition()).setTitleId(multilingualStringDef.getId());
            } else {
                ((AdsTitledDefinition) getAdsDefinition()).setTitleId(null);
            }
        }

        @Override
        public void onLanguagesPatternChange(EIsoLanguage language, String newStringValue) {
            getAdsMultilingualStringDef().setValue(language, newStringValue);
        }
    };
    private ItemListener accessFlagItemListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            AdsClassEditorGeneralPanel.this.onAccessFlagItemChange(e);
        }
    };

    public AdsClassEditorGeneralPanel() {
    }

    public void open(AdsClassDef adsClassDef) {
        this.sourceClass = adsClassDef;
        this.sourceClass.getAccessChangeSupport().addEventListener(new AdsDefinition.AccessListener() {
            @Override
            public void onEvent(AccessChangedEvent e) {
                AdsClassEditorGeneralPanel.this.update();
            }
        });
        setupUI();
    }

    private void enableStatic(AdsClassDef cls) {
        staticBox.setSelected(cls.getAccessFlags().isStatic());

        boolean enable = !sourceClass.isReadOnly();
        if (cls.isNested()) {
            if (cls.getOwnerClass() instanceof AdsInterfaceClassDef) {
                enable = false;
            }
        }

        if (cls.getClassDefType() == EClassType.ENUMERATION || cls.getClassDefType() == EClassType.INTERFACE) {
            enable = false;
        }

        staticBox.setEnabled(enable);
    }

    private void setupUI() {
        isUpdate = true;

        final boolean readonly = sourceClass.isReadOnly();

        MigLayout layout = new MigLayout("fillx", "[left][left, grow, fill][][]");
        setLayout(layout);

        descriptionEditor.open(sourceClass);
        add(descriptionEditor, "growx, spanx, wrap");

        deprecatedBox = accessEditor.addCheckBox(NbBundle.getMessage(AdsClassEditorGeneralPanel.class, "GeneralPanel-DeprecatedBox"));
        deprecatedBox.addItemListener(accessFlagItemListener);
        add(accessLabel);

        final EClassType classType = sourceClass.getClassDefType();
        final boolean isTransparent = AdsTransparence.isTransparent(sourceClass);
        if (isTransparent) {
            add(accessEditor, "grow 0, wrap");
            JLabel publishedLabel = new JLabel(NbBundle.getMessage(AdsClassEditorGeneralPanel.class, "GeneralPanel-PublishedClassLabel"));
            publishedClassViewer = new JTextField(sourceClass.getTransparence().getPublishedName());
            publishedClassViewer.setEditable(false);
            add(publishedLabel);
            add(publishedClassViewer, "growx, spanx, wrap");
        } else {
            add(accessEditor, "grow 0");
            add(chbOverwrite, "wrap");

            if (sourceClass instanceof AdsEntityObjectClassDef
                    || sourceClass instanceof AdsEntityModelClassDef
                    || sourceClass instanceof AdsFormHandlerClassDef) {

                JLabel envLabel = new JLabel("Client Environment:");
                add(envLabel);
                envSelector = new EnvSelectorPanel();
                add(envSelector, "growx, spanx, wrap");
                envSelector.open(sourceClass);
            }

            final AdsClassDef overwittenClass = sourceClass.getHierarchy().findOverwritten().get();
            if (overwittenClass != null) {
                JLabel overwrittenLabel = new JLabel(NbBundle.getMessage(AdsClassEditorGeneralPanel.class, "GeneralPanel-OverwrittenLabel"));
                overwrittenEditor = new DefinitionLinkEditPanel();
                overwrittenEditor.open(overwittenClass, overwittenClass.getId());
                add(overwrittenLabel);
                add(overwrittenEditor, "growx, spanx, wrap");
            }
        }

        if (!(sourceClass instanceof AdsModelClassDef)
                && classType != EClassType.INTERFACE
                && classType != EClassType.ENUMERATION) {

            abstractBox = accessEditor.addCheckBox(NbBundle.getMessage(AdsClassEditorGeneralPanel.class, "GeneralPanel-AbstractBox"));
            abstractBox.addItemListener(accessFlagItemListener);
        }

        if (sourceClass.isNested()
                && !(sourceClass instanceof AdsModelClassDef)
                && classType != EClassType.INTERFACE
                && classType != EClassType.ENUMERATION) {

            staticBox = accessEditor.addCheckBox(NbBundle.getMessage(AdsClassEditorGeneralPanel.class, "GeneralPanel-StaticBox"));
            staticBox.addItemListener(accessFlagItemListener);
        }

        accessEditor.open(sourceClass);

        final boolean hasEnvironment = isTransparent
                || classType == EClassType.INTERFACE || classType == EClassType.ENUMERATION || classType == EClassType.DYNAMIC;

        if (hasEnvironment) {
            environmentEditor = new JComboBox();
            environmentEditor.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!AdsClassEditorGeneralPanel.this.isUpdate) {
                        String str = AdsClassEditorGeneralPanel.this.environmentEditor.getSelectedItem().toString();
                        AdsClassEditorGeneralPanel.this.sourceClass.setUsageEnvironment(ERuntimeEnvironmentType.getForName(str));
                        if (dualEditor != null) {
                            dualEditor.setEnabled(!readonly && sourceClass.getUsageEnvironment() == ERuntimeEnvironmentType.COMMON_CLIENT);
                        }
                    }

                }
            });
//            environmentEditor.setModel(EnvironmentComboBoxModel.Factory.newInstance(sourceClass));
//            environmentEditor.getModel().setSelectedItem(sourceClass.getUsageEnvironment());
            environmentEditor.setEnabled(!readonly);

            JLabel environmentLabel = new JLabel(NbBundle.getMessage(AdsClassEditorGeneralPanel.class, "GeneralPanel-Environment"));

            add(environmentLabel);

            if (sourceClass instanceof AdsDynamicClassDef) {
                add(environmentEditor, "grow 0, wrap");
                dualEditor = new JCheckBox("Separate client runtime");
                dualEditor.setEnabled(!readonly && sourceClass.getUsageEnvironment() == ERuntimeEnvironmentType.COMMON_CLIENT);
                dualEditor.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (sourceClass instanceof AdsDynamicClassDef) {
                            ((AdsDynamicClassDef) sourceClass).setDual(dualEditor.isSelected());
                        }
                    }
                });
                add(dualEditor, "spanx, wrap");
            } else {
                add(environmentEditor, "grow 0, wrap");
            }
        }

        if (hasTypeArguments()) {
            typeArgumentsPanel = new TypeArgumentsOwnerPropertiesPanel();
            typeArgumentsPanel.open(sourceClass);
            add(typeArgumentsPanel, "grow, spanx, wrap");
        }

        if (hasTitleEditor()) {
            titleEditor = new LocalizingEditorPanel();
            titleEditor.open(handleInfo);
            titleEditor.setReadonly(readonly);
            add(titleEditor, "growx, spanx, wrap");
        }

        if (hasLinkedEntity()) {
            AdsEntityObjectClassDef entityObjectClassDef = (AdsEntityObjectClassDef) sourceClass;
            final DdsTableDef entityTable = entityObjectClassDef.findTable(entityObjectClassDef);

            JLabel linkedLabel = new JLabel(NbBundle.getMessage(AdsClassEditorGeneralPanel.class, "GeneralPanel-LinkedTable"));
            linkedTablePanel = new DefinitionLinkEditPanel();
            linkedTablePanel.open(entityTable, entityTable != null ? entityTable.getId() : null);
            linkedTablePanel.setClearable(false);
            linkedTablePanel.setEnabled(!readonly);

            add(linkedLabel);
            add(linkedTablePanel, "growx, spanx, wrap");

            if (entityTable != null) {
                openAllowedDetailsComponent(entityObjectClassDef, entityTable);
            }

            if (classType.equals(EClassType.ENTITY)) {
                restrictionsPanel = new RestrictionsPanel();
                restrictionsPanel.open(((AdsEntityClassDef) entityObjectClassDef).getPresentations().getRestrictions());
                add(restrictionsPanel, "growx, spanx, wrap");
            }
        }

        if (classType == EClassType.COMMAND_MODEL) {
            AdsCommandModelClassDef cmc = (AdsCommandModelClassDef) sourceClass;
            AdsCommandDef command = cmc.findCommand();
            DefinitionLinkEditPanel linkedCommandPanel = new DefinitionLinkEditPanel();
            linkedCommandPanel.open(command, null);
            linkedCommandPanel.setClearable(false);
            linkedCommandPanel.setEnabled(!readonly);

            JLabel linkedLabel = new JLabel("Handled command:");
            add(linkedLabel);
            add(linkedCommandPanel, "growx, spanx, wrap");
        }
        
        superClassHierarchyPanel = new SuperClassHierarchyPanel(isTransparent);
        superClassHierarchyPanel.open(sourceClass);
        add(superClassHierarchyPanel, "grow, spanx, wrap");
        
        if (!isTransparent) {
            uploadModelPanel = new UploadModelPanel();
            uploadModelPanel.open(sourceClass);
            add(uploadModelPanel, "span");
        }

        chbOverwrite.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sourceClass.setOverwrite(chbOverwrite.isSelected());
                updateOverwriteStatus();
            }
        });

        isUpdate = false;
    }

    private void updateOverwriteStatus() {
        final AdsClassDef overwritten = sourceClass.getHierarchy().findOverwritten().get();
        if (overwritten != null) {
            chbOverwrite.setVisible(true);
            chbOverwrite.setSelected(sourceClass.isOverwrite());
            chbOverwrite.setForeground(Color.BLACK);

        } else if (sourceClass.isOverwrite()) {
            chbOverwrite.setVisible(true);
            chbOverwrite.setSelected(true);
            chbOverwrite.setForeground(Color.RED);

        } else {
            chbOverwrite.setVisible(false);
        }
    }

    public void update() {
        isUpdate = true;

        final boolean readonly = sourceClass.isReadOnly();
        final EClassType classType = sourceClass.getClassDefType();
        final boolean isTransparent = AdsTransparence.isTransparent(sourceClass);

        descriptionEditor.update();

        accessEditor.open(sourceClass);
        deprecatedBox.setSelected(sourceClass.getAccessFlags().isDeprecated());
        deprecatedBox.setEnabled(!readonly);

        final AdsAccessFlags accessFlags = sourceClass.getAccessFlags();
        if (isTransparent) {
            publishedClassViewer.setText(sourceClass.getTransparence().getPublishedName());
            publishedClassViewer.setEnabled(!readonly);
        } else {

            if (envSelector != null) {
                envSelector.update();
            }
            final AdsClassDef overwittenClassDef = sourceClass.getHierarchy().findOverwritten().get();
            if (overwittenClassDef != null) {
                if (overwrittenEditor == null){
                    removeAll();
                    setupUI();
                    return;
                } else {
                    overwrittenEditor.open(overwittenClassDef, overwittenClassDef.getId());
                }
            }
        }

        if (!(sourceClass instanceof AdsModelClassDef)
                && classType != EClassType.INTERFACE
                && classType != EClassType.ENUMERATION
                && abstractBox != null) {

            abstractBox.setSelected(accessFlags.isAbstract());
            if (this.sourceClass.isFinal() || AdsTransparence.isTransparent(sourceClass, false)) {
                abstractBox.setEnabled(false);
            } else {
                abstractBox.setEnabled(!readonly);
            }
        }

        final boolean hasTypeArguments = hasTypeArguments();
        final boolean isEnumClass = hasTypeArguments || classType == EClassType.ENUMERATION;

        if (isEnumClass) {
            environmentEditor.setModel(EnvironmentComboBoxModel.Factory.newInstance(sourceClass));
            environmentEditor.getModel().setSelectedItem(sourceClass.getUsageEnvironment());
            environmentEditor.setEnabled(!readonly);
            if (dualEditor != null) {
                dualEditor.setEnabled(!readonly && sourceClass instanceof AdsDynamicClassDef && sourceClass.getUsageEnvironment() == ERuntimeEnvironmentType.COMMON_CLIENT);
                dualEditor.setSelected(sourceClass.isDual());
            }
        }
        if (hasTypeArguments) {
            typeArgumentsPanel.update();
        }

        if (hasTitleEditor()) {
            titleEditor.open(handleInfo);
            titleEditor.setReadonly(readonly);
        }

        if (hasLinkedEntity()) {
            AdsEntityObjectClassDef entityObjectClassDef = (AdsEntityObjectClassDef) sourceClass;
            final DdsTableDef entityTable = entityObjectClassDef.findTable(entityObjectClassDef);
            linkedTablePanel.open(entityTable, entityTable != null ? entityTable.getId() : null);
            linkedTablePanel.setEnabled(!readonly);

            if (allowedDetailsEditor != null) {
                allowedDetailsEditor.update();
                allowedDetailsEditor.setEnabled(!readonly);
            }

            if (classType.equals(EClassType.ENTITY)) {
                restrictionsPanel.setReadonly(readonly);
                restrictionsPanel.update();
            }
        }

        
        superClassHierarchyPanel.update();
        if (!isTransparent) {
            uploadModelPanel.update();
        }

        if (staticBox != null) {
            enableStatic(sourceClass);
        }

        updateOverwriteStatus();

        isUpdate = false;
    }

    private boolean hasTypeArguments() {
        return sourceClass != null && (AdsTransparence.isTransparent(sourceClass)
                || sourceClass.getClassDefType() == EClassType.INTERFACE || sourceClass.getClassDefType() == EClassType.DYNAMIC);
    }

    private boolean hasTitleEditor() {
        return sourceClass != null
                && (!AdsTransparence.isTransparent(sourceClass)
                && sourceClass.getClassDefType() != EClassType.ENTITY
                && sourceClass.getClassDefType() != EClassType.APPLICATION
                && sourceClass.getClassDefType() != EClassType.ENUMERATION)
                && !sourceClass.getUsageEnvironment().isClientEnv();
    }

    private boolean hasLinkedEntity() {
        return sourceClass != null && (sourceClass.getClassDefType() == EClassType.APPLICATION || sourceClass.getClassDefType() == EClassType.ENTITY);
    }

    private void openAllowedDetailsComponent(final AdsEntityObjectClassDef adsEntityObjectClassDef, DdsTableDef table) {

        final List<DetailReferenceInfo> detailReferenceInfoList = adsEntityObjectClassDef.getAllowedDetailRefs();
        final List<Id> configureDefinitionIDs = new ArrayList<>();
        for (DetailReferenceInfo xDetailReferenceInfo : detailReferenceInfoList) {
            configureDefinitionIDs.add(xDetailReferenceInfo.getReferenceId());
        }

        final Set<DdsReferenceDef> allReferences = table != null ? table.collectIncomingReferences() : Collections.<DdsReferenceDef>emptySet();
        final List<DdsReferenceDef> availableDefinitions = new ArrayList<>();
        for (DdsReferenceDef ref : allReferences) {
            if (ref.getType() == DdsReferenceDef.EType.MASTER_DETAIL) {
                availableDefinitions.add(ref);
            }
        }

        if (!availableDefinitions.isEmpty()) {

            final List<DetailReferenceInfo> parentIds;
            AdsClassDef superClassObject = adsEntityObjectClassDef.getInheritance().findSuperClass().get();
            if (superClassObject instanceof AdsEntityClassDef) {
                parentIds = ((AdsEntityClassDef) superClassObject).getPresentations().getOwnerClass().getAllowedDetailRefs();
            } else if (superClassObject instanceof AdsApplicationClassDef) {
                parentIds = ((AdsApplicationClassDef) superClassObject).getAllowedDetailRefs();
            } else {
                parentIds = new ArrayList<>();
            }

            final List<Id> forbiddenIDs = new ArrayList<>();
            for (DetailReferenceInfo xDetailReferenceInfo : parentIds) {
                forbiddenIDs.add(xDetailReferenceInfo.getReferenceId());
            }

            if (allowedDetailsEditor == null) {
                allowedDetailsEditor = new ConfigureDefinitionListPanel();
            } else {
                allowedDetailsEditor.setVisible(true);
            }
            if (allowedDetailListener == null) {
                allowedDetailListener = new IdBooleanValueChangeListener() {
                    @Override
                    public void onEvent(IdBooleanValueChangeEvent e) {
                        adsEntityObjectClassDef.setDetailAllowed(e.getId(), e.getNewValue());
                    }
                };
                allowedDetailsEditor.addConfigurableIdIsCheckedListener(allowedDetailListener);
            }

            allowedDetailsEditor.setEnabled(!adsEntityObjectClassDef.isReadOnly());
            this.allowedDetailsEditor.open(configureDefinitionIDs, ConfigureDefinitionListCfg.Factory.newInstanceWithDisabledIds(availableDefinitions, forbiddenIDs));
            JLabel allowedLabel = new JLabel(NbBundle.getMessage(AdsClassEditorGeneralPanel.class, "GeneralPanel-LinkedTable-AllowedDetails"));
            add(allowedLabel);
            add(allowedDetailsEditor, "height pref!, spanx, wrap");

        }
    }

    private void onAccessFlagItemChange(ItemEvent event) {
        if (!isUpdate) {
            final Object source = event.getItemSelectable();
            final boolean selected = event.getStateChange() == ItemEvent.SELECTED;

            if (source == abstractBox) {
                sourceClass.getAccessFlags().setAbstract(selected);
                if (selected) {
                    sourceClass.getAccessFlags().setDeprecated(false);
                }
            } else if (source == deprecatedBox) {
                sourceClass.getAccessFlags().setDeprecated(selected);
            } else if (source == staticBox) {
                sourceClass.getAccessFlags().setStatic(selected);
            }
        }
    }
}