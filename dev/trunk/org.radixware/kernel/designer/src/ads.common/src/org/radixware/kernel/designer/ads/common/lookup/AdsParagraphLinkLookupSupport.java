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

package org.radixware.kernel.designer.ads.common.lookup;

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProvider;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphLinkExplorerItemDef;


public class AdsParagraphLinkLookupSupport {

    public static final class Factory {

        public static AdsParagraphLinkLookupSupport newInstance(AdsParagraphLinkExplorerItemDef link) {
            return new AdsParagraphLinkLookupSupport(link);
        }

        public static AdsParagraphLinkLookupSupport newInstance(AdsParagraphExplorerItemDef owner) {
            return new AdsParagraphLinkLookupSupport(null);
        }
    }

    AdsParagraphLinkLookupSupport(AdsParagraphLinkExplorerItemDef item) {
    }

    public VisitorProvider getAvailableParagraphProvider() {
        return new AdsVisitorProvider() {

            @Override
            public boolean isTarget(RadixObject object) {
                return object instanceof AdsParagraphExplorerItemDef;
            }
        };
    }
}
