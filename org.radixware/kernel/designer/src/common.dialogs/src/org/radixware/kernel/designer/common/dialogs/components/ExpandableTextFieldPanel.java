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

import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.text.JTextComponent;
import org.radixware.kernel.designer.common.dialogs.components.values.EditorLayout;


public class ExpandableTextFieldPanel extends JPanel {

    private final JTextField lineEditor = new JTextField();
    private final JButton expandButton = new JButton("...");
    int height = 200;
    private StringPopupEditor popupEditor;

    public ExpandableTextFieldPanel() {

        super.setLayout(new EditorLayout());
        add(lineEditor, EditorLayout.LEADER_CONSTRAINT);
        add(expandButton);

        expandButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                invoke();
            }
        });
    }

    private StringPopupEditor getPopupEditor(int width, int height) {
        final JTextArea textArea = new JTextArea();
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        final StringPopupEditor popup = new StringPopupEditor(textArea, width, height,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        popup.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        setCaretPosition(popup.getEditor(), getCaretPosition(lineEditor));
                        popup.getEditor().requestFocusInWindow();
                    }
                });
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                popupEditor = null;

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        lineEditor.requestFocusInWindow();
                    }
                });
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
                popupMenuWillBecomeInvisible(e);
            }
        });

        return popup;
    }

    public void invoke() {

        if (popupEditor != null) {
            popupEditor.hide();
            return;
        }

        popupEditor = getPopupEditor(lineEditor.getWidth(), height);
        popupEditor.open(lineEditor.getDocument());
        popupEditor.show(this, 0, lineEditor.getHeight());

    }

    public void setReadOnly(boolean readonly) {
        this.lineEditor.setEditable(!readonly);

        this.expandButton.setEnabled(!readonly);
    }

    private int getCaretPosition(JTextComponent component) {
        if (component != null) {
            return component.getCaretPosition();
        }
        return -1;
    }

    private void setCaretPosition(JTextComponent component, int caretPosition) {
        if (component != null && caretPosition >= 0) {
            component.setCaretPosition(caretPosition);
        }
    }

    @Override
    public final void setLayout(LayoutManager mgr) {
    }

    public JTextField getTextEditor() {
        return lineEditor;
    }

    public void setText(String t) {
        lineEditor.setText(t);
    }

    public String getText() {
        return lineEditor.getText();
    }
}
