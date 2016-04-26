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

package org.radixware.kernel.designer.environment.upload;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.radixware.kernel.common.defs.Definitions;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.repository.fs.FSRepositorySegment;
import org.radixware.kernel.common.repository.fs.IRepositoryModule;
import org.radixware.kernel.common.repository.fs.IRepositorySegment;


class SegmentUploader<S extends Segment> extends RadixObjectUploader<S> {

    public SegmentUploader(S segment) {
        super(segment);
    }

    public S getSegment() {
        return getRadixObject();
    }

    @Override
    public Module uploadChild(File modulePrimaryFile) throws IOException {
        final File moduleDir = modulePrimaryFile.getParentFile();
        final IRepositoryModule moduleRepository = ((FSRepositorySegment) getSegment().getRepository()).getModuleRepository(moduleDir);
        final Module module = getSegment().getModules().addFromRepository(moduleRepository);
        return module;
    }

    private void loadNewModules(final Set<String> alreadyLoadedModuleNames) {
        final IRepositorySegment repository = getSegment().getRepository();
        if (repository != null) {
            final IRepositoryModule[] moduleRepositories = repository.listModules();
            if (moduleRepositories != null) {
                for (IRepositoryModule moduleRepository : moduleRepositories) {
                    final String moduleName = moduleRepository.getName();
                    if (!alreadyLoadedModuleNames.contains(moduleName)) {
                        final File modulePrimaryFile = moduleRepository.getDescriptionFile();
                        tryToUploadChild(modulePrimaryFile, Module.MODULE_TYPE_TITLE);
                    }
                }
            }
        }
    }

    @Override
    public void update() {
        updateChildren();
    }

    @Override
    public void updateChildren() {
        final Definitions<Module> modules = getSegment().getModulesIfLoaded();
        if (modules != null) {
            final Set<String> moduleNames = new HashSet<String>();
            for (Module module : modules) {
                ModuleUploader moduleUpdater = ModuleUploader.Factory.newInstance(module);
                moduleUpdater.update();
                moduleNames.add(module.getName());
            }
            loadNewModules(moduleNames);
        }
    }

    @Override
    public void close() {
        throw new IllegalStateException();
    }

    @Override
    public long getRememberedFileTime() {
        throw new IllegalStateException();
    }

    @Override
    public void reload() throws IOException {
        throw new IllegalStateException();
    }
}
