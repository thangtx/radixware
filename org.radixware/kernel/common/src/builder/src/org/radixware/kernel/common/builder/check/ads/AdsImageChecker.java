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

package org.radixware.kernel.common.builder.check.ads;

import java.io.File;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.module.AdsImageDef;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;


@RadixObjectCheckerRegistration
public class AdsImageChecker extends AdsDefinitionChecker<AdsImageDef> {

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return AdsImageDef.class;
    }

    @Override
    public void check(AdsImageDef imageDef, IProblemHandler problemHandler) {
        super.check(imageDef, problemHandler);

        final File imageFile = imageDef.getImageFile();
        if (imageFile == null || !imageFile.isFile()) {
            error(imageDef, problemHandler, "Image file not found");
        }
    }
}
