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
import java.util.List;
import org.apache.xmlbeans.SchemaParticle;
import org.apache.xmlbeans.SchemaType;
import org.radixware.kernel.common.client.editors.xmleditor.model.IXmlValueEditingOptionsProvider;
import org.radixware.kernel.common.client.editors.xmleditor.view.XmlValueEditingOptions;


public abstract class XmlSchemaItem {

    protected final SchemaParticle particle;
    private final String nameSpace;
    private List<XmlSchemaItem> childItems;

    public XmlSchemaItem(final SchemaParticle particle) {
        this(particle, null);        
    }

    public XmlSchemaItem(final SchemaParticle particle, final String nameSpace) {        
        this.particle = particle;
        this.nameSpace = nameSpace;
    }

    public abstract XmlValueEditingOptions getValueEditingOptions(final IXmlValueEditingOptionsProvider provider);

    public abstract boolean isValueTypeDefined();

    protected final String getNameSpace() {
        return nameSpace;
    }

    public List<XmlSchemaItem> getChildItems() {
        if (childItems == null) {
            childItems = new ArrayList<>();
            final SchemaType particleType = particle.getType();
            final SchemaParticle particleContentModel = particleType == null ? null : particleType.getContentModel();
            if (particleType != null && particleContentModel != null) {
                final XmlSchemaItem schemaItem = newInstance(particle.getParticleType(), particleContentModel, nameSpace);
                if (schemaItem != null) {
                    if (particleContentModel.getParticleType() == SchemaParticle.ELEMENT
                            && schemaItem instanceof XmlSchemaElementItem) {
                        childItems.add(new XmlSchemaSingletonContainerItem(particle, (XmlSchemaElementItem) schemaItem, nameSpace));
                    } else {
                        childItems.add(schemaItem);
                    }
                }
            } else {
                addChildItems(particle, childItems, nameSpace);
            }
        }
        return childItems;
    }

    private static void addChildItems(final SchemaParticle particle, final List<XmlSchemaItem> childItems, final String nameSpace) {
        for (int i = 0; i < particle.countOfParticleChild(); i++) {
            final XmlSchemaItem childItem = newInstance(particle.getParticleType(), particle.getParticleChild(i), nameSpace);
            if (childItem != null) {
                childItems.add(childItem);
            }
        }
    }

    private static XmlSchemaItem newInstance(final int parentParticalType, final SchemaParticle particle, final String nameSpace) {
        final int type = particle.getParticleType();
        switch (type) {
            case SchemaParticle.ELEMENT:
                if (particle.getMaxOccurs() == null
                    || particle.getMaxOccurs().intValue() > 1) {
                    return new XmlSchemaElementsListItem(particle,nameSpace);
                } else {
                    return new XmlSchemaElementItem(particle, nameSpace);
                }
            case SchemaParticle.CHOICE:
                return new XmlSchemaChoiceItem(particle,nameSpace);
            case SchemaParticle.ALL:
            case SchemaParticle.SEQUENCE:
                return new XmlSchemaContainerItem(particle,nameSpace);
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        return particle==null || particle.getName()==null ? "any type" : particle.getName().toString();
    }
        
}
