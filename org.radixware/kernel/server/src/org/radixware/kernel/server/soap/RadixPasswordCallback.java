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

package org.radixware.kernel.server.soap;

import java.io.IOException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import org.apache.ws.security.WSPasswordCallback;
import org.radixware.kernel.server.SrvRunParams;


public class RadixPasswordCallback implements CallbackHandler {

    public RadixPasswordCallback() {
    }

    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for (int i = 0; i < callbacks.length; i++) {
            final WSPasswordCallback pc = (WSPasswordCallback) callbacks[i];
            try {
                SrvRunParams.recieveKeystorePwd(new SrvRunParams.PasswordReciever() {
                    @Override
                    public void recievePassword(String password) {
                        pc.setPassword(password);
                    }
                });
            } catch (SrvRunParams.DecryptionException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
