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

package org.radixware.kernel.common.defs.ads.clazz.algo.generation;

import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.*;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EDwfFormSubmitVariant;


public final class AppUtils {
    public final static AdsTypeDeclaration SUBMITVARIANT_TYPE = AdsTypeDeclaration.Factory.newPlatformClass("org.radixware.kernel.common.enums.EDwfFormSubmitVariant");
    public final static AdsTypeDeclaration SUBMITVARIANT_STR_MAP_TYPE = AdsTypeDeclaration.Factory.newPlatformClass("java.util.HashMap<org.radixware.kernel.common.enums.EDwfFormSubmitVariant, String>");
    public final static AdsTypeDeclaration SUBMITVARIANT_BOOL_MAP_TYPE = AdsTypeDeclaration.Factory.newPlatformClass("java.util.HashMap<org.radixware.kernel.common.enums.EDwfFormSubmitVariant, Boolean>");
    public final static AdsTypeDeclaration STR_STR_MAP_TYPE = AdsTypeDeclaration.Factory.newPlatformClass("java.util.Map<String, String>");
    
    public static boolean isExecutable(AdsEdge edge) {
        return true;
    }

    public static boolean isTraceable(AdsEdge edge) {
        final String text = String.valueOf(edge.getTraceSource());
        return text != null && text.compareTo("") != 0;
    }

    public static boolean isExecutable(AdsBaseObject node) {
        return
                node instanceof AdsProgramObject ||
                node instanceof AdsIncludeObject ||
                node instanceof AdsScopeObject ||
                node instanceof AdsThrowObject ||
                node instanceof AdsCatchObject ||
                node instanceof AdsFinishObject ||
                node instanceof AdsReturnObject ||
                node instanceof AdsTermObject ||
                node instanceof AdsForkObject ||
                node instanceof AdsMergeObject ||
                node instanceof AdsAppObject ||
                node instanceof AdsEmptyObject;
    }

    public static boolean isExecutableCode(AdsBaseObject node) {
        return node instanceof IJavaSource;
    }

    public static Map<EDwfFormSubmitVariant, String> parseAsStrMap(final String text) { // example {OK=Text1, Cancel=Text2}
        final Map<EDwfFormSubmitVariant, String> v2s = new HashMap<EDwfFormSubmitVariant, String>();
        if (text == null)
            return v2s;        
        
        String s = text;
        s = s.substring(1, s.length()-1);
        
        final String[] entries = s.split(",");
        for (String e: entries) {
            final String[] p = e.split("=");
            v2s.put(EDwfFormSubmitVariant.getForValue(p[0]), p[1]);
        }

        return v2s;
    }
    
    public static Map<EDwfFormSubmitVariant, Boolean> parseAsBoolMap(final String text) { // example {OK=true, Cancel=false}
        final Map<EDwfFormSubmitVariant, Boolean> v2b = new HashMap<EDwfFormSubmitVariant, Boolean>();
        if (text == null)
            return v2b;
        
        for (Map.Entry<EDwfFormSubmitVariant, String> e: parseAsStrMap(text).entrySet()) {
            v2b.put(e.getKey(), Boolean.valueOf(e.getValue()));
        }
        
        return v2b;
    }
    
    public static String mergeAsStrMap(final Map<EDwfFormSubmitVariant, String> v2s) {
        if (v2s == null || v2s.isEmpty())
            return null;
        
        int i = 0;
        String s = "{";
        for (Map.Entry<EDwfFormSubmitVariant, String> e: v2s.entrySet()) {
            s += e.getKey().getValue() + "=" + e.getValue();
            if (++i < v2s.size())
                s += ",";
        }
        
        s += "}";
        return s;
    }
    
    public static String mergeAsBoolMap(final Map<EDwfFormSubmitVariant, Boolean> v2b) {
        if (v2b == null || v2b.isEmpty())
            return null;
        
        final Map<EDwfFormSubmitVariant, String> v2s = new HashMap<EDwfFormSubmitVariant, String>();
        for (Map.Entry<EDwfFormSubmitVariant, Boolean> e: v2b.entrySet()) {
            v2s.put(e.getKey(), String.valueOf(e.getValue()));
        }
        
        return mergeAsStrMap(v2s);
    }
}
