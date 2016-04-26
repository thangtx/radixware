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
 * AdsFilterParameterCreatureWizardStep1Visual.java
 *
 * Created on Aug 24, 2009, 12:10:17 PM
 */
package org.radixware.kernel.designer.ads.editors.filters.parameter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import javax.swing.JButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsFilterDef;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.common.dialogs.choosetype.ChooseType;
import org.radixware.kernel.designer.common.dialogs.choosetype.ITypeFilter;
import org.radixware.kernel.designer.common.dialogs.choosetype.TypeEditDisplayer;
import org.radixware.kernel.designer.common.dialogs.choosetype.TypeEditorModel;
import org.radixware.kernel.designer.common.dialogs.components.state.StateManager;


public class AdsFilterParameterCreatureWizardStep1Visual extends javax.swing.JPanel {

    private StateManager stateManager = new StateManager(this);
    private AdsTypeDeclaration typeDecl = null;
    private AdsPropertyDef propertyDef = null;
    private AdsFilterDef.Parameter tempParam = null;
    private AdsFilterParameterCreature creature;
    private static final String undefinedValue = "<undefined>";

    /** Creates new form AdsFilterParameterCreatureWizardStep1Visual */
    public AdsFilterParameterCreatureWizardStep1Visual() {
        super();
        initComponents();
    }

    public void open(final AdsFilterParameterCreature creature) {
        this.creature = creature;
        initAdditionalComponents();
    }

    private void initAdditionalComponents() {

        nameEditPanel.setCurrentName(creature.getName());
        nameEditPanel.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                creature.setName(nameEditPanel.getCurrentName());
                changeSupport.fireChange();
            }
        });

        typeExtendableTextField.setEditable(false);
        final JButton setupTypeButton = typeExtendableTextField.addButton();
        setupTypeButton.setIcon(RadixWareIcons.DIALOG.CHOOSE.getIcon(13, 13));
        setupTypeButton.setToolTipText("Choose Type");
        setupTypeButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                assert (tempParam != null);
                TypeEditDisplayer typeEditDisplayer = new TypeEditDisplayer();
                typeDecl = typeEditDisplayer.editType(tempParam.getType(), tempParam.getOwnerDef(), tempParam);

                if (typeDecl != null && typeDecl != AdsTypeDeclaration.UNDEFINED) {
                    typeExtendableTextField.setValue(typeDecl.getQualifiedName(tempParam));
                } else {
                    typeExtendableTextField.setValue(undefinedValue);
                }
                creature.setTypeDecl(typeDecl);
                changeSupport.fireChange();
            }
        });

//        columnExtendableTextField.setEditable(false);
//        final JButton setupColumnButton = columnExtendableTextField.addButton();
//        setupColumnButton.setIcon(RadixWareIcons.DIALOG.CHOOSE.getIcon(13, 13));
//        setupColumnButton.setToolTipText("Choose Column");
//        setupColumnButton.addActionListener(new ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//                   assert(creature.getContainer().getContainer() instanceof AdsFilterDef);
//                   final Definition selectedDef = ChooseDefinition.chooseDefinition(ChooseDefinitionCfg.Factory.newInstance(((AdsFilterDef)creature.getContainer().getContainer()).getOwnerClass().getProperties().get(EScope.LOCAL_AND_OVERWRITE, new IFilter<AdsPropertyDef>(){
//
//                        @Override
//                        public boolean isTarget(AdsPropertyDef radixObject) {
//                            if (radixObject instanceof ColumnProperty) {
//                                final DdsColumnDef column = ((ColumnProperty) radixObject).getColumnInfo().findColumn();
//                                return column != null && (column.isGeneratedInDb() || column.isExpression());
//                            } else if (radixObject instanceof AdsFormPropertyDef) {
//                                final EValType type = ((AdsFormPropertyDef) radixObject).getValue().getType().getTypeId();
//                                return type != null && ValTypes.DDS_COLUMN_TYPES.contains(type);
//                            } else {
//                                return false;
//                            }
//                        }
//
//                    })));
//
//                if (selectedDef != null) {
//                    assert (selectedDef instanceof AdsPropertyDef);
//                    propertyDef = (AdsPropertyDef) selectedDef;
//                    columnExtendableTextField.setValue(propertyDef.getQualifiedName());
//                    nameEditPanel.setCurrentName(propertyDef.getName());
//                } else {
//                    columnExtendableTextField.setValue(undefinedValue);
//                    propertyDef = null;
//                }
//
//                creature.setProperty(propertyDef);
//                changeSupport.fireChange();
//            }
//        });

        typeExtendableTextField.setValue(undefinedValue);
//        columnExtendableTextField.setValue(undefinedValue);

        buttonGroup1.setSelected(selectTypeButton.getModel(), true);
    }

    public boolean isComplete() {

        stateManager.ok(); //reset that panel's stateManager
        boolean complete = nameEditPanel.isComplete();

        if (!complete) {
            return false;
        } else {
            if (!((typeDecl != null && typeDecl != AdsTypeDeclaration.UNDEFINED) ^ propertyDef != null)) {
                //must be selected either typeDecl or propertyDef but not both simultaneously
                stateManager.error("Type not selected");
                return false;
            } else {
                stateManager.ok();
                return true;
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        nameLabel = new javax.swing.JLabel();
        nameEditPanel = new org.radixware.kernel.designer.common.dialogs.components.NameEditPanel();
        selectTypeButton = new javax.swing.JRadioButton();
        typeExtendableTextField = new org.radixware.kernel.common.components.ExtendableTextField();

        nameLabel.setText(org.openide.util.NbBundle.getMessage(AdsFilterParameterCreatureWizardStep1Visual.class, "AdsFilterParameterCreatureWizardStep1Visual.nameLabel.text")); // NOI18N

        buttonGroup1.add(selectTypeButton);
        selectTypeButton.setText(org.openide.util.NbBundle.getMessage(AdsFilterParameterCreatureWizardStep1Visual.class, "AdsFilterParameterCreatureWizardStep1Visual.selectTypeButton.text")); // NOI18N
        selectTypeButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                selectTypeButtonItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(selectTypeButton)
                    .addComponent(nameLabel))
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nameEditPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE)
                    .addComponent(typeExtendableTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameEditPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nameLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(selectTypeButton)
                    .addComponent(typeExtendableTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(246, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void selectTypeButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_selectTypeButtonItemStateChanged

        if (evt.getStateChange() == ItemEvent.SELECTED) {
            tempParam = AdsFilterDef.Parameter.Factory.newTemporaryInstance(creature.getContainer());
            typeExtendableTextField.setEnabled(true);
//            columnExtendableTextField.setEnabled(false);
//            columnExtendableTextField.setValue(undefinedValue);
            creature.setProperty(null);
            propertyDef = null;
        } else {
            tempParam = null;
            typeExtendableTextField.setEnabled(false);
//            columnExtendableTextField.setEnabled(true);
        }
        changeSupport.fireChange();
    }//GEN-LAST:event_selectTypeButtonItemStateChanged
    private final ChangeSupport changeSupport = new ChangeSupport(this);

    public void removeChangeListener(ChangeListener listener) {
        changeSupport.removeChangeListener(listener);
    }

    public void addChangeListener(ChangeListener listener) {
        changeSupport.addChangeListener(listener);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private org.radixware.kernel.designer.common.dialogs.components.NameEditPanel nameEditPanel;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JRadioButton selectTypeButton;
    private org.radixware.kernel.common.components.ExtendableTextField typeExtendableTextField;
    // End of variables declaration//GEN-END:variables
}
