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

package org.radixware.kernel.server.instance;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.exceptions.AlreadyOwnLockError;
import org.radixware.kernel.server.exceptions.DeadLockError;
import org.radixware.kernel.server.exceptions.LockError;

public final class SingletonUnitSessionLock {

    private final Instance instance;
    private final DbQueries dbQueries;

    SingletonUnitSessionLock(final Instance instance) {
        this.instance = instance;
        dbQueries = new DbQueries();

    }
    private final Map<String, Lock> locksByUnitType = new HashMap<>();

    synchronized public final boolean releaseLock(final String unitType) {
        final Lock lock = locksByUnitType.get(unitType);
        if (lock == null) {
            return false;
        }
        if (lock.release()) {
            locksByUnitType.remove(unitType);
            return true;
        }
        return false;
    }

    public void awaitAllLocksRelease() {
        synchronized (this) {
        }
    }

    synchronized public final void releaseAllLocks() {
        for (Lock lock : locksByUnitType.values()) {
            try {
                lock.release();
            } catch (Throwable e) {
                final String exStack = ExceptionTextFormatter.exceptionStackToString(e);
                instance.getTrace().put(EEventSeverity.ERROR, Messages.ERR_ON_SINGL_UNIT_LOCK_RELEASE_ + "\n" + exStack, Messages.MLS_ID_ERR_ON_SINGL_UNIT_LOCK_RELEASE, new ArrStr(instance.getFullTitle(), exStack), EEventSource.INSTANCE, false);
            }
        }
        locksByUnitType.clear();
    }

    synchronized final void closeAllDbQueries() {
        dbQueries.closeAll();
    }

    synchronized public final boolean lock(final String unitType) {
        Lock lock = locksByUnitType.get(unitType);
        if (lock == null) {
            lock = new Lock(unitType);
            locksByUnitType.put(unitType, lock);
        }
        return lock.lock();
    }

    private static class LockStateInfo {

        private final boolean locked;
        private final Connection connection;

        public LockStateInfo(boolean locked, Connection connection) {
            this.locked = locked;
            this.connection = connection;
        }
    }

    private final class Lock {

        final String id;
        LockStateInfo state;

        Lock(final String unitType) {
            id = generateLockId(unitType);
            state = new LockStateInfo(false, null);
        }

        boolean lock() {
            if (state.locked) {
                try {
                    if (!state.connection.isClosed()) {
                        return false;
                    }
                } catch (SQLException ex) {
                    //continue, maybe session is dead
                }
            }
            try {
                final CallableStatement qry = dbQueries.getQryLock();
                qry.setString(2, id);
                qry.execute();
                final int res = qry.getInt(1);
                if (qry.wasNull()) {
                    throw new LockError("Unsupported lock query result: null", null);
                }
                switch (res) {
                    case 0://success
                        state = new LockStateInfo(true, qry.getConnection());
                        break;
                    case 1: //timeout
                        state = new LockStateInfo(false, null);
                        break;
                    case 2:// dead lock
                        throw new DeadLockError("Deadlock", null);
                    case 3:// parameter error
                        throw new LockError("Lock query parameter error", null);
                    case 4://already own lock specified by 'id' or 'lockhandle'
                        throw new AlreadyOwnLockError("Already own lock specified by lock id", null);
                    case 5://illegal lockhandle
                        throw new LockError("Illegal lockhandle", null);
                    default:
                        throw new LockError("Unsupported lock query result: " + String.valueOf(res), null);
                }
            } catch (SQLException e) {
                throw new LockError("Can't do session lock: " + ExceptionTextFormatter.exceptionStackToString(e), e);
            }
            return state.locked;
        }

        boolean release() {
            if (!state.locked) {
                return false;
            }
            if (state.locked) {
                try {
                    if (state.connection.isClosed()) {
                        throw new LockError("Connection on which this lock was acquired is already closed", null);
                    }
                } catch (SQLException ex) {
                    throw new LockError("Error on checking if lock connection is alive", ex);
                }
            }
            final CallableStatement qry = dbQueries.getQryReleaseLock();
            try {
                qry.setString(2, id);
                qry.execute();
                final int res = qry.getInt(1);
                if (qry.wasNull()) {
                    throw new LockError("Unsupported release lock query result: null", null);
                }
                switch (res) {
                    case 0://success
                        state = new LockStateInfo(false, null);
                        return true;
                    case 3:// parameter error
                        throw new LockError("Lock query parameter error", null);
                    case 4://don't own lock specified by 'id' or 'lockhandle'
                        throw new LockError("Don't own lock specified by lock id", null);
                    case 5://illegal lockhandle
                        throw new LockError("Illegal lock id", null);
                    default:
                        throw new LockError("Unsupported release lock query result: " + String.valueOf(res), null);
                }
            } catch (SQLException e) {
                throw new LockError("Can't release session lock: " + ExceptionTextFormatter.exceptionStackToString(e), e);
            }
        }

        private String generateLockId(final String unitType) {
            final CallableStatement qry = dbQueries.getQryAllocateLockId();
            try {
                qry.setString(1, "SingletonUnitType" + String.valueOf(unitType));
                qry.execute();
                return qry.getString(2);
            } catch (SQLException e) {
                throw new LockError("Can't generate session lock id: " + ExceptionTextFormatter.exceptionStackToString(e), e);
            }
        }
    }

    private final class DbQueries {

        private void closeQry(final PreparedStatement qry) {
            try {
                if (qry != null) {
                    qry.close();
                }
            } catch (SQLException e) {
                instance.getTrace().put(EEventSeverity.WARNING, Messages.ERR_ON_DB_CONNECTION_CLOSE + ": \n" + ExceptionTextFormatter.exceptionStackToString(e), null, null, EEventSource.INSTANCE, false);
            }
        }

        void closeAll() {
            closeQry(qryAllocateLockId);
            qryAllocateLockId = null;
            closeQry(qryReleaseLock);
            qryReleaseLock = null;
            closeQry(qryLock);
            qryLock = null;
        }
        private CallableStatement qryAllocateLockId = null;

        final CallableStatement getQryAllocateLockId() {
            if (qryAllocateLockId == null) {
                try {
                    final Connection dbConnection = instance.getDbConnection();
                    if (dbConnection == null) {
                        throw new LockError("Can't allocate lock ID: there is no database connection", null);
                    }
                    qryAllocateLockId = dbConnection.prepareCall(
                            "begin DBMS_LOCK.ALLOCATE_UNIQUE('http://dblock.instance.radixware.org/' || ?, ?); end;");//parameters entityId+pidAsStr in, lockId out
                    qryAllocateLockId.registerOutParameter(2, java.sql.Types.VARCHAR);
                } catch (SQLException e) {
                    throw new LockError(ERR_CANT_INIT_LOCK_DB_QRY_ + ExceptionTextFormatter.exceptionStackToString(e), e);
                }
            }
            return qryAllocateLockId;
        }
        private CallableStatement qryReleaseLock = null;

        final CallableStatement getQryReleaseLock() {
            if (qryReleaseLock == null) {
                try {
                    final Connection dbConnection = instance.getDbConnection();
                    if (dbConnection == null) {
                        throw new LockError("Can't release lock: there is no database connection", null);
                    }
                    qryReleaseLock = dbConnection.prepareCall(
                            "begin ? := DBMS_LOCK.RELEASE(?); end;");//parameters: result out, lockId in
                    qryReleaseLock.registerOutParameter(1, java.sql.Types.INTEGER);
                    //  Return value:
                    // 0 - success
                    // 3 - parameter error
                    // 4 - don't own lock specified by 'id' or 'lockhandle'
                    // 5 - illegal lockhandle
                } catch (SQLException e) {
                    throw new LockError(ERR_CANT_INIT_LOCK_DB_QRY_ + ExceptionTextFormatter.exceptionStackToString(e), e);
                }
            }
            return qryReleaseLock;
        }
        private CallableStatement qryLock = null;

        final CallableStatement getQryLock() {
            if (qryLock == null) {
                try {
                    final Connection dbConnection = instance.getDbConnection();
                    if (dbConnection == null) {
                        throw new LockError("Can't request a lock: there is no database connection", null);
                    }
                    qryLock = dbConnection.prepareCall(
                            "begin ? := DBMS_LOCK.REQUEST(?, DBMS_LOCK.X_MODE, 0 , false); end;");//parameters: result out, lockId in, timeout seconds in
                    qryLock.registerOutParameter(1, java.sql.Types.INTEGER);
                    //  Return value:
                    //    0 - success
                    //    1 - timeout
                    //    2 - deadlock
                    //    3 - parameter error
                    //    4 - already own lock specified by 'id' or 'lockhandle'
                    //    5 - illegal lockhandle
                } catch (SQLException e) {
                    throw new LockError(ERR_CANT_INIT_LOCK_DB_QRY_ + ExceptionTextFormatter.exceptionStackToString(e), e);
                }
            }
            return qryLock;
        }
        private static final String ERR_CANT_INIT_LOCK_DB_QRY_ = "Can\'t init session lock service query: ";
    }
}
