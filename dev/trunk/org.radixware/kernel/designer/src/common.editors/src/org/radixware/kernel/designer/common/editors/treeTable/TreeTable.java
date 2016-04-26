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

package org.radixware.kernel.designer.common.editors.treeTable;

import java.awt.Color;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.table.*;

import java.awt.Dimension;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;

import java.awt.event.MouseEvent;

import java.util.EventObject;
import org.radixware.kernel.designer.common.dialogs.components.SimpleTable;

public class TreeTable extends SimpleTable {

    /**
     * A subclass of JTree.
     */
    protected TreeTableCellRenderer tree;
    boolean isRootVisible = true;

    public void expandAll() {
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
    }

    public boolean isRootVisible() {
        return isRootVisible;
    }

    public void setRootVisible(boolean flag) {
        tree.setRootVisible(isRootVisible = flag);

    }
    Color gridColorEx = null;

    public void afterOpen(TreeGridModel treeTableModel, Color gridColor) {
        gridColorEx = gridColor;

        // Create the tree. It will be used as a renderer and editor.
        tree = new TreeTableCellRenderer(treeTableModel);
        tree.setRootVisible(isRootVisible());


        // Install a tableModel representing the visible rows in the tree.
        TreeTableModelAdapter treeTableModelAdapter = new TreeTableModelAdapter(treeTableModel, tree);
        //treeTableModelAdapter.e

        //this.setModel(new TreeTableModelAdapter(treeTableModel, tree));

        // Force the JTable and JTree to share their row selection models.
        ListToTreeSelectionModelWrapper selectionWrapper = new ListToTreeSelectionModelWrapper();
        tree.setSelectionModel(selectionWrapper);
        setSelectionModel(selectionWrapper.getListSelectionModel());

        // Install the tree editor renderer and editor.
        setDefaultRenderer(TreeTableModel.class, tree);
        setDefaultEditor(TreeTableModel.class, new TreeTableCellEditor());

        // No grid.
        setShowGrid(false);

        // No intercell spacing
        setIntercellSpacing(new Dimension(0, 0));

        tree.setRowHeight(getRowHeight());

        setModel(treeTableModelAdapter);
    }

    @Override
    public TableCellEditor getCellEditor(int row, int column) {
        return super.getCellEditor(row, column);
    }

    public TreeTable(TreeGridModel treeTableModel) {
        super();
        afterOpen(treeTableModel, Color.white);
    }

    @Override
    public void updateUI() {
        super.updateUI();
        if (tree != null) {
            tree.updateUI();
        }
        // Use the tree's default foreground and background colors in the
        // table.
//        LookAndFeel.installColorsAndFont(this, "Tree.background",
//                "Tree.foreground", "Tree.font");
    }

    @Override
    public int getEditingRow() {
        return (getColumnClass(editingColumn) == TreeTableModel.class) ? -1
                : editingRow;
    }

    @Override
    public void setRowHeight(int rowHeight) {
        super.setRowHeight(rowHeight);
        if (tree != null && tree.getRowHeight() != rowHeight) {
            tree.setRowHeight(getRowHeight());
        }
    }

    @Override
    public int getRowHeight(int row) {
        return super.getRowHeight(row);
    }

    public int getRowForLocation(int y) {
        int row = y / (this.getRowHeight());
        if (row >= getRowCount()) {
            return -1;
        }
        return row;
    }

    public Object getObjectForLocation(int y) {
        int row = getRowForLocation(y);
        return row != -1 ? this.getValueAt(row, 0) : null;
    }

    /**
     * A TreeCellRenderer that displays a JTree.
     */
    private class TreeCellRendererEx extends DefaultTreeCellRenderer {

        TreeCellRendererEx() {
        }

        @Override
        public Component getTreeCellRendererComponent(JTree tree,
                Object value, boolean sel, boolean expanded,
                boolean leaf, int row, boolean hasFocus) {
            final Component res = super.getTreeCellRendererComponent(tree, value.toString(), sel, expanded, leaf, row, hasFocus);
            if (value instanceof TreeGridModel.TreeGridNode) {
                final TreeGridModel.TreeGridNode node = (TreeGridModel.TreeGridNode) value;
                setIcon(node.gridItem.getIcon(row));
                final Font font = node.gridItem.getFont(row), proto = getFont();

                if (font != null) {
                    this.setFont(font.deriveFont(proto.getSize2D()));
                }
                if (!sel) {
                    setForeground(node.gridItem.getForeground(row));
                }
            }
            return res;
        }
    }

    public class TreeTableCellRenderer extends JTree implements
            TableCellRenderer {

        protected int visibleRow;

        public TreeTableCellRenderer(TreeModel model) {
            super(model);

            final TreeCellRendererEx renderer = new TreeCellRendererEx();
            setCellRenderer(renderer);
            updateUI();
        }

        /**
         * updateUI is overridden to set the colors of the Tree's renderer to
         * match that of the table.
         */
        @Override
        public final void updateUI() {
            super.updateUI();
            // Make the tree's cell renderer use the table's cell selection
            // colors.
            TreeCellRenderer tcr = getCellRenderer();//NOPMD
            if (tcr instanceof DefaultTreeCellRenderer) {
                final DefaultTreeCellRenderer dtcr = ((DefaultTreeCellRenderer) tcr);
                // For 1.1 uncomment this, 1.2 has a bug that will cause an
                // exception to be thrown if the border selection color is
                // null.
                // dtcr.setBorderSelectionColor(null);
                dtcr.setTextSelectionColor(UIManager.getColor("Table.selectionForeground"));
                dtcr.setBackgroundSelectionColor(UIManager.getColor("Table.selectionBackground"));

                dtcr.setBackgroundNonSelectionColor(gridColorEx);

            }
        }

        /**
         * Sets the row height of the tree, and forwards the row height to the
         * table.
         */
        @Override
        public void setRowHeight(int rowHeight) {
            super.setRowHeight(rowHeight);
        }

        /**
         * This is overridden to set the height to match that of the JTable.
         */
        @Override
        public void setBounds(int x, int y, int w, int h) {
            super.setBounds(x, 0, w, TreeTable.this.getHeight());
        }

        /**
         * Sublcassed to translate the graphics such that the last visible row
         * will be drawn at 0,0.
         */
        @Override
        public void paint(Graphics g) {
            g.translate(0, -visibleRow * getRowHeight());

            super.paint(g);
        }

        /**
         * TreeCellRenderer method. Overridden to update the visible row.
         */
        @Override
        public Component getTableCellRendererComponent(JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row, int column) {

            visibleRow = row;

            if (!isSelected) {
                super.setBackground(gridColorEx);
            } else {
                setBackground(table.getSelectionBackground());
                // super.setForeground(Color.WHITE);
            }
            return this;
        }
    }

    /**
     * TreeTableCellEditor implementation. Component returned is the JTree.
     */
    public class TreeTableCellEditor extends AbstractCellEditor implements
            TableCellEditor {

        @Override
        public Component getTableCellEditorComponent(JTable table,
                Object value,
                boolean isSelected,
                int r, int c) {
            return tree;
        }

        @Override
        public boolean isCellEditable(EventObject e) {
            if (e instanceof MouseEvent) {
                for (int counter = getColumnCount() - 1; counter >= 0;
                        counter--) {
                    if (getColumnClass(counter) == TreeTableModel.class) {
                        MouseEvent me = (MouseEvent) e;
//                        MouseEvent newME = new MouseEvent(tree, me.getID(),
//                                me.getWhen(), me.getModifiers(),
//                                me.getX() - getCellRect(0, counter, true).x,
//                                me.getY(), me.getClickCount(),
//                                me.isPopupTrigger());
                        //tree.dispatchEvent(newME);
                        tree.dispatchEvent(me);
                        break;
                    }
                }
            }
            return false;
        }
    }

    class ListToTreeSelectionModelWrapper extends DefaultTreeSelectionModel {

        /**
         * Set to true when we are updating the ListSelectionModel.
         */
        protected boolean updatingListSelectionModel;

        public ListToTreeSelectionModelWrapper() {
            super();
            getListSelectionModel().addListSelectionListener(createListSelectionListener());
        }

        /**
         * Returns the list selection model. ListToTreeSelectionModelWrapper
         * listens for changes to this model and updates the selected paths
         * accordingly.
         */
        ListSelectionModel getListSelectionModel() {
            return listSelectionModel;
        }

        /**
         * This is overridden to set
         * <code>updatingListSelectionModel</code> and message super. This is
         * the only place DefaultTreeSelectionModel alters the
         * ListSelectionModel.
         */
        @Override
        public void resetRowSelection() {
            if (!updatingListSelectionModel) {
                updatingListSelectionModel = true;
                try {
                    super.resetRowSelection();
                } finally {
                    updatingListSelectionModel = false;
                }
            }
            // Notice how we don't message super if
            // updatingListSelectionModel is true. If
            // updatingListSelectionModel is true, it implies the
            // ListSelectionModel has already been updated and the
            // paths are the only thing that needs to be updated.
        }

        /**
         * Creates and returns an instance of ListSelectionHandler.
         */
        protected final ListSelectionListener createListSelectionListener() {
            return new ListSelectionHandler();
        }

        public void updateSelectedPathsFromSelectedRows() {
            if (!updatingListSelectionModel) {
                updatingListSelectionModel = true;
                try {
                    // This is way expensive, ListSelectionModel needs an
                    // enumerator for iterating.
                    int min = listSelectionModel.getMinSelectionIndex();
                    int max = listSelectionModel.getMaxSelectionIndex();

                    clearSelection();
                    if (min != -1 && max != -1) {
                        for (int counter = min; counter <= max; counter++) {
                            if (listSelectionModel.isSelectedIndex(counter)) {
                                TreePath selPath = tree.getPathForRow(counter);

                                if (selPath != null) {
                                    addSelectionPath(selPath);
                                }
                            }
                        }
                    }
                } finally {
                    updatingListSelectionModel = false;
                }
            }
        }

        /**
         * Class responsible for calling updateSelectedPathsFromSelectedRows
         * when the selection of the list changse.
         */
        class ListSelectionHandler implements ListSelectionListener {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                updateSelectedPathsFromSelectedRows();
            }
        }

    }

}
