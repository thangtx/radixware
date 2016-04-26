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

package org.radixware.kernel.common.client.meta;

import org.radixware.kernel.common.types.Id;

/**
 * Base class for explorer definition that has name.
 */
public abstract class NamedDefinition extends Definition {

    private final String name;

    /**
     * Constructs an explorer definition
     * @param id definition`s  unique identifier
     * @param name definition`s  unique name
     */
    public NamedDefinition(final Id id, final String name) {
        super(id);
        this.name = name;
    }

    /**
     * Returns definition`s name.
     * @return definition`s name.
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return (name != null && !name.isEmpty() ? "\"" + name + "\" " : "") + super.toString();
    }
}
