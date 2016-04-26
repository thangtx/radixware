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

package org.radixware.kernel.common.defs.ads.localization;

import org.radixware.kernel.common.enums.ELocalizedStringKind;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.LocalizedString;


public class AdsDescriptionDef extends AdsMultilingualStringDef {

    public AdsDescriptionDef(AdsMultilingualStringDef src, boolean forOverwrite) {
        super(src, forOverwrite);
    }
    
    public AdsDescriptionDef(AdsMultilingualStringDef src, boolean forOverwrite, boolean useSrcId) {
        super(src, forOverwrite, useSrcId);
    }
    
    public AdsDescriptionDef(LocalizedString xDef) {
        super(xDef);
    }

    public AdsDescriptionDef(Id id) {
        super(id);
    }

    @Override
    public ELocalizedStringKind getSrcKind() {
        return ELocalizedStringKind.DESCRIPTION;
    }
}
