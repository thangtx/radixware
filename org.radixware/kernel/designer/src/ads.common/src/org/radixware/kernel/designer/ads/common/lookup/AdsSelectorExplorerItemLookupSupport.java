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

import java.util.Collection;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.command.AdsCommandDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsSelectorExplorerItemDef;


public abstract class AdsSelectorExplorerItemLookupSupport extends AdsCommandsLookupSupport {

    /**
     * Returns visitor provider that iterates over available classes
     */
    public abstract VisitorProvider getAvailableClassesProvider();

    /**
     * Returns visitor provider that iterates over available selector presentations
     */
    public abstract VisitorProvider getAvailablePresentationsProvider();

    /**
     * Returns visitor provider that iterates over available class catalogs
     */
    public abstract VisitorProvider getAvailableClassCatalogsProvider();
    protected AdsSelectorExplorerItemDef item = null;

    protected AdsSelectorExplorerItemLookupSupport(AdsSelectorExplorerItemDef item) {
        this.item = item;
    }

    @Override
    public Collection<AdsCommandDef> getAvailableCommandList() {
        return getAvailableCommandList(item);
    }
}
