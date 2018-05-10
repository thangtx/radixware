
package org.radixware.kernel.designer.environment.upload;

import java.io.File;
import java.io.IOException;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.uds.IUdsDirectoryRadixObject;
import org.radixware.kernel.common.defs.uds.files.UdsDirectory;
import org.radixware.kernel.common.defs.uds.files.UdsFile;
import org.radixware.kernel.common.defs.uds.module.Loader;
import org.radixware.kernel.common.defs.uds.module.UdsModule;
import org.radixware.kernel.common.defs.uds.report.UdsExchangeReport;

public class UdsFileUploder extends RadixObjectUploader<UdsFile>{

    public UdsFileUploder(UdsFile radixObject) {
        super(radixObject);
    }

    @Override
    public void close() {
        UdsUploaderUtils.close(getRadixObject());
    }

    @Override
    public void updateChildren() {
        RadixObject udsFile = getRadixObject();
        if (udsFile instanceof UdsDirectory) {
            UdsDirectory udsDirectory = ((UdsDirectory) udsFile);
            UdsUploaderUtils.updateDirChildren(udsDirectory);
            loadNewFiles();
        }
    }

    private void loadNewFiles() {
        UdsDirectory directory = (UdsDirectory) getRadixObject();
        File[] list = directory.collectFiles();
        if (list != null) {
            for (File file : list) {
                RadixObject radixObject = directory.getRadixObjectByFileName(file.getName());
                if (radixObject == null){
                    tryToUploadChild(file, "File");
                }
                
            }
            
        }
    }

    @Override
    public void reload() throws IOException {
        UdsUploaderUtils.reload(getRadixObject());
    }

    @Override
    public long getRememberedFileTime() {
        return getRadixObject().getFileLastModifiedTime();
    }

    @Override
    public RadixObject uploadChild(File file) throws IOException {
        RadixObject udsFile = getRadixObject();
        if (udsFile instanceof UdsDirectory) {
            UdsDirectory directory = (UdsDirectory) udsFile;
            UdsModule module = directory.getModule();
            if (!directory.containsFile(file) && module != null){
                
                RadixObject newRadixObject = Loader.parseFile(file, module.getUdsFiles());
                if (newRadixObject != null){
                    directory.getFiles().add(newRadixObject);
                }
                return newRadixObject;
            }
        }
        return null;
    }

    @Override
    protected boolean needClose() {
        RadixObject udsFile = getRadixObject();
        if (udsFile instanceof UdsDirectory) {
            final File file = udsFile.getFile();
            return !file.exists();
        }
        return super.needClose();
    }
    
    
}
