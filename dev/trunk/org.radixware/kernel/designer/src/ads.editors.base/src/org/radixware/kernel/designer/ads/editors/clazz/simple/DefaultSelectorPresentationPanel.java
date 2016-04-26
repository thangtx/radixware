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
 * DefaultSelectionPresentationPanel111.java
 *
 * Created on Jun 8, 2009, 4:24:23 PM
 */
package org.radixware.kernel.designer.ads.editors.clazz.simple;

import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EntityPresentations;
import org.radixware.kernel.common.types.Id;



public class DefaultSelectorPresentationPanel extends javax.swing.JPanel {

    private EntityPresentations entityPresentations;
    private boolean isUpdating;

    /** Creates new form DefaultSelectorPresentationPanel */
    public DefaultSelectorPresentationPanel(EntityPresentations entityPresentations) {
        super();
        this.entityPresentations = entityPresentations;
        initComponents();
        initAdditionalComponents();
    }

    private void initAdditionalComponents() {
        definitionLinkEditPanel1.setClearable(true);
    }

    public void open() {
        
        final List<AdsSelectorPresentationDef> itemsList = entityPresentations.getSelectorPresentations().get(EScope.ALL);

        definitionLinkEditPanel1.setComboMode(entityPresentations);      
        definitionLinkEditPanel1.setComboBoxValues(itemsList, true);
        
        final Id currentSelectorPresentationId = entityPresentations.getDefaultSelectorPresentationId();
        Definition selectedDef = null;
        for (AdsSelectorPresentationDef xSelectorPresentationDef : itemsList){
            if (xSelectorPresentationDef.getId().equals(currentSelectorPresentationId)){
                selectedDef = xSelectorPresentationDef;
                break;
            }
        }
        definitionLinkEditPanel1.open(selectedDef, currentSelectorPresentationId);
        definitionLinkEditPanel1.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if (isUpdating){
                    return;
                }
                entityPresentations.setDefaultSelectorPresentationId(definitionLinkEditPanel1.getDefinitionId());
            }
        });
    }

    public void update() {
        isUpdating = true;
        definitionLinkEditPanel1.update();
        definitionLinkEditPanel1.setEnabled(!entityPresentations.isReadOnly());
        isUpdating = false;
    }

    public void setReadonly(boolean readonly){
        definitionLinkEditPanel1.setEnabled(!readonly);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        definitionLinkEditPanel1 = new org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel();
        jLabel1 = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(32767, 43));

        jLabel1.setText(org.openide.util.NbBundle.getMessage(DefaultSelectorPresentationPanel.class, "DefaultSelectorPresentationPanel.jLabel1.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(definitionLinkEditPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(definitionLinkEditPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel definitionLinkEditPanel1;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}