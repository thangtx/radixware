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
package org.radixware.kernel.designer.common.dialogs.jmlnb;

import javax.swing.Action;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

class JmlPreviousCCPosition extends JmlCCPosition {

    public JmlPreviousCCPosition(Action originalAction) {
        this(JmlKit.previousCamelCasePosition, originalAction);
    }

    protected JmlPreviousCCPosition(String name, Action originalAction) {
        super(name, originalAction);
    }

    protected int newOffset(JTextComponent textComponent) throws BadLocationException {
        return JmlCCOperations.previousCamelCasePosition(textComponent);
    }

    protected void moveToNewOffset(JTextComponent textComponent, int offset) throws BadLocationException {
        textComponent.setCaretPosition(offset);
    }
}
