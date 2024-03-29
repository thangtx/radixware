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

package org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.math.BigDecimal;
import java.util.ArrayList;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.*;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EPersoCommImportance;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.utils.RadixResourceBundle;


public class PersonalCommunicatorPanel extends EditorDialog.EditorPanel<AdsAppObject> {

    public PersonalCommunicatorPanel(AdsAppObject node) {
        super(node);
        initComponents();

        channel = obj.getPropByName("channelKind");
        importance = obj.getPropByName("importance");
        subject = obj.getPropByName("subject");
        body = obj.getPropByName("body");
        responseTimeout = obj.getPropByName("responseTimeout");

        comboChannel.setEnabled(!node.isReadOnly());
        comboImportance.setEnabled(!node.isReadOnly());
        textSubject.setEnabled(!node.isReadOnly());
        textBody.setEnabled(!node.isReadOnly());
        panelTimeout.setEnabled(!node.isReadOnly());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelChannel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        textBody = new javax.swing.JTextArea();
        textSubject = new javax.swing.JTextField();
        comboChannel = new javax.swing.JComboBox();
        labelSubject = new javax.swing.JLabel();
        labelBody = new javax.swing.JLabel();
        labelImportance = new javax.swing.JLabel();
        comboImportance = new javax.swing.JComboBox();
        panelTimeout = new org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.TimeoutPanel();

        setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.SystemColor.inactiveCaption));
        setMinimumSize(new java.awt.Dimension(500, 300));
        setPreferredSize(new java.awt.Dimension(500, 300));
        setRequestFocusEnabled(false);

        labelChannel.setText(RadixResourceBundle.getMessage(PersonalCommunicatorPanel.class, "PersonalCommunicatorPanel.labelChannel.text")); // NOI18N

        textBody.setColumns(20);
        textBody.setRows(5);
        jScrollPane1.setViewportView(textBody);

        comboChannel.setFocusable(false);
        comboChannel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboChannelActionPerformed(evt);
            }
        });

        labelSubject.setText(RadixResourceBundle.getMessage(PersonalCommunicatorPanel.class, "PersonalCommunicatorPanel.labelSubject.text")); // NOI18N

        labelBody.setText(RadixResourceBundle.getMessage(PersonalCommunicatorPanel.class, "PersonalCommunicatorPanel.labelBody.text")); // NOI18N

        labelImportance.setText(RadixResourceBundle.getMessage(PersonalCommunicatorPanel.class, "PersonalCommunicatorPanel.labelImportance.text")); // NOI18N

        comboImportance.setModel(new javax.swing.DefaultComboBoxModel(EPersoCommImportance.values()));
        comboImportance.setFocusable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(panelTimeout, javax.swing.GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(labelBody)
                            .addComponent(labelImportance)
                            .addComponent(labelChannel)
                            .addComponent(labelSubject))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 413, Short.MAX_VALUE)
                            .addComponent(comboChannel, 0, 413, Short.MAX_VALUE)
                            .addComponent(comboImportance, 0, 413, Short.MAX_VALUE)
                            .addComponent(textSubject, javax.swing.GroupLayout.DEFAULT_SIZE, 413, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboChannel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelChannel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboImportance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelImportance))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelSubject)
                    .addComponent(textSubject, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelBody)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelTimeout, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        labelChannel.getAccessibleContext().setAccessibleName("");
    }// </editor-fold>//GEN-END:initComponents

    private void comboChannelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboChannelActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_comboChannelActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox comboChannel;
    private javax.swing.JComboBox comboImportance;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelBody;
    private javax.swing.JLabel labelChannel;
    private javax.swing.JLabel labelImportance;
    private javax.swing.JLabel labelSubject;
    private org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.TimeoutPanel panelTimeout;
    private javax.swing.JTextArea textBody;
    private javax.swing.JTextField textSubject;
    // End of variables declaration//GEN-END:variables
    private AdsAppObject.Prop channel;
    private AdsAppObject.Prop importance;
    private AdsAppObject.Prop subject;
    private AdsAppObject.Prop body;
    private AdsAppObject.Prop responseTimeout;

    private class EnumItemWrapper {

        private AdsEnumItemDef item;

        public EnumItemWrapper(AdsEnumItemDef item) {
            this.item = item;
        }

        public EnumItemWrapper() {
            this.item = null;
        }

        @Override
        public String toString() {
            return getName();
        }

        public String getName() {
            return item != null ? item.getName() : RadixResourceBundle.getMessage(PersonalCommunicatorPanel.class, "Panel.empty.text");
        }

        public ValAsStr getValue() {
            return item != null ? item.getValue() : null;
        }
    }

    @Override
    public void init() {
        AdsEnumDef en = (AdsEnumDef) channel.getType().getPath().resolve(obj).get();
        List<EnumItemWrapper> wraps = new ArrayList<>();

        int selected = 0, idx = 0;
        wraps.add(new EnumItemWrapper());
        for (AdsEnumItemDef item : en.getItems().list(EScope.LOCAL)) {
            idx++;
            if (item.getValue().equals(channel.getValue())) {
                selected = idx;
            }
            wraps.add(new EnumItemWrapper(item));
        }

        comboChannel.setModel(new javax.swing.DefaultComboBoxModel(wraps.toArray()));
        comboChannel.setSelectedIndex(selected);

        comboImportance.setSelectedItem(EPersoCommImportance.getForValue(Long.valueOf(importance.getValue().toString())));
        textSubject.setText(subject.getValue() != null ? (String) subject.getValue().toObject(EValType.STR) : null);
        textBody.setText(body.getValue() != null ? (String) body.getValue().toObject(EValType.STR) : null);
        panelTimeout.setTimeout(responseTimeout.getValue() != null ? (BigDecimal) responseTimeout.getValue().toObject(EValType.NUM) : null);

        comboChannel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                channel.setValue(((EnumItemWrapper) comboChannel.getSelectedItem()).getValue());
            }
        });

        comboImportance.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                importance.setValue(String.valueOf(((EPersoCommImportance) comboImportance.getSelectedItem()).getValue()));
            }
        });

        textSubject.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                subject.setValue(ValAsStr.Factory.newInstance(textSubject.getText(), EValType.STR));
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                subject.setValue(ValAsStr.Factory.newInstance(textSubject.getText(), EValType.STR));
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                subject.setValue(ValAsStr.Factory.newInstance(textSubject.getText(), EValType.STR));
            }
        });

        textBody.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                body.setValue(ValAsStr.Factory.newInstance(textBody.getText(), EValType.STR));
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                body.setValue(ValAsStr.Factory.newInstance(textBody.getText(), EValType.STR));
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                body.setValue(ValAsStr.Factory.newInstance(textBody.getText(), EValType.STR));
            }
        });

        panelTimeout.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                responseTimeout.setValue(ValAsStr.Factory.newInstance(panelTimeout.getTimeout(), EValType.NUM));
            }
        });

    }

    @Override
    public void apply() {
    }

    @Override
    public String getTitle() {
        return RadixResourceBundle.getMessage(getClass(), "CTL_Properties");
    }

    @Override
    public RadixIcon getIcon() {
        return RadixWareIcons.EDIT.PROPERTIES;
    }
}
