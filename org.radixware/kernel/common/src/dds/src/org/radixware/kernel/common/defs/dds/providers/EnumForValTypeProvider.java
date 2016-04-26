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

import org.radixware.kernel.common.defs.IEnumDef;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.enums.EValType;

/**
 * Visitor prodiver for DDS enums, filtered by itemValType.
 */
class EnumForValTypeProvider extends VisitorProvider {

    private final EValType valType;

    public EnumForValTypeProvider(EValType valType) {
        this.valType = (valType != null && valType.isArrayType() ? valType.getArrayItemType() : valType);
    }

    @Override
    public boolean isTarget(RadixObject object) {
        if (object instanceof IEnumDef) {
            final IEnumDef enumDef = (IEnumDef) object;
            return (enumDef.getItemType() == valType);
        } else {
            return false;
        }
    }
}
