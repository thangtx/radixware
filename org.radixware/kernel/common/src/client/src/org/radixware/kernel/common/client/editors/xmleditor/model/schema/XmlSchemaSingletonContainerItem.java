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
import org.apache.xmlbeans.SchemaParticle;


final class XmlSchemaSingletonContainerItem extends XmlSchemaContainerItem {

    private final XmlSchemaElementItem childItem;

    public XmlSchemaSingletonContainerItem(final SchemaParticle particle, final XmlSchemaElementItem item) {
        this(particle, item, null);
    }

    public XmlSchemaSingletonContainerItem(final SchemaParticle particle, final XmlSchemaElementItem item, final String nameSpace) {
        super(particle, nameSpace);
        childItem = item;
    }

    @Override
    public boolean hasChildren() {
        return true;
    }

    @Override
    public List<XmlSchemaItem> getChildItems() {
        return Collections.<XmlSchemaItem>singletonList(childItem);
    }
}