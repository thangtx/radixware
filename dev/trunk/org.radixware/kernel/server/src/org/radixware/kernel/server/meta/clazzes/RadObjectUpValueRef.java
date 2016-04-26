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

package org.radixware.kernel.server.meta.clazzes;

import org.radixware.kernel.common.conventions.DbNameConventions;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.server.types.Entity;

public final class RadObjectUpValueRef {

    private final Id propertyDefId;
    private final Entity valueOwner;

    public RadObjectUpValueRef(final Entity valueOwner, final Id propertyDefId) {
        this.propertyDefId = propertyDefId;
        this.valueOwner = valueOwner;
        if (this.propertyDefId == null || this.valueOwner == null) {
            throw new IllegalUsageError("Owner and property id is required");
        }
    }

    /**
     * @return the propertyDefId
     */
    public Id getPropertyDefId() {
        return propertyDefId;
    }

    /**
     * @return the valueOwner
     */
    public Entity getValueOwner() {
        return valueOwner;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RadObjectUpValueRef)) {
            return false;
        }
        final RadObjectUpValueRef r = (RadObjectUpValueRef) o;
        return getValueOwner().equals(r.getValueOwner()) && getPropertyDefId().equals(r.getPropertyDefId());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + (this.getPropertyDefId() != null ? this.getPropertyDefId().hashCode() : 0);
        hash = 47 * hash + (this.getValueOwner() != null ? this.getValueOwner().hashCode() : 0);
        return hash;
    }

    public static final class DbNotation {

        public static final String PROP_DEF_ID_DB_COL_NAME = DbNameConventions.PROP_DEF_ID_DB_COL_NAME;
        public static final String VAL_OWNER_ENTITY_ID_DB_COL_NAME = DbNameConventions.VAL_OWNER_ENTITY_ID_DB_COL_NAME;
        public static final String VAL_OWNER_PID_DB_COL_NAME = DbNameConventions.VAL_OWNER_PID_DB_COL_NAME;

        public static final boolean isDbNotationColumn(final String colDbName) {
            return DbNameConventions.isColumnForEntityUsedAsUserPropObject(colDbName);
        }
    }
}
