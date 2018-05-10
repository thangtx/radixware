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

import javax.servlet.http.HttpServletResponse;
import org.radixware.kernel.common.utils.Base64;


final class KrbAuthHttpHeaderWriter implements HttpSessionContext.IHttpServletResponseHeaderWriter{    
    
    private final String realm;
    private final byte[] gssToken;

    private KrbAuthHttpHeaderWriter(final String realm, final byte[] gssToken) {
        this.realm = realm;
        this.gssToken = gssToken;
    }
    
    public KrbAuthHttpHeaderWriter(final String realm){
        this(realm,null);
    }
    
    public KrbAuthHttpHeaderWriter(final byte[] token){
        this(null,token);
    }

    @Override
    public void writeResponseHeader(HttpServletResponse rs) {
        if (realm!=null && !realm.isEmpty()){
            rs.addHeader(HttpHeaderConstants.AUTHN_HEADER, HttpHeaderConstants.BASIC_HEADER+" realm=\""+realm+"\"");
        }else if (gssToken == null || gssToken.length == 0){                    
            rs.setHeader(HttpHeaderConstants.AUTHN_HEADER, HttpHeaderConstants.NEGOTIATE_HEADER);
        }else{            
            rs.setHeader(HttpHeaderConstants.AUTHN_HEADER, HttpHeaderConstants.NEGOTIATE_HEADER+" "+ Base64.encode(gssToken));
        }        
        rs.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Override
    public boolean isResponseContentAllowed() {
        return true;
    }

    @Override
    public boolean isAuthDataRequested() {
        return true;
    }        
    
}
