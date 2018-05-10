package org.radixware.kernel.common.defs.uds;

import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.localization.IAdsLocalizedDef;


public interface IInnerLocalizingDef extends IAdsLocalizedDef {
    AdsLocalizingBundleDef findLocalizingBundleImpl(boolean createIfNotExists);
}
