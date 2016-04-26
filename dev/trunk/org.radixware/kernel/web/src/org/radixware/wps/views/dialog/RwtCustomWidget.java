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

package org.radixware.wps.views.dialog;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
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
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.types.ViewRestrictions;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.wps.models.CustomWidgetModel;
import org.radixware.wps.rwt.Container;
import org.radixware.wps.views.ViewSupport;


public abstract class RwtCustomWidget extends Container implements IView, IModelDefinition {

    public interface RwtCustomWidgetListener {

        public void opened(RwtCustomWidget w);
    }
    private ViewSupport<RwtCustomWidget> viewSupport = new ViewSupport<>(this);
    private final ViewRestrictions restrictions = new ViewRestrictions(this);
    private CustomWidgetModel model;
    private IClientEnvironment environment;
    private final List<RwtCustomWidgetListener> listeners = new LinkedList<>();

    public RwtCustomWidget(IClientEnvironment environment) {
        this.environment = environment;
        this.model = createModel(new IContext.CustomWidget(environment));
    }

    @Override
    abstract public Id getId();

    @Override
    public Model getModel() {
        return model;
    }

    @Override
    public IClientEnvironment getEnvironment() {
        return environment;
    }

    @Override
    public void setFocus() {
    }

    @Override
    public CustomWidgetModel createModel(final IContext.Abstract context) {
        if (context == null) {
            throw new NullPointerException("Context must be not null");
        }
        final Model m = createModelImpl(context.getEnvironment());
        m.setContext(context);
        return (CustomWidgetModel) m;
    }

    protected Model createModelImpl(IClientEnvironment environment) {
        final Id modelClassId = Id.Factory.changePrefix(getId(), EDefinitionIdPrefix.ADS_DIALOG_MODEL_CLASS);
        try {
            Class<Model> classModel = getEnvironment().getDefManager().getDefinitionModelClass(modelClassId);
            Constructor<Model> constructor = classModel.getConstructor(IClientEnvironment.class, RwtCustomWidget.class);
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
        return false;
    }

    @Override
    public void finishEdit() {
    }

    @Override
    public boolean setFocusedProperty(Id propertyId) {
        return false;
    }

    @Override
    public void reread() throws ServiceClientException {
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

    public IView createStandardView() {
        return null;
    }

    @Override
    public IView findParentView() {
        return viewSupport.findParentView();
    }

    @Override
    public void visitChildren(IView.Visitor visitor, boolean recursively) {
        //do nothing
    }

    @Override
    public boolean canSafelyClose(final CleanModelController cleanController) {
        return true;
    }        
       
    @Override
    public IWidget getParentWindow() {
        return viewSupport.getParentWindow();
    }

    @Override
    public boolean hasUI() {
        return true;
    }

    @Override
    public boolean isDisabled() {
        return !isEnabled();
    }

    @Override
    public IView createStandardView(IClientEnvironment environment) {
        return null;
    }

    protected void fireOpened() {
        final List<RwtCustomWidgetListener> lss;
        synchronized (listeners) {
            lss = new ArrayList<RwtCustomWidgetListener>(listeners);
        }

        for (RwtCustomWidgetListener l : lss) {
            l.opened(this);
        }
    }

    public void addCustomWidgetListener(RwtCustomWidgetListener l) {
        synchronized (listeners) {
            if (!listeners.contains(l)) {
                listeners.add(l);
            }
        }
    }

    public void removeCustomWidgetListener(RwtCustomWidgetListener l) {
        synchronized (listeners) {
            listeners.remove(l);
        }
    }
}
