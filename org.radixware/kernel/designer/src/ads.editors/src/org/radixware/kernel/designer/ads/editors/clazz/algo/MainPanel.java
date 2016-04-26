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
package org.radixware.kernel.designer.ads.editors.clazz.algo;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.algo.AdsAlgoClassDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.ads.common.dialogs.AccessPanel;
import org.radixware.kernel.designer.ads.editors.clazz.simple.SuperClassHierarchyPanel;
import org.radixware.kernel.designer.common.dialogs.components.DescriptionPanel;
import org.radixware.kernel.designer.common.dialogs.components.localizing.HandleInfo;
import org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingEditorPanel;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;

public class MainPanel extends JPanel {

    private AdsAlgoClassDef algoDef;
    private AccessPanel accessEditor = new AccessPanel();
    private GeneralPanel generalPanel;
    private JCheckBox deprecatedBox;
    private LocalizingEditorPanel titlePanel;
    private DescriptionPanel descriptionPanel;
    private SuperClassHierarchyPanel superHierarchyPanel;
    private JLabel accessLabel = new JLabel("Access:");
    private boolean initialized = false;
    private final ItemListener itemListener = new ItemListener() {

        @Override
        public void itemStateChanged(ItemEvent event) {
            if (!isUpdate) {
                final Object source = event.getItemSelectable();
                final boolean selected = event.getStateChange() == ItemEvent.SELECTED;
                if (source.equals(deprecatedBox)) {
                    algoDef.getAccessFlags().setDeprecated(selected);
                }
            }
        }
    };

    public MainPanel() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents

    public void open(final RadixObject radixObject, OpenInfo info) {
        algoDef = (AdsAlgoClassDef) radixObject;

        if (!initialized) {
            JScrollPane scroller = new JScrollPane();
            JPanel inner = new JPanel();
            scroller.setViewportView(inner);
            add(scroller);
            GridBagLayout l = new GridBagLayout();
            GridBagConstraints c = new GridBagConstraints();
            inner.setLayout(l);
            c.anchor = GridBagConstraints.NORTH;
            c.weighty = 0;
            c.gridwidth = 2;
            c.fill = GridBagConstraints.BOTH;
            c.weightx = 1.0;
            c.gridy = 0;
            c.insets = new Insets(5, 5, 5, 5);
            descriptionPanel = new DescriptionPanel();
            inner.add(descriptionPanel, c);

            c.gridy = 1;
            c.insets = new Insets(0, 5, 5, 5);
            l.setConstraints(titlePanel = new LocalizingEditorPanel(), c);
            inner.add(titlePanel);

            /**
             * ***********
             */
            deprecatedBox = accessEditor.addCheckBox("Deprecated");
            deprecatedBox.addItemListener(itemListener);

            c.gridy = 2;
            c.gridheight = 1;
            c.gridwidth = 1;
            c.gridx = 0;
            c.weightx = 0;
            c.insets = new Insets(10, 10, 10, 10);
            c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.HORIZONTAL;
            l.setConstraints(accessLabel, c);
            inner.add(accessLabel);
            c.gridwidth = GridBagConstraints.REMAINDER;
            c.gridx = 1;
            c.weightx = 1.0;
            c.fill = GridBagConstraints.HORIZONTAL;
            l.setConstraints(accessEditor, c);
            inner.add(accessEditor);
            c.gridx = 0;

            /**
             * ****************
             */
            c.gridy = 3;

            c.insets = new Insets(0, 5, 5, 5);
            l.setConstraints(generalPanel = new GeneralPanel(), c);
            inner.add(generalPanel);
            c.gridy = 4;
            c.insets = new Insets(0, 5, 5, 5);

            c.fill = GridBagConstraints.BOTH;
            c.weighty = 1;
            l.setConstraints(superHierarchyPanel = new SuperClassHierarchyPanel(), c);
            inner.add(superHierarchyPanel);
            
            c.gridy++;
            initialized = true;
        }

        titlePanel.open(new HandleInfo() {

            @Override
            public Id getTitleId() {
                return algoDef.getTitleId();
            }

            @Override
            public AdsDefinition getAdsDefinition() {
                return algoDef;
            }

            @Override
            protected void onAdsMultilingualStringDefChange(IMultilingualStringDef multilingualStringDef) {
                if (multilingualStringDef != null) {
                    algoDef.setTitleId(multilingualStringDef.getId());
                }
            }

            @Override
            protected void onLanguagesPatternChange(EIsoLanguage language, String newStringValue) {
                if (getAdsMultilingualStringDef() != null) {
                    getAdsMultilingualStringDef().setValue(language, newStringValue);
                }
            }
        });
        descriptionPanel.open(algoDef);
        superHierarchyPanel.open(algoDef);
        update();

    }
    private boolean isUpdate = false;

    public void update() {
        isUpdate = true;
        try {
            final boolean readonly = algoDef.isReadOnly();
            generalPanel.update(algoDef);
            accessEditor.open(algoDef);
            deprecatedBox.setSelected(algoDef.getAccessFlags().isDeprecated());
            deprecatedBox.setEnabled(!readonly);

            titlePanel.update(new HandleInfo() {

                @Override
                public Id getTitleId() {
                    return algoDef.getTitleId();
                }

                @Override
                public AdsDefinition getAdsDefinition() {
                    return algoDef;
                }

                @Override
                protected void onAdsMultilingualStringDefChange(IMultilingualStringDef multilingualStringDef) {
                    if (multilingualStringDef != null) {
                        algoDef.setTitleId(multilingualStringDef.getId());
                    }
                }

                @Override
                protected void onLanguagesPatternChange(EIsoLanguage language, String newStringValue) {
                    if (getAdsMultilingualStringDef() != null) {
                        getAdsMultilingualStringDef().setValue(language, newStringValue);
                    }
                }
            });
            descriptionPanel.update();
            superHierarchyPanel.update();
        } finally {
            isUpdate = false;
        }
    }

    public void setReadonly(boolean readonly) {
        generalPanel.setReadonly(readonly);
        titlePanel.setReadonly(readonly);
        descriptionPanel.setReadonly(readonly);
        superHierarchyPanel.setReadonly(readonly);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
