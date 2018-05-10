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

import java.sql.*;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.auth.AuthUtils;
import org.radixware.kernel.common.auth.PasswordHash;
import org.radixware.kernel.common.enums.EAuthType;
import org.radixware.kernel.common.enums.ETimingSection;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.enums.OracleTypeNames;

import org.radixware.kernel.common.exceptions.ServiceProcessClientFault;
import org.radixware.kernel.common.exceptions.ServiceProcessFault;
import org.radixware.kernel.common.exceptions.ServiceProcessServerFault;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.Hex;
import org.radixware.kernel.server.dbq.DbQuery;
import org.radixware.kernel.server.jdbc.DelegateDbQueries;
import org.radixware.kernel.server.jdbc.Stmt;
import org.radixware.kernel.server.jdbc.IDbQueries;
import org.radixware.kernel.server.jdbc.RadixConnection;
import org.radixware.schemas.eas.ChangePasswordMess;
import org.radixware.schemas.eas.ChangePasswordRq;
import org.radixware.schemas.eas.ExceptionEnum;
import org.radixware.schemas.easWsdl.ChangePasswordDocument;

final class ChangePasswordRequest extends SessionRequest {

    private static final String qryReadCurPwdHashStmtSQL = "select u.pwdHash, u.pwdHashHistory, "
                                                         + "(case when u.authTypes is NULL or u.authTypes like '%" + EAuthType.PASSWORD.getValue() + "%' then 1 else 0 end) CAN_CHANGE_PASSWORD, "
                                                         + "s.uniquePwdSeqLen  "
                                                         + "from rdx_ac_user u, rdx_system s where u.name = ? and s.id = 1";
    private static final Stmt qryReadCurPwdHashStmt = new Stmt(qryReadCurPwdHashStmtSQL,Types.VARCHAR);

    private static final String qryUpdatePwdStmtSQL = "update RDX_AC_USER set PWDHASH = ?, PWDHASHHISTORY = ?, PWDHASHALGO = ?, TEMPORARYPWDSTARTTIME = null where NAME = ?";
    private static final Stmt qryUpdatePwdStmt = new Stmt(qryUpdatePwdStmtSQL,Types.VARCHAR,Types.CLOB,Types.VARCHAR,Types.VARCHAR);
            
    private final IDbQueries delegate = new DelegateDbQueries(this, null);
            
    private ChangePasswordRequest(){}
    
    ChangePasswordRequest(final ExplorerAccessService presenter) {
        super(presenter);
    }

    public final ChangePasswordDocument process(final ChangePasswordMess request) throws ServiceProcessServerFault, ServiceProcessClientFault, InterruptedException {
        getArte().switchToReadonlyTransaction();//it's readonly (concerning Entity objects) request
        final ChangePasswordRq rqParams = request.getChangePasswordRq();
        final byte[] newPwdHashCryptogram = rqParams.getNewPwdHash();
        try {
            getArte().getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            final PreparedStatement qryReadCurPwdHash;
            try {
                qryReadCurPwdHash = ((RadixConnection)getArte().getDbConnection().get()).prepareStatement(qryReadCurPwdHashStmt);
            } finally {
                getArte().getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            }
            try {
                final ResultSet rs;
                getArte().getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
                try {
                    qryReadCurPwdHash.setString(1, getArte().getUserName());
                    rs = qryReadCurPwdHash.executeQuery();
                } finally {
                    getArte().getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
                }
                try {
                    if (!rs.next()) {
                        throw EasFaults.newAccessViolationFault(getArte(), "User \"" + String.valueOf(getArte().getUserName()) + "\" is not registered");
                    }
                    if (rs.getInt("CAN_CHANGE_PASSWORD") == 0) {
                        throw EasFaults.newAccessViolationFault(getArte(), "User \"" + String.valueOf(getArte().getUserName()) + "\" can't change password");
                    }
                    final byte[] curPwdHash = Hex.decode(rs.getString("pwdHash"));
                    ArrStr history = (ArrStr) DbQuery.getFieldVal(getArte(), rs, "pwdHashHistory", EValType.ARR_STR, OracleTypeNames.CLOB);
                    Long uniquePwdSeqLen = rs.getLong("uniquePwdSeqLen");
                    if (rs.wasNull()) {
                        uniquePwdSeqLen = null;
                    }
                    final byte[] newPwdHashData = AuthUtils.decryptNewPwdHash(newPwdHashCryptogram, curPwdHash);
                    final PasswordHash newPwdHash = PasswordHash.Factory.fromBytes(newPwdHashData);                    
                    if (uniquePwdSeqLen != null && history != null) {
                        for (int i = 0; i < uniquePwdSeqLen.longValue() && i < history.size(); i++) {
                            final byte[] oldPwdHash = Hex.decode(history.get(i));
                            for (PasswordHash.Algorithm algo: PasswordHash.Algorithm.values()){
                                if ( Arrays.equals(oldPwdHash, newPwdHash.getBytes(algo)) ){
                                    throw new ServiceProcessServerFault(ExceptionEnum.REPEATATIVE_PASSWORD.toString(), uniquePwdSeqLen.toString(), null, null);
                                }
                            }
                        }
                    }
                    final String newPwdHashString = Hex.encode(newPwdHash.getBytes(PasswordHash.DEFAULT_ALGORITHM));
                    if (uniquePwdSeqLen == null) {
                        history = null;
                    } else {
                        if (history == null) {
                            history = new ArrStr();
                        } else {
                            while (history.size() + 1 > uniquePwdSeqLen.longValue()) {
                                history.remove(history.size() - 1);
                            }
                        }
                        history.add(0, newPwdHashString);
                    }
                    final CallableStatement qryUpdatePwd;
                    getArte().getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
                    try {
                        qryUpdatePwd = ((RadixConnection)getArte().getDbConnection().get()).prepareCall(qryUpdatePwdStmt); //mustChangePwd is set to 0 by trigger
                    } finally {
                        getArte().getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
                    }
                    getArte().getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
                    try {
                        qryUpdatePwd.setString(1, newPwdHashString);
                        DbQuery.setParam(getArte(), qryUpdatePwd, 2, EValType.ARR_STR, OracleTypeNames.CLOB, history, "PWDHASHHISTORY");
                        qryUpdatePwd.setString(3, PasswordHash.DEFAULT_ALGORITHM.getTitle());
                        qryUpdatePwd.setString(4, getArte().getUserName());                        
                        final int updatedCnt = qryUpdatePwd.executeUpdate();
                        if (updatedCnt != 1) {
                            throw new ServiceProcessServerFault(ExceptionEnum.SERVER_MALFUNCTION.toString(), "Wrong number of users been updated: " + updatedCnt, null, null);
                        }
                    } finally {
                        try {
                            qryUpdatePwd.close();
                        } catch (SQLException ex) {
                            //do nothing
                            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                        }
                        getArte().getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
                    }
                } finally {
                    try {
                        rs.close();
                    } catch (SQLException ex) {
                        //do nothing
                        Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                    }
                }
            } finally {
                try {
                    qryReadCurPwdHash.close();
                } catch (SQLException ex) {
                    //do nothing
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }
            }
        } catch (SQLException e) {
            throw EasFaults.exception2Fault(getArte(), e, "Can't execute a database query");
        }
        final ChangePasswordDocument res = ChangePasswordDocument.Factory.newInstance();
        res.addNewChangePassword().addNewChangePasswordRs();
        return res;
    }

    @Override
    void prepare(final XmlObject rqXml) throws ServiceProcessServerFault, ServiceProcessClientFault {
        super.prepare(rqXml);
        prepare(((ChangePasswordMess) rqXml).getChangePasswordRq());
    }

    @Override
    XmlObject process(final XmlObject rq) throws ServiceProcessFault, InterruptedException {
        ChangePasswordDocument doc = null;
        try{
            doc = process((ChangePasswordMess) rq);
        }finally{
            postProcess(rq, doc==null ? null : doc.getChangePassword().getChangePasswordRs());
        }
        return doc;
    }
}
