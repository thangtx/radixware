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

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.miginfocom.swing.MigLayout;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportColumnDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportFormat;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel;
import org.radixware.kernel.designer.common.dialogs.components.localizing.HandleInfo;
import org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingEditorPanel;
import org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingStringContextFactory;
import org.radixware.kernel.designer.common.dialogs.components.localizing.ProxyLocalizingStringContext;
import org.radixware.kernel.designer.common.dialogs.components.state.StateAbstractDialog;
import org.radixware.kernel.designer.common.dialogs.components.state.StateDisplayer;
import org.radixware.kernel.designer.common.dialogs.components.state.StateManager;

public class ReportColumnEditorPanel extends StateAbstractDialog.StateAbstractPanel {

    private static final String ID_LBL_TEXT = "Id:";
    private static final String NAME_LBL_TEXT = "Name:";
    private static final String PROPERTY_LBL_TEXT = "Property:";
    private static final String CSV_FORMAT_LBL_TEXT = "CSV Export Format:";
    private static final String LEGACY_CSV_NAME_LBL_TEXT = "Legacy CSV Name:";
    private static final String CSV_PARAMETERS_GROUP_TEXT = "CSV Export Parameters";
    private static final String XLSX_PARAMETERS_GROUP_TEXT = "XLSX Export Parameters";

    private AdsPropertyDef currentProperty;
    private boolean isLocalizingStringsModified = false;

    private final AdsReportClassDef report;
    private final AdsReportColumnDef column;
    private final AdsReportColumnDef original;
    private final AdsReportFormat csvFormat;

    private final HandleInfo titleHandleInfo = new HandleInfo() {
        @Override
        public AdsDefinition getAdsDefinition() {
            return report;
        }

        @Override
        public Id getTitleId() {
            return column.getTitleId();
        }

        @Override
        protected void onAdsMultilingualStringDefChange(IMultilingualStringDef stringDef) {
            if (stringDef != null) {
                column.setTitleId(stringDef.getId());
            } else {
                column.setTitleId(null);
            }
        }

        @Override
        protected void onLanguagesPatternChange(EIsoLanguage language, String newStringValue) {
            IMultilingualStringDef stringDef = getAdsMultilingualStringDef();
            if (stringDef != null) {
                stringDef.setValue(language, newStringValue);
            }
        }
    };
    private final ProxyLocalizingStringContext titleLocalizingContext;

    private final JLabel legacyCsvNameLbl = new JLabel(LEGACY_CSV_NAME_LBL_TEXT);
    private final JLabel legacyCsvNameVal = new JLabel();

    private final JTextField nameTextField = new JTextField();
    private final DefinitionLinkEditPanel propertyEditor = new DefinitionLinkEditPanel();
    private final LocalizingEditorPanel titleEditor = new LocalizingEditorPanel();
    private final ReportColumnXlsxExportParametersPanel xlsxParameters;
    private final CellFormatEditor csvFormatEditor;
    private final StateDisplayer stateDisplayer = new StateDisplayer();

    public ReportColumnEditorPanel(AdsReportClassDef report, AdsReportColumnDef original, List<AdsPropertyDef> usedProperties) {
        this.report = report;
        this.original = original;
        this.column = new AdsReportColumnDef(original);
        this.csvFormat = column.getCsvExportFormat();

        stateManager = new StateManager(this);
        csvFormatEditor = new CellFormatEditor(csvFormat, "CSV Export Format", report.isReadOnly());
        xlsxParameters = new ReportColumnXlsxExportParametersPanel(column, report.isReadOnly());

        currentProperty = AdsReportClassDef.ReportUtils.findProperty(report, column.getPropertyId());

        this.titleLocalizingContext = (ProxyLocalizingStringContext) LocalizingStringContextFactory.newProxyInstance(titleHandleInfo);

        setupVerifiers();
        initComponents(usedProperties);
    }

    public AdsReportColumnDef getColumn() {
        saveLocalizedStrings();
        return column;
    }

    private void initComponents(final List<AdsPropertyDef> usedProperties) {
        JTextField idTextField = new JTextField(column.getId().toString());
        idTextField.setEditable(false);
        
        nameTextField.getDocument().addDocumentListener(getNameFieldListener());
        nameTextField.addFocusListener(getNameTextFieldFocusListener());
        nameTextField.setText(column.getName());
        if (AdsReportColumnDef.UNDEFINED_NAME.equals(column.getName())) {
            nameTextField.setForeground(Color.GRAY);
        }

        List<AdsPropertyDef> reportProperties = report.getProperties().get(EScope.LOCAL);
        List<AdsPropertyDef> availableProperties = ReportColumnEditorUtils.getPropertiesForEditor(reportProperties, usedProperties);
        if (currentProperty != null && !availableProperties.contains(currentProperty)) {
            availableProperties.add(0, currentProperty);
        }

        propertyEditor.setComboMode();
        propertyEditor.setComboBoxValues(availableProperties, true);
        propertyEditor.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                column.setPropertyId(propertyEditor.getDefinitionId());
                AdsPropertyDef property = (AdsPropertyDef) propertyEditor.getDefinition();
                if (property != null) {
                    String name = nameTextField.getText();
                    if (!ReportColumnEditorUtils.isNameDefined(name) || (currentProperty != null && name.equals(currentProperty.getName()))) {
                        nameTextField.setText(property.getName());
                    }
                    csvFormatEditor.setType(property.getValue().getType().getTypeId());

                    boolean isExportEnabled = report.getCsvInfo() == null ? false : report.getCsvInfo().isExportToCsvEnabled();
                    csvFormatEditor.setReadOnly(!isExportEnabled);

                    isExportEnabled = report.getXlsxReportInfo() == null ? false : report.getXlsxReportInfo().isExportToXlsxEnabled();
                    xlsxParameters.setReadOnly(!isExportEnabled);

                } else {
                    csvFormatEditor.setType(null);
                    csvFormatEditor.setReadOnly(true);
                    xlsxParameters.setReadOnly(true);
                }
                currentProperty = property;
            }
        });
        propertyEditor.open(currentProperty, column.getPropertyId());

        legacyCsvNameLbl.setForeground(Color.red);
        legacyCsvNameVal.setForeground(Color.red);
        if (column.getLegacyCsvName() != null && column.getTitleId() == null) {
            legacyCsvNameVal.setText(column.getLegacyCsvName());

            legacyCsvNameVal.setVisible(true);
            legacyCsvNameLbl.setVisible(true);
        } else {
            legacyCsvNameLbl.setVisible(false);
            legacyCsvNameVal.setVisible(false);
        }

        titleEditor.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if (!Utils.emptyOrNull(column.getLegacyCsvName())) {
                    legacyCsvNameLbl.setVisible(!titleLocalizingContext.hasValue());
                    legacyCsvNameVal.setVisible(!titleLocalizingContext.hasValue());
                }

                isLocalizingStringsModified = true;
            }
        });
        titleEditor.open(titleLocalizingContext);

        if (currentProperty != null) {
            csvFormatEditor.setType(currentProperty.getValue().getType().getTypeId());

            boolean isExportEnabled = report.getCsvInfo() == null ? false : report.getCsvInfo().isExportToCsvEnabled();
            csvFormatEditor.setReadOnly(!isExportEnabled);

            isExportEnabled = report.getXlsxReportInfo() == null ? false : report.getXlsxReportInfo().isExportToXlsxEnabled();
            xlsxParameters.setReadOnly(!isExportEnabled);
        } else {
            csvFormatEditor.setType(null);
            csvFormatEditor.setReadOnly(true);
            xlsxParameters.setReadOnly(true);
        }

        JLabel idLabel = new JLabel(ID_LBL_TEXT);
        JLabel nameLabel = new JLabel(NAME_LBL_TEXT);
        JLabel propertyLabel = new JLabel(PROPERTY_LBL_TEXT);
        JLabel csvFormatLabel = new JLabel(CSV_FORMAT_LBL_TEXT);

        this.setLayout(new MigLayout("fillx, hidemode 2", "[shrink][grow]", "[][][][][][][][]"));        
        this.add(nameLabel, "shrinkx, shrinky");
        this.add(nameTextField, "growx, shrinky, wrap");
        this.add(idLabel, "shrinkx, shrinky");
        this.add(idTextField, "growx, shrinky, wrap");
        this.add(propertyLabel, "shrinkx, shrinky");
        this.add(propertyEditor, "growx, shrinky, wrap");
        this.add(legacyCsvNameLbl, "growx, shrinky");
        this.add(legacyCsvNameVal, "growx, shrinky, wrap");
        this.add(titleEditor, "growx, shrinky, spanx 2, wrap");

        JPanel csvParemetersPanel = new JPanel(new MigLayout("fillx", "[shrink][grow]", "[]"));
        csvParemetersPanel.setBorder(BorderFactory.createTitledBorder(CSV_PARAMETERS_GROUP_TEXT));
        csvParemetersPanel.add(csvFormatLabel, "shrinkx, shrinky");
        csvParemetersPanel.add(csvFormatEditor, "growx, shrinky");

        JPanel xlsxParemetersPanel = new JPanel(new MigLayout("fillx", "[]", "[]"));
        xlsxParemetersPanel.setBorder(BorderFactory.createTitledBorder(XLSX_PARAMETERS_GROUP_TEXT));
        xlsxParemetersPanel.add(xlsxParameters, "growx, shrinky, spanx 2");

        this.add(csvParemetersPanel, "growx, shrinky, spanx 2, wrap");
        this.add(xlsxParemetersPanel, "growx, shrinky, spanx 2, wrap");
        this.add(stateDisplayer, "growx, shrinky, spanx 2");
    }

    private DocumentListener getNameFieldListener() {
        return new DocumentListener() {
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
                check();
            }
        };
    }

    private FocusListener getNameTextFieldFocusListener() {
        return new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                if (nameTextField.getText().equals(AdsReportColumnDef.UNDEFINED_NAME)) {
                    nameTextField.setText("");
                    nameTextField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (nameTextField.getText().isEmpty()) {
                    nameTextField.setForeground(Color.GRAY);
                    nameTextField.setText(AdsReportColumnDef.UNDEFINED_NAME);
                }
            }
        };
    }

    @Override
    public void check() {
        checkName();
    }

    private void checkName() {
        String name = nameTextField.getText();

        if (!ReportColumnEditorUtils.isNameDefined(name)) {
            stateManager.error("Column name is undefined");
            changeSupport.fireChange();
            return;
        }

        if (!RadixObjectsUtils.isCorrectName(name)) {
            stateManager.error("Column name is incorect");
            changeSupport.fireChange();
            return;
        }

        for (AdsReportColumnDef reportColumn : report.getColumns()) {
            if (reportColumn != original && reportColumn.getName().equals(name)) {
                stateManager.error("Column name must be unique");
                changeSupport.fireChange();
                return;
            }
        }

        stateManager.ok();
        changeSupport.fireChange();
        column.setName(name);
    }

    private void setupVerifiers() {
        nameTextField.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                return ReportColumnEditorUtils.isNameDefined(nameTextField.getText());
            }
        });
    }

    private void saveLocalizedStrings() {
        if (isLocalizingStringsModified) {
            titleLocalizingContext.commit();
        }
    }
}
