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

import java.util.GregorianCalendar;
import org.snmp4j.agent.mo.snmp.DateAndTime;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.SMIConstants;


public class VTimestamp extends OctetString {
    
    public static final int SYNTAX = SMIConstants.SYNTAX_OCTET_STRING;
    
    public VTimestamp(final long timeInMillis) {
        super(convert(timeInMillis));
    }
    
    private static OctetString convert(final long timeInMillis) {
        final GregorianCalendar gc = new GregorianCalendar();
        gc.setTimeInMillis(timeInMillis);
        return DateAndTime.makeDateAndTime(gc);
    }
}
