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

package org.radixware.kernel.common.builder.check.dds;

import org.radixware.kernel.common.defs.dds.DdsColumnTemplateDef;
import org.radixware.kernel.common.defs.dds.DdsTypeDef;
import org.radixware.kernel.common.defs.dds.IDdsDbDefinition;
import org.radixware.kernel.common.defs.dds.utils.DbTypeUtils;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;


@RadixObjectCheckerRegistration
public class DdsColumnTemplateChecker<T extends DdsColumnTemplateDef> extends DdsDefinitionChecker<T> {

    public DdsColumnTemplateChecker() {
    }

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return DdsColumnTemplateDef.class;
    }

    @Override
    public void check(T columnTemplate, IProblemHandler problemHandler) {
        super.check(columnTemplate, problemHandler);

        if (Utils.equals(columnTemplate.calcAutoDbType(), columnTemplate.getDbType())) {
            if (!columnTemplate.isAutoDbType()) {
                warning(columnTemplate, problemHandler, "Column auto database type must be turned on.");
            }
        } else {
            error(columnTemplate, problemHandler, "Calculated and assigned database type doesn't match.");
        }

        final EValType valType = columnTemplate.getValType();
        if (valType == null || !valType.isAllowedForDdsColumn()) {
            error(columnTemplate, problemHandler, "Illegal value type: " + String.valueOf(valType));
        }

// RADIX-1351
//        if (columnTemplate.getEnumId() != null) {
//            final IEnumDef enumDef = columnTemplate.findEnum();
//            if (enumDef != null) {
//                final boolean isArrayType = valType.isArrayType();
//                if ((isArrayType && valType.getArrayItemType() != enumDef.getItemType()) ||
//                        (!isArrayType && valType != enumDef.getItemType())) {
//                    error(columnTemplate, problemHandler, "Enum item type doesn't match to column value type");
//                }
//            } else {
//                error(columnTemplate, problemHandler, "Enum not found: #" + String.valueOf(columnTemplate.getEnumId()));
//            }
//        }

        if (valType == EValType.NATIVE_DB_TYPE) {
            DdsTypeDef type = columnTemplate.findNativeDbType();
            if (type != null) {
                if (!Utils.equals(columnTemplate.getDbType(), type.getDbName())) {
                    error(columnTemplate, problemHandler, "Column database type doesn't match to native database type");
                }
            } else {
                error(columnTemplate, problemHandler, "Native database type not found: #" + String.valueOf(columnTemplate.getNativeDbTypeId()));
            }
        } else {
            if (!(columnTemplate instanceof IDdsDbDefinition) || ((IDdsDbDefinition) columnTemplate).isGeneratedInDb()) {
                String dbType = columnTemplate.getDbType();
                if (!DbTypeUtils.isCompatible(valType, dbType)) {
                    error(columnTemplate, problemHandler, "Database type doesn't match to value type");
                }
            }
        }

        final int len = columnTemplate.getLength();
        final int precision = columnTemplate.getPrecision();

        switch (valType) {
            case NUM:
                if (len == 0 && precision < 0 || len > 0 && precision < 1) {
                    error(columnTemplate, problemHandler, "Precision is out of range");
                }
                if (len < 0) {
                    error(columnTemplate, problemHandler, "Length is out of range");
                }
                break;
            case INT:
                if (len <= 0 || len > 18) {
                    error(columnTemplate, problemHandler, "Length is out of range");
                }
                break;
            case STR:
                if (len < 1 || len > 4000) {
                    error(columnTemplate, problemHandler, "Length is out of range");
                }
                break;
            case BIN:
                if (len < 1 || len > 2000) {
                    error(columnTemplate, problemHandler, "Length is out of range");
                }
                break;
            case DATE_TIME:
                if (precision < 0 || precision > 9) {
                    error(columnTemplate, problemHandler, "Precision is out of range");
                }
                break;
        }

        if (valType.isArrayType()) {
            if (len < 0 || len > 4000) {
                error(columnTemplate, problemHandler, "Length is out of range");
            }
        }
    }
}
