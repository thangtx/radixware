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

package org.radixware.kernel.designer.common.dialogs.scmlnb.finder;

import java.util.regex.Pattern;


public final class ScmlFinderFactory {

    private static final String regexMetaChars = "([{\\^-$|]})?*+.";
    private static final String escapeChars = "?*";

    private ScmlFinderFactory() {
    }

    public static IFinder createRegexSubstringFinder(final String searchString, final TagTextFactory factory, final boolean matchCase, final boolean wholeWords, final boolean trueRegex) {
        return new RegexSubstringFinder(createPattern(searchString, matchCase, wholeWords, trueRegex), factory);
    }

    public static IFinder createTitleFinder(final String searchString, final boolean matchCase, final boolean wholeWords, final boolean trueRegex) {
        return new TitleFinder(createPattern(searchString, matchCase, wholeWords, trueRegex));
    }
    
    public static IFinder createEventCodePropFinderfinal(String searchString, final boolean matchCase, final boolean wholeWords, final boolean trueRegex) {
        return new EventCodePropTextFinder(createPattern(searchString, matchCase, wholeWords, trueRegex));
    }

    private static Pattern createPattern(final String searchString, final boolean matchCase, final boolean wholeWords, final boolean trueRegex) {
        if (searchString == null || searchString.length() == 0) {
            throw new IllegalArgumentException("Can no search for empty string");
        }
        int flags = 0;
        if (!matchCase) {
            flags |= Pattern.CASE_INSENSITIVE;
        }
        Pattern pattern;
        if (trueRegex) {
            pattern = Pattern.compile(searchString, flags);
        } else {
            String patternString = convertStringToRegex(searchString);
            if (wholeWords) {
                patternString = new StringBuffer().append("\\b").append(patternString).append("\\b").toString();
            }
            pattern = Pattern.compile(patternString, flags);
        }
        return pattern;
    }

    private static String convertStringToRegex(final String searchString) {
        final StringBuilder patternSb = new StringBuilder();
        boolean inEscape = false;
        char inputChar;
        for (int i = 0; i < searchString.length(); i++) {
            inputChar = searchString.charAt(i);
            if (inEscape) {
                if (inputChar == '\\') {
                    patternSb.append("\\\\");
                } else if (escapeChars.lastIndexOf(inputChar) != -1) {
                    patternSb.append('\\').append(inputChar);
                } else {
                    patternSb.append("\\\\").append(inputChar);
                }
                inEscape = false;
            } else {
                switch (inputChar) {
                    case '\\':
                        inEscape = true;
                        break;
                    case '*':
                        patternSb.append(".*");
                        break;
                    case '?':
                        patternSb.append(".");
                        break;
                    default:
                        if (regexMetaChars.indexOf(inputChar) != -1) {
                            patternSb.append('\\').append(inputChar);
                        } else {
                            patternSb.append(inputChar);
                        }
                }

            }
        }
        return patternSb.toString();
    }
}
