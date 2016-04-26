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

package org.radixware.kernel.common.defs.ads.ui;

import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.schemas.adsdef.ClassDefinition;


public class AdsPropEditorModelClassDef extends AdsDialogModelClassDef {

    AdsPropEditorModelClassDef(AdsDefinition owner, ClassDefinition xDef) {
        super(owner, xDef, EDefinitionIdPrefix.ADS_PROP_EDITOR_MODEL_CLASS);
    }

    AdsPropEditorModelClassDef(AdsDefinition owner) {
        super(owner, EDefinitionIdPrefix.ADS_PROP_EDITOR_MODEL_CLASS);
    }

    @Override
    public EClassType getClassDefType() {
        return EClassType.PROP_EDITOR_MODEL;
    }
}
