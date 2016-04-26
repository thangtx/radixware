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

package org.radixware.kernel.explorer.widgets;

import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QTimerEvent;
import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.client.widgets.IPeriodicalTask;
import org.radixware.kernel.common.client.widgets.TimerEventHandler;
import org.radixware.kernel.explorer.env.Application;


public class TimersController {

    private static int DEFAULT_TIMER_PERIOD_MILLS = 300;
    
    private static class PeriodicalTask implements IPeriodicalTask{
        
        private final int timerId;
        private volatile boolean isActive = true;
        private final TimerEventHandler handler;
        private volatile Object userObject;
        
        public PeriodicalTask(final int id, final TimerEventHandler handler){            
            timerId = id;
            this.handler = handler;
        }
        
        private void tick(){
            if (isActive){
                try{
                    handler.processTimerEvent(this);
                }catch(RuntimeException exception){
                    Application.getInstance().getTracer().error(exception);
                }
            }
        }

        public int getId() {
            return timerId;
        }

        @Override
        public void pause() {
            isActive = false;
        }

        @Override
        public void resume() {
            isActive = true;
        }  

        @Override
        public boolean isActive() {
            return isActive;
        }

        @Override
        public void setUserObject(final Object userObj) {
            userObject = userObj;
        }

        @Override
        public Object getUserObject() {
            return userObject;
        }        
    }

    private Map<Integer,PeriodicalTask> tasksById;

    public IPeriodicalTask startTimer(final TimerEventHandler handler, final int interval, final QObject owner) {
        if (handler==null){
            throw new NullPointerException("TimerEventHandler must be defined");
        }
        final int timerId = owner.startTimer(interval);
        final PeriodicalTask task = new PeriodicalTask(timerId, handler);
        if (tasksById==null){
            tasksById = new HashMap<>();
        }
        tasksById.put(timerId, task);
        return task;        
    }
    
    public IPeriodicalTask startTimer(final TimerEventHandler handler, final QObject owner) {
        return startTimer(handler,DEFAULT_TIMER_PERIOD_MILLS,owner);
    }

    public void killTimer(final IPeriodicalTask task, final QObject owner) {
        final int timerId = ((PeriodicalTask)task).getId();
        if (tasksById==null || !tasksById.containsKey(timerId)){
            throw new IllegalArgumentException("Unknown periodical task");
        }
        tasksById.remove(timerId);
        if (tasksById.isEmpty()){
            tasksById = null;
        }
        owner.killTimer(timerId);
    }

    public boolean processTimerEvent(final QTimerEvent event) {
        if (tasksById!=null){
            final PeriodicalTask task = tasksById.get(event.timerId());
            if (task!=null){
                task.tick();
                return true;
            }
        }
        return false;
    }
    
    public void clearTimers(){
        if (tasksById!=null){
            tasksById.clear();
        }
    }
}
