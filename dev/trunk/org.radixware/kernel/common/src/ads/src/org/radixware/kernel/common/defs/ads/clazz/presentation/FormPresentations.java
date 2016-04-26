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

package org.radixware.kernel.common.defs.ads.clazz.presentation;

import org.radixware.kernel.common.defs.ads.clazz.form.AdsFormHandlerClassDef;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.schemas.adsdef.ClassDefinition;


public class FormPresentations extends AbstractFormPresentations<AdsFormHandlerClassDef> {

    FormPresentations(final AdsFormHandlerClassDef ownerClass, final ClassDefinition.Presentations xDef) {
        super(ownerClass,xDef);
    }

    @Override
    public EDefinitionIdPrefix getCustomViewIdPrefix() {
        return EDefinitionIdPrefix.CUSTOM_FORM_DIALOG;
    }
}
