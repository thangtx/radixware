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

import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QTimer;
import com.trolltech.qt.gui.QApplication;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.views.IProgressHandle;


class ProgressHandle extends QObject implements IProgressHandle {

    final static int START_DURATION = 3000;
    private final static int STOP_DURATION = 500;
    private final static int PAUSE_AFTER_UPDATE = 4;
    private final static int PAUSE_FOR_USER = 8;
    
    private String labelText = null;
    private String windowTitle = null;
    private boolean inputBlocked;
    private int value = -1;
    private int maximumValue = 0;
    private ProgressHandle previous;
        
    private boolean isActive; //was called startProgress, but finishProgress was not called yet
    private boolean wasShown;
    private boolean wasCancelled;
    private boolean canCancel;
    private final ExplorerProgressHandleManager manager;
       
    
    private final QTimer startTimer;
    private final QTimer finishTimer;       

    public ProgressHandle(final ExplorerProgressHandleManager progressHandleManager) {
        super();
        manager = progressHandleManager;
        startTimer = new QTimer(this);
        startTimer.setSingleShot(true);
        startTimer.setInterval(START_DURATION);
        finishTimer = new QTimer(this);
        finishTimer.setSingleShot(true);
        finishTimer.setInterval(STOP_DURATION);
    }
    
    protected IClientEnvironment getEnvironment(){
        return manager.getEnvironment();
    }
    
    private IProgressView getProgressView(){
        return manager.getProgressView();
    }
    
    ProgressHandle getPrevious(){
        return previous;
    }
    
    void scheduleStart(){
        startTimer.start();
    }
    
    void cancelStart(){
        startTimer.stop();
    }
    
    boolean wasShown(){
        return wasShown;
    }
    
    private void traceCall(final String methodTitle){
        manager.traceCall(methodTitle);
    }

    private void blockUserInput() {
        manager.blockUserInput();
        inputBlocked = true;
    }

    private void unblockUserInput() {
        manager.unblockUserInput();
        inputBlocked = false;
    }
    
    private void setCurrent(final ProgressHandle progress){
        manager.setCurrentProgress(progress);
    }
    
    private ProgressHandle getCurrent(){
        return manager.getCurrentProgress();
    }
    
    private boolean canShow(){
        return !manager.isProgressBlocked();
    }
        
    @Override
    public final void startProgress(final String text, final boolean canCancel) {
        startProgress(text, canCancel, false);
    }

    @Override
    public final void startProgress(final String text, final boolean canCancel, final boolean forcedShow) {
        traceCall("startProgress \""+text+"\"");
        if (isActive()) {
            final MessageProvider mp = getEnvironment().getMessageProvider();
            final String message = mp.translate("ExplorerError", "Progress \'%s\' was already started with message \'%s\'");
            getEnvironment().getTracer().error(new IllegalStateException(message));
            return;
        }
        labelText = text;
        this.canCancel = canCancel;
        wasCancelled = false;
        if (canShow()) {
            blockUserInput();
        }
        startTimer.timeout.connect(this, "start()");
        finishTimer.timeout.connect(this, "finish()");
        for (ProgressHandle handle = getCurrent(); handle != null; handle = handle.previous) {
            handle.startTimer.stop();
            handle.finishTimer.stop();
            if (handle.isActive) {
                previous = handle;
                break;
            }
        }
        isActive = true;
        wasShown = false;
        setCurrent(this);
        if (canShow()) {
            if (forcedShow){
                start();
            }else{
                startTimer.start();
            }
        }
    }        

    @SuppressWarnings("unused")
    private void start() {
        traceCall("start");
        //WaitDialog.getInstance().setUpdatesEnabled(true);
        wasShown = true;
        if (canShow()) {
            getProgressView().showProgress();
            updateProgressView();
            pause(PAUSE_AFTER_UPDATE);
            unblockUserInput();
        } else {
            updateProgressView();
        }
    }

    @SuppressWarnings("unused")
    private void finish() {
        traceCall("finish");
        startTimer.disconnect();
        finishTimer.disconnect();
        if (!wasShown && previous==null) {
            unblockUserInput();
        }
        wasShown = false;
        boolean wasPrevious = false;
        for (ProgressHandle handle = previous; handle != null; handle = handle.previous) {
            if (handle.isActive()) {
                setCurrent(handle);
                wasPrevious = true;
                if (canShow()) {
                    if (handle.wasShown) {
                        handle.start();
                    } else {
                        handle.startTimer.start();
                    }
                }
                break;
            }
        }
        previous = null;
        if (!wasPrevious) {
            setCurrent(null);
            getProgressView().hideProgress();
        }
    }
    
    private static void pause(final int count) {
        for (int i = 1; i <= count; i++) {
            QApplication.processEvents();
            /*            try {
            Thread.sleep(10);
            } catch (InterruptedException ex) {

            }*/
        }
    }

    @Override
    public final boolean isActive() {
        return isActive;
    }

    @Override
    public final void cancel() {
        if (isActive && !wasCancelled) {
            wasCancelled = true;
            /*            for (ProgressHandle handle = current; handle!=this && handle!=null; handle=handle.previous){
            handle.wasCancelled = true;
            }
             */
            onCancel();
        }
    }

    protected void onCancel() {
    }

    @Override
    public final boolean wasCanceled() {
        //pause(PAUSE_FOR_USER);
/*        for (ProgressHandle handle = current; handle!=this && handle!=null; handle=handle.previous){
        if (handle.wasCancelled)
        return true;
        }
         */
        return wasCancelled;
    }

    @Override
    public final void finishProgress() {
        traceCall("finishProgress");
        if (isActive) {
            boolean userInputWasBlocked = false;
            for (ProgressHandle handle = getCurrent(); handle != this && handle != null; handle = handle.previous) {
                if (handle.inputBlocked){
                    userInputWasBlocked = true;
                }
                handle.isActive = false;
                handle.wasShown = false;
                handle.previous = null;
                handle.startTimer.stop();
                handle.startTimer.disconnect();
                handle.finishTimer.stop();
                handle.finishTimer.disconnect();
                //handle.cancelButton.clicked.disconnect();
            }
            isActive = false;
            startTimer.stop();
            startTimer.disconnect();
            //cancelButton.clicked.disconnect();
            if (wasShown) {
                if (userInputWasBlocked){
                    unblockUserInput();
                }
                finishTimer.start();
            } else {
                finish();
            }
        }
    }

    @Override
    public final void setTitle(final String title) {
        this.windowTitle = title;
        if (this == getCurrent() && isActive && wasShown) {
            updateProgressView();
            pause(4);
        }
    }

    @Override
    public final String getTitle() {
        final MessageProvider mp = getEnvironment().getMessageProvider();
        return windowTitle == null ? mp.translate("Wait Dialog", "Please Wait") : windowTitle;
    }

    @Override
    public final void setText(final String text) {
        this.labelText = text;
        if (this == getCurrent() && isActive && wasShown) {
            updateProgressView();
            pause(2);
        }
    }

    @Override
    public final String getText() {
        final MessageProvider mp = getEnvironment().getMessageProvider();
        return labelText == null ? mp.translate("Wait Dialog", "Please Wait...") : labelText;
    }

    @Override
    public final void setMaximumValue(final int value) {
        this.maximumValue = value;
        if (this == getCurrent() && isActive && wasShown) {
            updateProgressView();
            pause(4);
        }
    }

    @Override
    public final int getMaximumValue() {
        return maximumValue;
    }

    @Override
    public final void setValue(final int value) {
        this.value = value;
        if (this == getCurrent() && isActive && wasShown) {
            updateProgressView();
            pause(4);
        }
    }

    @Override
    public final void incValue() {
        value++;
        if (this == getCurrent() && isActive && wasShown) {
            updateProgressView();
            pause(4);
        }
    }

    @Override
    public final int getValue() {
        return value;
    }

    @Override
    public boolean canCancel() {
        return canCancel;
    }

    private void updateProgressView() {
        getProgressView().updateForProgress(this);
    }
}
