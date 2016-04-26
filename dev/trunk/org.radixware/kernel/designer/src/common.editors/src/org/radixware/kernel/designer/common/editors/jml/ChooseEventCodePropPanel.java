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
 * ChooseEventCodePropPanel.java
 *
 * Created on Dec 28, 2011, 7:49:04 AM
 */
package org.radixware.kernel.designer.common.editors.jml;

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsEventCodePropertyDef;
import org.radixware.kernel.common.defs.ads.localization.AdsEventCodeDef;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.jml.JmlTagEventCode;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;


class ChooseEventCodePropPanel extends javax.swing.JPanel {

    private boolean useSelectedProperty;
    private JmlTagEventCode context;

    private class ChoosePropCfg extends ChooseDefinitionCfg {

        public ChoosePropCfg() {
            super(context, new VisitorProvider() {

                @Override
                public boolean isTarget(RadixObject radixObject) {
                    if (radixObject instanceof AdsEventCodePropertyDef) {
                        AdsEventCodePropertyDef prop = (AdsEventCodePropertyDef) radixObject;
                        if (!prop.getAccessFlags().isStatic()) {
                            return false;
                        }
                        switch (prop.getUsageEnvironment()) {
                            case COMMON:
                                return true;
                            case COMMON_CLIENT:
                                return context.getOwnerJml().getUsageEnvironment() == ERuntimeEnvironmentType.COMMON_CLIENT
                                        || context.getOwnerJml().getUsageEnvironment() == ERuntimeEnvironmentType.EXPLORER || context.getOwnerJml().getUsageEnvironment() == ERuntimeEnvironmentType.WEB;
                            default:
                                return context.getOwnerJml().getUsageEnvironment() == prop.getUsageEnvironment();
                        }
                    }
                    return false;
                }
            });
        }
    }

    private class ChooseClassCfg extends ChooseDefinitionCfg {

        public ChooseClassCfg() {
            super(context, new VisitorProvider() {

                @Override
                public boolean isTarget(RadixObject radixObject) {
                    if (radixObject instanceof AdsClassDef) {
                        AdsClassDef clazz = (AdsClassDef) radixObject;
                        switch (clazz.getUsageEnvironment()) {
                            case COMMON:
                                return true;
                            case COMMON_CLIENT:
                                return context.getOwnerJml().getUsageEnvironment() == ERuntimeEnvironmentType.COMMON_CLIENT
                                        || context.getOwnerJml().getUsageEnvironment() == ERuntimeEnvironmentType.EXPLORER || context.getOwnerJml().getUsageEnvironment() == ERuntimeEnvironmentType.WEB;
                            default:
                                return context.getOwnerJml().getUsageEnvironment() == clazz.getUsageEnvironment();
                        }
                    }
                    return false;
                }
            });
        }
    }

    /** Creates new form ChooseEventCodePropPanel */
    public ChooseEventCodePropPanel(JmlTagEventCode context) {
        initComponents();
        useSelectedProperty = true;
        this.context = context;
        edProp.open(new ChoosePropCfg(), null, null);
        edClass.open(new ChooseClassCfg(), null, null);
        updateState();
    }

    private void updateState() {
        chUseExistingProp.setSelected(useSelectedProperty);
        chCreateNewProp.setSelected(!useSelectedProperty);
        edProp.setEnabled(useSelectedProperty);
        edClass.setEnabled(!useSelectedProperty);
        edName.setEnabled(!useSelectedProperty);
    }

    public boolean isOk() {
        if (useSelectedProperty) {
            if (edProp.getDefinition() == null) {
                DialogUtils.messageError("Event code property should be selected");
                return false;
            } else {
                return true;
            }
        } else {
            if (edClass.getDefinition() == null) {
                DialogUtils.messageError("Class of new event code property should be selected");
                return false;
            }
            if (edName.getText() == null || edName.getText().isEmpty()) {
                DialogUtils.messageError("Name of new event code property should be specified");
                return false;
            }
            return true;

        }
    }

    public AdsEventCodePropertyDef apply() {
        if (useSelectedProperty) {
            return (AdsEventCodePropertyDef) edProp.getDefinition();
        } else {
            AdsEventCodePropertyDef prop = AdsEventCodePropertyDef.Factory.newInstance();
            prop.setName(edName.getText());
            AdsMultilingualStringDef string = context.findLocalizedString(context.getStringId());
            if (string == null) {
                DialogUtils.messageError("Source event code not found");
                return null;
            }
            AdsEventCodeDef ec = AdsEventCodeDef.Factory.newEventCodeInstance(string);
            AdsClassDef clazz = (AdsClassDef) edClass.getDefinition();
            clazz.findLocalizingBundle().getStrings().getLocal().add(ec);
            prop.setEventId(ec.getId());
            clazz.getProperties().getLocal().add(prop);
            return prop;
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

        jToggleButton1 = new javax.swing.JToggleButton();
        jRadioButton1 = new javax.swing.JRadioButton();
        chUseExistingProp = new javax.swing.JRadioButton();
        chCreateNewProp = new javax.swing.JRadioButton();
        edProp = new org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel();
        edClass = new org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel();
        jLabel1 = new javax.swing.JLabel();
        edName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();

        jToggleButton1.setText(org.openide.util.NbBundle.getMessage(ChooseEventCodePropPanel.class, "ChooseEventCodePropPanel.jToggleButton1.text")); // NOI18N

        jRadioButton1.setText(org.openide.util.NbBundle.getMessage(ChooseEventCodePropPanel.class, "ChooseEventCodePropPanel.jRadioButton1.text")); // NOI18N

        chUseExistingProp.setText(org.openide.util.NbBundle.getMessage(ChooseEventCodePropPanel.class, "ChooseEventCodePropPanel.chUseExistingProp.text")); // NOI18N
        chUseExistingProp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chUseExistingPropActionPerformed(evt);
            }
        });

        chCreateNewProp.setText(org.openide.util.NbBundle.getMessage(ChooseEventCodePropPanel.class, "ChooseEventCodePropPanel.chCreateNewProp.text")); // NOI18N
        chCreateNewProp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chCreateNewPropActionPerformed(evt);
            }
        });

        jLabel1.setText(org.openide.util.NbBundle.getMessage(ChooseEventCodePropPanel.class, "ChooseEventCodePropPanel.jLabel1.text")); // NOI18N

        edName.setText(org.openide.util.NbBundle.getMessage(ChooseEventCodePropPanel.class, "ChooseEventCodePropPanel.edName.text")); // NOI18N

        jLabel2.setText(org.openide.util.NbBundle.getMessage(ChooseEventCodePropPanel.class, "ChooseEventCodePropPanel.jLabel2.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(edProp, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                    .addComponent(chUseExistingProp)
                    .addComponent(chCreateNewProp)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(edName, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                            .addComponent(edClass, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(chUseExistingProp)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(edProp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(chCreateNewProp)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(edClass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(edName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void chUseExistingPropActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chUseExistingPropActionPerformed
        useSelectedProperty = chUseExistingProp.isSelected();
        updateState();
    }//GEN-LAST:event_chUseExistingPropActionPerformed

    private void chCreateNewPropActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chCreateNewPropActionPerformed
        useSelectedProperty = !chCreateNewProp.isSelected();
        updateState();
    }//GEN-LAST:event_chCreateNewPropActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton chCreateNewProp;
    private javax.swing.JRadioButton chUseExistingProp;
    private org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel edClass;
    private javax.swing.JTextField edName;
    private org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel edProp;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JToggleButton jToggleButton1;
    // End of variables declaration//GEN-END:variables
}
