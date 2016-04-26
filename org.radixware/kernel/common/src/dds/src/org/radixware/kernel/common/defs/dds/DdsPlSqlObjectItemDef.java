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

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.schemas.commondef.Definition;
import org.radixware.schemas.commondef.DescribedDefinition;
import org.radixware.schemas.commondef.NamedDefinition;

/**
 * Metainformation about {@link DdsPlSqlObjectDef} item.
 */
public abstract class DdsPlSqlObjectItemDef extends DdsDefinition {

    protected DdsPlSqlObjectItemDef(final EDefinitionIdPrefix idPrefix, final String name) {
        super(idPrefix, name);
    }

    public DdsPlSqlObjectItemDef(DescribedDefinition xDescribedDefinition) {
        super(xDescribedDefinition);
    }

    public DdsPlSqlObjectItemDef(NamedDefinition xNamedDefinition) {
        super(xNamedDefinition);
    }

    public DdsPlSqlObjectItemDef(Definition xDefinition) {
        super(xDefinition);
    }

    /**
     * Get owner {@link DdsPlSqlObjectDef}.
     */
    public DdsPlSqlObjectDef getOwnerPlSqlObject() {
        for (RadixObject radixObject = getContainer(); radixObject != null; radixObject = radixObject.getContainer()) {
            if (radixObject instanceof DdsPlSqlObjectDef) {
                return (DdsPlSqlObjectDef) radixObject;
            }
        }
        return null;
    }
}
