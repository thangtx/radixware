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
package org.radixware.kernel.common.userreport.extrepository;

import java.io.File;
import java.io.IOException;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.userreport.common.UserExtensionManagerCommon;

public class UserExtRepository {

    public static final UserExtRepository INSTANCE = new UserExtRepository();

    public static UserExtRepository getInstance() {
        return INSTANCE;
    }
    private UserExtLayerRepository layerRepository;
    private File projectDir;

    public void initialize(final File projectDir, final IClientEnvironment env) throws IOException {
        synchronized (this) {
            if (layerRepository != null) {
                return;
            }
            layerRepository = new UserExtLayerRepository(env, projectDir);
            this.projectDir = projectDir;
            UserExtensionManagerCommon.getInstance().initializeExtRepository(projectDir, env, layerRepository);
        }
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

    public UserExtAdsSegment getUserExtSegment() {
        if (layerRepository == null) {
            return null;
        }
        return (UserExtAdsSegment) layerRepository.getLayer().getAds();
    }

    public void upload() {
        UserExtensionManagerCommon.getInstance().uploadExtRepository(layerRepository);
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
