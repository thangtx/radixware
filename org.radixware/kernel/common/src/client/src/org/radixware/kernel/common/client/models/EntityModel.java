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

import java.util.*;


import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.dialogs.IMessageBox;
import org.radixware.kernel.common.client.eas.RequestHandle;
import org.radixware.kernel.common.client.enums.EEntityCreationResult;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.errors.ModelError;
import org.radixware.kernel.common.client.errors.NoDefinitionWithSuchIdError;
import org.radixware.kernel.common.client.errors.ObjectNotFoundError;
import org.radixware.kernel.common.client.errors.UniqueConstraintViolationError;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.exceptions.InvalidPropertyValueException;
import org.radixware.kernel.common.client.exceptions.ModelException;
import org.radixware.kernel.common.client.exceptions.PropertyIsMandatoryException;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.RadClassPresentationDef;
import org.radixware.kernel.common.client.meta.RadCommandDef;
import org.radixware.kernel.common.client.meta.RadEditorPresentationDef;
import org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef;
import org.radixware.kernel.common.client.meta.explorerItems.RadParentRefExplorerItemDef;
import org.radixware.kernel.common.client.meta.RadPropertyDef;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.models.items.Command;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.models.items.properties.PropertyObject;
import org.radixware.kernel.common.client.models.items.properties.PropertyRef;
import org.radixware.kernel.common.client.models.items.properties.PropertyValue;
import org.radixware.kernel.common.client.tree.IExplorerTreeManager;
import org.radixware.kernel.common.client.types.EntityRestrictions;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.utils.ClientValueFormatter;
import org.radixware.kernel.common.client.views.IEditor;
import org.radixware.kernel.common.client.views.IExplorerItemView;
import org.radixware.kernel.common.client.views.ISelector;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.enums.*;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.exceptions.IllegalArgumentError;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;

import org.radixware.schemas.eas.CreateRs;
import org.radixware.schemas.eas.ExceptionEnum;
import org.radixware.schemas.eas.ListEdPresVisibleExpItemsRs;
import org.radixware.schemas.eas.PrepareCreateRs;
import org.radixware.schemas.eas.PropertyList;
import org.radixware.schemas.eas.ReadMess;
import org.radixware.schemas.eas.ReadRs;
import org.radixware.schemas.eas.UpdateRs;


public abstract class EntityModel extends ModelWithPages {

    private static class PresentationChangedHandler implements IPresentationChangedHandler {

        private final EntityModel selectorRow;
        private EntityModel actualEntityModel;

        public PresentationChangedHandler(final EntityModel selectorRow) {
            this.selectorRow = selectorRow;
        }

        @Override
        public EntityModel onChangePresentation(RawEntityModelData rawData, Id newPresentationClassId, Id newPresentationId) {
            final EntityModel newSelectorRow =
                    selectorRow.getOwnerSelectorModel().onChangePresentation(rawData, newPresentationClassId, newPresentationId);
            actualEntityModel = newSelectorRow.openInSelectorEditModel();
            return actualEntityModel;            
        }

        public EntityModel getActualEntityModel() {
            return actualEntityModel;
        }
    }    
        
    private static class InteractivePropertiesFilter implements IPropertiesFilter{
        
        private final IContext.Entity context;
        private final RadEditorPresentationDef presentation;
        
        public InteractivePropertiesFilter(final IContext.Entity context, final RadEditorPresentationDef pres){            
            this.context = context;
            this.presentation = pres;
        }

        @Override
        public boolean isFiltered(final Property property) {
            if (context instanceof IContext.SelectorRow){
                final GroupModel group = ((IContext.SelectorRow) context).parentGroupModel;
                final RadSelectorPresentationDef.SelectorColumns columns = group.getSelectorPresentationDef().getSelectorColumns();
                {
                    for (RadSelectorPresentationDef.SelectorColumn column : columns) {
                        if (column.getVisibility()!=ESelectorColumnVisibility.NEVER
                            && column.getPropertyId().equals(property.getId())){
                            return false;
                        }
                    }
                }
            }
            if (property.hasSubscriber()){
                return false;
            }
            return !presentation.getEditorPages().isPropertyDefined(property.getId());
        }                
    }
        
    private EntityModel copySource = null;    
    private boolean propertyValuesWasSetFromCopySource;
    private EntityModel synchronizedModel = null;
    private List<Id> commandIds = null;
    private Pid pid = null;
    private Pid srcPid = null;
    private Id classId = null;
    private EntityRestrictions restrictions = null;
    private boolean isNew = false;
    private boolean isExists = true;
    private boolean isEdited = false;
    private boolean wasRead = false;//Флажок, что был запрос на чтение
    private boolean wasActivated = false;//Флажок, что модель активирована и готова к использованию.
    private boolean activatePropsRecursionBlock = false;
    private ESelectorRowStyle selectorStyle = ESelectorRowStyle.NORMAL;
    private Map<Id,RawEntityModelData.ChildExplorerItemInfo> accessibleExplItemsById;
    private long lastReadTime = 0;

    protected EntityModel(IClientEnvironment environment, RadEditorPresentationDef def) {
        super(environment, def);
    }

    public RadEditorPresentationDef getEditorPresentationDef() {
        return (RadEditorPresentationDef) getDefinition();
    }

    public RadClassPresentationDef getClassPresentationDef() {
        if (classId != null) {
            return getEnvironment().getDefManager().getClassPresentationDef(classId);
        } else {
            return getEditorPresentationDef().getClassPresentation();
        }
    }

    public IEditor getEntityView() {
        return getView() != null ? (IEditor) getView() : null;
    }

    @Override
    public void setView(final IView view_) {
        final boolean changingRestrictions;
        if (view_==null && getView()!=null){
            changingRestrictions = getView().getRestrictions().greaterThan(getRestrictions());
        }else if (view_!=null){
            changingRestrictions = view_.getRestrictions().greaterThan(getRestrictions());
        }else{
            changingRestrictions = false;
        }
        super.setView(view_);
        if (changingRestrictions)
            onChangeRestriction();
    }
    
    public boolean canOpenEntityView() {
        return !getRestrictions().getIsViewRestricted() &&
                (getEditorPresentationDef().getRuntimeEnvironmentType() == ERuntimeEnvironmentType.COMMON_CLIENT
                 || getEditorPresentationDef().getRuntimeEnvironmentType() == getEnvironment().getApplication().getRuntimeEnvironmentType());
    }

    public final IContext.Entity getEntityContext() {
        return (IContext.Entity) getContext();
    }
        
    public final boolean isInSelectorRowContext(){
        return getContext() instanceof IContext.SelectorRow;
    }
    

//Overrides
    @Override
    public void clean() {
        stopSynchronization();
        if (restrictions != null) {
            restrictions.clear();
        }
        super.clean();
        accessibleExplItemsById = null;
        wasRead = false;
    }
    
    @Override
    public boolean canSafelyClean(final CleanModelController controller){
        if (isInSelectorRowContext() && synchronizedModel != null) {
            return synchronizedModel.canSafelyClean(controller) && super.canSafelyClean(controller);
        }
        if (getView() == null && controller.needToCheckUnsavedChanges(this) && isExists() && isEdited() && !askForUpdate()) {
            return false;
        } else if (!isExists() && isEdited()) {
            setIsEdited(false);
        }
        
        if (!super.canSafelyClean(controller)){
            return false;
        }
        
        if (controller.needToCheckMandatoryProperties(this) 
            && isExists()
            && getEnvironment().getConfigStore().readBoolean(CHECK_MANDATORY_CONFIG_SETTING, true)) {
            final List<Property> undefinedProperties = findUndefinedMandatoryProperties();
            if (!undefinedProperties.isEmpty()){
                final MessageProvider mp = getEnvironment().getMessageProvider();
                final String title = mp.translate("Editor", "Confirm to Close");
                final String message;
                if (undefinedProperties.size()==1){
                    message = mp.translate("Editor", "Do you really want to skip it and close the editor?");
                } else {
                    message = mp.translate("Editor","Do you really want to skip them and close the editor?");
                }
                if (!warnAboutMandatoryProperties(title, message, undefinedProperties, true)){
                    undefinedProperties.get(0).setFocused();
                    return false;                  
                }
            }
        }
        return true;        
    }
    
    public final EDialogButtonType showUpdateMessageConfirmation(){
        final GroupModel ownerSelectorModel = getOwnerSelectorModel();
        final String showConfirmSettingPath;
        if (ownerSelectorModel!=null){
            final StringBuilder  showConfirmSettingPathBuilder = new StringBuilder();
            showConfirmSettingPathBuilder.append(ownerSelectorModel.getConfigStoreGroupName());
            showConfirmSettingPathBuilder.append('/');
            showConfirmSettingPathBuilder.append(SettingNames.SYSTEM);
            showConfirmSettingPathBuilder.append("/show_confirm_on_update");
            showConfirmSettingPath = showConfirmSettingPathBuilder.toString();
            final boolean showConfirm = getEnvironment().getConfigStore().readBoolean(showConfirmSettingPath, true);
            if (!showConfirm){
                return EDialogButtonType.YES;//auto apply
            }
        }else{
            showConfirmSettingPath = null;
        }
        final MessageProvider mp = getEnvironment().getMessageProvider();
        final String title = mp.translate("ExplorerDialog", "Save Changes?");
        final String messageTemplate = mp.translate("ExplorerDialog", "Apply changes in editor of \'%s\'?");
        final String message = String.format(messageTemplate, getTitle());
        final Set<EDialogButtonType> buttons = EnumSet.of(EDialogButtonType.YES, EDialogButtonType.NO, EDialogButtonType.CANCEL);
        final IMessageBox messageBox = getEnvironment().newMessageBoxDialog(message, title, EDialogIconType.QUESTION, buttons);
        if (showConfirmSettingPath!=null){
            final String optionText = mp.translate("ExplorerDialog", "Apply changes without confirmation next time");
            messageBox.setOptionText(optionText);
        }
        final EDialogButtonType result = messageBox.execMessageBox();
        if (showConfirmSettingPath!=null){
            final boolean showConfirm = !messageBox.isOptionActivated();
            if (showConfirm){
                getEnvironment().getConfigStore().remove(showConfirmSettingPath);
            }else{
                getEnvironment().getConfigStore().writeBoolean(showConfirmSettingPath, false);
            }
        }
        return result;
    }

    protected boolean askForUpdate() {
        if (getEnvironment().getDefManager().getAdsVersion().isSupported()) {
            final EDialogButtonType answer = showUpdateMessageConfirmation();
            if (answer.equals(EDialogButtonType.YES)) {
                try {
                    final boolean isUpdateSuccessfull;
                    if (getEntityView() != null) {
                        getEntityView().applyChanges();
                        isUpdateSuccessfull = !isEdited();
                    } else {
                        isUpdateSuccessfull = update();
                    }
                    if (!isUpdateSuccessfull) {
                        return false;
                    }
                } catch (ObjectNotFoundError e) {
                    showException(getEnvironment().getMessageProvider().translate("Editor", "Failed to apply changes"), e);
                    return e.inContextOf(this);
                } catch (ServiceClientException e) {
                    showException(getEnvironment().getMessageProvider().translate("Editor", "Failed to apply changes"), e);
                    return false;
                } catch (ModelException e) {
                    showException(getEnvironment().getMessageProvider().translate("Editor", "Failed to apply changes"), e);
                    return false;
                } catch (InterruptedException ex) {
                    return false;
                }
            } else if (answer.equals(EDialogButtonType.CANCEL)) {
                return false;
            } else //Если не сделать будут проблемы при повторной синхронизации
            {
                if (getEntityView() == null) {
                    cancelChanges();
                } else {
                    getEntityView().cancelChanges();
                }
            }
        } else {
            //Текущая версия больше не поддерживается - сохранить изменения невозможно
            final String title = getEnvironment().getMessageProvider().translate("ExplorerDialog", "Close Editor?");
            final String message = getEnvironment().getMessageProvider().translate("ExplorerDialog", "Close editor of \'%s\' without saving changes?");
            if (getEnvironment().messageConfirmation(title, String.format(message, getTitle()))) {
                if (getEntityView() == null) {
                    cancelChanges();
                } else {
                    getEntityView().cancelChanges();
                }
            } else {
                return false;
            }
        }
        return true;
    }

    protected final List<Property> findUndefinedMandatoryProperties() {
        final List<Property> result = new LinkedList<>();
        if (properties == null || properties.isEmpty()
                || !getEnvironment().getDefManager().getAdsVersion().isSupported()) {
            return result;
        }        
        final List<Property> props = getActivePropertiesByOrder(new InteractivePropertiesFilter(getEntityContext(), getEditorPresentationDef()));
        boolean registeredInEditorPage;
        for (Property property : props) {
            //поиск мандаторных свойств
            if (!property.hasValidMandatoryValue()
                    && property.isVisible()
                    && !property.isReadonly()) {
                result.add(property);
            }
        }
        return result;
    }
    
    @Override
    protected List<Property> getActivePropertiesByOrder(final IPropertiesFilter filter) {
        if (isInSelectorRowContext()) {
            final GroupModel group = ((IContext.SelectorRow) getContext()).parentGroupModel;
            final RadSelectorPresentationDef.SelectorColumns columns = group.getSelectorPresentationDef().getSelectorColumns();
            final List<Property> result = new LinkedList<>();
            {
                Property property;
                for (RadSelectorPresentationDef.SelectorColumn column : columns) {
                    property = getProperty(column.getPropertyId());
                    if (!filter.isFiltered(property) && property.isActivated()) {
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
            return result;
        } else {
            return super.getActivePropertiesByOrder(filter);
        }
    }        

//	общие атрибуты
    public boolean isEdited() {
        return isEdited || isNew;
    }

    public void setIsEdited(boolean edited) {
        if (edited == isEdited) {
            return;
        }
        isEdited = edited;
        if (synchronizedModel != null) {
            synchronizedModel.setIsEdited(edited);
        }
        if (getEntityView() != null) {
            getEntityView().getActions().refresh();
        } else if (isInSelectorRowContext()) {
            final IContext.SelectorRow c = (IContext.SelectorRow) getContext();
            final ISelector selector = c.parentGroupModel.getGroupView();
            if (selector != null) {
                selector.getActions().refresh();
            }
        }
    }

    private boolean isAnyPropertyEdited() {
        Collection<Property> props = getActiveProperties();
        for (Property property : props) {
            if (property.isValEdited() && !property.isLocal()) {
                return true;
            }
        }
        return false;
    }

    public Pid getPid() {
        return pid;
    }

    public boolean isNew() {
        return isNew;
    }

    public boolean isExists() {
        return isExists;
    }
    
    protected final void setExists(final boolean flag){
        isExists = flag;
        if (synchronizedModel!=null){
            synchronizedModel.isExists = flag;
        }
    }

    public Id getClassId() {
        return classId;
    }

    public boolean isCopyOf(Pid src) {
        if (!isNew()) {
            throw new IllegalStateException("Object is not new");
        }
        return (copySource != null && copySource.getPid().equals(src))
                  || (srcPid!=null && srcPid.equals(src));
    }

    public final boolean isAuditEnabled() {
        return getClassPresentationDef().isAuditEnabled()
                && getEnvironment().canViewAuditTable();
    }

    public Pid getSrcPid() {
        if (!isNew()) {
            throw new IllegalStateException("Object is not new");
        } else if (copySource != null) {
            return copySource.getPid();
        } else if (srcPid != null){
            return srcPid;
        }
        return null;
    }

    @Override
    public String getTitle() {
        //RADIX-2584
        return isNew() ? String.format(getEnvironment().getMessageProvider().translate("ExplorerMessage", "New %s"), getDefinition().getTitle())
                : super.getTitle();
    }

    @Override
    public String getWindowTitle() {
        final String windowTitle,
                entityTitle = isNew() ? "" : ClientValueFormatter.capitalizeIfNecessary(getEnvironment(), getTitle()),
                definitionTitle = ClientValueFormatter.capitalizeIfNecessary(getEnvironment(), getDefinition().getTitle());
        if (isNew()) {
            String classTitle;
            try {
                final RadClassPresentationDef classDef = getClassPresentationDef();
                classTitle = ClientValueFormatter.capitalizeIfNecessary(getEnvironment(), classDef.getClassTitle());
            } catch (DefinitionError err) {
                classTitle = "";
            }
            windowTitle = getEnvironment().getMessageProvider().translate("EntityEditorDialog", "Creating. %s");
            return String.format(windowTitle, classTitle.isEmpty() ? definitionTitle : classTitle);
        } else {
            if (getRestrictions().getIsUpdateRestricted()) {
                windowTitle = getEnvironment().getMessageProvider().translate("EntityEditorDialog", "Viewing. %s \'%s\'");
            } else {
                windowTitle = getEnvironment().getMessageProvider().translate("EntityEditorDialog", "Editing. %s \'%s\'");
            }
            return String.format(windowTitle, definitionTitle, entityTitle);
        }
    }

    public EntityRestrictions getRestrictions() {
        return restrictions;
    }

    public ESelectorRowStyle getSelectorRowStyle() {
        return selectorStyle;
    }

    public void setSelectorRowStyle(ESelectorRowStyle newStyle) {
        if (newStyle == null) {
            throw new NullPointerException("selector row style must be not null");
        }
        if (selectorStyle != newStyle) {
            selectorStyle = newStyle;
            if (synchronizedModel != null) {
                synchronizedModel.setSelectorRowStyle(newStyle);
            }
            if (isInSelectorRowContext()) {
                final Collection<Property> props = getActiveProperties();
                for (Property property : props) {
                    property.afterModify();
                }
            }
        }
    }

    @Override
    public void setContext(final IContext.Abstract context) {
        super.setContext(context);
        if (context instanceof IContext.SelectorRow) {
            parentModel = ((IContext.SelectorRow) context).parentGroupModel;
        }
        restrictions = new EntityRestrictions(this);

        if (synchronizedModel != null)//RADIX-1462
        {
            restrictions.startSynchronization(synchronizedModel.restrictions);
        }
    }

    @Override
    public Command getCommand(final Id id) {
        final Command command = super.getCommand(id);
        if (synchronizedModel != null
                && synchronizedModel.commands != null
                && synchronizedModel.commands.containsKey(id)
                && !synchronizedModel.commands.get(id).isSynchronized()) {
            if (isInSelectorRowContext()){
                synchronizedModel.commands.get(id).startSynchronization(command);
            }else{
                command.startSynchronization(synchronizedModel.commands.get(id));
            }            
        }
        return command;
    }

    public List<Property> getEditedProperties() {
        return getEditedPropertiesImpl(false);
    }
    
    private List<Property> getEditedPropertiesImpl(final boolean excludeChangedByParentRef) {
        if (properties != null) {
            final List<Property> result = new ArrayList<>(properties.size());
            final Collection<Property> props = new LinkedList<>(properties.values());
            for (Property property : props) {
                if (property.isValEdited() && !(excludeChangedByParentRef && property.isValueModifiedByChangingParentRef())) {
                    result.add(property);
                }
            }
            if (properties.size()>props.size()){
                return getEditedPropertiesImpl(excludeChangedByParentRef);
            }else{
                return Collections.unmodifiableList(result);
            }
        }
        return Collections.<Property>emptyList();        
    }

    private List<PropertyValue> readProperties(final PropertyList propList, final RadClassPresentationDef classDef, final boolean WithLOBValues) {
        final List<PropertyValue> result;
        if (propList != null && !propList.getItemList().isEmpty()) {
            result = new ArrayList<>();
            RadPropertyDef propertyDef;
            PropertyValue propertyValue;
            for (PropertyList.Item p : propList.getItemList()) {
                try {
                    propertyDef = classDef.getPropertyDefById(p.getId());
                    if (!propertyDef.isReadSeparately() || WithLOBValues || isNew()) {//RADIX-4836
                        propertyValue = new PropertyValue(p, propertyDef, classDef.getId(), this);
                    }else{
                        propertyValue = null;
                    }
                } catch (DefinitionError error) {
                    final String msg = getEnvironment().getMessageProvider().translate("TraceMessage", "Cannot read property #%s: %s\n%s");
                    final String reason = ClientException.getExceptionReason(getEnvironment().getMessageProvider(), error);
                    final String stack = ClientException.exceptionStackToString(error);
                    getEnvironment().getTracer().debug(String.format(msg, p.getId().toString(), reason, stack));
                    continue;
                }
                if (propertyValue!=null){
                    result.add(propertyValue);
                }
            }
        } else {
            result = Collections.emptyList();
        }
        lastReadTime = Calendar.getInstance().getTime().getTime();
        return Collections.unmodifiableList(result);
    }

    public boolean wasRead() {
        return wasRead;
    }
    
    public void read() throws ServiceClientException, InterruptedException {
        removeProcessedHandles(ReadMess.class);
        revertLocalProperties();
        
        final RadEditorPresentationDef pres = getEditorPresentationDef();
        final ArrayList<Id> presentations = new ArrayList<>(1);
        presentations.add(pres.getId());

        final ReadRs rs;
        try {
            rs = getEnvironment().getEasSession().read(getPid(), classId, presentations, (IContext.Entity) getContext(), accessibleExplItemsById==null);
        } catch (ObjectNotFoundError error) {
            processObjectNotFound(error);
            throw error;
        }

        setIsEdited(false);
        final RawEntityModelData rawData = parseReadResponse(rs);
        if (rawData==null){
            return;
        }
        
        activate(rawData);
    }

    public void activateAllProperties() throws ServiceClientException, InterruptedException {
        removeProcessedHandles(ReadMess.class);
        revertLocalProperties();

        final RadEditorPresentationDef pres = getEditorPresentationDef();
        final ArrayList<Id> presentations = new ArrayList<>(1);
        presentations.add(pres.getId());

        final ReadRs rs;
        try {
            rs = getEnvironment().getEasSession().read(getPid(), classId, presentations, (IContext.Entity) getContext(), accessibleExplItemsById==null);
        } catch (ObjectNotFoundError error) {
            processObjectNotFound(error);
            throw error;
        }
        
        final RawEntityModelData rawData = parseReadResponse(rs);
        if (rawData==null){
            return;
        }

        final org.radixware.schemas.eas.Object data = rawData.getEntityObjectData();
        final List<PropertyValue> vals = new ArrayList<>();
        final RadClassPresentationDef classDef = getEnvironment().getDefManager().getClassPresentationDef(data.getClassId());
        vals.addAll(readProperties(data.getProperties(), classDef, false));

        Id propertyId;

        if (properties != null) {
            for (int i = vals.size() - 1; i >= 0; i--) {//except of already activated properties
                propertyId = vals.get(i).getPropertyDef().getId();
                if (properties.containsKey(propertyId) && properties.get(propertyId).hasServerValue()) {
                    vals.remove(i);
                }
            }
        }

        activate(data.getPID(), data.getTitle(), data.getClassId(), vals);
        updateServerRestrictions(rawData.getRestrictions());
        if (rawData.getEnabledEditorPages()!=null){
            updateServerEditorPageRestrictions(rawData.getEnabledEditorPages());
            if (synchronizedModel!=null){
                synchronizedModel.updateServerEditorPageRestrictions(rawData.getEnabledEditorPages());
            }
        }
        wasRead = true;
        updateCommands();
    }
    
    private void revertLocalProperties(){
        for (Property property: getLocalProperties()){
            property.cancelChanges();
        }
    }

    //чтение отдельного свойства
    public void readProperty(Id propId) throws ServiceClientException, InterruptedException {
        removeProcessedHandles(ReadMess.class);

        final ArrayList<Id> presentations = new ArrayList<>(1);
        presentations.add(getDefinition().getId());

        ReadRs rs;
        try {
            rs = getEnvironment().getEasSession().readProp(getPid(), classId, presentations, (IContext.Entity) getContext(), propId);
        } catch (ObjectNotFoundError error) {
            processObjectNotFound(error);
            throw error;
        }
        
        if (parseReadResponse(rs)==null){
            setIsEdited(false);
            return;            
        }

        final List<PropertyValue> vals = readProperties(rs.getData().getProperties(), getClassPresentationDef(), true);
        //cant call activate here cause it removes existing separately reading properties
        setTitle(rs.getData().getTitle());
        activateProps(vals);
    }

    public RequestHandle readAsync(final Collection<Id> propertyIds) {
        return readAsync(propertyIds, 0);
    }
    
    public RequestHandle readAsync(final Collection<Id> propertyIds, final int timeoutSec) {
        final RequestHandle handle = RequestHandle.Factory.createForReadEntityProperties(getEnvironment(), getPid(),
                classId,
                Collections.singletonList(getDefinition().getId()),
                propertyIds,
                (IContext.Entity) getContext());
        handle.addListener(this);
        getEnvironment().getEasSession().sendAsync(handle, timeoutSec);
        return handle;
    }    

    @Override
    public void onResponseReceived(XmlObject response, RequestHandle handle) {
        if (!(response instanceof ReadRs)) {
            super.onResponseReceived(response, handle);
            return;
        }
        final ReadRs rs = (ReadRs) response;
        if (Utils.equals(rs.getData().getPID(), (getPid() == null ? null : getPid().toString()))) {
            
            final RawEntityModelData rawData = parseReadResponse(rs);
            
            if (rawData==null) {
                return;
            }

            final org.radixware.schemas.eas.Object data = rawData.getEntityObjectData();
            final List<PropertyValue> vals = new ArrayList<>();
            final RadClassPresentationDef classDef =
                    getEnvironment().getDefManager().getClassPresentationDef(data.getClassId());
            vals.addAll(readProperties(data.getProperties(), classDef, false));
            onReadResponse(vals, 
                           rawData,
                           handle);
        }
    }

    protected void onReadResponse(final List<PropertyValue> propVals,            
            final RawEntityModelData data,
            final RequestHandle handle) {
        Id propertyId;
        if (properties != null) {
            for (int i = propVals.size() - 1; i >= 0; i--) {//except of local modified properties
                propertyId = propVals.get(i).getPropertyDef().getId();
                if (properties.containsKey(propertyId) && properties.get(propertyId).isValEdited()) {
                    propVals.remove(i);
                }
            }
        }

        activate(getPid().toString(), data.getEntityObjectData().getTitle(), classId, propVals);
        updateServerRestrictions(data.getRestrictions());
        final RawEntityModelData.EnabledEditorPages editorPages = data.getEnabledEditorPages();
        if (editorPages!=null){
            updateServerEditorPageRestrictions(editorPages);
            if (synchronizedModel!=null){
                synchronizedModel.updateServerEditorPageRestrictions(editorPages);
            }            
        }
        wasRead = true;
        updateCommands();
    }

    @Override
    public void onServiceClientException(final ServiceClientException exception, final RequestHandle handle) {
        super.onServiceClientException(exception, handle);
        if ((exception instanceof ServiceCallFault)
                && (handle.getRequest() instanceof org.radixware.schemas.eas.ReadRq)) {
            final org.radixware.schemas.eas.ReadRq rq =
                    (org.radixware.schemas.eas.ReadRq) handle.getRequest();
            final ServiceCallFault fault = (ServiceCallFault) exception;
            if (fault.getFaultString().equals(ExceptionEnum.OBJECT_NOT_FOUND.toString())
                    && Utils.equals(rq.getPID(), getPid() == null ? null : getPid().toString())) {
                onObjectNotFoundFault(handle);
            }
        }
    }

    protected void onObjectNotFoundFault(final RequestHandle handle) {
    }

    public boolean update() throws ModelException, ServiceClientException, InterruptedException {

        if (isInSelectorRowContext() && synchronizedModel != null) {
            //Если update был вызван для строки селектора - передаем вызов в модель редактора
            //чтобы учесть изменения в дочерних моделях.
            return synchronizedModel.update();
        }

        if (!isExists()) {
            if (isNew()) {
                throw new ModelError(ModelError.ErrorType.CANT_UPDATE, this, getEnvironment().getMessageProvider().translate("ExplorerError", "Object does not exist"));
            } else {
                throw new ObjectNotFoundError(this);
            }
        }

        if (!beforeUpdate()) {
            return false;
        }

        checkPropertyValues(false);
        final List<Property> undefinedProperties = findUndefinedMandatoryProperties();
        if (undefinedProperties!=null){
            final MessageProvider mp = getEnvironment().getMessageProvider();
            final String title = mp.translate("Editor", "Confirm to Apply Changes");
            final String message = mp.translate("Editor","Do you really want to apply changes?");
            if (!warnAboutMandatoryProperties(title, message, undefinedProperties, false)){
                undefinedProperties.get(0).setFocused();
                return false;
            }
        }
        removeProcessedHandles(ReadMess.class);
        /*
         * for (Model childModel : getOpenedChildModels()) { //применяем
         * изменения в дочерних моделях if ((childModel instanceof EntityModel)
         * && childModel.getView() != null && ((EntityModel)
         * childModel).isExists() && ((EntityModel) childModel).isEdited()) {
         * ((EntityModel) childModel).update(); } }
         */

        if (isAnyPropertyEdited()) {
            final UpdateRs response;
            try {
                response = getEnvironment().getEasSession().update(this);
            } catch (ObjectNotFoundError error) {
                processObjectNotFound(error);
                throw error;
            }

            setIsEdited(false);
            final RawEntityModelData rawData = new RawEntityModelData(response);
            final EntityModel entity = 
                checkPresentation(rawData, response.getData().getPresentation());
            if (entity != null) {
                entity.afterUpdate();//?????
                return true;
            }
            activate(rawData);
        } else {
            setIsEdited(false);
        }
        updateLocalProperties();
        afterUpdate();
        return true;
    }
    
    private void updateLocalProperties(){
        for (Property property: getLocalProperties()){
            if (property.isValEdited()){
                property.setValEdited(false);
            }
        }
    }
    
    public final boolean validatePropertyValues(){
        try{
            checkPropertyValues();
            return true;
        }catch(PropertyIsMandatoryException | InvalidPropertyValueException exception){
            showException(exception);
            return false;
        }
    }

    //фиксация вновь созданного объекта. Вызывается по окончании редактирования инициализированного объекта
    public EEntityCreationResult create() throws ModelException, ServiceClientException, InterruptedException {
        if (!isNew()) {
            throw new ModelError(ModelError.ErrorType.CANT_CREATE, this, getEnvironment().getMessageProvider().translate("ExplorerError", "Object already exists"));
        }
        
        final boolean canCreate = copySource==null ? beforeCreate() : beforePaste(copySource);

        if (!canCreate) {
            return EEntityCreationResult.CANCELED_BY_CLIENT;
        }

        checkPropertyValues();
        final CreateRs rs;
        try {
            rs = getEnvironment().getEasSession().create(this);
        } catch (ObjectNotFoundError error) {
            processObjectNotFound(error);
            throw error;
        }
        
        if (rs.getPID()==null){//creation was canceled         
             final String 
                    msg = getEnvironment().getMessageProvider().translate("TraceMessage", "Object creation was canceled by server");
             getEnvironment().getTracer().debug(msg);
            return EEntityCreationResult.CANCELED_BY_SERVER;
        }else{
            activate(rs.getPID(), rs.getTitle(), classId, null);            
            setIsEdited(false);
            final EntityModel copySrc = copySource;
            copySource = null;
            isNew = false;
            setExists(true);
            
            updateCommands();
            if (copySrc==null){
                afterCreate();
            }else{
                afterPaste(copySrc);
            }
            
            return EEntityCreationResult.SUCCESS;
        }        
    }

    public boolean delete(final boolean forced) throws ServiceClientException, InterruptedException {
        if (isNew()) {
            throw new ModelError(ModelError.ErrorType.CANT_DELETE, this, getEnvironment().getMessageProvider().translate("ExplorerError", "Object does not exist"));
        }

        if (!beforeDelete()) {
            return false;
        }

        if ((forced || confirmToDelete()) && deleteImpl(false, forced)) {
            setExists(false);            
            afterDelete();
            onDeleteSynchronization();
            return true;
        }
        return false;
    }
    
    protected boolean confirmToDelete(){
        final String title = getEnvironment().getMessageProvider().translate("ExplorerMessage", "Confirm Object Deletion");
        final String msg = getEnvironment().getMessageProvider().translate("ExplorerMessage", "Do you really want to delete \'%s\'?");
        return getEnvironment().messageConfirmation(title, String.format(msg, getTitle()));
    }

    public void cancelChanges() {
        if (isInSelectorRowContext() && synchronizedModel != null) {
            //Если был вызван для строки селектора - передаем вызов в модель редактора
            //чтобы учесть изменения в дочерних моделях.
            synchronizedModel.cancelChanges();
        }

        if (properties != null) {
            //Copy properties collection to avoid concurrent modification in overrided setters
            final Collection<Property> props = new ArrayList<>();
            props.addAll(properties.values());
            for (Property property : props) {
                property.cancelChanges();
            }
        }

        afterRead();
        setIsEdited(false);
        if (synchronizedModel != null) {
            synchronizedModel.afterRead();
        }
    }

    //Открытие связанных моделей
    //Создать синхронизированную модель строки селектора для редактирования объекта в селекторе
    public EntityModel openInSelectorEditModel() {
        if (!isInSelectorRowContext()) {
            throw new IllegalUsageError("openInSelectorEditModel() can be used only for entity with IContext.SelectorRow context");
        }
        IContext.InSelectorEditing ctx = new IContext.InSelectorEditing(this);
        final PresentationChangedHandler pch = new PresentationChangedHandler(this);
        ctx.setPresentationChangedHandler(pch);
        EntityModel m1 = getEditorPresentationDef().createModel(ctx);
        m1.parentModel = parentModel;
        try {
            m1.startSynchronization(this);
            return pch.getActualEntityModel() != null ? pch.getActualEntityModel() : m1;
        } finally {
            ((IContext.Entity) m1.getContext()).setPresentationChangedHandler(null);
        }
    }
    //Модель-источник для копирования при вставке в дерево
    //Не равен null только на время активации копии.
    private EntityModel sourceEntityModel;

    /**
     * Метод создает новую модель сущности для вставки в дерево проводника.
     * Атрибуты и значения свойств созданной модели копируются из данной. Может
     * быть вызван только для строки селектора селекторе.
     *
     * @return копия модели сущности для вставки в дерево проводника.
     */
    public EntityModel openChoosenEditModel() {
        if (getPid() == null && !isNew) {
            throw new IllegalUsageError("entity model was not activated");
        }
        if (isNew) {
            throw new IllegalUsageError("object does not exist");
        }
        if (!isInSelectorRowContext()) {
            throw new IllegalUsageError("entity model is not a select row");
        }
        final IContext.SelectorRow selfContext = (IContext.SelectorRow) getContext();
        final IContext.ChoosenEntityEditing choosenEntityContext = new IContext.ChoosenEntityEditing(this);
        final Id editorPresentationId = selfContext.parentGroupModel.getEditorPresentationIdForChoosenEntity(this);
        final RadEditorPresentationDef presentation = getEnvironment().getDefManager().getEditorPresentationDef(editorPresentationId);
        final EntityModel choosenEntity = presentation.createModel(choosenEntityContext);
        choosenEntity.sourceEntityModel = this;
        try {
            choosenEntity.activateCopy(this);
        } finally {
            choosenEntity.sourceEntityModel = null;
        }
        final String msg = "entity model \"%s\" was created";
        getEnvironment().getTracer().put(EEventSeverity.DEBUG, String.format(msg, getTitle()), EEventSource.EXPLORER);
        return choosenEntity;
    }

    protected Id getEditorPresentationIdForChooseIntoTree() {
        return getEditorPresentationDef().getId();
    }

    @Override
    Model getOwner() {
        return sourceEntityModel == null ? super.getOwner() : sourceEntityModel.getOwner();
    }

    /**
     * Метод создает модель сущности для редактирования родительского (по
     * отношению к данной сущности) объекта. Чтобы определить презентацию
     * редактора, по которой будет создана модель, и заголовок сущности
     * выполняется запрос CalculateParentTitles. Если в ответе сервера сведения
     * о нужном родительском объекте отсутствуют (например, когда не задано
     * значение соответствующего свойства-ссылки), то метод возвращает null.
     *
     * @param explorerItemDef - дочерний для презентации редактора данной модели
     * элемент проводника.
     * @return Модель родительской сущности, или null если родительская сущность
     * не задана.
     * @throws ServiceClientException ошибки при выполнении запроса на получение
     * идентификатора презентации редактора.
     * @throws InterruptedException выполнении запроса на получение
     * идентификатора презентации редактора было прервано.
     *
     */
    public EntityModel openParentEditModel(final RadParentRefExplorerItemDef explorerItemDef, final Model holder) throws ServiceClientException, InterruptedException {
        if (explorerItemDef == null) {
            throw new IllegalArgumentError("explorer item was not defined");
        }
        if (getPid() == null && !isNew) {
            throw new IllegalUsageError("entity model was not activated");
        }
        if (!isExists) {
            if (isNew) {
                throw new IllegalUsageError("object does not exist");
            } else {
                throw new ObjectNotFoundError(this);
            }
        }
        String msg = "creating parent entity model based on explorer item #%s for child entity \"%s\"";
        getEnvironment().getTracer().put(EEventSeverity.DEBUG, String.format(msg, explorerItemDef.getId(), getTitle()), EEventSource.EXPLORER);
        if (accessibleExplItemsById == null) {
            getAccessibleExplorerItems();
        }        
        RawEntityModelData.ChildExplorerItemInfo explorerItemInfo = findChildExplorerItemInfo(explorerItemDef.getId());
        if (explorerItemInfo==null || !explorerItemInfo.isParentObject()){
            msg = "parent entity model based on explorer item #%s was not created for child entity \"%s\" (cannot get parent title info)";
            getEnvironment().getTracer().put(EEventSeverity.DEBUG, String.format(msg, explorerItemDef.getId(), getTitle()), EEventSource.EXPLORER);
            return null;            
        }else{
            final Id editorPresentationId = explorerItemInfo.getEditorPresentationId();
            RadEditorPresentationDef presentation = 
                getEnvironment().getDefManager().getEditorPresentationDef(editorPresentationId);
            final IContext.ParentEntityEditing ctx = new IContext.ParentEntityEditing(this, holder, explorerItemDef);
            final EntityModel parentEntityModel = presentation.createModel(ctx);
            final String obj_pid = explorerItemInfo.getParentObjectPidAsStr();
            final String obj_title = explorerItemInfo.getParentObjectTitle();
            final Id obj_classId = explorerItemInfo.getClassId();
            parentEntityModel.activate(obj_pid, obj_title, obj_classId, null);
            parentEntityModel.updateCommands();
            msg = "parent entity model \"%s\" based on explorer item #%s was created for child entity \"%s\"";
            getEnvironment().getTracer().put(EEventSeverity.DEBUG, String.format(msg, parentEntityModel.getTitle(), explorerItemDef.getId(), getTitle()), EEventSource.EXPLORER);
            return parentEntityModel;            
        }
    }

    /**
     * Метод создает инстанцию сущности с идентификатором pid, для
     * редактирования в контексте context. При вызове метода выполняется запрос
     * к серверу на чтение объекта. На основании ответа будет создана и
     * {@link #activate(org.radixware.schemas.eas.Object, List) проинициализирована}
     * модель сущности. <p> В параметре presentations передается список
     * идентификаторов желаемаемых презентаций редактора. Фактическая
     * презентация, на основании которой будет создана модель сущности,
     * определяется из ответа сервера (не обязательно из списка presentations) .
     *
     * @param presentations Список возможных презентаций редактора.
     * @param pid Идентификатор сущности. Обязательный параметр.
     * @return модель сущности.
     * @throws ServiceClientException ошибки при выполении запроса на чтение
     * объекта.
     * @throws InterruptedException запрос на чтение объекта был прерван.
     */    
    public static EntityModel openModel(final Pid pid, 
                                        final Id classId, 
                                        final Collection<Id> presentations, 
                                        final IContext.Entity context
                                        ) throws ServiceClientException, InterruptedException {
        if (presentations == null || presentations.isEmpty()) {
            throw new IllegalArgumentError("editor presentation was not defined");
        }
        if (pid == null) {
            throw new IllegalArgumentError("entity identifier was not defined");
        }

        String msg = "creating model for entity with identifier \"%s\" in table %s.";
        context.getEnvironment().getTracer().put(EEventSeverity.DEBUG, String.format(msg, pid.toString(), pid.getTableId()), EEventSource.EXPLORER);

        //final Context.ContextlessEditing ctx = new Context.ContextlessEditing();
        //final ReadRs rs = Environment.session.read(pid, classId, presentations, ctx);
        final ReadRs rs = 
            context.getEnvironment().getEasSession().read(pid, classId, presentations, context, true);
        final Id presentationId = rs.getData().getPresentation().getId();
        final EntityModel entity;
        final RadEditorPresentationDef presentation = context.getEnvironment().getDefManager().getEditorPresentationDef(presentationId);
        entity = presentation.createModel(context);
        entity.activate(new RawEntityModelData(rs));
        msg = "model for entity with class #%s and identifier \"%s\"\n was created by editor presentation %s.\n in context %s";
        context.getEnvironment().getTracer().put(EEventSeverity.DEBUG,
                String.format(msg, entity.getClassId(), entity.getPid().toString(), entity.getDefinition().toString(), context.getDescription()),
                EEventSource.EXPLORER);
        return entity;
    }
    

    /**
     * Метод создает инстанцию сущности с идентификатором pid, для открытия в
     * режиме бесконтекстного редактирования. При вызове метода выполняется
     * запрос к серверу на чтение объекта. На основании ответа будет создана и
     * {@link #activate(org.radixware.schemas.eas.Object, List) проинициализирована}
     * модель сущности. <p> В параметре presentations передается список
     * идентификаторов желаемаемых презентаций редактора. Фактическая
     * презентация, на основании которой будет создана модель сущности,
     * определяется из ответа сервера (не обязательно из списка presentations) .
     *
     * @param presentations Список возможных презентаций редактора.
     * @param pid Идентификатор сущности. Обязательный параметр.
     * @return модель сущности.
     * @throws ServiceClientException ошибки при выполении запроса на чтение
     * объекта.
     * @throws InterruptedException запрос на чтение объекта был прерван.
     */
    public static EntityModel openContextlessModel(IClientEnvironment environment, final Pid pid, final Id classId, final Collection<Id> presentations) throws ServiceClientException, InterruptedException {
        return openModel(pid, classId, presentations, new IContext.ContextlessEditing(environment));
    }
    
    public static EntityModel openContextlessModel(IClientEnvironment environment, final Pid pid, final Id classId, final Collection<Id> presentations, final Model holderModel) throws ServiceClientException, InterruptedException {
        return openModel(pid, classId, presentations, new IContext.ContextlessEditing(environment,null,holderModel));
    }    

    public static EntityModel openContextlessModel(IClientEnvironment environment, final Pid pid, final Id classId, final Id presentationId) throws ServiceClientException, InterruptedException {
        return openModel(pid, classId, Collections.singleton(presentationId), new IContext.ContextlessEditing(environment));
    }        
    
    public static EntityModel openContextlessModel(IClientEnvironment environment, final Pid pid, final Id classId, final Id presentationId, final Model holderModel) throws ServiceClientException, InterruptedException {
        return openModel(pid, classId, Collections.singleton(presentationId), new IContext.ContextlessEditing(environment,null,holderModel));
    }        
    

    /**
     * Метод создает инстанцию сущности, для создания нового объекта. При вызове
     * метода выполняется запрос к серверу на подготовку к создание объекта. На
     * основании ответа будет создана и
     * {@link #activate(org.radixware.schemas.eas.Object, List) проинициализирована}
     * модель новой сущности. <p> В параметре epd передается желаемая
     * презентация редактора. Фактическая презентация, на основании которой
     * будет создана модель новой сущности, определяется из ответа сервера. <p>
     * Модель созданная в данном методе является лишь "заготовкой" для нового
     * объекта. Окончательное создание объекта произойдет только после вызова
     * метода {@link #create()}.
     *
     * @param epd желаемая презентация редактора для модели новой сущности.
     * Обязательный параметр.
     * @param classId Идентификатор класса новой сущности.
     * @param src Если этот параметр задан, то новая сущность будет создаваться
     * как копия сущности src.
     * @param ctx Контекст в котором происходит создание сущности. Обязательный
     * параметр. Допускается
     *  {@link Context.InSelectorCreating}, {@link Context.ObjectPropCreating}
     * либо {@link Context.ContextlessCreating}.
     * @return модель сущности для создания нового объекта.
     * @throws ServiceClientException ошибки при выполении запроса на подготовку
     * к созданию объекта.
     * @throws InterruptedException запрос на подготовку к созданию нового
     * объекта был прерван.
     * @see #isNew()
     * @see #isExists()
     */
    public static EntityModel openPrepareCreateModel(final RadEditorPresentationDef epd, final Id classId, final EntityModel src, final List<Id> alternativePresentationIds, final Map<Id, Object> initialValues, final IContext.Entity ctx) throws ServiceClientException, InterruptedException {
        if (epd == null) {
            throw new IllegalArgumentError("creation presentation was not defined");
        }
        if (ctx == null) {
            throw new IllegalArgumentError("context was not defined");
        }

        if (!(ctx instanceof IContext.InSelectorCreating)
                && !(ctx instanceof IContext.ObjectPropCreating)
                && !(ctx instanceof IContext.ContextlessCreating)) {
            throw new IllegalArgumentError("context \"" + ctx.getClass().getName() + "\" is not suitable for new entity");
        }

        String msg = "creating model for new entity with class #%s was created by editor presentation %s.";
        ctx.getEnvironment().getTracer().put(EEventSeverity.DEBUG, String.format(msg, classId, epd.toString()), EEventSource.EXPLORER);

        //Создаем модель для заполнения начальных значений свойств
        //перед отправкой запрроса на сервер.
        EntityModel entity = epd.createModel(ctx);
        entity.isNew = true;
        entity.classId = classId;
        entity.setExists(false);
        entity.copySource = src;
        
        if (initialValues != null && !initialValues.isEmpty()) {
            for (Map.Entry<Id, Object> entry : initialValues.entrySet()) {
                entity.getProperty(entry.getKey()).setValueObject(entry.getValue());
            }
        }

        boolean eventResult = src != null ? entity.beforePreparePaste(src)
                : entity.beforePrepareCreate();
        if (!eventResult) {
            return null;
        }

        final PrepareCreateRs rs;
        try {
            final List<Id> creationPresentationIds = new ArrayList<>();
            creationPresentationIds.add(epd.getId());
            if (alternativePresentationIds != null) {
                for (Id presentationId : alternativePresentationIds) {
                    if (presentationId != null && !creationPresentationIds.contains(presentationId)) {
                        creationPresentationIds.add(presentationId);
                    }
                }
            }
            rs = ctx.getEnvironment().getEasSession().prepareCreate(creationPresentationIds,
                    (classId != null ? classId : epd.getOwnerClassId()),
                    (src != null ? src.getPid() : null),
                    ctx, entity.getActiveProperties());
        } catch (ObjectNotFoundError error) {
            if (src != null && error.inContextOf(src)) {
                src.processObjectNotFound(error);
            }
            throw error;
        }
        final List<org.radixware.schemas.eas.PresentableObject> newObjects = rs.getObjectList();
        if (newObjects==null || newObjects.isEmpty()){
            return null;
        }
        final org.radixware.schemas.eas.PresentableObject data = newObjects.get(0);
        final Id presentationId = data.getPresentation().getId();

        if (!epd.getId().equals(presentationId)) {
            final RadEditorPresentationDef creationPresentation = ctx.getEnvironment().getDefManager().getEditorPresentationDef(presentationId);
            entity = creationPresentation.createModel(ctx);
        }

        entity.isNew = true;
        entity.setExists(false);
        entity.copySource = src;
        entity.activate(new RawEntityModelData(data));        
        eventResult = src != null ? entity.afterPreparePaste(src)
                : entity.afterPrepareCreate();
        if (!eventResult) {
            return null;
        }

        msg = "model for new entity with class #%s was created by editor presentation %s.";
        ctx.getEnvironment().getTracer().put(EEventSeverity.DEBUG, String.format(msg, classId, entity.getDefinition().toString()), EEventSource.EXPLORER);
        return entity;
    }       
    
    /**
     * Метод создает одну или несколько моделей презентаций редактора, на основе которых в 
     * последствии могут создаваться объекты сущности. Для каждого идентификатора класса сущности
     * из списка <code>classIds</code> будет создана одна модель презентации редактора.
     * При вызове метода выполняется запрос к серверу на подготовку к созданию объектов. 
     * На основании ответа будут созданы и 
     * {@link #activate(org.radixware.schemas.eas.Object, List) проинициализированы}
     * модели новый объектов сущности. <p> В параметре epd передается желаемая
     * презентация редактора. Фактическая презентация, на основании которой
     * будет создана модель нового объекта сущности, определяется из ответа сервера. <p>
     * Модели созданные в данном методе являются лишь "заготовкой" для новых
     * объектов. Окончательное создание объекта произойдет только после вызова
     * метода модели {@link #create()}.
     *
     * @param epd желаемая презентация редактора для модели новых объектов сущности.
     * Обязательный параметр.
     * @param classIds идентификаторы классов новых объектов сущности
     * @param  alternativePresentationIds идентификаторы альтернативных презентаций редактора.
     * Если у пользователя нет прав на презентацию редактора, которая была передана в параметре
     * <code>epd</code>, то будет выбрана одна из презентаций из списка, на которую у пользователя
     * есть права. Может быть <code>null</code>.
     * @param initialValues начальные значения свойств создаваемых объектов. Может быть <code>null</code>.
     * @param ctx Контекст, в котором происходит создание объектов сущности. Обязательный
     * параметр. Допускается
     *  {@link Context.InSelectorCreating}, {@link Context.ObjectPropCreating}
     * либо {@link Context.ContextlessCreating}.
     * @return список моделей сущности для создания новых объектов.
     * @throws ServiceClientException ошибки при выполении запроса на подготовку
     * к созданию объектов.
     * @throws InterruptedException запрос на подготовку к созданию новых
     * объектов был прерван.
     * @see #isNew()
     * @see #isExists()
     */    
    public static List<EntityModel> openPrepareCreateModels(final RadEditorPresentationDef epd, final List<Id> classIds,  final List<Id> alternativePresentationIds, final Map<Id, Object> initialValues, final IContext.Entity ctx) throws ServiceClientException, InterruptedException {
        if (epd == null) {
            throw new IllegalArgumentError("creation presentation was not defined");
        }
        if (ctx == null) {
            throw new IllegalArgumentError("context was not defined");
        }

        if (!(ctx instanceof IContext.InSelectorCreating)
                && !(ctx instanceof IContext.ObjectPropCreating)
                && !(ctx instanceof IContext.ContextlessCreating)) {
            throw new IllegalArgumentError("context \"" + ctx.getClass().getName() + "\" is not suitable for new entity");
        }

        final List<EntityModel> templates = new LinkedList<>();
        for (Id classId: classIds){
            final String msg = "creating model for new entity with class #%s was created by editor presentation %s.";
            ctx.getEnvironment().getTracer().put(EEventSeverity.DEBUG, String.format(msg, classId, epd.toString()), EEventSource.EXPLORER);
            
            //Создаем модель для заполнения начальных значений свойств
            //перед отправкой запрроса на сервер.
            EntityModel entity = epd.createModel(ctx);
            entity.isNew = true;
            entity.classId = classId;
            entity.setExists(false);            

            if (initialValues != null && !initialValues.isEmpty()) {
                for (Map.Entry<Id, Object> entry : initialValues.entrySet()) {
                    entity.getProperty(entry.getKey()).setValueObject(entry.getValue());
                }
            }

            if (entity.beforePrepareCreate()) {
                templates.add(entity);
            }
        }
        
        final List<Id> creationPresentationIds = new ArrayList<>();
        creationPresentationIds.add(epd.getId());
        if (alternativePresentationIds != null) {
            for (Id presentationId : alternativePresentationIds) {
                if (presentationId != null && !creationPresentationIds.contains(presentationId)) {
                    creationPresentationIds.add(presentationId);
                }
            }
        }
        final PrepareCreateRs rs = 
            ctx.getEnvironment().getEasSession().prepareCreate(creationPresentationIds, ctx, templates);
        final List<org.radixware.schemas.eas.PresentableObject> xmlObjects = rs.getObjectList();
        if (xmlObjects==null || xmlObjects.isEmpty()){
            return Collections.emptyList();
        }
        final List<EntityModel> result = new LinkedList<>();
        for (org.radixware.schemas.eas.PresentableObject xmlObject: xmlObjects){
            final Id presentationId = xmlObject.getPresentation().getId();
            final RadEditorPresentationDef creationPresentation = ctx.getEnvironment().getDefManager().getEditorPresentationDef(presentationId);
            final EntityModel entity = creationPresentation.createModel(ctx);            

            entity.isNew = true;
            entity.setExists(false);
            entity.activate(new RawEntityModelData(xmlObject));        
            if (entity.afterPrepareCreate()) {
                result.add(entity);
            }else{
                final String msg = "model for new entity with class #%s was created by editor presentation %s.";
                ctx.getEnvironment().getTracer().put(EEventSeverity.DEBUG, String.format(msg, entity.getClassId(), entity.getDefinition().toString()), EEventSource.EXPLORER);
            }            
        }
        return result;
    }
    
    public static List<EntityModel> openPrepareCreateModels(final RadEditorPresentationDef epd, final List<Id> classIds,  final List<Id> alternativePresentationIds, final IContext.Entity ctx) throws ServiceClientException, InterruptedException {
        return openPrepareCreateModels(epd, classIds, alternativePresentationIds, null, ctx);
    }
    
    public static List<EntityModel> openPrepareCreateModels(final RadEditorPresentationDef epd, final List<Id> classIds,  final IContext.Entity ctx) throws ServiceClientException, InterruptedException {
        return openPrepareCreateModels(epd, classIds, null, null, ctx);
    }    

    public static EntityModel openPrepareCreateModel(final RadEditorPresentationDef epd, final Id classId, final EntityModel src, final Map<Id, Object> initialValues, final IContext.Entity ctx) throws ServiceClientException, InterruptedException {
        return openPrepareCreateModel(epd, classId, src, null, initialValues, ctx);
    }

    public static EntityModel openPrepareCreateModel(final RadEditorPresentationDef epd, final Id classId, final EntityModel src, final List<Id> alternativePresentationIds, final IContext.Entity ctx) throws ServiceClientException, InterruptedException {
        return openPrepareCreateModel(epd, classId, src, alternativePresentationIds, null, ctx);
    }

    public static EntityModel openPrepareCreateModel(final Id EditorPresentationClassId, final Id presentationId, final Id classId, final EntityModel src, final IContext.Entity ctx) throws ServiceClientException, InterruptedException {
        return openPrepareCreateModel(ctx.getEnvironment().getDefManager().getEditorPresentationDef(presentationId), classId, src, null, null, ctx);
    }

    public static EntityModel openPrepareCreateModel(final Id EditorPresentationClassId, final Id presentationId, final Id classId, final EntityModel src, final List<Id> alternativePresentationIds, final IContext.Entity ctx) throws ServiceClientException, InterruptedException {
        return openPrepareCreateModel(ctx.getEnvironment().getDefManager().getEditorPresentationDef(presentationId), classId, src, null, null, ctx);
    }

    public static EntityModel openPrepareCreateModel(final Id EditorPresentationClassId, final Id presentationId, final Id classId, final EntityModel src, final Map<Id, Object> initialValues, final IContext.Entity ctx) throws ServiceClientException, InterruptedException {
        return openPrepareCreateModel(ctx.getEnvironment().getDefManager().getEditorPresentationDef(presentationId), classId, src, null, initialValues, ctx);
    }

    /*
     * @Override protected RadExplorerItemDef getChildExplorerItemDef(Id
     * explItemId) { return
     * getEditorPresentationDef().getChildrenExplorerItems().findExplorerItem(explItemId);
     * }
     */
    @Override
    public List<Id> getAccessibleCommandIds() {
        if (commandIds == null) {
            final Collection<RadCommandDef> commandsCollection = getEditorPresentationDef().getEnabledCommands();
            commandIds = new ArrayList<>(commandsCollection.size());
            for (RadCommandDef command : commandsCollection) {
                if ((command.getAccessibility() != ECommandAccessibility.ONLY_FOR_NEW || isNew)
                        && ((command.getAccessibility() != ECommandAccessibility.ONLY_FOR_EXISTENT &&
                             command.getAccessibility() != ECommandAccessibility.ONLY_FOR_FIXED)
                        || isExists())
                        && !getContext().getRestrictions().getIsCommandRestricted(command)
                        && isCommandAccessible(command)) {
                    commandIds.add(command.getId());
                }
            }
        }
        return Collections.unmodifiableList(commandIds);
    }

    //Вспомогательные методы
    private boolean modelActivating = false, scheduledAfterRead = false;

    /**
     * Метод производит инициализацию модели сущности. Позволяет задать
     * идентификатор сущности, ее заголовок, иденификатор класса, начальные
     * значения свойств (значения полученные от сервера). Свойства, переданные в
     * параметре propVals, должны присутствовать в {@link EditorPresentationDef презентации редактора},
     * на основании которой создана модель, иначе произойдет ошибка {@link NoDefinitionWithSuchIdError}.
     * Каждая модель сущности должна быть проинициализирована перед
     * использованием. <p> Метод может быть вызван провторно. В случае
     * повторного вызова помимо обновления перечисленных выше атрибутов,
     * производится удаление из модели свойств, значения которых приходят с
     * сервера отдельным запросом (см. {@link PropertyDef#isReadSeparately()}).
     * <p> Вызывается при выполнении операций {@link #read()}, {@link #update()}, {@link #create()}.
     *
     * @param pid_	идентификатор сущности. Если равен null сущность помечается
     * как несуществующая (см. {@link #isExists()}).
     * @param objectTitle_ заголовок сущности
     * @param classId_ идентификатор класса сущности.
     * @param propVals начальные значения свойств.
     * @see #activate(org.radixware.schemas.eas.Object, List)
     * @see #activateCopy(EntityModel)
     */
    public void activate(String pid_, String objectTitle_, Id classId_, List<PropertyValue> propVals) {
        if (pid_ != null && !pid_.isEmpty()) {
            pid = new Pid(getEditorPresentationDef().getTableId(), pid_);
            setExists(true);
            isNew = false;
        } else {
            wasRead = true;
            setExists(false);
            isNew = true;
        }
        classId = classId_;        
        deactivateSeparatelyReadingProps();
        if (propVals != null || canSetPropertyValuesFromCopySource()) {
            activateProps(propVals);
        }
        if (!isNew() && !activatePropsRecursionBlock){
            activatePropsRecursionBlock = true;
            try{
                setPropsToIgnoreUndefinedValCheck(findUndefinedMandatoryProperties());
            }finally{
                activatePropsRecursionBlock = false;
            }
        }
        setTitle(objectTitle_);
        if (propVals !=null ){
            afterReadInvoke();
        }
        wasActivated = true;
    }
    
    /**
     * Метод проверяет была ли данная модель сущности проинициализирована. 
     * @return <code>true</code> если данная модель была проинициализирована и готова к использованию
     */
    public final boolean wasActivated(){
        return wasActivated;
    }
    
    public void setServerPropertyValues(List<PropertyValue> propVals) {
        if ((propVals != null && !propVals.isEmpty()) || canSetPropertyValuesFromCopySource()) {
            activateProps(propVals);
            afterReadInvoke();
            updateCommands();//RADIX-7725
        }
    }
    
    private void afterReadInvoke(){
        if (modelActivating) {
            //Если в реализации afterRead делается дочитывание свойств модели,
            //то вместо рекурсивного вызова делается отложенный
            scheduledAfterRead = true;
        } else {
            modelActivating = true;
            scheduledAfterRead = false;
            try {
                afterRead();
            } finally {
                modelActivating = false;
            }
            if (scheduledAfterRead) {
                try {
                    afterRead();
                } finally {
                    scheduledAfterRead = false;
                }
            }
        }
    }    
    
    private void updateCommands(){
        for (Id commandId: this.getAccessibleCommandIds()){
            final Command command = getCommand(commandId);
            if (command.hasSubscriber()){
                command.afterModify();
            }
        }
    }

    /**
     * Инициализация модели сущности данными, полученными от сервера. Метод
     * читает данные из параметра data и вызывает {@link #activate(String, String, String, List)}.
     * Кроме того здесь устанавливается список команд, запрещенных на сервере
     * (см. {@link Command#setServerEnabled(boolean)}
     *
     * @param data данные сущности. Присылаются сервером в ответе на запрос.
     * @param disabledCommands список команд, запрещенных на сервере.
     * Присылаeтся в ответе на запрос.
     * @see #activate(String, String, String, List)
     */
    protected final void activate(final RawEntityModelData rawModelData, final boolean withLobValues) {
        final List<PropertyValue> vals;
        final RadClassPresentationDef classDef = 
            getEnvironment().getDefManager().getClassPresentationDef(rawModelData.getEntityObjectData().getClassId());
        vals = readProperties(rawModelData.getEntityObjectData().getProperties(), classDef, withLobValues);
        final ESelectorRowStyle rowStyle = rawModelData.getSelectorRowStyle();
        if (rowStyle!=null){
            setSelectorRowStyle(rowStyle);
        }        
        activate(rawModelData.getEntityObjectData().getPID(), rawModelData.getEntityObjectData().getTitle(), rawModelData.getEntityObjectData().getClassId(), vals);
        final String srcPidAsStr = rawModelData.getEntityObjectData().getSrcPID();
        if (srcPidAsStr!=null){
            srcPid = new Pid(classDef.getTableId(), srcPidAsStr);
        }
        updateServerRestrictions(rawModelData.getRestrictions());
        if (rawModelData.getEnabledEditorPages()!=null){
            updateServerEditorPageRestrictions(rawModelData.getEnabledEditorPages());
            if (synchronizedModel!=null){
                synchronizedModel.updateServerEditorPageRestrictions(rawModelData.getEnabledEditorPages());
            }
        }
        updateAccessibleExplorerItems(rawModelData.getAccessibleChildExplorerItems());
        if (synchronizedModel!=null){
            synchronizedModel.updateAccessibleExplorerItems(rawModelData.getAccessibleChildExplorerItems());
        }
        updateCommands();
    }

    public void activate(final RawEntityModelData rawModelData) {
        activate(rawModelData, isNew());
        wasRead = true;
    }

    private void updateServerRestrictions(RawEntityModelData.ServerRestrictions serverRestrictions) {
        if (serverRestrictions!=null && !serverRestrictions.isEmpty()) {
            restrictions.setServerRestrictions(serverRestrictions.getDisabledActions());
            setServerDisabledCommands(serverRestrictions.getDisabledCommands());
        }
    }
    
    private void updateAccessibleExplorerItems(Collection<RawEntityModelData.ChildExplorerItemInfo> explorerItems){
        if (explorerItems!=null){
            accessibleExplItemsById = new HashMap<>();
            for (RawEntityModelData.ChildExplorerItemInfo explorerItemInfo: explorerItems){
                accessibleExplItemsById.put(explorerItemInfo.getId(), explorerItemInfo);
            }
        }
    }
    
    private RawEntityModelData.ChildExplorerItemInfo findChildExplorerItemInfo(final Id explorerItemId) throws ServiceClientException, InterruptedException{
        if (accessibleExplItemsById==null){
            getAccessibleExplorerItems();
        }
        return accessibleExplItemsById.get(explorerItemId);
    }

    /**
     * Инициализация модели сущности на основе другой сущности. Метод считывает
     * из модели src идентификатор сущности, заголовок, идентификатор класса,
     * свойства и передает их в {@link #activate(String, String, String, List)}.
     * <p>Используется в операциях синхронизации модели и вставки в дерево
     * проводника.
     *
     * @param src модель сущности по которой будет проинициализирована данная
     * модель.
     * @see #activate(String, String, String, List)
     */
    public void activateCopy(final EntityModel src) {
        if (getContext() == null) {
            throw new IllegalStateException("Context is not defined");
        }
        if (isEdited()) {
            throw new IllegalStateException("Entity was edited");
        }
        if (src.getClassId() == null) {
            throw new IllegalStateException("Source entity was not activated");
        }

        activate(src.getPid() != null ? src.getPid().toString() : null, src.getTitle(), src.getClassId(), null);
        final Collection<Property> activeProperties = src.getActiveProperties();
        Property newProperty;
        for (Property property : activeProperties) {
            if (!property.getDefinition().isReadSeparately()) {
                newProperty = properties==null ? null : properties.get(property.getId());//case when property was activated in constructor of other property
                if (newProperty==null){
                    newProperty = activateProperty(property.getId());
                }
                if (synchronizedModel == null) {
                    newProperty.copy(property);
                }
            }
        }
        setSelectorRowStyle(src.selectorStyle);
        if (src.isEdited) {
            setIsEdited(true);
        }
        restrictions.add(src.restrictions); //RADIX-1462        
        updateCommands();
        copyServerEditorPageRestrictions(src);
        if (src.accessibleExplItemsById!=null){
            accessibleExplItemsById = new HashMap<>(src.accessibleExplItemsById);
        }
    }

    private EntityModel checkPresentation(final RawEntityModelData rawData,
            org.radixware.schemas.eas.Presentation presentation) {
        final Id presentationId = presentation.getId(),
                presentationClassId = presentation.getClassId();
        if (!presentationId.equals(getDefinition().getId())
                || !presentationClassId.equals(getDefinition().getOwnerClassId())) {
            final String traceMessage = getEnvironment().getMessageProvider().translate("TraceMessage", "Changing editor presentation for object \"%s\"\n "
                    + "from #%s (#%s) to #%s (#%s)");
            final String finalMessage = String.format(traceMessage, getTitle(), getDefinition().getId(), getDefinition().getOwnerClassId(),
                    presentationId, presentationClassId);
            getEnvironment().getTracer().put(EEventSeverity.EVENT, finalMessage, EEventSource.EXPLORER);
            if (((IContext.Entity) getContext()).getPresentationChangedHandler() != null) {
                return ((IContext.Entity) getContext()).getPresentationChangedHandler().onChangePresentation(rawData,
                        presentationClassId, presentationId);
            } else {
                final String errorMessage = getEnvironment().getMessageProvider().translate("ExplorerException", " from #%s (#%s) to #%s (#%s)");
                throw new ModelError(ModelError.ErrorType.CANT_CHANGE_PRESENTATION, this, String.format(errorMessage, getTitle(), getDefinition().getId(), getDefinition().getOwnerClassId(), presentationId, presentationClassId));
            }
        }
        return null;
    }

    public void startSynchronization(EntityModel model2) {
        if (synchronizedModel != null) {
            throw new IllegalStateException("Synchronization already established");
        }
//		if (isEdited() || model2.isEdited())
//			throw new DbpError("Object was edited");
        if (getClass() != model2.getClass()) {
            throw new IllegalArgumentError("Different entity models");
        }
        synchronizedModel = model2;
        model2.synchronizedModel = this;
        activateCopy(model2);
        if (commands != null) {
            for (Command command : commands.values()) {
                command.startSynchronization(synchronizedModel.getCommand(command.getId()));
            }
        }
        restrictions.startSynchronization(model2.restrictions);
    }

    public void stopSynchronization() {
        if (synchronizedModel != null) {
            if (properties != null) {
                for (Property p : properties.values()) {
                    p.stopSynchronization();
                }
            }
            if (commands != null) {
                for (Command c : commands.values()) {
                    c.stopSynchronization();
                }
            }
            restrictions.stopSynchronization(); //RADIX-1462
            synchronizedModel.synchronizedModel = null;
            synchronizedModel = null;
        }
    }
    
    @Override
    /*
     * Если чтения всей сущности еще не было, то при получении значения свойства
     * (в случае когда перекрыт геттер) в список свойств могут быть добавлены
     * новые записи. Данный метод вызывает геттеры всех свойств чтобы
     * сформировать окончательный список свойств.
     */
    protected Collection<Property> getActiveProperties(IPropertiesFilter filter) {
        if (isNew() && !wasRead()) {
            //to avoid concurrent modification
            Collection<Property> props = new ArrayList<>(super.getActiveProperties(filter));
            Collection<Property> result = new ArrayList<>();
            //Check if property value was defined.
            for (Property property : props) {
                if (property.isActivated()) {
                    result.add(property);
                }
            }
            return Collections.unmodifiableCollection(result);
        } else {
            Collection<Property> result = new ArrayList<>();
            Collection<Property> props = new ArrayList<>();
            props.addAll(super.getActiveProperties(filter));
            for (Property property : props) {
                if (property.isActivated()) {//to avoid unwanted read operation
                    try{
                        property.getValueObject();
                        result.add(property);
                    }
                    catch(Exception exception){
                        final String message = 
                            getEnvironment().getMessageProvider().translate("ExplorerError", "Can't get value of property \'%s\' (#%s) for object \'%s\' in presentation #%s");
                        final String formattedMessage =
                            String.format(message, property.getTitle(), property.getTitle(), property.getId().toString(), getTitle(), getDefinition().getId().toString());
                        getEnvironment().getTracer().error(formattedMessage, exception);
                    }
                }
            }
            return Collections.unmodifiableCollection(result);
        }
    }

    public final org.radixware.schemas.eas.Object writeToXml(final org.radixware.schemas.eas.Object target, final boolean includeReadonlyProperties) {
        final org.radixware.schemas.eas.Object object = target != null ? target : org.radixware.schemas.eas.Object.Factory.newInstance();
        final Collection<Property> propertyList;
        if (isNew()) {
            propertyList = getActiveProperties();
            if (getSrcPid() != null) {
                object.setSrcPID(getSrcPid().toString());
            }
        } else {
            propertyList = includeReadonlyProperties ? getActiveProperties() : getEditedPropertiesImpl(true);//DBP-1547
            object.setPID(getPid().toString());
        }

        object.setClassId(getClassId());

        final org.radixware.schemas.eas.PropertyList list = object.addNewProperties();
        if (!propertyList.isEmpty()) {
            for (Property property : propertyList) {
                if ((isNew() || !includeReadonlyProperties || property.isReadonly() || property.isValEdited())
                    && (property.getType()!=EValType.OBJECT || (property.isValEdited() && !isNew()))/*RADIX-12141, RADIX-12320*/
                    && !property.isLocal()
                   ) {
                    property.writeValue2Xml(list.addNewItem());
                }
            }
        }

        return object;
    }

    private void activateProps(List<PropertyValue> propVals) {
        final List<Property> modifiedProperties = new LinkedList<>();
        if (propVals!=null && !propVals.isEmpty()){
            if (properties == null) {
                properties = new HashMap<>(propVals.size());
            }
            for (PropertyValue val : propVals) {
                RadPropertyDef def = val.getPropertyDef();
                Property property = properties.get(def.getId());
                if (property == null) {
                    property = createProperty(def);
                    properties.put(def.getId(), property);
                    if (synchronizedModel != null) {
                        if (synchronizedModel.properties != null && synchronizedModel.properties.containsKey(def.getId())) {
                            final Property p2 = synchronizedModel.getProperty(def.getId());
                            modifiedProperties.add(p2);
                            p2.setNotificationsBlocked(true);
                            p2.setServerValue(val);
                            property.startSynchronization(p2);
                        } else {
                            final Property p2 = synchronizedModel.getProperty(def.getId());//activate and startSynchronization
                            modifiedProperties.add(p2);
                            p2.setNotificationsBlocked(true);                        
                            p2.setServerValue(val);
                        }
                    }
                }
                modifiedProperties.add(property);
                property.setNotificationsBlocked(true);            
                property.setServerValue(val);
            }
        }
        if (canSetPropertyValuesFromCopySource()){
            final Collection<Property> activeProperties = copySource.getActiveProperties();
            Property newProperty;
            for (Property property : activeProperties) {
                newProperty = properties==null ? null : properties.get(property.getId());//case when property was activated in constructor of other property
                if (newProperty==null){
                    newProperty = activateProperty(property.getId());
                }
                if (newProperty!=null){
                    if (!modifiedProperties.contains(newProperty)){
                        modifiedProperties.add(newProperty);
                        newProperty.setNotificationsBlocked(true);
                    }
                    newProperty.copy(property);
                }
            }
            propertyValuesWasSetFromCopySource = true;
        }
        for (Property property: modifiedProperties){
            property.setNotificationsBlocked(false);
            if (property.isNotificationScheduled()){
                property.afterModify();
            }
        }
    }
    
    private boolean canSetPropertyValuesFromCopySource(){
        return isNew() && copySource!=null  && copySource.isNew() && !propertyValuesWasSetFromCopySource;
    }

    @Override
    protected Property activateProperty(Id id) {
        final Property property = super.activateProperty(id);
        if (property != null && synchronizedModel != null) {
            final Property p2;
            if (synchronizedModel.properties != null && synchronizedModel.properties.containsKey(id)) {
                p2 = synchronizedModel.properties.get(id);
                property.startSynchronization(p2);
            } else {
                synchronizedModel.activateProperty(id);
            }
        }
        return property;
    }

    @Override
    protected RadPropertyDef getPropertyDef(final Id propertyId) {
        if (getClassPresentationDef().isPropertyDefExistsById(propertyId)){
            //it NOT the same as getEditorPresentationDef().getPropertyDefById(propertyId)
            return getClassPresentationDef().getPropertyDefById(propertyId);
        }else{
            return super.getPropertyDef(propertyId);
        }
    }

    @Override
    public boolean isExplorerItemAccessible(final Id explorerItemId) throws ServiceClientException, InterruptedException {
        final RadExplorerItemDef childExplorerItem = getChildExplorerItemDef(explorerItemId);
        if (childExplorerItem == null) {
            return false;
        }
        return findChildExplorerItemInfo(explorerItemId)!=null;
    }

    private boolean finishEditRecursionBlock;
    
    @Override
    public void finishEdit() {
        if (!finishEditRecursionBlock){
            finishEditRecursionBlock = true;
            try{
                super.finishEdit();
                if (getContext() instanceof IContext.InSelectorEditing){
                    final GroupModel groupModel = getOwnerSelectorModel();
                    if (groupModel!=null){
                        groupModel.finishEdit();
                    }
                }
            }finally{
                finishEditRecursionBlock = false;
            }
        }
    }

    private void getAccessibleExplorerItems() throws ServiceClientException, InterruptedException {
        if (isInSelectorRowContext() && !wasActivated()) {
            final String message = getEnvironment().getMessageProvider().translate("ExplorerMessage", "Explorer item invocation from object of class %s (presentation %s) in selector row context");
            getEnvironment().getTracer().warning(String.format(message, getClassPresentationDef().toString(), getEditorPresentationDef().toString()));
        }
        final ListEdPresVisibleExpItemsRs response = 
                getEnvironment().getEasSession().listEdPresVisibleExpItems(this);
        Collection<RawEntityModelData.ChildExplorerItemInfo> childExplorerItems = 
            RawEntityModelData.ChildExplorerItemInfo.parse(response.getVisibleExplorerItems());
        if (childExplorerItems==null){
            childExplorerItems = Collections.<RawEntityModelData.ChildExplorerItemInfo>emptyList();
        }
        updateAccessibleExplorerItems(childExplorerItems);
        if (synchronizedModel!=null){
            synchronizedModel.updateAccessibleExplorerItems(childExplorerItems);
        }
    }

    private void deactivateSeparatelyReadingProps() {
        if (properties != null) {
            for (Property property : properties.values()) {
                if (property.getDefinition().isReadSeparately()) {
                    property.setServerValue(null);
                }
            }
        }
    }

    protected void onDeleteSynchronization() {
        synchronizeRemoving(true, getPid());
        if (getEnvironment().getTreeManager() != null) {
            //синхронизация дерева
            final IExplorerTreeManager tree = getEnvironment().getTreeManager();
            tree.afterEntitiesRemoved(Collections.singletonList(getPid()), null);
        }
    }

    private void synchronizeRemoving(final boolean synchronizeGroup, final Pid removingPid) {

        final GroupModel parentGroupModel = getOwnerSelectorModel();

        if (getEnvironment().getTreeManager() != null && (!Utils.equals(getPid(), removingPid) || getView() == null)) {
            //синхронизация дерева
            final IExplorerTreeManager tree = getEnvironment().getTreeManager();
            final IView currentView = parentGroupModel != null ? parentGroupModel.getView() : getView();
            tree.afterEntitiesRemoved(Collections.singletonList(removingPid), currentView);
        }

        if (synchronizeGroup) {
            //синхронизация группы
            removeFromParentGroup(removingPid);
        }

        if (getEditorPresentationDef().getTableId().equals(getEnvironment().getClipboard().getTableId())) {
            //синхронизация буфера обмена
            getEnvironment().getClipboard().remove(removingPid);
        }
    }

    private void removeFromParentGroup(final Pid removingPid) {
        final GroupModel parentGroupModel = getOwnerSelectorModel();
        if (parentGroupModel != null) {
            final EntityObjectsSelection selection = parentGroupModel.getSelection();
            if (selection.isObjectSelected(removingPid)){
                selection.unselectObject(removingPid);
            }
            if (parentGroupModel.getGroupView() != null) {
                parentGroupModel.getGroupView().entityRemoved(removingPid);
                if (parentGroupModel.getGroupView() != null) {//RADIX-6285: После обработки удаления селектор может быть закрыт
                    parentGroupModel.getGroupView().repaint();
                }
            } else {
                parentGroupModel.removeRow(parentGroupModel.findEntityByPid(removingPid));
            }
        }
    }

    final public GroupModel getOwnerSelectorModel() {
        if (getContext() instanceof IContext.InSelectorEditing) {
            return ((IContext.InSelectorEditing) getContext()).getGroupModel();
        } else if (isInSelectorRowContext()) {
            final IContext.SelectorRow rowCtx = (IContext.SelectorRow) getContext();
            return rowCtx.parentGroupModel;//.removeRow(pid);
        } else if (getContext() instanceof IContext.InSelectorCreating) {
            final IContext.InSelectorCreating creatingCtx = (IContext.InSelectorCreating) getContext();
            return creatingCtx.group;//.removeRow(pid);
        } else {
            return null;
        }
    }

    private boolean deleteImpl(final boolean cascade, final boolean forced) throws ServiceClientException, InterruptedException {
        try {
            getEnvironment().getEasSession().deleteObject(getPid(), classId, getDefinition().getId(), (IContext.Entity) getContext(), cascade);
        } catch (ObjectNotFoundError error) {
            if (error.inContextOf(this)) {//RADIX-3051
                return true;
            }
            throw error;
        } catch (ServiceCallFault e) {
            if (e.getFaultString().equals(ExceptionEnum.CONFIRM_SUBOBJECTS_DELETE.toString())) {
                if (forced || confirmToDeleteSubobjects(e.getMessage())) {
                    return deleteImpl(true, forced);
                } else {
                    return false;
                }
            }
            throw e;
        }
        setIsEdited(false);
        return true;
    }
    
    protected boolean confirmToDeleteSubobjects(final String message){
        final String title = getEnvironment().getMessageProvider().translate("ExplorerMessage", "Confirm Object Deletion");
        return getEnvironment().messageConfirmation(title, message);
    }

    private void processObjectNotFound(final ObjectNotFoundError objNotFound) {
        getEnvironment().getClipboard().remove(objNotFound.getPid());
        if (objNotFound.inContextOf(this)) {
            if (Utils.equalsNotNull(getPid(), objNotFound.getPid())) {
                isExists = false;
            }
            objNotFound.setContextEntity(this);
            //RADIX-3051
            synchronizeRemoving(false, objNotFound.getPid());
        }
    }

    @Override
    public void showException(final String title, final Throwable ex) {
        ObjectNotFoundError objectNotFound = null;
        UniqueConstraintViolationError ucvError = null;
        for (Throwable err = ex; err != null && err.getCause() != err; err = err.getCause()) {
            if (err instanceof ObjectNotFoundError) {
                objectNotFound = (ObjectNotFoundError) err;
                break;
            } else if (err instanceof UniqueConstraintViolationError) {
                ucvError = (UniqueConstraintViolationError) err;
                break;
            }
        }
        if (ucvError != null && ucvError.inContextOf(this)) {
            //RADIX-2487
            getEnvironment().messageError(title, ucvError.getMessage(this));
            if (getView() != null) {
                Property property;
                getView().setFocus();
                final List<Id> propertyIds = ucvError.findVisiblePropertyIds(this);
                for (Id propertyId : propertyIds) {
                    property = getProperty(propertyId);
                    if (!property.isReadonly() && property.hasOwnValue() && property.setFocused()) {
                        break;
                    }
                }
            }
        } else if (objectNotFound != null && objectNotFound.inContextOf(this)) {
            objectNotFound.setContextEntity(this);
            if (Utils.equalsNotNull(getPid(), objectNotFound.getPid())) {
                setExists(false);
            }
            super.showException(title, objectNotFound);
            synchronizeRemoving(true, objectNotFound.getPid());
        } else {
            super.showException(title, ex);
        }
    }
    
    private RawEntityModelData parseReadResponse(final ReadRs readResponse){
        final RawEntityModelData data = new RawEntityModelData(readResponse);
        if (checkPresentation(data, readResponse.getData().getPresentation())!=null){
            return null;
        }
        return data;
    }

    public boolean canInsertIntoTreeFromSelector() {
        return isInSelectorRowContext();
    }

// События
    protected boolean beforePrepareCreate() {
        return true;
    }

    protected boolean afterPrepareCreate() {
        return true;
    }

    protected boolean beforeCreate() throws ModelException {
        return true;
    }

    protected void afterCreate() throws ModelException {
    }

    protected boolean beforeCopy() {
        return true;
    }

    protected boolean beforePreparePaste(EntityModel src) {
        return beforePrepareCreate();
    }

    protected boolean afterPreparePaste(EntityModel src) {
        return afterPrepareCreate();
    }

    protected boolean beforePaste(EntityModel src) throws ModelException {
        return beforeCreate();
    }

    protected void afterPaste(EntityModel src) throws ModelException {
        afterCreate();
    }

    protected void afterRead() {
    }

    protected boolean beforeUpdate() throws ModelException {
        return true;
    }

    protected void afterUpdate() throws ModelException {
    }

    protected boolean beforeDelete() {
        return true;
    }

    protected void afterDelete() {
    }

    protected boolean beforeTransferOut() {
        return true;
    }

    protected void afterTransferOut() {
    }

    public void afterSetParent(PropertyRef property) {
    }

    public boolean beforeInsertChildItem(IExplorerItemView childItem) {
        return true;
    }

    public void beforeOpenPropertyObjectEditor(final PropertyObject property, final EntityModel entity) {
    }

    public void afterChangePropertyObject(PropertyObject property) {
    }

    public void onChangeRestriction() {//RADIX-6074
    }
    
    public long getLastReadTime() {
        return lastReadTime;
    }
}