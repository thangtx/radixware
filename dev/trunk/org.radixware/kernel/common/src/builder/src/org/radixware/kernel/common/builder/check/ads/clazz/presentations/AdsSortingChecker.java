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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.builder.check.ads.AdsDefinitionChecker;
import org.radixware.kernel.common.builder.check.ads.CheckUtils;
import static org.radixware.kernel.common.builder.check.common.RadixObjectChecker.error;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSortingDef;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.DdsViewDef;

@RadixObjectCheckerRegistration
public class AdsSortingChecker extends AdsDefinitionChecker<AdsSortingDef> {

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return AdsSortingDef.class;
    }

    @Override
    public void check(AdsSortingDef sorting, IProblemHandler problemHandler) {
        super.check(sorting, problemHandler);
        CheckUtils.checkMLStringId(sorting, sorting.getTitleId(), problemHandler, "title");
        RadixObjects<AdsSortingDef.OrderBy> orderBys = sorting.getOrder();
        DdsTableDef table = sorting.getOwnerClass().findTable(sorting);
        boolean isView = table instanceof DdsViewDef;
        List<AdsPropertyDef> props = new LinkedList<>();
        for (AdsSortingDef.OrderBy orderBy : orderBys) {

            AdsPropertyDef prop = orderBy.findProperty();
            if (prop == null) {
                error(sorting, problemHandler, "Unknown property in order");
            } else {
                props.add(prop);
                AdsUtils.checkAccessibility(sorting, prop, false, problemHandler);
                //CheckUtils.checkExportedApiDatails(sorting, prop, problemHandler);
                //CLOB, BLOB, PARENT_REF, OBJECT, а также свойства с Nature = EPropNature.DYNAMIC
                prop.canBeUsedInSorting(sorting, problemHandler);
            }
        }
        if (isView) {
            List<DdsIndexDef> indexes = new LinkedList<>();
            indexes.add(table.getPrimaryKey());
            for (DdsIndexDef index : table.getIndices().get(ExtendableDefinitions.EScope.ALL)) {
                if (index.isUnique()) {
                    indexes.add(index);
                }
            }

            boolean atLeasOneIndexInSorting = false;
            for (DdsIndexDef index : indexes) {
                boolean indexInSorting = true;
                for (DdsIndexDef.ColumnInfo column : index.getColumnsInfo()) {
                    boolean columnFound = false;
                    for (AdsPropertyDef prop : props) {
                        if (column.getColumnId() == prop.getId()) {
                            columnFound = true;
                            break;
                        }
                    }
                    if (!columnFound) {
                        indexInSorting = false;
                        break;
                    }
                }
                if (indexInSorting) {
                    atLeasOneIndexInSorting = true;
                    break;
                }
            }

            if (!atLeasOneIndexInSorting) {
                error(sorting, problemHandler, "View-based sorting must include columns of at least on of owner view unique indices");
            }
        }
    }
}
