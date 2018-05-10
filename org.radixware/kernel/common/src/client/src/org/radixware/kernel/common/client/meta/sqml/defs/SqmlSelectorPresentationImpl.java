/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.common.client.meta.sqml.defs;

import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.meta.sqml.ISqmlSelectorPresentationDef;
import org.radixware.kernel.common.types.Id;
import org.xml.sax.Attributes;


final class SqmlSelectorPresentationImpl extends SqmlDefinitionImpl implements ISqmlSelectorPresentationDef{
    
    SqmlClassDef ownerClass;

    public SqmlSelectorPresentationImpl(final SqmlModule module, final Attributes attributes){
        super(module, attributes);
    }

    @Override
    public Id getOwnerClassId() {
        return ownerClass.getId();
    }
    
    @Override
    public ClientIcon getIcon() {
        return ClientIcon.Definitions.SELECTOR;
    }

    @Override
    public String getFullName() {
        return ownerClass.getFullName()+":"+getShortName();
    }

    @Override
    public Id[] getIdPath() {
        return new Id[]{ownerClass.getId(), getId()};
    }

    
}
