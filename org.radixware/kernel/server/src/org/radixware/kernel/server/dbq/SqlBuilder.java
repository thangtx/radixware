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

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EAggregateFunction;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.exceptions.TagTranslateError;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqml.ISqmlEnvironment;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.sqml.tags.PropSqlNameTag;
import org.radixware.kernel.common.sqml.translate.ISqmlPreprocessorConfig;
import org.radixware.kernel.server.arte.Arte;
import static org.radixware.kernel.server.dbq.QuerySqlBuilder.ROWS_COUNT_FIELD_ALIAS;
import org.radixware.kernel.server.dbq.sqml.QuerySqmlTranslator;
import org.radixware.kernel.server.dbq.sqml.ServerPreprocessorConfig;
import org.radixware.kernel.server.exceptions.DbQueryBuilderError;
import org.radixware.kernel.server.meta.clazzes.IRadRefPropertyDef;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.meta.clazzes.RadDetailParentRefPropDef;
import org.radixware.kernel.server.meta.clazzes.RadDetailPropDef;
import org.radixware.kernel.server.meta.clazzes.RadInnatePropDef;
import org.radixware.kernel.server.meta.clazzes.RadJoinPropDef;
import org.radixware.kernel.server.meta.clazzes.RadParentPropDef;
import org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef;
import org.radixware.kernel.server.meta.clazzes.RadPropDef;
import org.radixware.kernel.server.meta.clazzes.RadSqmlPropDef;
import org.radixware.kernel.server.meta.clazzes.RadUserRefPropDef;

public abstract class SqlBuilder {

    /**
     * @return the arte
     */
    public Arte getArte() {
        return arte;
    }
//Enum

    public static enum EQueryContextType {

        /**
         * query for selector used for foreign key value editing
         */
        PARENT_SELECTOR,
        /**
         * other queries
         */
        OTHER
    }

    public static enum EQueryBuilderAliasPolicy {

        /**
         * Use aliases for main query table and for joined table. Default.
         */
        USE_ALIASES,
        /**
         * Do not use aliases for main query table and for joined table. Not
         * recomended for queries with joins
         */
        DO_NOT_USE_ALIASES
    }

    public static enum EPropUsageType {

        /**
         * Property must be returned as query field
         */
        FIELD,
        
        /*Property used as an argument of aggregation function*/
        AGGREGATION_FUNC_ARG,
        
        /**
         * other usages, for example, as expression args
         */
        OTHER
    }
//Constants
    protected static final String TABLE_ALIAS_PREFIX = "DBP_T";
    protected static final String FIELD_ALIAS_PREFIX = "DBP_F";
//properties
    private final Arte arte;
    private final EQueryContextType queryCntxType;
    private final EQueryBuilderAliasPolicy aliasPolicy;

    /**
     * @return The query table alias
     */
    public String getAlias() {
        if (getAliasPolicy() == EQueryBuilderAliasPolicy.USE_ALIASES) {
            return alias;
        } else {
            return null;
        }
    }

    public EQueryBuilderAliasPolicy getAliasPolicy() {
        return aliasPolicy;
    }

    public EQueryContextType getQueryCntxType() {
        return queryCntxType;
    }

//Subclasses "Field"
    public static class Field {

        private final SqlBuilder builder;
        private String alias;
        private final Definition prop;
        private int index;
        //Constructors

        Field(final SqlBuilder builder, final Definition prop, final String alias) {
            this(builder, prop, alias, 0);
        }

        Field(final SqlBuilder builder, final Definition prop, final String alias, final int index) {
            this.builder = builder;
            this.alias = alias;
            this.prop = prop;
            this.index = index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public boolean isExpression() {
            if (getProp() instanceof RadPropDef) {
                return getProp() instanceof RadSqmlPropDef;
            }
            if (getProp() instanceof DdsColumnDef) {
                return ((DdsColumnDef) getProp()).isExpression();
            }
            throw new IllegalUsageError("Unsupported property");
        }

        public Sqml getExpression() {
            if (getProp() instanceof DdsColumnDef) {
                return ((DdsColumnDef) getProp()).getExpression();
            }
            if (getProp() instanceof RadSqmlPropDef) {
                return ((RadSqmlPropDef) getProp()).getExpression();
            }
            return null;
        }

        /**
         * @return the builder
         */
        public SqlBuilder getBuilder() {
            return builder;
        }

        /**
         * @return the alias
         */
        public String getAlias() {
            return alias;
        }

        public int getIndex() {
            return index;
        }

        /**
         * @return the prop
         */
        public Definition getProp() {
            return prop;
        }
        
        public Id getId(){
            return prop.getId();
        }
        
        public EValType getValType(){
            if (prop instanceof DdsColumnDef) {
                return ((DdsColumnDef) prop).getValType();
            }
            if (prop instanceof RadPropDef) {
                return ((RadPropDef) prop).getValType();
            }
            throw new IllegalUsageError("Unsupported property");
        }
        
        public String getDbType(){
            if (prop instanceof DdsColumnDef) {
                return ((DdsColumnDef) prop).getDbType();
            }
            if (prop instanceof RadPropDef) {
                return ((RadPropDef) prop).getDbType();
            }
            throw new IllegalUsageError("Unsupported property");            
        }
    }

    protected static class JoinField extends Field {
        //Public fields

        public Field joinField;
        //Constructors

        JoinField(
                final SqlBuilder builder,
                final RadJoinPropDef prop,
                final String alias,
                final Field joinField) {
            super(builder, prop, alias);
            this.joinField = joinField;
        }
    }//Protected fields
    
    protected static interface IAggregationField{
        EAggregateFunction getFunction();
    }    
    
    protected static final class AggregationJoinField extends JoinField implements IAggregationField{
        
        private final EAggregateFunction function;
        
        AggregationJoinField(final SqlBuilder builder, final RadJoinPropDef prop,  final Field joinField, final EAggregateFunction function){
            super(builder, prop,  joinField.getAlias(), joinField);
            this.function = function;
        }
        
        @Override
        public EAggregateFunction getFunction(){
            return function;
        }
        
        @Override
        public EValType getValType() {           
            return function==EAggregateFunction.AVG ? EValType.NUM : super.getValType();
        }        
                
        @Override
        public Id getId(){
            /*Definition targetProp = this.getProp();
            for (Field field=this; field instanceof JoinField; field=((JoinField)field).joinField){
               targetProp = field.prop;
            }*/
            return Id.Factory.append(super.getId(), "_"+function.getValue());
        }
    }    
    
    protected static final class CountField extends Field implements IAggregationField{
        
        CountField(final SqlBuilder builder){
            super(builder,null,ROWS_COUNT_FIELD_ALIAS);
        }

        @Override
        public EAggregateFunction getFunction() {
            return EAggregateFunction.COUNT;
        }

        @Override
        public EValType getValType() {
            return EValType.INT;
        }

        @Override
        public String getDbType() {
            return "Number(12,0)";//actually this string is not used for EValType.INT
        }                

        @Override
        public Id getId() {
            return QuerySqlBuilder.ROWS_COUNT_FIELD_COL_ID;
        }

        @Override
        public Sqml getExpression() {
            final Sqml expression = Sqml.Factory.newInstance();
            expression.setSql("*");
            return expression;
        }

        @Override
        public boolean isExpression() {
            return true;
        }
    }
    
    protected static final class AggregationField extends Field implements IAggregationField{
        
        private final EAggregateFunction function;
        
        AggregationField(final SqlBuilder builder,final Definition prop, final String alias, final EAggregateFunction function){
            super(builder, prop, alias);
            this.function = function;
        }
                
        @Override
        public EAggregateFunction getFunction(){
            return function;
        }

        @Override
        public EValType getValType() {           
            return function==EAggregateFunction.AVG ? EValType.NUM : super.getValType();
        }                
        
        @Override
        public Id getId(){
            return Id.Factory.append(super.getId(), "_"+function.getValue());
        }
    }
    
    private final DdsTableDef table;
    protected final Map<String, Field> fieldsByPropId;
    Map<String, JoinSqlBuilder> joinBuilders;
    private final String alias;
    private AtomicInteger nextFieldIdx = new AtomicInteger(1);

    public DdsTableDef getTable() {
        return table;
    }

//Constructor
    public SqlBuilder(
            final Arte arte,
            final DdsTableDef table,
            final String alias,
            final EQueryContextType queryCntxType,
            final EQueryBuilderAliasPolicy aliasPolicy) {
        this.arte = arte;
        this.table = table;
        this.alias = alias;
        this.queryCntxType = queryCntxType;
        this.aliasPolicy = aliasPolicy;
        fieldsByPropId = new HashMap<String, Field>();
        joinBuilders = null;
    }

//Protected abstract methods
    abstract QuerySqlBuilder getMainBuilder();

    public abstract RadClassDef getEntityClass();

    public abstract void addParameter(final DbQuery.Param param);
    
    public int getNumberOfItemsInParameterValue(final Id parameterId){
        throw new IllegalUsageError("condition parameter #"+String.valueOf(parameterId)+" was not found");
    }
    
    public boolean isParameterDefined(final Id parameterId){
        return false;
    }

    public AtomicInteger getNextFieldIdx() {
        return nextFieldIdx;
    }

//Protected methods
    Id getMainPropId(final Id propId) {
        return propId;
    }
    
    public Field addAggregationFunctionCall(final Id propId, final EAggregateFunction function){
        RadPropDef prop = null;
        DdsColumnDef dbpProp = null;
        try {
            prop = getEntityClass().getPropById(propId);
        } catch (DefinitionNotFoundError ex) {
            dbpProp = getTable().getColumns().getById(propId, ExtendableDefinitions.EScope.ALL);
        }        
        if (prop instanceof RadDetailPropDef) {
            final RadDetailPropDef detailProp = (RadDetailPropDef) prop;
            final JoinSqlBuilder joinBuilder = getJoinBuilder(detailProp.getDetailReference(), true);
            final Field destField = joinBuilder.addAggregationFunctionCall(detailProp.getJoinedPropId(), function);
            final Field field = new AggregationJoinField(this, detailProp, destField, function);
            fieldsByPropId.put(field.getId().toString(), field);
            return field;
        } else if (prop instanceof RadParentPropDef) {
            final RadParentPropDef parentProp = (RadParentPropDef) prop;
            boolean isDbRefsUsed = true;
            for (IRadRefPropertyDef refProp : parentProp.getRefProps()) {
                if (refProp instanceof RadInnateRefPropDef == false //TWRBS-3070
                        && refProp instanceof RadUserRefPropDef == false//RADIX-7772
                        && refProp instanceof RadDetailParentRefPropDef == false
                        ) {
                    isDbRefsUsed = false;
                    break;
                }
            }
            if (isDbRefsUsed) { //TWRBS-1386
                final RadClassDef parentClassDef = getArte().getDefManager().getClassDef(parentProp.getJoinedClassId());
                final RadPropDef destProp = parentClassDef.getPropById(parentProp.getJoinedPropId());
                if (isDbColOrSqlExpressProp(destProp)) {
                    final List<SqlBuilder> joins = new LinkedList<>();
                    SqlBuilder currentSqlBuilder = this, parentJoinBuilder;
                    joins.add(currentSqlBuilder);                    
                    for (IRadRefPropertyDef refProp : parentProp.getRefProps()) {                        
                        parentJoinBuilder = currentSqlBuilder.getParentJoinBuilder(refProp);
                        joins.add(parentJoinBuilder);
                        currentSqlBuilder = parentJoinBuilder;
                    }
                    Field field = currentSqlBuilder.addAggregationFunctionCall(parentProp.getJoinedPropId(), function);
                    for (int i=joins.size()-1; i>=1; i--){
                        field = new AggregationJoinField(this, parentProp, field, function);
                        currentSqlBuilder = joins.get(i-1);
                        currentSqlBuilder.fieldsByPropId.put(field.getId().toString(), field);
                    }                    
                    return field;
                }
            }
        } else if (isDbColOrSqlExpressProp(prop) || dbpProp != null) {
            final String sFieldName = genPropAlias();
            final Field field;
            if (prop instanceof RadInnatePropDef || dbpProp != null) {
                if (dbpProp == null) {
                    dbpProp = getTable().getColumns().getById(propId, ExtendableDefinitions.EScope.ALL);
                }
                field = new AggregationField(this, dbpProp, sFieldName, function);
                if (dbpProp.getExpression() != null) {
                    addPropsFromSqml(dbpProp.getExpression());
                }
            } else if (prop instanceof RadSqmlPropDef) {
                field = new AggregationField(this, prop, sFieldName, function);
                addPropsFromSqml(((RadSqmlPropDef) prop).getExpression());
            } else {
                field = new AggregationField(this, prop, sFieldName, function);
            }
            fieldsByPropId.put(field.getId().toString(), field);
            return field;
        }
        return null;
    }

    public Field addProp(final Id propId, final EPropUsageType propUsageType, final String propAlias) {
        Field field = fieldsByPropId.get(propId.toString());
        RadPropDef prop = null;
        DdsColumnDef dbpProp = null;
        try {
            prop = getEntityClass().getPropById(propId);
        } catch (DefinitionNotFoundError ex) {//Column possibly not published in class. See  RADIX-9231
            dbpProp = getTable().getColumns().getById(propId, ExtendableDefinitions.EScope.ALL);
        }
        if (field == null) {
            if (prop instanceof RadDetailPropDef) {
                final JoinSqlBuilder joinBuilder = getJoinBuilder(((RadDetailPropDef) prop).getDetailReference(), true);
                if (prop instanceof RadDetailParentRefPropDef) {
                    field = new Field(this, prop, null);
                    fieldsByPropId.put(propId.toString(), field);
                    final RadDetailParentRefPropDef refProp = (RadDetailParentRefPropDef) prop;
                    for (DdsReferenceDef.ColumnsInfoItem c : refProp.getReference().getColumnsInfo()) {
                        //RADIX-4628
                        joinBuilder.addProp(c.getChildColumnId(), propUsageType, null);
                    }
                } else {
                    final Field destField = joinBuilder.addProp(((RadDetailPropDef) prop).getJoinedPropId(), propUsageType, propAlias);
                    field = new JoinField(this, (RadDetailPropDef) prop, destField.getAlias(), destField);
                    fieldsByPropId.put(propId.toString(), field);
                }
            } else if (prop instanceof RadParentPropDef) {
                final RadParentPropDef parentProp = (RadParentPropDef) prop;
                boolean isDbRefsUsed = true;
                for (IRadRefPropertyDef refProp : parentProp.getRefProps()) {
                    if (refProp instanceof RadInnateRefPropDef == false //TWRBS-3070
                            && refProp instanceof RadUserRefPropDef == false//RADIX-7772
                            && refProp instanceof RadDetailParentRefPropDef == false
                            ) {
                        isDbRefsUsed = false;
                        break;
                    }
                }
                if (isDbRefsUsed) { //TWRBS-1386
                    final RadClassDef parentClassDef = getArte().getDefManager().getClassDef(parentProp.getJoinedClassId());
                    final RadPropDef destProp = parentClassDef.getPropById(parentProp.getJoinedPropId());
                    if (isDbColOrSqlExpressProp(destProp)) {
                        final Stack<SqlBuilder> joins = new Stack<SqlBuilder>();
                        joins.push(this);
                        for (IRadRefPropertyDef refProp : parentProp.getRefProps()) {
                            joins.add(joins.peek().getParentJoinBuilder(refProp));
                        }
                        field = joins.peek().addProp(parentProp.getJoinedPropId(), propUsageType, propAlias);
                        while (joins.size() > 1) {
                            field = new JoinField(this, parentProp, field.getAlias(), field);
                            joins.pop();
                            joins.peek().fieldsByPropId.put(propId.toString(), field);
                        }
                    }
                }
            } else if (prop instanceof RadInnateRefPropDef) {
                field = new Field(this, prop, null);
                fieldsByPropId.put(propId.toString(), field);
                final RadInnateRefPropDef refProp = ((RadInnateRefPropDef) prop);
                for (DdsReferenceDef.ColumnsInfoItem c : refProp.getReference().getColumnsInfo()) {
                    //RADIX-4628
                    addProp(c.getChildColumnId(), propUsageType, null);
                }
            } else if (isDbColOrSqlExpressProp(prop) || dbpProp != null) {
                final String sFieldName = propAlias == null ? (propUsageType == EPropUsageType.FIELD ? genPropAlias() : null) : propAlias;
                if (prop instanceof RadInnatePropDef || dbpProp != null) {
                    if (dbpProp == null) {
                        dbpProp = getTable().getColumns().getById(propId, ExtendableDefinitions.EScope.ALL);
                    }
                    field = new Field(this, dbpProp, sFieldName);
                    if (dbpProp.getExpression() != null) {
                        addPropsFromSqml(dbpProp.getExpression());
                    }
                } else if (prop instanceof RadSqmlPropDef) {
                    field = new Field(this, prop, sFieldName);
                    addPropsFromSqml(((RadSqmlPropDef) prop).getExpression());
                } else {
                    field = new Field(this, prop, sFieldName);
                }
                fieldsByPropId.put(propId.toString(), field);
            }
        } else if (propUsageType == EPropUsageType.FIELD && field.getAlias() == null
                && !(prop instanceof RadDetailParentRefPropDef || prop instanceof RadInnateRefPropDef)) {
            field.alias = propAlias == null ? genPropAlias() : propAlias;
        }
        return field;
    }

    protected final void addPropsFromSqml(final Sqml sqml) {
        try {
            sqml.setThreadLocalEnvironment(getSqmlEnvironment());
            QuerySqmlTranslator.Factory.newInstance(this, QuerySqmlTranslator.EMode.QUERY_TREE_CONSTRUCTION).translate(sqml, CodePrinter.Factory.newNullPrinter(), getPreprocessorConfig());
        } finally {
            sqml.setThreadLocalEnvironment(null);
        }
    }
    
    protected final boolean appendAggregationFieldsStr() {
        final StringBuilder qry = getMainBuilder().querySql;
        boolean res = false;
        for (Field field : fieldsByPropId.values()) {
            if (field.getAlias() != null                   
                && (field.isExpression() || field.getProp() instanceof DdsColumnDef)) {
                
                if (field instanceof IAggregationField==false){
                    field.alias = null;
                    continue;
                }
                
                if (res) {
                    qry.append(',');
                }                                
                
                final EAggregateFunction function = ((IAggregationField)field).getFunction();                
                qry.append(function.getValue());
                qry.append('(');
                if (field.isExpression()){
                    qry.append(translateSqml(field.getExpression()));
                }else{
                    qry.append(getAlias());
                    qry.append('.');
                    qry.append(((DdsColumnDef) field.getProp()).getDbName());
                }
                qry.append(") ");                    
                qry.append(field.getAlias());
                field.setIndex(getMainBuilder().getNextFieldIdx().getAndIncrement());
                res = true;
            }
        }

        if (joinBuilders != null) {
            for (SqlBuilder joinBuilder : joinBuilders.values()) {
                if (res) {
                    qry.append(',');
                }
                if (joinBuilder.appendAggregationFieldsStr()) {
                    res = true;
                } else {
                    if (res)// deleting unused ','
                    {
                        qry.setLength(qry.length() - 1);
                    }
                }
            }
        }
        return res;
    }
    
    protected final boolean appendFieldsStr() {
        final StringBuilder qry = getMainBuilder().querySql;
        boolean res = false;
        for (Field field : fieldsByPropId.values()) {
            if (field.getAlias() != null
                && (field.isExpression() || field.getProp() instanceof DdsColumnDef)) {
                
                if (res) {
                    qry.append(',');
                }                
                
                if (field.isExpression()) {
                    final Sqml expression = field.getExpression();
                    qry.append('(');
                    qry.append(translateSqml(expression));
                    qry.append(')');
                } else {
                    if (getAlias() != null) {
                        qry.append(getAlias());
                        qry.append('.');
                    }
                    qry.append(((DdsColumnDef) field.getProp()).getDbName());
                }
                qry.append(" ");
                qry.append(field.getAlias());
                field.setIndex(getMainBuilder().getNextFieldIdx().getAndIncrement());
                res = true;
            }
        }

        if (joinBuilders != null) {
            for (SqlBuilder joinBuilder : joinBuilders.values()) {
                if (res) {
                    qry.append(',');
                }
                if (joinBuilder.appendFieldsStr()) {
                    res = true;
                } else {
                    if (res)// deleting unused ','
                    {
                        qry.setLength(qry.length() - 1);
                    }
                }
            }
        }
        return res;
    }

    protected final void appendTablesStr(/*final StringBuffer joinHint*/) {
        final StringBuilder qry = getMainBuilder().querySql;
        qry.append(getTable().getDbName());
        if (getAlias() != null) {
            qry.append(' ');
            qry.append(getAlias());
        }
        if (joinBuilders != null) {
            JoinSqlBuilder joinBuilder;
            final Iterator<JoinSqlBuilder> iter = joinBuilders.values().iterator();
            while (iter.hasNext()) {
                joinBuilder = iter.next();
                qry.append(' ');
                joinBuilder.appendJoinTypeStr();
                qry.append(" JOIN ");
                joinBuilder.appendTablesStr(/*joinHint*/);
                qry.append(" ON ");
                joinBuilder.appendJoinCondStr();
            }
        }
    }

//Private methods
    private final String genPropAlias() {
        return SqlBuilder.FIELD_ALIAS_PREFIX + getMainBuilder().incrementFieldCount();
    }

    public Field addParentRefProp(final List<Id> refIds, final Id propId, final String propAlias, final DdsTableDef curTable) {
        final StringBuilder id_ref = new StringBuilder("");
        for (Id refId : refIds) {
            id_ref.append(refId.toString());
            //id_ref.append('\n');
        }
        final String virtPropId = id_ref.toString() + propId.toString();
        final Field field = fieldsByPropId.get(virtPropId);
        if (field != null) {
            return field;
        }
        if (refIds.isEmpty()) {
            return addProp(propId, EPropUsageType.OTHER, propAlias);
        } else {
            final DdsReferenceDef ref = getArte().getDefManager().getReferenceDef(refIds.get(0));
            final JoinSqlBuilder joinBuilder;
            final Id nextTableId;
            if (ref.getType() == DdsReferenceDef.EType.MASTER_DETAIL
                    && curTable != null
                    && curTable.getId().equals(ref.getParentTableId())) {
                joinBuilder = getJoinBuilder(ref, true);
                nextTableId = ref.getChildTableId();
            } else {
                joinBuilder = getJoinBuilder(ref, false);
                nextTableId = ref.getParentTableId();
            }
            final DdsTableDef nextTable = getArte().getDefManager().getTableDef(nextTableId);
            final Field joinedField = joinBuilder.addParentRefProp(refIds.subList(1, refIds.size()), propId, propAlias, nextTable);
            final JoinField joinField = new JoinField(this, null/*virtPropId*/, propAlias, joinedField);
            fieldsByPropId.put(virtPropId, joinField);
            return joinField;
        }
    }

    protected final SqlBuilder getParentJoinBuilder(final IRadRefPropertyDef refProp) {
        if (refProp instanceof RadInnateRefPropDef) {
            return getJoinBuilder(((RadInnateRefPropDef) refProp).getReference(), false);        
        } else if (refProp instanceof RadDetailParentRefPropDef){
            return getJoinBuilder(((RadDetailParentRefPropDef) refProp).getReference(), false);        
        }else {
            return getJoinBuilder((RadUserRefPropDef) refProp);
        }
    }

    public final JoinSqlBuilder getJoinBuilder(final RadUserRefPropDef refProp) {
        final String joinHashKey = JoinSqlBuilder.getJoinHashKey(refProp.getId(), false);
        JoinSqlBuilder res = null;
        if (joinBuilders != null) {
            res = joinBuilders.get(joinHashKey);
        }
        if (res != null) // already joined
        {
            return res;
            //creating new join
        }
        if (joinBuilders == null) {
            joinBuilders = new HashMap<>();
        }
        res = new UserRefJoinSqlBuilder(getMainBuilder(), this, refProp);
        joinBuilders.put(joinHashKey, res);
        return res;
    }

    public final JoinSqlBuilder getJoinBuilder(final DdsReferenceDef ref, final boolean bChild) {
        final String joinHashKey = JoinSqlBuilder.getJoinHashKey(ref.getId(), bChild);
        JoinSqlBuilder res = null;
        if (joinBuilders != null) {
            res = joinBuilders.get(joinHashKey);
        }
        if (res != null) // already joined
        {
            return res;
            //creating new join
        }
        if (joinBuilders == null) {
            joinBuilders = new HashMap<>();
        }
        if (bChild) {
            res = new NativeJoinSqlBuilder(getMainBuilder(), this, ref, true);
            for (DdsReferenceDef.ColumnsInfoItem refProp : ref.getColumnsInfo()) { // Adding reference columns
                addProp(refProp.getParentColumnId(), EPropUsageType.FIELD, null);
                res.addProp(refProp.getChildColumnId(), EPropUsageType.OTHER, null);
            }
            joinBuilders.put(joinHashKey, res);
        } else {
            SqlBuilder builderForChildColumn = this;
            if (!ref.getChildTableId().equals(getTable().getId())) {//child column not in this table
                for (DdsReferenceDef detailsRef : getEntityClass().getDetailsRefs()) {
                    if (ref.getChildTableId().equals(detailsRef.getChildTableId())) {
                        //case when child column from details table
                        builderForChildColumn = getJoinBuilder(detailsRef, true);
                        break;
                    }
                }
            }
            if (this!=builderForChildColumn && builderForChildColumn.joinBuilders!=null){
                res = builderForChildColumn.joinBuilders.get(joinHashKey);
                if (res!=null){
                    return res;
                }
            }
            res = new NativeJoinSqlBuilder(getMainBuilder(), builderForChildColumn, ref, false);
            for (DdsReferenceDef.ColumnsInfoItem refProp : ref.getColumnsInfo()) { // Adding reference columns
                builderForChildColumn.addProp(refProp.getChildColumnId(), EPropUsageType.FIELD, null);
                res.addProp(refProp.getParentColumnId(), EPropUsageType.OTHER, null);
            }
            if (builderForChildColumn.joinBuilders == null){
                builderForChildColumn.joinBuilders = new HashMap<>();
            }
            builderForChildColumn.joinBuilders.put(joinHashKey, res);            
        }
        return res;
    }

    public Field getDestField(final String propId) {
        Field f = fieldsByPropId.get(propId);
        while (f instanceof JoinField) {
            f = ((JoinField) f).joinField;
        }
        if (f == null) {
            throw new DbQueryBuilderError("Column requested by SQML is absent in the query", null);
        }
        return f;
    }
    
    protected ISqmlPreprocessorConfig getPreprocessorConfig() {
        return new ServerPreprocessorConfig(getArte().getDbConfiguration());
    }
    
    public final CharSequence translateSqml(final Sqml sqml) {
        final CodePrinter codePrinter = CodePrinter.Factory.newSqlPrinter();
        try {
            sqml.setThreadLocalEnvironment(getSqmlEnvironment());
            QuerySqmlTranslator.Factory.newInstance(this, QuerySqmlTranslator.EMode.SQL_CONSTRUCTION).translate(sqml, codePrinter, getPreprocessorConfig());
        } catch (RuntimeException e) {
            if (e instanceof TagTranslateError) {
                throw (TagTranslateError) e;
            }
            throw new RadixError("Can't translate SQML: " + e.getClass().getName() + (e.getMessage() != null ? "\n" + e.getMessage() : ""), e);
        } finally {
            sqml.setThreadLocalEnvironment(null);
        }
        return codePrinter.toString() + '\n';//to close single line comment if there is any
    }

    protected final PropSqlNameTag createThisPropTag(final Id propId) {
        final PropSqlNameTag tag = PropSqlNameTag.Factory.newInstance();
        tag.setOwnerType(PropSqlNameTag.EOwnerType.THIS);
        tag.setPropOwnerId(getMainBuilder().getTable().getId());
        tag.setPropId(propId);
        return tag;
    }

    protected static final PropSqlNameTag createParentPropTag(final Id tabId, final Id propId) {
        final PropSqlNameTag tag = PropSqlNameTag.Factory.newInstance();
        tag.setOwnerType(PropSqlNameTag.EOwnerType.PARENT);
        tag.setPropOwnerId(tabId);
        tag.setPropId(propId);
        return tag;
    }

    protected static final PropSqlNameTag createChildPropTag(final Id tabId, final Id propId) {
        final PropSqlNameTag tag = PropSqlNameTag.Factory.newInstance();
        tag.setOwnerType(PropSqlNameTag.EOwnerType.CHILD);
        tag.setPropOwnerId(tabId);
        tag.setPropId(propId);
        return tag;
    }

    protected final void appendThisPropTagTo(final Sqml to, final Id propId) {
        to.getItems().add(createThisPropTag(propId));
    }

    protected final CharSequence getNativePropDbPresentation(final Id propId) {
        final StringBuilder qry = new StringBuilder();
        final DdsColumnDef prop = getTable().getColumns().getById(propId, ExtendableDefinitions.EScope.ALL);
        if (prop.getExpression() == null) {
            if (getAlias() != null) {
                qry.append(getAlias());
                qry.append('.');
            }
            qry.append(prop.getDbName());
        } else {
            qry.append('(');
            qry.append(translateSqml(prop.getExpression()));
            qry.append(')');
        }
        return qry;
    }

    final static String translateSqmlExpression(final Arte arte, final DdsTableDef table, final String tableAlias, final Sqml sqml) {
        final ObjectQuerySqlBuilder constr = new ObjectQuerySqlBuilder(arte, table, tableAlias);
        final CharSequence res = constr.translateSqml(sqml);
        return res == null ? null : res.toString();
    }

    public String getThisPidScript() {
        return getPidScript(getTable(), getAlias());
    }

    public static String getPidScript(final DdsTableDef table, final String tabAlias) {
        return table.getPidScript(tabAlias);
    }
    private ISqmlEnvironment sqmlEnvironment = null;

    public ISqmlEnvironment getSqmlEnvironment() {
        if (sqmlEnvironment == null) {
            sqmlEnvironment = createSqmlEnvironment();
        }
        return sqmlEnvironment;
    }
    
    protected ISqmlEnvironment createSqmlEnvironment() {
        return new SqmlEnvironment(this);
    }

    private boolean isDbColOrSqlExpressProp(final RadPropDef prop) {
        return prop instanceof RadInnatePropDef || /*RADIX-2877: prop instanceof RadUserPropDef || */ prop instanceof RadSqmlPropDef;
    }
}
