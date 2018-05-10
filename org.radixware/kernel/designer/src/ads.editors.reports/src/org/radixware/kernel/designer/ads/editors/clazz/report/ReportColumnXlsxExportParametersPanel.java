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
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.miginfocom.swing.MigLayout;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportColumnDef;
import org.radixware.kernel.common.enums.EReportColumnResizeMode;
import org.radixware.kernel.designer.common.dialogs.components.CheckedNumberSpinnerEditor;

public class ReportColumnXlsxExportParametersPanel extends JPanel {
    private static final String XLSX_FORMAT_LBL_TEXT = "XSLX Export Format:";
    private static final String RESIZE_MODE_LBL_TEXT = "Column size:";
    private static final String WIDTH_LBL_TEXT = "Width:";

    private final AdsReportColumnDef column;

    private final XlsxFormatEditor xlsxFormatField;
    private final JComboBox<EReportColumnResizeMode> resizeModeComboBox = new JComboBox<>(EReportColumnResizeMode.values());
    private final JSpinner widthSpinner = new JSpinner();

    public ReportColumnXlsxExportParametersPanel(AdsReportColumnDef column, boolean isReadOnly) {
        this.column = column;
        xlsxFormatField = new XlsxFormatEditor(column);
        initComponents();
    }

    private void initComponents() {        
        widthSpinner.setModel(new SpinnerNumberModel(Long.valueOf(0L), Long.valueOf(0L), Long.valueOf(255L), Long.valueOf(1L)));
        widthSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int width = ((Long) widthSpinner.getValue()).intValue();
                column.getXlsxExportParameters().setWidth(width);
            }
        });
        widthSpinner.setEditor(new CheckedNumberSpinnerEditor(widthSpinner));        
        widthSpinner.setEnabled(column.getXlsxExportParameters().getResizeMode() == EReportColumnResizeMode.MANUAL);
        widthSpinner.setValue(Long.valueOf(column.getXlsxExportParameters().getWidth()));

        resizeModeComboBox.setSelectedItem(column.getXlsxExportParameters().getResizeMode());
        resizeModeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EReportColumnResizeMode resizeMode = (EReportColumnResizeMode) resizeModeComboBox.getSelectedItem();
                column.getXlsxExportParameters().setResizeMode(resizeMode);
                widthSpinner.setEnabled(resizeMode == EReportColumnResizeMode.MANUAL);
            }
        });
        
        JLabel xlsxFormatLbl = new JLabel(XLSX_FORMAT_LBL_TEXT);
        JLabel resizeModeLbl = new JLabel(RESIZE_MODE_LBL_TEXT);
        JLabel widthLbl = new JLabel(WIDTH_LBL_TEXT);
        
        this.setLayout(new MigLayout("fill, ins 0", "[shrink][grow]", "[shrink][shrink][shrink]"));
        this.add(xlsxFormatLbl, "shrinkx, shrinky");
        this.add(xlsxFormatField, "shrinky, growx, wrap");
        this.add(resizeModeLbl, "shrinkx, shrinky");
        this.add(resizeModeComboBox, "shrinky, growx, wrap");
        this.add(widthLbl, "shrinkx, shrinky");
        this.add(widthSpinner, "shrinky, growx");
    }
    
    public void setReadOnly(boolean isReadOnly) {
        xlsxFormatField.setReadOnly(isReadOnly);
        resizeModeComboBox.setEnabled(!isReadOnly);
        widthSpinner.setEnabled(!isReadOnly && column.getXlsxExportParameters().getResizeMode() == EReportColumnResizeMode.MANUAL);
    }
}
