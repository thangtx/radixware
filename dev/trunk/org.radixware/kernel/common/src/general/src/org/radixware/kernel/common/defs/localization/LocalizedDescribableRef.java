package org.radixware.kernel.common.defs.localization;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicLong;

public class LocalizedDescribableRef extends WeakReference<ILocalizedDescribable> {
    AtomicLong version = new AtomicLong(0);

    public LocalizedDescribableRef(ILocalizedDescribable referent) {
        super(referent);
        version.set(ILocalizingBundleDef.version.get());
    }

    public long getVersion() {
        return version.get();
    }
}
