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

package org.radixware.kernel.designer.environment.project;

import org.radixware.kernel.designer.environment.files.RadixFileEncodingQueryImplementation;
import java.io.File;
import java.io.IOException;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileObject;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileUtil;


class RadixProject implements Project {

    private final Lookup lookup;

    public RadixProject(Branch branch) {
        this.lookup = Lookups.fixed(
                branch,
                new RadixProjectInformation(this),
                new RadixLogicalViewProvider(this),
                //new RadixSharabilityQueryImplementation(),
                new RadixFileEncodingQueryImplementation(),
                //new RadixProjectSources(this),
                new RadixActionProvider(branch),
                new RadixProjectOpenedHook(branch));
    }

    @Override
    public Lookup getLookup() {
        return lookup;
    }

    public Branch getBranch() {
        return getLookup().lookup(Branch.class);
    }

    @Override
    public FileObject getProjectDirectory() {
        final Branch branch = getBranch();
        final File branchDir = branch.getDirectory();
        return RadixFileUtil.toFileObject(branchDir);
    }

    public void save() throws IOException {
        // nothing
    }
}
