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

package org.radixware.kernel.common.msdl.enums;


public enum EEncoding {

    NONE("None", "<Not defined>"),
    ASCII("Ascii","ASCII"),
    BCD("Bcd","BCD"),
    BIN("Bin","Bin"),
    CP866("Cp866","Cp866"),
    CP1251("Cp1251","Cp1251"),
    CP1252("Cp1252","Cp1252"),
    EBCDIC("Ebcdic","EBCDIC (IBM500)"),
    EBCDIC_CP1047("Ebcdic_Cp1047", "EBCDIC (Cp1047)"),
    HEXEBCDIC("HexEbcdic","Hex EBCDIC"),
    DECIMAL("Decimal","Decimal"),
    HEX("Hex","Hex"),
    LITTLEENDIANBIN("LittleEndianBin","Little Endian Bin"),
    BIGENDIANBIN("BigEndianBin","Big Endian Bin"),
    UTF8("Utf8","Utf8");

    private String value,title;

    EEncoding(String value, String title) {
       this.value = value;
       this.title = title;
    }
    public String getName() {
        return value;
    }

    public String getValue() {
        if (this == NONE)
            return null;
        else
            return value;
    }
    public static EEncoding getInstance(String value) {
        if (value == null)
            return NONE;
        for (EEncoding field : EEncoding.values())
            if (field.value.equals(value))
                return field;
        return NONE;
    }
        
    public static boolean isElement(EEncoding encoding) {
        return encoding == BCD;
    }

    @Override
    public String toString() {
        return title;
    }
    
}
