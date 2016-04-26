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

package org.radixware.kernel.designer.dds.editors.scripts;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.dds.DdsScript;
import org.radixware.kernel.common.repository.dds.DdsScripts;
import org.radixware.kernel.common.repository.dds.DdsSegment;
import org.radixware.kernel.common.repository.dds.DdsUpdateInfo;
import org.radixware.kernel.common.repository.dds.DdsUpdateInfo.BaseLayerInfo;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.common.annotations.registrators.EditorFactoryRegistration;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.editors.RadixObjectEditor;
import org.radixware.kernel.designer.common.general.editors.IEditorFactory;


public class DdsScriptsEditor extends RadixObjectEditor<DdsScripts> implements
        UpdateInfoList.EditingUpdateInfoChangeListener, BaseLayerInfoList.EditingBaseLayerChangeListener {

    private final String NOT_DEFINED = "<Not defined>";
    private final UpdateInfoList updateInfoList;
    private final BaseLayerInfoList baseLayerInfoList;
    private DdsUpdateInfo currentUpdateInfo = null;
    private DdsUpdateInfo.BaseLayerInfo currentLayerInfo = null;
    private volatile boolean updating = false;

    private class LayerPopupMenu extends JPopupMenu {

        public LayerPopupMenu() {
            this.add(new AbstractAction("Delete", null) {
                @Override
                public void actionPerformed(ActionEvent e) {
                    deleteSelectedLayerInfo();
                }
            });
        }
    }

    /**
     * Creates new form DdsScriptsEditor
     */
    protected DdsScriptsEditor(DdsScripts ddsScripts) {
        super(ddsScripts);
        initComponents();

        fileNameTextField.setEditable(false);
        releaseNumberTextField.setEditable(false);

        editButton.setIcon(RadixWareIcons.EDIT.EDIT.getIcon());
        deleteButton.setIcon(RadixWareIcons.DELETE.DELETE.getIcon());

        updateInfoList = new UpdateInfoList(ddsScripts);
        updateInfoScrollPane.setViewportView(updateInfoList);
        updateInfoList.addEditingUpdateInfoChangeListener(this);

        baseLayerInfoList = new BaseLayerInfoList();
        layerScrollPane.setViewportView(baseLayerInfoList);
        baseLayerInfoList.addEditingBaseLayerChangeListener(this);

        baseLayerInfoList.add(new LayerPopupMenu());

        AbstractAction action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedLayerInfo();
            }
        };
        InputMap inputMap = baseLayerInfoList.getInputMap(JList.WHEN_FOCUSED);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "DELETE");
        baseLayerInfoList.getActionMap().put("DELETE", action);

//        String[] fileNames = new String[ddsScripts.getUpdatesInfo().size()];
//        int moo = 0;
//        for (DdsUpdateInfo info : ddsScripts.getUpdatesInfo()) {
//            fileNames[moo++] = info.getUpdateFileName();
//        }

        lastScriptComboBox.setModel(new DefaultComboBoxModel(new String[]{}));
//
//        ddsScripts.getUpdatesInfo().addEditStateListener(new EditStateChangeListener() {
//
//            @Override
//            public void onEvent(EditStateChangedEvent e) {
//                updating = true;
//                String last = (String)lastScriptComboBox.getSelectedItem();
//                String[] fileNames = new String[getRadixObject().getUpdatesInfo().size()];
//                int moo = 0;
//                for (DdsUpdateInfo info : getRadixObject().getUpdatesInfo()) {
//                    fileNames[moo++] = info.getUpdateFileName();
//                }
//                lastScriptComboBox.setModel(new DefaultComboBoxModel(fileNames));
//                lastScriptComboBox.setSelectedItem(last);
//                updating = false;
//            }
//        });

        if (updateInfoList.getModel().getSize() > 0) {
            updateInfoList.setSelectedIndex(0);
        }
        initButton(deleteButton, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "DELETE");
        updateEnableState();
    }

    private void initButton(final JButton button, KeyStroke keyStroke, String key) {
        AbstractAction action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (button.isEnabled()) {
                    button.doClick();
                }
            }
        };
        InputMap inputMap = updateInfoList.getInputMap(JList.WHEN_FOCUSED);
        inputMap.put(keyStroke, key);
        updateInfoList.getActionMap().put(key, action);
    }

    private void updateEnableState() {
        boolean editable = !getRadixObject().isReadOnly();
        boolean exist = currentUpdateInfo != null;
        deleteButton.setEnabled(editable && exist);
        editButton.setEnabled(exist);
        bcCheckBox.setEnabled(editable && exist);
        fileNameTextField.setEnabled(editable && exist);
        exist = currentLayerInfo != null;
        releaseNumberTextField.setEnabled(editable && exist);
        lastScriptComboBox.setEnabled(editable && exist);
    }

    private void setUpdateInfo(DdsUpdateInfo updateInfo) {
        currentUpdateInfo = updateInfo;
        update();
    }

    private void setBaseLayerInfo(BaseLayerInfo layerInfo) {
        currentLayerInfo = layerInfo;
        update2();
    }

    private void deleteSelectedLayerInfo() {
        if (!DialogUtils.messageConfirmation(NbBundle.getBundle(DdsScriptsEditor.class).getString("REMOVE_LAYER_CONFIRM"))) {
            return;
        }
        BaseLayerInfo layerInfo = baseLayerInfoList.getSelectedBaseLayerInfo();
        if (layerInfo == null) {
            return;
        }
        int idx = baseLayerInfoList.getSelectedIndex();
        boolean last = idx == baseLayerInfoList.getModel().getSize() - 1;
        int cnt = baseLayerInfoList.getModel().getSize();
        currentUpdateInfo.getBaseLayersInfo().remove(layerInfo);

        baseLayerInfoList.update();
        baseLayerInfoList.clearSelection();
        if (last) {
            if (cnt > 1) {
                baseLayerInfoList.setSelectedIndex(idx - 1);
            }
        } else {
            baseLayerInfoList.setSelectedIndex(idx);
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

        jSplitPane1 = new javax.swing.JSplitPane();
        updateInfoScrollPane = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        bcCheckBox = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        fileNameTextField = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        layerScrollPane = new javax.swing.JScrollPane();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        releaseNumberTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        lastScriptComboBox = new javax.swing.JComboBox();
        jToolBar1 = new javax.swing.JToolBar();
        editButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();

        jSplitPane1.setDividerLocation(100);
        jSplitPane1.setLeftComponent(updateInfoScrollPane);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(DdsScriptsEditor.class, "DdsScriptsEditor.jPanel2.border.title"))); // NOI18N

        bcCheckBox.setText(org.openide.util.NbBundle.getMessage(DdsScriptsEditor.class, "DdsScriptsEditor.bcCheckBox.text")); // NOI18N
        bcCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bcCheckBoxActionPerformed(evt);
            }
        });

        jLabel1.setText(org.openide.util.NbBundle.getMessage(DdsScriptsEditor.class, "DdsScriptsEditor.jLabel1.text")); // NOI18N

        fileNameTextField.setText(org.openide.util.NbBundle.getMessage(DdsScriptsEditor.class, "DdsScriptsEditor.fileNameTextField.text")); // NOI18N

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(DdsScriptsEditor.class, "DdsScriptsEditor.jPanel3.border.title"))); // NOI18N

        jSplitPane2.setDividerLocation(100);
        jSplitPane2.setLeftComponent(layerScrollPane);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(DdsScriptsEditor.class, "DdsScriptsEditor.jPanel4.border.title"))); // NOI18N

        jLabel2.setText(org.openide.util.NbBundle.getMessage(DdsScriptsEditor.class, "DdsScriptsEditor.jLabel2.text")); // NOI18N

        releaseNumberTextField.setText(org.openide.util.NbBundle.getMessage(DdsScriptsEditor.class, "DdsScriptsEditor.releaseNumberTextField.text")); // NOI18N

        jLabel3.setText(org.openide.util.NbBundle.getMessage(DdsScriptsEditor.class, "DdsScriptsEditor.jLabel3.text")); // NOI18N

        lastScriptComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        lastScriptComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                lastScriptComboBoxItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(releaseNumberTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                    .addComponent(lastScriptComboBox, 0, 265, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(releaseNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(lastScriptComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(273, Short.MAX_VALUE))
        );

        jSplitPane2.setRightComponent(jPanel4);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 552, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 356, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bcCheckBox)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fileNameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(bcCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(fileNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jSplitPane1.setRightComponent(jPanel1);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        editButton.setText(org.openide.util.NbBundle.getMessage(DdsScriptsEditor.class, "DdsScriptsEditor.editButton.text")); // NOI18N
        editButton.setToolTipText(org.openide.util.NbBundle.getMessage(DdsScriptsEditor.class, "DdsScriptsEditor.editButton.toolTipText")); // NOI18N
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(editButton);

        deleteButton.setText(org.openide.util.NbBundle.getMessage(DdsScriptsEditor.class, "DdsScriptsEditor.deleteButton.text")); // NOI18N
        deleteButton.setToolTipText(org.openide.util.NbBundle.getMessage(DdsScriptsEditor.class, "DdsScriptsEditor.deleteButton.toolTipText")); // NOI18N
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(deleteButton);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 730, Short.MAX_VALUE)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 730, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 493, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        if (!DialogUtils.messageConfirmation(NbBundle.getBundle(DdsScriptsEditor.class).getString("REMOVE_SCRIPT_CONFIRM"))) {
            return;
        }
        DdsUpdateInfo updateInfo = updateInfoList.getSelectedUpdateInfo();
        if (updateInfo == null) {
            return;
        }
        int idx = updateInfoList.getSelectedIndex();
        boolean last = idx == updateInfoList.getModel().getSize() - 1;
        int cnt = updateInfoList.getModel().getSize();
        getRadixObject().getUpdatesInfo().remove(updateInfo);

        updateInfoList.update();
        updateInfoList.clearSelection();
        if (last) {
            if (cnt > 1) {
                updateInfoList.setSelectedIndex(idx - 1);
            }
        } else {
            updateInfoList.setSelectedIndex(idx);
        }
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        if (currentUpdateInfo != null) {
            DdsScript script = currentUpdateInfo.findScript();
            if (script != null) {
                DialogUtils.editFile(script.getFile());
            }
        }
    }//GEN-LAST:event_editButtonActionPerformed

    private void bcCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bcCheckBoxActionPerformed
        currentUpdateInfo.setBackwardCompatible(bcCheckBox.isSelected());
    }//GEN-LAST:event_bcCheckBoxActionPerformed

    private void lastScriptComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_lastScriptComboBoxItemStateChanged
        if (!updating) {
            if (((String) lastScriptComboBox.getSelectedItem()).equals(NOT_DEFINED)) {
                currentLayerInfo.setLastUpdateFileName(null);
            } else {
                currentLayerInfo.setLastUpdateFileName((String) lastScriptComboBox.getSelectedItem());
            }
        }
    }//GEN-LAST:event_lastScriptComboBoxItemStateChanged

    @Override
    public void update() {
        updating = true;
        if (currentUpdateInfo == null) {
            bcCheckBox.setSelected(false);
            fileNameTextField.setText("");
            baseLayerInfoList.setDdsUpdateInfo(null);
        } else {
            bcCheckBox.setSelected(currentUpdateInfo.isBackwardCompatible());
            fileNameTextField.setText(currentUpdateInfo.getUpdateFileName());
            baseLayerInfoList.setDdsUpdateInfo(currentUpdateInfo);
            if (currentUpdateInfo.getBaseLayersInfo().size() > 0) {
                baseLayerInfoList.setSelectedIndex(0);
            }
        }
        updating = false;
    }

    public void update2() {
        updating = true;
        if (currentLayerInfo == null) {
            releaseNumberTextField.setText("");
            lastScriptComboBox.setModel(new DefaultComboBoxModel(new String[]{NOT_DEFINED}));
            lastScriptComboBox.setSelectedIndex(0);
        } else {
            releaseNumberTextField.setText(currentLayerInfo.getReleaseNumber());

            Layer layer = currentLayerInfo.findBaseLayer();
            if (layer != null) {
                String fileName = currentLayerInfo.getLastUpdateFileName();
                DdsSegment segment = (DdsSegment) layer.getDds();
                String[] fileNames = new String[segment.getScripts().getDbScripts().getUpgradeScripts().size() + 1];
                fileNames[0] = NOT_DEFINED;
                int moo = 1;
                boolean exist = false;
                for (DdsScript script : segment.getScripts().getDbScripts().getUpgradeScripts().list()) {
                    fileNames[moo++] = script.getFileBaseName();
                    if (!exist && script.getFileBaseName().equals(fileName)) {
                        exist = true;
                    }
                }
                lastScriptComboBox.setModel(new DefaultComboBoxModel(fileNames));
                if (exist) {
                    lastScriptComboBox.setSelectedItem(fileName);
                } else {
                    lastScriptComboBox.setSelectedIndex(0);
                }
            } else {
                lastScriptComboBox.setModel(new DefaultComboBoxModel(new String[]{NOT_DEFINED}));
                lastScriptComboBox.setSelectedIndex(0);
            }
        }
        updating = false;
    }

    @Override
    public void editingUpdateInfoChanged(DdsUpdateInfo updateInfo) {
        setUpdateInfo(updateInfo);
        updateEnableState();
    }

    @Override
    public void editingBaseLayerChanged(BaseLayerInfo baseLayer) {
        setBaseLayerInfo(baseLayer);
        updateEnableState();
    }

    @EditorFactoryRegistration
    public static final class Factory implements IEditorFactory<DdsScripts> {

        @Override
        public RadixObjectEditor<DdsScripts> newInstance(DdsScripts ddsScripts) {
            return new DdsScriptsEditor(ddsScripts);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox bcCheckBox;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton editButton;
    private javax.swing.JTextField fileNameTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JComboBox lastScriptComboBox;
    private javax.swing.JScrollPane layerScrollPane;
    private javax.swing.JTextField releaseNumberTextField;
    private javax.swing.JScrollPane updateInfoScrollPane;
    // End of variables declaration//GEN-END:variables
}
