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
 * TitleModalEditor.java
 *
 * Created on Aug 12, 2009, 3:47:23 PM
 */
package org.radixware.kernel.designer.common.editors.editmask;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JComponent;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskList;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.components.localizing.HandleInfo;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;

class TitleModalEditor extends TopComponent {

    public interface TitleProvider {

        Id getTitleId();

        void setTitleId(Id id);

        String getTitle();

        void setTitle(String title);
    }

    private class TitleHandleInfo extends HandleInfo {

        @Override
        public AdsDefinition getAdsDefinition() {
            return definition;
        }

        @Override
        public Id getTitleId() {
            return null;
        }

        @Override
        public AdsMultilingualStringDef getAdsMultilingualStringDef() {
            return stringDefCopy;
        }

        @Override
        public void setAdsMultilingualStringDef(IMultilingualStringDef adsMultilingualStringDef) {
        }

        @Override
        public void removeAdsMultilingualStringDef() {
        }

        @Override
        protected boolean isBundleResettable() {
            return false;
        }

        @Override
        public void onLanguagesPatternChange(EIsoLanguage language, String newStringValue) {
//            strForLang.put(language, newStringValue);
            getAdsMultilingualStringDef().setValue(language, newStringValue);
        }
    }
    private final TitleProvider item;
    private final AdsMultilingualStringDef stringDef;
    private final AdsMultilingualStringDef stringDefCopy;
    private final AdsDefinition definition;
    private JComponent currentComponent = null;
    private volatile boolean updating = false;

    /**
     * Creates new form TitleModalEditor
     */
    protected TitleModalEditor(AdsDefinition definition, AdsMultilingualStringDef stringDef, TitleProvider item) {
        super();
        this.stringDef = stringDef;
        this.item = item;
        this.definition = definition;
        stringDefCopy = AdsMultilingualStringDef.Factory.newInstance(stringDef);
        initComponents();

        this.setMaximumSize(new Dimension(640, 160));
        this.setMinimumSize(new Dimension(240, 160));
        this.setPreferredSize(new Dimension(240, 160));

        updating = true;
        if (item.getTitleId() != null) {
//            stringDef = definition.findLocalizedString(item.getTitleId());
//            assert stringDef != null;
            translationCheckBox.setSelected(true);
            setupTitleId();
        } else {
            translationCheckBox.setSelected(false);
            setupTitle();
        }
        updating = false;

    }

    private void initTitle() {
        titleTextField.setText(stringDefCopy.getValue(EIsoLanguage.ENGLISH));
    }

    private void initStrDef() {
//        for (EIsoLanguage lang : EIsoLanguage.values())
        stringDefCopy.setValue(EIsoLanguage.ENGLISH, titleTextField.getText());
    }

    private void setupTitle() {
        updating = true;
        if (currentComponent != null) {
            currentComponent.setVisible(false);
            panel.getLayout().removeLayoutComponent(currentComponent);
        }
//        if (item.getTitleId() != null) {
//            definition.findLocalizingBundle().getStrings().getLocal().remove(stringDef);
//            item.setTitleId(null);
//        }
        currentComponent = titlePanel;
        panel.add(currentComponent, BorderLayout.CENTER);
        titleTextField.setText(item.getTitle());
        currentComponent.setVisible(true);
        updating = false;
    }

    private void setupTitleId() {
        updating = true;
        if (currentComponent != null) {
            currentComponent.setVisible(false);
            panel.getLayout().removeLayoutComponent(currentComponent);
        }
//        item.setTitle("");
//        if (item.getTitleId() == null) {
//            if (stringDef == null)
//                stringDef = AdsMultilingualStringDef.Factory.newInstance();
//            definition.findLocalizingBundle().getStrings().getLocal().add(stringDef);
//            item.setTitleId(stringDef.getId());
//        }
        currentComponent = localizingPaneList;
        panel.add(currentComponent, BorderLayout.CENTER);
        localizingPaneList.open(new TitleHandleInfo());
        currentComponent.setVisible(true);
        updating = false;
    }

    private class TitleModalDisplayer extends ModalDisplayer {

        public TitleModalDisplayer(TitleModalEditor editor) {
            super(editor);
            getDialog().setMaximumSize(new Dimension(640, 160));
            getDialog().setMinimumSize(new Dimension(240, 160));
            getDialog().setPreferredSize(new Dimension(240, 160));
        }

//        @Override
//        public Object[] getOptions() {
//            return new Object[]{DialogDescriptor.CLOSED_OPTION};
//        }
        @Override
        protected void apply() {
            if (translationCheckBox.isSelected()) {
                for (EIsoLanguage lang : stringDefCopy.getLanguages()) {
                    stringDef.setValue(lang, stringDefCopy.getValue(lang));
                }
                item.setTitleId(stringDef.getId());
            } else {
                item.setTitle(titleTextField.getText());
                item.setTitleId(null);
            }
        }
    }

    @Override
    public String getDisplayName() {
        return NbBundle.getBundle(TitleModalEditor.class).getString("Title_Editor");
    }

    @Override
    public void open() {
        TitleModalDisplayer modalDisplayer = new TitleModalDisplayer(this);
        modalDisplayer.showModal();
    }

    public static final class Factory {

        public static TitleModalEditor newInstance(AdsDefinition definition, AdsMultilingualStringDef stringDef, TitleProvider item) {
            return new TitleModalEditor(definition, stringDef, item);
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

        titlePanel = new javax.swing.JPanel();
        titleTextField = new javax.swing.JTextField();
        localizingPaneList = new org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingEditorPanel();
        translationCheckBox = new javax.swing.JCheckBox();
        panel = new javax.swing.JPanel();

        titleTextField.setText(org.openide.util.NbBundle.getMessage(TitleModalEditor.class, "TitleModalEditor.titleTextField.text")); // NOI18N
        titleTextField.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                titleTextFieldCaretUpdate(evt);
            }
        });

        javax.swing.GroupLayout titlePanelLayout = new javax.swing.GroupLayout(titlePanel);
        titlePanel.setLayout(titlePanelLayout);
        titlePanelLayout.setHorizontalGroup(
            titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(titlePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
                .addContainerGap())
        );
        titlePanelLayout.setVerticalGroup(
            titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(titlePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        translationCheckBox.setText(org.openide.util.NbBundle.getMessage(TitleModalEditor.class, "TitleModalEditor.translationCheckBox.text")); // NOI18N
        translationCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                translationCheckBoxItemStateChanged(evt);
            }
        });

        panel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(TitleModalEditor.class, "TitleModalEditor.panel.border.title"))); // NOI18N
        panel.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE)
                    .addComponent(translationCheckBox))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(translationCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void translationCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_translationCheckBoxItemStateChanged
        if (updating) {
            return;
        }
        if (translationCheckBox.isSelected()) {
            initStrDef();
            setupTitleId();
        } else {
            setupTitle();
            initTitle();
        }
    }//GEN-LAST:event_translationCheckBoxItemStateChanged

    private void titleTextFieldCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_titleTextFieldCaretUpdate
//        if (!updating)
//            item.setTitle(titleTextField.getText());
    }//GEN-LAST:event_titleTextFieldCaretUpdate
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingEditorPanel localizingPaneList;
    private javax.swing.JPanel panel;
    private javax.swing.JPanel titlePanel;
    private javax.swing.JTextField titleTextField;
    private javax.swing.JCheckBox translationCheckBox;
    // End of variables declaration//GEN-END:variables
}
