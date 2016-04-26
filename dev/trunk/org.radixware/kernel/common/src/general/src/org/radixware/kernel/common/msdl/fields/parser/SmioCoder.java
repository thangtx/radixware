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

package org.radixware.kernel.common.msdl.fields.parser;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.radixware.kernel.common.exceptions.SmioException;
import org.radixware.kernel.common.msdl.fields.parser.datasource.IDataSource;
import org.radixware.schemas.msdl.EncodingDef;

public class SmioCoder {
    
    private static final Map<String, Charset> CHARSET_CACHE = new ConcurrentHashMap<>();

    private boolean isStandart;
    public EncodingDef.Enum encoding;
    public boolean charIsConstLen;
    public int charLen;
    private final String name;

    private boolean isAsciiLike() {
        if (!isStandart) {
            return false;
        }
        if (encoding == EncodingDef.ASCII || encoding == EncodingDef.CP_1251
                || encoding == EncodingDef.CP_866) {
            return true;
        }
        return false;
    }

    public byte[] encode(final String from) throws SmioException {
        try {
            if (isStandart) {
                CharBuffer fromBuffer = CharBuffer.wrap(from.toCharArray());
                final CharsetEncoder encoder = getCharset(name).newEncoder();
                encoder.onUnmappableCharacter(CodingErrorAction.REPLACE);
                encoder.onMalformedInput(CodingErrorAction.REPLACE);
                byte[] replacement = new byte[1];
                if (isAsciiLike()) {
                    replacement[0] = 0x3F;
                } else {//EBCDIC
                    replacement[0] = 0x6F;
                }
                encoder.replaceWith(replacement);
                return encoder.encode(fromBuffer).array();
            } else {
                Long temp;
                int i;
                byte res[];
                String param;
                if (encoding == EncodingDef.BIG_ENDIAN_BIN) {
                    temp = Long.parseLong(from);
                    i = getHigher8BitCount(temp);
                    res = new byte[i];
                    for (int j = 0; j < i; j++) {
                        res[j] = (byte) ((temp >> ((i - j - 1) * 8)) & 255);
                    }
                    return res;
                } else if (encoding == EncodingDef.LITTLE_ENDIAN_BIN) {
                    temp = Long.parseLong(from);
                    i = getHigher8BitCount(temp);
                    res = new byte[i];
                    for (int j = 0; j < i; j++) {
                        res[j] = (byte) ((temp >> (j * 8)) & 255);
                    }
                    return res;
                } else if (encoding == EncodingDef.BCD) {
                    param = from;
                    if (param.length() % 2 != 0) {
                        param = "0" + param;
                    }
                    res = new byte[param.length() / 2];
                    for (int j = param.length() - 1; j >= 0; j--) {
                        if (j % 2 == 0) {
                            res[j / 2] |= (byte) ((param.charAt(j) - '0') << 4);
                        } else {
                            res[j / 2] |= (byte) ((param.charAt(j) - '0'));
                        }
                    }
                    return res;
                }
                throw new SmioException("Not supported encoding");
            }
        } catch (CharacterCodingException | NumberFormatException | SmioException e) {
            throw new SmioException("Encode error", e);
        }
    }

    static private int getHigher8BitCount(final Long param) {
        int i;
        for (i = 0; i < 8; i++) {
            if (param >> (i * 8) == 0) {
                break;
            }
        }
        if (i == 0) {
            i = 1;
        }
        return i;
    }

    public byte encodeByte(final IDataSource ids) {
        return 0;
    }

    public byte decode(final byte b) {
        return b;
    }
    
    private Charset getCharset(final String name) {
        Charset result = CHARSET_CACHE.get(name);
        if (result == null) {
            result = Charset.forName(name);
        }
        CHARSET_CACHE.put(name, result);
        return result;
    }

    public String decode(final ByteBuffer bf) throws SmioException, CharacterCodingException {
        if (isStandart) {
            final CharsetDecoder decoder = getCharset(name).newDecoder();
            decoder.onUnmappableCharacter(CodingErrorAction.REPLACE);
            decoder.onMalformedInput(CodingErrorAction.REPLACE);
            return decoder.decode(bf).toString();
        } else {
            if (encoding == EncodingDef.BIG_ENDIAN_BIN) {
                Long res = 0L;
                while (bf.remaining() > 0) {
                    int temp = bf.get();
                    if (temp < 0) {
                        temp += 256;
                    }
                    res = res * 256 + temp;
                }
                return res.toString();
            } else if (encoding == EncodingDef.LITTLE_ENDIAN_BIN) {
                Long res = 0L;
                while (bf.remaining() > 0) {
                    int temp = bf.get();
                    if (temp < 0) {
                        temp += 256;
                    }
                    res = res * 256 + temp;
                }
                return res.toString();
            } else if (encoding == EncodingDef.BCD) {
                StringBuilder r = new StringBuilder();
                while (bf.remaining() > 0) {
                    final byte b = bf.get();
                    final int digit1 = (b >> 4) & 15;
                    r = r.append(Integer.toString(digit1));
                    final int digit2 = b & 15;
                    r = r.append(Integer.toString(digit2));
                }
                return r.toString();
            }
            throw new SmioException("Not supported encoding");
        }
    }

    public String decode(final IDataSource ids, final int len) throws SmioException, IOException {
        return decode(ByteBuffer.wrap(ids.get(len)));
    }

    public SmioCoder(final String encodingName) {
        String resultName = encodingName;
        encoding = EncodingDef.Enum.forString(resultName);
        if (encoding == EncodingDef.HEX) {
            isStandart = true;
            charIsConstLen = true;
            charLen = 1;
            resultName = "US-ASCII";
        } else if (encoding == EncodingDef.HEX_EBCDIC) {
            isStandart = true;
            charIsConstLen = true;
            charLen = 1;
            resultName = "Cp1047";
        } else if (encoding == EncodingDef.BCD) {
            isStandart = false;
            charIsConstLen = true;
            charLen = 1;
        } else if (encoding == EncodingDef.BIN) {
            isStandart = false;
            charIsConstLen = true;
            charLen = 1;
        } else if (encoding == EncodingDef.ASCII || encoding == EncodingDef.DECIMAL) {
            isStandart = true;
            charIsConstLen = true;
            charLen = 1;
            resultName = "US-ASCII";
        } else if (encoding == EncodingDef.EBCDIC) {
            isStandart = true;
            charIsConstLen = true;
            resultName = "IBM500";
            charLen = 1;
        } else if (encoding == EncodingDef.UTF_8) {
            isStandart = true;
            charIsConstLen = true;
            charLen = 1;
            resultName = "UTF-8";
        } else if (encoding == EncodingDef.CP_866) {
            isStandart = true;
            charIsConstLen = true;
            charLen = 1;
            resultName = "Cp866";
        } else if (encoding == EncodingDef.CP_1251) {
            isStandart = true;
            charIsConstLen = true;
            charLen = 1;
            resultName = "Cp1251";
        } else if (encoding == EncodingDef.CP_1252) {
            isStandart = false;
            charIsConstLen = true;
            charLen = 1;
        } else if (encoding == EncodingDef.BIG_ENDIAN_BIN) {
            isStandart = false;
            charIsConstLen = true;
            charLen = 4;
        } else if (encoding == EncodingDef.LITTLE_ENDIAN_BIN) {
            charIsConstLen = true;
            charLen = 4;
            isStandart = false;
        } else if (encoding == EncodingDef.DEFAULT_CP) {
            isStandart = false;
            charIsConstLen = true;
            charLen = 1;
            resultName = Charset.defaultCharset().name();
        }
        name = resultName;
    }
}
