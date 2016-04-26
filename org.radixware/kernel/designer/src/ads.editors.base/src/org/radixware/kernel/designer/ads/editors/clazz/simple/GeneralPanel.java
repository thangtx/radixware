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
 * GeneralPanel111.java
 *
 * Created on Apr 23, 2009, 12:36:54 PM
 */
package org.radixware.kernel.designer.ads.editors.clazz.simple;

import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.SwingUtilities;
import org.radixware.kernel.common.defs.ads.AdsDefinition.AccessChangedEvent;
import org.radixware.kernel.common.defs.ads.AdsDefinition.AccessListener;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;

import org.radixware.kernel.common.defs.ads.type.AdsAccessFlags;
import org.radixware.kernel.common.enums.EClassType;


public class GeneralPanel extends javax.swing.JPanel implements ItemListener {

    private AdsClassDef adsClassDef = null;
    private boolean isInterface = false;
    private boolean isUpdating;

    /** Creates new form GeneralPanel */
    public GeneralPanel() {
        super();
        initComponents();
        abstractCheckBox.addItemListener(this);
        deprecatedCheckBox.addItemListener(this);        
        setMaximumSize(new Dimension(getMaximumSize().width, accessBox.getPreferredSize().height));
    }
    private final AccessListener listener = new AccessListener() {

        @Override
        public void onEvent(AccessChangedEvent e) {
            if (adsClassDef != null) {
                if (!isUpdating) {
                    update();
                }
            }
        }
    };

    public void open(AdsClassDef adsClassDef) {
        this.adsClassDef = adsClassDef;
        isInterface = adsClassDef.getClassDefType() == EClassType.INTERFACE;
        this.adsClassDef.getAccessChangeSupport().addEventListener(listener);
        abstractCheckBox.setVisible(!(adsClassDef instanceof AdsModelClassDef));
        update();
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                setSize(getSize());
                revalidate();
                repaint();
            }
        });
    }

    public void update() {

        isUpdating = true;
        final AdsAccessFlags adsAccessFlags = adsClassDef.getAccessFlags();

//        boolean isModel = adsClassDef instanceof AdsModelClassDef;
        boolean readonly = adsClassDef.isReadOnly();

//        accessLabel.setVisible(!isModel);
//        accessBox.setVisible(!isModel);
        //gap.setVisible(!isModel);
//        if (!isModel) {
            accessLabel.setEnabled(!readonly);
            accessBox.setEnabled(!readonly);

            accessBox.open(adsClassDef);
//        }

        if (isInterface) {
            if (abstractCheckBox.isVisible()) {
                this.remove(abstractCheckBox);
            }
            deprecatedCheckBox.setSelected(adsAccessFlags.isDeprecated());
        } else {
            abstractCheckBox.setSelected(adsAccessFlags.isAbstract());
            deprecatedCheckBox.setSelected(adsAccessFlags.isDeprecated());
            if (this.adsClassDef.isFinal()) {
                abstractCheckBox.setEnabled(false);
            } else {
                abstractCheckBox.setEnabled(!readonly);
            }
        }
        isUpdating = false;

    }

    @Override
    public Dimension getMinimumSize() {
        Dimension d = super.getMinimumSize();
        return new Dimension(d.width, accessBox.getMinimumSize().height);
    }

    public void setReadonly(boolean readonly) {

        if (isInterface) {
            deprecatedCheckBox.setEnabled(!readonly);
        } else {
            abstractCheckBox.setEnabled(!readonly);
            deprecatedCheckBox.setEnabled(!readonly);
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {

        if (isUpdating) {
            return;
        }

        final Object source = e.getItemSelectable();
        final boolean selected = e.getStateChange() == ItemEvent.SELECTED;

        if (source == abstractCheckBox) {
            adsClassDef.getAccessFlags().setAbstract(selected);

            if (selected) {
                adsClassDef.getAccessFlags().setDeprecated(false);
            }
        } else if (source == deprecatedCheckBox) {
            adsClassDef.getAccessFlags().setDeprecated(selected);
        }
//        } else {
//            assert(source == finalCheckBox);
//            adsClassDef.getAccessFlags().setFinal(selected);
//
//            if (selected){
//                if (abstractCheckBox.isSelected()){
//                    abstractCheckBox.setSelected(false);
//                    adsClassDef.getAccessFlags().setAbstract(false);
//                }
//            }
//        }
    }

//    @Override
//    public Dimension getMinimumSize() {
//        return getPreferredSize();
//    }
//
//    @Override
//    public int getHeight() {
//        return getPreferredSize().height;
//    }
//
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        accessLabel = new javax.swing.JLabel();
        accessBox = new org.radixware.kernel.designer.ads.common.dialogs.AccessEditPanel();
        abstractCheckBox = new javax.swing.JCheckBox();
        deprecatedCheckBox = new javax.swing.JCheckBox();

        accessLabel.setText(org.openide.util.NbBundle.getMessage(GeneralPanel.class, "AccessabilityComboBoxTip")); // NOI18N

        abstractCheckBox.setText(org.openide.util.NbBundle.getMessage(GeneralPanel.class, "GeneralPanel.abstractCheckBox.text")); // NOI18N

        deprecatedCheckBox.setText(org.openide.util.NbBundle.getMessage(GeneralPanel.class, "GeneralPanel.deprecatedCheckBox.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(accessLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(accessBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(abstractCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deprecatedCheckBox)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(accessLabel)
                    .addComponent(accessBox, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(abstractCheckBox)
                    .addComponent(deprecatedCheckBox))
                .addGap(0, 0, 0))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox abstractCheckBox;
    private org.radixware.kernel.designer.ads.common.dialogs.AccessEditPanel accessBox;
    private javax.swing.JLabel accessLabel;
    private javax.swing.JCheckBox deprecatedCheckBox;
    // End of variables declaration//GEN-END:variables
}
