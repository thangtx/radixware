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
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.server.exceptions.AlreadyOwnLockError;
import org.radixware.kernel.server.exceptions.DeadLockError;
import org.radixware.kernel.server.exceptions.LockError;
import org.radixware.kernel.server.exceptions.LockTimeoutError;
import org.radixware.kernel.server.types.Pid;

public final class SessionLock extends Object {

    private final Arte arte;
    private final DbQueries dbQueries;

    SessionLock(final Arte arte) {
        this.arte = arte;
        dbQueries = new DbQueries();
    }
    private final Map<Pid, Lock> locksByPid = new HashMap<Pid, Lock>();

    final void closeAllDbQueries() {
        dbQueries.closeAll();
    }

    public final void releaseLock(final Pid pid) {
        final Lock lock = locksByPid.get(pid);
        if (lock == null) {
            return;
        }
        if (lock.release(false)) {
            locksByPid.remove(pid);
        }
    }

    public final void releaseAllLocks() {
        for (Lock lock : locksByPid.values()) {
            try {
                lock.release(true);
            } catch (Throwable e) {
                arte.getTrace().put(EEventSeverity.ERROR, "Can't release session lock:\n" + arte.getTrace().exceptionStackToString(e), EEventSource.ARTE_DB);
            }
        }
        locksByPid.clear();
    }

    public boolean isLocked(final Pid pid) {
        final Lock lock = locksByPid.get(pid);
        return (lock != null) && lock.isLocked();
    }

    public final void lock(final Pid pid, final Long timeoutSec) {
        Lock lock = locksByPid.get(pid);
        if (lock == null) {
            lock = new Lock(pid);
            locksByPid.put(pid, lock);
        }
        lock.lock(timeoutSec);
    }

    private final class Lock {

        String id = null;
        final Pid pid;
        int nesting;

        Lock(final Pid pid) {
            this.pid = pid;
            nesting = 0;
        }

        boolean isLocked() {
            return nesting > 0 && id != null;
        }

        void lock(final Long timeoutSec) {
            nesting++;
            if (nesting == 1) {
                try {
                    final String pidPres = pid.getEntityId() + "->" + pid.toString();
                    arte.getTrace().put(EEventSeverity.DEBUG, "Requesting session lock for " + pidPres, EEventSource.ARTE);
                    //result out,  tabId, pid, timeout seconds, lockId out
                    final CallableStatement qry = dbQueries.getQryLock();
                    qry.setString(2, pid.getEntityId().toString());
                    qry.setString(3, pid.toString());
                    if (timeoutSec == null) {
                        qry.setNull(4, java.sql.Types.INTEGER);
                    } else {
                        qry.setLong(4, timeoutSec.longValue());
                    }
                    qry.execute();
                    final int res = qry.getInt(1);
                    if (qry.wasNull()) {
                        throw new LockError("Unsupported lock query result: null", null);
                    }
                    switch (res) {
                        case 0://success
                            id = qry.getString(5);
                            arte.getTrace().put(EEventSeverity.DEBUG, "Object " + pidPres + " is locked via session lock", EEventSource.ARTE);
                            return;
                        case 1: //timeout
                            throw new LockTimeoutError("Lock timeout exceeded", null);
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
                    throw new LockError("Can't do session lock: " + arte.getTrace().exceptionStackToString(e), e);
                }

            }
        }

        boolean release(final boolean bForce) {
            if (!isLocked()) {
                return true;
            }
            if (bForce) {
                nesting = 0;
            } else {
                nesting--;
            }
            if (nesting == 0) {
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
                    throw new LockError("Can't release session lock: " + arte.getTrace().exceptionStackToString(e), e);
                }
            }
            return false;
        }
    }

    private final class DbQueries {

        private void closeQry(final CallableStatement qry) {
            try {
                if (qry != null) {
                    qry.close();
                }
            } catch (SQLException ex) {
                //do nothing
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
        }

        void closeAll() {
            closeQry(qryReleaseLock);
            qryReleaseLock = null;
            closeQry(qryLock);
            qryLock = null;
        }
        private CallableStatement qryLock = null;

        private final CallableStatement getQryLock() {
            if (qryLock == null) {
                try {
                    qryLock = arte.getDbConnection().get().prepareCall(
                            "begin "
                            + "? := RDX_LOCK.REQUEST(?, ?, ?, ?);"
                            + " end;");//parameters: result out,  tabId, pid, timeout seconds, lockId out
//                        "begin ? := DBMS_LOCK.REQUEST(?, DBMS_LOCK.X_MODE, ? , false); end;"
//                    );//parameters: result out, lockId in, timeout seconds in
                    qryLock.registerOutParameter(1, java.sql.Types.INTEGER);
                    qryLock.registerOutParameter(5, java.sql.Types.VARCHAR);
                    //  Return value:
                    //    0 - success
                    //    1 - timeout
                    //    2 - deadlock
                    //    3 - parameter error
                    //    4 - already own lock specified by 'id' or 'lockhandle'
                    //    5 - illegal lockhandle
                } catch (SQLException e) {
                    throw new LockError(ERR_CANT_INIT_DB_QRY_ + arte.getTrace().exceptionStackToString(e), e);
                }
            }
            return qryLock;
        }
        private static final String ERR_CANT_INIT_DB_QRY_ = "Can\'t init session lock service query: ";
        private CallableStatement qryReleaseLock = null;

        private final CallableStatement getQryReleaseLock() {
            if (qryReleaseLock == null) {
                try {
                    qryReleaseLock = arte.getDbConnection().get().prepareCall(
                            "begin ? := RDX_LOCK.RELEASE(?); end;");//parameters: result out, lockId in
                    qryReleaseLock.registerOutParameter(1, java.sql.Types.INTEGER);
                    //  Return value:
                    // 0 - success
                    // 3 - parameter error
                    // 4 - don't own lock specified by 'id' or 'lockhandle'
                    // 5 - illegal lockhandle
                } catch (SQLException e) {
                    throw new LockError(ERR_CANT_INIT_DB_QRY_ + arte.getTrace().exceptionStackToString(e), e);
                }
            }
            return qryReleaseLock;
        }
    }
}
