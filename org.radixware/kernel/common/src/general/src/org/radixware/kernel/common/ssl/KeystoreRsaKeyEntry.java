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

package org.radixware.kernel.common.ssl;

import java.security.Key;
import java.security.cert.Certificate;



public class KeystoreRsaKeyEntry extends KeystoreEntry{
    private final Certificate[] certificateChain;
    private final Key key;

    public KeystoreRsaKeyEntry(String alias, Certificate[] certificateChain){
        super(alias);
        this.certificateChain = certificateChain;
        this.key = null;
    }

    public KeystoreRsaKeyEntry(String alias, Certificate[] certificateChain, Key key){
        super(alias);
        this.certificateChain = certificateChain;
        this.key = key;
    }

    public Certificate[] getCertificateChain(){
        return certificateChain;
    }

    public Key getKey(){
        return key;
    }
}
