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


public class TaskProcessor extends Thread {

    public final AtomicReference<TaskState> state = new AtomicReference<TaskState>(TaskState.RUNNING);
    private final AbstractTask task;

    public TaskProcessor(AbstractTask task) {
        super(task);
        this.task = task;
    }

    public AbstractTask getTask() {
        return task;
    }

    /**
     * Sets state of current TaskProcessor to TaskState.CANCELLED
     */
    public void cancelTask() {
        state.set(TaskState.CANCELLED);
    }

    /**
     * Sets state of current TaskProcessor to TaskState.CANCELLED and calls interrupt method
     */
    public void cancelAndInterruptTask() {
        cancelTask();
        interrupt();
    }
}
