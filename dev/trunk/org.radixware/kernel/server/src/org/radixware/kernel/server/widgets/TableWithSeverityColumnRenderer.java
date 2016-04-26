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

package org.radixware.kernel.server.widgets;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import org.radixware.kernel.common.enums.EEventSeverity;


public class TableWithSeverityColumnRenderer implements TableCellRenderer {

    private final JLabel label;
    private final Font arteFont = new Font("Tahoma", Font.PLAIN, 11);
    private final Color bgColor1 = new Color(236, 233, 216);
    private final Color bgColor2 = new Color(220, 220, 220);
    private final Color bgSelColor = Color.BLUE;
    private final Color arteFontColor = Color.BLUE;
    private final Color selFontColor = Color.WHITE;
    private final Icon debugIcon;
    private final Icon eventIcon;
    private final Icon warningIcon;
    private final Icon errorIcon;
    private final Icon alarmIcon;

    public TableWithSeverityColumnRenderer() {
        label = new JLabel();
        label.setBorder(new EmptyBorder(1, 4, 1, 1));
        label.setOpaque(true);
        label.setFont(arteFont);
        debugIcon = TraceSettings.getIconForSeverity(EEventSeverity.DEBUG);
        eventIcon = TraceSettings.getIconForSeverity(EEventSeverity.EVENT);
        warningIcon = TraceSettings.getIconForSeverity(EEventSeverity.WARNING);
        errorIcon = TraceSettings.getIconForSeverity(EEventSeverity.ERROR);
        alarmIcon = TraceSettings.getIconForSeverity(EEventSeverity.ALARM);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {
        if (value instanceof EEventSeverity) {
            label.setText(null);
            EEventSeverity severity = (EEventSeverity) value;
            if (severity == EEventSeverity.DEBUG) {
                label.setIcon(debugIcon);
            } else if (severity == EEventSeverity.EVENT) {
                label.setIcon(eventIcon);
            } else if (severity == EEventSeverity.WARNING) {
                label.setIcon(warningIcon);
            } else if (severity == EEventSeverity.ERROR) {
                label.setIcon(errorIcon);
            } else if (severity == EEventSeverity.ALARM) {
                label.setIcon(alarmIcon);
            } else {
                label.setIcon(null);
            }
            if (row != table.getSelectedRow()) {
                if (row % 2 == 0) {
                    label.setBackground(bgColor1);
                } else {
                    label.setBackground(bgColor2);
                }
            } else {
                label.setBackground(bgSelColor);
            }
        } else {
            label.setIcon(null);
            label.setText(value != null ? value.toString() : "");
            if (row != table.getSelectedRow()) {
                if (row % 2 == 0) {
                    label.setBackground(bgColor1);
                } else {
                    label.setBackground(bgColor2);
                }
                label.setForeground(arteFontColor);
            } else {
                label.setBackground(bgSelColor);
                label.setForeground(selFontColor);
            }
        }
        return label;
    }
}
