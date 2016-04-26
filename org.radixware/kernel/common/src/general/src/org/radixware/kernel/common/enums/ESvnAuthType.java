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
package org.radixware.kernel.common.enums;

import java.util.List;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.types.IKernelStrEnum;
import org.radixware.kernel.common.types.Id;

/**
 * SVN authrization type
 */
public enum ESvnAuthType implements IKernelStrEnum {

    NONE("None", "None"),
    SVN_PASSWORD("SVNPassword", "SVN Password"),
    SSH_PASSWORD("SSHPassword", "SSH Password"),
    SSH_KEY_FILE("SSHKeyFile", "SSH Key File"),
    SSL("SSL", "TLS");
    //SSH_KERBEROS("SSHKerberos", "SSH Kerberos");
    //
    private final String name;
    private final String value;    

    private ESvnAuthType(String value, String name) {
        this.value = value;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return value;
    }

    public static ESvnAuthType getForValue(final String value) {
        for (ESvnAuthType svnAuthType : ESvnAuthType.values()) {
            if (svnAuthType.value.equals(value)) {
                return svnAuthType;
            }
        }
        throw new NoConstItemWithSuchValueError("ESvnAuthType has no item with value: " + String.valueOf(value), value);
    }

    @Override
    public boolean isInDomain(Id id) {
        return false;
    }

    @Override
    public boolean isInDomains(List<Id> ids) {
        return false;
    }
};
