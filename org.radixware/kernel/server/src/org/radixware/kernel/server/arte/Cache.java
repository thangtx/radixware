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
package org.radixware.kernel.server.arte;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Blob;
import java.sql.Clob;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;

//import oracle.sql.BLOB;
//import oracle.sql.CLOB;
import org.radixware.kernel.server.types.Cursor;
import org.radixware.kernel.server.types.Entity;
import org.radixware.kernel.server.types.EntityGroup;
import org.radixware.kernel.server.types.EntityPropVals;
import org.radixware.kernel.server.types.Pid;
import org.radixware.kernel.server.types.PresentationEntityAdapter;
import org.radixware.kernel.common.utils.BufferedPool;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.radixware.kernel.common.defs.IDefinitionFactory;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EAutoUpdateReason;
import org.radixware.kernel.common.enums.EDdsTableExtOption;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.ETimingSection;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.SystemPropUtils;
import org.radixware.kernel.server.exceptions.PropNotLoadedException;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.types.EntityMethodAccessor;
import org.radixware.kernel.server.types.EntityDetails;

/**
 * Cache stores references to resources that require automatic
 * close/create/update (new entities, opened cursors). Supports conception of
 * the stacked cache lines (via sections)
 *
 *
 */
public class Cache {

    private static final boolean DISABLE_UPDATE_ORDERING = SystemPropUtils.getBooleanSystemProp("rdx.disable.update.ordering", false);
    private static final String WRITE_CACHE_CONTENT_ON_OVERFLOW_WARNING_DIR = SystemPropUtils.getStringSystemProp("rdx.write.cache.content.on.overflow.warning.dir", null);
    public static final boolean STORE_CACHE_OPERATION_STACKS = WRITE_CACHE_CONTENT_ON_OVERFLOW_WARNING_DIR != null || SystemPropUtils.getBooleanSystemProp("rdx.store.arte.cache.items.creation.stack", false);
    private static final int DEFAULT_EXPECTED_CACHE_SIZE = SystemPropUtils.getIntSystemProp("rdx.arte.cache.expected.size", 5000);

    public static enum EMode {

        OBJS_CACHED_IN_PREV_TRANS_ALLOWED, //default
        OBJS_CACHED_IN_PREV_TRANS_PROHIBITED // for presentations to avoid showing old data to user
    }
    //Создание объекта класса
    private static final Object[] EMPTY_OBJ_ARR = new Object[]{};
    private static final int MAX_POOL_PASSAGE_COUNT = 100;
    protected static final String ERR_CANT_INST_PRES_ENTITY_ADAPTER = "Can\'t instantiate presentation entity adapter class: ";
    protected static final String ERR_TRANSACTION_NOT_STARTED = "ARTE transaction is not started";
    static private final long LOBS_SUSPECTED_COUNT = 200;
    private static final int MAX_UPDATE_DB_ITER_COUNT = 100;
    //
    private EMode mode;
    private final Arte arte;
    private long sectionSeq = 0;
    private final Stack<Long> sections = new Stack<>();
    private final BufferedPool<Item<Entity>> newObjects;   //содержит новые объекты
    private final BufferedPool<Item<Cursor>> cursors;   //содержит все открытые курсоры
    private final List<Item<Statement>> tmpStmts;
    private final Map<Entity, List<EntityModification>> entityModifications = new HashMap<>();
    private List<Item<Blob>> tmpBlobs;
    private List<Item<Clob>> tmpClobs;
    private boolean isUpdatingDb = false;
    private int expectedCacheSize = DEFAULT_EXPECTED_CACHE_SIZE;

    protected Cache(final Arte arte) {
        this.arte = arte;
        newObjects = new BufferedPool<>(BufferedPool.ERegistrationMode.IMMEDIATELY);   //содержит новые объекты
        cursors = new BufferedPool<>(BufferedPool.ERegistrationMode.IMMEDIATELY);   //содержит все открытые курсоры
        tmpClobs = new ArrayList<>(128);
        tmpBlobs = new ArrayList<>(128);
        tmpStmts = new ArrayList<>(128);
        mode = EMode.OBJS_CACHED_IN_PREV_TRANS_ALLOWED;
    }

    EMode getMode() {
        return mode;
    }

    public void setMode(final EMode mode) {
        this.mode = mode;
    }

    void setDefaultMode() {
        setMode(EMode.OBJS_CACHED_IN_PREV_TRANS_ALLOWED);
    }

    public void afterUpdate(final Entity entity) {
        markAsModified(entity, EntityModification.EModificationType.UPDATE, getArte().getSavePointsNesting());
    }

    public void afterDelete(final Entity entity) {
        markAsModified(entity, EntityModification.EModificationType.DELETE, getArte().getSavePointsNesting());
    }

    public void afterCreate(final Entity entity) {
        markAsModified(entity, EntityModification.EModificationType.CREATE, getArte().getSavePointsNesting());
    }

    private void markAsModified(final Entity entity, EntityModification.EModificationType type, long savepointLevel) {
        if (entity instanceof EntityDetails) {
            return;
        }
        if (!EntityMethodAccessor.isAfterCommitRequired(entity)) {
            return;
        }
        List<EntityModification> modifications = entityModifications.get(entity);
        if (modifications != null) {
            if (modifications.size() > 0) {
                final long lastSavepointLevel = modifications.get(modifications.size() - 1).savepointNesting;
                if (lastSavepointLevel > savepointLevel) {
                    throw new IllegalStateException("Marking entity as modified at savepoint level " + savepointLevel + " when it has modifications at savepoint level " + lastSavepointLevel);
                }
                if (lastSavepointLevel < savepointLevel) {
                    modifications.add(null);//actual value is filled below
                }
            } else {
                modifications.add(null);//actual value is filled below
            }
        } else {
            modifications = new ArrayList<>();
            modifications.add(null);//actual value is filled below
            entityModifications.put(entity, modifications);
        }
        final EntityModification lastWrittenModification = modifications.get(modifications.size() - 1);
        if (lastWrittenModification != null
                && lastWrittenModification.type == EntityModification.EModificationType.CREATE
                && type != EntityModification.EModificationType.DELETE) {
            return;//CREATE can be overwritten only by DELETE
        }
        modifications.set(modifications.size() - 1, new EntityModification(savepointLevel, type));
    }

    /**
     * вход в секцию
     *
     * @return
     */
    public final Long enterSection() {
        sectionSeq++;
        final Long id = Long.valueOf(sectionSeq);
        sections.add(id);
        return id;
    }

    /**
     * выход из секции с удалением из кэша объектов, закешированных в этой
     * секции
     *
     * @param id
     */
    public final void leaveSection(final Long id) {
        if (sections.isEmpty()) {
            throw new IllegalUsageError("Section is not entered");
        }
        if (sections.peek().equals(id)) {
            updateDatabase(EAutoUpdateReason.BEFORE_LEAVING_CACHING_SECTION);
            clear(id);
            sections.pop();
            arte.getProfiler().flush();
            return;
        }
        throw new IllegalUsageError("Invalid section: try to leave #" + String.valueOf(id) + ", current section #" + String.valueOf(sections.peek()));
    }

    private Long curSection() {
        if (sections.isEmpty()) {
            return null;
        }
        return sections.peek();
    }

    private Id getRealClassId(final Pid pid, final EntityPropVals loadedProps) {
        if (pid == null) {
            return null;
        }
        final DdsTableDef tab = pid.getTable();
        if (!tab.getExtOptions().contains(EDdsTableExtOption.ENABLE_APPLICATION_CLASSES)) {
            return RadClassDef.getEntityClassIdByTableId(tab.getId());
        }
        Id classGuidColumnId = getClassGuidColumnId(pid);
        if (loadedProps != null && loadedProps.containsPropById(classGuidColumnId)) {
            try {
                return Id.Factory.loadFrom((String) loadedProps.getPropValById(classGuidColumnId));
            } catch (PropNotLoadedException e) {
                throw new RadixError("ClassGuid column not loaded"); //never
            }
        }
        return null;
    }

    Id getClassGuidColumnId(final Pid pid) {
        if (pid == null) {
            return null;
        }
        final DdsTableDef tab = pid.getTable();
        if (!tab.getExtOptions().contains(EDdsTableExtOption.ENABLE_APPLICATION_CLASSES)) {
            return null;
        }
        return tab.getClassGuidColumn().getId();
    }

    //Получение указателя на объект базы
    Entity getEntityObject(final Pid pid, final EntityPropVals loadedProps, final boolean checkExistence) {
        if (!arte.isInTransaction()) {
            throw new IllegalUsageError(ERR_TRANSACTION_NOT_STARTED);
        }
        arte.yeld();
        Entity obj = null;
        final ExistingObjectsPool.Item regItem = arte.getDefManager().releaseCache.getExistingObjects().getByPid(pid);
        if (regItem != null) {
            obj = regItem.getObject();
            if (getMode() == EMode.OBJS_CACHED_IN_PREV_TRANS_PROHIBITED
                    && arte.getTransactionSeqNumber() != regItem.getArteTranSeq()) { // an old object and it's prohibted to use old objects
                obj.discard();
                obj = null;
            }
        }
        if (obj == null) {
            final DdsTableDef tab = pid.getTable();
            EntityPropVals justReadProps = null;
            Id realClassId = getRealClassId(pid, loadedProps);
            if (realClassId == null) {
                //read ClassGuid and all other own props
                justReadProps = arte.getDefManager().readOwnProps(pid);
                if (loadedProps != null) {
                    for (Map.Entry<Id, Object> entry : loadedProps.asMap().entrySet()) {//preserve old behavior
                        justReadProps.putPropValById(entry.getKey(), entry.getValue());
                    }
                }
                realClassId = getRealClassId(pid, justReadProps);
            }
            obj = (Entity) newObject(realClassId);
            if (justReadProps != null) {
                obj.loadAndMarkAsRead(pid, justReadProps);
            } else {
                obj.load(pid, loadedProps);
            }
        }
        if (checkExistence && !obj.wasRead()) {
            obj.read(null, null);
        }
        return obj;
    }

    protected Object newObject(final Id classId) {
        return arte.getDefManager().newClassInstance(classId, EMPTY_OBJ_ARR);
    }

    private void checkSize() {
        int totalCount = newObjects.getRegistered().size() + arte.getDefManager().releaseCache.getExistingObjects().getSize();
        if (expectedCacheSize > 0 && totalCount > expectedCacheSize) {
            arte.getTrace().put(EEventSeverity.WARNING, "ARTE entity cache contains more then " + expectedCacheSize + " objects (" + totalCount + "), consider using caching sections or Arte.setExpectedCacheSize(int)", EEventSource.ARTE);
            writeCacheContent();
            expectedCacheSize = -1;
        }
    }

    private void writeCacheContent() {
        if (WRITE_CACHE_CONTENT_ON_OVERFLOW_WARNING_DIR != null) {
            final String fileName = (WRITE_CACHE_CONTENT_ON_OVERFLOW_WARNING_DIR.isEmpty()
                    ? "" : WRITE_CACHE_CONTENT_ON_OVERFLOW_WARNING_DIR + "/")
                    + "CacheContent_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_Arte" + String.format("%03d", arte.getSeqNumber()) + ".txt";
            try (Writer fw = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(new File(fileName))), StandardCharsets.UTF_8)) {
                for (Item<Entity> item : newObjects.getRegistered()) {
                    writeObject("new_object", item, fw);
                }
                for (EntityCacheItem item : arte.getDefManager().releaseCache.getExistingObjects().getRegistered()) {
                    writeObject("existing_object", item, fw);
                }
            } catch (Exception ex) {
                arte.getTrace().put(EEventSeverity.WARNING, "Unable to write cache content: " + ExceptionTextFormatter.throwableToString(ex), EEventSource.ARTE);
            }
        }
    }

    private void writeObject(final String comment, final Item<Entity> item, final Writer writer) throws IOException {
        writer.append(comment + ": " + item.object.getRadMeta().getName() + "[" + (item.object.getPid() == null ? "null" : item.object.getPid().toString()) + "]" + "\n");
        writer.append(item.creationStack);
        writer.append("*****\n");
    }

    //Регистрация объекта как существующего в базе, вызывается после create
    void registerExistingEntityObject(final Entity obj) {
        assert obj.isInDatabase(false);
        Item<Entity> registration = new Item<>(obj, curSection());
        final Item<Entity> oldRegistration = newObjects.findRegistration(registration);
        if (oldRegistration != null) //если объект уже был зарегистрнирован сохраняем его секцию, чтобы при create он не переходил в текущую
        {
            registration = oldRegistration;
        }
        newObjects.unregister(registration);
        arte.getDefManager().releaseCache.getExistingObjects().register(new EntityCacheItem(registration.object, registration.section));
        checkSize();
    }

    void unregisterExistingEntityObject(final Entity obj) {
        arte.getDefManager().releaseCache.getExistingObjects().unregister(obj);
    }

    void registerNewEntityObject(final Entity obj) {
        assert !obj.isInDatabase(false);
        newObjects.register(new Item<>(obj, curSection()));
        checkSize();
    }

    public void unregisterFromAfterCommit(final Entity entity) {
        entityModifications.remove(entity);
    }

    void unregisterNewEntityObject(final Entity obj) {
        newObjects.unregister(new Item<>(obj, null));
    }

    Entity findNewEntityObjectByPid(final Pid pid) {
        for (Item<Entity> newObjReg : newObjects.getRegistered()) {
            if (newObjReg.object.getPid().equals(pid)) {
                return newObjReg.object;
            }
        }
        return null;
    }

    Entity findParentInNewEntityObjects(final DdsReferenceDef ref, final Entity child) {
        final BufferedPool.ERegistrationMode oldMode = newObjects.getRegistrationMode();
        try {
            newObjects.setRegistrationMode(BufferedPool.ERegistrationMode.SHEDULE);
            Collection<Item<Entity>> shedulled = newObjects.getRegistered();
            int i = 0;
            while (!shedulled.isEmpty()) {
                if (++i > MAX_POOL_PASSAGE_COUNT) {
                    throw new IllegalUsageError("Can't find requested parent among new objects. " + String.valueOf(MAX_UPDATE_DB_ITER_COUNT) + " attempts were made but new objects pool enlarged time and again");
                }
                NEXT_OBJ_LBL:
                for (Item<Entity> it : shedulled) {
                    if (it.object.getDdsMeta().getId() == ref.getParentTableId()) { // отсечение по типу родителя
                        for (DdsReferenceDef.ColumnsInfoItem refProp : ref.getColumnsInfo()) {
                            // RADIX-892
                            // if it is a FK column and a parentRef prop on this FK exists and its value is inherited
                            // then return value of FK corresponded to inherited parent prop value
                            final Object fkVal = child.getNativePropOwnVal(refProp.getChildColumnId());
                            //final Object fkVal = child.getProp(refProp.getChildColumnId());
                            if (fkVal == null) {
                                return null;
                            }
                            if (!fkVal.equals(it.object.getNativePropOwnVal(refProp.getParentColumnId()))) {
                                continue NEXT_OBJ_LBL;
                            }
                        }
                        return it.object;
                    }
                }
                if (oldMode == BufferedPool.ERegistrationMode.IMMEDIATELY) // RADIX-2545
                {
                    shedulled = newObjects.flush();
                } else {
                    break;
                }
            }
        } finally {
            newObjects.setRegistrationMode(oldMode);
        }
        return null;

    }

    EntityGroup getGroupHander(final Id entityId, final boolean isContextWrapper) {
        final Id classId = RadClassDef.getEntityGroupClassIdByTableId(entityId);
        try {
            return (EntityGroup) arte.getDefManager().newClassInstance(classId, new Object[]{Boolean.valueOf(isContextWrapper)});
        } catch (DefinitionNotFoundError e) { // user group event handler is not defined
            return EntityGroup.getDefaultGroupEventHandler(arte, entityId, isContextWrapper);
        }
    }

    EntityGroup getGroupHander(final Id entityId) {
        return getGroupHander(entityId, false);
    }

    public <T extends Entity> PresentationEntityAdapter<T> getPresentationAdapter(final T entity) {
        if (!arte.isInTransaction()) {
            throw new IllegalUsageError(ERR_TRANSACTION_NOT_STARTED);
        }
        PresentationEntityAdapter<T> adapter;
        try {
            RadClassDef ac = null;
            Id classId = entity.getRadMeta().getId();
            while (ac == null && classId != null) {
                try {
                    ac = arte.getDefManager().getClassDef(Id.Factory.changePrefix(classId, EDefinitionIdPrefix.ADS_PRESENTATION_ENTITY_ADAPTER_CLASS));
                } catch (DefinitionNotFoundError e) { //not defined for this class let's check if adapter is defined for the ancestor
                    ac = null;
                    classId = arte.getDefManager().getClassDef(classId).getAncestorId();
                }
            }
            if (ac != null) { // an adapter definition was found

                final String factory = arte.getDefManager().getAdsFactoryClassNameByIClassId(ac.getId());
                if (factory == null) {
                    throw new ClassNotFoundException("Unable to find factory for class # " + ac.getId());
                }
                final Class c = arte.getDefManager().getClassLoader().loadClass(factory);
                adapter = (PresentationEntityAdapter<T>) ((IDefinitionFactory) c.newInstance()).newInstance(ac.getId(), new Object[]{entity});
            } else {//custom adapter is not defined let's use default
                adapter = new PresentationEntityAdapter<T>(entity);
            }
        } catch (ClassNotFoundException e) {
            throw new RadixError(ERR_CANT_INST_PRES_ENTITY_ADAPTER + ExceptionTextFormatter.getExceptionMess(e), e);
        } catch (InstantiationException e) {
            throw new RadixError(ERR_CANT_INST_PRES_ENTITY_ADAPTER + ExceptionTextFormatter.getExceptionMess(e), e);
        } catch (IllegalAccessException e) {
            throw new RadixError(ERR_CANT_INST_PRES_ENTITY_ADAPTER + ExceptionTextFormatter.getExceptionMess(e), e);
        }
        return adapter;
    }

    //Регистрация курсора
    void registerCursor(final Cursor obj) {
        cursors.register(new Item<Cursor>(obj, curSection()));
    }

    void unregisterCursor(final Cursor obj) {
        cursors.unregister(new Item<Cursor>(obj, null));
    }

    final void updateDatabase(final EAutoUpdateReason reason) {
        if (isUpdatingDb) {
            return; // continue the outer updateDatabase
        }
        isUpdatingDb = true;
        try {
            //при записи в базу обработчики событий могут модифицировать и(или) создавать объекты
            //делаем цикл: записываем в базу изменения, пока они есть
            for (int i = 0; i < MAX_UPDATE_DB_ITER_COUNT; i++) {
                if (!updateDatabaseIteration(reason))// все изменения уже записаны
                {
                    return;
                }
            }
            throw new IllegalUsageError("Can't update database. " + String.valueOf(MAX_UPDATE_DB_ITER_COUNT) + " attempts were made but objects were modified time and again");
        } finally {
            isUpdatingDb = false;
        }
    }

    final boolean updateDatabaseIteration(final EAutoUpdateReason reason) {
        boolean res = false;
        {
            final BufferedPool.ERegistrationMode oldMode = newObjects.getRegistrationMode();
            try {
                newObjects.setRegistrationMode(BufferedPool.ERegistrationMode.SHEDULE);
                Collection<Item<Entity>> shedulled = newObjects.getRegistered();
                int i = 0;
                while (!shedulled.isEmpty()) {
                    if (++i > MAX_POOL_PASSAGE_COUNT) {
                        throw new IllegalUsageError("Can't write all new objects to database. " + String.valueOf(MAX_UPDATE_DB_ITER_COUNT) + " attempts were made but new objects pool enlarged time and again");
                    }
                    for (Item<Entity> it : shedulled) {
                        if (it.object.autoUpdate(reason)) {
                            res = true;
                        }
                    }
                    if (oldMode == BufferedPool.ERegistrationMode.IMMEDIATELY) // RADIX-2545
                    {
                        shedulled = newObjects.flush();
                    } else {
                        break;
                    }
                }
            } finally {
                newObjects.setRegistrationMode(oldMode);
            }
        }
        {
            final ExistingObjectsPool existingObjects = arte.getDefManager().releaseCache.getExistingObjects();
            final BufferedPool.ERegistrationMode oldMode = existingObjects.getRegistrationMode();
            try {
                existingObjects.setRegistrationMode(BufferedPool.ERegistrationMode.SHEDULE);
                List<EntityCacheItem> schedulled = new ArrayList<>(existingObjects.getRegistered());
                int i = 0;
                while (!schedulled.isEmpty()) {
                    if (++i > MAX_POOL_PASSAGE_COUNT) {
                        throw new IllegalUsageError("Can't write all modifications of existing objects to database. " + String.valueOf(MAX_UPDATE_DB_ITER_COUNT) + " attempts were made but existing objects pool enlarged time and again");
                    }
                    orderEntityItemsForUpdate(schedulled);
                    for (Item<Entity> it : schedulled) {
                        if (it.object.autoUpdate(reason)) {
                            res = true;
                        }
                    }
                    if (oldMode == BufferedPool.ERegistrationMode.IMMEDIATELY) // RADIX-2545
                    {
                        schedulled = new ArrayList<>(existingObjects.flush());
                    } else {
                        break;
                    }
                }
            } finally {
                existingObjects.setRegistrationMode(oldMode);
            }
        }
        {
            final BufferedPool.ERegistrationMode oldMode = cursors.getRegistrationMode();
            try {
                cursors.setRegistrationMode(BufferedPool.ERegistrationMode.SHEDULE);
                List<Item<Cursor>> schedulled = new ArrayList<>(cursors.getRegistered());
                int i = 0;
                while (!schedulled.isEmpty()) {
                    if (++i > MAX_POOL_PASSAGE_COUNT) {
                        throw new IllegalUsageError("Can't write all modifications of cursors to database. " + String.valueOf(MAX_UPDATE_DB_ITER_COUNT) + " attempts were made but existing objects pool enlarged time and again");
                    }

                    orderCursorsForUpdate(schedulled);

                    for (Item<Cursor> it : schedulled) {
                        if (it.object.autoUpdate(reason)) {
                            res = true;
                        }
                    }
                    if (oldMode == BufferedPool.ERegistrationMode.IMMEDIATELY) // RADIX-2545
                    {
                        schedulled = new ArrayList<>(cursors.flush());
                    } else {
                        break;
                    }
                }
            } finally {
                cursors.setRegistrationMode(oldMode);
            }
        }
        return res;
    }

    private void orderEntityItemsForUpdate(final List<EntityCacheItem> items) {
        if (DISABLE_UPDATE_ORDERING) {
            return;
        }
        Collections.sort(items, new Comparator<Item<Entity>>() {
            @Override
            public int compare(Item<Entity> o1, Item<Entity> o2) {
                int priorityDiff = o2.object.getAutoUpdatePriority() - o1.object.getAutoUpdatePriority();
                if (priorityDiff != 0) {
                    return priorityDiff;
                }
                int tableIdDiff = o1.object.getEntityId().compareTo(o2.object.getEntityId());
                if (tableIdDiff != 0) {
                    return tableIdDiff;
                }
                return o1.object.getPid().toString().compareTo(o2.object.getPid().toString());
            }
        });
    }

    private void orderCursorsForUpdate(final List<Item<Cursor>> items) {
        Collections.sort(items, new Comparator<Item<Cursor>>() {
            @Override
            public int compare(Item<Cursor> o1, Item<Cursor> o2) {
                return o1.object.getClass().getName().compareTo(o2.object.getClass().getName());
            }
        });
    }

    /**
     *
     * @param sectionId - if null then clear all sections
     */
    private void discardAllNewObjects(final Long sectionId) {
        visitAllNewEntities(sectionId, new EntityVisitor() {
            @Override
            public void visit(final Entity ent) {
                ent.discard();
            }
        });
    }

    public static interface EntityVisitor {

        void visit(Entity ent);
    }

    /**
     *
     * @param sectionId - if null then clear all sections
     */
    public final void visitAllNewEntities(final Long sectionId, final EntityVisitor visitor) {
        final BufferedPool.ERegistrationMode oldMode = newObjects.getRegistrationMode();
        try {
            newObjects.setRegistrationMode(BufferedPool.ERegistrationMode.SHEDULE);
            Collection<Item<Entity>> shedulled = newObjects.getRegistered();
            int i = 0;
            while (!shedulled.isEmpty()) {
                if (++i > MAX_POOL_PASSAGE_COUNT) {
                    throw new IllegalUsageError("Can't visit all new objects. " + String.valueOf(MAX_UPDATE_DB_ITER_COUNT) + " attempts were made but new objects pool enlarged time and again");
                }
                for (Item<Entity> it : shedulled) {
                    if (sectionId == null || sectionId.equals(it.section)) {
                        visitor.visit(it.object);
                    }
                }
                if (oldMode == BufferedPool.ERegistrationMode.IMMEDIATELY) // RADIX-2545
                {
                    shedulled = newObjects.flush();
                } else {
                    break;
                }
            }
        } finally {
            newObjects.setRegistrationMode(oldMode);
        }
    }

    /**
     *
     * @param sectionId - if null then clear all sections
     */
    public final void visitAllUsedExistingEntities(final Long sectionId, EntityVisitor visitor) {
        if (arte.isInTransaction()) {
            final ExistingObjectsPool existingObjects = arte.getDefManager().releaseCache.getExistingObjects();
            final BufferedPool.ERegistrationMode oldMode = existingObjects.getRegistrationMode();
            try {
                existingObjects.setRegistrationMode(BufferedPool.ERegistrationMode.SHEDULE);
                Collection<EntityCacheItem> shedulled = existingObjects.getRegistered();
                int i = 0;
                while (!shedulled.isEmpty()) {
                    if (++i > MAX_POOL_PASSAGE_COUNT) {
                        throw new IllegalUsageError("Can't visit all existing objects. " + String.valueOf(MAX_UPDATE_DB_ITER_COUNT) + " attempts were made but objects modified time and again");
                    }
                    for (Item<Entity> it : shedulled) {
                        if (sectionId == null || sectionId.equals(it.section)) {
                            visitor.visit(it.object);
                        }
                    }
                    if (oldMode == BufferedPool.ERegistrationMode.IMMEDIATELY) // RADIX-2545
                    {
                        shedulled = existingObjects.flush();
                    } else {
                        break;
                    }
                }
            } finally {
                existingObjects.setRegistrationMode(oldMode);
            }
        } else {
            throw new IllegalUsageError("ARTE seance is not started");
        }
    }

    public void prepareForTransaction() {
        clear(null);//DBP-1582 to clear all old keptInCached objects
        entityModifications.clear();
        expectedCacheSize = DEFAULT_EXPECTED_CACHE_SIZE;
    }

    public void setExpectedCacheSize(int expectedCacheSize) {
        this.expectedCacheSize = expectedCacheSize;
    }

    public int getExpectedCacheSize() {
        return expectedCacheSize;
    }

    /**
     *
     * @param sectionId - if null then clear all sections
     */
    public void clear(final Long sectionId) {
        clear(sectionId, System.currentTimeMillis());
    }

    /**
     *
     * @param sectionId - if null then clear all sections
     * @param timeMillis - all keptInCache objects with expiration time less
     * then time millis will be removed
     */
    public void clear(final Long sectionId, final long timeMillis) {
        if (arte.isInTransaction()) {//in ARTE transaction
            visitAllUsedExistingEntities(sectionId, new EntityVisitor() {

                @Override
                public void visit(final Entity ent) {
                    if (ent.getCanBeRemovedFromCache(timeMillis)) {
                        ent.discard();
                    }
                }
            });
            discardAllNewObjects(sectionId);
            final BufferedPool.ERegistrationMode oldMode = cursors.getRegistrationMode();
            try {
                cursors.setRegistrationMode(BufferedPool.ERegistrationMode.SHEDULE);
                Collection<Item<Cursor>> shedulled = cursors.getRegistered();
                int i = 0;
                while (!shedulled.isEmpty()) {
                    if (++i > MAX_POOL_PASSAGE_COUNT) {
                        throw new IllegalUsageError("Can't close all cursors. " + String.valueOf(MAX_UPDATE_DB_ITER_COUNT) + " attempts were made but existing objects pool enlarged time and again");
                    }
                    for (Item<Cursor> it : shedulled) {
                        if (sectionId == null || sectionId.equals(it.section)) {
                            it.object.close();
                        }
                    }
                    if (oldMode == BufferedPool.ERegistrationMode.IMMEDIATELY) // RADIX-2545
                    {
                        shedulled = cursors.flush();
                    } else {
                        break;
                    }
                }
            } finally {
                cursors.setRegistrationMode(oldMode);
            }

        } else {
            if (!newObjects.isEmpty()) {
                arte.getTrace().put(EEventSeverity.ERROR, "ARTE seance not started but new objects cache is not empty", EEventSource.ARTE);
            }
            if (!cursors.isEmpty()) {
                arte.getTrace().put(EEventSeverity.ERROR, "ARTE seance not started but cursors cache is not empty", EEventSource.ARTE);
            }
        }
        if (!tmpStmts.isEmpty()) {
            final Iterator<Item<Statement>> it = tmpStmts.iterator();
            while (it.hasNext()) {
                final Item<Statement> item = it.next();
                if (sectionId == null || sectionId.equals(item.section)) {
                    it.remove();
                    try {
                        item.object.close();
                    } catch (SQLException e) {
                        arte.getTrace().put(EEventSeverity.WARNING, "Can't free temporary Statement: " + arte.getTrace().exceptionStackToString(e), EEventSource.ARTE);
                    }
                }
            }
        }
        if (!tmpBlobs.isEmpty()) {
            getArte().getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_LOB_TMP_FREE);
            try {
                final Iterator<Item<Blob>> it = tmpBlobs.iterator();
                while (it.hasNext()) {
                    final Item<Blob> item = it.next();
                    if (sectionId == null || sectionId.equals(item.section)) {
                        it.remove();
                        try {
                            if (arte.getRadixConnection().isTemporaryBlob(item.object)) {
                                arte.getRadixConnection().freeTemporaryBlob(item.object);
                            }
                        } catch (SQLException e) {
                            //isTemporary throws exception if BLOB already freed
                            //arte.getTrace().put(EEventSeverity.WARNING, "Can't free temporary BLOB: " + arte.getTrace().exceptionStackToString(e), EEventSource.ARTE);
                        }
                    }
                }
            } finally {
                getArte().getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_LOB_TMP_FREE);
            }
        }
        if (!tmpClobs.isEmpty()) {
            getArte().getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_LOB_TMP_FREE);
            try {
                final Iterator<Item<Clob>> it = tmpClobs.iterator();

                while (it.hasNext()) {
                    final Item<Clob> item = it.next();
                    if (sectionId == null || sectionId.equals(item.section)) {
                        it.remove();
                        try {
                            if (arte.getRadixConnection().isTemporaryClob(item.object)) {
                                arte.getRadixConnection().freeTemporaryClob(item.object);
                            }
                        } catch (SQLException e) {
                            //isTemporary throws exception if BLOB already freed
//                            arte.getTrace().put(EEventSeverity.WARNING, "Can't free temporary CLOB: " + arte.getTrace().exceptionStackToString(e), EEventSource.ARTE);
                        }
                    }
                }
            } finally {
                getArte().getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_LOB_TMP_FREE);
            }
        }
        if (sectionId == null) {
            sections.clear();
        }
    }

    void registerTemporaryClob(Clob clob) throws SQLException {
        tmpClobs.add(new Item<Clob>(clob, curSection()));
        if (arte.isInTransaction()) { //если мы не в транзакции, то не регистрируем (если не будет endTransaction, этот объект никогда не уберется ни нами, ни GC)
            if (tmpClobs.size() > LOBS_SUSPECTED_COUNT) {
                final List<Item<Clob>> oldTmpClobs = tmpClobs;
                tmpClobs = new ArrayList<Item<Clob>>(128);
                for (Item<Clob> it : oldTmpClobs) {
                    try {
                        if (arte.getRadixConnection().isTemporaryClob(it.object)) {
                            tmpClobs.add(it);
                        }
                    } catch (SQLException exception) {
                        //Ignore CLOB object that was freed 
                    }
                }
                if (tmpClobs.size() > LOBS_SUSPECTED_COUNT) {
                    arte.getTrace().put(EEventSeverity.WARNING, "Temporary CLOB count is " + String.valueOf(tmpClobs.size()), EEventSource.ARTE);
                }
            }
        }
    }

    void registerTemporaryBlob(Blob blob) throws SQLException {
        if (arte.isInTransaction()) { //если мы не в транзакции, то не регистрируем (если не будет endTransaction, этот объект никогда не уберется ни нами, ни GC)
            tmpBlobs.add(new Item<Blob>(blob, curSection()));
            if (tmpBlobs.size() > LOBS_SUSPECTED_COUNT) {
                final List<Item<Blob>> oldTmpBlobs = tmpBlobs;
                tmpBlobs = new ArrayList<Item<Blob>>(128);
                for (Item<Blob> it : oldTmpBlobs) {
                    try {
                        if (arte.getRadixConnection().isTemporaryBlob(it.object)) {
                            tmpBlobs.add(it);
                        }
                    } catch (SQLException exception) {
                        //Ignore BLOB object that was freed 
                    }
                }
                if (tmpBlobs.size() > LOBS_SUSPECTED_COUNT) {
                    arte.getTrace().put(EEventSeverity.WARNING, "Temporary BLOB count is " + String.valueOf(tmpBlobs.size()), EEventSource.ARTE);
                }
            }
        }
    }

    void freeTemporaryBlobs(final Collection<java.sql.Blob> blobs) {
        if (blobs == null) {
            return;
        }
        for (Blob blob : blobs) {
            try {
                arte.getRadixConnection().freeTemporaryBlob(blob);
            } catch (SQLException ex) {
//                    //ignore
            }
        }
        final Iterator<Item<Blob>> it = tmpBlobs.iterator();
        while (it.hasNext()) {
            if (blobs.contains(it.next().object)) {
                it.remove();
            }
        }
    }

    void freeTemporaryClobs(final Collection<java.sql.Clob> clobs) {
        if (clobs == null) {
            return;
        }
        for (java.sql.Clob clob : clobs) {
            try {
                arte.getRadixConnection().freeTemporaryClob(clob);
            } catch (SQLException ex) {
//                    //ignore
            }
        }
        final Iterator<Item<Clob>> it = tmpClobs.iterator();
        while (it.hasNext()) {
            if (clobs.contains(it.next().object)) {
                it.remove();
            }
        }
    }

    void registerTemporaryStatement(final Statement stmt) {
        if (arte.isInTransaction()) //если мы не в транзакции, то не регистрируем (если не будет endTransaction, этот объект никогда не уберется ни нами, ни GC)
        {
            tmpStmts.add(new Item<Statement>(stmt, curSection()));
        }
    }

    void unregisterTemporaryStatement(final Statement stmt) {
        tmpStmts.remove(new Item<Statement>(stmt, null));
    }

//savepoints    
    void onSetSavepoint(final String id) {
        {
            final BufferedPool.ERegistrationMode oldMode = newObjects.getRegistrationMode();
            try {
                newObjects.setRegistrationMode(BufferedPool.ERegistrationMode.SHEDULE);
                Collection<Item<Entity>> shedulled = newObjects.getRegistered();
                int i = 0;
                while (!shedulled.isEmpty()) {
                    if (++i > MAX_POOL_PASSAGE_COUNT) {
                        throw new IllegalUsageError("Can't prepare all new objects for savepoint setting. " + String.valueOf(MAX_UPDATE_DB_ITER_COUNT) + " attempts were made but new objects pool enlarged time and again");
                    }
                    for (Item<Entity> it : shedulled) {
                        it.object.onSetSavepoint(id);
                    }
                    if (oldMode == BufferedPool.ERegistrationMode.IMMEDIATELY) // RADIX-2545
                    {
                        shedulled = newObjects.flush();
                    } else {
                        break;
                    }
                }
            } finally {
                newObjects.setRegistrationMode(oldMode);
            }
        }
        {
            final ExistingObjectsPool existingObjects = arte.getDefManager().releaseCache.getExistingObjects();
            final BufferedPool.ERegistrationMode oldMode = existingObjects.getRegistrationMode();
            try {
                existingObjects.setRegistrationMode(BufferedPool.ERegistrationMode.SHEDULE);
                Collection<EntityCacheItem> shedulled = existingObjects.getRegistered();
                int i = 0;
                while (!shedulled.isEmpty()) {
                    if (++i > MAX_POOL_PASSAGE_COUNT) {
                        throw new IllegalUsageError("Can't prepare all existing objects for savepoint setting. " + String.valueOf(MAX_UPDATE_DB_ITER_COUNT) + " attempts were made but existing objects pool enlarged time and again");
                    }
                    for (Item<Entity> it : shedulled) {
                        it.object.onSetSavepoint(id);
                    }
                    if (oldMode == BufferedPool.ERegistrationMode.IMMEDIATELY) // RADIX-2545
                    {
                        shedulled = existingObjects.flush();
                    } else {
                        break;
                    }
                }
            } finally {
                existingObjects.setRegistrationMode(oldMode);
            }
        }
    }

    void beforeRollbackToSavepoint(final String id) {
        visitAllNewEntities(null, new Cache.EntityVisitor() {
            @Override
            public void visit(final Entity ent) {
                ent.beforeRollbackToSavepoint(id);
            }
        });
        final ExistingObjectsPool existingObjects = arte.getDefManager().releaseCache.getExistingObjects();
        final BufferedPool.ERegistrationMode oldMode = existingObjects.getRegistrationMode();
        try {
            existingObjects.setRegistrationMode(BufferedPool.ERegistrationMode.SHEDULE);
            Collection<EntityCacheItem> shedulled = existingObjects.getRegistered();
            int i = 0;
            while (!shedulled.isEmpty()) {
                if (++i > MAX_POOL_PASSAGE_COUNT) {
                    throw new IllegalUsageError("Can't prepare all existing objects for rollback to savepoint. " + String.valueOf(MAX_UPDATE_DB_ITER_COUNT) + " attempts were made but existing objects pool enlarged time and again");
                }
                for (Item<Entity> it : shedulled) {
                    it.object.beforeRollbackToSavepoint(id);
                }
                if (oldMode == BufferedPool.ERegistrationMode.IMMEDIATELY) // RADIX-2545
                {
                    shedulled = existingObjects.flush();
                } else {
                    break;
                }
            }
        } finally {
            existingObjects.setRegistrationMode(oldMode);
        }
    }

    void afterRollbackToSavepoint(final String id) {
        final ExistingObjectsPool existingObjects = arte.getDefManager().releaseCache.getExistingObjects();
        final BufferedPool.ERegistrationMode oldMode = existingObjects.getRegistrationMode();
        try {
            try {
                existingObjects.setRegistrationMode(BufferedPool.ERegistrationMode.SHEDULE);
                for (Item<Entity> it : existingObjects.getRegistered()) {
                    if (!it.object.autonomousStoreAfterRollback(id)) {
                        if (it.object.isCreatedAfterSavePoint(id)) {
                            it.object.discard();
                        } else {
                            it.object.purgePropCacheOnRollback(id);
                        }
                    }
                    it.object.afterRollbackToSavepoint(id);
                    //так как afterRollbackToSavepoint не должен вызываться у вновь зарегистрированных объектов
                    //цикл не делаем
                }
            } finally {
                existingObjects.setRegistrationMode(oldMode);
            }
            visitAllNewEntities(null, new Cache.EntityVisitor() {
                @Override
                public void visit(final Entity ent) {
                    if (ent.isInstantiatedAfterSavePoint(id)) {
                        if (ent.autonomousStoreAfterRollback(id)) {
                            registerExistingEntityObject(ent);
                            ent.afterRollbackToSavepoint(id);
                        } else {
                            ent.discard();
                        }
                    }
                }
            });
        } finally {
            clearModificationRecords(getArte().calcSpNesting(id));
        }
    }

    public int getEntityModificationsCount() {
        return entityModifications.size();
    }

    private void clearModificationRecords(final long savepointLevel) {
        if (savepointLevel == 0) {
            entityModifications.clear();
            return;
        }
        final List<Entity> toRemoveFromModified = new ArrayList<>();
        for (Map.Entry<Entity, List<EntityModification>> entry : entityModifications.entrySet()) {
            if (entry.getValue() != null) {

                List<EntityModification> modifications = entry.getValue();
                for (int i = 0; i < modifications.size(); i++) {
                    if (modifications.get(i).savepointNesting >= savepointLevel) {
                        while (modifications.size() > i) {
                            modifications.remove(i);
                        }
                        if (modifications.isEmpty()) {
                            toRemoveFromModified.add(entry.getKey());
                        }
                        break;
                    }
                }
            } else {
                toRemoveFromModified.add(entry.getKey());
            }
        }
        for (Entity toRemove : toRemoveFromModified) {
            entityModifications.remove(toRemove);
        }
    }

    void afterCommit() {
        final Map<Entity, List<EntityModification>> modificationsMap = new HashMap<>();
        modificationsMap.putAll(entityModifications);
        entityModifications.clear();

        for (Map.Entry<Entity, List<EntityModification>> entry : modificationsMap.entrySet()) {
            final List<EntityModification> modifications = entry.getValue();
            if (modifications != null && !modifications.isEmpty()) {
                if (modifications.get(modifications.size() - 1).type == EntityModification.EModificationType.DELETE) {
                    continue;
                }
                boolean isCreated = false;
                for (EntityModification modification : modifications) {
                    if (modification.type == EntityModification.EModificationType.CREATE) {
                        isCreated = true;
                    }
                }
                final Entity entity = entry.getKey();
                String title = entity.getClass().getName();
                try {
                    try {
                        title = entity.getRadMeta().getTitle() + "[Class ID: " + entity.getRadMeta().getId() + ", PID: " + String.valueOf(entry.getKey().getPid()) + "]";
                    } catch (Throwable t) {
                        //ignore
                    }
                    final Id classGuidColumnId = getClassGuidColumnId(entity.getPid());
                    final EntityPropVals propVals = new EntityPropVals();
                    if (classGuidColumnId != null) {
                        propVals.putPropValById(classGuidColumnId, entity.getRadMeta().getId().toString());
                    }
                    final Entity twin = getEntityObject(entity.getPid(), propVals, false);
                    EntityMethodAccessor.afterCommit(twin, isCreated);
                } catch (Throwable t) {
                    arte.getTrace().put(Messages.MLS_ID_ERROR_ON_AFTER_COMMIT, new ArrStr(title, ExceptionTextFormatter.throwableToString(t)), false);
                }
            }
        }
    }

    void afterRollback() {
        if (arte.isInTransaction()) {
            try {
                final ExistingObjectsPool existingObjects = arte.getDefManager().releaseCache.getExistingObjects();
                final BufferedPool.ERegistrationMode oldMode = existingObjects.getRegistrationMode();
                try {
                    existingObjects.setRegistrationMode(BufferedPool.ERegistrationMode.SHEDULE);
                    Collection<EntityCacheItem> shedulled = existingObjects.getRegistered();
                    int i = 0;
                    while (!shedulled.isEmpty()) {
                        if (++i > MAX_POOL_PASSAGE_COUNT) {
                            throw new IllegalUsageError("Can't purge all loaded props after rollback. " + String.valueOf(MAX_UPDATE_DB_ITER_COUNT) + " attempts were made but existing objects pool enlarged time and again");
                        }
                        for (Item<Entity> it : shedulled) {
                            if (!it.object.autonomousStoreAfterRollback(null)) {
                                if (it.object.isCreatedAfterSavePoint(null)) {
                                    it.object.discard();
                                } else {
                                    it.object.purgePropCacheOnRollback(null);
                                }
                            }
                        }
                        if (oldMode == BufferedPool.ERegistrationMode.IMMEDIATELY) // RADIX-2545
                        {
                            shedulled = existingObjects.flush();
                        } else {
                            break;
                        }
                    }
                } finally {
                    existingObjects.setRegistrationMode(oldMode);
                }
                visitAllNewEntities(null, new Cache.EntityVisitor() {
                    @Override
                    public void visit(final Entity ent) {
                        if (ent.autonomousStoreAfterRollback(null)) {
                            registerExistingEntityObject(ent);
                        } else {
                            ent.discard();
                        }
                    }
                });
            } finally {
                clearModificationRecords(0);
            }
        }
    }

    protected Arte getArte() {
        return arte;
    }

    public boolean isRegistered(final Entity ent) {
        if (ent.isInDatabase(false)) {
            return arte.getDefManager().releaseCache.getExistingObjects().isRegistered(ent);
        } else {
            return newObjects.isRegistered(new Item<>(ent, null));
        }
    }

    public boolean isRegistered(final Pid pid) {
        return arte.getDefManager().releaseCache.getExistingObjects().getByPid(pid) != null;
    }

    public Entity getRegisteredExistingEntityObject(Pid pid) {
        ExistingObjectsPool.Item item = arte.getDefManager().releaseCache.getExistingObjects().getByPid(pid);
        return item == null ? null : item.getObject();
    }

    static class Item<T> extends Object {

        final T object;
        final Long section;
        final String creationStack = getCreationString();

        Item(
                final T object,
                final Long section) {
            if (object == null) {
                throw new IllegalUsageError("Try to cache null reference");
            }
            this.object = object;
            this.section = section;
        }

        @Override
        public boolean equals(final Object o) {
            if (o instanceof Item) {
                return object.equals(((Item) o).object);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return object.hashCode();
        }

        @Override
        public String toString() {
            return "Item[" + object.toString() + "]";
        }

        private static String getCreationString() {
            if (Cache.STORE_CACHE_OPERATION_STACKS) {
                Arte arte = Arte.get();
                return (arte != null ? "Trace contexts: " + arte.getTrace().getContextStackAsStr() + "\n" : "") + ExceptionTextFormatter.getCurrentStackAsStr();

            }
            return null;
        }

    }

    public static class EntityCacheItem extends Item<Entity> {

        private static final Entity FAKE_NULL_ENTITY = new Entity(null, true) {

            @Override
            public RadClassDef getRadMeta() {
                return null;
            }

        };
        final Pid pid;
        final long arteTranSeq;

        public EntityCacheItem(Entity object, Long section) {
            super(object, section);
            this.pid = object.getPid();
            this.arteTranSeq = object.getArte().getTransactionSeqNumber();
        }

        /**
         * Fictive constructor to perform search by pid in
         * {@code HashMap<EntityCacheItem>}
         *
         * @param entityPid
         */
        public EntityCacheItem(Pid entityPid) {
            super(FAKE_NULL_ENTITY, null);
            this.pid = entityPid;
            this.arteTranSeq = 0;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 79 * hash + Objects.hashCode(this.pid);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final EntityCacheItem other = (EntityCacheItem) obj;
            if (!Objects.equals(this.pid, other.pid)) {
                return Objects.equals(this.object, other.object);
            }
            return true;
        }

        @Override
        public String toString() {
            return "EntityCacheItem[" + pid.getEntityId() + "~" + pid.toString() + "]";
        }

    }

    private static class EntityModification {

        public static enum EModificationType {

            CREATE,
            UPDATE,
            DELETE;
        }
        public final long savepointNesting;
        public final EModificationType type;

        public EntityModification(long savepointNesting, EModificationType type) {
            this.savepointNesting = savepointNesting;
            this.type = type;
        }
    }
}
