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

import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.explorer.webdriver.elements.UIElementPropertySet;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

final class AttributesWrapper implements org.w3c.dom.NamedNodeMap {
    
    private final UIElementWrapper owner;
    private final Map<String,AttrWrapper> attrsByName = new HashMap<>(64);
    
    public AttributesWrapper(final UIElementWrapper owner){
        this.owner = owner;
    }
    
    public UIElementWrapper getOwner(){
        return owner;
    }
    
    public UIElementPropertySet getPropertySet(){
        return owner.getPropertySet();
    }

    @Override
    public Node getNamedItem(final String name) {
        if (getPropertySet().containsProperty(name)){
            AttrWrapper attr = attrsByName.get(name);
            if (attr==null){
                attr = new AttrWrapper(name, this);
                attrsByName.put(name, attr);
            }
            return attr;
        }else{
            return null;
        }
    }

    @Override
    public Node setNamedItem(final Node arg) throws DOMException {
        throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "This operation is not supported");
    }

    @Override
    public Node removeNamedItem(final String name) throws DOMException {
        throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "This operation is not supported");
    }

    @Override
    public Node item(final int index) {
        final String name = getPropertySet().getOrderedNames().get(index);
        return getNamedItem(name);
    }

    @Override
    public int getLength() {
        return getPropertySet().size();
    }

    @Override
    public Node getNamedItemNS(final String namespaceURI, final String localName) throws DOMException {
        return null;
    }

    @Override
    public Node setNamedItemNS(final Node arg) throws DOMException {
        throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "This operation is not supported");
    }

    @Override
    public Node removeNamedItemNS(final String namespaceURI, final String localName) throws DOMException {
        throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "This operation is not supported");
    }

}
