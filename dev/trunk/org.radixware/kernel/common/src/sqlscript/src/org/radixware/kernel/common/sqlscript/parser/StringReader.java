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

    public StringReader(String str) {
        this.str = str;
        length = str.length();
        index = 0;
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        if (index >= length)
            return -1;
        int n = Math.min(length - index, len);
        str.getChars(index, index + n, cbuf, off);
        index += n;
        return n;
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
}
