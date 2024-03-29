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

package org.radixware.kernel.server.meta.clazzes;

import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.value.RadixDefaultValue;
import org.radixware.kernel.common.enums.EValType;

public class RadDynamicPropDef extends RadPropDef {

    public RadDynamicPropDef(
            final Id id,
            final String name,
            final Id titleId,
            final EValType valType,
            final Id constSetId,
            final RadixDefaultValue initVal,
            final IRadPropAccessor accessor) {
        super(id, name, titleId, valType, constSetId, false, null, null, null, initVal, accessor);
    }

    @Override
    public String getDbName() {
        return null;
    }

    @Override
    public String getDbType() {
        return null;
    }

    @Override
    public boolean isGeneratedInDb() {
        return false;
    }
}
