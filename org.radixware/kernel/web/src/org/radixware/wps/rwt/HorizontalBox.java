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
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.html.Div;


public class HorizontalBox extends AbstractContainer {
    
    private final static int DEFAULT_SPACE = 3;
    
    private static class HorizontalDevider extends UIObject{
        public HorizontalDevider(final int width){
            super(new Html("div"));
            setMinimumWidth(width);
            getHtml().setCss("max-width", width+"px");
        }
    }        

    private static class Proxy extends UIObject {

        private UIObject child;

        public Proxy(UIObject child) {
            super(new Div());
            this.child = child;
            this.html.setCss("float", "left");
            this.html.setCss("position", "relative");
            this.html.add(child.html);
        }

        @Override
        public UIObject findObjectByHtmlId(String id) {
            UIObject result = super.findObjectByHtmlId(id);
            if (result != null) {
                return result;
            }
            return child.findObjectByHtmlId(id);
        }

        @Override
        public void visit(Visitor visitor) {
            super.visit(visitor);
            child.visit(visitor);
        }
    }

    public HorizontalBox() {
        super(new Div());
        html.layout("$RWT.horizontal_layout.layout");
        html.setCss("overflow", "hidden");
        setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
    }

    @Override
    protected String[] clientScriptsRequired() {
        return new String[]{"org/radixware/wps/rwt/rwt-layout.js"};
    }

    @Override
    public void add(int index, UIObject child) {
        super.add(index, new Proxy(child));
    }
    
    public UIObject addSpace(final int height){
        final UIObject space = new HorizontalDevider(height);
        add(space);
        return space;
    }
    
    public UIObject addSpace(){
        return addSpace(DEFAULT_SPACE);
    }
    

    @Override
    public List<UIObject> getChildren() {
        List<UIObject> ccs = super.getChildren();
        List<UIObject> pcs = new ArrayList<UIObject>(ccs.size());
        for (UIObject cc : ccs) {
            pcs.add(((Proxy) cc).child);
        }
        return pcs;
    }

    @Override
    public void remove(UIObject child) {
        if (child instanceof Proxy) {
            super.remove(child);
        } else {
            List<UIObject> ccs = super.getChildren();
            for (UIObject cc : ccs) {
                UIObject obj = ((Proxy) cc).child;
                if (obj == child) {
                    super.remove(cc);
                    return;
                }
            }
        }
    }
}
