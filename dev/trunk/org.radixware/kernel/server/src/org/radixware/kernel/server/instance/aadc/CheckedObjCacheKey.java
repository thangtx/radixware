package org.radixware.kernel.server.instance.aadc;


class CheckedObjCacheKey {

    private final long id;
 
    CheckedObjCacheKey(long id) {
       this.id = id;
    }
    
    long getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) 
            return false;
        final CheckedObjCacheKey other = (CheckedObjCacheKey) obj;
        return this.id == other.id;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

}
