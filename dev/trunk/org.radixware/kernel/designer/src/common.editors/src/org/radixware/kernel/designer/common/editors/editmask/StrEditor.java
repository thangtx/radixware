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
 * StrEditor.java
 *
 * Created on Jul 27, 2009, 4:41:33 PM
 */
package org.radixware.kernel.designer.common.editors.editmask;

import java.awt.CardLayout;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.LinkedList;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMask;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskStr;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskStr.ValidatorType;
import org.radixware.kernel.designer.common.dialogs.components.CheckedNumberSpinnerEditor;


class StrEditor extends Editor {

    private boolean readOnly = false;
    private Long maxDbLen = null;
    private boolean isDbAllowEmptyString;

    private class CbModel implements ComboBoxModel {

        private class Item {

            final EditMaskStr.ValidatorType validatorType;

            public Item(ValidatorType validatorType) {
                this.validatorType = validatorType;
            }

            @Override
            public String toString() {
                switch (validatorType) {
                    case INT:
                        return "Integer";
                    case NUM:
                        return "Number";
                    case REGEX:
                        return "Regular expression";
                    default:
                        return "Default";
                }
            }
        }
        final List<ListDataListener> listeners = new LinkedList<ListDataListener>();
        Item selectedItem = null;
        Item[] items = new Item[]{
            new Item(EditMaskStr.ValidatorType.SIMPLE),
            new Item(EditMaskStr.ValidatorType.INT),
            new Item(EditMaskStr.ValidatorType.NUM),
            new Item(EditMaskStr.ValidatorType.REGEX)};

        @Override
        public void setSelectedItem(Object anItem) {
            selectedItem = (Item) anItem;
            fireChange();
            onValidatorTypeChange();
        }

        private ValidatorType getValidatorType() {
            return selectedItem.validatorType;
        }

        private void setValidatorType(ValidatorType vt) {
            for (Item item : items) {
                if (item.validatorType == vt) {
                    setSelectedItem(item);
                    return;
                }
            }
        }

        @Override
        public Object getSelectedItem() {
            return selectedItem;
        }

        @Override
        public int getSize() {
            return items.length;
        }

        @Override
        public Object getElementAt(int index) {
            return items[index];
        }

        private void fireChange() {
            for (ListDataListener listener : listeners) {
                listener.contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, items.length));
            }
        }

        @Override
        public void addListDataListener(ListDataListener l) {
            listeners.add(l);
        }

        @Override
        public void removeListDataListener(ListDataListener l) {
            listeners.remove(l);
        }
    }
    private final CbModel model = new CbModel();

   

    /** Creates new form StrEditor */
    public StrEditor(EditMaskStr editMaskStr) {
        initComponents();

        maxLengthSpinner.setEditor(new CheckedNumberSpinnerEditor(maxLengthSpinner));
        if (editMaskStr.getDbMaxLen() != null) {
            maxDbLen = Long.valueOf(editMaskStr.getDbMaxLen().intValue());
        } else {
            maxDbLen = null;
        }
        isDbAllowEmptyString = editMaskStr.isDbAllowEmptyString();

        edIntMin.setModel(new SpinnerNumberModel(new Long(0), new Long(Long.MIN_VALUE), new Long(Long.MAX_VALUE), new Long(1)));
        edIntMax.setModel(new SpinnerNumberModel(new Long(0), new Long(Long.MIN_VALUE), new Long(Long.MAX_VALUE), new Long(1)));
        edNumMin.setModel(new SpinnerNumberModel(new Double(0), new Double(-Double.MAX_VALUE), new Double(Double.MAX_VALUE), new Double(1)));
        edNumMax.setModel(new SpinnerNumberModel(new Double(0), new Double(-Double.MAX_VALUE), new Double(Double.MAX_VALUE), new Double(1)));

        edIntMin.setValue(Long.MIN_VALUE);
        edIntMax.setValue(Long.MAX_VALUE);
        edNumMin.setValue(new Double(-Double.MAX_VALUE));
        edNumMax.setValue(new Double(Double.MAX_VALUE));
        edPrecision.setValue(0);


        JSpinner.NumberEditor editor = (JSpinner.NumberEditor) edNumMin.getEditor();
        editor.getFormat().setMaximumFractionDigits(50);
        editor = (JSpinner.NumberEditor) edNumMax.getEditor();
        editor.getFormat().setMaximumFractionDigits(50);


        model.setValidatorType(editMaskStr.getValidator().getType());
        cbValidatorType.setModel(model);
        onValidatorTypeChange();
        if (editMaskStr != null) {
            setupInitialValues(editMaskStr);
        }
        updateEnableState();


        chIntMax.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                edIntMax.setEnabled(!readOnly && chIntMax.isSelected());
            }
        });
        chIntMin.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                edIntMin.setEnabled(!readOnly && chIntMin.isSelected());
            }
        });
        chNumMax.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                edNumMax.setEnabled(!readOnly && chNumMax.isSelected());
            }
        });
        chNumMin.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                edNumMin.setEnabled(!readOnly && chNumMin.isSelected());
            }
        });
        chPrecision.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                edPrecision.setEnabled(!readOnly && chPrecision.isSelected());
            }
        });

    }

    @Override
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        updateEnableState();
    }

    private void onValidatorTypeChange() {
        CardLayout l = (CardLayout) pValidatorMode.getLayout();
        switch (model.getValidatorType()) {
            case SIMPLE:
                l.show(pValidatorMode, "card2");
                break;
            case INT:
                l.show(pValidatorMode, "card3");
                break;
            case NUM:
                l.show(pValidatorMode, "card4");
                break;
            case REGEX:
                l.show(pValidatorMode, "card5");
                break;
        }
        updateEnableState();
    }

    private void setupInitialValues(EditMaskStr editMaskStr) {
        switch (editMaskStr.getValidator().getType()) {
            case SIMPLE:
                EditMaskStr.DefaultValidatorDef dv = (EditMaskStr.DefaultValidatorDef) editMaskStr.getValidator();
                maskTextField.setText(dv.getMask());
                useBlankCharacterCheckBox.setSelected(dv.isNoBlanckChar());
                keepSeparatorsCheckBox.setSelected(dv.isKeepSeparators());
                break;
            case INT:
                EditMaskStr.IntValidatorDef iv = (EditMaskStr.IntValidatorDef) editMaskStr.getValidator();
                if (iv.getMinValue() != null) {
                    chIntMin.setSelected(true);
                    edIntMin.setValue(iv.getMinValue());
                } else {
                    chIntMin.setSelected(false);
                    edIntMin.setValue(Long.MIN_VALUE);
                }
                if (iv.getMaxValue() != null) {
                    chIntMax.setSelected(true);
                    edIntMax.setValue(iv.getMaxValue());
                } else {
                    chIntMax.setSelected(false);
                    edIntMax.setValue(Long.MAX_VALUE);
                }
                break;
            case NUM:
                EditMaskStr.NumValidatorDef nv = (EditMaskStr.NumValidatorDef) editMaskStr.getValidator();
                if (nv.getMinValue() != null) {
                    chNumMin.setSelected(true);
                    edNumMin.setValue(nv.getMinValue().doubleValue());
                } else {
                    chNumMin.setSelected(false);
                    edNumMin.setValue(new Double(-Double.MAX_VALUE));
                }
                if (nv.getMaxValue() != null) {
                    chNumMax.setSelected(true);
                    edNumMax.setValue(nv.getMaxValue().doubleValue());
                } else {
                    chNumMax.setSelected(false);
                    edNumMax.setValue(new Double(Double.MAX_VALUE));
                }
                if (nv.getPrecision() > 0) {
                    chPrecision.setSelected(true);
                    edPrecision.setValue(nv.getPrecision());
                } else {
                    chPrecision.setSelected(false);
                    edPrecision.setValue(0);
                }
                break;
            case REGEX:
                EditMaskStr.RegexValidatorDef rv = (EditMaskStr.RegexValidatorDef) editMaskStr.getValidator();
                edPattern.setText(rv.getRegex());
                chMatchCase.setSelected(rv.isMatchCase());
                break;
        }

        passwordCheckBox.setSelected(editMaskStr.isPassword());
        chAllowEmptyString.setSelected(editMaskStr.isAllowEmptyString());

        if (editMaskStr.getMaxLen() != null) {
            maxLengthCheckBox.setSelected(true);
            maxLengthSpinner.setValue(Long.valueOf(editMaskStr.getMaxLen().intValue()));
        } else {
            maxLengthCheckBox.setSelected(false);
        }
    }

    private void updateEnableState() {
        qtMaskLabel.setEnabled(!readOnly);
        maskTextField.setEnabled(!readOnly);
        chAllowEmptyString.setEnabled(!readOnly);
        useBlankCharacterCheckBox.setEnabled(!readOnly);
        passwordCheckBox.setEnabled(!readOnly);
        keepSeparatorsCheckBox.setEnabled(!readOnly);
        maxLengthCheckBox.setEnabled(!readOnly);
        maxLengthSpinner.setEnabled(!readOnly && maxLengthCheckBox.isSelected());
        btResetToDb.setEnabled(!readOnly && maxDbLen != null);

        chIntMin.setEnabled(!readOnly);
        chIntMax.setEnabled(!readOnly);
        chNumMin.setEnabled(!readOnly);
        chNumMax.setEnabled(!readOnly);
        chPrecision.setEnabled(!readOnly);

        edNumMin.setEnabled(!readOnly && chNumMin.isSelected());
        edNumMax.setEnabled(!readOnly && chNumMax.isSelected());
        edIntMin.setEnabled(!readOnly && chIntMin.isSelected());
        edIntMax.setEnabled(!readOnly && chIntMax.isSelected());
        edPrecision.setEnabled(!readOnly && chPrecision.isSelected());
        edPattern.setEnabled(!readOnly);
        chMatchCase.setEnabled(!readOnly);
        cbValidatorType.setEnabled(!readOnly);
        jLabel1.setEnabled(!readOnly);
        jLabel2.setEnabled(!readOnly);

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        passwordCheckBox = new javax.swing.JCheckBox();
        maxLengthSpinner = new javax.swing.JSpinner();
        maxLengthCheckBox = new javax.swing.JCheckBox();
        btResetToDb = new javax.swing.JButton();
        chAllowEmptyString = new javax.swing.JCheckBox();
        pValidatorMode = new javax.swing.JPanel();
        pDefault = new javax.swing.JPanel();
        maskTextField = new javax.swing.JTextField();
        qtMaskLabel = new javax.swing.JLabel();
        useBlankCharacterCheckBox = new javax.swing.JCheckBox();
        keepSeparatorsCheckBox = new javax.swing.JCheckBox();
        pInt = new javax.swing.JPanel();
        edIntMin = new javax.swing.JSpinner();
        edIntMax = new javax.swing.JSpinner();
        chIntMin = new javax.swing.JCheckBox();
        chIntMax = new javax.swing.JCheckBox();
        pNum = new javax.swing.JPanel();
        edNumMin = new javax.swing.JSpinner();
        chNumMax = new javax.swing.JCheckBox();
        chNumMin = new javax.swing.JCheckBox();
        edNumMax = new javax.swing.JSpinner();
        chPrecision = new javax.swing.JCheckBox();
        edPrecision = new javax.swing.JSpinner();
        pRegex = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        edPattern = new javax.swing.JTextField();
        chMatchCase = new javax.swing.JCheckBox();
        cbValidatorType = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();

        passwordCheckBox.setText(org.openide.util.NbBundle.getMessage(StrEditor.class, "StrEditor.passwordCheckBox.text")); // NOI18N

        maxLengthSpinner.setModel(new javax.swing.SpinnerNumberModel(Long.valueOf(1L), Long.valueOf(0L), null, Long.valueOf(1L)));

        maxLengthCheckBox.setText(org.openide.util.NbBundle.getMessage(StrEditor.class, "StrEditor.maxLengthCheckBox.text")); // NOI18N
        maxLengthCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                maxLengthCheckBoxItemStateChanged(evt);
            }
        });

        btResetToDb.setText(org.openide.util.NbBundle.getMessage(StrEditor.class, "StrEditor.btResetToDb.text")); // NOI18N
        btResetToDb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btResetToDbresetToDb(evt);
            }
        });

        chAllowEmptyString.setText(org.openide.util.NbBundle.getMessage(StrEditor.class, "StrEditor.chAllowEmptyString.text")); // NOI18N

        pValidatorMode.setLayout(new java.awt.CardLayout());

        maskTextField.setText(org.openide.util.NbBundle.getMessage(StrEditor.class, "StrEditor.maskTextField.text")); // NOI18N

        qtMaskLabel.setText(org.openide.util.NbBundle.getMessage(StrEditor.class, "StrEditor.qtMaskLabel.text")); // NOI18N

        useBlankCharacterCheckBox.setText(org.openide.util.NbBundle.getMessage(StrEditor.class, "StrEditor.useBlankCharacterCheckBox.text")); // NOI18N

        keepSeparatorsCheckBox.setText(org.openide.util.NbBundle.getMessage(StrEditor.class, "StrEditor.keepSeparatorsCheckBox.text")); // NOI18N

        javax.swing.GroupLayout pDefaultLayout = new javax.swing.GroupLayout(pDefault);
        pDefault.setLayout(pDefaultLayout);
        pDefaultLayout.setHorizontalGroup(
            pDefaultLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pDefaultLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(qtMaskLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pDefaultLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(keepSeparatorsCheckBox)
                    .addComponent(useBlankCharacterCheckBox)
                    .addComponent(maskTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 392, Short.MAX_VALUE))
                .addContainerGap())
        );
        pDefaultLayout.setVerticalGroup(
            pDefaultLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pDefaultLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pDefaultLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(qtMaskLabel)
                    .addComponent(maskTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(useBlankCharacterCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(keepSeparatorsCheckBox)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pValidatorMode.add(pDefault, "card2");

        chIntMin.setText(org.openide.util.NbBundle.getMessage(StrEditor.class, "StrEditor.chIntMin.text")); // NOI18N

        chIntMax.setText(org.openide.util.NbBundle.getMessage(StrEditor.class, "StrEditor.chIntMax.text")); // NOI18N

        javax.swing.GroupLayout pIntLayout = new javax.swing.GroupLayout(pInt);
        pInt.setLayout(pIntLayout);
        pIntLayout.setHorizontalGroup(
            pIntLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pIntLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pIntLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chIntMin)
                    .addComponent(chIntMax))
                .addGap(18, 18, 18)
                .addGroup(pIntLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(edIntMin, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(edIntMax, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(244, Short.MAX_VALUE))
        );
        pIntLayout.setVerticalGroup(
            pIntLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pIntLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pIntLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chIntMin)
                    .addComponent(edIntMin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pIntLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chIntMax)
                    .addComponent(edIntMax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pValidatorMode.add(pInt, "card3");

        chNumMax.setText(org.openide.util.NbBundle.getMessage(StrEditor.class, "StrEditor.chNumMax.text")); // NOI18N

        chNumMin.setText(org.openide.util.NbBundle.getMessage(StrEditor.class, "StrEditor.chNumMin.text")); // NOI18N

        chPrecision.setText(org.openide.util.NbBundle.getMessage(StrEditor.class, "StrEditor.chPrecision.text")); // NOI18N

        javax.swing.GroupLayout pNumLayout = new javax.swing.GroupLayout(pNum);
        pNum.setLayout(pNumLayout);
        pNumLayout.setHorizontalGroup(
            pNumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pNumLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pNumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chNumMin)
                    .addComponent(chNumMax))
                .addGap(18, 18, 18)
                .addGroup(pNumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pNumLayout.createSequentialGroup()
                        .addComponent(edNumMin, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(chPrecision)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(edPrecision, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE))
                    .addComponent(edNumMax, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        pNumLayout.setVerticalGroup(
            pNumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
            .addGroup(pNumLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pNumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chNumMin)
                    .addComponent(edNumMin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chPrecision)
                    .addComponent(edPrecision, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pNumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chNumMax)
                    .addComponent(edNumMax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pValidatorMode.add(pNum, "card4");

        pRegex.setBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.shadow")));

        jLabel2.setText(org.openide.util.NbBundle.getMessage(StrEditor.class, "StrEditor.jLabel2.text")); // NOI18N

        edPattern.setText(org.openide.util.NbBundle.getMessage(StrEditor.class, "StrEditor.edPattern.text")); // NOI18N

        chMatchCase.setText(org.openide.util.NbBundle.getMessage(StrEditor.class, "StrEditor.chMatchCase.text")); // NOI18N

        javax.swing.GroupLayout pRegexLayout = new javax.swing.GroupLayout(pRegex);
        pRegex.setLayout(pRegexLayout);
        pRegexLayout.setHorizontalGroup(
            pRegexLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pRegexLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pRegexLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chMatchCase)
                    .addComponent(edPattern, javax.swing.GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE))
                .addContainerGap())
        );
        pRegexLayout.setVerticalGroup(
            pRegexLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pRegexLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pRegexLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(edPattern, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chMatchCase)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        pValidatorMode.add(pRegex, "card5");

        cbValidatorType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel1.setText(org.openide.util.NbBundle.getMessage(StrEditor.class, "StrEditor.jLabel1.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(pValidatorMode, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE))
                    .addComponent(btResetToDb, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbValidatorType, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(passwordCheckBox))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(maxLengthCheckBox))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(maxLengthSpinner, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(chAllowEmptyString)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(passwordCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(maxLengthCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(maxLengthSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chAllowEmptyString)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cbValidatorType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pValidatorMode, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btResetToDb)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void maxLengthCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_maxLengthCheckBoxItemStateChanged
        updateEnableState();
    }//GEN-LAST:event_maxLengthCheckBoxItemStateChanged

    private void btResetToDbresetToDb(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btResetToDbresetToDb
        if (this.maxDbLen != null) {
            maxLengthSpinner.setValue(maxDbLen);
            this.maxLengthCheckBox.setSelected(true);
        }
        chAllowEmptyString.setSelected(isDbAllowEmptyString);

}//GEN-LAST:event_btResetToDbresetToDb

    private BigDecimal getBigDecimalValue(Object obj) {
        if (obj instanceof Integer) {
            return new BigDecimal((Integer) obj);
        } else if (obj instanceof Long) {
            return new BigDecimal((Long) obj);
        } else if (obj instanceof Float) {
            return new BigDecimal(obj.toString());
        } else if (obj instanceof Double) {
            return new BigDecimal(obj.toString());
        } else if (obj instanceof String) {
            try {
                return new BigDecimal((String) obj);
            } catch (NumberFormatException e) {
                return BigDecimal.ZERO;
            }
        } else {
            return new BigDecimal(0);
        }
    }

    private Long getLongValue(Object obj) {
        if (obj instanceof Integer) {
            return new Long((Integer) obj);
        } else if (obj instanceof Long) {
            return (Long) obj;
        } else if (obj instanceof String) {
            try {
                return new Long((String) obj);
            } catch (NumberFormatException e) {
                return new Long(0);
            }
        } else {

            return new Long(0);
        }
    }

    @Override
    public void apply(EditMask editMask) {
        if (!(editMask instanceof EditMaskStr)) {
            return;
        }
        EditMaskStr editMaskStr = (EditMaskStr) editMask;
        editMaskStr.setValidatorType(model.getValidatorType());
        EditMaskStr.ValidatorDef validator = editMaskStr.getValidator();
        switch (validator.getType()) {
            case SIMPLE:
                EditMaskStr.DefaultValidatorDef dv = (EditMaskStr.DefaultValidatorDef) validator;
                dv.setMask(maskTextField.getText());
                dv.setNoBlanckChar(useBlankCharacterCheckBox.isSelected());
                dv.setKeepSeparators(keepSeparatorsCheckBox.isSelected());
                break;
            case INT:
                EditMaskStr.IntValidatorDef iv = (EditMaskStr.IntValidatorDef) validator;
                if (chIntMin.isSelected()) {
                    iv.setMinValue(getLongValue(edIntMin.getValue()));
                } else {
                    iv.setMinValue(null);
                }
                if (chIntMax.isSelected()) {
                    iv.setMaxValue(getLongValue(edIntMax.getValue()));
                } else {
                    iv.setMaxValue(null);
                }
                break;
            case NUM:
                EditMaskStr.NumValidatorDef nv = (EditMaskStr.NumValidatorDef) validator;
                if (chNumMin.isSelected()) {
                    nv.setMinValue(getBigDecimalValue(edNumMin.getValue()));
                } else {
                    nv.setMinValue(null);
                }
                if (chNumMax.isSelected()) {
                    nv.setMaxValue(getBigDecimalValue(edNumMax.getValue()));
                } else {
                    nv.setMaxValue(null);
                }
                if (chPrecision.isSelected()) {
                    nv.setPrecision((Integer) edPrecision.getValue());
                } else {
                    nv.setPrecision(-1);
                }
                break;
            case REGEX:
                EditMaskStr.RegexValidatorDef rv = (EditMaskStr.RegexValidatorDef) validator;
                rv.setRegex(edPattern.getText());
                rv.setMatchCase(chMatchCase.isSelected());
                break;

        }
        editMaskStr.setIsPassword(passwordCheckBox.isSelected());

        editMaskStr.setAllowEmptyString(chAllowEmptyString.isSelected());
        if (maxLengthCheckBox.isSelected()) {
            editMaskStr.setMaxLen(((Long) maxLengthSpinner.getValue()).intValue());
        } else {
            editMaskStr.setMaxLen(null);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btResetToDb;
    private javax.swing.JComboBox cbValidatorType;
    private javax.swing.JCheckBox chAllowEmptyString;
    private javax.swing.JCheckBox chIntMax;
    private javax.swing.JCheckBox chIntMin;
    private javax.swing.JCheckBox chMatchCase;
    private javax.swing.JCheckBox chNumMax;
    private javax.swing.JCheckBox chNumMin;
    private javax.swing.JCheckBox chPrecision;
    private javax.swing.JSpinner edIntMax;
    private javax.swing.JSpinner edIntMin;
    private javax.swing.JSpinner edNumMax;
    private javax.swing.JSpinner edNumMin;
    private javax.swing.JTextField edPattern;
    private javax.swing.JSpinner edPrecision;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JCheckBox keepSeparatorsCheckBox;
    private javax.swing.JTextField maskTextField;
    private javax.swing.JCheckBox maxLengthCheckBox;
    private javax.swing.JSpinner maxLengthSpinner;
    private javax.swing.JPanel pDefault;
    private javax.swing.JPanel pInt;
    private javax.swing.JPanel pNum;
    private javax.swing.JPanel pRegex;
    private javax.swing.JPanel pValidatorMode;
    private javax.swing.JCheckBox passwordCheckBox;
    private javax.swing.JLabel qtMaskLabel;
    private javax.swing.JCheckBox useBlankCharacterCheckBox;
    // End of variables declaration//GEN-END:variables
}
