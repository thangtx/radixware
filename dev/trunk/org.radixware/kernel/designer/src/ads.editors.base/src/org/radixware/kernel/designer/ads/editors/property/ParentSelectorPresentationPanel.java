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
 * ParentSelectopPresentationPanel.java
 *
 * Created on 03.09.2009, 17:03:53
 */
package org.radixware.kernel.designer.ads.editors.property;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.IModelPublishableProperty;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.types.Id;


public class ParentSelectorPresentationPanel extends javax.swing.JPanel {

    public ParentSelectorPresentationPanel() {
        initComponents();
        depParentSelectorPresentation.addChangeListener(chlParentSelectorPresentationChangedListener);
    }

    public void addChangeListener(ChangeListener listener) {
        depParentSelectorPresentation.addChangeListener(listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        depParentSelectorPresentation.removeChangeListener(listener);
    }
    boolean isMayModify = true;

    public void update() {
        if (!isMayModify || parentSelectorLookup == null) {
            return;
        }
        isMayModify = false;

        Id parentSelectorPresentationId = null;
        AdsSelectorPresentationDef presentation = null;
        AdsEntityObjectClassDef inheritCd = null;

        parentSelectorPresentationId = parentSelectorLookup.getParentSelectorPresentationId();
        presentation = parentSelectorLookup.findParentSelectorPresentation();
        AdsType at = prop.getTypedObject().getType().resolve((AdsDefinition) prop).get();
        if (at instanceof AdsClassType) {
            AdsClassType cl = (AdsClassType) at;
            AdsClassDef class_ = cl.getSource();
            if (class_ instanceof AdsEntityObjectClassDef) {
                inheritCd = (AdsEntityObjectClassDef) cl.getSource();
            }
        }


        depParentSelectorPresentation.setComboMode((AdsDefinition) prop);
        depParentSelectorPresentation.setComboBoxRenderer(new DefinitionLinkEditRendererForProperties());

        if (inheritCd != null) {
            depParentSelectorPresentation.setComboBoxValues(inheritCd.getPresentations().getSelectorPresentations().get(EScope.ALL), true);
        }

        depParentSelectorPresentation.open(presentation, parentSelectorPresentationId);
        depParentSelectorPresentation.setEnabled(!isReadOnly);
        isMayModify = true;

    }

    public void open(IModelPublishableProperty prop, IModelPublishableProperty.IParentSelectorPresentationLookup parentRefPropertyPresentation) {
        this.prop = prop;
        this.parentSelectorLookup = parentRefPropertyPresentation;
        update();
    }

    public void setReadOnly(boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
    }
    //KAA: required for filter parameter
    IModelPublishableProperty.IParentSelectorPresentationLookup parentSelectorLookup = null;
    IModelPublishableProperty prop = null;
    boolean isReadOnly = false;
    private ChangeListener chlParentSelectorPresentationChangedListener = new ChangeListener() {

        @Override
        public void stateChanged(ChangeEvent e) {
            if (isReadOnly || !isMayModify) {
                return;
            }
            if (parentSelectorLookup == null) {
                return;
            }
            parentSelectorLookup.setParentSelectorPresentationId(depParentSelectorPresentation.getDefinitionId());
            //updateParentTitlePanel();
            //updateRestricionPanel();
        }
    };

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel4 = new javax.swing.JLabel();
        depParentSelectorPresentation = new org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel();

        jLabel4.setText(org.openide.util.NbBundle.getMessage(ParentSelectorPresentationPanel.class, "ParentSelectorPresentationPanel.jLabel4.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(depParentSelectorPresentation, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(depParentSelectorPresentation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel depParentSelectorPresentation;
    private javax.swing.JLabel jLabel4;
    // End of variables declaration//GEN-END:variables
}
