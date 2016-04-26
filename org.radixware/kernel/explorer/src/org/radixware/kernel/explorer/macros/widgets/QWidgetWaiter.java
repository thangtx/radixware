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

package org.radixware.kernel.explorer.macros.widgets;

import com.trolltech.qt.core.QEventLoop;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QTimerEvent;
import com.trolltech.qt.gui.QWidget;


//Может заблокировать цикл событий в сессии
//Без крайней необходимости не использовать.
final class QWidgetWaiter extends QObject{

    private  QWidgetWaiter(){
        super();
    }

    private final static QWidgetWaiter INSTANCE = new QWidgetWaiter();
    private final static int TIME_INTERVAL_MILLS = 100;
    private long startTime,timeoutMills;
    private final QEventLoop localEventLoop = new QEventLoop(this);
    private QWidget resultWidget;
    private QWidgetPath path;    
    private int timer;

    public static QWidgetWaiter getInstance(){
        return INSTANCE;
    }

    public QWidget waitForWidget(final QWidgetPath path, final long timeountInMills){
        if (path==null){
            throw new NullPointerException();
        }
        this.path = path;
        timeoutMills = timeountInMills;
        resultWidget = null;
        startTime = System.currentTimeMillis();
        timer = startTimer(TIME_INTERVAL_MILLS);
        localEventLoop.exec();
        return resultWidget;
    }

    @Override
    protected void timerEvent(QTimerEvent timerEvent) {
        if (timerEvent.timerId()==timer){
            final long currentTime = System.currentTimeMillis();
            resultWidget = path.findWidget();
            if (resultWidget!=null || (timeoutMills>-1 && (currentTime-startTime)>timeoutMills)){
                stopSearching();
            }
        }
        super.timerEvent(timerEvent);
    }

    private void stopSearching(){
        killTimer(timer);
        localEventLoop.exit();
        timer = -1;
    }

}
