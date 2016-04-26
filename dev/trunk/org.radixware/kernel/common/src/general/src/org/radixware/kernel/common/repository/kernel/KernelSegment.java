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

package org.radixware.kernel.common.repository.kernel;

import java.io.IOException;
import java.io.InputStream;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.defs.Module.Factory;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.repository.fs.IRepositoryModule;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;


public class KernelSegment extends Segment<KernelModule> {

    private final static class KernelModuleFactory extends Factory<KernelModule> {

        @Override
        public KernelModule newInstance(Id moduleId, String moduleName) {
            return new KernelModule(moduleId, moduleName);
        }

        public KernelModule loadFromRepository(IRepositoryModule<KernelModule> moduleRepo) throws IOException {
            final org.radixware.schemas.product.Module xModule;

            try (InputStream stream = moduleRepo.getDescriptionData()) {
                xModule = org.radixware.schemas.product.ModuleDocument.Factory.parse(stream).getModule();
            } catch (XmlException | IOException cause) {
                //may be missing module.xml in runtime
                if (moduleRepo instanceof FSRepositoryKernelModule) {
                    if (cause instanceof IOException) {
                        throw (IOException) cause;
                    }
                    throw new IOException("Unable to load module description.", cause);
                }

                final KernelModule module = newInstance(Id.Factory.newInstance(EDefinitionIdPrefix.MODULE), moduleRepo.getName());
                return module;
            }

            final Id moduleId = Id.Factory.loadFrom(xModule.getId());
            final String moduleName = moduleRepo.getName();
            final KernelModule module = newInstance(moduleId, moduleName);
            module.reloadDescriptionFromRepository(moduleRepo, xModule);
            return module;
        }
    }

    protected KernelSegment(Layer ownerLayer) {
        super(ownerLayer);
    }

    @Override
    protected Factory<KernelModule> getModuleFactory() {
        return new KernelModuleFactory();
    }

    @Override
    public ERepositorySegmentType getType() {
        return ERepositorySegmentType.KERNEL;
    }

    @Override
    public RadixIcon getIcon() {
        return RadixObjectIcon.KERNEL_SEGMENT;
    }
}
