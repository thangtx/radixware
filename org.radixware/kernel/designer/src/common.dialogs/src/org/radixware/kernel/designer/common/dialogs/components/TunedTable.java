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

package org.radixware.kernel.designer.common.dialogs.components;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.common.utils.events.RadixEventSource;


public class TunedTable extends javax.swing.JTable {

    private boolean canRepaint = true;

    public void setUpdatesEnabled(final boolean enabled) {
        if (enabled != canRepaint) {
            canRepaint = enabled;
            if (enabled) {
                repaint();
            }
        }
    }

    @Override
    public void repaint() {
        if (canRepaint) {
            super.repaint();
        }
    }

    @Override
    public void repaint(int x, int y, int width, int height) {
        if (canRepaint) {
            super.repaint(x, y, width, height);
        }
    }

    @Override
    public void repaint(long tm, int x, int y, int width, int height) {
        if (canRepaint) {
            super.repaint(tm, x, y, width, height);
        }
    }

    @Override
    public void repaint(Rectangle r) {
        if (canRepaint) {
            super.repaint(r);
        }
    }

    @Override
    public void repaint(long tm) {
        if (canRepaint) {
            super.repaint(tm);
        }
    }
    private static final String EDIT_ACTION = "startEditing";
    private static final String CANCEL_ACTION = "cancel";

    public TunedTable() {
        super();
        inputSetup();
    }

    public TunedTable(TableModel tm) {
        super(tm);
        inputSetup();
    }

    public TunedTable(TableModel dm, TableColumnModel cm) {
        super(dm, cm);
        inputSetup();
    }

    public TunedTable(TableModel dm, TableColumnModel cm, ListSelectionModel sm) {
        super(dm, cm, sm);
        inputSetup();
    }

    public TunedTable(int numRows, int numColumns) {
        super(numRows, numColumns);
        inputSetup();
    }

    public TunedTable(Vector rowData, Vector columnNames) {
        super(rowData, columnNames);
        inputSetup();
    }

    public TunedTable(final Object[][] rowData, final Object[] columnNames) {
        super(rowData, columnNames);
        inputSetup();
    }

    @Override
    public boolean editCellAt(int row, int column) {
        setFocusOnEditingCell(row, column);
        return super.editCellAt(row, column);
    }

    @Override
    public boolean editCellAt(int row, int column, EventObject e) {
        setFocusOnEditingCell(row, column);

        if (super.editCellAt(row, column, e)) {
            if (e instanceof KeyEvent && getEditorComponent() instanceof PropertyValueEditPanel) {
                ((PropertyValueEditPanel) getEditorComponent()).activateByKeyInput((KeyEvent) e);
            }
            return true;
        } else {
            return false;
        }

    }

    private void setFocusOnEditingCell(int row, int column) {
        if (column != -1 && column < TunedTable.this.getModel().getColumnCount()) {
            TunedTable.this.getColumnModel().getSelectionModel().setLeadSelectionIndex(column);
        }
        if (row != -1 && row < TunedTable.this.getModel().getRowCount()) {
            TunedTable.this.getSelectionModel().setLeadSelectionIndex(row);
        }
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        super.tableChanged(e);
        if (e.getType() != TableModelEvent.UPDATE
                && getCellEditor() != null) {
            getCellEditor().stopCellEditing();
        }

        if (e.getLastRow() - e.getFirstRow() <= getRowCount()) {
            scrollToVisible(e.getFirstRow(), 0);
        }
    }

    public void scrollToVisible(int rowIndex, int vColIndex) {
        if (!(getParent() instanceof JViewport)) {
            return;
        }
        JViewport viewport = (JViewport) getParent();

        Rectangle rect = getCellRect(rowIndex, vColIndex, true);
        Point pt = viewport.getViewPosition();
        rect.setLocation(rect.x - pt.x, rect.y - pt.y);
        viewport.scrollRectToVisible(rect);
    }

    private void inputSetup() {
        setRowHeight(getRowHeight() + 9);
        Set<KeyStroke> fowardKeys = new HashSet<KeyStroke>();
        fowardKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0));
        setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, fowardKeys);

        Set<KeyStroke> backwardKeys = new HashSet<KeyStroke>();
        backwardKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, KeyEvent.SHIFT_DOWN_MASK));
        setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, backwardKeys);

        final Action cancelAction = getActionMap().get(CANCEL_ACTION);
        getActionMap().put(CANCEL_ACTION, null);
        getActionMap().put(CANCEL_ACTION, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TableCellEditor editor = TunedTable.this.getCellEditor();
                if (editor != null) {
                    editor.cancelCellEditing();
                } else {
                    TunedTable.this.getEscapeKeySupport().fireEvent(new EscapePressedOnTableEvent());
                    Component dialogParent = isInDialog();
                    if (dialogParent != null) {
                        Dialog asDialog = (Dialog) dialogParent;
                        asDialog.setVisible(false);
                    }
                }

                cancelAction.actionPerformed(e);
            }
        });

        final Action editAction = getActionMap().get(EDIT_ACTION);
        getActionMap().put(EDIT_ACTION, null);
        getActionMap().put(EDIT_ACTION, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editAction.actionPerformed(e);
                TableCellEditor editor = TunedTable.this.getCellEditor();
                if (editor != null
                        && editor instanceof TunedCellEditor) {
                    ((TunedCellEditor) editor).editingPerformed(new ActionEvent(editor, e.getID(), e.getActionCommand(), e.getWhen(), e.getModifiers()));
                }
            }
        });

        InputMap input = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        KeyStroke enterPressed = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        input.put(enterPressed, null);
        input.put(enterPressed, EDIT_ACTION);

        KeyStroke altDown = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, KeyEvent.ALT_DOWN_MASK);
        input.put(altDown, null);
        input.put(altDown, EDIT_ACTION);

        putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
    }

    private Component isInDialog() {
        for (Component component = TunedTable.this.getParent(); component != null; component = component.getParent()) {
            if (component instanceof Dialog) {
                return component;
            }
        }
        return null;
    }
    private EscapeKeyOnTableSupport escapeKeySupport;

    public EscapeKeyOnTableSupport getEscapeKeySupport() {
        if (escapeKeySupport == null) {
            escapeKeySupport = new EscapeKeyOnTableSupport();
        }
        return escapeKeySupport;
    }

    public class EscapePressedOnTableEvent extends RadixEvent {
    }

    public interface EscapeKeyOnTableListener extends IRadixEventListener<EscapePressedOnTableEvent> {
    }

    public class EscapeKeyOnTableSupport extends RadixEventSource {
    }

    public interface TunedCellEditor {

        public abstract void editingPerformed(ActionEvent e);
    }

    public static class TunedComboCellEditor extends AbstractCellEditor
            implements TableCellEditor, TunedCellEditor {

        private JComboBox combo = new JComboBox();
        private ActionListener spaceListener;
        private JTable table;
        private boolean readonly = false;

        public TunedComboCellEditor(JTable table) {
            this.table = table;
            initialSetup();
        }

        public TunedComboCellEditor(JTable table, JComboBox combo) {
            this.combo = combo;
            this.table = table;
            initialSetup();
        }

        public boolean isReadonly() {
            return this.readonly;
        }

        public void setReadonly(boolean readonly) {
            this.readonly = readonly;
        }

        public JComponent getComponent() {
            return combo;
        }

        private void initialSetup() {
            String dwnCommand = combo.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).get(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0)).toString();
            String upCommand = combo.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).get(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0)).toString();
            final Action dwnAction = combo.getActionMap().get(dwnCommand);
            if (dwnAction != null) {
                combo.getActionMap().put(dwnCommand, null);
                combo.getActionMap().put(dwnCommand, new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (TunedComboCellEditor.this.combo.isPopupVisible()) {
                            dwnAction.actionPerformed(e);
                        } else {
                            TunedComboCellEditor.this.stopCellEditing();
                            if (table != null) {
                                table.getActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0)).actionPerformed(new ActionEvent(table, e.getID(), e.getActionCommand(), e.getWhen(), e.getModifiers()));
                            }
                        }
                    }
                });
            }
            final Action upAction = combo.getActionMap().get(upCommand);
            if (upAction != null) {
                combo.getActionMap().put(upCommand, null);
                combo.getActionMap().put(upCommand, new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (TunedComboCellEditor.this.combo.isPopupVisible()) {
                            upAction.actionPerformed(e);
                        } else {
                            TunedComboCellEditor.this.stopCellEditing();
                            if (table != null) {
                                table.getActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0)).actionPerformed(new ActionEvent(table, e.getID(), e.getActionCommand(), e.getWhen(), e.getModifiers()));
                            }
                        }
                    }
                });
            }

            spaceListener = combo.getActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, KeyEvent.ALT_DOWN_MASK));

            combo.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    stopCellEditing();
                }
            });
        }

        public void addComboActionListener(ActionListener listener) {
            combo.addActionListener(listener);
        }

        public void removeComboActionListener(ActionListener listener) {
            combo.removeActionListener(listener);
        }

        public void setComboValues(Set<Object> values) {
            combo.removeAllItems();
            for (Object v : values) {
                combo.addItem(v);
            }
        }

        public void setComboValues(Enumeration enums) {
            combo.removeAllItems();
            while (enums.hasMoreElements()) {
                combo.addItem(enums.nextElement());
            }
        }

        public void setComboValues(Object[] values) {
            combo.removeAllItems();
            for (Object v : values) {
                combo.addItem(v);
            }
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            combo.setSelectedItem(value);
            return combo;
        }

        @Override
        public void editingPerformed(ActionEvent e) {
            spaceListener.actionPerformed(new ActionEvent(combo, e.getID(), e.getActionCommand(), e.getWhen(), e.getModifiers()));
        }

        @Override
        public Object getCellEditorValue() {
            return combo.getSelectedItem();
        }

        @Override
        public boolean isCellEditable(EventObject e) {
            return !this.readonly;
        }
    }
}
