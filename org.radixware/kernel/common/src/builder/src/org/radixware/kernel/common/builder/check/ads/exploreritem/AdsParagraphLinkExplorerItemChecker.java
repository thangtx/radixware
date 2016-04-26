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

package org.radixware.kernel.common.builder.check.ads.exploreritem;

import org.radixware.kernel.common.builder.check.ads.CheckUtils;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphLinkExplorerItemDef;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphExplorerItemDef;


@RadixObjectCheckerRegistration
public class AdsParagraphLinkExplorerItemChecker extends AdsExplorerItemChecker<AdsParagraphLinkExplorerItemDef> {

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return AdsParagraphLinkExplorerItemDef.class;
    }

    @Override
    public void check(AdsParagraphLinkExplorerItemDef explorerItem, IProblemHandler problemHandler) {
        super.check(explorerItem, problemHandler);
        if (!explorerItem.isReadOnly()) {//See RADIX-1833
            AdsParagraphExplorerItemDef par = explorerItem.findReferencedParagraph();
            if (par == null) {
                error(explorerItem, problemHandler, "Can not find referenced paragraph");
            } else {
                AdsUtils.checkAccessibility(explorerItem, par, false, problemHandler);
                CheckUtils.checkExportedApiDatails(explorerItem, par, problemHandler);
            }
        }
    }
}
