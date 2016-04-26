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

package org.radixware.wps;

import java.util.concurrent.CountDownLatch;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.eas.ISpnegoGssTokenProvider;


class SpnegoGssTokenProvider implements ISpnegoGssTokenProvider, HttpSessionContext.IAuthDataReceiver{
    
    private final HttpSessionContext httpSessionContext;
    private final Object tokenSemaphore = new Object();
    private final IClientEnvironment environment;
    private volatile CountDownLatch tokenLatch;
    private volatile byte[] initialToken;
    private volatile boolean initialTokenReceived;
    private volatile boolean initialTokenRequested;
    
    public SpnegoGssTokenProvider(final byte[] initialToken, final HttpSessionContext context, final IClientEnvironment environment){
        httpSessionContext = context;        
        this.environment = environment;
        synchronized (tokenSemaphore){
            this.initialToken = initialToken;
        }        
    }

    @Override
    public void asyncRequestForInitialGssToken() {
        synchronized (tokenSemaphore){
            if (initialToken==null){
                initialTokenReceived = false;
                initialTokenRequested = false;                
            }else{
                return;
            }
        }
        httpSessionContext.getAuthDataAsync(this);
    }

    @Override
    public byte[] receiveGssToken(final byte[] inToken) {
        final boolean waitForToken;
        synchronized (tokenSemaphore){
            waitForToken = initialTokenRequested;
        }
        if (waitForToken){
            try{
                tokenLatch.await();
            }catch(InterruptedException exception){

            }
        }
        synchronized (tokenSemaphore){
            if (initialTokenReceived || initialToken!=null){
                initialTokenReceived = false;
                byte[] result = initialToken;
                initialToken = null;
                return result;
            }
            initialToken = null;
        }        
        final ClientAuthData authData = httpSessionContext.getAuthData(inToken);
        return authData==null ? null : authData.getToken();
    }

    @Override
    public void authDataReady(final ClientAuthData authData) {
        synchronized (tokenSemaphore){
            initialTokenReceived = true;          
            initialToken = authData==null ? null : authData.getToken();
            if (initialToken==null){
                final String message = 
                    environment.getMessageProvider().translate("TraceMessage", "No authentication data provided for negotiate schema");
                environment.getTracer().debug(message);
            }
            initialTokenRequested = false;
            if (tokenLatch==null){
                return;
            }
        }
        tokenLatch.countDown();
    }

    @Override
    public void authDataRequested() {
        synchronized (tokenSemaphore){   
            tokenLatch = new CountDownLatch(1);
            initialTokenRequested = true;
        }
    } 

    @Override
    public void completeAuthentication() {
        synchronized (tokenSemaphore){
            httpSessionContext.completeAuthentication();
        }
    }        
}
