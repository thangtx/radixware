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
 * Contains only strong suites. Non-strong suites can be added in ADS.
 *
 */
public enum ESslCipherSuite implements IKernelStrEnum {
    
    
    TLS_ECDH_RSA_WITH_AES_128_CBC_SHA("TLS_ECDH_RSA_WITH_AES_128_CBC_SHA"),
    TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA("TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA"),
    TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA("TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA"),
    TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256("TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256"),
    TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA("TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA"),
    TLS_ECDH_RSA_WITH_3DES_EDE_CBC_SHA("TLS_ECDH_RSA_WITH_3DES_EDE_CBC_SHA"),
    TLS_DHE_RSA_WITH_AES_128_CBC_SHA("TLS_DHE_RSA_WITH_AES_128_CBC_SHA"),
    SSL_RSA_WITH_3DES_EDE_CBC_SHA("SSL_RSA_WITH_3DES_EDE_CBC_SHA"),
    TLS_RSA_WITH_AES_128_CBC_SHA("TLS_RSA_WITH_AES_128_CBC_SHA"),
    TLS_DHE_DSS_WITH_AES_128_CBC_SHA("TLS_DHE_DSS_WITH_AES_128_CBC_SHA"),
    SSL_DHE_DSS_WITH_3DES_EDE_CBC_SHA("SSL_DHE_DSS_WITH_3DES_EDE_CBC_SHA"),
    SSL_DHE_RSA_WITH_3DES_EDE_CBC_SHA("SSL_DHE_RSA_WITH_3DES_EDE_CBC_SHA");
    
    private final String value;

    private ESslCipherSuite(final String value) {
        this.value = value;
    }

    public static ESslCipherSuite getForValue(final String value) {
        for (ESslCipherSuite status : ESslCipherSuite.values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        throw new NoConstItemWithSuchValueError("ESslCipherSuite has no item with value: " + String.valueOf(value), value);
    }

    @Override
    public String getName() {
        return value;
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
}
