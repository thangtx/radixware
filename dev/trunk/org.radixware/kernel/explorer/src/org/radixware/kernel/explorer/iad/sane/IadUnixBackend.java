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

package org.radixware.kernel.explorer.iad.sane;

import com.trolltech.qt.core.QObject;
import com.trolltech.qt.gui.QDialog;
import com.tuneology.sane.Sane;
import com.tuneology.sane.SaneAuthorization;
import com.tuneology.sane.SaneCredentials;
import com.tuneology.sane.SaneException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.explorer.dialogs.EnterPasswordDialog;
import org.radixware.kernel.explorer.iad.AbstractIadBackend;
import org.radixware.kernel.explorer.iad.EImageAcquiringDeviceType;
import org.radixware.kernel.explorer.iad.IadException;
import org.radixware.kernel.explorer.iad.ImageAcquiringDevice;
import org.radixware.kernel.explorer.utils.QtJambiExecutor;


public class IadUnixBackend extends AbstractIadBackend{
    
    private final static IadUnixBackend INSTANCE = new IadUnixBackend();
        
    private static class Authorizer implements SaneAuthorization{
        
        private static class AskForPasswordTask implements Callable<SaneCredentials>{

            private final IClientEnvironment environment;
            private final String resourceName;

            public AskForPasswordTask(final IClientEnvironment environment, final String resource){
                this.environment = environment;
                resourceName = resource;
            }

            @Override
            public SaneCredentials call() throws Exception {
                final String message = environment.getMessageProvider().translate("IAD",
                            "Authentication required for resource: %1$s");
                final EnterPasswordDialog pwdDialog = new EnterPasswordDialog(environment, null);
                pwdDialog.setMessage(String.format(message,resourceName));
                pwdDialog.setWindowTitle(environment.getMessageProvider().translate("IAD","Authentication"));
                pwdDialog.setUserNameRequired(true);
                environment.getProgressHandleManager().blockProgress();
                try{
                    if (pwdDialog.exec()==QDialog.DialogCode.Accepted.value()){
                        final SaneCredentials credentials = new SaneCredentials();
                        credentials.password = pwdDialog.getPassword();
                        credentials.username = pwdDialog.getUserName();
                        return credentials;
                    }
                }finally{
                    environment.getProgressHandleManager().unblockProgress();
                }
                return null;
            }        
        }        
        
        private final IClientEnvironment environment;
        private final QtJambiExecutor executor;
        
        public Authorizer(final IClientEnvironment environment){
            this.environment = environment;
            executor = new QtJambiExecutor((QObject)environment.getApplication());
        }
        
        @Override
        public SaneCredentials getCredentials(final String resourceName) {
            try{
                return executor.invoke(new AskForPasswordTask(environment, resourceName));
            }catch(InterruptedException | ExecutionException ex){
                return null;
            }    
        }
        
    }        
    
    private IadUnixBackend(){        
    }    
        
    @Override
    protected Collection<ImageAcquiringDevice> enumDevicesImpl(final IClientEnvironment environment,
                                                        final EImageAcquiringDeviceType type){
        final com.tuneology.sane.SaneDevice[] devices;
        try{
            devices = Sane.getDevices(false);
        }catch(SaneException exception){
            environment.processException(exception);
            return Collections.emptySet();
        }
        final List<ImageAcquiringDevice> saneDevices = new LinkedList<>();
        for (com.tuneology.sane.SaneDevice device: devices){
            saneDevices.add(new SaneDevice(device, environment));
        }
        return saneDevices;
    }
    
    private void initSaneLib(final IClientEnvironment environment) throws SaneException{
        Sane.init();
        final int saneVersion = Sane.getVersionCode();
        final int majorVersion = (saneVersion>>24) & 0xff;
        final int minorVersion = (saneVersion>>16) & 0xff;
        final int buildVersion = (saneVersion>>0) & 0xffff;
        final String messageTemplate = 
            environment.getMessageProvider().translate("IAD","SANE library inited. Library version is %1$s (%2$s.%3$s.%4$s)");
        final String message = 
            String.format(messageTemplate, String.valueOf(saneVersion), String.valueOf(majorVersion), String.valueOf(minorVersion), String.valueOf(buildVersion));
        environment.getTracer().put(EEventSeverity.DEBUG, message, EEventSource.IAD);
        Sane.setAuthorizer(new Authorizer(environment));        
    }

    @Override
    protected Throwable processEnumDevicesTaskException(final ExecutionException exception) {
        if (exception.getCause() instanceof SaneException){
            return (SaneException)exception.getCause();//NOPMD
        }else if (exception.getCause()!=null){
            return new IadException(exception.getCause());//NOPMD
        }else{
            return new IadException(exception.getMessage());//NOPMD
        }
    }

    @Override
    protected String getDeviceNotFoundMessage(final MessageProvider messageProvider) {
        return messageProvider.translate("IAD", "<html>The SANE system could not find any device.<br>Check that the scanner is plugged in and turned on<br>or check your systems scanner setup.<br>For details about SANE see the <a href='http://www.sane-project.org/'>SANE homepage</a>.</html>");
    }

    @Override
    protected boolean init(final IClientEnvironment environment) {
        try{
            initSaneLib(environment);
            return true;
        }catch(SaneException exception){
            environment.processException(exception);            
        }        
        return false;
    }
                    
    @Override
    public void releaseImpl(){
        Sane.exit();
    }
    
    public static IadUnixBackend getInstance(){
        return INSTANCE;
    }        
}
