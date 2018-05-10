package org.radixware.kernel.common.defs.uds;

import org.radixware.kernel.common.defs.IDirectoryRadixObject;
import org.radixware.kernel.common.defs.RadixObject;

public interface IUdsDirectoryRadixObject extends IDirectoryRadixObject, Iterable<RadixObject>{
    
    public boolean remove(RadixObject radixObject);
    
    public void add(RadixObject radixObject);
}
