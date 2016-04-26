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

package org.radixware.kernel.designer.common.dialogs.sqmlnb;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.netbeans.editor.BaseDocument;
import org.netbeans.editor.GuardedException;
import org.netbeans.modules.editor.indent.spi.Context;
import org.netbeans.modules.editor.indent.spi.ExtraLock;
import org.netbeans.modules.editor.indent.spi.IndentTask;
import org.openide.util.Exceptions;


public class SqmlIndentTask implements IndentTask {

    private final Context context;

    public SqmlIndentTask(Context context) {
        this.context = context;
    }

    @Override
    public void reindent() throws BadLocationException {
        Document doc = context.document();
        int offset = context.startOffset();
        try {
            if (doc instanceof BaseDocument) {
                BaseDocument baseDoc = (BaseDocument) doc;
                char[] ch = baseDoc.getChars(0, offset);
                if (offset != context.lineStartOffset(offset)) {
                    return;
                }
                int offs = offset - 2;
                char c = 0;
                while (offs >= 0 && c != '\n') {
                    c = ch[offs--];
                }
                offs += 2;
                int spaceCount = 0;
                int ts = baseDoc.getTabSize();
                while (offs < offset && ((c = ch[offs++]) == ' ' || c == '\t')) {
                    if (c == '\t') {
                        spaceCount += ts;
                    } else {
                        spaceCount++;
                    }
                }

                StringBuilder sb = new StringBuilder();
                final int lineStart = context.lineStartOffset(offset);
                while (spaceCount > 0) {
                    if (!(offset-spaceCount >= lineStart && offset - spaceCount < ch.length && ch[offset - spaceCount] == ' ')) {
                        sb.append(' ');
                    }
                    spaceCount--;
                }

                String tab = sb.toString();
                doc.insertString(offset, tab, null);
            }

        } catch (GuardedException e) {
            java.awt.Toolkit.getDefaultToolkit().beep();

        } catch (BadLocationException e) {
            Exceptions.printStackTrace(e);
        }
    }

    @Override
    public ExtraLock indentLock() {
        return null;
    }
}
