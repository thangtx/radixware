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

package org.radixware.kernel.designer.ads.editors.pages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.border.CompoundBorder;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPageDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsSelectorExplorerItemDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EEditorPageType;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.components.ComponentTitledBorder;
import org.radixware.kernel.designer.common.dialogs.components.InheritableTitlePanel;
import org.radixware.kernel.designer.common.dialogs.components.localizing.HandleInfo;
import org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingEditorPanel;
import org.radixware.kernel.designer.common.editors.AdsDefinitionIconPresentation;
import org.radixware.kernel.designer.common.editors.AdsDefinitionIconPresentation.IconIdChangeEvent;


public class TitleAndIconPanel extends javax.swing.JPanel {

    private AdsEditorPageDef page;
    private boolean isContainer = false;

    public TitleAndIconPanel() {
        super();
    }
    private LocalizingEditorPanel titleEditor;
    private InheritableTitlePanel inheritableTitleEditor;
    private javax.swing.JCheckBox inheritIcon;
    private AdsDefinitionIconPresentation iconEditor;

    private void setupLook() {
        removeAll();
        setLayout(new BorderLayout());
        javax.swing.JPanel content = new javax.swing.JPanel();
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        content.setLayout(gbl);
        iconEditor = new AdsDefinitionIconPresentation();
        iconEditor.getIconIdChangeSupport().addEventListener(new AdsDefinitionIconPresentation.IconIdStateChangeListener() {

            @Override
            public void onEvent(IconIdChangeEvent e) {
                TitleAndIconPanel.this.page.setIconId(e.iconId);
            }
        });

        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTH;
        c.weightx = 1.0;

        if (isContainer) {
            c.insets = new Insets(0, 0, 0, 0);
            inheritableTitleEditor = new InheritableTitlePanel();

            gbl.setConstraints(inheritableTitleEditor, c);
            content.add(inheritableTitleEditor);
            if (page.findEmbeddedExplorerItem() instanceof AdsSelectorExplorerItemDef) {

                c.insets = new Insets(0, 0, 0, 0);
                c.gridx = 1;
                c.weightx = 0.0;

                AdsExplorerItemDef reference = page.findEmbeddedExplorerItem();
                if (reference != null) {
                    inheritIcon = new javax.swing.JCheckBox(NbBundle.getMessage(TitleAndIconPanel.class, "InheritIconTip"));
                    Id currentIconId = page.getIconId();
                    Id refIconId = null;
                    AdsSelectorPresentationDef refref = ((AdsSelectorExplorerItemDef) reference).findReferencedSelectorPresentation().get();
                    if (refref != null) {
                        refIconId = refref.getIconId();
                    }
                    inheritIcon.setSelected((currentIconId != null && refIconId != null && currentIconId.equals(refIconId))
                            || (refIconId == null && currentIconId == null));
                    inheritIcon.setEnabled(!page.isReadOnly()
                            && reference instanceof AdsSelectorExplorerItemDef);
                    iconEditor.open(page, currentIconId);
                    iconEditor.setReadonly(inheritIcon.isSelected() || page.isReadOnly());
                    inheritIcon.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            boolean isSelected = inheritIcon.isSelected();
                            if (isSelected) {
                                Id newIconId = null;
                                AdsSelectorExplorerItemDef pRef = (AdsSelectorExplorerItemDef) page.findEmbeddedExplorerItem();
                                if (pRef != null) {
                                    AdsSelectorPresentationDef refPresentation = pRef.findReferencedSelectorPresentation().get();
                                    if (refPresentation != null) {
                                        newIconId = refPresentation.getIconId();
                                    }
                                }
                                page.setIconId(newIconId);
                                iconEditor.open(page, newIconId);
                                iconEditor.setReadonly(true);
                            } else {
//                            if (!iconEditor.chooseNewIcon()) {
//                                inheritIcon.setSelected(true);
//                                iconEditor.setReadonly(true);
//                            } else {
                                iconEditor.setReadonly(false);
                                page.setIconId(null);
                                iconEditor.open(page, page.getIconId());
//                            }
                            }
                        }
                    });
                    CompoundBorder iBorder = new CompoundBorder(BorderFactory.createEtchedBorder(), BorderFactory.createEmptyBorder(0, 10, 10, 10));
                    ComponentTitledBorder iconsBorder = new ComponentTitledBorder(inheritIcon, iconEditor, iBorder);
                    iconEditor.setBorder(iconsBorder);
                } else {
                    iconEditor.open(page, page.getIconId());
                }
                gbl.setConstraints(iconEditor, c);
                content.add(iconEditor);
            }
        } else {
            titleEditor = new LocalizingEditorPanel();
            c.insets = new Insets(0, 0, 0, 0);
            gbl.setConstraints(titleEditor, c);
            content.add(titleEditor);
            
            c.insets = new Insets(0, 0, 0, 0);
            c.gridx = 1;
            c.weightx = 0.0;
            c.anchor = GridBagConstraints.CENTER;
            gbl.setConstraints(iconEditor, c);
            content.add(iconEditor);
        }

        add(content, BorderLayout.NORTH);
    }

    public boolean isIconInherited() {
        return inheritIcon != null && inheritIcon.isSelected();
    }

    public void open(final AdsEditorPageDef page) {
        this.page = page;
        this.isContainer = page.getType().equals(EEditorPageType.CONTAINER);
        boolean readonly = page.isReadOnly();
        setupLook();
        if (!isContainer || !(page.findEmbeddedExplorerItem() instanceof AdsSelectorExplorerItemDef)) {
            iconEditor.open(page, page.getIconId());
        }
        if (isContainer) {
            inheritableTitleEditor.open(page);
        } else {
            HandleInfo handleInfo = new HandleInfo() {

                @Override
                public AdsDefinition getAdsDefinition() {
                    return page;
                }

                @Override
                public Id getTitleId() {
                    return page.getTitleId();
                }

                @Override
                protected void onAdsMultilingualStringDefChange(IMultilingualStringDef multilingualStringDef) {
                    if (multilingualStringDef != null) {
                        page.setTitleId(multilingualStringDef.getId());
                    } else {
                        page.setTitleId(null);
                    }
                }

                @Override
                protected void onLanguagesPatternChange(EIsoLanguage language, String newStringValue) {
                    getAdsMultilingualStringDef().setValue(language, newStringValue);
                }
            };
            titleEditor.open(handleInfo);
            titleEditor.setReadonly(readonly);
        }
    }
}
