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

package org.radixware.kernel.common.client.meta.sqml.impl;

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.sqml.ISqmlSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.types.Id;


final class SqmlSelectorPresentationImpl extends SqmlDefinitionImpl implements ISqmlSelectorPresentationDef{
    
    private final Id classId;
    
    public SqmlSelectorPresentationImpl(final IClientEnvironment environment, final Id ownerClassId, final AdsSelectorPresentationDef presentationDef){
        super(environment, presentationDef);
        classId = ownerClassId;
    }

    @Override
    public Id getOwnerClassId() {
        return classId;
    }        

    @Override
    public String getTitle() {
        return getShortName();
    }
}
