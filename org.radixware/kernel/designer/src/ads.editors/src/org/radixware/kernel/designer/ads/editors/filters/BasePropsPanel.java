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
 * BasePropsPanel.java
 *
 * Created on Feb 19, 2010, 11:42:42 AM
 */
package org.radixware.kernel.designer.ads.editors.filters;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsFilterDef;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.components.localizing.HandleInfo;
import org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingEditorPanel;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;


class BasePropsPanel extends javax.swing.JPanel {

    private HandleInfo handleInfo = null;
    private LocalizingEditorPanel localizingPaneList;

    /** Creates new form BasePropsPanel */
    public BasePropsPanel() {
        initComponents();
        localizingPaneList = (LocalizingEditorPanel) titleEditorPanel;
        this.handleInfo = new HandleInfo() {

            @Override
            public AdsDefinition getAdsDefinition() {
                return getFilter();
            }

            @Override
            public Id getTitleId() {
                return getFilter().getTitleId();
            }

            @Override
            public void onAdsMultilingualStringDefChange(IMultilingualStringDef multilingualStringDef) {
                if (multilingualStringDef != null) {
                    getFilter().setTitleId(multilingualStringDef.getId());
                } else {
                    getFilter().setTitleId(null);
                }
            }

            @Override
            protected void onLanguagesPatternChange(EIsoLanguage language, String newStringValue) {
                getFilter().setTitle(language, newStringValue);
            }
        };
    }

    private AdsFilterDef getFilter() {
        return filterDef;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        descriptionPanel = new org.radixware.kernel.designer.common.dialogs.components.DescriptionPanel();
        chUseCustomView = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        accessPanel = new org.radixware.kernel.designer.ads.common.dialogs.AccessEditPanel();
        envSelectorPanel = new org.radixware.kernel.designer.ads.editors.base.EnvSelectorPanel();
        chUseCustomViewWeb = new javax.swing.JCheckBox();
        titleEditorPanel = new LocalizingEditorPanel()
        ;

        chUseCustomView.setText(org.openide.util.NbBundle.getMessage(BasePropsPanel.class, "BasePropsPanel.chUseCustomView.text")); // NOI18N

        jLabel1.setText(org.openide.util.NbBundle.getMessage(BasePropsPanel.class, "AccessibilityTip")); // NOI18N

        chUseCustomViewWeb.setText(org.openide.util.NbBundle.getMessage(BasePropsPanel.class, "BasePropsPanel.chUseCustomViewWeb.text")); // NOI18N

        javax.swing.GroupLayout titleEditorPanelLayout = new javax.swing.GroupLayout(titleEditorPanel);
        titleEditorPanel.setLayout(titleEditorPanelLayout);
        titleEditorPanelLayout.setHorizontalGroup(
            titleEditorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        titleEditorPanelLayout.setVerticalGroup(
            titleEditorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(titleEditorPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(descriptionPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(accessPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(envSelectorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(chUseCustomView)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chUseCustomViewWeb)
                        .addGap(0, 341, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(descriptionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(accessPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1))
                    .addComponent(envSelectorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(titleEditorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chUseCustomView)
                    .addComponent(chUseCustomViewWeb))
                .addContainerGap(12, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.radixware.kernel.designer.ads.common.dialogs.AccessEditPanel accessPanel;
    private javax.swing.JCheckBox chUseCustomView;
    private javax.swing.JCheckBox chUseCustomViewWeb;
    private org.radixware.kernel.designer.common.dialogs.components.DescriptionPanel descriptionPanel;
    private org.radixware.kernel.designer.ads.editors.base.EnvSelectorPanel envSelectorPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel titleEditorPanel;
    // End of variables declaration//GEN-END:variables
    private transient AdsFilterDef filterDef = null;

    void open(AdsFilterDef filter) {
        filterDef = filter;
        descriptionPanel.open(filter);
        localizingPaneList.open(handleInfo);
        envSelectorPanel.open(filter);
        update();
    }
    private final ActionListener chUseCustomViewListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent ae) {
            if (!chUseCustomView.isSelected() && getFilter().getCustomViewSupport().isUseCustomView(ERuntimeEnvironmentType.EXPLORER)) {
                if (!DialogUtils.messageConfirmation("Delete custom view for filter " + getFilter().getQualifiedName() + "?")) {
                    chUseCustomView.setSelected(true);
                    return;
                }
            }
            getFilter().getCustomViewSupport().setUseCustomView(ERuntimeEnvironmentType.EXPLORER, chUseCustomView.isSelected());
        }
    };
    private final ActionListener chUseCustomViewWebListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent ae) {
            if (!chUseCustomViewWeb.isSelected() && getFilter().getCustomViewSupport().isUseCustomView(ERuntimeEnvironmentType.WEB)) {
                if (!DialogUtils.messageConfirmation("Delete custom WEB view for filter " + getFilter().getQualifiedName() + "?")) {
                    chUseCustomViewWeb.setSelected(true);
                    return;
                }
            }
            getFilter().getCustomViewSupport().setUseCustomView(ERuntimeEnvironmentType.WEB, chUseCustomViewWeb.isSelected());
        }
    };

    void update() {
        accessPanel.open(filterDef);

        chUseCustomView.removeActionListener(chUseCustomViewListener);
        chUseCustomViewWeb.removeActionListener(chUseCustomViewWebListener);
        descriptionPanel.update();
        localizingPaneList.update(handleInfo);
        chUseCustomView.setSelected(getFilter().getCustomViewSupport().isUseCustomView(ERuntimeEnvironmentType.EXPLORER));
        chUseCustomView.addActionListener(chUseCustomViewListener);

        chUseCustomViewWeb.setSelected(getFilter().getCustomViewSupport().isUseCustomView(ERuntimeEnvironmentType.WEB));
        chUseCustomViewWeb.addActionListener(chUseCustomViewWebListener);

        envSelectorPanel.update();
        setReadonly(getFilter().isReadOnly());
    }

    public void setReadonly(boolean readonly) {
        if (!readonly) {
            //try to enable
            if (!getFilter().isReadOnly()) {
                descriptionPanel.setReadonly(false);
                localizingPaneList.setReadonly(false);
                chUseCustomView.setEnabled(filterDef.getClientEnvironment() != ERuntimeEnvironmentType.WEB);
                chUseCustomViewWeb.setEnabled(filterDef.getClientEnvironment() != ERuntimeEnvironmentType.EXPLORER);
                envSelectorPanel.setEnabled(true);
            }
        } else {
            descriptionPanel.setReadonly(true);
            localizingPaneList.setReadonly(true);
            chUseCustomView.setEnabled(false);
            chUseCustomViewWeb.setEnabled(false);

            envSelectorPanel.setEnabled(false);
        }
    }
}
