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

import java.io.UnsupportedEncodingException;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.xmlbeans.XmlObject;

import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.arte.services.Service;
import org.radixware.kernel.common.exceptions.ServiceProcessClientFault;
import org.radixware.kernel.common.exceptions.ServiceProcessFault;
import org.radixware.kernel.common.exceptions.ServiceProcessServerFault;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.EDrcServerResource;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.server.exceptions.DatabaseError;
import org.radixware.schemas.easWsdl.ListVisibleExplorerItemsDocument;
import org.radixware.schemas.easWsdl.ListEdPresVisibleExpItemsDocument;
import org.radixware.schemas.easWsdl.CommandDocument;
import org.radixware.schemas.easWsdl.ContextlessCommandDocument;
import org.radixware.schemas.easWsdl.CreateDocument;
import org.radixware.schemas.easWsdl.DeleteDocument;
import org.radixware.schemas.easWsdl.GetObjectTitlesDocument;
import org.radixware.schemas.easWsdl.ListInstantiatableClassesDocument;
import org.radixware.schemas.easWsdl.PrepareCreateDocument;
import org.radixware.schemas.easWsdl.ReadDocument;
import org.radixware.schemas.easWsdl.SelectDocument;
import org.radixware.schemas.easWsdl.SetParentDocument;
import org.radixware.schemas.easWsdl.UpdateDocument;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import org.radixware.kernel.common.auth.AuthUtils;
import org.radixware.kernel.common.enums.EAuthType;
import org.radixware.kernel.common.enums.EChannelType;
import org.radixware.kernel.common.enums.EClientAuthentication;
import org.radixware.kernel.common.enums.EIsoCountry;
import org.radixware.kernel.common.enums.EPortSecurityProtocol;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EServiceAccessibility;
import org.radixware.kernel.common.enums.ETimingSection;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.kerberos.KerberosException;
import org.radixware.kernel.common.ssl.CertificateUtils;
import org.radixware.kernel.common.trace.TraceItem;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.types.MultilingualString;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.Hex;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.server.instance.RadixLoaderActualizer;
import org.radixware.kernel.server.aio.ServiceManifestServerLoader;
import org.radixware.kernel.server.arte.ArteProfiler;
import org.radixware.kernel.server.arte.ArteSocket;
import org.radixware.kernel.server.arte.ArteTransactionParams;
import org.radixware.kernel.server.arte.Cache;
import org.radixware.kernel.server.instance.aadc.AadcManager;
import org.radixware.kernel.server.jdbc.DelegateDbQueries;
import org.radixware.kernel.server.jdbc.Stmt;
import org.radixware.kernel.server.jdbc.IDbQueries;
import org.radixware.kernel.server.jdbc.RadixConnection;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.utils.SrvValAsStr;
import org.radixware.schemas.eas.*;
import org.radixware.schemas.easWsdl.CalcSelectionStatisticDocument;
import org.radixware.schemas.easWsdl.GetDatabaseInfoDocument;

public final class ExplorerAccessService extends Service {
//Service URI

    public static final String SERVICE_WSDL = "http://schemas.radixware.org/eas.wsdl";
    private static final String PWD_EXPIRATION_PERIOD_OPTION = "pwdExpirationPeriod";

     //autonomous transaction is used to prevent deadlock on unregisterSession
    private static final String qryUpdateChallengeStmtSQL = "declare pragma autonomous_transaction; "
                                                          + "begin update RDX_EasSession set challenge = ?, lastconnecttime = SYSDATE where id = ? and (challenge = ? or ? is NULL); commit; end;";
    private static final Stmt qryUpdateChallengeStmt = new Stmt(qryUpdateChallengeStmtSQL,Types.VARCHAR,Types.BIGINT,Types.VARCHAR,Types.VARCHAR);
    private final PreparedStatement qryUpdateChallenge;

    private static final String qrySessionQryStmtSQL = "select ses.USERNAME,usr.PWDHASH,usr.dbTraceProfile usrDbTraceProfile,usr.traceGuiActions traceUsrActions, "
                                                    + "(case when usr.AUTHTYPES is NULL or usr.AUTHTYPES like ? then 1 else 0 end) AUTH_TYPE_ACCESSIBLE, "
                                                    + "(case when ((sysdate - usr.lastpwdchangetime > usr.pwdexpirationperiod) or (sysdate - usr.lastpwdchangetime > sys.pwdexpirationperiod)) then 1 else usr.MUSTCHANGEPWD end) MUSTCHANGEPWD,"
                                                    + "(case when  (usr.MUSTCHANGEPWD!=0 and (usr.temporaryPwdStartTime is not NULL) and sys.temporaryPwdExpirationPeriod>0 and (usr.temporaryPwdStartTime+sys.temporaryPwdExpirationPeriod/24<sysdate)) then 1 else 0 end) TMPPWDEXPIRED,"
                                                    + " usr.INVALIDLOGONCNT, (sysdate - usr.lastpwdchangetime) PASSWORD_USE_DAYS, "
                                                    + "(select count(*) from dual where usr.LOCKED != 0 or (usr.INVALIDLOGONCNT >= sys.BLOCKUSERINVALIDLOGONCNT AND usr.INVALIDLOGONTIME + sys.BLOCKUSERINVALIDLOGONMINS/24/60 > sysdate))  USERLOCKED, "
                                                    + "ses.STATIONNAME, ses.LANGUAGE, ses.COUNTRY,"
                                                    + "ses.CHALLENGE, ses.SERVERKEY, ses.CLIENTKEY, st.SCPNAME, ses.ENVIRONMENT, ses.ISINTERACTIVE,"
                                                    + "(case when ses.USERCERTIFICATE is not NULL and dbms_lob.getlength(ses.USERCERTIFICATE)>0 then 1 else 0 end) NEED_FOR_LOGIN,"
                                                    + "(case when ses.USERCERTIFICATE is NULL then 1 else 0 end) CHECK_CERTIFICATE,"
                                                    + "sys.CERTATTRFORUSERLOGIN, "
                                                    + "(case when "
                                                    + "(logonScheduleId is not null AND RDX_JS_IntervalSchedule.isIn(logonScheduleId,sysdate)>0) "
                                                    + "OR (logonScheduleId is null "
                                                    + "   AND (not exists (select NULL from RDX_AC_USER2USERGROUP, RDX_AC_USERGROUP where RDX_AC_USER2USERGROUP.USERNAME=usr.name and RDX_AC_USERGROUP.NAME = RDX_AC_USER2USERGROUP.GROUPNAME)"
                                                    + "        OR exists (select NULL from RDX_AC_USER2USERGROUP, RDX_AC_USERGROUP where RDX_AC_USER2USERGROUP.USERNAME=usr.name and RDX_AC_USERGROUP.NAME = RDX_AC_USER2USERGROUP.GROUPNAME and (RDX_AC_USERGROUP.LOGONSCHEDULEID is null OR RDX_JS_IntervalSchedule.isIn(RDX_AC_USERGROUP.LOGONSCHEDULEID,sysdate)>0))"
                                                    + "       )"
                                                    + "   )"
                                                    + "then 1 else 0 end) as LOGON_TIME_OK "
                                                    + "from RDX_EasSession ses left outer join RDX_STATION st on ses.stationname = st.name "
                                                    + "left outer join RDX_AC_USER usr on usr.name = ses.username, RDX_SYSTEM sys "
                                                    + "where ses.id = ? and sys.id = 1";
    private static final Stmt qrySessionQryStmt = new Stmt(qrySessionQryStmtSQL,Types.VARCHAR,Types.BIGINT);    
    private final PreparedStatement qrySessionQry;

    private static final String qryScpSapsStmtSQL = "select "
                                                    + "sap.id, sap.name, nvl(s2s.extAddress, sap.address) address, sap.securityProtocol, sap.channelType, "
                                                    + "sap.selfCheckTime, "
                                                    + "sap.selfCheckTimeMillis, "
                                                    + "RDX_Utils.getUnixEpochMillis() dbCurMillis, "
                                                    + "s2s.sapPriority, s2s.blockingPeriod, s2s.connectTimeout, instance.aadcMemberId "
                                                    + "from "
                                                    + "rdx_scp2sap s2s, rdx_sap sap, rdx_instance instance "
                                                    + "where "
                                                    + "sap.systemId=1 and "
                                                    + "sap.uri='" + ExplorerAccessService.SERVICE_WSDL + "' and "
                                                    + "(? is NULL or (?='" + EAuthType.PASSWORD.getValue() + "' and (sap.securityProtocol=" + EPortSecurityProtocol.NONE.getValue().intValue() + " or sap.checkClientCert!=" + EClientAuthentication.Required.getValue().intValue() + ")) "
                                                    + "or (?='" + EAuthType.CERTIFICATE.getValue() + "' and sap.securityProtocol != " + EPortSecurityProtocol.NONE.getValue().intValue() + " and sap.checkClientCert!=" + EClientAuthentication.None.getValue().intValue() + ") "
                                                    + "or (?='" + EAuthType.KERBEROS.getValue() + "' and (sap.securityProtocol=" + EPortSecurityProtocol.NONE.getValue().intValue() + " or sap.checkClientCert!=" + EClientAuthentication.Required.getValue().intValue() + ") and sap.easKrbAuth=" + EClientAuthentication.Enabled.getValue().intValue() + ")) and "
                                                    + "s2s.systemId=1 and s2s.sapId=sap.id and "
                                                    + "sap.isActive = 1 "
                                                    + "and instance.id = sap.systemInstanceId "
                                                    + "and s2s.scpName=?";
    private static final Stmt qryScpSapsStmt = new Stmt(qryScpSapsStmtSQL,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR);
    private final PreparedStatement qryScpSaps;

    private static final String qryAllSapsStmtSQL = "select "
                                                    + "sap.id, sap.name, sap.address, sap.securityProtocol, sap.channelType, instance.aadcMemberId, "
                                                    + "sap.selfCheckTime, "
                                                    + "sap.selfCheckTimeMillis, "
                                                    + "RDX_Utils.getUnixEpochMillis() dbCurMillis "
                                                    + "from "
                                                    + "rdx_sap sap, rdx_instance instance "
                                                    + "where "
                                                    + "sap.systemId=1 and "
                                                    + "sap.uri='" + ExplorerAccessService.SERVICE_WSDL + "' and "
                                                    + "(? is NULL or (?='" + EAuthType.PASSWORD.getValue() + "' and (sap.securityProtocol=" + EPortSecurityProtocol.NONE.getValue().intValue() + " or sap.checkClientCert!=" + EClientAuthentication.Required.getValue().intValue() + ")) "
                                                    + "or (?='" + EAuthType.CERTIFICATE.getValue() + "' and sap.securityProtocol != " + EPortSecurityProtocol.NONE.getValue().intValue() + " and sap.checkClientCert!=" + EClientAuthentication.None.getValue().intValue() + ") "
                                                    + "or (?='" + EAuthType.KERBEROS.getValue() + "' and (sap.securityProtocol=" + EPortSecurityProtocol.NONE.getValue().intValue() + " or sap.checkClientCert!=" + EClientAuthentication.Required.getValue().intValue() + ") and sap.easKrbAuth=" + EClientAuthentication.Enabled.getValue().intValue() + ")) and "
                                                    + "sap.isActive = 1 "
                                                    + "and instance.id = sap.systemInstanceId "
                                                    + "and sap.accessibility != '" + EServiceAccessibility.INTRA_SYSTEM.getValue().toString() + "'";
    private static final Stmt qryAllSapsStmt = new Stmt(qryAllSapsStmtSQL,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR);
    private final PreparedStatement qryAllSaps;

    private static final String qryScpByStationStmtSQL = "select scpname from rdx_station where name = ?";
    private static final Stmt qryScpByStationStmt = new Stmt(qryScpByStationStmtSQL,Types.VARCHAR);
    private final PreparedStatement qryScpByStation;

    private static final String qryListUsrSessionsStmtSQL = "select ID, STATIONNAME, ENVIRONMENT, CREATIONTIME, (sysdate - LASTCONNECTTIME) * 60 * 60 * 24 as IDLETIME, SERVERKEY "
                                                            + "from RDX_EASSESSION ses "
                                                            + "where ses.USERNAME = ? AND ses.ISINTERACTIVE = 1 "
                                                            + "AND (ses.USERCERTIFICATE is null or dbms_lob.getlength(ses.USERCERTIFICATE)=0) "//exclude incomplete login
                                                            + "order by CREATIONTIME";
    private static final Stmt qryListUsrSessionsStmt = new Stmt(qryListUsrSessionsStmtSQL,Types.VARCHAR);
    private final PreparedStatement qryListUsrSessions;

    private static final String qryCheckPwdIncStmtSQL = "declare\n"
                                                    + "pragma autonomous_transaction;\n"
                                                    + "begin\n"
                                                    + "update RDX_AC_USER set INVALIDLOGONCNT = NVL(INVALIDLOGONCNT,0)+1, INVALIDLOGONTIME = systimestamp where NAME = ?;\n"
                                                    + "commit;\n"
                                                    + "end;";
    private static final Stmt qryCheckPwdIncStmt = new Stmt(qryCheckPwdIncStmtSQL,Types.VARCHAR);
    
    private static final String qryCheckPwdClearStmtSQL = "update RDX_AC_USER set INVALIDLOGONCNT = 0 where NAME = ?";
    private static final Stmt qryCheckPwdClearStmt = new Stmt(qryCheckPwdClearStmtSQL,Types.VARCHAR);

    private static final String qryUdpateUserLastLogonTimeStmtSQL = "update RDX_AC_USER set LASTLOGONTIME = sysdate where NAME = ?";
    private static final Stmt qryUdpateUserLastLogonTimeStmt = new Stmt(qryUdpateUserLastLogonTimeStmtSQL,Types.VARCHAR);

    private static final String getSessionRestorePolicyStmtSQL = "select askUserPwdAfterInactivity from RDX_System where id = 1";
    private static final Stmt getSessionRestorePolicyStmt = new Stmt(getSessionRestorePolicyStmtSQL);

    private static final String qryWritePasswordRequirementsStmtSQL = "select pwdMinLen, pwdMustContainAChars, pwdMustBeInMixedCase, pwdMustContainNChars, pwdMustContainSChars, pwdMustDifferFromName, pwdBlackList  from RDX_System where id = 1";
    private static final Stmt qryWritePasswordRequirementsStmt = new Stmt(qryWritePasswordRequirementsStmtSQL);

    private static final String qryTerminateUserSessionsStmtSQL = "delete from RDX_EASSESSION where ID in (?)";    
    private static final Stmt qryTerminateUserSessionsStmt = new Stmt(qryTerminateUserSessionsStmtSQL,Types.BIGINT);

    private final CommonSelectorFilters commonFilters;
    private final Map<Class, EasRequest> processorsByRqClass = new HashMap<>();
    private final IDbQueries delegate = new DelegateDbQueries(this, null);
    
    private ColorSchemes colorSchemes;
    private String curRqUserName = null;
    private String curRqStationName = null;
    private Integer pwdExpirationPeriodDays = null;
    
    private AadcManager aadcManager;
    
    @Override
    public void close() {
        closeQry(qryUpdateChallenge);
        closeQry(qrySessionQry);
        closeQry(qryScpSaps);
        closeQry(qryAllSaps);
        closeQry(qryScpByStation);
        closeQry(qryListUsrSessions);
    }

    private static void closeQry(final PreparedStatement qry) {
        try {
            qry.close();
        } catch (SQLException ex) {
            //do nothing
            Logger.getLogger(ExplorerAccessService.class.getName()).log(Level.FINE, ex.getMessage(), ex);
        }
    }

    private ExplorerAccessService() {
        qryUpdateChallenge = null;
        qrySessionQry = null;
        qryScpSaps = null;
        qryAllSaps = null;
        qryScpByStation = null;
        qryListUsrSessions = null;
        commonFilters = null;
    }
    
    public ExplorerAccessService(final Arte arte, final int recvTimeout) {
        super(arte, recvTimeout);
        final Connection dbConnection = arte.getDbConnection().get();
        try (final ArteProfiler.TimingSection section = getArte().getProfiler().startTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE)) {
            qryUpdateChallenge = ((RadixConnection) dbConnection).prepareStatement(qryUpdateChallengeStmt);
            qrySessionQry = ((RadixConnection) dbConnection).prepareStatement(qrySessionQryStmt);
            qryScpSaps = ((RadixConnection) dbConnection).prepareStatement(qryScpSapsStmt);
            qryAllSaps = ((RadixConnection) dbConnection).prepareStatement(qryAllSapsStmt);
            qryScpByStation = ((RadixConnection) dbConnection).prepareStatement(qryScpByStationStmt);
            qryListUsrSessions = ((RadixConnection) dbConnection).prepareStatement(qryListUsrSessionsStmt);
            commonFilters = new CommonSelectorFilters(arte);
            colorSchemes = new ColorSchemes(arte);
        } catch (SQLException e) {
            throw new DatabaseError("Can't prepare DAS service query: " + ExceptionTextFormatter.getExceptionMess(e), e);
        }
        aadcManager = arte.getInstance().getAadcManager();
    }

    final class Sap {
        private final String name;
        private final String address;
        private final EPortSecurityProtocol securityProtocol;
        private final EChannelType channelType;
        private final Long priority;
        private final Long blockingPeriod;
        private final Long connectTimeout;
        private final Integer aadcMemberId;

        public Sap(final String name, 
                        final String address, 
                        final EChannelType channelType,
                        final EPortSecurityProtocol securityProtocol, 
                        final Long priority, 
                        final Long blockingPeriod, 
                        final Long connectTimeout,
                        final Integer aadcMemberId) {
            this.name = name;
            this.address = address;
            this.securityProtocol = securityProtocol;
            this.priority = priority;
            this.blockingPeriod = blockingPeriod;
            this.connectTimeout = connectTimeout;
            this.channelType = channelType;
            this.aadcMemberId =  aadcMemberId;
        }

        public String getAddress() {
            return address;
        }

        public Long getBlockingPeriod() {
            return blockingPeriod;
        }

        public Long getConnectTimeout() {
            return connectTimeout;
        }

        public String getName() {
            return name;
        }

        public Long getPriority() {
            return priority;
        }
        
        public Integer getAadcMemberId(){
            return aadcMemberId;
        }

        public EPortSecurityProtocol getSecurityProtocol() {
            return securityProtocol;
        }

        public EChannelType getChannelType() {
            return channelType;
        }
    }

    /**
     *
     * @param stationName
     * @return null if station is not found
     * @throws SQLException
     */
    final String getScpByStation(final String stationName) throws SQLException {
        final ResultSet rs;
        getArte().getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        try {
            qryScpByStation.setString(1, stationName);
            rs = qryScpByStation.executeQuery();
        } finally {
            getArte().getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        }
        try {
            if (rs.next()) {
                return rs.getString(1);
            } else {
                throw new ServiceProcessClientFault(ExceptionEnum.INVALID_STATION.toString(), stationName, null, null);
            }
        } finally {
            rs.close();
        }
    }

    final Collection<Sap> readSaps(final String scpName, final EAuthType authType) throws SQLException {
        final Collection<Sap> lst = new LinkedList<Sap>();
        final ResultSet rs;
        getArte().getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        try {
            final PreparedStatement qrySaps = scpName == null ? qryAllSaps : qryScpSaps;
            for (int i = 1; i <= 4; i++) {
                qrySaps.setString(i, authType == null ? null : authType.getValue());
            }
            if (scpName != null) {
                qrySaps.setString(5, scpName);
            }
            rs = qrySaps.executeQuery();
        } finally {
            getArte().getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        }
        try {
            while (rs.next()) {
                
                if (!ServiceManifestServerLoader.isSapActiveBySelfCheck(rs.getLong("id"), rs.getLong("selfCheckTimeMillis"), rs.getTimestamp("selfCheckTime"), rs.getLong("dbCurMillis"), aadcManager)) {
                    continue;
                }
                
                final String name = rs.getString("name");
                final String address = rs.getString("address");
                final long ProtocolLongVal = rs.getLong("securityProtocol");
                EPortSecurityProtocol securityProtocol;
                if (rs.wasNull()) {
                    securityProtocol = null;
                } else {
                    try {
                        securityProtocol = EPortSecurityProtocol.getForValue(Long.valueOf(ProtocolLongVal));
                    } catch (NoConstItemWithSuchValueError e) {
                        securityProtocol = EPortSecurityProtocol.NONE;
                    }
                }
                final EChannelType channelType = EChannelType.getForValue(rs.getString("channelType"));
                Long priority = Long.valueOf(50);
                Long connectTimeout = Long.valueOf(10);
                Long blockingPeriod = Long.valueOf(600);
                if (scpName != null) {
                    final long priorityLongVal = rs.getLong("sapPriority");
                    priority = rs.wasNull() ? null : Long.valueOf(priorityLongVal);
                    final long blockingPeriodLongVal = rs.getLong("blockingPeriod");
                    blockingPeriod = rs.wasNull() ? null : Long.valueOf(blockingPeriodLongVal);
                    final long connectTimeoutLongVal = rs.getLong("connectTimeout");
                    connectTimeout = rs.wasNull() ? null : Long.valueOf(connectTimeoutLongVal);
                }
                Integer aadcMemberId = null;
                final int aadcMemeberIdIntVal = rs.getInt("aadcMemberId");
                aadcMemberId = rs.wasNull() ? null : Integer.valueOf(aadcMemeberIdIntVal);
                lst.add(new Sap(name, address, channelType, securityProtocol, priority, blockingPeriod, connectTimeout, aadcMemberId));
            }
        } finally {
            rs.close();
        }
        return Collections.unmodifiableCollection(lst);
    }

    final ServiceProcessClientFault kerberosExceptionToFault(final KerberosException exception, final String stationName) throws ServiceProcessClientFault {
        final String reason = ExceptionTextFormatter.getExceptionMess(exception);
        final ArrStr loginTraceDetails = new ArrStr(stationName, String.valueOf(getArte().getArteSocket().getRemoteAddress()), reason);
        getArte().getTrace().put(EEventSeverity.EVENT, Messages.MLS_ID_UNABLE_AUTH_USER_VIA_KERBEROS_WITH_REASON, loginTraceDetails, EEventSource.APP_AUDIT.getValue());
        final String message;
        if (exception.getKerberosReturnCode() == KerberosException.KRB_AP_ERR_TKT_EXPIRED) {
            message = org.radixware.schemas.eas.KerberosAuthFaultMessage.RENEW_CREDENTIALS_REQUIRED.toString();
        } else {
            message = null;
        }
        return new ServiceProcessClientFault(ExceptionEnum.KERBEROS_AUTHENTICATION_FAILED.toString(), message, null, null);//do not send stack or error reason to client
    }

    void throwKerberosDisabled(final String stationName) {
        final String reason = MultilingualString.get(getArte(), Messages.MLS_OWNER_ID, Messages.MLS_ID_REASON_KERBEROS_DISABLED);
        final ArrStr loginTraceDetails = new ArrStr(stationName, String.valueOf(getArte().getArteSocket().getRemoteAddress()), reason);
        getArte().getTrace().put(EEventSeverity.EVENT, Messages.MLS_ID_UNABLE_AUTH_USER_VIA_KERBEROS_WITH_REASON, loginTraceDetails, EEventSource.APP_AUDIT.getValue());
        throw new ServiceProcessClientFault(ExceptionEnum.KERBEROS_AUTHENTICATION_FAILED.toString(), null, null, null);//do not send stack or error reason to client        
    }
    
    private byte[] challenge = null;
    private final byte[] tmpChallenge = new byte[8];

    private byte[] genChallenge() {
        final Random random = new Random();
        random.nextBytes(tmpChallenge);
        return tmpChallenge;
    }

    public byte[] getChallenge(final XmlObject request, final long sessionId, final boolean doWriteToSessionTable, final String currentChallenge) throws ServiceProcessServerFault {
        if (challenge == null || isLastRequest(request)) { // new challenge generate only on transaction start and end
            challenge = genChallenge();
            if (doWriteToSessionTable) {
                getArte().getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
                try {
                    qryUpdateChallenge.setString(1, Hex.encode(challenge));
                    qryUpdateChallenge.setLong(2, sessionId);
                    if (currentChallenge == null || currentChallenge.isEmpty()) {
                        qryUpdateChallenge.setNull(3, java.sql.Types.VARCHAR);
                        qryUpdateChallenge.setNull(4, java.sql.Types.VARCHAR);
                    } else {
                        qryUpdateChallenge.setString(3, currentChallenge);
                        qryUpdateChallenge.setString(4, currentChallenge);
                    }
                    final int updatedCount = qryUpdateChallenge.executeUpdate();
                    if (updatedCount != 1) {
                        throw new ServiceProcessServerFault(ExceptionEnum.SERVER_MALFUNCTION.toString(), "Can't update RDX_EasSession table. The updated sessions count is " + String.valueOf(updatedCount), null, null);
                    }
                } catch (SQLException e) {
                    final String preprocessedExStack = getArte().getTrace().exceptionStackToString(e);
                    throw new ServiceProcessServerFault(ExceptionEnum.SERVER_MALFUNCTION.toString(), "Can't update RDX_EasSession table: " + ExceptionTextFormatter.getExceptionMess(e), e, preprocessedExStack);
                } finally {
                    getArte().getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
                }
            }
        }
        return challenge;
    }

//Protected methods   
    @Override
    protected void prepare(final ArteSocket port, final XmlObject request, final Map<String, String> headerAttrs) throws ServiceProcessFault, InterruptedException {
        try {
            challenge = null;
            getRqProcessor(request).prepare(request);
            arte.addUserNameToProcessorThreadName();
        } catch (Throwable e) {
            throw EasFaults.exception2Fault(getArte(), e, "Request raises exception");
        }
    }

    @Override
    protected RequestTraceBuffer createRequestTraceBuffer() {
        return new RequestTraceBuffer() {
            @Override
            public void put(final TraceItem item) {
                if (Messages.MLS_ID_UNABLE_AUTH_USER_VIA_KERBEROS_WITH_REASON.equals(item.code)) {
                    super.put(new TraceItem(getArte().getMlsProcessor(),
                            item.severity,
                            Messages.MLS_ID_UNABLE_AUTH_USER_VIA_KERBEROS,
                            item.words,
                            item.source,
                            item.isSensitive,
                            item.time));
                } else if (Messages.MLS_ID_USER_FAILED_TO_OPEN_SESSION_WITH_REASON.equals(item.code)
                        && item.words != null
                        && (item.words.contains(Messages.getReasonForException(getArte(), ExceptionEnum.INVALID_USER.toString()))
                            || item.words.contains(Messages.getReasonForException(getArte(), ExceptionEnum.INVALID_PASSWORD.toString())))) {
                    //do not send to client reason of open session failure
                    super.put(new TraceItem(getArte().getMlsProcessor(),
                            item.severity,
                            Messages.MLS_ID_USER_FAILED_TO_OPEN_SESSION,
                            item.words,
                            item.source,
                            item.isSensitive,
                            item.time));
                } else {
                    super.put(item);
                }
            }
        };
    }

    @Override
    protected String getTraceProfile(final XmlObject request) {
        if (request instanceof ReadManifestMess || request instanceof CreateSessionMess) {
            return null;
        }
        return SessionRequest.getTraceProfile(request);
    }

    @Override
    protected boolean isLastRequest(final XmlObject request) {
        if (request instanceof ReadManifestMess || request instanceof CreateSessionMess) {
            return true;
        }
        switch (SessionRequest.getTranWordIntValue(request)) {
            case TransactionEnum.INT_NO:
            case TransactionEnum.INT_FINISH:
            case TransactionEnum.INT_COMMIT_FINISH:
            case TransactionEnum.INT_ROLLBACK_FINISH:
                return true;
            default:
                return false;
        }
    }

    @Override
    protected boolean shouldCommit(final XmlObject request) {
        switch (SessionRequest.getTranWordIntValue(request)) {
            case TransactionEnum.INT_NO:
            case TransactionEnum.INT_AUTO_COMMIT:
            case TransactionEnum.INT_FINISH:
            case TransactionEnum.INT_COMMIT_FINISH:
            case TransactionEnum.INT_ROLLBACK_FINISH:
                return true;
            default:
                return false;
        }
    }

    @Override
    protected XmlObject processRequest(final ArteSocket port, final XmlObject request, final Map<String, String> headerAttrs) throws ServiceProcessFault, InterruptedException {
        try {
            getArte().onStartRequestExecution(Arte.genUserRequestExecutorId(getArte().getUserName()));
            //Transaction
            switch (SessionRequest.getTranWordIntValue(request)) {
                case TransactionEnum.INT_COMMIT:
                case TransactionEnum.INT_COMMIT_FINISH:
                    getArte().commit();
                    break;
                case TransactionEnum.INT_ROLLBACK:
                case TransactionEnum.INT_ROLLBACK_FINISH:
                    getArte().rollback();
                    break;
            }
            return processRequest(request);
        } catch (Throwable e) {
            throw EasFaults.exception2Fault(getArte(), e, "Request raises exception");
        } finally {
            getArte().onFinishRequestExecution();
            curRqUserName = null;
            curRqStationName = null;
        }
    }

    XmlObject processRequest(final XmlObject request) throws ServiceProcessFault, InterruptedException {
        final EasRequest rqProcessor = getRqProcessor(request);
        final java.lang.Object dbLogHandler
                = rqProcessor.getUsrDbTraceProfile() == null
                        ? null
                        : arte.getTrace().addTargetLog(rqProcessor.getUsrDbTraceProfile(), "User[" + getCurRqUserName() + "]");

        try {
            return rqProcessor.process(request);
        } finally {
            commonFilters.clearLocalCache();
            colorSchemes.clearLocalCache();
            if (dbLogHandler != null) {
                arte.getTrace().delTarget(dbLogHandler);
            }
        }
    }


    private EasRequest getRqProcessor(final XmlObject request) throws ServiceProcessFault {
        EasRequest processor = request == null ? null : processorsByRqClass.get(request.getClass());
        if (processor != null) {
            return processor;
        }
        if (request instanceof ReadMess) {
            processor = new ReadRequest(this);
        } else if (request instanceof SelectMess) {
            processor = new SelectRequest(this);
        } else if (request instanceof SetParentMess) {
            processor = new SetParentRequest(this);
        } else if (request instanceof ListVisibleExplorerItemsMess) {
            processor = new ListVisibleExplorerItemsRequest(this);
        } else if (request instanceof ListEdPresVisibleExpItemsMess) {
            processor = new ListEdPresExpItemsRequest(this);
        } else if (request instanceof UpdateMess) {
            processor = new UpdateRequest(this);
        } else if (request instanceof CreateMess) {
            processor = new CreateRequest(this);
        } else if (request instanceof CommandMess) {
            processor = new CommandRequest(this);
        } else if (request instanceof ContextlessCommandMess) {
            processor = new ContextlessCommandRequest(this);
            //} else if(request instanceof GenerateReportMess){
            //	processor = new GenerateReportRequest(this);
        } else if (request instanceof PrepareCreateMess) {
            processor = new PrepareCreateRequest(this);
        } else if (request instanceof ListInstantiatableClassesMess) {
            processor = new ListInstantiatableClassesRequest(this);
        } else if (request instanceof CreateSessionMess) {
            processor = new CreateSessionRequest(this);
        } else if (request instanceof LoginMess) {
            processor = new LoginRequest(this);
        } else if (request instanceof TestMess) {
            processor = new TestRequest(this);
        } else if (request instanceof DeleteMess) {
            processor = new DeleteRequest(this);
        } else if (request instanceof ReadManifestMess) {
            processor = new ReadManifestRequest(this);
        } else if (request instanceof GetObjectTitlesMess) {
            processor = new GetObjectTitlesRequest(this);
        } else if (request instanceof CalcSelectionStatisticMess) {
            processor = new CalcSelectionStatisticRequest(this);
        } else if (request instanceof GetDatabaseInfoMess){
            processor = new GetDatabaseInfoRequest(this);
        } else if (request instanceof ChangePasswordMess) {
            processor = new ChangePasswordRequest(this);
        } else if (request instanceof GetPasswordRequirementsMess) {
            processor = new GetPasswordRequirementsRequest(this);
        } else if (request instanceof CloseSessionMess) {
            processor = new CloseSessionRequest(this);
        } else {
            if (getArte().getTrace().getMinSeverity(EEventSource.EAS) <= EEventSeverity.DEBUG.getValue().longValue()) {
                final String mess
                        = "EAS received wrong request: " + request.getDomNode().getLocalName() + "\n"
                        + "Request class: " + request.getClass().getName() + ", classLoader: " + request.getClass().getClassLoader().toString() + "\n"
                        + "EAS requests' classLoader: " + TestMess.class.getClassLoader().toString() + "\n"
                        + "Thread's context classLoader: " + Thread.currentThread().getContextClassLoader();
                getArte().getTrace().put(EEventSeverity.DEBUG, mess, EEventSource.EAS);
            }
            throw new ServiceProcessClientFault(ExceptionEnum.INVALID_REQUEST.toString(), "Request \"" + request.getDomNode().getLocalName() + "\" is not supported \nby \"" + SERVICE_WSDL + "\" service", null, null);
        }
        processorsByRqClass.put(request.getClass(), processor);
        return processor;
    }

    @Override
    protected ServiceProcessFault errorToFault(final Throwable e) {
        if (e instanceof ServiceProcessFault) {
            return (ServiceProcessFault) e;
        }
        final String comment = "Unhandled exception in service \"" + SERVICE_WSDL + "\"";
        final String reason;
        if (e instanceof RuntimeException) {
            return EasFaults.exception2Fault(getArte(), (RuntimeException) e, comment);
        } else {
            reason = ExceptionEnum.SERVER_EXCEPTION.toString();
        }
        final String preprocessedExStack = getArte().getTrace().exceptionStackToString(e);
        getArte().getTrace().put(EEventSeverity.WARNING, comment + " request processing:\n" + preprocessedExStack, EEventSource.EAS);
        return new ServiceProcessServerFault(reason, "Unhandled " + e.getClass().getName() + (e.getMessage() != null ? ":\n" + e.getMessage() : ""), e, preprocessedExStack);
    }

    @Override
    protected void prepareResponseForSend(final XmlObject request, final XmlObject response, final RequestTraceBuffer traceBuffer) {
        //final String rq = request.getDomNode().getLocalName();
        //Adding trace
        if (request instanceof ReadManifestMess || request instanceof CreateSessionMess) { //is out of the session
            return;
        } else if (request instanceof TestMess) {
            return;
        } else if (request instanceof LoginMess) {
            //do nothing
        } else if (request instanceof ReadMess) {
            SessionRequest.writeTrace(((ReadDocument) response).getRead().getReadRs(), traceBuffer);
        } else if (request instanceof SelectMess) {
            SessionRequest.writeTrace(((SelectDocument) response).getSelect().getSelectRs(), traceBuffer);
        } else if (request instanceof SetParentMess) {
            SessionRequest.writeTrace(((SetParentDocument) response).getSetParent().getSetParentRs(), traceBuffer);
        } else if (request instanceof ListVisibleExplorerItemsMess) {
            SessionRequest.writeTrace(((ListVisibleExplorerItemsDocument) response).getListVisibleExplorerItems().getListVisibleExplorerItemsRs(), traceBuffer);
        } else if (request instanceof ListEdPresVisibleExpItemsMess) {
            SessionRequest.writeTrace(((ListEdPresVisibleExpItemsDocument) response).getListEdPresVisibleExpItems().getListEdPresVisibleExpItemsRs(), traceBuffer);
        } else if (request instanceof UpdateMess) {
            SessionRequest.writeTrace(((UpdateDocument) response).getUpdate().getUpdateRs(), traceBuffer);
        } else if (request instanceof CreateMess) {
            SessionRequest.writeTrace(((CreateDocument) response).getCreate().getCreateRs(), traceBuffer);
        } else if (request instanceof CommandMess) {
            SessionRequest.writeTrace(((CommandDocument) response).getCommand().getCommandRs(), traceBuffer);
        } else if (request instanceof ContextlessCommandMess) {
            SessionRequest.writeTrace(((ContextlessCommandDocument) response).getContextlessCommand().getContextlessCommandRs(), traceBuffer);
        } else if (request instanceof PrepareCreateMess) {
            SessionRequest.writeTrace(((PrepareCreateDocument) response).getPrepareCreate().getPrepareCreateRs(), traceBuffer);
        } else if (request instanceof ListInstantiatableClassesMess) {
            SessionRequest.writeTrace(((ListInstantiatableClassesDocument) response).getListInstantiatableClasses().getListInstantiatableClassesRs(), traceBuffer);
        } else if (request instanceof DeleteMess) {
            SessionRequest.writeTrace(((DeleteDocument) response).getDelete().getDeleteRs(), traceBuffer);
        } else if (request instanceof GetObjectTitlesMess) {
            SessionRequest.writeTrace(((GetObjectTitlesDocument) response).getGetObjectTitles().getGetObjectTitlesRs(), traceBuffer);
        } else if (request instanceof GetPasswordRequirementsMess) {
            return;
        } else if (request instanceof CalcSelectionStatisticMess){
            SessionRequest.writeTrace(((CalcSelectionStatisticDocument) response).getCalcSelectionStatistic().getCalcSelectionStatisticRs(), traceBuffer);
        } else if (request instanceof GetDatabaseInfoMess){
            SessionRequest.writeTrace(((GetDatabaseInfoDocument) response).getGetDatabaseInfo().getGetDatabaseInfoRs(), traceBuffer);
        }else if (request instanceof ChangePasswordMess) {
            //do nothing 
            //SessionRequest.writeTrace(((ChangePasswordDocument) response).getChangePassword().getChangePasswordRs(), traceBuffer);
        } else if (request instanceof CloseSessionMess) {
            //session was removed - do nothing
        } else {
            getArte().getTrace().put(EEventSeverity.WARNING, "Trace for \"" + request.getDomNode().getLocalName() + "\" method is not supported", EEventSource.EAS);
        }
    }

    @Override
    protected String getServiceWsdl() {
        return SERVICE_WSDL;
    }

    @Override
    protected void checkClientCanTrace(final String traceProfile) throws ServiceProcessClientFault {
        if (getArte().isInTransaction() && traceProfile != null && !traceProfile.equals(EEventSeverity.NONE.getName()) && !traceProfile.equals(EEventSeverity.NONE.getName() + ";")) {
            if (!getArte().getRights().getUserCanAccess(arte.getUserName(), EDrcServerResource.TRACING)) {
                throw EasFaults.newAccessViolationFault(getArte(), Messages.MLS_ID_INSUF_PRIV_TO_TRACE_REQUEST, null);
            }
        }
    }

    static final class SessionOptions {

        private final String scp;
        private final String usrDbTraceProfile;
        private final String currentChallenge;
        private final byte[] securityToken;
        private final boolean traceUsrActions;

        public SessionOptions(final String scp,
                final String dbTraceProfile,
                final String currentChallenge,
                final boolean traceUsrActions,
                final byte[] securityToken) {
            this.scp = scp;
            this.currentChallenge = currentChallenge;
            this.usrDbTraceProfile = EEventSeverity.NONE.getName().equals(dbTraceProfile) ? null : dbTraceProfile;
            this.traceUsrActions = traceUsrActions;
            this.securityToken = securityToken;
        }

        public String getUsrDbTraceProfile() {
            return usrDbTraceProfile;
        }

        public String getScp() {
            return scp;
        }

        public boolean getUsrActionsIsTraced() {
            return traceUsrActions;
        }

        public byte[] getSecurityToken() {
            return securityToken;
        }

        public String getCurrentChallenge() {
            return currentChallenge;
        }
    }

    SessionOptions connectToSession(final SessionRequest rq,
            final Long sessionId,
            final Long version,
            final byte[] pwdToken,
            final byte[] clientChallenge,
            final EAuthType authType
    ) {
        return connectToSession(rq, sessionId, version, pwdToken, clientChallenge, authType, true);
    }

    SessionOptions connectToSession(final SessionRequest rq,
            final Long sessionId,
            final Long version,
            final byte[] pwdToken,
            final byte[] clientChallenge,           
            final EAuthType authType,
            final boolean startArteTransaction) {
        try {
            final ResultSet rs;
            getArte().getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
            try {
                qrySessionQry.setString(1, "%" + authType.getValue() + "%");
                qrySessionQry.setObject(2, sessionId);
                rs = qrySessionQry.executeQuery();
            } finally {
                getArte().getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
            }
            try {
                if (rs.next()) {
                    final String usrDbTraceProfile = rs.getString("usrDbTraceProfile");
                    final java.lang.Object dbLogHandler = usrDbTraceProfile == null ? null : arte.getTrace().addTargetLog(usrDbTraceProfile , "User[" + getCurRqUserName() + "]");
                    try {
                        authenticate(rq, rs, pwdToken, authType);
                        final EIsoLanguage clientLanguage = EIsoLanguage.getForValue(rs.getString("LANGUAGE"));
                        ERuntimeEnvironmentType clientEnvironment;
                        try {
                            clientEnvironment = ERuntimeEnvironmentType.getForValue(rs.getString("ENVIRONMENT"));
                        } catch (NoConstItemWithSuchValueError error) {
                            clientEnvironment = null;
                        }
                        EIsoCountry clientCountry;
                        try {
                            clientCountry = EIsoCountry.getForValue(rs.getString("COUNTRY"));
                        } catch (NoConstItemWithSuchValueError error) {
                            clientCountry = null;
                        }
                        if (startArteTransaction) {
                            final String serverKey;
                            switch (authType){
                                case PASSWORD:
                                    serverKey = rs.getString("PWDHASH");
                                    break;
                                case KERBEROS:
                                    serverKey = rs.getString("SERVERKEY");
                                    break;
                                default:
                                    if (rs.getInt("CHECK_CERTIFICATE") > 0){
                                        serverKey = null;
                                    }else{
                                        serverKey = rs.getString("SERVERKEY");
                                    }
                            }

                            String requestName = rq.getClass().getSimpleName();
                            if (requestName.endsWith("Request")) {
                                requestName = requestName.substring(0, requestName.length() - "Request".length());
                            }

                            final ArteTransactionParams tranParams = 
                                new ArteTransactionParams(version, 
                                                          sessionId,
                                                          requestName,
                                                          getCurRqUserName(),
                                                          getCurRqStationName(),
                                                          clientLanguage,
                                                          clientCountry,
                                                          clientEnvironment,
                                                          Hex.decode(serverKey),
                                                          null,
                                                          null
                                );
                            getArte().startTransaction(tranParams);
                            getArte().getCache().setMode(Cache.EMode.OBJS_CACHED_IN_PREV_TRANS_PROHIBITED);//RADIX-10047
                            if (!Utils.equals(version, getArte().getLatestCachedVersion())) { //optimization: current version compability is checked by Instance
                                //RADIX-3193
                                final List<RadixLoaderActualizer.DdsVersionWarning> warnings = new LinkedList<RadixLoaderActualizer.DdsVersionWarning>();
                                RadixLoaderActualizer.checkDdsVersions(
                                        getArte().getDbConnection().get(),
                                        getArte().getDefManager().getReleaseCache().getRelease().getRevisionMeta(),
                                        warnings);
                                for (RadixLoaderActualizer.DdsVersionWarning w : warnings) {
                                    if (!w.isDbStructCompatible()) {
                                        throw new ServiceProcessClientFault(ExceptionEnum.UNSUPPORTED_DEFINITION_VERSION.toString(), "Unsupported definitions version", null, null);
                                    }
                                }
                            }
                        }
                        //can't use getCurUserCanAccess() because getArte().onStartRequestExecution() has not been called yet
                        if (!getArte().getRights().getUserCanAccess(curRqUserName, EDrcServerResource.EAS)) {
                            throw EasFaults.newAccessViolationFault(getArte(), Messages.MLS_ID_INSUF_PRIV_TO_CONNECT_TO_EAS, null);
                        }
                        final String clientKey = rs.getString("CLIENTKEY");
                        final byte[] securityToken;
                        if (clientKey != null && !clientKey.isEmpty() && clientChallenge != null && clientChallenge.length > 0) {
                            securityToken
                                    = AuthUtils.calcPwdToken(clientChallenge, Hex.decode(clientKey));
                        } else {
                            securityToken = null;
                        }
                        return new SessionOptions(rs.getString("SCPNAME"),
                                usrDbTraceProfile,
                                rs.getString("CHALLENGE"),
                                rs.getLong("traceUsrActions") != 0,
                                securityToken);
                    } finally {
                        if (dbLogHandler != null) {
                            arte.getTrace().delTarget(dbLogHandler);
                        }
                    }
                } else {
                    throw EasFaults.newSessionDoesNotExist(getSessionRestorePolicy(getArte(), authType));
                }
            } finally {
                rs.close();
            }
        } catch (SQLException e) {
            throw new ServiceProcessServerFault(
                    ExceptionEnum.SERVER_MALFUNCTION.toString(),
                    "Can't read session data: " + ExceptionTextFormatter.getExceptionMess(e),
                    null, null);
        }
    }

    private void authenticate(final SessionRequest rq,
            final ResultSet rs,
            final byte[] pwdToken,
            final EAuthType authType) throws SQLException {
        curRqUserName = rs.getString("USERNAME");
        curRqStationName = rs.getString("STATIONNAME");
        final boolean needForLogin = rs.getInt("NEED_FOR_LOGIN") == 1;
        final boolean isCloseRequest = rq instanceof CloseSessionRequest;
        final boolean isLoginRequest = rq instanceof LoginRequest;
        final boolean isChangePasswordRequest = rq instanceof ChangePasswordRequest;
        if (!isCloseRequest && rs.getInt("USERLOCKED") != 0) {
            getArte().getTrace().put(EEventSeverity.WARNING, Messages.MLS_ID_TRY_TO_USE_LOCKED_USER, new ArrStr(getCurRqUserName(), getCurRqStationName(), String.valueOf(getArte().getArteSocket().getRemoteAddress())), EEventSource.APP_AUDIT.getValue());
            throw new ServiceProcessClientFault(ExceptionEnum.USER_ACCOUNT_LOCKED.toString(), getCurRqUserName(), null, null);
        }
        if (!isCloseRequest && rs.getInt("AUTH_TYPE_ACCESSIBLE") == 0) {
            getArte().getTrace().put(EEventSeverity.WARNING, Messages.MLS_ID_TRY_TO_USE_FORBIDDEN_AUTH_TYPE, new ArrStr(curRqUserName, curRqStationName, authType.getValue()), EEventSource.APP_AUDIT.getValue());
            throw new ServiceProcessClientFault(ExceptionEnum.INVALID_AUTH_TYPE.toString(), authType.getValue() + "\n" + curRqUserName, null, null);
        }
        if (needForLogin && !isLoginRequest && !isCloseRequest) {
            //incomplete authentication
            getArte().getTrace().put(EEventSeverity.WARNING, Messages.MLS_ID_LOGIN_REQUIRED, new ArrStr(curRqUserName, curRqStationName), EEventSource.APP_AUDIT.getValue());
            throw new ServiceProcessClientFault(ExceptionEnum.INVALID_REQUEST.toString(), "User authentication was not complete", null, null);
        } else if (!needForLogin && rq instanceof LoginRequest) {
            throw new ServiceProcessClientFault(ExceptionEnum.INVALID_REQUEST.toString(), "Login operation does not required", null, null);
        }

        final String challengeHex = rs.getString("CHALLENGE");
        final String pwdTokenHex = Hex.encode(pwdToken);
        switch (authType) {
            case KERBEROS:
                if (getArte().getArteSocket().getUnit().getEasKerberosAuthPolicy() == EClientAuthentication.None) {
                    throwKerberosDisabled(curRqStationName);
                }
                if (isChangePasswordRequest) {
                    checkPwd(getCurRqUserName(), rs.getString("PWDHASH"), challengeHex, pwdTokenHex, rs.getInt("INVALIDLOGONCNT") > 0);
                } else if (!isLoginRequest) {
                    checkPwd(getCurRqUserName(), rs.getString("SERVERKEY"), challengeHex, pwdTokenHex, rs.getInt("INVALIDLOGONCNT") > 0);
                }
                break;
            case CERTIFICATE:
                if (!isLoginRequest){
                    if (rs.getInt("CHECK_CERTIFICATE") > 0) {
                        final SSLSession sslSession = getArte().getArteSocket().getSslSession();
                        if (sslSession == null) {
                            throw new ServiceProcessClientFault(ExceptionEnum.INVALID_CERTIFICATE.toString(), "Secure connection must be used to authenticate by certificate", null, null);
                        }
                        final String dn;
                        try {
                            dn = sslSession.getPeerPrincipal().getName();
                        } catch (SSLPeerUnverifiedException e) {
                            throw new ServiceProcessClientFault(ExceptionEnum.INVALID_CERTIFICATE.toString(), "Unable to authorize user by given certificate", null, null);
                        }
                        if (isChangePasswordRequest) {
                            checkPwd(getCurRqUserName(), rs.getString("PWDHASH"), challengeHex, pwdTokenHex, rs.getInt("INVALIDLOGONCNT") > 0);
                        } else {
                            final String certAttrForUserName = rs.getString("CERTATTRFORUSERLOGIN");
                            curRqUserName = CertificateUtils.parseDistinguishedName(dn).get(certAttrForUserName);
                            if (curRqUserName == null || curRqUserName.isEmpty()) {
                                throw new ServiceProcessClientFault(ExceptionEnum.INVALID_CERTIFICATE.toString(), "Can't read user name from \'" + certAttrForUserName + "\' certificate attribute", null, null);
                            }
                            if (!challengeHex.equals(pwdTokenHex)) {
                                throw new ServiceProcessClientFault(ExceptionEnum.INVALID_CHALLENGE.toString(), "Session integrity is broken", null, null);
                            }
                        }
                    }else{
                        if (isChangePasswordRequest) {
                            checkPwd(getCurRqUserName(), rs.getString("PWDHASH"), challengeHex, pwdTokenHex, rs.getInt("INVALIDLOGONCNT") > 0);
                        } else if (!needForLogin || !isCloseRequest) {
                            final String encKey = rs.getString("SERVERKEY");
                            checkPwd(getCurRqUserName(), encKey, challengeHex, pwdTokenHex, rs.getInt("INVALIDLOGONCNT") > 0);
                        }
                    }
                }
                break;
            default:
                if (!isCloseRequest && rs.getInt("TMPPWDEXPIRED") == 1 ){
                    getArte().getTrace().put(Messages.MLS_ID_TEMPORARY_PASSWORD_EXPIRED, new ArrStr(getCurRqUserName(), getCurRqStationName(), String.valueOf(getArte().getArteSocket().getRemoteAddress())));
                    throw EasFaults.newTemporaryPasswordExpiredFault();
                }
                if (!isChangePasswordRequest
                        && !(rq instanceof GetPasswordRequirementsRequest)
                        && !isLoginRequest
                        && !isCloseRequest
                        && (rs.getInt("ISINTERACTIVE") > 0 && pwdExpired(rs.getInt("MUSTCHANGEPWD") != 0, rs.getInt("PASSWORD_USE_DAYS")))) {
                    final PasswordRequirementsDocument doc = PasswordRequirementsDocument.Factory.newInstance();
                    try {
                        writePasswordRequirements(getArte(), doc.addNewPasswordRequirements());
                        throw new ServiceProcessClientFault(ExceptionEnum.SHOULD_CHANGE_PASSWORD.toString(), doc.xmlText(), null, null);
                    } catch (InterruptedException exception) {
                        throw new ServiceProcessClientFault(ExceptionEnum.SHOULD_CHANGE_PASSWORD.toString(), null, null, null);
                    }
                }
                if (!needForLogin || !isCloseRequest) {
                    checkPwd(getCurRqUserName(), rs.getString("PWDHASH"), challengeHex, pwdTokenHex, rs.getInt("INVALIDLOGONCNT") > 0);
                }//else reject postponed login and close the session
        }
        if (!isCloseRequest && !isLoginRequest && rs.getInt("LOGON_TIME_OK") == 0) {
            getArte().getTrace().put(Messages.MLS_ID_LOGON_TIME_EXPIRED, new ArrStr(getCurRqUserName(), getCurRqStationName(), String.valueOf(getArte().getArteSocket().getRemoteAddress())));
            throw EasFaults.newLogonTimeRestrictionViolationFault();
        }
    }

    boolean pwdExpired(final boolean mustChangePwdByDbSettings, final int pwdUseDays) {
        final boolean mustChangePwdByRuntimeSettings = 0 < getPwdExpirationPeriodFromAppSettings() && getPwdExpirationPeriodFromAppSettings() < pwdUseDays;
        return mustChangePwdByDbSettings || mustChangePwdByRuntimeSettings;
    }

    private int getPwdExpirationPeriodFromAppSettings() {
        if (pwdExpirationPeriodDays == null) {
            final Map<String, String> appParams = getArte().getAppParams();
            if (appParams.containsKey(PWD_EXPIRATION_PERIOD_OPTION)) {
                try {
                    pwdExpirationPeriodDays = new Integer(appParams.get(PWD_EXPIRATION_PERIOD_OPTION));
                } catch (Exception ex) {
                    pwdExpirationPeriodDays = -1;
                }
            } else {
                pwdExpirationPeriodDays = -1;
            }
        }
        return pwdExpirationPeriodDays;
    }

    void checkPwd(
            final String userName,
            final String pwdHashHex,
            final String challengeHex,
            final String pwdTokenHex,
            final boolean clearInvalidLogonCounter) throws SQLException {
        if (pwdHashHex == null) // can't use localized variant here as transaction is not started
        {
            throw EasFaults.newAccessViolationFault(arte, "User \"" + userName + "\" can't be identified by password. Password is not defined.");
        }
        final byte[] pwdHash = Hex.decode(pwdHashHex);
        final byte[] rqChallenge = Hex.decode(challengeHex);
        final byte[] pwdToken = AuthUtils.calcPwdToken(rqChallenge, pwdHash);
        if (!Hex.encode(pwdToken).equals(pwdTokenHex)) {
            //let's increment INVALIDLOGONCNT
            try(final PreparedStatement qry = ((RadixConnection)getArte().getDbConnection().get()).prepareStatement(qryCheckPwdIncStmt)) {
                qry.setString(1, userName);
                qry.executeUpdate();
            }
            throw EasFaults.newInvalidPassword();
        } else if (clearInvalidLogonCounter) {
            try(final PreparedStatement qry = ((RadixConnection)getArte().getDbConnection().get()).prepareStatement(qryCheckPwdClearStmt)) {
                qry.setString(1, userName);
                qry.executeUpdate();
            }
        }
    }

    void udpateUserLastLogonTime(String userName) throws SQLException {
        try(final PreparedStatement qry = ((RadixConnection)getArte().getDbConnection().get()).prepareStatement(qryUdpateUserLastLogonTimeStmt)) {
            qry.setString(1, userName);
            qry.executeUpdate();
        }
    }

//	final String getUserNameBySessionId(final Long sessionId) {
//		try {
//			qrySessionQry.setLong(1, sessionId);
//			final ResultSet rs = qrySessionQry.executeQuery();
//			try {
//				if (rs.next()) {
//					return rs.getString("USERNAME");
//				} else {
//					throw EasFaults.newSessionDoesNotExist(getSessionRestorePolicy());
//				}
//			} finally {
//				rs.close();
//			}
//		} catch (SQLException e) {
//			final String preprocessedExStack = getArte().getTrace().exceptionStackToString(e);
//			throw new ServiceProcessServerFault(ExceptionEnum.SERVER_MALFUNCTION.toString(), "Can't read session data: " + ExceptionTextFormatter.getExceptionMess(e), e, preprocessedExStack);
//		}
//	}
    static SessionRestorePolicy.Enum getSessionRestorePolicy(final Arte arte, final EAuthType authType) throws SQLException {
        try(final PreparedStatement qry = ((RadixConnection)arte.getDbConnection().get()).prepareStatement(getSessionRestorePolicyStmt);
            final ResultSet rs = qry.executeQuery()) {
            if (!rs.next()) {
                throw new RadixError("This System (#1) not found in RDX_SYSTEM table");
            }
            else if (rs.getInt(1) != 0 && authType != EAuthType.KERBEROS) {
                return SessionRestorePolicy.PASSWORD_MUST_BE_ENTERED;
            } else {
                return SessionRestorePolicy.SAVED_PASSWORD_CAN_BE_USED;
            }
        }
    }

    String getCurRqUserName() {
        return curRqUserName;
    }

    String getCurRqStationName() {
        return curRqStationName;
    }

    CommonSelectorFilters getCommonSelectorFilters() {
        return commonFilters;
    }

    ColorScheme getColorScheme(final Arte arte, final RadClassDef classDef, final Id selPresentationId) {
        return colorSchemes.getColorScheme(arte, classDef, selPresentationId);
    }

    static void writePasswordRequirements(final Arte arte, org.radixware.schemas.eas.PasswordRequirements requirements) throws InterruptedException {
        try {
            final PreparedStatement st;
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            try {
                st = ((RadixConnection)arte.getDbConnection().get()).prepareStatement(qryWritePasswordRequirementsStmt);
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            }
            try {
                final ResultSet rs;
                arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
                try {
                    rs = st.executeQuery();
                } finally {
                    arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
                }
                try {
                    if (rs.next()) {
                        final int minLen = rs.getInt("pwdMinLen");
                        if (!rs.wasNull()) {
                            requirements.setMinLen(minLen);
                        }
                        final boolean mustHaveAChars = rs.getBoolean("pwdMustContainAChars");
                        if (!rs.wasNull()) {
                            requirements.setAlphabeticCharsRequired(mustHaveAChars);
                            if (mustHaveAChars){
                                final boolean mustHaveACharsInMixedCase = rs.getBoolean("pwdMustBeInMixedCase");
                                if (!rs.wasNull()){
                                    requirements.setAlphabeticCharsInMixedCaseRequired(mustHaveACharsInMixedCase);
                                }
                            }
                        }                        
                        final boolean mustHaveNChars = rs.getBoolean("pwdMustContainNChars");
                        if (!rs.wasNull()) {
                            requirements.setNumericCharsRequired(mustHaveNChars);
                        }
                        final boolean mustHaveSChars = rs.getBoolean("pwdMustContainSChars");
                        if (!rs.wasNull()) {
                            requirements.setSpecialCharsRequired(mustHaveSChars);
                        }
                        requirements.setPwdMustDifferFromName(rs.getBoolean("pwdMustDifferFromName"));
                        final Clob pwdBlackListAsClob = rs.getClob("pwdBlackList");
                        final ArrStr arrPwdBlackList;
                        if (pwdBlackListAsClob==null){                            
                            arrPwdBlackList = null;
                        }else{
                            try{
                                final long len = pwdBlackListAsClob.length();
                                if (len > Integer.MAX_VALUE){
                                    throw new ServiceProcessServerFault(ExceptionEnum.SERVER_MALFUNCTION.toString(), "Too large password black list", null, null);
                                }else{
                                    arrPwdBlackList = 
                                        (ArrStr)SrvValAsStr.fromStr(arte, pwdBlackListAsClob.getSubString(1, (int)len), EValType.ARR_STR);
                                }
                            }finally{
                                pwdBlackListAsClob.free();
                            }
                        }
                        if (arrPwdBlackList!=null && !arrPwdBlackList.isEmpty()){
                             final org.radixware.schemas.eas.PasswordRequirements.BlackList blackList = requirements.addNewBlackList();
                             for (String forbiddenPwd: arrPwdBlackList){
                                 blackList.addItem(forbiddenPwd);
                             }
                        }
                    }
                } finally {
                    rs.close();
                }
            } finally {
                st.close();
            }
        } catch (SQLException ex) {
            throw EasFaults.exception2Fault(arte, ex, "Error on service DB query processing");
        }
    }

    void checkNumberOfUserSessions(final String userName,
            final int maxNumber,
            final Collection<String> sessionsToTerminate) {
        try {
            qryListUsrSessions.setString(1, userName);
            try (ResultSet rs = qryListUsrSessions.executeQuery()) {
                final UserSessionsDocument userSessionsDoc = UserSessionsDocument.Factory.newInstance();
                final UserSessionsDocument.UserSessions userSessions = userSessionsDoc.addNewUserSessions();
                userSessions.setMaxNumber(maxNumber);
                int counter = 0;
                List<String> selectedForTerminate = new LinkedList<>();
                while (rs.next()) {
                    final String serverKeyAsStr = rs.getString("SERVERKEY");
                    final byte[] serverKey = Hex.decode(serverKeyAsStr);
                    final String idAsStr = String.format("%1$016d", rs.getLong("ID"));
                    final byte[] encryptedId = AuthUtils.encrypt(idAsStr.getBytes("UTF-8"), serverKey);
                    final String encryptedIdStr = Hex.encode(encryptedId);
                    if (sessionsToTerminate.contains(encryptedIdStr)) {
                        selectedForTerminate.add(idAsStr);
                    } else {
                        writeSessionDescription(userSessions.addNewItem(), rs, encryptedIdStr);
                        counter++;
                    }
                }
                if (!selectedForTerminate.isEmpty()) {
                    terminateUserSessions(selectedForTerminate);
                }
                if (maxNumber > 0 && counter >= maxNumber) {
                    throw EasFaults.newSessionsLimitExceedFault(userSessionsDoc);
                }
            }
        } catch (SQLException | UnsupportedEncodingException ex) {
            final String preprocessedExStack = ExceptionTextFormatter.exceptionStackToString(ex);
            throw new ServiceProcessServerFault(ExceptionEnum.SERVER_MALFUNCTION.toString(), "Failed to check number of opened sessions: " + ExceptionTextFormatter.getExceptionMess(ex), ex, preprocessedExStack);
        }
    }

    private static void writeSessionDescription(final SessionDescription sessionDesc, final ResultSet rs, final String encryptedIdStr) throws SQLException {
        sessionDesc.setCreationTime(rs.getTimestamp("CREATIONTIME"));
        sessionDesc.setIdleSeconds(rs.getInt("IDLETIME"));
        sessionDesc.setStationName(rs.getString("STATIONNAME"));
        final String environmentAsStr = rs.getString("ENVIRONMENT");
        ERuntimeEnvironmentType environment;
        if (environmentAsStr == null || environmentAsStr.isEmpty()) {
            environment = null;
        } else {
            try {
                environment = ERuntimeEnvironmentType.getForValue(environmentAsStr);
            } catch (NoConstItemWithSuchValueError error) {
                environment = null;
            }
        }
        if (environment != null) {
            sessionDesc.setEnvironment(environment);
        }
        sessionDesc.setEncryptedId(encryptedIdStr);
    }

    private int terminateUserSessions(final List<String> sessionIds) {
        final StringBuilder delSessionsQueryBuilder = new StringBuilder();
        char prefix = ' ';
        
        for (String idAsStr : sessionIds) {
            delSessionsQueryBuilder.append(prefix).append(idAsStr);
            prefix = ',';
        }
        // Modify the only parameter id (?) on the SQL to the parameter's list!
        try (PreparedStatement delStatement = getArte().getDbConnection().get().prepareStatement(qryTerminateUserSessionsStmt.getText().replace("?",delSessionsQueryBuilder.toString()))) {
            return delStatement.executeUpdate();
        } catch (SQLException ex) {
            final String preprocessedExStack = ExceptionTextFormatter.exceptionStackToString(ex);
            throw new ServiceProcessServerFault(ExceptionEnum.SERVER_MALFUNCTION.toString(), "Failed to terminate selected sessions: " + ExceptionTextFormatter.getExceptionMess(ex), ex, preprocessedExStack);
        }
    }
}
