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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.dds.DdsColumnTemplateDef;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsTypeDef;
import org.radixware.kernel.common.defs.dds.providers.DdsVisitorProviderFactory;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.ValTypes;
import org.radixware.kernel.designer.common.annotations.registrators.EditorFactoryRegistration;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.editors.RadixObjectEditor;
import org.radixware.kernel.designer.common.general.editors.IEditorFactory;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;

/**
 * DdsColumnTemplateDef editor.
 *
 */
public final class DdsColumnTemplateEditor extends RadixObjectEditor<DdsColumnTemplateDef> {

    /**
     * Listener is invoked whenever {@linkplain DdsColumnTemplateDef} properties are changed
     */
    public interface DdsColumnTemplateChangeListener {

        public void ddsColumnTemplateChanged(DdsColumnTemplateDef columnTemplate);
    }
    private volatile boolean updating = false;
    private boolean readOnly = false;
    private final boolean isTemplate;
    private DdsColumnTemplateDef templateDef;
    private final ArrayList<DdsColumnTemplateChangeListener> listeners = new ArrayList<DdsColumnTemplateChangeListener>();

    protected DdsColumnTemplateEditor(DdsColumnTemplateDef ddsColumnTemplate) {
        super(ddsColumnTemplate);
        initComponents();

//        dbTypeTextField.setEditable(false);

        isTemplate = !(ddsColumnTemplate instanceof DdsColumnDef);
        if (!isTemplate) {
            templateDef = ((DdsColumnDef) ddsColumnTemplate).findTemplate();
        } else {
            templateDef = null;
        }

        valTypePanel.setFilter(ValTypes.DDS_COLUMN_TYPES);
        valTypePanel.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent evt) {
                valTypePanelStateChanged();
            }
        });

        linkPanel.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent evt) {
                linkPanelStateChanged();
            }
        });

        templateLinkEditPanel.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent evt) {
                templateLinkEditPanelStateChanged();
            }
        });

//        final CheckForDuplicationProvider checkForDuplicationProvider = CheckForDuplicationProvider.Factory.newForRenaming(getDefinition());
//        nameEditPanel.setCheckForDuplicationProvider(checkForDuplicationProvider);

        nameEditPanel.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if (!updating && nameEditPanel.isComplete()) {
                    if (!getColumnTemplate().getName().equals(nameEditPanel.getCurrentName())) {
                        getColumnTemplate().setName(nameEditPanel.getCurrentName());
                        fireDdsColumnTemplateChanged();
                    }
                }
            }
        });

//        lengthBox.addActionListener(new ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                DdsColumnTemplateEditor.this.lengthIsUnlimitedCheckBoxChange();
//            }
//        });
        chIsDeprecated.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!updating) {
                    getColumnTemplate().setDeprecated(chIsDeprecated.isSelected());
                    fireDdsColumnTemplateChanged();
                }
            }
        });

        update();
    }

    private void fireDdsColumnTemplateChanged() {
        for (DdsColumnTemplateChangeListener listener : listeners) {
            listener.ddsColumnTemplateChanged(getColumnTemplate());
        }
    }

//    private void lengthIsUnlimitedCheckBoxChange() {
//        final boolean isSelected = lengthBox.isSelected();
//        DdsColumnTemplateDef columnTemplateDef = getColumnTemplate();
//        if (!isSelected) {
//            columnTemplateDef.setLength(0);
//            lengthSpinBox.setEnabled(false);
//            //lengthLabel.setEnabled(false);
//        } else {
//            EValType valType = columnTemplateDef.getValType();
//            if (valType == EValType.INT || valType == EValType.NUM) {
//                columnTemplateDef.setLength(18);
//            } else if (valType == EValType.DATE_TIME) {
//                columnTemplateDef.setLength(6);
//            } else if (valType == EValType.ARR_BIN
//                    || valType == EValType.ARR_BLOB
//                    || valType == EValType.ARR_BOOL
//                    || valType == EValType.ARR_CHAR
//                    || valType == EValType.ARR_CLOB
//                    || valType == EValType.ARR_DATE_TIME
//                    || valType == EValType.ARR_INT
//                    || valType == EValType.ARR_NUM
//                    || valType == EValType.ARR_REF
//                    || valType == EValType.ARR_STR) {
//                columnTemplateDef.setLength(4000);
//            } else {
//                columnTemplateDef.setLength(0);
//            }
//            lengthSpinBox.setEnabled(true);
//            //lengthLabel.setEnabled(true);
//        }
//        lengthSpinBox.setValue(columnTemplateDef.getLength());
//    }
    /**
     * Allow or forbid user to edit DdsColumnTemplate properties
     * @param readOnly
     */
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        dbTypePanel1.setReadonly(readOnly);
        updateEnableState();
    }

    private boolean isReadOnly() {
        return getRadixObject().isReadOnly() || readOnly;
    }

    private void updateEnableState() {
        nameEditPanel.setEditable(!isReadOnly());
        if (!isTemplate) {
            boolean hasTemplate = templateDef != null;
            templateLinkEditPanel.setEnabled(!isReadOnly());
            valTypePanel.setEnabled(!isReadOnly() && !hasTemplate);
            updateLinkPanelEnabledState(!hasEnumOrDbType());
            //dbTypeTextField.setEditable(!isReadOnly() && !hasTemplate);
//            updateSpnsEnabledState(!hasTemplate);
        } else {
            templateLabel.setVisible(false);
            templateLinkEditPanel.setVisible(false);
            valTypePanel.setEnabled(!isReadOnly());
            updateLinkPanelEnabledState(true);
            //dbTypeTextField.setEditable(!isReadOnly());
//            updateSpnsEnabledState(true);
        }
        chIsDeprecated.setEnabled(!isReadOnly());
        descriptionPanel.update();
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean requestFocusInWindow() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                nameEditPanel.requestFocusInWindow();
            }
        });
        return super.requestFocusInWindow();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        linkPanel = new org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel();
        nameEditPanel = new org.radixware.kernel.designer.common.dialogs.components.NameEditPanel();
        valTypeLabel = new javax.swing.JLabel();
        linkDefLabel = new javax.swing.JLabel();
        templateLabel = new javax.swing.JLabel();
        templateLinkEditPanel = new org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel();
        jLabel1 = new javax.swing.JLabel();
        valTypePanel = new org.radixware.kernel.designer.common.dialogs.components.ValTypeComboBox();
        descriptionPanel = new org.radixware.kernel.designer.common.dialogs.components.DescriptionPanel();
        dbTypePanel1 = new org.radixware.kernel.designer.common.editors.DbTypePanel();
        chIsDeprecated = new javax.swing.JCheckBox();

        valTypeLabel.setLabelFor(valTypePanel);
        org.openide.awt.Mnemonics.setLocalizedText(valTypeLabel, org.openide.util.NbBundle.getMessage(DdsColumnTemplateEditor.class, "DdsColumnTemplateEditor.valTypeLabel.text")); // NOI18N

        linkDefLabel.setLabelFor(linkPanel);
        org.openide.awt.Mnemonics.setLocalizedText(linkDefLabel, org.openide.util.NbBundle.getMessage(DdsColumnTemplateEditor.class, "DdsColumnTemplateEditor.linkDefLabel.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(templateLabel, org.openide.util.NbBundle.getMessage(DdsColumnTemplateEditor.class, "DdsColumnTemplateEditor.templateLabel.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(DdsColumnTemplateEditor.class, "DdsColumnTemplateEditor.jLabel1.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(chIsDeprecated, org.openide.util.NbBundle.getMessage(DdsColumnTemplateEditor.class, "DdsColumnTemplateEditor.chIsDeprecated.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dbTypePanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 521, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(templateLabel)
                            .addComponent(valTypeLabel)
                            .addComponent(jLabel1)
                            .addComponent(linkDefLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(linkPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 407, Short.MAX_VALUE)
                            .addComponent(templateLinkEditPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 407, Short.MAX_VALUE)
                            .addComponent(valTypePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 407, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(nameEditPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(chIsDeprecated))))
                    .addComponent(descriptionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 521, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameEditPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(chIsDeprecated))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(templateLabel)
                    .addComponent(templateLinkEditPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(valTypeLabel)
                    .addComponent(valTypePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(linkDefLabel)
                    .addComponent(linkPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dbTypePanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(descriptionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void valTypePanelStateChanged() {
        if (!updating) {
            updating = true;
            DdsColumnTemplateDef columnTemplate = getColumnTemplate();
            columnTemplate.setValType(valTypePanel.getValType());
//            columnTemplate.setEnumId(null);
            columnTemplate.setNativeDbTypeId(null);
            updateLinkPanel();
            updateNativeDbTypeState();
            updateLinkPanelEnabledState(true);

            dbTypePanel1.valTypeChanged();
//            updating = false;
//            setupDefaultValues(columnTemplate);
//            updating = true;

//            updateSpnsEnabledState(true);
//            if (autoDbTypeCheckBox.isSelected()) {
            String dbType = columnTemplate.calcAutoDbType();
            columnTemplate.setDbType(dbType);
//            dbTypeTextField.setText(dbType);
//            }
            updating = false;
            fireDdsColumnTemplateChanged();
        }
    }

//    private void setupDefaultValues(DdsColumnTemplateDef columnTemplateDef) {
//        EValType valType = columnTemplateDef.getValType();
//        columnTemplateDef.setPrecision(0);
//        if (valType == EValType.BOOL || valType == EValType.CHAR) {
//            columnTemplateDef.setLength(1);
//        } else if (valType == EValType.STR) {
//            columnTemplateDef.setLength(100);
//        } else if (valType == EValType.BIN) {
//            columnTemplateDef.setLength(2000);
//        } else {
//            columnTemplateDef.setLength(0);
//        }
//        lengthSpinBox.setValue(columnTemplateDef.getLength());
//
//        precisionSpinBox.setValue(columnTemplateDef.getPrecision());
//        lengthBox.setSelected(columnTemplateDef.getLength() != 0);
//    }
    private void templateLinkEditPanelStateChanged() {
        if (!updating) {
            DdsColumnDef column = (DdsColumnDef) getColumnTemplate();
            Id definitionId = templateLinkEditPanel.getDefinitionId();
            column.setTemplateId(definitionId);
            templateDef = column.findTemplate();
            update();
            fireDdsColumnTemplateChanged();
        }
    }

    private void assignTemplate(DdsColumnTemplateDef template) {
        if (template == null || isReadOnly()) {
            return;
        }
        getColumnTemplate().setValType(template.getValType());
        if (template.getValType().equals(EValType.NATIVE_DB_TYPE)) {
            getColumnTemplate().setNativeDbTypeId(template.getNativeDbTypeId());
//            getColumnTemplate().setEnumId(null);
        } else {
            getColumnTemplate().setNativeDbTypeId(null);
//            getColumnTemplate().setEnumId(template.getEnumId());
        }
        getColumnTemplate().setDbType(template.getDbType());
        getColumnTemplate().setLength(template.getLength());
        getColumnTemplate().setPrecision(template.getPrecision());
    }

    private void linkPanelStateChanged() {
        if (!updating) {
            DdsColumnTemplateDef columnTemplate = getColumnTemplate();
            Id definitionId = linkPanel.getDefinitionId();
            if (valTypePanel.getValType().equals(EValType.NATIVE_DB_TYPE)) {
//                columnTemplate.setEnumId(null);
                columnTemplate.setNativeDbTypeId(definitionId);
                if (definitionId != null) {
                    DdsTypeDef nativeDbType = columnTemplate.getNativeDbType();
                    columnTemplate.setDbType(nativeDbType.getDbName());
                }
            } else {
                columnTemplate.setNativeDbTypeId(null);
//                columnTemplate.setEnumId(definitionId);
            }
            fireDdsColumnTemplateChanged();
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox chIsDeprecated;
    private org.radixware.kernel.designer.common.editors.DbTypePanel dbTypePanel1;
    private org.radixware.kernel.designer.common.dialogs.components.DescriptionPanel descriptionPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel linkDefLabel;
    private org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel linkPanel;
    private org.radixware.kernel.designer.common.dialogs.components.NameEditPanel nameEditPanel;
    private javax.swing.JLabel templateLabel;
    private org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel templateLinkEditPanel;
    private javax.swing.JLabel valTypeLabel;
    private org.radixware.kernel.designer.common.dialogs.components.ValTypeComboBox valTypePanel;
    // End of variables declaration//GEN-END:variables

    private DdsColumnTemplateDef getColumnTemplate() {
        return getRadixObject();
    }

    public void updateName() { //need for update of the name after editing one in the table
//        updating = true;
        final String name = getColumnTemplate().getName();
        if (name == null || !name.equals(nameEditPanel.getCurrentName())) {
            nameEditPanel.setCurrentName(name);
            fireDdsColumnTemplateChanged();
        }
//        updating = false;
    }

    private void updateTemplateLinkEditPanel() {
        VisitorProvider provider = DdsVisitorProviderFactory.newColumnTemplateForColumnProvider();
        Id id = templateDef != null ? templateDef.getId() : null;
        ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(getColumnTemplate(), provider);
        templateLinkEditPanel.open(cfg, templateDef, id);
    }

    private void updateLinkPanel() {
        if (!valTypePanel.getValType().equals(EValType.NATIVE_DB_TYPE)) {
            linkPanel.setVisible(false);
            return;
        }
        DdsColumnTemplateDef columnTemplate = getColumnTemplate();
        VisitorProvider provider = DdsVisitorProviderFactory.newNativeDbTypeForColumnTemplateProvider();
//        if (valTypePanel.getValType().equals(EValType.NATIVE_DB_TYPE)) {
//            provider = DdsVisitorProviderFactory.newNativeDbTypeForColumnTemplateProvider();
//        } else {
//            provider = DdsVisitorProviderFactory.newEnumForColumnTemplateProvider(columnTemplate);
//        }

        Id id = columnTemplate.getNativeDbTypeId();
        Definition definition = columnTemplate.findNativeDbType();
//        if (valTypePanel.getValType().equals(EValType.NATIVE_DB_TYPE)) {
//            id = columnTemplate.getNativeDbTypeId();
//            definition = columnTemplate.findNativeDbType();
//        } else {
////            id = columnTemplate.getEnumId();
////            definition = (Definition) columnTemplate.findEnum();
//        }

        ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(columnTemplate, provider);
        linkPanel.open(cfg, definition, id);
        linkPanel.setVisible(true);
    }

    private boolean hasEnumOrDbType() {
        if (isTemplate || templateDef == null) {
            return false;
        }
        if (valTypePanel.getValType().equals(EValType.NATIVE_DB_TYPE)) {
            return templateDef.findNativeDbType() != null;
        } else {
            return false; //templateDef.findEnum() != null;
        }
    }

    private void updateLinkPanelEnabledState(boolean enabled) {
        EValType valType = valTypePanel.getValType();
        linkPanel.setEnabled(!isReadOnly() && enabled
                && (/*valType == EValType.ARR_CHAR ||
                valType == EValType.ARR_INT ||
                valType == EValType.ARR_STR ||
                valType == EValType.CHAR ||
                valType == EValType.INT ||
                valType == EValType.STR ||*/valType == EValType.NATIVE_DB_TYPE));
    }

//    private void updateSpnsEnabledState(boolean enabled) {
//        EValType valType = valTypePanel.getValType();
//        boolean isPrecisionEnabled = !isReadOnly() && enabled && (valType == EValType.NUM || valType == EValType.DATE_TIME);
//        precisionLabel.setEnabled(isPrecisionEnabled);
//        precisionSpinBox.setEnabled(isPrecisionEnabled);
//
//        lengthBox.setEnabled(!isReadOnly() && enabled && (valType != EValType.CHAR
//                && valType != EValType.BOOL
//                && valType != EValType.BLOB
//                && valType != EValType.CLOB
//                && valType != EValType.BIN
//                && valType != EValType.STR
//                && valType != EValType.NATIVE_DB_TYPE
//                && valType != EValType.DATE_TIME));
//        boolean isLengthEnabled = !isReadOnly() && enabled
//                && (valType != EValType.CHAR
//                && valType != EValType.BOOL
//                && valType != EValType.BLOB
//                && valType != EValType.CLOB
//                && valType != EValType.NATIVE_DB_TYPE)
//                && valType != EValType.DATE_TIME;
//        lengthSpinBox.setEnabled(isLengthEnabled && lengthBox.isSelected());
//        lengthLabel.setEnabled(lengthBox.isEnabled() || lengthSpinBox.isEnabled());
//    }
    private void updateNativeDbTypeState() {
        boolean isNativeDbType = valTypePanel.getValType().equals(EValType.NATIVE_DB_TYPE);
        if (isNativeDbType) {
            linkDefLabel.setText(NbBundle.getBundle(DdsColumnTemplateEditor.class).getString("DB_TYPE"));
            linkDefLabel.setVisible(true);
        } else {
            linkDefLabel.setVisible(false);
//            linkDefLabel.setText(NbBundle.getBundle(DdsColumnTemplateEditor.class).getString("ENUM"));
        }
//        dbTypeTextField.setVisible(!isNativeDbType);
//        dbTypeLabel.setVisible(!isNativeDbType);
//        lengthLabel.setVisible(!isNativeDbType);
//        lengthSpinBox.setVisible(!isNativeDbType);
//        lengthBox.setVisible(!isNativeDbType);
//        precisionLabel.setVisible(!isNativeDbType);
//
//        precisionSpinBox.setVisible(precisionSpinBox.isVisible() && !isNativeDbType);
        dbTypePanel1.setVisible(!isNativeDbType);
    }

    @Override
    public boolean open(OpenInfo openInfo) {
        update();
        return super.open(openInfo);
    }

    @Override
    public void update() {
        updating = true;
        DdsColumnTemplateDef columnTemplate = getColumnTemplate();

        if (!isTemplate) {
            assignTemplate(templateDef); //synchronize column with it's template
            updateTemplateLinkEditPanel();
        }

        nameEditPanel.setCurrentName(columnTemplate.getName());

        valTypePanel.setValType(columnTemplate.getValType());
        updateLinkPanel();

//        dbTypeTextField.setText(columnTemplate.getDbType());
//
//        lengthSpinBox.setValue(columnTemplate.getLength());
//        lengthBox.setSelected(columnTemplate.getLength() != 0);
//
//        precisionSpinBox.setValue(columnTemplate.getPrecision());
//

        dbTypePanel1.open(columnTemplate);

        descriptionPanel.open(columnTemplate);

        chIsDeprecated.setSelected(columnTemplate.isDeprecated());
        updateNativeDbTypeState();

        updateEnableState();

        updating = false;
    }

    public void addDdsColumnTemplateChangeListener(DdsColumnTemplateChangeListener listener) {
        listeners.add(listener);
    }

    public void removeDdsColumnTemplateChangeListener(DdsColumnTemplateChangeListener listener) {
        listeners.remove(listener);
    }

    @EditorFactoryRegistration
    public static final class Factory implements IEditorFactory<DdsColumnTemplateDef> {

        @Override
        public DdsColumnTemplateEditor newInstance(DdsColumnTemplateDef ddsColumnTemplate) {
            return new DdsColumnTemplateEditor(ddsColumnTemplate);
        }
    }
}
