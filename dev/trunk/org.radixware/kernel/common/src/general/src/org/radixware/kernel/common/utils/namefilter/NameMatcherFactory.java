/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * Contributor(s):
 *
 * Portions Copyrighted 2007 Sun Microsystems, Inc.
 */
package org.radixware.kernel.common.utils.namefilter;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class NameMatcherFactory {

    protected NameMatcherFactory() {
    }

    private static abstract class BaseNameMatcher implements NameMatcher {

        protected String patternText;

        protected BaseNameMatcher(String patternText) {
            this.patternText = patternText;
        }
    }

    private static final class ExactNameMatcher extends BaseNameMatcher implements NameMatcher {

        public ExactNameMatcher(String patternText) {
            super(patternText);
        }

        @Override
        public final boolean accept(String name) {
            return patternText.equals(name);
        }
    }

    private static final class CaseInsensitiveExactNameMatcher extends BaseNameMatcher implements NameMatcher {

        public CaseInsensitiveExactNameMatcher(String patternText) {
            super(patternText);
        }

        @Override
        public final boolean accept(String name) {
            return patternText.equalsIgnoreCase(name);
        }
    }

    private static final class PrefixNameMatcher extends BaseNameMatcher implements NameMatcher {

        public PrefixNameMatcher(String patternText) {
            super(patternText);
        }

        @Override
        public final boolean accept(String name) {
            return name != null && name.startsWith(patternText);
        }
    }

    private static final class CaseInsensitivePrefixNameMatcher extends BaseNameMatcher implements NameMatcher {

        public CaseInsensitivePrefixNameMatcher(String patternText) {
            super(patternText.toLowerCase());
        }

        @Override
        public final boolean accept(String name) {
            return name != null && name.toLowerCase().startsWith(patternText); // commented by RADIX-3519
        }
    }

    private static final class RegExpNameMatcher implements NameMatcher {

        Pattern pattern;

        public RegExpNameMatcher(String patternText, boolean caseSensitive) {
            pattern = Pattern.compile(patternText, caseSensitive ? 0 : Pattern.CASE_INSENSITIVE);
        }

        @Override
        public final boolean accept(String name) {
            return name != null && pattern.matcher(name).matches();
        }
    }

    private static final class CamelCaseNameMatcher implements NameMatcher {

        Pattern pattern;

        public CamelCaseNameMatcher(String name) {
            if (name.length() == 0) {
                throw new IllegalArgumentException();
            }
            final StringBuilder patternString = new StringBuilder();
            for (int i = 0; i < name.length(); i++) {
                char c = name.charAt(i);
                patternString.append(c);
                if (i == name.length() - 1) {
                    patternString.append("[\\w\\p{Punct}]*");  // NOI18N
                } else {
                    patternString.append("[\\p{Lower}\\p{Digit}\\p{Punct}]*");  // NOI18N
                }
            }
            pattern = Pattern.compile(patternString.toString());
        }

        @Override
        public final boolean accept(String name) {
            return name != null && pattern.matcher(name).matches();
        }
    }

    // from org.netbeans.modules.java.source.ui.JavaTypeProvider
    private static String removeNonJavaChars(String text) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (Character.isJavaIdentifierPart(c) || c == '*' || c == '?') {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static NameMatcher createNameMatcher(String textPattern, ESearchType type) {
        switch (type) {
            case REGEXP:
            case CASE_INSENSITIVE_REGEXP:
                // from org.netbeans.modules.java.source.ui.JavaTypeProvider
                textPattern = removeNonJavaChars(textPattern);
                String pattern = type == ESearchType.CASE_INSENSITIVE_EXACT_NAME ? textPattern : textPattern + "*";
                pattern = pattern.replace("*", ".*").replace('?', '.');
                textPattern = pattern;
                break;
        }

        try {
            switch (type) {
                case EXACT_NAME:
                    return new ExactNameMatcher(textPattern);
                case CASE_INSENSITIVE_EXACT_NAME:
                    return new CaseInsensitiveExactNameMatcher(textPattern);
                case PREFIX:
                    return new PrefixNameMatcher(textPattern);
                case CASE_INSENSITIVE_PREFIX:
                    return new CaseInsensitivePrefixNameMatcher(textPattern);
                case REGEXP:
                    return new RegExpNameMatcher(textPattern, true);
                case CASE_INSENSITIVE_REGEXP:
                    return new RegExpNameMatcher(textPattern, false);
                case CAMEL_CASE:
                    return new CamelCaseNameMatcher(textPattern);
                default:
                    return null;
            }
        } catch (PatternSyntaxException ex) {
            return null;
        }
    }
}
