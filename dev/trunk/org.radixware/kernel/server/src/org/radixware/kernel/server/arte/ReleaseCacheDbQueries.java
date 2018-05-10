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
package org.radixware.kernel.server.arte;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashSet;
import java.util.Set;
import org.radixware.kernel.common.enums.ETimingSection;
import org.radixware.kernel.server.jdbc.DelegateDbQueries;
import org.radixware.kernel.server.jdbc.Stmt;
import org.radixware.kernel.server.jdbc.IDbQueries;
import org.radixware.kernel.server.jdbc.RadixConnection;

final class ReleaseCacheDbQueries {

    private static final String qryAreVersionIndicesDeployedStmtSQL = "begin ?:= RDX_ADS_META.areVersionIndicesDeployed(?); end;";
    private static final Stmt qryAreVersionIndicesDeployedStmt = new Stmt(new Stmt(qryAreVersionIndicesDeployedStmtSQL,Types.BIGINT,Types.BIGINT),1);

    private final IDbQueries delegate = new DelegateDbQueries(this, null);
    private final ReleaseCache release;

    private ReleaseCacheDbQueries() {
        this.release = null;
    }
    
    ReleaseCacheDbQueries(final ReleaseCache release) {
        this.release = release;
    }
    private CallableStatement qryVersionIndicesDeployed = null;
    private final Set<Long> versionsWithDeployedIndices = new HashSet<>();

    boolean isVersionIndicesDeployed() throws SQLException {
        final Long curVer = Long.valueOf(release.getRelease().getVersion());
        if (qryVersionIndicesDeployed == null) {
            release.getArte().getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            try {
                qryVersionIndicesDeployed = ((RadixConnection)release.getArte().getDbConnection().get()).prepareCall(qryAreVersionIndicesDeployedStmt);
                qryVersionIndicesDeployed.registerOutParameter(1, java.sql.Types.INTEGER);
            } finally {
                release.getArte().getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            }
        } else if (versionsWithDeployedIndices.contains(curVer)) {
            //RADIX-3027: there is no need to check it again
            //as ARTE is going to use the same dbConnection till it dyes
            return true;
        }
        release.getArte().getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        try {
            qryVersionIndicesDeployed.setLong(2, curVer.longValue());
            qryVersionIndicesDeployed.execute();
        } finally {
            release.getArte().getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
        }
        if (qryVersionIndicesDeployed.getLong(1) != 0) {
            //RADIX-3027
            if (versionsWithDeployedIndices.size() > 100) //to prevent memory leak
            {
                versionsWithDeployedIndices.clear();
            }
            versionsWithDeployedIndices.add(curVer);
            return true;
        } else {
            return false;
        }
    }

    void close() {
        if (qryVersionIndicesDeployed != null) {
            try {
                qryVersionIndicesDeployed.close();
            } catch (SQLException ex) {
                //do nothing
            } finally {
                qryVersionIndicesDeployed = null;
            }
        }
    }
}
