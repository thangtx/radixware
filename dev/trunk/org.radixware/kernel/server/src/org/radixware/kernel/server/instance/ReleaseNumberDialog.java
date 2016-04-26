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

package org.radixware.kernel.server.instance;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.server.Server;
import org.radixware.kernel.starter.meta.LayerMeta;
import org.radixware.kernel.starter.radixloader.RadixClassLoader;


public class ReleaseNumberDialog {

    private final JDialog dialog;
    private static Rectangle bounds = null;

    ReleaseNumberDialog(Component parent) {
        while (parent != null && !(parent instanceof Frame)) {
            parent = parent.getParent();
        }

        JTable table = new JTable(createTableModel());
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(24);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);

        JPanel panel = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        JOptionPane pane = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE);
        Border padding = BorderFactory.createEmptyBorder(0, 0, 10, 0);
        pane.setBorder(padding);
        pane.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if (!e.getPropertyName().equals(JOptionPane.VALUE_PROPERTY) || e.getNewValue() == null) {
                    return;
                }
//                pane.setValue(null);
                dialog.setVisible(false);
                dialog.dispose();
            }
        });

        dialog = new JDialog((Frame) parent, Messages.TITLE_RELEASE_NUMBER, true);
        dialog.setContentPane(pane);
        dialog.pack();
        dialog.setResizable(true);
        ActionListener actionListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                dialog.setVisible(false);
                dialog.dispose();
            }
        };
        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        dialog.getRootPane().registerKeyboardAction(actionListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
        if (bounds != null) {
            dialog.setBounds(bounds);
        } else {
            dialog.setSize(new Dimension(320, 240));
            dialog.setLocationRelativeTo(null);
        }
    }

    private TableModel createTableModel() {
        RadixClassLoader cl;
        try {
           cl = (RadixClassLoader)Server.class.getClassLoader();
        } catch (ClassCastException e) {
           throw new RadixError("Server should be loaded by starter", e);
        }
        String columns[] = {Messages.LAYER_URI, Messages.RELEASE_NUMBER};
        List<LayerMeta> list = cl.getRevisionMeta().getAllLayersSortedFromBottom();
        String rows[][] = new String[list.size()][2];
        for (int i = 0; i < list.size(); ++i) {
            LayerMeta layer = list.get(i);
            rows[i][0] = layer.getUri();
            rows[i][1] = layer.getReleaseNumber();
        }
        DefaultTableModel tableModel = new DefaultTableModel(rows, columns) {

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
        return tableModel;
    }

    public void show() {
        dialog.setVisible(true);
        bounds = dialog.getBounds();
    }

}
