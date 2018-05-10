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

package org.radixware.wps.views.editor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.errors.ModelCreationError;
import org.radixware.kernel.common.client.meta.RadEditorPresentationDef;
import org.radixware.kernel.common.client.meta.TitledDefinition;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.IPresentationChangedHandler;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.RawEntityModelData;
import org.radixware.kernel.common.client.models.items.ModelItem;
import org.radixware.kernel.common.client.models.items.properties.PropertyObject;
import org.radixware.kernel.common.client.models.items.properties.PropertyRef;
import org.radixware.kernel.common.client.models.items.properties.PropertyReference;
import org.radixware.kernel.common.client.models.items.properties.PropertyValue;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.views.IEditor;
import org.radixware.kernel.common.client.views.IEditor.CloseHandler;
import org.radixware.kernel.common.client.views.IEditor.OpenHandler;
import org.radixware.kernel.common.client.views.IEmbeddedEditor;
import org.radixware.kernel.common.client.views.IEmbeddedViewContext;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.utils.UIObjectUtils;
import org.radixware.wps.views.EmbeddedView;
import org.radixware.wps.views.selector.RwtSelector;


public class EmbeddedEditor extends EmbeddedView implements IPresentationChangedHandler, IEmbeddedEditor {

    private final static class EntityModelFactory{
        
        private final RawEntityModelData rawEntityModelData;
        private EntityModel newEntityModel;
        
        public EntityModelFactory(final RawEntityModelData rawEntityModelData){
            this.rawEntityModelData = rawEntityModelData;
        }
        
        public void activate(final EntityModel entityModel){
            entityModel.activate(rawEntityModelData);
        }
        
        public EntityModel createNewEntityModel(final EntityModel rowModel){
            newEntityModel = rowModel.openInSelectorEditModel();
            newEntityModel.activate(rawEntityModelData);
            return newEntityModel;
        }
        
        public EntityModel getNewEntityModel(){
            return newEntityModel;
        }
    }
    
    
    private PropertyReference property;
    private RwtSelector selector;
    private List<Id> presentations = null;
    private Id ownerClassId = null;
    private Id entityClassId = null;
    private boolean doingRefresh;
    private String pid = null;    
    private EntityModelFactory entityModelFactory;

    public EmbeddedEditor(IClientEnvironment env) {
        super((WpsEnvironment) env);
    }

    public EmbeddedEditor(RwtSelector selector) {
        super(selector.getEnvironment(), selector, null, false, new IEmbeddedViewContext.CurrentEntityEditor());
        this.selector = selector;
    }       
    
    public EmbeddedEditor(IClientEnvironment environment, final Id ownerClassId, final Id editorPresentationId, final String pid) {
        super((WpsEnvironment) environment);
        this.ownerClassId = ownerClassId;
        presentations = new ArrayList<>(1);
        presentations.add(editorPresentationId);
        this.pid = pid;
    }    
    
    public EmbeddedEditor(IClientEnvironment environment, final PropertyReference property) {
        super((WpsEnvironment) environment);        
        this.property = property;        
    }

    @Override
    protected final Model createModel() throws ServiceClientException, InterruptedException {
        boolean needForRead = false;
        EntityModel entityModel;
        if (property != null) {
            if (property instanceof PropertyObject 
                && property.getVal()==null
                && ((EntityModel)property.getOwner()).isNew()){
                entityModel = ((PropertyObject)property).initPrepareCreateModel(entityClassId, null, null);
            }else{
                entityModel = property.openEntityModel();
            }
            setObjectName("rx_embedded_editor_by_prop_#"+property.getId().toString());
        } else if (presentations != null && !presentations.isEmpty() && ownerClassId != null) {
            final RadEditorPresentationDef pres = getEnvironment().getDefManager().getEditorPresentationDef(presentations.get(0));
            final Model holderModel = UIObjectUtils.findNearestModel(this);
            if (pid == null) {
                final IContext.ContextlessCreating context = 
                    holderModel==null ? new IContext.ContextlessCreating(getEnvironment()) : new IContext.ContextlessCreating(holderModel);
                entityModel = 
                    EntityModel.openPrepareCreateModel(ownerClassId, presentations.get(0), entityClassId, null, presentations, context);
            } else if (presentations.size() > 1) {
                entityModel = 
                    EntityModel.openContextlessModel(getEnvironment(), new Pid(pres.getTableId(), pid), ownerClassId, presentations, holderModel);
            } else {
                entityModel = 
                    EntityModel.openContextlessModel(getEnvironment(), new Pid(pres.getTableId(), pid), ownerClassId, pres.getId(), holderModel);
            }
            if (entityModel!=null){
                setObjectName("rx_embedded_editor_#"+entityModel.getDefinition().getId().toString());
            }
        } else if (selector != null) {
            final GroupModel groupModel = selector.getGroupModel();
            parentModel = groupModel;
            final EntityModel currentEntity = selector.getCurrentEntity();
            if (currentEntity == null) {
                throw new ModelCreationError(ModelCreationError.ModelType.ENTITY_MODEL,
                        null,
                        (TitledDefinition) selector.getGroupModel().getDefinition(),
                        null,
                        getEnvironment().getMessageProvider().translate("ExplorerError", "Current entity was not defined in selector"));
            }
            entityModel = currentEntity.openInSelectorEditModel();
            final int rowIndex = groupModel.findEntityByPid(currentEntity.getPid());
            if (groupModel.getRowsWhenReadEntireObject().contains(Integer.valueOf(rowIndex))
                || groupModel.getPidsWhenReadEntireObject().contains(currentEntity.getPid())){
                if (entityModel!=null){
                    //invoke afterRead
                    entityModel.activate(entityModel.getPid().toString(), entityModel.getTitle(), entityModel.getClassId(), Collections.<PropertyValue>emptyList());
                }
                needForRead = false;
            }else{
                needForRead = true;
            }          
            setObjectName("rx_embedded_editor_for_selector_#"+selector.getModel().getDefinition().getId());
        } else {
            entityModel = (EntityModel)super.createModel();
        }
        if (entityModel!=null) {
            entityModel.getEntityContext().setPresentationChangedHandler(this);
            if (needForRead && !entityModel.wasRead()) {
                try{
                    if (entityModelFactory==null){
                        if (entityModel.isEdited()){
                            entityModel.activateAllProperties();
                        }
                        else{
                            entityModel.read();
                        }
                        if (entityModelFactory!=null){//Во время чтения произошло изменение презенитации - нужно взять новую модель.
                            entityModel = entityModelFactory.getNewEntityModel();
                            entityModel.getEntityContext().setPresentationChangedHandler(this);                            
                            return entityModel;
                        }
                    }
                    else{
                        entityModelFactory.activate(entityModel);
                    }
                }
                finally{
                    entityModelFactory = null;
                }
            }
        }
        return entityModel;
    }

    @Override
    @SuppressWarnings("UseSpecificCatch")
    public final EntityModel getModel() {
        if (model == null) {
            try {
                model = createModel();
            } catch (Exception ex) {
                getEnvironment().processException(ex);
            }
        }
        return model != null ? (EntityModel) model : null;
    }

    @Override
    public final EntityModel onChangePresentation(RawEntityModelData rawEntityModelData,
                                           Id newPresentationClassId, 
                                           Id newPresentationId) {        
        if (selector!=null) {
            entityModelFactory = new EntityModelFactory(rawEntityModelData);
            final boolean wasOpened = isOpened();
            if (wasOpened){
                close(true);
            }
            IContext.Entity entityContext = selector.getCurrentEntity().getEntityContext();
            //Здесь должно произойти переоткрытие, в процессе которого будет использоваться rawData.
            final EntityModel newRowModel = 
                entityContext.getPresentationChangedHandler().onChangePresentation(rawEntityModelData, newPresentationClassId, newPresentationId);
            return wasOpened ? (EntityModel)model : entityModelFactory.createNewEntityModel(newRowModel);
        } else {
            return super.onChangePresentation(rawEntityModelData, newPresentationClassId, newPresentationId);
        }
    }

    @Override
    public final void bind() {
        if (property != null) {
            property.subscribe(this);
            refresh(property);
        }
        else{
            super.bind();
        }
    }

    @Override
    @SuppressWarnings("UseSpecificCatch")
    public final void refresh(ModelItem changedItem) {
        if (changedItem != null && property != null && changedItem.getId().equals(property.getId()) && !doingRefresh) {
            doingRefresh = true;
            try{
                final Pid value = property.getVal()==null ? null : property.getVal().getPid();
                if (model == null && canOpenEntityModel()) {
                    try {
                        open();
                    } catch (Exception ex) {
                        processExceptionOnOpen(ex);
                    }
                } else if (model != null && !Objects.equals(((EntityModel) model).getPid(),value)) {
                    setUpdatesEnabled(false);
                    if (close(false) && canOpenEntityModel()) {
                        try {
                            open();
                        } catch (Exception ex) {
                            processExceptionOnOpen(ex);
                        } finally {
                            setUpdatesEnabled(true);
                        }
                    } else {
                        setUpdatesEnabled(true);
                    }
                }
            }finally{
                doingRefresh = false;
            }
        }
    }

    @Override
    public final Editor getView() {
        return (Editor) embeddedView;
    }

    @Override
    public final void setPresentation(final Id ownerClassId, final Id editorPresentationId) {
        if (model != null && !close(false)) {
            return;
        }
        if (presentations == null) {
            presentations = new ArrayList<>(1);
        }
        presentations.clear();
        presentations.add(editorPresentationId);
        childItemId = null;
        this.ownerClassId = ownerClassId;
    }

    @Override
    public final void setPresentations(final Id ownerClassId, final List<Id> editorPresentationIds) {
        if (model != null && !close(false)) {
            return;
        }
        if (presentations == null) {
            presentations = new ArrayList<>(1);
        }
        presentations.clear();
        presentations.addAll(editorPresentationIds);
        this.ownerClassId = ownerClassId;
        childItemId = null;
    }
    
    /**
     * Установить свойство-ссылку для открытия редактора на основе значения этого свойства.
     * При открытии редактора по свойству ссылке для получения модели модели объекта сущности
     * будет использоваться метод {@link PropertyRef#openEntityModel() }.
     * <p>
     * Если на момент вызова компонент уже был открыт, то произойдет его закрытие.
     * @param propertyRef свойство-ссылка на объект сущности.
     */
    @Override
    public final void setPropertyRef(final PropertyReference propertyRef){
        if (model!=null && !close(false)){
            return;
        }
        property = propertyRef;
        if (presentations!=null){
            presentations.clear();
        }
        ownerClassId = null;
        entityClassId = null;
        pid = null;
        childItemId = null;        
    }    

    public final List<Id> getPresentations() {
        if (presentations != null) {
            return Collections.unmodifiableList(presentations);
        }
        return null;
    }

    /**
     * Установить {@link Pid идентификатор сущности} для открытия в режиме
     * бесконтекстного редактирования на основе презентации редактора.
     * Для открытия редактора в этом режиме необходимо также задать презентацию,
     * вызвав метод {@link #setPresentation(String, String)}.
     * <p>
     * Если значение идентификатора равно null после открытия будет производится редактирование
     * новой сущности.
     * <p>
     * Если на момент вызова компонент уже был открыт, то произойдет его закрытие.
     * @param pid - строковое представление {@link Pid идентификатора сущности}
     */
    @Override
    public final void setEntityPid(String pid) {
        if (model != null && !close(false)) {
            return;
        }
        entityClassId = null;
        this.pid = pid;
        childItemId = null;
    }

    /**
     * Получить строковое представление {@link Pid идентификатора сущности} для режима бесконтекстного редактирования в указанной презентации.
     */
    @Override
    public final String getEntityPid() {
        return pid;
    }

    /**
     * Устанавливает идентификатор класса сущности для режима бесконтекстного редактирования в указанной презентации,
     * в случае когда происходит создание новой сущности.
     * <p>
     * Если на момент вызова компонент уже был открыт, то произойдет его закрытие.
     * @param classId - идентификатор класса новой сущности
     */
    @Override
    public final void setClassOfNewEntity(final Id classId) {
        if (model != null && !close(false)) {
            return;
        }
        pid = null;
        entityClassId = classId;
        childItemId = null;
    }

    @Override
    public final void setExplorerItem(Model parentModel, final Id explorerItemId) {
        super.setExplorerItem(parentModel, explorerItemId);
        presentations = null;
        entityClassId = null;
        ownerClassId = null;
        pid = null;
    }

    @Override
    protected void onViewClosed() {
        final Editor editor = getView();
        if (editor != null) {
            editor.removeEditorListener(defaultListener);
            editor.removeCloseHandler(defaulCloseHandler);
            editor.removeOpenHandler(defaulOpenHandler);
        }

        super.onViewClosed();
    }
    
    
    private final List<OpenHandler> openHandlers = new LinkedList<>();
    private final List<CloseHandler> closeHandlers = new LinkedList<>();
    private final List<IEditor.EditorListener> editorListeners = new LinkedList<>();
    private final OpenHandler defaulOpenHandler = new OpenHandler() {

        @Override
        public void afterOpen() {
            final EntityModel entity = (EntityModel) model;
            if (entity.isNew()) {
                getView().setToolBarHidden(true);
            }            
            final List<OpenHandler> ls;
            synchronized (openHandlers) {
                ls = new ArrayList<>(openHandlers);
            }
            for (OpenHandler h : ls) {
                h.afterOpen();
            }
        }
    };
    private final CloseHandler defaulCloseHandler = new CloseHandler() {

        @Override
        public void onClose() {
            final List<CloseHandler> ls;
            synchronized (closeHandlers) {
                ls = new ArrayList<>(closeHandlers);
            }
            for (CloseHandler h : ls) {
                h.onClose();
            }
        }
    };
    
    private final IEditor.EditorListener defaultListener = new IEditor.EditorListener() {

        @Override
        public void entityUpdated() {
            final List<IEditor.EditorListener> ls;
            synchronized (editorListeners) {
                ls = new ArrayList<>(editorListeners);
            }
            for (IEditor.EditorListener l : ls) {
                l.entityUpdated();
            }
        }

        @Override
        public void entityRemoved() {
            final List<IEditor.EditorListener> ls;
            synchronized (editorListeners) {
                ls = new ArrayList<>(editorListeners);
            }
            for (IEditor.EditorListener l : ls) {
                l.entityRemoved();
            }
        }
    };
    
    private boolean canOpenEntityModel(){
        if (property instanceof PropertyObject){
            final PropertyObject propObject = (PropertyObject)property;
            if (propObject.getVal()==null){
                return propObject.canCreate();
            }else{
                if (((EntityModel)property.getOwner()).isNew()){
                    return propObject.canModifyEntityObject();
                }else{
                    return property.canOpenEntityModel();
                }
            }
        }else{
            return property.canOpenEntityModel();
        }
    }

    @Override
    protected void onModifiedStateChanged() {
        super.onModifiedStateChanged();
        if (property instanceof PropertyObject && ((EntityModel)property.getOwner()).isNew()){
            property.setValEdited(inModifiedStateNow());
        }
    }        
    
    @Override
    protected void onViewCreated() {
        final Editor editor = getView();
        if (selector != null) {
            editor.setMenu(selector.getEditorMenu());
        }
        editor.addEditorListener(defaultListener);
        editor.addOpenHandler(defaulOpenHandler);
        editor.addCloseHandler(defaulCloseHandler);
    }

    public final void addOpenHandler(OpenHandler h) {
        synchronized (openHandlers) {
            if (!openHandlers.contains(h)) {
                openHandlers.add(h);
            }
        }
    }

    public final void removeOpenHandler(OpenHandler h) {
        synchronized (openHandlers) {
            openHandlers.remove(h);
        }
    }

    public final void addCloseHandler(CloseHandler h) {
        synchronized (closeHandlers) {
            if (!closeHandlers.contains(h)) {
                closeHandlers.add(h);
            }
        }
    }

    public final void removeCloseHandler(CloseHandler h) {
        synchronized (closeHandlers) {
            closeHandlers.remove(h);
        }
    }

    public final void addEditorListener(IEditor.EditorListener l) {
        synchronized (editorListeners) {
            if (!editorListeners.contains(l)) {
                editorListeners.add(l);
            }
        }
    }

    public final void removeEditorListener(IEditor.EditorListener l) {
        synchronized (editorListeners) {
            editorListeners.remove(l);
        }
    }
}