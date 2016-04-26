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

package org.radixware.kernel.designer.debugger;

import java.util.EnumMap;
import java.util.Map;
import java.util.prefs.Preferences;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.designer.debugger.StartupInfo.EEnvironment;


public class StartupInfoProfile {

    private final String name;
    private final Branch branch;
    private final Map<EEnvironment, StartupInfo> infoMap;

    public StartupInfoProfile(Branch branch, String name, Map<EEnvironment, StartupInfo> infoMap) {
        this.name = name;
        this.infoMap = infoMap;
        this.branch = branch;
    }

    public String getName() {
        return name;
    }

    public Branch getBranch() {
        return branch;
    }

    public StartupInfo getInfo(EEnvironment environment) {
        return infoMap.get(environment);
    }

    public void save() {
        final StartupInfoManager manager = StartupInfoManager.getInstance();

        for (final StartupInfo startupInfo : infoMap.values()) {
            final Preferences node = manager.getNode(branch, name, startupInfo.getConfigPathSpec());
            startupInfo.save(node);
        }
    }

    public StartupInfoProfile copy(String newName) {

        final Map<EEnvironment, StartupInfo> copyInfoMap = new EnumMap<>(EEnvironment.class);

        for (final StartupInfo startupInfo : infoMap.values()) {
            copyInfoMap.put(startupInfo.getEnvironment(), startupInfo.copy());
        }

        return new StartupInfoProfile(branch, newName, copyInfoMap);
    }
}
