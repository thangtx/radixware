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

import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.meta.clazzes.IRadPropReadAccessor;
import org.radixware.kernel.server.meta.clazzes.IRadPropWriteAccessor;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;

/**
 * Base class for details subobject
 *

 */
public final class EntityDetails extends Entity {

    @Override
    public DdsTableDef getDdsMeta() {
        return ddsMeta;
    }

    @Override
    public RadClassDef getRadMeta() {
        return radMeta;
    }
    private final DdsTableDef ddsMeta;
    private final RadClassDef radMeta;

    EntityDetails(final Arte arte, final DdsReferenceDef mdRef) {
        super(arte, true);
        ddsMeta = arte.getDefManager().getTableDef(mdRef.getChildTableId());
        radMeta = generateRadMeta(mdRef);
    }

    private RadClassDef generateRadMeta(final DdsReferenceDef mdRef) {
        return getArte().getDefManager().getDetailRadMeta(mdRef);
    }

    public static final class PropAccessor implements IRadPropReadAccessor, IRadPropWriteAccessor {

        private final Id propId;

        public PropAccessor(final Id propId) {
            this.propId = propId;
        }

        @Override
        public final Object get(final Object owner, Id propId) {
            return ((Entity) owner).getNativeProp(propId);
        }

        @Override
        public final void set(final Object owner, Id propId, final Object val) {
            ((Entity) owner).setNativeProp(propId, val);
        }
    }
}
