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

package org.radixware.kernel.designer.common.dialogs;

import java.awt.BorderLayout;
import java.util.List;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import org.openide.util.NbBundle;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import org.radixware.kernel.common.repository.IExtensionInstaller;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.utils.RadixResourceBundle;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;


public class ExtensionsPanel extends javax.swing.JPanel {

    private final Layer layer;
    private final List<Layer.Extension> extensions;
    private final ExtensionsModel model = new ExtensionsModel();

    class ExtensionsModel extends AbstractTableModel {

        private String[] columns = new String[] {
            NbBundle.getBundle(ExtensionsPanel.class).getString("ExtensionsPanel.ColumnTitle"),
            NbBundle.getBundle(ExtensionsPanel.class).getString("ExtensionsPanel.ColumnFile"),
            NbBundle.getBundle(ExtensionsPanel.class).getString("ExtensionsPanel.ColumnInstaller")
        };

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public int getRowCount() {
            return extensions.size();
        }

        @Override
        public String getColumnName(int col) {
            return columns[col];
        }

        @Override
        public Object getValueAt(int row, int col) {
            Layer.Extension extension = extensions.get(row);
            switch (col) {
                case 0:
                    return extension.getTitle();
                case 1:
                    return extension.getFile();
                case 2:
                    return extension.getInstaller();
            }
            return null;
        }

        @Override
        public Class getColumnClass(int col) {
            switch (col) {
                case 0:
                    return String.class;
                case 1:
                    return String.class;
                case 2:
                    return String.class;
            }
            return null;
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return true;
        }

        @Override
        public void setValueAt(Object value, int row, int col) {
            Layer.Extension extension = extensions.get(row);
            switch (col) {
                case 0:
                    extension.setTitle((String)value);
                    break;
                case 1:
                    extension.setFile((String)value);
                    break;
                case 2:
                    extension.setInstaller((String)value);
                    break;
            }
            fireTableCellUpdated(row, col);
        }
    }


    public ExtensionsPanel(Layer layer, List<Layer.Extension> extensions) {
        initComponents();

        this.layer = layer;
        this.extensions = extensions;

        table.setModel(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        table.setRowHeight(20);

        TableColumn col0 = table.getColumnModel().getColumn(0);
        col0.setCellEditor(new StringCellEditor());

        TableColumn col1 = table.getColumnModel().getColumn(1);
        col1.setCellEditor(new FileCellEditor());
        
        TableColumn col2 = table.getColumnModel().getColumn(2);
        col2.setCellEditor(new InstallerCellEditor());

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                update();
            }
        });

        update();
        table.requestFocusInWindow();
    }

    public List<Layer.Extension> getExtensions() {
        return extensions;
    }

    private void update() {
        boolean readonly = layer.isReadOnly();
        int row = table.getSelectedRow();
        int count = table.getRowCount();
        btnAdd.setEnabled(!readonly);
        buttonRemove.setEnabled(!readonly&& row >= 0 && count > 0);
        buttonUp.setEnabled(!readonly && row >= 1);
        buttonDown.setEnabled(!readonly && row >= 0 && row < count-1);
        table.setEnabled(!readonly);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollPane = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        buttonPanel = new javax.swing.JPanel();
        btnAdd = new javax.swing.JButton();
        buttonRemove = new javax.swing.JButton();
        buttonUp = new javax.swing.JButton();
        buttonDown = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createEtchedBorder());
        setMinimumSize(new java.awt.Dimension(250, 120));
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
        table.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        btnAdd.setIcon(RadixWareIcons.CREATE.ADD.getIcon());
        btnAdd.setToolTipText(RadixResourceBundle.getMessage(ExtensionsPanel.class, "ExtensionsPanel.btnAdd.text")); // NOI18N
        btnAdd.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        buttonRemove.setIcon(RadixWareIcons.DELETE.DELETE.getIcon());
        buttonRemove.setToolTipText(RadixResourceBundle.getMessage(ExtensionsPanel.class, "ExtensionsPanel.btnRemove.text")); // NOI18N
        buttonRemove.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        buttonRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonRemoveActionPerformed(evt);
            }
        });

        buttonUp.setIcon(RadixWareIcons.ARROW.MOVE_UP.getIcon());
        buttonUp.setToolTipText(RadixResourceBundle.getMessage(ExtensionsPanel.class, "ExtensionsPanel.btnUp.text")); // NOI18N
        buttonUp.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        buttonUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonUpActionPerformed(evt);
            }
        });

        buttonDown.setIcon(RadixWareIcons.ARROW.MOVE_DOWN.getIcon());
        buttonDown.setToolTipText(RadixResourceBundle.getMessage(ExtensionsPanel.class, "ExtensionsPanel.btnDown.text")); // NOI18N
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
                    .addComponent(buttonRemove, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonUp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonDown, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAdd))
                .addContainerGap())
        );
        buttonPanelLayout.setVerticalGroup(
            buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonPanelLayout.createSequentialGroup()
                .addComponent(btnAdd)
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
                .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(buttonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(scrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
        if (table.getCellEditor() != null)
            table.getCellEditor().stopCellEditing();
        
        int row = Math.max(table.getSelectedRow(), 0);
        Layer.Extension extension = layer.new Extension(NbBundle.getBundle(ExtensionsPanel.class).getString("ExtensionsPanel.DefaultTitle"), "", "");
        extensions.add(row, extension);
        table.getSelectionModel().setSelectionInterval(row, row);
        table.requestFocusInWindow();
        update();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                table.updateUI();
            }
        });
}//GEN-LAST:event_btnAddActionPerformed

    private void buttonUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonUpActionPerformed
        // TODO add your handling code here:
        if (table.getCellEditor() != null)
            table.getCellEditor().stopCellEditing();

        int row = table.getSelectedRow();
        if (row > 0) {
            Layer.Extension extension0 = extensions.get(row-1);
            Layer.Extension extension1 = extensions.get(row);
            extensions.remove(extension0);
            extensions.remove(extension1);
            extensions.add(row-1, extension1);
            extensions.add(row, extension0);
            table.getSelectionModel().setSelectionInterval(row-1, row-1);
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
        if (table.getCellEditor() != null)
            table.getCellEditor().stopCellEditing();

        int row = table.getSelectedRow();
        if (row > -1 && row < table.getRowCount()-1) {
            Layer.Extension extension0 = extensions.get(row);
            Layer.Extension extension1 = extensions.get(row+1);
            extensions.remove(extension0);
            extensions.remove(extension1);
            extensions.add(row, extension1);
            extensions.add(row+1, extension0);
            table.getSelectionModel().setSelectionInterval(row+1, row+1);
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
        if (table.getCellEditor() != null)
            table.getCellEditor().stopCellEditing();
        
        int row = table.getSelectedRow();
        if (row > -1 && row < model.getRowCount()) {
            extensions.remove(row);
            row = Math.min(row, model.getRowCount()-1);
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
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton buttonDown;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton buttonRemove;
    private javax.swing.JButton buttonUp;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
    
    private class StringCellEditor extends DefaultCellEditor {

        public StringCellEditor() {
            super(new JTextField());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return super.getTableCellEditorComponent(table, value, isSelected, row, column);
        }

        @Override
        public boolean stopCellEditing() {
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
            button.setToolTipText(NbBundle.getMessage(ExtensionsPanel.class, "ExtensionsPanel.FileToolTip"));
            button.setPreferredSize(new Dimension(20, 20));
            button.setBackground(table.getSelectionBackground());
            button.setForeground(table.getSelectionForeground());
            button.setOpaque(true);
            add(label);
            add(button, BorderLayout.EAST);
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

    private class FileCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

        int row;
        private String file = "";
        private ExtLabel editor = new ExtLabel(this);

        public FileCellEditor() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fc = new JFileChooser(layer.getDirectory());

            fc.addChoosableFileFilter(new FileFilter() {

                @Override
                public boolean accept(File f) {
                    return f.isDirectory() || f.getName().endsWith(".jar");
                }

                @Override
                public String getDescription() {
                    return "";
                }
            });
            fc.setAcceptAllFileFilterUsed(false);

            int res = fc.showOpenDialog(ExtensionsPanel.this);
            if (res == JFileChooser.APPROVE_OPTION) {
                try {
                    file = getRelativePath(fc.getSelectedFile());
                } catch (IOException ex) {
                    file = null;
                }
                extensions.get(row).setInstaller(null);
                editor.setText(file);

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        table.updateUI();
                    }
                });
            }

            fireEditingStopped();
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int r, int c) {
            row = r;
            file = (String)value;
            editor.setText(file);
            return editor;
        }

        @Override
        public Object getCellEditorValue() {
            return file;
        }

    }

    private class InstallerCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

        private String installer = "";
        private JComboBox editor = new JComboBox();

        public InstallerCellEditor() {
            editor.addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            installer = (String)editor.getSelectedItem();
            fireEditingStopped();
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int r, int c) {
            installer = (String)value;

            List<String> values = new ArrayList<String>();
            File file = new File(layer.getDirectory(), extensions.get(r).getFile());
            if (file.exists() && file.isFile()) {
                try {
                    final URLClassLoader classLoader = URLClassLoader.newInstance(
                            new URL[] { file.toURI().toURL() },
                            Thread.currentThread().getContextClassLoader()
                            );
                    final JarFile jar = new JarFile(file);
                    final Enumeration<JarEntry> en = jar.entries();
                    while (en.hasMoreElements()) {
                        String name = en.nextElement().getName().replaceAll("\\/", ".");
                        if (!name.startsWith(layer.getURI()) || !name.endsWith(".class")) {
                            continue;
                        }
                        name = name.substring(0, name.length() - 6);
                        final Class<?> clazz = classLoader.loadClass(name);
                        if (IExtensionInstaller.class.isAssignableFrom(clazz)) {
                            values.add(name.substring(layer.getURI().length() + 1));
                        }
                    }
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }
            }

            editor.setModel(new DefaultComboBoxModel(values.toArray()));

            editor.setSelectedItem(installer);
            return editor;
        }

        @Override
        public Object getCellEditorValue() {
            return installer;
        }
    }

    private String getRelativePath(File path) throws IOException {
        String a = layer.getDirectory().getCanonicalFile().toURI().getPath();
        String b = path.getCanonicalFile().toURI().getPath();

        String[] basePaths = a.split("/");
        String[] otherPaths = b.split("/");

        int n = 0;
        for(; n < basePaths.length && n < otherPaths.length; n++) {
            if (!basePaths[n].equals(otherPaths[n]))
                break;
        }

        StringBuffer tmp = new StringBuffer("");
        for(int m = n; m < basePaths.length; m++)
            tmp.append("../");

        for(int m = n; m < otherPaths.length; m++) {
            tmp.append(otherPaths[m]);
            if (m < otherPaths.length - 1)
                tmp.append("/");
        }

        return tmp.toString();
    }

    public static boolean show(final Layer layer, final List<Layer.Extension> extensions) {
        final ExtensionsPanel panel = new ExtensionsPanel(layer, extensions);
        return new ModalDisplayer(panel, NbBundle.getBundle(ExtensionsPanel.class).getString("ExtensionsPanel.Title")) {
            @Override
            public void apply() {
            }
        }.showModal();
    }
}
