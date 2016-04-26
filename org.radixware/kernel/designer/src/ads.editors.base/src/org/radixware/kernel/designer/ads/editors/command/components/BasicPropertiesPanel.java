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

package org.radixware.kernel.designer.ads.editors.command.components;

import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsScopeCommandDef;
import org.radixware.kernel.common.defs.ads.command.AdsCommandDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.editors.AdsDefinitionIconPresentation.IconIdChangeEvent;
import org.radixware.kernel.designer.common.dialogs.components.localizing.HandleInfo;
import org.radixware.kernel.designer.common.editors.AdsDefinitionIconPresentation;


public class BasicPropertiesPanel extends javax.swing.JPanel {

    private AdsCommandDef command;

    /**
     * Creates new form BasicPropertiesPanel
     */
    public BasicPropertiesPanel() {
        initComponents();
        titlesEditor.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        revalidate();
                    }
                });
            }
        });
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(super.getPreferredSize().width, descriptionEditor.getPreferredSize().height + Math.max(titlesEditor.getPreferredSize().height, iconHandler.getPreferredSize().height) + accessEditor.getPreferredSize().height + envSelector.getPreferredSize().height + 40);
    }

    private void setupListeners() {
        AdsDefinitionIconPresentation.IconIdStateChangeListener iconHandlerListener = new AdsDefinitionIconPresentation.IconIdStateChangeListener() {
            @Override
            public void onEvent(IconIdChangeEvent e) {
                if (!isUpdate) {
                    command.getPresentation().setIconId(e.iconId);
                }
            }
        };
        iconHandler.getIconIdChangeSupport().addEventListener(iconHandlerListener);
        chDeprecated.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                 if (command != null && !isUpdate) {
                    command.setDeprecated(chDeprecated.isSelected());
                }
            }
            
        });
    }

    public void open(final AdsCommandDef command) {
        this.command = command;
        titlesEditor.open(new HandleInfo() {
            @Override
            public AdsDefinition getAdsDefinition() {
                return command;
            }

            @Override
            public Id getTitleId() {
                return command != null ? command.getTitleId() : null;
            }

            @Override
            protected void onAdsMultilingualStringDefChange(IMultilingualStringDef multilingualStringDef) {
                if (command != null) {
                    if (multilingualStringDef != null) {
                        command.setTitleId(multilingualStringDef.getId());
                    } else {
                        command.setTitleId(null);
                    }
                }
            }

            @Override
            protected void onLanguagesPatternChange(EIsoLanguage language, String newStringValue) {
                this.getAdsMultilingualStringDef().setValue(language, newStringValue);
            }
        });
        descriptionEditor.open(command);
        envSelector.open(command);
        update();
        setupListeners();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                revalidate();
            }
        });
    }
    private boolean isUpdate = false;

    public void update() {
        isUpdate = true;
        titlesEditor.update(new HandleInfo() {
            @Override
            public AdsDefinition getAdsDefinition() {
                return command;
            }

            @Override
            public Id getTitleId() {
                Id res = command != null ? command.getTitleId() : null;
                return res;
            }

            @Override
            protected void onAdsMultilingualStringDefChange(IMultilingualStringDef multilingualStringDef) {
                if (command != null) {
                    if (multilingualStringDef != null) {
                        command.setTitleId(multilingualStringDef.getId());
                    } else {
                        command.setTitleId(null);
                    }
                }
            }

            @Override
            protected void onLanguagesPatternChange(EIsoLanguage language, String newStringValue) {
                if (this.getAdsMultilingualStringDef() != null) {
                    this.getAdsMultilingualStringDef().setValue(language, newStringValue);
                }
            }
        });
        boolean readonly = command.isReadOnly();
        boolean isScope = command instanceof AdsScopeCommandDef;
        if (isScope) {
            boolean isShowable = isScope ? ((AdsScopeCommandDef) command).getPresentation().getIsVisible() : false;
            titlesEditor.setReadonly(readonly || !isShowable);
            iconHandler.setReadonly(readonly | !isShowable);
        } else {
            titlesEditor.setReadonly(readonly);
            iconHandler.setReadonly(readonly);
        }

        this.chDeprecated.setSelected(command.isDeprecated());
        this.chDeprecated.setEnabled(!readonly);
        iconHandler.open(command, command.getPresentation().getIconId());
        descriptionEditor.update();
        accessEditor.open(command);
        envSelector.update();
        isUpdate = false;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                revalidate();
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        titlesEditor = new org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingEditorPanel();
        iconHandler = new org.radixware.kernel.designer.common.editors.AdsDefinitionIconPresentation();
        descriptionEditor = new org.radixware.kernel.designer.common.dialogs.components.DescriptionPanel();
        jLabel1 = new javax.swing.JLabel();
        accessEditor = new org.radixware.kernel.designer.ads.common.dialogs.AccessEditPanel();
        chDeprecated = new javax.swing.JCheckBox();
        envSelector = new org.radixware.kernel.designer.ads.editors.base.EnvSelectorPanel();
        jLabel2 = new javax.swing.JLabel();

        jLabel1.setText(org.openide.util.NbBundle.getMessage(BasicPropertiesPanel.class, "ScopeProperties-Common-Accessibility")); // NOI18N
        jLabel1.setAutoscrolls(true);

        accessEditor.setBorder(null);
        accessEditor.setAlignmentX(0.0F);
        accessEditor.setAutoscrolls(true);

        chDeprecated.setText(org.openide.util.NbBundle.getMessage(BasicPropertiesPanel.class, "BasicPropertiesPanel.chDeprecated.text")); // NOI18N
        chDeprecated.setAutoscrolls(true);
        chDeprecated.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chDeprecatedActionPerformed(evt);
            }
        });

        jLabel2.setText(org.openide.util.NbBundle.getMessage(BasicPropertiesPanel.class, "BasicPropertiesPanel.jLabel2.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(envSelector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(accessEditor, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chDeprecated, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(177, 177, 177))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(descriptionEditor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(iconHandler, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addComponent(titlesEditor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(71, 71, 71))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(descriptionEditor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel2)
                    .addComponent(envSelector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(chDeprecated, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(accessEditor, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(iconHandler, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(titlesEditor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(128, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {accessEditor, chDeprecated, jLabel1});

    }// </editor-fold>//GEN-END:initComponents

    private void chDeprecatedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chDeprecatedActionPerformed
    }//GEN-LAST:event_chDeprecatedActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.radixware.kernel.designer.ads.common.dialogs.AccessEditPanel accessEditor;
    private javax.swing.JCheckBox chDeprecated;
    private org.radixware.kernel.designer.common.dialogs.components.DescriptionPanel descriptionEditor;
    private org.radixware.kernel.designer.ads.editors.base.EnvSelectorPanel envSelector;
    private org.radixware.kernel.designer.common.editors.AdsDefinitionIconPresentation iconHandler;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingEditorPanel titlesEditor;
    // End of variables declaration//GEN-END:variables
}
