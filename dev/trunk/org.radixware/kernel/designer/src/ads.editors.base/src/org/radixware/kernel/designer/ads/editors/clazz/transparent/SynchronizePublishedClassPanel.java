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
 * SynchronizePublishedClassDialog.java
 *
 * Created on 06.07.2009, 10:55:43
 */
package org.radixware.kernel.designer.ads.editors.clazz.transparent;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.UIResource;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import org.openide.util.ChangeSupport;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.platform.RadixPlatformClass;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.dialogs.utils.PublicationUtils;


final class SynchronizePublishedClassPanel extends JPanel implements ChangeListener {

    private AdsClassDef transparentClass;
    private RadixPlatformClass platformClass;
    private PublishTableModel model;
    private final ChangeSupport changeSupport = new ChangeSupport(this);

    public SynchronizePublishedClassPanel() {
        initComponents();

        btnGeneric.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                AdsTypeDeclaration.TypeArguments publishedArgs = transparentClass.getTypeArguments();
                AdsTypeDeclaration.TypeArguments platformArgs = platformClass.getDeclaration().getGenericArguments();
                if (publishedArgs.equalsTo(transparentClass, platformArgs)) {
                    DialogUtils.messageInformation(NbBundle.getMessage(SynchronizePublishedClassPanel.class, "Dialog-Generic-Correct"));
                    btnGeneric.setEnabled(false);
                } else {
                    if (DialogUtils.messageConfirmation(NbBundle.getMessage(SynchronizePublishedClassPanel.class, "Dialog-Generic-Incorrect"))) {
                        publishedArgs.getArgumentList().clear();
                        for (AdsTypeDeclaration.TypeArgument a : platformArgs.getArgumentList()) {
                            publishedArgs.add(a);
                        }
                        btnGeneric.setEnabled(false);
                    }
                }
            }
        });
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        changeSupport.fireChange();
    }

    public void addChangeListener(ChangeListener l) {
        changeSupport.addChangeListener(l);
    }

    public void removeChangeListener(ChangeListener l) {
        changeSupport.removeChangeListener(l);
    }

    public void apply() {
        model.apply(transparentClass);
    }

//    AdsClassDef getTransparentClass() {
//        return transparentClass;
//    }

    public void open(RadixPlatformClass platformClass, AdsClassDef transparentClass) {
        this.transparentClass = transparentClass;
        this.platformClass = platformClass;

        if (platformClass != null && transparentClass != null) {
            btnGeneric.setEnabled(platformClass.getDeclaration().isGeneric());

            final Set<ClassMemberPresenter> content = new HashSet<>();

            collectMethods(transparentClass, platformClass, content);

            collectFields(transparentClass, platformClass, content);

            model = new PublishTableModel(content);
            treeTable.setModel(model);
            setupColumns();
        }
    }

    private boolean acceptAccess(RadixPlatformClass.ClassMember member) {
        return !member.getName().equals("<clinit>") && member.getAccess() != EAccess.PRIVATE && member.getAccess() != EAccess.DEFAULT;
    }

    private void collectMethods(AdsClassDef transparentClass, RadixPlatformClass platformClass, Set<ClassMemberPresenter> content) {

        final List<AdsMethodDef> transparentMethods = transparentClass.getMethods().get(EScope.LOCAL, new PublicationUtils.TransparencyFilter<AdsMethodDef>());
        final Set<RadixPlatformClass.Method> publishedMethods = new HashSet<>();
        for (final AdsMethodDef method : transparentMethods) {

            final RadixPlatformClass.Method platformMethod = platformClass.findMethodByProfile(method);
            if (platformMethod != null) {
                final ClassMemberPresenter presentation = new MethodPresenter(method, platformMethod, transparentClass, true);
                presentation.addChangeListener(this);

                content.add(presentation);
                publishedMethods.add(platformMethod);
            } else {
                final ClassMemberPresenter incorrectMethod = new MethodPresenter(method);
                incorrectMethod.addChangeListener(this);
                content.add(incorrectMethod);
            }
        }

        for (RadixPlatformClass.Method platformMethod : platformClass.getMethods()) {
            if (!publishedMethods.contains(platformMethod) && acceptAccess(platformMethod)) {

                final ClassMemberPresenter presenter = new MethodPresenter(platformMethod, transparentClass, false);
                content.add(presenter);
                presenter.addChangeListener(this);
            }
        }
    }

    private void collectFields(final AdsClassDef transparentClass, final RadixPlatformClass platformClass,
        Set<ClassMemberPresenter> content) {

        List<AdsPropertyDef> transparentFields = transparentClass.getProperties().get(EScope.LOCAL, new PublicationUtils.TransparencyFilter<AdsPropertyDef>());
        Set<RadixPlatformClass.Field> publishedFields = new HashSet<>();
        for (AdsPropertyDef prop : transparentFields) {

            RadixPlatformClass.Field field = platformClass.findFieldByProfile(prop);
            if (field != null) {
                ClassMemberPresenter presentation = new FieldPresenter(prop, field, transparentClass, true);
                content.add(presentation);
                publishedFields.add(field);
                presentation.addChangeListener(this);
            } else {
                ClassMemberPresenter fakeMethod = new FieldPresenter(prop);
                fakeMethod.addChangeListener(this);
                content.add(fakeMethod);
            }
        }

        for (RadixPlatformClass.Field field : platformClass.getFields()) {
            if (!publishedFields.contains(field) && acceptAccess(field) && field.isStatic()) {

                ClassMemberPresenter mp = new FieldPresenter(field, transparentClass, false);
                content.add(mp);
                mp.addChangeListener(this);
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        btnGeneric = new javax.swing.JButton();
        tableScrollPane = new javax.swing.JScrollPane();
        treeTable = new javax.swing.JTable();

        setLayout(new java.awt.GridBagLayout());

        btnGeneric.setText(org.openide.util.NbBundle.getMessage(SynchronizePublishedClassPanel.class, "Dialog-GenericButton")); // NOI18N
        btnGeneric.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 10, 0);
        add(btnGeneric, gridBagConstraints);

        treeTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tableScrollPane.setViewportView(treeTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(tableScrollPane, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGeneric;
    private javax.swing.JScrollPane tableScrollPane;
    private javax.swing.JTable treeTable;
    // End of variables declaration//GEN-END:variables

    private void setupColumns() {
        treeTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        treeTable.getColumnModel().getColumn(0).setCellRenderer(new PresenterCellRenderer());
        treeTable.setRowHeight(25);

        TableColumn pCol = treeTable.getColumnModel().getColumn(1);
        pCol.setPreferredWidth(65);
        pCol.setMaxWidth(65);
        pCol.setResizable(false);
        pCol.setCellRenderer(new BoolRenderer());
        TableColumn dCol = treeTable.getColumnModel().getColumn(2);
        dCol.setPreferredWidth(65);
        dCol.setMaxWidth(65);
        dCol.setResizable(false);
        dCol.setCellRenderer(new BoolRenderer());
        TableColumn cCol = treeTable.getColumnModel().getColumn(3);
        cCol.setPreferredWidth(85);
        cCol.setCellRenderer(new Link.CorrectLinkRenderer());
        cCol.setCellEditor(new Link.CorrectLinkEditor());

    }

    private static class BoolRenderer extends JCheckBox implements TableCellRenderer, UIResource {

        public BoolRenderer() {
            super();
            setHorizontalAlignment(JLabel.CENTER);
            setBorderPainted(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

            final PublishTableModel model = (PublishTableModel) table.getModel();

            setOpaque(true);
            setEnabled(model.isCellEditable(row, column));
            setSelected(value instanceof Boolean ? (Boolean) value : false);

            return this;
        }
    }
}