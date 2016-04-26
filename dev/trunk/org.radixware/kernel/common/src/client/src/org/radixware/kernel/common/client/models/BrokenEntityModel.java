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

import org.radixware.kernel.common.client.meta.explorerItems.RadParentRefExplorerItemDef;
import java.util.Collection;
import java.util.List;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.eas.CommandRequestHandle;
import org.radixware.kernel.common.client.eas.RequestHandle;
import org.radixware.kernel.common.client.errors.UsageOfBrokenEntityError;
import org.radixware.kernel.common.client.exceptions.*;
import org.radixware.kernel.common.client.meta.*;
import org.radixware.kernel.common.client.meta.mask.validators.InvalidValueReason;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.enums.EEntityCreationResult;
import org.radixware.kernel.common.client.models.IContext.Abstract;
import org.radixware.kernel.common.client.models.items.Command;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.models.items.properties.PropertyValue;
import org.radixware.kernel.common.client.types.EntityRestrictions;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.views.IEditor;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.client.widgets.IModelWidget;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.ESelectorRowStyle;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;


public final class BrokenEntityModel extends EntityModel {

    private static class BrokenEntityModelPresentationDef extends RadEditorPresentationDef {

        public BrokenEntityModelPresentationDef(final RadSelectorPresentationDef presentation, final ERuntimeEnvironmentType environmentType) {
            super(Id.Factory.loadFrom("epr_BROKEN_ENTITY_PRESENTATION_"),
                    presentation.getClassPresentation().getName(),
                    environmentType,
                    null,//basePresentationId
                    presentation.getOwnerClassId(),
                    presentation.getTableId(),
                    null,//titleId
                    null,//iconId
                    null,//editorPages
                    null,//pageOrder
                    null,//explorerItems
                    null,//explorerItemsSettings
                    (Id[])null,//contextCommandIds
                    0,//restrictionsMask
                    null,//enabledCommandIds
                    null,//presPropertyAttributes
                    0//inheritanceMask
                );
        }
    }
    private final Pid pid;
    private final String exceptionClass, exceptionMessage, exceptionStack;

    public BrokenEntityModel(final IClientEnvironment environment,
            final RadSelectorPresentationDef selPresentation,
            final org.radixware.schemas.eas.ObjectList.Rows.Item.Exception exception) {
        super(environment, new BrokenEntityModelPresentationDef(selPresentation, environment.getApplication().getRuntimeEnvironmentType()));
        if (exception.getPID() != null && !exception.getPID().isEmpty()) {
            pid = new Pid(selPresentation.getTableId(), exception.getPID());
        } else {
            pid = null;
        }
        exceptionClass = exception.getClass1();
        exceptionMessage = exception.getMessage();
        exceptionStack = exception.getStack();
    }

    public BrokenEntityModel(final IClientEnvironment environment,
            final RadSelectorPresentationDef selPresentation,
            final BrokenEntityObjectException exception) {
        super(environment, new BrokenEntityModelPresentationDef(selPresentation, environment.getApplication().getRuntimeEnvironmentType()));
        this.pid = exception.getPid();
        exceptionClass = exception.getCauseExceptionClassName();
        exceptionMessage = exception.getCauseExceptionMessage();
        exceptionStack = exception.getCauseExceptionStack();
    }

    public BrokenEntityModel(final IClientEnvironment environment,
            final RadSelectorPresentationDef selPresentation,
            final Pid pid,
            final Throwable exception) {
        super(environment, new BrokenEntityModelPresentationDef(selPresentation, environment.getApplication().getRuntimeEnvironmentType()));
        this.pid = pid;
        exceptionClass = exception.getClass().getName();
        exceptionMessage = exception.getMessage();
        exceptionStack = ClientException.exceptionStackToString(exception);
    }

    public String getExceptionClass() {
        return exceptionClass;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public String getExceptionStack() {
        return exceptionStack;
    }

    @Override
    public void activate(String pid_, String objectTitle_, Id classId_, List<PropertyValue> propVals) {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    public void activate(RawEntityModelData rawData) {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    public void activateAllProperties() throws ServiceClientException, InterruptedException {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    public void activateCopy(EntityModel src) {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    protected Property activateProperty(Id id) {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    public boolean askForUpdate() {
        throw new UsageOfBrokenEntityError(this);
    }
    
    @Override
    public boolean canSafelyClean(final CleanModelController controller){
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    public void cancelChanges() {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    public void clean() {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    public EEntityCreationResult create() throws ModelException, ServiceClientException, InterruptedException {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    public boolean delete(boolean forced) throws ServiceClientException, InterruptedException {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    public Collection<Property> getActiveProperties() {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    protected List<Property> getActivePropertiesByOrder() {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    public Id getClassId() {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    public RadClassPresentationDef getClassPresentationDef() {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    public Command getCommand(Id id) {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    public List<Property> getEditedProperties() {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    protected Id getEditorPresentationIdForChooseIntoTree() {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    public List<Id> getAccessibleCommandIds() {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    public IEditor getEntityView() {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    Model getOwner() {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    public Pid getPid() {
        return pid;
    }

    @Override
    protected RadPropertyDef getPropertyDef(Id propertyId) {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    public EntityRestrictions getRestrictions() {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    public ESelectorRowStyle getSelectorRowStyle() {
        return ESelectorRowStyle.VERY_BAD;
    }

    @Override
    public Pid getSrcPid() {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    public String getTitle() {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    public String getWindowTitle() {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    public boolean isCopyOf(Pid src) {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    public boolean isEdited() {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    public boolean isExists() {
        return true;
    }

    @Override
    public boolean isNew() {
        return false;
    }

    @Override
    public EntityModel openChoosenEditModel() {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    public EntityModel openInSelectorEditModel() {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    public EntityModel openParentEditModel(RadParentRefExplorerItemDef explorerItemDef, Model holder) throws ServiceClientException, InterruptedException {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    public void read() throws ServiceClientException, InterruptedException {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    public RequestHandle readAsync(Collection<Id> propertyIds) {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    public void readProperty(Id propId) throws ServiceClientException, InterruptedException {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    public void setContext(Abstract context) {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    public void setIsEdited(boolean edited) {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    public void setSelectorRowStyle(ESelectorRowStyle newStyle) {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    public void showException(String title, Throwable ex) {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    public void startSynchronization(EntityModel model2) {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    public void stopSynchronization() {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    public boolean update() throws ModelException, ServiceClientException, InterruptedException {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    public boolean wasRead() {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    public boolean canUsePropEditorDialog(Id propertyId) {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    protected void checkPropertyValues() throws PropertyIsMandatoryException, InvalidPropertyValueException {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    protected Command createCommand(RadCommandDef def) {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    protected Property createProperty(RadPropertyDef def) {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    public IModelWidget createPropertyEditor(Id propertyId) {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    public IView createView() {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    public void finishEdit() {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    public Model getChildModel(Id explItemId) throws ServiceClientException, InterruptedException {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    public String getConfigStoreGroupName() {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    public Id getCustomViewId() {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    public String getDisplayString(Id propertyId, java.lang.Object propertyValue, String defaultDisplayString, boolean isInherited) {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    public Icon getIcon() {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    protected Collection<Model> getOpenedChildModels() {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    public ValidationResult getPropertyValueState(Id propertyId) {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    public boolean isCommandAccessible(RadCommandDef command) {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    protected void onCommandResponse(Id commandId, XmlObject output, java.lang.Object userData, CommandRequestHandle handler) {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    public void onFinishEditPropertyValue(Property property, boolean valueWasAccepted) {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    protected void onInvalidValue(Id propertyId, InvalidValueReason reason) throws InvalidPropertyValueException {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    public void onStartEditPropertyValue(Property property) {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    public CommandRequestHandle sendCommandAsync(Id commandId, Id propertyId, XmlObject input, Class<? extends XmlObject> expectedOutputClass, java.lang.Object userData) {
        throw new UsageOfBrokenEntityError(this);
    }
    
    @Override
    public CommandRequestHandle sendCommandAsync(Id commandId, Id propertyId, XmlObject input, Class<? extends XmlObject> expectedOutputClass, java.lang.Object userData, int timeoutSec) {
        throw new UsageOfBrokenEntityError(this);
    }    

    @Override
    public void setIcon(Icon icon) {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    public void setTitle(String newTitle) {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    public void setView(IView view_) {
        throw new UsageOfBrokenEntityError(this);
    }

    @Override
    public void showException(Throwable ex) {
        throw new UsageOfBrokenEntityError(this);
    }
}
