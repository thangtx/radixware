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

import java.util.Comparator;


public class ReleaseUtilsCommon {
    
    public static final String VERSION_DELIMITER = "\\.|-";
    
    //From ReleaseSettings
    public static int[] parseVersionStr(String s) {
        if (s == null || s.isEmpty()) {
            return new int[]{0};
        } else {
            String[] strings = s.split(VERSION_DELIMITER);
            if (strings.length == 0) {
                return new int[]{0};
            }
            int result[] = new int[strings.length];
            for (int i = 0; i < strings.length; i++) {
                try {
                    result[i] = Integer.parseInt(strings[i]);
                } catch (NumberFormatException e) {
                    return new int[]{0};
                }
            }
            return result;
        }
    }

    //From ReleaseSettings
    public static boolean isValidReleaseName(String s, boolean isPatch) {
        if (s.isEmpty()) {
            return false;
        }
        boolean wasPatchNumber = false;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '0') {
                if (i > 0 && !(Character.isDigit(s.charAt(i - 1)) || s.charAt(i - 1) != '.')) {
                    return false;
                }
                if (i == 0 && s.length() > 1 && s.charAt(1) != '.') {
                    return false;
                }
            } else if (c == '.') {
                if (i == 0) {
                    return false;
                }
                if (i >= s.length() - 1) {
                    return false;
                } else {
                    if (!Character.isDigit(s.charAt(i + 1))) {
                        return false;
                    }
                }
            } else if (c == '-') {
                wasPatchNumber = true;
                if (!isPatch) {
                    return false;
                }
                if (i == 0) {
                    return false;
                }
                if (i == s.length() - 1) {
                    return false;
                }
                for (int j = i + 1; j < s.length(); j++) {
                    if (!Character.isDigit(s.charAt(j))) {
                        return false;
                    }
                }
            } else if (!Character.isDigit(c)) {
                return false;
            }
        }
        if (isPatch) {
            return wasPatchNumber;
        } else {
            return true;
        }
    }

    public static String mergeVersionStr(int[] version, boolean patch) {
        if (version.length == 0) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i
                < version.length; i++) {
            if (i > 0) {
                if (patch && i == version.length - 1) {
                    result.append('-');
                } else {
                    result.append('.');
                }
            }
            result.append(String.valueOf(version[i]));
        }
        return result.toString();
    }

    public static boolean isExpired(String lastReleaseNumberStr, String expirationReleaseStr) {
        if (lastReleaseNumberStr == null || expirationReleaseStr == null) {
            return false;
        }
        if (lastReleaseNumberStr.equals(expirationReleaseStr)) {
            return true;
        }
        int res = new ReleaseStringComporator().compare(lastReleaseNumberStr, expirationReleaseStr);
        return res >= 0;
    }
    
    public static class ReleaseStringComporator implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            int[] first = parseVersionStr(o1);
            int[] second = parseVersionStr(o2);
            int len = first.length > second.length ? first.length : second.length;
            for (int i = 0; i < len; i++) {
                int f = i < first.length ? first[i] : 0;
                int s = i < second.length ? second[i] : 0;
                int res = f - s;
                if (res != 0) {
                    return res;
                }
            }
            return 0;
        }
    }
}
