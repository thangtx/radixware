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
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityGroupClassDef;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.JavaClassType;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;


@RadixObjectCheckerRegistration
public class AdsEntityGroupClassChecker extends AdsEntityBasedClassChecker<AdsEntityGroupClassDef> {

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return AdsEntityGroupClassDef.class;
    }

    @Override
    protected void checkSuperclassDetails(AdsEntityGroupClassDef clazz, AdsClassDef resolvedBaseClass, IProblemHandler problemHandler) {
        super.checkSuperclassDetails(clazz, resolvedBaseClass, problemHandler);
        if (!(resolvedBaseClass.getTransparence() != null && resolvedBaseClass.getTransparence().isTransparent() && AdsEntityGroupClassDef.PLATFORM_CLASS_NAME.equals(resolvedBaseClass.getTransparence().getPublishedName()))) {
            error(clazz, problemHandler, "Entity group class must be based on publishing of " + AdsEntityGroupClassDef.PLATFORM_CLASS_NAME);
        }
    }

    @Override
    protected void nullSuperclassReferenceDetails(AdsEntityGroupClassDef clazz, IProblemHandler problemHandler) {
        super.nullSuperclassReferenceDetails(clazz, problemHandler);
        error(clazz, problemHandler, "Entity group class must be based on publishing of " + AdsEntityGroupClassDef.PLATFORM_CLASS_NAME);
    }

    @Override
    protected void unresolvedSuperclassReferenceDetails(AdsEntityGroupClassDef clazz, AdsType ref, IProblemHandler problemHandler) {
        super.unresolvedSuperclassReferenceDetails(clazz, ref, problemHandler);

        AdsClassDef anc = findBaseHandler(clazz, AdsEntityGroupClassDef.PREDEFINED_ID);
        if (anc == null) {
            error(clazz, problemHandler, "Entity group class must be based on publishing of " + AdsEntityGroupClassDef.PLATFORM_CLASS_NAME);
        } else {
            error(clazz, problemHandler, "Entity group class must extend " + anc.getQualifiedName());
        }
    }
}
