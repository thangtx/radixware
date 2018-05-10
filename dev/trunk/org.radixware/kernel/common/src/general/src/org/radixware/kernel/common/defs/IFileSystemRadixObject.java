package org.radixware.kernel.common.defs;

import java.io.File;

public interface IFileSystemRadixObject {
    IDirectoryRadixObject getOwnerDirectory();
    
    RadixObject getRadixObjectByFileName(String name);
    
    File getFile();
}
