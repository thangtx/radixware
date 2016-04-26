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

package org.radixware.kernel.common.repository.fs;

import java.io.File;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.enums.ERepositorySegmentType;

/**
 * Repository segment abstraction
 */
public interface IRepositorySegment<T extends Module> extends IRadixRepository {

    /**
     * @return list of segment modules
     */
    public IRepositoryModule<T>[] listModules();

    /**
     * @return segment directory (for segments loaded from system file system)
     */
    public File getDirectory();

    public IRepositoryModule<T> getModuleRepository(T module);

    public ERepositorySegmentType getSegmentType();
}
