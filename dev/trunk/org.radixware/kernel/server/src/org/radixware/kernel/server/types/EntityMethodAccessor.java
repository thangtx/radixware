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

package org.radixware.kernel.server.types;

import org.radixware.kernel.common.enums.EValType;

/**
 * Provides public access to special Entity methods that should not be seen
 * from ADS normally.
 */
public class EntityMethodAccessor {
    
    public static void afterCommit(final Entity entity, boolean isNewObject) {
        entity.afterCommit(isNewObject);
    }
    
    public static boolean propsAreEqual(final EValType valType, final Object val1, final Object val2) {
        return Entity.propsAreEqual(valType, val1, val2);
    }
    
    public static boolean isAfterCommitRequired(final Entity entity) {
        return entity.isAfterCommitRequired();
    }
    
}
