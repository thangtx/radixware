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


public class SystemPropUtils {

    public static long getLongSystemProp(final String name, final long defaultVal) {
        final String val = System.getProperty(name);
        if (val == null) {
            return defaultVal;
        }
        try {
            return Long.parseLong(val);
        } catch (Exception ex) {
            return defaultVal;
        }
    }

    public static int getIntSystemProp(final String name, final int defaultVal) {
        final String val = System.getProperty(name);
        if (val == null) {
            return defaultVal;
        }
        try {
            return Integer.parseInt(val);
        } catch (Exception ex) {
            return defaultVal;
        }
    }

    /**
     * Empty existing property is treated like true
     */
    public static boolean getBooleanSystemProp(final String name, final boolean defaultVal) {
        final String val = System.getProperty(name);
        if (val == null) {
            return defaultVal;
        }
        if (val != null && val.isEmpty()) {
            return true;
        }
        try {
            return Boolean.parseBoolean(val);
        } catch (Exception ex) {
            return defaultVal;
        }
    }

    public static String getStringSystemProp(final String name, final String defaultVal) {
        final String val = System.getProperty(name);
        return val == null ? defaultVal : val;
    }
}
