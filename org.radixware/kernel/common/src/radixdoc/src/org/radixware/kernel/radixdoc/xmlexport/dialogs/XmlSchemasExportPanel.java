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
package org.radixware.kernel.radixdoc.xmlexport.dialogs;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import org.radixware.kernel.common.dialogs.chooseobject.ChooseObjectsPanel;
import org.radixware.kernel.common.dialogs.chooseobject.ISelectableObject;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.radixdoc.xmlexport.IExportableXmlSchema;

public class XmlSchemasExportPanel extends JPanel {

    private static final String LINKED_SCHEMAS_CHB_TEXT = "Linked Schemas";
    private static final String EMBEDDED_DOC_CHB_TEXT = "Embedded Documentation";
    private static final String EXPORT_DOC_CHB_TEXT = "Export Documentation";
    private static final String ZIP_SCHEMAS_CHB_TEXT = "Pack Schemas to Zip";
    private static final String USE_EXTENDED_NAMES_CHB_TEXT = "Use Extended File Names";

    private static final String TOP_LAYER_LABEL_TXT = "Top Layer URI: ";
    private static final String LANG_LABEL_TXT = "Documentation Language: ";
    private static final String EMBEDDED_DOC_LANG_LABEL_TXT = "Embedded Documentation Language: ";

    private static final String[] DEFAULT_TOP_LAYER = {"org.radixware"};

    private final Collection<IExportableXmlSchema> schemas;
    private final List<EIsoLanguage> languages;
    private final Set<String> topLayerVariants;
    private final boolean needSelection;

    private ChooseObjectsPanel chooseSchemasPanel;

    private final JCheckBox linkedSchemasChb = new JCheckBox(LINKED_SCHEMAS_CHB_TEXT);
    private final JCheckBox zipSchemasChb = new JCheckBox(ZIP_SCHEMAS_CHB_TEXT);
    private final JCheckBox embeddedDocChb = new JCheckBox(EMBEDDED_DOC_CHB_TEXT);
    private final JCheckBox exportDocChb = new JCheckBox(EXPORT_DOC_CHB_TEXT);
    private final JCheckBox useExtendedFileNamesChb = new JCheckBox(USE_EXTENDED_NAMES_CHB_TEXT);

    private final JLabel langLabel = new JLabel(LANG_LABEL_TXT);
    private final JLabel embeddedDocLangLabel = new JLabel(EMBEDDED_DOC_LANG_LABEL_TXT);
    private final JLabel topLayerLabel = new JLabel(TOP_LAYER_LABEL_TXT);

    private JComboBox<EIsoLanguage> exportLanguagesCmbBx;
    private JComboBox<String> topLayerCmbBx;
    private JComboBox<EmbeddedDocLangCmbBxElement> embeddedDocLangCmbBx;

    private JPanel langPanel;
    private JPanel embeddedDocLangPanel;
    private JPanel topLayerPanel;

    public XmlSchemasExportPanel(Collection<IExportableXmlSchema> schemas, List<EIsoLanguage> languages, Set<String> topLayerVariants, boolean needSelection) {
        this.schemas = new ArrayList<>(schemas);
        this.languages = new ArrayList<>(languages);
        this.topLayerVariants = new HashSet<>(topLayerVariants);
        this.needSelection = needSelection;
        initComponents();
    }

    private void initComponents() {
        Set<ISelectableObject> selectableSchemas = new HashSet<>();
        for (IExportableXmlSchema schema : schemas) {
            selectableSchemas.add(schema);
        }

        chooseSchemasPanel = new ChooseObjectsPanel(selectableSchemas, "Xml Schema");
        chooseSchemasPanel.setVisible(needSelection);

        Collections.sort(languages);
        exportLanguagesCmbBx = new JComboBox<>(languages.toArray(new EIsoLanguage[languages.size()]));
        exportLanguagesCmbBx.setMinimumSize(new Dimension(100, 10));

        langPanel = new JPanel(new MigLayout("", "[shrink][shrink]", "[shrink]"));
        langPanel.add(langLabel, "shrink");
        langPanel.add(exportLanguagesCmbBx, "shrink");
        langPanel.setVisible(false);

        zipSchemasChb.setEnabled(false);

        exportDocChb.addActionListener(getExportDocChbListener());

        embeddedDocLangCmbBx = new JComboBox<>(getEmbeddedDocLangCmbBxElements(languages));
        embeddedDocLangCmbBx.setMinimumSize(new Dimension(100, 10));

        embeddedDocLangPanel = new JPanel(new MigLayout("", "[shrink][shrink]", "[shrink]"));
        embeddedDocLangPanel.add(embeddedDocLangLabel, "shrink");
        embeddedDocLangPanel.add(embeddedDocLangCmbBx, "shrink");
        embeddedDocLangPanel.setEnabled(false);
        embeddedDocLangLabel.setEnabled(false);
        embeddedDocLangCmbBx.setEnabled(false);

        topLayerCmbBx = topLayerVariants == null ? new JComboBox<>(DEFAULT_TOP_LAYER) : new JComboBox<>(topLayerVariants.toArray(new String[topLayerVariants.size()]));
        exportLanguagesCmbBx.setMinimumSize(new Dimension(100, 10));

        topLayerPanel = new JPanel(new MigLayout("", "[shrink][shrink]", "[shrink]"));
        topLayerPanel.add(topLayerLabel, "shrink");
        topLayerPanel.add(topLayerCmbBx, "shrink");
        topLayerPanel.setVisible(false);

        embeddedDocChb.addActionListener(getEmbeddedDocChbListener());

        linkedSchemasChb.addActionListener(getLinkedSchemasChbListner());

        useExtendedFileNamesChb.setEnabled(true);
        useExtendedFileNamesChb.setVisible(false);

        JPanel optionsPanel = new JPanel(new MigLayout("fill", "[grow]", "[shrink][shrink][shrink][shrink][shrink][shrink][shrink][shrink]"));
        optionsPanel.add(useExtendedFileNamesChb, "growx, shrinky, hidemode 2, wrap");
        optionsPanel.add(linkedSchemasChb, "growx, shrinky, wrap");
        optionsPanel.add(zipSchemasChb, "growx, shrinky, wrap");
        optionsPanel.add(embeddedDocChb, "growx, shrinky, wrap");
        optionsPanel.add(embeddedDocLangPanel, "growx, shrinky, hidemode 2, wrap");
        optionsPanel.add(exportDocChb, "growx, shrinky, wrap");
        optionsPanel.add(topLayerPanel, "growx, shrinky, wrap");
        optionsPanel.add(langPanel, "growx, shrinky, wrap push");
        optionsPanel.setBorder(chooseSchemasPanel.getBorder());

        this.setLayout(new MigLayout("fill, hidemode 2", "[grow]", "[grow][shrink]"));
        this.add(chooseSchemasPanel, "grow, wrap");
        this.add(optionsPanel, "grow, wrap push");
    }

    public void update() {
        chooseSchemasPanel.update();
    }

    public ActionListener getExportDocChbListener() {
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                langPanel.setVisible(exportDocChb.isSelected());
                if (topLayerVariants != null && topLayerVariants.size() > 1) {
                    topLayerPanel.setVisible(exportDocChb.isSelected());
                }
            }
        };
    }

    public ActionListener getEmbeddedDocChbListener() {
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                embeddedDocLangPanel.setEnabled(embeddedDocChb.isSelected());
                embeddedDocLangLabel.setEnabled(embeddedDocChb.isSelected());
                embeddedDocLangCmbBx.setEnabled(embeddedDocChb.isSelected());
            }
        };
    }

    public ActionListener getLinkedSchemasChbListner() {
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                zipSchemasChb.setEnabled(linkedSchemasChb.isSelected());
            }
        };
    }

    public boolean isEmbeddedDoc() {
        return embeddedDocChb.isSelected();
    }

    public boolean isSaveLinkedSchemas() {
        return linkedSchemasChb.isSelected();
    }

    public boolean isExportDoc() {
        return exportDocChb.isSelected();
    }

    public boolean isZipSchemas() {
        return zipSchemasChb.isEnabled() && zipSchemasChb.isSelected();
    }

    public boolean isUseExtendedNames() {
        return useExtendedFileNamesChb.isSelected();
    }

    public boolean isNeedSelection() {
        return needSelection;
    }

    public Collection<IExportableXmlSchema> getSelectedSchemas() {
        Set<IExportableXmlSchema> result;
        if (needSelection) {
            result = new HashSet<>();
            for (ISelectableObject obj : chooseSchemasPanel.getSelectedObjects()) {
                result.add((IExportableXmlSchema) obj);
            }
        } else {
            result = new HashSet<>(schemas);
        }

        return result;
    }

    public EIsoLanguage getExportLanguage() {
        return (EIsoLanguage) exportLanguagesCmbBx.getSelectedItem();
    }

    public List<EIsoLanguage> getEmbeddedDocLanguages() {
        return ((EmbeddedDocLangCmbBxElement) embeddedDocLangCmbBx.getSelectedItem()).getLangs();
    }

    public String getTopLayerUri() {
        return (String) topLayerCmbBx.getSelectedItem();
    }

    private static class EmbeddedDocLangCmbBxElement {

        private final List<EIsoLanguage> langs;

        public EmbeddedDocLangCmbBxElement(EIsoLanguage lang) {
            langs = new ArrayList<>();
            langs.add(lang);
        }

        public EmbeddedDocLangCmbBxElement(List<EIsoLanguage> langs) {
            this.langs = new ArrayList<>(langs);
        }

        public List<EIsoLanguage> getLangs() {
            return new ArrayList<>(langs);
        }

        @Override
        public String toString() {
            return langs.size() > 1 ? "All Laguages" : langs.get(0).toString();
        }
    }

    private EmbeddedDocLangCmbBxElement[] getEmbeddedDocLangCmbBxElements(List<EIsoLanguage> langs) {
        int resultSize = langs.size() > 1 ? langs.size() + 1 : langs.size();
        EmbeddedDocLangCmbBxElement[] result = new EmbeddedDocLangCmbBxElement[resultSize];

        int currentIndex = 0;
        if (langs.size() > 1) {
            result[currentIndex++] = new EmbeddedDocLangCmbBxElement(langs);
        }

        for (EIsoLanguage lang : langs) {
            result[currentIndex++] = new EmbeddedDocLangCmbBxElement(lang);
        }

        return result;
    }
}
