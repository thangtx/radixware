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

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.editors.property.PropEditorOptions;
import org.radixware.kernel.common.client.errors.SettingPropertyValueError;
import org.radixware.kernel.common.client.meta.RadPropertyDef;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.models.items.properties.PropertyArr;
import org.radixware.kernel.common.client.views.IArrayEditorDialog;
import org.radixware.kernel.common.client.views.IDialog.DialogResult;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Arr;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.wps.rwt.InputBox;
import org.radixware.wps.rwt.Label;
import org.radixware.wps.rwt.ValueEditor;
import org.radixware.wps.views.editors.valeditors.IValEditor;
import org.radixware.wps.views.editors.valeditors.ValArrEditorController;
import org.radixware.wps.views.editors.valeditors.ValEditorFactory;


public class PropArrEditor<T extends Arr> extends PropEditor {
    
    protected static class ValEditorFactoryImpl<T extends Arr> extends AbstractValEditorFactoryImpl<T>{
        
        private final PropertyArr property;
        
        public ValEditorFactoryImpl(final PropertyArr property){
            super(property);
            this.property = property;
        }

        @Override
        @SuppressWarnings("unchecked")
        public IValEditor createValEditor(EValType valType, EditMask editMask, IClientEnvironment env) {
            final ValArrEditorController controller = new ValArrEditorController(env, property.getType(), property.getValClass()) {
                @Override
                public void edit() {
                    final IArrayEditorDialog dialog = property.getEditorDialog(null);
                    dialog.setReadonly(isReadOnly());
                    if (RadPropertyDef.isPredefinedValuesSupported(property.getType().getArrayItemType(), property.getEditMask().getType())) {
                        dialog.setPredefinedValues(property.getPredefinedValuesForArrayItem());
                    }
                    if (dialog.execDialog() != DialogResult.REJECTED) {
                        final Arr newValue = dialog.getCurrentValue();
                        setPropertyValue(newValue);
                    }

                }
                
                private void setPropertyValue(final Arr newValue){
                    if (property.isOwnValueAcceptable(newValue)){
                        try {
                            property.setValueObject(newValue);
                        } catch (Exception ex) {
                            getEnvironment().processException(new SettingPropertyValueError(property, ex));
                        }
                        //getValEditor().getLineEdit().setText(property.getValueAsString());
//                            if (!Utils.equals(controller.getPropertyValue(), getCurrentValueInEditor())) { //case when bind method was not called (ex. org.radixware.kernel.explorer.widgets.selector.WrapModelDelegate).
//                                PropArrEditor.this.refresh(property);
//                            }
                    }                   
                }

                @Override
                protected InputBox.DisplayController<T> createDisplayController() {
                    return ValEditorFactoryImpl.this.createDisplayControllerWrapper(super.createDisplayController());
                }

                @Override
                protected Label createLabel() {
                    return ValEditorFactoryImpl.this.createLabel();
                }
                                                
                @Override
                protected void setupValEditor(InputBox inputBox) {
                    if (!isMandatory()) {
                        inputBox.setClearController(new InputBox.ClearController<T>() {
                            @Override
                            public T clear() {
                                setPropertyValue(null);
                                return null;
                            }
                        });
                    } else {
                        inputBox.setClearController(null);
                    }                
                }
                
            };
            return controller.getValEditor();
        }
        
    }

    private boolean isEditButtonVisible = true;

    @SuppressWarnings("unchecked")
    public PropArrEditor(final PropertyArr property) {
        this(property, new ValEditorFactoryImpl<T>(property));
    }
        
    @SuppressWarnings("unchecked")
    public PropArrEditor(final PropertyArr property, ValEditorFactory valEditorFactory) {
        super(property, valEditorFactory);
        getValEditor().addValueChangeListener(new ValueEditor.ValueChangeListener() {
            @Override
            public void onValueChanged(final Object oldValue, final Object newValue) {
                if (!Utils.equals(controller.getPropertyValue(), getCurrentValueInEditor())){
                    refresh(getProperty());
                }
            }
        });
    }

    public final void setEditButtonVisible(final boolean isVisible) {
        isEditButtonVisible = isVisible;
        refresh(getProperty());
    }

    @Override
    protected void updateEditor(final Object currentValue, final Object initialValue, PropEditorOptions options) {
        super.updateEditor(currentValue, initialValue, options);
        final ValArrEditorController editorController = (ValArrEditorController) getValEditor();
        if (options.isReadOnly()) {
            editorController.setEditButtonVisible(currentValue != null
                    && !((Arr) currentValue).isEmpty()
                    && isEditButtonVisible
                    && !getProperty().isCustomEditOnly());
        } else {
            editorController.setEditButtonVisible(isEditButtonVisible && !controller.isInheritedValue());
        }
    }
}
