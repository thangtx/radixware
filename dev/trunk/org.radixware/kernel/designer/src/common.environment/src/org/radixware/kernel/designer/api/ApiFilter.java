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

package org.radixware.kernel.designer.api;

import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.IAccessible;
import org.radixware.kernel.common.enums.EAccess;


public class ApiFilter {

    private EAccess[] accessLevel = {EAccess.PUBLIC, EAccess.PROTECTED};
    private boolean includeNotPublished;

    ApiFilter() {
    }

    public boolean accept(RadixObject object) {
        if (object instanceof Definition) {
            if (!includeNotPublished && !((Definition) object).isPublished()) {
                return false;
            }

            if (object instanceof IAccessible) {
                final EAccess access = ((IAccessible) object).getAccessFlags().getAccessMode();
                return checkAccess(access);
            }

            if (object instanceof AdsDefinition) {
                final EAccess access = ((AdsDefinition) object).getAccessMode();
                return checkAccess(access);
            }
        }
        return true;
    }

    synchronized void setAccessLevel(EAccess... level) {
        this.accessLevel = level;
    }

    synchronized void setShowNotPublished(boolean showNotPublished) {
        this.includeNotPublished = showNotPublished;
    }

    private boolean checkAccess(EAccess access) {

        if (accessLevel == null) {
            return true;
        }

        for (final EAccess a : accessLevel) {
            if (a == access) {
                return true;
            }
        }
        return false;
    }
}
