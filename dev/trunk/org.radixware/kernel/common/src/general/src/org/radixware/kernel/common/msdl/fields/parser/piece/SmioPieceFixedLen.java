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
import org.radixware.kernel.common.msdl.fields.parser.SmioField;
import org.radixware.kernel.common.msdl.fields.parser.SmioCoder;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.exceptions.SmioError;
import org.radixware.kernel.common.exceptions.SmioException;
import org.radixware.kernel.common.msdl.MsdlUnitContext;
import org.radixware.kernel.common.msdl.fields.parser.datasource.IDataSource;
import org.radixware.schemas.msdl.AlignDef;
import org.radixware.schemas.msdl.FixedLenDef;
import org.radixware.schemas.msdl.LenUnitDef;
import org.radixware.kernel.common.msdl.fields.parser.piece.extras.PadRemover;

public class SmioPieceFixedLen extends SmioPiecePadded {

    private int len;
    private LenUnitDef.Enum unit;
    private AlignDef.Enum align;
    private Byte padByte;
    private Character padChar;
    private SmioCoder coder;
    private FixedLenDef fixedLen;
    private boolean trimToLengthIfExceed = false;

    public SmioPieceFixedLen(final SmioField field, final FixedLenDef fixedLen) throws SmioError {
        super(field);
        this.fixedLen = fixedLen;
        len = fixedLen.getLen().intValue();
        if(fixedLen.isSetTrimToLengthIfExceed()) {
            trimToLengthIfExceed = fixedLen.getTrimToLengthIfExceed();
        } 
    }
    
    @Override
    protected SmioCoder getCoder() {
        if (coder == null) {
            final String encoding = smioField.getModel().getEncoding();
            if (encoding != null) {
                coder = new SmioCoder(encoding);
            }
        }
        return coder;
    }

    @Override
    protected LenUnitDef.Enum getUnit() {
        if (unit == null) {
            if (fixedLen.isSetUnit()) {
                unit = LenUnitDef.Enum.forString(fixedLen.getUnit());
            } else {
                unit = LenUnitDef.Enum.forString(smioField.getModel().getUnit(true, 
                        new MsdlUnitContext(MsdlUnitContext.EContext.FIXED_LEN)));
            }
        }
        return unit;
    }

    @Override
    public  AlignDef.Enum getAlign() {
        if (align == null) {
            if (fixedLen.isSetAlign()) {
                align = AlignDef.Enum.forString(fixedLen.getAlign());
            } else {
                align = AlignDef.Enum.forString(smioField.getModel().getAlign());
            }
        }
        return align;
    }

    @Override
    protected Byte getPadByte() {
        if (padByte == null) {
            byte [] bin = null;
            if(fixedLen.isSetPadBin()) {
                bin = fixedLen.getPadBin();
            }
            else {
                bin = smioField.getModel().getPadBin();
            }
            if(bin != null)
                padByte = new Byte(bin[0]);
        }
        return padByte;
    }

    @Override
    protected Character getPadChar() throws SmioException {
        if (padChar == null) {
           if(fixedLen.isSetPadChar()) 
                padChar = fixedLen.getPadChar().charAt(0);
            else if (smioField.getModel().getPadChar() != null && smioField.getModel().getPadChar().length() > 0)
                padChar = smioField.getModel().getPadChar().charAt(0);
            else {
                throw new SmioException("No pad character set!");
            }
        }
        return padChar;
    }

    @Override
    public IDataSource parse(final IDataSource ids) throws IOException, SmioException {
        return PadRemover.removePadding(this, ids, len);
    }

    @Override
    public ByteBuffer merge(final ByteBuffer bf) throws SmioException {
        if (getUnit() == LenUnitDef.CHAR) {
            return insertPadCharacters(bf, len, trimToLengthIfExceed);
        }
        if (getUnit() == LenUnitDef.BYTE || (unit == LenUnitDef.ELEMENT && !smioField.getIsBCH() && !smioField.getIsBSD())) {
            return insertPadBytes(bf, len, trimToLengthIfExceed);
        }
        if (unit == LenUnitDef.ELEMENT && (smioField.getIsBCH() || smioField.getIsBSD())) {
            if (len%2==1)
                len+=1;
            return insertPadBytes(bf, len/2, trimToLengthIfExceed);
        }
        return null;
    }
    
    @Override
    public void check(final RadixObject source, final IProblemHandler handler) {
        super.check(source, handler);

        try {
            if (getUnit() == null) {
                handler.accept(RadixProblem.Factory.newError(source, "MSDL Field '" + source.getQualifiedName() + "' formatting error: 'Unit not defined'"));
            }
            if (getAlign() == null) {
                handler.accept(RadixProblem.Factory.newError(source, "MSDL Field '" + source.getQualifiedName() + "' formatting error: 'Align not defined'"));
            }
            if (getUnit() != null && (getUnit() == LenUnitDef.CHAR || getUnit() == LenUnitDef.ELEMENT) && getCoder() == null) {
                handler.accept(RadixProblem.Factory.newError(source, "MSDL Field '" + source.getQualifiedName() + "' formatting error: 'Encoding not defined'"));
            }
//            if ((getUnit() == LenUnitDef.BYTE || getUnit() == LenUnitDef.ELEMENT) && getCoder() != null) {
//                final Enum enc = getCoder().encoding;
//                if (
//                    enc == EncodingDef.ASCII   ||
//                    enc == EncodingDef.CP_1251 ||
//                    enc == EncodingDef.CP_1252 ||
//                    enc == EncodingDef.CP_866  ||
//                    enc == EncodingDef.UTF_8
//                ){
//                    handler.accept(RadixProblem.Factory.newWarning(source, "MSDL Field '" + source.getQualifiedName() + "': 'Length is ambiguous. Use Character unit for length.'"));
//                }
//            }
        } catch (Exception ex) {
            handler.accept(RadixProblem.Factory.newError(source, "MSDL Field '" + source.getQualifiedName() + "' formating error: " + ex.getMessage()));
        }
    }
    
    public int getLen() { return len; }
}
