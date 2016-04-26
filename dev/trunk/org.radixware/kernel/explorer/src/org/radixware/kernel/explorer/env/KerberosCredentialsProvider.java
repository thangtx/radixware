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

package org.radixware.kernel.explorer.env;

import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QDialog;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import org.ietf.jgss.GSSCredential;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.eas.IKerberosCredentialsProvider;
import org.radixware.kernel.common.client.errors.KerberosError;
import org.radixware.kernel.common.client.utils.ISecretStore;
import org.radixware.kernel.common.client.utils.TokenProcessor;
import org.radixware.kernel.common.kerberos.KerberosCredentials;
import org.radixware.kernel.common.kerberos.KerberosException;
import org.radixware.kernel.common.kerberos.KerberosLoginConfiguration;
import org.radixware.kernel.explorer.dialogs.EnterPasswordDialog;
import org.radixware.kernel.explorer.env.progress.TaskWaiter;
import org.radixware.kernel.explorer.utils.QtJambiExecutor;


final class KerberosCredentialsProvider implements IKerberosCredentialsProvider {
    
    private static class KerberosLoginCallbackHandler  implements CallbackHandler{

        private boolean firstTime=true;
        private final QtJambiExecutor executor = new QtJambiExecutor();
        private final AskForPasswordTask askPasswordTask;
        private final ISecretStore secretStore;

        private static class AskForPasswordTask implements Callable<char[]>{

            private final IClientEnvironment environment;

            public AskForPasswordTask(final IClientEnvironment environment){
                this.environment = environment;
            }

            @Override
            public char[] call() throws Exception {
                final String message = environment.getMessageProvider().translate("ExplorerMessage",
                            "You must reauthorize to continue working\nPlease enter your password or press cancel to disconnect");
                final EnterPasswordDialog pwdDialog = new EnterPasswordDialog(environment, null);
                    pwdDialog.setMessage(message);
                environment.getProgressHandleManager().blockProgress();
                try{
                    if (pwdDialog.exec()==QDialog.DialogCode.Accepted.value()){
                        return pwdDialog.getPassword().toCharArray();
                    }
                }finally{
                    environment.getProgressHandleManager().unblockProgress();
                }
                return null;
            }        
        }    

        public KerberosLoginCallbackHandler(final IClientEnvironment environment, final ISecretStore secretStore){
            this.secretStore = secretStore;
            askPasswordTask = new AskForPasswordTask(environment);
        }


        @Override
        public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
            for(Callback callback: callbacks){
                if (callback instanceof PasswordCallback){
                    if (firstTime){
                        firstTime = false;
                        final byte[] epwd = secretStore.getSecret();
                        if (epwd!=null){
                            final char[] pwd = new TokenProcessor().decrypt(epwd);
                            ((PasswordCallback)callback).setPassword(pwd);                        
                            Arrays.fill(pwd, ' ');
                            Arrays.fill(epwd, (byte)0);
                            return;
                        }                    
                    }
                    final char[] pwd = askForPassword(callback);                
                    ((PasswordCallback)callback).setPassword(pwd);
                    Arrays.fill(pwd, ' ');
                }else{
                    throw new UnsupportedCallbackException(callback);
                }
            }
        }        

        private char[] askForPassword(final Callback callback) throws UnsupportedCallbackException, IOException {
            final char[] pass;
            if (QApplication.instance().thread()==Thread.currentThread()){
                try{
                    pass = askPasswordTask.call();
                }catch(Exception exception){
                    throw new UnsupportedCallbackException(callback, exception.getMessage());//NOPMD
                }
            }else{
                try{
                    pass = executor.invoke(askPasswordTask);
                }catch(InterruptedException ex){
                    throw new UnsupportedCallbackException(callback);//NOPMD
                }catch(ExecutionException ex){
                    throw new UnsupportedCallbackException(callback,ex.getCause().getMessage());//NOPMD
                }            
            }
            if (pass==null){
                throw new IOException("Cancelled by user");
            }
            secretStore.setSecret(new TokenProcessor().encrypt(pass));
            return pass;
        }
        
        public void dispose(){
            executor.dispose();
        }
    }
    
    
    private final String userName;
    private final ISecretStore pwdStore;
    private final String spn;

    public KerberosCredentialsProvider(final String userName, final ISecretStore pwdStore, final String spn) {
        this.userName = userName;
        this.pwdStore = pwdStore;
        this.spn = spn;
    }

    @Override
    public KerberosCredentials createCredentials(IClientEnvironment environment) throws InterruptedException, KerberosError {
        final KerberosLoginCallbackHandler handler = new KerberosLoginCallbackHandler(environment, pwdStore);
        final Callable<KerberosCredentials> task = new Callable<KerberosCredentials>() {
            @Override
            public KerberosCredentials call() throws Exception {
                return createKerberosClientCredentials(userName, spn, handler);
            }
        };
        final TaskWaiter taskWaiter = new TaskWaiter(environment);
        try {
            final String waitMessage = environment.getMessageProvider().translate("Wait Dialog", "User Identification");
            taskWaiter.setMessage(waitMessage);            
            return taskWaiter.runAndWait(task);
        } catch (ExecutionException exception) {
            throw new KerberosError(exception);
        }finally{
            taskWaiter.close();
            handler.dispose();
        }        
    }

    private static KerberosCredentials createKerberosClientCredentials(final String userName, final String spn, final CallbackHandler pwdCallbackHandler) throws KerberosException {
        final KerberosLoginConfiguration loginConfig = createKerberosLoginConfiguration(userName, pwdCallbackHandler);
        return KerberosCredentials.Factory.newClientCredentials(userName, spn, loginConfig, GSSCredential.DEFAULT_LIFETIME);
    }

    private static KerberosLoginConfiguration createKerberosLoginConfiguration(final String userName, final CallbackHandler pwdCallbackHandler) {
        final Map<String, String> parameters = new HashMap<>();
        parameters.put("useTicketCache", "true");
        parameters.put("renewTGT", "true");
        parameters.put("principal", userName);
        return KerberosLoginConfiguration.Factory.newInstance("Explorer", parameters, pwdCallbackHandler);
    }
    
}
