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

package org.radixware.kernel.common.repository.kernel;

import java.io.File;
import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.repository.fs.*;


public class FSRepositoryKernelModule extends FSRepositoryModule<KernelModule> {

    public FSRepositoryKernelModule(KernelModule module) {
        super(module);
    }

    public FSRepositoryKernelModule(File moduleDir) {
        super(moduleDir);
    }

    @Override
    public IRepositoryDefinition getDefinitionRepository(Definition def) {
        return null;
    }

    @Override
    public void processException(Throwable e) {
        e.printStackTrace();
    }

    protected String getDirName() {
        return getName().toLowerCase();
    }

    @Override
    public void setModule(KernelModule module) {
    }

}
