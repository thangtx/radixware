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
 * TestCaseCreatureVisual.java
 *
 * Created on Jun 17, 2010, 2:00:48 PM
 */
package org.radixware.kernel.designer.ads.editors.creation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsApplicationClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProviders;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.components.state.StateManager;
import org.radixware.kernel.designer.common.dialogs.utils.NameAcceptorFactory;


public class TestCaseCreatureVisual extends javax.swing.JPanel {

    private final StateManager stateManager = new StateManager(this);

    private static Collection<? extends Definition> findAllTests(AdsModule context) {
        final ArrayList<Definition> defs = new ArrayList<Definition>();
        context.getBranch().visit(new IVisitor() {

            @Override
            public void accept(RadixObject radixObject) {
                defs.add((Definition) radixObject);
            }
        }, AdsVisitorProviders.newEntityObjectTypeProvider(AdsApplicationClassDef.TEST_CASE_CLASS_ID));
        return defs;
    }

    private class ChooseCfg extends ChooseDefinitionCfg {

        public ChooseCfg(AdsModule context) {
            super(findAllTests(context));
        }
    }
    private final ChooseDefinitionCfg cfg;

    /** Creates new form TestCaseCreatureVisual */
    public TestCaseCreatureVisual(AdsModule context) {
        initComponents();
        nameEditor.setNameAcceptor(NameAcceptorFactory.newCreateAcceptor(context.getDefinitions(), EDefType.CLASS));
        cfg = new ChooseCfg(context);
    }
    private TestCaseCreature creature;
    private final ActionListener actionListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            baseClass.setEnabled(chCustomBase.isSelected());
            creature.useCustomBase = chCustomBase.isSelected();
            changeSupport.fireChange();
        }
    };
    private final ChangeListener changeListener = new ChangeListener() {

        @Override
        public void stateChanged(ChangeEvent e) {
            creature.customBase = (AdsEntityObjectClassDef) baseClass.getDefinition();
            changeSupport.fireChange();
        }
    };
    private final ChangeListener nameListener = new ChangeListener() {

        @Override
        public void stateChanged(ChangeEvent e) {
            creature.definitionName = nameEditor.getCurrentName();
            changeSupport.fireChange();
        }
    };
    private ChangeSupport changeSupport = new ChangeSupport(this);

    public void addChangeListener(ChangeListener l) {
        changeSupport.addChangeListener(l);
    }

    public void removeChangeListener(ChangeListener l) {
        changeSupport.removeChangeListener(l);
    }

    void open(final TestCaseCreature creature) {
        this.creature = creature;

        nameEditor.removeChangeListener(nameListener);
        nameEditor.setCurrentName(creature.definitionName);
        chCustomBase.removeActionListener(actionListener);

        baseClass.removeChangeListener(changeListener);
        baseClass.open(cfg, creature.customBase, null);

        chCustomBase.setSelected(creature.useCustomBase);


        chCustomBase.addActionListener(actionListener);
        nameEditor.addChangeListener(nameListener);
        baseClass.addChangeListener(changeListener);
    }

    boolean isComplete() {
        if (nameEditor.isComplete()) {
            if (creature.useCustomBase) {
                if (creature.customBase == null) {
                    stateManager.error("Base test case is not specified");
                    return false;
                }
            }
            stateManager.ok();
            return true;
        } else {
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

        chCustomBase = new javax.swing.JCheckBox();
        baseClass = new org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel();
        nameEditor = new org.radixware.kernel.designer.common.dialogs.components.NameEditPanel();
        jLabel1 = new javax.swing.JLabel();

        chCustomBase.setText(org.openide.util.NbBundle.getMessage(TestCaseCreatureVisual.class, "TestCaseCreatureVisual.chCustomBase.text")); // NOI18N

        baseClass.setEnabled(false);

        jLabel1.setText(org.openide.util.NbBundle.getMessage(TestCaseCreatureVisual.class, "TestCaseCreatureVisual.jLabel1.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nameEditor, javax.swing.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE))
            .addComponent(chCustomBase)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(baseClass, javax.swing.GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(nameEditor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(33, 33, 33))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(35, Short.MAX_VALUE)
                        .addComponent(chCustomBase)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(baseClass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(207, 207, 207))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel baseClass;
    private javax.swing.JCheckBox chCustomBase;
    private javax.swing.JLabel jLabel1;
    private org.radixware.kernel.designer.common.dialogs.components.NameEditPanel nameEditor;
    // End of variables declaration//GEN-END:variables
}
