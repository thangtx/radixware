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

package org.radixware.kernel.common.builder.check.ads.algo;

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.*;
import org.radixware.kernel.common.builder.check.ads.AdsDefinitionChecker;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.check.IProblemHandler;


@RadixObjectCheckerRegistration
public class AdsBaseObjectChecker<T extends AdsBaseObject> extends AdsDefinitionChecker<T> {

    public AdsBaseObjectChecker() {
    }

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return AdsBaseObject.class;
    }

    @Override
    public void check(T obj, IProblemHandler problemHandler) {
        super.check(obj, problemHandler);
        if (obj instanceof AdsAppObject) {
            final AdsAppObject app = (AdsAppObject)obj;
            app.syncProperties();
            //if (app.isUserObject() && app.getUserDef() == null) {
            //    error(app, problemHandler, "Can't find applied object definition #" + app.getClazz());
            //}
        }
    }
}
