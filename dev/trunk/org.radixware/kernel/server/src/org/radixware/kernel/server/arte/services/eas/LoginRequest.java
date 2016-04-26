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

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlObject;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.operator.OperatorCreationException;
import org.radixware.kernel.common.auth.AuthUtils;
import org.radixware.kernel.common.enums.EAuthType;
import org.radixware.kernel.common.enums.ETimingSection;
import org.radixware.kernel.common.exceptions.ServiceProcessClientFault;
import org.radixware.kernel.common.exceptions.ServiceProcessFault;
import org.radixware.kernel.common.exceptions.ServiceProcessServerFault;
import org.radixware.kernel.common.ssl.CertificateUtils;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.Hex;
import org.radixware.kernel.server.arte.ArteProfiler;
import org.radixware.kernel.server.exceptions.DatabaseError;
import org.radixware.schemas.eas.ExceptionEnum;
import org.radixware.schemas.eas.LoginMess;
import org.radixware.schemas.eas.LoginRq;
import org.radixware.schemas.eas.LoginRs;
import org.radixware.schemas.eas.SessionRestorePolicy;
import org.radixware.schemas.easWsdl.LoginDocument;

final class LoginRequest extends SessionRequest {

    private final PreparedStatement selectSessionQry;

    LoginRequest(final ExplorerAccessService presenter) {
        super(presenter);
        final ArteProfiler profiler = getArte().getProfiler();
        profiler.enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
        try {
            selectSessionQry = getArte().getDbConnection().get().prepareStatement(
                    "select ses.USERCERTIFICATE, ses.SERVERKEY, ses.CHALLENGE, ses.ISINTERACTIVE, ses.USERNAME,"
                    + " (case when "
                    + "(u.logonScheduleId is not null AND RDX_JS_IntervalSchedule.isIn(logonScheduleId,sysdate)>0) "
                    + "OR (u.logonScheduleId is null "
                    + "   AND (not exists (select NULL from RDX_AC_USER2USERGROUP, RDX_AC_USERGROUP where RDX_AC_USER2USERGROUP.USERNAME=u.Name and RDX_AC_USERGROUP.NAME = RDX_AC_USER2USERGROUP.GROUPNAME)"
                    + "        OR exists  (select NULL from RDX_AC_USER2USERGROUP, RDX_AC_USERGROUP where RDX_AC_USER2USERGROUP.USERNAME=u.Name and RDX_AC_USERGROUP.NAME = RDX_AC_USER2USERGROUP.GROUPNAME and (RDX_AC_USERGROUP.LOGONSCHEDULEID is null OR RDX_JS_IntervalSchedule.isIn(RDX_AC_USERGROUP.LOGONSCHEDULEID,sysdate)>0))"
                    + "       )"
                    + "   )"
                    + "then 1 else 0 end) as LOGON_TIME_OK, "
                    + " (case when "
                    + "(u.SESSIONSLIMIT = 0 AND (sys.LIMITEASSESSIONSPERUSR is NULL or sys.LIMITEASSESSIONSPERUSR < 1 or ( select count(*) from RDX_EASSESSION where RDX_EASSESSION.USERNAME = u.Name AND RDX_EASSESSION.ISINTERACTIVE = 1  AND (RDX_EASSESSION.USERCERTIFICATE is null OR dbms_lob.getlength(RDX_EASSESSION.USERCERTIFICATE)=0) ) < sys.LIMITEASSESSIONSPERUSR))"
                    + " OR "
                    + "(u.SESSIONSLIMIT !=0 AND (u.SESSIONSLIMIT is NULL or u.SESSIONSLIMIT < 1 or ( select count(*) from RDX_EASSESSION where RDX_EASSESSION.USERNAME = u.Name AND RDX_EASSESSION.ISINTERACTIVE = 1  AND (RDX_EASSESSION.USERCERTIFICATE is null OR dbms_lob.getlength(RDX_EASSESSION.USERCERTIFICATE)=0) ) < u.SESSIONSLIMIT))"
                    + " then 1 else 0 end) as SESSIONSLIMIT_OK, "
                    + " (case when u.SESSIONSLIMIT = 0 then NVL(sys.LIMITEASSESSIONSPERUSR,0) else NVL(u.SESSIONSLIMIT,0) end) as SESSIONSLIMIT "
                    + " from RDX_EASSESSION ses left outer join RDX_AC_USER u on u.name = ses.username, RDX_SYSTEM sys"
                    + " where ses.ID = ? and sys.id = 1");
        } catch (SQLException e) {
            throw new DatabaseError("Can't init EAS service DB query: " + ExceptionTextFormatter.getExceptionMess(e), e);
        } finally {
            profiler.leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
        }
    }

    public final LoginDocument process(final LoginRq request) throws ServiceProcessFault, InterruptedException {
        getArte().switchToReadonlyTransaction();//it's readonly (concerning Entity objects) request
        final long sessionId = request.getSessionId();
        final LoginDocument document = LoginDocument.Factory.newInstance();
        final LoginRs response = document.addNewLogin().addNewLoginRs();
        final boolean isPasswordAuth = request.getAuthType() == EAuthType.PASSWORD;
        final X509Certificate certificate;
        final String serverKey;
        final String challenge;
        final String userName;
        final boolean isInteractive;
        final boolean isLogonTimeOK;
        final boolean isSessionsLimitExceed;
        final int sessionsLimit;
        getArte().getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        try {
            selectSessionQry.setLong(1, sessionId);
            try (ResultSet rs = selectSessionQry.executeQuery()) {
                if (rs.next()) {
                    if (!isPasswordAuth) {
                        final Blob userCertificate = rs.getBlob("USERCERTIFICATE");
                        try {
                            certificate
                                    = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(userCertificate.getBinaryStream());
                        } catch (CertificateException | SQLException exception) {
                            throw new ServiceProcessServerFault(ExceptionEnum.INVALID_CERTIFICATE.toString(), "Unable to check client certifiacate", null, null);
                        } finally {
                            userCertificate.free();
                        }
                        challenge = rs.getString("CHALLENGE");
                        serverKey = rs.getString("SERVERKEY");
                    } else {
                        certificate = null;
                        challenge = null;
                        serverKey = null;
                    }
                    isLogonTimeOK = rs.getInt("LOGON_TIME_OK") > 0;
                    isInteractive = rs.getInt("ISINTERACTIVE") > 0;
                    if (isInteractive) {
                        isSessionsLimitExceed = rs.getInt("SESSIONSLIMIT_OK") == 0;
                        sessionsLimit = rs.getInt("SESSIONSLIMIT");
                        userName = rs.getString("USERNAME");
                    } else {
                        isSessionsLimitExceed = false;
                        sessionsLimit = -1;
                        userName = null;
                    }
                } else {
                    throw EasFaults.newSessionDoesNotExist(SessionRestorePolicy.PASSWORD_MUST_BE_ENTERED);
                }
            }
        } catch (SQLException e) {
            throw EasFaults.exception2Fault(getArte(), e, "Can't execute a database query");
        } finally {
            getArte().getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        }
        final String newServerKey;
        if (!isPasswordAuth) {
            if (isInteractive) {
                verifySignature(request.getSignedChallenge(), certificate, challenge);
                response.setEncKey(Hex.decode(serverKey));
                newServerKey = null;
            } else {
                try {
                    presenter.checkPwd(userName, serverKey, challenge, Hex.encode(request.getPwdToken()), false);
                } catch (SQLException ex) {
                    throw EasFaults.exception2Fault(getArte(), ex, "Can't execute a database query");
                }
                final byte[] key = new byte[16];
                new SecureRandom().nextBytes(key);
                response.setEncKey(key);
                newServerKey = Hex.encode(key);
            }
        } else {
            newServerKey = null;
        }
        if (!isLogonTimeOK) {
            getArte().getTrace().put(Messages.MLS_ID_LOGON_TIME_EXPIRED, new ArrStr(userName, presenter.getCurRqStationName(), String.valueOf(getArte().getArteSocket().getRemoteAddress())));
            throw EasFaults.newLogonTimeRestrictionViolationFault();
        }
        if (isInteractive) {
            final List<String> encryptedSessionIds;
            if (request.getSessionsToTerminate() != null
                    && request.getSessionsToTerminate().getEncryptedSessionIdList() != null) {
                encryptedSessionIds = request.getSessionsToTerminate().getEncryptedSessionIdList();
            } else {
                encryptedSessionIds = Collections.emptyList();
            }

            final int maxSessionsNumber = isSessionsLimitExceed ? sessionsLimit : -1;

            if (maxSessionsNumber > 0 || !encryptedSessionIds.isEmpty()) {
                presenter.checkNumberOfUserSessions(userName, maxSessionsNumber, encryptedSessionIds);
            }
        }
        updateSession(sessionId, isPasswordAuth, newServerKey);
        if (isInteractive) {
            try {
                presenter.udpateUserLastLogonTime(userName);
            } catch (SQLException ex) {
                throw EasFaults.exception2Fault(getArte(), ex, "Can't execute a database query");
            }
        }
        return document;
    }

    private void updateSession(final long sessionId, final boolean passwordAuth, final String newServerKey) throws InterruptedException {
        final PreparedStatement updateSessionQry;
        getArte().getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
        try {
            if (passwordAuth) {
                updateSessionQry = getArte().getDbConnection().get().prepareStatement(
                        "declare pragma autonomous_transaction; "//autonomous transaction is used to prevent deadlock on unregisterSession
                        + "begin update RDX_EASSESSION set USERCERTIFICATE = NULL where RDX_EASSESSION.ID = ?; commit; end;");
            } else {
                if (newServerKey == null) {
                    updateSessionQry = getArte().getDbConnection().get().prepareStatement(
                            "declare pragma autonomous_transaction; "//autonomous transaction is used to prevent deadlock on unregisterSession
                            + "begin update RDX_EASSESSION set USERCERTIFICATE = EMPTY_BLOB() where RDX_EASSESSION.ID = ?; commit; end;");
                } else {
                    updateSessionQry = getArte().getDbConnection().get().prepareStatement(
                            "declare pragma autonomous_transaction; "//autonomous transaction is used to prevent deadlock on unregisterSession
                            + "begin update RDX_EASSESSION set USERCERTIFICATE = EMPTY_BLOB(), SERVERKEY = ? where RDX_EASSESSION.ID = ?; commit; end;");
                }
            }
        } catch (SQLException e) {
            throw EasFaults.exception2Fault(getArte(), e, "Can't create a database query");
        } finally {
            getArte().getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
        }
        getArte().getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        try {
            if (newServerKey == null) {
                updateSessionQry.setLong(1, sessionId);
            } else {
                updateSessionQry.setString(1, newServerKey);
                updateSessionQry.setLong(2, sessionId);
            }
            if (updateSessionQry.executeUpdate() != 1) {
                throw new ServiceProcessServerFault(ExceptionEnum.SERVER_MALFUNCTION.toString(), "Can't update EAS session", null, null);
            }
        } catch (SQLException e) {
            final String preprocessedExStack = ExceptionTextFormatter.exceptionStackToString(e);
            throw new ServiceProcessServerFault(ExceptionEnum.SERVER_MALFUNCTION.toString(), "Can't update EAS session: " + ExceptionTextFormatter.getExceptionMess(e), e, preprocessedExStack);
        } finally {
            getArte().getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
            closeQry(updateSessionQry);
        }
    }

    private void verifySignature(final byte[] signature, final X509Certificate certificate, final String challengeHex) {
        boolean verified = false;
        try {
            verified = CertificateUtils.verifySignature(signature, challengeHex.getBytes(), certificate);
        } catch (CMSException exception) {
            verified = false;
        } catch (OperatorCreationException exception) {
            throw new ServiceProcessServerFault(ExceptionEnum.INVALID_CERTIFICATE.toString(), "Unable to check client certifiacate", null, null);
        }
        if (!verified) {
            try {//Internet Explorer encodes text to UTF-16LE before make signature
                verified = CertificateUtils.verifySignature(signature, challengeHex.getBytes("UTF-16LE"), certificate);
            } catch (CMSException | OperatorCreationException | UnsupportedEncodingException exception) {
                throw new ServiceProcessServerFault(ExceptionEnum.INVALID_CERTIFICATE.toString(), "Signature verification failure", null, null);
            }
        }
        if (!verified) {
            throw new ServiceProcessServerFault(ExceptionEnum.INVALID_CERTIFICATE.toString(), "Signature was not verified", null, null);
        }
    }

    @Override
    void prepare(final XmlObject message) throws ServiceProcessServerFault, ServiceProcessClientFault {
        prepare(((LoginMess) message).getLoginRq());
    }

    @Override
    XmlObject process(final XmlObject message) throws ServiceProcessFault, InterruptedException {
        final LoginDocument document = process(((LoginMess) message).getLoginRq());
        postProcess(message, document.getLogin().getLoginRs());
        return document;

    }

    private static void closeQry(final PreparedStatement qry) {
        try {
            qry.close();
        } catch (SQLException ex) {
            //do nothing
        }
    }
}
