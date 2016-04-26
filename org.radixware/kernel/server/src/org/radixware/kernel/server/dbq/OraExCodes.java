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

package org.radixware.kernel.server.dbq;


public final class OraExCodes {
	public static final int UNIQUE_CONSTRAINT_VIOLATED = 1;
	public static final int NO_DATA_FOUND = 1403;
	public static final int CANT_SET_LONG_VALUE_IN_NOT_LONG_FIELD = 1461;
	public static final int RESOURCE_REQUEST_TIMEOUT = 30006;
	public static final int RESOURCE_BUSY = 54;
    public static final int WALLET_IS_NOT_OPENED = 28365;
    public static final int WALLET_IS_ALREADY_OPEN = 28354;
    
    public static final int NOT_NULL_CONSTRAINT_VIOLATED = 1400;
    public static final int INTEGRITY_CONSTRAINT_VIOLATED_PARENT_KEY_NOT_FOUND= 2291;
    public static final int INTEGRITY_CONSTRAINT_VIOLATED_CHILD_RECORD_FOUND = 2292;
}
