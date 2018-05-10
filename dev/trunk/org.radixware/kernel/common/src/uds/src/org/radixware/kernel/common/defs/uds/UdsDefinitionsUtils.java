package org.radixware.kernel.common.defs.uds;

import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.uds.report.UdsExchangeReport;

public class UdsDefinitionsUtils {
    public static String getDefinitionFileName(Definition def){
        return def instanceof UdsDefinition ? ((UdsDefinition) def).getFileName() : (def.getId().toString() + ".xml");
    }
    
    public static RadixObject getRadixObjectByFileName(String name, List<RadixObject> radixObjects){
        for (RadixObject radixObject : radixObjects) {
            if (radixObject instanceof Definition){
                String fileName = getDefinitionFileName((Definition) radixObject);
                if (fileName.equals(name)) {
                    return radixObject;
                }
            }
            if (radixObject instanceof UdsExchangeReport){
                if(((UdsExchangeReport) radixObject).getFileName().equals(name)){
                    return radixObject;
                }
            } else if (radixObject.getName().equals(name)) {
                return radixObject;
            }
        }
        return null;
    }

}
