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
package org.radixware.kernel.server.types;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef.ColumnsInfoItem;

import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EDeleteMode;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.EPaginationMethod;
import org.radixware.kernel.common.enums.ESelectionMode;
import org.radixware.kernel.common.enums.ETimingSection;
import org.radixware.kernel.common.exceptions.AppException;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.server.arte.DefManager;
import org.radixware.kernel.server.sqml.Sqml;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.arte.AdsClassLoader;
import org.radixware.kernel.server.dbq.DbQuery;
import org.radixware.kernel.server.dbq.DeleteGroupQuery;
import org.radixware.kernel.server.dbq.PkSubQuery;
import org.radixware.kernel.server.dbq.QueryBuilderDelegate;
import org.radixware.kernel.server.dbq.UpdateGroupQuery;
import org.radixware.kernel.server.exceptions.DatabaseError;
import org.radixware.kernel.server.exceptions.DeleteCascadeConfirmationRequiredException;
import org.radixware.kernel.server.exceptions.DeleteCascadeRestrictedException;
import org.radixware.kernel.server.exceptions.FilterParamNotDefinedException;
import org.radixware.kernel.server.meta.clazzes.IRadPropReadAccessor;
import org.radixware.kernel.server.meta.clazzes.IRadPropWriteAccessor;
import org.radixware.kernel.server.meta.clazzes.IRadRefPropertyDef;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.meta.clazzes.RadPropDef;
import org.radixware.kernel.server.meta.presentations.RadConditionDef;
import org.radixware.kernel.server.meta.presentations.RadClassPresentationDef;
import org.radixware.kernel.server.meta.presentations.RadExplorerItemDef;
import org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef;
import org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef;
import org.radixware.kernel.server.meta.presentations.RadSortingDef;

public abstract class EntityGroup<T extends Entity> implements Iterable<T> {

    public static interface EntityFilter<T extends Entity> {

        boolean omitEntity(T entity);
    }

    public static abstract class Context {

        final RadClassDef selectionClassDef;

        public Context(final RadClassDef selClassDef) {
            this.selectionClassDef = selClassDef;
        }

        public RadClassDef getSelectionClassDef() {
            return selectionClassDef;
        }
    }

    public static final class EmptyContext extends Context {

        public EmptyContext(final RadClassDef selClassDef) {
            super(selClassDef);
        }
    }

    public static final class TreeContext extends Context {

        final RadExplorerItemDef explorerItem;
        final Pid parentPid;

        public TreeContext(
                final RadClassDef selClassDef,
                final RadExplorerItemDef explorerItem,
                final Pid parentPid) {
            super(selClassDef);
            this.explorerItem = explorerItem;
            this.parentPid = parentPid;
        }

        public final RadExplorerItemDef getExplorerItem() {
            return explorerItem;
        }

        public final Pid getParentPid() {
            return parentPid;
        }
    }

    public static final class PropContext extends Context {

        final IRadRefPropertyDef parentTitleProp;
        final RadClassPresentationDef parentTitlePropClassPres;
        final RadParentTitlePropertyPresentationDef parentTitlePropPres;
        final IRadClassInstance childInst;
        final List<DdsReferenceDef.ColumnsInfoItem> fixedParentRefProps;

        public PropContext(
                final RadClassDef selClassDef,
                final IRadRefPropertyDef parentTitleProp,
                final RadClassPresentationDef parentTitlePropClassPres,
                final RadParentTitlePropertyPresentationDef parentTitlePropPres,
                final IRadClassInstance childInst,
                final List<ColumnsInfoItem> fixedParentRefProps) {
            super(selClassDef);
            this.parentTitleProp = parentTitleProp;
            this.parentTitlePropClassPres = parentTitlePropClassPres;
            this.parentTitlePropPres = parentTitlePropPres;
            this.childInst = childInst;
            if (fixedParentRefProps != null) {
                this.fixedParentRefProps = Collections.unmodifiableList(fixedParentRefProps);
            } else {
                this.fixedParentRefProps = Collections.emptyList();
            }
        }

        public IRadRefPropertyDef getParentRefProp() {
            return parentTitleProp;
        }

        public Id getParentRefPresPropId() {
            return parentTitlePropPres.getPropId();
        }

        public RadConditionDef getParentSelectCondition() {
            return parentTitlePropPres.getParentSelectCondition(parentTitlePropClassPres);
        }

        public final List<DdsReferenceDef.ColumnsInfoItem> getFixedParentRefProps() {
            return fixedParentRefProps;
        }

        public final IRadClassInstance getPropOwner() {
            if (childInst == null) {
                throw new IllegalUsageError("Can't translate \"ChildColumn\" tag cause context not specified", null);
            }
            return childInst;
        }
    }

    private final Arte arte;
    private final boolean isContextWrapper;
    private EntityFilter<T> filter;
    private EntitySelection selection;
    private Sqml additionalCond = null;
    private Sqml additionalFrom = null;
    private Map<Id, Object> fltParamValsById = null;
    private Sqml hint = null;
    private PkSubQuery pkSelectSql = null;
    private EPaginationMethod paginationMethod = EPaginationMethod.ABSOLUTE;
    private Pid previousEntityPid;
    private Id previousEntityClassId;

    public final Arte getArte() {
        return arte;
    }

    public EntityGroup() {
        this(null, false);
    }

    public EntityGroup(final Arte arte) {
        this(arte, false);
    }

    public EntityGroup(final boolean isContextWrapper) {
        this(null, isContextWrapper);
    }

    public EntityGroup(final Arte arte, final boolean isContextWrapper) {
        super();
        this.arte = arte != null ? arte : ((AdsClassLoader) getClass().getClassLoader()).getArte();
        this.isContextWrapper = isContextWrapper;
    }
    // Metainfo

    public RadClassDef getSelectionClassDef() {
        return getArte().getDefManager().getClassDef(getSelectionClassId());
    }

    private DdsTableDef ddsMeta = null;

    public DdsTableDef getDdsMeta() {
        if (ddsMeta == null) {
            ddsMeta = getArte().getDefManager().getTableDef(getRadMeta().getEntityId());
        }
        return ddsMeta;
    }

    public abstract RadClassDef getRadMeta();

    public RadClassPresentationDef getSelectionClassPresDef() {
        return getSelectionClassDef().getPresentation();
    }

    //event handlers
    public boolean isContextDefined() {
        return getContext() != null;
    }

    @SuppressWarnings("unused")
    protected boolean beforeDelete() {
        return true;
    }

    @SuppressWarnings("unused")
    protected void afterDelete() {
        //template method
    }

    @SuppressWarnings("unused")
    protected boolean beforeUpdate(final List<Id> propIds) {
        //template method
        return true;
    }

    @SuppressWarnings("unused")
    protected void afterUpdate(final List<Id> propIds) {
        //template method
    }

    @SuppressWarnings("unused")
    public boolean isCommandDisabled(final Id cmdId) throws AppException, InterruptedException {
        return false;
    }

    @SuppressWarnings("unused")
    public FormHandler.NextDialogsRequest execCommand(final Id cmdId, final org.apache.xmlbeans.XmlObject input, final org.apache.xmlbeans.XmlObject output) throws AppException, InterruptedException {
        return null;
    }
    private Context context = null;

    public final Context getContext() {
        checkInited();
        return context;
    }

    private RadSelectorPresentationDef presentation = null;

    public final RadSelectorPresentationDef getPresentation() {
        checkInited();
        return presentation;
    }

    /**
     * Дополнительное условие, накладываемое поверх условия презентации,
     * explorerItem, parentRef (т.е. фильтр плюс условие выборки указанное в
     * селекте)
     */
    public final Sqml getAdditionalCond() {
        checkInited();
        return additionalCond;
    }

    public final Sqml getAdditionalFrom() {
        checkInited();
        return additionalFrom;
    }

    public final Map<Id, Object> getFltParamValsById() {
        checkInited();
        return fltParamValsById;
    }
    private List<RadSortingDef.Item> orderBy = null;

    public final List<RadSortingDef.Item> getOrderBy() {
        checkInited();
        return orderBy;
    }

    public EPaginationMethod getPaginationMethod() {
        return paginationMethod;
    }

    public Pid getPreviousEntityPid() {
        return previousEntityPid;
    }

    public Id getPreviousEntityClassId() {
        return previousEntityClassId;
    }

    public final Sqml getHint() {
        checkInited();
        return hint;
    }

    @Override
    public Iterator<T> iterator() {
        checkIfContextWrapper();
        return new GroupIterator<>(this);
    }

    public final void setEntityFilter(EntityFilter<T> filter) {
        this.filter = filter;
    }

    public EntityFilter<T> getEntityFilter() {
        return filter;
    }

    public final void setEntitySelection(final EntitySelection selection) {
        this.selection = selection;
    }

    public final EntitySelection getEntitySelection() {
        return selection == null ? EntitySelection.EMPTY : selection;
    }

    /**
     * Not implemented and maybe won't be
     *
     * @return
     * @deprecated
     */
    @Deprecated
    public final Long forbiddenCount() {
        //count of objects fobidden by access control system  
        //TODO 2 Group -> forbiddenCount()
        throw new RadixError("TODO Group -> forbiddenCount()");
    }

    public PkSubQuery getPrimaryKeyQuery(final boolean saveSelectionOrder) {
        checkIfContextWrapper();
        return saveSelectionOrder ? getPkQueryWithSelectionOrder() : getPkQuery();
    }

    private PkSubQuery getPkQuery() {
        if (pkSelectSql == null) {
            pkSelectSql = getArte().getDefManager().getDbQueryBuilder().buildPkSelectQuery(this, false);
        }
        return pkSelectSql;
    }
    private PkSubQuery pkSelectSqlWithSelectionOrder = null;

    private PkSubQuery getPkQueryWithSelectionOrder() {
        if (pkSelectSqlWithSelectionOrder == null) {
            pkSelectSqlWithSelectionOrder = getArte().getDefManager().getDbQueryBuilder().buildPkSelectQuery(this, true);
        }
        return pkSelectSqlWithSelectionOrder;
    }

    public QueryBuilderDelegate getQueryBuilderDelegate() {
        return null;
    }

    public void set(
            final EntityGroup.Context context,
            final RadSelectorPresentationDef presentation,
            final Sqml additionalCondSqml,
            final Map<Id, Object> fltParamValsById,
            final List<RadSortingDef.Item> orderBy,
            final Sqml hint) {
        set(context, presentation, additionalCondSqml, null, fltParamValsById, orderBy, hint);
    }

    /**
     * Установка границ группы
     *
     * @param context
     * @param presentation
     * @param additionalCondSqml Дополнительное (накладываемое поверх условия
     * презентации, explorerItem, parentRef) условие (фильтр плюс условие
     * выборки указанное в селекте)
     * @param fltParamValsByName
     * @param orderBy
     * @param hint
     */
    public void set(final EntityGroup.Context context,
            final RadSelectorPresentationDef presentation,
            final Sqml additionalCondSqml,
            final Sqml additionalFromSqml,
            final Map<Id, Object> fltParamValsById,
            final List<RadSortingDef.Item> orderBy,
            final Sqml hint) {
        EntityGroup.Parameters params = new EntityGroup.Parameters(
                context,
                presentation,
                additionalCondSqml,
                additionalFromSqml,
                fltParamValsById,
                orderBy,
                hint,
                EPaginationMethod.ABSOLUTE,
                null,
                null);
        set(params);
    }

    public void set(EntityGroup.Parameters params) {
        checkIfContextWrapper();
        this.context = params.getContext();
        this.presentation = params.getPresentation();
        this.additionalCond = params.getAdditionalCondSqml();
        if (this.additionalCond != null) {
            this.additionalCond.switchOnWriteProtection();
        }
        this.additionalFrom = params.getAdditionalFromSqml();
        if (this.additionalFrom != null) {
            this.additionalFrom.switchOnWriteProtection();
        }
        this.fltParamValsById = params.getFltParamValsById();
        if (params.getOrderBy() != null) {
            this.orderBy = Collections.unmodifiableList(params.getOrderBy());
        } else {
            this.orderBy = Collections.emptyList();
        }
        this.hint = params.getHint();
        if (this.hint != null) {
            this.hint.switchOnWriteProtection();
        }
        this.paginationMethod = params.getPaginationMethod();
        this.previousEntityPid = params.getPreviousEntityPid();
        this.previousEntityClassId = params.getPreviousEntityClassId();
        this.inited = true;
    }

    public Id getSelectionClassId() {
        if (context != null && context.getSelectionClassDef() != null) {
            return context.getSelectionClassDef().getId();
        }
        final RadSelectorPresentationDef p = getPresentation();
        if (p != null) {
            return p.getSelectionClassId();
        }
        return RadClassDef.getEntityClassIdByTableId(getDdsMeta().getId());
    }

    /**
     *
     * @param bDeleteCascade
     */
    protected void doCheckCascadeBeforeDelete(final boolean bDeleteCascade) throws DeleteCascadeRestrictedException, DeleteCascadeConfirmationRequiredException {
        defaultCheckCascadeBeforeDelete(bDeleteCascade);
    }

    protected void doDelete() throws FilterParamNotDefinedException {
        final DeleteGroupQuery q = getArte().getDefManager().getDbQueryBuilder().buildDeleteGroupQuery(this);
        try {
            final Pid parentPid = getParentPidIfInTree();
            q.delete(this, parentPid);
        } finally {
            q.free();
        }
    }

    public final void delete(final boolean bDeleteCascade) throws FilterParamNotDefinedException, DeleteCascadeRestrictedException, DeleteCascadeConfirmationRequiredException {
        checkIfContextWrapper();
        doCheckCascadeBeforeDelete(bDeleteCascade);
        if (!beforeDelete()) {
            return;
        }
        doDelete();
        afterDelete();
    }

    private Pid getParentPidIfInTree() {
        return (getContext() instanceof TreeContext) ? ((TreeContext) getContext()).getParentPid() : null;
    }

    public static EntityGroup getDefaultGroupEventHandler(final Arte arte, final DdsTableDef table, final boolean isContextWrapper) {
        return new DefaultGroupEventHandler(arte, table, isContextWrapper);
    }

    public static EntityGroup getDefaultGroupEventHandler(final Arte arte, final DdsTableDef table) {
        return getDefaultGroupEventHandler(arte, table, false);
    }

    public static EntityGroup getDefaultGroupEventHandler(final Arte arte, final Id entityId, final boolean isContextWrapper) {
        return getDefaultGroupEventHandler(arte, arte.getDefManager().getTableDef(entityId), isContextWrapper);
    }

    public static EntityGroup getDefaultGroupEventHandler(final Arte arte, final Id entityId) {
        return getDefaultGroupEventHandler(arte, arte.getDefManager().getTableDef(entityId), false);
    }

    public final void update(final EntityPropVals propVals) throws FilterParamNotDefinedException {
        //Arte.checkBreak();
        checkIfContextWrapper();
        final List<Id> propIds = new LinkedList<>();
        final Iterator<Id> ids = propVals.asMap().keySet().iterator();
        while (ids.hasNext()) {
            propIds.add(ids.next());
        }
        if (!beforeUpdate(propIds)) {
            return;
        }
        final UpdateGroupQuery q = getArte().getDefManager().getDbQueryBuilder().buildUpdateGroupQuery(
                this, propVals.asMap().keySet());
        try {
            q.update(this, getParentPidIfInTree(), propVals);
        } finally {
            q.free();
        }
        afterUpdate(propIds);
    }

    public final String getRequestedRoleIds() {
        return context instanceof PropContext ? getSelectionClassPresDef().getRequestedRoleIdsStr() : getPresentation().getRequestedRoleIdsStr();
    }

//Class catalogs management
    public final static class ClassCatalogItem {

        private Id classId;
        private int level;
        private String title;
        private final Id itemId;

        /**
         *
         * @param level
         * @param classId - null for groups
         * @param title
         */
        public ClassCatalogItem(final int level, final Id classId, final String title) {
            this.classId = classId;
            this.level = level;
            this.title = title;
            itemId = null;
        }

        public ClassCatalogItem(final int level, final Id classId, final String title, final Id itemId) {
            this.classId = classId;
            this.level = level;
            this.title = title;
            this.itemId = itemId;
        }

        public Id getClassId() {
            return classId;
        }

        public int getLevel() {
            return level;
        }

        public String getTitle() {
            return title;
        }

        public Id getItemId() {
            return itemId;
        }

        public void setClassId(final Id classId) {
            this.classId = classId;
        }

        public void setLevel(final int level) {
            this.level = level;
        }

        public void setTitle(final String title) {
            this.title = title;
        }
    }

    /**
     *
     * @param classCatalogId - can be null
     * @param list - list of instantiable classes, can be modified by this
     * handler
     */
    public void onListInstantiableClasses(final Id classCatalogId, final List<ClassCatalogItem> list) {
    }

    private boolean inited = false;

    private void checkInited() {
        if (!inited) {
            throw new IllegalUsageError("EntityGroup must be inited before first usage");
        }
    }

    public final boolean wasInited() {
        return inited;
    }

    private void checkIfContextWrapper() {
        if (isContextWrapper) {
            throw new IllegalUsageError("This operation cannot be used in context entity group");
        }
    }

    private static final class DefaultGroupEventHandler extends EntityGroup {

        DefaultGroupEventHandler(final Arte arte, final DdsTableDef table) {
            this(arte, table, false);
        }

        DefaultGroupEventHandler(final Arte arte, final DdsTableDef table, final boolean isContextWrapper) {
            super(arte, isContextWrapper);
            this.table = table;
        }

        private final DdsTableDef table;

        @Override
        public DdsTableDef getDdsMeta() {
            return table;
        }

        @Override
        public RadClassDef getRadMeta() {
            return new RadClassDef(getArte(), RadClassDef.getEntityGroupClassIdByTableId(table.getId()), table.getName(), null, EClassType.ENTITY_GROUP, null, table.getId(), null, null, null, null, null, null);
        }
    }

    private static final class GroupIterator<E extends Entity> implements Iterator<E> {

        private final EntityGroup<E> group;
        private final PreparedStatement stmt;
        private final ResultSet rs;
        private final EntityFilter<E> filter;
        private final EntitySelection selection;//not null
        private boolean endOfGrp = false;
        private int numberOfRestSelectedObjects;
        private E curObj = null;
        private E nextObj = null;

        GroupIterator(final EntityGroup<E> group) {
            this.group = group;
            filter = group.getEntityFilter();
            selection = group.getEntitySelection();
            if (!selection.isEmpty() && selection.getMode() == ESelectionMode.INCLUSION) {
                numberOfRestSelectedObjects = selection.getSelection().size();
            } else {
                numberOfRestSelectedObjects = -1;
            }
            final PkSubQuery pkQry = group.getPrimaryKeyQuery(true);
            try {
                group.getArte().getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
                try {
                    stmt = group.getArte().getDbConnection().get().prepareStatement(pkQry.getSql());
                } finally {
                    group.getArte().getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
                }
                group.getArte().getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
                try {
                    group.getArte().getDbConnection().registerTemporaryStatement(stmt);
                    pkQry.setParams(stmt, 1);
                    rs = stmt.executeQuery();
                } finally {
                    group.getArte().getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
                }
            } catch (SQLException e) {
                throw new DatabaseError("Can't create group iterator: " + String.valueOf(e.getMessage()), e);
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean hasNext() {
            if (nextObj != null) {
                return true;
            }
            if (endOfGrp) {
                return false;
            }
            try {
                E objectCandidate;
                do {
                    if (numberOfRestSelectedObjects == 0 || !rs.next()) {
                        endOfGrp = true;
                        nextObj = null;
                        try {
                            rs.close();
                        } catch (SQLException ex) {
                            //do nothing
                            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                        }
                        try {
                            stmt.close();
                        } catch (SQLException ex) {
                            //do nothing
                            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                        }
                        return false;
                    }
                    final EntityPropVals key = new EntityPropVals();
                    int i = 1;
                    for (DdsIndexDef.ColumnInfo pkProp : group.getDdsMeta().getPrimaryKey().getColumnsInfo()) {
                        key.putPropValById(
                                pkProp.getColumnId(),
                                DbQuery.getFieldVal(group.getArte(), rs, i, pkProp.getColumn().getValType(), pkProp.getColumn().getDbType()));
                        i++;
                    }
                    objectCandidate = (E) group.getArte().getEntityObject(new Pid(group.getArte(), group.getDdsMeta(), key.asMap()), key, false);
                } while ((filter != null && filter.omitEntity(objectCandidate)) || (!selection.isEmpty() && !selection.isEntitySelected(objectCandidate)));
                nextObj = objectCandidate;
                if (selection.getMode() == ESelectionMode.INCLUSION) {
                    numberOfRestSelectedObjects--;
                }
                return true;
            } catch (SQLException e) {
                throw new DatabaseError("Can't load next entity of group: " + String.valueOf(e.getMessage()), e);
            }
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            curObj = nextObj;
            nextObj = null;
            return curObj;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private void defaultCheckCascadeBeforeDelete(final boolean bCascade) throws DeleteCascadeRestrictedException, DeleteCascadeConfirmationRequiredException {
        final StringBuffer subDelEntitiesList = new StringBuffer();
        final StringBuffer subNullEntitiesList = new StringBuffer();
        defaultCheckCascadeBeforeDelete(getDdsMeta(), getPrimaryKeyQuery(false), bCascade, subDelEntitiesList, subNullEntitiesList);
        for (DdsReferenceDef detRef : getRadMeta().getDetailsRefs()) {
            defaultCheckCascadeBeforeDelete(getArte().getDefManager().getTableDef(detRef.getChildTableId()), new PkSubQuery(getArte(), detRef, getPrimaryKeyQuery(false)), bCascade, subDelEntitiesList, subNullEntitiesList);
        }
        if (!bCascade && (subNullEntitiesList.length() != 0 || subDelEntitiesList.length() != 0)) {
            throw new DeleteCascadeConfirmationRequiredException(
                    //TODO use ML strings

                    getArte().getClientLanguage() != EIsoLanguage.RUSSIAN ? (subNullEntitiesList.length() != 0 ? "Do you want to clear child references to these objects: " + subNullEntitiesList + "?\n" : "")
                            + (subDelEntitiesList.length() != 0 ? "Do you  want to delete subobject: " + subDelEntitiesList + "?" : "") : (subNullEntitiesList.length() != 0 ? "Обнулить ссылки на эти объекты в дочерних подобъектах: " + subNullEntitiesList + "?\n" : "")
                            + (subDelEntitiesList.length() != 0 ? "Удалить подобъекты: " + subDelEntitiesList + "?" : ""));
        }
    }

    private void defaultCheckCascadeBeforeDelete(final DdsTableDef tab, final SubQuery pkSelect, final boolean bCascade, final StringBuffer subDelEntitiesList, final StringBuffer subNullEntitiesList) throws DeleteCascadeRestrictedException {
        for (DdsReferenceDef childRef : tab.collectIncomingReferences()) {
            if (((childRef.getDeleteMode() == EDeleteMode.CASCADE
                    || childRef.getDeleteMode() == EDeleteMode.SET_NULL)
                    && (childRef.isConfirmDelete() && !bCascade)
                    || childRef.getDeleteMode() == EDeleteMode.RESTRICT
                    && childRef.getRestrictCheckMode() == DdsReferenceDef.ERestrictCheckMode.ALWAYS)
                    && pkSelect.isChildExists(childRef)) {
                String childTitle;
                final DefManager defManager = arte.getDefManager();

                DdsTableDef childTable = defManager.getTableDef(childRef.getChildTableId());
                final DdsReferenceDef mdRef = defManager.getMasterReferenceDef(childTable);
                if (mdRef != null) {
                    childTable = defManager.getTableDef(mdRef.getParentTableId());
                }
                try {
                    childTitle = "\"" + defManager.getClassDef(RadClassDef.getEntityClassIdByTableId(childTable.getId())).getTitle() + "\"";
                } catch (DefinitionNotFoundError e) {
                    childTitle = null;
                }
                if (childTitle == null || childTitle.length() == 0) {
                    childTitle = "\"" + childTable.getName() + "\" (#" + childTable.getId() + ")";
                }
                if (childRef.getDeleteMode() == EDeleteMode.CASCADE) {
                    if (subDelEntitiesList.length() != 0) {
                        subDelEntitiesList.append(", ");
                    }
                    subDelEntitiesList.append(childTitle);
                } else if (childRef.getDeleteMode() == EDeleteMode.SET_NULL) {
                    if (subNullEntitiesList.length() != 0) {
                        subNullEntitiesList.append(", ");
                    }
                    subNullEntitiesList.append(childTitle);
                } else {
                    //TODO use ML strings
//		        	final String exMess = MessageFormat.format(arte.getDefManager().messageBundle.message(CArteMlStrings.CANT_DELETE_BECAUSE_CASCADE_DELETING_IS_RESTRICTED), new Object[]{childTitle});
                    final String exMess;
                    if (getArte().getClientLanguage() != EIsoLanguage.RUSSIAN) {
                        exMess = "Can't delete the objects because they are used by " + childTitle + " object.";
                    } else {
                        exMess = "Удаление объектов запрещено, так как эти объекты используются объектом " + childTitle + ".";
                    }
                    throw new DeleteCascadeRestrictedException(exMess, childTitle, childRef.getChildTableId());
                }
            }
        }
    }

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

    public Restrictions getAdditionalRestrictions(final RadSelectorPresentationDef presentation, final List<Id> applicableRoles) {
        return Restrictions.ZERO;
    }

    public static class Parameters {

        private final EntityGroup.Context context;
        private final RadSelectorPresentationDef presentation;
        private final Sqml additionalCondSqml;
        private final Sqml additionalFromSqml;
        private final Map<Id, Object> fltParamValsById;
        private final List<RadSortingDef.Item> orderBy;
        private final Sqml hint;
        private final EPaginationMethod paginationMethod;
        private final Pid previousEntityPid;
        private final Id previousEntityClassId;

        public Parameters(Context context, RadSelectorPresentationDef presentation, Sqml additionalCondSqml, Sqml additionalFromSqml, Map<Id, Object> fltParamValsById, List<RadSortingDef.Item> orderBy, Sqml hint, EPaginationMethod paginationMethod, Pid previousEntity, Id previousEntityClassId) {
            this.context = context;
            this.presentation = presentation;
            this.additionalCondSqml = additionalCondSqml;
            this.additionalFromSqml = additionalFromSqml;
            this.fltParamValsById = fltParamValsById;
            this.orderBy = orderBy;
            this.hint = hint;
            this.paginationMethod = paginationMethod;
            this.previousEntityPid = previousEntity;
            this.previousEntityClassId = previousEntityClassId;
        }

        public Context getContext() {
            return context;
        }

        public RadSelectorPresentationDef getPresentation() {
            return presentation;
        }

        public Sqml getAdditionalCondSqml() {
            return additionalCondSqml;
        }

        public Sqml getAdditionalFromSqml() {
            return additionalFromSqml;
        }

        public Map<Id, Object> getFltParamValsById() {
            return fltParamValsById;
        }

        public List<RadSortingDef.Item> getOrderBy() {
            return orderBy;
        }

        public Sqml getHint() {
            return hint;
        }

        public EPaginationMethod getPaginationMethod() {
            return paginationMethod;
        }

        public Pid getPreviousEntityPid() {
            return previousEntityPid;
        }

        public Id getPreviousEntityClassId() {
            return previousEntityClassId;
        }

    }

}
