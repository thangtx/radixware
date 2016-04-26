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
import org.apache.commons.io.IOUtils;

import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.sc.EWsdlSourceType;
import org.radixware.kernel.common.sc.WsdlSource;
import org.radixware.kernel.server.SrvRunParams;

public final class SapQueries {

    public static void setDbActiveState(final long sapId, final Connection db, final boolean isActive) throws SQLException {
        if (db == null) {
            return;
        }
        try (PreparedStatement qry = db.prepareStatement("update rdx_sap set isActive = ?, selfchecktime = systimestamp, systemInstanceId=? where id = ?")) {
            qry.setInt(1, isActive ? 1 : 0);
            qry.setLong(2, SrvRunParams.getInstanceId());
            qry.setLong(3, sapId);
            if (qry.executeUpdate() == 0) {
                throw new RadixError("SAP #" + String.valueOf(sapId) + " is not defined");
            }
        }
    }

    public static byte[] readWsdlData(final WsdlSource source, final Connection db) throws SQLException {
        if (db == null) {
            return null;
        }
        PreparedStatement st = null;
        try {
            if (source.getType() == EWsdlSourceType.SAP) {
                st = db.prepareStatement("select serviceWsdl from rdx_sap where id=?");
                st.setLong(1, source.getSapId());
            } else if (source.getType() == EWsdlSourceType.DATASCHEME_TABLE) {
                st = db.prepareStatement("select scheme from rdx_sb_datascheme where uri=?");
                st.setString(1, source.getServiceWsdlUri());
            } else {
                throw new IllegalArgumentException("Unsupported source type: " + source.getType());
            }
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    try {
                        return IOUtils.toByteArray(rs.getClob(1).getCharacterStream(), "UTF-8");
                    } catch (IOException ex) {
                        throw new SQLException("Unable to convert data from clob to UTF-8 stream");
                    }
                } else {
                    return null;
                }
            }
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public static void setDbSelfCheck(final long sapId, final java.sql.Connection db) throws SQLException {
        setDbActiveState(sapId, db, true);
    }
}
