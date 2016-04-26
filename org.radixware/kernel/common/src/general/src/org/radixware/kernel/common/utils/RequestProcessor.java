/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.common.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author akrylov
 */
public final class RequestProcessor {

    private static long index = 0;

    private RequestProcessor() {
    }
    private final static ExecutorService STARTER = Executors.newFixedThreadPool(10, new ThreadFactory() {
        @Override
        public Thread newThread(final Runnable r) {
            final Thread t = new Thread(r);
            t.setName("RadixWare Request Processor Thread #" + (index++));
            t.setDaemon(true);
            if (index > Integer.MAX_VALUE) {
                index = 0;
            }
            return t;
        }
    });

    public static Future<?> submit(final Runnable r) {
        return STARTER.submit(r);
    }

    public static <T> Future<T> submit(final Callable<T> r) {
        return STARTER.submit(r);
    }
    
    public static void shutdown(){
        STARTER.shutdown();
    }
    
    public static boolean awaitTermination(final long timeout, final TimeUnit timeUnit) throws InterruptedException{
        return STARTER.awaitTermination(timeout, timeUnit);
    }
}
