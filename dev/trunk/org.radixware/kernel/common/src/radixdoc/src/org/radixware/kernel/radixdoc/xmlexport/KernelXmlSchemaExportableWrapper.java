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
package org.radixware.kernel.radixdoc.xmlexport;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.XmlUtils;
import org.w3c.dom.Node;

public abstract class KernelXmlSchemaExportableWrapper implements IExportableXmlSchema {

    private final XmlObject schema;
    private final String name;

    public KernelXmlSchemaExportableWrapper(XmlObject schema, String name) {
        this.schema = schema;
        this.name = name;
    }

    @Override
    public Id getId() {
        return null;
    }

    @Override
    public Collection<IExportableXmlSchema> getLinkedSchemas() {
        List<IExportableXmlSchema> result = new ArrayList<>();
        if (schema == null) {
            return result;
        }
        List<XmlUtils.Namespace2Location> imports = XmlUtils.getImportedNamespaces2Loc(schema, false);

        for (XmlUtils.Namespace2Location imp : imports) {
            try {
                URI uri = new URI(imp.location);
                File file = new File(uri.getPath());
                
                String schemaName = file.getName();
                XmlObject obj = findKernelSchema(schemaName, imp.namespace);
                result.add(new KernelXmlSchemaExportableWrapper(obj, schemaName) {
                    
                    @Override
                    public XmlObject findKernelSchema(String name, String namespace) {
                        return KernelXmlSchemaExportableWrapper.this.findKernelSchema(name, namespace);
                    }
                });
            } catch (URISyntaxException ex) {
                Logger.getLogger(KernelXmlSchemaExportableWrapper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    @Override
    public InputStream getDefinitionInputStream() {
        return null;
    }

    @Override
    public InputStream getInputStream() {
        return schema == null ? null : schema.newInputStream();
    }

    @Override
    public Map<Id, String> getLocalizedStrings(EIsoLanguage lang) {
        return new HashMap<>();
    }

    @Override
    public void processEnumerations(Node node) {
    }

    @Override
    public Collection<Id> getEnumIds() {
        return new ArrayList<>();
    }

    @Override
    public String getTitle() {
        return name;
    }

    @Override
    public Object getUserObject() {
        return schema;
    }

    @Override
    public String getLocation() {
        return null;
    }

    @Override
    public Icon getIcon() {
        return null;
    }

    @Override
    public Icon getLocationIcon() {
        return null;
    }
}
