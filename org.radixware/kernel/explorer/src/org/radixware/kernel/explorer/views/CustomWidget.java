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

package org.radixware.kernel.explorer.views;

import com.trolltech.qt.gui.QWidget;
import java.lang.reflect.Constructor;
import java.util.Collection;

import java.util.Collections;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.errors.ModelCreationError;
import org.radixware.kernel.common.client.errors.NoDefinitionWithSuchIdError;
import org.radixware.kernel.common.client.meta.IModelDefinition;
import org.radixware.kernel.common.client.meta.RadCommandDef;
import org.radixware.kernel.common.client.meta.RadPropertyDef;
import org.radixware.kernel.common.client.models.CleanModelController;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.types.ViewRestrictions;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.models.CustomWidgetModel;
import org.radixware.kernel.explorer.widgets.ExplorerWidget;
import org.radixware.kernel.explorer.widgets.QWidgetProxy;

public abstract class CustomWidget extends ExplorerWidget implements IExplorerView, IModelDefinition {

    private CustomWidgetModel model;
    final public Signal1<QWidget> opened = new Signal1<>();
    private final ViewRestrictions restrictions;

    public CustomWidget(IClientEnvironment environment, final QWidget owner) {
        super(environment, owner);
        this.model = createModel(new IContext.CustomWidget(environment));
        restrictions = new ViewRestrictions(this);
    }

    @Override
    abstract public Id getId();

    @Override
    public Model getModel() {
        return model;
    }

    @Override
    public CustomWidgetModel createModel(final IContext.Abstract context) {
        if (context == null) {
            throw new NullPointerException("Context must be not null");
        }
        final Model model = createModelImpl(context.getEnvironment());
        model.setContext(context);
        return (CustomWidgetModel) model;
    }

    protected Model createModelImpl(IClientEnvironment environment) {
        final Id modelClassId = Id.Factory.changePrefix(getId(), EDefinitionIdPrefix.ADS_DIALOG_MODEL_CLASS);
        try {
            Class<Model> classModel = getEnvironment().getApplication().getDefManager().getDefinitionModelClass(modelClassId);
            Constructor<Model> constructor = classModel.getConstructor(IClientEnvironment.class, CustomWidget.class);
            return constructor.newInstance(environment, this);
        } catch (Exception ex) {
            throw new ModelCreationError(ModelCreationError.ModelType.CUSTOM_WIDGET_MODEL, this, null, ex);
        }
    }

    @Override
    public void open(Model model) {
        model.setView(this);
    }

    public void open() {
        open(getModel());
    }

    @Override
    public boolean close(boolean forced) {        
        return true;
    }

    @Override
    public void finishEdit() {
        Collection<Property> properties = model.getActiveProperties();
        for (Property property : properties) {
            property.finishEdit();
        }        
    }

    @Override
    public boolean setFocusedProperty(Id propertyId) {
        return false;
    }

    @Override
    public void reread() throws ServiceClientException {
    }

    @Override
    public QWidget asQWidget() {
        return this;
    }

    @Override
    public Id getOwnerClassId() {
        return getId();
    }

    @Override
    public RadPropertyDef getPropertyDefById(Id propertyId) {
        throw new NoDefinitionWithSuchIdError(this, NoDefinitionWithSuchIdError.SubDefinitionType.PROPERTY, propertyId);
    }

    @Override
    public boolean isPropertyDefExistsById(Id id) {
        return false;
    }

    @Override
    public RadCommandDef getCommandDefById(Id commandId) {
        throw new NoDefinitionWithSuchIdError(this, NoDefinitionWithSuchIdError.SubDefinitionType.COMMAND, commandId);
    }

    @Override
    public boolean isCommandDefExistsById(Id id) {
        return false;
    }

    @Override
    public List<RadCommandDef> getEnabledCommands() {
        return Collections.emptyList();
    }

    @Override
    public ViewRestrictions getRestrictions() {
        return restrictions;
    }

    @Override
    public Icon getIcon() {
        return null;
    }

    @Override
    public String getTitle() {
        return null;
    }

    public IExplorerView createStandardView() {
        return null;
    }

    @Override
    public void visitChildren(Visitor visitor, boolean recursively) {
        //do nothing
    }

    @Override
    public boolean canSafelyClose(final CleanModelController cleanController) {
        return true;
    }    

    @Override
    public IView findParentView() {
        return QWidgetProxy.findParentView(asQWidget());
    }

    @Override
    public IWidget getParentWindow() {
        return QWidgetProxy.getParentWindow(asQWidget());
    }

    @Override
    public boolean hasUI() {
        return QWidgetProxy.hasUI(asQWidget());
    }

    @Override
    public boolean isDisabled() {
        return !asQWidget().isEnabled();
    }

    @Override
    public IView createStandardView(IClientEnvironment environment) {
        return null;
    }
}