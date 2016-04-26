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
package org.radixware.kernel.common.builder.api.userext;

import java.io.File;
import java.io.IOException;
import org.radixware.kernel.common.repository.Branch;

public class UDSRepository {

    public static final UDSRepository INSTANCE = new UDSRepository();

    public static UDSRepository getInstance() {
        return INSTANCE;
    }
    private UDSLayerRepository layerRepository;
    private File projectDir;

    public void initialize(Branch branch, final File projectDir, final UDSDefCustomLoader loader) throws IOException {
        synchronized (this) {
            if (layerRepository != null) {
                return;
            }
            layerRepository = new UDSLayerRepository(projectDir, new File[1], branch, "User-Extensions", "user.extensions", loader);
            this.projectDir = projectDir;
            

        }
    }

    public static UDSRepository newInstance(Branch branch, final File projectDir, final UDSDefCustomLoader loader) throws IOException {
        UDSRepository repo = new UDSRepository();
        repo.initialize(branch, projectDir, loader);
        return repo;
    }

    public File getProjectDir() {
        return projectDir;
    }

    public Branch getBranch() {
        if (layerRepository != null) {
            return layerRepository.getBranch();
        } else {
            return null;
        }
    }

    public UDSAdsSegment getUserExtSegment() {
        if (layerRepository == null) {
            return null;
        }
        return (UDSAdsSegment) layerRepository.getLayer().getAds();
    }

    public void upload() {

    }

    public void cleanup() {
        synchronized (this) {
            if (layerRepository != null) {
                layerRepository.cleanup();
                layerRepository = null;
            }
        }
    }

    public void close() {
        synchronized (this) {
            if (layerRepository != null) {
                layerRepository.close();
                layerRepository = null;
            }
        }
    }
}
