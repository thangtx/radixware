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

package org.radixware.kernel.designer.common.dialogs.events;

import java.awt.Color;
import java.awt.Component;
import java.util.logging.Level;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.enums.EEventSeverity;


public class TraceTableCellRenderer extends DefaultTableCellRenderer {

    private static final Color bgColor1 = new Color(236, 233, 216);
    private static final Color bgColor2 = new Color(220, 220, 220);
    private static final Color debugColor = Color.BLACK;
    private static final Color eventColor = Color.BLUE;
    private static final Color warningColor = new Color(128, 0, 0);
    private static final Color errorColor = Color.RED;
    private static final Color alarmColor = Color.RED;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        isSelected = row == table.getSelectedRow();
        JComponent pattern = (JComponent)super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column);
        JLabel label = new JLabel("");
        label.setOpaque(true);
        if (value instanceof EEventSeverity) {
            EEventSeverity sev = (EEventSeverity)value;
            label.setIcon(RadixObjectIcon.getForSeverity(sev).getIcon());
        } else {
            if (value != null)
                label.setText(value.toString());
            Level level = ((TraceTableModel)table.getModel()).getLogRecordAt(row).getLevel();
            EEventSeverity sev = TraceTableModel.getSeverityByLevel(level);
            if (sev.equals(EEventSeverity.DEBUG))
                label.setForeground(debugColor);
            else if (sev.equals(EEventSeverity.EVENT))
                label.setForeground(eventColor);
            else if (sev.equals(EEventSeverity.WARNING))
                label.setForeground(warningColor);
            else if (sev.equals(EEventSeverity.ERROR))
                label.setForeground(errorColor);
            else
                label.setForeground(alarmColor);
        }
        if (!isSelected) {
            if ((row & 1) == 0)
                label.setBackground(bgColor1);
            else
                label.setBackground(bgColor2);
        } else
            label.setBackground(pattern.getBackground());
        if (hasFocus)
            label.setBorder(pattern.getBorder());
        return label;
    }
    
}
