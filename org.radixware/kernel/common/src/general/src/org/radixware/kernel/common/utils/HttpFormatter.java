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

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.enums.EHttpParameter;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.trace.LocalTracer;

public class HttpFormatter {

    public static final String REQUEST_LINE_HEADER_KEY = "";
    public static final String TRANSFER_ENCODING_HEADER_ATTR = "transfer-encoding";
    public static final String KEEP_ALIVE = "keep-alive";
    public static final String CLOSE = "close";
    public static final String CHUNKED = "chunked";
    public static final String DEFAULT_HTTP_CONTENT_CHAR_SET = "UTF-8";
    public static final String SOAP_CONTENT_TYPE = "text/xml; charset=\"UTF-8\"";
    public static final String PRAGMA_RADIX_PRIORITY = "org-radixware-priority";
    public static final String PRAGMA_RADIX_VERSION = "org-radixware-version";
    private final static String rqHeaderMask
            = "{0} {1} HTTP/1.1\r\n"
            + "cache-control: no-cache\r\n"
            + "host: {2}\r\n"
            + "connection: {3}\r\n"
            + "content-length: {4}\r\n"
            + "content-type: {5}\r\n"
            + "user-agent: {6}\r\n"
            + "\r\n";
    private final static String rsHeaderMask
            = "HTTP/1.1 {0}\r\n"
            + "connection: {1}\r\n"
            + "content-length: {2}\r\n"
            + "content-type: {3}\r\n"
            + "\r\n";
    private final static String soapRqHeaderMask
            = "POST {0} HTTP/1.1\r\n"
            + "cache-control: no-cache\r\n"
            + "host: {1}\r\n"
            + "connection: {2}\r\n"
            + "content-length: {3}\r\n"
            + "content-type: " + SOAP_CONTENT_TYPE + "\r\n"
            + "soapaction: \"{4}\"\r\n"
            + "user-agent: {5}\r\n"
            + "\r\n";
    private final static String soapRsHeaderMask
            = "HTTP/1.1 200 OK\r\n"
            + "connection: {0}\r\n"
            + "content-length: {1}\r\n"
            + "content-type: " + SOAP_CONTENT_TYPE + "\r\n"
            + "soapaction: \"{2}\"\r\n"
            + "\r\n";
    private final static String soapFaultHeaderMask
            = "HTTP/1.1 500 Internal Server Error\r\n"
            + "connection: {0}\r\n"
            + "content-length: {1}\r\n"
            + "content-type: " + SOAP_CONTENT_TYPE + "\r\n"
            + "\r\n";

    static private String addHeaderAttrs(String header, Map<String, String> headerAttrs) {
        if (headerAttrs == null || headerAttrs.isEmpty()) {
            return header;
        }
        StringBuilder attrs = new StringBuilder();

        for (String key : headerAttrs.keySet()) {
            String attr = headerAttrs.get(key);
            if (key != null && attr != null && header.indexOf("\r\n" + key + ": ") < 0) {
                attrs.append(key).append(": ").append(attr).append("\r\n");
            }
        }
        attrs.append("\r\n");
        return header.substring(0, header.length() - 2) + attrs.toString();
    }

    public static Map<String, String> appendPragma(final Map<String, String> headerAttrs, final String pragmaEntry) {
        final Map<String, String> result = headerAttrs == null ? new HashMap<String, String>() : headerAttrs;
        final String curPragma = result.get(EHttpParameter.HTTP_PRAGMA.getValue());
        result.put(EHttpParameter.HTTP_PRAGMA.getValue(), curPragma == null || curPragma.isEmpty() ? pragmaEntry : curPragma + "," + pragmaEntry);
        return result;
    }

    /**
     * @return null if there is no such pragma, empty string if pragma has no
     * value, value of the pragma otherwise.
     */
    public static String getPragma(final Map<String, String> httpHeaders, final String pragmaName) {
        if (httpHeaders == null) {
            return null;
        }
        final String pragmaStr = httpHeaders.get(EHttpParameter.HTTP_PRAGMA.getValue());
        if (pragmaStr == null || pragmaStr.isEmpty()) {
            return null;
        }
        final String[] pragmas = pragmaStr.split(",");
        for (String pragma : pragmas) {
            final String[] nameAndVal = pragma.toLowerCase().split("=", 2);
            if (nameAndVal[0].trim().equals(pragmaName)) {
                if (nameAndVal.length == 1) {
                    return "";
                } else {
                    String value = nameAndVal[1].trim();
                    if (value.startsWith("\"") && value.endsWith("\"")) {
                        return value.substring(1, value.length() - 1);
                    } else {
                        return value;
                    }
                }

            }
        }
        return null;
    }

    public static Map<String, String> appendRadixVersionHeaderAttr(final Map<String, String> headerAttrs, final long version) {
        return appendPragma(headerAttrs, PRAGMA_RADIX_VERSION + "=" + version);
    }

    /**
     * @return value of {@code org-radixware-priority} pragma or -1
     */
    public static long getRadixVersionFromHeaders(final Map<String, String> httpHeaders) {
        final String priorityStr = getPragma(httpHeaders, PRAGMA_RADIX_VERSION);
        if (priorityStr != null && !priorityStr.isEmpty()) {
            try {
                return Long.parseLong(priorityStr);
            } catch (RuntimeException ex) {
                return -1;
            }
        }
        return -1;
    }

    public static Map<String, String> appendPriorityHeaderAttr(final Map<String, String> headerAttrs, int radixPriority) {
        return appendPragma(headerAttrs, PRAGMA_RADIX_PRIORITY + "=" + radixPriority);
    }

    /**
     * @return value of {@code org-radixware-priority} pragma or -1
     */
    public static int getRadixPriorityFromHeaders(final Map<String, String> httpHeaders) {
        final String priorityStr = getPragma(httpHeaders, PRAGMA_RADIX_PRIORITY);
        if (priorityStr != null && !priorityStr.isEmpty()) {
            try {
                return Integer.parseInt(priorityStr);
            } catch (RuntimeException ex) {
                return -1;
            }
        }
        return -1;
    }

    static public String prepareSoapRequestHeader(String host, boolean keepConnect, String soapAction, Map<String, String> headerAttrs, int contentLen) {
        return prepareSoapRequestHeader(host, null, keepConnect, soapAction, headerAttrs, contentLen);
    }

    static public String prepareSoapRequestHeader(String host, String requestUri, boolean keepConnect, String soapAction, Map<String, String> headerAttrs, int contentLen) {
        return addHeaderAttrs(
                MessageFormat.format(soapRqHeaderMask,
                        requestUri == null || requestUri.isEmpty() ? "/" : requestUri,
                        host,
                        keepConnect ? KEEP_ALIVE : "close",
                        Integer.toString(contentLen),
                        soapAction,
                        getAttr(headerAttrs, EHttpParameter.HTTP_USER_AGENT_ATTR.getValue(), "RadixWare")),
                headerAttrs);
    }

    static public String prepareSoapResponseHeader(boolean keepConnect, String soapAction, Map<String, String> headerAttrs, int contentLen) {
        return addHeaderAttrs(
                MessageFormat.format(soapRsHeaderMask,
                        keepConnect ? KEEP_ALIVE : "close",
                        Integer.toString(contentLen),
                        soapAction),
                headerAttrs);
    }

    static public String prepareRequestHeader(String method, String path, String host, boolean keepConnect, Map<String, String> headerAttrs, int contentLen) {
        return prepareRequestHeader(method, path, host, keepConnect, headerAttrs, contentLen, DEFAULT_HTTP_CONTENT_CHAR_SET);
    }

    static public String prepareRequestHeader(String method, String path, String host, boolean keepConnect, Map<String, String> headerAttrs, int contentLen, String httpContentCharSet) {
        return addHeaderAttrs(
                MessageFormat.format(rqHeaderMask,
                        method,
                        path,
                        host,
                        keepConnect ? KEEP_ALIVE : "close",
                        Integer.toString(contentLen),
                        getAttr(headerAttrs, EHttpParameter.HTTP_CONTENT_TYPE_ATTR.getValue(), "text/html; charset=\"" + (httpContentCharSet == null ? DEFAULT_HTTP_CONTENT_CHAR_SET : httpContentCharSet) + "\""),
                        getAttr(headerAttrs, EHttpParameter.HTTP_USER_AGENT_ATTR.getValue(), "RadixWare")),
                headerAttrs);
    }

    static String getAttr(final Map<String, String> attrs, final String attrName, final String defaultValue) {
        if (attrs == null || !attrs.containsKey(attrName)) {
            return defaultValue;
        }
        return attrs.get(attrName);
    }

    static public String prepareResponseHeader(String responseCodeStr, boolean keepConnect, Map<String, String> headerAttrs, int contentLen) {
        return prepareResponseHeader(responseCodeStr, keepConnect, headerAttrs, contentLen, DEFAULT_HTTP_CONTENT_CHAR_SET);
    }

    static public String prepareResponseHeader(String responseCodeStr, boolean keepConnect, Map<String, String> headerAttrs, int contentLen, String contentCharSet) {
        return addHeaderAttrs(
                MessageFormat.format(rsHeaderMask,
                        responseCodeStr,
                        keepConnect ? KEEP_ALIVE : CLOSE,
                        Integer.toString(contentLen),
                        getAttr(headerAttrs, EHttpParameter.HTTP_CONTENT_TYPE_ATTR.getValue(), "text/html; charset=\"" + (contentCharSet == null ? DEFAULT_HTTP_CONTENT_CHAR_SET : contentCharSet) + "\"")),
                headerAttrs);
    }

    static public String prepareSoapFaultHeader(Map<String, String> headerAttrs, final int contentLen) {
        return prepareSoapFaultHeader(headerAttrs, contentLen, false);
    }

    static public String prepareSoapFaultHeader(Map<String, String> headerAttrs, final int contentLen, final boolean keepAlive) {
        return MessageFormat.format(soapFaultHeaderMask, keepAlive ? KEEP_ALIVE : CLOSE, Integer.toString(contentLen));
    }

    static public boolean getKeepConnectionAlive(final Map<String, String> headerAttrs) {
        if (headerAttrs != null) {
            final String attr = headerAttrs.get("connection");
            return attr == null || attr.equals(KEEP_ALIVE);
        }
        return false;
    }

    /**
     *
     * @param stream - stream to be readed from
     * @param headerAttrs - if not null header's attributes will be returened in
     * the map. Attribute name in lowercase will be a key. Attribute value in
     * lowercase wil be a value.
     * @return return content length
     * @throws IOException
     */
    static public int readHeader(final InputStream stream, final Map<String, String> headerAttrs) throws IOException {	//return context length
        return readHeader(stream, headerAttrs, null, null);
    }

    public static void readHeaderContent(final InputStream stream, final StringBuilder httpHeader) throws IOException {
        final int[] lastBytes = {0, 0, 0};

        while (true) {
            int b = stream.read();
            if (b < 0) {
                throw new EOFException();
            }
            if (b == 0x0A && lastBytes[0] == 0x0D && lastBytes[1] == 0x0A && lastBytes[2] == 0x0D) {
                break;
            }
            lastBytes[0] = lastBytes[1];
            lastBytes[1] = lastBytes[2];
            lastBytes[2] = b;
            httpHeader.append((char) b);
            if (httpHeader.length() == 2 && httpHeader.charAt(0) == '\r' && httpHeader.charAt(1) == '\n') {
                //RADIX-1613: In the interest of robustness, servers SHOULD ignore any empty line(s) received where a Request-Line is expected
                httpHeader.setLength(0);
                lastBytes[0] = lastBytes[1] = lastBytes[2] = 0;
            }
        }
    }

    /**
     *
     * @param stream - stream to be readed from
     * @param headerAttrs - if not null header's attributes will be returened in
     * the map. Attribute name in lowercase will be a key. Attribute value in
     * lowercase wil be a value.
     * @param headerStatusLineItems - if not null header's status line items (in
     * original case)will be returened in the list.
     * @return return content length
     * @throws IOException
     */
    static public int readHeader(final InputStream stream, final Map<String, String> headerAttrs, final List<String> statusLineItems, final LocalTracer tracer) throws IOException {
        StringBuilder httpHeader = new StringBuilder();
        try {
            readHeaderContent(stream, httpHeader);
            if (headerAttrs != null) {
                headerAttrs.clear();
            }
            int packetLen = 0;
            int pos1 = httpHeader.indexOf("\r\n");
            if (pos1 < 0) {
                throw new IOException("Invalid HTTP header");
            }
            if (statusLineItems != null) {//RADIX-1613: return status-line if requested
                statusLineItems.clear();
                statusLineItems.addAll(Arrays.asList(httpHeader.substring(0, pos1).split(" ")));
            }
            if (headerAttrs != null) {
                headerAttrs.put(REQUEST_LINE_HEADER_KEY, httpHeader.substring(0, pos1));
            }

            pos1 += 2; // skipping "\r\n"
            while (pos1 >= 0) {
                while (httpHeader.charAt(pos1) == ' ') {
                    pos1++;
                }
                int pos2 = httpHeader.indexOf(":", pos1);
                if (pos2 < 0) {
                    break;
                }
                int pos3 = pos2 + 1;
                while (httpHeader.charAt(pos2 - 1) == ' ') {
                    pos2--;
                }
                final String name = httpHeader.substring(pos1, pos2).toLowerCase();
                while (httpHeader.charAt(pos3) == ' ') {
                    pos3++;
                }
                int pos4 = httpHeader.indexOf("\r\n", pos3);
                if (pos4 < 0) {
                    throw new IOException("Invalid HTTP header");
                }
                pos1 = pos4 + 2;
                while (httpHeader.charAt(pos4 - 1) == ' ') {
                    pos4--;
                }
                String val = pos3 <= pos4 ? httpHeader.substring(pos3, pos4).toLowerCase() : null;
                if (name.equals("content-length")) {
                    try {
                        packetLen = Integer.parseInt(val);
                    } catch (NumberFormatException e) {
                        throw new IOException("Invalid HTTP header CONTENT-LENGTH attribute", e);
                    }
                } else if (headerAttrs != null) {
                    headerAttrs.put(name, val);
                }
            }

            return packetLen;
        } catch (IOException ex) {
            if (httpHeader.length() == 0 && !(ex instanceof SocketTimeoutException)) {
                //when kept connection is closed by the remote side, we can face
                //either end of stream (indicated by EOFException thrown by our own code)
                //or IOException("An existing connection was forcibly closed by the remote host") (RADIX-6981).
                //In both cases, we didn't receive any data and EOFException designed to say
                //"Hey, connection is closed for some reason, but i didn't read anything from it at all"
                //In some cases such exceptions can be silently ignored (e.g., when server understands that client has closed kept connection)
                throwEOF();
            }
            throw ex;
        }
    }

    /**
     * read header and body
     *
     * @param stream - stream to be readed from
     * @param headerAttrs - if not null header's attributes will be returened in
     * the map. Attribute name in lowercase will be a key. Attribute value in
     * lowercase wil be a value.
     * @return body
     * @throws IOException
     */
    static public byte[] readMessage(final InputStream stream, final Map<String, String> headerAttrs) throws IOException {
        return readMessage(stream, headerAttrs, null, null);
    }

    static public byte[] readMessage(final InputStream stream, final Map<String, String> headerAttrs, final LocalTracer tracer) throws IOException {
        return readMessage(stream, headerAttrs, null, tracer);
    }

    /**
     * read header and body
     *
     * @param stream - stream to be readed from
     * @param headerAttrs - if not null header's attributes will be returened in
     * the map. Attribute name in lowercase will be a key. Attribute value in
     * lowercase wil be a value.
     * @param headerStatusLineItems - if not null header's status line items (in
     * original case) will be returened in the list
     * @return body
     * @throws IOException
     */
    static public byte[] readMessage(final InputStream socketStream, Map<String, String> headerAttrs, final List<String> headerStatusLineItems, final LocalTracer tracer) throws IOException {
        
        if (headerAttrs == null) {
            headerAttrs = new HashMap<>();
        }
        
        final UnreadableInputStream stream = new UnreadableInputStream(socketStream);
        int len = readHeader(stream, headerAttrs, headerStatusLineItems, tracer);

        if (len == 0 && headerAttrs != null && headerAttrs.containsKey(TRANSFER_ENCODING_HEADER_ATTR) && CHUNKED.equals(headerAttrs.get(TRANSFER_ENCODING_HEADER_ATTR))) {
            final ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
            int chunkSize;
            int cur, prev;
            int n;
            while (true) {
                chunkSize = readChunkSize(stream);

                prev = -1;
                //skip chunk extensions
                while (true) {
                    cur = stream.read();
                    if (cur < 0) {
                        throwEOF();
                    }
                    if (cur == '\n' && prev == '\r') {
                        break;
                    }
                    prev = cur;
                }

                n = 0;
                while (n < chunkSize) {
                    cur = stream.read();
                    if (cur < 0) {
                        throwEOF();
                    }
                    bos.write(cur);
                    n++;
                }

                if (chunkSize == 0) {//last chunk
                    cur = stream.read();
                    if (cur < 0) {
                        throwEOF();
                    }
                    stream.unread((byte) cur);
                    if (cur == '\r') {
                        consumeCRLF(stream);
                    } else {
                        final StringBuilder trailerHeaderBuilder = new StringBuilder();
                        readHeaderContent(stream, trailerHeaderBuilder);
                        final String trailerHeader = trailerHeaderBuilder.toString();
                        if (!trailerHeader.endsWith("\r\n\r")) {
                            throw new IOException("Invalid trailer header: " + trailerHeader);
                        }
                        final String[] headers = trailerHeader.substring(0, trailerHeader.length() - 3).split("\r\n");
                        for (String header : headers) {
                            int colonIdx = header.indexOf(":");
                            if (colonIdx < 0) {
                                headerAttrs.put(header, null);
                            } else {
                                headerAttrs.put(header.substring(0, colonIdx).trim().toLowerCase(), header.substring(colonIdx + 1, header.length()).trim().toLowerCase());
                            }
                        }
                    }
                    break;
                } else {
                    consumeCRLF(stream);
                }
            }
            return bos.toByteArray();
        } else {
            final byte[] mess = new byte[len];
            int n = 0;
            while (n < mess.length) {
                int count = stream.read(mess, n, mess.length - n);
                if (count < 0) {
                    throwEOF();
                }
                n += count;
            }

            return mess;
        }
    }

    private static int readChunkSize(final UnreadableInputStream stream) throws IOException {
        int c;
        int len = 0;
        while (true) {
            c = stream.read();
            if (c < 0) {
                throwEOF();
            }
            try {
                if (c >= 'a' && c <= 'f') {
                    c = c - 'a' + 'A';
                }
                len = 16 * len + Framer.getHexValueFromAsciiByte(c);
                if (len > Integer.MAX_VALUE / 16) {
                    throw new IOException("Chunk size is too big: " + len);
                }
            } catch (WrongFormatError ex) {
                stream.unread((byte) c);
                return len;
            }
        }
    }

    private static void throwEOF() throws EOFException {
        throw new EOFException("Can't read HTTP message: EOF");
    }

    private static void consumeCRLF(final InputStream stream) throws IOException {
        int b1 = stream.read();
        if (b1 < 0) {
            throwEOF();
        }
        int b2 = stream.read();
        if (b2 < 0) {
            throwEOF();
        }
        if (b1 != '\r' || b2 != '\n') {
            throw new IOException("Unable to read HTTP message: exptected CRLF, but got " + b1 + "," + b2);
        }
    }
}
