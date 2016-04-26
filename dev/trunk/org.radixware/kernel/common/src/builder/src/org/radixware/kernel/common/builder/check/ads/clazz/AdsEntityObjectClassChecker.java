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

package org.radixware.kernel.common.builder.check.ads.clazz;

import java.util.List;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsApplicationClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.utils.Utils;


public abstract class AdsEntityObjectClassChecker<T extends AdsEntityObjectClassDef> extends AdsEntityBasedClassChecker<T> {

    @Override
    public void check(T clazz, IProblemHandler problemHandler) {
        super.check(clazz, problemHandler);

        boolean isTestClass = Utils.equals(AdsApplicationClassDef.TEST_CASE_CLASS_ID, clazz.getEntityId());
        if (isTestClass) {
            Module module = clazz.getModule();
            if (!module.isTest()) {
                warning(clazz, problemHandler, "Test class must be in test module");
            }
        }

        List<AdsEntityObjectClassDef.DetailReferenceInfo> refs = clazz.getAllowedDetailRefs();
        if (refs != null) {
            for (AdsEntityObjectClassDef.DetailReferenceInfo ref : refs) {
                if (ref.findReference() == null) {
                    error(clazz, problemHandler, "Unknown reference in allowed details list. Id: #" + ref.getReferenceId().toString());
                }
            }
        }                
    }
}
