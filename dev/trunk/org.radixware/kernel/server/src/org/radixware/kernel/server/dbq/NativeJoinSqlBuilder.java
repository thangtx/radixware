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

import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.enums.EDeleteMode;
import org.radixware.kernel.common.enums.EDdsConstraintDbOption;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;

final class NativeJoinSqlBuilder extends JoinSqlBuilder {

    protected final DdsReferenceDef reference;

//Constructor
    protected NativeJoinSqlBuilder(
            final QuerySqlBuilder mainBuilder,
            final SqlBuilder parentBuilder,
            final DdsReferenceDef reference,
            final boolean isChildRef) {
        super(
                mainBuilder,
                parentBuilder,
                mainBuilder.getArte().getDefManager().getTableDef(isChildRef ? reference.getChildTableId() : reference.getParentTableId()),
                //reference.getChildTable(mainBuilder.getArte().getDefManager().getRelease().getDdsLoadContext()) :
                //reference.getParentTable(mainBuilder.getArte().getDefManager().getRelease().getDdsLoadContext()),
                isChildRef);
        this.reference = reference;
    }

    @Override
    public final RadClassDef getEntityClass() {
        if (entityClass == null) {
            if (isChildRef && (reference.getType() == DdsReferenceDef.EType.MASTER_DETAIL)) {
                //Details has no stored DacMeta. Let's generate a temporary one.
                entityClass = getArte().getDefManager().getDetailRadMeta(reference);
            } else {
                entityClass = getArte().getDefManager().getClassDef(RadClassDef.getEntityClassIdByTableId(getTable().getId()));
            }
        }
        return entityClass;
    }
    private RadClassDef entityClass = null;

    @Override
    protected final Id getMainPropId(final Id propId) {
        // for details primary key props - main props is master primary key prop
        // (they have equal values)
        if (isChildRef) {
            for (DdsReferenceDef.ColumnsInfoItem refProp : reference.getColumnsInfo()) {
                if (refProp.getChildColumnId().equals(propId)) {
                    return parentBuilder.getMainPropId(refProp.getParentColumnId());
                }
            }
        }
        return super.getMainPropId(propId);
    }

    @Override
    protected final void appendJoinCondStr() {
        //append condition of join to Table of Parent SqlConstructor ( != Parent Table)
        final StringBuilder qry = getMainBuilder().querySql;
        boolean isFirst = true;
        if (isChildRef) {
            for (DdsReferenceDef.ColumnsInfoItem refProp : reference.getColumnsInfo()) {
                if (!isFirst) {
                    qry.append(" and ");
                } else {
                    isFirst = false;
                }
                qry.append(getNativePropDbPresentation(refProp.getChildColumnId()));
                qry.append("=");
                qry.append(parentBuilder.getNativePropDbPresentation(refProp.getParentColumnId()));
            }
        } else {
            for (DdsReferenceDef.ColumnsInfoItem refProp : reference.getColumnsInfo()) {
                if (!isFirst) {
                    qry.append(" and ");
                } else {
                    isFirst = false;
                }
                qry.append(parentBuilder.getNativePropDbPresentation(refProp.getChildColumnId()));
                qry.append("=");
                qry.append(getNativePropDbPresentation(refProp.getParentColumnId()));
            }
        }
    }

    @Override
    void appendJoinTypeStr() {
        final String type;
        if (//RADIX-4291: optimization for cases then null or broken reference forbidden by DB
                !isChildRef
                && reference.isGeneratedInDb()
                && (reference.getDeleteMode() == EDeleteMode.CASCADE
                || reference.getDeleteMode() == EDeleteMode.RESTRICT)
                && !reference.getDbOptions().contains(EDdsConstraintDbOption.NOVALIDATE)
                && !reference.getDbOptions().contains(EDdsConstraintDbOption.DISABLE)
                && isRefNotNull()) {
            type = "INNER";
        } else {
            type = "LEFT";
        }
        getMainBuilder().querySql.append(type);
    }

    private boolean isRefNotNull() {
        for (DdsReferenceDef.ColumnsInfoItem i : reference.getColumnsInfo()) {
            if (!i.getChildColumn().isNotNull()) {
                return false;
            }
        }
        return true;
    }
}
