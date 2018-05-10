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

package org.radixware.kernel.designer.ads.editors.presentations;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.openide.util.ChangeSupport;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.common.AdsFilters;
import org.radixware.kernel.common.defs.ads.common.ICustomViewable;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.ads.common.dialogs.AccessEditPanel;
import org.radixware.kernel.designer.ads.editors.base.EnvSelectorPanel;
import org.radixware.kernel.designer.ads.editors.common.CreatePresentationsListPanel;
import org.radixware.kernel.designer.common.dialogs.components.ComponentTitledBorder;
import org.radixware.kernel.designer.common.dialogs.components.DefaultFormSizePanel;
import org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel;
import org.radixware.kernel.designer.common.dialogs.components.InheritableTitlePanel;
import org.radixware.kernel.designer.common.dialogs.components.localizing.HandleInfo;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.editors.AdsDefinitionIconPresentation;
import org.radixware.kernel.designer.common.editors.AdsDefinitionIconPresentation.IconIdChangeEvent;


class CommonPropertiesPanel extends javax.swing.JPanel {

    private javax.swing.JLabel baseLabel = new javax.swing.JLabel(NbBundle.getMessage(CommonPropertiesPanel.class, "Presentations-BaseTip"));
    private final javax.swing.JLabel replacedLable = new javax.swing.JLabel(NbBundle.getMessage(CommonPropertiesPanel.class, "Presentations-ReplacedTip"));
    // private javax.swing.JLabel editorForCreationLabel = new javax.swing.JLabel(NbBundle.getMessage(CommonPropertiesPanel.class, "EditorPresentationForCreation"));
    private final javax.swing.JCheckBox inheritIcon = new javax.swing.JCheckBox(NbBundle.getMessage(CommonPropertiesPanel.class, "Presentations-InheritIconTip"));
    private final javax.swing.JCheckBox inheritCustomView = new javax.swing.JCheckBox(NbBundle.getMessage(CommonPropertiesPanel.class, "Presentations-InheritCustomView"));
    private final javax.swing.JCheckBox inheritChildren = new javax.swing.JCheckBox(NbBundle.getMessage(CommonPropertiesPanel.class, "Presentations-InheritChildrenTip"));
    private final javax.swing.JCheckBox inheritEditorPages = new javax.swing.JCheckBox(NbBundle.getMessage(CommonPropertiesPanel.class, "Presentations-InheritEditorPages"));
    private final javax.swing.JCheckBox useCustomView = new javax.swing.JCheckBox(NbBundle.getMessage(CommonPropertiesPanel.class, "Presentations-CustomViewTip"));
    private final javax.swing.JCheckBox useWebCustomView = new javax.swing.JCheckBox(NbBundle.getMessage(CommonPropertiesPanel.class, "Presentations-WebCustomViewTip"));
    private final javax.swing.JCheckBox useDefaultModelBox = new javax.swing.JCheckBox(NbBundle.getMessage(CommonPropertiesPanel.class, "UseDefaultModelTip"));
    private final javax.swing.JCheckBox cbAutoExpand = new javax.swing.JCheckBox("Enable auto expand");
    private final javax.swing.JCheckBox cbRestorePosition = new javax.swing.JCheckBox("Enable position restore");
    private AdsPresentationDef presentation;
    private final InheritableTitlePanel titleEditor;
    private final AdsDefinitionIconPresentation iconEditor;
    private final DefinitionLinkEditPanel baseEditor;
    private final DefinitionLinkEditPanel replacedEditor;
    private final DefinitionLinkEditPanel overEditor;
    private final PresentationsForEditing editorPresentationsEditor = new PresentationsForEditing();
    private final CreatePresentationsListPanel editorForCreation = new CreatePresentationsListPanel();
    //private DefinitionLinkEditPanel editorForCreation = new DefinitionLinkEditPanel();
    private final javax.swing.JCheckBox inheritClassCatalog = new javax.swing.JCheckBox(NbBundle.getMessage(CommonPropertiesPanel.class, "InheritClassCatalog"));
    private final javax.swing.JCheckBox isAutoSortInstantiatableClassCatalog = new javax.swing.JCheckBox("Auto sort by title");
    private final javax.swing.JLabel classCatalogLabel = new javax.swing.JLabel(NbBundle.getMessage(CommonPropertiesPanel.class, "ClassCatalog"));
    private final DefinitionLinkEditPanel classCatalogEditor = new DefinitionLinkEditPanel();
    private final javax.swing.JCheckBox inheritRightsInheritanceMode = new javax.swing.JCheckBox(NbBundle.getMessage(CommonPropertiesPanel.class, "RightInheritance-InheritTip"));
    private final javax.swing.JLabel rightsInheritanceLabel = new javax.swing.JLabel(NbBundle.getMessage(CommonPropertiesPanel.class, "RightInheritance-Label"));
    private final RightsInheritancePanel rightInheritancePanel = new RightsInheritancePanel();
    private final Color defaultForeground;
    private final javax.swing.JLabel accessLabel = new javax.swing.JLabel(NbBundle.getMessage(CommonPropertiesPanel.class, "AccessibilityTip"));
    private final AccessEditPanel accessPanel = new AccessEditPanel();
    private final EnvSelectorPanel envSelectorPanel = new EnvSelectorPanel();
    private final DefaultFormSizePanel defaultSizePanel = new DefaultFormSizePanel();
    private boolean readonly = false;
    private volatile boolean isUpdate = false;
    private boolean isTitleInherited = false;

    public CommonPropertiesPanel() {
        titleEditor = new InheritableTitlePanel();
        iconEditor = new AdsDefinitionIconPresentation();

        iconEditor.getIconIdChangeSupport().addEventListener(new AdsDefinitionIconPresentation.IconIdStateChangeListener() {
            @Override
            public void onEvent(IconIdChangeEvent e) {
                if (!CommonPropertiesPanel.this.isUpdate) {
                    CommonPropertiesPanel.this.presentation.setIconId(e.iconId);
                }
            }
        });

        inheritCustomView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CommonPropertiesPanel.this.onInheritCustomViewChange();
            }
        });

        useCustomView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onUseCustomView(ERuntimeEnvironmentType.EXPLORER, useCustomView);
                update();
            }
        });

        useWebCustomView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onUseCustomView(ERuntimeEnvironmentType.WEB, useWebCustomView);
                update();
            }
        });

        inheritIcon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CommonPropertiesPanel.this.onInheritIconChange();
            }
        });

        inheritChildren.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CommonPropertiesPanel.this.onInheritChildrenChange();
            }
        });

        baseEditor = new DefinitionLinkEditPanel();
        baseEditor.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                CommonPropertiesPanel.this.onBasePresentationChange();
            }
        });
        baseEditor.setComboMode();
        baseEditor.setComboBoxRenderer(new BasePresentationListRenderer());

        replacedEditor = new DefinitionLinkEditPanel();
        replacedEditor.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                CommonPropertiesPanel.this.onReplacePresentationId();
            }
        });
        replacedEditor.setComboMode();
        replacedEditor.setComboBoxRenderer(new BasePresentationListRenderer());

        overEditor = new DefinitionLinkEditPanel();

//KAA: no longer usable         ChangeListener editorCreationListener = new ChangeListener() {
//
//            @Override
//            public void stateChanged(ChangeEvent e) {
//                CommonPropertiesPanel.this.onEditorForCreationChange();
//            }
//        };
//KAA: no loger usable code        editorForCreation.addChangeListener(editorCreationListener);
//        editorForCreation.setComboMode();
//        editorForCreation.setComboBoxRenderer(new BasePresentationListRenderer());

        inheritClassCatalog.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CommonPropertiesPanel.this.onClassCatalogInheritanceChange();
            }
        });
        
        isAutoSortInstantiatableClassCatalog.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 CommonPropertiesPanel.this.onAutoSortClassesChange();
            }
        });

        defaultForeground = inheritClassCatalog.getForeground();
        classCatalogEditor.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                CommonPropertiesPanel.this.onClassCataglogChange();
            }
        });

        classCatalogEditor.setComboMode();
        inheritEditorPages.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CommonPropertiesPanel.this.onInheritEditorPagesChange();
            }
        });

        useDefaultModelBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isUpdate) {
                    setDefaultModel(useDefaultModelBox.isSelected());
                }
            }
        });

        inheritRightsInheritanceMode.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (!isUpdate) {
                    final boolean result = inheritRightsInheritanceMode.isSelected();
                    assert (presentation instanceof AdsEditorPresentationDef);
                    ((AdsEditorPresentationDef) presentation).setRightsInheritanceModeInherited(result);
                    rightInheritancePanel.update();
                }
            }
        });

        cbAutoExpand.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isUpdate) {
                    ((AdsSelectorPresentationDef) presentation).setAutoExpandEnabled(cbAutoExpand.isSelected());

                }
            }
        });
        cbRestorePosition.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isUpdate) {
                    ((AdsSelectorPresentationDef) presentation).setRestorePositionEnabled(cbRestorePosition.isSelected());

                }
            }
        });
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(CommonPropertiesPanel.class, "SelectorTabs-General");
    }

    public void open(final AdsPresentationDef presentation) {
        this.presentation = presentation;
        isUpdate = true;
        if (presentation instanceof AdsSelectorPresentationDef) {
            editorPresentationsEditor.open((AdsSelectorPresentationDef) presentation);
            editorForCreation.open((AdsSelectorPresentationDef) presentation);
        }
        isUpdate = false;

        titleEditor.open(presentation);
        defaultSizePanel.open(presentation);
        update();
        setupUI();
    }

    public void updateTitles(final AdsPresentationDef reference) {
        titleEditor.open(new HandleInfo() {
            @Override
            public AdsDefinition getAdsDefinition() {
                if (CommonPropertiesPanel.this.isTitleInherited) {
                    return reference != null ? reference : CommonPropertiesPanel.this.presentation;
                }
                return CommonPropertiesPanel.this.presentation;
            }

            @Override
            public Id getTitleId() {
                if (CommonPropertiesPanel.this.isTitleInherited) {
                    return reference != null ? reference.getTitleId() : presentation.getOwnerClass().getTitleId();
                }
                return CommonPropertiesPanel.this.presentation.getTitleId();
            }

            @Override
            protected void onAdsMultilingualStringDefChange(IMultilingualStringDef multilingualStringDef) {
                if (CommonPropertiesPanel.this.presentation != null) {
                    if (multilingualStringDef != null) {
                        CommonPropertiesPanel.this.presentation.setTitleId(multilingualStringDef.getId());
                    } else {
                        CommonPropertiesPanel.this.presentation.setTitleId(null);
                    }
                }
            }

            @Override
            protected void onLanguagesPatternChange(EIsoLanguage language, String newStringValue) {
                this.getAdsMultilingualStringDef().setValue(language, newStringValue);
            }
        });
        titleEditor.setReadonly(readonly || isTitleInherited);
    }

    private void updateInheritanceCheckBoxState(javax.swing.JCheckBox box, boolean inherits, boolean definedRef) {
        if (inherits && !definedRef) {
            box.setForeground(Color.RED);
        } else {
            box.setForeground(defaultForeground);
        }
        if (inherits) {
            box.setEnabled(!readonly);
        } else {
            box.setEnabled(definedRef && !readonly);
        }
    }

    @SuppressWarnings("unchecked")
    public void update() {
        isUpdate = true;

        readonly = presentation.isReadOnly();
        isTitleInherited = presentation.isTitleInherited();
        boolean isSelector = presentation.getClass().equals(AdsSelectorPresentationDef.class);
        final AdsPresentationDef reference = isSelector ? ((AdsSelectorPresentationDef) presentation).findBaseSelectorPresentation().get() : ((AdsEditorPresentationDef) presentation).findBaseEditorPresentation().get();
        AdsDefinition overwritten = presentation.getHierarchy().findOverwritten().get();

        boolean definedRef = reference != null || overwritten != null;
        titleEditor.update();

        inheritIcon.setSelected(presentation.isIconInherited());
        inheritIcon.setEnabled(!readonly);
        iconEditor.open(presentation, presentation.getIconId());
        iconEditor.setReadonly(readonly || presentation.isIconInherited());

        if (presentation.getClass().equals(AdsEditorPresentationDef.class)) {
            boolean childrenInherited = ((AdsEditorPresentationDef) presentation).isExplorerItemsInherited();
            inheritChildren.setSelected(childrenInherited);
            updateInheritanceCheckBoxState(inheritChildren, childrenInherited, definedRef);

            IFilter filter = AdsFilters.newBasePresentationFilter(presentation);
            List<AdsEditorPresentationDef> pr = presentation.getOwnerClass().getPresentations().getEditorPresentations().get(EScope.ALL, filter);
            updateBasePresentationValues(pr);
            baseEditor.setEnabled(!readonly && pr.size() > 0);

            final Id replaceId = ((AdsEditorPresentationDef) presentation).getReplacedEditorPresentationId();
            List<AdsEditorPresentationDef> replaceList = presentation.getOwnerClass().getPresentations().getEditorPresentations().get(EScope.ALL, new IFilter<AdsEditorPresentationDef>() {
                @Override
                public boolean isTarget(AdsEditorPresentationDef radixObject) {
                    AdsEditorPresentationDef r = radixObject.findReplacedEditorPresentation().get();
                    while (r != null) {
                        if (r.getId() == presentation.getId()) {
                            return false;
                        }
                        r = r.findReplacedEditorPresentation().get();
                    }
                    return true;
                }
            });

            if (replaceList.contains((AdsEditorPresentationDef) presentation)) {
                replaceList.remove((AdsEditorPresentationDef) presentation);
            }
            replacedEditor.setComboBoxValues(replaceList, true);
            replacedEditor.setEnabled(!readonly && !replaceList.isEmpty());
            replacedEditor.open(((AdsEditorPresentationDef) presentation).findReplacedEditorPresentation().get(), replaceId);

            boolean pagesInherited = ((AdsEditorPresentationDef) presentation).isEditorPagesInherited();
            inheritEditorPages.setSelected(pagesInherited);
            updateInheritanceCheckBoxState(inheritEditorPages, pagesInherited, definedRef);

            inheritRightsInheritanceMode.setSelected(((AdsEditorPresentationDef) presentation).isRightsInheritanceModeInherited());
            updateInheritanceCheckBoxState(inheritRightsInheritanceMode, ((AdsEditorPresentationDef) presentation).isRightsInheritanceModeInherited(), definedRef);
            rightInheritancePanel.open((AdsEditorPresentationDef) presentation);
        }
        inheritCustomView.setSelected(presentation.isCustomViewInherited());
        updateInheritanceCheckBoxState(inheritCustomView, presentation.isCustomViewInherited(), definedRef);
        useCustomView.setSelected(presentation instanceof AdsEditorPresentationDef ? ((AdsEditorPresentationDef) presentation).getCustomViewSupport().isUseCustomView(ERuntimeEnvironmentType.EXPLORER) : ((AdsSelectorPresentationDef) presentation).getCustomViewSupport().isUseCustomView(ERuntimeEnvironmentType.EXPLORER));

        ICustomViewable cv = (ICustomViewable) presentation;

        boolean isExplorerViewAvailable = presentation instanceof AdsEditorPresentationDef ? ((AdsEditorPresentationDef) presentation).getClientEnvironment() != ERuntimeEnvironmentType.WEB : ((AdsSelectorPresentationDef) presentation).getClientEnvironment() != ERuntimeEnvironmentType.WEB;


        boolean isIncorrectExplorerView = !isExplorerViewAvailable && cv.getCustomViewSupport().isUseCustomView(ERuntimeEnvironmentType.EXPLORER);
        useCustomView.setEnabled(!readonly && !presentation.isCustomViewInherited() && (isExplorerViewAvailable || isIncorrectExplorerView));
        if (isIncorrectExplorerView) {
            useCustomView.setForeground(Color.red);
        } else {
            useCustomView.setForeground(Color.black);
        }



        useWebCustomView.setSelected(presentation instanceof AdsEditorPresentationDef ? ((AdsEditorPresentationDef) presentation).getCustomViewSupport().isUseCustomView(ERuntimeEnvironmentType.WEB) : ((AdsSelectorPresentationDef) presentation).getCustomViewSupport().isUseCustomView(ERuntimeEnvironmentType.WEB));

        boolean isWebViewAvailable = presentation instanceof AdsEditorPresentationDef ? ((AdsEditorPresentationDef) presentation).getClientEnvironment() != ERuntimeEnvironmentType.EXPLORER : ((AdsSelectorPresentationDef) presentation).getClientEnvironment() != ERuntimeEnvironmentType.EXPLORER;

        boolean isIncorrectWebView = !isWebViewAvailable && cv.getCustomViewSupport().isUseCustomView(ERuntimeEnvironmentType.WEB);
        useWebCustomView.setEnabled(!readonly && !presentation.isCustomViewInherited() && (isWebViewAvailable || isIncorrectWebView));
        if (isIncorrectWebView) {
            useWebCustomView.setForeground(Color.red);
        } else {
            useWebCustomView.setForeground(Color.black);
        }



        if (isSelector) {
            AdsSelectorPresentationDef asSelector = (AdsSelectorPresentationDef) presentation;

            List<AdsSelectorPresentationDef> pr = presentation.getOwnerClass().getPresentations().getSelectorPresentations().get(EScope.ALL, AdsFilters.newBasePresentationFilter(presentation));
            updateBasePresentationValues(pr);
            baseEditor.setEnabled(!readonly);

            //  editorForCreation.setComboBoxValues(asSelector.getOwnerClass().getPresentations().getEditorPresentations().get(EScope.ALL), true);
            classCatalogEditor.setComboBoxValues(asSelector.getOwnerClass().getPresentations().getClassCatalogs().get(EScope.ALL), true);

            editorPresentationsEditor.update();
            //editorForCreation.open(asSelector.findEditorPresentationForCreation(), asSelector.getCreationEditorPresentationId());
            editorForCreation.update();
            //editorForCreation.setEnabled(!readonly);
            inheritClassCatalog.setSelected(asSelector.isCreationClassCatalogInherited());
            boolean disabled = asSelector.isCreationClassCatalogInherited() && !definedRef;
            if (disabled) {
                inheritClassCatalog.setForeground(Color.RED);
            } else {
                inheritClassCatalog.setForeground(defaultForeground);
            }
            if (asSelector.isCreationClassCatalogInherited()) {
                inheritClassCatalog.setEnabled(!readonly);
            } else {
                inheritClassCatalog.setEnabled(!readonly && definedRef);
            }
            classCatalogEditor.open(asSelector.findCreationClassCatalog().get(), asSelector.getCreationClassCatalogId());
            classCatalogEditor.setEnabled(!readonly && !asSelector.isCreationClassCatalogInherited());
            isAutoSortInstantiatableClassCatalog.setEnabled(!readonly && !asSelector.isCreationClassCatalogInherited());
            isAutoSortInstantiatableClassCatalog.setSelected(asSelector.isAutoSortClasses());

            cbAutoExpand.setVisible(true);
            cbRestorePosition.setVisible(true);

            cbAutoExpand.setEnabled(!readonly);
            cbRestorePosition.setEnabled(!readonly);

            cbAutoExpand.setSelected(asSelector.isAutoExpandEnabled());
            cbRestorePosition.setSelected(asSelector.isRestorePositionEnabled());
        } else {
            cbAutoExpand.setVisible(false);
            cbRestorePosition.setVisible(false);

        }
        envSelectorPanel.open(presentation);
        envSelectorPanel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!isUpdate) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            if (!isUpdate) {
                                update();
                            }
                        }
                    });
                }
            }
        });


        baseEditor.open(reference, presentation.getBasePresentationId());

        updateUseDefaultModelBox();
//        useDefaultModelBox.setEnabled(!readonly);
//        useDefaultModelBox.setSelected(presentation.isUseDefaultModel());

        accessPanel.open(presentation);
        defaultSizePanel.update();
        isUpdate = false;
    }

    private void updateBasePresentationValues(List<? extends AdsPresentationDef> pr) {
        if (pr.contains(presentation)) {
            boolean removed = false;
            int i = 0;
            while (!removed && i <= pr.size() - 1) {
                if (pr.get(i).equals(presentation)) {
                    removed = true;
                    pr.remove(i);
                } else {
                    i++;
                }
            }
        }
        baseEditor.setComboBoxValues(pr, true);
    }
    //UI setup

    private javax.swing.JPanel setupBaseProps() {
        javax.swing.JPanel base = new javax.swing.JPanel();
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        base.setLayout(gbl);

        c.anchor = GridBagConstraints.NORTH;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 10, 10, 10);
        gbl.setConstraints(titleEditor, c);
        base.add(titleEditor);

        return base;
    }

    private javax.swing.JPanel setupCommonProps() {
        javax.swing.JPanel common = new javax.swing.JPanel();
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        common.setLayout(gbl);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 10, 10, 0);
        if (presentation instanceof AdsSelectorPresentationDef) {
            baseLabel = new javax.swing.JLabel(NbBundle.getMessage(CommonPropertiesPanel.class, "PresentationsSelector-BaseTip"));
        }
        gbl.setConstraints(baseLabel, c);
        common.add(baseLabel);
        c.weightx = 1.0;
        c.gridx = 1;
        c.gridwidth = 3;
        c.insets = new Insets(10, 10, 10, 10);
        gbl.setConstraints(baseEditor, c);
        common.add(baseEditor);

        AdsDefinition overridden = presentation.getHierarchy().findOverridden().get();
        AdsDefinition overwritten = presentation.getHierarchy().findOverwritten().get();
        boolean isOverSmth = overridden != null || overwritten != null;
        if (isOverSmth) {
            javax.swing.JLabel overLabel = new javax.swing.JLabel();
            if (overridden != null) {
                overLabel.setText(NbBundle.getMessage(CommonPropertiesPanel.class, "OverriddenPresentation"));
                overEditor.open(overridden, overridden.getId());
            } else {
                overLabel.setText(NbBundle.getMessage(CommonPropertiesPanel.class, "OverwrittenPresentation"));
                overEditor.open(overwritten, overwritten.getId());
            }
            c.gridx = 0;
            c.gridy = 1;
            c.gridwidth = 1;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.insets = new Insets(0, 10, 10, 0);
            c.weightx = 0.0;
            gbl.setConstraints(overLabel, c);
            common.add(overLabel);
            c.gridx = 1;
            c.weightx = 1.0;
            c.gridwidth = 3;
            c.insets = new Insets(0, 10, 10, 10);
            gbl.setConstraints(overEditor, c);
            common.add(overEditor);
            c.gridy = 2;
        } else {
            c.gridy = 1;
        }

        boolean isEditor = presentation.getClass().equals(AdsEditorPresentationDef.class);
        final int defaultSizeGridY;
        if (isEditor) {
            c.gridx = 1;
            c.gridwidth = 1;
            c.weightx = 0.0;
            c.insets = new Insets(0, 10, 10, 10);
            c.fill = GridBagConstraints.HORIZONTAL;
            gbl.setConstraints(inheritRightsInheritanceMode, c);
            common.add(inheritRightsInheritanceMode);

            c.gridx = 0;
            c.gridy = isOverSmth ? 3 : 2;
            c.weightx = 0.0;
            c.insets = new Insets(0, 10, 10, 0);
            c.fill = GridBagConstraints.HORIZONTAL;
            gbl.setConstraints(rightsInheritanceLabel, c);
            common.add(rightsInheritanceLabel);

            c.gridx = 1;
            c.gridwidth = 3;
            c.weightx = 1.0;
            c.insets = new Insets(0, 10, 10, 10);
            gbl.setConstraints(rightInheritancePanel, c);
            common.add(rightInheritancePanel);

            c.gridx = 0;
            c.gridwidth = 1;
            c.weightx = 0.0;
            c.gridy = isOverSmth ? 4 : 3;
            c.insets = new Insets(0, 10, 10, 0);
            c.fill = GridBagConstraints.HORIZONTAL;
            gbl.setConstraints(replacedLable, c);
            common.add(replacedLable);

            c.gridwidth = 3;
            c.weightx = 1.0;
            c.gridx = 1;
            c.insets = new Insets(0, 10, 10, 10);
            gbl.setConstraints(replacedEditor, c);
            common.add(replacedEditor);

            c.weightx = 0.0;
            c.gridwidth = 1;
            c.gridy = isOverSmth ? 5 : 4;
            c.insets = new Insets(0, 10, 10, 10);
            defaultSizeGridY = c.gridy;
            gbl.setConstraints(inheritChildren, c);
            common.add(inheritChildren);
            c.gridy = isOverSmth ? 6 : 5;
        } else {
            JSplitPane splitter = new JSplitPane();
            c.gridwidth = 4;
            c.weightx = 1.0;
            c.gridx = 0;
            c.insets = new Insets(0, 10, 0, 10);
            c.gridy = isOverSmth ? 3 : 2;

            splitter.setTopComponent(editorPresentationsEditor);
            splitter.setBottomComponent(editorForCreation);
            splitter.setOrientation(JSplitPane.VERTICAL_SPLIT);
            splitter.setPreferredSize(new Dimension(200, 350));
            gbl.setConstraints(splitter, c);
            common.add(splitter);
//            gbl.setConstraints(editorPresentationsEditor, c);
//            common.add(editorPresentationsEditor);


//            c.weightx = 1.0;
//            c.insets = new Insets(0, 10, 10, 10);
//            c.gridx = 0;
//            c.gridy = isOverSmth ? 4 : 3;
//            c.gridwidth = 4;
//            gbl.setConstraints(editorForCreation, c);
//            common.add(editorForCreation);


            c.gridy = isOverSmth ? 5 : 4;
            c.gridx = 1;
            c.weightx = 1.0;
            c.gridwidth = 1;
            c.insets = new Insets(0, 10, 10, 10);
            gbl.setConstraints(inheritClassCatalog, c);
            common.add(inheritClassCatalog);
            
            
            
            
            c.gridy = isOverSmth ? 6 : 5;
            c.gridx = 0;
            c.weightx = 0.0;
            c.insets = new Insets(0, 10, 10, 0);
            c.gridwidth = 1;
            gbl.setConstraints(classCatalogLabel, c);
            common.add(classCatalogLabel);
            c.gridx = 1;
            c.insets = new Insets(0, 10, 10, 10);
            c.gridwidth = 3;
            
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
            panel.add(classCatalogEditor);
            panel.add(isAutoSortInstantiatableClassCatalog);
            isAutoSortInstantiatableClassCatalog.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
            gbl.setConstraints(panel, c);
            common.add(panel);

            c.gridwidth = 1;
            c.gridy = isOverSmth ? 7 : 6;
            defaultSizeGridY = c.gridy;
        }
        c.gridx = 1;
        c.weightx = 0.0;
        c.insets = new Insets(0, 10, 10, 10);
        gbl.setConstraints(inheritCustomView, c);
        common.add(inheritCustomView);

        c.gridx = 2;
        c.gridheight = isEditor ? 5 : 4;
        int tmp = c.gridy;
        c.gridy = defaultSizeGridY;
        gbl.setConstraints(defaultSizePanel, c);
        common.add(defaultSizePanel);

        c.gridx = 3;
        c.weightx = 0.0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.WEST;
        gbl.setConstraints(iconEditor, c);

        CompoundBorder iBorder = new CompoundBorder(BorderFactory.createEtchedBorder(), BorderFactory.createEmptyBorder(0, 10, 10, 10));
        ComponentTitledBorder iconsBorder = new ComponentTitledBorder(inheritIcon, iconEditor, iBorder);
        iconEditor.setBorder(iconsBorder);
        common.add(iconEditor);


        c.gridy = tmp;
        c.gridheight = 1;
        c.gridx = 1;
        c.gridy++;
        gbl.setConstraints(useCustomView, c);
        common.add(useCustomView);
        c.gridy++;
        gbl.setConstraints(useWebCustomView, c);
        common.add(useWebCustomView);


        if (isEditor) {
            c.gridy++;
            c.gridx = 1;
            gbl.setConstraints(inheritEditorPages, c);
            common.add(inheritEditorPages);
        }

        for (int i = 0; i < (isEditor ? 2 : 1); i++) {
            final JLabel ph = new JLabel("");
            c.gridy++;
            gbl.setConstraints(ph, c);
            common.add(ph);
        }

        // <--------------- client env selector
        c.gridy++;
        JLabel label = new JLabel("Client Environment:");
        c.gridwidth = 2;
        c.gridx = 0;
        gbl.setConstraints(label, c);
        common.add(label);
        c.gridx = 1;
        gbl.setConstraints(envSelectorPanel, c);
        common.add(envSelectorPanel);
        c.gridy++;
        // client env selector ------------->        



        c.gridx = 1;
        gbl.setConstraints(useDefaultModelBox, c);
        common.add(useDefaultModelBox);
        if (!isEditor) {
            c.gridy++;
            c.gridx = 1;
            c.gridwidth = 1;
            gbl.setConstraints(cbAutoExpand, c);
            common.add(cbAutoExpand);

            c.gridx = 2;
            gbl.setConstraints(cbRestorePosition, c);
            common.add(cbRestorePosition);
        }

        c.gridx = 0;
//        if (isEditor){
//            c.gridy = isOverSmth ? 7 : 6;
//        } else {
        c.gridy++;
//        }
        gbl.setConstraints(accessLabel, c);
        common.add(accessLabel);

        c.gridx = 1;
        gbl.setConstraints(accessPanel, c);
        common.add(accessPanel);
        return common;
    }

    private void setupUI() {
        removeAll();
        javax.swing.JPanel baseProps = setupBaseProps();
        javax.swing.JPanel commonProps = setupCommonProps();

        javax.swing.JPanel content = new javax.swing.JPanel();
//        boolean isSelector = presentation instanceof AdsSelectorPresentationDef;

        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        content.setLayout(gbl);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        gbl.setConstraints(commonProps, c);
        content.add(commonProps);
        c.gridy = 1;
//        if (!isSelector) {
//            c.insets = new Insets(10, 10, 0, 10);
//            gbl.setConstraints(envSelectorPanel, c);
//            content.add(envSelectorPanel);
//            c.gridy++;
//        }
        c.insets = new Insets(0, 0, 0, 0);
        gbl.setConstraints(baseProps, c);
        content.add(baseProps);
        setLayout(new BorderLayout());
        add(content, BorderLayout.NORTH);
    }

    //listeners
    private void onInheritCustomViewChange() {
        if (!isUpdate) {
            boolean isSelected = inheritCustomView.isSelected();
            presentation.setCustomViewInherited(isSelected);

            final AdsPresentationDef reference = presentation instanceof AdsSelectorPresentationDef ? ((AdsSelectorPresentationDef) presentation).findBaseSelectorPresentation().get() : ((AdsEditorPresentationDef) presentation).findBaseEditorPresentation().get();
            AdsDefinition ovr = presentation.getHierarchy().findOverwritten().get();
            boolean definedRef = reference != null || ovr != null;

            boolean useExplorer = false;
            if (presentation instanceof AdsEditorPresentationDef) {
                useExplorer = ((AdsEditorPresentationDef) presentation).getCustomViewSupport().isUseCustomView(ERuntimeEnvironmentType.EXPLORER);
            } else {
                useExplorer = ((AdsSelectorPresentationDef) presentation).getCustomViewSupport().isUseCustomView(ERuntimeEnvironmentType.EXPLORER);
            }
            boolean useWeb = false;
            if (presentation instanceof AdsEditorPresentationDef) {
                useWeb = ((AdsEditorPresentationDef) presentation).getCustomViewSupport().isUseCustomView(ERuntimeEnvironmentType.WEB);
            } else {
                useWeb = ((AdsSelectorPresentationDef) presentation).getCustomViewSupport().isUseCustomView(ERuntimeEnvironmentType.WEB);
            }

            updateInheritanceCheckBoxState(inheritCustomView, isUpdate, definedRef);

            useCustomView.setSelected(useExplorer);
            useCustomView.setEnabled(!readonly && !isSelected);
            useWebCustomView.setSelected(useWeb);
            useWebCustomView.setEnabled(!readonly && !isSelected);

        }
    }

    private void onUseCustomView(ERuntimeEnvironmentType environmentType, JCheckBox checkBox) {
        if (!isUpdate) {
            if (!setCustomView(environmentType, checkBox.isSelected())) {
                isUpdate = true;
                checkBox.setSelected(true);
                isUpdate = false;
            }
        }
        updateUseDefaultModelBox();
    }

    private void updateUseDefaultModelBox() {
        isUpdate = true;

        if (presentation.isOwnModelAllowed()) {
            if (useCustomView.isSelected() || useWebCustomView.isSelected()) {
                if (!presentation.isCustomViewInherited()) {
                    presentation.setUseDefaultModel(false);
                    useDefaultModelBox.setEnabled(false);
                    useDefaultModelBox.setSelected(false);
                }
            } else {
                useDefaultModelBox.setSelected(presentation.isUseDefaultModel());
                useDefaultModelBox.setEnabled(!readonly);
            }
        } else {
            boolean use = !presentation.isUseDefaultModel();
            if (use && !presentation.isReadOnly()) {
                useDefaultModelBox.setSelected(false);
                useDefaultModelBox.setEnabled(true);
                useDefaultModelBox.setForeground(Color.red);
            } else {
                useDefaultModelBox.setSelected(true);
                useDefaultModelBox.setEnabled(false);

            }

        }

        isUpdate = false;

    }

    private boolean setCustomView(ERuntimeEnvironmentType type, boolean select) {

        if (select || DialogUtils.messageConfirmation(NbBundle.getMessage(CommonPropertiesPanel.class, "DeleteCustomViewTip"))) {

            if (presentation instanceof ICustomViewable) {
                ((ICustomViewable) presentation).getCustomViewSupport().setUseCustomView(type, select);
                return true;
            }
        }
        return false;
    }

    private boolean isEmptyModel(AdsModelClassDef model) {
        if (model != null) {
            if (!model.getProperties().get(EScope.LOCAL).isEmpty()) {
                return false;
            }

            if (!model.getMethods().get(EScope.LOCAL).isEmpty()) {
                return false;
            }

            if (!model.getNestedClasses().get(EScope.LOCAL).isEmpty()) {
                return false;
            }

            if (!model.getInheritance().getInerfaceRefList(EScope.LOCAL).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private void setDefaultModel(boolean select) {

        if (select) {
            if (!isEmptyModel(presentation.getModel())) {
                if (!DialogUtils.messageConfirmation(NbBundle.getMessage(CommonPropertiesPanel.class, "CommonPropertiesPanel.SetDefaultModel"))) {
                    useDefaultModelBox.setSelected(!select);
                    return;
                }
            }
        }

        presentation.setUseDefaultModel(select);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                updateUseDefaultModelBox();
            }
        });
    }

    private void onInheritIconChange() {
        if (!isUpdate) {
            boolean isSelected = inheritIcon.isSelected();
            presentation.setIconInherited(isSelected);
            iconEditor.open(presentation, presentation.getIconId());
            iconEditor.setReadonly(isSelected || readonly);
        }
    }

    private void onInheritChildrenChange() {
        if (!isUpdate) {
            boolean isSelected = inheritChildren.isSelected();
            ((AdsEditorPresentationDef) presentation).setExplorerItemsInherited(isSelected);
        }
    }

    private void onBasePresentationChange() {
        if (!isUpdate && presentation != null) {
            Id currentBaseId = presentation.getBasePresentationId();
            Id newBaseId = baseEditor.getDefinitionId();
            boolean shoudChangeInheritance = (currentBaseId != null && newBaseId == null)
                    || (currentBaseId == null && newBaseId != null);
            if (shoudChangeInheritance) {
                boolean inherit = newBaseId != null;
                presentation.setRestrictionsInherited(inherit);
                presentation.setTitleInherited(inherit);
                presentation.setIconInherited(inherit);
                presentation.setCustomViewInherited(inherit);
                if (presentation instanceof AdsEditorPresentationDef) {
                    ((AdsEditorPresentationDef) presentation).setExplorerItemsInherited(inherit);
                    ((AdsEditorPresentationDef) presentation).setEditorPagesInherited(inherit);
                    ((AdsEditorPresentationDef) presentation).setObjectTitleFormatInherited(inherit);
                    ((AdsEditorPresentationDef) presentation).setPropertyPresentationAttributesInherited(inherit);
                } else {
                    ((AdsSelectorPresentationDef) presentation).setCreationClassCatalogInherited(inherit);
                    ((AdsSelectorPresentationDef) presentation).setColumnsInherited(inherit);
                    ((AdsSelectorPresentationDef) presentation).setConditionInherited(inherit);
                    ((AdsSelectorPresentationDef) presentation).setAddonsInherited(inherit);
                }
            }
            boolean res = presentation.setBasePresentationId(newBaseId);
            update();
            changeSupport.fireChange();
        }
    }

    private void onReplacePresentationId() {
        if (!isUpdate && presentation != null) {
            Id newId = replacedEditor.getDefinitionId();
            ((AdsEditorPresentationDef) presentation).setReplacedEditorPresentationId(newId);
            rightInheritancePanel.update();
        }
    }

//NO LONGER USABLE    private void onEditorForCreationChange() {
//        if (!isUpdate && presentation != null) {
//            ((AdsSelectorPresentationDef) presentation).setCreationEditorPresentationId(editorForCreation.getDefinitionId());
//        }
//    }
    private void onClassCataglogChange() {
        if (!isUpdate && presentation != null) {
            ((AdsSelectorPresentationDef) presentation).setCreationClassCatalogId(classCatalogEditor.getDefinitionId());
        }
    }
    
    private void onAutoSortClassesChange(){
        if (!isUpdate && presentation != null) {
            ((AdsSelectorPresentationDef) presentation).setAutoSortClasses(isAutoSortInstantiatableClassCatalog.isSelected());
        }
    }

    private void onClassCatalogInheritanceChange() {
        if (!isUpdate && presentation != null) {
            boolean isSelected = inheritClassCatalog.isSelected();
            AdsSelectorPresentationDef asSelector = (AdsSelectorPresentationDef) presentation;
            asSelector.setCreationClassCatalogInherited(isSelected);
            classCatalogEditor.open(asSelector.findCreationClassCatalog().get(), asSelector.getCreationClassCatalogId());
            classCatalogEditor.setEnabled(!isSelected);
            isAutoSortInstantiatableClassCatalog.setEnabled(!isSelected);
            isAutoSortInstantiatableClassCatalog.setSelected(asSelector.isAutoSortClasses());
            if (!isSelected) {
                inheritClassCatalog.setEnabled(asSelector.getBasePresentationId() != null || asSelector.getHierarchy().findOverwritten().get() != null);
                inheritClassCatalog.setForeground(defaultForeground);
            }
        }
    }

    private void onInheritEditorPagesChange() {
        if (!isUpdate) {
            boolean isSelected = inheritEditorPages.isSelected();
            ((AdsEditorPresentationDef) presentation).setEditorPagesInherited(isSelected);
        }
    }
    private ChangeSupport changeSupport = new ChangeSupport(this);

    public void addChangeListener(ChangeListener l) {
        changeSupport.addChangeListener(l);
    }

    public void removeChangeListener(ChangeListener l) {
        changeSupport.removeChangeListener(l);
    }
}
