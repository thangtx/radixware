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

package org.radixware.kernel.designer.debugger.impl.ui;

import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.api.editor.EditorRegistry;
import org.openide.text.Line;
import org.openide.text.NbDocument;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlDocument;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditorPane;


public class EditorManagerProxy {

    public static Jml getCurrentScml() {
        JTextComponent c = EditorRegistry.lastFocusedComponent();
        if (c instanceof ScmlEditorPane) {
            ScmlEditorPane editor = (ScmlEditorPane) c;
            Scml scml = editor.getScml();
            if (scml instanceof Jml) {
                return (Jml) scml;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static Line getLine(Jml jml, int lineNumber) {
        for (JTextComponent c : EditorRegistry.componentList()) {
            if (c instanceof ScmlEditorPane) {
                ScmlEditorPane editor = (ScmlEditorPane) c;
                if (editor.getScml() == jml) {
                    int lineOffset = NbDocument.findLineOffset(editor.getScmlDocument(), lineNumber);
                    try {
                        Line line = editor.getScmlDocument().getLine(lineOffset);
                        c.setCaretPosition(lineOffset);
                        return line;
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            }
        }
        return null;
    }

    public static ScmlDocument findDocument(Scml scml) {
        for (JTextComponent c : EditorRegistry.componentList()) {
            if (c instanceof ScmlEditorPane) {
                Document doc = ((ScmlEditorPane) c).getDocument();
                if (doc instanceof ScmlDocument && ((ScmlDocument) doc).getScml() == scml) {
                    return (ScmlDocument) doc;
                }
            }
        }
        return null;
    }

    public static JTextComponent findComponent(Scml scml) {
        for (JTextComponent c : EditorRegistry.componentList()) {
            if (c instanceof ScmlEditorPane) {
                Document doc = ((ScmlEditorPane) c).getDocument();
                if (doc instanceof ScmlDocument && ((ScmlDocument) doc).getScml() == scml) {
                    return c;
                }
            }
        }
        return null;
    }
}
