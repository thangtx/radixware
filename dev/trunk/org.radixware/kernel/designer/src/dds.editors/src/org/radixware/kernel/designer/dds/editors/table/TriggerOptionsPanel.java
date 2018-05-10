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
 * TriggerOptionsPanel.java
 *
 * Created on Feb 17, 2009, 11:25:23 AM
 */
package org.radixware.kernel.designer.dds.editors.table;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.conventions.RadixdocConventions;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.HierarchyWalker;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangedEvent;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangesListener;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProvider;
import org.radixware.kernel.common.defs.ads.module.ModuleDefinitions;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsTriggerDef;
import org.radixware.kernel.common.defs.dds.providers.DdsVisitorProviderFactory;
import org.radixware.kernel.common.defs.dds.utils.DbNameUtils;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.radixdoc.RadixdocUtils;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ConfigureDefinitionList;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ConfigureDefinitionListCfg;
import org.radixware.kernel.designer.common.dialogs.components.localizing.HandleInfo;
import org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingStringEditor;
import org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingStringEditor.EEditorMode;
import org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingStringEditor.Options;
import org.radixware.kernel.designer.common.dialogs.components.state.IStateDisplayer;
import org.radixware.kernel.designer.common.dialogs.components.state.StateDisplayer;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlDocument;
import org.radixware.kernel.designer.common.dialogs.utils.NameAcceptorFactory;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;
import org.radixware.kernel.designer.dds.script.DdsScriptGeneratorUtils;

public class TriggerOptionsPanel extends javax.swing.JPanel implements ContainerChangesListener, IUpdateable {

    @Override
    public void update() {
        if (trigger == null) {
            return;
        }
        updating = true;

//        CheckForDuplicationProvider checkForDuplicationProvider = CheckForDuplicationProvider.Factory.newForRenaming(trigger);
//        triggerNameEditPanel.setCheckForDuplicationProvider(checkForDuplicationProvider);
        triggerNameEditPanel.setCurrentName(trigger.getName());
        triggerAutoDbNameCheckBox.setSelected(trigger.isAutoDbName());
        triggerDbNameEditPanel.setDbName(trigger.getDbName());

        triggerDbNameEditPanel.setNameAcceptor(NameAcceptorFactory.newDbNameAcceptor(trigger.getLayer()));

        forEachRowCheckBox.setSelected(trigger.isForEachRow());
        disabledCheckBox.setSelected(trigger.isDisabled());

        if (trigger.getActuationTime() == DdsTriggerDef.EActuationTime.AFTER) {
            afterRadioButton.setSelected(true);
        } else if (trigger.getActuationTime() == DdsTriggerDef.EActuationTime.BEFORE) {
            beforeRadioButton.setSelected(true);
        } else {
            insteadOfRadioButton.setSelected(true);
        }

        insertCheckBox.setSelected(trigger.getTriggeringEvents().contains(DdsTriggerDef.ETriggeringEvent.ON_INSERT));
        deleteCheckBox.setSelected(trigger.getTriggeringEvents().contains(DdsTriggerDef.ETriggeringEvent.ON_DELETE));
        updateColumnsList();

        if (trigger.getType() == DdsTriggerDef.EType.FOR_AUDIT) {
            forAuditRadioButton.setSelected(true);
        } else if (trigger.getType() == DdsTriggerDef.EType.FOR_USER_PROPS) {
            forUserPropertiesRadioButton.setSelected(true);
        } else {
            noneRadioButton.setSelected(true);
        }
        sqmlEditorPanel.open(trigger.getBody());
        setReadOnly(trigger.isReadOnly());

        chIsDeprecated.setSelected(trigger.isDeprecated());
        updating = false;

        if (trigger.getType() == DdsTriggerDef.EType.FOR_AUDIT && radixdocOwnerDef != null) {
            ((LocalizingStringEditor) descriptionEditor).update(auditDescriptionHandleInfo);
            ((LocalizingStringEditor) descriptionEditor).setReadonly(true);
        } else {
            ((LocalizingStringEditor) descriptionEditor).update(descriptionHandleInfo);
        }
    }

    public interface TriggerChangeListener {

        public void triggerChanged(DdsTriggerDef trigger);
    }

    private static final Id AUDIT_DESCRIPTION_ID = Id.Factory.loadFrom("mlsNDNKDELXRRF5LKEEN4SC35L4GI");

    private DdsTriggerDef trigger = null;
    private boolean readOnly = false;
    private boolean inherited = false;
    private volatile boolean updating = false;
    private final ArrayList<TriggerChangeListener> listeners = new ArrayList<TriggerChangeListener>();
    private final StateDisplayer stateDisplayer = new StateDisplayer();
    private final JButton chooseColumnsButton;
    private Definition radixdocOwnerDef;
    private final HandleInfo descriptionHandleInfo = new HandleInfo() {
        @Override
        public Definition getAdsDefinition() {
            return trigger;
        }

        @Override
        public Id getTitleId() {
            if (trigger.getDescriptionId() != null) {
                return trigger.getDescriptionId();
            } else {
                return null;
            }
        }

        @Override
        protected void onAdsMultilingualStringDefChange(IMultilingualStringDef multilingualStringDef) {
            if (multilingualStringDef != null) {
                trigger.setDescriptionId(multilingualStringDef.getId());
            } else {
                trigger.setDescriptionId(null);
            }
        }

        @Override
        protected void onLanguagesPatternChange(EIsoLanguage language, String newStringValue) {
            if (getAdsMultilingualStringDef() != null) {
                getAdsMultilingualStringDef().setValue(language, newStringValue);
            }
        }
    };
    private final HandleInfo auditDescriptionHandleInfo = new HandleInfo() {
        @Override
        public Definition getAdsDefinition() {
            return radixdocOwnerDef;
        }

        @Override
        public Id getTitleId() {
            return AUDIT_DESCRIPTION_ID;
        }
    };

    private class TriggerSqmlDocument extends ScmlDocument {

        public TriggerSqmlDocument(String mimeType) {
            super(mimeType);
        }

        public TriggerSqmlDocument(Class kitClass) {
            super(kitClass);
        }

        @Override
        protected String getPreviewContent() {
            return DdsScriptGeneratorUtils.getTriggeCreationScript(trigger, null /*handler*/);
        }

        @Override
        public Scml createDefaultScml() {
            return Sqml.Factory.newInstance();
        }
    }

    /**
     * Creates new form TriggerOptionsPanel
     */
    public TriggerOptionsPanel() {
        initComponents();

        final TriggerSqmlDocument sqmlDocument = new TriggerSqmlDocument(sqmlEditorPanel.getPane().getContentType());
        sqmlEditorPanel.getPane().setDocument(sqmlDocument);

        IStateDisplayer.Locator.register(stateDisplayer, this);

        extTextField.setReadOnly(true);
        chooseColumnsButton = extTextField.addButton();
        chooseColumnsButton.setIcon(RadixWareIcons.DIALOG.CHOOSE.getIcon(13, 13));
        chooseColumnsButton.setToolTipText(NbBundle.getBundle(TriggerOptionsPanel.class).getString("CHOOSE_COLS"));
        chooseColumnsButton.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chooseColumnsActionPerformed();
            }
        });

        triggerNameEditPanel.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if (!updating && trigger != null && triggerNameEditPanel.isComplete()) {
                    trigger.setName(triggerNameEditPanel.getCurrentName());
//                    updateDbName();
                    for (TriggerChangeListener listener : listeners) {
                        listener.triggerChanged(trigger);
                    }
                }
            }
        });

        triggerDbNameEditPanel.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if (!updating && trigger != null && triggerDbNameEditPanel.isComplete()) {
                    trigger.setDbName(triggerDbNameEditPanel.getDbName());
                }
            }
        });

        chIsDeprecated.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!updating && trigger != null) {
                    trigger.setDeprecated(chIsDeprecated.isSelected());
                    for (TriggerChangeListener listener : listeners) {
                        listener.triggerChanged(trigger);
                    }
                }
            }
        });

        this.setVisible(false);
        stateDisplayer.setVisible(false);
    }

    @Override
    public void onEvent(ContainerChangedEvent e) {
        if (updating) {
            return;
        }
        updating = true;
        updateColumnsList();
        updating = false;
    }

    public StateDisplayer getStateDisplayer() {
        return stateDisplayer;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        updateEnableState();
    }

    private void updateEnableState() {
        triggerNameEditPanel.setEditable(!readOnly && !inherited);
        triggerAutoDbNameCheckBox.setEnabled(!readOnly && !inherited);
        boolean isAutoDbName = trigger != null ? trigger.isAutoDbName() : false;
        triggerDbNameEditPanel.setEditable(!readOnly && !inherited && !isAutoDbName);

        forEachRowCheckBox.setEnabled(!readOnly && !inherited);
        disabledCheckBox.setEnabled(!readOnly && !inherited);

        beforeRadioButton.setEnabled(!readOnly && !inherited);
        afterRadioButton.setEnabled(!readOnly && !inherited);
        insteadOfRadioButton.setEnabled(!readOnly && !inherited);
        insertCheckBox.setEnabled(!readOnly && !inherited);
        updateCheckBox.setEnabled(!readOnly && !inherited);
        deleteCheckBox.setEnabled(!readOnly && !inherited);
        forUserPropertiesRadioButton.setEnabled(!readOnly && !inherited);
        forAuditRadioButton.setEnabled(!readOnly && !inherited);
        noneRadioButton.setEnabled(!readOnly && !inherited);

        extTextField.setEnabled(!readOnly && !inherited);
        chooseColumnsButton.setEnabled(!readOnly && !inherited);
        sqmlEditorPanel.setEditable(!readOnly && !inherited);
        chIsDeprecated.setEnabled(!readOnly && !inherited);
        ((LocalizingStringEditor) descriptionEditor).setReadonly(readOnly);
    }

    public void setTrigger(DdsTriggerDef trigger, boolean inherited) {
        if (Utils.equals(this.trigger, trigger)) {
            return;
        }
        if (this.trigger != null) {
            this.trigger.getColumnsInfo().getContainerChangesSupport().removeEventListener(this);
        }
        this.trigger = trigger;
        this.inherited = inherited;
        if (trigger == null) {
            this.setVisible(false);
            stateDisplayer.setVisible(false);
            return;
        }
        trigger.getColumnsInfo().getContainerChangesSupport().addEventListener(this);

        if (radixdocOwnerDef == null) {            
            radixdocOwnerDef = RadixdocUtils.getRadixdocOwnerDef(trigger.getLayer());
        }

        update();
        this.setVisible(true);
        stateDisplayer.setVisible(true);
    }

    private void updateColumnsList() {
        StringBuilder str = new StringBuilder();
        for (DdsTriggerDef.ColumnInfo col : trigger.getColumnsInfo()) {
            DdsColumnDef ddsCol = col.findColumn();
            if (ddsCol != null) {
                str.append(ddsCol.getName() + ", ");
            } else {
                str.append("NULL, ");
            }

        }
        if (str.length() >= 2) {
            str.delete(str.length() - 2, str.length());
        }
//        updating = true;
        extTextField.setValue(str.toString());
        boolean onUpdate = trigger.getTriggeringEvents().contains(DdsTriggerDef.ETriggeringEvent.ON_UPDATE);
        updateCheckBox.setSelected(onUpdate);
        forColumnsLabel.setVisible(onUpdate);
        extTextField.setVisible(onUpdate);
//        updating = false;
    }

    private void updateDbName() {
        if (trigger.isAutoDbName()) {
            DbNameUtils.updateAutoDbNames(trigger);
//            updating = true;
            triggerDbNameEditPanel.setDbName(trigger.getDbName());
//            updating = false;
        }
    }

    public void addTriggerChangeListener(TriggerChangeListener listener) {
        listeners.add(listener);
    }

    public void removeTriggerChangeListener(TriggerChangeListener listener) {
        listeners.remove(listener);
    }

    @Override
    public boolean requestFocusInWindow() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                triggerNameEditPanel.requestFocusInWindow();
            }
        });
        return super.requestFocusInWindow();
    }

    public void open(OpenInfo openInfo) {
        sqmlEditorPanel.open(trigger.getBody(), openInfo);
        if (trigger.getType() == DdsTriggerDef.EType.FOR_AUDIT && radixdocOwnerDef != null) {
            ((LocalizingStringEditor) descriptionEditor).update(auditDescriptionHandleInfo);
            ((LocalizingStringEditor) descriptionEditor).setReadonly(true);
        } else {
            ((LocalizingStringEditor) descriptionEditor).update(descriptionHandleInfo);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        triggerNameEditPanel = new org.radixware.kernel.designer.common.dialogs.components.NameEditPanel();
        triggerAutoDbNameCheckBox = new javax.swing.JCheckBox();
        triggerDbNameEditPanel = new org.radixware.kernel.designer.dds.editors.DbNameEditPanel();
        forEachRowCheckBox = new javax.swing.JCheckBox();
        disabledCheckBox = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        beforeRadioButton = new javax.swing.JRadioButton();
        afterRadioButton = new javax.swing.JRadioButton();
        insteadOfRadioButton = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        insertCheckBox = new javax.swing.JCheckBox();
        deleteCheckBox = new javax.swing.JCheckBox();
        updateCheckBox = new javax.swing.JCheckBox();
        jPanel4 = new javax.swing.JPanel();
        forUserPropertiesRadioButton = new javax.swing.JRadioButton();
        forAuditRadioButton = new javax.swing.JRadioButton();
        noneRadioButton = new javax.swing.JRadioButton();
        forColumnsLabel = new javax.swing.JLabel();
        extTextField = new org.radixware.kernel.common.components.ExtendableTextField();
        sqmlEditorPanel = new org.radixware.kernel.designer.common.editors.sqml.SqmlEditorPanel();
        chIsDeprecated = new javax.swing.JCheckBox();
        descriptionEditor = LocalizingStringEditor.Factory.createEditor(
            new Options()
            .add(Options.COLLAPSABLE_KEY, true)
            .add(Options.TITLE_KEY, "Description")
            .add(Options.MODE_KEY, EEditorMode.MULTILINE)
        );

        jLabel1.setText(org.openide.util.NbBundle.getMessage(TriggerOptionsPanel.class, "TriggerOptionsPanel.jLabel1.text")); // NOI18N

        triggerAutoDbNameCheckBox.setText(org.openide.util.NbBundle.getMessage(TriggerOptionsPanel.class, "TriggerOptionsPanel.triggerAutoDbNameCheckBox.text")); // NOI18N
        triggerAutoDbNameCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                triggerAutoDbNameCheckBoxItemStateChanged(evt);
            }
        });

        forEachRowCheckBox.setText(org.openide.util.NbBundle.getMessage(TriggerOptionsPanel.class, "TriggerOptionsPanel.forEachRowCheckBox.text")); // NOI18N
        forEachRowCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                forEachRowCheckBoxItemStateChanged(evt);
            }
        });

        disabledCheckBox.setText(org.openide.util.NbBundle.getMessage(TriggerOptionsPanel.class, "TriggerOptionsPanel.disabledCheckBox.text")); // NOI18N
        disabledCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                disabledCheckBoxItemStateChanged(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(TriggerOptionsPanel.class, "TriggerOptionsPanel.jPanel1.border.title"))); // NOI18N

        buttonGroup1.add(beforeRadioButton);
        beforeRadioButton.setText(org.openide.util.NbBundle.getMessage(TriggerOptionsPanel.class, "TriggerOptionsPanel.beforeRadioButton.text")); // NOI18N
        beforeRadioButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                beforeRadioButtonItemStateChanged(evt);
            }
        });

        buttonGroup1.add(afterRadioButton);
        afterRadioButton.setText(org.openide.util.NbBundle.getMessage(TriggerOptionsPanel.class, "TriggerOptionsPanel.afterRadioButton.text")); // NOI18N
        afterRadioButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                afterRadioButtonItemStateChanged(evt);
            }
        });

        buttonGroup1.add(insteadOfRadioButton);
        insteadOfRadioButton.setText(org.openide.util.NbBundle.getMessage(TriggerOptionsPanel.class, "TriggerOptionsPanel.insteadOfRadioButton.text")); // NOI18N
        insteadOfRadioButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                insteadOfRadioButtonItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(beforeRadioButton)
                    .addComponent(afterRadioButton)
                    .addComponent(insteadOfRadioButton))
                .addContainerGap(41, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(beforeRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(afterRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(insteadOfRadioButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(TriggerOptionsPanel.class, "TriggerOptionsPanel.jPanel2.border.title"))); // NOI18N

        insertCheckBox.setText(org.openide.util.NbBundle.getMessage(TriggerOptionsPanel.class, "TriggerOptionsPanel.insertCheckBox.text")); // NOI18N
        insertCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                insertCheckBoxItemStateChanged(evt);
            }
        });

        deleteCheckBox.setText(org.openide.util.NbBundle.getMessage(TriggerOptionsPanel.class, "TriggerOptionsPanel.deleteCheckBox.text")); // NOI18N
        deleteCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                deleteCheckBoxItemStateChanged(evt);
            }
        });

        updateCheckBox.setText(org.openide.util.NbBundle.getMessage(TriggerOptionsPanel.class, "TriggerOptionsPanel.updateCheckBox.text")); // NOI18N
        updateCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                updateCheckBoxItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(insertCheckBox)
                    .addComponent(deleteCheckBox)
                    .addComponent(updateCheckBox))
                .addContainerGap(56, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(insertCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deleteCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(updateCheckBox)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(TriggerOptionsPanel.class, "TriggerOptionsPanel.jPanel4.border.title"))); // NOI18N

        buttonGroup2.add(forUserPropertiesRadioButton);
        forUserPropertiesRadioButton.setText(org.openide.util.NbBundle.getMessage(TriggerOptionsPanel.class, "TriggerOptionsPanel.forUserPropertiesRadioButton.text")); // NOI18N
        forUserPropertiesRadioButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                forUserPropertiesRadioButtonItemStateChanged(evt);
            }
        });

        buttonGroup2.add(forAuditRadioButton);
        forAuditRadioButton.setText(org.openide.util.NbBundle.getMessage(TriggerOptionsPanel.class, "TriggerOptionsPanel.forAuditRadioButton.text")); // NOI18N
        forAuditRadioButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                forAuditRadioButtonItemStateChanged(evt);
            }
        });

        buttonGroup2.add(noneRadioButton);
        noneRadioButton.setText(org.openide.util.NbBundle.getMessage(TriggerOptionsPanel.class, "TriggerOptionsPanel.noneRadioButton.text")); // NOI18N
        noneRadioButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                noneRadioButtonItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(noneRadioButton)
                    .addComponent(forUserPropertiesRadioButton)
                    .addComponent(forAuditRadioButton))
                .addContainerGap(36, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(noneRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(forUserPropertiesRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(forAuditRadioButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        forColumnsLabel.setText(org.openide.util.NbBundle.getMessage(TriggerOptionsPanel.class, "TriggerOptionsPanel.forColumnsLabel.text")); // NOI18N

        chIsDeprecated.setText(org.openide.util.NbBundle.getMessage(TriggerOptionsPanel.class, "TriggerOptionsPanel.chIsDeprecated.text")); // NOI18N

        javax.swing.GroupLayout descriptionEditorLayout = new javax.swing.GroupLayout(descriptionEditor);
        descriptionEditor.setLayout(descriptionEditorLayout);
        descriptionEditorLayout.setHorizontalGroup(
            descriptionEditorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        descriptionEditorLayout.setVerticalGroup(
            descriptionEditorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sqmlEditorPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 526, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(descriptionEditor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(triggerAutoDbNameCheckBox)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(triggerNameEditPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(chIsDeprecated))
                            .addComponent(triggerDbNameEditPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 397, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(forColumnsLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(extTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(forEachRowCheckBox)
                        .addGap(87, 87, 87)
                        .addComponent(disabledCheckBox)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(triggerNameEditPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(chIsDeprecated))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(descriptionEditor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(triggerAutoDbNameCheckBox)
                    .addComponent(triggerDbNameEditPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(forEachRowCheckBox)
                    .addComponent(disabledCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(forColumnsLabel)
                    .addComponent(extTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sqmlEditorPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void triggerAutoDbNameCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_triggerAutoDbNameCheckBoxItemStateChanged
        if (updating) {
            return;
        }
        boolean isAutoDbName = triggerAutoDbNameCheckBox.isSelected();
        triggerDbNameEditPanel.setEditable(!isAutoDbName);
        trigger.setAutoDbName(isAutoDbName);
        if (isAutoDbName) {
            String dbName = trigger.calcAutoDbName();
            trigger.setDbName(dbName);
            updating = true;
            triggerDbNameEditPanel.setDbName(dbName);
            updating = false;
        } else {
            if (triggerAutoDbNameCheckBox.hasFocus()) {
                triggerDbNameEditPanel.requestFocusInWindow();
                triggerDbNameEditPanel.selectAll();
            }
        }
    }//GEN-LAST:event_triggerAutoDbNameCheckBoxItemStateChanged

    private void forEachRowCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_forEachRowCheckBoxItemStateChanged
        if (updating) {
            return;
        }
        trigger.setForEachRow(forEachRowCheckBox.isSelected());
        updating = true;
        updateDbName();
        updating = false;
    }//GEN-LAST:event_forEachRowCheckBoxItemStateChanged

    private void disabledCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_disabledCheckBoxItemStateChanged
        if (updating) {
            return;
        }
        trigger.setDisabled(disabledCheckBox.isSelected());
    }//GEN-LAST:event_disabledCheckBoxItemStateChanged

    private void beforeRadioButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_beforeRadioButtonItemStateChanged
        if (updating) {
            return;
        }
        if (beforeRadioButton.isSelected()) {
            trigger.setActuationTime(DdsTriggerDef.EActuationTime.BEFORE);
            updating = true;
            updateDbName();
            updating = false;
        }
    }//GEN-LAST:event_beforeRadioButtonItemStateChanged

    private void afterRadioButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_afterRadioButtonItemStateChanged
        if (updating) {
            return;
        }
        if (afterRadioButton.isSelected()) {
            trigger.setActuationTime(DdsTriggerDef.EActuationTime.AFTER);
            updating = true;
            updateDbName();
            updating = false;
        }
    }//GEN-LAST:event_afterRadioButtonItemStateChanged

    private void insteadOfRadioButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_insteadOfRadioButtonItemStateChanged
        if (updating) {
            return;
        }
        if (insteadOfRadioButton.isSelected()) {
            trigger.setActuationTime(DdsTriggerDef.EActuationTime.INSTEAD_OF);
            updating = true;
            updateDbName();
            updating = false;
        }
    }//GEN-LAST:event_insteadOfRadioButtonItemStateChanged

    private void insertCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_insertCheckBoxItemStateChanged
        if (updating) {
            return;
        }
        if (insertCheckBox.isSelected()) {
            trigger.getTriggeringEvents().add(DdsTriggerDef.ETriggeringEvent.ON_INSERT);
        } else {
            trigger.getTriggeringEvents().remove(DdsTriggerDef.ETriggeringEvent.ON_INSERT);
        }
        updating = true;
        updateDbName();
        updating = false;
    }//GEN-LAST:event_insertCheckBoxItemStateChanged

    private void deleteCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_deleteCheckBoxItemStateChanged
        if (updating) {
            return;
        }
        if (deleteCheckBox.isSelected()) {
            trigger.getTriggeringEvents().add(DdsTriggerDef.ETriggeringEvent.ON_DELETE);
        } else {
            trigger.getTriggeringEvents().remove(DdsTriggerDef.ETriggeringEvent.ON_DELETE);
        }
        updating = true;
        updateDbName();
        updating = false;
    }//GEN-LAST:event_deleteCheckBoxItemStateChanged

    private void updateCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_updateCheckBoxItemStateChanged
        if (updating) {
            return;
        }
        if (updateCheckBox.isSelected()) {
            trigger.getTriggeringEvents().add(DdsTriggerDef.ETriggeringEvent.ON_UPDATE);
        } else {
            trigger.getTriggeringEvents().remove(DdsTriggerDef.ETriggeringEvent.ON_UPDATE);
        }
        updating = true;
        updateColumnsList();
        updateDbName();
        updating = false;
    }//GEN-LAST:event_updateCheckBoxItemStateChanged

    private void forUserPropertiesRadioButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_forUserPropertiesRadioButtonItemStateChanged
        if (updating) {
            return;
        }
        if (forUserPropertiesRadioButton.isSelected()) {
            trigger.setType(DdsTriggerDef.EType.FOR_USER_PROPS);
        }
    }//GEN-LAST:event_forUserPropertiesRadioButtonItemStateChanged

    private void forAuditRadioButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_forAuditRadioButtonItemStateChanged
        if (updating) {
            return;
        }
        if (forAuditRadioButton.isSelected()) {
            trigger.setType(DdsTriggerDef.EType.FOR_AUDIT);
        }
    }//GEN-LAST:event_forAuditRadioButtonItemStateChanged

    private void noneRadioButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_noneRadioButtonItemStateChanged
        if (updating) {
            return;
        }
        if (noneRadioButton.isSelected()) {
            trigger.setType(DdsTriggerDef.EType.NONE);
        }
    }//GEN-LAST:event_noneRadioButtonItemStateChanged

    private void chooseColumnsActionPerformed() {
        final IFilter<DdsColumnDef> filter = DdsVisitorProviderFactory.newColumnForTriggerFilter(trigger);
        final List<Id> columnsIds = new ArrayList<Id>();
        for (DdsTriggerDef.ColumnInfo col : trigger.getColumnsInfo()) {
            columnsIds.add(col.getColumnId());
        }
        final Collection<DdsColumnDef> columnsForTrigger = trigger.getOwnerTable().getColumns().get(EScope.ALL, filter);
        final ConfigureDefinitionListCfg cfg = ConfigureDefinitionListCfg.Factory.newInstance(columnsForTrigger);
        if (ConfigureDefinitionList.configure(columnsIds, cfg)) {
            trigger.getColumnsInfo().clear();
            for (Id id : columnsIds) {
                trigger.getColumnsInfo().add(id);
            }
            updating = true;
            updateColumnsList();
            updating = false;
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton afterRadioButton;
    private javax.swing.JRadioButton beforeRadioButton;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JCheckBox chIsDeprecated;
    private javax.swing.JCheckBox deleteCheckBox;
    private javax.swing.JPanel descriptionEditor;
    private javax.swing.JCheckBox disabledCheckBox;
    private org.radixware.kernel.common.components.ExtendableTextField extTextField;
    private javax.swing.JRadioButton forAuditRadioButton;
    private javax.swing.JLabel forColumnsLabel;
    private javax.swing.JCheckBox forEachRowCheckBox;
    private javax.swing.JRadioButton forUserPropertiesRadioButton;
    private javax.swing.JCheckBox insertCheckBox;
    private javax.swing.JRadioButton insteadOfRadioButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JRadioButton noneRadioButton;
    private org.radixware.kernel.designer.common.editors.sqml.SqmlEditorPanel sqmlEditorPanel;
    private javax.swing.JCheckBox triggerAutoDbNameCheckBox;
    private org.radixware.kernel.designer.dds.editors.DbNameEditPanel triggerDbNameEditPanel;
    private org.radixware.kernel.designer.common.dialogs.components.NameEditPanel triggerNameEditPanel;
    private javax.swing.JCheckBox updateCheckBox;
    // End of variables declaration//GEN-END:variables
}
