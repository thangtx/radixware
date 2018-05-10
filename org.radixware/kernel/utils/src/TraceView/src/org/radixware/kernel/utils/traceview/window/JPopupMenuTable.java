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
package org.radixware.kernel.utils.traceview.window;

import org.radixware.kernel.utils.traceview.utils.TraceViewUtils;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.radixware.kernel.utils.traceview.TraceEvent.IndexedDate;
import org.radixware.kernel.utils.traceview.TraceViewSettings;
import org.radixware.kernel.utils.traceview.TraceViewSettings.EEventColumn;

class JPopupMenuTable extends JPopupMenu {

    private int rowIndex;
    private final JTable table;
    private final FilterPanelRight filterPanelRight;

    JPopupMenuTable(JTable table, FilterPanelRight filterPanelRight) {
        this.table = table;
        this.filterPanelRight = filterPanelRight;

        JMenuItem addContext = new JMenuItem("Add context");
        JMenuItem saveSelectedLogMessageInFile = new JMenuItem("Save to file");
        JMenuItem copyDate = new JMenuItem("Copy date");

        addContext.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (rowIndex > -1) {
                    JPopupMenuTable.this.filterPanelRight.parseContext(JPopupMenuTable.this.table.getValueAt(rowIndex, EEventColumn.CONTEXT.getIndex()).toString());
                }
            }
        });

        saveSelectedLogMessageInFile.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedRows = JPopupMenuTable.this.table.getSelectedRows();
                if (selectedRows.length != 0) {
                    JFileChooser fc = new JFileChooserWithOverwriting();
                    FileNameExtensionFilter traceFiles = new FileNameExtensionFilter("TRACE FILES(*.log,*.xml)", "log","xml");
                    fc.addChoosableFileFilter(traceFiles);
                    fc.setFileFilter(traceFiles);
                    if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = fc.getSelectedFile();
                        if (selectedFile.exists()) {
                            selectedFile.delete();
                        }
                        try {
                            selectedFile.createNewFile();
                            TraceViewUtils.saveLogMessagesInFile(selectedFile, selectedRows, JPopupMenuTable.this.table);
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(null, "Error on creating file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });

        copyDate.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = JPopupMenuTable.this.table.getSelectedRow();
                if (selectedRow != -1) {
                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
                            new StringSelection(
                                    TraceViewSettings.DATE_FORMAT_PLAIN_TEXT.get().format(((IndexedDate) JPopupMenuTable.this.table.getValueAt(rowIndex, EEventColumn.DATE.getIndex())).getDate())), null);
                }
            }
        });

        add(addContext);
        add(saveSelectedLogMessageInFile);
        add(copyDate);
        addPopupMenuListener(new PopupMenuListenerContext(this));
    }

    public void setRowAndColumnIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

   public class PopupMenuListenerContext implements PopupMenuListener {

        JPopupMenuTable popupMenu;

        PopupMenuListenerContext(JPopupMenuTable popupMenu) {
            this.popupMenu = popupMenu;
        }

        @Override
        public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    int rowAtPoint = table.rowAtPoint(SwingUtilities.convertPoint(popupMenu, new Point(0, 0), table));
                    popupMenu.setRowAndColumnIndex(rowAtPoint);
                }
            });
        }

        @Override
        public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
        }

        @Override
        public void popupMenuCanceled(PopupMenuEvent e) {
        }
    }
}
