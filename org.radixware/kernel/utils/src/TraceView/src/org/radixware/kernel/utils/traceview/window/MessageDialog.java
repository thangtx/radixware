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
package org.radixware.kernel.utils.traceview.window;

import java.awt.BorderLayout;
import org.radixware.kernel.utils.traceview.TraceEvent;
import org.radixware.kernel.utils.traceview.TraceViewSettings;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToolBar;

class MessageDialog extends JFrame {

    private JTextArea text = new JTextArea();
    private JEditorPane textXml = new JEditorPane();
    private JTabbedPane tabbedPane;
    private JScrollPane scroll;
    private JScrollPane scrollXml;
    private JPanel window;
    private JButton next;
    private JButton previous;
    private JToolBar toolbar;
    int currentRow;
    JTable tab;

    KeyListener keyListener = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                setVisible(false);
                dispose();
            }
        }
    };

    public MessageDialog(String message, JTable table, int rowIndex) {
        super(TraceViewSettings.DATE_FORMAT_GUI.get().format(((TraceEvent.IndexedDate) table.getModel().getValueAt(table.convertRowIndexToModel(rowIndex), 1)).getDate()));
        toolbar = new JToolBar();
        window = new JPanel();
        tab = table;
        currentRow = rowIndex;
        ImageIcon iconPrev = TraceViewSettings.EIcon.PREV.getIcon();
        ImageIcon iconNext = TraceViewSettings.EIcon.NEXT.getIcon();
        JPanel menuButtons = new JPanel();
        tabbedPane = new JTabbedPane();
        text.setText(message);
        scroll = new JScrollPane(text);
        scrollXml = new JScrollPane(textXml);
        window.setLayout(new BorderLayout());
        menuButtons.setLayout(new BoxLayout(menuButtons, BoxLayout.X_AXIS));
        textXml.setContentType("text/html");
        previous = new JButton(iconPrev);
        previous.setContentAreaFilled(false);
        previous.setFocusable(false); 
        next = new JButton(iconNext);
        next.setContentAreaFilled(false);
        next.setFocusable(false); 
        text.setEditable(false);
        textXml.setEditable(false);
        setMinimumSize(new Dimension(300, 200));
        setMaximumSize(new Dimension(800, 600));
        text.setPreferredSize(new Dimension(text.getPreferredSize().width, text.getPreferredSize().height));
        textXml.setPreferredSize(new Dimension(textXml.getPreferredSize().width, textXml.getPreferredSize().height));
        toolbar.setFloatable(false);
        toolbar.add(menuButtons, BorderLayout.LINE_START);
        menuButtons.add(previous);
        menuButtons.add(next);
        window.add(toolbar, BorderLayout.PAGE_START);
        tabbedPane.addTab("Data", scroll);
        setTextXml(message);
        setSize(800, 600);
        setLocationRelativeTo(table.getParent());
        setVisible(true);
        add(window);

        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentRow < tab.getRowCount() - 1) {
                    previous.setEnabled(true);
                    currentRow += 1;
                    setTitle(TraceViewSettings.DATE_FORMAT_GUI.get().format(((TraceEvent.IndexedDate) tab.getModel().getValueAt(tab.convertRowIndexToModel(currentRow), 1)).getDate()));
                    setTextXml((String) tab.getValueAt(currentRow, 4));
                } else {
                    next.setEnabled(false);
                }
            }
        });

        previous.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentRow > 0) {
                    next.setEnabled(true);
                    currentRow -= 1;
                    setTitle(TraceViewSettings.DATE_FORMAT_GUI.get().format(((TraceEvent.IndexedDate) tab.getModel().getValueAt(tab.convertRowIndexToModel(currentRow), 1)).getDate()));
                    setTextXml((String) tab.getValueAt(currentRow, 4));
                } else {
                    previous.setEnabled(false);
                }
            }
        });

        text.addKeyListener(keyListener);
    }

    private void setTextXml(String message) {
        text.setText(message);
        if (checkisAlreadyFormat(message)) {
            textXml.setText(syntaxHylightning(message));
        } else {
            textXml.setText(formatStrToXmlFormat(message));
        }

        tabbedPane.addTab("XML (test)", scrollXml);
        window.add(tabbedPane);
    }

    private boolean checkisAlreadyFormat(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '\n') {
                return true;
            }
        }
        return false;
    }

    private String syntaxHylightning(String str) {
        boolean isSecondQoute = false, isBetweenTags = false;
        StringBuilder result = new StringBuilder();
        int i = 0;
        result.append("<div style='white-space:nowrap;font-family:\"Monospaced\"'>");
        while (i < str.length() - 2) {

            switch (str.charAt(i)) {
                case '>':
                    isBetweenTags = true;
                    result.append("</font>&gt;</font>");
                    break;
                case '<':
                    isBetweenTags = false;
                    result.append("<font color = \"blue\">&lt;<font color = \"Orange\">");
                    break;
                case '"':
                    if (isSecondQoute == false) {
                        result.append("<font color = \"green\">\"");
                        isSecondQoute = true;
                        break;
                    }
                    if (isSecondQoute == true) {
                        result.append("\"</font>");
                        isSecondQoute = false;
                        break;
                    }
                case '\n':
                    result.append("<br>");
                    break;
                case ' ':
                    if (isBetweenTags) {
                        result.append("&emsp;");
                    } else {
                        result.append(" ");
                    }
                    break;
                default:
                    result.append(str.charAt(i));
                    break;
            }
            i++;
        }
        result.append(str.charAt(str.length() - 2));
        result.append("</font>");
        result.append(str.charAt(str.length() - 1));
        result.append("</div>");
        return result.toString();
    }

    private String formatStrToXmlFormat(String str) {
        char[] chArray = str.toCharArray();
        int t = 0, k = 0;
        boolean isEndTag = false, isSecondQoute = false;
        StringBuilder result = new StringBuilder();
        result.append("<div style='white-space:nowrap;font-family:\"Monospaced\"'>");
        for (k = 0; k < chArray.length; k++) {
            if (chArray[k] == '<') {
                result.append("<br>");
                break;
            } else {
                result.append(chArray[k]);
            }
        }
        if (chArray.length > 3 && k != chArray.length - 1) {
            int i = k;
            while (i < chArray.length - 2) {
                if (chArray[i] != ' ' || chArray[i] != '\t' || chArray[i] != '\n') {
                    if (chArray[i] == '<') {
                        result.append("<font color = \"blue\">");
                    }
                    if (chArray[i] == '"' && isSecondQoute == false) {
                        result.append("<font color = \"green\">\"");
                        isSecondQoute = true;
                        i++;
                    }
                    if (chArray[i] == '>') {
                        result.append("</font>");
                        if (chArray[i + 1] != '<') {
                            t += 1;
                        }
                        while (chArray[i + 1] != '<') {
                            result.append(chArray[i] == '>' ? "&gt;</font>" : chArray[i]);
                            i++;
                        }
                    }
                    if (chArray[i] == '>' && chArray[i + 2] != '/' && chArray[i + 1] == '<' && chArray[i - 1] != '/') {
                        t += 1;
                        result.append("&gt; </font><br>"); // &gt == >
                        for (int j = 0; j < t; j++) {
                            result.append("&emsp;");
                        }
                        isEndTag = false;
                    } else if (chArray[i] == '<' && chArray[i + 1] == '/') {
                        if (isEndTag) {
                            t -= 1;
                            result.append("<br>");
                            for (int j = 0; j <= t; j++) {
                                result.append("&emsp;");
                            }
                        } else {
                            t -= 2;
                        }
                        isEndTag = true;
                        result.append("&lt;<font color = \"Orange\">");// &lt == <
                    } else if (chArray[i] == '>' && chArray[i - 1] == '/') {
                        result.append("&gt;</font> <br>");
                        for (int j = 0; j < t; j++) {
                            result.append("&emsp;");
                        }
                        isEndTag = false;
                    } else if (chArray[i] == '<') {
                        result.append("&lt;<font color = \"Orange\">");
                    } else {
                        result.append(chArray[i]);
                        if (chArray[i] == '"' && isSecondQoute == true) {
                            result.append("</font>");
                            isSecondQoute = false;
                        }
                        if (chArray[i] == '>') {
                            result.append("</font>");
                        }
                    }
                }
                i++;
            }
            if (k != chArray.length) {
                result.append(chArray[chArray.length - 2]);
                result.append("</font>");
                result.append(chArray[chArray.length - 1]);
            }
        } else {
            for (int i = 0; i < chArray.length; i++) {
                result.append(chArray[i]);
            }
        }
        result.append("</div>");
        return result.toString();
    }
}
