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

package org.radixware.kernel.common.client.models;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.types.Id;


public class FilteredByPropertiesGroupModel extends FilteredGroupModel {

    private final HashMap<Id, Object> filters = new HashMap<Id, Object>(16);

    public FilteredByPropertiesGroupModel(final GroupModel source) {
        super(source);
    }

    public final void setPropertyFilter(final Id propertyId, final Object value) {
        filters.put(propertyId, value);
    }

    public final void setPropertyFilters(final Map<Id, Object> values) {
        filters.clear();
        filters.putAll(values);
    }

    public final void removePropertyFilter(final Id propertyId) {
        filters.remove(propertyId);
    }

    public final void clearPropertyFilters() {
        filters.clear();
    }

    public final Map<Id, Object> getPropertyFilters() {
        return Collections.unmodifiableMap(filters);
    }

    @Override
    protected boolean filterAcceptsEntity(EntityModel entity) {
        for (Map.Entry<Id, Object> entry : filters.entrySet()) {
            if (!valuesEqual(entity.getProperty(entry.getKey()).getValueObject(), entry.getValue())) {
                return false;
            }
        }
        return true;
    }

    private boolean valuesEqual(Object val1, Object val2) {
        return val1 != null ? val1.equals(val2) : val2 == null;
    }
}
