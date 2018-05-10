package org.radixware.kernel.server.instance.aadc;

class AffinityCacheVal {

    private static final int DEFAULT_EXPIRATION = 60000;

    private final int memberId;
    private final long expirationTime;
    private final boolean confirmed;

    AffinityCacheVal(int memberId, Long expirationDuration, final boolean confirmed) {
        this.memberId = memberId;
        this.expirationTime = System.currentTimeMillis() + (expirationDuration == null ? DEFAULT_EXPIRATION : expirationDuration);
        this.confirmed = confirmed;
    }

    public long getExpirationTime() {
        return expirationTime;
    }

    public int getMemberId() {
        return memberId;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

}
