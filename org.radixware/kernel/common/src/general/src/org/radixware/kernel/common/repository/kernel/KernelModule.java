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

import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;

public class KernelModule extends Module {

    protected KernelModule(Id id, String name) {
        super(id, name);
    }

    @Override
    public ERepositorySegmentType getSegmentType() {
        return ERepositorySegmentType.KERNEL;
    }

    public boolean isSuitableForEnvironment(ERuntimeEnvironmentType env) {
        if (env == null) {
            return false;
        }
        String name = getName();
        if (name != null) {
            name = name.toLowerCase();
        }
        switch (env) {
            case COMMON:
            case COMMON_CLIENT:
                return "common".equals(name) || "utils".equals(name) || "starter".equals(name);
            case EXPLORER:
                return "explorer".equals(name) || "common".equals(name) || "utils".equals(name) || "starter".equals(name);
            case SERVER:
                return "server".equals(name) || "common".equals(name) || "utils".equals(name) || "starter".equals(name) || "jdbc".equals(name);
            case WEB:
                return "web".equals(name) || "common".equals(name) || "utils".equals(name) || "starter".equals(name);
            default:
                return false;
        }
    }

    @Override
    public DefinitionSearcher<? extends Definition> getDefinitionSearcher() {
        return new DefinitionSearcher<Definition>(this) {

            @Override
            public DefinitionSearcher<Definition> findSearcher(Module module) {
                return null;
            }

            @Override
            public Definition findInsideById(Id id) {
                return null;
            }
        };
    }

    @Override
    public RadixIcon getIcon() {
        return RadixObjectIcon.KERNEL_MODULE;
    }

    @Override
    public String getTypeTitle() {
        return "Kernel Module";
    }

    @Override
    public String getTypesTitle() {
        return "Kernel Modules";
    }
}
