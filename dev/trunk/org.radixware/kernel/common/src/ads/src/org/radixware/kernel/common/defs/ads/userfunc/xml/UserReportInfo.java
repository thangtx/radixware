package org.radixware.kernel.common.defs.ads.userfunc.xml;

import org.radixware.schemas.adsdef.UserReportExchangeType;

public class UserReportInfo extends ParseInfo{
    private UserReportExchangeType type;
    
    public UserReportInfo(String name, String xPath, boolean isRoot) {
        super(name, xPath, isRoot);
    }

    public UserReportExchangeType getType() {
        return type;
    }

    public void setType(UserReportExchangeType type) {
        this.type = type;
    }

}
