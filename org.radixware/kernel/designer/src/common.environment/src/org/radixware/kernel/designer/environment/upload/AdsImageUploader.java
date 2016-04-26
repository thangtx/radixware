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
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.AdsImageDef;


class AdsImageUploader extends RadixObjectUploader<AdsImageDef> {

    public AdsImageUploader(AdsImageDef imageInfo) {
        super(imageInfo);
    }

    public AdsImageDef getImageDef() {
        return getRadixObject();
    }

    @Override
    public void close() {
        final AdsImageDef imageDef = getImageDef();
        final AdsModule module = imageDef.getModule();
        if (module != null) {
            // do not call imageDef.delete(), because its files will be deleted.
            module.getImages().remove(imageDef);
        }
    }

    @Override
    public long getRememberedFileTime() {
        return getImageDef().getFileLastModifiedTime();
    }

    @Override
    public void reload() throws IOException {
        final AdsImageDef imageDef = getImageDef();
        imageDef.reload();
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
