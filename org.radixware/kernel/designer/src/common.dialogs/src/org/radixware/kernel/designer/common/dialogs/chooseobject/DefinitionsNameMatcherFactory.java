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
package org.radixware.kernel.designer.common.dialogs.chooseobject;

import java.util.regex.PatternSyntaxException;
import org.radixware.kernel.common.utils.namefilter.ESearchType;
import org.radixware.kernel.common.utils.namefilter.NameMatcher;
import org.radixware.kernel.common.utils.namefilter.NameMatcherFactory;

/**
 *
 * @author dlastochkin
 */
public class DefinitionsNameMatcherFactory {

    protected DefinitionsNameMatcherFactory() {
    }

    private static abstract class BaseNameMatcher implements NameMatcher {

        protected String patternText;

        protected BaseNameMatcher(String patternText) {
            this.patternText = patternText;
        }
    }

    private static final class PrefixNameMatcher extends BaseNameMatcher implements NameMatcher {

        private final EChooseDefinitionDisplayMode displayMode;

        public PrefixNameMatcher(String patternText, EChooseDefinitionDisplayMode displayMode) {
            super(patternText);
            this.displayMode = displayMode;
        }

        @Override
        public final boolean accept(String name) {
            if (displayMode == EChooseDefinitionDisplayMode.NAME_AND_LOCATION) { // RADIX-3519
                return name != null && name.startsWith(patternText);
            } else {
                return name != null && name.contains(patternText);
            }
        }
    }

    private static final class CaseInsensitivePrefixNameMatcher extends BaseNameMatcher implements NameMatcher {

        private final EChooseDefinitionDisplayMode displayMode;

        public CaseInsensitivePrefixNameMatcher(String patternText, EChooseDefinitionDisplayMode displayMode) {
            super(patternText.toLowerCase());
            this.displayMode = displayMode;
        }

        @Override
        public final boolean accept(String name) {
            if (displayMode == EChooseDefinitionDisplayMode.NAME_AND_LOCATION) { // RADIX-3519
                return name != null && name.toLowerCase().startsWith(patternText); // commented by RADIX-3519
            } else {
                return name != null && name.toLowerCase().contains(patternText);
            }
        }
    }

    public static NameMatcher createNameMatcher(String textPattern, ESearchType type, EChooseDefinitionDisplayMode displayMode) {
        try {
            switch (type) {
                case EXACT_NAME:
                case CASE_INSENSITIVE_EXACT_NAME:
                case REGEXP:
                case CASE_INSENSITIVE_REGEXP:
                case CAMEL_CASE:
                    return NameMatcherFactory.createNameMatcher(textPattern, type);
                case PREFIX:
                    return new PrefixNameMatcher(textPattern, displayMode);
                case CASE_INSENSITIVE_PREFIX:
                    return new CaseInsensitivePrefixNameMatcher(textPattern, displayMode);
                default:
                    return null;
            }
        } catch (PatternSyntaxException ex) {
            return null;
        }
    }
}
