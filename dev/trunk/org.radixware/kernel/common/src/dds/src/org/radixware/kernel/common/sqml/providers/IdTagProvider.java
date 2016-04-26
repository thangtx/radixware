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

package org.radixware.kernel.common.sqml.providers;

import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;


class IdTagProvider extends VisitorProvider {

    private final Class<? extends Definition> template;
    private final IFilter<RadixObject> filter;

    public IdTagProvider(Class<? extends Definition> template) {
        this(template, null);
    }

    public IdTagProvider(Class<? extends Definition> template, IFilter<RadixObject> filter) {
        this.template = template;
        this.filter = filter;
    }

    @Override
    public boolean isTarget(RadixObject radixObject) {
        return template.isInstance(radixObject) && (filter == null || (filter != null && filter.isTarget(radixObject)));
    }
}
