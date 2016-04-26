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
 * EventCodeEditPanel.java
 *
 * Created on Dec 27, 2011, 3:44:55 PM
 */
package org.radixware.kernel.designer.ads.common.dialogs;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.LinkedList;
import javax.swing.DefaultComboBoxModel;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.AdsDefinition;

import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.localization.AdsEventCodeDef;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.localization.ILocalizedDef.MultilingualStringInfo;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.ads.common.EventSourceEnumFinder;
import org.radixware.kernel.designer.common.dialogs.components.localizing.HandleInfo;
import org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingEditorPanel;


public class EventCodeEditPanel extends javax.swing.JPanel {

    LocalizingEditorPanel lsPanel = new LocalizingEditorPanel("Event Messages");
    private MultilingualStringInfo string;

    /**
     * Creates new form EventCodeEditPanel
     */
    public EventCodeEditPanel() {
        initComponents();
        pnLocale2.setLayout(new BorderLayout());
        pnLocale2.add(lsPanel, BorderLayout.CENTER);
        lsPanel.setExtendedMode(true);


        btCopyGuid.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                org.radixware.kernel.designer.common.dialogs.utils.RadixNbEditorUtils.copyIdToClipboard(getStringId());
            }
        });
    }

    private Id getStringId() {
        return string.getId();
    }

    public void open(MultilingualStringInfo eventCode) {
        this.string = eventCode;
        update();
    }
    private ActionListener severityListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!updating) {
                if (getEventCode() != null) {
                    getEventCode().setEventSeverity((EEventSeverity) coSeverity.getSelectedItem());
                }
            }
        }
    };
    private ActionListener sourceListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!updating) {
                if (getEventCode() != null) {
                    getEventCode().setEventSource((String) coEventSource.getSelectedItem());
                }
            }
        }
    };

    private AdsEventCodeDef getEventCode() {
        return (AdsEventCodeDef) string.findString();
    }
    private boolean updating = false;

    public void update() {
        updating = true;
        try {
            lsPanel.open(new HandleInfo() {

                @Override
                public AdsDefinition getAdsDefinition() {
                    return (AdsDefinition) string.getOwner();
                }

                @Override
                public Id getTitleId() {
                    return string.getId();
                }

                @Override
                protected boolean isBundleResettable() {
                    return false;
                }

                @Override
                protected void onAdsMultilingualStringDefChange(IMultilingualStringDef multilingualStringDef) {
                    if (multilingualStringDef != null) {
                        string.setId(multilingualStringDef.getId());
                    } else {
                        string.setId(null);
                    }
                }

                @Override
                protected void onLanguagesChange() {
                    super.onLanguagesChange();
                }

                @Override
                protected void onLanguagesPatternChange(EIsoLanguage language, String newStringValue) {
                    IMultilingualStringDef nls = getAdsMultilingualStringDef();
                    if (nls != null) {
                        nls.setValue(language, newStringValue);
                    }
                }

                @Override
                protected void onSingleLanguagePatternChange(String newStringValue) {
                    super.onSingleLanguagePatternChange(newStringValue);
                }
            });


            DefaultComboBoxModel severityModel = new DefaultComboBoxModel(EEventSeverity.values());
            severityModel.removeElement(EEventSeverity.NONE);
            coSeverity.setModel(severityModel);


            IMultilingualStringDef s = string.findString();
            if (s instanceof AdsEventCodeDef) {
                coSeverity.setEnabled(!s.isReadOnly());
                coEventSource.setEnabled(!s.isReadOnly());
                final AdsEventCodeDef ec = (AdsEventCodeDef) s;
                if (ec.getEventSeverity() != null) {
                    coSeverity.setSelectedItem(ec.getEventSeverity());
                }
                coSeverity.addActionListener(severityListener);


                AdsEnumDef sourceEnum = findEventSourceEnum();
                if (sourceEnum == null) {
                    throw new IllegalStateException("Can't find enumeration with event sources.");
                }
                DefaultComboBoxModel sourceModel = new DefaultComboBoxModel(createSourceValues(sourceEnum));
                coEventSource.setModel(sourceModel);
                if (ec.getEventSource() != null) {
                    coEventSource.setSelectedItem(ec.getEventSource());
                }

                coEventSource.addActionListener(sourceListener);
            }


            btCopyGuid.setVisible(true);
        } finally {
            updating = false;
        }

    }

    private AdsEnumDef findEventSourceEnum() {
        EventSourceEnumFinder finder = new EventSourceEnumFinder();
        return finder.findEventSourceEnum((AdsDefinition) string.getOwner());
    }

    private Object[] createSourceValues(AdsEnumDef sourceEnum) {
        LinkedList<Object> list = new LinkedList<>();
        for (final AdsEnumDef.IItem item : sourceEnum.getItems().get(EScope.LOCAL_AND_OVERWRITE)) {
            list.add(item.getValue().toString());
        }
        Object[] arr = list.toArray();
        Arrays.sort(arr);
        return arr;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btCopyGuid = new javax.swing.JButton();
        pnLocale2 = new javax.swing.JPanel();
        lbEventSource = new javax.swing.JLabel();
        coEventSource = new javax.swing.JComboBox();
        lbSeverity = new javax.swing.JLabel();
        coSeverity = new javax.swing.JComboBox();

        btCopyGuid.setText(org.openide.util.NbBundle.getMessage(EventCodeEditPanel.class, "EventCodeEditPanel.btCopyGuid.text")); // NOI18N
        btCopyGuid.setToolTipText(org.openide.util.NbBundle.getMessage(EventCodeEditPanel.class, "EventCodeEditPanel.btCopyGuid.toolTipText")); // NOI18N

        javax.swing.GroupLayout pnLocale2Layout = new javax.swing.GroupLayout(pnLocale2);
        pnLocale2.setLayout(pnLocale2Layout);
        pnLocale2Layout.setHorizontalGroup(
            pnLocale2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 388, Short.MAX_VALUE)
        );
        pnLocale2Layout.setVerticalGroup(
            pnLocale2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 137, Short.MAX_VALUE)
        );

        lbEventSource.setText(org.openide.util.NbBundle.getMessage(EventCodeEditPanel.class, "EventCodeEditPanel.lbEventSource.text")); // NOI18N

        coEventSource.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lbSeverity.setText(org.openide.util.NbBundle.getMessage(EventCodeEditPanel.class, "EventCodeEditPanel.lbSeverity.text")); // NOI18N

        coSeverity.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 412, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnLocale2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbEventSource)
                            .addComponent(coEventSource, 0, 194, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbSeverity)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(coSeverity, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btCopyGuid)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnLocale2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbEventSource)
                    .addComponent(lbSeverity))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(coEventSource, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(coSeverity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btCopyGuid))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btCopyGuid;
    private javax.swing.JComboBox coEventSource;
    private javax.swing.JComboBox coSeverity;
    private javax.swing.JLabel lbEventSource;
    private javax.swing.JLabel lbSeverity;
    private javax.swing.JPanel pnLocale;
    private javax.swing.JPanel pnLocale1;
    private javax.swing.JPanel pnLocale2;
    // End of variables declaration//GEN-END:variables
}
