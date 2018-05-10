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
import java.util.EnumSet;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.editors.property.PropEditorOptions;
import org.radixware.kernel.common.client.editors.property.ProxyPropEditorController;
import org.radixware.kernel.common.client.enums.EReferenceStringFormat;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.validators.InvalidValueReason;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.utils.IValAsStrConverter;
import org.radixware.kernel.common.client.views.IProxyPropEditor;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.explorer.editors.valeditors.ExceptionView;
import org.radixware.kernel.explorer.editors.valeditors.ValEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValEditorFactory;


public  class ProxyPropEditor extends PropEditor implements IProxyPropEditor {
    
    private static class InternalValEditorFactory extends ValEditorFactory {
        
        private final String value;
        private final Throwable exception;
        private final EditMask mask;
        private final EValType valType;
        
        public InternalValEditorFactory(final EValType valType, final EditMask editMask){
            this.valType = valType;
            this.mask = editMask;
            this.value = null;
            this.exception = null;
        }
        
        public InternalValEditorFactory(final Throwable exception, final String value){
            this.value = value;
            this.exception = exception;
            this.valType = null;
            this.mask = null;
        }

        @Override
        public ValEditor createValEditor(final EValType valType, final EditMask editMask, final IClientEnvironment environment, final QWidget parentWidget) {
            if (exception==null){
                return getDefault().createValEditor(this.valType, this.mask, environment, parentWidget);
            }else{
                final ExceptionView editor = new ExceptionView(environment, parentWidget);
                editor.setExceptionDetailsBtnVisible(false);
                editor.setStringToShow(value);
                editor.setValue(exception);
                editor.setValidationResult(ValidationResult.Factory.newInvalidResult(InvalidValueReason.WRONG_FORMAT));
                return editor;
            }
        }
    }    
    
    private EditMask editMask;
    private EValType evalType;
    private boolean wasBinded;
    private final ProxyPropEditorController proxyPropEditorController;
    
    @SuppressWarnings("LeakingThisInConstructor")
    private ProxyPropEditor(final Property prop, 
                            final EValType type, 
                            final EditMask mask, 
                            final IValAsStrConverter converter, 
                            final EReferenceStringFormat format) {
        super(prop, new InternalValEditorFactory(type, mask));
        evalType = type;
        editMask = EditMask.newCopy(mask);
        proxyPropEditorController = new ProxyPropEditorController(getEnvironment(), this, converter, format) {
            
            @Override
            protected void updateErrorView(final Throwable error, final String value) {
                final ExceptionView editor = (ExceptionView)getValEditor();
                editor.setStringToShow(value);
                editor.setValue(error);
            }
            
            @Override
            protected void switchToErrorView(final Throwable error, final String value) {
                changeValEditor(new InternalValEditorFactory(error, value));
            }
            
            @Override
            protected void switchToEditor(final EValType valType, final EditMask mask) {
                changeValEditor(new InternalValEditorFactory(valType, mask));
            }
            
            @Override
            protected void setEditMask(final EditMask mask) {
                editMask = EditMask.newCopy(mask);
            }
            
            @Override
            protected void setValueType(final EValType type) {
                evalType = type;
            }
            
            @Override
            protected Object getCurrentValueInEditor() {
                return ProxyPropEditor.this.getCurrentValueInEditor();
            }

            @Override
            protected boolean wasBinded() {
                return wasBinded;
            }                        
        };
    }    

    public ProxyPropEditor(final Property prop, final EValType type, final EditMask mask) {
        this(prop,type,mask,null,null);
    }
    
    public ProxyPropEditor(final Property prop, final EValType type, final EditMask mask, final IValAsStrConverter converter) {
        this(prop, type, mask, converter, null);
    }    
    
    public ProxyPropEditor(final Property prop, final EValType type, final EditMask mask, final EReferenceStringFormat refFormat) {
        this(prop, type, mask, null, refFormat);
    }    
    
    @Override
    public final void changeValueType(final EValType newValType, final EditMask newMask){
        proxyPropEditorController.changeValueType(newValType, newMask);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    protected void updateEditor(final Object value, final PropEditorOptions options) {// value - String        
        final List<Object> values = proxyPropEditorController.updateEditor(value, null, options);
        if (values!=null){
            super.updateEditor(values.get(0), options);
        }
    }

    @Override
    protected EnumSet<ETextOptionsMarker> getTextOptionsMarkers(final EnumSet<ETextOptionsMarker> valEditorMarkers) {
        final EnumSet<ETextOptionsMarker> markers = super.getTextOptionsMarkers(valEditorMarkers);
        return proxyPropEditorController==null ? markers : proxyPropEditorController.getTextOptionsMarkers(markers);
    }
            
    @Override
    protected void updateSettings() {
        if (!isInErrorMode()){
            super.updateSettings();
        }
    }

    @Override
    protected Object getCurrentValueInEditor() {
        if (isInErrorMode()){
            return ((ExceptionView)getValEditor()).getStringToShow();
        }else{
            return proxyPropEditorController.writeValueToString(super.getCurrentValueInEditor());
        }
    }

    @Override
    public void bind() {
        wasBinded = true;
        super.bind();
    }    

    @Override
    public EditMask getEditMask() {
        return EditMask.newCopy(editMask);
    }

    @Override
    public EValType getValueType() {
        return evalType;
    }
    
    @Override
    public final boolean isInErrorMode(){
        return proxyPropEditorController.isInErrorMode();
    }

    @Override
    public final IValAsStrConverter getValueConverter() {
        return proxyPropEditorController.getValueConverter();
    }

    @Override
    public final void setValueConverter(final IValAsStrConverter converter) {
        proxyPropEditorController.setValueConverter(converter);
    }

    @Override
    public final EReferenceStringFormat getReferenceValueFormat() {
        return proxyPropEditorController.getReferenceFormat();
    }

    @Override
    public final void setReferenceValueFormat(final EReferenceStringFormat format) {
        proxyPropEditorController.setReferenceFormat(format);
    }        
}
