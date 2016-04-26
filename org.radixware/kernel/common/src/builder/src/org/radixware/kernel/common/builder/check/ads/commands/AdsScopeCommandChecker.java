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

package org.radixware.kernel.common.builder.check.ads.commands;

import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsScopeCommandDef;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;


@RadixObjectCheckerRegistration
public class AdsScopeCommandChecker<T extends AdsScopeCommandDef> extends AdsCommandChecker<T> {

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return AdsScopeCommandDef.class;
    }

    @Override
    public void check(final T command, final IProblemHandler problemHandler) {
        super.check(command, problemHandler);
        final AdsClassDef ownerClass = command.getOwnerClass();
        final EClassType classType = ownerClass.getClassDefType();
        switch (command.getScope()) {
            case GROUP:
                if (classType != EClassType.ENTITY_GROUP) {
                    error(command, problemHandler, "Group command should be declared in entity group handler class");
                }
                break;
            case OBJECT:
                if (classType != EClassType.APPLICATION && classType != EClassType.ENTITY && classType != EClassType.FORM_HANDLER && classType != EClassType.REPORT) {
                    error(command, problemHandler, "Object command should be declared in entity or form handler or report class");
                }
                break;
            case PROPERTY:
                if (classType != EClassType.APPLICATION && classType != EClassType.ENTITY && classType != EClassType.FORM_HANDLER && classType != EClassType.REPORT) {
                    error(command, problemHandler, "Property command should be declared in entity or form handler or report class");
                }
                break;
        }
        if (ownerClass instanceof AdsEntityObjectClassDef) {
            ERuntimeEnvironmentType ownerEnv = ((AdsEntityObjectClassDef) ownerClass).getClientEnvironment();
            if (ownerEnv != null) {
                if (command.getPresentation().getClientEnvironment() != ERuntimeEnvironmentType.COMMON_CLIENT && ownerEnv != ERuntimeEnvironmentType.COMMON_CLIENT && ownerEnv != command.getPresentation().getClientEnvironment()) {
                    error(command, problemHandler, "Client environment of command (" + command.getPresentation().getClientEnvironment().getName() + " ) does not match to it's owner class client environment (" + ownerEnv.getName() + ")");
                }
            }
        }
    }
}