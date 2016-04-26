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

package org.radixware.wps.views.editor.property;

import java.math.BigDecimal;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.editors.property.PropertyProxy;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskConstSet;
import org.radixware.kernel.common.client.meta.mask.EditMaskList;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.types.IKernelEnum;
import org.radixware.kernel.common.types.Id;
import org.radixware.wps.rwt.InputBox;
import org.radixware.wps.rwt.InputBox.DisplayController;
import org.radixware.wps.rwt.Label;
import org.radixware.wps.views.editors.valeditors.IValEditor;
import org.radixware.wps.views.editors.valeditors.ValConstSetEditorController;
import org.radixware.wps.views.editors.valeditors.ValEditorController;
import org.radixware.wps.views.editors.valeditors.ValEditorFactory;
import org.radixware.wps.views.editors.valeditors.ValListEditorController;


public class PropListEditor extends PropEditor {
    
    private static class ValListEditorFactoryImpl<T> extends AbstractValEditorFactoryImpl<T>{
        
        public ValListEditorFactoryImpl(final Property property){
            super(property);
        }

        @Override
        public IValEditor createValEditor(final EValType valType, final EditMask editMask, final IClientEnvironment env) {
            final ValEditorController controller = new ValListEditorController<T>(env){
                @Override
                protected DisplayController<T> createDisplayController() {
                    return ValListEditorFactoryImpl.this.createDisplayControllerWrapper(super.createDisplayController());
                }

                @Override
                protected Label createLabel() {
                    return ValListEditorFactoryImpl.this.createLabel();
                }                
            };
            return controller.getValEditor();
        }    
    }            
    
    private static class ValEnumEditorFactoryImpl<T extends Comparable> extends AbstractValEditorFactoryImpl<T>{
        
        public ValEnumEditorFactoryImpl(final Property property){
            super(property);
        }

        @Override
        public IValEditor createValEditor(final EValType valType, final EditMask editMask, final IClientEnvironment env) {
            final ValEditorController controller = new ValConstSetEditorController<T>(env, (EditMaskConstSet)property.getEditMask()){
                                
                @Override
                protected DisplayController<T> createDisplayController() {
                    final DisplayController<T> defaultDisplayController = super.createDisplayController();                            
                    return new InputBox.DisplayController<T>(){
                        @Override
                        public String getDisplayValue(final T value, final boolean isFocused, final boolean isReadOnly) {
                            final String defaultDisplayString = defaultDisplayController.getDisplayValue(value, isFocused, isReadOnly);
                            RadEnumPresentationDef.Items items = 
                                ((EditMaskConstSet)property.getEditMask()).getItems(property.getEnvironment().getApplication());
                            RadEnumPresentationDef.Item item = items.findItemByValue(value);
                            final Model model = property.getOwner();
                            final Id propertyId = property.getId();
                            final boolean isOwn = property.hasOwnValue();
                            if (item==null){
                                return model.getDisplayString(propertyId,value,defaultDisplayString,!isOwn);
                            }else{
                                return model.getDisplayString(propertyId,item.getConstant(),defaultDisplayString,!isOwn);
                            }
                        }
                    
                    };
                }

                @Override
                protected Label createLabel() {
                    return ValEnumEditorFactoryImpl.this.createLabel();
                }                
            };
            return controller.getValEditor();
        }    
    }    
    

    public PropListEditor(final Property property) {
        super(property, createValEditorFactory(property));
        setPropertyProxy(new PropertyProxy(property) {

            @Override
            public Object getPropertyInitialValue() {
                final Object value = super.getPropertyInitialValue();
                if ((getProperty().getEditMask() instanceof EditMaskConstSet) && (value instanceof IKernelEnum)) {
                    return ((IKernelEnum) value).getValue();
                }
                return value;
            }

            @Override
            public Object getPropertyValue() {
                final Object value = super.getPropertyValue();
                if ((getProperty().getEditMask() instanceof EditMaskConstSet) && (value instanceof IKernelEnum)) {
                    return ((IKernelEnum) value).getValue();
                }
                return value;
            }
        });
    }
    
    private static ValEditorFactory createValEditorFactory(final Property property){
        if (property.getEditMask().getType()==EEditMaskType.LIST){
            switch(property.getType()){
                case STR:
                    return new ValListEditorFactoryImpl<String>(property);
                case INT:
                    return new ValListEditorFactoryImpl<Long>(property);
                case NUM:
                    return new ValListEditorFactoryImpl<BigDecimal>(property);
                case CHAR:
                    return new ValListEditorFactoryImpl<Character>(property);
                default:
                    throw new IllegalUsageError("Can't create editor for list edit mask and " + property.getType().getName() + " property type");
            }
        }else{
            switch(property.getType()){
                case STR:
                    return new ValEnumEditorFactoryImpl<String>(property);
                case INT:
                    return new ValEnumEditorFactoryImpl<Long>(property);
                case CHAR:
                    return new ValEnumEditorFactoryImpl<Character>(property);
                default:
                    throw new IllegalUsageError("Can't create editor for enum edit mask and " + property.getType().getName() + " property type");
            }
        }
    }
}