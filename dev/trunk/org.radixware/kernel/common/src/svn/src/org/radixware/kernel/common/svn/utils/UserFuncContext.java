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
package org.radixware.kernel.common.svn.utils;

import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.types.Id;

/**
 *
 * @author npopov
 */
class UserFuncContext implements IVerifyContext {
    
    private final int id;
    private final Id upOwnerId;
    private final String upOwnerPid;
    private final String upDefId;
    private final Id classGuid;


    public UserFuncContext(int id, String upOwnerId, String upOwnerPid, String upDefId, Id classGuid) {
        this.id = id;
        Id ownerId = Id.Factory.loadFrom(upOwnerId);
        if (ownerId.getPrefix() == EDefinitionIdPrefix.DDS_TABLE) {
            ownerId = Id.Factory.changePrefix(ownerId, EDefinitionIdPrefix.ADS_ENTITY_CLASS);
        }
        this.upOwnerId = ownerId;
        this.upOwnerPid = upOwnerPid;
        this.upDefId = upDefId;
        this.classGuid = classGuid;
    }

    @Override
    public int getKey() {
        return id;
    }

    @Override
    public String getName(IDefinitionNameResolver resolver) {
        final StringBuilder sb = new StringBuilder();
        sb.append("UDF #")
                .append(id)
                .append(' ')
                .append(resolver.resolveName(upOwnerId))
                .append("[")
                .append(upOwnerPid)
                .append("]:")
                .append(resolver.resolveName(classGuid));
        return sb.toString();
    }
    
}
