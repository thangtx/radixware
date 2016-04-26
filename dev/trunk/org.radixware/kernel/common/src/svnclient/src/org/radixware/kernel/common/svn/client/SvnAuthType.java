/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.common.svn.client;

/**
 *
 * @author akrylov
 */
public enum SvnAuthType {

    NONE("None", "None"),
    SVN_PASSWORD("SVNPassword", "SVN Password"),
    SSH_PASSWORD("SSHPassword", "SSH Password"),
    SSH_KEY_FILE("SSHKeyFile", "SSH Key File"),
    SSL("SSL", "TLS");
    private final String name;
    private final String value;

    private SvnAuthType(String value, String name) {
        this.value = value;
        this.name = name;
    }

    
    public String getName() {
        return name;
    }

    
    public String getValue() {
        return value;
    }

    public static SvnAuthType getForValue(final String value) {
        for (SvnAuthType svnAuthType : SvnAuthType.values()) {
            if (svnAuthType.value.equals(value)) {
                return svnAuthType;
            }
        }
        throw new IllegalStateException("ESvnAuthType has no item with value: " + String.valueOf(value));
    }

}
