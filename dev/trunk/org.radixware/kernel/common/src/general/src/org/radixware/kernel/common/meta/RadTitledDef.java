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

package org.radixware.kernel.common.meta;

import org.radixware.kernel.common.environment.IRadixEnvironment;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.types.MultilingualString;

public abstract class RadTitledDef extends RadDefinition {
    private final Id     titleId;
	private final Id titleOwnerDefId;
    
    //Constructor
    public RadTitledDef(
    	final Id      id,
    	final String  name, final Id titleOwnerDefId,
    	final Id      titleId
    ) {
        super(id, name);
        this.titleOwnerDefId = titleOwnerDefId;
        this.titleId    = titleId;
    }

    protected final String getTitle(final IRadixEnvironment env) {
    	return MultilingualString.get(env,getTitleOwnerDefId(), getTitleId());
    }

    /**
     * @return the titleId
     */
    public Id getTitleId() {
            return titleId;
    }

    /**
     * @return the ownerDefId
     */
    public Id getTitleOwnerDefId() {
            return titleOwnerDefId;
    }
}
