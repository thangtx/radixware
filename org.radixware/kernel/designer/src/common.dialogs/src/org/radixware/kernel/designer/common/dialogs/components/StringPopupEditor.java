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
import java.awt.Dimension;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;


public class StringPopupEditor extends JPopupMenu {

    private final JTextComponent editor;

    public StringPopupEditor(JTextComponent editor, int width, int height, int vsp, int hsp) {

        this.editor = editor;

        setLayout(new BorderLayout());

        final JScrollPane pane = new JScrollPane(editor, vsp, hsp);
        setPreferredSize(new Dimension(width, height));
        add(pane, BorderLayout.CENTER);
    }

    public StringPopupEditor(JTextComponent editor, int width, int height) {
        this(editor, width, height, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

    public JTextComponent getEditor() {
        return editor;
    }

    public void open(String value) {
        editor.setText(value);
    }

    public void open(Document document) {
        editor.setDocument(document);
    }
}
