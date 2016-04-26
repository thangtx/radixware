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

package org.radixware.kernel.reporteditor.repository;

import java.io.File;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.repository.fs.IRepositorySegment;
import org.radixware.kernel.common.userreport.extrepository.UserExtLayerRepository;


public abstract class ReportsSegmentRepository<T extends Module> implements IRepositorySegment<T> {

    private final UserExtLayerRepository layer;

    public ReportsSegmentRepository(final UserExtLayerRepository layer) {
        this.layer = layer;
    }

    @Override
    public File getDirectory() {
        return new File(layer.getDirectory(), getSegmentType().getValue());
    }

    @Override
    public void processException(final Throwable e) {
        layer.processException(e);
    }

    public abstract void close();

    public UserExtLayerRepository getLayerRepository() {
        return layer;
    }
}
