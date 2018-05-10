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

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main {

    public static void main(String[] args) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        JFrame frame = new JFrame("Test DateAndTimePicker");
        
        JPanel panel = new JPanel();
        panel.setLayout(new net.miginfocom.swing.MigLayout());
        
        final DateAndTimePicker dateAndTimePickerMillis = new DateAndTimePicker(DateAndTimePicker.EOperatingMode.DATE_AND_TIME_MILLISECONDS);
        final DateAndTimePicker dateAndTimePicker = new DateAndTimePicker(DateAndTimePicker.EOperatingMode.DATE_AND_TIME);
        final DateAndTimePicker datePicker = new DateAndTimePicker(DateAndTimePicker.EOperatingMode.DATE);
        
        JButton dateAndTimeButton = new JButton("Print date and time");
        JButton dateButton = new JButton("Print date");
        JButton dateAndTimeMillisButton = new JButton("Print date and time millis");
        
        panel.add(dateAndTimePickerMillis);
        panel.add(dateAndTimePicker);
        panel.add(datePicker, "wrap");
        panel.add(dateAndTimeMillisButton, "wrap");
        panel.add(dateAndTimeButton, "wrap");
        panel.add(dateButton, "wrap");
                
        dateAndTimeMillisButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.err.println(dateAndTimePickerMillis.getCalendar());
            }

        });
        
        dateAndTimeButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.err.println(dateAndTimePicker.getCalendar());
            }

        });

        dateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.err.println(datePicker.getCalendar());
            }
        });
        
        frame.setPreferredSize(new Dimension(420, 150));
        frame.setSize(new Dimension(420, 150));
        frame.add(panel);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
