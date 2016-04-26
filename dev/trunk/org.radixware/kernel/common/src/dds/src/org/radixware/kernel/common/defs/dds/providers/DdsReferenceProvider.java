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

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.dds.DdsDefinitions;
import org.radixware.kernel.common.defs.dds.DdsModelDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;

/**
 * Visitor prodiver for {@linkplain DdsReferenceDef}.
 */
class DdsReferenceProvider extends DdsModelItemVisitorProvider {

    @Override
    public boolean isTarget(RadixObject object) {
        return (object instanceof DdsReferenceDef);
    }

    @Override
    public boolean isContainer(RadixObject object) {
        if (!super.isContainer(object)) {
            return false;
        }

        if (object instanceof DdsDefinitions) {
            return object == ((DdsModelDef) object.getContainer()).getReferences();
        } else {
            return true;
        }
    }
}
