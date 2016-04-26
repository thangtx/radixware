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
 * UserFuncWizardStep1Panel.java
 *
 * Created on Jul 21, 2011, 8:12:54 AM
 */
package org.radixware.kernel.designer.uds.creation;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsApplicationClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsUserPropertyDef;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.components.state.StateManager;
import org.radixware.kernel.designer.common.dialogs.utils.NameAcceptorFactory;


class UserFuncWizardStep1Panel extends javax.swing.JPanel {

    private UdsUserFuncCreature creature;
    private static final Id USER_FUNC_TABLE_ID = Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM");

    private class ChooseUserPropCfg extends ChooseDefinitionCfg {

        public ChooseUserPropCfg(RadixObject context) {
            super(context, new VisitorProvider() {

                @Override
                public boolean isTarget(RadixObject radixObject) {
                    if (radixObject instanceof AdsUserPropertyDef) {
                        AdsUserPropertyDef prop = (AdsUserPropertyDef) radixObject;
                        AdsTypeDeclaration decl = prop.getValue().getType();
                        if (decl != null) {
                            AdsType type = decl.resolve(prop).get();
                            if (type instanceof AdsClassType) {
                                AdsClassDef clazz = ((AdsClassType) type).getSource();
                                if (clazz instanceof AdsEntityObjectClassDef) {
                                    Id entityId = ((AdsEntityObjectClassDef) clazz).getEntityId();
                                    if (entityId == USER_FUNC_TABLE_ID) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                    return false;
                }
            });
        }
    }

    private static class ChooseMethodCfg extends ChooseDefinitionCfg {

        public ChooseMethodCfg(AdsClassDef targetClass) {
            super(targetClass.getMethods().get(EScope.ALL, new IFilter<AdsMethodDef>() {

                @Override
                public boolean isTarget(AdsMethodDef radixObject) {
                    return isMatch(radixObject);
                }
            }));
        }

        public ChooseMethodCfg(RadixObject context) {
            super(context, new VisitorProvider() {

                @Override
                public boolean isTarget(RadixObject radixObject) {
                    if (radixObject instanceof AdsMethodDef) {
                        AdsMethodDef method = (AdsMethodDef) radixObject;
                        if (method.getOwnerClass() instanceof AdsApplicationClassDef && ((AdsApplicationClassDef) method.getOwnerClass()).getEntityId() == USER_FUNC_TABLE_ID) {
                            return isMatch(method);
                        }
                    }
                    return false;
                }
            });
        }

        private static boolean isMatch(AdsMethodDef radixObject) {
            return (radixObject.getTransparence() == null || !radixObject.getTransparence().isTransparent()) && (radixObject.getOwnerClass() instanceof AdsApplicationClassDef) && radixObject.getId() != Id.Factory.loadFrom("mthFOOX7DTCW5GA5CUK2VYOOVAC3Q");
        }

        private static ChooseMethodCfg createConfig(UdsDefinitionCreature creature, AdsClassDef targetClass) {
            if (targetClass != null) {
                return new ChooseMethodCfg(targetClass);
            } else {
                return new ChooseMethodCfg(creature.getContainer());
            }
        }
    }

    private AdsClassDef getMethodClass(AdsUserPropertyDef prop) {
        ;
        if (prop == null) {
            return null;
        }

        AdsType type = prop.getValue().getType().resolve(prop).get();
        if (type instanceof AdsClassType) {
            AdsClassDef clazz = ((AdsClassType) type).getSource();
            if (clazz instanceof AdsEntityObjectClassDef && ((AdsEntityObjectClassDef) clazz).getEntityId() == USER_FUNC_TABLE_ID) {
                return clazz;
            }
        }
        return null;
    }

    /** Creates new form UserFuncWizardStep1Panel */
    UserFuncWizardStep1Panel(final UdsUserFuncCreature creature) {
        initComponents();
        this.creature = creature;
        edName.setCurrentName(creature.getName());
        edName.setNameAcceptor(NameAcceptorFactory.newCreateAcceptor(creature.getContainer(), EDefType.USER_FUNC));
        edName.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                creature.setName(edName.getCurrentName());
                fireChange();

            }
        });
        edProp.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                AdsUserPropertyDef prop = getProperty();
                if (prop != creature.getUserProperty()) {
                    if (prop == null) {
                        edMethod.setEnabled(false);
                    } else {

                        AdsMethodDef method = getMethod();
                        AdsClassDef clazz = getMethodClass(prop);
                        if (method != null) {
                            if (clazz != null) {
                                method = clazz.getMethods().findById(method.getId(), EScope.ALL).get();
                            }
                        }
                        creature.setMethod(method);

                        edMethod.open(ChooseMethodCfg.createConfig(creature, clazz), method, null);
                        edMethod.setEnabled(true);

                    }

                    creature.setUserProperty(prop);
                }

                fireChange();
            }
        });

        edMethod.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                AdsMethodDef method = getMethod();
                creature.setMethod(method);
                fireChange();

            }
        });
        edProp.open(new ChooseUserPropCfg(creature.getContainer()), null, null);
        edMethod.open(ChooseMethodCfg.createConfig(creature, null), null, null);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        chooseDefinitionPanel1 = new org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionPanel();
        edName = new org.radixware.kernel.designer.common.dialogs.components.NameEditPanel();
        jLabel1 = new javax.swing.JLabel();
        edProp = new org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel();
        jLabel2 = new javax.swing.JLabel();
        edMethod = new org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        jLabel1.setText(org.openide.util.NbBundle.getMessage(UserFuncWizardStep1Panel.class, "UserFuncWizardStep1Panel.jLabel1.text")); // NOI18N

        jLabel2.setText(org.openide.util.NbBundle.getMessage(UserFuncWizardStep1Panel.class, "UserFuncWizardStep1Panel.jLabel2.text")); // NOI18N

        jLabel3.setText(org.openide.util.NbBundle.getMessage(UserFuncWizardStep1Panel.class, "UserFuncWizardStep1Panel.jLabel3.text")); // NOI18N

        jLabel4.setText(org.openide.util.NbBundle.getMessage(UserFuncWizardStep1Panel.class, "UserFuncWizardStep1Panel.jLabel4.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(edMethod, javax.swing.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE)
                            .addComponent(edProp, javax.swing.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(103, 103, 103)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(edName, javax.swing.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(edName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(edProp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(edMethod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addContainerGap(180, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionPanel chooseDefinitionPanel1;
    private org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel edMethod;
    private org.radixware.kernel.designer.common.dialogs.components.NameEditPanel edName;
    private org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel edProp;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    // End of variables declaration//GEN-END:variables
    StateManager sm = new StateManager(this);

    public boolean isComplete() {
        if (!edName.isComplete()) {
            return false;
        }
//        if (getProperty() == null) {
//            sm.error("Storage property for function should be specified");
//            return false;
//        } else {
//            sm.ok();
//        }
        if (getMethod() == null) {
            sm.error("Implemented method should be specified");
            return false;
        } else {
            sm.ok();
        }
        return true;

    }

    private AdsMethodDef getMethod() {
        Definition def = edMethod.getDefinition();
        if (def instanceof AdsMethodDef) {
            return (AdsMethodDef) def;
        } else {
            return null;
        }
    }

    private AdsUserPropertyDef getProperty() {
        Definition def = edProp.getDefinition();
        if (def instanceof AdsUserPropertyDef) {
            return (AdsUserPropertyDef) def;
        } else {
            return null;
        }
    }
    private ChangeSupport cs = new ChangeSupport(this);

    public void removeChangeListener(ChangeListener listener) {
        cs.removeChangeListener(listener);
    }

    public void fireChange() {
        cs.fireChange();
    }

    public void addChangeListener(ChangeListener listener) {
        cs.addChangeListener(listener);
    }
}
