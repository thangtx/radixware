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

package org.radixware.kernel.common.builder.check.ads.clazz.presentations;

import org.radixware.kernel.common.builder.check.common.RadixObjectChecker;
import static org.radixware.kernel.common.builder.check.common.RadixObjectChecker.error;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsInnateRefPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;

import org.radixware.kernel.common.defs.ads.common.Prop2ValueMap;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.ParentRefType;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.exceptions.WrongFormatError;


@RadixObjectCheckerRegistration
public class Prop2ValueMapChecker extends RadixObjectChecker<Prop2ValueMap> {

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return Prop2ValueMap.class;
    }

    @Override
    public void check(Prop2ValueMap map, IProblemHandler problemHandler) {
        super.check(map, problemHandler);
        for (Prop2ValueMap.Prop2ValMapItem item : map.getItems()) {
            AdsPropertyDef prop = item.findProperty();
            if (prop == null) {
                error(item, problemHandler, "Declarative part of condition refers to unknown property #" + item.getPropertyId());
            } else {
                if (!map.isPropertySuitableForMap(prop)) {
                    error(item, problemHandler, "Property " + prop.getQualifiedName() + " can not be used in declarative part of condition");
                }
                if (prop instanceof AdsInnateRefPropertyDef) {
                    String valueForProperty = item.getStringValue();
                    if (valueForProperty != null) {
                        AdsType type = prop.getValue().getType().resolve(map.getDefinition()).get();
                        if (type instanceof ParentRefType) {
                            AdsEntityObjectClassDef clazz = ((ParentRefType) type).getSource();
                            if (clazz == null) {
                                error(item, problemHandler, "Can not find class referenced by property " + prop.getQualifiedName() + " used in declarative part of condition");
                            } else {
                                DdsTableDef table = clazz.findTable(map);
                                if (table == null) {
                                    error(item, problemHandler, "Can not find table referenced by property " + prop.getQualifiedName() + " used in declarative part of condition");
                                } else {
                                    int count = table.getPrimaryKey().getColumnsInfo().size();
                                    String[] pidComponents = valueForProperty.split("~");
                                    if (pidComponents.length != count) {
                                        error(item, problemHandler, "Value for reference property " + prop.getQualifiedName() + " used in declarative part of condition does not match to PK format of referenced table " + table.getQualifiedName());
                                    } else {
                                        int idx = 0;
                                        for (DdsIndexDef.ColumnInfo cInfo : table.getPrimaryKey().getColumnsInfo()) {
                                            DdsColumnDef column = cInfo.findColumn();
                                            if (column == null) {
                                                error(item, problemHandler, "Can not check value of reference property " + prop.getQualifiedName() + " used in declarative part of condition, because column #" + cInfo.getColumnId() + " of table " + table.getQualifiedName() + " does not exist");
                                                break;
                                            } else {
                                                EValType valType = column.getValType();
                                                try {
                                                    ValAsStr.fromStr(pidComponents[idx], valType);
                                                } catch (WrongFormatError | RadixError e) {
                                                    error(item, problemHandler, "Wrong value for column " + column.getQualifiedName() + " in value of property " + prop.getQualifiedName() + " in declarative part of condition");
                                                }
                                            }
                                            idx++;
                                        }
                                    }
                                }
                            }
                        } else {
                            error(item, problemHandler, "Wrong type of property " + prop.getQualifiedName() + " in declarative part of condition. Parent reference type expected");
                        }
                    }
                } else {
                    if (item.getStringValue() != null) {
                        EValType valType = prop.getValue().getType().getTypeId();
                        try {
                            ValAsStr.fromStr(item.getStringValue(), valType);
                        } catch (WrongFormatError | RadixError e) {
                            error(item, problemHandler, "Wrong value of property " + prop.getQualifiedName() + " in declarative part of condition");
                        }
                    }
                }
            }
        }
    }
}
