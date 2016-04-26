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

import java.util.List;
import static org.radixware.kernel.common.builder.check.common.RadixObjectChecker.error;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsPrimaryKeyDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.utils.DbTypeUtils;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.dds.DdsExtTableDef;
import org.radixware.kernel.common.defs.dds.DdsExtendableDefinitions;
import org.radixware.kernel.common.enums.EDdsConstraintDbOption;
import org.radixware.kernel.common.enums.EDeleteMode;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;

@RadixObjectCheckerRegistration
public class DdsReferenceChecker<T extends DdsReferenceDef> extends DdsConstraintChecker<T> {

    public DdsReferenceChecker() {
    }

    private static boolean isValidColumnTypeForReference(EValType valType) {
        return valType == EValType.INT
                || valType == EValType.STR
                || valType == EValType.DATE_TIME
                || valType == EValType.NUM
                || valType == EValType.CHAR
                || valType == EValType.BOOL;
    }

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return DdsReferenceDef.class;
    }

    @Override
    public void check(T reference, IProblemHandler problemHandler) {
        super.check(reference, problemHandler);

        checkForDbNameDuplication(reference, problemHandler);

        // links
        DdsTableDef childTable = reference.findChildTable(reference);
        if (childTable == null) {
            error(reference, problemHandler, "Child table not found: #" + String.valueOf(reference.getChildTableId()));
        }

        DdsTableDef parentTable = reference.findParentTable(reference);
        if (parentTable == null) {
            error(reference, problemHandler, "Parent table not found: #" + String.valueOf(reference.getParentTableId()));
        } else {
            if (parentTable.isDetailTable()) {
                error(reference, problemHandler, "Detail table " + parentTable.getQualifiedName() + " can not be used as reference parent table");
            }
        }

        if (reference.getExtChildTableId() != null && reference.findExtChildTable() == null) {
            error(reference, problemHandler, "External child table not found");
        }

        if (reference.getExtParentTableId() != null) {
            DdsExtTableDef extParent = reference.findExtParentTable();
            if (extParent == null) {
                error(reference, problemHandler, "External parent table not found");
            } else {
                if (extParent.findTable() != null && extParent.findTable().isDetailTable()) {
                    error(reference, problemHandler, "Detail table " + extParent.findTable().getQualifiedName() + " can not be used as reference parent table");
                }
            }
        }

        if (parentTable == null || childTable == null) {
            return;
        }

        // type
        if (reference.getType() == null) {
            error(reference, problemHandler, "Reference type doesn't defined");
        }

        // global temporaty
        if (parentTable.isGlobalTemporary()) {
            error(reference, problemHandler, "Reference to global temporary table");
        }

        // master detail
        if (DdsReferenceDef.EType.MASTER_DETAIL.equals(reference.getType())) {
            for (DdsIndexDef.ColumnInfo columnInfo : childTable.getPrimaryKey().getColumnsInfo()) {
                DdsColumnDef pkColumn = columnInfo.findColumn();
                if (pkColumn != null && pkColumn.getSequenceId() != null) {
                    error(reference, problemHandler, "Primary key column '" + pkColumn.getName() + "' of master-detail child table has sequence");
                }
            }

            // deep master details
            for (DdsReferenceDef topReference : parentTable.collectOutgoingReferences()) {
                if (DdsReferenceDef.EType.MASTER_DETAIL.equals(topReference.getType())) {
                    error(reference, problemHandler, "Deep master detail");
                }
            }

            if (!(reference.findParentIndex() instanceof DdsPrimaryKeyDef)) {
                error(reference, problemHandler, "Master-detail reference must be on primary key");
            }

            if (reference.getDeleteMode() != EDeleteMode.CASCADE) {
                error(reference, problemHandler, "Master-detail reference must be delete cascade");
            }
        }

        // column structure
        int size = reference.getColumnsInfo().size();

        DdsIndexDef index = reference.findParentIndex();
        if (index != null) {
            boolean corresponded = true;
            if (index.getColumnsInfo().size() == size) {
                for (int i = 0; i < size; i++) {
                    DdsIndexDef.ColumnInfo indexColumnInfo = index.getColumnsInfo().get(i);

                    boolean isIndexColumn = false;
                    for (int j = 0; j < size; j++) {
                        DdsReferenceDef.ColumnsInfoItem referenceColumnsInfo = reference.getColumnsInfo().get(j);
                        if (Utils.equals(indexColumnInfo.getColumnId(), referenceColumnsInfo.getParentColumnId())) {
                            isIndexColumn = true;
                            break;
                        }
                    }

                    if (!isIndexColumn) {
                        corresponded = false;
                        break;
                    }
                }
            } else {
                corresponded = false;
            }

            if (!corresponded) {
                error(reference, problemHandler, "Reference columns structure does not correspond to index columns structure");
            }
            if (reference.isGeneratedInDb() && !index.isGeneratedInDb()) {
                error(reference, problemHandler, "Reference unique constraint must be generated in database.");
            }
        }
//        else {
//                if (size > 0) {
//                    List<DdsIndexDef> parentIndices = parentTable.getIndices().get(ExtendableDefinitions.EScope.ALL);
//                    DdsReferenceDef.ColumnsInfoItems columnsInfoItems = reference.getColumnsInfo();
//                    
//                    boolean isUnique = true;
//                    boolean columnNotFound = false;
//                    
//                    DdsIndexDef pk = parentTable.getPrimaryKey();
//                    DdsIndexDef.ColumnsInfo pkColumnsInfo = pk.getColumnsInfo();
//                    if (pkColumnsInfo.size() == size){
//                        for (int i = 0; i < pkColumnsInfo.size(); i++){
//                            DdsIndexDef.ColumnInfo columnInfo = pkColumnsInfo.get(i);
//                            
//                            boolean isReferenceColumn = false;
//                            for (int j = 0; j < size; j++){
//                                DdsReferenceDef.ColumnsInfoItem referenceColumnsInfo = columnsInfoItems.get(j);
//                                DdsColumnDef columnDef = referenceColumnsInfo.findParentColumn();
//                                            
//                                if (columnDef != null){
//                                    if (Utils.equals(columnInfo.getColumn() , columnDef)){
//                                        isReferenceColumn = true;
//                                        break;
//                                    }
//                                }  else {
//                                        columnNotFound = true;
//                                        break;
//                                }           
//                            }
//                                        
//                            if (!isReferenceColumn){
//                                isUnique = false;
//                                break;       
//                            }
//                        }  
//                    } else {
//                        isUnique = false;
//                    }
//                    
//                    if (!isUnique && !columnNotFound){
//                        for (DdsIndexDef ddsIndexDef: parentIndices){
//                            if (columnNotFound) break;
//
//                            if (ddsIndexDef.isUnique()){
//                                DdsIndexDef.ColumnsInfo indexColumnsInfo = ddsIndexDef.getColumnsInfo();
//
//                                if (size == indexColumnsInfo.size()){
//                                    boolean corresponded = true;
//                                    for (int i = 0; i < indexColumnsInfo.size(); i++){
//                                        if (columnNotFound) break;
//                                        DdsIndexDef.ColumnInfo columnInfo = indexColumnsInfo.get(i);
//
//                                        boolean isReferenceColumn = false;
//                                        for (int j = 0; j < size; j++){
//                                            DdsReferenceDef.ColumnsInfoItem referenceColumnsInfo = columnsInfoItems.get(j);
//                                            DdsColumnDef columnDef = referenceColumnsInfo.findParentColumn();
//
//                                            if (columnDef != null){
//                                                if (Utils.equals(columnInfo.getColumn() , columnDef)){
//                                                    isReferenceColumn = true;
//                                                    break;
//                                                }
//                                            }  else {
//                                                    columnNotFound = true;
//                                                    break;
//                                            }           
//                                        }
//
//                                        if (!isReferenceColumn){
//                                            corresponded = false;
//                                            break;
//                                        }
//                                    }
//
//                                    if (corresponded){
//                                        isUnique = true;
//                                        break;
//                                    }
//                                }        
//                            }
//                        }
//                    }
//                    if (columnNotFound){
//                        error(reference, problemHandler, "Parent column not found");
//                    } else {
////                        if (!isUnique) {
////                            final String message = "Parent column is not unique";
////                            error(reference, problemHandler, message);
////                        }
//                    }
//                } else {
//                    error(reference, problemHandler, "Reference must be based on at least one column");
//                }    
//        }

        if (size == 0) {
            error(reference, problemHandler, "Reference must be based on at least one column");
        }
        for (int i = 0; i < size; i++) {
            DdsReferenceDef.ColumnsInfoItem item = reference.getColumnsInfo().get(i);
            Id childColumnId = item.getChildColumnId();
            Id parentColumnId = item.getParentColumnId();

            DdsColumnDef childColumn = item.findChildColumn();
            DdsColumnDef parentColumn = item.findParentColumn();
            if (childColumn != null && parentColumn != null) {

                if (!childColumn.isGeneratedInDb() && reference.isGeneratedInDb()) {
                    error(reference, problemHandler, "Child column '" + childColumn.getName() + "' is not column of database");
                }

                if (!parentColumn.isGeneratedInDb() && reference.isGeneratedInDb()) {
                    error(reference, problemHandler, "Parent column '" + parentColumn.getName() + "' is not column of database");
                }

                if (reference.isGeneratedInDb() && !DbTypeUtils.isDbTypeEquals(childColumn.getDbType(), parentColumn.getDbType())) {
                    error(reference, problemHandler, "The type of the reference child column '" + childColumn.getName() + "' doesn't match to the type of parent column '" + parentColumn.getName() + "'");
                }

                if (!isValidColumnTypeForReference(childColumn.getValType())) {
                    error(reference, problemHandler, "Illegal type of reference child column '" + childColumn.getName() + "'");
                }

                if (!isValidColumnTypeForReference(parentColumn.getValType())) {
                    error(reference, problemHandler, "Illegal type of reference parent column '" + parentColumn.getName() + "'");
                }

                for (int j = i + 1; j < size; j++) {
                    if (Utils.equals(reference.getColumnsInfo().get(j).getChildColumnId(), childColumnId)) {
                        error(reference, problemHandler, "Child column '" + childColumn.getName() + "' duplicated in reference");
                    }
                    if (Utils.equals(reference.getColumnsInfo().get(j).getParentColumnId(), parentColumnId)) {
                        error(reference, problemHandler, "Parent column '" + parentColumn.getName() + "' duplicated in reference");
                    }
                }

                if (childColumn.isNotNull() && reference.getDeleteMode() == EDeleteMode.SET_NULL) {
                    error(reference, problemHandler, "Delete mode 'set null' assigned for not null column '" + childColumn.getName() + "'");
                }
            } else {
                error(reference, problemHandler, "Erronerous structure of reference columns");
                break;
            }
        }

        // master detail
        if (reference.getType() == DdsReferenceDef.EType.MASTER_DETAIL) {
            final DdsPrimaryKeyDef parentPk = parentTable.getPrimaryKey();
            final DdsPrimaryKeyDef childPk = childTable.getPrimaryKey();
            boolean equals = (childPk.getColumnsInfo().size() == size && parentPk.getColumnsInfo().size() == size);
            if (equals) {
                int i = 0;
                for (DdsReferenceDef.ColumnsInfoItem item : reference.getColumnsInfo()) {
                    if (item.getChildColumnId() != childPk.getColumnsInfo().get(i).getColumnId()
                            || item.getParentColumnId() != parentPk.getColumnsInfo().get(i).getColumnId()) {
                        equals = false;
                        break;
                    }
                    i++;
                }
            }
            if (!equals) {
                error(reference, problemHandler, "Master-Detail PID structure must be the same"); // for audit
            }
        }

        final boolean isOracleFieturesWorks = !reference.getDbOptions().contains(EDdsConstraintDbOption.DISABLE) && reference.isGeneratedInDb();

        if (isOracleFieturesWorks) {
            if (reference.getDeleteMode() == EDeleteMode.NONE) {
                warning(reference, problemHandler, "Invalid option value for 'on parent deletion' option. Must be 'Set NULL' or 'Delete cascade' or 'Prohibit if children exist', because reference is enabled and generated in database");
            }
        } else {
            final String description = reference.isGeneratedInDb() ? " is disabled" : " is not generated in database";
            if (reference.getDeleteMode() == EDeleteMode.CASCADE || reference.getDeleteMode() == EDeleteMode.SET_NULL) {
                warning(reference, problemHandler, "Invalid option value for 'on parent deletion' option. Must be 'Do not modify children', because reference" + description);
            } else {
                if (reference.getDeleteMode() == EDeleteMode.RESTRICT && reference.getRestrictCheckMode() != DdsReferenceDef.ERestrictCheckMode.ALWAYS) {
                    warning(reference, problemHandler, "EAS check of the existence of children could be useful, because the reference" + description);
                }
            }

        }
    }
}
