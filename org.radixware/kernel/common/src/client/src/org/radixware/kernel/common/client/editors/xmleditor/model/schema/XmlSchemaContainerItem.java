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

import org.apache.xmlbeans.SchemaParticle;
import org.radixware.kernel.common.client.editors.xmleditor.model.IXmlValueEditingOptionsProvider;
import org.radixware.kernel.common.client.editors.xmleditor.view.XmlValueEditingOptions;


public class XmlSchemaContainerItem extends XmlSchemaItem {

    public XmlSchemaContainerItem(final SchemaParticle particle) {
        this(particle, null);
    }

    public XmlSchemaContainerItem(final SchemaParticle particle, final String nameSpace) {
        super(particle, nameSpace);
    }

    public boolean isSequence() {
        return (particle.getParticleType() == 3);
    }

    public boolean hasChildren() {
        if (particle.countOfParticleChild() > 0) {
            return true;
        }
        return false;

    }

    @Override
    public XmlValueEditingOptions getValueEditingOptions(final IXmlValueEditingOptionsProvider provider) {
        return null;
    }

    @Override
    public boolean isValueTypeDefined() {
        return false;
    }
}
