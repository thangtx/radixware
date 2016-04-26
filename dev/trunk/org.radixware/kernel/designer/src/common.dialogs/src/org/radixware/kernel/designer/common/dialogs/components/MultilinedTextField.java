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

package org.radixware.kernel.designer.common.dialogs.components;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.SwingUtilities;
import javax.swing.text.PlainDocument;
import org.openide.windows.WindowManager;
import org.radixware.kernel.common.components.ExtendableTextField;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.common.dialogs.spellchecker.Spellchecker;


public class MultilinedTextField extends ExtendableTextField {

    protected javax.swing.JTextField field;
    private javax.swing.JTextArea area;
    private Dialog dialog;
    private final char SQUARE = '\uFFFD';
    private final char NEWLINE = '\n';

    protected String replaceNewLine2Square(String str) {
        if ((str != null) && (str.indexOf(NEWLINE) >= 0)) {
            StringBuffer filtered = new StringBuffer(str);
            int n = filtered.length();
            for (int i = 0; i < n; i++) {
                if (filtered.charAt(i) == NEWLINE) {
                    filtered.setCharAt(i, SQUARE);
                }
            }
            str = filtered.toString();
        }
        return str;
    }

    protected String replaceSquares2NewLines(String str) {
        if ((str != null) && (str.indexOf(SQUARE) >= 0)) {
            StringBuffer filtered = new StringBuffer(str);
            int n = filtered.length();
            for (int i = 0; i < n; i++) {
                if (filtered.charAt(i) == SQUARE) {
                    filtered.setCharAt(i, NEWLINE);
                }
            }
            str = filtered.toString();
        }
        return str;
    }
    private boolean saveNewLines = true;

    public boolean getSaveNewLines() {
        return saveNewLines;
    }

    public void setSaveNewLines(boolean saveNewLines) {
        this.saveNewLines = saveNewLines;
    }

    public void setRowCount(int rows) {
        area.setRows(rows);
    }

    public MultilinedTextField() {
        this(false, EIsoLanguage.ENGLISH, null);
    }

    protected MultilinedTextField(boolean saveNewLines, EIsoLanguage language, RadixObject context) {
        super();
        this.saveNewLines = saveNewLines;
        field = new javax.swing.JTextField();
        PlainDocument newDoc = new PlainDocument();
        if (!saveNewLines) {
            newDoc.putProperty("filterNewlines", Boolean.FALSE);
        }
        area = new javax.swing.JTextArea();
        Spellchecker.register(area, language, context);
        area.setLineWrap(true);

        setEditor(field);
        addButton(RadixWareIcons.DIALOG.CHOOSE.getIcon(16, 16)).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MultilinedTextField.this.showTextArea();
            }
        });

        area.setRows(4);
        dialogHeight = area.getPreferredSize().height + 2;
    }
    private int dialogHeight;
    private final Object lock = new Object();
    private final ComponentAdapter rootFrameListener = new ComponentAdapter() {
        @Override
        public void componentMoved(ComponentEvent e) {
            closeTextArea();
        }

        @Override
        public void componentResized(ComponentEvent e) {
            closeTextArea();
        }
    };

    private void closeTextArea() {
        synchronized (lock) {
            WindowManager.getDefault().getMainWindow().removeComponentListener(rootFrameListener);
            dialog.setVisible(false);
            String oldText = field.getText();
            String newText = saveNewLines ? replaceNewLine2Square(area.getText()) : area.getText();
            if (!oldText.equals(newText)) {
                field.setText(newText);
            }
        }
    }

    private void prepareDialog() {
        dialog = new Dialog(WindowManager.getDefault().getMainWindow());
        dialog.setUndecorated(true);
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowDeactivated(WindowEvent e) {

                super.windowDeactivated(e);
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        closeTextArea();
                    }
                });
            }
        });
        javax.swing.JScrollPane scroll = new javax.swing.JScrollPane();
        scroll.setViewportView(area);
        dialog.setLayout(new BorderLayout());
        dialog.add(scroll, BorderLayout.CENTER);

    }

    private void showTextArea() {
        synchronized (lock) {
            WindowManager.getDefault().getMainWindow().addComponentListener(rootFrameListener);
            final Point point = field.getLocationOnScreen();
            if (dialog == null) {
                prepareDialog();
            }
            dialog.setBounds(
                    (int) point.getX(),
                    (int) point.getY() + field.getHeight(),
                    field.getWidth(),
                    dialogHeight);

            dialog.setVisible(true);
            dialog.toFront();
            area.setText(saveNewLines ? replaceSquares2NewLines(field.getText()) : field.getText());
            area.requestFocusInWindow();

        }
    }

    public String getText() {
        if (dialog != null
                && dialog.isVisible()) {
            return area.getText();
        }
        return saveNewLines ? replaceSquares2NewLines((String) this.getValue()) : (String) this.getValue();
    }

    public void setText(String text) {
        if (saveNewLines) {
            super.setValue(replaceNewLine2Square(text));
        } else {
            super.setValue(text);
        }
    }
}
