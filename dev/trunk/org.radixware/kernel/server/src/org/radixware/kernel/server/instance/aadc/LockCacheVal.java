package org.radixware.kernel.server.instance.aadc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

public class LockCacheVal {

    static protected class LockInfo {

        final long lockId;
        final long lockTime;
        final String dbTranId;

        public LockInfo(long lockId, long lockTime, String dbTranId) {
            this.lockId = lockId;
            this.lockTime = lockTime;
            this.dbTranId = dbTranId;
        }

    };

    private int memberId;
    final private List<LockInfo> uncommittedLocks;
    private long maxScn;
    long minLockTime;
    long maxLockTime;
    private Map<String, String> payload = null;

    LockCacheVal(int memberId) {
        this.memberId = memberId;
        this.maxScn = Long.MIN_VALUE;
        this.minLockTime = Long.MAX_VALUE;
        this.maxLockTime = Long.MIN_VALUE;
        uncommittedLocks = new ArrayList<>();
    }

    public int getMemberId() {
        return memberId;
    }

    public boolean hasUncommitedLocks() {
        return !uncommittedLocks.isEmpty();
    }

    public boolean hasOldUncommittedLocks(long upperAge) {
        for (LockInfo l : uncommittedLocks) {
            if (l.lockTime < upperAge) {
                return true;
            }
        }
        return false;
    }

    public boolean isCommitted() {
        return maxScn > Long.MIN_VALUE;
    }

    public boolean isFinished() {
        return maxScn == Long.MIN_VALUE && uncommittedLocks.isEmpty();
    }

    public long getMaxScn() {
        return maxScn;
    }

    public long getMinLockTime() {
        return minLockTime;
    }

    public long getMaxLockTime() {
        return maxLockTime;
    }

    long addLock(long lockTime, String dbTranId) {
        long lockId;
        if (uncommittedLocks.isEmpty()) {
            lockId = lockTime; //randomization
        } else {
            lockId = uncommittedLocks.get(uncommittedLocks.size() - 1).lockId + 1; //next seq
        }
        if (minLockTime > lockTime)
            minLockTime = lockTime;
        if (maxLockTime < lockTime)
            maxLockTime = lockTime;
        uncommittedLocks.add(new LockInfo(lockId, lockTime, dbTranId));
        return lockId;
    }

    boolean capture(long receivedScn, int memberId) {
        if (uncommittedLocks.isEmpty() && maxScn <= receivedScn) {
            maxScn = Long.MIN_VALUE;
            this.memberId = memberId;
            return true;
        } else {
            return false;
        }
    }

    void commitLock(long lockId, long curScn) {
        ListIterator<LockInfo> it = uncommittedLocks.listIterator();
        while (it.hasNext()) {
            if (it.next().lockId == lockId) {
                if (maxScn < curScn)
                    maxScn = curScn;
                it.remove();
                break;
            }
        }
    }

    boolean rollbackLock(long lockId) {
        ListIterator<LockInfo> it = uncommittedLocks.listIterator();
        while (it.hasNext()) {
            if (it.next().lockId == lockId) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    /**
     * Purge uncommitted locks: all or only whose database transaction is
     * finished
     */
    boolean purgeInvalidLocks(Set<String> curDbTrans) {
        boolean purged = false;
        ListIterator<LockInfo> it = uncommittedLocks.listIterator();
        while (it.hasNext()) {
            LockInfo l = it.next();
            if (curDbTrans == null || !curDbTrans.contains(l.dbTranId)) {
                it.remove();
                purged = true;
            }
        }
        return purged;
    }

    void putPayload(Map<String, String> pl) {
        if (payload == null) {
            payload = new HashMap<>(pl.size());
        }
        payload.putAll(pl);
    }

    Map<String, String> getPayload() {
        return payload;
    }

}
