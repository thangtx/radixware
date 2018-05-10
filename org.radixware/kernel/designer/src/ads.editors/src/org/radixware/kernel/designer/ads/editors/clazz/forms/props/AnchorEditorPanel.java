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
 * AnchorEditorPanel.java
 *
 * Created on Dec 14, 2011, 2:15:44 PM
 */
package org.radixware.kernel.designer.ads.editors.clazz.forms.props;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JSpinner;
import org.openide.explorer.propertysheet.PropertyEnv;
import org.openide.util.Exceptions;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty.AnchorProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;


public class AnchorEditorPanel extends javax.swing.JPanel implements PropertyChangeListener {

    private AnchorEditor editor;
    private PropertyEnv env;
    private boolean updating;
    private final ChooseWidgetCfg cfg;

    /** Creates new form AnchorEditorPanel */
    public AnchorEditorPanel(AnchorEditor editor, final PropertyEnv env) {
        initComponents();

        env.setState(PropertyEnv.STATE_NEEDS_VALIDATION);
        env.addPropertyChangeListener(this);
        this.editor = editor;
        this.env = env;
        cfg = new ChooseWidgetCfg(getNode().getOwnerWidget());
        init();


        chAnchorTop.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (updating) {
                    return;
                }

                if (chAnchorTop.isSelected()) {
                    Rectangle nodeGeometry = getGeometry();
                    edTopPart.setValue(0f);
                    edTopOffset.setValue(nodeGeometry.y);
                    edTopRef.open(cfg, null, null);
                }
                updateEnabled();
            }
        });
        chAnchorLeft.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (updating) {
                    return;
                }
                if (chAnchorLeft.isSelected()) {
                    Rectangle nodeGeometry = getGeometry();
                    edLeftPart.setValue(0f);
                    edLeftOffset.setValue(nodeGeometry.x);
                    edLeftRef.open(cfg, null, null);
                }
                updateEnabled();
            }
        });
        chAnchorRight.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (updating) {
                    return;
                }

                if (chAnchorRight.isSelected()) {
                    Rectangle nodeGeometry = getGeometry();
                    Rectangle ownerGeometry = getGeometry(getNode().getOwnerWidget());
                    edRightPart.setValue(1f);
                    edRightOffset.setValue(-(ownerGeometry.width - nodeGeometry.x - nodeGeometry.width));
                    edRightRef.open(cfg, null, null);
                }
                updateEnabled();
            }
        });
        chAnchorBottom.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (updating) {
                    return;
                }

                if (chAnchorBottom.isSelected()) {
                    Rectangle nodeGeometry = getGeometry();
                    Rectangle ownerGeometry = getGeometry(getNode().getOwnerWidget());
                    edBottomPart.setValue(1f);
                    edBottomOffset.setValue(-(ownerGeometry.height - nodeGeometry.y - nodeGeometry.height));
                    edBottomRef.open(cfg, null, null);
                }
                updateEnabled();
            }
        });
    }

    private Rectangle getGeometry() {
        return getGeometry(getNode());
    }

    private Rectangle getGeometry(AdsRwtWidgetDef w) {
        return ((AdsUIProperty.RectProperty) AdsUIUtil.getUiProperty(w, "geometry")).getRectangle();
    }

    private AdsRwtWidgetDef getNode() {
        return (AdsRwtWidgetDef) ((UIPropertySupport) editor.getSource()).getNode();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pAnchorRight = new javax.swing.JPanel();
        chAnchorRight = new javax.swing.JCheckBox();
        edRightPart = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        edRightOffset = new javax.swing.JSpinner();
        edRightRef = new org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel();
        jLabel8 = new javax.swing.JLabel();
        pAnchorLeft = new javax.swing.JPanel();
        chAnchorLeft = new javax.swing.JCheckBox();
        edLeftPart = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        edLeftOffset = new javax.swing.JSpinner();
        jLabel7 = new javax.swing.JLabel();
        edLeftRef = new org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel();
        pAnchorTop = new javax.swing.JPanel();
        chAnchorTop = new javax.swing.JCheckBox();
        edTopPart = new javax.swing.JSpinner();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        edTopOffset = new javax.swing.JSpinner();
        edTopRef = new org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel();
        jLabel3 = new javax.swing.JLabel();
        pAnchorBottom = new javax.swing.JPanel();
        chAnchorBottom = new javax.swing.JCheckBox();
        edBottomPart = new javax.swing.JSpinner();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        edBottomOffset = new javax.swing.JSpinner();
        edBottomRef = new org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel();
        jLabel4 = new javax.swing.JLabel();

        pAnchorRight.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(AnchorEditorPanel.class, "AnchorEditorPanel.pAnchorRight.border.title"))); // NOI18N
        pAnchorRight.setPreferredSize(new java.awt.Dimension(400, 160));

        chAnchorRight.setText(org.openide.util.NbBundle.getMessage(AnchorEditorPanel.class, "AnchorEditorPanel.chAnchorRight.text")); // NOI18N

        edRightPart.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), null, null, Float.valueOf(1.0f)));

        jLabel1.setText(org.openide.util.NbBundle.getMessage(AnchorEditorPanel.class, "AnchorEditorPanel.jLabel1.text")); // NOI18N

        jLabel2.setText(org.openide.util.NbBundle.getMessage(AnchorEditorPanel.class, "AnchorEditorPanel.jLabel2.text")); // NOI18N

        edRightOffset.setModel(new javax.swing.SpinnerNumberModel());

        jLabel8.setText(org.openide.util.NbBundle.getMessage(AnchorEditorPanel.class, "AnchorEditorPanel.jLabel8.text")); // NOI18N

        javax.swing.GroupLayout pAnchorRightLayout = new javax.swing.GroupLayout(pAnchorRight);
        pAnchorRight.setLayout(pAnchorRightLayout);
        pAnchorRightLayout.setHorizontalGroup(
            pAnchorRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pAnchorRightLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pAnchorRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pAnchorRightLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(pAnchorRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1)
                            .addComponent(jLabel8))
                        .addGap(18, 18, 18)
                        .addGroup(pAnchorRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(edRightPart, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                            .addComponent(edRightOffset, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                            .addComponent(edRightRef, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)))
                    .addComponent(chAnchorRight, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        pAnchorRightLayout.setVerticalGroup(
            pAnchorRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pAnchorRightLayout.createSequentialGroup()
                .addComponent(chAnchorRight, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pAnchorRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(edRightPart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pAnchorRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(edRightOffset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pAnchorRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(edRightRef, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pAnchorLeft.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(AnchorEditorPanel.class, "AnchorEditorPanel.pAnchorLeft.border.title"))); // NOI18N
        pAnchorLeft.setPreferredSize(new java.awt.Dimension(400, 160));

        chAnchorLeft.setText(org.openide.util.NbBundle.getMessage(AnchorEditorPanel.class, "AnchorEditorPanel.chAnchorLeft.text")); // NOI18N

        edLeftPart.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), null, null, Float.valueOf(1.0f)));

        jLabel5.setText(org.openide.util.NbBundle.getMessage(AnchorEditorPanel.class, "AnchorEditorPanel.jLabel5.text")); // NOI18N

        jLabel6.setText(org.openide.util.NbBundle.getMessage(AnchorEditorPanel.class, "AnchorEditorPanel.jLabel6.text")); // NOI18N

        edLeftOffset.setModel(new javax.swing.SpinnerNumberModel());

        jLabel7.setText(org.openide.util.NbBundle.getMessage(AnchorEditorPanel.class, "AnchorEditorPanel.jLabel7.text")); // NOI18N

        javax.swing.GroupLayout pAnchorLeftLayout = new javax.swing.GroupLayout(pAnchorLeft);
        pAnchorLeft.setLayout(pAnchorLeftLayout);
        pAnchorLeftLayout.setHorizontalGroup(
            pAnchorLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pAnchorLeftLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pAnchorLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pAnchorLeftLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(pAnchorLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pAnchorLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(edLeftRef, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
                            .addComponent(edLeftOffset, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
                            .addComponent(edLeftPart, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)))
                    .addComponent(chAnchorLeft))
                .addContainerGap())
        );
        pAnchorLeftLayout.setVerticalGroup(
            pAnchorLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pAnchorLeftLayout.createSequentialGroup()
                .addComponent(chAnchorLeft, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pAnchorLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(edLeftPart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pAnchorLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(edLeftOffset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pAnchorLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(edLeftRef, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pAnchorTop.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(AnchorEditorPanel.class, "AnchorEditorPanel.pAnchorTop.border.title"))); // NOI18N
        pAnchorTop.setPreferredSize(new java.awt.Dimension(400, 160));

        chAnchorTop.setText(org.openide.util.NbBundle.getMessage(AnchorEditorPanel.class, "AnchorEditorPanel.chAnchorTop.text")); // NOI18N

        edTopPart.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), null, null, Float.valueOf(1.0f)));

        jLabel9.setText(org.openide.util.NbBundle.getMessage(AnchorEditorPanel.class, "AnchorEditorPanel.jLabel9.text")); // NOI18N

        jLabel10.setText(org.openide.util.NbBundle.getMessage(AnchorEditorPanel.class, "AnchorEditorPanel.jLabel10.text")); // NOI18N

        edTopOffset.setModel(new javax.swing.SpinnerNumberModel());

        jLabel3.setText(org.openide.util.NbBundle.getMessage(AnchorEditorPanel.class, "AnchorEditorPanel.jLabel3.text")); // NOI18N

        javax.swing.GroupLayout pAnchorTopLayout = new javax.swing.GroupLayout(pAnchorTop);
        pAnchorTop.setLayout(pAnchorTopLayout);
        pAnchorTopLayout.setHorizontalGroup(
            pAnchorTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pAnchorTopLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pAnchorTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chAnchorTop)
                    .addGroup(pAnchorTopLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(pAnchorTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addGroup(pAnchorTopLayout.createSequentialGroup()
                                .addGroup(pAnchorTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel3))
                                .addGap(22, 22, 22)
                                .addGroup(pAnchorTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(edTopRef, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)
                                    .addComponent(edTopPart, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)
                                    .addComponent(edTopOffset, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE))))))
                .addContainerGap())
        );
        pAnchorTopLayout.setVerticalGroup(
            pAnchorTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pAnchorTopLayout.createSequentialGroup()
                .addComponent(chAnchorTop)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pAnchorTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(edTopPart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pAnchorTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(edTopOffset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pAnchorTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(edTopRef, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pAnchorBottom.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(AnchorEditorPanel.class, "AnchorEditorPanel.pAnchorBottom.border.title"))); // NOI18N
        pAnchorBottom.setPreferredSize(new java.awt.Dimension(400, 160));

        chAnchorBottom.setText(org.openide.util.NbBundle.getMessage(AnchorEditorPanel.class, "AnchorEditorPanel.chAnchorBottom.text")); // NOI18N

        edBottomPart.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), null, null, Float.valueOf(1.0f)));

        jLabel11.setText(org.openide.util.NbBundle.getMessage(AnchorEditorPanel.class, "AnchorEditorPanel.jLabel11.text")); // NOI18N

        jLabel12.setText(org.openide.util.NbBundle.getMessage(AnchorEditorPanel.class, "AnchorEditorPanel.jLabel12.text")); // NOI18N

        edBottomOffset.setModel(new javax.swing.SpinnerNumberModel());

        jLabel4.setText(org.openide.util.NbBundle.getMessage(AnchorEditorPanel.class, "AnchorEditorPanel.jLabel4.text")); // NOI18N

        javax.swing.GroupLayout pAnchorBottomLayout = new javax.swing.GroupLayout(pAnchorBottom);
        pAnchorBottom.setLayout(pAnchorBottomLayout);
        pAnchorBottomLayout.setHorizontalGroup(
            pAnchorBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pAnchorBottomLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pAnchorBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chAnchorBottom)
                    .addGroup(pAnchorBottomLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(pAnchorBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel11)
                            .addComponent(jLabel12))
                        .addGap(12, 12, 12)
                        .addGroup(pAnchorBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(edBottomPart, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
                            .addComponent(edBottomOffset, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
                            .addComponent(edBottomRef, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE))))
                .addContainerGap())
        );
        pAnchorBottomLayout.setVerticalGroup(
            pAnchorBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pAnchorBottomLayout.createSequentialGroup()
                .addComponent(chAnchorBottom)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pAnchorBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(edBottomPart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pAnchorBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(edBottomOffset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pAnchorBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(edBottomRef, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(pAnchorTop, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)
                    .addComponent(pAnchorLeft, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pAnchorRight, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)
                    .addComponent(pAnchorBottom, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pAnchorRight, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(pAnchorBottom, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(pAnchorLeft, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(8, 8, 8)
                            .addComponent(pAnchorTop, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox chAnchorBottom;
    private javax.swing.JCheckBox chAnchorLeft;
    private javax.swing.JCheckBox chAnchorRight;
    private javax.swing.JCheckBox chAnchorTop;
    private javax.swing.JSpinner edBottomOffset;
    private javax.swing.JSpinner edBottomPart;
    private org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel edBottomRef;
    private javax.swing.JSpinner edLeftOffset;
    private javax.swing.JSpinner edLeftPart;
    private org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel edLeftRef;
    private javax.swing.JSpinner edRightOffset;
    private javax.swing.JSpinner edRightPart;
    private org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel edRightRef;
    private javax.swing.JSpinner edTopOffset;
    private javax.swing.JSpinner edTopPart;
    private org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel edTopRef;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel pAnchorBottom;
    private javax.swing.JPanel pAnchorLeft;
    private javax.swing.JPanel pAnchorRight;
    private javax.swing.JPanel pAnchorTop;
    // End of variables declaration//GEN-END:variables

    private AdsUIProperty.AnchorProperty getProperty() {
        return (AdsUIProperty.AnchorProperty) editor.getValue();
    }

    private float floatValue(JSpinner spinner) {
        Object obj = spinner.getValue();
        if (obj instanceof Float) {
            return ((Float) obj).floatValue();
        } else {
            return 0;
        }
    }

    private int intValue(JSpinner spinner) {
        Object obj = spinner.getValue();
        if (obj instanceof Integer) {
            return ((Integer) obj).intValue();
        } else {
            return 0;
        }
    }

    private class ChooseWidgetCfg extends ChooseDefinitionCfg {

        public ChooseWidgetCfg(final AdsRwtWidgetDef context) {
            super(context.getWidgets().list(new IFilter<AdsRwtWidgetDef>() {

                @Override
                public boolean isTarget(AdsRwtWidgetDef radixObject) {
                    return radixObject != context;
                }
            }));
        }
    }

    private void init() {
        updating = true;
        AdsUIProperty.AnchorProperty prop = getProperty();

        if (prop.getTop() != null) {
            AnchorProperty.Anchor a = prop.getTop();
            chAnchorTop.setSelected(true);
            edTopPart.setValue(a.part);
            edTopOffset.setValue(a.offset);
            edTopRef.open(cfg, resolve(a.refId), a.refId);

        }
        if (prop.getLeft() != null) {
            AnchorProperty.Anchor a = prop.getLeft();
            chAnchorLeft.setSelected(true);
            edLeftPart.setValue(a.part);
            edLeftOffset.setValue(a.offset);
            edLeftRef.open(cfg, resolve(a.refId), a.refId);
        }
        if (prop.getBottom() != null) {
            AnchorProperty.Anchor a = prop.getBottom();
            chAnchorBottom.setSelected(true);
            edBottomPart.setValue(a.part);
            edBottomOffset.setValue(a.offset);
            edBottomRef.open(cfg, resolve(a.refId), a.refId);
        }
        if (prop.getRight() != null) {
            AnchorProperty.Anchor a = prop.getRight();
            chAnchorRight.setSelected(true);
            edRightPart.setValue(a.part);
            edRightOffset.setValue(a.offset);
            edRightRef.open(cfg, resolve(a.refId), a.refId);
        }


        updateEnabled();
        updating = false;
    }

    private Definition resolve(Id id) {
        return getNode().getOwnerWidget().findWidgetById(id);
    }

    private void updateEnabled() {
        AdsUIProperty.AnchorProperty prop = getProperty();
        if (prop.isReadOnly()) {
            chAnchorBottom.setEnabled(false);
            chAnchorLeft.setEnabled(false);
            chAnchorRight.setEnabled(false);
            chAnchorTop.setEnabled(false);
            edBottomOffset.setEnabled(false);
            edTopOffset.setEnabled(false);
            edLeftOffset.setEnabled(false);
            edRightOffset.setEnabled(false);
            edBottomPart.setEnabled(false);
            edLeftPart.setEnabled(false);
            edTopPart.setEnabled(false);
            edRightPart.setEnabled(false);
            edRightRef.setEnabled(false);
            edTopRef.setEnabled(false);
            edBottomRef.setEnabled(false);
            edLeftRef.setEnabled(false);
        } else {
            chAnchorBottom.setEnabled(true);
            chAnchorLeft.setEnabled(true);
            chAnchorRight.setEnabled(true);
            chAnchorTop.setEnabled(true);
            edBottomOffset.setEnabled(chAnchorBottom.isSelected());
            edTopOffset.setEnabled(chAnchorTop.isSelected());
            edLeftOffset.setEnabled(chAnchorLeft.isSelected());
            edRightOffset.setEnabled(chAnchorRight.isSelected());
            edBottomPart.setEnabled(chAnchorBottom.isSelected());
            edLeftPart.setEnabled(chAnchorLeft.isSelected());
            edTopPart.setEnabled(chAnchorTop.isSelected());
            edRightPart.setEnabled(chAnchorRight.isSelected());
            edRightRef.setEnabled(chAnchorRight.isSelected());
            edTopRef.setEnabled(chAnchorTop.isSelected());
            edBottomRef.setEnabled(chAnchorBottom.isSelected());
            edLeftRef.setEnabled(chAnchorLeft.isSelected());
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (PropertyEnv.PROP_STATE.equals(evt.getPropertyName()) && evt.getNewValue() == PropertyEnv.STATE_VALID) {
            AdsUIProperty.AnchorProperty prop = getProperty();

            if (chAnchorTop.isSelected()) {
                prop.setTop(new AnchorProperty.Anchor(floatValue(edTopPart), intValue(edTopOffset), edTopRef.getDefinitionId()));
            } else {
                prop.setTop(null);
            }

            if (chAnchorLeft.isSelected()) {
                prop.setLeft(new AnchorProperty.Anchor(floatValue(edLeftPart), intValue(edLeftOffset), edLeftRef.getDefinitionId()));
            } else {
                prop.setLeft(null);
            }

            if (chAnchorRight.isSelected()) {
                prop.setRight(new AnchorProperty.Anchor(floatValue(edRightPart), intValue(edRightOffset), edRightRef.getDefinitionId()));
            } else {
                prop.setRight(null);
            }


            if (chAnchorBottom.isSelected()) {
                prop.setBottom(new AnchorProperty.Anchor(floatValue(edBottomPart), intValue(edBottomOffset), edBottomRef.getDefinitionId()));
            } else {
                prop.setBottom(null);
            }

            editor.setValue(prop);
            ((UIPropertySupport) editor.getSource()).setValue(prop);
        }
    }
}
