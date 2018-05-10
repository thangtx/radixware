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

package org.radixware.kernel.explorer.env;

import java.util.concurrent.CancellationException;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinitions;
import org.radixware.kernel.common.client.meta.sqml.SqmlDefinitionsLoader;
import org.radixware.kernel.common.exceptions.DefinitionError;


final class SqmlLoadingTask implements Runnable{
    
    private final Environment environment;
    private final boolean preloadBranch;
    private volatile  ISqmlDefinitions defs;
    private volatile DefinitionError exception;
    private volatile boolean wasCancelled;
    
    public SqmlLoadingTask(final Environment environment, final boolean preloadBranch){
        this.environment = environment;
        this.preloadBranch = preloadBranch;
    }

    @Override
    public void run() {
        defs = null;
        wasCancelled = false;
        try{
            defs  = SqmlDefinitionsLoader.getInstance().load(environment);
        }catch(DefinitionError error){
            exception = error;
        }catch(CancellationException ex){
            wasCancelled = true;
        }
        if (preloadBranch){
            environment.getDefManager().getRepository().preloadBranch();
        }
    }
    
    public ISqmlDefinitions getSqmlDefinitions() throws DefinitionError{        
        if (exception!=null){
            throw exception;
        }
        return wasCancelled ? null : defs;
    }    
    
    public void cancel(){
        wasCancelled = true;
        SqmlDefinitionsLoader.getInstance().cancelLoading();
    }
}
