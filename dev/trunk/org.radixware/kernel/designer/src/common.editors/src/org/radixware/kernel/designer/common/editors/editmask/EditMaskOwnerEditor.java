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
package org.radixware.kernel.designer.common.editors.editmask;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMask;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskBool;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskDateTime;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskEnum;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskFilePath;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskInt;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskList;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskNum;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskStr;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskTimeInterval;
import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.designer.common.annotations.registrators.EditorFactoryRegistration;
import org.radixware.kernel.designer.common.editors.RadixObjectEditor;
import org.radixware.kernel.designer.common.editors.RadixObjectModalEditor;
import org.radixware.kernel.designer.common.general.editors.IEditorFactory;

public class EditMaskOwnerEditor extends RadixObjectModalEditor<EditMaskOwner> {

    private final EditMaskOwner editMaskOwner;
    private final EditMask defaultEditMask;
    private Editor currentEditor = null;
    private EEditMaskType currentEditMaskType = null;
    private volatile boolean updating = false;

    /**
     * For properties
     */
    protected EditMaskOwnerEditor(EditMaskOwner editMaskOwner) {
        super(editMaskOwner);

        this.editMaskOwner = editMaskOwner;
        initComponents();

        updating = true;
        useDefaultCheckBox.setSelected(editMaskOwner.getEditMask() == null);
        updating = false;

        defaultEditMask = editMaskOwner.getDefaultEditMask();
        if (defaultEditMask == null) {
            JLabel label = new JLabel("<This type cannot be edited>");
            JPanel forLabel = new JPanel();
            forLabel.setLayout(new GridLayout());
            label.setAlignmentX(0.5f);
            label.setAlignmentY(0.5f);
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setHorizontalTextPosition(JLabel.CENTER);
            forLabel.add(label);
            editorPanel.add(forLabel, BorderLayout.CENTER);
            updateEnableState();
            return;
        }

        updating = true;
//        useDefaultCheckBox.setSelected(editMaskOwner.getEditMask() == null);
        EEditMaskType type = (editMaskOwner.getEditMask() != null ? editMaskOwner.getEditMask() : defaultEditMask).getType();
        setCurrentEditMaskType(type);
        updating = false;
    }

    private void updateEnableState() {
        boolean enabled = !editMaskOwner.isReadOnly() && defaultEditMask != null;
        boolean hasEditMask = !useDefaultCheckBox.isSelected();
//        useDefaultCheckBox.setEnabled(enabled);
        useDefaultCheckBox.setEnabled(!editMaskOwner.isReadOnly());
        EEditMaskType type = defaultEditMask != null ? defaultEditMask.getType() : null;
        intNumberRadioButton.setEnabled(enabled && hasEditMask && (type == EEditMaskType.INT/* || type == EEditMaskType.ENUM*/));
        realNumberRadioButton.setEnabled(enabled && hasEditMask && (type == EEditMaskType.NUM));
        stringRadioButton.setEnabled(enabled && hasEditMask && (type == EEditMaskType.STR));
        listRadioButton.setEnabled(enabled && hasEditMask && (type == EEditMaskType.INT || type == EEditMaskType.NUM
                || type == EEditMaskType.STR/* || type == EEditMaskType.ENUM*/));
        dateTimeRadioButton.setEnabled(enabled && hasEditMask && (type == EEditMaskType.DATE_TIME));
        timeIntervalRadioButton.setEnabled(enabled && hasEditMask && (type == EEditMaskType.INT));
        constSetRadioButton.setEnabled(enabled && hasEditMask && (type == EEditMaskType.ENUM));
        booleanRadioButton.setEnabled(enabled && hasEditMask && (type == EEditMaskType.BOOL || type == EEditMaskType.INT
                || type == EEditMaskType.STR || type == EEditMaskType.NUM));
        filePathRadioButton.setEnabled(enabled && hasEditMask && (type == EEditMaskType.FILE_PATH || type == EEditMaskType.STR));
        if (currentEditor != null) {
            currentEditor.setReadOnly(!enabled || !hasEditMask);
        }
        setComplete(enabled);
    }

    private void setCurrentEditMaskType(EEditMaskType editMaskType) {
        updating = true;
        currentEditMaskType = editMaskType;
        EditMask editMask;
        boolean useDefault = useDefaultCheckBox.isSelected();
        if (useDefault) {
            editMask = defaultEditMask;
        } else {
            editMask = editMaskOwner.getEditMask();
            if (editMask == null || editMask.getType() != editMaskType) {
                editMask = defaultEditMask;
            }
        }
        if (editMask != null && editMask.getType() != editMaskType) {
            editMask = null;
        }
        if (currentEditor != null) {
            currentEditor.setVisible(false);
            editorPanel.getLayout().removeLayoutComponent(currentEditor);
        }
        switch (editMaskType) {
            case DATE_TIME:
                editorPanel.add(currentEditor = new DateTimeEditor((EditMaskDateTime) editMask), BorderLayout.CENTER);
                dateTimeRadioButton.setSelected(true);
                break;
            case ENUM:
                editorPanel.add(currentEditor = new EnumEditor((EditMaskEnum) editMask), BorderLayout.CENTER);
                constSetRadioButton.setSelected(true);
                break;
            case INT:
                editorPanel.add(currentEditor = new IntEditor((EditMaskInt) editMask), BorderLayout.CENTER);
                intNumberRadioButton.setSelected(true);
                break;
            case STR:
                editorPanel.add(currentEditor = new StrEditor((EditMaskStr) editMask), BorderLayout.CENTER);
                stringRadioButton.setSelected(true);
                break;
            case LIST:
                ListEditor listEditor = new ListEditor(editMaskOwner.getDefinition(), (EditMaskList) editMask, defaultEditMask.getType());
                editorPanel.add(currentEditor = listEditor, BorderLayout.CENTER);
                listRadioButton.setSelected(true);
                break;
            case NUM:
                editorPanel.add(currentEditor = new NumEditor((EditMaskNum) editMask), BorderLayout.CENTER);
                realNumberRadioButton.setSelected(true);
                break;
            case TIME_INTERVAL:
                editorPanel.add(currentEditor = new TimeIntervalEditor((EditMaskTimeInterval) editMask), BorderLayout.CENTER);
                timeIntervalRadioButton.setSelected(true);
                break;
            case BOOL:
                editorPanel.add(currentEditor = new BooleanEditor(editMaskOwner.getDefinition(), (EditMaskBool) editMask, defaultEditMask.getType()), BorderLayout.CENTER);
                booleanRadioButton.setSelected(true);
                break;
            case FILE_PATH:
                editorPanel.add(currentEditor = new FilePathEditor(editMaskOwner.getDefinition(), (EditMaskFilePath) editMask), BorderLayout.CENTER);
                filePathRadioButton.setSelected(true);
                break;
        }
        updateEnableState();
        updating = false;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        useDefaultCheckBox = new javax.swing.JCheckBox();
        commonPanel = new javax.swing.JPanel();
        intNumberRadioButton = new javax.swing.JRadioButton();
        realNumberRadioButton = new javax.swing.JRadioButton();
        stringRadioButton = new javax.swing.JRadioButton();
        dateTimeRadioButton = new javax.swing.JRadioButton();
        timeIntervalRadioButton = new javax.swing.JRadioButton();
        listRadioButton = new javax.swing.JRadioButton();
        constSetRadioButton = new javax.swing.JRadioButton();
        booleanRadioButton = new javax.swing.JRadioButton();
        filePathRadioButton = new javax.swing.JRadioButton();
        editorPanel = new javax.swing.JPanel();

        useDefaultCheckBox.setText(org.openide.util.NbBundle.getMessage(EditMaskOwnerEditor.class, "EditMaskOwnerEditor.useDefaultCheckBox.text")); // NOI18N
        useDefaultCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                useDefaultCheckBoxItemStateChanged(evt);
            }
        });

        commonPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        buttonGroup1.add(intNumberRadioButton);
        intNumberRadioButton.setText(org.openide.util.NbBundle.getMessage(EditMaskOwnerEditor.class, "EditMaskOwnerEditor.intNumberRadioButton.text")); // NOI18N
        intNumberRadioButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                intNumberRadioButtonItemStateChanged(evt);
            }
        });

        buttonGroup1.add(realNumberRadioButton);
        realNumberRadioButton.setText(org.openide.util.NbBundle.getMessage(EditMaskOwnerEditor.class, "EditMaskOwnerEditor.realNumberRadioButton.text")); // NOI18N
        realNumberRadioButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                realNumberRadioButtonItemStateChanged(evt);
            }
        });

        buttonGroup1.add(stringRadioButton);
        stringRadioButton.setText(org.openide.util.NbBundle.getMessage(EditMaskOwnerEditor.class, "EditMaskOwnerEditor.stringRadioButton.text")); // NOI18N
        stringRadioButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                stringRadioButtonItemStateChanged(evt);
            }
        });

        buttonGroup1.add(dateTimeRadioButton);
        dateTimeRadioButton.setText(org.openide.util.NbBundle.getMessage(EditMaskOwnerEditor.class, "EditMaskOwnerEditor.dateTimeRadioButton.text")); // NOI18N
        dateTimeRadioButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                dateTimeRadioButtonItemStateChanged(evt);
            }
        });

        buttonGroup1.add(timeIntervalRadioButton);
        timeIntervalRadioButton.setText(org.openide.util.NbBundle.getMessage(EditMaskOwnerEditor.class, "EditMaskOwnerEditor.timeIntervalRadioButton.text")); // NOI18N
        timeIntervalRadioButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                timeIntervalRadioButtonItemStateChanged(evt);
            }
        });

        buttonGroup1.add(listRadioButton);
        listRadioButton.setText(org.openide.util.NbBundle.getMessage(EditMaskOwnerEditor.class, "EditMaskOwnerEditor.listRadioButton.text")); // NOI18N
        listRadioButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                listRadioButtonItemStateChanged(evt);
            }
        });

        buttonGroup1.add(constSetRadioButton);
        constSetRadioButton.setText(org.openide.util.NbBundle.getMessage(EditMaskOwnerEditor.class, "EditMaskOwnerEditor.constSetRadioButton.text")); // NOI18N
        constSetRadioButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                constSetRadioButtonItemStateChanged(evt);
            }
        });

        buttonGroup1.add(booleanRadioButton);
        booleanRadioButton.setText(org.openide.util.NbBundle.getMessage(EditMaskOwnerEditor.class, "EditMaskOwnerEditor.booleanRadioButton.text")); // NOI18N
        booleanRadioButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                booleanRadioButtonItemStateChanged(evt);
            }
        });

        buttonGroup1.add(filePathRadioButton);
        filePathRadioButton.setText(org.openide.util.NbBundle.getMessage(EditMaskOwnerEditor.class, "EditMaskOwnerEditor.filePathRadioButton.text")); // NOI18N
        filePathRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filePathRadioButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout commonPanelLayout = new javax.swing.GroupLayout(commonPanel);
        commonPanel.setLayout(commonPanelLayout);
        commonPanelLayout.setHorizontalGroup(
            commonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(commonPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(commonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(intNumberRadioButton)
                    .addComponent(realNumberRadioButton)
                    .addComponent(stringRadioButton)
                    .addComponent(dateTimeRadioButton)
                    .addComponent(timeIntervalRadioButton)
                    .addComponent(listRadioButton)
                    .addComponent(constSetRadioButton)
                    .addComponent(booleanRadioButton)
                    .addComponent(filePathRadioButton))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        commonPanelLayout.setVerticalGroup(
            commonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(commonPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(intNumberRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(realNumberRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(stringRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(dateTimeRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(timeIntervalRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(listRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(constSetRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(booleanRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(filePathRadioButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        editorPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        editorPanel.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(commonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(editorPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE))
                    .addComponent(useDefaultCheckBox))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(useDefaultCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(editorPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE)
                    .addComponent(commonPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void useDefaultCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_useDefaultCheckBoxItemStateChanged
        if (updating) {
            return;
        }
        if (useDefaultCheckBox.isSelected()) {
            if (defaultEditMask != null) {
                setCurrentEditMaskType(defaultEditMask.getType());
            }
        } else if (editMaskOwner.getEditMask() != null) {
            if (defaultEditMask != null) {
                setCurrentEditMaskType(editMaskOwner.getEditMask().getType());
            }
        } else {
            updateEnableState();
        }
    }//GEN-LAST:event_useDefaultCheckBoxItemStateChanged

    private void intNumberRadioButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_intNumberRadioButtonItemStateChanged
        if (updating) {
            return;
        }
        if (intNumberRadioButton.isSelected()) {
            setCurrentEditMaskType(EEditMaskType.INT);
        }
    }//GEN-LAST:event_intNumberRadioButtonItemStateChanged

    private void realNumberRadioButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_realNumberRadioButtonItemStateChanged
        if (updating) {
            return;
        }
        if (realNumberRadioButton.isSelected()) {
            setCurrentEditMaskType(EEditMaskType.NUM);
        }
    }//GEN-LAST:event_realNumberRadioButtonItemStateChanged

    private void stringRadioButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_stringRadioButtonItemStateChanged
        if (updating) {
            return;
        }
        if (stringRadioButton.isSelected()) {
            setCurrentEditMaskType(EEditMaskType.STR);
        }
    }//GEN-LAST:event_stringRadioButtonItemStateChanged

    private void dateTimeRadioButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_dateTimeRadioButtonItemStateChanged
        if (updating) {
            return;
        }
        if (dateTimeRadioButton.isSelected()) {
            setCurrentEditMaskType(EEditMaskType.DATE_TIME);
        }
    }//GEN-LAST:event_dateTimeRadioButtonItemStateChanged

    private void timeIntervalRadioButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_timeIntervalRadioButtonItemStateChanged
        if (updating) {
            return;
        }
        if (timeIntervalRadioButton.isSelected()) {
            setCurrentEditMaskType(EEditMaskType.TIME_INTERVAL);
        }
    }//GEN-LAST:event_timeIntervalRadioButtonItemStateChanged

    private void listRadioButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_listRadioButtonItemStateChanged
        if (updating) {
            return;
        }
        if (listRadioButton.isSelected()) {
            setCurrentEditMaskType(EEditMaskType.LIST);
        }
    }//GEN-LAST:event_listRadioButtonItemStateChanged

    private void constSetRadioButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_constSetRadioButtonItemStateChanged
        if (updating) {
            return;
        }
        if (constSetRadioButton.isSelected()) {
            setCurrentEditMaskType(EEditMaskType.ENUM);
        }
    }//GEN-LAST:event_constSetRadioButtonItemStateChanged

    private void booleanRadioButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_booleanRadioButtonItemStateChanged
        if (updating) {
            return;
        }
        if (booleanRadioButton.isSelected()) {
            setCurrentEditMaskType(EEditMaskType.BOOL);
        }
    }//GEN-LAST:event_booleanRadioButtonItemStateChanged

    private void filePathRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filePathRadioButtonActionPerformed
        if (updating) {
            return;
        }
        if (filePathRadioButton.isSelected()) {
            setCurrentEditMaskType(EEditMaskType.FILE_PATH);
        }
    }//GEN-LAST:event_filePathRadioButtonActionPerformed

    @Override
    protected void apply() {
//        if (editMaskOwner.isReadOnly() || defaultEditMask == null || currentEditor == null) {
        if (editMaskOwner.isReadOnly()) {
            return;
        }
        if (currentEditor != null) {
            currentEditor.stopEditing();
        }
        EditMask editMask = editMaskOwner.getEditMask();
        if (editMask == null) {
            if (!useDefaultCheckBox.isSelected()) {
                if (defaultEditMask != null) {
                    editMaskOwner.createEditMask(currentEditMaskType);
                    EditMask newEditMask = editMaskOwner.getEditMask();
                    assert newEditMask != null;
                    currentEditor.apply(newEditMask);
                }
            }
        } else {
            if (useDefaultCheckBox.isSelected()) {
                editMaskOwner.removeEditMask();
            } else {
                if (editMask.getType() != currentEditMaskType) {
                    if (defaultEditMask != null) {
                        editMaskOwner.createEditMask(currentEditMaskType);
                        EditMask newEditMask = editMaskOwner.getEditMask();
                        assert newEditMask != null;
                        currentEditor.apply(newEditMask);
                    }
                } else {
                    if (currentEditor != null) {
                        currentEditor.apply(editMask);
                    }
                }
            }
        }
    }

    @Override
    public String getTitle() {
        return "Edit Input Mask";
    }

    @Override
    public void update() {
    }

    @EditorFactoryRegistration
    public static final class Factory implements IEditorFactory<EditMaskOwner> {

        @Override
        public RadixObjectEditor<EditMaskOwner> newInstance(EditMaskOwner editMaskOwner) {
            return new EditMaskOwnerEditor(editMaskOwner);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton booleanRadioButton;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JPanel commonPanel;
    private javax.swing.JRadioButton constSetRadioButton;
    private javax.swing.JRadioButton dateTimeRadioButton;
    private javax.swing.JPanel editorPanel;
    private javax.swing.JRadioButton filePathRadioButton;
    private javax.swing.JRadioButton intNumberRadioButton;
    private javax.swing.JRadioButton listRadioButton;
    private javax.swing.JRadioButton realNumberRadioButton;
    private javax.swing.JRadioButton stringRadioButton;
    private javax.swing.JRadioButton timeIntervalRadioButton;
    private javax.swing.JCheckBox useDefaultCheckBox;
    // End of variables declaration//GEN-END:variables
}
