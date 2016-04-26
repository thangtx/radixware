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

package org.radixware.kernel.common.defs.ads.clazz;

import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.schemas.adsdef.ClassDefinition;


public class AdsExceptionClassDef extends AdsDynamicClassDef {

    public static final class Factory{

        public static AdsExceptionClassDef loadFrom(ClassDefinition classDef)  {
            return new AdsExceptionClassDef(classDef);
        }

        public static AdsExceptionClassDef newInstance(ERuntimeEnvironmentType env) {
            return new AdsExceptionClassDef("NewExceptionClass",env);
        }
    }

    protected AdsExceptionClassDef(String name,ERuntimeEnvironmentType env) {
        super(EDefinitionIdPrefix.ADS_EXCEPTION_CLASS, name,env);
    }

    protected AdsExceptionClassDef(ClassDefinition xDef){
        super(xDef);
    }

    protected AdsExceptionClassDef(AdsExceptionClassDef source) {
        super(source);
    }

    @Override
    public EClassType getClassDefType() {
        return EClassType.EXCEPTION;
    }
    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.CLASS_EXCEPTION;
    }
    @Override
    public String getTypeTitle() {
        return environment.getName() + " Exception Class";
    }
}
