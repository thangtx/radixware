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
}
