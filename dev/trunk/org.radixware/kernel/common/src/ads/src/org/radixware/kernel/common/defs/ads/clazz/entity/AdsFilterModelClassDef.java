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

package org.radixware.kernel.common.defs.ads.clazz.entity;

import java.util.EnumSet;
import java.util.Set;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsFilterDef;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.schemas.adsdef.ClassDefinition;


public class AdsFilterModelClassDef extends AdsModelClassDef {

    public static final class Factory {

        public static AdsFilterModelClassDef newInstance(AdsFilterDef filter) {
            return new AdsFilterModelClassDef(filter);
        }

        public static AdsFilterModelClassDef loadFrom(AdsFilterDef filter, ClassDefinition xDef) {
            return xDef == null ? new AdsFilterModelClassDef(filter) : new AdsFilterModelClassDef(filter, xDef);
        }
    }

    private AdsFilterModelClassDef(AdsDefinition owner) {
        super(owner, EDefinitionIdPrefix.ADS_FILTER_MODEL_CLASS);
    }

    private AdsFilterModelClassDef(AdsDefinition owner, ClassDefinition xDef) {
        super(owner, xDef, EDefinitionIdPrefix.ADS_FILTER_MODEL_CLASS);
    }

    @Override
    public AdsClassDef findServerSideClasDef() {
        return getOwnerFilterDef().getOwnerClass();
    }

    @Override
    public EClassType getClassDefType() {
        return EClassType.FILTER_MODEL;
    }

    public AdsFilterDef getOwnerFilterDef() {
        return (AdsFilterDef) getContainer();
    }

    @Override
    public ERuntimeEnvironmentType getClientEnvironment() {
        AdsFilterDef filter = getOwnerFilterDef();
        if (filter != null) {
            return filter.getClientEnvironment();
        } else {
            return super.getClientEnvironment();
        }
    }

    @Override
    public ERuntimeEnvironmentType getUsageEnvironment() {
        AdsFilterDef filter = getOwnerFilterDef();
        if (filter != null) {
            return filter.getClientEnvironment();
        } else {
            return super.getUsageEnvironment();
        }
    }

    @Override
    public Set<ERuntimeEnvironmentType> getTypeUsageEnvironments() {
        ERuntimeEnvironmentType env = getClientEnvironment();
        if (env == ERuntimeEnvironmentType.COMMON_CLIENT) {
            return super.getTypeUsageEnvironments();
        } else {
            return EnumSet.of(env);
        }

    }
    
    @Override
    protected void insertToolTipPrefix(StringBuilder sb) {
        super.insertToolTipPrefix(sb);
        String access = getDefaultAccess().getName().toUpperCase().charAt(0) + getDefaultAccess().getName().substring(1, getDefaultAccess().getName().length()); 
        sb.append("<b>").append(access).append(' ').append(getClientEnvironment().getName()).append("</b>&nbsp;");
    }
}