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
package org.radixware.kernel.common.repository.ads.fs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.module.AdsImageDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.enums.ERadixIconType;

public class FSRepositoryAdsImageDefinition extends FSRepositoryAdsDefinition implements IRepositoryAdsImageDefinition {

    public FSRepositoryAdsImageDefinition(AdsImageDef def) {
        super(def, false);
    }

    public FSRepositoryAdsImageDefinition(File file) {
        super(file);
    }

    @Override
    public InputStream getImageData() throws FileNotFoundException {
        File file = getImageFile();
        return file == null ? null : new FileInputStream(file);
    }

    @Override
    public String getImageFileName() {
        File file = getImageFile();
        return file == null ? null : file.getName();
    }

    @Override
    public File getImageFile() {
        File file = getFile();
        if (file == null) {
            return null;
        } else {
            File imagesDir = file.getParentFile();
            for (ERadixIconType type : ERadixIconType.values()) {
                final String ext = type.getValue();
                final String name = getId().toString() + "." + ext;
                final File imageFile = new File(imagesDir, name);
                if (imageFile.exists()) {
                    return imageFile;
                }
            }
            return null;
        }
    }

    @Override
    protected String getSrcDirShortName(ESaveMode saveMode) {
        return AdsModule.IMAGES_DIR_NAME;
    }

    @Override
    public void processException(Throwable e) {
        e.printStackTrace();
    }
}
