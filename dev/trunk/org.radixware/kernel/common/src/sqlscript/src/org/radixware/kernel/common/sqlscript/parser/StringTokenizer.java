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

    public StringTokenizer(String str) {
        super(new StringReader(str));
    }

    public StringTokenizer(String str, String delim) {
        super(new StringReader(str), delim);
    }

    public StringTokenizer(String str, String delim, String quotes) {
        super(new StringReader(str), delim, quotes);
    }

    public boolean hasMoreTokens() {
        return !((StringReader)getReader()).isEof();
    }

    public int getPosistion() {
        return ((StringReader)getReader()).getIndex();
    }
}
