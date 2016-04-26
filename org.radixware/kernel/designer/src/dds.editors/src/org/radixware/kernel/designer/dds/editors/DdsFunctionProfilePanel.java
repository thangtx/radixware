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

package org.radixware.kernel.designer.dds.editors;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.EventObject;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import org.openide.util.ChangeSupport;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.defs.dds.DdsDefinitions;
import org.radixware.kernel.common.defs.dds.DdsFunctionDef;
import org.radixware.kernel.common.defs.dds.DdsParameterDef;
import org.radixware.kernel.common.enums.EParamDirection;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.utils.ValTypes;
import org.radixware.kernel.designer.common.dialogs.components.CommonParametersEditorCellLib;
import org.radixware.kernel.designer.common.dialogs.components.CommonParametersEditorCellLib.LabelWithInsetsRenderer;
import org.radixware.kernel.designer.common.dialogs.components.TunedTable.EscapeKeyOnTableListener;
import org.radixware.kernel.designer.common.dialogs.components.TunedTable.EscapePressedOnTableEvent;
import org.radixware.kernel.designer.common.dialogs.components.TunedTable.TunedComboCellEditor;
import org.radixware.kernel.designer.common.dialogs.components.UpdateLocker;
import org.radixware.kernel.designer.common.dialogs.components.ValTypeComboBox;
import org.radixware.kernel.designer.common.dialogs.components.state.StateManager;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;

public class DdsFunctionProfilePanel extends javax.swing.JPanel {

    private class DdsFunctionProfileDisplayer extends ModalDisplayer
            implements ChangeListener {

        public DdsFunctionProfileDisplayer(DdsFunctionProfilePanel panel, DdsFunctionDef function) {
            super(panel);
            panel.setPreferredSize(new Dimension(500, 300));
            panel.setMinimumSize(new Dimension(500, 300));
            panel.open(function);
            getDialogDescriptor().setValid(panel.isComplete());
            panel.addChangeListener(this);
            setTitle(NbBundle.getMessage(DdsFunctionProfilePanel.class, "DdsFunctionProfile-Dialog-Title"));
            setIcon(function.getIcon());
        }
        private boolean isEdited = false;

        @Override
        protected void apply() {
            if (function != null && isEdited && !((DdsFunctionProfilePanel) getComponent()).isEscapePressedOnTable) {
                ((DdsFunctionProfilePanel) getComponent()).apply();
            }
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            if (e.getSource().equals(getComponent())) {
                isEdited = true;
                getDialogDescriptor().setValid(((DdsFunctionProfilePanel) getComponent()).isComplete());
            }
        }
    }

    public void editProfile(DdsFunctionDef function) {
        DdsFunctionProfileDisplayer displayer = new DdsFunctionProfileDisplayer(this, function);
        displayer.showModal();
    }
    private DefaultTableModel pModel;
    private CommonParametersEditorCellLib.StringCellEditor nameEditor;
    private TypeCellEditor typeEditor;
    private CommonParametersEditorCellLib.StringCellEditor dbTypeEditor;
    private TunedComboCellEditor directionEditor;
    private CommonParametersEditorCellLib.StringCellEditor defValEditor;
    private CommonParametersEditorCellLib.DescriptionCellEditor commentEditor;
    private DdsFunctionDef function;
    protected boolean isEscapePressedOnTable = false;
    private EscapeKeyOnTableListener escapeListener = new EscapeKeyOnTableListener() {
        @Override
        public void onEvent(EscapePressedOnTableEvent e) {
            DdsFunctionProfilePanel.this.isEscapePressedOnTable = true;
        }
    };

    public DdsFunctionProfilePanel() {
        super();
        initComponents();
        setupUI();
    }

    private void setupUI() {
        setupParametersTable();
        namePanel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                changeSupport.fireChange();
            }
        });
        Set<EValType> content = new HashSet<>();
        content.add(null);
        content.addAll(ValTypes.DDS_FUNCTION_RESULT_TYPES);
        resultTypePanel.setFilter(content);

        DocumentListener dbTypeListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                changeSupport.fireChange();
            }
        };
        dbResultTypePanel.getDocument().addDocumentListener(dbTypeListener);

        resultTypePanel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                EValType selected = resultTypePanel.getValType();
                changeSupport.fireChange();
                if (selected == null) {
                    dbResultTypePanel.setText("");
                    dbResultTypePanel.setEditable(false);
                    dbLabel.setEnabled(false);
                } else {
                    dbResultTypePanel.setEditable(true);
                    dbLabel.setEnabled(true);
                    if (selected.equals(EValType.BOOL)) {
                        dbResultTypePanel.setText("boolean");
                    }
                    if (selected.equals(EValType.INT)) {
                        dbResultTypePanel.setText("integer");
                    }
                    if (selected.equals(EValType.STR)) {
                        dbResultTypePanel.setText("varchar2");
                    }
                    if (selected.equals(EValType.NUM)) {
                        dbResultTypePanel.setText("number");
                    }
                    if (selected.equals(EValType.DATE_TIME)) {
                        dbResultTypePanel.setText("date");
                    }
                }
            }
        });

        addBtn.setIcon(RadixWareIcons.CREATE.ADD.getIcon(13, 13));
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addButtonPressed();
            }
        });
        removeBtn.setIcon(RadixWareIcons.DELETE.DELETE.getIcon(13, 13));
        removeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeButtonPressed();
            }
        });

        ActionListener upListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                move(true);
            }
        };
        upButton.addActionListener(upListener);

        ActionListener downListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                move(false);
            }
        };
        downButton.addActionListener(downListener);

        pTable.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent e) {
                afterFocusEvent();
            }

            @Override
            public void focusGained(FocusEvent e) {
                afterFocusEvent();
            }
        });

        pTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                afterFocusEvent();
            }
        });
    }

    private void afterFocusEvent() {
        if (pModel.getRowCount() != 0) {
            final int selected = pTable.getSelectedRow();
            if (selected != -1) {
                removeBtn.setEnabled(!readonly);
                upButton.setEnabled(!readonly && selected > 0);
                downButton.setEnabled(!readonly && selected < (pTable.getRowCount() - 1));
            } else {
                removeBtn.setEnabled(false);
                upButton.setEnabled(false);
                downButton.setEnabled(false);
            }
        } else {
            removeBtn.setEnabled(false);
            upButton.setEnabled(false);
            downButton.setEnabled(false);
        }
        addBtn.setEnabled(!readonly);
    }

    @Override
    public void requestFocus() {
        namePanel.requestFocusInWindow();
    }

    private void setupParametersTable() {
        pTable.getEscapeKeySupport().addEventListener(escapeListener);

        String[] titles = new String[7];
        titles[0] = NbBundle.getMessage(DdsFunctionProfilePanel.class, "DdsFunctionParameters-Name");
        titles[1] = NbBundle.getMessage(DdsFunctionProfilePanel.class, "DdsFunctionParameters-Type");
        titles[2] = NbBundle.getMessage(DdsFunctionProfilePanel.class, "DdsFunctionParameters-DBType");
        titles[3] = NbBundle.getMessage(DdsFunctionProfilePanel.class, "DdsFunctionParameters-Direction");
        titles[4] = NbBundle.getMessage(DdsFunctionProfilePanel.class, "DdsFunctionParameters-Default");
        titles[5] = NbBundle.getMessage(DdsFunctionProfilePanel.class, "DdsFunctionParameters-Comment");
        titles[6] = "Id";
        pModel = new DefaultTableModel(titles, 0);
        pTable.setModel(pModel);
        pTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        pTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pTable.setCellSelectionEnabled(true);
        nameEditor = new CommonParametersEditorCellLib.StringCellEditor(changeSupport, pModel, readonly);//new NameCellEditor();
        typeEditor = new TypeCellEditor(pTable, new ValTypeComboBox());
        dbTypeEditor = new CommonParametersEditorCellLib.StringCellEditor(changeSupport, pModel, readonly);

        directionEditor = new TunedComboCellEditor(pTable);
        directionEditor.setComboValues(EParamDirection.values());
        directionEditor.addComboActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeSupport.fireChange();
            }
        });

        defValEditor = new CommonParametersEditorCellLib.StringCellEditor(changeSupport, pModel, readonly);
        commentEditor = new CommonParametersEditorCellLib.DescriptionCellEditor(changeSupport, readonly, pModel);//new CommentCellEditor();
        pTable.getColumnModel().getColumn(0).setCellEditor(nameEditor);
        pTable.getColumnModel().getColumn(0).setCellRenderer(new LabelWithInsetsRenderer());
        pTable.getColumnModel().getColumn(1).setCellEditor(typeEditor);
        pTable.getColumnModel().getColumn(1).setCellRenderer(new TypeCellRenderer());
        pTable.getColumnModel().getColumn(2).setCellEditor(dbTypeEditor);
        pTable.getColumnModel().getColumn(2).setCellRenderer(new LabelWithInsetsRenderer());
        pTable.getColumnModel().getColumn(3).setCellEditor(directionEditor);
        pTable.getColumnModel().getColumn(3).setCellRenderer(new LabelWithInsetsRenderer());
        pTable.getColumnModel().getColumn(4).setCellEditor(defValEditor);
        pTable.getColumnModel().getColumn(4).setCellRenderer(new LabelWithInsetsRenderer());
        pTable.getColumnModel().getColumn(5).setCellEditor(commentEditor);
        pTable.getColumnModel().getColumn(5).setCellRenderer(new LabelWithInsetsRenderer());
        pTable.getColumnModel().removeColumn(pTable.getColumnModel().getColumn(6));
        pTable.putClientProperty("terminateEditOnFocusLost", Boolean.FALSE);
    }
    private boolean readonly = false;

    public void open(DdsFunctionDef function) {
        this.isEscapePressedOnTable = false;
        this.function = function;
        update();
    }

    public void updateReadonlyState() {
        nameEditor.setReadonly(readonly);
        dbTypeEditor.setReadonly(readonly);
        commentEditor.setReadonly(readonly);
        defValEditor.setReadonly(readonly);

        namePanel.setEditable(!readonly);
        resultTypePanel.setEnabled(!readonly);
        dbResultTypePanel.setEnabled(!readonly);
        directionEditor.setReadonly(readonly);
        afterFocusEvent();
    }

    public void update() {
        if (function != null) {
            readonly = function.isReadOnly();
            namePanel.setCurrentName(function.getName());
            resultTypePanel.setValType(function.getResultValType());
            dbResultTypePanel.setText(function.getResultDbType());
            pModel.setRowCount(0);

            if (function.getParameters() != null
                    && function.getParameters().size() != 0) {
                for (DdsParameterDef p : function.getParameters()) {
                    pModel.addRow(new Object[]{
                        new CommonParametersEditorCellLib.StringCellValue(p.getName()),
                        new TypeCellValue(p.getValType()),
                        new CommonParametersEditorCellLib.StringCellValue(p.getDbType()),
                        p.getDirection(),
                        new CommonParametersEditorCellLib.StringCellValue(p.getDefaultVal()),
                        new CommonParametersEditorCellLib.StringCellValue(p.getDescription()),
                        p /*pseudocolumn*/});
                }
            }
        } else {
            readonly = true;
        }
        updateReadonlyState();
    }

    public void apply() {
        TableCellEditor editor = pTable.getCellEditor();
        if (editor != null) {
            editor.stopCellEditing();
        }

        function.setName(namePanel.getCurrentName());
        function.setResultValType(resultTypePanel.getValType());

        function.setResultDbType(dbResultTypePanel.getText());

        //function.getParameters().clear();

        function.getParameters().clear();
        for (int i = 0; i < pModel.getRowCount(); i++) {
            DdsParameterDef param = (DdsParameterDef) pModel.getValueAt(i, 6);
            final String name = pModel.getValueAt(i, 0).toString();
            param.setName(name);
            TypeCellValue type = (TypeCellValue) pModel.getValueAt(i, 1);
            param.setValType(type.value);
            param.setDbType(pModel.getValueAt(i, 2).toString());
            param.setDirection((EParamDirection) pModel.getValueAt(i, 3));
            param.setDefaultVal(pModel.getValueAt(i, 4).toString());
            param.setDescription(pModel.getValueAt(i, 5).toString());
            function.getParameters().add(param);
        }
    }

    private void addButtonPressed() {
        pModel.addRow(new Object[]{
            new CommonParametersEditorCellLib.StringCellValue("newParameter"),
            new TypeCellValue(EValType.INT),
            new CommonParametersEditorCellLib.StringCellValue("integer"),
            EParamDirection.IN,
            new CommonParametersEditorCellLib.StringCellValue(""),
            new CommonParametersEditorCellLib.StringCellValue(""),
            DdsParameterDef.Factory.newInstance("")
        });
        changeSupport.fireChange();

        pTable.clearSelection();

        pTable.requestFocusInWindow();

        pTable.getSelectionModel().setSelectionInterval(pModel.getRowCount() - 1, pModel.getRowCount() - 1);
        pTable.editCellAt(pModel.getRowCount() - 1, 0);
        nameEditor.getComponent().requestFocusInWindow();
    }

    private void removeButtonPressed() {
        TableCellEditor editor = pTable.getCellEditor();
        if (editor != null) {
            editor.stopCellEditing();
        }
        int row = pTable.getSelectedRow();
        if (row > -1 && row < pTable.getRowCount()) {
            pModel.removeRow(row);
            changeSupport.fireChange();
            if (row == 0) {
                row++;
            }
            pTable.requestFocusInWindow();
            pTable.getSelectionModel().setSelectionInterval(row - 1, row - 1);
        }
    }

    private void move(boolean up) {
        TableCellEditor editor = pTable.getCellEditor();
        if (editor != null) {
            editor.stopCellEditing();
        }
        int row = pTable.getSelectedRow();
        if (row > -1 && row < pTable.getRowCount()) {
            CommonParametersEditorCellLib.StringCellValue nameVal = new CommonParametersEditorCellLib.StringCellValue(pModel.getValueAt(row, 0).toString());
            TypeCellValue typeVal = (TypeCellValue) pModel.getValueAt(row, 1);
            CommonParametersEditorCellLib.StringCellValue dbTypeVal = new CommonParametersEditorCellLib.StringCellValue(pModel.getValueAt(row, 2).toString());
            EParamDirection directionVal = (EParamDirection) pModel.getValueAt(row, 3);
            CommonParametersEditorCellLib.StringCellValue defVal = new CommonParametersEditorCellLib.StringCellValue(pModel.getValueAt(row, 4).toString());
            CommonParametersEditorCellLib.StringCellValue descriptionVal = new CommonParametersEditorCellLib.StringCellValue(pModel.getValueAt(row, 5).toString());
            DdsParameterDef paramDef = (DdsParameterDef) pModel.getValueAt(row, 6);
            pModel.removeRow(row);

            if (up) {
                row--;
            } else {
                row++;
            }
            pModel.insertRow(row, new Object[]{
                nameVal,
                typeVal,
                dbTypeVal,
                directionVal,
                defVal,
                descriptionVal,
                paramDef
            });

            changeSupport.fireChange();
            pTable.requestFocusInWindow();
            pTable.getSelectionModel().setSelectionInterval(row, row);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        dbLabel = new javax.swing.JLabel();
        namePanel = new org.radixware.kernel.designer.common.dialogs.components.NameEditPanel();
        dbResultTypePanel = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        stateDisplayer1 = new org.radixware.kernel.designer.common.dialogs.components.state.StateDisplayer();
        jScrollPane1 = new javax.swing.JScrollPane();
        pTable = new org.radixware.kernel.designer.common.dialogs.components.TunedTable();
        addBtn = new javax.swing.JButton();
        removeBtn = new javax.swing.JButton();
        resultTypePanel = new org.radixware.kernel.designer.common.dialogs.components.ValTypeComboBox();
        upButton = new javax.swing.JButton();
        downButton = new javax.swing.JButton();

        setMinimumSize(new java.awt.Dimension(300, 250));

        jLabel1.setText(org.openide.util.NbBundle.getMessage(DdsFunctionProfilePanel.class, "DdsFunctionProfile-Name")); // NOI18N

        jLabel2.setText(org.openide.util.NbBundle.getMessage(DdsFunctionProfilePanel.class, "DdsFunctionProfile-ResultType")); // NOI18N

        dbLabel.setText(org.openide.util.NbBundle.getMessage(DdsFunctionProfilePanel.class, "DdsFunctionProfile-DB-ResultType")); // NOI18N

        dbResultTypePanel.setMaximumSize(null);
        dbResultTypePanel.setMinimumSize(null);

        jLabel4.setText(org.openide.util.NbBundle.getMessage(DdsFunctionProfilePanel.class, "DdsFunctionProfile-Parameters")); // NOI18N

        pTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(pTable);

        addBtn.setText(org.openide.util.NbBundle.getMessage(DdsFunctionProfilePanel.class, "DdsProfileAddTip")); // NOI18N
        addBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        removeBtn.setText(org.openide.util.NbBundle.getMessage(DdsFunctionProfilePanel.class, "DdsProfileRemoveTip")); // NOI18N
        removeBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        upButton.setIcon(RadixWareIcons.ARROW.MOVE_UP.getIcon(13, 13));
        upButton.setText(org.openide.util.NbBundle.getMessage(DdsFunctionProfilePanel.class, "DdsProfileUpTip")); // NOI18N
        upButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        downButton.setIcon(RadixWareIcons.ARROW.MOVE_DOWN.getIcon(13, 13));
        downButton.setText(org.openide.util.NbBundle.getMessage(DdsFunctionProfilePanel.class, "DdsProfileDownTip")); // NOI18N
        downButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
                    .addComponent(jLabel4)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(namePanel, 0, 0, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(resultTypePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                                .addGap(17, 17, 17)
                                .addComponent(dbLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(dbResultTypePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE))))
                    .addComponent(stateDisplayer1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addBtn)
                    .addComponent(removeBtn)
                    .addComponent(upButton)
                    .addComponent(downButton))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {addBtn, downButton, removeBtn, upButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(namePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(dbResultTypePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(dbLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
                        .addComponent(resultTypePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE))
                .addGap(12, 12, 12)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(addBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(upButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(downButton)
                        .addGap(56, 56, 56))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(stateDisplayer1, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel1, namePanel});

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {dbLabel, jLabel2});

    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addBtn;
    private javax.swing.JLabel dbLabel;
    private javax.swing.JTextField dbResultTypePanel;
    private javax.swing.JButton downButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private org.radixware.kernel.designer.common.dialogs.components.NameEditPanel namePanel;
    private org.radixware.kernel.designer.common.dialogs.components.TunedTable pTable;
    private javax.swing.JButton removeBtn;
    private org.radixware.kernel.designer.common.dialogs.components.ValTypeComboBox resultTypePanel;
    private org.radixware.kernel.designer.common.dialogs.components.state.StateDisplayer stateDisplayer1;
    private javax.swing.JButton upButton;
    // End of variables declaration//GEN-END:variables
    private ChangeSupport changeSupport = new ChangeSupport(this);

    public void addChangeListener(ChangeListener l) {
        changeSupport.addChangeListener(l);
    }

    public void removeChangeListener(ChangeListener l) {
        changeSupport.removeChangeListener(l);
    }
    //STATE CHECK UTILS
    private final StateManager stateManager = new StateManager(this);

    public boolean isComplete() {
        if (namePanel.isComplete()) {
            return checkNameCellState();
        } else {
            stateManager.error(ERROR_NAME);
            return false;
        }
    }

    public boolean checkNameCellState() {
        int undefined = checkUndefined();
        if (undefined != -1) {
            stateManager.error(UNDEFINED_NAMES + undefined);
            return false;
        } else {
            String repeated = checkRepeated();
            if (!repeated.isEmpty()) {
                stateManager.error(REPEATED_NAMES + repeated);
                return false;
            } else {
                String incorrect = checkIncorrectName();
                if (!incorrect.isEmpty()) {
                    stateManager.error("Incorrect parameter name: " + incorrect);
                    return false;
                }
                stateManager.ok();
                return true;
            }
        }
    }

    private String checkIncorrectName() {
        if (pModel != null) {
            if (nameEditor.getComponent().isShowing()) {
                String strval = nameEditor.getCellEditorValue().toString();
                if (strval.matches("(\\d)+(\\w)*")
                        || strval.contains(" ")) {
                    return strval;
                }
            }
            for (int i = 0; i < pModel.getRowCount(); i++) {
                String strval = pModel.getValueAt(i, 0).toString();
                if (strval.matches("(\\d)+(\\w)*")
                        || strval.contains(" ")) {
                    return pModel.getValueAt(i, 0).toString();
                }
            }
        }
        return "";
    }
    private static final String UNDEFINED_NAMES = NbBundle.getMessage(DdsFunctionProfilePanel.class, "DdsFunctionProfile-Undefined");
    private static final String REPEATED_NAMES = NbBundle.getMessage(DdsFunctionProfilePanel.class, "DdsFunctionProfile-Repeated");
    private static final String ERROR_NAME = NbBundle.getMessage(DdsFunctionProfilePanel.class, "DdsFunctionProfile-ErrorName");

    private int checkUndefined() {
        if (pModel != null) {
            int row = nameEditor.getCurrentRow();
            if (nameEditor.getComponent().isShowing()) {
                if (nameEditor.getCellEditorValue().toString().isEmpty()) {
                    return row + 1;
                }
                for (int i = 0; i < pModel.getRowCount(); i++) {
                    if (i != row
                            && pModel.getValueAt(i, 0).toString().isEmpty()) {
                        return i + 1;
                    }
                }
            } else {
                for (int i = 0; i < pModel.getRowCount(); i++) {
                    if (pModel.getValueAt(i, 0).toString().isEmpty()) {
                        return i + 1;
                    }
                }
            }
        }
        return -1;
    }

    private String checkRepeated() {
        HashSet<String> names = new HashSet<>();
        if (pModel != null) {
            int row = nameEditor.getCurrentRow();
            if (nameEditor.getComponent().isShowing()) {
                names.add(nameEditor.getCellEditorValue().toString());
                for (int i = 0; i < pModel.getRowCount(); i++) {
                    if (i != row
                            && !names.add(pModel.getValueAt(i, 0).toString())) {
                        return pModel.getValueAt(i, 0).toString();
                    }
                }
            } else {
                for (int i = 0; i < pModel.getRowCount(); i++) {
                    if (!names.add(pModel.getValueAt(i, 0).toString())) {
                        return pModel.getValueAt(i, 0).toString();
                    }
                }
            }
        }
        return "";
    }

    //TABLE UTILS
    private class TypeCellValue {

        EValType value;

        public TypeCellValue(EValType value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value != null ? value.getName() : "";
        }
    }

    private class TypeCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setText(value.toString());
            TypeCellValue v = (TypeCellValue) value;
            setIcon(RadixObjectIcon.getForValType(v.value).getIcon(13, 13));
            return this;
        }
    }

    private class TypeCellEditor extends TunedComboCellEditor {

        private ValTypeComboBox editor;
        private int row;
        private TypeCellValue val;
        private UpdateLocker locker = new UpdateLocker();

        public TypeCellEditor(JTable table, ValTypeComboBox combo) {
            super(table, combo.getComboComponent());
            editor = combo;
            editor.setFilter(ValTypes.DDS_FUNCTION_PARAM_TYPES);
            editor.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    if (!locker.inUpdate()) {
                        EValType selected = editor.getValType();
                        changeSupport.fireChange();
                        if (selected.equals(EValType.BOOL)) {
                            pModel.setValueAt(new CommonParametersEditorCellLib.StringCellValue("boolean"), row, 2);
                        }
                        if (selected.equals(EValType.INT)) {
                            pModel.setValueAt(new CommonParametersEditorCellLib.StringCellValue("integer"), row, 2);
                        }
                        if (selected.equals(EValType.STR)) {
                            pModel.setValueAt(new CommonParametersEditorCellLib.StringCellValue("varchar2"), row, 2);
                        }
                        if (selected.equals(EValType.NUM)) {
                            pModel.setValueAt(new CommonParametersEditorCellLib.StringCellValue("number"), row, 2);
                        }
                        if (selected.equals(EValType.DATE_TIME)) {
                            pModel.setValueAt(new CommonParametersEditorCellLib.StringCellValue("date"), row, 2);
                        }
                    }
                }
            });

        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
                int r, int c) {
            this.row = r;
            TypeCellValue v = (TypeCellValue) value;
            val = v;
            locker.enterUpdate();
            editor.setValType(v.value);
            locker.leavUpdate();
            return editor;
        }

        @Override
        public Object getCellEditorValue() {
            return new TypeCellValue(editor.getValType());
        }

        @Override
        public boolean isCellEditable(EventObject e) {
            return !DdsFunctionProfilePanel.this.readonly;
        }

        @Override
        public void editingPerformed(ActionEvent e) {
            super.editingPerformed(e);
            if (!editor.getFilter().contains(val.value)) {
                editor.getEditor().setItem(val.value);
            }
        }
    }
}
