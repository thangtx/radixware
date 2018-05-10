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

final class NodeListWrapper implements org.w3c.dom.NodeList{
    
    private final UIElementWrapper owner;
    private List<UIElementWrapper> childNodes;
    
    public NodeListWrapper(final UIElementWrapper owner){
        this.owner = owner;
    }
    
    private List<UIElementWrapper> getChildNodes(){
        if (childNodes==null){
            childNodes = owner.getChildElements();
        }
        return childNodes;
    }

    @Override
    public UIElementWrapper item(final int index) {
        return getChildNodes().get(index);
    }

    @Override
    public int getLength() {
        return getChildNodes().size();
    }

    public int getIndexOf(final UIElementWrapper node){
        return getChildNodes().indexOf(node);
    }
    
    public NodeListWrapper getNodesByName(final String name){
        final List<UIElementWrapper> filteredChilds = new ArrayList<>(getChildNodes());
        if (!"*".equals(name)){
            for (int i=filteredChilds.size(); i>=0; i--){
                if (!filteredChilds.get(i).getNodeName().equals(name)){
                    filteredChilds.remove(i);
                }
            }
        }
        final NodeListWrapper result = new NodeListWrapper(owner);
        result.childNodes = filteredChilds;
        return result;
    }
    
    public void writeTo(final List<UIElementWrapper> list){
        list.addAll(getChildNodes());
    }
}
