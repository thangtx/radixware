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

package org.radixware.kernel.common.msdl.fields.parser.piece;

import org.radixware.kernel.common.msdl.fields.parser.piece.extras.SmioPiecePadded;
import java.nio.charset.CharacterCodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.msdl.fields.parser.SmioFieldInt;
import org.radixware.kernel.common.msdl.fields.parser.SmioField;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.msdl.fields.IntFieldModel;
import org.radixware.kernel.common.exceptions.SmioError;
import org.radixware.kernel.common.exceptions.SmioException;
import org.radixware.kernel.common.msdl.MsdlUnitContext;
import org.radixware.kernel.common.msdl.fields.parser.SmioCoder;
import org.radixware.kernel.common.msdl.fields.parser.datasource.DataSourceByteBuffer;
import org.radixware.kernel.common.msdl.fields.parser.datasource.IDataSource;
import org.radixware.kernel.common.msdl.fields.parser.fieldlist.ExtByteBuffer;
import org.radixware.schemas.msdl.EmbeddedLenDef;
import org.radixware.schemas.msdl.LenUnitDef;
import org.radixware.schemas.types.Int;
import org.radixware.schemas.msdl.AlignDef;
import org.radixware.kernel.common.msdl.fields.parser.piece.extras.PadRemover;

public class SmioPieceEmbeddedLen extends SmioPiecePadded {

    private LenUnitDef.Enum unit;
    private Boolean isSelfInclusive;
    private EmbeddedLenDef embeddedLenDef;
    private SmioFieldInt smioFieldInt;
    private SmioCoder smioCoder;
    private boolean isOddElNeeded = false;
    private boolean isBounded = false;
    private int lowBound, highBound;

    public SmioPieceEmbeddedLen(final SmioField field, final EmbeddedLenDef embeddedLenDef) throws SmioError {
        super(field);
        this.embeddedLenDef = embeddedLenDef;
        if (this.embeddedLenDef.isSetBounds()) {
            isBounded = true;
            lowBound = this.embeddedLenDef.getBounds().getLowBound();
            highBound = this.embeddedLenDef.getBounds().getHighBound();
        }
    }

    protected SmioCoder getCoder() {
        if (smioCoder == null && getUnit() != LenUnitDef.BYTE) {
            String encoding = embeddedLenDef.getEncoding();
            if (encoding == null) {
                encoding = getFieldLen().getModel().getEncoding();
            }
            smioCoder = new SmioCoder(encoding);
        }
        return smioCoder;
    }

    private SmioFieldInt getFieldLen() {
        if (smioFieldInt == null) {
            IntFieldModel modelInt = new IntFieldModel(smioField.getModel().getMsdlField(), embeddedLenDef);
            //modelInt.setName("dummy");
            modelInt.setName("LenField");
            smioFieldInt = (SmioFieldInt) modelInt.getParser();
        }
        return smioFieldInt;
    }

    private boolean getSelfInclusive() {
        if (isSelfInclusive == null) {
            isSelfInclusive = false;
            if (embeddedLenDef.isSetIsSelfInclusive()) {
                isSelfInclusive = embeddedLenDef.getIsSelfInclusive();
            } else {
                isSelfInclusive = smioField.getModel().getSelfInclusive(true);
            }
        }
        return isSelfInclusive;
    }

    @Override
    protected LenUnitDef.Enum getUnit() {
        if (unit == null) {
            if (embeddedLenDef.isSetUnit()) {
                unit = LenUnitDef.Enum.forString(embeddedLenDef.getUnit());
            } else {
                unit = LenUnitDef.Enum.forString(smioField.getModel().getUnit(true, 
                        new MsdlUnitContext(MsdlUnitContext.EContext.EMBEDDED_LEN)));
            }
        }
        return unit;
    }

    @Override
    public IDataSource parse(IDataSource ids) throws SmioException, IOException {
        DataSourceByteBuffer ret= null;
        Int i = Int.Factory.newInstance();
        getFieldLen().parse(i, ids);
        int len = i.getBigIntegerValue().intValue();
        int oldLen = len;
        if (getSelfInclusive()) {
            if (getUnit() == LenUnitDef.CHAR) {
                len -= String.valueOf(len).length();
            } else {
                len -= smioFieldInt.fieldByteLen;
            }
        }
        ExtByteBuffer exbf = new ExtByteBuffer();
        if (getUnit() == LenUnitDef.BYTE) {
            exbf.extPut(ByteBuffer.wrap(ids.get(len)));
        } else if (getUnit() == LenUnitDef.CHAR) {
            String bf = getCoder().decode(ids, len);
            exbf.extPut(ByteBuffer.wrap(getCoder().encode(bf)));
        } else if (getUnit() == LenUnitDef.ELEMENT) {
            if (smioField.getIsBCH() || smioField.getIsBSD()) {
                if (len % 2 == 1) {
                    len += 1;
                    isOddElNeeded = true;
                }
                else
                    isOddElNeeded = false;
                len /= 2;
            } else if (smioField.getIsHex()) {
                len *= 2;
            }

            exbf.extPut(ByteBuffer.wrap(ids.get(len)));            
        }
        ret = new DataSourceByteBuffer(exbf.flip());
        if (embeddedLenDef.isSetBounds()) {
            ret = PadRemover.removePadding(this, ret, oldLen);
        }
        return ret;
    }

    @Override
    public ByteBuffer merge(ByteBuffer bf) throws SmioException {
        ExtByteBuffer exbf = new ExtByteBuffer();
        int len = 0;
        if (getUnit() == LenUnitDef.BYTE) {
            len = bf.remaining();
        }
        if (getUnit() == LenUnitDef.ELEMENT) {
            len = bf.remaining();
            if (smioField.getIsBCH() || smioField.getIsBSD()) {
                len *= 2;
                if (smioField.needOddElementLen()) {
                    len -= 1;
                }
            } else if (smioField.getIsHex()) {
                if (len % 2 == 1) {
                    len += 1;
                }
                len /= 2;
            }
        }
        if (getUnit() == LenUnitDef.CHAR) {
            try {
                len = getCoder().decode(bf).length();
                bf.flip();
            } catch (SmioException ex) {
                Logger.getLogger(SmioPieceEmbeddedLen.class.getName()).log(Level.SEVERE, null, ex);
            } catch (CharacterCodingException ex) {
                Logger.getLogger(SmioPieceEmbeddedLen.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Int intLen = Int.Factory.newInstance();
        Long longLen = new Long(len);
        if (getSelfInclusive()) {
            if (getUnit() == LenUnitDef.CHAR) {
                longLen += longLen.toString().length();
            } else {
                longLen += 1;
                if (longLen > 255) {
                    longLen += 1;
                }
                if (longLen > 65536) {
                    longLen += 1;
                }
            }
        }

        Long wrapper[] = new Long[1];
        wrapper[0] = longLen;
        /*
         * Throw SmioException in case:
         * a) longLen is longer, than highBound
         * b) padCharacter and alignment not set
         */
        ByteBuffer padded = padIfBounded(bf, wrapper);
        intLen.setBigDecimalValue(BigDecimal.valueOf(wrapper[0]));
        ByteBuffer fieldRow = getFieldLen().mergeField(intLen);
        ByteBuffer fieldFormatted = getFieldLen().getPiece().merge(fieldRow);
        exbf.extPut(fieldFormatted);
        exbf.extPut(padded);
        return exbf.flip();
    }

    @Override
    public void check(RadixObject source, IProblemHandler handler) {
        super.check(source, handler);
        if (getUnit() == null) {
            handler.accept(RadixProblem.Factory.newError(source, "MSDL Field '" + source.getQualifiedName() + "' formating error: 'Unit not defined'"));
        }
        if (getUnit() != LenUnitDef.BYTE && getCoder() == null) {
            handler.accept(RadixProblem.Factory.newError(source, "MSDL Field '" + source.getQualifiedName() + "' formating error: 'Encoding not defined (can be used 'Int Default Encoding' from parents also)'"));
        }
    }

    public boolean isOddElementNeeded() {
        return isOddElNeeded;
    }

    /**
     * Pad piece, formatted as embedded length with pad character in case:
     * bounds are set and current length is less, than lower bound
     * @param contents - piece contents, except length piece
     * @param currentLength - current content length, ready to be merged
     * @return padded contents or contents unchanged, if no bounds set or
     * currentLength is between low and high bound
     * @throws SmioException in case:
     * a) currentLength is longer, than high bound
     * b) either pad or alignment is not set
     */
    private ByteBuffer padIfBounded(ByteBuffer contents, Long currentLength[]) throws SmioException {
        ByteBuffer ret = contents;
        if (embeddedLenDef.isSetBounds()) {
            int low = embeddedLenDef.getBounds().getLowBound();
            int high = embeddedLenDef.getBounds().getHighBound();
            int len = currentLength[0].intValue();
            if (currentLength[0] > high) {
                throw new SmioException("Current length is greater than high bound");
            }
            if (len < low) {
                if (!embeddedLenDef.isSetAlign() || !(embeddedLenDef.isSetPadBin() || embeddedLenDef.isSetPadChar())) {
                    throw new SmioException("Cannot merge bounded embedded length piece, because either padding or alignment was not set");
                }
                if (getUnit() == LenUnitDef.CHAR) {
                    ret = insertPadCharacters(ret, low);
                }
                if (getUnit() == LenUnitDef.BYTE || (unit == LenUnitDef.ELEMENT && !smioField.getIsBCH() && !smioField.getIsBSD())) {
                    ret = insertPadBytes(ret, low);
                }
                if (unit == LenUnitDef.ELEMENT && (smioField.getIsBCH() || smioField.getIsBSD())) {
                    if (low % 2 == 1) {
                        low += 1;
                    }
                    int arg = low / 2;
                    ret = insertPadBytes(ret, arg);
                    low = embeddedLenDef.getBounds().getLowBound();
                }
                len = low;
                currentLength[0] = new Long(len);
            }
        }
        return ret;
    }

    @Override
    protected Byte getPadByte() throws SmioException {
        Byte padByte = null;
//        padByte = embeddedLenDef.isSetPadBin() ? embeddedLenDef.getPadBin()[0] : smioField.getModel().getPadBin(true)[0];
        padByte = embeddedLenDef.isSetPadBin() ? embeddedLenDef.getPadBin()[0] : smioField.getModel().getPadBin(true)[0];
        byte[] bin = null;
        
        if(embeddedLenDef.isSetPadBin()) {
            bin = embeddedLenDef.getPadBin();
        }
        else {
            bin = smioField.getModel().getBinPad(true);
        }
        if(bin != null)
                padByte = new Byte(bin[0]);
        
        return padByte;
    }

    @Override
    protected Character getPadChar() throws SmioException {
        Character padChar = null;
        if (padChar == null) {
            if (embeddedLenDef.isSetPadChar()) {
                padChar = embeddedLenDef.getPadChar().charAt(0);
            } else if (smioField.getModel().getPadChar() != null && smioField.getModel().getPadChar().length() > 0) {
                padChar = smioField.getModel().getPadChar().charAt(0);
            } else {
                throw new SmioException("No pad character set!");
            }
        }
        return padChar;
    }

    @Override
    public AlignDef.Enum getAlign() {
        AlignDef.Enum align = null;
        if (align == null) {
            if (embeddedLenDef.isSetAlign()) {
                align = AlignDef.Enum.forString(embeddedLenDef.getAlign());
            } else {
                align = AlignDef.Enum.forString(smioField.getModel().getAlign());
            }
        }
        return align;
    }

    private ExtByteBuffer stripPadding(ExtByteBuffer exbf, int len) throws SmioException, IOException {
        // Extract some methods into SmioPiecePadded from here and SmioPieceFixedLen
        DataSourceByteBuffer dsbf = new DataSourceByteBuffer(exbf.flip());
        ExtByteBuffer ret = exbf;
        if (getUnit() == LenUnitDef.CHAR) {
            final SmioCoder cs = getCoder();
            if (cs == null) {
                throw new SmioException("Encoding not defined");
            }
            
            String data = cs.decode(dsbf, len);

            if (getAlign() == AlignDef.LEFT) {
                final Character ch = getPadChar();
                int last;
                for (last = len - 1; last > -1; last--) {
                    if (data.charAt(last) != ch) {
                        break;
                    }
                }
                data = data.substring(0, last + 1);
            }
            
            if (getAlign() == AlignDef.RIGHT) {
                final Character ch = getPadChar();
                int first;
                for (first = 0; first < len; first++) {
                    if (data.charAt(first) != ch) {
                        break;
                    }
                }
                data = data.substring(first, len);
            }
            ret = new ExtByteBuffer();
            ret.extPut(ByteBuffer.wrap(cs.encode(data)));
        }
        if (getUnit() == LenUnitDef.BYTE || (unit == LenUnitDef.ELEMENT && !smioField.getIsBCH() && !smioField.getIsBSD())) {
            final byte[] data = exbf.flip().array();
            final ByteBuffer res = ByteBuffer.allocate(len);
            final int minLen = smioField.getMinFieldLen();
            if (getAlign() == AlignDef.LEFT) {
                int last = len - 1;
                if (getPadByte() != null) {
                    for (last = len - 1; last > 0; last--) {
                        if (data[last] != getPadByte()) {
                            break;
                        }
                    }
                }
                res.put(data, 0, last + 1);
            }
            if (getAlign() == AlignDef.RIGHT) {
                int first = 0;
                if (getPadByte() != null) {
                    for (first = 0; first < len - minLen; first++) {
                        if (data[first] != getPadByte()) {
                            break;
                        }
                    }
                }
                res.put(data, first, len - first);
            }
            res.flip();
            ret = new ExtByteBuffer();
            ret.extPut(res);
        }
        return ret;
    }
}
