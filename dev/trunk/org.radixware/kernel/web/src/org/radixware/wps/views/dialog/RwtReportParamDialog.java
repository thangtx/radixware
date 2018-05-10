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

import java.util.Collection;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.exceptions.InvalidPropertyValueException;
import org.radixware.kernel.common.client.exceptions.PropertyIsMandatoryException;
import org.radixware.kernel.common.client.models.CleanModelController;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.ReportParamDialogModel;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.types.ViewRestrictions;
import org.radixware.kernel.common.client.views.AbstractViewController;
import org.radixware.kernel.common.client.views.IEmbeddedView;
import org.radixware.kernel.common.client.views.IReportParamDialogView;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.Dialog;
import org.radixware.wps.views.ViewSupport;

public class RwtReportParamDialog extends Dialog implements IReportParamDialogView {

    private final ViewSupport<RwtReportParamDialog> viewSupport = new ViewSupport<>(this);
    private final IClientEnvironment environment;
    private final AbstractViewController controller;
    private ReportParamDialogModel model;
    private boolean forcedClose;
    protected final FormEventSupport eventSupport = new FormEventSupport(this);

    public RwtReportParamDialog(final IClientEnvironment environment) {
        super(((WpsEnvironment) environment).getDialogDisplayer(), "");
        this.environment = environment;
        controller = new AbstractViewController(environment, this) {
            @Override
            protected List<IEmbeddedView> findChildrenViews() {
                return viewSupport.findEmbeddedViews();
            }
        };
    }

    @Override
    public boolean isUpdatesEnabled() {
        return true;
    }

    public ReportParamDialogModel getReportModel() {
        return model;
    }

    @Override
    public void setUpdatesEnabled(final boolean enabled) {
    }

    @Override
    public void open(final Model model) {
        this.model = (ReportParamDialogModel) model;
        setWindowTitle(model.getWindowTitle());
        setWindowIcon(model.getIcon());
        model.setView(this);
        if (this.model.getReportPresentationDef().getDefaultWidth() > 0) {
            setWidth(this.model.getReportPresentationDef().getDefaultWidth());
        }
        if (this.model.getReportPresentationDef().getDefaultHeight() > 0) {
            setHeight(this.model.getReportPresentationDef().getDefaultHeight());
        }
        setObjectName("rx_report_param_view_#"+model.getDefinition().getId());
    }

    @Override
    public void setFocus() {
    }

    @Override
    public boolean setFocusedProperty(final Id propertyId) {
        return false;
    }

    @Override
    public boolean close(final boolean forced) {
        if ((!forced && !model.canSafelyClean(CleanModelController.DEFAULT_INSTANCE)) || (!model.beforeCloseDialog(DialogResult.REJECTED) && !forced)) {
            return false;
        }
        forcedClose = true;
        fireClosed();
        model.setView(null);
        controller.afterCloseView();
        //reject
        return true;
    }

    @Override
    public boolean canSafelyClose(final CleanModelController cleanController) {
        return controller.canSafelyClose(this, cleanController);
    }

    @Override
    public void finishEdit() {
        final Collection<Property> properties = model.getActiveProperties();
        for (Property property : properties) {
            property.finishEdit();
        }
    }

    public void removeFormListener(IFormListener l) {
        eventSupport.removeFormListener(l);
    }

    public void addFormListener(IFormListener l) {
        eventSupport.addFormListener(l);
    }

    protected void fireOpened() {
        eventSupport.opened();
    }

    protected void fireClosed() {
        eventSupport.closed();
    }

    @Override
    public Model getModel() {
        return model;
    }

    @Override
    public void reread() throws ServiceClientException {
    }

    @Override
    public boolean hasUI() {
        return true;
    }

    @Override
    public IWidget getParentWindow() {
        return viewSupport.getParentWindow();
    }

    @Override
    public IView findParentView() {
        return viewSupport.findParentView();
    }

    @Override
    public void visitChildren(IView.Visitor visitor, boolean recursively) {
        controller.visitChildren(visitor, recursively);
    }

    @Override
    public ViewRestrictions getRestrictions() {
        return controller.getViewRestrictions();
    }

    @Override
    public boolean isDisabled() {
        return !isEnabled();
    }

    @Override
    public IClientEnvironment getEnvironment() {
        return environment;
    }

    @Override
    protected DialogResult onClose(final String action, final DialogResult actionResult) {
        if (model != null) {
            if (!forcedClose) {
                model.finishEdit();
                if (!model.canSafelyClean(CleanModelController.DEFAULT_INSTANCE) || !model.beforeCloseDialog(actionResult)) {
                    return null;
                }
            }
            if (actionResult == DialogResult.ACCEPTED) {
                try {
                    if (!model.acceptParameters()) {
                        return null;
                    }
                } catch (PropertyIsMandatoryException | InvalidPropertyValueException ex) {
                    model.showException(ex);
                    return null;
                }
            }
        }
        return super.onClose(action, actionResult);
    }
}
