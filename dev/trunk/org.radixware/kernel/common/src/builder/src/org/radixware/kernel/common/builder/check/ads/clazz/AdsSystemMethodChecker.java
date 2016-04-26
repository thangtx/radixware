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
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsSystemMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.MethodParameter;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;


@RadixObjectCheckerRegistration
public class AdsSystemMethodChecker extends AdsMethodChecker<AdsSystemMethodDef> {

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return AdsSystemMethodDef.class;
    }

    @Override
    public void check(AdsSystemMethodDef method, IProblemHandler problemHandler) {
        super.check(method, problemHandler);

        if (method.getId() == AdsSystemMethodDef.ID_LOAD_BY_PID_STR || method.getId() == AdsSystemMethodDef.ID_LOAD_BY_PK) {
            AdsSystemMethodDef.ArgumentsProvider ap = method.getArgumentsProvider();
            if (!ap.isValid()) {
                problemHandler.accept(RadixProblem.Factory.newError(method, ap.getErrorMessage()));
            } else {
                List<AdsSystemMethodDef.ArgumentsProvider.ArgumentInfo> args = ap.getParameterInfos();
                AdsTypeDeclaration[] expectedProfile = new AdsTypeDeclaration[args.size() + 1];
                for (int i = 0; i < args.size(); i++) {
                    expectedProfile[i] = args.get(i).type;
                }
                expectedProfile[expectedProfile.length - 1] = ap.getReturnValueInfo().type;
                if (checkProfileCompatibility(method, method.getProfile().getNormalizedProfile(), expectedProfile, problemHandler)) {
                    if (method.getId() == AdsSystemMethodDef.ID_LOAD_BY_PK) {
                        int index = 0;
                        for (MethodParameter p : method.getProfile().getParametersList()) {
                            String name = ap.getParameterInfos().get(index).name;
                            if (!name.equals(p.getName())) {
                                problemHandler.accept(RadixProblem.Factory.newError(p, "Incorrect name for parameter " + index + ": " + p.getName() + " should be " + name));

                            }
                            index++;
                        }
                    }
                }
            }
        } 

    }
}
