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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaLocalAttribute;
import org.apache.xmlbeans.SchemaParticle;
import org.apache.xmlbeans.SchemaType;
import org.radixware.kernel.common.client.editors.xmleditor.model.IXmlValueEditingOptionsProvider;
import org.radixware.kernel.common.client.editors.xmleditor.view.XmlValueEditingOptions;


public final class XmlSchemaElementItem extends XmlSchemaItem implements IXmlSchemaNamedItem, IXmlSchemaItemWithAttributes {

    public XmlSchemaElementItem(final SchemaParticle particle) {
        this(particle, null);
    }

    public XmlSchemaElementItem(final XmlSchemaElementsListItem listItem) {
        this(listItem.particle, null);
    }

    public XmlSchemaElementItem(final XmlSchemaElementsListItem listItem, final String nameSpace) {
        this(listItem.particle, nameSpace);
    }

    public XmlSchemaElementItem(final SchemaParticle particle, final String nameSpace) {
        super(particle, nameSpace);
    }

    @Override
    public String getLocalName() {
        return particle.getName().getLocalPart();
    }

    @Override
    public QName getFullName() {
        if (getNameSpace() == null || getNameSpace().isEmpty()) {
            return particle.getName();
        } else {
            return new QName(getNameSpace(), getLocalName());
        }
    }
    
    @Override
    public List<XmlSchemaAttributeItem> getAttributes() {
        List<XmlSchemaAttributeItem> attributeItems;
        attributeItems = new ArrayList<>();
        if (particle.getType().getAttributeModel() == null) {
            return Collections.emptyList();
        } else {
            for (SchemaLocalAttribute i : particle.getType().getAttributeModel().getAttributes()) {
                XmlSchemaAttributeItem attr = new XmlSchemaAttributeItem(i);
                attributeItems.add(attr);
            }
            return attributeItems;
        }
    }

    public int getMaxElementsCount() {
        return particle.getMaxOccurs() == null ? -1 : particle.getMaxOccurs().intValue();
    }

    public int getMinElementsCount() {
        return particle.getMinOccurs() == null ? 0 : particle.getMinOccurs().intValue();
    }

    public boolean isAnyType() {
        if (particle.getType().isURType()) {
            return true;
        }
        return false;
    }

    public boolean isNillable() {
        if (particle.isNillable()) {
            return true;
        }
        return false;
    }

    @Override
    public XmlValueEditingOptions getValueEditingOptions(final IXmlValueEditingOptionsProvider provider) {
        if (isAnyType()) {
            return provider.getEditingOptions(null, null, false, !isNillable());
        } else {
            final SchemaType type = particle.getType();
            final SchemaType primitiveType = type.isSimpleType() ? type : type.getPrimitiveType();
            if (primitiveType != null && primitiveType.getSimpleVariety() == SchemaType.UNION) {
                return provider.getEditingOptions(null, null, false, true);
            } else if (primitiveType != null && primitiveType.isSimpleType()) {
                return provider.getEditingOptions(type, null, false, !particle.isNillable());
            } else {
                return null;
            }
        }
    }

    @Override
    public boolean isValueTypeDefined() {
        if (isAnyType()) {
            return true;
        } else {
            final SchemaType type = particle.getType();
            final SchemaType primitiveType = type.getPrimitiveType();
            if (primitiveType == null) {
                return type.getSimpleVariety() == SchemaType.ATOMIC
                        || type.getSimpleVariety() == SchemaType.LIST
                        || type.getSimpleVariety() == SchemaType.UNION;
            } else {
                return primitiveType.isSimpleType() && primitiveType.getSimpleVariety() == SchemaType.ATOMIC;
            }
        }
    }
}