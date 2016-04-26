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

package org.radixware.kernel.common.client.meta.sqml.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.KernelIcon;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableReferences;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableColumns;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableIndices;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsApplicationClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef.DetailReferenceInfo;
import org.radixware.kernel.common.defs.dds.DdsDefinitionIcon;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.types.Id;


final class SqmlTableDefImpl extends SqmlDefinitionImpl implements ISqmlTableDef {

    private ISqmlTableColumns columns;
    private ISqmlTableReferences references;
    private ISqmlTableIndices indices;
    private final DdsTableDef tableDef;
    private final AdsEntityObjectClassDef classDef;

    public SqmlTableDefImpl(final IClientEnvironment environment, final DdsTableDef ddsTable) {
        super(environment, ddsTable);
        tableDef = ddsTable;
        classDef = null;
    }

    public SqmlTableDefImpl(final IClientEnvironment environment, final AdsEntityObjectClassDef adsClass) {
        super(environment, adsClass);
        classDef = adsClass;
        tableDef = null;
    }

    @Override
    public Id getTableId() {
        return tableDef == null ? classDef.findTable(classDef).getId() : tableDef.getId();
    }

    @Override
    public String getTitle() {
        if (classDef == null) {
            return tableDef.getName();
        }
        return classDef.getTitleId() == null ? classDef.getName() : checkTitle(classDef.getTitle(environment.getLanguage()));
    }

    @Override
    public ISqmlTableColumns getColumns() {
        if (columns == null) {
            if (classDef == null) {
                columns = new SqmlTableColumns(environment, tableDef);
            } else {
                columns = new SqmlTableColumns(environment, classDef);
            }
        }
        return columns;
    }

    @Override
    public ISqmlTableReferences getReferences() {
        if (references == null) {
            final DdsTableDef ddsTables;
            final List<ISqmlTableReferences> referencesFromDetailTables = new LinkedList<>();
            if (tableDef == null) {
                ddsTables = classDef.findTable(classDef);
                final List<AdsEntityObjectClassDef.DetailReferenceInfo> detailRefInfoList =
                        classDef.getAllowedDetailRefs();
                DdsReferenceDef reference;
                ISqmlTableDef tableDetail;
                for (AdsEntityObjectClassDef.DetailReferenceInfo detailRefInfo : detailRefInfoList) {
                    reference = detailRefInfo.findReference();
                    if (reference != null) {
                        tableDetail = environment.getSqmlDefinitions().findTableById(reference.getChildTableId());
                        if (tableDetail != null) {
                            referencesFromDetailTables.add(tableDetail.getReferences());
                        }
                    }
                }
            } else {
                ddsTables = tableDef;
            }
            references = new SqmlTableReferences(environment, ddsTables, referencesFromDetailTables);
        }
        return references;
    }

    @Override
    public ISqmlTableIndices getIndices() {
        if (indices == null) {
            indices = new SqmlTableIndices(environment, tableDef == null ? classDef.findTable(classDef) : tableDef);
        }
        return indices;
    }
    private String alias;

    @Override
    public ISqmlTableDef createCopyWithAlias(final String alias) {
        final SqmlTableDefImpl copy;
        if (classDef != null) {
            copy = new SqmlTableDefImpl(environment, classDef);
        } else {
            copy = new SqmlTableDefImpl(environment, tableDef);
        }
        copy.alias = alias;
        return copy;
    }

    @Override
    public boolean hasEntityClass() {
        return classDef != null;
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public boolean hasAlias() {
        return alias != null && !alias.isEmpty();
    }

    @Override
    public ClientIcon getIcon() {
        if (classDef != null && classDef.getId().getPrefix() == EDefinitionIdPrefix.ADS_APPLICATION_CLASS) {
            return KernelIcon.getInstance(AdsDefinitionIcon.CLASS_APPLICATION);
        }
        return KernelIcon.getInstance(DdsDefinitionIcon.TABLE);
    }

    @Override
    public boolean hasDetails() {
        if (classDef instanceof AdsApplicationClassDef) {
            return ((AdsApplicationClassDef) classDef).isDetailPropsAllowed();
        }
        return false;
    }

    Collection<Id> getDetailReferenceIds() {
        if (classDef instanceof AdsApplicationClassDef) {
            final List<DetailReferenceInfo> detailReferences =
                    ((AdsApplicationClassDef) classDef).getAllowedDetailRefs();
            if (!detailReferences.isEmpty()) {
                final Collection<Id> result = new LinkedList<>();
                for (DetailReferenceInfo refInfo : detailReferences) {
                    result.add(refInfo.getReferenceId());
                }
                return result;
            }
        }
        return Collections.emptyList();
    }

    @Override
    public boolean isDeprecatedDdsDef() {
        DdsTableDef table = classDef != null ? classDef.findTable(classDef) : tableDef;
        return table != null && table.isDeprecated();
    }
}
