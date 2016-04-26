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

package org.radixware.kernel.common.client.eas.connections;

import java.util.Objects;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ITaskWaiter;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.exceptions.FileException;
import org.radixware.kernel.common.client.trace.ClientTracer;
import org.radixware.kernel.common.client.utils.Pkcs11Token;
import org.radixware.kernel.common.exceptions.KeystoreControllerException;
import org.radixware.kernel.common.ssl.KeystoreController;
import org.radixware.kernel.starter.radixloader.RadixLoader;

public final class Pkcs11SlotDetector{
    
    private Pkcs11SlotDetector(){        
    }
    
    private final static class DetectingTask implements Callable<Integer>{
        
        private final Pkcs11Config pkcs11Config;
        private final String certAlias;
        private final IClientEnvironment environment;
        private final char[] password;
        
        public DetectingTask(final IClientEnvironment environment, final Pkcs11Config config, final String certificateAlias, final char[] pwd){
            pkcs11Config = config.clone();
            certAlias = certificateAlias;
            this.environment = environment;            
            password = pwd;
        }

        @Override
        public Integer call() throws Exception {
            final List<Pkcs11Token> tokens = Pkcs11Token.enumTokens(environment, pkcs11Config.getFieldValue(Pkcs11Config.Field.LIBPATH), false);
            if (tokens.isEmpty()){
                return null;
            }
            final File tmpConfigFile;
            if (RadixLoader.getInstance()==null){
                final File tmpDir = new File(environment.getWorkPath());
                tmpConfigFile = File.createTempFile("pkcs11", ".cfg", tmpDir);                
            }else{
                tmpConfigFile = RadixLoader.getInstance().createTempFile("pkcs11_cfg_");
            }
            final String tmpConfigFilePath = tmpConfigFile.getAbsolutePath();
            Collections.sort(tokens, new Comparator<Pkcs11Token>(){
                @Override
                public int compare(final Pkcs11Token t1, final Pkcs11Token t2) {
                    return Long.compare(t1.getSlotId(), t2.getSlotId());
                }            
            });
            try{
                for (Pkcs11Token token: tokens){
                    pkcs11Config.setFieldValue(Pkcs11Config.Field.SLOTLI, String.valueOf(token.getSlotId()));
                    try{
                        pkcs11Config.writeToFile(tmpConfigFilePath);
                    }catch(IOException ex){
                        final FileException exception = 
                            new FileException(environment, FileException.EExceptionCode.CANT_WRITE, tmpConfigFilePath, ex);//NOPMD
                        environment.getTracer().error(exception);
                        return null;
                    }
                    final String aliases[];
                    try{
                        aliases = getCertAlias(password, tmpConfigFilePath);                        
                    }catch(KeystoreControllerException exception){//NOPMD
                        traceException(exception,environment.getTracer());
                        continue;
                    }
                    if (aliases.length>0){
                        final String debugMessageTemplate = 
                            environment.getMessageProvider().translate("PKCS11", "Following certificates with RSA key where found on device with slot index %1$s: [%2$s]");
                        final StringBuilder certAliases = new StringBuilder();
                        for (int i=0; i<aliases.length; i++){
                            if (i>0){
                                certAliases.append(", ");
                            }
                            certAliases.append('\'');
                            certAliases.append(aliases[i]);
                            certAliases.append('\'');
                        }
                        final String traceMessage = 
                            String.format(debugMessageTemplate, String.valueOf(token.getSlotId()), certAliases.toString());
                        environment.getTracer().debug(traceMessage);
                        for (String alias: aliases){
                            if (Objects.equals(alias, certAlias)){
                                return Integer.valueOf((int)token.getSlotId());
                            }
                        }
                    }
                }
            }finally{
                if (!tmpConfigFile.delete() || tmpConfigFile.exists()){
                    tmpConfigFile.deleteOnExit();
                }
            }            
            return null;
        }
        
        private String[] getCertAlias(final char[] password, final String configPath) throws KeystoreControllerException {
            KeystoreController kc = null;
            String[] aliases = null;

            try {
                kc = KeystoreController.newTempInstance(configPath, password);
                aliases = kc.getRsaKeyAliases();
            } finally {
                try {
                    if(kc != null) {
                        kc.close();
                    }
                } catch (KeystoreControllerException ex) {
                    traceException(ex,environment.getTracer());
                }
            }

            return aliases == null ? new String[0] : aliases;
        }        
        
        public static void traceException(final Throwable exception, final ClientTracer tracer){
            final String exceptionStack = ClientException.exceptionStackToString(exception);
            if (exception.getMessage()!=null && !exception.getMessage().isEmpty()){
                tracer.debug(exception.getMessage()+"\n"+exceptionStack);
            }else{
                tracer.debug(exceptionStack);
            }        
        }        
    }
    
    public static Integer findSlotIndexByCertAlias(final IClientEnvironment environment, final Pkcs11Config config, final String certificateAlias, final char[] password, final boolean showWaitDialog){
        final String traceMessageTemplate = 
            environment.getMessageProvider().translate("PKCS11", "Looking for HSM with '%1$s' certificate. Using following PKCS#11 configuration:\n%2$s");
        environment.getTracer().debug(String.format(traceMessageTemplate, certificateAlias, config.toString()));
        final DetectingTask task = new DetectingTask(environment, config, certificateAlias, password);
        if (showWaitDialog){
            final ITaskWaiter taskWaiter = environment.getApplication().newTaskWaiter();
            taskWaiter.setMessage(environment.getMessageProvider().translate("PKCS11", "Reading the HSM"));
            try{
                return taskWaiter.runAndWait(task);
            }catch(ExecutionException exception){
                DetectingTask.traceException(exception.getCause(), environment.getTracer());
                return null;
            }catch(InterruptedException exception){//NOPMD
                return null;
            }finally{
                taskWaiter.close();
            }
        }else{
            try{
                return task.call();
            }catch(Exception exception){
                DetectingTask.traceException(exception, environment.getTracer());
                return null;
            }
        }
    }
}
