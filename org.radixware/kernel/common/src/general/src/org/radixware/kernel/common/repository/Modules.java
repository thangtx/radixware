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

import java.io.IOException;
import java.util.*;
import org.radixware.kernel.common.defs.ClipboardSupport.CanPasteResult;
import org.radixware.kernel.common.defs.ClipboardSupport.DuplicationResolver;
import org.radixware.kernel.common.defs.ClipboardSupport.Transfer;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.exceptions.RadixObjectError;
import org.radixware.kernel.common.repository.fs.IRepositoryModule;
import org.radixware.kernel.common.types.Id;


public class Modules<T extends Module> extends Definitions<T> {

    public static volatile long modificationStamp = 0;

    protected Modules(Segment<T> owner) {
        super(owner);
    }

    /**
     * Find module by identifier in current segment or segments of based layers.
     *
     * @param segmentType segment type where to search or null for the same
     * segment type.
     */
    public List<Module> findById(ERepositorySegmentType segmentType, final Id id, final boolean findFirst, List<Module> resultSet) {
        final Segment<T> segment = getSegment();

        if (segmentType == null || segment.getType() == segmentType) {
            //   synchronized (this) {
            Module module = (Module) segment.getModules().findById(id);
            if (module != null) {
                if (resultSet == null) {
                    return Collections.singletonList(module);
                } else {
                    resultSet.add(module);
                    return resultSet;
                }
            }
            final ERepositorySegmentType st = segment.getType();
            final List<Module> results = resultSet == null ? (findFirst ? Collections.singletonList((Module) null) : new LinkedList<Module>()) : resultSet;
            Layer.HierarchyWalker.walk(getLayer(), new Layer.HierarchyWalker.Acceptor<Object>() {
                @Override
                public void accept(HierarchyWalker.Controller controller, Layer layer) {
                    Segment segment = layer.getSegmentByType(st);
                    if (segment != null) {
                        Module module = (Module) segment.getModules().findById(id);
                        if (module != null) {

                            if (findFirst) {
                                if (results.isEmpty()) {
                                    results.add(module);
                                } else {
                                    results.set(0, module);
                                }
                                controller.stop();
                            } else {
                                results.add(module);
                                controller.pathStop();
                            }
                        }
                    }
                }
            });
            return findFirst && (results.isEmpty() || results.get(0) == null) ? Collections.<Module>emptyList() : results;
            //  }
        } else {
            return segment.getLayer().getSegmentByType(segmentType).getModules().findById(segmentType, id, findFirst, resultSet);
        }
    }

    public Segment<T> getSegment() {
        return (Segment<T>) getContainer();
    }

    private static void saveModuleRecurs(Module module) throws IOException {
        final List<RadixObject> saveables = new ArrayList<>();

        module.visit(new IVisitor() {
            @Override
            public void accept(RadixObject radixObject) {
                if (radixObject.isSaveable()) {
                    saveables.add(radixObject);
                }
            }
        }, VisitorProviderFactory.createDefaultVisitorProvider());

        for (RadixObject saveable : saveables) {
            saveable.save();
        }
    }
    private boolean loading = false;

    // to simplify add when segment is untyped.
    @Override
    public void add(Module module) {
        assert (module.getSegmentType() == getSegment().getType());
        super.add((T) module);
    }

    @Override
    protected void onRemove(T definition) {
        super.onRemove(definition); //To change body of generated methods, choose Tools | Templates.
        modificationStamp = System.currentTimeMillis();
    }

    @Override
    protected void onAdd(T module) {
        super.onAdd(module);

        if (!loading && module.getDirectory() != null) {
            try {
                saveModuleRecurs(module);
            } catch (IOException cause) {
                throw new RadixObjectError("Unable to save module.", module, cause);
            }
        }
        modificationStamp = System.currentTimeMillis();
    }

    public T addFromRepository(final IRepositoryModule<T> moduleRepo) throws IOException {
        final Segment<T> segment = getSegment();
        final Module.Factory<T> moduleFactory = segment.getModuleFactory();
        final T module;

        try {
            module = moduleFactory.loadFromRepository(moduleRepo);

            if (this.findById(module.getId()) != null) {
                throw new IllegalStateException("Module with identifier '" + module.getId() + "' is already loaded.");
            }
            
            moduleRepo.setModule(module);
        } catch (IOException | IllegalStateException cause) {
            throw new IOException("Unable to load module from '" + moduleRepo.getPath() + "'", cause);
        }

        synchronized (this) {
            loading = true;
            try {
                super.add(module);
            } finally {
                loading = false;
            }
        }

        return module;
    }

    @Override
    public <R extends T> R overwrite(R sourceModule) {
        final Segment<T> segment = getSegment();

        if (sourceModule == null) {
            throw new DefinitionError("Source module for overwrite must not be null.", this);
        }
        if (sourceModule.getSegmentType() != segment.getType()) {
            throw new DefinitionError("Module segment type mismatch on overwriting.", sourceModule);
        }
        final Layer layer = segment.getLayer();
        if (!layer.isHigherThan(sourceModule.getSegment().getLayer())) {
            throw new DefinitionError("Overwritten module should be in the layer lower than current.", sourceModule);
        }
        T ext = findById(sourceModule.getId());
        if (ext == null) {
            final Module.Factory<T> moduleFactory = segment.getModuleFactory();

            ext = moduleFactory.overwrite(sourceModule);
            if (ext != null) {
                add(ext);
            }
        }
        return (R) ext;
    }

    @Override
    protected boolean isPersistent() {
        return false;
    }

    @Override
    protected CanPasteResult canPaste(List<Transfer> transfers, DuplicationResolver resolver) {
        return getSegment().getClipboardSupport().canPaste(transfers, resolver);
    }
}
