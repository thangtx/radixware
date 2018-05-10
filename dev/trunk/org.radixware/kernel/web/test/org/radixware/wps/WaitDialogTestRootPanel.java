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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.client.env.IEventLoop;
import org.radixware.kernel.common.client.views.IProgressHandle;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.IMainStatusBar;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.wps.dialogs.IDialogDisplayer;
import org.radixware.wps.rwt.*;
import org.radixware.wps.views.RwtAction;
import org.radixware.wps.views.editors.valeditors.ValEditorController;


class WaitDialogTestRootPanel extends RootPanel {
    
    private final static class WorkThread extends Thread{

        private final long workTime;
        private final IEventLoop eventLoop;
        private final IProgressHandle progressHandle;
        private final Object semaphore = new Object();
        private Throwable error;
        
        public WorkThread(final long time, final IEventLoop eventLoop, final IProgressHandle progressHandle){
            workTime = time;
            this.eventLoop = eventLoop;
            this.progressHandle = progressHandle;
            setName("Work Thread");
        }
        
        @Override
        public void run() {
            try{
                final long startTime = System.currentTimeMillis();
                long timeSpent;
                do{
                    try{
                        Thread.sleep(500);
                    }catch(InterruptedException excepion){

                    }  
                    timeSpent = System.currentTimeMillis() - startTime;
                    progressHandle.setValue((int)timeSpent);
                }while(timeSpent < workTime && !progressHandle.wasCanceled());
                WaitDialogTestRootPanel.log("loop exit");
            }catch(Throwable ex){
                WaitDialogTestRootPanel.log("loop exception", ex);
                synchronized(semaphore){
                    error = ex;
                }
            }
            WaitDialogTestRootPanel.log("Before event loop stop");
            eventLoop.stop();
            WaitDialogTestRootPanel.log("Thread exit");
        }        
        
        public Throwable getError(){
            synchronized(semaphore){
                return error;
            }
        }
    }

    private final WpsEnvironment env;    
    private final IEventLoop eventLoop;

    public WaitDialogTestRootPanel(final WpsEnvironment env) {
        this.env = env;
        eventLoop = env.newEventLoop();
        createTestUI();
    }

    @Override
    protected IDialogDisplayer getDialogDisplayer() {
        return env.getDialogDisplayer();
    }

    @Override
    public IMainView getExplorerView() {
        return null;
    }

    @Override
    public void closeExplorerView() {
    }

    /*@Override
    protected Runnable componentRendered(HttpQuery query) {
        return new Runnable() {

            @Override
            public void run() {
                createTestUI();
            }
        };
    }*/
    
    private void addEditorController(ValEditorController controller, int top, int left){
        final UIObject editor = (UIObject)controller.getValEditor();
        add(editor);
        editor.setTop(top);
        editor.setLeft(left);
    }

    private void createTestUI() {
        final VerticalBoxContainer mainLayout = new VerticalBoxContainer();
        {
            mainLayout.setTop(20);
            mainLayout.setLeft(200);
            mainLayout.setVSizePolicy(SizePolicy.EXPAND);
            mainLayout.setWidth(600);            
            add(mainLayout);
        }
        final PushButton pbShowMessage = new PushButton("Show WaitDialog");
        mainLayout.add(pbShowMessage);
        pbShowMessage.addClickHandler(new IButton.ClickHandler() {

            @Override
            public void onClick(final IButton source) {
                final WpsProgressHandleManager.WpsProgressHandle progressHandle = 
                    (WpsProgressHandleManager.WpsProgressHandle)getEnvironment().getProgressHandleManager().newProgressHandle();
                progressHandle.setText("Wait Dialog Text");
                progressHandle.setTitle("Wait Dialog Titile");
                final int processTime = 60000;
                progressHandle.setMaximumValue(processTime);
                progressHandle.addAction(createAction("First Button"), null, false);
                progressHandle.addAction(createAction("Second Button"), null, false);
                final WorkThread task = new WorkThread(processTime, eventLoop, progressHandle);
                eventLoop.scheduleTask(new Runnable() {
                    @Override
                    public void run() {
                        task.start();
                        progressHandle.startProgress("Wait Dialog Progress Text", true);                        
                    }
                });
                try{
                    eventLoop.start();
                }finally{
                    log("finishing progress");
                    progressHandle.finishProgress();
                }
                final Throwable ex = task.getError();
                if (ex!=null){
                    env.processException(ex);
                }
                log("exit on click");
            }
        });
    }
    
    public Action createAction(final String title){
        final RwtAction action = new RwtAction(getEnvironment());
        action.setTextShown(true);
        action.setText(title);
        return action;
    }

    @Override
    public IMainStatusBar getMainStatusBar() {
        return null;
    }
    
    public static void log(final String message){
        Logger.getLogger(WebServer.class.getName()).log(Level.SEVERE, message);
    }
    
    public static void log(final String message, final Throwable ex){
        Logger.getLogger(WebServer.class.getName()).log(Level.SEVERE, message, ex);
    }
    
}
