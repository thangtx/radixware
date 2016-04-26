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

import java.util.ArrayList;
import org.radixware.kernel.common.enums.EIfParamTagOperator;
import java.util.List;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.sqml.tags.*;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.exceptions.RadixObjectError;
import org.radixware.kernel.common.repository.DbOptionValue;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.scml.Scml;

/**
 * Загрузчик {@link Sqml} из XmlBeans объекта.
 *
 */
class SqmlLoader {
    // there are no instances needed.

    private SqmlLoader() {
    }

    /**
     * Загрузить XmlBeans объект в {@link Sqml}.
     *
     * @param xSqml can be null
     */
    public static void load(final Sqml sqml, final org.radixware.schemas.xscml.Sqml xSqml) {
        sqml.getItems().clear();
        append(sqml, xSqml);
    }

    /**
     * Добавить XmlBeans объект в {@link Sqml}.
     *
     * @param xSqml can be null
     */
    public static void append(final Sqml sqml, final org.radixware.schemas.xscml.Sqml xSqml) {
        if (xSqml == null) {
            return;
        }

        final List<org.radixware.schemas.xscml.Sqml.Item> xItems = xSqml.getItemList();
        if (xItems == null) {
            return;
        }
        final RadixObjects<Scml.Item> items = sqml.getItems();

        for (org.radixware.schemas.xscml.Sqml.Item xItem : xItems) {
            Sqml.Item item;
            if (xItem.isSetSql()) {
                final String xSql = xItem.getSql();
                final Sqml.Text sql = load(xSql);
                item = sql;
            } else if (xItem.isSetConstValue()) {
                final org.radixware.schemas.xscml.Sqml.Item.ConstValue xConstValueTag = xItem.getConstValue();
                ConstValueTag constValueTag = load(xConstValueTag);
                item = constValueTag;
            } else if (xItem.isSetDbFuncCall()) {
                final org.radixware.schemas.xscml.Sqml.Item.DbFuncCall xDbFuncCallTag = xItem.getDbFuncCall();
                DbFuncCallTag dbDbFuncCallTag = load(xDbFuncCallTag);
                item = dbDbFuncCallTag;
            } else if (xItem.isSetElseIf()) {
                item = ElseIfTag.Factory.newInstance();
            } else if (xItem.isSetEndIf()) {
                item = EndIfTag.Factory.newInstance();
            } else if (xItem.isSetId()) {
                final org.radixware.schemas.xscml.Sqml.Item.Id xId = xItem.getId();
                IdTag idTag = load(xId);
                item = idTag;
            } else if (xItem.isSetEventCode()) {
                item = load(xItem.getEventCode());
            } else if (xItem.isSetData()) {
                final org.radixware.schemas.xscml.Sqml.Item.Data xData = xItem.getData();
                DataTag dataTag = load(xData);
                item = dataTag;
            } else if (xItem.isSetDbName()) {
                final org.radixware.schemas.xscml.Sqml.Item.DbName xDbName = xItem.getDbName();
                DbNameTag dbNameTag = load(xDbName);
                item = dbNameTag;
            } else if (xItem.isSetIndexDbName()) {
                final org.radixware.schemas.xscml.Sqml.Item.IndexDbName xIndexDbNameTag = xItem.getIndexDbName();
                IndexDbNameTag indexDbNameTag = load(xIndexDbNameTag);
                item = indexDbNameTag;
            } else if (xItem.isSetJoin()) {
                final org.radixware.schemas.xscml.Sqml.Item.Join xJoinTag = xItem.getJoin();
                JoinTag joinTag = load(xJoinTag);
                item = joinTag;
            } else if (xItem.isSetParentCondition()) {
                final org.radixware.schemas.xscml.Sqml.Item.ParentCondition xParentConditionTag = xItem.getParentCondition();
                ParentConditionTag parentConditionTag = load(xParentConditionTag);
                item = parentConditionTag;
            } else if (xItem.isSetParentRefPropSqlName()) {
                final org.radixware.schemas.xscml.Sqml.Item.ParentRefPropSqlName xParentRefPropSqlNameTag = xItem.getParentRefPropSqlName();
                ParentRefPropSqlNameTag parentRefPropSqlNameTag = load(xParentRefPropSqlNameTag);
                item = parentRefPropSqlNameTag;
            } else if (xItem.isSetIfParam()) {
                final org.radixware.schemas.xscml.Sqml.Item.IfParam xIfParamTag = xItem.getIfParam();
                IfParamTag ifParamTag = load(xIfParamTag);
                item = ifParamTag;
            } else if (xItem.isSetPropSqlName()) {
                final org.radixware.schemas.xscml.Sqml.Item.PropSqlName xPropSqlNameTag = xItem.getPropSqlName();
                PropSqlNameTag propSqlNameTag = load(xPropSqlNameTag);
                item = propSqlNameTag;
            } else if (xItem.isSetSequenceDbName()) {
                final org.radixware.schemas.xscml.Sqml.Item.SequenceDbName xSequenceDbNameTag = xItem.getSequenceDbName();
                SequenceDbNameTag sequenceDbNameTag = load(xSequenceDbNameTag);
                item = sequenceDbNameTag;
            } else if (xItem.isSetTableSqlName()) {
                final org.radixware.schemas.xscml.Sqml.Item.TableSqlName xTableSqlNameTag = xItem.getTableSqlName();
                TableSqlNameTag tableSqlNameTag = load(xTableSqlNameTag);
                item = tableSqlNameTag;
            } else if (xItem.isSetThisTableId()) {
                item = ThisTableIdTag.Factory.newInstance();
            } else if (xItem.isSetThisTableSqlName()) {
                item = ThisTableSqlNameTag.Factory.newInstance();
            } else if (xItem.isSetTypifiedValue()) {
                final org.radixware.schemas.xscml.Sqml.Item.TypifiedValue xTypifiedValueTag = xItem.getTypifiedValue();
                TypifiedValueTag typifiedValueTag = load(xTypifiedValueTag);
                item = typifiedValueTag;
            } else if (xItem.isSetXPath()) {
                final org.radixware.schemas.xscml.Sqml.Item.XPath xxPathTag = xItem.getXPath();
                XPathTag xPathTag = load(xxPathTag);
                item = xPathTag;
            } else if (xItem.isSetParameter()) {
                final org.radixware.schemas.xscml.Sqml.Item.Parameter xParameterTag = xItem.getParameter();
                ParameterTag parameterTag = load(xParameterTag);
                item = parameterTag;
            } else if (xItem.isSetEntityRefParameter()) {
                final org.radixware.schemas.xscml.Sqml.Item.EntityRefParameter xParameterTag = xItem.getEntityRefParameter();
                EntityRefParameterTag parameterTag = load(xParameterTag);
                item = parameterTag;
            } else if (xItem.isSetEntityRefValue()) {
                final org.radixware.schemas.xscml.Sqml.Item.EntityRefValue xValueTag = xItem.getEntityRefValue();
                EntityRefValueTag tag = load(xValueTag);
                item = tag;
            } else if (xItem.isSetThisTableRef()) {
                final org.radixware.schemas.xscml.Sqml.Item.ThisTableRef xTag = xItem.getThisTableRef();
                ThisTableRefTag tag = load(xTag);
                item = tag;
            } else if (xItem.isSetTask()) {
                final org.radixware.schemas.xscml.TaskTagType xTag = xItem.getTask();
                TaskTag tag = TaskTag.Factory.loadFrom(xTag);
                item = tag;
            } else if (xItem.isSetOracleHint()) {
                continue;
            } else if (xItem.isSetTargetDbPreprocessor()) {
                item = load(xItem.getTargetDbPreprocessor());
            } else {
                throw new RadixObjectError("Unknown SQML tag: " + String.valueOf(xItem), sqml);
            }
            items.add(item);
        }
    }

    private static Scml.Text load(final String xSql) {
        final Scml.Text sql = Scml.Text.Factory.newInstance();
        sql.setText(xSql);
        return sql;
    }

    private static TargetDbPreprocessorTag load(final org.radixware.schemas.xscml.Sqml.Item.TargetDbPreprocessor xTag) {
        final TargetDbPreprocessorTag tag = TargetDbPreprocessorTag.Factory.newInstance();
        tag.setDbTypeName(xTag.getDbTypeName());
        tag.setCheckOptions(xTag.getCheckOptions());
        tag.setCheckVersion(xTag.getCheckVersion());
        if (xTag.isSetDbVersion()) {
            tag.setDbVersion(xTag.getDbVersion());
        }
        if (xTag.isSetVersionOperator()) {
            tag.setVersionOperator(xTag.getVersionOperator());
        }
        if (xTag.getOptionList() != null) {
            final List<DbOptionValue> options = new ArrayList<>();
            for (org.radixware.schemas.xscml.Sqml.Item.TargetDbPreprocessor.Option xOpt : xTag.getOptionList()) {
                options.add(new DbOptionValue(xOpt.getName(), xOpt.getValue()));
            }
            tag.setOptions(options);
        }
        return tag;
    }

    private static ConstValueTag load(final org.radixware.schemas.xscml.Sqml.Item.ConstValue xConstValueTag) {
        final ConstValueTag constValueTag = ConstValueTag.Factory.newInstance();

        final Id enumId = xConstValueTag.getEnumId();
        constValueTag.setEnumId(enumId);

        final Id itemId = xConstValueTag.getItemId();
        constValueTag.setItemId(itemId);

        final String sql = xConstValueTag.getSql();
        constValueTag.setSql(sql);

        return constValueTag;
    }

    private static DbFuncCallTag load(final org.radixware.schemas.xscml.Sqml.Item.DbFuncCall xDbFuncCallTag) {
        final DbFuncCallTag dbFuncCallTag = DbFuncCallTag.Factory.newInstance();

        final Id funcId = xDbFuncCallTag.getFuncId();
        dbFuncCallTag.setFunctionId(funcId);

        final boolean paramsDefined = xDbFuncCallTag.isSetParams();
        dbFuncCallTag.setParamsDefined(paramsDefined);

        if (paramsDefined) {
            final List<org.radixware.schemas.xscml.Sqml.Item.DbFuncCall.Params.Param> xParams = xDbFuncCallTag.getParams().getParamList();
            if (xParams != null) {
                for (org.radixware.schemas.xscml.Sqml.Item.DbFuncCall.Params.Param xParam : xParams) {
                    final DbFuncCallTag.ParamValue param = DbFuncCallTag.ParamValue.Factory.newInstance();

                    final Id paramId = xParam.getParamId();
                    param.setParamId(paramId);

                    final String paramValue = (xParam.isSetValue() ? xParam.getValue() : null);
                    param.setValue(paramValue);

                    dbFuncCallTag.getParamValues().add(param);
                }
            }
        }

        final String sql = xDbFuncCallTag.getSql();
        dbFuncCallTag.setSql(sql);

        return dbFuncCallTag;
    }

    private static IdTag load(final org.radixware.schemas.xscml.Sqml.Item.Id xIdTag) {
        final List<Id> idList = xIdTag.getPath();

        final Id[] ids = (idList != null ? idList.toArray(new Id[idList.size()]) : new Id[0]);
        final IdTag idTag = IdTag.Factory.newInstance(ids, xIdTag.getSoft());
        return idTag;
    }

    private static EventCodeTag load(final org.radixware.schemas.xscml.Sqml.Item.EventCode xECTag) {
        return EventCodeTag.Factory.newInstance(xECTag.getOwnerId(), xECTag.getStringId());
    }

    private static DataTag load(final org.radixware.schemas.xscml.Sqml.Item.Data xDataTag) {
        final Id id = xDataTag.getId();
        final DataTag dataTag = DataTag.Factory.newInstance(id);
        return dataTag;
    }

    private static DbNameTag load(final org.radixware.schemas.xscml.Sqml.Item.DbName xDbNameTag) {
        final List<Id> idList = xDbNameTag.getPath();
        final Id[] ids = (idList != null ? idList.toArray(new Id[idList.size()]) : new Id[0]);
        final DbNameTag dbNameTag = DbNameTag.Factory.newInstance(ids);

        final String sql = xDbNameTag.getSql();
        dbNameTag.setSql(sql);

        return dbNameTag;
    }

    private static IndexDbNameTag load(final org.radixware.schemas.xscml.Sqml.Item.IndexDbName xIndexDbNameTag) {
        final IndexDbNameTag indexDbNameTag = IndexDbNameTag.Factory.newInstance();

        final Id tableId = xIndexDbNameTag.getTableId();
        indexDbNameTag.setTableId(tableId);

        if (xIndexDbNameTag.isSetIndexId()) {
            final Id indexId = xIndexDbNameTag.getIndexId();
            indexDbNameTag.setIndexId(indexId);
        }

        final String sql = xIndexDbNameTag.getSql();
        indexDbNameTag.setSql(sql);

        return indexDbNameTag;
    }

    private static JoinTag load(final org.radixware.schemas.xscml.Sqml.Item.Join xJoinTag) {
        final JoinTag joinTag = JoinTag.Factory.newInstance();

        final org.radixware.schemas.xscml.Sqml.Item.Join.Type.Enum xType = xJoinTag.getType();
        final JoinTag.Type type = JoinTag.Type.valueOf(xType.toString());
        joinTag.setType(type);

        final Id referenceId = xJoinTag.getReferenceId();
        joinTag.setReferenceId(referenceId);

        if (xJoinTag.isSetChildTableAlias()) {
            final String childTableAlias = xJoinTag.getChildTableAlias();
            joinTag.setChildTableAlias(childTableAlias);
        }

        if (xJoinTag.isSetParentTableAlias()) {
            final String parentTableAlias = xJoinTag.getParentTableAlias();
            joinTag.setParentTableAlias(parentTableAlias);
        }

        return joinTag;
    }

    private static ParameterTag load(final org.radixware.schemas.xscml.Sqml.Item.Parameter xParameterTag) {
        final ParameterTag parameterTag = ParameterTag.Factory.newInstance();

        final Id paramId = xParameterTag.getParamId();
        parameterTag.setParameterId(paramId);

        if (xParameterTag.isSetLiteral()) {
            parameterTag.setLiteral(xParameterTag.getLiteral());
        }

        if (xParameterTag.isSetDirection()) {
            parameterTag.setDirection(xParameterTag.getDirection());
        }

        if (xParameterTag.isSetPropId()) {
            parameterTag.setPropId(xParameterTag.getPropId());
        }

        return parameterTag;
    }

    private static ParentConditionTag load(final org.radixware.schemas.xscml.Sqml.Item.ParentCondition xParentConditionTag) {
        final ParentConditionTag parentConditionTag = ParentConditionTag.Factory.newInstance();

        final Id tableId = xParentConditionTag.getTableId();
        parentConditionTag.setPropOwnerId(tableId);

        final Id propId = xParentConditionTag.getPropId();
        parentConditionTag.setPropId(propId);

        final org.radixware.schemas.xscml.Sqml.Item.ParentCondition.Operator.Enum xOperator = xParentConditionTag.getOperator();
        ParentConditionTag.Operator operator = ParentConditionTag.Operator.valueOf(xOperator.toString());
        parentConditionTag.setOperator(operator);

        if (xParentConditionTag.isSetParentPid()) {
            final String parentPid = xParentConditionTag.getParentPid();
            parentConditionTag.setParentPid(parentPid);
        }

        if (xParentConditionTag.isSetParentTitle()) {
            final String parentTitle = xParentConditionTag.getParentTitle();
            parentConditionTag.setParentTitle(parentTitle);
        }

        return parentConditionTag;
    }

    private static ParentRefPropSqlNameTag load(final org.radixware.schemas.xscml.Sqml.Item.ParentRefPropSqlName xParentRefPropSqlNameTag) {
        final ParentRefPropSqlNameTag parentRefPropSqlNameTag = ParentRefPropSqlNameTag.Factory.newInstance();

        final Id propId = xParentRefPropSqlNameTag.getPropId();
        parentRefPropSqlNameTag.setPropId(propId);

        final List<Id> xReferenceIds = xParentRefPropSqlNameTag.getReferenceIdList();
        parentRefPropSqlNameTag.getReferenceIds().addAll(xReferenceIds);

        return parentRefPropSqlNameTag;
    }

    private static IfParamTag load(final org.radixware.schemas.xscml.Sqml.Item.IfParam xIfParamTag) {
        final IfParamTag ifParamTag = IfParamTag.Factory.newInstance();

        final Id paramId = xIfParamTag.getParamId();
        ifParamTag.setParameterId(paramId);

        final EIfParamTagOperator operator = xIfParamTag.getOperation();
        ifParamTag.setOperator(operator);

        if (xIfParamTag.isSetValue()) {
            final String value = xIfParamTag.getValue();
            ifParamTag.setValue(ValAsStr.Factory.loadFrom(value));
        }

        return ifParamTag;
    }

    private static PropSqlNameTag load(final org.radixware.schemas.xscml.Sqml.Item.PropSqlName xPropSqlNameTag) {
        final PropSqlNameTag propSqlNameTag = PropSqlNameTag.Factory.newInstance(xPropSqlNameTag.isSetSuppressedWarnings() ? xPropSqlNameTag.getSuppressedWarnings() : null);

        final Id tableId = xPropSqlNameTag.getTableId();
        propSqlNameTag.setPropOwnerId(tableId);

        final Id propId = xPropSqlNameTag.getPropId();
        propSqlNameTag.setPropId(propId);

        org.radixware.schemas.xscml.Sqml.Item.PropSqlName.Owner.Enum xOwner = xPropSqlNameTag.getOwner();
        PropSqlNameTag.EOwnerType ownerType = PropSqlNameTag.EOwnerType.valueOf(xOwner.toString());
        propSqlNameTag.setOwnerType(ownerType);

        if (xPropSqlNameTag.isSetTableAlias()) {
            final String tableAlias = xPropSqlNameTag.getTableAlias();
            propSqlNameTag.setTableAlias(tableAlias);
        }

        final String sql = xPropSqlNameTag.getSql();
        propSqlNameTag.setSql(sql);

        return propSqlNameTag;
    }

    private static EntityRefParameterTag load(final org.radixware.schemas.xscml.Sqml.Item.EntityRefParameter xTag) {
        final EntityRefParameterTag tag = EntityRefParameterTag.Factory.newInstance();

        tag.setParameterId(xTag.getParamId());
        tag.setReferencedTableId(xTag.getReferencedTableId());
        tag.setPidTranslationSecondaryKeyId(xTag.getPidTranslationSecondaryKeyId());
        tag.setPidTranslationMode(xTag.getPidTranslationMode());
        return tag;
    }

    private static EntityRefValueTag load(final org.radixware.schemas.xscml.Sqml.Item.EntityRefValue xTag) {
        final EntityRefValueTag tag = EntityRefValueTag.Factory.newInstance();

        tag.setDisplayValue(xTag.getDisplayValue());
        tag.setReferencedTableId(xTag.getReferencedTableId());
        tag.setReferencedPidAsStr(xTag.getReferencedObjectPidAsStr());
        tag.setPidTranslationSecondaryKeyId(xTag.getPidTranslationSecondaryKeyId());
        tag.setPidTranslationMode(xTag.getPidTranslationMode());
        tag.setLiteral(xTag.isSetLiteral() && xTag.getLiteral());
        return tag;
    }

    private static ThisTableRefTag load(final org.radixware.schemas.xscml.Sqml.Item.ThisTableRef xTag) {
        final ThisTableRefTag tag = ThisTableRefTag.Factory.newInstance();
        tag.setPidTranslationSecondaryKeyId(xTag.getPidTranslationSecondaryKeyId());
        tag.setPidTranslationMode(xTag.getPidTranslationMode());
        return tag;
    }

    private static SequenceDbNameTag load(final org.radixware.schemas.xscml.Sqml.Item.SequenceDbName xSequenceDbNameTag) {
        final SequenceDbNameTag sequenceDbNameTag = SequenceDbNameTag.Factory.newInstance();

        final Id sequenceId = xSequenceDbNameTag.getSequenceId();
        sequenceDbNameTag.setSequenceId(sequenceId);

        org.radixware.schemas.xscml.Sqml.Item.SequenceDbName.Postfix.Enum xPostfix = xSequenceDbNameTag.getPostfix();
        SequenceDbNameTag.Postfix postfix = SequenceDbNameTag.Postfix.valueOf(xPostfix.toString());
        sequenceDbNameTag.setPostfix(postfix);

        final String sql = xSequenceDbNameTag.getSql();
        sequenceDbNameTag.setSql(sql);

        return sequenceDbNameTag;
    }

    private static TableSqlNameTag load(final org.radixware.schemas.xscml.Sqml.Item.TableSqlName xTableSqlNameTag) {
        TableSqlNameTag tableSqlNameTag = TableSqlNameTag.Factory.newInstance();

        final Id tableId = xTableSqlNameTag.getTableId();
        tableSqlNameTag.setTableId(tableId);

        if (xTableSqlNameTag.isSetTableAlias()) {
            final String tableAlias = xTableSqlNameTag.getTableAlias();
            tableSqlNameTag.setTableAlias(tableAlias);
        }

        final String sql = xTableSqlNameTag.getSql();
        tableSqlNameTag.setSql(sql);

        return tableSqlNameTag;
    }

    private static TypifiedValueTag load(final org.radixware.schemas.xscml.Sqml.Item.TypifiedValue xTypifiedValueTag) {
        TypifiedValueTag typifiedValueTag = TypifiedValueTag.Factory.newInstance();

        final Id tableId = xTypifiedValueTag.getTableId();
        typifiedValueTag.setPropOwnerId(tableId);

        final Id propId = xTypifiedValueTag.getPropId();
        typifiedValueTag.setPropId(propId);

        if (xTypifiedValueTag.isSetValue()) {
            final String value = xTypifiedValueTag.getValue();
            typifiedValueTag.setValue(ValAsStr.Factory.loadFrom(value));
        }

        if (xTypifiedValueTag.isSetLiteral()) {
            typifiedValueTag.setLiteral(xTypifiedValueTag.getLiteral());
        }

        final String displayValue = xTypifiedValueTag.getDisplayValue();
        typifiedValueTag.setDisplayValue(displayValue);

        return typifiedValueTag;
    }

    private static XPathTag load(final org.radixware.schemas.xscml.Sqml.Item.XPath xxPathTag) {
        final XPathTag xPathTag = XPathTag.Factory.newInstance();
        for (org.radixware.schemas.xscml.Sqml.Item.XPath.Item2 xItem : xxPathTag.getItemList()) {
            final String name = xItem.getName();
            final boolean isAttribute = xItem.isSetIsAttribute() && xItem.getIsAttribute();
            final Id schemaId = xItem.getSchemaId();
            final Long index = (xItem.isSetIndex() ? Long.valueOf(xItem.getIndex()) : null);

            final XPathTag.Item item = XPathTag.Item.Factory.newInstance();
            item.setName(name);
            item.setIndex(index);
            item.setIsAttribute(isAttribute);
            item.setSchemaId(schemaId);

            if (xItem.isSetCondition()) {
                item.setCondition(xItem.getCondition());
            }

            xPathTag.getItems().add(item);
        }
        return xPathTag;
    }
}
