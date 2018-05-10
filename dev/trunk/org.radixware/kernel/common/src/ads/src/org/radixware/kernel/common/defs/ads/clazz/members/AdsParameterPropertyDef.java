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

package org.radixware.kernel.common.defs.ads.clazz.members;

import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IParameterDef;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsProcedureClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsSqlClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumUtils;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.type.AdsClassType.EntityObjectType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.ArrayType;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EIfParamTagOperator;
import org.radixware.kernel.common.enums.EParamDirection;
import org.radixware.kernel.common.enums.EPropNature;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.sqml.tags.IfParamTag;
import org.radixware.kernel.common.sqml.tags.ParameterTag;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.schemas.adsdef.AbstractPropertyDefinition;


public class AdsParameterPropertyDef extends AdsDynamicPropertyDef {

    public static final class Factory {

        private Factory() {
            super();
        }

        public static AdsParameterPropertyDef newInstance() {
            return new AdsParameterPropertyDef("newParameter");
        }

        @Deprecated // for upgrade only
        public static AdsParameterPropertyDef newInstance(final Id id, final String name) {
            return new AdsParameterPropertyDef(id, name);
        }

        public static AdsParameterPropertyDef newTemporaryInstance(final RadixObject container) {
            final AdsParameterPropertyDef prop = newInstance();
            prop.setContainer(container);
            return prop;
        }
    }

    AdsParameterPropertyDef(final String name) {
        super(EDefinitionIdPrefix.PARAMETER, name);
        getAccessFlags().setPublic();
    }

    @Deprecated // for upgrade only
    AdsParameterPropertyDef(final Id id, final String name) {
        super(id, name);
        getAccessFlags().setPublic();
    }

    AdsParameterPropertyDef(final AdsParameterPropertyDef src, final boolean forOvr) {
        super(src, forOvr);
    }

    AdsParameterPropertyDef(final AbstractPropertyDefinition xProp) {
        super(null, xProp);
    }

    @Override
    public AdsSqlClassDef getOwnerClass() {
        return (AdsSqlClassDef) super.getOwnerClass();
    }

    @Override
    public EPropNature getNature() {
        return EPropNature.SQL_CLASS_PARAMETER;
    }

    @Override
    protected AdsPropertyDef createOvr(final boolean forOverride) {
        return new AdsParameterPropertyDef(this, forOverride);
    }

    public EParamDirection calcDirection() {
        final AdsSqlClassDef sqlClass = getOwnerClass();

        if (!(sqlClass instanceof AdsProcedureClassDef)) {
            return EParamDirection.IN;
        }

        EParamDirection direction = null;

        for (Sqml.Item item : sqlClass.getSqml().getItems()) {
            if (item instanceof ParameterTag) {
                final ParameterTag paramTag = (ParameterTag) item;
                if (Utils.equals(this.getId(), paramTag.getParameterId())) {
                    if (direction == null) {
                        direction = paramTag.getDirection();
                    } else if (direction != paramTag.getDirection()) {
                        direction = EParamDirection.BOTH;
                    }
                }
            }
        }

        return (direction != null ? direction : EParamDirection.IN);
    }

    public static void printTagCondition(final CodePrinter cp, final IfParamTag ifParamTag) {
        AdsUtils.printTagCondition(cp, ifParamTag);
    }

    @Override
    public ServerPresentationSupport getPresentationSupport() {
        final AdsSqlClassDef ownerClass = getOwnerClass();
        if (ownerClass == null || ownerClass instanceof AdsReportClassDef) {
            return super.getPresentationSupport();
        } else {
            return null;
        }
    }

    public static boolean canBeLiteral(AdsDynamicPropertyDef property) {
        return property.getValue().getType().getTypeId() == EValType.STR;
    }

    public static boolean canBeOutput(AdsDynamicPropertyDef property) {
        final AdsClassDef sqlClass = property.getOwnerClass();
        if (!(sqlClass instanceof AdsProcedureClassDef)) {
            return false;
        }

        final EValType valType = property.getValue().getType().getTypeId();
        return (valType != EValType.PARENT_REF && !valType.isArrayType());
    }

    public static AdsEntityObjectClassDef findEntity(AdsDynamicPropertyDef property) {
        final AdsTypeDeclaration typeDeclaration = property.getValue().getType();
        final EValType valType = typeDeclaration.getTypeId();
        if (valType == EValType.PARENT_REF || valType == EValType.ARR_REF) {
            AdsType type = typeDeclaration.resolve(property).get();
            if (type instanceof ArrayType) {
                final ArrayType arrType = (ArrayType) type;
                type = arrType.getItemType();
            }
            if (type instanceof EntityObjectType) {
                return ((EntityObjectType) type).getSource();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public boolean isTransferable(ERuntimeEnvironmentType env) {
        return isSuperTransferable(env);
    }

    @Override
    protected void fireNameChange() {
        super.fireNameChange();
        if (getOwnerClass() instanceof AdsReportClassDef) {
            ((AdsReportClassDef) getOwnerClass()).updateMethodsParams();
        }
    }
}
