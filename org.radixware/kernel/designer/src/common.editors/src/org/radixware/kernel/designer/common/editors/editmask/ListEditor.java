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
 * ListEditor.java
 *
 * Created on Jul 29, 2009, 3:30:48 PM
 */
package org.radixware.kernel.designer.common.editors.editmask;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.components.ExtendableTextField;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMask;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskList;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.designer.common.dialogs.components.PropertyValueEditPanel;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.types.Id;

class ListEditor extends Editor {
    
    @Override
    public void revert() {
//        for (EditMaskList.Item item : model.getItems()) {
//            if (item.getTitleId() != null) {
//                AdsMultilingualStringDef stringDef = definition.findLocalizedString(item.getTitleId());
//                definition.findLocalizingBundle().getStrings().getLocal().remove(stringDef);
//            }
//        }
    }
    
    private class ValueCellEditor extends AbstractCellEditor implements TableCellEditor {
        
        private final PropertyValueEditPanel editor = new PropertyValueEditPanel();
        
        public ValueCellEditor() {
            editor.setNullAble(true);
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            switch (editMaskType) {
                case INT:
                    editor.setValue(EValType.INT, ValAsStr.Factory.newInstance(value, EValType.INT));
                    break;
                case NUM:
                    editor.setValue(EValType.NUM, ValAsStr.Factory.newInstance(value, EValType.NUM));                    
                    break;
                default:                    
                    editor.setValue(EValType.STR, ValAsStr.Factory.newInstance(value, EValType.STR));
            }
            return editor;
        }
        
        @Override
        public Object getCellEditorValue() {
            return editor.getValue() != null ? editor.getValue().toString() : null;
        }
        
    }
    
    private class TitleCellEditor extends AbstractCellEditor implements TableCellEditor/*, TunedCellEditor*/ {
        
        private ExtendableTextField editor = new ExtendableTextField(true);
        private ActionListener btnListener;
        private JButton btn;
        
        public TitleCellEditor() {
            btn = editor.addButton();
            btn.setIcon(RadixWareIcons.DIALOG.CHOOSE.getIcon(13, 13));
            btnListener = new ActionListener() {
                
                @Override
                public void actionPerformed(ActionEvent e) {
                    int row = itemsTable.getSelectedRow();
                    final EditMaskList.Item item = model.getItemAt(row);
                    if (item != null) {
                        TitleModalEditor.Factory.newInstance(definition, stringDefForItem.get(item), new TitleModalEditor.TitleProvider() {
                            
                            @Override
                            public Id getTitleId() {
                                return item.getTitleId();
                            }
                            
                            @Override
                            public void setTitleId(Id id) {
                                item.setTitleId(id);
                            }
                            
                            @Override
                            public String getTitle() {
                                return item.getTitle();
                            }
                            
                            @Override
                            public void setTitle(String title) {
                                item.setTitle(title);
                            }
                        }).open();
                        Object val = model.getValueAt(row, 1);
                        editor.setValue(val != null ? val.toString() : "");
                    }
                }
            };
            btn.addActionListener(btnListener);
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int r, int c) {
            editor.setValue(value != null ? value.toString() : "");
            return editor;
        }
        
        @Override
        public Object getCellEditorValue() {
            return null;
        }

//        @Override
//        public void editingPerformed(ActionEvent e) {
//            btnListener.actionPerformed(new ActionEvent(btn, e.getID(), e.getActionCommand(), e.getWhen(), e.getModifiers()));
//        }
    }
    
    private class ItemsTableModel extends AbstractTableModel {
        
        private final String[] columns = {
            NbBundle.getBundle(ListEditor.class).getString("VALUE"),
            NbBundle.getBundle(ListEditor.class).getString("TITLE")
        };
        
        private ArrayList<EditMaskList.Item> items = new ArrayList<EditMaskList.Item>();
        
        public ItemsTableModel(List<EditMaskList.Item> list) {
            for (EditMaskList.Item item : list) {
                EditMaskList.Item newItem = EditMaskList.Item.Factory.newInstance(item);
                items.add(newItem);
                AdsMultilingualStringDef copy;
                if (newItem.getTitleId() != null) {
                    AdsMultilingualStringDef stringDef = definition.findLocalizedString(newItem.getTitleId());
                    if (stringDef == null) {
                        copy = AdsMultilingualStringDef.Factory.newInstance();
                        newItem.setTitleId(null);
                    } else {
                        copy = AdsMultilingualStringDef.Factory.newInstance(stringDef);
                        newItem.setTitleId(copy.getId());
                    }
                } else {
                    copy = AdsMultilingualStringDef.Factory.newInstance();
                }
                stringDefForItem.put(newItem, copy);
            }
        }
        
        @Override
        public int getRowCount() {
            return items.size();
        }
        
        @Override
        public int getColumnCount() {
            return columns.length;
        }
        
        @Override
        public String getColumnName(int columnIndex) {
            return columns[columnIndex];
        }
        
        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return String.class;
        }
        
        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return !readOnly;
        }
        
        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return items.get(rowIndex).getValue();
                case 1:
                    if (items.get(rowIndex).getTitleId() != null) {
                        return stringDefForItem.get(items.get(rowIndex)).getValue(EIsoLanguage.ENGLISH);
                    } else {
                        return items.get(rowIndex).getTitle();
                    }
                default:
                    return null;
            }
        }
        
        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (rowIndex < 0 || rowIndex >= getRowCount() || columnIndex < 0 || columnIndex >= getColumnCount()) {
                return;
            }
            if (columnIndex == 0) {
                items.get(rowIndex).setValue(aValue != null ? aValue.toString() : null);
            }
            fireTableCellUpdated(rowIndex, columnIndex);
        }
        
        public void raiseRow(int row) {
            if (row < 1 || row >= getRowCount()) {
                return;
            }
            EditMaskList.Item cur = items.get(row);
            EditMaskList.Item up = items.get(row - 1);
            items.set(row, up);
            items.set(row - 1, cur);
            fireTableRowsUpdated(row - 1, row);
        }
        
        public void lowerRow(int row) {
            if (row < 0 || row >= getRowCount() - 1) {
                return;
            }
            EditMaskList.Item cur = items.get(row);
            EditMaskList.Item down = items.get(row + 1);
            items.set(row, down);
            items.set(row + 1, cur);
            fireTableRowsUpdated(row, row + 1);
        }
        
        public void addRow() {
            EditMaskList.Item newItem = EditMaskList.Item.Factory.newInstance();
            switch (editMaskType) {
                case INT:
                case NUM:
                    newItem.setValue("0");
                    break;
                default:
                    newItem.setValue("");
            }
            items.add(newItem);
            AdsMultilingualStringDef copy = AdsMultilingualStringDef.Factory.newInstance();
            stringDefForItem.put(newItem, copy);
            fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
        }
        
        public void deleteRow(int row) {
            if (row < 0 || row >= getRowCount()) {
                return;
            }
            EditMaskList.Item item = getItemAt(row);
            stringDefForItem.remove(item);
            items.remove(row);
            fireTableRowsDeleted(row, row);
        }
        
        public void clear() {
            items.clear();
            stringDefForItem.clear();
            fireTableDataChanged();
        }
        
        public List<EditMaskList.Item> getItems() {
            return items;
        }
        
        public EditMaskList.Item getItemAt(int row) {
            if (row < 0 || row >= getRowCount()) {
                return null;
            }
            return items.get(row);
        }
        
    }
    
    private class ItemsTableCellRenderer extends DefaultTableCellRenderer {
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            String str = value != null ? value.toString() : "NULL";
            JComponent comp = (JComponent) super.getTableCellRendererComponent(table, str, isSelected, hasFocus, row, column);
            if (readOnly) {
                comp.setForeground(Color.DARK_GRAY);
            }
            return comp;
        }
    }
    
    private final EEditMaskType editMaskType;
    private final ItemsTableModel model;
    private final ValueCellEditor valueEditor;
    private final TitleCellEditor titleEditor;
    private final AdsDefinition definition;
    private final Map<EditMaskList.Item, AdsMultilingualStringDef> stringDefForItem = new HashMap<EditMaskList.Item, AdsMultilingualStringDef>();
    private boolean readOnly = false;

    /**
     * Creates new form ListEditor
     */
    public ListEditor(AdsDefinition definition, EditMaskList editMaskList, EEditMaskType editMaskType) {
        this.definition = definition;
        this.editMaskType = editMaskType;
        initComponents();
        
        addButton.setIcon(RadixWareIcons.CREATE.ADD.getIcon());
        removeButton.setIcon(RadixWareIcons.DELETE.DELETE.getIcon());
        upButton.setIcon(RadixWareIcons.ARROW.MOVE_UP.getIcon());
        downButton.setIcon(RadixWareIcons.ARROW.MOVE_DOWN.getIcon());
        
        if (editMaskList != null) {
            model = new ItemsTableModel(editMaskList.getItems());
        } else {
            model = new ItemsTableModel(Collections.EMPTY_LIST);
        }
        valueEditor = new ValueCellEditor();
        titleEditor = new TitleCellEditor();
        
        initTable();
        itemsTable.clearSelection();
        
        initButton(addButton, KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, 0), "INSERT");
        initButton(removeButton, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "DELETE");
        initButton(upButton, KeyStroke.getKeyStroke(KeyEvent.VK_UP, KeyEvent.CTRL_DOWN_MASK), "UP");
        initButton(downButton, KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, KeyEvent.CTRL_DOWN_MASK), "DOWN");
        
        SwingUtilities.invokeLater(new Runnable() {
            
            @Override
            public void run() {
                updateButtonsEnableState();
            }
        });
        
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
        InputMap inputMap = itemsTable.getInputMap(JTable.WHEN_FOCUSED);
        inputMap.put(keyStroke, key);
        itemsTable.getActionMap().put(key, action);
    }
    
    private void initTable() {
        itemsTable.setModel(model);
        itemsTable.setDefaultRenderer(String.class, new ItemsTableCellRenderer());
//        itemsTable.setDefaultEditor(String.class, valueEditor);
        itemsTable.getColumnModel().getColumn(0).setCellEditor(valueEditor);
        itemsTable.getColumnModel().getColumn(1).setCellEditor(titleEditor);
        itemsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemsTable.setCellSelectionEnabled(true);
        itemsTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        itemsTable.setRowHeight(24);
        itemsTable.getTableHeader().setReorderingAllowed(false);
        itemsTable.getColumnModel().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            
            @Override
            public void valueChanged(ListSelectionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    
                    @Override
                    public void run() {
                        updateButtonsEnableState();
                    }
                });
            }
        });
        itemsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            
            @Override
            public void valueChanged(ListSelectionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    
                    @Override
                    public void run() {
                        updateButtonsEnableState();
                    }
                });
            }
        });
    }
    
    private void updateButtonsEnableState() {
        int row = itemsTable.getSelectedRow();
        addButton.setEnabled(!readOnly);
        removeButton.setEnabled(!readOnly && row != -1);
//        clearButton.setEnabled(!readOnly && itemsTable.getRowCount() > 0);
        upButton.setEnabled(!readOnly && row > 0);
        downButton.setEnabled(!readOnly && row < itemsTable.getRowCount() - 1);
    }
    
    @Override
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        updateButtonsEnableState();
    }
    
    @Override
    public void stopEditing() {
        valueEditor.stopCellEditing();
        titleEditor.stopCellEditing();
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
        itemsTable = new org.radixware.kernel.designer.common.dialogs.components.TunedTable();
        addButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();
        upButton = new javax.swing.JButton();
        downButton = new javax.swing.JButton();

        itemsTable.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(itemsTable);

        addButton.setText(org.openide.util.NbBundle.getMessage(ListEditor.class, "ListEditor.addButton.text")); // NOI18N
        addButton.setToolTipText(org.openide.util.NbBundle.getMessage(ListEditor.class, "ListEditor.addButton.toolTipText")); // NOI18N
        addButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        removeButton.setText(org.openide.util.NbBundle.getMessage(ListEditor.class, "ListEditor.removeButton.text")); // NOI18N
        removeButton.setToolTipText(org.openide.util.NbBundle.getMessage(ListEditor.class, "ListEditor.removeButton.toolTipText")); // NOI18N
        removeButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });

        upButton.setText(org.openide.util.NbBundle.getMessage(ListEditor.class, "ListEditor.upButton.text")); // NOI18N
        upButton.setToolTipText(org.openide.util.NbBundle.getMessage(ListEditor.class, "ListEditor.upButton.toolTipText")); // NOI18N
        upButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        upButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upButtonActionPerformed(evt);
            }
        });

        downButton.setText(org.openide.util.NbBundle.getMessage(ListEditor.class, "ListEditor.downButton.text")); // NOI18N
        downButton.setToolTipText(org.openide.util.NbBundle.getMessage(ListEditor.class, "ListEditor.downButton.toolTipText")); // NOI18N
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(addButton)
                    .addComponent(removeButton)
                    .addComponent(upButton)
                    .addComponent(downButton))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(addButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(upButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(downButton)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        stopEditing();
        model.addRow();
        itemsTable.setRowSelectionInterval(model.getRowCount() - 1, model.getRowCount() - 1);
        itemsTable.setColumnSelectionInterval(0, 0);
}//GEN-LAST:event_addButtonActionPerformed

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed
        stopEditing();
//        if (!DialogUtils.messageConfirmation(NbBundle.getBundle(InitialValuesPanel.class).getString("REMOVE_ROW_CONFIRM")))
//            return;
        int row = itemsTable.getSelectedRow();
        model.deleteRow(row);
        if (row < model.getRowCount()) {
            itemsTable.setRowSelectionInterval(row, row);
        } else if (row > 0) {
            itemsTable.setRowSelectionInterval(row - 1, row - 1);
        } else {
            itemsTable.clearSelection();
        }
        updateButtonsEnableState();
}//GEN-LAST:event_removeButtonActionPerformed

    private void upButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upButtonActionPerformed
        stopEditing();
        int row = itemsTable.getSelectedRow();
        model.raiseRow(row);
        if (row > 0) {
            itemsTable.setRowSelectionInterval(row - 1, row - 1);
        }
}//GEN-LAST:event_upButtonActionPerformed

    private void downButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downButtonActionPerformed
        stopEditing();
        int row = itemsTable.getSelectedRow();
        model.lowerRow(row);
        if (row < itemsTable.getRowCount() - 1) {
            itemsTable.setRowSelectionInterval(row + 1, row + 1);
        }
}//GEN-LAST:event_downButtonActionPerformed
    
    @Override
    public void apply(EditMask editMask) {
        if (!(editMask instanceof EditMaskList)) {
            revert();
            return;
        }
        EditMaskList editMaskList = (EditMaskList) editMask;
        
        for (EditMaskList.Item item : editMaskList.getItems()) {
            if (item.getTitleId() != null) {
                AdsMultilingualStringDef stringDef = definition.findLocalizedString(item.getTitleId());
                if (stringDef != null) {
                    definition.findExistingLocalizingBundle().getStrings().getLocal().remove(stringDef);
                }
            }
        }
        editMaskList.clearItems();
        for (EditMaskList.Item item : model.getItems()) {
            editMaskList.addItem(item);
            if (item.getTitleId() != null) {
                definition.findExistingLocalizingBundle().getStrings().getLocal().add(stringDefForItem.get(item));
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton downButton;
    private org.radixware.kernel.designer.common.dialogs.components.TunedTable itemsTable;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton removeButton;
    private javax.swing.JButton upButton;
    // End of variables declaration//GEN-END:variables

}
