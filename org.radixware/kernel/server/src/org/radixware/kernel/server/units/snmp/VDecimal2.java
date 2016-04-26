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

package org.radixware.kernel.server.units.snmp;

import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.SMIConstants;


public class VDecimal2 extends Integer32 {

    public static final int MULTIPLIER = 100;
    public static final int NULL = VPositive32.NULL;
    public static final int SYNTAX = SMIConstants.SYNTAX_INTEGER32;

    public VDecimal2(final Double value) {
        if (value == null) {
            setValue(NULL);
        } else {
            setValue((int) Math.round(value * MULTIPLIER));
        }
    }
}
