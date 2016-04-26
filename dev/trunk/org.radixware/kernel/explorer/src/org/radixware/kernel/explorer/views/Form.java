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

import com.trolltech.qt.gui.QCloseEvent;
import java.util.Collection;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QAbstractButton;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QWidget;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.models.CleanModelController;
import org.radixware.kernel.common.client.models.FormModel;
import org.radixware.kernel.common.client.models.FormModel.FormResult;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.tree.IExplorerTree;
import org.radixware.kernel.common.client.types.ViewRestrictions;
import org.radixware.kernel.common.client.views.AbstractViewController;
import org.radixware.kernel.common.client.views.IEmbeddedView;
import org.radixware.kernel.common.client.views.IFormView;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.client.widgets.IPushButton;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.dialogs.DialogSizeManager;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.dialogs.QtDialog;
import org.radixware.kernel.explorer.env.ExplorerSettings;
import org.radixware.kernel.explorer.utils.WidgetUtils;
import org.radixware.kernel.explorer.widgets.ExplorerDialogButtonBox;
import org.radixware.kernel.explorer.widgets.QWidgetProxy;

/**
 * Базовый класс для стандартного редактора (StandardForm) и кастомных
 * редакторов созданных DAC-дизайнером.
 *
 *
 */
public abstract class Form extends QtDialog implements IExplorerView, IFormView {

    private FormModel formModel;
    private final IClientEnvironment environment;
    final public Signal1<QWidget> opened = new Signal1<>();
    final public Signal0 closed = new Signal0();
    private FormEventSupport eventSupport = new FormEventSupport(this);
    private boolean canSafelyClean;
    private DialogSizeManager sizeManager;
    final private AbstractViewController controller;

    protected Form(final IClientEnvironment environment) {
        super();
        this.environment = environment;
        controller = new AbstractViewController(environment, this) {
            @Override
            protected List<IEmbeddedView> findChildrenViews() {
                return WidgetUtils.findExplorerViews(Form.this);
            }
        };
        setContextMenuPolicy(Qt.ContextMenuPolicy.NoContextMenu);
    }

    @Override
    public Model getModel() {
        return formModel;
    }

    public FormModel getFormModel() {
        return formModel;
    }

    @Override
    public ViewRestrictions getRestrictions() {
        return controller.getViewRestrictions();
    }

    @Override
    public void open(FormModel model) {
        open((Model) model);
    }

    @Override
    public void open(Model model) {
        Model m = null;
        if (model.getContext() instanceof IContext.Form) {
            final IContext.Form formContext = (IContext.Form) model.getContext();
            if (formContext != null && formContext.startCommand != null) {
                m = formContext.startCommand.getOwner();
            }
        }
        QWidget parent = null;
        if (m != null) {
            parent = (QWidget) m.getView();
            if (parent==null && m.getContext() instanceof IContext.SelectorRow) {
                IContext.SelectorRow context = (IContext.SelectorRow) m.getContext();
                parent = (QWidget)context.parentGroupModel.getGroupView();
            }
        }
        if (parent==null){
            if (getEnvironment().getTreeManager()!=null &&
                getEnvironment().getTreeManager().getCurrentTree()!=null){
                final IExplorerTree tree = getEnvironment().getTreeManager().getCurrentTree();
                if (tree.getCurrent()!=null && 
                    tree.getCurrent().isValid() &&
                    tree.getCurrent().getView()!=null &&
                    tree.getCurrent().getView().getModel()!=null &&
                    tree.getCurrent().getView().getModel().getView()!=null
                    ){
                    parent = (QWidget)tree.getCurrent().getView().getModel().getView();
                }
                if (parent==null && tree instanceof QWidget){
                    parent = (QWidget)tree;
                }
            }            
        }
        if (parent==null){
            parent = Application.getMainWindow();
        }
        setParent(parent);

        setWindowFlags(WidgetUtils.WINDOW_FLAGS_FOR_DIALOG);
        setModal(true);
        model.setView(this);
        formModel = (FormModel) model;
        final String configPrefix = model.getConfigStoreGroupName()+"/"+ SettingNames.SYSTEM;
        sizeManager = new DialogSizeManager((ExplorerSettings)environment.getConfigStore(), this, configPrefix);
        sizeManager.setGeometryKey("/dialogGeometry");
        opened.connect(this, "restoreGeometry()");
        setWindowTitle(model.getWindowTitle());
        setWindowIcon(model.getIcon());
    }

    @SuppressWarnings("unused")
    private void restoreGeometry() {
        if (sizeManager!=null){
            final int sizeX = formModel.getFormDef().getDefaultWidth();
            final int sizeY = formModel.getFormDef().getDefaultHeight();
            sizeManager.loadGeometry(sizeX, sizeY);
        }
    }
    
    @Override
    public boolean close(boolean forced) {
        if (hasUI()) {            
            if (formModel==null){//call close in beforeOpenView handler
                canSafelyClean = true;
                close();
                return true;            
            }
            if ((!forced && !formModel.canSafelyClean(CleanModelController.DEFAULT_INSTANCE)) || (!formModel.beforeCloseDialog(DialogResult.REJECTED) && !forced)) {
                return false;
            }
            canSafelyClean = true;
            close();
        }
        return true;
    }

    @Override
    public boolean canSafelyClose(final CleanModelController cleanController) {
        return controller.canSafelyClose(this, cleanController);
    }

    @Override
    protected void closeEvent(QCloseEvent event) {
        if (environment.getProgressHandleManager().getActive() != null) {
            event.ignore();
        } else {
            super.closeEvent(event);
        }
    }

    @Override
    public void finishEdit() {
        Collection<Property> properties = formModel.getActiveProperties();
        for (Property property : properties) {
            property.finishEdit();
        }
    }

    @Override
    public boolean setFocusedProperty(final Id propertyId) {
        return false;
    }

    @Override
    public boolean submit() {
        try {
            formModel.finishEdit();
            if (sizeManager!=null){
                sizeManager.saveGeometry();
            }
            if (formModel.submit()) {
                if (hasUI()) {
                    setResult(QDialog.DialogCode.Accepted.value());
                }
                return true;
            }
        } catch (InterruptedException ex) {
            return false;
        } catch (Exception ex) {
            formModel.showException(ex);
        }
        return false;
    }

    public final boolean returnToPreviousForm() {
        if (sizeManager!=null){
            sizeManager.saveGeometry();
        }
        return formModel.returnToPreviousForm();
    }

    @Override
    public void reread() throws ServiceClientException {
    }

    public void setupButtonBox(final ExplorerDialogButtonBox buttonBox) {//DBP-1671
        buttonBox.clear();
        final ArrStr buttons = getFormModel().getButtons();
        if (buttons != null) {
            IPushButton button;
            for (String buttonType : buttons) {
                button = buttonBox.addButton(buttonType);
                getFormModel().setupButton(button, buttonType);
                button.setObjectName(buttonType);
            }
        }
    }

    public QAbstractButton findButtonByType(final ExplorerDialogButtonBox findIn, final String buttonType) {
        return (QAbstractButton) findIn.getButton(buttonType);
    }

    @Override
    public int exec() {
        try {
            return super.exec();
        } finally {
            opened.disconnect();
            closed.disconnect();
        }
    }

    public void doneImpl(int result) {
        if (hasUI()) {//not closet yet
            if (formModel == null) {
                super.done(result);
            } else {
                closed.emit();
                if (isVisible() && sizeManager!=null) {
                    sizeManager.saveGeometry();                    
                }
                formModel.setView(null);
                controller.afterCloseView();
                WidgetUtils.closeChildrenEmbeddedViews(formModel, this);
                WidgetUtils.closeChildrenWidgets(this);
                super.done(result);
            }
        }
    }

    @Override
    public void done(int result) {        
        switch (result) {
            case 0:
                done(FormResult.CANCEL);
                break;
            case 2:
                done(FormResult.PREVIOUS);
                break;
            case 1:
                done(FormResult.NEXT);
                break;
        }        
    }

    @Override
    public QWidget asQWidget() {
        return this;
    }

    @Override
    public boolean isDisabled() {
        return !asQWidget().isEnabled();
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
        return asQWidget() != null && asQWidget().nativePointer() != null;
    }
    private FormResult formResult = FormResult.CANCEL;

    @Override
    public void done(final FormResult result) {        
        if (formModel == null || canSafelyClean || (formModel.canSafelyClean(CleanModelController.DEFAULT_INSTANCE) && formModel.beforeCloseDialog(result.getDialogResult()))) {
            formResult = result;
            this.doneImpl(result.value);
        }
    }

    @Override
    public FormResult formResult() {
        return formResult;
    }

    @Override
    public void visitChildren(Visitor visitor, boolean recursively) {
        controller.visitChildren(visitor, recursively);
    }

    @Override
    public boolean isUpdatesEnabled() {
        return updatesEnabled();
    }

    @Override
    public IClientEnvironment getEnvironment() {
        return environment;
    }

    @Override
    public void removeFormListener(IFormListener l) {
        eventSupport.removeFormListener(l);
    }

    @Override
    public void addFormListener(IFormListener l) {
        eventSupport.addFormListener(l);
    }
}