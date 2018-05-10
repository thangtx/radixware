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
    private static final String FALSE = "false";
    private static final String TRUE = "true";
    
    protected StringBuilder buffer = new StringBuilder();
    private boolean lastCharWasEnter = false;
    
    protected BufferedCodePrinter() {
        super();
    }

    protected BufferedCodePrinter(final CodePrinter container) {
        super(container);
    }

    @Override
    public CodePrinter print(final char c) {
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
//        if (buffer.indexOf("char)")>0) {
//            throw new IllegalArgumentException("CATCH!!!");
//        }
        return this;
    }

    @Override
    public final CodePrinter print(final CharSequence text) {
        if (text == null) {
            return printError();
        }
        else {
            final int len = text.length();
            final char[] chars = new char[len];

            if (text instanceof String) {
                ((String) text).getChars(0, chars.length, chars, 0);
            } else if (text instanceof StringBuilder) {
                ((StringBuilder) text).getChars(0, chars.length, chars, 0);
            } else if (text instanceof StringBuffer) {
                ((StringBuffer) text).getChars(0, chars.length, chars, 0);
            } else {
                for (int i = 0; i < len; i++) {
                    chars[i] = text.charAt(i);
                }
            }
            return print(chars);
        }
    }

    @Override
    public final CodePrinter print(final char[] text) {
        if (text == null) {
            return printError();
        }
        else {
            for (char c : text) {
                print(c);
            }
            return this;
        }
    }

    @Override
    public char charAt(final int index) {
        return this.buffer.charAt(index);
    }

    @Override
    public CodePrinter println(final char[] text) {
        print(text);
        return println();
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
    public CodePrinter print(final long l) {
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
        return this;
    }

    @Override
    public CodePrinter print(final int l) {
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
        return this;
    }

    @Override
    public CodePrinter print(final Id id) {
        Monitor m = getActiveMonitor();
        if (m != null) {
            int len = buffer.length();
            if (id == null) {
                printError();
            } else {
                print(id.toCharArray());
            }
            m.offset(buffer.length() - len);
        } else {
            if (id == null) {
                printError();
            } else {
                print(id.toCharArray());
            }
        }
        return this;
    }

    @Override
    public CodePrinter print(final boolean b) {
        Monitor m = getActiveMonitor();
        if (m != null) {
            int len = buffer.length();
            print(b ? TRUE : FALSE);
            m.offset(buffer.length() - len);
        } else {
            print(b ? TRUE : FALSE);
        }
        return this;
    }

    @Override
    public String toString() {
        return buffer.toString();
    }

    @Override
    public char[] getContents() {
        final int len = length();   // Исключить дублирование массива
        final char[] result = new char[len];
        
        buffer.getChars(0,len,result,0);
        return result;
    }

    @Override
    public int length() {
        return buffer.length();
    }

    @Override
    public int getLineNumber(final int globalOffset) {
        final int last = Math.min(buffer.length(),globalOffset);
        int lineNumber = 0;

        for (int i = 0; i < last; i++) {
            if (buffer.charAt(i) == '\n') {
                lineNumber++;
            }
        }
        return lineNumber;
    }
}
