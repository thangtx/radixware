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

package org.radixware.kernel.common.defs.ads.type.interfacing;

import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;


public final class RadixClassInterfacingVisitiorProvider extends VisitorProvider {
    private final VisitorProvider provider;
    private final RadixClassInterfacing classInterfacing;

    public RadixClassInterfacingVisitiorProvider(VisitorProvider provider, AdsClassDef base) {
        this.provider = provider;
        this.classInterfacing = new RadixClassInterfacing(base);
    }

    @Override
    public boolean isTarget(RadixObject radixObject) {
        return provider != null && provider.isTarget(radixObject) && classInterfacing.isSuperFor((Definition) radixObject);
    }

}
