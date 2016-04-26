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

package org.radixware.kernel.common.repository.uds.fs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.uds.module.UdsModule;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.repository.ads.fs.FSRepositoryAdsSegment;
import org.radixware.kernel.common.repository.fs.FSRepositorySegment;
import org.radixware.kernel.common.repository.fs.IRepositoryModule;
import org.radixware.kernel.common.repository.uds.IRepositoryUdsSegment;
import org.radixware.kernel.common.utils.FileUtils;


public class FSRepositoryUdsSegment extends FSRepositoryAdsSegment implements IRepositoryUdsSegment{

    public FSRepositoryUdsSegment(Segment<AdsModule> segment) {
        super(segment);
    }

    @Override
    public IRepositoryModule<AdsModule> getModuleRepository(AdsModule module) {
        IRepositoryModule<AdsModule> cached = getCachedRepository(module);
        if (cached != null) {
            return cached;
        } else {
            if (module instanceof UdsModule) {
                return new FSRepositoryUdsModule((UdsModule) module);
            } else {
                return null;
            }
        }
    }

    @Override
    public ERepositorySegmentType getSegmentType() {
        return ERepositorySegmentType.UDS;
    }

    @Override
    public void processException(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public File getDirectory() {
        File file = super.getDirectory();
        if (file != null) {
            if (!file.exists()) {
                try {
                    FileUtils.mkDirs(file);
                } catch (IOException ex) {
                }
            }
        }
        return file;
    }

    @Override
    public InputStream getLayerDictionaryData(EIsoLanguage language) {
        File file = getLayerDictionaryFile(language);

        if (file.exists()) {
            try {
                return new FileInputStream(file);
            } catch (FileNotFoundException ex) {
                return null;
            }
        }
        return null;
    }

    @Override
    public File getLayerDictionaryFile(EIsoLanguage language) {
        String path;
        if (language != null) {
            path = getDirectory().getAbsolutePath() + "/dictionaries/" + language.getValue() + ".xml";
        } else {
            path = getDirectory().getAbsolutePath() + "/dictionaries/common.xml";
        }
        return new File(path);
    }
}
