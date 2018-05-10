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

package org.radixware.kernel.common.defs.ads.clazz.members;

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.IDependentId;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;

import org.radixware.kernel.common.defs.ads.clazz.form.AdsFormHandlerClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsGroupCommandDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsPropertyCommandDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsScopeCommandDef;
import org.radixware.kernel.common.defs.ads.command.AdsCommandDef;
import org.radixware.kernel.common.defs.ads.platform.IPlatformClassPublisher;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.ECommandNature;
import org.radixware.kernel.common.enums.EMethodNature;

import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.exceptions.RadixPublishedException;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.jml.JmlTagInvocation;
import org.radixware.kernel.common.jml.JmlTagTypeDeclaration;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.AbstractMethodDefinition;


public class AdsCommandHandlerMethodDef extends AdsUserMethodDef implements IDependentId {

    public static final class Factory {

        public static AdsCommandHandlerMethodDef newInstance(AdsCommandDef command) {
            return new AdsCommandHandlerMethodDef(command);
        }

        static AdsCommandHandlerMethodDef newInstance(AdsCommandHandlerMethodDef source, boolean overwrite) {
            return new AdsCommandHandlerMethodDef(source, overwrite);
        }
    }
    private final MethodCommandProvider commandProvider = new MethodCommandProvider(this) {

        @Override
        protected Id getCommandId() {
            return AdsCommandHandlerMethodDef.this.getCommandId();
        }
    };

    private AdsCommandHandlerMethodDef(AdsCommandDef command) {
        super(command.getHandlerId(), getCommandHandlerName(command));

    }

    private AdsCommandHandlerMethodDef(AdsCommandHandlerMethodDef source, boolean overwrite) {
        super(source, overwrite);
    }

    AdsCommandHandlerMethodDef(AbstractMethodDefinition xMethod) {
        super(xMethod);
    }

    public static String getCommandHandlerName(AdsCommandDef command) {
        return "onCommand_" + command.getName();
    }

    @Override
    public EMethodNature getNature() {
        return EMethodNature.COMMAND_HANDLER;
    }

    public Id getCommandId() {
        return Id.Factory.loadFrom(getId().toString().substring(3));
    }

    public AdsCommandDef findCommand() {
        return commandProvider.findCommand();
    }

    @Override
    public void collectDependences(final List<Definition> list) {
        super.collectDependences(list);
        final AdsCommandDef command = findCommand();
        if (command != null) {
            list.add(command);
        }
    }

    public static void computeProfile(final AdsCommandDef command, final ERuntimeEnvironmentType env, final List<AdsTypeDeclaration> profile, final List<String> argumentNames) {
        if (command == null) {
            throw new IllegalArgumentException("command is null");
        }
        final AdsTypeDeclaration commandOutputType = command.getData().getOutType();
        final AdsTypeDeclaration commandInputType = command.getData().getInType();
        final AdsTypeDeclaration returnType;
        if (env == null) {
            throw new IllegalArgumentException("");
        }
        switch (env) {
            case EXPLORER:
            case COMMON_CLIENT:
            case WEB:
                profile.add(AdsTypeDeclaration.Factory.newInstance(command));
                argumentNames.add("command");
                if (command instanceof AdsPropertyCommandDef) {
                    profile.add(AdsTypeDeclaration.Factory.newPlatformClass("org.radixware.kernel.common.types.Id"));
                    argumentNames.add("propertyId");
                }
                returnType = AdsTypeDeclaration.VOID;

                profile.add(returnType);
                break;
            case SERVER:
                if (command instanceof AdsScopeCommandDef) {

                    switch (command.getData().getNature()) {
                        case FORM_IN_OUT:
                        case XML_IN_FORM_OUT:
                            returnType = AdsTypeDeclaration.Factory.newPlatformClass(AdsFormHandlerClassDef.PLATFORM_NEXT_REQUEST_CLASS_NAME);
                            break;
                        default:
                            returnType = commandOutputType == null ? AdsTypeDeclaration.VOID : commandOutputType;
                    }
                    if (command instanceof AdsPropertyCommandDef) {
                        profile.add(AdsTypeDeclaration.Factory.newPlatformClass("org.radixware.kernel.common.types.Id"));
                        argumentNames.add("propId");
                    }
                    if (commandInputType != null && commandInputType != AdsTypeDeclaration.VOID) {
                        profile.add(commandInputType);
                        argumentNames.add("input");
                    }

                    if (!(command instanceof AdsGroupCommandDef) && ((AdsScopeCommandDef) command).getOwnerClass().getClassDefType() != EClassType.FORM_HANDLER) {
                        profile.add(AdsTypeDeclaration.Factory.newPlatformClass("org.radixware.kernel.server.types.PropValHandlersByIdMap"));
                        argumentNames.add("newPropValHandlersById");
                    }
                    profile.add(returnType);
                }
                break;
            default:
                throw new IllegalArgumentException(env.name());
        }
    }

    public void updateProfile() throws RadixPublishedException {
        if (getContainer() == null) {
            throw new RadixPublishedException("Can not determine usage context");
        }
        final AdsCommandDef command = findCommand();
        if (command != null) {
            final ArrayList<AdsTypeDeclaration> types = new ArrayList<>();
            final ArrayList<String> names = new ArrayList<>();
            computeProfile(command, getUsageEnvironment(), types, names);
            this.getProfile().getParametersList().clear();
            for (int i = 0, len = names.size(); i < len; i++) {
                this.getProfile().getParametersList().add(MethodParameter.Factory.newInstance(names.get(i), types.get(i)));
            }
            this.getProfile().getReturnValue().setType(types.get(types.size() - 1));
            setName(getCommandHandlerName(command));
            fireNameChange();
        } else {
            throw new RadixPublishedException("Handled command not found");
        }

    }

    public void setUpAutoCode() {

        AdsCommandDef command = findCommand();
        if (command == null) {
            return;
        }
        if (getUsageEnvironment() == null || !getUsageEnvironment().isClientEnv()) {
            return;
        }

        if (command.getData().getNature() == ECommandNature.XML_IN_FORM_OUT || command.getData().getNature() == ECommandNature.XML_IN_OUT) {
            Jml src = getSource();
            src.getItems().add(Scml.Text.Factory.newInstance("try{\n    "));

            AdsTypeDeclaration in = command.getData().getInType();
            if (in != null && in != AdsTypeDeclaration.VOID && in != AdsTypeDeclaration.UNDEFINED) {
                src.getItems().add(new JmlTagTypeDeclaration(in));
                src.getItems().add(Scml.Text.Factory.newInstance(" xIn = null;//TODO:\n    "));
            }


            AdsTypeDeclaration out = command.getData().getOutType();
            if (out != null && out != AdsTypeDeclaration.VOID && out != AdsTypeDeclaration.UNDEFINED) {
                if(command.getData().getNature() != ECommandNature.XML_IN_FORM_OUT){
                    src.getItems().add(new JmlTagTypeDeclaration(out));
                    src.getItems().add(Scml.Text.Factory.newInstance(" xOut = "));
                }
            }

            src.getItems().add(Scml.Text.Factory.newInstance("command.send("));
            boolean hasInput = false;
            if (in != null && in != AdsTypeDeclaration.VOID && in != AdsTypeDeclaration.UNDEFINED) {
                src.getItems().add(Scml.Text.Factory.newInstance("xIn"));
                hasInput = true;
            }
            if (command instanceof AdsPropertyCommandDef) {
                if (getProfile().getParametersList().size() == 2) {
                    if (hasInput) {
                        src.getItems().add(Scml.Text.Factory.newInstance(","));
                    }
                    src.getItems().add(Scml.Text.Factory.newInstance(getProfile().getParametersList().get(1).getName()));
                }
            }
            src.getItems().add(Scml.Text.Factory.newInstance(");\n"));

            String intename = "java.lang.InterruptedException";
            IPlatformClassPublisher inte = ((AdsSegment) getModule().getSegment()).getBuildPath().getPlatformPublishers().findPublisherByName(intename);
            if (inte instanceof AdsClassDef) {
                getModule().getDependences().add(((AdsClassDef) inte).getModule());
            } else {
                inte = null;
            }
            src.getItems().add(Scml.Text.Factory.newInstance("}catch("));
            if (inte == null) {
                src.getItems().add(Scml.Text.Factory.newInstance(intename));
            } else {
                src.getItems().add(new JmlTagTypeDeclaration(AdsTypeDeclaration.Factory.newInstance((AdsClassDef) inte)));
            }
            src.getItems().add(Scml.Text.Factory.newInstance("  e){\n    "));

            String name = String.valueOf(WriterUtils.EXPLORER_MODEL_CLASS_NAME);
            AdsMethodDef showExceptionPublisher = null;
            IPlatformClassPublisher baseModel = ((AdsSegment) getModule().getSegment()).getBuildPath().getPlatformPublishers().findPublisherByName(name);
            if (baseModel instanceof AdsClassDef) {
                showExceptionPublisher = ((AdsClassDef) baseModel).getMethods().findBySignature("showException(Ljava.lang.Throwable;)V".toCharArray(), EScope.ALL);
                if (showExceptionPublisher != null) {
                    getModule().getDependences().add(((AdsClassDef) baseModel).getModule());
                }
            } else {
                baseModel = null;
            }

            if (showExceptionPublisher == null) {
                src.getItems().add(Scml.Text.Factory.newInstance("    showException(e);\n"));
            } else {
                src.getItems().add(Scml.Text.Factory.newInstance("    "));
                src.getItems().add(JmlTagInvocation.Factory.newInstance(showExceptionPublisher));
                src.getItems().add(Scml.Text.Factory.newInstance("(e);\n"));
            }


            String scename = "org.radixware.kernel.common.exceptions.ServiceClientException";
            IPlatformClassPublisher sce = ((AdsSegment) getModule().getSegment()).getBuildPath().getPlatformPublishers().findPublisherByName(scename);
            if (sce instanceof AdsClassDef) {
                getModule().getDependences().add(((AdsClassDef) sce).getModule());
            } else {
                sce = null;
            }
            src.getItems().add(Scml.Text.Factory.newInstance("}catch("));
            if (sce == null) {
                src.getItems().add(Scml.Text.Factory.newInstance(scename));
            } else {
                src.getItems().add(new JmlTagTypeDeclaration(AdsTypeDeclaration.Factory.newInstance((AdsClassDef) sce)));
            }
            src.getItems().add(Scml.Text.Factory.newInstance("  e){\n    "));
            /*if (env == null) {
            src.getItems().add(Scml.Text.Factory.newInstance(name));
            } else {
            src.getItems().add(new JmlTagTypeDeclaration(AdsTypeDeclaration.Factory.newInstance((AdsClassDef) env)));
            }
            src.getItems().add(Scml.Text.Factory.newInstance(".processException(e);\n"));*/
            if (showExceptionPublisher == null) {
                src.getItems().add(Scml.Text.Factory.newInstance("    showException(e);\n"));
            } else {
                src.getItems().add(Scml.Text.Factory.newInstance("    "));
                src.getItems().add(JmlTagInvocation.Factory.newInstance(showExceptionPublisher));
                src.getItems().add(Scml.Text.Factory.newInstance("(e);\n"));
            }
            src.getItems().add(Scml.Text.Factory.newInstance("}"));
        }
    }
}