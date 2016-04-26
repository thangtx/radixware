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

package org.radixware.kernel.designer.ads.editors.clazz.simple;

import java.util.List;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.dds.DdsAccessPartitionFamilyDef;
import org.radixware.kernel.common.defs.dds.providers.DdsApfProvider;
import org.radixware.kernel.common.types.Id;


class AccessPartitionFamiliesTunedProvider extends DdsApfProvider{

    private final List<Id> listToExclude;

    public AccessPartitionFamiliesTunedProvider(List<Id> listToExclude) {
        super();
        this.listToExclude = listToExclude;
    }

    @Override
    public boolean isTarget(RadixObject object) {
        return super.isTarget(object) && !listToExclude.contains(((DdsAccessPartitionFamilyDef)object).getId());
    }
}
