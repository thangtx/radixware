/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.common.svn.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.svn.RadixSvnException;

/**
 *
 * @author akrylov
 */
public class RAMessage {

    public enum MessageItemType {

        WORD,
        NUMBER,
        STRING,
        LIST,
        BYTES
    }

    public RAMessage() {
    }

    public static class MessageItemTemplate {

        private final boolean optional;
        private final boolean multiple;
        private final MessageItemType type;

        private MessageItemTemplate(boolean optional, boolean multiple, MessageItemType type) {
            this.optional = optional;
            this.multiple = multiple;
            this.type = type;
        }

        public boolean isOptional() {
            return optional;
        }

        public boolean isMultiple() {
            return multiple;
        }

        public MessageItemType getType() {
            return type;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();

            sb.append(type.name());
            if (isOptional()) {
                sb.append('?');
            }
            if (isMultiple()) {
                sb.append('*');
            }
            return sb.toString();
        }

    }
    public static final MessageItemTemplate WORD = new MessageItemTemplate(false, false, MessageItemType.WORD);
    public static final MessageItemTemplate WORD_OPT = new MessageItemTemplate(true, false, MessageItemType.WORD);
    public static final MessageItemTemplate WORD_MUL = new MessageItemTemplate(false, true, MessageItemType.WORD);
    public static final MessageItemTemplate WORD_OPT_MUL = new MessageItemTemplate(true, true, MessageItemType.WORD);

    public static final MessageItemTemplate NUMBER = new MessageItemTemplate(false, false, MessageItemType.NUMBER);
    public static final MessageItemTemplate NUMBER_OPT = new MessageItemTemplate(true, false, MessageItemType.NUMBER);
    public static final MessageItemTemplate NUMBER_MUL = new MessageItemTemplate(false, true, MessageItemType.NUMBER);
    public static final MessageItemTemplate NUMBER_OPT_MUL = new MessageItemTemplate(true, true, MessageItemType.NUMBER);

    public static final MessageItemTemplate STRING = new MessageItemTemplate(false, false, MessageItemType.STRING);
    public static final MessageItemTemplate STRING_OPT = new MessageItemTemplate(true, false, MessageItemType.STRING);
    public static final MessageItemTemplate STRING_MUL = new MessageItemTemplate(false, true, MessageItemType.STRING);
    public static final MessageItemTemplate STRING_OPT_MUL = new MessageItemTemplate(true, true, MessageItemType.STRING);

    public static final MessageItemTemplate LIST = new MessageItemTemplate(false, false, MessageItemType.LIST);
    public static final MessageItemTemplate LIST_OPT = new MessageItemTemplate(true, false, MessageItemType.LIST);
    public static final MessageItemTemplate LIST_MUL = new MessageItemTemplate(false, true, MessageItemType.LIST);
    public static final MessageItemTemplate LIST_OPT_MUL = new MessageItemTemplate(true, true, MessageItemType.LIST);

    public static class MessageTemplate {

        private final MessageItemTemplate[] items;

        public MessageTemplate(MessageItemTemplate... items) {
            this.items = items;
        }

    }

    public static class MessageItem {

        public final MessageItemType type;
        private long longValue;
        private String stringValue;
        private byte[] byteValue;
        private List<MessageItem> listValue;

        public static MessageItem newList(MessageItem... items) {
            return new MessageItem(MessageItemType.LIST, Arrays.asList(items));
        }

        public static MessageItem emptyList() {
            return new MessageItem(MessageItemType.LIST, Collections.<MessageItem>emptyList());
        }

        public static MessageItem newList(List<MessageItem> items) {
            return new MessageItem(MessageItemType.LIST, items);
        }

        public static MessageItem newList(String[] strings) {
            List<MessageItem> items = new LinkedList<>();
            for (String s : strings) {
                items.add(MessageItem.newString(s));
            }
            return new MessageItem(MessageItemType.LIST, items);
        }

        public static MessageItem newString(String data) {
            try {
                return new MessageItem(MessageItemType.STRING, data.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException ex) {
                return null;
            }
        }

        public static MessageItem newBytes(byte[] data) {
            return new MessageItem(MessageItemType.BYTES, data);
        }

        public static MessageItem newWord(String data) {
            return new MessageItem(MessageItemType.WORD, data);
        }

        public static MessageItem newNumber(long data) {
            return new MessageItem(MessageItemType.NUMBER, data);
        }

        public static MessageItem newBoolean(boolean data) {
            return new MessageItem(MessageItemType.WORD, data ? "true" : "false");
        }

        private MessageItem(MessageItemType type, byte[] data) {
            this.type = type;
            this.byteValue = data;
        }

        private MessageItem(MessageItemType type, String data) {
            this.type = type;
            this.stringValue = data;
        }

        private MessageItem(MessageItemType type, long data) {
            this.type = type;
            this.longValue = data;
        }

        private MessageItem(MessageItemType type, List<MessageItem> data) {
            this.type = type;
            this.listValue = data;
        }

        public long getLong() {
            return longValue;
        }

        public void appendBytes(byte[] bytes) {
        }

        public String getString() {
            if (type == MessageItemType.WORD) {
                return stringValue;
            } else {
                try {
                    return new String(byteValue, 0, byteValue.length, "UTF-8");
                } catch (UnsupportedEncodingException ex) {
                    return null;
                }
            }
        }

        public boolean getBoolean() {
            if (type == MessageItemType.WORD) {
                return Boolean.parseBoolean(stringValue);
            } else {
                return false;
            }
        }

        public Date getDate() {
            return SvnUtil.parseDate(getString());
        }

        public byte[] getByteArray() {
            return byteValue;
        }

        public List<MessageItem> getList() {
            return listValue;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(type.name()).append(' ');
            switch (type) {
                case LIST:
                    sb.append("( ");
                    for (MessageItem inner : listValue) {
                        sb.append(inner.toString());
                    }
                    sb.append(") ");
                    break;
                case NUMBER:
                    sb.append(longValue).append(' ');
                    break;
                case STRING:
                    String asStr = getString();
                    sb.append(asStr.length())
                            .append(':')
                            .append(asStr)
                            .append(' ');
                    break;
                case WORD:
                    sb.append(stringValue)
                            .append(' ');
                    break;
            }

            return sb.toString();
        }
    }

    public void mergeItems(List<MessageItem> items, OutputStream out) throws RadixSvnException {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            stream.write('(');
            stream.write(' ');
            mergeItemsImpl(items, stream);
            stream.write(')');
            stream.write(' ');

            byte[] bytes = stream.toByteArray();
//            String debug = new String(bytes, "UTF-8");
//            System.out.println(debug);
            out.write(bytes);
            out.flush();
        } catch (IOException ex) {
            throw new RadixSvnException(ex);
        }
    }
    public void mergeItemsWithoutEnvelope(List<MessageItem> items, OutputStream out) throws RadixSvnException {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            mergeItemsImpl(items, stream);
            
            byte[] bytes = stream.toByteArray();
//            String debug = new String(bytes, "UTF-8");
//            System.out.println(debug);
            out.write(bytes);
            out.flush();
        } catch (IOException ex) {
            throw new RadixSvnException(ex);
        }
    }

    private void mergeItemsImpl(List<MessageItem> items, OutputStream stream) throws RadixSvnException {
        try {
            for (MessageItem item : items) {
                switch (item.type) {
                    case WORD:
                        if (item.stringValue == null || item.stringValue.isEmpty()) {
                            continue;
                        }
                        byte[] data = item.stringValue.getBytes("UTF-8");
                        stream.write(data);
                        stream.write(' ');
                        break;
                    case NUMBER:
                        data = String.valueOf(item.longValue).getBytes("UTF-8");
                        stream.write(data);
                        stream.write(' ');
                        break;
                    case STRING:
                    case BYTES:
                        if (item.byteValue == null) {
                            stream.write('0');
                            stream.write(':');
                            stream.write(' ');
                            continue;
                        }
                        data = String.valueOf(item.byteValue.length).getBytes("UTF-8");
                        stream.write(data);
                        stream.write(':');
                        stream.write(item.byteValue);
                        stream.write(' ');
                        break;
                    case LIST:
                        stream.write('(');
                        stream.write(' ');
                        if (item.listValue != null) {
                            mergeItemsImpl(item.listValue, stream);
                        }
                        stream.write(')');
                        stream.write(' ');
                        break;
                }
            }
        } catch (IOException ex) {
            throw new RadixSvnException(ex);
        }

    }

    public List<MessageItem> parseItems(InputStream input) throws IOException {
        MessageItem root = parseItemImpl(input, skipSpaces(input), 0);
        if (root.type == MessageItemType.LIST) {
            return root.getList();
        } else {
            return Collections.singletonList(root);
        }
    }

    public RAMessage.MessageItem readItem(InputStream is) throws IOException {
        return readItem(is, skipSpaces(is));
    }

    private RAMessage.MessageItem readItem(InputStream is, char ch) throws IOException {
        RAMessage.MessageItem item = null;
        if (Character.isDigit(ch)) {
            long value = Character.digit(ch, 10);
            long previousValue;
            while (true) {
                previousValue = value;
                ch = readChar(is);
                if (Character.isDigit(ch)) {
                    value = value * 10 + Character.digit(ch, 10);
                    if (previousValue != value / 10 && value != -1) {
                        throw new IOException("malformed data: Number is larger than maximum");
                    }
                    continue;
                }
                break;
            }
            if (ch == ':') {
                // string.
                byte[] buffer = new byte[(int) value];
                try {
                    int toRead = (int) value;
                    while (toRead > 0) {
                        int r = is.read(buffer, buffer.length - toRead, toRead);
                        if (r < 0) {
                            throw new IOException("malformed data");
                        }
                        toRead -= r;
                    }
                } catch (IOException e) {
                    throw new IOException("malformed data", e);
                }
                ch = readChar(is);
                return RAMessage.MessageItem.newBytes(buffer);
            } else {
                return RAMessage.MessageItem.newNumber(value);
            }
        } else if (Character.isLetter(ch)) {
            final StringBuilder buffer = new StringBuilder();
            buffer.append(ch);
            while (true) {
                ch = readChar(is);
                if (Character.isLetterOrDigit(ch) || ch == '-') {
                    buffer.append(ch);
                    continue;
                }
                break;
            }
            return RAMessage.MessageItem.newWord(buffer.toString());
        } else if (ch == '(') {
//            item.setKind(SVNItem.LIST);
//            item.setItems(new ArrayList());
            List<MessageItem> children = new ArrayList<>();
            while (true) {
                ch = skipSpaces(is);
                if (ch == ')') {
                    break;
                }
                children.add(readItem(is, ch));
            }
            ch = readChar(is);
            return RAMessage.MessageItem.newList(children);
        }
        if (!Character.isWhitespace(ch)) {
            throw new IOException("malformed data");
        }
        return null;
    }

    public MessageItem parseItemImpl(InputStream input, char c, int level) throws IOException {
        try {
            if (Character.isDigit(c)) {//string or number
                long value = Character.digit(c, 10);
                for (;;) {
                    c = readChar(input);
                    if (Character.isDigit(c)) {
                        value = value * 10 + Character.digit(c, 10);
                    } else if (c == ':') {//string
                        byte[] data = new byte[(int) value];

                        int totalCount = 0;
                        while (totalCount < value) {
                            int count = input.read(data, totalCount, (int) value - totalCount);
                            if (count < 0) {
                                throw new IOException("Unexpected end of stream");
                            }
                            totalCount += count;
                        }
                        return new MessageItem(MessageItemType.STRING, data);
                    } else if (isSpace(c)) {
                        return new MessageItem(MessageItemType.NUMBER, value);
                    } else {
                        throw new IOException("Unexpected character: " + c);
                    }
                }
            } else if (Character.isLetter(c)) {//word
                StringBuilder buffer = new StringBuilder();
                buffer.append(c);
                for (;;) {
                    c = readChar(input);
                    if (Character.isLetter(c) || Character.isDigit(c) || c == '_' || c == '-') {
                        buffer.append(c);
                    } else if (isSpace(c)) {
                        return new MessageItem(MessageItemType.WORD, buffer.toString());
                    } else {
                        throw new IOException("Unexpected character: " + c);
                    }
                }
            } else if (c == '(') {//list
                List<MessageItem> inner = new ArrayList<>();
                for (;;) {
                    c = skipSpaces(input);
                    if (c == ')') {
                        if (level == 0) {//top level message finished. read tail space
                            c = readChar(input);
                            if (c != ' ') {
                                throw new IOException("Something went wrong");
                            }
                        }
                        return new MessageItem(MessageItemType.LIST, inner);
                    } else {
                        inner.add(parseItemImpl(input, c, level + 1));
                    }
                }
            }
        } catch (EOFException ex) {
            return null;
        }
        throw new IOException("Unexpected character: " + c);
    }

    private char skipSpaces(InputStream in) throws IOException {
        for (;;) {
            char c = readChar(in);
            if (!isSpace(c)) {
                return c;
            }
        }
    }

    private boolean isSpace(char c) {
        return c == ' ' || c == '\n';
    }

    private char readChar(InputStream is) throws IOException {
        int r = 0;
        try {
            r = is.read();

            if (r < 0) {
                throw new java.io.EOFException();
            }
        } catch (NullPointerException ex) {
            throw ex;
        } catch (IOException e) {
            throw e;
        }
        char c = (char) (r & 0xFF);
        return c;
    }

    private static class EOFException extends IOException {
    }

}
