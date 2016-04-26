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

import org.radixware.kernel.common.html.Html;
import java.io.UnsupportedEncodingException;
import org.radixware.kernel.common.utils.Base64;
import org.radixware.kernel.common.utils.FileUtils;


public class Label extends UIObject {

    public static final String HTML_DISPLAY_COMPONENT_CLASS_NAME = "rwt-html-display-component";

    public Label() {
        this("");
    }

    public Label(String text) {
        this(text, false);
    }

    public Label(String text, boolean noHtml) {
        super(new Html("label"));

        //this.html.setInnerText(text);
        setPreferredHeight(17);
        setPreferredWidth(75);
        setVSizePolicy(SizePolicy.PREFERRED);
        if (!noHtml) {
            html.addClass(HTML_DISPLAY_COMPONENT_CLASS_NAME);
        }
        setText(text);
        setDefaultClassName("rwt-ui-element-text");
    }

    public void ignoreHtml(boolean ignore) {
        String text = getText();
        if (ignore) {
            html.removeClass(HTML_DISPLAY_COMPONENT_CLASS_NAME);
        } else {
            html.addClass(HTML_DISPLAY_COMPONENT_CLASS_NAME);
        }
        setText(text);
    }

    /*@Override
     protected String getDefaultClassName() {
     return "rwt-ui-element-text";
     }*/
    public String getText() {
        final String s = this.html.getInnerText();
        if (html.containsClass(HTML_DISPLAY_COMPONENT_CLASS_NAME)) {
            if (s == null || s.isEmpty()) {
                return s;
            } else {
                try {
                    return new String(Base64.decode(s), FileUtils.XML_ENCODING);
                } catch (UnsupportedEncodingException ex) {
                    return "";
                }
            }
        } else {
            return s;
        }
    }

    public final void setText(String text) {
        if (html.containsClass(HTML_DISPLAY_COMPONENT_CLASS_NAME)) {
            if (text == null || text.isEmpty()) {
                html.setInnerText(null);
            } else {
                try {
                    html.setInnerText(Base64.encode(text.getBytes(FileUtils.XML_ENCODING)));
                    //this.html.setInnerText(text);
                } catch (UnsupportedEncodingException ex) {
                }
            }
        } else {
            html.setInnerText(text);
        }
    }

    public boolean isTextWrapDisabled() {
        return "nowrap".equals(html.getCss("white-space"));
    }

    public void setTextWrapDisabled(boolean disable) {
        html.setCss("white-space", disable ? "nowrap" : null);
    }

    public void setAlign(Alignment a) {
        switch (a) {
            case CENTER:
                html.setCss("text-align", "center");
                break;
            case LEFT:
                html.setCss("text-align", null);
                break;
            case RIGHT:
                html.setCss("text-align", "right");
                break;
        }
    }

    public Alignment getAlign() {
        String val = html.getCss("text-align");
        switch (val) {
            case "center":
                return Alignment.CENTER;
            case "right":
                return Alignment.RIGHT;
            default:
                return Alignment.LEFT;
        }
    }

    public void setVAlign(Alignment a) {
        switch (a) {
            case BASELINE:
                html.setCss("vertical-align", "baseline");
                break;
            case BOTTOM:
                html.setCss("vertical-align", "bottom");
                break;
            case MIDDLE:
                html.setCss("vertical-align", "middle");
                break;
            case SUPER:
                html.setCss("vertical-align", "super");
                break;
            case SUB:
                html.setCss("vertical-align", "sub");
                break;
        }
    }

    public Alignment getVAlign() {
        String val = html.getCss("vertical-align");
        switch (val) {
            case "bsaeline":
                return Alignment.BASELINE;
            case "bottom":
                return Alignment.BOTTOM;
            case "middle":
                return Alignment.MIDDLE;
            case "super":
                return Alignment.SUPER;
            case "sub":
                return Alignment.SUB;
            default:
                return Alignment.BASELINE;
        }
    }

    @Override
    protected boolean fontSizePersistent() {
        return false;
    }
}
