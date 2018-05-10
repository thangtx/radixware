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

package org.radixware.wps;

import java.util.Objects;
import org.radixware.kernel.common.utils.Base64;


final class ClientAuthData {
    
    private static final String NTLM_PROLOG = "TlRMTVNT";
    
    private final String token;
    private final boolean isBasicAuth;
    private final boolean isNtlmAuth;
    
    public ClientAuthData(final String authHeader){
        if (authHeader.startsWith(HttpHeaderConstants.NEGOTIATE_HEADER)) {
            token = authHeader.substring(HttpHeaderConstants.NEGOTIATE_HEADER.length() + 1);
            isBasicAuth = false;
            isNtlmAuth = token.startsWith(NTLM_PROLOG);
        } else if (authHeader.startsWith(HttpHeaderConstants.BASIC_HEADER)) {
            token = authHeader.substring(HttpHeaderConstants.BASIC_HEADER.length() + 1);
            isBasicAuth = true;
            isNtlmAuth = true;
        }else{
            throw new UnsupportedOperationException("Unsupported authentication header:" + authHeader);
        }
    }
    
    public boolean isBasicAuth(){
        return isBasicAuth;
    }
    
    public boolean isNtlmAuth(){
        return isNtlmAuth;
    }    
    
    public byte[] getToken(){
        return token==null || token.isEmpty() ? null : Base64.decode(this.token);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.token);
        hash = 23 * hash + (this.isBasicAuth ? 1 : 0);
        hash = 23 * hash + (this.isNtlmAuth ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ClientAuthData other = (ClientAuthData) obj;
        if (!Objects.equals(this.token, other.token)) {
            return false;
        }
        if (this.isBasicAuth != other.isBasicAuth) {
            return false;
        }
        if (this.isNtlmAuth != other.isNtlmAuth) {
            return false;
        }
        return true;
    }
    
    
}
