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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import net.miginfocom.swing.MigLayout;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.radixware.kernel.common.utils.Utils;

public class XlsxFormatEditorPanel extends JPanel {
    
    public static final String GENERAL_FORMAT_TEXT = "General";
    private final String[] XLSX_EXAMPLES = {
        GENERAL_FORMAT_TEXT,
        "0",
        "0.00",
        "#,##0",
        "#,##0.00",
        "#,##0_);(#,##0)",
        "#,##0_);[Red](#,##0)",
        "#,##0.00_);(#,##0.00)",
        "#,##0.00_);[Red](#,##0.00)",
        "$#,##0_);($#,##0)",
        "$#,##0_);[Red]($#,##0)",
        "$#,##0.00_);($#,##0.00)",
        "$#,##0.00_);[Red]($#,##0.00)",
        "0%",
        "0.00%",
        "0.00E+00",
        "##0.0E+0",
        "#\" \"?/?",
        "#\" \"??/??",
        "dd.mm.yyyy",
        "dd.mmm.yy",
        "dd.yyy",
        "mmm.yy",
        "h:mm AM/PM",
        "h:mm:ss AM/PM",
        "h:mm",
        "h:mm:ss",
        "dd.mm.yyyy h:mm",
        "mm:ss",
        "mm:ss.0",
        "@",
        "[h]:mm:ss",
        "_($* #,##0_);_($* (#,##0);_($* \"-\"_);_(@_)",
        "_(* #,##0_);_(* (#,##0);_(* \"-\"_);_(@_)",
        "_($* #,##0.00_);_($* (#,##0.00);_($* \"-\"??_);_(@_)",
        "_(* #,##0.00_);_(* (#,##0.00);_(* \"-\"??_);_(@_)"
    };
    
    private final String FORMAT_LABEL_TEXT = "Format:";
    private final String PREVIEW_LABEL_TEXT = "Preview:";

    private final String initialFormat;
    
    private final Workbook workbook = new SXSSFWorkbook();
    private final Cell sampleCell = workbook.createSheet().createRow(0).createCell(0);
    private final CellStyle style = workbook.createCellStyle();
    {
        sampleCell.setCellValue(25569.5213541667);        
    }
    private final DataFormatter formatter;
    private final JComboBox<String> formatComboBox = new JComboBox<>(XLSX_EXAMPLES);
    
    private final JLabel formatLabel = new JLabel(FORMAT_LABEL_TEXT);
    
    private final JLabel previewLabel = new JLabel(PREVIEW_LABEL_TEXT);    
    private final JLabel sampleLabel = new JLabel();       

    public XlsxFormatEditorPanel(String initialFormat) {
        formatter = new DataFormatter();
        this.initialFormat = Utils.emptyOrNull(initialFormat) ? GENERAL_FORMAT_TEXT : initialFormat;
        initComponents();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout());                
        updateSample(initialFormat);                
        
        formatComboBox.setEditable(true);
        formatComboBox.addActionListener(getFormatComboBoxActionListener());
        
        JTextComponent examplesEditor = (JTextComponent) formatComboBox.getEditor().getEditorComponent();
        examplesEditor.getDocument().addDocumentListener(getFormatChangeListener());
                
        if (Arrays.asList(XLSX_EXAMPLES).contains(initialFormat)) {            
            formatComboBox.setSelectedItem(initialFormat);
        } else {
            formatComboBox.getEditor().setItem(initialFormat);
        }
        
        this.setLayout(new MigLayout("fillx", "[shrink][grow]", "[shrink][shrink]"));
        this.add(formatLabel, "shrink");
        this.add(formatComboBox, "growx, shrinky, wrap");
        this.add(previewLabel, "shrink");
        this.add(sampleLabel, "growx, shrinky, wrap");                
    }
    
    private ActionListener getFormatComboBoxActionListener() {
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String format = (String) formatComboBox.getSelectedItem();                
                updateSample(format);
            }
        };
    }    
    
    private DocumentListener getFormatChangeListener() {
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
                String format = (String) formatComboBox.getEditor().getItem();
                updateSample(format);
            }
        };
    }
    
    private void updateSample(final String format) {
        String actualFormat = format == null ? GENERAL_FORMAT_TEXT : format;
        style.setDataFormat(workbook.createDataFormat().getFormat(actualFormat));                
        
        sampleCell.setCellStyle(style);
        sampleLabel.setText(formatter.formatCellValue(sampleCell));
    }
    
    public String getXlsxFormat() {
        return (String) formatComboBox.getEditor().getItem();
    }
}
