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
 * ReturnTypePanel.java
 *
 * Created on Dec 4, 2008, 4:25:34 PM
 */
package org.radixware.kernel.designer.ads.method.profile;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumSet;
import java.util.Set;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.components.ExtendableTextField.ExtendableTextChangeEvent;
import org.radixware.kernel.common.components.ExtendableTextField.ExtendableTextChangeListener;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.common.JavaSignatures;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.common.dialogs.choosetype.ChooseType;
import org.radixware.kernel.designer.common.dialogs.choosetype.ETypeNature;
import org.radixware.kernel.designer.common.dialogs.choosetype.ITypeFilter;
import org.radixware.kernel.designer.common.dialogs.choosetype.TypeEditDisplayer;
import org.radixware.kernel.designer.common.dialogs.components.DescriptionPanel;
import org.radixware.kernel.designer.common.dialogs.components.description.DescriptionEditor;
import org.radixware.kernel.designer.common.dialogs.components.description.DescriptionModel;
import org.radixware.kernel.designer.common.dialogs.components.state.StateManager;


public class ReturnTypePanel extends javax.swing.JPanel {

    private AdsTypeDeclaration returnType;
    private AdsMethodDef method;
    private AdsClassDef owner;
    private JButton returnTypeBtn;
    private JButton resetTypeBtn;
    private JButton descriptionBtn;
    private Color defautlForeground;
    private JButton[] nameFeatures;
    private boolean isOverride;
    private JButton fixButton;
    private ActionListener fixButtonListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            AdsMethodDef ovr = method.getHierarchy().findOverridden().get();
            if (ovr != null) {
                fixButton.setVisible(false);
                namePanel.setValue(ovr.getName());
                namePanel.setForeground(defautlForeground);
                namePanel.removeButton(fixButton);
                namePanel.repaint();
                fixButton = null;
            }
        }
    };
    private DescriptionModel descriptionModel;

    public ReturnTypePanel(int nameFeatureCount) {
        initComponents();

        //returnBtn setup
        returnTypeBtn = returnEditor.addButton();
        returnTypeBtn.setIcon(RadixWareIcons.DIALOG.CHOOSE.getIcon(13, 13));
        returnTypeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isOverride) {
                    refineReturnType();
                } else {
                    editReturnType();
                }
            }
        });
        returnTypeBtn.setToolTipText(NbBundle.getMessage(ReturnTypePanel.class, "ReturnTip"));

        //descriptionBtn setup
        descriptionBtn = returnEditor.addButton();
        descriptionBtn.setToolTipText(NbBundle.getMessage(ReturnTypePanel.class, "PP-DescriptionTip"));
        descriptionBtn.setIcon(RadixWareIcons.EDIT.EDIT.getIcon(13, 13));
        descriptionBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editDescription();
            }
        });

        //voidBtn setup
        Icon voidicon = RadixWareIcons.METHOD.VOID.getIcon(13, 13);
        resetTypeBtn = returnEditor.addButton();
        resetTypeBtn.setToolTipText(NbBundle.getMessage(ReturnTypePanel.class, "VoidTip"));
        resetTypeBtn.setIcon(voidicon);
        resetTypeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!returnType.isVoid()) {
                    returnType = AdsTypeDeclaration.Factory.voidType();
                    returnEditor.setValue(returnType.getName(owner));
                    changeSupport.fireChange();
                } else {
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        });

        if (nameFeatureCount > 0) {
            nameFeatures = new JButton[nameFeatureCount];
            for (int i = 0; i < nameFeatureCount; i++) {
                nameFeatures[i] = namePanel.addButton();
            }
        }

        namePanel.getChangeSupport().addEventListener(new ExtendableTextChangeListener() {
            @Override
            public void onEvent(ExtendableTextChangeEvent e) {
                ReturnTypePanel.this.changeSupport.fireChange();
            }
        });
        defautlForeground = namePanel.getForeground();
    }

    @Override
    public void requestFocus() {
        namePanel.requestFocusInWindow();
    }

    public JButton[] getNameFeatures() {
        return nameFeatures;
    }

    DescriptionModel getDescriptionModel() {
        return descriptionModel;
    }

    void open(AdsMethodDef method, AdsClassDef owner, boolean readonly) {
        this.method = method;
        this.owner = owner;
        this.isOverride = method != null && !method.getHierarchy().findOverridden().isEmpty();

        namePanel.setValue(method.getName());
        AdsMethodDef ovr = method.getHierarchy().findOverridden().get();
        if (ovr != null && !method.getName().equals(ovr.getName())) {
            namePanel.setForeground(Color.RED);
            if (fixButton == null) {
                fixButton = namePanel.addButton();
                fixButton.setIcon(RadixWareIcons.EDIT.FIX.getIcon());
                fixButton.setToolTipText("Fix Overriden Name");
                fixButton.addActionListener(fixButtonListener);
            }
        } else {
            namePanel.setForeground(defautlForeground);
        }

        if (method.getProfile().getReturnValue() != null) {
            returnType = method.getProfile().getReturnValue().getType();
            returnEditor.setValue(returnType.getName(owner));
        }

        descriptionModel = DescriptionModel.Factory.newInstance(method.getProfile().getReturnValue());

        enableAvailableTools(readonly);
    }

    void apply() {
        if (!method.isConstructor()) {
            method.getProfile().getReturnValue().setType(returnType);
            method.setName((String) namePanel.getValue());

            descriptionModel.applyFor(method.getProfile().getReturnValue());
        }
    }

    private void enableAvailableTools(boolean readonly) {
        namePanel.setEditable(!readonly && !isOverride);
        returnTypeBtn.setEnabled(!readonly && (!isOverride || isRefinable()));
        resetTypeBtn.setEnabled(!readonly && !isOverride);
    }

    private boolean isRefinable() {
        AdsMethodDef overridden = method.getHierarchy().findOverridden().get();

        if (overridden != null && method.getHierarchy().findOverwritten().isEmpty()) {
            AdsTypeDeclaration baseType = overridden.getProfile().getReturnValue().getType();
            if (baseType.isArray()) {
                baseType = baseType.getArrayItemType();
            }

            Set<ETypeNature> refinableNatureSet = EnumSet.of(ETypeNature.JAVA_CLASS, ETypeNature.RADIX_CLASS);

            ETypeNature typeNature = ETypeNature.getByType(baseType, overridden);
            return refinableNatureSet.contains(typeNature);
        }
        return false;
    }

    private void refineReturnType() {
        AdsMethodDef overridden = method.getHierarchy().findOverridden().get();
        if (overridden != null) {

            AdsTypeDeclaration baseMethodReturnType = overridden.getProfile().getReturnValue().getType();
            AdsTypeDeclaration currentMethodReturnType = method.getProfile().getReturnValue().getType();
            ITypeFilter typeFilter = new ChooseType.DefaultTypeFilter(method, method.getProfile().getReturnValue(), baseMethodReturnType);
            AdsTypeDeclaration newType = ChooseType.getInstance().refineType(currentMethodReturnType, typeFilter);

            if (newType != null && !newType.equalsTo(method, returnType)) {
                returnType = newType;
                returnEditor.setValue(returnType.getName(method));
                changeSupport.fireChange();
            }
        }
    }

    private void editReturnType() {
        final TypeEditDisplayer typeEditDisplayer = new TypeEditDisplayer();
        final AdsTypeDeclaration newType = typeEditDisplayer.editType(returnType, method, method.getProfile().getReturnValue());

        if (newType != null && !newType.equalsTo(method, returnType)) {
            returnType = newType;
            returnEditor.setValue(returnType.getName(method));
            changeSupport.fireChange();
        }
    }

    String getCurrentName() {
        return (String) namePanel.getValue();
    }

    AdsTypeDeclaration getReturnType() {
        return returnType != null ? returnType : AdsTypeDeclaration.Factory.voidType();
    }

    private void editDescription() {
        final DescriptionEditor descriptionEditor = new DescriptionPanel();
        descriptionEditor.open(descriptionModel);

        if (DescriptionPanel.showModal(descriptionEditor)) {
            descriptionEditor.commit();
        }
    }

    public Color getNameForeground() {
        return namePanel.getForeground();
    }

    public void setNameForeground(Color fg) {
        namePanel.setForeground(fg);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        returnEditor = new org.radixware.kernel.common.components.ExtendableTextField();
        namePanel = new org.radixware.kernel.common.components.ExtendableTextField();

        jLabel1.setText(org.openide.util.NbBundle.getMessage(ReturnTypePanel.class, "NameTip")); // NOI18N

        jLabel2.setText(org.openide.util.NbBundle.getMessage(ReturnTypePanel.class, "PP-Return")); // NOI18N

        returnEditor.setEditable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addGap(13, 13, 13)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(namePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)
                    .addComponent(returnEditor, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(namePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(returnEditor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private org.radixware.kernel.common.components.ExtendableTextField namePanel;
    private org.radixware.kernel.common.components.ExtendableTextField returnEditor;
    // End of variables declaration//GEN-END:variables
    private ChangeSupport changeSupport = new ChangeSupport(this);

    public void addChangeListener(ChangeListener l) {
        changeSupport.addChangeListener(l);
    }

    public void removeChangeListener(ChangeListener l) {
        changeSupport.removeChangeListener(l);
    }
    private final StateManager stateManager = new StateManager(this);

    public boolean isComplete() {
        String methodName = (String) namePanel.getValue();
        if (JavaSignatures.isCorrectJavaIdentifier(methodName)) {
            stateManager.ok();
            return true;
        }
        stateManager.error(NbBundle.getMessage(ReturnTypePanel.class, "Error-Invalid-Name"));
        return false;
    }
}
