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

package org.radixware.kernel.designer.ads.method.transparent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Collections;
import javax.swing.JButton;
import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsTransparentMethodDef;
import org.radixware.kernel.common.defs.ads.platform.PlatformLib;
import org.radixware.kernel.common.defs.ads.platform.RadixPlatformClass;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.common.dialogs.components.state.StateManager;
import org.radixware.kernel.designer.common.dialogs.utils.AdsClassMembersUtils;

/**
 * @deprecated
 */
@Deprecated
class AdsTransparentMethodPanel extends javax.swing.JPanel {

    public final static String BEGIN_STATE = NbBundle.getMessage(AdsTransparentMethodPanel.class, "BeginStateTip");
    private boolean err_state = true;
    private JButton nameBtn;
    private AdsTransparentMethodDef adsmethod;
    private boolean isPublished = false;
    private AdsClassDef adsclass;
    private RadixPlatformClass context;
    private RadixPlatformClass.Method chosen;
    private Collection<RadixPlatformClass.Method> toBePublished = Collections.<RadixPlatformClass.Method>emptyList();

    /** Creates new form TransparentMethodPanel */
    public AdsTransparentMethodPanel() {
        initComponents();
        nameBtn = nameEditor.addButton();
        nameBtn.setIcon(RadixWareIcons.DIALOG.CHOOSE.getIcon(13, 13));
        nameBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                MethodChoosePanel panel = new MethodChoosePanel(toBePublished, adsclass);
                RadixPlatformClass.Method m = panel.chooseMethod();
                if (m != null) {
                    if (isPublished) {
                        nameEditor.setValue(String.valueOf(m.getRadixSignature()));
                    } else {
                        nameEditor.setValue(new AdsClassMembersUtils.TransparentMethodInfo(m, adsclass).toString());
                    }
                    chosen = m;
                    changeSupport.fireChange();
                }
            }
        });
        nameEditor.setValue(BEGIN_STATE);
    }

    private PlatformLib currentLib;

    public void open(AdsClassDef ownerclass) {
        this.open(null, ownerclass, false);
    }

    public void updateState() {
        if (context != null) {
            toBePublished = AdsClassMembersUtils.getMethods(adsclass, context, isPublished, adsmethod);
            nameBtn.setEnabled(!toBePublished.isEmpty());
        } else {
            toBePublished = Collections.<RadixPlatformClass.Method>emptyList();
            nameBtn.setEnabled(false);
        }
    }

    public void open(AdsTransparentMethodDef method, AdsClassDef ownerclass, boolean isPublished) {
        if (method != null || !isPublished) {
            this.isPublished = isPublished;
            currentLib = ((AdsSegment) ownerclass.getModule().getSegment()).getBuildPath().getPlatformLibs().getKernelLib(ownerclass.getUsageEnvironment());
            adsclass = ownerclass;
            adsmethod = method;
            if (isPublished) {
                nameEditor.setToolTipText(NbBundle.getMessage(AdsTransparentMethodPanel.class, "SignatureToolTip"));
                nameEditor.setValue(adsmethod.getTransparence().getPublishedName());
            } else {
                nameEditor.setToolTipText(null);
            }
            if (ownerclass.getTransparence() != null && ownerclass.getTransparence().isTransparent()) {
                String classcontext = ownerclass.getTransparence().getPublishedName();
                context = currentLib.findPlatformClass(classcontext);
                if (!isPublished) {
                    toBePublished = AdsClassMembersUtils.getMethods(adsclass, context, isPublished, adsmethod);
                }
                nameBtn.setEnabled(context != null && !toBePublished.isEmpty() && !isPublished);
                if (context != null) {
                    err_state = true;
                } else {
                    err_state = isPublished;
                }
            } else {
                err_state = isPublished;
            }
            isComplete();
        }
    }

    public RadixPlatformClass.Method getChosenMethod() {
        return chosen;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        stateDisplayer1 = new org.radixware.kernel.designer.common.dialogs.components.state.StateDisplayer();
        nameEditor = new org.radixware.kernel.common.components.ExtendableTextField();

        setMaximumSize(new java.awt.Dimension(32767, 64));
        setMinimumSize(getPreferredSize());
        setPreferredSize(new java.awt.Dimension(460, 70));

        jLabel1.setText(org.openide.util.NbBundle.getMessage(AdsTransparentMethodPanel.class, "PublishedMethod")); // NOI18N

        nameEditor.setReadOnly(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(stateDisplayer1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
                    .addComponent(nameEditor, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(nameEditor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addComponent(stateDisplayer1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private org.radixware.kernel.common.components.ExtendableTextField nameEditor;
    private org.radixware.kernel.designer.common.dialogs.components.state.StateDisplayer stateDisplayer1;
    // End of variables declaration//GEN-END:variables
    private ChangeSupport changeSupport = new ChangeSupport(this);

    public final void addChangeListener(ChangeListener l) {
        changeSupport.addChangeListener(l);
    }

    public final void removeChangeListener(ChangeListener l) {
        changeSupport.removeChangeListener(l);
    }
    private final StateManager stateManager = new StateManager(this);

    public boolean isComplete() {
        if (nameEditor.getValue().equals(BEGIN_STATE)) {
            if (err_state) {
                stateManager.warning(NbBundle.getMessage(AdsTransparentMethodPanel.class, "ErrorStateTip"));
                return false;
            } else {
                stateManager.error(NbBundle.getMessage(AdsTransparentMethodPanel.class, "ClassLoadErrorTip"));
                return false;
            }
        } else {
            if (!err_state) {
                stateManager.error(NbBundle.getMessage(AdsTransparentMethodPanel.class, "ClassLoadErrorTip"));
                return false;
            } else {
                stateManager.ok();
                return true;
            }
        }
    }
}
