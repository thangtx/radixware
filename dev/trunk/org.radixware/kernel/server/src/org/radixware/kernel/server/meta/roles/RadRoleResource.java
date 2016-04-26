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

package org.radixware.kernel.server.meta.roles;

import org.radixware.kernel.common.enums.EDrcResourceType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.types.ModifiableRestrictions;
import org.radixware.kernel.server.types.Restrictions;

public final class RadRoleResource {

    public RadRoleResource(final EDrcResourceType type, final Id defId, final Id subDefId, final Restrictions restrictions) {
        super();
        this.type = type;
        this.defId = defId;
        this.subDefId = subDefId;

        if (restrictions != null) {
            if (restrictions.getIsContextlessUsageRestricted()) {
                //ContextlessUsageRestriction is not used by ACS
                //ignoring it...
                final ModifiableRestrictions r = new ModifiableRestrictions(restrictions);
                r.setContextlessUsageRestricted(false);
                this.restrictions = Restrictions.Factory.newInstance(r);
            } else {
                this.restrictions = restrictions;
            }
        } else {
            this.restrictions = Restrictions.ZERO;
        }
    }
    private final EDrcResourceType type;
    private final Id defId;
    private final Id subDefId;
    private final Restrictions restrictions;

    /**
     * @return the type
     */
    public EDrcResourceType getType() {
        return type;
    }

    /**
     * @return the defId
     */
    public Id getDefId() {
        return defId;
    }

    /**
     * @return the subDefId
     */
    public Id getSubDefId() {
        return subDefId;
    }

    /**
     * @return the restrictions
     */
    public Restrictions getRestrictions() {
        return restrictions;
    }
}
