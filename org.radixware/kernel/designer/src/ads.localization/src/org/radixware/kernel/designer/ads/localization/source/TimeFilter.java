
package org.radixware.kernel.designer.ads.localization.source;

import java.sql.Timestamp;

public class TimeFilter extends LanguagesSettings{

    private Timestamp timeFrom;
    private Timestamp timeTo;

    public Timestamp getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(Timestamp timeFrom) {
        this.timeFrom = timeFrom;
    }

    public Timestamp getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(Timestamp timeTo) {
        this.timeTo = timeTo;
    }

    public boolean isEmpty() {
        return timeFrom == null && timeTo == null;
    }
}
