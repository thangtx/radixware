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

package org.radixware.kernel.common.builder.check.common;

import java.util.HashSet;
import java.util.Set;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.repository.fs.IRepositoryModule;


@RadixObjectCheckerRegistration
public class SegmentChecker<T extends Segment<? extends Module>> extends RadixObjectChecker<T> {

    @Override
    public void check(T segment, IProblemHandler problemHandler) {
        final Set<String> loadedModuleNames = new HashSet<String>();
        if (segment.getType() != ERepositorySegmentType.KERNEL) {
            for (Module m : segment.getModules()) {
                loadedModuleNames.add(m.getName());
            }

            final IRepositoryModule[] moduleRepositories = segment.getRepository().listModules();
            if (moduleRepositories != null) {
                for (IRepositoryModule repositoryModule : moduleRepositories) {
                    final String name = repositoryModule.getName();
                    if (!loadedModuleNames.contains(name)) {
                        error(segment, problemHandler, "Module '" + name + "' not loaded");
                    }
                }
            }
        }
    }

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return Segment.class;
    }
}
