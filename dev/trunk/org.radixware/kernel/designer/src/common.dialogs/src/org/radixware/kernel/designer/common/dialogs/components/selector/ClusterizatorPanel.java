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

package org.radixware.kernel.designer.common.dialogs.components.selector;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.common.dialogs.components.UpdateLocker;


final class ClusterizatorPanel<T> extends javax.swing.JPanel {

    private final class Render extends DefaultListCellRenderer {

        public Render() {
        }

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            setText(model.getName((T) value));
            setIcon(model.getIcon((T) value));

            if (model.isInvalid((T) value)) {
                setForeground(Color.RED);
            }

            if (model.isConst((T) value)) {
                setForeground(Color.GRAY);
            }

            return this;
        }
    }
    private ItemClusterizator.IClusterizatorModel<T> model;
    private UpdateLocker locker = new UpdateLocker();

    public ClusterizatorPanel(ItemClusterizator.IClusterizatorModel<T> model) {
        initComponents();

        this.model = model;

        chbInherit.setVisible(model.isInheritable());
        cmdUp.setVisible(model.isSortable());
        cmdDown.setVisible(model.isSortable());

        lstAll.setCellRenderer(new Render());
        lstSelected.setCellRenderer(new Render());

        lstAll.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        lstSelected.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        final ListSelectionListener listener = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                updateButtonsState();
            }
        };
        lstAll.getSelectionModel().addListSelectionListener(listener);
        lstSelected.getSelectionModel().addListSelectionListener(listener);

        lstAll.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    add();
                }
            }
        });

        lstSelected.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    remove();
                }
            }
        });

        update();
    }

    private void update() {
        locker.enterUpdate();
        lstAll.setModel(createAllModel());
        lstSelected.setModel(createSelectedModel());

        final boolean inherit = model.isInherit();
        chbInherit.setSelected(inherit);

        lstAll.setEnabled(!inherit);
        lstSelected.setEnabled(!inherit);

        cmdAdd.setEnabled(!inherit);
        cmdRemove.setEnabled(!inherit);

        cmdUp.setEnabled(!inherit);
        cmdDown.setEnabled(!inherit);

        updateButtonsState();

        locker.leavUpdate();
    }

    private DefaultListModel<T> createAllModel() {
        DefaultListModel<T> avaliableModel = new DefaultListModel<>();

        for (T item : model.getAvaliable()) {
            avaliableModel.addElement(item);
        }

        return avaliableModel;
    }

    private DefaultListModel<T> createSelectedModel() {
        DefaultListModel<T> avaliableModel = new DefaultListModel<>();

        for (T item : model.getAllSelected()) {
            avaliableModel.addElement(item);
        }

        return avaliableModel;
    }

    private T getSelectedItem(JList<T> list) {
        final int[] indices = list.getSelectedIndices();

        int lastIndex = indices[indices.length - 1];
        if (lastIndex < list.getModel().getSize() - 1) {
            return list.getModel().getElementAt(lastIndex + 1);
        }

        if (indices.length == list.getModel().getSize()) {
            return null;
        }
        return list.getModel().getElementAt(list.getModel().getSize() - indices.length - 1);
    }

    private void select(JList<T> list, List<T> values) {
        final int[] indices = new int[values.size()];

        int pos = 0;
        for (T item : values) {
            int index = indexOf(list, item);

            if (index != -1) {
                indices[pos++] = index;
            }
        }

        list.setSelectedIndices(indices);
    }

    private int indexOf(JList<T> list, T value) {
        for (int i = 0; i < list.getModel().getSize(); i++) {
            if (Objects.equals(value, list.getModel().getElementAt(i))) {
                return i;
            }
        }
        return -1;
    }

    private void add() {
        final List<T> selectedValuesAll = lstAll.getSelectedValuesList();

        if (selectedValuesAll != null && !selectedValuesAll.isEmpty()) {

            final DefaultListModel<T> selectedModel = (DefaultListModel<T>) lstSelected.getModel();
            final DefaultListModel<T> avaliableModel = (DefaultListModel<T>) lstAll.getModel();

            final T selectedItem = (T)getSelectedItem(lstAll);

            for (T object : selectedValuesAll) {
                selectedModel.addElement(object);
                avaliableModel.removeElement(object);
                model.add(object);
            }

            lstAll.setSelectedValue(selectedItem, true);

            select(lstSelected, selectedValuesAll);
        }
    }

    private void remove() {
        if (hasSelectionConst()) {
            return;
        }

        final List<T> selectedValues = lstSelected.getSelectedValuesList();

        if (selectedValues != null) {

            final DefaultListModel<T> selectedModel = (DefaultListModel<T>) lstSelected.getModel();
            final DefaultListModel<T> avaliableModel = (DefaultListModel<T>) lstAll.getModel();

            final T selectedItem = (T)getSelectedItem(lstSelected);

            for (T object : selectedValues) {
                selectedModel.removeElement(object);
                if (!model.isInvalid(object)) {
                    avaliableModel.addElement(object);
                }
                model.remove(object);
            }

            lstSelected.setSelectedValue(selectedItem, true);
            select(lstAll, selectedValues);
        }
    }

    private void up() {
        if (hasSelectionConst()) {
            return;
        }

        final List<T> selectedValues = lstSelected.getSelectedValuesList();

        if (indexOf(lstSelected, selectedValues.get(0)) == 0) {
            return;
        }

        for (T item : selectedValues) {
            model.up(item);
        }

        lstSelected.setModel(createSelectedModel());

        select(lstSelected, selectedValues);
    }

    private void down() {
        if (hasSelectionConst()) {
            return;
        }

        final List<T> selectedValues = lstSelected.getSelectedValuesList();

        if (indexOf(lstSelected, selectedValues.get(selectedValues.size() - 1)) == lstSelected.getModel().getSize() - 1) {
            return;
        }

        Collections.reverse(selectedValues);
        for (T item : selectedValues) {
            model.down(item);
        }

        lstSelected.setModel(createSelectedModel());

        select(lstSelected, selectedValues);
    }

    private void inherit(boolean inherit) {
        model.setInherit(inherit);
        update();
    }

    private void updateButtonsState() {
        final int[] selectedValues = lstSelected.getSelectedIndices();
        final int[] selectedValuesAll = lstAll.getSelectedIndices();

        cmdAdd.setEnabled(selectedValuesAll.length != 0);
        cmdRemove.setEnabled(selectedValues.length != 0);


        cmdUp.setEnabled(selectedValues.length != 0 && selectedValues[0] > 0);
        cmdDown.setEnabled(selectedValues.length != 0 && selectedValues[selectedValues.length - 1] < lstSelected.getModel().getSize() - 1);

        if (hasSelectionConst()) {
            cmdRemove.setEnabled(false);
            cmdUp.setEnabled(false);
            cmdDown.setEnabled(false);
        }
    }

    private boolean hasSelectionConst() {
        final int[] selectedValues = lstSelected.getSelectedIndices();

        if (selectedValues == null || selectedValues.length == 0) {
            return false;
        }
        for (int i : selectedValues) {
            final T item = (T) lstSelected.getModel().getElementAt(i);

            if (model.isConst(item)) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        chbInherit = new javax.swing.JCheckBox();
        lblSelected = new javax.swing.JLabel();
        lblAll = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstSelected = new javax.swing.JList();
        cmdAdd = new javax.swing.JButton();
        cmdRemove = new javax.swing.JButton();
        cmdUp = new javax.swing.JButton();
        cmdDown = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstAll = new javax.swing.JList();

        setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(chbInherit, org.openide.util.NbBundle.getMessage(ClusterizatorPanel.class, "ClusterizatorPanel.chbInherit.text")); // NOI18N
        chbInherit.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chbInheritItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        add(chbInherit, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(lblSelected, org.openide.util.NbBundle.getMessage(ClusterizatorPanel.class, "ClusterizatorPanel.lblSelected.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 0);
        add(lblSelected, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(lblAll, org.openide.util.NbBundle.getMessage(ClusterizatorPanel.class, "ClusterizatorPanel.lblAll.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 0);
        add(lblAll, gridBagConstraints);

        jScrollPane1.setViewportView(lstSelected);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jScrollPane1, gridBagConstraints);

        cmdAdd.setIcon(RadixWareIcons.ARROW.LEFT.getIcon(16, 16));
        org.openide.awt.Mnemonics.setLocalizedText(cmdAdd, org.openide.util.NbBundle.getMessage(ClusterizatorPanel.class, "ClusterizatorPanel.cmdAdd.text")); // NOI18N
        cmdAdd.setMinimumSize(null);
        cmdAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAddActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        add(cmdAdd, gridBagConstraints);

        cmdRemove.setIcon(RadixWareIcons.ARROW.RIGHT.getIcon(16, 16));
        org.openide.awt.Mnemonics.setLocalizedText(cmdRemove, org.openide.util.NbBundle.getMessage(ClusterizatorPanel.class, "ClusterizatorPanel.cmdRemove.text")); // NOI18N
        cmdRemove.setMinimumSize(null);
        cmdRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRemoveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        add(cmdRemove, gridBagConstraints);

        cmdUp.setIcon(RadixWareIcons.ARROW.MOVE_UP.getIcon(16, 16));
        org.openide.awt.Mnemonics.setLocalizedText(cmdUp, org.openide.util.NbBundle.getMessage(ClusterizatorPanel.class, "ClusterizatorPanel.cmdUp.text")); // NOI18N
        cmdUp.setMinimumSize(null);
        cmdUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdUpActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        add(cmdUp, gridBagConstraints);

        cmdDown.setIcon(RadixWareIcons.ARROW.MOVE_DOWN.getIcon(16, 16));
        org.openide.awt.Mnemonics.setLocalizedText(cmdDown, org.openide.util.NbBundle.getMessage(ClusterizatorPanel.class, "ClusterizatorPanel.cmdDown.text")); // NOI18N
        cmdDown.setMinimumSize(null);
        cmdDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdDownActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        add(cmdDown, gridBagConstraints);

        jScrollPane2.setViewportView(lstAll);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jScrollPane2, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void cmdAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAddActionPerformed
        add();
    }//GEN-LAST:event_cmdAddActionPerformed

    private void cmdRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRemoveActionPerformed
        remove();
    }//GEN-LAST:event_cmdRemoveActionPerformed

    private void cmdUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdUpActionPerformed
        up();
    }//GEN-LAST:event_cmdUpActionPerformed

    private void cmdDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdDownActionPerformed
        down();
    }//GEN-LAST:event_cmdDownActionPerformed

    private void chbInheritItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chbInheritItemStateChanged
        if (!locker.inUpdate()) {
            inherit(chbInherit.isSelected());
        }
    }//GEN-LAST:event_chbInheritItemStateChanged
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox chbInherit;
    private javax.swing.JButton cmdAdd;
    private javax.swing.JButton cmdDown;
    private javax.swing.JButton cmdRemove;
    private javax.swing.JButton cmdUp;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblAll;
    private javax.swing.JLabel lblSelected;
    private javax.swing.JList lstAll;
    private javax.swing.JList lstSelected;
    // End of variables declaration//GEN-END:variables
}
