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

package org.radixware.kernel.common.client.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.radixware.kernel.common.client.meta.filters.RadCommonFilter;
import org.radixware.kernel.common.client.meta.explorerItems.RadSelectorExplorerItemDef;
import org.radixware.kernel.common.client.meta.explorerItems.RadChildRefExplorerItemDef;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.eas.RequestHandle;
import org.radixware.kernel.common.client.eas.SelectRequestHandle;
import org.radixware.kernel.common.client.errors.ModelCreationError;
import org.radixware.kernel.common.client.errors.NoDefinitionWithSuchIdError;
import org.radixware.kernel.common.client.errors.ObjectNotFoundError;
import org.radixware.kernel.common.client.errors.UniqueConstraintViolationError;
import org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException;
import org.radixware.kernel.common.client.exceptions.CommonFilterIsObsoleteException;
import org.radixware.kernel.common.client.exceptions.CommonFilterNotFoundException;
import org.radixware.kernel.common.client.exceptions.InvalidPropertyValueException;
import org.radixware.kernel.common.client.exceptions.InvalidSortingException;
import org.radixware.kernel.common.client.exceptions.PropertyIsMandatoryException;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.RadClassPresentationDef;
import org.radixware.kernel.common.client.meta.RadCommandDef;
import org.radixware.kernel.common.client.meta.RadEditorPresentationDef;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.RadSortingDef;
import org.radixware.kernel.common.client.meta.RadUnknownSortingDef;
import org.radixware.kernel.common.client.meta.explorerItems.RadSelectorUserExplorerItemDef;
import org.radixware.kernel.common.client.models.groupsettings.Filters;
import org.radixware.kernel.common.client.models.groupsettings.Sortings;
import org.radixware.kernel.common.client.models.items.SelectorColumnModelItem;
import org.radixware.kernel.common.client.models.items.SelectorColumns;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.models.items.properties.PropertyValue;
import org.radixware.kernel.common.client.tree.IExplorerTreeManager;
import org.radixware.kernel.common.client.types.GroupRestrictions;
import org.radixware.kernel.common.client.types.InstantiatableClass;
import org.radixware.kernel.common.client.types.InstantiatableClasses;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.views.IProgressHandle;
import org.radixware.kernel.common.client.views.ISelector;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.EPropNature;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.ESelectionMode;
import org.radixware.kernel.common.exceptions.IllegalArgumentError;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.scml.SqmlExpression;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.schemas.eas.ColorScheme;
import org.radixware.schemas.eas.DeleteRejections;
import org.radixware.schemas.eas.DeleteRs;
import org.radixware.schemas.eas.ExceptionEnum;
import org.radixware.schemas.eas.SelectMess;
import org.radixware.schemas.eas.SelectRs;

public abstract class GroupModel extends Model implements IPresentationChangedHandler {
    
    public static abstract class SelectionListener{
        public boolean beforeChangeSelection(final EntityObjectsSelection oldSelection, final EntityObjectsSelection newSelection){
            return true;
        }
        public void afterSelectionChanged(final EntityObjectsSelection selection){
            
        }
    }
    
    private final static int DEFAULT_READ_PAGE_SIZE = 50;
    
    private static final class EntitiesArray extends ArrayList<EntityModel>{
        
        private static final long serialVersionUID = 3289768308026489884L;
        private final Map<Pid,EntityModel> entitiesByPid = new HashMap<>(128);
        
        public EntitiesArray(){
            super();
        }
        
        public EntitiesArray(final int initialCapacity){
            super(initialCapacity);
        }
        
        public EntitiesArray(final Collection<EntityModel> c){
            super(c.size());
            addAll(c);
        }

        @Override
        public EntityModel set(final int index, final EntityModel element) {
            final EntityModel model = entitiesByPid.get(element.getPid());
            if (model==null || indexOf(model)==index){
                final EntityModel previous = super.set(index, element);
                entitiesByPid.put(element.getPid(), element);
                return previous;
            }
            return null;
        }

        @Override
        public boolean add(final EntityModel model) {
            if (model==null || entitiesByPid.containsKey(model.getPid())){
                return false;
            }
            if (super.add(model)){
                entitiesByPid.put(model.getPid(), model);
                return true;
            }
            return false;
        }

        @Override
        public void add(final int index, final EntityModel model) {
            if (model!=null && !entitiesByPid.containsKey(model.getPid())){
                super.add(index, model);
                entitiesByPid.put(model.getPid(), model);
            }
        }

        @Override
        public EntityModel remove(int index) {
            final EntityModel model = get(index);
            super.remove(index);
            entitiesByPid.remove(model.getPid());
            return  model;
        }

        @Override
        public boolean remove(final Object o) {
            if (super.remove(o)){
                entitiesByPid.remove(((EntityModel)o).getPid());
                return true;
            }
            return false;
        }

        @Override
        public void clear() {
            super.clear();
            entitiesByPid.clear();
        }

        @Override
        public boolean addAll(final Collection<? extends EntityModel> c) {
            boolean result = false;
            for (EntityModel model: c){
                result = add(model) || result;
            }
            return result;
        }

        @Override
        public boolean addAll(int index, final Collection<? extends EntityModel> c) {            
            final int sizeBefore = size();
            for (EntityModel model: c){
                add(index,model);
                index++;
            }
            return sizeBefore!=size();
        }

        @Override
        protected void removeRange(int fromIndex, int toIndex) {
            for (int i=fromIndex; i>toIndex; i--){
                remove(i);
            }
        }

        @Override
        public boolean removeAll(final Collection<?> c) {
            boolean result = false;
            for (Object model: c){
                result = result || remove(model);
            }
            return result;
        }

        @Override
        public boolean retainAll(final Collection<?> c) {
            final int sizeBefore = size();
            for (int i=size()-1; i>=0; i--){
                if (!c.contains(get(i))){
                    remove(i);
                }
            }
            return sizeBefore!=size();
        }
        
        public EntityModel findEntityModelByPid(final Pid pid){
            return entitiesByPid.get(pid);
        }
        
        @Override
        public int indexOf(final Object o){
            if (o instanceof Pid){
                final EntityModel model = entitiesByPid.get(o);
                return model==null ? -1 : super.indexOf(model);
            }else{
                return super.indexOf(o);
            }
        }
    }
    
    private org.radixware.schemas.xscml.Sqml condition = null;
    private EntitiesArray entities = null;
    private int startReadIndex=1;
    private int readPageSize = DEFAULT_READ_PAGE_SIZE;
    private boolean hasMoreRows = true;
    private boolean beforeRead;
    private List<Id> commandIds = null;
    private SelectorColumns selectorColumns = null;    
    private GroupRestrictions restrictions = null;   
    private FilterModel filter;
    private Long filterChangeTimestamp;
    private Filters filters;    
    private RadSortingDef sorting;
    private Sortings sortings;
    final List<ProxyGroupModel> linkedGroups = new ArrayList<>();    
    List<InstantiatableClass> instantiatableClasses;
    private IEntitySelectionController selectionController;
    private SelectRs bufferedResponce;
    private boolean settingsWasRead;
    private final GroupModelAsyncReader asyncReader;
    private final EntityObjectsSelection selection;
    private Set<Integer> rowsWhenReadEntireObject;
    private Set<Pid> pidsWhenReadEntireObject;
    private List<SelectionListener> selectionListeners;

    public GroupModel(IClientEnvironment environment, RadSelectorPresentationDef def) {
        super(environment, def);        
        asyncReader = new GroupModelAsyncReader(this); // 'this' leakage
        selection = new EntityObjectsSelection(this);
    }

    @Override
    public void clean() {
        if (selectorColumns!=null) {
            selectorColumns.storeSettings();
        }
        super.clean();
        clearRows();
        startReadIndex = 1;
        asyncReader.clean();
        removeProcessedHandles(SelectMess.class);
        selectorColumns = null;
        setHasMoreRows(true);
        if (restrictions != null) {
            restrictions.clear();
        }
        condition = null;
        if (filters != null) {
            filters.saveAll();
            filters.invalidate();
        }
        if (sortings!=null){
            sortings.saveAll();
            sortings.invalidate();
        }
        filter = null;
        filterChangeTimestamp = null;
        sorting = null;
        instantiatableClasses = null;
        for (int i = linkedGroups.size() - 1; i >= 0; i--) {
            linkedGroups.get(i).clean();
        }
        linkedGroups.clear();
        selectionListeners = null;
        selection.internalClear();
    }

    public RadSelectorPresentationDef getSelectorPresentationDef() {
        return (RadSelectorPresentationDef) getDefinition();
    }

    public ISelector getGroupView() {
        return (ISelector) getView();
    }
    
    @Override
    public void setView(final IView view_) {
        final boolean changingRestrictions;
        if (view_==null && getView()!=null){
            for (ProxyGroupModel linkedGroupModel: linkedGroups){
                if (linkedGroupModel.getView()==getView()){
                    linkedGroupModel.setView(null);
                }
            }            
            changingRestrictions = getView().getRestrictions()!=null &&
                    getView().getRestrictions().greaterThan(getRestrictions());
        }else if (view_!=null){
            changingRestrictions = view_.getRestrictions().greaterThan(getRestrictions());
        }else{
            changingRestrictions = false;
        }
        super.setView(view_);
        if (changingRestrictions)
            onChangeRestriction();
    }    

    public FilterModel getCurrentFilter() {
        return filter;
    }
    
    public RadSortingDef getCurrentSorting(){
        return sorting;
    }

    public IContext.Group getGroupContext() {
        return (IContext.Group) getContext();
    }

    public Filters getFilters() {
        if (filters == null && getContext() != null) {
            filters = new Filters(this);
        }
        return filters;
    }
    
    public Sortings getSortings(){
        if (sortings == null && getContext() !=null ){
            sortings = new Sortings(this);
        }
        return sortings;
    }

    public boolean hasMoreRows() {
        return hasMoreRows;
    }
    
    protected void setHasMoreRows(final boolean hasMore){
        hasMoreRows = hasMore;
    }

    public SelectorColumns getSelectorColumns() {
        if (selectorColumns==null)
            selectorColumns = new SelectorColumns(this);
        return selectorColumns;
    }

    public SelectorColumnModelItem getSelectorColumn(final Id propertyId) {
        if (selectorColumns == null) {
            selectorColumns = new SelectorColumns(this);
        }
        return selectorColumns.getColumnByPropertyId(propertyId);
    }

//Открытие связанных моделей
    //Получить модель строки
    public EntityModel getEntity(final int i) throws BrokenEntityObjectException, ServiceClientException, InterruptedException {
        if (i < 0) {
            return null;
        }
        while (i >= getEntitiesCount()) {
            if (hasMoreRows()) {
                final int oldCount = getEntitiesCount();
                final int oldStartIndex = startReadIndex;
                readMore();
                if (hasMoreRows() && oldCount==getEntitiesCount() && oldStartIndex==startReadIndex){
                    final String message = 
                        getEnvironment().getMessageProvider().translate("TraceMessage", "Unable to read group model");
                    getEnvironment().getTracer().warning(message);
                    return null;
                }
            } else {
                return null;
            }
        }
        final EntityModel entity = getRow(i);
        if (entity instanceof BrokenEntityModel){
            throw new BrokenEntityObjectException((BrokenEntityModel)entity);
        }
        return entity;
    }
    
    public boolean isBrokenEntity(final int i){
        return (getRow(i) instanceof BrokenEntityModel);
    }

    public int readToEntity(final Pid pid) throws InterruptedException, ServiceClientException {
        return readToEntity(pid, null);
    }

    public int readToEntity(final Pid pid, final String progressTitle) throws InterruptedException, ServiceClientException {
        if (getEntitiesCount() == 0 && !hasMoreRows()) {
            return -1;
        }
        final int idx = findEntityByPid(pid);
        if (idx >= 0) {
            return idx;
        }
        int i = getEntitiesCount();
        final IProgressHandle progressHandle;
        if (progressTitle != null) {
            progressHandle = getEnvironment().getProgressHandleManager().newStandardProgressHandle();
            progressHandle.startProgress(progressTitle, true);
        } else {
            progressHandle = null;
        }

        try {
            EntityModel entity;
            for (; true; i++) {
                try{
                    entity = getEntity(i);
                }
                catch(BrokenEntityObjectException exception){
                    if (Utils.equals(pid, exception.getPid())){
                        return i;
                    }
                    else{
                        continue;
                    }
                }
                if (entity==null){
                    break;
                }
                if (entity.getPid().equals(pid)) {
                    return i;
                }
                if (progressHandle != null && progressHandle.wasCanceled()) {
                    return -1;
                }
            }
        } finally {
            if (progressHandle != null) {
                progressHandle.finishProgress();
            }
        }
        return -1;
    }

    public int findEntityByPid(final Pid pid) {
        if (pid == null || isEmpty() || entities==null) {
            return -1;
        }        
        return entities.indexOf(pid);
    }

    @Override
    public EntityModel onChangePresentation(final RawEntityModelData rawData,
            final Id newPresentationClassId,
            final Id newPresentationId) {
        final Pid pid = new Pid(getSelectorPresentationDef().getTableId(), rawData.getEntityObjectData().getPID());
        final int idx = findEntityByPid(pid);
        if (idx >= 0) {
            final EntityModel oldEntity = getRow(idx);
            try {
                final RadEditorPresentationDef epd = getEnvironment().getDefManager().getEditorPresentationDef(newPresentationId);
                final IContext.SelectorRow ctx = new IContext.SelectorRow(this);
                ctx.setPresentationChangedHandler(this);
                final EntityModel entity = epd.createModel(ctx);
                entity.activate(rawData);
                entity.parentModel = this;
                if (selectorColumns != null) {
                    //check if all selector-column properties was activated in new entity model
                    Property property;
                    for (SelectorColumnModelItem column : selectorColumns) {
                        property = entity.getProperty(column.getId());
                        if (!property.isActivated()) {
                            /*if selector-column was not activated
                            (no corresponding property was registered in editor page)
                            then copy property value from old entity model
                             */
                            assert oldEntity.getProperty(column.getId()).isActivated();

                            final PropertyValue value = new PropertyValue(property.getDefinition(), oldEntity.getProperty(column.getId()).getServerValObject());
                            value.setReadonly(true);
                            property.setServerValue(value);
                        }
                    }
                }
                setRow(idx, entity);                
                return entity;
            } finally {
                oldEntity.clean();
            }
        }
        return null;
    }

    /**
     * Метод создает модель новой сущности внутри данной группы. Модель новой сущности будет создана
     * на основе презентации {@link RadSelectorPresentationDef#getCreationPresentation()}
     * <p>Если идентификатор класса не задан, а в группе допускается наличие сущностей,
     * принадлежащих разным классам, то будет вызван диалог выбора класса. Если задан параметр
     * src, то параметр classId можно не указывать.
     * Когда идентификатор класса не удается определить или не указана презентация для создания
     * объекта, генерируется {@link ModelCreationError}.
     * <p>Для завершения создания новой сущности необходим вызов {@link EntityModel#create()}
     * @param classId идентификатор класса новой сущности
     * @param src Если указан, то будет создана копия сущности src.
     * @return Модель новой сущности.
     * @throws ServiceClientException ошибки при выполнении запроса на получение списка классов.
     * @throws InterruptedException отмена запроса на получение списка возможных классов
     * @see EntityModel#openPrepareCreateModel(EditorPresentationDef, String, EntityModel, org.radixware.kernel.explorer.models.Context.Entity)
     */
    public EntityModel openCreatingEntity(Id classId, final EntityModel src, final Map<Id, Object> initialValues) throws ServiceClientException, InterruptedException {        
        if (classId == null) {
            //Определение класса создаваемого объекта
            classId = onSelectCreationClass();
            if (classId == null) {
                try {
                    classId = selectCreationClass();
                } catch (InterruptedException ex) {
                    return null;//Создание было отменено
                }
                assert classId != null;
            }
        }
        final IContext.InSelectorCreating context = new IContext.InSelectorCreating(this);
        if (getSelectorPresentationDef().getCreationPresentation() == null) {
            final String msg = getEnvironment().getMessageProvider().translate("ExplorerError", "creating presentation was not defined");
            throw new ModelCreationError(ModelCreationError.ModelType.ENTITY_MODEL_FOR_NEW, getDefinition(), context, msg);
        }
        return EntityModel.openPrepareCreateModel(getSelectorPresentationDef().getCreationPresentation(),
                classId, src,
                getSelectorPresentationDef().getCreationPresentationIds(),
                initialValues, context);
    }

    public EntityModel openCreatingEntity(Id classId, final EntityModel src) throws ServiceClientException, InterruptedException {
        return openCreatingEntity(classId, src, null);
    }

    /**
     * Создать модель группы для селектора независимой таблицы
     * @param parent  Модель родительской сущности (если есть)
     * @param holder  Модель дефиниция которой непосредственно является родительской
     * 				  для explorerItemDef
     * @param explorerItemDef Элемент проводника по которому будет открываться селектор.
     * @return Модель группы для селектора дочерней таблицы
     */
    public static GroupModel openTableSelectorModel(IClientEnvironment environment, EntityModel parent, Model holder, RadSelectorExplorerItemDef explorerItemDef) {
        if (holder == null) {
            throw new IllegalArgumentError("holder entity was not defined");
        }
        if (explorerItemDef == null) {
            throw new IllegalArgumentError("explorer item was not defined");
        }
        if (parent != null && parent.isNew()) {
            final String message = "Cannot create group model based on explorer item #%s\n in context of not existing entity \"%s\"";
            throw new IllegalArgumentError(String.format(message, explorerItemDef.getId(), parent.getTitle()));
        }
        final IContext.TableSelect ctx = new IContext.TableSelect(environment, parent, holder, explorerItemDef);
        final GroupModel m = explorerItemDef.getModelDefinition().createModel(ctx);
        final String msg = "group model \"%s\" based on explorer item #%s was created";
        holder.getEnvironment().getTracer().put(EEventSeverity.DEBUG, String.format(msg, m.getTitle(), explorerItemDef.getId()), EEventSource.EXPLORER);
        return m;
    }

    /**
     * Создать модель группы для селектора дочерней таблицы
     * @param parent  Модель родительской сущности. Обязательный параметр.
     * @param holder  Модель сущности, дефиниция которой непосредственно является родительской
     * 				  для explorerItemDef. Обязательный параметр.
     * @param explorerItemDef Дочерний элемент проводника. Обязательный параметр.
     * @return Модель группы для селектора дочерней таблицы
     * @see #openTableSelectorModel(EntityModel, Model, TableExplorerItemDef)
     */
    public static GroupModel openChildTableSelectorModel(IClientEnvironment environment, EntityModel parent, Model holder, RadChildRefExplorerItemDef explorerItemDef) {
        if (parent == null) {
            throw new IllegalArgumentError("parent entity was not defined");
        }
        if (holder == null) {
            throw new IllegalArgumentError("holder entity was not defined");
        }
        if (explorerItemDef == null) {
            throw new IllegalArgumentError("explorer item was not defined");
        }
        if (parent.isNew()) {
            final String message = "Cannot create child group model based on explorer item #%s\n for not existing parent entity \"%s\"";
            throw new IllegalArgumentError(String.format(message, explorerItemDef.getId(), parent.getTitle()));
        }

        final IContext.ChildTableSelect ctx = new IContext.ChildTableSelect(environment, parent, holder, explorerItemDef);
        final GroupModel m = explorerItemDef.getModelDefinition().createModel(ctx);
        final String msg = "child group model \"%s\" based on explorer item #%s was created for parent entity \"%s\"";
        holder.getEnvironment().getTracer().put(EEventSeverity.DEBUG, String.format(msg, m.getTitle(), explorerItemDef.getId(), parent.getTitle()), EEventSource.EXPLORER);
        return m;
    }
    
    static GroupModel openChildTableSelectorModel(IClientEnvironment environment, EntityModel parent, Model holder, RadSelectorUserExplorerItemDef explorerItemDef) {
        if (parent == null) {
            throw new IllegalArgumentError("parent entity was not defined");
        }
        if (holder == null) {
            throw new IllegalArgumentError("holder entity was not defined");
        }
        if (explorerItemDef == null) {
            throw new IllegalArgumentError("explorer item was not defined");
        }
        if (explorerItemDef.getTargetExplorerItem() instanceof RadChildRefExplorerItemDef==false){
            throw new IllegalArgumentError("actual explorer item must be instance of RadChildRefExplorerItemDef class");
        }
        if (parent.isNew()) {
            final String message = "Cannot create child group model based on explorer item #%s\n for not existing parent entity \"%s\"";
            throw new IllegalArgumentError(String.format(message, explorerItemDef.getId(), parent.getTitle()));
        }        
        final IContext.ChildTableSelect ctx = new IContext.ChildTableSelect(environment, parent, holder, explorerItemDef);
        final GroupModel m = explorerItemDef.getModelDefinition().createModel(ctx);
        final String msg = "child group model \"%s\" based on explorer item #%s was created for parent entity \"%s\"";
        holder.getEnvironment().getTracer().put(EEventSeverity.DEBUG, String.format(msg, m.getTitle(), explorerItemDef.getId(), parent.getTitle()), EEventSource.EXPLORER);
        return m;
    }    

    public static GroupModel openTableContextlessSelectorModel(final IClientEnvironment environment, final Id classId, final Id presentationId) {
        final RadSelectorPresentationDef presentation = environment.getDefManager().getSelectorPresentationDef(presentationId);
        return openTableContextlessSelectorModel(environment, presentation, null);
    }
    
    public static GroupModel openTableContextlessSelectorModel(final Model holderModel, final Id classId, final Id presentationId) {
        final RadSelectorPresentationDef presentation = holderModel.getEnvironment().getDefManager().getSelectorPresentationDef(presentationId);
        return openTableContextlessSelectorModel(holderModel.getEnvironment(), presentation, holderModel);
    }    
    
    public static GroupModel openTableContextlessSelectorModel(final IClientEnvironment environment, final RadSelectorPresentationDef presentation) {
        return openTableContextlessSelectorModel(environment, presentation, null);
    }
    
    public static GroupModel openTableContextlessSelectorModel(final Model holderModel, final RadSelectorPresentationDef presentation) {
        return openTableContextlessSelectorModel(holderModel.getEnvironment(), presentation, holderModel);
    }    

    private static GroupModel openTableContextlessSelectorModel(final IClientEnvironment environment, final RadSelectorPresentationDef presentation, final Model holderModel) {
        final IContext.ContextlessSelect context;
        if (holderModel==null){
            context = new IContext.ContextlessSelect(environment,presentation);
        }else{
            context = new IContext.ContextlessSelect(holderModel, presentation);
        }        
        final GroupModel result = presentation.createModel(context);
        final String msg = "group model \"%s\" based on selector presentaion #%s was created";
        environment.getTracer().put(EEventSeverity.DEBUG, String.format(msg, result.getTitle(), presentation.getId()), EEventSource.EXPLORER);
        return result;
    }

    /**
     * Перечитывает сущности в группе.
     * Очищает группу и читает первую порцию данных.
     * @throws org.radixware.kernel.common.exceptions.ServiceClientException
     * @throws java.lang.InterruptedException
     */
    public void reread() throws ServiceClientException {
        if (getView() != null) {
            getGroupView().reread();
        } else {
            reset();
        }
    }
    
    public SelectRequestHandle readMoreAsync() {
        return readMoreAsync(0, startReadIndex, readPageSize);
    }
      
    public SelectRequestHandle readMoreAsync(final int timeoutSec) {
        return readMoreAsync(timeoutSec, startReadIndex, readPageSize);
    }
    
    private SelectRequestHandle readMoreAsync(final int timeoutSec, final int startIndex, final int count) {
        if (getGroupView() != null && count>0) {
            throw new IllegalUsageError("Asynchronous read is not allowed when group view is opened");
        }
        final Collection<RequestHandle> handles = getRequestHandles(SelectMess.class);
        for (RequestHandle handle : handles) {
            if (((SelectRequestHandle) handle).isSearchForCurrentEntity()) {
                return (SelectRequestHandle) handle;
            }
        }
        if (startIndex==1){
            beforeRead = true;
            try{
                beforeReadFirstPage();
            }finally{
                beforeRead = false;
            }
        }
        final boolean requestSelectorAddons = startIndex==1 || count==0;
        final SelectRequestHandle handle =
                RequestHandle.Factory.createForSelect(this, startIndex, count, requestSelectorAddons);        
        handle.addListener(this);        
        getEnvironment().getEasSession().sendAsync(handle, timeoutSec);
        return handle;
    }    

    public void reset() {
        for (ProxyGroupModel linkedGroup : linkedGroups) {
            linkedGroup.invalidate();
        }
        clearRows();
        instantiatableClasses = null;
        startReadIndex = 1;
        asyncReader.reset();
        removeProcessedHandles(SelectMess.class);
        setHasMoreRows(true);
    }
    
    private boolean wasRead(){
        return !hasMoreRows() || getEntitiesCount()>0;
    }

    public void setCondition(org.radixware.schemas.xscml.Sqml condition) throws ServiceClientException, InterruptedException {
        this.condition = condition != null ? (org.radixware.schemas.xscml.Sqml) condition.copy() : null;
        if (!beforeRead){
            if (getView() != null) {
                try{
                    getGroupView().reread(null);                
                }catch (CommonFilterNotFoundException exception) {
                    showException(exception);
                    final FilterModel defaultFilter = getFilters().getDefaultFilter();
                    try {
                        if (defaultFilter == null || !defaultFilter.canApply() || !applyFilter(defaultFilter)) {
                            applyFilter(null);
                        }
                    } catch (InterruptedException ex) {
                    }
                } catch (Exception ex) {
                    showException(ex);
                }
            } else if (wasRead()) {
                reread();
            }
            selection.clear();
        }     
    }
    
    private boolean applyFilter(final FilterModel filter) throws InterruptedException {
        try {
            setFilter(filter);
            return true;
        } catch (PropertyIsMandatoryException | InvalidPropertyValueException exception) {
            return false;
        } catch (ServiceClientException exception) {
            getEnvironment().getTracer().error(exception);
            return false;
        }
    }


    public void setCondition(SqmlExpression expression) throws ServiceClientException, InterruptedException {
        setCondition(expression != null ? expression.asXsqml() : null);
    }

    public void removeCondition() throws ServiceClientException, InterruptedException {
        setCondition((org.radixware.schemas.xscml.Sqml) null);
    }
    
    private void applyGroupSettingsImpl(final FilterModel newFilter, final RadSortingDef newSorting) throws PropertyIsMandatoryException, InvalidPropertyValueException, ServiceClientException, InterruptedException {
        if (filters == null) {
            if (getContext() == null) {
                throw new IllegalStateException("It is not allowed to set filter for group model with no context");
            } else {
                filters = new Filters(this);
            }
        }
        if (sortings == null){
            if (getContext() == null) {
                throw new IllegalStateException("It is not allowed to set sorting for group model with no context");
            } else {
                sortings = new Sortings(this);
            }            
        }
        if (newFilter != null && !newFilter.isValid()) {
            if (newFilter.isCommon()){
                throw new CommonFilterNotFoundException(getEnvironment(), (RadCommonFilter)newFilter.getFilterDef());
            }
            else{
                throw new IllegalArgumentError("Cannot apply invalid filter \"" + newFilter.getName() + "\"");
            }
        }
        if (newSorting!=null && !newSorting.isValid()){
            throw new IllegalArgumentError("Cannot apply invalid sorting \"" + newSorting.getName() + "\"");
        }
        if (newFilter != null) {
            newFilter.finishEdit();
            newFilter.checkPropertyValues();
            if (!newFilter.beforeApply()) {
                return;
            }
        }
        if (getView()!=null){
            finishEdit();
            if (!getGroupView().canChangeCurrentEntity(false)){
                return;
            }
        }
        if (getGroupContext().getRootGroupContext()!=null && getEntitiesCount()==0 && getView()==null){
            //Настройка дочерней модели группы в древовидном селекторе
            filter = newFilter;
            filterChangeTimestamp = filter==null ? null : filter.getChangeTimestamp();
            sorting = newSorting;
            return;
        }
        //TWRBS-1555
        //Делаем предварительное чтение чтобы сразу узнать
        //валидный фильтр или нет.
        final FilterModel oldFilter = filter;
        final Long oldFilterChangeTimestamp = filterChangeTimestamp;
        final boolean needToClearSelection = oldFilter!=newFilter
            || (oldFilter!=null && oldFilter.isPropertyValueEdited())
            || (oldFilter==newFilter && newFilter!=null && !Objects.equals(filterChangeTimestamp, filter.getChangeTimestamp()));        
        filter = newFilter;
        filterChangeTimestamp = filter==null ? null : filter.getChangeTimestamp();
        final RadSortingDef oldSorting = sorting;
        final List<InstantiatableClass> oldInstantiatableClasses = instantiatableClasses;
        sorting = newSorting;
        instantiatableClasses = null;
        SelectRs response = null;
        final boolean readEntireObjectAtFirstRow;
        if (getView()!=null 
            && !getRestrictions().getIsEditorRestricted() 
            && (rowsWhenReadEntireObject==null || !rowsWhenReadEntireObject.contains(Integer.valueOf(0)))){
            if (rowsWhenReadEntireObject==null){
                rowsWhenReadEntireObject = new HashSet<>();
            }
            rowsWhenReadEntireObject.add(Integer.valueOf(0));
            readEntireObjectAtFirstRow = true;
        }else{
            readEntireObjectAtFirstRow = false;
        }
        try{
            try {
                response =  readMoreImpl(1,readPageSize, needToClearSelection);
            }
            finally {
                if (response == null) {//wasException
                    filter = oldFilter;
                    filterChangeTimestamp = oldFilterChangeTimestamp;
                    sorting = oldSorting;
                    instantiatableClasses = oldInstantiatableClasses;
                } else {
                    bufferedResponce = response;
                }
            }
            if (getView() != null) {            
                getGroupView().leaveCurrentEntity(true);
                getGroupView().reread(null);
                getGroupView().repaint();

            } else {
                reset();
            }
        }finally{
            if (readEntireObjectAtFirstRow && rowsWhenReadEntireObject!=null){
                rowsWhenReadEntireObject.remove(Integer.valueOf(0));
                if (rowsWhenReadEntireObject.isEmpty()){
                    rowsWhenReadEntireObject = null;
                }
            }
        }
        if (newFilter != null) {
            filters.setAsLastUsed(newFilter);
            newFilter.setIsPropertyValueEdited(false);
            newFilter.afterApply();
        }        
        if (newSorting != null){
            sortings.setAsLastUsed(sorting);
        }
    }

    public void setFilter(final FilterModel newFilter) throws PropertyIsMandatoryException, InvalidPropertyValueException, ServiceClientException, InterruptedException {
        applyGroupSettingsImpl(newFilter,getCurrentSorting());
    }
    
    public void setSorting(final RadSortingDef newSorting) throws ServiceClientException, InterruptedException{
        try{
            applyGroupSettingsImpl(getCurrentFilter(), newSorting);
        }
        catch(PropertyIsMandatoryException | InvalidPropertyValueException exception){
            showException(exception);
        }
                
    }
    
    public void applySettings(final FilterModel newFilter, final RadSortingDef newSorting) throws PropertyIsMandatoryException, InvalidPropertyValueException, ServiceClientException, InterruptedException {
        applyGroupSettingsImpl(newFilter, newSorting);
    }

    public void setColorScheme(ColorScheme cs) throws ServiceClientException, InterruptedException {
    }

    public org.radixware.schemas.xscml.Sqml getCondition() {
        return condition != null ? (org.radixware.schemas.xscml.Sqml) condition.copy() : null;
    }

    private Id selectCreationClass() throws ServiceClientException, InterruptedException {
        final RadSelectorPresentationDef presentation = getSelectorPresentationDef();
        if (!presentation.getClassPresentation().isClassCatalogExists()) {

            /*             //RADIX-3002
            if (getContext() instanceof Context.TableSelect){
            final Context.TableSelect context = (Context.TableSelect)getContext();
            if (context.explorerItemDef.getModelDefinitionClassId()!=null)
            return context.explorerItemDef.getModelDefinitionClassId();
            }*/
            return presentation.getOwnerClassId();
        }        
        final List<InstantiatableClass> classes;
        if (instantiatableClasses==null || instantiatableClasses.isEmpty()){
            final Id entityId = presentation.getTableId();
            classes = InstantiatableClasses.getClasses(getEnvironment(), entityId, ((IContext.Group) getContext()).toXml());            
            instantiatableClasses = new ArrayList<>();
            for (InstantiatableClass item: classes){
                instantiatableClasses.add(item.clone());
            }
        }else{
            classes = new ArrayList<>(instantiatableClasses.size());
            for (InstantiatableClass item: instantiatableClasses){
                classes.add(item.clone());
            }
        }
        if (!beforePresentClassList(classes)) {
            throw new InterruptedException();
        }

        final IWidget parentWidget = getGroupView() == null ? getEnvironment().getMainWindow() : getGroupView();
        final InstantiatableClass cl = 
            InstantiatableClasses.selectClass(getEnvironment(), 
                                              parentWidget, 
                                              classes, 
                                              presentation.getId().toString(),
                                              presentation.autoSortInstantiatableClasses());

        if (cl == null) {
            throw new InterruptedException();
        }
        return cl.getId();
    }

    public boolean deleteAll(final boolean forced) throws ServiceClientException, InterruptedException {
        if (!beforeDeleteAll()) {
            return false;
        }
        final String title = getEnvironment().getMessageProvider().translate("ExplorerMessage", "Confirm Object Deletion"), msg = getEnvironment().getMessageProvider().translate("ExplorerMessage", "Do you really want to clear \'%s\'?");
        if (forced || getEnvironment().messageConfirmation(title, String.format(msg, getTitle()))) {
            if (deleteImpl(false, forced)) {                
                final Collection<Pid> pids;
                if (getEntitiesCount()>0) {
                    pids = new ArrayList<>(getEntitiesCount());
                    final List<EntityModel> allRows = getAllRows();
                    if (getSelectorPresentationDef().getTableId().equals(getEnvironment().getClipboard().getTableId())) {
                        getEnvironment().getClipboard().remove(allRows);
                    }

                    if (getEnvironment().getTreeManager() != null) {
                        for (EntityModel entity : allRows) {
                            pids.add(entity.getPid());
                        }
                    }
                    clearRows();
                    startReadIndex = 1;
                    asyncReader.clean();
                }else{
                    pids = Collections.emptyList();
                }
                //then invoke event
                afterDeleteAll();
                //and finally synchronize explorer tree
                if (!pids.isEmpty() && getEnvironment().getTreeManager() != null) {
                    final IExplorerTreeManager tree = getEnvironment().getTreeManager();
                    tree.afterEntitiesRemoved(pids, getView());
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Id> getAccessibleCommandIds() {
        if (commandIds == null) {
            if (getContext() instanceof IContext.ContextlessSelect) {
                commandIds = Collections.emptyList();
            } else {
                final Collection<RadCommandDef> commandDefs = getSelectorPresentationDef().getEnabledCommands();
                commandIds = new ArrayList<>(commandDefs.size());
                for (RadCommandDef command : commandDefs) {
                    if (!getContext().getRestrictions().getIsCommandRestricted(command.getId())
                        && isCommandAccessible(command)
                       ) {
                        commandIds.add(command.getId());
                    }
                }
            }
        }
        return Collections.unmodifiableList(commandIds);
    }
    
    public final org.radixware.schemas.eas.PropertyList writePropertiesToXml(final org.radixware.schemas.eas.PropertyList xml){
        final Collection<Property> props = getActiveProperties();
        final org.radixware.schemas.eas.PropertyList propertyList;
        if (xml==null){
            propertyList = org.radixware.schemas.eas.PropertyList.Factory.newInstance();
        }else{
            propertyList = xml;
        }
        for (Property property : props) {
            if (!property.isLocal() && property.getDefinition().getNature()== EPropNature.GROUP_PROPERTY){
                property.writeValue2Xml(propertyList.addNewItem());
            }
        }
        return propertyList;
    }    
    
// События

    protected Id onSelectCreationClass() {
        return null;
    }

    protected boolean beforePresentClassList(List<InstantiatableClass> classes) {
        return true;
    }

    protected boolean beforeDeleteAll() {
        return true;
    }

    protected void afterDeleteAll() {
    }

    protected void afterRead(final List<EntityModel> newEntities, final List<Id> serverDisabledCommands, final EnumSet<ERestriction> serverRestrictions) {
    }
    
    protected void beforeReadFirstPage(){        
    }

// Вспомогательные методы
    protected void readMore() throws ServiceClientException, InterruptedException {
        if (getEntitiesCount()==0 && bufferedResponce != null) {
            try {
                processResponse(bufferedResponce,true);
            } finally {
                bufferedResponce = null;
            }
        } else {
            bufferedResponce = null;
            final boolean needToClearSelection = startReadIndex==1 
                                                 && filter!=null 
                                                 && !Objects.equals(filter.getChangeTimestamp(), filterChangeTimestamp);
            final SelectRs response = readMoreImpl(startReadIndex, readPageSize, needToClearSelection);
            if (needToClearSelection){
                filterChangeTimestamp = filter==null ? null : filter.getChangeTimestamp();
            }
            processResponse(response, true);
        }
    }

    private SelectRs readMoreImpl(final int startIndex, final int count, final boolean clearSelection) throws ServiceClientException, InterruptedException {
        removeProcessedHandles(SelectMess.class);
        asyncReader.clean();
        if (startReadIndex==1){
            beforeRead = true;
            try{
                beforeReadFirstPage();
            }finally{
                beforeRead = false;
            }
        }
        final boolean requestSelectorAddons = startIndex==1 || count==0;
        final boolean requestInstantiatableClasses = 
            instantiatableClasses==null && getSelectorPresentationDef().getClassPresentation().isClassCatalogExists();
        final SelectRs response;
        try {
            response = 
                getEnvironment().getEasSession().select(this, startIndex, count, requestSelectorAddons, requestInstantiatableClasses);
        } catch (ServiceCallFault fault) {
            final ServiceCallFault preprocessedFault = processServiceCallFault(fault);
            if (preprocessedFault instanceof CommonFilterIsObsoleteException){
                final CommonFilterIsObsoleteException filterIsObsolete = 
                                                        (CommonFilterIsObsoleteException)preprocessedFault;
                if (!filterIsObsolete.isParametersChanged() && startIndex==1){
                    return readMoreImpl(1, count, true);
                }
            }
            throw preprocessedFault;
        }
        if (clearSelection){
            selection.clear();
        }
        return response;
    }
    
    public void readCommonSettings() throws ServiceClientException, InterruptedException{
        final SelectRs response = readMoreImpl(0,0,false);
        processResponse(response, false);
    }
    
    public SelectRequestHandle readCommonSettingsAsync(){
        return readMoreAsync(0, 0, 0);
    }
    
    public boolean settingsWasRead(){
        return settingsWasRead;
    }

    private void processResponse(final SelectRs rs, 
                                 final boolean isRowsRequested) {
        GroupModelData selectResult = new SelectResponseParser(this).parse(rs);
        final List<EntityModel> newEntities = new LinkedList<>();
        final List<EntityModel> selectedEntities = selectResult.getEntityModels();
        startReadIndex += selectedEntities.size();
        for(EntityModel e : selectedEntities) {
            // exclude broken entites for afterRead
            if(!(e instanceof BrokenEntityModel)) {
                newEntities.add(e);
            }
            
            addRow(e);
        }
        
        if (selectResult.getCommonFilters()!=null){
            getFilters().setCommonFilters(selectResult.getCommonFilters());
        }
        
        final EnumSet<ERestriction> srvRestrictions = selectResult.getRestrictions();
        final List<Id> srvDisabledCommands = selectResult.getDisabledCommands();
        
        if (isRowsRequested){
            final Id filterId = selectResult.getFilterId();
            if (filterId != null && (filter == null || !filterId.equals(filter.getId()))) {
                filter = getFilters().findById(filterId);
                if (filter == null) {
                    throw new NoDefinitionWithSuchIdError(getDefinition(), NoDefinitionWithSuchIdError.SubDefinitionType.FILTER, filterId);
                }
                else{
                    filter.afterApply();
                }
            }

            final Id sortingId = selectResult.getSortingId();
            if (sortingId != null && (sorting == null || !sortingId.equals(sorting.getId()))) {
                sorting = getSortings().findById(sortingId);
                if (sorting == null) {
                    getEnvironment().getTracer().error(new NoDefinitionWithSuchIdError(getDefinition(), NoDefinitionWithSuchIdError.SubDefinitionType.SORTING, sortingId));
                    sorting = new RadUnknownSortingDef(sortingId);
                }            
            }

            setHasMoreRows(selectResult.hasMore());
            afterRead(newEntities, srvDisabledCommands, srvRestrictions);
        }
        
        setServerDisabledCommands(srvDisabledCommands);
        restrictions.setServerRestrictions(srvRestrictions);//this call will refresh group view
        final List<InstantiatableClass> classes = selectResult.getInstantiatableClasses();
        if (classes!=null){
            instantiatableClasses = classes;
        }
        settingsWasRead = true;
    }

    @Override
    public void onResponseReceived(XmlObject response, RequestHandle handle) {
        if ((response instanceof SelectRs) && (handle instanceof SelectRequestHandle)) {
            final SelectRequestHandle selectRequestHandle = (SelectRequestHandle)handle;
            if (getView() == null || selectRequestHandle.getRowCount()==0) {
                processResponse((SelectRs) response, true);
            }
        } else {
            super.onResponseReceived(response, handle);
        }
    }

    public GroupRestrictions getRestrictions() {
        return restrictions;
    }

    @Override
    public void setContext(final IContext.Abstract context) {
        if (context instanceof IContext.Group){
            super.setContext(((IContext.Group)context).copy());
        }else{
            super.setContext(context);
        }
        if (context != null) {
            restrictions = new GroupRestrictions(this);
        }
    }        

    //вызывается после удаления объекта - строки группы
    public void removeRow(final int row) {
        if (row >= 0 && row < entities.size()) {
            final Pid pid = entities.get(row).getPid();
            for (ProxyGroupModel linkedGroup : linkedGroups) {
                if (linkedGroup.mapEntityIndexFromSource(row) >= 0
                        && linkedGroup.getGroupView() != null && linkedGroup.getGroupView() != getGroupView()) {
                    linkedGroup.getGroupView().entityRemoved(pid);
                }
            }
            entities.remove(row);
            startReadIndex--;
            for (ProxyGroupModel linkedGroup : linkedGroups) {
                linkedGroup.invalidate();
            }
            //entities.remove(row);
        }
    }
    
    protected void addRows(final List<EntityModel> entities){
        if (entities!=null && !entities.isEmpty()){
            if (this.entities==null){
                this.entities = new EntitiesArray(entities);
            }else{
                this.entities.addAll(entities);
            }
        }
    }
    
    protected void removeRows(final List<EntityModel> entities){
        if (this.entities!=null){
            this.entities.removeAll(entities);
        }
    }
    
    protected void addRow(final EntityModel entity){
        if (entity!=null){
            if (entities==null){
                entities = new EntitiesArray(getReadPageSize());
            }
            entities.add(entity);
        }
    }
    
    protected EntityModel getRow(final int row){
        return entities==null || row>=entities.size() || row<0 ? null : entities.get(row);
    }
    
    protected void setRow(final int row, final EntityModel entity){
        if (entities==null){
            entities = new EntitiesArray(getReadPageSize());            
        }
        entities.set(row, entity);
    }
    
    protected List<EntityModel> getAllRows(){
        return entities==null ? Collections.<EntityModel>emptyList() : Collections.unmodifiableList(entities);
    }
    
    protected void clearRows(){
        if (entities!=null){
            entities.clear();
            entities = null;
        }
    }

    public int getEntitiesCount() {
        return entities == null ? 0 : entities.size();
    }

    public boolean isEmpty() {
        return entities != null ? entities.isEmpty() && !hasMoreRows() : true;
    }

    private boolean deleteImpl(final boolean cascade, final boolean forced) throws ServiceClientException, InterruptedException {
        try {
            getEnvironment().getEasSession().deleteContext(this, cascade);
        } catch (ServiceCallFault fault) {
            if (fault.getFaultString().equals(ExceptionEnum.CONFIRM_SUBOBJECTS_DELETE.toString())) {
                final String title = getEnvironment().getMessageProvider().translate("ExplorerMessage", "Confirm Object Deletion");
                if (forced || getEnvironment().messageConfirmation(title, fault.getMessage())) {
                    return deleteImpl(true, forced);
                }
                return false;
            }
            throw processServiceCallFault(fault);
        }
        return true;
    }

    @Override
    public void showException(final String title, final Throwable ex) {        
        final Throwable exceptionToProcess;
        if (ex instanceof ServiceCallFault) {
            final ServiceCallFault fault = (ServiceCallFault) ex;
            if (fault.getFaultString().equals(ExceptionEnum.FILTER_IS_OBLIGATORY.toString()) && getView() != null && getGroupView().disable()) {
                final String messageTitle = getEnvironment().getMessageProvider().translate("ExplorerMessage", "Please Select Filter");
                final String messageText = getEnvironment().getMessageProvider().translate("ExplorerMessage", "It is necessary to use filter in this selector");
                getEnvironment().messageInformation(messageTitle, messageText);
                return;
            }
            else if (!(fault instanceof CommonFilterIsObsoleteException) && fault.getFaultString().equals(ExceptionEnum.FILTER_IS_OBSOLETE.toString())){
                exceptionToProcess = processServiceCallFault(fault);
            }
            else if (!(fault instanceof CommonFilterNotFoundException) && fault.getFaultString().equals(ExceptionEnum.FILTER_NOT_FOUND.toString())){
                exceptionToProcess = processServiceCallFault(fault);
            }            
            else{
                exceptionToProcess = ex;
            }
        }
        else{
            exceptionToProcess = ex;
        }

        ObjectNotFoundError objectNotFound = null;
        CommonFilterIsObsoleteException filterIsObsolete = null;
        UniqueConstraintViolationError ucvError = null;
        for (Throwable err = exceptionToProcess; err != null && err.getCause() != err; err = err.getCause()) {
            if (err instanceof ObjectNotFoundError) {
                objectNotFound = (ObjectNotFoundError) err;
            }
            else if (err instanceof CommonFilterIsObsoleteException){
                filterIsObsolete = (CommonFilterIsObsoleteException)err;
            }else if (err instanceof UniqueConstraintViolationError) {
                ucvError = (UniqueConstraintViolationError) err;
            }
        }
        
        if (filterIsObsolete!=null && getGroupView()!=null){
            final MessageProvider mp = getEnvironment().getMessageProvider();
            final String messageTitle = title==null || title.isEmpty() ? filterIsObsolete.getTitle(mp) : title;
            getEnvironment().messageInformation(messageTitle, filterIsObsolete.getLocalizedMessage(mp));
            final FilterModel filterModel = getFilters().findById(filterIsObsolete.getNewFilter().getId());
            if (filterModel==null){
                getGroupView().disable();
            }
            else{
                final boolean withoutParameters = filterModel.getFilterDef().getParameters().isEmpty();
                getGroupView().setCurrentFilter(filterModel, withoutParameters);
            }
        }else if (objectNotFound != null) {
            //RADIX-3051
            super.showException(title, objectNotFound);
            if (objectNotFound.inContextOf(this)) {
                final Pid pid = objectNotFound.getPid();
                final int idx = findEntityByPid(pid);
                if (getView() != null) {
                    getGroupView().entityRemoved(pid);

                } else if (idx >= 0) {
                    removeRow(idx);
                }

                if (getEnvironment().getTreeManager() != null) {
                    //синхронизация дерева
                    final IExplorerTreeManager tree = getEnvironment().getTreeManager();
                    tree.afterEntitiesRemoved(Collections.singletonList(pid), getGroupView());
                }
            }
        } else if (ucvError != null && ucvError.inContextOf(this) ){
            getEnvironment().messageError(title, ucvError.getMessage(this));
        }else {
            super.showException(title, exceptionToProcess);
        }
    }

    private ServiceCallFault processServiceCallFault(ServiceCallFault fault) {
        if (fault.getFaultString().equals(ExceptionEnum.FILTER_IS_OBSOLETE.toString())) {
            final org.radixware.schemas.eas.CommonFilter commonFilterXml;
            try{
                commonFilterXml = 
                    org.radixware.schemas.eas.CommonFilter.Factory.parse(fault.getMessage());
            }
            catch(XmlException exception){
                getEnvironment().processException(exception);
                return fault;
            }
              
            if (getCurrentFilter()!=null && (getCurrentFilter().getFilterDef() instanceof RadCommonFilter)){                
                final RadCommonFilter commonFilter = (RadCommonFilter)getCurrentFilter().getFilterDef();
                if (commonFilter.getId().equals(commonFilterXml.getId())){
                    final CommonFilterIsObsoleteException exception = new CommonFilterIsObsoleteException(getEnvironment(), fault, commonFilter);
                    commonFilter.actualize(commonFilterXml.getBaseFilterId(),
                                           commonFilterXml.getTitle(), 
                                           commonFilterXml.getCondition(),                                           
                                           commonFilterXml.getParameters(),
                                           commonFilterXml.getLastUpdateTime()
                                           );                
                    return exception;
                }
            }            
        }
        else if (fault.getFaultString().equals(ExceptionEnum.FILTER_NOT_FOUND.toString())){
            if (getCurrentFilter()!=null && (getCurrentFilter().getFilterDef() instanceof RadCommonFilter)){
                final RadCommonFilter commonFilter = (RadCommonFilter)getCurrentFilter().getFilterDef();                                                                
                commonFilter.setWasRemoved();
                return new CommonFilterNotFoundException(getEnvironment(), fault, commonFilter);
            }                        
        }
        else if (fault.getFaultString().equals(ExceptionEnum.INVALID_SORTING.toString())){
            return new InvalidSortingException(getEnvironment(), fault, filter, sorting);
        }
        return fault;
    }

    protected Id getEditorPresentationIdForChoosenEntity(EntityModel entity) {
        return entity.getEditorPresentationIdForChooseIntoTree();
    }

    public final boolean isAuditEnabled() {
        return getSelectorPresentationDef().getClassPresentation().isAuditEnabled()
                && getEnvironment().canViewAuditTable();
    }

    public final void setEntitySelectionController(final IEntitySelectionController controller) {
        selectionController = controller;
    }

    public IEntitySelectionController getEntitySelectionController() {
        return new IEntitySelectionController() {

            @Override
            public boolean isEntityChoosable(EntityModel entity) {
                if (selectionController != null && !selectionController.isEntityChoosable(entity)) {
                    return false;
                }
                return !getRestrictions().getIsEntityRestricted(entity.getPid());
            }                        
        };
    }
    
    public boolean canOpenGroupView(){
        return getSelectorPresentationDef().getRuntimeEnvironmentType() == ERuntimeEnvironmentType.COMMON_CLIENT
               || getSelectorPresentationDef().getRuntimeEnvironmentType() == getEnvironment().getApplication().getRuntimeEnvironmentType();

    }
    
    public void onChangeRestriction(){//RADIX-6074
        
    }    
    
    void mergeEntities(List<EntityModel> models, List<Id> disabledCommands, EnumSet<ERestriction> serverRestrictions) {
        final List<EntityModel> resultWithoutBroken = new LinkedList<>();
        final ISelector groupView = getGroupView();
        final EntityModel currentEntity = groupView == null ? null : groupView.getCurrentEntity();
        final AbstractReaderController reader = asyncReader.getReaderController();
        final List<EntityModel> mergedEntities = 
            new LinkedList<>(reader.merge(getAllRows(), models, currentEntity));
        clearRows();
        addRows(mergedEntities);
        setHasMoreRows(false);
        for(EntityModel e : getAllRows()) {
            if(!(e instanceof BrokenEntityModel)) {
                resultWithoutBroken.add(e);
            }
        }
        
        afterRead(resultWithoutBroken, disabledCommands, serverRestrictions);
    }
    
    public GroupModelAsyncReader getAsyncReader() {
        return asyncReader;
    }
    
    public final int getReadPageSize(){
        return readPageSize;
    }
    
    public final void setReadPageSize(final int newReadPageSize){
        if (newReadPageSize<=0){
            throw new IllegalArgumentException("read page size cannot be less or equal zero");
        }
        readPageSize = newReadPageSize;
    }
    
    public static int listContainsModel(final List<EntityModel> listOfModels, final EntityModel model) {
        final Pid pid = model.getPid();
        for(int i = 0; i < listOfModels.size(); i++) {
            if(listOfModels.get(i).getPid().equals(pid)) {
                return i;
            }
        }
        return -1;
    }
    
    public boolean canPaste(final EntityModel entityModel){
        if (entityModel==null || entityModel instanceof BrokenEntityModel){
            return false;
        }
        final RadClassPresentationDef objectClassDef = entityModel.getClassPresentationDef();
        if (instantiatableClasses==null) {            
            final Id actualClassId;
            if (getContext() instanceof IContext.TableSelect) {
                final IContext.TableSelect context =
                        (IContext.TableSelect)getContext();
                actualClassId = context.explorerItemDef.getModelDefinitionClassId();
            } else {
                actualClassId = getSelectorPresentationDef().getOwnerClassId();
            }
            final RadClassPresentationDef actualClassDef = 
                getEnvironment().getDefManager().getClassPresentationDef(actualClassId);                        
            return objectClassDef.getId().equals(actualClassDef.getId()) || actualClassDef.isAncestorOf(objectClassDef);
        }else{
            for (InstantiatableClass instantiatableClass: instantiatableClasses){
                if (objectClassDef.getId().equals(instantiatableClass.getId())
                    || instantiatableClass.findDerivedClassByIdRecursively(objectClassDef.getId())!=null
                   ){
                    return true;
                }
            }
            return false;
        }
    }
    
    public final void setRowsWhenReadEntireObject(final Set<Integer> rows){
        if (rows==null || rows.isEmpty()){
            rowsWhenReadEntireObject = null;
        }else{
            rowsWhenReadEntireObject = new HashSet<>(rows);
        }
    }

    public final void setPidsWhenReadEntireObject(final Set<Pid> pids){
        if (pids==null || pids.isEmpty()){
            pidsWhenReadEntireObject = null;
        }else{
            pidsWhenReadEntireObject = new HashSet<>(pids);
        }
    }

    public final Set<Integer> getRowsWhenReadEntireObject(){
        return rowsWhenReadEntireObject==null ? Collections.<Integer>emptySet() : Collections.unmodifiableSet(rowsWhenReadEntireObject);
    }

    public final Set<Pid> getPidsWhenReadEntireObject(){        
        return pidsWhenReadEntireObject==null ? Collections.<Pid>emptySet() : Collections.unmodifiableSet(pidsWhenReadEntireObject);
    }
 
    public final EntityObjectsSelection getSelection(){
        return selection;
    }
    
    public void addSelectionListener(final SelectionListener handler){
        if (handler!=null){
            if (selectionListeners==null){
                selectionListeners = new LinkedList<>();
            }
            if (!selectionListeners.contains(handler)){
                selectionListeners.add(handler);
            }
        }
    }
    
    public void removeSelectionListener(final SelectionListener handler){
        if (handler!=null && selectionListeners!=null){
            selectionListeners.remove(handler);
            if (selectionListeners.isEmpty()){
                selectionListeners = null;
            }
        }
    }
    
    final boolean notifyBeforeChangeSelection(final EntityObjectsSelection newSelection){        
        if (selectionListeners!=null){
            final List<SelectionListener> listeners = new LinkedList<>(selectionListeners);            
            for (SelectionListener listener: listeners){
                if (!listener.beforeChangeSelection(new EntityObjectsSelection(selection),newSelection)){
                    return false;
                }
            }            
        }
        return beforeChangeSelection(new EntityObjectsSelection(selection), newSelection);
    }
    
    final void notifyAfterChangeSelection(){
        if (selectionListeners!=null){
            final List<SelectionListener> listeners = new LinkedList<>(selectionListeners);
            for (SelectionListener listener: listeners){
                listener.afterSelectionChanged(new EntityObjectsSelection(selection));
            }            
        }
        afterSelectionChanged(new EntityObjectsSelection(selection));
    }
    
    protected boolean beforeChangeSelection(final EntityObjectsSelection oldSelection, final EntityObjectsSelection newSelection){
        return true;
    }
    
    protected void afterSelectionChanged(final EntityObjectsSelection selection){
        
    }
    
    public BatchDeleteResult deleteSelectedObjects(final boolean forced)  throws ServiceClientException, InterruptedException {
        EntityObjectsSelection selection = getSelection().normalize(this);        
        final BatchDeleteResult result = new BatchDeleteResult();        
        if (!beforeDeleteSelectedObjects(selection)) {
            return BatchDeleteResult.CANCELLED_RPOGRAMMATICALLY;
        }
        if (selection.isEmpty()){
            return BatchDeleteResult.EMPTY;
        }
        selection = selection.normalize(this);
        if (selection.isSingleObjectSelected()){            
            final Pid pid = selection.getSelectedObjects().iterator().next();
            final int row = findEntityByPid(pid);
            if (row>=0){
                final EntityModel entityModel;
                try{
                    entityModel = getEntity(row);                    
                }catch(BrokenEntityObjectException | ServiceClientException | InterruptedException exception){//NOPMD
                    //do not expect this exceptions here
                    return BatchDeleteResult.EMPTY;
                }
                if (entityModel==null){
                    return BatchDeleteResult.EMPTY;
                }else{
                    try{                        
                        if (!entityModel.delete(forced)){
                            return BatchDeleteResult.EMPTY;
                        }
                    }catch (InterruptedException e) {
                        return BatchDeleteResult.CANCELLED_BY_USER;
                    }
                    getSelection().clear();
                    result.setNumberOfDeletedObjects(1);
                    return result;
                }
            }else{
                return BatchDeleteResult.EMPTY;
            }
        }else{                
            final String title = getEnvironment().getMessageProvider().translate("ExplorerMessage", "Confirm Objects Deletion"),
                    msg = getEnvironment().getMessageProvider().translate("ExplorerMessage", "Do you really want to delete selected objects?"),
                    confirmationMessage = String.format(msg, getTitle());

            if (forced || getEnvironment().messageConfirmation(title, confirmationMessage)){
                if (selection.getSelectionMode()==ESelectionMode.INCLUSION){
                    if (entities!=null){
                        final Collection<Pid> selectedObjects = new LinkedList<>(selection.getSelectedObjects());
                        boolean canRemoveSomeObject = false;
                        for (Pid pid: selectedObjects){
                            final EntityModel entityModel = entities.findEntityModelByPid(pid);
                            if (entityModel==null){
                                canRemoveSomeObject = true;
                            }else if (entityModel instanceof BrokenEntityModel){
                                selection.unselectObject(pid);
                                result.addBrokenObject((BrokenEntityModel)entityModel);
                            }else if (entityModel.getRestrictions().getIsDeleteRestricted()){
                                selection.unselectObject(pid);
                                result.addDeleteRestricted(entityModel);
                            }else{
                                canRemoveSomeObject = true;
                            }
                        }
                        if (!canRemoveSomeObject){
                            return result;
                        }
                    }
                }
                if (deleteSelectedObjectsImpl(selection, forced, result)){
                    afterDeleteSelectedObjects();
                    return result;
                }else{
                    return BatchDeleteResult.CANCELLED_BY_USER;
                }
            }else{            
                return BatchDeleteResult.CANCELLED_BY_USER;
            }
        }
    }
    
    private boolean deleteSelectedObjectsImpl(final EntityObjectsSelection selection, final boolean cascade, final BatchDeleteResult result)
        throws ServiceClientException, InterruptedException {
        try{
            parseDeleteResponse(getEnvironment().getEasSession().deleteSelectedObjects(this, selection, cascade),result);
        }catch (ServiceCallFault e) {
            if (e.getFaultString().equals(ExceptionEnum.CONFIRM_SUBOBJECTS_DELETE.toString())) {
                final String title = getEnvironment().getMessageProvider().translate("ExplorerMessage", "Confirm Objects Deletion");
                if (getEnvironment().messageConfirmation(title, e.getMessage())) {
                   return deleteSelectedObjectsImpl(selection,true,result);
                }else{
                    return false;
                }
            }
            throw e;
        }
        for (EntityModel entityModel: entities){
            if (selection.isObjectSelected(entityModel) 
                && result.objectWasDeleted(entityModel.getPid())){
                entityModel.setExists(false);
            }
        }        
        return true;
    }
    
    private void parseDeleteResponse(final DeleteRs response, final BatchDeleteResult result){
        selection.clear();
        for (String pidAsStr: result.getRejectedObjectPids()){
            selection.selectObject(new Pid(getSelectorPresentationDef().getTableId(),pidAsStr));
        }
        if (response.getRejections()!=null && response.getRejections().getRejectionList()!=null){
            for (DeleteRejections.Rejection rejection: response.getRejections().getRejectionList()){
                final String objPid = rejection.getObjectPid();
                final String objTitle = rejection.isSetObjectTitle() ? rejection.getObjectTitle() : objPid;
                final MessageProvider mp = getEnvironment().getMessageProvider();
                if (rejection.isSetDeleteOperationRestricted()){
                    result.addDeleteRestricted(objPid, objTitle, mp);
                }else if (rejection.isSetDeleteCascadeRestriction()){
                    final String childTitle = rejection.getDeleteCascadeRestriction();
                    result.addCascadeDeleRestricted(objPid, objTitle, childTitle, mp);
                }else if (rejection.isSetException()){
                    final org.radixware.schemas.faultdetail.Exception exception = rejection.getException();
                    result.addException(objPid, objTitle, exception.getClass1(), exception.getMessage(), exception.getStack(), mp);
                }
                selection.selectObject(new Pid(getSelectorPresentationDef().getTableId(),objPid));
            }
        }
        result.setNumberOfDeletedObjects(response.getNumberOfDeletedObjects());
    }    
    
    protected boolean beforeDeleteSelectedObjects(final EntityObjectsSelection selection){
        return true;
    }
    
    protected void afterDeleteSelectedObjects(){
        
    }
}