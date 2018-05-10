package org.radixware.ws.servlet;

class PackageContent {

    private final long timestamp = System.currentTimeMillis();
    private final int ttl;
    private volatile byte[] content;

    PackageContent(final int packageSize, final int ttl) {
        this.content = new byte[packageSize];
        this.ttl = ttl;

        for (int index = 0; index < packageSize; index++) {
            content[index] = (byte) (256 * Math.random());
        }
    }

    synchronized boolean isContentValid() {
        return content != null;
    }

    synchronized boolean ttlExhausted(final long currentTimestamp) {
        return currentTimestamp > timestamp + ttl;
    }

    synchronized byte[] getContent() {
        return content;
    }

    synchronized void invalidateContent() {
        content = null;
    }

    @Override
    public String toString() {
        return "PackageContent [timestamp=" + timestamp + ", ttl=" + ttl + ", content size " + (content != null ? "= " + content.length : " is invalid") + "]";
    }
}
