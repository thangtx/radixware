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

import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsRPCMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsRPCCommandDef;
import org.radixware.kernel.common.defs.ads.command.AdsCommandDef;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.types.Id;


@RadixObjectCheckerRegistration
public class AdsRPCMethodChecker extends AdsMethodChecker<AdsRPCMethodDef> {

    @Override
    public void check(AdsRPCMethodDef method, IProblemHandler problemHandler) {
        super.check(method, problemHandler);
        AdsCommandDef command = method.findCommand();
        if (command == null) {
            error(method, problemHandler, "Can not find command for remote call");
        } else {
            if (command instanceof AdsRPCCommandDef) {
                Id commandMethodId = ((AdsRPCCommandDef) command).getMethodId();
                if (commandMethodId != method.getServerSideMethodId()) {
                    error(method, problemHandler, "Server-side method, referenced from command " + command.getQualifiedName() + " is differs from server-side method of this remote call method");
                }
            } else {
                error(method, problemHandler, "Command " + command.getQualifiedName() + " is not suppots remote calls");
            }
        }
        AdsMethodDef ssMethod = method.findServerSideMethod();
        if (ssMethod == null) {
            AdsClassDef clazz = method.findServerSideClass();
            if (clazz == null) {
                error(method, problemHandler, "Can not find server-side class");
            } else {
                error(method, problemHandler, "Can not find server-side method #" + method.getServerSideMethodId() + " in class " + clazz.getQualifiedName());
            }

        } else {
            AdsTypeDeclaration[] types = method.getProfile().getNormalizedProfile();
            AdsTypeDeclaration[] ssTypes = ssMethod.getProfile().getNormalizedProfile();

            //profiles are compatible
            //check types
            boolean hasProfileErrors = false;
            for (int i = 0; i < ssTypes.length; i++) {
                if (!AdsRPCMethodDef.isSupportedServerType(ssMethod, ssTypes[i])) {
                    hasProfileErrors = true;
                    if (i == types.length - 1) {
                        error(method, problemHandler, "Return value type is not allowed for remote call method");

                    } else {
                        error(method, problemHandler, "Type of parameter " + ssMethod.getProfile().getParametersList().get(i).getName() + " is not allowed for remote call method");
                    }
                }
            }
            if (!hasProfileErrors) {
                for (int i = 0; i < ssTypes.length; i++) {
                    ssTypes[i] = AdsRPCMethodDef.convertServerType2ClientType(ssMethod, ssTypes[i], i == ssTypes.length - 1);
                }
                checkProfileCompatibility(method, types, ssTypes, problemHandler);
            }
        }
    }
}
