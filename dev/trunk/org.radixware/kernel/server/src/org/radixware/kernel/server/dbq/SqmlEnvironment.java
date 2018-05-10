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
package org.radixware.kernel.server.dbq;

import java.util.Collections;
import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IEnumDef;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.dds.DdsFunctionDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsSequenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqml.ISqmlEnvironment;
import org.radixware.kernel.common.sqml.ISqmlProperty;
import org.radixware.kernel.common.sqml.ISqmlSchema;
import org.radixware.kernel.common.sqml.tags.IfParamTag;
import org.radixware.kernel.common.sqml.tags.PropSqlNameTag.EOwnerType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.common.defs.IParameterDef;
import org.radixware.kernel.common.sqml.tags.ElseIfTag;
import org.radixware.kernel.common.sqml.tags.EndIfTag;
import org.radixware.kernel.common.sqml.tags.TargetDbPreprocessorTag;

class SqmlEnvironment implements ISqmlEnvironment {

    private final SqlBuilder builder;

    public SqmlEnvironment(final SqlBuilder builder) {
        this.builder = builder;
    }

    @Override
    public DdsTableDef findTableById(final Id tableId) {
        return builder.getArte().getDefManager().getTableDef(tableId);
    }

    /*	@Override
     public IEnumDef.IItem findEnumItem(final Id enumId, final String enumItemName) {
     final List<? extends IEnumDef.IItem> items = findEnumById(enumId).getItems().list(EScope.ALL);
     for (IEnumDef.IItem item : items){
     if (item.getName().equals(enumItemName))
     return item;
     }
     throw new DefinitionNotFoundError(enumId);
     }*/
    @Override
    public IEnumDef findEnumById(final Id enumId) {
        return builder.getArte().getDefManager().getEnumDef(enumId);
    }

    @Override
    public DdsFunctionDef findThisFunction() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DdsFunctionDef findFunctionById(final Id funcId) {
        return builder.getArte().getDefManager().getFunctionDef(funcId);
    }

    @Override
    public Definition findDefinitionByIds(final Id[] ids) {
        return builder.getArte().getDefManager().getDdsDef(ids);
    }

    @Override
    public Definition findDefinitionByIds(final Id[] ids, boolean global) {
        return builder.getArte().getDefManager().getDdsDef(ids);
    }

    @Override
    public IParameterDef findParameterById(final Id paramId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ISqmlProperty findPropertyById(final Id propOwnerId, final Id propertyId) {
        final ISqmlProperty prop;
        if (propOwnerId == null) {
            throw new IllegalUsageError("propOwnerId can't be null");
        }
        if (propOwnerId.getPrefix() == EDefinitionIdPrefix.DDS_TABLE) {
            final DdsTableDef tableDef = findTableById(propOwnerId);
            prop = tableDef.getColumns().getById(propertyId, EScope.ALL);
        } else {
            final RadClassDef classDef = builder.getArte().getDefManager().getClassDef(propOwnerId);
            prop = classDef.getPropById(propertyId);
        }
        return prop;
    }

    @Override
    public Definition findChildPropertiesOwner() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ISqmlProperty findChildPropertyById(Id propertyId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Definition findParentPropertiesOwner() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ISqmlProperty findParentPropertyById(Id propertyId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Definition> findThisPropertiesOwner() {
        Definition def = builder.getEntityClass();
        if (def == null) {
            return Collections.emptyList();
        } else {
            return Collections.singletonList(def);
        }
    }

    @Override
    public ISqmlProperty findThisPropertyById(final Id propertyId) {
        return builder.getEntityClass().getPropById(propertyId);
    }

    @Override
    public DdsReferenceDef findReferenceById(final Id referenceId) {
        return builder.getArte().getDefManager().getReferenceDef(referenceId);
    }

    @Override
    public DdsSequenceDef findSequenceById(final Id sequenceId) {
        return builder.getArte().getDefManager().getSequenceDef(sequenceId);
    }

    @Override
    public DdsTableDef findThisTable() {
        return builder.getTable();
    }

    @Override
    public ERepositorySegmentType getSegmentType() {
        return ERepositorySegmentType.ADS;
    }

    @Override
    public VisitorProvider getPropProvider(EOwnerType ownerType) {
        return new VisitorProvider() {
            @Override
            public boolean isTarget(RadixObject radixObject) {
                return true;
            }
        };
    }

    @Override
    public void printTagCondition(CodePrinter cp, IfParamTag ifParamTag) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ISqmlSchema findSchemaById(final Id schemaId) {
        return new ISqmlSchema() {
            @Override
            public Definition getDefinition() {
                return null;
            }

            @Override
            public String getNamespace() {
                return builder.getArte().getDefManager().getTargetNamespaceUriBySchemaDefId(schemaId);
            }
        };
    }

    @Override
    public void printEndIf(CodePrinter cp, EndIfTag endIfTag) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void printElse(CodePrinter cp, ElseIfTag elseIfTag) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void printTargetDbPreprocessorTag(CodePrinter cp, TargetDbPreprocessorTag tag) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public RadixObject getContext() {
        return null;
    }
}
