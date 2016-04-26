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
 * DdsTypeFieldsEditor.java
 *
 * Created on Apr 8, 2009, 5:23:21 PM
 */
package org.radixware.kernel.designer.dds.editors;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.EventObject;
import java.util.HashSet;
import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import org.openide.util.ChangeSupport;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.components.ExtendableTextField;
import org.radixware.kernel.common.defs.dds.DdsDefinitions;
import org.radixware.kernel.common.defs.dds.DdsTypeFieldDef;
import org.radixware.kernel.designer.common.dialogs.components.TunedTable.TunedCellEditor;
import org.radixware.kernel.designer.common.dialogs.components.state.StateManager;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;
import org.radixware.kernel.designer.common.dialogs.components.DescriptionEditPanel;
import org.radixware.kernel.common.resources.RadixWareIcons;


public class DdsTypeFieldsEditor extends javax.swing.JPanel {

    private DdsDefinitions<DdsTypeFieldDef> fields = null;
    private boolean editable = true;

    /**
     * Creates new form DdsTypeFieldsEditor
     */
    public DdsTypeFieldsEditor() {
        initComponents();

        this.setMinimumSize(new Dimension(500, 300));
        this.setPreferredSize(new Dimension(500, 300));

        addButton.setIcon(RadixWareIcons.CREATE.ADD.getIcon());
        removeButton.setIcon(RadixWareIcons.DELETE.DELETE.getIcon());
        removeAllButton.setIcon(RadixWareIcons.DELETE.CLEAR.getIcon());
        upButton.setIcon(RadixWareIcons.ARROW.MOVE_UP.getIcon());
        downButton.setIcon(RadixWareIcons.ARROW.MOVE_DOWN.getIcon());

        initFieldsTable();

        fieldsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                updateButtonsEnableState();
            }
        });

        initButton(addButton, KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, 0), "INSERT");
        initButton(removeButton, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "DELETE");
        initButton(removeAllButton, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, KeyEvent.SHIFT_DOWN_MASK), "DELETE_ALL");
        initButton(upButton, KeyStroke.getKeyStroke(KeyEvent.VK_UP, KeyEvent.CTRL_DOWN_MASK), "UP");
        initButton(downButton, KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, KeyEvent.CTRL_DOWN_MASK), "DOWN");

    }

    private void updateButtonsEnableState() {
        int row = fieldsTable.getSelectedRow();
        removeButton.setEnabled(editable && row != -1);
        upButton.setEnabled(editable && row > 0);
        downButton.setEnabled(editable && row != -1 && row < fieldsTable.getRowCount() - 1);
        addButton.setEnabled(editable);
        removeAllButton.setEnabled(editable && fieldsTable.getRowCount() > 0);
    }

    private void initButton(final JButton button, KeyStroke keyStroke, String key) {
        AbstractAction action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (button.isEnabled()) {
                    button.doClick();
                }
            }
        };
        InputMap inputMap = fieldsTable.getInputMap(JTable.WHEN_FOCUSED);
        inputMap.put(keyStroke, key);
        fieldsTable.getActionMap().put(key, action);
    }

    private void initFieldsTable() {
        String[] titles = {
            NbBundle.getMessage(DdsTypeFieldsEditor.class, "NAME"),
            NbBundle.getMessage(DdsTypeFieldsEditor.class, "DB_TYPE_COLUMN"),
            NbBundle.getMessage(DdsTypeFieldsEditor.class, "DESCRIPTION")
        };
        fieldsTableModel = new DefaultTableModel(titles, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return editable;
            }
        };
        fieldsTable.setRowHeight(24);
        fieldsTable.setModel(fieldsTableModel);
        fieldsTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        fieldsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fieldsTable.setCellSelectionEnabled(true);
        nameEditor = new NameCellEditor();
        dbTypeEditor = new NameCellEditor();
        commentEditor = new CommentCellEditor();
        fieldsTable.getColumnModel().getColumn(0).setCellEditor(nameEditor);
        fieldsTable.getColumnModel().getColumn(1).setCellEditor(dbTypeEditor);
        fieldsTable.getColumnModel().getColumn(2).setCellEditor(commentEditor);
    }

    private class DdsTypeFieldsDisplayer extends ModalDisplayer implements ChangeListener {

        public DdsTypeFieldsDisplayer(DdsTypeFieldsEditor editor, DdsDefinitions<DdsTypeFieldDef> fields) {
            super(editor);
            editor.open(fields);
            getDialogDescriptor().setValid(editor.isComplete());
            editor.addChangeListener(this);
            setTitle(NbBundle.getMessage(DdsTypeFieldsEditor.class, "TYPE_FIELDS"));
            setIcon(fields.getIcon());
        }
        private boolean isEdited = false;

        @Override
        protected void apply() {
            if (fields != null && isEdited) {
                stopEditingAll();
                fields.clear();
                for (int i = 0; i < fieldsTableModel.getRowCount(); i++) {
                    final String name = fieldsTableModel.getValueAt(i, 0).toString();
                    DdsTypeFieldDef field = DdsTypeFieldDef.Factory.newInstance(name);
                    field.setDbType(fieldsTableModel.getValueAt(i, 1).toString());
                    field.setDescription(fieldsTableModel.getValueAt(i, 2).toString());
                    fields.add(field);
                }
            }
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            if (e.getSource().equals(getComponent())) {
                isEdited = true;
                getDialogDescriptor().setValid(((DdsTypeFieldsEditor) getComponent()).isComplete());
            }
        }
    }

    public void open(DdsDefinitions<DdsTypeFieldDef> fields) {
        this.fields = fields;
        update();
    }

    public void update() {
        if (fields != null) {
            fieldsTableModel.setRowCount(0);
            for (DdsTypeFieldDef field : fields) {
                fieldsTableModel.addRow(new Object[]{
                    field.getName(),
                    field.getDbType(),
                    field.getDescription()
                });
            }
            updateButtonsEnableState();
        }
    }

    private void stopEditingAll() {
        nameEditor.stopCellEditing();
        dbTypeEditor.stopCellEditing();
        commentEditor.stopCellEditing();
    }

//    public void apply() {
//
//    }
    public void editFields(DdsDefinitions<DdsTypeFieldDef> fields, boolean readOnly) {
        this.editable = !readOnly;
        DdsTypeFieldsDisplayer displayer = new DdsTypeFieldsDisplayer(this, fields);
        displayer.showModal();
    }
    private DefaultTableModel fieldsTableModel;
    private NameCellEditor nameEditor;
    private NameCellEditor dbTypeEditor;
    private CommentCellEditor commentEditor;

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        fieldsTable = new org.radixware.kernel.designer.common.dialogs.components.TunedTable();
        addButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();
        removeAllButton = new javax.swing.JButton();
        upButton = new javax.swing.JButton();
        downButton = new javax.swing.JButton();
        stateDisplayer1 = new org.radixware.kernel.designer.common.dialogs.components.state.StateDisplayer();

        fieldsTable.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(fieldsTable);

        addButton.setText(org.openide.util.NbBundle.getMessage(DdsTypeFieldsEditor.class, "DdsTypeFieldsEditor.addButton.text")); // NOI18N
        addButton.setToolTipText(org.openide.util.NbBundle.getMessage(DdsTypeFieldsEditor.class, "DdsTypeFieldsEditor.addButton.toolTipText")); // NOI18N
        addButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        removeButton.setText(org.openide.util.NbBundle.getMessage(DdsTypeFieldsEditor.class, "DdsTypeFieldsEditor.removeButton.text")); // NOI18N
        removeButton.setToolTipText(org.openide.util.NbBundle.getMessage(DdsTypeFieldsEditor.class, "DdsTypeFieldsEditor.removeButton.toolTipText")); // NOI18N
        removeButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });

        removeAllButton.setText(org.openide.util.NbBundle.getMessage(DdsTypeFieldsEditor.class, "DdsTypeFieldsEditor.removeAllButton.text")); // NOI18N
        removeAllButton.setToolTipText(org.openide.util.NbBundle.getMessage(DdsTypeFieldsEditor.class, "DdsTypeFieldsEditor.removeAllButton.toolTipText")); // NOI18N
        removeAllButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        removeAllButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeAllButtonActionPerformed(evt);
            }
        });

        upButton.setText(org.openide.util.NbBundle.getMessage(DdsTypeFieldsEditor.class, "DdsTypeFieldsEditor.upButton.text")); // NOI18N
        upButton.setToolTipText(org.openide.util.NbBundle.getMessage(DdsTypeFieldsEditor.class, "DdsTypeFieldsEditor.upButton.toolTipText")); // NOI18N
        upButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        upButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upButtonActionPerformed(evt);
            }
        });

        downButton.setText(org.openide.util.NbBundle.getMessage(DdsTypeFieldsEditor.class, "DdsTypeFieldsEditor.downButton.text")); // NOI18N
        downButton.setToolTipText(org.openide.util.NbBundle.getMessage(DdsTypeFieldsEditor.class, "DdsTypeFieldsEditor.downButton.toolTipText")); // NOI18N
        downButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        downButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
                    .addComponent(stateDisplayer1, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(addButton)
                    .addComponent(removeButton)
                    .addComponent(removeAllButton)
                    .addComponent(upButton)
                    .addComponent(downButton))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(addButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeAllButton)
                        .addGap(18, 18, 18)
                        .addComponent(upButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(downButton))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stateDisplayer1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        fieldsTableModel.addRow(new Object[]{"NewField", "integer", ""});
        changeSupport.fireChange();
        fieldsTable.clearSelection();
        fieldsTable.requestFocusInWindow();
        int last = fieldsTableModel.getRowCount() - 1;
        fieldsTable.setRowSelectionInterval(last, last);
        fieldsTable.editCellAt(last, 0);
        nameEditor.getComponent().requestFocusInWindow();
    }//GEN-LAST:event_addButtonActionPerformed

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed
        stopEditingAll();
        int row = fieldsTable.getSelectedRow();
        if (row == -1) {
            return;
        }
        int cnt = fieldsTable.getRowCount();
        boolean last = row == cnt - 1;
        fieldsTableModel.removeRow(row);
        changeSupport.fireChange();
        if (last) {
            if (cnt > 1) {
                fieldsTable.setRowSelectionInterval(row - 1, row - 1);
            } else {
                fieldsTable.clearSelection();
            }
        } else {
            fieldsTable.setRowSelectionInterval(row, row);
        }
        fieldsTable.requestFocusInWindow();
    }//GEN-LAST:event_removeButtonActionPerformed

    private void removeAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeAllButtonActionPerformed
        stopEditingAll();
        fieldsTableModel.setRowCount(0);
        changeSupport.fireChange();
        fieldsTable.clearSelection();
        fieldsTable.requestFocusInWindow();
    }//GEN-LAST:event_removeAllButtonActionPerformed

    private void upButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upButtonActionPerformed
        stopEditingAll();
        int row = fieldsTable.getSelectedRow();
        if (row < 1) {
            return;
        }
        for (int i = 0; i < 3; ++i) {
            Object up = fieldsTableModel.getValueAt(row - 1, i);
            Object cur = fieldsTableModel.getValueAt(row, i);
            fieldsTableModel.setValueAt(cur, row - 1, i);
            fieldsTableModel.setValueAt(up, row, i);
        }
        changeSupport.fireChange();
        fieldsTable.setRowSelectionInterval(row - 1, row - 1);
    }//GEN-LAST:event_upButtonActionPerformed

    private void downButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downButtonActionPerformed
        stopEditingAll();
        int row = fieldsTable.getSelectedRow();
        if (row == -1 || row == fieldsTable.getRowCount() - 1) {
            return;
        }
        for (int i = 0; i < 3; ++i) {
            Object down = fieldsTableModel.getValueAt(row + 1, i);
            Object cur = fieldsTableModel.getValueAt(row, i);
            fieldsTableModel.setValueAt(cur, row + 1, i);
            fieldsTableModel.setValueAt(down, row, i);
        }
        changeSupport.fireChange();
        fieldsTable.setRowSelectionInterval(row + 1, row + 1);
    }//GEN-LAST:event_downButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton downButton;
    private org.radixware.kernel.designer.common.dialogs.components.TunedTable fieldsTable;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton removeAllButton;
    private javax.swing.JButton removeButton;
    private org.radixware.kernel.designer.common.dialogs.components.state.StateDisplayer stateDisplayer1;
    private javax.swing.JButton upButton;
    // End of variables declaration//GEN-END:variables
    private ChangeSupport changeSupport = new ChangeSupport(this);

    public void addChangeListener(ChangeListener l) {
        changeSupport.addChangeListener(l);
    }

    public void removeChangeListener(ChangeListener l) {
        changeSupport.removeChangeListener(l);
    }
    private final StateManager stateManager = new StateManager(this);

    public boolean isComplete() {
        int row = getRowWithUndefinedName();
        if (row != -1) {
            stateManager.error(UNDEFINED_NAME + " " + row);
            return false;
        }
        row = getRowWithUndefinedDbType();
        if (row != -1) {
            stateManager.error(UNDEFINED_DB_TYPE + " " + row);
            return false;
        }
        String name = getRepeatedName();
        if (name != null) {
            stateManager.error(REPEATED_NAME + " " + name);
            return false;
        }
        stateManager.ok();
        return true;
    }
    private static final String UNDEFINED_NAME = NbBundle.getMessage(DdsTypeFieldsEditor.class, "UNDEFINED_NAME");
    private static final String UNDEFINED_DB_TYPE = NbBundle.getMessage(DdsTypeFieldsEditor.class, "UNDEFINED_DB_TYPE");
    private static final String REPEATED_NAME = NbBundle.getMessage(DdsTypeFieldsEditor.class, "REPEATED_NAME");

    private int getRowWithUndefinedName() {
        if (fieldsTableModel != null) {
            int row = nameEditor.getCurrentRow();
            if (nameEditor.getComponent().isShowing()) {
                if (nameEditor.getCellEditorValue().toString().isEmpty()) {
                    return row + 1;
                }
                for (int i = 0; i < fieldsTableModel.getRowCount(); i++) {
                    if (i != row && fieldsTableModel.getValueAt(i, 0).toString().isEmpty()) {
                        return i + 1;
                    }
                }
            } else {
                for (int i = 0; i < fieldsTableModel.getRowCount(); i++) {
                    if (fieldsTableModel.getValueAt(i, 0).toString().isEmpty()) {
                        return i + 1;
                    }
                }
            }
        }
        return -1;
    }

    private int getRowWithUndefinedDbType() {
        if (fieldsTableModel != null) {
            int row = dbTypeEditor.getCurrentRow();
            if (dbTypeEditor.getComponent().isShowing()) {
                if (dbTypeEditor.getCellEditorValue().toString().isEmpty()) {
                    return row + 1;
                }
                for (int i = 0; i < fieldsTableModel.getRowCount(); i++) {
                    if (i != row && fieldsTableModel.getValueAt(i, 1).toString().isEmpty()) {
                        return i + 1;
                    }
                }
            } else {
                for (int i = 0; i < fieldsTableModel.getRowCount(); i++) {
                    if (fieldsTableModel.getValueAt(i, 1).toString().isEmpty()) {
                        return i + 1;
                    }
                }
            }
        }
        return -1;
    }

    private String getRepeatedName() {
        if (fieldsTableModel != null) {
            HashSet<String> names = new HashSet<String>(fieldsTableModel.getRowCount());
            int row = nameEditor.getCurrentRow();
            if (nameEditor.getComponent().isShowing()) {
                names.add(nameEditor.getCellEditorValue().toString());
                for (int i = 0; i < fieldsTableModel.getRowCount(); i++) {
                    if (i != row && !names.add(fieldsTableModel.getValueAt(i, 0).toString())) {
                        return fieldsTableModel.getValueAt(i, 0).toString();
                    }
                }
            } else {
                for (int i = 0; i < fieldsTableModel.getRowCount(); i++) {
                    if (!names.add(fieldsTableModel.getValueAt(i, 0).toString())) {
                        return fieldsTableModel.getValueAt(i, 0).toString();
                    }
                }
            }
        }
        return null;
    }

    private class NameCellEditor extends AbstractCellEditor implements TableCellEditor {

        private JTextField editor = new JTextField();
        private int row;

        public NameCellEditor() {
            editor.addCaretListener(new CaretListener() {
                @Override
                public void caretUpdate(CaretEvent e) {
                    changeSupport.fireChange();
                }
            });
        }

        public Component getComponent() {
            return editor;
        }

        public int getCurrentRow() {
            return row;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int r, int c) {
            this.row = r;
            editor.setText(value != null ? value.toString() : "");
            editor.setCaretPosition(editor.getText().length());
            editor.getCaret().setVisible(true);
            editor.select(0, editor.getText().length());
            return editor;
        }

        @Override
        public Object getCellEditorValue() {
            return editor.getText();
        }

        @Override
        public boolean isCellEditable(EventObject e) {
            return super.isCellEditable(e);
        }

        @Override
        public boolean shouldSelectCell(EventObject e) {
            return super.shouldSelectCell(e);
        }
    }

    private class CommentCellEditor extends AbstractCellEditor implements TableCellEditor, TunedCellEditor {

        private ExtendableTextField editor = new ExtendableTextField(true);
        private ActionListener btnListener;
        private JButton btn;
        private String val;

        public CommentCellEditor() {
            btn = editor.addButton();
            btn.setIcon(RadixWareIcons.DIALOG.CHOOSE.getIcon(13, 13));
            btnListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    DescriptionEditPanel panel = new DescriptionEditPanel();
                    String res = panel.edit(val);
                    if (!res.equals(val)) {
                        editor.setValue(res);
                        val = res;
                        changeSupport.fireChange();
                    }
                }
            };
            btn.addActionListener(btnListener);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int r, int c) {
            this.val = value.toString();
            editor.setValue(value.toString());
            return editor;
        }

        @Override
        public Object getCellEditorValue() {
            return val;
        }

        @Override
        public void editingPerformed(ActionEvent e) {
            btnListener.actionPerformed(new ActionEvent(btn, e.getID(), e.getActionCommand(), e.getWhen(), e.getModifiers()));
        }
    }
}
