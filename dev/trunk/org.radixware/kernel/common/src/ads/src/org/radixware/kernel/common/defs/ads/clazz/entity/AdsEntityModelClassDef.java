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
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.IdPrefixes;
import org.radixware.schemas.adsdef.ClassDefinition;


public class AdsEntityModelClassDef extends AdsModelClassDef {

    public static class Factory {

        public static AdsEntityModelClassDef loadFrom(AdsEditorPresentationDef owner, ClassDefinition xClass) {
            return new AdsEntityModelClassDef(owner, xClass);
        }

        public static AdsEntityModelClassDef newInstance(AdsEditorPresentationDef owner) {
            return new AdsEntityModelClassDef(owner);
        }
    }
    private ERuntimeEnvironmentType clientEnv = null;

    private AdsEntityModelClassDef(AdsEditorPresentationDef owner, ClassDefinition xDef) {
        super(owner, xDef, EDefinitionIdPrefix.ADS_ENTITY_MODEL_CLASS);
        if (xDef.isSetClientEnvironment()) {
            this.clientEnv = xDef.getClientEnvironment();
        }
    }

    private AdsEntityModelClassDef(AdsEditorPresentationDef owner) {
        super(owner, EDefinitionIdPrefix.ADS_ENTITY_MODEL_CLASS);
    }

    @Override
    public void appendTo(ClassDefinition xDef, ESaveMode saveMode) {
        super.appendTo(xDef, saveMode);
        if (!isClientEnvLooksLikeInherited()) {
            xDef.setClientEnvironment(clientEnv);
        }
    }

    private boolean isClientEnvLooksLikeInherited() {
        if (clientEnv == null) {
            return true;
        }
        AdsEditorPresentationDef epr = getOwnerEditorPresentation();
        if (epr == null) {
            return false;
        }
        return epr.getClientEnvironment() == clientEnv;
    }

    public AdsEditorPresentationDef getOwnerEditorPresentation() {
        for (RadixObject owner = getContainer(); owner != null; owner = owner.getContainer()) {
            if (owner instanceof AdsEditorPresentationDef) {
                return (AdsEditorPresentationDef) owner;
            }
        }
        return null;
    }

    @Override
    public EClassType getClassDefType() {
        return EClassType.ENTITY_MODEL;
    }

    @Override
    public AdsEntityObjectClassDef findServerSideClasDef() {
        return getOwnerEditorPresentation().getOwnerClass();
    }

    @Override
    public SearchResult<? extends AdsDefinition> findComponentDefinition(Id id) {
        SearchResult<? extends AdsDefinition> result = super.findComponentDefinition(id);
        if (result.isEmpty()) {
            if (IdPrefixes.isAdsPropertyId(id)) {
                AdsEditorPresentationDef epr = getOwnerEditorPresentation();
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

        AdsEditorPresentationDef epr = getOwnerEditorPresentation();
        if (epr != null) {
            AdsEntityObjectClassDef clazz = epr.getOwnerClass();
            if (clazz != null) {
                ERuntimeEnvironmentType clazzEnv = clazz.getClientEnvironment();
                if (clazzEnv != ERuntimeEnvironmentType.COMMON_CLIENT) {
                    return clazzEnv;
                }
            }
            ERuntimeEnvironmentType eprEnv = epr.getClientEnvironment();

            if (clientEnv == null || eprEnv == ERuntimeEnvironmentType.COMMON_CLIENT) {
                return eprEnv;
            }
            return clientEnv;
        } else {
            if (clientEnv != null) {
                return clientEnv;
            }
            return super.getClientEnvironment();
        }
    }

    @Override
    public boolean canChangeClientEnvironment() {
        AdsEditorPresentationDef epr = getOwnerEditorPresentation();
        if (epr != null) {
            AdsEntityObjectClassDef clazz = epr.getOwnerClass();
            if (clazz != null) {
                ERuntimeEnvironmentType clazzEnv = clazz.getClientEnvironment();
                if (clazzEnv != ERuntimeEnvironmentType.COMMON_CLIENT) {
                    return false;
                }
            }
            ERuntimeEnvironmentType eprEnv = epr.getClientEnvironment();

            if (eprEnv == ERuntimeEnvironmentType.COMMON_CLIENT) {
                return false;
            }
            return true;
        } else {
            return true;
        }
    }

    public void setClientEnvironment(ERuntimeEnvironmentType env) {
        if (this.clientEnv != env && canChangeClientEnvironment()) {
            this.clientEnv = env;
            setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    public boolean isFinal() {
        AdsEditorPresentationDef epr = getOwnerEditorPresentation();
        if (epr != null) {
            if (getUsageEnvironment() == ERuntimeEnvironmentType.COMMON_CLIENT) {
                return false;
            } else {
                return epr.isFinal();
            }

        } else {
            return super.isFinal();
        }
    }

    @Override
    public ERuntimeEnvironmentType getUsageEnvironment() {
//        AdsEditorPresentationDef epr = getOwnerEditorPresentation();
////        if (epr != null) {
//            return epr.getClientEnvironment();
//        } else {
//            return super.getUsageEnvironment();
//        }
        return getClientEnvironment();
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
