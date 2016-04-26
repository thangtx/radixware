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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IEnumDef;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.common.defs.dds.DdsAccessPartitionFamilyDef;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsExtTableDef;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsModelDef;
import org.radixware.kernel.common.defs.dds.DdsModule;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.enums.EDeleteMode;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef.EType;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.IPlacementSupport;
import org.radixware.kernel.common.defs.dds.providers.DdsVisitorProvider;
import org.radixware.kernel.common.defs.dds.providers.DdsVisitorProviderFactory;
import org.radixware.kernel.common.defs.dds.utils.DbNameUtils;
import org.radixware.kernel.common.defs.value.RadixDefaultValue;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EOrder;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.RadixObjectsUtils;


public class SystemTablesBuilder {

    public static final Id DRC_MODULE_ID = Id.Factory.loadFrom("mdlOB5NQX24YXNBDGMBABQAQH3XQ4");
    private static final String USER2ROLE = "User2Role";
    private static final String USERGROUP2ROLE = "UserGroup2Role";
    private static final String CDRCACCESSAREAMODENAME = "CDrcAccessAreaMode";
    private static final int ConjecturalRectangleSizeX = 100;
    private static final int ConjecturalRectangleSizeY = 180;
    SystemTablesSearcher searcher;
    //private IVisitor noneVisitor;
    //private SystemTablesVisitorProvider sysVisitorProvider;
    private IEnumDef cDrcAccessAreaMode = null;
    private DdsModelDef model = null;
    private DdsModule module = null;
    private List<Item> itemList = null;
    // private List<String> columnModeList = null;
    // private List<String> columnKeyList = null;
    // private List<String> columnIdList = null;
    //private List<UseAxis> useAxisList = null;
    private List<Point> pointList = new ArrayList<Point>(0);

    //private DdsTableDef clerk2Role = null;
    //private DdsTableDef clerkGroup2Role = null;
    private static class Item extends RadixObject {

        Item(DdsAccessPartitionFamilyDef apf) {
            this.apf = apf;
            String s = apf.getHeadId().toString();
            modeDBName = "M" + s.substring(0, 1) + "$$" + s.substring(3);
            keyDBName = "P" + s.substring(0, 1) + "$$" + s.substring(3);
        }
        final String modeDBName;
        final String keyDBName;
        DdsAccessPartitionFamilyDef apf;
        DdsColumnDef columnMode = null;
        DdsColumnDef columnKey = null;
        //boolean isTable = false;
        DdsReferenceDef ref = null;
        DdsIndexDef index = null;

        @Override
        public String getName() {
            return apf.getName();
        }
    }

    private class IPlacementVisitorProvider extends DdsVisitorProvider {

        @Override
        public boolean isTarget(RadixObject obj) {
            if (obj instanceof IPlacementSupport) {
                IPlacementSupport pls = (IPlacementSupport) obj;
                pointList.add(new Point(pls.getPlacement().getPosX(), pls.getPlacement().getPosY()));
            }
            return false;
        }
    }

    SystemTablesBuilder(DdsModule module) {
        if (module == null) {
            throw new DefinitionError("Dds module not defined ", this);
        }
        this.module = module;

    }

    private void searchSystemInstances() {
        searcher = new SystemTablesSearcher(module, false, false, false);

        if (searcher.getClerk2Role() == null) {
            throw new DefinitionError("Can't create access control service columns: overriding table for \"" + USER2ROLE + "\" is required but not found.", this);
        }
        if (searcher.getClerkGroup2Role() == null) {
            throw new DefinitionError("Can't create access control service columns: overriding table for \"" + USERGROUP2ROLE + "\" is required but not found.", this);
        }
        if (searcher.getIndexOverriteUser2Role() != searcher.getIndexOverriteUserGroup2Role() /*|| searcher.getIndexOverriteUser2Role() == 0*/) {
            throw new DefinitionError("Incorrect system overriding tables location.", this);
        }


    }

    private void removeColumns(DdsTableDef table) {

        List<DdsColumnDef> columnList = table.getColumns().get(EScope.LOCAL);


        for (DdsColumnDef col : columnList) {
            String name = col.getName();
            int nameLen = name.length();
            if (nameLen > 2 && name.substring(0, 2).equals("AP")) {
                boolean mustDel = false;
                if (nameLen > 6 && name.substring(nameLen - 4, nameLen).equals("Mode")) {
                    mustDel = true;
                }
                if (nameLen > 5 && name.substring(nameLen - 3, nameLen).equals("Key")) {
                    // remove index
                    DdsIndexDef index = null;
                    for (DdsIndexDef ind : table.getIndices().get(EScope.LOCAL)) {
                        if (ind.getColumnsInfo().size() == 1 && ind.getColumnsInfo().get(0).getColumn() == col) {
                            index = ind;
                            break;
                        }
                    }

                    if (index != null) {
                        if (!index.delete()) {
                            throw new DefinitionError("Can't remove index "
                                    + table + "(" + index.getName() + ")", this);
                        }
                    }

                    //Remove reference
                    for (DdsReferenceDef ref : table.collectOutgoingReferences()) {
                        if (ref.getColumnsInfo().size() == 1 && ref.getColumnsInfo().get(0).findChildColumn() == col) {
                            if (!ref.delete()) {
                                throw new DefinitionError("Can't remove reference "
                                        + table + "(" + ref.getName() + ")", this);
                            }
                            break;
                        }
                    }
                    mustDel = true;
                }
                if (mustDel) {
                    if (!col.delete()) {
                        throw new DefinitionError("Can't remove column "
                                + table + "(" + col.getName() + ")", this);
                    }
                    continue;
                }
            }



            // Remove new version
            if (!col.isAutoDbName()) {
                String dbName = col.getDbName();
                if (dbName.length() == 30) {
                    String prefix, postfix;

                    prefix = dbName.substring(0, 1);
                    postfix = dbName.substring(1, 30);
                    // второй и третий символы заменяем


                    if (postfix.substring(1, 3).equals("$$") && prefix.equals("P") || prefix.equals("M")) {


                        if (postfix.substring(0, 1).equals("t")) {
                            postfix = "tbl" + postfix.substring(3);
                        } else {
                            postfix = "acs" + postfix.substring(3);
                        }
                        int i = searchAPFIndex(Id.Factory.loadFrom(postfix));
                        if (i == -1) {
                            postfix = "apf" + postfix.substring(3);
                            i = searchAPFIndexEx(Id.Factory.loadFrom(postfix));
                        } else {
                            postfix = "apf" + postfix.substring(3);
                        }

                        boolean mustRemoveColumn = i == -1;


                        if (!mustRemoveColumn) {
                            if (prefix.equals("M")) {
                                itemList.get(i).columnMode = col;
                            } else {
                                itemList.get(i).columnKey = col;

                                if (itemList.get(i).apf.getHeadId().getPrefix().equals(EDefinitionIdPrefix.DDS_TABLE)) {
                                    //DdsTableDef tbl = (DdsTableDef)apfList.get(i).getHead();
                                    for (DdsIndexDef ind : table.getIndices().get(EScope.LOCAL)) {
                                        if (ind.getColumnsInfo().size() == 1 && ind.getColumnsInfo().get(0).findColumn() != null && ind.getColumnsInfo().get(0).findColumn() == col) {
                                            //useAxisList.get(i).useIndex = true;
                                            itemList.get(i).index = ind;
                                            break;
                                        }
                                    }
                                    for (DdsReferenceDef ref : table.collectOutgoingReferences()) {
                                        if (ref.getColumnsInfo().size() == 1 && ref.getColumnsInfo().get(0).findChildColumn() == col) {
                                            itemList.get(i).ref = ref;
                                            break;
                                        }
                                    }
                                }
                            }
                        }

                        if (mustRemoveColumn) {
                            Id id = Id.Factory.loadFrom(postfix);
                            DdsAccessPartitionFamilyDef apf = module.getDdsApfSearcher().findById(id).get();
                            DdsTableDef tbl = null;
                            IEnumDef enm = null;
                            if (apf != null) {
                                Definition currDef = apf.findHead();
                                if (currDef != null) {
                                    if (currDef instanceof DdsTableDef) {
                                        tbl = (DdsTableDef) currDef;
                                    } else if (currDef instanceof IEnumDef) {
                                        enm = (IEnumDef) currDef;
                                    }

                                }
                            }

                            if (tbl != null || enm != null) {
                                if (tbl != null/* && tbl.getPrimaryKey().getColumnsInfo().size()==1*/) {// remove index
                                    DdsIndexDef index = null;
                                    for (DdsIndexDef ind : table.getIndices().get(EScope.LOCAL)) {
                                        if (ind.getColumnsInfo().size() == 1 && ind.getColumnsInfo().get(0).getColumn() == col) {
                                            index = ind;
                                            break;
                                        }
                                    }
                                    if (index != null) {
                                        if (!index.delete()) {
                                            throw new DefinitionError("Can't remove index "
                                                    + table + "(" + index.getName() + ")", this);
                                        }
                                    }
                                    //Remove reference

                                    for (DdsReferenceDef ref : table.collectOutgoingReferences()) {
                                        if (ref.getColumnsInfo().size() == 1 && ref.getColumnsInfo().get(0).findChildColumn() == col) {
                                            if (!ref.delete()) {
                                                throw new DefinitionError("Can't remove reference"
                                                        + table + "(" + ref.getName() + ")", this);
                                            }
                                            // break;
                                        }
                                    }
                                }
                            }
                            if (!col.delete()) {
                                throw new DefinitionError("Can't remove  column"
                                        + table + "(" + col.getName() + ")", this);
                            }
                        }
                    }
                }
            }
        }
    }

    private void getApfList() {
        Collection<Definition> apfList2 = RadixObjectsUtils.collectAllAround(module, DdsVisitorProviderFactory.newAccessPartitionFamilyProvider());
        if (apfList2 == null || apfList2.isEmpty()) {
            throw new DefinitionError("Access partition families not found", this);
        }
        itemList = new ArrayList<Item>(apfList2.size());


        Layer currLayer = this.module.getSegment().getLayer();
        for (Definition def : apfList2) {
            if (currLayer != def.getModule().getSegment().getLayer()) {
                continue;
            }
            //check dublicate
            DdsAccessPartitionFamilyDef apf = (DdsAccessPartitionFamilyDef) def;
            for (Item def2 : itemList) {
                if (apf.findHead().equals(def2.apf.findHead())) {
                    throw new DefinitionError("Dublicate access partition family head", apf);
                }
            }

            itemList.add(new Item((DdsAccessPartitionFamilyDef) def));
        }
        RadixObjectsUtils.sortByName(itemList);
    }

    private void getCDrcAccessAreaMode() {
        cDrcAccessAreaMode = null;
        Collection<Definition> enumList = RadixObjectsUtils.collectAllAround(module, VisitorProviderFactory.createEnumVisitorProvider());
        for (Definition def : enumList) {
            if (def instanceof IEnumDef && def.getName().equals(CDRCACCESSAREAMODENAME)) {
                cDrcAccessAreaMode = (IEnumDef) def;
                break;
            }
        }

        if (cDrcAccessAreaMode == null && false)// TODO search system const
        {
            throw new DefinitionError("System enum \"CDrcAccessAreaMode\" not found", this);
        }
    }

    private void getModel() {
        try {
            model = module.getModelManager().getModel();
        } catch (Exception e) {
            throw new DefinitionError("Model not found - " + e.toString(), this);
        }
    }

    private int searchAPFIndex(Id id) {
        int i = 0;
        for (Item item : itemList) {
            Definition def = item.apf.findHead();
            if (def != null && def.getId().equals(id)) {
                return i;
            }
            ++i;
        }
        return -1;
    }

    private int searchAPFIndexEx(Id id) {
        int i = 0;
        for (Item item : itemList) {
            if (item.apf.getId().equals(id)) {
                return i;
            }
            ++i;
        }
        return -1;
    }

    private void reCreateColumns(DdsTableDef tbl) {
        /*
         int size = this.apfList.size();
         useAxisList = new ArrayList<UseAxis>(size);
         for (int i = 0; i < size; i++) {
         useAxisList.add(new UseAxis());
         }
         */
        for (Item item : itemList) {
            item.columnKey = item.columnMode = null;
            item.index = null;
            item.ref = null;
        }
        removeColumns(tbl);
        createColumns(tbl);
    }

    private void reCreateColumns() //throws Exception
    {
        reCreateColumns(searcher.getClerk2Role());
        reCreateColumns(searcher.getClerkGroup2Role());
    }

    public static void refresh(DdsModule module) {

        SystemTablesBuilder stBuilder = new SystemTablesBuilder(module);
        stBuilder.getModel();
        stBuilder.getCDrcAccessAreaMode();
        stBuilder.searchSystemInstances();
        stBuilder.getApfList();
        stBuilder.reCreateColumns();


    }

    private void createColumns(DdsTableDef table) {


        pointList.clear();
        boolean findPoints = false;
        //int j = -1;

        for (Item item : itemList) {
            {
                DdsAccessPartitionFamilyDef apf = item.apf;
                //apfList
                //++j;
                //DdsAccessPartitionFamilyDef apf = (DdsAccessPartitionFamilyDef) def;
                Definition target = apf.findHead();

                if (target == null) {
                    throw new DefinitionError("Access partition family head not found", apf);
                }
                //DdsColumnDef columnMode;

                if (item.columnMode == null) {
                    item.columnMode = DdsColumnDef.Factory.newInstance("ap" + apf.getName() + "Mode");
                    table.getColumns().getLocal().add(item.columnMode);
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
                {
                    String oldId = apf.getId().toString();
                    item.columnMode.setDbName("M" + oldId.substring(0, 1).toUpperCase() + "$$" + oldId.substring(3));
                }


                DbNameUtils.updateAutoDbNames(item.columnMode);



                if (item.columnKey == null) {
                    item.columnKey = DdsColumnDef.Factory.newInstance("ap" + apf.getName() + "Key");
                    table.getColumns().getLocal().add(item.columnKey);
                } else {
                    item.columnKey.setName("ap" + apf.getName() + "Key");
                }

                item.columnKey.setAutoDbName(false);

                item.columnKey.setHidden(false);
                item.columnKey.setNotNull(false);
                {
                    String oldId = apf.getId().toString();
                    item.columnKey.setDbName("P" + oldId.substring(0, 1).toUpperCase() + "$$" + oldId.substring(3));
                }










                if (target instanceof DdsTableDef) {
                    DdsTableDef tbl = (DdsTableDef) target;
                    if (tbl.getPrimaryKey().getColumnsInfo().size() != 1) {
                        throw new DefinitionError("Incorrect primary key in access partition family table " + apf.getName(), apf);
                    }
                    DdsColumnDef parentColumn = tbl.getPrimaryKey().getColumnsInfo().get(0).getColumn();
                    if (parentColumn == null || parentColumn.getValType() != EValType.INT && parentColumn.getValType() != EValType.STR) {
                        throw new DefinitionError("Incorrect primary key in access partition family table " + apf.getName(), apf);
                    }
                    int maxLen = parentColumn.getLength();
                    item.columnKey.setValType(parentColumn.getValType());
                    item.columnKey.setLength(maxLen);
                    item.columnKey.setDbType(
                            parentColumn.getValType() == EValType.INT ? "NUMBER(" + String.valueOf(maxLen) + ",0)" : "VARCHAR2(" + String.valueOf(maxLen) + " char)");
                } else if (target instanceof IEnumDef) {
                    IEnumDef enumDef = (IEnumDef) target;
                    //module.
                    String dbType;
                    EValType type;
                    int maxLen;

                    EValType keyType = enumDef.getItemType();

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


            Definition target = item.apf.findHead();
            if (target instanceof DdsTableDef) {
                DdsTableDef tbl = (DdsTableDef) target;
                String goodName;
                if (item.index == null) {
                    goodName = item.apf.getName();
                    goodName = goodName.replace(' ', '_');
                    if (goodName.length() > 25) {
                        goodName = goodName.substring(0, 25);
                    }
                    goodName += "Index";

                    item.index = DdsIndexDef.Factory.newInstance(goodName);
                    item.index.getColumnsInfo().add(item.columnKey.getId(), EOrder.ASC);
                    table.getIndices().getLocal().add(item.index);
                }


                item.index.setAutoDbName(true);
                item.index.setGeneratedInDb(true);
                DbNameUtils.updateAutoDbNames(item.index);

                //if (!useAxisList.get(j).useRef)
                {

                    if (item.ref == null) {
                        item.ref = DdsReferenceDef.Factory.newInstance();
                        model.getReferences().add(item.ref);
                    }


                    item.ref.setType(EType.LINK);
                    item.ref.setAutoDbName(true);
                    item.ref.setGeneratedInDb(true);
                    item.ref.setDeleteMode(EDeleteMode.CASCADE);

                    item.ref.setParentUnuqueConstraintId(tbl.getPrimaryKey().getUniqueConstraint().getId());

                    boolean findTable = false;
                    for (DdsTableDef tb : model.getTables()) {
                        if (tb.getId().equals(tbl.getId())) {
                            findTable = true;
                            break;
                        }
                    }
                    if (!findTable) {
                        DdsExtTableDef exTable = null;
                        for (DdsExtTableDef ext : model.getExtTables()) {
                            if (ext.findTable() == tbl) {
                                exTable = ext;
                                break;
                            }
                        }
                        if (exTable == null) {
                            exTable = DdsExtTableDef.Factory.newInstance(tbl);
                            model.getExtTables().add(exTable);
                            exTable.setTableId(tbl.getId());

                            if (!findPoints) {
                                findPoints = true;
                                module.visitChildren(searcher.getNoneVisitor(), new IPlacementVisitorProvider());
                            }
                            int px = table.getPlacement().getPosX();
                            int py = table.getPlacement().getPosY() - 10;
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
                                    for (int g = 0; g < pointList.size(); g++) {
                                        Point p = pointList.get(g);
                                        if (px1 <= p.getX()
                                                && px2 >= p.getX()
                                                && py1 <= p.getY()
                                                && py2 >= p.getY() /*
                                                 ||
                                                 px <= p.getX() &&
                                                 px + ConjecturalRectangleSizeX >= p.getX() &&
                                                 py <= p.getY() &&
                                                 p.getY()+ConjecturalRectangleSizeY  <= py + ConjecturalRectangleSizeY
                                                 ||
                                                 px <=  p.getX()+ConjecturalRectangleSizeX &&
                                                 px + ConjecturalRectangleSizeX >= p.getX() &&
                                                 py <= p.getY() &&
                                                 p.getY() <= py + ConjecturalRectangleSizeY
                                                 ||
                                                 px <= p.getX()+ConjecturalRectangleSizeX &&
                                                 px + ConjecturalRectangleSizeX >= p.getX() &&
                                                 py <= p.getY() &&
                                                 p.getY()+ConjecturalRectangleSizeY  <= py + ConjecturalRectangleSizeY
                                                 * */) {
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
                        }
                        item.ref.setExtParentTableId(exTable.getId());
                    }
                    item.ref.setChildTableId(table.getId());
                    item.ref.setParentTableId(tbl.getId());
                    item.ref.getColumnsInfo().clear();
                    item.ref.getColumnsInfo().add(item.columnKey, tbl.getPrimaryKey().getColumnsInfo().get(0).getColumn());
                    //correct name and dbname for ref



                    goodName = "_" + table.getName() + "_" + tbl.getName();
                    goodName = goodName.replace(' ', '_');
                    item.ref.setName(goodName);

                    DbNameUtils.updateAutoDbNames(item.ref);
                }
            }
        }

    }
}
