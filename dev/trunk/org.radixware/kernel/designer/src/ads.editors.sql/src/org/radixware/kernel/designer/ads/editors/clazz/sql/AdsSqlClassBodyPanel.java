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

package org.radixware.kernel.designer.ads.editors.clazz.sql;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JSplitPane;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsSqlClassDef;
import org.radixware.kernel.common.defs.dds.utils.ISqlDef;
import org.radixware.kernel.designer.common.dialogs.utils.EditorOpenInfo;


public class AdsSqlClassBodyPanel extends javax.swing.JPanel {

    JSplitPane splitPane;
    AdsSqlClassCodeEditor codeEditor;
    AdsSqlClassTreePanel treePanel;

    /**
     * Creates new form SqlClassCodePanel
     */
    public AdsSqlClassBodyPanel() {
        initComponents();
        codeEditor = new AdsSqlClassCodeEditor();
        treePanel = new AdsSqlClassTreePanel(codeEditor);
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, treePanel, codeEditor);
        setLayout(new BorderLayout());
        add(splitPane, BorderLayout.CENTER);
        splitPane.setResizeWeight(0.33);

        codeEditor.setBorder(BorderFactory.createEtchedBorder());


    }

    public void update() {
        codeEditor.update();
        treePanel.update();
    }

    public void open(ISqlDef radixObject, EditorOpenInfo info) {
        codeEditor.open(radixObject, info);
        treePanel.open(radixObject, info);
        update();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
