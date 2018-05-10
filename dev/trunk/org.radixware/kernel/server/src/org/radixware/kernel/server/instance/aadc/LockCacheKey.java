package org.radixware.kernel.server.instance.aadc;

import java.util.Objects;
import org.radixware.kernel.common.types.Pid;

public class LockCacheKey {

    private final String objPid;
    private final byte lockType;

    public LockCacheKey(Pid pid, byte lockType) {
       this.objPid = pid.toStr();
       this.lockType = lockType;
    }
    
    public String getObjPid() {
        return objPid;
    }

    public byte getlockType() {
        return lockType;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) 
            return false;
        LockCacheKey o = (LockCacheKey) obj;
        return objPid.equals(o.objPid) && lockType == o.lockType;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.objPid);
        hash = 41 * hash + this.lockType;
        return hash;
    }    
}
