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

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.DdsViewDef;
import org.radixware.kernel.common.enums.EAggregateFunction;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EOrder;
import org.radixware.kernel.common.enums.EPaginationMethod;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.server.sqml.Sqml;
import org.radixware.kernel.common.sqml.tags.PropSqlNameTag;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.dbq.sqml.QuerySqmlTranslator;
import org.radixware.kernel.server.exceptions.DbQueryBuilderError;
import org.radixware.kernel.common.repository.DbConfiguration;
import org.radixware.kernel.common.sqml.translate.ISqmlPreprocessorConfig;
import org.radixware.kernel.common.types.AggregateFunctionCall;
import org.radixware.kernel.common.types.Arr;
import org.radixware.kernel.server.dbq.sqml.ServerPreprocessorConfig;
import org.radixware.kernel.server.meta.clazzes.IRadRefPropertyDef;
import org.radixware.kernel.server.meta.clazzes.RadClassApJoins;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.meta.clazzes.RadDetailParentRefPropDef;
import org.radixware.kernel.server.meta.clazzes.RadDetailPropDef;
import org.radixware.kernel.server.meta.clazzes.RadInnatePropDef;
import org.radixware.kernel.server.meta.clazzes.RadParentPropDef;
import org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef;
import org.radixware.kernel.server.meta.clazzes.RadPropDef;
import org.radixware.kernel.server.meta.clazzes.RadSqmlPropDef;
import org.radixware.kernel.server.meta.presentations.RadChildRefExplorerItemDef;
import org.radixware.kernel.server.meta.presentations.RadConditionDef;
import org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef;
import org.radixware.kernel.server.meta.presentations.RadExplorerItemDef;
import org.radixware.kernel.server.meta.presentations.RadSelectorExplorerItemDef;
import org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef;
import org.radixware.kernel.server.meta.presentations.RadSortingDef;
import org.radixware.kernel.server.types.Entity;
import org.radixware.kernel.server.types.EntityGroup;

public class GroupQuerySqlBuilder extends QuerySqlBuilder {

    public static enum ESelectType {

        SELECTION_PAGE,
        PRIMARY_KEY,
        PRIMARY_KEY_IN_SELECTION_ORDER,
        STATISTIC
    };

    public static enum ESortingColumnPurpose {

        RELATIVE_CONDITION_COMPARISON,
        RELATIVE_CONDITION_EQUALITY,
        ORDER_CLAUSE
    }

    private static final String _AND_ = " and ";
    private static final String _OR_ = " or ";
    private final EntityGroup<? extends Entity> group;
    private final ISqmlPreprocessorConfig preprocessorConfig;

//Constructor
    public GroupQuerySqlBuilder(final EntityGroup<? extends Entity> group) {
        super(
                group.getArte(),
                group.getDdsMeta(),
                group.getContext() instanceof EntityGroup.PropContext ? EQueryContextType.PARENT_SELECTOR : EQueryContextType.OTHER,
                EQueryBuilderAliasPolicy.USE_ALIASES);
        this.group = group;
        preprocessorConfig = new ServerPreprocessorConfig(getDbConfiguration(), group.getFltParamValsById());
    }

    @Override
    protected ISqmlPreprocessorConfig getPreprocessorConfig() {
        return preprocessorConfig;
    }

    @Override
    public RadClassDef getEntityClass() {
        return group.getSelectionClassDef();
    }

    public final boolean buildSelect(final ESelectType type) {
        return buildSelectImpl(type, null);
    }

    public final void buildCalcStatistic(final List<AggregateFunctionCall> functions) {
        buildSelectImpl(ESelectType.STATISTIC, functions);
    }

    private boolean buildSelectImpl(final ESelectType type, final List<AggregateFunctionCall> functions) {
        RadClassApJoins apJoins = null;
        if (getEntityClass().hasPartitionRights()) {
            apJoins = getEntityClass().getApJoins();
        }

        // adding key columns for inserting into tree
        final RadConditionDef grpSelCond;
        try {
            grpSelCond = getGroupSelectCondition(group);
        } catch (XmlException ex) {
            throw new DbQueryBuilderError("Error on SQML processing: " + ExceptionTextFormatter.getExceptionMess(ex), ex);
        }

        if (functions == null) {
            buildSelectQueryTree(group.getPresentation(), grpSelCond != null ? grpSelCond.getCondition() : null, group.getOrderBy());
            // subquery is used for rights check or(and) count check	
            if (type == ESelectType.SELECTION_PAGE) {
                querySql = new StringBuilder("select * from (select qry.*, ROWNUM R_N from (select ");
            } else {
                querySql = new StringBuilder("select ");
            }
        } else {
            addAggregationFunctions(functions, grpSelCond != null ? grpSelCond.getCondition() : null, group.getOrderBy());
            querySql = new StringBuilder("select ");
        }

        final Sqml hint = group.getHint();
        if (hint != null) {
            querySql.append("/*+ ");
            querySql.append(translateSqml(hint));
            querySql.append(" */ ");
        }

        if (getTable() instanceof DdsViewDef && ((DdsViewDef) getTable()).isDistinct()) {
            querySql.append("distinct ");
        }

        if (functions == null) {
            switch (type) {
                case SELECTION_PAGE: {
                    if (fieldsByPropId.isEmpty()) {
                        return false;
                    }
                    appendFieldsStr();

                    querySql.append(", RDX_ACS.getCurUserAllRolesForObject(");
                    if (apJoins != null) {
                        querySql.append(apJoins.getAreaListSql());
                    } else {
                        querySql.append("null");
                    }
                    querySql.append(") ");
                    querySql.append(ALL_ROLES_FIELD_ALIAS);
                    break;
                }
                default: {
                    boolean isFirstPkProp = true;
                    for (DdsIndexDef.ColumnInfo pkProp : getTable().getPrimaryKey().getColumnsInfo()) {
                        if (isFirstPkProp) {
                            isFirstPkProp = false;
                        } else {
                            querySql.append(',');
                        }
                        querySql.append(getAlias());
                        querySql.append('.');
                        querySql.append(pkProp.getColumn().getDbName());
                    }
                }
            }
        } else {
            appendAggregationFieldsStr();
        }

        buildSelectFromAndWhere(apJoins, grpSelCond, type);
        return true;
    }

    private void buildSelectFromAndWhere(final RadClassApJoins apJoins, final RadConditionDef grpSelCond, final ESelectType type) {
        querySql.append(" from ");

        //final StringBuffer joinHint = new StringBuffer();
        appendTablesStr(/*joinHint*/); //пусть оракловый оптимизатор поработает, теперь когда join-ы явно выделены ему будет легче

        if (apJoins != null && apJoins.getJoinsSql() != null) {
            querySql.append(apJoins.getJoinsSql());
        }

        if (grpSelCond != null && grpSelCond.getConditionFrom() != null) {
            if (group.getQueryBuilderDelegate() == null || !group.getQueryBuilderDelegate().appendConditionFrom(querySql, this, grpSelCond)) {
                querySql.append(',');
                final int tmp = querySql.length();
                querySql.append(translateSqml(grpSelCond.getConditionFrom()));
                if (querySql.length() == tmp) {
                    querySql.setLength(tmp - 1);
                }//removing unused ','
            }
        }

        //пусть оракловый оптимизатор поработает, теперь когда join-ы явно выделены ему будет легче
        //if (group.hint == null ){
        //	final String stdHint = (((grpSelCond == null || grpSelCond.condition == null) && group.orderBy != null && group.orderBy.length != 0) ? getIndexHint(group.orderBy): "");  
        //    querySql.replace(hintPos, hintPos+ 1, 
        //    	" FIRST_ROWS(30) " + (joinHint.length() != 0 ? joinHint + " " : "")+
        //    	(stdHint.length() != 0 ? stdHint + " " : "") 
        //    );
        //} else {
        //	if(joinHint.length() != 0)
        //		querySql.replace(hintPos, hintPos+ 1, " " + joinHint + " ");
        //}
        querySql.append(" where ");
        if (grpSelCond != null && grpSelCond.getCondition() != null) {
            querySql.append(translateSqml(grpSelCond.getCondition()));
            querySql.append(_AND_);
        }

        boolean sortingByUniqueValues = false;

        if (!group.getOrderBy().isEmpty() && type != ESelectType.PRIMARY_KEY) {
            final Collection<Id> sortingColumns = new ArrayList<>();
            for (RadSortingDef.Item srtItem : group.getOrderBy()) {
                sortingColumns.add(srtItem.getColumnId());
            }
            if (group.getDdsMeta() instanceof DdsViewDef == false) {
                sortingByUniqueValues = isTableIndexContainsColumns(group.getDdsMeta().getPrimaryKey(), sortingColumns);
                if (!sortingByUniqueValues) {
                    for (DdsIndexDef idx : group.getDdsMeta().getIndices().get(EScope.ALL)) {
                        if (idx.isGeneratedInDb() && idx.isUnique() && isTableIndexContainsColumns(idx, sortingColumns)) {
                            sortingByUniqueValues = true;
                            break;
                        }
                    }
                }
            }
        }

        boolean isRelativeSorting = false;

        if (group.getPaginationMethod() == EPaginationMethod.RELATIVE && !group.getOrderBy().isEmpty() && type != ESelectType.PRIMARY_KEY) {
            if (!sortingByUniqueValues) {
                throw new RadixError("Relative pagination can not be used with non-unique sorting");
            }
            isRelativeSorting = true;
            if (group.getPreviousEntityPid() != null) {
                boolean firstItem = true;
                for (RadSortingDef.Item srtItem : group.getOrderBy()) {
                    if (!firstItem) {
                        querySql.append(_AND_);
                    } else {
                        firstItem = false;
                    }
                    appendSortingColumn(this, srtItem.getColumnId(), (group.getContext() instanceof EntityGroup.PropContext), srtItem.getOrder(), ESortingColumnPurpose.RELATIVE_CONDITION_COMPARISON);
                }
                querySql.append(_AND_);
                querySql.append(" NOT (");
                firstItem = true;
                for (RadSortingDef.Item srtItem : group.getOrderBy()) {
                    if (!firstItem) {
                        querySql.append(_AND_);
                    } else {
                        firstItem = false;
                    }
                    appendSortingColumn(this, srtItem.getColumnId(), (group.getContext() instanceof EntityGroup.PropContext), srtItem.getOrder(), ESortingColumnPurpose.RELATIVE_CONDITION_EQUALITY);
                }
                querySql.append(")");
                querySql.append(_AND_);
            }
        }
        querySql.append("RDX_ACS.curUserHasRoleForObject(?, ");
        addParameter(new GroupQuery.InputRequestedRoleIdsParam());
        if (apJoins == null) {
            querySql.append("null");
        } else {
            querySql.append(apJoins.getAreaListSql());
        }
        querySql.append(") != 0");

        if (!group.getOrderBy().isEmpty() && type != ESelectType.PRIMARY_KEY) {
            querySql.append(" order by ");
            boolean firstItem = true;
            for (RadSortingDef.Item srtItem : group.getOrderBy()) {
                if (!firstItem) {
                    querySql.append(',');
                } else {
                    firstItem = false;
                }
                appendSortingColumn(this, srtItem.getColumnId(), (group.getContext() instanceof EntityGroup.PropContext), srtItem.getOrder(), ESortingColumnPurpose.ORDER_CLAUSE);
            }
            if (group.getDdsMeta() instanceof DdsViewDef == false) {
                if (!sortingByUniqueValues) {
                    querySql.append(',');
                    if (getAlias() == null) {
                        querySql.append(getTable().getDbName());
                    } else {
                        querySql.append(getAlias());
                    }
                    querySql.append(".ROWID");
                }
            }
        }
        if (type == ESelectType.SELECTION_PAGE) {
            querySql.append(") qry where");
            querySql.append(" ROWNUM <= ?");
            if (isRelativeSorting) {
                addParameter(new SelectQuery.InputRecCountParam());
            } else {
                addParameter(new SelectQuery.InputToRecParam());
            }
            querySql.append(')');
            if (!isRelativeSorting) {
                querySql.append(" where R_N >= ?");
                addParameter(new SelectQuery.InputFromRecParam());
            }
        }
    }

    private static boolean isTableIndexContainsColumns(final DdsIndexDef index, final Collection<Id> columnsId) {
        for (DdsIndexDef.ColumnInfo columnInfo : index.getColumnsInfo()) {
            if (!columnsId.contains(columnInfo.getColumnId())) {
                return false;
            }
        }
        return true;
    }

    public final boolean buildDeleteGroup() {
        if (buildSelect(ESelectType.PRIMARY_KEY)) {
            final StringBuilder pkQry = querySql;
            querySql = new StringBuilder("delete from ");
            querySql.append(getTable().getDbName());
            querySql.append(" where (");
            boolean isFirstKeyProp = true;
            for (DdsIndexDef.ColumnInfo keyProp : getTable().getPrimaryKey().getColumnsInfo()) {
                if (isFirstKeyProp) {
                    isFirstKeyProp = false;
                } else {
                    querySql.append(',');
                }
                querySql.append(keyProp.getColumn().getDbName());
            }
            querySql.append(") in (");
            querySql.append(pkQry);
            querySql.append(')');
            return true;
        }
        return false;
    }

    public final boolean buildUpdateGroup(final Set<Id> propIds) {
        if (propIds != null && !propIds.isEmpty() && buildSelect(ESelectType.PRIMARY_KEY)) {
            final StringBuilder pkQry = querySql;
            querySql = new StringBuilder("update ");
            querySql.append(getTable().getDbName());
            querySql.append(" set ");
            int ownParamPos = 0;
            for (Id propId : propIds) {
                final DdsColumnDef prop = group.getDdsMeta().getColumns().getById(propId, ExtendableDefinitions.EScope.ALL);
                querySql.append(prop.getDbName());
                querySql.append("=? ");
                insParameter(new DbQuery.InputThisPropParam(prop.getId()), ownParamPos);
                ownParamPos++;
            }
            querySql.append("where (");
            boolean isFirstKeyProp = true;
            for (DdsIndexDef.ColumnInfo keyProp : getTable().getPrimaryKey().getColumnsInfo()) {
                if (isFirstKeyProp) {
                    isFirstKeyProp = false;
                } else {
                    querySql.append(',');
                }
                querySql.append(keyProp.getColumn().getDbName());
            }
            querySql.append(") in (");
            querySql.append(pkQry);
            querySql.append(")");
            return true;
        }
        return false;
    }
//Private methods

    private final void addAggregationFunctions(final List<AggregateFunctionCall> functions, final Sqml grpCondSqml, final List<RadSortingDef.Item> orderBy) {
        for (AggregateFunctionCall functionCall : functions) {
            if (functionCall.getFunction() == EAggregateFunction.COUNT) {
                fieldsByPropId.put(ROWS_COUNT_FIELD_COL_ID.toString(), new CountField(this));
            } else {
                addAggregationFunctionCall(functionCall.getColumnId(), functionCall.getFunction());
            }
        }

        if (grpCondSqml != null) {
            addPropsFromSqml(grpCondSqml);
        }

        //sortings columns
        if (!orderBy.isEmpty()) {
            for (RadSortingDef.Item srtItem : orderBy) {
                addQueryProp(srtItem.getColumnId(), EPropUsageType.OTHER);
            }
        }
    }

    private final void buildSelectQueryTree(final RadSelectorPresentationDef pres, final Sqml grpCondSqml, final List<RadSortingDef.Item> orderBy) {
        for (DdsIndexDef.ColumnInfo pkProp : getTable().getPrimaryKey().getColumnsInfo()) {
            addQueryProp(pkProp.getColumnId(), EPropUsageType.FIELD);
        }

        for (RadSelectorPresentationDef.SelectorColumn col : pres.getSelectorColumns()) {
            addQueryProp(col.getPropId(), EPropUsageType.FIELD);
        }

        if (getTable().findClassGuidColumn() != null) {
            addQueryProp(getTable().getClassGuidColumn().getId(), EPropUsageType.FIELD);
        }

        // adding name columns for inserting into tree
        // смотрим заголовок в презентации по умолчанию, если потом презентация редактора изменится и там будут др колонки - докачаются
        // (или ручками можно вытащат в колонки селектора)
        final RadEntityTitleFormatDef f = getEntityClass().getPresentation().getDefaultObjectTitleFormat();
        if (f != null && f.getFormat() != null) {
            for (RadEntityTitleFormatDef.TitleItem titleItem : f.getFormat()) {
                addQueryProp(titleItem.getPropId(), EPropUsageType.FIELD);
            }
        }

        if (grpCondSqml != null) {
            addPropsFromSqml(grpCondSqml);
        }

        //sortings columns
        if (!orderBy.isEmpty()) {
            for (RadSortingDef.Item srtItem : orderBy) {
                addQueryProp(srtItem.getColumnId(), EPropUsageType.OTHER);
            }
        }
    }

    private final RadConditionDef getGroupSelectCondition(final EntityGroup group) throws XmlException {
        //calculating final condition 
        final RadConditionDef presCond = group.getPresentation().getCondition();
        Sqml condSqml = null, condFromSqml = null;
        if (presCond != null) {
            condSqml = addCondSqml(condSqml, presCond.getCondition());
            condSqml = addCondSqml(condSqml, presCond.getProp2ValueCondition().toSqml(group.getSelectionClassDef()));
            if (presCond.getConditionFrom() != null) {
                condFromSqml = addFromSqml(condFromSqml, presCond.getConditionFrom());
            }
        }
        condSqml = addCondSqml(condSqml, group.getAdditionalCond());
        condFromSqml = addFromSqml(condFromSqml, group.getAdditionalFrom());

        final EntityGroup.PropContext propContext = (group.getContext() instanceof EntityGroup.PropContext) ? (EntityGroup.PropContext) group.getContext() : null;
        if (propContext != null) {
            //contextColVals.Clear();
            final RadConditionDef parentSelCond = propContext.getParentSelectCondition();
            if (parentSelCond != null) {
                condSqml = addCondSqml(condSqml, parentSelCond.getCondition());
                condSqml = addCondSqml(condSqml, parentSelCond.getProp2ValueCondition().toSqml(group.getSelectionClassDef()));
                condFromSqml = addFromSqml(condFromSqml, parentSelCond.getConditionFrom());
            }
            final IRadRefPropertyDef dacRefProp = propContext.getParentRefProp();

            //RADIX-2054: destination class condition
            if (dacRefProp.getDestinationClassId().getPrefix() != EDefinitionIdPrefix.ADS_ENTITY_CLASS) {
                if (condSqml == null) {
                    condSqml = createTmpSqml();
                } else {
                    condSqml.getItems().add(Sqml.Text.Factory.newInstance(_AND_));
                }
                condSqml.getItems().add(Sqml.Text.Factory.newInstance("RDX_ADS_META.isClassExtends("));
                condSqml.getItems().add(createThisPropTag(getTable().getClassGuidColumn().getId()));
                condSqml.getItems().add(Sqml.Text.Factory.newInstance(",'" + dacRefProp.getDestinationClassId().toString() + "') != 0"));
            }

            final DdsReferenceDef ptRef = (dacRefProp instanceof RadInnateRefPropDef) ? ((RadInnateRefPropDef) dacRefProp).getReference() : null;
            if (ptRef != null) {
                // условие по константной части ключа (мы должны увидить только те записи у которых эта часть совпадает с нашей)
                for (DdsReferenceDef.ColumnsInfoItem fixedParentRefProp : propContext.getFixedParentRefProps()) {
                    if (condSqml == null) {
                        condSqml = createTmpSqml();
                    } else {
                        condSqml.getItems().add(Sqml.Text.Factory.newInstance(_AND_));
                    }
                    appendThisPropTagTo(condSqml, fixedParentRefProp.getParentColumnId());
                    condSqml.getItems().add(Sqml.Text.Factory.newInstance("="));
                    condSqml.getItems().add(createChildPropTag(ptRef.getChildTableId(), fixedParentRefProp.getChildColumnId()));
                    //condSqml.addNewItem().setSql("=?");
                    //addParameter(new DbQuery.InputChildPropParam(fixedParentRefProp.getChildColumnId()));
                }
                // PK

                final DdsTableDef ptRefChildTable = getArte().getDefManager().getTableDef(ptRef.getChildTableId());
                Sqml uniqueCond = generateUniqueCondSqml(ptRef, propContext.getFixedParentRefProps(), ptRefChildTable.getPrimaryKey());
                final List<DdsIndexDef> childTabIndeces = ptRefChildTable.getIndices().get(ExtendableDefinitions.EScope.ALL);
                if (!childTabIndeces.isEmpty()) {
                    for (DdsIndexDef idx : childTabIndeces) {
                        if (idx.getDbOptions().contains(DdsIndexDef.EDbOption.UNIQUE) || idx.isSecondaryKey()) {
                            final Sqml tmpCond = generateUniqueCondSqml(ptRef, propContext.getFixedParentRefProps(), idx);
                            if (tmpCond != null) {
                                if (uniqueCond == null) {
                                    uniqueCond = tmpCond;
                                } else {
                                    uniqueCond.getItems().add(Sqml.Text.Factory.newInstance(_AND_));
                                    appendCondSqmlTo(uniqueCond, tmpCond);
                                }
                            }
                        }
                    }
                }
                if (uniqueCond != null) {
                    if (condSqml == null) {
                        condSqml = createTmpSqml();
                        condSqml.getItems().add(Sqml.Text.Factory.newInstance("("));
                    } else {
                        condSqml.getItems().add(Sqml.Text.Factory.newInstance(" and ("));
                    }
                    appendCondSqmlTo(condSqml, uniqueCond);
//					condSqml.getItems().add(Sqml.Text.Factory.newInstance(" or "));
//					boolean isFirst = true;
//					for (DdsReferenceDef.ColumnsInfoItem refProp : ptRef.getColumnsInfo()) {
//						if (isFirst) {
//							isFirst = false;
//						} else {
//							condSqml.getItems().add(Sqml.Text.Factory.newInstance(_AND_));
//						}
//						appendThisPropTagTo(condSqml, refProp.getParentColumnId());
//						condSqml.getItems().add(Sqml.Text.Factory.newInstance("="));
//						condSqml.getItems().add(createChildPropTag(ptRef.getChildTableId(), refProp.getChildColumnId()));
//					}
                    condSqml.getItems().add(Sqml.Text.Factory.newInstance(")"));
                }
            }
        } else {
            final EntityGroup.TreeContext treeContext = (group.getContext() instanceof EntityGroup.TreeContext) ? (EntityGroup.TreeContext) group.getContext() : null;
            final RadExplorerItemDef ei = treeContext != null ? treeContext.getExplorerItem() : null;
            if (ei instanceof RadSelectorExplorerItemDef) {//context condition
                if (ei instanceof RadChildRefExplorerItemDef) {//context condition
                    final DdsReferenceDef ref = ((RadChildRefExplorerItemDef) ei).getChildReference();
                    //Sqml complCondSqml = null;
                    for (DdsReferenceDef.ColumnsInfoItem refProp : ref.getColumnsInfo()) {
                        final PropSqlNameTag thisCol = createThisPropTag(refProp.getChildColumnId());
                        final PropSqlNameTag parCol;
                        if (treeContext.getParentPid().getEntityId().equals(ref.getParentTableId())) {//reference to table itself
                            parCol = createParentPropTag(ref.getParentTableId(), refProp.getParentColumnId());
                        } else {//reference to detail table ?
                            final Entity parent = getArte().getEntityObject(treeContext.getParentPid());
                            DdsReferenceDef mdRef = null;
                            for (DdsReferenceDef r : parent.getRadMeta().getDetailsRefs()) {
                                if (r.getChildTableId().equals(ref.getParentTableId())) {
                                    mdRef = r;
                                    break;
                                }
                            }
                            if (mdRef == null) {
                                throw new DefinitionError("Can't select subobjects because reference #" + ref.getId() + " is not child reference of #" + parent.getEntityId(), null);
                            }
                            final Id masterPropId = getArte().getDefManager().getMasterPropIdByDetailPropId(mdRef, parent.getRadMeta(), refProp.getParentColumnId());
                            if (masterPropId == null) {
                                throw new DefinitionError("Can't select subobjects of master-detail object (class #" + parent.getRadMeta().getId().toString() + ")\nbecause subobjects' foreign key based on a details' secondary key (detail table #" + ref.getParentTableId().toString() + ")\nand the key's columns are not published in the master class", null);
                            }
                            parCol = createParentPropTag(treeContext.getParentPid().getEntityId(), masterPropId);
                        }
//						if (bSetComplement && !isPropReadOnly(group.getPresentation(), refProp.getChildColumnId())) {
//							if (complCondSqml == null) {
//								complCondSqml = createTmpSqml();
//							} else {
//								complCondSqml.getItems().add(Sqml.Text.Factory.newInstance(" or "));
//							}
//							complCondSqml.getItems().add(Sqml.Text.Factory.newInstance("("));
//							complCondSqml.getItems().add(thisCol);
//							complCondSqml.getItems().add(Sqml.Text.Factory.newInstance("<>"));
//							complCondSqml.getItems().add(parCol);
//							complCondSqml.getItems().add(Sqml.Text.Factory.newInstance(" or "));
//							complCondSqml.getItems().add(thisCol);
//							complCondSqml.getItems().add(Sqml.Text.Factory.newInstance(" IS NULL)"));
//						} else {
                        if (condSqml == null) {
                            condSqml = createTmpSqml();
                        } else {
                            condSqml.getItems().add(Sqml.Text.Factory.newInstance(_AND_));
                        }
                        condSqml.getItems().add(thisCol);
                        condSqml.getItems().add(Sqml.Text.Factory.newInstance("="));
                        condSqml.getItems().add(parCol);
//						}
                    }
//					if (bSetComplement) {//добавим условие "дополнения" в основное
//						if (condSqml == null) {
//							condSqml = createTmpSqml();
//						} else {
//							condSqml.getItems().add(Sqml.Text.Factory.newInstance(_AND_));
//						}
//						if (complCondSqml == null) { // все колонки ключа readOnly - дополнение пустое множество
//							condSqml.getItems().add(Sqml.Text.Factory.newInstance("1=0"));
//						} else {
//							condSqml.getItems().add(Sqml.Text.Factory.newInstance("("));
//							appendCondSqmlTo(condSqml, complCondSqml);
//							condSqml.getItems().add(Sqml.Text.Factory.newInstance(")"));
//						}
//					}
                }
                final RadSelectorExplorerItemDef sei = (RadSelectorExplorerItemDef) ei;
                if (/*!bSetComplement &&*/sei.getCondition() != null) {
                    condSqml = addCondSqml(condSqml, sei.getCondition().getCondition());
                    condSqml = addCondSqml(condSqml, sei.getCondition().getProp2ValueCondition().toSqml(group.getSelectionClassDef()));
                    condFromSqml = addFromSqml(condFromSqml, sei.getCondition().getConditionFrom());
                }
            }
        }
        if (condSqml != null || condFromSqml != null) {
            return new RadConditionDef(condSqml, condFromSqml);
        }

        return null;
    }

    private void appendSortingColumn(final SqlBuilder builder, final Id propId, final boolean bForSelectParent, final EOrder order, final ESortingColumnPurpose purpose) {
        if (group.getQueryBuilderDelegate() != null) {
            if (group.getQueryBuilderDelegate().appendOrdColDirective(querySql, builder, propId, bForSelectParent, order, purpose)) {
                return;
            }
        }

        if (propId.getPrefix() == EDefinitionIdPrefix.ADS_USER_PROP) {
            querySql.append(UpValSqlBuilder.getValGetSql('\'' + propId.toString() + '\'', builder.getEntityClass().getPropById(propId).getValType(), '\'' + builder.getTable().getId().toString() + '\'', getPidScript(builder.getTable(), builder.getAlias())));
            return;
        }
        final RadPropDef prop = builder.getEntityClass().getPropById(propId);

        if (prop instanceof RadInnatePropDef) {
            final DdsColumnDef dbpProp = builder.getTable().getColumns().getById(prop.getId(), ExtendableDefinitions.EScope.ALL);
            if (dbpProp.getExpression() != null) {
                querySql.append('(');
                querySql.append(builder.translateSqml(dbpProp.getExpression()));
                querySql.append(')');
            } else {
                querySql.append(builder.getAlias());
                querySql.append('.');
                querySql.append(dbpProp.getDbName());
            }
            appendSortingColumnRightPart(prop, order, purpose);
        } else if (prop instanceof RadSqmlPropDef) {
            querySql.append('(');
            querySql.append(translateSqml(((RadSqmlPropDef) prop).getExpression()));
            querySql.append(')');
            appendSortingColumnRightPart(prop, order, purpose);
        } else if (purpose != ESortingColumnPurpose.ORDER_CLAUSE) {
            throw new RadixError("Unsupported property type \"" + prop.getClass().getName() + "\"" + " for sorting with relative pagination", null);
        } else if (prop instanceof RadParentPropDef) {
            SqlBuilder joinBuilder = builder;
            for (IRadRefPropertyDef refProp : ((RadParentPropDef) prop).getRefProps()) {
                joinBuilder = joinBuilder.getParentJoinBuilder(refProp);
            }
            appendSortingColumn(joinBuilder, ((RadParentPropDef) prop).getJoinedPropId(), bForSelectParent, order, purpose);
        } else if (prop instanceof RadInnateRefPropDef) {
            final DdsReferenceDef ref = ((RadInnateRefPropDef) prop).getReference();
            boolean isFirst = true;
            for (DdsReferenceDef.ColumnsInfoItem refProp : ref.getColumnsInfo()) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    querySql.append(',');
                }
                appendSortingColumn(this, refProp.getChildColumnId(), bForSelectParent, order, purpose);
            }
        } else if (prop instanceof RadDetailParentRefPropDef) {
            final JoinSqlBuilder joinBuilder = builder.getJoinBuilder(((RadDetailPropDef) prop).getDetailReference(), true);
            final DdsReferenceDef ref = ((RadDetailParentRefPropDef) prop).getReference();
            boolean isFirst = true;
            for (DdsReferenceDef.ColumnsInfoItem refProp : ref.getColumnsInfo()) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    querySql.append(',');
                }
                appendSortingColumn(joinBuilder, refProp.getChildColumnId(), bForSelectParent, order, purpose);
            }
        } else if (prop instanceof RadDetailPropDef) {
            final JoinSqlBuilder joinBuilder = builder.getJoinBuilder(((RadDetailPropDef) prop).getDetailReference(), true);
            appendSortingColumn(joinBuilder, ((RadDetailPropDef) prop).getJoinedPropId(), bForSelectParent, order, purpose);
        } else {
            throw new RadixError("Unsupported sorting property type \"" + prop.getClass().getName() + "\"", null);
        }
    }

    private void appendSortingColumnRightPart(final RadPropDef propDef, final EOrder order, final ESortingColumnPurpose purpose) {
        if (purpose == ESortingColumnPurpose.ORDER_CLAUSE) {
            if (order == EOrder.DESC) {
                querySql.append(" desc");
            }
        } else if (purpose == ESortingColumnPurpose.RELATIVE_CONDITION_COMPARISON) {
            querySql.append(order == EOrder.ASC ? " >= " : " <= ");
            querySql.append("?");
            addParameter(new SelectQuery.InputPrevObjectPropParam(propDef.getId()));
        } else if (purpose == ESortingColumnPurpose.RELATIVE_CONDITION_EQUALITY) {
            querySql.append(" = ?");
            addParameter(new SelectQuery.InputPrevObjectPropParam(propDef.getId()));
        } else {
            throw new RadixError("Unknown ESortingColumnPurpose: " + purpose);
        }
    }

    private final Sqml createTmpSqml() {
        return Sqml.Factory.newInstance();
    }

    /**
     * Объединяет SQML условия, результат складывается в параметр mainCondSqml и
     * возвращется
     *
     * @param mainCondSqml условие к которому будет осуществляться добавление,
     * если null - будет создано новое условие, если не null - будет
     * модифицировано методом, если addonSqml != null
     * @param addonSqml добавляемое условие, если null, не производится никаких
     * действий
     * @return результирующий SQML, может быть равен null (если оба входных
     * параметра = null)
     */
    private final Sqml addCondSqml(Sqml mainCondSqml, final Sqml addonSqml) throws XmlException {
        if (addonSqml != null) {
            if (mainCondSqml == null) {
                mainCondSqml = createTmpSqml();
            } else {
                mainCondSqml.getItems().add(Sqml.Text.Factory.newInstance(_AND_));
            }
            appendCondSqmlTo(mainCondSqml, addonSqml);
        }
        return mainCondSqml;
    }

    /**
     * Объединяет SQML FROM-ов, результат складывается в параметр mainFromSqml и
     * возвращется
     *
     * @param mainFromSqml from к которому будет осуществляться добавление, если
     * null - будет создан новый, если не null - будет модифицирован методом,
     * если addonSqml != null
     * @param addonSqml добавляемый from, если null, не производится никаких
     * действий
     * @return результирующий SQML, может быть равен null (если оба входных
     * параметра = null)
     */
    private final Sqml addFromSqml(Sqml mainFromSqml, final Sqml addonSqml) throws XmlException {
        if (addonSqml != null) {
            if (mainFromSqml == null) {
                mainFromSqml = createTmpSqml();
            } else {
                mainFromSqml.getItems().add(Sqml.Text.Factory.newInstance(","));
            }

            appendFromSqmlTo(mainFromSqml, addonSqml);
        }
        return mainFromSqml;
    }

    private DbConfiguration getDbConfiguration() {
        if (getArte() != null) {
            return getArte().getDbConfiguration();
        }
        return null;
    }

    private boolean appendCondSqmlTo(final Sqml to, final Sqml sqml) throws XmlException {

        final org.radixware.kernel.common.sqml.Sqml preprocessedSqml = QuerySqmlTranslator.preprocess(sqml, getDbConfiguration(), group.getFltParamValsById());

        final RadixObjects<Sqml.Item> itemList = preprocessedSqml.getItems();
        if (itemList.isEmpty()) {
            return false;
        }
        int userRightParenthesisCount = 0;
        for (Sqml.Item item : itemList) {
            if (item instanceof Sqml.Text) {
                // найдем все закрывающиеся скобочки
                // потом поставим вокруг этого sqml
                // скобочек на один уровень больше
                // тогда пользовательские скобочки не смогут закрывать наши
                final String sql = ((Sqml.Text) item).getText();
                int i = sql.indexOf(')');
                while (i >= 0) {
                    userRightParenthesisCount++;
                    if (i == sql.length() - 1) //строка просмотрена до конца
                    {
                        break;
                    }
                    i = sql.indexOf(')', i + 1);
                }
            }
        }
        final StringBuilder leftProtectiveParenthesis = new StringBuilder("(");
        final StringBuilder rightProtectiveParenthesis = new StringBuilder("\n)");// '\n' - to close one line comments
        for (int i = 0; i < userRightParenthesisCount; i++) {
            leftProtectiveParenthesis.append('(');
            rightProtectiveParenthesis.append(')');
        }
        to.getItems().add(Sqml.Text.Factory.newInstance(leftProtectiveParenthesis.toString()));
        //for (Sqml.Item item : itemList) {
        //	to.getItems().add(item);
        //}
        to.appendFrom(preprocessedSqml);
        to.getItems().add(Sqml.Text.Factory.newInstance(rightProtectiveParenthesis.toString()));
        return true;
    }

    private boolean appendFromSqmlTo(final Sqml to, final Sqml sqml) throws XmlException {
        if (sqml.getItems().isEmpty()) {
            return false;
        }
        to.appendFrom(sqml);
        return true;
    }

    private Sqml generateUniqueCondSqml(final DdsReferenceDef ptRef, final List<DdsReferenceDef.ColumnsInfoItem> fixedParentRefProps, final DdsIndexDef key) throws XmlException {
        boolean bNeedCond = false;
        for (int k = 0; !bNeedCond && k < key.getColumnsInfo().size(); k++) {
            bNeedCond = false;
            final Id columnId = key.getColumnsInfo().get(k).getColumnId();
            for (int j = 0; !bNeedCond && j < ptRef.getColumnsInfo().size(); j++)//if foreign key includes key property... 
            {
                bNeedCond = ptRef.getColumnsInfo().get(j).getChildColumnId().equals(columnId);
            }
            if (bNeedCond) { //...and this prop is not fixed 
                for (DdsReferenceDef.ColumnsInfoItem fixedParentRefProp : fixedParentRefProps) {
                    bNeedCond = !fixedParentRefProp.getChildColumnId().equals(columnId);
                    if (!bNeedCond) {
                        break;
                    }
                }
            }
        }
        if (bNeedCond) {
            final Sqml uniqueCondSqml = createTmpSqml();
            final Sqml oldValuesCondSqml = createTmpSqml();
            uniqueCondSqml.getItems().add(Sqml.Text.Factory.newInstance(" not exists (select 1 from " + getArte().getDefManager().getTableDef(ptRef.getChildTableId()).getDbName() + " DBP_T where "));
            boolean isFirstKeyProp = true;
            for (DdsIndexDef.ColumnInfo keyProp : key.getColumnsInfo()) {
                if (isFirstKeyProp) {
                    isFirstKeyProp = false;
                } else {
                    uniqueCondSqml.getItems().add(Sqml.Text.Factory.newInstance(_AND_));
                }
                uniqueCondSqml.getItems().add(Sqml.Text.Factory.newInstance("DBP_T." + keyProp.getColumn().getDbName() + "="));
                boolean isRefProp = false;
                Id refToPropId = null;
                for (DdsReferenceDef.ColumnsInfoItem refProp : ptRef.getColumnsInfo()) {
                    isRefProp = keyProp.getColumnId().equals(refProp.getChildColumnId());
                    if (isRefProp) {
                        refToPropId = refProp.getParentColumnId();
                        if (!oldValuesCondSqml.getItems().isEmpty()) {
                            oldValuesCondSqml.getItems().add(Sqml.Text.Factory.newInstance(_AND_));
                        }
                        appendThisPropTagTo(oldValuesCondSqml, refToPropId);
                        oldValuesCondSqml.getItems().add(Sqml.Text.Factory.newInstance("="));
                        oldValuesCondSqml.getItems().add(createChildPropTag(ptRef.getChildTableId(), refProp.getChildColumnId()));
                        break;
                    }
                }
                if (isRefProp) {
                    appendThisPropTagTo(uniqueCondSqml, refToPropId);
                } else {
                    uniqueCondSqml.getItems().add(createChildPropTag(ptRef.getChildTableId(), keyProp.getColumnId()));
                }
            }
            uniqueCondSqml.getItems().add(Sqml.Text.Factory.newInstance(")"));
            if (!oldValuesCondSqml.getItems().isEmpty()) {
                uniqueCondSqml.getItems().add(Sqml.Text.Factory.newInstance(" or "));
                appendCondSqmlTo(uniqueCondSqml, oldValuesCondSqml);
            }
            return uniqueCondSqml;

        }
        return null;
    }

    @Override
    public int getNumberOfItemsInParameterValue(final Id parameterId) {
        final Map<Id, Object> paramValues = group.getFltParamValsById();
        final Object value = paramValues.get(parameterId);
        if (value instanceof Arr) {
            return ((Arr) value).size();
        } else if (value == null) {
            if (paramValues.containsKey(parameterId)) {
                return 0;
            } else {
                return super.getNumberOfItemsInParameterValue(parameterId);
            }
        } else {
            return 1;
        }
    }

    @Override
    public boolean isParameterDefined(final Id parameterId) {
        return group.getFltParamValsById().containsKey(parameterId);
    }

}
