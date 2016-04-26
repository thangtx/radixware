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


public class VerticalBoxContainer extends LinearBoxContainer {
    
    private final static int DEFAULT_SPACE = 3;
    
    private static class VerticalDevider extends UIObject{
        public VerticalDevider(final int height){
            super(new Html("div"));
            setMinimumHeight(height);
            getHtml().setCss("max-height", height+"px");
        }
    }    

    public VerticalBoxContainer() {
        super("verticalBox");
    }

    @Override
    public void setAlignment(Alignment a) {
        switch (a) {
            case TOP:
                this.html.setAttr("valign", "top");
                break;
            case CENTER:
                this.html.setAttr("valign", "center");
                break;
            case BOTTOM:
                this.html.setAttr("valign", "bottom");
                break;
        }
    }

    @Override
    public Alignment getAlignment() {
        String val = this.html.getAttr("align");
        if ("bottom".equals(val)) {
            return Alignment.BOTTOM;
        } else if ("center".equals(val)) {
            return Alignment.CENTER;
        } else {
            return Alignment.TOP;
        }
    }
    
    public UIObject addSpace(final int height){
        final UIObject space = new VerticalDevider(height);
        add(space);
        return space;
    }
    
    public UIObject addSpace(){
        return addSpace(DEFAULT_SPACE);
    }    
}
