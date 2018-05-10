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

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.types.Id;


final class SqmlModule {
    
    private final Id id;
    private final String name;    
    private final IClientEnvironment environment;
    
    public SqmlModule(final IClientEnvironment environment, final Id moduleId, final String moduleName){
        id = moduleId;
        name = moduleName;
        this.environment = environment;
    }

    public Id getId() {
        return id;
    }

    public String getQualifiedName() {
        return name;
    }
    
    public IClientEnvironment getEnvironment(){
        return environment;
    }
}
