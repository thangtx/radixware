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

package org.radixware.kernel.designer.common.editors;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import org.openide.util.ChangeSupport;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IEnumDef;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDomainDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.components.localizing.HandleInfo;
import org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingEditorPanel;
import org.radixware.kernel.designer.common.dialogs.components.state.StateDisplayer;
import org.radixware.kernel.designer.common.dialogs.components.state.StateManager;
import org.radixware.kernel.designer.common.editors.AdsDefinitionIconPresentation.IconIdChangeEvent;

/**
 * Embedded panel for NewItemDialog dialog
 *
 */
public class NewItemPanel extends javax.swing.JPanel {

    private javax.swing.JLabel nameLabel = new javax.swing.JLabel(NbBundle.getMessage(NewItemPanel.class, "NewItemPanel-NameLabel"));
    private javax.swing.JLabel valueLabel = new javax.swing.JLabel(NbBundle.getMessage(NewItemPanel.class, "NewItemPanel-ValueLabel"));
    private javax.swing.JTextField nameTextField = new javax.swing.JTextField();
    private javax.swing.JTextField valueTextField = new javax.swing.JTextField();
    private javax.swing.JComboBox valueComboBox = new javax.swing.JComboBox();
    private AdsDefinitionIconPresentation iconEditor = new AdsDefinitionIconPresentation();
    private LocalizingEditorPanel titleEditor = new LocalizingEditorPanel();
    private StateDisplayer stateDisplayer1 = new StateDisplayer();
    private javax.swing.JPanel content = new javax.swing.JPanel();
    private final StateManager stateManager;
    private final AdsEnumDef enumDefinition;
    private final AdsEnumItemDef item;
    private final AdsEnumItemDef original;
    private DocumentListener documentListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            changedUpdate(e);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            changedUpdate(e);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            isComplete();
            changeSupport.fireChange();
        }
    };
    private boolean isHexMode = false;
    private EnumItemDomainsEditor domainsEditor = new EnumItemDomainsEditor(null);

    private static class IdCbItem {

        static final IdCbItem UNDEFINED = new IdCbItem(null);
        private final Id value;
        private final EDefinitionIdPrefix prefix;

        public IdCbItem(EDefinitionIdPrefix prefix) {
            this.prefix = prefix;
            if (prefix != null) {
                this.value = Id.Factory.newInstance(prefix);
            } else {
                value = null;
            }
        }

        public String getValue() {
            return value == null ? null : value.toString();
        }

        @Override
        public String toString() {
            if (prefix == null) {
                return "<undefined>";
            } else {
                return prefix.name();
            }
        }
    }

    private void setupIdComboBox() {
        List<IdCbItem> items = new LinkedList<>();
        for (EDefinitionIdPrefix prefix : EDefinitionIdPrefix.values()) {
            items.add(new IdCbItem(prefix));
        }
        Collections.sort(items, new Comparator<IdCbItem>() {
            @Override
            public int compare(IdCbItem o1, IdCbItem o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });
        items.add(0, IdCbItem.UNDEFINED);
        ComboBoxModel model = new DefaultComboBoxModel(items.toArray(new Object[items.size()]));
        model.setSelectedItem(IdCbItem.UNDEFINED);
        valueComboBox.setModel(model);
        valueComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                isComplete();
                changeSupport.fireChange();
            }
        });
    }

    public NewItemPanel(AdsEnumDef enumDefinition, AdsEnumItemDef original, final boolean isHexMode) {
        this(enumDefinition, original, isHexMode, false);
    }
    private final boolean edit;

    public static void install(AdsEnumDef enumDefinition, AdsEnumItemDef target, AdsEnumItemDef source, boolean isHexMode) {
        target.setName(source.getName());
        target.setIconId(source.getIconId());
        target.setValue(source.getValue());
        if (isHexMode) {
            try {
                Long asLong = Long.valueOf(source.getValue().toString());
                String asHex = Long.toHexString(asLong).toUpperCase();
                target.setValue(ValAsStr.Factory.loadFrom(asHex));
            } catch (NumberFormatException ex) {
                target.setValue(source.getValue());
            }
        } else {
            target.setValue(source.getValue());
        }

        final AdsLocalizingBundleDef bundle = enumDefinition.findLocalizingBundle();
        if (bundle != null) {
            final AdsMultilingualStringDef src = bundle.findLocalizedString(source.getTitleId());
            if (src != null) {
                final AdsMultilingualStringDef string = AdsMultilingualStringDef.Factory.newInstance(src);
                bundle.getStrings().getLocal().add(string);
                target.setTitleId(string.getId());
            }
        }

        target.getDomains().clearDomains();
        final Branch branch = source.getBranch();
        for (final Id domainId : source.getDomainIds()) {
            final AdsDomainDef domain = (AdsDomainDef) branch.find(new VisitorProvider() {
                @Override
                public boolean isTarget(RadixObject radixObject) {
                    return radixObject instanceof AdsDomainDef && ((Definition) radixObject).getId() == domainId;
                }
            });
            if (domain != null) {
                target.getDomains().addDomain(domain);
            }
        }
    }

    public NewItemPanel(AdsEnumDef enumDefinition, AdsEnumItemDef original, final boolean isHexMode, boolean edit) {
        this.stateManager = new StateManager(content);
        this.isHexMode = isHexMode;

        this.enumDefinition = enumDefinition;

        this.edit = edit;
        this.original = original;

        this.item = org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef.Factory.newTemporaryInstance(enumDefinition);

        if (original != null) {
            install(enumDefinition, item, original, isHexMode);
        } else {
            this.item.setName("<undefined>");
        }

        content.setLayout(new GridBagLayout());

        setupVerifiers();

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.0;
        c.gridy = 0;
        content.add(nameLabel, c);

        c = new GridBagConstraints();
        c.insets = new Insets(10, 0, 10, 10);
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        content.add(nameTextField, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(0, 10, 10, 10);
        c.weightx = 0.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        if (enumDefinition.isIdEnum()) {
            valueLabel.setText("Identifier prefix:");
        }
        content.add(valueLabel, c);

        c = new GridBagConstraints();
        c.insets = new Insets(0, 0, 10, 10);
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;

        final JComponent valueEditor;
        if (enumDefinition.isIdEnum()) {
            valueEditor = valueComboBox;
            setupIdComboBox();
        } else {
            valueEditor = valueTextField;
        }
        content.add(valueEditor, c);

        c = new GridBagConstraints();
        c.gridy = 2;
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 10, 0, 10);

        JLabel lblDomainsEditor = new JLabel("Domains:");
        content.add(lblDomainsEditor, c);

        c = new GridBagConstraints();
        c.gridy = 2;
        c.gridx = 1;
        c.insets = new Insets(0, 0, 0, 10);
        c.fill = GridBagConstraints.HORIZONTAL;
        content.add(domainsEditor, c);
        domainsEditor.open(item);


        c = new GridBagConstraints();
        c.gridy = 3;
        c.gridx = 1;
        c.insets = new Insets(10, 0, 10, 10);
        c.fill = GridBagConstraints.HORIZONTAL;
        content.add(titleEditor, c);

        c = new GridBagConstraints();
        c.gridy = 4;
        c.gridx = 1;
        c.weightx = 0.0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.WEST;
        content.add(iconEditor, c);
        iconEditor.getIconIdChangeSupport().addEventListener(new AdsDefinitionIconPresentation.IconIdStateChangeListener() {
            @Override
            public void onEvent(IconIdChangeEvent e) {
                item.setIconId(e.iconId);
            }
        });

        c = new GridBagConstraints();
        c.gridy = 5;
        c.gridx = 1;
        c.weightx = 1.0;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        content.add(stateDisplayer1, c);

        setLayout(new BorderLayout());
        add(content, BorderLayout.NORTH);

        titleEditor.open(new HandleInfo() {
            @Override
            public AdsDefinition getAdsDefinition() {
                return item;
            }

            @Override
            public Id getTitleId() {
                return item.getTitleId();
            }

            @Override
            protected void onAdsMultilingualStringDefChange(IMultilingualStringDef stringDef) {
                if (stringDef != null) {
                    item.setTitleId(stringDef.getId());
                } else {
                    item.setTitleId(null);
                }
            }

            @Override
            protected void onLanguagesPatternChange(EIsoLanguage language, String newStringValue) {
                IMultilingualStringDef stringDef = getAdsMultilingualStringDef();
                if (stringDef != null) {
                    stringDef.setValue(language, newStringValue);
                }
            }
        });

        iconEditor.open(item, item.getIconId());

        this.nameTextField.getDocument().addDocumentListener(documentListener);
        if (!enumDefinition.isIdEnum()) {
            this.valueTextField.getDocument().addDocumentListener(documentListener);
            if (item.getValue() != null) {
                valueTextField.setText(item.getValue().toString());
            }
            final EValType itemType = enumDefinition.getItemType();
            if (itemType == EValType.INT) {
                ((AbstractDocument) valueTextField.getDocument()).setDocumentFilter(new NumericDocumentFilter(isHexMode));
            }
        }
        nameTextField.setText(item.getName());
    }

    private void setupVerifiers() {

        nameTextField.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                return !nameTextField.getText().isEmpty();
            }
        });

//        final EValType defsType = enumDefinition.getItemType();
//        if (defsType == EValType.CHAR) {
//            valueTextField.setInputVerifier(new InputVerifier() {
//
//                @Override
//                public boolean verify(JComponent input) {
//                    final String value = valueTextField.getText();
//                    return !value.isEmpty() && value.length() == 1;
//                }
//            });
//            ((AbstractDocument) valueTextField.getDocument()).setDocumentFilter(new DocumentSizeFilter(1));
//        } else if (defsType == EValType.INT) {
//            valueTextField.setInputVerifier(new InputVerifier() {
//
//                @Override
//                public boolean verify(JComponent input) {
//                    try {
//                        Integer.parseInt(valueTextField.getText());
//                    } catch (NumberFormatException e) {
//                        return false;
//                    }
//
//                    return true;
//                }
//            });
//            ((AbstractDocument) valueTextField.getDocument()).setDocumentFilter(new NumericDocumentFilter());
//        } else {
//            valueTextField.setInputVerifier(new InputVerifier() {
//
//                @Override
//                public boolean verify(JComponent input) {
//                    final String value = valueTextField.getText();
//                    return !value.isEmpty() && !value.equals("<undefined>");
//                }
//            });
//        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setPreferredSize(new java.awt.Dimension(328, 74));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 368, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 145, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    ChangeSupport changeSupport = new ChangeSupport(this);

    public final void addChangeListener(ChangeListener l) {
        changeSupport.addChangeListener(l);
    }

    public final void removeChangeListener(ChangeListener l) {
        changeSupport.removeChangeListener(l);
    }

    public boolean isComplete() {
        boolean checkResult = false;

        final String itemName = nameTextField.getText();
        final String itemValue;
        if (enumDefinition.isIdEnum()) {
            Object selectedItem = valueComboBox.getSelectedItem();
            if (selectedItem instanceof IdCbItem) {
                IdCbItem cbItem = (IdCbItem) selectedItem;
                if (cbItem == IdCbItem.UNDEFINED) {
                    stateManager.error(NbBundle.getMessage(NewItemPanel.class, "Error-Item-Value-Is-Invalid-Id"));
                    return false;
                } else {
                    itemValue = cbItem.getValue();
                    if (itemValue == null || itemValue.isEmpty()) {
                        stateManager.error(NbBundle.getMessage(NewItemPanel.class, "Error-Item-Value-Is-Invalid-Id"));
                        return false;
                    }
                }
            } else {
                stateManager.error(NbBundle.getMessage(NewItemPanel.class, "Error-Item-Value-Is-Invalid-Id"));
                return false;
            }
        } else {
            itemValue = valueTextField.getText();
        }


        if (!stringValueIsDefined(itemName)) {
            stateManager.error(NbBundle.getMessage(NewItemPanel.class, "Error-Item-Name-Is-Undefined"));
        } else if (!itemNameIsUnique(itemName)) {
            stateManager.error(NbBundle.getMessage(NewItemPanel.class, "Error-Item-Name-Is-Not-Unique"));
        } else {
            final EValType defsType = enumDefinition.getItemType();

            if (defsType == EValType.CHAR) {
                //check for valid char value
                if (itemValue.length() != 1) {
                    stateManager.error(NbBundle.getMessage(NewItemPanel.class, "Error-Item-Value-Is-Invalid-Char"));
                } else {
                    checkResult = true;
                }
            } else if (defsType == EValType.INT) {
                //check for valid integer value
                checkResult = checkForValidInteger(itemValue);
                if (!checkResult) {
                    stateManager.error(NbBundle.getMessage(NewItemPanel.class, "Error-Item-Value-Is-Invalid-Integer"));
                } else {
                    checkResult = true;
                }
            } else if (defsType == EValType.STR) {
                //check for valid string value
                if (!stringValueIsDefined(itemValue)) {
                    stateManager.error(NbBundle.getMessage(NewItemPanel.class, "Error-Item-Value-Is-Undefined"));
                } else {
                    checkResult = true;
                }
            } else {
                //improper behaviour
                stateManager.error(NbBundle.getMessage(NewItemPanel.class, "Error-Definition-Has-Unsupported-Type"));
            }
        }

        if (checkResult) {
            //okay
            stateManager.ok();
            item.setName(itemName);
            if (enumDefinition.getItemType().equals(EValType.INT) && isHexMode) {
                Long asLong = Long.parseLong(itemValue, 16);
                item.setValue(ValAsStr.Factory.loadFrom(asLong.toString()));
            } else {
                item.setValue(ValAsStr.Factory.loadFrom(itemValue));
            }

        }

        return checkResult;
    }

    public IEnumDef.IItem getCreatedItem() {
        if (edit) {
            return item;
        }

        AdsEnumItemDef copy = item.getClipboardSupport().duplicate();

        return copy;
    }

    public Map<EIsoLanguage, String> getTitles(EIsoLanguage[] languages) {
        Map<EIsoLanguage, String> result = new HashMap<>();
        for (EIsoLanguage l : languages) {
            result.put(l, item.getTitle(l));
        }
        return result;
    }

    private boolean stringValueIsDefined(String name) {
        return name != null && !name.isEmpty() && !name.equals("<undefined>");
    }

    private boolean itemNameIsUnique(String name) {

        for (IEnumDef.IItem xItem : enumDefinition.getItems().list(EScope.ALL)) {
            if (edit && original == xItem) {
                continue;
            }
            if (xItem.getName().equals(name)) {
                return false;
            }
        }

        return true;
    }

    private boolean checkForValidInteger(String value) {
        try {
            if (isHexMode) {
                Long.parseLong(value, 16);
            } else {
                Long.parseLong(value);
            }
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
