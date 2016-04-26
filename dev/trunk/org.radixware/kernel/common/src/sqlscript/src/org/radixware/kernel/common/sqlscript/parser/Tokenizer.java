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


public class Tokenizer {

    private final Reader in;
    private final String delim;
    private final String quotes;
    private boolean spaceDelim=true;

    public Tokenizer(Reader in) {
        this(in, " ", "\"");
    }

    public Tokenizer(Reader in, String delim) {
        this(in, delim, "\"");
    }

    public Tokenizer(Reader in, String delim, String quotes) {
        this.in = in;
        this.delim = delim;
        this.quotes = quotes;
    }

    public Reader getReader() {
        return in;
    }

    public String makeToken(String inStr, String delim, boolean spaceDelim) {
        if (inStr.length() == 0) {
            StringBuffer ret = new StringBuffer();
            if (spaceDelim && quotes != null) {
                ret.append(quotes.charAt(0));
                ret.append(quotes.charAt(0));
            } else {
                ret.append(delim);
            }
            return ret.toString();
        }
        StringBuffer str = new StringBuffer(inStr);
        for (int i = 0; i < str.length(); i++) {
            if (spaceDelim) {
                if (quotes.indexOf(str.charAt(i)) != -1 || str.charAt(i) == '\\') {
                    str.insert(i++, '\\');
                }
            } else {
                if (delim.indexOf(str.charAt(i)) != -1 || str.charAt(i) == '\\') {
                    str.insert(i++, '\\');
                }
            }
        }
        if (spaceDelim && containsDelim(str.toString())) {
            str.insert(0, quotes.charAt(0));
            str.append(quotes.charAt(0));
        }
        return str.toString();
    }

    private boolean containsDelim(String str) {
        for (char c : delim.toCharArray()) {
            if (str.indexOf(c) != -1)
                return true;
        }
        return false;
    }

    public String nextToken() throws IOException {
        return nextToken(true);
    }

    public String nextToken(boolean handleSpec) throws IOException {
        boolean escaped = false;
        boolean escape_used = false;
        boolean complex = false;
        int ch;

        StringBuffer token = new StringBuffer();
        while ((ch = in.read()) != -1) {
            if (spaceDelim && quotes.indexOf(ch) != -1) {
                if (complex) {
                    if (!escaped) {
                        ch = in.read();
                        if (ch == -1 || delim.indexOf(ch) != -1) {
                            return token.toString();
                        }
                        complex = false;
                    } else {
                        escape_used = true;
                    }
                } else if (token.length() == 0 && !escaped) {
                    complex = true;
                    continue;
                }
            } else if (delim.indexOf(ch) != -1) {
                if (!complex && !escaped) {
                    if (!spaceDelim || token.length() > 0) {
                        return token.toString();
                    } else {
                        continue;
                    }
                }
                if (escaped) {
                    escape_used = true;
                }
            } else if (handleSpec && ch == '\\') {
                if (!escaped) {
                    escaped = true;
                    continue;
                } else {
                    escape_used = true;
                }
            }
            if (escaped) {
                if (!escape_used) {
                    token.append('\\');
                } else {
                    escape_used = false;
                }
                escaped = false;
            }
            token.append((char)ch);
        }
        if (token.length() != 0) {
            return token.toString();
        }
        return null;
    }

    public String getToken(boolean handleSpec) throws IOException {
        String token = nextToken(handleSpec);
        if (token == null) {
            throw new IOException("Tokenizer : no more tokens");
        }
        return token;
    }

    public String getRest() throws IOException {
        StringBuffer token = new StringBuffer();
        int ch;
        while ((ch = in.read()) != -1) {
            token.append((char)ch);
        }
        return token.toString();
    }
}
