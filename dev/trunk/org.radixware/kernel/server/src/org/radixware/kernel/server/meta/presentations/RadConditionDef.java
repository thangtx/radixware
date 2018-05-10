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

package org.radixware.kernel.server.meta.presentations;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EAccessAreaType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.sqml.tags.ParentConditionTag;
import org.radixware.kernel.common.sqml.tags.PropSqlNameTag;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.sqml.Sqml;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.ValueToSqlConverter;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.meta.clazzes.IRadRefPropertyDef;
import org.radixware.kernel.server.meta.clazzes.RadClassAccessArea;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.meta.clazzes.RadPropDef;
import org.radixware.kernel.server.types.Pid;
import org.radixware.kernel.server.utils.SrvValAsStr;
import org.radixware.schemas.xscml.SqmlDocument;

public final class RadConditionDef {

    private final Sqml condition;
    private final Sqml conditionFrom;
    private final Prop2ValueCondition prop2ValueCondition;

    public RadConditionDef(
            final String condition,
            final String conditionFrom) {
        this(condition, conditionFrom, null);
    }

    public RadConditionDef(
            final String condition,
            final String conditionFrom,
            final String layerUri) {
        this(condition, conditionFrom, null, layerUri);
    }

    public RadConditionDef(
            final String condition,
            final String conditionFrom,
            final Prop2ValueCondition declarativeCondition,
            final String layerUri) {
        SqmlDocument expr;
        try {
            expr = condition == null || condition.length() == 0 ? null : SqmlDocument.Factory.parse(condition);
        } catch (XmlException e) {
            throw new WrongFormatError("Can't parse condition SQML: " + ExceptionTextFormatter.getExceptionMess(e), e);
        }
        this.condition = expr != null ? Sqml.Factory.loadFrom("CondWhere", expr.getSqml()) : null;
        if (this.condition != null) {
            this.condition.setLayerUri(layerUri);
            this.condition.switchOnWriteProtection();
        }
        try {
            expr = conditionFrom == null || conditionFrom.length() == 0 ? null : SqmlDocument.Factory.parse(conditionFrom);
        } catch (XmlException e) {
            throw new WrongFormatError("Can't parse condition \"from\" SQML: " + ExceptionTextFormatter.getExceptionMess(e), e);
        }
        this.conditionFrom = expr != null ? Sqml.Factory.loadFrom("CondFrom", expr.getSqml()) : null;
        if (this.conditionFrom != null) {
            this.conditionFrom.setLayerUri(layerUri);
            this.conditionFrom.switchOnWriteProtection();
        }
        if (declarativeCondition != null) {
            this.prop2ValueCondition = declarativeCondition;
        } else {
            this.prop2ValueCondition = null;
        }
    }

    public RadConditionDef(
            final Sqml condition,
            final Sqml conditionFrom) {
        this.condition = condition;
        this.conditionFrom = conditionFrom;
        this.prop2ValueCondition = null;
    }

    /**
     * @return the condition
     */
    public Sqml getCondition() {
        return condition;
    }

    /**
     * @return the conditionFrom
     */
    public Sqml getConditionFrom() {
        return conditionFrom;
    }

    public Prop2ValueCondition getProp2ValueCondition() {
        return prop2ValueCondition == null ? Prop2ValueCondition.EMPTY_CONDITION : prop2ValueCondition;
    }

    public static class Prop2ValueCondition {

        public final static Prop2ValueCondition EMPTY_CONDITION = new Prop2ValueCondition(new Id[]{}, new String[]{});
        private final Map<Id, String> propVals;
        private final String cacheKey;
        //@GuardedBy cachedDataSem
        protected List<String> cachedAccessAreaList;
        //@GuardedBy cachedDataSem
        private String cachedAccessAreaListAsSql;
        protected final Object cachedDataSem = new Object();

        public Prop2ValueCondition(Id[] propIds, String[] propVals) {
            if (propIds.length==0){
                this.propVals = Collections.<Id,String>emptyMap();
                cacheKey = "";                
            }else{
                this.propVals = new HashMap<>(4);
                final StringBuilder cacheKeyBuilder = new StringBuilder();
                Id propId;
                String propVal;
                for (int i = 0; i < propIds.length; i++) {
                    if (i>0){
                        cacheKeyBuilder.append('\n');
                    }
                    propId = propIds[i];
                    propVal = propVals[i];
                    cacheKeyBuilder.append(propId.toString());                
                    if (propVal!=null){
                        cacheKeyBuilder.append('=');
                        if (!propVal.isEmpty()){
                            cacheKeyBuilder.append(propVal.replaceAll("=", "\\=").replaceAll("\\n", "\\\\n"));
                        }
                    }
                    this.propVals.put(propId, propVal);
                }
                cacheKey = cacheKeyBuilder.toString();
            }
        }

        public String getPropValAsStr(Id propId) {
            return propVals.get(propId);
        }

        public Object getPropVal(final Arte arte, final RadPropDef property) {
            final String valAsStr = getPropValAsStr(property.getId());
            if (valAsStr == null) {
                return null;
            }
            if (property instanceof IRadRefPropertyDef) {
                final Id entityId = ((IRadRefPropertyDef) property).getDestinationEntityId();
                final Pid pid = new Pid(arte, entityId, valAsStr);
                return arte.getEntityObject(pid);
            }
            final EValType valType;
            if (property.getValType() == EValType.BLOB) {
                valType = EValType.BIN;
            } else if (property.getValType() == EValType.CLOB) {
                valType = EValType.STR;
            } else {
                valType = property.getValType();
            }
            return ValAsStr.fromStr(valAsStr, valType);
        }

        public Set<Id> getPropIds() {
            return Collections.unmodifiableSet(propVals.keySet());
        }

        public boolean containsProperty(Id propId) {
            return propVals.containsKey(propId);
        }

        private static void addPropToSqml(RadClassDef context, RadPropDef prop, String propVal, Sqml sqml) {
            if (sqml.getItems().isEmpty()) {
                sqml.getItems().add(Scml.Text.Factory.newInstance("("));
            } else {
                sqml.getItems().add(Scml.Text.Factory.newInstance(" and ("));
            }
            if (prop.getValType() == EValType.PARENT_REF) {
                if (prop instanceof IRadRefPropertyDef) {
                    ParentConditionTag pcTag = ParentConditionTag.Factory.newInstance();
                    if (propVal == null) {
                        pcTag.setOperator(ParentConditionTag.Operator.IS_NULL);
                    } else {
                        pcTag.setParentPid(propVal);
                        pcTag.setOperator(ParentConditionTag.Operator.EQUAL);
                    }
                    pcTag.setPropId(prop.getId());
                    pcTag.setPropOwnerId(context.getId());
                    sqml.getItems().add(pcTag);
                } else {
                    throw new WrongFormatError("Inadmissible nature of reference property in condition " + context.getName() + "." + prop.getName() + " (" + context.getId() + "." + prop.getId() + ")");
                }
            } else {
                addSimplePropToSqml(context, prop, propVal, sqml);
            }
            sqml.getItems().add(Scml.Text.Factory.newInstance(")"));
        }

        private static void addSimplePropToSqml(RadClassDef context, RadPropDef prop, String propVal, Sqml sqml) {
            PropSqlNameTag tag = PropSqlNameTag.Factory.newInstance();
            tag.setOwnerType(PropSqlNameTag.EOwnerType.THIS);
            tag.setPropId(prop.getId());
            tag.setPropOwnerId(context.getId());
            sqml.getItems().add(tag);
            String sqlValue = "NULL";
            if (propVal != null) {
                EValType propType = prop.getValType();
                sqlValue = ValueToSqlConverter.toSql(SrvValAsStr.fromStr(propVal, propType), propType);
            }
            if ("NULL".equals(sqlValue)) {
                sqml.getItems().add(Scml.Text.Factory.newInstance(" is NULL"));
            } else {
                sqml.getItems().add(Scml.Text.Factory.newInstance(" = " + sqlValue));
            }
        }

        public Sqml toSqml(RadClassDef context) {
            if (propVals.isEmpty()) {
                return null;
            }
            final Sqml sqml = Sqml.Factory.newInstance();
            addPropValsToSqml(context, sqml);
            return sqml;
        }

        protected final void addPropValsToSqml(final RadClassDef context, final Sqml sqml) {
            for (Id id : propVals.keySet()) {
                final String propVal = propVals.get(id);
                final RadPropDef prop = context.getPropById(id);
                addPropToSqml(context, prop, propVal, sqml);
            }
        }

        public final List<Id> getCurUserRoleIds(final Arte arte, final RadClassDef classDef) {
            final String accessAreaListAsSql = getAccessAreaListAsSql(arte, classDef);
            if (accessAreaListAsSql.isEmpty()) {
                return arte.getRights().getCurUserAllRolesInAllAreas();
            } else {
                return arte.getRights().getCurUserAllRolesInAreas(accessAreaListAsSql);
            }
        }

        public final boolean hasAccessAreas(final Arte arte, final RadClassDef classDef) {
            return !getAccessAreaListAsSql(arte, classDef).isEmpty();
        }

        private String getAccessAreaListAsSql(final Arte arte, final RadClassDef classDef) {
            synchronized (cachedDataSem) {
                if (cachedAccessAreaListAsSql == null) {
                    final List<String> accessAreaList = fillAccessAreaList(arte, classDef);
                    if (accessAreaList.isEmpty()) {
                        cachedAccessAreaListAsSql = "";
                    } else {
                        final StringBuilder accessAreaListBuilder = new StringBuilder();
                        for (String accessAreaAsSql : accessAreaList) {
                            if (accessAreaListBuilder.length() == 0) {
                                accessAreaListBuilder.append("TRdxAcsAreaList(");
                            } else {
                                accessAreaListBuilder.append(',');
                            }
                            accessAreaListBuilder.append(accessAreaAsSql);
                        }
                        accessAreaListBuilder.append(')');
                        cachedAccessAreaListAsSql = accessAreaListBuilder.toString();
                    }
                }
                return cachedAccessAreaListAsSql;
            }
        }

        protected List<String> fillAccessAreaList(final Arte arte, final RadClassDef classDef) {
            if (!propVals.isEmpty() && (classDef.getAccessAreaType() == EAccessAreaType.OWN || classDef.getAccessAreaType() == EAccessAreaType.INHERITED)) {
                synchronized (cachedDataSem) {
                    if (cachedAccessAreaList == null) {
                        final List<String> accessAreaList = new LinkedList<>();
                        final Map<RadPropDef, String> valAsStrByPropDef = new HashMap<>();
                        for (Map.Entry<Id, String> entry : propVals.entrySet()) {
                            valAsStrByPropDef.put(classDef.getPropById(entry.getKey()), entry.getValue());
                        }
                        if (classDef.hasOwnAccessAreas()) {
                            fillAcsAreaList(arte, valAsStrByPropDef, classDef.getAccessAreas(), accessAreaList);
                        }
                        if (classDef.getAccessAreaType() == EAccessAreaType.INHERITED) {
                            //There are no parent properties in declarative condition so only first inheritance level need to be processed
                            final DdsReferenceDef reference = classDef.getAccessAreaInheritRef();
                            for (Map.Entry<RadPropDef, String> entry : valAsStrByPropDef.entrySet()) {
                                if (entry.getKey() instanceof IRadRefPropertyDef) {
                                    final IRadRefPropertyDef propRefDef = (IRadRefPropertyDef) entry.getKey();
                                    if (reference.getId().equals(propRefDef.getReference().getId())
                                            && reference.getColumnsInfo().size() == 1) {
                                        final Id parentEntityId =
                                                RadClassDef.getEntityClassIdByTableId(reference.getParentTableId());
                                        final RadClassDef parentClassDef = classDef.getRelease().getClassDef(parentEntityId);
                                        if (parentClassDef.hasOwnAccessAreas()) {
                                            final Pid pid = entry.getValue() == null ? null : new Pid(arte, parentEntityId, entry.getValue());
                                            final Object pkVal = pid == null ? null : pid.getPkVals().get(0);
                                            final Id parentPropId = reference.getColumnsInfo().get(0).getParentColumnId();
                                            final RadPropDef parentPropDef = parentClassDef.getPropById(parentPropId);
                                            final EValType pkValType = parentPropDef.getValType();
                                            final Map<RadPropDef, String> parentValAsStrByPropDef =
                                                    Collections.singletonMap(parentPropDef, ValAsStr.toStr(pkVal, pkValType));
                                            fillAcsAreaList(arte, parentValAsStrByPropDef, parentClassDef.getAccessAreas(), accessAreaList);
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                        if (accessAreaList.isEmpty()) {
                            cachedAccessAreaList = Collections.emptyList();
                        } else {
                            cachedAccessAreaList = Collections.unmodifiableList(accessAreaList);
                        }
                    }
                    return cachedAccessAreaList;
                }
            }
            return Collections.emptyList();
        }
        
        public String getCacheKey(){
            return cacheKey;
        }

        private static void fillAcsAreaList(final Arte arte,
                final Map<RadPropDef, String> valAsStrByPropDef,
                final List<RadClassAccessArea> sourceAreaList,
                final List<String> destAreaList) {
            for (RadClassAccessArea area : sourceAreaList) {
                final String areaAsSqtStr = getAcsAreaAsSqlStr(arte, area, valAsStrByPropDef);
                if (areaAsSqtStr != null) {
                    destAreaList.add(areaAsSqtStr);
                }
            }
        }

        private static String getAcsAreaAsSqlStr(final Arte arte,
                final RadClassAccessArea area,
                final Map<RadPropDef, String> valAsStrByPropDef) {
            final StringBuilder builder = new StringBuilder();
            for (RadClassAccessArea.Partition partition : area.getPartitions()) {
                final String coordinate = getAcsCoordinateAsSqlStr(arte, partition, valAsStrByPropDef);
                if (coordinate != null) {
                    if (builder.length() == 0) {
                        builder.append("TRdxAcsArea(TRdxAcsCoordinates(");
                    } else {
                        builder.append(',');
                    }
                    builder.append(coordinate);
                }
            }
            if (builder.length() == 0) {
                return null;
            }
            builder.append("))");
            return builder.toString();
        }

        private static String getAcsCoordinateAsSqlStr(final Arte arte,
                final RadClassAccessArea.Partition partition,
                final Map<RadPropDef, String> valAsStrByPropDef) {
            final String coordinateTemplate = "TRdxAcsCoordinate(0,\'%1$s\',%2$s)";
            for (Map.Entry<RadPropDef, String> entry : valAsStrByPropDef.entrySet()) {
                if (entry.getKey() instanceof IRadRefPropertyDef) {
                    final IRadRefPropertyDef propDef = (IRadRefPropertyDef) entry.getKey();
                    final DdsReferenceDef reference = propDef.getReference();
                    if (reference.getColumnsInfo().size() == 1
                            && partition.getReferenceIds().size() == 1
                            && reference.getId().equals(partition.getReferenceIds().get(0))
                            && reference.getColumnsInfo().get(0).getParentColumnId().equals(partition.getPropId())) {
                        final Id entityId = ((IRadRefPropertyDef) propDef).getDestinationEntityId();
                        final Pid pid = entry.getValue() == null ? null : new Pid(arte, entityId, entry.getValue());
                        final Object pkVal = pid == null ? null : pid.getPkVals().get(0);
                        final EValType pkValType = reference.getColumnsInfo().get(0).getParentColumn().getValType();
                        final String sqlVal = ValueToSqlConverter.toSql(pkVal, pkValType);
                        return String.format(coordinateTemplate, partition.getFamilyId(), sqlVal);
                    }
                } else if (entry.getKey().getId().equals(partition.getPropId())) {
                    final String sqlVal = ValueToSqlConverter.toSql(entry.getValue(), entry.getKey().getValType());
                    return String.format(coordinateTemplate, partition.getFamilyId(), sqlVal);
                }
            }
            return null;
        }

        public static Prop2ValueCondition fromExplorerItem(final RadSelectorExplorerItemDef explorerItem) {
            final RadSelectorPresentationDef presentation = explorerItem.getSelectorPresentation();
            final RadConditionDef.Prop2ValueCondition explorerItemCondition =
                    explorerItem.getCondition() == null ? null : explorerItem.getCondition().getProp2ValueCondition();
            final RadConditionDef.Prop2ValueCondition presentationCondition =
                    presentation.getCondition() == null ? null : presentation.getCondition().getProp2ValueCondition();
            if (explorerItemCondition == null && presentationCondition == null) {
                return Prop2ValueCondition.EMPTY_CONDITION;
            } else {
                return mergeConditions(presentationCondition, explorerItemCondition);
            }
        }

        public static Prop2ValueCondition fromParentRefProperty(final IRadRefPropertyDef property,
                final RadClassDef propertyOwner,
                final RadParentTitlePropertyPresentationDef propertyPresentation,
                final RadSelectorPresentationDef selectorPresentation) {
            final RadClassPresentationDef ptPropOwnerClassPres = propertyOwner.getPresentation();
            final RadConditionDef propertyCondition = propertyPresentation.getParentSelectCondition(ptPropOwnerClassPres);
            final RadConditionDef.Prop2ValueCondition propertyContextProps = propertyCondition == null ? null : propertyCondition.getProp2ValueCondition();
            final RadConditionDef presentationCondition = selectorPresentation.getCondition();
            final RadConditionDef.Prop2ValueCondition presentationContextProps = presentationCondition == null ? null : presentationCondition.getProp2ValueCondition();
            return mergeConditions(presentationContextProps, propertyContextProps);
        }

        public static Prop2ValueCondition mergeConditions(final Prop2ValueCondition condition1, final Prop2ValueCondition condition2) {
            if (condition1 == null || condition1.propVals.isEmpty()) {
                return condition2 == null ? Prop2ValueCondition.EMPTY_CONDITION : condition2;
            }
            if (condition2 == null || condition2.propVals.isEmpty()) {
                return condition1 == null ? Prop2ValueCondition.EMPTY_CONDITION : condition1;
            }
            return new MergedProp2ValueCondition(condition1, condition2);
        }
    }

    private static final class MergedProp2ValueCondition extends Prop2ValueCondition {

        private final Prop2ValueCondition condition1;
        private final Prop2ValueCondition condition2;
        private final String cacheKey;

        public MergedProp2ValueCondition(final Prop2ValueCondition condition1, final Prop2ValueCondition condition2) {
            super(new Id[]{}, new String[]{});
            this.condition1 = condition1;
            this.condition2 = condition2;
            if (condition1==null || condition1.getCacheKey().isEmpty()){
                cacheKey = condition2==null ? "" : condition2.getCacheKey();
            }else if (condition2==null || condition2.getCacheKey().isEmpty()){
                cacheKey = condition1.getCacheKey();
            }else{
                cacheKey = condition1.getCacheKey()+"\n"+condition2.getCacheKey();
            }
        }

        @Override
        public String getPropValAsStr(final Id propId) {
            if (condition2.containsProperty(propId)) {
                return condition2.getPropValAsStr(propId);
            }
            return condition1.getPropValAsStr(propId);
        }

        @Override
        public Object getPropVal(final Arte arte, final RadPropDef property) {
            if (condition2.containsProperty(property.getId())) {
                return condition2.getPropVal(arte, property);
            }
            return condition1.getPropVal(arte, property);
        }

        @Override
        public Set<Id> getPropIds() {
            final Set<Id> result = new HashSet<>(condition1.getPropIds());
            result.addAll(condition2.getPropIds());
            return Collections.unmodifiableSet(result);
        }

        @Override
        public boolean containsProperty(final Id propId) {
            return condition1.containsProperty(propId) || condition2.containsProperty(propId);
        }

        @Override
        public Sqml toSqml(final RadClassDef context) {
            if (condition1.propVals.isEmpty()) {
                return condition2.toSqml(context);
            }
            if (condition2.propVals.isEmpty()) {
                return condition1.toSqml(context);
            }
            final Sqml sqml = Sqml.Factory.newInstance();
            condition1.addPropValsToSqml(context, sqml);
            condition2.addPropValsToSqml(context, sqml);
            return sqml;
        }

        @Override
        protected List<String> fillAccessAreaList(final Arte arte, final RadClassDef classDef) {
            if (classDef.getAccessAreaType() == EAccessAreaType.OWN || classDef.getAccessAreaType() == EAccessAreaType.INHERITED) {
                synchronized (cachedDataSem) {
                    if (cachedAccessAreaList == null) {
                        final List<String> accessAreaList = new LinkedList<>();
                        accessAreaList.addAll(condition1.fillAccessAreaList(arte, classDef));
                        accessAreaList.addAll(condition2.fillAccessAreaList(arte, classDef));
                        if (accessAreaList.isEmpty()) {
                            cachedAccessAreaList = Collections.emptyList();
                        } else {
                            cachedAccessAreaList = Collections.unmodifiableList(accessAreaList);
                        }
                    }
                    return cachedAccessAreaList;
                }
            }
            return Collections.emptyList();
        }

        @Override
        public String getCacheKey() {
            return cacheKey;
        }                
    }
}