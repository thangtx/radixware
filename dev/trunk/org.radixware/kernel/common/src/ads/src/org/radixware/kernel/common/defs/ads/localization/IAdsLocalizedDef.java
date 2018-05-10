package org.radixware.kernel.common.defs.ads.localization;

import org.radixware.kernel.common.defs.localization.ILocalizedDef;
import org.radixware.kernel.common.types.Id;

public interface IAdsLocalizedDef extends ILocalizedDef{
    public Id getLocalizingBundleId();
     
    public Id getId();
}
