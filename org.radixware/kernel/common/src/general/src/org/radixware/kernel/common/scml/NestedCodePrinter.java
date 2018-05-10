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

public class NestedCodePrinter extends CodePrinter {
    protected NestedCodePrinter(final CodePrinter container) {
        super(container);
    }

    @Override
    public char charAt(int index) {
        return getContainer().charAt(index);
    }

    @Override
    public CodePrinter print(Id id) {
        getContainer().print(id);
        return this;
    }

    @Override
    public CodePrinter print(char c) {
        getContainer().print(c);
        return this;
    }

    @Override
    public CodePrinter print(boolean b) {
        getContainer().print(b);
        return this;
    }

    @Override
    public CodePrinter print(CharSequence text) {
        getContainer().print(text);
        return this;
    }

    @Override
    public CodePrinter print(char[] text) {
        getContainer().print(text);
        return this;
    }

    @Override
    public CodePrinter println(char[] text) {
        getContainer().println(text);
        return this;
    }

    @Override
    public CodePrinter print(long l) {
        getContainer().print(l);
        return this;
    }

    @Override
    public CodePrinter print(int l) {
        getContainer().print(l);
        return this;
    }

    @Override
    public int length() {
        return getContainer().length();
    }

    @Override
    public CodePrinter printStringLiteral(String text) {
        getContainer().printStringLiteral(text);
        return this;
    }

    @Override
    public CodePrinter printCommandSeparator() {
        getContainer().printCommandSeparator();
        return this;
    }

    @Override
    public char[] getContents() {
        return getContainer().getContents();
    }

    @Override
    public int getLineNumber(int globalOffset) {
        return getContainer().getLineNumber(globalOffset);
    }
    
    @Override
    public String toString() {
        return getContainer().toString();
    }
}
