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


public class HorizontalBoxContainer extends LinearBoxContainer {

    private final static int DEFAULT_SPACE = 3;
    
    private static class HorizontalDevider extends UIObject{
        public HorizontalDevider(final int width){
            super(new Html("div"));
            setMinimumWidth(width);
            getHtml().setCss("max-width", width+"px");
        }
    }   
    
    public HorizontalBoxContainer() {
        super("horizontalBox");
    }

    @Override
    public void setAlignment(Alignment a) {
        switch (a) {
            case LEFT:
                this.html.setAttr("halign", "left");
                break;
            case RIGHT:
                this.html.setAttr("halign", "right");
                break;
            case CENTER:
                this.html.setAttr("halign", "center");
                break;
        }
    }

    @Override
    public Alignment getAlignment() {
        String val = this.html.getAttr("align");
        if ("right".equals(val)) {
            return Alignment.RIGHT;
        } else if ("center".equals(val)) {
            return Alignment.CENTER;
        } else {
            return Alignment.LEFT;
        }
    }
    
    public UIObject addSpace(final int height){
        final UIObject space = new HorizontalDevider(height);
        add(space);
        return space;
    }
    
    public UIObject addSpace(){
        return addSpace(DEFAULT_SPACE);
    }
}
