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
package org.radixware.kernel.designer.common.dialogs.mmlnb;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.text.*;
import org.netbeans.editor.*;
import org.netbeans.editor.ext.java.*;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditor;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditorKit;

public class MmlKit extends ScmlEditorKit {

    public MmlKit() {
    }

    @Override
    protected void initDocument(BaseDocument doc) {
        doc.putProperty(SyntaxUpdateTokens.class,
                new SyntaxUpdateTokens() {
                    private List tokenList = new ArrayList();

                    @Override
                    public void syntaxUpdateStart() {
                        tokenList.clear();
                    }

                    @Override
                    public List syntaxUpdateEnd() {
                        return tokenList;
                    }

                    @Override
                    public void syntaxUpdateToken(TokenID id, TokenContextPath contextPath, int offset, int length) {
                        if (JavaTokenContext.LINE_COMMENT == id) {
                            tokenList.add(new SyntaxUpdateTokens.TokenInfo(id, contextPath, offset, length));
                        }
                    }
                });
    }

    @Override
    public Document createDefaultDocument() {
        return new MmlDocument(getContentType());
    }

    @Override
    protected Action[] createActions() {
        Action[] superActions = super.createActions();
        Action[] javaActions = new Action[]{
            new JavaDeleteCharAction(deletePrevCharAction, false),
            new JavaDeleteCharAction(deleteNextCharAction, true)
        };

        return TextAction.augmentList(superActions, javaActions);
    }

    @Override
    public String getContentType() {
        return ScmlEditor.MML_MIME_TYPE;
    }

    public static class JavaDeleteCharAction extends ScmlEditorKit.ScmlDeleteCharAction {

        public JavaDeleteCharAction(String nm, boolean nextChar) {
            super(nm, nextChar);
        }

        @Override
        public void actionPerformed(final ActionEvent evt, final JTextComponent target) {
            if (target != null) {
                try {
                    target.putClientProperty(JavaDeleteCharAction.class, this);
                    super.actionPerformed(evt, target);
                } finally {
                    target.putClientProperty(JavaDeleteCharAction.class, null);
                }
            }
        }

        @Override
        public boolean getNextChar() {
            return nextChar;
        }
    }

}

