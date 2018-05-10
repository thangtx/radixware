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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.models.CleanModelController;
import org.radixware.kernel.common.client.models.FormModel;
import org.radixware.kernel.common.client.models.FormModel.FormResult;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.types.ViewRestrictions;
import org.radixware.kernel.common.client.views.AbstractViewController;
import org.radixware.kernel.common.client.views.IEmbeddedView;
import org.radixware.kernel.common.client.views.IFormView;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.client.widgets.IPushButton;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Id;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.Dialog;
import org.radixware.wps.views.ViewSupport;


public abstract class RwtForm extends Dialog implements IFormView {

    public static class RwtFormListener implements IFormListener {

        @Override
        public void opened(final IFormView form) {
        }

        @Override
        public void closed(final IFormView form) {
        }
    }
    private final ViewSupport<RwtForm> viewSupport = new ViewSupport<>(this);
    private IClientEnvironment environment;
    private FormModel form;
    private boolean canSafelyClean;
    private FormResult formResult;
    protected final FormEventSupport eventSupport = new FormEventSupport(this);
    private final AbstractViewController controller;

    protected RwtForm(final IClientEnvironment environment) {
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
    public Model getModel() {
        return form;
    }

    public FormModel getFormModel() {
        return form;
    }

    @Override
    public void open(final FormModel model) {
        open((Model) model);
    }

    @Override
    public void open(final Model model) {
        html.setAttr("dlgId", model.getDefinition().getId().toString());
        model.setView(this);
        form = (FormModel) model;
        html.setAttr("dlgId", "ee" + form.getFormDef().getId().toString());
        setWindowTitle(model.getWindowTitle());
        setWindowIcon(model.getIcon());
        if (form.getFormDef().getDefaultWidth() > 0) {
            setWidth(form.getFormDef().getDefaultWidth());
        }
        if (form.getFormDef().getDefaultHeight() > 0) {
            setHeight(form.getFormDef().getDefaultHeight());
        }
        setObjectName("rx_form_view_#"+model.getDefinition().getId());
    }

    @Override
    public boolean submit() {
        try {
            form.finishEdit();
            //DialogGeometryStorage.writeDialogGeometry(form, saveGeometry());
            if (form.submit()) {
                setResult(FormResult.NEXT);
                return true;
            }
        } catch (InterruptedException ex) {
            return false;
        } catch (Exception ex) {
            form.showException(ex);
        }
        return false;
    }

    private void setResult(final FormResult result) {
        this.formResult = result;
    }

    @Override
    public boolean isUpdatesEnabled() {
        return true;
    }

    @Override
    public void setUpdatesEnabled(final boolean enable) {
    }

    @Override
    public void hide() {
        setVisible(false);
    }

    @Override
    public void show() {
        setVisible(true);
    }

    @Override
    public void done(FormResult result) {
        if (result == null) {
            result = FormResult.CANCEL;
        }
        setResult(result);
        switch (result) {
            case CANCEL:
                close(DialogResult.REJECTED);
                break;
            case NEXT:
                close(DialogResult.ACCEPTED);
                break;
            case PREVIOUS:
                close(DialogResult.ACCEPTED);
                break;
        }
    }

    @Override
    public void close(final DialogResult result) {
        if (form == null || canSafelyClean || (form.canSafelyClean(CleanModelController.DEFAULT_INSTANCE) && form.beforeCloseDialog(result))) {
            super.close(result);
        }
    }

    @Override
    public boolean close() {
        rejectDialog();
        return true;
    }

    @Override
    public FormResult formResult() {
        return formResult;
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
        if (form==null){//call close in beforeOpenView handler
            canSafelyClean = true;
            close();
            return true;            
        }
        if ((!forced && !form.canSafelyClean(CleanModelController.DEFAULT_INSTANCE)) || (!form.beforeCloseDialog(DialogResult.REJECTED) && !forced)) {
            return false;
        }
        canSafelyClean = true;
        fireClosed();
        form.setView(null);
        controller.afterCloseView();
        close();
        return true;
    }

    @Override
    public boolean canSafelyClose(final CleanModelController cleanController) {
        return controller.canSafelyClose(this, cleanController);
    }

    @Override
    public void finishEdit() {
        Collection<Property> properties = form.getActiveProperties();
        for (Property property : properties) {
            property.finishEdit();
        }
    }

    @Override
    public ViewRestrictions getRestrictions() {
        return controller.getViewRestrictions();
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
    public void visitChildren(IView.Visitor visitor, boolean recursively) {
        controller.visitChildren(visitor, recursively);
    }

    @Override
    public IView findParentView() {
        return viewSupport.findParentView();
    }

    @Override
    public boolean isDisabled() {
        return !isEnabled();
    }

    @Override
    public IClientEnvironment getEnvironment() {
        return environment;
    }

    public final void setupButtons() {
        clearCloseActions();
        final ArrStr buttons = getFormModel().getButtons();
        if (buttons != null) {
            Set<EDialogButtonType> buttonTypes = new HashSet<>();
            for (String buttonType : buttons) {
                final IPushButton button =
                        environment.getApplication().getWidgetFactory().newPushButton();
                getFormModel().setupButton(button, buttonType);
                button.setObjectName(buttonType);
                try {
                    EDialogButtonType bt = EDialogButtonType.getForValue(buttonType);
                    addCloseAction(button, DialogResult.NONE, null, bt);
                } catch (NoConstItemWithSuchValueError error) {
                    addCloseAction(button, DialogResult.NONE, null, buttonType);
                }
            }
            setupDefaultButtons(buttonTypes);
        }
    }

    @Override
    public void removeFormListener(IFormListener l) {
        eventSupport.removeFormListener(l);
    }

    @Override
    public void addFormListener(IFormListener l) {
        eventSupport.addFormListener(l);
    }

    protected void fireOpened() {
        eventSupport.opened();
    }

    protected void fireClosed() {
        eventSupport.closed();
    }
}
