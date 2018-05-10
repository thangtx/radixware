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

package org.radixware.kernel.explorer.env.progress;

import com.trolltech.qt.core.QCoreApplication;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QTimerEvent;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QCursor;
import com.trolltech.qt.gui.QMainWindow;
import com.trolltech.qt.gui.QMenuBar;
import com.trolltech.qt.gui.QWidget;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.WeakHashMap;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.views.IProgressHandle;
import org.radixware.kernel.common.client.views.IProgressHandle.Cancellable;
import org.radixware.kernel.common.exceptions.AppError;
import org.radixware.kernel.common.utils.SystemTools;
import org.radixware.kernel.explorer.dialogs.WaitDialog;
import org.radixware.kernel.explorer.env.UserInputFilter;


public class ExplorerProgressHandleManager extends QObject implements org.radixware.kernel.common.client.env.progress.ProgressHandleManager {

    private static class CancellableProgressHandle extends ProgressHandle{
        
        private final Cancellable cancelHandler;
        
        public CancellableProgressHandle(final ExplorerProgressHandleManager manager, final Cancellable onCancelHandler){
            super(manager);
            cancelHandler = onCancelHandler;
        }

        @Override
        protected void onCancel() {
            cancelHandler.onCancel();
        }

        @Override
        public boolean canCancel() {
            return true;
        }        
    }
    
    private final IClientEnvironment environment;
    private final IProgressView progressView;
    private final QObject eventFilter = new UserInputFilter(this);
    
    private int hidden = 0;
    private boolean isBlocked;
    private int unblockTimer = 0;
    private ProgressHandle current;
    
    private final SimpleDateFormat traceCallTimeFormat = new SimpleDateFormat("HH:mm:ss.SSS");
    private final ArrayList<String> traceCall = new ArrayList<>(); 
    private final ArrayList<String> blockProgressTraceCall = new ArrayList<>();
    private Set<ProgressHandle> weakProgressHandleSet = Collections.newSetFromMap(new WeakHashMap<ProgressHandle, Boolean>());

    public ExplorerProgressHandleManager(final IClientEnvironment environment, final QObject parent) {
        super(parent);
        this.environment = environment;
        progressView = new WaitDialog(environment);
    }

    @Override
    public void blockProgress() {
        if (isQtThread()){
            blockTraceCall("blocking progress");
            hidden++;
            final ProgressHandle active = (ProgressHandle)getActive();
            if (active != null) {
                active.cancelStart();
                getProgressView().hideProgress();
            }
            unblockUserInput();            
        }
    }

    @Override
    public void unblockProgress() {
        if (isQtThread() && hidden > 0){
            blockTraceCall("unblocking progress");
            hidden--;
            if (hidden == 0) {
                final ProgressHandle active = (ProgressHandle)getActive();
                if (active != null) {
                    if (active.wasShown()) {
                        getProgressView().showProgress();
                    } else {
                        blockUserInput();
                        active.scheduleStart();
                    }
                }
            }
        }
    }
    
    public boolean isProgressBlocked(){
        return hidden>0;
    }
    
    void blockUserInput() {
        if (!isBlocked) {
            traceCall("blockUserInput");
            if (SystemTools.isOSX && environment.getMainWindow() instanceof QMainWindow){
                final QMenuBar menuBar = ((QMainWindow)environment.getMainWindow()).menuBar();
                if (menuBar!=null){
                    menuBar.setDisabled(true);
                }
            }            
            QApplication.instance().installEventFilter(eventFilter);
            QApplication.setOverrideCursor(new QCursor(Qt.CursorShape.BusyCursor));            
            isBlocked = true;
            unblockTimer = startTimer(ProgressHandle.START_DURATION*2);
        }
    }

    void unblockUserInput() {
        if (isBlocked) {
            traceCall("unblockUserInput");
            if (SystemTools.isOSX && environment.getMainWindow() instanceof QMainWindow){
                final QMenuBar menuBar = ((QMainWindow)environment.getMainWindow()).menuBar();
                if (menuBar!=null){
                    menuBar.setDisabled(false);
                }
            }            
            QApplication.instance().removeEventFilter(eventFilter);
            QApplication.restoreOverrideCursor();
            isBlocked = false;
            killTimer(unblockTimer);            
        }
    }    

    @Override
    public IProgressHandle newProgressHandle() {
        return isQtThread() ? newProgressHandle(null) : new DummyProgressHandle();
    }

    @Override
    public IProgressHandle newProgressHandle(final Cancellable onCancelHandler) {
        if (isQtThread()){
            final ProgressHandle ph;
            if (onCancelHandler != null) {
                ph = new CancellableProgressHandle(this, onCancelHandler);
            } else {
                ph = new ProgressHandle(this);
            }
            weakProgressHandleSet.add(ph);
            return ph;            
        }
        else{
            return new DummyProgressHandle();
        }

    }
    
    public IClientEnvironment getEnvironment(){
        return environment;
    }
    
    public IProgressView getProgressView(){
        return progressView;
    }
    
    void setCurrentProgress(final ProgressHandle currentProgress){
        current = currentProgress;
    }
    
    ProgressHandle getCurrentProgress(){
        return current;
    }
    
    void traceCall(final String methodTitle){
        final String time = "call "+methodTitle+" at "+traceCallTimeFormat.format(new Date(System.currentTimeMillis()));
        traceCall.add(ClientException.exceptionStackToString(new AppError(time)));
        if (traceCall.size()>64){
            traceCall.remove(0);
        }
    }
    
    private void blockTraceCall(final String message){
        final String time = message+" at "+traceCallTimeFormat.format(new Date(System.currentTimeMillis()));
        blockProgressTraceCall.add(ClientException.exceptionStackToString(new AppError(time)));
        if (blockProgressTraceCall.size()>64){
            blockProgressTraceCall.remove(0);
        }
    }
        
    public void assertProgressUnblocked(final String message){
        if (isProgressBlocked()){
            final StringBuilder traceMessageBuilder = new StringBuilder(message);
            for (String traceCall: blockProgressTraceCall){
                traceMessageBuilder.append('\n');
                traceMessageBuilder.append(traceCall);
            }
            environment.getTracer().error(traceMessageBuilder.toString());
            hidden = 0;
            final ProgressHandle active = (ProgressHandle)getActive();
            if (active != null) {
                if (active.wasShown()) {
                    getProgressView().showProgress();
                } else {
                    blockUserInput();
                    active.scheduleStart();
                }
            }
        }
        blockProgressTraceCall.clear();
    }
    
    public boolean forceShowActive(){
        final ProgressHandle active = (ProgressHandle)getActive();
        return active==null ? false : active.forceShow();
    }
    
    @Override
    public IProgressHandle getActive(){
        if (isQtThread()){
            for (ProgressHandle handle = getCurrentProgress(); handle != null; handle = handle.getPrevious()) {
                if (handle.isActive()) {
                    return handle;
                }
            }
        }
        return null;
    }

    @Override
    public IProgressHandle newStandardProgressHandle() {
        if (isQtThread()){
            final ProgressHandle ph = new StandardProgressHandle(this);
            weakProgressHandleSet.add(ph);
            return ph;
        }
        return new DummyProgressHandle();
    }
    
    private boolean isQtThread(){
        return QCoreApplication.instance().thread()==Thread.currentThread();
    }
    
    public void setProgressViewHidden(final boolean hidden){
        ((QWidget)progressView).setHidden(hidden);
    }
    
    public boolean isProgressViewVisible(){
        return ((QWidget)progressView).isVisible();
    }
    
    public void clear(){
        for (ProgressHandle ph: weakProgressHandleSet){
            if (ph!=null){
                ph.dispose();
            }
        }
    }

    @Override
    protected void timerEvent(final QTimerEvent timerEvent) {
        if (timerEvent.timerId()==unblockTimer){
            timerEvent.accept();
            if (getActive()==null){
                if (isBlocked){
                    final String traceMessage = environment.getMessageProvider().translate("TraceMessage", "User input is blocked but no active progress detected. Forcedly unblocking.\nTrace Call:");
                    final StringBuilder traceBuilder = new StringBuilder(traceMessage);
                    for (String call: traceCall){
                        traceBuilder.append('\n');
                        traceBuilder.append(call);                    
                    }
                    environment.getTracer().error(traceBuilder.toString());
                    unblockUserInput();
                }else{
                    killTimer(unblockTimer);
                }
            }
        }else{
            super.timerEvent(timerEvent);
        }
    }

    
    
}
