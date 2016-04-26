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

package org.radixware.kernel.designer.ads.method.creation;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.algo.AdsAlgoClassDef;
import org.radixware.kernel.common.defs.ads.type.AdsTransparence;
import org.radixware.kernel.designer.common.dialogs.components.state.StateManager;


public class MethodSetupStep1Visual extends javax.swing.JPanel
    implements ItemListener {

    private ChangeSupport changeSupport = new ChangeSupport(this);

    public static final String USER_DEF = NbBundle.getMessage(MethodSetupStep1Visual.class,
        "UserDefMethod");
    public static final String CONSTRUCTOR = NbBundle.getMessage(MethodSetupStep1Visual.class,
        "ConstructorMethod");
    public static final String TRANSPARENT = NbBundle.getMessage(MethodSetupStep1Visual.class,
        "TransparentMethod");
    public static final String ALGO_METHOD = NbBundle.getMessage(MethodSetupStep1Visual.class,
        "AlgoMethod");
    public static final String ALGO_STROB = NbBundle.getMessage(MethodSetupStep1Visual.class,
        "AlgoStrob");
    public static final String RPC_CALL =
        NbBundle.getMessage(MethodSetupStep1Visual.class,
        "RPCCall");
    private ButtonGroup group = new ButtonGroup();
    private JRadioButton userBtn = new JRadioButton(USER_DEF);
    private JRadioButton constBtn = new JRadioButton(CONSTRUCTOR);
    private JRadioButton algoBtn = new JRadioButton(ALGO_METHOD);
    private JRadioButton strobBtn = new JRadioButton(ALGO_STROB);
    private JRadioButton rpcBtn = new JRadioButton(RPC_CALL);

    /**
     * Creates new form MethodSetupStep1Visual
     */
    public MethodSetupStep1Visual() {
        initComponents();
        BoxLayout box = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(box);
    }

    private void initUsualList(AdsClassDef context) {
        userBtn.addItemListener(this);
        constBtn.addItemListener(this);
        userBtn.setActionCommand(String.valueOf(MethodCreature.EMethodType.USER_DEF.id));
        constBtn.setActionCommand(String.valueOf(MethodCreature.EMethodType.CONSTRUCTOR.id));
        group.add(userBtn);
        group.add(constBtn);
        add(userBtn);
        add(constBtn);
        userBtn.setSelected(true);
        switch (context.getClassDefType()) {
            case ENTITY_MODEL:
            case GROUP_MODEL:
            case FORM_MODEL:
                rpcBtn.setActionCommand(String.valueOf(MethodCreature.EMethodType.RPC.id));
                group.add(rpcBtn);
                add(rpcBtn);
                break;
        }
    }

    private void initAlgoList() {
        algoBtn.addItemListener(this);
        strobBtn.addItemListener(this);
        algoBtn.setActionCommand(String.valueOf(MethodCreature.EMethodType.ALGO_METHOD.id));
        strobBtn.setActionCommand(String.valueOf(MethodCreature.EMethodType.ALGO_STROB.id));
        group.add(algoBtn);
        group.add(strobBtn);
        add(algoBtn);
        add(strobBtn);
        algoBtn.setSelected(true);
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(MethodSetupStep2Visual.class, "SetupStep1");
    }

    private boolean firstOpened = false;

    void open(AdsClassDef context) {
        if (context instanceof AdsAlgoClassDef) {
            if (!firstOpened) {
                initAlgoList();
                firstOpened = true;
            }
        } else {
            if (!firstOpened) {
                initUsualList(context);
                firstOpened = true;
            }

            if (!AdsTransparence.isTransparent(context) || context.getTransparence().isExtendable()) {
                userBtn.setEnabled(true);
                constBtn.setEnabled(true);
                if (!firstOpened) {
                    firstOpened = true;
                }
            } else {
                userBtn.setEnabled(false);
                constBtn.setEnabled(false);
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        changeSupport.fireChange();
    }

    public MethodCreature.EMethodType getSelectedType() {
        return MethodCreature.EMethodType.getById(Integer.valueOf(group.getSelection().getActionCommand()));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    public final void addChangeListener(ChangeListener l) {
        changeSupport.addChangeListener(l);
    }

    public final void removeChangeListener(ChangeListener l) {
        changeSupport.removeChangeListener(l);
    }

    private final StateManager stateManager = new StateManager(this);

    public boolean isComplete() {
        if (group.getSelection() != null) {
            stateManager.ok();
            return true;
        } else {
            stateManager.error(NbBundle.getMessage(MethodSetupStep1Visual.class, "ErrorStateStep1"));
            return false;
        }
    }

}
