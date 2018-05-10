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
package org.radixware.kernel.common.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

/**
 * Allows to export XML in formatted style - add line separators and tabulators
 * before new elements that not contains text or CDATA. Instead in XmlBeans
 * formatter, it safes any text, even that not in CDATA. Must be used only for
 * newly created and filled XML documents, not loaded.
 */
public class XmlFormatter extends OutputStreamWriter {

    private boolean inText = false;
    private int level = 0;
    private boolean opened = false;
    private boolean specData = false;
    private boolean wasSpecData = false;
    private int cc = -1; // prev char (buffer)
    private int ccc = -1; // prev prev char (buffer)
    private boolean first = true;
    private boolean inElement = false;
    private boolean escapeSpaces;
    private boolean startNewLine = true;

    public XmlFormatter(OutputStream out, boolean escapeSpaces) {
        super(new BufferedOutputStream(out), Charset.forName(FileUtils.XML_ENCODING));
        this.escapeSpaces = escapeSpaces;
    }

    public XmlFormatter(OutputStream out) {
        this(out, true);
    }

    @Override
    public void write(int c) throws IOException {
        if (c == '<') {
            if (cc == '<') {
                super.write(cc);
            }
            ccc = cc;
            cc = c;
            return; // hold, do not print '<' at once (only for '<');
        }

        if (specData) {
            if ((ccc == '-' && cc == '-' && c == '>')
                    || (ccc == ']' && cc == ']' && c == '>')) {
                specData = false;
                wasSpecData = true;
            }
        }

        if (cc == '<' && c == '!' && !specData && !inText) {
            specData = true;
        }

        if (cc == '<' && !specData && !inText) {
            if (c == '/') {
                level--;
            }
            boolean opening = (c != '!' && c != '?' && c != '/');
            if (!wasSpecData && (!opened || opening) && !first) {
                if (startNewLine) {
                    //if (cc != '\n') { //commented, because (cc != '\n') any way at this place.
                    super.write('\n');
                    //}
                    for (int j = 0; j < level; j++) {
                        super.write(' '); // do not print "  " - infinite recursion
                        super.write(' ');
                    }
                }
            }
            startNewLine = true;
            opened = opening;
            if (opened) {
                level++;
            }
            wasSpecData = false;
            first = false;
            inElement = true;
        }

        if (cc == '/' && c == '>' && !specData && !inText && inElement) {
            level--;
            opened = false;
            inElement = false;
        }

        if (c == '>' && !specData && !inText && inElement) {
            inElement = false;
        }

        if (c == '"' && inElement && !specData && cc != '\\') {
            inText = !inText;
        }

        // === output ===
        if (cc == '<') {
            super.write(cc);
        }
        if (inElement && inText && escapeSpaces) {
            if (c == '\t') {
                super.write('&');
                super.write('#');
                super.write('x');
                super.write('9');
                super.write(';');
            } else if (c == '\n') {
                super.write('&');
                super.write('#');
                super.write('x');
                super.write('A');
                super.write(';');
            } else if (c == '\r') {
                super.write('&');
                super.write('#');
                super.write('x');
                super.write('D');
                super.write(';');

            } else {
                super.write(c);
            }
        } else {
            if (c == '\n' && !inElement && !inText && !specData) {
                startNewLine = false;
            }
            super.write(c);
        }
        ccc = cc;
        cc = c;
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        for (int i = 0; i < len; i++) {
            write(cbuf[i + off]);
        }
    }

    @Override
    public void write(String str, int off, int len) throws IOException {
        for (int i = 0; i < len; i++) {
            write(str.charAt(i + off));
        }
    }

    @Override
    public void flush() throws IOException {
        if (cc == '<') {
            super.write(cc);
            cc = -1;
            ccc = -1;
        }
        super.flush();
    }

    @Override
    public void close() throws IOException {
        inText = false;
        level = 0;
        opened = false;
        specData = false;
        wasSpecData = false;
        cc = -1;
        ccc = -1;
        first = true;

        super.close();
    }

    @Deprecated
    public static boolean formatXmlFile(File file) throws IOException {
        boolean changed = false;
        boolean inText = false;
        int level = 0;

        final char[] s = FileUtils.readTextFile(file, FileUtils.XML_ENCODING).toCharArray();
        final int len = s.length;
        final StringBuilder sb = new StringBuilder(len);
        boolean elementOpened = false;
        boolean cdata = false;

        for (int i = 0; i < len; i++) {
            char c = s[i];
            sb.append(c);

            switch (c) {
                case '<':
                    if (!inText) {
                        elementOpened = (i < len - 1 && s[i + 1] != '!' && s[i + 1] != '?' && s[i + 1] != '/');
                        if (elementOpened) {
                            level++;
                        }
                        if (i < len - 1 && s[i + 1] == '/') {
                            level--;
                        }
                        cdata = (i < len - 2 && s[i + 1] == '!' && s[i + 2] == '[');
                    }
                    break;
                case '>':
                    if (!inText && !cdata) {
                        if (i > 0 && s[i - 1] == '/') {
                            level--;
                            elementOpened = false;
                        }
                        for (int j = i + 1; j < len; j++) {
                            char cc = s[j];
                            if (cc == ' ' || cc == '\t' || cc == '\n' || cc == '\r') {
                                continue;
                            }
                            if (cc == '<') {
                                final boolean opening = (j < len - 1 && s[j + 1] != '/');
                                final boolean openingCData = (j < len - 2 && s[j + 1] == '!' && s[j + 2] == '[');  // <![CDATA[
                                if (!openingCData && (!elementOpened || opening)) {
                                    sb.append('\n');
                                    changed = changed || (++i < len - 1 && s[i] != '\n');
                                    for (int k = (opening ? 0 : 1); k < level; k++) {
                                        sb.append('\t');
                                        changed = changed || (++i < len - 1 && s[i] != '\t');
                                    }
                                    i = j - 1;
                                }
                            }
                            break;//NOPMD
                        }
                    }
                    break;
                case '\"':
                    inText = !inText;
                    break;
            }
        }

        if (changed) {
            final String result = sb.toString();
            FileUtils.writeString(file, result, FileUtils.XML_ENCODING);
        }
        return changed;
    }

    public static void save(XmlObject xmlObject, OutputStream outputStream) throws IOException {
        save(xmlObject, outputStream, true);
    }

    public static void save(XmlObject xmlObject, OutputStream outputStream, boolean escapeSpaces) throws IOException {
        final XmlFormatter formatter = new XmlFormatter(outputStream, escapeSpaces);
        try (BufferedWriter bufferedWriter = new BufferedWriter(formatter)) {
            XmlOptions options = new XmlOptions();
            options.setSaveNamespacesFirst();
            options.setSaveAggressiveNamespaces();
            options.setUseDefaultNamespace();
            formatter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"); // XmlObject.save(Writer) not printed XML Header, RADIX-1923
            xmlObject.save(bufferedWriter, options);
        }
    }

    public static void save(XmlObject xmlObject, File file) throws IOException {
        save(xmlObject, file, true);
    }

    public static void save(XmlObject xmlObject, File file, boolean escapeSpaces) throws IOException {
        final OutputStream outputStream = FileOperations.getDefault().getOutputStream(file);
        try {
            save(xmlObject, outputStream, escapeSpaces);
        } finally {
            outputStream.close();
        }
    }

    public static String format(XmlObject xmlObject) throws IOException {
        return format(xmlObject, true);
    }

    public static String format(XmlObject xmlObject, boolean escapeSpaces) throws IOException {
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        save(xmlObject, bytes, escapeSpaces);
        return bytes.toString(FileUtils.XML_ENCODING);
    }
}
