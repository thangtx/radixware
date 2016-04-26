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

package org.radixware.kernel.designer.common.editors;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import org.openide.windows.WindowManager;
import org.radixware.kernel.designer.common.dialogs.components.TunedTable.TunedCellEditor;


public class MultiLinedPopupCellEditor extends AbstractCellEditor implements TableCellEditor, TunedCellEditor {

    private JTextArea invokingCellTextArea;
    private Dialog popupDialog;
    private boolean firstTime = true;
    private String strValue = "";
    private String previousStrValue = "";
    private int popupHeight;
    final private JTextField cellTextField;
    private int rowsCount;
    private final int heightOfRow;
    private AbstractTableModel model;
    private int row;
    private int col;
    private JTable table;
    private MouseAdapter areaCaller = new MouseAdapter() {

        @Override
        public void mousePressed(MouseEvent e) {
            invokingCellTextArea.setText(strValue);
            showPopup(true);
        }
    };

    public MultiLinedPopupCellEditor(final AbstractTableModel model) {
        this.model = model;

        cellTextField = new JTextField();
        cellTextField.setEditable(false);

        invokingCellTextArea = new JTextArea();
        //invokingCellTextArea.setWrapStyleWord(true);
        invokingCellTextArea.setLineWrap(true);
        //invokingCellTextArea.setAutoscrolls(true);

        rowsCount = 1;
        heightOfRow = invokingCellTextArea.getFontMetrics(invokingCellTextArea.getFont()).getHeight();

        invokingCellTextArea.addCaretListener(new CaretListener() {

            @Override
            public void caretUpdate(CaretEvent e) {
                strValue = invokingCellTextArea.getText();

                int newRowsCount = invokingCellTextArea.getLineCount();

                FontMetrics fm = invokingCellTextArea.getFontMetrics(invokingCellTextArea.getFont());
                int w = fm.stringWidth(strValue);
                int areaw = invokingCellTextArea.getPreferredSize().width > 0 ? invokingCellTextArea.getPreferredSize().width : 30;
                if (w > areaw) {
                    int ost = w % areaw;
                    if (ost > 0) {
                        newRowsCount = w / areaw + 1;
                    } else {
                        newRowsCount = w / areaw;
                    }
                }

                if (rowsCount != newRowsCount) {
                    rowsCount = newRowsCount;

                    if (cellTextField.isShowing()) {
                        popupHeight = rowsCount == 1 ? 10 + heightOfRow : rowsCount * heightOfRow;
                        showPopup(false);
                    } else {
                        popupHeight = rowsCount == 1 ? 10 + heightOfRow : rowsCount * heightOfRow;
                        invokingCellTextArea.updateUI();
                        popupDialog.repaint();
                    }
                }
            }
        });

        popupDialog = new Dialog(WindowManager.getDefault().getMainWindow());
        popupDialog.setUndecorated(true);

        popupDialog.addWindowListener(new WindowAdapter() {

            @Override
            public void windowDeactivated(WindowEvent e) {

                super.windowDeactivated(e);
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        cellTextField.setText(strValue);
                        popupDialog.setVisible(false);
                        fireEditingStopped();
                    }
                });
            }
        });

        invokingCellTextArea.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    popupDialog.setVisible(false);
                    MultiLinedPopupCellEditor.this.cancelCellEditing();
                }
            }
        });
        AbstractAction enterAction = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                popupDialog.setVisible(false);
               // MultiLinedPopupCellEditor.this.stopCellEditing();
            }
        };

        KeyStroke enterKey = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        invokingCellTextArea.getInputMap().put(enterKey, "IgnoreNewLine");
        invokingCellTextArea.getActionMap().put("IgnoreNewLine", enterAction);

        popupDialog.setLayout(new BorderLayout());
        popupDialog.add(invokingCellTextArea, BorderLayout.CENTER);

        cellTextField.addMouseListener(areaCaller);

        invokingCellTextArea.setSize(cellTextField.getSize());
        popupHeight = heightOfRow + 10;

        Frame parentFrame = WindowManager.getDefault().getMainWindow();
        parentFrame.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentMoved(ComponentEvent e) {
                popupDialog.setVisible(false);
            }

            @Override
            public void componentResized(ComponentEvent e) {
                popupDialog.setVisible(false);
            }
        });

    }

    @Override
    public JComponent getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected,
            int rowIndex,
            int vColIndex) {

        strValue = (String) value;
        previousStrValue = strValue;
        row = rowIndex;
        col = vColIndex;

        if (firstTime) {
            invokingCellTextArea.setSize(cellTextField.getSize());
            firstTime = false;
        }
        cellTextField.setText(strValue);
        return cellTextField;

    }

    @Override
    public Object getCellEditorValue() {
        return strValue;
    }

    @Override
    public boolean stopCellEditing() {
        int count = model.getRowCount();
        if (row > -1 && row <= count - 1) {
            model.setValueAt(strValue, row, col);
            fireEditingStopped();
        }
        return true;
    }

    @Override
    public void cancelCellEditing() {
        if (!strValue.equals(previousStrValue)) {
            int count = model.getRowCount();
            if (row > -1 && row <= count - 1) {
                invokingCellTextArea.setText(previousStrValue);
                model.setValueAt(previousStrValue, row, col);
            }
        }
       // super.cancelCellEditing();
        if (table != null) {
            table.setRowSelectionInterval(row, row);
            table.getSelectionModel().setAnchorSelectionIndex(col);
            table.requestFocusInWindow();
        }
    }

    private void showPopup(boolean aftreMousePressed) {

        final Point point = cellTextField.getLocationOnScreen();
        popupDialog.setBounds(
                (int) point.getX(),
                (int) point.getY(),
                cellTextField.getWidth(),
                popupHeight);

        popupDialog.setVisible(true);
        popupDialog.toFront();
        invokingCellTextArea.requestFocusInWindow();
        if (aftreMousePressed){
            invokingCellTextArea.select(0, invokingCellTextArea.getText().length());
        }
    }

    @Override
    public void editingPerformed(ActionEvent e) {
        areaCaller.mousePressed(new MouseEvent(cellTextField, 0, e.getWhen(), 0, 0, 0, 1, false));
    }
}
