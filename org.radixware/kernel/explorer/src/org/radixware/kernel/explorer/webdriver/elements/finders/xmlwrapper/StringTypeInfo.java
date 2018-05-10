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

final class StringTypeInfo implements org.w3c.dom.TypeInfo{
    
    public final static StringTypeInfo INSTANCE = new StringTypeInfo();
    
    private StringTypeInfo(){        
    }

    @Override
    public String getTypeName() {
        return "string";
    }

    @Override
    public String getTypeNamespace() {
        return "http://www.w3.org/TR/REC-xml";
    }

    @Override
    public boolean isDerivedFrom(final String typeNamespaceArg, final String typeNameArg, final int derivationMethod) {
        return false;
    }
}
