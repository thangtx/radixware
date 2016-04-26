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

package org.radixware.kernel.designer.ads.editors.clazz.algo;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.openide.util.NbBundle;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.DefaultTableCellRenderer;
import org.radixware.kernel.common.defs.RadixObject.EEditState;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.ads.clazz.algo.AdsAlgoClassDef;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EParamDirection;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.common.dialogs.choosetype.ChooseType;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;
import org.radixware.kernel.designer.common.dialogs.choosetype.ETypeNature;


public class ParametersPanel extends javax.swing.JPanel {

    private AdsAlgoClassDef algoDef;
    private RadixObjects<AdsAlgoClassDef.Param> params;
    private ParametersModel model = new ParametersModel();

    class ParametersModel extends AbstractTableModel {

        private String[] columns = new String[]{
            NbBundle.getBundle(ParametersPanel.class).getString("ParametersPanel.ColumnName"),
            NbBundle.getBundle(ParametersPanel.class).getString("ParametersPanel.ColumnDirection"),
            NbBundle.getBundle(ParametersPanel.class).getString("ParametersPanel.ColumnType")
        };

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public int getRowCount() {
            return params.size();
        }

        @Override
        public String getColumnName(int col) {
            return columns[col];
        }

        @Override
        public Object getValueAt(int row, int col) {
            AdsAlgoClassDef.Param param = params.get(row);
            switch (col) {
                case 0:
                    return param.getName();
                case 1:
                    return param.getDirection();
                case 2:
                    return param.getType();
            }
            return null;
        }

        @Override
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return true;
        }

        @Override
        public void setValueAt(Object value, int row, int col) {
//            if (true) {
////                System.out.println("Setting value at " + row + "," + col
////                                   + " to " + value
////                                   + " (an instance of "
////                                   + value.getClass() + ")");
//            }

            AdsAlgoClassDef.Param param = params.get(row);
            switch (col) {
                case 0:
                    param.setName((String) value);
                    break;
                case 1:
                    param.setDirection((EParamDirection) value);
                    break;
                case 2:
                    param.setType((AdsTypeDeclaration) value);
                    break;
            }

            param.setEditState(EEditState.MODIFIED);
            fireTableCellUpdated(row, col);

//            if (true) {
////                System.out.println("New value of data:");
//            }
        }
    }

    public ParametersPanel() {
        initComponents();

        table.setModel(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        table.setRowHeight(20);

        table.setDefaultEditor(
                EParamDirection.class,
                new DefaultCellEditor(new JComboBox(new javax.swing.DefaultComboBoxModel(EParamDirection.values()))));

        table.setDefaultEditor(String.class, new NameCellEditor());
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                update();
            }
        });

        table.setDefaultEditor(AdsTypeDeclaration.class, new TypeCellEditor());
        table.setDefaultRenderer(AdsTypeDeclaration.class, new TypeCellRenderer());

        table.requestFocusInWindow();
    }

    void open(AdsAlgoClassDef algoDef) {
        this.algoDef = algoDef;
        this.params = algoDef.getParams();
    }

    public void update() {
        setReadonly(algoDef.isReadOnly());
    }

    public void setReadonly(boolean readonly) {
        int row = table.getSelectedRow();
        int count = table.getRowCount();
        buttonAdd.setEnabled(!readonly);
        buttonRemove.setEnabled(!readonly && row >= 0 && count > 0);
        buttonUp.setEnabled(!readonly && row >= 1);
        buttonDown.setEnabled(!readonly && row >= 0 && row < count - 1);
        table.setEnabled(!readonly);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollPane = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        buttonPanel = new javax.swing.JPanel();
        buttonAdd = new javax.swing.JButton();
        buttonRemove = new javax.swing.JButton();
        buttonUp = new javax.swing.JButton();
        buttonDown = new javax.swing.JButton();

        setMinimumSize(new java.awt.Dimension(250, 100));
        setPreferredSize(new java.awt.Dimension(250, 150));

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
        table.setColumnSelectionAllowed(true);
        table.getTableHeader().setReorderingAllowed(false);
        scrollPane.setViewportView(table);

        buttonAdd.setIcon(RadixWareDesignerIcon.CREATE.ADD.getIcon());
        buttonAdd.setText(org.openide.util.NbBundle.getMessage(ParametersPanel.class, "ParametersPanel.buttonAdd.text")); // NOI18N
        buttonAdd.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        buttonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAddActionPerformed(evt);
            }
        });

        buttonRemove.setIcon(RadixWareDesignerIcon.DELETE.DELETE.getIcon());
        buttonRemove.setText(org.openide.util.NbBundle.getMessage(ParametersPanel.class, "ParametersPanel.buttonRemove.text")); // NOI18N
        buttonRemove.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        buttonRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonRemoveActionPerformed(evt);
            }
        });

        buttonUp.setIcon(RadixWareDesignerIcon.ARROW.MOVE_UP.getIcon());
        buttonUp.setText(org.openide.util.NbBundle.getMessage(ParametersPanel.class, "ParametersPanel.buttonUp.text")); // NOI18N
        buttonUp.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        buttonUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonUpActionPerformed(evt);
            }
        });

        buttonDown.setIcon(RadixWareDesignerIcon.ARROW.MOVE_DOWN.getIcon());
        buttonDown.setText(org.openide.util.NbBundle.getMessage(ParametersPanel.class, "ParametersPanel.buttonDown.text")); // NOI18N
        buttonDown.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        buttonDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDownActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout buttonPanelLayout = new javax.swing.GroupLayout(buttonPanel);
        buttonPanel.setLayout(buttonPanelLayout);
        buttonPanelLayout.setHorizontalGroup(
            buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(buttonAdd, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                    .addComponent(buttonRemove, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonUp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonDown, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        buttonPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {buttonAdd, buttonRemove});

        buttonPanelLayout.setVerticalGroup(
            buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonPanelLayout.createSequentialGroup()
                .addComponent(buttonAdd)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonRemove)
                .addGap(18, 18, 18)
                .addComponent(buttonUp)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(buttonDown))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(buttonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void buttonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAddActionPerformed
        // TODO add your handling code here:
        if (table.getCellEditor() != null) {
            table.getCellEditor().stopCellEditing();
        }

        int idx = 0;
        for (AdsAlgoClassDef.Param p : params) {
            String name = p.getName();
            if (name.startsWith("param")) {
                try {
                    idx = Math.max(idx, Integer.parseInt(name.substring(5)));
                } catch (NumberFormatException ex) {
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }
            }
        }

        int row = Math.max(table.getSelectedRow(), 0);
        AdsAlgoClassDef.Param param = new AdsAlgoClassDef.Param("param" + String.valueOf(++idx), EParamDirection.IN, AdsTypeDeclaration.Factory.newInstance(EValType.STR));
        params.add(row, param);
        table.getSelectionModel().setSelectionInterval(row, row);
        table.requestFocusInWindow();
        update();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                table.updateUI();
            }
        });
}//GEN-LAST:event_buttonAddActionPerformed

    private void buttonUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonUpActionPerformed
        // TODO add your handling code here:
        if (table.getCellEditor() != null) {
            table.getCellEditor().stopCellEditing();
        }

        int row = table.getSelectedRow();
        if (row > 0) {
            AdsAlgoClassDef.Param param0 = params.get(row - 1);
            AdsAlgoClassDef.Param param1 = params.get(row);
            params.remove(param0);
            params.remove(param1);
            params.add(row - 1, param1);
            params.add(row, param0);
            table.getSelectionModel().setSelectionInterval(row - 1, row - 1);
            table.requestFocusInWindow();
            update();

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    table.updateUI();
                }
            });
        }
}//GEN-LAST:event_buttonUpActionPerformed

    private void buttonDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDownActionPerformed
        // TODO add your handling code here:
        if (table.getCellEditor() != null) {
            table.getCellEditor().stopCellEditing();
        }

        int row = table.getSelectedRow();
        if (row > -1 && row < table.getRowCount() - 1) {
            AdsAlgoClassDef.Param param0 = params.get(row);
            AdsAlgoClassDef.Param param1 = params.get(row + 1);
            params.remove(param0);
            params.remove(param1);
            params.add(row, param1);
            params.add(row + 1, param0);
            table.getSelectionModel().setSelectionInterval(row + 1, row + 1);
            table.requestFocusInWindow();
            update();

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    table.updateUI();
                }
            });
        }
}//GEN-LAST:event_buttonDownActionPerformed

    private void buttonRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRemoveActionPerformed
        // TODO add your handling code here:
        if (table.getCellEditor() != null) {
            table.getCellEditor().stopCellEditing();
        }

        int row = table.getSelectedRow();
        if (row > -1 && row < model.getRowCount()) {
            params.remove(row);
            row = Math.min(row, model.getRowCount() - 1);
            table.getSelectionModel().setSelectionInterval(row, row);
            table.requestFocusInWindow();
            update();

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    table.updateUI();
                }
            });
        }
    }//GEN-LAST:event_buttonRemoveActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonAdd;
    private javax.swing.JButton buttonDown;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton buttonRemove;
    private javax.swing.JButton buttonUp;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables

    private class NameCellEditor extends DefaultCellEditor {

        private int row;

        public NameCellEditor() {
            super(new JTextField());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.row = row;
            return super.getTableCellEditorComponent(table, value, isSelected, row, column);
        }
        private final String[] INVALID_NAMES = {"clientData", "process", "processTraceProfile"};

        @Override
        public boolean stopCellEditing() {
            String val = (String) getCellEditorValue();
            if ("".equals(val) || Arrays.binarySearch(INVALID_NAMES, val) >= 0) {
                DialogUtils.messageError(NbBundle.getMessage(getClass(), "ParametersPanel.InvalidOrDuplicateName"));
                cancelCellEditing();
                return true;
            }
            for (int i = 0; i < params.size(); i++) {
                AdsAlgoClassDef.Param param = params.get(i);
                if (i != row && param.getName().equals(val)) {
                    DialogUtils.messageError(NbBundle.getMessage(getClass(), "ParametersPanel.InvalidOrDuplicateName"));
                    cancelCellEditing();
                    return true;
                }
            }
            return super.stopCellEditing();
        }
    }

    private class ExtLabel extends javax.swing.JPanel {

        private JLabel label = new JLabel();
        private JButton button = new JButton("...");

        public ExtLabel(ActionListener listener) {
            getInsets().set(0, 0, 0, 0);
            setLayout(new BorderLayout());
            label.setBorder(new EmptyBorder(1, 1, 1, 1));
            label.setBackground(table.getSelectionBackground());
            label.setForeground(table.getSelectionForeground());
            label.setOpaque(true);
            button.addActionListener(listener);
            button.setToolTipText(NbBundle.getMessage(ParametersPanel.class, "ParametersPanel.TypeTip"));
            button.setPreferredSize(new Dimension(20, 20));
            button.setBackground(table.getSelectionBackground());
            button.setForeground(table.getSelectionForeground());
            button.setOpaque(true);
            add(label);
            add(button, BorderLayout.EAST);
        }

        public void setIcon(RadixIcon radixIcon, int height) {
            label.setIcon(radixIcon != null ? radixIcon.getIcon(height) : null);
        }

        public void setText(String text) {
            label.setText(text);
        }

        public String getText() {
            return label.getText();
        }

        @Override
        public boolean requestFocusInWindow() {
            return label.requestFocusInWindow();
        }
    }

    private class TypeCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

        private AdsTypeDeclaration type;
        private ExtLabel editor = new ExtLabel(this);

        public TypeCellEditor() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            ChooseType.DefaultTypeFilter typeFilter = new ChooseType.DefaultTypeFilter(algoDef, params.get(0));
            typeFilter.except(ETypeNature.JAVA_PRIMITIVE);
            AdsTypeDeclaration t = ChooseType.getInstance().chooseType(typeFilter);
            if (t != null) {
                type = t;
                editor.setText(type.getName(algoDef));
                editor.setIcon(type != null ? RadixObjectIcon.getForValType(type.getTypeId()) : null, 13);
            }
            fireEditingStopped();
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int r, int c) {
            type = (AdsTypeDeclaration) value;
            if (type == null) {
                type = AdsTypeDeclaration.Factory.undefinedType();
            }
            editor.setText(type.getName(algoDef));
            editor.setIcon(type != null ? RadixObjectIcon.getForValType(type.getTypeId()) : null, 13);
            return editor;
        }

        @Override
        public Object getCellEditorValue() {
            return type;
        }
    }

    private class TypeCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            AdsTypeDeclaration type = value == null ? AdsTypeDeclaration.Factory.undefinedType() : (AdsTypeDeclaration) value;
            setText(type.getName(algoDef));
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }
            RadixIcon icon = type != null ? RadixObjectIcon.getForValType(type.getTypeId()) : null;
            setIcon(icon != null ? icon.getIcon(13) : null);
            return this;
        }
    }
}
