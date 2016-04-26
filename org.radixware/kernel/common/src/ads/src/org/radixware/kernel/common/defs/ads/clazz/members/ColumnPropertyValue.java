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

import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.common.defs.ads.AdsValAsStr;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProviders;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.ERadixDefaultValueChoice;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.schemas.adsdef.AbstractPropertyDefinition;


class ColumnPropertyValue extends PropertyValue {

    ColumnPropertyValue(AdsPropertyDef context, AbstractPropertyDefinition xDef) {
        super(context, xDef);
    }

    ColumnPropertyValue(AdsPropertyDef context) {
        super(context);
    }

    ColumnPropertyValue(AdsPropertyDef context, ColumnPropertyValue source) {
        super(context, source);
    }

    @Override
    public boolean isTypeAllowed(EValType type) {
        DdsColumnDef c = getColumnInfo().findColumn();
        if (c == null) {
            return false;
        } else {
            if (c.getValType() == type || (c.getValType() == EValType.CLOB && (type == EValType.XML || type == EValType.STR))) {
                if (!type.isAllowedForInnateProperty() && type != EValType.XML && type != EValType.STR) {
                    return false;
                }
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public boolean isTypeRefineAllowed(EValType type) {
        DdsColumnDef c = getColumnInfo().findColumn();
        if (c == null) {
            return false;
        } else {
            if (c.getValType() == EValType.CLOB && type == EValType.STR) {
                return !type.isEnumAssignableType();                
            }
            return type != null && (type.isEnumAssignableType() || type == EValType.ARR_REF || type == EValType.XML);
        }
    }

    @Override
    public VisitorProvider getTypeSourceProvider(EValType toRefine) {
        if (toRefine != null && (toRefine.isEnumAssignableType() || toRefine == EValType.ARR_REF || toRefine == EValType.XML)) {
            DdsColumnDef c = getColumnInfo().findColumn();
            if (c != null) {
                if (c.getValType().isEnumAssignableType()) {
                    return AdsVisitorProviders.newEnumForColumnPropertyProvider(c);
                } else if (c.getValType() == EValType.ARR_REF) {
                    return AdsVisitorProviders.newEntityObjectTypeProvider(null);
                } else if (c.getValType() == EValType.XML) {
                    return AdsVisitorProviders.newXmlBasedTypesProvider(ERuntimeEnvironmentType.SERVER);
                } else {
                    return VisitorProviderFactory.createEmptyVisitorProvider();
                }
            } else {
                return VisitorProviderFactory.createEmptyVisitorProvider();
            }
        } else {
            return VisitorProviderFactory.createEmptyVisitorProvider();
        }
    }

    private ColumnInfo getColumnInfo() {
        return ((ColumnProperty) getProperty()).getColumnInfo();
    }

    void updateInitValFromColumn() {

        AdsValAsStr propInitVal = getInitial();
        ValAsStr propVal = null;
        if (propInitVal != null && propInitVal.getValueType() == AdsValAsStr.EValueType.VAL_AS_STR) {
            propVal = propInitVal.getValAsStr();
        }

        DdsColumnDef column = getColumnInfo().findColumn();
        if (column != null) {
            ValAsStr colVal = column.getDefaultValue() != null && column.getDefaultValue().getChoice() == ERadixDefaultValueChoice.VAL_AS_STR ? column.getDefaultValue().getValAsStr() : null;
            if (propVal != null || colVal != null) {
                if (propVal != null) {
                    if (colVal != null) {
                        if (!propVal.equals(colVal)) {
                            setInitial(ValAsStr.Factory.newCopy(colVal));
                        }
                    }
                } else {
                    if (colVal != null) {
                        setInitial(ValAsStr.Factory.newCopy(colVal));
                    }
                }
            }
        }
    }
}
