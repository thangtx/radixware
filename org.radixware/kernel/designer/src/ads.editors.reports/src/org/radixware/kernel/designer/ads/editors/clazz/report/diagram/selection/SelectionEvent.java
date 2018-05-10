package org.radixware.kernel.designer.ads.editors.clazz.report.diagram.selection;

import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.AbstractAdsReportWidget;

public class SelectionEvent extends RadixEvent {

    private final AbstractAdsReportWidget sourse;
    private final boolean select;
    private final boolean expand;

    public SelectionEvent(AbstractAdsReportWidget sourse, boolean select) {
        this.sourse = sourse;
        this.select = select;
        this.expand = false;
    }

    public SelectionEvent(AbstractAdsReportWidget sourse, boolean expand, boolean isSelected) {
        this.sourse = sourse;
        this.expand = expand;
        this.select = expand? !isSelected :true;
    }
    
    public AbstractAdsReportWidget getSourse() {
        return sourse;
    }

    public boolean isSelected() {
        return select;
    }

    public boolean isExpand() {
        return expand;
    }
}
