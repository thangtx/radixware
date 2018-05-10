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

import java.util.concurrent.CancellationException;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinitions;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinitionsProvider;
import org.radixware.kernel.common.defs.RadixObjectInitializationPolicy;
import org.radixware.kernel.common.exceptions.DefinitionError;


@Deprecated //use org.radixware.kernel.common.client.meta.sqml.defs.SqmlDefinitionsProvider instead
public class SqmlDefinitionsProvider implements ISqmlDefinitionsProvider{
    
    public SqmlDefinitionsProvider(){
    }
    
    private volatile SqmlDefinitions defs;
    private volatile boolean wasCancelled;

    @Override
    public ISqmlDefinitions load(final IClientEnvironment environment) throws DefinitionError, CancellationException {        
        wasCancelled = false;
        try{            
            RadixObjectInitializationPolicy.set(new RadixObjectInitializationPolicy(true));
            defs =
                new org.radixware.kernel.common.client.meta.sqml.impl.SqmlDefinitions(environment, environment.getDefManager().getRepository().getBranch());
            if (!wasCancelled){
                defs.getTables();
            }
        }catch(Exception exception){
            defs = null;
            final String message = environment.getMessageProvider().translate("ExplorerError", "Failed to load SQML definitions");
            throw new DefinitionError(message, exception);
        }
        if (wasCancelled){
            defs = null;
            throw new CancellationException("SQML definitions loading was cancelled");
        }else{
            return defs;
        }
    }

    @Override
    public void cancelLoading() {
        wasCancelled = true;
        if (defs!=null){
            defs.cancelLoading();
        }
    }
}
