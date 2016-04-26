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
package org.radixware.kernel.common.defs.ads.xml;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.Filer;
import org.radixware.kernel.common.utils.FileUtils;

public class AdsXmlFilerImpl implements Filer {

    private class WriterImpl extends Writer {

        private StringBuilder content = new StringBuilder();
        private String fileName;
        private OutputStream out;

        public WriterImpl(String fileName) {
            this.fileName = fileName;
            if (baseDir != null) {
                try {
                    out = new FileOutputStream(new File(baseDir, fileName));
                } catch (IOException e) {
                    Logger.getLogger(AdsXmlFilerImpl.class.getName()).log(Level.SEVERE, e.getMessage(), e);
                    out = null;
                }
            }
        }

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            if (out != null) {
                String stringToWrite = String.valueOf(cbuf, off, len);
                try {
                    out.write(stringToWrite.getBytes(FileUtils.XML_ENCODING));
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(AdsXmlFilerImpl.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                }
            } else {
                content.append(cbuf, off, len);
            }
        }

        @Override
        public void flush() throws IOException {
        }

        @Override
        public void close() throws IOException {
            if (baseDir != null) {
                File fileToWrite = new File(baseDir, fileName);
                writtenFiles.add(fileToWrite);
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException ex) {
                        Logger.getLogger(AdsXmlFilerImpl.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                    }
                } else {
                    FileUtils.writeString(fileToWrite, content.toString(), FileUtils.XML_ENCODING);
                    content = null;
                }
            } else {
                char[] data = new char[content.length()];
                content.getChars(0, data.length, data, 0);
                content = null;
                sources.put(fileName, data);
            }
        }
    }

    private class OutputStreamImpl extends OutputStream {

        private OutputStream content = new ByteArrayOutputStream();
        private final String fileName;

        public OutputStreamImpl(String fileName) {
            if (baseDir != null) {
                File fileToWrite = new File(baseDir, fileName);
                if (!fileToWrite.getParentFile().exists()) {
                    fileToWrite.getParentFile().mkdirs();
                }
                try {
                    content = new FileOutputStream(fileToWrite);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(AdsXmlFilerImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            this.fileName = fileName;
        }

        @Override
        public void write(int b) throws IOException {
            content.write(b);
        }

        @Override
        public void write(byte[] b) throws IOException {
            content.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            content.write(b, off, len); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void flush() throws IOException {
        }

        @Override
        public void close() throws IOException {
            if (baseDir != null) {
                File fileToWrite = new File(baseDir, fileName);
                writtenFiles.add(fileToWrite);
                content.close();
            } else {
                binaries.put(fileName, ((ByteArrayOutputStream) content).toByteArray());
            }
            content = null;
        }
    }
    private final Map<String, char[]> sources = new HashMap<>();
    private final Map<String, byte[]> binaries = new HashMap<>();
    private final List<File> writtenFiles = new LinkedList<>();
    private final File baseDir;

    public AdsXmlFilerImpl(File baseDir) {
        this.baseDir = baseDir;
    }

    @Override
    public OutputStream createBinaryFile(String fileName) throws IOException {
        return new OutputStreamImpl(fileName);
    }

    @Override
    public Writer createSourceFile(String fileName) throws IOException {
        return new WriterImpl(fileName);
    }

    public Map<String, char[]> getSources() {
        return sources;
    }

    public Map<String, byte[]> getBinaries() {
        return binaries;
    }

    public File[] getFiles() {
        return writtenFiles.toArray(new File[writtenFiles.size()]);
    }
}
