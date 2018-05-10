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
import org.radixware.kernel.common.client.views.IPropLabel;
import org.radixware.kernel.common.client.views.IProxyPropEditor;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Arr;
import org.radixware.wps.rwt.Label;
import org.radixware.wps.views.editors.valeditors.ExceptionViewController;
import org.radixware.wps.views.editors.valeditors.IValEditor;
import org.radixware.wps.views.editors.valeditors.LabelFactory;
import org.radixware.wps.views.editors.valeditors.ValArrEditorController;
import org.radixware.wps.views.editors.valeditors.ValDateTimeEditorController;
import org.radixware.wps.views.editors.valeditors.ValEditorController;
import org.radixware.wps.views.editors.valeditors.ValEditorFactory;


public  class ProxyPropEditor extends PropEditor implements IProxyPropEditor{
    
    private static class InternalLabelFactory extends LabelFactory{
        
        private final Property property;
        
        public InternalLabelFactory(final Property property){
            this.property = property;
        }

        @Override
        public Label createLabel() {
            IPropLabel label = property.createPropertyLabel();
            if (label instanceof PropLabel == false) {
                label = new PropLabel(property);
            }
            label.bind();
            return (PropLabel) label;
        }
    }
    
    private static class InternalValEditorFactory extends ValEditorFactory{
        
        private final EValType valType;
        private final EditMask editMask;
        private final ValEditorFactory delegate;
        private final Throwable exception;
        final LabelFactory labelFactory;
        private final String value;
                
        
        public InternalValEditorFactory(final EValType valType, final EditMask editMask, final Property property){
            this.valType = valType;
            this.editMask = editMask;
            labelFactory = null;
            delegate = new ValEditorFactory.DefaultValEditorFactory(new InternalLabelFactory(property));
            exception = null;
            value = null;
        }
        
        public InternalValEditorFactory(final Throwable exception, final String value, final Property property){
            this.valType = null;
            this.editMask = null;
            delegate = null;
            labelFactory = new InternalLabelFactory(property);
            this.exception = exception;
            this.value = value;            
        }

        @Override
        public IValEditor<?, ?> createValEditor(final EValType valType, final EditMask editMask, final IClientEnvironment env) {
            if (exception==null){
                return delegate.createValEditor(this.valType, this.editMask, env);
            }else{
                final ExceptionViewController exceptionView = new ExceptionViewController(env,labelFactory);
                exceptionView.setValue(exception);
                exceptionView.setStringToShow(value);
                exceptionView.setExceptionDetailsBtnVisible(false);
                exceptionView.setValidationResult(ValidationResult.Factory.newInvalidResult(InvalidValueReason.Factory.createForWrongFormatValue(env)));
                return exceptionView.getValEditor();
            }
        }        
    }

    private EditMask editMask;
    private EValType evalType;
    private boolean wasBinded;
    private final ProxyPropEditorController proxyPropEditorController;
    
    @SuppressWarnings("unchecked")
    private ProxyPropEditor(final Property prop, 
                            final EValType type, 
                            final EditMask mask,
                            final IValAsStrConverter converter,
                            final EReferenceStringFormat format) {
        super(prop, new InternalValEditorFactory(type, mask, prop));
        evalType = type;
        editMask = EditMask.newCopy(mask);        
        proxyPropEditorController = new ProxyPropEditorController(getEnvironment(), this, converter, format) {
            
            @Override
            protected void updateErrorView(final Throwable error, final String value) {
                final ExceptionViewController editor = (ExceptionViewController)getValEditor();
                editor.setStringToShow(value);
                editor.setValue(error);
            }
            
            @Override
            protected void switchToErrorView(final Throwable error, final String value) {
                changeValEditor(new InternalValEditorFactory(error, value, getProperty()));                
            }
            
            @Override
            protected void switchToEditor(final EValType valType, final EditMask mask) {
                changeValEditor(new InternalValEditorFactory(valType, mask, getProperty()));
            }
            
            @Override
            protected void setEditMask(final EditMask mask) {
                editMask = EditMask.newCopy(mask);
            }
            
            @Override
            protected void setValueType(final EValType valueType) {
                evalType = valueType;
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
    
    public ProxyPropEditor(final Property prop, final EValType type, final EditMask mask){
        this(prop,type,mask,null,null);
    }
    
    public ProxyPropEditor(final Property prop, final EValType type, final EditMask mask, final IValAsStrConverter converter){
        this(prop,type,mask,converter,null);
    }    
    
    public ProxyPropEditor(final Property prop, final EValType type, final EditMask mask, final EReferenceStringFormat format){
        this(prop,type,mask,null,format);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void updateEditor(final Object value, final Object initialValue, final PropEditorOptions options) {// value - String
        final List<Object> values = proxyPropEditorController.updateEditor(value, initialValue, options);
        if (values!=null){
            super.updateEditor(values.get(0), values.get(1), options);
            if (getValueType().isArrayType() && getValEditor() instanceof ValArrEditorController){
                final ValArrEditorController editorController = (ValArrEditorController) getValEditor();
                editorController.setItemEditMask(getEditMask());                
                if (options.isReadOnly()) {
                    final Arr currentValue = (Arr)editorController.getValue();
                    editorController.setEditButtonVisible(currentValue != null
                            && !currentValue.isEmpty()
                            && !getProperty().isCustomEditOnly());
                } else {
                    editorController.setEditButtonVisible(true);
                }
            }
            updateDialogTitle();
        }
    }
    
    private void updateDialogTitle(){
        final ValEditorController editor = getValEditor();
        if (editor instanceof ValArrEditorController){
            ((ValArrEditorController)editor).setDialogTitle(getProperty().getTitle());
        }else if (editor instanceof ValDateTimeEditorController){
            ((ValDateTimeEditorController)editor).setDialogTitle(getProperty().getTitle());
        }
    }

    @Override
    protected Object getCurrentValueInEditor() {
        if (isInErrorMode()){
            return ((ExceptionViewController)getValEditor()).getStringToShow();
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
    public final void changeValueType(final EValType newValType, final EditMask newMask){
        proxyPropEditorController.changeValueType(newValType, newMask);
    }
    
    @Override
    protected EnumSet<ETextOptionsMarker> getTextOptionsMarkers(final EnumSet<ETextOptionsMarker> valEditorMarkers) {
        final EnumSet<ETextOptionsMarker> markers =  super.getTextOptionsMarkers(valEditorMarkers);
        return proxyPropEditorController==null ? markers : proxyPropEditorController.getTextOptionsMarkers(markers);
    }    
    
    @Override
    protected void updateSettings() {
        if (!isInErrorMode()){
            super.updateSettings();
        }
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
