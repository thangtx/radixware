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

package org.radixware.kernel.common.sqlscript.parser;

import java.io.IOException;
import java.io.Reader;


public class StringReader extends Reader {
    private final String str;
    private final int length;
    private int index;

    public StringReader(final String str) {
        if (str == null) {
            throw new IllegalArgumentException("String to read can't be null");
        }
        else {
            this.str = str;
            length = str.length();
            index = 0;
        }
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        if (cbuf == null) {
            throw new IllegalArgumentException("Target char array can't be null");
        }
        else if (index >= length) {
            return -1;
        }
        else {
            final int n = Math.min(length - index, len);

            str.getChars(index, index + n, cbuf, off);
            return index += n;
        }
    }

    @Override
    public void close() throws IOException {
    }

    public int getIndex() {
        return index;
    }

    public String getRest() {
        return str.substring(index);
    }

    public boolean isEof() {
        return index >= length;
    }

    @Override
    public String toString() {
        return "StringReader{" + "str=" + str + ", length=" + length + ", index=" + index + '}';
    }
}
