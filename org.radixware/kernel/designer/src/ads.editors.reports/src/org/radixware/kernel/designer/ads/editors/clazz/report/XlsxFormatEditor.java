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
import org.radixware.kernel.common.components.ExtendableTextField;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportColumnDef;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;

public class XlsxFormatEditor extends ExtendableTextField {
        
    private final AdsReportColumnDef.XlsxExportParameters parameters;
    
    private final JButton chooseFormatButton;

    public XlsxFormatEditor(AdsReportColumnDef column) {
        super(true);
        this.parameters = column.getXlsxExportParameters();        

        chooseFormatButton = addButton();
        chooseFormatButton.setIcon(RadixWareIcons.ARROW.GO_TO_OBJECT.getIcon(13, 13));
        chooseFormatButton.setToolTipText("Configure XLSX export format");
        
        chooseFormatButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                XlsxFormatEditorPanel formatEditorPanel = new XlsxFormatEditorPanel(XlsxFormatEditor.this.parameters.getXlsxExportFormat());
                ModalDisplayer displayer = new ModalDisplayer(formatEditorPanel, "XLSX Export Format");
                
                if(displayer.showModal()) {
                    String format = formatEditorPanel.getXlsxFormat();
                    XlsxFormatEditor.this.parameters.setXlsxExportFormat(format);
                    if (format != null) {
                        setValue(format);
                    }
                }
            }
        });                
        
        String format = Utils.emptyOrNull(parameters.getXlsxExportFormat()) ? XlsxFormatEditorPanel.GENERAL_FORMAT_TEXT : parameters.getXlsxExportFormat();
        setValue(format);
    }        
}