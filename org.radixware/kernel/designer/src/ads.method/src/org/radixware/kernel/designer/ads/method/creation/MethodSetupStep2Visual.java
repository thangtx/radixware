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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.*;
import javax.swing.JLabel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsRPCMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.MethodParameter;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.designer.ads.method.profile.ChangeProfilePanel;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel;
import org.radixware.kernel.designer.common.dialogs.components.description.DescriptionStorage;
import org.radixware.kernel.designer.common.dialogs.components.state.StateManager;


public class MethodSetupStep2Visual extends javax.swing.JPanel {

    private Map<AdsMethodDef, ChangeProfilePanel> profiles = new HashMap<>();
    private ChangeProfilePanel profile = new ChangeProfilePanel();
//    private AdsTransparentMethodPanel transparency = new AdsTransparentMethodPanel();
    private DefinitionLinkEditPanel methodChoose;

    /** Creates new form MethodSetupStep2Visual */
    public MethodSetupStep2Visual() {
        initComponents();
        setLayout(new BorderLayout());
        add(profile, BorderLayout.CENTER);
    }

    @Override
    public String getName() {
        if (methodChoose != null) {
            return "Choose server side method";
        }
        return NbBundle.getMessage(MethodSetupStep2Visual.class, "SetupStep2");
    }

    private List<AdsMethodDef> listServerMethodForRPC(AdsRPCMethodDef clazz) {
        List<AdsMethodDef> methods;
        if (clazz == null) {
            return Collections.emptyList();
        }
        AdsClassDef ss = clazz.findServerSideClass();
        if (ss == null) {
            methods = Collections.emptyList();
        } else {
            methods = ss.getMethods().get(EScope.LOCAL_AND_OVERWRITE, AdsRPCMethodDef.createSuitableMethodsFilter());
            Collections.sort(methods, new Comparator<AdsMethodDef>() {

                @Override
                public int compare(AdsMethodDef o1, AdsMethodDef o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
        }

        return methods;
    }

    public void openRpc(final AdsRPCMethodDef rpcMethod) {
        methodChoose = new DefinitionLinkEditPanel();
        methodChoose.open(ChooseDefinitionCfg.Factory.newInstance(listServerMethodForRPC(rpcMethod)), rpcMethod.findServerSideMethod(), null);
        methodChoose.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                rpcMethod.setServerSideMethod((AdsMethodDef) methodChoose.getDefinition());
                changeSupport.fireChange();
            }
        });
        add(methodChoose);
        AdsClassDef clazz = rpcMethod.findServerSideClass();
        String text = "Server-side method:";
        if (clazz != null) {
            text = "Server-side method (local method of class " + clazz.getQualifiedName() + "):";
        }
        JLabel label = new JLabel(text);
        add(label, BorderLayout.PAGE_START);
    }

//    public void open(AdsClassDef owner) {
//        methodChoose = null;
//        transparency.open(owner);
//        transparency.addChangeListener(new ChangeListener() {
//
//            @Override
//            public void stateChanged(ChangeEvent e) {
//                changeSupport.fireChange();
//            }
//        });
//        removeAll();
//        add(transparency, BorderLayout.CENTER);
//    }

    public void open(AdsMethodDef context, AdsClassDef owner) {
        methodChoose = null;
        removeAll();
        ChangeProfilePanel current = profiles.get(context);
        if (current == null) {
            current = new ChangeProfilePanel();
            current.open(context, owner);
            current.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    changeSupport.fireChange();
                }
            });
            profiles.put(context, current);
        }
        if (current.isShowing() == false) {
            removeAll();
            add(current, BorderLayout.CENTER);
            profile = current;
        }
    }

    public String getMethodName() {
        return profile.getCurrentlyDisplayedName();
    }

    public DescriptionStorage getMethodDescriptionStorage() {
        return profile.getMethodDescriptionStorage();
    }

    public AdsMethodDef getRPCMethod() {
        if (methodChoose != null) {
            Definition def = methodChoose.getDefinition();
            if (def instanceof AdsMethodDef) {
                return (AdsMethodDef) def;
            }
        }
        return null;
    }

    public AdsTypeDeclaration getMethodReturnType() {
        return profile.getCurrentReturnType();
    }

//    public String getMethodReturnValueDescription() {
//        return "";
//        return profile.getCurrentReturnValueDescription();
//    }

//    public String getMethodDescription() {
//        return "";
//        return profile.getMethodDescription();
//    }

    public List<MethodParameter> getMethodParameters() {
        return profile.getParameters();
    }

//    public RadixPlatformClass.Method getPublisingMethod() {
//        return transparency.getChosenMethod();
//    }

    public List<AdsTypeDeclaration> getMethodThrowList() {
        return profile.getThrowList();
    }

    @Override
    public Dimension getPreferredSize() {
        if (profile.isShowing()) {
            Dimension dim = profile.getPreferredSize();
            return dim;
        }
        return super.getPreferredSize();
    }

    @Override
    public Dimension getMinimumSize() {
        if (profile.isShowing()) {
            return profile.getMinimumSize();
        }
        return super.getMinimumSize();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
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
    private ChangeSupport changeSupport = new ChangeSupport(this);

    public final void addChangeListener(ChangeListener l) {
        changeSupport.addChangeListener(l);
    }

    public final void removeChangeListener(ChangeListener l) {
        changeSupport.removeChangeListener(l);
    }
    private StateManager stateManager = new StateManager(this);

    public boolean isComplete() {
        if (methodChoose != null) {
            if (methodChoose.getDefinition() instanceof AdsMethodDef) {
                stateManager.ok();
                return true;
            } else {
                stateManager.error("Server side method must be specified");
                return false;
            }
        }
        boolean p = profile.isShowing();
        if (p) {
            return profile.isComplete();
        }
        return false;
//        else {
//            return transparency.isComplete();
//        }
    }
}
