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
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;


public interface IJarDataProvider {

    interface IJarEntry {

        String getName();

        boolean isDirectory();
    }

    /**
     * Returns data for specified entry or null if not found
     */
    public byte[] getEntryData(String name) throws IOException;

    public InputStream getEntryDataStream(String name) throws IOException;

    public boolean entryExists(String name);

    public boolean close() throws IOException;

    public String getName();

    public File getFile();

    public boolean isFileBased();

    Collection<IJarEntry> entries();
}
