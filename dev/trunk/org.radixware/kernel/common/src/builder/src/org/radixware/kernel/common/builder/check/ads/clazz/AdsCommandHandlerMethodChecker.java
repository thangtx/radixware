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

import static org.radixware.kernel.common.builder.check.common.RadixObjectChecker.warning;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsCommandHandlerMethodDef;
import org.radixware.kernel.common.defs.ads.command.AdsCommandDef;
import org.radixware.kernel.common.defs.ads.command.AdsContextlessCommandDef;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.ads.clazz.members.ProfileUtilities;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsScopeCommandDef;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.ECommandScope;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodProblems;
import org.radixware.kernel.common.enums.ECommandNature;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.types.Id;


@RadixObjectCheckerRegistration
public class AdsCommandHandlerMethodChecker<T extends AdsCommandHandlerMethodDef> extends AdsUserMethodChecker<T> {

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return AdsCommandHandlerMethodDef.class;
    }

    @Override
    public void check(T method, IProblemHandler problemHandler) {
        super.check(method, problemHandler);
        AdsCommandDef command = method.findCommand();
        if (command == null) {
            if (method.getCommandId().getPrefix() == EDefinitionIdPrefix.COMMAND) {
                error(method, problemHandler, "Handled command not found in hierarchy of class " + method.getOwnerClass().getQualifiedName());
            } else {
                error(method, problemHandler, "Handled contextless command not found");
            }
        } else {
            if (method.getUsageEnvironment().isClientEnv()) {
                if (command.getPresentation().getClientEnvironment() != ERuntimeEnvironmentType.COMMON_CLIENT) {
                    if (method.getUsageEnvironment() != command.getPresentation().getClientEnvironment()) {
                        error(method, problemHandler, "Command handler usage environment (" + method.getUsageEnvironment().getName() + ") does not match to command client environment (" + command.getPresentation().getClientEnvironment().getName() + ")");
                    }
                } else {
                    if (method.getOwnerClass().getUsageEnvironment() == ERuntimeEnvironmentType.COMMON_CLIENT) {
                        if (method.getUsageEnvironment() != ERuntimeEnvironmentType.COMMON_CLIENT) {
                            ERuntimeEnvironmentType misEnv;
                            switch (method.getUsageEnvironment()) {
                                case WEB:
                                    misEnv = ERuntimeEnvironmentType.EXPLORER;
                                    break;
                                default:
                                    misEnv = ERuntimeEnvironmentType.WEB;
                            }

                            warning(method, problemHandler, AdsMethodProblems.MISSING_COMMAND_HANDLER_IMPLEMENTATION, new String[]{misEnv.getName(), command.getQualifiedName()});
                        }
                    }
                }
                AdsClassDef clazz = method.getOwnerClass();
                AdsClassDef commandModel = clazz.getNestedClasses().findById(Id.Factory.changePrefix(command.getId(), EDefinitionIdPrefix.ADS_COMMAND_MODEL_CLASS), ExtendableDefinitions.EScope.ALL).get();
                if (commandModel != null) {
                    if (commandModel.getOwnerClass() == clazz) {
                        warning(method, problemHandler, "Command " + command.getQualifiedName() + " is handled by command model class " + commandModel.getQualifiedName() + " so the method becomes unusable");
                    } else {
                        warning(method, problemHandler, "The method obstructs usage of command model class " + commandModel.getQualifiedName());
                    }
                }

            } else if (method.getUsageEnvironment() == ERuntimeEnvironmentType.SERVER) {
                if (command.getData().getNature() == ECommandNature.FORM_IN_OUT) {
                    problemHandler.accept(RadixProblem.Factory.newError(method, "Commands with form in input can not be handled by command owner class. Remove this method or modify command"));
                }
            }
            compareParametersWithCommand(method, command, problemHandler);
        }
    }

    private void compareParametersWithCommand(T method, AdsCommandDef command, IProblemHandler problemHandler) {
        if (method.getUsageEnvironment() == ERuntimeEnvironmentType.SERVER) {//server side class command handler should take parameters same with command input and output
            if (command instanceof AdsContextlessCommandDef) {
                problemHandler.accept(RadixProblem.Factory.newError(method, "Contextless command handlers are not allowed in serv side classes"));
            } else {
                checkProfileCompatibility(method, method.getProfile().getNormalizedProfile(), ProfileUtilities.commandHandlerProfile(command, ERuntimeEnvironmentType.SERVER), problemHandler);
            }
        } else {//client side class command handler takes pointer to command as only argument and returns command
            if (command instanceof AdsScopeCommandDef) {
                ECommandScope scope = ((AdsScopeCommandDef) command).getScope();
                EClassType classType = method.getOwnerClass().getClassDefType();
                switch (scope) {
                    case GROUP:
                        if (classType != EClassType.GROUP_MODEL) {
                            problemHandler.accept(RadixProblem.Factory.newError(method, "Group command handler must be in selector presentation model class only "));
                        }
                        break;
                    case OBJECT:
                        if (classType != EClassType.ENTITY_MODEL && classType != EClassType.FORM_MODEL) {
                            problemHandler.accept(RadixProblem.Factory.newError(method, "Object command handler must be in editor presentation or form model class only "));
                        }
                        break;
                    case PROPERTY:
                        if (classType != EClassType.ENTITY_MODEL && classType != EClassType.FORM_MODEL) {
                            problemHandler.accept(RadixProblem.Factory.newError(method, "Property command handler must be in editor presentation or form model class only "));
                        }
                        break;
                }
            }
            checkProfileCompatibility(method, method.getProfile().getNormalizedProfile(), ProfileUtilities.commandHandlerProfile(command, ERuntimeEnvironmentType.EXPLORER), problemHandler);
        }
    }
}
