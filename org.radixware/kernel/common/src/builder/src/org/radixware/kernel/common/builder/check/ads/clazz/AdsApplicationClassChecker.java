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
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsApplicationClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.defs.dds.DdsTableDef;


@RadixObjectCheckerRegistration
public class AdsApplicationClassChecker extends AdsEntityObjectClassChecker<AdsApplicationClassDef> {

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return AdsApplicationClassDef.class;
    }

    @Override
    protected void checkSuperclassDetails(AdsApplicationClassDef clazz, AdsClassDef resolvedBaseClass, IProblemHandler problemHandler) {
        super.checkSuperclassDetails(clazz, resolvedBaseClass, problemHandler);
        if (resolvedBaseClass instanceof AdsEntityClassDef || resolvedBaseClass instanceof AdsApplicationClassDef) {
            AdsEntityObjectClassDef resolvedEntityClass = (AdsEntityObjectClassDef) resolvedBaseClass;
            if (resolvedEntityClass.findRootBasis() != clazz.findRootBasis()) {
                error(clazz, problemHandler, "Superclass must be based on the same entity with the class");
            }
            if (resolvedEntityClass != clazz.findBasis()) {
                error(clazz, problemHandler, "Superclass must be a basis class too");
            }
        }
    }

    @Override
    protected void nullSuperclassReferenceDetails(AdsApplicationClassDef clazz, IProblemHandler problemHandler) {
        super.nullSuperclassReferenceDetails(clazz, problemHandler);
        error(clazz, problemHandler, "Application class must extend Entity or Application class");
    }

    @Override
    public void check(AdsApplicationClassDef clazz, IProblemHandler problemHandler) {
        super.check(clazz, problemHandler);
        if (!clazz.isPolymorphic()) {
            DdsTableDef table = clazz.findTable(clazz);
            String tableName = table == null ? "#" + clazz.getEntityId().toString() : table.getQualifiedName();
            error(clazz, problemHandler, "Application classes are not allowed for table " + tableName);
        }
    }
}
