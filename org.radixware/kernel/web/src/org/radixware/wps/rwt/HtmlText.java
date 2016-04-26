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

package org.radixware.wps.rwt;

import java.io.UnsupportedEncodingException;
import org.radixware.kernel.common.html.Div;
import org.radixware.kernel.common.utils.Base64;
import org.radixware.kernel.common.utils.FileUtils;



public class HtmlText extends UIObject {

    public HtmlText() {
        super(new Div());
        html.setCss("overflow", "auto");
        html.addClass("rwt-html-display-component");
    }

    public void setText(String htmlAsStr) {
        setHtmlText(htmlAsStr);
    }

    public String getText() {
        return getHtmlText();
    }

    public void setHtmlText(String htmlAsStr) {
        if (htmlAsStr != null && !htmlAsStr.isEmpty()) {
            try {
                html.setInnerText(Base64.encode(htmlAsStr.getBytes(FileUtils.XML_ENCODING)));
            } catch (UnsupportedEncodingException ex) {
            }
        }
    }

    public String getHtmlText() {
        String string = html.getInnerText();
        if (string == null || string.isEmpty()) {
            return "";
        }
        try {
            return new String(Base64.decode(string), FileUtils.XML_ENCODING);
        } catch (UnsupportedEncodingException ex) {
            return "";
        }
    }
}
