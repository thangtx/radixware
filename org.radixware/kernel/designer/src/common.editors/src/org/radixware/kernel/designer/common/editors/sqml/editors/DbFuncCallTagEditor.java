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

package org.radixware.kernel.designer.common.editors.sqml.editors;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.defs.dds.DdsFunctionDef;
import org.radixware.kernel.common.defs.dds.DdsParameterDef;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.sqml.providers.SqmlVisitorProviderFactory;
import org.radixware.kernel.common.sqml.tags.DbFuncCallTag;
import org.radixware.kernel.common.sqml.tags.DbFuncCallTag.ParamValue;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.components.TunedTable;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditor;


public class DbFuncCallTagEditor extends SqmlTagEditor<DbFuncCallTag> {

    DefaultTableModel model;
    TunedTable paramsTable;

    public DbFuncCallTagEditor() {
        initComponents();

        paramsTable = new TunedTable();
        model = new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Name", "Value"
                }) {

            boolean[] canEdit = new boolean[]{
                false, true
            };

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };
        paramsTable.setModel(model);
        paramsTable.setCellSelectionEnabled(true);
        paramsTable.setFillsViewportHeight(true);
        paramsTable.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {

            @Override
            protected void setValue(Object value) {
                DdsParameterDef param = (DdsParameterDef) value;
                super.setIcon(RadixObjectIcon.getForValType(param.getValType()).getIcon());
                super.setValue(param.getName());
            }
        });
        jScrollPane1.setViewportView(paramsTable);

        funcPanel.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                rebuildParams();
                if (funcPanel.getDefinition() != null) {
                    cbDefineParams.setEnabled(true);
                } else {
                    cbDefineParams.setSelected(false);
                    cbDefineParams.setEnabled(false);
                }
                updateOkState();
            }
        });
        cbDefineParams.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if (cbDefineParams.isSelected()) {
                    paramsTable.setEnabled(true);
                } else {
                    paramsTable.removeEditor();
                    paramsTable.clearSelection();
                    paramsTable.setEnabled(false);
                }
            }
        });
        funcPanel.setClearable(false);
    }

    private void rebuildParams() {
        if (paramsTable.getCellEditor() != null) {
            paramsTable.getCellEditor().stopCellEditing();
        }
        model.setRowCount(0);
        DdsFunctionDef function = (DdsFunctionDef) funcPanel.getDefinition();
        if (function != null) {
            for (DdsParameterDef param : function.getParameters()) {
                String val = findParamValue(param);
                Object[] data = new Object[]{
                    param,
                    val == null ? param.getDefaultVal() : val,};
                model.addRow(data);
            }
        }
    }

    private String findParamValue(DdsParameterDef param) {
        for (ParamValue value : getTag().getParamValues()) {
            if (value.getParamId().equals(param.getId())) {
                return value.getValue();
            }
        }
        return null;
    }

    public String[] getParamValues() {
        if (funcPanel.getDefinition() != null && cbDefineParams.isSelected()) {
            String[] ret = new String[paramsTable.getRowCount()];
            for (int i = 0; i < paramsTable.getRowCount(); i++) {
                ret[i] = (String) paramsTable.getModel().getValueAt(i, 1);
            }
            return ret;

        }
        return null;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        funcPanel = new org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel();
        cbDefineParams = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();

        setMinimumSize(new java.awt.Dimension(520, 350));
        setPreferredSize(new java.awt.Dimension(520, 350));

        cbDefineParams.setText(org.openide.util.NbBundle.getMessage(DbFuncCallTagEditor.class, "DbFuncCallTagEditor.cbDefineParams.text")); // NOI18N
        cbDefineParams.setEnabled(false);

        jLabel1.setText(org.openide.util.NbBundle.getMessage(DbFuncCallTagEditor.class, "DbFuncCallTagEditPanel.lbFunc.text")); // NOI18N
        jLabel1.setName("lbFunc"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 496, Short.MAX_VALUE)
                    .addComponent(funcPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 496, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbDefineParams, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(funcPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbDefineParams)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox cbDefineParams;
    private org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel funcPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

    @Override
    protected String getTitleKey() {
        return "db-func-call-tag-edit-panel-title";
    }

    @Override
    protected boolean tagDefined() {
        return (funcPanel.getDefinition() != null);
    }

    @Override
    protected void applyChanges() {
        if (paramsTable.getCellEditor() != null) {
            paramsTable.getCellEditor().stopCellEditing();
        }
        DbFuncCallTag tag = getTag();
        tag.setFunctionId(funcPanel.getDefinitionId());
        String[] params = getParamValues();
        if (params != null) {
            DdsFunctionDef function = (DdsFunctionDef) funcPanel.getDefinition();
            tag.getParamValues().clear();
            DbFuncCallTag.ParamValue val;
            for (int i = 0; i < params.length; i++) {
                val = DbFuncCallTag.ParamValue.Factory.newInstance();
                val.setValue(params[i]);
                val.setParamId(function.getParameters().get(i).getId());
                tag.getParamValues().add(val);
            }
        }
        tag.setParamsDefined(cbDefineParams.isSelected());
    }

    @Override
    protected void afterOpen() {
        Sqml sqml = getObject().getOwnerSqml();
        if (sqml == null) {
            sqml = (Sqml) getOpenInfo().getLookup().lookup(ScmlEditor.class).getSource();
        }
        DbFuncCallTag tag = getObject();
        ChooseDefinitionCfg cfg;
        cfg = ChooseDefinitionCfg.Factory.newInstance(
                sqml,
                SqmlVisitorProviderFactory.newDbFuncCallTagProvider(sqml));
        cfg.setStepCount(2);

        DdsFunctionDef function = tag.findFunction();

        if (function == null) {
            function = getOpenInfo().getLookup().lookup(DdsFunctionDef.class);
        }
        if (function == null) {
            function = sqml.getEnvironment().findFunctionById(tag.getFunctionId());
        }

        funcPanel.open(cfg, function, tag.getFunctionId());
        cbDefineParams.setEnabled(true);
        if (tag.isParamsDefined()) {
            cbDefineParams.setSelected(true);
        } else {
            cbDefineParams.setSelected(false);
        }
        paramsTable.setEnabled(cbDefineParams.isSelected());
        rebuildParams();
        updateOkState();
    }

    @Override
    protected boolean beforeShowModal() {
        return true;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        funcPanel.setEnabled(!readOnly && funcPanel.isEnabled());
        paramsTable.setEnabled(!readOnly && paramsTable.isEnabled());
        cbDefineParams.setEnabled(!readOnly && funcPanel.getDefinition() != null);
    }
}
