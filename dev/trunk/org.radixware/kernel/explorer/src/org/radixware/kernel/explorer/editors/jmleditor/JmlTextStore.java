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
package org.radixware.kernel.explorer.editors.jmleditor;

import com.trolltech.qt.gui.QTextCharFormat;
import com.trolltech.qt.gui.QTextCursor;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.text.ITextStore;


class JmlTextStore implements ITextStore {

    private final StringBuilder fakeText;
    private final QTextCursor tc;
    private final QTextCharFormat charFormat;
    private final String initialFakeText;
    private final List<ReplaceOper> operations = new ArrayList<>();

    public JmlTextStore(StringBuilder fakeText, QTextCursor tc, QTextCharFormat charFormat) {
        this.fakeText = fakeText;
        this.initialFakeText = fakeText.toString();
        this.tc = tc;
        this.charFormat = charFormat;
    }
    
    @Override
    public char get(int pos) {
        return fakeText.charAt(pos);
    }

    @Override
    public int getLength() {
        return fakeText.length();
    }

    @Override
    public String get(int pos, int length) {
        return fakeText.substring(pos, pos + length);
    }

    @Override
    public void replace(int offset, int length, String text) {
        fakeText.replace(offset, offset + length, text);
        
        operations.add(new ReplaceOper(offset, offset + length, text));
    }

    @Override
    public void set(String text) {
        replace(0, getLength(), text);
    }
    
    public boolean needFormat() {
        return !initialFakeText.equals(fakeText.toString().replace(System.lineSeparator(), "\n"));
    }
    
    
    public void applyFormat() {
        for (ReplaceOper operation : operations) {
            tc.setPosition(operation.startPos);
            tc.setPosition(operation.endPos, QTextCursor.MoveMode.KeepAnchor);
            tc.insertText(operation.text, charFormat);
        }
    }
    
    private static final class ReplaceOper {
        
        private final int startPos;
        private final int endPos;
        private final String text;

        public ReplaceOper(int startPos, int endPos, String text) {
            this.startPos = startPos;
            this.endPos = endPos;
            this.text = text;
        }
    }

}
