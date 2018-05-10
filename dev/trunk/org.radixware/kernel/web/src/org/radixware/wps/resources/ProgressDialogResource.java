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

package org.radixware.wps.resources;

import java.util.Stack;
import org.radixware.kernel.common.client.eas.resources.IProgressDialogResource;
import org.radixware.kernel.common.client.eas.resources.IProgressMonitor;
import org.radixware.kernel.common.client.views.IProgressHandle;
import org.radixware.schemas.eas.ProgressDialogSetRq;
import org.radixware.schemas.eas.ProgressDialogStartProcessRq;
import org.radixware.schemas.eas.Trace;
import org.radixware.wps.WpsEnvironment;


public class ProgressDialogResource implements IProgressDialogResource {
    
    private static long id = 0;
    private static long maxId = Long.MAX_VALUE - 1;
    
    private static long nextId() {
        if (id >= maxId) {
            id = 0;
        } else {
            id++;
        }
        return id;
    }
    
    private class Process implements IProgressMonitor {
        
        private IProgressHandle progressHandle;
        private String text;
        private String caption;
        private boolean canCancel;
        final long id;
        
        public Process(WpsEnvironment env, ProgressDialogStartProcessRq request) {
            id = nextId();
            IProgressHandle.Cancellable cancellable = null;
            if (request.getCancellable()) {
                cancellable = new IProgressHandle.Cancellable() {
                    
                    @Override
                    public void onCancel() {
                    }
                };
            }
            
            canCancel = request.getCancellable();
            text = request.getTitle();
            caption = request.getCaption();
            this.progressHandle = env.getProgressHandleManager().newProgressHandle(cancellable);
            if (request.getShowPercent()) {
                this.progressHandle.setMaximumValue(100);
            }
        }
        
        public void start() {
            progressHandle.startProgress(text, canCancel);
        }
        
        public void finish() {
            progressHandle.finishProgress();
        }
        
        @Override
        public boolean wasCanceled() {
            return progressHandle.wasCanceled();
        }
        
        @Override
        public void setText(String text) {
            progressHandle.setText(text);
        }

        @Override
        public String getText() {
            return progressHandle.getText();
        }                
        
        @Override
        public void setValue(int value) {
            progressHandle.setValue(value);
        }
        
        @Override
        public void setCanCancel(boolean canCancel) {
            //cancel button in wait dialog is not supported in WEB yet
        }

        @Override
        public void setCancelButtonTitle(String title) {
            //cancel button in wait dialog is not supported in WEB yet
        }                
    }
    
    private final WpsEnvironment env;
    private Process process;
    private Stack<Process> processes = new Stack<>();
    
    public ProgressDialogResource(WpsEnvironment env) {
        this.env = env;
    }
    
    @Override
    public long startProcess(ProgressDialogStartProcessRq request) {
        process = new Process(env, request);
        
        processes.push(process);
        process.start();
        return process.id;
    }
    
    @Override
    public boolean finishProcess() {
        process.finish();
        return true;
    }
    
    @Override
    public void forceShowIfActive() {
    }
    
    @Override
    public void clear() {
        while (!processes.isEmpty()) {
            Process p = processes.pop();
            p.finish();
        }
    }
    
    @Override
    public boolean addTrace(Trace trace) {
        return true;
    }
    
    @Override
    public IProgressMonitor findProcess(long id) {
        for (Process p : processes) {
            if (p.id == id) {
                return p;
            }
        }
        return null;
    }
    
    @Override
    public boolean updateProcess(IProgressMonitor pm, ProgressDialogSetRq request) {
        if (pm.wasCanceled()) {
            return false;
        }
        if (request.isSetTitle()) {
            pm.setText(request.getTitle());
        }
        if (request.isSetCancellable()) {
            pm.setCanCancel(request.isSetCancellable());
        }
        if (request.isSetCancelButtonTitle()){
            pm.setCancelButtonTitle(request.getCancelButtonTitle());
        }
        if (request.isSetProgress()) {
            pm.setValue(Math.round(request.getProgress()));
        }
        return true;
    }
}
