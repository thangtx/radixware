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

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.values.XmlValueOutOfRangeException;
import org.radixware.kernel.common.auth.AuthUtils;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.exceptions.ServiceAuthenticationException;
import org.radixware.kernel.common.client.trace.AbstractTraceBuffer;
import org.radixware.kernel.common.client.utils.ISecretStore;
import org.radixware.kernel.common.client.utils.TokenProcessor;
import org.radixware.kernel.common.enums.EAuthType;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.exceptions.ServiceCallException;
import org.radixware.kernel.common.exceptions.ServiceCallSendException;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.trace.TraceItem;
import org.radixware.kernel.common.utils.XmlObjectProcessor;
import org.radixware.kernel.common.utils.XmlUtils;
import org.radixware.schemas.eas.*;
import org.radixware.schemas.easWsdl.*;


class EasMessageProcessor {

    private final IClientEnvironment environment;
    private final ITokenCalculator tokenCalculator;
    private final ISecretStore clientKeyStore;
    private final EAuthType authType;
    private final java.lang.Object semaphore = new java.lang.Object();
    private volatile long sessionId;
    private Request.DefinitionsVer userDefVer;
    private byte[] serverChallenge;
    private byte[] clientChallenge;
    private String scp;    
    
    private EasMessageProcessor(final IClientEnvironment environment, final ITokenCalculator tokenCalculator, final ISecretStore keyStore, final EAuthType authType) {
        this.environment = environment;
        this.tokenCalculator = tokenCalculator;
        this.clientKeyStore = keyStore;
        this.authType = authType;
    }    

    public EasMessageProcessor(final IClientEnvironment environment, final ITokenCalculator tokenCalculator, final EAuthType authType) {
        this(environment,tokenCalculator,null,authType);
    }
    
    public EasMessageProcessor createCopy(final ITokenCalculator tokenCalculator, final EAuthType authType) {
        return createCopy(tokenCalculator, clientKeyStore, authType);
    }

    public EasMessageProcessor createCopy(final ITokenCalculator tokenCalculator, final ISecretStore keyStore, final EAuthType authType) {
        final EasMessageProcessor result = new EasMessageProcessor(environment, tokenCalculator, keyStore, authType);
        synchronized(semaphore){
            try {
                if (userDefVer != null) {
                    result.userDefVer = Request.DefinitionsVer.Factory.newInstance();
                    result.userDefVer.setLongValue(userDefVer.getLongValue());
                }
            } catch (XmlValueOutOfRangeException ex) {
                environment.processException(ex);
            }
            result.scp = scp;
            result.sessionId = sessionId;
            if (serverChallenge != null) {
                result.serverChallenge = Arrays.copyOf(serverChallenge, serverChallenge.length);
            }
            if (clientChallenge != null) {
                result.clientChallenge = Arrays.copyOf(clientChallenge, clientChallenge.length);
            }        
        }
        return result;
    }
/*
    public final byte[] createEncryptedHashForNewPassword(final String userName, final String newPassword) {
        final byte[] newPwdHash = AuthUtils.calcPwdHash(userName, newPassword);
        return AuthUtils.encryptNewPwdHash(newPwdHash, pwdHash);
    }
*/
    public String getCurrentScpName() {
        synchronized(semaphore){
            return scp;
        }
    }
    
    public long getSessionId(){
        synchronized(semaphore){
            return sessionId;
        }
    }

    private XmlObject prepareDocument(final XmlObject request) throws ServiceCallException {
        if (request instanceof ReadManifestRq) {
            final ReadManifestDocument document = ReadManifestDocument.Factory.newInstance();
            document.addNewReadManifest().setReadManifestRq((ReadManifestRq) request);
            return document;
        } else if (request instanceof CreateSessionRq) {
            final CreateSessionDocument document = CreateSessionDocument.Factory.newInstance();
            document.addNewCreateSession().setCreateSessionRq((CreateSessionRq) request);
            return document;
        } else if (request instanceof LoginRq){
            final LoginDocument document = LoginDocument.Factory.newInstance();
            document.addNewLogin().setLoginRq((LoginRq) request);
            return document;
        } else if (request instanceof TestRq) {
            final TestDocument doc = TestDocument.Factory.newInstance();
            doc.addNewTest().setTestRq((TestRq) request);
            return doc;
        } else if (request instanceof ChangePasswordRq) {
            final ChangePasswordDocument doc = ChangePasswordDocument.Factory.newInstance();
            doc.addNewChangePassword().setChangePasswordRq((ChangePasswordRq) request);
            return doc;
        } else if (request instanceof SelectRq) {
            final SelectDocument doc = SelectDocument.Factory.newInstance();
            doc.addNewSelect().setSelectRq((SelectRq) request);
            return doc;
        } else if (request instanceof ReadRq) {
            final ReadDocument doc = ReadDocument.Factory.newInstance();
            doc.addNewRead().setReadRq((ReadRq) request);
            return doc;
        } else if (request instanceof ContextlessCommandRq) {
            final ContextlessCommandDocument doc = ContextlessCommandDocument.Factory.newInstance();
            doc.addNewContextlessCommand().setContextlessCommandRq((ContextlessCommandRq) request);
            return doc;
        } else if (request instanceof CommandRq) {
            final CommandDocument doc = CommandDocument.Factory.newInstance();
            doc.addNewCommand().setCommandRq((CommandRq) request);
            return doc;
        } else if (request instanceof DeleteRq) {
            final DeleteDocument doc = DeleteDocument.Factory.newInstance();
            doc.addNewDelete().setDeleteRq((DeleteRq) request);
            return doc;
        } else if (request instanceof ListInstantiatableClassesRq) {
            final ListInstantiatableClassesDocument doc = ListInstantiatableClassesDocument.Factory.newInstance();
            doc.addNewListInstantiatableClasses().setListInstantiatableClassesRq((ListInstantiatableClassesRq) request);
            return doc;
        } else if (request instanceof PrepareCreateRq) {
            final PrepareCreateDocument doc = PrepareCreateDocument.Factory.newInstance();
            doc.addNewPrepareCreate().setPrepareCreateRq((PrepareCreateRq) request);
            return doc;
        } else if (request instanceof CreateRq) {
            final CreateDocument doc = CreateDocument.Factory.newInstance();
            doc.addNewCreate().setCreateRq((CreateRq) request);
            return doc;
        } else if (request instanceof SetParentRq) {
            final SetParentDocument doc = SetParentDocument.Factory.newInstance();
            doc.addNewSetParent().setSetParentRq((SetParentRq) request);
            return doc;
        } else if (request instanceof UpdateRq) {
            final UpdateDocument doc = UpdateDocument.Factory.newInstance();
            doc.addNewUpdate().setUpdateRq((UpdateRq) request);
            return doc;
        } else if (request instanceof ListVisibleExplorerItemsRq) {
            final ListVisibleExplorerItemsDocument doc = ListVisibleExplorerItemsDocument.Factory.newInstance();
            doc.addNewListVisibleExplorerItems().setListVisibleExplorerItemsRq((ListVisibleExplorerItemsRq) request);
            return doc;
        } else if (request instanceof ListEdPresVisibleExpItemsRq) {
            final ListEdPresVisibleExpItemsDocument doc = ListEdPresVisibleExpItemsDocument.Factory.newInstance();
            doc.addNewListEdPresVisibleExpItems().setListEdPresVisibleExpItemsRq((ListEdPresVisibleExpItemsRq) request);
            return doc;
        } else if (request instanceof GetObjectTitlesRq) {
            final GetObjectTitlesDocument doc = GetObjectTitlesDocument.Factory.newInstance();
            doc.addNewGetObjectTitles().setGetObjectTitlesRq((GetObjectTitlesRq) request);
            return doc;
        } else if (request instanceof CalcSelectionStatisticRq){
            final CalcSelectionStatisticDocument doc = CalcSelectionStatisticDocument.Factory.newInstance();
            doc.addNewCalcSelectionStatistic().setCalcSelectionStatisticRq((CalcSelectionStatisticRq) request);
            return doc;
        } else if (request instanceof GetPasswordRequirementsRq) {
            final GetPasswordRequirementsDocument doc = GetPasswordRequirementsDocument.Factory.newInstance();
            doc.addNewGetPasswordRequirements().setGetPasswordRequirementsRq((GetPasswordRequirementsRq) request);
            return doc;
        } else if (request instanceof GetDatabaseInfoRq){
            final GetDatabaseInfoDocument doc = GetDatabaseInfoDocument.Factory.newInstance();
            doc.addNewGetDatabaseInfo().setGetDatabaseInfoRq((GetDatabaseInfoRq)request);
            return doc;
        } else if (request instanceof CloseSessionRq){
            final CloseSessionDocument doc = CloseSessionDocument.Factory.newInstance();
            doc.addNewCloseSession().setCloseSessionRq((CloseSessionRq)request);
            return doc;
        }
        throw new ServiceCallSendException("Unknown eas request \"" + request.getClass().getName() + "\"");
    }

    public XmlObject prepareRequest(final XmlObject requestBody) throws ServiceClientException {
        synchronized(semaphore){
            if (requestBody instanceof Request && serverChallenge!=null){//sessionRequest
                final Request rq = (Request) requestBody;
                try {
                    if (authType==EAuthType.PASSWORD || requestBody instanceof LoginRq==false){
                        rq.setPwdToken(tokenCalculator.calcToken(serverChallenge).token);
                    }
                    rq.setAuthType(requestBody instanceof ChangePasswordRq ? EAuthType.PASSWORD : authType);
                } catch (Exception e) {
                    final String msg = environment.getMessageProvider().translate("ClientSessionException", "Can't generate token: %s");
                    final String info = e.getMessage() != null && !e.getMessage().isEmpty() ? e.getMessage() : e.getClass().getName();
                    throw new ServiceClientException(String.format(msg, info),e);
                }
                rq.setSessionId(sessionId);
                if (environment.getTracer().isServerTraceAllowed()) {
                    rq.setTraceProfile(environment.getTracer().getProfile().toString());
                }

                if (userDefVer == null) {
                    userDefVer = Request.DefinitionsVer.Factory.newInstance();
                }

                if (environment.getDefManager().isOldVersionModeEnabled()) {
                    userDefVer.setForced(true);
                } else {
                    userDefVer.setForced(false);
                }

                userDefVer.setLongValue(environment.getDefManager().getAdsVersion().getNumber());
                rq.setDefinitionsVer(userDefVer);
                if (authType==EAuthType.KERBEROS){
                    clientChallenge = genClientChallenge();
                    rq.setChallenge(clientChallenge);                
                }
            }else if (requestBody instanceof TestRq){
                ((TestRq) requestBody).setSessionId(sessionId);
            }
            return prepareDocument(requestBody);
        }
    }
    
    private byte[] genClientChallenge(){
        final byte[] challenge = new byte[8];
        new Random().nextBytes(challenge);
        return challenge;
    }

    public XmlObject parseResponce(final XmlObject message, final AbstractTraceBuffer explorerTrace) throws ServiceAuthenticationException {
        synchronized(semaphore){
            XmlObject response = null;
            if (message instanceof TestMess) {
                final TestRs rs = ((TestMess) message).getTestRs();
                serverChallenge = rs.getChallenge();
                response = rs;
            } else if (message instanceof CreateSessionMess) {
                final CreateSessionRs rs = ((CreateSessionMess) message).getCreateSessionRs();
                scp = rs.getScpName();
                serverChallenge = rs.getChallenge();
                sessionId = rs.getSessionId();
                response = rs;
            } else if (message instanceof CloseSessionMess){
                response = ((CloseSessionMess) message).getCloseSessionRs();
            } else {
                response = XmlObjectProcessor.getXmlObjectFirstChild(message);
            }

            if (response instanceof Response) {

                final Response rsp = (Response) response;
                if (authType== EAuthType.KERBEROS){                
                    final byte[] key = new TokenProcessor().decryptBytes(clientKeyStore.getSecret());
                    try{
                        final byte[] token = AuthUtils.calcPwdToken(clientChallenge, key);
                        if (!Arrays.equals(token, rsp.getSecurityToken())) {
                            throw new ServiceAuthenticationException();
                        }
                    }finally{
                        Arrays.fill(key, (byte)0);
                    }
                }
                scp = rsp.getScpName();
                serverChallenge = rsp.getChallenge();

                if (rsp.getTrace() != null && rsp.getTrace().getItemList() != null) {
                    final List<org.radixware.schemas.eas.Trace.Item> traceItems = rsp.getTrace().getItemList();
                    //Put into explorer trace all server trace messages:
                    for (org.radixware.schemas.eas.Trace.Item item : traceItems) {
                        final EEventSeverity severity = EEventSeverity.getForName(item.getLevel().toString());                        
                        final String traceItemText = XmlUtils.parseSafeXmlString(item.getStringValue());
                        try {
                            final java.lang.Object parts[] = TraceItem.parseMess(traceItemText);
                            if (environment.getTracer().getProfile().itemMatch(severity, (String)parts[3])) {
                                explorerTrace.put(severity,parts);
                            }
                        } catch (ParseException ex) {
                            final String errorMessage = environment.getMessageProvider().translate("ExplorerError", "Can't parse trace message \'%s\':\n%s");
                            final String itemText = String.format(errorMessage, traceItemText, ex.toString());
                            if (environment.getTracer().getProfile().itemMatch(EEventSeverity.ERROR,EEventSource.CLIENT.getValue())) {
                                explorerTrace.put(EEventSeverity.ERROR, itemText, EEventSource.CLIENT.getValue());
                            }
                        }
                    }
                }
            }

            return response;
        }
    }
    
    private static final Class<?>[] callBackRequests = {MessageDialogOpenMess.class,
        MessageDialogWaitButtonMess.class,
        ProgressDialogStartProcessMess.class,
        ProgressDialogSetMess.class,
        ProgressDialogTraceMess.class,
        ProgressDialogFinishProcessMess.class,
        FileSelectMess.class,
        FileAccessMess.class,
        FileSizeMess.class,
        FileTransitMess.class,
        FileOpenMess.class,
        FileCloseMess.class,
        FileReadMess.class,
        FileWriteMess.class,
        FileSeekMess.class,
        FileDeleteMess.class,
        FileCopyMess.class,
        FileMoveMess.class,
        FileDirSelectMess.class,
        FileDirReadMess.class,
        FileDirCreateMess.class,
        FileDirDeleteMess.class,        
        FileDirMoveMess.class,  
        FileDirGetUserHomeMess.class,
        GetUserDownloadsDirMess.class,
        TestIfDirExistsMess.class,
        TestIfFileExistsMess.class,
        ClientMethodInvocationMess.class,
        GetSecurityTokenMess.class
    };

    public static boolean isCallBackRequest(XmlObject response) {
        if (response == null) {
            return false;
        }
        for (Class<?> callBackRequest : callBackRequests) {
            if (callBackRequest.isInstance(response)) {
                return true;
            }
        }
        return false;
    }
}
