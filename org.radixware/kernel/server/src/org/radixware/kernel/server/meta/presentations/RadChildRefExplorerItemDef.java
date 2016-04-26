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

package org.radixware.kernel.server.meta.presentations;

import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.types.Restrictions;

public final class RadChildRefExplorerItemDef extends RadSelectorExplorerItemDef {

    private final Id childReferenceId;
    private DdsReferenceDef ref = null;

    public RadChildRefExplorerItemDef(
            final Id id,
            final Id ownerDefId,
            final Id classId,
            final Id selectorPresentationId,
            final RadConditionDef condition,
            final Id childReferenceId,
            final Restrictions restrictions,
            final Id classCatalogId) {
        super(id, ownerDefId, classId, selectorPresentationId, condition, restrictions, classCatalogId);
        this.childReferenceId = childReferenceId;
    }

    @Override
    public void link() {
        super.link();
        super.link();
        getChildReference();
    }
    
    public DdsReferenceDef getChildReference() {
        if (ref == null) {
            ref = getRelease().getReferenceDef(getChildReferenceId());
        }
        return ref;
    }

    /**
     * @return the childReferenceId
     */
    public Id getChildReferenceId() {
        return childReferenceId;
    }

    @Override
    public DdsReferenceDef getContextReference() {
        return getChildReference();
    }
}
