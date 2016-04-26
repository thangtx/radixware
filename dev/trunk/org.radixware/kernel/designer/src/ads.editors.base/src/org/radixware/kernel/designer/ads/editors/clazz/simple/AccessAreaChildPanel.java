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
 * AccessAreaChildPanel.java
 *
 * Created on 11.12.2009, 12:46:08
 */
package org.radixware.kernel.designer.ads.editors.clazz.simple;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import org.jdesktop.swingx.autocomplete.ComboBoxCellEditor;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef.AccessAreas.AccessArea;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef.AccessAreas.AccessArea.Partition;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsInnateColumnPropertyDef;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher.Factory.EntityClassSearcher;
import org.radixware.kernel.common.defs.dds.DdsAccessPartitionFamilyDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EAccessAreaType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.kernel.designer.common.editors.treeTable.TreeGridModel;
import org.radixware.kernel.designer.common.editors.treeTable.TreeGridRow;
import org.radixware.kernel.designer.common.editors.treeTable.TreeTable;
import org.radixware.kernel.designer.common.editors.treeTable.TreeTableModel;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;


public class AccessAreaChildPanel extends javax.swing.JPanel {

    private final AdsEntityClassDef ownerClass;
    private Id searcherId = null;
    private final EntityClassSearcher adsSearcher;
    private TreeTable table;
    private TreeGridModel model;
    private JComboBoxEx comboBox;
    private final AdsInnateColumnPropertyDef prop;
    private final List<AdsEntityClassDef.AccessAreas.AccessArea> ownerAccessAreaList;
    private final static String NONE = "None";
    private final static String INHERIT_THIS = "Inherit this ref";
    private final static String INHERIT_OTHER = "Inherit other ref";
    private final static String OWN_THIS = "Own this ref";
    private final static String OWN_OTHER = "Own other ref";
    private final static String NO_OVERWRITE = "Not overwrite";
    private String sCurrSearchAreaName;
    private DdsAccessPartitionFamilyDef currClassApf;

    public AccessAreaChildPanel(AdsEntityClassDef clazz, String sSearchAreaName, DdsAccessPartitionFamilyDef currClassApf) {
        this.ownerClass = clazz;
        DdsTableDef tbl = ownerClass.findTable(ownerClass);
        prop = (AdsInnateColumnPropertyDef) ownerClass.getProperties().findById(tbl.getPrimaryKey().getColumnsInfo().get(0).getColumnId(), EScope.ALL).get();

        this.currClassApf = currClassApf;
        initComponents();
        ownerAccessAreaList = new ArrayList<AdsEntityClassDef.AccessAreas.AccessArea>();
        collectCurrentAreas(true, clazz, ownerAccessAreaList);


        adsSearcher = AdsSearcher.Factory.newEntityClassSearcher(clazz.getModule());
        textField.setText(sSearchAreaName);
        //if (Math.abs(12)==Math.abs(121))
        //reBuildTree(sSearchAreaName);
        textField.setEnabled(false);
        jButton1.setEnabled(false);
    }

    public void reBuildTree() {
        String sSearchAreaName = textField.getText();
        reBuildTree(sSearchAreaName);
    }

    private class JComboBoxEx extends JComboBox {

        @Override
        public void firePopupMenuWillBecomeInvisible() {
            super.firePopupMenuWillBecomeInvisible();
            if (table.getCellEditor() != null) {
                table.getCellEditor().stopCellEditing();
            }
        }
    }

    private void reBuildTree(String sSearchAreaName) {

        String[] cNames = {"Class", "Areas"};
        Class[] cTypes = {TreeTableModel.class, String.class};
        boolean areaNameIsNotDefined = sSearchAreaName == null || sSearchAreaName.isEmpty();
        if (!areaNameIsNotDefined) {

            brothers.clear();
            TreeGridRowEx treeGridRowEx = new TreeGridRowEx(
                    true,
                    ownerClass,
                    new ArrayList<Id>(0),
                    null,
                    null,
                    false);
            List<TreeGridRowEx> l = new ArrayList<TreeGridRowEx>();
            brothers.put(ownerClass.getId(), l);
            l.add(treeGridRowEx);

            model = new TreeGridModel(
                    treeGridRowEx,
                    cNames,
                    cTypes);

            table = new TreeTable(model);
            table.getColumnModel().getColumn(1).setMaxWidth(140);
            table.getColumnModel().getColumn(1).setMinWidth(120);
            table.getColumnModel().getColumn(1).setMinWidth(0);

            comboBox = new JComboBoxEx();
            table.getColumnModel().getColumn(1).setCellEditor(new ComboBoxCellEditor(comboBox));
        }

        for (int r = jPanel1.getComponentCount() - 1; r >= 0; r--) {
            if (jPanel1.getComponent(r) instanceof JScrollPane
                    || jPanel1.getComponent(r) instanceof JLabel) {

                jPanel1.remove(r);
            }
        }
        JLabel jLabel3 = null;
        textField.setEnabled(true);
        jButton1.setEnabled(true);

        sCurrSearchAreaName = sSearchAreaName;
        if (areaNameIsNotDefined) {
            jLabel3 = new javax.swing.JLabel();
            jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
            jPanel1.setLayout(jPanel1Layout);
            jPanel1Layout.setHorizontalGroup(
                    jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE));
            jPanel1Layout.setVerticalGroup(
                    jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE));

            jLabel3.setText("Enter access area name");
            jLabel3.setVisible(true);
        } else {
            jPanel1.setLayout(new BorderLayout());
            jPanel1.add(new JScrollPane(table), BorderLayout.CENTER);

            Object obj = table.getValueAt(0, 0);
            TreeGridModel.TreeGridNode node = (TreeGridModel.TreeGridNode) obj;


            setValGridNodeChilds(node);
            table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }

        Component p = this;
        while (p != null) {
            if (p instanceof Dialog) {
                int w = p.getSize().width;
                int h = p.getSize().height;
                p.setSize(w, ++h);
                p.setSize(w, --h);
                break;
            }
            p = p.getParent();
        }
    }

    private void SaveGridNodeChilds(TreeGridModel.TreeGridNode node) {

        {
            Object[] vals = node.getValues();
            String value = (String) vals[1];
            TreeGridRowEx treeGridRowEx = (TreeGridRowEx) node.getGridItem();
            if (treeGridRowEx.isMayEdit) {
                if (value.equals(INHERIT_THIS)) {

                    if (!(treeGridRowEx.currClass.getAccessAreas().getType().equals(EAccessAreaType.INHERITED)
                            && treeGridRowEx.currClass.getAccessAreas().getInheritReferenceId().equals(treeGridRowEx.ref.getId()))) {
                        treeGridRowEx.currClass.getAccessAreas().setType(EAccessAreaType.INHERITED);
                        if (treeGridRowEx.ref != null) {
                            treeGridRowEx.currClass.getAccessAreas().setInheritReferenceId(treeGridRowEx.ref.getId());
                        }
                    }

                    //удаляем собственную дефиницию, если она есть!
                    for (AccessArea area : treeGridRowEx.currClass.getAccessAreas()) {
                        if (area.getName().equals(sCurrSearchAreaName)) {
                            for (Partition part : area.getPartitions()) {
                                if (part.getFamilyId().equals(currClassApf.getId())) {
                                    area.getPartitions().remove(part);
                                    break;
                                }
                            }
                            if (area.getPartitions().size() == 0) {
                                treeGridRowEx.currClass.getAccessAreas().remove(area);
                            }
                            break;
                        }
                    }

                    //brothers.remove(treeGridRowEx.currClass.getId());
                    //Данный класс заюзали - удалим его из списка,
                    //чтобы не затереть его при обработки других пустых узлов дерева!
                } else if (value.equals(NONE)) {

                    for (AccessArea area : treeGridRowEx.currClass.getAccessAreas()) {
                        if (area.getName().equals(sCurrSearchAreaName)) {
                            for (Partition prt : area.getPartitions()) {
                                if (prt.getFamilyId().equals(currClassApf.getId())) {
                                    area.getPartitions().remove(prt);
                                }
                            }
                            if (area.getPartitions().isEmpty()) {
                                treeGridRowEx.currClass.getAccessAreas().remove(area);
                            }
                            break;
                        }
                    }
                    if (treeGridRowEx.currClass.getAccessAreas().getType().equals(EAccessAreaType.INHERITED)
                            || treeGridRowEx.currClass.getAccessAreas().getType().equals(EAccessAreaType.NOT_OVERRIDDEN)) {//стало none, было NOT_OVERRIDDEN || INHERITED
                        //значит и правда надо сделать none
                        treeGridRowEx.currClass.getAccessAreas().setType(EAccessAreaType.NONE);
                    }
                    if (treeGridRowEx.currClass.getAccessAreas().getType().equals(EAccessAreaType.OWN)) {
                        if (treeGridRowEx.currClass.getAccessAreas().isEmpty()) {
                            if (!treeGridRowEx.currClass.getAccessAreas().getType().equals(EAccessAreaType.NONE)) {
                                treeGridRowEx.currClass.getAccessAreas().setType(EAccessAreaType.NONE);
                            }
                        }
                    } else {
                        if (!treeGridRowEx.currClass.getAccessAreas().getType().equals(EAccessAreaType.NONE)) {
                            treeGridRowEx.currClass.getAccessAreas().setType(EAccessAreaType.NONE);
                        }
                    }


                } else if (value.equals(NO_OVERWRITE)) {
                    if (treeGridRowEx.isMayEdit) {
                        if (!treeGridRowEx.currClass.getAccessAreas().getType().equals(EAccessAreaType.NOT_OVERRIDDEN)) {
                            treeGridRowEx.currClass.getAccessAreas().setType(EAccessAreaType.NOT_OVERRIDDEN);
                        }
                    }
                } else if (value.equals(OWN_THIS) /*&& !treeGridRowEx.getOldValue().equals(OWN_THIS)*/) {
                    if (!treeGridRowEx.currClass.getAccessAreas().getType().equals(EAccessAreaType.OWN)) {
                        treeGridRowEx.currClass.getAccessAreas().setType(EAccessAreaType.OWN);
                    }
                    AccessArea searchArea = null;
                    for (AccessArea area : treeGridRowEx.currClass.getAccessAreas()) {
                        if (area.getName().equals(sCurrSearchAreaName)) {
                            searchArea = area;
                            break;
                        }
                    }
                    if (searchArea == null) {
                        searchArea = treeGridRowEx.currClass.getAccessAreas().newAccessArea(sCurrSearchAreaName);
                        treeGridRowEx.currClass.getAccessAreas().add(searchArea);
                    }
                    Partition oldPartition = null;
                    for (Partition part : searchArea.getPartitions()) {
                        if (part.getFamilyId().equals(currClassApf.getId())) {
                            oldPartition = part;
                            break;
                        }
                    }

                    // поднимаемся
                    Partition newPartition;
                    {
                        TreeGridRowEx currRowEx = treeGridRowEx;
                        List<DdsReferenceDef> references = new ArrayList<DdsReferenceDef>();
                        while (currRowEx.parent != null) {
                            references.add(currRowEx.ref);
                            currRowEx = currRowEx.parent;
                        }
                        newPartition = searchArea.newPartition(currClassApf, prop, references);
                    }

                    boolean isMustReplased = true;
                    if (oldPartition != null) {
                        isMustReplased = !newPartition.getFamilyId().equals(oldPartition.getFamilyId());
                        if (!isMustReplased) {
                            isMustReplased = !newPartition.getPropertyId().equals(oldPartition.getPropertyId());
                            if (!isMustReplased) {
                                int n = newPartition.getReferenceIds().size();
                                isMustReplased = n != oldPartition.getReferenceIds().size();
                                if (!isMustReplased) {
                                    for (int i = 0; i < n; i++) {
                                        if (!newPartition.getReferenceIds().get(i).equals(
                                                newPartition.getReferenceIds().get(i))) {
                                            isMustReplased = true;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (isMustReplased) {
                        if (oldPartition != null) {
                            searchArea.getPartitions().remove(oldPartition);
                        }
                        searchArea.getPartitions().add(newPartition);
                    }





                }
            }
        }
        TreeGridModel.TreeGridNode[] childs = node.getChilds();
        if (childs != null) {
            for (TreeGridModel.TreeGridNode n : childs) {
                SaveGridNodeChilds(n);
            }
        }
    }

//    private static void unSetGridNodeChilds(boolean isCheck, boolean isSet, TreeGridModel.TreeGridNode node, String newData) {
//        TreeGridRowEx curr = (TreeGridRowEx) node.getGridItem();
//        if (!curr.isMayEdit) {
//            return;
//        }
//        String currVal = ((String) node.getValues()[1]);
//        if (isCheck && (currVal.equals(OWN_THIS) || currVal.equals(OwnOther))) {
//            return;
//        }
//
//
//        if (isSet) {
//            String[] stringArr = new String[2];
//            stringArr[1] = newData;
//            node.setValues(stringArr);
//        }
//
//        TreeGridModel.TreeGridNode[] childs = node.getChilds();
//        if (childs != null) {
//            for (TreeGridModel.TreeGridNode n : childs) {
//                unSetGridNodeChilds(true, true, n, newData);
//            }
//        }
//    }
    private static void setAllThisInheritChilds(String value,
            TreeGridRowEx item) {


        TreeGridModel.TreeGridNode grid = item.getRowEx();
        TreeGridRowEx curr = (TreeGridRowEx) grid.getGridItem();
        String[] stringArr = new String[2];
        stringArr[1] = value;
        curr.getRowEx().setValues(stringArr);
        List<? extends TreeGridRow> childs = item.list;
        if (childs != null) {
            for (TreeGridRow row : childs) {
                TreeGridRowEx rowEx = (TreeGridRowEx) row;
                TreeGridModel.TreeGridNode gridChild = rowEx.getRowEx();
                if (gridChild != null) {
                    String val = ((String) (gridChild.getValues()[1]));
                    if (val.equals(INHERIT_THIS)) {
                        setAllThisInheritChilds(value, rowEx);
                    }
                }
            }
        }


    }

//    private static void clearBrotherNodeChilds(String newData, List<TreeGridRowEx> lst, TreeGridRowEx ignore) {
//
//        for (TreeGridRowEx item : lst) {
//            TreeGridModel.TreeGridNode grid = item.getRowEx();
//
//
//            if (((String) (grid.getValues()[1])).equals(InheritThis)) {
//                if (ignore != grid.getGridItem()) {
//                    unSetGridNodeChilds(false, false, grid, None);
//                    TreeGridRowEx curr = (TreeGridRowEx) grid.getGridItem();
//                    String[] stringArr = new String[2];
//                    stringArr[1] = newData;
//                    curr.getRowEx().setValues(stringArr);
//                }
//            }
//        }
//    }
    private static void setValGridNodeChilds(TreeGridModel.TreeGridNode node) {
        TreeGridRowEx curr = (TreeGridRowEx) node.getGridItem();


        String[] arr = new String[2];
        arr[1] = curr.getAreaValue();
        node.setValues(arr);

        TreeGridModel.TreeGridNode[] childs = node.getChilds();
        if (childs != null) {
            for (TreeGridModel.TreeGridNode n : childs) {
                setValGridNodeChilds(n);
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        textField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText(org.openide.util.NbBundle.getMessage(AccessAreaChildPanel.class, "AccessAreaChildPanel.jLabel2.text")); // NOI18N
        jLabel2.setAlignmentX(0.5F);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 451, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE)
        );

        textField.setText(org.openide.util.NbBundle.getMessage(AccessAreaChildPanel.class, "AccessAreaChildPanel.textField.text")); // NOI18N
        textField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textFieldActionPerformed(evt);
            }
        });

        jLabel1.setText(org.openide.util.NbBundle.getMessage(AccessAreaChildPanel.class, "AccessAreaChildPanel.jLabel1.text")); // NOI18N

        jButton1.setIcon(RadixWareDesignerIcon.EDIT.EDIT.getIcon());
        jButton1.setText(org.openide.util.NbBundle.getMessage(AccessAreaChildPanel.class, "AccessAreaChildPanel.jButton1.text")); // NOI18N
        jButton1.setToolTipText(org.openide.util.NbBundle.getMessage(AccessAreaChildPanel.class, "AccessAreaChildPanel.jButton1.toolTipText")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textField, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(textField, javax.swing.GroupLayout.Alignment.LEADING))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void textFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textFieldActionPerformed
        reBuildTree(textField.getText());
    }//GEN-LAST:event_textFieldActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        reBuildTree(textField.getText());
      //  System.out.println(textField.getText());
    }//GEN-LAST:event_jButton1ActionPerformed
    boolean isMustRebuild = true;
    private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained
      //  System.out.println("PropertyChange");
        if (isMustRebuild) {
            isMustRebuild = false;
            textField.setEnabled(false);
            jButton1.setEnabled(false);
            textField.repaint();
            jButton1.repaint();
            this.reBuildTree();
        }
   //     System.out.println("PropertyChange2");
    //    System.out.println(evt.paramString());
    }//GEN-LAST:event_formFocusGained
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField textField;
    // End of variables declaration//GEN-END:variables

    public void apply() {
        if (table == null) {
            return;
        }
        Object obj = table.getValueAt(0, 0);
        TreeGridModel.TreeGridNode node = (TreeGridModel.TreeGridNode) obj;
//        if (grid.)
        //grid.editingStopped()
        //TreeGridModel.get
        if (table.getCellEditor() != null) {
            table.getCellEditor().stopCellEditing();
        }
        SaveGridNodeChilds(node);
    }
    private HashMap<Id, List<TreeGridRowEx>> brothers = new HashMap<Id, List<TreeGridRowEx>>();

    private class TreeGridRowEx extends TreeGridRow {

        boolean isMayChild;
        boolean isCheckInherit;
        List<TreeGridRowEx> list = null;
        final private Icon icon;
        final DdsTableDef tbl;
        final String title;
        final AdsEntityClassDef currClass;
        final TreeGridRowEx parent;
        final DdsReferenceDef ref;
        final Boolean isMayEdit;
        final List<Id> idList_;
        private TreeGridModel.TreeGridNode row = null;
        private String oldValue;

        public String getOldValue() {
            return oldValue;
        }

        @Override
        public final String getName() {
            return currClass.getQualifiedName() + (ref == null ? "" : " - (" + ref.getName() + ")");
        }

        public TreeGridRowEx(boolean isCheckInherit, AdsEntityClassDef currClass, List<Id> idList, TreeGridRowEx parent, DdsReferenceDef ref_, boolean isMustDisable) {

            this.idList_ = idList;
            //isClearAreas = false;
            this.isCheckInherit = isCheckInherit;



            isMayEdit = !currClass.isReadOnly() && !isMustDisable;
            this.ref = ref_;
            this.parent = parent;
            this.currClass = currClass;
            title = getName();

            tbl = currClass.findTable(currClass);
            isMayChild = false;


            if (tbl != null) {
                Set<DdsReferenceDef> refSet = tbl.collectIncomingReferences();
                for (DdsReferenceDef ref2 : refSet) {
                    if (ref2.getColumnsInfo().size() == 1) {
                        final DdsTableDef childTbl = ref2.findChildTable(currClass);
                        if (childTbl != null) {
                            AdsEntityClassDef childClazz = adsSearcher.findEntityClass(childTbl).get();
                            if (childClazz != null) {
                                TreeGridRowEx tmp = this;
                                boolean find = false;
                                while (tmp != null) {
                                    if (tmp.currClass == childClazz) {
                                        find = true;
                                        break;
                                    }
                                    tmp = tmp.parent;
                                }
                                if (!find) {
                                    isMayChild = true;
                                    break;
                                }
                                //if (childClazz.getAccessAreas().getType().equals(EAccessAreaType.OWN)){}
                            }
                            if (childClazz == null) {
                                childClazz = AdsUtils.findEntityClass(childTbl);
                            }
                        }
                    }
                }
                if (!isMayChild) {
                    for (DdsReferenceDef ref2 : refSet) {
                        if (ref2.getColumnsInfo().size() == 1) {
                            final DdsTableDef childTbl = ref2.findChildTable(currClass);
                            if (childTbl != null) {
                                AdsEntityClassDef childClazz = AdsUtils.findEntityClass(childTbl);
                                if (childClazz != null) {
                                    TreeGridRowEx tmp = this;
                                    boolean find = false;
                                    while (tmp != null) {
                                        if (tmp.currClass == childClazz) {
                                            find = true;
                                            break;
                                        }
                                        tmp = tmp.parent;
                                    }
                                    if (!find) {
                                        isMayChild = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            icon = currClass.getIcon().getIcon();
        }

        @Override
        public Icon getIcon(int row) {
            return icon;
        }

        private String getAreaValue() {
//            if (this.currClass.getName().equals(  "Terminal"))
//                if (this.currClass.getName().equals(  "Terminal"));
            AdsEntityClassDef clazz = this.currClass;
            if (clazz.getAccessAreas().getType().equals(EAccessAreaType.NONE)) {
                return NONE;
            }
            //currClass.getAccessAreas().size();

            for (AccessArea currArea : clazz.getAccessAreas()) {
                if (currArea.getName().equals(sCurrSearchAreaName)) {    //find

                    for (Partition partition : currArea.getPartitions()) {
                        if (partition.getFamilyId().equals(currClassApf.getId())) {
                            if (clazz.getAccessAreas().getType().equals(EAccessAreaType.OWN)
                                    || clazz.getAccessAreas().getType().equals(EAccessAreaType.INHERITED) //вне зависимости от того own or inherit
                                    //все равно это собственная область определённая у этого класса
                                    ) //if (isPartitionEquals(idList, part1, part2))
                            {
                                List<Id> refList = partition.getReferenceIds();
                                if (refList.size() == idList_.size()) {
                                    boolean isEqual = true;

                                    int i = 0;
                                    for (Id id : idList_) {
                                        if (!refList.get(i).equals(id)) {
                                            isEqual = false;
                                            break;
                                        }
                                        i++;
                                    }

                                    if (isEqual) {
                                        return OWN_THIS;
                                    }

                                }
                                return OWN_OTHER;
                            }
                        }
                    }
                    //currClassApf
                    break;
                }
            }

            if (clazz.getAccessAreas().getType().equals(EAccessAreaType.INHERITED)) {
                //if ( this.parent != null)
                {
                    Object[] parentVals = this.parent.getRowEx().getValues();

                    if ((parentVals[1].equals(OWN_THIS)
                            || parentVals[1].equals(INHERIT_THIS))
                            && this.ref != null
                            && this.ref.getId().equals(clazz.getAccessAreas().getInheritReferenceId())) {
                        return INHERIT_THIS;
                    }
                    return INHERIT_OTHER;
                }
            }






            return NONE;
        }

        @Override
        public boolean isHaveChilds() {
            return isMayChild;
        }

        @Override
        public List<? extends TreeGridRow> getChilds() {
            if (!isMayChild) {
                return null;
            }
            if (list == null) {
                list = new ArrayList<TreeGridRowEx>(0);
                if (tbl != null) {
                    Set<DdsReferenceDef> refSet = tbl.collectIncomingReferences();
                    //int i=0;++i<10 &&
                    for (DdsReferenceDef ref2 : refSet) {

                        if (ref2.getColumnsInfo().size() == 1) {
                            final DdsTableDef childTbl = ref2.findChildTable(currClass);
                            if (childTbl != null) {
                                AdsEntityClassDef childClazz = adsSearcher.findEntityClass(childTbl).get();
                                if (childClazz == null) {
                                    childClazz = AdsUtils.findEntityClass(childTbl);
                                }

                                if (childClazz != null) {
                                    TreeGridRowEx tmp = this;
                                    boolean find = false;
                                    while (tmp != null) {
                                        if (tmp.currClass == childClazz) {
                                            find = true;
                                            break;
                                        }
                                        tmp = tmp.parent;
                                    }
                                    if (!find) {

                                        List<Id> childList = new ArrayList<Id>(idList_);
                                        childList.add(0, ref2.getId());


                                        currClass.getAccessAreas().size();

                                        if (ref2 != null) {
                                            list.add(new TreeGridRowEx(
                                                    (currClass.getAccessAreas().size() == 0
                                                    || currClass == ownerClass)
                                                    && ref2.getId().equals(childClazz.getAccessAreas().getInheritReferenceId())
                                                    && isCheckInherit
                                                    && childClazz.getAccessAreas().getType().equals(EAccessAreaType.INHERITED),
                                                    childClazz, childList, this, ref2, !isMayEdit));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                RadixObjectsUtils.sortByName(list);
                for (TreeGridRowEx child : list) {
                    List<TreeGridRowEx> l = brothers.get(child.currClass.getId());
                    if (l == null) {
                        l = new ArrayList<TreeGridRowEx>();
                        brothers.put(child.currClass.getId(), l);
                    }
                    l.add(child);
                }
            }
            return list;
        }

        @Override
        public String getTitle(int column) {

            if (column == 0) {
                return title;
            }

            return null;

        }

        @Override
        public boolean isMayModify(int column) {
            //table;
            //TreeGridModel model this.currClass
            if (column == 1 && (!isMayEdit)) {
                return false;
            }
            comboBox.removeAllItems();
            comboBox.addItem(NONE);
            if (this.parent != null) {
                comboBox.addItem(INHERIT_THIS);
            }
            comboBox.addItem(OWN_THIS);
            if (this.currClass.getHierarchy().findOverwritten() != null) {
                comboBox.addItem(NO_OVERWRITE);
            }
            return true;
        }

        void replaseInheritThis(TreeGridRowEx from, TreeGridRowEx to) {
            List<? extends TreeGridRow> fromList = from.list;
            List<? extends TreeGridRow> toList = to.list;

            TreeGridModel.TreeGridNode toNode = to.getRowEx();
            if (toNode != null) {
                Object arr[] = new Object[2];
                arr[1] = INHERIT_THIS;
                toNode.setValues(arr);
            }

            if (fromList != null) {
                for (TreeGridRow fromChild : fromList) {
                    TreeGridModel.TreeGridNode currNode = fromChild.getRowEx();
                    if (currNode != null) {
                        String value = (String) ((currNode.getValues())[1]);
                        if (value.equals(INHERIT_THIS)) {
                            TreeGridRowEx fromChildEx = (TreeGridRowEx) fromChild;
                            TreeGridRowEx findToChildEx = null;
                            for (TreeGridRow toChild : toList) {
                                TreeGridRowEx toChildEx = (TreeGridRowEx) toChild;
                                if (toChildEx.ref == fromChildEx.ref) {
                                    findToChildEx = toChildEx;
                                    break;
                                }
                            }
                            if (findToChildEx != null) {
                                replaseInheritThis(fromChildEx, findToChildEx);
                            }
                        }
                    }
                }
            }





        }

        void setInheritThis(TreeGridRowEx item) {
            List<TreeGridRowEx> brotherList = brothers.get(item.currClass.getId());
            TreeGridRowEx oldThisInherit = null;
            for (TreeGridRowEx brother : brotherList) {
                if (brother != this) {
                    TreeGridModel.TreeGridNode currNode = brother.getRowEx();
//                        if (brother.parent.parent == null)
//                            if (brother.parent.parent == null);
                    if (currNode != null) {
                        String value = (String) ((currNode.getValues())[1]);
                        if (value.equals(INHERIT_THIS) || value.equals(OWN_THIS) || value.equals(NO_OVERWRITE)) {
                            oldThisInherit = brother;
                            break;
                        }
                    }
                }
            }
            if (oldThisInherit != null) {
                replaseInheritThis(oldThisInherit, item);
                setAllThisInheritChilds(INHERIT_OTHER, oldThisInherit);
            }



            for (TreeGridRowEx brother : brotherList) {
                if (brother != item && brother != oldThisInherit) {
                    TreeGridModel.TreeGridNode currNode = brother.getRowEx();
                    if (currNode != null) {
                        Object arr[] = new Object[2];
                        arr[1] = INHERIT_OTHER;
                        currNode.setValues(arr);
                    }
                }
            }

            {
                TreeGridModel.TreeGridNode currNode = item.getRowEx();

                if (currNode != null) {
                    Object arr[] = new Object[2];
                    arr[1] = INHERIT_THIS;
                    currNode.setValues(arr);
                }

            }



        }

        @Override
        public void CellWasModified(int column, Object val) {

            String data = (String) val;
            Object obj = table.getValueAt(table.getSelectedRow(), 0);
            TreeGridModel.TreeGridNode node = (TreeGridModel.TreeGridNode) obj;

            //Находим старого inherit this
            List<TreeGridRowEx> brotherList = brothers.get(this.currClass.getId());
            TreeGridRowEx oldThisInherit = null;
            for (TreeGridRowEx brother : brotherList) {
                if (brother != this) {
                    TreeGridModel.TreeGridNode currNode = brother.getRowEx();
//                        if (brother.parent.parent == null)
//                            if (brother.parent.parent == null);
                    if (currNode != null) {
                        String value = (String) ((currNode.getValues())[1]);
                        if (value.equals(INHERIT_THIS) || value.equals(OWN_THIS) || value.equals(NO_OVERWRITE)) {
                            oldThisInherit = brother;
                            break;
                        }
                    }
                }
            }
            // ------------------------------------------------------
            if (data.equals(INHERIT_THIS)) {

                //сперва поднимаемся вверх до ближайшего не inherit other и не inherit own
                TreeGridRowEx curr = this;
                while (curr != null) {
                    TreeGridModel.TreeGridNode currNode = curr.getRowEx();
                    if (currNode != null) {
                        String value = (String) ((currNode.getValues())[1]);
                        if (curr != this
                                && (value.equals(INHERIT_THIS)
                                || value.equals(OWN_THIS))) {
                            break;
                        }
                        //currNode = currNode.getParent();
//                    Object arr[] = new Object[2];
//                    arr[1] = InheritThis;
//                    currNode.setValues(arr); 
                    }
                    setInheritThis(curr);
                    curr = curr.parent;
                    if (curr.parent == null) {
                        TreeGridModel.TreeGridNode currNode2 = curr.getRowEx();
                        Object arr[] = new Object[2];
                        arr[1] = OWN_THIS;
                        currNode2.setValues(arr);
                        break;
                    }
                }






            } // ------------------------------------------------------
            else if (data.equals(NONE)) {
                setNone((TreeGridRowEx) node.getGridItem(), new HashMap<Id, Id>());
//                TreeGridRowEx currGridRowEx = (TreeGridRowEx) node.getGridItem();
//                List<TreeGridRowEx> l = brothers.get(currGridRowEx.currClass.getId());
//                for (TreeGridRowEx item : l) {
//                    setAllThisInheritChilds(None, item);
//                }
            } // ------------------------------------------------------
            else if (data.equals(OWN_THIS)) {
                if (oldThisInherit != null) {
                    replaseInheritThis(oldThisInherit, this);
                    setAllThisInheritChilds(INHERIT_OTHER, oldThisInherit);
                }
                TreeGridRowEx currGridRowEx = (TreeGridRowEx) node.getGridItem();
                List<TreeGridRowEx> lst = brothers.get(currGridRowEx.currClass.getId());
                for (TreeGridRowEx item : lst) {
                    TreeGridModel.TreeGridNode r = item.getRowEx();
                    if (r != null && currGridRowEx != item) {
                        String[] stringArr = new String[2];
                        stringArr[1] = OWN_OTHER;
                        r.setValues(stringArr);
                    }
                }
                {
                    TreeGridModel.TreeGridNode r = this.getRowEx();
                    if (r != null) {
                        String[] stringArr = new String[2];
                        stringArr[1] = OWN_THIS;
                        r.setValues(stringArr);
                    }
                }

            } // ------------------------------------------------------
            else //if (data.equals(NotOverwrite))
            {
                if (oldThisInherit != null) {
                    replaseInheritThis(oldThisInherit, this);
                    setAllThisInheritChilds(INHERIT_OTHER, oldThisInherit);
                }
                TreeGridRowEx currGridRowEx = (TreeGridRowEx) node.getGridItem();
                List<TreeGridRowEx> l = brothers.get(currGridRowEx.currClass.getId());
                //clearBrotherNodeChilds(None, l, currGridRowEx);
                for (TreeGridRowEx item : l) {
                    TreeGridModel.TreeGridNode r = item.getRowEx();

                    if (r != null && currGridRowEx != item) {
                        String arr[] = new String[2];
                        arr[1] = NO_OVERWRITE;
                        r.setValues(arr);
                    }
                }
                {
                    TreeGridModel.TreeGridNode r = this.getRowEx();
                    if (r != null) {
                        String[] stringArr = new String[2];
                        stringArr[1] = NO_OVERWRITE;
                        r.setValues(stringArr);
                    }
                }
            }
            table.repaint();
        }
//        private void setNone(TreeGridRowEx currGridRowEx)
//        {
//            List<TreeGridRowEx> list = brothers.get(currGridRowEx.currClass.getId());
//        }

        private void setNone(TreeGridRowEx item2, HashMap<Id, Id> usingItems) {
            Id id = item2.currClass.getId();
            if (usingItems.get(id) != null) {
                return;
            }
            usingItems.put(id, id);



            List<TreeGridRowEx> list_ = brothers.get(item2.currClass.getId());
            //if (list_!=)
            for (TreeGridRowEx item : list_) {
                TreeGridModel.TreeGridNode grid = item.getRowEx();
                if (grid != null) {
                    String[] stringArr = new String[2];
                    stringArr[1] = NONE;
                    item.getRowEx().setValues(stringArr);
                    List<? extends TreeGridRow> childs = item.list;
                    if (childs != null) {
                        for (TreeGridRow row_ : childs) {
                            TreeGridRowEx rowEx = (TreeGridRowEx) row_;
                            TreeGridModel.TreeGridNode gridChild = rowEx.getRowEx();
                            if (gridChild != null) {
                                String val = ((String) (gridChild.getValues()[1]));
                                if (val.equals(INHERIT_THIS)) {
                                    setNone(rowEx, usingItems);
                                }
                            }
                        }
                    }
                }
            }

        }

        @Override
        public TreeGridModel.TreeGridNode getRowEx() {
            return row;
        }

        @Override
        public void setRowEx(TreeGridModel.TreeGridNode row) {
            this.row = row;
        }

        @Override
        public void loadValues() {
            TreeGridModel.TreeGridNode node = getRowEx();
            TreeGridRowEx curr = (TreeGridRowEx) node.getGridItem();
            //if (curr.parent!=null)
            //{

            String[] arr = new String[2];
//            if (this.currClass.getName().equals(  "Terminal"))
//                if (this.currClass.getName().equals(  "Terminal"));
            String s = null;
            List<TreeGridRowEx> l = brothers.get(curr.currClass.getId());
            if (l != null) {
                for (TreeGridRowEx br : l) {
                    if (br.getRowEx() != null) {
                        Object brArr[] = br.getRowEx().getValues();

                        if (brArr[1] != null) {
                            if (brArr[1].equals(NONE)) {
                                s = NONE;
                                break;
                            } else if (brArr[1].equals(NO_OVERWRITE)) {
                                s = NO_OVERWRITE;
                                break;
                            } else if (brArr[1].equals(OWN_THIS)) {
                                s = OWN_OTHER;
                                break;
                            } else if (brArr[1].equals(INHERIT_THIS)) {
                                //  if (br.ref != curr.ref)
                                {
                                    s = INHERIT_OTHER;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            arr[1] = s == null ? curr.getAreaValue() : s;
            oldValue = arr[1];
            //isWasOther = arr[1].equals(Other);
            node.setValues(arr);
            //}
        }
    }

    private void collectCurrentAreas(boolean isUseCurrent, AdsEntityClassDef clazz, List<AdsEntityClassDef.AccessAreas.AccessArea> list) {
        //java.lang.reflect.c
        //Class d = void.class;

        if (clazz.getAccessAreas().getType().equals(EAccessAreaType.NONE)) {
            return;
        }
        if (isUseCurrent
                && (clazz.getAccessAreas().getType().equals(EAccessAreaType.OWN)
                || clazz.getAccessAreas().getType().equals(EAccessAreaType.INHERITED))) {
            Iterator<AdsEntityClassDef.AccessAreas.AccessArea> iterArea1 = clazz.getAccessAreas().iterator();
            while (iterArea1.hasNext()) {
                AdsEntityClassDef.AccessAreas.AccessArea area1 = iterArea1.next();
                list.add(area1);
            }
        }
        if (clazz.getAccessAreas().getType().equals(EAccessAreaType.NOT_OVERRIDDEN)) {
            AdsEntityClassDef owrClazz = (AdsEntityClassDef) clazz.getHierarchy().findOverwritten().get();
            if (owrClazz != null) {
                collectCurrentAreas(true, owrClazz, list);
            }
        }
        if (clazz.getAccessAreas().getType().equals(EAccessAreaType.INHERITED)) {
            DdsReferenceDef ref = clazz.getAccessAreas().findInheritReference();
            if (ref != null) {
                Id classId = Id.Factory.changePrefix(ref.getParentTableId(), EDefinitionIdPrefix.ADS_ENTITY_CLASS);
                AdsEntityClassDef owrClazz = (AdsEntityClassDef) findDefinition(classId);
                if (owrClazz != null) {
                    collectCurrentAreas(true, owrClazz, list);
                }
            }
        }
    }

    public Definition findDefinition(Id currId) {
        searcherId = currId;
        return (Definition) ownerClass.getModule().getSegment().getLayer().find(
                new VisitorProvider() {
            @Override
            public boolean isTarget(RadixObject radixObject) {
                if (radixObject instanceof Definition) {
                    if (((Definition) radixObject).getId().equals(searcherId)) {
                        return true;
                    }
                }
                return false;
            }
        });
    }
}
