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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.event.ChangeListener;

import org.openide.util.ChangeSupport;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;


public class IdSourcePanel extends javax.swing.JPanel {

    /** Creates new form IsSourcePanel */
    private JLabel label = new JLabel("Inherit Id from:");
    private boolean wasEdited = false;
    private DefinitionLinkEditPanel linkEditPanel = new DefinitionLinkEditPanel() {

        @Override
        protected void onChange() {
            if (!isUpdate) {
                Definition def = linkEditPanel.getDefinition();
                if (def instanceof AdsEnumItemDef) {
                    if (directUpdate) {
                        referent.setIdSourceItem((AdsEnumItemDef) def);
                    }
                    selectedItem = (AdsEnumItemDef) def;

                } else {
                    selectedItem = null;
                    if (directUpdate) {
                        referent.setIdSourceItem(null);
                    }
                }
                wasEdited = true;
                changeSupport.fireChange();
            }
        }

        @Override
        protected void onClear() {
            if (!isUpdate) {
                selectedItem = null;
                if (directUpdate) {
                    referent.setIdSourceItem(null);
                }
                wasEdited = true;
                changeSupport.fireChange();
            }
        }
    };
    private boolean directUpdate = true;
    private AdsDefinition referent;
    private boolean isReadOnly;
    private volatile boolean isUpdate = false;
    private AdsEnumItemDef selectedItem;

    public IdSourcePanel(boolean directUpdate) {
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        setLayout(layout);
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.1;
        c.weighty = 0.0;
        c.insets = new Insets(10, 10, 10, 10);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        layout.setConstraints(label, c);
        add(label);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridx = 1;
        c.weightx = 0.9;
        c.fill = GridBagConstraints.HORIZONTAL;
        layout.setConstraints(linkEditPanel, c);
        add(linkEditPanel);
        linkEditPanel.setClearable(true);
        this.directUpdate = directUpdate;

    }

    public IdSourcePanel() {
        this(true);
    }

    public void open(AdsDefinition referent) {
        this.referent = referent;
        update();
    }

    public void setReadOnly(boolean ro) {
        if (this.isReadOnly != ro) {
            this.isReadOnly = ro;
            update();
        }
    }

    private class MyCfg extends ChooseDefinitionCfg {

        public MyCfg() {
            super(referent, new VisitorProvider() {

                @Override
                public boolean isTarget(RadixObject radixObject) {
                    return radixObject instanceof AdsEnumItemDef && ((AdsEnumItemDef) radixObject).getOwnerEnum().isIdEnum();
                }
            });
        }
    }
    private final ChangeSupport changeSupport = new ChangeSupport(this);

    public void removeChangeListener(ChangeListener listener) {
        changeSupport.removeChangeListener(listener);
    }

    public void addChangeListener(ChangeListener listener) {
        changeSupport.addChangeListener(listener);
    }

    public void apply() {
        if (wasEdited && !isReadOnly) {
            referent.setIdSourceItem(selectedItem);
        }
    }

    public void update() {
        isUpdate = true;
        if (directUpdate || selectedItem == null) {
            selectedItem = referent.findIdSourceItem();
        }
        try {
            Id id = null;
            if (selectedItem != null) {
                id = selectedItem.getId();
            } else {
                id = referent.getIdSourceItemId();
            }
            linkEditPanel.setEnabled(!isReadOnly);
            linkEditPanel.open(new MyCfg(), selectedItem, id);
        } finally {
            isUpdate = false;
        }
    }

    public AdsEnumItemDef getSelectedItem() {
        return selectedItem;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
