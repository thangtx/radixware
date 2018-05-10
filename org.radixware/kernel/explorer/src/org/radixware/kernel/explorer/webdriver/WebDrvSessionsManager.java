/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

package org.radixware.kernel.explorer.webdriver;

import org.radixware.kernel.explorer.webdriver.exceptions.WebDrvException;
import org.radixware.kernel.explorer.webdriver.exceptions.EWebDrvErrorCode;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.radixware.kernel.explorer.utils.QtJambiExecutor;

public final class WebDrvSessionsManager {
    
    private static WebDrvSessionsManager INSTANCE = new WebDrvSessionsManager();   
    
    private final Map<UUID, WebDrvSession> sessionsById = new HashMap<>();
    private final List<QtJambiExecutor> executors = new LinkedList<>();    
    private final Object semaphore = new Object();
    
    private WebDrvSessionsManager(){
    }

    public static WebDrvSessionsManager getInstance(){
        return INSTANCE;
    }
    
    public WebDrvSession createSession(final WebDrvCapabilities caps, long timeStart) throws WebDrvException{
        synchronized(semaphore){
            if (executors.isEmpty()){
                throw new WebDrvException(EWebDrvErrorCode.SESSION_NOT_CREATED, "Sessions limit exceeded");
            }
            final WebDrvSession session = new WebDrvSession(caps, executors.remove(0), WebDrvServer.getInstance().getEnvironment(), timeStart);
            sessionsById.put(session.getId(), session);
            return session;
        }
    }
    
    public WebDrvSession getSessionById(final UUID id) throws WebDrvException{
        synchronized(semaphore){
            final WebDrvSession session = sessionsById.get(id);
            if (session==null){
                throw new WebDrvException(EWebDrvErrorCode.INVALID_SESSION_ID, "Session with id \'"+String.valueOf(id)+"\' does not exist");
            }
            return session;
        }
    }
    
    public void deleteSession(final WebDrvSession session) throws WebDrvException{
        synchronized(semaphore){
            final QtJambiExecutor executor = session.close();
            sessionsById.remove(session.getId());
            executors.add(executor);
        }
    }
    
    public int getSessionCount(){
        synchronized(semaphore){
            return sessionsById.size();
        }
    }
    
    public int getSessionLimit(){
        return getSessionCount()+executors.size();
    }
        
    public void setExecutors(final Collection<QtJambiExecutor> moreExecutors){
        synchronized(semaphore){
            executors.clear();
            executors.addAll(moreExecutors);
        }
    }
}
