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
package org.radixware.kernel.designer.ads.editors.filters.parameter;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsFilterDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsEnumType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.choosetype.ChooseType;
import org.radixware.kernel.designer.common.dialogs.choosetype.ITypeFilter;
import org.radixware.kernel.designer.common.dialogs.choosetype.TypeEditDisplayer;
import org.radixware.kernel.designer.common.dialogs.choosetype.TypeEditorModel;
import org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel;
import org.radixware.kernel.designer.common.dialogs.components.ValAsStrEditPanel;
import org.radixware.kernel.designer.common.dialogs.components.localizing.HandleInfo;

public class FilterParameterEditorPanel extends javax.swing.JPanel {

    private boolean isUpdating;
    private JButton setupTypeButton;
    private static final String undefined = "<undefined>";
    private HandleInfo handleInfo;
    private JTextField textfieldForUndefinedValAsStr;
    private DefinitionLinkEditPanel itemsPanel = null;
    private boolean isEnumTypeDef;
    private AdsFilterDef.Parameter filterParameter;

    protected FilterParameterEditorPanel() {
        initComponents();
//        initAdditionalComponents();
        typeExtendableTextField.setEditable(false);
        setupTypeButton = typeExtendableTextField.addButton();
        setupTypeButton.setIcon(RadixWareIcons.DIALOG.CHOOSE.getIcon(13, 13));
        setupTypeButton.setToolTipText("Choose Type");
        setupTypeButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (filterParameter != null) {
                    final TypeEditDisplayer typeEditDisplayer = new TypeEditDisplayer();
                    final AdsTypeDeclaration newTypeDeclaration = typeEditDisplayer.editType(filterParameter.getType(), filterParameter, filterParameter);

                    if (newTypeDeclaration != null) {
                        filterParameter.setType(newTypeDeclaration);
                        filterParameter.setDefaultValue(null); //reset
                        updateJPanel2(newTypeDeclaration);
                    }
                }
            }
        });

        defaultValueValAsStrEditPanel.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if (filterParameter != null) {
                    if (isUpdating) {
                        return;
                    }
                    filterParameter.setDefaultValue(defaultValueValAsStrEditPanel.getValue());
                }
            }
        });

        textfieldForUndefinedValAsStr = new JTextField(undefined) {

            @Override
            public void paste() {
                //do nothing preventing component from getting incorrect data
            }
        };
//        textfieldForUndefinedValAsStr.setText(undefined);
        textfieldForUndefinedValAsStr.setEditable(false);
    }

    private void initHandleInfo() {
        final AdsFilterDef.Parameter parameter = filterParameter;
        handleInfo = new HandleInfo() {

            @Override
            public AdsDefinition getAdsDefinition() {
                return parameter;
            }

            @Override
            public Id getTitleId() {
                return parameter.getTitleId();
            }

            @Override
            public void onAdsMultilingualStringDefChange(IMultilingualStringDef adsMultilingualStringDef) {
                if (adsMultilingualStringDef != null) {
                    parameter.setTitleId(adsMultilingualStringDef.getId());
                } else {
                    parameter.setTitleId(null);
                }
            }

            @Override
            public void onLanguagesPatternChange(EIsoLanguage language, String newStringValue) {
                getAdsMultilingualStringDef().setValue(language, newStringValue);
            }
        };

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

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        typeExtendableTextField = new org.radixware.kernel.common.components.ExtendableTextField();
        jPanel2 = new javax.swing.JPanel();
        defaultValueValAsStrEditPanel = new org.radixware.kernel.designer.common.dialogs.components.ValAsStrEditPanel();
        jLabel1 = new javax.swing.JLabel();
        accessPanel = new org.radixware.kernel.designer.ads.common.dialogs.AccessEditPanel();
        localizingPaneList1 = new org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingEditorPanel(){
            @Override
            public Dimension getMaximumSize() {
                return new Dimension(super.getMaximumSize().width, getPreferredSize().height);
            }
        }
        ;
        editOptionsPanel = new org.radixware.kernel.designer.common.editors.EditOptionsPanel();
        descriptionPanel = new org.radixware.kernel.designer.common.dialogs.components.DescriptionPanel();
        parentSelectorPresentationPanel = new org.radixware.kernel.designer.ads.editors.property.ParentSelectorPresentationPanel();

        setLayout(new java.awt.GridBagLayout());

        jPanel1.setAlignmentX(0.0F);
        jPanel1.setPreferredSize(new java.awt.Dimension(90, 105));
        jPanel1.setRequestFocusEnabled(false);
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel2.setText(org.openide.util.NbBundle.getMessage(FilterParameterEditorPanel.class, "FilterParameterEditorPanel.jLabel2.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        jPanel1.add(jLabel2, gridBagConstraints);

        jLabel3.setText(org.openide.util.NbBundle.getMessage(FilterParameterEditorPanel.class, "FilterParameterEditorPanel.jLabel3.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        jPanel1.add(jLabel3, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 10, 0);
        jPanel1.add(typeExtendableTextField, gridBagConstraints);

        jPanel2.setLayout(new java.awt.BorderLayout());
        jPanel2.add(defaultValueValAsStrEditPanel, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 10, 0);
        jPanel1.add(jPanel2, gridBagConstraints);

        jLabel1.setText(org.openide.util.NbBundle.getMessage(FilterParameterEditorPanel.class, "AccessibilityTip")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jLabel1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanel1.add(accessPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 10, 10);
        add(jPanel1, gridBagConstraints);

        localizingPaneList1.setAlignmentY(0.5F);
        localizingPaneList1.setMaximumSize(new java.awt.Dimension(32768, 32768));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 10, 10);
        add(localizingPaneList1, gridBagConstraints);

        editOptionsPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 0, 0, 0));
        editOptionsPanel.setAlignmentX(0.0F);
        editOptionsPanel.setMaximumSize(new java.awt.Dimension(32767, 32767));
        editOptionsPanel.setMinimumSize(new java.awt.Dimension(90, 200));
        editOptionsPanel.setPreferredSize(new java.awt.Dimension(90, 200));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 10, 10);
        add(editOptionsPanel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(descriptionPanel, gridBagConstraints);

        parentSelectorPresentationPanel.setAlignmentX(0.0F);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 11, 0, 10);
        add(parentSelectorPresentationPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    public void open(AdsFilterDef.Parameter filterParameter) {
        this.filterParameter = filterParameter;
        initHandleInfo();

        descriptionPanel.open(filterParameter);
        localizingPaneList1.open(handleInfo);
        final AdsTypeDeclaration currentTypeDeclaration = filterParameter.getType();
        editOptionsPanel.open(filterParameter, filterParameter.getEditOptions(), currentTypeDeclaration);
        parentSelectorPresentationPanel.open(filterParameter, filterParameter.getEditOptions());
        update();
    }

    private void updateJPanel2(AdsTypeDeclaration typeDeclaration) {

        final AdsFilterDef.Parameter parameter = filterParameter;
        jPanel2.removeAll();

        if (typeDeclaration == null || typeDeclaration == AdsTypeDeclaration.UNDEFINED) {
            isEnumTypeDef = false;
            typeExtendableTextField.setValue(undefined);
            jPanel2.add(textfieldForUndefinedValAsStr);
        } else {
            typeExtendableTextField.setValue(typeDeclaration.getName(parameter.getOwnerDefinition()));
            final AdsType resolvedType = typeDeclaration.resolve(parameter).get();
            if (resolvedType instanceof AdsEnumType) {
                isEnumTypeDef = true;
                final AdsEnumType adsEnumType = (AdsEnumType) resolvedType;
                final AdsEnumDef source = adsEnumType.getSource();
                final List<AdsEnumItemDef> itemsList = source.getItems().get(EScope.ALL);

                if (itemsPanel == null) {
                    itemsPanel = new DefinitionLinkEditPanel();
                    itemsPanel.setComboMode(parameter);
                    itemsPanel.addChangeListener(new ChangeListener() {

                        @Override
                        public void stateChanged(ChangeEvent e) {

//                            SwingUtilities.invokeLater(new Runnable() {
//
//                                @Override
//                                public void run() {
                            if (isUpdating) {
                                return;
                            }
                            final Definition selectedDef = itemsPanel.getDefinition();
                            if (selectedDef == null) {
                                parameter.setDefaultValue(null);
                            } else {
                                final AdsEnumItemDef selectedItem = (AdsEnumItemDef) itemsPanel.getDefinition();
                                parameter.setDefaultValue(selectedItem.getValue());
                            }
//                                }
//                            });
                        }
                    });
                }
                itemsPanel.setComboBoxValues(itemsList, true);
                final ValAsStr valAsStr = parameter.getDefaultValue();
                Definition selectedItem = null;
                for (AdsEnumItemDef xItem : itemsList) {
                    if (xItem.getValue() == valAsStr) {
                        selectedItem = xItem;
                        break;
                    }
                }
                itemsPanel.open(selectedItem, selectedItem != null ? selectedItem.getId() : null);
                itemsPanel.update();
                jPanel2.add(itemsPanel);
            } else {
                isEnumTypeDef = false;
                defaultValueValAsStrEditPanel.setValue(ValAsStrEditPanel.getValTypeForArgument(parameter.getType().getTypeId()), findTableType(parameter.getType()), parameter.getDefaultValue());
                jPanel2.add(defaultValueValAsStrEditPanel);
            }
        }

        jPanel2.revalidate();
        jPanel2.repaint();
    }

    private DdsTableDef findTableType(AdsTypeDeclaration decl) {
        AdsType resolved = decl.resolve(filterParameter).get();
        if (resolved instanceof AdsClassType) {
            AdsClassDef clazz = ((AdsClassType) resolved).getSource();
            if (clazz instanceof AdsEntityObjectClassDef) {
                return ((AdsEntityObjectClassDef) clazz).findTable(filterParameter);
            }
        }
        return null;
    }

    public void update() {

        isUpdating = true;

        accessPanel.open(filterParameter);

        descriptionPanel.update();
        localizingPaneList1.update(handleInfo);
        final AdsFilterDef.Parameter parameter = filterParameter;
        final AdsTypeDeclaration typeDeclaration = parameter.getType();
        updateJPanel2(typeDeclaration);
        editOptionsPanel.update();
        setReadonly(parameter.isReadOnly());
        parentSelectorPresentationPanel.setReadOnly(parameter.getType().getTypeId() != EValType.PARENT_REF);
        parentSelectorPresentationPanel.update();
        isUpdating = false;
    }

    public void setReadonly(boolean readonly) {

        final AdsFilterDef.Parameter parameter = filterParameter;
        final AdsTypeDeclaration typeDeclaration = parameter.getType();

        if (!readonly) {
            //try to enable
            if (!parameter.isReadOnly()) {
                descriptionPanel.setEnabled(true);
                localizingPaneList1.setEnabled(true);

                setupTypeButton.setEnabled(true);
                if (isEnumTypeDef) {
                    assert (itemsPanel != null);
                    itemsPanel.setEnabled(typeDeclaration != null);
                } else {
                    assert (defaultValueValAsStrEditPanel != null);
                    defaultValueValAsStrEditPanel.setEnabled(typeDeclaration != null && typeDeclaration != AdsTypeDeclaration.UNDEFINED);
                }
                editOptionsPanel.setReadOnly(false);
            }
        } else {
            descriptionPanel.setEnabled(false);
            localizingPaneList1.setEnabled(false);

            setupTypeButton.setEnabled(false);
            if (isEnumTypeDef) {
                assert (itemsPanel != null);
                itemsPanel.setEnabled(false);
            } else {
                assert (defaultValueValAsStrEditPanel != null);
                defaultValueValAsStrEditPanel.setEnabled(typeDeclaration != null && typeDeclaration != AdsTypeDeclaration.UNDEFINED);
            }
            editOptionsPanel.setReadOnly(true);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.radixware.kernel.designer.ads.common.dialogs.AccessEditPanel accessPanel;
    private org.radixware.kernel.designer.common.dialogs.components.ValAsStrEditPanel defaultValueValAsStrEditPanel;
    private org.radixware.kernel.designer.common.dialogs.components.DescriptionPanel descriptionPanel;
    private org.radixware.kernel.designer.common.editors.EditOptionsPanel editOptionsPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingEditorPanel localizingPaneList1;
    private org.radixware.kernel.designer.ads.editors.property.ParentSelectorPresentationPanel parentSelectorPresentationPanel;
    private org.radixware.kernel.common.components.ExtendableTextField typeExtendableTextField;
    // End of variables declaration//GEN-END:variables
}
