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

package org.radixware.kernel.designer.common.general.utils;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.openide.util.Mutex;
import org.openide.util.Mutex.Action;
import org.openide.util.Mutex.ExceptionAction;
import org.openide.util.MutexException;


public class RadixMutex {

    private static final Mutex mutex = new Mutex();

    private static Mutex mutex() {
        return mutex;
    }

    public static void readAccess(Runnable action) {
        mutex().readAccess(action);
    }

    public static void writeAccess(Runnable action) {
        mutex().writeAccess(action);
    }

    public static <T> T writeAccess(ExceptionAction<T> action) throws MutexException {
        return mutex().writeAccess(action);
    }

    public static <T> T writeAccess(Action<T> action) {
        return mutex().writeAccess(action);
    }

    public static <T> T readAccess(ExceptionAction<T> action) throws MutexException {
        return mutex().readAccess(action);
    }

    public static <T> T readAccess(Action<T> action) {
        return mutex().readAccess(action);
    }
    private static final Lock checkLock = new ReentrantLock();

    public static Lock getLongProcessLock() {
        return checkLock;
    }
}
