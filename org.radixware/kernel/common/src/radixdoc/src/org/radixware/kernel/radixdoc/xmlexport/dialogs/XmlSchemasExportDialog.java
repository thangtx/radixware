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
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.prefs.Preferences;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.radixdoc.xmlexport.IExportableXmlSchema;
import org.radixware.kernel.radixdoc.xmlexport.XmlSchemasExportTask;

public class XmlSchemasExportDialog {

    private final static String DIALOG_TITLE = "Export XML Schemas";
    private final static String OK_BTN_TEXT = "OK";
    private final static String CANCEL_BTN_TEXT = "Cancel";

    private final static String XSD_EXPORT_PATH = "XsdExportPath";
    private final static Preferences prefs = Utils.findOrCreatePreferences(XSD_EXPORT_PATH);

    XmlSchemasExportTask result = null;

    private XmlSchemasExportPanel schemasExportPanel;

    private JDialog dialog;
    private Window parent;

    public XmlSchemasExportDialog(XmlSchemasExportDialogParameters parameters) {
        this(parameters.schemas, parameters.languages, parameters.topLayerVariants);
    }

    public XmlSchemasExportDialog(Collection<IExportableXmlSchema> schemas, List<EIsoLanguage> languages, Set<String> topLayerVariants) {
        this(null, schemas, languages, topLayerVariants);
    }

    public XmlSchemasExportDialog(Window parent, Collection<IExportableXmlSchema> schemas, List<EIsoLanguage> languages, Set<String> topLayerVariants) {
        this(parent, schemas, languages, topLayerVariants, true);
    }

    public XmlSchemasExportDialog(
            Window parent, 
            Collection<IExportableXmlSchema> schemas, 
            List<EIsoLanguage> languages, 
            Set<String> topLayerVariants,
            boolean needSelection
            ) {
        this.parent = parent;

        schemasExportPanel = new XmlSchemasExportPanel(schemas, languages, topLayerVariants, needSelection);
    }

    private ActionListener getOkBtnListener() {
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String xsdEsportPath = prefs.get(XSD_EXPORT_PATH, null);
                File dir = xsdEsportPath == null ? null : new File(xsdEsportPath);
                if (dir != null && !dir.exists()) {
                    dir = null;
                }

                JFileChooser chooser = new JFileChooser(dir);
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setDialogType(JFileChooser.SAVE_DIALOG);

                int returnVal = chooser.showSaveDialog(parent);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    dir = chooser.getSelectedFile();
                    prefs.put(XSD_EXPORT_PATH, dir.getAbsolutePath());
                    dialog.setVisible(false);
                } else {
                    return;
                }

                List<EIsoLanguage> langs = new ArrayList<>();
                langs.add(schemasExportPanel.getExportLanguage());

                result = new XmlSchemasExportTask(
                        schemasExportPanel.getSelectedSchemas(),
                        schemasExportPanel.isUseExtendedNames(),
                        schemasExportPanel.isEmbeddedDoc(),
                        schemasExportPanel.isSaveLinkedSchemas(),
                        schemasExportPanel.isZipSchemas(),
                        schemasExportPanel.isExportDoc(),
                        langs,
                        schemasExportPanel.getEmbeddedDocLanguages(),
                        dir
                );
            }
        };
    }

    private ActionListener getCancelBtnListener() {
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                result = null;
                dialog.setVisible(false);
            }
        };
    }

    public void setParent(Window parent) {
        this.parent = parent;
    }

    public String getTopLayerUri() {
        return schemasExportPanel.getTopLayerUri();
    }
    
    public XmlSchemasExportTask show() {
        JButton cancelBtn = new JButton(CANCEL_BTN_TEXT);
        cancelBtn.addActionListener(getCancelBtnListener());

        JButton okBtn = new JButton(OK_BTN_TEXT);
        okBtn.setMinimumSize(cancelBtn.getMinimumSize());
        okBtn.addActionListener(getOkBtnListener());

        JPanel buttonsPanel = new JPanel(new MigLayout("fill, alignx right", "[grow][shrink][shrink]", "[shrink]"));
        buttonsPanel.add(okBtn, "cell 1 0, grow");
        buttonsPanel.add(cancelBtn, "cell 2 0, grow");

        JPanel mainPanel = new JPanel(new MigLayout("fill", "[grow]", "[shrink][shrink]"));
        mainPanel.add(schemasExportPanel, "growx, shrinky, wrap");
        mainPanel.add(buttonsPanel, "growx, shrinky, wrap push");

        dialog = new JDialog(parent, DIALOG_TITLE);
        dialog.add(mainPanel);
        dialog.pack();
        dialog.setModal(true);
        dialog.setMinimumSize(new Dimension(dialog.getWidth(), dialog.getHeight() / 2 + buttonsPanel.getMinimumSize().height));
        dialog.setLocationRelativeTo(null);

        if (!schemasExportPanel.isNeedSelection()) {
            dialog.setResizable(false);
        }

        dialog.setVisible(true);

        return result;
    }
}
