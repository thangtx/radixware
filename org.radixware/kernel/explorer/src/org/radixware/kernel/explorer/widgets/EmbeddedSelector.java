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

import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QWidget;

//import org.radixware.kernel.explorer.errors.NotEnoughParamsForOpenError;
import java.util.List;
import java.util.LinkedList;
import javax.swing.text.View;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.IContext.ContextlessSelect;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.items.properties.PropertyRef;
import org.radixware.kernel.common.client.types.ModelRestrictions;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.views.IEmbeddedSelector;
import org.radixware.kernel.common.client.views.IExplorerItemView;
import org.radixware.kernel.common.client.views.ISelector.CurrentEntityHandler;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.utils.WidgetUtils;

import org.radixware.kernel.explorer.views.selector.Selector;

/**
 * Виджет вложенный селектор. Содержит {@link GroupModel модель группы} и ее
 * {@link Selector селектор}.  Потдерживает несколько режимов работы:
 * <p>
 * <h4>	Открытие дочерней группы для модели у заданного представления </h4>
 * </p>
 * <p>
 * 		Для работы в данном режиме в конструкторе {@link #EmbeddedSelector(IView, Id)}
 * необходимо передать {@link IView представление}, внутри которого находится виджет, и
 * идентификатор дочернего элемента проводника. После вызова метода {@link #open()} или
 * {@link #bind()} будет открыт селектор для группы дочерней по отношению к модели представления сущности.
 * </p>
 * <p>
 * <h4>Открытие родительской группы на основе заданного свойства типа ParentRef</h4>
 * </p>
 * <p>
 * 		Для работы в данном режиме в конструкторе {@link #EmbeddedSelector(PropertyRef)}
 * необходимо передать свойство {@link PropertyRef}. После вызова метода {@link #open()} или {@link #bind()}
 * будет открыт селектор группы на которую ссылается свойство.
 * </p>
 * <p>
 * <h4> Открытие бесконтекстного селектора на основе заданной презентации</h4>
 * </p>
 * <p>
 *		Для работы в данном режиме необходимо задать {@link RadSelectorPresentationDef презентацию селектора}.
 * Установка презентации производится либо в конструкторе {@link #EmbeddedSelector(Id, Id)}, либо
 * в методe {@link #setPresentation(Id, Id)}. После вызова метода {@link #open()} или
 * {@link #bind()} будет открыт {@link ContextlessSelect бесконтекстный} селектор в заданной презентации.
 * На открытый таким образом селектор будут наложены все {@link ModelRestrictions ограничения}.
 * @see EmbeddedView
 */
public /*final*/ class EmbeddedSelector extends EmbeddedView implements org.radixware.kernel.common.client.eas.IResponseListener ,IEmbeddedSelector{

    private final PropertyRef property;
    private RadSelectorPresentationDef presentation = null;
    /**
     * Сигнал посылается после открытия {@link Selector селектора} модели группы.
     * Параметр сигнала - {@link QWidget}, который содержит визуальные компоненты селектора.
     * @see Selector#open(org.radixware.kernel.explorer.models.Model)
     */
    final public Signal1<QWidget> opened = new Signal1<>();
    /**
     * Сигнал посылается после закрытия {@link Selector селектора}.
     * @see Selector#close(boolean)
     */
    final public Signal0 closed = new Signal0();
    /**
     * Сигнал посылается после ухода с текущей сущности селектора
     * @see Selector#leaveCurrentEntity(boolean)
     */
    final public Signal0 onLeaveCurrentEntity = new Signal0();
    /**
     * Сигнал посылается после установки текущей сущности в селекторе
     * @see Selector#setCurrentEntity(EntityModel)
     */
    final public Signal1<EntityModel> onSetCurrentEntity = new Signal1<>();
    /**
     * Сигнал посылается после перечитывания данных в селекторе.
     * Pid - идентификатор сущности, которая должна стать текущей
     * после перечитывания.
     */
    final public Signal1<Pid> afterReread = new Signal1<>();
    /**
     * Сигнал посылается после создания в селекторе новой сущности.
     * @see Selector#create()
     */
    final public Signal1<List<EntityModel>> entitiesCreated = new Signal1<>();
    /**
     * Сигнал посылается после выполнения вставки сущности в дерево.
     * @see Selector#insertedIntoTree
     */
    final public Signal1<IExplorerItemView> insertedIntoTree = new Signal1<>();
    /**
     * Сигнал посылается после удаления текущей сущности селектора
     * @see EntityModel#delete(boolean)
     */
    final public Signal0 entityRemoved = new Signal0();
    final public Signal0 onDeleteAll = new Signal0();
    /**
     * Сигнал посылается после применения изменений в текущей сущности селектора
     * @see EntityModel#update()
     */
    final public Signal0 entityUpdated = new Signal0();

    /**
     * @see EmbeddedView#EmbeddedView(View, String)
     */
    public EmbeddedSelector(IClientEnvironment environment, IView parentView, Id childItemId) {
        super(environment, parentView, childItemId);
        setObjectName("embedded_selector");
        property = null;
    }

    /**
     * Создает вложенный cелектор для режима открытия группы на основе заданного свойства типа
     * ParentRef
     * @param property - свойство для которого будет открыт селектор
     */
    public EmbeddedSelector(IClientEnvironment environment, final PropertyRef property) {
        super(environment);
        setObjectName("embedded_selector");
        this.property = property;
    }

    /**
     * Создает вложенный селектор для режима бесконтектного открытия группы в заданной презентации.
     * @param selectorPresentationId - идентификатор {@link SelectorPresentationDef презентации селектора},
     *  в которой будет открыта группа
     * @param ownerClassId - идентификатор {@link org.radixware.kernel.explorer.meta.RadClassPresentationDef   класса}, где находится {@link RadSelectorPresentationDef презентация селектора}.	 */
    public EmbeddedSelector(IClientEnvironment environment, final Id ownerClassId, final Id selectorPresentationId) {
        super(environment);
        setObjectName("embedded_selector");
        presentation = getEnvironment().getApplication().getDefManager().getSelectorPresentationDef(selectorPresentationId);
        property = null;
    }

    /**
     * Создает вложенный селектор, не устанавливая конкретного режима открытия.
     * Созданный таким образом вложенный селектор перед использованием нуждается в установке
     * презентации через вызов метода {@link #setPresentation(Id, Id)}.
     * 	 */
    public EmbeddedSelector(IClientEnvironment environment) {
        super(environment);
        setObjectName("embedded_selector");
        property = null;
    }

    /**
     * Устанавливает {@link RadSelectorPresentationDef презентацию селектора} для режима бесконтекстного открытия группы в указанной презентации.
     * <p>
     * Если на момент вызова компонент уже был открыт, то произойдет его закрытие.
     * @param ownerClassId  - идентификатор {@link org.radixware.kernel.explorer.meta.RadClassPresentationDef класса}, где находится {@link RadSelectorPresentationDef презентация селектора}.
     * @param selectorPresentationId идентификатор {@link RadSelectorPresentationDef презентации селектора},
     *  в которой будет открыта группа.
     */
    @Override
    public void setPresentation(final Id ownerClassId, final Id selectorPresentationId) {
        if (model != null && !close(false)) {
            return;
        }
        presentation = getEnvironment().getApplication().getDefManager().getSelectorPresentationDef(selectorPresentationId);
        childItemId = null;
    }

    @Override
    public void setExplorerItem(Model parentModel, final Id explorerItemId) {
        super.setExplorerItem(parentModel, explorerItemId);
        presentation = null;
    }

    /**
     * Получить {@link RadSelectorPresentationDef презентацию селектора} для режима бесконтекстного открытия в указанной презентации.
     */
    @Override
    public RadSelectorPresentationDef getPresentation() {
        return presentation;
    }

    @Override
    protected Model createModel() throws ServiceClientException, InterruptedException {
        final Model newModel;
        if (property != null) {
            newModel = property.openGroupModel();
            property.subscribe(this);
        } else if (getPresentation() != null) {
            final Model holderModel = WidgetUtils.findNearestModel(this);
            if (holderModel==null){
                newModel = GroupModel.openTableContextlessSelectorModel(getEnvironment(), presentation);
            }else{
                newModel = GroupModel.openTableContextlessSelectorModel(holderModel, presentation);
            }
        } else {
            newModel = super.createModel();
        }
        return newModel;
    }
    private final List<CurrentEntityHandler> customHandlers = new LinkedList<> ();
    private CurrentEntityHandler defaultCurrentEntityHandler = new CurrentEntityHandler() {

        @Override
        public void onSetCurrentEntity(EntityModel entity) {

            synchronized (customHandlers) {
                for (CurrentEntityHandler h : customHandlers) {
                    h.onSetCurrentEntity(entity);
                }
            }
        }

        @Override
        public void onLeaveCurrentEntity() {
            synchronized (customHandlers) {
                for (CurrentEntityHandler h : customHandlers) {
                    h.onLeaveCurrentEntity();
                }
            }
        }
    };

    public void addCurrentEntityHandler(CurrentEntityHandler handler) {
        synchronized (customHandlers) {
            if (!customHandlers.contains(handler)) {
                customHandlers.add(handler);
            }
        }
    }

    public void removeCurrentEntityHandler(CurrentEntityHandler handler) {
        synchronized (customHandlers) {
            if (!customHandlers.contains(handler)) {
                customHandlers.remove(handler);
            }
        }
    }

    @Override
    protected void onViewCreated() {
        super.onViewCreated();
        final Selector selector = getView();
        selector.opened.connect(opened, "emit(Object)");
        selector.closed.connect(closed, "emit()");
        selector.entityUpdated.connect(entityUpdated);
        selector.entityRemoved.connect(entityRemoved);
        selector.onDeleteAll.connect(onDeleteAll);
        selector.entitiesCreated.connect(entitiesCreated);
        selector.addCurrentEntityHandler(defaultCurrentEntityHandler);
        selector.onLeaveCurrentEntity.connect(onLeaveCurrentEntity);
        selector.onSetCurrentEntity.connect(onSetCurrentEntity);
        selector.insertedIntoTree.connect(insertedIntoTree);
    }

    private void disconnectSignals() {
        opened.disconnect();
        closed.disconnect();
        entityUpdated.disconnect();
        entityRemoved.disconnect();
        onDeleteAll.disconnect();
        entitiesCreated.disconnect();
        if (getView()!=null){
            getView().removeCurrentEntityHandler(defaultCurrentEntityHandler);
        }
        onLeaveCurrentEntity.disconnect();
        onSetCurrentEntity.disconnect();
        insertedIntoTree.disconnect();
    }

    /**
     * Возвращает открытый селектор
     */
    @Override
    public Selector getView() {
        return super.getView()==null ? null : (Selector)super.getView();
    }

    /**
     * Возвращает созданную для одного из режимов открытия {@link GroupModel модель группы}.
     * Если на момент вызова модель группы еще не была создана - произойдет попытка ее создания.
     * Если для создания модели у вложенного селектора недостаточно параметров сгенерируется {@link org.radixware.kernel.explorer.errors.NotEnoughParamsForOpenError}.
     * @return модель группы.
     */
    @Override
    @SuppressWarnings("UseSpecificCatch")
    public GroupModel getModel() {
        if (model == null) {
            try {
                model = createModel();
            } catch (Exception ex) {
                getEnvironment().processException(ex);
            }
        }
        return model != null ? (GroupModel) model : null;
    }

    @Override
    public void reread() {
        if (getView() != null) {
            getView().getActions().getRereadAction().trigger();
        } else {
            throw new IllegalStateException("widget was not opened");
        }
    }

    @Override
    protected void closeEvent(QCloseEvent event) {
        disconnectSignals();
        super.closeEvent(event);
    }

    @Override
    public boolean close(boolean forced) {
        final boolean result = super.close(forced);
        if (property != null && result) {
            property.unsubscribe(this);
        }
        return result;
    }

    @SuppressWarnings("UseSpecificCatch")
    public org.radixware.kernel.common.client.eas.SelectRequestHandle bindAsync() {
        try {
            final GroupModel group = (GroupModel) createModel();
            final org.radixware.kernel.common.client.eas.SelectRequestHandle handle = group.readMoreAsync();
            handle.setUserData(group);
            handle.addListener(this);
            return handle;
        } catch (Exception ex) {
            processExceptionOnOpen(ex);
            return null;
        }
    }

    @Override
    @SuppressWarnings("UseSpecificCatch")
    public void onResponseReceived(XmlObject response, org.radixware.kernel.common.client.eas.RequestHandle handle) {
        if (handle.getUserData() instanceof GroupModel) {
            final GroupModel group = (GroupModel) handle.getUserData();
            if (group.getEntitiesCount() == 0 && group.hasMoreRows()) {
                //group has not got response yet.
                group.onResponseReceived(response, handle);
                handle.removeListener(group);
            }
            try {
                if (getView() != null) {
                    close(true);
                }
                model = group;
                open();
            } catch (Exception ex) {
                processExceptionOnOpen(ex);
            } finally {
                setUpdatesEnabled(true);
            }
        }
    }
}