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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.lang.MetaInfo;

public class TraceParser {
    
    private static final Pattern RADIX_SOURCE_LINE = Pattern.compile(".+at\\s.+\\.ads\\.mdl[A-Z0-9_]{26}\\..+\\(.+\\.java\\:[0-9]+\\)");
    private static final Pattern RADIX_ID = Pattern.compile("^[a-z]{3,6}[A-Z0-9_]{26}$");

    private final ClassLoader classLoader;
    public TraceParser(final ClassLoader classLoader) {
        this.classLoader = classLoader;
    }
    
    public String parse(final String traceText) {
        if (traceText == null || traceText.isEmpty())
            return traceText;
        
        String res = "";        
        final String[] split = traceText.split("\n");
        for (String line : split) {
            res += parseLine(line) + "\n";
        }
        if (!res.isEmpty())
            res = res.substring(0, res.length()-1);
        
        return res;
    }
    
    private String parseLine(String line) {
        if (!RADIX_SOURCE_LINE.matcher(line).matches()) {
            return line;
        }

        int at = line.indexOf("at ") + 3;
        final String prefix = line.substring(0, at);
        final int lbracket = line.indexOf("(", at);
        final String fullName = line.substring(at, lbracket);
        
        final int colon = line.indexOf(":", lbracket + 1);
        final int rbracket = line.indexOf(")", colon + 1);
        final int lineNumber = Integer.decode(line.substring(colon + 1, rbracket));

        final int pnt = fullName.lastIndexOf(".");
        final String className = fullName.substring(0, pnt);
        final String methodName = fullName.substring(pnt + 1);
//        if (!RADIX_ID.matcher(methodName).matches())
//            return line;

        try {
            final Class cl = classLoader.loadClass(className);

            // check constructors
            Constructor ct = null;
            for (Constructor c : cl.getDeclaredConstructors()) {
                if (c.getName().equals(methodName)) {
                    ct = c;
                    break;
                }
            }
            if (ct != null && ct.isAnnotationPresent(MetaInfo.class)) {
                final MetaInfo metaInfo = (MetaInfo)ct.getAnnotation(MetaInfo.class);
                return prefix + metaInfo.name() + " (line:" + (lineNumber - metaInfo.line()) + ")";
            }
            
            // check methods
            Method mf = null;
            for (Method m : cl.getDeclaredMethods()) {
                if (m.getName().equals(methodName)) {
                    mf = m;
                    break;
                }
            }
            if (mf != null && mf.isAnnotationPresent(MetaInfo.class)) {
                final MetaInfo metaInfo = (MetaInfo)mf.getAnnotation(MetaInfo.class);
                return prefix + metaInfo.name() + " (line:" + (lineNumber - metaInfo.line()) + ")";
            }
            
            // default
            if (cl.isAnnotationPresent(MetaInfo.class)) {
                final MetaInfo metaInfo = (MetaInfo)cl.getAnnotation(MetaInfo.class);
                return prefix + metaInfo.name() + ":" + methodName;
            } else
                return line;
        } catch (ClassNotFoundException e) {
            return line;
        } catch (NumberFormatException | NoConstItemWithSuchValueError e) {
            final String msg = String.format(e.getMessage() + " => %s", line);
            Logger.getLogger(TraceParser.class.getName()).log(Level.INFO, msg);
            return line;
        }
    }
}