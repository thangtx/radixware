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
package org.radixware.kernel.designer.ads.editors.clazz.creation;

import java.awt.CardLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsDetailColumnPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsDynamicPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsExpressionPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsInnateColumnPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyPresentationPropertyDef;
import org.radixware.kernel.common.enums.EPropNature;
import static org.radixware.kernel.common.enums.EPropNature.PROPERTY_PRESENTATION;
import org.radixware.kernel.designer.ads.editors.base.EnvSelectorPanel;

class PropertySetupStep1Visual extends javax.swing.JPanel {

    private EnvSelectorPanel selectorPanel;

    /**
     * Creates new form PropertySetupStep1Visual
     */
    public PropertySetupStep1Visual() {
        initComponents();
        chCreateColumnRef.addActionListener((new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                creature.updateColumnPropertyKind(false);
                openLinks();

                changeSupport.fireChange();
            }
        }));
        chCreateRef.addActionListener((new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                creature.updateColumnPropertyKind(true);
                openLinks();
                changeSupport.fireChange();
            }
        }));
        chPureSql.addActionListener(pureSqlListener);

        selectorPanel = new EnvSelectorPanel();
        selectorPanel.setBorder(BorderFactory.createEmptyBorder(6, 1, 6, 1));
        topPanel.add(selectorPanel);
    }
    private PropertyCreature creature;
    private ChangeListener nameListener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            if (creature != null) {
                creature.setPropertyName(namePanel.getCurrentName());
                changeSupport.fireChange();
            }
        }
    };
    private ChangeListener typeListener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            if (creature != null) {
                changeSupport.fireChange();
            }
        }
    };
    private String checkName = null;
    private ChangeListener refListener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            if (creature != null) {
                if (refEditPanel.getDefinition() != null) {
                    String newCheckName = refEditPanel.getAutoName();
                    if (creature.getTemporaryInstance().getName().equals(checkName)) {
                        creature.getTemporaryInstance().setName(newCheckName);
                        namePanel.removeChangeListener(nameListener);
                        namePanel.setCurrentName(creature.getTemporaryInstance().getName());
                        namePanel.addChangeListener(nameListener);
                        checkName = newCheckName;
                    }
                }
                changeSupport.fireChange();
            }
        }
    };
    private volatile boolean isOpening = false;
    private ActionListener pureSqlListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!isOpening) {
                ((AdsExpressionPropertyDef) creature.getTemporaryInstance()).setInvisibleForArte(chPureSql.isSelected());
                redesignWithExpressionProp();
                changeSupport.fireChange();
            }
        }
    };

    private boolean isPureSql() {
        return creature.propNature == EPropNature.EXPRESSION && ((AdsExpressionPropertyDef) creature.getTemporaryInstance()).isInvisibleForArte();
    }

    private void installEnvSelectorPanel() {
        AdsPropertyDef temporaryInstance = creature.getTemporaryInstance();

        if (EnvSelectorPanel.isMeaningFullFor(temporaryInstance)) {
            selectorPanel.open(temporaryInstance);
            selectorPanel.setVisible(true);
        } else {
            selectorPanel.setVisible(false);
        }
    }

    public void open(PropertyCreature creature) {
        try {
            isOpening = true;
            this.creature = creature;
            namePanel.setCurrentName(creature.getPropertyName());
            namePanel.addChangeListener(nameListener);
            checkName = creature.getTemporaryInstance().getName();
            redesignWithExpressionProp();
            installEnvSelectorPanel();
            switch (creature.propNature) {
                case USER:
                case DYNAMIC:
                case FIELD:
                case FIELD_REF:
                case SQL_CLASS_PARAMETER:
                //case FORM_PROPERTY:
                case GROUP_PROPERTY:
                case EXPRESSION:
                case EVENT_CODE:
                    openTypes();
                    break;
                case PARENT_PROP:
                case DETAIL_PROP:
                case INNATE:
                    openLinks();
                    break;
                case PROPERTY_PRESENTATION:
                    if (((AdsPropertyPresentationPropertyDef) creature.getTemporaryInstance()).isLocal()) {
                        openTypes();
                    } else {
                        openLinks();
                    }
                    break;
            }
        } finally {
            isOpening = false;
        }

    }

    private void redesignWithExpressionProp() {
        if (creature.propNature == EPropNature.EXPRESSION) {
            chPureSql.setVisible(true);
            if (isPureSql()) {
                Point p = internalFrame.getLocation();
                internalFrame.setVisible(false);
                chPureSql.setLocation(p);
            } else {
                internalFrame.setVisible(true);
                internalFrame.setLocation(internalFrame.getX(), chPureSql.getY() + 5);
            }
        } else {
            chPureSql.setVisible(false);
            internalFrame.setVisible(true);
            internalFrame.setLocation(internalFrame.getX(), chPureSql.getY());
        }
    }

    public void openTypes() {

        ((CardLayout) internalFrame.getLayout()).show(internalFrame, "card2");
        typeEditPanel.open(creature.getTemporaryInstance());
        typeEditPanel.addChangeListener(typeListener);
        if (creature.propNature == EPropNature.EVENT_CODE) {
            typeEditPanel.setVisible(false);
            jLabel2.setVisible(false);
        } else {
            typeEditPanel.setVisible(true);
            jLabel2.setVisible(true);
        }

        namePanel.setLocation(typeEditPanel.getX(), namePanel.getY());
    }

    public void openLinks() {
        ((CardLayout) internalFrame.getLayout()).show(internalFrame, "card3");
        this.refEditPanel.setClearable(false);
        this.refEditPanel.open(creature.getTemporaryInstance());
        this.refEditPanel.addChangeListener(refListener);
        updateLinkExtInfo();
        namePanel.setLocation(refEditPanel.getX(), namePanel.getY());
    }

    void updateLinkExtInfo() {
        lbRefKind.setText(refEditPanel.getLabelText());
        if (creature.propNature == EPropNature.DETAIL_PROP) {
            chCreateRef.setText(org.openide.util.NbBundle.getMessage(PropertySetupStep1Visual.class, "PropertySetupStep1Visual.chCreateRef.text2"));
            detailPropKindPanl.setVisible(true);
            if (creature.getTemporaryInstance() instanceof AdsDetailColumnPropertyDef) {
                chCreateColumnRef.setSelected(true);
                chCreateRef.setSelected(false);
            } else {
                chCreateColumnRef.setSelected(false);
                chCreateRef.setSelected(true);
            }
        } else if (creature.propNature == EPropNature.INNATE) {
            chCreateRef.setText(org.openide.util.NbBundle.getMessage(PropertySetupStep1Visual.class, "PropertySetupStep1Visual.chCreateRef.text"));
            detailPropKindPanl.setVisible(true);
            if (creature.getTemporaryInstance() instanceof AdsInnateColumnPropertyDef) {
                chCreateColumnRef.setSelected(true);
                chCreateRef.setSelected(false);
            } else {
                chCreateColumnRef.setSelected(false);
                chCreateRef.setSelected(true);
            }
        } else {
            detailPropKindPanl.setVisible(false);
        }
    }

    public void apply() {
        namePanel.removeChangeListener(nameListener);
        typeEditPanel.removeChangeListener(typeListener);
        refEditPanel.removeChangeListener(refListener);
    }
    
    private ChangeSupport changeSupport = new ChangeSupport(this);

    public final void addChangeListener(ChangeListener l) {
        changeSupport.addChangeListener(l);
    }

    public final void removeChangeListener(ChangeListener l) {
        changeSupport.removeChangeListener(l);
    }

    boolean isComplete() {
        boolean result = true;
        if (!namePanel.isComplete()) {
            result = false;
        }
        switch (creature.propNature) {
            case USER:
            case DYNAMIC:
            //case FORM_PROPERTY:
            case GROUP_PROPERTY:
            case EXPRESSION:
            case SQL_CLASS_PARAMETER:
            case FIELD:
//                if (isPureSql()) {
//                    stateManager.ok();
//                    result = true;
//                } else {
                if (!typeEditPanel.isComplete()) {
                    result = false;
                }
                //}
                break;
            case PARENT_PROP:
            case DETAIL_PROP:
            case INNATE:
                if (!refEditPanel.isComplete()) {
                    result = false;
                }
                break;
            case PROPERTY_PRESENTATION:
                AdsPropertyPresentationPropertyDef pp = (AdsPropertyPresentationPropertyDef) creature.getTemporaryInstance();
                if (pp.isLocal()) {
                    if (!typeEditPanel.isComplete()) {
                        result = false;
                    }
                } else {
                    if (!refEditPanel.isComplete()) {
                        result = false;
                    }
                }
                break;

        }
        return result;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        internalFrame = new javax.swing.JPanel();
        typePanel = new javax.swing.JPanel();
        typeEditPanel = new org.radixware.kernel.designer.ads.editors.clazz.property.PropertyTypeChoosePanel();
        jLabel2 = new javax.swing.JLabel();
        paublishingPanel = new javax.swing.JPanel();
        refEditPanel = new org.radixware.kernel.designer.ads.editors.clazz.property.PublishingPropertyRefPanel();
        lbRefKind = new javax.swing.JLabel();
        detailPropKindPanl = new javax.swing.JPanel();
        chCreateColumnRef = new javax.swing.JRadioButton();
        chCreateRef = new javax.swing.JRadioButton();
        topPanel = new javax.swing.JPanel();
        nameOwnerPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        namePanel = new org.radixware.kernel.designer.common.dialogs.components.NameEditPanel();
        chPureSql = new javax.swing.JCheckBox();

        setLayout(new java.awt.BorderLayout());

        internalFrame.setLayout(new java.awt.CardLayout());

        typePanel.setName("TypePanel"); // NOI18N

        typeEditPanel.setEditable(false);

        jLabel2.setText(org.openide.util.NbBundle.getMessage(PropertySetupStep1Visual.class, "PropertySetupStep1Visual.jLabel2.text")); // NOI18N

        javax.swing.GroupLayout typePanelLayout = new javax.swing.GroupLayout(typePanel);
        typePanel.setLayout(typePanelLayout);
        typePanelLayout.setHorizontalGroup(
            typePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(typePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(typeEditPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 479, Short.MAX_VALUE)
                .addContainerGap())
        );
        typePanelLayout.setVerticalGroup(
            typePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(typePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(typePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(typeEditPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addContainerGap(199, Short.MAX_VALUE))
        );

        internalFrame.add(typePanel, "card2");

        lbRefKind.setText(org.openide.util.NbBundle.getMessage(PropertySetupStep1Visual.class, "PropertySetupStep1Visual.lbRefKind.text")); // NOI18N

        chCreateColumnRef.setText(org.openide.util.NbBundle.getMessage(PropertySetupStep1Visual.class, "PropertySetupStep1Visual.chCreateColumnRef.text")); // NOI18N

        chCreateRef.setText(org.openide.util.NbBundle.getMessage(PropertySetupStep1Visual.class, "PropertySetupStep1Visual.chCreateRef.text")); // NOI18N

        javax.swing.GroupLayout detailPropKindPanlLayout = new javax.swing.GroupLayout(detailPropKindPanl);
        detailPropKindPanl.setLayout(detailPropKindPanlLayout);
        detailPropKindPanlLayout.setHorizontalGroup(
            detailPropKindPanlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(detailPropKindPanlLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(detailPropKindPanlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chCreateColumnRef)
                    .addComponent(chCreateRef))
                .addContainerGap(94, Short.MAX_VALUE))
        );
        detailPropKindPanlLayout.setVerticalGroup(
            detailPropKindPanlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(detailPropKindPanlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(chCreateColumnRef)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chCreateRef)
                .addContainerGap(127, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout paublishingPanelLayout = new javax.swing.GroupLayout(paublishingPanel);
        paublishingPanel.setLayout(paublishingPanelLayout);
        paublishingPanelLayout.setHorizontalGroup(
            paublishingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, paublishingPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbRefKind)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(paublishingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(detailPropKindPanl, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(refEditPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 366, Short.MAX_VALUE))
                .addContainerGap())
        );
        paublishingPanelLayout.setVerticalGroup(
            paublishingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paublishingPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(paublishingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(refEditPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbRefKind))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(detailPropKindPanl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        internalFrame.add(paublishingPanel, "card3");

        add(internalFrame, java.awt.BorderLayout.CENTER);

        topPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 10));
        topPanel.setLayout(new javax.swing.BoxLayout(topPanel, javax.swing.BoxLayout.Y_AXIS));

        nameOwnerPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 6, 1));
        nameOwnerPanel.setLayout(new javax.swing.BoxLayout(nameOwnerPanel, javax.swing.BoxLayout.LINE_AXIS));

        jLabel1.setText(org.openide.util.NbBundle.getMessage(PropertySetupStep1Visual.class, "PropertySetupStep1Visual.jLabel1.text")); // NOI18N
        jLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 8));
        nameOwnerPanel.add(jLabel1);
        nameOwnerPanel.add(namePanel);

        topPanel.add(nameOwnerPanel);

        chPureSql.setText(org.openide.util.NbBundle.getMessage(PropertySetupStep1Visual.class, "PropertySetupStep1Visual.chPureSql.text")); // NOI18N
        topPanel.add(chPureSql);

        add(topPanel, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton chCreateColumnRef;
    private javax.swing.JRadioButton chCreateRef;
    private javax.swing.JCheckBox chPureSql;
    private javax.swing.JPanel detailPropKindPanl;
    private javax.swing.JPanel internalFrame;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel lbRefKind;
    private javax.swing.JPanel nameOwnerPanel;
    private org.radixware.kernel.designer.common.dialogs.components.NameEditPanel namePanel;
    private javax.swing.JPanel paublishingPanel;
    private org.radixware.kernel.designer.ads.editors.clazz.property.PublishingPropertyRefPanel refEditPanel;
    private javax.swing.JPanel topPanel;
    private org.radixware.kernel.designer.ads.editors.clazz.property.PropertyTypeChoosePanel typeEditPanel;
    private javax.swing.JPanel typePanel;
    // End of variables declaration//GEN-END:variables
}
