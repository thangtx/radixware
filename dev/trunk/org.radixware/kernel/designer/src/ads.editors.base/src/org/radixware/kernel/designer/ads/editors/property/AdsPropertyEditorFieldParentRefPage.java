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
 *
 * Created on Jun 15, 2010, 10:16:25 AM
 */
package org.radixware.kernel.designer.ads.editors.property;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsFieldRefPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsFieldRefPropertyDef.RefMapItem;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsIndexDef.ColumnsInfo;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinition;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.components.DescriptionPanel;
import org.radixware.kernel.designer.common.general.utils.DefinitionsUtils;


public class AdsPropertyEditorFieldParentRefPage extends javax.swing.JPanel {

    public AdsPropertyEditorFieldParentRefPage() {
        initComponents();

        jTableFieldRefProp.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

        depFieldRefPropType.addChangeListener(selectorFieldRefEntytyDefListener);
        depFieldRefPropIndexes.addChangeListener(fieldIndexChangedListener);

        jTableFieldRefProp.setModel(jtblTableModel);

        TableColumnModel columnModel = jTableFieldRefProp.getColumnModel();
        TableColumn column = columnModel.getColumn(1);
        column.setCellEditor(new DefaultCellEditor(jComboBox));

        jTableFieldRefProp.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                if (fProp == null || !fProp.isInBranch() || "ancestor".equals(evt.getPropertyName())) {
                    return;
                }
                if (jTableFieldRefProp.getSelectedColumn() == 1 && jTableFieldRefProp.getSelectedRow() > -1) //edit column
                {
                    int index = jTableFieldRefProp.getSelectedRow();
                    Object valObj = jTableFieldRefProp.getValueAt(index, 1);

                    if (valObj != null) {
                        VisibleItemAdsProperty value = (VisibleItemAdsProperty) valObj;
                        if (value.prop != null) {
                            fProp.getFieldToColumnMap().get(index).setFieldId(value.prop.getId());
                        } else {
                            fProp.getFieldToColumnMap().get(index).setFieldId(null);
                        }
                    } else {
                        fProp.getFieldToColumnMap().get(index).setFieldId(null);
                    }
                }
            }
        });


    }
    private boolean isReadOnly = true;
    private boolean isMayModify = false;
    private AdsPropertyDef prop = null;
    private DdsIndexDef lastIndexDef = null;
    private List<EValType> propFieldColumnTypeList = null;
    private AdsFieldRefPropertyDef fProp = null;
    private List<DdsIndexDef> indexList = null;
    //AdsFieldRefPropertyDef fProp = null;
    private DdsTableDef propRefFieldTbl = null;

    private class AdsEntityClassDefVisitorProvider extends VisitorProvider {

        @Override
        public boolean isTarget(RadixObject radixObject) {
            return (radixObject instanceof AdsEntityClassDef);
        }
    }

    private class VisibleItemAdsProperty {

        protected final String name;
        protected final AdsPropertyDef prop;

        VisibleItemAdsProperty(final String name, final AdsPropertyDef prop) {
            this.name = name;
            this.prop = prop;
        }

        @Override
        public String toString() {
            return name;
        }
    }
    private JComboBox jComboBox = new JComboBox();
    private DefaultTableModel jtblTableModel = new javax.swing.table.DefaultTableModel(
            new Object[][][]{},
            new String[]{
        "Table Column", "Cursor Field", "Item"
    }) {
        @Override
        public Class getColumnClass(int columnIndex) {
            return java.lang.Object.class;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            if (columnIndex == 2) {
                return false;
            }
            boolean isSecondColumn = !isReadOnly && columnIndex == 1;
            if (isSecondColumn) {
                jComboBox.removeAllItems();
                prop.setEditState(RadixObject.EEditState.MODIFIED);
                EValType valType = propFieldColumnTypeList.get(rowIndex);
                if (valType != null) {
                    List<AdsPropertyDef> properties = fProp.getOwnerClass().getProperties().get(EScope.ALL);
                    for (AdsPropertyDef p : properties) {
                        if (p.getValue().getType().getTypeId().equals(valType)) {
                            jComboBox.addItem(new VisibleItemAdsProperty(p.getName(), p));
                        }
                    }
                }
            }
            return isSecondColumn;
        }
    };
    private ChangeListener fieldIndexChangedListener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            if (!isMayModify || isReadOnly) {
                return;
            }
            lastIndexDef = (DdsIndexDef) depFieldRefPropIndexes.getDefinition();
        }
    };
    private ChangeListener selectorFieldRefEntytyDefListener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            if (!isMayModify || isReadOnly) {
                return;
            }
            if (fProp == null) {
                return;
            }

            fProp.getFieldToColumnMap().clear();
            prop.getValue().getType().getTypeId();
            prop.getValue().setType(
                    AdsTypeDeclaration.Factory.newParentRef(
                    //newEntityObject(
                    (AdsEntityObjectClassDef) depFieldRefPropType.getDefinition()));
            prop.getValue().
                    getType().getTypeId();
            depFieldRefPropIndexes.open(null, null);
            update();
        }
    };

    public void open(AdsPropertyDef prop, boolean isReadOnly) {
        this.prop = prop;
        this.isReadOnly = isReadOnly;
        update();
    }

    private RefMapItem getSelectedRefMapItem() {
        int rowIdx = this.jTableFieldRefProp.getSelectedRow();
        int count = this.jtblTableModel.getRowCount();
        if (rowIdx >= 0 && rowIdx < count) {
            return (RefMapItem) this.jtblTableModel.getValueAt(rowIdx, 2);
        }
        return null;
    }

    private ColumnsInfo getEffectiveColumnsInfo() {
        ColumnsInfo csi = null;
        if (jRadioButton1.isSelected()) {
            csi = propRefFieldTbl.getPrimaryKey().getColumnsInfo();
        } else {
            Definition dInd = depFieldRefPropIndexes.getDefinition();
            if (dInd != null) {
                DdsIndexDef index = (DdsIndexDef) dInd;
                csi = index.getColumnsInfo();
            }
        }
        return csi;
    }

    private boolean canDeleteRefMapItem(RefMapItem item) {
        if (item == null) {
            return false;
        }
        ColumnsInfo infos = getEffectiveColumnsInfo();
        for (DdsIndexDef.ColumnInfo info : infos) {
            if (info.getColumnId() == item.getColumnId()) {
                return false;
            }
        }
        return true;
    }
    private final ListSelectionListener selectionListener = new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            btRemoveColumn.setEnabled(!isReadOnly && canDeleteRefMapItem(getSelectedRefMapItem()));
        }
    };

    public void update() {
        if (!(prop instanceof AdsFieldRefPropertyDef)) {
            return;
        }

        isMayModify = false;

        fProp = (AdsFieldRefPropertyDef) prop;
        accessComboBox1.open(fProp);

        AdsType type = fProp.getValue().getType().resolve(fProp).get();
        propFieldColumnTypeList = new ArrayList<>();
        AdsEntityClassDef cl = null;
        if (type instanceof AdsClassType) {
            AdsClassDef clazz = ((AdsClassType) type).getSource();
            if (clazz instanceof AdsEntityClassDef) {
                cl = (AdsEntityClassDef) clazz;
            }
        }

        //depFieldRefPropType.setComboMode(prop);
        Collection<Definition> collection = DefinitionsUtils.collectAllAround(prop, new AdsEntityClassDefVisitorProvider());
        //depFieldRefPropType.setComboBoxValues(collection, false);
        depFieldRefPropType.setEnabled(!isReadOnly);
        depFieldRefPropType.setClearable(false);
        jButton1.setEnabled(!isReadOnly);
        btAddColumn.setEnabled(!isReadOnly);


        if (cl == null) {
            depFieldRefPropType.open(ChooseDefinitionCfg.Factory.newInstance(collection), null, null);
            jRadioButton1.setEnabled(false);
            jRadioButton2.setEnabled(false);
            depFieldRefPropIndexes.open(null, null);
            depFieldRefPropIndexes.setEnabled(false);
            jTableFieldRefProp.setEnabled(false);
            jtblTableModel.setRowCount(0);
            isMayModify = true;
            return;
        }

        depFieldRefPropType.open(ChooseDefinitionCfg.Factory.newInstance(collection), cl, cl.getId());
        propRefFieldTbl = cl.findTable(prop);
        if (propRefFieldTbl == null) {
            depFieldRefPropType.open(ChooseDefinitionCfg.Factory.newInstance(collection), null, null);
            jRadioButton1.setEnabled(false);
            jRadioButton2.setEnabled(false);
            depFieldRefPropIndexes.open(null, null);
            depFieldRefPropIndexes.setEnabled(false);
            jTableFieldRefProp.setEnabled(false);
            jtblTableModel.setRowCount(0);
            isMayModify = true;
            return;
        }

        jRadioButton1.setEnabled(!isReadOnly);
        jRadioButton2.setEnabled(!isReadOnly);
        depFieldRefPropIndexes.setEnabled(!isReadOnly);
        jTableFieldRefProp.setEnabled(!isReadOnly);

        //fill Indexes
        List<DdsIndexDef> list = propRefFieldTbl.getIndices().get(EScope.ALL);
        indexList = new ArrayList<>();

        boolean findOldIndex = false;
        for (DdsIndexDef ind : list) {
            if (lastIndexDef == ind) {
                findOldIndex = true;
            }
            indexList.add(ind);
        }

        depFieldRefPropIndexes.setComboMode(prop);
        depFieldRefPropIndexes.setComboBoxValues(indexList, false);
        if (findOldIndex) {
            depFieldRefPropIndexes.open(lastIndexDef, lastIndexDef.getId());
        } else if (indexList.size() > 0) {
            depFieldRefPropIndexes.open(indexList.get(0), indexList.get(0).getId());
        }
        depFieldRefPropIndexes.setEnabled(!isReadOnly && jRadioButton2.isSelected());

        //fill table
        List<RefMapItem> lst = fProp.getFieldToColumnMap().list();

        List<DdsColumnDef> columns = propRefFieldTbl.getColumns().get(EScope.ALL);
        List<AdsPropertyDef> properties = fProp.getOwnerClass().getProperties().get(EScope.ALL);
        jTableFieldRefProp.getColumnModel().getColumn(2).setWidth(0);
        jTableFieldRefProp.getColumnModel().getColumn(2).setMaxWidth(0);
        jTableFieldRefProp.getColumnModel().getColumn(2).setMinWidth(0);
        int selectedRow = jTableFieldRefProp.getSelectedRow();

        jtblTableModel.setRowCount(lst.size());


        int i = 0;

        jTableFieldRefProp.getSelectionModel().removeListSelectionListener(selectionListener);
        for (RefMapItem item : lst) {
            jtblTableModel.setValueAt(item, i, 2);
            boolean find = false;
            Id id = item.getColumnId();
            if (id != null) {
                for (DdsColumnDef column : columns) {
                    if (id.equals(column.getId())) {
                        jtblTableModel.setValueAt(column.getName(), i, 0);
                        propFieldColumnTypeList.add(column.getValType());
                        find = true;
                        break;
                    }
                }
            }
            if (!find) {
                jtblTableModel.setValueAt(id == null ? "" : id.toString(), i, 0);
                propFieldColumnTypeList.add(null);
            }


            find = false;
            id = item.getFieldId();
            if (id != null) {
                for (AdsPropertyDef propertie : properties) {
                    if (id.equals(propertie.getId())) {
                        jtblTableModel.setValueAt(new VisibleItemAdsProperty(propertie.getName(), propertie), i, 1);
                        find = true;
                        break;
                    }
                }
            }
            if (!find) {
                jtblTableModel.setValueAt(id == null ? new VisibleItemAdsProperty(null, null) : new VisibleItemAdsProperty(id.toString(), null), i, 1);
            }
            i++;
        }
        if (selectedRow >= jTableFieldRefProp.getRowCount()) {
            selectedRow = jTableFieldRefProp.getRowCount() - 1;
        }
        if (selectedRow >= 0 && selectedRow < jTableFieldRefProp.getRowCount()) {
            jTableFieldRefProp.getSelectionModel().setSelectionInterval(selectedRow, selectedRow);
        }


        btRemoveColumn.setEnabled(!isReadOnly && canDeleteRefMapItem(getSelectedRefMapItem()));
        jTableFieldRefProp.getSelectionModel().addListSelectionListener(selectionListener);
        ((DescriptionPanel) descriptionPanel).open(prop);
        ((DescriptionPanel) descriptionPanel).setReadonly(isReadOnly);

        isMayModify = true;

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        depFieldRefPropType = new org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jButton1 = new javax.swing.JButton();
        depFieldRefPropIndexes = new org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel();
        btAddColumn = new javax.swing.JButton();
        btRemoveColumn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableFieldRefProp = new javax.swing.JTable();
        descriptionPanel = new DescriptionPanel();
        jLabel5 = new javax.swing.JLabel();
        accessComboBox1 = new org.radixware.kernel.designer.ads.common.dialogs.AccessEditPanel();

        jLabel6.setText(org.openide.util.NbBundle.getMessage(AdsPropertyEditorFieldParentRefPage.class, "AdsPropertyEditorFieldParentRefPage.jLabel6.text")); // NOI18N

        jPanel22.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(AdsPropertyEditorFieldParentRefPage.class, "AdsPropertyEditorFieldParentRefPage.jPanel22.border.title"))); // NOI18N

        jRadioButton1.setSelected(true);
        jRadioButton1.setText(org.openide.util.NbBundle.getMessage(AdsPropertyEditorFieldParentRefPage.class, "AdsPropertyEditorFieldParentRefPage.jRadioButton1.text")); // NOI18N
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        jRadioButton2.setText(org.openide.util.NbBundle.getMessage(AdsPropertyEditorFieldParentRefPage.class, "AdsPropertyEditorFieldParentRefPage.jRadioButton2.text")); // NOI18N
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        jButton1.setText(org.openide.util.NbBundle.getMessage(AdsPropertyEditorFieldParentRefPage.class, "AdsPropertyEditorFieldParentRefPage.jButton1.text")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btAddColumn.setText(org.openide.util.NbBundle.getMessage(AdsPropertyEditorFieldParentRefPage.class, "AdsPropertyEditorFieldParentRefPage.btAddColumn.text")); // NOI18N
        btAddColumn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAddColumnActionPerformed(evt);
            }
        });

        btRemoveColumn.setText(org.openide.util.NbBundle.getMessage(AdsPropertyEditorFieldParentRefPage.class, "AdsPropertyEditorFieldParentRefPage.btRemoveColumn.text")); // NOI18N
        btRemoveColumn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRemoveColumnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jRadioButton1)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addComponent(jRadioButton2)
                .addGap(6, 6, 6)
                .addComponent(depFieldRefPropIndexes, javax.swing.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE))
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btAddColumn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btRemoveColumn))
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addComponent(jRadioButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton2)
                    .addComponent(depFieldRefPropIndexes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(btAddColumn)
                    .addComponent(btRemoveColumn)))
        );

        jTableFieldRefProp.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jTableFieldRefProp);

        javax.swing.GroupLayout descriptionPanelLayout = new javax.swing.GroupLayout(descriptionPanel);
        descriptionPanel.setLayout(descriptionPanelLayout);
        descriptionPanelLayout.setHorizontalGroup(
            descriptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        descriptionPanelLayout.setVerticalGroup(
            descriptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 16, Short.MAX_VALUE)
        );

        jLabel5.setText(org.openide.util.NbBundle.getMessage(AdsPropertyEditorFieldParentRefPage.class, "AdsPropertyEditorFieldParentRefPage.jLabel5.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(depFieldRefPropType, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(10, 10, 10)
                        .addComponent(accessComboBox1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(descriptionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(descriptionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(accessComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(depFieldRefPropType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        jRadioButton1.setSelected(true);
        jRadioButton2.setSelected(false);
        depFieldRefPropIndexes.setEnabled(jRadioButton2.isSelected() && !isReadOnly);
}//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        jRadioButton1.setSelected(false);
        jRadioButton2.setSelected(true);
        depFieldRefPropIndexes.setEnabled(jRadioButton2.isSelected() && !isReadOnly);
}//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (fProp == null || propRefFieldTbl == null || isReadOnly || !isMayModify) {
            return;
        }
        AdsFieldRefPropertyDef.FieldToColumnMap map = fProp.getFieldToColumnMap();
        ColumnsInfo csi = null;
        if (jRadioButton1.isSelected()) {
            csi = propRefFieldTbl.getPrimaryKey().getColumnsInfo();
        } else {
            Definition dInd = depFieldRefPropIndexes.getDefinition();
            if (dInd != null) {
                DdsIndexDef index = (DdsIndexDef) dInd;
                csi = index.getColumnsInfo();
            }
        }
        if (csi != null) {
            for (DdsIndexDef.ColumnInfo info : csi) {
                Id columnId = info.getColumnId();
                RefMapItem item = map.findItemByColumnId(columnId);
                if (item == null) {
                    item = RefMapItem.Factory.newInstance();
                    item.setColumnId(columnId);
                    map.add(item);
                }
            }
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                update();
            }
        });

}//GEN-LAST:event_jButton1ActionPerformed

    private static class ChooseColumnCfg extends ChooseDefinitionCfg {

        private static List<DdsColumnDef> listColumns(DdsTableDef table, AdsFieldRefPropertyDef fProp) {

            if (table == null) {
                return Collections.emptyList();
            }
            final AdsFieldRefPropertyDef.FieldToColumnMap map = fProp.getFieldToColumnMap();

            return table.getColumns().get(EScope.ALL, new IFilter<DdsColumnDef>() {
                @Override
                public boolean isTarget(DdsColumnDef radixObject) {
                    if (map.findItemByColumnId(radixObject.getId()) != null) {
                        return false;
                    } else {
                        return true;
                    }
                }
            });
        }

        public ChooseColumnCfg(AdsFieldRefPropertyDef prop, DdsTableDef table) {
            super(listColumns(table, prop));
        }
    }
    private void btAddColumnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAddColumnActionPerformed
        ChooseColumnCfg cfg = new ChooseColumnCfg(fProp, propRefFieldTbl);
        Definition def = ChooseDefinition.chooseDefinition(cfg);
        if (def instanceof DdsColumnDef && fProp.getFieldToColumnMap().findItemByColumnId(def.getId()) == null) {
            RefMapItem item = RefMapItem.Factory.newInstance();
            item.setColumnId(def.getId());
            fProp.getFieldToColumnMap().add(item);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    update();
                }
            });
        }

    }//GEN-LAST:event_btAddColumnActionPerformed

    private void btRemoveColumnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRemoveColumnActionPerformed
        RefMapItem item = getSelectedRefMapItem();
        if (canDeleteRefMapItem(item)) {
            fProp.getFieldToColumnMap().remove(item);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    update();
                }
            });
        }
    }//GEN-LAST:event_btRemoveColumnActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.radixware.kernel.designer.ads.common.dialogs.AccessEditPanel accessComboBox1;
    private javax.swing.JButton btAddColumn;
    private javax.swing.JButton btRemoveColumn;
    private org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel depFieldRefPropIndexes;
    private org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel depFieldRefPropType;
    private javax.swing.JPanel descriptionPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableFieldRefProp;
    // End of variables declaration//GEN-END:variables
}
