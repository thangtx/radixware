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

package org.radixware.kernel.explorer.env;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QWidget;
import java.util.Collection;
import java.util.List;
import java.util.Stack;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.RadSortingDef;
import org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskConstSet;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameterFactory;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.FilterModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.items.EditorPageModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.models.items.properties.PropertyArr;
import org.radixware.kernel.common.client.models.items.properties.PropertyBin;
import org.radixware.kernel.common.client.models.items.properties.PropertyBlob;
import org.radixware.kernel.common.client.models.items.properties.PropertyBool;
import org.radixware.kernel.common.client.models.items.properties.PropertyChar;
import org.radixware.kernel.common.client.models.items.properties.PropertyClob;
import org.radixware.kernel.common.client.models.items.properties.PropertyDateTime;
import org.radixware.kernel.common.client.models.items.properties.PropertyInt;
import org.radixware.kernel.common.client.models.items.properties.PropertyNum;
import org.radixware.kernel.common.client.models.items.properties.PropertyObject;
import org.radixware.kernel.common.client.models.items.properties.PropertyRef;
import org.radixware.kernel.common.client.models.items.properties.PropertyStr;
import org.radixware.kernel.common.client.models.items.properties.PropertyXml;
import org.radixware.kernel.common.client.views.IArrayEditorDialog;
import org.radixware.kernel.common.client.views.IEditorPageView;
import org.radixware.kernel.common.client.views.IEditorPageWidget;
import org.radixware.kernel.common.client.views.IEntityEditorDialog;
import org.radixware.kernel.common.client.views.IFilterEditorDialog;
import org.radixware.kernel.common.client.views.IParameterCreationWizard;
import org.radixware.kernel.common.client.views.IParameterEditorDialog;
import org.radixware.kernel.common.client.views.IPropEditor;
import org.radixware.kernel.common.client.views.IPropLabel;
import org.radixware.kernel.common.client.views.IProxyPropEditor;
import org.radixware.kernel.common.client.views.ISelectEntitiesDialog;
import org.radixware.kernel.common.client.views.ISelectEntityDialog;
import org.radixware.kernel.common.client.views.ISortingEditorDialog;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.client.widgets.IListWidget;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.client.widgets.area.IWidgetArea;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.types.Arr;
import org.radixware.kernel.explorer.dialogs.ArrayEditorDialog;
import org.radixware.kernel.explorer.dialogs.EntityEditorDialog;
import org.radixware.kernel.explorer.dialogs.EnumItemsEditorDialog;
import org.radixware.kernel.explorer.dialogs.FilterEditorDialog;
import org.radixware.kernel.explorer.dialogs.SelectEntitiesDialog;
import org.radixware.kernel.explorer.dialogs.SelectEntityDialog;
import org.radixware.kernel.explorer.dialogs.SortingEditorDialog;
import org.radixware.kernel.explorer.editors.filterparameditor.ParameterCreationWizard;
import org.radixware.kernel.explorer.editors.filterparameditor.ParameterEditorDialog;
import org.radixware.kernel.explorer.views.IExplorerView;
import org.radixware.kernel.explorer.views.StandardEditor;
import org.radixware.kernel.explorer.views.StandardEditorPage;
import org.radixware.kernel.explorer.views.StandardFilterParameters;
import org.radixware.kernel.explorer.views.StandardForm;
import org.radixware.kernel.explorer.views.StandardParagraphEditor;
import org.radixware.kernel.explorer.views.StandardReportParametersDialog;
import org.radixware.kernel.explorer.views.selector.StandardSelector;
import org.radixware.kernel.explorer.widgets.EditorPage;
import org.radixware.kernel.explorer.widgets.ExplorerListWidget;
import org.radixware.kernel.explorer.widgets.PropLabelsPool;
import org.radixware.kernel.explorer.widgets.area.ExplorerWidgetsArea;
import org.radixware.kernel.explorer.widgets.propeditors.PropEditorsPool;
import org.radixware.kernel.explorer.widgets.propeditors.PropTextEditor;
import org.radixware.kernel.explorer.widgets.propeditors.ProxyPropEditor;


public class ExplorerStandardViewsFactory extends QObject implements org.radixware.kernel.common.client.views.StandardViewFactory {                

    @Override
    public IWidgetArea newWidgetArea(IClientEnvironment environment, IWidget parent) {
        return new ExplorerWidgetsArea(environment, parent); 
    }
    
    private final static class UpdatePool extends QEvent{        
        public UpdatePool(){
            super(QEvent.Type.User);
        }
    }
    
    private static final int SELECTORS_MAX_POOL_SIZE = 3;
    private static final int EDITORS_MAX_POOL_SIZE = 4;
    
    private final Stack<StandardSelector> standardSelectorsPool = new Stack<>();
    private final Stack<StandardEditor> standardEditorsPool = new Stack<>();        
        
    private boolean updatePoolScheduled;
    private boolean updatePoolBlocked;
    private int blockScheduled;
    private final IClientEnvironment environment;
    
    ExplorerStandardViewsFactory(final QObject parent, final IClientEnvironment environment){
        super(parent);
        this.environment = environment;        
        scheduleUpdatePool();
    }

    @Override
    public StandardSelector newStandardSelector(IClientEnvironment environment) {
        final StandardSelector view;
        if (standardSelectorsPool.isEmpty()){
            view = new StandardSelector(environment);
        }else{            
            view = standardSelectorsPool.pop();
            view.setInCache(false);
        }
        if (!updatePoolScheduled){
            scheduleUpdatePool();
        }        
        return view;
    }

    @Override
    public IView newStandardReportParametersDialog(IClientEnvironment environment) {
        return new StandardReportParametersDialog(environment);
    }

    @Override
    public IView newStandardParagraphEditor(IClientEnvironment environment) {
        return new StandardParagraphEditor(environment);
    }

    @Override
    public IView newStandardForm(IClientEnvironment environment) {
        return new StandardForm(environment);
    }

    @Override
    public IView newStandardFilterParameters(IClientEnvironment environment) {
        return new StandardFilterParameters(environment);
    }

    @Override
    public StandardEditor newStandardEditor(IClientEnvironment environment) {
        final StandardEditor view;
        if (standardEditorsPool.isEmpty()){            
            view = new StandardEditor(environment);
        }else{
            view = standardEditorsPool.pop();            
            view.setInCache(false);
        }
        if (!updatePoolScheduled){
            scheduleUpdatePool();
        }        
        return view;
    }

    @Override
    public IParameterCreationWizard newParameterCreationWizard(IClientEnvironment environment, ISqmlTableDef table, final ISqmlParameterFactory parameterFactory, List<String> parameterNames, IWidget parent) {
        return new ParameterCreationWizard(environment, table, parameterFactory, parameterNames, (QWidget) parent);
    }

    @Override
    public IParameterEditorDialog newParameterEditorDialog(IClientEnvironment environment, ISqmlParameter parameter, ISqmlTableDef table, List<String> parameterNames, boolean readonly, IWidget parent) {
        return new ParameterEditorDialog(environment, parameter, table, parameterNames, readonly, null);
    }

    @Override
    public IPropEditor newPropIntEditor(PropertyInt prop) {
        return PropEditorsPool.getInstance().getPropIntEditor(prop);
    }

    @Override
    public IPropEditor newPropObjectEditor(PropertyObject prop) {
        return PropEditorsPool.getInstance().getPropObjectEditor(prop);
    }

    @Override
    public IPropEditor newPropListEditor(Property prop) {
        return PropEditorsPool.getInstance().getPropListEditor(prop);
    }

    @Override
    public IPropEditor newPropXmlEditor(PropertyXml prop) {
        return PropEditorsPool.getInstance().getPropXmlEditor(prop);
    }

    @Override
    public IPropEditor newPropStrEditor(PropertyStr prop) {
        return PropEditorsPool.getInstance().getPropStrEditor(prop);
    }

    @Override
    public IPropEditor newPropBinEditor(PropertyBin prop) {
        return PropEditorsPool.getInstance().getPropBinEditor(prop);
    }

    @Override
    public IPropEditor newPropBlobEditor(PropertyBlob prop) {
        return PropEditorsPool.getInstance().getPropBinEditor(prop);
    }

    @Override
    public IPropEditor newPropBoolEditor(PropertyBool prop) {
        return PropEditorsPool.getInstance().getPropBoolEditor(prop);
    }

    @Override
    public IPropEditor newPropCharEditor(PropertyChar prop) {
        return PropEditorsPool.getInstance().getPropCharEditor(prop);
    }

    @Override
    public IPropEditor newPropClobEditor(PropertyClob prop) {
        return PropEditorsPool.getInstance().getPropStrEditor(prop);
    }

    @Override
    public IPropEditor newPropDateTimeEditor(PropertyDateTime prop) {
        return PropEditorsPool.getInstance().getPropDateTimeEditor(prop);
    }

    @Override
    public IPropEditor newPropNumEditor(PropertyNum prop) {
        return PropEditorsPool.getInstance().getPropNumEditor(prop);
    }

    @Override
    public IPropEditor newPropObjectlEditor(PropertyObject prop) {
        return PropEditorsPool.getInstance().getPropObjectEditor(prop);
    }

    @Override
    public IPropEditor newPropRefEditor(PropertyRef prop) {
        return PropEditorsPool.getInstance().getPropRefEditor(prop);
    }
    
    @Override
    public IPropEditor newPropTextStrEditor(PropertyStr prop) {
        return new PropTextEditor(prop);
    }

    @Override
    public IPropEditor newPropTextClobEditor(PropertyClob prop) {
        return new PropTextEditor(prop);
    }    

    @Override
    public <T extends Arr> IPropEditor newPropArrEditor(PropertyArr<T> prop) {
        return PropEditorsPool.getInstance().getPropArrEditor(prop);               
    }

    @Override
    public IProxyPropEditor newProxyPropEditor(final Property prop, final EValType valType, final EditMask editMask) {
        return new ProxyPropEditor(prop, valType, editMask);
    }        

    @Override
    public IPropLabel newPropLabel(Property prop) {
        return PropLabelsPool.getInstance().getPropLabel(null, prop);
    }

    @Override
    public IEntityEditorDialog newEntityEditorDialog(EntityModel entity){
        final ERuntimeEnvironmentType environmentType = entity.getEditorPresentationDef().getRuntimeEnvironmentType();
        if (environmentType!=ERuntimeEnvironmentType.COMMON_CLIENT && environmentType!=ERuntimeEnvironmentType.EXPLORER){
            final String message = 
                "Can't use editor for "+environmentType.getName()+" environment  in "+ERuntimeEnvironmentType.EXPLORER.getName()+" environment";
            throw new IllegalUsageError(message);
        }
        return new EntityEditorDialog(entity);
    }

    @Override
    public ISelectEntityDialog newSelectEntityDialog(GroupModel parentGroupModel, boolean canClear){
        final ERuntimeEnvironmentType environmentType = parentGroupModel.getSelectorPresentationDef().getRuntimeEnvironmentType();
        if (environmentType!=ERuntimeEnvironmentType.COMMON_CLIENT && environmentType!=ERuntimeEnvironmentType.EXPLORER){
            final String message = 
                "Unable to use selector for "+environmentType.getName()+" environment  in "+ERuntimeEnvironmentType.EXPLORER.getName()+" environment";
            throw new IllegalUsageError(message);
        }
        return new SelectEntityDialog(parentGroupModel, canClear);
    }

    @Override
    public ISelectEntitiesDialog newSelectEntitiesDialog(final GroupModel groupModel, final boolean canClear) {
        final ERuntimeEnvironmentType environmentType = groupModel.getSelectorPresentationDef().getRuntimeEnvironmentType();
        if (environmentType!=ERuntimeEnvironmentType.COMMON_CLIENT && environmentType!=ERuntimeEnvironmentType.EXPLORER){
            final String message = 
                "Unable to use selector for "+environmentType.getName()+" environment  in "+ERuntimeEnvironmentType.EXPLORER.getName()+" environment";
            throw new IllegalUsageError(message);
        }
        return new SelectEntitiesDialog(groupModel, canClear);
    }
        
    @Override
    public IFilterEditorDialog newFilterEditorDialog(FilterModel filter, Collection<String> restrictedNames, boolean showApplyButton, IWidget parent) {
        return new FilterEditorDialog(filter, restrictedNames, showApplyButton, (QWidget) parent);
    }

    @Override
    public ISortingEditorDialog newSortingEditorDialog(final IClientEnvironment environment, final RadSortingDef sorting, final Collection<String> restrictedNames, final boolean showApplyButton, final IWidget parent) {
        return new SortingEditorDialog(environment, sorting, restrictedNames, showApplyButton, (QWidget)parent);
    }    
    
    @Override
    public IEditorPageWidget newEditorPageWidget(EditorPageModelItem item) {
        return new EditorPage(item);
    }

    @Override
    public IEditorPageView newStandardEditorPage(IClientEnvironment environment, IView parentView, RadEditorPageDef editorPage) {
        return new StandardEditorPage(environment, (IExplorerView) parentView, editorPage);
    }

    @Override
    public IArrayEditorDialog newArrayEditorDialog(PropertyArr prop, IWidget parent) {
        final boolean canUseEnumItemsEditor = 
                (prop.getEditMask() instanceof EditMaskConstSet) && 
                !prop.isReadonly() &&
                 prop.isArrayItemMandatory() &&
                !prop.getDefinition().isDuplicatesEnabled();
        if (canUseEnumItemsEditor){
            return new EnumItemsEditorDialog(prop, (QWidget) parent);
        }
        else{
            return new ArrayEditorDialog(prop, (QWidget) parent);
        }
    }    

    @Override
    public IListWidget newListWidget(IClientEnvironment environment, IWidget parent) {
        return new ExplorerListWidget(environment, (QWidget)parent);
    }
        
  
    @Override
    protected void customEvent(final QEvent event) {
        if (event instanceof UpdatePool){
            event.accept();
            if (blockScheduled>0){
                updatePoolBlocked = true;
                return;
            }else{
                updatePoolBlocked = false;
            }
            if (standardSelectorsPool.size()<SELECTORS_MAX_POOL_SIZE){
                final StandardSelector view = new StandardSelector(environment);
                standardSelectorsPool.push(view);
                view.setInCache(true);
            }else if (standardEditorsPool.size()<EDITORS_MAX_POOL_SIZE){
                final StandardEditor view = new StandardEditor(environment);                
                standardEditorsPool.push(view);
                view.setInCache(true);
            }
            if (standardSelectorsPool.size()<SELECTORS_MAX_POOL_SIZE
                 || standardEditorsPool.size()<EDITORS_MAX_POOL_SIZE){
                scheduleUpdatePool();
            }else{
                updatePoolScheduled = false;
            }
        }else{
            super.customEvent(event);
        }
    }
    
    private void scheduleUpdatePool(){
        updatePoolScheduled = true;
        QApplication.postEvent(this, new UpdatePool());
    }
    
    void clearPools(){
        for (StandardSelector selector: standardSelectorsPool){
            selector.setAttribute(Qt.WidgetAttribute.WA_DeleteOnClose, true);
            selector.setInCache(false);
            selector.close();
        }
        standardSelectorsPool.clear();
        for (StandardEditor editor: standardEditorsPool){
            editor.setAttribute(Qt.WidgetAttribute.WA_DeleteOnClose, true);
            editor.setInCache(false);
            editor.close();
        }
        standardEditorsPool.clear();
    }
    
    public void blockScheduledViewCreation(){
        blockScheduled++;
    }
    
    public void unblockScheduledViewCreation(){
        if (blockScheduled>0){
            blockScheduled--;
            if (blockScheduled==0 && updatePoolBlocked){
                updatePoolBlocked = false;
                QApplication.postEvent(this, new UpdatePool());
            }
        }
    }
    
}
