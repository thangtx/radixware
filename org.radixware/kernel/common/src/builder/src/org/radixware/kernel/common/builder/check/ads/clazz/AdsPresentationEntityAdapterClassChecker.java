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
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsPresentationEntityAdapterClassDef;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;


@RadixObjectCheckerRegistration
public class AdsPresentationEntityAdapterClassChecker extends AdsEntityBasedClassChecker<AdsPresentationEntityAdapterClassDef> {

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return AdsPresentationEntityAdapterClassDef.class;
    }

    @Override
    public void check(AdsPresentationEntityAdapterClassDef clazz, IProblemHandler problemHandler) {
        super.check(clazz, problemHandler);
    }

    @Override
    protected void checkSuperclassDetails(AdsPresentationEntityAdapterClassDef clazz, AdsClassDef resolvedBaseClass, IProblemHandler problemHandler) {
        super.checkSuperclassDetails(clazz, resolvedBaseClass, problemHandler);
        if (resolvedBaseClass instanceof AdsPresentationEntityAdapterClassDef) {
            AdsPresentationEntityAdapterClassDef resolvedAdapter = (AdsPresentationEntityAdapterClassDef) resolvedBaseClass;
            AdsEntityClassDef rootBasis = clazz.findRootBasis();
            if (rootBasis != resolvedAdapter.findRootBasis()) {
                if (rootBasis != null) {
                    error(clazz, problemHandler, "Superclass must be based on the entity " + rootBasis.getQualifiedName());
                }
            } else {
                AdsEntityObjectClassDef clazzBasis = clazz.findBasis();
                AdsEntityObjectClassDef resolvedAdapterBasis = resolvedAdapter.findBasis();

                if (clazzBasis.getInheritance().findSuperClass().get() != resolvedAdapterBasis) {
                    error(clazz, problemHandler, "Basis of superclass must be superclass of class basis");
                }
            }
        } else {
            AdsEntityObjectClassDef clazzBasis = clazz.findBasis();
            if (clazzBasis instanceof AdsEntityClassDef) {
                if (!(resolvedBaseClass.getTransparence() != null && resolvedBaseClass.getTransparence().isTransparent() && AdsPresentationEntityAdapterClassDef.PLATFORM_ADAPTER_CLASS_NAME.equals(resolvedBaseClass.getTransparence().getPublishedName()))) {
                    error(clazz, problemHandler, "Presentation adapter of entity class should be based on " + AdsPresentationEntityAdapterClassDef.PLATFORM_ADAPTER_CLASS_NAME);
                }
            }
        }
    }

    @Override
    protected void nullSuperclassReferenceDetails(AdsPresentationEntityAdapterClassDef clazz, IProblemHandler problemHandler) {
        super.nullSuperclassReferenceDetails(clazz, problemHandler);
        error(clazz, problemHandler, "Presentation adapter of entity class extend presentation adapter of basis class or " + AdsPresentationEntityAdapterClassDef.PLATFORM_ADAPTER_CLASS_NAME);
    }

    @Override
    protected void unresolvedSuperclassReferenceDetails(AdsPresentationEntityAdapterClassDef clazz, AdsType ref, IProblemHandler problemHandler) {
        super.unresolvedSuperclassReferenceDetails(clazz, ref, problemHandler);
    }
}
