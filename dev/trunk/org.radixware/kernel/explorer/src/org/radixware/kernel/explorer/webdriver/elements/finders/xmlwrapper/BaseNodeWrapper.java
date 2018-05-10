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

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.UserDataHandler;

abstract class BaseNodeWrapper implements org.w3c.dom.Node{

    @Override
    public void setNodeValue(final String nodeValue) throws DOMException {
        throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "This operation is not supported");
    }

    @Override
    public Node insertBefore(final Node newChild, final Node refChild) throws DOMException {
        throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "This operation is not supported");
    }

    @Override
    public Node replaceChild(final Node newChild, final Node oldChild) throws DOMException {
        throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "This operation is not supported");
    }

    @Override
    public Node removeChild(final Node oldChild) throws DOMException {
        throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "This operation is not supported");
    }

    @Override
    public Node appendChild(final Node newChild) throws DOMException {
        throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "This operation is not supported");
    }

    @Override
    public Node cloneNode(boolean deep) {
        throw new UnsupportedOperationException("This operation is not supported");
    }

    @Override
    public void normalize() {
    }

    @Override
    public boolean isSupported(final String feature, final String version) {
        return false;
    }

    @Override
    public String getNamespaceURI() {
        return null;
    }

    @Override
    public String getPrefix() {
        return null;
    }

    @Override
    public void setPrefix(final String prefix) throws DOMException {
        throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "This operation is not supported");
    }

    @Override
    public String getBaseURI() {
        return null;
    }

    @Override
    public short compareDocumentPosition(final Node other) throws DOMException {
        throw new UnsupportedOperationException("This operation is not supported");
    }

    @Override
    public String getTextContent() throws DOMException {
        return null;
    }

    @Override
    public void setTextContent(final String textContent) throws DOMException {
        throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "This operation is not supported");
    }

    @Override
    public boolean isSameNode(final Node other) {
        return false;
    }

    @Override
    public String lookupPrefix(final String namespaceURI) {
        return null;
    }

    @Override
    public boolean isDefaultNamespace(final String namespaceURI) {
        return false;
    }

    @Override
    public String lookupNamespaceURI(final String prefix) {
        return null;
    }

    @Override
    public boolean isEqualNode(final Node arg) {
        return false;
    }

    @Override
    public Object getFeature(final String feature, final String version) {
        return false;
    }

    @Override
    public Object setUserData(final String key, final Object data, final UserDataHandler handler) {
        throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "This operation is not supported");
    }

    @Override
    public Object getUserData(final String key) {
        return null;
    }        
}
