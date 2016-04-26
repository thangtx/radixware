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

import org.radixware.kernel.common.html.Div;
import org.radixware.kernel.common.html.Html;
import org.radixware.kernel.common.html.ICssStyledItem;
import org.radixware.kernel.common.utils.Utils;



public class TextArea extends AbstractTextField {

    private Html input = new Html("textarea");

    public TextArea() {
        super(new Div());
        html.add(input);
        input.setCss("resize", "none");
        input.setCss("width", "100%");
        input.setCss("height", "100%");
        input.setCss("overflow", "auto");
        input.setCss("padding", "0px");
        input.setCss("margin", "0px");
        input.setCss("border", "none");
        html.addClass("rwt-text-area");
        input.addClass("rwt-ui-element");
        input.addClass("rwt-ui-element-text");
        input.setAttr("rwt_f_finishEdit", "$RWT.textArea.finishEdit");
        input.setAttr("onfocus", "$RWT.textArea.focusIn");
        input.setAttr("onblur", "$RWT.textArea.focusOut");
        input.setAttr("ondrop", "$RWT.textArea.drop");
        input.setAttr("ondragover", "$RWT.textArea.dragOver");
        input.setAttr("ondragenter", "$RWT.textArea.dragEnter");
        input.setAttr("ondragleave", "$RWT.textArea.dragLeave");
        html.layout("$RWT.textArea.layout");
    }

    @Override
    protected String getTextFromHtml() {
        //RADIX-7722
      /*  if (getEnvironment()!=null && 
            ((WpsEnvironment)getEnvironment()).getBrowserEngineType()==EWebBrowserEngineType.MSIE){
            return input.getAttr("value");
        }else{
            return input.getInnerText();
        }*/
        return input.getInnerText();
    }

    @Override
    protected Html getForegroundHolder() {
        return input;
    }

    @Override
    protected ICssStyledItem getFontOptionsHolder() {
        return input;
    }

    @Override
    protected ICssStyledItem getBackgroundHolder() {
        return input;
    }
    
    @Override
    protected void updateTextInHtml(final String text) {
        input.setAttr("value",text.replaceAll("(\r\n|\n)", "<br />"));
        input.setInnerText(text);
        
    }

    @Override
    public UIObject findObjectByHtmlId(String id) {
        UIObject obj = super.findObjectByHtmlId(id);
        if (obj != null) {
            return obj;
        } else if (input.getId().equals(id)) {
            return this;
        }
        return null;
    }
    
    @Override
    public boolean isReadOnly() {
        return Utils.equals(input.getAttr("readonly"), "true");
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        if (readOnly) {
            input.setAttr("readonly", "true");
        } else {
            input.setAttr("readonly", null);
        }
    }

    @Override
    public void setMinimumHeight(int h) {
        super.setMinimumHeight(h);
        input.setCss("min-height", String.valueOf(h) + "px");
    }
    
    public void setLineWrap(final boolean wrapOn){
        if (wrapOn){
            input.setCss("white-space", null);
            input.setAttr("wrap", null);
        }
        else{
            input.setCss("white-space", "pre");
            input.setAttr("wrap", "off");
        }
    }    
}