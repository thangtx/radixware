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

package org.radixware.kernel.common.client.eas.resources;

import org.radixware.kernel.common.client.eas.resources.rpc.RadixRpcServer;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.exceptions.TerminalResourceException;
import org.radixware.kernel.common.enums.EFileOpenMode;
import org.radixware.kernel.common.enums.EFileOpenShareMode;

import org.radixware.schemas.eas.FileOpenRq;
import org.radixware.schemas.eas.MessageDialogOpenRq;
import org.radixware.schemas.eas.ProgressDialogStartProcessRq;
import org.radixware.schemas.eas.ProgressDialogSetRq;
import org.radixware.schemas.eas.ProgressDialogTraceRq;

public final class TerminalResources {

    private final Map<String, ITerminalResource> resources = new HashMap<>();
    private final Map<IClientEnvironment,RadixRpcServer> rpcServers = new WeakHashMap<>(16);
    private final static Object SEMAPHORE = new Object();
    private final static Map<IClientEnvironment,TerminalResources> RESOURCES_BY_ENV = new WeakHashMap<>(16);

    private TerminalResources(){
    }

    public static TerminalResources getInstance(final IClientEnvironment environment) {
        synchronized(SEMAPHORE){
            TerminalResources instance = RESOURCES_BY_ENV.get(environment);
            if (instance==null){
                instance = new TerminalResources();
                RESOURCES_BY_ENV.put(environment, instance);
            }
            return instance;
        }
    }

    public String startProgressDialogProcess(IClientEnvironment environment, ProgressDialogStartProcessRq request) {
        return String.valueOf(environment.getResourceManager().getProgressDialogResource().startProcess(request));
    }

    public boolean updateProgressDialog(IClientEnvironment environment, ProgressDialogSetRq request) throws TerminalResourceException {
        if (request.getId() == null || request.getId().isEmpty()) {
            throw new TerminalResourceException("Invalid request format: process id was not defined");
        }
        final long processId;
        try {
            processId = Long.parseLong(request.getId());
        } catch (NumberFormatException ex) {
            throw new TerminalResourceException(TerminalResourceException.ResourceKind.Process, request.getId());
        }
        final IProgressMonitor process = environment.getResourceManager().getProgressDialogResource().findProcess(processId);
        if (process == null) {
            throw new TerminalResourceException(TerminalResourceException.ResourceKind.Process, request.getId());
        }
        return environment.getResourceManager().getProgressDialogResource().updateProcess(process, request);
    }

    public boolean addTraceInProgressDilog(IClientEnvironment environment, ProgressDialogTraceRq request) {
        return environment.getResourceManager().getProgressDialogResource().addTrace(request.getTrace());
    }

    public void finishProgressDialogProcess(IClientEnvironment environment) {
        environment.getResourceManager().getProgressDialogResource().finishProcess();
    }

    public String openMessageDialogResource(IClientEnvironment environment, MessageDialogOpenRq request) {
        environment.getResourceManager().getProgressDialogResource().forceShowIfActive();
        IMessageDialogResource dlg = environment.getResourceManager().openMessageDialogResource(request);
        final String id = dlg.getId();
        resources.put(id, dlg);
        return id;
    }

    public ITerminalResource getResource(final String id) throws TerminalResourceException {
        if (resources.containsKey(id)) {
            return resources.get(id);
        }
        throw new TerminalResourceException(TerminalResourceException.ResourceKind.MessageDialog, id);
    }

    public void freeResource(final String id) throws TerminalResourceException {
        if (resources.containsKey(id)) {
            resources.get(id).free();
        }
        resources.remove(id);
    }

    public void freeAllResources(final IClientEnvironment environment) throws TerminalResourceException {
        for (Map.Entry<String, ITerminalResource> entry : resources.entrySet()) {
            entry.getValue().free();
        }
        resources.clear();
        rpcServers.remove(environment);
        environment.getResourceManager().getProgressDialogResource().clear();
    }

    public String openFileResource(final IClientEnvironment environment, final FileOpenRq request) throws TerminalResourceException, IOException {
        EFileOpenMode mode = request.getMode();
        EFileOpenShareMode share = request.getShare();
        IFileResource resource = environment.getResourceManager().openFileResource(request.getFileName(), mode, share);
        final String id = resource.getId();
        resources.put(id, resource);
        return id;
    }
    
    public IFileDirResource getFileDirResource(final IClientEnvironment environment){
        return environment.getResourceManager().getFileDirResource();
    }
    
    public RadixRpcServer getRpcServer(final IClientEnvironment environment){
        RadixRpcServer rpcServer = rpcServers.get(environment);
        if (rpcServer==null){
            rpcServer = new RadixRpcServer(environment);
            rpcServers.put(environment, rpcServer);
        }
        return rpcServer;
    }
}
