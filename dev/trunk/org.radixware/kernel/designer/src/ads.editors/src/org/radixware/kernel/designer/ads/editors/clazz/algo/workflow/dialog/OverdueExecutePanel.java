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

package org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog;

import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.*;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.common.editors.jml.JmlEditor;
import org.radixware.kernel.common.resources.RadixWareIcons;


public class OverdueExecutePanel extends EditorDialog.EditorPanel<AdsAppObject> {

    public OverdueExecutePanel(AdsAppObject node) {
        super(node);
        initComponents();
        processOverDue = node.getPropByName("processOverDue");
        add(editor);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.SystemColor.inactiveCaption));
        setMinimumSize(new java.awt.Dimension(500, 300));
        setPreferredSize(new java.awt.Dimension(500, 300));
        setRequestFocusEnabled(false);
        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    private final JmlEditor editor = new JmlEditor();
    private AdsAppObject.Prop processOverDue;
    
    @Override
    public void init() {
        editor.open(obj.getOnOverdue());
    }

    @Override
    public void apply() {
        processOverDue.setValue(obj.getOnOverdue().getItems().isEmpty() ? "0" : "1");
    }

    @Override
    public String getTitle() {
        return NbBundle.getMessage(getClass(), "CTL_OverdueExecute");
    }

    @Override
    public RadixIcon getIcon() {
        return RadixWareIcons.JML_EDITOR.EVENT_CODE;
    }
}