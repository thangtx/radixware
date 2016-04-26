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

package org.radixware.kernel.common.scml;

import org.radixware.kernel.common.types.Id;


class NullCodePrinter extends CodePrinter {

    protected NullCodePrinter() {
        super();
    }

    @Override
    public void print(char c) {
    }

    @Override
    public void print(CharSequence text) {
    }

    @Override
    public void print(char[] text) {
    }

    @Override
    public void print(long l) {
    }

    @Override
    public void print(int l) {
    }

    @Override
    public void printCommandSeparator() {
    }

    @Override
    public void printStringLiteral(String text) {
    }

    @Override
    public void println(char[] text) {
    }

    @Override
    public void print(Id id) {
    }

    @Override
    public void print(boolean b) {
    }

    @Override
    public int length() {
        return 0;
    }

    @Override
    public char[] getContents() {
        return new char[0];
    }

    @Override
    public int getLineNumber(int globalOffset) {
        return 0;
    }

    @Override
    public char charAt(int index) {
        return (char) 0;
    }
}
