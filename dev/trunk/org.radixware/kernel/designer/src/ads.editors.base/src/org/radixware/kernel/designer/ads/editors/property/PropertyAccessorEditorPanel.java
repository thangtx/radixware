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

package org.radixware.kernel.designer.ads.editors.property;

import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.designer.ads.editors.AdsSourceCodePanel;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;


final class PropertyAccessorEditorPanel extends javax.swing.JPanel {

    public PropertyAccessorEditorPanel() {
        initComponents();
    }

    @Override
    public boolean requestFocusInWindow() {
        return getSourceCodePanel().requestFocusInWindow();
    }

    public void open(OpenInfo info, AdsPropertyDef.Accessor accessor) {
        if (accessor != null) {
            getSourceCodePanel().open(accessor.getSources(), info, accessor.isReadOnly());
        }
    }

    private AdsSourceCodePanel getSourceCodePanel() {
        return (AdsSourceCodePanel) sourceCodePanel;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sourceCodePanel = new AdsSourceCodePanel();

        setLayout(new java.awt.BorderLayout());

        sourceCodePanel.setLayout(null);
        add(sourceCodePanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel sourceCodePanel;
    // End of variables declaration//GEN-END:variables
}
