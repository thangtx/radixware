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
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsSqlClassDef;
import org.radixware.kernel.designer.common.dialogs.utils.EditorOpenInfo;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;


public class AdsSqlClassEditorPanel extends javax.swing.JPanel {

    AdsSqlClassDescriptionPanel descriptionPanel = new AdsSqlClassDescriptionPanel();
    AdsSqlClassBodyPanel bodyPanel = new AdsSqlClassBodyPanel();

    /**
     * Creates new form AdsClassEditorPanel
     */
    public AdsSqlClassEditorPanel() {
        initComponents();
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
//        descriptionPanel.setMinimumSize(new Dimension(0, 150));
//        descriptionPanel.setPreferredSize(new Dimension(0, 150));
        setLayout(new BorderLayout(12, 12));
        add(descriptionPanel, BorderLayout.NORTH);
        add(bodyPanel, BorderLayout.CENTER);
    }

    public void update() {
        descriptionPanel.update();
        bodyPanel.update();
    }

    public void open(AdsSqlClassDef radixObject, OpenInfo info) {
        EditorOpenInfo objectInfo = new EditorOpenInfo(radixObject.isReadOnly(), info.getLookup());
        descriptionPanel.open(radixObject, objectInfo);
        bodyPanel.open(radixObject, objectInfo);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
