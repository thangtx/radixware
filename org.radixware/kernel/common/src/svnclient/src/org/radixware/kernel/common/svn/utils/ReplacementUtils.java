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
package org.radixware.kernel.common.svn.utils;

import java.text.ParseException;

/**
 *
 * @author npopov
 */
public class ReplacementUtils {

    public static final int VERSION_COMPONENTS_CNT = 5;
    public static final int MIN_VERSION_COMPONENTS_CNT = 3;
    public static final int X_VERSION_COMPONENT = Integer.MAX_VALUE;

    private static boolean checkVersionFormat(String[] verNumbers) {
        return MIN_VERSION_COMPONENTS_CNT <= verNumbers.length && verNumbers.length <= VERSION_COMPONENTS_CNT;
    }

    public static int[] parseVersion(final String version, final String replacementFilePath) throws ParseException {
        final boolean fromReplacementFile = replacementFilePath != null && !replacementFilePath.isEmpty();
        final String contextInfo = fromReplacementFile ? " from replacement file " + replacementFilePath : "";
        final String[] verNumbers = version.trim().split("\\.");
        if (!checkVersionFormat(verNumbers)) {
            throw new ParseException("Unable to parse layer version string '" + version + "'" + contextInfo, 0);
        }

        final int[] res = new int[VERSION_COMPONENTS_CNT];
        for (int i = 0; i < VERSION_COMPONENTS_CNT; i++) {
            if (i < verNumbers.length) {
                final String number = verNumbers[i];
                try {
                    res[i] = Integer.parseInt(number);
                } catch (Exception ex) {
                    if (i == verNumbers.length - 1 && "x".equals(number.toLowerCase())) {
                        res[i] = X_VERSION_COMPONENT;
                    } else {
                        throw new ParseException("Unable to parse layer version string '" + version + "': " + ex.getMessage() + " " + contextInfo, 0);
                    }
                }
            } else {
                res[i] = res[i - 1] == X_VERSION_COMPONENT ? X_VERSION_COMPONENT : 0;
            }
        }
        return res;
    }

}
