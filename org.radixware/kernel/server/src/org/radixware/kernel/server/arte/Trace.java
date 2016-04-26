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

package org.radixware.kernel.server.arte;

import java.util.List;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;

/**
 * ARTE Trace
 *

 *
 */
public class Trace extends org.radixware.kernel.server.trace.Trace {

    public static class Factory {

        public static Trace newInstance(final Arte arte) {
            final Trace trace = new Trace(arte);
            final LocalTracer tracer = new LocalTracer() {
                @Override
                public long getMinSeverity() {
                    return trace.getMinSeverity();
                }

                @Override
                public long getMinSeverity(final String eventSource) {
                    return trace.getMinSeverity(eventSource);
                }

                @Override
                public void put(
                        final EEventSeverity severity,
                        final String localizedMess,
                        final String code,
                        final List<String> words,
                        final boolean isSensitive) {
                    if (code == null) {
                        trace.put(severity, localizedMess, EEventSource.INSTANCE.getValue(), isSensitive);
                    } else {
                        trace.put(code, words, isSensitive);
                    }
                }
            };
            trace.initDbLog(arte.getDbConnection().get(), tracer);
            return trace;
        }
    }

    protected Trace(final Arte arte) {
        super(arte);
    }

    public LocalTracer newTracer(final String eventSource) {
        return new LocalTracer() {
            @Override
            public void put(EEventSeverity severity, String localizedMess, String code, List<String> words, boolean isSensitive) {
                if (code == null) {
                    Trace.this.put(severity, localizedMess, eventSource);
                } else {
                    Trace.this.put(severity, code, words, eventSource, isSensitive);
                }
            }

            @Override
            public long getMinSeverity() {
                return Trace.this.getMinSeverity();
            }

            @Override
            public long getMinSeverity(String eventSource) {
                return Trace.this.getMinSeverity(eventSource);
            }
        };
    }

    @Deprecated
    public final String exceptionStackToString(final Throwable e) {
//        try {
//            if (e != null) {
//                StackTraceElement[] stack = e.getStackTrace();
//                StringBuffer b = new StringBuffer(stack.length * 200);
//                b.append(e);
//                if (stack != null) {
//                    HashMap<String, String> ks = new HashMap<String, String>();
//                    for (int i = 0; i < stack.length; i++) {
//                        b.append("\n\tat ");
//                        b.append(traceStackElementToString(stack[i], ks));
//                    }
//                }
//                if (e.getCause() != null) {
//                    b.append("Caused by\n");
//                    b.append(exceptionStackToString(e.getCause()));
//                }
//                return b.toString();
//            }
//        } catch (Throwable ex) {
//            put(EEventSeverity.ERROR, "Error on exception stack parsing: " + ExceptionTextFormatter.exceptionStackToString(ex) + "\nParsed exception stack is: " + ExceptionTextFormatter.exceptionStackToString(e), EEventSource.ARTE);
//        }
        return ExceptionTextFormatter.exceptionStackToString(e);
    }
}
