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
 * RightsInheritancePanel.java
 *
 * Created on Oct 25, 2010, 2:14:05 PM
 */
package org.radixware.kernel.designer.ads.editors.presentations;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import javax.swing.JList;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.common.AdsFilters;
import org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditRenderer;


public class RightsInheritancePanel extends javax.swing.JPanel {

    private ItemListener comboListener = new ItemListener() {

        @Override
        public void itemStateChanged(ItemEvent e) {
            RightsInheritancePanel.this.onModeChange();
        }
    };

    private void onModeChange() {
        if (!isUpdate) {
            Object selected = modeEditor.getSelectedItem();
            assert (selected instanceof String);
            presentation.setRightsInheritanceMode(EEditorPresentationRightsInheritanceMode.getForName((String) selected));

            if (presentation.getRightInheritanceMode().equals(EEditorPresentationRightsInheritanceMode.FROM_DEFINED)) {
                List<AdsEditorPresentationDef> pr = presentation.getOwnerClass().getPresentations().getEditorPresentations().get(EScope.ALL, new DefinedFilter(presentation)); 
                if (pr.contains(presentation)) {
                    boolean removed = false;
                    int i = 0;
                    while (!removed && i <= pr.size() - 1) {
                        if (pr.get(i).equals(presentation)) {
                            removed = true;
                            pr.remove(i);
                        } else {
                            i++;
                        }
                    }
                }
                definedEditor.setComboBoxValues(pr, false);
                if (pr.size() > 0){
                    presentation.setRightsSourceEditorPresentationId(pr.get(0).getId());
                } else {
                    presentation.setRightsSourceEditorPresentationId(null);
                }
            }
            update();
        }
    }

    /** Creates new form RightsInheritancePanel */
    public RightsInheritancePanel() {
        initComponents();
        modeEditor.setModel(new RightInheritanceComboModel(true));
        modeEditor.setPrototypeDisplayValue(EEditorPresentationRightsInheritanceMode.FROM_REPLACED.getName());
        modeEditor.setEditable(false);
        modeEditor.addItemListener(comboListener);

        givenPresentationLabel.setVisible(false);
        definedEditor.setVisible(false);
        definedEditor.setClearable(false);
        definedEditor.setComboMode();
        definedEditor.setComboBoxRenderer(new DefinitionLinkEditRenderer(){

            @Override
            public Component getListCellRendererComponent(JList list, Object object, int index, boolean isSelected, boolean hasFocus) {
                Component superComponent = super.getListCellRendererComponent(list, object, index, isSelected, hasFocus);
                if (object != null && object instanceof RadixObject){
                    ((javax.swing.JLabel) superComponent).setText(((RadixObject) object).getQualifiedName());
                }
                return superComponent;
            }

        });
        definedEditor.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if (!isUpdate){
                    presentation.setRightsSourceEditorPresentationId(definedEditor.getDefinitionId()); 
                }
            }
        });
    }
    private AdsEditorPresentationDef presentation;
    private boolean isUpdate = false;

    public void open(final AdsEditorPresentationDef presentation) {
        this.presentation = presentation;
        if (presentation != null){
            update();
        }
    }

    public void update() {
        this.isUpdate = true;
        final boolean isInherited = presentation.isRightsInheritanceModeInherited();
        final boolean readonly = presentation.isReadOnly();

        EEditorPresentationRightsInheritanceMode mode = presentation.getRightInheritanceMode();

        modeEditor.setModel(new RightInheritanceComboModel(presentation.getReplacedEditorPresentationId() != null));
        modeEditor.getModel().setSelectedItem(mode);
        modeEditor.setEnabled(!readonly && !isInherited);

        final boolean fromDefined = mode.equals(EEditorPresentationRightsInheritanceMode.FROM_DEFINED);
        givenPresentationLabel.setVisible(fromDefined);
        definedEditor.setVisible(fromDefined);
        if (fromDefined) {
            List<AdsEditorPresentationDef> pr = presentation.getOwnerClass().getPresentations().getEditorPresentations().get(EScope.ALL, new DefinedFilter(presentation));
            if (pr.contains(presentation)) {
                boolean removed = false;
                int i = 0;
                while (!removed && i <= pr.size() - 1) {
                    if (pr.get(i).equals(presentation)) {
                        removed = true;
                        pr.remove(i);
                    } else {
                        i++;
                    }
                }
            }
            definedEditor.setComboBoxValues(pr, false);

            Id definedId = presentation.getRightsSourceEditorPresentationId();
            AdsEditorPresentationDef defined = presentation.getOwnerClass().getPresentations().getEditorPresentations().findById(definedId, EScope.ALL).get();

            definedEditor.open(defined, defined != null ? defined.getId() : null);
            definedEditor.setEnabled(!readonly && !isInherited);
        }
        isUpdate = false;
    }

    private class DefinedFilter implements IFilter<AdsEditorPresentationDef>{

        private IFilter filter;
        private AdsEditorPresentationDef current;

        DefinedFilter(AdsEditorPresentationDef presentation){
            filter = AdsFilters.newBasePresentationFilter(presentation);
            current = presentation;
        }

        @Override
        public boolean isTarget(AdsEditorPresentationDef radixObject) {
            if (filter.isTarget(radixObject)){
                return !hasCurrentAsDefinedRightSource(radixObject);
            }
            return false;
        }

        private boolean hasCurrentAsDefinedRightSource(AdsEditorPresentationDef obj){
            if (obj.getRightInheritanceMode().equals(EEditorPresentationRightsInheritanceMode.FROM_DEFINED)){
                AdsEditorPresentationDef objDefined = obj.findRightSourceEditorPresentation();
                if (objDefined != null && objDefined.equals(current)){
                    return true;
                } else if (objDefined != null){
                    return hasCurrentAsDefinedRightSource(objDefined);
                }
            }
            return false;
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

        modeEditor = new javax.swing.JComboBox();
        givenPresentationLabel = new javax.swing.JLabel();
        definedEditor = new org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel();

        modeEditor.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        givenPresentationLabel.setText(org.openide.util.NbBundle.getMessage(RightsInheritancePanel.class, "RightInheritance-GivenPresentation")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(modeEditor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(givenPresentationLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(definedEditor, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(modeEditor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(givenPresentationLabel)
                .addComponent(definedEditor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel definedEditor;
    private javax.swing.JLabel givenPresentationLabel;
    private javax.swing.JComboBox modeEditor;
    // End of variables declaration//GEN-END:variables
}
