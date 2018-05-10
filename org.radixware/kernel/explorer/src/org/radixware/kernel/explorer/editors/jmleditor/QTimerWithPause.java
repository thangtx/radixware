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
package org.radixware.kernel.explorer.editors.jmleditor;

import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QTimer;

/**
 *
 * @author npopov
 */
public class QTimerWithPause extends QTimer {

    private long startTime = 0;
    private int elapsedTime = 0;
    private int interval = 0;
    private boolean isPaused = false;

    public QTimerWithPause(QObject parent) {
        super(parent);
    }

    public void startWithPause(int msec) {
        elapsedTime = 0;
        startTime = System.currentTimeMillis();
        interval = msec;
        isPaused = false;
        start(msec);
    }

    public void pause() {
        if (!isPaused && isActive()) {
            stop();
            elapsedTime = (int) (System.currentTimeMillis() - startTime);
            isPaused = true;
        }
    }

    public void resume() {
        if (isPaused) {
            int iterval_tmp = interval - elapsedTime;
            iterval_tmp = iterval_tmp > 0 ? iterval_tmp : 0;
            startWithPause(iterval_tmp);
            isPaused = false;
        }
    }
}
