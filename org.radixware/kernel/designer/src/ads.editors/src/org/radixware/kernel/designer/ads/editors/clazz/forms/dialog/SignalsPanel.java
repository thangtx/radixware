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
 * SignalsPanel.java
 *
 * Created on Jul 29, 2009, 11:20:25 AM
 */
package org.radixware.kernel.designer.ads.editors.clazz.forms.dialog;

import java.awt.Component;
import java.awt.Dimension;
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
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.components.ExtendableTextField;
import org.radixware.kernel.common.defs.ads.AdsDefinitions;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.ui.AdsAbstractUIDef;
import org.radixware.kernel.common.defs.ads.ui.AdsCustomWidgetDef;
import org.radixware.kernel.common.defs.ads.ui.AdsUISignalDef;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtCustomDialogDef;
import org.radixware.kernel.designer.common.dialogs.components.CommonParametersEditorCellLib;
import org.radixware.kernel.designer.common.dialogs.components.state.StateAbstractDialog.StateAbstractPanel;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;
import org.radixware.kernel.common.resources.RadixWareIcons;


public class SignalsPanel extends StateAbstractPanel {

    private AdsAbstractUIDef customDef;
    private TableModel tableModel;
    private boolean isReadOnly;

    /**
     * Creates new form SignalsPanel
     */
    public SignalsPanel(final AdsAbstractUIDef customDef) {
        this.customDef = customDef;
        isReadOnly = customDef.isReadOnly();
        initComponents();
        table.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent e) {
                SignalsPanel.this.onFocusEvent();
            }

            @Override
            public void focusGained(FocusEvent e) {
                SignalsPanel.this.onFocusEvent();
            }
        });
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                SignalsPanel.this.onFocusEvent();
            }
        });
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final SignalParametersPanel dialog = new SignalParametersPanel(customDef, "newSignal", null, true);
                dialog.setBorder(new EmptyBorder(10, 10, 10, 10));
                final ModalDisplayer md = new ModalDisplayer(dialog, NbBundle.getMessage(SignalsPanel.class, "Add_Signal")) {
                    @Override
                    protected void apply() {
                        //ignore
                    }
                };
                if (md.showModal()) {
                    SignalsPanel.this.addSignal(dialog.getSignalName(), dialog.getSignalTypes());
                    check();
                }
            }
        });
        btnRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SignalsPanel.this.removeCurSignal();
                check();
            }
        });
        btnUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SignalsPanel.this.moveRow(true);
            }
        });

        btnDown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SignalsPanel.this.moveRow(false);
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

        setupTableUI();//NOPMD
        btnDown.setEnabled(false);
        btnUp.setEnabled(false);
        btnRemove.setEnabled(false);
        btnAdd.setEnabled(!isReadOnly);
        check();
    }
    private TableCellTypeEditor typesEditor;
    private CommonParametersEditorCellLib.StringCellEditor nameEditor;

    private AdsDefinitions<AdsUISignalDef> getSignalSet() {
        if (customDef instanceof AdsCustomWidgetDef) {
            return ((AdsCustomWidgetDef) customDef).getSignals();
        } else if (customDef instanceof AdsRwtCustomDialogDef) {
            return ((AdsRwtCustomDialogDef) customDef).getCloseSignals();
        } else {
            return null;
        }
    }

    private List<AdsUISignalDef> listSignals() {
        AdsDefinitions<AdsUISignalDef> defs = getSignalSet();
        if (defs == null) {
            return new LinkedList<>();
        } else {
            return defs.list();
        }
    }

    private void setupTableUI() {
        String[] titles = new String[2];
        titles[0] = NbBundle.getMessage(SignalsPanel.class, "Name");
        titles[1] = NbBundle.getMessage(SignalsPanel.class, "Params");
        List<AdsUISignalDef> signals = listSignals();
        tableModel = new TableModel(signals, titles);
        table.setModel(tableModel);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setCellSelectionEnabled(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        nameEditor = new CommonParametersEditorCellLib.StringCellEditor(changeSupport, tableModel, false);
        nameEditor.addCellEditorListener(new CellEditorListener() {
            @Override
            public void editingStopped(ChangeEvent e) {
                tableModel.setName(table.getSelectedRow(), nameEditor.getCellEditorValue().toString());
            }

            @Override
            public void editingCanceled(ChangeEvent e) {
            }
        });
        typesEditor = new TableCellTypeEditor();
        table.getColumnModel().getColumn(0).setCellEditor(nameEditor);
        table.getColumnModel().getColumn(1).setCellEditor(typesEditor);
        table.setEnabled(!isReadOnly);
    }

    private void addSignal(String signalName, List<AdsTypeDeclaration> types) {
        stopCellEditing();
        AdsUISignalDef signal = new AdsUISignalDef(signalName, types);
        tableModel.addRow(signal);
        onFocusEvent();
    }

    private void removeCurSignal() {
        int row = table.getSelectedRow();
        removeSignal(row);
    }

    private void removeSignal(int row) {
        stopCellEditing();
        if (row != -1) {
            tableModel.removeRow(row);
            if (tableModel.getRowCount() > 0) {
                int index = row == 0 ? row : row - 1;
                table.getSelectionModel().setSelectionInterval(index, index);
            }
        }
    }
    static private String REPEATED_NAMES = NbBundle.getBundle(SignalsPanel.class).getString("Error_Duplicated_Sigals");

    private void moveRow(boolean up) {
        stopCellEditing();
        int row = table.getSelectedRow();
        AdsUISignalDef curSignal = tableModel.getSignal(row);
        removeSignal(row);
        int newrow = up ? row - 1 : row + 1;
        tableModel.insertRow(newrow, curSignal);
        table.requestFocusInWindow();
        table.getSelectionModel().setSelectionInterval(newrow, newrow);
    }

    private void stopCellEditing() {
        if (typesEditor.getComponent().isShowing()) {
            typesEditor.stopCellEditing();
        }
        if (nameEditor.getComponent().isShowing()) {
            nameEditor.stopCellEditing();
        }
    }

    private void setSignalTypes(int index, List<AdsTypeDeclaration> types) {
        stopCellEditing();
        tableModel.setSignalTypes(index, types);
        table.requestFocusInWindow();
        table.getSelectionModel().setSelectionInterval(index, index);
    }

    public final List<AdsUISignalDef> getSignals() {
        return tableModel.getSignals();
    }

    @Override
    public final void check() {
        final HashSet<String> names = new HashSet<>();
        for (int i = 0, size = tableModel.getRowCount(); i < size; i++) {
            final String name = ((CommonParametersEditorCellLib.StringCellValue) tableModel.getValueAt(i, 0)).toString();
            final String types = ((DeclarationPresentation) tableModel.getValueAt(i, 1)).toString();
            final String singalName = name + "(" + types + ")";
            if (!names.add(singalName)) {
                stateManager.error(REPEATED_NAMES + singalName);
                changeSupport.fireChange();
                return;
            }
        }
        stateManager.ok();
        changeSupport.fireChange();
    }

    public class TableCellTypeEditor extends AbstractCellEditor implements TableCellEditor {

        private final ExtendableTextField extendableTextField;
        private Object val;

        public TableCellTypeEditor() {
            super();

            extendableTextField = new ExtendableTextField(false);
            Dimension d = new Dimension(extendableTextField.getPreferredSize().width, table.getRowHeight());
            extendableTextField.setPreferredSize(d);
            extendableTextField.setEditable(false);
            extendableTextField.addButton("...");
            extendableTextField.getButtons().get(0).addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int row = table.getSelectedRow();
                    String name = ((CommonParametersEditorCellLib.StringCellValue) tableModel.getValueAt(row, 0)).toString();
                    List<AdsTypeDeclaration> types = ((DeclarationPresentation) tableModel.getValueAt(row, 1)).getTypes();
                    final SignalParametersPanel dialog = new SignalParametersPanel(customDef, name, types, false);
                    dialog.setBorder(new EmptyBorder(10, 10, 10, 10));
                    ModalDisplayer md = new ModalDisplayer(dialog, NbBundle.getMessage(SignalsPanel.class, "Edit_Signal")) {
                        @Override
                        protected void apply() {
                        }
                    };
                    if (md.showModal()) {
                        setSignalTypes(row, dialog.getSignalTypes());
                        check();
                    }
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (value instanceof DeclarationPresentation) {
                val = value;
                extendableTextField.setValue((DeclarationPresentation) value);
            }
            return extendableTextField;
        }

        @Override
        public Object getCellEditorValue() {
            return val;
        }

        public Component getComponent() {
            return extendableTextField;
        }
    }

    public static class DeclarationPresentation {

        private List<AdsTypeDeclaration> types;

        public DeclarationPresentation(List<AdsTypeDeclaration> types) {
            this.types = types;
            if (types == null) {
                this.types = new LinkedList<AdsTypeDeclaration>();
            }
        }

        public List<AdsTypeDeclaration> getTypes() {
            return types;
        }

        public void setTypes(List<AdsTypeDeclaration> types) {
            this.types.clear();
            this.types.addAll(types);
        }

        @Override
        public String toString() {
            String res = "";
            int size = types.size();
            for (int i = 0; i < size; i++) {
                res += types.get(i).getTypeId().getName() + ", ";
            }
            if (size > 0) {
                res = res.substring(0, res.length() - 2);
            }
            return res;
        }
    }

    private class TableModel extends DefaultTableModel {

        private class SignalInfo {

            private AdsUISignalDef signal;
            private String newName;
            private List<AdsTypeDeclaration> newTypes;

            SignalInfo(AdsUISignalDef signal) {
                this.signal = signal;
                newName = signal.getName();
                setTypes(signal.getTypes().list());
            }

            AdsUISignalDef getSignal() {
                return signal;
            }

            final void setName(String s) {
                newName = s;
            }

            final void setTypes(List<AdsTypeDeclaration> types) {
                newTypes = new ArrayList<>();
                for (AdsTypeDeclaration type : types) {
                    newTypes.add(AdsTypeDeclaration.Factory.newCopy(type));
                }
            }

            String getName() {
                return newName;
            }

            List<AdsTypeDeclaration> getTypes() {
                return newTypes;
            }

            AdsUISignalDef getSignalWithNewVals() {
                signal.setName(newName);
                signal.getTypes().clear();
                for (AdsTypeDeclaration type : newTypes) {
                    signal.getTypes().add(type);
                }
                return signal;
            }
        }
        private LinkedList<SignalInfo> signalInfos = new LinkedList<>();
        private Object[] columnNames;

        TableModel(List<AdsUISignalDef> signals, Object[] columnNames) {
            super(columnNames, 0);
            this.columnNames = columnNames;
            for (int i = 0; i < signals.size(); i++) {
                addRow(signals.get(i));
            }
        }

        @Override
        public int getRowCount() {
            if (signalInfos == null) {
                return 0;
            }
            return signalInfos.size();
        }

        @Override
        public int getColumnCount() {
            if (columnNames == null) {
                return 0;
            }
            return columnNames.length;
        }

        @Override
        public String getColumnName(int i) {
            return columnNames[i].toString();
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (columnIndex == 0) {
                return new CommonParametersEditorCellLib.StringCellValue(signalInfos.get(rowIndex).getName());//signals.get(rowIndex).getName();
            } else {
                return new DeclarationPresentation(signalInfos.get(rowIndex).getTypes());
            }
        }

        public final void addRow(AdsUISignalDef signal) {
            signalInfos.add(new SignalInfo(signal));
            CommonParametersEditorCellLib.StringCellValue nameitem = new CommonParametersEditorCellLib.StringCellValue(signal.getName());
            DeclarationPresentation type = new DeclarationPresentation(signal.getTypes().list());
            Object[] rowData = {nameitem, type};
            super.insertRow(signalInfos.size() - 1, rowData);
        }

        @Override
        public void removeRow(int row) {
            if (row >= 0 && row < signalInfos.size()) {
                signalInfos.remove(row);
                super.removeRow(row);
            }
        }

        public void insertRow(int index, AdsUISignalDef signal) {
            signalInfos.add(index, new SignalInfo(signal));
            CommonParametersEditorCellLib.StringCellValue nameitem = new CommonParametersEditorCellLib.StringCellValue(signal.getName());
            DeclarationPresentation type = new DeclarationPresentation(signal.getTypes().list());
            Object[] rowData = {nameitem, type};
            super.insertRow(index, rowData);
        }

        public AdsUISignalDef getSignal(int row) {
            return signalInfos.get(row).getSignal();
        }

        public void setSignalTypes(int index, List<AdsTypeDeclaration> types) {
            /*AdsUISignalDef signal=signals.get(index).getSignal();
             signal.getTypes().clear();
             for(AdsTypeDeclaration type:types)
             *               signal.getTypes().add(type);*/

            SignalInfo signalInfo = signalInfos.get(index);
            signalInfo.setTypes(types);
            this.fireTableRowsUpdated(index, index);
        }

        public void setName(int index, String name) {
            //AdsUISignalDef signal=signals.get(index).getSignal();
            //signal.setName(name);
            SignalInfo signalInfo = signalInfos.get(index);
            signalInfo.setName(name);
            this.fireTableCellUpdated(index, 0);
        }

        public LinkedList<AdsUISignalDef> getSignals() {
            LinkedList<AdsUISignalDef> result = new LinkedList<>();
            for (SignalInfo s : signalInfos) {
                result.add(s.getSignalWithNewVals());
            }
            return result;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        btnAdd = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        btnUp = new javax.swing.JButton();
        btnDown = new javax.swing.JButton();
        lbCustomWidgetName = new javax.swing.JLabel();
        edName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        stateDisplayer1 = new org.radixware.kernel.designer.common.dialogs.components.state.StateDisplayer();
        jScrollPane3 = new javax.swing.JScrollPane();
        table = new org.radixware.kernel.designer.common.dialogs.components.TunedTable();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(jTable2);

        setMinimumSize(new java.awt.Dimension(300, 300));
        setPreferredSize(new java.awt.Dimension(400, 430));

        btnAdd.setText(org.openide.util.NbBundle.getMessage(SignalsPanel.class, "SignalsPanel.btnAdd.text")); // NOI18N
        btnAdd.setToolTipText(org.openide.util.NbBundle.getMessage(SignalsPanel.class, "SignalsPanel.btnAdd.toolTipText")); // NOI18N
        btnAdd.setPreferredSize(new java.awt.Dimension(32, 32));

        btnRemove.setText(org.openide.util.NbBundle.getMessage(SignalsPanel.class, "SignalsPanel.btnRemove.text")); // NOI18N
        btnRemove.setToolTipText(org.openide.util.NbBundle.getMessage(SignalsPanel.class, "SignalsPanel.btnRemove.toolTipText")); // NOI18N
        btnRemove.setPreferredSize(new java.awt.Dimension(32, 32));

        btnUp.setText(org.openide.util.NbBundle.getMessage(SignalsPanel.class, "SignalsPanel.btnUp.text")); // NOI18N
        btnUp.setToolTipText(org.openide.util.NbBundle.getMessage(SignalsPanel.class, "SignalsPanel.btnUp.toolTipText")); // NOI18N
        btnUp.setPreferredSize(new java.awt.Dimension(32, 32));

        btnDown.setText(org.openide.util.NbBundle.getMessage(SignalsPanel.class, "SignalsPanel.btnDown.text")); // NOI18N
        btnDown.setToolTipText(org.openide.util.NbBundle.getMessage(SignalsPanel.class, "SignalsPanel.btnDown.toolTipText")); // NOI18N
        btnDown.setPreferredSize(new java.awt.Dimension(32, 32));

        lbCustomWidgetName.setText(org.openide.util.NbBundle.getMessage(SignalsPanel.class, "SignalsPanel.lbCustomWidgetName.text")); // NOI18N

        edName.setText(org.openide.util.NbBundle.getMessage(SignalsPanel.class, "SignalsPanel.edName.text")); // NOI18N

        jLabel2.setText(org.openide.util.NbBundle.getMessage(SignalsPanel.class, "SignalsPanel.jLabel2.text")); // NOI18N

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        table.setRowHeight(24);
        jScrollPane3.setViewportView(table);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addContainerGap(365, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addComponent(stateDisplayer1, javax.swing.GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(lbCustomWidgetName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(edName, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnAdd, btnDown, btnRemove, btnUp});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbCustomWidgetName)
                    .addComponent(edName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 164, Short.MAX_VALUE)
                        .addComponent(btnUp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stateDisplayer1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnAdd, btnDown, btnRemove, btnUp});

    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnDown;
    private javax.swing.JButton btnRemove;
    private javax.swing.JButton btnUp;
    private javax.swing.JTextField edName;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JLabel lbCustomWidgetName;
    private org.radixware.kernel.designer.common.dialogs.components.state.StateDisplayer stateDisplayer1;
    private org.radixware.kernel.designer.common.dialogs.components.TunedTable table;
    // End of variables declaration//GEN-END:variables
}
