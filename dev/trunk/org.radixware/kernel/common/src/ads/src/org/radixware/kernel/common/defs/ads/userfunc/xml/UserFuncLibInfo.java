package org.radixware.kernel.common.defs.ads.userfunc.xml;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class UserFuncLibInfo extends ParseInfo{
    private final List<UserFuncImportInfo> userFuncs;

    public UserFuncLibInfo(String name, String xPath, boolean isRoot) {
        super(name, xPath, isRoot);
        this.userFuncs = new LinkedList<>();
    }
    
    public void addUserFunc(UserFuncImportInfo userFunc){
        userFuncs.add(userFunc);
    }

    public List<UserFuncImportInfo> getUserFuncs() {
        return new ArrayList<>(userFuncs);
    }
}
