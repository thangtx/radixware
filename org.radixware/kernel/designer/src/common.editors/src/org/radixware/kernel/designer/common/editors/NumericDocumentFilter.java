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

package org.radixware.kernel.designer.common.editors;


import java.util.HashSet;
import java.util.Set;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;


public class NumericDocumentFilter extends DocumentFilter {

    private boolean isHexMode = false;
    private Set hexSet;

    public NumericDocumentFilter(boolean isHexMode){
        this.isHexMode = isHexMode;
        hexSet = new HashSet();
        for (char ch = 'A';ch<'G';ch++){
            hexSet.add(ch);
        }
    }

    @Override
    public void insertString(FilterBypass fb,
            int offset, String string, AttributeSet attr)
            throws BadLocationException {

        if (string == null) {
            return;
        }
        String docText = fb.getDocument().getText(0, fb.getDocument().getLength());
        if (isStringNumeric(offset, string, docText, isHexMode)) {
            super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset,
            int length, String text, AttributeSet attrs)
            throws BadLocationException {
        if (text == null) {
            return;
        }
        String docText = fb.getDocument().getText(0, fb.getDocument().getLength());
        if (isStringNumeric(offset, text, docText, isHexMode)) {
            super.replace(fb, offset, length, text, attrs);
        }
    }

    private boolean isStringNumeric(int offset, String string, String docText, final boolean isHexMode) {
        char[] characters = string.toCharArray();
        for (char c : characters) {
            if (!Character.isDigit(c)) {
                if (String.valueOf(c).equals("-") &&
                    !docText.contains("-") &&
                    offset == 0){
                    continue;
                }
                if (isHexMode){
                    if (hexSet.contains(c)){
                        continue;
                    }
                }
                return false;
            }
        }
        return true;
    }
}
