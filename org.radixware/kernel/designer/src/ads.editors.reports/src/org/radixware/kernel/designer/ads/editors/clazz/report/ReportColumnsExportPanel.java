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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import org.openide.DialogDescriptor;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsAbstractReportInfo;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;

public class ReportColumnsExportPanel extends JPanel {

    private final String EXPORT_COLUMN_NAMES_CHB_TEXT = "Export column names";
    private final String ROW_CONDITION_BTN_TEXT = "Row Condition";

    private AdsReportClassDef report;
    private AdsAbstractReportInfo reportInfo;

    private final JCheckBox exportColumnNamesChb = new JCheckBox(EXPORT_COLUMN_NAMES_CHB_TEXT);
    private final JButton rowConditionBtn = new JButton(ROW_CONDITION_BTN_TEXT);    

    public ReportColumnsExportPanel() { 
        exportColumnNamesChb.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                reportInfo.setIsExportColumnName(exportColumnNamesChb.isSelected());
            }
        });
        
        rowConditionBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                final AdsAbstractReportInfoEditor editor = new AdsAbstractReportInfoEditor(reportInfo);
                editor.open(new OpenInfo(report));
                final String title = "Row Visibility Condition";
                final ModalDisplayer md = new ModalDisplayer(editor, title, new Object[]{DialogDescriptor.CLOSED_OPTION}) {
                    @Override
                    protected void apply() {
                    }
                };
                md.showModal();
            }
        });
        
        this.setLayout(new MigLayout("fill", "[grow][shrink]", "[shrink]"));
        this.add(exportColumnNamesChb, "growx, shrinky");
        this.add(rowConditionBtn, "shrink");
    }

    public void open(final AdsReportClassDef report, final AdsAbstractReportInfo reportInfo) {
        this.report = report;
        this.reportInfo = reportInfo;
        
        exportColumnNamesChb.setSelected(reportInfo.isExportColumnName());
    }

    public boolean isExportColumnNames() {
        return exportColumnNamesChb.isSelected();
    }   
}
