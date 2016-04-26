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

import java.io.IOException;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.dds.DdsModule;
import org.radixware.kernel.common.defs.uds.module.UdsModule;
import org.radixware.kernel.common.repository.Segment;


abstract class ModuleUploader<M extends Module> extends RadixObjectUploader<M> {

    public ModuleUploader(M module) {
        super(module);
    }

    public M getModule() {
        return getRadixObject();
    }

    @Override
    public void close() {
        Module module = getModule();
        Segment segment = module.getSegment();
        if (segment != null) {
            // do not call module.delete(), because module directory will be deleted.
            segment.getModules().remove(module);
        }
    }

    @Override
    public long getRememberedFileTime() {
        return getModule().getFileLastModifiedTime();
    }

    @Override
    public void reload() throws IOException {
        Module module = getModule();
        module.reloadDescription();
    }

    public static final class Factory {

        private Factory() {
        }

        public static ModuleUploader newInstance(Module module) {
            if (module instanceof UdsModule) {
                return new UdsModuleUploader((UdsModule) module);
            } else if (module instanceof AdsModule) {
                return new AdsModuleUploader((AdsModule) module);
            } else if (module instanceof DdsModule) {
                return new DdsModuleUploader((DdsModule) module);
            } else {
                throw new IllegalStateException();
            }
        }
    }
}
