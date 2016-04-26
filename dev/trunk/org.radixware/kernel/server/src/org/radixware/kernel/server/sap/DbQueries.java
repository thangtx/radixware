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

package org.radixware.kernel.server.sap;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.IDbQueries;
import org.radixware.kernel.server.SrvRunParams;


final class DbQueries implements IDbQueries {

    private final Sap sap;

    DbQueries(final Sap sap) {
        this.sap = sap;
    }
    private PreparedStatement qrySetDbActiveState = null;
    private ReadSapOptionsQuery qryReadSapOpts = null;

    void setDbActiveState(final boolean isActive) throws SQLException {
        if (qrySetDbActiveState == null) {
            qrySetDbActiveState = sap.getDbConnection().prepareStatement("update rdx_sap set isActive = ?, selfchecktime = systimestamp, systemInstanceId = ? where id = ?");
        }
        qrySetDbActiveState.setInt(1, isActive ? 1 : 0);
        qrySetDbActiveState.setInt(2, SrvRunParams.getInstanceId());
        qrySetDbActiveState.setLong(3, sap.getId());
        if (qrySetDbActiveState.executeUpdate() == 0) {
            throw new RadixError("SAP #" + String.valueOf(sap.getId()) + " is not defined");
        } else {
            sap.getDbConnection().commit();
        }
    }

    public void setDbSelfCheck() throws SQLException {
        setDbActiveState(true);
    }

    SapOptions readOptions() throws SQLException {
        if (qryReadSapOpts == null) {
            qryReadSapOpts = new ReadSapOptionsQuery(sap.getDbConnection());
        }
        return qryReadSapOpts.readOptions(sap.getId());
    }

    @Override
    public void closeAll() {
        try {
            if (qrySetDbActiveState != null) {
                qrySetDbActiveState.close();
            }
        } catch (SQLException e) {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            sap.getTracer().put(EEventSeverity.WARNING, Messages.ERR_ON_DB_CONNECTION_CLOSE + ": " + exStack, Messages.MLS_ID_ERR_ON_DB_CONNECTION_CLOSE, new ArrStr(exStack), false);
        }
        qrySetDbActiveState = null;
        if (qryReadSapOpts != null) {
            qryReadSapOpts.close();
            qryReadSapOpts = null;
        }
    }
}
