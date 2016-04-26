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

package org.radixware.kernel.designer.dds.editors.reference;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.EnumSet;
import java.util.HashMap;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import org.openide.util.NbBundle;
import static org.radixware.kernel.common.builder.check.common.RadixObjectChecker.warning;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EDeleteMode;
import org.radixware.kernel.common.enums.EDdsConstraintDbOption;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.designer.common.dialogs.components.state.IStateDisplayer;
import org.radixware.kernel.designer.common.dialogs.components.state.StateDisplayer;
import org.radixware.kernel.designer.common.editors.RadixObjectModalEditor;
import org.radixware.kernel.designer.common.general.editors.IEditorFactory;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.common.dialogs.utils.NameAcceptorFactory;

/**
 * DdsReferenceDef editor.
 *
 */
public final class DdsReferenceEditor extends RadixObjectModalEditor<DdsReferenceDef> {

    private final DdsReferenceDef copy;
    private final ColumnsTable columnsTable;
    private final boolean editable;
    private final boolean hasSecondaryKeys;
    private volatile boolean updating = false;
    

    protected DdsReferenceEditor(DdsReferenceDef reference) {
        super(reference);

        updating = true;

        initComponents();
        this.setMinimumSize(new Dimension(500, 480));

        nameTextField.setEditable(false);
        createChildColumnsButton.setVisible(false);

        initStateDisplayer();
        addButton.setIcon(RadixWareIcons.CREATE.ADD.getIcon());
        removeButton.setIcon(RadixWareIcons.DELETE.DELETE.getIcon());
        fillParentColumnsButton.setIcon(RadixWareIcons.ARROW.MOVE_DOWN.getIcon());

        copy = DdsReferenceDef.Factory.newInstance(reference, reference.getOwnerModel());
        editable = !copy.isReadOnly();

        typeComboBox.removeAllItems();
//        typeComboBox.setModel(new TypeComboBoxModel());
//        typeComboBox.setRenderer(new TypeComboBoxRenderer());
        typeComboBox.addItem(new ComboBoxItem(DdsReferenceDef.EType.MASTER_DETAIL,
                NbBundle.getBundle(DdsReferenceEditor.class).getString("MASTER_DETAIL")));
        typeComboBox.addItem(new ComboBoxItem(DdsReferenceDef.EType.LINK,
                NbBundle.getBundle(DdsReferenceEditor.class).getString("LINK")));

        restrictComboBox.removeAllItems();
//        restrictComboBox.setRenderer(new RestrictCheckModeComboBoxRenderer());
        restrictComboBox.addItem(new ComboBoxItem(DdsReferenceDef.ERestrictCheckMode.ALWAYS,
                NbBundle.getBundle(DdsReferenceEditor.class).getString("ALWAYS")));
        restrictComboBox.addItem(new ComboBoxItem(DdsReferenceDef.ERestrictCheckMode.NEVER,
                NbBundle.getBundle(DdsReferenceEditor.class).getString("NEVER")));
        restrictComboBox.addItem(new ComboBoxItem(DdsReferenceDef.ERestrictCheckMode.ONLY_FOR_SINGLE_OBJECT,
                NbBundle.getBundle(DdsReferenceEditor.class).getString("ONLY_FOR_SINGLE_OBJECT")));

        indicesComboBox.removeAllItems();
//        indicesComboBox.setRenderer(new IndicesComboBoxRenderer());
        DdsTableDef parentTable = copy.findParentTable(copy);
        boolean has = false;
        if (parentTable != null) {
            for (DdsIndexDef index : parentTable.getIndices().get(EScope.ALL)) {
                if (index.getUniqueConstraint() != null) {
                    indicesComboBox.addItem(new ComboBoxItem(index, index.getName()));
                    has = true;
                }
            }
        }
        hasSecondaryKeys = has;

        columnsTable = new ColumnsTable(copy);
        JScrollPane scrollPane = new JScrollPane(columnsTable);
        columnsPanel.add(scrollPane, BorderLayout.CENTER);
        columnsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                updateButtonsEnableState();
            }
        });

        columnsTable.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                nameTextField.setText(copy.getName());
            }
        });

//        nameEditPanel.addChangeListener(new ChangeListener() {
//
//            @Override
//            public void stateChanged(ChangeEvent e) {
//                if (!updating && nameEditPanel.isComplete()) {
//                    copy.setName(nameEditPanel.getCurrentName());
//                    if (copy.isAutoDbName()) {
//                        String dbName = copy.calcAutoDbName();
//                        copy.setDbName(dbName);
//                        dbNameEditPanel.setDbName(dbName);
//                    }
//                }
//            }
//        });

        dbNameEditPanel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!updating && dbNameEditPanel.isComplete()) {
                    copy.setDbName(dbNameEditPanel.getDbName());
                }
            }
        });

        dbNameEditPanel.setNameAcceptor(NameAcceptorFactory.newDbNameAcceptor(reference.getLayer()));

        setupInitialValues();

        updating = false;

        this.setComplete(true);

    }

    private void updateButtonsEnableState() {
        int idx = columnsTable.getSelectedRow();
//        boolean enabled = editable && !readOnly && !isPrimaryKey() && !isSecondaryKey();
//        boolean enabled = editable && !readOnly && anyColumnsRadioButton.isSelected();
        boolean enabled = editable && !primaryKeyCheckBox.isSelected() && !secondaryKeyCheckBox.isSelected();
        removeButton.setEnabled(enabled && idx != -1);
        addButton.setEnabled(enabled && columnsTable.canAddItem());

        columnsTable.setBasedOnKeys(!enabled); //согласно замечанию

//        enabled = editable && !readOnly && (isSecondaryKey() || isPrimaryKey());
//        enabled = editable && !readOnly && !anyColumnsRadioButton.isSelected();
        enabled = editable && (primaryKeyCheckBox.isSelected() || secondaryKeyCheckBox.isSelected());
        fillParentColumnsButton.setEnabled(enabled);
        createChildColumnsButton.setEnabled(enabled);



    }

    private void initStateDisplayer() {
        StateDisplayer stateDisplayer = new StateDisplayer();
        IStateDisplayer.Locator.register(stateDisplayer, this);
        columnsPanel.add(stateDisplayer, BorderLayout.SOUTH);
    }

    private class ComboBoxItem {

        public final Object obj;
        public final String name;

        public ComboBoxItem(Object obj, String name) {
            this.obj = obj;
            this.name = name;
            comboBoxItems.put(obj, this);
        }

        @Override
        public String toString() {
            return name;
        }
    }
    private HashMap<Object, ComboBoxItem> comboBoxItems = new HashMap<Object, ComboBoxItem>();

    private void updateEnableState() {
        typeComboBox.setEnabled(editable);
        autoDbNameCheckBox.setEnabled(editable);
        dbNameEditPanel.setEditable(editable && !copy.isAutoDbName());
        generateInDbCheckBox.setEnabled(editable);
//        descriptionTextArea.setEditable(editable);

        relyCheckBox.setEnabled(editable);
        disableCheckBox.setEnabled(editable);
        novalidateCheckBox.setEnabled(editable);
        deferrableCheckBox.setEnabled(editable);
        initiallyDeferredCheckBox.setEnabled(editable);


        final boolean isOracleFieturesWorks = !copy.getDbOptions().contains(EDdsConstraintDbOption.DISABLE) && copy.isGeneratedInDb();

        boolean noneRadioButtonEnabled = true;
        boolean setNullRadioButtonEnabled = true;
        boolean deleteCascadeRadioButtonEnabled = true;

        if (isOracleFieturesWorks) {
            noneRadioButtonEnabled = false;
        } else {
            deleteCascadeRadioButtonEnabled = false;
            setNullRadioButtonEnabled = false;
        }

        boolean enabled = editable
                && !((DdsReferenceDef.EType) ((ComboBoxItem) typeComboBox.getSelectedItem()).obj).
                equals(DdsReferenceDef.EType.MASTER_DETAIL);
        restrictRadioButton.setEnabled(enabled);
        deleteCascadeRadioButton.setEnabled(enabled && deleteCascadeRadioButtonEnabled);
        setNullRadioButton.setEnabled(enabled && setNullRadioButtonEnabled);
        noneRadioButton.setEnabled(enabled && noneRadioButtonEnabled);

        restrictComboBox.setEnabled(enabled && copy.getDeleteMode() == EDeleteMode.RESTRICT);
        restrictLabel.setEnabled(enabled && copy.getDeleteMode() == EDeleteMode.RESTRICT);
        confirmCheckBox.setEnabled(enabled && (deleteCascadeRadioButton.isSelected() || setNullRadioButton.isSelected()));

        primaryKeyCheckBox.setEnabled(editable);
        secondaryKeyCheckBox.setEnabled(editable && hasSecondaryKeys);
//        anyColumnsRadioButton.setEnabled(editable && !readOnly);
        indicesComboBox.setEnabled(editable && secondaryKeyCheckBox.isSelected() && hasSecondaryKeys);

        columnsTable.setReadOnly(!editable);

        updateButtonsEnableState();
    }

    private void setupInitialValues() {
        nameTextField.setText(copy.getName());
        typeComboBox.setSelectedItem(comboBoxItems.get(copy.getType()));
        autoDbNameCheckBox.setSelected(copy.isAutoDbName());
//        if (copy.isAutoDbName()) //закомментил согласно замечанию
//            copy.setDbName(copy.calcAutoDbName());
        dbNameEditPanel.setDbName(copy.getDbName());
        generateInDbCheckBox.setSelected(copy.isGeneratedInDb());
        String str = copy.getDescription();
//        descriptionTextArea.setText(copy.getDescription());

        EnumSet<EDdsConstraintDbOption> set = copy.getDbOptions();
        relyCheckBox.setSelected(set.contains(EDdsConstraintDbOption.RELY));
        disableCheckBox.setSelected(set.contains(EDdsConstraintDbOption.DISABLE));
        novalidateCheckBox.setSelected(set.contains(EDdsConstraintDbOption.NOVALIDATE));
        deferrableCheckBox.setSelected(set.contains(EDdsConstraintDbOption.DEFERRABLE));
        initiallyDeferredCheckBox.setSelected(set.contains(EDdsConstraintDbOption.INITIALLY_DEFERRED));

        restrictRadioButton.setSelected(copy.getDeleteMode() == EDeleteMode.RESTRICT);
        deleteCascadeRadioButton.setSelected(copy.getDeleteMode() == EDeleteMode.CASCADE);
        setNullRadioButton.setSelected(copy.getDeleteMode() == EDeleteMode.SET_NULL);
        noneRadioButton.setSelected(copy.getDeleteMode() == EDeleteMode.NONE);

        restrictComboBox.setSelectedItem(comboBoxItems.get(copy.getRestrictCheckMode()));
        confirmCheckBox.setSelected(copy.isConfirmDelete());

        DdsIndexDef index = copy.findParentIndex();
        DdsTableDef parentTable = copy.findParentTable(copy);
        if (index == null || parentTable == null) {
            primaryKeyCheckBox.setSelected(false);
            secondaryKeyCheckBox.setSelected(false);
        } else {
            if (Utils.equals(index, parentTable.getPrimaryKey())) {
                primaryKeyCheckBox.setSelected(true);
                secondaryKeyCheckBox.setSelected(false);
            } else {
                primaryKeyCheckBox.setSelected(false);
                secondaryKeyCheckBox.setSelected(true);
                indicesComboBox.setSelectedItem(comboBoxItems.get(index));
            }
        }

        updateEnableState();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        typeComboBox = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        restrictRadioButton = new javax.swing.JRadioButton();
        restrictComboBox = new javax.swing.JComboBox();
        deleteCascadeRadioButton = new javax.swing.JRadioButton();
        setNullRadioButton = new javax.swing.JRadioButton();
        noneRadioButton = new javax.swing.JRadioButton();
        confirmCheckBox = new javax.swing.JCheckBox();
        restrictLabel = new javax.swing.JLabel();
        autoDbNameCheckBox = new javax.swing.JCheckBox();
        dbNameEditPanel = new org.radixware.kernel.designer.dds.editors.DbNameEditPanel();
        generateInDbCheckBox = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        indicesComboBox = new javax.swing.JComboBox();
        fillParentColumnsButton = new javax.swing.JButton();
        createChildColumnsButton = new javax.swing.JButton();
        primaryKeyCheckBox = new javax.swing.JCheckBox();
        secondaryKeyCheckBox = new javax.swing.JCheckBox();
        addButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();
        columnsPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        relyCheckBox = new javax.swing.JCheckBox();
        disableCheckBox = new javax.swing.JCheckBox();
        novalidateCheckBox = new javax.swing.JCheckBox();
        deferrableCheckBox = new javax.swing.JCheckBox();
        initiallyDeferredCheckBox = new javax.swing.JCheckBox();
        nameTextField = new javax.swing.JTextField();

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(DdsReferenceEditor.class, "DdsReferenceEditor.jLabel1.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(DdsReferenceEditor.class, "DdsReferenceEditor.jLabel2.text")); // NOI18N

        typeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        typeComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                typeComboBoxItemStateChanged(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(DdsReferenceEditor.class, "DdsReferenceEditor.jPanel1.border.title"))); // NOI18N

        buttonGroup1.add(restrictRadioButton);
        org.openide.awt.Mnemonics.setLocalizedText(restrictRadioButton, org.openide.util.NbBundle.getMessage(DdsReferenceEditor.class, "DdsReferenceEditor.restrictRadioButton.text")); // NOI18N
        restrictRadioButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                restrictRadioButtonItemStateChanged(evt);
            }
        });

        restrictComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        restrictComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                restrictComboBoxItemStateChanged(evt);
            }
        });

        buttonGroup1.add(deleteCascadeRadioButton);
        org.openide.awt.Mnemonics.setLocalizedText(deleteCascadeRadioButton, org.openide.util.NbBundle.getMessage(DdsReferenceEditor.class, "DdsReferenceEditor.deleteCascadeRadioButton.text")); // NOI18N
        deleteCascadeRadioButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                deleteCascadeRadioButtonItemStateChanged(evt);
            }
        });

        buttonGroup1.add(setNullRadioButton);
        org.openide.awt.Mnemonics.setLocalizedText(setNullRadioButton, org.openide.util.NbBundle.getMessage(DdsReferenceEditor.class, "DdsReferenceEditor.setNullRadioButton.text")); // NOI18N
        setNullRadioButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                setNullRadioButtonItemStateChanged(evt);
            }
        });

        buttonGroup1.add(noneRadioButton);
        org.openide.awt.Mnemonics.setLocalizedText(noneRadioButton, org.openide.util.NbBundle.getMessage(DdsReferenceEditor.class, "DdsReferenceEditor.noneRadioButton.text")); // NOI18N
        noneRadioButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                noneRadioButtonItemStateChanged(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(confirmCheckBox, org.openide.util.NbBundle.getMessage(DdsReferenceEditor.class, "DdsReferenceEditor.confirmCheckBox.text")); // NOI18N
        confirmCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                confirmCheckBoxItemStateChanged(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(restrictLabel, org.openide.util.NbBundle.getMessage(DdsReferenceEditor.class, "DdsReferenceEditor.restrictLabel.text")); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(restrictLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(restrictComboBox, 0, 92, Short.MAX_VALUE))
                    .addComponent(restrictRadioButton)
                    .addComponent(deleteCascadeRadioButton)
                    .addComponent(setNullRadioButton)
                    .addComponent(noneRadioButton)
                    .addComponent(confirmCheckBox))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(restrictRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(restrictLabel)
                    .addComponent(restrictComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deleteCascadeRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(setNullRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(noneRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(confirmCheckBox)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.openide.awt.Mnemonics.setLocalizedText(autoDbNameCheckBox, org.openide.util.NbBundle.getMessage(DdsReferenceEditor.class, "DdsReferenceEditor.autoDbNameCheckBox.text")); // NOI18N
        autoDbNameCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                autoDbNameCheckBoxItemStateChanged(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(generateInDbCheckBox, org.openide.util.NbBundle.getMessage(DdsReferenceEditor.class, "DdsReferenceEditor.generateInDbCheckBox.text")); // NOI18N
        generateInDbCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                generateInDbCheckBoxItemStateChanged(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(DdsReferenceEditor.class, "DdsReferenceEditor.jPanel3.border.title"))); // NOI18N

        indicesComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        indicesComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                indicesComboBoxItemStateChanged(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(fillParentColumnsButton, org.openide.util.NbBundle.getMessage(DdsReferenceEditor.class, "DdsReferenceEditor.fillParentColumnsButton.text")); // NOI18N
        fillParentColumnsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fillParentColumnsButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(createChildColumnsButton, org.openide.util.NbBundle.getMessage(DdsReferenceEditor.class, "DdsReferenceEditor.createChildColumnsButton.text")); // NOI18N
        createChildColumnsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createChildColumnsButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(primaryKeyCheckBox, org.openide.util.NbBundle.getMessage(DdsReferenceEditor.class, "DdsReferenceEditor.primaryKeyCheckBox.text")); // NOI18N
        primaryKeyCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                primaryKeyCheckBoxItemStateChanged(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(secondaryKeyCheckBox, org.openide.util.NbBundle.getMessage(DdsReferenceEditor.class, "DdsReferenceEditor.secondaryKeyCheckBox.text")); // NOI18N
        secondaryKeyCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                secondaryKeyCheckBoxItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(secondaryKeyCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(indicesComboBox, 0, 330, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(primaryKeyCheckBox)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(fillParentColumnsButton)
                                .addGap(103, 103, 103)
                                .addComponent(createChildColumnsButton)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(primaryKeyCheckBox)
                .addGap(6, 6, 6)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(indicesComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(secondaryKeyCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fillParentColumnsButton)
                    .addComponent(createChildColumnsButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.openide.awt.Mnemonics.setLocalizedText(addButton, org.openide.util.NbBundle.getMessage(DdsReferenceEditor.class, "DdsReferenceEditor.addButton.text")); // NOI18N
        addButton.setToolTipText(org.openide.util.NbBundle.getMessage(DdsReferenceEditor.class, "DdsReferenceEditor.addButton.toolTipText")); // NOI18N
        addButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(removeButton, org.openide.util.NbBundle.getMessage(DdsReferenceEditor.class, "DdsReferenceEditor.removeButton.text")); // NOI18N
        removeButton.setToolTipText(org.openide.util.NbBundle.getMessage(DdsReferenceEditor.class, "DdsReferenceEditor.removeButton.toolTipText")); // NOI18N
        removeButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });

        columnsPanel.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(DdsReferenceEditor.class, "DdsReferenceEditor.jPanel2.border.title"))); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(relyCheckBox, org.openide.util.NbBundle.getMessage(DdsReferenceEditor.class, "DdsReferenceEditor.relyCheckBox.text")); // NOI18N
        relyCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                relyCheckBoxItemStateChanged(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(disableCheckBox, org.openide.util.NbBundle.getMessage(DdsReferenceEditor.class, "DdsReferenceEditor.disableCheckBox.text")); // NOI18N
        disableCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                disableCheckBoxItemStateChanged(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(novalidateCheckBox, org.openide.util.NbBundle.getMessage(DdsReferenceEditor.class, "DdsReferenceEditor.novalidateCheckBox.text")); // NOI18N
        novalidateCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                novalidateCheckBoxItemStateChanged(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(deferrableCheckBox, org.openide.util.NbBundle.getMessage(DdsReferenceEditor.class, "DdsReferenceEditor.deferrableCheckBox.text")); // NOI18N
        deferrableCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                deferrableCheckBoxItemStateChanged(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(initiallyDeferredCheckBox, org.openide.util.NbBundle.getMessage(DdsReferenceEditor.class, "DdsReferenceEditor.initiallyDeferredCheckBox.text")); // NOI18N
        initiallyDeferredCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                initiallyDeferredCheckBoxItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(relyCheckBox)
                    .addComponent(disableCheckBox)
                    .addComponent(novalidateCheckBox)
                    .addComponent(deferrableCheckBox)
                    .addComponent(initiallyDeferredCheckBox))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(relyCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(disableCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(novalidateCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deferrableCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(initiallyDeferredCheckBox)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        nameTextField.setText(org.openide.util.NbBundle.getMessage(DdsReferenceEditor.class, "DdsReferenceEditor.nameTextField.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(autoDbNameCheckBox)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(typeComboBox, javax.swing.GroupLayout.Alignment.TRAILING, 0, 401, Short.MAX_VALUE)
                            .addComponent(dbNameEditPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
                            .addComponent(nameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)))
                    .addComponent(generateInDbCheckBox)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(columnsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 514, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(removeButton)
                            .addComponent(addButton)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(typeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(autoDbNameCheckBox)
                    .addComponent(dbNameEditPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(generateInDbCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(addButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeButton)
                        .addContainerGap(55, Short.MAX_VALUE))
                    .addComponent(columnsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void relyCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_relyCheckBoxItemStateChanged
        if (updating) {
            return;
        }
        if (relyCheckBox.isSelected()) {
            copy.getDbOptions().add(EDdsConstraintDbOption.RELY);
        } else {
            copy.getDbOptions().remove(EDdsConstraintDbOption.RELY);
        }
}//GEN-LAST:event_relyCheckBoxItemStateChanged

    private void disableCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_disableCheckBoxItemStateChanged
        if (updating) {
            return;
        }
        if (disableCheckBox.isSelected()) {
            copy.getDbOptions().add(EDdsConstraintDbOption.DISABLE);
        } else {
            copy.getDbOptions().remove(EDdsConstraintDbOption.DISABLE);
        }
        updateEnableState();
}//GEN-LAST:event_disableCheckBoxItemStateChanged

    private void novalidateCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_novalidateCheckBoxItemStateChanged
        if (updating) {
            return;
        }
        if (novalidateCheckBox.isSelected()) {
            copy.getDbOptions().add(EDdsConstraintDbOption.NOVALIDATE);
        } else {
            copy.getDbOptions().remove(EDdsConstraintDbOption.NOVALIDATE);
        }
}//GEN-LAST:event_novalidateCheckBoxItemStateChanged

    private void deferrableCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_deferrableCheckBoxItemStateChanged
        if (updating) {
            return;
        }
        if (deferrableCheckBox.isSelected()) {
            copy.getDbOptions().add(EDdsConstraintDbOption.DEFERRABLE);
        } else {
            copy.getDbOptions().remove(EDdsConstraintDbOption.DEFERRABLE);
        }
}//GEN-LAST:event_deferrableCheckBoxItemStateChanged

    private void initiallyDeferredCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_initiallyDeferredCheckBoxItemStateChanged
        if (updating) {
            return;
        }
        if (initiallyDeferredCheckBox.isSelected()) {
            copy.getDbOptions().add(EDdsConstraintDbOption.INITIALLY_DEFERRED);
        } else {
            copy.getDbOptions().remove(EDdsConstraintDbOption.INITIALLY_DEFERRED);
        }
}//GEN-LAST:event_initiallyDeferredCheckBoxItemStateChanged

    private void typeComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_typeComboBoxItemStateChanged
        if (!updating) {
            copy.setType((DdsReferenceDef.EType) ((ComboBoxItem) typeComboBox.getSelectedItem()).obj);
            updateEnableState();
        }
    }//GEN-LAST:event_typeComboBoxItemStateChanged

    private void autoDbNameCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_autoDbNameCheckBoxItemStateChanged
        if (updating) {
            return;
        }
        boolean isAutoDbName = autoDbNameCheckBox.isSelected();
        copy.setAutoDbName(isAutoDbName);
        if (isAutoDbName) {
            String dbName = copy.calcAutoDbName();
            copy.setDbName(dbName);
//            if (tableAutoDbNameCheckBox.hasFocus())
//                tableDbNameEditPanel.requestFocusInWindow();
            dbNameEditPanel.setDbName(dbName);
        } else {
            if (autoDbNameCheckBox.hasFocus()) {
                dbNameEditPanel.requestFocusInWindow();
            }
            dbNameEditPanel.setDbName(copy.getDbName());
        }
        dbNameEditPanel.setEditable(!isAutoDbName);
    }//GEN-LAST:event_autoDbNameCheckBoxItemStateChanged

    private void generateInDbCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_generateInDbCheckBoxItemStateChanged
        if (!updating) {
            copy.setGeneratedInDb(generateInDbCheckBox.isSelected());
            updateEnableState();
        }
    }//GEN-LAST:event_generateInDbCheckBoxItemStateChanged

    private void restrictRadioButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_restrictRadioButtonItemStateChanged
        if (updating) {
            return;
        }
        if (restrictRadioButton.isSelected()) {
            copy.setDeleteMode(EDeleteMode.RESTRICT);
            restrictComboBox.setEnabled(true);
            restrictLabel.setEnabled(true);
            confirmCheckBox.setEnabled(false);
        } else {
            restrictComboBox.setEnabled(false);
            restrictLabel.setEnabled(false);
        }
    }//GEN-LAST:event_restrictRadioButtonItemStateChanged

    private void deleteCascadeRadioButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_deleteCascadeRadioButtonItemStateChanged
        if (updating) {
            return;
        }
        if (deleteCascadeRadioButton.isSelected()) {
            copy.setDeleteMode(EDeleteMode.CASCADE);
            confirmCheckBox.setEnabled(true);
        }
    }//GEN-LAST:event_deleteCascadeRadioButtonItemStateChanged

    private void setNullRadioButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_setNullRadioButtonItemStateChanged
        if (updating) {
            return;
        }
        if (setNullRadioButton.isSelected()) {
            copy.setDeleteMode(EDeleteMode.SET_NULL);
            confirmCheckBox.setEnabled(true);
        }
    }//GEN-LAST:event_setNullRadioButtonItemStateChanged

    private void noneRadioButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_noneRadioButtonItemStateChanged
        if (updating) {
            return;
        }
        if (noneRadioButton.isSelected()) {
            copy.setDeleteMode(EDeleteMode.NONE);
            confirmCheckBox.setEnabled(false);
        }
    }//GEN-LAST:event_noneRadioButtonItemStateChanged

    private void confirmCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_confirmCheckBoxItemStateChanged
        if (!updating) {
            copy.setConfirmDelete(confirmCheckBox.isSelected());
        }
    }//GEN-LAST:event_confirmCheckBoxItemStateChanged

    private void restrictComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_restrictComboBoxItemStateChanged
        if (!updating) {
            copy.setRestrictCheckMode((DdsReferenceDef.ERestrictCheckMode) ((ComboBoxItem) restrictComboBox.getSelectedItem()).obj);
        }
    }//GEN-LAST:event_restrictComboBoxItemStateChanged

    private void primaryKeysRadioButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_primaryKeysRadioButtonItemStateChanged
//        if (updating) {
//            return;
//        }
//        if (primaryKeysRadioButton.isSelected()) {
//            DdsTableDef parentTable = copy.findParentTable();
//            if (parentTable != null) {
//                copy.setParentUnuqueConstraintId(parentTable.getPrimaryKey().getUniqueConstraint().getId());
//            } else {
//                copy.setParentUnuqueConstraintId(null);
//            }
//            updateButtonsEnableState();
//        }
    }//GEN-LAST:event_primaryKeysRadioButtonItemStateChanged

    private void secondaryKeysRadioButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_secondaryKeysRadioButtonItemStateChanged
//        if (updating) {
//            return;
//        }
//        indicesComboBox.setEnabled(secondaryKeysRadioButton.isSelected());
//        if (secondaryKeysRadioButton.isSelected()) {
//            ComboBoxItem item = (ComboBoxItem) indicesComboBox.getSelectedItem();
//            DdsIndexDef index = item != null ? (DdsIndexDef) item.obj : null;
//            if (index != null) {
//                copy.setParentUnuqueConstraintId(index.getUniqueConstraint().getId());
//            } else {
//                copy.setParentUnuqueConstraintId(null);
//            }
//            updateButtonsEnableState();
//        }
    }//GEN-LAST:event_secondaryKeysRadioButtonItemStateChanged

    private void indicesComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_indicesComboBoxItemStateChanged
        if (updating) {
            return;
        }
        columnsTable.stopEditing();
        DdsIndexDef index = (DdsIndexDef) ((ComboBoxItem) indicesComboBox.getSelectedItem()).obj;
        if (index != null) {
            copy.setParentUnuqueConstraintId(index.getUniqueConstraint().getId());
        } else {
            copy.setParentUnuqueConstraintId(null);
        }
    }//GEN-LAST:event_indicesComboBoxItemStateChanged

    private void fillParentColumnsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fillParentColumnsButtonActionPerformed
        columnsTable.stopEditing();
        if (primaryKeyCheckBox.isSelected()) {
            DdsTableDef parentTable = copy.findParentTable(copy);
            if (parentTable != null) {
                columnsTable.assignIndex(parentTable.getPrimaryKey());
            }
        } else if (secondaryKeyCheckBox.isSelected()) {
            ComboBoxItem item = (ComboBoxItem) indicesComboBox.getSelectedItem();
            DdsIndexDef index = item != null ? (DdsIndexDef) item.obj : null;
            if (index != null) {
                columnsTable.assignIndex(index);
            }
        }
    }//GEN-LAST:event_fillParentColumnsButtonActionPerformed

    private void createChildColumnsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createChildColumnsButtonActionPerformed
        columnsTable.stopEditing();
        columnsTable.autoAssignChildColumns();
    }//GEN-LAST:event_createChildColumnsButtonActionPerformed

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        columnsTable.addItem();
        updateButtonsEnableState();
    }//GEN-LAST:event_addButtonActionPerformed

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed
        int idx = columnsTable.getSelectedRow();
        boolean last = idx == columnsTable.getRowCount() - 1;
        int cnt = columnsTable.getRowCount();
        columnsTable.removeSelectedItem();
        if (last) {
            if (cnt > 1) {
                columnsTable.setRowSelectionInterval(idx - 1, idx - 1);
            } else {
                columnsTable.clearSelection();
            }
        } else {
            columnsTable.setRowSelectionInterval(idx, idx);
        }
//        updateButtonsEnableState();
    }//GEN-LAST:event_removeButtonActionPerformed

    private void primaryKeyCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_primaryKeyCheckBoxItemStateChanged
        if (updating) {
            return;
        }
        columnsTable.stopEditing();
        if (primaryKeyCheckBox.isSelected()) {
            updating = true;
            secondaryKeyCheckBox.setSelected(false);
            updating = false;
            DdsTableDef parentTable = copy.findParentTable(copy);
            if (parentTable != null) {
                copy.setParentUnuqueConstraintId(parentTable.getPrimaryKey().getUniqueConstraint().getId());
            } else {
                copy.setParentUnuqueConstraintId(null);
            }
        } else {
            if (!secondaryKeyCheckBox.isSelected()) {
                copy.setParentUnuqueConstraintId(null);
            }
        }
        updateButtonsEnableState();
    }//GEN-LAST:event_primaryKeyCheckBoxItemStateChanged

    private void secondaryKeyCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_secondaryKeyCheckBoxItemStateChanged
        if (updating) {
            return;
        }
        columnsTable.stopEditing();
        indicesComboBox.setEnabled(secondaryKeyCheckBox.isSelected());
        if (secondaryKeyCheckBox.isSelected()) {
            updating = true;
            primaryKeyCheckBox.setSelected(false);
            updating = false;
            ComboBoxItem item = (ComboBoxItem) indicesComboBox.getSelectedItem();
            DdsIndexDef index = item != null ? (DdsIndexDef) item.obj : null;
            if (index != null) {
                copy.setParentUnuqueConstraintId(index.getUniqueConstraint().getId());
            } else {
                copy.setParentUnuqueConstraintId(null);
            }
        } else {
            if (!primaryKeyCheckBox.isSelected()) {
                copy.setParentUnuqueConstraintId(null);
            }
        }
        updateButtonsEnableState();
    }//GEN-LAST:event_secondaryKeyCheckBoxItemStateChanged
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JCheckBox autoDbNameCheckBox;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JPanel columnsPanel;
    private javax.swing.JCheckBox confirmCheckBox;
    private javax.swing.JButton createChildColumnsButton;
    private org.radixware.kernel.designer.dds.editors.DbNameEditPanel dbNameEditPanel;
    private javax.swing.JCheckBox deferrableCheckBox;
    private javax.swing.JRadioButton deleteCascadeRadioButton;
    private javax.swing.JCheckBox disableCheckBox;
    private javax.swing.JButton fillParentColumnsButton;
    private javax.swing.JCheckBox generateInDbCheckBox;
    private javax.swing.JComboBox indicesComboBox;
    private javax.swing.JCheckBox initiallyDeferredCheckBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JRadioButton noneRadioButton;
    private javax.swing.JCheckBox novalidateCheckBox;
    private javax.swing.JCheckBox primaryKeyCheckBox;
    private javax.swing.JCheckBox relyCheckBox;
    private javax.swing.JButton removeButton;
    private javax.swing.JComboBox restrictComboBox;
    private javax.swing.JLabel restrictLabel;
    private javax.swing.JRadioButton restrictRadioButton;
    private javax.swing.JCheckBox secondaryKeyCheckBox;
    private javax.swing.JRadioButton setNullRadioButton;
    private javax.swing.JComboBox typeComboBox;
    // End of variables declaration//GEN-END:variables

    private void assign(DdsReferenceDef reference, DdsReferenceDef copy) {
        reference.setAutoDbName(copy.isAutoDbName());
        reference.setDbName(copy.getDbName());
        reference.setName(copy.getName());
        reference.getDbOptions().clear();
        reference.getDbOptions().addAll(copy.getDbOptions());

        reference.setConfirmDelete(copy.isConfirmDelete());
        reference.setDeleteMode(copy.getDeleteMode());
        reference.setGeneratedInDb(copy.isGeneratedInDb());
        reference.setParentUnuqueConstraintId(copy.getParentUnuqueConstraintId());
        reference.setRestrictCheckMode(copy.getRestrictCheckMode());
        reference.setType(copy.getType());

        columnsTable.unregisterContainerChangeListener();
        columnsTable.stopEditing();
        RadixObjectsUtils.moveItems(copy.getColumnsInfo(), reference.getColumnsInfo());

        if (reference.getType() == DdsReferenceDef.EType.MASTER_DETAIL) {
            reference.setDeleteMode(EDeleteMode.CASCADE);
        }
    }

    @Override
    protected void apply() {
        DdsReferenceDef reference = getReference();
        if (!reference.isReadOnly()) {
            assign(reference, copy);
        }
    }

    private DdsReferenceDef getReference() {
        return getRadixObject();
    }

    @Override
    public boolean open(OpenInfo openInfo) {
        return super.open(openInfo);
    }

    @Override
    public void update() {
        // NOTHING
    }

    public static final class Factory implements IEditorFactory<DdsReferenceDef> {

        @Override
        public DdsReferenceEditor newInstance(DdsReferenceDef reference) {
            return new DdsReferenceEditor(reference);
        }
    }
}
