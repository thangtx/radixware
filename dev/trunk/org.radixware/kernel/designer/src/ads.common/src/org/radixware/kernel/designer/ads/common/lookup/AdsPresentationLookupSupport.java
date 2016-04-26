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
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.command.AdsCommandDef;


public class AdsPresentationLookupSupport extends AdsCommandsLookupSupport {

    public static final class Factory {

        public static final AdsPresentationLookupSupport newInstance(AdsEditorPresentationDef p) {
            return new AdsPresentationLookupSupport(p);
        }

        public static final AdsPresentationLookupSupport newInstance(AdsSelectorPresentationDef p) {
            return new AdsPresentationLookupSupport(p);
        }
    }
    private AdsPresentationDef presentation;

    private AdsPresentationLookupSupport(AdsPresentationDef p) {
        this.presentation = p;
    }

    @Override
    public Collection<AdsCommandDef> getAvailableCommandList() {
        if (presentation instanceof AdsEditorPresentationDef) {
            return getAvailableCommandList((AdsEditorPresentationDef) presentation);
        } else {
            return getAvailableCommandList((AdsSelectorPresentationDef) presentation);
        }
    }
}
