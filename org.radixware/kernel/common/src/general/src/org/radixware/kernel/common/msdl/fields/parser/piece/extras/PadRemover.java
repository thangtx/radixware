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

package org.radixware.kernel.common.msdl.fields.parser.piece.extras;

import java.io.IOException;
import org.radixware.schemas.msdl.AlignDef;
import org.radixware.schemas.msdl.LenUnitDef;
import org.radixware.kernel.common.msdl.fields.parser.datasource.IDataSource;
import org.radixware.kernel.common.msdl.fields.parser.datasource.DataSourceByteBuffer;
import org.radixware.kernel.common.msdl.fields.parser.SmioCoder;
import org.radixware.kernel.common.exceptions.SmioException;
import java.nio.ByteBuffer;

/**
 * Small utility class, able to remove padding from FixedLen and bounded 
 * EmbeddedLen pieces while parsing. Implements Strategy pattern in a crooked way
 */
public abstract class PadRemover {

    public static DataSourceByteBuffer removePadding(SmioPiecePadded pp, IDataSource ids, int len) throws SmioException, IOException {
        if (pp.getUnit() == LenUnitDef.CHAR) {
            return new CharPadRemover(pp).removePadding(ids, len);
        }
        if (pp.getUnit() == LenUnitDef.BYTE || (pp.getUnit() == LenUnitDef.ELEMENT && !pp.getField().getIsBCH() && !pp.getField().getIsBSD())) {
            return new BytePadRemover(pp).removePadding(ids, len);
        }
        if (pp.getUnit() == LenUnitDef.ELEMENT && (pp.getField().getIsBCH() || pp.getField().getIsBSD())) {
            return new ElementPadRemover(pp).removePadding(ids, len);
        }
        throw new SmioException("Units not defined for piece");
    }

    abstract protected DataSourceByteBuffer removePadding(IDataSource ids, int len) throws SmioException, IOException;
    protected SmioPiecePadded piece = null;

    protected PadRemover(SmioPiecePadded pp) {
        piece = pp;
    }
}

class CharPadRemover extends PadRemover {

    protected CharPadRemover(SmioPiecePadded pp) {
        super(pp);

    }

    @Override
    protected DataSourceByteBuffer removePadding(IDataSource ids, int len) throws SmioException, IOException {
        DataSourceByteBuffer ret = null;
        final SmioCoder cs = piece.getCoder();
        if (cs == null) {
            throw new SmioException("Encoding not defined");
        }
        if (ids.available() < len) {
            throw new SmioException("Not enough data for field");
        }
        String data = cs.decode(ids, len);
        if (piece.getAlign() == AlignDef.NONE) {
            if (data.length() < len) {
                throw new SmioException("Field length is too small");
            }
        } else {
            int minLen = piece.getField().getChildrenLen();
            if (piece.getAlign() == AlignDef.LEFT) {
                if (minLen < len) {
                    final Character ch = piece.getPadChar();
                    int last;
                    for (last = len - 1; last > minLen - 1; last--) {
                        if (data.charAt(last) != ch) {
                            break;
                        }
                    }
                    data = data.substring(0, last + 1);
                }
            }
            if (piece.getAlign() == AlignDef.RIGHT) {
                if (minLen < len) {
                    final Character ch = piece.getPadChar();
                    int first;
                    for (first = 0; first < len - minLen; first++) {
                        if (data.charAt(first) != ch) {
                            break;
                        }
                    }
                    data = data.substring(first, len);
                }
            }
        }
        ret = new DataSourceByteBuffer(cs.encode(data));
        return ret;
    }
}

class BytePadRemover extends PadRemover {

    protected BytePadRemover(SmioPiecePadded pp) {
        super(pp);
    }

    @Override
    protected DataSourceByteBuffer removePadding(IDataSource ids, int len) throws SmioException, IOException {
        DataSourceByteBuffer ret = null;
        if (ids.available() < len) {
            throw new SmioException("Not enough data for field");
        }
        if (piece.getAlign() == null) {
            throw new SmioException("Alignment not set");
        }
        final byte[] data = ids.get(len);
        final ByteBuffer res = ByteBuffer.allocate(len);
        if (piece.getAlign() == AlignDef.NONE) {
            if (data.length != len) {
                throw new SmioException("Wrong field length");
            }
            res.put(data);
        }
        else {
            if (piece.getPadByte() != null) {
                final int minLen = piece.getField().getMinFieldLen();
                if (piece.getAlign() == AlignDef.LEFT) {
                    int last;
                    for (last = len - 1; last >= minLen; last--) {
                        if (data[last] != piece.getPadByte()) {
                            break;
                        }
                    }
                    res.put(data, 0, last + 1);
                }
                if (piece.getAlign() == AlignDef.RIGHT) {
                    int first;
                    for (first = 0; first < len - minLen; first++) {
                        if (data[first] != piece.getPadByte()) {
                            break;
                        }
                    }
                    res.put(data, first, len - first);
                }
            }
            else { //result is everything we have
                res.put(data);
            }
        }
        //res.position(0);
        res.flip();
        ret = new DataSourceByteBuffer(res);
        return ret;
    }
}

class ElementPadRemover extends PadRemover {

    protected ElementPadRemover(SmioPiecePadded pp) {
        super(pp);
    }

    @Override
    protected DataSourceByteBuffer removePadding(IDataSource ids, int len) throws SmioException, IOException {
        //WARNING: BCD and BCH-encoded values are always considered packed, according to A. Kaptsan
        DataSourceByteBuffer ret = null;
        if (len % 2 == 1) {
            len += 1;
        }
        if (ids.available() < len / 2) {
            throw new SmioException("Not enough data for field");
        }
        byte[] data = ids.get(len / 2);
        ByteBuffer res = ByteBuffer.allocate(len / 2);
        if (piece.getAlign() == AlignDef.NONE) {
            if (data.length != len / 2) {
                throw new SmioException("Wrong field length");
            }
            res.put(data);
        }
        if(piece.getPadByte() != null) {
            int minLen = piece.getField().getMinFieldLen();
            if (piece.getAlign() == AlignDef.LEFT) {
                byte pad = piece.getPadByte();
                int lastByteIdx;
                for (lastByteIdx = len / 2 - 1; lastByteIdx >= minLen; lastByteIdx--) {
//                    if (data[lastByteIdx] != pad) {
//                        break;
//                    }
                    byte b = data[lastByteIdx];
                    if ((b & 0x0F) != pad)
                        break;
                    if ((b & 0xF0)>>4 != pad) {
                        data[lastByteIdx] &= 0xF0;
                        break;
                    }

                    
                    //first check, if high 4 bits are equal to padding
                    //if so, there is no need to check low 4 bits
                    //if it is not the case, 
//                    byte read = data[lastByteIdx];
//                    byte highBits = (read & 0xF0);
//                    if(highBits == pad)
//                        break;
//                    byte lowBits = (read & 0x0F);
//                    if(lowBits == pad)
                       
                }
                res.put(data, 0, lastByteIdx + 1);
            }
            if (piece.getAlign() == AlignDef.RIGHT) {
                byte pad = piece.getPadByte();
                boolean padMode = true;
                int byteCount = 0;
                for (byteCount = 0; byteCount < len / 2 - minLen; byteCount++) {
                    final byte b = data[byteCount];
                    if (padMode) {
//                        if (b != pad) {
//                            padMode = false;
//                            res.put(b);
//                        }
                        
                        /* If we have packed BCH or BCD, check, if high or low  
                         4 bits contain value*/
                        if ((b & 0xF0)>>4 != pad) {
                            padMode = false;
                            res.put(b);
                            continue;
                        }    
                        if ((b & 0x0F) != pad) {
                            padMode = false;
                            res.put((byte)(b & 0x0F));
                        }


                    } else {
                        res.put(b);
                    }
                    
                }
                res.put(data, byteCount, len / 2 - byteCount);
            }
        }
        else {
            res.put(data);
        }
        //res.position(0);
        res.flip();
        ret = new DataSourceByteBuffer(res);
        return ret;
    }
}
