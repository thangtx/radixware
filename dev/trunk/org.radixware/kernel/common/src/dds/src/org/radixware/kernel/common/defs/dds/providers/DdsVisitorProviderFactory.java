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

package org.radixware.kernel.common.defs.dds.providers;

import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.dds.DdsAccessPartitionFamilyDef;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsColumnTemplateDef;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsPlSqlObjectDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsSequenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.DdsTriggerDef;
import org.radixware.kernel.common.defs.dds.DdsTypeDef;
import org.radixware.kernel.common.defs.dds.DdsViewDef;

/**
 * Factory for visitor providers of DDS segment.
 akiliyvich.
 */
public class DdsVisitorProviderFactory {

    /**
     * Returns visitor provider instance that helps
     * to choose enumeration for DdsColumnTemplateDef.
     */
    public static final VisitorProvider newEnumForColumnTemplateProvider(final DdsColumnTemplateDef columnTemplate) {
        return new EnumForValTypeProvider(columnTemplate.getValType());
    }

    /**
     * Returns visitor provider instance that helps to choose {@linkplain DdsTypeDef} for DdsColumnTemplateDef.
     */
    public static final VisitorProvider newNativeDbTypeForColumnTemplateProvider() {
        return new DdsNativeDbTypeProvider();
    }

    /**
     * Returns visitor provider instance that helps to choose {@linkplain DdsColumnTemplateDef} for DdsColumnDef.
     */
    public static final VisitorProvider newColumnTemplateForColumnProvider() {
        return new DdsColumnTemplateProvider();
    }

    /**
     * @return visitor provider instance that helps to choose any {@linkplain DdsRerefenceDef}.
     */
    public static final VisitorProvider newReferenceProvider() {
        return new DdsReferenceProvider();
    }

    /**
     * @return visitor provider instance that helps to choose any {@linkplain DdsTableDef} (include {@linkplain DdsViewDef}).
     */
    public static final VisitorProvider newTableProvider() {
        return new DdsTableProvider();
    }

    /**
     * @return visitor provider instance that helps to choose generated {@linkplain DdsTableDef} (include {@linkplain DdsViewDef}).
     */
    public static final VisitorProvider newGeneratedTableProvider() {
        return new DdsGeneratedTableProvider();
    }
    
    public static final VisitorProvider newGeneratedInDbProvider() {
        return new DdsGeneratedInDbProvider();
    }


    /**
     * @return visitor provider instance that helps to choose any {@linkplain DdsSequenceDef}.
     */
    public static final VisitorProvider newSequenceProvider() {
        return new DdsSequenceProvider();
    }

    /**
     * @return visitor provider instance that helps to choose {@linkplain DdsColumnDef} for {@linkplain DdsIndexDef}.
     */
    public static final IFilter<DdsColumnDef> newColumnForIndexFilter(DdsIndexDef index) {
        return new DdsColumnForIndexFilter(index);
    }

    /**
     * @return visitor provider instance that helps to choose {@linkplain DdsColumnDef} for {@linkplain DdsTriggerDef}.
     */
    public static final IFilter<DdsColumnDef> newColumnForTriggerFilter(DdsTriggerDef trigger) {
        return new DdsColumnForTriggerFilter(trigger);
    }

    /**
     * @return visitor provider instance that helps to choose {@linkplain DdsColumnDef} for {@linkplain DdsTableDef} initial values.
     */
    public static final IFilter<DdsColumnDef> newColumnForInitialValuesFilter(DdsTableDef table) {
        return new DdsColumnForInitialValuesFilter(table);
    }

    /**
     * @return  visitor provider instance that helps to choose any {@linkplain DdsAccessPartitionFamily}.
     */
    public static final VisitorProvider newAccessPartitionFamilyProvider() {
        return new DdsApfProvider();
    }

    /**
     * @return  visitor provider instance that helps to choose head for access partition family.
     */
    public static final VisitorProvider newApfHeadProvider() {
        return new DdsApfHeadProvider();
    }

    /**
     * @return  visitor provider instance that helps to choose reference to parent for access partition family.
     */
    public static final VisitorProvider newApfParentReferenceProvider(DdsAccessPartitionFamilyDef apf) {
        return new DdsApfParentReferenceProvider(apf, apf.getCorrectParentTableIds(apf));
    }

    /**
     * @return  visitor provider instance that helps to choose function for Prototype of spectified PL/SQL object.
     */
    public static final VisitorProvider newFunctionForPrototypeProvider(DdsPlSqlObjectDef plSqlObject) {
        return new DdsFunctionForPrototypeProvider(plSqlObject);
    }

    /**
     * @return visitor provider that allows to find all tables that overwrite specified table.
     */
    public static final VisitorProvider newFindTableOverwriteProvider(DdsTableDef overwrittenTable) {
        return new DdsFindTableOverwriteProvider(overwrittenTable);
    }

    /**
     * @return visitor provider that allows to find all DDS definitions.
     */
    public static final DdsVisitorProvider newDdsDefinitionVisitorProvider() {
        return new DdsDefinitionVisitorProvider();
    }

    /**
     * @return visitor provider that allows to find all triggers.
     */
    public static final DdsVisitorProvider newTriggerProvider() {
        return new DdsTriggerProvider();
    }

    /**
     * @return visitor provider instance that helps to choose table for PL/SQL function.
     */
    public static final VisitorProvider newTableForFunctionProvider() {
        return new DdsGeneratedTableProvider();
    }

    /**
     * @return visitor provider instance that helps to choose generated index.
     */
    public static final VisitorProvider newGeneratedIndexProvider() {
        return new DdsGeneratedIndexProvider();
    }

    /**
     * @return visitor provider instance that helps to choose any DdsColumnDef.
     */
    public static final VisitorProvider newColumnProvider() {
        return new DdsColumnProvider();
    }

    /**
     * @return visitor provider instance that helps to choose any DdsFunctionDef.
     */
    public static final VisitorProvider newFunctionProvider() {
        return new DdsFunctionProvider();
    }

    public static final VisitorProvider newPlSqlObjectProvider() {
        return new DdsPlSqlObjectProvider();
    }

    public static final IFilter<DdsReferenceDef> newOutgoingReferencesFilter(DdsTableDef table) {
        return new OutgoingReferencesFilter(table);
    }

    public static final IFilter<DdsReferenceDef> newIncomingReferencesFilter(DdsTableDef table) {
        return new IncomingReferencesFilter(table);
    }

    public static final VisitorProvider newDdsModelItemVisitorProvider() {
        return new DdsModelItemVisitorProvider();
    }

    /**
     * @return filter for DdsTableDef.AuditReference
     */
    public static final IFilter<DdsReferenceDef> newReferenceForAuditFilter() {
        return new DdsReferenceForAuditFilter();
    }
}
