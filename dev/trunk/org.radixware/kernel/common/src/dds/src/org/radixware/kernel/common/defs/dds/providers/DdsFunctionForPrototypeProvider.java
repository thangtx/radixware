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

package org.radixware.kernel.common.defs.dds.providers;

import java.util.Collections;
import java.util.Set;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.dds.DdsFunctionDef;
import org.radixware.kernel.common.defs.dds.DdsPlSqlObjectDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;

/**
 * Visitor prodiver for {@linkplain DdsReferenceDef}.
 */
class DdsFunctionForPrototypeProvider extends DdsFunctionProvider {

    private final DdsPlSqlObjectDef plSqlObject;

    public DdsFunctionForPrototypeProvider(DdsPlSqlObjectDef plSqlObject) {
        this.plSqlObject = plSqlObject;
    }

    @Override
    public boolean isTarget(RadixObject object) {
        return ((object instanceof DdsFunctionDef) && ((DdsFunctionDef) object).getOwnerPlSqlObject() == plSqlObject);
    }
}
