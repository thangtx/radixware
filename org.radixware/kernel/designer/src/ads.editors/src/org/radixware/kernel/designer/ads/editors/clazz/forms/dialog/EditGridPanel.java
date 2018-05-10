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
import org.radixware.kernel.common.defs.ads.ui.AdsUIItemDef;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.designer.ads.editors.clazz.forms.props.UIPropertySupport;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;

abstract class EditGridPanel<T extends RadixObject, Y extends AdsUIItemDef> extends EditorDialog.EditorPanel<Y> {

    /**
     * Creates new form EditGridPanel
     */
    public EditGridPanel(AdsAbstractUIDef uiDef, Y widget) {
        super(uiDef, widget);
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        splitPane = new javax.swing.JSplitPane();
        leftPanel = new javax.swing.JPanel();
        btnPanel = new javax.swing.JPanel();
        downButton = new javax.swing.JButton();
        addButton = new javax.swing.JButton();
        delButton = new javax.swing.JButton();
        upButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        propList = new javax.swing.JList();
        rightPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        editPropsTable = new javax.swing.JTable();

        setLayout(new java.awt.GridLayout(1, 0));

        splitPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        splitPane.setDividerLocation(160);
        splitPane.setDividerSize(3);
        splitPane.setLastDividerLocation(160);

        downButton.setMaximumSize(new java.awt.Dimension(32, 32));
        downButton.setMinimumSize(new java.awt.Dimension(32, 32));
        downButton.setPreferredSize(new java.awt.Dimension(32, 32));
        downButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downButtonActionPerformed(evt);
            }
        });

        addButton.setToolTipText("");
        addButton.setMaximumSize(new java.awt.Dimension(32, 32));
        addButton.setMinimumSize(new java.awt.Dimension(32, 32));
        addButton.setPreferredSize(new java.awt.Dimension(32, 32));
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        delButton.setToolTipText("");
        delButton.setMaximumSize(new java.awt.Dimension(32, 32));
        delButton.setMinimumSize(new java.awt.Dimension(32, 32));
        delButton.setPreferredSize(new java.awt.Dimension(32, 32));
        delButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delButtonActionPerformed(evt);
            }
        });

        upButton.setMaximumSize(new java.awt.Dimension(32, 32));
        upButton.setMinimumSize(new java.awt.Dimension(32, 32));
        upButton.setPreferredSize(new java.awt.Dimension(32, 32));
        upButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout btnPanelLayout = new javax.swing.GroupLayout(btnPanel);
        btnPanel.setLayout(btnPanelLayout);
        btnPanelLayout.setHorizontalGroup(
            btnPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(delButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(upButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(downButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        btnPanelLayout.setVerticalGroup(
            btnPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(delButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(upButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(downButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        propList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        propList.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jScrollPane1.setViewportView(propList);

        javax.swing.GroupLayout leftPanelLayout = new javax.swing.GroupLayout(leftPanel);
        leftPanel.setLayout(leftPanelLayout);
        leftPanelLayout.setHorizontalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(leftPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        leftPanelLayout.setVerticalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, leftPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jScrollPane1.getAccessibleContext().setAccessibleParent(leftPanel);

        splitPane.setLeftComponent(leftPanel);

        editPropsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null}
            },
            new String [] {
                "Title"
            }
        ));
        jScrollPane2.setViewportView(editPropsTable);

        javax.swing.GroupLayout rightPanelLayout = new javax.swing.GroupLayout(rightPanel);
        rightPanel.setLayout(rightPanelLayout);
        rightPanelLayout.setHorizontalGroup(
            rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rightPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        rightPanelLayout.setVerticalGroup(
            rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rightPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 5, Short.MAX_VALUE))
        );

        splitPane.setRightComponent(rightPanel);

        add(splitPane);
    }// </editor-fold>//GEN-END:initComponents

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed

        //addItem();
    }//GEN-LAST:event_addButtonActionPerformed

    private void delButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delButtonActionPerformed

        //delItem();
    }//GEN-LAST:event_delButtonActionPerformed

    private void upButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upButtonActionPerformed

        //upItem();
    }//GEN-LAST:event_upButtonActionPerformed

    private void downButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downButtonActionPerformed

        //downItem();
    }//GEN-LAST:event_downButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JPanel btnPanel;
    private javax.swing.JButton delButton;
    private javax.swing.JButton downButton;
    private javax.swing.JTable editPropsTable;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel leftPanel;
    private javax.swing.JList propList;
    private javax.swing.JPanel rightPanel;
    private javax.swing.JSplitPane splitPane;
    private javax.swing.JButton upButton;
    // End of variables declaration//GEN-END:variables
    private IEditTableModel<T> propTableModel;
    private IListModel<T> propListModel;
    private boolean rowEdit = true;//get context

    @Override
    public void init() {
        propList.setModel(propListModel = createPropListModel());
        editPropsTable.setModel(propTableModel = createEditTableModel());
        propList.getSelectionModel().addListSelectionListener(propTableModel);

        propList.setSelectedIndex(getCurrentIndex());//0 by default
        propList.requestFocusInWindow();
        editPropsTable.setRowHeight(18);
        addButton.setIcon(RadixWareDesignerIcon.CREATE.ADD.getIcon());
        delButton.setIcon(RadixWareDesignerIcon.DELETE.DELETE.getIcon());

        propList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                ListSelectionModel sm = (ListSelectionModel) e.getSource();
                cursorChanged(sm.isSelectionEmpty() ? -1 : propList.getSelectedIndex());
            }
        });
        if (propList.getSelectedIndex() == 0) {//need to create proplist listener
            //context==rowNUm
            rowEdit = true;
            upButton.setIcon(RadixWareDesignerIcon.ARROW.MOVE_UP.getIcon());
            downButton.setIcon(RadixWareDesignerIcon.ARROW.MOVE_DOWN.getIcon());
        }

        if (propList.getSelectedIndex() == 1) {
            //context==colNum
            rowEdit = false;
            //switch button icons
            downButton.setIcon(RadixWareDesignerIcon.ARROW.LEFT.getIcon());
            upButton.setIcon(RadixWareDesignerIcon.ARROW.RIGHT.getIcon());
        }

        editPropsTable.setDefaultRenderer(AdsUIProperty.class, new PropertyCellRenderer());
        editPropsTable.setDefaultEditor(AdsUIProperty.class, getCellEditor());

        editPropsTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        update();
    }

    public final IListModel<T> getPropListModel() {
        return propListModel;
    }

    public final IEditTableModel<T> getTableModel() {
        return propTableModel;
    }

    public final JList getPropertyList() {
        return propList;
    }

    protected abstract IListModel<T> createPropListModel();

    protected abstract IEditTableModel<T> createEditTableModel();

    protected int getCurrentIndex() {
        return 0;
    }

    protected void cursorChanged(int idx) {
        propListModel.fireChanged(idx);
        update();
    }

    //update rownum and colnum rows
    private void update() {
        addButton.setEnabled(!isReadOnly());
        delButton.setEnabled(!isReadOnly());
        upButton.setEnabled(!isReadOnly());
        downButton.setEnabled(!isReadOnly());
    }

    
    protected TableCellEditor getCellEditor() {
        if (isReadOnly()) {
            return null;
        }
        return new PropertyCellEditor() {
            @Override
            protected UIPropertySupport getPropertySupport(AdsUIProperty prop) {
                final T item = propTableModel.getProperty();//return item
                return new UIPropertySupport(prop, uiDef, item) {
                    @Override
                    public void setValue(Object val) {
                        try {
                            super.setValue(val);
                            propListModel.fireContentChanged(item);
                        } catch (Throwable ex) {
                            java.util.logging.Logger.getLogger(UIPropertySupport.class.getName()).log(Level.WARNING, "Can not change property", ex);
                        }
                    }
                };
            }
        };
    }

    protected static abstract class DefaultTableModel<T extends RadixObject> extends AbstractTableModel implements IEditTableModel<T> {

        private String[] columns = new String[]{
            NbBundle.getMessage(EditGridPanel.class, "PropTable.Name"),
            NbBundle.getMessage(EditGridPanel.class, "PropTable.Value")
        };
        protected T item;
        protected List<AdsUIProperty> rows;//properties

        @Override
        public T getProperty() {
            return item;
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
        public void setValueAt(Object value, int row, int col) {
            rows.set(row, (AdsUIProperty) value);
            fireTableCellUpdated(row, col);
        }
    }

    //create model for JList propList
    public interface IListModel<T extends RadixObject> extends javax.swing.ListModel<Object> {
        //private IListModel<T> propListModel;

        void fireContentChanged(T w);

        void fireChanged(int idx);

        T getPropertyAt(int i);
    }

    //create model for JTable editPropsTable
    public interface IEditTableModel<T extends RadixObject> extends javax.swing.table.TableModel, ListSelectionListener {
        //  private IEditTableModel<T> propTableModel;

        T getProperty();
    }

    private class PropertyCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return new PropertyPanel(
                    new UIPropertySupport((AdsUIProperty) value, uiDef, ((IEditTableModel) table.getModel()).getProperty()),
                    PropertyPanel.PREF_TABLEUI);
        }
    }
}
