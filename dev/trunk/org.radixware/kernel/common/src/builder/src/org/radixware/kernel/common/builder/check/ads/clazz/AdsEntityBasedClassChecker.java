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

import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityBasedClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;


public abstract class AdsEntityBasedClassChecker<T extends AdsEntityBasedClassDef> extends AdsClassChecker<T> {

    @Override
    public void check(T clazz, IProblemHandler problemHandler) {
        super.check(clazz, problemHandler);
        if (clazz.findTable(clazz) == null) {
            error(clazz, problemHandler, "Can not find DDS table for entity based class");
        }
        if (!(clazz instanceof AdsEntityClassDef)) {
            if (clazz.findBasis() == null) {
                error(clazz, problemHandler, "Can not find basis class (Application or Entity class expected)");
            } else if (clazz.findRootBasis() == null) {
                error(clazz, problemHandler, "Can not find root basis class (Entity class expected)");
            }
        }
    }
}
