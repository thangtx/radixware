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

import java.io.File;
import java.io.IOException;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.module.AdsModule;


class AdsDefinitionUploader extends RadixObjectUploader<AdsDefinition> {

    public AdsDefinitionUploader(AdsDefinition radixObject) {
        super(radixObject);
    }

    public AdsDefinition getDefinition() {
        return getRadixObject();
    }

    @Override
    public void close() {
        AdsDefinition definition = getDefinition();
        AdsModule module = definition.getModule();
        if (module != null) {
            // do not call definition.delete(), because definition file will be deleted.
            module.getDefinitions().remove(definition);
        }
    }

    @Override
    public long getRememberedFileTime() {
        return getDefinition().getFileLastModifiedTime();
    }

    @Override
    public void reload() throws IOException {
        AdsDefinition oldDefinition = getDefinition();
        AdsModule module = oldDefinition.getModule();
        module.getDefinitions().reload(oldDefinition);
    }

    // ===========================================
    @Override
    public RadixObject uploadChild(File file) throws IOException {
        throw new IllegalStateException();
    }

    @Override
    public void updateChildren() {
        // NOTHING
    }
}
