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
package org.radixware.kernel.designer.common.editors.editmask;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import javax.swing.JButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.Exceptions;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMask;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskBool;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.localization.ILocalizingBundleDef;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.ECompatibleTypesForBool;
import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;

public class BooleanEditor extends Editor {

    private boolean readOnly = false;
    EEditMaskType contextEditMaskType;
    private boolean titleVisible = false;
    private Id trueTitleId;
    private Id falseTitleId;
    private String trueTitle;
    private String falseTitle;
    private AdsMultilingualStringDef trueTitleDef;
    private AdsMultilingualStringDef falseTitleDef;
    private ECompatibleTypesForBool type;
    final private DecimalFormat decimalFormat;
    private final EEditMaskType editMaskType;
    private AdsDefinition definition;
    private ChangeListener listener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                updateWarning();
            }
    };

    public BooleanEditor(AdsDefinition def, final EditMaskBool editMaskBool, EEditMaskType editMaskType) {
        initComponents();
        this.definition = def;
        this.editMaskType = editMaskType;
        if (editMaskBool != null) {
            setupInitialValues(editMaskBool);
        }
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(getLocale());
        decimalFormat = new DecimalFormat("#.############", otherSymbols);
        System.out.println("\n\ndecimalFormat: " + decimalFormat + "; applying: " + decimalFormat.format(9345.12345678) + "; " + decimalFormat.format(1234567890) + "; locale: " + getLocale());
        this.contextEditMaskType = editMaskType;

        this.trueValueTitle.setEditable(false);
        JButton button = this.trueValueTitle.addButton(RadixWareDesignerIcon.EDIT.EDIT.getIcon());
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                TitleModalEditor editor = new TitleModalEditor(definition, trueTitleDef, new TitleModalEditor.TitleProvider() {

                    @Override
                    public Id getTitleId() {
                        return trueTitleId;
                    }

                    @Override
                    public void setTitleId(Id id) {
                        trueTitleId = id;
                        if (trueTitleId != null && trueTitleId == trueTitleDef.getId()) {
                            trueValueTitle.setValue(getDisplayString(trueTitleDef));
                        } else {
                            trueValueTitle.setValue(trueTitle);
                        }
                    }

                    @Override
                    public String getTitle() {
                        return trueTitle;
                    }

                    @Override
                    public void setTitle(String title) {
                        trueTitle = title;
                        if (trueTitleId != null && trueTitleId == trueTitleDef.getId()) {
                            trueValueTitle.setValue(getDisplayString(trueTitleDef));
                        } else {
                            trueValueTitle.setValue(trueTitle);
                        }
                    }
                });
                editor.open();

            }
        });
        this.falseValueTitle.setEditable(false);
        button = this.falseValueTitle.addButton(RadixWareDesignerIcon.EDIT.EDIT.getIcon());
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                TitleModalEditor editor = new TitleModalEditor(definition, falseTitleDef, new TitleModalEditor.TitleProvider() {

                    @Override
                    public Id getTitleId() {
                        return falseTitleId;
                    }

                    @Override
                    public void setTitleId(Id id) {
                        falseTitleId = id;
                        if (falseTitleId != null && falseTitleId == falseTitleDef.getId()) {
                            falseValueTitle.setValue(getDisplayString(falseTitleDef));
                        } else {
                            falseValueTitle.setValue(falseTitle);
                        }
                    }

                    @Override
                    public String getTitle() {
                        return falseTitle;
                    }

                    @Override
                    public void setTitle(String title) {
                        falseTitle = title;
                        if (falseTitleId != null && falseTitleId == falseTitleDef.getId()) {
                            falseValueTitle.setValue(getDisplayString(falseTitleDef));
                        } else {
                            falseValueTitle.setValue(falseTitle);
                        }
                    }
                });
                editor.open();
            }
        });
        this.trueValueEditor.setNullAble(true);
        this.falseValueEditor.setNullAble(true);
        if (trueTitleDef == null) {
            trueTitleDef = AdsMultilingualStringDef.Factory.newInstance();
        }
        if (falseTitleDef == null) {
            falseTitleDef = AdsMultilingualStringDef.Factory.newInstance();
        }
        
        if (editMaskType == EEditMaskType.BOOL) {
            trueValueEditor.setVisible(false);
            falseValueEditor.setVisible(false);
            jLabel3.setVisible(false);
            jLabel4.setVisible(false);
            jLabel1.setText(org.openide.util.NbBundle.getMessage(BooleanEditor.class, "BooleanEditor.jLabel1.Bool.text"));
            jLabel2.setText(org.openide.util.NbBundle.getMessage(BooleanEditor.class, "BooleanEditor.jLabel2.Bool.text"));
        }
        
        this.trueValueEditor.addChangeListener(listener);
        this.falseValueEditor.addChangeListener(listener);

        updateWarning();
    }

    private String getDisplayString(AdsMultilingualStringDef def) {
        String result = def.getValue(EIsoLanguage.ENGLISH);

        if (result == null || result.isEmpty()) {
            for (EIsoLanguage lang : def.getLanguages()) {
                result = def.getValue(lang);
                if (result != null && result.isEmpty()) {
                    return result;
                }
            }
            return "";
        } else {
            return result;
        }

    }

    private AdsMultilingualStringDef findTitle(EditMask editMask, Id id) {
        if (id == null) {
            return null;
        }
        AdsDefinition def = editMask.getOwnerDef();
        if (def != null) {
            return def.findExistingLocalizingBundle().findLocalizedString(id);
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        trueValueEditor = new org.radixware.kernel.designer.common.dialogs.components.ValAsStrEditPanel();
        falseValueEditor = new org.radixware.kernel.designer.common.dialogs.components.ValAsStrEditPanel();
        trueValueTitle = new org.radixware.kernel.common.components.ExtendableTextField();
        falseValueTitle = new org.radixware.kernel.common.components.ExtendableTextField();
        notificationLabel = new javax.swing.JLabel();
        visCheckBox = new javax.swing.JCheckBox();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(BooleanEditor.class, "BooleanEditor.jPanel1.border.title"))); // NOI18N
        jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel1.setMaximumSize(new java.awt.Dimension(30000, 30000));
        jPanel1.setName("Select value:"); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(BooleanEditor.class, "BooleanEditor.jLabel1.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(BooleanEditor.class, "BooleanEditor.jLabel2.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(BooleanEditor.class, "BooleanEditor.jLabel3.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel4, org.openide.util.NbBundle.getMessage(BooleanEditor.class, "BooleanEditor.jLabel4.text")); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(falseValueEditor, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(trueValueEditor, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(trueValueTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(falseValueTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)))
                .addGap(56, 56, 56))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(trueValueEditor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(trueValueTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(falseValueEditor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(falseValueTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(97, Short.MAX_VALUE))
        );

        notificationLabel.setFont(new java.awt.Font("Dialog", 2, 11)); // NOI18N
        notificationLabel.setForeground(new java.awt.Color(255, 51, 51));
        org.openide.awt.Mnemonics.setLocalizedText(notificationLabel, org.openide.util.NbBundle.getMessage(BooleanEditor.class, "BooleanEditor.notificationLabel.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(visCheckBox, org.openide.util.NbBundle.getMessage(BooleanEditor.class, "BooleanEditor.visCheckBox.text")); // NOI18N
        visCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                visCheckBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(visCheckBox)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(notificationLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(visCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(notificationLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void visCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_visCheckBoxActionPerformed
        this.titleVisible = visCheckBox.isSelected();
        updateWarning();
    }//GEN-LAST:event_visCheckBoxActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.radixware.kernel.designer.common.dialogs.components.ValAsStrEditPanel falseValueEditor;
    private org.radixware.kernel.common.components.ExtendableTextField falseValueTitle;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel notificationLabel;
    private org.radixware.kernel.designer.common.dialogs.components.ValAsStrEditPanel trueValueEditor;
    private org.radixware.kernel.common.components.ExtendableTextField trueValueTitle;
    private javax.swing.JCheckBox visCheckBox;
    // End of variables declaration//GEN-END:variables

    @Override
    public void apply(EditMask editMask) {
        try {
            if (!(editMask instanceof EditMaskBool)) {
                return;
            }
            type = getEditMaskType();
            EditMaskBool boolEditMask = (EditMaskBool) editMask;
            this.titleVisible = visCheckBox.isSelected();
            boolEditMask.setValueTitleVisible(titleVisible && !readOnly);

            boolEditMask.setTrueTitle(trueTitle);
            boolEditMask.setFalseTitle(falseTitle);

            if (trueTitleDef != null && trueTitleId != null) {
                if (boolEditMask.getTrueTitleId() != null) {
                    AdsMultilingualStringDef string = findTitle(boolEditMask, boolEditMask.getTrueTitleId());
                    if (string != null) {
                        string.delete();
                    }
                }
                boolEditMask.setTrueTitleId(trueTitleDef.getId());
                Definition def = boolEditMask.getOwnerDef();
                if (def != null) {
                    ILocalizingBundleDef bundle = def.findLocalizingBundle();
                    if (bundle != null) {
                        bundle.getStrings().getLocal().add(trueTitleDef);
                    }
                }
            } else {
                if (boolEditMask.getTrueTitleId() != null) {
                    boolEditMask.setTrueTitleId(null);
                }
            }
            if (falseTitleDef != null && falseTitleId != null) {
                if (boolEditMask.getFalseTitleId() != null) {
                    AdsMultilingualStringDef string = findTitle(boolEditMask, boolEditMask.getFalseTitleId());
                    if (string != null) {
                        string.delete();
                    }
                }
                boolEditMask.setFalseTitleId(falseTitleDef.getId());
                Definition def = boolEditMask.getOwnerDef();
                if (def != null) {
                    ILocalizingBundleDef bundle = def.findLocalizingBundle();
                    if (bundle != null) {
                        bundle.getStrings().getLocal().add(falseTitleDef);
                    }
                }
            } else {
                if (boolEditMask.getFalseTitleId() != null) {
                    boolEditMask.setFalseTitleId(null);
                }
            }
            boolEditMask.setCompatibleType(type);
            boolEditMask.setFalseValue(falseValueEditor.getValue() != null? falseValueEditor.getValue().toString() : null);
            boolEditMask.setTrueValue(trueValueEditor.getValue() != null? trueValueEditor.getValue().toString() : null);

            // System.out.println("apply: " + contextEditMaskType + "; trueValue: " + trueValue + "; falsevalue: " + falseValue + "; flsTextField.getText(): " + flsTextField.getText() + "; trueTextField.getText(): " + trueTextField.getText());
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        } finally {
        }
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        visCheckBox.setEnabled(!readOnly);
        trueValueEditor.setEnabled(!readOnly);
        falseValueEditor.setEnabled(!readOnly);
        trueValueTitle.setEnabled(!readOnly);
        falseValueTitle.setEnabled(!readOnly);
        updateWarning();
    }

    private void updateWarning() {
        final ValAsStr trueText;
        final ValAsStr falseText;
        trueText = trueValueEditor.getValue();
        falseText = falseValueEditor.getValue();

        if ((trueText != null && trueText.equals(falseText)) || (trueText == null && falseText == null)) {
            notificationLabel.setVisible(true);
            notificationLabel.setText("Warning: Values in the fields True value and False value cannot be equal or both be null.");
        } else {
            notificationLabel.setVisible(false);
            notificationLabel.setText("");
        }
    }

    @Override
    public void stopEditing() {
        //ignore
    }

    private EValType getValueType() {
        EValType valType;
        if (editMaskType == EEditMaskType.NUM) {
            valType = EValType.NUM;
        } else if (editMaskType == EEditMaskType.INT) {
            valType = EValType.INT;
        } else if (editMaskType == EEditMaskType.BOOL) {
            valType = EValType.BOOL;
        } else if (editMaskType == EEditMaskType.STR) {
            valType = EValType.STR;
        } else {
            throw new UnsupportedOperationException("Unsupported input mask type " + editMaskType.getName());
        }
        return valType;
    }

    private ECompatibleTypesForBool getEditMaskType() {

        if (editMaskType == EEditMaskType.NUM) {
            return ECompatibleTypesForBool.REALNUM;
        } else if (editMaskType == EEditMaskType.INT) {
            return ECompatibleTypesForBool.INT;
        } else if (editMaskType == EEditMaskType.BOOL) {
            return ECompatibleTypesForBool.DEFAULT;
        } else if (editMaskType == EEditMaskType.STR) {
            return ECompatibleTypesForBool.STR;
        } else {
            throw new UnsupportedOperationException("Unsupported input mask type " + editMaskType.getName());
        }
    }

    private void setupInitialValues(EditMaskBool boolEditMask) {
        boolean titleVis = boolEditMask.isValueTitleVisible();
        this.titleVisible = titleVis;
        visCheckBox.setSelected(titleVis);
        this.trueTitle = boolEditMask.getTrueTitle();
        this.falseTitle = boolEditMask.getFalseTitle();
        this.trueTitleId = boolEditMask.getTrueTitleId();
        this.falseTitleId = boolEditMask.getFalseTitleId();
        AdsMultilingualStringDef string = findTitle(boolEditMask, trueTitleId);
        if (string != null) {
            trueTitleDef = AdsMultilingualStringDef.Factory.newInstance(string);
        } else {
            trueTitleDef = AdsMultilingualStringDef.Factory.newInstance();
        }
        string = findTitle(boolEditMask, falseTitleId);
        if (string != null) {
            falseTitleDef = AdsMultilingualStringDef.Factory.newInstance(string);
        } else {
            falseTitleDef = AdsMultilingualStringDef.Factory.newInstance();
        }
        this.type = boolEditMask.getCompatibleType();
        EValType valType = getValueType();
        trueValueEditor.setValue(valType, boolEditMask.getTrueValue() == null ? null : ValAsStr.Factory.newInstance(boolEditMask.getTrueValue(), valType));
        falseValueEditor.setValue(valType, boolEditMask.getFalseValue() == null ? null : ValAsStr.Factory.newInstance(boolEditMask.getFalseValue(), valType));
        if (trueTitleId != null) {
            trueValueTitle.setValue(getDisplayString(trueTitleDef));
        } else {
            trueValueTitle.setValue(trueTitle);
        }
        if (falseTitleId != null) {
            falseValueTitle.setValue(getDisplayString(falseTitleDef));
        } else {
            falseValueTitle.setValue(falseTitle);
        }

    }
}
