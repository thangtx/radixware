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

package org.radixware.kernel.designer.common.general.displaying;

import java.awt.Color;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.utils.XmlColor;


public class HtmlNameProcessor {

    protected HtmlNameProcessor() {
    }

    protected String calcHtmlDisplayName(IHtmlStyled htmlStyled) {
        String result = htmlStyled.getDisplayName();
        StringBuilder builder = new StringBuilder(result.length() * 2);
        for (int i = 0, length = result.length(); i < length; i++) {
            char c = result.charAt(i);
            if (c == '<') {
                builder.append("&lt;");
            } else if (c == '>') {
                builder.append("&gt;");
            } else {
                builder.append(c);
            }
        }
        if (htmlStyled.isItalic()) {
            builder.insert(0, "<i>");
            builder.append("</i>");
        }
        if (htmlStyled.isBold()) {
            builder.insert(0, "<b>");
            builder.append("</b>");
        }
        if (htmlStyled.isStrikeOut()) {
            builder.insert(0, "<S>");
            builder.append("</S>");
        }
        Color color = htmlStyled.getColor();
        if (!Utils.equals(color, Color.BLACK)) {
            String htmlColor = XmlColor.mergeColor(color);
            builder.insert(0, "<font color=\"" + htmlColor + "\">");
            builder.append("</font>");
        }
        return builder.toString();
    }

    public static HtmlNameProcessor getDefault() {
        return new HtmlNameProcessor();
    }
}
