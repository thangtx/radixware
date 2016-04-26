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
 * FormatPopupLocalizingPanel.java
 *
 * Created on Mar 31, 2009, 5:05:26 PM
 */
package org.radixware.kernel.designer.common.dialogs.components.localizing;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.*;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsObjectTitleFormatDef;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;


public class FormatLocalizingPanel extends JPanel implements ChangeListener {

    private boolean isMultilingual = false;
    private boolean isExpanded = false;
    protected JPanel bodyPanel;
    protected GridBagLayout gridBagLayout;
    protected HandleInfo handleInfo;
    private List<LocalizedTextField> items = new ArrayList<>();
    private LocalizedTextField valueField;
    private AdsObjectTitleFormatDef.TitleItem titleItem;
    private ChangeSupport changeSupport = new ChangeSupport(this);
    private boolean isReadonly = false;
    private boolean isUpdating = false;

    public void addChangeSupportListener(ChangeListener l) {
        changeSupport.addChangeListener(l);
    }

    public void removeChangeSupportListener(ChangeListener l) {
        changeSupport.removeChangeListener(l);
    }

    /** Creates new form FormatLocalizingPanel */
    public FormatLocalizingPanel(AdsObjectTitleFormatDef.TitleItem titleItem) {
        super();
        initComponents();

        this.titleItem = titleItem;

        bodyPanel = new javax.swing.JPanel();
        bodyPanel.setAlignmentX(0.0f);

        gridBagLayout = new GridBagLayout();
        bodyPanel.setLayout(gridBagLayout);

        bodyPanel.setBorder(new EmptyBorder(0, 10, 10, 10));

        jCheckBox1.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                processCheckBox();
            }
        });
    }

    private LocalizedTextField createComponent(EIsoLanguage language) {
        return new LocalizedTextField(language);
    }

    @Override
    public Dimension getMaximumSize() {
        return isExpanded ? super.getMaximumSize() : new Dimension(super.getMaximumSize().width, Short.MAX_VALUE);
    }

    public boolean isMultilingual() {
        return isMultilingual;
    }

    private void processCheckBox() {
        changeSupport.fireChange();

        if (jCheckBox1.isSelected()) {

            isMultilingual = false;
            items.clear();
            if (!isUpdating) {
                titleItem.setMultilingual(false);
                handleInfo.removeAdsMultilingualStringDef();
            }
            bodyPanel.removeAll();

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.anchor = GridBagConstraints.NORTHWEST;
            constraints.insets = new Insets(10, 10, 0, 0);

            valueField = createComponent(null);
            valueField.setReadonly(isReadonly);

            valueField.addChangeListener(this);

            String value = titleItem.getPattern();
            if (value == null) {
                value = getUsablePattern();
            }
            valueField.setText(value);

            final JLabel label = new JLabel("Default Pattern:");
            bodyPanel.add(label);
            constraints.weightx = 0.0f;
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.gridwidth = GridBagConstraints.RELATIVE;
            constraints.fill = GridBagConstraints.NONE;
            constraints.anchor = GridBagConstraints.WEST;
            gridBagLayout.setConstraints(label, constraints);

            bodyPanel.add(valueField);
            constraints.weightx = 20.0f;
            constraints.gridx = 1;
            constraints.gridwidth = GridBagConstraints.REMAINDER;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.anchor = GridBagConstraints.WEST;
            gridBagLayout.setConstraints(valueField, constraints);

            performExpand();
        } else {

            isMultilingual = true;
            items.clear();
            if (!isUpdating) {
                titleItem.setMultilingual(true);

                if (handleInfo.getAdsMultilingualStringDef() == null) {
                    //create new AdsMultilingualStringDef and add it to bundle
                    handleInfo.setAdsMultilingualStringDef(AdsMultilingualStringDef.Factory.newInstance());
                }
            }

            updateEditlines();
            handleInfo.onLanguagesChange();
            performExpand();
        }

        revalidate();
        repaint();
    }

    @Override
    public boolean requestFocusInWindow() {
        if (isMultilingual) {
            if (items != null && items.size() > 0) {
                return items.get(0).requestFocusInWindow();
            }
        } else if (valueField != null){
            return valueField.requestFocusInWindow();
        }
        return super.requestFocusInWindow();
    }

    private void performExpand() {
        bodyPanel.setVisible(true);
        this.add(bodyPanel);
        isExpanded = true;
    }

    public void update(HandleInfo handleInfo) {

        this.isUpdating = true;
        this.handleInfo = handleInfo;
        boolean noStringDef = handleInfo.getAdsMultilingualStringDef() == null;
        jCheckBox1.setSelected(noStringDef);
        processCheckBox();
        this.isUpdating = false;
    }

    private String getUsablePattern() {

        final String result;

        final EValType valType = titleItem.findProperty().getValue().getType().getTypeId();

        if (valType == EValType.DATE_TIME) {
            result = "{0, date}";
        } else if (valType == EValType.INT) {
            result = "{0, number, long}";
        } else if (valType == EValType.NUM) {
            result = "{0, number}";
        } else if (valType == EValType.BOOL) {
            result = "{0}";
        } else if (valType == EValType.CHAR) {
            result = "{0}";
        } else { //else use default for all the rest valTypes
            result = "{0}";
        }

        return result;
    }

    protected void updateEditlines() {

        bodyPanel.removeAll();

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(10, 10, 0, 0);

        final List<EIsoLanguage> curLanguages = handleInfo.getAdsDefinition().getModule().getSegment().getLayer().getLanguages();

        final IMultilingualStringDef adsMultilingualStringDef = handleInfo.getAdsMultilingualStringDef();

        int totalLanguages = 0;
        for (EIsoLanguage l : curLanguages) {

            final LocalizedTextField valueField = createComponent(l);
            valueField.setReadonly(isReadonly);
            items.add(valueField);

            valueField.addChangeListener(this);

            String value;
            if (adsMultilingualStringDef != null) {
                value = adsMultilingualStringDef.getValue(l);
                if (value == null) {
                    value = getUsablePattern();
                }
            } else {
                value = getUsablePattern();
            }

            valueField.setText(value);

            final JLabel label = new JLabel(l.getName() + ":");
            bodyPanel.add(label);
            constraints.weightx = 0.0f;
            constraints.gridx = 0;
            constraints.gridy = totalLanguages++;
            constraints.gridwidth = GridBagConstraints.RELATIVE;
            constraints.fill = GridBagConstraints.NONE;
            constraints.anchor = GridBagConstraints.WEST;
            gridBagLayout.setConstraints(label, constraints);

            bodyPanel.add(valueField);
            constraints.weightx = 20.0f;
            constraints.gridx = 1;
            constraints.gridwidth = GridBagConstraints.REMAINDER;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.anchor = GridBagConstraints.WEST;
            gridBagLayout.setConstraints(valueField, constraints);
        }
        if (items.size() > 0) {
            items.get(0).requestFocusInWindow();
        }
    }

    public void open(HandleInfo handleInfo) {
        update(handleInfo);
    }

    public void setReadonly(boolean readonly) {

        this.isReadonly = readonly;

        jCheckBox1.setEnabled(!readonly);

        for (Component c : this.getComponents()) {
            if (c instanceof LocalizedTextField) {
                ((LocalizedTextField) c).setReadonly(readonly);
            }
        }
    }

    private static EIsoLanguage getEISOLanguageForCurrentLocale() {

        try {
            return EIsoLanguage.getForValue(Locale.getDefault().getLanguage());
        } catch (NoConstItemWithSuchValueError e) {
            return EIsoLanguage.ENGLISH;
        }
    }

    public String getPatternForCurrentLocale() {

        if (isMultilingual) {
            return getLanguages2PatternsMap().get(getEISOLanguageForCurrentLocale());
        } else {
            return titleItem.getPattern();
        }
    }

    public Map<EIsoLanguage, String> getLanguages2PatternsMap() {

        final Map<EIsoLanguage, String> languages2PatternsMap = new HashMap<>(items.size());
        for (LocalizedTextField field : items) {
            languages2PatternsMap.put(field.getLanguage(), field.getText().toString());
        }

        return languages2PatternsMap;
    }

    @Override
    public void stateChanged(ChangeEvent e) {

        if (isUpdating) {
            return;
        }

        if (isMultilingual) {
            if (handleInfo.getAdsMultilingualStringDef() != null) {
                final LocalizedTextField valueField = (LocalizedTextField) e.getSource();
                handleInfo.onLanguagesPatternChange(valueField.getLanguage(), valueField.getText());
            }
        } else {
            final LocalizedTextField valueField = (LocalizedTextField) e.getSource();
            final String newPattern = valueField.getText();
            handleInfo.onSingleLanguagePatternChange(newPattern);
        }

        changeSupport.fireChange();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jCheckBox1 = new javax.swing.JCheckBox();

        setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(FormatLocalizingPanel.class, "FormatLocalizingPanel.border.title"))); // NOI18N
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        jCheckBox1.setText(org.openide.util.NbBundle.getMessage(FormatLocalizingPanel.class, "FormatLocalizingPanel.jCheckBox1.text")); // NOI18N
        add(jCheckBox1);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox jCheckBox1;
    // End of variables declaration//GEN-END:variables
}
