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

import java.util.List;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaParticle;
import org.radixware.kernel.common.client.editors.xmleditor.model.XmlModelItem;


public class XmlSchemaElementsListItem extends XmlSchemaContainerItem implements IXmlSchemaNamedItem {

    public XmlSchemaElementsListItem(final SchemaParticle particle) {
        this(particle, null);
    }

    public XmlSchemaElementsListItem(final SchemaParticle particle, final String nameSpace) {
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

    public int getMaxElementsCount() {
        return particle.getMaxOccurs() == null ? -1 : particle.getMaxOccurs().intValue();
    }

    public int getMinElementsCount() {
        return particle.getMinOccurs() == null ? 0 : particle.getMinOccurs().intValue();
    }

    public int childWithEqualName(final XmlModelItem item) {
        int countOfChildWithEqualName = 0;
        final List<QName> childs = item.getParent().getExistingElements();
        for (QName child : childs) {
            if (item.getXmlNode().getFullName().equals(child)) {
                countOfChildWithEqualName++;
            }
        }
        return countOfChildWithEqualName;
    }

    public boolean isAnyType() {
        if (particle.getType().isURType()) {
            return true;
        }
        return false;
    }
}