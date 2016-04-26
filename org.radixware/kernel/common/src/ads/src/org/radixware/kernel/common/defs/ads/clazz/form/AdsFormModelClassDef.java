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

package org.radixware.kernel.common.defs.ads.clazz.form;

import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AbstractFormModelClassDef;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.IdPrefixes;
import org.radixware.schemas.adsdef.ClassDefinition;


public class AdsFormModelClassDef extends AbstractFormModelClassDef<AdsFormHandlerClassDef> {

    public static final class Factory {

        private Factory() {
            super();
        }

        public static AdsFormModelClassDef loadFrom(final AdsFormHandlerClassDef owner, final ClassDefinition xDef) {
            return new AdsFormModelClassDef(owner, xDef);
        }

        public static AdsFormModelClassDef newInstance(final AdsFormHandlerClassDef owner) {
            return new AdsFormModelClassDef(owner);
        }
    }

    private AdsFormModelClassDef(final AdsFormHandlerClassDef owner, final ClassDefinition xDef) {
        super(owner, xDef, EDefinitionIdPrefix.ADS_FORM_MODEL_CLASS);
    }

    private AdsFormModelClassDef(final AdsFormHandlerClassDef owner) {
        super(owner, EDefinitionIdPrefix.ADS_FORM_MODEL_CLASS);
    }

    @Override
    public EClassType getClassDefType() {
        return EClassType.FORM_MODEL;
    }

    @Override
    public SearchResult<? extends AdsDefinition> findComponentDefinition(Id id) {
        SearchResult<? extends AdsDefinition> result = super.findComponentDefinition(id);
        if (result.isEmpty()) {
            if (IdPrefixes.isAdsPropertyId(id)) {
                AdsFormHandlerClassDef clazz = getOwnerClass();
                if (clazz != null) {
                    result = clazz.getProperties().findById(id, EScope.ALL);
                }
            }
        }
        return result;
    }
}
