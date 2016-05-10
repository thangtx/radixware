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
package org.radixware.kernel.common.svn.client.impl;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javax.net.ssl.X509TrustManager;

/**
 *
 * @author akrylov
 */
public class TrustManager implements X509TrustManager {

    private final X509TrustManager delegate;
    private List<X509Certificate> acceptedIssuers = new LinkedList<>();

    public TrustManager(X509TrustManager delegate) {
        this.delegate = delegate;
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        if (delegate != null) {
            delegate.checkServerTrusted(chain, authType);
        } else {
            if (chain != null && chain.length > 0) {
                acceptedIssuers.addAll(Arrays.asList(chain));
            }
        }
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return acceptedIssuers.toArray(new X509Certificate[acceptedIssuers.size()]);
    }

}
