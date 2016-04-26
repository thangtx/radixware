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
 * ExplorerItemsCommonPanel.java
 *
 * Created on Apr 15, 2009, 1:17:14 PM
 */
package org.radixware.kernel.designer.ads.editors.exploreritems;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphExplorerItemDef;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.ads.editors.base.EnvSelectorPanel;
import org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel;
import org.radixware.kernel.designer.common.editors.AdsDefinitionIconPresentation;
import org.radixware.kernel.designer.common.dialogs.components.localizing.HandleInfo;
import org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingEditorPanel;
import org.radixware.kernel.designer.common.editors.AdsDefinitionIconPresentation.IconIdChangeEvent;
import org.radixware.kernel.designer.common.editors.AdsLogoPresentation;
import org.radixware.kernel.designer.common.editors.AdsLogoPresentation.LogoIdChangeEvent;

public class ParagraphItemCommonPanel extends javax.swing.JPanel {

    private AdsParagraphExplorerItemDef paragraph;
    private LocalizingEditorPanel titlesPane = new LocalizingEditorPanel();
    private AdsDefinitionIconPresentation iconHandler = new AdsDefinitionIconPresentation();
    private AdsLogoPresentation logoHandler = new AdsLogoPresentation();
    private AccessibilityPanel accessPanel = new AccessibilityPanel();
    private JPanel commonPane = new JPanel();
    private JCheckBox cbInheritChildren;
    private JPanel propPane = new JPanel();
    private EnvSelectorPanel envSelector = new EnvSelectorPanel();
    private UsedContextlessCommandsListView usedContextlessCommands = new UsedContextlessCommandsListView();
    private javax.swing.JLabel overlabel = new javax.swing.JLabel(NbBundle.getMessage(ParagraphItemCommonPanel.class, "OverridesTip"));
    private javax.swing.JLabel ovrlabel = new javax.swing.JLabel(NbBundle.getMessage(ParagraphItemCommonPanel.class, "OverwritesTip"));
    private DefinitionLinkEditPanel overEditor = new DefinitionLinkEditPanel();
    private DefinitionLinkEditPanel ovrEditor = new DefinitionLinkEditPanel();

    /**
     * Creates new form ExplorerItemsCommonPanel
     */
    public ParagraphItemCommonPanel() {
        initComponents();
        setLayout(new BorderLayout());
        add(commonPane, BorderLayout.NORTH);
        propPane.setLayout(new BoxLayout(propPane, BoxLayout.Y_AXIS));
        add(propPane, BorderLayout.CENTER);

        logoHandler.getLogoIdChangeSupport().addEventListener(new AdsLogoPresentation.LogoIdStateChangeListener() {

            @Override
            public void onEvent(LogoIdChangeEvent e) {
                if (!ParagraphItemCommonPanel.this.isUpdate) {
                    ParagraphItemCommonPanel.this.paragraph.setLogoId(e.logoId);
                }
            }
        });

        iconHandler.getIconIdChangeSupport().addEventListener(new AdsDefinitionIconPresentation.IconIdStateChangeListener() {

            @Override
            public void onEvent(IconIdChangeEvent e) {
                if (!ParagraphItemCommonPanel.this.isUpdate) {
                    ParagraphItemCommonPanel.this.paragraph.setIconId(e.iconId);
                }
            }
        });
    }

    private void setupCommonPaneLook() {
        GridBagLayout commonGbl = new GridBagLayout();
        GridBagConstraints commonConst = new GridBagConstraints();

        commonPane.setLayout(commonGbl);
        commonConst.anchor = GridBagConstraints.NORTH;
        commonConst.fill = GridBagConstraints.HORIZONTAL;
        commonConst.weightx = 1.0;
        commonConst.weighty = 0.0;
        commonConst.gridx = 0;
        commonConst.gridy = 0;
        commonConst.gridwidth = 1;

        commonConst.insets = new Insets(10, 10, 0, 10);
        commonGbl.setConstraints(titlesPane, commonConst);
        commonPane.add(titlesPane);

        commonConst.insets = new Insets(10, 0, 0, 10);
        commonConst.gridx = 1;
        commonConst.gridy = 0;
        commonConst.weightx = 0.0;
        commonGbl.setConstraints(iconHandler, commonConst);
        commonPane.add(iconHandler);

        commonConst.anchor = GridBagConstraints.WEST;
        commonConst.gridx = 0;
        commonConst.gridy = 2;
        commonConst.gridwidth = 2;
        commonGbl.setConstraints(accessPanel, commonConst);
        commonPane.add(accessPanel);
    }

    private void setupParagraphPropListeners() {
        ActionListener customListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!ParagraphItemCommonPanel.this.isUpdate) {
                    AdsParagraphExplorerItemDef item = ParagraphItemCommonPanel.this.paragraph;
                    boolean isSelected = ParagraphItemCommonPanel.this.customViewCheck.isSelected();
                    item.getCustomViewSupport().setUseCustomView(ERuntimeEnvironmentType.EXPLORER, isSelected);

                    isSelected = ParagraphItemCommonPanel.this.webCustomViewCheck.isSelected();
                    item.getCustomViewSupport().setUseCustomView(ERuntimeEnvironmentType.WEB, isSelected);
                }
            }
        };
        customViewCheck.addActionListener(customListener);
        webCustomViewCheck.addActionListener(customListener);

    }
    private JCheckBox rootCheck;
    private JCheckBox customViewCheck;
    private JCheckBox webCustomViewCheck;

    private void setupPropPaneLook() {
        JPanel propContent = new JPanel();
        customViewCheck = new JCheckBox();
        int w = customViewCheck.getPreferredSize().width;
        customViewCheck.setText(NbBundle.getMessage(ParagraphItemCommonPanel.class, "ParagraphItem-CustomViewTip"));
        webCustomViewCheck = new JCheckBox();
        webCustomViewCheck.setText(NbBundle.getMessage(ParagraphItemCommonPanel.class, "ParagraphItem-WebCustomViewTip"));
        GridBagLayout paragraphGbl = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        propPane.setLayout(new BorderLayout());
        propContent.setLayout(paragraphGbl);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 0.0;

        AdsExplorerItemDef overriden = paragraph.getHierarchy().findOverridden().get();
        if (overriden != null) {
            overEditor.open(overriden, overriden.getId());

            javax.swing.JPanel overPanel = new javax.swing.JPanel();
            GridBagLayout overGBL = new GridBagLayout();
            GridBagConstraints overC = new GridBagConstraints();
            overPanel.setLayout(overGBL);

            overC.fill = GridBagConstraints.HORIZONTAL;
            overGBL.setConstraints(overlabel, overC);
            overPanel.add(overlabel);

            overC.insets = new Insets(0, 10, 0, 0);
            overC.gridx = 1;
            overC.weightx = 1.0;

            overGBL.setConstraints(overEditor, overC);
            overPanel.add(overEditor);
            c.gridwidth = 2;
            c.insets = new Insets(10, 10, 0, 10);
            paragraphGbl.setConstraints(overPanel, c);
            propContent.add(overPanel);
            c.gridwidth = 1;
            c.gridy++;
        }
        AdsExplorerItemDef overwritten = paragraph.getHierarchy().findOverwritten().get();
        if (overwritten != null) {
            ovrEditor.open(overwritten, overwritten.getId());

            javax.swing.JPanel overPanel = new javax.swing.JPanel();
            GridBagLayout overGBL = new GridBagLayout();
            GridBagConstraints overC = new GridBagConstraints();
            overPanel.setLayout(overGBL);

            overC.fill = GridBagConstraints.HORIZONTAL;
            overGBL.setConstraints(ovrlabel, overC);
            overPanel.add(ovrlabel);

            overC.insets = new Insets(0, 10, 0, 0);
            overC.gridx = 1;

            overC.weightx = 1.0;
            overGBL.setConstraints(ovrEditor, overC);
            overPanel.add(ovrEditor);
            c.gridwidth = 2;
            c.insets = new Insets(10, 10, 0, 10);
            paragraphGbl.setConstraints(overPanel, c);
            propContent.add(overPanel);
            c.gridwidth = 1;
            c.gridy++;
        }

        if (paragraph.isTopLevelDefinition()) {
            rootCheck = new JCheckBox();
            rootCheck.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!ParagraphItemCommonPanel.this.isUpdate) {
                        boolean isSelected = ParagraphItemCommonPanel.this.rootCheck.isSelected();
                        ParagraphItemCommonPanel.this.paragraph.setRoot(isSelected);
                    }
                }
            });
            rootCheck.setText(NbBundle.getMessage(ParagraphItemCommonPanel.class, "ParagraphItem-RootTip"));
            c.insets = new Insets(10, 10, 0, 10);
            paragraphGbl.setConstraints(rootCheck, c);
            rootCheck.setEnabled(!readonly);
            rootCheck.setSelected(paragraph.isRoot());
            propContent.add(rootCheck);

        }
        if (!paragraph.getHierarchy().findOverwritten().isEmpty()) {
            cbInheritChildren = new JCheckBox();
            cbInheritChildren.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!isUpdate) {
                        paragraph.setExplorerItemsInherited(cbInheritChildren.isSelected());
                    }
                }
            });
            c.gridx++;
            paragraphGbl.setConstraints(cbInheritChildren, c);
            cbInheritChildren.setText("Explorer Items Inherited");
            cbInheritChildren.setEnabled(!readonly);
            cbInheritChildren.setSelected(paragraph.isExplorerItemsInherited());
            propContent.add(cbInheritChildren);
            c.gridx--;
            c.gridwidth++;
        }
        c.gridy++;

        c.insets = new Insets(10, 10, 10, 10);
        paragraphGbl.setConstraints(envSelector, c);
        propContent.add(envSelector);

        c.gridy++;
        c.insets = new Insets(10, 10, 10, 10);
        paragraphGbl.setConstraints(customViewCheck, c);
        propContent.add(customViewCheck);
        c.gridy++;
        paragraphGbl.setConstraints(webCustomViewCheck, c);
        propContent.add(webCustomViewCheck);

        c.insets = new Insets(0, 0, 0, 0);
        c.gridy++;
        c.fill = GridBagConstraints.HORIZONTAL;
        paragraphGbl.setConstraints(usedContextlessCommands, c);
        propContent.add(usedContextlessCommands);

        c.insets = new Insets(0, 10, 0, 10);
        c.gridy++;
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1.0;
        c.insets = new Insets(0, 10, 10, 10);
        paragraphGbl.setConstraints(logoHandler, c);
        propContent.add(logoHandler);

        propPane.add(propContent, BorderLayout.CENTER);

        customViewCheck.setSelected(paragraph.getCustomViewSupport().isUseCustomView(ERuntimeEnvironmentType.EXPLORER));
        customViewCheck.setEnabled(!readonly);

        webCustomViewCheck.setSelected(paragraph.getCustomViewSupport().isUseCustomView(ERuntimeEnvironmentType.WEB));
        webCustomViewCheck.setEnabled(!readonly);

        setupParagraphPropListeners();
    }

    private void updatePanelLook() {
        commonPane.removeAll();
        propPane.removeAll();
        setupCommonPaneLook();
        setupPropPaneLook();
    }

    private void updateTitlesEditor() {
        titlesPane.open(new HandleInfo() {

            @Override
            public AdsDefinition getAdsDefinition() {
                return paragraph;
            }

            @Override
            public Id getTitleId() {
                return paragraph != null ? paragraph.getTitleId() : null;
            }

            @Override
            protected void onAdsMultilingualStringDefChange(IMultilingualStringDef multilingualStringDef) {
                if (paragraph != null) {
                    if (multilingualStringDef != null) {
                        paragraph.setTitleId(multilingualStringDef.getId());
                    } else {
                        paragraph.setTitleId(null);
                    }
                }
            }

            @Override
            protected void onLanguagesPatternChange(EIsoLanguage language, String newStringValue) {
                this.getAdsMultilingualStringDef().setValue(language, newStringValue);
            }
        });
    }
    private boolean isUpdate = false;
    private boolean readonly = false;

    public void open(final AdsParagraphExplorerItemDef paragraph) {
        this.paragraph = paragraph;
        this.readonly = paragraph.isReadOnly();
        isUpdate = true;
        updateTitlesEditor();
        titlesPane.setReadonly(readonly);

        iconHandler.open(paragraph, paragraph.getIconId());
        iconHandler.setReadonly(readonly);
        logoHandler.open(paragraph, paragraph.getLogoId());
        logoHandler.setReadonly(readonly);
        usedContextlessCommands.open(paragraph);
        updatePanelLook();

        accessPanel.open(paragraph);
        envSelector.open(paragraph);
        isUpdate = false;
    }

    public void update() {
        isUpdate = true;
        readonly = paragraph.isReadOnly();
        updateTitlesEditor();
        titlesPane.setReadonly(readonly);

        iconHandler.update();
        iconHandler.setReadonly(readonly);

        logoHandler.update();
        logoHandler.setReadonly(readonly);

        boolean usecustom = paragraph.getCustomViewSupport().isUseCustomView(ERuntimeEnvironmentType.EXPLORER);
        customViewCheck.setSelected(usecustom);
        customViewCheck.setEnabled(!readonly);

        usecustom = paragraph.getCustomViewSupport().isUseCustomView(ERuntimeEnvironmentType.WEB);
        webCustomViewCheck.setSelected(usecustom);
        webCustomViewCheck.setEnabled(!readonly);

        usedContextlessCommands.update();

        if (rootCheck != null) {
            rootCheck.setEnabled(!readonly);
            rootCheck.setSelected(paragraph.isRoot());
        }
        AdsExplorerItemDef overriden = paragraph.getHierarchy().findOverridden().get();
        if (overriden != null) {
            overEditor.open(overriden, overriden.getId());
        } else {
            if (overEditor.isShowing()) {
                overEditor.open(null, null);
            }
        }
        envSelector.open(paragraph);
        accessPanel.update();
        isUpdate = false;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
