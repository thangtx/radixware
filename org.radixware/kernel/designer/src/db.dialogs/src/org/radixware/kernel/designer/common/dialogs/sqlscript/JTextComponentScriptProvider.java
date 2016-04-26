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
package org.radixware.kernel.designer.common.dialogs.sqlscript;

import javax.swing.text.JTextComponent;
import org.netbeans.editor.BaseDocument;
import org.netbeans.editor.Utilities;
import org.openide.windows.TopComponent;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;

public class JTextComponentScriptProvider implements ISQLScriptProvider {

    private final JTextComponent textComponent;

    public JTextComponentScriptProvider(JTextComponent textComponent) {
        this.textComponent = textComponent;
    }

    @Override
    public String getScript() {
        return textComponent.getText();
    }

    @Override
    public Object getObjectAtLine(int line) {
        return textComponent;
    }

    @Override
    public void activateObject(Object component) {
        final TopComponent tc = DialogUtils.findTopComponent(textComponent);
        if (tc != null && !tc.isVisible()) {
            tc.requestVisible();
        }
        textComponent.requestFocusInWindow();
    }

    @Override
    public int convertScriptLineToObjectLine(Object object, int line) {
        return line;
    }

    @Override
    public JTextComponent getObjectTextComponent(Object object) {
        return textComponent;
    }

    @Override
    public void setPosition(Object object, int absLine, int absColumn) {
        if (textComponent.getDocument() instanceof BaseDocument) {
            final BaseDocument baseDoc = (BaseDocument) textComponent.getDocument();
            final int rowOffset = Utilities.getRowStartFromLineOffset(baseDoc, absLine);
            if (rowOffset >= 0) {
                final int offset = rowOffset + absColumn;
                if (offset >= 0 && offset <= textComponent.getDocument().getLength()) {
                    textComponent.setCaretPosition(offset);
                }
            };
        }
    }

}
