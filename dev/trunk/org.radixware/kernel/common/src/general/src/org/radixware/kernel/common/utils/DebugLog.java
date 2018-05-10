/*
 * Copyright (c) 2008-2017, Compass Plus Limited. All rights reserved.
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

import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.radixware.kernel.common.enums.EEventSeverity;

public class DebugLog {
    private static final boolean ENABLED = Boolean.getBoolean("rdx.kernel.debug.log.enabled");
    private static final boolean WRITE_THREAD_NAME = Boolean.getBoolean("rdx.kernel.debug.log.write.thread.name");
    private static final String LOG_NAME = "rdx.kernel.debug.log";
    private static final EEventSeverity LOG_SEVERITY = EEventSeverity.getForName(System.getProperty("rdx.kernel.debug.log.severity", EEventSeverity.EVENT.getName()));
    private static final int ANGLE_BRACKETS_REPEAT = 0;
    private static final int WHITESPACES_REPEATS = 4;
    
    final static ThreadLocal<DebugLog> INSTANCE = new ThreadLocal<DebugLog>() {
        @Override
        protected DebugLog initialValue() {
            return new DebugLog();
        }
    };
    
    private int logicalOffset = 0;
    private int physicalOffset = 0;
    private final boolean[] logicalOffsetUsed = new boolean[65536];
    
    
    private void outputToTrace(String s) {
            final Log log = LogFactory.getLog(s == null ? "" : LOG_NAME);
            switch (LOG_SEVERITY) {
                case ALARM:
                    log.fatal(s);
                    break;
                case ERROR:
                    log.error(s);
                    break;
                case WARNING:
                    log.warn(s);
                    break;
                case EVENT:
                    log.info(s);
                    break;
                default:
                    log.debug(s);
            }
    }
    
    private String wrapString(String s) {
        final String ws = (WRITE_THREAD_NAME ? Thread.currentThread().getName() + " : " : "")
                + StringUtils.repeat(StringUtils.repeat(">", ANGLE_BRACKETS_REPEAT), physicalOffset)
                + StringUtils.repeat(StringUtils.repeat(" ", WHITESPACES_REPEATS), physicalOffset) + s;
        return ws;
    }
    
    private void offsetUp() {
        if (logicalOffsetUsed[logicalOffset++])
            physicalOffset++;
        logicalOffsetUsed[logicalOffset] = false;
        if (physicalOffset == 0 && logicalOffset == 1)
            outputToTrace(null);
    }
    
    private void offsetDown() {
        if (logicalOffsetUsed[--logicalOffset])
            --physicalOffset;
        
    }
    
    private void logLocal(String s) {
        outputToTrace(wrapString(s));
        logicalOffsetUsed[logicalOffset] = true;
    }
    
    public static final void log(boolean predicate, String s) {
        if (!ENABLED || !predicate) return;
        INSTANCE.get().logLocal(s);
    }
    
    public static final void log(String s) {
        log(true, s);
    }
    
    public static final void logUpDown(boolean predicate, String s) {
        if (!ENABLED || !predicate) return;
        INSTANCE.get().offsetUp();
        log(s);
        INSTANCE.get().offsetDown();
    }
    
    public static final void logUpDown(String s) {
        logUpDown(true, s);
    }
    
    public static final void blockIn(boolean predicate, String s) {
        if (!ENABLED || !predicate) return;
        log(s);
        INSTANCE.get().offsetUp();
    }
    
    public static final void blockIn(String s) {
        blockIn(true, s);
    }
    
    public static final void blockOut(boolean predicate, String s) {
        if (!ENABLED || !predicate) return;
        INSTANCE.get().offsetDown();
        log(s);
    }
    
    public static final void blockOut(String s) {
        blockOut(true, s);
    }
    
    public static final void funcIn(boolean predicate, String s) {
        if (!ENABLED || !predicate) return;
        INSTANCE.get().offsetUp();
        blockIn(s);
    }
    
    public static final void funcIn(String s) {
        funcIn(true, s);
    }
    
    public static final void funcOut(boolean predicate, String s) {
        if (!ENABLED || !predicate) return;
        blockOut(s);
        INSTANCE.get().offsetDown();
    }
    
    public static final void funcOut(String s) {
        funcOut(true, s);
    }
    
    public static final String certToString(X509Certificate cert) {
        if (cert == null || !ENABLED)
            return null;
        String res = "[SerialNumber = " + cert.getSerialNumber().toString(16)
                + ";   IssuerDN = " + cert.getIssuerDN()
                + ";   SubjectDN = " + cert.getSubjectDN()
                + "]";
        return res;
    }

    public static final String[] certsToStringArr(X509Certificate[] certs) {
        if (certs == null || !ENABLED)
            return null;
        final String[] res = new String[certs.length];
        for (int i = 0; i < res.length; ++i) {
            res[i] = DebugLog.certToString(certs[i]);
        }
        return res;
    }
    
    public static final String certsToString(X509Certificate[] certs) {
        if (certs == null || !ENABLED)
            return null;
        return StringUtils.join(certsToStringArr(certs), "\n");
    }
    
    public static final String sizeDescr(List<?> list) {
        return list == null ? "null-list" : "list(" + list.size() + ")";
    }
    
    public static final String toString(String className, Object... info) {
        final StringBuilder sb = new StringBuilder(className).append("[");
        for (int i = 0; i < info.length - 1; i += 2) {
            if (i != 0) {
                sb.append(", ");
            }
            final String name = Objects.toString(info[i]);
            final String value = Objects.toString(info[i + 1]);
            sb.append(name).append(" = ").append(value);
        }
        sb.append("]");
        return sb.toString();
    }
}
