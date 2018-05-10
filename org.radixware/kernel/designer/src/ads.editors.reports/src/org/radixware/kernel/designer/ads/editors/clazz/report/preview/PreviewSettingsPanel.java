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
package org.radixware.kernel.designer.ads.editors.clazz.report.preview;

import org.radixware.kernel.designer.ads.common.reports.ReportPreviewSettings;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.JTextComponent;
import net.miginfocom.swing.MigLayout;
import org.radixware.kernel.common.components.ExtendableTextField;
import org.radixware.kernel.common.defs.ads.build.BuildOptions;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.enums.EIsoCountry;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.EReportExportFormat;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.utils.Utils;

public class PreviewSettingsPanel extends JPanel {

    private static final String AAS_ADDRESS_LBL_TEXT = "AAS address:";
    private static final String AAS_REQUEST_TIMEOUT_LBL_TEXT = "AAS request timeout (sec):";
    private static final String EXPORT_LANGUAGE_LBL_TEXT = "Export language:";
    private static final String EXPORT_REGION_LBL_TEXT = "Export region:";
    private static final String EXPORT_FORMAT_LBL_TEXT = "Export format:";
    private static final String TEST_DATA_FOLDER_LBL_TEXT = "Test data folder:";

    private static final boolean IS_USER_MODE = BuildOptions.UserModeHandlerLookup.getUserModeHandler() != null;

    private static final int TIMEOUT_SEC_DEFAULT = 10;
    private static final int TIMEOUT_SEC_MIN = 1;
    private static final int TIMEOUT_SEC_MAX = 3600 * 3;
    private static final int TIMEOUT_SEC_STEP = 1;

    private static final String DEFAULT_COMBO_BOX_ITEM = "<default>";

    private final JButton compileAndPreviewBtn = new JButton();
    private final JButton previewBtn = new JButton();

    private PreviewSettingsTextField aasAddressEditor;
    private PreviewSettingsTextField testDataFolderPathEditor;

    private final DocumentListener editorsDocumentListener = new DocumentListener() {
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
            compileAndPreviewBtn.setEnabled(isButtonsEnabled());
            previewBtn.setEnabled(isButtonsEnabled());
        }
    };

    private final AdsReportClassDef report;

    public PreviewSettingsPanel(AdsReportClassDef report) {
        this.report = report;
        initComponents();
    }

    public void update() {
        this.removeAll();
        initComponents();
    }

    private void initComponents() {
        JLabel aasAddressLbl = new JLabel(AAS_ADDRESS_LBL_TEXT);
        JLabel aasRequestTimeoutLbl = new JLabel(AAS_REQUEST_TIMEOUT_LBL_TEXT);
        JLabel exportLanguageLbl = new JLabel(EXPORT_LANGUAGE_LBL_TEXT);
        JLabel exportRegionLbl = new JLabel(EXPORT_REGION_LBL_TEXT);
        JLabel exportFormatLbl = new JLabel(EXPORT_FORMAT_LBL_TEXT);
        JLabel testDataFolderLbl = new JLabel(TEST_DATA_FOLDER_LBL_TEXT);

        createAasAddressEditor();

        JSpinner aasRequestTimeoutEditor = createAasRequestTimeoutEditor();

        JComboBox<String> exportRegionEditor = createExportRegionEditor();
        JComboBox<String> exportLanguageEditor = createExportLanguageEditor(exportRegionEditor);

        JComboBox<String> exportFormatEditor = createExportFormatEditor();

        ExtendableTextField testDataFolderChooser = createTestDataFolderChooser();

        JPanel buttonsPanel = createButtonsPanel();

        this.setLayout(new MigLayout("", "[shrink][grow][shrink][grow]", "[shrink][shrink][shrink][shrink][shrink]"));
        this.add(aasAddressLbl, "shrinkx");
        this.add(aasAddressEditor, "growx");
        this.add(aasRequestTimeoutLbl, "shrinkx");
        this.add(aasRequestTimeoutEditor, "growx, wrap");

        this.add(exportLanguageLbl, "shrinkx");
        this.add(exportLanguageEditor, "growx");
        this.add(exportRegionLbl, "shrinkx");
        this.add(exportRegionEditor, "growx, wrap");

        this.add(exportFormatLbl, "shrinkx");
        this.add(exportFormatEditor, "growx, span 3, wrap");

        this.add(testDataFolderLbl, "shrinkx");
        this.add(testDataFolderChooser, "growx, span 3, wrap");

        this.add(buttonsPanel, "growx, shrinky, span 4");
    }

    private void createAasAddressEditor() {
        aasAddressEditor = new PreviewSettingsTextField(ReportPreviewSettings.AAS_ADDRESS, report.getId());
        aasAddressEditor.setPlaceholderText("host:port");
        aasAddressEditor.getDocument().addDocumentListener(editorsDocumentListener);
    }

    private JSpinner createAasRequestTimeoutEditor() {
        int initialTimeout;
        if (!Utils.emptyOrNull(ReportPreviewSettings.AAS_REQUEST_TIMEOUT.get(report.getId()))) {
            initialTimeout = Integer.valueOf(ReportPreviewSettings.AAS_REQUEST_TIMEOUT.get(report.getId()));
        } else {
            initialTimeout = TIMEOUT_SEC_DEFAULT;
            ReportPreviewSettings.AAS_REQUEST_TIMEOUT.set(report.getId(), String.valueOf(initialTimeout));
        }

        final JSpinner aasRequestTimeoutEditor = new JSpinner(
                new SpinnerNumberModel(initialTimeout, TIMEOUT_SEC_MIN, TIMEOUT_SEC_MAX, TIMEOUT_SEC_STEP)
        );
        JSpinner.NumberEditor spinnerEditor = new JSpinner.NumberEditor(aasRequestTimeoutEditor);
        ((DefaultFormatter) spinnerEditor.getTextField().getFormatter()).setCommitsOnValidEdit(true);
        aasRequestTimeoutEditor.setEditor(spinnerEditor);
        aasRequestTimeoutEditor.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Integer timeoutVal = (Integer) aasRequestTimeoutEditor.getValue();
                ReportPreviewSettings.AAS_REQUEST_TIMEOUT.set(report.getId(), timeoutVal.toString());
            }
        });

        return aasRequestTimeoutEditor;
    }

    private JComboBox<String> createExportFormatEditor() {
        final JComboBox<String> exportFormatEditor = new JComboBox<>(getAvailableFormats());

        String initialFormat;
        if (!Utils.emptyOrNull(ReportPreviewSettings.EXPORT_FORMAT.get(report.getId()))) {
            initialFormat = ReportPreviewSettings.EXPORT_FORMAT.get(report.getId());
        } else {
            initialFormat = EReportExportFormat.PDF.getName();
            ReportPreviewSettings.EXPORT_FORMAT.set(report.getId(), initialFormat);
        }

        exportFormatEditor.setSelectedItem(initialFormat);
        exportFormatEditor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String exportFormat = (String) exportFormatEditor.getSelectedItem();
                ReportPreviewSettings.EXPORT_FORMAT.set(report.getId(), exportFormat);
            }
        });

        return exportFormatEditor;
    }

    private ExtendableTextField createTestDataFolderChooser() {
        testDataFolderPathEditor = new PreviewSettingsTextField(ReportPreviewSettings.TEST_DATA_FOLDER_PATH, report.getId());
        testDataFolderPathEditor.getDocument().addDocumentListener(editorsDocumentListener);

        ExtendableTextField testDataFolderChooser = new ExtendableTextField();
        testDataFolderChooser.setEditor(testDataFolderPathEditor);

        JButton chooseTestDataFolderBtn = testDataFolderChooser.addButton(RadixWareIcons.DIALOG.CHOOSE.getIcon());
        chooseTestDataFolderBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String initialPath = ReportPreviewSettings.TEST_DATA_FOLDER_PATH.get(report.getId());
                File dir = initialPath == null ? null : new File(initialPath);
                if (dir != null && !dir.exists()) {
                    dir = null;
                }

                JFileChooser chooser = new JFileChooser(dir);
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setDialogType(JFileChooser.SAVE_DIALOG);

                if (chooser.showSaveDialog(PreviewSettingsPanel.this) == JFileChooser.APPROVE_OPTION) {
                    String testDataFolderPath = chooser.getSelectedFile().getAbsolutePath();

                    ReportPreviewSettings.TEST_DATA_FOLDER_PATH.set(report.getId(), testDataFolderPath);
                    testDataFolderPathEditor.setText(testDataFolderPath);
                }
            }
        });

        return testDataFolderChooser;
    }

    private JComboBox<String> createExportRegionEditor() {
        final JComboBox<String> exportRegionsEditor = new JComboBox<>(getRegionsArray());
        exportRegionsEditor.insertItemAt(DEFAULT_COMBO_BOX_ITEM, 0);

        String initialRegion;
        if (Utils.emptyOrNull(ReportPreviewSettings.EXPORT_REGION.get(report.getId()))) {
            initialRegion = DEFAULT_COMBO_BOX_ITEM;
        } else {
            initialRegion = ReportPreviewSettings.EXPORT_REGION.get(report.getId());
        }
        exportRegionsEditor.setSelectedItem(initialRegion);

        exportRegionsEditor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newVal = exportRegionsEditor.getSelectedItem().toString();
                ReportPreviewSettings.EXPORT_REGION.set(report.getId(), DEFAULT_COMBO_BOX_ITEM.equals(newVal) ? "" : newVal);
            }
        });

        JTextComponent regionsEditor = (JTextComponent) exportRegionsEditor.getEditor().getEditorComponent();
        regionsEditor.getDocument().addDocumentListener(new DocumentListener() {
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
                String newVal = exportRegionsEditor.getEditor().getItem().toString();
                ReportPreviewSettings.EXPORT_REGION.set(report.getId(), DEFAULT_COMBO_BOX_ITEM.equals(newVal) ? "" : newVal);
            }
        });

        exportRegionsEditor.setEditable(true);
        exportRegionsEditor.setEnabled(!Utils.emptyOrNull(ReportPreviewSettings.EXPORT_LANGUAGE.get(report.getId())));

        return exportRegionsEditor;
    }

    private JComboBox<String> createExportLanguageEditor(final JComboBox<String> exportRegionEditor) {
        final JComboBox<String> exportLanguagesEditor = new JComboBox<>(getLanguagesArray());
        exportLanguagesEditor.insertItemAt(DEFAULT_COMBO_BOX_ITEM, 0);

        String initialLanguage;
        if (Utils.emptyOrNull(ReportPreviewSettings.EXPORT_LANGUAGE.get(report.getId()))) {
            initialLanguage = DEFAULT_COMBO_BOX_ITEM;
        } else {
            initialLanguage = ReportPreviewSettings.EXPORT_LANGUAGE.get(report.getId());
        }
        exportLanguagesEditor.setSelectedItem(initialLanguage);

        exportLanguagesEditor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newVal = exportLanguagesEditor.getSelectedItem().toString();
                ReportPreviewSettings.EXPORT_LANGUAGE.set(report.getId(), DEFAULT_COMBO_BOX_ITEM.equals(newVal) ? "" : newVal);

                exportRegionEditor.setEnabled(!DEFAULT_COMBO_BOX_ITEM.equals(newVal));
            }
        });

        return exportLanguagesEditor;
    }

    private String[] getAvailableFormats() {
        List<String> availableFormatsList = new ArrayList<>();
        for (EReportExportFormat format : EReportExportFormat.values()) {
            switch (format) {
                case XLSX:
                    if (report.getXlsxReportInfo() == null || !report.getXlsxReportInfo().isExportToXlsxEnabled()) {
                        break;
                    } else {
                        availableFormatsList.add(format.getName());
                        break;
                    }
                case CSV:
                    if (report.getCsvInfo() == null || !report.getCsvInfo().isExportToCsvEnabled()) {
                        break;
                    } else {
                        availableFormatsList.add(format.getName());
                        break;
                    }
                case TXT:
                    if (report.getForm() == null || !report.getForm().isSupportsTxt()) {
                        break;
                    } else {
                        availableFormatsList.add(format.getName());
                        break;
                    }
                case OOXML:
                    availableFormatsList.add("XLS");
                case MSDL:
                case CUSTOM:
                    break;
                default:
                    availableFormatsList.add(format.getName());
            }
        }

        return availableFormatsList.toArray(new String[availableFormatsList.size()]);
    }

    private String[] getRegionsArray() {
        List<String> regions = new ArrayList<>();
        for (EIsoCountry country : EIsoCountry.values()) {
            regions.add(country.getValue());
        }

        Collections.sort(regions);
        return regions.toArray(new String[regions.size()]);
    }

    private String[] getLanguagesArray() {
        Set<String> languages = new HashSet<>();
        Layer reportLayer = report.getLayer();

        if (report.getBranch() != null) {
            for (Layer layer : report.getBranch().getLayers()) {
                if (layer == reportLayer || (layer.getBaseLayerURIs().contains(reportLayer.getURI()) && layer.isLocalizing())) {
                    for (EIsoLanguage language : layer.getLanguages()) {
                        languages.add(language.getValue());
                    }
                }
            }
        }

        List<String> languagesList = new ArrayList<>(languages);
        Collections.sort(languagesList);
        return languages.toArray(new String[languages.size()]);
    }

    private JPanel createButtonsPanel() {
        previewBtn.setAction(ReportPreviewActionsProvider.getPreviewAction(report));
        previewBtn.setEnabled(isButtonsEnabled());

        compileAndPreviewBtn.setAction(ReportPreviewActionsProvider.getCompileAndPreviewAction(report));
        compileAndPreviewBtn.setEnabled(isButtonsEnabled());

        JPanel buttonsPanel = new JPanel(new MigLayout("fill", "[grow][][][grow]", "[]"));
        buttonsPanel.add(compileAndPreviewBtn, "shrink, cell 2 1");
        buttonsPanel.add(previewBtn, "shrink, cell 3 1");

        return buttonsPanel;
    }

    private boolean isButtonsEnabled() {
        return (!aasAddressEditor.isEmpty() || IS_USER_MODE) && !testDataFolderPathEditor.isEmpty();
    }
}
