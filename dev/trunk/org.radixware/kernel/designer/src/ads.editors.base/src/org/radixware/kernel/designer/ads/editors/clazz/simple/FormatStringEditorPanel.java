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
 * FormatStringEditorPanel.java
 *
 * Created on Mar 4, 2009, 5:59:21 PM
 */
package org.radixware.kernel.designer.ads.editors.clazz.simple;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.radixware.kernel.common.builder.check.ads.MessageFormatValidator;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsObjectTitleFormatDef;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.components.localizing.HandleInfo;

import org.radixware.kernel.designer.common.dialogs.components.localizing.FormatLocalizingPanel;
import org.radixware.kernel.designer.common.dialogs.components.state.StateAbstractDialog;
import org.radixware.kernel.designer.common.dialogs.components.state.StateDisplayer;


public class FormatStringEditorPanel extends StateAbstractDialog.StateAbstractPanel {

    private FormatLocalizingPanel formatLocalizingPanel = null;
    //private AdsObjectTitleFormatDef objectTitleFormat = null;
    private AdsObjectTitleFormatDef.TitleItem editingTitleItem = null;
    private HandleInfo handleInfo;
    private boolean isMultilingualResult = false;
    private Map<EIsoLanguage, String> languages2PatternsMap;
    private String singleLanguageValue;
    private HelpAboutMessageFormatPanel helpAboutMessageFormatPanel;
    private DemoFormatPanel demoFormatPanel;
    private StateDisplayer stateDisplayer;
    private String inputPattern;
    private FormatGroupSeparatorPanel formatGroupSeparatorPanel;
    private AdsTypeDeclaration inputType;
    private boolean isSimplePatternCheck = false;

    private boolean validatePattern(String pattern) {

        assert (editingTitleItem != null);
        final AdsPropertyDef adsPropertyDef = editingTitleItem.findProperty();

        if (adsPropertyDef == null || MessageFormatValidator.formatIsValid(adsPropertyDef.getValue().getType().getTypeId(), pattern)) {
            stateManager.ok();
            return true;
        } else {
            if (!demoFormatPanel.getCurrentError().isEmpty()) {
                stateManager.error("Invalid pattern format: " + demoFormatPanel.getCurrentError());
            } else {
                stateManager.error("Invalid pattern format!");
            }
            return false;
        }
    }

    private void validateSimplePattern(String pattern) {
        if (inputPattern != null && inputType != null) {
            if (MessageFormatValidator.formatIsValid(inputType.getTypeId(), inputPattern)) {
                stateManager.ok();
            } else {
                stateManager.error("Invalid pattern format!");
            }
        } else {
            stateManager.error("Input values missed");
        }
    }

    public String getPatternForCurrentLocale() {
        return formatLocalizingPanel.getPatternForCurrentLocale();
    }

    private void checkMultilingualPatterns() {

        Iterator<String> iter = languages2PatternsMap.values().iterator();
        while (iter.hasNext()) {
            if (!validatePattern(iter.next())) {
                return;
            }
        }
    }

    private void checkSingleLanguagePattern() {
        validatePattern(singleLanguageValue);
    }

    @Override
    public void check() {

        if (isMultilingualResult) {
            checkMultilingualPatterns();
        } else if (!isSimplePatternCheck) {
            checkSingleLanguagePattern();
        } else {
            validateSimplePattern(inputPattern);
        }
        changeSupport.fireChange();
    }

    protected void apply() {
        if (!isSimplePatternCheck) {
            if (!editingTitleItem.isMultilingual()) {
                editingTitleItem.setMultilingual(isMultilingualResult);
            }

            if (isMultilingualResult) {
                Iterator<Map.Entry<EIsoLanguage, String>> iter = languages2PatternsMap.entrySet().iterator();
                while (iter.hasNext()) {
                    final Map.Entry<EIsoLanguage, String> pair = iter.next();
                    editingTitleItem.setPattern(pair.getKey(), pair.getValue());
                }
            } else {
                boolean isMustSet = false;
                if (editingTitleItem.getPattern() != null
                        && !editingTitleItem.getPattern().equals(singleLanguageValue)) {
                    isMustSet = true;
                }
                if (editingTitleItem.getPattern() == null
                        && singleLanguageValue != null) {
                    isMustSet = true;
                }

                if (isMustSet) {
                    editingTitleItem.setPattern(singleLanguageValue);
                }
            }
        }
    }

    public String getPattern() {
        if (isSimplePatternCheck) {
            return inputPattern;
        }
        return getPatternForCurrentLocale();
    }

    public String getGroupSeparator() {
        if (formatGroupSeparatorPanel != null && formatGroupSeparatorPanel.isSetGroupSeparator()) {
            return formatGroupSeparatorPanel.getGroupSeparator();
        }
        return null;
    }

    public int getGroupSize() {
        if (formatGroupSeparatorPanel != null && formatGroupSeparatorPanel.isSetGroupSeparator()) {
            return formatGroupSeparatorPanel.getGroupSize();
        }
        return 0;
    }

    /**
     * Creates new form FormatStringEditorPanel
     */
    public FormatStringEditorPanel() {
        initComponents();
    }

    @Override
    public void requestFocus() {
        requestFocusInWindow();
    }

    @Override
    public boolean requestFocusInWindow() {
        if (toFocusOn != null) {
            return toFocusOn.requestFocusInWindow();
        }
        return false;
    }

    public void open(final AdsObjectTitleFormatDef objectTitleFormat, final AdsObjectTitleFormatDef.TitleItem titleItem) {
        this.editingTitleItem = titleItem;
        this.languages2PatternsMap = new HashMap<EIsoLanguage, String>();
        this.singleLanguageValue = titleItem.getPattern();

        handleInfo = new HandleInfo() {
            @Override
            public AdsDefinition getAdsDefinition() {
                return objectTitleFormat;
            }

            @Override
            public Id getTitleId() {
                return titleItem.getPatternId();
            }

            @Override
            public void onAdsMultilingualStringDefChange(IMultilingualStringDef multilingualStringDef) {

                isMultilingualResult = formatLocalizingPanel.isMultilingual();
                if (!isMultilingualResult) {
                    languages2PatternsMap.clear();
                }
                check();
            }

            @Override
            public void onLanguagesPatternChange(EIsoLanguage language, String newPattern) {

                languages2PatternsMap.put(language, newPattern);
                demoFormatPanel.setPatternValue(newPattern);
                check();

                //if (isOk()) {

                //changeSupport.fireChange();
                //}
            }

            @Override
            public void onSingleLanguagePatternChange(String newStringValue) {
                singleLanguageValue = newStringValue;
                demoFormatPanel.setPatternValue(newStringValue);
                check();

                //if (isOk()) {

                //changeSupport.fireChange();
                //}
            }

            @Override
            public void onLanguagesChange() {
                languages2PatternsMap.clear();
                check();
                changeSupport.fireChange();
            }
        };

        setupUI(null, 0);
    }

    public void open(AdsTypeDeclaration inputType, String inputPattern,
            String groupSeparator, int groupSize) {
        this.inputPattern = inputPattern != null ? inputPattern : "";
        this.inputType = inputType;
        this.isSimplePatternCheck = true;

        setupUI(groupSeparator, groupSize);
    }
    private Component toFocusOn;

    public void setupUI(String groupSeparator, int groupSize) {

        final JPanel upperPanel = new JPanel();
        upperPanel.setLayout(new BorderLayout());
        if (!isSimplePatternCheck) {
            formatLocalizingPanel = createFormatLocalizingPanel();
            upperPanel.add(formatLocalizingPanel, BorderLayout.NORTH);
            formatLocalizingPanel.update(handleInfo);

            toFocusOn = formatLocalizingPanel;
        } else {
            javax.swing.JPanel content = new javax.swing.JPanel();
            javax.swing.JLabel label = new javax.swing.JLabel("Pattern:");
            final javax.swing.JTextField field = new javax.swing.JTextField(inputPattern != null ? inputPattern : "");
            field.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    changedUpdate(e);
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    changedUpdate(e);
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    inputPattern = field.getText();
                    check();
                    if (isOk()) {
                        String groupSeparator = formatGroupSeparatorPanel != null
                                ? formatGroupSeparatorPanel.getGroupSeparator() : null;
                        int groupSize = formatGroupSeparatorPanel != null
                                ? formatGroupSeparatorPanel.getGroupSize() : 0;
                        demoFormatPanel.setPatternValue(inputPattern, groupSeparator, groupSize);
                    }
                }
            });


            GridBagLayout gbl = new GridBagLayout();
            GridBagConstraints c = new GridBagConstraints();
            content.setLayout(gbl);

            c.fill = GridBagConstraints.HORIZONTAL;
            c.insets = new Insets(0, 0, 0, 10);
            gbl.setConstraints(label, c);
            content.add(label);

            c.weightx = 1.0;
            c.insets = new Insets(0, 0, 0, 0);
            gbl.setConstraints(field, c);
            content.add(field);
            upperPanel.add(content, BorderLayout.NORTH);

            if (inputType.getTypeId() == EValType.NUM || inputType.getTypeId() == EValType.INT) {
                formatGroupSeparatorPanel = new FormatGroupSeparatorPanel(this, groupSeparator, groupSize);
                //upperPanel.add(formatGroupSeparatorPanel);
            }
            toFocusOn = field;
        }

        helpAboutMessageFormatPanel = createHelpAboutMessageFormatPanel();
        demoFormatPanel = createDemoFormatPanel(groupSeparator, groupSize);
        this.addChangeListener(demoFormatPanel);

        if (!isSimplePatternCheck) {
            helpAboutMessageFormatPanel.open(editingTitleItem.findProperty().getValue().getType().getTypeId());
        } else {
            helpAboutMessageFormatPanel.open(inputType.getTypeId());
        }

        stateDisplayer = createStateDisplayer();


        GridBagLayout mainGbl = new GridBagLayout();
        setLayout(mainGbl);
        GridBagConstraints mainConst = new GridBagConstraints();
        mainConst.fill = GridBagConstraints.HORIZONTAL;
        mainConst.weightx = 1.0;
        mainConst.insets = new Insets(10, 10, 0, 10);
        mainGbl.setConstraints(upperPanel, mainConst);
        add(upperPanel);

        int index = 1;
        if (formatGroupSeparatorPanel != null) {
            mainConst.gridy = index;
            mainGbl.setConstraints(formatGroupSeparatorPanel, mainConst);
            add(formatGroupSeparatorPanel);
            index++;
        }

        mainConst.gridy = index;
        mainGbl.setConstraints(demoFormatPanel, mainConst);
        add(demoFormatPanel);
        index++;

        mainConst.gridy = index;
        mainConst.weighty = 1.0;
        mainConst.fill = GridBagConstraints.BOTH;
        mainGbl.setConstraints(helpAboutMessageFormatPanel, mainConst);
        add(helpAboutMessageFormatPanel);
        index++;

        mainConst.gridy = index;
        mainConst.weightx = 1.0;
        mainConst.weighty = 0.0;
        mainConst.fill = GridBagConstraints.HORIZONTAL;
        mainGbl.setConstraints(stateDisplayer, mainConst);
        add(stateDisplayer);

//        this.revalidate();
//        this.repaint();

        check();
    }

    private DemoFormatPanel createDemoFormatPanel(String groupSeparator, int groupSize) {

        AdsType type = null;
        DdsTableDef referencedTable = null;
        if (!isSimplePatternCheck) {
            editingTitleItem.findProperty().getValue().getType().resolve(editingTitleItem.getDefinition()).get();

            if (type instanceof AdsClassType.EntityObjectType) {
                referencedTable = ((AdsClassType.EntityObjectType) type).getSource().findTable(editingTitleItem.getDefinition());
            }
            EValType valType = editingTitleItem.findProperty().getValue().getType().getTypeId();
            final DemoFormatPanel result = new DemoFormatPanel(valType, referencedTable, groupSeparator, groupSize);
            result.setMinimumSize(new Dimension(result.getMinimumSize().width, 70));
            result.setMaximumSize(new Dimension(result.getMaximumSize().width, 70));
            result.setPreferredSize(new Dimension(result.getPreferredSize().width, 70));

            result.setAlignmentX(0.0f);

            return result;
        }
        type = inputType.resolve(editingTitleItem.getDefinition()).get();
        if (type instanceof AdsClassType.EntityObjectType) {
            referencedTable = ((AdsClassType.EntityObjectType) type).getSource().findTable(editingTitleItem.getDefinition());
        }

        final DemoFormatPanel result = new DemoFormatPanel(inputType.getTypeId(), referencedTable, groupSeparator, groupSize);
        result.setMinimumSize(new Dimension(result.getMinimumSize().width, 70));
        result.setMaximumSize(new Dimension(result.getMaximumSize().width, 70));
        result.setPreferredSize(new Dimension(result.getPreferredSize().width, 70));

        result.setAlignmentX(0.0f);

        return result;

    }

    private HelpAboutMessageFormatPanel createHelpAboutMessageFormatPanel() {
        return new HelpAboutMessageFormatPanel();
    }

    private StateDisplayer createStateDisplayer() {

        final StateDisplayer result = new StateDisplayer() {
            @Override
            public Dimension getMaximumSize() {
                return new Dimension(super.getMaximumSize().width, super.getPreferredSize().height);
            }
        };
        result.setAlignmentX(0.0F);

        return result;
    }

    private FormatLocalizingPanel createFormatLocalizingPanel() {

        final FormatLocalizingPanel result = new FormatLocalizingPanel(editingTitleItem) {
            @Override
            public Dimension getMaximumSize() {
                return new Dimension(super.getMaximumSize().width, super.getPreferredSize().height);
            }
        };

        result.addChangeSupportListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (FormatStringEditorPanel.this.formatLocalizingPanel != null) {
                    FormatStringEditorPanel.this.isMultilingualResult = FormatStringEditorPanel.this.formatLocalizingPanel.isMultilingual();
                }
            }
        });
        result.open(handleInfo);
        result.setAlignmentX(0.0f);

        return result;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(null);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
