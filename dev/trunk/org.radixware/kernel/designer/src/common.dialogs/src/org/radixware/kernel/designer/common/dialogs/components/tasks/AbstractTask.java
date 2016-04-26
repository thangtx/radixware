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

package org.radixware.kernel.designer.common.dialogs.components.tasks;

import java.util.concurrent.atomic.AtomicReference;


public abstract class AbstractTask implements Runnable {

    private TaskProcessor processor;

    /**
     * Start this task in new thread
     * @return running TaskProcessor
     */
    public TaskProcessor startTask() {
        synchronized (this) {
            processor = new TaskProcessor(this);
            processor.start();
        }
        return processor;
    }

    /**
     * Only for used in method {@link run}()
     *
     * @return state of running TaskProcessor
     */
    protected final AtomicReference<TaskState> getState() {
        TaskProcessor taskProcessor = getProcessor();

        assert taskProcessor != null;

        if (taskProcessor != null) {
            return taskProcessor.state;
        }
        return null;
    }

    /**
     * Only for used in method {@link run}()
     *
     * @return current running TaskProcessor
     */
    protected final TaskProcessor getProcessor() {
        Thread currentThread = Thread.currentThread();

        assert currentThread instanceof TaskProcessor;
        
        if (currentThread instanceof TaskProcessor) {
            return (TaskProcessor) currentThread;
        }
        return null;
    }
}
