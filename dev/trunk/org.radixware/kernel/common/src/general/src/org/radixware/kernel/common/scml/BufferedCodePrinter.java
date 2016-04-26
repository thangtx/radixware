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


abstract class BufferedCodePrinter extends CodePrinter {

    private StringBuilder buffer = new StringBuilder();
    private boolean lastCharWasEnter = false;

    protected BufferedCodePrinter() {
        super();
    }

    @Override
    public final void print(char c) {
        if (c != '\n' && lastCharWasEnter) {
            lastCharWasEnter = false;
            applyIndentation();
        }

        Monitor m = getActiveMonitor();
        if (m != null) {
            int len = buffer.length();
            buffer.append(c);
            m.offset(buffer.length() - len);
        } else {
            buffer.append(c);
        }

        if (c == '\n') {
            lastCharWasEnter = true;
            getCurrentLineMatcher().newLine();
        }
    }

    @Override
    public final void print(final CharSequence text) {
        char[] chars = new char[text.length()];
        if (text instanceof String) {
            ((String) text).getChars(0, chars.length, chars, 0);
        } else if (text instanceof StringBuilder) {
            ((StringBuilder) text).getChars(0, chars.length, chars, 0);
        } else if (text instanceof StringBuffer) {
            ((StringBuffer) text).getChars(0, chars.length, chars, 0);
        } else {
            for (int i = 0; i < chars.length; i++) {
                chars[i] = text.charAt(i);
            }
        }
        print(chars);
    }

    @Override
    public final void print(char[] text) {
        for (int i = 0, length = text.length; i < length; i++) {
            print(text[i]);
        }
    }

    @Override
    public char charAt(int index) {
        return this.buffer.charAt(index);
    }

    @Override
    public void println(char[] text) {
        print(text);
        println();
    }

    @Override
    public void reset() {
        super.reset();
        buffer.setLength(0);
    }

    @Override
    public void clear() {
        super.clear();
        buffer = new StringBuilder();
    }

    @Override
    public final void print(long l) {
        if (lastCharWasEnter) {
            lastCharWasEnter = false;
            applyIndentation();
        }

        Monitor m = getActiveMonitor();
        if (m != null) {
            int len = buffer.length();
            buffer.append(l);
            m.offset(buffer.length() - len);
        } else {
            buffer.append(l);
        }
    }

    @Override
    public final void print(int l) {
        if (lastCharWasEnter) {
            lastCharWasEnter = false;
            applyIndentation();
        }

        Monitor m = getActiveMonitor();
        if (m != null) {
            int len = buffer.length();
            buffer.append(l);
            m.offset(buffer.length() - len);
        } else {
            buffer.append(l);
        }
    }

    @Override
    public void print(Id id) {
        Monitor m = getActiveMonitor();
        if (m != null) {
            int len = buffer.length();
            if (id == null) {
                print((String) null);
            } else {
                print(id.toCharArray());
            }
            m.offset(buffer.length() - len);
        } else {
            if (id == null) {
                print((String) null);
            } else {
                print(id.toCharArray());
            }
        }
    }

    @Override
    public void print(boolean b) {
        Monitor m = getActiveMonitor();
        if (m != null) {
            int len = buffer.length();
            print(b ? "true" : "false");
            m.offset(buffer.length() - len);
        } else {
            print(b ? "true" : "false");
        }
    }

    @Override
    public String toString() {
        return buffer.toString();
    }

    @Override
    public char[] getContents() {
        return buffer.toString().toCharArray();
    }

    @Override
    public int length() {
        return buffer.length();
    }

    @Override
    public int getLineNumber(int globalOffset) {
        int lineNumber = 0;
        for (int i = 0, len = buffer.length(); i < len; i++) {
            char c = buffer.charAt(i);
            if (c == '\n') {
                lineNumber++;
            }
            if (i >= globalOffset) {
                break;
            }
        }
        return lineNumber;
    }
}
