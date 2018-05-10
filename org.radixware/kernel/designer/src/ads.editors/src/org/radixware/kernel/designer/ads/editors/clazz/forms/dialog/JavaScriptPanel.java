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
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.BoxLayout;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.text.EditorKit;
import org.netbeans.api.editor.mimelookup.MimeLookup;
import org.netbeans.editor.Utilities;
import org.netbeans.modules.editor.NbEditorDocument;
import org.openide.text.CloneableEditorSupport;


public class JavaScriptPanel extends JPanel {

    private Component editor;
    private NbEditorDocument document;
    private JEditorPane editorPane;
    JToolBar tb;

    public JavaScriptPanel() {
        setLayout(new BorderLayout());
        
        String mimeType = "text/javascript"; // NOI18N

        editorPane = new JEditorPane();
        editorPane.setContentType(mimeType);
//        editorPane.setEditorKit(MimeLookup.getLookup(mimeType).lookup(EditorKit.class));
        document = new NbEditorDocument(mimeType);
        tb = document.createToolbar(editorPane);
        editor = document.createEditor(editorPane);
        add(tb, BorderLayout.NORTH);
        add(editor, BorderLayout.CENTER);
//        add(editorPane, BorderLayout.CENTER);
    }

    public void open(String code) {

        // EditorUI ui = Utilities.getEditorUI(editorPane);
        editorPane.setText(code);
    }

    public String getCode() {
        return editorPane == null ? null : editorPane.getText();
    }

    @Override
    protected void validateTree() {
        super.validateTree();
        update();
    }

    private void update(){
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                editor.revalidate();
                editor.repaint();
                tb.revalidate();
                tb.repaint();
            }
        });
    }
   
}
