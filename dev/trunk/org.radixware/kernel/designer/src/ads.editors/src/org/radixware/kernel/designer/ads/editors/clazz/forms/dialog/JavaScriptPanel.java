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

package org.radixware.kernel.designer.ads.editors.clazz.forms.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.text.EditorKit;
import org.netbeans.api.editor.mimelookup.MimeLookup;
import org.netbeans.modules.editor.NbEditorDocument;


public class JavaScriptPanel extends JPanel {

    private Component editor;
    private NbEditorDocument document;
    private JEditorPane editorPane;

    public JavaScriptPanel() {
        setLayout(new BorderLayout());

    }

    public void open(String code) {

        String mimeType = "text/javascript"; // NOI18N

        editorPane = new JEditorPane();

        editorPane.setContentType(mimeType);
        editorPane.setEditorKit(MimeLookup.getLookup(mimeType).lookup(EditorKit.class));
        document = new NbEditorDocument(mimeType);
        JToolBar tb = document.createToolbar(editorPane);
        editor = document.createEditor(editorPane);
        add(tb, BorderLayout.NORTH);
        add(editor, BorderLayout.CENTER);
        // EditorUI ui = Utilities.getEditorUI(editorPane);
        editorPane.setText(code);

    }

    public String getCode() {
        return editorPane == null ? null : editorPane.getText();
    }
}
