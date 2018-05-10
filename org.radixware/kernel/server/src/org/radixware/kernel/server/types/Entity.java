/*
 * Copyright (c) 2008-2018, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */
package org.radixware.kernel.server.types;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsIndexDef.ColumnInfo;
import org.radixware.kernel.common.defs.dds.DdsPrimaryKeyDef;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.dbq.CreateObjectQuery;
import org.radixware.kernel.server.dbq.DeleteObjectQuery;
import org.radixware.kernel.server.dbq.QuerySqlBuilder;
import org.radixware.kernel.server.dbq.ReadObjectQuery;
import org.radixware.kernel.server.dbq.TranLockObjectQuery;
import org.radixware.kernel.server.dbq.UpObjectOwnerQuery;
import org.radixware.kernel.server.dbq.UpdateObjectQuery;
import org.radixware.kernel.server.meta.clazzes.IRadPropReadAccessor;
import org.radixware.kernel.server.meta.clazzes.IRadPropWriteAccessor;
import org.radixware.kernel.server.meta.clazzes.RadDetailParentRefPropDef;
import org.radixware.kernel.server.meta.clazzes.RadDetailPropDef;
import org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef;
import org.radixware.kernel.server.meta.clazzes.RadInnatePropDef;
import org.radixware.kernel.server.meta.clazzes.RadParentPropDef;
import org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef;
import org.radixware.kernel.server.meta.clazzes.RadPropDef;
import org.radixware.kernel.server.meta.clazzes.RadSqmlPropDef;
import org.radixware.kernel.server.meta.clazzes.RadObjectUpValueRef;
import org.radixware.kernel.server.meta.clazzes.RadUserPropDef;
import org.radixware.kernel.server.meta.clazzes.RadPropDef.ValInheritancePath;
import org.radixware.kernel.server.meta.presentations.RadClassPresentationDef;
import org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import org.apache.commons.io.IOUtils;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsSequenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EAutoUpdateReason;
import org.radixware.kernel.common.enums.EDdsTableExtOption;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EEntityLockMode;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.exceptions.IllegalArgumentError;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.types.Arr;
import org.radixware.kernel.common.types.ArrInt;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.IKernelCharEnum;
import org.radixware.kernel.common.types.IKernelIntEnum;
import org.radixware.kernel.common.types.IKernelStrEnum;
import org.radixware.kernel.server.exceptions.DbQueryBuilderError;
import org.radixware.kernel.server.exceptions.DetailsNotExistsError;
import org.radixware.kernel.server.exceptions.EntityObjectNotExistsError;
import org.radixware.kernel.server.exceptions.PrimaryKeyModificationError;
import org.radixware.kernel.server.exceptions.PropertyIsMandatoryError;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.enums.EEntityInitializationPhase;
import org.radixware.kernel.common.enums.EPropInitializationPolicy;
import org.radixware.kernel.common.enums.ETimingSection;
import org.radixware.kernel.common.meta.RadEnumDef;
import org.radixware.kernel.common.types.ArrChar;
import org.radixware.kernel.common.utils.SystemPropUtils;
import org.radixware.kernel.server.arte.DefManager;
import org.radixware.kernel.server.dbq.*;
import org.radixware.kernel.server.dbq.UpObjectOwnerQuery.OwnerProp;
import org.radixware.kernel.server.exceptions.DatabaseError;
import org.radixware.kernel.server.jdbc.RadixConnection;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.types.EntityPropVals.PropOptional;

/**
 * Base class for persistent application objects
 *
 */
public abstract class Entity extends EntityEvents implements IRadClassInstance {

    //Private fields
    private static final int MAX_INHERITANCE_ACCESS_ROUNDS = SystemPropUtils.getIntSystemProp("rdx.entity.max.inheritance.access.rounds", 10);

    EntityState state;
    private boolean initInProcess = false;
    protected Pid pid = null;
    //private PresentationContext presentationContext = null;
    private final EntityPropVals loadedPropVals;
    private final SortedSet<Id> modifiedPropIds; //sorted because it will be used for query hash calculation
    private final SortedSet<Id> lastModifiedPropIds;
    private Map<DdsReferenceDef, EntityDetails> detailsByRef;
    private Map<Id, Boolean> upHasOwnVal;
// Metainfo
    private DdsTableDef ddsMeta = null;
    private Long creationDbTranSeqNum = null; //see RADIX-4942
    private Long creationSpNesting = null; //see RADIX-4942
    private int autoUpdatePriority = 0;
    private final Long instSpNesting;
    //inheritance cycle detection
    private final List<RadPropDef> inheritedValCalcInProgressProps = new ArrayList<>();
    private final List<RadPropDef> inheritancePathCalcInProgressProps = new ArrayList<>();
    private final List<RadPropDef> propDefinedCalcInProgressProps = new ArrayList<>();
    private IEntityTouchListener entityTouchListener;
    private final long touchTimeMillis = System.currentTimeMillis();

    public DdsTableDef getDdsMeta() {
        if (ddsMeta == null) {
            ddsMeta = getArte().getDefManager().getTableDef(getRadMeta().getEntityId());
        }
        return ddsMeta;
    }

    public RadClassPresentationDef getPresentationMeta() {
        return getRadMeta().getPresentation();
    }

    // Constructor
    public Entity() {
        this(null, false);
    }
    private final Arte arte;

    public final Arte getArte() {
        return arte;
    }

    Entity(Pid pid) {
        super();
        loadedPropVals = null;
        modifiedPropIds = null;
        lastModifiedPropIds = null;
        instSpNesting = null;
        arte = null;
        this.pid = pid;
    }

    protected Entity(final Arte arte, final boolean bDoNotRegisterInArte) {
        super();
        state = new EntityState(this);
        loadedPropVals = new EntityPropVals(this);
        modifiedPropIds = new TreeSet<Id>();
        lastModifiedPropIds = new TreeSet<>();
        upHasOwnVal = null;
        detailsByRef = null;
        this.arte = arte != null ? arte : Arte.get();
        instSpNesting = (this.arte == null ? null : this.arte.getSavePointsNesting());
        if (this.arte != null && !bDoNotRegisterInArte) {
            this.arte.registerNewEntityObject(this);
        }
    }

//Public methods    
    public Pid getPid() {
        if (state.isNewOrInited()) {
            return new Pid(getArte(), getDdsMeta(), loadedPropVals.asMap(), Pid.EEmptyPkPolicy.CREATE_NULL_OBJ);
        }
        return pid;
    }

    public Entity getAnyChildEntity(final DdsReferenceDef childRef) {
        final DdsTableDef childTable = getArte().getDefManager().getTableDef(childRef.getChildTableId());
        final StringBuilder qryBuilder = new StringBuilder();
        qryBuilder.append(buildReadOwnProps(childTable));
        qryBuilder.append(" where (");
        qryBuilder.append(buildConditionByRef(childRef));
        qryBuilder.append(")");
        qryBuilder.append(" and ROWNUM < 2");
        final String qrySql = qryBuilder.toString();
        arte.getTrace().put(EEventSeverity.DEBUG, "Child exists query built: " + qrySql, EEventSource.DB_QUERY_BUILDER);
        try {
            PreparedStatement qry = null;
            getArte().getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            try {
                qry = arte.getDbConnection().get().prepareStatement(qrySql);
            } finally {
                getArte().getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            }
            int paramIdx = 1;
            for (DdsReferenceDef.ColumnsInfoItem refProp : childRef.getColumnsInfo()) {
                final DdsColumnDef parentColumn = refProp.getParentColumn();
                DbQuery.setParam(arte, qry, paramIdx, parentColumn.getValType(), parentColumn.getDbType(), getProp(refProp.getParentColumnId()), refProp.getName());
                paramIdx++;
            }
            try {
                ResultSet rs = null;
                getArte().getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
                try {
                    rs = qry.executeQuery();
                } finally {
                    getArte().getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
                }
                try {
                    if (rs.next()) {
                        EntityPropVals vals = new EntityPropVals();
                        paramIdx = 1;
                        for (DdsColumnDef column : childTable.getColumns().get(ExtendableDefinitions.EScope.ALL)) {
                            vals.putPropValById(column.getId(), DbQuery.getFieldVal(arte, rs, paramIdx, column.getValType(), column.getDbType()));
                            paramIdx++;
                        }
                        Pid childPid = new Pid(getArte(), childTable, vals.asMap());//last column is pidAsStr
                        //if child table is detail table, we should return master entity object
                        if (childTable.isDetailTable()) {
                            final DdsReferenceDef masterRef = childTable.findMasterReference();
                            if (masterRef == null) {
                                throw new RadixError("Unable to find master table for " + childTable.getDbName() + " [#" + childTable.getId() + "]");
                            }
                            //transfer values of the primary key of the detail record to the corresponding properties of the master record
                            EntityPropVals masterPropVals = new EntityPropVals();
                            for (DdsReferenceDef.ColumnsInfoItem refProp : masterRef.getColumnsInfo()) {
                                final PropOptional propOptional = vals.getPropOptionalById(refProp.getChildColumnId());
                                if (propOptional.isPresent()) {
                                    masterPropVals.putPropValById(refProp.getParentColumnId(), propOptional.get());
                                } else {
                                    //shouldn't happen
                                }
                            }
                            vals = masterPropVals;
                            childPid = new Pid(arte, masterRef.getParentTableId(), vals.asMap());
                        }
                        try {
                            return getArte().getEntityObject(childPid, vals, false);
                        } catch (Exception ex) {
                            //in situation when some error occurred during entity loading
                            //this exception can tell that database actually contains some child
                            //objects by this reference
                            throw new ErrorOnEntityCreation("Error while loading child entity", ex);
                        }
                    } else {
                        return null;
                    }
                } finally {
                    rs.close();
                }
            } finally {
                qry.close();
            }
        } catch (SQLException e) {
            throw new DatabaseError("Can't check child record exists: " + e.getMessage(), e);
        }
    }

    private String buildConditionByRef(final DdsReferenceDef childRef) {
        boolean isFirst = true;
        final StringBuilder conditionBuilder = new StringBuilder();
        for (DdsReferenceDef.ColumnsInfoItem refProp : childRef.getColumnsInfo()) {
            if (isFirst) {
                isFirst = false;
            } else {
                conditionBuilder.append(" and ");
            }
            final String columnName = RadClassDef.getPropDbPresentation(getArte(), childRef.getChildTableId(), null, refProp.getChildColumnId());
            conditionBuilder.append(columnName);
            conditionBuilder.append("=?");
        }
        return conditionBuilder.toString();
    }

    private String buildReadOwnProps(final DdsTableDef childTable) {
        final StringBuilder selectBuilder = new StringBuilder("select ");
        boolean isFirst = true;
        for (DdsColumnDef column : childTable.getColumns().get(ExtendableDefinitions.EScope.ALL)) {
            if (isFirst) {
                isFirst = false;
            } else {

                selectBuilder.append(", ");
            }
            final String columnName = RadClassDef.getPropDbPresentation(getArte(), childTable.getId(), null, column.getId());
            selectBuilder.append(columnName);

        }
        selectBuilder.append(" from ");
        selectBuilder.append(childTable.getDbName());
        return selectBuilder.toString();
    }

    /**
     * Checks if any object that has a reference to this entity exists.
     *
     * Search is performed only for references to this table (references to
     * details of this table are not scanned)
     *
     * @return
     */
    public boolean isChildExists(final DdsReferenceDef childRef) {
        return getAnyChildEntity(childRef) != null;
    }

    public final boolean wasRead() {
        return state.wasRead();
    }

    public int getAutoUpdatePriority() {
        return autoUpdatePriority;
    }

    public void setAutoUpdatePriority(int autoUpdatePriority) {
        this.autoUpdatePriority = autoUpdatePriority;
    }

    public final boolean isInDatabase(final boolean doSelect) {
        if (state.isInDatabase()) {
            if (!doSelect) {
                return true;
            }
            if (pid.isExistsInDb()) {
                return true;
            }
            dbObjDoesNotExistsAnyMore();
        }
        return false;
    }

    public final Id getEntityId() {
        return getDdsMeta().getId();
    }

    public final void load(@SuppressWarnings("hiding")
            final Pid pid, final EntityPropVals loadedProps) {
        load(pid, loadedProps, false);
    }

    public final void loadAndMarkAsRead(@SuppressWarnings("hiding")
            final Pid pid, final EntityPropVals loadedProps) {
        load(pid, loadedProps, true);
    }

    private void load(@SuppressWarnings("hiding")
            final Pid pid, final EntityPropVals loadedProps, boolean markAsRead) {
        if (!state.isNew()) {
            throw new IllegalUsageError("Can't activate object which is not new");
        }
        state.set(EntityState.Enum.IN_DATABASE);
        setPid(pid);
        if (!(this instanceof EntityDetails)) {
            getArte().registerExistingEntityObject(this);
        }
        if (loadedProps != null) {
            final Map<Id, Object> values = loadedProps.asMap();
            for (Id propId : values.keySet()) {
                if (getRadMeta().isPropExistById(propId) || isVirtualColumn(propId)) {
                    loadedPropVals.putPropValById(propId, values.get(propId));//NOPMD
                }
            }
            if (markAsRead) {
                state.afterRead();
            }
            afterRead();
        }
    }
//Reference properties

    public final Entity getParentRef(final Id refId) {
        return getParentRef(getArte().getDefManager().getReferenceDef(refId));
    }

    public final Entity getParentRef(final DdsReferenceDef ref) {
        return getParentRef(ref, null);
    }

    public final Entity getParentRef(final DdsReferenceDef ref, final RadPropDef prop) {
        state.assertReadAllowed();
        if (!ref.getChildTableId().equals(getDdsMeta().getId())) {
            throw new IllegalArgumentError("Reference \"" + ref.getName() + "\" (#" + ref.getId() + ") is not parent reference.");
        }
        final DefManager defManager = getArte().getDefManager();
        final DdsTableDef parentTab = defManager.getTableDef(ref.getParentTableId());
        final DdsReferenceDef parent2masterRef = defManager.getMasterReferenceDef(parentTab);
        Entity parent = parentsPtrByRefCache.get(ref);
        if (parent != null) {
            if (!parent.state.isDiscarded() && arte.getCache().isRegistered(parent)) {
                //if reference is to details then master is stored in cache
                //in this case we need to get details object because it will be the real parent for the reference
                final Entity referencedObject = parent2masterRef == null ? parent : parent.getDetailRef(parent2masterRef);
                //let's check that this parent is still appropriate one
                boolean bOk = true;
                for (DdsReferenceDef.ColumnsInfoItem refProp : ref.getColumnsInfo()) {
                    final Object childKeyVal = getNativePropOwnVal(refProp.getChildColumnId());
                    final Object parentKeyVal = referencedObject.getNativePropOwnVal(refProp.getParentColumnId());
                    bOk = normalizedValEquals(childKeyVal, parentKeyVal);
                    if (!bOk) {
                        break;
                    }
                }
                if (bOk) {
                    return parent;
                }
            }
        } else if (parentsPtrByRefCache.contains(ref)) {
            return null;
        }
        parent = getArte().findParentInNewEntityObjects(ref, this);
        if (parent != null) {
            parentsPtrByRefCache.cache(ref, parent);
            return parent;
        }

        final HashMap<Id, Object> key = new HashMap<Id, Object>();
        for (DdsReferenceDef.ColumnsInfoItem refProp : ref.getColumnsInfo()) {
            final Object fkVal = getNativePropOwnVal(refProp.getChildColumnId());
            if (fkVal == null) {
                parentsPtrByRefCache.cache(ref, null);
                return null;
            }
            key.put(refProp.getParentColumnId(), fkVal);
        }
        Pid parentPid = new Pid(getArte(), parentTab, key);
        if (parent2masterRef != null) {//is detail table, return master instead
            final DdsTableDef masterTab = defManager.getTableDef(parent2masterRef.getParentTableId());
            final List<Object> detPk = parentPid.getPkVals();
            key.clear();
            for (ColumnInfo detPkProp : parentTab.getPrimaryKey().getColumnsInfo()) {
                key.put(defManager.getMasterKeyPropIdByDetailKeyPropId(parent2masterRef, detPkProp.getColumnId()), detPk.get(key.size()));
            }
            parentPid = new Pid(getArte(), masterTab, key);
        }

        if (prop != null && prop.getIsValInheritable() && prop.getValIsInheritMark(arte, parentPid)) {
            return createDummyInheritMarkEntity(parentPid, prop);
        }

        parent = getArte().getEntityObject(parentPid);
        parentsPtrByRefCache.cache(ref, parent);
        return parent;
    }
    private final EntityParentsByRefCache parentsPtrByRefCache = new EntityParentsByRefCache();

    public final void setParentRef(final Id refId, final Entity parent) {
        setParentRef(getArte().getDefManager().getReferenceDef(refId), parent);
    }

    public final void setParentRef(final DdsReferenceDef ref, final Entity parent) {
        state.assertWriteAllowed();
        if (!ref.getChildTableId().equals(getDdsMeta().getId())) {
            throw new IllegalArgumentError("Reference \"" + ref.getName() + "\" (#" + ref.getId() + ") is not parent reference.");
        }
        if (isPersistenPropReadonly(ref.getId()) && parent != getParentRef(ref)) {
            throw new IllegalUsageError("Try to modify readonly property \"" + getRadMeta().getPropById(ref.getId()).getName() + "\" (#" + ref.getId() + ")");
        }
        if (parent != null) {
            DdsReferenceDef mdRef = null;
            if (!ref.getParentTableId().equals(parent.getDdsMeta().getId())) {
                mdRef = arte.getDefManager().getMasterReferenceDef(getArte().getDefManager().getTableDef(ref.getParentTableId()));
                if (mdRef == null || !mdRef.getParentTableId().equals(parent.getDdsMeta().getId())) {
                    throw new IllegalArgumentError("Try to set parent of wrong type");
                }
            }
            for (DdsReferenceDef.ColumnsInfoItem refProp : ref.getColumnsInfo()) {
                final RadPropDef prop = getRadMeta().getPropById(refProp.getChildColumnId());
                Id parentColumnId = refProp.getParentColumnId();
                if (mdRef != null) {
                    parentColumnId = getArte().getDefManager().getMasterPropIdByDetailPropId(mdRef, parent.getRadMeta(), parentColumnId);
                }
                final Object newVal = parent.getProp(parentColumnId);
                if (newVal == null && !(ref.getType() == DdsReferenceDef.EType.MASTER_DETAIL && !isInDatabase(false))) {
                    throw new IllegalUsageError("Can't change parent reference \"" + ref.getName() + "\" (#" + ref.getId() + "). \nParent key property \"" + parent.getRadMeta().getPropById(parentColumnId).getName() + "\"(#" + parentColumnId + ") is null. \nParent can't be identified.");
                }
                if (prop.getAccessor() instanceof IRadPropWriteAccessor && !isPersistenPropReadonly(refProp.getChildColumnId())) {
                    setProp(refProp.getChildColumnId(), newVal);
                } else {
                    final Object curVal = getProp(refProp.getChildColumnId());
                    final boolean changed = (curVal == null ? newVal != null : !curVal.equals(newVal));
                    if (changed) {
                        throw new IllegalUsageError("Can't change parent reference \"" + ref.getName() + "\" (#" + ref.getId() + "). \nForeign key property \"" + prop.getName() + "\"(#" + prop.getId().toString() + ") is readonly.");
                    }
                }
            }
        } else {
            boolean isFkCleared = false;
            final List<Id> propsToClear = new ArrayList<>();
            for (DdsReferenceDef.ColumnsInfoItem refProp : ref.getColumnsInfo()) {
                propsToClear.add(refProp.getChildColumnId());
            }
            if (isNewObject()) {
                final List<Id> pkProps = new ArrayList<>();
                int pkPropsInRefCnt = 0;
                for (ColumnInfo pkColInfo : getDdsMeta().getPrimaryKey().getColumnsInfo()) {
                    pkProps.add(pkColInfo.getColumnId());
                    if (propsToClear.contains(pkColInfo.getColumnId())) {
                        pkPropsInRefCnt++;
                    }
                }
                if (pkPropsInRefCnt < propsToClear.size()) {
                    propsToClear.removeAll(pkProps);//RADIX-12267
                }
            }
            for (Id propId : propsToClear) {
                if (getRadMeta().getPropById(propId).getAccessor() instanceof IRadPropWriteAccessor) {
                    try {
                        if (!isPersistenPropReadonly(propId)) { //RADIX-386
                            setProp(propId, null);
                            isFkCleared = true;
                        }
                    } catch (PrimaryKeyModificationError e) {
                        continue;
                    }
                }
            }
            if (!isFkCleared) {
                throw new IllegalUsageError("Can't set parent reference \"" + ref.getName() + "\" (#" + ref.getId() + ") to NULL. \nAll foreign key properties are readonly.");
            }
        }
        parentsPtrByRefCache.cache(ref, parent);
    }

    public final Entity getDetailRef(final DdsReferenceDef ref) {
        state.assertReadAllowed();
        if (!(ref.getParentTableId().equals(getDdsMeta().getId()) && ref.getType() == DdsReferenceDef.EType.MASTER_DETAIL)) {
            throw new IllegalArgumentError("Reference \"" + ref.getName() + "\" (#" + ref.getId() + ") is not detail reference.");
        }
        if (!getRadMeta().getDetailsRefs().contains(ref)) {
            throw new DetailsNotExistsError("Object has no requested details: detail's reference #" + ref.getId() + " is not registered in class #" + getRadMeta().getId(), null);
        }
        EntityDetails res = detailsByRef == null ? null : detailsByRef.get(ref);
        if (res != null) {
            return res;
        }
        res = new EntityDetails(getArte(), ref);
        if (isInDatabase(false)) {
            final HashMap<Id, Object> key = new HashMap<Id, Object>();
            for (DdsReferenceDef.ColumnsInfoItem refProp : ref.getColumnsInfo()) {
                key.put(refProp.getChildColumnId(), getProp(refProp.getParentColumnId()));
            }
            try {
                res.load(new Pid(getArte(), getArte().getDefManager().getTableDef(ref.getChildTableId()), key), null);
                res.read(EEntityLockMode.NONE, null);
            } catch (EntityObjectNotExistsError e) {
                throw new DetailsNotExistsError("Object has no requested details (reference \"" + ref.getName() + "\", #" + ref.getId() + "): master-detail structure is corrupted", e);
            }
        } else {
            res.setParentRef(ref, this);
        }
        if (detailsByRef == null) {
            detailsByRef = new HashMap<DdsReferenceDef, EntityDetails>();
        }
        detailsByRef.put(ref, res);
        return res;
    }

    private void checkPkIsDefined() {
        final DdsPrimaryKeyDef pk = getDdsMeta().getPrimaryKey();
        if (pk != null) {
            for (DdsIndexDef.ColumnInfo col : pk.getColumnsInfo()) {
                final PropOptional propOptional = loadedPropVals.getPropOptionalById(col.getColumnId());
                if (!propOptional.isPresent() || propOptional.get() == null) {
                    throw new PropertyIsMandatoryError(getRadMeta().getId(), col.getColumnId());
                }
            }
        }
    }

    public final Object getPropInheritedVal(final Id propId) {
        return getPropInheritedVal(getRadMeta().getPropById(propId));
    }

    public final Object getPropInheritedVal(final RadPropDef prop) {
        final String stepDescr = "getInheritedValue";
        if (count(inheritedValCalcInProgressProps, prop) == MAX_INHERITANCE_ACCESS_ROUNDS) {
            throw new PropInheritanceCycleException("Property value inheritance cycle", this, prop, stepDescr);
        }
        inheritedValCalcInProgressProps.add(prop);
        try {
            NEXT_PATH:
            for (RadPropDef.ValInheritancePath path : prop.getValInheritPathes()) {
                Entity parent = this;
                for (final Id refPropId : path.getRefPropIds()) {
                    parent = (Entity) parent.getProp(refPropId);
                    if (parent == null) {
                        continue NEXT_PATH;
                    }
                }
                if (parent.getIsPropValDefined(path.getDestPropId())) {
                    return parent.getProp(path.getDestPropId());
                }
            }
        } catch (PropInheritanceCycleException ex) {
            ex.appendStep(stepDescr, this, prop);
            throw ex;
        } finally {
            inheritedValCalcInProgressProps.remove(prop);
        }
        return null;
    }

    private boolean normalizedValEquals(final Object val1, final Object val2) {
        if (val1 == null) {
            return val2 == null;
        }
        return normalizeVal(val1).equals(val2);
    }

    private Object normalizeVal(final Object val) {
        if (val == null) {
            return null;
        }
        if (val instanceof IKernelIntEnum) {
            return ((IKernelIntEnum) val).getValue();
        } else if (val instanceof IKernelStrEnum) {
            return ((IKernelStrEnum) val).getValue();
        } else if (val instanceof IKernelCharEnum) {
            return ((IKernelCharEnum) val).getValue();
        } else if (val instanceof Pid) {
            return getArte().getEntityObject((Pid) val);
        } else if (val instanceof Arr) {
            final Arr a = (Arr) val;
            //edited by yremizov:
            //old code:
/*
             * if (a.getItemValType() == EValType.STR || a.getItemValType() ==
             * EValType.INT) { final Arr normArr = a.getItemValType() ==
             * EValType.STR ? new ArrStr() : new ArrInt(); for (Object i : a) {
             * normArr.add(normalizeVal(i)); } return normArr; }
             */
            //new code:
            final Arr normArr;
            switch (a.getItemValType()) {
                case STR:
                    normArr = new ArrStr(a.size());
                    break;
                case CHAR:
                    normArr = new ArrChar(a.size());
                    break;
                case INT:
                    normArr = new ArrInt(a.size());
                    break;
                default:
                    return val;
            }
            for (Object i : a) {
                normArr.add(normalizeVal(i));
            }
            return normArr;

        }
        return val;
    }

    protected Object adjustValue(final Object x, final RadPropDef propMeta) {
        if (propMeta.getValType() == EValType.DATE_TIME
                && propMeta instanceof RadInnatePropDef
                && ((RadInnatePropDef) propMeta).getDbPrecision() == 0
                && x instanceof Timestamp) {
            final Timestamp roundedValue = new Timestamp(((Timestamp) x).getTime());
            roundedValue.setNanos(0);
            return roundedValue;
        }
        return x;
    }

    protected final void setNativeDetailProp(RadDetailPropDef propDef, Object x) {
        final Entity detail = getDetailRef(propDef.getDetailReference());
        if (propDef.getValType() == EValType.PARENT_REF) {
            detail.setParentRef(propDef.getJoinedPropId(), (Entity) normalizeVal(x));
        } else {
            detail.setNativeProp(propDef.getJoinedPropId(), x);
        }
    }

    protected final void setNativeInnateRefProp(RadInnateRefPropDef propDef, Object x) {
        setParentRef(propDef.getReferenceId(), (Entity) normalizeVal(x));
    }

    private void ensureValidObject(final RadPropDef propDef, final Object x) {
        if (propDef.getValType() == EValType.PARENT_REF) {
            if (x != null && !(x instanceof Entity) && !(x instanceof Pid)) {
                throw new IllegalArgumentError("Only Entity or Pid instance can be value of ParentRef property");
            }
        }
    }

    private void ensureNotChangingClassGuid(final DdsColumnDef columnDef, final Object x) {
        if (columnDef != null && columnDef == getDdsMeta().findClassGuidColumn()) {
            if (x == null || !x.equals(getRadMeta().getId().toString())) {
                throw new IllegalUsageError("Try to change \"CLASSGUID\" property");
            }
        }
    }

    private void ensureNotChangingPrimaryKey(final DdsColumnDef columnDef, final Id propId, final Object normalizedNewVal) {
        if (isInDatabase(false)) {//is not new object
            if (columnDef != null && columnDef.isPrimaryKey()) {
                final Object oldVal = normalizeVal(getNativeProp(propId));
                if (normalizedNewVal == null || !normalizedNewVal.equals(oldVal)) {
                    throw new PrimaryKeyModificationError("Try to change primary key property of existing object");
                }
            }
        }
    }

    private boolean checkModified(final RadPropDef propDef, final Id propId, final Object normalizedNewVal) {
        if (!loadedPropVals.containsPropById(propId) && isInDatabase(false) && !wasRead()) {
            read(EEntityLockMode.NONE, 0l);
        }
        final PropOptional propOptional = loadedPropVals.getPropOptionalById(propId);
        if (!propOptional.isPresent()) {
            return true;
        }
        final Object normalizedCurVal = normalizeVal(propOptional.get());
        if (normalizedCurVal == normalizedNewVal || (normalizedCurVal != null
                && !propDef.getValType().equals(EValType.BLOB)
                && !propDef.getValType().equals(EValType.CLOB)
                && normalizedCurVal.equals(normalizedNewVal))) {
            return false;
        }
        return true;
    }

    private void modifyProp(final DdsColumnDef columnDef, final Id propId, final Object x) {
        if (isPersistenPropReadonly(propId)) {
            throw new IllegalUsageError("Try to modify readonly property \"" + columnDef.getName() + "\" (#" + propId + ")");
        }
        loadedPropVals.putPropValById(propId, x);
        if (columnDef != null && columnDef.getExpression() == null) {
            setPropModified(propId);
        }
        parentsPtrByRefCache.clear();

        if (detailsByRef != null && !isInDatabase(false)) {
            for (Map.Entry<DdsReferenceDef, EntityDetails> entry : detailsByRef.entrySet()) {
                entry.getValue().setParentRef(entry.getKey(), this);
            }
        }
    }

// Native properties
    protected final void setNativeProp(final Id id, Object x) {
        state.assertWriteAllowed();
        touch();
        final RadPropDef dacProp = getRadMeta().getPropById(id);
        ensureValidObject(dacProp, x);

        if (dacProp instanceof RadInnatePropDef || dacProp instanceof RadSqmlPropDef) {
            final DdsColumnDef columnProp = dacProp instanceof RadSqmlPropDef ? null : getDdsMeta().getColumns().getById(id, ExtendableDefinitions.EScope.ALL);

            ensureNotChangingClassGuid(columnProp, x);

            x = adjustValue(x, dacProp);

            final Object normalizedNewVal = normalizeVal(x);

            final boolean modified = checkModified(dacProp, id, normalizedNewVal);
            if (modified) {
                ensureNotChangingPrimaryKey(columnProp, id, normalizedNewVal);
                modifyProp(columnProp, id, x);
            } else if (state.isNew() && !initInProcess) {
                modifyProp(columnProp, id, x);
            }
        } else if (dacProp instanceof RadDetailPropDef) {
            setNativeDetailProp((RadDetailPropDef) dacProp, x);
        } else if (dacProp instanceof RadInnateRefPropDef) {
            setNativeInnateRefProp((RadInnateRefPropDef) dacProp, x);
        } else {
            throw new DefinitionError("Property #" + id + " is not native");
        }
    }

    protected final void refinePropVal(final Id id, final Object refinedVal) {
        refinePropVal(id, refinedVal, true);
    }

    protected final void refinePropVal(final Id id, final Object refinedVal, boolean check) {
        final RadPropDef dacProp = getRadMeta().getPropById(id);
        if (dacProp instanceof RadDetailPropDef) {
            final RadDetailPropDef detProp = (RadDetailPropDef) dacProp;
            final Entity detail = getDetailRef(detProp.getDetailReference());
            if (detProp.getValType() == EValType.PARENT_REF) {
                detail.refineParentRef(getArte().getDefManager().getReferenceDef(detProp.getJoinedPropId()), refinedVal);
            } else {
                detail.refinePropVal(detProp.getJoinedPropId(), refinedVal, check);
            }
            return;
        } else if (dacProp instanceof RadInnateRefPropDef) {
            final RadInnateRefPropDef ptProp = (RadInnateRefPropDef) dacProp;
            refineParentRef(ptProp.getReference(), refinedVal);
        } else {
            if (check) {
                final PropOptional propOptional = loadedPropVals.getPropOptionalById(id);
                if (!propOptional.isPresent()) {
                    throw new IllegalUsageError("Try to refine not loaded value", propOptional.cause(id));
                }
                final Object normalizedCurVal = normalizeVal(propOptional.get());
                if (normalizedCurVal == null ? refinedVal == null : normalizedCurVal.equals(normalizeVal(refinedVal))) {
                    loadedPropVals.putPropValById(id, refinedVal);
                } else {
                    throw new IllegalUsageError("Refined value is not equal to value");
                }
            } else {
                loadedPropVals.putPropValById(id, refinedVal);
            }
        }
    }

    private void refineParentRef(final DdsReferenceDef ref, final Object refinedVal) throws IllegalUsageError {
        final Object normalizedCurVal = normalizeVal(parentsPtrByRefCache.get(ref));
        final Object normalizedNewVal = normalizeVal(refinedVal);
        if (normalizedCurVal == null ? normalizedNewVal == null : normalizedCurVal.equals(normalizedNewVal)) {
            parentsPtrByRefCache.cache(ref, (Entity) normalizedNewVal);
        } else {
            throw new IllegalUsageError("Refined value is not equal to value");
        }
    }

    public final void putLoadedPropVal(final Id id, final Object val) {
        final RadPropDef dacProp = getRadMeta().getPropById(id);
        if (dacProp instanceof RadDetailPropDef) {
            final RadDetailPropDef detProp = (RadDetailPropDef) dacProp;
            final Entity detail = getDetailRef(detProp.getDetailReference());
            detail.putLoadedPropVal(detProp.getJoinedPropId(), val);
            return;
        } else if (dacProp instanceof RadInnateRefPropDef) {
            return;
        } else {
            loadedPropVals.putPropValById(id, normalizeVal(val));
        }
    }

    public final Object getNativePropOwnVal(final Id propId) {
        return getNativePropOwnVal(getRadMeta().getPropById(propId));
    }

    private Object getInitPropVal(final RadPropDef dacProp) {
        if (//RADIX-1640
                dacProp.getIsValInheritable()
                && (dacProp.getInitPolicy() == EPropInitializationPolicy.DO_NOT_DEFINE
                || dacProp.getInitPolicy() == EPropInitializationPolicy.DEFINE_IF_NOT_INHERITED
                && getPropValInheritancePath(dacProp.getId()) != null // value can be inherited
                )) {
            return dacProp.getValInheritMarkVal(getArte());
        } else {
            final Object val = dacProp.getInitVal(getArte());
            if (dacProp instanceof RadInnatePropDef && ((RadInnatePropDef) dacProp).isDbInitValOverriden()) {
                setPropModified(dacProp.getId());
            }
            return val;
        }
    }

    private Object getDetailPropOwnVal(final RadDetailPropDef detProp) {
        final Entity detail = getDetailRef(detProp.getDetailReference());
        if (detProp.getValType() == EValType.PARENT_REF) {
            return detail.getParentRef(detProp.getJoinedPropId());
        } else {
            return detail.getNativeProp(detProp.getJoinedPropId());
        }
    }

    private Object getNativePropOwnVal(final RadPropDef dacProp) {
        if (dacProp instanceof RadInnatePropDef || dacProp instanceof RadSqmlPropDef) {
            if (!loadedPropVals.containsPropById(dacProp.getId())) {
                if (isInDatabase(false)) {
                    read(EEntityLockMode.NONE, null);
                } else {
                    final Object val = getInitPropVal(dacProp);
                    loadedPropVals.putPropValById(dacProp.getId(), val); // dangerous(???) optimization caused (???) RADIX-4266
                    return val;
                }
            }
            PropOptional propOptional = loadedPropVals.getPropOptionalById(dacProp.getId());
            if (propOptional.isPresent()) {
                return propOptional.get();
            } else {
                throw new RadixError("Native property #'" + dacProp.getId() + "' requested but can not be loaded");
            }
        } else if (dacProp instanceof RadDetailPropDef) {
            return getDetailPropOwnVal((RadDetailPropDef) dacProp);
        } else if (dacProp instanceof RadInnateRefPropDef) {
            final RadInnateRefPropDef ptProp = (RadInnateRefPropDef) dacProp;
            return getParentRef(ptProp.getReference(), dacProp);
        } else {
            throw new DefinitionError("Property #" + dacProp.getId() + " is not native");
        }
    }

    protected final Object getNativeProp(final Id id) {
        state.assertReadAllowed();
        touch();

        final RadPropDef dacProp = getRadMeta().getPropById(id);
        final Object ownVal = getNativePropOwnVal(dacProp);

        final RadClassDef.UpstandingRefInfo upstandingRefInfo = getRadMeta().getUpstandingRefInfo(id);
        if (upstandingRefInfo != null) {
            // RADIX-892
            // if it is a FK column and a parentRef prop on this FK exists and its value is inherited
            // then return value of FK corresponded to inherited parent prop value
            if (!getPropHasOwnVal(upstandingRefInfo.getParentRefPropId())) {
                final Entity referencedObj = (Entity) getProp(upstandingRefInfo.getParentRefPropId());
                return referencedObj == null ? null : referencedObj.getProp(upstandingRefInfo.getPropInParentId());
            }
        }

        if (dacProp.getIsValInheritable() && dacProp.getValIsInheritMark(getArte(), ownVal)) {
            return getPropInheritedVal(dacProp);
        }
        return ownVal;
    }

    @Override
    public Object getProp(final Id id) {
        if (getRadMeta() != null) {
            final RadPropDef prop = getRadMeta().getPropById(id);
            if (prop.getAccessor() instanceof IRadPropReadAccessor) {
                return ((IRadPropReadAccessor) prop.getAccessor()).get(this, id);
            } else {
                throw new IllegalUsageError("Try to get write-only property \"" + prop.getName() + "\" (#" + id + ") value");
            }
        }
        throw new DefinitionNotFoundError(id);
    }

    @Override
    public final void setProp(final Id id, final Object x) {
        if (getRadMeta() != null) {
            final RadPropDef prop = getRadMeta().getPropById(id);
            if (prop.getAccessor() instanceof IRadPropWriteAccessor) {
                ((IRadPropWriteAccessor) prop.getAccessor()).set(this, id, x);
            } else {
                throw new IllegalUsageError("Try to modify readonly property \"" + prop.getName() + "\" (#" + id + ")");
            }
        } else {
            throw new DefinitionNotFoundError(id);
        }
    }

    public final void setProps(final Map<Id, Object> propValsById) {
        for (Map.Entry<Id, Object> entry : propValsById.entrySet()) {
            setProp(entry.getKey(), entry.getValue());
        }
    }
/////////////////////////////// User Properties maintenanc? ///////////////////////////////

    protected final void setUserProp(final Id id, final Object newVal) {
        //Arte.checkBreak();
        state.assertWriteAllowed();
        final RadPropDef prop = getRadMeta().getPropById(id);//throws exception if the property doesn't exist
        Object normalizedNewVal = normalizeVal(newVal);
        boolean modified = true;
        for (int i = 0; i < 2; i++) {
            PropOptional propOptional = loadedPropVals.getPropOptionalById(id);
            if (propOptional.isPresent()) {
                Object curVal = propOptional.get();
                try {
                    curVal = normalizeVal(curVal);
                } catch (EntityObjectNotExistsError e) {
                    //to support restore from broken ref
                }
                if (getUserPropHasOwnVal(id) && (curVal == normalizedNewVal || (curVal != null
                        && !prop.getValType().equals(EValType.BLOB)
                        && !prop.getValType().equals(EValType.CLOB)
                        && curVal.equals(normalizedNewVal)))) {
                    modified = false;
                }
            } else {
                if (i == 0 && isInDatabase(false)) {
                    try {
                        readUserPropVal((RadUserPropDef) prop);
                    } catch (EntityObjectNotExistsError e) {
                        //to support restore from broken ref
                    }
                    continue;
                }
                modified = true;
            }
            break;
        }

        if (modified) {
            if (isPersistenPropReadonly(id)) {
                throw new IllegalUsageError("Try to modify readonly property \"" + prop.getName() + "\" (#" + id + ")");
            }
            boolean isAlreadyOwn = false;
            if (prop.getValType() == EValType.OBJECT) {
                //deleting old object on object change
                Entity oldVal;
                boolean wasBroken = false;
                try {
                    oldVal = (Entity) getProp(id);
                } catch (EntityObjectNotExistsError e) {
                    oldVal = null;//to allow restoring from incorrect state
                    wasBroken = true;//to distinguish modifications null->null and broken->null
                }
                if (oldVal != normalizedNewVal) { // if the value of the property is changed
                    if (oldVal != null && getUserPropHasOwnVal(id)) {
                        try {
                            oldVal.discard(); //TWRBS-1509
                            //oldVal.delete();
                        } catch (EntityObjectNotExistsError e) {
                            //continue to allow restoring from incorrect state;
                        }
                    }
                    if (newVal != null) { //linking val with user property
                        final Entity obj = (Entity) normalizedNewVal;
                        obj.initUpValRef(new RadObjectUpValueRef(this, id));
                        normalizedNewVal = obj;// initUpRef fills PK
                    }
                } else if (getUserPropHasOwnVal(id) && !wasBroken) {
                    isAlreadyOwn = true;
                }
            }
            if (!isAlreadyOwn) {//RADIX-6497, setUserPropHasOwnVal always marks this prop as modified, 
                //but it can lead to removal of the actual value if it is not really modified
                setUserPropHasOwnVal(id, true);
            }
            loadedPropVals.putPropValById(id, normalizedNewVal);
            //setPropModified(id); // is called in setIsOwnUserPropVal
        }
    }

    protected final Object getUserProp(final Id id) {
        state.assertReadAllowed();

        final RadUserPropDef prop = getUserPropDef(id);

        if (!getPropHasOwnVal(id)) {
            if (prop.getIsValInheritable()) {
                return getPropInheritedVal(prop);
            } else {
                return null;
            }
        }

        PropOptional propOptional = loadedPropVals.getPropOptionalById(id);
        Object val = null;
        if (propOptional.isPresent()) {
            val = propOptional.get();
        } else {
            if (isInDatabase(false)) {
                readUserPropVal(prop);
            } else {
                loadedPropVals.putPropValById(id, null);
                if (upHasOwnVal == null) {
                    upHasOwnVal = new HashMap<Id, Boolean>();
                }
                upHasOwnVal.put(id, Boolean.FALSE);
            }
            propOptional = loadedPropVals.getPropOptionalById(id);
            if (propOptional.isPresent()) {
                val = propOptional.get();
            } else {
                throw new DbQueryBuilderError("User property requested but not loaded", propOptional.cause(id));
            }
        }
        if (val instanceof Entity) { // if entity is discarded  then try to find entity by its pid
            final Entity e = (Entity) val;
            if (e.state.isDiscarded()) {
                val = e.getPid();
            }
        }
        if (val instanceof Pid) {// for entities key is stored
            final Entity newObject = getArte().findNewEntityObjectByPid((Pid) val);
            if (newObject != null) {
                return newObject;
            }
            return getArte().getEntityObject((Pid) val);
        } else {
            return val;
        }
    }

    public final boolean getIsPropValDefined(final Id id) {
        return getIsPropValDefinedImpl(id, true);
    }

    private boolean getIsPropValDefinedImpl(final Id id, final boolean deepValInheritanceCheck) {
        state.assertReadAllowed();
        if (getPropHasOwnValImpl(id, deepValInheritanceCheck)) {
            return true;
        }
        final String stepDescr = "isPropValDefined";
        final RadPropDef prop = getRadMeta().getPropById(id);
        if (count(propDefinedCalcInProgressProps, prop) == MAX_INHERITANCE_ACCESS_ROUNDS) {
            throw new PropInheritanceCycleException("Cycle in property value existence check", this, prop, stepDescr);
        }
        propDefinedCalcInProgressProps.add(prop);
        try {
            if (prop.getIsValInheritable()) {
                NEXT_PATH:
                for (RadPropDef.ValInheritancePath path : prop.getValInheritPathes()) {
                    Entity parent = this;
                    for (final Id refPropId : path.getRefPropIds()) {
                        parent = (Entity) parent.getProp(refPropId);
                        if (parent == null) {
                            continue NEXT_PATH;
                        }
                    }
                    if (parent.getIsPropValDefinedImpl(path.getDestPropId(), deepValInheritanceCheck)) {
                        return true;
                    }
                }
            }
        } catch (PropInheritanceCycleException ex) {
            ex.appendStep(stepDescr, this, prop);
            throw ex;
        } finally {
            propDefinedCalcInProgressProps.remove(prop);
        }
        return false;
    }

    public final boolean getPropHasOwnVal(final Id id) {
        return getPropHasOwnValImpl(id, true);
    }

    private boolean getPropHasOwnValImpl(final Id id, final boolean checkForInheritancePath) {
        if (id.getPrefix() == EDefinitionIdPrefix.ADS_USER_PROP) {
            return getUserPropHasOwnVal(id);
        } else {
            final RadPropDef prop = getRadMeta().getPropById(id);
            if (prop.getIsValInheritable() && (prop instanceof RadInnatePropDef || prop instanceof RadInnateRefPropDef || prop instanceof RadDetailPropDef || prop instanceof RadSqmlPropDef)) {
                if (prop.getValIsInheritMark(getArte(), getNativePropOwnVal(prop))) {
                    return checkForInheritancePath && getPropValInheritancePath(id) == null;//RADIX-6710
                } else {
                    return true;
                }
            } else {
                return true;
            }
        }
    }

    private boolean getUserPropHasOwnVal(final Id id) {
        state.assertReadAllowed();
        Boolean res;
        if (upHasOwnVal != null) {
            res = upHasOwnVal.get(id);
            if (res != null) {
                return res.booleanValue();
            }
        } else {
            upHasOwnVal = new HashMap<Id, Boolean>();
        }
        res = isInDatabase(false) ? readIsOwnUserPropVal(getUserPropDef(id)) : Boolean.FALSE;
        upHasOwnVal.put(id, res);
        return res.booleanValue();
    }

    public final void setPropHasOwnVal(final Id id, final boolean x) {
        setPropHasOwnValImpl(id, x, true);
    }

    public final void setPropHasOwnVal(final Id id, final boolean has, Object val) {
        setPropHasOwnValImpl(id, has, true);
        if (has) {
            setProp(id, val);
        }
    }

    private void setPropHasOwnValImpl(final Id id, final boolean x, final boolean checkForInheritancePath) {
        final boolean hadOwnVal = x ? getPropHasOwnValImpl(id, checkForInheritancePath) : false /*
                 * optimization
                 */;
        if (id.getPrefix() == EDefinitionIdPrefix.ADS_USER_PROP) {
            setUserPropHasOwnVal(id, x);
        } else {
            final RadPropDef prop = getRadMeta().getPropById(id);
            if (prop.getIsValInheritable() && !x) {
                Object valInheritMark = prop.getValInheritMarkVal(getArte());
                if (valInheritMark instanceof Pid) {
                    setNativeProp(id, createDummyInheritMarkEntity((Pid) valInheritMark, prop));
                } else {
                    setProp(id, valInheritMark);
                }
            }
        }
        if (x && !hadOwnVal) { //RADIX-1640
            final RadPropDef prop = getRadMeta().getPropById(id);
            setProp(id, prop.getInitVal(arte));
        }
    }

    private Entity createDummyInheritMarkEntity(final Pid inheritPid, final RadPropDef prop) {
        Entity valInheritMark = new Entity(inheritPid) {

            final DdsTableDef tableDef = Arte.get().getDefManager().getTableDef(((RadInnateRefPropDef) prop).getDestinationEntityId());

            @Override
            public RadClassDef getRadMeta() {
                throw new UnsupportedOperationException("Not supported for dummy entity");
            }

            @Override
            public DdsTableDef getDdsMeta() {
                return tableDef;
            }

            @Override
            public Object getProp(Id id) {
                DdsIndexDef.ColumnsInfo info = tableDef.getPrimaryKey().getColumnsInfo();
                for (int i = 0; i < info.size(); i++) {
                    if (info.get(i).getColumnId().equals(id)) {
                        return pid.getPkVals().get(i);
                    }
                }
                throw new IllegalStateException("Can't find " + id + " in primary key of " + tableDef.getQualifiedName());
            }

        };
        valInheritMark.state = new EntityState((Entity) valInheritMark);
        valInheritMark.state.set(EntityState.Enum.DISCARDED);
        return valInheritMark;
    }

    private void setUserPropHasOwnVal(final Id id, final boolean x) {
        state.assertWriteAllowed();
        if (isPersistenPropReadonly(id) && x != getUserPropHasOwnVal(id)) {
            throw new IllegalUsageError("Try to modify readonly property \"" + getRadMeta().getPropById(id).getName() + "\" (#" + id + ")");
        }
        if (id.getPrefix() == EDefinitionIdPrefix.ADS_USER_PROP) {
            if (upHasOwnVal == null) {
                upHasOwnVal = new HashMap<Id, Boolean>();
            }
            upHasOwnVal.put(id, Boolean.valueOf(x));
            setPropModified(id);
        }
    }

    private Boolean readIsOwnUserPropVal(final RadUserPropDef prop) {
        String sec = ETimingSection.RDX_ARTE_DB_QRY_EXEC.getValue();
        getArte().getProfiler().enterTimingSection(sec);
        try {
            return prop.isValueExist(this.getPid());
        } finally {
            getArte().getProfiler().leaveTimingSection(sec);
        }
    }

    private void readUserPropVal(final RadUserPropDef prop) {
        String sec = ETimingSection.RDX_ARTE_DB_QRY_EXEC.getValue();
        getArte().getProfiler().enterTimingSection(sec);
        try {
            loadedPropVals.putPropValById(prop.getId(), prop.getValue(this));
        } finally {
            getArte().getProfiler().leaveTimingSection(sec);
        }
    }

    private RadUserPropDef getUserPropDef(final Id id) {
        final RadPropDef prop = getRadMeta().getPropById(id);
        if (!(prop instanceof RadUserPropDef)) {
            throw new IllegalUsageError("Property \"" + prop.getName() + "\" (#" + id + ") is not user property");
        }
        return (RadUserPropDef) prop;
    }

    /**
     *
     * @param id
     * @return path to parent where value is defined, null - if value is not
     * defined
     */
    public final ValInheritancePath getPropValInheritancePath(final Id id) {
        state.assertReadAllowed();
        final RadPropDef prop = getRadMeta().getPropById(id);
        final String stepDescr = "getInheritancePath";
        if (count(inheritancePathCalcInProgressProps, prop) == MAX_INHERITANCE_ACCESS_ROUNDS) {
            throw new PropInheritanceCycleException("Inheritance path calculation cycle", this, prop, stepDescr);
        }
        inheritancePathCalcInProgressProps.add(prop);
        try {
            NEXT_PATH:
            for (RadPropDef.ValInheritancePath path : prop.getValInheritPathes()) {
                Entity parent = this;
                for (final Id refPropId : path.getRefPropIds()) {
                    parent = (Entity) parent.getProp(refPropId);
                    if (parent == null) {
                        continue NEXT_PATH;
                    }
                }
                if (parent.getIsPropValDefined(path.getDestPropId())) {
                    return path;
                }
            }
        } catch (PropInheritanceCycleException ex) {
            ex.appendStep(stepDescr, this, prop);
            throw ex;
        } finally {
            inheritancePathCalcInProgressProps.remove(prop);
        }
        return null;
    }

    public final Entity getInitSrc() {
        return initSrc;
    }

////////////////////////////////////////////////////////////////////////////////////////////
    private boolean isVirtualColumn(final Id propId) {
        return QuerySqlBuilder.ALL_ROLES_FIELD_COL_ID.equals(propId) || QuerySqlBuilder.ACS_COORDINATES_AS_STR_COL_ID.equals(propId);
    }

    public final void purgePropCache() {
        for (Id propId : loadedPropVals.asMap().keySet()) {
            if (!isVirtualColumn(propId)
                    && getRadMeta().getPropById(propId).getValType() == EValType.OBJECT
                    && getPropHasOwnVal(propId)) {
                try {
                    final Entity valEntity = (Entity) getProp(propId);
                    if (valEntity != null) {
                        if (valEntity.isInDatabase(false)) {
                            try {
                                valEntity.purgePropCache();
                            } catch (EntityObjectNotExistsError e) {
                                //object creation might be rollbacked
                                valEntity.discard();
                            }
                        } else {
                            valEntity.discard();
                        }
                    }
                } catch (EntityObjectNotExistsError e) {
                    //object creation might be rollbacked, see #RADIX-6761
                }
            }
        }
        modifiedPropIds.clear();
        loadedPropVals.clear();
        parentsPtrByRefCache.clear();
        curUserRoleIds = null;
        if (detailsByRef != null) {
            detailsByRef.clear();
        }
        if (upHasOwnVal != null) {
            upHasOwnVal.clear();
        }
        if (isInDatabase(false)) {
            setPid(pid);
        }
        state.onPurgeProps();
    }// Published methods
    private Entity initSrc = null;

    final void init(final InitializingEntityController initController) {
        final PropValHandlersByIdMap initialPropValsById = initController.getInitialPropVals();
        final Entity src = initController.getSrcEntity();
        final EEntityInitializationPhase phase = initController.getInitializationPhase();
        if (src != null && !getClass().isAssignableFrom(src.getClass())) {
            throw new IllegalUsageError("Can't create a copy. Can't cast source type \"" + src.getClass().getName() + "\" to this object type \"" + getClass().getName() + "\"");
        }
        if (state.isInited()) {
            throw new IllegalUsageError("Try to initialize twice");
        }
        if (!state.isNew()) {
            throw new IllegalUsageError("Try to initialize object with invalid state");
        }
        if (!beforeInit(initialPropValsById, src, phase)) {
            return;
        }

        initSrc = src;
        initInProcess = true;

        //initing details
        for (DdsReferenceDef detail : getRadMeta().getDetailsRefs()) {
            getDetailRef(detail);
        }

        for (RadPropDef prop : getRadMeta().getProps()) {
            if (!(prop.getAccessor() instanceof IRadPropWriteAccessor)) {
                continue;
            }
            if (prop instanceof RadParentPropDef
                    || prop instanceof RadInnateRefPropDef) {
                continue;
            }
            DdsColumnDef dbpProp = null;
            if (prop instanceof RadInnatePropDef) {
                dbpProp = getDdsMeta().getColumns().getById(prop.getId(), ExtendableDefinitions.EScope.ALL);
                if (dbpProp == getDdsMeta().findClassGuidColumn()) {
                    continue;// class guid inited separately
                }
            }
            final PropValHandler initPropVal = initialPropValsById.get(prop.getId());
            if (initPropVal != null) {
                if (initPropVal.getIsOwnValue()) {
                    setProp(prop.getId(), initPropVal.getValue());
                } else {
                    setPropHasOwnValImpl(prop.getId(), false, false);
                }
                //continue;
                //we have to mark value as modified in case it has default value and src has another value
                //to prevent the value to be copied from src by insert query
            }
            if (modifiedPropIds.contains(prop.getId())) {
                continue;
            }
            if (prop instanceof RadDetailParentRefPropDef) {
                continue;
            } else if (prop instanceof RadDetailPropDef) {
                final RadDetailPropDef detProp = (RadDetailPropDef) prop;
                final Entity detail = getDetailRef(detProp.getDetailReference());
                if (detail.modifiedPropIds.contains(detProp.getJoinedPropId())) {
                    continue;
                } else if (initPropVal != null) {
                    detail.modifiedPropIds.add(detProp.getJoinedPropId());
                    continue;
                }
                dbpProp = detail.getDdsMeta().getColumns().getById(detProp.getJoinedPropId(), ExtendableDefinitions.EScope.ALL);
            } else if (initPropVal != null) {
                modifiedPropIds.add(prop.getId());
                continue;
            }
            final Id seqId = dbpProp != null ? dbpProp.getSequenceId() : null;
            if (seqId != null) {
                final DdsSequenceDef seq = getArte().getDefManager().getSequenceDef(seqId);
                final long seqNextVal;
                getArte().getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
                try {
                    seqNextVal = getArte().getDefManager().getDbQueryBuilder().getSeqNextVal(seq);
                } finally {
                    getArte().getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
                }
                setProp(prop.getId(), seqNextVal);
            } else {
                if (src != null && initController.canCopyPropertyValue(prop.getId())) {
                    if (!(prop instanceof RadUserPropDef) || src.getUserPropHasOwnVal(prop.getId())) {
                        if (prop.getValType() != EValType.OBJECT) // do not copy object until create DBP-1549
                        {
                            copyPropVal(prop, src);
                        }
                    }
                } else if (!(prop instanceof RadUserPropDef || prop instanceof RadDynamicPropDef)) {
                    if (!prop.getIsValInheritable() || //RADIX-1640
                            prop.getInitPolicy() == EPropInitializationPolicy.DEFINE_ALWAYS //RADIX-1640
                            ) {
                        final Object propInitVal = prop.getInitVal(getArte());
                        setProp(prop.getId(), propInitVal);
                    }
                }
            }
        }

        //class
        if (getDdsMeta().findClassGuidColumn() != null) {
            setProp(getDdsMeta().getClassGuidColumn().getId(), getRadMeta().getId().toString());
        }

        for (RadPropDef prop : getRadMeta().getProps()) {//RADIX-1640
            if (!(prop.getAccessor() instanceof IRadPropWriteAccessor)) {
                continue;
            }
            if ((prop.getIsValInheritable() || prop instanceof RadUserPropDef)
                    && (prop.getInitPolicy() == EPropInitializationPolicy.DEFINE_ALWAYS && !getPropHasOwnValImpl(prop.getId(), false)
                    || prop.getInitPolicy() == EPropInitializationPolicy.DEFINE_IF_NOT_INHERITED && !getIsPropValDefinedImpl(prop.getId(), false))) {
                setPropHasOwnValImpl(prop.getId(), true, false);
            }
        }
        state.set(EntityState.Enum.INITED);
        initInProcess = false;
        if (detailsByRef != null) {
            for (EntityDetails detail : detailsByRef.values()) {
                detail.state.set(EntityState.Enum.INITED);
            }
        }
        afterInit(src, phase);
        if (src != null) {
            //should be done here so at the time create dialog will be 
            //shown in Explorer inheritance will alredy be restored
            restorePropInheritance(src);
        }
    }

    public final void init(PropValHandlersByIdMap initialPropValsById, final Entity src, final EEntityInitializationPhase phase) {
        final InitializingEntityController controller = new InitializingEntityController(phase);
        if (src != null) {
            controller.setSrcEntity(src);
        }
        if (initialPropValsById != null) {
            controller.setInitialPropVals(initialPropValsById);
        }
        init(controller);
    }

    private RadObjectUpValueRef upValRef = null;

    private void initUpValRef(final RadObjectUpValueRef ref) {
        if (!state.isNewOrInited()) {
            throw new IllegalUsageError("Only new object can be assigned to object user property.");
        }
        if (this.upValRef != null && !this.upValRef.equals(ref)) {
            throw new IllegalUsageError("Object is already assigned to other user property.");
        }
        this.upValRef = ref;

        for (RadPropDef prop : getRadMeta().getProps()) {
            if (!(prop.getAccessor() instanceof IRadPropWriteAccessor)) // const-?????? ????????
            {
                continue;
            }
            DdsColumnDef dbpProp = null;
            if (prop instanceof RadInnatePropDef) {
                dbpProp = getDdsMeta().getColumns().getById(prop.getId(), ExtendableDefinitions.EScope.ALL);
                if (dbpProp == getDdsMeta().findClassGuidColumn()) {
                    continue;// class guid inited separately
                }
            }
            if (upValRef != null && dbpProp != null && dbpProp.getExpression() == null) {
                final String dbName = dbpProp.getDbName();
                if (RadObjectUpValueRef.DbNotation.PROP_DEF_ID_DB_COL_NAME.equalsIgnoreCase(dbName)) {
                    setProp(prop.getId(), upValRef.getPropertyDefId().toString());
                    continue;
                } else if (RadObjectUpValueRef.DbNotation.VAL_OWNER_ENTITY_ID_DB_COL_NAME.equalsIgnoreCase(dbName)) {
                    if (upValRef.getValueOwner() != null) {
                        setProp(prop.getId(), upValRef.getValueOwner().getEntityId().toString());
                    } else {
                        setProp(prop.getId(), null);
                    }
                    continue;
                } else if (RadObjectUpValueRef.DbNotation.VAL_OWNER_PID_DB_COL_NAME.equalsIgnoreCase(dbName)) {
                    if (upValRef.getValueOwner() != null) {
                        setProp(prop.getId(), upValRef.getValueOwner().getPid().toString());
                    } else {
                        setProp(prop.getId(), null);
                    }
                    continue;
                }
            }
        }
    }

    private void restorePropInheritance(final Entity src) {
        for (RadPropDef prop : getRadMeta().getProps()) {
            if (prop.getIsValInheritable()) {
                final ValInheritancePath propValInheritancePath = src.getPropValInheritancePath(prop.getId());
                final boolean wasInherited = !src.getPropHasOwnVal(prop.getId()) && (propValInheritancePath != null);

                if (wasInherited) {
                    final RadPropDef refProp = getRadMeta().getPropById(propValInheritancePath.getRefPropIds().get(0));
                    if (propsAreEqual(prop.getValType(), getProp(prop.getId()), src.getProp(prop.getId()))
                            && propsAreEqual(refProp.getValType(), getProp(refProp.getId()), src.getProp(refProp.getId()))) {
                        setPropHasOwnVal(prop.getId(), false);
                    }
                }
            }
        }
    }

    /**
     * Deep comparison of prop values. Can be expensive for LOBs.
     */
    static boolean propsAreEqual(EValType valType, final Object val1, final Object val2) {
        if (val1 == null && val2 == null) {
            return true;
        }
        if (val1 == null || val2 == null) {
            return false;
        }
        if (valType == EValType.XML) {
            return ((XmlObject) val1).xmlText().equals(((XmlObject) val2).xmlText());
        }
        try {
            if (valType == EValType.CLOB) {
                return Arrays.equals(IOUtils.toCharArray(((Clob) val1).getCharacterStream()), IOUtils.toCharArray(((Clob) val2).getCharacterStream()));
            } else if (valType == EValType.BLOB) {
                return Arrays.equals(IOUtils.toByteArray(((Blob) val1).getBinaryStream()), IOUtils.toByteArray(((Blob) val2).getBinaryStream()));
            }
        } catch (IOException | SQLException ex) {
            throw new DatabaseError("Error on check is props are equal", ex);
        }
        if (valType == EValType.PARENT_REF) {
            return ((Entity) val1).getPid().equals(((Entity) val2).getPid());
        } else if (valType == EValType.ARR_REF) {
            return ((ArrEntity) val1).toArrStr().equals(((ArrEntity) val2).toArrStr());
        }
        return Objects.equals(val1, val2);
    }

    public final void create(Entity src) {
        setAutoUpdatePriority(0);
        state.assertDbWriteAllowed();

        if (!state.isInited()) {
            throw new IllegalUsageError(state.isNew() ? "Try to create not initialized object" : "Object has been already created or deleted");
        }
        if (src != null && !getClass().isAssignableFrom(src.getClass())) {
            throw new IllegalUsageError("Can't create a copy. Can't cast source type \"" + src.getClass().getName() + "\" to this object type \"" + getClass().getName() + "\"");
        }
        if (src == null) {
            src = initSrc;
        } else if (initSrc != null && src != initSrc) {
            throw new IllegalUsageError("Try to use different source objects in create and init.");		//class
        }
        if (getDdsMeta().findClassGuidColumn() != null) {
            setProp(getDdsMeta().getClassGuidColumn().getId(), getRadMeta().getId().toString());
        }
        if (!beforeCreate(src)) {
            discard();// to garantee object unregistration in newObjects pool
            return;
        }

        checkPkIsDefined();
        pid = new Pid(getArte(), getDdsMeta(), loadedPropVals.asMap());

        //saving innate properties
        final CreateObjectQuery q = getArte().getDefManager().getDbQueryBuilder().buildCreate(this, modifiedPropIds, src, upValRef != null);
        try {
            String sec = ETimingSection.RDX_ENTITY_DB_CREATE.getValue() + ":" + getRadMeta().getName();
            getArte().getProfiler().enterTimingSection(sec);
            try {
                q.create(this, loadedPropVals, src, upValRef);
            } finally {
                getArte().getProfiler().leaveTimingSection(sec);
            }
        } finally {
            q.free();
        }

        //saving userproperties
        for (RadPropDef prop : getRadMeta().getProps()) {
            if (!(prop instanceof RadUserPropDef)) {
                continue;
            }
            final RadUserPropDef upProp = (RadUserPropDef) prop;
            if (modifiedPropIds.contains(prop.getId())) {
                if (getUserPropHasOwnVal(prop.getId())) {
                    final PropOptional propOptional = loadedPropVals.getPropOptionalById(prop.getId());
                    if (propOptional.isPresent()) {
                        upProp.setValue(this, propOptional.get());
                    } else {
                        throw new RadixError("Value of modified property is lost", propOptional.cause(prop.getId()));
                    }
                }
            } else if (src != null && src.getUserPropHasOwnVal(prop.getId())) {
                copyPropVal(prop, src);
                getArte().getProfiler().enterTimingSection(ETimingSection.RDX_ENTITY_DB_CREATE);
                try {
                    final PropOptional propOptional = loadedPropVals.getPropOptionalById(prop.getId());
                    if (propOptional.isPresent()) {
                        upProp.setValue(this, propOptional.get());
                    } else {
                        throw new RadixError("Value of modified property is lost", propOptional.cause(prop.getId()));
                    }
                } finally {
                    getArte().getProfiler().leaveTimingSection(ETimingSection.RDX_ENTITY_DB_CREATE);
                }
            }
        }
        //DBP-1587 oracle clones LOBs on insert and update let's clear our handlers they are not actual any more
        loadedPropVals.removeModifiedLobs(modifiedPropIds);
        modifiedPropIds.clear();
        if (detailsByRef != null) {
            for (Map.Entry<DdsReferenceDef, EntityDetails> entry : detailsByRef.entrySet()) {
                final Entity detail = entry.getValue();
                detail.setParentRef(entry.getKey(), this);
                detail.create(src == null ? null : src.getDetailRef(entry.getKey()));
            }
            detailsByRef.clear();
        }
        state.set(EntityState.Enum.IN_DATABASE);
        if (!(this instanceof EntityDetails)) {
            Entity registeredInstance = getArte().getCache().getRegisteredExistingEntityObject(getPid());
            if (registeredInstance != null && !registeredInstance.state.isInDatabase()) {
                getArte().unregisterExistingEntityObject(registeredInstance);
            }
            getArte().registerExistingEntityObject(this);
        }
        curUserRoleIds = null;
        updateInnerObjects();
        afterCreate(src);
        creationDbTranSeqNum = getArte().getDbTransactionSeqNumber();
        creationSpNesting = getArte().getSavePointsNesting();
        getArte().afterCreate(this);
    }

    private List<Id> getVirtualColumnsToRead() {
        final List<Id> result = new ArrayList<>();
        if (state.getReadAcsCoords()) {
            result.add(QuerySqlBuilder.ACS_COORDINATES_AS_STR_COL_ID);
        }
        if (state.getReadRights()) {
            result.add(QuerySqlBuilder.ALL_ROLES_FIELD_COL_ID);
        }
        return result.isEmpty() ? null : result;
    }

    ;

    /**
     *
     * @param lockMode
     * @param waitTime - seconds to wait for lock (waitTime=0 - nowait,
     * waitTime=null - wait forever)
     */
    public final void read(final EEntityLockMode lockMode, final Long waitTime) {
        if (!state.isInDatabase()) {
            throw new IllegalUsageError("Can't read object which is not in database");
        }
        touch();
        boolean firstRead = !wasRead();
        String promisedClassGuid = null;
        if (firstRead && getRadMeta().getId().getPrefix() == EDefinitionIdPrefix.ADS_APPLICATION_CLASS) {
            final Id classGuidColId = getDdsMeta().getClassGuidColumn().getId();
            final PropOptional propOptional = loadedPropVals.getPropOptionalById(classGuidColId);
            if (propOptional.isPresent()) {
                promisedClassGuid = (String) propOptional.get();
            } else {
                promisedClassGuid = null;
            }
            if (promisedClassGuid != null) {
                final RadixObjects<ColumnInfo> pkMeta = getDdsMeta().getPrimaryKey().getColumnsInfo();
                boolean classGuidIsInPk = false;
                for (int i = 0; i < pkMeta.size(); i++) {
                    if (pkMeta.get(i).getColumnId().equals(classGuidColId)) {
                        classGuidIsInPk = true;
                        break;
                    }
                }
                if (!classGuidIsInPk) {
                    loadedPropVals.removePropValById(getDdsMeta().getClassGuidColumn().getId());
                }
            }
        }

        final boolean isLocking = lockMode == EEntityLockMode.SESSION || lockMode == EEntityLockMode.TRANSACTION;
        final RadixConnection conn = isLocking ? (RadixConnection) getArte().getDbConnection().get() : null;
        if (conn != null) {
            conn.setOperationDescription("Reading and locking object with PID " + pid.getEntityId() + "->" + pid.toString());
        }
        if (lockMode == EEntityLockMode.SESSION) {
            lock(lockMode, waitTime);
            //else if (lockMode == EEntityLockMode.TRANSACTION) - select "for update" will be constructed
        }
        final ReadObjectQuery q = getArte().getDefManager().getDbQueryBuilder().buildRead(this, getVirtualColumnsToRead(), (lockMode == EEntityLockMode.TRANSACTION), waitTime);
        if (q != null) {
            try {
                try {
                    q.read(pid, loadedPropVals);
                } finally {
                    if (conn != null) {
                        conn.setOperationDescription(null);
                    }
                    q.free();
                }
                if (lockMode == EEntityLockMode.TRANSACTION) {
                    getArte().getTransactionLock().registerLock(pid);
                }
                state.afterRead();
            } catch (EntityObjectNotExistsError e) {
                dbObjDoesNotExistsAnyMore();
                throw e;
            }
        }
        if (promisedClassGuid != null) {
            final PropOptional propOptional = loadedPropVals.getPropOptionalById(getDdsMeta().getClassGuidColumn().getId());
            final Object actualClassGuid;
            if (propOptional.isPresent()) {
                actualClassGuid = propOptional.get();
            } else {
                actualClassGuid = null;
            }
            if (!Objects.equals(actualClassGuid, promisedClassGuid)) {
                discard();
                throw new IllegalStateException("Actual classGuid in DB '" + actualClassGuid + "' differs from classGuid supplied during entity creation: '" + promisedClassGuid + "'");
            }
        }
        afterRead();
    }

    /**
     *
     * @param lockMode
     * @param waitTime - seconds to wait for lock (waitTime=0 - nowait,
     * waitTime=null - wait forever)
     */
    public final void reread(final EEntityLockMode lockMode, final Long waitTime) {
        purgePropCache();
        read(lockMode, waitTime);
    }

    public final String calcTitle() {
        return calcTitle(getPresentationMeta().getDefaultObjectTitleFormat());
    }

    public final String calcTitle(final RadEntityTitleFormatDef titleFormat) {
        if (titleFormat == null || titleFormat.getFormat() == null) {
            return onCalcTitle("");
        }
        final StringBuilder res = new StringBuilder();
        for (RadEntityTitleFormatDef.TitleItem titleItem : titleFormat.getFormat()) {
            final Id propId = titleItem.getPropId();
            final Id enumId = getRadMeta().getPropById(propId).getEnumId();
            Object val = getProp(propId);
            if (enumId != null && val instanceof Comparable) { //for enums item title is used
                final RadEnumDef enumDef = getArte().getDefManager().getEnumDef(enumId);
                val = enumDef.getItemByVal((Comparable) val).getTitle(getArte());
            } else if (val instanceof Entity) { // for object refs default title is used
                final Entity refEnt = (Entity) val;
                val = onCalcParentTitle(propId, refEnt, refEnt.calcTitle());
            }
            String defaulValue = null;
            if (val == null) { // replace null strings by empty strings;
                final RadPropDef propDef = getRadMeta().getPropById(propId);
                switch (propDef.getValType()) {
                    case STR:
                    case CHAR:
                    case CLOB:
                    case XML:
                        defaulValue = "";
                        break;
                }
            }
            res.append(titleItem.format(getArte(), val, defaulValue));
        }

        if (titleFormat.getDefinitionContextType() == RadEntityTitleFormatDef.DefinitionContextType.PROPERTY) {
            return res.toString();
        }

        return onCalcTitle(res.toString());
    }

    public final void setIsAutoUpdateEnabled(final boolean isEnabled) {
        state.setIsAutoUpdateEnabled(isEnabled);
    }

    public final boolean autoUpdate(final EAutoUpdateReason r) {
        if (!state.isInDatabase() && !state.isNewOrInited()) {
            return false;
        }
        if (state.isInDatabase() && !isModified()) //no modifications
        {
            return false;
        }
        if (!state.isAutoUpdateEnabled()) {
            return false;
        }
        if (upValRef != null) //owner must be updated first
        {
            upValRef.getValueOwner().autoUpdate(r);
        }
        if (beforeAutoUpdate(r)) {
            if (state.isInDatabase()) {
                update();
            } else {
                create(null);
            }
        }
        return true;
    }

    protected boolean isAfterCommitRequired() {
        return false;
    }

    public final void update() {
        setAutoUpdatePriority(0);
        if (!state.isInDatabase()) {
            throw new IllegalUsageError("Can't update object which is not in database");
        }
        state.assertDbWriteAllowed();
        Entity owner = null;
        OwnerProp ownerProp = null;
        if (getDdsMeta().getExtOptions().contains(EDdsTableExtOption.USE_AS_USER_PROPERTIES_OBJECT)) {
            final UpObjectOwnerQuery qry = getArte().getDefManager().getDbQueryBuilder().buildUpObjectOwnerQuery(getDdsMeta());
            if (qry != null) {
                try {
                    ownerProp = qry.getUpObjectOwner(pid);
                    owner = getArte().getEntityObject(ownerProp.ownerPid);
                } finally {
                    qry.free();
                }
            }
            if (owner != null && ownerProp != null && !owner.beforeUpdatePropObject(ownerProp.propId, this)) {
                return;
            }
        }
        if (!beforeUpdate()) {
            return;
        }
        if (!isModified()) {
            return;
        }
        touch();
        try { //saving innate properties
            final UpdateObjectQuery qry = getArte().getDefManager().getDbQueryBuilder().buildUpdate(this, modifiedPropIds);
            if (qry != null) {
                try {
                    String sec = ETimingSection.RDX_ENTITY_DB_UPDATE.getValue() + ":" + getRadMeta().getName();
                    getArte().getProfiler().enterTimingSection(sec);
                    try {
                        qry.update(this, loadedPropVals);
                    } finally {
                        getArte().getProfiler().leaveTimingSection(sec);
                    }
                } finally {
                    qry.free();
                }
            }
        } catch (EntityObjectNotExistsError e) {
            dbObjDoesNotExistsAnyMore();
            throw e;
        }
        //saving user properties
        for (Id propId : modifiedPropIds) {
            if (propId.getPrefix() == EDefinitionIdPrefix.ADS_USER_PROP) {
                final RadUserPropDef prop = ((RadUserPropDef) getRadMeta().getPropById(propId));
                String sec = ETimingSection.RDX_ENTITY_DB_UPDATE.getValue() + ":" + getRadMeta().getName() + "." + prop.getName();
                if (getUserPropHasOwnVal(propId)) {
                    getArte().getProfiler().enterTimingSection(sec);
                    try {
                        if (prop.getValType() == EValType.OBJECT) { //TWRBS-1509
                            //to remove old object if it exists
                            prop.delValue(pid);
                        }
                        final PropOptional propOptional = loadedPropVals.getPropOptionalById(propId);
                        if (propOptional.isPresent()) {
                            prop.setValue(this, propOptional.get());
                        } else {
                            throw new RadixError("Value of modified property is lost", propOptional.cause(propId));
                        }
                    } finally {
                        getArte().getProfiler().leaveTimingSection(sec);
                    }
                } else {
                    getArte().getProfiler().enterTimingSection(sec);
                    try {
                        prop.delValue(getPid());
                    } finally {
                        getArte().getProfiler().leaveTimingSection(sec);
                    }
                }
            }
        }
        //DBP-1587 oracle clones LOBs on insert and update let's clear our handlers they are not actual any more
        loadedPropVals.removeModifiedLobs(modifiedPropIds);
        lastModifiedPropIds.clear();
        lastModifiedPropIds.addAll(modifiedPropIds);
        modifiedPropIds.clear();
        if (detailsByRef != null) {
            for (EntityDetails detail : detailsByRef.values()) {
                detail.update();
            }
            detailsByRef.clear();
        }
        updateInnerObjects();
        afterUpdate();
        if (owner != null && ownerProp != null) {
            owner.afterUpdatePropObject(ownerProp.propId, this);
        }
        getArte().afterUpdate(this);
    }

    private void updateInnerObjects() {
        for (RadPropDef prop : getRadMeta().getProps()) {
            if (prop.getValType() == EValType.OBJECT) {
                if (!getPropHasOwnVal(prop.getId())) {
                    continue;
                }
                final Entity obj = (Entity) getProp(prop.getId());
                if (obj == null) {
                    continue;
                }
                if (obj.isInDatabase(false)) {
                    if (obj.isModified()) {
                        obj.update();
                    }
                } else {
                    obj.create(null);
                }
            }
        }
    }

    public boolean isPersistentPropModified(final Id propId) {
        final RadPropDef propDef = getRadMeta().getPropById(propId);
        if (propDef == null) {
            throw new RadixError("There is no property #" + propId + " in entity class #" + getRadMeta().getId());
        }
        if (propDef instanceof RadInnatePropDef
                || propDef instanceof RadInnateRefPropDef
                || propDef instanceof RadDetailPropDef
                || propDef instanceof RadUserPropDef) {
            return getPersistentModifiedPropIds().contains(propId);
        }
        throw new RadixError("Check for modification is supported only for column-based properties, user properties, parent references and detail properties. Property #" + propId + " has the " + propDef.getClass().getName() + " type");
    }

    public final boolean isModified() {
        if (!modifiedPropIds.isEmpty()) {
            return true;
        }
        if (detailsByRef != null) {
            for (EntityDetails detail : detailsByRef.values()) {
                if (detail.isModified()) {
                    return true;
                }
            }
        }
        return false;
    }

    public final void delete() {
        state.assertDbWriteAllowed();
        if (!state.isInDatabase() && !state.isNewOrInited()) {
            throw new IllegalUsageError("Can't delete object which is not in database");
        }
        final boolean wasExistingObj = isInDatabase(false);
        if (wasExistingObj && !beforeDelete()) {
            return;
        }
        if (wasExistingObj) {
            final DeleteObjectQuery q = getArte().getDefManager().getDbQueryBuilder().buildDeleteQuery(getDdsMeta());
            try {
                getArte().getProfiler().enterTimingSection(ETimingSection.RDX_ENTITY_DB_DELETE);
                try {
                    q.delete(pid);
                } finally {
                    getArte().getProfiler().leaveTimingSection(ETimingSection.RDX_ENTITY_DB_DELETE);
                }
            } finally {
                q.free();
            }
            dbObjDoesNotExistsAnyMore();
        } else {
            if (!(this instanceof EntityDetails)) {
                getArte().unregisterNewEntityObject(this);
            }
            state.set(EntityState.Enum.DELETED);
        }
        if (wasExistingObj) {
            //FAQ: called after status "is spoiled" to put in the same conditions
            // situation when read has been done before delete and
            // when read hasn't been done before in:
            // read is not allowed.
            afterDelete();
            getArte().afterDelete(this);
        }
    }

    public final void discard() {
        if (state.isInDatabase()) {
            getArte().unregisterExistingEntityObject(this);
        } else if (state.isNewOrInited()) {
            getArte().unregisterNewEntityObject(this);
            discardLinkedPropObjects();
        }
        state.set(EntityState.Enum.DISCARDED);
    }

    public final void markAsDeleted() {
        if (state.isInDatabase()) {
            dbObjDoesNotExistsAnyMore();
        } else if (state.isNewOrInited()) {
            arte.unregisterNewEntityObject(this);
            state.set(EntityState.Enum.DELETED);
        }
        arte.unregisterFromAfterCommit(this);
    }

    private void discardLinkedPropObjects() {
        //discard user prop objects userproperties
        for (RadPropDef prop : getRadMeta().getProps()) {
            if (prop.getValType() == EValType.OBJECT && prop instanceof RadUserPropDef && getPropHasOwnVal(prop.getId())) {
                Object val = getUserProp(prop.getId());
                if (val instanceof Entity) {
                    Entity userPropVal = (Entity) val;
                    if (userPropVal.isNewObject()) {
                        userPropVal.discard();
                    }
                }
            }
        }
    }

    /**
     * Calls select for update
     *
     * @param waitTime - seconds to wait for lock (waitTime=0 - nowait,
     * waitTime=null - wait forever)
     */
    public final void lock(final EEntityLockMode mode, final Long waitTimeSec) {
        if (mode == EEntityLockMode.NONE) {
            return;
        }
        if (!state.isInDatabase()) {
            throw new IllegalUsageError("Can't lock object which is not in database");
        }
        if (mode == EEntityLockMode.TRANSACTION) {
            if (isLocked(mode)) {
                return;
            }
            final TranLockObjectQuery q = getArte().getDefManager().getDbQueryBuilder().buildTranLockObjectQuery(getDdsMeta(), waitTimeSec);
            try {
                getArte().getProfiler().enterTimingSection(ETimingSection.RDX_ENTITY_DB_LOCK);
                try {
                    q.lock(pid);
                } finally {
                    getArte().getProfiler().leaveTimingSection(ETimingSection.RDX_ENTITY_DB_LOCK);
                }
                getArte().getTransactionLock().registerLock(pid);
            } finally {
                q.free();
            }
        } else if (mode == EEntityLockMode.SESSION) {
            getArte().getProfiler().enterTimingSection(ETimingSection.RDX_ENTITY_DB_LOCK);
            try {
                getArte().getSessionLock().lock(pid, waitTimeSec);
            } finally {
                getArte().getProfiler().leaveTimingSection(ETimingSection.RDX_ENTITY_DB_LOCK);
            }
        } else {
            throw new IllegalArgumentError("Unsupported lock mode: \"" + mode.getName() + "\", in Entity::lock()");
        }
    }

    public final boolean isLocked(final EEntityLockMode mode) {
        if (!state.isInDatabase()) {
            return false;
        }
        if (mode == EEntityLockMode.SESSION) {
            return getArte().getSessionLock().isLocked(pid);
        }
        if (mode == EEntityLockMode.TRANSACTION) {
            return getArte().getTransactionLock().isLocked(pid);
        }
        return false;
    }

    public final void releaseSessionLock() {
        if (pid != null) {
            getArte().getProfiler().enterTimingSection(ETimingSection.RDX_ENTITY_DB_LOCK);
            try {
                getArte().getSessionLock().releaseLock(pid);
            } finally {
                getArte().getProfiler().leaveTimingSection(ETimingSection.RDX_ENTITY_DB_LOCK);
            }
        }
    }

    @Override
    public String toString() {
        return getClass().getName() + "[PID = " + String.valueOf(pid) + "]";
    }

    public Collection<Id> getLastOwnModifiedPropIds() {
        return new ArrayList<>(lastModifiedPropIds);
    }

    /**
     * Modified native, detail, parentRef and user property IDs
     *
     * @return
     */
    public Collection<Id> getPersistentModifiedPropIds() {
        // return Collections.unmodifiableCollection(modifiedPropIds);

        // to resoleve DBP-1539 return all modified props because setParentRef can be overriden
        // let's return detail and reference properties
        final List<Id> propIds = new ArrayList<Id>(modifiedPropIds);
        if (detailsByRef != null) {//let's add all modified detail properties
            for (Map.Entry<DdsReferenceDef, EntityDetails> det : detailsByRef.entrySet()) {
                if (det.getValue().isModified()) {
                    for (Id detPropId : det.getValue().getPersistentModifiedPropIds()) {
                        propIds.add(getArte().getDefManager().getMasterPropIdByDetailPropId(det.getKey(), getRadMeta(), detPropId));
                    }
                }
            }
        }

        //let's add all modified ParentRefs and DetailParentRefs
        for (RadPropDef p : getRadMeta().getProps()) {
            if (p instanceof RadInnateRefPropDef) {
                final DdsReferenceDef ref = ((RadInnateRefPropDef) p).getReference();
                for (DdsReferenceDef.ColumnsInfoItem refProp : ref.getColumnsInfo()) {
                    if (modifiedPropIds.contains(refProp.getChildColumnId())) {
                        propIds.add(p.getId());
                        continue;
                    }
                }
            } else if (p instanceof RadDetailParentRefPropDef && detailsByRef != null) {
                final RadDetailParentRefPropDef detPt = (RadDetailParentRefPropDef) p;
                final EntityDetails det = detailsByRef.get(detPt.getDetailReference());
                if (det != null && det.isModified()) {
                    final Collection<Id> detModifPropIds = det.getPersistentModifiedPropIds();
                    final DdsReferenceDef ref = detPt.getReference();
                    for (DdsReferenceDef.ColumnsInfoItem refProp : ref.getColumnsInfo()) {
                        if (detModifPropIds.contains(refProp.getChildColumnId())) {
                            propIds.add(p.getId());
                            continue;
                        }
                    }
                }
            }
        }
        return Collections.unmodifiableCollection(propIds);
    }

    /**
     * Try not to delete object from ARTE cache on the end of transaction during
     * specified time. ARTE can remove this object from cache before time is
     * over due to its Cache implementation (for exemple during rollback).
     *
     * @param maxAgeInSeconds - if null then try to keep in cache forever
     */
    public final void keepInCache(final Long maxAgeInSeconds) {
        state.setKeepInCache(maxAgeInSeconds);
    }

    /**
     *
     * @return true if object can be removed from cache by timeout
     * @deprecated Use version with explicit current time for better performance
     * when checking multiple objects
     */
    @Deprecated
    public final boolean getCanBeRemovedFromCache() {
        return getCanBeRemovedFromCache(System.currentTimeMillis());
    }

    public final boolean getCanBeRemovedFromCache(final long curTimeMillis) {
        return state.getCanBeRemovedFromCache(curTimeMillis);
    }
//trace 

    protected void trace(final EEventSeverity severity, final String mess) {
        getArte().getTrace().put(severity, mess, EEventSource.APP_CLASS);
    }

//Private  methods
    private void setPid(final Pid pid) {
        this.pid = pid;
        final RadixObjects<ColumnInfo> pkMeta = getDdsMeta().getPrimaryKey().getColumnsInfo();
        for (int i = 0; i < pkMeta.size(); i++) {
            loadedPropVals.putPropValById(pkMeta.get(i).getColumnId(), pid.getPkVals().get(i));
        }
    }

    final void setPropModified(final Id id) {
        modifiedPropIds.add(id);
    }

    private void dbObjDoesNotExistsAnyMore() {
        if (!state.isInDatabase()) {
            throw new IllegalUsageError("Object has been already deleted from database");
        }
        state.set(EntityState.Enum.DELETED);
        getArte().unregisterExistingEntityObject(this);
    }	//DRC
    private List<Id> curUserRoleIds = null;

    /**
     * Roles that are granted to current user for this object ACS coordinates.
     * If empty access is forbidden.
     *
     * @return List of role IDs.
     */
    public final List<Id> getCurUserApplicableRoleIds() {
        if (curUserRoleIds == null) {
            state.assertReadAllowed();
            if (isInDatabase(false)) {
                PropOptional propOptional = loadedPropVals.getPropOptionalById(QuerySqlBuilder.ALL_ROLES_FIELD_COL_ID);
                String roleIds;
                if (propOptional.isPresent()) {
                    roleIds = (String) propOptional.get();
                } else {
                    state.setReadRights(true);
                    read(EEntityLockMode.NONE, null);
                    propOptional = loadedPropVals.getPropOptionalById(QuerySqlBuilder.ALL_ROLES_FIELD_COL_ID);
                    if (propOptional.isPresent()) {
                        roleIds = (String) propOptional.get();
                    } else {
                        throw new DbQueryBuilderError("Current user applicable roles requested but not loaded", propOptional.cause(QuerySqlBuilder.ALL_ROLES_FIELD_COL_ID));
                    }
                }
                final List<Id> arrIdRoleIds = new ArrayList<Id>(10);
                if (roleIds != null && roleIds.length() != 0) {
                    for (String idStr : roleIds.split(",")) {
                        arrIdRoleIds.add(Id.Factory.loadFrom(idStr));
                    }
                }
                curUserRoleIds = Collections.unmodifiableList(arrIdRoleIds);
            } else {
                curUserRoleIds = getArte().getRights().getCurUserAllRolesInAllAreas();
            }
        }
        return curUserRoleIds;
    }

    public final String getAcsCoordinatesAsString() {
        state.assertReadAllowed();
        if (isInDatabase(false)) {
            PropOptional propOptional = loadedPropVals.getPropOptionalById(QuerySqlBuilder.ACS_COORDINATES_AS_STR_COL_ID);
            String acsCoordinatesStr;
            if (propOptional.isPresent()) {
                acsCoordinatesStr = (String) propOptional.get();
            } else {
                state.setReadAcsCoords(true);
                read(EEntityLockMode.NONE, null);
                propOptional = loadedPropVals.getPropOptionalById(QuerySqlBuilder.ACS_COORDINATES_AS_STR_COL_ID);
                if (propOptional.isPresent()) {
                    acsCoordinatesStr = (String) propOptional.get();
                } else {
                    throw new DbQueryBuilderError("Acs coordinates were not loaded", propOptional.cause(QuerySqlBuilder.ACS_COORDINATES_AS_STR_COL_ID));
                }
            }
            return acsCoordinatesStr;
        } else {
            throw new DatabaseError("Unable to get acs coordinates for object wich is not in database", null);
        }
    }

    public final void setReadRights(final boolean readRights) {
        state.setReadRights(readRights);
    }

    protected final void setState(final EntityState.Enum st) {
        state.set(st);
    }
    private static final Id AUDIT_SCHEME_TAB_ID = Id.Factory.loadFrom("tblJBWGL34ATTNBDPK5ABQJO5ADDQ");
    private static final int MAX_AUDIT_REF_PATH_LEGTH = 100;

    public String getAuditSchemeId() {
        if (getAuditReferenceDef() != null) {
            Entity parent = getParentRef(auditReferenceDef);
            int cnt = 1;
            while (parent != null && parent.getAuditReferenceDef() != null) {
                if (cnt > MAX_AUDIT_REF_PATH_LEGTH) {
                    getArte().getTrace().put(EEventSeverity.ERROR, "Can't determinate audit scheme for " + toString() + ".\nAudit references path legnth limit exceeded:\n" + String.valueOf(cnt) + " references were processed.", EEventSource.ARTE);
                    return null;
                }
                parent = parent.getParentRef(parent.auditReferenceDef);
                cnt++;
            }
            if (parent != null && AUDIT_SCHEME_TAB_ID.equals(parent.getDdsMeta().getId())) {
                return parent.getPid().getPkVals().get(0).toString();
            }
        }
        return null;
    }
    private DdsReferenceDef auditReferenceDef = null;

    private DdsReferenceDef getAuditReferenceDef() {
        final DdsTableDef tab = getDdsMeta();
        if (auditReferenceDef != null) {
            return auditReferenceDef;
        }
        if (tab.getAuditInfo() != null && tab.getAuditInfo().getAuditReferenceId() != null) {
            auditReferenceDef = getArte().getDefManager().getReferenceDef(tab.getAuditInfo().getAuditReferenceId());
        }
        return auditReferenceDef;
    }
    private Set<Id> readonlyPersistentPropIds = null;

    public final void setPersistenPropIsReadonly(final Id id, final boolean isReadonly) {
        final RadPropDef dacProp = getRadMeta().getPropById(id);
        if (dacProp instanceof RadDetailPropDef) {
            final RadDetailPropDef detProp = (RadDetailPropDef) dacProp;
            final Entity detail = getDetailRef(detProp.getDetailReference());
            detail.setPersistenPropIsReadonly(detProp.getJoinedPropId(), isReadonly);
        } else {
            if (isReadonly) {
                if (readonlyPersistentPropIds == null) {
                    readonlyPersistentPropIds = new HashSet<Id>();
                }
                readonlyPersistentPropIds.add(id);
            } else if (readonlyPersistentPropIds != null) {
                readonlyPersistentPropIds.remove(id);
            }
        }
    }

    private boolean isPersistenPropReadonly(final Id id) {
        return readonlyPersistentPropIds != null && readonlyPersistentPropIds.contains(id);
    }

    public final boolean isDiscarded() {
        return state.isDiscarded();
    }

    public final boolean isDeleted() {
        return state.isDeleted();
    }

    public final boolean isInited() {
        return state.isInited();
    }

    public final boolean isNewObject() {
        return state.isNewOrInited();
    }

    public boolean isCreatedAfterSavePoint(final String spId) { //see RADIX-4942
        if (creationDbTranSeqNum == null
                || creationDbTranSeqNum.longValue() != getArte().getDbTransactionSeqNumber()) {
            return false;
        }
        if (spId == null) {
            return true; //created in this tran no matter when
        }
        if (creationSpNesting == null) {
            return false; //created before any savepoint was set
        }
        final long spNesting = getArte().calcSpNesting(spId);
        return creationSpNesting.longValue() >= spNesting;
    }

    public boolean isInstantiatedAfterSavePoint(final String spId) { //see RADIX-4942
        if (spId == null) {
            return true; //instantiated in this tran no matter when
        }
        if (instSpNesting == null) {
            return false; //instantiated before any savepoint was set
        }
        final long spNesting = getArte().calcSpNesting(spId);
        return instSpNesting.longValue() >= spNesting;
    }

    public boolean autonomousStoreAfterRollback(final String savePointId) {
        if (onAutonomousStoreAfterRollback(savePointId)) {
            checkPkIsDefined();
            pid = new Pid(getArte(), getDdsMeta(), loadedPropVals.asMap());
            state.set(EntityState.Enum.IN_DATABASE);
            creationSpNesting = null;
            return true;
        } else {
            return false;
        }
    }

    public void copyPropVal(final RadPropDef prop, final Entity src) {
        Object val = src.getProp(prop.getId());
        if (val != null) {
            if (prop.getValType() == EValType.OBJECT) {//if valType is object then copy it
                final Entity srcObj = (Entity) val;
                final Entity obj = (Entity) getArte().newObject(srcObj.getRadMeta().getId());
                obj.init(null, srcObj, EEntityInitializationPhase.PROGRAMMED_CREATION);
                obj.initUpValRef(new RadObjectUpValueRef(this, prop.getId()));
                val = obj;
            } else if (prop.getValType() == EValType.XML) {
                val = ((XmlObject) val).copy();
            } else if (val instanceof Arr) {
                try {
                    try {
                        val = val.getClass().getConstructor(Collection.class).newInstance(val);
                    } catch (NoSuchMethodException ex) {
                        //some arrays require ARTE as a first constructor parameter
                        val = val.getClass().getConstructor(Arte.class, Collection.class).newInstance(getArte(), val);
                    }
                } catch (Exception ex) {
                    throw new RuntimeException("Error while copying array #" + prop.getId(), ex);
                }
            }
            try {
                if (prop.getValType() == EValType.CLOB) {
                    final Clob newVal = arte.getDbConnection().createTemporaryClob();
                    try (Writer writer = newVal.setCharacterStream(1); Reader reader = ((Clob) val).getCharacterStream()) {
                        IOUtils.copy(reader, writer);
                    }
                    val = newVal;
                } else if (prop.getValType() == EValType.BLOB) {
                    final Blob newVal = arte.getDbConnection().createTemporaryBlob();
                    try (OutputStream os = newVal.setBinaryStream(1); InputStream is = ((Blob) val).getBinaryStream()) {
                        IOUtils.copy(is, os);
                    }
                    val = newVal;
                }
            } catch (SQLException | IOException ex) {
                throw new DatabaseError("Error while copying lob #" + prop.getId(), ex);
            }
        }
        setProp(prop.getId(), val);
    }

    public void touch() {
        if (entityTouchListener != null) {
            entityTouchListener.onTouch(this);
        }
    }

    public long getTouchTimeMillis() {
        return touchTimeMillis;
    }

    public void purgePropCacheOnRollback(final String savepointId) {
        purgePropCache();
    }

    public Entity getDescriptiveOwner() {
        return null;
    }

    public String calcDescriptiveTitle() {
        final Entity descriptiveOwner = getDescriptiveOwner();
        if (descriptiveOwner != null) {
            return descriptiveOwner.calcDescriptiveTitle() + " / " + calcTitle();
        }
        return calcTitle();
    }

    private static int count(List list, Object obj) {
        if (list == null) {
            return 0;
        }
        int result = 0;
        for (Object o : list) {
            if (o == obj) {
                result++;
            }
        }
        return result;
    }

    public static class ErrorOnEntityCreation extends RadixError {

        public ErrorOnEntityCreation(String mess, Throwable cause) {
            super(mess, cause);
        }

        public ErrorOnEntityCreation(String mess) {
            super(mess);
        }
    }

    public static class TouchListenerSetter {

        public static void setTouchListener(final Entity entity, final IEntityTouchListener listener) {
            entity.entityTouchListener = listener;
        }
    }
}
