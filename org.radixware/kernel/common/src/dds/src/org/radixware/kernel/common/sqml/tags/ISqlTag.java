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

package org.radixware.kernel.common.sqml.tags;

/**
 * Tag that stored translated SQL.
 * Required to search changed SQML after updating of platform.
 */
public interface ISqlTag {

    /**
     * Get last translated result of tag.
     * @return SQL or null if tag was translated early.
     */
    public String getSql();

    /**
     * Update last translated result of tag by new value.
     */
    public void setSql(final String sql);
}
