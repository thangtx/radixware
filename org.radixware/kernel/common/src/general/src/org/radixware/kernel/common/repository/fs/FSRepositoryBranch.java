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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;


public class FSRepositoryBranch implements IRepositoryBranch {

    private final File branchDir;
    private FileLock lockHandle = null;

    public FSRepositoryBranch(File branchDir) {
        this.branchDir = branchDir;
    }

    @Override
    public File getDescriptionFile() {
        return new File(branchDir, Branch.BRANCH_XML_FILE_NAME);
    }

    @Override
    public File getDirectory() {
        return branchDir;
    }

    @Override
    public InputStream getDescriptionData() throws FileNotFoundException {
        return new FileInputStream(getDescriptionFile());
    }

    @Override
    public IRepositoryLayer[] listLayers() {
        if (branchDir != null) {
            File[] layerDirs = branchDir.listFiles();

            if (layerDirs != null) {
                ArrayList<IRepositoryLayer> layers = new ArrayList<IRepositoryLayer>();
                for (File layerDir : layerDirs) {
                    if (Layer.isLayerDir(layerDir)) {
                        layers.add(new FSRepositoryLayer(layerDir));
                    }
                }
                return layers.toArray(new IRepositoryLayer[0]);
            }
        }
        return new IRepositoryLayer[0];
    }

    @Override
    public String getName() {
        return branchDir.getName();
    }
    private final Map<Layer, FSRepositoryLayer> cache = new WeakHashMap<Layer, FSRepositoryLayer>();

    @Override
    public IRepositoryLayer getLayerRepository(Layer layer) {
        synchronized (cache) {
            FSRepositoryLayer cached = cache.get(layer);
            if (cached != null) {
                return cached;
            }
        }
        return new FSRepositoryLayer(layer);
    }

    void keepInCache(FSRepositoryLayer layer, boolean keep) {
        synchronized (cache) {
            if (keep) {
                cache.put(layer.layer, layer);
            } else {
                cache.remove(layer.layer);
            }
        }
    }

    @Override
    public void lock() throws IOException {
        lock(-1);
    }

    @Override
    public boolean lock(int timeout) throws IOException {
        final long ts = System.currentTimeMillis();
        File file = getDescriptionFile();
        FileChannel fc = new RandomAccessFile(file, "rw").getChannel();

        for (int i = 0;; i++) {
            synchronized (this) {
                FileLock l = fc.tryLock(0L, Long.MAX_VALUE, false);
                if (l != null) {
                    if (i > 10) {
                        Logger.getLogger(FSRepositoryBranch.class.getName()).log(Level.FINE, "Lock done");
                    }
                    lockHandle = l;
                    return true;
                }
                if (i == 10) {
                    Logger.getLogger(FSRepositoryBranch.class.getName()).log(Level.FINE, "Try to lock {0}... ", file.getName());
                }
                if (timeout > 0) {
                    long timeSpent = System.currentTimeMillis() - ts;
                    if (timeSpent > timeout) {
                        return false;
                    }
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                }
            }
        }

    }

    @Override
    public void unlock() throws IOException {
        synchronized (this) {
            if (lockHandle != null) {
                lockHandle.release();
                lockHandle.channel().close();
                lockHandle = null;
            }
        }
    }

    @Override
    public void processException(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void close() {
    }

    @Override
    public boolean isBaseDevUriMeaningful() {
        return true;
    }
}
