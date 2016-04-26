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
 * AdsObjectPropertPresentationPanel.java
 *
 * Created on May 17, 2010, 2:34:06 PM
 */
package org.radixware.kernel.designer.ads.editors.property;

import java.awt.BorderLayout;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsClassCatalogDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.IAdsPresentableProperty;
import org.radixware.kernel.common.defs.ads.clazz.presentation.ObjectPropertyPresentation;
import org.radixware.kernel.common.defs.ads.clazz.presentation.PropertyEditOptions;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.ObjectType;
import org.radixware.kernel.designer.ads.editors.common.CreatePresentationsListPanel;


public class AdsObjectPropertPresentationPanel extends javax.swing.JPanel {

    private CreatePresentationsListPanel createList = new CreatePresentationsListPanel();

    public AdsObjectPropertPresentationPanel() {
        initComponents();
        panelForCreateList.setLayout(new BorderLayout());
        panelForCreateList.add(createList, BorderLayout.CENTER);
        //lepObjectCreationPresentatin.addChangeListener(chlObjectCreationPresentatinListener);
        lepObjectClassCatalog.addChangeListener(chlObjectClassCatalogListener);

    }
    boolean isMayModify = true;
    boolean isReadOnly = false;
    PropertyEditOptions propertyEditOptions;
    AdsPropertyDef prop = null;
    // AdsTypeDeclaration type = null;
    ObjectPropertyPresentation objectPropertyPresentation = null;

    public void open(AdsPropertyDef prop/*, PropertyEditOptions propertyEditOptions, AdsTypeDeclaration type*/) {
        //  this.propertyEditOptions = propertyEditOptions;
        this.prop = prop;
        //  this.type = type;
        update();
    }

    public void setReadOnly(boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
    }
//    private ChangeListener chlObjectCreationPresentatinListener = new ChangeListener() {
//
//        @Override
//        public void stateChanged(ChangeEvent e) {
//            if (!isMayModify || isReadOnly) {
//                return;
//            }
//            if (objectPropertyPresentation != null) {
//                objectPropertyPresentation.setObjectCreationEditorPresentationId(lepObjectCreationPresentatin.getDefinitionId());
//            }
//            update();
//        }
//    };
    private ChangeListener chlObjectClassCatalogListener = new ChangeListener() {

        @Override
        public void stateChanged(ChangeEvent e) {
            if (!isMayModify || isReadOnly) {
                return;
            }
            if (objectPropertyPresentation != null) {
                objectPropertyPresentation.setCreationClassCatalogId(lepObjectClassCatalog.getDefinitionId());
            }
            update();
        }
    };

    public void update() {

        if (!isMayModify) {
            return;
        }

        isMayModify = false;
        setReadOnly(false);



        IAdsPresentableProperty sProp = (IAdsPresentableProperty) prop;
        if (sProp.getPresentationSupport() != null && (sProp.getPresentationSupport().getPresentation() instanceof ObjectPropertyPresentation)) {

            DefinitionLinkEditRendererForProperties definitionLinkEditRendererForProperties = new DefinitionLinkEditRendererForProperties();
            //lepObjectCreationPresentatin.setComboBoxRenderer(definitionLinkEditRendererForProperties);
            lepObjectClassCatalog.setComboBoxRenderer(definitionLinkEditRendererForProperties);

            //lepObjectCreationPresentatin.setComboMode(prop);
            lepObjectClassCatalog.setComboMode(prop);

            createList.update();
            //lepObjectCreationPresentatin.update();
            lepObjectClassCatalog.update();

            AdsType currType = prop.getValue().getType().resolve(prop).get();
            if (currType instanceof ObjectType) {
                objectPropertyPresentation = (ObjectPropertyPresentation) sProp.getPresentationSupport().getPresentation();
                ObjectType currObjectType = (ObjectType) currType;
                ExtendableDefinitions<AdsEditorPresentationDef> presentations =
                        currObjectType.getSource().getPresentations().getEditorPresentations();
                List<AdsEditorPresentationDef> list = presentations.get(EScope.ALL);
//                lepObjectEditorPresentatin.setComboBoxValues(list, true);
//                lepObjectEditorPresentatin.open(objectPropertyPresentation.getEditOptions().findObjectEditorPresentation(),
//                        objectPropertyPresentation.getEditOptions().getObjectEditorPresentationId());
//                lepObjectEditorPresentatin.setEnabled(!isReadOnly);
                createList.setEnabled(!isReadOnly);
                // lepObjectCreationPresentatin.setComboBoxValues(list, true);
                createList.open(objectPropertyPresentation);
//                lepObjectCreationPresentatin.open(objectPropertyPresentation.findObjectCreationEditorPresentation(),
//                        objectPropertyPresentation.getObjectCreationEditorPresentationId());
//                lepObjectCreationPresentatin.setEnabled(!isReadOnly);

                List<AdsClassCatalogDef> list2 = currObjectType.getSource().getPresentations().getClassCatalogs().get(EScope.ALL);

                lepObjectClassCatalog.setComboBoxValues(list2, true);
                lepObjectClassCatalog.open(objectPropertyPresentation.findCreationClassCatalog().get(),
                        objectPropertyPresentation.getCreationClassCatalogId());
                lepObjectClassCatalog.setEnabled(!isReadOnly);
            } else {
//                lepObjectEditorPresentatin.setEnabled(false);
                createList.setEnabled(false);
                //lepObjectCreationPresentatin.setEnabled(false);
            }

        }

        isMayModify = true;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lepObjectClassCatalog = new org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel();
        jLabel2 = new javax.swing.JLabel();
        panelForCreateList = new javax.swing.JPanel();

        jLabel2.setText(org.openide.util.NbBundle.getMessage(AdsObjectPropertPresentationPanel.class, "AdsObjectPropertPresentationPanel.jLabel2.text")); // NOI18N

        javax.swing.GroupLayout panelForCreateListLayout = new javax.swing.GroupLayout(panelForCreateList);
        panelForCreateList.setLayout(panelForCreateListLayout);
        panelForCreateListLayout.setHorizontalGroup(
            panelForCreateListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 584, Short.MAX_VALUE)
        );
        panelForCreateListLayout.setVerticalGroup(
            panelForCreateListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 77, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lepObjectClassCatalog, javax.swing.GroupLayout.DEFAULT_SIZE, 441, Short.MAX_VALUE))
            .addComponent(panelForCreateList, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(panelForCreateList, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(lepObjectClassCatalog, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel2;
    private org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel lepObjectClassCatalog;
    private javax.swing.JPanel panelForCreateList;
    // End of variables declaration//GEN-END:variables
}
