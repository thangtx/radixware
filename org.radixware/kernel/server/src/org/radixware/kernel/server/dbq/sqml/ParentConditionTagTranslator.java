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

package org.radixware.kernel.server.dbq.sqml;

import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.exceptions.TagTranslateError;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqml.tags.ParentConditionTag.Operator;
import org.radixware.kernel.server.dbq.SqlBuilder;
import org.radixware.kernel.server.meta.clazzes.RadPropDef;
import org.radixware.kernel.common.sqml.tags.ParentConditionTag;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.ValueToSqlConverter;
import org.radixware.kernel.server.dbq.SqlBuilder.EQueryBuilderAliasPolicy;
import org.radixware.kernel.server.exceptions.EntityObjectNotExistsError;
import org.radixware.kernel.server.meta.clazzes.IRadRefPropertyDef;
import org.radixware.kernel.server.types.Entity;
import org.radixware.kernel.server.types.Pid;

class ParentConditionTagTranslator<T extends ParentConditionTag> extends QueryTagTranslator<T> {

    protected ParentConditionTagTranslator(final SqlBuilder queryBuilder, final QuerySqmlTranslator.EMode translationMode) {
        super(queryBuilder, translationMode);
    }

    @Override
    public void translate(final T tag, final CodePrinter cp) {
        Operator oper;
        if (getTranslationMode() == QuerySqmlTranslator.EMode.QUERY_TREE_CONSTRUCTION) {
            final Id propOwnerId = tag.getPropOwnerId();
            if (propOwnerId == null) {
                throw new TagTranslateError(tag);
            }
            boolean thisPropUsed = false;
            if (propOwnerId.getPrefix() == EDefinitionIdPrefix.DDS_TABLE) {
                thisPropUsed = getQueryBuilder().getTable().getId().equals(propOwnerId);
            } else {
                try {
                    thisPropUsed = (getQueryBuilder().getEntityClass().getPropById(tag.getPropId()) != null);
                } catch (DefinitionNotFoundError e) {
                    thisPropUsed = false;
                }
            }
            if (thisPropUsed) {
                getQueryBuilder().addProp(tag.getPropId(), SqlBuilder.EPropUsageType.OTHER, null);
            }
        } else {
            final DdsReferenceDef ref = getParentTitleReference(tag);
            cp.print("(");
            final SqlBuilder.Field f = getQueryBuilder().getDestField(tag.getPropId().toString());
            oper = tag.getOperator();
            if (oper == Operator.EQUAL || oper == Operator.NOT_EQUAL) {
                boolean isFirst = true;
                for (DdsReferenceDef.ColumnsInfoItem refProp : ref.getColumnsInfo()) {
                    if (isFirst) {
                        isFirst = false;
                    } else {
                        if (oper == Operator.EQUAL) {
                            cp.print(" and ");
                        } else {
                            cp.print(" or ");
                        }
                    }
                    final DdsColumnDef fkProp = refProp.getChildColumn();
                    if (getQueryBuilder().getAliasPolicy() == EQueryBuilderAliasPolicy.USE_ALIASES) {
                        cp.print(f.getBuilder().getAlias());
                        cp.print('.');
                    }
                    cp.print(fkProp.getDbName());
                    String valueStr;
                    try {
                        if (tag.getParentPid() == null) {
                            throw new TagTranslateError(tag, "Parent is not defined");
                        }
                        final Pid parentPid = new Pid(
                                getQueryBuilder().getArte(),
                                getQueryBuilder().getArte().getDefManager().getTableDef(ref.getParentTableId()),
                                tag.getParentPid());
                        final Entity parent = getQueryBuilder().getArte().getEntityObject(parentPid);
                        valueStr = ValueToSqlConverter.toSql(
                                parent.getProp(refProp.getParentColumnId()),
                                refProp.getParentColumn().getValType());
                    } catch (EntityObjectNotExistsError e) {
                        valueStr = "NULL";
                    }
                    if (oper == Operator.EQUAL) {
                        cp.print("=");
                        cp.print(valueStr);
                    } else {
                        cp.print("<>");
                        cp.print(valueStr);
                        cp.print(" or ");
                        if (getQueryBuilder().getAliasPolicy() == EQueryBuilderAliasPolicy.USE_ALIASES) {
                            cp.print(f.getBuilder().getAlias());
                            cp.print('.');
                        }
                        cp.print(fkProp.getDbName());

                        cp.print(" IS NULL");
                    }
                }
            } else {
                boolean isFirst = true;
                for (DdsReferenceDef.ColumnsInfoItem refProp : ref.getColumnsInfo()) {
                    if (isFirst) {
                        isFirst = false;
                    } else {
                        if (tag.getOperator() == ParentConditionTag.Operator.IS_NOT_NULL) {
                            cp.print(" and ");
                        } else {
                            cp.print(" or ");
                        }
                    }
                    final DdsColumnDef fkProp = refProp.getChildColumn();
                    if (getQueryBuilder().getAliasPolicy() == EQueryBuilderAliasPolicy.USE_ALIASES) {
                        cp.print(f.getBuilder().getAlias());
                        cp.print('.');
                    }
                    cp.print(fkProp.getDbName());
                    switch (oper) {
                        case IS_NOT_NULL:
                            cp.print(" IS NOT NULL");
                            break;
                        case IS_NULL:
                            cp.print(" IS NULL");
                            break;
                        default:
                            throw new TagTranslateError(tag, "Unsupported operator: " + String.valueOf(oper));
                    }
                }
            }
            cp.print(')');
        }
    }

    private final DdsReferenceDef getParentTitleReference(final ParentConditionTag tag) {
        final RadPropDef dacProp;
        dacProp = getQueryBuilder().getEntityClass().getPropById(tag.getPropId());
        if (dacProp instanceof IRadRefPropertyDef) {
            final DdsReferenceDef ref = ((IRadRefPropertyDef) dacProp).getReference();
            if (ref != null) {
                return ref;
            }
        }
        throw new WrongFormatError("\"" + dacProp.getClass().getName() + "\" is not supperted in ParentCondition tag", null);
    }
}
