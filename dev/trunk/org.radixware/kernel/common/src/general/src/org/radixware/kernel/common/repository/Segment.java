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
package org.radixware.kernel.common.repository;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

import java.util.List;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.ClipboardSupport.Transfer;
import org.radixware.kernel.common.defs.IDirectoryRadixObject;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.repository.fs.IRepositoryLayer;
import org.radixware.kernel.common.repository.fs.IRepositoryModule;
import org.radixware.kernel.common.repository.fs.IRepositorySegment;
import org.radixware.kernel.common.utils.FileUtils;

/**
 * {
 *
 * @linplain Layer} segment.
 *
 */
public abstract class Segment<T extends Module> extends RadixObject implements IDirectoryRadixObject {

    protected abstract Module.Factory<T> getModuleFactory();
    private Modules<T> modules = null;

    public abstract ERepositorySegmentType getType();

    protected Segment(Layer ownerLayer) {
        setContainer(ownerLayer);
    }

    public IRepositorySegment<T> getRepository() {
        final Layer layer = getLayer();
        if (layer != null) {
            final IRepositoryLayer layerRepository = layer.getRepository();
            if (layerRepository != null) {
                return layerRepository.getSegmentRepository(this);
            }
        }
        return null;
    }

    public Modules<T> getModules() {
        synchronized (this) {

            if (modules == null) {
                if (getContainer() == null) {
                    return new Modules<T>(this);
                }

                modules = new Modules<T>(this);
                // upload
                final IRepositorySegment<T> segmentRepository = getRepository();
                if (segmentRepository != null) {
                    IRepositoryModule<T>[] moduleRepositories = segmentRepository.listModules();
                    for (IRepositoryModule<T> module : moduleRepositories) {
                        try {
                            modules.addFromRepository(module);
                        } catch (IOException ex) {
                            segmentRepository.processException(ex);
                        }
                    }
                }
            }
            return modules;
        }
    }

    public void reloadModules() {
        synchronized (this) {
            modules = null;
        }
    }

    public Modules<T> getModulesIfLoaded() {
        synchronized (this) {
            return modules;
        }
    }

    /**
     * returns owner layer
     */
    @Override
    public final Layer getLayer() {
        return (Layer) getContainer();
    }

    @Override
    public String getName() {
        return getType().getName();
    }

    /**
     * Get segment directory location or null if segment is not in layer.
     */
    @Override
    public File getDirectory() {
        final IRepositorySegment<T> segmentRepository = getRepository();
        return (segmentRepository != null ? segmentRepository.getDirectory() : null);
    }

    /**
     * Get segment directory location or null if segment is not in layer.
     */
    @Override
    public File getFile() {
        return getDirectory();
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        for (Module module : this.getModules()) {
            module.visit(visitor, provider);
        }
    }

    /**
     * Save the segment to the current directory (create if it is nessesary).
     */
    @Override
    public void save() throws IOException {
        File dir = getDirectory();

        if (dir == null) {
            throw new IOException("Segment " + getQualifiedName() + "is not file-based");
        }

        FileUtils.mkDirs(dir);

        setEditState(EEditState.NONE);
    }

    @Override
    public boolean isSaveable() {
        return true;
    }

    @Override
    protected boolean isQualifiedNamePart() {
        return false;
    }

//    public List<File> collectModuleDirectories() {
//        final File segmentDir = getDirectory();
//        final File[] moduleDirs = segmentDir.listFiles();
//        if (moduleDirs == null) {
//            return Collections.emptyList();
//        }
//
//        final List<File> result = new ArrayList<File>(moduleDirs.length);
//
//        for (File moduleDir : moduleDirs) {
//            if (Module.isModuleDir(moduleDir)) {
//                result.add(moduleDir);
//            }
//        }
//
//        return result;
//    }
    protected class SegmentClipboardSupport extends ClipboardSupport<Segment> {

        private final Class<T> supportedModuleClass;

        public SegmentClipboardSupport(Class<T> supportedModuleClass) {
            super(Segment.this);
            this.supportedModuleClass = supportedModuleClass;
        }

        @Override
        public CanPasteResult canPaste(List<Transfer> transfers, DuplicationResolver resolver) {
            if (isReadOnly() || transfers.isEmpty()) {
                return CanPasteResult.NO;
            }
            for (Transfer transfer : transfers) {
                final RadixObject objectInClipboard = transfer.getObject();
                if (objectInClipboard.getClass() != supportedModuleClass) {
                    return CanPasteResult.NO;
                }
                if (getModules().contains((T) objectInClipboard)) {
                    return CanPasteResult.NO;
                }
            }
            return CanPasteResult.YES;
        }

        @Override
        public boolean canCopy() {
            return false;
        }

        @Override
        public void paste(List<Transfer> transfers, DuplicationResolver resolver) {
            getModules().getClipboardSupport().paste(transfers, resolver);
        }
    }
}
