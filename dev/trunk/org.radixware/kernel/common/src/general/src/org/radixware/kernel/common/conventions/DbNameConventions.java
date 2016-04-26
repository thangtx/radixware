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

package org.radixware.kernel.common.conventions;


public class DbNameConventions {

    public static final String PROP_DEF_ID_DB_COL_NAME = "upDefId";
    public static final String VAL_OWNER_ENTITY_ID_DB_COL_NAME = "upOwnerEntityId";
    public static final String VAL_OWNER_PID_DB_COL_NAME = "upOwnerPid";

    public static boolean isColumnForEntityUsedAsUserPropObject(final String colDbName) {
        return PROP_DEF_ID_DB_COL_NAME.equalsIgnoreCase(colDbName)
                || VAL_OWNER_ENTITY_ID_DB_COL_NAME.equalsIgnoreCase(colDbName) || VAL_OWNER_PID_DB_COL_NAME.equalsIgnoreCase(colDbName);
    }
}
