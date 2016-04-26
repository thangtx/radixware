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

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.ChangeListener;
import org.openide.actions.CopyAction;
import org.openide.util.ChangeSupport;
import org.openide.util.NbBundle;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.components.ExtendableTextField;
import org.radixware.kernel.common.components.ExtendableTextField.ExtendableTextChangeEvent;
import org.radixware.kernel.common.defs.dds.utils.DbNameUtils;
import org.radixware.kernel.designer.common.dialogs.components.ICheckableEditor;
import org.radixware.kernel.designer.common.dialogs.components.state.StateManager;
import org.radixware.kernel.designer.common.dialogs.utils.ClipboardUtils;
import org.radixware.kernel.designer.common.dialogs.utils.IAcceptResult;
import org.radixware.kernel.designer.common.dialogs.utils.IAdvancedAcceptor;

/**
 * Radix object name editor panel
 *
 */
public class DbNameEditPanel extends JPanel implements ICheckableEditor {

    private StateManager stateManager = new StateManager(this);
    private JButton copyButton;
    private IAdvancedAcceptor<String> nameAcceptor;
    public boolean allowEmptyName = false;

    public DbNameEditPanel() {
        initComponents();
        stateManager.ok();

        copyButton = extTextField.addButton();
        copyButton.setIcon(SystemAction.get(CopyAction.class).getIcon());
        copyButton.setToolTipText(NbBundle.getMessage(DbNameEditPanel.class, "COPY_TO_CLIPBOARD"));
        copyButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String dbName = getDbName();
                ClipboardUtils.copyToClipboard(dbName);
            }
        });

        ExtendableTextField.ExtendableTextChangeListener changeListener = new ExtendableTextField.ExtendableTextChangeListener() {
            @Override
            public void onEvent(ExtendableTextChangeEvent e) {
                changeSupport.fireChange();
                isComplete();
            }
        };

        extTextField.getChangeSupport().addEventListener(changeListener);
    }

//    @Override
//    public int getBaseline(int width, int height) {
//        return nameExtEditPanel.getBaseline(width, height);
//    }
    public boolean isEditable() {
        return !extTextField.getReadOnly();
    }

    public boolean isAllowEmptyName() {
        return allowEmptyName;
    }

    public void setAllowEmptyName(boolean allowEmptyName) {
        this.allowEmptyName = allowEmptyName;
    }

    public void setEditable(boolean editable) {
        extTextField.setReadOnly(!editable);
        isComplete();
    }

    @Override
    public boolean isEnabled() {
        return extTextField.isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        extTextField.setEnabled(enabled);
        isComplete();
    }

    @Override
    public boolean requestFocusInWindow() {
        return extTextField.requestFocusInWindow();
    }

    public void selectAll() {
        extTextField.selectAll();
    }

    public String getDbName() {
        return extTextField.getValue().toString();
    }

    public void setDbName(String dbName) {
        extTextField.setValue(dbName);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        extTextField = new org.radixware.kernel.common.components.ExtendableTextField();

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(extTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 422, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(extTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.radixware.kernel.common.components.ExtendableTextField extTextField;
    // End of variables declaration//GEN-END:variables
    private ChangeSupport changeSupport = new ChangeSupport(this);

    public final void addChangeListener(ChangeListener l) {
        changeSupport.addChangeListener(l);
    }

    public final void removeChangeListener(ChangeListener l) {
        changeSupport.removeChangeListener(l);
    }

    @Override
    public boolean isComplete() {
        String dbName = getDbName();

        copyButton.setEnabled(dbName != null && !dbName.isEmpty());

        if (isEnabled() && isEditable()) {

            if (!DbNameUtils.isCorrectDbName(dbName)) {
                if (!(allowEmptyName && "".equals(dbName))) {
                    stateManager.error(NbBundle.getMessage(DbNameEditPanel.class, "Error-Invalid-DbName"));
                    return false;
                }
            }

            if (nameAcceptor != null) {
                final IAcceptResult accepted = nameAcceptor.getResult(dbName);
                if (!accepted.isAccepted()) {
                    stateManager.error(accepted.getErrorMessage());
                    return false;
                }
            }
        }

        stateManager.ok();
        return true;
    }

    @Override
    public IAdvancedAcceptor<String> getNameAcceptor() {
        return nameAcceptor;
    }

    @Override
    public void setNameAcceptor(IAdvancedAcceptor<String> nameAcceptor) {
        this.nameAcceptor = nameAcceptor;
    }
}
