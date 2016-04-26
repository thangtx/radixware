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

package org.radixware.kernel.common.client.editors.xmleditor.model.schema;

import java.util.Collections;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaLocalAttribute;
import org.radixware.kernel.common.client.editors.xmleditor.model.IXmlValueEditingOptionsProvider;
import org.radixware.kernel.common.client.editors.xmleditor.view.XmlValueEditingOptions;


public final class XmlSchemaAttributeItem extends XmlSchemaItem implements IXmlSchemaNamedItem {

    private final SchemaLocalAttribute attribute;

    public XmlSchemaAttributeItem(final SchemaLocalAttribute attribute) {
        this(attribute, null);
    }

    public XmlSchemaAttributeItem(final SchemaLocalAttribute attribute, final String nameSpace) {
        super(null, nameSpace);
        this.attribute = attribute;
    }

    @Override
    public List<XmlSchemaItem> getChildItems() {
        return Collections.emptyList();
    }

    @Override
    public String getLocalName() {
        return attribute.getName().getLocalPart();
    }

    @Override
    public QName getFullName() {
        if (getNameSpace() == null || getNameSpace().isEmpty()) {
            return attribute.getName();
        } else {
            return new QName(getNameSpace(), getLocalName());
        }
    }

    public boolean usedAttribute() {
        return attribute.getUse() == 3;
    }

    @Override
    public XmlValueEditingOptions getValueEditingOptions(final IXmlValueEditingOptionsProvider provider) {
        return provider.getEditingOptions(attribute.getType(), attribute.getAnnotation(), false, true);
    }

    @Override
    public boolean isValueTypeDefined() {
        return true;
    }
}
