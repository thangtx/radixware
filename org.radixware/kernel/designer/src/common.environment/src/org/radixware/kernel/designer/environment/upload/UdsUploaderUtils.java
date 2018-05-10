package org.radixware.kernel.designer.environment.upload;

import java.io.IOException;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.uds.IUdsDirectoryRadixObject;
import org.radixware.kernel.common.defs.uds.files.UdsFile;
import org.radixware.kernel.common.defs.uds.module.UdsModule;
import org.radixware.kernel.common.defs.uds.report.UdsExchangeReport;

public class UdsUploaderUtils {

    public static void close(RadixObject radixObject) {
        IUdsDirectoryRadixObject dir = getOwnerDirectory(radixObject);
        if (dir != null) {
            // do not call definition.delete(), because definition file will be deleted.
            dir.remove(radixObject);
        }
    }

    public static IUdsDirectoryRadixObject getOwnerDirectory(RadixObject radixObject) {
        for (RadixObject owner = radixObject.getContainer(); owner != null; owner = owner.getContainer()) {
            if (owner instanceof IUdsDirectoryRadixObject) {
                return (IUdsDirectoryRadixObject) owner;
            }
        }
        return null;
    }
    
    public static void updateDirChildren(IUdsDirectoryRadixObject dir){
        if (dir == null){
            return;
        }
        for (RadixObject radixObject : dir) {
                RadixObjectUploader uploader = null;
                if (radixObject instanceof UdsFile) {
                    uploader = new UdsFileUploder((UdsFile) radixObject);
                } else if (radixObject instanceof AdsDefinition) {
                    uploader = new UdsDefinitionUploader((AdsDefinition) radixObject);
                } else if (radixObject instanceof UdsExchangeReport) {
                    uploader = new UdsReportUploder((UdsExchangeReport) radixObject);
                }
                
                if (uploader != null){
                    uploader.update();
                }
            }
    }
    
    public static void reload(RadixObject radixObject) throws IOException{
        UdsModule module = (UdsModule) radixObject.getModule();
        if (module != null) {
            module.getUdsFiles().reload(radixObject);
        }
    }
}
