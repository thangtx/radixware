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
package org.radixware.kernel.common.dialogs.datetimepicker;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Calendar;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class CalendarTable extends JPanel {

    final DefaultTableModel defaultTableModel;
    final String[] headers = new String[]{"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
    JTable table;
    int selectRow;
    int selectColumn;

    public CalendarTable(Calendar calendar) {
        //setBorder(LineBorder.createGrayLineBorder());
        setLayout(new BorderLayout());

        defaultTableModel = new DefaultTableModel();
        defaultTableModel.setDataVector(calendarTableFormation(calendar), headers);
        setData(calendar);

        table = new JTable(defaultTableModel) {
            @Override
            public boolean isCellEditable(int arg0, int arg1) {
                return false;
            }
        };

        table.setRowSelectionAllowed(false);
        table.setCellSelectionEnabled(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setResizingAllowed(false);
        //table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setRowHeight(22);

        for (Object header : headers) {
            table.getColumn(header).setCellRenderer(new CellRender(calendar));
        }

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getTableHeader().getColumnModel().getColumn(i).setHeaderRenderer(new HeaderRenderer(table));
        }
        table.getTableHeader().setReorderingAllowed(false);
        add(table.getTableHeader(), BorderLayout.NORTH);
        add(table, BorderLayout.CENTER);

        //int columnNumber=table.getColumnModel().getColumnCount();
        /*for (int i = 0; i < 7; i++) {
            table.getColumnModel().getColumn(i).setResizable(false);
        }*/
    }

    public JTable getJTable() {
        return table;
    }

    private static class HeaderRenderer implements TableCellRenderer {

        DefaultTableCellRenderer renderer;

        public HeaderRenderer(JTable table) {
            renderer = (DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer();
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int col) {
            Component c = renderer.getTableCellRendererComponent(table, value,
                    isSelected, hasFocus, row, col);
            JLabel label = (JLabel) c;
            label.setBackground(table.getTableHeader().getBackground());
            label.setBorder(LineBorder.createGrayLineBorder());
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setFont(new Font(label.getFont().getFontName(), Font.BOLD, label.getFont().getSize()));

            if (col == 5 || col == 6) {
                label.setForeground(Color.RED);
            }

            return label;
        }
    }

    public void setData(Calendar calendar) {
        defaultTableModel.setDataVector(calendarTableFormation(calendar), headers);
        if (table != null) {
            int row = 0;
            int column = dayOfWeekFirstDayOfMonth(calendar);
            boolean tmp = true;

            for (; column < 7 && tmp; column++) {
                if (Integer.parseInt((String) table.getValueAt(row, column)) == calendar.get(Calendar.DAY_OF_MONTH)) {
                    tmp = false;
                }

            }

            for (row = 1; row < 6 && tmp; row++) {
                for (column = 0; column < 7 && tmp; column++) {
                    if (Integer.parseInt((String) table.getValueAt(row, column)) == calendar.get(Calendar.DAY_OF_MONTH)) {
                        tmp = false;
                    }
                }
            }

            for (Object header : headers) {
                table.getColumn(header).setCellRenderer(new CellRender(calendar));
            }
            for (int i = 0; i < table.getColumnCount(); i++) {
                table.getTableHeader().getColumnModel().getColumn(i).setHeaderRenderer(new HeaderRenderer(table));
            }
            selectRow = --row;
            selectColumn = --column;
            table.setColumnSelectionInterval(selectColumn, selectColumn);
            table.setRowSelectionInterval(selectRow, selectRow);
            table.setShowHorizontalLines(false);
            table.setShowVerticalLines(false);
            table.setIntercellSpacing(new Dimension(0, 0));
            /*for (int i = 0; i < 7; i++) {
                table.getColumnModel().getColumn(i).setResizable(false);
            }*/
        }
    }

    private String[][] calendarTableFormation(Calendar calendar) {
        Calendar calendarBuffer = Calendar.getInstance();
        calendarBuffer.setTime(calendar.getTime());
        calendarBuffer.add(Calendar.MONTH, -1);

        int numberDaysInPastMonth = calendarBuffer.getActualMaximum(Calendar.DAY_OF_MONTH);
        int numberDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int weekday = dayOfWeekFirstDayOfMonth(calendar);
        int daysCounter = 1;
        int rowCounter = 0;
        int nextMonthDaysCounter = 1;
        String[][] rowDate = new String[6][7];

        for (int i = weekday - 1; i > -1; i--) {
            rowDate[rowCounter][i] = Integer.toString(numberDaysInPastMonth--);
        }
        for (int i = weekday; i < 7; i++) {
            rowDate[rowCounter][i] = Integer.toString(daysCounter++);
        }

        for (rowCounter++; rowCounter < 6; rowCounter++) {
            int i = 0;
            for (; i < 7 && daysCounter <= numberDaysInMonth; i++) {
                rowDate[rowCounter][i] = Integer.toString(daysCounter++);
            }
            for (; i < 7 && daysCounter > numberDaysInMonth; i++) {
                rowDate[rowCounter][i] = Integer.toString(nextMonthDaysCounter++);
            }
        }

        return rowDate;
    }

    public class CellRender extends DefaultTableCellRenderer {

        Calendar calendar;

        public CellRender(Calendar calendar) {
            this.calendar = calendar;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            int weekday = dayOfWeekFirstDayOfMonth(calendar);

            setBackground(table.getBackground());

            if (row == 0 && column < weekday) {
                setForeground(Color.GRAY);
            } else {
                if (column == 5 || column == 6) {
                    setForeground(Color.RED);
                } else {
                    setForeground(Color.BLACK);
                }
            }

            int rowt = ((dayOfWeekFirstDayOfMonth(calendar) + calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) / 7);
            int columnt = ((dayOfWeekFirstDayOfMonth(calendar) + calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) % 7) - 1;

            if ((row * 10 + column) > (rowt * 10 + columnt)) {
                setForeground(Color.GRAY);
            }

            if (row == selectRow && column == selectColumn) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            }

            /*if (hasFocus) {
             setBackground(table.getSelectionBackground());
             setForeground(table.getSelectionForeground());
             }*/
            setHorizontalAlignment(JLabel.CENTER);
            setBorder(BorderFactory.createEtchedBorder());
            return this;
        }

    }

    public int dayOfWeekFirstDayOfMonth(Calendar calendar) {
        Calendar calendarBuffer = Calendar.getInstance();
        calendarBuffer.setTime(calendar.getTime());
        calendarBuffer.set(Calendar.DAY_OF_MONTH, 1);
        return (calendarBuffer.get(Calendar.DAY_OF_WEEK) == 1) ? 6 : calendarBuffer.get(Calendar.DAY_OF_WEEK) - 2;
    }
}
