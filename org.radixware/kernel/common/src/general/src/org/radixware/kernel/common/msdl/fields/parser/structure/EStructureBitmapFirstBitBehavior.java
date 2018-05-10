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

package org.radixware.kernel.common.msdl.fields.parser.structure;


public enum EStructureBitmapFirstBitBehavior {

    FIELD("Field","Field", false),
    BITMAPS_FIRST("BITMAPS_FIRST", "Bitmaps First", true),
    ISO_8583("ISO_8583", "ISO_8583", true);
    
    private String value,title;
    private final boolean isCountiue;

    EStructureBitmapFirstBitBehavior(String value, String title, boolean isContinue) {
       this.value = value;
       this.title = title;
       this.isCountiue = isContinue;
    }

    public static EStructureBitmapFirstBitBehavior getInstance(String value) {
        if (value == null || value.equals("Continue"))
            return ISO_8583;
        for (EStructureBitmapFirstBitBehavior field : EStructureBitmapFirstBitBehavior.values())
            if (field.value.equals(value))
                return field;
        return ISO_8583;
    }
    
    public boolean isContinue() {
        return isCountiue;
    }

    public String getName() {
        return value;
    }

    @Override
    public String toString() {
        return title;
    }
    
}
