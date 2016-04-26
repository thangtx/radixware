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

import java.io.File;
import java.io.IOException;
//import org.netbeans.modules.subversion.util.Context;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ProjectFactory;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileUtil;
import org.radixware.kernel.designer.environment.saveall.SaveAllImpl;
import org.radixware.kernel.designer.environment.upgrade.UpgraderHandler;

/**
 * Radix project factory. Registered in META-INF.
 */
public class RadixProjectFactory implements ProjectFactory {

    @Override
    public boolean isProject(FileObject projectDirFileObject) {
        if (projectDirFileObject == null) {
            return false;
        }

        final File projectDir = FileUtil.toFile(projectDirFileObject);
        if (projectDir == null || !(Branch.isBranchDir(projectDir))) {
            return false;
        }

        if (RadixFileUtil.isDebugMode()) {
            return true;
        }

        final File userDir = RadixFileUtil.getUserDir();

        final FileObject userDirFileObject = RadixFileUtil.toFileObject(userDir);
        if (userDirFileObject == null) {
            return false;
        }

        return FileUtil.isParentOf(projectDirFileObject, userDirFileObject);
    }

    @Override
    public Project loadProject(FileObject projectDirectory, ProjectState state) throws IOException {
        if (!isProject(projectDirectory)) { // calls by system without any checking.
            return null;
        }

        //create
        UpgraderHandler.getDefault();
        SaveAllImpl.getDefault();

        final File file = FileUtil.toFile(projectDirectory);
//        if (FreeFormRepository.isInspectorBranchDir(file)) {
//            final FreeFormRepository rep = FreeFormRepository.Factory.newInstance(file);
//            final Branch branch = Branch.Factory.newInstanceInMemory(rep);
//            final InspectorProject project = new InspectorProject(branch);
//            return project;
//        } else {
        final Branch branch = Branch.Factory.loadFromDir(file);
        final RadixProject project = new RadixProject(branch);
        return project;
        //}
    }

    @Override
    public void saveProject(Project project) throws IOException, ClassCastException {
        RadixProject radixProject = (RadixProject) project;
        radixProject.save();
    }
}
