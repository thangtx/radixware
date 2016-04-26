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

package org.radixware.kernel.starter.radixloader;

import org.radixware.kernel.starter.filecache.CacheEntry;
import org.radixware.kernel.starter.filecache.FileCacheEntry;
import org.radixware.kernel.starter.filecache.JarCacheEntry;

/**
 * Accessor to internal loader functionality -
 * {@linkplain RadixLoaderAccessor#getFile(java.lang.String, long, RadixLoader.HowGetFile, boolean)}.
 * This functionality has been made protected to simplify synchronization
 * control, but if RadixLoder itself needs to give someone access to it, it can
 * do this with Accessor.
 *
 * <p> Note that generally {@link CacheEntry} can be acquired and used safely
 * only under read lock on RadixLoader (In the current implementation,
 * {@link FileCacheEntry} can be used safely at any time after construction, but
 * {@link JarCacheEntry} can not. {@link JarCacheEntry} can be based on the file
 * in cache, and this file can be removed during actualization process. </p>
 *
 */
public interface RadixLoaderAccessor {

    public CacheEntry getFile(String file, long revision, RadixLoader.HowGetFile howGet) throws RadixLoaderException;

    public CacheEntry getFile(String file, long revision) throws RadixLoaderException;

    public RadixLoader getLoader();
}
