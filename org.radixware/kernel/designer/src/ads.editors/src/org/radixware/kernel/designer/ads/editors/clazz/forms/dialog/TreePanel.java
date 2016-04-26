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

import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.ui.AdsAbstractUIDef;
import org.radixware.kernel.common.defs.ads.ui.AdsItemWidgetDef;


public class TreePanel extends EditorDialog.EditorPanel<AdsItemWidgetDef> {

    public TreePanel(AdsAbstractUIDef uiDef,AdsItemWidgetDef widget) {
        super(uiDef,widget);
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabPane = new javax.swing.JTabbedPane();

        setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.SystemColor.inactiveCaption));
        setMinimumSize(new java.awt.Dimension(400, 200));
        setPreferredSize(new java.awt.Dimension(400, 200));
        setRequestFocusEnabled(false);
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

        tabPane.setDoubleBuffered(true);
        add(tabPane);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane tabPane;
    // End of variables declaration//GEN-END:variables

    @Override
    public void init() {
        tabPane.addTab(org.openide.util.NbBundle.getMessage(TreePanel.class, "Columns.text"), new ColumnsPanel(uiDef,widget));
        tabPane.addTab(org.openide.util.NbBundle.getMessage(TreePanel.class, "Items.text"), new TreeItemsPanel(uiDef,widget));
    }

    @Override
    public String getTitle() {
        return NbBundle.getMessage(getClass(), "CTL_TreePanel");
    }
}
