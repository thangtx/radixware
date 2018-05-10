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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Stack;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.dialogs.IMandatoryPropertiesConfirmationDialog;
import org.radixware.kernel.common.client.dialogs.IMessageBox;

import org.radixware.kernel.common.client.eas.CommandRequestHandle;
import org.radixware.kernel.common.client.eas.RequestHandle;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.errors.CantLoadCustomViewError;
import org.radixware.kernel.common.client.errors.ModelCreationError;
import org.radixware.kernel.common.client.errors.ObjectNotFoundError;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.exceptions.InvalidPropertyValueException;
import org.radixware.kernel.common.client.exceptions.ModelException;
import org.radixware.kernel.common.client.exceptions.PropertyIsMandatoryException;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.IExplorerItemsHolder;
import org.radixware.kernel.common.client.meta.IModelDefinition;
import org.radixware.kernel.common.client.meta.explorerItems.RadChildRefExplorerItemDef;
import org.radixware.kernel.common.client.meta.RadClassPresentationDef;
import org.radixware.kernel.common.client.meta.RadCommandDef;
import org.radixware.kernel.common.client.meta.RadEditorPresentationDef;
import org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef;
import org.radixware.kernel.common.client.meta.filters.RadFilterParamDef;
import org.radixware.kernel.common.client.meta.explorerItems.RadParagraphDef;
import org.radixware.kernel.common.client.meta.explorerItems.RadParagraphLinkDef;
import org.radixware.kernel.common.client.meta.explorerItems.RadParentRefExplorerItemDef;
import org.radixware.kernel.common.client.meta.RadParentRefPropertyDef;
import org.radixware.kernel.common.client.meta.RadPropertyDef;
import org.radixware.kernel.common.client.meta.explorerItems.RadSelectorExplorerItemDef;
import org.radixware.kernel.common.client.meta.TitledDefinition;
import org.radixware.kernel.common.client.meta.editorpages.IEditorPagesHolder;
import org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef;
import org.radixware.kernel.common.client.meta.editorpages.RadEditorPages;
import org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef;
import org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItems;
import org.radixware.kernel.common.client.meta.explorerItems.RadSelectorUserExplorerItemDef;
import org.radixware.kernel.common.client.meta.mask.validators.InvalidValueReason;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.models.items.Command;
import org.radixware.kernel.common.client.models.items.EditorPageModelItem;
import org.radixware.kernel.common.client.models.items.properties.PropertyRef;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.models.items.properties.PropertyArrBin;
import org.radixware.kernel.common.client.models.items.properties.PropertyArrBlob;
import org.radixware.kernel.common.client.models.items.properties.PropertyArrBool;
import org.radixware.kernel.common.client.models.items.properties.PropertyArrChar;
import org.radixware.kernel.common.client.models.items.properties.PropertyArrClob;
import org.radixware.kernel.common.client.models.items.properties.PropertyArrDateTime;
import org.radixware.kernel.common.client.models.items.properties.PropertyArrInt;
import org.radixware.kernel.common.client.models.items.properties.PropertyArrNum;
import org.radixware.kernel.common.client.models.items.properties.PropertyArrRef;
import org.radixware.kernel.common.client.models.items.properties.PropertyArrStr;
import org.radixware.kernel.common.client.models.items.properties.PropertyBin;
import org.radixware.kernel.common.client.models.items.properties.PropertyBlob;
import org.radixware.kernel.common.client.models.items.properties.PropertyBool;
import org.radixware.kernel.common.client.models.items.properties.PropertyChar;
import org.radixware.kernel.common.client.models.items.properties.PropertyClob;
import org.radixware.kernel.common.client.models.items.properties.PropertyDateTime;
import org.radixware.kernel.common.client.models.items.properties.PropertyInt;
import org.radixware.kernel.common.client.models.items.properties.PropertyNum;
import org.radixware.kernel.common.client.models.items.properties.PropertyObject;
import org.radixware.kernel.common.client.models.items.properties.PropertyReference;
import org.radixware.kernel.common.client.models.items.properties.PropertyStr;
import org.radixware.kernel.common.client.models.items.properties.PropertyXml;
import org.radixware.kernel.common.client.tree.IViewManager;
import org.radixware.kernel.common.client.tree.UserExplorerItemsStorage;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.utils.ClientValueFormatter;
import org.radixware.kernel.common.client.utils.ValueConverter;
import org.radixware.kernel.common.client.views.IDialog;
import org.radixware.kernel.common.client.views.IDialog.DialogResult;
import org.radixware.kernel.common.client.views.IEmbeddedView;
import org.radixware.kernel.common.client.views.IExplorerItemView;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.client.widgets.IModelWidget;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EDialogIconType;
import org.radixware.kernel.common.enums.EPropNature;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;

/**
 * Base class for explorer models. Provides common methods for view/model
 * interaction
 *
 */
public abstract class Model extends ResponseListener{
    
    protected static interface IPropertiesFilter{
        boolean isFiltered(final Property property);
    }    
    
    private static class DummyPropertiesFilter implements IPropertiesFilter{
        
        private final static DummyPropertiesFilter INSTANCE = new DummyPropertiesFilter();
        
        private DummyPropertiesFilter(){
        }
        
        @Override
        public boolean isFiltered(final Property property){
            return false;
        }
        
        public static IPropertiesFilter getInstance(){
            return INSTANCE;
        }
    }    
    
    protected final static String CHECK_MANDATORY_CONFIG_SETTING = 
        SettingNames.SYSTEM+"/"+SettingNames.EDITOR_GROUP+"/"+SettingNames.Editor.COMMON_GROUP
        +"/"+SettingNames.Editor.Common.CHECK_MANDATORY_ON_CLOSE;

    public static interface ITitleListener {

        void titleChanged(final String oldTitle, final String newTitle);
    }
    
    private class ChildModelPresentationChangedHandler implements IPresentationChangedHandler{                
        
        private final RadParentRefExplorerItemDef explItemDef;
        
        public ChildModelPresentationChangedHandler(RadParentRefExplorerItemDef parentItemDef){
            explItemDef = parentItemDef;
        }

        @Override
        public EntityModel onChangePresentation(final RawEntityModelData rawData, 
                                                final Id newPresentationClassId,
                                                final Id newPresentationId) {
            final EntityModel entityModel = (EntityModel)childModelsByExplorerItemId.get(explItemDef.getId());
            if (entityModel!=null){
                final IContext.Entity entityContext = entityModel.getEntityContext();
                final RadEditorPresentationDef presentation =  
                    getEnvironment().getApplication().getDefManager().getEditorPresentationDef(newPresentationId);
                final EntityModel newEntityModel = presentation.createModel(entityContext);
                childModelsByExplorerItemId.put(explItemDef.getId(),newEntityModel);
                return newEntityModel;
            }else{
                return null;
            }            
        }
        
    }
    /**
     * Model presentation attributes
     */
    private final IModelDefinition def;
    protected Map<Id, Property> properties = null;
    protected Map<Id, Command> commands = null;
    private IContext.Abstract context = null;
    private IView view = null;
    private List<Id> enabledCommands = null;
    private List<ITitleListener> titleListeners;
    private final Map<Id, Model> childModelsByExplorerItemId = new HashMap<>();
    protected Model parentModel = null;
    private IExplorerItemView explorerItemView = null;
    private String title = null;
    private Icon icon = null;
    private Collection<Id> propIdsToIgnoreUndefinedValCheck;
    private boolean strongCheckMandatoryProperties = true;

    public RadClassPresentationDef getRadMeta() {
        return null;
    }

    /**
     * Constructs a new explorer model by definition
     *
     * @param definition presentation attributes of this model
     */
    protected Model(final IClientEnvironment environment, final IModelDefinition definition) {
        super(environment);
        this.def = definition;
        title = definition.getTitle();
        icon = definition.getIcon();
    }

    public IModelDefinition getDefinition() {
        return def;
    }

    /**
     * Method removes all
     * {@link org.radixware.kernel.explorer.models.items.ModelItem model items}
     * from model, clear all cached server data, and called recursively for
     * child models (see {@link #getChildModel(java.lang.String)} method).
     * <p><b>NOTE:</b> If {@link IView view} activated calling this method will
     * close it by executing {@link IView#close(boolean) } method with forced
     * flag setted on. After calling this method model is still usefull and
     * request for model item will create new one.
     */
    public void clean() {

        if (view != null) {
            view.close(true);
            getEnvironment().getClipboard().removeAllChangeListeners(this);
        }
        
        titleListeners = null;
        view = null;

        closeActiveRequestHandles(true);

        if (properties != null) {
            final List<Id> propertiesToClean = new LinkedList<>();
            for (Property property : properties.values()) {
                property.unsubscribeAll();
                final EPropNature nature = property.getDefinition().getNature();
                if (nature!=EPropNature.GROUP_PROPERTY){
                    propertiesToClean.add(property.getId());
                }
            }
            for (Id propertyId: propertiesToClean){
                properties.remove(propertyId);
            }
        }
        if (commands != null) {
            for (Command command : commands.values()) {
                    command.unsubscribeAll();
            }
            commands.clear();
        }                

        final IViewManager viewManager = findNearestExplorerItemView() != null ? findNearestExplorerItemView().getExplorerTree().getViewManager() : null;
        for (Model childModel : childModelsByExplorerItemId.values()) {
            //if child model view was opened separately we shoud not to clear this child model
            if (viewManager == null || !viewManager.isViewOpenedForModel(childModel)) {
                childModel.clean();
            }
        }
    }

    /**
     * Returns true if all data modifications in this model and child models was
     * stored on server otherwise returns false. It will attempt to save data if
     * needed.
     *
     * @return true if all data modifications was stored on server.
     */
    public boolean canSafelyClean(final CleanModelController controller){
        if (controller.needToCloseRequestHandles(this) && !closeActiveRequestHandles(false)) {
            return false;
        }
        if (getView() != null && !getView().canSafelyClose(controller)) {
            return false;
        }
        for (Model model : childModelsByExplorerItemId.values()) {
            if (view != null && model.getView() == null && !model.canSafelyClean(controller)) {
                return false;
            }
        }
        return true;
    }

    public void finishEdit() {
        if (getView() != null && getView().getModel()!=null) {
            getView().finishEdit();
        }
    }

    /**
     * Register opened {@link IView view} in model. <p>Only one view may be
     * registered for model at a time. Clearning model will cause closing
     * regitered view. You may call this method with null parameter to unlink
     * model and view.
     *
     * @param view_ a view to register.
     * @see #getView()
     * @see #createView()
     */
    public void setView(IView view_) {
        if (view != null && view_ != null) {
            throw new IllegalStateException("model " + toString() + " already has another view.");
        }
        if (view == null && view_ == null) {
            return;
        }
        view = view_;
        if (view == null) {
            //view of child models should be also closed
            //because they may be open directly from explorer tree.
            final IViewManager viewManager = findNearestExplorerItemView() != null ? findNearestExplorerItemView().getExplorerTree().getViewManager()
                    : null;
            for (Model childModel : childModelsByExplorerItemId.values()) {
                if (childModel.view != null && (viewManager == null || !viewManager.isViewOpenedForModel(childModel))) {
                    childModel.view.close(true);
                }
            }
            if (properties != null) {
                for (Property property : properties.values()) {
                    property.unsubscribeAll();
                }
            }
            if (commands != null) {
                for (Command command : commands.values()) {
                    command.unsubscribeAll();
                }
            }
            getEnvironment().getClipboard().removeAllChangeListeners(this);
        } else {
            beforeOpenView();
        }
    }

    /**
     * Returns {@link IView view} registered in model at this moment
     *
     * @return view opened for this model.
     * @see #setView(org.radixware.kernel.explorer.views.IView)
     * @see #createView()
     */
    public final IView getView() {
        return view;
    }

    /**
     * Method creates a new {@link IView view} for representing model data.
     * <p>By default new view creates by calling {@link IModelDefinition#createStandardView()
     * } method, but derived classes may override {@link #getCustomViewId()}
     * method to create it's own custom views. <p>Once new was view created it
     * may be opened with {@link IView#open(org.radixware.kernel.explorer.models.Model)
     * } method.
     *
     * @return new view for this model.
     * @see #setView(org.radixware.kernel.explorer.views.IView)
     * @see #getView()
     * @see #getCustomViewId()
     */
    public IView createView() {
        final Id customDialogId = getCustomViewId();
        if (customDialogId != null) {
            try {
                Class<IView> classView = getEnvironment().getDefManager().getCustomViewClass(customDialogId);
                return classView.newInstance();
            } catch (Exception e) {
                throw new CantLoadCustomViewError(this, e);
            }
        } else {
            return def.createStandardView(getEnvironment());
        }
    }
    
    public final IView findNearestView(){
        if (view==null){
            final Model ownerModelWithView = findOwner(new IModelFinder() {
                @Override
                public boolean isTarget(final Model model) {
                    return model.getView()!=null;
                }
            });
            return ownerModelWithView==null ? null : ownerModelWithView.getView();
        }else{
            return view;
        }
    }

    /**
     * Returns identifier of cusom view. If no custom view provided for this
     * model method returns null.
     *
     * @return custom view identifier
     */
    public Id getCustomViewId() {
        return null;
    }

    //ExplorerItemView
    /**
     * Returns explorer item view for this model. {@link ExplorerItemView}
     * determines model reprethentation If no explorer item view registered for
     * this model this method returns explorer item view of nearest parent model
     * which have it.
     *
     * @return explorer item view.
     * @see
     * #setExplorerItemView(org.radixware.kernel.explorer.tree.ExplorerItemView)
     */
    @SuppressWarnings("empty-statement")
    public final IExplorerItemView findNearestExplorerItemView() {
        Model model;
        for (model = this; model != null && model.explorerItemView == null; model = model.parentModel) {
            if (model.getContext() instanceof IContext.ContextlessEditing) {
                final IContext.ContextlessEditing contextlessEditing = ((IContext.ContextlessEditing) model.getContext());
                if (contextlessEditing.getExplorerItemView() != null) {
                    return contextlessEditing.getExplorerItemView();
                }
            }
        }
        return model != null ? model.explorerItemView : null;
    }

    /**
     * Register explorer item view for this model
     *
     * @param view explorer item view of this model.
     * @see #getExplorerItemView()
     */
    public final void setExplorerItemView(IExplorerItemView view) {
        this.explorerItemView = view;
        if (view != null && parentModel != null && explorerItemView.getExplorerItemId() != null) {
            final RadExplorerItemDef explorerItem =
                    parentModel.getChildExplorerItemDef(explorerItemView.getExplorerItemId());
            if (explorerItem.hasTitle()) {
                explorerItemView.setTitle(explorerItem.getTitle());
            }
        }
    }
    
    public final IExplorerItemView getExplorerItemView(){
        return explorerItemView;
    }

    protected final void removeFromTree() {
        if (explorerItemView != null) {
            explorerItemView.remove();
            explorerItemView = null;
        }
    }

    /**
     * Returns model title
     *
     * @return model title
     * @see #setTitle(java.lang.String)
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns model title to show in window header
     *
     * @return model title
     * @see #setTitle(java.lang.String)
     */
    public String getWindowTitle() {
        return ClientValueFormatter.capitalizeIfNecessary(getEnvironment(), getTitle());
    }

    /**
     * Set model title. <p>Title of registered
     * {@link ExplorerItemView explorer item view} will be also updated. Default
     * value recieves from {@link IModelDefinition#getTitle() model definition}
     *
     * @param newTitle model title
     * @see #getTitle()
     */
    public void setTitle(final String newTitle) {
        if (!Objects.equals(title, newTitle)) {
            final String oldTitle = title;
            title = newTitle;
            if (explorerItemView != null) {
                explorerItemView.refresh();
            }
            changeTitleNotify(oldTitle, newTitle);
        }
    }

    /**
     * Returns model icon.
     *
     * @return model icon.
     * @see #setIcon(com.trolltech.qt.gui.QIcon)
     */
    public Icon getIcon() {
        return icon;
    }

    /**
     * Set model icon. <p> Icon of registered
     * {@link ExplorerItemView explorer item view} will be also updated. Default
     * value recieves from {@link IModelDefinition#getIcon() model definition}
     *
     * @param icon
     * @see #getIcon()
     */
    public void setIcon(Icon icon) {
        this.icon = icon;
        if (explorerItemView != null) {
            explorerItemView.refresh();
        }
    }

    //Properties
    /**
     * Creates a new property model item for this model by definition. In common
     * cases if model has properties this method must be reimplemented in
     * derived classes. Normally you do not need to call this method directly
     * use {@link #activateProp(org.radixware.kernel.common.types.Id) } instead.
     *
     * @param def property presentation attributes
     * @return new model property
     * @see #activateProperty(org.radixware.kernel.common.types.Id)
     */
    protected Property createProperty(RadPropertyDef def) {
        switch (def.getType()) {
            case INT:
                return new PropertyInt(this, def);
            case ARR_INT:
                return new PropertyArrInt(this, def);
            case NUM:
                return new PropertyNum(this, def);
            case ARR_NUM:
                return new PropertyArrNum(this, def);
            case CHAR:
                return new PropertyChar(this, def);
            case ARR_CHAR:
                return new PropertyArrChar(this, def);
            case STR:
                return new PropertyStr(this, def);
            case XML:
                return new PropertyXml(this, def);
            case ARR_STR:
                return new PropertyArrStr(this, def);
            case DATE_TIME:
                return new PropertyDateTime(this, def);
            case ARR_DATE_TIME:
                return new PropertyArrDateTime(this, def);
            case CLOB:
                return new PropertyClob(this, def);
            case ARR_CLOB:
                return new PropertyArrClob(this, def);
            case BLOB:
                return new PropertyBlob(this, def);
            case BIN:
                return new PropertyBin(this, def);
            case ARR_BIN:
                return new PropertyArrBin(this, def);
            case ARR_BLOB:
                return new PropertyArrBlob(this, def);
            case BOOL:
                return new PropertyBool(this, def);
            case ARR_BOOL:
                return new PropertyArrBool(this, def);
            case PARENT_REF:
                if (def instanceof RadFilterParamDef) {
                    return new PropertyRef(this, (RadFilterParamDef) def);
                } else {
                    return new PropertyRef(this, (RadParentRefPropertyDef) def);
                }
            case OBJECT:
                return new PropertyObject(this, (RadParentRefPropertyDef) def);
            case ARR_REF:
                if (def instanceof RadFilterParamDef){
                    return new PropertyArrRef(this, (RadFilterParamDef) def);
                }else{
                    return new PropertyArrRef(this, (RadParentRefPropertyDef) def);
                }
            default:
                throw new IllegalArgumentException("Can't create property for type " + def.getType());
        }
    }

    /**
     * Returns property with id identifier. If property with specified id is not
     * exists in this model method {@link #activateProp(org.radixware.kernel.common.types.Id)
     * } called.
     *
     * @param id property identifier
     * @return property model item.
     * @see #getActiveProperties()
     */
    public final Property getProperty(Id id) {
        Property p = properties != null ? properties.get(id) : null;
        if (p == null) {
            p = activateProperty(id);
        }
        return p;
    }

    /**
     * Method creates a new property with specified identifier and register it
     * in model.
     * <p>Property presentation attributes requested from
     * {@link IModelDefinition#getPropertyDefById(org.radixware.kernel.common.types.Id) model definition}.
     *
     * @param id property identifier
     * @return new Property
     * @see #getActiveProperties()
     * @see #createProperty(org.radixware.kernel.explorer.meta.RadPropertyDef)
     */
    protected Property activateProperty(Id id) {
        final RadPropertyDef d = getPropertyDef(id);
        final Property p = createProperty(d);
        if (properties == null) {
            properties = new HashMap<>();
        }
        properties.put(id, p);
        return p;
    }

    protected RadPropertyDef getPropertyDef(Id id) {
        RadClassPresentationDef self = getRadMeta();
        if (self != null && self.isPropertyDefExistsById(id)) {
            RadPropertyDef prop = self.getPropertyDefById(id);
            if (prop != null) {
                return prop;
            }
        }

        return def.getPropertyDefById(id);
    }
    
    /**
     * Returns collection of all properties activated in this model
     *
     * @return activated properties.
     * @see #getProperty(org.radixware.kernel.common.types.Id)
     * @see #activateProperty(org.radixware.kernel.common.types.Id)
     */    
    public Collection<Property> getActiveProperties() {
        return getActiveProperties(DummyPropertiesFilter.getInstance());
    }

    protected Collection<Property> getActiveProperties(final IPropertiesFilter filter) {
        if (properties == null) {
            return Collections.emptyList();
        }
        final Collection<Property> result = new LinkedList<>();
        for (Property property: properties.values()){
            if (!filter.isFiltered(property)){
                result.add(property);
            }
        }
        return Collections.unmodifiableCollection(result);
    }
    
    public final Collection<Property> getLocalProperties(){
        if (properties==null || properties.isEmpty()){
            return Collections.emptyList();
        }
        final List<Property> result = new ArrayList<>();        
        for (Property property: properties.values()){
            if (property.isLocal()){
                result.add(property);
            }
        }
        return Collections.unmodifiableList(result);
    }
    
    protected List<Property> getActivePropertiesByOrder() {
        return getActivePropertiesByOrder(DummyPropertiesFilter.getInstance());
    }

    protected List<Property> getActivePropertiesByOrder(final IPropertiesFilter filter) {
        final List<Property> result = new LinkedList<>();
        if (def instanceof IEditorPagesHolder) {
            final Stack<RadEditorPageDef> pagesStack = new Stack<>();
            {
                final RadEditorPages pages = ((IEditorPagesHolder) def).getEditorPages();
                for (int i = pages.getTopLevelPages().size() - 1; i >= 0; i--) {
                    pagesStack.push(pages.getTopLevelPages().get(i));
                }
            }
            RadEditorPageDef page;
            List<RadEditorPageDef> childPages;
            final List<Id> propertyIds = new LinkedList<>();
            while (!pagesStack.isEmpty()) {
                page = pagesStack.pop();
                if (page instanceof RadStandardEditorPageDef) {
                    final RadStandardEditorPageDef.PropertiesGroup propGroup = ((RadStandardEditorPageDef) page).getRootPropertiesGroup();
                    collectPropertiesInGroup(((RadStandardEditorPageDef) page), propGroup, propertyIds);                    
                } 
                childPages = page.getSubPages();
                for (int i = childPages.size() - 1; i >= 0; i--) {
                    pagesStack.push(childPages.get(i));
                }                
            }
            {
                Property property;
                for (Id propertyId : propertyIds) {
                    property = getProperty(propertyId);
                    if (!result.contains(property) && !filter.isFiltered(property) && property.isActivated()) {
                        result.add(property);
                    }
                }
            }
            final Collection<Property> allActiveProperties = getActiveProperties(filter);
            for (Property property : allActiveProperties) {
                if (!result.contains(property) && property.hasSubscriber() && property.getDefinition().getType() == EValType.PARENT_REF) {
                    result.add(property);
                }
            }
            for (Property property : allActiveProperties) {
                if (!result.contains(property) && property.hasSubscriber() && property.getDefinition().getType() != EValType.PARENT_REF) {
                    result.add(property);
                }
            }
        } else {
            final Collection<Property> allActiveProps = getActiveProperties(filter);
            for (Property property : allActiveProps) {
                if (!result.contains(property) && property.hasSubscriber()) {
                    result.add(property);
                }
            }
        }
        return result;
    }

    private void collectPropertiesInGroup(final RadStandardEditorPageDef pageDef, final RadStandardEditorPageDef.PropertiesGroup propGroup, final List<Id> propertyIds) {
        final List<RadStandardEditorPageDef.PageItem> items = propGroup.getPageItems();
        Id itemId;
        for (RadStandardEditorPageDef.PageItem item : items) {
            itemId = item.getItemId();
            if (itemId.getPrefix() == EDefinitionIdPrefix.EDITOR_PAGE_PROP_GROUP) {
                collectPropertiesInGroup(pageDef, pageDef.getPropertiesGroup(itemId), propertyIds);
            } else if (getDefinition().isPropertyDefExistsById(itemId)) {
                propertyIds.add(itemId);
            }
        }
    }        

    private static class ChildViewPropertiesChecker implements IView.Visitor {

        private InvalidPropertyValueException invalidValue = null;
        private PropertyIsMandatoryException propertyIsMandatory = null;
        private final boolean strongCheckIfMandatoryPropertiesDefined;
        
        public ChildViewPropertiesChecker(final boolean strongCheckIfMandatoryPropertiesDefined){
            this.strongCheckIfMandatoryPropertiesDefined = strongCheckIfMandatoryPropertiesDefined;
        }

        @Override
        public void visit(final IEmbeddedView embeddedView) {
            if (embeddedView.isSynchronizedWithParentView()) {
                try {
                    embeddedView.getModel().checkPropertyValues(strongCheckIfMandatoryPropertiesDefined);
                } catch (PropertyIsMandatoryException exception) {
                    propertyIsMandatory = exception;
                } catch (InvalidPropertyValueException exception) {
                    invalidValue = exception;
                }
            }
        }

        @Override
        public boolean cancelled() {
            return invalidValue != null || propertyIsMandatory != null;
        }

        public void checkResults() throws InvalidPropertyValueException, PropertyIsMandatoryException {
            if (invalidValue != null) {
                throw invalidValue;
            }
            if (propertyIsMandatory != null) {
                throw propertyIsMandatory;
            }
        }
    }
    
    protected final void setPropsToIgnoreUndefinedValCheck(final Collection<Property> props){
        if (props==null){
            propIdsToIgnoreUndefinedValCheck = null;
        }else{
            propIdsToIgnoreUndefinedValCheck = new LinkedList<>();
            for (Property property: props){
                propIdsToIgnoreUndefinedValCheck.add(property.getId());
            }
        }
    }
    
    protected final boolean isUndefinedValCheckIgnored(final Property property){
        if (property.isValEdited()){
            return false;
        }
        return propIdsToIgnoreUndefinedValCheck==null || propIdsToIgnoreUndefinedValCheck.contains(property.getId());
    }

    /**
     * This method checks that activated properties have valid values.
     * <p>Validation performed in next sequence:
     * <ul>
     * <li>Checking if all activated inheritable properties with own value have
     * values not equal to inheritable mark. If for some property validation
     * fails {@link InvalidPropertyValueException} thrown.
     * <li>Checking if all visible mandatory properties of PARENT_REF
     * {@link RadPropertyDef#getType()  type} have not null values. If for some
     * property validation fails {@link PropertyIsMandatoryException } thrown.
     * <li>Checking if all visible mandatory properties of other types have not
     * null values. If for some property validation fails {@link PropertyIsMandatoryException
     * } thrown.
     * </ul>
     *
     * @throws PropertyIsMandatoryException if activated visible mandatory
     * property has null value
     * @throws InvalidPropertyValueException if activated inheritable property
     * has own value which equals to inheritable mark
     * @see Property#isMandatory()
     * @see Property#getValueObject()
     * @see Property#hasOwnValue()
     * @see RadPropertyDef#isInheritable()
     * @see RadPropertyDef#getValInheritanceMark()
     */
    protected final void checkPropertyValues(final boolean strongCheckIfMandatoryPropertiesDefined) throws PropertyIsMandatoryException, InvalidPropertyValueException {
        if (strongCheckIfMandatoryPropertiesDefined){
            checkPropertyValues();
        }else{
            strongCheckMandatoryProperties = false;
            try{
                checkPropertyValues();
            }finally{
                strongCheckMandatoryProperties = true;
            }            
        }
    }
    
    protected void checkPropertyValues()  throws PropertyIsMandatoryException, InvalidPropertyValueException {
        if (properties == null || properties.isEmpty()) {
            return;
        }
        Object value;
        final List<Property> props = getActivePropertiesByOrder();
        boolean propertyIsAccessibleFromEditorPage;
        final IEditorPagesHolder editorPagesHolder =
                def instanceof IEditorPagesHolder ? (IEditorPagesHolder) def : null;
        final Map<RadPropertyDef, InvalidValueReason> invalidValues = new LinkedHashMap<>();
        Collection<Id> pageIds;
        for (Property property : props) {
            //Проверяем только те свойства, на которые есть подписчики
            //и те, которые зарегистрированы на странице редактора
            if (editorPagesHolder==null){
                pageIds = Collections.emptyList();
            }else{
                pageIds = editorPagesHolder.getEditorPages().findPagesWithProperty(property.getId());                
            }
            propertyIsAccessibleFromEditorPage = false;
            if (!pageIds.isEmpty()){
                if (this instanceof ModelWithPages){
                    final ModelWithPages self = (ModelWithPages)this;
                    boolean pageIsAccessible;
                    for (Id pageId: pageIds){
                        if (self.isEditorPageExists(pageId)){
                            pageIsAccessible = true;
                            for (EditorPageModelItem page=self.getEditorPage(pageId); page!=null; page=page.getParentPage()){
                                if (!page.isVisible() || !page.isEnabled()){
                                    pageIsAccessible = false;
                                    break;
                                }
                            }
                            if (pageIsAccessible){
                                propertyIsAccessibleFromEditorPage = true;
                                break;
                            }
                        }
                    }
                }else{
                    propertyIsAccessibleFromEditorPage = true;
                }
            }
            
            if (propertyIsAccessibleFromEditorPage || property.hasSubscriber()) {                
                //поиск мандаторных свойств
                if (!property.hasValidMandatoryValue()
                    && property.isVisible() 
                    && !property.isReadonly()
                    && property.isEnabled()
                    && (strongCheckMandatoryProperties || !isUndefinedValCheckIgnored(property))
                    ) {
                    onInvalidValue(property.getId(), InvalidValueReason.NOT_DEFINED);
                    invalidValues.put(property.getDefinition(), InvalidValueReason.NOT_DEFINED);
                    continue;
                }
                value = property.getValueObject();
                //проверка наследуемых значений:
                if (property.hasOwnValue() && !property.isOwnValueAcceptable(value)) {
                    final String valAsStr = value == null ? "NULL" : "\"" + value.toString() + "\"";
                    final InvalidValueReason reason =
                            InvalidValueReason.Factory.createForInfeasibleValue(getEnvironment(), valAsStr);
                    onInvalidValue(property.getId(), reason);
                    invalidValues.put(property.getDefinition(), reason);
                    continue;
                }
                //проверка на правильность ввода
                if (property.isActivated()
                        && property.isVisible()
                        && !property.isReadonly()
                        && property.isEnabled()
                        && property.hasOwnValue()
                        //??? && property.isValEdited() ???
                        ) {
                    if (property.isUnacceptableInputRegistered()){
                        invalidValues.put(property.getDefinition(), property.getUnacceptableInput().getReason());
                    } else if (value != null){
                        final ValidationResult state = getPropertyValueState(property.getId());
                        if (state != ValidationResult.ACCEPTABLE) {
                            onInvalidValue(property.getId(), state.getInvalidValueReason());
                            invalidValues.put(property.getDefinition(), state.getInvalidValueReason());
                        }
                    }
                }
            }
        }
        if (!invalidValues.isEmpty()) {
            throw InvalidPropertyValueException.createForProperties(this, invalidValues);
        }
        if (getView() != null) {
            final ChildViewPropertiesChecker checker = 
                    new ChildViewPropertiesChecker(strongCheckMandatoryProperties);
            getView().visitChildren(checker, false);
            checker.checkResults();
        }        
    }

    protected void onInvalidValue(final Id propertyId, final InvalidValueReason reason) throws InvalidPropertyValueException {
    }
    
    protected boolean warnAboutMandatoryProperties(final String title, final String message, final List<Property> properties, final boolean showDontAskAgainOption) {
        if (properties != null && !properties.isEmpty()) {
            final List<String> propertyTitles = new LinkedList<>();
            for (Property property : properties) {
                propertyTitles.add(property.getTitle());
            }
            return showMandatoryPropertiesWarning(title, message, propertyTitles, showDontAskAgainOption);
        }
        return true;
    }    
    
    protected boolean showMandatoryPropertiesWarning(final String title, final String message, final List<String> propertyTitles, final boolean showDontAskAgainOption) {
        if (propertyTitles != null && !propertyTitles.isEmpty()) {
            final boolean confirmed;
            final boolean dontShowAgain;            
            if (propertyTitles.size() > 1) {
                final IMandatoryPropertiesConfirmationDialog confirmationDialog =
                    getApplication().getDialogFactory().newMandatoryPropertiesConfirmationDialog(getEnvironment(), getView(), propertyTitles);
                confirmationDialog.setWindowTitle(title);
                confirmationDialog.setConfirmationMessage(message);
                confirmationDialog.setDontAskAgainOptionVisible(showDontAskAgainOption);
                confirmed = confirmationDialog.execDialog() == IDialog.DialogResult.ACCEPTED;
                dontShowAgain = confirmationDialog.isDontAskAgainOptionChecked();
            } else {
                final MessageProvider mp = getEnvironment().getMessageProvider();
                final String messageTemplate = mp.translate("Editor", "The \'%1$s\' mandatory property is not defined.\n%2$s");
                final String dialogMessage = String.format(messageTemplate, propertyTitles.get(0),message);
                final EnumSet<EDialogButtonType> buttons = EnumSet.of(EDialogButtonType.YES,EDialogButtonType.NO);
                final IMessageBox messageDialog = getEnvironment().newMessageBoxDialog(dialogMessage, title, EDialogIconType.QUESTION, buttons);
                if (showDontAskAgainOption){
                    messageDialog.setOptionText(mp.translate("Editor", "Don't ask me again"));
                }
                confirmed = messageDialog.execMessageBox()==EDialogButtonType.YES;
                dontShowAgain = messageDialog.isOptionActivated();                
            }
            if (showDontAskAgainOption && dontShowAgain){
                getEnvironment().getConfigStore().writeBoolean(CHECK_MANDATORY_CONFIG_SETTING, false);                
            }
            return confirmed;
        }
        return true;        
    }

    public ValidationResult getPropertyValueState(final Id propertyId) {
        return getProperty(propertyId).validateValue();
    }
        
    public final List<Property> findPropertiesWithUnacceptableInput(){
        final List<Property> result = new LinkedList<>();
        if (properties == null || properties.isEmpty()
                || !getEnvironment().getDefManager().getAdsVersion().isSupported()) {
            return result;
        }
        final List<Property> props = getActivePropertiesByOrder();
        for (Property property : props) {
            //Проверяем только те свойства, на которые есть подписчики            
            if (property.hasSubscriber() 
                && property.isVisible() 
                && !property.isReadonly() 
                && property.isUnacceptableInputRegistered()
                && !result.contains(property)){                
                result.add(property);
            }
        }
        return result;        
    }
    
    public final boolean checkInputAcceptable(){
        final List<Property> propertiesWithUnacceptableInput = findPropertiesWithUnacceptableInput();
        if (propertiesWithUnacceptableInput.isEmpty()){
            return true;
        }else{
            final String messageTitle = getEnvironment().getMessageProvider().translate("ExplorerException", "Unacceptable Input");            
            final String messageText;
            if (propertiesWithUnacceptableInput.size()==1){
                final Property property = propertiesWithUnacceptableInput.get(0);
                final String messageTemplate = 
                    getEnvironment().getMessageProvider().translate("ExplorerException", "Field '%1$s' has unacceptable input.\n%2$s");
                final String reason = property.getUnacceptableInput().getMessageText();
                messageText = String.format(messageTemplate, property.getTitle(), reason);
            }else{
                final StringBuilder propertyTitles = new StringBuilder();
                for (Property property: propertiesWithUnacceptableInput){
                    if (propertyTitles.length()>0){
                        propertyTitles.append(",\n");
                    }
                    propertyTitles.append(property.getTitle());
                }
                final String messageTemplate = 
                    getEnvironment().getMessageProvider().translate("ExplorerException", "Following fields has unacceptable input:\n%1$s");
                messageText = String.format(messageTemplate, propertyTitles.toString());
            }
            getEnvironment().messageError(messageTitle, messageText);
            propertiesWithUnacceptableInput.get(0).setFocused();
            return false;
        }        
    }

    //              Commands
    /**
     * Creates a new command model item for this model by definition. In common
     * cases if model has commands this method must be reimplemented in derived
     * classes.
     *
     * @param def command presentation attributes
     * @return model command
     */
    protected Command createCommand(RadCommandDef def) {
        return def.newCommand(this);
    }

    /**
     * Get identifiers of commands accessible in this model according to model
     * state {@link Context context} and
     * {@link  IModelDefinition presentation attributes}.
     *
     * @return List of command identifiers.
     * @see #getCommand(org.radixware.kernel.common.types.Id)
     */
    public List<Id> getAccessibleCommandIds() {
        if (enabledCommands == null) {
            enabledCommands = new ArrayList<>();
            for (RadCommandDef command : def.getEnabledCommands()) {
                if (isCommandAccessible(command)) {
                    enabledCommands.add(command.getId());
                }
            }
        }
        return enabledCommands;
    }

    /**
     * Returns true if
     * <code>command</code> is accessible in this model; otherwise returns
     * false. Calls one time in getAccessibleCommandIds() than result is cached.
     *
     * @param command command for chek.
     * @return true if command is enabled.
     * @see #getAccessibleCommandIds()
     */
    protected boolean isCommandAccessible(RadCommandDef command) {
        return true;
    }

    ; 

    /**
     * Returns true if <code>command</code> is enabled for this model; otherwise returns false.
     * Calls from Command.isEnabled each time when need to calculate state of command button.
     * @param command command for chek.
     * @return true if command is enabled.
     * @see #getAccessibleCommandIds()
     */    
    public boolean isCommandEnabled(RadCommandDef command) {
        return true;
    }

    public boolean canInsertReferencedObjectIntoTree(final Id propertyId) {
        if (getProperty(propertyId) instanceof PropertyReference) {
            final PropertyReference property = (PropertyReference) getProperty(propertyId);
            return findNearestExplorerItemView() != null && property.canOpenEntityModel();
        }
        return false;
    }

    /**
     * Returns command with specified identifier.<p> If requested entity command
     * is not exists in this model method {@link #createCommand(org.radixware.kernel.explorer.meta.RadCommandDef)
     * } called to create it. Command presentation attributes requested from
     * {@link DefManager#getContextlessCommandDef(org.radixware.kernel.common.types.Id)}
     * for {@link RadCommandDef contextless commands} and from
     * {@link IModelDefinition#getCommandDefById(org.radixware.kernel.common.types.Id)}
     * for {@link RadPresentationCommandDef entity commands}.
     * <p><b>NOTE:</b> This method does not checks if requested command is
     * enabled for model. Any contextless command might be load throw this
     * method.
     *
     * @param id command identifier
     * @return command model item.
     * @see #getEnabledCommands()
     */
    public Command getCommand(Id id) {
        if (commands == null) {
            commands = new HashMap<>();
        }
        Command c = commands.get(id);
        if (c == null) {
            final RadCommandDef cmdDef;
            if (id.getPrefix() == EDefinitionIdPrefix.CONTEXTLESS_COMMAND) {
                cmdDef = getEnvironment().getDefManager().getContextlessCommandDef(id);
            } else {
                cmdDef = def.getCommandDefById(id);
            }
            c = createCommand(cmdDef);
        }
        if (c==null){
            final String messageTemplate = 
                getEnvironment().getMessageProvider().translate("TraceMessage", "Command instance #%1$s was not found");
            getEnvironment().getTracer().warning(String.format(messageTemplate, id.toString()));
            return null;
        }
        commands.put(id, c);
        return c;
    }

    /**
     * set list of server-side restricted commands
     *
     * @param disabledCommands list of disabled commands
     */
    protected final void setServerDisabledCommands(List<Id> disabledCommands) {
        if (!disabledCommands.isEmpty()) {
            final List<Id> commandIds = getAccessibleCommandIds();
            boolean isEnabled;
            for (Id commandId : commandIds) {
                isEnabled = !disabledCommands.contains(commandId);
                getCommand(commandId).setServerEnabled(isEnabled);
            }
        }
    }

    public CommandRequestHandle sendCommandAsync(final Id commandId,
            final Id propertyId,
            final XmlObject input,
            final Class<? extends XmlObject> expectedOutputClass,
            final java.lang.Object userData) {
        return sendCommandAsync(commandId, propertyId, input, expectedOutputClass, userData, 0);
    }
    
    public CommandRequestHandle sendCommandAsync(final Id commandId,
            final Id propertyId,
            final XmlObject input,
            final Class<? extends XmlObject> expectedOutputClass,
            final java.lang.Object userData,
            final int timeoutSec) {
        CommandRequestHandle handle = RequestHandle.Factory.createForSendContextCommand(getEnvironment(), commandId, propertyId, this, input, expectedOutputClass);
        handle.addListener(this);
        handle.setUserData(userData);

        getEnvironment().getEasSession().sendAsync(handle, timeoutSec);
        return handle;
    }    

    //Child models
    /**
     * Метод возвращает модель, созданную на основе дочернего для дефиниции
     * текущей модели {@link ExplorerItemDef элемента проводника}. В контексте
     * дочерней модели метод {@link Context.Abstract#getHolderModel()} будет
     * возвращать данную модель.<p> Если по заданному идентификатору дочерний
     * элемент проводника найти не удается - генерируется ошибка
     * ModelCreationError. Если для дочернего элемента проводника модель не
     * существует - метод возвращает null.
     *
     * @param explItemId идентификатор дочернего элемента проводника.
     * @return Дочерняя модель.
     * @throws ServiceClientException ошибки выполнения запросов во время
     * создания модели
     * @throws InterruptedException
     */
    public Model getChildModel(Id explItemId) throws ServiceClientException, InterruptedException {
        if (!childModelsByExplorerItemId.containsKey(explItemId) || childModelsByExplorerItemId.get(explItemId).getClass().getClassLoader() != getEnvironment().getDefManager().getClassLoader()) {
            final RadExplorerItemDef explItemDef = getChildExplorerItemDef(explItemId);
            if (explItemDef != null && explItemDef.isValid()) {
                final Model result;
                final RadExplorerItemDef actualExplItemDef;
                if (explItemDef instanceof RadSelectorUserExplorerItemDef){
                    actualExplItemDef = ((RadSelectorUserExplorerItemDef)explItemDef).getTargetExplorerItem();
                }else{
                    actualExplItemDef = explItemDef;
                }
                if (actualExplItemDef instanceof RadChildRefExplorerItemDef) {
                    final Model holder = this;
                    final EntityModel parentEntity = this instanceof EntityModel ? (EntityModel) this : context.getHolderEntityModel();
                    if (parentEntity == null) {
                        throw new ModelCreationError(ModelCreationError.ModelType.GROUP_MODEL, explItemDef.getModelDefinition(), null, "Parent entity is not found");
                    }
                    if (!parentEntity.isExists()) {
                        if (parentEntity.isNew()) {
                            throw new ModelCreationError(ModelCreationError.ModelType.GROUP_MODEL, explItemDef.getModelDefinition(), null, "Parent object is not exists");
                        } else {
                            throw new ObjectNotFoundError(parentEntity);
                        }
                    }
                    if (explItemDef instanceof RadChildRefExplorerItemDef){
                        result = 
                            GroupModel.openChildTableSelectorModel(getEnvironment(), parentEntity, holder, (RadChildRefExplorerItemDef)explItemDef);
                    }else{
                        result = 
                            GroupModel.openChildTableSelectorModel(getEnvironment(), parentEntity, holder, (RadSelectorUserExplorerItemDef)explItemDef);
                    }
                } else if (explItemDef instanceof RadParentRefExplorerItemDef) {
                    final EntityModel childEntity = this instanceof EntityModel ? (EntityModel) this : context.getHolderEntityModel();
                    if (!childEntity.isExists()) {
                        if (childEntity.isNew()) {
                            throw new ModelCreationError(ModelCreationError.ModelType.ENTITY_MODEL, explItemDef.getModelDefinition(), null, "Child object is not exists");
                        } else {
                            throw new ObjectNotFoundError(childEntity);
                        }
                    }
                    final RadParentRefExplorerItemDef parentExplorerItemDef = (RadParentRefExplorerItemDef) explItemDef;
                    result = childEntity.openParentEditModel(parentExplorerItemDef, this);
                    if (result == null) {
                        return result;
                    }
                    final IPresentationChangedHandler pch = new ChildModelPresentationChangedHandler(parentExplorerItemDef);
                    ((EntityModel)result).getEntityContext().setPresentationChangedHandler(pch);
                } else if (explItemDef instanceof RadSelectorExplorerItemDef) {
                    final EntityModel parentEntity = this instanceof EntityModel ? (EntityModel) this : context.getHolderEntityModel();
                    final RadSelectorExplorerItemDef tableExplorerItem = (RadSelectorExplorerItemDef) explItemDef;
                    result = GroupModel.openTableSelectorModel(getEnvironment(), parentEntity, this, tableExplorerItem);
                } else if (explItemDef instanceof RadParagraphDef) {
                    result = ParagraphModel.openModel(getEnvironment(), (RadParagraphDef) explItemDef.getModelDefinition(), this);
                } else if (explItemDef instanceof RadParagraphLinkDef) {
                    result = ParagraphModel.openModel(getEnvironment(), (RadParagraphLinkDef) explItemDef.getModelDefinition(), this);
                } else {
                    final String msg = getEnvironment().getMessageProvider().translate("ExplorerError", "Unknown type of explorer item: \"%s\"");
                    throw new ModelCreationError(ModelCreationError.ModelType.UNKNOWN, null, (TitledDefinition) def, null, String.format(msg, explItemDef.getClass().getName()));
                }
                result.parentModel = this;
                afterCreateChildModel(explItemId, result);
                childModelsByExplorerItemId.put(explItemId, result);
            } else {
                final String msg = getEnvironment().getMessageProvider().translate("ExplorerError", "Can't find child explorer item #%s");
                throw new ModelCreationError(ModelCreationError.ModelType.UNKNOWN, null, (TitledDefinition) def, null, String.format(msg, explItemId));
            }
        }
        return childModelsByExplorerItemId.get(explItemId);
    }

    protected final Model getParentModel() {
        return parentModel;
    }

    /**
     * Метод проверяет доступность дочернего элемента проводника с
     * идентификатором explorerItemId. Если дочеоний элемент проводника с
     * указанным идентификатором не найден метод возвращает false. Если дочеоний
     * элемент проводника является параграфом или ссылкой на параграф, то метод
     * возвращает true. Если данная модель является дочерней для какой-либо
     * EntityModel, то доступность элемента проводника определяется на основе
     * результатов запроса ListEdPresVisibleExpItems, иначе метод возвращает
     * true.
     *
     * @param explorerItemId identifier of explorer item to check for
     * @return true when specified explorer item is accessible and can be used
     * to open child model
     * @throws ServiceClientException ошибка выполнения запроса во время
     * получения доступных элементов проводника
     * @throws InterruptedException
     * @see #getChildModel(org.radixware.kernel.common.types.Id)
     */
    public boolean isExplorerItemAccessible(final Id explorerItemId) throws ServiceClientException, InterruptedException {
        final RadExplorerItemDef childExplorerItem = getChildExplorerItemDef(explorerItemId);
        if (childExplorerItem == null || !childExplorerItem.isValid()) {
            return false;
        }
        if (childExplorerItem instanceof RadSelectorUserExplorerItemDef) {
            return true;
        }
        final EntityModel ownerEntity = (EntityModel) findOwner(new IModelFinder() {
            @Override
            public boolean isTarget(Model model) {
                return model instanceof EntityModel;
            }
        });
        return ownerEntity != null ? ownerEntity.isExplorerItemAccessible(explorerItemId) : true;
    }

    /**
     * Returns child models opened in
     * {@link #getChildModel(org.radixware.kernel.common.types.Id)} method at
     * this moment.
     *
     * @return
     * @see #getChildModel(org.radixware.kernel.common.types.Id)
     */
    protected Collection<Model> getOpenedChildModels() {
        return childModelsByExplorerItemId.values();
    }

    /**
     * Returns definition of child explorer item for
     * {@link IModelDefinition model definitin}.
     *
     * @param explItemId explorer item identifier
     * @return child explorer item.
     */
    protected final RadExplorerItemDef getChildExplorerItemDef(final Id explItemId) {
        if (explItemId!=null){            
            if (def instanceof IExplorerItemsHolder) {            
                final RadExplorerItems childItems = ((IExplorerItemsHolder) def).getChildrenExplorerItems();
                if (explItemId.getPrefix()==EDefinitionIdPrefix.USER_EXPLORER_ITEM){
                    if (explorerItemView==null){
                        return null;
                    }else{
                        final UserExplorerItemsStorage storage = 
                            UserExplorerItemsStorage.getInstance(getEnvironment());
                        final IExplorerItemsHolder itemsHolder = explorerItemView.getTopLevelExplorerItemsHolder();
                        final Id userExplorerItemContextId = UserExplorerItemsStorage.getContextId(itemsHolder);
                        return storage.findUserExplorerItemById(userExplorerItemContextId, childItems, explItemId);
                    }                    
                }else{
                    return childItems.findExplorerItem(explItemId);
                }
            }
        }
        return null;
    }

    public void afterInsertChildItem(IExplorerItemView childItem) {
    }

    public void afterRemoveChildItem(IExplorerItemView childItem) {
    }

//Context
    /**
     * Retuns context for this model.
     *
     * @return model context
     * @see #setContext(org.radixware.kernel.explorer.models.Context.Abstract)
     */
    public final IContext.Abstract getContext() {
        return context;
    }

    /**
     * Set context to model
     *
     * @param context
     * @see #getContext()
     */
    public void setContext(IContext.Abstract context) {
        this.context = context;
    }

    /**
     * Gets name of group that may used to save and restore model settings in
     * {@link org.radixware.kernel.explorer.env.ExplorerSettings explorer settings}
     *
     * @return name of settings group for this model
     * @see Environment#getConfigStore()
     * @see
     * org.radixware.kernel.explorer.env.ExplorerSettings#beginGroup(java.lang.String)
     */
    public String getConfigStoreGroupName() {
        return getContext().getSettingsGroupPrefix() + def.getId().toString();
    }

    /**
     * Process exception and show it in exception box.
     * <p> If reason of exception was {@link PropertyIsMandatoryException} or
     * {@link InvalidPropertyValueException} this methods tryes to focus widget
     * of corresponding property in registered view.
     *
     * @param ex exception to process for
     */
    public void showException(Throwable ex) {
        showException(null, ex);
    }

    /**
     * Process exception and show it in exception box.
     * <p>* If reason of exception was {@link PropertyIsMandatoryException} or
     * {@link InvalidPropertyValueException} this methods tryes to focus widget
     * of corresponding property in registered view.
     * <p>If
     * <code>title</code> string is not null it will be displayed as a title of
     * exception box.
     *
     * @param title title of exception box
     * @param ex exception to process for
     */
    public void showException(final String title, final Throwable ex) {
        if (ClientException.isSystemFault(ex)) {
            getEnvironment().processException(ex);
            return;
        }

        Throwable toProcess = ex;
        if (ex instanceof ServiceCallFault) {
            final ModelException modelException = ModelException.create(this, (ServiceCallFault) ex);
            if (modelException != null) {
                toProcess = modelException;
            }
        }

        final MessageProvider mp = getEnvironment().getMessageProvider();
        if (toProcess instanceof PropertyIsMandatoryException) {
            final PropertyIsMandatoryException exception = (PropertyIsMandatoryException) toProcess;
            final String message;
            if (title == null || title.isEmpty()) {
                message = exception.getMessage(mp);
            } else {
                message = title + ".\n" + exception.getMessage(mp);
            }
            getEnvironment().messageError(exception.getTitle(mp), message);
            exception.goToProblem(this);
        } else if (toProcess instanceof InvalidPropertyValueException) {
            final InvalidPropertyValueException exception = (InvalidPropertyValueException) toProcess;
            final String message;
            if (title == null || title.isEmpty()) {
                message = exception.getMessage(mp);
            } else {
                message = title + ".\n" + exception.getMessage(mp);
            }
            getEnvironment().messageError(exception.getTitle(mp), message);
            exception.goToProblem(this);
        } else {
            getEnvironment().processException(title, ex);
        }
    }

    public IModelWidget createPropertyEditor(final Id propertyId) {
        return getProperty(propertyId).createPropertyEditor();
    }

    protected void beforeOpenView() {
    }

    protected void afterCreateChildModel(final Id explorerItemId, final Model childModel) {
    }

    public boolean canUsePropEditorDialog(final Id propertyId) {
        return true;
    }

    public void beforeOpenPropEditorDialog(final Property property, final PropEditorModel propEditorModel) {
    }

    public IDialog.DialogResult afterClosePropEditorDialog(final Property property, final PropEditorModel propEditorModel, final DialogResult dialogResult) {
        return dialogResult;
    }

    public boolean beforeSelectParent(final PropertyRef property, final GroupModel group) {
        return true;
    }

    public void onStartEditPropertyValue(final Property property) {
    }

    public void onFinishEditPropertyValue(final Property property, final boolean valueWasAccepted) {
    }

    public String getDisplayString(final Id propertyId, final Object propertyValue, final String defaultDisplayString, final boolean isInherited) {
        return defaultDisplayString;
    }

    Model getOwner() {
        if (getParentModel() != null) {
            return getParentModel();
        } else if (getContext().getOwnerModel() != null) {
            return getContext().getOwnerModel();
        } else if (findNearestExplorerItemView() != null) {
            return findNearestExplorerItemView().getParentModel();
        } else if (getView() != null && getView().hasUI()) {
            IView parentView = getView().findParentView();
            return parentView == null ? null : parentView.getModel();
        } else {
            return null;
        }
    }

    public final Model findOwner(IModelFinder filter) {
        Model owner = getOwner();
        while (owner != null && !filter.isTarget(owner)) {
            owner = owner.getOwner();
        }
        return owner;
    }

    public final Model findOwnerByClass(final Class targetClass) {
        return findOwner(new IModelFinder() {
            @Override
            public boolean isTarget(final Model model) {
                return targetClass.isInstance(model);
            }
        });
    }

    public final List<Model> visitChildren(ModelVisitor visitor) {
        for (Model childModel : getOpenedChildModels()) {
            visitor.visit(childModel);
            if (visitor.wasCancelled()) {
                break;
            }
            childModel.visitChildren(visitor);
        }
        return visitor.getSelectedModels();
    }

    public final List<Model> visitOwners(ModelVisitor visitor) {
        Model owner = getOwner();
        do {
            visitor.visit(owner);
            owner = owner.getOwner();
        } while (owner != null && !visitor.wasCancelled());
        return visitor.getSelectedModels();
    }

    @Override
    protected void onCommandResponse(Id commandId, XmlObject output, Object userData, CommandRequestHandle handler) {
        try {
            if ((handler.getResponse() instanceof org.radixware.schemas.eas.CommandRs)
                    && def.isCommandDefExistsById(commandId)//Self command
                    ) {
                //Set property values from command response
                final org.radixware.schemas.eas.CommandRs response = (org.radixware.schemas.eas.CommandRs) handler.getResponse();
                final org.radixware.schemas.eas.PropertyList propertyList;
                if (response.getCurrentData() != null) {
                    propertyList = response.getCurrentData().getProperties();
                } else {
                    propertyList = null;
                }

                if (propertyList != null) {
                    List<org.radixware.schemas.eas.PropertyList.Item> itemList = propertyList.getItemList();
                    if (itemList != null) {
                        Property property;
                        for (org.radixware.schemas.eas.PropertyList.Item item : itemList) {
                            property = getProperty(item.getId());
                            if (!property.isValEdited()) {
                                if (property.getDefinition().getType() == EValType.XML) {
                                    final PropertyXml xmlProp = (PropertyXml) property;
                                    final XmlObject value = xmlProp.castValue(ValueConverter.easPropXmlVal2ObjVal(item, EValType.XML, null));
                                    property.setValueObject(value);
                                } else {
                                    final Id valTableId;
                                    if (property.getDefinition() instanceof RadParentRefPropertyDef) {
                                        valTableId = ((RadParentRefPropertyDef) property.getDefinition()).getReferencedTableId();
                                    } else {
                                        valTableId = null;
                                    }
                                    property.setValueObject(ValueConverter.easPropXmlVal2ObjVal(item, property.getDefinition().getType(), valTableId, getEnvironment().getDefManager()));
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception exception) {
            final String message = "Exception on processing response for command %s: %s\n%s";
            final String exTitle = ClientException.getExceptionReason(getEnvironment().getMessageProvider(), exception);
            final String stack = ClientException.exceptionStackToString(exception);
            getEnvironment().getTracer().error(String.format(message, commandId.toString(), exTitle, stack));
        }
    }

    public final void addTitleListener(final ITitleListener listener) {
        if (listener != null && (titleListeners == null || !titleListeners.contains(listener))) {
            if (titleListeners == null) {
                titleListeners = new LinkedList<>();
            }
            titleListeners.add(listener);
        }
    }

    public final void removeTitleListener(final ITitleListener listener) {
        if (titleListeners != null) {
            titleListeners.remove(listener);
        }
    }

    private void changeTitleNotify(final String oldTitle, final String newTitle) {
        if (titleListeners != null && !titleListeners.isEmpty()) {
            final List<ITitleListener> listeners = new LinkedList<>(titleListeners);
            for (ITitleListener listener : listeners) {
                listener.titleChanged(oldTitle, newTitle);
            }
        }
    }
}