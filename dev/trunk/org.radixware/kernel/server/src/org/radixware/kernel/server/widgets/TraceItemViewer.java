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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.radixware.kernel.common.trace.TraceItem;
import org.radixware.kernel.common.utils.Hex;
import org.radixware.kernel.server.trace.ViewTraceItem;

public final class TraceItemViewer {

    private final JTextField edType;
    private final JTextField edDate;
    private final JTextField edTime;
    private final JTextField edSource;
    private final JTextField edContext;
    private final JTextArea edMessage;
    private String xmlDocument;
    private final JButton btStartXmlViewer;
    private final JButton btDecodeHex;
    private Object parent;
    private final JOptionPane pane;
    private JDialog dialog = null;
    private final XMLViewer xmlViewer;

    public TraceItemViewer(Object parent) {
        this.parent = parent;
        xmlDocument = "";
        xmlViewer = new XMLViewer(parent);

        final JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new GridLayout(5, 1, 0, 3));

        final JPanel textPanel = new JPanel();
        textPanel.setLayout(new GridLayout(5, 1, 0, 3));

        final JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout(10, 0));
        topPanel.add(labelPanel, BorderLayout.WEST);
        topPanel.add(textPanel, BorderLayout.CENTER);

        final JLabel lbType = new JLabel(Messages.COL_SEVERITY);
        lbType.setHorizontalAlignment(SwingConstants.RIGHT);
        labelPanel.add(lbType);
        edType = new JTextField();
        lbType.setLabelFor(edType);
        textPanel.add(edType);

        final JLabel lbDate = new JLabel(Messages.COL_DATE);
        lbDate.setHorizontalAlignment(SwingConstants.RIGHT);
        labelPanel.add(lbDate);
        edDate = new JTextField();
        lbDate.setLabelFor(edDate);
        textPanel.add(edDate);

        final JLabel lbTime = new JLabel(Messages.COL_TIME);
        lbTime.setHorizontalAlignment(SwingConstants.RIGHT);
        labelPanel.add(lbTime);
        edTime = new JTextField();
        lbTime.setLabelFor(edTime);
        textPanel.add(edTime);

        final JLabel lbSource = new JLabel(Messages.COL_SOURCE);
        lbSource.setHorizontalAlignment(SwingConstants.RIGHT);
        labelPanel.add(lbSource);
        edSource = new JTextField();
        lbSource.setLabelFor(edSource);
        textPanel.add(edSource);

        final JLabel lblContext = new JLabel(Messages.COL_CONTEXT);
        lblContext.setHorizontalAlignment(SwingConstants.RIGHT);
        labelPanel.add(lblContext);
        edContext = new JTextField();
        lblContext.setLabelFor(edContext);
        textPanel.add(edContext);

        final JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());

        final JLabel lbMessage = new JLabel(Messages.COL_MESSAGE);
        lbMessage.setHorizontalAlignment(SwingConstants.LEFT);
        bottomPanel.add(lbMessage, BorderLayout.NORTH);

        edMessage = new JTextArea();
        final JScrollPane scrollPane = new JScrollPane(edMessage);
        bottomPanel.add(scrollPane, BorderLayout.CENTER);

        final JPanel buttonBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btStartXmlViewer = new JButton(Messages.BTN_SHOW_XML_TREE);
        btStartXmlViewer.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        xmlViewer.show(xmlDocument);
                    }
                });
        buttonBar.add(btStartXmlViewer);

        btDecodeHex = new JButton(Messages.BTN_DECODE_HEX);
        btDecodeHex.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final String selectedText = edMessage.getSelectedText();
                if (selectedText != null) {
                    byte[] data;
                    try {
                        data = Hex.decode(selectedText);
                    } catch (RuntimeException ex) {
                        data = null;
                    }
                    if (data != null) {
                        showBytesAsString(data);
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Selected text is not a HEX string");
                    }
                } else {
                    JOptionPane.showMessageDialog(dialog, "No selection");
                }
            }
        });
        buttonBar.add(btDecodeHex);

        edMessage.getCaret().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                btDecodeHex.setEnabled(edMessage.getSelectionStart() != edMessage.getSelectionEnd());
            }
        });

        final JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(bottomPanel, BorderLayout.CENTER);
        panel.add(buttonBar, BorderLayout.SOUTH);

        final String[] name = {Messages.BTN_CLOSE};
        pane = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.CLOSED_OPTION, null, name, name[0]);
        pane.addPropertyChangeListener(
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent e) {
                        if (!e.getPropertyName().equals(JOptionPane.VALUE_PROPERTY) || e.getNewValue() == null) {
                            return;
                        }
                        String t = e.getNewValue().toString();
                        if (t.equals(Messages.BTN_CLOSE)) {
                            pane.setValue(null);
                            dialog.setVisible(false);
                        }
                    }
                });
    }

    private void showBytesAsString(final byte[] data) {
        final JPanel panel = new JPanel(new BorderLayout());
        final JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        final JComboBox cbCharset = new JComboBox(Charset.availableCharsets().keySet().toArray());
        final JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);

        final JButton btShowAsXml = new JButton("Show as XML");
        toolbar.add(new JLabel("Charset:"));

        toolbar.add(cbCharset);
        final String defaultEncoding = "UTF-8";
        cbCharset.setSelectedItem(defaultEncoding);
        final ItemListener listener = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                try {
                    textArea.setFont(TraceSettings.getCurrentSettings().traceFont);
                    final String str = new String(data, cbCharset.getSelectedItem().toString());
                    textArea.setText(str);
                    btShowAsXml.setEnabled(getXML(textArea.getSelectedText() == null ? textArea.getText() : textArea.getSelectedText()) != null);
                } catch (UnsupportedEncodingException ex) {
                    JOptionPane.showMessageDialog(dialog, ex.getMessage());
                }
            }
        };
        cbCharset.addItemListener(listener);
        listener.itemStateChanged(null);

        toolbar.add(btShowAsXml);
        btShowAsXml.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final String xml = getXML(textArea.getSelectedText() == null ? textArea.getText() : textArea.getSelectedText());
                if (xml != null) {
                    xmlViewer.show(xml);
                }
            }
        });

        final JCheckBox cbWrapLines = new JCheckBox("Wrap lines", false);
        cbWrapLines.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setLineWrap(cbWrapLines.isSelected());
            }
        });

        toolbar.add(cbWrapLines);

        toolbar.setLayout(new FlowLayout(FlowLayout.LEADING, 4, 0));

        panel.add(toolbar, BorderLayout.NORTH);

        final JDialog internalDialog = new JDialog(dialog, "Decoded data");
        internalDialog.setMinimumSize(new Dimension(400, 300));
        internalDialog.setContentPane(panel);
        internalDialog.setLocationRelativeTo(null);
        internalDialog.pack();
        internalDialog.setModal(true);
        internalDialog.setVisible(true);
    }

    public void show(final TraceItem traceItem) {
        edType.setText(traceItem.severity.getName());
        edDate.setText(new SimpleDateFormat("dd/MM/yy").format(new Date(traceItem.time)));
        edTime.setText(new SimpleDateFormat("HH:mm:ss.SSS").format(new Date(traceItem.time)));
        edSource.setText(traceItem.source);
        edContext.setText(traceItem instanceof ViewTraceItem ? ((ViewTraceItem) traceItem).context : "");
        final String mess = traceItem.getMess();
        edMessage.setFont(TraceSettings.getCurrentSettings().traceFont);
        edMessage.setText(mess);
        xmlDocument = getXML(mess);
        btStartXmlViewer.setEnabled(xmlDocument != null);
        if (dialog == null) {
            while (parent != null && !(parent instanceof Frame)) {
                parent = ((Component) parent).getParent();
            }
            dialog = new JDialog((Frame) parent, Messages.TITLE_SERVER_MESSAGE, true);
            dialog.setContentPane(pane);
            dialog.setLocationRelativeTo((Frame) parent);
            dialog.setResizable(true);
            dialog.setMinimumSize(new Dimension(600, 500));
            dialog.pack();
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

    private String getXML(String str) {
        if (str != null /* && str.matches("(.)*<(.)*>(.)*</(.)*>(.)*")*/) {
            int i1 = str.indexOf('<');
            int i2 = str.lastIndexOf('>');
            if (i1 != -1 && i2 != -1 && i1 < i2) {
                return str.substring(i1, i2 + 1);
            }
        }
        return null;
    }

    private static final class Messages {

        static {
            final ResourceBundle bundle = ResourceBundle.getBundle("org.radixware.kernel.server.widgets.mess.messages");

            COL_SEVERITY = bundle.getString("COL_SEVERITY");
            COL_DATE = bundle.getString("COL_DATE");
            COL_TIME = bundle.getString("COL_TIME");
            COL_SOURCE = bundle.getString("COL_SOURCE");
            COL_MESSAGE = bundle.getString("COL_MESSAGE");
            COL_CONTEXT = bundle.getString("COL_CONTEXT");
            BTN_CLOSE = bundle.getString("BTN_CLOSE");
            TITLE_SERVER_MESSAGE = bundle.getString("TITLE_SERVER_MESSAGE");
            BTN_SHOW_XML_TREE = bundle.getString("BTN_SHOW_XML_TREE");
            BTN_DECODE_HEX = bundle.getString("BTN_DECODE_HEX");
        }
        static final String COL_SEVERITY;
        static final String COL_DATE;
        static final String COL_TIME;
        static final String COL_SOURCE;
        static final String COL_MESSAGE;
        static final String COL_CONTEXT;
        static final String BTN_CLOSE;
        static final String BTN_SHOW_XML_TREE;
        static final String BTN_DECODE_HEX;
        static final String TITLE_SERVER_MESSAGE;
    }
}
