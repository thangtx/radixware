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
import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.Module;

/**
 * RadixWare repository directory abstraction
 *
 */
public interface IRepositoryModule<T extends Module> extends IRadixRepository {

    /**
     * @return module directory (for modules loaded from system file system) or
     * null
     */
    File getDirectory();

    //public File getAPIDirectory();
    /**
     * @return module description file (for modules loaded from system file
     * system) or null
     */
    File getDescriptionFile();

    /**
     * @return module description data input stream
     */
    InputStream getDescriptionData() throws IOException;

    InputStream getDirectoryXmlData() throws IOException;

    /**
     * @return module name
     *
     */
    String getName();

    /**
     * @return module path
     *
     */
    String getPath();

    IRepositoryDefinition getDefinitionRepository(Definition def);

    boolean isInjection();

    void installInjection(RepositoryInjection.ModuleInjectionInfo injection) throws IOException;

    void uninstallInjection();

    List<IJarDataProvider> getBinaries();
}
