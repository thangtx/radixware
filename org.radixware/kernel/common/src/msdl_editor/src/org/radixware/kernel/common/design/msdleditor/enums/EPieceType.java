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

package org.radixware.kernel.common.design.msdleditor.enums;

import org.radixware.schemas.msdl.Piece;


public enum EPieceType {
    NONE("None"),
    FIXED_LEN("Fixed Length"),
    SEPARATED("Separated"),
    EMBEDDED_LEN("Embedded Length"),
    BERTLV("BerTLV");
    String name;

    EPieceType(String name) {
        this.name = name;
    }

    static public EPieceType getType(Piece piece) {
        if (piece.isSetFixedLen())
            return FIXED_LEN;
        if (piece.isSetSeparated())
            return SEPARATED;
        if (piece.isSetEmbeddedLen())
            return EMBEDDED_LEN;
        if (piece.isSetBerTLV())
            return BERTLV;
        return NONE;
    }

    @Override
    public String toString() {
        return name;
    }
}
