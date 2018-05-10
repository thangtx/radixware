/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.common.client.widgets.arreditor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskRef;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.IEntitySelectionController;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.items.properties.PropertyArrRef;
import org.radixware.kernel.common.client.types.ArrRef;
import org.radixware.kernel.common.client.types.ChoosableEntitiesFilter;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.client.views.IDialog;
import org.radixware.kernel.common.client.views.ISelectEntitiesDialog;
import org.radixware.kernel.common.client.views.ISelectEntityDialog;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;


public abstract class AbstractArrayRefEditorDelegate<T,W> extends AbstractArrayEditorDelegate<T,W> {
    
    private GroupModel group;
    
    protected abstract Model findNearestModel(final IWidget arrayEditor);
    
    public GroupModel getGroupModel(final IWidget arrayEditor,
                                                         final IClientEnvironment environment,
                                                         final ArrayItemEditingOptions options,
                                                         final IEntitySelectionController filter) throws ServiceClientException, InterruptedException {
        final RadSelectorPresentationDef selectorPresentation = options.getSelectorPresentation(environment);
        if (selectorPresentation==null && options.getPropertyRef()==null) {
            throw new IllegalUsageError("Selector presentation not defined");
        }
        if (group == null) {
            if (options.getPropertyRef()!= null) {
                group = options.getPropertyRef().openGroupModel();
            } else {
                final Model holderModel = findNearestModel(arrayEditor);
                if (holderModel==null){
                    group = GroupModel.openTableContextlessSelectorModel(environment, selectorPresentation);                            
                }else{
                    group = GroupModel.openTableContextlessSelectorModel(holderModel, selectorPresentation);
                }
                final EditMask editMask = options.getEditMask();
                if (editMask instanceof EditMaskRef && !group.applyEditMaskSettings((EditMaskRef)editMask)){
                    return null;
                }
            }
        } else if (options.getPropertyRef()!= null) {
            group.reset();
            group.setCondition(options.getPropertyRef().getCondition());
            final Map<Id, Object> propertyValues = options.getPropertyRef().getGroupPropertyValues();
            for (Map.Entry<Id, Object> propertyValue : propertyValues.entrySet()) {
                group.getProperty(propertyValue.getKey()).setValueObject(propertyValue.getValue());
            }
        } else {
            final EditMask editMask = options.getEditMask();
            if (editMask instanceof EditMaskRef && !group.applyEditMaskSettings((EditMaskRef)editMask)){
                return null;
            }
        }
        group.setEntitySelectionController(filter);
        return group;
    }    
    
    protected Reference selectEntity(final IWidget parentWidget,
                                                      final IClientEnvironment environment,
                                                      final ArrayItemEditingOptions options,
                                                      final Reference currentValue,
                                                      final List<Reference> currentValues){
        final ChoosableEntitiesFilter filter;
        if (!options.isDuplicatesEnabled()) {
            filter = new ChoosableEntitiesFilter();            
            for (Reference ref: currentValues){
                if (ref!=null && !ref.equals(currentValue)){
                    filter.add(ref.getPid());
                }
            }
        }else{
            filter = null;
        }        
        
        GroupModel groupModel = null;
        try {
            groupModel = getGroupModel(parentWidget, environment, options, filter);
            if (groupModel==null){
                throw new IllegalUsageError("Unable to create GroupModel instance");
            }            
            final ISelectEntityDialog dialog = 
                environment.getApplication().getStandardViewsFactory().newSelectEntityDialog(groupModel, !options.isMandatory());
            if (dialog.execDialog()==IDialog.DialogResult.REJECTED) {
                return currentValue;
            }
            final EntityModel entity = dialog.getSelectedEntity();
            final Reference newValue = entity == null ? null : new Reference(entity);
            if (newValue==null){
                return null;
            }else{
                try{
                    final ArrRef arrRefs = calcTitles(options.getPropertyRef(), new ArrRef(newValue));
                    return arrRefs!=null && arrRefs.size()==1 ? arrRefs.get(0) : null;
                }catch(InterruptedException ex){
                    return currentValue;
                }catch(ServiceClientException ex){
                    processExceptionOnCalcTitles(environment, ex, options.getPropertyRef(), options.getSelectorPresentation(environment));
                    return currentValue;
                }
            }
        }catch(InterruptedException ex){
            return currentValue;
        }catch(ServiceClientException | RuntimeException exception){
            processExceptionOnOpenSelector(environment, 
                                                               exception, 
                                                               options.getPropertyRef(),
                                                               options.getSelectorPresentation(environment));
            return currentValue;
        } finally {
            if (groupModel!=null){
                groupModel.clean();
            }
        }
        
    }
    
    @Override
    public List<Object> createNewValues(final IWidget arrayEditor, 
                                                             final IClientEnvironment environment,
                                                             final ArrayItemEditingOptions options,
                                                             final List<Object> currentValues) {
        final ChoosableEntitiesFilter filter;
        if (!options.isDuplicatesEnabled() && currentValues!=null && !currentValues.isEmpty()) {
            filter = new ChoosableEntitiesFilter();            
            for (Object value: currentValues){
                if (value instanceof Reference){
                    filter.add(((Reference)value).getPid());
                }
            }
        }else{
            filter = null;
        }        
        final List<Object> newValues = new ArrayList<>();
        try {
            final GroupModel groupModel = getGroupModel(arrayEditor, environment, options, filter);
            if (groupModel!=null){
                final ISelectEntitiesDialog dialog = 
                    environment.getApplication().getStandardViewsFactory().newSelectEntitiesDialog(groupModel, !options.isMandatory());                
                if (dialog.execDialog()!=IDialog.DialogResult.REJECTED) {
                    if (dialog.clearButtonWasClicked()){
                        newValues.add(null);
                    }else{
                        final List<EntityModel> selection = dialog.getSelectedEntities();
                        ArrRef arrRefs = new ArrRef();
                        for (EntityModel selectedObject: selection){
                            arrRefs.add(new Reference(selectedObject));
                        }
                        try{
                           arrRefs = calcTitles(options.getPropertyRef(), arrRefs);
                        }catch(ServiceClientException exception){
                            processExceptionOnCalcTitles(environment, 
                                                                         exception, 
                                                                         options.getPropertyRef(), 
                                                                         options.getSelectorPresentation(environment));
                            return newValues;
                        }
                        for (Reference ref: arrRefs){
                            if (ref!=null && ref.isValid()){
                                newValues.add(ref);
                            }
                        }
                    }
                }
            }            
        }catch(InterruptedException ex){

        }catch(ServiceClientException | RuntimeException exception){
            processExceptionOnOpenSelector(environment, 
                                                               exception,
                                                               options.getPropertyRef(), 
                                                               options.getSelectorPresentation(environment));
        }finally {
            if (group!=null){
                group.clean();
            }
        }
        return newValues;
    }
    
    
    protected void processExceptionOnCalcTitles(final IClientEnvironment environment, 
                                                                        final ServiceClientException ex,
                                                                        final PropertyArrRef propertyRef,
                                                                        final RadSelectorPresentationDef selectorPresentation){
        final MessageProvider mp = environment.getMessageProvider();
        final String err_title = mp.translate("ExplorerException", "Error on set value");
        final String err_msg = mp.translate("ExplorerException", "Failed to set value of \'%s\':\n%s");
        final String reason = ClientException.getExceptionReason(mp, ex);
        final String trace = reason + ":\n" + ClientException.exceptionStackToString(ex);
        final String title;
        if (propertyRef==null){
            if (selectorPresentation==null){
                title = mp.translate("Explorer item","<No Title>");
            }else{
                title = selectorPresentation.getTitle();
            }
        }else{
            title = propertyRef.getTitle();
        }
        environment.processException(err_title, ex);
        environment.getTracer().put(EEventSeverity.DEBUG, String.format(err_msg, title, trace), EEventSource.EXPLORER);        
    }
    
    protected void processExceptionOnOpenSelector(final IClientEnvironment environment, 
                                                                             final Throwable ex,
                                                                             final PropertyArrRef propertyRef,
                                                                             final RadSelectorPresentationDef selectorPresentation){
        final MessageProvider mp = environment.getMessageProvider();
        final String err_title = mp.translate("ExplorerException", "Failed to open selector");
        final String err_msg = mp.translate("ExplorerException", "Failed to open selector for \'%s\':\n%s");
        final String reason = ClientException.getExceptionReason(mp, ex);
        final String trace = reason + ":\n" + ClientException.exceptionStackToString(ex);
        final String title;
        if (propertyRef==null){
            if (selectorPresentation==null){
                title = mp.translate("Explorer item","<No Title>");
            }else{
                title = selectorPresentation.getTitle();
            }
        }else{
            title = propertyRef.getTitle();
        }
        environment.processException(err_title, ex);
        environment.getTracer().put(EEventSeverity.DEBUG, String.format(err_msg, title, trace), EEventSource.EXPLORER);        
    }    

    protected static ArrRef calcTitles(final PropertyArrRef propertyRef, final ArrRef arrRefs) throws InterruptedException, ServiceClientException{
        if (arrRefs==null 
            || arrRefs.isEmpty() 
            || propertyRef==null 
            || propertyRef.createContext() instanceof IContext.ContextlessSelect){
            return arrRefs;
        }
        return propertyRef.updateTitles(arrRefs);
    }     
}