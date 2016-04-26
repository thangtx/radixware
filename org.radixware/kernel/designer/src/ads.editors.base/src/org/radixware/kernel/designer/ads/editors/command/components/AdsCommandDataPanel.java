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

package org.radixware.kernel.designer.ads.editors.command.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.clazz.form.AdsFormHandlerClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsScopeCommandDef;
import org.radixware.kernel.common.defs.ads.command.AdsCommandDef;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.IAdsTypeSource;
import org.radixware.kernel.common.enums.ECommandScope;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinition;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.choosetype.ChooseType;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.common.dialogs.choosetype.ETypeNature;


public class AdsCommandDataPanel extends javax.swing.JPanel {

    public static final String NONE_ACTION = "NoValue";
    public static final String XML_ACTION = "Xml";
    public static final String FORM_ACTION = "Form";
    private JButton xmlBtn;
    private JButton formBtn;
    private ButtonGroup group;

    /**
     * Creates new form AdsCommandDataPanel
     */
    public AdsCommandDataPanel() {
        initComponents();
        xmlBtn = xmlField.addButton();
        xmlBtn.setIcon(RadixWareIcons.DIALOG.CHOOSE.getIcon(13, 13));
        xmlBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                AdsTypeDeclaration newtype = ChooseType.getInstance().editType(ETypeNature.RADIX_XML, new ChooseType.DefaultTypeFilter(command, null));
                if (newtype != null) {
                    valueType = newtype;
                    AdsCommandDataPanel.this.applyTypeToCommand();
                    xmlField.setValue(valueType.getName(command));
                }
            }
        });

        formBtn = formField.addButton();
        formBtn.setIcon(RadixWareIcons.DIALOG.CHOOSE.getIcon(13, 13));
        formBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(command, new FormDefVisitorProvider());
                Definition def = ChooseDefinition.chooseDefinition(cfg);
                if (def != null
                        && def instanceof AdsFormHandlerClassDef) {
                    AdsFormHandlerClassDef formhandler = (AdsFormHandlerClassDef) def;
                    valueType = AdsTypeDeclaration.Factory.newInstance(formhandler);
                    AdsCommandDataPanel.this.applyTypeToCommand();
                    formField.setValue(valueType.getName(command));
                }
            }
        });

        group = new ButtonGroup();
        noneCheck.setActionCommand(NONE_ACTION);
        xmlCheck.setActionCommand(XML_ACTION);
        formCheck.setActionCommand(FORM_ACTION);
        group.add(noneCheck);
        group.add(xmlCheck);
        group.add(formCheck);
    }

    private void setupCheckListeners() {
        noneCheck.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (!isUpdate) {
                    if (e.getStateChange() == ItemEvent.SELECTED
                            && !valueType.equals(AdsTypeDeclaration.Factory.voidType())) {
                        valueType = AdsTypeDeclaration.Factory.voidType();
                        AdsCommandDataPanel.this.applyTypeToCommand();
                        xmlBtn.setEnabled(false);
                        formBtn.setEnabled(false);
                        xmlField.setValue("");
                        formField.setValue("");
                        changeSupport.fireChange();
                    }
                }
            }
        });
        xmlCheck.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (!isUpdate) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        AdsTypeDeclaration xmltype = ChooseType.getInstance().editType(ETypeNature.RADIX_XML, new ChooseType.DefaultTypeFilter(command, null));
                        if (xmltype != null) {
                            valueType = xmltype;
                            AdsCommandDataPanel.this.applyTypeToCommand();
                            xmlBtn.setEnabled(true);
                            xmlField.setValue(valueType.getName(command));
                            changeSupport.fireChange();
                        } else {
                            AdsCommandDataPanel.this.update();
                            if (valueType.isVoid()) {
                                noneCheck.requestFocusInWindow();
                            } else {
                                formCheck.requestFocusInWindow();
                            }
                        }
                    } else {
                        xmlBtn.setEnabled(false);
                        xmlField.setValue("");
                    }
                }
            }
        });
        formCheck.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (!isUpdate) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        if (AdsCommandDataPanel.this.editorType == IN_EDITOR) {
                            ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(command, new FormDefVisitorProvider());
                            Definition def = ChooseDefinition.chooseDefinition(cfg);
                            if (def != null) {
                                AdsCommandDataPanel.this.valueType = AdsTypeDeclaration.Factory.newInstance((IAdsTypeSource) def);
                                AdsCommandDataPanel.this.applyTypeToCommand();
                                formField.setValue(valueType.getName(command));
                                formBtn.setEnabled(formCheck.isEnabled() ? true : false);
                                changeSupport.fireChange();
                            } else {
                                AdsCommandDataPanel.this.update();
                                if (valueType.isVoid()) {
                                    noneCheck.requestFocusInWindow();
                                } else {
                                    xmlCheck.requestFocusInWindow();
                                }
                            }
                        } else {
                            AdsCommandDataPanel.this.valueType = AdsTypeDeclaration.Factory.newPlatformClass(AdsFormHandlerClassDef.PLATFORM_NEXT_REQUEST_CLASS_NAME);
                            AdsCommandDataPanel.this.applyTypeToCommand();
                            changeSupport.fireChange();
                        }
                    } else {
                        formBtn.setEnabled(false);
                        formField.setValue("");
                    }
                }
            }
        });
    }

    private void applyTypeToCommand() {
        if (editorType == IN_EDITOR) {
            command.getData().setInType(valueType);
        } else {
            command.getData().setOutType(valueType);
        }
    }

    public String getCurrentDataType() {
        return group.getSelection().getActionCommand();
    }

    public void selectDataType(String actioncommand) {
        if (actioncommand.equals(FORM_ACTION)) {
            formCheck.setSelected(!isReadOnly());
        } else if (actioncommand.equals(XML_ACTION)) {
            xmlCheck.setSelected(!isReadOnly());
        } else if (actioncommand.equals(NONE_ACTION)) {
            noneCheck.setSelected(!isReadOnly());
        }
    }

    public void setChecksReadonly(boolean readonly) {
        boolean cmdReadonly = command != null ? isReadOnly() || command.getData().isLocal() : isReadOnly();
        xmlCheck.setEnabled(!readonly && !cmdReadonly);
        noneCheck.setEnabled(!readonly && !cmdReadonly);
        formCheck.setEnabled(!readonly && !cmdReadonly);
    }
    //*******************************************
    public static final int IN_EDITOR = 0;
    public static final int OUT_EDITOR = 1;
    private AdsCommandDef command;
    private AdsTypeDeclaration valueType;
    private int editorType = IN_EDITOR;
    private boolean isUpdate = false;

    public void open(AdsCommandDef command, int type) {
        this.command = command;
        this.editorType = type;
        update();
        setupCheckListeners();
    }

    public void update() {
        isUpdate = true;
        boolean isReadOnly = isReadOnly();
        if (command.getData().isLocal()) {
            this.setReadonly(true);
            isReadOnly = true;
            formField.setValue("");
            xmlField.setValue("");
        } else {
            if (command instanceof AdsScopeCommandDef) {
                if (((AdsScopeCommandDef) command).getScope() == ECommandScope.RPC) {
                    this.setReadonly(true);
                    isReadOnly = true;
                } else {
                    this.setReadonly(false);
                }
            } else {
                this.setReadonly(false);
            }
        }
        if (editorType == IN_EDITOR) {
            AdsTypeDeclaration intype = command.getData().getInType();
            this.valueType = intype;
            formField.setVisible(!isReadOnly);
            updateTypeState();
        } else {
            formBtn.setEnabled(false);
            //formCheck.setEnabled(false);
            AdsTypeDeclaration outtype = command.getData().getOutType();
            this.valueType = outtype;
            formField.setVisible(false);
            updateTypeState();
        }
        if (noneCheck.isSelected()) {
            xmlBtn.setEnabled(false);
            formBtn.setEnabled(false);
        } else if (xmlCheck.isSelected()) {
            xmlBtn.setEnabled(!isReadOnly);
            formBtn.setEnabled(false);
        } else if (formCheck.isSelected()) {
            xmlBtn.setEnabled(false);
            if (editorType != OUT_EDITOR) {
                formBtn.setEnabled(!isReadOnly);
            }
        }
        isUpdate = false;
    }

    private boolean isReadOnly() {
        if (command.getData().isReadOnly()) {
            return true;
        } else {
            if (command instanceof AdsScopeCommandDef) {
                if (((AdsScopeCommandDef) command).getScope() == ECommandScope.RPC) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    private void updateTypeState() {
        if (valueType.isVoid()) {
            noneCheck.setSelected(true);
        } else {
            if (valueType.isBasedOn(EValType.XML)) {
                xmlCheck.setSelected(true);
                xmlField.setValue(valueType.getName(command));
            } else {
                formCheck.setSelected(true);
                formField.setValue(valueType.getName(command));
            }
        }
    }

    public void setReadonly(boolean readonly) {
        noneCheck.setEnabled(!readonly);
        xmlCheck.setEnabled(!readonly);
        formCheck.setEnabled(!readonly);

        xmlBtn.setEnabled(!readonly);
        formBtn.setEnabled(!readonly);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        xmlField = new org.radixware.kernel.common.components.ExtendableTextField(true);
        formField = new org.radixware.kernel.common.components.ExtendableTextField(true);
        noneCheck = new javax.swing.JRadioButton();
        xmlCheck = new javax.swing.JRadioButton();
        formCheck = new javax.swing.JRadioButton();

        noneCheck.setText(org.openide.util.NbBundle.getMessage(AdsCommandDataPanel.class, "AdsCommand-NoValueTip")); // NOI18N

        xmlCheck.setText(org.openide.util.NbBundle.getMessage(AdsCommandDataPanel.class, "AdsCommand-XmlDataTip")); // NOI18N

        formCheck.setText(org.openide.util.NbBundle.getMessage(AdsCommandDataPanel.class, "AdsCommand-FormDataTip")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(noneCheck)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(xmlCheck)
                    .addComponent(formCheck))
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(formField, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                    .addComponent(xmlField, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {formCheck, noneCheck, xmlCheck});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(noneCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(xmlField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(xmlCheck))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(formCheck)
                    .addComponent(formField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton formCheck;
    private org.radixware.kernel.common.components.ExtendableTextField formField;
    private javax.swing.JRadioButton noneCheck;
    private javax.swing.JRadioButton xmlCheck;
    private org.radixware.kernel.common.components.ExtendableTextField xmlField;
    // End of variables declaration//GEN-END:variables
    private ChangeSupport changeSupport = new ChangeSupport(this);

    public final void addChangeListener(ChangeListener l) {
        changeSupport.addChangeListener(l);
    }

    public final void removeChangeListener(ChangeListener l) {
        changeSupport.removeChangeListener(l);
    }

    private class FormDefVisitorProvider extends VisitorProvider {

        @Override
        public boolean isTarget(RadixObject obj) {
            return obj instanceof AdsFormHandlerClassDef;
        }

        @Override
        public boolean isContainer(RadixObject object) {
            return true;
        }
    }
}
