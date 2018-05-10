/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.server.instance.aadc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.radixware.kernel.common.types.Pid;
import org.radixware.kernel.server.exceptions.LockTimeoutError;

/**
 *
 * @author dsafonov
 */
public class AadcHelper {

    //
    private PreparedStatement curScnQry;
    private PreparedStatement aadcStateQry;
    private final AadcManager aadcManager;

    public AadcHelper(AadcManager aadcManager) {
        this.aadcManager = aadcManager;
    }

    public long acquireSessionLock(final Pid pid, final int timeoutSec, final Connection dbConnection) throws Exception {
        return doAcquireLock(pid, AadcManager.LOCK_TYPE_SESS, timeoutSec, dbConnection);
    }

    private long doAcquireLock(final Pid pid, final byte lockType, final int timeoutSec, final Connection dbConnection) throws Exception {
        final long curNanos = System.nanoTime();
        do {
            final Long lockId = aadcManager.acquireLock(pid, lockType, getReceivedScn(dbConnection), null);
            if (lockId != null) {
                return lockId;
            }
            Thread.sleep(500);
        } while (System.nanoTime() < curNanos + timeoutSec * 1000000000l);
        throw new LockTimeoutError("Timeout while acquiring lock for " + pid.toStr(), null);
    }

    public void commitSessionalLock(final Pid lockKey, final long lockId, final Connection connection) throws Exception {
        commitLock(lockKey, AadcManager.LOCK_TYPE_SESS, lockId, connection);
    }

    private void commitLock(final Pid lockKey, final byte lockType, final long lockId, final Connection connection) throws Exception {
        aadcManager.commitLock(lockKey, lockType, lockId, getCurrentScn(connection));
    }

    long getReceivedScn(final Connection dbConnection) throws SQLException {
        if (dbConnection == null || dbConnection.isClosed()) {
            throw new SQLException("Unable to get last received SCN: no database connection");
        }
        if (aadcStateQry != null
                && (aadcStateQry.isClosed()
                || aadcStateQry.getConnection().isClosed()
                || aadcStateQry.getConnection() != dbConnection)) {
            try {
                aadcStateQry.close();
            } catch (Throwable t) {
                //ignore
            } finally {
                aadcStateQry = null;
            }
        }
        if (aadcStateQry == null) {
            aadcStateQry = dbConnection.prepareStatement("select max(AADCMYSCN) from rdx_instance where aadcmemberid <> ?");
            aadcStateQry.setLong(1, aadcManager.getMemberId());
        }
        try (final ResultSet rs = aadcStateQry.executeQuery()) {
            rs.next();
            long result = rs.getLong(1);
            if (rs.wasNull()) {
                return Integer.MIN_VALUE;
            }
            return result;
        }
    }

    private long getCurrentScn(final Connection dbConnection) throws SQLException {
        if (curScnQry != null
                && (curScnQry.isClosed()
                || curScnQry.getConnection().isClosed()
                || curScnQry.getConnection() != dbConnection)) {
            try {
                curScnQry.close();
            } catch (Throwable t) {
                //ignore
            } finally {
                curScnQry = null;
            }
        }
        if (curScnQry == null) {
            curScnQry = dbConnection.prepareStatement("select current_scn from GV$DATABASE");
        }
        try (final ResultSet rs = curScnQry.executeQuery()) {
            rs.next();
            return rs.getLong(1);
        }
    }

}
