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

package org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsAppObject;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.common.AdsCondition;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProvider;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.utils.RadixResourceBundle;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinition;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.editors.sqml.SqmlEditorPanel;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.schemas.adsdef.SqmlCondition;
import org.radixware.schemas.xscml.Sqml;


public class FormSelectorPanel extends EditorDialog.EditorPanel<AdsAppObject> {

    public FormSelectorPanel(AdsAppObject node) {
        super(node);
        initComponents();

        classId = node.getPropByName("objClassId");
        presentationId = node.getPropByName("presentationId");
        condition = node.getPropByName("condition");
        object = node.getPropByName("object");

        textClass.setEditable(false);
        textClass.addChooseButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AdsVisitorProvider provider = new AdsVisitorProvider() {
                    @Override
                    public boolean isTarget(RadixObject object) {
                        return object instanceof AdsEntityObjectClassDef;
                    };
                };

                ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(obj, provider);
                AdsEntityObjectClassDef def = (AdsEntityObjectClassDef)ChooseDefinition.chooseDefinition(cfg);
                if (def != null) {
                    classDef = def;
                    textClass.setValue(classDef.getQualifiedName());

                    presentationDef = null;
                    textPresentation.setEmpty();
                }
            }
        });

        textClass.addResetButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                classDef = null;
                textClass.setEmpty();

                presentationDef = null;
                textPresentation.setEmpty();
            }
        });

        textPresentation.setEditable(false);
        textPresentation.addChooseButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (classDef == null)
                    return;
/*
                AdsVisitorProvider provider = new AdsVisitorProvider() {
                    @Override
                    public boolean isTarget(RadixObject object) {
                        if (object instanceof AdsSelectorPresentationDef) {
                            return ((AdsSelectorPresentationDef)object).getOwnerClass() == classDef;
                        }
                        return false;
                    };
                };
                ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(obj, provider);
*/
                ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(classDef.getPresentations().getSelectorPresentations().get(ExtendableDefinitions.EScope.ALL));
                AdsSelectorPresentationDef def = (AdsSelectorPresentationDef)ChooseDefinition.chooseDefinition(cfg);
                if (def != null) {
                    presentationDef = def;
                    textPresentation.setValue(presentationDef.getName());
//                    sqml.changeContainer(def);
                }
            }
        });

        textPresentation.addResetButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                presentationDef = null;
                textPresentation.setEmpty();
//                sqml.changeContainer(obj);
            }
        });

        textCondition.setBorder(BorderFactory.createLineBorder(new Color(130, 135, 144), 1));
        panelEditor.add(textCondition);

        panelSubmit.setEnabled(!node.isReadOnly());
        textClass.setEnabled(!node.isReadOnly());
        textPresentation.setEnabled(!node.isReadOnly());
        textCondition.setEnabled(!node.isReadOnly());
        panelSubmit.setEnabled(!node.isReadOnly());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelSubmit = new org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.SubmitPanel();
        textClass = new org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.ExtTextField();
        labelObjectClass = new javax.swing.JLabel();
        labelPresentation = new javax.swing.JLabel();
        textPresentation = new org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.ExtTextField();
        labelCondition = new javax.swing.JLabel();
        panelEditor = new javax.swing.JPanel();

        setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.SystemColor.inactiveCaption));
        setMinimumSize(new java.awt.Dimension(500, 430));
        setPreferredSize(new java.awt.Dimension(500, 430));
        setRequestFocusEnabled(false);

        labelObjectClass.setText(RadixResourceBundle.getMessage(FormSelectorPanel.class, "FormSelectorPanel.labelObjectClass.text")); // NOI18N

        labelPresentation.setText(RadixResourceBundle.getMessage(FormSelectorPanel.class, "FormSelectorPanel.labelPresentation.text")); // NOI18N

        labelCondition.setText(RadixResourceBundle.getMessage(FormSelectorPanel.class, "FormSelectorPanel.labelCondition.text")); // NOI18N

        panelEditor.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(panelSubmit, javax.swing.GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(labelObjectClass)
                            .addComponent(labelPresentation)
                            .addComponent(labelCondition))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panelEditor, javax.swing.GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE)
                            .addComponent(textClass, javax.swing.GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE)
                            .addComponent(textPresentation, javax.swing.GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textClass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelObjectClass))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelPresentation)
                    .addComponent(textPresentation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelEditor, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelCondition))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelSubmit, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel labelCondition;
    private javax.swing.JLabel labelObjectClass;
    private javax.swing.JLabel labelPresentation;
    private javax.swing.JPanel panelEditor;
    private org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.SubmitPanel panelSubmit;
    private org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.ExtTextField textClass;
    private org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.ExtTextField textPresentation;
    // End of variables declaration//GEN-END:variables

    private SqmlEditorPanel textCondition = new SqmlEditorPanel();
    private AdsCondition sqml;

    private AdsAppObject.Prop classId;
    private AdsAppObject.Prop presentationId;
    private AdsAppObject.Prop condition;
    private AdsAppObject.Prop object;

    private AdsEntityObjectClassDef classDef = null;
    private AdsSelectorPresentationDef presentationDef = null;

    @Override
    public void init() {
        if (classId.getValue() != null)
            classDef = (AdsEntityObjectClassDef)AdsSearcher.Factory.newAdsDefinitionSearcher(obj).findById(Id.Factory.loadFrom(classId.getValue().toString())).get();
        if (classDef != null)
            textClass.setValue(classDef.getQualifiedName());
        else
            textClass.setEmpty();

        if (presentationId.getValue() != null && classDef != null)
            presentationDef = classDef.getPresentations().getSelectorPresentations().findById(Id.Factory.loadFrom(String.valueOf(presentationId.getValue())), EScope.ALL).get();
        if (presentationDef != null)
            textPresentation.setValue(presentationDef.getName());
        else
            textPresentation.setEmpty();

        if (condition.getValue() != null) {
            try {
                Sqml xSqml = Sqml.Factory.parse(condition.getValue().toString());
                sqml = AdsCondition.Factory.newInstance(/*presentationDef != null ? presentationDef : */obj);
                sqml.getWhere().loadFrom(xSqml);
            } catch(Exception e) {                
                sqml = AdsCondition.Factory.newInstance(/*presentationDef != null ? presentationDef : */obj);
            }
        } else
            sqml = AdsCondition.Factory.newInstance(/*presentationDef != null ? presentationDef : */obj);
        textCondition.setContext(obj);
        textCondition.open(sqml.getWhere());
        panelSubmit.init(obj);
    }

    @Override
    public void apply() {
        classId.setValue(classDef != null ? String.valueOf(classDef.getId()) : null);
        presentationId.setValue(presentationDef != null ? String.valueOf(presentationDef.getId()) : null);
        object.setType(AdsTypeDeclaration.Factory.newInstance(EValType.OBJECT, classDef == null ? Id.Factory.loadFrom("pdcEntity____________________") : classDef.getId()));

        if (sqml.isEmpty())
            condition.setValue((ValAsStr)null);
        else {
            SqmlCondition xSqml = SqmlCondition.Factory.newInstance();
            sqml.appendTo(xSqml);
            condition.setValue(xSqml.getConditionWhere().xmlText());
        }

        panelSubmit.apply();
    }

    @Override
    public String getTitle() {
        return RadixResourceBundle.getMessage(getClass(), "CTL_AdditionalProperties");
    }

    @Override
    public RadixIcon getIcon() {
        return RadixWareIcons.EDIT.PROPERTIES_PLUS;
    }
}
