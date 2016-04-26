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

package org.radixware.kernel.common.trace;

import java.util.HashMap;
import java.util.List;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.environment.IRadixClassLoader;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;


public interface IRadixTrace {

    /**
     * Helper class for simply radix trace access see {@linkplain Lookup#getInstance(java.lang.Class)}
     */
    public static final class Lookup {

        /**
         * Tries to find an instance of IRadixTrace according to given context
         *
         * @return instance of IRadixTrace if context is loaded by {@linkplain  IRadixClassLoader}
         * or if current thread is implementing {@linkplain IRadixTraceProvider},
         * otherwise null
         */
        public static IRadixTrace findInstance(Class context) {
            if (context != null && context.getClassLoader() instanceof IRadixClassLoader) {
                IRadixClassLoader cl = (IRadixClassLoader) context.getClassLoader();
                return cl.getEnvironment().getTrace();
            } else if (Thread.currentThread() instanceof IRadixTraceProvider) {
                return ((IRadixTraceProvider) Thread.currentThread()).getTrace();
            }
            return null;
        }
    }

    public static class ExceptionStackFormatter {

        private final IRadixTrace trace;

        public ExceptionStackFormatter(IRadixTrace trace) {
            this.trace = trace;
        }

        public String traceStackElementToString(StackTraceElement e, HashMap<String, String> knownSources) {
            return e.toString();
        }

        public String exceptionStackToString(final Throwable e) {
            try {
                if (e != null) {
                    StackTraceElement[] stack = e.getStackTrace();
                    StringBuffer b = new StringBuffer(stack.length * 200);
                    b.append(e);
                    if (stack != null) {
                        HashMap<String, String> ks = new HashMap<String, String>();
                        for (int i = 0; i < stack.length; i++) {
                            b.append("\n\tat ");
                            b.append(traceStackElementToString(stack[i], ks));
                        }
                    }
                    if (e.getCause() != null) {
                        b.append("Caused by\n");
                        b.append(exceptionStackToString(e.getCause()));
                    }
                    return b.toString();
                }
            } catch (Throwable ex) {
                trace.put(EEventSeverity.ERROR, "Error on exception stack parsing: " + ExceptionTextFormatter.exceptionStackToString(ex) + "\nParsed exception stack is: " + ExceptionTextFormatter.exceptionStackToString(e), EEventSource.ARTE);
            }
            return ExceptionTextFormatter.exceptionStackToString(e);
        }
    }

    public void put(final EEventSeverity severity, final String localizedMess, final EEventSource source);

    public void put(final EEventSeverity severity, final String code, final List<String> words, final String source);
}
