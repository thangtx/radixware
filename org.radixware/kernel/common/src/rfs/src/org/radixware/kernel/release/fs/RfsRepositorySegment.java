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

package org.radixware.kernel.release.fs;

import java.io.File;
import java.util.HashMap;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.repository.fs.IRepositoryModule;
import org.radixware.kernel.common.repository.fs.IRepositorySegment;
import org.radixware.kernel.starter.meta.DirectoryMeta;


abstract class RfsRepositorySegment<T extends Module> implements IRepositorySegment<T> {

    protected final DirectoryMeta dir;
    protected final RfsRepositoryLayer layer;
    protected final ERepositorySegmentType type;
    private HashMap<String, IRepositoryModule> modules = null;

    public RfsRepositorySegment(RfsRepositoryLayer layer, DirectoryMeta dir, ERepositorySegmentType type) {
        this.dir = dir;
        this.layer = layer;
        this.type = type;
    }

    @Override
    public IRepositoryModule<T>[] listModules() {
        synchronized (this) {
            if (modules == null) {
                modules = new HashMap<String, IRepositoryModule>();
                for (String moduleDirXmlFileName : dir.getIncludes()) {
                    if (!moduleDirXmlFileName.endsWith(DirectoryMeta.DIRECTORY_XML_FILE)) {
                        continue;
                    }
                    final String moduleDirName = dir.getDirectory() + "/" + moduleDirXmlFileName.substring(0, moduleDirXmlFileName.length() - DirectoryMeta.DIRECTORY_XML_FILE.length());

                    IRepositoryModule<T> module = createModule(moduleDirName);
                    modules.put(moduleDirName, module);
                }
            }
            return modules.values().toArray(new IRepositoryModule[modules.size()]);
        }
    }

    protected abstract IRepositoryModule<T> createModule(String moduleDirName);

    @Override
    public File getDirectory() {
        return null;
    }

    @Override
    public IRepositoryModule<T> getModuleRepository(T module) {
        synchronized (this) {
            if (modules == null) {
                listModules();
            }
            IRepositoryModule<T> result =  modules.get(dir.getDirectory() + "/" + module.getName() + "/");
            if(result == null){
                String name = dir.getDirectory() + "/" + module.getName() + "/";
                result = createModule(name);
                modules.put(name, result);
            }
            return result;
        }
        //return createModule();
    }

    File getTemporaaryStorage() {
        return new File(layer.getTemporaryStorage(), type.getValue());
    }

    ReleaseRepository getRelease() {
        return layer.getRelease();
    }
}
