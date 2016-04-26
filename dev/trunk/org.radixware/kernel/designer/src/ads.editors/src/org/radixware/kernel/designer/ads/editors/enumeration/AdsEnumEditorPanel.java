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

package org.radixware.kernel.designer.ads.editors.enumeration;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JCheckBox;
import javax.swing.SwingUtilities;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.ads.common.dialogs.AccessEditPanel;
import org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel;
import org.radixware.kernel.designer.common.dialogs.components.DescriptionPanel;
import org.radixware.kernel.designer.common.dialogs.components.localizing.HandleInfo;
import org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingStringEditor;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;


public class AdsEnumEditorPanel extends javax.swing.JPanel {

    private AdsEnumDef enumDef;
    private final DescriptionPanel descriptionEditor = new DescriptionPanel();
    private final javax.swing.JLabel ovrLabel = new javax.swing.JLabel(NbBundle.getMessage(AdsEnumEditorPanel.class, "OverwrittenTip"));
    private final DefinitionLinkEditPanel ovrEditor = new DefinitionLinkEditPanel();
    private final PublishedEnumStatefullPanel pubEditor = new PublishedEnumStatefullPanel();
    private final javax.swing.JCheckBox isExtendableCheck = new javax.swing.JCheckBox(NbBundle.getMessage(AdsEnumEditorPanel.class, "Extendable"));
    //private javax.swing.JCheckBox isIdCheck = new javax.swing.JCheckBox(NbBundle.getMessage(AdsEnumEditorPanel.class, "IsId"));
    private final javax.swing.JButton synchButton = new javax.swing.JButton(NbBundle.getMessage(AdsEnumEditorPanel.class, "Synchronize"));
    private final LocalizingStringEditor titleEditor = LocalizingStringEditor.Factory.createLineEditor(new LocalizingStringEditor.Options().add(LocalizingStringEditor.Options.COLLAPSABLE_KEY, true)
            .add(LocalizingStringEditor.Options.TITLE_KEY, "Title")
            .add(LocalizingStringEditor.Options.MODE_KEY, LocalizingStringEditor.EEditorMode.LINE));
    private AdsEnumEditorTabbedPanel tabbedEditor;
    private final AccessEditPanel accessBox = new AccessEditPanel();
    private final JCheckBox jDeprecated = new JCheckBox();
    private final ItemListener extendListener = new ItemListener() {
        volatile boolean inProcess = false;

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (inProcess || AdsEnumEditorPanel.this.isUpdate) {
                return;
            }

            assert SwingUtilities.isEventDispatchThread();

            inProcess = true;

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        final boolean isSelected = isExtendableCheck.isSelected();
                        if (!isSelected && !DialogUtils.messageConfirmation("Are you sure to disable extensibility?")) {
                            isExtendableCheck.setSelected(true);
                            return;
                        }
                        AdsEnumEditorPanel.this.tabbedEditor.onExtendablityChange(isSelected);

                    } finally {
                        inProcess = false;
                    }
                }
            });


        }
    };
//    private ActionListener isIdListener = new ActionListener() {
//
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            if (!AdsEnumEditorPanel.this.isUpdate) {
//                if (isIdCheck.isSelected()) {
//                    List<String> errors = new LinkedList<String>();
//                    if (!enumDef.canBeIdEnum(errors)) {
//                        DialogUtils.messageError(errors.get(0));
//                        AdsEnumEditorPanel.this.isUpdate = true;
//                        isIdCheck.setSelected(false);
//                        AdsEnumEditorPanel.this.isUpdate = false;
//                    } else {
//                        enumDef.setIdEnum(true);
//                    }
//                } else {
//                    enumDef.setIdEnum(false);
//                }
//            }
//        }
//    };

    public AdsEnumEditorPanel() {
        ovrEditor.setClearable(false);

        isExtendableCheck.addItemListener(extendListener);
        //  isIdCheck.addActionListener(isIdListener);
        synchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!AdsEnumEditorPanel.this.isUpdate) {
                    AdsEnumEditorPanel.this.tabbedEditor.onSynchronizeAction(e);
                }
            }
        });
    }

    private void setupUI() {
        removeAll();
        final GridBagLayout gbl = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        final javax.swing.JPanel content = new javax.swing.JPanel();
        content.setLayout(gbl);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 10, 10, 10);
        c.gridwidth = 3;
        gbl.setConstraints(descriptionEditor, c);
        content.add(descriptionEditor);

        c.gridy = 1;
        final AdsEnumDef overwritten = enumDef.getHierarchy().findOverwritten().get();
        final boolean isOverwritten = overwritten != null;
        if (isOverwritten) {
            c.insets = new Insets(0, 10, 10, 10);
            c.gridwidth = 1;
            gbl.setConstraints(ovrLabel, c);
            content.add(ovrLabel);

            c.gridx = 1;
            c.weightx = 1.0;
            c.insets = new Insets(0, 0, 10, 10);
            gbl.setConstraints(ovrEditor, c);
            content.add(ovrEditor);

            c.gridy = 2;
            c.gridx = 0;
            c.gridwidth = 3;
            c.weightx = 0.0;
        }

        final boolean isPublisher = enumDef.isPlatformEnumPublisher();
        if (isPublisher) {
            c.weightx = 1.0;
            c.gridwidth = 2;
            c.insets = new Insets(0, 10, 0, 0);
            c.anchor = GridBagConstraints.CENTER;
            gbl.setConstraints(pubEditor, c);
            content.add(pubEditor);

            c.gridy = 1;
            c.weightx = 0.0;
            c.weighty = 0.0;

            c.insets = new Insets(0, 10, 0, 10);

            c.gridwidth = 1;
            //   c.insets = new Insets(10, 10, 10, 10);
            final Dimension sz = synchButton.getPreferredSize();
            if (sz != null) {
                final Dimension dim2 = pubEditor.getPreferredSize();
                if (dim2 != null) {
                    sz.height = dim2.height;
                    synchButton.setPreferredSize(sz);
                }
            }
            gbl.setConstraints(synchButton, c);
            content.add(synchButton);

            c.gridy = 2;
            c.insets = new Insets(0, 10, 10, 10);
            c.weightx = 0.0;
            gbl.setConstraints(isExtendableCheck, c);
            content.add(isExtendableCheck);



            c.gridwidth = 2;
            c.gridy = 4;
        }

        c.weightx = 1.0;
        c.gridwidth = 3;
        c.insets = new Insets(0, 10, 10, 10);
        gbl.setConstraints(titleEditor, c);
        content.add(titleEditor);

        c.gridy = isPublisher ? 5 : (isOverwritten ? 3 : 2);


        final javax.swing.JLabel label = new javax.swing.JLabel(NbBundle.getMessage(AdsEnumEditorPanel.class, "AccessBoxLabelTip"));
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0.0;
        c.insets = new Insets(0, 10, 10, 0);
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.WEST;

        gbl.setConstraints(label, c);

        content.add(label);
        c.insets = new Insets(0, 10, 10, 10);
        c.gridx = 1;

        gbl.setConstraints(accessBox, c);

        content.add(accessBox);
        c.insets = new Insets(0, 10, 10, 10);
        c.gridx = 2;
        c.anchor = GridBagConstraints.EAST;

        gbl.setConstraints(jDeprecated, c);

        content.add(jDeprecated);

        jDeprecated.setText(
                "Deprecated");
        jDeprecated.setSelected(enumDef.isDeprecated());
        jDeprecated.addActionListener(
                new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //System.out.println(enumDef.getClass().toString());
                enumDef.setDeprecated(jDeprecated.isSelected());
            }
        });



//        if (enumDef.getItemType() == EValType.STR) {
//            c.gridx = 2;
//            gbl.setConstraints(isIdCheck, c);
//            content.add(isIdCheck);
//        }



        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = isPublisher ? 6 : (isOverwritten ? 4 : 3);
        c.weighty = 1.0;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.BOTH;
        tabbedEditor = new AdsEnumEditorTabbedPanel();

        gbl.setConstraints(tabbedEditor, c);

        content.add(tabbedEditor);

        setLayout(
                new BorderLayout());
        add(content, BorderLayout.CENTER);
    }

    public void open(final AdsEnumDef enumDef, OpenInfo info) {
        this.enumDef = enumDef;
        setupUI();
        tabbedEditor.open(enumDef, info);
        update();
        final RadixObject target = info.getTarget();
        if (target instanceof AdsEnumItemDef) {
            tabbedEditor.setSelectedEnumItem((AdsEnumItemDef) target);
        }
    }
    private boolean isUpdate = false;

    public void update() {
        isUpdate = true;
        final boolean readonly = enumDef.isReadOnly();
        descriptionEditor.open(enumDef);



        final AdsEnumDef ovrwritten = enumDef.getHierarchy().findOverwritten().get();
        if (ovrwritten != null) {
            ovrEditor.open(ovrwritten, ovrwritten.getId());
            ovrEditor.setEnabled(!readonly);
        }

        if (enumDef.isPlatformEnumPublisher()) {
            pubEditor.open(enumDef);
            synchButton.setEnabled(!readonly);
            isExtendableCheck.setEnabled(!readonly);
            isExtendableCheck.setSelected(enumDef.isExtendable());
        }

//        isIdCheck.setEnabled(!readonly);
//        isIdCheck.setSelected(enumDef.isIdEnum());

        accessBox.open(enumDef);

        titleEditor.open(new HandleInfo() {
            @Override
            public AdsDefinition getAdsDefinition() {
                return enumDef;
            }

            @Override
            public Id getTitleId() {
                return enumDef.getTitleId();
            }

            @Override
            protected void onAdsMultilingualStringDefChange(IMultilingualStringDef multilingualStringDef) {

                if (multilingualStringDef != null) {
                    enumDef.setTitleId(multilingualStringDef.getId());
                } else {
                    enumDef.setTitleId(null);
                }
            }

            @Override
            protected void onLanguagesPatternChange(EIsoLanguage language, String newStringValue) {
                getAdsMultilingualStringDef().setValue(language, newStringValue);
            }
        });
        titleEditor.setReadonly(readonly);

        tabbedEditor.update();

        isUpdate = false;
    }
}