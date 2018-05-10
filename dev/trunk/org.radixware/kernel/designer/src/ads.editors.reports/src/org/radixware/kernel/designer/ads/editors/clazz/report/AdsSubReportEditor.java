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
package org.radixware.kernel.designer.ads.editors.clazz.report;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.ads.build.BuildOptions;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsParameterPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsSubReport;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsSubReport.Association;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProviders;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.userreport.common.UserExtensionManagerCommon;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.designer.common.annotations.registrators.EditorFactoryRegistration;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.components.ComponentTitledBorder;
import org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.editors.RadixObjectEditor;
import org.radixware.kernel.designer.common.editors.RadixObjectModalEditor;
import org.radixware.kernel.designer.common.general.editors.IEditorFactory;

public class AdsSubReportEditor extends RadixObjectModalEditor<AdsSubReport> {

    private class AssociationsTableModel extends AbstractTableModel {

        private final RadixObjects<Association> associations;
        private final ArrayList<AdsParameterPropertyDef> parameters = new ArrayList<>();
        private final ArrayList<AdsPropertyDef> properties = new ArrayList<>();
        private final Map<Id, AdsPropertyDef> parameterId2property = new HashMap<>();

        public AssociationsTableModel() {

            associations = getSubReport().getAssociations();
            for (AdsSubReport.Association association : associations) {
                final Id parameterId = association.getParameterId();
                if (parameterId != null) {
                    parameterId2property.put(parameterId, association.findProperty());
                }
            }
        }

        public void update() {
            parameters.clear();
            properties.clear();
            final Set<Id> unresolvedParameterIds = new HashSet<>(parameterId2property.keySet());
            final AdsReportClassDef report = getSubReport().findReport();
            if (report != null) {
                parameters.addAll(report.getInputParameters());
                for (AdsParameterPropertyDef parameter : parameters) {
                    if (parameterId2property.containsKey(parameter.getId())) {
                        properties.add(parameterId2property.get(parameter.getId()));
                        unresolvedParameterIds.remove(parameter.getId());
                    } else {
                        properties.add(null);
                    }
                }
            }
            removeUnresolvedParameters(unresolvedParameterIds);
            fireTableDataChanged();
        }

        private void removeUnresolvedParameters(Set<Id> parameterIds) {
            if (!parameterIds.isEmpty()) {
                for (Id id : parameterIds) {
                    parameterId2property.remove(id);
                    for (int i = 0; i < associations.size(); ++i) {
                        if (id.equals(associations.get(i).getParameterId())) {
                            associations.remove(i);
                            break;
                        }
                    }
                }
                DialogUtils.messageInformation("Some non-existent parameters were found and deleted");
            }
        }

        public final AdsSubReport getSubReport() {
            return getRadixObject();
        }

        @Override
        public int getRowCount() {
            return parameters.size();
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public String getColumnName(int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return "Parameter";
                case 1:
                    return "Property";
                default:
                    return "";
            }
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return AdsParameterPropertyDef.class;
                case 1:
                    return AdsPropertyDef.class;
                default:
                    return Object.class;
            }
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 1 && !getRadixObject().isReadOnly();
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return parameters.get(rowIndex);
                case 1:
                    return properties.get(rowIndex);
                default:
                    return null;
            }
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (!isCellEditable(rowIndex, columnIndex)) {
                return;
            }
            if (aValue == null) {
                return;
            }
            AdsPropertyDef newProperty = (AdsPropertyDef) aValue;
            properties.set(rowIndex, newProperty);
            AdsParameterPropertyDef parameter = parameters.get(rowIndex);
            if (parameterId2property.containsKey(parameter.getId())) {
                int idx = -1;
                for (int i = 0; i < associations.size(); ++i) {
                    if (parameter.getId().equals(associations.get(i).getParameterId())) {
                        idx = i;
                        break;
                    }
                }
                assert (idx != -1);
                parameterId2property.put(parameter.getId(), newProperty);
                associations.get(idx).setPropertyId(newProperty != null ? newProperty.getId() : null);
            } else {

                parameterId2property.put(parameter.getId(), newProperty);
                Association association = Association.Factory.newInstance();
                association.setParameterId(parameter.getId());
                association.setPropertyId(newProperty != null ? newProperty.getId() : null);
                associations.add(association);
            }
        }
    }

    private class AssociationsTableCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            isSelected = row == table.getSelectedRow();
            if (value instanceof AdsParameterPropertyDef) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table,
                        ((AdsParameterPropertyDef) value).getName(), isSelected, hasFocus, row, column);
                label.setIcon(((AdsParameterPropertyDef) value).getIcon().getIcon());
                return label;
            } else if (value instanceof AdsPropertyDef) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table,
                        ((AdsPropertyDef) value).getName(), isSelected, hasFocus, row, column);
                label.setIcon(((AdsPropertyDef) value).getIcon().getIcon());
                return label;
            } else {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, "<not specified>", isSelected, hasFocus, row, column);
                label.setIcon(null);
                return label;
            }
        }
    }

    private class AssociationsTableCellEditor extends AbstractCellEditor implements TableCellEditor {

        private final DefinitionLinkEditPanel definitionLinkEditPanel = new DefinitionLinkEditPanel() {

            @Override
            public Container getParent() {
                return associationsTable;
            }
        };
        private int row = -1;
        private int column = -1;
        private boolean isOpening = false;

        public AssociationsTableCellEditor() {
            definitionLinkEditPanel.setComboMode();
            definitionLinkEditPanel.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    if (!isOpening) {
                        Object val = getCellEditorValue();
                        if (val != null) {
                            associationsTable.setValueAt(val, row, column);
                        }
                    }
                }
            });
        }

        List<AdsPropertyDef> getAvailableProperties(AdsParameterPropertyDef parameter) {
            final AdsTypeDeclaration parameterType = parameter.getValue().getType();
            final AdsReportClassDef report = getRadixObject().getOwnerReport();
            final List<AdsPropertyDef> all = report.getProperties().get(EScope.ALL);
            final List<AdsPropertyDef> ret = new ArrayList<>();
            for (AdsPropertyDef property : all) {
                AdsTypeDeclaration propertyType = property.getValue().getType();
                if (AdsTypeDeclaration.isAssignable(parameterType, propertyType, report)) {
                    ret.add(property);
                }
            }
            return ret;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.row = row;
            this.column = column;
            AdsPropertyDef property = (AdsPropertyDef) value;
            AdsParameterPropertyDef parameter = (AdsParameterPropertyDef) table.getValueAt(row, 0);
            definitionLinkEditPanel.setComboBoxValues(getAvailableProperties(parameter), true);
            try {
                isOpening = true;
                definitionLinkEditPanel.open(property, property != null ? property.getId() : null);
            } finally {
                isOpening = false;
            }
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    definitionLinkEditPanel.requestFocusInWindow();
                }
            });
            return definitionLinkEditPanel;
        }

        @Override
        public Object getCellEditorValue() {
            return definitionLinkEditPanel.getDefinition();
        }
    }
    private final AssociationsTableCellRenderer renderer = new AssociationsTableCellRenderer();
    private final AssociationsTableCellEditor editor = new AssociationsTableCellEditor();
    private final AssociationsTableModel tableModel;
    private volatile boolean updating = false;

    /**
     * Creates new form AdsSubReportEditor
     */
    protected AdsSubReportEditor(AdsSubReport subReport) {
        super(subReport);
        initComponents();

        tableModel = new AssociationsTableModel();

        associationsTable.setDefaultRenderer(AdsParameterPropertyDef.class, renderer);
        associationsTable.setDefaultRenderer(AdsPropertyDef.class, renderer);
        associationsTable.setDefaultEditor(AdsParameterPropertyDef.class, editor);
        associationsTable.setDefaultEditor(AdsPropertyDef.class, editor);
        associationsTable.setModel(tableModel);
        associationsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        associationsTable.setCellSelectionEnabled(true);
        associationsTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        associationsTable.setRowHeight(20);
        associationsTable.getTableHeader().setReorderingAllowed(false);
        associationsTable.setAutoCreateColumnsFromModel(false);

        subReportLinkEditPanel.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if (!updating) {
                    getRadixObject().setReportId(subReportLinkEditPanel.getDefinitionId());
                    tableModel.update();
                }
            }
        });
        
        marginPanel.setBorder(BorderFactory.createTitledBorder("Margins")); 
        
        setupInitialValues();
    }

    @Override
    public String getTitle() {
        return getRadixObject().getTypeTitle() + " Editor";
    }

    private void setupInitialValues() {
        updating = true;

        boolean isUserMode = BuildOptions.UserModeHandlerLookup.getUserModeHandler() != null;

        if (isUserMode) {
            visitUserAds();
        }
        ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(
                getRadixObject(), !isUserMode ? AdsVisitorProviders.newReportProvider() : AdsVisitorProviders.newCommonReportAndUserReportProvider());
        subReportLinkEditPanel.open(cfg, getRadixObject().findReport(), getRadixObject().getReportId());
        tableModel.update();
        if (getRadixObject().getOwnerReport().getForm().getMode() == AdsReportForm.Mode.GRAPHICS){
            marginPanel.open(getRadixObject().getMarginMm());
        } else {
            marginPanel.open(getRadixObject().getMarginTxt());
        }
        updateEnableState();
        updating = false;
    }

    private void visitUserAds() {
        UserExtensionManagerCommon.getInstance().getUFRequestExecutor().executeTask(new Runnable() {
            @Override
            public void run() {
                for (Module m : getRadixObject().getLayer().getAds().getModules()) {
                    ((AdsModule) m).getDefinitions().list();
                }
            }
        });
    }

    private void updateEnableState() {
        boolean enabled = !getRadixObject().isReadOnly();
        subReportLinkEditPanel.setEnabled(enabled);
        if (getRadixObject().getOwnerReport().getForm().getMode() == AdsReportForm.Mode.GRAPHICS){
            marginPanel.setEnabled(enabled);
        } else {
            marginPanel.setEnabled(enabled);
        }
    }

    @Override
    public boolean isCancelable() {
        return false;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        subReportLinkEditPanel = new org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        associationsTable = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        marginPanel = new org.radixware.kernel.designer.ads.editors.clazz.report.MarginPanel();

        jLabel1.setText(org.openide.util.NbBundle.getMessage(AdsSubReportEditor.class, "AdsSubReportEditor.jLabel1.text")); // NOI18N

        associationsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(associationsTable);

        jLabel2.setText(org.openide.util.NbBundle.getMessage(AdsSubReportEditor.class, "AdsSubReportEditor.jLabel2.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(subReportLinkEditPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel2))
                .addContainerGap())
            .addComponent(marginPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(subReportLinkEditPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                .addGap(6, 6, 6)
                .addComponent(marginPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    @Override
    protected void apply() {
    }

    @Override
    public void update() {
        setupInitialValues();
    }

    @EditorFactoryRegistration
    public static final class Factory implements IEditorFactory<AdsSubReport> {

        @Override
        public RadixObjectEditor<AdsSubReport> newInstance(AdsSubReport adsSubReport) {
            return new AdsSubReportEditor(adsSubReport);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable associationsTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private org.radixware.kernel.designer.ads.editors.clazz.report.MarginPanel marginPanel;
    private org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel subReportLinkEditPanel;
    // End of variables declaration//GEN-END:variables
}
