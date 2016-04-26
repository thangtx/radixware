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

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FindUtils {

    public static String html(String s) {
        return "<html>" + s + "</html>";
    }

    public static String markPatternBold(String str, final Pattern pattern) {
        final StringBuilder sb = new StringBuilder();
        final Matcher matcher = pattern.matcher(str);
        int start = 0;
        while (matcher.find()) {
            sb.append(str.substring(start, matcher.start()));
            sb.append("<b>");
            sb.append(str.substring(matcher.start(), matcher.end()));
            sb.append("</b>");
            start = matcher.end();
        }
        sb.append(str.substring(start));
        return sb.toString();
    }

    public static ContainingLineInfo getContainingLine(final String text, final int offset) {
        int startOffs = offset;
        while (startOffs >= 0 && text.charAt(startOffs) != '\n') {
            startOffs--;
        }
        if (startOffs < 0) {
            startOffs = 0;
        }

        int endOffset = offset;
        while (endOffset < text.length() && text.charAt(endOffset) != '\n') {
            endOffset++;
        }
        if (endOffset == text.length() - 1) {
            endOffset++;
        }

        return new ContainingLineInfo(startOffs, text.substring(startOffs, endOffset));
    }

    public static class ContainingLineInfo {

        private final int lineStartOffset;
        private final String line;

        public ContainingLineInfo(final int lineStartOffset, final String line) {
            this.lineStartOffset = lineStartOffset;
            this.line = line;
        }

        public String getLine() {
            return line;
        }

        public int getLineStartOffset() {
            return lineStartOffset;
        }
    }
}
