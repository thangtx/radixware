
package org.radixware.kernel.common.enums;

import java.util.List;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.types.IKernelStrEnum;
import org.radixware.kernel.common.types.Id;

public enum EReportTextFormat implements IKernelStrEnum {

    RICH("Rich"),
    PLAIN("Plain");
    private final String value;

    private EReportTextFormat(final String val) {
        this.value = val;
    }

    @Override
    public String getName() {
        return value;
    }

    @Override
    public String getValue() {
        return value;
    }

    public static EReportTextFormat getForValue(final String val) {
        for (EReportTextFormat e : EReportTextFormat.values()) {
            if (e.value.equals(val)) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError("EReportTextFormat has no item with value: " + String.valueOf(val),val);
    }

    @Override
    public boolean isInDomain(Id id) {
        return false;
    }

    @Override
    public boolean isInDomains(List<Id> ids) {
        return false;
    }
}

