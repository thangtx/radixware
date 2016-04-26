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

/*
 * InheritableRestrictionsPanel.java
 *
 * Created on 20.08.2009, 11:05:20
 */
package org.radixware.kernel.designer.ads.editors.presentations;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObject.EEditState;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.designer.ads.common.lookup.AdsPresentationLookupSupport;


public class InheritableRestrictionsPanel extends javax.swing.JPanel {

    /** Creates new form InheritableRestrictionsPanel */
    public InheritableRestrictionsPanel() {
        initComponents();

        ActionListener inheritListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                InheritableRestrictionsPanel.this.onInheritanceChange();
            }
        };
        inheritCheck.addActionListener(inheritListener);
        defaultForeground = inheritCheck.getForeground();
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(InheritableRestrictionsPanel.class, "RestrictionsTip");
    }

    private void onInheritanceChange() {
        boolean isSelected = inheritCheck.isSelected();
        if (!isUpdate) {
            presentation.setEditState(EEditState.MODIFIED);
            presentation.setRestrictionsInherited(isSelected);
            update();
        }
    }
    private AdsPresentationDef presentation;
    private Color defaultForeground;

    public void open(final AdsPresentationDef presentation) {
        this.presentation = presentation;
        if (presentation != null) {
            isUpdate = true;
            if (presentation instanceof AdsEditorPresentationDef) {
                restrictionsPanel.open(presentation, AdsPresentationLookupSupport.Factory.newInstance((AdsEditorPresentationDef) presentation));
            } else {
                restrictionsPanel.open(presentation, AdsPresentationLookupSupport.Factory.newInstance((AdsSelectorPresentationDef) presentation));
            }

            boolean readonly = presentation.isReadOnly();
            boolean isInherited = presentation.isRestrictionsInherited();

            inheritCheck.setSelected(isInherited);
            //inheritCheck.setEnabled(!readonly && presentation.getBasePresentationId() != null);

            updateInheritCheckState();

            restrictionsPanel.setEnabled(!readonly && !isInherited);
            isUpdate = false;
        }
    }
    private boolean isUpdate = false;

    public void update() {
        if (presentation != null) {
            isUpdate = true;

            boolean readonly = presentation.isReadOnly();
            boolean isInhertited = presentation.isRestrictionsInherited();

            inheritCheck.setSelected(isInhertited);
            //inheritCheck.setEnabled(!readonly && presentation.getBasePresentationId() != null);
            updateInheritCheckState();

            restrictionsPanel.update();
            restrictionsPanel.setEnabled(!readonly && !isInhertited);

            isUpdate = false;
        }
    }

    private void updateInheritCheckState() {
        final boolean isInherited = presentation.isRestrictionsInherited();
        boolean erronious = isInherited
                && (presentation.getBasePresentationId() == null
                && presentation.getHierarchy().findOverwritten() == null);
        if (erronious) {
            inheritCheck.setForeground(Color.RED);
        } else {
            inheritCheck.setForeground(defaultForeground);
        }
        if (isInherited) {
            inheritCheck.setEnabled(!presentation.isReadOnly());
        } else {
            inheritCheck.setEnabled(!presentation.isReadOnly() && !erronious);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        inheritCheck = new javax.swing.JCheckBox();
        restrictionsPanel = new org.radixware.kernel.designer.ads.editors.exploreritems.AdsRestrictionsPanel();

        setLayout(new java.awt.GridBagLayout());

        inheritCheck.setText(org.openide.util.NbBundle.getMessage(InheritableRestrictionsPanel.class, "InheritConditions")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 0);
        add(inheritCheck, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 10, 10);
        add(restrictionsPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox inheritCheck;
    private org.radixware.kernel.designer.ads.editors.exploreritems.AdsRestrictionsPanel restrictionsPanel;
    // End of variables declaration//GEN-END:variables
}