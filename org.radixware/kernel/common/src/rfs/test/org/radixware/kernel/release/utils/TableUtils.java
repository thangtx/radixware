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

package org.radixware.kernel.release.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;

import org.radixware.kernel.common.defs.dds.DdsModule;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EValType;
import static org.junit.Assert.*;


public abstract class TableUtils extends TestUtils {

    private static final int tableCounter = 0;

    public TableUtils(int layersCount, int modulePerLayer, boolean enumsRequired) throws IOException {
        super(layersCount, modulePerLayer, enumsRequired);
    }

    public DdsTableDef createTable(DdsModule module) {
        DdsTableDef table = DdsTableDef.Factory.newInstance("table" + tableCounter);
        module.getModelManager().findModel().getTables().add(table);
        return table;
    }

    public ArrayList<DdsColumnDef> createColumns(DdsTableDef table, int columnCount) {
        ArrayList<DdsColumnDef> columns = new ArrayList<DdsColumnDef>();
        DdsModule context = table.getModule();
        for (int i = 0; i < columnCount; i++) {
            EValType type = EValType.values()[random.nextInt(EValType.values().length)];

            while (!type.allowedForInnateProperty()) {
                type = EValType.values()[random.nextInt(EValType.values().length)];
            }

            final String name = "c" + String.valueOf(i) + "_" + type.name();
            DdsColumnDef c = DdsColumnDef.Factory.newInstance(name);
            c.setValType(type);

            table.getColumns().getLocal().add(c);
            columns.add(c);
        }
        return columns;
    }
    private static final EnumSet<EValType> const_types = EnumSet.of(EValType.INT, EValType.CHAR, EValType.STR, EValType.ARR_CHAR, EValType.ARR_STR, EValType.ARR_INT);

    private static final EValType simpleType(EValType arrType) {
        switch (arrType) {
            case ARR_INT:
                return EValType.INT;
            case ARR_STR:
                return EValType.STR;
            case ARR_CHAR:
                return EValType.CHAR;
            default:
                return arrType;
        }
    }

    public DdsReferenceDef createParentRef(DdsTableDef child, DdsTableDef parent) {
        ArrayList<DdsTableDef> list = new ArrayList<DdsTableDef>();
        list.add(parent);
        return createParentRefs(child, list).get(0);
    }

    public ArrayList<DdsReferenceDef> createParentRefs(DdsTableDef child, ArrayList<DdsTableDef> parentTables) {
        ArrayList<DdsReferenceDef> refs = new ArrayList<DdsReferenceDef>();
        DdsModule module = child.getModule();
        int count = parentTables.size();
        for (int p = 0; p < count; p++) {
            DdsTableDef parentTable = parentTables.get(p);
            DdsReferenceDef ref = DdsReferenceDef.Factory.newInstance();
            ref.setChildTableId(child.getId());
            ref.setParentTableId(parentTable.getId());

            module.getModelManager().findModel().getReferences().add(ref);

            module.getDependences().add(parentTable.getModule());

            assertNotNull(ref.findChildTable(child));
            assertNotNull(ref.findParentTable(child));
            assertEquals(parentTable.getId(), ref.findParentTable(child).getId());
            assertEquals(child.getId(), ref.findChildTable(child).getId());

            refs.add(ref);
        }
        return refs;
    }

    public ArrayList<DdsReferenceDef> createDetails(DdsTableDef parent, int detailsCount, int maxColumnCount) {
        ArrayList<DdsReferenceDef> detailTablesArr = new ArrayList<DdsReferenceDef>();
        DdsModule module = parent.getModule();
        for (int dtidx = 0; dtidx < detailsCount; dtidx++) {
            final String name = "<detail of " + parent.getQualifiedName() + ">." + dtidx;
            DdsTableDef detail = DdsTableDef.Factory.newInstance(name);

            module.getModelManager().findModel().getTables().add(detail);
            if (maxColumnCount > 0) {
                createColumns(detail, random.nextInt(maxColumnCount));
            }

            DdsReferenceDef ref = DdsReferenceDef.Factory.newInstance();
            ref.setType(DdsReferenceDef.EType.MASTER_DETAIL);
            ref.setChildTableId(detail.getId());
            ref.setParentTableId(parent.getId());

            detailTablesArr.add(ref);
            module.getModelManager().findModel().getReferences().add(ref);

            assertNotNull(ref.findChildTable(parent));
            assertNotNull(ref.findParentTable(parent));
            assertEquals(parent.getId(), ref.findParentTable(parent).getId());
            assertEquals(detail.getId(), ref.findChildTable(parent).getId());

        }
        return detailTablesArr;
    }

    public TableUtils(TestUtils source) {
        super(source);
    }
}
