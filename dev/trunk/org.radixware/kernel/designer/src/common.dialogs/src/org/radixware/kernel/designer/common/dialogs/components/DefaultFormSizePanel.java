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

package org.radixware.kernel.designer.common.dialogs.components;

import javax.swing.BoxLayout;
import javax.swing.SwingUtilities;
import org.radixware.kernel.common.defs.ads.IModalDisplayable;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;


public class DefaultFormSizePanel extends javax.swing.JPanel {

    /**
     * Creates new form DefaultFormSizePanel
     */
    private IModalDisplayable displayable;
    private SizeEditPanel explorerSizePanel = new SizeEditPanel();
    private SizeEditPanel webSizePanel = new SizeEditPanel();

    public DefaultFormSizePanel() {
        initComponents();
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(explorerSizePanel);
        add(webSizePanel);
    }

    public void open(IModalDisplayable displayable) {
        this.displayable = displayable;
        explorerSizePanel.open(displayable, ERuntimeEnvironmentType.EXPLORER);
        webSizePanel.open(displayable, ERuntimeEnvironmentType.WEB);
    }

    public void update() {
        if (displayable != null) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    explorerSizePanel.update();
                    webSizePanel.update();
                    revalidate();
                }
            });


        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(DefaultFormSizePanel.class, "DefaultFormSizePanel.border.title"))); // NOI18N
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
