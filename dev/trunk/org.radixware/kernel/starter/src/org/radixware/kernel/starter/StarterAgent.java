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
package org.radixware.kernel.starter;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Field;

public class StarterAgent {
    private final static String[] JVM_KEYWORDS = {"DYNAMIC", "CODE", "EVOLUTION"};
    private static Instrumentation instrumentation = null;
    private static File res;

    public static void premain(final String agentArgs, final Instrumentation instr) {
        instrumentation = instr;
    }

    public static boolean isValidJVM4DynamicCodeEvolution(final String jvmName) {
        if (jvmName == null || jvmName.isEmpty()) {
            throw new IllegalArgumentException("JVM name to test can't be null or empty");
        } else {
            final String upperCaseJvm = jvmName.toUpperCase();

            for (String item : JVM_KEYWORDS) {
                if (!upperCaseJvm.contains(item)) {
                    return false;
                }
            }
            return true;
        }
    }

    public static Instrumentation installInstrumentation() throws IOException {
        try{if (instrumentation == null) {
                try {
                    final Class<?> cl = StarterAgent.class.getClassLoader().getParent().loadClass(StarterAgent.class.getCanonicalName());
                    final Field f = cl.getDeclaredField("instrumentation");

                    f.setAccessible(true);
                    instrumentation = (Instrumentation) f.get(null);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            final Instrumentation instr = instrumentation;
            
            if (instr == null) {
                throw new IOException("Instrumentation is not installed properly (premain(...) method was not called). Check -for the given JVM doesn't support class redefinition. Set Can-redefine-classes option in the jar manifest");
            }
            else if (!instr.isRedefineClassesSupported()) {
                throw new IOException("Instrumentation for the given JVM doesn't support class redefinition. Set Can-redefine-classes option in the jar manifest");
            } else {
                return instr;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Error installing instrumentation: " + e.getClass().getSimpleName() + " (" + e.getLocalizedMessage() + ")", e);
        }
    }
}
