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

package org.radixware.kernel.designer.ads.editors.domain;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jdesktop.swingx.VerticalLayout;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDomainDef;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.ads.common.dialogs.AccessPanel;
import org.radixware.kernel.designer.common.annotations.registrators.EditorFactoryRegistration;
import org.radixware.kernel.designer.common.dialogs.components.localizing.HandleInfo;
import org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingEditorPanel;
import org.radixware.kernel.designer.common.editors.RadixObjectModalEditor;
import org.radixware.kernel.designer.common.general.editors.IEditorFactory;
import org.radixware.kernel.designer.common.general.editors.IRadixObjectEditor;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;


public class AdsDomainEditor extends RadixObjectModalEditor<AdsDomainDef> {

    @EditorFactoryRegistration
    public static final class Factory implements IEditorFactory<AdsDomainDef> {

        @Override
        public IRadixObjectEditor<AdsDomainDef> newInstance(AdsDomainDef adsDomainDef) {
            return new AdsDomainEditor(adsDomainDef);
        }
    }
    private AdsDomainDef adsDomainDef;
    private HandleInfo handleInfo;
    private boolean isEdited;
    private JCheckBox chIsDeprecated = new JCheckBox();
    private AccessPanel accessPanel;
    private final Map<EIsoLanguage, String> changedTitles = new HashMap<>();

    /**
     * Creates new form AdsDomainEditor
     */
    private AdsDomainEditor(AdsDomainDef adsDomainDef) {
        super(adsDomainDef);
        this.adsDomainDef = adsDomainDef;

//        initComponents();
        initPanel();

        setComplete(true);


    }

    private void initPanel() {
        setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        setLayout(new BorderLayout());

        accessPanel = new AccessPanel(false);

        accessPanel.getChangeSupport().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                isEdited = true;
                AdsDomainEditor.this.setComplete(true);
            }
        });

        final JPanel ap = new JPanel();
        ap.setLayout(new VerticalLayout());

        ap.add(accessPanel);
        accessPanel.open(adsDomainDef);

        chIsDeprecated.setText("Deprecated");
        chIsDeprecated.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isEdited = true;
                AdsDomainEditor.this.setComplete(true);
            }
        });
        ap.add(chIsDeprecated);

        add(ap, BorderLayout.PAGE_START);
        localizingPaneList = new LocalizingEditorPanel();
        add(localizingPaneList, BorderLayout.CENTER);
    }

    @Override
    public boolean isOpeningAfterNewObjectCreationRequired() {
        return false;
    }

    @Override
    public boolean open(OpenInfo openInfo) {

        handleInfo = new HandleInfo() {
            @Override
            public AdsDefinition getAdsDefinition() {
                return adsDomainDef;
            }

            @Override
            public Id getTitleId() {
                return adsDomainDef.getTitleId();
            }

            @Override
            protected void onAdsMultilingualStringDefChange(IMultilingualStringDef multilingualStringDef) {
                isEdited = true;
                AdsDomainEditor.this.setComplete(true);
            }

            @Override
            protected void onLanguagesPatternChange(EIsoLanguage language, String newStringValue) {
                isEdited = true;
                changedTitles.put(language, newStringValue);
                AdsDomainEditor.this.setComplete(true);
            }
        };
        update();
        return super.open(openInfo);
    }

    @Override
    public void update() {
        isEdited = false;
        chIsDeprecated.setSelected(adsDomainDef.isDeprecated());
        chIsDeprecated.setEnabled(!(adsDomainDef.isReadOnly() || adsDomainDef.isOwnerDeprecated()));

        AdsDomainEditor.this.setComplete(false);
        localizingPaneList.update(handleInfo);
    }

    @Override
    protected void apply() {
        if (isEdited) {
            adsDomainDef.setDeprecated(chIsDeprecated.isSelected());
            final IMultilingualStringDef resultAdsMultilingualStringDef = handleInfo.getAdsMultilingualStringDef();



            if (resultAdsMultilingualStringDef != null) {
                for (Map.Entry<EIsoLanguage, String> e : changedTitles.entrySet()) {
                    resultAdsMultilingualStringDef.setValue(e.getKey(), e.getValue());
                }
                adsDomainDef.setTitleId(resultAdsMultilingualStringDef.getId());
            } else {
                adsDomainDef.setTitleId(null);
            }
            final Map<EIsoLanguage, String> resultLanguages2PatternsMap = localizingPaneList.getLanguages2PatternsMap();
            assert (resultLanguages2PatternsMap != null);
            Iterator<Map.Entry<EIsoLanguage, String>> iter = resultLanguages2PatternsMap.entrySet().iterator();
            while (iter.hasNext()) {
                final Map.Entry<EIsoLanguage, String> entry = iter.next();
                adsDomainDef.setTitle(entry.getKey(), entry.getValue());
            }
            accessPanel.apply();
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

        localizingPaneList = new org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingEditorPanel();

        setMaximumSize(null);
        setMinimumSize(null);
        setPreferredSize(new java.awt.Dimension(400, 80));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));
        add(localizingPaneList);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingEditorPanel localizingPaneList;
    // End of variables declaration//GEN-END:variables
}
