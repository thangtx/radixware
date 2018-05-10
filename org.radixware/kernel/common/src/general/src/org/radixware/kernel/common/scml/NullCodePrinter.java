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
    public CodePrinter print(char c) {
        return this;
    }

    @Override
    public CodePrinter print(CharSequence text) {
        return this;
    }

    @Override
    public CodePrinter print(char[] text) {
        return this;
    }

    @Override
    public CodePrinter print(long l) {
        return this;
    }

    @Override
    public CodePrinter print(int l) {
        return this;
    }

    @Override
    public CodePrinter printCommandSeparator() {
        return this;
    }

    @Override
    public CodePrinter printStringLiteral(String text) {
        return this;
    }

    @Override
    public CodePrinter println(char[] text) {
        return this;
    }

    @Override
    public CodePrinter print(Id id) {
        return this;
    }

    @Override
    public CodePrinter print(boolean b) {
        return this;
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
