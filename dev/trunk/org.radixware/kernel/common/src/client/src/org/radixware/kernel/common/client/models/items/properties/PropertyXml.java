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
import java.util.List;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.meta.RadPropertyDef;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.views.IPropEditor;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.XmlObjectProcessor;

public class PropertyXml extends SimpleProperty<XmlObject> {

    private String visibleValue;
    private Id schemaId;

    public PropertyXml(final Model owner, final RadPropertyDef propDef) {
        super(owner, propDef);
    }

    @Override
    public final RadPropertyDef getDefinition() {
        return super.getDefinition();
    }

    @Override
    public void setServerValue(final PropertyValue propertyValue) {
        if (propertyValue != null) {
            propertyValue.refineValue(castValue(propertyValue.getValue()));
        }
        super.setServerValue(propertyValue);
    }

    public Id getSchemaId() {
        if (schemaId == null && getValClass() != XmlObject.class) {
            schemaId = getEnvironment().getDefManager().getRepository().findIdByExecutableClassName(getValClass().getName());
        }
        return schemaId;
    }

    @Override
    public IPropEditor createPropertyEditor() {
        return getEnvironment().getApplication().getStandardViewsFactory().newPropXmlEditor(this);
    }

    @Override
    public Class<? extends XmlObject> getValClass() {
        return XmlObject.class;
    }

    @Override
    public void setValObjectImpl(final Object x) {
        setInternalVal((XmlObject) x);
    }

    @Override
    public String getValueAsString() {
        final Object o = this.getValueObject();
        if (o == null) {
            return getEditMask().getNoValueStr(getEnvironment().getMessageProvider());
        }
        final XmlObject xml = (XmlObject) o;
        if (xml.isNil()) {
            return getEditMask().getNoValueStr(getEnvironment().getMessageProvider());
        }
        if (visibleValue==null){
            visibleValue = getValClass() == XmlObject.class ? "<Xml>" : "<" + getValClass().getSimpleName() + ">";
        }
        return visibleValue;
    }

    public XmlObject castValue(final Object value) {
        if (value != null) {
            final XmlObject xml = (XmlObject) value;
            if (!xml.isNil()) {
                if (getValClass() == XmlObject.class) {
                    return XmlObjectProcessor.toSeparateXml(xml);
                } else {
                    return XmlObjectProcessor.castToXmlClass((ClassLoader) getEnvironment().getDefManager().getClassLoader(), xml, getValClass());//NOPMD call of getClassLoader not for Class instance
                }
            }
        }
        return value != null ? (XmlObject) value : null;
    }

    @Override
    public final EValType getType() {
        return EValType.XML;
    }

    @Override
    protected Object getValObjectImpl() {
        if (needForActivation()) {
            activate();
        }
        return internalValue.getCopyOfValue();
    }

    @Override
    public XmlObject getInitialValue() {
        return (XmlObject) initialValue.getCopyOfValue();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void setInternalVal(final XmlObject x) {
        super.setInternalVal((XmlObject) PropertyValue.copyValue(x, getDefinition().getType()));
    }

    @Override
    public void setPredefinedValues(final List<Object> values) {
        throw new UnsupportedOperationException("Predefined values is not supported for xml properties");
    }

    @Override
    public void saveToStream(final OutputStream output, final XmlObject value) throws IOException {
        if (value != null) {
            FileUtils.writeString(output, value.xmlText(), FileUtils.XML_ENCODING);
        }
    }

    @Override
    public XmlObject loadFromStream(final InputStream input) throws IOException {
        try {
            final XmlObject xmlObject = XmlObject.Factory.parse(input);
            return castValue(xmlObject);
        } catch (XmlException ex) {
            throw new IOException(ex);
        }
    }
}
