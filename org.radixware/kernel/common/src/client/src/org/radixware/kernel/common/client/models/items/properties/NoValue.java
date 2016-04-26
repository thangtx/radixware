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

package org.radixware.kernel.common.client.models.items.properties;

import org.radixware.kernel.common.client.meta.RadPropertyDef;
import org.radixware.kernel.common.exceptions.IllegalUsageError;


class NoValue extends PropertyValue {

    public NoValue(final RadPropertyDef propertyDef) {
        super(propertyDef, null);
    }

    @Override
    public void refineValue(Object typifiedValue) {
        throw new IllegalUsageError("Cannot refine value of class NoValue");
    }

    @Override
    public void setOwn(boolean isOwn) {
        throw new IllegalUsageError("Cannot change 'own' attribute for NoValue");
    }

    @Override
    public void setValue(Object newValue) {
        throw new IllegalUsageError("Cannot change value for NoValue");
    }

    @Override
    public boolean hasSameValue(Object compareWith) {
        return compareWith instanceof NoValue;
    }
}
