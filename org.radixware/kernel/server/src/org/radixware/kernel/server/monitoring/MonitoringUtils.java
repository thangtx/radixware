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
package org.radixware.kernel.server.monitoring;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import org.apache.commons.logging.LogFactory;

public class MonitoringUtils {

    static {
        ThreadMXBean tbean = null;
        boolean cpuTimeSupported = false;
        try {
            tbean = ManagementFactory.getThreadMXBean();
            if (tbean != null) {
                if (tbean.isCurrentThreadCpuTimeSupported()) {
                    tbean.getCurrentThreadCpuTime();//check
                    cpuTimeSupported = true;
                }
            }
        } catch (Throwable t) {
            try {
                LogFactory.getLog(MonitoringUtils.class).warn("Unable to init cpu monitoring support", t);
            } catch (Throwable t1) {
                //ignore
            }
        }
        THREAD_MX_BEAN = tbean;
        CUR_THREAD_CPU_TIME_SUPPORTED = cpuTimeSupported;
    }

    private static final ThreadMXBean THREAD_MX_BEAN;
    private static final boolean CUR_THREAD_CPU_TIME_SUPPORTED;

    public static long getCurrentThreadCpuTime() {
        if (CUR_THREAD_CPU_TIME_SUPPORTED) {
            return THREAD_MX_BEAN.getCurrentThreadCpuTime();
        }
        return -1;
    }

    public static long getThreadCpuTime(final long threadId) {
        if (CUR_THREAD_CPU_TIME_SUPPORTED) {
            return THREAD_MX_BEAN.getThreadCpuTime(threadId);
        }
        return -1;
    }

}
