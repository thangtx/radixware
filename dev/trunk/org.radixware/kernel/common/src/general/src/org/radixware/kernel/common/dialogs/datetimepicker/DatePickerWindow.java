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
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.text.NumberFormatter;
import net.miginfocom.swing.MigLayout;

public class DatePickerWindow extends JDialog {

    private Calendar calendar = Calendar.getInstance();
    private PaintClock clock;
    private CalendarTable calendarTable;
    private final IImageDistributor img;
    private final JPanel timePanel = new JPanel();
    private final JPanel datePanel = new JPanel();
    private final JSpinner hourSpinner = new JSpinner();
    private final JSpinner minSpinner = new JSpinner();
    private final JSpinner secSpinner = new JSpinner();
    private final JSpinner millisSpinner = new JSpinner();
    private final JSpinner yearSpinner = new JSpinner();
    private JLabel monthLabel;
    private JLabel yearLabel;
    private JButton previousYearButton;
    private JButton previousMonthButton;
    private JButton nextMonthButton;
    private JButton nextYearButton;
    private JTable jtable;
    private JButton reset_Btn;

    Calendar oldCal;

    public static final String DATE_MASK = "dd/MM/yyyy";
    public static final String TIME_MASK = "HH:mm:ss";
    public static final String TIME_MASK_MILLIS = "HH:mm:ss.SSS";
    
    private final JComboBox monthBox = new JComboBox(new String[]{
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December"
    });

    DatePickerWindow(Calendar cal, final boolean useOfTime, Frame owner, IImageDistributor img, final boolean useOfMillis) {
        super(owner);
        this.img = img;
        setLayout(new MigLayout());
        setIconImage(img.getCalendarIcon());
        oldCal = Calendar.getInstance();
        oldCal.setTime(calendar.getTime());
        if (cal != null) {
            calendar.setTime(cal.getTime());
            oldCal.setTime(cal.getTime());
        }

        add(createDatePickerPanel(useOfTime, useOfMillis), "span 3 5");
        if (useOfTime) {
            add(createTimePickerPanel(useOfTime, useOfMillis), "span 2 5");
            setSize(new Dimension(850, 280)); //750 //710
            setTitle("Date and time");
        } else {
            setSize(new Dimension(460, 280)); //500
            setTitle("Date");
        }
        add(createButtonPanel(useOfTime, useOfMillis));
        setResizable(false);

        setModal(true);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getWheelRotation() == 1) {
                    if (goPreviousMonth()) {
                        refreshDate(useOfTime, useOfMillis);
                    }
                } else {
                    if (goNextMonth()) {
                        refreshDate(useOfTime, useOfMillis);
                    }
                }
            }
        });
        addWindowListener(new WindowListener() {

            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
                calendar = null;
                setVisible(false);
            }

            @Override
            public void windowClosed(WindowEvent e) {
                calendar = null;
                setVisible(false);
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }

        });
        addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                calendar.set(Calendar.MONTH, monthBox.getSelectedIndex());
                calendar.set(Calendar.YEAR, (int) yearSpinner.getValue());

                refreshDate(useOfTime, useOfMillis);

                monthLabel.setVisible(true);
                monthBox.setVisible(false);
                yearSpinner.setVisible(false);
                yearLabel.setVisible(true);
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        refreshDate(useOfTime, useOfMillis);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public Calendar getCalendar() {
        return calendar;
    }

    private JPanel createButtonPanel(final boolean useOfTime, final boolean useOfMillis) {
        JPanel buttonPanel = new JPanel();
        JButton ok_Btn = new JButton("OK");
        JButton cancel_Btn = new JButton("Cancel");
        JButton today_Btn = useOfTime ? new JButton("Now") : new JButton("Today");
        reset_Btn = new JButton("Reset");

        today_Btn.setPreferredSize(new Dimension(90, 26));
        cancel_Btn.setPreferredSize(new Dimension(90, 26));
        ok_Btn.setPreferredSize(new Dimension(90, 26));
        reset_Btn.setPreferredSize(new Dimension(90, 26));

        ok_Btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        cancel_Btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calendar = null;
                setVisible(false);
            }
        });

        today_Btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calendar.setTime(Calendar.getInstance().getTime());
                refreshDate(useOfTime, useOfMillis);
            }
        });

        reset_Btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calendar.setTime(oldCal.getTime());
                refreshDate(useOfTime, useOfMillis);
            }
        });

        buttonPanel.setPreferredSize(new Dimension(90, 118));
        buttonPanel.setLayout(new MigLayout("", "", "[]20[]20[]20[]"));
        buttonPanel.add(ok_Btn, "wrap, align center");
        buttonPanel.add(cancel_Btn, "wrap, align center");
        buttonPanel.add(today_Btn, "wrap, align center");
        buttonPanel.add(reset_Btn, "wrap, align center");

        return buttonPanel;
    }

    private JPanel createTimePickerPanel(final boolean useOfTime, final boolean useOfMillis) {
        timePanel.setLayout(new MigLayout("ins 0"));

        clock = new PaintClock(calendar, img);

        hourSpinner.setModel(new SpinnerNumberModel(calendar.get(Calendar.HOUR_OF_DAY), -1, 24, 1));
        minSpinner.setModel(new SpinnerNumberModel(calendar.get(Calendar.MINUTE), -1, 60, 1));
        secSpinner.setModel(new SpinnerNumberModel(calendar.get(Calendar.SECOND), -1, 60, 1));

        hourSpinner.setEditor(new JSpinner.NumberEditor(hourSpinner, "0"));
        minSpinner.setEditor(new JSpinner.NumberEditor(minSpinner, "0"));
        secSpinner.setEditor(new JSpinner.NumberEditor(secSpinner, "0"));

        ((NumberFormatter) ((JSpinner.DefaultEditor) hourSpinner.getEditor()).getTextField().getFormatter()).setAllowsInvalid(false);
        ((NumberFormatter) ((JSpinner.DefaultEditor) minSpinner.getEditor()).getTextField().getFormatter()).setAllowsInvalid(false);
        ((NumberFormatter) ((JSpinner.DefaultEditor) secSpinner.getEditor()).getTextField().getFormatter()).setAllowsInvalid(false);

        JPanel timePicker = new JPanel();
        timePicker.add(new JLabel("H."));
        timePicker.add(hourSpinner);
        timePicker.add(new JLabel("Min."));
        timePicker.add(minSpinner);
        timePicker.add(new JLabel("Sec."));
        timePicker.add(secSpinner);
        if (useOfMillis) {
            millisSpinner.setModel(new SpinnerNumberModel(calendar.get(Calendar.MILLISECOND), -1, 1000, 1));
            millisSpinner.setEditor(new JSpinner.NumberEditor(millisSpinner, "0"));
            ((NumberFormatter) ((JSpinner.DefaultEditor) millisSpinner.getEditor()).getTextField().getFormatter()).setAllowsInvalid(false);

            timePicker.add(new JLabel("Ms."));
            timePicker.add(millisSpinner);
        }

        hourSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (calendar.get(Calendar.HOUR_OF_DAY) != (int) hourSpinner.getValue()) {
                    updateHour((int) hourSpinner.getValue());
                }
                refreshDate(useOfTime, useOfMillis);
            }
        });
        minSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (calendar.get(Calendar.MINUTE) != (int) minSpinner.getValue()) {
                    updateMin((int) minSpinner.getValue());
                }
                refreshDate(useOfTime, useOfMillis);
            }
        });
        secSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (calendar.get(Calendar.SECOND) != (int) secSpinner.getValue()) {
                    updateSec((int) secSpinner.getValue());
                }
                refreshDate(useOfTime, useOfMillis);
            }
        });

        if (useOfMillis) {
            millisSpinner.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    if (calendar.get(Calendar.MILLISECOND) != (int) millisSpinner.getValue()) {
                        updateMillis((int) millisSpinner.getValue());
                    }
                    refreshDate(useOfTime, useOfMillis);
                }
            });
        }

        timePanel.setPreferredSize(new Dimension(250, 222));
        timePanel.add(timePicker, "wrap, align center");
        timePanel.add(clock, "wrap, align center");

        return timePanel;
    }

    public void updateHour(int newHour) {
        if (newHour == 24) {
            calendar.set(Calendar.HOUR_OF_DAY, 0);
        } else if (newHour == -1) {
            calendar.set(Calendar.HOUR_OF_DAY, 23);
        } else {
            calendar.set(Calendar.HOUR_OF_DAY, newHour);
        }
    }

    public void updateMin(int newMin) {
        if (newMin == 60) {
            calendar.set(Calendar.MINUTE, 0);
            updateHour(calendar.get(Calendar.HOUR_OF_DAY) + 1);
        } else if (newMin == -1) {
            calendar.set(Calendar.MINUTE, 59);
            updateHour(calendar.get(Calendar.HOUR_OF_DAY) - 1);
        } else {
            calendar.set(Calendar.MINUTE, newMin);
        }
    }

    public void updateSec(int newSec) {
        if (newSec == 60) {
            calendar.set(Calendar.SECOND, 0);
            updateMin(calendar.get(Calendar.MINUTE) + 1);
        } else if (newSec == -1) {
            calendar.set(Calendar.SECOND, 59);
            updateMin(calendar.get(Calendar.MINUTE) - 1);
        } else {
            calendar.set(Calendar.SECOND, newSec);
        }
    }
    
    public void updateMillis(int newMillis) {
        if (newMillis == 1000) {
            calendar.set(Calendar.MILLISECOND, 0);
            updateSec(calendar.get(Calendar.SECOND) + 1);
        } else if (newMillis == -1) {
            calendar.set(Calendar.MILLISECOND, 999);
            updateSec(calendar.get(Calendar.SECOND) - 1);
        } else {
            calendar.set(Calendar.MILLISECOND, newMillis);
        }
    }

    private JPanel createDatePickerPanel(final boolean useOfTime, final boolean useOfMillis) {
        JPanel headPanel = new JPanel();
        headPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        calendarTable = new CalendarTable(calendar);

        jtable = calendarTable.getJTable();

        jtable.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) { //38
                    calendar.add(Calendar.DAY_OF_MONTH, -7);
                    refreshDate(useOfTime, useOfMillis);
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) { //40
                    calendar.add(Calendar.DAY_OF_MONTH, 7);
                    refreshDate(useOfTime, useOfMillis);
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) { //39
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    refreshDate(useOfTime, useOfMillis);
                } else if (e.getKeyCode() == KeyEvent.VK_LEFT) { //37
                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                    refreshDate(useOfTime, useOfMillis);
                }
            }
        });

        jtable.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                int markerPreviousMonth = calendarTable.dayOfWeekFirstDayOfMonth(calendar);
                int positionTable = jtable.getSelectedRow() * 10 + jtable.getSelectedColumn();
                int markerNextMonth = (((calendarTable.dayOfWeekFirstDayOfMonth(calendar) + calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) / 7) * 10) + (((calendarTable.dayOfWeekFirstDayOfMonth(calendar) + calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) % 7) - 1);

                if (positionTable < markerPreviousMonth) {
                    if (goPreviousMonth(Integer.parseInt((String) jtable.getValueAt(jtable.getSelectedRow(), jtable.getSelectedColumn())))) {
                        refreshDate(useOfTime, useOfMillis);
                    }
                } else if (positionTable > markerNextMonth) {
                    if (goNextMonth(Integer.parseInt((String) jtable.getValueAt(jtable.getSelectedRow(), jtable.getSelectedColumn())))) {
                        refreshDate(useOfTime, useOfMillis);
                    }
                } else {
                    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt((String) jtable.getValueAt(jtable.getSelectedRow(), jtable.getSelectedColumn())));
                    refreshDate(useOfTime, useOfMillis);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        previousYearButton = new JButton();
        previousMonthButton = new JButton();
        nextMonthButton = new JButton();
        nextYearButton = new JButton();

        previousYearButton.setPreferredSize(new Dimension(40, 20));
        previousMonthButton.setPreferredSize(new Dimension(20, 20));
        nextMonthButton.setPreferredSize(new Dimension(20, 20));
        nextYearButton.setPreferredSize(new Dimension(40, 20));

        previousYearButton.setContentAreaFilled(false);
        previousMonthButton.setContentAreaFilled(false);
        nextMonthButton.setContentAreaFilled(false);
        nextYearButton.setContentAreaFilled(false);

        previousYearButton.setBorder(null);
        previousMonthButton.setBorder(null);
        nextMonthButton.setBorder(null);
        nextYearButton.setBorder(null);

        previousYearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (calendar.get(Calendar.YEAR) > 1) {
                    calendar.add(Calendar.YEAR, -1);
                    refreshDate(useOfTime, useOfMillis);
                }
            }
        });

        previousMonthButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!(calendar.get(Calendar.YEAR) == 1 && calendar.get(Calendar.MONTH) == Calendar.JANUARY)) {
                    calendar.add(Calendar.MONTH, -1);
                    refreshDate(useOfTime, useOfMillis);
                }
            }
        });

        nextMonthButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!(calendar.get(Calendar.MONTH) == Calendar.DECEMBER && calendar.get(Calendar.YEAR) == 9999)) {
                    calendar.add(Calendar.MONTH, 1);
                    refreshDate(useOfTime, useOfMillis);
                }
            }
        });

        nextYearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (calendar.get(Calendar.YEAR) < 9999) {
                    calendar.add(Calendar.YEAR, 1);
                    refreshDate(useOfTime, useOfMillis);
                }
            }
        });

        monthLabel = new JLabel(intToCalendarMonth(calendar.get(Calendar.MONTH)));
        yearLabel = new JLabel(Integer.toString(calendar.get(Calendar.YEAR)), SwingConstants.RIGHT);
        yearSpinner.setModel(new SpinnerNumberModel(calendar.get(Calendar.YEAR), 1, 9999, 1));
        yearSpinner.setEditor(new JSpinner.NumberEditor(yearSpinner, "0"));

        ((NumberFormatter) ((JSpinner.DefaultEditor) yearSpinner.getEditor()).getTextField().getFormatter()).setAllowsInvalid(false);

        yearSpinner.setPreferredSize(new Dimension(75, 25));
        yearLabel.setPreferredSize(new Dimension(75, 25));
        monthLabel.setPreferredSize(new Dimension(76, 25));
        monthBox.setPreferredSize(new Dimension(75, 25));

        yearLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                yearLabel.setVisible(false);
                yearSpinner.setVisible(true);
                yearSpinner.requestFocus();
            }
        });
        monthLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                monthLabel.setVisible(false);
                monthBox.setVisible(true);
                monthBox.requestFocus();
            }
        });
        yearSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                calendar.set(Calendar.YEAR, (int) yearSpinner.getValue());
                refreshDate(useOfTime, useOfMillis);
            }
        });
        yearSpinner.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {

                if (e.getOppositeComponent() != null && !e.getOppositeComponent().equals(((JSpinner.DefaultEditor) yearSpinner.getEditor()).getTextField())) {
                    refreshDate(useOfTime, useOfMillis);
                    yearSpinner.setVisible(false);
                    yearLabel.setVisible(true);
                }
            }
        });

        ((JSpinner.DefaultEditor) yearSpinner.getEditor()).getTextField().addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                refreshDate(useOfTime, useOfMillis);
                yearSpinner.setVisible(false);
                yearLabel.setVisible(true);
            }
        });

        JPanel monthAndYearPanel = new JPanel();
        monthAndYearPanel.setLayout(new BoxLayout(monthAndYearPanel, BoxLayout.LINE_AXIS));
        JPanel monthPanel = new JPanel();
        JPanel yearPanel = new JPanel();

        monthPanel.add(monthLabel);
        monthPanel.add(monthBox);
        yearPanel.add(yearLabel);
        yearPanel.add(yearSpinner);
        monthAndYearPanel.add(monthPanel);
        monthAndYearPanel.add(yearPanel);

        yearSpinner.setVisible(false);
        monthBox.setVisible(false);
        monthBox.setSelectedIndex(calendar.get(Calendar.MONTH));

        monthBox.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                monthBox.showPopup();
            }

            @Override
            public void focusLost(FocusEvent e) {
                calendar.set(Calendar.MONTH, monthBox.getSelectedIndex());
                monthLabel.setVisible(true);
                monthBox.setVisible(false);
                refreshDate(useOfTime, useOfMillis);
            }
        });
        monthBox.addPopupMenuListener(new PopupMenuListener() {

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                calendar.set(Calendar.MONTH, monthBox.getSelectedIndex());
                monthLabel.setVisible(true);
                monthBox.setVisible(false);
                refreshDate(useOfTime, useOfMillis);
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
            }
        });

        JPanel previousPanel = new JPanel();
        JPanel nextPanel = new JPanel();

        previousPanel.add(previousYearButton);
        previousPanel.add(previousMonthButton);
        nextPanel.add(nextMonthButton);
        nextPanel.add(nextYearButton);

        headPanel.setLayout(new BorderLayout());
        headPanel.add(previousPanel, BorderLayout.WEST);
        headPanel.add(monthAndYearPanel, BorderLayout.CENTER);
        headPanel.add(nextPanel, BorderLayout.EAST);

        headPanel.setSize(225, 40);

        datePanel.setBorder(new TitledBorder("Date: " + new SimpleDateFormat(DatePickerSettings.MASK_DATE).format(calendar.getTime())));
        datePanel.setLayout(new BorderLayout());
        datePanel.add(headPanel, BorderLayout.NORTH);
        datePanel.add(calendarTable, BorderLayout.CENTER);

        return datePanel;
    }

    private void refreshDate(boolean useOfTime, boolean useOfMillis) {
        if (calendar != null) {
            datePanel.setBorder(new TitledBorder("Date: " + new SimpleDateFormat(DATE_MASK).format(calendar.getTime())));

            if (calendar.get(Calendar.YEAR) == 1 && calendar.get(Calendar.MONTH) == Calendar.JANUARY) {
                previousMonthButton.setIcon(img.getArrowLeftBlocked_1());
                previousYearButton.setIcon(img.getArrowLeftBlocked_2());
            } else if (calendar.get(Calendar.YEAR) == 1) {
                previousMonthButton.setIcon(img.getArrowLeftActive_1());
                previousYearButton.setIcon(img.getArrowLeftBlocked_2());
            } else {
                previousMonthButton.setIcon(img.getArrowLeftActive_1());
                previousYearButton.setIcon(img.getArrowLeftActive_2());
            }

            if (calendar.get(Calendar.YEAR) == 9999 && calendar.get(Calendar.MONTH) == Calendar.DECEMBER) {
                nextMonthButton.setIcon(img.getArrowRightBlocked_1());
                nextYearButton.setIcon(img.getArrowRightBlocked_2());
            } else if (calendar.get(Calendar.YEAR) == 9999) {
                nextMonthButton.setIcon(img.getArrowRightActive_1());
                nextYearButton.setIcon(img.getArrowRightBlocked_2());
            } else {
                nextMonthButton.setIcon(img.getArrowRightActive_1());
                nextYearButton.setIcon(img.getArrowRightActive_2());
            }

            previousYearButton.setFocusPainted(false);
            previousMonthButton.setFocusPainted(false);
            nextMonthButton.setFocusPainted(false);
            nextYearButton.setFocusPainted(false);

            yearSpinner.setValue(calendar.get(Calendar.YEAR));
            yearLabel.setText(Integer.toString(calendar.get(Calendar.YEAR)));
            monthLabel.setText(intToCalendarMonth(calendar.get(Calendar.MONTH)));
            monthBox.setSelectedIndex(calendar.get(Calendar.MONTH));

            reset_Btn.setEnabled(!calendar.equals(oldCal));

            calendarTable.setData(calendar);

            if (useOfTime) {
                timePanel.setBorder(new TitledBorder("Time: " + ((useOfMillis) ? new SimpleDateFormat(TIME_MASK_MILLIS) : new SimpleDateFormat(TIME_MASK)).format(calendar.getTime())));
                hourSpinner.setValue(calendar.get(Calendar.HOUR_OF_DAY));
                minSpinner.setValue(calendar.get(Calendar.MINUTE));
                secSpinner.setValue(calendar.get(Calendar.SECOND));
                if(useOfMillis) {
                    millisSpinner.setValue(calendar.get(Calendar.MILLISECOND));
                }
                clock.setTime(calendar);
            }
        }
    }

    private boolean goPreviousMonth() {
        if (!(calendar.get(Calendar.YEAR) == 1 && calendar.get(Calendar.MONTH) == Calendar.JANUARY)) {
            calendar.add(Calendar.MONTH, -1);
            return true;
        }
        return false;
    }

    private boolean goNextMonth() {
        if (!(calendar.get(Calendar.MONTH) == Calendar.DECEMBER && calendar.get(Calendar.YEAR) == 9999)) {
            calendar.add(Calendar.MONTH, 1);
            return true;
        }
        return false;
    }

    private boolean goPreviousMonth(int day) {
        if (!(calendar.get(Calendar.YEAR) == 1 && calendar.get(Calendar.MONTH) == Calendar.JANUARY)) {
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            return true;
        }
        return false;
    }

    private boolean goNextMonth(int day) {
        if (!(calendar.get(Calendar.MONTH) == Calendar.DECEMBER && calendar.get(Calendar.YEAR) == 9999)) {
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            return true;
        }
        return false;
    }

    private String intToCalendarMonth(int num) {
        switch (num) {
            case Calendar.JANUARY:
                return "January";
            case Calendar.FEBRUARY:
                return "February";
            case Calendar.MARCH:
                return "March";
            case Calendar.APRIL:
                return "April";
            case Calendar.MAY:
                return "May";
            case Calendar.JUNE:
                return "June";
            case Calendar.JULY:
                return "July";
            case Calendar.AUGUST:
                return "August";
            case Calendar.SEPTEMBER:
                return "September";
            case Calendar.OCTOBER:
                return "October";
            case Calendar.NOVEMBER:
                return "November";
            case Calendar.DECEMBER:
                return "December";
        }
        return "";
    }
}
