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

package org.radixware.kernel.explorer.widgets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.xmlbeans.XmlObject;


import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QWidget;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;
import javax.swing.text.View;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.eas.RequestHandle;
import org.radixware.kernel.common.client.errors.ModelCreationError;
import org.radixware.kernel.common.client.meta.RadEditorPresentationDef;
import org.radixware.kernel.common.client.meta.filters.RadFilterParamDef;
import org.radixware.kernel.common.client.meta.RadParentRefPropertyDef;
import org.radixware.kernel.common.client.meta.RadPresentationDef;
import org.radixware.kernel.common.client.meta.RadPropertyDef;
import org.radixware.kernel.common.client.meta.TitledDefinition;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.IContext.ContextlessEditing;
import org.radixware.kernel.common.client.models.IContext.InSelectorEditing;
import org.radixware.kernel.common.client.models.IPresentationChangedHandler;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.RawEntityModelData;
import org.radixware.kernel.common.client.models.items.ModelItem;
import org.radixware.kernel.common.client.models.items.properties.PropertyObject;
import org.radixware.kernel.common.client.models.items.properties.PropertyRef;
import org.radixware.kernel.common.client.models.items.properties.PropertyReference;
import org.radixware.kernel.common.client.models.items.properties.PropertyValue;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.views.IEditor.CloseHandler;
import org.radixware.kernel.common.client.views.IEmbeddedEditor;
import org.radixware.kernel.common.client.views.IEmbeddedViewContext;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.errors.NotEnoughParamsForOpenError;
import org.radixware.kernel.explorer.utils.WidgetUtils;
import org.radixware.kernel.explorer.views.Editor;

import org.radixware.kernel.explorer.views.selector.Selector;
import org.radixware.schemas.eas.ReadRs;

/**
 * Виджет вложенный редактор. Содержит {@link EntityModel модель сущности} и ее
 * {@link Editor редактор}.  Потдерживает несколько режимов работы:
 * <p>
 * <h4>	Редактирование дочерней сущности модели заданного представления </h4>
 * <p>
 * 		Для работы в данном режиме в конструкторе {@link #EmbeddedEditor(View, String)}
 * необходимо передать {@link View представление}, внутри которого находится виджет, и
 * идентификатор дочернего элемента проводника. После вызова метода {@link #open()} или
 * {@link #bind()} будет открыт редактор дочерней по отношению к модели представления сущности.
 * <p>
 * <h4>Редактирование сущности на основе значения заданного свойства типа ParentRef или Object </h4>
 * <p>
 * 		Для работы в данном режиме в конструкторе {@link #EmbeddedEditor(PropertyReference)}
 * необходимо передать свойство {@link org.radixware.kernel.explorer.models.items.properties.PropertyRef} или {@link org.radixware.kernel.explorer.models.items.properties.PropertyObject}, в метоописании
 * которого задана презентация редактора. После вызова метода {@link #open()} или {@link #bind()}
 * будет открыт редактор сущности на которую в данный момент ссылается свойство
 * (если свойство в данный момент не имеет значения открытия не произойдет).
 * Если для открытия использовался метод {@link #bind()} произойдет связывание виджета со свойством
 * и после смены значения свойства редактор будет автоматически переоткрыт.
 * <p>
 * <h4> Бесконтекстное редактирование новой или существующей сущности на основе заданной презентации редактора </h4>
 * <p>
 *		Для работы в данном режиме необходимо задать {@link EditorPresentationDef презентацию редактора}.
 * Если производится редактирование существующей сущности необходимо также задать ее {@link Pid идентификатор}.
 * Установка этих параметров производится либо в конструкторе {@link #EmbeddedEditor(String, String, String)}, либо
 * в методах {@link #setPresentation(String, String)} и {@link #setEntityPid(String)}. После вызова метода {@link #open()} или
 * {@link #bind()} будет открыт {@link ContextlessEditing бесконтекстный} редактор заданной презентации.
 * <p>
 * <h4> Редактирование текущей сущности селектора</h4>
 * <p>
 * 		Для работы в данном режиме в конструкторе {@link #EmbeddedEditor(Selector)}
 * необходимо передать {@link Selector селектор}. После вызова метода {@link #open()} или {@link #bind()}
 * будет открыт редактор текущей на данный момент сущности селектора в контексте {@link InSelectorEditing}
 * (если текущая сущность не задана открытия не произойдет).
 * @see EmbeddedView
 */
public class EmbeddedEditor extends EmbeddedView implements IPresentationChangedHandler, org.radixware.kernel.common.client.eas.IResponseListener,IEmbeddedEditor {
        
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
    private Selector selector;
    private List<Id> presentations = null;
    private Id ownerClassId = null;
    private Id entityClassId = null;
    private boolean doingRefresh;
    private String pid = null;
    private EntityModelFactory entityFactory;
    
    /**
     * Сигнал посылается после открытия {@link Editor редактора} модели сущности.
     * Параметр сигнала - {@link QWidget}, который содержит визуальные компоненты редактора.
     * @see Editor#open(org.radixware.kernel.explorer.models.Model)
     */
    final public Signal1<QWidget> opened = new Signal1<>();
    /**
     * Сигнал посылается после закрытия {@link Editor редактора}.
     * @see Editor#close(boolean)
     */
    final public Signal0 closed = new Signal0();
    /**
     * Сигнал посылается после удаления сущности из редактора
     * @see Editor#delete()
     */
    final public Signal0 entityRemoved = new Signal0();
    /**
     * Сигнал посылается после применения изменений в свойствах сущности
     * @see Editor#update()
     */
    final public Signal0 entityUpdated = new Signal0();

    /**
     * @see EmbeddedView#EmbeddedView(IView, Id)
     */
    public EmbeddedEditor(IClientEnvironment environment, IView parentView, Id childItemId) {
        super(environment, parentView, childItemId);
        setObjectName("embedded_editor");
        property = null;
        selector = null;
    }

    /**
     * Создает вложенный редактор для режима редактирования сущности в заданной презентации.
     * Если параметр pid равен null, будет производится редактирование новой сущности.
     * @param editorPresentationId - идентификатор {@link EditorPresentationDef презентации редактора},
     *  в которой будет производится редактирование сущности
     * @param ownerClassId - идентификатор {@link org.radixware.kernel.explorer.meta.RadClassPresentationDef класса}, где находится {@link EditorPresentationDef презентации редактора}.
     * @param pid - строковое представление {@link Pid идентификатора}, редактируемой сущности.
     */
    public EmbeddedEditor(IClientEnvironment environment, final Id ownerClassId, final Id editorPresentationId, final String pid) {
        super(environment);
        setObjectName("embedded_editor");
        this.ownerClassId = ownerClassId;
        presentations = new ArrayList<>(1);
        presentations.add(editorPresentationId);
        this.pid = pid;
        property = null;
        selector = null;
    }

    /**
     * Создает вложенный редактор для режима редактирования сущности на основе значения заданного свойства типа
     * ParentRef или Object
     * @param property - свойство по значению которого будет открываться редактор
     */
    public EmbeddedEditor(IClientEnvironment environment, final PropertyReference property) {
        super(environment);
        setObjectName("embedded_editor");
        this.property = property;
        selector = null;
    }

    /**
     * Создает вложенный редактор для режима редактирования текущей сущности селектора
     * @param selector
     */
    public EmbeddedEditor(IClientEnvironment environment, final Selector selector) {
        super(environment, selector, null, false, selector==null ? null : new IEmbeddedViewContext.CurrentEntityEditor());
        setObjectName("embedded_editor");
        this.selector = selector;
        property = null;
    }

    /**
     * Создает вложенный редактор, не устанавливая конкретного режима редактирования.
     * Созданный таким образом вложенный редактор перед использованием нуждается в установке
     * параметров через вызовы методов {@link #setPresentation(String, String)} и {@link #setEntityPid(String)}.
     * 	 */
    public EmbeddedEditor(IClientEnvironment environment) {
        super(environment);
        setObjectName("embedded_editor");
        property = null;
        selector = null;
    }

    /**
     * Устанавливает {@link EditorPresentationDef презентацию редактора} для режима бесконтекстного редактирования в указанной презентации.
     * <p>
     * Если на момент вызова компонент уже был открыт, то произойдет его закрытие.
     * @param ownerClassId  - идентификатор {@link org.radixware.kernel.explorer.meta.RadClassPresentationDef класса}, где находится {@link EditorPresentationDef презентации редактора}.
     * @param editorPresentationId идентификатор {@link EditorPresentationDef презентации редактора},
     *  в которой будет производится редактирование сущности
     *  @see #setEntityPid(String)
     *  @see #setClassOfNewEntity(String)
     */
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
        property = null;
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
        property = null;
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
        property = null;
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
            final RadEditorPresentationDef pres = getEnvironment().getApplication().getDefManager().getEditorPresentationDef(presentations.get(0));
            final Model holderModel = WidgetUtils.findNearestModel(this);
            if (pid == null) {
                final IContext.ContextlessCreating context = holderModel==null ? new IContext.ContextlessCreating(getEnvironment()) : new IContext.ContextlessCreating(holderModel);
                entityModel = EntityModel.openPrepareCreateModel(ownerClassId, presentations.get(0), entityClassId, null, presentations, context);
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
                        (TitledDefinition) groupModel.getDefinition(),
                        null,
                        getEnvironment().getMessageProvider().translate("ExplorerError", "Current object was not defined in selector"));
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
                    if (entityFactory==null){
                        if (entityModel.isEdited()){
                            entityModel.activateAllProperties();
                        }
                        else{
                            entityModel.read();
                        }
                        if (entityFactory!=null){//Во время чтения произошло изменение презенитации - нужно взять новую модель.
                            entityModel = entityFactory.getNewEntityModel();
                            entityModel.getEntityContext().setPresentationChangedHandler(this);                            
                            return entityModel;
                        }
                    }
                    else{
                        entityFactory.activate(entityModel);
                    }
                }
                finally{
                    entityFactory = null;
                }
            }
        }
        return entityModel;
    }
    
    @Override
    protected final RadPresentationDef getExpectedPresentation(){
        if (property != null) {
            final List<Id> presentationIds = 
                ((RadParentRefPropertyDef)property.getDefinition()).getObjectEditorPresentationIds();
            if (presentationIds!=null && !presentationIds.isEmpty()){
                return getEnvironment().getApplication().getDefManager().getEditorPresentationDef(presentationIds.get(0));
            }else{
                return null;
            }
        } else if (presentations != null && !presentations.isEmpty() && ownerClassId != null) {
            return getEnvironment().getApplication().getDefManager().getEditorPresentationDef(presentations.get(0));
        } else if (selector != null) {            
            if (selector.getCurrentEntity() != null) {
                return selector.getCurrentEntity().getEditorPresentationDef();
            }else{
                return null;
            }
        } else {
            return null;
        }        
    }

    /**
     * @see EmbeddedView#bind()
     */
    @Override
    public void bind() {
        if (property != null) {            
            property.subscribe(this);
            refresh(property);
        }
        else{
            super.bind();
        }
    }

    @Override
    protected final void closeEvent(final QCloseEvent event) {
        disconnectSignals();
        if (property != null) {
            property.unsubscribe(this);
        }
        property = null;
        selector = null;
        super.closeEvent(event);
    }

    /**
     * @see EmbeddedView#refresh(org.radixware.kernel.explorer.models.items.ModelItem)
     */
    @Override
    @SuppressWarnings("UseSpecificCatch")
    public final void refresh(final ModelItem changedItem) {
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
        super.onViewCreated();
        final Editor editor = getView();
        if (selector != null) {
            editor.setMenu(selector.getEditorMenu());
        }
        editor.opened.connect(this, "afterOpened(QWidget)");
        editor.addCloseHandler(new CloseHandler() {

            @Override
            public void onClose() {
                closed.emit();
            }
        });

        editor.entityUpdated.connect(entityUpdated);
        editor.entityRemoved.connect(entityRemoved);
    }

    private void disconnectSignals() {
        opened.disconnect();
        closed.disconnect();
        entityUpdated.disconnect();
        entityRemoved.disconnect();
    }

    @SuppressWarnings("unused")
    protected void afterOpened(final QWidget content) {
        final EntityModel entity = (EntityModel) model;
        if (entity.isNew()) {
            getView().setToolBarHidden(true);
        }
        opened.emit(content);
    }

    /**
     * Возвращает открытый редактор
     */
    @Override
    public final Editor getView() {
        return super.getView() == null ? null : (Editor)super.getView();
    }

    /**
     * Возвращает созданную для одного из режимов редактирования {@link EntityModel модель сущности}.
     * Если на момент вызова модель сущности еще не была создана - произойдет попытка ее создания
     * Если для создания модели у вложенного редактора недостаточно параметров сгенерируется {@link NotEnoughParamsForOpenError}.
     * @return модель сущности.
     */
    @Override
    @SuppressWarnings("UseSpecificCatch")
    public final EntityModel getModel() {
        if (model == null) {
            try {
                createModelAndPrepareView();
            } catch (Exception ex) {
                getEnvironment().processException(ex);
            }
        }
        return model != null ? (EntityModel) model : null;
    }

    public final RequestHandle rereadAsync() {
        if (model == null) {
            return null;
        } else {
            return ((EntityModel) model).readAsync(null);
        }
    }

    @Override
    public final EntityModel onChangePresentation(final RawEntityModelData rawData,
                                            Id newPresentationClassId, Id newPresentationId) {
        if (selector!=null) {
            entityFactory = new  EntityModelFactory(rawData);
            final boolean wasOpened = isOpened();
            if (wasOpened){
                close(true);
            }
            IContext.Entity entityContext = selector.getCurrentEntity().getEntityContext();
            //Здесь должно произойти переоткрытие, в процессе которого будет использоваться rawData.
            final EntityModel newRowModel = 
                entityContext.getPresentationChangedHandler().onChangePresentation(rawData, newPresentationClassId, newPresentationId);
            return wasOpened ? (EntityModel)model : entityFactory.createNewEntityModel(newRowModel);
        } else {
            return super.onChangePresentation(rawData, newPresentationClassId, newPresentationId);
        }
    }
    
    public final void bindAsync() {
        bindAsync(0);
    }

    public final void bindAsync(final int timeoutSec) {
        final IContext.Entity context;
        final Id classId;
        final Pid entityPid;
        final Collection<Id> presentationIds;
        if (property != null) {
            final RadPropertyDef propertyDef = property.getDefinition();
            if (propertyDef instanceof RadFilterParamDef) {
                throw new IllegalUsageError("Cannot open entity model by value of filter parameter");
            }
            final RadParentRefPropertyDef pd = (RadParentRefPropertyDef) propertyDef;
            context = new IContext.ReferencedEntityEditing(property);
            if (!pd.isObjectEditorPresentationDefined()) {
                final String info = getEnvironment().getMessageProvider().translate("ExplorerException", "editor presentation was not defined for property %s");
                throw new ModelCreationError(ModelCreationError.ModelType.ENTITY_MODEL, null, propertyDef, context, String.format(info, propertyDef.getDescription()));
            }

            if (property.getVal() == null) {
                final String info = getEnvironment().getMessageProvider().translate("ExplorerException", "value of property %s is not defined");
                throw new ModelCreationError(ModelCreationError.ModelType.ENTITY_MODEL, null, propertyDef, context, String.format(info, propertyDef.getDescription()));
            }

            presentationIds = pd.getObjectEditorPresentationIds();
            classId = pd.getReferencedClassId();
            entityPid = property.getVal().getPid();
        } else if (presentations != null) {
            presentationIds = new LinkedList<>();
            presentationIds.addAll(presentations);
            final RadEditorPresentationDef pres = getEnvironment().getApplication().getDefManager().getEditorPresentationDef(presentations.get(0));            
            context = new IContext.ContextlessEditing(getEnvironment(),null,WidgetUtils.findNearestModel(this));
            entityPid = new Pid(pres.getTableId(), pid);
            classId = ownerClassId;
        } else {
            throw new NotEnoughParamsForOpenError(this);
        }

        if (!presentationIds.isEmpty() && classId != null && entityPid != null) {
            cancelAsyncActions();
            String msg = "creating model for entity with identifier \"%s\" in table %s.";
            getEnvironment().getTracer().put(EEventSeverity.DEBUG, String.format(msg, entityPid.toString(), entityPid.getTableId()), EEventSource.EXPLORER);

            final RequestHandle handle = 
                RequestHandle.Factory.createForReadEntityObject(getEnvironment(), entityPid, classId, presentationIds, context);

            handle.addListener(this);
            handle.setUserData(context);
            getEnvironment().getEasSession().sendAsync(handle, timeoutSec);
        } else {
            throw new NotEnoughParamsForOpenError(this);
        }
    }

    @Override
    @SuppressWarnings("UseSpecificCatch")
    public final void onResponseReceived(XmlObject response, RequestHandle handle) {
        final ReadRs rs = (ReadRs) response;
        final Id presentationId = rs.getData().getPresentation().getId();
        final RadEditorPresentationDef presentation = getEnvironment().getApplication().getDefManager().getEditorPresentationDef(presentationId);
        final IContext.Entity context = (IContext.Entity) handle.getUserData();
        final EntityModel entity = presentation.createModel(context);
        entity.activate(new RawEntityModelData(rs));
        final String msg = "model for entity with class #%s and identifier \"%s\"\n was created by editor presentation %s.\n in context %s";
        getEnvironment().getTracer().put(EEventSeverity.EVENT,
                String.format(msg, entity.getClassId(), entity.getPid().toString(), entity.getDefinition().toString(), context.getDescription()),
                EEventSource.EXPLORER);

        setUpdatesEnabled(false);
        try {
            if (getView() != null) {
                close(true);
            }
            model = entity;
            open();
        } catch (Exception ex) {
            processExceptionOnOpen(ex);
        } finally {
            setUpdatesEnabled(true);
        }
    }
}