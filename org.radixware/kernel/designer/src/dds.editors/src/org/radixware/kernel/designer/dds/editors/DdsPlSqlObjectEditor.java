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

package org.radixware.kernel.designer.dds.editors;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.dds.DdsDefinitions;
import org.radixware.kernel.common.defs.dds.DdsPlSqlObjectDef;
import org.radixware.kernel.common.defs.dds.DdsPlSqlPartDef;
import org.radixware.kernel.common.defs.dds.DdsTypeDef;
import org.radixware.kernel.common.defs.dds.DdsTypeFieldDef;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.designer.common.annotations.registrators.EditorFactoryRegistration;
import org.radixware.kernel.designer.common.dialogs.components.state.IStateDisplayer;
import org.radixware.kernel.designer.common.dialogs.components.state.StateDisplayer;
import org.radixware.kernel.designer.common.dialogs.utils.NameAcceptorFactory;
import org.radixware.kernel.designer.common.editors.RadixObjectEditor;
import org.radixware.kernel.designer.common.general.editors.IEditorFactory;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;
import org.radixware.kernel.designer.dds.script.DdsScriptGeneratorUtils;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.common.editors.sqml.SqmlEditorPanel;

/**
 * DdsPlSqlObjectDef editor.
 *
 */
public class DdsPlSqlObjectEditor extends RadixObjectEditor<DdsPlSqlObjectDef> {

    private final SqmlEditorPanel sqmlEditor;
//    private final ScmlEditorPane scmlEditor;
    private volatile boolean updating = false;
    private final DdsPlSqlPartDef plSqlPart;

    protected DdsPlSqlObjectEditor(DdsPlSqlPartDef plSqlPart) {
        this(plSqlPart.getPlSqlObjectDef(), plSqlPart);
    }

    protected DdsPlSqlObjectEditor(DdsPlSqlObjectDef ddsPlSqlObject) {
        this(ddsPlSqlObject, null);
    }

    protected DdsPlSqlObjectEditor(DdsPlSqlObjectDef ddsPlSqlObject, DdsPlSqlPartDef plSqlPart) {
        super(ddsPlSqlObject);
        this.plSqlPart = plSqlPart;
        initComponents();
        initStateDisplayer();

        sqmlEditor = new SqmlEditorPanel();
        sqmlPanel.add(sqmlEditor, BorderLayout.CENTER);

        fieldsExtTextField.setReadOnly(true);
        fieldsButton = fieldsExtTextField.addButton();
        fieldsButton.setIcon(RadixWareIcons.DIALOG.CHOOSE.getIcon(13, 13));
        fieldsButton.setToolTipText(NbBundle.getBundle(DdsPlSqlObjectEditor.class).getString("EDIT_FIELDS"));
        fieldsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DdsTypeFieldsEditor editor = new DdsTypeFieldsEditor();
                editor.editFields(((DdsTypeDef) getRadixObject()).getFields(), getPlSqlObject().isReadOnly());
                initFields(((DdsTypeDef) getRadixObject()).getFields());
            }
        });


        nameEditPanel.setNameAcceptor(NameAcceptorFactory.newAcceptorForRename(getRadixObject()));

        nameEditPanel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!updating && nameEditPanel.isComplete()) {
                    getPlSqlObject().setName(nameEditPanel.getCurrentName());
                }
            }
        });

        typeEditPanel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!updating) {
                    ((DdsTypeDef) getPlSqlObject()).setDbType(typeEditPanel.getCurrentName());
                }
            }
        });
        chIsDeprecated.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!updating) {
                    getPlSqlObject().setDeprecated(chIsDeprecated.isSelected());
                }
            }
        });

    }

    private void initStateDisplayer() {
        StateDisplayer stateDisplayer = new StateDisplayer();
        IStateDisplayer.Locator.register(stateDisplayer, this);
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(5, 10, 5, 10));
        panel.add(stateDisplayer, BorderLayout.CENTER);
        sqmlPanel.add(panel, BorderLayout.SOUTH);
    }

    private void updateEnableState() {
        nameEditPanel.setEditable(!getPlSqlObject().isReadOnly());
        purityLevelEditor.setReadOnly(getPlSqlObject().isReadOnly());
        typeEditPanel.setEditable(!getPlSqlObject().isReadOnly());
        chIsDeprecated.setEnabled(!getPlSqlObject().isReadOnly());
    }

    private void setupInitialValues() {
        updating = true;

        final DdsPlSqlObjectDef plSqlObject = getPlSqlObject();

        nameEditPanel.setCurrentName(plSqlObject.getName());
        descriptionEditor.open(plSqlObject);
        purityLevelEditor.setPurityLevel(plSqlObject.getPurityLevel());

        final Sqml sqml =
                (plSqlPart != null
                ? DdsScriptGeneratorUtils.calcPlSqlPartSqml(plSqlPart, null /*handler*/)
                : DdsScriptGeneratorUtils.calcPlSqlObjectSqml(plSqlObject, null /*handler*/));
        sqmlEditor.open(sqml);

        if (getPlSqlObject() instanceof DdsTypeDef) {
            DdsTypeDef type = (DdsTypeDef) plSqlObject;
            typeEditPanel.setCurrentName(type.getDbType());
            initFields(type.getFields());
            typeEditPanel.setVisible(true);
            fieldsExtTextField.setVisible(true);
            typeLabel.setVisible(true);
            fieldsLabel.setVisible(true);
        } else {
            typeEditPanel.setVisible(false);
            fieldsExtTextField.setVisible(false);
            typeLabel.setVisible(false);
            fieldsLabel.setVisible(false);
        }
        chIsDeprecated.setSelected(plSqlObject.isDeprecated());

        updateEnableState();

        updating = false;
    }

    private void initFields(DdsDefinitions<DdsTypeFieldDef> arr) {
        StringBuilder str = new StringBuilder();
        for (DdsTypeFieldDef field : arr) {
            str.append(field.getName() + " " + field.getDbType() + "; ");
        }
        if (str.length() > 2) {
            str.delete(str.length() - 2, str.length());
        }
        fieldsExtTextField.setValue(str.toString());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        nameEditPanel = new org.radixware.kernel.designer.common.dialogs.components.NameEditPanel();
        typeLabel = new javax.swing.JLabel();
        fieldsLabel = new javax.swing.JLabel();
        fieldsExtTextField = new org.radixware.kernel.common.components.ExtendableTextField();
        purityLevelEditor = new org.radixware.kernel.designer.dds.editors.PurityLevelEditor();
        sqmlPanel = new javax.swing.JPanel();
        typeEditPanel = new org.radixware.kernel.designer.common.dialogs.components.NameEditPanel();
        chIsDeprecated = new javax.swing.JCheckBox();
        descriptionEditor = new org.radixware.kernel.designer.common.dialogs.components.description.DescriptionEditor();

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(DdsPlSqlObjectEditor.class, "DdsPlSqlObjectEditor.jLabel1.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(typeLabel, org.openide.util.NbBundle.getMessage(DdsPlSqlObjectEditor.class, "DdsPlSqlObjectEditor.typeLabel.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(fieldsLabel, org.openide.util.NbBundle.getMessage(DdsPlSqlObjectEditor.class, "DdsPlSqlObjectEditor.fieldsLabel.text")); // NOI18N

        sqmlPanel.setLayout(new java.awt.BorderLayout(0, 5));

        org.openide.awt.Mnemonics.setLocalizedText(chIsDeprecated, org.openide.util.NbBundle.getMessage(DdsPlSqlObjectEditor.class, "DdsPlSqlObjectEditor.chIsDeprecated.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sqmlPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 701, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(descriptionEditor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(typeLabel)
                            .addComponent(fieldsLabel))
                        .addGap(32, 32, 32)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fieldsExtTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 605, Short.MAX_VALUE)
                            .addComponent(typeEditPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 605, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(nameEditPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 504, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(chIsDeprecated))))
                    .addComponent(purityLevelEditor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(nameEditPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chIsDeprecated))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(typeLabel)
                    .addComponent(typeEditPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fieldsLabel)
                    .addComponent(fieldsExtTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(descriptionEditor, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(purityLevelEditor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sqmlPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    //getPlSqlObject().setName(edName.getText());
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox chIsDeprecated;
    private org.radixware.kernel.designer.common.dialogs.components.description.DescriptionEditor descriptionEditor;
    private org.radixware.kernel.common.components.ExtendableTextField fieldsExtTextField;
    private javax.swing.JLabel fieldsLabel;
    private javax.swing.JLabel jLabel1;
    private org.radixware.kernel.designer.common.dialogs.components.NameEditPanel nameEditPanel;
    private org.radixware.kernel.designer.dds.editors.PurityLevelEditor purityLevelEditor;
    private javax.swing.JPanel sqmlPanel;
    private org.radixware.kernel.designer.common.dialogs.components.NameEditPanel typeEditPanel;
    private javax.swing.JLabel typeLabel;
    // End of variables declaration//GEN-END:variables
    private JButton fieldsButton;    

    @Override
    public boolean open(OpenInfo openInfo) {
        setupInitialValues();
        return super.open(openInfo);
    }

    @Override
    public void update() {
        setupInitialValues();
    }

    protected DdsPlSqlObjectDef getPlSqlObject() {
        return getRadixObject();
    }

    @EditorFactoryRegistration
    public static final class Factory implements IEditorFactory<DdsPlSqlObjectDef> {

        @Override
        public RadixObjectEditor newInstance(DdsPlSqlObjectDef ddsPlSqlObject) {
            return new DdsPlSqlObjectEditor(ddsPlSqlObject);
        }
    }
}
