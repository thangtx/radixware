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
 * AdsTransparentClassWizardStep2Panel.java
 *
 * Created on 26.05.2009, 16:54:05
 */
package org.radixware.kernel.designer.ads.editors.creation;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashSet;
import java.util.Set;
import javax.swing.BoxLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.Methods;
import org.radixware.kernel.common.defs.ads.platform.RadixPlatformClass;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.common.dialogs.utils.AdsClassMembersUtils;


@Deprecated
class UnpublishedMethodsPanel extends javax.swing.JPanel
        implements ChangeListener {

    private Set<RadixPlatformClass.Method> checked = new HashSet<RadixPlatformClass.Method>();
    private Set<MethodCheckBox> allBoxes = new HashSet<MethodCheckBox>();
    /** Creates new form AdsTransparentClassWizardStep2Panel */
    private javax.swing.JPanel content = new javax.swing.JPanel();
    private MethodCheckBox allCheck = new MethodCheckBox();

    public UnpublishedMethodsPanel() {
        initComponents();
        setLayout(new BorderLayout());
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        javax.swing.JScrollPane scroll = new javax.swing.JScrollPane();
        javax.swing.JPanel c = new javax.swing.JPanel();
        c.setLayout(new BorderLayout());
        c.add(content, BorderLayout.NORTH);
        scroll.setViewportView(c);
        scroll.setPreferredSize(getPreferredSize());
        add(scroll, BorderLayout.CENTER);

        ChangeListener allListener = new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if (allCheck.isSelected()) {
                    for (MethodCheckBox b : allBoxes) {
                        if (!b.isSelected()) {
                            b.setSelected(true);
                        }
                    }
                } else {
                    for (MethodCheckBox b : allBoxes) {
                        if (b.isSelected()) {
                            b.setSelected(false);
                        }
                    }
                }
            }
        };
        allCheck.addChangeListener(allListener);
        allCheck.setAlignmentX(0);
        allCheck.setAlignmentY(0);
    }

    private void init() {
        checked.clear();
        allBoxes.clear();
        content.removeAll();
        content.add(allCheck);
    }

    public void open(RadixPlatformClass cl, Definition context) {
        init();
        RadixPlatformClass.Method[] methods = cl.getMethods();
        allCheck.setVisible(methods.length > 0);
        for (RadixPlatformClass.Method m : methods) {
            if (!m.getName().equals("<clinit>")) {
                MethodCheckBox box = new MethodCheckBox(cl, m, context);
                box.setAlignmentX(0);
                box.setAlignmentY(0);
                box.addChangeListener(this);
                content.add(box);
                allBoxes.add(box);
            }
        }
    }

    public void open(RadixPlatformClass cl, AdsClassDef adsClass) {
        init();
        RadixPlatformClass.Method[] methods = cl.getMethods();
        allCheck.setVisible(methods.length > 0);
        Methods adsMethods = adsClass.getMethods();
        for (RadixPlatformClass.Method m : methods) {
            if (!m.getName().equals("<clinit>")) {
                MethodCheckBox box = new MethodCheckBox(cl, m, adsClass);
                box.setAlignmentX(0);
                box.setAlignmentY(0);
                box.addChangeListener(this);
                AdsMethodDef adsM = adsMethods.findBySignature(m.getRadixSignature(), EScope.ALL);
                if (adsM != null) {
                    box.setSelected(adsM.getTransparence() != null
                            && adsM.getTransparence().isTransparent());
                }
                content.add(box);
                allBoxes.add(box);
            }
        }
    }

    public Set<RadixPlatformClass.Method> getPublishedMethods() {
        return checked;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource().getClass().equals(MethodCheckBox.class)) {
            MethodCheckBox box = (MethodCheckBox) e.getSource();
            boolean isSelected = box.isSelected();
            if (isSelected) {
                checked.add(box.getMethod());
            } else {
                checked.remove(box.getMethod());
            }
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 313, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 192, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    private class MethodCheckBox extends javax.swing.JPanel {

        private RadixPlatformClass.Method method;
        private AdsClassMembersUtils.TransparentMethodInfo info;
        private javax.swing.JCheckBox box;
        private javax.swing.JLabel label;

        MethodCheckBox() {//empty box, just for one case: switch on/off all other boxes
            super();
            setLayout(new BorderLayout());
            box = new javax.swing.JCheckBox();
            box.setText(null);
            add(box, BorderLayout.WEST);
            label = new javax.swing.JLabel();
            label.setIcon(RadixWareIcons.DIALOG.OK.getIcon(16, 16));
            add(label, BorderLayout.CENTER);
            box.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    changeSupport.fireChange();
                }
            });
        }

        MethodCheckBox(RadixPlatformClass cl, RadixPlatformClass.Method method, Definition context) {
            super();
            this.method = method;
            this.info = new AdsClassMembersUtils.TransparentMethodInfo(cl, method, context);
            setLayout(new BorderLayout());
            box = new javax.swing.JCheckBox();
            box.setText(null);
            add(box, BorderLayout.WEST);
            javax.swing.JLabel label = new javax.swing.JLabel();
            label.setIcon(info.getIcon());
            label.setText(info.toString());
            add(label, BorderLayout.CENTER);
            box.addItemListener(new ItemListener() {

                @Override
                public void itemStateChanged(ItemEvent e) {
                    changeSupport.fireChange();
                }
            });
        }

        @Override
        public void setVisible(boolean aFlag) {
            super.setVisible(aFlag);
            box.setVisible(aFlag);
            label.setVisible(aFlag);
        }

        void setSelected(boolean selected) {
            box.setSelected(selected);
        }

        boolean isSelected() {
            return box.isSelected();
        }

        RadixPlatformClass.Method getMethod() {
            return method;
        }

        AdsClassMembersUtils.TransparentMethodInfo getInfo() {
            return info;
        }
        private ChangeSupport changeSupport = new ChangeSupport(this);

        public final void addChangeListener(ChangeListener l) {
            changeSupport.addChangeListener(l);
        }

        public final void removeChangeListener(ChangeListener l) {
            changeSupport.removeChangeListener(l);
        }
    }
}
