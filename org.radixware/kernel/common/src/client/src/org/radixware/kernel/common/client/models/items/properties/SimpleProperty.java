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
import org.radixware.kernel.common.enums.EFileDialogOpenMode;
import org.radixware.kernel.common.client.dialogs.FileDialogSettings;
import org.radixware.kernel.common.client.dialogs.IFileDialogSettings;
import org.radixware.kernel.common.client.meta.RadPropertyDef;
import org.radixware.kernel.common.client.meta.mask.EditMaskFilePath;

import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.utils.ValueConverter;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EFileSelectionMode;
import org.radixware.kernel.common.enums.EMimeType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.utils.FileUtils;

public abstract class SimpleProperty<E> extends Property {

    public SimpleProperty(final Model owner, final RadPropertyDef propDef) {
        super(owner, propDef);
    }

//	неопубликованный метод, только для model.setter
    protected void setInternalVal(final E x) {
        if (internalValue.hasSameValue(x) && hasOwnValue()) {
            return;
        }
        setInternalValObject(x);
        if (getSynchronizedProperty() != null) {
            getSynchronizedProperty().setValObjectImpl(x);
        }
    }

    /**
     * Вызывается в сгенерированном геттере свойства, в случае когда необходимо типизировать текущее
     * значение набором констант.
     *
     * @param value - типизированное значение свойства
     */
    protected void refineValue(final E value) {
        internalValue.refineValue(value);
        initialValue.refineValue(value);
        if (serverValue != null) {
            serverValue.refineValue(value);
        }
    }

    @Override
    public Object getServerValObject() {
        if (isLocal()) {
            throw new IllegalUsageError("This property cannot have server value (you may want to use getInitialValue method)");
        }
        if (!isActivated()) {
            activate();
        }
        return serverValue != null ? serverValue.getValue() : null;
    }

    @Override
    protected Object getValObjectImpl() {
        if (needForActivation()) {
            activate();
        }
        return internalValue.getValue();
    }

    @Override
    @SuppressWarnings("unchecked")
    public E getInitialValue() {
        return (E) super.getInitialValue();
    }

    @SuppressWarnings("unchecked")
    public void setInitialValue(final E value) {
        super.setInitialValObject(value);
        if (getSynchronizedProperty() != null) {
            ((SimpleProperty) getSynchronizedProperty()).setInitialValue(value);
        }
    }

    public IFileDialogSettings getFileDialogSettings(final EFileDialogOpenMode openMode) {
        if (getEditMask() instanceof EditMaskFilePath) {
            return new FileDialogSettings(getEnvironment(), (EditMaskFilePath)getEditMask());
        }
        return new FileDialogSettings(openMode);
    }
    
    public void saveToStream(final OutputStream output, final E value) throws IOException {
        if (value !=null){
            final EValType valType = ValueConverter.serverValType2ClientValType(getType());
            final String strValue = ValAsStr.toStr(value, valType);
            FileUtils.writeString(output, strValue, FileUtils.XML_ENCODING);
        }
    }

    @SuppressWarnings("unchecked")
    public E loadFromStream(final InputStream input) throws IOException {
        final EValType valType = ValueConverter.serverValType2ClientValType(getType());
        final String retVal = FileUtils.readTextStream(input, FileUtils.XML_ENCODING);
        return (E)ValAsStr.fromStr(retVal, valType);        
    }
}