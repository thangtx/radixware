/*
 * Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */
 
package org.radixware.kernel.utils.wia;

import java.util.EnumSet;

public enum EImageIntent {

    NONE(0x0), 
	COLOR(0x1), 
	GRAYSCALE(0x2), 
	TEXT(0x4), 
	MINIMAZE_SIZE(0x10000), 
	MAXIMIZE_QUALITY(0x20000), 
	BEST_PREVIES(0x40000);
	
    private final long value;

    private EImageIntent(final long val) {
        value = val;
    }

    public long getValue() {
        return value;
    }

    public static long toBitMask(final EnumSet<EImageIntent> intents) {
        long result = 0;
        if (intents != null && !intents.isEmpty()) {
            for (EImageIntent intent : intents) {
                result |= intent.getValue();
            }
        }
        return result;
    }
}
