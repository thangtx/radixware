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

import java.util.List;


public class StatusBar extends Container {
    
    private final Label label = new Label();
    private final HorizontalBox container = new HorizontalBox();
    private final AbstractContainer labelContainer = new AbstractContainer();
    
    public StatusBar(){
        super();
        container.addSpace();
        container.setSizePolicy(UIObject.SizePolicy.EXPAND, UIObject.SizePolicy.EXPAND);
        {            
            label.setTextWrapDisabled(true);
            label.setHSizePolicy(UIObject.SizePolicy.EXPAND);
            label.getHtml().setCss("font-weight", "bold");
            labelContainer.getHtml().setCss("overflow", "hidden");
            labelContainer.add(label);
            labelContainer.setHSizePolicy(UIObject.SizePolicy.EXPAND);
            container.add(labelContainer);
            container.addSpace();
        }
        super.add(-1, container);        
    }
    
    protected final Label getLabel(){
        return label;
    }
    
    protected final AbstractContainer getLabelContainer(){
        return labelContainer;
    }
    
    public void addSpace(){
        container.addSpace();
    }

    @Override
    public void setHeight(int h) {
        super.setHeight(h);
        label.getHtml().setCss("line-height", getHeight() + "px");
    }

    @Override
    public void clear() {
        container.clear();
    }        
       
    public List<UIObject> getItems() {
        return container.getChildren();
    }

    @Override
    public void add(final int index, final UIObject child) {
        container.add(index, child);
    }

    @Override
    public void add(final UIObject child) {
        container.add(child);
    }
    
    public void setText(final String text){        
        label.setText(text);        
    }
    
    public String getText(){
        return label.getText();
    }
    
}
