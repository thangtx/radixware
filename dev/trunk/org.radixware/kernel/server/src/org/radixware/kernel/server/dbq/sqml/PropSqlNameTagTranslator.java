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

import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.exceptions.TagTranslateError;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqml.tags.PropSqlNameTag;
import org.radixware.kernel.common.sqml.tags.PropSqlNameTag.EOwnerType;
import org.radixware.kernel.common.sqml.translate.SqmlTagTranslatorFactory;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.dbq.DbQuery;
import org.radixware.kernel.server.dbq.SqlBuilder;
import org.radixware.kernel.server.meta.clazzes.IRadRefPropertyDef;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.meta.clazzes.RadDetailParentRefPropDef;
import org.radixware.kernel.server.meta.clazzes.RadPropDef;

class PropSqlNameTagTranslator<T extends PropSqlNameTag> extends QueryTagTranslator<T> {

    protected PropSqlNameTagTranslator(final SqlBuilder queryBuilder, final QuerySqmlTranslator.EMode translationMode) {
        super(queryBuilder, translationMode);
    }

    @Override
    public void translate(final T tag, final CodePrinter cp) {
        final EOwnerType owner = tag.getOwnerType();
        SqmlTagTranslatorFactory defaultFactory;
        if (getTranslationMode() == QuerySqmlTranslator.EMode.QUERY_TREE_CONSTRUCTION) {
            switch (owner) {
                case THIS:
                    if (tag.getPropId().getPrefix() != EDefinitionIdPrefix.ADS_GROUP_PROP) {
                        getQueryBuilder().addProp(tag.getPropId(), SqlBuilder.EPropUsageType.OTHER, null);
                    }
                    break;
                case PARENT:
                case CHILD: {
                    boolean isThisColumn = false;
                    try {
                        getQueryBuilder().getEntityClass().getPropById(tag.getPropId());
                        isThisColumn = true;
                    } catch (DefinitionNotFoundError e) {
                        //it isn't  column of this
                        isThisColumn = false;
                    }
                    if (isThisColumn) {
                        getQueryBuilder().addProp(tag.getPropId(), SqlBuilder.EPropUsageType.OTHER, null);
                    }
                }
                break;
                default:
                //do nothing because it's not a column of this table
            }
        } else {
            switch (owner) {
                case THIS:
                case PARENT:
                case CHILD:
                    translateContextPropSqlName(tag, cp);
                    break;
                default: {
                    defaultFactory = org.radixware.kernel.common.sqml.translate.SqmlTagTranslatorFactory.Factory.newInstance(new IProblemHandler() {
                        @Override
                        public void accept(RadixProblem problem) {
                            //do nothing because check is design time activity
                        }
                    });
                    defaultFactory.findTagTranslator(tag).translate(tag, cp);
                }
            }
        }
    }

    private void translateContextPropSqlName(final PropSqlNameTag tag, final CodePrinter cp) {
        final EOwnerType owner = tag.getOwnerType();
        if (tag.getPropId().getPrefix() != EDefinitionIdPrefix.ADS_GROUP_PROP
                && (owner == PropSqlNameTag.EOwnerType.THIS
                || (getQueryBuilder().getQueryCntxType() == SqlBuilder.EQueryContextType.PARENT_SELECTOR) && owner == PropSqlNameTag.EOwnerType.PARENT
                || (getQueryBuilder().getQueryCntxType() == SqlBuilder.EQueryContextType.OTHER) && owner == PropSqlNameTag.EOwnerType.CHILD)) {
            //this table column
            final String tabAlias = tag.isTableAliasDefined() ? tag.getTableAlias() : null;
            printPropSqlPres(cp, getQueryBuilder(), tag.getPropId(), tabAlias, tag);
        } else if ( //group or context property
                tag.getPropId().getPrefix() == EDefinitionIdPrefix.ADS_GROUP_PROP
                || (getQueryBuilder().getQueryCntxType() == SqlBuilder.EQueryContextType.PARENT_SELECTOR && owner == PropSqlNameTag.EOwnerType.CHILD)
                || (getQueryBuilder().getQueryCntxType() == SqlBuilder.EQueryContextType.OTHER && owner == PropSqlNameTag.EOwnerType.PARENT)) {
            RadPropDef propDef = null;
            RadClassDef classDef = null;
            if (tag.getPropOwnerId().getPrefix() != EDefinitionIdPrefix.DDS_TABLE) {
                classDef = getQueryBuilder().getArte().getDefManager().getClassDef(tag.getPropOwnerId());
                propDef = classDef.getPropById(tag.getPropId());
            }
            if ((propDef instanceof IRadRefPropertyDef) && ((IRadRefPropertyDef) propDef).getReference() != null) {
                cp.print('(');
                boolean isFirst = true;
                final DdsReferenceDef ref = ((IRadRefPropertyDef) propDef).getReference();
                for (DdsReferenceDef.ColumnsInfoItem refProp : ref.getColumnsInfo()) {
                    if (!isFirst) {
                        cp.print(',');
                    }
                    cp.print("?");
                    Id fkPropId = refProp.getChildColumnId();
                    if (propDef instanceof RadDetailParentRefPropDef) {
                        fkPropId = getQueryBuilder().getArte().getDefManager().
                                getMasterPropIdByDetailPropId(
                                ((RadDetailParentRefPropDef) propDef).getDetailReference(),
                                classDef,
                                fkPropId);
                    }
                    if (tag.getPropId().getPrefix() == EDefinitionIdPrefix.ADS_GROUP_PROP) {
                        getQueryBuilder().addParameter(new DbQuery.InputGroupPropParam(tag.getPropId()));
                    } else if (owner == PropSqlNameTag.EOwnerType.PARENT) {
                        getQueryBuilder().addParameter(new DbQuery.InputParentPropParam(fkPropId));
                    } else {
                        getQueryBuilder().addParameter(new DbQuery.InputChildPropParam(fkPropId));
                    }
                    isFirst = false;
                }
                cp.print(')');
            } else {
                cp.print("?");
                if (tag.getPropId().getPrefix() == EDefinitionIdPrefix.ADS_GROUP_PROP) {
                    getQueryBuilder().addParameter(new DbQuery.InputGroupPropParam(tag.getPropId()));
                } else if (owner == PropSqlNameTag.EOwnerType.PARENT) {
                    getQueryBuilder().addParameter(new DbQuery.InputParentPropParam(tag.getPropId()));
                } else {
                    getQueryBuilder().addParameter(new DbQuery.InputChildPropParam(tag.getPropId()));
                }
            }
        } else {
            throw new TagTranslateError(tag, new WrongFormatError("Can't interpret CHILD or PARENT column in current context", null));
        }
    }
}
