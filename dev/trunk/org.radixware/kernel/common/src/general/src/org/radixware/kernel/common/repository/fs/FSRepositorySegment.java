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

package org.radixware.kernel.common.repository.fs;

import java.io.File;

import java.util.ArrayList;
import java.util.Map;
import java.util.WeakHashMap;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Segment;


public abstract class FSRepositorySegment<T extends Module> implements IRepositorySegment<T> {

    protected final Segment<T> segment;

    protected FSRepositorySegment(Segment<T> segment) {
        this.segment = segment;
    }

    public Segment<T> getSegment() {
        return this.segment;
    }

    @Override
    public IRepositoryModule[] listModules() {
        final ArrayList<IRepositoryModule> modules = new ArrayList<IRepositoryModule>();
        File sd = getDirectory();
        if (sd != null) {
            File[] moduleDirs = sd.listFiles();
            if (moduleDirs != null) {
                for (File moduleDir : moduleDirs) {
                    if (Module.isModuleDir(moduleDir)) {
                        modules.add(getModuleRepository(moduleDir));
                    }
                }
            }
        }
        return modules.toArray(new IRepositoryModule[0]);
    }

    @Override
    public File getDirectory() {
        Layer layer = segment.getLayer();
        if (layer != null) {
            File layerDir = segment.getLayer().getDirectory();
            if (layerDir != null) {
                File result = new File(layerDir, segment.getType().getValue());
                return result;
            }
        }
        return null;
    }

    public abstract IRepositoryModule<T> getModuleRepository(File moduleDir);
    private final Map<T, IRepositoryModule<T>> cache = new WeakHashMap<T, IRepositoryModule<T>>();

    @Override
    public IRepositoryModule<T> getModuleRepository(T module) {
        synchronized (cache) {
            return cache.get(module);
        }
    }

    void keepInCache(FSRepositoryModule<T> rep, boolean keep) {
        synchronized (cache) {
            if (keep) {
                cache.put(rep.module, rep);
            } else {
                cache.remove(rep.module);
            }
            ((FSRepositoryLayer) segment.getLayer().getRepository()).keepInCache(this, !cache.isEmpty());
        }
    }
}
