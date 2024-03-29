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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.enums.ETimingSection;
import org.radixware.kernel.common.exceptions.ServiceProcessClientFault;
import org.radixware.kernel.common.exceptions.ServiceProcessFault;
import org.radixware.kernel.common.exceptions.ServiceProcessServerFault;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.exceptions.DatabaseError;
import org.radixware.kernel.server.jdbc.DelegateDbQueries;
import org.radixware.kernel.server.jdbc.Stmt;
import org.radixware.kernel.server.jdbc.IDbQueries;
import org.radixware.kernel.server.jdbc.RadixConnection;
import org.radixware.schemas.eas.CloseSessionMess;
import org.radixware.schemas.eas.CloseSessionRq;
import org.radixware.schemas.eas.ExceptionEnum;
import org.radixware.schemas.easWsdl.CloseSessionDocument;


public class CloseSessionRequest extends SessionRequest {

    private static final String delSessionQryStmtSQL = "delete from RDX_EasSession where id = ?";
    private static final Stmt delSessionQryStmt = new Stmt(delSessionQryStmtSQL,Types.BIGINT);
    
    private PreparedStatement delSessionQry = null;
    
    private final IDbQueries delegate = new DelegateDbQueries(this, null);
            
    private CloseSessionRequest(){}    
    
    public CloseSessionRequest(final ExplorerAccessService presenter) {
        super(presenter);
    }
    
    final CloseSessionDocument process(final CloseSessionRq request) throws ServiceProcessFault, InterruptedException {        
        if (delSessionQry == null) {
            getArte().getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            try{
                delSessionQry = ((RadixConnection)getArte().getDbConnection().get()).prepareStatement(delSessionQryStmt);
            }catch(SQLException e){
                throw new DatabaseError("Can't init EAS service DB query: " + ExceptionTextFormatter.getExceptionMess(e), e);
            }finally{
                getArte().getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            }
        }
        try{
            delSessionQry.setLong(1, Long.valueOf(request.getSessionId()));
            delSessionQry.execute();
        }catch(SQLException e){
            final String preprocessedExStack = ExceptionTextFormatter.exceptionStackToString(e);
            throw new ServiceProcessServerFault(ExceptionEnum.SERVER_MALFUNCTION.toString(), "Can't unregister session: " + ExceptionTextFormatter.getExceptionMess(e), e, preprocessedExStack);
        }
        final CloseSessionDocument resultDoc = CloseSessionDocument.Factory.newInstance();
        resultDoc.addNewCloseSession().addNewCloseSessionRs().setResult("OK");
        return resultDoc;
    }

    @Override
    void prepare(final XmlObject rqXml) throws ServiceProcessServerFault, ServiceProcessClientFault {
        super.prepare(rqXml);
        final CloseSessionRq request = ((CloseSessionMess) rqXml).getCloseSessionRq();                
        if (request.getAuthType() == null) {
            throw EasFaults.newParamRequiedFault("AuthType", rqXml.getDomNode().getNodeName());
        }
        final Long sessionId = Long.valueOf(request.getSessionId());
        final ExplorerAccessService.SessionOptions sessionOptions = 
            presenter.connectToSession(this, sessionId, presenter.getArte().getEffectiveRequestVersion(), request.getPwdToken(), request.getChallenge(), request.getAuthType(), false);
        setSessionOptions(sessionOptions);
    }

    @Override
    XmlObject process(final XmlObject rq) throws ServiceProcessFault, InterruptedException {
        return process(((CloseSessionMess) rq).getCloseSessionRq());
    }
}
