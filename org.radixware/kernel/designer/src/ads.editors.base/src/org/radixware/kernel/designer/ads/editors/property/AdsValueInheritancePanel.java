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
package org.radixware.kernel.designer.ads.editors.property;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IEnumDef;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsInnateColumnPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsParentRefPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsServerSidePropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsTablePropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsUserPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.ValueInheritanceRules.InheritancePath;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.ParentRefType;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EPropInitializationPolicy;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionSequence;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionSequence.ChooseDefinitionCfgs;

public class AdsValueInheritancePanel extends javax.swing.JPanel {

    private final ChangeListener valueInheritanceRulesChangedListener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            if (isReadOnly || !isMayModify) {
                return;
            }
            prop.getValueInheritanceRules().setInheritanceMark(propertyValueEditPanel.getValue());
            //prop.getValueInheritanceRules().getInitializationPolicy()
        }
    };

    private class MyVisitorProvider extends VisitorProvider {

        HashMap<RadixObject, RadixObject> list = new HashMap<>();

        MyVisitorProvider(AdsEntityObjectClassDef tbl, boolean showProps, AdsTypeDeclaration type, boolean firstStep) {
            List<AdsPropertyDef> lst = tbl.getProperties().get(EScope.ALL);
            for (AdsPropertyDef p : lst) {
                if (firstStep && p == prop) {
                    continue;
                }
                if (showProps
                        && p instanceof AdsServerSidePropertyDef
                        && //!p.getValue().getType().getTypeId().equals(EValType.PARENT_REF) &&
                        isTypeCastable(type, p.getValue().getType())
                        || p.getValue().getType().getTypeId().equals(EValType.PARENT_REF)) {
                    list.put(p, null);
                }
            }
        }

        private boolean isTypeCastable(AdsTypeDeclaration myType, AdsTypeDeclaration otherType) {
            return AdsTypeDeclaration.isAssignable(myType, otherType, prop);
        }

        @Override
        public boolean isTarget(RadixObject object) {
            return list.containsKey(object);
        }
    }

    class ChooseDefinitionCfg2 extends ChooseDefinitionCfg {

        ChooseDefinitionCfg prev;

        protected ChooseDefinitionCfg2(
                final RadixObject context,
                final VisitorProvider provider, ChooseDefinitionCfg prev) {
            super(context, provider);
            this.prev = prev;
        }
    }

    private class ChooseDefinitionCfgs2 extends ChooseDefinitionCfgs {

        Definition context;
        AdsTypeDeclaration type;

        ChooseDefinitionCfgs2(ChooseDefinitionCfg2 init, Definition context, AdsTypeDeclaration type) {
            super(init);
            this.type = type;
            this.context = context;
        }

        @Override
        protected ChooseDefinitionCfg nextConfig(Definition choosenDef) {
            return null;
        }

        @Override
        protected ChooseDefinitionCfg nextConfig(ChooseDefinitionCfg prev, Definition choosenDef) {
            AdsPropertyDef currProp = (AdsPropertyDef) choosenDef;

            AdsEntityObjectClassDef cd = null;

            if (choosenDef instanceof AdsParentRefPropertyDef) {
                AdsParentRefPropertyDef propRef = (AdsParentRefPropertyDef) choosenDef;
                cd = propRef.findReferencedEntityClass();
            } else {
                AdsType type_ = currProp.getValue().getType().resolve(prop).get();
                if (type_ instanceof ParentRefType) {
                    ParentRefType pref = (ParentRefType) type_;
                    cd = pref.getSource();
                }
            }

            if (cd != null) {
                return new ChooseDefinitionCfg2(context, new MyVisitorProvider(cd, true, type, prev == null), prev);
            }
            return null;
        }

        @Override
        protected boolean hasNextConfig(Definition choosenDef) {
            AdsPropertyDef currProp = (AdsPropertyDef) choosenDef;
            if (choosenDef instanceof AdsParentRefPropertyDef) {
                AdsParentRefPropertyDef propRef = (AdsParentRefPropertyDef) choosenDef;
                return propRef.findReferencedEntityClass() != null;
            } else {
                AdsType type_ = currProp.getValue().getType().resolve(prop).get();
                if (type_ instanceof ParentRefType) {
                    ParentRefType pref = (ParentRefType) type_;
                    return null != pref.getSource();
                }
            }
            return false;
        }

        @Override
        protected boolean isFinalTarget(ChooseDefinitionCfg cfg, Definition choosenDef) {
            //boolean b = choosenDef instanceof AdsInnateColumnPropertyDef;
            AdsPropertyDef prop = (AdsPropertyDef) choosenDef;
            //prop.getValue()!=null            
            if (prop == AdsValueInheritancePanel.this.prop && cfg instanceof ChooseDefinitionCfg2 && ((ChooseDefinitionCfg2) cfg).prev == null) {
                return false;
            }
            return super.isFinalTarget(cfg, choosenDef);
            //return choosenDef instanceof AdsInnateColumnPropertyDef;

        }

        @Override
        protected boolean isFinalTarget(Definition choosenDef) {
            //boolean b = choosenDef instanceof AdsInnateColumnPropertyDef;
            AdsPropertyDef prop = (AdsPropertyDef) choosenDef;
            //prop.getValue()!=null            
            if (prop.getValue().getType().equals(type)) {
                return true;
            }
            return false;
            //return choosenDef instanceof AdsInnateColumnPropertyDef;
        }

        @Override
        public String getDisplayName() {
            return "Choose Object";
        }
    }

    private String pathToString(InheritancePath path) {

        String rez = "this";

        AdsClassDef cd = prop.getOwnerClass();
        AdsEntityObjectClassDef ecd = null;
        if (cd instanceof AdsEntityObjectClassDef) {
            ecd = (AdsEntityObjectClassDef) cd;
            if (ecd != null) {
                for (Id id : path.getReferenceIds()) {
                    boolean find = false;
                    for (AdsPropertyDef p : ecd.getProperties().get(EScope.ALL)) {
                        if (p.getId().equals(id)) {

                            if (p instanceof AdsParentRefPropertyDef) {
                                AdsParentRefPropertyDef propRef = (AdsParentRefPropertyDef) p;
                                ecd = propRef.findReferencedEntityClass();
                            } else {
                                AdsType type_ = p.getValue().getType().resolve(prop).get();
                                if (type_ instanceof ParentRefType) {
                                    ParentRefType pref = (ParentRefType) type_;
                                    ecd = pref.getSource();
                                }
                            }

                            //AdsInnateRefPropertyDef propRef = (AdsInnateRefPropertyDef) p;
                            if (ecd != null) {
                                rez += "->" + ecd.getName() + "(" + p.getName() + ")";
                                //ecd = (AdsEntityClassDef) propRef.findReferencedEntityClass();
                                find = true;
                            }
                            break;
                        }
                    }
                    if (!find) {
                        return rez + "->???";
                    }
                }
            }
        }

        if (ecd != null) {
            for (AdsPropertyDef p : ecd.getProperties().get(EScope.ALL)) {
                if (p.getId().equals(path.getPropertyId())) {
                    return rez + "." + p.getName();
                }
            }
        }
        return rez + ".???";
    }

    public void setEnum(IEnumDef enumDef, ValAsStr initialValAsStr) {
        isMayModify = false;
        propertyValueEditPanel.setEnum(enumDef, initialValAsStr);
        isMayModify = true;
    }

    public void setValue(AdsPropertyDef property, ValAsStr initialValAsStr) {
        isMayModify = false;
        AdsTypeDeclaration typeDeclaration = property.getValue().getType();
        AdsType type = typeDeclaration.resolve(property).get();
        DdsTableDef targetTable = null;
        EValType valType = typeDeclaration.getTypeId();
        if (type instanceof AdsClassType) {
            AdsClassDef clazz = ((AdsClassType) type).getSource();
            if (clazz instanceof AdsEntityObjectClassDef) {
                targetTable = ((AdsEntityObjectClassDef) clazz).findTable(property);
            }
        }
        propertyValueEditPanel.setValue(valType, targetTable, initialValAsStr);
        isMayModify = true;
    }

    private void fillValueInheritanceRulesStateDisabled() {
        DefaultListModel model = (DefaultListModel) jListInhVal.getModel();
        boolean isDisable = !prop.getValueInheritanceRules().getInheritable() || isReadOnly;

        propertyValueEditPanel.setEnabled(!isDisable);
        if (isDisable) {
            jbtAddValInh.setEnabled(false);
            jbtDelValInh.setEnabled(false);
            jbtUpValInh.setEnabled(false);
            jbtDownValInh.setEnabled(false);
        } else {
            jbtAddValInh.setEnabled(true);
            jbtDelValInh.setEnabled(model.getSize() > 0);
            jbtUpValInh.setEnabled(model.getSize() > 0 && jListInhVal.getSelectedIndex() != 0);
            jbtDownValInh.setEnabled(model.getSize() > 0 && jListInhVal.getSelectedIndex() != model.getSize() - 1);
        }
    }

    private void fillValueInheritanceRules() {

        // TODO
        // jcbAllowValueInheritance.setSelected(prop.getValueInheritanceRules().getInheritable());
        // jcbAllowValueInheritance.setEnabled(!isReadOnly);
        setValue(prop, prop.getValueInheritanceRules().getInheritanceMark());

        DefaultListModel model = (DefaultListModel) jListInhVal.getModel();
        if (!(prop instanceof AdsTablePropertyDef)) {
            model.setSize(0);
            fillValueInheritanceRulesStateDisabled();
            return;
        }
        int i = 0;
        model.setSize(prop.getValueInheritanceRules().getPathes().size());
        for (InheritancePath path : prop.getValueInheritanceRules().getPathes()) {
            model.set(i, pathToString(path));
            i++;
        }
        if (model.getSize() > 0) {
            jListInhVal.setSelectedIndex(0);
        }
        
        if (prop instanceof AdsInnateColumnPropertyDef && prop.getValueInheritanceRules().getInheritable()) {
            final DdsColumnDef column = ((AdsInnateColumnPropertyDef) prop).getColumnInfo().findColumn();
            if (column != null && column.isNotNull()) {
                ValAsStr valAsStr = prop.getValueInheritanceRules().getInheritanceMark();
                if (valAsStr == null) {
                    propertyValueEditPanel.setForeground(Color.RED);
                } else {
                    propertyValueEditPanel.setForeground(Color.BLACK);
                }
            }
        }

        fillValueInheritanceRulesStateDisabled();
    }

    /**
     * Creates new form AdsValueInheritancePanel
     */
    public AdsValueInheritancePanel() {
        initComponents();
        jListInhVal.setModel(new DefaultListModel());
        jListInhVal.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        propertyValueEditPanel.addChangeListener(valueInheritanceRulesChangedListener);

        cmbValueInitMode.removeAllItems();

        cmbValueInitMode.addItem("Don't define");
        cmbValueInitMode.addItem("Define if not inherited");
        cmbValueInitMode.addItem("Define always");

    }
    AdsTablePropertyDef prop = null;
    private boolean isReadOnly = false;
    private boolean isMayModify = false;

    public void setReadOnly(boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
        boolean isDisable = !prop.getValueInheritanceRules().getInheritable() || isReadOnly;
        cmbValueInitMode.setEnabled(!isDisable);
    }
    boolean isUserProp = false;

    public void open(AdsTablePropertyDef prop) {
        this.prop = prop;
        isUserProp = prop instanceof AdsUserPropertyDef;
        update();
    }

    private void setVisibleValueInitMode(boolean visible) {
        lblValueInitMode.setVisible(visible);
        cmbValueInitMode.setVisible(visible);
    }

    public void update() {
        isMayModify = false;
        boolean isVisible = !(prop instanceof AdsUserPropertyDef);
        jLabel1.setVisible(isVisible);
        propertyValueEditPanel.setVisible(isVisible);

        setVisibleValueInitMode(isVisible);

        doLayout();

        if (prop.getValueInheritanceRules().getInitializationPolicy().equals(
                EPropInitializationPolicy.DO_NOT_DEFINE)) {
            cmbValueInitMode.setSelectedIndex(0);
        } else if (prop.getValueInheritanceRules().getInitializationPolicy().equals(
                EPropInitializationPolicy.DEFINE_IF_NOT_INHERITED)) {
            cmbValueInitMode.setSelectedIndex(1);
        } else //if (prop.getValueInheritanceRules().getInitializationPolicy().equals(
        //   EPropInitializationPolicy.DEFINE_ALWAYS))
        {
            cmbValueInitMode.setSelectedIndex(2);
        }
        boolean isDisable = !prop.getValueInheritanceRules().getInheritable() || isReadOnly;
        cmbValueInitMode.setEnabled(!isDisable);

        fillValueInheritanceRules();
        isMayModify = true;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        propertyValueEditPanel = new org.radixware.kernel.designer.common.dialogs.components.PropertyValueEditPanel();
        jLabel1 = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        jbtAddValInh = new javax.swing.JButton();
        jbtDelValInh = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jbtUpValInh = new javax.swing.JButton();
        jbtDownValInh = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jListInhVal = new javax.swing.JList();
        cmbValueInitMode = new javax.swing.JComboBox();
        lblValueInitMode = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 8, 4);
        add(propertyValueEditPanel, gridBagConstraints);

        jLabel1.setText(org.openide.util.NbBundle.getMessage(AdsValueInheritancePanel.class, "AdsValueInheritancePanel.jLabel1.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 8, 0);
        add(jLabel1, gridBagConstraints);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jbtAddValInh.setIcon(RadixWareDesignerIcon.CREATE.ADD.getIcon());
        jbtAddValInh.setToolTipText(org.openide.util.NbBundle.getMessage(AdsValueInheritancePanel.class, "AdsValueInheritancePanel.jbtAddValInh.toolTipText")); // NOI18N
        jbtAddValInh.setFocusable(false);
        jbtAddValInh.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtAddValInh.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtAddValInh.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtAddValInh.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtAddValInh.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtAddValInh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtAddValInhActionPerformed(evt);
            }
        });
        jToolBar1.add(jbtAddValInh);

        jbtDelValInh.setIcon(RadixWareDesignerIcon.CREATE.DELETE.DELETE.getIcon());
        jbtDelValInh.setToolTipText(org.openide.util.NbBundle.getMessage(AdsValueInheritancePanel.class, "AdsValueInheritancePanel.jbtDelValInh.toolTipText")); // NOI18N
        jbtDelValInh.setFocusable(false);
        jbtDelValInh.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtDelValInh.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtDelValInh.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtDelValInh.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtDelValInh.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtDelValInh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtDelValInhActionPerformed(evt);
            }
        });
        jToolBar1.add(jbtDelValInh);
        jToolBar1.add(jSeparator1);

        jbtUpValInh.setIcon(RadixWareDesignerIcon.ARROW.MOVE_UP.getIcon());
        jbtUpValInh.setToolTipText(org.openide.util.NbBundle.getMessage(AdsValueInheritancePanel.class, "AdsValueInheritancePanel.jbtUpValInh.toolTipText")); // NOI18N
        jbtUpValInh.setFocusable(false);
        jbtUpValInh.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtUpValInh.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtUpValInh.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtUpValInh.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtUpValInh.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtUpValInh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtUpValInhActionPerformed(evt);
            }
        });
        jToolBar1.add(jbtUpValInh);

        jbtDownValInh.setIcon(RadixWareDesignerIcon.ARROW.MOVE_DOWN.getIcon());
        jbtDownValInh.setToolTipText(org.openide.util.NbBundle.getMessage(AdsValueInheritancePanel.class, "AdsValueInheritancePanel.jbtDownValInh.toolTipText")); // NOI18N
        jbtDownValInh.setFocusable(false);
        jbtDownValInh.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtDownValInh.setMaximumSize(new java.awt.Dimension(32, 32));
        jbtDownValInh.setMinimumSize(new java.awt.Dimension(32, 32));
        jbtDownValInh.setPreferredSize(new java.awt.Dimension(32, 32));
        jbtDownValInh.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtDownValInh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtDownValInhActionPerformed(evt);
            }
        });
        jToolBar1.add(jbtDownValInh);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 8, 4);
        add(jToolBar1, gridBagConstraints);

        jListInhVal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jListInhValMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jListInhValMouseReleased(evt);
            }
        });
        jListInhVal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jListInhValKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(jListInhVal);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        add(jScrollPane2, gridBagConstraints);

        cmbValueInitMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbValueInitModeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 8, 4);
        add(cmbValueInitMode, gridBagConstraints);

        lblValueInitMode.setText(org.openide.util.NbBundle.getMessage(AdsValueInheritancePanel.class, "AdsValueInheritancePanel.lblValueInitMode.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 8, 0);
        add(lblValueInitMode, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void jbtAddValInhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtAddValInhActionPerformed
        if (!(prop instanceof AdsTablePropertyDef)
                || //prop instanceof AdsParentRefPropertyDef ||
                !(prop.getOwnerClass() instanceof AdsEntityObjectClassDef)) {
            return;
        }
        AdsEntityObjectClassDef cd = (AdsEntityObjectClassDef) prop.getOwnerClass();
        AdsTypeDeclaration type = prop.getValue().getType();//.getTypeId();

        final ChooseDefinitionCfg2 init = new ChooseDefinitionCfg2(prop, new MyVisitorProvider(cd, false, type, true), null);
        final List<Definition> defList2 = ChooseDefinitionSequence.chooseDefinitionSequence(new ChooseDefinitionCfgs2(init, prop, type));
        final List<Definition> defList = new ArrayList<>(defList2);
        if (defList.isEmpty()) {
            return;
        }

        InheritancePath x = InheritancePath.Factory.newInstance(prop);
        while (defList.size() > 0 && defList.get(defList.size() - 1) == null) {
            defList.remove(defList.size() - 1);
        }
        int i = 0, cnt = defList.size();
        final List<Id> list = new ArrayList<>();
        for (final Definition def : defList) {
            if (++i == cnt) {
                x.setPropertyId(def.getId());
            } else {
                list.add(def.getId());
            }
        }
        x.setReferenceIds(list);
        prop.getValueInheritanceRules().addPath(x);
        fillValueInheritanceRules();

    }//GEN-LAST:event_jbtAddValInhActionPerformed

    private void jbtDelValInhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtDelValInhActionPerformed
        if (!(prop instanceof AdsTablePropertyDef)) {
            return;
        }
        //DefaultListModel model = (DefaultListModel) jListInhVal.getModel();
        int i = jListInhVal.getSelectedIndex();
        InheritancePath p = prop.getValueInheritanceRules().getPathes().get(i);
        prop.getValueInheritanceRules().removePath(p);
        fillValueInheritanceRules();
}//GEN-LAST:event_jbtDelValInhActionPerformed

    private void jbtUpValInhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtUpValInhActionPerformed
        if (!(prop instanceof AdsTablePropertyDef)) {
            return;
        }
        //DefaultListModel model = (DefaultListModel) jListInhVal.getModel();
        int i = jListInhVal.getSelectedIndex();
        InheritancePath p = prop.getValueInheritanceRules().getPathes().get(i);
        prop.getValueInheritanceRules().movePathUp(p);
        fillValueInheritanceRules();
        jListInhVal.setSelectedIndex(i - 1);
        fillValueInheritanceRulesStateDisabled();
}//GEN-LAST:event_jbtUpValInhActionPerformed

    private void jbtDownValInhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtDownValInhActionPerformed
        if (!(prop instanceof AdsTablePropertyDef)) {
            return;
        }
        //DefaultListModel model = (DefaultListModel) jListInhVal.getModel();
        int i = jListInhVal.getSelectedIndex();
        InheritancePath p = prop.getValueInheritanceRules().getPathes().get(i);
        prop.getValueInheritanceRules().movePathDn(p);
        fillValueInheritanceRules();
        jListInhVal.setSelectedIndex(i + 1);
        fillValueInheritanceRulesStateDisabled();
}//GEN-LAST:event_jbtDownValInhActionPerformed

    private void jListInhValMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jListInhValMouseClicked
        fillValueInheritanceRulesStateDisabled();
    }//GEN-LAST:event_jListInhValMouseClicked

    private void jListInhValMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jListInhValMouseReleased
        fillValueInheritanceRulesStateDisabled();
}//GEN-LAST:event_jListInhValMouseReleased

    private void jListInhValKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jListInhValKeyReleased
        fillValueInheritanceRulesStateDisabled();
}//GEN-LAST:event_jListInhValKeyReleased

    private void cmbValueInitModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbValueInitModeActionPerformed
        if (!isMayModify || prop == null || !prop.isInBranch() || isUserProp) {
            return;
        }
        switch (cmbValueInitMode.getSelectedIndex()) {
            case 0:
                prop.getValueInheritanceRules().setInitializationPolicy(EPropInitializationPolicy.DO_NOT_DEFINE);
                break;
            case 1:
                prop.getValueInheritanceRules().setInitializationPolicy(EPropInitializationPolicy.DEFINE_IF_NOT_INHERITED);
                break;
            case 2:
                prop.getValueInheritanceRules().setInitializationPolicy(EPropInitializationPolicy.DEFINE_ALWAYS);
                break;
        }
    }//GEN-LAST:event_cmbValueInitModeActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbValueInitMode;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JList jListInhVal;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JButton jbtAddValInh;
    private javax.swing.JButton jbtDelValInh;
    private javax.swing.JButton jbtDownValInh;
    private javax.swing.JButton jbtUpValInh;
    private javax.swing.JLabel lblValueInitMode;
    private org.radixware.kernel.designer.common.dialogs.components.PropertyValueEditPanel propertyValueEditPanel;
    // End of variables declaration//GEN-END:variables
}
