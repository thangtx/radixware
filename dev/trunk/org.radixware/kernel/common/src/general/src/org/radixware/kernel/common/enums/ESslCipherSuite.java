/*
 * Copyright (c) 2008-2017, Compass Plus Limited. All rights reserved.
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
    
    // TLS v1.2 cipher suites
    TLS_RSA_WITH_AES_128_CBC_SHA256("TLS_RSA_WITH_AES_128_CBC_SHA256", 12),
    TLS_RSA_WITH_AES_256_CBC_SHA256("TLS_RSA_WITH_AES_256_CBC_SHA256", 12),
    TLS_RSA_WITH_AES_128_GCM_SHA256("TLS_RSA_WITH_AES_128_GCM_SHA256", 12),
    TLS_RSA_WITH_AES_256_GCM_SHA384("TLS_RSA_WITH_AES_256_GCM_SHA384", 12),
    TLS_DH_RSA_WITH_AES_128_CBC_SHA256("TLS_DH_RSA_WITH_AES_128_CBC_SHA256", 12),
    TLS_DH_RSA_WITH_AES_256_CBC_SHA256("TLS_DH_RSA_WITH_AES_256_CBC_SHA256", 12),
    TLS_DH_RSA_WITH_AES_128_GCM_SHA256("TLS_DH_RSA_WITH_AES_128_GCM_SHA256", 12),
    TLS_DH_RSA_WITH_AES_256_GCM_SHA384("TLS_DH_RSA_WITH_AES_256_GCM_SHA384", 12),
    TLS_DH_DSS_WITH_AES_128_CBC_SHA256("TLS_DH_DSS_WITH_AES_128_CBC_SHA256", 12),
    TLS_DH_DSS_WITH_AES_256_CBC_SHA256("TLS_DH_DSS_WITH_AES_256_CBC_SHA256", 12),
    TLS_DH_DSS_WITH_AES_128_GCM_SHA256("TLS_DH_DSS_WITH_AES_128_GCM_SHA256", 12),
    TLS_DH_DSS_WITH_AES_256_GCM_SHA384("TLS_DH_DSS_WITH_AES_256_GCM_SHA384", 12),
    TLS_DHE_RSA_WITH_AES_128_CBC_SHA256("TLS_DHE_RSA_WITH_AES_128_CBC_SHA256", 12),
    TLS_DHE_RSA_WITH_AES_256_CBC_SHA256("TLS_DHE_RSA_WITH_AES_256_CBC_SHA256", 12),
    TLS_DHE_RSA_WITH_AES_128_GCM_SHA256("TLS_DHE_RSA_WITH_AES_128_GCM_SHA256", 12),
    TLS_DHE_RSA_WITH_AES_256_GCM_SHA384("TLS_DHE_RSA_WITH_AES_256_GCM_SHA384", 12),
    TLS_DHE_DSS_WITH_AES_128_CBC_SHA256("TLS_DHE_DSS_WITH_AES_128_CBC_SHA256", 12),
    TLS_DHE_DSS_WITH_AES_256_CBC_SHA256("TLS_DHE_DSS_WITH_AES_256_CBC_SHA256", 12),
    TLS_DHE_DSS_WITH_AES_128_GCM_SHA256("TLS_DHE_DSS_WITH_AES_128_GCM_SHA256", 12),
    TLS_DHE_DSS_WITH_AES_256_GCM_SHA384("TLS_DHE_DSS_WITH_AES_256_GCM_SHA384", 12),
    TLS_ECDH_RSA_WITH_AES_128_CBC_SHA256("TLS_ECDH_RSA_WITH_AES_128_CBC_SHA256", 12),
    TLS_ECDH_RSA_WITH_AES_256_CBC_SHA384("TLS_ECDH_RSA_WITH_AES_256_CBC_SHA384", 12),
    TLS_ECDH_RSA_WITH_AES_128_GCM_SHA256("TLS_ECDH_RSA_WITH_AES_128_GCM_SHA256", 12),
    TLS_ECDH_RSA_WITH_AES_256_GCM_SHA384("TLS_ECDH_RSA_WITH_AES_256_GCM_SHA384", 12),
    TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA256("TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA256", 12),
    TLS_ECDH_ECDSA_WITH_AES_256_CBC_SHA384("TLS_ECDH_ECDSA_WITH_AES_256_CBC_SHA384", 12),
    TLS_ECDH_ECDSA_WITH_AES_128_GCM_SHA256("TLS_ECDH_ECDSA_WITH_AES_128_GCM_SHA256", 12),
    TLS_ECDH_ECDSA_WITH_AES_256_GCM_SHA384("TLS_ECDH_ECDSA_WITH_AES_256_GCM_SHA384", 12),
    TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256("TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256", 12),
    TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384("TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384", 12),
    TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256("TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256", 12),
    TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384("TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384", 12),
    TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256("TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256", 12),
    TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA384("TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA384", 12),
    TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256("TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256", 12),
    TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384("TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384", 12),

    // TLS v1.0 cipher suites
    TLS_ECDH_RSA_WITH_AES_128_CBC_SHA("TLS_ECDH_RSA_WITH_AES_128_CBC_SHA", 10),
    TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA("TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA", 10),
    TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA("TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA", 10),
    TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA("TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA", 10),
    TLS_ECDH_RSA_WITH_3DES_EDE_CBC_SHA("TLS_ECDH_RSA_WITH_3DES_EDE_CBC_SHA", 10),
    TLS_DHE_RSA_WITH_AES_128_CBC_SHA("TLS_DHE_RSA_WITH_AES_128_CBC_SHA", 10),
    TLS_RSA_WITH_AES_128_CBC_SHA("TLS_RSA_WITH_AES_128_CBC_SHA", 10),
    TLS_DHE_DSS_WITH_AES_128_CBC_SHA("TLS_DHE_DSS_WITH_AES_128_CBC_SHA", 10),

    // SSL v3.0 cipher suites
    SSL_RSA_WITH_3DES_EDE_CBC_SHA("SSL_RSA_WITH_3DES_EDE_CBC_SHA", 3),
    SSL_DHE_DSS_WITH_3DES_EDE_CBC_SHA("SSL_DHE_DSS_WITH_3DES_EDE_CBC_SHA", 3),
    SSL_DHE_RSA_WITH_3DES_EDE_CBC_SHA("SSL_DHE_RSA_WITH_3DES_EDE_CBC_SHA", 3);
    
    private final String value;
    public final int version; // SSL v3 -> 0.3 -> 3; TLS vX.Y = X.X = XY

    private ESslCipherSuite(final String value, final int version) {
        this.value = value;
        this.version = version;
    }

    public static ESslCipherSuite getForValue(final String value) {
        for (ESslCipherSuite suite : ESslCipherSuite.values()) {
            if (suite.getValue().equals(value)) {
                return suite;
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
