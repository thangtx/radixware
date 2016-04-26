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

package org.radixware.kernel.designer.common.dialogs.chooseobject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import org.radixware.kernel.common.components.ExtendableTextField;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.common.utils.events.RadixEventSource;
import org.radixware.kernel.designer.common.general.editors.EditorsManager;
import org.radixware.kernel.common.resources.RadixWareIcons;


public class ConfigureDefinitionTable extends JTable {

    public class DefBooleanValueChangeEvent extends RadixEvent {

        private RadixObject radixObject;
        private Boolean oldValue,  newValue;

        public DefBooleanValueChangeEvent(RadixObject radixObject, Boolean oldValue, Boolean newValue) {
            this.radixObject = radixObject;
            this.oldValue = oldValue;
            this.newValue = newValue;
        }

        public RadixObject getRadixObject() {
            return radixObject;
        }

        public Boolean getOldValue() {
            return oldValue;
        }

        public Boolean getNewValue() {
            return newValue;
        }
    }

    public class IdBooleanValueChangeEvent extends RadixEvent {

        private Id id;
        private Boolean oldValue,  newValue;

        public IdBooleanValueChangeEvent(Id id, Boolean oldValue, Boolean newValue) {
            this.id = id;
            this.oldValue = oldValue;
            this.newValue = newValue;
        }

        public Id getId() {
            return id;
        }

        public Boolean getOldValue() {
            return oldValue;
        }

        public Boolean getNewValue() {
            return newValue;
        }
    }

    public interface DefBooleanValueChangeListener extends IRadixEventListener<DefBooleanValueChangeEvent> {
    }

    public class DefBooleanValueChangeSupport extends RadixEventSource<DefBooleanValueChangeListener, DefBooleanValueChangeEvent> {
    }

    public interface IdBooleanValueChangeListener extends IRadixEventListener<IdBooleanValueChangeEvent> {
    }

    public class IdBooleanValueChangeSupport extends RadixEventSource<IdBooleanValueChangeListener, IdBooleanValueChangeEvent> {
    }

    private class DefinitionCellEditor extends AbstractCellEditor implements TableCellEditor/*, TunedCellEditor*/ {

        private ExtendableTextField editor = new ExtendableTextField(true);
        private ActionListener btnListener;
        private JButton btn;

        public DefinitionCellEditor() {
            btn = editor.addButton();
            btn.setIcon(RadixWareIcons.DIALOG.CHOOSE.getIcon(13, 13));
            btnListener = new ActionListener(){

                @Override
                public void actionPerformed(ActionEvent e) {
                    int row = ConfigureDefinitionTable.this.getSelectedRow();
                    Definition def = model.getDefinitionAt(row);
                    if (def != null) {
                        EditorsManager.getDefault().open(def);
                    }
                }
            };
            btn.addActionListener(btnListener);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int r, int c) {
            editor.setValue(value != null && value instanceof JLabel ? ((JLabel)value).getText() : "");
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

    private class ConfigureDefinitionTableModel extends AbstractTableModel {

        private List<Id> configurableDefinitionIds = null;
        private List<Id> unresolvedIds = new ArrayList<Id>();
        private Collection<Id> disabledDefinitionIds = null;

        private List<? extends Definition> availableDefinitions = null;
        
        private DefBooleanValueChangeSupport defBooleanValueChangeSupport = new DefBooleanValueChangeSupport();
        private IdBooleanValueChangeSupport idBooleanValueChangeSupport = new IdBooleanValueChangeSupport();

        public ConfigureDefinitionTableModel() {
        }

        public void setData(List<Id> configurableDefinitionIds, List<? extends Definition> availableDefinitions, Collection<Id> disabledDefinitionIds) {
            this.configurableDefinitionIds = configurableDefinitionIds;
            this.availableDefinitions = availableDefinitions;
            this.disabledDefinitionIds = disabledDefinitionIds;

            Collection<Id> avalableIds = new HashSet<Id>();
            for (Definition definition : availableDefinitions) {
                avalableIds.add(definition.getId());
            }

            unresolvedIds.clear();
            for (Id id : configurableDefinitionIds) {
                if (!avalableIds.contains(id)) {
                    unresolvedIds.add(id);
                }
            }

            fireTableDataChanged();
        }

        @Override
        public int getRowCount() {
            return availableDefinitions != null ? availableDefinitions.size() + unresolvedIds.size() : 0;
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public String getColumnName(int columnIndex) {
            return null;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return Boolean.class;
                case 1:
                    return JLabel.class;
                default:
                    return Object.class;
            }
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            if (columnIndex == 1 && rowIndex < availableDefinitions.size())
                return true;
            return false;
        }

        public boolean isRowEditable(int row) {
            if (!enabled || availableDefinitions == null)
                return false;
            Id defId;
            if (row < availableDefinitions.size()) {
                defId = getDefinitionAt(row).getId();
            } else {
                defId = getIdAt(row - availableDefinitions.size());
            }
            return !disabledDefinitionIds.contains(defId);
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (rowIndex < availableDefinitions.size()) {
                Definition def = getDefinitionAt(rowIndex);
                if (def == null)
                    return null;
                switch (columnIndex) {
                    case 0:
                        return new Boolean(isSelected(def.getId()));
                    case 1:
                        JLabel label = new JLabel(def.getName());
                        if (!isRowEditable(rowIndex))
                            label.setEnabled(false);
//                            label.setForeground(Color.GRAY);
                        return label;
                    default:
                        return null;
                }
            } else {
                Id id = getIdAt(rowIndex - availableDefinitions.size());
                if (id == null)
                    return null;
                switch (columnIndex) {
                    case 0:
                        return new Boolean(isSelected(id));
                    case 1:
                        JLabel label = new JLabel(id.toString());
                        if (!isRowEditable(rowIndex))
                            label.setForeground(new Color(128, 0, 0));
                        else
                            label.setForeground(Color.RED);
                        return label;
                    default:
                        return null;
                }
            }
        }

        private boolean isSelected(Id defId) {
            for (Id id : configurableDefinitionIds) {
                if (Utils.equals(id, defId)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (columnIndex != 0 || !isRowEditable(rowIndex))
                return;
            if (rowIndex < availableDefinitions.size()) {
                Definition def = getDefinitionAt(rowIndex);
                if (((Boolean) aValue).booleanValue()) {
                    configurableDefinitionIds.add(def.getId());
                    fireConfigurableDefinitionIsCheckedChanged(def, Boolean.FALSE, Boolean.TRUE);
                    fireConfigurableIdIsCheckedChanged(def.getId(), Boolean.FALSE, Boolean.TRUE);
                } else {
                    configurableDefinitionIds.remove(def.getId());
                    fireConfigurableDefinitionIsCheckedChanged(def, Boolean.TRUE, Boolean.FALSE);
                    fireConfigurableIdIsCheckedChanged(def.getId(), Boolean.TRUE, Boolean.FALSE);
                }
            } else {
                Id id = getIdAt(rowIndex - availableDefinitions.size());
//                Definition def = getDefinitionAt(rowIndex - availableDefinitions.size());
                if (((Boolean) aValue).booleanValue()) {
                    configurableDefinitionIds.add(id);
                    fireConfigurableIdIsCheckedChanged(id, Boolean.FALSE, Boolean.TRUE);
                } else {
                    configurableDefinitionIds.remove(id);
                    fireConfigurableIdIsCheckedChanged(id, Boolean.TRUE, Boolean.FALSE);
                }
            }
            fireTableRowsUpdated(rowIndex, rowIndex);
        }

        public void addConfigurableDefinitionIsCheckedkListener(DefBooleanValueChangeListener l) {
            defBooleanValueChangeSupport.addEventListener(l);            
        }

        public void removeConfigurableDefinitionIsCheckedkListener(DefBooleanValueChangeListener l) {
            defBooleanValueChangeSupport.removeEventListener(l);
        }

        public void addConfigurableIdIsCheckedkListener(IdBooleanValueChangeListener l) {
            idBooleanValueChangeSupport.addEventListener(l);
        }

        public void removeConfigurableIdIsCheckedkListener(IdBooleanValueChangeListener l) {
            idBooleanValueChangeSupport.removeEventListener(l);
        }

        public Definition getDefinitionAt(int row) {
            if (row >= availableDefinitions.size() || row < 0)
                return null;
            return availableDefinitions.get(row);
        }

        public Id getIdAt(int row) {
            if (row >= unresolvedIds.size() || row < 0)
                return null;
            return unresolvedIds.get(row);
        }

        private void fireConfigurableDefinitionIsCheckedChanged(RadixObject radixObject, Boolean oldBooleanValue, Boolean newBooleanValue) {
            defBooleanValueChangeSupport.fireEvent(new DefBooleanValueChangeEvent(radixObject, oldBooleanValue, newBooleanValue));
        }

        private void fireConfigurableIdIsCheckedChanged(Id id, Boolean oldBooleanValue, Boolean newBooleanValue) {
            idBooleanValueChangeSupport.fireEvent(new IdBooleanValueChangeEvent(id, oldBooleanValue, newBooleanValue));
        }
        
    }

    private class ConfigureDefinitionTableCellRenderer extends DefaultTableCellRenderer {

        private final TableCellRenderer booleanRenderer;

        public ConfigureDefinitionTableCellRenderer() {
            booleanRenderer = ConfigureDefinitionTable.this.getDefaultRenderer(Boolean.class);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (hasFocus) {
                focusedRow = row;
                focusedCol = column;
            }
            isSelected = row == table.getSelectedRow();
            JComponent pattern = (JComponent) super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column);
            if (value instanceof JLabel) {
                JLabel label = (JLabel) value;
                label.setOpaque(true);
                label.setBackground(pattern.getBackground());
                if (hasFocus)
                    label.setBorder(pattern.getBorder());
                return label;
            }
            if (value == null) {
                JLabel label = new JLabel("NULL");
                label.setOpaque(true);
                label.setBackground(pattern.getBackground());
                if (hasFocus)
                    label.setBorder(pattern.getBorder());
                return label;
            }
            JComponent comp = (JComponent) booleanRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (!model.isRowEditable(row))
                comp.setEnabled(false);
            else
                comp.setEnabled(true);
            return comp;
//            comp.setOpaque(true);
//            comp.setBackground(pattern.getBackground());
//            JPanel panel = new JPanel(new BorderLayout());
//            panel.add(comp, BorderLayout.CENTER);
//            if (hasFocus && pattern.getBorder() != null) {
//                try {
//                    panel.setBorder(pattern.getBorder());
//                } catch (Exception e) {
//                }
//            }
//            return panel;
        }
    }

    private final ConfigureDefinitionTableModel model = new ConfigureDefinitionTableModel();
    private final DefinitionCellEditor editor = new DefinitionCellEditor();
    private boolean enabled = true;
    private int focusedRow = -1;
    private int focusedCol = -1;

    public ConfigureDefinitionTable() {
        super();
        this.setModel(model);
        ConfigureDefinitionTableCellRenderer renderer = new ConfigureDefinitionTableCellRenderer();
        for (int i = 0; i < this.getColumnCount(); ++i)
            this.getColumnModel().getColumn(i).setCellRenderer(renderer);
        this.getColumnModel().getColumn(1).setCellEditor(editor);
//        this.setShowHorizontalLines(false);
//        this.setShowVerticalLines(false);
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.setRowSelectionAllowed(true);
        this.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        this.setRowHeight(24);
        this.setTableHeader(null);
        this.getColumnModel().getColumn(0).setMaxWidth(24);
        this.getColumnModel().getColumn(0).setMinWidth(24);
        this.getColumnModel().getColumn(0).setPreferredWidth(24);
        this.getColumnModel().getColumn(0).setResizable(false);

        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent arg0) {
                int col = ConfigureDefinitionTable.this.columnAtPoint(arg0.getPoint());
                if (col != 0)
                    return;
                int row = ConfigureDefinitionTable.this.rowAtPoint(arg0.getPoint());
                if (row < 0 || row >= model.getRowCount() || !model.isRowEditable(row))
                    return;
                Boolean bool = (Boolean) model.getValueAt(row, 0);
                model.setValueAt(new Boolean(!bool.booleanValue()), row, 0);
            }
        });

        ActionListener actionListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
//                int selectedRow = ConfigureDefinitionTable.this.getSelectedRow();
                if (focusedRow == -1 || focusedCol == -1)
                    return;
                if (focusedCol == 0) {
                    if (!model.isRowEditable(focusedRow))
                        return;
                    Boolean bool = (Boolean) model.getValueAt(focusedRow, 0);
                    model.setValueAt(new Boolean(!bool.booleanValue()), focusedRow, 0);
                } else if (focusedCol == 1) {
                    ConfigureDefinitionTable.this.editCellAt(focusedRow, 1);
                }
            }
        };
        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false);
        KeyStroke space = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false);
        this.registerKeyboardAction(actionListener, enter, JComponent.WHEN_FOCUSED);
        this.registerKeyboardAction(actionListener, space, JComponent.WHEN_FOCUSED);
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        model.fireTableDataChanged();
        super.setEnabled(enabled);
    }

    public void setData(List<Id> configurableDefinitionIds, List<? extends Definition> availableDefinitions, Collection<Id> disabledDefinitionIds) {
        model.setData(configurableDefinitionIds, availableDefinitions, disabledDefinitionIds);
    }

    public void addConfigurableDefinitionIsCheckedkListener(DefBooleanValueChangeListener l) {
        model.addConfigurableDefinitionIsCheckedkListener(l);
    }

    public void removeConfigurableDefinitionIsCheckedkListener(DefBooleanValueChangeListener l) {
        model.removeConfigurableDefinitionIsCheckedkListener(l);
    }

    public void addConfigurableIdIsCheckedkListener(IdBooleanValueChangeListener l) {
        model.addConfigurableIdIsCheckedkListener(l);
    }

    public void removeConfigurableIdIsCheckedkListener(IdBooleanValueChangeListener l) {
        model.removeConfigurableIdIsCheckedkListener(l);
    }

}
