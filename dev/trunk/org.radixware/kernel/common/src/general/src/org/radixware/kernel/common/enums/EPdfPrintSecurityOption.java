package org.radixware.kernel.common.enums;

import java.util.List;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.types.IKernelIntEnum;
import org.radixware.kernel.common.types.Id;

public enum EPdfPrintSecurityOption implements IKernelIntEnum {

    NO_PRINT(0, "Not allowed"),
    ALLOW_PRINT_LQ(1, "Low Resolution"),
    ALLOW_PRINT_HQ(2, "High Resolution");
    
    
    private final Long val;
    private final String name;

    private EPdfPrintSecurityOption(long val, String name) {
        this.val = Long.valueOf(val);
        this.name = name;
    }

    @Override
    public Long getValue() {
        return val;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isInDomain(Id id) {
        return false;
    }

    @Override
    public boolean isInDomains(List<Id> ids) {
        return false;
    }
    
    @Override
    public String toString() {
        return getName();
    }
    
    public static EPdfPrintSecurityOption getForValue(final long val) {
        for (EPdfPrintSecurityOption e : EPdfPrintSecurityOption.values()) {
            if (e.val.longValue() == val) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError("EPdfPrintSecurityOption has no item with value: " + String.valueOf(val),val);
    }
    
    
    
}
