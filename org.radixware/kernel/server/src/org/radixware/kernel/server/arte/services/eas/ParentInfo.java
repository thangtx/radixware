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
package org.radixware.kernel.server.arte.services.eas;

import java.util.EnumSet;
import org.radixware.kernel.common.enums.EReferencedObjectActions;
import org.radixware.kernel.server.exceptions.EntityObjectNotExistsError;

public final class ParentInfo {

    public static final class Factory {

        public static final ParentInfo createBrokenRefInfo(final EntityObjectNotExistsError brokenRefErr, final boolean isSelectable) {
            return new ParentInfo(null, brokenRefErr, null, isSelectable);
        }

        public static final ParentInfo createParentInfo(final String title, final EnumSet<EReferencedObjectActions> actions, final boolean isSelectable) {
            return new ParentInfo(title, null, actions, isSelectable);
        }
    }

    protected ParentInfo(final String title, final EntityObjectNotExistsError brokenRefErr, final EnumSet<EReferencedObjectActions> actions, final boolean isSelectable) {
        this.title = title;
        this.brokenRefErr = brokenRefErr;
        this.isSelectable = isSelectable;
        allowedActions = actions == null ? EnumSet.noneOf(EReferencedObjectActions.class) : EnumSet.copyOf(actions);
    }

    final String title;
    final EntityObjectNotExistsError brokenRefErr;
    final EnumSet<EReferencedObjectActions> allowedActions;
    final boolean isSelectable;

    final boolean isBrokenRef() {
        return brokenRefErr != null;
    }

}
