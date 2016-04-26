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
package org.radixware.kernel.common.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.types.IKernelEnum;
import org.radixware.kernel.common.types.IKernelIntEnum;
import org.radixware.kernel.common.types.IKernelStrEnum;
import org.radixware.kernel.common.types.Id;

// TODO: rename
public class Utils {

    /**
     * Compare two objects.
     *
     * @return return true if both are null or equals.
     */
    public static boolean equals(Object obj1, Object obj2) {//NOPMD
        if (obj1 == null && obj2 == null) {
            return true;
        }
        if (obj1 != null && obj2 != null) {
            return obj1.equals(obj2);
        }
        return false;
    }

    public static boolean equalsNotNull(Object obj1, Object obj2) {
        if (obj1 != null && obj2 != null) {
            return obj1.equals(obj2);
        }
        return false;
    }

    public static boolean emptyOrNull(final String x) {
        return x == null || x.isEmpty();
    }

    public static boolean aliasesAreEqual(String alias, String tableAlias) {
        return emptyOrNull(alias) ? emptyOrNull(tableAlias) : alias.equals(tableAlias);
    }

    public static Object nvl(Object obj, Object def) {
        return obj == null ? def : obj;
    }

    public static <E extends Object> boolean equalsCollection(Collection<E> c1, Collection<E> c2) {
        if (c1 == null && c2 == null) {
            return true;
        }
        if (c1 == null || c2 == null) {
            return false;
        }
        return (new HashSet(c1)).equals(new HashSet(c2));
    }

    public static <T extends IKernelEnum> T toEnum(Object obj, Class<T> clazz) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof IKernelEnum) {
            return (T) obj;
        }
        try {
            if (clazz != null && IKernelIntEnum.class.isAssignableFrom(clazz)) {
                java.lang.reflect.Method m = clazz.getMethod("getForValue", new Class<?>[]{Long.class});
                return (T) m.invoke(null, new Object[]{obj});
            }
            if (clazz != null && IKernelStrEnum.class.isAssignableFrom(clazz)) {
                java.lang.reflect.Method m = clazz.getMethod("getForValue", new Class<?>[]{String.class});
                return (T) m.invoke(null, new Object[]{obj});
            }
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new IllegalUsageError("To enum casting error", ex);
        }
        return null;
    }
    /*
     * public static Object toEnum(Object obj, Class<? extends IKernelEnum>
     * clazz) { if (obj == null) return null; if (obj instanceof IKernelEnum)
     * return obj; try { if (clazz != null &&
     * IKernelIntEnum.class.isAssignableFrom(clazz)) { java.lang.reflect.Method
     * m = clazz.getMethod("getForValue", new Class<?>[] { Long.class }); return
     * m.invoke(null, new Object[] { obj }); } if (clazz != null &&
     * IKernelStrEnum.class.isAssignableFrom(clazz)) { java.lang.reflect.Method
     * m = clazz.getMethod("getForValue", new Class<?>[] { String.class });
     * return m.invoke(null, new Object[] { obj }); } } catch (Exception ex) {
     * throw new IllegalUsageError("To enum casting error", ex); } return null;
     * }
     */

    public static String toString(Object obj) {
        return obj == null ? null : String.valueOf(obj);
    }
    public static final String RADIX_PREFERENCES_KEY = "RadixWare";

    public static Preferences findPreferences(final String key) throws BackingStoreException {
        if (Preferences.userRoot().nodeExists(RADIX_PREFERENCES_KEY)) {
            final Preferences designerPreferences = Preferences.userRoot().node(RADIX_PREFERENCES_KEY);
            if (designerPreferences.nodeExists(key)) {
                return designerPreferences.node(key);
            }
        }
        return null;
    }

    public static Preferences findPreferencesWithoutException(final String key) {
        try {
            return findPreferences(key);
        } catch (BackingStoreException ex) {
        }
        return null;
    }

    public static Preferences findOrCreatePreferences(final String key) {
        return Preferences.userRoot().node(RADIX_PREFERENCES_KEY).node(key);
    }

    public static void appendReferenceToolTipHtml(Definition def, String refernenceDesc, StringBuilder sb) {
        if (def != null) {
            if (refernenceDesc != null) {
                sb.append("<br>");
                sb.append(refernenceDesc);
                sb.append(":<br>&nbsp;");
            }
            final String location = def.getQualifiedName();
            sb.append("<a href=\"");

            Id[] ids = def.getIdPath();
            boolean first = true;
            for (int i = 0; i < ids.length; i++) {
                if (first) {
                    first = false;
                } else {
                    sb.append(':');
                }
                sb.append(ids[i].toCharArray());
            }

            sb.append("\">");
            sb.append(location);
            sb.append("</a>");
        }
    }

    public static int compareVersions(String v1, String v2) throws NumberFormatException {
        final StringTokenizer st1 = new StringTokenizer(v1, ".");
        final StringTokenizer st2 = new StringTokenizer(v2, ".");
        while (true) {
            final String n1 = st1.nextToken();
            final String n2 = st2.nextToken();
            final Long l1 = Long.valueOf(n1);
            final Long l2 = Long.valueOf(n2);
            int i = l1.compareTo(l2);
            if (i != 0) {
                return i;
            }
            if (st2.hasMoreTokens() && !st1.hasMoreTokens()) {
                return -1;
            }
            if (st1.hasMoreTokens() && !st2.hasMoreTokens()) {
                return 1;
            }
            if (!st1.hasMoreTokens() && !st1.hasMoreTokens()) {
                return 0;
            }
        }
    }

    public static String stackToString(final StackTraceElement[] stack) {
        final StringBuffer b = new StringBuffer(stack.length * 200);
        for (int i = 0; i < stack.length; i++) {
            b.append("\tat ");
            b.append(stack[i]);
            b.append('\n');
        }
        return b.toString();
    }

    public static boolean isNotNull(Object... objects) {

        if (objects == null) {
            return false;
        }

        for (final Object object : objects) {
            if (object == null) {
                return false;
            }
        }

        return true;
    }

    public static String[] merge(String[]... array) {
        if (array == null) {
            return null;
        }
        if (array.length == 0) {
            return new String[0];
        }
        int len = 0;
        for (String[] arr : array) {
            if (arr != null) {
                len += arr.length;
            }
        }
        String[] result = new String[len];
        int start = 0;
        for (String[] arr : array) {
            if (arr != null) {
                System.arraycopy(arr, 0, result, start, arr.length);
                start += arr.length;
            }
        }
        return result;

    }

    public static int indexOf(char[] array, char item, int startFrom) {
        for (int i = startFrom; i < array.length; i++) {
            if (array[i] == item) {
                return i;
            }
        }
        return -1;
    }

    public static List<String> parseLines(String text, String delim) {
        List<String> lines = new ArrayList();
        if (text == null || text.isEmpty()) {
            return lines;
        }

        StringTokenizer stringTokenizer = new StringTokenizer(text, delim);

        while (stringTokenizer.hasMoreTokens()) {
            String s = stringTokenizer.nextToken();
            s = s.trim();
            lines.add(s);
        }
        return lines;
    }

    public static List<String> parseLines(final String text) {
        List<String> lines = new LinkedList();
        if (text == null || text.isEmpty()) {
            return lines;
        }

        int last = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '\n') {
                if (last == i - 1) {
                    lines.add("");
                } else {
                    lines.add(text.substring(last + 1, i));
                }
                last = i;
            }
        }

        if (last == text.length() - 1) {
            lines.add("");
        } else {
            lines.add(text.substring(last + 1));
        }

        return lines;
    }

    public static String mergeLines(List<String> lines) {
        return mergeLines(lines, "\n");
    }

    public static String mergeLines(List<String> lines, String delim) {
        if (lines == null || lines.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;

        for (String line : lines) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(delim);
            }
            sb.append(line);
        }
        return sb.toString();
    }

    public static Object convertValueToEnumAcceptable(Object value, EValType valType) {
        if (value == null) {
            return value;
        }
        switch (valType) {
            case CHAR:
                if (value instanceof Character) {
                    return value;
                } else if (value instanceof String) {
                    String asStr = (String) value;
                    if (asStr.isEmpty()) {
                        return null;
                    } else {
                        return asStr.charAt(0);
                    }
                } else {
                    return value;
                }
            case STR:
                if (value instanceof Character) {
                    return String.valueOf(((Character) value).charValue());
                } else if (value instanceof String) {
                    return value;
                } else {
                    return value;
                }
            case INT:
                if (value instanceof Long) {
                    return (Long) value;
                } else {
                    return value;
                }
            default:
                return value;
        }
    }
    
    public static byte[] compressString(final String input) throws IOException{
        if (input==null) {
            return null;
        }
        if (input.isEmpty()){
            return new byte[0];
        }        
        final ByteArrayOutputStream obj=new ByteArrayOutputStream();
        try (GZIPOutputStream gzip = new GZIPOutputStream(obj)) {
            gzip.write(input.getBytes("UTF-8"));
        }
        return obj.toByteArray();
    }
    
    public static String decompressString(final byte[] compressedData) throws IOException{
        if (compressedData==null){
            return null;
        }
        if (compressedData.length==0){
            return "";
        }
        try(GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(compressedData))){
            final BufferedReader bf = new BufferedReader(new InputStreamReader(gis, "UTF-8"));
            final StringBuilder strBuilder = new StringBuilder();
            boolean firstLine = true;
            String line;
            while ((line=bf.readLine())!=null) {             
              if (firstLine){
                  firstLine = false;
              }else{
                  strBuilder.append('\n');
              }
              strBuilder.append(line);              
            }
            return strBuilder.toString();
        }
    }
    
    public static String readableTime(final long timeMillis) {
        return new SimpleDateFormat("YYYYMMddHHmmssSSS").format(new Date(timeMillis));
    }
}
