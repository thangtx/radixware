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


package org.radixware.kernel.designer.api.editors.components;

import org.radixware.kernel.common.defs.IDescribable;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.localization.ILocalizedDescribable;


public final class DescriptionService {

    private DescriptionService() {
    }

    public static DescriptionService getInstance() {
        return DescriptionServiceHolder.INSTANCE;
    }

    public String getDescription(RadixObject object) {
        if (object instanceof ILocalizedDescribable) {
            return AdsDefinition.getLocalizedDescriptionForToolTip((ILocalizedDescribable) object);
        } else if (object instanceof IDescribable) {
            return ((IDescribable) object).getDescription();
        }
        return null;
    }

    private static class DescriptionServiceHolder {

        private static final DescriptionService INSTANCE = new DescriptionService();
    }
}
