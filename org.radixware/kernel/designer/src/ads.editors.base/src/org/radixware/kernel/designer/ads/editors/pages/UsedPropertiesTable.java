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

package org.radixware.kernel.designer.ads.editors.pages;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.DropMode;
import javax.swing.GroupLayout;
import javax.swing.InputVerifier;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import org.openide.util.ChangeSupport;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPageDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.components.CommonRadixObjectPopupMenu;
import org.radixware.kernel.designer.common.dialogs.components.CommonRadixObjectPopupMenu.IRadixObjectPopupMenuOwner;
import org.radixware.kernel.designer.common.dialogs.components.FixedSizeSquareButton;
import org.radixware.kernel.designer.common.dialogs.components.IRadixObjectChooserLeftComponent;
import org.radixware.kernel.designer.common.dialogs.components.RadixObjectChooserPanel;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.common.dialogs.components.SimpleTable;


public class UsedPropertiesTable implements IRadixObjectChooserLeftComponent, IRadixObjectPopupMenuOwner {

    private javax.swing.JScrollPane scroll;
    private javax.swing.JPanel panel;
    private javax.swing.JButton addColumnButton;
    private javax.swing.JButton removeColumnButton;
    private JSpinner spinner;
    private SimpleTable table;
    private EditorPagePropTableModel model;
    private AdsEditorPageDef editorPage;
    private JCheckBox chGlueToLeft;
    private JCheckBox chGlueToRight;

    public UsedPropertiesTable(AdsEditorPageDef page) {
        this.editorPage = page;
        this.table = new SimpleTable();
        updateContent();
        setupTable();
        scroll = new javax.swing.JScrollPane(table);

        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        panel = new javax.swing.JPanel();
        panel.setLayout(gbl);
        c.gridx = 0;
        c.gridy = 0;
        //c.weightx = 1.0;
        //c.weighty = 1.0;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        gbl.setConstraints(scroll, c);
        panel.add(scroll);

        JPanel cep = setupCellEditPanel();

        c.gridy++;
        c.weighty = 0;
        c.insets = new Insets(5, 0, 0, 0);
        c.fill = GridBagConstraints.HORIZONTAL;
        gbl.setConstraints(cep, c);
        panel.add(cep);
    }

    @Override
    public boolean isPopupMenuAvailable() {
        return hasSelection();
    }

    @Override
    public RadixObject getSelectedRadixObject() {
        Object[] selection = getSelectedItems();
        if (selection.length > 0) {
            Object sel = selection[0];
            if (sel instanceof Id) {
                AdsDefinition prop = editorPage.getUsedProperties().getReference((Id) sel).findProperty();
                if (prop != null) {
                    return prop;
                }
            } else if (sel instanceof Definition) {
                return (Definition) sel;
            }
        }
        return null;
    }
    private boolean selectionHandling = false;

    private JPanel setupCellEditPanel() {
        JPanel panel = new JPanel();
        javax.swing.JLabel label = new javax.swing.JLabel("Column span:");

        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        spinner = new JSpinner(new SpinnerNumberModel(1, 1, 1, 1));

        spinner.setEnabled(false);
        spinner.getEditor().setEnabled(false);


        chGlueToLeft = new JCheckBox("Stick to left");
        chGlueToLeft.setEnabled(false);
        chGlueToRight = new JCheckBox("Stick to right");
        chGlueToRight.setEnabled(false);
        chGlueToLeft.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!selectionHandling) {
                    EditorPagePropTableModel.PageItem selection = getSelectedItem();
                    EditorPagePropTableModel.PageItem item = getSelectedNeigbour(-1);
                    if (item != null) {
                        model.setGlutToLeft(selection, chGlueToLeft.isSelected());
                        //selection.setGlutToLeft(chGlueToLeft.isSelected());
                        if (chGlueToLeft.isSelected()) {
                            model.setGlutToRight(selection, false);
                            //selection.setGlutToRight(false);
                        }
                        chGlueToRight.setSelected(selection.isGlutToRight());
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                table.revalidate();
                                table.repaint();
                            }
                        });
                    }
                }
            }
        });
        chGlueToRight.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!selectionHandling) {
                    EditorPagePropTableModel.PageItem selection = getSelectedItem();
                    EditorPagePropTableModel.PageItem item = getSelectedNeigbour(1);
                    if (item != null) {
                        //selection.setGlutToRight(chGlueToRight.isSelected());
                        model.setGlutToRight(selection, chGlueToRight.isSelected());
                        if (chGlueToRight.isSelected()) {
                            model.setGlutToLeft(selection, false);
                        }
                        chGlueToLeft.setSelected(selection.isGlutToLeft());
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                table.revalidate();
                                table.repaint();
                            }
                        });
                    }
                }
            }
        });

        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).
                addGroup(layout.createSequentialGroup().addComponent(label).addGap(5).
                addComponent(spinner).
                addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).
                addComponent(chGlueToLeft).
                addGap(5).
                addComponent(chGlueToRight)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).
                addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).
                addComponent(spinner).addComponent(label).addComponent(chGlueToLeft).addComponent(chGlueToRight)));

        spinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!selectionHandling) {
                    EditorPagePropTableModel.PageItem item = getSelectedItem();
                    if (item != null) {
                        model.setColumnSpan(item, ((Number) spinner.getValue()).intValue());
                    }
                }
            }
        });
        return panel;
    }

    private javax.swing.JPanel setupLabelPanel() {
        javax.swing.JPanel labelPanel = new javax.swing.JPanel();

        addColumnButton = new FixedSizeSquareButton();
        addColumnButton.setIcon(RadixWareIcons.CREATE.ADD_COLUMN.getIcon());
        ActionListener buttonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UsedPropertiesTable.this.model.addColumn();
                addColumnButton.setEnabled(UsedPropertiesTable.this.model.getColumnCount() < 10);
            }
        };
        addColumnButton.addActionListener(buttonListener);

        removeColumnButton = new FixedSizeSquareButton();
        removeColumnButton.setIcon(RadixWareIcons.DELETE.DELETE_COLUMN.getIcon());
        ActionListener removeListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final int index = UsedPropertiesTable.this.table.getSelectedColumn();
                UsedPropertiesTable.this.model.removeColumn(index);
                afterRemoveSupport.fireChange();
                
            }
        };
        removeColumnButton.addActionListener(removeListener);

        javax.swing.JLabel label = new javax.swing.JLabel("Used Properties");

        GroupLayout layout = new GroupLayout(labelPanel);
        labelPanel.setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(label).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(addColumnButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(removeColumnButton)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(addColumnButton).addComponent(removeColumnButton).addComponent(label)));

        return labelPanel;
    }

    private EditorPagePropTableModel.PageItem getSelectedItem() {
        int c = table.getSelectedColumn();
        int r = table.getSelectedRow();
        if (c >= 0 && c < table.getColumnCount() && r >= 0 && r < table.getRowCount()) {
            Object val = table.getValueAt(r, c);
            if (val instanceof EditorPagePropTableModel.PageItem) {
                return (EditorPagePropTableModel.PageItem) val;
            }
        }
        return null;
    }

    private EditorPagePropTableModel.PageItem getSelectedNeigbour(int offset) {
        int c = table.getSelectedColumn();
        int r = table.getSelectedRow();
        if (r >= 0 && r < table.getRowCount()) {
            if (c + offset >= 0 && c + offset < table.getColumnCount()) {
                Object val = table.getValueAt(r, c + offset);
                if (val instanceof EditorPagePropTableModel.PageItem) {
                    return (EditorPagePropTableModel.PageItem) val;
                }
            }
        }
        return null;
    }
    final int selection[] = new int[2];

    private void setupTable() {
        CommonRadixObjectPopupMenu menu = new CommonRadixObjectPopupMenu(this);
        table.setComponentPopupMenu(menu);
        table.setAutoCreateColumnsFromModel(true);
        table.setDefaultRenderer(EditorPagePropTableModel.PageItem.class, new EditorPagePropTableCellRenderer());
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.setCellSelectionEnabled(true);
        table.setGridColor(Color.WHITE);

        final ListSelectionListener listener = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    if (table.getSelectedColumn() >= 0 && table.getSelectedRow() >= 0) {
                        selection[0] = table.getSelectedColumn();
                        selection[1] = table.getSelectedRow();
                    }
                    changeSupport.fireChange();
                }
            }
        };
        table.getColumnModel().getSelectionModel().addListSelectionListener(listener);
        table.getSelectionModel().addListSelectionListener(listener);

        changeSupport.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (spinner != null) {
                    selectionHandling = true;
                    try {

                        EditorPagePropTableModel.PageItem item = getSelectedItem();
                        if (item != null) {
                            SpinnerNumberModel spinnerModel = new SpinnerNumberModel(Integer.valueOf(model.getColSpan(item)), Integer.valueOf(1), null, Integer.valueOf(1));

                            spinner.setModel(spinnerModel);

                            spinner.setEnabled(!isReadonly());
                            spinner.getEditor().setEnabled(!isReadonly());
                            chGlueToLeft.setSelected(item.isGlutToLeft());
                            chGlueToRight.setSelected(item.isGlutToRight());
                            EditorPagePropTableModel.PageItem n = getSelectedNeigbour(-1);
                            chGlueToLeft.setEnabled(!isReadonly() && n != null && !n.isGlutToRight());
                            n = getSelectedNeigbour(1);
                            chGlueToRight.setEnabled(!isReadonly() && n != null && !n.isGlutToLeft());
                        } else {
                            spinner.setEnabled(false);
                            chGlueToLeft.setEnabled(false);
                            chGlueToRight.setEnabled(false);
                        }
                    } finally {
                        selectionHandling = false;
                    }
                }
            }
        });


        table.setTableHeader(null);

        table.setDragEnabled(true);
        table.setDropMode(DropMode.ON_OR_INSERT_ROWS);
        table.setTransferHandler(new PropertiesArrangmentTransferHandler());

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (e.getClickCount() == 2) {
                    final Point origin = e.getPoint();
                    int row = table.rowAtPoint(origin);
                    int column = table.columnAtPoint(origin);

                    if (row != -1 && column != -1) {
                        getDoubleClickSupport().fireEvent(new RadixObjectChooserPanel.DoubleClickChooserEvent(row));
                    }
                }
            }
        };
        table.addMouseListener(mouseAdapter);

        final String deleteAction = "delete";

        table.getActionMap().put(deleteAction, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getDoubleClickSupport().fireEvent(new RadixObjectChooserPanel.DoubleClickChooserEvent(getSelectedIndex()));
            }
        });

        KeyStroke deleteKey = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0);
        table.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(deleteKey, null);
        table.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(deleteKey, deleteAction);

        model.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int r = selection[1];
                int c = selection[0];
                if (r >= 0 && r < table.getRowCount()) {
                    table.setRowSelectionInterval(r, r);
                }
                if (c >= 0 && c < table.getColumnCount()) {
                    table.setColumnSelectionInterval(c, c);
                }
            }
        });

    }

    @Override
    public void addAllItems(Object[] objects) {
        int sRow = table.getSelectedRow();
        int sCol = table.getSelectedColumn();
        //sRow+1: RADIX-7557
        EditorPagePropTableModel.PageItem item = model.addItem(sRow + 1, sCol, objects);
        if (item != null && item.getRow() <= table.getRowCount() && item.getStartColumn()<=table.getColumnCount()) {
            table.changeSelection(item.getRow(), item.getStartColumn(), false, false);            
        }
//        changeSupport.fireChange();
    }

    @Override
    public void removeAll(Object[] objects) {
        for (Object obj : objects) {
            assert (obj instanceof Id);
            model.removeItem((Id) obj);
            //     changeSupport.fireChange();
        }
    }

    @Override
    public final void updateContent() {
        table.setEnabled(!editorPage.isReadOnly());
        if (model == null) {
            model = new EditorPagePropTableModel();
        }
        model.open(editorPage);
        table.setModel(model);
        if (addColumnButton != null) {
            addColumnButton.setEnabled(model.getColumnCount() < 10);
        }
    }

    @Override
    public Integer getItemCount() {
        if (model != null) {
            return model.getRowCount();
        }
        return 0;
    }

    @Override
    public Integer getSelectedIndex() {
        return table.getSelectedRow();
    }

    @Override
    public Object[] getSelectedItems() {
        int row = table.getSelectedRow();
        if (row > -1 && row < model.getRowCount()) {
            EditorPagePropTableModel.PageItem ref = (EditorPagePropTableModel.PageItem) model.getValueAt(row, table.getSelectedColumn());
            if (ref != null) {
                return new Object[]{ref.getPropertyId()};
            }
        }
        return new Object[0];
    }

    @Override
    public JComponent getVisualComponent() {
        return panel;
    }

    @Override
    public boolean hasSelection() {
        int row = getSelectedIndex();
        return row > -1 && row < model.getRowCount();
    }

    @Override
    public void setSelectedItem(Integer index) {
        if (index > -1 && index < model.getRowCount()) {
            table.setRowSelectionInterval(index, index);
            
            table.setColumnSelectionInterval(0, 0);
        }
    }

    @Override
    public void setSelectedItems(Object[] items) {
        assert (items[0] instanceof Id);
//        final int row = model.getRowIndexByProperty((Id) items[0]);
//        final int column = model.getColumnIndexByProperty((Id) items[0]);
//
//        assert (row > -1 && column > -1);
//
//        table.setRowSelectionInterval(row, row + items.length - 1);
//        table.setColumnSelectionInterval(column, column);
    }

    @Override
    public boolean isReadonly() {
        return editorPage.isReadOnly();
    }

    @Override
    public void setReadonly(boolean readonly) {
        table.setEnabled(!readonly);
    }

    @Override
    public void moveDown() {
        //do nothing
    }

    @Override
    public void moveUp() {
        //do nothing
    }

    @Override
    public JComponent getLabelComponent() {
        return setupLabelPanel();
    }
    private ChangeSupport changeSupport = new ChangeSupport(this);

    @Override
    public void addSelectionEventListener(ChangeListener listener) {
        changeSupport.addChangeListener(listener);
    }

    @Override
    public void removeSelectionEventListener(ChangeListener listener) {
        changeSupport.removeChangeListener(listener);
    }
    private ChangeSupport afterRemoveSupport = new ChangeSupport(this);

    public void addRemoveListener(ChangeListener listener) {
        afterRemoveSupport.addChangeListener(listener);
    }

    public void removeRemoveListener(ChangeListener listener) {
        afterRemoveSupport.removeChangeListener(listener);
    }

    @Override
    public boolean isOrderDependant() {
        return false;
    }

    @Override
    public RadixObjectChooserPanel.DoublieClickChooserSupport getDoubleClickSupport() {
        if (doubleClickSupport == null) {
            doubleClickSupport = new RadixObjectChooserPanel.DoublieClickChooserSupport();
        }
        return doubleClickSupport;
    }
    private RadixObjectChooserPanel.DoublieClickChooserSupport doubleClickSupport;
}
