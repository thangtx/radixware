/*
 * Copyright (c) 2008-2018, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */
package org.radixware.kernel.server.arte.services.eas;

import java.nio.ByteBuffer;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.Blob;
import java.util.List;
import org.radixware.kernel.server.arte.Arte;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.xmlbeans.XmlObject;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.MessageProp;

import org.radixware.kernel.common.enums.*;
import org.radixware.kernel.common.exceptions.*;
import org.radixware.kernel.common.kerberos.KerberosException;
import org.radixware.kernel.common.ssl.CertificateUtils;
import org.radixware.kernel.common.trace.TraceItem;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.ByteBufferInputStream;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.Hex;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.server.SrvRunParams;
import org.radixware.kernel.server.arte.ArteProfiler;
import org.radixware.kernel.server.arte.ArteTransactionParams;
import org.radixware.kernel.server.arte.RoleLoadError;
import org.radixware.kernel.server.exceptions.DatabaseError;
import org.radixware.kernel.server.jdbc.DelegateDbQueries;
import org.radixware.kernel.server.jdbc.Stmt;
import org.radixware.kernel.server.jdbc.IDbQueries;
import org.radixware.kernel.server.jdbc.RadixConnection;
import org.radixware.kernel.server.meta.presentations.RadContextlessCommandDef;
import org.radixware.kernel.server.meta.presentations.RadExplorerRootDef;
import org.radixware.kernel.starter.meta.RevisionMeta;
import org.radixware.kernel.starter.radixloader.RadixLoader;
import org.radixware.kernel.starter.radixloader.RadixSVNLoader;
import org.radixware.schemas.eas.*;
import org.radixware.schemas.eas.CreateSessionRs.ContextlessCommands;
import org.radixware.schemas.eas.CreateSessionRs.ExplorerRoots;
import org.radixware.schemas.eas.CreateSessionRs.ExplorerRoots.Item;
import org.radixware.schemas.eas.CreateSessionRs.ServerResources;
import org.radixware.schemas.easWsdl.CreateSessionDocument;
import org.radixware.schemas.easWsdl.GetSecurityTokenDocument;
import org.radixware.schemas.types.MapStrStr;

final class CreateSessionRequest extends EasRequest {

    private static class LoginResult {

        public final int sessionId;
        public final boolean passwordExpired;
        public final boolean canChangePassword;
        public final boolean sessionsLimitExceed;
        public final int maxSessionsNumber;
        public final String masterSessionKey;
        public final String pwdHashAlgo;

        public LoginResult(final int sessionId,
                final boolean mustChangePassword,
                final boolean canChangePassword,
                final boolean sessionsLimitExceed,
                final int maxSessionsNumber,
                final String masterSessionKey,
                final String pwdHashAlgo) {
            this.sessionId = sessionId;
            this.passwordExpired = mustChangePassword;
            this.canChangePassword = canChangePassword;
            this.sessionsLimitExceed = sessionsLimitExceed;
            this.maxSessionsNumber = maxSessionsNumber;
            this.masterSessionKey = masterSessionKey;
            this.pwdHashAlgo = pwdHashAlgo;
        }
    }

    //Queries    
    private static final String certAttrTypeQryStmtSQL = "select CERTATTRFORUSERLOGIN from RDX_SYSTEM where RDX_SYSTEM.ID=1";
    private static final Stmt certAttrTypeQryStmt = new Stmt(certAttrTypeQryStmtSQL);
    private final PreparedStatement certAttrTypeQry;
    
    private static final String checkSessionsLimitSqlExpl = "(case when "
            + "(u.SESSIONSLIMIT = 0 AND (sys.LIMITEASSESSIONSPERUSR is NULL or sys.LIMITEASSESSIONSPERUSR < 1 or ( select count(*) from RDX_EASSESSION where RDX_EASSESSION.USERNAME = u.Name AND RDX_EASSESSION.ISINTERACTIVE = 1 AND (RDX_EASSESSION.USERCERTIFICATE is null OR dbms_lob.getlength(RDX_EASSESSION.USERCERTIFICATE)=0) ) < sys.LIMITEASSESSIONSPERUSR))"
            + " OR "
            + "(u.SESSIONSLIMIT !=0 AND (u.SESSIONSLIMIT is NULL or u.SESSIONSLIMIT < 1 or ( select count(*) from RDX_EASSESSION where RDX_EASSESSION.USERNAME = u.Name AND RDX_EASSESSION.ISINTERACTIVE = 1 AND (RDX_EASSESSION.USERCERTIFICATE is null OR dbms_lob.getlength(RDX_EASSESSION.USERCERTIFICATE)=0) ) < u.SESSIONSLIMIT))"
            + " then 1 else 0 end) ";

    private static final String prepareCreateQryStmtSQL = "select SQN_RDX_EasSessionId.nextval as ID,"
            + "(case when ((sysdate - u.lastpwdchangetime > u.pwdexpirationperiod) or (sysdate - u.lastpwdchangetime > sys.pwdexpirationperiod)) then 1 else u.MUSTCHANGEPWD end) MUSTCHANGEPWD,"
            + " u.INVALIDLOGONCNT, (sysdate - u.lastpwdchangetime) PASSWORD_USE_DAYS, "
            + "(select count(*) from dual where u.LOCKED != 0 or (u.INVALIDLOGONCNT >= sys.BLOCKUSERINVALIDLOGONCNT AND u.INVALIDLOGONTIME + sys.BLOCKUSERINVALIDLOGONMINS/24/60 > sysdate))  USERLOCKED, "
            + "(case when  (u.MUSTCHANGEPWD !=0 and (u.temporaryPwdStartTime is not NULL) and sys.temporaryPwdExpirationPeriod>0 and (u.temporaryPwdStartTime+sys.temporaryPwdExpirationPeriod/24<sysdate)) then 1 else 0 end) TMPPWDEXPIRED,"
            + " u.PWDHASH, "
            + " u.PWDHASHALGO, "
            + "(case when u.AUTHTYPES is NULL or u.AUTHTYPES like ? then 1 else 0 end) AUTH_TYPE_ACCESSIBLE, "
            + "(case when u.AUTHTYPES is NULL or u.AUTHTYPES like '%" + EAuthType.PASSWORD.getValue() + "%' then 1 else 0 end) PASSWORD_AUTH_ENABLED, "
            + "(case when u.AUTHTYPES is NULL or u.AUTHTYPES like '%" + EAuthType.CERTIFICATE.getValue() + "%' then 1 else 0 end) CERTIFICATE_AUTH_ENABLED, "
            + "decode(u.CheckStation, 1, (select count(*) from RDX_USER2STATION u2s where u2s.UserName = u.Name and u2s.StationName = ? and ROWNUM<2), 1) as STOK, "
            + "(case when "
            + "(logonScheduleId is not null AND RDX_JS_IntervalSchedule.isIn(logonScheduleId,sysdate)>0) "
            + "OR (logonScheduleId is null "
            + "   AND (not exists (select NULL from RDX_AC_USER2USERGROUP, RDX_AC_USERGROUP where RDX_AC_USER2USERGROUP.USERNAME=u.Name and RDX_AC_USERGROUP.NAME = RDX_AC_USER2USERGROUP.GROUPNAME)"
            + "        OR exists  (select NULL from RDX_AC_USER2USERGROUP, RDX_AC_USERGROUP where RDX_AC_USER2USERGROUP.USERNAME=u.Name and RDX_AC_USERGROUP.NAME = RDX_AC_USER2USERGROUP.GROUPNAME and (RDX_AC_USERGROUP.LOGONSCHEDULEID is null OR RDX_JS_IntervalSchedule.isIn(RDX_AC_USERGROUP.LOGONSCHEDULEID,sysdate)>0))"
            + "       )"
            + "   )"
            + " then 1 else 0 end) as LOGON_TIME_OK, "
            + checkSessionsLimitSqlExpl+" as SESSIONSLIMIT_OK, "
            + "(case when u.SESSIONSLIMIT = 0 then NVL(sys.LIMITEASSESSIONSPERUSR,0) else NVL(u.SESSIONSLIMIT,0) end) as SESSIONSLIMIT "
            + "from RDX_AC_user u, RDX_SYSTEM sys where u.Name = ? and sys.id = 1";
    private static final Stmt prepareCreateQryStmt = new Stmt(prepareCreateQryStmtSQL, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR);
    private final PreparedStatement prepareCreateQry;   

    private static final String createQryStmtSQL = "insert into RDX_EasSession columns (ID, USERNAME, STATIONNAME, LANGUAGE, COUNTRY, ENVIRONMENT, CHALLENGE, SERVERKEY, CLIENTKEY, USERCERTIFICATE, ISINTERACTIVE, LASTCONNECTTIME) "
            + "values  ( ?,         ?,           ?,        ?,       ?,           ?,         ?,         ?,         ?,               ?,             ?, SYSDATE)";
    private static final Stmt createQryStmt = new Stmt(createQryStmtSQL, Types.BIGINT, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.BLOB, Types.INTEGER);
    private final PreparedStatement createQry;

    private static final String scpQryStmtSQL = "select ScpName from rdx_station where name = ?";
    private static final Stmt scpQryStmt = new Stmt(scpQryStmtSQL, Types.VARCHAR);
    private final PreparedStatement scpQry;

    private static final String masterSessionQryStmtSQL = "select SERVERKEY from RDX_EASSESSION "
            + "where ID = ? AND ISINTERACTIVE = 1 AND (USERCERTIFICATE is null OR dbms_lob.getlength(USERCERTIFICATE)=0) "
            + " AND USERNAME = ? AND STATIONNAME = ?";
    private static final Stmt masterSessionQryStmt = new Stmt(masterSessionQryStmtSQL, Types.BIGINT, Types.VARCHAR, Types.VARCHAR);
    private final PreparedStatement masterSessionQry;
    
    private static final String deletePreviousSessionQryStmtSQL = "delete from RDX_EASSESSION where ID = ? AND USERNAME = ?";
    private static final Stmt deletePreviousSessionQryStmt = new Stmt(deletePreviousSessionQryStmtSQL, Types.BIGINT, Types.VARCHAR);
    private final PreparedStatement deletePreviousSessionQry;
    
    private static final String checkSessionsLimitQryStmtSQL = "select "
            + checkSessionsLimitSqlExpl+" as SESSIONSLIMIT_OK "
            + "from RDX_AC_user u, RDX_SYSTEM sys where u.Name = ? and sys.id = 1";
    private static final Stmt checkSessionsLimitQryStmt = new Stmt(checkSessionsLimitQryStmtSQL, Types.VARCHAR);
    private final PreparedStatement checkSessionsLimitQry;    

    private static final String productInstallationOptionsQryStmtSQL = "Select LAYERURI||'\\'||NAME as name, VALUE as val from RDX_DDSOPTION";
    private static final Stmt productInstallationOptionsQryStmt = new Stmt(productInstallationOptionsQryStmtSQL);
    private final PreparedStatement productInstallationOptionsQry;
            
    private final IDbQueries delegate = new DelegateDbQueries(this, null);

    private CreateSessionRequest() {
        certAttrTypeQry = null;
        prepareCreateQry = null;
        createQry = null;
        scpQry = null;
        masterSessionQry = null;
        deletePreviousSessionQry = null;
        checkSessionsLimitQry = null;
        productInstallationOptionsQry = null;
    }

    CreateSessionRequest(final ExplorerAccessService presenter) {
        super(presenter);
        final java.sql.Connection dbConnection = getArte().getDbConnection().get();
        try (final ArteProfiler.TimingSection section = getArte().getProfiler().startTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE)) {
            certAttrTypeQry = ((RadixConnection) dbConnection).prepareStatement(certAttrTypeQryStmt);
            prepareCreateQry = ((RadixConnection) dbConnection).prepareStatement(prepareCreateQryStmt);
            createQry = ((RadixConnection) dbConnection).prepareStatement(createQryStmt);
            scpQry = ((RadixConnection) dbConnection).prepareStatement(scpQryStmt);
            masterSessionQry = ((RadixConnection) dbConnection).prepareStatement(masterSessionQryStmt);
            deletePreviousSessionQry = ((RadixConnection) dbConnection).prepareStatement(deletePreviousSessionQryStmt);
            checkSessionsLimitQry = ((RadixConnection) dbConnection).prepareStatement(checkSessionsLimitQryStmt);
            productInstallationOptionsQry = ((RadixConnection) dbConnection).prepareStatement(productInstallationOptionsQryStmt);
        } catch (SQLException e) {
            throw new DatabaseError("Can't init EAS service DB query: " + ExceptionTextFormatter.getExceptionMess(e), e);

        }
    }

    private LoginResult login(final String user,
            final String station,
            final byte[] challenge,
            final EAuthType authType,
            final X509Certificate[] clientCertificates,
            final Long replacedSessionId,
            final Long parentSessionId) throws ServiceProcessFault {
        try {
            prepareCreateQry.setString(1, "%" + authType.getValue() + "%");
            prepareCreateQry.setString(2, station);
            prepareCreateQry.setString(3, user);
            try (ResultSet rs = prepareCreateQry.executeQuery()) {
                final boolean userNameIsOk;
                if (rs.next()) {
                    if (rs.getInt("USERLOCKED") > 0) {
                        getArte().getTrace().put(EEventSeverity.WARNING, Messages.MLS_ID_TRY_TO_USE_LOCKED_USER, new ArrStr(user, station, String.valueOf(getArte().getArteSocket().getRemoteAddress())), EEventSource.APP_AUDIT.getValue());
                        throw new ServiceProcessClientFault(ExceptionEnum.USER_ACCOUNT_LOCKED.toString(), user, null, null);
                    }
                    if (rs.getInt("AUTH_TYPE_ACCESSIBLE") == 0) {
                        throw new ServiceProcessClientFault(ExceptionEnum.INVALID_AUTH_TYPE.toString(), authType.getValue() + "\n" + user, null, null);
                    }
                    userNameIsOk = true;
                } else {
                    userNameIsOk = false;
                }
                final boolean passwordExpired;
                if (userNameIsOk) {
                    if (authType == EAuthType.PASSWORD) {
                        final String pwdTockenHex = getPwdToken(challenge, rs.getString("PWDHASHALGO"));
                        presenter.checkPwd(user, rs.getString("PWDHASH"), Hex.encode(challenge), pwdTockenHex, rs.getInt("INVALIDLOGONCNT") > 0);
                        if (rs.getInt("TMPPWDEXPIRED") == 1) {
                            throw EasFaults.newTemporaryPasswordExpiredFault();
                        }
                        passwordExpired
                                = presenter.pwdExpired(rs.getInt("MUSTCHANGEPWD") != 0, rs.getInt("PASSWORD_USE_DAYS"));
                    } else if (clientCertificates != null) {
                        if (rs.getInt("CERTIFICATE_AUTH_ENABLED") == 0) {
                            throw new ServiceProcessClientFault(ExceptionEnum.INVALID_AUTH_TYPE.toString(), authType.getValue() + "\n" + user, null, null);
                        }
                        verifyClientCertificate(clientCertificates);
                        passwordExpired
                                = presenter.pwdExpired(rs.getInt("MUSTCHANGEPWD") != 0, rs.getInt("PASSWORD_USE_DAYS"));
                    } else {
                        passwordExpired = false;
                    }
                } else {
                    throw new ServiceProcessClientFault(ExceptionEnum.INVALID_USER.toString(), user, null, null);
                }
                if (rs.getInt("STOK") == 0) {
                    throw EasFaults.newAccessViolationFault(getArte(), "User \"" + user + "\" can't use station \"" + station + '\"');
                }
                if (rs.getInt("LOGON_TIME_OK") == 0 && clientCertificates == null/*check logon time in login request*/) {
                    throw EasFaults.newLogonTimeRestrictionViolationFault();
                }
                final String masterSessionKey;
                if (parentSessionId == null) {
                    masterSessionKey = null;
                } else {
                    masterSessionKey = verifyMasterSession(parentSessionId, user, station, authType);
                }
                boolean sessionsLimitExceed = rs.getInt("SESSIONSLIMIT_OK") == 0;
                if (replacedSessionId!=null){
                    closeReplacedSession(replacedSessionId, user, authType);
                    if (sessionsLimitExceed){//recheck sessions limit
                        checkSessionsLimitQry.setString(1, user);                        
                        try(final ResultSet qryRs = checkSessionsLimitQry.executeQuery()){
                            if (!qryRs.next()) {
                                throw new ServiceProcessServerFault(ExceptionEnum.SERVER_MALFUNCTION.toString(), "Unable to check sessions limit for user "+user, null, null);
                            }
                            sessionsLimitExceed = qryRs.getInt("SESSIONSLIMIT_OK") == 0;
                        }
                    }
                }
                return new LoginResult(rs.getInt("ID"),
                        passwordExpired,
                        rs.getInt("PASSWORD_AUTH_ENABLED") > 0,
                        sessionsLimitExceed,
                        rs.getInt("SESSIONSLIMIT"),
                        masterSessionKey,
                        rs.getString("PWDHASHALGO"));
            }
        } catch (SQLException e) {
            final String preprocessedExStack = ExceptionTextFormatter.exceptionStackToString(e);
            throw new ServiceProcessServerFault(ExceptionEnum.SERVER_MALFUNCTION.toString(), "Can't read user data: " + ExceptionTextFormatter.getExceptionMess(e), e, preprocessedExStack);
        }
    }

    private String getPwdToken(final byte[] challenge, final String hashAlgo) {
        final GetSecurityTokenDocument doc = GetSecurityTokenDocument.Factory.newInstance();
        final GetSecurityTokenRq request = doc.addNewGetSecurityToken().addNewGetSecurityTokenRq();
        request.setInputToken(challenge);
        request.setHashAlgorithm(HashAlgorithmEnum.Enum.forString(hashAlgo));
        try {
            final GetSecurityTokenMess mess = (GetSecurityTokenMess) getArte().getArteSocket().invokeResource(doc, GetSecurityTokenMess.class, 20);//20 seconds
            return Hex.encode(mess.getGetSecurityTokenRs().getOutToken());
        } catch (ResourceUsageException | ResourceUsageTimeout | InterruptedException exception) {
            throw new ServiceProcessServerFault(ExceptionEnum.NO_CREDENTIALS.toString(), "Can't get client credentials", exception, null);
        }
    }

    private void verifyClientCertificate(final X509Certificate[] certificateChain) {
        final List<String> trustedCertAliases
                = getArte().getArteSocket().getUnit().getTrustedCertAliases();
        final TrustManager[] trustManagers;
        X509TrustManager x509TrustManager = null;
        try {
            trustManagers = CertificateUtils.createServerTrustManagers(trustedCertAliases);
        } catch (CertificateUtilsException exception) {
            throw new ServiceProcessServerFault(ExceptionEnum.INVALID_CERTIFICATE.toString(), "Unable to verify client certifiacate", null, null);
        }
        for (TrustManager trustManager : trustManagers) {
            if (trustManager instanceof X509TrustManager) {
                x509TrustManager = (X509TrustManager) trustManager;
                break;
            }
        }
        if (x509TrustManager == null) {
            throw new ServiceProcessServerFault(ExceptionEnum.INVALID_CERTIFICATE.toString(), "Unable to verify client certifiacate", null, null);
        }
        final PublicKey key = certificateChain[0].getPublicKey();
        final String keyAlgorithm = key.getAlgorithm();
        final String authType;
        switch (keyAlgorithm) {
            case "RSA":
                authType = "RSA";
                break;
            case "DSA":
                authType = "DSA";
                break;
            case "EC":
                authType = "EC";
                break;
            default:
                // unknown public key type
                authType = "UNKNOWN";
                break;
        }
        try {
            x509TrustManager.checkServerTrusted(certificateChain, authType);
        } catch (CertificateException exception) {
            throw new ServiceProcessServerFault(ExceptionEnum.INVALID_CERTIFICATE.toString(), "Untrusted client certifiacate", null, null);
        }
    }

    private String verifyMasterSession(final long id, final String user, final String station, final EAuthType authType) {
        try {
            masterSessionQry.setLong(1, id);
            masterSessionQry.setString(2, user);
            masterSessionQry.setString(3, station);
            try (ResultSet rs = masterSessionQry.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("SERVERKEY");
                } else {
                    throw EasFaults.newSessionDoesNotExist(ExplorerAccessService.getSessionRestorePolicy(getArte(), authType));
                }
            }
        } catch (SQLException e) {
            final String preprocessedExStack = ExceptionTextFormatter.exceptionStackToString(e);
            throw new ServiceProcessServerFault(ExceptionEnum.SERVER_MALFUNCTION.toString(), "Can't verify master session: " + ExceptionTextFormatter.getExceptionMess(e), e, preprocessedExStack);
        }
    }
    
    private void closeReplacedSession(final long sessionId, final String userName, final EAuthType authType){
        try{
            deletePreviousSessionQry.setLong(1, sessionId);
            deletePreviousSessionQry.setString(2, userName);
            if (deletePreviousSessionQry.executeUpdate()!=1){
                final SessionRestorePolicy.Enum restorePolicy = 
                        ExplorerAccessService.getSessionRestorePolicy(getArte(), authType);
                if (restorePolicy==SessionRestorePolicy.PASSWORD_MUST_BE_ENTERED){
                    throw EasFaults.newSessionDoesNotExist(restorePolicy);
                }
            }
        }catch(SQLException e){
            final String preprocessedExStack = ExceptionTextFormatter.exceptionStackToString(e);
            throw new ServiceProcessServerFault(ExceptionEnum.SERVER_MALFUNCTION.toString(), "Failed to delete previous session: " + ExceptionTextFormatter.getExceptionMess(e), e, preprocessedExStack);
        }
    }

    private void create(final long id,
            final String user,
            final String station,
            final String lang,
            final String country,
            final String env,
            final String challenge,
            final String serverKey,
            final String krbClientKey,
            final byte[] userCertificate,
            final boolean isInteractive) throws ServiceProcessServerFault {
        try {
            createQry.setLong(1, id);
            createQry.setString(2, user);
            createQry.setString(3, station);
            createQry.setString(4, lang);
            createQry.setString(5, country);
            createQry.setString(6, env);
            createQry.setString(7, challenge);
            createQry.setString(8, serverKey);
            createQry.setString(9, krbClientKey);
            boolean meansThisIsLogin = false;
            if (userCertificate == null) {
                createQry.setBlob(10, (Blob) null);
                meansThisIsLogin = true;
            } else {
                createQry.setBlob(10, new ByteBufferInputStream(ByteBuffer.wrap(userCertificate)));
            }
            createQry.setInt(11, isInteractive ? 1 : 0);
            if (createQry.executeUpdate() != 1) {
                throw new ServiceProcessServerFault(ExceptionEnum.SERVER_MALFUNCTION.toString(), "Can't register new session", null, null);
            }
            if (isInteractive && meansThisIsLogin) {
                presenter.udpateUserLastLogonTime(user);
            }
        } catch (SQLException e) {
            final String preprocessedExStack = ExceptionTextFormatter.exceptionStackToString(e);
            throw new ServiceProcessServerFault(ExceptionEnum.SERVER_MALFUNCTION.toString(), "Can't register new session: " + ExceptionTextFormatter.getExceptionMess(e), e, preprocessedExStack);
        }
    }

    private String getUserNameFromCertifiacte(final X509Certificate userCertificate, final boolean checkSsl) {//FIXME Возможно, ошибки нужно записывать в трассу с источником App.Audit и не передовать клиенту стек        
        try (ResultSet rs = certAttrTypeQry.executeQuery()) {
            if (rs.next()) {
                final String attrType = rs.getString("CERTATTRFORUSERLOGIN");
                final String dn;
                if (userCertificate == null) {
                    final SSLSession sslSession = getArte().getArteSocket().getSslSession();
                    if (sslSession == null) {
                        throw new ServiceProcessClientFault(ExceptionEnum.INVALID_CERTIFICATE.toString(), "Unable to identify user", null, null);
                    }
                    try {
                        dn = sslSession.getPeerPrincipal().getName();
                    } catch (SSLPeerUnverifiedException ex) {
                        throw new ServiceProcessClientFault(ExceptionEnum.INVALID_CERTIFICATE.toString(), "Unable to identify user by given certificate", null, null);
                    }
                } else {
                    if (checkSsl && getArte().getArteSocket().getSslSession() == null) {
                        throw new ServiceProcessClientFault(ExceptionEnum.INVALID_CERTIFICATE.toString(), "Unable to identify user", null, null);
                    }
                    dn = userCertificate.getSubjectDN().getName();
                }
                return CertificateUtils.parseDistinguishedName(dn).get(attrType == null || attrType.isEmpty() ? ELdapX500AttrType.COMMON_NAME.getValue() : attrType);
            } else {
                throw new ServiceProcessServerFault(ExceptionEnum.SERVER_MALFUNCTION.toString(), "Can't read data from rdx_system table: record with id=1 was not found", null, null);
            }
        } catch (SQLException e) {
            final String preprocessedExStack = ExceptionTextFormatter.exceptionStackToString(e);
            throw new ServiceProcessServerFault(ExceptionEnum.SERVER_MALFUNCTION.toString(), "Can't read data from rdx_system table: " + ExceptionTextFormatter.getExceptionMess(e), e, preprocessedExStack);
        }
    }

    public final CreateSessionDocument process(final CreateSessionMess request) throws ServiceProcessFault, InterruptedException {
        final CreateSessionRq rqParams = request.getCreateSessionRq();
        final String stationName = rqParams.getStationName();
        final GSSHandshaker.HandshakeResult gssHandshakeResult;
        final String userName;
        final EAuthType authType = rqParams.getAuthType();
        if (authType == null) {
            throw EasFaults.newParamRequiedFault("AuthType", rqParams.getDomNode().getNodeName());
        }
        final X509Certificate[] userCertificatesChain;
        byte[] userCertificate = null;
        final CreateSessionRq.UserCertificatesChain certificatesChain = rqParams.getUserCertificatesChain();
        if (certificatesChain != null && certificatesChain.sizeOfItemArray() > 0) {
            final int certCount = certificatesChain.sizeOfItemArray();
            userCertificatesChain = new X509Certificate[certCount];
            byte[] certificate;
            try {
                for (int i = 0; i < certCount; i++) {
                    certificate = certificatesChain.getItemArray(i);
                    if (i == 0) {
                        userCertificate = certificate;
                    }
                    userCertificatesChain[i]
                            = (X509Certificate) CertificateUtils.readCertificate("X.509", certificate);
                }
            } catch (CertificateException exception) {
                throw new ServiceProcessClientFault(ExceptionEnum.INVALID_CERTIFICATE.toString(), "Unable to read client certificate", exception, null);
            }
        } else {
            userCertificatesChain = null;
        }
        switch (authType) {
            case KERBEROS:
                try {
                    if (getArte().getArteSocket().getUnit().getEasKerberosAuthPolicy() == EClientAuthentication.None
                            || getArte().getInstance().getKerberosServiceCredentials() == null) {
                        presenter.throwKerberosDisabled(stationName);
                    }
                    gssHandshakeResult
                            = GSSHandshaker.getInstance().doHandshake(getArte(),
                                    getArte().getInstance().getKerberosServiceCredentials(),
                                    rqParams.getKrbInitialToken(),
                                    stationName);
                    if (userCertificatesChain == null) {
                        userName = gssHandshakeResult.getInitiatorName();
                    } else {
                        userName = getUserNameFromCertifiacte(userCertificatesChain[0], false);
                    }
                } catch (KerberosException e) {
                    throw presenter.kerberosExceptionToFault(e, stationName);
                }
                break;
            case CERTIFICATE:
                userName = getUserNameFromCertifiacte(userCertificatesChain == null ? null : userCertificatesChain[0], true);
                gssHandshakeResult = null;
                break;
            default:
                userName = rqParams.getUser();
                gssHandshakeResult = null;
        }
        try {
            
            final RevisionMeta curRevMeta = getArte().getDefManager().getReleaseCache().getRelease().getRevisionMeta();
            
            EIsoLanguage clientLanguage = getArte().getServerLanguage();

            final Long easSessionId;

            if (rqParams.getPlatform() == null) {
                throw EasFaults.newParamRequiedFault("Platform", rqParams.getDomNode().getNodeName());
            }

            if (!Utils.equals(rqParams.getPlatform().getTopLayerUri(), getTopLayerUri())) {
                throw new ServiceProcessClientFault(ExceptionEnum.INVALID_PLATFORM_VERSION.toString(), "Server's top layer URIs: " + String.valueOf(getTopLayerUri()) + "\nClient's top layer URIs: " + String.valueOf(rqParams.getPlatform().getTopLayerUri()), null, null);
            }

            if (SrvRunParams.getIsEasVerChecksOn()) {
                if (rqParams.getPlatform().getKernelVersion() != null && rqParams.getPlatform().getAppVersion() != null) {
                    if (!rqParams.getPlatform().getKernelVersion().equals(curRevMeta.getKernelLayerVersionsString())) {
                        throw new ServiceProcessClientFault(ExceptionEnum.INVALID_PLATFORM_VERSION.toString(), "Server's kernel version: " + curRevMeta.getKernelLayerVersionsString() + "\nClient's kernel version: " + rqParams.getPlatform().getKernelVersion(), null, null);
                    }
                    if (!rqParams.getPlatform().getAppVersion().equals(curRevMeta.getAppLayerVersionsString())) {
                        throw new ServiceProcessClientFault(ExceptionEnum.INVALID_PLATFORM_VERSION.toString(), "Server's application version: " + curRevMeta.getAppLayerVersionsString() + "\nClient's application version: " + rqParams.getPlatform().getAppVersion(), null, null);
                    }
                } else if (!isCompatibleUri(rqParams.getPlatform().getRepositoryUri())) {
                    throw new ServiceProcessClientFault(ExceptionEnum.INVALID_PLATFORM_VERSION.toString(), "Server's repository URI: " + RadixLoader.getInstance().getRepositoryUri() + "\nClient's repository URI: " + String.valueOf(rqParams.getPlatform().getRepositoryUri()), null, null);
                }
            }

            if (SrvRunParams.getIsEasVerChecksOn()) {
                final long explorerDefVer = rqParams.getPlatform().getDefinitionsVer();
                if (explorerDefVer > curRevMeta.getNum()) {//should not happen because now request will be dumped earlier in ARTE unit (Server.Busy fault will be sent)
                    throw new ServiceProcessClientFault(ExceptionEnum.INVALID_PLATFORM_VERSION.toString(), "Server's kernel definitions version: " + String.valueOf(curRevMeta.getNum()) + "\nClient's kernel definitions version: " + String.valueOf(explorerDefVer), null, null);
                }
                if (explorerDefVer != curRevMeta.getNum()) {
                    throw new ServiceProcessClientFault(
                            ExceptionEnum.INVALID_DEFINITION_VERSION.toString(),
                            String.valueOf(curRevMeta.getNum()), null, null);
                }
            }
            
            if (!SrvRunParams.getAllowWebDriverForEas()
                && rqParams.isSetWebDriverEnabled()
                && rqParams.getWebDriverEnabled()){
                throw new ServiceProcessClientFault(
                        ExceptionEnum.WEB_DRIVER_IS_NOT_ALLOWED.toString(),null, null, null);
            }

            if (rqParams.isSetLanguage()) {
                try {
                    clientLanguage = rqParams.getLanguage();
                } catch (NoConstItemWithSuchValueError e) {
                    //clientLanguage is already equal serverLanguage;
                }
            }

            EIsoCountry clientCountry = null;
            if (rqParams.isSetCountry()) {
                try {
                    clientCountry = rqParams.getCountry();
                } catch (NoConstItemWithSuchValueError error) {
                    clientCountry = null; //use systemCountry
                }
            }

            ERuntimeEnvironmentType clientEnvironment = null;
            if (rqParams.isSetEnvironment()) {
                try {
                    clientEnvironment = rqParams.getEnvironment();
                } catch (NoConstItemWithSuchValueError error) {
                    clientEnvironment = null;
                }
            }
            final Long parentSessionId;
            if (request.getCreateSessionRq().isSetParentSessionId()) {
                parentSessionId = request.getCreateSessionRq().getParentSessionId();
            } else {
                parentSessionId = null;
            }
            final Long replacedSessionId;
            if (request.getCreateSessionRq().isSetReplacedSessionId()){
                replacedSessionId = request.getCreateSessionRq().getReplacedSessionId();
            }else{
                replacedSessionId = null;
            }
            final LoginResult loginResult = login(userName,
                    stationName,
                    presenter.getChallenge(request, 0, false, null),
                    authType,
                    userCertificatesChain,
                    replacedSessionId,
                    parentSessionId
            );
            easSessionId = (long) loginResult.sessionId;
            final ArteTransactionParams transactionParams
                    = new ArteTransactionParams(getArte().getActualVersion(), easSessionId, null, userName, stationName, clientLanguage, clientCountry, clientEnvironment, null, null, null);
            getArte().startTransaction(transactionParams);
            getArte().onFinishRequestExecution();// перерегистрируем RequestExecution, так как без startTran регистрация прошла не до конца
            getArte().onStartRequestExecution(Arte.genUserRequestExecutorId(getArte().getUserName()));

            final CreateSessionDocument res = CreateSessionDocument.Factory.newInstance();
            final CreateSessionRs rsStruct = res.addNewCreateSession().addNewCreateSessionRs();
            rsStruct.setSessionId(easSessionId.longValue());
            rsStruct.setUserDefVer(getArte().getVersion().longValue());
            rsStruct.setUser(userName);
            if (loginResult.canChangePassword) {
                rsStruct.setHashAlgorithm(HashAlgorithmEnum.Enum.forString(loginResult.pwdHashAlgo));
            }
            final ExplorerRoots rootsXml = rsStruct.addNewExplorerRoots();
            final Id requestedExplorerRootId = rqParams.isSetExplorerRoot() ? rqParams.getExplorerRoot().getId() : null;
            int rootCount = 0;
            RadExplorerRootDef rootDef = null;
            for (RadExplorerRootDef lookup : getArte().getDefManager().getAccessibleExplorerRootDefs()) {
                rootCount++;
                if (lookup.getCurUserCanAccess()) {
                    final ExplorerRoots.Item root = rootsXml.addNewItem();
                    root.setId(lookup.getId());
                    root.setTitle(lookup.getTitle(getArte()));
                    root.setHidden(lookup.isHidden());
                    if (lookup.getId().equals(requestedExplorerRootId)) {
                        writeVisibleExplorerItems(root, lookup);
                    }
                    if (!lookup.isHidden()) {
                        rootDef = lookup;
                    }
                }
            }
            if (rootCount == 0) {//RADIX-2987
                throw new ServiceProcessServerFault(ExceptionEnum.DEFINITION_NOT_FOUND.toString(), "ExplorerRoots are not defined", null, null);
            }
            if (rootsXml.getItemList() != null
                    && rootsXml.getItemList().size() == 1
                    && !rootsXml.getItemList().get(0).isSetVisibleExplorerItems()
                    && rootDef != null) {
                writeVisibleExplorerItems(rootsXml.getItemList().get(0), rootDef);
            }
            fillInvalidRolesWarnings(rsStruct);
            final ServerResources srvResourcesXml = rsStruct.addNewServerResources();
            ServerResources.Item srvResXml;
            for (EDrcServerResource srvRes : EDrcServerResource.values()) {
                if (getArte().getRights().getCurUserCanAccess(srvRes)) {
                    srvResXml = srvResourcesXml.addNewItem();
                    srvResXml.setId(Id.Factory.loadFrom(srvRes.getValue()));
                    srvResXml.setTitle(srvRes.getName());
                } else if (srvRes == EDrcServerResource.EAS) {
                    throw EasFaults.newAccessViolationFault(getArte(), Messages.MLS_ID_INSUF_PRIV_TO_CONNECT_TO_EAS, null);
                }
            }
            final ContextlessCommands contextlessCmdsXml = rsStruct.addNewContextlessCommands();
            for (RadContextlessCommandDef cmd : getArte().getDefManager().getContextlessCommandDefs()) {
                if (cmd.getCurUserCanAccess(getArte())) {
                    contextlessCmdsXml.addNewItem().setId(cmd.getId());
                }
            }
            ResultSet rs = null;
            try {
                scpQry.setString(1, stationName);
                rs = scpQry.executeQuery();
                final String scp = rs.next() ? rs.getString(1) : null;
                if (scp != null) {
                    rsStruct.setScpName(scp);
                }
            } catch (SQLException e) {
                final String preprocessedExStack = getArte().getTrace().exceptionStackToString(e);
                throw new ServiceProcessServerFault(ExceptionEnum.SERVER_MALFUNCTION.toString(), "Can't determine station's SCP: " + ExceptionTextFormatter.getExceptionMess(e), e, preprocessedExStack);
            } finally {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException e) {
                        getArte().getTrace().put(EEventSeverity.WARNING, "Can't close ResultSet: " + getArte().getTrace().exceptionStackToString(e), EEventSource.APP_DB);
                    }
                }
            }

            if (parentSessionId == null && userCertificate == null /*check number of sessions in login request when user cert defined*/) {
                final List<String> encryptedSessionIds;
                if (rqParams.getSessionsToTerminate() != null
                        && rqParams.getSessionsToTerminate().getEncryptedSessionIdList() != null) {
                    encryptedSessionIds = rqParams.getSessionsToTerminate().getEncryptedSessionIdList();
                } else {
                    encryptedSessionIds = Collections.emptyList();
                }

                final int maxSessionsNumber = loginResult.sessionsLimitExceed ? loginResult.maxSessionsNumber : -1;

                if (maxSessionsNumber > 0 || !encryptedSessionIds.isEmpty()) {
                    presenter.checkNumberOfUserSessions(userName, maxSessionsNumber, encryptedSessionIds);
                }
            }

            final byte[] challenge = presenter.getChallenge(request, loginResult.sessionId, true, null);

            final String krbClientKey;
            final String serverKeyAsStr;
            final byte[] serverKey = new byte[16];
            if (parentSessionId == null || userCertificate == null || loginResult.masterSessionKey == null || loginResult.masterSessionKey.isEmpty()) {
                new SecureRandom().nextBytes(serverKey);
                serverKeyAsStr = Hex.encode(serverKey);
            } else {
                serverKeyAsStr = loginResult.masterSessionKey;
            }

            if (authType == EAuthType.KERBEROS && gssHandshakeResult != null) {
                if (userCertificate == null) {
                    try {
                        rsStruct.setEncKey(gssHandshakeResult.getGSSContext().wrap(serverKey, 0, serverKey.length, new MessageProp(true)));
                    } catch (Exception ex) {
                        throw presenter.kerberosExceptionToFault(new KerberosException(ex), stationName);
                    }
                }
                krbClientKey = gssHandshakeResult.getClientKey();
            } else {
                krbClientKey = null;
            }

            final String clientEnvironmentAsStr = clientEnvironment == null ? null : clientEnvironment.getValue();
            create(loginResult.sessionId,
                    userName,
                    stationName,
                    clientLanguage.getValue(),
                    clientCountry == null ? null : clientCountry.getAlpha2Code(),
                    clientEnvironmentAsStr,
                    Hex.encode(challenge),
                    serverKeyAsStr,
                    krbClientKey,
                    userCertificate,
                    parentSessionId == null);
            final ArrStr loginTraceDetails = new ArrStr(userName, stationName, String.valueOf(getArte().getArteSocket().getRemoteAddress()));
            switch (authType) {
                case CERTIFICATE:
                    getArte().getTrace().put(EEventSeverity.EVENT, Messages.MLS_ID_USERLOGGED_IN_CERTIFICATE, loginTraceDetails, EEventSource.APP_AUDIT.getValue());
                    break;
                case KERBEROS:
                    getArte().getTrace().put(EEventSeverity.EVENT, Messages.MLS_ID_USERLOGGED_IN_KERBEROS, loginTraceDetails, EEventSource.APP_AUDIT.getValue());
                    break;
                default:
                    getArte().getTrace().put(EEventSeverity.EVENT, Messages.MLS_ID_USERLOGGED_IN_PASSWORD, loginTraceDetails, EEventSource.APP_AUDIT.getValue());
            }
            if (!loginResult.canChangePassword) {
                rsStruct.setCanChangePassword(false);
            } else if (loginResult.passwordExpired) {
                rsStruct.setShouldChangePassword(true);
                ExplorerAccessService.writePasswordRequirements(getArte(), rsStruct.addNewPasswordRequirements());
            }
            if (userCertificate != null) {
                rsStruct.setCanLoginWithPassword(loginResult.canChangePassword);
            }
            rsStruct.setChallenge(challenge);
            writeSrvTimeZoneInfo(rsStruct.addNewServerTimeZone(), getArte().getClientLocale());
            writeProductInstallationOptions(rsStruct.addNewProductInstallationOptions());
            return res;
        } catch (ServiceProcessClientFault flt) {
            if (ExceptionEnum.INVALID_AUTH_TYPE.toString().equals(flt.reason)) {
                getArte().getTrace().put(EEventSeverity.WARNING, Messages.MLS_ID_TRY_TO_USE_FORBIDDEN_AUTH_TYPE, new ArrStr(userName, stationName, authType.getValue()), EEventSource.APP_AUDIT.getValue());
                throw flt;
            }
            final ArrStr loginTraceDetails = new ArrStr(userName, stationName, String.valueOf(getArte().getArteSocket().getRemoteAddress()));
            final String reasonDescription = Messages.getReasonForException(getArte(), flt.reason);
            if (reasonDescription != null) {
                loginTraceDetails.add(reasonDescription);
                getArte().getTrace().put(EEventSeverity.EVENT, Messages.MLS_ID_USER_FAILED_TO_OPEN_SESSION_WITH_REASON, loginTraceDetails, EEventSource.APP_AUDIT.getValue());
            } else {
                getArte().getTrace().put(EEventSeverity.EVENT, Messages.MLS_ID_USER_FAILED_TO_OPEN_SESSION, loginTraceDetails, EEventSource.APP_AUDIT.getValue());
            }
            if ((authType == EAuthType.PASSWORD || authType == EAuthType.CERTIFICATE)
                    && (ExceptionEnum.INVALID_USER.toString().equals(flt.reason)
                    || ExceptionEnum.INVALID_PASSWORD.toString().equals(flt.reason))) {
                throw new ServiceProcessClientFault(ExceptionEnum.INVALID_CREDENTIALS.toString(), null, null, null);
            }
            throw flt;
        } finally {
            if (gssHandshakeResult != null) {
                try {
                    gssHandshakeResult.getGSSContext().dispose();
                } catch (GSSException exception) {
                    //ignore
                }
            }
        }
    }

    @Override
    XmlObject process(final XmlObject rq) throws ServiceProcessFault, InterruptedException {
        CreateSessionDocument doc = null;
        try{
            doc = process((CreateSessionMess) rq);
        }finally{
            postProcess(rq, doc==null ? null : doc.getCreateSession().getCreateSessionRs());
        }
        return doc;
    }

    private String trimLastSlash(final String str) {
        if (str == null) {
            return null;
        }
        if (str.endsWith("/")) {
            return str.substring(0, str.length() - 1);
        }
        return str;
    }

    private boolean isCompatibleUri(String uri) {
        if (uri == null) {
            return false;
        }
        uri = trimLastSlash(uri);
        final String thisUri = trimLastSlash(RadixLoader.getInstance().getRepositoryUri());
        if (Objects.equals(uri, thisUri)) {
            return true;
        }

        final List<String> compatibleRoots = new ArrayList<>();
        if (RadixLoader.getInstance() instanceof RadixSVNLoader) {
            final RadixSVNLoader svnLoader = (RadixSVNLoader) RadixLoader.getInstance();
            //special case -- remote uri is the same as one of explicitly provided alternative uris
            for (String alternativeUri : svnLoader.getSvnUrls()) {
                if (Objects.equals(uri, trimLastSlash(alternativeUri))) {
                    return true;
                }
            }
            compatibleRoots.addAll(((RadixSVNLoader) RadixLoader.getInstance()).getReplicationUrls());
        }
        String thisLocalPath = null;
        String otherLocalPath = null;
        for (String compatibleRoot : compatibleRoots) {
            compatibleRoot = trimLastSlash(compatibleRoot);
            if (thisLocalPath == null && thisUri.startsWith(compatibleRoot)) {
                thisLocalPath = thisUri.substring(compatibleRoot.length());
            }
            if (otherLocalPath == null && uri.startsWith(compatibleRoot)) {
                otherLocalPath = uri.substring(compatibleRoot.length());
            }
        }
        if (thisLocalPath != null && otherLocalPath != null && thisLocalPath.equals(otherLocalPath)) {
            return true;
        }
        return false;
    }

    private String getTopLayerUri() {
        return RadixLoader.getInstance().getTopLayerUrisAsString();
    }

    @Override
    protected String getUsrDbTraceProfile() {
        return null;
    }

    private void writeVisibleExplorerItems(final Item root, final RadExplorerRootDef rootDef) {
        final List<Id> explorerItems = this.getAccessibleExplorerItems(rootDef);
        final CreateSessionRs.ExplorerRoots.Item.VisibleExplorerItems visibleItems
                = root.addNewVisibleExplorerItems();
        for (Id explorerItemId : explorerItems) {
            visibleItems.addNewItem().setId(explorerItemId);
        }
    }

    private void writeSrvTimeZoneInfo(final org.radixware.schemas.eas.TimeZone timeZoneInfo, final Locale locale) {
        final TimeZone timeZone = TimeZone.getDefault();
        timeZoneInfo.setId(timeZone.getID());
        timeZoneInfo.setLongNameInStdTime(timeZone.getDisplayName(false, TimeZone.LONG, locale));
        timeZoneInfo.setLongNameInDlSavingTime(timeZone.getDisplayName(true, TimeZone.LONG, locale));
        timeZoneInfo.setShortNameInStdTime(timeZone.getDisplayName(false, TimeZone.SHORT, locale));
        timeZoneInfo.setShortNameInDlSavingTime(timeZone.getDisplayName(true, TimeZone.SHORT, locale));
        final Calendar c = Calendar.getInstance();
        timeZoneInfo.setOffsetMills(c.get(Calendar.ZONE_OFFSET));
        timeZoneInfo.setDstOffsetMills(c.get(Calendar.DST_OFFSET));
        timeZoneInfo.setTimestamp(new Timestamp(c.getTimeInMillis()));
    }
    
    private void writeProductInstallationOptions(final MapStrStr xmlMap){
        try (ResultSet rs = productInstallationOptionsQry.executeQuery()) {
            final boolean userNameIsOk;
            MapStrStr.Entry xmlMapEntry;
            while (rs.next()) {
                xmlMapEntry = xmlMap.addNewEntry();
                xmlMapEntry.setKey(rs.getString("name"));
                xmlMapEntry.setValue(rs.getString("val"));
            }
        }catch (SQLException e) {
            final String preprocessedExStack = ExceptionTextFormatter.exceptionStackToString(e);
            throw new ServiceProcessServerFault(ExceptionEnum.SERVER_MALFUNCTION.toString(), "Failed to read product installation options: " + ExceptionTextFormatter.getExceptionMess(e), e, preprocessedExStack);
        }
    }
    
    private void fillInvalidRolesWarnings(final CreateSessionRs rs) {
        final List<Id> userAllRoles = getCurUserAllRolesInAllAreas();
        final StringBuilder sb = new StringBuilder();
        for (Id roleId : userAllRoles) {
            final RoleLoadError error = getArte().getDefManager().getRoleLoadError(roleId);
            if (error != null) {
                final String details = error.getCause() == null ? null : ExceptionTextFormatter.throwableToString(error.getCause());
                final ArrStr words = new ArrStr(roleId.toString(), error.getAppRoleInfo() == null ? "" : error.getAppRoleInfo().name, details);
                sb.append(TraceItem.getMess(getArte().getMlsProcessor(), Messages.MLS_ID_UNABLE_TO_LOAD_ROLE, getArte().getClientLanguage(), words)).append("\n\n");
                getArte().getTrace().put(Messages.MLS_ID_UNABLE_TO_LOAD_ROLE, words);
            }
        }
        if (sb.length() > 0) {
            final Warnings.Warning xWarn = rs.ensureWarnings().addNewWarning();
            xWarn.setMessage(TraceItem.getMess(getArte().getMlsProcessor(), Messages.MLS_ID_UNABLE_TO_LOAD_ALL_ROLES, getArte().getClientLanguage(), null));
            xWarn.setDetails(sb.toString());
        }

    }
}
