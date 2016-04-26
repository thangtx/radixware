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

package org.radixware.kernel.common.defs.dds;

import org.radixware.kernel.common.types.Id;

/**
 * Database definition.
 */
public interface IDdsDbDefinition {

    /**
     * Get definition identifier.
     */
    public Id getId();

    /**
     * Returns id sequence containing all container ids from top level definition to current
     */
    public Id[] getIdPath();

    /**
     * Get name of the definition.
     */
    public String getName();

    /**
     * Get database name of the definition.
     */
    public String getDbName();

    /**
     * @return true if object and its owners are generated in database, false otherwise.
     */
    public boolean isGeneratedInDb();
}
