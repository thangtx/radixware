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
package org.radixware.kernel.common.defs.dds;

import java.util.Collections;
import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IEnumDef;
import org.radixware.kernel.common.defs.IParameterDef;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.enums.EOptionMode;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.repository.DbOptionValue;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Layer.DatabaseOption;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqlscript.parser.SQLPreprocessor;
import org.radixware.kernel.common.sqml.ISqmlSchema;
import org.radixware.kernel.common.sqml.tags.IfParamTag;
import org.radixware.kernel.common.sqml.tags.PropSqlNameTag.EOwnerType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.sqml.ISqmlEnvironment;
import org.radixware.kernel.common.sqml.ISqmlProperty;
import org.radixware.kernel.common.sqml.tags.ElseIfTag;
import org.radixware.kernel.common.sqml.tags.EndIfTag;
import org.radixware.kernel.common.sqml.tags.TargetDbPreprocessorTag;

/**
 * SQML Translator Environment for DDS definition.
 *
 */
class DdsSqmlEnvironment implements ISqmlEnvironment {

    private final DdsDefinition definition;

    protected DdsSqmlEnvironment(DdsDefinition definition) {
        this.definition = definition;
    }

    @Override
    public Definition findChildPropertiesOwner() {
        return null;
    }

    @Override
    public ISqmlProperty findChildPropertyById(Id propertyId) {
        return null;
    }

    @Override
    public IParameterDef findParameterById(Id paramId) {
        return null;
    }

    @Override
    public Definition findParentPropertiesOwner() {
        return null;
    }

    @Override
    public ISqmlProperty findParentPropertyById(Id propertyId) {
        return null;
    }

    @Override
    public Definition findDefinitionByIds(Id[] ids) {
        return findDefinitionByIds(ids, false);
    }

    @Override
    public Definition findDefinitionByIds(Id[] ids, boolean global) {
        DdsPath path = new DdsPath(ids);
        return global ? path.resolveGlobal(definition).get() : path.resolve(definition, ids).get();
    }

    @Override
    public IEnumDef findEnumById(Id enumId) {
        final DdsModule module = definition.getModule();
        if (module != null) {
            return module.findEnumById(enumId);
        } else {
            return null;
        }
    }

    @Override
    public DdsFunctionDef findFunctionById(Id funcId) {
        final DdsModule module = definition.getModule();
        if (module != null) {
            return module.getDdsFunctionSearcher().findById(funcId).get();
        } else {
            return null;
        }
    }

    @Override
    public ISqmlProperty findPropertyById(Id propOwnerId, Id propertyId) {
        final DdsModule module = definition.getModule();
        if (module == null) {
            return null;
        }
        final DdsTableDef table = module.getDdsTableSearcher().findById(propOwnerId).get();
        if (table == null) {
            return null;
        }

        final DdsColumnDef column = table.getColumns().findById(propertyId, EScope.ALL).get();
        return column;
    }

    @Override
    public DdsReferenceDef findReferenceById(Id refId) {
        final DdsModule module = definition.getModule();
        if (module != null) {
            return module.getDdsReferenceSearcher().findById(refId).get();
        } else {
            return null;
        }
    }

    @Override
    public DdsSequenceDef findSequenceById(Id sequenceId) {
        final DdsModule module = definition.getModule();
        if (module != null) {
            return module.getDdsSequenceSearcher().findById(sequenceId).get();
        } else {
            return null;
        }
    }

    @Override
    public DdsTableDef findTableById(Id tableId) {
        final DdsModule module = definition.getModule();
        if (module != null) {
            return module.getDdsTableSearcher().findById(tableId).get();
        } else {
            return null;
        }
    }

    @Override
    public ISqmlProperty findThisPropertyById(Id propertyId) {
        final DdsTableDef thisTable = findThisTable();
        if (thisTable != null) {
            final DdsColumnDef column = thisTable.getColumns().findById(propertyId, EScope.LOCAL_AND_OVERWRITE).get();
            return column;
        } else {
            return null;
        }
    }

    @Override
    public List<Definition> findThisPropertiesOwner() {
        DdsTableDef table = findThisTable();
        if (table == null) {
            return Collections.emptyList();
        } else {
            return Collections.singletonList((Definition) table);
        }
    }

    @Override
    public DdsTableDef findThisTable() {
        if (definition instanceof DdsTableDef) {
            return (DdsTableDef) definition;
        } else if (definition instanceof IDdsTableItemDef) {
            IDdsTableItemDef tableItem = (IDdsTableItemDef) definition;
            return tableItem.getOwnerTable();
        } else if (definition instanceof DdsCheckConstraintDef) {
            DdsCheckConstraintDef checkConstraint = (DdsCheckConstraintDef) definition;
            return checkConstraint.getOwnerColumn() == null ? null : checkConstraint.getOwnerColumn().getOwnerTable();
        } else if (definition instanceof DdsUniqueConstraintDef) {
            DdsUniqueConstraintDef uniqueConstraint = (DdsUniqueConstraintDef) definition;
            return uniqueConstraint.getOwnerIndex() == null ? null : uniqueConstraint.getOwnerIndex().getOwnerTable();
        } else {
            return null;
        }
    }

    @Override
    public DdsFunctionDef findThisFunction() {
        if (definition instanceof DdsFunctionDef) {
            return (DdsFunctionDef) definition;
        } else if (definition instanceof DdsParameterDef) {
            return ((DdsParameterDef) definition).getOwnerFunction();
        } else if (definition instanceof DdsPrototypeDef) {
            return ((DdsPrototypeDef) definition).findFunction();
        } else {
            return null;
        }
    }

    @Override
    public ERepositorySegmentType getSegmentType() {
        return ERepositorySegmentType.DDS;
    }

//    @Override
//    public boolean isDbModificationAllowed() {
//        if (definition instanceof DdsTriggerDef) {
//            return true;
//        }
//        if (definition instanceof DdsModelDef) {
//            // Begin/End scripts
//            return true;
//        }
//        if (definition instanceof DdsPlSqlObjectItemDef) {
//            return true;
//            // unable to return false for WNDS functions, so, its cleared DBMS
//            //final DdsFunctionDef func = (DdsFunctionDef) definition;
//            // allowed only in functions that modified database
//            //return !func.getPurityLevel().isWNDS();
//        }
//        if (definition instanceof DdsPlSqlObjectDef) {
//            // calculated full sqml
//            return true;
//        }
//        return false;
//    }
    private final class PropSqlNameTagProvider extends VisitorProvider {

        private final EOwnerType ownerType;

        public PropSqlNameTagProvider(final EOwnerType ownerType) {
            this.ownerType = ownerType;
        }

        @Override
        public boolean isTarget(final RadixObject radixObject) {
            if (radixObject instanceof DdsColumnDef) {
                final DdsColumnDef column = (DdsColumnDef) radixObject;
                if (!column.isGeneratedInDb()) {
                    return false;
                }

                if (definition instanceof DdsColumnDef) {
                    final DdsColumnDef thisColumn = (DdsColumnDef) definition;
                    if (thisColumn.isExpression()) {
                        // enable only nonvirtual columns of current table in virtual column query.
                        return column.getContainer() == thisColumn.getContainer() && !column.isExpression();
                    }
                }

                switch (ownerType) {
                    case NONE:
                    case TABLE:
                        return true;
                    case THIS:
                        final DdsTableDef thisTable = findThisTable();
                        return (thisTable != null && thisTable.getColumns().contains(column, EScope.ALL));
                }
            }

            return false;
        }
    }

    @Override
    public VisitorProvider getPropProvider(final EOwnerType ownerType) {
        return new PropSqlNameTagProvider(ownerType);
    }

    @Override
    public void printTagCondition(CodePrinter cp, IfParamTag ifParamTag) {
        cp.printError();
    }

    @Override
    public void printTargetDbPreprocessorTag(CodePrinter cp, TargetDbPreprocessorTag tag) {
        cp.print("#IF ");
        cp.print(DatabaseOption.TARGET_DB_TYPE);
        cp.print(" == \"");
        cp.print(tag.getDbTypeName());
        cp.print("\"");
        if (tag.isCheckVersion()) {
            cp.print(" AND ");
            cp.print(DatabaseOption.TARGET_DB_VERSION);
            cp.print(" ");
            cp.print(tag.getVersionOperator().getValue());
            cp.print(" ");
            cp.print(tag.getDbVersion());
        }
        if (tag.isCheckOptions()) {
            for (DbOptionValue dbOpt : tag.getDbOptions()) {
                cp.print(" AND ");
                if (dbOpt.getMode()== EOptionMode.DISABLED) {
                    cp.print("!");
                }
                cp.print(SQLPreprocessor.FUNC_IS_ENABLED);
                cp.print("(\"");
                cp.print(dbOpt.getOptionName().replace("\\", "\\\\"));
                cp.print("\")");
            }
        }
        cp.print(" THEN");
    }

    @Override
    public void printElse(CodePrinter cp, ElseIfTag elseIfTag) {
        cp.print("#ELSE");
    }

    @Override
    public void printEndIf(CodePrinter cp, EndIfTag endIfTag) {
        cp.print("#ENDIF");
    }

    @Override
    public ISqmlSchema findSchemaById(Id schemaId) {
        return null;
    }

    @Override
    public RadixObject getContext() {
        return definition;
    }
}
