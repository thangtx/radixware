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

public final class RadClassApJoins {

    private final String joinsSql;
    private final String areaListSql;

    protected RadClassApJoins(final String joinsSql, final String areaListSql) {
        super();
        this.joinsSql = joinsSql;
        this.areaListSql = areaListSql;
    }

    /**
     * @return the joinsSql
     */
    public String getJoinsSql() {
        return joinsSql;
    }

    /**
     * @return the areaListSql
     */
    public String getAreaListSql() {
        return areaListSql;
    }
}
