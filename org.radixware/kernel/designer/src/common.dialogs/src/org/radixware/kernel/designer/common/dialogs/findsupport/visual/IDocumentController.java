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

package org.radixware.kernel.designer.common.dialogs.findsupport.visual;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;


public interface IDocumentController {

    CharSequence getSequence();
    CharSequence getSelectedText();

    int getLength();

    void select(int first, int last);
    int getSelectionStart();
    int getSelectionEnd();
    int getCaretPosition();
    void setCaretPosition(int caretPosition);
    void unselect();

    void remove(int first, int last) throws BadLocationException;
    void insert(int pos, CharSequence val) throws BadLocationException;
    void runAtomic(Runnable runnable);

    Document getDocument();
    JTextComponent getComponent();

    boolean isEmpty();
}
