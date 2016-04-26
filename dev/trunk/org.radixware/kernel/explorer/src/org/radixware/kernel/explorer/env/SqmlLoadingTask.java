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
import java.util.concurrent.CountDownLatch;
import org.radixware.kernel.common.client.meta.sqml.impl.SqmlDefinitions;
import org.radixware.kernel.common.defs.RadixObjectInitializationPolicy;


final class SqmlLoadingTask implements Runnable{
    
    private final Environment environment;
    private volatile SqmlDefinitions defs;
    private volatile Exception exception;
    private volatile boolean wasCancelled;
    
    public SqmlLoadingTask(final Environment environment){
        this.environment = environment;
    }

    @Override
    public void run() {
        try{
            RadixObjectInitializationPolicy.set(new RadixObjectInitializationPolicy(true));
            defs =
                new SqmlDefinitions(environment, environment.getDefManager().getRepository().getBranch());
            if (wasCancelled){
                exception = new CancellationException("Tables loading was cancelled");
                defs = null;
            }
            defs.getTables();
        }catch(Exception exception){
            defs = null;
            this.exception = exception;
        }
    }
    
    public SqmlDefinitions getSqmlDefinitions() throws Exception{
        if (exception!=null){
            throw exception;
        }
        return defs;
    }    
    
    public void cancel(){
        wasCancelled = true;
        if (defs!=null){
            defs.cancelLoading();
        }        
    }
}
