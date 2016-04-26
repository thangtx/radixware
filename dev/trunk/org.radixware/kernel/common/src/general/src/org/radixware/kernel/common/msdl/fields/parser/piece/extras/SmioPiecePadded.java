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

import java.nio.ByteBuffer;
import org.radixware.kernel.common.exceptions.SmioException;
import org.radixware.schemas.msdl.AlignDef;
import org.radixware.schemas.msdl.LenUnitDef;
import org.radixware.kernel.common.msdl.fields.parser.SmioField;
import org.radixware.kernel.common.msdl.fields.parser.SmioCoder;
import java.nio.charset.CharacterCodingException;
import java.util.logging.Logger;
import java.util.logging.Level;
import org.radixware.kernel.common.msdl.fields.parser.piece.SmioPiece;
import org.radixware.kernel.common.msdl.fields.parser.piece.SmioPieceFixedLen;


public abstract class SmioPiecePadded extends SmioPiece {

    abstract public AlignDef.Enum getAlign();

    abstract protected Character getPadChar() throws SmioException;

    abstract protected Byte getPadByte() throws SmioException;

    abstract protected SmioCoder getCoder();

    abstract protected LenUnitDef.Enum getUnit();

    protected SmioField getField() {
        return smioField;
    }

    public SmioPiecePadded(SmioField smioField) {
        super(smioField);
    }
    
    public ByteBuffer insertPadCharacters(final ByteBuffer bf, int length) throws SmioException {
        return insertPadCharacters(bf, length, false);
    }

    public ByteBuffer insertPadCharacters(final ByteBuffer bf, int length, boolean trimToLengthIfExceed) throws SmioException {
        String data = null;
        final SmioCoder cs = getCoder();
        if (cs == null) {
            throw new SmioException("Encoding not defined");
        }
        try {
            data = cs.decode(bf);
        } catch (CharacterCodingException ex) {
            Logger.getLogger(SmioPieceFixedLen.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (data.length() > length) {
            if (trimToLengthIfExceed) {
                data = data.substring(0, length);
            } else {
                throw new SmioException("Piece contains MORE characters, than specified in its size");
            }
        }
        if (getAlign() == AlignDef.NONE) {
            if (data.length() < length) {
                throw new SmioException("NONE-aligned piece contains LESS characters, than specified in its size");
            }
        } else {
            final StringBuilder sb = new StringBuilder();
            if (getAlign() == AlignDef.LEFT) {
                sb.append(data);
            }
            for (int i = 0; i < length - data.length(); i++) {
                sb.append(getPadChar());
            }
            if (getAlign() == AlignDef.RIGHT) {
                sb.append(data);
            }
            data = sb.toString();
        }
        return ByteBuffer.wrap(cs.encode(data));
    }
    
    public ByteBuffer insertPadBytes(final ByteBuffer bf, int length) throws SmioException {
        return insertPadBytes(bf, length, false);
    }

    public ByteBuffer insertPadBytes(final ByteBuffer bf, int length, boolean trimToLengthIfExceed) throws SmioException {
        final ByteBuffer res = ByteBuffer.allocate(length);
        if (bf.remaining() > length) {
            if (trimToLengthIfExceed) {
                bf.limit(bf.limit() - (bf.remaining() - length));
            } else {
                throw new SmioException("Piece contains MORE bytes, than specified in its size");
            }
        }
        final int r = bf.remaining();
        if (getAlign() == AlignDef.NONE) {
            if (r < length) {
                throw new SmioException("NONE-aligned piece contains LESS bytes, than specified in its size");
            }
            res.put(bf);
        } else {
            Byte pad = getPadByte();
            if (pad != null) {
                if (getAlign() == AlignDef.LEFT) {
                    res.put(bf);
                }
                for (int i = 0; i < length - r; i++) {
                    res.put(pad);
                }
                if (getAlign() == AlignDef.RIGHT) {
                    res.put(bf);
                }
            } else {
                res.put(bf);
            }
        }
        res.flip();
        return res;
    }
}
