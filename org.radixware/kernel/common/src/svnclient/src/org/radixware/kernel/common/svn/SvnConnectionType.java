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

package org.radixware.kernel.common.svn;


public enum SvnConnectionType {
    
    SVN("svn"),
    HTTP("http");//and https
    
    private final String value;
    private SvnConnectionType(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
        //"SvnConnectionType{" + "value=" + value + '}';
    }
    
    
    
}
