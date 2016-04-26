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
 * SuperClassHierarchyPanel.java
 *
 * Created on Jan 15, 2009, 5:29:56 PM
 */
package org.radixware.kernel.designer.ads.editors.clazz.simple;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsEnumClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsInterfaceClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsApplicationClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityBasedClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.platform.IPlatformClassPublisher;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsTransparence;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.utils.PropertyStore;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;
import org.radixware.kernel.designer.common.dialogs.utils.ExtendableTableCellEditor;
import org.radixware.kernel.designer.common.editors.ColumnEditors;
import org.radixware.kernel.designer.common.editors.JTableX;
import org.radixware.kernel.designer.common.general.editors.EditorsManager;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;



/*
 * y AdsEntityBasedClassDef нельзя править extends у AdsApplicationClassDef
 * нельзя чтобы супер класс был null у AdsModelClassDef нельзя редактировать
 * супер класс
 */
public class SuperClassHierarchyPanel extends javax.swing.JPanel {

    private static class Mediator {

        public static PropertyStore getOptions(AdsClassDef cls) {
            final PropertyStore options = new PropertyStore();

            options.set("class", cls);

            options.set("isTransparent", AdsTransparence.isTransparent(cls));
            options.set("isInterface", cls instanceof AdsInterfaceClassDef);
            options.set("isAdsApplicationClassDef", cls instanceof AdsApplicationClassDef);
            options.set("isAdsEntityBasedClassDef", cls instanceof AdsEntityBasedClassDef);
            options.set("isAdsModelClassDef", cls instanceof AdsModelClassDef);
            options.set("isAdsReportClassDef", cls instanceof AdsReportClassDef);
            options.set("isEnumClass", cls instanceof AdsEnumClassDef);
            options.set("isOverwrite", !cls.getHierarchy().findOverwritten().isEmpty());

            options.set("isReadonly", cls.isReadOnly());

            return options;
        }

        private final SuperClassHierarchyPanel panel;

        public Mediator(SuperClassHierarchyPanel panel) {
            this.panel = panel;
        }

        public void update(AdsClassDef cls) {
            final PropertyStore options = getOptions(cls);
            specialize(options);
        }

        private void specialize(PropertyStore options) {

            boolean isReadonly = options.isTrue("isReadonly");

            getPanel().interfacesTable.setEnabled(!isReadonly);
            getPanel().addButton.setEnabled(!isReadonly);
            getPanel().removeButton.setEnabled(!isReadonly && getPanel().interfacesTable.getRowCount() > 0);
            getPanel().extendableTextField.setReadOnly(isReadonly || options.isTrue("isAdsReportClassDef"));

            if (options.isTrue("isInterface")) {
                specializeInterface(options);
                if (options.isTrue("isTransparent")) {
                    specializeTransparent(options);
                }
            } else if (options.condition(true, false).accept("isEnumClass", "isOverwrite")) {
                specializeEnumClass(options);
            } else {
                if (options.isTrue("isTransparent")) {
                    specializeTransparent(options);
                } else {
                    specializeDefault(options);
                }
            }
            
            getPanel().setupTable();
                
        }

        private void specializeInterface(PropertyStore options) {
            getPanel().extendsLabel.setVisible(false);
            getPanel().extendableTextField.setVisible(false);
            getPanel().tableLabel.setText("Extends: ");
        }

        private void specializeEnumClass(PropertyStore options) {
            getPanel().extendsLabel.setVisible(true);
            getPanel().extendableTextField.setVisible(true);
            getPanel().extendableTextField.setEnabled(false);
            getPanel().tableLabel.setText("Implements: ");
        }

        private void specializeDefault(PropertyStore options) {
            getPanel().extendsLabel.setVisible(true);
            getPanel().extendableTextField.setVisible(true);
            getPanel().tableLabel.setText("Implements: ");

            final AdsClassDef cls = options.<AdsClassDef>get("class");
            final AdsClassDef superClassRef = cls.getInheritance().findSuperClass().get();
            final AdsTypeDeclaration superDeclaration = cls.getInheritance().getSuperClassRef();

            final boolean hasSuperClass = superClassRef != null;
            final boolean hasSuperDeclaration = superDeclaration != null;

            if (!hasSuperDeclaration) {
                getPanel().extendableTextField.setValue("");
            } else {
                getPanel().extendableTextField.setValue(superDeclaration.getQualifiedName(cls));
            }

            getPanel().gotoObjectButton.setEnabled(hasSuperClass);
            getPanel().selectInExplorerButton.setEnabled(hasSuperClass);

            getPanel().resetSuperClassButton.setVisible(options.condition(false, true).accept(
                    "isReadonly", "isAdsApplicationClassDef", "isAdsEntityBasedClassDef", "isAdsModelClassDef"));

            getPanel().setupSuperClassButton.setVisible(options.condition(false, true).accept(
                    "isReadonly", "isAdsModelClassDef") &&
                    (!options.isTrue("isAdsEntityBasedClassDef") || options.isTrue("isAdsApplicationClassDef")));
        }

        private void specializeTransparent(PropertyStore options) {
//            specializeEnumClass(options);
//            getPanel().interfacesTable.setEnabled(false);
            AdsClassDef cls = options.<AdsClassDef>get("class");
            AdsClassDef superClassRef = null;
            AdsTypeDeclaration superDeclaration = null;
            
            superClassRef = TransparentClassUtils.findTransparentSuperClass(cls);
            superDeclaration = TransparentClassUtils.getTransparentSuperClassRef(cls);

            final boolean hasSuperClass = superClassRef != null;
            final boolean hasSuperDeclaration = superDeclaration != null;

            if (!hasSuperDeclaration || AdsTypeDeclaration.isObject(superDeclaration)) {
                getPanel().extendableTextField.setValue("");
            } else {
                getPanel().extendableTextField.setValue(superDeclaration.getQualifiedName(cls));
            }
            
            getPanel().extendableTextField.setEditable(false);
            getPanel().gotoObjectButton.setEnabled(hasSuperClass);
            getPanel().selectInExplorerButton.setEnabled(hasSuperClass);
            
            
            getPanel().resetSuperClassButton.setVisible(false);
            getPanel().setupSuperClassButton.setVisible(false);
            getPanel().addButton.setEnabled(false);
            getPanel().removeButton.setEnabled(false);
        }

        public SuperClassHierarchyPanel getPanel() {
            return panel;
        }
    }
    private static final Icon cellEditorButtonsIcon = RadixWareIcons.ARROW.GO_TO_OBJECT.getIcon();
    private boolean isReadonly = false;
    private boolean isTransparent = false;
    private AdsClassDef adsClassDef;
    private JButton setupSuperClassButton, resetSuperClassButton, gotoObjectButton, selectInExplorerButton;
    private ColumnEditors<AdsClassDef> rowEditorModel = new ColumnEditors<>();
    private ColumnEditors<AdsTypeDeclaration> rowTransparentEditorModel = new ColumnEditors<>();

    Mediator mediator = new Mediator(this);

    public SuperClassHierarchyPanel(){
        this(false);
    }
    
    public SuperClassHierarchyPanel(boolean isTransparent) {
        super();

        adsClassDef = null;

        initComponents();
        this.isTransparent = isTransparent;
        if (isTransparent){
            interfacesTable = new TransparentInterfacesTable();
            jScrollPane1.setViewportView(interfacesTable);
            addButton.setEnabled(false);
            removeButton.setEnabled(false);
        }

        setupSuperClassButton = extendableTextField.addButton();
        setupSuperClassButton.setIcon(RadixWareIcons.DIALOG.CHOOSE.getIcon(13, 13));
        setupSuperClassButton.setToolTipText("Choose Super Class");
        setupSuperClassButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                SuperClassHierarchyPanel.this.setupSuperClass();
            }
        });

        resetSuperClassButton = extendableTextField.addButton();
        resetSuperClassButton.setToolTipText("Reset Super Class");
        resetSuperClassButton.setIcon(RadixWareIcons.DELETE.CLEAR.getIcon());
        resetSuperClassButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                SuperClassHierarchyPanel.this.resetSuperClass();
            }
        });

        gotoObjectButton = extendableTextField.addButton();
        gotoObjectButton.setToolTipText("Open");
        gotoObjectButton.setIcon(RadixWareIcons.ARROW.GO_TO_OBJECT.getIcon());
        gotoObjectButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                SuperClassHierarchyPanel.this.gotoObject();
            }
        });

        selectInExplorerButton = extendableTextField.addButton();
        selectInExplorerButton.setToolTipText("Select in Tree");
        selectInExplorerButton.setIcon(RadixWareIcons.TREE.SELECT_IN_TREE.getIcon());
        selectInExplorerButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                final AdsClassDef superClassObj;
                if (AdsTransparence.isTransparent(adsClassDef)){
                    superClassObj = TransparentClassUtils.findTransparentSuperClass(adsClassDef); 
                } else {
                    superClassObj = adsClassDef.getInheritance().findSuperClass().get();
                }
                assert (superClassObj != null);
                NodesManager.selectInProjects(superClassObj);
            }
        });

        gotoObjectButton.setEnabled(false);
        selectInExplorerButton.setEnabled(false);
        removeButton.setEnabled(false);
        extendableTextField.setReadOnly(true);

        addButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                SuperClassHierarchyPanel.this.addInterface();
            }
        });

        removeButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                SuperClassHierarchyPanel.this.removeInterface();
            }
        });
    }

    private void gotoObject() {
        final AdsClassDef superClassObj;
        if (isTransparent){
            superClassObj = TransparentClassUtils.findTransparentSuperClass(adsClassDef); 
        } else {
            superClassObj = adsClassDef.getInheritance().findSuperClass().get();
        }
        assert (superClassObj != null);
        EditorsManager.getDefault().open(superClassObj);
    }

    private void resetSuperClass() {
        if (isTransparent) return;
        
        adsClassDef.getInheritance().setSuperClass(null);
        final AdsTypeDeclaration superDeclaration = adsClassDef.getInheritance().getSuperClassRef();
        if (superDeclaration != null) {
            extendableTextField.setValue(superDeclaration.getQualifiedName(adsClassDef));
        } else {
            extendableTextField.setValue("");
        }
        gotoObjectButton.setEnabled(false);
        selectInExplorerButton.setEnabled(false);
    }

    public void open(AdsClassDef classDef) {
        this.adsClassDef = classDef;
        update();
    }

    public void update() {

        if (adsClassDef != null) {
            mediator.update(adsClassDef);
        }
    }

    public void setReadonly(boolean readonly) {
        this.isReadonly = readonly;
        extendableTextField.setReadOnly(readonly);
        update();
    }

    private ExtendableTableCellEditor createTableCellEditor() {

        final ExtendableTableCellEditor interfacesTableCellEditor = new InterfacesTableCellEditor();
        final JButton gotoInterfaceButton = interfacesTableCellEditor.addButton();
        gotoInterfaceButton.setEnabled(true);
        gotoInterfaceButton.setIcon(cellEditorButtonsIcon);
        gotoInterfaceButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                final int selectedRow = interfacesTable.getSelectedRow();
                assert (selectedRow != -1);
                final int selectedRowInModel = interfacesTable.convertRowIndexToModel(selectedRow);
                final InterfacesTableModel model = (InterfacesTableModel) interfacesTable.getModel();
                if (!isTransparent){
                    EditorsManager.getDefault().open(model.getInterface(selectedRowInModel));
                } else {
                    EditorsManager.getDefault().open(model.getInterface(model.getInterfaceType(selectedRowInModel)));
                }
            }
        });

        return interfacesTableCellEditor;
    }

    private void setupTable() {

        final InterfacesTableModel interfacesTableModel = new InterfacesTableModel(adsClassDef,isTransparent);
        interfacesTable.setModel(interfacesTableModel);
        interfacesTable.getColumnModel().getColumn(0).setCellRenderer(new InterfaceRenderer());

        if (interfacesTable.getRowHeight() < cellEditorButtonsIcon.getIconHeight() + 8) {
            interfacesTable.setRowHeight(cellEditorButtonsIcon.getIconHeight() + 8);
        }
        interfacesTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        interfacesTable.setAutoCreateColumnsFromModel(false);
        interfacesTable.setColumnSelectionAllowed(false);
        interfacesTable.setRowSelectionAllowed(true);

        interfacesTable.setTableHeader(null);       //trick
        jScrollPane1.setColumnHeaderView(null);     //to remove table header

        
        final int rowsCount = interfacesTable.getRowCount();
        // tell the JTableX which RowEditorModel we are using
        if (isTransparent){
            interfacesTable.setRowEditorModel(rowTransparentEditorModel);
            rowTransparentEditorModel.removeEditors();
            if (rowsCount > 0) {
                for (int i = 0; i < rowsCount; ++i) {
                    AdsTypeDeclaration adsTypeDeclaration = interfacesTableModel.getInterfaceType(i);
                    if (adsTypeDeclaration != null) {
                        rowTransparentEditorModel.addEditorForItem(adsTypeDeclaration, createTableCellEditor());
                    }
                }
                interfacesTable.setRowSelectionInterval(rowsCount - 1, rowsCount - 1);
            }
        }else {
            interfacesTable.setRowEditorModel(rowEditorModel);
            rowEditorModel.removeEditors();
            if (rowsCount > 0) {
                for (int i = 0; i < rowsCount; ++i) {
                    AdsClassDef rowInterface = interfacesTableModel.getInterface(i);
                    if (rowInterface != null) {
                        rowEditorModel.addEditorForItem(rowInterface, createTableCellEditor());
                    }
                }
                interfacesTable.setRowSelectionInterval(rowsCount - 1, rowsCount - 1);
            }
        }
    }
    
    private static AdsTypeDeclaration getSuperClassDeclaration(AdsClassDef context) {
        final SuperClassWizard wizard = new SuperClassWizard(context);
        final WizardDescriptor wizardDescriptor = new org.radixware.kernel.designer.common.dialogs.wizards.WizardDescriptor(wizard);
        final Dialog dialog = DialogDisplayer.getDefault().createDialog(wizardDescriptor);
        dialog.setMinimumSize(new Dimension(450, 350));
        dialog.setVisible(true);
        if (wizardDescriptor.getValue().equals(WizardDescriptor.FINISH_OPTION)) {
            return wizard.getResultClassDeclaration();
        }
        return null;
    }

    private void setupSuperClass() {
        final AdsTypeDeclaration superClassDeclaration = getSuperClassDeclaration(adsClassDef);

        if (superClassDeclaration != null) {
            adsClassDef.getInheritance().setSuperClassRef(superClassDeclaration);
            extendableTextField.setValue(superClassDeclaration.getQualifiedName(adsClassDef));
            selectInExplorerButton.setEnabled(true);
            gotoObjectButton.setEnabled(true);
        }
//        else {
//            selectInExplorerButton.setEnabled(false);
//            gotoObjectButton.setEnabled(false);
//        }
    }

    private static AdsTypeDeclaration getInterfaceClassDeclaration(AdsClassDef context) {
        final SuperClassWizard wizard = new InterfaceClassWizard(context);
        final WizardDescriptor wizardDescriptor = new org.radixware.kernel.designer.common.dialogs.wizards.WizardDescriptor(wizard);
        final Dialog dialog = DialogDisplayer.getDefault().createDialog(wizardDescriptor);
        dialog.setMinimumSize(new Dimension(450, 350));
        dialog.setVisible(true);
        if (wizardDescriptor.getValue().equals(WizardDescriptor.FINISH_OPTION)) {
            return wizard.getResultClassDeclaration();
        }
        return null;
    }

    private void addInterface() {
        if (isTransparent) return;
        
        final AdsTypeDeclaration interfaceClassDeclaration = getInterfaceClassDeclaration(adsClassDef);

        if (interfaceClassDeclaration != null) {
            ((InterfacesTableModel) interfacesTable.getModel()).addInterface(interfaceClassDeclaration);
            removeButton.setEnabled(true);

            final int newRowPosition = interfacesTable.getRowCount() - 1;
            rowEditorModel.addEditorForItem(((InterfacesTableModel) interfacesTable.getModel()).getInterface(newRowPosition), createTableCellEditor());
            interfacesTable.setRowSelectionInterval(newRowPosition, newRowPosition);
        }
    }

    private void removeInterface() {
        if (isTransparent) return;
        
        final int tableRowIndex = interfacesTable.getSelectedRow();
        if (tableRowIndex != -1) {
            //close current cell editor
            if (interfacesTable.isEditing()) {
                interfacesTable.getCellEditor().stopCellEditing();
            }
            final int modelRowIndex = interfacesTable.convertRowIndexToModel(tableRowIndex);
            AdsClassDef rowInterface = ((InterfacesTableModel) this.interfacesTable.getModel()).getInterface(modelRowIndex);
            if (rowInterface != null) {
                rowEditorModel.removeEditorForItem(rowInterface);
            }

            ((InterfacesTableModel) interfacesTable.getModel()).removeInterface(modelRowIndex);

            final int rowsCount = interfacesTable.getRowCount();
            if (rowsCount == 0) {
                removeButton.setEnabled(false);
            } else {
                int newRowPosition = rowsCount - 1;
                interfacesTable.setRowSelectionInterval(newRowPosition, newRowPosition);
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tableLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        interfacesTable =  new InterfacesTableX(); /*TunedTable()*/;
        extendableTextField = new org.radixware.kernel.common.components.ExtendableTextField();
        extendsLabel = new javax.swing.JLabel();
        addButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(SuperClassHierarchyPanel.class, "SuperClassHierarchyPanel.border.title"))); // NOI18N

        tableLabel.setText(org.openide.util.NbBundle.getMessage(SuperClassHierarchyPanel.class, "SuperClassHierarchyPanel.tableLabel.text_1")); // NOI18N

        jScrollPane1.setAlignmentX(0.0F);

        jScrollPane1.setViewportView(interfacesTable);

        extendableTextField.setEditable(false);

        extendsLabel.setText(org.openide.util.NbBundle.getMessage(SuperClassHierarchyPanel.class, "SuperClassHierarchyPanel.extendsLabel.text")); // NOI18N

        addButton.setIcon(RadixWareDesignerIcon.CREATE.ADD.getIcon(13, 13));
        addButton.setText(org.openide.util.NbBundle.getMessage(SuperClassHierarchyPanel.class, "SuperClassHierarchyPanel.addButton.text")); // NOI18N
        addButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        removeButton.setIcon(RadixWareDesignerIcon.DELETE.DELETE.getIcon(13, 13));
        removeButton.setText(org.openide.util.NbBundle.getMessage(SuperClassHierarchyPanel.class, "SuperClassHierarchyPanel.removeButton.text")); // NOI18N
        removeButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tableLabel)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(extendsLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(extendableTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 505, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 498, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(addButton)
                            .addComponent(removeButton))))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {addButton, removeButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(extendableTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(extendsLabel))
                .addGap(17, 17, 17)
                .addComponent(tableLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(addButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeButton))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(SuperClassHierarchyPanel.class, "SuperClassHierarchyPanel.AccessibleContext.accessibleName")); // NOI18N
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private org.radixware.kernel.common.components.ExtendableTextField extendableTextField;
    private javax.swing.JLabel extendsLabel;
    /*
    private javax.swing.JTable interfacesTable;
    */
    //private TunedTable interfacesTable;
    private JTableX interfacesTable;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton removeButton;
    private javax.swing.JLabel tableLabel;
    // End of variables declaration//GEN-END:variables

    private static class InterfacesTableCellEditor extends ExtendableTableCellEditor {

        public InterfacesTableCellEditor() {
            super(true);
            setReadonly(true);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (value != null) {
                if (value instanceof AdsClassType.InterfaceType) {
                    extendableTextField.setValue(((AdsClassType.InterfaceType) value).getName());
                } else {
                    extendableTextField.setValue(value.toString());
                }
            }
            return extendableTextField;
        }
    }

    private static class InterfaceRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (value != null) {
                if (value instanceof AdsClassType.InterfaceType) {
                    String strValue = ((AdsClassType.InterfaceType) value).getName();
                    setText(strValue);
                } else {
                    setText(value.toString());
                }
            }
            return this;
        }
    }
    
    private class TransparentInterfacesTable extends JTableX<AdsTypeDeclaration>{

        @Override
        protected TableCellEditor getColumnCellEditorByRow(int row, int col) {
            if (col == InterfacesTableModel.INTERFACE_СOLUMN_INDEX && columnEditors != null) {

                    final int modelIndex = this.convertRowIndexToModel(row);
                    final AdsTypeDeclaration item = ((InterfacesTableModel) this.getModel()).getInterfaceType(modelIndex);
                    if (item != null){
                        return columnEditors.getEditorForItem(item);
                    }

            }
            return null;
        }
    }
}
