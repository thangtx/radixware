/*
 * Copyright (c) 2008-2018, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */
package org.radixware.kernel.common.utils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.radixware.kernel.common.enums.EHttpParameter;
import org.radixware.kernel.common.exceptions.FrameTooLargeException;
import org.radixware.kernel.common.exceptions.WrongFormatError;

//////////////////////////////////////////////////Format ///////////////////////////////////////////////////////
//STX L:ASCII[6] <###> ETX                     "%S%[6]D%P%E"
//STX <###> ETX                                "%S%P%E"
//<###> ETX [CR]                               "%[0D]A%P%E"
//L1 L2 <###>                                  "%[2]L%P"
//L2 L1 <###>                                  "%[2R]L%P"
//L2 L1 0 0 <###>                              "%[2R]L\x00\x00%P"
//STX L:BCD2 <###> ETX LRC(L,<###>,ETX)        "%S%[2]B%P%E%[*2-4]C"
//L2 L1 0 0 0 0 0 <###>                        "%[2R]L\x00\x00\x00\x00\x00%P"
//STX L2 L1 <###> LRC(###) ETX                 "%S%[2R]L%P%[*3-3]C%E"
//0xFF L:ASCII[6] <###>                        "\xFF%[6]D%P"
//L4 L3 L2 L1 <###>                            "%[4R]L%P"
//L2 L1 0 <###>                                "%[2R *2-3]L\x00%P"
//< <###> >                                    "<%P>"
//STX <###> ETX LRC(<###>,ETX)                 "%S%P%E%[*2-3]C"
//L:ASCII[6] <###>                             "%[6]D%P"
//                                             "%[R]H" "%[S]H"
//0xFF <###> 0xFF with 2s timeout              "[RTO_MS=2000]\xFF%P\xFF"
//0xFF <###> 0xFF with 2KB payload data limit  "[MAX_LEN=2048]\xFF%P\xFF"
// combination of both previous formats        "[RTO_MS=2000,MAX_LEN=2048]\xFF%P\xFF"
/**
 * Framer.
 */
public final class Framer {

    //
    private static final String DEFAULT_HOST_PARAM = SystemPropUtils.getStringSystemProp("rdx.framer.default.host.header", "unknown");
    //

    public static final String FRAME_RECEIVE_TIMEOUT_MILLIS_PARAM = "RTO_MS";
    public static final String FRAME_PAYLOAD_DATA_MAX_LENGTH_BYTES_PARAM = "MAX_LEN";
    public static final String CLEAR_DATA_FRAME_FORMAT = "%P";
    private static final String HTTP_OK = "200";
    private static final int PAR_STX = 'S';
    private static final int PAR_ETX = 'E';
    private static final int PAR_SPACE = 'A';
    private static final int PAR_PACKET = 'P';
    private static final int PAR_LEN = 'L';
    private static final int PAR_LEN_HEX = 'X';
    private static final int PAR_LEN_DEC = 'D';
    private static final int PAR_LEN_BCD = 'B';
    private static final int PAR_LRC = 'C';
    private static final int PAR_HTTP = 'H';
    private static final int PAR_JUNK = '*';
    private static final int PAR_STR = -1;
    private static final int PAR_FIELD = 'F';
    //private static final int ATTR_OPTIONAL      = 'O';
    private static final int ATTR_REVERSE = 'R';
    private static final int ATTR_REQUEST = 'R';
    private static final int ATTR_LRC_HEX = 'X';

    //private static final int ATTR_RESPONSE      = 'S';
    private static final class FormatParameter {

        int param;
        String attr;
        ByteBuffer str;
        byte[] attrSpace;
        int attrLenLen;
        boolean attrLenReverse;
        boolean attrRequest;
        boolean attrLrcHex;
        int attrRangeStart;
        int attrRangeEnd;
        String fieldName;
        int fieldLen;

        FormatParameter() {
            param = -1;
            attrLenLen = 0;
            attrLenReverse = false;
            attrRequest = false;
            attrLrcHex = false;
            attrRangeStart = -1;
            attrRangeEnd = -1;
            fieldLen = -1;
        }

        void parseAttr() {
            switch (param) {
                case PAR_FIELD:
                    if (attr == null || attr.isEmpty()) {
                        throw new WrongFormatError("%F element should have options [<length> <header attr name>], for example %[2 messageTag]F");
                    }
                    final String[] lenAndName = attr.split(" ");
                    if (lenAndName.length == 0 || lenAndName.length > 2) {
                        throw new WrongFormatError("Invalid attributes for %F: " + attr + ", too many spaces");
                    }
                    fieldLen = Integer.parseInt(lenAndName[0]);
                    if (lenAndName.length == 2) {
                        fieldName = lenAndName[1];
                    }
                    if (fieldLen == -1) {
                        throw new WrongFormatError("Length should be defined for %F (ex: %[2 messTag]F");
                    }
                    break;
                case PAR_SPACE: {
                    if (attr.length() != 0) {
                        attrSpace = Hex.decode(attr);
                    }
                    break;
                }
                case PAR_LEN:
                case PAR_LEN_DEC:
                case PAR_LEN_HEX:
                case PAR_LEN_BCD: {
                    final StringTokenizer st = new StringTokenizer(attr, " ");
                    while (st.hasMoreTokens()) {
                        final String tk = st.nextToken();
                        if (tk.length() != 0) {
                            if (tk.charAt(0) == '*') {
                                parseRange(tk);
                            } else {
                                attrLenLen = tk.charAt(0) - '0';
                                attrLenReverse = (param == PAR_LEN && tk.length() > 1 && tk.charAt(1) == ATTR_REVERSE);
                            }
                        }
                    }
                    if (attrLenLen == 0) {
                        throw new WrongFormatError("Length format parameter is invalid", null);
                    }
                    break;
                }
                case PAR_LRC: {
                    final String[] formatAndRange = attr.split(" ");
                    final String rangeString;
                    if (formatAndRange.length == 2) {
                        if (formatAndRange[0].length() != 1 || formatAndRange[0].charAt(0) != ATTR_LRC_HEX) {
                            throw new WrongFormatError("Framer format parameter PAR_LRC supports only 'H' attribute, but '" + formatAndRange[0] + "' was found");
                        } else {
                            attrLrcHex = true;
                            rangeString = formatAndRange[1];
                        }
                    } else {
                        rangeString = attr;
                    }
                    if (rangeString.length() != 0 && rangeString.charAt(0) == '*') {
                        parseRange(rangeString);
                    }
                    if (attrRangeStart == -1) {
                        throw new WrongFormatError("Framer format parameter PAR_LRC is invalid", null);
                    }
                    break;
                }
                case PAR_HTTP: {
                    if (attr.length() == 0) {
                        throw new WrongFormatError("Framer format parameter PAR_HTTP is invalid", null);
                    }
                    attrRequest = attr.charAt(0) == ATTR_REQUEST;
                    break;
                }
            }
        }

        int getLength() {
            switch (param) {
                case PAR_ETX:
                case PAR_STX:
                    return 1;
                case PAR_LRC:
                    return attrLrcHex ? 2 : 1;
                case PAR_LEN:
                case PAR_LEN_DEC:
                case PAR_LEN_BCD:
                case PAR_LEN_HEX:
                    return attrLenLen;
                case PAR_STR:
                    return str.position();
                default:
                    return 0;
            }
        }

        private void parseRange(final String range) {
            final StringTokenizer st = new StringTokenizer(range.substring(1), "-");
            if (st.hasMoreTokens()) {
                attrRangeStart = Integer.parseInt(st.nextToken());
                if (st.hasMoreTokens()) {
                    attrRangeEnd = Integer.parseInt(st.nextToken());
                } else {
                    attrRangeEnd = attrRangeStart;
                }
            } else {
                throw new WrongFormatError("Framer format parameter PAR_LRC is invalid", null);
            }
        }
    }

    private static class Frame {

        private final String format;
        final List<FormatParameter> params;
        private int receiveTimeoutMillis = -1;
        private int maxLengthBytes = 128 * 1024 * 1024;
        int paramsIndex;

        Frame(final String format) {
            this.format = format;
            paramsIndex = 0;
            params = new ArrayList<>();

            int frameItemsStartOffset = 0;
            if (!format.isEmpty() && format.charAt(0) == '[') {
                int overallEndOffs = format.indexOf(']');
                if (overallEndOffs > 0) {
                    parseOverallOpts(format.substring(1, overallEndOffs));
                    frameItemsStartOffset = overallEndOffs + 1;
                } else {
                    throw new WrongFormatError("Closing ']' not found for overall params part");
                }
            }

            final int index[] = new int[]{frameItemsStartOffset};
            final StringBuilder tmpAttr = new StringBuilder();
            ByteBuffer tmpStr;
            final int tmpParam[] = new int[]{-1};
            while (index[0] < format.length()) {
                final FormatParameter param = new FormatParameter();
                tmpStr = ByteBuffer.allocate(format.length());
                parseFormatBytes(format.getBytes(StandardCharsets.US_ASCII), tmpParam, tmpAttr, tmpStr, index);
                param.param = tmpParam[0];
                param.attr = tmpAttr.toString();
                param.str = tmpStr;
                param.parseAttr();
                params.add(param);
            }
        }

        public int getReceiveTimeoutMillis() {
            return receiveTimeoutMillis;
        }
        
        public int getMaxLengthBytes() {
            return maxLengthBytes;
        }

        boolean isHTTP() {
            for (FormatParameter param : params) {
                if (param.param == PAR_HTTP) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public String toString() {
            return getClass().getSimpleName() + "{" + format + "}";
        }

        private void parseOverallOpts(final String opts) {
            final String[] nameAndValStrs = opts.split(",");
            for (String nameAndValStr : nameAndValStrs) {
                final String[] nameAndVal = nameAndValStr.split("=");
                if (nameAndVal.length != 2) {
                    throw new WrongFormatError("Expected '=' in '" + nameAndValStr + "'");
                }
                final String name = nameAndVal[0].trim();
                if (FRAME_RECEIVE_TIMEOUT_MILLIS_PARAM.equals(name)) {
                    try {
                        receiveTimeoutMillis = Integer.parseInt(nameAndVal[1]);
                        if (receiveTimeoutMillis <= 0) {
                            throw new WrongFormatError("'" + FRAME_RECEIVE_TIMEOUT_MILLIS_PARAM + "' should be > 0");
                        }
                    } catch (NumberFormatException ex) {
                        throw new WrongFormatError("Invalid value of '" + FRAME_RECEIVE_TIMEOUT_MILLIS_PARAM + "'", ex);
                    }
                } else if (FRAME_PAYLOAD_DATA_MAX_LENGTH_BYTES_PARAM.equals(name)) {
                    try {
                        maxLengthBytes = Integer.parseInt(nameAndVal[1]);
                        if (maxLengthBytes <= 0) {
                            throw new WrongFormatError("'" + FRAME_PAYLOAD_DATA_MAX_LENGTH_BYTES_PARAM + "' should be > 0");
                        }
                    } catch (NumberFormatException ex) {
                        throw new WrongFormatError("Invalid value of '" + FRAME_PAYLOAD_DATA_MAX_LENGTH_BYTES_PARAM + "'", ex);
                    }
                } else {
                    throw new WrongFormatError("Unexpected overall param: '" + name + "'");
                }
            }
        }
    }

    private static void parseFormatBytes(final byte[] format, final int[] param, final StringBuilder attr, final ByteBuffer str, final int[] index) {
        param[0] = -1;
        attr.setLength(0);
        str.clear();
        boolean escaped = false;
        for (; index[0] < format.length; index[0]++) {
            switch (format[index[0]]) {
                case '\\':
                    if (!escaped) {
                        escaped = true;
                        continue;
                    }
                    str.put(format[index[0]]);
                    break;
                case '%':
                    if (!escaped && index[0] + 1 != format.length) {
                        int index_sv = index[0];
                        if (str.position() != 0) {
                            return;
                        }
                        int ch = format[++index[0]];
                        if (ch == '[') {
                            while (++index[0] != format.length && format[index[0]] != ']') {
                                attr.append((char) format[index[0]]);
                            }
                            if (++index[0] < format.length) {
                                param[0] = format[index[0]];
                            } else {
                                index[0] = index_sv;
                                str.put(format[index[0]]);
                                break;
                            }
                        } else {
                            param[0] = ch;
                        }
                        index[0]++;
                        return;
                    } else {
                        str.put(format[index[0]]);
                    }
                    break;
                case 'n':
                    if (escaped) {
                        str.put((byte) 0x0D);
                        str.put((byte) 0x0A);
                    } else {
                        str.put(format[index[0]]);
                    }
                    break;
                case 'x':
                    if (escaped && index[0] + 2 < format.length) {
                        //int i1 = ParseHexChar(format[++index]);
                        //int i2 = ParseHexChar(format[++index]);
                        //str += ((i1 << 4) | i2);
                        str.put(Hex.decode(new String(new byte[]{format[++index[0]], format[++index[0]]}, StandardCharsets.US_ASCII)));
                    } else {
                        str.put(format[index[0]]);
                    }
                    break;
                default:
                    str.put(format[index[0]]);
            }
            escaped = false;
        }
    }

    private String encodeAsString(final ByteBuffer buffer) {
        if (buffer == null) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < buffer.position(); i++) {
            sb.append(Hex.byte2Hex(byte2unsignedByte(buffer.get(i))));
        }
        return sb.toString();
    }

    private static final class InFrame extends Frame {

        InFrame(final String format) {
            super(format);
            block = ByteBuffer.allocate(0);
            len = -1;
            blockToRead = 0;
            block.clear();
        }
        int len;
        ByteBuffer block;
        int blockToRead;
    }

    private static final class OutFrame extends Frame {

        OutFrame(final String format) {
            super(format);
            block = EMPTY_BLOCK; //ByteBuffer.allocate(0);
        }
        byte[] block;
    }
    private static final byte[] EMPTY_BLOCK = new byte[]{};
//////////////////////////////////////////////	 Framer /////////////////////////////////////////////
    private final UnreadableInputStream in;
    private final OutputStream out;
    private final InFrame inFrame;
    private final OutFrame outFrame;

    public Framer(final InputStream pIn, final OutputStream pOut, final String inFormat, String outFormat) {
        in = new UnreadableInputStream(pIn);
        out = pOut;
        if (inFormat.length() == 0) {
            throw new WrongFormatError("Input format is empty", null);
        }
        outFormat = outFormat.replaceAll("%\\*", "");
        if (outFormat.length() == 0) {
            throw new WrongFormatError("Output format is empty", null);
        }
        inFrame = new InFrame(inFormat);
        outFrame = new OutFrame(outFormat);
    }

    public int getFullFrameReceiveTimeoutMillis() {
        return inFrame.getReceiveTimeoutMillis();
    }

    public byte[] recvFrame(final long timeoutMillis, final Map<String, String> headerInfo) throws IOException, InvalidDataException {
        try {
            final ArrayList<ByteBuffer> params = new ArrayList<>();
            ByteBuffer pck = null;
            final long timeoutTimeMillis = timeoutMillis <= 0 ? 0 : System.currentTimeMillis() + timeoutMillis;
            for (;;) {
                PARAMS_LOOP:
                while (inFrame.paramsIndex < inFrame.params.size()) {
                    while (inFrame.blockToRead == -1 || inFrame.block.position() != inFrame.blockToRead) {
                        if (inFrame.blockToRead == -1) {
                            if (inFrame.block.position() == 0) {
                                inFrame.block = ByteBuffer.allocate(1024);
                            } else if (inFrame.block.position() == inFrame.block.capacity()) {
                                final ByteBuffer b = ByteBuffer.allocate(inFrame.block.capacity() + 1024);
                                b.put(inFrame.block);
                                inFrame.block = b;
                            }
                        } else if (inFrame.block.position() == 0) {
                            inFrame.block = ByteBuffer.allocate(inFrame.blockToRead);
                        }
                        final byte[] tmp = new byte[inFrame.block.capacity() - inFrame.block.position()];
                        final int ret = in.read(tmp);
                        if (ret > 0) {
                            inFrame.block.put(tmp, 0, ret);
                            continue;
                        }
                        if (ret == -1) {
                            if (inFrame.params.size() == 1 && inFrame.params.get(0).param == PAR_PACKET) {
                                pck = inFrame.block;
                                break PARAMS_LOOP;
                            }
                            throw new EOFException("EOF on frame receive");
                        }
                        if (timeoutTimeMillis != 0 && System.currentTimeMillis() > timeoutTimeMillis) {
                            if (inFrame.blockToRead == -1 && inFrame.block.position() != 0) {
                                inFrame.block.flip();//erase(inFrame.blockIndex);
                                final byte[] res = new byte[inFrame.block.limit()];
                                inFrame.block.get(res);
                                return res;
                            }
                            return null;
                        }
                    }
                    if (inFrame.blockToRead != 0) {
                        inFrame.blockToRead = 0;
                        params.add(inFrame.block);
                    }
                    FormatParameter param = inFrame.params.get(inFrame.paramsIndex);
                    switch (param.param) {
                        case PAR_FIELD: {
                            final byte[] fieldData = new byte[param.fieldLen];
                            for (int i = 0; i < param.fieldLen; i++) {
                                int ch = in.read();
                                if (ch == -1) {
                                    throw new EOFException("EOF on frame receive");
                                }
                                if (ch == -2) {
                                    return null;
                                }
                                fieldData[i] = (byte) ch;
                            }
                            if (headerInfo != null && param.fieldName != null && !param.fieldName.isEmpty()) {
                                headerInfo.put(param.fieldName, Hex.encode(fieldData));
                            }
                            break;
                        }
                        case PAR_SPACE: {
                            for (;;) {
                                final int ch = in.read();
                                if (ch == -1) {
                                    throw new EOFException("EOF on frame receive");
                                }
                                if (ch == -2) {
                                    return null;
                                }
                                boolean found = false;
                                for (byte b : param.attrSpace) {
                                    if (b == ch) {
                                        found = true;
                                        break;
                                    }
                                }
                                if (!found) {
                                    in.unread((byte) ch);
                                    break;
                                }
                            }
                            break;
                        }
                        case PAR_JUNK: {
                            int end_char;
                            if (inFrame.paramsIndex + 1 < inFrame.params.size()) {
                                final FormatParameter p = inFrame.params.get(inFrame.paramsIndex + 1);
                                if (p.param == PAR_ETX) {
                                    end_char = 0x3;
                                } else if (p.param == PAR_STR) {
                                    end_char = byte2unsignedByte(p.str.get(0));
                                } else if (p.param == PAR_STX) {
                                    end_char = 0x2;
                                } else {
                                    throw new WrongFormatError("Invalid frame format: %* requires STX, ETX, of explicit byte after it");
                                }
                            } else {
                                throw new WrongFormatError("Invalid frame format: %* can not be the last parameter");
                            }
                            for (;;) {
                                final int ch = in.read();
                                if (ch == -1) {
                                    throw new EOFException("EOF on frame receive");
                                }
                                if (ch == -2) {
                                    return null;
                                }
                                if (ch == end_char) {
                                    in.unread((byte) ch);
                                    break;
                                }
                            }
                            break;
                        }
                        case PAR_STX: {
                            if (inFrame.block.position() == 0) {
                                inFrame.blockToRead = 1;
                            } else if (param.param == PAR_STX && inFrame.block.get(0) != 0x02) {
                                inFrame.block.clear();
                                throw new InvalidDataException("Frame receive error: no STX", null);
                            }
                            break;
                        }
                        case PAR_ETX: {
                            if (inFrame.block.position() == 0) {
                                inFrame.blockToRead = 1;
                            } else if (param.param == PAR_ETX && inFrame.block.get(0) != 0x03) {
                                inFrame.block.clear();
                                throw new InvalidDataException("Frame receive error: no ETX", null);
                            }
                            break;
                        }
                        case PAR_PACKET: {
                            if (inFrame.block.position() == 0 && inFrame.len != 0) {
                                if (inFrame.len != -1) {
                                    inFrame.blockToRead = inFrame.len;
                                } else if (inFrame.paramsIndex + 1 < inFrame.params.size()) {
                                    int end_char;
                                    final FormatParameter p = inFrame.params.get(inFrame.paramsIndex + 1);
                                    if (p.param == PAR_ETX) {
                                        end_char = 0x3;
                                    } else if (p.param == PAR_STR) {
                                        end_char = byte2unsignedByte(p.str.get(0));
                                    } else {
                                        throw new WrongFormatError("Invalid frame format: Frame length is unknown", null);
                                    }
                                    pck = ByteBuffer.allocate(1024);
                                    for (;;) {
                                        int ch = in.read();
                                        if (ch == -1) {
                                            throw new EOFException("EOF on frame receive");
                                        }
                                        if (ch == -2) {
                                            return null;
                                        }
                                        if (ch != end_char) {
                                            if (pck.position() == pck.capacity()) {
                                                final ByteBuffer b = ByteBuffer.allocate(pck.capacity() * 2);
                                                pck.position(0);
                                                b.put(pck);
                                                pck = b;
                                            }
                                            pck.put((byte) ch);
                                        } else {
                                            in.unread((byte) ch);
                                            break;
                                        }
                                        Framer.assertFrameLength(pck.position(), inFrame.getMaxLengthBytes());
                                    }
                                    params.add(pck);
                                } else {
                                    inFrame.blockToRead = -1;
                                }
                            } else {
                                pck = inFrame.block;
                            }
                            break;
                        }
                        case PAR_LEN:
                        case PAR_LEN_DEC:
                        case PAR_LEN_HEX:
                        case PAR_LEN_BCD: {
                            if (inFrame.block.position() == 0) {
                                inFrame.blockToRead = param.attrLenLen;
                            } else {
                                int len = 0;
                                boolean wasSpace = true;
                                for (int i = 0; i < inFrame.block.position(); i++) {
                                    int ch = byte2unsignedByte(inFrame.block.get(i));
                                    if (param.param == PAR_LEN) {
                                        if (param.attrLenReverse) {
                                            len |= (ch << (param.attrLenLen - i - 1) * 8);
                                        } else {
                                            len |= (ch << i * 8);
                                        }
                                    } else if (param.param == PAR_LEN_DEC) {
                                        if (ch == ' ') {
                                            if (wasSpace) {
                                                continue;
                                            } else {
                                                throw new InvalidDataException("Unexpected byte in length block at index " + i + ": " + Hex.byte2Hex(ch));
                                            }
                                        }
                                        wasSpace = false;
                                        ch -= '0';
                                        for (int n = 1; n < param.attrLenLen - i; n++) {
                                            ch *= 10;
                                        }
                                        len += ch;
                                    } else if (param.param == PAR_LEN_BCD) {
                                        int mod = 1;
                                        for (int n = 1; n < param.attrLenLen - i; n++) {
                                            mod *= 100;
                                        }
                                        len += ch / 16 * mod * 10 + ch % 16 * mod;
                                    } else if (param.param == PAR_LEN_HEX) {
                                        ch = getHexValueFromAsciiByte(inFrame.block.get(i));
                                        for (int n = 1; n < param.attrLenLen - i; n++) {
                                            ch *= 16;
                                        }
                                        len += ch;
                                    }
                                }
                                if (param.attrRangeStart != -1) {
                                    int nonPacketLen = 0;
                                    boolean packetIsInRange = false;
                                    for (int i = param.attrRangeStart; i <= param.attrRangeEnd; i++) {
                                        final FormatParameter prevParam = inFrame.params.get(i - 1);
                                        if (prevParam.param != PAR_PACKET) {
                                            nonPacketLen += prevParam.getLength();
                                        } else {
                                            packetIsInRange = true;
                                        }
                                    }
                                    if (packetIsInRange) {
                                        inFrame.len = len - nonPacketLen;
                                    }
                                } else {
                                    inFrame.len = len;
                                }
                                Framer.assertFrameLength(inFrame.len, inFrame.getMaxLengthBytes());
                            }
                            break;
                        }
                        case PAR_LRC: {
                            if (inFrame.block.position() == 0) {
                                inFrame.blockToRead = param.getLength();
                            } else {
                                byte lrc = 0;
                                for (int n = param.attrRangeStart; n <= param.attrRangeEnd; n++) {
                                    final ByteBuffer par = params.get(n - 1);
                                    for (int i = 0; i < par.position(); i++) {
                                        lrc ^= par.get(i);
                                    }
                                }
                                final int specifiedLrc;
                                if (param.attrLrcHex) {
                                    specifiedLrc = (getHexValueFromAsciiByte(inFrame.block.get(0)) << 4) + getHexValueFromAsciiByte(inFrame.block.get(1));
                                } else {
                                    specifiedLrc = byte2unsignedByte(inFrame.block.get(0));
                                }
                                if (specifiedLrc != byte2unsignedByte(lrc)) {
                                    inFrame.block.clear();
                                    throw new InvalidDataException("Frame receive error: invalid LRC", null);
                                }
                            }
                            break;
                        }
                        case PAR_STR: {
                            if (inFrame.block.position() == 0) {
                                inFrame.blockToRead = param.str.position();
                            } else if (!startingsEqual(inFrame.block, param.str)) {
                                inFrame.block.clear();
                                throw new InvalidDataException("Frame receive error: no expected " + encodeAsString(param.str), null);
                            }
                            break;
                        }
                        case PAR_HTTP: {
                            final byte[] header = HttpFormatter.readMessage(in.getStream(), headerInfo, inFrame.getMaxLengthBytes());
                            pck = ByteBuffer.wrap(header);
                            pck.position(header.length);
                            break;
                        }
                        default:
                            throw new WrongFormatError("Invalid frame format: unknown parameter " + String.valueOf(param.param), null);
                    }
                    inFrame.block = ByteBuffer.allocate(inFrame.block.capacity());//inFrame.block.clear();
                    if (inFrame.blockToRead == 0) {
                        inFrame.paramsIndex++;
                    }
                }
                pck.flip();//erase(inFrame.blockIndex);
                final byte[] res = new byte[pck.limit()];
                pck.get(res);
                return res;//NOPMD
            }
        } finally {
            inFrame.paramsIndex = 0;
            inFrame.len = -1;

        }
    }

    static private String getHeaderParam(final Map<String, String> headerInfo, final String name, final String defaultValue) {
        if (headerInfo != null) {
            final String val = headerInfo.get(name);
            if (val != null || headerInfo.containsKey(name)) {
                return val;
            }

        }
        return defaultValue;
    }

    static private String getAndRemoveHeaderParam(final Map<String, String> headerInfo, final String name, final String defaultValue) {
        final String result = getHeaderParam(headerInfo, name, defaultValue);
        if (headerInfo != null) {
            headerInfo.remove(name);
        }
        return result;
    }

    public boolean sendFrame(final byte[] pck, final Map<String, String> inputHeaderInfo) throws IOException {
        final Map<String, String> headerInfo = inputHeaderInfo == null ? null : new HashMap<>(inputHeaderInfo);//make a copy for future modification
        final ArrayList<byte[]> blocks = new ArrayList<>();
        int sumLength = 0;

        while (outFrame.paramsIndex < outFrame.params.size()) {
            //if (outFrame.block.position() == 0){
            final FormatParameter param = outFrame.params.get(outFrame.paramsIndex);
            switch (param.param) {
                case PAR_FIELD:
                    outFrame.block = new byte[param.fieldLen];
                    if (inputHeaderInfo != null) {
                        final String fieldHex = inputHeaderInfo.get(param.fieldName);
                        if (fieldHex != null) {
                            byte[] fieldData = Hex.decode(fieldHex);
                            if (fieldData.length != param.fieldLen) {
                                throw new WrongFormatError("Field " + param.fieldName + " actual length " + fieldData.length + " differ from required " + param.fieldLen);
                            }
                            System.arraycopy(fieldData, 0, outFrame.block, 0, param.fieldLen);
                        }
                    }
                    break;
                case PAR_STX:
                    outFrame.block = new byte[]{0x02};
                    break;
                case PAR_ETX:
                    outFrame.block = new byte[]{0x03};
                    break;
                case PAR_PACKET:
                    outFrame.block = pck;
                    break;
                case PAR_LEN: {
                    final int len = getLenForParam(param, pck);
                    outFrame.block = new byte[param.attrLenLen];
                    for (int i = 0; i < param.attrLenLen; i++) {
                        if (param.attrLenReverse) {
                            outFrame.block[i] = ((byte) ((len >> ((param.attrLenLen - 1 - i) * 8)) & 0xFF));
                        } else {
                            outFrame.block[i] = ((byte) ((len >> (i * 8)) & 0xFF));
                        }
                    }
                    break;
                }
                case PAR_LEN_DEC: {
                    String lenAsStr = String.valueOf(getLenForParam(param, pck));
                    if (param.attrLenLen != lenAsStr.length()) {
                        if (lenAsStr.length() > param.attrLenLen) {
                            throw new WrongFormatError("Invalid frame format: package length has " + String.valueOf(lenAsStr.length()) + " digits while frame supports only " + String.valueOf(param.attrLenLen), null);
                        }
                        final StringBuilder fullLenAsStr = new StringBuilder();
                        for (int i = 0; i < param.attrLenLen - lenAsStr.length(); i++) {
                            fullLenAsStr.append('0');
                        }
                        fullLenAsStr.append(lenAsStr);
                        lenAsStr = fullLenAsStr.toString();
                    }
                    byte[] len = lenAsStr.getBytes(StandardCharsets.US_ASCII);
                    outFrame.block = len;
                    break;
                }
                case PAR_LEN_HEX: {
                    String lenAsStr = Integer.toHexString(getLenForParam(param, pck)).toUpperCase();
                    if (param.attrLenLen != lenAsStr.length()) {
                        if (lenAsStr.length() > param.attrLenLen) {
                            throw new WrongFormatError("Invalid frame format: package length has " + String.valueOf(lenAsStr.length()) + " digits while frame supports only " + String.valueOf(param.attrLenLen), null);
                        }
                        final StringBuilder fullLenAsStr = new StringBuilder();
                        for (int i = 0; i < param.attrLenLen - lenAsStr.length(); i++) {
                            fullLenAsStr.append('0');
                        }
                        fullLenAsStr.append(lenAsStr);
                        lenAsStr = fullLenAsStr.toString();
                    }
                    byte[] len = lenAsStr.getBytes(StandardCharsets.US_ASCII);
                    outFrame.block = len;
                    break;
                }
                case PAR_LEN_BCD: {
                    int len = getLenForParam(param, pck);
                    outFrame.block = new byte[param.attrLenLen];
                    for (int i = 0; i < param.attrLenLen; i++) {
                        int mod = 1;
                        for (int n = 1; n < param.attrLenLen - i; n++) {
                            mod *= 100;
                        }
                        outFrame.block[i] = (byte) (len / (mod * 10) % 10 * 16 + len / mod % 10);
                    }
                    break;
                }
                case PAR_LRC: {
                    byte lrc = 0;
                    for (int n = param.attrRangeStart; n <= param.attrRangeEnd; n++) {
                        final byte[] par = blocks.get(n - 1);
                        for (int i = 0; i < par.length; i++) {
                            lrc ^= par[i];
                        }
                    }
                    int unsignedLrc = byte2unsignedByte(lrc);
                    if (param.attrLrcHex) {
                        outFrame.block = new byte[]{(byte) getAsciiByteFromHexValue(unsignedLrc >> 4), (byte) getAsciiByteFromHexValue(unsignedLrc & 0xF)};
                    } else {
                        outFrame.block = new byte[]{(byte) lrc};
                    }
                    break;
                }
                case PAR_SPACE: {
                    break;
                }
                case PAR_STR: {
                    outFrame.block = new byte[param.str.position()];
                    for (int i = 0; i < param.str.position(); i++) {
                        outFrame.block[i] = param.str.get(i);
                    }
                    break;
                }
                case PAR_HTTP: {
                    final byte[] header;
                    if (param.attrRequest) {
                        final String method = getAndRemoveHeaderParam(headerInfo, EHttpParameter.HTTP_REQ_METHOD_PARAM.getValue(), EHttpParameter.HTTP_METHOD_POST.getValue());
                        final String path = getAndRemoveHeaderParam(headerInfo, EHttpParameter.HTTP_REQ_PATH_PARAM.getValue(), "/");
                        final String host = getHeaderParam(headerInfo, EHttpParameter.HTTP_HOST_ATTR.getValue(), DEFAULT_HOST_PARAM);
                        final boolean closeByCloseAttr = Boolean.parseBoolean(getAndRemoveHeaderParam(headerInfo, EHttpParameter.HTTP_CLOSE_PARAM.getValue(), "false"));//deprecated
                        final boolean closeByConnectionAttr = getHeaderParam(headerInfo, EHttpParameter.HTTP_CONNECTION_ATTR.getValue(), "close").equalsIgnoreCase("close");
                        header = HttpFormatter.prepareRequestHeader(method, path, host, !(closeByCloseAttr || closeByConnectionAttr), headerInfo, pck.length).getBytes(StandardCharsets.US_ASCII);
                    } else {
                        final String stat_code = getAndRemoveHeaderParam(headerInfo, EHttpParameter.HTTP_RESP_STATUS_PARAM.getValue(), String.valueOf(HTTP_OK));
                        final String reason_phrase = getAndRemoveHeaderParam(headerInfo, EHttpParameter.HTTP_RESP_REASON_PARAM.getValue(), "OK");
                        final boolean closeByCloseAttr = Boolean.parseBoolean(getAndRemoveHeaderParam(headerInfo, EHttpParameter.HTTP_CLOSE_PARAM.getValue(), "false"));//deprecated
                        final boolean closeByConnectionAttr = getHeaderParam(headerInfo, EHttpParameter.HTTP_CONNECTION_ATTR.getValue(), "close").equalsIgnoreCase("close");
                        header = HttpFormatter.prepareResponseHeader(stat_code + " " + reason_phrase, !(closeByCloseAttr || closeByConnectionAttr), headerInfo, pck.length).getBytes(StandardCharsets.US_ASCII);
                    }
                    out.write(header, 0, header.length);
                    for (int i = 0; i < pck.length; i++) {
                        out.write(pck[i]);
                    }
                    out.flush();
                    break;
                }
                default:
                    throw new WrongFormatError("Invalid frame format: unknown parameter " + String.valueOf(param.param), null);
            }
            //}
            /*
             * MULTICARTA-754 - передавать все одним блоком while
             * (outFrame.blockIndex < outFrame.block.length) {
             * out.write(outFrame.block[outFrame.blockIndex]);
             * outFrame.blockIndex++; }
             */
            sumLength += outFrame.block.length;
            blocks.add(outFrame.block);
            outFrame.block = EMPTY_BLOCK;
            outFrame.paramsIndex++;
        }
        outFrame.paramsIndex = 0;
        //MULTICARTA-754 - передать все одним блоком
        byte[] frame = new byte[sumLength];
        int curPos = 0;
        for (byte[] b : blocks) {
            for (int i = 0; i < b.length; i++) {
                frame[curPos++] = b[i];
            }
        }
        out.write(frame);
        out.flush();
        return true;
    }

    private int getLenForParam(final FormatParameter param, final byte[] pck) {
        int len = 0;
        if (param.attrRangeStart != -1) {
            for (int i = param.attrRangeStart; i <= param.attrRangeEnd; i++) {
                final FormatParameter p = outFrame.params.get(i - 1);
                if (p.param != PAR_PACKET) {
                    len += p.getLength();
                } else {
                    len += pck.length;
                }
            }
        } else {
            len = pck.length;
        }
        return len;
    }

    public static int byte2unsignedByte(final byte b) {
        if (b < 0) {
            return 0x100 + b;
        }
        return b;
    }

    public static int getHexValueFromAsciiByte(final int asciiByte) {
        if (!((('0' <= asciiByte) && (asciiByte <= '9')) || (('A' <= asciiByte) && (asciiByte <= 'F')))) {
            throw new WrongFormatError("ASCII-HEX symbol expected, but got " + asciiByte);
        }
        if (asciiByte <= '9') {
            return asciiByte - '0';
        }
        return asciiByte - 'A' + 10;
    }

    public static int getAsciiByteFromHexValue(final int hexValue) {
        if (hexValue < 10) {
            return '0' + hexValue;
        } else {
            return 'A' + hexValue - 10;
        }
    }
    
    public static void assertFrameLength(int length, int maximum) throws FrameTooLargeException {
        if (length > maximum) {
            throw new FrameTooLargeException(length, maximum);
        }
    }

    /**
     * Compare bytes zero position (inclusive) to current position (exclusive)
     *
     * @param b1
     * @param b2
     * @return
     */
    private static boolean startingsEqual(final ByteBuffer b1, final ByteBuffer b2) {
        if (b1.position() != b2.position()) {
            return false;
        }
        for (int i = 0; i < b1.position(); i++) {
            if (b1.get(i) != b2.get(i)) {
                return false;
            }
        }
        return true;
    }
}
