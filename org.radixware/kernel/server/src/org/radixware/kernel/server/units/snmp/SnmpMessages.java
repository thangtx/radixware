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

import org.radixware.kernel.server.utils.MessagesHelper;


public class SnmpMessages {

    public static String SNMP_AGENT_UNIT_TYPE_TITLE;
    
    static {
        MessagesHelper.initialize(SnmpMessages.class, SnmpMessages.class.getPackage().getName() + ".messages");
    }
}
