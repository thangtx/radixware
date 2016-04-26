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

import java.awt.event.KeyEvent;
import java.util.EnumSet;

import org.radixware.kernel.common.enums.EIsoLanguage;
import javax.swing.event.TableModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.DefaultListModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import java.awt.Toolkit;
import java.awt.Dimension;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;

public final class LanguagesSelector extends TopComponent {

    private List<EIsoLanguage> languages;
    private final DefaultListModel listModel;
    private final TableModelListener modelListener = new TableModelListener() {

        @Override
        public void tableChanged(TableModelEvent e) {

            final int column = e.getColumn();

            if (column == LanguagesTableModel.CHECK_BOX_COLUMN) {
                final TableModel model = (TableModel) e.getSource();
                int row = e.getFirstRow();
                Object data = model.getValueAt(row, LanguagesTableModel.CHECK_BOX_COLUMN);
                Object obj = model.getValueAt(row, LanguagesTableModel.LANGUAGE_NAME_COLUMN);
                if (((Boolean) data)) {
                    listModel.addElement(((EIsoLanguage) obj));
                } else {
                    listModel.removeElement(((EIsoLanguage) obj));
                }
            }
        }
    };

    private LanguagesSelector(List<EIsoLanguage> initialSet) {

        super();

        languages = new ArrayList<>(initialSet);

        setName(NbBundle.getMessage(LanguagesSelector.class, "CTL_NewLanguagesSelectorTopComponent"));
        setToolTipText(NbBundle.getMessage(LanguagesSelector.class, "HINT_NewLanguagesSelectorTopComponent"));

        initComponents();

        keywordTextField.setBackground(java.awt.Color.white);

        listModel = new DefaultListModel();
        lstSelected.setModel(listModel);

        final TableColumnModel columnModel = languagesTable.getColumnModel();
        final TableColumn checkboxesColumn = columnModel.getColumn(LanguagesTableModel.CHECK_BOX_COLUMN);
        checkboxesColumn.setMaxWidth(10);

        final TableColumn languagesColumn = columnModel.getColumn(LanguagesTableModel.LANGUAGE_NAME_COLUMN);
        int languagesColumnWidth = getWidth() - 10;
        languagesColumn.setMinWidth(languagesColumnWidth);
        languagesColumn.setMinWidth(languagesColumnWidth);
        languagesColumn.setPreferredWidth(languagesColumnWidth);

        //optimize table
        javax.swing.ToolTipManager.sharedInstance().unregisterComponent(languagesTable);

        languagesTable.setTableHeader(null);
        jScrollPane1.setColumnHeaderView(null);
        languagesTable.getModel().addTableModelListener(modelListener);
        fillTable();

        keywordTextField.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                processEvent();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                processEvent();
            }

            @Override
            public void keyPressed(KeyEvent e) {
                processEvent();
            }

            private void processEvent() {
                if (keywordTextField.getText().equals("<Type Language Name>")) {
                    keywordTextField.setText("");
                }

                final String key = keywordTextField.getText().toLowerCase();
                for (int i = 0, count = languagesTable.getRowCount(); i < count; ++i) {

                    if (((EIsoLanguage) languagesTable.getValueAt(i, LanguagesTableModel.LANGUAGE_NAME_COLUMN)).toString().toLowerCase().startsWith(key)) {
                        languagesTable.setRowSelectionInterval(i, i);
                        languagesTable.scrollRectToVisible(languagesTable.getCellRect(i, 0, true));
                        break;
                    }
                }
            }
        });
        btMoveUp.setIcon(RadixWareDesignerIcon.ARROW.MOVE_UP.getIcon());
        btMoveDn.setIcon(RadixWareDesignerIcon.ARROW.MOVE_DOWN.getIcon());

        btMoveUp.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        move(-1);
                        updateButtonsState();
                    }
                });

            }
        });
        btMoveDn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        move(1);
                        updateButtonsState();
                    }
                });

            }
        });

        //set position
        final Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((dimension.width - getWidth()) / 2, (dimension.height - getHeight()) / 2);
        updateButtonsState();
        lstSelected.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                updateButtonsState();
            }
        });
    }

    private void updateButtonsState() {
        int index = lstSelected.getSelectedIndex();
        if (index >= 0 && index < listModel.getSize()) {
            btMoveDn.setEnabled(index < listModel.getSize() - 1);
            btMoveUp.setEnabled(index > 0);
        } else {
            btMoveDn.setEnabled(false);
            btMoveUp.setEnabled(false);
        }
    }

    private void move(int dir) {
        int index = lstSelected.getSelectedIndex();
        if (index >= 0 && index < listModel.getSize()) {
            int target = index + dir;
            if (target >= 0 && target < listModel.getSize()) {
                Object one = listModel.get(index);
                Object another = listModel.get(target);
                listModel.set(index, another);
                listModel.set(target, one);
                calculateCurrentLanguages();
                if (target >= 0 && target < listModel.getSize()) {
                    lstSelected.setSelectedIndex(target);
                }
            }
        }
    }

    private void fillTable() {
        if (!languages.isEmpty()) {
            final TableModel tableModel = languagesTable.getModel();
            final EIsoLanguage[] l = EIsoLanguage.values();

            for (int i = 0, len = l.length; i < len; ++i) {
                if (languages.contains(l[i])) {
                    tableModel.setValueAt(true, i, LanguagesTableModel.CHECK_BOX_COLUMN);
                }
            }
        }
    }

    private void performOK() {
        calculateCurrentLanguages();
    }

    private void performCancel() {
        //restore state
        final TableModel tableModel = languagesTable.getModel();
        for (int i = 0; i < tableModel.getRowCount(); ++i) {
            tableModel.setValueAt(false, i, LanguagesTableModel.CHECK_BOX_COLUMN);
        }

        listModel.clear();
        fillTable();
        calculateCurrentLanguages();
    }

    private void calculateCurrentLanguages() {
        languages = new LinkedList<>();

        if (!listModel.isEmpty()) {
            try {

                for (int i = 0; i < listModel.getSize(); ++i) {
                    languages.add((EIsoLanguage) listModel.get(i));
                }
            } catch (NoConstItemWithSuchValueError e) {
                return;
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        keywordTextField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        languagesTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstSelected = new javax.swing.JList();
        btMoveUp = new javax.swing.JButton();
        btMoveDn = new javax.swing.JButton();

        jSplitPane1.setDividerLocation(300);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        keywordTextField.setText(org.openide.util.NbBundle.getMessage(LanguagesSelector.class, "LanguagesSelector.keywordTextField.text_1")); // NOI18N
        keywordTextField.setOpaque(false);

        jScrollPane1.setAutoscrolls(true);
        jScrollPane1.setFocusCycleRoot(true);

        languagesTable.setModel(new LanguagesTableModel(EIsoLanguage.values()) );
        languagesTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
        languagesTable.setAutoscrolls(false);
        languagesTable.setFocusable(false);
        languagesTable.setSurrendersFocusOnKeystroke(true);
        jScrollPane1.setViewportView(languagesTable);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE)
                    .addComponent(keywordTextField))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(keywordTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                .addContainerGap())
        );

        jSplitPane1.setTopComponent(jPanel1);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(LanguagesSelector.class, "LanguagesSelector.jLabel1.text_1")); // NOI18N
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jScrollPane2.setAutoscrolls(true);
        jScrollPane2.setFocusCycleRoot(true);

        lstSelected.setAutoscrolls(false);
        lstSelected.setFocusable(false);
        lstSelected.setValueIsAdjusting(true);
        jScrollPane2.setViewportView(lstSelected);

        org.openide.awt.Mnemonics.setLocalizedText(btMoveUp, org.openide.util.NbBundle.getMessage(LanguagesSelector.class, "LanguagesSelector.btMoveUp.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(btMoveDn, org.openide.util.NbBundle.getMessage(LanguagesSelector.class, "LanguagesSelector.btMoveDn.text")); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btMoveUp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btMoveDn, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btMoveUp)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btMoveDn)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jSplitPane1.setRightComponent(jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btMoveDn;
    private javax.swing.JButton btMoveUp;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTextField keywordTextField;
    private javax.swing.JTable languagesTable;
    private javax.swing.JList lstSelected;
    // End of variables declaration//GEN-END:variables

    /**
     * Shows language set configure dialog Returns true if initial set was
     * modified false otherwise
     */
    public static final boolean configureLanguages(final List<EIsoLanguage> languages) {
        final LanguagesSelector selectLanguagesDlg = new LanguagesSelector(languages);
        DialogDescriptor selectLanguagesDialogDescriptor = new DialogDescriptor(selectLanguagesDlg, "Configure Languages");
        selectLanguagesDialogDescriptor.setHelpCtx(null);
        selectLanguagesDialogDescriptor.setModal(true);
        selectLanguagesDialogDescriptor.setValid(true);
        EnumSet<EIsoLanguage> check = languages.isEmpty()? EnumSet.noneOf(EIsoLanguage.class) : EnumSet.copyOf(languages);
        selectLanguagesDialogDescriptor.setButtonListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {

                final Object obj = evt.getSource();
                if (obj == DialogDescriptor.OK_OPTION) {
                    selectLanguagesDlg.performOK();
                    languages.clear();
                    languages.addAll(selectLanguagesDlg.languages);
                } else if (obj == DialogDescriptor.CANCEL_OPTION) {
                    selectLanguagesDlg.performCancel();
                }
            }
        });

        java.awt.Dialog selectLanguagesDialog = DialogDisplayer.getDefault().createDialog(selectLanguagesDialogDescriptor);
        selectLanguagesDialog.setMinimumSize(new java.awt.Dimension(360, 580));

        selectLanguagesDialog.setVisible(true);

        return !languages.equals(check);
    }
}
