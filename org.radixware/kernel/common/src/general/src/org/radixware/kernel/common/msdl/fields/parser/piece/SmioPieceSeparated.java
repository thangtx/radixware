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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.msdl.fields.parser.SmioField;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.exceptions.SmioError;
import org.radixware.kernel.common.exceptions.SmioException;
import org.radixware.kernel.common.msdl.fields.parser.datasource.DataSourceByteBuffer;
import org.radixware.kernel.common.msdl.fields.parser.datasource.IDataSource;
import org.radixware.kernel.common.msdl.fields.parser.fieldlist.ExtByteBuffer;
import org.radixware.kernel.common.utils.Hex;
import org.radixware.schemas.msdl.SeparatedDef;

public class SmioPieceSeparated extends SmioPiece {

    private Byte startSep;
    private Byte endSep;
    private Byte shield;
    private ArrayList<Byte> shieldedList;
    private ArrayList<Byte> shieldedListFieldList;
    private Boolean shieldedIsHex;
    private SeparatedDef separatedDef;

    public SmioPieceSeparated(final SmioField field, SeparatedDef separatedDef) throws SmioError {
        super(field);
        this.separatedDef = separatedDef;
    }

    public Byte getShield() {
        if (shield == null) {
            if (separatedDef.isSetShield()) {
                if (separatedDef.getShield().length > 0) {
                    shield = separatedDef.getShield()[0];
                }
            } else {
                byte[] sh = smioField.getModel().getShield(true);
                if (sh != null && sh.length > 0) {
                    shield = sh[0];
                }
            }
        }
        return shield;
    }

    public ArrayList<Byte> getShieldeList() {
        if (shieldedList == null) {
            shieldedList = new ArrayList<Byte>();
            if (getShield() != null) {
                shieldedList.add(shield);
                if (getStartSeparator() != null) {
                    shieldedList.add(startSep);
                }
                if (getEndSeparator() != null) {
                    shieldedList.add(endSep);
                }
                for (byte[] cur : separatedDef.getShieldedList()) {
                    shieldedList.add(cur[0]);
                }
            }
        }
        return shieldedList;
    }

    public ArrayList<Byte> getShieldeListFieldList() {
        if (shieldedListFieldList == null) {
            shieldedListFieldList = new ArrayList<Byte>();
            if (getShield() != null) {
                shieldedListFieldList.add(shield);
                for (byte[] cur : separatedDef.getShieldedList()) {
                    shieldedListFieldList.add(cur[0]);
                }
            }
        }
        return shieldedListFieldList;
    }

    public boolean getShieldedIsHex() {
        if (shieldedIsHex == null) {
            String sh = null;
            if (separatedDef.isSetShieldedFormat()) {
                sh = separatedDef.getShieldedFormat();
            } else {
                sh = smioField.getModel().getShieldedFormat(true);
            }
            shieldedIsHex = SeparatedDef.ShieldedFormat.Enum.forString(sh) == SeparatedDef.ShieldedFormat.HEX;
        }
        return shieldedIsHex;
    }

    private Byte getStartSeparator() {
        if (startSep == null) {
            if (separatedDef.isSetStartSeparator()) {
                if (separatedDef.getStartSeparator().length > 0) {
                    startSep = separatedDef.getStartSeparator()[0];
                }
            } else {
                byte[] st = smioField.getModel().getStartSeparator(true);
                if (st != null && st.length > 0) {
                    startSep = st[0];
                }
            }
        }
        return startSep;
    }

    private Byte getEndSeparator() {
        if (endSep == null) {
            if (separatedDef.isSetEndSeparator()) {
                if (separatedDef.getEndSeparator().length > 0) {
                    endSep = separatedDef.getEndSeparator()[0];
                }
            } else {
                byte[] es = smioField.getModel().getEndSeparator(true);
                if (es != null && es.length > 0) {
                    endSep = es[0];
                }
            }
        }
        return endSep;
    }

    @Override
    public IDataSource parse(IDataSource ids) throws IOException, SmioException {
        ExtByteBuffer exbf = new ExtByteBuffer();
        if (getStartSeparator() != null) {
            while (ids.hasAvailable()) {
                byte b = ids.get();
                if (b == getStartSeparator()) {
                    break;
                }
            }
        }
        while (ids.hasAvailable()) {
            final byte b = ids.get();
            if (getEndSeparator() != null && b == getEndSeparator()) {
                break;
            } else if (getShield() != null && b == getShield() && ids.hasAvailable()) {
                final byte b1 = ids.get();
                if (getShieldedIsHex() && Hex.isHexDigit(b1) && ids.hasAvailable()) {
                    final byte b2 = ids.get();
                    if (Hex.isHexDigit(b2)) {
                        final byte byteFromHex = Hex.decode(CharBuffer.wrap(new char[]{(char) b1, (char) b2}).toString())[0];
                        if (findShielded(byteFromHex)) {
                            exbf.extPut(byteFromHex);
                        } else {
                            exbf.extPut(b);
                            exbf.extPut(b1);
                            exbf.extPut(b2);
                        }
                    } else {
                        exbf.extPut(b);
                        exbf.extPut(b1);
                        exbf.extPut(b2);
                    }
                } else {
                    if (findShielded(b1)) {
                        exbf.extPut(b1);
                    } else {
                        exbf.extPut(b);
                        exbf.extPut(b1);
                    }
                }
            } else {
                exbf.extPut(b);
            }
        }
        return new DataSourceByteBuffer(exbf.flip());
    }

    @Override
    public ByteBuffer merge(ByteBuffer bf) throws SmioException {
        ExtByteBuffer exbf = new ExtByteBuffer();
        boolean isEmpty = bf.remaining() == 0;
        if (getStartSeparator() != null && !isEmpty) {
            exbf.extPut(getStartSeparator());
        }
        while (bf.remaining() > 0) {
            byte b = bf.get();
            if (getShield() != null && findShielded(b)) {
                exbf.extPut(getShield());
                if (getShieldedIsHex()) {
                    exbf.extPut(ByteBuffer.wrap(Hex.encode(new byte[]{b}).getBytes(StandardCharsets.US_ASCII)));
                } else {
                    exbf.extPut(b);
                }
            } else {
                exbf.extPut(b);
            }
        }
        if (getEndSeparator() != null && !isEmpty) {
            exbf.extPut(getEndSeparator());
        }
        return exbf.flip();
    }

    @Override
    public void check(RadixObject source, IProblemHandler handler) {
        super.check(source, handler);
    }

    private boolean findShielded(byte b) {
        for (Byte cur : getShieldeList()) {
            if (cur == b) {
                return true;
            }
        }
        return false;
    }

}
