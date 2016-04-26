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

package org.radixware.kernel.designer.common.editors.sqml.editors;

import java.util.LinkedList;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EPidTranslationMode;
import org.radixware.kernel.common.sqml.tags.IEntityRefTag;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.utils.ModalObjectEditor;


public class EntityRefEditPanel extends ModalObjectEditor<IEntityRefTag> {

    /**
     * Creates new form EntityRefTagEditor
     */
    public EntityRefEditPanel() {
        initComponents();

        rbSkColumns.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                setPnSkEnabled(rbSkColumns.isSelected());
                updateCorrect();
            }
        });

        setPnSkEnabled(rbSkColumns.isSelected());

        dlSk.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                updateCorrect();
            }
        });

        updateCorrect();

    }

    private void updateCorrect() {
        final boolean oldCorrect = isCorrect();
        if (rbSkColumns.isSelected()) {
            setCorrect(dlSk.getDefinition() != null);
        } else {
            setCorrect(true);
        }
        final boolean correct = isCorrect();
        if (oldCorrect != correct) {
            firePropertyChange("correct", oldCorrect, correct);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgTranslationMode = new javax.swing.ButtonGroup();
        rbPkColumns = new javax.swing.JRadioButton();
        rbPid = new javax.swing.JRadioButton();
        rbSkColumns = new javax.swing.JRadioButton();
        pnSk = new javax.swing.JPanel();
        lbSelectSk = new javax.swing.JLabel();
        dlSk = new org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel();

        setMinimumSize(new java.awt.Dimension(396, 158));

        bgTranslationMode.add(rbPkColumns);
        rbPkColumns.setSelected(true);
        rbPkColumns.setText(org.openide.util.NbBundle.getMessage(EntityRefEditPanel.class, "EntityRefEditPanel.rbPkColumns.text")); // NOI18N

        bgTranslationMode.add(rbPid);
        rbPid.setText(org.openide.util.NbBundle.getMessage(EntityRefEditPanel.class, "EntityRefEditPanel.rbPid.text")); // NOI18N

        bgTranslationMode.add(rbSkColumns);
        rbSkColumns.setText(org.openide.util.NbBundle.getMessage(EntityRefEditPanel.class, "EntityRefEditPanel.rbSkColumns.text")); // NOI18N

        lbSelectSk.setText(org.openide.util.NbBundle.getMessage(EntityRefEditPanel.class, "EntityRefEditPanel.lbSelectSk.text")); // NOI18N

        javax.swing.GroupLayout pnSkLayout = new javax.swing.GroupLayout(pnSk);
        pnSk.setLayout(pnSkLayout);
        pnSkLayout.setHorizontalGroup(
            pnSkLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnSkLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnSkLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dlSk, javax.swing.GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE)
                    .addComponent(lbSelectSk))
                .addContainerGap())
        );
        pnSkLayout.setVerticalGroup(
            pnSkLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnSkLayout.createSequentialGroup()
                .addComponent(lbSelectSk)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dlSk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnSk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rbPkColumns)
                    .addComponent(rbPid)
                    .addComponent(rbSkColumns))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(rbPkColumns)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbPid)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbSkColumns)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnSk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    @Override
    protected String getTitle() {
        return NbBundle.getMessage(EntityRefEditPanel.class, "entity-reference");
    }

    @Override
    protected void applyChanges() {
        if (rbPid.isSelected()) {
            getObject().setPidTranslationMode(EPidTranslationMode.AS_STR);
        } else if (rbPkColumns.isSelected()) {
            getObject().setPidTranslationMode(EPidTranslationMode.PRIMARY_KEY_PROPS);
        } else if (rbSkColumns.isSelected()) {
            getObject().setPidTranslationMode(EPidTranslationMode.SECONDARY_KEY_PROPS);
            getObject().setPidTranslationSecondaryKeyId(dlSk.getDefinitionId());
        }
    }

    @Override
    protected void afterOpen() {


        final DdsTableDef tableDef = getOpenInfo().getLookup().lookup(DdsTableDef.class);
        final List<Definition> secondaryKeys = new LinkedList<Definition>();
        DdsIndexDef selectedIndex = null;
        for (DdsIndexDef index : tableDef.getIndices().get(EScope.LOCAL_AND_OVERWRITE)) {
            if (index.isSecondaryKey()) {
                secondaryKeys.add(index);
                if (index.getId().equals(getObject().getPidTranslationSecondaryKeyId())) {
                    selectedIndex = index;
                }
            }
        }

        dlSk.open(ChooseDefinitionCfg.Factory.newInstance(secondaryKeys), selectedIndex, getObject().getPidTranslationSecondaryKeyId());

        switch (getObject().getPidTranslationMode()) {
            case AS_STR:
                rbPid.setSelected(true);
                break;
            case PRIMARY_KEY_PROPS:
                rbPkColumns.setSelected(true);
                break;
            case SECONDARY_KEY_PROPS:
                rbSkColumns.setSelected(true);
                break;
        }
    }

    private void setPnSkEnabled(boolean enabled) {
        dlSk.setEnabled(enabled);
    }

    @Override
    protected void setReadOnly(boolean readOnly) {
        final boolean enabled = !readOnly;
        rbPid.setEnabled(enabled);
        rbSkColumns.setEnabled(enabled);
        rbPkColumns.setEnabled(enabled);
        if (enabled && rbSkColumns.isSelected()) {
            dlSk.setEnabled(true);
        } else {
            dlSk.setEnabled(false);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgTranslationMode;
    private org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel dlSk;
    private javax.swing.JLabel lbSelectSk;
    private javax.swing.JPanel pnSk;
    private javax.swing.JRadioButton rbPid;
    private javax.swing.JRadioButton rbPkColumns;
    private javax.swing.JRadioButton rbSkColumns;
    // End of variables declaration//GEN-END:variables
}
