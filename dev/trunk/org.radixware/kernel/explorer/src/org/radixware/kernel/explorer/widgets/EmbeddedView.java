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

import com.trolltech.qt.QNoNativeResourcesException;
import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.Qt.WidgetAttribute;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.text.View;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.eas.RequestHandle;
import org.radixware.kernel.common.client.errors.CantOpenSelectorError;
import org.radixware.kernel.common.client.errors.ObjectNotFoundError;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.meta.RadEditorPresentationDef;
import org.radixware.kernel.common.client.meta.RadPresentationDef;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.IPresentationChangedHandler;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.RawEntityModelData;
import org.radixware.kernel.common.client.models.items.ModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.types.GroupRestrictions;
import org.radixware.kernel.common.client.types.ViewRestrictions;
import org.radixware.kernel.common.client.views.IEditor;
import org.radixware.kernel.common.client.views.IEmbeddedView;
import org.radixware.kernel.common.client.views.IEmbeddedViewContext;
import org.radixware.kernel.common.client.views.ISelector;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.client.widgets.IModifableComponent;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.explorer.errors.NotEnoughParamsForOpenError;
import org.radixware.kernel.explorer.utils.WidgetUtils;
import org.radixware.kernel.explorer.views.Editor;
import org.radixware.kernel.explorer.views.ErrorView;
import org.radixware.kernel.explorer.views.selector.Selector;
import org.radixware.schemas.webUi.Widget;

/**
 * Создает {@link Model модель} и открывает ее {@link View представление}.
 * <p>
 * Данный {@link IWidget виджет} открывает представление модели, созданной на основе
 * дочернего элемента проводника. Для открытия представлений в других режимах
 * следует использовать {@link EmbeddedEditor} и {@link EmbeddedSelector}.
 */
public class EmbeddedView extends ExplorerWidget implements IExplorerModelWidget, IPresentationChangedHandler, org.radixware.kernel.common.client.eas.IResponseListener, IEmbeddedView {
    
    private static final class CreateView extends QEvent{
        
        public final RadPresentationDef presentation;
        
        public CreateView(final RadPresentationDef presentation){
            super(QEvent.Type.User);
            this.presentation = presentation;
        }        
    }
    
    private static final List<EmbeddedView> OPENED_VIEWS = new ArrayList<>();

    protected Id childItemId;
    protected Model parentModel;
    private IView embeddedView;
    private IView preparedView;
    private boolean cancelCreateView;
    private IPresentationChangedHandler oldPch;
    protected Model model;
    private ComponentModificationRegistrator modificationRegistrator;
    private final ErrorView errWidget;
    private boolean editorWasRestricted = false;
    private boolean prepareViewSchedulted = false;
    private final IEmbeddedViewContext viewContext;
    private boolean synchronizedWithParentView;
    private final ViewRestrictions initialRestrictions = new ViewRestrictions();

    /**
     * Создает {@link Widget виджет} в заданном {@link View представлении}. После создания можно вызвать метод
     * {@link #bind()} или {@link #open()} для открытия {@link View представления} дочерней модели.
     * @param parentView - {@link IView Представление}, в котором создается этот {@link Widget виджет}.
     * @param childItemId - Идентификатор дочернего элемента проводника.
     */
    public EmbeddedView(final IClientEnvironment environment, final IView parentView, final Id childItemId) {
        this(environment, parentView, childItemId, false, new IEmbeddedViewContext.EmbeddedView());
    }
    
    public EmbeddedView(final IClientEnvironment environment, final IView parentView, final Id childItemId, final boolean synchronizeWithParentView) {
        this(environment, parentView, childItemId, parentView!=null && synchronizeWithParentView, new IEmbeddedViewContext.EmbeddedView());
    }    
    
    public EmbeddedView(final IClientEnvironment environment, final IView parentView, final Id childItemId, final boolean synchronizeWithParentView, final IEmbeddedViewContext context) {
        super(environment);
        errWidget = new ErrorView(environment, this);
        parentModel = parentView==null ? null : parentView.getModel();        
        this.childItemId = childItemId;
        this.viewContext = context==null ? new IEmbeddedViewContext.EmbeddedView() : context;
        this.synchronizedWithParentView = synchronizeWithParentView;
        setObjectName(childItemId==null ? "embedded_view" : "embedded_view_by_" + childItemId.toString());
        setupUi();        
    }

    protected EmbeddedView(IClientEnvironment environment) {
        this(environment,null,null,false,null);
    }    

    @Override
    public void setExplorerItem(final Model parentModel, final Id explorerItemId) {
        if (model != null && !close(false)) {
            return;
        }
        this.parentModel = parentModel;
        childItemId = explorerItemId;
    }

    public Id getExplorerItemId() {
        return childItemId;
    }

    private void setupUi() {
        setLayout(new QVBoxLayout(this));
        layout().setMargin(0);
        layout().addWidget(errWidget);
        errWidget.hide();
    }

    @Override
    public IView getView() {
        return embeddedView;
    }

    @Override
    public IEmbeddedViewContext getViewContext() {
        return viewContext;
    }

    @Override
    public boolean isSynchronizedWithParentView() {
        return synchronizedWithParentView;
    }    
    
    @Override
    public void setSynchronizedWithParentView(final boolean isSynchronized){
        synchronizedWithParentView = isSynchronized;
    }

    protected Model createModel() throws ServiceClientException, InterruptedException {
        if (parentModel == null || childItemId == null) {
            throw new NotEnoughParamsForOpenError(this);
        }
        final Model newModel = parentModel.getChildModel(childItemId);
        if (newModel instanceof GroupModel) {
            final GroupRestrictions restrictions = ((GroupModel) newModel).getRestrictions();
            if (!restrictions.getIsEditorRestricted()) {
                restrictions.setEditorRestricted(true);
                editorWasRestricted = true;
            }
        }else if (newModel instanceof EntityModel){
            final EntityModel entityModel = (EntityModel)newModel;
            if (!entityModel.wasRead()){
                entityModel.read();//read model to get visible editor pages         
            }
            if (entityModel.getEntityContext().getPresentationChangedHandler()!=this){//NOPMD
                oldPch = entityModel.getEntityContext().getPresentationChangedHandler();
                entityModel.getEntityContext().setPresentationChangedHandler(this);
            }
        }
        return newModel;
    }
    
    protected RadPresentationDef getExpectedPresentation(){
        return null;
    }
    
    protected void onViewCreated() {
        final QWidget viewWidget = (QWidget)embeddedView;
        viewWidget.hide();
        if (viewWidget.parent()!=this){
            viewWidget.setParent(this);
        }
        viewWidget.setAttribute(WidgetAttribute.WA_DeleteOnClose, true);
        layout().addWidget(viewWidget);
        setFocusProxy(viewWidget);
        embeddedView.getRestrictions().add(initialRestrictions);        
    }

    protected void onViewClosed() {
    }

    /**
     * Создает {@link Model модель} на основе заданных параметров и открывает ее {@link View представление}.
     * Параметры создания модели передаются либо в конструкторе либо через вызовы методов set у потомков.
     * В случае когда параметры не были заданы генерируется {@link NotEnoughParamsForOpenError}.
     * <p>
     * Если на момент вызова представление уже было открыто, произойдет переоткрытие.
     * <p>
     * @throws ModelCreationException Ошибка при созданиии {@link Model модели} (например, не удалось получить презентацию)
     * @throws ViewException Ошибка при открытии {@link View представления} (например, не удалось получить данные из модели)
     * @throws InterruptedException Открытие было прервано пользователем
     * @see #bind()
     * @see #isOpened()
     * @see #close(boolean)
     */
    @Override
    public final void open() throws ServiceClientException, InterruptedException {
        if (modificationRegistrator == null) {
            modificationRegistrator = new ComponentModificationRegistrator(this, this);
        }

        errWidget.hide();
        if (embeddedView != null) {
            close(true);
        }        
        setUpdatesEnabled(false);
        try {            
            if (model == null) {
                createModelAndPrepareView();
                if (model==null){//entity object creation was cancelled in event handler
                    close(true);
                    return;
                }                
            }
            if (preparedView!=null){
                embeddedView = preparedView;
                preparedView = null;
            }else{
                embeddedView = model.createView();
            }
            onViewCreated();
            final QWidget viewWidget = ((QWidget) embeddedView);
            if (embeddedView instanceof Editor) {
                ((Editor) embeddedView).open(model, modificationRegistrator);
                if (childItemId!=null) {
                    ((Editor)embeddedView).setToolBarHidden(true);//редактор родительского объекта использует панель инструменов внешнего редактора
                }
                ((Editor) embeddedView).modifiedStateChanged.connect(this, "onModifiedStateChanged()");
                if (inModifiedStateNow()){
                    //Чтобы предусмотреть модификацию значений свойств, которая была во время открытия
                    onModifiedStateChanged();
                }
            } else if (embeddedView instanceof Selector) {
                ((Selector) embeddedView).open(model, modificationRegistrator);
                ((Selector) embeddedView).modifiedStateChanged.connect(this, "onModifiedStateChanged()");
                if (inModifiedStateNow()){
                    //Чтобы предусмотреть модификацию значений свойств, которая была во время открытия
                    onModifiedStateChanged();
                }
            } else {
                embeddedView.open(model);
            }
            OPENED_VIEWS.add(this);
            viewWidget.show();
            setFocusProxy(viewWidget);
        }catch(CantOpenSelectorError err){
            if (err.getCause() instanceof InterruptedException) {
                close(true);
                throw (InterruptedException) err.getCause();
            }
            processErrorOnOpen(err);
            throw err;            
        }catch (RuntimeException | ServiceClientException err) {
            processErrorOnOpen(err);
            throw err;
        } catch (InterruptedException ex) {
            close(true);
            throw ex;
        } finally {
            setUpdatesEnabled(true);
        }
    }
    
    private void processErrorOnOpen(final Exception err){
        final String msg = getEnvironment().getMessageProvider().translate("TraceMessage", "Cannot open embedded view %s");
        getEnvironment().getTracer().error(String.format(msg, childItemId == null ? objectName() : "#" + childItemId.toString()), err);
        close(true);        
    }
    
    protected final Model createModelAndPrepareView() throws ServiceClientException, InterruptedException{
        closePreparedView();
        final RadPresentationDef expectedPresentation = getExpectedPresentation();
        if (expectedPresentation!=null){
            cancelCreateView = false;
            if (!prepareViewSchedulted){
                prepareViewSchedulted = true;
                QApplication.postEvent(this, new CreateView(expectedPresentation));                
            }
        }
        model = createModel();
        cancelCreateView = true;
        if (preparedView!=null 
            && (model==null
                || expectedPresentation==null 
                || !Utils.equals(expectedPresentation.getId(), model.getDefinition().getId()) )
            ){
            closePreparedView();
        }
        return model;
    }

    /**
     * @return true, если в данном {@link Widget виджете} создана {@link Model модель} и открыто ее {@link View представление}.
     * @see #open()
     * @see #close(boolean)
     */
    @Override
    public final boolean isOpened() {
        return embeddedView != null && embeddedView.getModel() != null;
    }

    /**
     * Закрывает {@link Model модель} и ее {@link View представление}.
     * @see View#close(boolean)
     * @param forced равен true, если закрытие нельзя отменить и оно произойдет
     * 		даже в случае, наличия в модели несохреаненных данных.
     * @return true, если было произведено закрытие, false, если закрытие было отменено.
     */
    @Override
    public boolean close(final boolean forced) {
        cancelAsyncActions();
        closePreparedView();
        if (model == null) {
            return true;
        }
        if (embeddedView != null) {
            if (!embeddedView.close(forced) && !forced) {
                return false;
            }
            if (embeddedView instanceof Editor) {
                ((Editor) embeddedView).modifiedStateChanged.disconnect(this);
            } else if (embeddedView instanceof Selector) {
                ((Selector) embeddedView).modifiedStateChanged.disconnect(this);
            }
            onViewClosed();
            setFocusProxy(null);
            embeddedView = null;
            OPENED_VIEWS.remove(this);
        }
        model.clean();
        if (model instanceof GroupModel) {
            final GroupRestrictions restrictions = ((GroupModel) model).getRestrictions();
            if (restrictions != null) {
                if (editorWasRestricted && restrictions.canBeAllowed(ERestriction.EDITOR)) {
                    restrictions.setEditorRestricted(false);
                    editorWasRestricted = false;
                }
            }
        }
        if (modificationRegistrator != null) {
            modificationRegistrator.close();
        }
        model = null;
        return true;
    }

    /**
     * Выполняются те же действия, что и в методе {@link #open()}, но при этом
     * производится стандартная обработка ошибок и исключений.
     * @see #open()
     * @see #isOpened()
     * @see #close(boolean)
     */
    @Override
    @SuppressWarnings("UseSpecificCatch")
    public void bind() {
        try {
            open();
        } catch (Exception ex) {
            processExceptionOnOpen(ex);
        }
    }

    public void cancelAsyncActions() {
        RequestHandle handle;
        for (int i = handles.size() - 1; i >= 0; i--) {
            handle = handles.get(i);
            if (handle.isActive()) {
                handle.cancel();
            }
        }
        handles.clear();
    }

    protected void processExceptionOnOpen(final Throwable exception) {
        close(true);
        ObjectNotFoundError objectNotFound = null;
        for (Throwable err = exception; err != null && err.getCause() != err; err = err.getCause()) {
            if (err instanceof ObjectNotFoundError) {
                objectNotFound = (ObjectNotFoundError) err;
            }
        }
        if (objectNotFound != null) {
            if (model instanceof EntityModel) {
                final EntityModel entity = (EntityModel) model;
                if (objectNotFound.inContextOf(entity)) {
                    objectNotFound.setContextEntity(entity);
                }
            } else if (model instanceof GroupModel) {
                objectNotFound.inContextOf((GroupModel) model);
            }
        }
        final String name = objectName() != null && !objectName().isEmpty()
                ? "\"" + objectName() + "\""
                : getEnvironment().getMessageProvider().translate("EmbeddedViewErr", "widget");
        final String message = getEnvironment().getMessageProvider().translate("EmbeddedViewErr", "Can't open %s");
        errWidget.show();        
        errWidget.setError(String.format(message, name), objectNotFound == null ? exception : objectNotFound);
    }

    /**
     * Уведомить {@link View представление} внутри {@link Widget виджета} о том, что
     * необходимо записать все редактируемые данные в {@link Model модель}.
     */
    public void finishEdit() {
        if (embeddedView != null) {
            if (embeddedView.getModel()!=null){
                embeddedView.getModel().finishEdit();
            }         
            else{
                embeddedView.finishEdit();
            }
        }
    }

    /* (non-Javadoc)
     * @see org.radixware.kernel.explorer.widgets.Widget#refresh(org.radixware.kernel.explorer.models.ModelItem)
     */
    @Override
    public void refresh(ModelItem changedItem) {
        bind();
    }

    /* (non-Javadoc)
     * @see org.radixware.kernel.explorer.widgets.Widget#setFocus(org.radixware.kernel.explorer.models.properties.Property)
     */
    @Override
    public boolean setFocus(Property property) {
        return false;
    }

    @Override
    public void reread() {
        if (embeddedView != null) {
            try {
                embeddedView.reread();
            } catch (ServiceClientException ex) {
                model.showException(ex);
            }
        }
    }

    @Override
    protected void customEvent(final QEvent event) {
        if (event instanceof CreateView){
            prepareViewSchedulted = false;
            event.accept();
            if (!cancelCreateView){
                try{
                    preparedView = 
                        createViewByPresentation(((CreateView)event).presentation, getEnvironment());
                    ((QWidget)preparedView).setParent(this);
                }catch(Exception exception){
                    getEnvironment().getTracer().error(exception);
                }
            }
        }
        super.customEvent(event);
    }
    
    private IView createViewByPresentation(final RadPresentationDef presentation, final IClientEnvironment environment){
        final Model model;
        if (presentation instanceof RadEditorPresentationDef){
            final Model holderModel = WidgetUtils.findNearestModel(this);
            model = presentation.createModel(new IContext.ContextlessEditing(environment,null,holderModel));
        }
        else if (presentation instanceof RadSelectorPresentationDef){
            final Model holderModel = WidgetUtils.findNearestModel(this);
            if (holderModel==null){
                model = GroupModel.openTableContextlessSelectorModel(getEnvironment(), (RadSelectorPresentationDef)presentation);
            }else{
                model = GroupModel.openTableContextlessSelectorModel(holderModel, (RadSelectorPresentationDef)presentation);
            }
        }else{
            model = null;
        }
        return model==null ? null : model.createView();
    }

    @Override
    protected void closeEvent(final QCloseEvent event) {
        try {
            close(true);
        } catch (Throwable ex) {
            final String name = objectName() != null && !objectName().isEmpty()
                    ? "\"" + objectName() + "\""
                    : getEnvironment().getMessageProvider().translate("EmbeddedViewErr", "widget");
            final String message = getEnvironment().getMessageProvider().translate("TraceMessage", "Error on closing %s: %s:\n%s");
            final String reason = ClientException.getExceptionReason(model.getEnvironment().getMessageProvider(), ex);
            final String stack = ClientException.exceptionStackToString(ex);
            getEnvironment().getTracer().error(String.format(message, name, reason, stack));
        }
        closePreparedView();
        super.closeEvent(event);
    }
    
    private void closePreparedView(){
        if (preparedView!=null){
            ((QWidget)preparedView).setAttribute(WidgetAttribute.WA_DeleteOnClose, true);
            preparedView.close(true);
            preparedView = null;
        }
    }
    
    protected final List<RequestHandle> handles = new ArrayList<>();

    @Override
    public void registerRequestHandle(final RequestHandle handle) {
        handles.add(handle);
    }

    @Override
    public void unregisterRequestHandle(final RequestHandle handle) {
        handles.remove(handle);
    }

    @Override
    public void onResponseReceived(final XmlObject response, final RequestHandle handle) {
    }

    @Override
    public void onServiceClientException(final ServiceClientException exception, final RequestHandle handle) {
        close(true);
        processExceptionOnOpen(exception);
    }

    @Override
    public void onRequestCancelled(final XmlObject request, final RequestHandle handler) {
        close(true);
    }

    @Override
    public QWidget asQWidget() {
        return this;
    }

    @Override
    public Model getModel() {
        return model;
    }

    @Override
    public boolean inModifiedStateNow() {
        if ((embeddedView instanceof IEditor) && ((IEditor) embeddedView).getActions().getUpdateAction().isEnabled()) {
            return true;
        }
        if ((embeddedView instanceof ISelector) && ((ISelector) embeddedView).getActions().getUpdateAction().isEnabled()) {
            return true;
        }
        return modificationRegistrator == null ? false : modificationRegistrator.isSomeChildComponentInModifiedState();
    }

    @SuppressWarnings("unused")
    protected void onModifiedStateChanged() {
        if (modificationRegistrator != null) {
            modificationRegistrator.getListener().notifyComponentModificationStateChanged(this, inModifiedStateNow());
        }
    }

    @Override
    public final Collection<IModifableComponent> getInnerComponents() {
        return modificationRegistrator.getRegisteredComponents();
    }    

    @Override
    public void setReadOnly(boolean isReadOnly) {
        if (isReadOnly){
            getRestrictions().add(ViewRestrictions.READ_ONLY);
        }else{
            getRestrictions().remove(ViewRestrictions.READ_ONLY);
        }
    }

    @Override
    public boolean isReadOnly() {
        return getRestrictions().getIsUpdateRestricted()
               && getRestrictions().getIsDeleteRestricted()
               && getRestrictions().getIsCreateRestricted();
    }

    @Override
    public ViewRestrictions getRestrictions() {
        return isOpened() ? getView().getRestrictions() : initialRestrictions;
    }
        
    public static List<EmbeddedView> findOpenedEmbeddedViews(final QWidget parentWidget, final boolean recursively){
        final List<EmbeddedView> result = new ArrayList<>();
        EmbeddedView current;
        for (int i=OPENED_VIEWS.size()-1; i>=0; i--){
            current = OPENED_VIEWS.get(i); 
            if (current.nativeId()!=0 && current.isOpened()){
                if (parentWidget==null){
                    result.add(current);
                }else{
                    try{
                        for (QWidget widget=current.parentWidget(); widget!=null && widget.nativeId()!=0; widget=widget.parentWidget()){                            
                            if (parentWidget.equals(widget)){
                                result.add(current);
                                break;
                            }
                            if (!recursively && widget instanceof EmbeddedView && OPENED_VIEWS.contains(widget)){
                                break;
                            }
                        }
                    }catch(QNoNativeResourcesException exception){
                        OPENED_VIEWS.remove(current);
                        continue;
                    }
                }
            }else{
                OPENED_VIEWS.remove(current);
            }
        }
        return result;
    }
    
    @Override
    public EntityModel onChangePresentation(final RawEntityModelData rawData,
                                            final Id newPresentationClassId, 
                                            final Id newPresentationId) {
        if (oldPch==null){
            final EntityModel entity = (EntityModel)getModel();
            final IContext.Entity context = (IContext.Entity) entity.getContext();
            final RadEditorPresentationDef presentation = 
                getEnvironment().getApplication().getDefManager().getEditorPresentationDef(newPresentationId);
            setUpdatesEnabled(false);
            try {
                close(true);
                EntityModel newEntity = presentation.createModel(context);
                newEntity.activate(rawData);
                model = newEntity;
                bind();
                return newEntity;
            } finally {
                setUpdatesEnabled(true);
            }            
        }else{
            setUpdatesEnabled(false);
            try {
                close(true);
                EntityModel newEntity = 
                    oldPch.onChangePresentation(rawData, newPresentationClassId, newPresentationId);
                model = newEntity;
                bind();
                return newEntity;
            } finally {
                setUpdatesEnabled(true);
            }                        
        }
    }    
}
