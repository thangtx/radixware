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

package org.radixware.kernel.common.kerberos;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.InvalidPathException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.kerberos.KerberosKey;
import javax.security.auth.kerberos.KerberosPrincipal;
import javax.security.auth.kerberos.KeyTab;
import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;


public final class KerberosUtils {
    
    public static final Oid KRB5_OID=createOid("1.2.840.113554.1.2.2");
    public static final Oid SPNEGO_OID=createOid("1.3.6.1.5.5.2");

    private final static String SECTION_NAME_IN_LOGIN_CONFIGURATION = "Server";
    private final static String ETYPE_CLASS_NAME = "sun.security.krb5.internal.crypto.EType";
    private final static String GET_BUILDIN_DEFAULTS_METHOD_NAME = "getBuiltInDefaults";
    
    private static int[] supportedETypes = null;
    private final static Object SUPPORTED_ETYPES_SEM = new Object();

    private KerberosUtils() {
    }

    public static enum EKrbLoginEvent {

        USING_DEFAULT_KEY_TAB_FILE(EEventSeverity.DEBUG, 0),
        KEYTAB_FILE_DOES_NOT_EXIST(EEventSeverity.ERROR, 1),
        KEYTAB_FILE_FOUND(EEventSeverity.DEBUG, 1),
        WRONG_SPN_FORMAT(EEventSeverity.ERROR, 1),
        SPN_TO_USE(EEventSeverity.DEBUG, 1),
        NO_KEYS(EEventSeverity.DEBUG, 2),
        SOME_KEYS_FOUND(EEventSeverity.DEBUG, 3),
        SUPPORTED_ETYPES(EEventSeverity.DEBUG, 1),
        NO_COMPATIBLE_KEY(EEventSeverity.WARNING, 2),
        ERROR_ON_LOGIN(EEventSeverity.ERROR, 2),
        CANT_GET_GSS_CREDENTIALS(EEventSeverity.ERROR, 2);
        private final int argsCount;
        private final EEventSeverity severity;

        private EKrbLoginEvent(final EEventSeverity severity, final int argsCount) {
            this.argsCount = argsCount;
            this.severity = severity;
        }

        public int getMessageArgsCount() {
            return argsCount;
        }

        public EEventSeverity getSeverity() {
            return severity;
        }
    }

    public static interface IKrbLoginEventsPrinter {

        void printEvent(final EKrbLoginEvent event, final String[] args);
    }
    private static final IKrbLoginEventsPrinter DUMMY_EVENTS_PRINTER = new IKrbLoginEventsPrinter() {
        @Override
        public void printEvent(final EKrbLoginEvent event, final String[] args) {
        }
    };

    private static boolean isSomeKeySupported(final int[] supportedKeyTypes, final KerberosKey[] keys) {
        for (KerberosKey key : keys) {
            final int keyType = key.getKeyType();
            for (int i = 0; i < supportedKeyTypes.length; i++) {
                if (keyType == supportedKeyTypes[i]) {
                    return true;
                }
            }
        }
        return false;
    }

    private static String printKeysInfo(final KerberosKey[] keys) {
        Method toString = null;
        Class etypeClass = null;
        try{
            etypeClass = Class.forName(ETYPE_CLASS_NAME);
        }catch(ClassNotFoundException | LinkageError e){//NOPMD            
        }        
        if (etypeClass!=null){
            try{
                toString = etypeClass.getDeclaredMethod("toString",int.class);
            }catch(NoSuchMethodException | SecurityException e){//NOPMD                
            }        
        }
        final StringBuilder strBuilder = new StringBuilder();
        for (KerberosKey key : keys) {
            if (strBuilder.length() > 0) {
                strBuilder.append('\n');
            }
            strBuilder.append("kvno = ");
            strBuilder.append(key.getVersionNumber());
            strBuilder.append('\t');
            strBuilder.append(key.getKeyType());            
            if (toString!=null){
                final Object invocationResult;
                try{
                    invocationResult = toString.invoke(null, Integer.valueOf(key.getKeyType()));
                }catch(IllegalArgumentException | IllegalAccessException | InvocationTargetException e){//NOPMD
                    continue;
                }
                if (invocationResult instanceof String){
                    strBuilder.append('\t');
                    strBuilder.append((String)invocationResult);
                }
            }
        }
        return strBuilder.toString();
    }

    private static Map<String, String> getLoginParameters(final String keyTab, final String principal) {
        final Map<String, String> parameters = new HashMap<>();
        parameters.put("isInitiator", "false");
        parameters.put("useKeyTab", "true");
        if (keyTab != null && !keyTab.isEmpty()) {
            parameters.put("keyTab", keyTab);
        }
        parameters.put("storeKey", "true");
        parameters.put("useTicketCache", "false");
        parameters.put("principal", principal);
        return parameters;
    }
    
    private static int[] getBuildInEncriptionTypes(){
        synchronized(SUPPORTED_ETYPES_SEM){
            if (supportedETypes==null){
                final Class etypeClass;
                try{
                    etypeClass = Class.forName(ETYPE_CLASS_NAME);
                }catch(ClassNotFoundException | LinkageError e){//NOPMD
                    return new int[0];
                }
                final Method getBuildInDefaults;
                try{
                    getBuildInDefaults = etypeClass.getDeclaredMethod(GET_BUILDIN_DEFAULTS_METHOD_NAME);            
                }catch(NoSuchMethodException | SecurityException e){//NOPMD
                    return new int[0];
                }
                final Object result;
                try{
                    result = getBuildInDefaults.invoke(null);
                }catch(IllegalArgumentException | IllegalAccessException | InvocationTargetException e){//NOPMD
                    return new int[0];
                }
                supportedETypes = result instanceof int[] ? (int[])result : new int[0];
            }
            return supportedETypes;
        }
    }

    private static KerberosLoginConfiguration createLoginConfiguration(final String keyTab, final String principal) {
        final Map<String, String> parameters = getLoginParameters(keyTab, principal);
        return KerberosLoginConfiguration.Factory.newInstance(SECTION_NAME_IN_LOGIN_CONFIGURATION, parameters);
    }

    private static String printSupportedEncryptionTypes() {
        Method toString = null;
        Class etypeClass = null;
        try{
            etypeClass = Class.forName(ETYPE_CLASS_NAME);
        }catch(ClassNotFoundException | LinkageError e){//NOPMD            
        }        
        if (etypeClass!=null){
            try{
                toString = etypeClass.getDeclaredMethod("toString",int.class);
            }catch(NoSuchMethodException | SecurityException e){//NOPMD                
            }        
        }
        final StringBuilder strBuilder = new StringBuilder();
        for (int type : getBuildInEncriptionTypes()) {
            if (strBuilder.length() > 0) {                
                strBuilder.append(toString==null ? ' ': '\n');
            }
            strBuilder.append(type);
            if (toString!=null){       
                final Object invocationResult;
                try{
                    invocationResult = toString.invoke(null, Integer.valueOf(type));
                }catch(IllegalArgumentException | IllegalAccessException | InvocationTargetException e){//NOPMD
                    continue;
                }
                if (invocationResult instanceof String){
                    strBuilder.append('\t');
                    strBuilder.append((String)invocationResult);
                }
            }
        }
        return strBuilder.toString();
    }

    public static KerberosCredentials createCredentials(final KrbServiceOptions options, final IKrbLoginEventsPrinter eventsPrinter) {
        final IKrbLoginEventsPrinter printer = eventsPrinter == null ? DUMMY_EVENTS_PRINTER : eventsPrinter;        
        final String keyTabPath;
        try {
            keyTabPath = options.getAbsoluteKeyTabPath();
        } catch (InvalidPathException exception) {
            printer.printEvent(EKrbLoginEvent.KEYTAB_FILE_DOES_NOT_EXIST, new String[]{options.getKeyTabPath()});
            return null;
        }
        final KeyTab keyTab;
        if (keyTabPath == null || keyTabPath.isEmpty()) {
            printer.printEvent(EKrbLoginEvent.USING_DEFAULT_KEY_TAB_FILE, new String[]{});
            keyTab = KeyTab.getInstance();
        } else {
            final File keyTabFile = new File(keyTabPath);
            if (keyTabFile.exists()) {
                printer.printEvent(EKrbLoginEvent.KEYTAB_FILE_FOUND, new String[]{keyTabFile.getAbsolutePath()});
                keyTab = KeyTab.getInstance(keyTabFile);
            } else {
                printer.printEvent(EKrbLoginEvent.KEYTAB_FILE_DOES_NOT_EXIST, new String[]{keyTabPath});
                return null;
            }
        }
        final KerberosPrincipal principal;
        try {
            principal = options.getKerberosPrincipal();
        } catch (IllegalArgumentException exception) {
            final String reason = exception.getLocalizedMessage();
            printer.printEvent(EKrbLoginEvent.WRONG_SPN_FORMAT, new String[]{options.getPrincipalName(), reason});
            return null;
        }

        final String principalName = principal.getName();
        final KerberosKey[] keys = keyTab.getKeys(principal);
        printer.printEvent(EKrbLoginEvent.SPN_TO_USE, new String[]{principalName});
        if (keys.length == 0) {
            printer.printEvent(EKrbLoginEvent.NO_KEYS, new String[]{principalName, keyTabPath});
        } else {
            final String keysInfo = printKeysInfo(keys);
            printer.printEvent(EKrbLoginEvent.SOME_KEYS_FOUND, new String[]{principalName, keyTabPath, keysInfo});
        }
        if (!isSomeKeySupported(getBuildInEncriptionTypes(), keys)) {
            if (keys.length > 0) {
                final String etypes = printSupportedEncryptionTypes();
                printer.printEvent(EKrbLoginEvent.SUPPORTED_ETYPES, new String[]{etypes});
            }
            printer.printEvent(EKrbLoginEvent.NO_COMPATIBLE_KEY, new String[]{principalName, keyTabPath});
        }
        final KerberosLoginConfiguration config = createLoginConfiguration(keyTabPath, principalName);
        try {
            return KerberosCredentials.Factory.newServiceCredentials(principalName, config, options.isSpnego());
        } catch (KerberosException exception) {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(exception);
            if (exception.isLoginException()) {
                printer.printEvent(EKrbLoginEvent.ERROR_ON_LOGIN, new String[]{principalName, exStack});
            } else {
                printer.printEvent(EKrbLoginEvent.CANT_GET_GSS_CREDENTIALS, new String[]{principalName, exStack});
            }
        } catch (Exception exception) {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(exception);
            printer.printEvent(EKrbLoginEvent.CANT_GET_GSS_CREDENTIALS, new String[]{principalName, exStack});
        }
        return null;
    }

    public static String extractInitiatorName(final GSSContext gssContext) throws KerberosException {
        final String fullInitiatorName;
        try {
            fullInitiatorName = gssContext.getSrcName().toString();
        } catch (GSSException exception) {
            throw new KerberosException(exception);
        }
        int i=0;
        boolean isEscape = false;
        final StringBuffer initiatorName = new StringBuffer(fullInitiatorName.length());
        for (;i<fullInitiatorName.length();i++){
            if (fullInitiatorName.charAt(i)=='@' && !isEscape){
                break;
            }
            if (fullInitiatorName.charAt(i)=='\\'){
                isEscape = true;
            }else{
                initiatorName.append(fullInitiatorName.charAt(i));
                isEscape = false;
            }
        }
        return initiatorName.toString();
    }
    
    private static Oid createOid(final String oidStr) {
        try {
            return new Oid(oidStr);
        } catch (GSSException e) {
            final String exceptionMessage = e.getMessage();
            final String message = exceptionMessage==null || exceptionMessage.isEmpty() ? "" : ":\n"+exceptionMessage;
            Logger.getLogger(KerberosUtils.class.getName()).log(Level.SEVERE, "Failed to parse Oid string: ''{0}''{1}", new Object[]{oidStr, message});
            return null;
        }
    }
}