package org.radixware.kernel.common.defs.uds;

import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.types.Id;

public class UdsLocalizingBundleDef extends AdsLocalizingBundleDef{

    public UdsLocalizingBundleDef(Id ownerId, AdsDefinition owner) {
        super(ownerId);
        this.owner = owner;
    }

    @Override
    public boolean isSaveable() {
        return false;
    }

    @Override
    public AdsDefinition findBundleOwner() {
        return owner;
    }

    private final AdsDefinition owner;
}
