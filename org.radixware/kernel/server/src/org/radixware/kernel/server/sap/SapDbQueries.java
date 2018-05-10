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
import java.sql.Statement;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.SrvRunParams;
import org.radixware.kernel.server.jdbc.AbstractDbQueries;

final class SapDbQueries extends AbstractDbQueries {

    private final Sap sap;

    private SapDbQueries() {
        this(null);
    }

    SapDbQueries(final Sap sap) {
        this.sap = sap;
    }
    private PreparedStatement qrySetDbActiveState = null;
    private static final String qrySetDbActiveStateSQL = "update rdx_sap set isActive = ?, selfchecktime = systimestamp, selfCheckTimeMillis=RDX_UTILS.getUnixEpochMillis(), systemInstanceId = ? where id = ?";

    private ReadSapOptionsQuery qryReadSapOpts = null;

    void setDbActiveState(final boolean isActive) throws SQLException {
        if (qrySetDbActiveState == null) {
            qrySetDbActiveState = prepareQuery(qrySetDbActiveStateSQL);
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
            closeStatement(qrySetDbActiveState);
        } finally {
            qrySetDbActiveState = null;
        }

        if (qryReadSapOpts != null) {
            try {
                qryReadSapOpts.close();
            } finally {
                qryReadSapOpts = null;
            }
        }
    }

    @Override
    public void prepareAll() throws SQLException {
        prepareAll(sap.getDbConnection());
    }

    private PreparedStatement prepareQuery(final String query) throws SQLException {
        return prepareQuery(sap.getDbConnection(), query);
    }

    private void closeStatement(final Statement st) {
        try {
            if (st != null) {
                st.close();
            }
        } catch (SQLException e) {
            final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
            sap.getTracer().put(EEventSeverity.WARNING, Messages.ERR_ON_DB_CONNECTION_CLOSE + ": " + exStack, Messages.MLS_ID_ERR_ON_DB_CONNECTION_CLOSE, new ArrStr(exStack), false);
        }
    }
}
