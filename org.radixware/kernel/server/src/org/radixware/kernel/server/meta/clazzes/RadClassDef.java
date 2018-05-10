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
package org.radixware.kernel.server.meta.clazzes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.enums.EAccessAreaType;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.server.arte.Release;
import org.radixware.kernel.server.dbq.QuerySqlBuilder;
import org.radixware.kernel.server.exceptions.DbQueryBuilderError;
import org.radixware.kernel.common.meta.RadDefinition;
import org.radixware.kernel.common.types.MultilingualString;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.dbq.DbQueryBuilder;
import org.radixware.kernel.server.meta.presentations.RadClassPresentationDef;

public class RadClassDef extends RadDefinition {

    private final EClassType type;
    private final Id entityId;
    private final Id titleId;
    private final Id ancestorId;
    private final RadClassPresentationDef presentation;
    private Collection<Id> detailsRefIds;
    private Map<Id, RadPropDef> propsById;
    private Map<Id, RadMethodDef> methodsById;
    private final EAccessAreaType accessAreaType;
    private final Id accessAreaInheritRefId;
    private final List<RadClassAccessArea> accessAreas;
    private Release release = null;
    private boolean isDeprecated;
    private RadClassDef aecDef = null;
    private Collection<RadPropDef> allProps = null;
    private Collection<DdsReferenceDef> allDetailRefs = null;
    private RadClassApJoins apJoins = null;
    private DdsTableDef tableDef = null;
    private DdsReferenceDef accessAreaInheritRef = null;
    private Map<Id, UpstandingRefInfo> upstandingRefInfoById = new HashMap<>();

    public RadClassDef(
            final Arte arte,
            final Id id,
            final String name,
            final Id titleId,
            final EClassType type,
            final RadClassPresentationDef pres,
            final Id entityId,
            final Id ancestorId,
            final RadPropDef[] props,
            final Id[] detailsRefIds,
            final EAccessAreaType accessAreaType,
            final Id accessAreaInheritRefId,
            final RadClassAccessArea[] accessAreas) {
        this(arte.getDefManager().getReleaseCache().getRelease(), id, name, titleId, type, pres, entityId, ancestorId, props, null, detailsRefIds, accessAreaType, accessAreaInheritRefId, accessAreas);
    }

    public RadClassDef(
            final Release release,
            final Id id,
            final String name,
            final Id titleId,
            final EClassType type,
            final RadClassPresentationDef pres,
            final Id entityId,
            final Id ancestorId,
            final RadPropDef[] props,
            final Id[] detailsRefIds,
            final EAccessAreaType accessAreaType,
            final Id accessAreaInheritRefId,
            final RadClassAccessArea[] accessAreas) {
        this(release, id, name, titleId, type, pres, entityId, ancestorId, props, null, detailsRefIds, accessAreaType, accessAreaInheritRefId, accessAreas);
    }

    public RadClassDef(
            final Arte arte,
            final Id id,
            final String name,
            final Id titleId,
            final EClassType type,
            final RadClassPresentationDef pres,
            final Id entityId,
            final Id ancestorId,
            final RadPropDef[] props,
            final RadMethodDef[] methods,
            final Id[] detailsRefIds,
            final EAccessAreaType accessAreaType,
            final Id accessAreaInheritRefId,
            final RadClassAccessArea[] accessAreas) {
        this(arte.getDefManager().getReleaseCache().getRelease(), id, name, titleId, type, pres, entityId, ancestorId, props, methods, detailsRefIds, accessAreaType, accessAreaInheritRefId, accessAreas);
    }

    public RadClassDef(
            final Release release,
            final Id id,
            final String name,
            final Id titleId,
            final EClassType type,
            final RadClassPresentationDef pres,
            final Id entityId,
            final Id ancestorId,
            final RadPropDef[] props,
            final RadMethodDef[] methods,
            final Id[] detailsRefIds,
            final EAccessAreaType accessAreaType,
            final Id accessAreaInheritRefId,
            final RadClassAccessArea[] accessAreas) {
        this(release, id, name, titleId, type, pres, entityId, ancestorId, props, methods, detailsRefIds, accessAreaType, accessAreaInheritRefId, accessAreas, false);
    }

    public RadClassDef(
            final Arte arte,
            final Id id,
            final String name,
            final Id titleId,
            final EClassType type,
            final RadClassPresentationDef pres,
            final Id entityId,
            final Id ancestorId,
            final RadPropDef[] props,
            final RadMethodDef[] methods,
            final Id[] detailsRefIds,
            final EAccessAreaType accessAreaType,
            final Id accessAreaInheritRefId,
            final RadClassAccessArea[] accessAreas,
            final boolean isDeprecated) {
        this(arte.getDefManager().getReleaseCache().getRelease(), id, name, titleId, type, pres, entityId, ancestorId, props, methods, detailsRefIds, accessAreaType, accessAreaInheritRefId, accessAreas, isDeprecated);
    }

    public RadClassDef(
            final Release release,
            final Id id,
            final String name,
            final Id titleId,
            final EClassType type,
            final RadClassPresentationDef pres,
            final Id entityId,
            final Id ancestorId,
            final RadPropDef[] props,
            final RadMethodDef[] methods,
            final Id[] detailsRefIds,
            final EAccessAreaType accessAreaType,
            final Id accessAreaInheritRefId,
            final RadClassAccessArea[] accessAreas,
            final boolean depracated) {
        super(id, name);
        this.type = type;
        this.entityId = entityId;
        this.titleId = titleId;
        this.presentation = pres;
        this.ancestorId = ancestorId;
        if (props == null) {
            this.propsById = Collections.emptyMap();
        } else {
            this.propsById = new HashMap<>(props.length * 2, 0.3f);
            for (RadPropDef prop : props) {
                propsById.put(prop.getId(), prop);
            }
            this.propsById = Collections.unmodifiableMap(this.propsById);
        }
        if (methods == null) {
            this.methodsById = Collections.emptyMap();
        } else {
            this.methodsById = new HashMap<>(methods.length * 2 + 1);
            for (RadMethodDef method : methods) {
                methodsById.put(method.getId(), method);
            }
            this.propsById = Collections.unmodifiableMap(this.propsById);
        }
        if (detailsRefIds != null) {
            this.detailsRefIds = Collections.unmodifiableCollection(Arrays.asList(detailsRefIds));
        } else {
            this.detailsRefIds = Collections.emptyList();
        }

        this.accessAreaType = accessAreaType == null ? EAccessAreaType.NONE : accessAreaType;
        this.accessAreaInheritRefId = accessAreaInheritRefId;
        if (accessAreas != null && accessAreas.length != 0) {
            this.accessAreas = Collections.unmodifiableList(Arrays.asList(accessAreas));
        } else {
            this.accessAreas = Collections.emptyList();
        }
        this.isDeprecated = depracated;
        linkAfterConstruct(release);
    }

    private void linkAfterConstruct(final Release release) {
        this.release = release;
        if (presentation != null) {
            presentation.link(this);
        }
        for (RadPropDef prop : propsById.values()) {
            prop.link(this);
        }

    }

    private void linkUpstandingRefInfo() {
        for (RadPropDef p : getProps()) {
            if (p.getValType() == EValType.PARENT_REF && (p instanceof RadDetailParentRefPropDef || p instanceof RadInnateRefPropDef )) {
                if (p instanceof RadDetailParentRefPropDef) {
                    final RadDetailParentRefPropDef refProp = (RadDetailParentRefPropDef) p;
                    for (DdsReferenceDef.ColumnsInfoItem refPart : refProp.getReference().getColumnsInfo()) {
                        for (RadPropDef detPropCandidate : getProps()) {
                            if (detPropCandidate instanceof RadDetailPropDef) {
                                RadDetailPropDef detProp = (RadDetailPropDef) detPropCandidate;
                                if (refPart.getChildColumnId().equals(detProp.getJoinedPropId())) {
                                    upstandingRefInfoById.put(detProp.getId(), new UpstandingRefInfo(refProp.getReference(), p.getId(), refPart.getParentColumnId()));
                                }
                            }
                        }
                    }
                } else {
                    final RadInnateRefPropDef refProp = (RadInnateRefPropDef) p;
                    for (DdsReferenceDef.ColumnsInfoItem refPart : refProp.getReference().getColumnsInfo()) {
                        for (RadPropDef innatePropCandidate : getProps()) {
                            if (innatePropCandidate instanceof RadInnatePropDef) {
                                RadInnatePropDef innnateProp = (RadInnatePropDef) innatePropCandidate;
                                if (refPart.getChildColumnId().equals(innnateProp.getId())) {
                                    upstandingRefInfoById.put(innnateProp.getId(), new UpstandingRefInfo(refProp.getReference(), p.getId(), refPart.getParentColumnId()));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public UpstandingRefInfo getUpstandingRefInfo(final Id propId) {
        return upstandingRefInfoById.get(propId);
    }

    @Override
    public void link() {
        super.link();
        getProps();
        if (type == EClassType.ENTITY || type == EClassType.APPLICATION) {
            getDetailsRefs();
            getEntityClass();
            getTableDef();
            if (getAccessAreaType() == EAccessAreaType.INHERITED) {
                getAccessAreaInheritRef();
            }
            if (hasPartitionRights()) {
                getApJoins();
            }
            linkUpstandingRefInfo();
        }
        if (presentation != null) {
            presentation.link();
        }
    }

    /**
     * @return the type
     */
    public EClassType getType() {
        return type;
    }

    /**
     * @return the entityId
     */
    public Id getEntityId() {
        return entityId;
    }

    /**
     * @return the ancestorId
     */
    public Id getAncestorId() {
        return ancestorId;
    }

    /**
     * @return the presentation
     */
    public RadClassPresentationDef getPresentation() {
        return presentation;
    }

    /**
     * @return the accessAreaType
     */
    public EAccessAreaType getAccessAreaType() {
        return getEntityClass().accessAreaType;
    }

    /**
     * @return the accessAreaInheritRefId
     */
    protected Id getAccessAreaInheritRefId() {
        return getEntityClass().accessAreaInheritRefId;
    }

    /**
     * @return the accessAreas
     */
    public List<RadClassAccessArea> getAccessAreas() {
        return getEntityClass().accessAreas;
    }

    public boolean hasOwnAccessAreas() {
        return !getAccessAreas().isEmpty();
    }

    @Override
    public boolean isDeprecated() {
        return isDeprecated;
    }

     public String getTitle() {
        if (titleId != null) {
            return MultilingualString.get(Arte.get(), getId(), titleId);
        } else if (ancestorId != null && (getType() == EClassType.APPLICATION || getType() == EClassType.ENTITY)) {
            return Arte.get().getDefManager().getClassDef(ancestorId).getTitle();
        }
        return null;
    }

    public final Collection<RadPropDef> getProps() {
        if (allProps == null) {
            if (getAncestorId() == null) // has only own props
            {
                allProps = propsById.values();
            } else { // has own and inherited props
                final Collection<RadPropDef> inheritedProps = release.getClassDef(getAncestorId()).getProps();
                if (propsById.isEmpty()) {//only inheritance props
                    allProps = inheritedProps;
                } else if (inheritedProps.isEmpty()) {//only own props
                    allProps = propsById.values();
                } else {
                    allProps = new ArrayList<RadPropDef>(inheritedProps.size() + propsById.size());
                    for (RadPropDef inheritedProp : inheritedProps) {
                        if (!propsById.containsKey(inheritedProp.getId())) {
                            allProps.add(inheritedProp);
                        }
                    }
                    allProps.addAll(propsById.values());
                    allProps = Collections.unmodifiableCollection(allProps);
                }
            }
        }
        return allProps;
    }

    public final Collection<DdsReferenceDef> getDetailsRefs() {
        if (allDetailRefs == null) {
            final Collection<DdsReferenceDef> inheritedDetailRefs;
            if (getAncestorId() != null) {
                inheritedDetailRefs = release.getClassDef(getAncestorId()).getDetailsRefs();
            } else {
                inheritedDetailRefs = Collections.emptyList();
            }
            if (detailsRefIds.isEmpty()) {//only inheritance props
                allDetailRefs = inheritedDetailRefs;
            } else {
                allDetailRefs = new ArrayList<DdsReferenceDef>(inheritedDetailRefs.size() + detailsRefIds.size());
                allDetailRefs.addAll(inheritedDetailRefs);
                for (Id refId : detailsRefIds) {
                    allDetailRefs.add(release.getReferenceDef(refId));
                }
                allDetailRefs = Collections.unmodifiableCollection(allDetailRefs);
            }
        }
        return allDetailRefs;
    }

    public RadPropDef getPropById(final Id propId) {
        final RadPropDef prop = propsById.get(propId);
        if (prop != null) {
            return prop;
        }

        if (getAncestorId() != null) //check in ancestor
        {
            return release.getClassDef(getAncestorId()).getPropById(propId);
        }

        throw new DefinitionNotFoundError(propId);
    }

    public boolean isPropExistById(final Id propId) {
        if (propsById.containsKey(propId)) {
            return true;
        }
        if (getAncestorId() == null) {
            return false;
        }
        return release.getClassDef(getAncestorId()).isPropExistById(propId);
    }

    public RadMethodDef getMethodById(final Id mhId) {
        final RadMethodDef method = methodsById.get(mhId);
        if (method != null) {
            return method;
        }

        if (getAncestorId() != null) //check in ancestor
        {
            return release.getClassDef(getAncestorId()).getMethodById(mhId);
        }

        throw new DefinitionNotFoundError(mhId);
    }

    public static long getFormatVersion() {
        return 1;
    }

//    public boolean getCurUserCanInstantiate() {
//        return getRelease().getArte().getRights().getCurUserHasRole(getInstantiateRoleIdsStr());
//    }
//    private String roleList = null;
    public Release getRelease() {
        return release;
    }

    public final DdsTableDef getTableDef() {
        if (tableDef == null) {
            final Id entId = getEntityId();
            if (entId != null) {
                tableDef = getRelease().getTableDef(entId);
            }
        }
        return tableDef;
    }

    public final boolean hasPartitionRights() {
        switch (getAccessAreaType()) {
            case NONE:
                return false;
            case OWN:
                return !getEntityClass().accessAreas.isEmpty();
            case INHERITED: {
                return !getEntityClass().accessAreas.isEmpty() || release.getClassDef(RadClassDef.getEntityClassIdByTableId(getAccessAreaInheritRef().getParentTableId())).hasPartitionRights();
            }
            default:
                throw new WrongFormatError("Unsupported access area type: \"" + getAccessAreaType().getName() + "\"", null);
        }
    }

    public final DdsReferenceDef getAccessAreaInheritRef() {
        if (accessAreaInheritRef == null) {
            Id inheritRefId = getAccessAreaInheritRefId();
            if (inheritRefId != null) {
                accessAreaInheritRef = release.getReferenceDef(inheritRefId);
            }
        }
        return accessAreaInheritRef;
    }

    private void addAreas2List(final List<RadClassAccessArea> areas, final RadClassApJoinItem root, final int[] joinsCounter) {
        for (RadClassAccessArea area : getAccessAreas()) {
            if (areas.contains(area)) {
                throw new WrongFormatError("Recurring access areas inheritance is not supported", null);
            }
            areas.add(area);
            for (RadClassAccessArea.Partition partition : area.getPartitions()) //preparing partitions
            {
                partition.setJoinItem(root.join(partition.getReferenceIds(), 0, joinsCounter));//saving part of calculation temporary
            }
        }
        if (getAccessAreaType() == EAccessAreaType.INHERITED) {
            final DdsReferenceDef inherRef = getAccessAreaInheritRef();
            final RadClassDef inherClass = release.getClassDef(RadClassDef.getEntityClassIdByTableId(inherRef.getParentTableId()));
            if (!inherClass.hasPartitionRights()) {
                throw new WrongFormatError("Access partitions joins was inherited from class that has no access areas definitions", null);
            }
            final RadClassApJoinItem joinRoot = root.join(inherRef.getId(), joinsCounter);
            inherClass.addAreas2List(areas, joinRoot, joinsCounter);
        }
    }

    public final RadClassApJoins getApJoins() {
        if (apJoins == null) {
            if (!hasPartitionRights()) {
                throw new DbQueryBuilderError("Access partitions joins was requested but class has no access partition references", null);
            }
            final int[] joinsCounter = new int[]{0};
            final RadClassApJoinItem root = new RadClassApJoinItem(release, release.getTableDef(this.getEntityId()), QuerySqlBuilder.MAIN_ALIAS);
            final List<RadClassAccessArea> areas = new LinkedList<RadClassAccessArea>();
            addAreas2List(areas, root, joinsCounter);	//add and prepare inherited areas

            final StringBuilder apJoinsSql = new StringBuilder();
            for (RadClassApJoinItem join : root.getJoinsByRefId().values()) {
                join.appendJoinsSql(apJoinsSql, QuerySqlBuilder.MAIN_ALIAS);
            }

            final StringBuilder areaListSql = new StringBuilder();
            areaListSql.append("TRdxAcsAreaList(");
            boolean isFirtsArea = true;
            for (RadClassAccessArea area : areas) {
                if (isFirtsArea) {
                    isFirtsArea = false;
                } else {
                    areaListSql.append(',');
                }
                area.appendValSql(areaListSql);
            }
            areaListSql.append(')');

            apJoins = new RadClassApJoins(apJoinsSql.toString(), areaListSql.toString());
        }
        return apJoins;
    }

    public static String getPropDbPresentation(final Arte arte, final Id tabId, final String tabAlias, final Id propId) {
        return getPropDbPresentation(arte.getDefManager().getReleaseCache().getRelease(), tabId, tabAlias, propId);
    }

    public static String getPropDbPresentation(final Release release, final Id tabId, final String tabAlias, final Id propId) {
        final DdsTableDef tab = release.getTableDef(tabId);
        final DdsColumnDef ddsProp = tab.getColumns().findById(propId, ExtendableDefinitions.EScope.ALL).get();
        org.radixware.kernel.common.sqml.Sqml expression = null;
        if (ddsProp != null) {
            expression = ddsProp.getExpression();
        } else {
            final RadClassDef c = release.getClassDef(RadClassDef.getEntityClassIdByTableId(tabId));
            final RadPropDef prop = c.getPropById(propId);
            if (prop instanceof RadSqmlPropDef) {
                expression = ((RadSqmlPropDef) prop).getExpression();
            }
        }
        if (expression != null) {
            return "("
                    + DbQueryBuilder.translateSqmlExpression(Arte.get(), tab, tabAlias, expression)
                    + ")";
        } else if (ddsProp != null) {
            return (tabAlias != null ? tabAlias + "." : "") + ddsProp.getDbName();
        } else {
            throw new IllegalArgumentException("Unsupported column type #" + String.valueOf(propId), null);
        }
    }

    private RadClassDef getEntityClass() {
        if (aecDef == null) {
            final Id entityId = getEntityId();
            if (entityId != null) {
                final Id aecId = getEntityClassIdByTableId(getEntityId());
                if (getId().equals(aecId)) {
                    aecDef = this;
                } else {
                    aecDef = release.getClassDef(aecId);
                }
            }
        }
        return aecDef;
    }

    public static Id getEntityClassIdByTableId(final Id tableId) {
        return Id.Factory.loadFrom(EDefinitionIdPrefix.ADS_ENTITY_CLASS.getValue() + tableId.toString().substring(3));
    }

    public static Id getEntityGroupClassIdByTableId(final Id tableId) {
        return Id.Factory.loadFrom(EDefinitionIdPrefix.ADS_ENTITY_GROUP_CLASS.getValue() + tableId.toString().substring(3));
    }

    public static class UpstandingRefInfo {

        private final DdsReferenceDef reference;
        private final Id parentRefPropId;
        private final Id propInParentId;

        public UpstandingRefInfo(DdsReferenceDef reference, Id parentRefPropId, Id propInParentId) {
            this.reference = reference;
            this.parentRefPropId = parentRefPropId;
            this.propInParentId = propInParentId;
        }

        public DdsReferenceDef getReference() {
            return reference;
        }

        public Id getParentRefPropId() {
            return parentRefPropId;
        }

        public Id getPropInParentId() {
            return propInParentId;
        }

    }
}
