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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.MaskFormatter;

public class DateAndTimePicker extends JPanel {

    private final SimpleDateFormat simpleDateF;
    private JFormattedTextField textFieldMask;
    private final boolean useOfTime;
    private final boolean useOfMillis;
    private final JButton settingDateButton = new JButton("...");
    private Calendar calendar = null;
    private boolean isDateGood = true;
    private final DocumentListener documentListener;
    private final IImageDistributor images;
    
    public static enum EOperatingMode {
        DATE,
        DATE_AND_TIME,
        DATE_AND_TIME_MILLISECONDS
    }

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy") {
        {
            setLenient(false);
        }
    };
    
    public DateAndTimePicker() {
        this(EOperatingMode.DATE_AND_TIME, new DefaultImageDistributor());
    }
    
    public DateAndTimePicker(EOperatingMode mode) {
        this(mode, new DefaultImageDistributor());
    }
    
    public DateAndTimePicker(IImageDistributor images) {
        this(EOperatingMode.DATE_AND_TIME, images);
    }

    public DateAndTimePicker(EOperatingMode mode, IImageDistributor images) {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        this.images = images == null ? new DefaultImageDistributor() : images;

        switch (mode) {
            case DATE:
                simpleDateF = new SimpleDateFormat(DatePickerSettings.MASK_DATE, Locale.ENGLISH);
                textFieldMask = new JFormattedTextField(createFormatter(DatePickerSettings.FORMAT_DATE));
                useOfTime = false;
                useOfMillis = false;
                break;
            case DATE_AND_TIME:
                simpleDateF = new SimpleDateFormat(DatePickerSettings.MASK_DATE_TIME, Locale.ENGLISH);
                textFieldMask = new JFormattedTextField(createFormatter(DatePickerSettings.FORMAT_DATE_TIME));
                useOfTime = true;
                useOfMillis = false;
                break;
            default:
                simpleDateF = new SimpleDateFormat(DatePickerSettings.MASK_DATE_TIME_MILLIS, Locale.ENGLISH);
                textFieldMask = new JFormattedTextField(createFormatter(DatePickerSettings.FORMAT_DATE_TIME_MILLIS));
                useOfTime = true;
                useOfMillis = true;
                break;
        }
        
        textFieldMask.setFocusLostBehavior(JFormattedTextField.COMMIT);
        
        documentListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                isDateValid(textFieldMask.getText().split(" "));
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        };

        settingDateButton.setPreferredSize(new Dimension(20, 20));

        settingDateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setCalendarCL(new DatePickerWindow(calendar, useOfTime, null, DateAndTimePicker.this.images, useOfMillis).getCalendar());
                textFieldMask.requestFocus();
            }
        });

        textFieldMask.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                isDateValid(textFieldMask.getText().split(" "));
            }

            @Override
            public void focusLost(FocusEvent e) {
                isDateValid(textFieldMask.getText().split(" "));
                isFieldCorrect(isDateGood);
            }
        });

        add(textFieldMask);
        add(settingDateButton);

        setVisible(true);
        textFieldMask.getDocument().addDocumentListener(documentListener);
    }

    public static Date calendarDialog(Date date, EOperatingMode mode, IImageDistributor img, Frame owner) {
        Calendar calTmp = Calendar.getInstance();
        if (date != null) {
            calTmp.setTime(date);
        }
        Calendar retCal = new DatePickerWindow(calTmp, mode != EOperatingMode.DATE, owner, img, mode == EOperatingMode.DATE_AND_TIME_MILLISECONDS).getCalendar();
        return (retCal == null) ? null : retCal.getTime();
    }

    public static Date calendarDialog(Date date, EOperatingMode mode, IImageDistributor img) {
        return calendarDialog(date, mode, img, null);
    }

    public boolean isEmptyDateField(String date) {
        return (date.equals("__/__/____") || date.equals("__/__/____ __:__:__") || date.equals("__/__/____ __:__:__.___"));
    }

    private void isFieldCorrect(boolean date) {
        isDateGood = date;
        textFieldMask.setForeground(isDateGood ? Color.BLACK : Color.RED);
    }

    public void setCalendar(Calendar cal) {
        if (cal == null) {
            textFieldMask.setValue(null);
            textFieldMask.setText("");
            calendar = null;
        } else {
            calendar = Calendar.getInstance();
            calendar.setTime(cal.getTime());
            textFieldMask.setText(simpleDateF.format(cal.getTime()));
        }
    }

    private void setCalendarCL(Calendar cal) {
        if (cal != null) {
            calendar = Calendar.getInstance();
            calendar.setTime(cal.getTime());
            textFieldMask.setText(simpleDateF.format(cal.getTime()));
        }
    }

    public void setDate(Date date) {
        if (date == null) {
            textFieldMask.setValue(null);
            calendar = null;
        } else {
            calendar = Calendar.getInstance();
            calendar.setTime(date);
            textFieldMask.setText(simpleDateF.format(calendar.getTime()));
        }
    }

    public JFormattedTextField getEditor() {
        return textFieldMask;
    }

    public boolean isDateCorrect(String date) {
        try {
            simpleDateFormat.parse(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Calendar getCalendar() {
        return (calendar == null) ? null : calendar;
    }

    public Date getDate() {
        if (calendar == null) {
            return null;
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(calendar.getTime());
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            return cal.getTime();
        }
    }

    public Date getDateAndTime() {
        return (calendar == null) ? null : calendar.getTime();
    }

    private MaskFormatter createFormatter(String s) {
        MaskFormatter formatter = null;
        try {
            formatter = new MaskFormatter(s);
            formatter.setPlaceholderCharacter('_');
        } catch (java.text.ParseException e) {
            Logger.getLogger(MaskFormatter.class.getName()).log(Level.WARNING, "", e);
        }
        return formatter;
    }

    public boolean isTimeCorrect(String time[]) {
        boolean intermediateValue = Integer.parseInt(time[0]) <= 23 && Integer.parseInt(time[1]) <= 59;
        String[] secAndMillis = time[2].split("\\.");
        if(secAndMillis.length == 1) {
            return intermediateValue && Integer.parseInt(time[2]) <= 59;
        } else {
            return intermediateValue && Integer.parseInt(secAndMillis[0]) <= 59 && Integer.parseInt(secAndMillis[1]) <= 999;
        }
    }

    public void isDateValid(String source[]) {
        isFieldCorrect(true);
        if (textFieldMask.getText().contains("_")) {
            calendar = null;
            if (isEmptyDateField(textFieldMask.getText())) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        textFieldMask.getDocument().removeDocumentListener(documentListener);
                        textFieldMask.setText("");
                        textFieldMask.setValue(null);
                        textFieldMask.getDocument().addDocumentListener(documentListener);
                    }
                });
            } else {
                isDateGood = false;
            }
        } else {
            if ((useOfTime && !isTimeCorrect(source[1].split(":"))) || !isDateCorrect(source[0].replace('/', '.'))) {
                isFieldCorrect(false);
            } else {
                try {
                    calendar = Calendar.getInstance();
                    calendar.setTime(simpleDateF.parse(textFieldMask.getText()));
                } catch (ParseException ex) {
                    Logger.getLogger(DateAndTimePicker.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
