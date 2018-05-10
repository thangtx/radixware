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

package org.radixware.kernel.designer.ads.method.profile;

import java.util.*;
import javax.swing.JTable;
import javax.swing.event.*;
import org.openide.util.ChangeSupport;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodParameters;
import org.radixware.kernel.common.defs.ads.clazz.members.MethodParameter;
import org.radixware.kernel.common.defs.ads.type.AdsTransparence;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EMethodNature;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;
import org.radixware.kernel.designer.common.dialogs.components.AdvanceTable;
import org.radixware.kernel.designer.common.dialogs.components.AdvanceTableModel;
import org.radixware.kernel.designer.common.dialogs.components.TypeEditorComponent.TypeContext;
import org.radixware.kernel.designer.common.dialogs.components.description.DescriptionModel;
import org.radixware.kernel.designer.common.dialogs.components.values.NameEditorComponent.NamedContext;


public final class MethodParametersPanel extends javax.swing.JPanel {

    static final class ParameterModel {

        private final ChangeSupport changeSupport = new ChangeSupport(this);
        private boolean variable;
        private AdsTypeDeclaration type;
        private String name;
        private final MethodParameter parameter;
        private final DescriptionModel descriptionModel;

        public ParameterModel(MethodParameter parameter) {
            this.variable = parameter.isVariable();
            if (variable && parameter.getType().isArray()) {
                this.type = parameter.getType().toArrayType(parameter.getType().getArrayDimensionCount() - 1);
            } else {
                this.type = parameter.getType();
            }
            this.name = parameter.getName();
            this.parameter = parameter;

            descriptionModel = DescriptionModel.Factory.newInstance(parameter);
        }

        public void sync(boolean syncDdescription) {

            if (!Objects.equals(name, parameter.getName())) {
                parameter.setName(name);
            }

            final AdsTypeDeclaration parametrtype = isVariable() ? type.toArrayType(type.getArrayDimensionCount() + 1) : type;
            if (!Objects.equals(parametrtype, parameter.getType()) || isVariable() != parameter.isVariable()) {
                parameter.setType(parametrtype);
            }

            if (parameter.isVariable() != variable) {
                parameter.setVariable(variable);
            }
            if (syncDdescription) {
                descriptionModel.applyFor(parameter);
            }
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            if (!Objects.equals(name, this.name)) {
                this.name = name;
                fireChange();
            }
        }

        public AdsTypeDeclaration getType() {
            return type;
        }

        public void setType(AdsTypeDeclaration type) {
            if (!Objects.equals(type, this.type)) {
                this.type = type;
                fireChange();
            }
        }

        public boolean isVariable() {
            return variable;
        }

        public void setVariable(boolean variable) {
            if (this.variable != variable) {
                this.variable = variable;
                fireChange();
            }
        }

        public MethodParameter getParameter() {
            return parameter;
        }

        private void fireChange() {
            changeSupport.fireChange();
        }

        public void addChangeListener(ChangeListener listener) {
            changeSupport.addChangeListener(listener);
        }

        public DescriptionModel getDescriptionModel() {
            return descriptionModel;
        }
    }

    private static class ParametersTable extends AdvanceTable<ParametersTableModel> {

        public ParametersTable() {
            super();

            setDefaultRenderer(AdsTypeDeclaration.class, new DefaultColorCellRender() {
                @Override
                public void setValue(Object value) {
                    setText(((AdsTypeDeclaration) value).getName(getModel().getMethod()));
                    setIcon(((AdsTypeDeclaration) value).getIcon().getIcon());
                }
            });

            setDefaultEditor(AdsTypeDeclaration.class, new TypeEditor() {
                @Override
                protected TypeContext getTypeContext(JTable table, Object value, int row, int column) {
                    final ParametersTableModel model = ((AdvanceTable<ParametersTableModel>) table).getModel();

                    final ParameterModel parametr = model.getRowSource(row);
                    return new TypeContext(parametr.getParameter(), model.getMethod()) {
                        @Override
                        public void commit(AdsTypeDeclaration type) {
                            parametr.setType(type);
                        }
                    };
                }

                @Override
                protected AdsTypeDeclaration getCurrentValue(JTable table, Object value, int row, int column) {
                    return ((AdvanceTable<ParametersTableModel>) table).getModel().getRowSource(row).getType();
                }
            });

            setDefaultEditor(DescriptionModel.class, new DescriptionCellEditor());

            setDefaultEditor(String.class, new StringEditor());

            setRowHeight(26);
        }

        @Override
        protected ParametersTableModel getDefauldModel() {
            return new ParametersTableModel(null);
        }

        @Override
        protected void afterSetModel() {
            getColumnModel().getColumn(0).setCellEditor(new NameEditor() {
                @Override
                protected NamedContext getNamedContext(JTable table, Object value, int row, int column) {
                    final ParameterModel model = ((ParametersTableModel) table.getModel()).getRowSource(table.convertRowIndexToModel(row));

                    return new NamedContext() {
                        @Override
                        public String getName() {
                            return model.getName();
                        }

                        @Override
                        public void setName(String name) {
                            model.setName(name);
                        }

                        @Override
                        public boolean isValidName(String name) {
                            return name != null && !name.isEmpty();
                        }
                    };
                }
            });

            getColumnModel().getColumn(3).setResizable(false);
            getColumnModel().getColumn(3).setPreferredWidth(60);
            getColumnModel().getColumn(3).setMaxWidth(60);
        }
    }

    private static class ParametersTableModel extends AdvanceTableModel<List<ParameterModel>, ParameterModel> {

        private static List<ParameterModel> deepCopy(AdsMethodDef method) {
            final List<ParameterModel> copy = new ArrayList<>();
            if (method != null) {
                for (final MethodParameter parametr : method.getProfile().getParametersList().list()) {
                    copy.add(new ParameterModel(parametr));
                }
            }
            return copy;
        }
        private final List<IColumnHandler> columnHandlers = new ArrayList<>();
        private final AdsMethodDef method;

        private ParametersTableModel(AdsMethodDef method) {
            super(deepCopy(method));

            final boolean isTransparance = AdsTransparence.isTransparent(method);

            this.method = method;

            columnHandlers.add(new ColumnHandlerAdapter("Name") {
                @Override
                public Object getCellValue(int row, int col, Object param) {
                    return getRowSource(row).getName();
                }

                @Override
                public Class<?> getColumnClass(int col) {
                    return String.class;
                }

                @Override
                public boolean isCellEditable(int row, int col) {
                    return true;
                }
            });

            columnHandlers.add(new ColumnHandlerAdapter("Type") {
                @Override
                public Object getCellValue(int row, int col, Object param) {
                    return getRowSource(row).getType();
                }

                @Override
                public Class<?> getColumnClass(int col) {
                    return AdsTypeDeclaration.class;
                }

                @Override
                public boolean isCellEditable(int row, int col) {
                    return !isTransparance;
                }
            });

            columnHandlers.add(new ColumnHandlerAdapter("Description") {
                @Override
                public Object getCellValue(int row, int col, Object param) {
                    return getRowSource(row).getDescriptionModel();
                }

                @Override
                public Class<?> getColumnClass(int col) {
                    return DescriptionModel.class;
                }

                @Override
                public boolean isCellEditable(int row, int col) {
                    return true;
                }

                @Override
                public void setCellValue(Object value, int row, int col, Object params) {
                }
            });

            columnHandlers.add(new ColumnHandlerAdapter("Varargs") {
                @Override
                public Object getCellValue(int row, int col, Object param) {
                    return getRowSource(row).isVariable();
                }

                @Override
                public Class<?> getColumnClass(int col) {
                    return Boolean.class;
                }

                @Override
                public boolean isCellEditable(int row, int col) {
                    return !isTransparance && (row == getRowCount() - 1 || getRowSource(row).isVariable());
                }

                @Override
                public void setCellValue(Object value, int row, int col, Object params) {
                    getRowSource(row).setVariable((Boolean) value);
                }
            });
        }

        @Override
        public ParameterModel getRowSource(int row) {
            return getModelSource().get(row);
        }

        @Override
        protected IColumnHandler getColumnHandler(Object key) {
            return columnHandlers.get(((Integer) key).intValue());
        }

        @Override
        public int getRowCount() {
            return getModelSource().size();
        }

        @Override
        public int getColumnCount() {
            return 4;
        }

        @Override
        public void addRow(ParameterModel row) {
            getModelSource().add(row);
            fireTableRowsInserted(getRowCount(), getRowCount());
        }

        @Override
        public void removeRow(int row) {
            ParameterModel parameterModel = getModelSource().get(row);
            //parameterModel.getParameter().removeDescriptionString();

            getModelSource().remove(parameterModel);
            fireTableRowsDeleted(row, row);
        }

        @Override
        public void moveRow(int row, int sh) {
            final int newIndex = row + sh;
            if (validRowIndex(newIndex)) {
                final ParameterModel current = getModelSource().get(row);
                getModelSource().set(row, getModelSource().get(newIndex));
                getModelSource().set(newIndex, current);

                fireTableDataChanged();
            }
        }

        void sync(AdsMethodParameters parameters, boolean description) {
            // REWORK 
            parameters.clear();
            for (final ParameterModel model : getModelSource()) {              
                model.getParameter().delete();
                parameters.add(model.getParameter());
                // synchronized after adding, because parametr must have container AdsDefinition
                model.sync(description);                
            }   
        }

        AdsMethodDef getMethod() {
            return method;
        }

        void addRow(ParameterModel row, int index) {
            getModelSource().add(index, row);
            fireTableDataChanged();
        }
    }
    private AdsMethodDef method;
    private final ChangeSupport changeSupport = new ChangeSupport(this);
    private boolean change = false;

    public MethodParametersPanel() {
        initComponents();

        parametrs.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                updateButtonState();
            }
        });
    }

    public void open(AdsMethodDef method) {
        this.method = method;

        update();
    }

    public void apply() {
        apply(true);
    }

    public void apply(boolean syncDescription) {
        if (change) {
            getParametersModel().sync(method.getProfile().getParametersList(), syncDescription);
        }
    }

    public void update() {
        final ParametersTableModel parametersModel = new ParametersTableModel(method);

        parametersModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                fireChange();
            }
        });

        parametrs.setModel(parametersModel);
        parametrs.setEnabled(!isReadonly());

        updateButtonState();
    }

    private boolean isReadonly() {
        return method.isReadOnly() || method.getNature() == EMethodNature.RPC;
    }

    public boolean isComplete() {

        // check duplicates
        final Set<String> names = new HashSet<>();
        for (ParameterModel methodParameter : getParametersModel().getModelSource()) {
            if (names.contains(methodParameter.getName())) {
                return false;
            }
            names.add(methodParameter.getName());
        }
        return true;
    }

    public void addChangeListener(ChangeListener aThis) {
        changeSupport.addChangeListener(aThis);
    }

    public List<MethodParameter> getParameters() {
        apply(false);

        final List<ParameterModel> modelSource = getParametersModel().getModelSource();
        final List<MethodParameter> parameters = new ArrayList<>(modelSource.size());

        for (final ParameterModel model : modelSource) {
            parameters.add(model.getParameter());
        }

        return parameters;
    }

    Map<Object, DescriptionModel> getDescriptionMap() {
        Map<Object, DescriptionModel> descriptionMap = new HashMap<>();

        for (final ParameterModel parameterModel : getParametersModel().getModelSource()) {
            descriptionMap.put(ChangeProfilePanel.METHOD_PARAMETR_DESCRIPTION_KEY + parameterModel.getName(), parameterModel.getDescriptionModel());
        }

        return descriptionMap;
    }

    private ParametersTableModel getParametersModel() {
        return (ParametersTableModel) parametrs.getModel();
    }

    private void fireChange() {
        change = true;
        changeSupport.fireChange();
        updateButtonState();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        cmdAdd = new javax.swing.JButton();
        cmdRemove = new javax.swing.JButton();
        cmdUp = new javax.swing.JButton();
        cmdDown = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        parametrs = new ParametersTable();

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 6, 0, 0));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        cmdAdd.setIcon(RadixWareDesignerIcon.CREATE.ADD.getIcon(13, 13));
        cmdAdd.setText(org.openide.util.NbBundle.getMessage(MethodParametersPanel.class, "MethodParametersPanel.cmdAdd.text")); // NOI18N
        cmdAdd.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cmdAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAddActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        jPanel1.add(cmdAdd, gridBagConstraints);

        cmdRemove.setIcon(RadixWareDesignerIcon.DELETE.DELETE.getIcon(13, 13));
        cmdRemove.setText(org.openide.util.NbBundle.getMessage(MethodParametersPanel.class, "MethodParametersPanel.cmdRemove.text")); // NOI18N
        cmdRemove.setEnabled(false);
        cmdRemove.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cmdRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRemoveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        jPanel1.add(cmdRemove, gridBagConstraints);

        cmdUp.setIcon(RadixWareDesignerIcon.ARROW.MOVE_UP.getIcon(13, 13));
        cmdUp.setText(org.openide.util.NbBundle.getMessage(MethodParametersPanel.class, "MethodParametersPanel.cmdUp.text")); // NOI18N
        cmdUp.setEnabled(false);
        cmdUp.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cmdUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdUpActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        jPanel1.add(cmdUp, gridBagConstraints);

        cmdDown.setIcon(RadixWareDesignerIcon.ARROW.MOVE_DOWN.getIcon(13, 13));
        cmdDown.setText(org.openide.util.NbBundle.getMessage(MethodParametersPanel.class, "MethodParametersPanel.cmdDown.text")); // NOI18N
        cmdDown.setEnabled(false);
        cmdDown.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cmdDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdDownActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(cmdDown, gridBagConstraints);

        parametrs.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(parametrs);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cmdAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAddActionPerformed
        addActionPerformed();
    }//GEN-LAST:event_cmdAddActionPerformed

    private void cmdRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRemoveActionPerformed
        removeActionPerformed();
    }//GEN-LAST:event_cmdRemoveActionPerformed

    private void cmdUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdUpActionPerformed
        upActionPerformed();
    }//GEN-LAST:event_cmdUpActionPerformed

    private void cmdDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdDownActionPerformed
        downActionPerformed();
    }//GEN-LAST:event_cmdDownActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdAdd;
    private javax.swing.JButton cmdDown;
    private javax.swing.JButton cmdRemove;
    private javax.swing.JButton cmdUp;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable parametrs;
    // End of variables declaration//GEN-END:variables

    private void addActionPerformed() {
        final ParameterModel parameter = new NewParameterDysplayer().createParameter(method);
        if (parameter != null) {
            getParametersModel().addRow(parameter);
        }
    }

    private void removeActionPerformed() {
        if (parametrs.getSelectedRowCount() > 0) {
            final int[] selectedRows = parametrs.getSelectedRows();
            Arrays.sort(selectedRows);

            for (int i = selectedRows.length - 1; i >= 0; --i) {
                getParametersModel().removeRow(selectedRows[i]);
            }
        }
    }

    private void upActionPerformed() {
        if (parametrs.getSelectedRowCount() > 0) {
            final int row = parametrs.getSelectedRow();
            getParametersModel().moveRowUp(row);
            if (row > 0) {
                parametrs.getSelectionModel().setSelectionInterval(row - 1, row - 1);
            }
        }
    }

    private void downActionPerformed() {
        if (parametrs.getSelectedRowCount() > 0) {
            final int row = parametrs.getSelectedRow();
            getParametersModel().moveRowDown(row);

            if (row < parametrs.getRowCount() - 1) {
                parametrs.getSelectionModel().setSelectionInterval(row + 1, row + 1);
            }
        }
    }

    private void updateButtonState() {

        if (method.isReadOnly() || AdsTransparence.isTransparent(method)) {
            cmdAdd.setEnabled(false);
            cmdRemove.setEnabled(false);
            cmdDown.setEnabled(false);
            cmdUp.setEnabled(false);
            return;
        }

        final int rowCount = getParametersModel().getRowCount();
        final int selectedRowCount = parametrs.getSelectedRowCount();

        cmdAdd.setEnabled(true);
        if (rowCount == 0 || selectedRowCount == 0) {
            cmdRemove.setEnabled(false);
            cmdDown.setEnabled(false);
            cmdUp.setEnabled(false);
        } else {
            cmdRemove.setEnabled(true);
            cmdUp.setEnabled(parametrs.getSelectedRow() > 0 && selectedRowCount == 1);
            cmdDown.setEnabled(parametrs.getSelectedRow() < rowCount - 1 && selectedRowCount == 1);
        }
    }
}
