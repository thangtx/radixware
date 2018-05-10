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

package org.radixware.wps.views.selector;

import java.util.LinkedList;
import java.util.List;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.items.properties.PropertyRef;
import org.radixware.kernel.common.client.views.IEmbeddedSelector;
import org.radixware.kernel.common.client.views.ISelector.CurrentEntityHandler;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.utils.UIObjectUtils;
import org.radixware.wps.views.EmbeddedView;


public class EmbeddedSelector extends EmbeddedView implements org.radixware.kernel.common.client.eas.IResponseListener, IEmbeddedSelector {

    private final PropertyRef property;
    private RadSelectorPresentationDef presentation = null;

    public EmbeddedSelector(WpsEnvironment environment, IView parentView, Id childItemId) {
        super(environment, parentView, childItemId);
        setObjectName("embedded_selector");
        property = null;
    }

    /**
     * Создает вложенный cелектор для режима открытия группы на основе заданного свойства типа
     * ParentRef
     * @param property - свойство для которого будет открыт селектор
     */
    public EmbeddedSelector(WpsEnvironment environment, final PropertyRef property) {
        super(environment);
        setObjectName("embedded_selector");
        this.property = property;
    }

    /**
     * Создает вложенный селектор для режима бесконтектного открытия группы в заданной презентации.
     * @param selectorPresentationId - идентификатор {@link SelectorPresentationDef презентации селектора},
     *  в которой будет открыта группа
     * @param ownerClassId - идентификатор {@link org.radixware.kernel.explorer.meta.RadClassPresentationDef   класса}, где находится {@link RadSelectorPresentationDef презентация селектора}.	 */
    public EmbeddedSelector(WpsEnvironment environment, final Id ownerClassId, final Id selectorPresentationId) {
        this(environment);
        setObjectName("embedded_selector");
        presentation = getEnvironment().getDefManager().getSelectorPresentationDef(selectorPresentationId);
    }

    /**
     * Создает вложенный селектор, не устанавливая конкретного режима открытия.
     * Созданный таким образом вложенный селектор перед использованием нуждается в установке
     * презентации через вызов метода {@link #setPresentation(Id, Id)}.
     * 	 */
    public EmbeddedSelector(IClientEnvironment environment) {
        super((WpsEnvironment) environment);
        setObjectName("embedded_selector");
        property = null;
    }

    @Override
    public void setPresentation(final Id ownerClassId, final Id selectorPresentationId) {
        if (model != null && !close(false)) {
            return;
        }
        presentation = getEnvironment().getDefManager().getSelectorPresentationDef(selectorPresentationId);
        childItemId = null;
    }

    @Override
    public RadSelectorPresentationDef getPresentation() {
        return presentation;
    }

    @Override
    public void setExplorerItem(Model parentModel, final Id explorerItemId) {
        super.setExplorerItem(parentModel, explorerItemId);
        presentation = null;
    }

    @Override
    protected Model createModel() throws ServiceClientException, InterruptedException {
        final Model newModel;
        if (property != null) {
            newModel = property.openGroupModel();
            property.subscribe(this);
            setObjectName("rx_embedded_selector_by_prop_#"+property.getId().toString());
        } else if (getPresentation() != null) {
            final Model holderModel = UIObjectUtils.findNearestModel(this);
            if (holderModel==null){
                newModel = GroupModel.openTableContextlessSelectorModel(getEnvironment(), presentation);
            }else{
                newModel = GroupModel.openTableContextlessSelectorModel(holderModel, presentation);
            }
            setObjectName("rx_embedded_selector_#"+presentation.getId().toString());
        } else {
            newModel = super.createModel();
        }
        return newModel;
    }
    private final List<CurrentEntityHandler> customHandlers = new LinkedList<CurrentEntityHandler>();
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
        final RwtSelector selector = getView();
//        selector.opened.connect(opened, "emit(Object)");
//        selector.closed.connect(closed, "emit()");
//        selector.entityUpdated.connect(entityUpdated);
//        selector.entityRemoved.connect(entityRemoved);
//        selector.onDeleteAll.connect(onDeleteAll);
//        selector.entityCreated.connect(entityCreated);
        selector.addCurrentEntityHandler(defaultCurrentEntityHandler);
//        selector.onLeaveCurrentEntity.connect(onLeaveCurrentEntity);
//        selector.onSetCurrentEntity.connect(onSetCurrentEntity);
//        selector.insertedIntoTree.connect(insertedIntoTree);
    }

    private void disconnectSignals() {
//        opened.disconnect();
//        closed.disconnect();
//        entityUpdated.disconnect();
//        entityRemoved.disconnect();
//        onDeleteAll.disconnect();
//        entityCreated.disconnect();
        if (getView() != null) {
            getView().removeCurrentEntityHandler(defaultCurrentEntityHandler);
        }
//        onLeaveCurrentEntity.disconnect();
//        onSetCurrentEntity.disconnect();
//        insertedIntoTree.disconnect();
    }

    /**
     * Возвращает открытый селектор
     */
    @Override
    public RwtSelector getView() {
        return embeddedView != null ? (RwtSelector) embeddedView : null;
    }

    /**
     * Возвращает созданную для одного из режимов открытия {@link GroupModel модель группы}.
     * Если на момент вызова модель группы еще не была создана - произойдет попытка ее создания.
     * Если для создания модели у вложенного селектора недостаточно параметров сгенерируется {@link org.radixware.kernel.explorer.errors.NotEnoughParamsForOpenError}.
     * @return модель группы.
     */
    @Override
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
            //radix-8816
            getView().getActions().getRereadAction().trigger();
        } else {
            throw new IllegalStateException("widget was not opened");
        }
    }

    @Override
    public boolean close(boolean forced) {
        final boolean result = super.close(forced);
        if (property != null && result) {
            property.unsubscribe(this);
        }
        if (result) {
            disconnectSignals();
        }

        return result;
    }

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
    public void onResponseReceived(XmlObject response, org.radixware.kernel.common.client.eas.RequestHandle handle) {
        if (handle.getUserData() instanceof GroupModel) {
            final GroupModel group = (GroupModel) handle.getUserData();
            if (group.getEntitiesCount() == 0 && group.hasMoreRows()) {
                //group has not got response yet.
                group.onResponseReceived(response, handle);
                handle.removeListener(group);
            }
            try {
                if (embeddedView != null) {
                    close(true);
                }
                model = group;
                open();
            } catch (Exception ex) {
                processExceptionOnOpen(ex);
            } finally {
            }
        }
    }
}
