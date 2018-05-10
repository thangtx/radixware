/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.server.arte.services.eas;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.exceptions.ServiceProcessClientFault;
import org.radixware.schemas.easWsdl.GetDatabaseInfoDocument;
import org.radixware.schemas.eas.GetDatabaseInfoMess;
import org.radixware.schemas.eas.GetDatabaseInfoRs;
import org.radixware.kernel.common.exceptions.ServiceProcessFault;
import org.radixware.kernel.common.exceptions.ServiceProcessServerFault;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;


public class GetDatabaseInfoRequest extends SessionRequest{
    
    GetDatabaseInfoRequest(final ExplorerAccessService presenter){
        super(presenter);
    }
    
    private GetDatabaseInfoDocument process() {
        getArte().switchToReadonlyTransaction();
        final GetDatabaseInfoDocument doc = GetDatabaseInfoDocument.Factory.newInstance();
        final GetDatabaseInfoRs databaseInfo = doc.addNewGetDatabaseInfo().addNewGetDatabaseInfoRs();
        final DatabaseMetaData dbMeta;
        try {
            dbMeta = getArte().getDbConnection().get().getMetaData();
        } catch (SQLException exception) {
            traceGetDbInfoException("database information", exception);
            return doc;
        }
        try {
            databaseInfo.setProductName(dbMeta.getDatabaseProductName());
        } catch (SQLException exception) {
            traceGetDbInfoException("database product name", exception);
        }
        try {
            databaseInfo.setProductVersion(dbMeta.getDatabaseProductVersion());
        } catch (SQLException exception) {
            traceGetDbInfoException("database product version", exception);
        }
        try {
            databaseInfo.setDriverName(dbMeta.getDriverName());
        } catch (SQLException exception) {
            traceGetDbInfoException("database driver name", exception);
        }
        try {
            databaseInfo.setDriverVersion(dbMeta.getDriverVersion());
        } catch (SQLException exception) {
            traceGetDbInfoException("database driver version", exception);
        }
        return doc;
    }
    
    private void traceGetDbInfoException(final String name, final SQLException exception) {
        final StringBuilder traceMessage = new StringBuilder("Failed to get ");
        traceMessage.append(name);
        traceMessage.append(": ");
        traceMessage.append(ExceptionTextFormatter.getExceptionMess(exception));
        traceMessage.append('\n');
        traceMessage.append(ExceptionTextFormatter.exceptionStackToString(exception));
        getArte().getTrace().put(EEventSeverity.WARNING, traceMessage.toString(), EEventSource.APP_DB);
    }    

    @Override
    XmlObject process(final XmlObject rq) throws ServiceProcessFault, InterruptedException {
        GetDatabaseInfoDocument doc = null;
        try{
            doc = process();
        }finally{
            postProcess(rq, doc==null ? null : doc.getGetDatabaseInfo().getGetDatabaseInfoRs());
        }
        return doc;
    }

    @Override
    void prepare(final XmlObject rqXml) throws ServiceProcessServerFault, ServiceProcessClientFault {
        super.prepare(rqXml);
        prepare(((GetDatabaseInfoMess)rqXml).getGetDatabaseInfoRq());
    }
    
    

}
