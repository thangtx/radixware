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

package org.radixware.kernel.server.arte.services.eas;

import java.util.Arrays;
import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.MessageProp;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.exceptions.ResourceUsageException;
import org.radixware.kernel.common.exceptions.ResourceUsageTimeout;
import org.radixware.kernel.common.exceptions.ServiceProcessClientFault;
import org.radixware.kernel.common.exceptions.ServiceProcessServerFault;
import org.radixware.kernel.common.kerberos.KerberosCredentials;
import org.radixware.kernel.common.kerberos.KerberosException;
import org.radixware.kernel.common.kerberos.KerberosUtils;
import org.radixware.kernel.common.kerberos.KrbServiceOptions;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.Hex;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.instance.KrbLoginEventsPrinter;
import org.radixware.schemas.eas.ExceptionEnum;
import org.radixware.schemas.eas.GetSecurityTokenMess;
import org.radixware.schemas.eas.GetSecurityTokenRq;
import org.radixware.schemas.easWsdl.GetSecurityTokenDocument;


class GSSHandshaker {
    
    public static class HandshakeResult{
        private final GSSContext gssContext;
        private final String clientKey;
        private final String initiatorName;
        
        public HandshakeResult(final GSSContext context, final String clientKey, final String initiator){
            gssContext = context;
            initiatorName = initiator;
            this.clientKey = clientKey;
        }
        
        public GSSContext getGSSContext(){
            return gssContext;
        }
        
        public String getInitiatorName(){
            return initiatorName;
        }
        
        public String getClientKey(){
            return clientKey;
        }
    }
    
    private GSSHandshaker(){
        
    }
    
    private static final GSSHandshaker INSTANCE = new GSSHandshaker();
    
    public static GSSHandshaker getInstance(){
        return INSTANCE;
    }
    
    private static class ClientToken{
        public final byte[] krbEncKey;
        public final byte[] outToken;        
        
        public ClientToken(final byte[] outToken, final byte[] krbEncKey){
            this.outToken = outToken;
            this.krbEncKey = krbEncKey;
        }
    }
    
    private static class KrbWebServiceOptions extends KrbServiceOptions{
        
        public KrbWebServiceOptions(final String keytabFile,final String spn){
            super(keytabFile,spn,true);
        }

        @Override
        protected String getDefaultPrincipalName() {
            return "";//Principal name is always defined here
        }                        
    }
    
    public HandshakeResult doHandshake(final Arte arte, final KerberosCredentials krbCreds, final byte[] initialToken, final String stationName) throws KerberosException, ServiceProcessClientFault{
        final GSSContext gssContext = krbCreds.createSecurityContext();
        final byte[] firstToken;
        if (initialToken==null || initialToken.length==0){
            firstToken = requestToken(arte, new byte[]{}, true).outToken;
        }else{
            firstToken = initialToken;                    
        }
        byte[] token = null;
        do{            
            token = token==null ? firstToken : requestToken(arte,token,true).outToken;
            token = krbCreds.getNextHandshakeToken(gssContext, token);
        }while(!gssContext.isEstablished());                
        final String initiatorName;
        //Send final token to client.        
        final ClientToken response = requestToken(arte, token, false);
        //Read client encryption key
        final byte[] encryptedKey = response.krbEncKey;
        final byte[] decryptedKey;
        try{
            decryptedKey = gssContext.unwrap(encryptedKey, 0, encryptedKey.length, new MessageProp(true));
        }catch(GSSException exception){
            throw new KerberosException(exception);
        }
        if (response.outToken!=null && response.outToken.length>0){//SPNEGO
            initiatorName = spnegoAuth(arte,gssContext,response.outToken,stationName);
        }else{
            initiatorName = KerberosUtils.extractInitiatorName(gssContext);
        }
        return new HandshakeResult(gssContext, Hex.encode(decryptedKey), initiatorName);
    }
    
    private static String spnegoAuth(final Arte arte, final GSSContext gssContext, final byte[] initialToken, final String stationName) throws KerberosException{
        final String spnegoServiceName;
        try{
            spnegoServiceName = gssContext.getSrcName().toString();
        }
        catch(GSSException exception){
            throw new KerberosException(exception);
        }
        final String keytabFile = arte.getInstance().getKerberosServiceOptions().getKeyTabPath();
        final KrbWebServiceOptions options = new KrbWebServiceOptions(keytabFile,spnegoServiceName);
        final String instanceTitle = arte.getInstance().getFullTitle();
        final KrbLoginEventsPrinter eventsPriner = 
            new KrbLoginEventsPrinter(arte.getInstance().getTrace(), instanceTitle, "Radix WEB service");
        final KerberosCredentials webServiceCreds = 
                KerberosUtils.createCredentials(options, eventsPriner);
        if (webServiceCreds==null){
            final ArrStr loginTraceDetails = new ArrStr(stationName, String.valueOf(arte.getArteSocket().getRemoteAddress()),"Unable to get credentials for service "+spnegoServiceName);
            arte.getTrace().put(EEventSeverity.EVENT, Messages.MLS_ID_UNABLE_AUTH_USER_VIA_KERBEROS_WITH_REASON, loginTraceDetails, EEventSource.APP_AUDIT.getValue());
            throw new ServiceProcessClientFault(ExceptionEnum.KERBEROS_AUTHENTICATION_FAILED.toString(), null, null, null);//do not send stack or error reason to client
        }
        try{
            final GSSContext spnegoGssContext = webServiceCreds.createSecurityContext();
            try{
                byte encryptedToken[] = null, decryptedToken[] = null, token[] = null;
                do{
                    encryptedToken = encryptedToken==null ? initialToken : requestToken(arte,token,true).outToken;
                    try{
                        decryptedToken = gssContext.unwrap(encryptedToken, 0, encryptedToken.length, new MessageProp(true));
                    }catch(GSSException exception){
                        throw new KerberosException(exception);
                    }                
                    token = webServiceCreds.getNextHandshakeToken(spnegoGssContext, decryptedToken);
                }while(!spnegoGssContext.isEstablished());
                return KerberosUtils.extractInitiatorName(spnegoGssContext);
            }finally{
                try{
                    spnegoGssContext.dispose();
                }catch(GSSException exception){
                    //ignore
                }
            }
        }finally{
            webServiceCreds.dispose();
        }
    }
    
    private static ClientToken requestToken(final Arte arte, final byte[] inToken, final boolean outTokenRequired){
        final GetSecurityTokenDocument doc = GetSecurityTokenDocument.Factory.newInstance();
        final GetSecurityTokenRq request = doc.addNewGetSecurityToken().addNewGetSecurityTokenRq();
        request.setInputToken(inToken);
        try {
            final GetSecurityTokenMess mess = (GetSecurityTokenMess) arte.getArteSocket().invokeResource(doc, GetSecurityTokenMess.class, 20);//20 seconds
            final byte token[] = mess.getGetSecurityTokenRs().getOutToken();
            final byte key[] = mess.getGetSecurityTokenRs().getKrbEncKey();
            if (outTokenRequired && (token==null || token.length==0)){
                throw new ServiceProcessServerFault(ExceptionEnum.SERVER_MALFUNCTION.toString(), "Can't get client security token", null, null);
            }            
            return new ClientToken(token, key);
        } catch (ResourceUsageException | ResourceUsageTimeout | InterruptedException exception) {
            throw new ServiceProcessServerFault(ExceptionEnum.SERVER_MALFUNCTION.toString(), "Can't get client security token", exception, null);
        }
    }    
}
