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

package org.radixware.kernel.common.sqml;

import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IEnumDef;
import org.radixware.kernel.common.defs.IParameterDef;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.dds.DdsFunctionDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsSequenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqml.tags.ElseIfTag;
import org.radixware.kernel.common.sqml.tags.EndIfTag;
import org.radixware.kernel.common.sqml.tags.IfParamTag;
import org.radixware.kernel.common.sqml.tags.PropSqlNameTag.EOwnerType;
import org.radixware.kernel.common.sqml.tags.TargetDbPreprocessorTag;

/**
 * Окружение SQML, возвращает всю необходимую контекстную информацию,
 * необходимую для проверки и трансляции тэга.
 *
 */
public interface ISqmlEnvironment {

    /**
     * Find table by identitier.
     *
     * @return table or null if not found.
     */
    public DdsTableDef findTableById(Id tableId);

    /**
     * Find enum by identifier.
     *
     * @return enum or null if not found.
     */
    public IEnumDef findEnumById(Id enumId);

    /**
     * Find function in current context.
     *
     * @return function or null if not found.
     */
    public DdsFunctionDef findThisFunction();

    /**
     * Find SQL function by identifier.
     *
     * @return function or null if not found.
     */
    public DdsFunctionDef findFunctionById(Id funcId);

    /**
     * Find definition by its identifier and identifies of its owners (for
     * optimization, if required). Final definition identifier is the last item
     * of array.
     *
     * @return definition or null if not found.
     */
    public Definition findDefinitionByIds(Id[] ids);

    public Definition findDefinitionByIds(Id[] ids, boolean global);

    /**
     * Find SQML parameter (AdsSqlClass.Parameter) by identifier.
     *
     * @return parameter or null if not found.
     */
    public IParameterDef findParameterById(Id paramId);

    /**
     * Find property (AdsClassDef.Property or DdsTableDef.Column) by its
     * identifier and identifier or its owner.
     *
     * @return property or null if not found.
     */
    public ISqmlProperty findPropertyById(Id propOwnerId, Id propertyId);

    /**
     * Find owner for child properties (AdsClassDef.Property or
     * DdsTableDef.Column).
     *
     * @return owner or null if not found.
     */
    public Definition findChildPropertiesOwner();

    /**
     * Find property of child (AdsClassDef.Property or DdsTableDef.Column) by
     * identifier.
     *
     * @return property or null if not found.
     */
    public ISqmlProperty findChildPropertyById(Id propertyId);

    /**
     * Find owner for parent properties (AdsClassDef.Property or
     * DdsTableDef.Column).
     *
     * @return owner or null if not found.
     */
    public Definition findParentPropertiesOwner();

    /**
     * Find property of parent (AdsClassDef.Property or DdsTableDef.Column) by
     * identifier.
     *
     * @return property or null if not found.
     */
    public ISqmlProperty findParentPropertyById(Id propertyId);

    /**
     * Find owner for this properties (AdsClassDef.Property or
     * DdsTableDef.Column).
     *
     * @return owner or null if not found.
     */
    public List<Definition> findThisPropertiesOwner();

    /**
     * Find property of current (AdsClassDef.Property or DdsTableDef.Column) by
     * identifier.
     *
     * @return property or null if not found.
     */
    public ISqmlProperty findThisPropertyById(Id propertyId);

    /**
     * Find reference by identifier.
     *
     * @return reference or null if not found.
     */
    public DdsReferenceDef findReferenceById(Id referenceId);

    /**
     * Find sequence by identifier.
     *
     * @return sequence or null if not found.
     */
    public DdsSequenceDef findSequenceById(Id sequenceId);

    /**
     * Find current table by identifier.
     *
     * @return current table or null if not found.
     */
    public DdsTableDef findThisTable();

    /**
     * @return repository segment type of SCML.
     */
    public ERepositorySegmentType getSegmentType();

    /**
     * @return true if it is possible to write statement that will modify
     * database, false otherwise.
     */
    //public boolean isDbModificationAllowed();
    public VisitorProvider getPropProvider(EOwnerType ownerType);

    public void printTagCondition(CodePrinter cp, IfParamTag ifParamTag);

    public void printTargetDbPreprocessorTag(CodePrinter cp, TargetDbPreprocessorTag tag);

    public void printEndIf(CodePrinter cp, EndIfTag endIfTag);

    public void printElse(CodePrinter cp, ElseIfTag elseIfTag);

    public ISqmlSchema findSchemaById(Id schemaId);

    /**
     * @return Initial context for this environment.
     */
    public RadixObject getContext();
}
