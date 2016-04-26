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

package org.radixware.kernel.common.client.editors.xmleditor.model;

import java.util.LinkedList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaTypeSystem;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.radixware.kernel.common.client.editors.xmleditor.model.schema.XmlSchemaElementItem;
import org.radixware.kernel.common.client.editors.xmleditor.model.schema.XmlSchemaItem;


public class XmlDocument {

    private XmlDocumentItem root;
    private XmlObject doc;

    public XmlDocument(final XmlObject document) {
        this.doc = document;
        initRootItem();
    }

    public XmlDocument(final SchemaType schemaType) {
        final XmlObject newXml = XmlObject.Factory.newInstance();
        final XmlCursor cursor = newXml.newCursor();
        try {
            cursor.toNextToken();
            cursor.beginElement(schemaType.getDocumentElementName());
        } finally {
            cursor.dispose();
        }
        doc = newXml.changeType(schemaType);
        initRootItem();
    }

    public XmlDocument(final QName name, final String value) {
        final XmlObject newXml = XmlObject.Factory.newInstance();
        final XmlCursor cursor = newXml.newCursor();
        try {
            cursor.toNextToken();
            cursor.beginElement(name);
            if (value!= null && !value.isEmpty()) {
                cursor.toPrevToken();
                cursor.setTextValue(value);
                cursor.toNextToken();
            }
        } finally {
            cursor.dispose();
        }
        doc = newXml;
        initRootItem();
    }

    private void initRootItem() {
        final XmlCursor cursor = doc.newCursor();
        try {
            cursor.toFirstChild();            
            root = new XmlDocumentItem(cursor.getObject(), EXmlItemType.Element);
        } finally {
            cursor.dispose();
        }
    }

    public void typify(final SchemaTypeSystem types) throws XmlException {
        SchemaType xsdType;
        final XmlCursor cursor = doc.newCursor();
        try {
            xsdType = types.findDocumentType(cursor.getName());
        } finally {
            cursor.dispose();
        }
        doc = types.parse(doc.xmlText(), xsdType, new XmlOptions());
        initRootItem();
    }

    public XmlDocumentItem getRootElement() {
        return root;
    }

    public XmlSchemaItem getRootSchemaItem() {
        final SchemaType schemaType = doc.schemaType();
        if (schemaType != null
            && !schemaType.isNoType()
            && (!schemaType.isBuiltinType() || schemaType.getBuiltinTypeCode()!=SchemaType.BTC_ANY_TYPE)
            && schemaType.getContentModel() != null) {
            return new XmlSchemaElementItem(schemaType.getContentModel());
        }
        return null;

    }

    public String getXmlText() {        
        return doc.xmlText();
    }
    
    public String getXmlText(XmlOptions options) {
        return doc.xmlText(options);
    }    

    public SchemaType getSchemaType() {
        return doc.schemaType();
    }
    
    public XmlObject asXmlObject(){
        return doc;
    }
    
    public List<XmlError> validate(){
        final List<XmlError> errors = new LinkedList<>();
        final XmlOptions options = new XmlOptions();
        options.setErrorListener(errors);
        doc.validate(options);        
        return errors;
    }
}
