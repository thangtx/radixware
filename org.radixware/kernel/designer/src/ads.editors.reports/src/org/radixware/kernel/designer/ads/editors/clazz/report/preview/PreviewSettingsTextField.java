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
import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.radixware.kernel.common.types.Id;

public class PreviewSettingsTextField extends JTextField {

    private final Id reportId;
    private final ReportPreviewSettings destinationSettings;
    private String placeholderText = "";

    public PreviewSettingsTextField(ReportPreviewSettings destinationSettings, Id reportId) {
        super(destinationSettings.get(reportId, ""));

        this.destinationSettings = destinationSettings;
        this.reportId = reportId;

        getDocument().addDocumentListener(getDocumentListener());
        addFocusListener(getFocusListener());
    }

    public void setPlaceholderText(String placeholderText) {
        if (this.placeholderText.equals(getText())) {
            setText(placeholderText);
        }

        if (getText().equals(placeholderText)) {
            setForeground(Color.GRAY);
        }
        this.placeholderText = placeholderText;
    }

    private DocumentListener getDocumentListener() {
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
                if (!getText().equals(placeholderText)) {
                    destinationSettings.set(reportId, getText());
                } else {
                    destinationSettings.set(reportId, "");
                }
            }
        };
    }

    private FocusListener getFocusListener() {
        return new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (getText().equals(placeholderText) && !placeholderText.isEmpty()) {
                    setText("");
                    setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getText().isEmpty() && !placeholderText.isEmpty()) {
                    setForeground(Color.GRAY);
                    setText(placeholderText);
                }
            }
        };
    }
    
    public boolean isEmpty() {
        return getText().equals(placeholderText) || getText().isEmpty();
    }
}
