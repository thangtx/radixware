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

package org.radixware.kernel.common.client.eas;

import java.util.Arrays;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.schemas.eas.GetSecurityTokenRs;


interface ITokenCalculator {
    
    static class SecurityToken{
        final byte[] token;
        private final byte[] key;
                
        SecurityToken(final byte[] data){
            this(data,null);
        }
        
        SecurityToken(final byte[] data, final byte[] krbEncKey){
            token = data;
            key = krbEncKey;
        }
        
        void write(final GetSecurityTokenRs xml){
            if (token!=null){
                xml.setOutToken(token);
            }
            if (key!=null){
                xml.setKrbEncKey(key);
            }
        }
        
        void clear(){
            if (token!=null){
                Arrays.fill(token, (byte)0);
            }
            if (key!=null){
                Arrays.fill(key, (byte)0);
            }
        }
    }

    SecurityToken calcToken(final byte[] inToken);

    byte[] createEncryptedHashForNewPassword(final String userName, final char[] newPassword);

    void dispose();

    ITokenCalculator copy(final IClientEnvironment environment);
}
