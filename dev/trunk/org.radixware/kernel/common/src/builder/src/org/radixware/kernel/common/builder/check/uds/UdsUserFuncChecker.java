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

package org.radixware.kernel.common.builder.check.uds;

import org.radixware.kernel.common.builder.check.common.RadixObjectChecker;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsUserPropertyDef;
import org.radixware.kernel.common.defs.uds.userfunc.UdsUserFuncDef;
import org.radixware.kernel.common.types.Id;


@RadixObjectCheckerRegistration
public class UdsUserFuncChecker extends RadixObjectChecker<UdsUserFuncDef> {

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return UdsUserFuncDef.class;
    }

    @Override
    public void check(UdsUserFuncDef radixObject, IProblemHandler problemHandler) {
        super.check(radixObject, problemHandler);
        AdsMethodDef method = radixObject.findMethod();
        if (method == null) {
            AdsClassDef baseClass = radixObject.findBaseClass();
            if (baseClass == null) {
                problemHandler.accept(RadixProblem.Factory.newError(radixObject, "Implemented class not found: #" + radixObject.getBaseClassId().toString()));
            } else {
//                boolean noNeedForMethod = false;
//                AdsClassDef clazz = baseClass;
//                while (clazz != null) {
//                    if (clazz.getId() == Id.Factory.loadFrom("aclO6J5TMLZSRD3NJRWZUT7F3EL5Y")) {
//                        noNeedForMethod = true;
//                        break;
//                    }
//                    clazz = clazz.getInheritance().findSuperClass().get();
//                }
                
                    problemHandler.accept(RadixProblem.Factory.newError(radixObject, "Implemented method not found in class " + baseClass.getQualifiedName()
                            + ": #" + radixObject.getMethodId().toString()));
                
            }
        }
        if (radixObject.getPropId() != null) {
            AdsUserPropertyDef prop = radixObject.findOwnerProperty();
            if (prop == null) {
                if (radixObject.getOwnerClassId() != null || radixObject.getOwnerEntityClassId() != null) {
                    AdsClassDef ownerClass = radixObject.findOwnerClass();
                    if (ownerClass == null) {
                        Id id = radixObject.getOwnerClassId() == null ?  radixObject.getOwnerEntityClassId() : radixObject.getOwnerClassId();
                        
                        problemHandler.accept(RadixProblem.Factory.newError(radixObject, "Owner class not found: #" + id.toString()));
                    } else {
                        problemHandler.accept(RadixProblem.Factory.newError(radixObject, "Owner property not found in class " + ownerClass.getQualifiedName()
                                + ": #" + radixObject.getPropId().toString()));
                    }
                }
            }
        }
    }
}
