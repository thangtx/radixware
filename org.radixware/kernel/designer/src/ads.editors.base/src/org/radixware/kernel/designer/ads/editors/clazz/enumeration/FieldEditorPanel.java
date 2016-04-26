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

package org.radixware.kernel.designer.ads.editors.clazz.enumeration;

import java.awt.BorderLayout;
import javax.swing.Box;
import javax.swing.JLabel;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsEnumClassFieldDef;
import org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel;
import org.radixware.kernel.designer.common.dialogs.components.DescriptionPanel;


public class FieldEditorPanel extends javax.swing.JPanel {

    private final DescriptionPanel descriptionPanel = new DescriptionPanel();
    private final FieldValuesEditorPanel valuesPanel = new FieldValuesEditorPanel();
    private final DefinitionLinkEditPanel overwriteLinkPanel = new DefinitionLinkEditPanel();

    public FieldEditorPanel() {
        initComponents();

        final JLabel overLable = new JLabel(NbBundle.getMessage(FieldEditorPanel.class, "FieldEditorPanel.Overwrite-Label.text"));

        overwritePanel.add(overLable);
        overwritePanel.add(Box.createHorizontalStrut(4));
        overwritePanel.add(overwriteLinkPanel);

        centerPanel.add(valuesPanel, BorderLayout.CENTER);

        add(descriptionPanel, BorderLayout.PAGE_START);
    }
    private AdsEnumClassFieldDef field;

    public void open(AdsEnumClassFieldDef field) {
        this.field = field;
        descriptionPanel.open(field);
        valuesPanel.open(field);

        final SearchResult<AdsEnumClassFieldDef> result = field.getHierarchy().findOverwritten();
        if (result.isEmpty()) {
            overwritePanel.setVisible(false);
        } else {
            overwritePanel.setVisible(true);
            overwriteLinkPanel.open(result.get(), result.get().getId());
        }
    }

    public void update() {
        descriptionPanel.update();
        valuesPanel.update();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        centerPanel = new javax.swing.JPanel();
        overwritePanel = new javax.swing.JPanel();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8));
        setLayout(new java.awt.BorderLayout());

        centerPanel.setLayout(new java.awt.BorderLayout());

        overwritePanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 0, 8, 0));
        overwritePanel.setLayout(new javax.swing.BoxLayout(overwritePanel, javax.swing.BoxLayout.X_AXIS));
        centerPanel.add(overwritePanel, java.awt.BorderLayout.PAGE_START);

        add(centerPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel centerPanel;
    private javax.swing.JPanel overwritePanel;
    // End of variables declaration//GEN-END:variables
}
