package org.radixware.kernel.server.instance.aadc;

class CheckedObjCacheVal {
    private final long lastCheckTime;
    
    CheckedObjCacheVal(long lct) {
        this.lastCheckTime = lct;
    }

    long getLastCheckTime() {
        return lastCheckTime;
    }
}
