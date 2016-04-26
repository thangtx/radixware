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

package org.radixware.kernel.common.client.editors.xmleditor.model.validators;


final class Scaner {

    private final String input;
    final private int length;
    private int curPos;

    public Scaner(final String input) {
        this.input = input;
        length = input.length();
        curPos = 0;
    }

    public int skipSymbol(final char symbol) {
        if (length > curPos && input.charAt(curPos) == symbol) {
            return curPos++;
        } else {
            return curPos;
        }
    }

    public boolean isSymbol(final char symbol) {
        return input.length() > curPos && input.charAt(curPos) == symbol;
    }

    public String buildIntegerNumber() {
        final StringBuilder number = new StringBuilder();
        for (int i = curPos; i < input.length(); i++) {
            if (Character.isDigit(input.charAt(i))) {
                number.append(input.charAt(i));
            } else {
                break;
            }
        }
        moveCurrentPosition(number.length());
        return number.toString();
    }

    public String buildDecimalNumber() {
        final StringBuilder number = new StringBuilder();
        for (int i = curPos; i < input.length(); i++) {
            if (Character.isDigit(input.charAt(i)) || input.charAt(i) == '.') {
                number.append(input.charAt(i));
            } else {
                break;
            }
        }
        moveCurrentPosition(number.length());
        return number.toString();
    }

    public int getLength() {
        return length;
    }

    public int getCurPos() {
        return curPos;
    }

    public int moveCurrentPosition(final int move) {
        curPos += move;
        return curPos;
    }

    public boolean atEnd() {
        return curPos >= length;
    }
}