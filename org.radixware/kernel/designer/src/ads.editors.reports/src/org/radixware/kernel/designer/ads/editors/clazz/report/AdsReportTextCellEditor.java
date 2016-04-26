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

package org.radixware.kernel.designer.ads.editors.clazz.report;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportTextCell;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.components.localizing.HandleInfo;
import org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingEditorPanel;


class AdsReportTextCellEditor extends JPanel {

    private class TitleHandleInfo extends HandleInfo {

        @Override
        public AdsDefinition getAdsDefinition() {
            return adsReportTextCell.getOwnerReport();
        }

        @Override
        public Id getTitleId() {
            if (adsReportTextCell.findText() == null && !adsReportTextCell.isReadOnly()) {
                final AdsMultilingualStringDef def = AdsMultilingualStringDef.Factory.newInstance();
//                def.setValue(EIsoLanguage.ENGLISH, "Hello");
//                def.setValue(EIsoLanguage.RUSSIAN, "Привет");
                adsReportTextCell.getOwnerReport().findLocalizingBundle().getStrings().getLocal().add(def);
                adsReportTextCell.setTextId(def.getId());
            }
            return adsReportTextCell.getTextId();
        }

//        @Override
//        public AdsMultilingualStringDef getAdsMultilingualStringDef() {
//            if (adsReportTextCell.findText() == null && !adsReportTextCell.isReadOnly()) {
//                AdsMultilingualStringDef def = AdsMultilingualStringDef.Factory.newInstance();
//                def.setValue(EIsoLanguage.ENGLISH, "Hello");
//                def.setValue(EIsoLanguage.RUSSIAN, "Привет");
//                adsReportTextCell.getOwnerReport().findLocalizingBundle().getStrings().getLocal().add(def);
//                adsReportTextCell.setTextId(def.getId());
//            }
//            return adsReportTextCell.findText();
//        }

//        @Override
//        public void setAdsMultilingualStringDef(AdsMultilingualStringDef adsMultilingualStringDef) {
//            adsReportTextCell.getOwnerReport().findLocalizingBundle().getStrings().getLocal().add(def);
//            adsReportTextCell.setTextId(adsMultilingualStringDef.getId());
//        }

//        @Override
//        public void removeAdsMultilingualStringDef() {
//            adsReportTextCell.setTextId(null);
//        }

        @Override
        protected boolean isBundleResettable() {
            return false;
        }

        @Override
        public void onLanguagesPatternChange(final EIsoLanguage language, final String newStringValue) {
//            strForLang.put(language, newStringValue);
            getAdsMultilingualStringDef().setValue(language, newStringValue);
        }

    }

    //private volatile boolean updating = false;
    private final AdsReportTextCell adsReportTextCell;
    private final LocalizingEditorPanel localizingPaneList = new LocalizingEditorPanel();

    /** Creates new form AdsReportTextCellEditor */
    public AdsReportTextCellEditor(final AdsReportTextCell cell) {
        super();
        adsReportTextCell = cell;
        initComponents();
        panel.add(localizingPaneList, BorderLayout.NORTH);

//        typeComboBox.setModel(new DefaultComboBoxModel(EReportCellType.values()));

        setupInitialValues();
//        SwingUtilities.invokeLater(new Runnable() {
//
//            @Override
//            public void run() {
//                contentTextField.requestFocusInWindow();
//            }
//        });
    }

    private void setupInitialValues() {
        //updating = true;
//        typeComboBox.setSelectedItem(adsReportTextCell.getCellType());
        localizingPaneList.open(new TitleHandleInfo());
        localizingPaneList.setVisible(true);
//        contentTextField.setText(adsReportTextCell.getContent());
        updateEnableState();
        //updating = false;
    }

    private void updateEnableState() {
        final boolean enabled = !adsReportTextCell.isReadOnly();
//        typeComboBox.setEnabled(false);
        localizingPaneList.setEnabled(enabled);
//        contentTextField.setEnabled(enabled);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel = new javax.swing.JPanel();

        panel.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel panel;
    // End of variables declaration//GEN-END:variables

}
