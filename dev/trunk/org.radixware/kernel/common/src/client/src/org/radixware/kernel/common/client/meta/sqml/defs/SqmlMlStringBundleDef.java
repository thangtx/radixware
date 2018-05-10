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

import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.KernelIcon;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinition;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.meta.RadMlStringBundleDef;
import org.radixware.kernel.common.types.Id;


final class SqmlMlStringBundleDef implements ISqmlDefinition{
    
    private final RadMlStringBundleDef bundle;
    
    public SqmlMlStringBundleDef(final RadMlStringBundleDef bundle){
        this.bundle = bundle;
    }

    @Override
    public Id getId() {
        return bundle.getId();
    }

    @Override
    public String getShortName() {
        return bundle.getName();
    }

    @Override
    public String getModuleName() {
        return bundle.getModule().getQualifiedName();
    }

    @Override
    public String getFullName() {
        return bundle.getQualifiedName();
    }

    @Override
    public String getTitle() {
        return getShortName();
    }

    @Override
    public String getDisplayableText(final EDefinitionDisplayMode mode) {
        return mode==EDefinitionDisplayMode.SHOW_FULL_NAMES ? getFullName() : getShortName();
    }

    @Override
    public ClientIcon getIcon() {
        return KernelIcon.getInstance(RadixObjectIcon.UNKNOWN);
    }

    @Override
    public Id[] getIdPath() {
        return new Id[]{getId()};
    }

    @Override
    public boolean isDeprecated() {
        return false;
    }

}
