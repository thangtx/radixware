package org.radixware.kernel.common.defs.ads.userfunc.xml;

import java.util.List;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.LocalizingBundleDefinition;
import org.radixware.schemas.xscml.JmlType;


public class ParseInfo {
    private final String name;
    private final String xPath;
    private final boolean isRoot;

    public ParseInfo(String name, String xPath, boolean isRoot) {
        this.name = name;
        this.xPath = xPath;
        this.isRoot = isRoot;
    }
    
    public String getName() {
        return name;
    }
    
    public String getXPath() {
        return xPath;
    }

            
    public static final class Builder {

        public Id id;
        public String name;
        public Id classId;
        public Id methodId;
        public Id ownerEntityId;
        public Id ownerClassId;
        public Id propId;
        public String ownerPid;
        public UserFuncImportInfo.ProfileInfo profInfo;
        public String profile;
        public JmlType source;
        public String xPath;
        public LocalizingBundleDefinition strings;
        public String description;
        public List<Integer> suppressedWarnings;
        public boolean isRoot;

        public UserFuncImportInfo createUserFuncInfo() {
            return new UserFuncImportInfo(id, name, classId, methodId, ownerEntityId, ownerClassId, propId, ownerPid, profInfo, profile, source, strings, description, suppressedWarnings, xPath, isRoot);
        }

        public UserFuncLibInfo createLib() {
            return new UserFuncLibInfo(name, xPath, isRoot);
        }
        
        public UserReportInfo createUserReportInfo() {
            return new UserReportInfo(name, xPath, isRoot);
        }
    }
}
