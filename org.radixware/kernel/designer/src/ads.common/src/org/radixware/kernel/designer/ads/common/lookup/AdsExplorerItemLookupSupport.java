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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.command.AdsCommandDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphLinkExplorerItemDef;


public abstract class AdsExplorerItemLookupSupport {

    public interface IFilter {

        boolean accept(AdsExplorerItemDef explorerItem);
    }

    /**
     * Return list of available commands
     */
    public Collection<AdsCommandDef> getAvailableCommandList() {
        return Collections.emptyList();
    }

    public static Collection<AdsExplorerItemDef> getAvailableEmbeddedExplorerItems(Collection<AdsExplorerItemDef> root, final IFilter filter) {

        final ArrayList<AdsExplorerItemDef> result = new ArrayList<>();

        for (final AdsExplorerItemDef ei : root) {
            ei.visit(new IVisitor() {
                @Override
                public void accept(RadixObject radixObject) {
                    if (filter == null || filter.accept((AdsExplorerItemDef) radixObject)) {
                        result.add((AdsExplorerItemDef) radixObject);
                    }
                }
            }, new VisitorProvider() {
                @Override
                public boolean isTarget(RadixObject object) {
                    return object instanceof AdsExplorerItemDef
                            && !(object instanceof AdsParagraphExplorerItemDef)
                            && !(object instanceof AdsParagraphLinkExplorerItemDef);//AdsSelectorExplorerItemDef;
                }
            });
        }
        return result;
    }
}
