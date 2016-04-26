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
import org.netbeans.editor.BaseDocument;
import org.netbeans.editor.Utilities;


class DocumentController implements IDocumentController {

    public static final IDocumentController EMPTY_DOCUMENT_CONTROLLER = new EmptyDocumentController();

    private JTextComponent component;

    public DocumentController() {
    }

    public DocumentController(JTextComponent component) {
        this.component = component;
    }

    @Override
    public CharSequence getSequence() {
        return (CharSequence) getComponent().getDocument().getProperty(ReplaceAction.SEQUENCE_KEY);
    }

    @Override
    public void select(int first, int last) {
        getComponent().select(first, last + 1);
    }

    @Override
    public int getSelectionStart() {
        return getComponent().getSelectionStart();
    }

    @Override
    public int getSelectionEnd() {
        return getComponent().getSelectionEnd();
    }

    @Override
    public CharSequence getSelectedText() {
        return getComponent().getSelectedText();
    }

    @Override
    public int getCaretPosition() {
        return getComponent().getCaretPosition();
    }

    @Override
    public int getLength() {
        return getComponent().getDocument().getLength();
    }

    @Override
    public void setCaretPosition(int caretPosition) {
        getComponent().setCaretPosition(caretPosition);
    }

    @Override
    public JTextComponent getComponent() {
        return component != null ? component : Utilities.getLastActiveComponent();
    }

    @Override
    public void runAtomic(Runnable runnable) {
        Document document = getDocument();
        if (document instanceof BaseDocument) {
            ((BaseDocument) document).runAtomic(runnable);
        } else {
            runnable.run();
        }
    }

    @Override
    public void remove(int first, int last) throws BadLocationException {
        getDocument().remove(first, last - first + 1);
    }

    @Override
    public void insert(int pos, CharSequence val) throws BadLocationException {
        getDocument().insertString(pos, val.toString(), null);
    }

    @Override
    public Document getDocument() {
        return getComponent().getDocument();
    }

    @Override
    public void unselect() {
        // unselect text
        setCaretPosition(getCaretPosition());
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    private static final class EmptyDocumentController implements IDocumentController {

        private static final IDocumentController INSTANCE = new EmptyDocumentController();

        public static IDocumentController getInstance() {
            return INSTANCE;
        }

        private EmptyDocumentController() {
        }

        @Override
        public CharSequence getSequence() {
            return "";
        }

        @Override
        public CharSequence getSelectedText() {
            return "";
        }

        @Override
        public int getLength() {
            return 0;
        }

        @Override
        public void select(int first, int last) {
        }

        @Override
        public int getSelectionStart() {
            return -1;
        }

        @Override
        public int getSelectionEnd() {
            return -1;
        }

        @Override
        public int getCaretPosition() {
            return -1;
        }

        @Override
        public void setCaretPosition(int caretPosition) {
        }

        @Override
        public void unselect() {
        }

        @Override
        public void remove(int first, int last) {
        }

        @Override
        public void insert(int pos, CharSequence val) {
        }

        @Override
        public void runAtomic(Runnable runnable) {
        }

        @Override
        public Document getDocument() {
            return null;
        }

        @Override
        public JTextComponent getComponent() {
            return null;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }
    }
}