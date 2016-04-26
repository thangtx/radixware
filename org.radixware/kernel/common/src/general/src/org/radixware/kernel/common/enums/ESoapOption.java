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


public enum ESoapOption implements IKernelStrEnum {

    WS_SECURITY_ENCRYPT_CERT_ALIAS("ws-security.encryption.username"),
    WS_SECURITY_SIGN_KEY_ALIAS("ws-security.signature.username"),
    LOG_DIRTY_DATA("log.dirty.data"),
    RDX_WS_TRUSTED_CERT_ALIASES("rdx.trusted.cert.aliases");
    private final String value;

    private ESoapOption(String val) {
        this.value = val;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean isInDomain(Id id) {
        return false;
    }

    @Override
    public boolean isInDomains(List<Id> ids) {
        return false;
    }

    public static ESoapOption getForValue(final String str) {
        for (ESoapOption type : ESoapOption.values()) {
            if (type.getValue().equals(str)) {
                return type;
            }
        }
        throw new NoConstItemWithSuchValueError("ESoapOption has no item with value " + str, str);
    }

    @Override
    public String getName() {
        return null;
    }
}
