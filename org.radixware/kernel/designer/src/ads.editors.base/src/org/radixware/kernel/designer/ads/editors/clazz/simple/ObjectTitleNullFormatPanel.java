/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.designer.ads.editors.clazz.simple;

import java.awt.BorderLayout;
import java.util.EnumSet;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.radixware.kernel.common.builder.check.ads.MessageFormatValidator;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.ServerPresentationSupport;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsObjectTitleFormatDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.IAdsPresentableProperty;
import org.radixware.kernel.common.defs.ads.clazz.presentation.PropertyEditOptions;
import org.radixware.kernel.common.defs.ads.clazz.presentation.PropertyPresentation;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ETitleNullFormat;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.components.localizing.FormatLocalizingPanel;
import org.radixware.kernel.designer.common.dialogs.components.localizing.ProxyHandleInfo;
import org.radixware.kernel.designer.common.dialogs.components.state.StateAbstractDialog;

public class ObjectTitleNullFormatPanel extends StateAbstractDialog.StateAbstractPanel {
    private FormatLocalizingPanel formatLocalizingPanel;
    private final AdsObjectTitleFormatDef.TitleItem titleItem;
    private AdsObjectTitleFormatDef.TitleItem customItem;
    private final CheckableProxyHandleInfo handleInfo;
    private String inputPattern;

    public ObjectTitleNullFormatPanel(final AdsObjectTitleFormatDef.TitleItem titleItem) {
        initComponents();
        this.titleItem = titleItem;

        
        for (ETitleNullFormat format : ETitleNullFormat.values()) {
            jComboBox1.addItem(format);
        }
        
        ETitleNullFormat format = titleItem.getNullFormat();
        handleInfo = new CheckableProxyHandleInfo();
        jComboBox1.setSelectedItem(format);

        removePropertyItem();
    }
    
    private void removePropertyItem() {
        Object obj = jComboBox1.getSelectedItem();
        if (obj != ETitleNullFormat.PROPERTY_NULL_TITLE) {
            final AdsPropertyDef prop = titleItem.findProperty();
            if (!(prop instanceof IAdsPresentableProperty)) {
                jComboBox1.removeItem(ETitleNullFormat.PROPERTY_NULL_TITLE);
                return;
            }
            ServerPresentationSupport support = ((IAdsPresentableProperty) prop).getPresentationSupport();
            if (support == null) {
                jComboBox1.removeItem(ETitleNullFormat.PROPERTY_NULL_TITLE);
                return;
            }
            PropertyPresentation pres = support.getPresentation();
            PropertyEditOptions options = pres.getEditOptions();
            if (!pres.isPresentable()) {
                jComboBox1.removeItem(ETitleNullFormat.PROPERTY_NULL_TITLE);
            }
        }
    }
    
    public void update() {
        Object object = jComboBox1.getSelectedItem();
        jPanel1.removeAll();
        if (object instanceof ETitleNullFormat) {
            if (object == ETitleNullFormat.CUSTOM) {
                customItem = titleItem.getCustomTitle();
                if (customItem == null) {
                    AdsPropertyDef prop = titleItem.findProperty();
                    customItem = AdsObjectTitleFormatDef.TitleItem.Factory.newInstanceCustomNullTitle(prop, titleItem);
                }
                formatLocalizingPanel = new FormatLocalizingPanel(customItem);
                this.inputPattern = customItem.getPattern();
                jPanel1.add(formatLocalizingPanel, BorderLayout.CENTER);
                formatLocalizingPanel.open(handleInfo);
                formatLocalizingPanel.addChangeSupportListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        if (ObjectTitleNullFormatPanel.this.formatLocalizingPanel != null) {
                        }
                    }
                });
            }
            removePropertyItem();
        }
        jPanel1.repaint();
    }
    
    protected void apply() {
        Object object = jComboBox1.getSelectedItem();
        if (object instanceof ETitleNullFormat) {
            titleItem.setNullFormat((ETitleNullFormat) object);
            if (customItem != null) {
                handleInfo.commit();
                titleItem.setCustomTitle(customItem);
            }
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

        jComboBox1 = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();

        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jPanel1.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox1, 0, 369, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        update();
    }//GEN-LAST:event_jComboBox1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables

    public boolean checkPattern(AdsPropertyDef adsPropertyDef, String pattern) {
        if (adsPropertyDef == null || MessageFormatValidator.formatIsValid(adsPropertyDef.getValue().getType().getTypeId(), pattern)) {
            stateManager.ok();
            return true;
        } else {
            stateManager.error("Invalid pattern format");
            return false;
        }
    }
    
    @Override
    public void check() {
        final AdsPropertyDef adsPropertyDef = titleItem.findProperty();
        if (customItem != null) {
            if (formatLocalizingPanel.isMultilingual()) {
                handleInfo.check(adsPropertyDef);
            } else {
                checkPattern(adsPropertyDef, inputPattern);
            }
        }
        changeSupport.fireChange();
    }
    
    public static boolean showModel(AdsObjectTitleFormatDef.TitleItem titleItem) {
        ObjectTitleNullFormatPanel panel = new ObjectTitleNullFormatPanel(titleItem);
        StateAbstractDialog dialog = new StateAbstractDialog(panel, "Null Format Editor") {};
        boolean result = dialog.showModal();
        if (result) {
            panel.apply();
        }
        return result;
    }
    
    
    class CheckableProxyHandleInfo extends ProxyHandleInfo {
            @Override
            public Definition getAdsDefinition() {
                return titleItem.getOwnerDefinition();
            }

            @Override
            public Id getTitleId() {
                AdsObjectTitleFormatDef.TitleItem item = titleItem.getCustomTitle();
                return item == null? null : item.getPatternId();
            }

            @Override
            public void onAdsMultilingualStringDefChange(IMultilingualStringDef multilingualStringDef) {
                ObjectTitleNullFormatPanel.this.check();
            }

            @Override
            public void onLanguagesPatternChange(EIsoLanguage language, String newPattern) {
                super.onLanguagesPatternChange(language, newPattern);
                ObjectTitleNullFormatPanel.this.check();
            }

            @Override
            public void onSingleLanguagePatternChange(String newStringValue) {
                inputPattern = newStringValue;
                ObjectTitleNullFormatPanel.this.check();
            }

            @Override
            public void onLanguagesChange() {
                if (formatLocalizingPanel.isMultilingual()) {
                    String defaultValue = inputPattern;
                    if (defaultValue == null) {
                        defaultValue = "";

                    }
                    Layer layer = getAdsDefinition().getLayer();
                    EnumSet<EIsoLanguage> languages = EnumSet.noneOf(EIsoLanguage.class);
                    if (layer != null) {
                        languages.addAll(layer.getLanguages());
                    }
                    if (languages.isEmpty()) {
                        languages.add(EIsoLanguage.ENGLISH);
                    }
                    createAdsMultilingualStringDef();
                    for (EIsoLanguage l : languages) {
                        onLanguagesPatternChange(l, inputPattern);
                    }
                } else {
                    removeAdsMultilingualStringDef();
                }
                ObjectTitleNullFormatPanel.this.check();
                changeSupport.fireChange();
            }

            @Override
            public boolean isProxyState() {
                return true;
            }

            @Override
            public boolean commit() {
            if (customItem != null) {
                customItem.setMultilingual(formatLocalizingPanel.isMultilingual());
                if (formatLocalizingPanel.isMultilingual()) {
                    if (adsMultilingualStringDef != null) {
                        for (IMultilingualStringDef.StringStorage storage : adsMultilingualStringDef.getValues(ExtendableDefinitions.EScope.LOCAL)) {
                            customItem.setPattern(storage.getLanguage(), storage.getValue());
                        }
                    }
                } else {
                    customItem.setPattern(inputPattern);
                }
            }
                return true;
            }
            
            public void check(AdsPropertyDef adsPropertyDef) {
                if (adsMultilingualStringDef != null) {
                    for (IMultilingualStringDef.StringStorage storage : adsMultilingualStringDef.getValues(ExtendableDefinitions.EScope.LOCAL)) {
                        if (!checkPattern(adsPropertyDef, storage.getValue())) {
                            return;
                        }
                    }
                }
            }
        };

}
