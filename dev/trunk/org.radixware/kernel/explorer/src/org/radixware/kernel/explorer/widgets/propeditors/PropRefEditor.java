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

package org.radixware.kernel.explorer.widgets.propeditors;

import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.editors.property.PropEditorOptions;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.errors.ObjectNotFoundError;
import org.radixware.kernel.common.client.errors.ParentRefSetterError;
import org.radixware.kernel.common.client.errors.SettingPropertyValueError;
import org.radixware.kernel.common.client.meta.filters.RadFilterParamDef;
import org.radixware.kernel.common.client.meta.RadParentRefPropertyDef;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskRef;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.models.items.properties.PropertyRef;
import org.radixware.kernel.common.client.types.RefEditingHistory;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.explorer.editors.valeditors.ValEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValEditorFactory;
import org.radixware.kernel.explorer.editors.valeditors.ValRefEditor;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import static org.radixware.kernel.explorer.widgets.propeditors.PropRefEditor.getValEditorFactory;

public class PropRefEditor extends PropReferenceEditor {
    
    private static class ValEditorFactoryImpl extends ValEditorFactory{
        
        private static final PropRefEditor.ValEditorFactoryImpl INSTANCE = new PropRefEditor.ValEditorFactoryImpl();
        
        private ValEditorFactoryImpl(){
            
        }

        @Override
        public ValEditor createValEditor(final EValType valType, final EditMask editMask, final IClientEnvironment environment, final QWidget parentWidget) {
            return new ValRefEditor(environment, parentWidget, (EditMaskRef)editMask, false, false, false, false){
                        @Override
                        protected boolean eq(final Reference value1, final Reference value2) {
                            return Reference.exactlyMatch(value1, value2);
                        }
                    };
        }
        
    }    
    
    private boolean internalUpdate;

    public PropRefEditor(final PropertyRef property) {
       this(property, getValEditorFactory());
    }
    
    protected PropRefEditor(final PropertyRef property, final ValEditorFactory factory){
        super(property,factory);
        setup();
        changeReferenceButton.setToolTip(getEnvironment().getMessageProvider().translate("PropRefEditor", "Select Object"));
        changeReferenceButton.setIcon(ExplorerIcon.getQIcon(ClientIcon.Definitions.SELECTOR));        
    }
    
    @Override
    public void setProperty(final Property property) {
        super.setProperty(property);
        if (property!=null){
            setup();
        }
    }    
    
    private void setup(){
        final ValEditor editor = getValEditor();
        if (editor instanceof ValRefEditor){
            ((ValRefEditor)editor).setSelectorPresentation(getParentSelectorPresentation());
        }        
        editor.valueChanged.connect(this,"updatePropertyValue(Object)");        
    }

    @Override
    void clear() {
        getValEditor().valueChanged.disconnect(this);
        super.clear();        
    }
    
    
    
    public static ValEditorFactory getValEditorFactory(){
        return PropRefEditor.ValEditorFactoryImpl.INSTANCE;
    }

    private RadSelectorPresentationDef getParentSelectorPresentation() {
        RadSelectorPresentationDef presentation;
        if (getProperty().getDefinition() instanceof RadParentRefPropertyDef) {
            presentation = ((RadParentRefPropertyDef) getProperty().getDefinition()).getParentSelectorPresentation();
        } else if (getProperty().getDefinition() instanceof RadFilterParamDef) {
            presentation = ((RadFilterParamDef) getProperty().getDefinition()).getParentSelectorPresentation();
        } else {
            presentation = null;
        }
        return presentation;
    }

    @Override
    public void onChangeReferenceButtonClick() {
        final PropertyRef property = (PropertyRef) getProperty();
        boolean newValueAccepted = false;
        boolean tryAgain;
        Reference newValue = null;
        do{
            try {
                newValue = property.selectParent();
            } catch (InterruptedException e) {
                getEnvironment().processException(e);
                return;
            } catch (Exception exception) {
                final String msg = getEnvironment().getMessageProvider().translate("ExplorerException", "Can't open selector for \'%s\':\n%s");
                processException(exception, getEnvironment().getMessageProvider().translate("ExplorerException", "Error on opening selector"), msg);
                return;
            }
            try{
                newValueAccepted = updatePropertyValue(newValue);
                tryAgain = false;
            }catch(ObjectNotFoundError error){
                final String title = getEnvironment().getMessageProvider().translate("ExplorerMessage", "Selected Object Does not Exist");
                getEnvironment().processException(title,error);
                tryAgain = true;
            }
        }while(tryAgain);
        if (newValueAccepted){
            edited.emit(newValue);
            getValEditor().updateHistory();            
        }
        property.onFinishEditValue(newValueAccepted);
    }
        
    private boolean updatePropertyValue(final Object newValue) throws ObjectNotFoundError{
        if (internalUpdate){
            return false;
        }
        final PropertyRef property = (PropertyRef) getProperty();        
        final Reference newReference = (Reference)newValue;
        if (!Utils.equals(getPropertyValue(), newReference)) {
            try {
                property.setValueObject(newReference);
            } catch(ParentRefSetterError error){
                if (error.getCause() instanceof ObjectNotFoundError){
                    final ObjectNotFoundError objectNotFound = (ObjectNotFoundError)error.getCause();
                    if (newReference!=null && objectNotFound.inContextOf(newReference)){
                        throw objectNotFound;
                    }
                }
                getEnvironment().processException(new SettingPropertyValueError(property, error));
                return false;                
            } catch (Exception exception) {
                getEnvironment().processException(new SettingPropertyValueError(property, exception));
                return false;
            }
            if (!Utils.equals(getPropertyValue(), getCurrentValueInEditor())) //case when bind method was not called (ex. org.radixware.kernel.explorer.widgets.selector.WrapModelDelegate).
            {
                refresh(property);                
            }
            return true;
        }
        return false;
    }

    @Override
    protected void enableEditingHistory(final String settingPath) {
        final RadSelectorPresentationDef parentPresentation = getParentSelectorPresentation();
        if(parentPresentation == null) {
            setEditingHistory(null);
        } else {
            final RefEditingHistory history = new RefEditingHistory(getEnvironment(), settingPath, parentPresentation.getTableId(), parentPresentation.getId());
            setEditingHistory(history);
        }
    }

    @Override
    protected void updateEditor(final Object value, final PropEditorOptions options) {
        internalUpdate = true;        
        try{
            super.updateEditor(value, options);
        }
        finally{
            internalUpdate = false;
        }
    }
    
    
    
}
