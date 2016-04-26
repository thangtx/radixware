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
package org.radixware.kernel.designer.ads.editors.clazz.forms.dialog;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import org.openide.explorer.propertysheet.PropertyPanel;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.ui.AdsAbstractUIDef;
import org.radixware.kernel.common.defs.ads.ui.AdsCustomWidgetDef;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.designer.ads.editors.clazz.forms.props.UIPropertySupport;
import org.radixware.kernel.designer.common.dialogs.components.CommonParametersEditorCellLib;
import org.radixware.kernel.designer.common.dialogs.components.state.StateAbstractDialog.StateAbstractPanel;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;
import org.radixware.kernel.common.resources.RadixWareIcons;

public class CustomPropertiesPanel extends StateAbstractPanel {

    private final AdsCustomWidgetDef customDef;
    private TableModel tableModel;
    private final boolean isReadOnly;
    private AdsAbstractUIDef uiDef;

    /**
     * Creates new form ParametersPanel
     */
    public CustomPropertiesPanel(AdsAbstractUIDef uiDef, final AdsCustomWidgetDef customDef) {
        this.customDef = customDef;
        this.uiDef = uiDef;
        isReadOnly = customDef.isReadOnly();
        initComponents();
        table.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent e) {
                onFocusEvent();
            }

            @Override
            public void focusGained(FocusEvent e) {
                onFocusEvent();
            }
        });
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                CustomPropertiesPanel.this.onFocusEvent();
            }
        });
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final TypePropertyChoosePanel dialog = new TypePropertyChoosePanel(customDef, "newProp");
                dialog.setBorder(new EmptyBorder(10, 10, 10, 10));
                ModalDisplayer md = new ModalDisplayer(dialog, NbBundle.getMessage(SignalsPanel.class, "Add_Prop")) {
                    @Override
                    protected void apply() {

                    }
                };
                if (md.showModal()) {
                    addPropRow(dialog.getProp());
                    check();
                }
            }
        });
        btnRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeCurProp();
            }
        });
        btnUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveRow(true);
            }
        });
        btnDown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveRow(false);
            }
        });
        createUI();
    }

    private void onFocusEvent() {
        if (tableModel != null) {
            if (tableModel.getRowCount() > 0) {
                if (table.getSelectedRow() != -1) {
                    btnRemove.setEnabled(true);
                    btnUp.setEnabled(table.getSelectedRow() > 0);
                    btnDown.setEnabled(table.getSelectedRow() < (tableModel.getRowCount() - 1));
                }
            } else {
                btnRemove.setEnabled(false);
                btnUp.setEnabled(false);
                btnDown.setEnabled(false);
            }
            check();
        }
    }

    public final void createUI() {
        btnAdd.setIcon(RadixWareIcons.CREATE.ADD.getIcon());
        btnRemove.setIcon(RadixWareIcons.DELETE.DELETE.getIcon());
        btnDown.setIcon(RadixWareIcons.ARROW.MOVE_DOWN.getIcon());
        btnUp.setIcon(RadixWareIcons.ARROW.MOVE_UP.getIcon());

        edName.setText(customDef.getName());
        edName.setEnabled(false);
        lbCustomWidgetName.setEnabled(false);

        List<AdsUIProperty> props = customDef.getProperties().list();
       //oldProps=new ArrayList();
        // oldProps.addAll(props);
        setupTableUI(props);
        btnDown.setEnabled(false);
        btnUp.setEnabled(false);
        btnRemove.setEnabled(false);
        check();
        if (isReadOnly) {
            btnAdd.setEnabled(false);

        }
    }

    private CommonParametersEditorCellLib.StringCellEditor nameEditor;
    private PropertyCellEditor valEditor;

    private void setupTableUI(List<AdsUIProperty> props) {
        tableModel = new TableModel(props);
        table.setModel(tableModel);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowSelectionAllowed(true);//.setCellSelectionEnabled(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        nameEditor = new CommonParametersEditorCellLib.StringCellEditor(changeSupport, tableModel, false);
        valEditor = new PropertyCellEditor();
        table.getColumnModel().getColumn(0).setCellEditor(nameEditor);
        table.getColumnModel().getColumn(1).setCellEditor(valEditor);
        table.getColumnModel().getColumn(1).setCellRenderer(new PropertyCellRenderer());
        table.setEnabled(!isReadOnly);
    }

    private void moveRow(boolean up) {
        stopCellEditing();
        int row = table.getSelectedRow();

        AdsUIProperty prop = (AdsUIProperty) tableModel.getValueAt(row, 1);
        removeProp(row);
        int newrow = up ? row - 1 : row + 1;
        insertPropRow(newrow, prop);
        table.requestFocusInWindow();
        table.getSelectionModel().setSelectionInterval(newrow, newrow);
    }

    private void insertPropRow(int index, AdsUIProperty property) {
        stopCellEditing();
        tableModel.insertRow(index, property);
        onFocusEvent();
    }

    private void addPropRow(AdsUIProperty property) {
        stopCellEditing();
        tableModel.addRow(property);
        onFocusEvent();
    }

    private void removeCurProp() {
        int row = table.getSelectedRow();
        removeProp(row);
    }

    private void removeProp(int row) {
        stopCellEditing();
        if (row != -1) {
            tableModel.removeRow(row);
            if (tableModel.getRowCount() > 0) {
                int index = row == 0 ? row : row - 1;
                table.getSelectionModel().setSelectionInterval(index, index);
            }
        }
    }

    private void stopCellEditing() {
        if ((valEditor.getComponent() != null) && (valEditor.getComponent().isShowing())) {
            valEditor.stopCellEditing();
        }
        if (nameEditor.getComponent().isShowing()) {
            nameEditor.stopCellEditing();
        }
    }

    @Override
    public final void check() {
        final HashSet<String> names = new HashSet<>();

        String errorMsg = NbBundle.getMessage(SignalsPanel.class, "Error_Custom_Properties");
        for (int i = 0, size = tableModel.getRowCount(); i < size; i++) {
            String name = ((CommonParametersEditorCellLib.StringCellValue) tableModel.getValueAt(i, 0)).toString();
            if (!names.add(name)) {
                stateManager.error(errorMsg + name);
                changeSupport.fireChange();
                return;
            }
            if (AdsMetaInfo.getPropByName(AdsMetaInfo.WIDGET_CLASS, name, customDef) != null) {
                errorMsg = NbBundle.getMessage(SignalsPanel.class, "Error_Inherited_Custom_Properties");
                stateManager.error(errorMsg + name);
                changeSupport.fireChange();
                return;
            }
        }
        //String duplName=checkInheritanceNames(names);
        //if(duplName==null)
        stateManager.ok();
        //else{
        //    errorMsg= NbBundle.getBundle(SignalsPanel.class).getString("Error_Inherited_Custom_Properties");
        //    stateManager.error(errorMsg + duplName);
        //}
        changeSupport.fireChange();
    }

    /* private String checkInheritanceNames(HashSet<String> names){
     AdsMetaInfo.getPropByName(customDef, customDef.getName(), customDef);
     AdsClassDef d= customDef.getModelClass().getInheritance().findSuperClass();//et.getHierarchy().findOverwritten().findOverwriteBase();
     while(d instanceof AdsDialogModelClassDef){
     AdsCustomDialogDef cw=((AdsDialogModelClassDef)d).getOwnerDialog();
     if(cw instanceof AdsCustomWidgetDef){
     List<AdsUIProperty> props=((AdsCustomWidgetDef)cw).getProperties().list();
     for(int i=0,size=props.size();i<size;i++){
     if(!names.add(props.get(i).getName()))
     return props.get(i).getName();
     }
     d=cw.getModelClass().getInheritance().findSuperClass();
     }
     }
     return null;
     }*/
    public List<AdsUIProperty> getCustomProperties() {
        List<AdsUIProperty> props = new LinkedList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            AdsUIProperty prop = (AdsUIProperty) tableModel.getValueAt(i, 1);
            props.add(prop);
        }
        return props;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane3 = new javax.swing.JScrollPane();
        table = new org.radixware.kernel.designer.common.dialogs.components.TunedTable();
        btnAdd = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        btnUp = new javax.swing.JButton();
        btnDown = new javax.swing.JButton();
        stateDisplayer1 = new org.radixware.kernel.designer.common.dialogs.components.state.StateDisplayer();
        edName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        lbCustomWidgetName = new javax.swing.JLabel();

        setMinimumSize(new java.awt.Dimension(300, 300));

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        table.setRowHeight(24);
        jScrollPane3.setViewportView(table);

        btnAdd.setText(org.openide.util.NbBundle.getMessage(CustomPropertiesPanel.class, "CustomPropertiesPanel.btnAdd.text")); // NOI18N
        btnAdd.setToolTipText(org.openide.util.NbBundle.getMessage(CustomPropertiesPanel.class, "CustomPropertiesPanel.btnAdd.toolTipText")); // NOI18N
        btnAdd.setPreferredSize(new java.awt.Dimension(32, 32));

        btnRemove.setText(org.openide.util.NbBundle.getMessage(CustomPropertiesPanel.class, "CustomPropertiesPanel.btnRemove.text")); // NOI18N
        btnRemove.setToolTipText(org.openide.util.NbBundle.getMessage(CustomPropertiesPanel.class, "CustomPropertiesPanel.btnRemove.toolTipText")); // NOI18N
        btnRemove.setPreferredSize(new java.awt.Dimension(32, 32));

        btnUp.setText(org.openide.util.NbBundle.getMessage(CustomPropertiesPanel.class, "CustomPropertiesPanel.btnUp.text")); // NOI18N
        btnUp.setToolTipText(org.openide.util.NbBundle.getMessage(CustomPropertiesPanel.class, "CustomPropertiesPanel.btnUp.toolTipText")); // NOI18N
        btnUp.setPreferredSize(new java.awt.Dimension(32, 32));

        btnDown.setText(org.openide.util.NbBundle.getMessage(CustomPropertiesPanel.class, "CustomPropertiesPanel.btnDown.text")); // NOI18N
        btnDown.setToolTipText(org.openide.util.NbBundle.getMessage(CustomPropertiesPanel.class, "CustomPropertiesPanel.btnDown.toolTipText")); // NOI18N
        btnDown.setPreferredSize(new java.awt.Dimension(32, 32));

        edName.setText(org.openide.util.NbBundle.getMessage(CustomPropertiesPanel.class, "CustomPropertiesPanel.edName.text")); // NOI18N

        jLabel2.setText(org.openide.util.NbBundle.getMessage(CustomPropertiesPanel.class, "CustomPropertiesPanel.jLabel2.text")); // NOI18N

        lbCustomWidgetName.setText(org.openide.util.NbBundle.getMessage(CustomPropertiesPanel.class, "CustomPropertiesPanel.lbCustomWidgetName.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(layout.createSequentialGroup()
                .addComponent(lbCustomWidgetName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(edName, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE))
            .addComponent(stateDisplayer1, javax.swing.GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE)
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnAdd, btnDown, btnRemove, btnUp});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(edName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbCustomWidgetName))
                .addGap(6, 6, 6)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 164, Short.MAX_VALUE)
                        .addComponent(btnUp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stateDisplayer1, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnAdd, btnDown, btnRemove, btnUp});

    }// </editor-fold>//GEN-END:initComponents

    private class TableModel extends AbstractTableModel implements ListSelectionListener {

        private String[] columns = new String[]{
            NbBundle.getMessage(ComboBoxPanel.class, "Name"),
            NbBundle.getMessage(ComboBoxPanel.class, "Init_Value")
        };

        private List<AdsUIProperty> rows = new ArrayList();

        public TableModel(List<AdsUIProperty> rows) {
            this.rows = rows;
        }

        @Override
        public int getRowCount() {
            return rows.size();
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return true;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (columnIndex == 0) {
                CommonParametersEditorCellLib.StringCellValue nameitem = new CommonParametersEditorCellLib.StringCellValue(rows.get(rowIndex).getName());
                return nameitem;
            } else {
                return rows.get(rowIndex);
            }
        }

        @Override
        public void setValueAt(Object value, int row, int col) {
            if (col == 0) {
                rows.get(row).setName(value.toString());
            } else {
                rows.set(row, (AdsUIProperty) value);
            }
            fireTableCellUpdated(row, col);
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            fireTableStructureChanged();
        }

        public void addRow(AdsUIProperty prop) {
            rows.add(prop);
            this.fireTableRowsInserted(rows.size(), rows.size());
        }

        public void insertRow(int index, AdsUIProperty prop) {
            rows.add(index, prop);
            this.fireTableRowsInserted(rows.size(), rows.size());
        }

        public void removeRow(int row) {
            rows.remove(row);
            this.fireTableRowsDeleted(row, row);
        }

        @Override
        public String getColumnName(int col) {
            return columns[col];
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnDown;
    private javax.swing.JButton btnRemove;
    private javax.swing.JButton btnUp;
    private javax.swing.JTextField edName;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lbCustomWidgetName;
    private org.radixware.kernel.designer.common.dialogs.components.state.StateDisplayer stateDisplayer1;
    private org.radixware.kernel.designer.common.dialogs.components.TunedTable table;
    // End of variables declaration//GEN-END:variables

    private class PropertyCellEditor extends AbstractCellEditor implements TableCellEditor {

        private AdsUIProperty prop;
        private PropertyPanel propPanel;

        public PropertyCellEditor() {
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int r, int c) {
            prop = (AdsUIProperty) value;
            UIPropertySupport sup = new UIPropertySupport(prop, uiDef, customDef) {
                /*@Override
                 public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

                 super.setValue(val);
                 ///listModel.fireContentChanged(customDef);
                 }*/
            };
            propPanel = new PropertyPanel(sup, PropertyPanel.PREF_TABLEUI);
            return propPanel;
        }

        @Override
        public Object getCellEditorValue() {
            return prop;
        }

        public Component getComponent() {
            return propPanel;
        }
    }

    private class PropertyCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            AdsUIProperty prop = (AdsUIProperty) value;
            UIPropertySupport sup = new UIPropertySupport(prop, uiDef, customDef);
            return new PropertyPanel(sup, PropertyPanel.PREF_TABLEUI);
        }
    }

}
