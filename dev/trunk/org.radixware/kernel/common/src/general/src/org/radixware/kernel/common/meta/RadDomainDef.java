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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.radixware.kernel.common.types.Id;

public final class RadDomainDef extends RadTitledDef{
    private final Id         ownerDomainId;
    private final List<Id>   innerDomainIds;
    
    public RadDomainDef(
        final Id           id,
        final Id           ownerDomainId,
        final Id[]         innerDomainIds,
        final String       name,
        final Id           titleOwnerId,
        final Id           titleId
    ) {
        super(id,name,titleOwnerId, titleId);
        this.ownerDomainId = ownerDomainId;
        if (innerDomainIds == null || innerDomainIds.length == 0){
            this.innerDomainIds = Collections.emptyList();
        } else {
            this.innerDomainIds = Collections.unmodifiableList(Arrays.asList(innerDomainIds));
        }
    }

    public Id getOwnerDomainId() {
        return ownerDomainId;
    }

    public List<Id> getInnerDomainIds() {
        return innerDomainIds;
    }
}
