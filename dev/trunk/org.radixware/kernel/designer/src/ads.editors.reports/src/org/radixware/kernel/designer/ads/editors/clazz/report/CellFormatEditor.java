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
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportFormat;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;

public class CellFormatEditor extends ExtendableTextField {

    private EValType type;

    private final JButton chooseFormatButton;
    private final FormattedCellPanel formatEditorPanel;

    private final AdsReportFormat original;
    private final AdsReportFormat format;

    public CellFormatEditor(final AdsReportFormat format, final String dialogTitle, final boolean isReadOnly) {
        super(true);

        this.original = format;
        this.format = new AdsReportFormat(original);

        formatEditorPanel = new FormattedCellPanel(isReadOnly);

        chooseFormatButton = addButton();
        chooseFormatButton.setIcon(RadixWareIcons.ARROW.GO_TO_OBJECT.getIcon(13, 13));
        chooseFormatButton.setToolTipText("Configure format");

        chooseFormatButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                formatEditorPanel.open(CellFormatEditor.this.format, type);

                ModalDisplayer displayer = new ModalDisplayer(formatEditorPanel, dialogTitle);
                if (displayer.showModal()) {
                    ReportColumnEditorUtils.copyFormatContent(CellFormatEditor.this.format, CellFormatEditor.this.original);
                    setValue(CellFormatEditor.this.format.getStrValue(type));
                } else {
                    ReportColumnEditorUtils.copyFormatContent(CellFormatEditor.this.original, CellFormatEditor.this.format);
                }
            }
        });

        setValue(format.getStrValue(type));
    }

    public void setType(EValType type) {
        this.type = type;
        setValue(format.getStrValue(type));
    }
}
