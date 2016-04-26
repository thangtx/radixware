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

package org.radixware.kernel.common.client.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ITaskWaiter;
import org.radixware.kernel.common.client.exceptions.Pkcs11Exception;


@SuppressWarnings("PMD.ImmutableField")
public final class Pkcs11Token {
    private final static String PKCS11 = "sun.security.pkcs11.wrapper.PKCS11";
    private final static String CK_C_INITIALIZE_ARGS = "sun.security.pkcs11.wrapper.CK_C_INITIALIZE_ARGS";
    private final static String CK_TOKEN_INFO = "sun.security.pkcs11.wrapper.CK_TOKEN_INFO";    
    private final static String PKCS11_GET_INSTANCE = "getInstance";
    private final static String CK_TOKEN_INFO_SERIAL_NUMBER = "serialNumber";
    private final static String CK_TOKEN_INFO_MANUFACTURER_ID = "manufacturerID";
    private final static String CK_TOKEN_INFO_MODEL = "model";
    private final static String C_GET_TOKEN_INFO = "C_GetTokenInfo";
    private final static String FUNC_LIST = "C_GetFunctionList";
    
    private final Object wrapper;
    private final Method getTokenInfo;
    private final Class<?> ck_token_info_class;
    private final Field ck_token_info_serialNumber;
    private final Field ck_token_info_manufacturerID;
    private final Field ck_token_info_model;
        
    private final long slotId;
    private final IClientEnvironment env;
    private final String serialNumber;
    private final String manufacturerID;
    private final String model;
    
    private Pkcs11Token(final Object wrapper,
                        final Method getTokenInfoMethod,
                        final long slotId,
                        final IClientEnvironment environment) 
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException, NoSuchFieldException{
        env = environment;
        this.wrapper = wrapper;
        getTokenInfo = getTokenInfoMethod;
        this.slotId = slotId;
        ck_token_info_class = Class.forName(CK_TOKEN_INFO);
        ck_token_info_serialNumber = ck_token_info_class.getDeclaredField(CK_TOKEN_INFO_SERIAL_NUMBER);        
        ck_token_info_manufacturerID = getTokenInfoClassField(CK_TOKEN_INFO_MANUFACTURER_ID);
        ck_token_info_model = getTokenInfoClassField(CK_TOKEN_INFO_MODEL);
        final Object tokenInfo = getTokenInfo();
        serialNumber = getTokenInfoFieldValue(tokenInfo, ck_token_info_serialNumber);
        if (ck_token_info_manufacturerID!=null){
            manufacturerID = getTokenInfoFieldValue(tokenInfo, ck_token_info_manufacturerID);
        }else{
            manufacturerID = null;
        }
        if (ck_token_info_model!=null){
            model = getTokenInfoFieldValue(tokenInfo, ck_token_info_model);
        }else{
            model = null;
        }
    }
    
    public static class Factory{
        
        private Factory(){            
        }
        
        /**
         * Constructs a new PKCS#11 API wrapper.
         * This wrapper can check if a token presents in the system. By default, the slot#0 is used for checks.
         * Slot id can be changed by <code>setSlotId(long)</code>
         * @param module PKCS11 library to work with a token
         * @param environment Environment
         * @see setSlotId(long)
         * @see getSlotId()
         */        
        public static Pkcs11Token newInstance(final String module, final long slotId, final IClientEnvironment environment) throws Pkcs11Exception{
            if (slotId < 0){
                throw new IllegalArgumentException("The slot id must be a non-negative number");
            }
            if (module==null || module.isEmpty()){
                throw new IllegalArgumentException("The module library path must be defined");
            }            
            try {
                final Class<?> pkcs11Wrapper = Class.forName(PKCS11);
                final Class<?> initArgsClass = Class.forName(CK_C_INITIALIZE_ARGS);
                final Constructor<?> CK_C_INIT_ARGS = initArgsClass.getDeclaredConstructor();

                final Method getInstance = 
                    pkcs11Wrapper.getMethod(PKCS11_GET_INSTANCE, String.class, String.class, initArgsClass, boolean.class);            
                final Object wrapper = 
                    getInstance.invoke(null, module, FUNC_LIST, CK_C_INIT_ARGS.newInstance(), false);
                final Method getTokenInfoMethod = 
                    pkcs11Wrapper.getDeclaredMethod(C_GET_TOKEN_INFO, long.class);
                return new Pkcs11Token(wrapper, getTokenInfoMethod, slotId, environment);
            } catch (Exception ex) {
                final String message = 
                    environment.getMessageProvider().translate("PKCS11", "Unable to check  availability of HSM");
                throw new Pkcs11Exception(message, ex);
            }
        }
    }
    
    /**
     * Determines if a PKCS11 token is inserted.
     * Also the method gets a HSM serial number in order to compare it with the serial number of a device which was the class instance initialized with.
     * @return 
     */
    public boolean isPresent() {        
        try{
           final Object tokenInfo = getTokenInfo();
           final String curSerNum = getTokenInfoFieldValue(tokenInfo, ck_token_info_serialNumber);
           if (!Objects.equals(serialNumber, curSerNum)){
               return false;
           }
           if (ck_token_info_manufacturerID!=null){
               final String curManufacturer = getTokenInfoFieldValue(tokenInfo, ck_token_info_manufacturerID);
               if (!Objects.equals(manufacturerID, curManufacturer)){
                   return false;
               }
           }
           if (ck_token_info_model!=null){
               final String curModel = getTokenInfoFieldValue(tokenInfo, ck_token_info_model);
               if (!Objects.equals(model, curModel)){
                   return false;
               }
           }                      
        } catch (Exception ex) {
            final String message = env.getMessageProvider().translate("PKCS11", "Hardware security module is removed.");
            env.getTracer().event(message);
            return false;
        }
        return true;
    }
    
    /**
     * Returns a security module device's attribute.
     * @return 
     */
    private String getTokenInfoFieldValue(final Object tokenInfo, final Field field) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        final char[] sn = (char[]) field.get(tokenInfo);
        return String.valueOf(sn);
    }
    
    private Object getTokenInfo() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        return getTokenInfo.invoke(wrapper, slotId);
    }
       
    private Field getTokenInfoClassField(final String fieldName){
        try{
            return ck_token_info_class.getField(fieldName);
        }catch(NoSuchFieldException exception){
            return null;
        }
    }

    public long getSlotId() {
        return slotId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getManufacturerID() {
        return manufacturerID;
    }

    public String getModel() {
        return model;
    }

    public IClientEnvironment getEnvironment() {
        return env;
    }        

    @Override
    public String toString() {
        final StringBuilder descriptionBuilder = new StringBuilder(128);
        if (manufacturerID!=null){
            descriptionBuilder.append("manufacturerID='");
            descriptionBuilder.append(manufacturerID);
            descriptionBuilder.append('\'');
        }
        if (model!=null){
            if (descriptionBuilder.length()>0){
                descriptionBuilder.append(' ');
            }
            descriptionBuilder.append("model='");
            descriptionBuilder.append(model);
            descriptionBuilder.append('\'');
        }
        if (descriptionBuilder.length()>0){
            descriptionBuilder.append(' ');
        }
        descriptionBuilder.append("serialNumber='");
        descriptionBuilder.append(serialNumber);
        descriptionBuilder.append("' at ");
        descriptionBuilder.append(String.valueOf(slotId));
        descriptionBuilder.append(" slot");
        return descriptionBuilder.toString();    
    }
    
    public static List<Pkcs11Token> enumTokens(final IClientEnvironment environment, final String libPath, final boolean showWaitDialog){
        if (libPath==null || libPath.isEmpty()){
            throw new IllegalArgumentException("libPath argument must be defined");
        }
        final Callable<List<Pkcs11Token>> enumTokensTask = new Callable<List<Pkcs11Token>>(){
                    @Override
                    public List<Pkcs11Token> call() throws Exception {                        
                        final List<Pkcs11Token> tokens = new LinkedList<>();
                        for (int slotId=0; slotId<100; slotId++){
                            try{
                                final Pkcs11Token token = 
                                    Pkcs11Token.Factory.newInstance(libPath, slotId, environment);
                                tokens.add(token);
                            }catch(Pkcs11Exception exception){//NOPMD
                                //just try with another slot
                            }
                        }
                        if (tokens.isEmpty()){
                            final String debugMessage = environment.getMessageProvider().translate("PKCS11", "No PKCS#11 tokens detected");
                            environment.getTracer().debug(debugMessage);
                        }else{
                            final String debugMessageTemplate = environment.getMessageProvider().translate("PKCS11", "Following PKCS#11 tokens detected:\n");
                            final StringBuilder messageBuilder = new StringBuilder(debugMessageTemplate);
                            for (Pkcs11Token token: tokens){
                                messageBuilder.append(token.toString());
                            }
                            environment.getTracer().debug(messageBuilder.toString());
                        }
                        return tokens;                    
                    }
                };
        if (showWaitDialog){
            final ITaskWaiter taskWaiter = environment.getApplication().newTaskWaiter();            
            taskWaiter.setMessage(environment.getMessageProvider().translate("PKCS11", "Detecting Devices..."));
            try {
                return taskWaiter.runAndWait(enumTokensTask);                    
            } catch (ExecutionException ex) {
                environment.getTracer().error(ex.getCause()==null ? ex : ex.getCause());
                return Collections.<Pkcs11Token>emptyList();
            } catch (InterruptedException ex) {
                return Collections.<Pkcs11Token>emptyList();
            }finally{
                taskWaiter.close();
            }
        }else{
            try{
                return enumTokensTask.call();
            }catch(Exception ex){
                environment.getTracer().error(ex.getCause()==null ? ex : ex.getCause());
                return Collections.<Pkcs11Token>emptyList();                
            }
        }
    }   
}