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
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.errors.ModelCreationError;
import org.radixware.kernel.common.client.errors.NoDefinitionWithSuchIdError;
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
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.Dialog;
import org.radixware.wps.views.ViewSupport;


public abstract class RwtDialog extends Dialog implements ICustomDialog {        
    
    public interface RwtDialogListener {
        
        public void opened();
    }
    private final Id id;
    private final Id iconId;
    final Id titleId;
    private final WpsEnvironment env;
    private DialogModel model;
    private String title = null;
    private Icon icon = null;
    private Map<Id, RadCommandDef> commandsById = null;
    protected RadCommandDef[] commands;
    private boolean forcedClose;
    private boolean opening;
    private final ViewSupport<RwtDialog> viewSupport;
    private final List<RwtDialogListener> listeners = new LinkedList<>();
    private final AbstractViewController controller;
    private final String geometryConfigKey;    
    
    protected RwtDialog(IClientEnvironment env, final Id id, final Id titleId, final Id iconId) {
        super(((WpsEnvironment) env).getDialogDisplayer(), "", false);
        controller = new AbstractViewController(env,this) {

            @Override
            protected List<IEmbeddedView> findChildrenViews() {
                return viewSupport.findEmbeddedViews();
            }
        };        
        this.id = id;
        html.setAttr("dlgId", "ee" + id.toString());
        this.iconId = iconId;
        this.titleId = titleId;
        this.env = (WpsEnvironment) env;
        this.model = createModel(new IContext.Dialog(env));
        if (commands != null && commands.length > 0) {
            commandsById = new HashMap<>(commands.length * 2);
            for (RadCommandDef command : commands) {
                commandsById.put(command.getId(), command);
            }
            commands = null;
        } else {
            commandsById = Collections.emptyMap();
        }
        viewSupport = new ViewSupport<>(this);
        setObjectName("rx_dlg_view_#"+id.toString());
        geometryConfigKey =  SettingNames.SYSTEM + "/" + id.toString() + "/geometry";
    }
    
    public void addDialogListener(RwtDialogListener l) {
        synchronized (listeners) {
            if (!listeners.contains(l)) {
                listeners.add(l);
            }
        }
    }
    
    public void removeDialogListener(RwtDialogListener l) {
        synchronized (listeners) {
            listeners.remove(l);
        }
    }
    
    protected void fireOpened() {
        opening = false;
        restoreGeometryFromConfig(geometryConfigKey);        
        List<RwtDialogListener> lst;
        synchronized (listeners) {
            lst = new ArrayList<>(listeners);
        }
        for (RwtDialogListener l : lst) {
            l.opened();
        }        
    }
    
    @Override
    public Icon getIcon() {
        if (icon == null && iconId != null) {
            try {
                icon = getEnvironment().getDefManager().getImage(iconId);
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
    public IClientEnvironment getEnvironment() {
        return env;
    }
    
    @Override
    public String getTitle() {
        if (title == null) {
            if (titleId != null) {
                try {
                    title = getEnvironment().getDefManager().getMlStringValue(id, titleId);
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
    public void open(Model m) {
        if (model.getView() == null) {
            model.setView(this);
        }
        setWindowTitle(model.getWindowTitle());
        setWindowIcon(model.getIcon());        
    }
    
    @Override
    public DialogModel createModel(final IContext.Abstract context) {
        if (context == null) {
            throw new NullPointerException("Context must be not null");
        }
        final Model m = createModelImpl(context.getEnvironment());
        m.setTitle(getTitle()); 
        m.setIcon(getIcon());
        m.setContext(context);
        return (DialogModel) m;
    }
    
    protected Model createModelImpl(IClientEnvironment environment) {
        final Id modelClassId = Id.Factory.changePrefix(id, EDefinitionIdPrefix.ADS_DIALOG_MODEL_CLASS);
        
        try {
            Class<Model> classModel = getEnvironment().getDefManager().getDefinitionModelClass(modelClassId);
            Constructor<Model> constructor = classModel.getConstructor(IClientEnvironment.class, ICustomDialog.class);
            constructor.setAccessible(true);            
            return constructor.newInstance(environment, this);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new ModelCreationError(ModelCreationError.ModelType.DIALOG_MODEL, this, null, e);
        }
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
    public List<RadCommandDef> getEnabledCommands() {
        return Collections.emptyList();
    }
    
    @Override
    public DialogResult execDialog(IWidget parentWidget) {        
        unsetClose();
        opening = true;
        try{
            open(model);
        }finally{
            opening = false;
        }
        if (wasClosed()) {
            return getDialogResult();
        }
        final DialogResult result =  super.execDialog(parentWidget);
        if (model!=null){
            model.afterCloseDialog();
        }
        return result;
    }
    
    @Override
    public boolean close(boolean forced) {
        if (wasClosed()) {
            return true;
        } else if ((forced || model.canSafelyClean(CleanModelController.DEFAULT_INSTANCE)) && (model.beforeCloseDialog(DialogResult.REJECTED) || forced)) {
            forcedClose = true;//do not call canSafelyClean() next time
            rejectDialog();
            return true;
        }
        return false;
    }

    @Override
    protected DialogResult onClose(String action, DialogResult actionResult) {
        if (forcedClose || model==null || (model.canSafelyClean(CleanModelController.DEFAULT_INSTANCE) && model.beforeCloseDialog(actionResult))){
            final Dialog.DialogGeometry geometry = getDialogGeometry();
            if (geometry.isValid()){
                 getEnvironment().getConfigStore().writeString(geometryConfigKey, geometry.asStr());
            }
            return super.onClose(action, actionResult);
        }else{
            return null;
        }
    }    
    
    @Override
    public boolean canSafelyClose(final CleanModelController cleanController) {
        return controller.canSafelyClose(this, cleanController);
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
    public Model getModel() {
        return model;
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
    public void reread() throws ServiceClientException {
    }
    
    @Override
    public void setFocus() {
    }

    @Override
    public void setTop(final int top) {
        if (!opening){//ignore position that was generated by rx designer in open method
            super.setTop(top);
        }
    }

    @Override
    public void setLeft(final int left) {
        if (!opening){//ignore position that was generated by rx designer in open method
            super.setLeft(left);
        }
    }
    
    
}
