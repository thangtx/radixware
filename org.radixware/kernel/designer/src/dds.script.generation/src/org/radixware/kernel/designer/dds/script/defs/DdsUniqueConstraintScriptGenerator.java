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
package org.radixware.kernel.designer.dds.script.defs;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsUserPropertyDef;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.IAdsTypedObject;
import org.radixware.kernel.common.defs.ads.type.ObjectType;
import org.radixware.kernel.common.defs.ads.type.ParentRefType;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsPrimaryKeyDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.DdsUniqueConstraintDef;
import org.radixware.kernel.common.defs.dds.DdsViewDef;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.dds.script.IScriptGenerationHandler;

public class DdsUniqueConstraintScriptGenerator extends DdsConstraintScriptGenerator<DdsUniqueConstraintDef> {

    private static final class UserPropsRequestor {

        private static final Id USER_FUNC_TABLE_ID = Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM");
        private List<AdsUserPropertyDef> allProps = null;

        UserPropsRequestor() {
        }

        public Set<AdsUserPropertyDef> getRefsFrom(DdsTableDef ownerTable) {
            final Branch branch = ownerTable.getBranch();
            if (branch == null) {
                return Collections.emptySet();
            }
            final Id tableId = ownerTable.getId();
            final Set<AdsUserPropertyDef> props = new HashSet<>();
            if (allProps == null) {
                final List<AdsUserPropertyDef> registry = new LinkedList<>();
                branch.visit(new IVisitor() {
                    @Override
                    public void accept(RadixObject radixObject) {
                        if (radixObject instanceof AdsUserPropertyDef) {
                            AdsUserPropertyDef prop = (AdsUserPropertyDef) radixObject;
                            registry.add(prop);
                            final DdsTableDef table = getOwnerTableDef(prop);
                            if (table != null && table.getId().equals(tableId)) {
                                props.add(prop);
                            }
                        }
                    }
                }, VisitorProviderFactory.createDefaultVisitorProvider());
                allProps = registry;
            } else {
                for (AdsUserPropertyDef prop : allProps) {
                    final DdsTableDef table = getOwnerTableDef(prop);
                    if (table != null && table.getId().equals(tableId)) {
                        props.add(prop);
                    }
                }
            }

            return props;
        }

        public Set<AdsUserPropertyDef> getRefsTo(final DdsTableDef targetTable) {
            final Branch branch = targetTable.getBranch();
            if (branch == null) {
                return Collections.emptySet();
            }
            final Id tableId = targetTable.getId();
            final Set<AdsUserPropertyDef> props = new HashSet<>();
            if (allProps == null) {
                final List<AdsUserPropertyDef> registry = new LinkedList<>();
                branch.visit(new IVisitor() {
                    @Override
                    public void accept(RadixObject radixObject) {
                        if (radixObject instanceof AdsUserPropertyDef) {
                            AdsUserPropertyDef prop = (AdsUserPropertyDef) radixObject;
                            registry.add(prop);
                            final DdsTableDef table = getTargetTableDef(prop);
                            if (table != null && table.getId().equals(tableId)) {
                                props.add(prop);
                            }
                        }
                    }
                }, VisitorProviderFactory.createDefaultVisitorProvider());
                allProps = registry;
            } else {
                for (AdsUserPropertyDef prop : allProps) {
                    final DdsTableDef table = getTargetTableDef(prop);
                    if (table != null && table.getId().equals(tableId)) {
                        props.add(prop);
                    }
                }
            }
            return props;
        }

        public static DdsTableDef getTargetTableDef(AdsUserPropertyDef prop) {
            final IAdsTypedObject typedObject = prop.getTypedObject();
            if (typedObject != null) {
                final AdsTypeDeclaration typeDecl = typedObject.getType();
                if (typeDecl != null) {
                    final AdsType type = typeDecl.resolve(prop).get();
                    final DdsTableDef tableDef;
                    if (type instanceof ParentRefType) {
                        tableDef = ((ParentRefType) type).getSource().findTable(prop);
                    } else if (type instanceof ObjectType) {
                        tableDef = ((ObjectType) type).getSource().findTable(prop);
                    } else {
                        tableDef = null;
                    }
                    return tableDef;
                }
            }
            return null;
        }

        private static DdsTableDef getOwnerTableDef(AdsUserPropertyDef prop) {
            return prop.findTable();
        }

        public static boolean isUserFuncDef(AdsUserPropertyDef prop) {
            final EValType typeId = prop.getValue().getType().getTypeId();
            if (typeId != EValType.OBJECT) {
                return false;
            }

            final DdsTableDef table = getTargetTableDef(prop);
            return table != null && table.getId() == USER_FUNC_TABLE_ID;
        }
    }

    protected DdsUniqueConstraintScriptGenerator() {
    }

    @Override
    protected DdsTableDef findOwnerTable(DdsUniqueConstraintDef constraint) {
        DdsIndexDef idx = constraint.getOwnerIndex();
        DdsTableDef table = idx.getOwnerTable();
        return table;
    }

    // for the future:
    // alter table drop unique(column1, column2) cascade drop index
    // alter table drop primary key cascade drop index
    @Override
    public boolean isModifiedToDrop(DdsUniqueConstraintDef oldUc, DdsUniqueConstraintDef newUc) {
        DdsIndexDef oldIdx = oldUc.getOwnerIndex();
        DdsIndexDef newIdx = newUc.getOwnerIndex();
        DdsIndexScriptGenerator indexScriptGenerator = DdsIndexScriptGenerator.Factory.newInstance();

        if (indexScriptGenerator.isModifiedToDrop(oldIdx, newIdx)) {
            return true;
        }

        return super.isModifiedToDrop(oldUc, newUc);
    }

    @Override
    public void getRunRoleScript(CodePrinter printer, DdsUniqueConstraintDef definition) {
    }

    @Override
    public void getCreateScript(CodePrinter cp, DdsUniqueConstraintDef uc, IScriptGenerationHandler handler) {
        if (handler != null) {
            handler.onGenerationStarted(uc, cp);
        }

        DdsIndexDef idx = uc.getOwnerIndex();
        DdsTableDef table = idx.getOwnerTable();
        String tableDbName = table.getDbName();

        cp.print("alter ");
        if (table instanceof DdsViewDef) {
            cp.print("view ");
        } else {
            cp.print("table ");
        }

        cp.print(tableDbName).print(" add constraint ").print(uc.getDbName()).print(idx instanceof DdsPrimaryKeyDef ? " primary key (" : " unique (");

        boolean uniqueFlag = false;
        for (DdsIndexDef.ColumnInfo columnInfo : idx.getColumnsInfo()) {
            if (uniqueFlag) {
                cp.print(", ");
            } else {
                uniqueFlag = true;
            }
            DdsColumnDef column = columnInfo.getColumn();
            cp.print(column.getDbName());
        }

        cp.print(')');

        getCreateDbOptionsScript(cp, uc.getDbOptions());

        cp.printCommandSeparator();
    }

    @Override
    public void getReCreateScript(CodePrinter printer, DdsUniqueConstraintDef definition, boolean storeData) {
    }

    @Override
    public void getEnableDisableScript(CodePrinter cp, DdsUniqueConstraintDef definition, boolean enable) {
    }
    
    private final static Map<EValType, String> type2UpValTable = new HashMap<>();

    static {
        type2UpValTable.put(EValType.DATE_TIME, "UPVALDATETIME");

        type2UpValTable.put(EValType.INT, "UPVALNUM");
        type2UpValTable.put(EValType.BOOL, "UPVALNUM");
        type2UpValTable.put(EValType.NUM, "UPVALNUM");

        type2UpValTable.put(EValType.STR, "UPVALSTR");
        type2UpValTable.put(EValType.CHAR, "UPVALSTR");

        type2UpValTable.put(EValType.PARENT_REF, "UPVALREF");
        type2UpValTable.put(EValType.OBJECT, "UPVALREF");

        type2UpValTable.put(EValType.BIN, "UPVALRAW");

        type2UpValTable.put(EValType.BLOB, "UPVALBLOB");

        type2UpValTable.put(EValType.CLOB, "UPVALCLOB");
        type2UpValTable.put(EValType.ARR_BLOB, "UPVALCLOB");
        type2UpValTable.put(EValType.ARR_INT, "UPVALCLOB");
        type2UpValTable.put(EValType.ARR_CLOB, "UPVALCLOB");
        type2UpValTable.put(EValType.ARR_BIN, "UPVALCLOB");
        type2UpValTable.put(EValType.ARR_REF, "UPVALCLOB");
        type2UpValTable.put(EValType.ARR_CHAR, "UPVALCLOB");
        type2UpValTable.put(EValType.ARR_DATE_TIME, "UPVALCLOB");
        type2UpValTable.put(EValType.ARR_STR, "UPVALCLOB");
        type2UpValTable.put(EValType.ARR_BOOL, "UPVALCLOB");
        type2UpValTable.put(EValType.ARR_NUM, "UPVALCLOB");
    }
    private boolean showHeader = true;

    private void printPkModifyScriptHeader(CodePrinter cp, DdsTableDef table) {
        if (showHeader) {
            cp.println("-- !!! PRIMARY KEY FOR TABLE " + table.getDbName() + " WAS CHANGED, PLEASE UPDATE REFERENCE VALUES").println();
            showHeader = false;
        }
    }

    private void printPkModifyScriptTrailer(CodePrinter cp) {
        if (!showHeader) {
            cp.println("-- !!! PRIMARY KEY UPDATE REFERENCE VALUES FINISHED").println();
            showHeader = true;
        }
    }

    public void afterModifyPkScript(CodePrinter cp, DdsPrimaryKeyDef pk) {
        DdsTableDef table = pk.getOwnerTable();
        showHeader = true;

        boolean isUserFunc = false;
        UserPropsRequestor requestor = new UserPropsRequestor();
        for (AdsUserPropertyDef prop : requestor.getRefsFrom(table)) {
            EValType typeId = prop.getValue().getType().getTypeId();
            String tableName = type2UpValTable.get(typeId);
            if (tableName == null) {
                continue;
            }

            if (UserPropsRequestor.isUserFuncDef(prop)) {
                if (!isUserFunc) {
                    printPkModifyScriptHeader(cp, table);
                    isUserFunc = true;
                    cp.println("alter table RDX_USERFUNC drop constraint FK_RDX_USERFUNC_UPVALREF");
                    cp.println("/");
                    cp.println();
                }

                printPkModifyScriptHeader(cp, table);
                cp.println("update RDX_USERFUNC set UPOWNERPID = UPOWNERPID /* modify this query for correct PID conversion */ where UPDEFID = '" + prop.getId() + "' and UPOWNERENTITYID = '" + table.getId() + "'");
                cp.println("/");
                cp.println();
            }

            printPkModifyScriptHeader(cp, table);
            cp.println("update RDX_" + tableName + " set OWNERPID = OWNERPID /* modify this query for correct PID conversion */ where DEFID = '" + prop.getId() + "' and OWNERENTITYID = '" + table.getId() + "'");
            cp.println("/");
            cp.println();
        }
        if (isUserFunc) {
            cp.println("alter table RDX_USERFUNC add constraint FK_RDX_USERFUNC_UPVALREF foreign key (UPDEFID, UPOWNERENTITYID, UPOWNERPID) references RDX_UPVALREF (DEFID, OWNERENTITYID, OWNERPID) on delete cascade");
            cp.println("/");
            cp.println();
        }

        for (AdsUserPropertyDef prop : requestor.getRefsTo(table)) {
            DdsTableDef ownerTable = UserPropsRequestor.getOwnerTableDef(prop);
            if (ownerTable == null) {
                continue;
            }

            printPkModifyScriptHeader(cp, table);
            cp.println("update RDX_UPVALREF set VAL = VAL /* modify this query for correct PID conversion */ where DEFID = '" + prop.getId() + "' and OWNERENTITYID = '" + ownerTable.getId() + "'");
            cp.println("/");
            cp.println();
        }

        printPkModifyScriptTrailer(cp);
    }

    public static final class Factory {

        private Factory() {
        }

        public static DdsUniqueConstraintScriptGenerator newInstance() {
            return new DdsUniqueConstraintScriptGenerator();
        }
    }
}
