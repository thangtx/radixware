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

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.border.Border;

public class SettingsDialog {

    private JDialog dialog = null;
    private Object parent = null;
    private final JOptionPane pane;
    private final TraceFont traceFont;
    private final ColorScheme colorScheme;
    final JCheckBox cbMinimizeTraceColums;

    private class TraceFont {

        private Font font = null;
        private final JLabel lbParam = new JLabel();
        public final JPanel panel = new JPanel(new GridLayout(1, 2));

        public TraceFont() {
            panel.add(lbParam);
            JButton changeButton = new JButton(Messages.BTN_CHANGE);
            changeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Font newFont = FontChooser.showDialog(dialog, Messages.TITLE_CHOOSE_FONT, font);
                    setFont(newFont);
                }
            });
            panel.add(changeButton);
        }

        public void setFont(Font newFont) {
            font = newFont;
            lbParam.setText(font.getFontName() + ", " + font.getSize());
        }
    }

    private class ColorChooser extends JColorChooser {

        private final JDialog dialog;
        private final Color initialColor;

        public ColorChooser(JDialog parent, String title, Color initColor) {
            super();
            this.initialColor = initColor;
            this.setColor(initColor);
            JOptionPane pane = new JOptionPane(this, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
            dialog = new JDialog(parent, title, true);
            pane.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent e) {
                    if (!e.getPropertyName().equals(JOptionPane.VALUE_PROPERTY) || e.getNewValue() == null) {
                        return;
                    }
                    int res = ((Integer) (e.getNewValue())).intValue();
                    if (res == JOptionPane.CANCEL_OPTION) {
                        ColorChooser.this.setColor(initialColor);
                    }
                    dialog.dispose();
                }
            });
            dialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    ColorChooser.this.setColor(initialColor);
                    dialog.dispose();
                }
            });
            ActionListener actionListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    ColorChooser.this.setColor(initialColor);
                    dialog.dispose();
                }
            };
            KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
            dialog.getRootPane().registerKeyboardAction(actionListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
            dialog.setContentPane(pane);
            dialog.pack();
            dialog.setResizable(false);
            dialog.setLocationRelativeTo(null);
        }

        public Color getNewColor() {
            dialog.setVisible(true);
            return this.getColor();
        }
    }

    private class ColorScheme {

        public final Button bgColor1 = new Button();
        public final Button bgColor2 = new Button();
        public final Button bgSelColor = new Button();
        public final Button debugColor = new Button();
        public final Button eventColor = new Button();
        public final Button warningColor = new Button();
        public final Button errorColor = new Button();
        public final Button alarmColor = new Button();
        public final Button selColor = new Button();
        public final JPanel paramPanel = new JPanel(new GridLayout(9, 1, 0, 4));
        public final JPanel labelPanel = new JPanel(new GridLayout(9, 1, 0, 4));

        public ColorScheme() {
            Button[] buttons = {bgColor1, bgColor2, bgSelColor, selColor, debugColor,
                eventColor, warningColor, errorColor, alarmColor};
            for (Button button : buttons) {
                button.setPreferredSize(new Dimension(16, 10));
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Button bt = (Button) (e.getSource());
                        ColorChooser colorChooser = new ColorChooser(dialog, Messages.TITLE_CHOOSE_COLOR, bt.getBackground());
                        bt.setBackground(colorChooser.getNewColor());
                    }
                });
                paramPanel.add(button);
            }
            String[] names = {Messages.LBL_BG_COLOR1, Messages.LBL_BG_COLOR2, Messages.LBL_BG_SEL_COLOR,
                Messages.LBL_SEL_COLOR, Messages.LBL_DEBUG_COLOR, Messages.LBL_EVENT_COLOR,
                Messages.LBL_WARNING_COLOR, Messages.LBL_ERROR_COLOR, Messages.LBL_ALARM_COLOR};
            for (int i = 0; i < names.length; i++) {
                labelPanel.add(new JLabel(names[i], JLabel.LEFT));
            }
        }
    }

    public SettingsDialog(Object parent) {
        this.parent = parent;

        //panel for JOptionPane
        JPanel panel = new JPanel(new BorderLayout());

        JToolBar toolBar = new JToolBar();
        panel.add(toolBar, BorderLayout.NORTH);

        JTabbedPane tabPane = new JTabbedPane();
        JPanel generalPanel = new JPanel();
        tabPane.addTab(Messages.TAB_FONT, null, generalPanel, Messages.TAB_FONT);
        JPanel colorPanel = new JPanel();
        tabPane.addTab(Messages.TAB_COLOR, null, colorPanel, Messages.TAB_COLOR);
        panel.add(tabPane, BorderLayout.CENTER);

        //toolBar
        toolBar.setAlignmentX(JToolBar.LEFT_ALIGNMENT);
        toolBar.setFloatable(false);

        JButton defaultButton = new JButton(new ImageIcon(SettingsDialog.class.getResource("img/default.png")));
        defaultButton.setToolTipText(Messages.HINT_RESTORE_DEFAULTS);
        defaultButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restoreDefaultSettings();
            }
        });
        defaultButton.setFocusable(false);
        toolBar.add(defaultButton);

        JButton cancelButton = new JButton(new ImageIcon(SettingsDialog.class.getResource("img/reread.png")));
        cancelButton.setToolTipText(Messages.HINT_CANCEL);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                init();
            }
        });
        cancelButton.setFocusable(false);
        toolBar.add(cancelButton);

        //fontPanel
        generalPanel.setLayout(new BorderLayout());
        Border padding = BorderFactory.createEmptyBorder(10, 15, 10, 15);
        generalPanel.setBorder(padding);
        final JPanel innerPanel = new JPanel(new GridLayout(3, 1, 8, 8));
        traceFont = new TraceFont();
        innerPanel.add(new JLabel(Messages.LBL_FONT));
        innerPanel.add(traceFont.panel);

        cbMinimizeTraceColums = new JCheckBox(Messages.LBL_MINIMIZE_TRACE_COLS);
        innerPanel.add(cbMinimizeTraceColums);
        cbMinimizeTraceColums.setSelected(true);
        
        generalPanel.add(innerPanel, BorderLayout.NORTH);

        colorPanel.setLayout(new BorderLayout(10, 0));
        padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        colorPanel.setBorder(padding);
        colorScheme = new ColorScheme();
        colorPanel.add(colorScheme.paramPanel, BorderLayout.WEST);
        colorPanel.add(colorScheme.labelPanel, BorderLayout.CENTER);

        //JOptionPane for dialog content
        pane = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
        padding = BorderFactory.createEmptyBorder(0, 0, 10, 0);
        pane.setBorder(padding);
        pane.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if (!e.getPropertyName().equals(JOptionPane.VALUE_PROPERTY) || e.getNewValue() == null) {
                    return;
                }
                int res = ((Integer) (e.getNewValue())).intValue();
                if (res == JOptionPane.OK_OPTION) {
                    apply();
                }
                pane.setValue(null);
                dialog.setVisible(false);
            }
        });

    }

    private void restoreDefaultSettings() {
        traceFont.setFont(TraceSettings.defaultSettings.traceFont);
        colorScheme.bgColor1.setBackground(TraceSettings.defaultSettings.bgColor1);
        colorScheme.bgColor2.setBackground(TraceSettings.defaultSettings.bgColor2);
        colorScheme.bgSelColor.setBackground(TraceSettings.defaultSettings.bgSelColor);
        colorScheme.debugColor.setBackground(TraceSettings.defaultSettings.debugColor);
        colorScheme.eventColor.setBackground(TraceSettings.defaultSettings.eventColor);
        colorScheme.warningColor.setBackground(TraceSettings.defaultSettings.warningColor);
        colorScheme.errorColor.setBackground(TraceSettings.defaultSettings.errorColor);
        colorScheme.alarmColor.setBackground(TraceSettings.defaultSettings.alarmColor);
        colorScheme.selColor.setBackground(TraceSettings.defaultSettings.selColor);
        cbMinimizeTraceColums.setSelected(true);
    }

    private void apply() {
        TraceSettings.setCurrentSettings(new TraceSettings(traceFont.font, cbMinimizeTraceColums.isSelected(), colorScheme.bgColor1.getBackground(),
                colorScheme.bgColor2.getBackground(), colorScheme.bgSelColor.getBackground(), colorScheme.debugColor.getBackground(),
                colorScheme.eventColor.getBackground(), colorScheme.warningColor.getBackground(), colorScheme.errorColor.getBackground(),
                colorScheme.alarmColor.getBackground(), colorScheme.selColor.getBackground()));
    }

    private void init() {
        traceFont.setFont(TraceSettings.getCurrentSettings().traceFont);
        colorScheme.bgColor1.setBackground(TraceSettings.getCurrentSettings().bgColor1);
        colorScheme.bgColor2.setBackground(TraceSettings.getCurrentSettings().bgColor2);
        colorScheme.bgSelColor.setBackground(TraceSettings.getCurrentSettings().bgSelColor);
        colorScheme.debugColor.setBackground(TraceSettings.getCurrentSettings().debugColor);
        colorScheme.eventColor.setBackground(TraceSettings.getCurrentSettings().eventColor);
        colorScheme.warningColor.setBackground(TraceSettings.getCurrentSettings().warningColor);
        colorScheme.errorColor.setBackground(TraceSettings.getCurrentSettings().errorColor);
        colorScheme.alarmColor.setBackground(TraceSettings.getCurrentSettings().alarmColor);
        colorScheme.selColor.setBackground(TraceSettings.getCurrentSettings().selColor);
        cbMinimizeTraceColums.setSelected(TraceSettings.getCurrentSettings().minimizeTraceColumns);
    }

    public void show() {
        init();
        if (dialog == null) {
            while (parent != null && !(parent instanceof Frame)) {
                parent = ((Component) parent).getParent();
            }
            dialog = new JDialog((Frame) parent, Messages.TITLE_SETTINGS, true);
            dialog.setContentPane(pane);
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setResizable(false);
            ActionListener actionListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    dialog.setVisible(false);
                }
            };
            KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
            dialog.getRootPane().registerKeyboardAction(actionListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
        }
        dialog.setVisible(true);
    }

    private static final class Messages {

        static {
            final ResourceBundle bundle = ResourceBundle.getBundle("org.radixware.kernel.server.widgets.mess.messages");

            TITLE_SETTINGS = bundle.getString("TITLE_SETTINGS");
            TITLE_CHOOSE_FONT = bundle.getString("TITLE_CHOOSE_FONT");
            TITLE_CHOOSE_COLOR = bundle.getString("TITLE_CHOOSE_COLOR");

            LBL_BG_COLOR1 = bundle.getString("LBL_BG_COLOR1");
            LBL_BG_COLOR2 = bundle.getString("LBL_BG_COLOR2");
            LBL_BG_SEL_COLOR = bundle.getString("LBL_BG_SEL_COLOR");
            LBL_DEBUG_COLOR = bundle.getString("LBL_DEBUG_COLOR");
            LBL_EVENT_COLOR = bundle.getString("LBL_EVENT_COLOR");
            LBL_WARNING_COLOR = bundle.getString("LBL_WARNING_COLOR");
            LBL_ERROR_COLOR = bundle.getString("LBL_ERROR_COLOR");
            LBL_ALARM_COLOR = bundle.getString("LBL_ALARM_COLOR");
            LBL_SEL_COLOR = bundle.getString("LBL_SEL_COLOR");

            BTN_CHANGE = bundle.getString("BTN_CHANGE");

            TAB_FONT = bundle.getString("TAB_FONT");
            TAB_COLOR = bundle.getString("TAB_COLOR");

            HINT_CANCEL = bundle.getString("HINT_CANCEL");
            HINT_RESTORE_DEFAULTS = bundle.getString("HINT_RESTORE_DEFAULTS");
            LBL_FONT = bundle.getString("LBL_FONT");
            LBL_MINIMIZE_TRACE_COLS = bundle.getString("LBL_MINIMIZE_TRACE_COLS");
        }
        static final String TITLE_SETTINGS;
        static final String TITLE_CHOOSE_FONT;
        static final String TITLE_CHOOSE_COLOR;
        static final String LBL_BG_COLOR1;
        static final String LBL_BG_COLOR2;
        static final String LBL_BG_SEL_COLOR;
        static final String LBL_DEBUG_COLOR;
        static final String LBL_EVENT_COLOR;
        static final String LBL_WARNING_COLOR;
        static final String LBL_ERROR_COLOR;
        static final String LBL_ALARM_COLOR;
        static final String LBL_SEL_COLOR;
        static final String LBL_FONT;
        static final String LBL_MINIMIZE_TRACE_COLS;
        static final String BTN_CHANGE;
        static final String TAB_FONT;
        static final String TAB_COLOR;
        static final String HINT_CANCEL;
        static final String HINT_RESTORE_DEFAULTS;
    }
}
