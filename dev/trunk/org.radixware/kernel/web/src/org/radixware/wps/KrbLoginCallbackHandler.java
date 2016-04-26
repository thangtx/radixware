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

import java.io.IOException;
import java.util.Arrays;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import org.radixware.wps.dialogs.EnterPasswordDialog;
import org.radixware.wps.rwt.Dialog;


public class KrbLoginCallbackHandler  implements CallbackHandler{

    private boolean firstTime=true;
    private char[] password;
    private final WpsEnvironment environment;

    public KrbLoginCallbackHandler(final WpsEnvironment environment, final char[] pwd){
        this.environment = environment;
        password = new char[pwd.length];
        System.arraycopy(pwd, 0, password, 0, pwd.length);
    }

    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for(Callback callback: callbacks){
            if (callback instanceof PasswordCallback){
                if (firstTime){
                    firstTime = false;
                    ((PasswordCallback)callback).setPassword(password);
                    Arrays.fill(password, ' ');
                    return;
                }
                final char[] pwd = askForPassword();
                ((PasswordCallback)callback).setPassword(pwd);
                Arrays.fill(pwd, ' ');
            }else{
                throw new UnsupportedCallbackException(callback);
            }
        }

    }

    private char[] askForPassword() {
        final String message = environment.getMessageProvider().translate("ExplorerMessage",
                    "You must reauthorize to continue working\nPlease enter your password or press cancel to disconnect");
        final EnterPasswordDialog pwdDialog = new EnterPasswordDialog(environment);
            pwdDialog.setMessage(message);
        environment.getProgressHandleManager().blockProgress();
        try{
            if (pwdDialog.execDialog()==Dialog.DialogResult.ACCEPTED){
                return pwdDialog.getPassword().toCharArray();
            }
        }finally{
            environment.getProgressHandleManager().unblockProgress();
        }
        return null;
    }

    
}
