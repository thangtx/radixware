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
package org.radixware.kernel.server.reports.csv;

import java.io.Flushable;
import java.io.Closeable;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import org.radixware.kernel.server.types.Report;

public class CsvReportWriter {

    public static class Writer {

        private OutputStream outputStream;
        private char delimiter = ';';
        private boolean first = true;
        private String encoding;
        private final Report report;

        public Writer(Report report,OutputStream outputStream, String encoding) {
            this.outputStream = outputStream;
            this.encoding = encoding;
            this.report = report;
        }

        public Writer value(String value) {
            if (!first) {
                string(String.valueOf(delimiter));
            }            
            string(report.adjustCsvColumnValue(value, escape(value), delimiter));
            first = false;
            return this;
        }

        public Writer newLine() {
            first = true;
            return string("\n");
        }

        public Writer comment(String comment) {
            if (!first) {
                throw new FormatException("invalid csv: misplaced comment");
            }
            return string("#").string(comment).newLine();
        }

        public Writer flush() {
            try {
                if (outputStream instanceof Flushable) {
                    Flushable flushable = (Flushable) outputStream;
                    flushable.flush();
                }
            } catch (java.io.IOException e) {
                throw new IOException(e);
            }
            return this;
        }

        public void close() {

            //if (outputStream instanceof Closeable) {
            //}
            try {
                if (outputStream instanceof Closeable) {
                    Closeable closeable = (Closeable) outputStream;
                    closeable.close();
                }
            } catch (java.io.IOException e) {
                throw new IOException(e);
            }
        }

        private Writer string(String s) {

            try {
                //appendable.append(s);
                if (this.encoding != null) {
                    try {
                        outputStream.write(s.getBytes(encoding));
                    } catch (UnsupportedEncodingException ex) {
                        outputStream.write(s.getBytes());
                    }
                } else {
                    outputStream.write(s.getBytes());
                }
            } catch (java.io.IOException e) {
                throw new IOException(e);
            }
            return this;
        }

        private String escape(String value) {
            if (value == null) {
                return "";
            }
            if (value.length() == 0) {
                return "\"\"";
            }

            boolean needQuoting = value.charAt(0) == ' ' || value.endsWith(" ") || (value.charAt(0) == '#' && first);
            if (!needQuoting) {
                for (char ch : new char[]{'\"', '\\', '\r', '\n', '\t', delimiter}) {
                    if (value.indexOf(ch) != -1) {
                        needQuoting = true;
                        break;
                    }
                }
            }

            String result = value.replace("\"", "\"\"");
            if (needQuoting) {
                StringBuilder sb = new StringBuilder();
                sb.append("\"").append(result).append("\"");
                result = sb.toString();//"\"" + result + "\"";
            }
            return result;
        }

        public Writer delimiter(char delimiter) {
            this.delimiter = delimiter;
            return this;
        }

        public void setDelimiter(char delimiter) {
            this.delimiter = delimiter;
        }

        public String getEncoding() {
            return encoding;
        }

        public void setEncoding(String encoding) {
            this.encoding = encoding;
        }

        public char getDelimiter() {
            return delimiter;
        }
        
        
    }

    public static class Exception extends RuntimeException {

        public Exception() {
        }

        public Exception(String message) {
            super(message);
        }

        public Exception(String message, Throwable cause) {
            super(message, cause);
        }

        public Exception(Throwable cause) {
            super(cause);
        }
    }

    public static class IOException extends Exception {

        public IOException() {
        }

        public IOException(String message) {
            super(message);
        }

        public IOException(String message, Throwable cause) {
            super(message, cause);
        }

        public IOException(Throwable cause) {
            super(cause);
        }
    }

    public static class FormatException extends Exception {

        public FormatException() {
        }

        public FormatException(String message) {
            super(message);
        }

        public FormatException(String message, Throwable cause) {
            super(message, cause);
        }

        public FormatException(Throwable cause) {
            super(cause);
        }
    }
}
