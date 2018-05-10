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

package org.radixware.kernel.explorer.webdriver.elements.finders.xmlwrapper;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Node;

final class NodeListImpl implements org.w3c.dom.NodeList{

    private final List<UIElementWrapper> elements = new ArrayList<>();
    
    public NodeListImpl(){        
    }
    
    public void insert(final UIElementWrapper element){
        elements.add(0, element);
    }
    
    public NodeListImpl(List<UIElementWrapper> list){
        if (list!=null && !list.isEmpty()){
            elements.addAll(list);
        }
    }
    
    public void expand(NodeListWrapper other){
        other.writeTo(elements);
    }

    @Override
    public Node item(final int index) {
        return elements.get(index);
    }

    @Override
    public int getLength() {
        return elements.size();
    }
}
