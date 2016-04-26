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

package org.radixware.kernel.common.defs.ads.ui.rwt;

import org.radixware.kernel.common.defs.ads.clazz.presentation.AbstractFormPresentations;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.schemas.adsdef.AbstractDialogDefinition;


public class AdsRwtCustomFormDialogDef extends AbstractRwtCustomFormDialogDef {

    public static final String PLATFORM_CLASS_NAME_STR = "org.radixware.wps.views.dialog.RwtForm";
    private static final char[] PLATFORM_CLASS_NAME = PLATFORM_CLASS_NAME_STR.toCharArray();

    AdsRwtCustomFormDialogDef(AbstractFormPresentations context) {
        super(context);
    }

    AdsRwtCustomFormDialogDef(AbstractFormPresentations context, AbstractDialogDefinition xDef) {
        super(context, xDef);
    }

    @Override
    public EDefType getDefinitionType() {
        return EDefType.CUSTOM_FORM_EDITOR;
    }

    @Override
    public char[] getSuperClassName() {
        return PLATFORM_CLASS_NAME;
    }
}