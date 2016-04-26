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
 * EditOptionsPanelPanel.java
 *
 * Created on 12.08.2009, 10:02:13
 */
package org.radixware.kernel.designer.common.editors;

import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsParentRefPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsFilterDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EditOptions;
import org.radixware.kernel.common.defs.ads.clazz.presentation.PropertyEditOptions;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.ui.AdsCustomPropEditorDef;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtCustomPropEditorDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.EPropertyValueStorePossibility;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.components.localizing.HandleInfo;
import org.radixware.kernel.designer.common.editors.editmask.EditMaskOwner;
import org.radixware.kernel.designer.common.general.editors.EditorsManager;
import org.radixware.kernel.designer.common.general.utils.DefinitionsUtils;


public class EditOptionsPanel extends javax.swing.JPanel {

    private static class ValueStoreComboBoxModelItem {

        private EPropertyValueStorePossibility value;
        private static final ValueStoreComboBoxModelItem NONE = new ValueStoreComboBoxModelItem(EPropertyValueStorePossibility.NONE);

        public ValueStoreComboBoxModelItem(EPropertyValueStorePossibility value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value.getName();
        }
    }

    private static class ValueStoreComboBoxModel implements ComboBoxModel<ValueStoreComboBoxModelItem> {

        private ValueStoreComboBoxModelItem current;
        private final List<ValueStoreComboBoxModelItem> alternatives;
        private final List<ListDataListener> listeners = new LinkedList<>();
        private volatile boolean isUpdating;

        public ValueStoreComboBoxModel() {
            this.current = ValueStoreComboBoxModelItem.NONE;
            this.alternatives = new LinkedList<>();
            this.alternatives.add(this.current);
        }

        private void fireChange() {
            final ListDataEvent e = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, getSize());
            for (ListDataListener l : listeners) {
                l.contentsChanged(e);
            }
        }

        @Override
        public void setSelectedItem(Object anItem) {
            this.current = (ValueStoreComboBoxModelItem) anItem;
        }

        @Override
        public Object getSelectedItem() {
            return this.current;
        }

        @Override
        public int getSize() {
            return alternatives.size();
        }

        @Override
        public ValueStoreComboBoxModelItem getElementAt(int index) {
            return alternatives.get(index);
        }

        @Override
        public void addListDataListener(ListDataListener l) {
            listeners.add(l);
        }

        @Override
        public void removeListDataListener(ListDataListener l) {
            listeners.remove(l);
        }

        public boolean isUpdating() {
            return isUpdating;
        }

        void update(EditOptions editOptions) {
            try {
                isUpdating = true;

                if (editOptions.canChangeEditEnvironment()) {
                    this.alternatives.clear();
                    EPropertyValueStorePossibility cur = editOptions.getValueStorePossibility();
                    this.alternatives.clear();
                    for (EPropertyValueStorePossibility e : EPropertyValueStorePossibility.values()) {
                        ValueStoreComboBoxModelItem item = new ValueStoreComboBoxModelItem(e);
                        if (cur == e) {
                            this.current = item;
                        }
                        this.alternatives.add(item);
                    }

                } else {
                    this.current = new ValueStoreComboBoxModelItem(editOptions.getValueStorePossibility());
                    this.alternatives.clear();
                    this.alternatives.add(this.current);
                }
                fireChange();
            } finally {
                isUpdating = false;
            }
        }
    }

    private static class EditEnvComboBoxModelItem {

        private static final EditEnvComboBoxModelItem NONE = new EditEnvComboBoxModelItem(null);
        private final ERuntimeEnvironmentType env;

        public EditEnvComboBoxModelItem(ERuntimeEnvironmentType env) {
            this.env = env;
        }

        @Override
        public String toString() {
            if (this.env == null) {
                return "Any";
            } else {
                return this.env.getName();
            }
        }
    }

    private static class EditEnvComboBoxModel implements ComboBoxModel<EditEnvComboBoxModelItem> {

        private EditEnvComboBoxModelItem current;
        private final List<EditEnvComboBoxModelItem> alternatives;
        private final List<ListDataListener> listeners = new LinkedList<>();
        private volatile boolean isUpdating;

        public EditEnvComboBoxModel() {
            this.current = EditEnvComboBoxModelItem.NONE;
            this.alternatives = new LinkedList<>();
            this.alternatives.add(this.current);
        }

        void update(EditOptions editOptions) {
            try {
                isUpdating = true;

                if (editOptions.canChangeEditEnvironment()) {
                    this.alternatives.clear();
                    ERuntimeEnvironmentType env = editOptions.getEditEnvironment();
                    this.alternatives.clear();
                    for (ERuntimeEnvironmentType e : new ERuntimeEnvironmentType[]{ERuntimeEnvironmentType.COMMON_CLIENT, ERuntimeEnvironmentType.EXPLORER, ERuntimeEnvironmentType.WEB}) {
                        EditEnvComboBoxModelItem item = new EditEnvComboBoxModelItem(e);
                        if (env == e) {
                            this.current = item;
                        }
                        this.alternatives.add(item);
                    }

                } else {
                    this.current = new EditEnvComboBoxModelItem(editOptions.getEditEnvironment());
                    this.alternatives.clear();
                    this.alternatives.add(this.current);
                }
                fireChange();
            } finally {
                isUpdating = false;
            }
        }

        private void fireChange() {
            final ListDataEvent e = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, getSize());
            for (ListDataListener l : listeners) {
                l.contentsChanged(e);
            }
        }

        @Override
        public void setSelectedItem(Object anItem) {
            this.current = (EditEnvComboBoxModelItem) anItem;
        }

        @Override
        public Object getSelectedItem() {
            return this.current;
        }

        @Override
        public int getSize() {
            return alternatives.size();
        }

        @Override
        public EditEnvComboBoxModelItem getElementAt(int index) {
            return alternatives.get(index);
        }

        @Override
        public void addListDataListener(ListDataListener l) {
            listeners.add(l);
        }

        @Override
        public void removeListDataListener(ListDataListener l) {
            listeners.remove(l);
        }

        public boolean isUpdating() {
            return isUpdating;
        }
    }

    public EditOptionsPanel() {
        super();
        initComponents();
        this.cbEditEnvironment.setModel(editEnvModel);
        this.cbEditEnvironment.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (editOptions != null && !editEnvModel.isUpdating()) {
                    editOptions.setEditEnvironment(((EditEnvComboBoxModelItem) editEnvModel.getSelectedItem()).env);
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            updateCustomDialogSelectorState();
                        }
                    });
                }
            }
        });
        this.cbLoadStoreValueMode.setModel(valueStoreModel);
        this.cbLoadStoreValueMode.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (editOptions != null && !valueStoreModel.isUpdating()) {
                    editOptions.setValueStorePossibility(((ValueStoreComboBoxModelItem) valueStoreModel.getSelectedItem()).value);
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            updateCustomDialogSelectorState();
                        }
                    });
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        cbEditEnvironment = new javax.swing.JComboBox();
        jbtEditMask = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        cbLoadStoreValueMode = new javax.swing.JComboBox();
        localizingPaneList = new org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingEditorPanel("Display instead of '<Not Defined>'");
        localizingPaneList1 = new org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingEditorPanel("Empty array title");
        localizingPaneList2 = new org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingEditorPanel("Null array element title");
        jPanel20 = new javax.swing.JPanel();
        jchbEditingCustomDialog = new javax.swing.JCheckBox();
        jchbEditingCustomEditOnly = new javax.swing.JCheckBox();
        depParentEPExp = new org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel();
        depParentEPWeb = new org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel26 = new javax.swing.JPanel();
        jchbEditingNotNull = new javax.swing.JCheckBox();
        jchbEditingMemo = new javax.swing.JCheckBox();
        jchbEditingStoreEditHistory = new javax.swing.JCheckBox();

        setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        jLabel3.setText(org.openide.util.NbBundle.getMessage(EditOptionsPanel.class, "EditOptionsPanel.jLabel3.text")); // NOI18N

        cbEditEnvironment.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jbtEditMask.setText(org.openide.util.NbBundle.getMessage(EditOptionsPanel.class, "EditOptionsPanel.jbtEditMask.text")); // NOI18N
        jbtEditMask.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtEditMaskActionPerformed(evt);
            }
        });

        jLabel4.setText(org.openide.util.NbBundle.getMessage(EditOptionsPanel.class, "EditOptionsPanel.jLabel4.text")); // NOI18N

        cbLoadStoreValueMode.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbEditEnvironment, 0, 214, Short.MAX_VALUE)
                    .addComponent(cbLoadStoreValueMode, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jbtEditMask)
                .addContainerGap(339, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cbEditEnvironment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtEditMask))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cbLoadStoreValueMode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        add(jPanel1);
        add(localizingPaneList);
        add(localizingPaneList1);
        add(localizingPaneList2);

        jPanel20.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(EditOptionsPanel.class, "EditOptionsPanel.jPanel20.border.title"))); // NOI18N

        jchbEditingCustomDialog.setText(org.openide.util.NbBundle.getMessage(EditOptionsPanel.class, "EditOptionsPanel.jchbEditingCustomDialog.text")); // NOI18N
        jchbEditingCustomDialog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jchbEditingCustomDialogActionPerformed(evt);
            }
        });

        jchbEditingCustomEditOnly.setText(org.openide.util.NbBundle.getMessage(EditOptionsPanel.class, "EditOptionsPanel.jchbEditingCustomEditOnly.text")); // NOI18N
        jchbEditingCustomEditOnly.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jchbEditingCustomEditOnlyActionPerformed(evt);
            }
        });

        jLabel1.setText(org.openide.util.NbBundle.getMessage(EditOptionsPanel.class, "EditOptionsPanel.jLabel1.text")); // NOI18N

        jLabel2.setText(org.openide.util.NbBundle.getMessage(EditOptionsPanel.class, "EditOptionsPanel.jLabel2.text")); // NOI18N

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addComponent(jchbEditingCustomDialog)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jchbEditingCustomEditOnly)
                .addContainerGap(544, Short.MAX_VALUE))
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(depParentEPWeb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(depParentEPExp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jchbEditingCustomDialog, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jchbEditingCustomEditOnly))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(depParentEPExp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(depParentEPWeb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        add(jPanel20);

        jchbEditingNotNull.setText(org.openide.util.NbBundle.getMessage(EditOptionsPanel.class, "EditOptionsPanel.jchbEditingNotNull.text")); // NOI18N
        jchbEditingNotNull.setAlignmentY(0.0F);
        jchbEditingNotNull.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jchbEditingNotNullActionPerformed(evt);
            }
        });

        jchbEditingMemo.setText(org.openide.util.NbBundle.getMessage(EditOptionsPanel.class, "EditOptionsPanel.jchbEditingMemo.text")); // NOI18N
        jchbEditingMemo.setAlignmentY(0.0F);
        jchbEditingMemo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jchbEditingMemoActionPerformed(evt);
            }
        });

        jchbEditingStoreEditHistory.setText(org.openide.util.NbBundle.getMessage(EditOptionsPanel.class, "EditOptionsPanel.jchbEditingStoreEditHistory.text")); // NOI18N
        jchbEditingStoreEditHistory.setAlignmentY(0.0F);
        jchbEditingStoreEditHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jchbEditingStoreEditHistoryActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jchbEditingNotNull)
                .addGap(37, 37, 37)
                .addComponent(jchbEditingMemo)
                .addGap(35, 35, 35)
                .addComponent(jchbEditingStoreEditHistory)
                .addContainerGap(471, Short.MAX_VALUE))
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jchbEditingNotNull)
                .addComponent(jchbEditingMemo)
                .addComponent(jchbEditingStoreEditHistory))
        );

        add(jPanel26);
    }// </editor-fold>//GEN-END:initComponents
    private boolean isMayModify;
    private boolean isReadOnly;
    private EditEnvComboBoxModel editEnvModel = new EditEnvComboBoxModel();
    private ValueStoreComboBoxModel valueStoreModel = new ValueStoreComboBoxModel();
    private boolean isStringType;
    EditOptions editOptions = null;
    AdsDefinition context = null;
    AdsTypeDeclaration type = null;

    private class HandelInfoEx extends HandleInfo {

        final int mode_null_title = 0;
        final int mode_arr_title = 1;
        final int mode_arr_item_title = 2;
        EditOptionsPanel panel;
        int mode;

        HandelInfoEx(EditOptionsPanel panel, int mode) {
            this.panel = panel;
            this.mode = mode;
        }

        @Override
        public AdsDefinition getAdsDefinition() {
            return context;
        }

        @Override
        public Id getTitleId() {
            if (editOptions != null) {
                if (mode == 0) {
                    return editOptions.getNullValTitleId();
                } else if (mode == 1) {
                    return editOptions.getEmptyArrayValTitleId();
                } else if (mode == 2 && editOptions instanceof PropertyEditOptions) {
                    return ((PropertyEditOptions) editOptions).getNullArrayElementTitleId();
                }
            }
            return null;
        }

        @Override
        public void onAdsMultilingualStringDefChange(IMultilingualStringDef adsMultilingualStringDef) {

            if (!isMayModify || isReadOnly) {
                return;
            }

            if (mode == 0) {
                if (adsMultilingualStringDef != null) {
                    editOptions.setNullValTitleId(adsMultilingualStringDef.getId());
                } else {
                    editOptions.setNullValTitleId(null);
                }
            } else if (mode == 1) {
                if (adsMultilingualStringDef != null) {
                    editOptions.setEmptyArrayValTitleId(adsMultilingualStringDef.getId());
                } else {
                    editOptions.setEmptyArrayValTitleId(null);
                }

            } else if (mode == 2 && editOptions instanceof PropertyEditOptions) {
                if (adsMultilingualStringDef != null) {
                    ((PropertyEditOptions) editOptions).setNullArrayElementTitleId(adsMultilingualStringDef.getId());
                } else {
                    ((PropertyEditOptions) editOptions).setNullArrayElementTitleId(null);
                }
            }
        }

        @Override
        public void onLanguagesPatternChange(EIsoLanguage language, String newStringValue) {
            IMultilingualStringDef string = getAdsMultilingualStringDef();
            if (string != null){
                string.setValue(language, newStringValue);
            }
        }
    }

    private class CustomDlgVisitorProvider extends VisitorProvider {

        final ERuntimeEnvironmentType env;

        public CustomDlgVisitorProvider(ERuntimeEnvironmentType env) {
            this.env = env;
        }

        @Override
        public boolean isTarget(RadixObject radixObject) {
            if (env == ERuntimeEnvironmentType.EXPLORER) {
                return (radixObject instanceof AdsCustomPropEditorDef);
            } else {
                return (radixObject instanceof AdsRwtCustomPropEditorDef);
            }
        }
    }
    private ChangeListener chlParentEPChangedListener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            if (!isMayModify || isReadOnly || editOptions == null) {
                return;
            }
            if (depParentEPExp.getDefinition() == null) {
                editOptions.setCustomDialogId(ERuntimeEnvironmentType.EXPLORER, null);
            } else {
                editOptions.setCustomDialogId(ERuntimeEnvironmentType.EXPLORER, depParentEPExp.getDefinition().getId());
            }
            if (depParentEPWeb.getDefinition() == null) {
                editOptions.setCustomDialogId(ERuntimeEnvironmentType.WEB, null);
            } else {
                editOptions.setCustomDialogId(ERuntimeEnvironmentType.WEB, depParentEPWeb.getDefinition().getId());
            }
            update();
        }
    };

    public void setReadOnly(boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
        update();
    }

    private void updateCustomDialogSelectorState() {
        boolean isCustomViewEnabled = editOptions != null && !isReadOnly && editOptions.isShowDialogButton();
        ERuntimeEnvironmentType editEnv = editOptions == null ? ERuntimeEnvironmentType.COMMON_CLIENT : editOptions.getEditEnvironment();
        depParentEPExp.setEnabled(isCustomViewEnabled && editEnv != ERuntimeEnvironmentType.WEB);
        depParentEPWeb.setEnabled(isCustomViewEnabled && editEnv != ERuntimeEnvironmentType.EXPLORER);
    }

    public void update() {

        updateCustomDialogSelectorState();

        jchbEditingStoreEditHistory.setEnabled(!isReadOnly);
        jchbEditingNotNull.setEnabled(!isReadOnly);
        
        boolean isArrayType = false;
        if (editOptions != null) {
            AdsTypeDeclaration currType = editOptions.getTypedObject().getType();
            if (currType != null) {
                EValType tyteId = currType.getTypeId();
                if (tyteId != null) {
                    isArrayType = tyteId.isArrayType();
                }
            }
        }
        localizingPaneList1.setVisible(isArrayType);
        boolean isArrayProp = isArrayType && editOptions instanceof PropertyEditOptions;
        if (isArrayType) {
            jchbEditingStoreEditHistory.setVisible(false);
        }

        localizingPaneList2.setVisible(isArrayProp);
        //jchbEditingCustomEditOnly.setEnabled(!isReadOnly);
        jchbEditingCustomEditOnly.setEnabled(editOptions != null && !isReadOnly && editOptions.isShowDialogButton());
        jchbEditingCustomDialog.setEnabled(!isReadOnly);


        jchbEditingMemo.setEnabled(!isReadOnly
                && (isStringType
                || !isStringType && editOptions != null && editOptions.isMemo()));

        if (editOptions == null) {
            return;
        }
        isMayModify = false;

        localizingPaneList.open(new HandelInfoEx(this, 0));
        localizingPaneList.setReadonly(isReadOnly);
        if (isArrayType) {
            localizingPaneList1.open(new HandelInfoEx(this, 1));
            localizingPaneList1.setReadonly(isReadOnly);
        }

        if (isArrayProp) {
            localizingPaneList2.open(new HandelInfoEx(this, 2));
            localizingPaneList2.setReadonly(isReadOnly);
        }



        jchbEditingCustomEditOnly.setSelected(editOptions.isCustomEditOnly());

        jchbEditingCustomDialog.setSelected(editOptions.isShowDialogButton());

        jchbEditingNotNull.setSelected(editOptions.isNotNull());

        jchbEditingMemo.setSelected(editOptions.isMemo());
        if (!isStringType && editOptions.isMemo()) {
            jchbEditingMemo.setForeground(Color.red);
        } else {
            jchbEditingMemo.setForeground(Color.black);
        }

        jbtEditMask.setIcon(
                editOptions == null || editOptions.getEditMask() == null
                ? RadixWareIcons.CREATE.NEW_DOCUMENT.getIcon()
                : RadixWareIcons.EDIT.EDIT.getIcon());

        jbtEditMask.setEnabled(!isReadOnly);


        jchbEditingStoreEditHistory.setSelected(editOptions.isStoreEditHistory());


        depParentEPExp.setComboMode(context);
        Collection<Definition> collection = DefinitionsUtils.collectTopAround(context, new CustomDlgVisitorProvider(ERuntimeEnvironmentType.EXPLORER));
        depParentEPExp.setComboBoxValues(collection, true);
        depParentEPWeb.setComboMode(context);
        collection = DefinitionsUtils.collectTopAround(context, new CustomDlgVisitorProvider(ERuntimeEnvironmentType.WEB));
        depParentEPWeb.setComboBoxValues(collection, true);
        if (editOptions != null) {
            depParentEPExp.open((Definition) editOptions.findCustomDialog(ERuntimeEnvironmentType.EXPLORER),
                    editOptions.getCustomDialogId(ERuntimeEnvironmentType.EXPLORER));
            depParentEPWeb.open((Definition) editOptions.findCustomDialog(ERuntimeEnvironmentType.WEB),
                    editOptions.getCustomDialogId(ERuntimeEnvironmentType.WEB));
        }


        boolean isFilterParameter = context instanceof AdsFilterDef.Parameter;
        boolean isParentRefPropertyDef = context instanceof AdsParentRefPropertyDef;
        jbtEditMask.setVisible(!isParentRefPropertyDef);
        jchbEditingMemo.setVisible(!isFilterParameter && !isParentRefPropertyDef);
        cbEditEnvironment.setEnabled(!isReadOnly);
        cbLoadStoreValueMode.setEnabled(!isReadOnly);
        isMayModify = true;
        editEnvModel.update(editOptions);
        valueStoreModel.update(editOptions);
    }

    public void setType(AdsTypeDeclaration type) {
        this.isStringType = type != null && (type.getTypeId().equals(EValType.STR)
                || type.getTypeId().equals(EValType.CLOB));
        this.type = type;
        update();
    }

    public void open(AdsDefinition context, EditOptions editOptions, AdsTypeDeclaration type) {

        this.isStringType = type != null && type.getTypeId() != null
                && (type.getTypeId().equals(EValType.STR)
                || type.getTypeId().equals(EValType.CLOB));
        this.editOptions = editOptions;
        this.type = type;
        this.context = context;
        depParentEPExp.addChangeListener(chlParentEPChangedListener);
        depParentEPWeb.addChangeListener(chlParentEPChangedListener);
        update();
    }
    private void jchbEditingNotNullActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jchbEditingNotNullActionPerformed
        if (!isMayModify || isReadOnly || editOptions == null) {
            return;
        }
        editOptions.setNotNull(jchbEditingNotNull.isSelected());
        update();
}//GEN-LAST:event_jchbEditingNotNullActionPerformed

    private void jchbEditingMemoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jchbEditingMemoActionPerformed
        if (!isMayModify || isReadOnly || editOptions == null) {
            return;
        }
        editOptions.setMemo(jchbEditingMemo.isSelected());
        update();
}//GEN-LAST:event_jchbEditingMemoActionPerformed

    private void jchbEditingStoreEditHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jchbEditingStoreEditHistoryActionPerformed
        if (!isMayModify || isReadOnly || editOptions == null) {
            return;
        }

        editOptions.setStoreEditHistory(jchbEditingStoreEditHistory.isSelected());
        update();
}//GEN-LAST:event_jchbEditingStoreEditHistoryActionPerformed

    private void jbtEditMaskActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtEditMaskActionPerformed
        if (!isMayModify || editOptions == null) {
            return;
        }
        final EditMaskOwner editMaskOwner = EditMaskOwner.Factory.newInstance(editOptions);
        editMaskOwner.setReadOnly(isReadOnly);
        EditorsManager.getDefault().open(editMaskOwner);
        update();
}//GEN-LAST:event_jbtEditMaskActionPerformed

    private void jchbEditingCustomDialogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jchbEditingCustomDialogActionPerformed
        if (!isMayModify || isReadOnly || editOptions == null) {
            return;
        }
        editOptions.setShowDialogButton(jchbEditingCustomDialog.isSelected());
        update();
}//GEN-LAST:event_jchbEditingCustomDialogActionPerformed

    private void jchbEditingCustomEditOnlyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jchbEditingCustomEditOnlyActionPerformed
        if (!isMayModify || isReadOnly || editOptions == null) {
            return;
        }
        editOptions.setCustomEditOnly(jchbEditingCustomEditOnly.isSelected());
        update();
}//GEN-LAST:event_jchbEditingCustomEditOnlyActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cbEditEnvironment;
    private javax.swing.JComboBox cbLoadStoreValueMode;
    private org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel depParentEPExp;
    private org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel depParentEPWeb;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JButton jbtEditMask;
    private javax.swing.JCheckBox jchbEditingCustomDialog;
    private javax.swing.JCheckBox jchbEditingCustomEditOnly;
    private javax.swing.JCheckBox jchbEditingMemo;
    private javax.swing.JCheckBox jchbEditingNotNull;
    private javax.swing.JCheckBox jchbEditingStoreEditHistory;
    private org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingEditorPanel localizingPaneList;
    private org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingEditorPanel localizingPaneList1;
    private org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingEditorPanel localizingPaneList2;
    // End of variables declaration//GEN-END:variables
}
