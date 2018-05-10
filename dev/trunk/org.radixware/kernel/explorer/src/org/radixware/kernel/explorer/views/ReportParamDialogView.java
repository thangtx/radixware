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

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QAbstractButton;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QDialogButtonBox;
import com.trolltech.qt.gui.QWidget;
import java.util.Collection;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.models.CleanModelController;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.ReportParamDialogModel;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.types.ViewRestrictions;
import org.radixware.kernel.common.client.views.AbstractViewController;
import org.radixware.kernel.common.client.views.IEmbeddedView;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Id;

import org.radixware.kernel.explorer.env.Application;

import org.radixware.kernel.common.client.views.IReportParamDialogView;
import org.radixware.kernel.explorer.dialogs.DialogSizeManager;
import org.radixware.kernel.explorer.dialogs.QtDialog;
import org.radixware.kernel.explorer.env.ExplorerSettings;
import org.radixware.kernel.explorer.env.progress.ExplorerProgressHandleManager;
import org.radixware.kernel.explorer.utils.WidgetUtils;
import org.radixware.kernel.explorer.widgets.QWidgetProxy;


public class ReportParamDialogView extends QtDialog implements IExplorerView, IReportParamDialogView {

    protected ReportParamDialogModel report;
    final public Signal1<QWidget> opened = new Signal1<>();
    final public Signal0 closed = new Signal0();
    private boolean forcedClose, wasClosed;
    private DialogSizeManager sizeManager;
    private final IClientEnvironment environment;    
    private final AbstractViewController controller;

    protected ReportParamDialogView(IClientEnvironment environment) {
        super();        
        this.environment = environment;
        controller = new AbstractViewController(environment,this){
            @Override
            protected List<IEmbeddedView> findChildrenViews() {
                return WidgetUtils.findExplorerViews(ReportParamDialogView.this);
            }            
        };
        setContextMenuPolicy(Qt.ContextMenuPolicy.NoContextMenu);
    }

    public final ReportParamDialogModel getReportModel() {
        return report;
    }

    @Override
    public boolean isUpdatesEnabled() {
        return updatesEnabled();
    }

    @Override
    public void open(final Model model_) {
        final Model ownerModel = ((IContext.Report) model_.getContext()).getOwnerModel();
        if (ownerModel != null && ownerModel.getView() != null) {
            setParent((QWidget) ownerModel.getView());
        } else if (ownerModel != null && ownerModel.getContext() instanceof IContext.SelectorRow) {
            final IContext.SelectorRow context = (IContext.SelectorRow) ownerModel.getContext();
            if (context.parentGroupModel.getGroupView() != null) {
                setParent(QWidgetProxy.getWindow(context.parentGroupModel.getGroupView()));
            } else {
                setParent(Application.getMainWindow());
            }
        } else {
            setParent(Application.getMainWindow());
        }

        setWindowFlags(WidgetUtils.WINDOW_FLAGS_FOR_DIALOG);
        setModal(true);
        model_.setView(this);
        report = (ReportParamDialogModel) model_;
        final String configPrefix = model_.getConfigStoreGroupName()+"/"+ SettingNames.SYSTEM;
        sizeManager = new DialogSizeManager((ExplorerSettings)environment.getConfigStore(), this, configPrefix);
        sizeManager.setGeometryKey("/dialogGeometry");        
        opened.connect(this, "restoreGeometry()");
        setWindowTitle(model_.getWindowTitle());
        setWindowIcon(model_.getIcon());
        setObjectName("rx_report_params_view_#"+model_.getDefinition().getId());
    }

    @SuppressWarnings("unused")
    private void restoreGeometry() {
        if (sizeManager!=null){
            final int sizeX = report.getReportPresentationDef().getDefaultWidth();
            final int sizeY = report.getReportPresentationDef().getDefaultHeight();
            sizeManager.loadGeometry(sizeX, sizeY);
        }
    }
    
    @Override
    protected void closeEvent(QCloseEvent event) {
        final ExplorerProgressHandleManager manager = 
            (ExplorerProgressHandleManager)environment.getProgressHandleManager();
        if (manager.getActive()!=null && !manager.isProgressBlocked()) {
            event.ignore();
        } else {
            super.closeEvent(event);
        }
    }

    @Override
    public boolean close(final boolean forced) {
        if (wasClosed) {
            return true;
        } else if ((forced || report.canSafelyClean(CleanModelController.DEFAULT_INSTANCE)) && (report.beforeCloseDialog(DialogResult.REJECTED) || forced)) {
            forcedClose = true;//do not call canSafelyClean() next time
            reject();
            return true;
        }
        return false;
    }

    @Override
    public boolean canSafelyClose(final CleanModelController cleanController) {
        return controller.canSafelyClose(this, cleanController);
    }        

    @Override
    public void done(int result) {
        if (report == null || wasClosed) {
            super.done(result);
        } else {
            report.finishEdit();
            if (forcedClose || (report.canSafelyClean(CleanModelController.DEFAULT_INSTANCE) && report.beforeCloseDialog(DialogResult.getForValue(result)))) {
                try {
                    if (result == QDialog.DialogCode.Accepted.value() && !report.acceptParameters()) {
                        return;
                    }
                } catch (Exception ex) {
                    report.showException(ex);
                    return;
                }
                setResult(result);
                closed.emit();
                wasClosed = true;
                report.setView(null);
                controller.afterCloseView();
                if (sizeManager!=null){
                    sizeManager.saveGeometry();
                }                
                WidgetUtils.closeChildrenEmbeddedViews(report, this);
                super.done(result);
            }
        }
        super.done(result);
    }

    public void setupButtonBox(final QDialogButtonBox buttonBox) {
        buttonBox.clear();
        final ArrStr buttons = getReportModel().getButtons();
        if (buttons != null) {
            QAbstractButton button;
            for (String buttonType : buttons) {
                button = (QAbstractButton) getReportModel().createButton(buttonType);
                button.setObjectName(buttonType);
                buttonBox.addButton(button, QDialogButtonBox.ButtonRole.NoRole);
            }
        }
    }

    public QAbstractButton findButtonByType(final QDialogButtonBox findIn, final String buttonType) {
        final List<QAbstractButton> buttons = findIn.buttons();
        for (QAbstractButton button : buttons) {
            if (buttonType.equals(button.objectName())) {
                return button;
            }
        }
        return null;
    }

    @Override
    public Model getModel() {
        return report;
    }

    @Override
    public void finishEdit() {
        final Collection<Property> properties = report.getActiveProperties();
        for (Property property : properties) {
            property.finishEdit();
        }
    }

    @Override
    public boolean setFocusedProperty(final Id propertyId) {
        return false;
    }

    @Override
    public void reread() throws ServiceClientException {
    }

    @Override
    public int exec() {
        try {
            forcedClose = false;
            wasClosed = false;
            return super.exec();
        } finally {
            opened.disconnect();
            closed.disconnect();
        }
    }

    @Override
    public QWidget asQWidget() {
        return this;
    }

    @Override
    public IView findParentView() {
        return QWidgetProxy.findParentView(this);
    }

    @Override
    public IWidget getParentWindow() {
        return QWidgetProxy.getParentWindow(this);
    }

    @Override
    public boolean hasUI() {
        return asQWidget() != null && asQWidget().nativePointer() != null;
    }

    @Override
    public boolean isDisabled() {
        return !asQWidget().isEnabled();
    }

    @Override
    public void visitChildren(Visitor visitor, boolean recursively) {
        controller.visitChildren(visitor, recursively);
    }    

    @Override
    public ViewRestrictions getRestrictions() {
       return controller.getViewRestrictions();
    }        

    @Override
    public IClientEnvironment getEnvironment() {
        return environment;
    }
}
