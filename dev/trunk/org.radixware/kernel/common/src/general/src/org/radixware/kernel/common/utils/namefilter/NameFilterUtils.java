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
package org.radixware.kernel.common.utils.namefilter;

import java.util.regex.Pattern;

public class NameFilterUtils {
    
    private static final Pattern camelCasePattern = Pattern.compile("(?:\\p{javaUpperCase}(?:\\p{javaLowerCase}|\\p{Digit}|\\.|\\$)*){2,}"); // NOI18N
    
    public static boolean isAllUpper(String text) {
        for (int i = 0; i < text.length(); i++) {
            if (!Character.isUpperCase(text.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    public static int containsWildCard(String text) {
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '?' || text.charAt(i) == '*') { // NOI18N
                return i;
            }
        }
        return -1;
    }    

    public static boolean isCamelCase(String text) {
        return camelCasePattern.matcher(text).matches();
    }
    
    public static String transformWildCardsToJavaStyle(String text) {
        final StringBuilder regexp = new StringBuilder(""); // NOI18N
        int lastWildCardPosition = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '?') { // NOI18N
                regexp.append(text.substring(lastWildCardPosition, i));
                regexp.append('.'); // NOI18N
                lastWildCardPosition = i + 1;
            } else if (text.charAt(i) == '.') { // NOI18N
                regexp.append(text.substring(lastWildCardPosition, i));
                regexp.append("\\."); // NOI18N
                lastWildCardPosition = i + 1;
            } else if (text.charAt(i) == '*') { // NOI18N
                regexp.append(text.substring(lastWildCardPosition, i));
                regexp.append(".*"); // NOI18N
                lastWildCardPosition = i + 1;
            }
        }
        regexp.append(text.substring(lastWildCardPosition, text.length()));
        return regexp.toString();
    }
}
