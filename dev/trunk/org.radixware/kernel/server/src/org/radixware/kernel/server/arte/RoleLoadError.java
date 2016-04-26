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

package org.radixware.kernel.server.arte;

import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.dbq.DbQueryBuilder;


public class RoleLoadError {

    private final Exception cause;
    private final Id id;
    private final DbQueryBuilder.AppRoleInfo appRoleInfo;

    public RoleLoadError(Id id, DbQueryBuilder.AppRoleInfo appRoleInfo, Exception cause) {
        this.cause = cause;
        this.id = id;
        this.appRoleInfo = appRoleInfo;
    }

    public Exception getCause() {
        return cause;
    }

    public Id getId() {
        return id;
    }

    public DbQueryBuilder.AppRoleInfo getAppRoleInfo() {
        return appRoleInfo;
    }
}
