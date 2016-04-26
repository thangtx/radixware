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

//import org.radixware.kernel.userext.repository.UserExtLayerRepository;
import org.radixware.kernel.common.defs.dds.DdsModule;
//import org.radixware.kernel.common.defs.dds.DdsModule;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.repository.dds.DdsSegmentFactory;
import org.radixware.kernel.common.repository.dds.fs.FSRepositoryDdsSegment;
//import org.radixware.kernel.common.repository.dds.DdsSegmentFactory;
//import org.radixware.kernel.common.repository.dds.fs.FSRepositoryDdsSegment;
import org.radixware.kernel.common.repository.fs.IRepositoryModule;


public class UserExtDDSSegmentRepository extends FSRepositoryDdsSegment {

    public UserExtDDSSegmentRepository(final UserExtLayerRepository layer) {
        super(DdsSegmentFactory.getDefault(ERepositorySegmentType.DDS).newSegment(layer.getLayer()));
    }

    @SuppressWarnings(value = {"rawtypes", "unchecked"})
    @Override
    public IRepositoryModule<DdsModule>[] listModules() {
        return new IRepositoryModule[0];
    }

    @Override
    public ERepositorySegmentType getSegmentType() {
        return ERepositorySegmentType.DDS;
    }

    @Override
    public IRepositoryModule<DdsModule> getModuleRepository(final DdsModule module) {
        return null;
    }
}
