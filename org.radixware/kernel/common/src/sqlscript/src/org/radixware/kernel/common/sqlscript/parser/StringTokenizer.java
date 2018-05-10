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

public class StringTokenizer extends Tokenizer {
    public StringTokenizer(final String str) {
        super(new StringReader(str));
    }

    public StringTokenizer(final String str, final String delim) {
        super(new StringReader(str), delim);
    }

    public StringTokenizer(final String str, final String delim, final String quotes) {
        super(new StringReader(str), delim, quotes);
    }

    public boolean hasMoreTokens() {
        return !((StringReader)getReader()).isEof();
    }

    public int getPosistion() {
        return ((StringReader)getReader()).getIndex();
    }
}
