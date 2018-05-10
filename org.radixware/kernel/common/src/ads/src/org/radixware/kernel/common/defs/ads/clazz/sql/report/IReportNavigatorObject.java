package org.radixware.kernel.common.defs.ads.clazz.sql.report;

import java.util.List;
import org.radixware.kernel.common.defs.RadixObject;

public interface IReportNavigatorObject<T extends RadixObject>{
    
    RadixObject getParent();
    
    List<T> getChildren();
    
    boolean isDiagramModeSupported(AdsReportForm.Mode mode);
}
