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

import java.text.MessageFormat;
import org.radixware.kernel.common.defs.ads.command.AdsCommandDef;
import org.radixware.kernel.common.defs.ads.command.AdsCommandDef.CommandData;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.enums.ECommandNature;
import org.radixware.kernel.common.builder.check.ads.CheckUtils;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.XmlType;
import org.radixware.kernel.common.builder.check.ads.AdsDefinitionChecker;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.form.AdsFormHandlerClassDef;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.defs.ads.type.AdsDefinitionType;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;


@RadixObjectCheckerRegistration
public class AdsCommandChecker<T extends AdsCommandDef> extends AdsDefinitionChecker<T> {

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return AdsCommandDef.class;
    }

    private void checkType(final AdsCommandDef command, final AdsTypeDeclaration type, final boolean isInput, final IProblemHandler problemHandler) {
        final ECommandNature nature = command.getData().getNature();
        switch (nature) {
            case FORM_IN_OUT:
                if (isInput) {
                    final AdsType reference = type.resolve(command).get(AdsDefinitionChecker.<AdsType>getSearchDuplicatesChecker(command, problemHandler));
                    if (reference instanceof AdsDefinitionType) {
                        final Definition def = ((AdsDefinitionType) reference).getSource();
                        if (def instanceof AdsDefinition) {
                            AdsUtils.checkAccessibility(command, (AdsDefinition) def, false, problemHandler);
                            CheckUtils.checkExportedApiDatails(command, (AdsDefinition) def, problemHandler);
                            if (def instanceof AdsFormHandlerClassDef) {
                                final AdsFormHandlerClassDef clazz = (AdsFormHandlerClassDef) def;
                                final ERuntimeEnvironmentType clazzEnv = clazz.getClientEnvironment();
                                final ERuntimeEnvironmentType cmdEnv = command.getClientEnvironment();
                                if (clazzEnv != cmdEnv && clazzEnv != ERuntimeEnvironmentType.COMMON_CLIENT) {
                                    error(command, problemHandler, cmdEnv.getName() + " command can not use " + clazzEnv.getName() + " form as input");
                                }
                            }
                        }
                    }
                    if (reference == null) {
                        error(command, problemHandler, "Command input type can not be resolved");
                    } else {
                        if (!(reference instanceof AdsClassType.FormHandlerType)) {
                            error(command, problemHandler, MessageFormat.format("Form based class expected as command input type but got {0}", reference.getQualifiedName(null)));
                        }
                    }
                }
                break;
            case LOCAL:
                //no input output values expected.both types should be void;
                if (type != AdsTypeDeclaration.Factory.voidType()) {
                    if (isInput) {
                        error(command, problemHandler, "Command input type should be void");
                    } else {
                        error(command, problemHandler, "Command output type should be void");
                    }
                }
                break;
            case XML_IN_FORM_OUT:
                if (isInput) {
                    if (type != AdsTypeDeclaration.Factory.voidType()) {
                        final AdsType reference = type.resolve(command).get(AdsDefinitionChecker.<AdsType>getSearchDuplicatesChecker(command, problemHandler));
                        if (reference == null) {
                            error(command, problemHandler, "Command input type can not be resolved");
                        } else {
                            if (reference instanceof AdsDefinitionType) {
                                Definition def = ((AdsDefinitionType) reference).getSource();
                                if (def instanceof AdsDefinition) {
                                    AdsUtils.checkAccessibility(command, (AdsDefinition) def, false, problemHandler);
                                    CheckUtils.checkExportedApiDatails(command, (AdsDefinition) def, problemHandler);
                                }
                            }
                            if (!(reference instanceof XmlType)) {
                                error(command, problemHandler, MessageFormat.format("Xml based class expected as command input type but got {0}", reference.getQualifiedName(null)));
                            }
                        }
                    }
                }
                break;
            case XML_IN_OUT:
                if (type != AdsTypeDeclaration.Factory.voidType()) {

                    final AdsType reference = type.resolve(command).get(AdsDefinitionChecker.<AdsType>getSearchDuplicatesChecker(command, problemHandler));
                    if (reference == null) {
                        error(command, problemHandler, isInput ? "Command input type can not be resolved" : "Command output type can not be resolved");
                    } else {
                        if (reference instanceof AdsDefinitionType) {
                            Definition def = ((AdsDefinitionType) reference).getSource();
                            if (def instanceof AdsDefinition) {
                                AdsUtils.checkAccessibility(command, (AdsDefinition) def, false, problemHandler);
                                CheckUtils.checkExportedApiDatails(command, (AdsDefinition) def, problemHandler);
                            }
                        }
                        if (!(reference instanceof XmlType)) {
                            error(command, problemHandler, MessageFormat.format("Xml based class expected as command {1} type but got {0}", reference.getQualifiedName(null), isInput ? "input" : "output"));
                        }
                    }
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void check(final T command, final IProblemHandler problemHandler) {
        super.check(command, problemHandler);

        CheckUtils.checkIconId(command, command.getPresentation().getIconId(), problemHandler, "icon");
        CheckUtils.checkMLStringId(command, command.getTitleId(), problemHandler, "title");
        final CommandData data = command.getData();

        checkType(command, data.getInType(), true, problemHandler);
        checkType(command, data.getOutType(), false, problemHandler);
    }
}
