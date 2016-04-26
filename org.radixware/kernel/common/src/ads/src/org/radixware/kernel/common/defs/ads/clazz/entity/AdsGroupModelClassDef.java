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
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;

import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.IdPrefixes;
import org.radixware.schemas.adsdef.ClassDefinition;


public class AdsGroupModelClassDef extends AdsModelClassDef {

    public static class Factory {

        public static AdsGroupModelClassDef loadFrom(AdsSelectorPresentationDef owner, ClassDefinition xClass) {
            return new AdsGroupModelClassDef(owner, xClass);
        }

        public static AdsGroupModelClassDef newInstance(AdsSelectorPresentationDef owner) {
            return new AdsGroupModelClassDef(owner);
        }
    }

    private AdsGroupModelClassDef(AdsSelectorPresentationDef owner, ClassDefinition xDef) {
        super(owner, xDef, EDefinitionIdPrefix.ADS_GROUP_MODEL_CLASS);
    }

    public AdsGroupModelClassDef(AdsSelectorPresentationDef owner) {
        super(owner, EDefinitionIdPrefix.ADS_GROUP_MODEL_CLASS);
    }

    public AdsSelectorPresentationDef getOwnerSelectorPresentation() {
        for (RadixObject owner = getContainer(); owner != null; owner = owner.getContainer()) {
            if (owner instanceof AdsSelectorPresentationDef) {
                return (AdsSelectorPresentationDef) owner;
            }
        }
        return null;
    }

    @Override
    public EClassType getClassDefType() {
        return EClassType.GROUP_MODEL;
    }

    @Override
    public AdsEntityObjectClassDef findServerSideClasDef() {
        return getOwnerSelectorPresentation().getOwnerClass();
    }

    @Override
    public SearchResult<? extends AdsDefinition> findComponentDefinition(Id id) {
        SearchResult<? extends AdsDefinition> result = super.findComponentDefinition(id);
        if (result.isEmpty()) {
            if (IdPrefixes.isAdsPropertyId(id)) {
                AdsSelectorPresentationDef epr = getOwnerSelectorPresentation();
                if (epr != null) {
                    AdsEntityObjectClassDef clazz = epr.getOwnerClass();
                    if (clazz != null) {
                        result = clazz.getProperties().findById(id, EScope.ALL);
                    }
                }
            }

        }
        return result;
    }

    @Override
    public ERuntimeEnvironmentType getClientEnvironment() {
        AdsSelectorPresentationDef spr = getOwnerSelectorPresentation();
        if (spr != null) {
            return spr.getClientEnvironment();
        } else {
            return super.getClientEnvironment();
        }
    }

    @Override
    public boolean isFinal() {
        AdsSelectorPresentationDef spr = getOwnerSelectorPresentation();
        if (spr != null) {
            if (getUsageEnvironment() == ERuntimeEnvironmentType.COMMON_CLIENT) {
                return false;
            } else {
                return spr.isFinal();
            }
        } else {
            return super.isFinal();
        }
    }

    @Override
    public Set<ERuntimeEnvironmentType> getTypeUsageEnvironments() {
        if (getUsageEnvironment() == ERuntimeEnvironmentType.COMMON_CLIENT) {
            return super.getTypeUsageEnvironments();
        } else {
            return EnumSet.of(getUsageEnvironment());
        }
    }

    @Override
    public ERuntimeEnvironmentType getUsageEnvironment() {
        AdsSelectorPresentationDef spr = getOwnerSelectorPresentation();
        if (spr != null) {
            return spr.getClientEnvironment();
        } else {
            return super.getUsageEnvironment();
        }
    }
    
     @Override
    protected void insertToolTipPrefix(StringBuilder sb) {
        super.insertToolTipPrefix(sb);
        String access = getDefaultAccess().getName().toUpperCase().charAt(0) + getDefaultAccess().getName().substring(1, getDefaultAccess().getName().length()); 
        sb.append("<b>").append(access).append(' ').append(getClientEnvironment().getName()).append("</b>&nbsp;");
    }
    
}
