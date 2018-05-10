/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.designer.ads.editors.xml;

import java.util.List;
import net.miginfocom.swing.MigLayout;
import org.radixware.kernel.common.enums.EIsoLanguage;

/**
 *
 * @author student
 */
public class DocImportOptionsPanel extends javax.swing.JPanel {

    private final List<EIsoLanguage> languages;
    private javax.swing.JComboBox comboBoxLang;
    private javax.swing.JCheckBox jChbRemove;
    private javax.swing.JCheckBox jChbReplace;
    private javax.swing.JLabel sinceTextLabel;
    private javax.swing.JLabel langTextLabel;
    private javax.swing.JTextField sinceEditor;

    public DocImportOptionsPanel(List<EIsoLanguage> languages) {
        this.languages = languages;
        initComponents();
    }

    private void initComponents() {
        langTextLabel = new javax.swing.JLabel("Default language:");
        comboBoxLang = new javax.swing.JComboBox(languages.toArray());
        if(languages.contains(EIsoLanguage.RUSSIAN)) {
            comboBoxLang.setSelectedItem(EIsoLanguage.RUSSIAN);
        }

        sinceTextLabel = new javax.swing.JLabel("Default \"Since\":");
        sinceEditor = new javax.swing.JTextField();

        jChbRemove = new javax.swing.JCheckBox("Remove records from the source document");
        jChbReplace = new javax.swing.JCheckBox("Replace existing documentation");

        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(7, 7, 7, 7));
        this.setLayout(new MigLayout("fillx", "[shrink][grow]", "[][][][]"));

        this.add(langTextLabel);
        this.add(comboBoxLang, "growx, wrap");
        this.add(sinceTextLabel);
        this.add(sinceEditor, "growx, wrap");
        this.add(jChbRemove, "span 2, wrap");
        this.add(jChbReplace, "span 2");
    }

    public EIsoLanguage getLanguage() {
        return (EIsoLanguage) comboBoxLang.getSelectedItem();
    }

    public boolean isRemoveNeeded() {
        return jChbRemove.isSelected();
    }

    public boolean isReplaceNeeded() {
        return jChbReplace.isSelected();
    }

    public String getVersion() {
        return sinceEditor.getText();
    }
}
