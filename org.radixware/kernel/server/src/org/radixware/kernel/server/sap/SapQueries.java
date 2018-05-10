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

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import org.apache.commons.io.IOUtils;

import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.sc.WsdlSource;
import org.radixware.kernel.server.SrvRunParams;
import org.radixware.kernel.server.jdbc.DelegateDbQueries;
import org.radixware.kernel.server.jdbc.Stmt;
import org.radixware.kernel.server.jdbc.IDbQueries;
import org.radixware.kernel.server.jdbc.RadixConnection;

public final class SapQueries {
    
    private static final String qrySetDbActiveStateStmtSQL = "update rdx_sap set isActive = ?, selfchecktime = systimestamp, selfCheckTimeMillis = RDX_UTILS.getUnixEpochMillis(), systemInstanceId=? where id = ?";
    private static final Stmt qrySetDbActiveStateStmt = new Stmt(qrySetDbActiveStateStmtSQL,Types.INTEGER,Types.BIGINT,Types.BIGINT);
    
    private static final String qryReadWsdlServiceStmtSQL = "select serviceWsdl from rdx_sap where id=?";
    private static final Stmt qryReadWsdlServiceStmt = new Stmt(qryReadWsdlServiceStmtSQL,Types.BIGINT);

    private static final String qryReadWsdlSchemeStmtSQL = "select scheme from rdx_sb_datascheme where uri=?";
    private static final Stmt qryReadWsdlSchemeStmt = new Stmt(qryReadWsdlSchemeStmtSQL,Types.VARCHAR);

    private final IDbQueries delegate = new DelegateDbQueries(this, null);
    
    private SapQueries(){        
    }

    public static void setDbActiveState(final long sapId, final Connection db, final boolean isActive) throws SQLException {
        if (db == null) {
            return;
        }
        else {
            try (PreparedStatement qry = ((RadixConnection)db).prepareStatement(qrySetDbActiveStateStmt)) {
                qry.setInt(1, isActive ? 1 : 0);
                qry.setLong(2, SrvRunParams.getInstanceId());
                qry.setLong(3, sapId);
                if (qry.executeUpdate() == 0) {
                    throw new RadixError("SAP #" + String.valueOf(sapId) + " is not defined");
                }
            }
        }
    }

    public static byte[] readWsdlData(final WsdlSource source, final Connection db) throws SQLException {
        if (source == null) {
            throw new IllegalArgumentException("Source type can't be null!");
        }
        else if (db == null) {
            return null;
        }
        else {
            switch (source.getType()) {
                case SAP :
                    try (final PreparedStatement st = ((RadixConnection)db).prepareStatement(qryReadWsdlServiceStmt)) {
                        st.setLong(1, source.getSapId());
                        return loadData(st,"rdx_sap.serviceWsdl");
                    }
                case DATASCHEME_TABLE :
                    try (final PreparedStatement st = ((RadixConnection)db).prepareStatement(qryReadWsdlSchemeStmt)) {
                        st.setString(1, source.getServiceWsdlUri());
                        return loadData(st,"rdx_sb_datascheme.scheme");
                    }
                default :
                    throw new IllegalArgumentException("Unsupported source type: " + source.getType());
            }
        }
    }

    public static void setDbSelfCheck(final long sapId, final java.sql.Connection db) throws SQLException {
        setDbActiveState(sapId, db, true);
    }

    private static byte[] loadData(final PreparedStatement st, final String errorDetails) throws SQLException {
        try (ResultSet rs = st.executeQuery()) {
            if (rs.next()) {
                try{
                    return IOUtils.toByteArray(rs.getClob(1).getCharacterStream(), "UTF-8");
                } catch (IOException ex) {
                    throw new SQLException("Unable to convert data from clob to UTF-8 stream when reading "+errorDetails+": "+ex.getMessage());
                }
            } else {
                return null;
            }
        }
    }    
}
