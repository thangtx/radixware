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
import java.util.Collections;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPageDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsExplorerItemDef;


public class AdsEditorPageLookupSupport {

    public Collection<AdsExplorerItemDef> getAvailableEmbeddedxplorerItems(AdsEditorPageDef editorPage) {

        final AdsEditorPresentationDef epr = editorPage.findOwnerEditorPresentation();
        if (epr == null) {
            return Collections.emptySet();
        } else {
            return AdsEditorPresentationLookupSupport.getAvailableEmbeddedExplorerItems(epr, null);
        }
    }
}
