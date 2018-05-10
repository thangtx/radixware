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

import org.w3c.dom.Node;

final class SingletonNodeListWrapper implements org.w3c.dom.NodeList{
    
    private final Node node;    
    
    public SingletonNodeListWrapper(final Node node){
        this.node = node;
    }

    @Override
    public Node item(final int index) {
        if (index==0){
            return node;
        }
        throw new IndexOutOfBoundsException("size=1, index="+String.valueOf(index));
    }

    @Override
    public int getLength() {
        return 1;
    }
}
