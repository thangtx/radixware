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

package org.radixware.kernel.common.client.models.items.properties;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.client.meta.RadPropertyDef;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.utils.ClientValueFormatter;
import org.radixware.kernel.common.client.utils.ValueConverter;
import org.radixware.kernel.common.client.views.IArrayEditorDialog;
import org.radixware.kernel.common.client.views.IPropEditor;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Arr;
import org.radixware.kernel.common.utils.FileUtils;

public abstract class PropertyArr<T extends Arr> extends SimpleProperty<T> {

    private boolean itemMandatory;
    private List<Object> predefinedArrayItemValues;
    private final int firstArrayItemIndex;
    private final int minArrayItemsCount;
    private final int maxArrayItemsCount;
    

    public PropertyArr(final Model owner, final RadPropertyDef propDef) {
        super(owner, propDef);
        itemMandatory = propDef.isArrayItemMandatory();
        this.firstArrayItemIndex = propDef.getFirstArrayItemIndex();
        this.minArrayItemsCount = propDef.getMinArrayItemsCount();
        this.maxArrayItemsCount = propDef.getMaxArrayItemsCount();
        
    }

    @Override
    public IPropEditor createPropertyEditor() {
        return getEnvironment().getApplication().getStandardViewsFactory().newPropArrEditor(this);
    }

    @SuppressWarnings("unchecked")
    public IArrayEditorDialog getEditorDialog(final IWidget parent) {
        final Arr value = getValueObject() != null ? (Arr) getValueObject() : null;
        return getEditorDialog(parent, value);
    }

    public IArrayEditorDialog getEditorDialog(final IWidget parent, final Arr value) {
        final IArrayEditorDialog dialog;
        final IWidget parentWidget;
        if (parent != null) {
            parentWidget = parent;
        } else if (owner.getView() != null) {
            parentWidget = (IWidget) owner.getView();
        } else {
            parentWidget = getEnvironment().getMainWindow();
        }
        dialog = getEnvironment().getApplication().getStandardViewsFactory().newArrayEditorDialog(this, parentWidget);
        dialog.setCurrentValue(value);
        if (RadPropertyDef.isPredefinedValuesSupported(getType().getArrayItemType(), getEditMask().getType())){
            dialog.setPredefinedValues(getPredefinedValuesForArrayItem());
        }        
        dialog.setWindowTitle(ClientValueFormatter.capitalizeIfNecessary(getEnvironment(), getTitle()));
        return dialog;
    }

    @Override
    protected Object getValObjectImpl() {
        if (needForActivation()) {
            activate();
        }
        return internalValue.getCopyOfValue();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void setInternalVal(final T value) {
        super.setInternalVal((T) PropertyValue.copyValue(value, getDefinition().getType()));
    }

    public boolean isArrayItemMandatory() {
        return itemMandatory;
    }

    public void setArrayItemMandatory(final boolean isMandatory) {
        itemMandatory = isMandatory;
    }
    
    @SuppressWarnings("unchecked")
    public void setPredefinedValuesForArrayItem(final List<Object> values){
        if (RadPropertyDef.isPredefinedValuesSupported(getType().getArrayItemType(), getEditMask().getType())){
            if (values==null){
                predefinedArrayItemValues = null;
            }
            else{
                predefinedArrayItemValues = new LinkedList<>();
                for (Object value: values){
                    predefinedArrayItemValues.add(PropertyValue.copyValue(value, getType().getArrayItemType()));
                }
            }            
            if (getSynchronizedProperty() != null) {
                ((PropertyArr)getSynchronizedProperty()).predefinedArrayItemValues = predefinedArrayItemValues;
            }
        }
        else{
            throw new UnsupportedOperationException("Predefined array item values is not supported for this property");
        }        
    }
    
    public List<Object> getPredefinedValuesForArrayItem(){
        if (predefinedArrayItemValues==null){
            return null;
        }
        else{
            final List<Object> result = new LinkedList<>();
            for (Object value: predefinedArrayItemValues){
                result.add(PropertyValue.copyValue(value, getType().getArrayItemType()));
            }
            return Collections.unmodifiableList(result);
        }     
    }
    
    public int getFirstArrayItemIndex() {
        return this.firstArrayItemIndex;
    }
    
    public int getMinArrayItemsCount() {
        return this.minArrayItemsCount;
    }
    
    public int getMaxArrayItemsCount() {
        return this.maxArrayItemsCount;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getInitialValue() {
        return (T)initialValue.getCopyOfValue();
    }


    public Object loadItemFromStream(final InputStream input, final int index) throws IOException {
        EValType valType = ValueConverter.serverValType2ClientValType(getType());
        EValType type = valType.isArrayType() ? valType.getArrayItemType() : valType;
        String retVal = null;
        try {
            retVal = FileUtils.readTextStream(input, FileUtils.XML_ENCODING);
        } catch (IOException ex) {
            Logger.getLogger(PropertyArr.class.getName()).log(Level.SEVERE, null, ex);
        }        
        return ValAsStr.fromStr(retVal, type);        
    }


    public void saveItemToStream(final OutputStream output, final Object value, final int index) throws IOException {
        if (value !=null){
            EValType valType = ValueConverter.serverValType2ClientValType(getType());
            EValType type = valType.isArrayType() ? valType.getArrayItemType() : valType;
            String strValue = ValAsStr.toStr(value, type);
            try {
                FileUtils.writeString(output, strValue, FileUtils.XML_ENCODING);
            } catch (IOException ex) {
                Logger.getLogger(PropertyArr.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
