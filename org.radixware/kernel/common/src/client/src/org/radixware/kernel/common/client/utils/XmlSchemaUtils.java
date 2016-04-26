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

package org.radixware.kernel.common.client.utils;

import java.util.ArrayList;
import java.util.List;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaTypeSystem;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;


public final class XmlSchemaUtils {

    public static boolean canCreateNewDocument(ClassLoader classLoader, final String schemaDocStr) {
        final SchemaTypeSystem typeSystem =
                XmlSchemaTypeSystemCompiler.compile(classLoader, schemaDocStr, new ArrayList<String>());
        return typeSystem != null
                && typeSystem.documentTypes() != null
                && typeSystem.documentTypes().length > 0;
    }

    public static boolean isValidDocument(final XmlObject xml, final SchemaTypeSystem typeSystem) {
        if (xml == null) {
            return false;
        }
        final XmlCursor xmlCursor = xml.newCursor();
        final SchemaType doctype;
        try {
            doctype = typeSystem.findDocumentType(xmlCursor.getName());
        } finally {
            xmlCursor.dispose();
        }
        try {
            return typeSystem.parse(xml.getDomNode(), doctype, new XmlOptions()) != null;
        } catch (XmlException xmle) {
            return false;
        }
    }

    public static boolean isValidDocument(ClassLoader classLoader, final XmlObject xml, final String schemaDocStr, final List<String> errors) {
        if (xml == null) {
            return false;
        }
        final SchemaTypeSystem typeSystem =
                XmlSchemaTypeSystemCompiler.compile(classLoader, schemaDocStr, errors);
        if (typeSystem == null){
            return false;
        }
        if (typeSystem.documentTypes()==null || typeSystem.documentTypes().length==0){
            if (errors!=null)
                errors.add("No document types found in xml schema");
            return false;
        }
        final XmlCursor xmlCursor = xml.newCursor();
        xmlCursor.toStartDoc();
        xmlCursor.toNextToken();
        final SchemaType doctype;
        try {
            doctype = typeSystem.findDocumentType(xmlCursor.getName());
            if (doctype==null){
                if (errors!=null)
                    errors.add("No document type found for "+xmlCursor.getName()+" type");
                return false;
            }
        } finally {
            xmlCursor.dispose();
        }
        try {
            return typeSystem.parse(xml.getDomNode(), doctype, new XmlOptions()) != null;
        } catch (XmlException xmle) {
            if (errors != null) {
                errors.add(xmle.getMessage());
            }
            return false;
        }
    }
}
