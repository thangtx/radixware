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

final class EmptyNodeList implements org.w3c.dom.NodeList{
    
    public static final EmptyNodeList INSTANCE = new EmptyNodeList();
    
    private EmptyNodeList(){        
    }

    @Override
    public Node item(final int index) {
        throw new IndexOutOfBoundsException("size=0, index="+String.valueOf(index));
    }

    @Override
    public int getLength() {
        return 0;
    }

}
