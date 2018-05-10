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

import com.trolltech.qt.core.Qt.WidgetAttribute;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.trolltech.qt.core.Qt.WindowFlags;
import com.trolltech.qt.core.Qt.WindowType;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QWidget;
import java.util.Collections;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.errors.ModelCreationError;
import org.radixware.kernel.common.client.errors.NoDefinitionWithSuchIdError;
import org.radixware.kernel.common.client.meta.IModelDefinition;
import org.radixware.kernel.common.client.meta.RadCommandDef;
import org.radixware.kernel.common.client.meta.RadPropertyDef;
import org.radixware.kernel.common.client.models.CleanModelController;
import org.radixware.kernel.common.client.models.DialogModel;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.types.ViewRestrictions;
import org.radixware.kernel.common.client.views.AbstractViewController;
import org.radixware.kernel.common.client.views.ICustomDialog;
import org.radixware.kernel.common.client.views.IEmbeddedView;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.dialogs.DialogSizeManager;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.dialogs.QtDialog;
import org.radixware.kernel.explorer.env.ExplorerSettings;
import org.radixware.kernel.explorer.env.progress.ExplorerProgressHandleManager;
import org.radixware.kernel.explorer.utils.LeakedWidgetsDetector;
import org.radixware.kernel.explorer.utils.WidgetUtils;
import org.radixware.kernel.explorer.widgets.QWidgetProxy;

public abstract class Dialog extends QtDialog implements IModelDefinition, IExplorerView, ICustomDialog {

    protected RadCommandDef[] commands;	//Заполняется в кострукторе наследника
    private final Id id;
    private final Id iconId;
    private Icon icon;
    private Map<Id, RadCommandDef> commandsById = null;
    final public Signal1<QWidget> opened = new Signal1<>();
    final public Signal0 closed = new Signal0();
    protected final Id titleId;
    protected String title;
    private boolean forcedClose, wasClosed;
    final private AbstractViewController controller;
    private final IClientEnvironment environment;
    private final DialogSizeManager sizeManager;
    private boolean wasOpened = false;

    public Dialog(final IClientEnvironment environment, final Id id, final Id titleId, final Id iconId) {
        super();
        this.environment = environment;
        controller = new AbstractViewController(environment, this) {
            @Override
            protected List<IEmbeddedView> findChildrenViews() {
                return WidgetUtils.findExplorerViews(Dialog.this);
            }
        };        
        setWindowFlags(WidgetUtils.WINDOW_FLAGS_FOR_DIALOG);
        setAttribute(WidgetAttribute.WA_DeleteOnClose, true);
        this.id = id;
        this.titleId = titleId;
        this.iconId = iconId;
        if (commands != null && commands.length > 0) {
            commandsById = new HashMap<>(commands.length * 2);
            for (RadCommandDef command : commands) {
                commandsById.put(command.getId(), command);
            }
        } else {
            commandsById = new HashMap<>(0);
        }
        model = createModel(new IContext.Dialog(environment));
        final String configPrefix = model.getConfigStoreGroupName()+"/"+ SettingNames.SYSTEM;
        sizeManager = new DialogSizeManager((ExplorerSettings)environment.getConfigStore(), this, configPrefix);
        sizeManager.setGeometryKey("dialogGeometry");
        setObjectName("rx_dlg_view_#"+id.toString());
    }

    @Override
    public IClientEnvironment getEnvironment() {
        return environment;
    }

    @Override
    public int exec() {
        if (parentWidget() == null) {
            setParent(Application.getMainWindow());
        }
        final WindowFlags f = new WindowFlags();
        f.set(new WindowType[]{WindowType.Dialog, WindowType.WindowSystemMenuHint});
        setWindowFlags(f);
        forcedClose = false;
        wasClosed = false;
        if (!wasOpened) {
            open(model);
        }
        sizeManager.loadGeometry(0, 0);
        if (wasClosed || model.getView() == null) { //was closed on open        
            return result();
        }
        setModal(true);
        LeakedWidgetsDetector.getInstance().beforeExecDialog(environment, this);
        try{
            return super.exec();            
        }finally{
            LeakedWidgetsDetector.getInstance().afterExecDialog(environment);
        }
    }    //   Реализация View
    private DialogModel model;

    @Override
    public Model getModel() {
        return model;
    }

    @Override
    public void open(Model model_) {
        model.setView(this);
        opened.connect(this, "restoreGeometry()");
        setWindowTitle(model.getWindowTitle());
        setWindowIcon(model.getIcon());        
        wasOpened = true;
    }

    @SuppressWarnings("unused")
    private void restoreGeometry() {
        sizeManager.loadGeometry(0, 0);
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
    public void done(int result) {
        if (model == null) {
            super.done(result);
        } else if (forcedClose || (model.canSafelyClean(CleanModelController.DEFAULT_INSTANCE) && model.beforeCloseDialog(DialogResult.getForValue(result)))) {
            closed.emit();
            wasClosed = true;
            sizeManager.saveGeometry();            
            model.afterCloseDialog();
            controller.afterCloseView();
            WidgetUtils.closeChildrenEmbeddedViews(model, this);
            WidgetUtils.closeChildrenWidgets(this);
            super.done(result);
        }
    }

    @Override
    public boolean close(boolean forced) {
        if (wasClosed) {
            return true;
        } else if ((forced || model.canSafelyClean(CleanModelController.DEFAULT_INSTANCE)) && (model.beforeCloseDialog(DialogResult.REJECTED) || forced)) {
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
    public boolean setFocusedProperty(Id propertyId) {
        return false;
    }

    @Override
    public void finishEdit() {
        Collection<Property> properties = model.getActiveProperties();
        for (Property property : properties) {
            property.finishEdit();
        }
    }

    // Реализация ModelDefinition
    @Override
    public IView createStandardView(IClientEnvironment environment) {
        return null;
    }

    @Override
    public DialogModel createModel(final IContext.Abstract context) {
        if (context == null) {
            throw new NullPointerException("Context must be not null");
        }
        final Model model = createModelImpl(context.getEnvironment());
        model.setContext(context);
        return (DialogModel) model;
    }

    protected Model createModelImpl(IClientEnvironment environment) {
        final Id modelClassId = Id.Factory.changePrefix(id, EDefinitionIdPrefix.ADS_DIALOG_MODEL_CLASS);

        try {
            Class<Model> classModel = getEnvironment().getApplication().getDefManager().getDefinitionModelClass(modelClassId);
            Constructor<Model> constructor = classModel.getConstructor(IClientEnvironment.class, ICustomDialog.class);
            constructor.setAccessible(true);
            return constructor.newInstance(environment, this);
        } catch (Exception e) {
            throw new ModelCreationError(ModelCreationError.ModelType.DIALOG_MODEL, this, null, e);
        }
    }

    @Override
    public RadCommandDef getCommandDefById(Id commandId) {
        if (commandsById.containsKey(commandId)) {
            return commandsById.get(commandId);
        }
        throw new NoDefinitionWithSuchIdError(this, NoDefinitionWithSuchIdError.SubDefinitionType.COMMAND, commandId);
    }

    @Override
    public boolean isCommandDefExistsById(Id commandId) {
        return commandsById.containsKey(commandId);
    }

    @Override
    public RadPropertyDef getPropertyDefById(Id propertyId) {
        throw new NoDefinitionWithSuchIdError(this, NoDefinitionWithSuchIdError.SubDefinitionType.PROPERTY, propertyId);
    }

    @Override
    public boolean isPropertyDefExistsById(Id propertyId) {
        return false;
    }

    @Override
    public ViewRestrictions getRestrictions() {
        return controller.getViewRestrictions();
    }

    @Override
    public Id getOwnerClassId() {
        return id;
    }

    @Override
    public String getTitle() {
        if (title == null) {
            if (titleId != null) {
                try {
                    title = getEnvironment().getApplication().getDefManager().getMlStringValue(id, titleId);
                } catch (DefinitionError err) {
                    final String mess = getEnvironment().getMessageProvider().translate("TraceMessage", "Cannot get title #%s for custom dialog %s");
                    getEnvironment().getTracer().error(String.format(mess, titleId, toString()), err);
                }
            }
            if (title == null) {
                title = getEnvironment().getMessageProvider().translate("ExplorerDialog", "<No Title>");
            }
        }
        return title;
    }

    @Override
    public Icon getIcon() {
        if (icon == null && iconId != null) {
            try {
                icon = getEnvironment().getApplication().getDefManager().getImage(iconId);
            } catch (DefinitionError err) {
                final String mess = getEnvironment().getMessageProvider().translate("TraceMessage", "Cannot get icon #%s for custom dialog %s");
                getEnvironment().getTracer().error(String.format(mess, iconId, toString()), err);
            }
        }
        return icon;
    }

    @Override
    public Id getId() {
        return id;
    }

    @Override
    public String toString() {
        if (windowTitle() != null) {
            return "#" + id + " with title \"" + windowTitle() + "\"";
        } else {
            return "#" + id;
        }
    }

    @Override
    public void reread() throws ServiceClientException {
        return;
    }

    @Override
    public QWidget asQWidget() {
        return this;
    }

    @Override
    public List<RadCommandDef> getEnabledCommands() {
        return Collections.emptyList();
    }

    @Override
    public void visitChildren(Visitor visitor, boolean recursively) {
        controller.visitChildren(visitor, recursively);
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
        return !this.isEnabled();
    }
    
    
}
