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

package org.radixware.kernel.designer.common.dialogs.components.selector;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


public class ItemNameFilter<TValue> extends EmptyTextFilter<TValue> {

    private String strPattern = "";
    private Pattern pattern;
    private final Pattern emptyPattern = Pattern.compile(" ");
    private final Object patternLock = new Object();

    public ItemNameFilter() {
        super();
    }

    @Override
    public boolean accept(TValue value) {
        return acceptName(value);
    }

    @Override
    protected void updatePattern() {
        updateNamePattern();
        fireChange();
    }

    protected final void updateNamePattern() {
        strPattern = transformWildCardsToJavaStyle(getNameStringPattern());
        synchronized (patternLock) {
            if (strPattern.isEmpty()) {
                pattern = null;
            } else {
                try {
                    pattern = Pattern.compile(strPattern + ".*", Pattern.CASE_INSENSITIVE);
                } catch (PatternSyntaxException ex) {
                    pattern = emptyPattern;
                }
            }
        }
    }

    protected final boolean acceptName(TValue value) {
        synchronized (patternLock) {
            if (pattern == null) {
                return true;
            }
            return pattern.matcher(getName(value)).matches();
        }
    }

    protected String getNameStringPattern() {
        return field.getText().toLowerCase();
    }
    /*
     * Default implementation
     */

    public String getName(TValue value) {
        return value.toString().toLowerCase();
    }
    
        
    private static String transformWildCardsToJavaStyle(String text) {
        final StringBuilder regexBuilder = new StringBuilder(""); // NOI18N
        int lastWildCardPosition = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '?') { // NOI18N
                regexBuilder.append(text.substring(lastWildCardPosition, i));
                regexBuilder.append('.'); // NOI18N
                lastWildCardPosition = i + 1;
            } else if (text.charAt(i) == '*') { // NOI18N
                regexBuilder.append(text.substring(lastWildCardPosition, i));
                regexBuilder.append(".*"); // NOI18N
                lastWildCardPosition = i + 1;
            }
        }
        regexBuilder.append(text.substring(lastWildCardPosition, text.length()));
        return regexBuilder.toString();
    }
}
