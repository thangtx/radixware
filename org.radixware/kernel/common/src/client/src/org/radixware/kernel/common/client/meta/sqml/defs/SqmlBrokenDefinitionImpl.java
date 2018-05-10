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

package org.radixware.kernel.common.client.meta.sqml.defs;

import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.meta.sqml.ISqmlBrokenDefinition;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinition;
import org.radixware.kernel.common.types.Id;


final class SqmlBrokenDefinitionImpl implements ISqmlBrokenDefinition {

    private final ISqmlDefinition owner;
    private final Id targetId;
    private final String typeName;

    public SqmlBrokenDefinitionImpl(final ISqmlDefinition ownerDefinition, final Id definitionId, final String typeName) {
        this.typeName = typeName;
        owner = ownerDefinition;
        targetId = definitionId;
    }

    public SqmlBrokenDefinitionImpl(final ISqmlDefinition ownerDefinition, final Id definitionId) {
        this(ownerDefinition,definitionId,null);
    }

    public String getTitle() {
        return getShortName();
    }

    @Override
    public String getDefinitionTypeName() {
        return typeName;
    }

    @Override
    public ISqmlDefinition getOwnerDefinition() {
        return owner;
    }

    @Override
    public String getDisplayableText(final EDefinitionDisplayMode mode) {
        return getShortName();
    }

    @Override
    public String getFullName() {
        return owner==null ? getShortName() : owner.getFullName() + ":" + getShortName();
    }

    @Override
    public ClientIcon getIcon() {
        return null;
    }

    @Override
    public Id getId() {
        return targetId;
    }

    @Override
    public String getShortName() {
        return "#" + targetId.toString();
    }

    @Override
    public String getModuleName() {
        return owner==null ? "" : owner.getModuleName();
    }

    @Override
    public Id[] getIdPath() {
        if (owner==null){
            return new Id[]{targetId};
        }else{
            final Id[] ownerIdPath = owner.getIdPath();
            final Id[] idPath = new Id[ownerIdPath.length+1];
            System.arraycopy(ownerIdPath, 0, idPath, 0, ownerIdPath.length);
            idPath[idPath.length-1]=targetId;
            return idPath;
        }
    }

    @Override
    public boolean isDeprecated() {
        return false;
    }
}
