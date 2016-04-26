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

package org.radixware.kernel.designer.ads.editors.sortings;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JSplitPane;
import javax.swing.border.TitledBorder;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSortingDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.ads.common.dialogs.AccessEditPanel;
import org.radixware.kernel.designer.common.dialogs.components.DescriptionPanel;
import org.radixware.kernel.designer.common.dialogs.components.RadixObjectChooserPanel;
import org.radixware.kernel.designer.common.dialogs.components.localizing.HandleInfo;
import org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingEditorPanel;
import org.radixware.kernel.designer.common.editors.sqml.SqmlEditorPanel;


public class AdsSortingEditorPanel extends javax.swing.JPanel {

    private AdsSortingDef sorting;

    private DescriptionPanel descriptionEditor = new DescriptionPanel();
    private AccessEditPanel accessPanel = new AccessEditPanel();
    private LocalizingEditorPanel titleEditor = new LocalizingEditorPanel();

    private JSplitPane splitPane;
    private ComponentListener splitAdapter = new ComponentListener() {

        @Override
        public void componentShown(ComponentEvent e) {
            AdsSortingEditorPanel.this.splitPane.setDividerLocation(0.80f);
        }

        @Override
        public void componentResized(ComponentEvent e) {
//            AdsSortingEditorPanel.this.splitPane.setDividerLocation(0.80f);
        }

        @Override
        public void componentMoved(ComponentEvent e) {
        }

        @Override
        public void componentHidden(ComponentEvent e) {
        }
    };
    private RadixObjectChooserPanel chooser = new RadixObjectChooserPanel();
    private SqmlEditorPanel hintEditor = new SqmlEditorPanel();

    public AdsSortingEditorPanel(){
        hintEditor.setBorder(new TitledBorder(NbBundle.getMessage(AdsSortingEditorPanel.class, "AdsSortingPanel-HintTitle")));
        Dimension defaultSize = hintEditor.getPreferredSize();

        float hfl = defaultSize.height * 1.5F;
        int h = Math.round(hfl);
        hintEditor.setPreferredSize(new Dimension(defaultSize.width, h));

        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        setLayout(gbl);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 10, 10, 10);
        c.weightx = 1.0;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        gbl.setConstraints(descriptionEditor, c);
        add(descriptionEditor);

        c.gridwidth = 1;
        c.gridy = 1;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0.0;
        c.insets = new Insets(0, 10, 10, 10);
        javax.swing.JLabel label = new javax.swing.JLabel(NbBundle.getMessage(AdsSortingEditorPanel.class, "AccessibilityTip"));
        gbl.setConstraints(label, c);
        add(label);

        c.gridx = 1;
        c.insets = new Insets(0, 0, 10, 10);
        gbl.setConstraints(accessPanel, c);
        add(accessPanel);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;

        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        c.insets = new Insets(0, 10, 10, 10);
        gbl.setConstraints(titleEditor, c);
        add(titleEditor);

        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, chooser, hintEditor);
        splitPane.setBorder(null);
        splitPane.setContinuousLayout(true);

        c.gridy = 3;
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1.0;
        gbl.setConstraints(splitPane, c);
        add(splitPane);

        splitPane.addComponentListener(splitAdapter);
    }

    private HandleInfo handleInfo;
    public void open(final AdsSortingDef sorting){
        this.sorting = sorting;

        handleInfo = new HandleInfo() {

            @Override
            public AdsDefinition getAdsDefinition() {
                return sorting;
            }

            @Override
            public Id getTitleId() {
                return sorting.getTitleId();
            }

            @Override
            public void onAdsMultilingualStringDefChange(IMultilingualStringDef multilingualStringDef) {

                if (multilingualStringDef != null) {
                    sorting.setTitleId(multilingualStringDef.getId());
                } else {
                    sorting.setTitleId(null);
                }
            }

            @Override
            protected void onLanguagesPatternChange(EIsoLanguage language, String newStringValue) {
                getAdsMultilingualStringDef().setValue(language, newStringValue);
            }
        };

        chooser.open(new UsedAdsSortingDefPropertiesTable(sorting), new AvailableAdsSortingDefPropertiesList(sorting));

        update();
    }

    public void update(){
        final boolean readonly = sorting.isReadOnly();

        accessPanel.open(sorting);

        titleEditor.update(handleInfo);
        titleEditor.setReadonly(readonly);

        descriptionEditor.open(sorting);
        descriptionEditor.setReadonly(readonly);

        chooser.update();
        chooser.setReadonly(readonly);

        hintEditor.open(sorting.getHint());
        hintEditor.setEnabled(!readonly);
    }

}
