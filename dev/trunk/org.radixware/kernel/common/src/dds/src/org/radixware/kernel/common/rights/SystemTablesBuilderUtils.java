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
package org.radixware.kernel.common.rights;

import java.awt.Point;
import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.IEnumDef;
import org.radixware.kernel.common.defs.dds.DdsAccessPartitionFamilyDef;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsExtTableDef;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsModelDef;
import org.radixware.kernel.common.defs.dds.DdsModule;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.utils.DbNameUtils;
import org.radixware.kernel.common.defs.value.RadixDefaultValue;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EDeleteMode;
import org.radixware.kernel.common.enums.EOrder;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import static org.radixware.kernel.common.rights.SystemTablesBuilder.ConjecturalRectangleSizeX;
import static org.radixware.kernel.common.rights.SystemTablesBuilder.ConjecturalRectangleSizeY;
import org.radixware.kernel.common.utils.Reference;
import org.radixware.kernel.common.utils.Utils;

public class SystemTablesBuilderUtils {

    protected static DdsColumnDef findColumnByDbName(final DdsTableDef table, final String dbName) {
        for (DdsColumnDef col : table.getColumns().get(ExtendableDefinitions.EScope.LOCAL)) {
            if (Utils.equals(col.getDbName(), dbName)) {
                return col;
            }
        }        
        return null;
    }

    private static DdsReferenceDef findRef(final DdsModelDef model, final DdsTableDef tbl, final DdsTableDef table2Role, final DdsColumnDef columnInTable2Role) {
        for (DdsReferenceDef ref : model.getReferences()) {
            if (!Utils.equals(ref.getChildTableId(), table2Role.getId())) {
                continue;
            }
            if (!Utils.equals(ref.getParentTableId(), tbl.getId())) {
                continue;
            }
            if (tbl.getPrimaryKey().getColumnsInfo().size() != 1) {
                continue;
            }

            final DdsReferenceDef.ColumnsInfoItems colInfo = ref.getColumnsInfo();
            if (colInfo == null || colInfo.size() != 1) {
                continue;
            }

            final DdsReferenceDef.ColumnsInfoItem colItem = colInfo.get(0);

            if (!Utils.equals(colItem.getChildColumnId(), columnInTable2Role.getId())) {
                continue;
            }

            if (!Utils.equals(colItem.getParentColumnId(), tbl.getPrimaryKey().getColumnsInfo().get(0).getColumn().getId())) {
                continue;
            }
            return ref;
        }
        return null;
    }

    protected static DdsExtTableDef createExtTable(final DdsModelDef model,
            final DdsModule module,
            final SystemTablesSearcher searcher,
            final DdsTableDef tbl,
            final Reference<Boolean> findPoints,
            final List<Point> pointList,
            final DdsTableDef table2Role) {
        DdsExtTableDef exTable = DdsExtTableDef.Factory.newInstance(tbl);
        model.getExtTables().add(exTable);
        exTable.setTableId(tbl.getId());

        if (!findPoints.get()) {
            findPoints.set(true);
            module.visitChildren(
                    searcher.getNoneVisitor(),
                    new SystemTablesBuilder.IPlacementVisitorProvider(pointList));
        }
        int px = table2Role.getPlacement().getPosX();
        int py = table2Role.getPlacement().getPosY() - 10;
        int px1, py1, px2, py2;

        int count = -1;
        int napr = 0;
        boolean find = false;

        while (true) {
            if ((napr % 2) == 0) {
                count++;
            }
            for (int i = 0; i < count; i++) {
                px += 100 * (napr == 1 ? 1 : (napr == 3 ? -1 : 0));
                py += 100 * (napr == 2 ? 1 : (napr == 0 ? -1 : 0));
                px1 = px - ConjecturalRectangleSizeX;
                py1 = py - ConjecturalRectangleSizeY;
                px2 = px + ConjecturalRectangleSizeX;
                py2 = py + ConjecturalRectangleSizeY;

                find = true;
                for (Point p : pointList) {
                    if (px1 <= p.getX()
                            && px2 >= p.getX()
                            && py1 <= p.getY()
                            && py2 >= p.getY()) {
                        find = false;
                        break;
                    }
                }
                if (find) {
                    break;
                }
            }

            if (find) {
                break;
            }
            if (++napr > 3) {
                napr = 0;
            }
        }

        exTable.getPlacement().setPosX(px);
        exTable.getPlacement().setPosY(py);
        pointList.add(new Point(px, py));

        return exTable;
    }

    protected static DdsExtTableDef findExTable(final DdsModelDef model, final DdsTableDef tbl) {
        for (DdsExtTableDef ext : model.getExtTables()) {
            if (ext.findTable() == tbl) {
                return ext;
            }
        }
        return null;
    }

    private static boolean existsTable(final DdsModelDef model, final DdsTableDef tbl) {
        for (DdsTableDef tb : model.getTables()) {
            if (tb.getId().equals(tbl.getId())) {
                return true;
            }
        }
        return false;
    }

    protected static void fillRef(
            final DdsReferenceDef ref,
            final boolean isNewRef,
            final DdsTableDef tbl,
            final DdsModelDef model,
            final SystemTablesSearcher searcher,
            final List<Point> pointList,
            final DdsModule module,
            final Reference<Boolean> findPoints,
            final DdsTableDef table2Role,
            final DdsColumnDef childColumn
    ) {
        ref.setType(DdsReferenceDef.EType.LINK);

        ref.setGeneratedInDb(true);
        ref.setDeleteMode(EDeleteMode.CASCADE);

        ref.setParentUnuqueConstraintId(tbl.getPrimaryKey().getUniqueConstraint().getId());

        if (!existsTable(model, tbl)) {
            DdsExtTableDef exTable = findExTable(model, tbl);
            if (exTable == null) {
                exTable = createExtTable(model, module, searcher, tbl, findPoints, pointList, table2Role);
            }
            ref.setExtParentTableId(exTable.getId());
        }
        ref.setChildTableId(table2Role.getId());
        ref.setParentTableId(tbl.getId());
        ref.getColumnsInfo().clear();
        ref.getColumnsInfo().add(childColumn, tbl.getPrimaryKey().getColumnsInfo().get(0).getColumn());
        //correct name and dbname for ref

        String goodName = "_" + table2Role.getName() + "_" + tbl.getName();
        goodName = goodName.replace(' ', '_');

        ref.setName(goodName);

        final String oldDbName = ref.getDbName();
        if (isNewRef || Utils.emptyOrNull(oldDbName)) {
            ref.setAutoDbName(true);
            DbNameUtils.updateAutoDbNames(ref);
        } else {
            //do nothing
        }
    }

    protected static void checkOrCreatePartitionRef(
            final DdsModelDef model,
            final SystemTablesSearcher searcher,
            final List<Point> pointList,
            final DdsModule module,
            final SystemTablesBuilder.Item item,
            final Reference<Boolean> findPoints,
            final DdsTableDef table2Role) {

        final DdsTableDef tbl = searcher.getPartitionGroup();

        if (item.partGroupRef == null) {
            item.partGroupRef = findRef(model, tbl, table2Role, item.columnPartGroupId);
        }
        boolean isNewRef = false;

        if (item.partGroupRef == null) {
            isNewRef = true;
            item.partGroupRef = DdsReferenceDef.Factory.newInstance();
            model.getReferences().add(item.partGroupRef);
        }

        fillRef(item.partGroupRef, isNewRef, tbl,
                model, searcher, pointList,
                module, findPoints, table2Role, item.columnPartGroupId);
    }

    protected static void checkOrCreateKeyRef(
            final DdsModelDef model,
            final SystemTablesSearcher searcher,
            final List<Point> pointList,
            final DdsModule module,
            final SystemTablesBuilder.Item item,
            final Definition apfTargetDef,
            final Reference<Boolean> findPoints,
            final DdsTableDef table2Role) {

        if (!(apfTargetDef instanceof DdsTableDef)) {
            return;
        }

        final DdsTableDef tbl = (DdsTableDef) apfTargetDef;

        if (item.keyRef == null) {
            item.keyRef = findRef(model, tbl, table2Role, item.columnKey);
        }
        boolean isNewRef = false;

        if (item.keyRef == null) {
            isNewRef = true;
            item.keyRef = DdsReferenceDef.Factory.newInstance();
            model.getReferences().add(item.keyRef);
        }
        
        fillRef(item.keyRef, isNewRef, tbl,
                model, searcher, pointList,
                module, findPoints, table2Role, item.columnKey);
    }

    protected static DdsIndexDef findKeyIndex(final SystemTablesBuilder.Item item, final DdsTableDef table2Role) {
        for (DdsIndexDef ind : table2Role.getIndices().get(ExtendableDefinitions.EScope.LOCAL)) {
            if (ind.getColumnsInfo().size() != 1) {
                continue;
            }
            final DdsColumnDef column = ind.getColumnsInfo().get(0).findColumn();
            if (column == null) {
                continue;
            }
            if (Utils.equals(column.getDbName(), item.columnKey.getDbName())) {
                return ind;
            }
        }
        return null;
    }

    protected static void checkOrCreateKeyIndex(final SystemTablesBuilder.Item item, final DdsTableDef table2Role, final Definition apfTargetDef) {

        if (!(apfTargetDef instanceof DdsTableDef)) {
            return;
        }

        if (item.keyIndex == null) {
            item.keyIndex = findKeyIndex(item, table2Role);
        }

        if (item.keyIndex == null) {
            String goodName = item.apf.getName();
            goodName = goodName.replace(' ', '_');
            if (goodName.length() > 25) {
                goodName = goodName.substring(0, 25);
            }
            goodName += "Index";

            item.keyIndex = DdsIndexDef.Factory.newInstance(goodName);
            item.keyIndex.getColumnsInfo().add(item.columnKey.getId(), EOrder.ASC);
            table2Role.getIndices().getLocal().add(item.keyIndex);
        }

        item.keyIndex.setAutoDbName(true);
        item.keyIndex.setGeneratedInDb(true);
        DbNameUtils.updateAutoDbNames(item.keyIndex);
    }

    protected static void checkOrCreateColumnPartGroupId(final SystemTablesBuilder.Item item, final DdsTableDef table2Role, final DdsAccessPartitionFamilyDef apf) {
        final String apIdAsStr = apf.getId().toString();
        final String dbName = "PG$$" + apIdAsStr.substring(3);
        if (item.columnPartGroupId == null) {
            item.columnPartGroupId = SystemTablesBuilderUtils.findColumnByDbName(table2Role, dbName);
        }

        if (item.columnPartGroupId == null) {
            item.columnPartGroupId = DdsColumnDef.Factory.newInstance("ap" + apf.getName() + "PartGroupId");
            table2Role.getColumns().getLocal().add(item.columnPartGroupId);
        } else {
            item.columnPartGroupId.setName("ap" + apf.getName() + "PartGroupId");
        }
        item.columnPartGroupId.setAutoDbName(false);
        item.columnPartGroupId.setValType(EValType.INT);
        item.columnPartGroupId.setDefaultValue(null);//RadixDefaultValue.Factory.newValAsStr(ValAsStr.Factory.newInstance(0, EValType.INT))
        item.columnPartGroupId.setHidden(false);
        item.columnPartGroupId.setNotNull(false);
        item.columnPartGroupId.setLength(9);
        item.columnPartGroupId.setDbType("NUMBER(9,0)");
        item.columnPartGroupId.setDbName(dbName);
        DbNameUtils.updateAutoDbNames(item.columnPartGroupId);
    }

    protected static void checkOrCreateColumnMode(final SystemTablesBuilder.Item item, final DdsTableDef table2Role, final DdsAccessPartitionFamilyDef apf) {
        final String apIdAsStr = apf.getId().toString();
        final String dbName = "MA$$" + apIdAsStr.substring(3);

        if (item.columnMode == null) {
            item.columnMode = SystemTablesBuilderUtils.findColumnByDbName(table2Role, dbName);
        }

        if (item.columnMode == null) {
            item.columnMode = DdsColumnDef.Factory.newInstance("ap" + apf.getName() + "Mode");
            table2Role.getColumns().getLocal().add(item.columnMode);
        } else {
            item.columnMode.setName("ap" + apf.getName() + "Mode");
        }
        item.columnMode.setAutoDbName(false);
        item.columnMode.setValType(EValType.INT);
        item.columnMode.setDefaultValue(RadixDefaultValue.Factory.newValAsStr(ValAsStr.Factory.newInstance(0, EValType.INT)));
        item.columnMode.setHidden(false);
        item.columnMode.setNotNull(true);
        item.columnMode.setLength(1);
        item.columnMode.setDbType("NUMBER(1,0)");
        item.columnMode.setDbName(dbName);
        DbNameUtils.updateAutoDbNames(item.columnMode);
    }

    protected static void checkOrCreateColumnKey(final SystemTablesBuilder.Item item, final DdsTableDef table2Role, final DdsAccessPartitionFamilyDef apf, final Definition apfTargetDef) {
        final String apIdAsStr = apf.getId().toString();
        final String dbName = "PA$$" + apIdAsStr.substring(3);

        if (item.columnKey == null) {
            item.columnKey = findColumnByDbName(table2Role, dbName);
        }

        if (item.columnKey == null) {
            item.columnKey = DdsColumnDef.Factory.newInstance("ap" + apf.getName() + "Key");
            table2Role.getColumns().getLocal().add(item.columnKey);
        } else {
            item.columnKey.setName("ap" + apf.getName() + "Key");
        }

        item.columnKey.setAutoDbName(false);

        item.columnKey.setHidden(false);
        item.columnKey.setNotNull(false);
        item.columnKey.setDbName(dbName);

        if (apfTargetDef instanceof DdsTableDef) {
            final DdsTableDef tbl = (DdsTableDef) apfTargetDef;
            if (tbl.getPrimaryKey().getColumnsInfo().size() != 1) {
                throw new DefinitionError("Incorrect primary key in access partition family table " + apf.getName(), apf);
            }
            final DdsColumnDef parentColumn = tbl.getPrimaryKey().getColumnsInfo().get(0).getColumn();
            if (parentColumn == null || parentColumn.getValType() != EValType.INT && parentColumn.getValType() != EValType.STR) {
                throw new DefinitionError("Incorrect primary key in access partition family table " + apf.getName(), apf);
            }
            final int maxLen = parentColumn.getLength();
            item.columnKey.setValType(parentColumn.getValType());
            item.columnKey.setLength(maxLen);
            item.columnKey.setDbType(
                    parentColumn.getValType() == EValType.INT ? "NUMBER(" + String.valueOf(maxLen) + ",0)" : "VARCHAR2(" + String.valueOf(maxLen) + " char)");
        } else if (apfTargetDef instanceof IEnumDef) {
            final IEnumDef enumDef = (IEnumDef) apfTargetDef;
            final String dbType;
            final EValType type;
            final int maxLen;

            final EValType keyType = enumDef.getItemType();

            if (keyType.equals(EValType.INT)) {
                dbType = "NUMBER(10,0)";
                maxLen = 10;
                type = EValType.INT;
            } else if (keyType.equals(EValType.CHAR)) {
                dbType = "VARCHAR2(4000 char)";
                maxLen = 4000;
                type = EValType.STR;
            } else {
                dbType = "VARCHAR2(1 char)";
                maxLen = 1;
                type = EValType.STR;
            }

            item.columnKey.setValType(type);
            item.columnKey.setLength(maxLen);
            item.columnKey.setDbType(dbType);
//                    columnKey.setEnumId(enumDef.getId());
        } else {
            throw new DefinitionError("Incorrect access partition family type " + apf.getName(), apf);
        }

        DbNameUtils.updateAutoDbNames(item.columnKey);
    }
    

}
