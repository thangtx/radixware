package org.radixware.kernel.server.instance.aadc;


class AffinityCacheKey {

    private final int objectKey;
 
    AffinityCacheKey(int objectKey) {
       this.objectKey = objectKey;
    }
    
    long getObjectKey() {
        return objectKey;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) 
            return false;
        final AffinityCacheKey other = (AffinityCacheKey) obj;
        return this.objectKey == other.objectKey;
    }

    @Override
    public int hashCode() {
        return objectKey;
    }

}
