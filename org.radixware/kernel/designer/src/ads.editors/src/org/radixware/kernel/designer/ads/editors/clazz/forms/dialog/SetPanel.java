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
package org.radixware.kernel.designer.ads.editors.clazz.forms.dialog;

import java.awt.Component;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.logging.Level;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import org.openide.explorer.propertysheet.PropertyPanel;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsAbstractUIDef;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIItemDef;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.AdsWidgetDef;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;
import org.radixware.kernel.designer.ads.editors.clazz.forms.props.UIPropertySupport;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;

abstract class SetPanel<T extends RadixObject, Y extends AdsUIItemDef> extends EditorDialog.EditorPanel<Y> {

    public SetPanel(AdsAbstractUIDef uiDef, Y widget) {
        super(uiDef, widget);
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        splitPane = new javax.swing.JSplitPane();
        scrollPane = new javax.swing.JScrollPane();
        tableProp = new javax.swing.JTable();
        panel = new javax.swing.JPanel();
        scrollPane2 = new javax.swing.JScrollPane();
        itemList = new javax.swing.JList();
        panel2 = new javax.swing.JPanel();
        buttonAdd = new javax.swing.JButton();
        buttonDel = new javax.swing.JButton();
        buttonUp = new javax.swing.JButton();
        buttonDown = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        setMinimumSize(new java.awt.Dimension(400, 200));
        setPreferredSize(new java.awt.Dimension(400, 200));
        setRequestFocusEnabled(false);
        setLayout(new java.awt.BorderLayout());

        splitPane.setDividerLocation(160);
        splitPane.setLastDividerLocation(160);

        tableProp.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null}
            },
            new String [] {
                "Title"
            }
        ));
        tableProp.setGridColor(new java.awt.Color(213, 213, 213));
        tableProp.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        scrollPane.setViewportView(tableProp);

        splitPane.setRightComponent(scrollPane);

        panel.setMinimumSize(new java.awt.Dimension(160, 10));
        panel.setPreferredSize(new java.awt.Dimension(160, 100));
        panel.setLayout(new java.awt.BorderLayout());

        itemList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        itemList.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        scrollPane2.setViewportView(itemList);

        panel.add(scrollPane2, java.awt.BorderLayout.CENTER);

        buttonAdd.setIcon(RadixWareDesignerIcon.CREATE.ADD.getIcon());
        buttonAdd.setFocusPainted(false);
        buttonAdd.setPreferredSize(new java.awt.Dimension(32, 32));
        buttonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAddActionPerformed(evt);
            }
        });
        panel2.add(buttonAdd);

        buttonDel.setIcon(RadixWareDesignerIcon.DELETE.DELETE.getIcon());
        buttonDel.setFocusPainted(false);
        buttonDel.setPreferredSize(new java.awt.Dimension(32, 32));
        buttonDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDelActionPerformed(evt);
            }
        });
        panel2.add(buttonDel);

        buttonUp.setIcon(RadixWareDesignerIcon.ARROW.MOVE_UP.getIcon());
        buttonUp.setFocusPainted(false);
        buttonUp.setPreferredSize(new java.awt.Dimension(32, 32));
        buttonUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonUpActionPerformed(evt);
            }
        });
        panel2.add(buttonUp);

        buttonDown.setIcon(RadixWareDesignerIcon.ARROW.MOVE_DOWN.getIcon());
        buttonDown.setFocusPainted(false);
        buttonDown.setMaximumSize(new java.awt.Dimension(32, 32));
        buttonDown.setMinimumSize(new java.awt.Dimension(32, 32));
        buttonDown.setPreferredSize(new java.awt.Dimension(32, 32));
        buttonDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDownActionPerformed(evt);
            }
        });
        panel2.add(buttonDown);

        panel.add(panel2, java.awt.BorderLayout.PAGE_END);

        splitPane.setLeftComponent(panel);

        add(splitPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void buttonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAddActionPerformed
        addHandler();
    }//GEN-LAST:event_buttonAddActionPerformed

    private void buttonDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDelActionPerformed
        delHandler();
    }//GEN-LAST:event_buttonDelActionPerformed

    private void buttonUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonUpActionPerformed
        upHandler();
    }//GEN-LAST:event_buttonUpActionPerformed

    private void buttonDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDownActionPerformed
        downHandler();
    }//GEN-LAST:event_buttonDownActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonAdd;
    private javax.swing.JButton buttonDel;
    private javax.swing.JButton buttonDown;
    private javax.swing.JButton buttonUp;
    private javax.swing.JList itemList;
    private javax.swing.JPanel panel;
    private javax.swing.JPanel panel2;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JScrollPane scrollPane2;
    private javax.swing.JSplitPane splitPane;
    private javax.swing.JTable tableProp;
    // End of variables declaration//GEN-END:variables
    private IListModel<T> listModel;
    private ITableModel<T> tableModel;

    @Override
    public void init() {
        itemList.setModel(listModel = createListModel());
        tableProp.setModel(tableModel = createTableModel());

        itemList.getSelectionModel().addListSelectionListener(tableModel);
        itemList.setSelectedIndex(getCurrentIndex());
        itemList.requestFocusInWindow();
        itemList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                ListSelectionModel sm = (ListSelectionModel) e.getSource();
                cursorChanged(sm.isSelectionEmpty() ? -1 : itemList.getSelectedIndex());
            }
        });

        tableProp.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableProp.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        tableProp.setRowHeight(18);

        tableProp.setDefaultRenderer(AdsUIProperty.class, new PropertyCellRenderer());
        tableProp.setDefaultEditor(AdsUIProperty.class, getCellEditor());

        tableProp.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

        update();
    }

    public final IListModel<T> getListModel() {
        return listModel;
    }

    public final ITableModel<T> getTableModel() {
        return tableModel;
    }

    public final JList getItemList() {
        return itemList;
    }

    protected abstract IListModel<T> createListModel();

    protected abstract ITableModel<T> createTableModel();

    protected int getCurrentIndex() {
        return 0;
    }

    protected void cursorChanged(int idx) {
        listModel.fireChanged(idx);
        update();
    }

    protected void update() {
        int idx = itemList.getSelectedIndex();
        int size = listModel.getSize();
        buttonAdd.setEnabled(!isReadOnly());
        buttonDel.setEnabled(!isReadOnly() && idx >= 0);
        buttonUp.setEnabled(!isReadOnly() && idx >= 1);
        buttonDown.setEnabled(!isReadOnly() && idx >= 0 && idx < size - 1);
    }

    protected void upHandler() {
        int idx = itemList.getSelectedIndex();
        listModel.up(idx);
        itemList.setSelectedIndex(Math.max(Math.min(idx - 1, listModel.getSize() - 1), -1));
        update();
    }

    protected void downHandler() {
        int idx = itemList.getSelectedIndex();
        listModel.down(idx);
        itemList.setSelectedIndex(Math.max(Math.min(idx + 1, listModel.getSize() - 1), -1));
        update();
    }

    protected void delHandler() {

        int idx = itemList.getSelectedIndex();
        listModel.del(idx);
        itemList.setSelectedIndex(Math.min(idx, listModel.getSize() - 1));
        update();
    }

    protected void addHandler() {
        int idx = itemList.getSelectedIndex();

        final T item = createItem();

        listModel.add(idx + 1, item);
        itemList.setSelectedIndex(Math.max(idx + 1, 0));
        update();
    }

    protected T createItem() {
        if (widget instanceof AdsRwtWidgetDef) {
            return (T) new AdsRwtWidgetDef(AdsMetaInfo.RWT_TAB_SET_TAB);
        } else {
            return (T) new AdsWidgetDef(AdsMetaInfo.WIDGET_CLASS);
        }
    }

    protected TableCellEditor getCellEditor() {
        return new PropertyCellEditor() {

            @Override
            protected UIPropertySupport getPropertySupport(AdsUIProperty prop) {
                final T item = tableModel.getItem();
                return new UIPropertySupport(prop, uiDef, item) {

                    @Override
                    public void setValue(Object val) {
                        try {
                            super.setValue(val);
                            listModel.fireContentChanged(item);
                        } catch (Throwable ex) {
                            java.util.logging.Logger.getLogger(UIPropertySupport.class.getName()).log(Level.WARNING, "Can not change property", ex);
                        }
                    }
                };
            }
        };
    }

    public interface ITableModel<T extends RadixObject> extends javax.swing.table.TableModel, ListSelectionListener {

        T getItem();
    }

    public interface IListModel<T extends RadixObject> extends javax.swing.ListModel<Object> {

        void fireContentChanged(T w);

        void fireChanged(int idx);

        void up(int idx);

        void down(int idx);

        void del(int idx);

        void add(int i, T item);

        T getItemAt(int i);
    }

    protected static abstract class DefaultTableModel<T extends RadixObject> extends AbstractTableModel implements ITableModel<T> {

        private String[] columns = new String[]{
            NbBundle.getMessage(SetPanel.class, "PropTable.Name"),
            NbBundle.getMessage(SetPanel.class, "PropTable.Value")
        };
        protected T item;
        protected List<AdsUIProperty> rows;
        protected final AdsAbstractUIDef uiDef;

        public DefaultTableModel(AdsAbstractUIDef uiDef) {
            this.uiDef = uiDef;
        }

        @Override
        public T getItem() {
            return item;
        }

        private boolean isReadOnly() {
            return item.isReadOnly() || AdsUIUtil.isReadOnlyNode(uiDef, item);
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public int getRowCount() {
            return item == null || rows == null ? 0 : rows.size();
        }

        @Override
        public String getColumnName(int col) {
            return columns[col];
        }

        @Override
        public Object getValueAt(int row, int col) {
            return col > 0 ? rows.get(row) : rows.get(row).getName();
        }

        @Override
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return col != 0 && !item.isReadOnly();
        }

        @Override
        public void setValueAt(Object value, int row, int col) {
            rows.set(row, (AdsUIProperty) value);
            fireTableCellUpdated(row, col);
        }
    }

    private class PropertyCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return new PropertyPanel(
                    new UIPropertySupport((AdsUIProperty) value, uiDef, ((ITableModel) table.getModel()).getItem()),
                    PropertyPanel.PREF_TABLEUI);
        }
    }
}
