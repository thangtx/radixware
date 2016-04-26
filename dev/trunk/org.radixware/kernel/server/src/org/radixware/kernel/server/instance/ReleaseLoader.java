/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */

package org.radixware.kernel.server.instance;

import java.lang.ref.WeakReference;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.server.arte.ILoadErrorsHandler;
import org.radixware.kernel.server.arte.Release;
import org.radixware.kernel.starter.radixloader.RadixLoader;
import org.radixware.kernel.starter.radixloader.RadixLoaderException;


public class ReleaseLoader {

    private final long version;
    //@GuardedBy this
    private WeakReference<Release> cachedRelease = new WeakReference<>(null);
    private final ILoadErrorsHandler loadErrorsHandler;

    public ReleaseLoader(long version, final ILoadErrorsHandler loadErrorsHandler) {
        this.version = version;
        this.loadErrorsHandler = loadErrorsHandler;
    }

    public synchronized Release get() throws ReleaseLoadException {
        Release result = cachedRelease.get();
        if (result != null) {
            return result;
        } else {
            final long loadStartMillis = System.currentTimeMillis();
            result = loadRelease();
            Instance.get().getTrace().debug("Release meta for revision #" + version + " has been loaded in " + (System.currentTimeMillis() - loadStartMillis) + " ms", EEventSource.INSTANCE, true);
            cachedRelease = new WeakReference<>(result);
            return result;
        }
    }

    public long getVersion() {
        return version;
    }

    private Release loadRelease() throws ReleaseLoadException {
        try {
            return new Release(RadixLoader.getInstance().getRevisionMeta(version), loadErrorsHandler);
        } catch (RadixLoaderException ex) {
            throw new ReleaseLoadException("Unable to load release", ex);
        }
    }
}
