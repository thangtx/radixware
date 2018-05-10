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
import java.util.Arrays;
import org.radixware.kernel.common.enums.EIfParamTagOperator;
import java.util.List;
import org.radixware.kernel.common.check.RadixProblem.WarningSuppressionSupport;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.sqml.tags.*;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EParamDirection;
import org.radixware.kernel.common.repository.DbOptionValue;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.scml.TaskTagUtils;
import org.radixware.schemas.xscml.Sqml.Item.EntityRefValue;
import org.radixware.schemas.xscml.Sqml.Item.ThisTableRef;

/**
 * Выгрузчик {@link Sqml} в XmlBeans объект.
 *
 */
class SqmlWriter {

    // there are no instances needed.
    private SqmlWriter() {
    }

    public static void write(final Sqml sqml, final org.radixware.schemas.xscml.Sqml xSqml) {
        RadixObjects<Scml.Item> items = sqml.getItems();
        for (Sqml.Item item : items) {
            if (item instanceof Sqml.Text) {
                final Sqml.Text sql = (Sqml.Text) item;
                final String text = sql.getText();
                xSqml.addNewItem().setSql(text);
            } else if (item instanceof ConstValueTag) {
                final ConstValueTag constValueTag = (ConstValueTag) item;
                write(constValueTag, xSqml.addNewItem().addNewConstValue());
            } else if (item instanceof DbFuncCallTag) {
                final DbFuncCallTag dbFuncCallTag = (DbFuncCallTag) item;
                write(dbFuncCallTag, xSqml.addNewItem().addNewDbFuncCall());
            } else if (item instanceof ElseIfTag) {
                xSqml.addNewItem().addNewElseIf();
            } else if (item instanceof EndIfTag) {
                xSqml.addNewItem().addNewEndIf();
            } else if (item instanceof IdTag) {
                final IdTag idReferenceTag = (IdTag) item;
                write(idReferenceTag, xSqml.addNewItem().addNewId());
            } else if (item instanceof EventCodeTag) {
                final EventCodeTag eventCodeTag = (EventCodeTag) item;
                write(eventCodeTag, xSqml.addNewItem().addNewEventCode());
            } else if (item instanceof DataTag) {
                final DataTag dataTag = (DataTag) item;
                write(dataTag, xSqml.addNewItem().addNewData());
            } else if (item instanceof DbNameTag) {
                final DbNameTag dbNameTag = (DbNameTag) item;
                write(dbNameTag, xSqml.addNewItem().addNewDbName());
            } else if (item instanceof IndexDbNameTag) {
                final IndexDbNameTag indexDbNameTag = (IndexDbNameTag) item;
                write(indexDbNameTag, xSqml.addNewItem().addNewIndexDbName());
            } else if (item instanceof JoinTag) {
                final JoinTag joinTag = (JoinTag) item;
                write(joinTag, xSqml.addNewItem().addNewJoin());
            } else if (item instanceof EntityRefParameterTag) {
                final EntityRefParameterTag parameterTag = (EntityRefParameterTag) item;
                write(parameterTag, xSqml.addNewItem().addNewEntityRefParameter());
            } else if (item instanceof EntityRefValueTag) {
                final EntityRefValueTag valueTag = (EntityRefValueTag) item;
                write(valueTag, xSqml.addNewItem().addNewEntityRefValue());
            } else if (item instanceof ThisTableRefTag) {
                final ThisTableRefTag tag = (ThisTableRefTag) item;
                write(tag, xSqml.addNewItem().addNewThisTableRef());
            } else if (item instanceof XPathTag) {
                final XPathTag tag = (XPathTag) item;
                write(tag, xSqml.addNewItem().addNewXPath());
            } else if (item instanceof ParameterTag) {
                final ParameterTag parameterTag = (ParameterTag) item;
                write(parameterTag, xSqml.addNewItem().addNewParameter());
            } else if (item instanceof ParamValCountTag){
                final ParamValCountTag paramValCountTag = (ParamValCountTag) item;
                write(paramValCountTag, xSqml.addNewItem().addNewParamValCount());
            } else if (item instanceof ParentConditionTag) {
                final ParentConditionTag ParentConditionTag = (ParentConditionTag) item;
                write(ParentConditionTag, xSqml.addNewItem().addNewParentCondition());
            } else if (item instanceof ParentRefPropSqlNameTag) {
                final ParentRefPropSqlNameTag parentRefPropSqlNameTag = (ParentRefPropSqlNameTag) item;
                write(parentRefPropSqlNameTag, xSqml.addNewItem().addNewParentRefPropSqlName());
            } else if (item instanceof IfParamTag) {
                final IfParamTag ifParamTag = (IfParamTag) item;
                write(ifParamTag, xSqml.addNewItem().addNewIfParam());
            } else if (item instanceof PropSqlNameTag) {
                final PropSqlNameTag propSqlNameTag = (PropSqlNameTag) item;
                write(propSqlNameTag, xSqml.addNewItem().addNewPropSqlName());
            } else if (item instanceof SequenceDbNameTag) {
                final SequenceDbNameTag sequenceDbNameTag = (SequenceDbNameTag) item;
                write(sequenceDbNameTag, xSqml.addNewItem().addNewSequenceDbName());
            } else if (item instanceof TableSqlNameTag) {
                final TableSqlNameTag tableSqlNameTag = (TableSqlNameTag) item;
                write(tableSqlNameTag, xSqml.addNewItem().addNewTableSqlName());
            } else if (item instanceof ThisTableIdTag) {
                xSqml.addNewItem().addNewThisTableId();
            } else if (item instanceof ThisTableSqlNameTag) {
                xSqml.addNewItem().addNewThisTableSqlName();
            } else if (item instanceof TypifiedValueTag) {
                final TypifiedValueTag typifiedValueTag = (TypifiedValueTag) item;
                write(typifiedValueTag, xSqml.addNewItem().addNewTypifiedValue());
            } else if (item instanceof TaskTag) {
                final TaskTag taskTag = (TaskTag) item;
                org.radixware.schemas.xscml.TaskTagType xTask = xSqml.addNewItem().addNewTask();
                TaskTagUtils.appendTo(taskTag, xTask);
            } else if (item instanceof TargetDbPreprocessorTag) {
                write((TargetDbPreprocessorTag) item, xSqml.addNewItem().addNewTargetDbPreprocessor());
            }
        }
    }

    private static void write(final TargetDbPreprocessorTag tag, final org.radixware.schemas.xscml.Sqml.Item.TargetDbPreprocessor xTag) {
        xTag.setDbTypeName(tag.getDbTypeName());
        xTag.setDbVersion(tag.getDbVersion());
        xTag.setCheckOptions(tag.isCheckOptions());
        xTag.setCheckVersion(tag.isCheckVersion());
        xTag.setVersionOperator(tag.getVersionOperator());
        if (tag.getDbOptions() != null) {
            for (DbOptionValue dep : tag.getDbOptions()) {
                org.radixware.schemas.xscml.Sqml.Item.TargetDbPreprocessor.Option opt = xTag.addNewOption();
                opt.setName(dep.getOptionName());
                opt.setValue(dep.getMode());
            }
        }
    }

    private static void write(final ConstValueTag constValueTag, final org.radixware.schemas.xscml.Sqml.Item.ConstValue xConstValueTag) {
        final Id enumId = constValueTag.getEnumId();
        xConstValueTag.setEnumId(enumId);

        final Id itemId = constValueTag.getItemId();
        xConstValueTag.setItemId(itemId);

        final String sql = constValueTag.getSql();
        if (sql != null && !sql.isEmpty()) {
            xConstValueTag.setSql(sql);
        }
    }

    private static void write(final DbFuncCallTag dbFuncCallTag, final org.radixware.schemas.xscml.Sqml.Item.DbFuncCall xDbFuncCallTag) {
        final Id funcId = dbFuncCallTag.getFunctionId();
        xDbFuncCallTag.setFuncId(funcId);

        final boolean paramsDefined = dbFuncCallTag.isParamsDefined();
        if (paramsDefined) {
            RadixObjects<DbFuncCallTag.ParamValue> params = dbFuncCallTag.getParamValues();
            org.radixware.schemas.xscml.Sqml.Item.DbFuncCall.Params xParams = xDbFuncCallTag.addNewParams();

            for (DbFuncCallTag.ParamValue param : params) {
                org.radixware.schemas.xscml.Sqml.Item.DbFuncCall.Params.Param xParam = xParams.addNewParam();

                final Id paramId = param.getParamId();
                xParam.setParamId(paramId);

                final String paramValue = param.getValue();
                if (paramValue != null) {
                    xParam.setValue(paramValue);
                }
            }
        }

        final String sql = dbFuncCallTag.getSql();
        if (sql != null && !sql.isEmpty()) {
            xDbFuncCallTag.setSql(sql);
        }
    }

    private static void write(final IdTag idTag, final org.radixware.schemas.xscml.Sqml.Item.Id xIdTag) {
        final Id[] ids = idTag.getPath();
        xIdTag.setPath(Arrays.asList(ids));
        if (idTag.isSoftReference()) {
            xIdTag.setSoft(true);
        }
    }

    private static void write(final EventCodeTag ecTag, final org.radixware.schemas.xscml.Sqml.Item.EventCode xECTag) {
        xECTag.setOwnerId(ecTag.getBundleId());
        xECTag.setStringId(ecTag.getStringId());
    }

    private static void write(final DataTag dataTag, final org.radixware.schemas.xscml.Sqml.Item.Data xDataTag) {
        final Id id = dataTag.getId();
        xDataTag.setId(id);
    }

    private static void write(final DbNameTag dbNameTag, final org.radixware.schemas.xscml.Sqml.Item.DbName xDbNameTag) {
        final Id[] ids = dbNameTag.getPath();
        xDbNameTag.setPath(Arrays.asList(ids));

        final String sql = dbNameTag.getSql();
        if (sql != null && !sql.isEmpty()) {
            xDbNameTag.setSql(sql);
        }
    }

    private static void write(final IndexDbNameTag indexDbNameTag, final org.radixware.schemas.xscml.Sqml.Item.IndexDbName xIndexDbNameTag) {
        final Id tableId = indexDbNameTag.getTableId();
        xIndexDbNameTag.setTableId(tableId);

        final Id indexId = indexDbNameTag.getIndexId();
        if (indexId != null) {
            xIndexDbNameTag.setIndexId(indexId);
        }

        final String sql = indexDbNameTag.getSql();
        if (sql != null && !sql.isEmpty()) {
            xIndexDbNameTag.setSql(sql);
        }
    }

    private static void write(final JoinTag joinTag, final org.radixware.schemas.xscml.Sqml.Item.Join xJoinTag) {
        final JoinTag.Type type = joinTag.getType();
        if (JoinTag.Type.FULL.equals(type)) {
            xJoinTag.setType(org.radixware.schemas.xscml.Sqml.Item.Join.Type.FULL);
        } else if (JoinTag.Type.INNER.equals(type)) {
            xJoinTag.setType(org.radixware.schemas.xscml.Sqml.Item.Join.Type.INNER);
        } else if (JoinTag.Type.LEFT.equals(type)) {
            xJoinTag.setType(org.radixware.schemas.xscml.Sqml.Item.Join.Type.LEFT);
        } else if (JoinTag.Type.RIGHT.equals(type)) {
            xJoinTag.setType(org.radixware.schemas.xscml.Sqml.Item.Join.Type.RIGHT);
        }

        final Id referenceId = joinTag.getReferenceId();
        xJoinTag.setReferenceId(referenceId);

        final String childTableAlias = joinTag.getChildTableAlias();
        if (childTableAlias != null && !childTableAlias.isEmpty()) {
            xJoinTag.setChildTableAlias(childTableAlias);
        }
        final String parentTableAlias = joinTag.getParentTableAlias();
        if (parentTableAlias != null && !parentTableAlias.isEmpty()) {
            xJoinTag.setParentTableAlias(parentTableAlias);
        }
    }

    private static void write(final ParameterTag parameterTag, final org.radixware.schemas.xscml.Sqml.Item.Parameter xParameterTag) {
        final Id paramId = parameterTag.getParameterId();
        xParameterTag.setParamId(paramId);

        if (parameterTag.isLiteral()) {
            xParameterTag.setLiteral(parameterTag.isLiteral());
        }

        if (parameterTag.getDirection() != EParamDirection.IN) {
            xParameterTag.setDirection(parameterTag.getDirection());
        }

        if (parameterTag.getPropId() != null) {
            xParameterTag.setPropId(parameterTag.getPropId());
        }
        
        if (parameterTag.isExpressionList()){
            xParameterTag.setExpressionList(true);
        }
    }
    
    private static void write(final ParamValCountTag paramValCountTag, final org.radixware.schemas.xscml.Sqml.Item.ParamValCount xParamValCountTag){
        xParamValCountTag.setParamId(paramValCountTag.getParameterId());
    }

    private static void write(final ParentConditionTag parentConditionTag, final org.radixware.schemas.xscml.Sqml.Item.ParentCondition xParentConditionTag) {
        final Id tableId = parentConditionTag.getPropOwnerId();
        xParentConditionTag.setTableId(tableId);

        final Id propId = parentConditionTag.getPropId();
        xParentConditionTag.setPropId(propId);

        ParentConditionTag.Operator operator = parentConditionTag.getOperator();
        if (operator == ParentConditionTag.Operator.EQUAL) {
            xParentConditionTag.setOperator(org.radixware.schemas.xscml.Sqml.Item.ParentCondition.Operator.EQUAL);
        } else if (operator == ParentConditionTag.Operator.IS_NOT_NULL) {
            xParentConditionTag.setOperator(org.radixware.schemas.xscml.Sqml.Item.ParentCondition.Operator.IS_NOT_NULL);
        } else if (operator == ParentConditionTag.Operator.IS_NULL) {
            xParentConditionTag.setOperator(org.radixware.schemas.xscml.Sqml.Item.ParentCondition.Operator.IS_NULL);
        } else if (operator == ParentConditionTag.Operator.NOT_EQUAL) {
            xParentConditionTag.setOperator(org.radixware.schemas.xscml.Sqml.Item.ParentCondition.Operator.NOT_EQUAL);
        }
        final String parentPid = parentConditionTag.getParentPid();
        if (parentPid != null && !parentPid.isEmpty()) {
            xParentConditionTag.setParentPid(parentPid);
        }

        final String parentTitle = parentConditionTag.getParentTitle();
        if (parentTitle != null && !parentTitle.isEmpty()) {
            xParentConditionTag.setParentTitle(parentTitle);
        }
    }

    private static void write(final ParentRefPropSqlNameTag parentRefPropSqlNameTag, final org.radixware.schemas.xscml.Sqml.Item.ParentRefPropSqlName xParentRefPropSqlNameTag) {
        final Id propId = parentRefPropSqlNameTag.getPropId();
        xParentRefPropSqlNameTag.setPropId(propId);

        final List<Id> referenceIds = parentRefPropSqlNameTag.getReferenceIds();
        xParentRefPropSqlNameTag.getReferenceIdList().addAll(referenceIds);
    }

    private static void write(final IfParamTag ifParamTag, final org.radixware.schemas.xscml.Sqml.Item.IfParam xIfParamTag) {
        final Id paramId = ifParamTag.getParameterId();
        xIfParamTag.setParamId(paramId);

        final EIfParamTagOperator operator = ifParamTag.getOperator();
        xIfParamTag.setOperation(operator);

        final ValAsStr value = ifParamTag.getValue();
        if (value != null) {
            xIfParamTag.setValue(value.toString());
        }
    }

    private static void write(final PropSqlNameTag propSqlNameTag, final org.radixware.schemas.xscml.Sqml.Item.PropSqlName xPropSqlNameTag) {
        final Id tableId = propSqlNameTag.getPropOwnerId();
        xPropSqlNameTag.setTableId(tableId);

        final Id propId = propSqlNameTag.getPropId();
        xPropSqlNameTag.setPropId(propId);

        final PropSqlNameTag.EOwnerType owner = propSqlNameTag.getOwnerType();
        if (PropSqlNameTag.EOwnerType.CHILD.equals(owner)) {
            xPropSqlNameTag.setOwner(org.radixware.schemas.xscml.Sqml.Item.PropSqlName.Owner.CHILD);
        } else if (PropSqlNameTag.EOwnerType.NONE.equals(owner)) {
            xPropSqlNameTag.setOwner(org.radixware.schemas.xscml.Sqml.Item.PropSqlName.Owner.NONE);
        } else if (PropSqlNameTag.EOwnerType.PARENT.equals(owner)) {
            xPropSqlNameTag.setOwner(org.radixware.schemas.xscml.Sqml.Item.PropSqlName.Owner.PARENT);
        } else if (PropSqlNameTag.EOwnerType.TABLE.equals(owner)) {
            xPropSqlNameTag.setOwner(org.radixware.schemas.xscml.Sqml.Item.PropSqlName.Owner.TABLE);
        } else if (PropSqlNameTag.EOwnerType.THIS.equals(owner)) {
            xPropSqlNameTag.setOwner(org.radixware.schemas.xscml.Sqml.Item.PropSqlName.Owner.THIS);
        }
        final String tableAlias = propSqlNameTag.getTableAlias();
        if (tableAlias != null && !tableAlias.isEmpty()) {
            xPropSqlNameTag.setTableAlias(tableAlias);
        }

        final String sql = propSqlNameTag.getSql();
        if (sql != null && !sql.isEmpty()) {
            xPropSqlNameTag.setSql(sql);
        }

        WarningSuppressionSupport wss = propSqlNameTag.getWarningSuppressionSupport(false);
        if (wss != null && !wss.isEmpty()) {

            int[] warnings = wss.getSuppressedWarnings();
            List<Integer> list = new ArrayList<Integer>(warnings.length);
            for (int w : warnings) {
                list.add(Integer.valueOf(w));
            }
            xPropSqlNameTag.setSuppressedWarnings(list);
        }
    }

    private static void write(final SequenceDbNameTag sequenceDbNameTag, final org.radixware.schemas.xscml.Sqml.Item.SequenceDbName xSequenceDbNameTag) {
        final Id sequenceId = sequenceDbNameTag.getSequenceId();
        xSequenceDbNameTag.setSequenceId(sequenceId);

        SequenceDbNameTag.Postfix postfix = sequenceDbNameTag.getPostfix();
        if (SequenceDbNameTag.Postfix.CUR_VAL.equals(postfix)) {
            xSequenceDbNameTag.setPostfix(org.radixware.schemas.xscml.Sqml.Item.SequenceDbName.Postfix.CUR_VAL);
        } else if (SequenceDbNameTag.Postfix.NEXT_VAL.equals(postfix)) {
            xSequenceDbNameTag.setPostfix(org.radixware.schemas.xscml.Sqml.Item.SequenceDbName.Postfix.NEXT_VAL);
        } else if (SequenceDbNameTag.Postfix.NONE.equals(postfix)) {
            xSequenceDbNameTag.setPostfix(org.radixware.schemas.xscml.Sqml.Item.SequenceDbName.Postfix.NONE);
        }

        final String sql = sequenceDbNameTag.getSql();
        if (sql != null && !sql.isEmpty()) {
            xSequenceDbNameTag.setSql(sql);
        }
    }

    private static void write(final TableSqlNameTag tableSqlNameTag, final org.radixware.schemas.xscml.Sqml.Item.TableSqlName xTableSqlNameTag) {
        final Id tableId = tableSqlNameTag.getTableId();
        xTableSqlNameTag.setTableId(tableId);

        final String tableAlias = tableSqlNameTag.getTableAlias();
        if (tableAlias != null && !tableAlias.isEmpty()) {
            xTableSqlNameTag.setTableAlias(tableAlias);
        }

        final String sql = tableSqlNameTag.getSql();
        if (sql != null && !sql.isEmpty()) {
            xTableSqlNameTag.setSql(sql);
        }
    }

    private static void write(final TypifiedValueTag typifiedValueTag, final org.radixware.schemas.xscml.Sqml.Item.TypifiedValue xTypifiedValueTag) {
        final Id tableId = typifiedValueTag.getPropOwnerId();
        xTypifiedValueTag.setTableId(tableId);

        final Id propId = typifiedValueTag.getPropId();
        xTypifiedValueTag.setPropId(propId);

        final ValAsStr value = typifiedValueTag.getValue();
        if (value != null) {
            xTypifiedValueTag.setValue(value.toString());
        }
        final String displayValue = typifiedValueTag.getDisplayValue();
        xTypifiedValueTag.setDisplayValue(displayValue);

        if (typifiedValueTag.isLiteral()) {
            xTypifiedValueTag.setLiteral(true);
        }
    }

    private static void write(final EntityRefParameterTag tag, final org.radixware.schemas.xscml.Sqml.Item.EntityRefParameter xTag) {
        xTag.setParamId(tag.getParameterId());
        xTag.setReferencedTableId(tag.getReferencedTableId());
        xTag.setPidTranslationSecondaryKeyId(tag.getPidTranslationSecondaryKeyId());
        xTag.setPidTranslationMode(tag.getPidTranslationMode());
        if (tag.isExpressionList()){
            xTag.setExpressionList(true);
        }
        if (tag.isLiteral()){
            xTag.setLiteral(true);
        }
    }

    private static void write(final EntityRefValueTag tag, final EntityRefValue xTag) {
        xTag.setDisplayValue(tag.getDisplayValue());
        xTag.setReferencedTableId(tag.getReferencedTableId());
        xTag.setReferencedObjectPidAsStr(tag.getReferencedPidAsStr());
        xTag.setPidTranslationSecondaryKeyId(tag.getPidTranslationSecondaryKeyId());
        xTag.setPidTranslationMode(tag.getPidTranslationMode());
        xTag.setLiteral(tag.isLiteral());
    }

    private static void write(final ThisTableRefTag tag, final ThisTableRef xTag) {
        xTag.setPidTranslationSecondaryKeyId(tag.getPidTranslationSecondaryKeyId());
        xTag.setPidTranslationMode(tag.getPidTranslationMode());
    }

    private static void write(final XPathTag tag, final org.radixware.schemas.xscml.Sqml.Item.XPath xTag) {
        for (XPathTag.Item item : tag.getItems()) {
            final org.radixware.schemas.xscml.Sqml.Item.XPath.Item2 xItem = xTag.addNewItem();

            xItem.setName(item.getName());
            xItem.setSchemaId(item.getSchemaId());
            if (item.getIndex() != null) {
                xItem.setIndex(item.getIndex().longValue());
            }
            if (item.isAttribute()) {
                xItem.setIsAttribute(item.isAttribute());
            }
            final String condition = item.getCondition();
            if (condition != null && !condition.isEmpty()) {
                xItem.setCondition(condition);
            }
        }
    }
}
