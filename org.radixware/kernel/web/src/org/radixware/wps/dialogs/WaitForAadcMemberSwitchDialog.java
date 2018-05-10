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

package org.radixware.wps.dialogs;

import java.util.Timer;
import java.util.TimerTask;
import org.radixware.kernel.common.client.dialogs.IWaitForAadcMemberSwitchDialog;
import org.radixware.kernel.common.client.eas.EasClient;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.WpsProgressHandleManager;
import org.radixware.wps.views.RwtAction;


public final class WaitForAadcMemberSwitchDialog implements IWaitForAadcMemberSwitchDialog{
    
    private final static ClientIcon INSTANT_SWITH_ICON = new ClientIcon("classpath:images/lightning.svg"){};    
    private final static long UPDATE_PROGRESS_BAR_INTERVAL_MILLS = 1000;    
    
    private final WpsEnvironment environment;
    private final WpsProgressHandleManager.WpsProgressHandle progressHandle;
    private final Object semaphore = new Object();
    private final Action.ActionListener actionListener = new Action.ActionListener() {
        @Override
        public void triggered(final Action action) {
            synchronized(semaphore){
                WaitForAadcMemberSwitchDialog.this.pressedButton = 
                    (IWaitForAadcMemberSwitchDialog.Button)action.getUserObject();
            }
            WaitForAadcMemberSwitchDialog.this.finish();
        }
    };    
    private final long startTime = System.currentTimeMillis();    
    private final TimerTask timerTask = new TimerTask(){
        @Override
        public void run() {
            final long currentTime = System.currentTimeMillis();        
            final long spentTime = currentTime - startTime;
            if (spentTime<=(int)EasClient.KEEP_AADC_MEMBER_INTERVAL_MILLIS){                
                progressHandle.setValue((int)spentTime);
            }else{
                cancel();
                WaitForAadcMemberSwitchDialog.this.finish();                
            }            
        }        
    };
    private final Timer timer = new Timer("WaitForAadcMemberSwitchDialog Timer", true);
    private IWaitForAadcMemberSwitchDialog.Button pressedButton;
    private boolean finished;
    
    public WaitForAadcMemberSwitchDialog(final WpsEnvironment env){
        environment = env;
        progressHandle = (WpsProgressHandleManager.WpsProgressHandle)env.getProgressHandleManager().newProgressHandle();
        {
            final String buttonText = env.getMessageProvider().translate("Wait Dialog", "Immediate switch");
            final RwtAction action = 
                createAction(buttonText, INSTANT_SWITH_ICON, IWaitForAadcMemberSwitchDialog.Button.IMMEDIATE_SWITCH);            
            final String cancelationText = 
                env.getMessageProvider().translate("Wait Dialog", "Switching to another cluster node...");
            progressHandle.addAction(action, cancelationText, false);
        }
        {
            final String buttonText = env.getMessageProvider().translate("Wait Dialog", "Interrupt operation");
            final RwtAction action = 
                createAction(buttonText, ClientIcon.Dialog.BUTTON_CANCEL, IWaitForAadcMemberSwitchDialog.Button.CANCEL);
            final String cancelationText = 
                env.getMessageProvider().translate("Wait Dialog", "Cancelling request...");
            progressHandle.addAction(action, cancelationText, false);
        }        
        progressHandle.setMaximumValue((int)EasClient.KEEP_AADC_MEMBER_INTERVAL_MILLIS);
    }
    
    private RwtAction createAction(final String text, 
                                                   final ClientIcon icon,
                                                   final IWaitForAadcMemberSwitchDialog.Button button){
        final RwtAction action = new RwtAction(environment);
        action.setTextShown(true);
        action.setText(text);
        action.setIcon(environment.getApplication().getImageManager().getIcon(icon));
        action.setUserObject(button);
        action.setObjectName(button.name().toLowerCase());
        action.addActionListener(actionListener);        
        return action;
    }

    @Override
    public void display() {
        final String progressText = 
            environment.getMessageProvider().translate("Wait Dialog", "Switching to another cluster node.")
            +" \n"
            +environment.getMessageProvider().translate("Wait Dialog", "Please wait for data to reach a consistent state.");
        progressHandle.setValue((int)(System.currentTimeMillis() - startTime));
        progressHandle.startProgress(progressText, false);
        timer.schedule(timerTask, UPDATE_PROGRESS_BAR_INTERVAL_MILLS, UPDATE_PROGRESS_BAR_INTERVAL_MILLS);
    }

    @Override
    public IWaitForAadcMemberSwitchDialog.Button getPressedButton() {
        synchronized(semaphore){
            return pressedButton;
        }
    }

    @Override
    public void finish() {
        synchronized(semaphore){
            if (finished){
                return;
            }else{
                finished = true;
            }
        }
        timer.cancel();
        progressHandle.finishProgress();
    }

}
