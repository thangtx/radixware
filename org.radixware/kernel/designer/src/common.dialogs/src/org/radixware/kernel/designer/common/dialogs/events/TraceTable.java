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

package org.radixware.kernel.designer.common.dialogs.events;

import java.awt.AWTKeyStroke;
import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.enums.EEventSeverity;


public class TraceTable extends JTable {

    private static final TraceTableModel traceTableModel = new TraceTableModel();

    public TraceTable() {
        super();
        this.setModel(traceTableModel);
        TraceTableCellRenderer renderer = new TraceTableCellRenderer();
        for (int i = 0; i < this.getColumnCount(); ++i)
            this.getColumnModel().getColumn(i).setCellRenderer(renderer);

        this.setShowHorizontalLines(false);
        this.setShowVerticalLines(false);
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.setRowSelectionAllowed(true);
        this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        this.setRowHeight(20);
        this.getColumnModel().getColumn(0).setPreferredWidth(20);
        this.getColumnModel().getColumn(0).setResizable(false);
        this.getColumnModel().getColumn(1).setPreferredWidth(32);
        this.getColumnModel().getColumn(2).setPreferredWidth(70);
        this.getColumnModel().getColumn(3).setPreferredWidth(100);
        this.getColumnModel().getColumn(4).setPreferredWidth(300);
        this.getColumnModel().getColumn(5).setPreferredWidth(1100);

        Set<AWTKeyStroke> forwardKeys = this.getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS);
        Set<AWTKeyStroke> newForwardKeys = new HashSet<AWTKeyStroke>(forwardKeys);
        newForwardKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0));
        this.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, newForwardKeys);

        Set<AWTKeyStroke> backwardKeys = this.getFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS);
        Set<AWTKeyStroke> newBackwardKeys = new HashSet<AWTKeyStroke>(backwardKeys);
        newBackwardKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, KeyEvent.SHIFT_DOWN_MASK));
        this.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, newBackwardKeys);

        this.getTableHeader().setToolTipText(NbBundle.getBundle(TraceTable.class).getString("HINT_SHOW_HIDE_COLUMNS"));
        ((DefaultTableCellRenderer)this.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        
        traceTableModel.addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        int rowCount = TraceTable.this.getRowCount();
                        int selRow = TraceTable.this.getSelectedRow();
                        if (rowCount > 0 && (selRow == rowCount - 2 || selRow == -1)) {
                            TraceTable.this.scrollToVisible(rowCount - 1, 0);
                            TraceTable.this.setRowSelectionInterval(rowCount - 1, rowCount - 1);
                        }
                    }
                });
            }
        });

        this.getTableHeader().addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    ColumnsVisibilityDialog.show(TraceTable.this);
                }
            }
        });

        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                    int row = TraceTable.this.rowAtPoint(e.getPoint());
                    if (row < 0 || row >= TraceTable.this.getRowCount())
                        return;
                    TraceItemViewerDialog.show(traceTableModel.getLogRecordAt(row));
                }
            }
        });
        
        ActionListener actionListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                TraceItemViewerDialog.show(traceTableModel.getLogRecordAt(TraceTable.this.getSelectedRow()));
            }
        };
        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false);
        this.registerKeyboardAction(actionListener, enter, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public static TraceTableModel getTraceTableModel() {
        return traceTableModel;
    }

    public void scrollToVisible(int rowIndex, int colIndex) {
        if (!(getParent() instanceof JViewport))
            return;
        JViewport viewport = (JViewport)getParent();
        Rectangle rect = getCellRect(rowIndex, colIndex, true);
        Point pt = viewport.getViewPosition();
        rect.setLocation(rect.x - pt.x, rect.y - pt.y);
        viewport.scrollRectToVisible(rect);
    }

    public void setStringFilter(String str) {
        traceTableModel.setStringFilter(str);
    }

    public void clear() {
        traceTableModel.clear();
    }

    public void setSeverityFilter(EEventSeverity severity) {
        traceTableModel.setSeverityFilter(severity);
    }

    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int rowIndex, int vColIndex) {
        Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);
        if (c instanceof JComponent) {
            JComponent jc = (JComponent) c;
            Object value = traceTableModel.getValueAt(rowIndex, vColIndex);
            if (value instanceof EEventSeverity) {
                EEventSeverity severity = (EEventSeverity)value;
                jc.setToolTipText(severity.getName());
            } else {
                jc.setToolTipText(null);
            }
        }
        return c;
    }

}
